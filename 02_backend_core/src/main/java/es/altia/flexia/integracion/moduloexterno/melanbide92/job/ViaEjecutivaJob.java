package es.altia.flexia.integracion.moduloexterno.melanbide92.job;

import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.melanbide92.dao.MeLanbide92DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide92.manager.MeLanbide92Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide92.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide92.util.ConstantesMeLanbide92;
import es.altia.flexia.integracion.moduloexterno.melanbide92.util.MeLanbide92Utilidades;
import es.altia.flexia.integracion.moduloexterno.melanbide92.vo.ProcedimientoLireiVO;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExternoCampoSupFactoria;
import es.altia.flexia.integracion.moduloexterno.plugin.camposuplementario.IModuloIntegracionExternoCamposFlexia;
import es.altia.flexia.integracion.moduloexterno.plugin.camposuplementario.ModuloIntegracionExternoCamposFlexia;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.SalidaIntegracionVO;
import es.altia.technical.PortableContext;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javax.sql.DataSource;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;

/**
 *
 * @author kepa.gonzalez
 */
public class ViaEjecutivaJob implements Job {

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        try {
            MeLanbide92DAO m92DAO = MeLanbide92DAO.getInstance();
            MeLanbide92Manager m92Manager = MeLanbide92Manager.getInstance();

            String resultadoJob = "OK";
            String mensajeErrorJob = "";
            String detalleError = "";
            int registrosOK = 0;
            int registrosKO = 0;
            Integer idSecuencia = 0;

            String servidor = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_SERVIDOR, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
            log.debug("servidor " + servidor);
            log.debug("weblogic.Name " + System.getProperty("weblogic.Name"));
            if (servidor.equals(System.getProperty("weblogic.Name"))) {//PARA LOCAL QUITAR
                synchronized (jec) {
                    AdaptadorSQLBD adapt = null;
                    Connection con = null;
                    try {
                        log.info("ViaEjecutivaJob() - Execute lanzado " + System.getProperty("weblogic.Name"));

                        int codOrg = Integer.parseInt(ConfigurationParameter.getParameter("COD_ORG", ConstantesMeLanbide92.FICHERO_PROPIEDADES));
                        adapt = this.getAdaptSQLBD(String.valueOf(codOrg));
                        if (codOrg < 2) {
                            con = this.getAdaptSQLBD(String.valueOf(codOrg)).getConnection();
                        }
                            idSecuencia = m92Manager.insertarRegistroProcesosProgramadosEjec(new Date(), adapt);
                        String codCampoFecResol = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_FEC_RESOLUCION, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
                        String codCampoFecAcuseRes = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_FEC_ACUSE_NOTIF_RESOL, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
                        String codCampoFecLimite = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_FEC_VENCIMIENTO_CARTA_RES, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
                        String codCampoTipoNotif = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_TIPO_NOTIF_RESOL, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
                        String codCampoNumLiquidacion = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_ID_DEUDA_RES, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
                            List<ProcedimientoLireiVO> listaProcedimientos = m92Manager.getTramitesProcedimientos(adapt);
                            for (ProcedimientoLireiVO procedimiento : listaProcedimientos) {
                                String codProc = procedimiento.getCodProcedimiento();
                            log.info("ViaEjecutivaJob() - Procedimiento: " + codProc);
                                int codTramResolucion = procedimiento.getResolucion();
                                int codTramAcuseRes = procedimiento.getAcuseRes();
                                int codTramEsperaPago = procedimiento.getPago();
                                int codTramEjecutiva = procedimiento.getEjecutiva();
                                int codTramFinReint = procedimiento.getFinRama();
                            int codTramCierreEsp = procedimiento.getCierreEspera();
                            log.debug(procedimiento.toString());

                                //llamar a procedimiento bd de actualizar Pago Deuda                                 
                                m92DAO.actualizarPagoDeuda(codProc, con);
                                /*
                             Buscar los expedientes que estén en el trámite 5007 - ESPERA PAGO VOLUNTARIO y para los que la fecha actual sea mayor que la fecha de acuse de notificación 
                             (campo FECACUSENOTIFRESOL del trámite 5005 - NOTIFICACION RESOLUCION) más dos meses                        
                                 */
                                List<String> expedientes = m92Manager.getExpedientesEnEspera(codOrg, procedimiento, adapt);
                                /*
                             Para esos expedientes, llamar al método enviarEjecutivaNoRGI() del SW de deuda.                       
                             Cerrar el trámite 5006 - ESPERA PAGO VOLUNTARIO, avanzar al siguiente, 5007 - VIA EJECUTIVA y cerrar ese trámite y la rama de Reintegro
                                 */
                            log.info("ViaEjecutivaJob() - Expedientes " + procedimiento.getCodProcedimiento() + " para Via ejecutiva: " + expedientes.size());
                                for (String numExp : expedientes) {
                                log.info("ViaEjecutivaJob() - Expediente: " + numExp);
                                    detalleError = "";
                                    // variables para recoger los resultados
                                    boolean deudaPagada = false;
                                    boolean deudaEnEjecutiva = false;
                                    boolean deudaFraccionada = false;
                                    boolean deudaAnulada = false;
                                    boolean existeFecResolucion = false;
                                    boolean existeFecAcuse = false;
                                    boolean existeFecLimite = false;
                                    boolean fecAcuseMayorFecResolucion = false;
                                    boolean fecLimiteMayorFecAcuse = false;
                                    boolean hoyMayorFecLimitePagoMas14Dias = false;
                                    int ocuResolucion = 1;
                                    boolean existeTipoNotificacion = false;
                                    boolean notificadaBOE = false;
                                    boolean importeInsuficiente = false;
                                String tipoNotif = null;
// recoger max ocurrencia
                                    ocuResolucion = m92Manager.getMaxOcurrenciaTramitexCodigo(codOrg, numExp, codTramResolucion, adapt);
                                    deudaPagada = m92Manager.deudaPagadaZORKU(codOrg, numExp, adapt);
                                    deudaEnEjecutiva = m92Manager.deudaEnEjecutivaZORKU(codOrg, numExp, adapt);
                                    deudaFraccionada = m92Manager.tienePagoFraccionadoZORKU(codOrg, numExp, adapt);
                                    deudaAnulada = m92Manager.deudaAnuladaZORKU(codOrg, numExp, adapt);
                                    existeFecResolucion = m92Manager.existeFechaTram(codOrg, numExp, codTramResolucion, ocuResolucion, codCampoFecResol, adapt);
                                    existeFecAcuse = m92Manager.existeFechaTram(codOrg, numExp, codTramAcuseRes, ocuResolucion, codCampoFecAcuseRes, adapt);
                                    existeFecLimite = m92Manager.existeFechaExp(codOrg, numExp, codCampoFecLimite, adapt);
                                    existeTipoNotificacion = m92Manager.existeDesplegableTramite(codOrg, numExp, codTramAcuseRes, ocuResolucion, codCampoTipoNotif, adapt);

                                // si no tiene tipo notificación grabada en el tramite hay que comprobar si tiene el checlk de notificación en E_EXT y grabar tipo 14 - Notificación Electrónica
                                if (!existeTipoNotificacion) {
                                    log.debug("NO tiene Tipo Notificación ");
                                    if (m92Manager.tieneMarcaTelematica(codOrg, numExp, adapt)) {
                                        log.debug("Tiene Check de Notificación Telemática");
                                        if (m92Manager.guardarValorDesplegableTramite(codOrg, numExp, codTramAcuseRes, ocuResolucion, codCampoTipoNotif, "14", adapt) > 0) {
                                            log.debug("Grabado Tipo Notificación - 14");
                                            existeTipoNotificacion = true;
                                        }
                                    }
                                } else {
                                    log.debug("Tiene Tipo Notificación ");
                                    // si se ha notificado en Boletín no se tiene que comprobar si se ha acusado después de la fecha límite de pago
                                    tipoNotif = m92Manager.getValorDesplegableTramite(codOrg, numExp, codTramAcuseRes, ocuResolucion, codCampoTipoNotif, adapt);
                                    log.debug("Tipo Notificación: " + tipoNotif);
                                    if (tipoNotif.equalsIgnoreCase("04") || tipoNotif.equalsIgnoreCase("03")) {
                                        notificadaBOE = true;
                                    }
                                }

                                    if (existeFecResolucion && existeFecAcuse) {
                                        fecAcuseMayorFecResolucion = m92Manager.compruebaFecAcuseFecResol(codOrg, numExp, codTramResolucion, codTramAcuseRes, ocuResolucion, adapt);

                                    }
                                    if (existeFecAcuse && existeFecLimite) {
                                        if (notificadaBOE) {
                                            fecLimiteMayorFecAcuse = true;
                                        } else {
                                            fecLimiteMayorFecAcuse = m92Manager.compruebaFechaAcuseFechaLimPago(codOrg, numExp, codTramAcuseRes, ocuResolucion, adapt);
                                        }
                                        hoyMayorFecLimitePagoMas14Dias = m92Manager.compruebaFechaHoyFechaLimPago(codOrg, numExp, adapt);
                                    }
                                String numLiquidacion = m92Manager.getValorCampoTextoExpediente(codOrg, numExp, codCampoNumLiquidacion, adapt);
                                    BigDecimal importePendiente = new BigDecimal("0");
                                    String importeZorku = null;
                                    try {
                                    importeZorku = m92Manager.getImportePendienteZORKU(codOrg, numExp, numLiquidacion, adapt);
                                        importePendiente = new BigDecimal(importeZorku.replace(',', '.'));
                                        importeInsuficiente = importePendiente.compareTo(new BigDecimal("30")) < 0;
                                    } catch (Exception e) {
                                        log.error("ViaEjecutivaJob() -  Errores al recuperar Importe Pendiente - ", e);
                                    }
                                // Compruebo que 
                                // no está pagado ni anulado, fracionado, en suspension o en vía ejecutiva
                                // tiene tipo de notificación, 
                                // el importe pendiente (importe + intereses) supera 30 euros
                                    // la fecha de resolución es menor que la fecha de notificación,
                                    // la fecha de notificación es menor que la fecha límite de pago
                                    // la fecha limite de pago + 14 dias es menor a la fecha actual
                                    if (!deudaPagada && !deudaEnEjecutiva && !deudaFraccionada && !deudaAnulada
                                            && existeTipoNotificacion && !importeInsuficiente
                                            && existeFecResolucion && existeFecAcuse && existeFecLimite
                                            && fecAcuseMayorFecResolucion && fecLimiteMayorFecAcuse && hoyMayorFecLimitePagoMas14Dias) {
                                        // llamar a WS EnviarEjecutivaNoRGI
                                    log.info("LLAMAR A enviarEjecutivaSubvenciones");
                                    String mensaje = MeLanbide92Utilidades.enviarEjecutivaSubvenciones(String.valueOf(codOrg), numExp, codTramAcuseRes, ocuResolucion, idSecuencia, adapt);
                                        if ("0".equals(mensaje)) {
                                            //Cerrar el trámite 06, avanzar al siguiente 07 y cerrarlo.
                                            log.debug("Finalizar Trámite 5006-ESPERA PAGO VOLUNTARIO -codInterno(" + codTramEsperaPago + "): " + numExp);
                                        m92Manager.cerrarTramite(codOrg, numExp, codTramEsperaPago, adapt);
                                            log.debug("Abrir y Finalizar Trámite 5007-VIA EJECUTIVA -codInterno(" + codTramEjecutiva + "): " + numExp);
                                            String uor = m92Manager.getUnidadExpediente(codOrg, numExp, adapt);
                                        m92Manager.abrirTramite(codOrg, numExp, codTramEjecutiva, 1, uor, adapt);
                                            try {
                                                Thread.sleep(10000);
                                            } catch (InterruptedException e) {
                                                // Mensaje en caso de que falle
                                            }
                                        m92Manager.cerrarTramite(codOrg, numExp, codTramEjecutiva, adapt);
                                            log.debug("Abrir y Finalizar Trámite 5099-FIN REINTEGRO -codInterno(" + codTramFinReint + "): " + numExp);
                                        m92Manager.abrirTramite(codOrg, numExp, codTramFinReint, 1, uor, adapt);
                                            try {
                                                Thread.sleep(10000);
                                            } catch (InterruptedException e) {
                                                // Mensaje en caso de que falle
                                            }
                                        m92Manager.cerrarTramite(codOrg, numExp, codTramFinReint, adapt);
                                        // comprobar si tiene abierto el 5999-Cierre Espera Recursos REINTEGRO
                                        if (m92Manager.tieneTramiteAbierto(codOrg, numExp, codTramCierreEsp, adapt)) {
                                            //Cerrar el trámite 9199
                                            log.debug("Finalizar Trámite 9199-Cierre Espera Recursos-codInterno(" + codTramCierreEsp + "): " + numExp);
                                            m92Manager.cerrarTramite(codOrg, numExp, codTramCierreEsp, adapt);
                                        }
                                            registrosOK++;
                                        } else {
                                            resultadoJob = "OKERR";
                                            registrosKO++;
                                        }
                                    } else {

                                        detalleError = "Error de validación: ";
                                        if (deudaPagada) {
                                            detalleError = detalleError + "El expediente ya está pagado. ";
                                        log.error("El expediente ya está pagado. ");
                                        }
                                        if (deudaEnEjecutiva) {
                                            detalleError = detalleError + "El expediente ya está en vía ejecutiva, avisar al tramitador. ";
                                        log.error("El expediente ya está en vía ejecutiva, avisar al tramitador. ");
                                        }
                                        if (deudaFraccionada) {
                                            detalleError = detalleError + "El expediente posiblemente está en fraccionamiento. ";
                                        log.error("El expediente posiblemente está en fraccionamiento. ");
                                        }
                                        if (deudaAnulada) {
                                            detalleError = detalleError + "La deuda está anulada, avisar al tramitador. ";
                                        log.error("La deuda está anulada, avisar al tramitador. ");
                                        }
                                        if (!existeTipoNotificacion) {
                                            detalleError = detalleError + "El expediente no tiene tipo de notificación. ";
                                        log.error("El expediente no tiene tipo de notificación. ");
                                        }
                                        if (!existeFecResolucion) {
                                            detalleError = detalleError + "No existe fecha de resolución. ";
                                        log.error("No existe fecha de resolución. ");
                                        }
                                        if (!existeFecAcuse) {
                                            detalleError = detalleError + "No existe fecha de Acuse notificación. ";
                                        log.error("No existe fecha de Acuse notificación. ");
                                        }
                                        if (!existeFecLimite) {
                                            detalleError = detalleError + "No existe fecha límite de pago. ";
                                        log.error("No existe fecha límite de pago. ");
                                        }
                                        if (existeFecResolucion && existeFecAcuse && !fecAcuseMayorFecResolucion) {
                                            detalleError = detalleError + "La fecha notificación tiene que ser mayor que la fecha de resolución. ";
                                        log.error("La fecha notificación tiene que ser mayor que la fecha de resolución. ");
                                        }
                                        if (existeFecAcuse && existeFecLimite && !fecLimiteMayorFecAcuse) {
                                            detalleError = detalleError + "La fecha límite de pago tiene que ser mayor que la fecha de notificacion. ";
                                        log.error("La fecha límite de pago tiene que ser mayor que la fecha de notificacion. ");
                                        }
                                        if (existeFecLimite && !hoyMayorFecLimitePagoMas14Dias) {
                                            detalleError = detalleError + "La fecha límite de pago + 14 días tiene que ser anterior a la fecha actual. ";
                                        log.error("La fecha límite de pago + 14 días tiene que ser anterior a la fecha actual. ");
                                        }
                                        if (importeInsuficiente) {
                                            detalleError = detalleError + "El importe más los intereses no superan los 30 euro y no se puede enviar a ejecutiva. Se debe anular la deuda y volver a generar otra. ";
                                        log.error("El importe más los intereses no superan los 30 euro y no se puede enviar a ejecutiva. Se debe anular la deuda y volver a generar otra. ");
                                        }
                                        // obtenemos idDeuda para insertar el registro de Error
                                        resultadoJob = "OKERR";
                                    m92Manager.insertarRegistroProcProgViaEjeLIREI(idSecuencia, new Date(), numExp, numLiquidacion, detalleError, 1, adapt);
                                        registrosKO++;
                                    }
                                    log.debug("ViaEjecutivaJob() - fin procesar expediente " + numExp);

                                }
                            log.info("ViaEjecutivaJob() - Expedientes de " + codProc + " a Via ejecutiva - FIN");
                            }
                            log.info("ViaEjecutivaJob() -  FIN ");
                    } catch (Exception e) {
                        log.error("ViaEjecutivaJob() - Error en el job de Vía Ejecutiva LIREI: ", e);
                        mensajeErrorJob = e.getMessage();
                        resultadoJob = "NOOK";
                        try {
                            throw e;
                        } catch (Exception ex) {
                            java.util.logging.Logger.getLogger(ViaEjecutivaJob.class.getName()).log(Level.SEVERE, null, ex);
                            mensajeErrorJob = ex.getMessage();
                        }
                    } finally {
                        try {
                            m92Manager.modificarRegistroProcesosProgramadosEjec(idSecuencia, new Date(), resultadoJob, registrosOK, registrosKO, mensajeErrorJob, adapt);
                        } catch (Exception ex) {
                            java.util.logging.Logger.getLogger(ViaEjecutivaJob.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        if (con != null) {
                            con.close();
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            log.error("ViaEjecutivaJob() - Error: " + ex);
        }
    }

    private final Logger log = LogManager.getLogger(ViaEjecutivaJob.class);

    public AdaptadorSQLBD getAdaptSQLBD(String codOrganizacion) throws SQLException {
        ResourceBundle config = ResourceBundle.getBundle("techserver");
        String gestor = config.getString("CON.gestor");
        String jndiGenerico = config.getString("CON.jndi");
        Connection conGenerico = null;
        Statement st = null;
        ResultSet rs = null;
        String[] salida = null;

        DataSource ds = null;
        AdaptadorSQLBD adapt = null;
        synchronized (this) {
            try {
                PortableContext pc = PortableContext.getInstance();
                ds = (DataSource) pc.lookup(jndiGenerico, DataSource.class);
                // Conexion al esquema generico
                conGenerico = ds.getConnection();
                String sql = "SELECT EEA_BDE FROM A_EEA WHERE EEA_APL=" + ConstantesDatos.APP_GESTION_EXPEDIENTES + " AND AAE_ORG=" + codOrganizacion;
                st = conGenerico.createStatement();
                rs = st.executeQuery(sql);
                String jndi = null;
                while (rs.next()) {
                    jndi = rs.getString("EEA_BDE");
                }//while(rs.next())

                st.close();
                rs.close();
                conGenerico.close();

                if (jndi != null && gestor != null && !"".equals(jndi) && !"".equals(gestor)) {
                    salida = new String[7];
                    salida[0] = gestor;
                    salida[1] = "";
                    salida[2] = "";
                    salida[3] = "";
                    salida[4] = "";
                    salida[5] = "";
                    salida[6] = jndi;
                    adapt = new AdaptadorSQLBD(salida);
                }//if(jndi!=null && gestor!=null && !"".equals(jndi) && !"".equals(gestor))
            } catch (TechnicalException te) {
                log.error("*** AdaptadorSQLBD: " + te.toString());
            } catch (SQLException e) {
            } finally {
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (conGenerico != null && !conGenerico.isClosed()) {
                    conGenerico.close();
                }
            }// finally
        }// synchronized
        return adapt;
    }//getConnection

}
