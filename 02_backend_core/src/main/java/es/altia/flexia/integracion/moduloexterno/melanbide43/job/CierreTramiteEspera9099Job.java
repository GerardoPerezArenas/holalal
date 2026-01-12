package es.altia.flexia.integracion.moduloexterno.melanbide43.job;

import es.altia.flexia.integracion.moduloexterno.melanbide43.MELANBIDE43;
import es.altia.flexia.integracion.moduloexterno.melanbide43.dao.MeLanbide43GestionTramitesExpedienteDAO;
import es.altia.flexia.integracion.moduloexterno.melanbide43.manager.MeLanbide43Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide43.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide43.util.ConstantesMeLanbide43;
import es.altia.flexia.integracion.moduloexterno.melanbide43.util.MeLanbide43BDUtil;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.MeLanbide43GestionTramitesExpedienteETraVO;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.MeLanbide43GestionTramitesExpedienteRespuesta;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.MeLanbide43JobsTareaEje;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.Tram9099CerrarVO;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExternoParamAdicionales;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import es.altia.util.conexion.BDException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author alexandrep
 */
public class CierreTramiteEspera9099Job implements Job{
    
      private final Logger log = LogManager.getLogger(CierreTramiteEspera9099Job.class);
      private static MeLanbide43GestionTramitesExpedienteDAO meLanbide43GestionTramitesExpedienteDAO = new MeLanbide43GestionTramitesExpedienteDAO();
    
