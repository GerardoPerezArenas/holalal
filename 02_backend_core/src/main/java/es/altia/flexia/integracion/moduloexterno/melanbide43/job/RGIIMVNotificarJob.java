package es.altia.flexia.integracion.moduloexterno.melanbide43.job;

import com.lanbide.lan6.errores.bean.ErrorBean;
import es.altia.flexia.integracion.moduloexterno.melanbide43.dao.RGI_JobDAO;
import es.altia.flexia.integracion.moduloexterno.melanbide43.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide43.util.ConstantesMeLanbide43;
import es.altia.flexia.integracion.moduloexterno.melanbide43.util.MeLanbide43BDUtil;
import es.altia.flexia.integracion.moduloexterno.melanbide43.util.RGI_IMV_JobUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.ExpedienteNotificarVO;
import es.altia.flexia.integracion.moduloexterno.pluginnotificacionplatea.NotificacionBVO;
import es.altia.flexia.integracion.moduloexterno.pluginnotificacionplatea.PluginNotificacionPlatea;
import es.altia.flexia.integracion.moduloexterno.pluginnotificacionplatea.PluginNotificacionPlateaNotificacionDAO;
import es.altia.flexia.notificacion.persistence.AutorizadoNotificacionManager;
import es.altia.flexia.notificacion.plugin.FactoriaPluginNotificacion;
import es.altia.flexia.notificacion.plugin.PluginNotificacion;
import es.altia.flexia.notificacion.vo.AdjuntoNotificacionVO;
import es.altia.flexia.notificacion.vo.AutorizadoNotificacionVO;
import es.altia.flexia.notificacion.vo.NotificacionVO;
import es.altia.flexia.registro.digitalizacion.lanbide.util.GestionAdaptadoresLan6Config;
import es.altia.flexiaWS.documentos.bd.util.Configuracion;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import es.lanbide.lan6.adaptadoresPlatea.dokusi.beans.Lan6Documento;
import es.lanbide.lan6.adaptadoresPlatea.dokusi.servicios.Lan6DokusiServicios;
import es.lanbide.lan6.adaptadoresPlatea.excepciones.Lan6Excepcion;
import es.lanbide.lan6.adaptadoresPlatea.informarConsulta.beans.Lan6Interesado;
import es.lanbide.lan6.adaptadoresPlatea.notificaciones.beans.Lan6TramiteNotificacion;
import es.lanbide.lan6.adaptadoresPlatea.notificaciones.servicios.Lan6NotificacionesServicios;
import es.lanbide.lan6.adaptadoresPlatea.utilidades.constantes.Lan6Constantes;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author kepa
 */
public class RGIIMVNotificarJob implements Job {

