/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide07.job;

import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.flexia.integracion.moduloexterno.melanbide07.dao.MeLanbide07DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide07.manager.MeLanbide07Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide07.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide07.util.ConstantesMeLanbide07;
import es.altia.flexia.integracion.moduloexterno.melanbide07.util.UtilidadesREINT;
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

public class PasoViaEjecutivaJob implements Job {

    //private static final String ERROR_RECUPERANDO_ID_DEUDA = "31";
    //private static final String ERROR_RECUPERANDO_ID_DEUDA_VACIO = "35";
    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        try {
            MeLanbide07Manager m07Manager = MeLanbide07Manager.getInstance();
            MeLanbide07DAO m07DAO = MeLanbide07DAO.getInstance();
            String resultadoJob = "OK";
            String mensajeErrorJob = "";
            String detalleError = "";
            int registrosProcesadosOK = 0;
            int registrosProcesadosNOOK = 0;
            Integer idSecuencia = 0;

            String servidor = ConfigurationParameter.getParameter(ConstantesMeLanbide07.CAMPO_SERVIDOR, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
            log.debug("servidor " + servidor);
            log.debug("weblogic.Name " + System.getProperty("weblogic.Name"));
            //String servidor = "flexia1"; //DESA
            //String servidor = "paprergi1_flexia1"; //PRE
            //String servidor = "pargi1_flexia1"; //PRO

            if (servidor.equals(System.getProperty("weblogic.Name"))) {//PARA LOCAL QUITAR
                synchronized (jec) {
                    Connection con = null;
                    int numIntentos = 0;
                    try {
                        log.info("Execute lanzado " + System.getProperty("weblogic.Name"));

                        int codOrg = Integer.parseInt(ConfigurationParameter.getParameter("COD_ORG", ConstantesMeLanbide07.FICHERO_PROPIEDADES));
                        //int codOrg = 0;      //pruebas
                        //int codOrg = 1;      //real
                        boolean dosEntornos = Boolean.getBoolean(ConfigurationParameter.getParameter("DOS_ENTORNOS", ConstantesMeLanbide07.FICHERO_PROPIEDADES));
                        if (codOrg < 2) {
                            con = this.getAdaptSQLBD(String.valueOf(codOrg)).getConnection();
                        }
                        while (codOrg < 2) {
                            log.info("en el while de tokens codOrg: " + codOrg);
                            //String codOrganizacion = String.valueOf(codOrg);
                            /*#195505
                             //llamar a procedimiento bd de actualizar Pago Deuda
                             */
                            //MeLanbide07DAO meLanbide07DAO = MeLanbide07DAO.getInstance();
                            m07DAO.actualizarPagoDeuda(con);

                            /*#195505
                             Buscar los expedientes que estén en el trámite 06 - ESPERA PAGO VOLUNTARIO y para los que la fecha actual sea mayor que la fecha de acuse de notificación 
                             (campo FECACUSENOTIFRESOL del trámite 5 - NOTIFICACION RESOLUCION) más dos meses                        
                             */
                            List<String> expedientes = m07DAO.getExpedientesEnEspera(con);

                            /*#195505
                             Para esos expedientes, llamar al método enviarEjecutivaNoRGI() del SW de deuda. Esta llamada está programada en la tarea #244792 dentro del MELANBIDE07                        
                             Cerrar el trámite 06 - ESPERA PAGO VOLUNTARIO, avanzar al siguiente, 07 - VIA EJECUTIVA y cerrar ese trámite y el expediente
                             */
                            log.debug("Expedientes REINT Via ejecutiva: " + expedientes.size());
                            idSecuencia = m07DAO.insertarRegistroProcesosProgramadosEjec(new Date(), con);
                            String nombreModulo = ConfigurationParameter.getParameter(ConstantesMeLanbide07.NOMBRE_MODULO, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
                            String codigoCampoIdDeuda = ConfigurationParameter.getParameter(codOrg + ConstantesMeLanbide07.BARRA_SEPARADORA
                                    + ConstantesMeLanbide07.MODULO_INTEGRACION + ConstantesMeLanbide07.BARRA_SEPARADORA + nombreModulo + ConstantesMeLanbide07.BARRA_SEPARADORA
                                    + ConstantesMeLanbide07.CAMPO_ID_DEUDA, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
                            AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrg));

                            for (String numExpediente : expedientes) {
                                log.debug("numExpediente: " + numExpediente);
                                detalleError = "";
                                String[] datos = numExpediente.split(ConstantesMeLanbide07.BARRA_SEPARADORA);
                                String ejercicio = datos[0];
                                String codProcedimiento = datos[1];
                                String codigoInternoTramite_4 = m07DAO.obtenerCodigoInternoTramite(codProcedimiento, ConstantesMeLanbide07.TRAMITE_4, con).toString();
                                String codigoInternoTramite_5 = m07DAO.obtenerCodigoInternoTramite(codProcedimiento, ConstantesMeLanbide07.TRAMITE_5, con).toString();
                                int ocurrenciaResolucion = m07Manager.getMaxOcurrenciaTramitexCodigo(codOrg, codProcedimiento, ejercicio, numExpediente, Integer.parseInt(codigoInternoTramite_4), adaptador);
                                //llamada a WS
                                //Creo variables boolean para recoger resultados de BBDD que voy a utilizar varias veces...
                                boolean expedientePagado = m07DAO.expedientePagadoZORKU(numExpediente, con);
                                boolean expedienteEnViaEjecutiva = m07DAO.expedienteEnViaEjecutivaZORKU(numExpediente, con);
                                boolean expedienteTieneTipoNotificacion = m07DAO.expedienteTieneTipoNotificacion(numExpediente, con);
                                boolean existeFechaResolucion = m07DAO.existeFechaTramREINT(numExpediente, ConstantesMeLanbide07.TABLA_E_TFET, codigoInternoTramite_4, ocurrenciaResolucion, ConstantesMeLanbide07.CAMPO_FECRESOLUCION, con);
                                boolean existeFechaNotificacion = m07DAO.existeFechaTramREINT(numExpediente, ConstantesMeLanbide07.TABLA_E_TFET, codigoInternoTramite_5, ocurrenciaResolucion, ConstantesMeLanbide07.CAMPO_FECACUSENOTIFRESOL, con);
                                // #445600
                                boolean existeFechaLimitePago = m07DAO.existeFechaExpREINT(numExpediente, ConstantesMeLanbide07.TABLA_E_TFE, ConstantesMeLanbide07.CAMPO_FECLIMITEPAGO, con);
                                boolean esFechaNotificacionMayorQueFechaResolucion = false;
                                boolean esFechaLimitePagoMayorQueFechaNotificacion = false;
                                boolean esHoyMayorQueFechaLimitePagoMas14Dias = false;
                                boolean notificadaBOE  = false;
                                String tipoNotif = m07DAO.getValorDesplegableTramite(codOrg, numExpediente, codigoInternoTramite_5, String.valueOf(ocurrenciaResolucion), "TIPONOTIF", con);
                                if (tipoNotif.equalsIgnoreCase("04") || tipoNotif.equalsIgnoreCase("03")) {
                                notificadaBOE = true;
                                }
                                if (existeFechaResolucion && existeFechaNotificacion) {
                                    esFechaNotificacionMayorQueFechaResolucion = m07Manager.compruebaFechaNotifFechaResolREINT(numExpediente, adaptador);
                                }
                                if (existeFechaNotificacion && existeFechaLimitePago) {
                                    // #663539 si se ha notificado publicando en BOE 
                                    if (notificadaBOE) {
                                        esFechaLimitePagoMayorQueFechaNotificacion = true;
                                    } else {
                                        esFechaLimitePagoMayorQueFechaNotificacion = m07Manager.compruebaFechaNotifFechaLimPagoREINT(numExpediente, adaptador);
                                    }
                                    // #355448
                                    esHoyMayorQueFechaLimitePagoMas14Dias = m07Manager.compruebaFechaHoyFechaLimPagoREINT(numExpediente, adaptador);
                                }
                                boolean expedienteFraccionado = m07DAO.tienePagoFraccionadoZORKU(numExpediente, con);
                                boolean expedienteAnulado = m07DAO.expedienteAnuladoZORKU(numExpediente, con);
                                // obtenemos idDeuda para insertar el registro de Error

                                String idDeuda = m07Manager.getValorCampoTextoExpediente(codOrg, numExpediente, ejercicio, codigoCampoIdDeuda, codProcedimiento, adaptador);

                                BigDecimal importePendiente = new BigDecimal("0");
                                String importeZorku = null;
                                try {
                                    importeZorku = MeLanbide07Manager.getInstance().getImportePendienteZORKU(numExpediente, idDeuda, adaptador);
                                    importePendiente = new BigDecimal(importeZorku.replace(',', '.'));
                                } catch (Exception e) {
                                    log.error("enviarEjecutivaSubvenciones.Devolvemos: Errores al recuperar Importe Pendiente - ", e);
                                    //                   return ERROR_RECUPERANDO_IMP_PENDIENTE;
                                }
                                boolean importeInsuficiente = importePendiente.compareTo(new BigDecimal("30")) < 0;

                                // Compruebo que el expediente no está pagado,
                                // no está en vía ejecutiva
                                // el expediente tiene tipo de notificación, 
                                // la fecha de resolución es menor que la fecha de notificación,
                                // la fecha de notificación es menor que la fecha límite de pago
                                // la fecha limite de pago + 14 dias es menor a la fecha actual
                                if (!expedientePagado
                                        && !expedienteEnViaEjecutiva
                                        && expedienteTieneTipoNotificacion
                                        && existeFechaResolucion
                                        && existeFechaNotificacion
                                        && existeFechaLimitePago
                                        && esHoyMayorQueFechaLimitePagoMas14Dias
                                        && esFechaNotificacionMayorQueFechaResolucion
                                        && esFechaLimitePagoMayorQueFechaNotificacion
                                        && !expedienteFraccionado
                                        && !expedienteAnulado
                                        && !importeInsuficiente) {

                                    String mensaje = UtilidadesREINT.llamaAlServicioWebEnviarEjecutivaNoRGI(String.valueOf(codOrg), numExpediente, idSecuencia, adaptador);
                                    if ("0".equals(mensaje)) {
                                        //llamaAlServicioWebEnviarEjecutivaNoRGI(String.valueOf(codOrg), numExpediente);
                                        //Cerrar el trámite 06, avanzar al siguiente 07 y cerrarlo.
                                        String codTram = m07DAO.getCodigoInternoTramite(codOrg, codProcedimiento, "6", con);
                                        log.debug("Finalizar Trámite 6-ESPERA PAGO VOLUNTARIO -codInterno(" + codTram + "): " + numExpediente);
                                        m07DAO.cerrarTramite(codOrg, numExpediente, codTram, con);
                                        //finalizarAvanzarTramite(codOrg, codTram, params, null, listaExpedientes);
                                        codTram = m07DAO.getCodigoInternoTramite(codOrg, codProcedimiento, "7", con);
                                        log.debug("Abrir y Finalizar Trámite 7-VIA EJECUTIVA -codInterno(" + codTram + "): " + numExpediente);
                                        //finalizarAvanzarTramite(codOrg, codTram, params, null, listaExpedientes);
                                        String uor = m07DAO.obtenerUnidadExpediente(numExpediente, con);
                                        m07DAO.abrirTramite(codOrg, numExpediente, codTram, uor, con);
                                        try {
                                            Thread.sleep(10000);
                                        } catch (InterruptedException e) {
                                            // Mensaje en caso de que falle
                                        }
                                        m07DAO.cerrarTramite(codOrg, numExpediente, codTram, con);
                                        // comprobar si tiene abierto el 9199-Cierre Espera Recursos
                                        codTram = m07DAO.getCodigoInternoTramite(codOrg, codProcedimiento, "9199", con);
                                        if (m07DAO.tieneTramiteAbierto(codOrg, numExpediente, codTram, con)) {
                                            //Cerrar el trámite 9199
                                            log.debug("Finalizar Trámite 9199-Cierre Espera Recursos-codInterno(" + codTram + "): " + numExpediente);
                                            m07DAO.cerrarTramite(codOrg, numExpediente, codTram, con);
                                        }
                                        // Actualizamos el estado del expediente a Finalizado : 0-Activo 1-Anulado 9-Finalizado
                                        log.debug("Cerrar expediente -estado finalizado (9)");
                                        m07DAO.cerrarExpediente(codOrg, numExpediente, con);
                                        registrosProcesadosOK++;

                                    } else {
                                        resultadoJob = "OKERR";
                                        registrosProcesadosNOOK++;
                                    }
                                } else {
                                    detalleError = "Error de validación: ";
                                    if (expedientePagado) {
                                        detalleError = detalleError + "La liquidación ya está pagada. ";
                                    }
                                    if (expedienteEnViaEjecutiva) {
                                        detalleError = detalleError + "El expediente ya está en vía ejecutiva, avisar al tramitador. ";
                                    }
                                    if (!expedienteTieneTipoNotificacion) {
                                        detalleError = detalleError + "El expediente no tiene tipo de notificación. ";
                                    }
                                    if (!existeFechaResolucion) {
                                        detalleError = detalleError + "No existe fecha de resolución. ";
                                    }
                                    if (!existeFechaNotificacion) {
                                        detalleError = detalleError + "No existe fecha de notificación. ";
                                    }
                                    if (!existeFechaLimitePago) {
                                        detalleError = detalleError + "No existe fecha límite de pago. ";
                                    }

                                    if (existeFechaResolucion
                                            && existeFechaNotificacion
                                            && !esFechaNotificacionMayorQueFechaResolucion) {
                                        detalleError = detalleError + "La fecha notificación tiene que ser mayor que la fecha de resolución. ";
                                    }
                                    if (existeFechaNotificacion
                                            && existeFechaLimitePago
                                            && !esFechaLimitePagoMayorQueFechaNotificacion) {
                                        detalleError = detalleError + "La fecha límite de pago tiene que ser mayor que la fecha de notificacion. ";
                                    }
                                    if (existeFechaLimitePago
                                            && !esHoyMayorQueFechaLimitePagoMas14Dias) {
                                        detalleError = detalleError + "La fecha límite de pago + 14 días tiene que ser anterior a la fecha actual. ";
                                    }
                                    if (expedienteFraccionado) {
                                        detalleError = detalleError + "La liquidación posiblemente está en fraccionamiento. ";
                                    }
                                    if (expedienteAnulado) {
                                        detalleError = detalleError + "La liquidación está anulada, avisar al tramitador. ";
                                    }
                                    if (importeInsuficiente) {
                                        detalleError = detalleError + "El importe más los intereses no superan los 30 euro y no se puede enviar a ejecutiva. Se debe anular la deuda y volver a generar otra. ";
                                    }
                                    resultadoJob = "OKERR";

                                    m07DAO.insertarRegistroProcesosProgramadosViaEjeREINT(idSecuencia, new Date(), numExpediente, idDeuda, detalleError, 1, con);
                                    registrosProcesadosNOOK++;
                                }
                                log.debug("fin procesar expediente " + numExpediente);
                            }
                            log.debug("Expedientes REINT Via ejecutiva - FIN");

                            if (dosEntornos) {
                                codOrg++;
                            } else {
                                codOrg = 2;
                            }
                        }
                    } catch (Exception e) {
                        log.error("Error en el job de apertura automática de trámites: ", e);
                        mensajeErrorJob = e.getMessage();
                        resultadoJob = "NOOK";
                        try {
                            int intentos = numIntentos + 1;
                        } catch (Exception i) {
                            mensajeErrorJob = i.getMessage();
                            log.error("Error en la función actualizarError: " + mensajeErrorJob);

                        }

                        try {
                            throw e;
                        } catch (Exception ex) {
                            java.util.logging.Logger.getLogger(PasoViaEjecutivaJob.class.getName()).log(Level.SEVERE, null, ex);
                            mensajeErrorJob = ex.getMessage();
                        }
                    } finally {
                        m07DAO.modificarRegistroProcesosProgramadosEjec(idSecuencia, new Date(), resultadoJob, registrosProcesadosOK, registrosProcesadosNOOK, mensajeErrorJob, con);
                        if (con != null) {
                            //try {                                
                            con.close();
                            //} catch (SQLException ex) {
                            //    java.util.logging.Logger.getLogger(PasoViaEjecutivaJob.class.getName()).log(Level.SEVERE, null, ex);
                            //}
                        }
                    }
                }//para local quitar
            }
        } catch (Exception ex) {
            log.error("Error: " + ex);
        }
    }
/*
    private Vector<TramitacionExpedientesValueObject> transformarListaTramitesIniciar(Vector<TramitacionExpedientesValueObject> listaTramitesIniciar, String codUnidadTramitadora) {
        for (int i = 0; listaTramitesIniciar != null && i < listaTramitesIniciar.size(); i++) {
            listaTramitesIniciar.get(i).setCodUnidadTramitadoraTram(codUnidadTramitadora);
        }
        return listaTramitesIniciar;
    }*/
//    public void actualizarPagosDeudaREINT(Connection con) {
//        //melanbide07DAO.getActuaPagoDeuda(con);
//    }
    protected static Config conf = ConfigServiceHelper.getConfig("notificaciones");
    private Logger log = LogManager.getLogger(PasoViaEjecutivaJob.class);
    private String codOrganizacion;

    public String getCodOrganizacion() {
        return codOrganizacion;
    }

    public void setCodOrganizacion(String codOrganizacion) {
        this.codOrganizacion = codOrganizacion;
    }

    private AdaptadorSQLBD getAdaptSQLBD(String codOrganizacion) {
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
                log.debug("He cogido el jndi: " + jndiGenerico);

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
                try {
                    if (st != null) {
                        st.close();
                    }
                    if (rs != null) {
                        rs.close();
                    }
                    if (conGenerico != null && !conGenerico.isClosed()) {
                        conGenerico.close();
                    }
                } catch (SQLException e) {
                }//try-catch
            }// finally

        }// synchronized
        return adapt;
    }//getConnection
}
