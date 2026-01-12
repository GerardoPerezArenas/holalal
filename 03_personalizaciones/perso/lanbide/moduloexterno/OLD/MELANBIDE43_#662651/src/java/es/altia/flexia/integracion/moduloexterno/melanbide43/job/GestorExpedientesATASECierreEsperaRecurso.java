/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide43.job;

import es.altia.flexia.integracion.moduloexterno.melanbide43.MELANBIDE43;
import es.altia.flexia.integracion.moduloexterno.melanbide43.dao.MeLanbide43GestionTramitesExpedienteDAO;
import es.altia.flexia.integracion.moduloexterno.melanbide43.dao.MeLanbide43JobsEjeInfDAO;
import es.altia.flexia.integracion.moduloexterno.melanbide43.dao.MeLanbide43JobsTareaEjeDAO;
import es.altia.flexia.integracion.moduloexterno.melanbide43.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide43.util.ConstantesMeLanbide43;
import es.altia.flexia.integracion.moduloexterno.melanbide43.util.MeLanbide43BDUtil;
import es.altia.flexia.integracion.moduloexterno.melanbide43.util.MeLanbide43GeneralUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.MeLanbide43GestionTramitesExpedienteETraVO;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.MeLanbide43GestionTramitesExpedienteRespuesta;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.MeLanbide43JobsEjeInf;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.MeLanbide43JobsTareaEje;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import es.lanbide.lan6.adaptadoresPlatea.excepciones.Lan6Excepcion;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Cierra las espera de presentacion de recursos en mi carpeta, cierra el tramite en flexia y abre cierre expediente. 
 * @author INGDGC
 */

public class GestorExpedientesATASECierreEsperaRecurso implements Job{