    private final Logger log = LogManager.getLogger(RGIIMVNotificarJob.class);
    public static final String DOT = ".";
    private static final GestionAdaptadoresLan6Config gestionAdaptadoresLan6Config = new GestionAdaptadoresLan6Config();

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {

        String servidor = ConfigurationParameter.getParameter(ConstantesMeLanbide43.CAMPO_SERVIDOR, ConstantesMeLanbide43.FICHERO_PROPIEDADES);
        long inicio = System.currentTimeMillis();
        int contador = 0;
        if (servidor.equals(System.getProperty("weblogic.Name"))) {//PARA LOCAL QUITAR
            synchronized (jec) {
                Connection con = null;
                try {
                    log.info("=======================================> RGI_NotificarJob() - Execute lanzado " + System.getProperty("weblogic.Name") + "  =======================================  ");
                    log.info("=======> RGI_NotificarJob() - Tiempo inicio " + inicio);

                    Integer codOrg = Integer.valueOf(ConfigurationParameter.getParameter("COD_ORG", ConstantesMeLanbide43.FICHERO_PROPIEDADES));
                    boolean dosEntornos = Boolean.getBoolean(ConfigurationParameter.getParameter("DOS_ENTORNOS", ConstantesMeLanbide43.FICHERO_PROPIEDADES));
                    Configuracion.getParamsBD(codOrg.toString());
                    RGI_JobDAO rgiJobDAO = RGI_JobDAO.getInstance();
                    while (codOrg < 2) {
                        MeLanbide43BDUtil meLanbide43BDUtil = new MeLanbide43BDUtil();
                        AdaptadorSQLBD adaptador = meLanbide43BDUtil.getAdaptSQLBD(String.valueOf(codOrg));
                        con = adaptador.getConnection();

                        String codTramReqSub = "111";
                        String codTramSeguimientoSub = "112";
                        String codTramRequisitos = "113";
                        String codTramSeguimientoRequisitos = "114";
                        String codTramRequisitosPac = "213";
                        String codTramSeguimientoRequisitosPac = "214";
                        String codTramDomicilio = "120";
                        String codTramSeguimientoDomi = "121";
                        String codTramResolRGI = "6";
                        String codTramPropuesta3 = "3";
                        String codTramPropuesta4 = "4";
                        String codTramReintegro = "1";

                        String nombreCampoRequerir = "IDDOCREQUER";
                        String nombreCampoRequisitos = "IDDOCREQUIS";
                        String nombreCampoResolucion = "IDDOCRESOL";
                        String nombreCampoReintegro = "IDDOCINIREI";

                        String[] RGI_Requerir   = {"RGI", "REV", "REI", "REN", "RKP", "PAC", "PC"}; // 111
                        String[] RGI_Requisitos = {"RGI", "REV", "REN", "REC", "PAC", "PC"};        // 113
                        String[] PAC_Requisitos = {"PAC"};                                          // 213
                        String[] RGI_Domicilio  = {"RGI", "PC"};                                    // 120
                        String[] RGI_Resolucion = {"RGI", "REV", "REN", "PAC", "PC"};               // 6
                        String[] RGI_Propuesta3 = {"REI"};                                          // 3 
                        String[] RGI_Propuesta4 = {"RKP", "REC", "DEU"};                            // 4 
                        String[] RGI_Reintegro  = {"REI", "DEU"};                                   // 1

//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
                        // RGI
                        String procedimientoFlexia = "RGI";
                        String procedimientoPlatea = gestionAdaptadoresLan6Config.getCodProcedimientoPlatea(procedimientoFlexia);
                        List<ExpedienteNotificarVO> expRequerirRGI = new ArrayList<ExpedienteNotificarVO>();
                        List<ExpedienteNotificarVO> expRequisitosRGI = new ArrayList<ExpedienteNotificarVO>();
                        List<ExpedienteNotificarVO> expRequisitosPAC = new ArrayList<ExpedienteNotificarVO>();
                        List<ExpedienteNotificarVO> expDomicilioRGI = new ArrayList<ExpedienteNotificarVO>();
                        List<ExpedienteNotificarVO> expResolucionRGI = new ArrayList<ExpedienteNotificarVO>();
                        List<ExpedienteNotificarVO> expPropuesta3RGI = new ArrayList<ExpedienteNotificarVO>();
                        List<ExpedienteNotificarVO> expPropuesta4RGI = new ArrayList<ExpedienteNotificarVO>();
                        List<ExpedienteNotificarVO> expReintegroRGI = new ArrayList<ExpedienteNotificarVO>();

                        List<ExpedienteNotificarVO> expedientesHijos = null;

                        int tope = Integer.parseInt(ConfigurationParameter.getParameter("TOPE_NOTIFICACIONES_RGI", ConstantesMeLanbide43.FICHERO_PROPIEDADES));
                        // 111 - Requerimiento Subsanación - RGI
                        log.info("=> Inciamos RGI 111 - REQUERIMIENTO SUBSANACIÓN");
                        for (String procedimientoHijo : RGI_Requerir) {
                            log.debug("Busco expedientes de " + procedimientoHijo);
                            expedientesHijos = rgiJobDAO.recuperarExpedientesRequerir(procedimientoHijo, nombreCampoRequerir, codTramReqSub, codTramSeguimientoSub, con);
                            if (!expedientesHijos.isEmpty()) {
                                log.info("Se recuperan " + expedientesHijos.size() + " expedientes de " + procedimientoHijo);
                                expRequerirRGI.addAll(expedientesHijos);
                                contador += expedientesHijos.size();
                            }
                            if (contador > tope) {
                                break;
                            }
                        }
                        if (!expRequerirRGI.isEmpty()) {
                            log.info("Se recuperan " + expRequerirRGI.size() + " expedientes de " + procedimientoFlexia + " e Hijos");
                            try {
                                envioNotificaciones(procedimientoFlexia, procedimientoPlatea, codTramReqSub, codOrg.toString(), expRequerirRGI, con);
                            } catch (Exception e) {
                                log.error("Error no controlado al intentar enviar notificaciones de " + procedimientoFlexia + " tramite con codigo externo " + codTramReqSub);
                            }
                        }

                        if (contador <= tope) {//  113 - Requisitos Incumplidos - RGI
                        log.info("=> Inciamos RGI 113 - REQUISITOS INCUMPLIDOS");
                        for (String procedimientoHijo : RGI_Requisitos) {
                                log.debug("Busco expedientes de " + procedimientoHijo);
                            expedientesHijos = rgiJobDAO.recuperarExpedientesRequerir(procedimientoHijo, nombreCampoRequisitos, codTramRequisitos, codTramSeguimientoRequisitos, con);
                            if (!expedientesHijos.isEmpty()) {
                                log.info("Se recuperan " + expedientesHijos.size() + " expedientes de " + procedimientoHijo);
                                expRequisitosRGI.addAll(expedientesHijos);
                                    contador += expedientesHijos.size();
                                }
                                if (contador > tope) {
                                    break;
                            }
                        }
                        if (expRequisitosRGI != null && !expRequisitosRGI.isEmpty()) {
                            log.info("Se recuperan " + expRequisitosRGI.size() + " expedientes de " + procedimientoFlexia + " e Hijos");
                            try {
                                envioNotificaciones(procedimientoFlexia, procedimientoPlatea, codTramRequisitos, codOrg.toString(), expRequisitosRGI, con);
                            } catch (Exception e) {
                                log.error("Error no controlado al intentar enviar notificaciones de " + procedimientoFlexia + " tramite con codigo externo " + codTramRequisitos);
                            }
                        }

                            if (contador <= tope) {//  213 - Requisitos Incumplidos - RGI
                        log.info("=> Inciamos RGI 213 - REQUISITOS INCUMPLIDOS");
                        for (String procedimientoHijo : PAC_Requisitos) {
                                    log.debug("Busco expedientes de " + procedimientoHijo);
                            expedientesHijos = rgiJobDAO.recuperarExpedientesRequerir(procedimientoHijo, nombreCampoRequisitos, codTramRequisitosPac, codTramSeguimientoRequisitosPac, con);
                            if (!expedientesHijos.isEmpty()) {
                                log.info("Se recuperan " + expedientesHijos.size() + " expedientes de " + procedimientoHijo);
                                expRequisitosPAC.addAll(expedientesHijos);
                                        contador += expedientesHijos.size();
                                    }
                                    if (contador > tope) {
                                        break;
                            }

                        }
                        if (expRequisitosPAC != null && !expRequisitosPAC.isEmpty()) {
                            log.info("Se recuperan " + expRequisitosPAC.size() + " expedientes de " + procedimientoFlexia + " e Hijos");
                            try {
                                envioNotificaciones(procedimientoFlexia, procedimientoPlatea, codTramRequisitosPac, codOrg.toString(), expRequisitosPAC, con);
                            } catch (Exception e) {
                                log.error("Error no controlado al intentar enviar notificaciones de " + procedimientoFlexia + " tramite con codigo externo " + codTramRequisitosPac);
                            }
                        }

                                if (contador <= tope) { //  120 - Acuerdo Acceso Domicilio - RGI
                        log.info("=> Inciamos RGI 120 - ACUERDO ACCESO A DOMICILIO");
                        for (String procedimientoHijo : RGI_Domicilio) {
                                        log.debug("Busco expedientes de " + procedimientoHijo);
                            expedientesHijos = rgiJobDAO.recuperarExpedientesRequerir(procedimientoHijo, nombreCampoRequisitos, codTramDomicilio, codTramSeguimientoDomi, con);
                            if (!expedientesHijos.isEmpty()) {
                                log.info("Se recuperan " + expedientesHijos.size() + " expedientes de " + procedimientoHijo);
                                expDomicilioRGI.addAll(expedientesHijos);
                                            contador += expedientesHijos.size();
                                        }
                                        if (contador > tope) {
                                            break;
                            }
                        }
                        if (expDomicilioRGI != null && !expDomicilioRGI.isEmpty()) {
                            log.info("Se recuperan " + expDomicilioRGI.size() + " expedientes de " + procedimientoFlexia + " e Hijos");
                            try {
                                envioNotificaciones(procedimientoFlexia, procedimientoPlatea, codTramDomicilio, codOrg.toString(), expDomicilioRGI, con);
                            } catch (Exception e) {
                                log.error("Error no controlado al intentar enviar notificaciones de " + procedimientoFlexia + " tramite con codigo externo " + codTramDomicilio);
                            }
                        }

                                    if (contador <= tope) { // 6 - Resolucion de RGI
                        log.info("=> Inciamos RGI 6 - RESOLUCION ");
                        for (String procedimientoHijo : RGI_Resolucion) {
                                            log.debug("Busco expedientes de " + procedimientoHijo);
                            expedientesHijos = rgiJobDAO.recuperarExpResolver(procedimientoHijo, nombreCampoResolucion, codTramResolRGI, con);
                            if (!expedientesHijos.isEmpty()) {
                                log.info("Se recuperan " + expedientesHijos.size() + " expedientes de " + procedimientoHijo);
                                expResolucionRGI.addAll(expedientesHijos);
                                                contador += expedientesHijos.size();
                                            }
                                            if (contador > tope) {
                                                break;
                            }
                        }
                        if (expResolucionRGI != null && !expResolucionRGI.isEmpty()) {
                            log.info("Se recuperan " + expResolucionRGI.size() + " expedientes de " + procedimientoFlexia + " e Hijos");
                            try {
                                envioNotificaciones(procedimientoFlexia, procedimientoPlatea, codTramResolRGI, codOrg.toString(), expResolucionRGI, con);
                            } catch (Exception e) {
                                log.error("Error no controlado al intentar enviar notificaciones de " + procedimientoFlexia + " tramite con codigo externo " + codTramResolRGI);
                            }
                        }

                                        if (contador <= tope) {// 3 - Propuesta Resolucion de RGI
                        log.info("=> Inciamos RGI 3 - PROPUESTA RESOLUCION");
                        for (String procedimientoHijo : RGI_Propuesta3) {
                                                log.debug("Busco expedientes de " + procedimientoHijo);
                            expedientesHijos = rgiJobDAO.recuperarExpResolver(procedimientoHijo, nombreCampoResolucion, codTramPropuesta3, con);
                            if (!expedientesHijos.isEmpty()) {
                                log.info("Se recuperan " + expedientesHijos.size() + " expedientes de " + procedimientoHijo);
                                expPropuesta3RGI.addAll(expedientesHijos);
                                                    contador += expedientesHijos.size();
                                                }
                                                if (contador > tope) {
                                                    break;
                            }
                        }
                        if (expPropuesta3RGI != null && !expPropuesta3RGI.isEmpty()) {
                            log.info("Se recuperan " + expPropuesta3RGI.size() + " expedientes de " + procedimientoFlexia + " e Hijos");
                            try {
                                envioNotificaciones(procedimientoFlexia, procedimientoPlatea, codTramPropuesta3, codOrg.toString(), expPropuesta3RGI, con);
                            } catch (Exception e) {
                                log.error("Error no controlado al intentar enviar notificaciones de " + procedimientoFlexia + " tramite con codigo externo " + codTramPropuesta4);
                            }
                        }

                                            if (contador <= tope) {// 4 - Propuesta Resolucion de RGI
                        log.info("=> Inciamos RGI 4 - PROPUESTA RESOLUCION");
                        for (String procedimientoHijo : RGI_Propuesta4) {
                                                    log.debug("Busco expedientes de " + procedimientoHijo);
                            expedientesHijos = rgiJobDAO.recuperarExpResolver(procedimientoHijo, nombreCampoResolucion, codTramPropuesta4, con);
                            if (!expedientesHijos.isEmpty()) {
                                log.info("Se recuperan " + expedientesHijos.size() + " expedientes de " + procedimientoHijo);
                                expPropuesta4RGI.addAll(expedientesHijos);
                                                        contador += expedientesHijos.size();
                                                    }
                                                    if (contador > tope) {
                                                        break;
                            }
                        }
                        if (expPropuesta4RGI != null && !expPropuesta4RGI.isEmpty()) {
                            log.info("Se recuperan " + expPropuesta4RGI.size() + " expedientes de " + procedimientoFlexia + " e Hijos");
                            try {
                                envioNotificaciones(procedimientoFlexia, procedimientoPlatea, codTramPropuesta4, codOrg.toString(), expPropuesta4RGI, con);
                            } catch (Exception e) {
                                log.error("Error no controlado al intentar enviar notificaciones de " + procedimientoFlexia + " tramite con codigo externo " + codTramPropuesta4);
                            }
                        }

                                                if (contador <= tope) {// 1 - inicio Reintegro
                        log.info("=> Inciamos RGI 1 - INICIO REINTEGRO");
                        for (String procedimientoHijo : RGI_Reintegro) {
                                                        log.debug("Busco expedientes de " + procedimientoHijo);
                            expedientesHijos = rgiJobDAO.recuperarExpResolver(procedimientoHijo, nombreCampoReintegro, codTramReintegro, con);
                            if (!expedientesHijos.isEmpty()) {
                                log.info("Se recuperan " + expedientesHijos.size() + " expedientes de " + procedimientoHijo);
                                expReintegroRGI.addAll(expedientesHijos);
                                                            contador += expedientesHijos.size();
                                                        }
                                                        if (contador > tope) {
                                                            break;
                            }
                        }
                        if (expReintegroRGI != null && !expReintegroRGI.isEmpty()) {
                            log.info("Se recuperan " + expReintegroRGI.size() + " expedientes de " + procedimientoFlexia + " e Hijos");
                            try {
                                envioNotificaciones(procedimientoFlexia, procedimientoPlatea, codTramReintegro, codOrg.toString(), expReintegroRGI, con);
                            } catch (Exception e) {
                                log.error("Error no controlado al intentar enviar notificaciones de " + procedimientoFlexia + " tramite con codigo externo " + codTramReintegro);
                            }
                        }
                                                } else {
                                                    log.error("Superado el límite de " + tope + " expedientes.");
                        }
                                            } else {
                                                log.error("Superado el límite de " + tope + " expedientes.");
                            }
                                        } else {
                                            log.error("Superado el límite de " + tope + " expedientes.");
                        }
                                    } else {
                                        log.error("Superado el límite de " + tope + " expedientes.");
                            }
                                } else {
                                    log.error("Superado el límite de " + tope + " expedientes.");
                        }
                            } else {
                                log.error("Superado el límite de " + tope + " expedientes.");
                            }
                        } else {
                            log.error("Superado el límite de " + tope + " expedientes.");
                        }

                        if (dosEntornos) {
                            codOrg++;
                        } else {
                            codOrg = 2;
                        }
//                        if (con != null) {
//                            con.close();
//                        }

                    }
                } catch (BDException e) {
                    log.error(" =======> RGI_NotificarJob() -  Error en el job : ", e);
                } catch (NumberFormatException e) {
                    log.error(" =======> RGI_NotificarJob() -  Error en el job : ", e);
                } catch (SQLException e) {
                    log.error(" =======> RGI_NotificarJob() -  Error en el job : ", e);
                } catch (Exception ex) {
                    log.error(" =======> RGI_NotificarJob() -  Error en el job : ", ex);
                } finally {
                    try {
                        con.close();
                    } catch (SQLException ex) {
                        log.error(" =======> RGI_NotificarJob() -  Error cerrando BBDD", ex);
                    }
                }
                if (con != null) {
                    try {
                        con.close();
                    } catch (SQLException ex) {
                        log.error(" =======> RGI_NotificarJob() -  Error cerrando BBDD", ex);
                    }
                }
            }// synchroniced
        } // if servidor
        long fin = System.currentTimeMillis();
        long transcurrido = fin - inicio;
        log.info(" =======> RGI_NotificarJob() - Tiempo inicio - " + new SimpleDateFormat("hh:mm:ss:SSS").format(new Date(inicio)));
        log.info(" =======> RGI_NotificarJob() - Tiempo fin    - " + new SimpleDateFormat("hh:mm:ss:SSS").format(new Date(fin)));
        log.info(" =======> RGI_NotificarJob() - Transcurrido  - " + new SimpleDateFormat("hh:mm:ss:SSS").format(new Date(transcurrido)));
        log.info(" =======> RGI_NotificarJob() - Procesados  - " + contador);
    }