     @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        try {
            int contador = jec.getRefireCount();
            int usuario = 5;
            String nombreUsuario = "ADMIN";
            log.info(this.getClass().getName() + " jec.getRefireCount(): " + contador);
            String servidor = ConfigurationParameter.getParameter(ConstantesMeLanbide43.CAMPO_SERVIDOR, ConstantesMeLanbide43.FICHERO_PROPIEDADES);
            log.debug(this.getClass().getName() + " servidor: " + servidor);
            if (servidor.equals(System.getProperty("weblogic.Name"))) {//PARA LOCAL QUITAR
                synchronized (jec) {
                    AdaptadorSQLBD adaptador = null;
                    Connection con = null;
                    Connection conTransaccion = null;
                    try {
                        log.info(this.getClass().getSimpleName()+" CierreTramiteEspera9099Job - Execute lanzado " + System.getProperty("weblogic.Name"));

                        int codOrg = Integer.parseInt(ConfigurationParameter.getParameter("COD_ORG", ConstantesMeLanbide43.FICHERO_PROPIEDADES));
                        boolean dosEntornos = Boolean.getBoolean(ConfigurationParameter.getParameter("DOS_ENTORNOS", ConstantesMeLanbide43.FICHERO_PROPIEDADES));
                        while (codOrg < 2) {
                            MeLanbide43BDUtil meLanbide43BDUtil = new MeLanbide43BDUtil();
                            adaptador = meLanbide43BDUtil.getAdaptSQLBD(String.valueOf(codOrg));
                            con = adaptador.getConnection();
                            log.debug("CierreTramiteEspera9099Job - En el while de tokens codOrg: " + codOrg);
                            //expedientes a cerrar la espera de Recurso
                            List<Tram9099CerrarVO> expedientes = MeLanbide43Manager.getInstance().getExpedientesCerrar9099(adaptador);
                            log.debug("CierreTramiteEspera9099Job - Expedientes a cerrar espera Recurso : " + expedientes.size());
                            conTransaccion = adaptador.getConnection();
                            for (Iterator<Tram9099CerrarVO> i = expedientes.iterator(); i.hasNext();) {
                                Tram9099CerrarVO exp = i.next();
                                log.info("CierreTramiteEspera9099Job - Tratamos el expediente " + exp.getNumExpediente());
                                String enMiCarpeta = exp.getEnMiCarpeta();
                                String numExp = exp.getNumExpediente();
                                int eje = exp.getEjercicio();
                                String proc = exp.getCodProcedimiento();
                                int utr = exp.getUor();
                                int tra9099 = exp.getCodTramite();
                                int traCou9099 = 9099;
                                String desTra9099 = exp.getDesTramite();
                                int ocu9099 = exp.getOcuTramite();
                                int traSalida = exp.getCodTramiteSal();
                                int traVisSalida = exp.getCodTramiteSalVis();
                                String desTraSalida = exp.getDesTramiteSal();
                                int ocuSalida = exp.getOcuTramiteSal();
                               
 
                                //cerrar espera recurso MiCarpeta y avanzar el trámite 9099 ESPERA PRESENTACION RECURSOS en Regexlan
                                try {
                                    //Cerrar espera en MiCarpeta si procede
                                    String resultadoCerrarEspera = "1";
                                    if (enMiCarpeta.equalsIgnoreCase("S")){
                                        //cierre de la espera en MiCarpeta
                                         MELANBIDE43 melanbide43 = new MELANBIDE43();
                                         ModuloIntegracionExternoParamAdicionales adicionales = new ModuloIntegracionExternoParamAdicionales();
                                        adicionales.setOrigenLlamada("IN");
                                        resultadoCerrarEspera =  melanbide43.cerrarEspRecursoStd(codOrg, exp.getCodTramite(), exp.getOcuTramite(), numExp, adicionales);
                                            
                                        if (resultadoCerrarEspera != null && resultadoCerrarEspera.equalsIgnoreCase("0")){
                                            log.info("CierreTramiteEspera9099Job - Cerrada la espera en MiCarpeta para el expediente: "+exp.getNumExpediente());
                                        } else {
                                            log.error("CierreTramiteEspera9099Job - Error al cerrar la espera para el expediente: "+exp.getNumExpediente()+" - No se avanza el expediente en Regexlan");
                                            MeLanbide43Manager.getInstance().insertarLineasLogJob9099(adaptador, numExp, "ERROR", "ERROR - NO se ha cerrado la espera de recurso en MiCarpeta", enMiCarpeta, tra9099, ocu9099, traSalida, ocuSalida);
                                        }
                                               }

                                    if (enMiCarpeta.equalsIgnoreCase("N") || (enMiCarpeta.equalsIgnoreCase("S") && (resultadoCerrarEspera != null && resultadoCerrarEspera.equalsIgnoreCase("0")))) {
                                        adaptador.inicioTransaccion(conTransaccion);
                                        MeLanbide43GestionTramitesExpedienteETraVO tramitePresentacionRecursos = meLanbide43GestionTramitesExpedienteDAO.getTramiteExpedienteETraVOByCodVisible(codOrg, proc, Integer.toString(traCou9099), con);
                                        MeLanbide43GestionTramitesExpedienteETraVO tramiteSalida = meLanbide43GestionTramitesExpedienteDAO.getTramiteExpedienteETraVOByCodVisible(codOrg, proc, Integer.toString(traVisSalida), con);
                                        MeLanbide43JobsTareaEje tf = new MeLanbide43JobsTareaEje();
                                        tf.setCodigoUsuario(usuario);
                                        tf.setNombreUsuario(nombreUsuario);
                                        MeLanbide43GestionTramitesExpedienteRespuesta respuestaTramitacion = meLanbide43GestionTramitesExpedienteDAO.cerrarTramiteConRegistroHistoricoExpediente(codOrg,numExp,tramitePresentacionRecursos,ocu9099,tf,"Finalizado por proceso CierreTramiteEspera9099Job",conTransaccion);
                                        if (respuestaTramitacion != null && respuestaTramitacion.isRespuestaOKBoolean()) {
                                            if (ocuSalida > 0) {
                                                respuestaTramitacion = meLanbide43GestionTramitesExpedienteDAO.abrirTramiteOcuConRegistroHistoricoExpediente(tra9099, ocu9099, ocuSalida, codOrg, numExp, tramiteSalida, "Iniciado por proceso CierreTramiteEspera9099Job", tf, Boolean.TRUE, conTransaccion);
                                                if (respuestaTramitacion != null && respuestaTramitacion.isRespuestaOKBoolean()) {
                                                    log.info("Operacion correcta en el expediente " + numExp + " Procedemos a hacer commit de los cambios en Regexlan.");
                                                    adaptador.finTransaccion(conTransaccion);
                                                    MeLanbide43Manager.getInstance().insertarLineasLogJob9099(adaptador, numExp, "OK", "OK - Procesado correctamente", enMiCarpeta, tra9099, ocu9099, traSalida, ocuSalida);
                                            }else {
                                                    log.error("No se ha podido dar de alta el trámite de salida. Hacemos rollback Operaciones en Regexlan .. "
                                                            + (respuestaTramitacion != null ? respuestaTramitacion.getCodigoDescripcionErrorAsString() : "")
                                                    );
                                                    adaptador.rollBack(conTransaccion);
                                                    MeLanbide43Manager.getInstance().insertarLineasLogJob9099(adaptador, numExp, "ERROR", "ERROR - Rollback Operaciones Regexlan.  No se ha podido dar alta  el tramite de salida en Regexlan: " + (respuestaTramitacion!=null ? respuestaTramitacion.getCodigoDescripcionErrorAsString():""), enMiCarpeta, tra9099, ocu9099, traSalida, ocuSalida);
                                            }
                                        } else {
                                                log.info("Operacion correcta en el expediente " + numExp + " Procedemos a hacer commit de los cambios en Regexlan.");
                                                adaptador.finTransaccion(conTransaccion);
                                                MeLanbide43Manager.getInstance().insertarLineasLogJob9099(adaptador, numExp, "OK", "OK - Procesado correctamente", enMiCarpeta, tra9099, ocu9099, traSalida, ocuSalida);
                                        }
                                        } else {
                                            log.error("No se ha podido cerrar el tramite en regexlan"
                                                    + (respuestaTramitacion != null ? respuestaTramitacion.getCodigoDescripcionErrorAsString() : "")
                                            );
                                            adaptador.rollBack(conTransaccion);
                                            MeLanbide43Manager.getInstance().insertarLineasLogJob9099(adaptador, numExp, "ERROR", "ERROR - Rollback Operaciones Regexlan.  No se ha podido cerrar  el tramite de salida en Regexlan: " + (respuestaTramitacion!=null ? respuestaTramitacion.getCodigoDescripcionErrorAsString():""), enMiCarpeta, tra9099, ocu9099, traSalida, ocuSalida);
                                    }
                             }    
                                } catch (BDException e) {
                                    log.error("Error devolviendo la conexion transaccional... " + e.getErrorCode() + " " + e.getDescripcion() + " " + e.getMensaje(), e);
                                    // La update no propaga error, no hacemos catch
                                    MeLanbide43Manager.getInstance().insertarLineasLogJob9099(adaptador, numExp, "ERROR", "ERROR - " + e.getErrorCode() + " " + e.getDescripcion() + " " + e.getMensaje(), enMiCarpeta, tra9099, ocu9099, traSalida, ocuSalida);
                                    adaptador.rollBack(conTransaccion);
                                } catch (Exception ex) {
                                    MeLanbide43Manager.getInstance().insertarLineasLogJob9099(adaptador, numExp, "ERROR", "ERROR - " + ex.getMessage(), enMiCarpeta, tra9099, ocu9099, traSalida, ocuSalida);
                                    adaptador.rollBack(conTransaccion);
                             }
                            
                            }//for

                            if (dosEntornos) {
                                codOrg++;
                            } else {
                                codOrg = 2;
                            }
                            if (con != null) {
                                con.close();
                            }

                        }//while
                    } catch (Exception e) {
                        log.error(CierreTramiteEspera9099Job.class.getName() + " Error en el job : ", e);
                    } finally {
                        if (con != null) {
                            try {
                                con.close();
                            } catch (SQLException ex) {
                                log.error(CierreTramiteEspera9099Job.class.getName()+" - Error cerrando BBDD", ex);
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
        }
        
                log.error(this.getClass().getName() + ".execute -  Fin " + new Date().toString());
            }//PARA LOCAL QUITAR
        } catch (Exception ex) {
            log.error(this.getClass().getName() + " Error: " + ex.getMessage(), ex);
        }
        
    }
       
    
    
}