    private final Logger log = Logger.getLogger(GestorExpedientesATASECierreEsperaRecurso.class);
    private static MeLanbide43GestionTramitesExpedienteDAO meLanbide43GestionTramitesExpedienteDAO = new MeLanbide43GestionTramitesExpedienteDAO();
    private static MeLanbide43JobsTareaEjeDAO meLanbide43JobsTareaEjeDAO = new MeLanbide43JobsTareaEjeDAO();
    private static MeLanbide43JobsEjeInfDAO meLanbide43JobsEjeInfDAO = new MeLanbide43JobsEjeInfDAO();
    
    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        try {
            int contador = jec.getRefireCount();
            log.info("jec.getRefireCount(): " + contador);
            String servidor = ConfigurationParameter.getParameter(ConstantesMeLanbide43.CAMPO_SERVIDOR, ConstantesMeLanbide43.FICHERO_PROPIEDADES);
            //String servidor = "flexia1"; //DESA
            //String servidor = "paprergi1_flexia1"; //PRE
            //String servidor = "pargi1_flexia1"; //PRO
            log.info("servidor: " + servidor);
            String auxMensajeError ="";
            if (servidor.equals(System.getProperty("weblogic.Name"))) {//PARA LOCAL QUITAR
                synchronized (jec) {
                    AdaptadorSQLBD adaptador = null;
                    Connection con = null;
                    Connection conTransaccion = null;
                    String numExpediente = "";
                    try {
                        log.info("Job lanzado : " + System.getProperty("weblogic.Name"));

                        int codOrg = Integer.parseInt(ConfigurationParameter.getParameter("COD_ORG", ConstantesMeLanbide43.FICHERO_PROPIEDADES));
                        boolean dosEntornos = Boolean.getBoolean(ConfigurationParameter.getParameter("DOS_ENTORNOS",
                                ConstantesMeLanbide43.FICHERO_PROPIEDADES));
                        while (codOrg < 2) {
                            MeLanbide43BDUtil meLanbide43BDUtil = new MeLanbide43BDUtil();
                            adaptador = meLanbide43BDUtil.getAdaptSQLBD(String.valueOf(codOrg));
                            con = adaptador.getConnection();
                            log.info("While de tokens codOrg: " + codOrg);
                            MeLanbide43JobsTareaEje numeroTareaProcesar=meLanbide43JobsTareaEjeDAO.getMeLanbide43JobsTareaEje(this.getClass().getSimpleName(),con);
                            if(numeroTareaProcesar!=null && numeroTareaProcesar.getNumeroTarea()!=null){
                                List<MeLanbide43JobsEjeInf> expedientes = meLanbide43JobsEjeInfDAO.getExpedientesProcesarByNumeroTarea(numeroTareaProcesar.getNumeroTarea(), con);
                                log.info("Numexpedientes Tratar :" + (expedientes != null ? expedientes.size() : 0) );
                                //Operaciones en Regexlan las hacemos transaccionales para cada expedientes.
                                conTransaccion = adaptador.getConnection();
                                for (MeLanbide43JobsEjeInf item : expedientes) {
                                    numExpediente = item.getNumeroExpediente();
                                    item.setProcesado(1);
                                    item.setDetalles("Proceso Iniciado");
                                    this.actualizarEstadoOperacionExpediente(item, con);
                                    String codProcedimiento = MeLanbide43GeneralUtils.getProcedimientoFromNumeroExpediente(numExpediente);
                                    log.info("numexpediente:" + numExpediente);
                                    String codVisibleTramitePresentacionRecursos = ConfigurationParameter.getParameter("TRAM_" + codProcedimiento + "_ESPERA_PRESENTACION_RECURSOS_COD_VISIBLE", ConstantesMeLanbide43.FICHERO_PROPIEDADES);
                                    String codVisibleTramiteCierreExpediente = ConfigurationParameter.getParameter("TRAM_" + codProcedimiento + "_CIERRE_EXPEDIENTE_COD_VISIBLE", ConstantesMeLanbide43.FICHERO_PROPIEDADES);
                                    log.info("codVisibleTramitePresentacionRecursos - properties : " + codVisibleTramitePresentacionRecursos);
                                    log.info("codVisibleTramiteCierreExpediente - properties : " + codVisibleTramiteCierreExpediente);
                                    MeLanbide43GestionTramitesExpedienteETraVO tramitePresentacionRecursos = meLanbide43GestionTramitesExpedienteDAO.getTramiteExpedienteETraVOByCodVisible(codOrg, codProcedimiento, codVisibleTramitePresentacionRecursos, con);
                                    MeLanbide43GestionTramitesExpedienteETraVO tramiteCierreExpediente = meLanbide43GestionTramitesExpedienteDAO.getTramiteExpedienteETraVOByCodVisible(codOrg, codProcedimiento, codVisibleTramiteCierreExpediente, con);
                                    log.info("codTramitePresentacionRecursos Leido BD : " + tramitePresentacionRecursos != null ? tramitePresentacionRecursos.getTRA_COD() + " : " + tramitePresentacionRecursos.getE_TML().getTML_VALOR() : "");
                                    log.info("codTramiteCierreExpediente Leido BD : " + tramiteCierreExpediente != null ? tramiteCierreExpediente.getTRA_COD() + " : " + tramiteCierreExpediente.getE_TML().getTML_VALOR() : "");
                                    MELANBIDE43 melanbide43 = new MELANBIDE43();
                                    try {
                                        String respuestaCierre = melanbide43.cerrarEsperaPresentacionRecursoReposicion(codOrg, tramitePresentacionRecursos.getTRA_COD(), 0, numExpediente);
                                        if (respuestaCierre != null && respuestaCierre.equalsIgnoreCase("0")) {
                                            // Si se cierra la espera procedemos a avanzar el expediente.
                                            adaptador.inicioTransaccion(conTransaccion);
                                            MeLanbide43GestionTramitesExpedienteRespuesta respuestaTramitacion = meLanbide43GestionTramitesExpedienteDAO.cerrarTramiteConRegistroHistoricoExpediente(codOrg, numExpediente, tramitePresentacionRecursos, 0, numeroTareaProcesar, numeroTareaProcesar.getNumeroTarea() + " Proceso Batch Cierre de esperas presentacion de recursos.", conTransaccion);
                                            if (respuestaTramitacion != null && respuestaTramitacion.isRespuestaOKBoolean()) {
                                                respuestaTramitacion = meLanbide43GestionTramitesExpedienteDAO.abrirTramiteConRegistroHistoricoExpediente(codOrg, numExpediente, tramiteCierreExpediente, numeroTareaProcesar.getNumeroTarea() + " Proceso Batch Cierre de esperas presentacion de recursos.", numeroTareaProcesar, Boolean.FALSE, conTransaccion);
                                                if (respuestaTramitacion != null && respuestaTramitacion.isRespuestaOKBoolean()) {
                                                    log.info("Operacion correcta en el expediente " + numExpediente + " Procedemos a hacer commit de los cambios en Regexlan.");
                                                    adaptador.finTransaccion(conTransaccion);
                                                    item.setResultado("OK");
                                                    item.setDetalles("Procesado Correctamente");
                                                    this.actualizarEstadoOperacionExpediente(item, con);
                                                } else {
                                                    log.error("No se ha podido dar de alta el tramite cierre del expediente. Si se ha cerrado la espera. Hacemos rollback Operaciones en Regexlan .. "
                                                     + (respuestaTramitacion != null ? respuestaTramitacion.getCodigoDescripcionErrorAsString() : "")
                                                    );
                                                    adaptador.rollBack(conTransaccion);
                                                    item.setResultado("ERROR");
                                                    item.setDetalles("Espera Mi Carpeta Cerrada. Tramite Espera Recurso Cerrado. Rollback Operaciones Regexlan.  No se ha podido dar alta  el tramite Cierre Expediente en Regexlan: " + (respuestaTramitacion!=null ? respuestaTramitacion.getCodigoDescripcionErrorAsString():""));
                                                    this.actualizarEstadoOperacionExpediente(item, con);
                                                }
                                            } else {
                                                log.error("No se ha podido cerrar el tramite en regexlan - Pero si se ha cerrado la espera en Mi carpeta"
                                                        + (respuestaTramitacion != null ? respuestaTramitacion.getCodigoDescripcionErrorAsString() : "")
                                                );
                                                adaptador.rollBack(conTransaccion);
                                                item.setResultado("ERROR");
                                                item.setDetalles("Espera Mi Carpeta Cerrada. Rollback Operaciones Regexlan. No se ha podido cerrar el tramite en Regexlan: " + (respuestaTramitacion!=null ? respuestaTramitacion.getCodigoDescripcionErrorAsString():""));
                                                this.actualizarEstadoOperacionExpediente(item, con);
                                            }
                                        } else {
                                            log.error("No se ha podido realizar la operacion de cierre esperas recurso " + numExpediente + ". No se avanza el expediente. " + respuestaCierre);
                                            item.setResultado("ERROR");
                                            item.setDetalles("No se ha podido cerrar las esperasen Mi carpeta. " + respuestaCierre );
                                            this.actualizarEstadoOperacionExpediente(item, con);
                                        }
                                    } catch (BDException e) {
                                        log.error("Error devolviendo la conexion transaccional... " + e.getErrorCode() + " " + e.getDescripcion() + " " + e.getMensaje(), e);
                                        // La update no propaga error, no hacemos catch
                                        item.setResultado("ERROR");
                                        item.setDetalles(e.getErrorCode() + " " + e.getDescripcion() + " " + e.getMensaje());
                                        this.actualizarEstadoOperacionExpediente(item, con);
                                        adaptador.rollBack(conTransaccion);
                                    } catch (Exception e) {
                                        if(e instanceof Lan6Excepcion){
                                            Lan6Excepcion ex=(Lan6Excepcion)e;
                                            auxMensajeError=(ex.getCodes()!=null ? Arrays.toString(ex.getCodes().toArray()): " -NoCodigoError-")
                                                    + " : " + (ex.getMessages()!=null ? Arrays.toString(ex.getMessages().toArray()):"-NoMensajeError-")
                                                    ;
                                            log.error("Error Ejecutando operaciones de vance cierre de tramites en Regexlan " + numExpediente + ". Hacemos Rollback Transaccion.", ex);
                                        }else{
                                            log.error("Error Ejecutando operaciones de vance cierre de tramites en Regexlan " + numExpediente + ". Hacemos Rollback Transaccion.", e);
                                            auxMensajeError=e.getMessage() + " " + e.getLocalizedMessage();
                                        }
                                        item.setResultado("ERROR");
                                        item.setDetalles(auxMensajeError);
                                        this.actualizarEstadoOperacionExpediente(item, con);
                                        adaptador.rollBack(conTransaccion);
                                    }
                                }
                            }else{
                                log.error("Configuracion Tarea en BD no recuperada, no se ejecuta el job. MELANBIDE43_JOBS_TAREA_EJE");
                            }                            
                            if (dosEntornos) {
                                codOrg++;
                            } else {
                                codOrg = 2;
                            }
                        }
                    } catch (Exception e) {
                        log.error(GestorExpedientesATASECierreEsperaRecurso.class.getName() 
                                + " Error en el job : " + e.getMessage(), e);
                    } finally {
                        if (con != null) {
                            try {
                                con.close();
                            } catch (SQLException ex) {
                                log.error(GestorExpedientesATASECierreEsperaRecurso.class.getName()+" - Error cerrando BBDD", ex);
                            }
                        }
                        if (adaptador != null) {
                            try {
                                adaptador.devolverConexion(conTransaccion);
                            } catch (BDException e) {
                                log.error("Error devolviendo la conexion transaccional... " + e.getErrorCode() + " " + e.getDescripcion() + " " + e.getMensaje(), e);
                            }
                        }
                    }
                }//para local quitar
            }
        log.info(this.getClass().getName() + ".execute -  Fin " + new Date().toString());
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error(this.getClass().getName()  + " Error: " + ex);
        }
    }
    
    public void actualizarEstadoOperacionExpediente(MeLanbide43JobsEjeInf data, Connection con){
        try {
            meLanbide43JobsEjeInfDAO.updateById(data, con);
        } catch (Exception e) {
            log.error("Error Actualizando el estado de la opeacion en el expediente  -  " + (data!=null ? data.getNumeroExpediente() + " - " + data.getNombreProcesoJob() : "" ),e);
            // No propagamos error para no parar proceso
        }
        
    }

}