    /**
     *
     * @param procedimientoPlatea
     * @param con
     * @param codOrg
     * @param paramsBD
     * @param acto
     * @param textoNotificado
     * @param expedientesTratar
     * @throws SQLException
     * @throws Exception
     */
    private void envioNotificaciones(String procedimientoPadre, String procedimientoPlatea, String codTramExterno, String codOrg, List<ExpedienteNotificarVO> expedientesTratar, Connection conEnvio /*AdaptadorSQLBD adapt*/) throws SQLException, Exception {
        log.info("envioNotificaciones INIT ==> " + procedimientoPadre + " - " + codTramExterno);
        //Connection conEnvio = null;
        String actoNotificacion = null;
        String actoNotificacion_eu = null;

        String textoNotificado = null;
        String idActoNotificadoLan6 = null;
        String tipoNotificacionFlexia = null;
        String expedienteMiCarpeta = null;
        String mensajeError = null;
        String procedimientoExpediente = null;

        int idNotificacion = 0;
        
        if (!expedientesTratar.isEmpty()) {
            String[] paramsBD = Configuracion.getParamsBD(codOrg);
            //  conEnvio = adapt.getConnection();
            RGI_JobDAO rgiJobDAO = RGI_JobDAO.getInstance();
            PluginNotificacion pluginNotificacion = FactoriaPluginNotificacion.getImpl(codOrg); // se llama a métodos de FLEXIA
            PluginNotificacionPlateaNotificacionDAO notificacionPluginDAO = PluginNotificacionPlateaNotificacionDAO.getInstance();
            boolean esActivo = true;
            boolean esHijo;
            boolean huerfano;
            boolean pubSede;
            boolean creada;
            boolean interesadosRepetidos;

            //Recorremos los expedientes a notificar
            for (ExpedienteNotificarVO expNotificarVO : expedientesTratar) {
                NotificacionVO notificacionFlexiaVO = new NotificacionVO();
                NotificacionBVO notificacionPLATEA = new NotificacionBVO();
                esActivo = true;
                esHijo = false;
                huerfano = false;
                pubSede = true;
                creada = false;
                interesadosRepetidos = false;
                String descTramEu = "";

                try {
                    String[] datosExp = expNotificarVO.getNumExpediente().split(ConstantesMeLanbide43.BARRA_SEPARADORA);
                    procedimientoExpediente = datosExp[1];

                    textoNotificado = rgiJobDAO.recuperaTextoNotificacIon(expNotificarVO.getNumExpediente(), expNotificarVO.getCodTramite(), expNotificarVO.getOcurrenciaTramite(), conEnvio);

                    notificacionFlexiaVO.setNumExpediente(expNotificarVO.getNumExpediente());
                    notificacionFlexiaVO.setCodigoProcedimiento(expNotificarVO.getProcedimiento());
                    notificacionFlexiaVO.setEjercicio(Integer.parseInt(expNotificarVO.getEjercicio()));
                    notificacionFlexiaVO.setCodigoMunicipio(Integer.parseInt(expNotificarVO.getCodigoMunicipio()));
                    notificacionFlexiaVO.setCodigoTramite(Integer.parseInt(expNotificarVO.getCodTramite()));
                    notificacionFlexiaVO.setOcurrenciaTramite(Integer.parseInt(expNotificarVO.getOcurrenciaTramite()));
                    notificacionFlexiaVO.setTextoNotificacion(textoNotificado);
                    notificacionFlexiaVO.setCodDepartamento(codOrg);

                    // ACTO NOTIFICADO
                    if (codTramExterno.equalsIgnoreCase("111")) { // 1 - REQUERIMIENTO SUBSANACION; 
                        idActoNotificadoLan6 = gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("ACTO_NOTIFICADO_REQ_SUB");
                        actoNotificacion = gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("ACTO_NOTIFICADO_REQ_SUB_DESC");
                        actoNotificacion_eu = gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("ACTO_NOTIFICADO_REQ_SUB_DESC_EU");
                        descTramEu = "DOKUMENTAZIOA ESKATZEA";
                        tipoNotificacionFlexia = "1";
                    } else if (codTramExterno.equalsIgnoreCase("113")) { //  3 - REQUERIMIENTO ALEGACION; Requisitos incumplidos
                        idActoNotificadoLan6 = gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("ACTO_NOTIFICADO_REQ_ALEG");
                        actoNotificacion = "REQUISITOS INCUMPLIDOS";
                        actoNotificacion_eu = "BETE GABEKO BETEKIZUNAK";
                        descTramEu = "BETE GABEKO BETEKIZUNAK";
                        tipoNotificacionFlexia = "3";
                        if (expNotificarVO.getProcedimiento().equalsIgnoreCase("PAC")) { // LIQUIDACION
                            idActoNotificadoLan6 = gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("ACTO_NOTIFICADO_LIQUID");
                            actoNotificacion = gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("ACTO_NOTIFICADO_LIQUID_DESC");
                            actoNotificacion_eu = gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("ACTO_NOTIFICADO_LIQUID_DESC_EU");
                            descTramEu = "BEHIN-BEHINEKO LIKIDAZIOA";
                            tipoNotificacionFlexia = "13";
                        }
                    } else if (codTramExterno.equalsIgnoreCase("213")) { //  3 - REQUERIMIENTO ALEGACION; Requisitos incumplidos
                        idActoNotificadoLan6 = gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("ACTO_NOTIFICADO_REQ_ALEG");
                        actoNotificacion = "REQUISITOS INCUMPLIDOS";
                        actoNotificacion_eu = "BETE GABEKO BETEKIZUNAK";
                        descTramEu = "BETE GABEKO BETEKIZUNAK";
                        tipoNotificacionFlexia = "3";
                    } else if (codTramExterno.equalsIgnoreCase("120")) { // ACUERDO ACCESO A DOMICILIO
                        idActoNotificadoLan6 = gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("ACTO_NOTIFICADO_NOTIFACUERDO"); // PENDIENTE DEFINIR
                        actoNotificacion = "ACUERDO ACCESO A DOMICILIO";
                        actoNotificacion_eu = "ETXEBIZITZA ESKURATZEKO AKORDIOA";
                        descTramEu = "ETXEBIZITZA ESKURATZEKO AKORDIOA";
                        tipoNotificacionFlexia = "14";
                    } else if (codTramExterno.equalsIgnoreCase("3") || codTramExterno.equalsIgnoreCase("4") || codTramExterno.equalsIgnoreCase("6")) {  //   2 - RESOLUCION;
                        idActoNotificadoLan6 = gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("ACTO_NOTIFICADO_RESOL");
                        actoNotificacion = gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("ACTO_NOTIFICADO_RESOL_DESC");
                        actoNotificacion_eu = gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("ACTO_NOTIFICADO_RESOL_DESC_EU");
                        if (expNotificarVO.getProcedimiento().equalsIgnoreCase("RGI")) { // PENDIENTE ADOPTAR RESOLUCION RECONOCIMIENTO
                            descTramEu = "AITORTZEKO EBAZPENA EMATEKE";
                        } else if (expNotificarVO.getProcedimiento().equalsIgnoreCase("REV") || expNotificarVO.getProcedimiento().equalsIgnoreCase("PAC") || expNotificarVO.getProcedimiento().equalsIgnoreCase("PC")) { // PENDIENTE ADOPTAR RESOLUCION REVISION
                            descTramEu = "BERRIKUSTEKO EBAZPENA HARTZEKO ZAIN";
                        } else if (expNotificarVO.getProcedimiento().equalsIgnoreCase("REN")) { // PENDIENTE ADOPTAR RESOLUCION RENOVACION
                            descTramEu = "BERRITZEKO EBAZPENA EMATEKE";
                        } else if (expNotificarVO.getProcedimiento().equalsIgnoreCase("RKP")) { // PROPUESTA MODIFICACION CUANTIA FRACCIONAMIENTO
                            descTramEu = "ZATIKAPENAREN ZENBATEKOA ALDATZEKO PROPOSAMENA";
                        } else if (expNotificarVO.getProcedimiento().equalsIgnoreCase("REC") || expNotificarVO.getProcedimiento().equalsIgnoreCase("IMVRC")) { // PROPUESTA RESOLUCION RECURSO
                            descTramEu = "ERREKURTSOAREN EBAZPEN-PROPOSAMENA";
                        } else if (expNotificarVO.getProcedimiento().equalsIgnoreCase("REI") || expNotificarVO.getProcedimiento().equalsIgnoreCase("IMVRI")) { // PROPUESTA RESOLUCION REINTEGRO 
                            descTramEu = "DIRUA ITZULTZEKO EBAZPEN-PROPOSAMENA";
                        } else if (expNotificarVO.getProcedimiento().equalsIgnoreCase("IMV") || expNotificarVO.getProcedimiento().equalsIgnoreCase("IMVRV")) {
                            descTramEu = "AMAIERA";
                        } else if (expNotificarVO.getProcedimiento().equalsIgnoreCase("DEU")) { // PROPUESTA 
                            descTramEu = "PROPOSAMENA";
                        }
                        tipoNotificacionFlexia = "2";
                    } else if (codTramExterno.equalsIgnoreCase("1")) { //   2 - INICIO REINTEGRO;
                        idActoNotificadoLan6 = gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("ACTO_NOTIFICADO_RESOL");
                        actoNotificacion = gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("ACTO_NOTIFICADO_RESOL_DESC") + " - Inicio Reintegro";
                        actoNotificacion_eu = gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("ACTO_NOTIFICADO_RESOL_DESC_EU") + " - Itzulketaren hasiera";
                        descTramEu = "ITZULKETAREN HASIERA";
                        tipoNotificacionFlexia = "2";
                    }
                    expNotificarVO.setDesTramite_eu(descTramEu);
                    // comprobar si es COMUNICACION para cambiar el tipo
                    if (expNotificarVO.getTipoEnvio() != null) {
                        if (expNotificarVO.getTipoEnvio().equalsIgnoreCase("C")) {
                            // COMUNICACION 
                            idActoNotificadoLan6 = gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("ACTO_NOTIFICADO_COMUNICATION");
                            actoNotificacion = gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("ACTO_NOTIFICADO_COMUNICATION_DESC") + " - " + actoNotificacion;
                            actoNotificacion_eu = gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("ACTO_NOTIFICADO_COMUNICATION_DESC_EU") + " - " + actoNotificacion_eu;
                            tipoNotificacionFlexia = "4";
                        }
                    } else {
                        // no hay Tipo envio
                    }

                    log.info("EXPEDIENTE: " + expNotificarVO.getNumExpediente());
                    log.info("TRAMITE: " + expNotificarVO.getDesTramite());
                    log.info("COD TIPO notificación FLEXIA: " + tipoNotificacionFlexia);
                    log.info("ID del ACTO Notificado: " + idActoNotificadoLan6);
                    log.info("Descripción ACTO notificado: " + actoNotificacion + " / " + actoNotificacion_eu);
                    log.info("Texto: " + textoNotificado);

                    notificacionFlexiaVO.setActoNotificado(actoNotificacion);
                    notificacionFlexiaVO.setCodigoTipoNotificacion(tipoNotificacionFlexia);

                    if (expNotificarVO.getProcedimiento().equalsIgnoreCase(procedimientoPadre)) {
                        expedienteMiCarpeta = expNotificarVO.getNumExpediente();
                    } else {
                        log.info(expNotificarVO.getNumExpediente() + " es hijo");
                        esHijo = true;
                        expedienteMiCarpeta = rgiJobDAO.buscaExpedientePadre(expNotificarVO.getNumExpediente(), procedimientoPadre, conEnvio);
                        if (expedienteMiCarpeta != null) {
                            log.info("Expediente PADRE: " + expedienteMiCarpeta);
                        } else {
                            mensajeError = "No se ha encontrado el Expediente PADRE del expediente " + expNotificarVO.getNumExpediente() + " No se envía la notificación";
                            log.error(mensajeError);
                            huerfano = true;
                        }
                    }

                    log.info(" ===> Creamos la notificación por defecto");
                    // Se crea la notificiacion por defecto si es que no existe
                    creada = rgiJobDAO.crearNotificacionImvRgi(notificacionFlexiaVO, conEnvio);
                    if (creada) {
                        log.info("Notificacion por defecto creada");
                        log.info(" ===> Entramos a obtener la notificación");
                        notificacionFlexiaVO = rgiJobDAO.getNotificacionImvRgi(notificacionFlexiaVO, conEnvio);
                        idNotificacion = notificacionFlexiaVO.getCodigoNotificacion();
                        log.info("Notificacion obtenida. ID: " + idNotificacion);
                        if (!huerfano) {
                            // comprobar ACTIVO / HISTORICO
                            if (rgiJobDAO.expedienteHistorico(expedienteMiCarpeta, conEnvio)) {
                                esActivo = false;
                                log.info("El expediente " + expedienteMiCarpeta + " está en HIST");
                            }
                            log.info("Expediente " + expedienteMiCarpeta + " está " + (esActivo ? "ACTIVO" : "en HISTÓRICO"));
                            // compruebo si tiene PUBSEDE
                            if (!rgiJobDAO.publicadoEnSede(expedienteMiCarpeta, esActivo, conEnvio)) {
                                mensajeError = "El expediente " + expedienteMiCarpeta + " No tiene grabado PUBSEDE. No se envía la notificación";
                                log.error(mensajeError);
                                pubSede = false;
                            }

                            if (pubSede) {
                                // comprobar que no hay más de un tercero con rol 1-Interesado en el expediente Padre
                                interesadosRepetidos = rgiJobDAO.hayVariosInteresados(expedienteMiCarpeta, esActivo, conEnvio);
                                if (!interesadosRepetidos) {
                                    // solo hay un tercero con rol 1 en el expediente RGI
                        log.info(" ===> Ponemos marca de Notificación Telemática en E_EXT");
                                    if (rgiJobDAO.marcaNotificacionInteresadoRepresentante(expedienteMiCarpeta, esActivo, conEnvio)) {
// AKIIIIII
                                        /* SE RECUPERAN LOS INTERESADOS DEL EXPEDIENTE */
                                        ArrayList<AutorizadoNotificacionVO> arrayAutorizadoNotif = new ArrayList<AutorizadoNotificacionVO>();
                                        arrayAutorizadoNotif = rgiJobDAO.getInteresadosExpedientePluginNotificacion(expedienteMiCarpeta, esActivo, idNotificacion, conEnvio);
                                        notificacionFlexiaVO.setAutorizados(arrayAutorizadoNotif);

                                        log.debug("INTERESADOS: " + notificacionFlexiaVO.getAutorizados().size());
                                        if (notificacionFlexiaVO.getAdjuntosExternos() != null) {
                                            log.debug("DOCUMENTOS: " + notificacionFlexiaVO.getAdjuntosExternos().size());
                                        }

                                        log.info(" ===> Intentamos grabar la notificación: " + notificacionFlexiaVO.getNumExpediente());
                                        // aqui se insertan los autorizados en AUTORIZADO_NOTIFICACION
                                        boolean actualizada = pluginNotificacion.actualizarNotificacion(notificacionFlexiaVO, paramsBD);
                            if (actualizada) {
                                String oidAnexo = null;
                                Lan6Documento lan6Anexo = null;
                                Lan6DokusiServicios servicios = new Lan6DokusiServicios(procedimientoPadre);

                                //Firmamos y localizamos el documento tiene que ser un documento pdfa
                                // TODO falta preguntar si nos lo enviaran como pdfa si no fuera asi se podria modificar mas adelante
                                //             firmarLocalizarDocumentoOid(expNotificar.getOidDocumentoNotificar(), procedimientoPlatea);
                                            log.info(" ===> Recuperamos lan6Documento -  oid: " + expNotificarVO.getOidDocumentoNotificar() + " con el codigo lan6 " + procedimientoPadre);
                                            Lan6Documento lan6Documento = servicios.consultarDocumento(expNotificarVO.getOidDocumentoNotificar());
                                // si no está firmado se manda a firmar
                                log.info("Documento recuperado. OID: " + lan6Documento.getIdDocumento());

                                if (lan6Documento.getFirmas() != null) {
                                    log.info(" ===> Hay firmas");
                                    if (lan6Documento.getFirmas().isEmpty()) {
                                        log.info(" ===> Firmar documento");
                                        lan6Documento.setConvertirAPdf(true);
                                        lan6Documento = servicios.firmarDocumento(lan6Documento);
                                    }
                                } else {
                                    log.info(" ===> NO Hay firmas");
                                log.info(" ===> Firmar documento");
                                    lan6Documento.setConvertirAPdf(true);
                                lan6Documento = servicios.firmarDocumento(lan6Documento);
                                }
                                if (lan6Documento.getFirmas() != null) {
                                    log.info(" ===> AHora Hay firmas");
                                }
                                byte[] fichero = lan6Documento.getContenido();
                                            String mimeTypeContent = RGI_IMV_JobUtils.getInstance().getMimeTypeFromExtension(lan6Documento.getFormat());
                                String nombreFichero = lan6Documento.getNombre();

                                            log.info("Id documento:\t" + lan6Documento.getIdDocumento());
                                            log.info("Id expediente:\t" + lan6Documento.getIdExpediente());
                                log.info("Titulo:\t\t\t" + lan6Documento.getTitulo());
                                log.info("Nombre:\t\t\t" + lan6Documento.getNombre());
                                log.info("Formato:\t\t\t" + lan6Documento.getFormat());
                                log.info("Tipo documental:\t" + lan6Documento.getTipoDocumental());
                                            log.info("Serie Documental:" + lan6Documento.getSerieDocumental());
                                log.info("Asunto Documental: " + lan6Documento.getAsuntoDocumental());
                                log.info("Naturaleza:\t\t" + lan6Documento.getNaturaleza());
                                log.info("Origen:\t\t\t" + lan6Documento.getOrigen());
                                log.info("Convertir a PDF:\t" + lan6Documento.isConvertirAPdf());
                                log.info("Con Localizador:\t" + lan6Documento.isConLocalizador());
                                            log.info("Sincrono:\t\t" + lan6Documento.isSincrono());
                                            log.info("PDFA:\t\t\t" + lan6Documento.isPDFA());
                                log.info("Reducir QR:\t\t" + lan6Documento.isReducirQR());
                                log.info("Proc Platea:\t\t" + lan6Documento.getIdProcPlatea());
                                log.info("Id Archivo:\t\t" + lan6Documento.getIdProcArchivoDigital());
                                log.info("Tipo Firma:\t\t" + lan6Documento.getTipoFirma());

                                ArrayList<AdjuntoNotificacionVO> arrayDocumentos = new ArrayList<AdjuntoNotificacionVO>();
                                AdjuntoNotificacionVO adjuntoVO = new AdjuntoNotificacionVO();

                                            adjuntoVO.setCodigoMunicipio(notificacionFlexiaVO.getCodigoMunicipio());
                                            adjuntoVO.setCodigoNotificacion(notificacionFlexiaVO.getCodigoNotificacion());
                                            adjuntoVO.setNumeroExpediente(notificacionFlexiaVO.getNumExpediente());
                                            adjuntoVO.setOcurrenciaTramite(notificacionFlexiaVO.getOcurrenciaTramite());
                                            adjuntoVO.setCodigoTramite(notificacionFlexiaVO.getCodigoTramite());
                                adjuntoVO.setNombre(nombreFichero);
                                adjuntoVO.setContentType(mimeTypeContent);
                                adjuntoVO.setContenido(fichero);
                                            adjuntoVO.setEjercicio(notificacionFlexiaVO.getEjercicio());
                                            adjuntoVO.setCodigoProcedimiento(notificacionFlexiaVO.getCodigoProcedimiento());

                                arrayDocumentos.add(adjuntoVO);
                                //Una vez adjuntado debemos adjuntar el oid de dokusi para cuando se mande
                                log.info(" ===> Insertar adjunto externo");
                                            boolean grabado = rgiJobDAO.insertarAdjuntoExterno(adjuntoVO, expNotificarVO.getOidDocumentoNotificar(), conEnvio);

                                            // en las notificaciones/comunicaciones en RESOLUCION además de la resolucion hay que adjuntar el anexo IDDOCANEXO si existe
                                // tram 1   REI - DEU
                                // tram 3   REI
                                // tram 4   RKP - REC - DEU
                                // tram 6   RGI - REV - REN - PAC - PC
                                if ((codTramExterno.equals("1") && (procedimientoExpediente.equalsIgnoreCase("REI") || procedimientoExpediente.equalsIgnoreCase("DEU")))
                                        || (codTramExterno.equals("3") && (procedimientoExpediente.equalsIgnoreCase("REI")))
                                        || (codTramExterno.equals("4") && (procedimientoExpediente.equalsIgnoreCase("RKP") || procedimientoExpediente.equalsIgnoreCase("REC") || procedimientoExpediente.equalsIgnoreCase("DEU")))
                                        || (codTramExterno.equals("6") && (procedimientoExpediente.equalsIgnoreCase("RGI") || procedimientoExpediente.equalsIgnoreCase("REV") || procedimientoExpediente.equalsIgnoreCase("REN") || procedimientoExpediente.equalsIgnoreCase("PAC") || procedimientoExpediente.equalsIgnoreCase("PC")))) {
                                    log.info("Es una resolución de " + procedimientoExpediente);
                                    // comprobar si el expediente tiene anexo
                                                if (rgiJobDAO.existeAnexoResolucion(notificacionFlexiaVO.getCodigoMunicipio(), notificacionFlexiaVO.getNumExpediente(), notificacionFlexiaVO.getCodigoTramite(), notificacionFlexiaVO.getOcurrenciaTramite(), conEnvio)) {
                                        log.info("==> Se va a recuperar el Anexo");
                                        // preparo el anexo
                                        AdjuntoNotificacionVO anexoVO = new AdjuntoNotificacionVO();
                                                    anexoVO.setCodigoMunicipio(notificacionFlexiaVO.getCodigoMunicipio());
                                        anexoVO.setCodigoProcedimiento(procedimientoExpediente);
                                                    anexoVO.setEjercicio(notificacionFlexiaVO.getEjercicio());
                                                    anexoVO.setNumeroExpediente(notificacionFlexiaVO.getNumExpediente());
                                                    anexoVO.setCodigoTramite(notificacionFlexiaVO.getCodigoTramite());
                                                    anexoVO.setOcurrenciaTramite(notificacionFlexiaVO.getOcurrenciaTramite());
                                                    anexoVO.setCodigoNotificacion(notificacionFlexiaVO.getCodigoNotificacion());
                                        // recoger el resto de datos del ANEXO en E_TFIT - melanbide_dokusi_reldoc_doccst
                                                    anexoVO = rgiJobDAO.recuperaAnexoResolucion(anexoVO, conEnvio);
                                        // como este objeto no tiene propiedad para el OID lo devuelvo en observacionesRechazo
                                        if (!anexoVO.getObservacionesRechazo().isEmpty()) {
                                            oidAnexo = anexoVO.getObservacionesRechazo();
                                            // hay que vaciar este campo 
                                            anexoVO.setObservacionesRechazo("");
                                            log.info("OID del Anexo " + anexoVO.getNombre() + " ==> " + oidAnexo);
                                            lan6Anexo = servicios.consultarDocumento(oidAnexo);

                                            // si no está firmado se manda a firmar
                                            if (lan6Anexo.getFirmas() != null) {
                                                log.info(" ===> Hay firmas");
                                                if (lan6Anexo.getFirmas().isEmpty()) {
                                            log.info(" ===> Firmar documento");
                                                    lan6Anexo.setConvertirAPdf(true);
                                                    lan6Anexo = servicios.firmarDocumento(lan6Anexo);
                                                }
                                            } else {
                                                log.info(" ===> NO Hay firmas");
                                                log.info(" ===> Firmar documento");
                                                lan6Anexo.setConvertirAPdf(true);
                                            lan6Anexo = servicios.firmarDocumento(lan6Anexo);
                                            }
                                            if (lan6Anexo.getFirmas() != null) {
                                                log.info(" ===> AHora Hay firmas");
                                            }

                                                        log.info("Id documento:\t" + lan6Anexo.getIdDocumento());
                                                        log.info("Id expediente:\t" + lan6Anexo.getIdExpediente());
                                            log.info("Titulo:\t\t\t" + lan6Anexo.getTitulo());
                                            log.info("Nombre:\t\t\t" + lan6Anexo.getNombre());
                                            log.info("Formato:\t\t\t" + lan6Anexo.getFormat());
                                            log.info("Tipo documental:\t" + lan6Anexo.getTipoDocumental());
                                                        log.info("Serie Documental:" + lan6Anexo.getSerieDocumental());
                                            log.info("Asunto Documental: " + lan6Anexo.getAsuntoDocumental());
                                            log.info("Naturaleza:\t\t" + lan6Anexo.getNaturaleza());
                                            log.info("Origen:\t\t\t" + lan6Anexo.getOrigen());
                                            log.info("Convertir a PDF:\t" + lan6Anexo.isConvertirAPdf());
                                            log.info("Con Localizador:\t" + lan6Anexo.isConLocalizador());
                                                        log.info("Sincrono:\t\t" + lan6Anexo.isSincrono());
                                                        log.info("PDFA:\t\t\t" + lan6Anexo.isPDFA());
                                            log.info("Reducir QR:\t\t" + lan6Anexo.isReducirQR());
                                            log.info("Proc Platea:\t\t" + lan6Anexo.getIdProcPlatea());
                                            log.info("Id Archivo:\t\t" + lan6Anexo.getIdProcArchivoDigital());
                                            log.info("Tipo Firma:\t\t" + lan6Anexo.getTipoFirma());
                                            arrayDocumentos.add(anexoVO);
                                            //Una vez adjuntado debemos adjuntar el oid de dokusi para cuando se mande
                                            log.info(" ===> Insertar Anexo ");
                                                        grabado = rgiJobDAO.insertarAdjuntoExterno(anexoVO, oidAnexo, conEnvio);
                                        } else {
                                            log.error("No se han recuperado los datos del Anexo a la resolución. No se envía este documento.");
                                        }

                                    } else {
                                        log.info("No hay documento Anexo en la resolución   <====");
                                    }
                                }
                                            notificacionFlexiaVO.setAdjuntosExternos(arrayDocumentos);

                                //Si no hay interesados se sigue hacia delante igualmente
                                            log.debug("INTERESADOS: " + notificacionFlexiaVO.getAutorizados().size());
                                            log.debug("DOCUMENTOS: " + notificacionFlexiaVO.getAdjuntosExternos().size());

                                if (grabado) {
                                        //Procedemos a enviar la notificiacion
                                                log.info(" ===> Intentamos enviar: " + notificacionFlexiaVO.getNumExpediente());
                                        // Se Prepara la Notificacion
                                                notificacionPLATEA = rgiJobDAO.recogeNotificacionImvRgiPreparada(notificacionFlexiaVO.getCodigoNotificacion(), expedienteMiCarpeta, esActivo, conEnvio);

                                                if (notificacionPLATEA != null) {
                                            Lan6TramiteNotificacion lan6TramiteNotificacion = new Lan6TramiteNotificacion();
                                            log.info("Se crea el objeto Lan6TramiteNotificacion a enviar a Platea");
                                            // Rellenamos datos Notificacion
                                                    lan6TramiteNotificacion.setNumExpediente(expedienteMiCarpeta);
                                                    lan6TramiteNotificacion.setEjercicioExpediente(expedienteMiCarpeta.substring(0, 4));
                                                    lan6TramiteNotificacion.setTextoNotificacion(notificacionPLATEA.getTextoNotificacion());

                                                    if (esHijo) {
                                                        // añado el numero del expediente a la descrpcion del tramite
                                                        expNotificarVO.setDesTramite(expNotificarVO.getDesTramite() + " - " + expNotificarVO.getNumExpediente());
                                                        expNotificarVO.setDesTramite_eu(expNotificarVO.getDesTramite_eu() + " - " + expNotificarVO.getNumExpediente());
                                                    }
                                                    // Antes de recoger los interesados del expediente validamos los REPRESENTANTES LEGALES y actualizamos la tabla de AUTORIZADO_NOTIFICACION si es necesario, con los representantes validos
                                                log.info(" ===> Añadimos autorizados");
                                                    MeLanbide43BDUtil meLanbide43BDUtil = new MeLanbide43BDUtil();
                                                    AdaptadorSQLBD adapt = meLanbide43BDUtil.getAdaptSQLBD(String.valueOf(codOrg));

                                                try {
                                                        log.debug("Vamos a validar el representante legal .... contra REA");
                                                    int idioma = 1;
                                                    int codOrganizacionInt = (codOrg != null && !codOrg.isEmpty() ? Integer.parseInt(codOrg) : 0);
                                                        //     Map<String, String> respuestaValidacionRdR = (Map<String, String>) method.invoke(me43Class, codOrganizacionInt, lan6TramiteNotificacion.getNumExpediente(), idioma, idNotificacion);
                                                        Map<String, String> respuestaValidacionREA = RGI_IMV_JobUtils.getInstance().validarREA(codOrganizacionInt, expedienteMiCarpeta, esActivo, idioma, idNotificacion, procedimientoPlatea, adapt);
                                                        if (respuestaValidacionREA != null) {
                                                            String codRespuestaValREA = respuestaValidacionREA.get("codigo");
                                                            String mensajeRespuestaValREA = respuestaValidacionREA.get("descripcion");
                                                            log.info("Hemos recibido respuesta : " + codRespuestaValREA + " / " + mensajeRespuestaValREA);
                                                        // Actualizamos datos Envio Notificacion, desde la validacion se actualizan.
                                                            String emails = rgiJobDAO.getDatosNotificacionElectronica(expedienteMiCarpeta, esActivo, "AVISOEMAIL", conEnvio);
                                                            String smss = rgiJobDAO.getDatosNotificacionElectronica(expedienteMiCarpeta, esActivo, "AVISOSMS", conEnvio);
                                                            String emailsTitular = rgiJobDAO.getDatosNotificacionElectronica(expedienteMiCarpeta, esActivo, "AVISOEMAILTIT", conEnvio);
                                                            String smssTitular = rgiJobDAO.getDatosNotificacionElectronica(expedienteMiCarpeta, esActivo, "AVISOSMSTIT", conEnvio);
                                                            notificacionPLATEA.setEmails(emails);
                                                            notificacionPLATEA.setSms(smss);
                                                            notificacionPLATEA.setEmailsTitular(emailsTitular);
                                                            notificacionPLATEA.setSmsTitular(smssTitular);
                                                            log.info("Hemos Actualizado los datos de Aviso en el Objeto de Notificacion antes del envio: " + "" + emails + "-*-" + smss + "" + emailsTitular + "-*-" + smssTitular);
                                                    }
                                                } catch (Exception e) {
                                                        log.error("Error al validar el rol de representante legal del expediente contra REA " + e.getMessage(), e);
                                                }
                                                //estableciendo autorizados
                                                log.info("estableciendo autorizados ");

                                                    arrayAutorizadoNotif = new ArrayList<AutorizadoNotificacionVO>();
                                                    arrayAutorizadoNotif = rgiJobDAO.getInteresadosExpedientePluginNotificacion(expedienteMiCarpeta,esActivo, notificacionFlexiaVO.getCodigoNotificacion(), conEnvio);
                                                    notificacionPLATEA.setAutorizados(arrayAutorizadoNotif);

                                                    notificacionPLATEA.setAdjuntosExternos(arrayDocumentos);
                                                    notificacionPLATEA.setCodigoTipoNotificacion(tipoNotificacionFlexia);
                                                    notificacionPLATEA.setCodDepartamento(codOrg);

                                                    // Version 2.1.15 ==> Una notificación a varios interesados cambia el sentido de este for: Se enviaban n Notificaciones a n Interesados
                                                // Recorremos y preparamos: Autorizado(Marca telemática) ==> Demas receptores: Siempre Titular/Representante.
                                                // Si el autorizado es el titular Enviamos al Representante si lo hay, o Viceversa. Si hay solo uno ese se usa como autorizado
                                                // Desde el core solo se permite una marca por ello se modifica la query para leer los dos roles mas los de la marca
                                                AutorizadoNotificacionVO autorizado = new AutorizadoNotificacionVO();
                                                List<Lan6Interesado> autorizados = new ArrayList<Lan6Interesado>();
                                                    for (int i = 0; i < notificacionPLATEA.getAutorizados().size(); i++) {
                                                        AutorizadoNotificacionVO autorizado1 = notificacionPLATEA.getAutorizados().get(i);
                                                    log.info("comprueba tercero  seleccionado - " + autorizado1.getNif() + "/" + autorizado1.getCodigoTercero() + "/" + autorizado1.getNumeroVersionTercero());
                                                        if (notificacionPluginDAO.getTercero(notificacionPLATEA.getCodigoNotificacion(), autorizado1.getCodigoTercero(), conEnvio)) {
                                                        // Si ya ha cogido uno con marca, el otro lo enviamos como autorizado
                                                        log.info("Interesado con marca telematica");
                                                        if (!(autorizado != null && autorizado.getCodigoTercero() > 0)) {
                                                            log.info("Interesado Asignado como destinatario principal en la Notificacion");
                                                            autorizado = autorizado1;
                                                        } else {
                                                            log.info("Interesado con marca, pero ya hay un destinatario principal, lo agregaremos como Autorizado.");
                                                        }
                                                    } else {
                                                            // Como no tiene la marca y vamos a enviarle notificación, lo insertamos en la tabla de Autorizados
                                                            log.info("Interesado sin marca telematica. agregamos a la lista autorizados y registramos en tabla autorizados notificación");
                                                        try {
                                                            AutorizadoNotificacionManager autorizadoNotificacionManager = AutorizadoNotificacionManager.getInstance();
                                                                if (autorizadoNotificacionManager.insertarAutorizado(autorizado1, conEnvio)) {
                                                                    log.info("Dato Insertado en la tabla de autorizados para el envio de la notificación." + autorizado1.getNif() + "/" + autorizado1.getCodigoTercero() + "/" + autorizado1.getNumeroVersionTercero());
                                                            } else {
                                                                    log.error("Dato NO Insertado en la tabla de autorizados para el envio de la notificación." + autorizado1.getNif() + "/" + autorizado1.getCodigoTercero() + "/" + autorizado1.getNumeroVersionTercero());
                                                            }
                                                        } catch (Exception e) {
                                                                log.error("Error al actualizar la tabla AUTORIZADO_NOTIFICACION con un Autorizado al enviar la notificación " + notificacionFlexiaVO.getNumExpediente() + " - " + autorizado1.getNif() + " => " + e.getMessage(), e);
                                                        }
                                                    }
                                                    // Vamos aregando a la lista de otros receptores sino es el Autorizado principal
                                                    if (autorizado.getCodigoTercero() != autorizado1.getCodigoTercero()) {
                                                        Lan6Interesado lan6Interesado = new Lan6Interesado();
                                                        String tipoIdentificacion = "";
                                                        if ("1".equalsIgnoreCase(autorizado1.getTipoDocumento())) {
                                                                tipoIdentificacion = Lan6Constantes.TIPO_IDENT_NIF;
                                                        } else if ("2".equalsIgnoreCase(autorizado1.getTipoDocumento())) {
                                                                tipoIdentificacion = Lan6Constantes.TIPO_IDENT_PASAPORTE;
                                                        } else if ("3".equalsIgnoreCase(autorizado1.getTipoDocumento())) {
                                                                tipoIdentificacion = Lan6Constantes.TIPO_IDENT_NIE;
                                                        } else if ("4".equalsIgnoreCase(autorizado1.getTipoDocumento())) {
                                                                tipoIdentificacion = Lan6Constantes.TIPO_IDENT_CIF;
                                                        }
                                                        lan6Interesado.setTipoIdentificacion(tipoIdentificacion);
                                                        lan6Interesado.setNumIdentificacion(autorizado1.getNif());
                                                        lan6Interesado.setNombre((autorizado1.getNombre() != null && !autorizado1.getNombre().isEmpty() ? autorizado1.getNombre() : autorizado1.getNombreCompleto()));
                                                        lan6Interesado.setApellido1(autorizado1.getApellido1());
                                                        lan6Interesado.setApellido2(autorizado1.getApellido2());
                                                        autorizados.add(lan6Interesado);
                                                    }
                                                }

                                                Lan6NotificacionesServicios servicioNotificacion = new Lan6NotificacionesServicios(procedimientoPadre);// 2.2
                                                log.info("Se crea el servicio de envio de notificaciones con prodimiento " + procedimientoPadre);

                                                lan6TramiteNotificacion.setIdActoNotificado(idActoNotificadoLan6);
                                                    lan6TramiteNotificacion.setDescActoNotificado(notificacionFlexiaVO.getActoNotificado());
                                                lan6TramiteNotificacion.setDescActoNotificadoEu(actoNotificacion_eu);
                                                    lan6TramiteNotificacion.setDescTramiteNotificacion(expNotificarVO.getDesTramite()); // este es el titulo en Mi Carpeta
                                                    lan6TramiteNotificacion.setDescTramiteNotificacionEu(expNotificarVO.getDesTramite_eu()); // este es el titulo en Mi Carpeta EUSKERA a pelo

                                                    lan6TramiteNotificacion.setIdDestinatario(notificacionFlexiaVO.getAutorizados().get(0).getNif());
                                                    lan6TramiteNotificacion.setNombreDestinatario(notificacionFlexiaVO.getAutorizados().get(0).getNombre() + " " + notificacionFlexiaVO.getAutorizados().get(0).getApellido1() + " " + notificacionFlexiaVO.getAutorizados().get(0).getApellido2());
                                                    lan6TramiteNotificacion.setIdEmisor(notificacionPLATEA.getIdEmisor());
                                                    lan6TramiteNotificacion.setNombreEmisor(notificacionPLATEA.getNombreEmisor());
                                                lan6TramiteNotificacion.setAutorizados(autorizados);

                                                    log.info("El procedimiento Flexia es " + notificacionFlexiaVO.getCodigoProcedimiento());

                                                List<Lan6Documento> documentos = new ArrayList<Lan6Documento>();
                                                log.info(" ===> Añadir  lan6Documento en Lan6Documentos");
                                                documentos.add(lan6Documento);
                                                    if (notificacionFlexiaVO.getAdjuntosExternos().size() > 1) {
                                                        // en las resoluciones puede haber mas de un documento, hay que anadir el lan6Anexo
                                                    log.info(" ===> Añadir el Anexo en Lan6Documentos");
                                                    documentos.add(lan6Anexo);
                                                }
                                                lan6TramiteNotificacion.setDocumentos(documentos);

                                                    lan6TramiteNotificacion.setMailsAvisos(RGI_IMV_JobUtils.getInstance().formatearEmailsAvisoNotificacionDsdNotificacionVO(notificacionPLATEA, notificacionFlexiaVO.getAutorizados().get(0).getRol()));
                                                    lan6TramiteNotificacion.setTfnosAvisos(RGI_IMV_JobUtils.getInstance().formatearTlfnosAvisoNotificacionDsdNotificacionVO(notificacionPLATEA, notificacionFlexiaVO.getAutorizados().get(0).getRol()));
                                                String idioma = Lan6Constantes.IDIOMA_ES;
                                                    if (notificacionPLATEA.getIdioma() != null) {
                                                        if (notificacionPLATEA.getIdioma().equals("eu")) {
                                                        idioma = Lan6Constantes.IDIOMA_EU;
                                                    }
                                                }
                                                lan6TramiteNotificacion.setIdiomaNotificacion(idioma);

                                                // Llamada método
                                                String idNotificacionPublicada = "";

                                                    String informacionLan6TramiteNotificacion = RGI_IMV_JobUtils.getInstance().objetoLan6TramiteNotificacionTexto(lan6TramiteNotificacion);
                                                log.info(" ===> Enviamos el objeto:" + informacionLan6TramiteNotificacion);
                                                //Grabamos en la bbdd en el campo xml información la informacion que mandamos en el objeto lan6TramiteNotificacion

                                                    notificacionPluginDAO.updateNotificacionXML(notificacionPLATEA, informacionLan6TramiteNotificacion, adapt);
                                                log.info(" ===> Vamos a realizar la llamada a crearYPublicarNotificacion");
                                                idNotificacionPublicada = servicioNotificacion.crearYPublicarNotificacion(lan6TramiteNotificacion);
                                                log.info("El resultado de la llamada a Platea es " + idNotificacionPublicada);
                                                if (idNotificacionPublicada != null && !idNotificacionPublicada.isEmpty()) {
                                                    //   dev = true;
                                                    log.info(" ===> crearTramiteNotificacion ");
                                                    String creaTramite = servicioNotificacion.crearTramiteNotificacion(lan6TramiteNotificacion, idNotificacionPublicada);
                                                    log.info("Tramite creado en Mi Carpeta " + creaTramite);

                                                        boolean exito = notificacionPluginDAO.updateNotificacion(notificacionPLATEA, idNotificacionPublicada, adapt);
                                                    if (exito) {
                                                            log.info("Exito al actualizar el estado notificación con Id de Platea " + exito);
                                                            rgiJobDAO.insertarLineasLogNotificaciones(conEnvio, expNotificarVO.getNumExpediente(), "OK", "Notificacion ENVIADA");
                                                    } else {
                                                            String error = "Error al actualizar el estado de la notificación - " + idNotificacion;
                                                        log.error(error);
                                                        ErrorBean err = new ErrorBean();
                                                        err.setIdError("NOTIFICACIONES_010");
                                                        err.setMensajeError(error);
                                                            err.setSituacion("RGI_NotificarJob");
                                                            err.setIdClave("Notificación - " + idNotificacion);
                                                            err.setEvento("Envío automático de notificaciones RGI");

                                                            rgiJobDAO.marcarFallida(idNotificacion, conEnvio);
//                                                        rgiJobDAO.eliminarDatosNotificacion(id, con);
                                                            PluginNotificacionPlatea.grabarError(err, error, error, expNotificarVO.getNumExpediente());
                                                            rgiJobDAO.insertarLineasLogNotificaciones(conEnvio, expNotificarVO.getNumExpediente(), "ERROR", error);
                                                    }
                                                } else {
                                                    ErrorBean err = new ErrorBean();
                                                        String error = "Error al enviar la notificación - " + notificacionPLATEA.getCodigoNotificacion();
                                                    log.error(error);
                                                    err.setIdError("NOTIFICACIONES_002");
                                                    err.setMensajeError(error);
                                                        err.setSituacion("RGI_NotificarJob");
                                                        err.setIdClave("Notificación - " + idNotificacion);
                                                        err.setEvento("Envío automático de notificaciones RGI");

                                                        rgiJobDAO.marcarFallida(idNotificacion, conEnvio);
//                                                    rgiJobDAO.eliminarDatosNotificacion(id, con);
                                                        PluginNotificacionPlatea.grabarError(err, error, error, expNotificarVO.getNumExpediente());
                                                        rgiJobDAO.insertarLineasLogNotificaciones(conEnvio, expNotificarVO.getNumExpediente(), "ERROR", error);
                                                }

                                            } else {
                                                    String error = "Error al recuperar la notificación - " + idNotificacion;
                                                    log.error(error);
                                                    ErrorBean err = new ErrorBean();
                                                    err.setIdError("NOTIFICACIONES_005");
                                                    err.setMensajeError(error);
                                                    err.setSituacion("RGI_NotificarJob");
                                                    err.setIdClave("Notificación - " + idNotificacion);
                                                    err.setEvento("Envío automático de notificaciones RGI");

                                                    rgiJobDAO.marcarFallida(idNotificacion, conEnvio);
//                                                rgiJobDAO.eliminarDatosNotificacion(id, con);
                                                    PluginNotificacionPlatea.grabarError(err, error, error, expNotificarVO.getNumExpediente());
                                                    rgiJobDAO.insertarLineasLogNotificaciones(conEnvio, expNotificarVO.getNumExpediente(), "ERROR", error);
                                            }

                                        } else {
                                                String error = "Error al adjuntar un documento a la notificación - " + idNotificacion;
                                            log.error(error);
                                            ErrorBean err = new ErrorBean();
                                                err.setIdError("NOTIFICACIONES_010");
                                            err.setMensajeError(error);
                                            err.setSituacion("RGI_NotificarJob");
                                                err.setIdClave("Notificación - " + idNotificacion);
                                            err.setEvento("Envío automático de notificaciones RGI");

                                                rgiJobDAO.marcarFallida(idNotificacion, conEnvio);
//                                            rgiJobDAO.eliminarDatosNotificacion(id, con);
                                                PluginNotificacionPlatea.grabarError(err, error, error, expNotificarVO.getNumExpediente());
                                                rgiJobDAO.insertarLineasLogNotificaciones(conEnvio, expNotificarVO.getNumExpediente(), "ERROR", error);
                                        }

                                    } else {
                                            String error = "Error al actualizar la notificación - " + idNotificacion;
                                        log.error(error);
                                        ErrorBean err = new ErrorBean();
                                            err.setIdError("NOTIFICACIONES_010");
                                        err.setMensajeError(error);
                                        err.setSituacion("RGI_NotificarJob");
                                            err.setIdClave("Notificación - " + idNotificacion);
                                        err.setEvento("Envío automático de notificaciones RGI");

                                            rgiJobDAO.marcarFallida(idNotificacion, conEnvio);
//                                        rgiJobDAO.eliminarDatosNotificacion(id, con);
                                            PluginNotificacionPlatea.grabarError(err, error, error, expNotificarVO.getNumExpediente());
                                            rgiJobDAO.insertarLineasLogNotificaciones(conEnvio, expNotificarVO.getNumExpediente(), "ERROR", error);
                                    }
                                } else {
                                        // tercero no marcado
                                        String error = "Error al marcar el interesado para notificar - Notificación - " + idNotificacion;
                                    log.error(error);
                                    ErrorBean err = new ErrorBean();
                                    err.setIdError("NOTIFICACIONES_010");
                                    err.setMensajeError(error);
                                    err.setSituacion("RGI_NotificarJob");
                                        err.setIdClave("Notificación - " + idNotificacion);
                                    err.setEvento("Envío automático de notificaciones RGI");

                                        rgiJobDAO.marcarFallida(idNotificacion, conEnvio);
                                        rgiJobDAO.insertarLineasLogNotificaciones(conEnvio, expNotificarVO.getNumExpediente(), "ERROR", error);
                                        PluginNotificacionPlatea.grabarError(err, error, error, expNotificarVO.getNumExpediente());
                                }

                            } else {
                                    // hay más un tercero con rol 1 en el expediente RGI
                                    String error = "Hay más un tercero con rol 1 en el expediente " + expedienteMiCarpeta + " Padre del " + expNotificarVO.getNumExpediente() + " Notificación - " + idNotificacion;
                                log.error(error);
                                ErrorBean err = new ErrorBean();
                                err.setIdError("NOTIFICACIONES_010");
                                err.setMensajeError(error);
                                err.setSituacion("RGI_NotificarJob");
                                err.setEvento("Envío automático de notificaciones RGI");

                                    rgiJobDAO.insertarLineasLogNotificaciones(conEnvio, expNotificarVO.getNumExpediente(), "ERROR", error);
                                    PluginNotificacionPlatea.grabarError(err, error, error, expNotificarVO.getNumExpediente());
                                }

                            } else {
                                // sin marca PUBSEDE
                                log.info(mensajeError + " Notificación - " + idNotificacion);
                                rgiJobDAO.marcarFallida(idNotificacion, conEnvio);
                                rgiJobDAO.insertarLineasLogNotificaciones(conEnvio, expNotificarVO.getNumExpediente(), "NOPUB", mensajeError + " Notificación - " + idNotificacion);
                            }
                        } else {
                            // No se ha encontrado el Expediente PADRE
                            ErrorBean err = new ErrorBean();
                            err.setIdError("NOTIFICACIONES_010");
                            err.setMensajeError(mensajeError + " - " + idNotificacion);
                            err.setSituacion("RGI_NotificarJob");
                            err.setEvento("Envío automático de notificaciones RGI");

                            rgiJobDAO.marcarFallida(idNotificacion, conEnvio);
                            rgiJobDAO.insertarLineasLogNotificaciones(conEnvio, expNotificarVO.getNumExpediente(), "ERROR", mensajeError + " Notificación - " + idNotificacion);
                            PluginNotificacionPlatea.grabarError(err, mensajeError, mensajeError, expNotificarVO.getNumExpediente());
                        }

                    } else {
                        String error = "Error al crear la notificación";
                        log.error(error);
                        ErrorBean err = new ErrorBean();
                        err.setIdError("NOTIFICACIONES_011");
                        err.setMensajeError(error);
                        err.setSituacion("RGI_NotificarJob");
                        err.setEvento("Envío automático de notificaciones RGI");

                        rgiJobDAO.insertarLineasLogNotificaciones(conEnvio, expNotificarVO.getNumExpediente(), "ERROR", error);
                        PluginNotificacionPlatea.grabarError(err, error, error, expNotificarVO.getNumExpediente());
                    }

                } catch (Lan6Excepcion ex) {
                    log.error("Error en ADAPTADORES al intentar enviar una notificación - " + expNotificarVO.getNumExpediente() + " Notificación - " + idNotificacion, ex);
                    ArrayList<String> codes = ex.getCodes();
                    ArrayList<String> messages = ex.getMessages();
                    String causa = ex.getCausaExcepcion();
                    log.error("causa error en el job de notificación Lan6Excepcion: " + causa);
                    String error = "";
                    for (int i = 0; i < codes.size(); i++) {
                        error += messages.get(i);
                    }
                    log.error(error);
                    ErrorBean err = new ErrorBean();
                    err.setIdError("NOTIFICACIONES_004");
                    err.setMensajeError("Error en el job de notificación Lan6Excepcion - Notificación " + idNotificacion);
                    err.setSituacion("RGI_NotificarJob");
                    err.setIdClave("Notificación - " + idNotificacion);
                    err.setEvento("Envío automático de notificaciones RGI");

                    PluginNotificacionPlatea.grabarError(err, error, causa, expNotificarVO.getNumExpediente());
                    rgiJobDAO.marcarFallida(idNotificacion, conEnvio);
//                    rgiJobDAO.eliminarDatosNotificacion(id, con);
                    rgiJobDAO.insertarLineasLogNotificaciones(conEnvio, expNotificarVO.getNumExpediente(), "ERROR", "Error en ADAPTADORES al intentar enviar una notificación - " + idNotificacion);
                } catch (Exception ex) {
                    String error = "Error genérico al intentar tramitar el expediente - " + expNotificarVO.getNumExpediente();
                    log.error(error, ex);
                    ErrorBean err = new ErrorBean();
                    err.setIdError("NOTIFICACIONES_001");
                    err.setMensajeError(error);
                    err.setSituacion("RGI_NotificarJob");
                    err.setEvento("Envío automático de notificaciones RGI");

                    PluginNotificacionPlatea.grabarError(err, error, ex.getMessage(), expNotificarVO.getNumExpediente());
                    rgiJobDAO.marcarFallida(idNotificacion, conEnvio);
//                    rgiJobDAO.eliminarDatosNotificacion(id, con);
                    rgiJobDAO.insertarLineasLogNotificaciones(conEnvio, expNotificarVO.getNumExpediente(), "ERROR", "Error al intentar tramitar el expediente: " + ex.getMessage());
                }

            } //Cierre for

        } else {
            log.info("No existen expedientes a notificar.");
        }
        log.info("envio notificaciones END " + procedimientoPlatea + " - " + codTramExterno);
    }

    /**
     *
     * @param oidDocumentoNotificar
     * @param idProcedimiento
     * @throws Lan6Excepcion
     */
    private void firmarLocalizarDocumentoOid(String oidDocumentoNotificar, String idProcedimiento) throws Lan6Excepcion {
        log.info("firmarLocalizarDocumentoOid START " + oidDocumentoNotificar);
        Lan6DokusiServicios servicios = new Lan6DokusiServicios(idProcedimiento);
        Lan6Documento lan6Documento = new Lan6Documento();
        lan6Documento.setIdDocumento(oidDocumentoNotificar);// pdf
        // sincrono y superLocalizador
        /*
        lan6Documento.setSincrono(true);
        servicios.crearLocalizadorDocumento(lan6Documento, false);
         */
        servicios.firmarDocumento(lan6Documento);
        log.info("firmarLocalizarDocumentoOid END ");
    }

}
