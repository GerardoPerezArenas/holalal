/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.pluginnotificacionplatea;

import com.lanbide.lan6.errores.bean.ErrorBean;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.flexia.integracion.moduloexterno.pluginnotificacionplatea.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.pluginnotificacionplatea.util.ConstantesPluginNotificacionPlatea;
import es.altia.flexia.notificacion.persistence.AdjuntoNotificacionDAO;
import es.altia.flexia.notificacion.persistence.AutorizadoNotificacionManager;
import es.altia.flexia.notificacion.vo.AdjuntoNotificacionVO;
import es.altia.flexia.notificacion.vo.AutorizadoNotificacionVO;
import es.altia.flexia.registro.digitalizacion.lanbide.util.GestionAdaptadoresLan6Config;
import es.altia.technical.PortableContext;
import es.altia.util.commons.MimeTypes;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import es.altia.util.notificaciones.NotificacionesUtil;
import es.altia.util.notificaciones.TipoNotificacionValueObject;
import es.lanbide.lan6.adaptadoresPlatea.dokusi.beans.Lan6Documento;
import es.lanbide.lan6.adaptadoresPlatea.dokusi.servicios.Lan6DokusiServicios;
import es.lanbide.lan6.adaptadoresPlatea.excepciones.Lan6Excepcion;
import es.lanbide.lan6.adaptadoresPlatea.excepciones.Lan6NotificacionesExcepcion;
import es.lanbide.lan6.adaptadoresPlatea.informarConsulta.beans.Lan6Interesado;
import es.lanbide.lan6.adaptadoresPlatea.notificaciones.beans.Lan6TramiteNotificacion;
import es.lanbide.lan6.adaptadoresPlatea.notificaciones.servicios.Lan6NotificacionesServicios;
import es.lanbide.lan6.adaptadoresPlatea.utilidades.constantes.Lan6Constantes;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javax.sql.DataSource;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;

public class PluginNotificacionJob implements Job {

    private final Logger log = LogManager.getLogger(PluginNotificacionJob.class);
    private final GestionAdaptadoresLan6Config gestionAdaptadoresLan6Config = new GestionAdaptadoresLan6Config();


    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {

        Connection con = null;
        try {
            int pepe = jec.getRefireCount();
            Job trab = jec.getJobInstance();

            log.info("jec.getRefireCount(): " + pepe);
            String servidor = ConfigurationParameter.getParameter(ConstantesPluginNotificacionPlatea.CAMPO_SERVIDOR,
                    ConstantesPluginNotificacionPlatea.FICHERO_PROPIEDADES);
            //String servidor = "flexia1"; //DESA
            //String servidor = "paprergi1_flexia1"; //PRE
            //String servidor = "pargi1_flexia1"; //PRO

            if (servidor.equals(System.getProperty("weblogic.Name"))) {
                synchronized (jec) {
                    ArrayList<NotificacionBVO> notificaciones = null;
                    // LEIRE
                    boolean dev = false;
                    int numIntentos = 0;
                    String numExpediente = "";
                    int id = 0;
                    String[] params = new String[]{"ORACLE"};
                    try {
                        log.info("Execute lanzado " + System.getProperty("weblogic.Name"));
                        //int codOrg = 0;      //pruebas
                        //int codOrg = 1;      //real

                        int codOrg = Integer.parseInt(ConfigurationParameter.getParameter(ConstantesPluginNotificacionPlatea.COD_ORG,
                                ConstantesPluginNotificacionPlatea.FICHERO_PROPIEDADES));

                        boolean dosEntornos = false;//Boolean.getBoolean(ConfigurationParameter.getParameter(ConstantesPluginNotificacionPlatea.DOS_ENTORNOS, ConstantesPluginNotificacionPlatea.FICHERO_PROPIEDADES));
                        String entornos = ConfigurationParameter.getParameter(ConstantesPluginNotificacionPlatea.DOS_ENTORNOS, ConstantesPluginNotificacionPlatea.FICHERO_PROPIEDADES);
                        log.debug("entornos: " + entornos);
                        if (!entornos.toUpperCase().equals("FALSE")) {
                            dosEntornos = true;
                        }
                        while (codOrg < 2) {

                            con = this.getAdaptSQLBD(String.valueOf(codOrg)).getConnection();
                            log.debug("en el while de tokens codOrg: " + codOrg);
                            log.debug("dosEntornos: " + dosEntornos);

                            notificaciones = PluginNotificacionPlateaNotificacionDAO.getInstance().recogerNotificacionesPendientes(this.getAdaptSQLBD(String.valueOf(codOrg)));
                            log.info("notificaciones recogidas");
                            log.info("notificaciones: " + notificaciones.size());
                            for (NotificacionBVO notificacionVO : notificaciones) {
                                boolean docNotif = true;

                                if (notificacionVO.getNumIntentos() < 3) {
                                    try {   
                                        numIntentos = notificacionVO.getNumIntentos();
                                        id = notificacionVO.getCodigoNotificacion();
                                        log.info("codigo notificacion: " + id);
                                        log.info("expediente: " + notificacionVO.getNumExpediente());
                                        numExpediente = notificacionVO.getNumExpediente();
                                        //Codigo tipo notificación
                                        //String codigoTipoNotificacion=NotificacionManager.getInstance().getTipoNotificacion(notificacionVO, params);
                                        //NotificacionManager notificacionManager = NotificacionManager.getInstance();
                                        String codigoTipoNotificacion = PluginNotificacionPlateaNotificacionDAO.getInstance().getTipoNotificacion(notificacionVO, con);
                                        log.info("codigoTipoNotificacion: " + codigoTipoNotificacion);
                                        notificacionVO.setCodigoTipoNotificacion(codigoTipoNotificacion);

                                        // Antes de recoger los ineresados del expediente validamos los REPRESENTANTES LEGALES
                                        // y actualizamos la tabla de AUTORIZADO_NOTIFICACION si es necesario, con los representantes validos
                                        try {

                                            // Invocamos el Metodo del Modulo de inegracion M43 - Solo si el Autorizado es El representante. 
                                            log.debug("Vamos a validar el representante legal .... contra RdR");
                                            final Class cls = Class.forName("es.altia.flexia.integracion.moduloexterno.melanbide43.gestionrdr.MELANBIDE43_GestionRdR");
                                            log.debug("Despues de instanciar class for name");
                                            final Object me43Class = cls.newInstance();
                                            final Class[] types = {int.class, String.class, int.class, int.class};
                                            log.debug("Creada instancia y definidos types");
                                            final Method method = cls.getMethod("validarExistActualRolRepresContraRdR", types);
                                            log.debug("GetMethod... Ahora invocamos");
                                            int idioma = 1;
                                            //int codOrganizacionInt = (codOrganizacion != null && !codOrganizacion.isEmpty() ? Integer.parseInt(codOrganizacion) : 0);
                                            Map<String, String> respuestaValidacionRdR = (Map<String, String>) method.invoke(me43Class, codOrg, numExpediente, idioma, id);
                                            log.debug("GetMethod... Despues de invoke metodo");
                                            if (respuestaValidacionRdR != null) {
                                                String codRespuestaValRdR = respuestaValidacionRdR.get("codigo");
                                                String mensajeRespuestaValRdR = respuestaValidacionRdR.get("descripcion");
                                                log.info("Hemos recibido respuesta : " + codRespuestaValRdR + " / " + mensajeRespuestaValRdR);
                                                // Actualizamos datos Envio Notificacion, desde la validacion se actualizan.
//                                                if (codRespuestaValRdR != null && !"0".equalsIgnoreCase(codRespuestaValRdR)
//                                                        && !"1".equalsIgnoreCase(codRespuestaValRdR)
//                                                        && !"2".equalsIgnoreCase(codRespuestaValRdR)) {
                                                    String emails = PluginNotificacionPlateaNotificacionDAO.getInstance().getValorCampoSuplementarioExpteTipoTextoxCodNumExp(numExpediente, "AVISOEMAIL", con);
                                                    String smss = PluginNotificacionPlateaNotificacionDAO.getInstance().getValorCampoSuplementarioExpteTipoTextoxCodNumExp(numExpediente, "AVISOSMS", con);
                                                    String emailsTitular = PluginNotificacionPlateaNotificacionDAO.getInstance().getValorCampoSuplementarioExpteTipoTextoxCodNumExp(numExpediente, "AVISOEMAILTIT", con);
                                                    String smssTitular = PluginNotificacionPlateaNotificacionDAO.getInstance().getValorCampoSuplementarioExpteTipoTextoxCodNumExp(numExpediente, "AVISOSMSTIT", con);
                                                    notificacionVO.setEmails(emails);
                                                    notificacionVO.setSms(smss);
                                                    notificacionVO.setEmailsTitular(emailsTitular);
                                                    notificacionVO.setSmsTitular(smssTitular);
                                                    log.info("Hemos Actualizado los datos de Aviso en el Obejeto de Notificacion antes del envio: "
                                                            + "" + emails + "-*-"+smss
                                                            + "" + emailsTitular + "-*-"+smssTitular
                                                    );
//                                                }
                                            }
                                        } catch (Exception e) {
                                            log.error("Error al validar el rol de representante legal del expediente contra RdR " + e.getMessage(), e);
                                        }
                                        
                                        //estableciendo autorizados
                                        log.info("estableciendo autorizados ");
                                        ArrayList<AutorizadoNotificacionVO> arrayAutorizadoNotif = new ArrayList<AutorizadoNotificacionVO>();
                                        //AutorizadoNotificacionDAO autorizadoNotificacionDAO = AutorizadoNotificacionDAO.getInstance();
                                        arrayAutorizadoNotif = PluginNotificacionPlateaNotificacionDAO.getInteresadosExpedientePluginNotificacion((notificacionVO.getNumExpediente()), notificacionVO.getCodigoNotificacion(), con);
                                        notificacionVO.setAutorizados(arrayAutorizadoNotif);

                                        //Obtengo los documentos de la notificacion
                                        log.info("documentos de la notificacion ");
                                        AdjuntoNotificacionVO adjuntoNotificacionVO = new AdjuntoNotificacionVO();
                                        adjuntoNotificacionVO.setCodigoMunicipio(notificacionVO.getCodigoMunicipio());
                                        adjuntoNotificacionVO.setEjercicio(notificacionVO.getEjercicio());
                                        adjuntoNotificacionVO.setNumeroExpediente(notificacionVO.getNumExpediente());
                                        adjuntoNotificacionVO.setCodigoProcedimiento(notificacionVO.getCodigoProcedimiento());
                                        adjuntoNotificacionVO.setCodigoTramite(notificacionVO.getCodigoTramite());
                                        adjuntoNotificacionVO.setOcurrenciaTramite(notificacionVO.getOcurrenciaTramite());
                                        adjuntoNotificacionVO.setCodigoNotificacion(notificacionVO.getCodigoNotificacion());

                                        //estableciendo adjuntos                               
                                        log.info("estableciendo adjuntos");
                                        AdjuntoNotificacionDAO adjuntoNotificacionDAO = AdjuntoNotificacionDAO.getInstance();
                                        ArrayList<AdjuntoNotificacionVO> arrayDocumentos = new ArrayList<AdjuntoNotificacionVO>();
                                        arrayDocumentos = adjuntoNotificacionDAO.getDocumentosTramitacion(adjuntoNotificacionVO, con);
                                        notificacionVO.setAdjuntos(arrayDocumentos);

                                        // DOCUMENTOS EXTERNOS ANEXOS A LA NOTIFICACIÓN 
                                        log.info("documentos externos ");
                                        ArrayList<AdjuntoNotificacionVO> adjuntosExternos = adjuntoNotificacionDAO.getListaAdjuntosExterno(notificacionVO.getCodigoNotificacion(), con);
                                        notificacionVO.setAdjuntosExternos(adjuntosExternos);

                                        // Version 2.1.15 ==> Una notificacion a varios interesados cambia el sentido de este for: Se enviaban n Notificaciones a n Interesados
                                        // Recorremos y preparamos: Autorizado(Marca telemática) ==> Demas receptores: Siempre Titular/Representante.
                                        // Si el autorizado es el titular Enviamos al Representante si lo hay, o Viceversa. Si hay solo uno ese se usa como autorizado
                                        // Desde el core solo se permite una marca por ello se modifica la query para leer los dos roles mas los de la marca
                                        AutorizadoNotificacionVO autorizado = new AutorizadoNotificacionVO();
                                        List<Lan6Interesado> autorizados = new ArrayList<Lan6Interesado>();
                                        for (int i = 0; i < notificacionVO.getAutorizados().size(); i++) {
                                            AutorizadoNotificacionVO autorizado1 = notificacionVO.getAutorizados().get(i);
                                            log.info("comprueba tercero  seleccionado - " + autorizado1.getNif() + "/" + autorizado1.getCodigoTercero() + "/" + autorizado1.getNumeroVersionTercero() );
                                            if (PluginNotificacionPlateaNotificacionDAO.getInstance().getTercero(notificacionVO.getCodigoNotificacion(), autorizado1.getCodigoTercero(), con)) {
                                                // Si ya ha cogido uno con marca, el otro lo enviamos como autorizado
                                                log.info("Interesado con marca telematica");
                                                if(!(autorizado!=null && autorizado.getCodigoTercero()>0)) {
                                                    log.info("Interesado Asignado como destinatario principal en la Notificacion");
                                                    autorizado = autorizado1;
                                                }else{
                                                    log.info("Interesado con marca, pero ya hay un destinatario principal, lo agregaremos como Autorizado.");
                                                }
                                            }else{
                                                // Como no tiene la marca y vamos a enviarle notificacion, lo insertamos en la tabla de Autorizados
                                                log.info("Interesado sin marca telematica. agregamos a la lista autorizados y registramos en tabla autorizados notificacion");
                                                try {
                                                    AutorizadoNotificacionManager autorizadoNotificaiconManager = AutorizadoNotificacionManager.getInstance();
                                                    if (autorizadoNotificaiconManager.insertarAutorizado(autorizado1, con)) {
                                                        log.info("Dato Insertado en la tabla de autorizados para el envio de la notificaicon." + autorizado1.getNif() + "/" + autorizado1.getCodigoTercero() + "/" + autorizado1.getNumeroVersionTercero());
                                                    } else {
                                                        log.error("Dato NO Insertado en la tabla de autorizados para el envio de la notificaicon." + autorizado1.getNif() + "/" + autorizado1.getCodigoTercero() + "/" + autorizado1.getNumeroVersionTercero());
                                                    }
                                                } catch (Exception e) {
                                                    log.error("Error al actualizar la tabla AUTORIZADO_NOTIFICACION con un Autorizado al enviar la notificacion " + numExpediente + " - " + autorizado1.getNif() + " => " + e.getMessage(), e);
                                                }
                                            }
                                            // Vamos aregando a la lista de otros receptores sino es el Autorizado principal
                                            if(autorizado.getCodigoTercero()!=autorizado1.getCodigoTercero()){
                                                Lan6Interesado lan6Interesado = new Lan6Interesado();
                                                String tipoIdentificacion = "";
                                                if("1".equalsIgnoreCase(autorizado1.getTipoDocumento())){
                                                    tipoIdentificacion=Lan6Constantes.TIPO_IDENT_NIF;
                                                } else if ("2".equalsIgnoreCase(autorizado1.getTipoDocumento())){
                                                    tipoIdentificacion=Lan6Constantes.TIPO_IDENT_PASAPORTE;
                                                }  else if ("3".equalsIgnoreCase(autorizado1.getTipoDocumento())){
                                                    tipoIdentificacion=Lan6Constantes.TIPO_IDENT_NIE;
                                                } else if ("4".equalsIgnoreCase(autorizado1.getTipoDocumento())){
                                                    tipoIdentificacion=Lan6Constantes.TIPO_IDENT_CIF;
                                                }
                                                lan6Interesado.setTipoIdentificacion(tipoIdentificacion);
                                                lan6Interesado.setNumIdentificacion(autorizado1.getNif());
                                                lan6Interesado.setNombre((autorizado1.getNombre()!=null && !autorizado1.getNombre().isEmpty()?autorizado1.getNombre():autorizado1.getNombreCompleto()));
                                                lan6Interesado.setApellido1(autorizado1.getApellido1());
                                                lan6Interesado.setApellido2(autorizado1.getApellido2());
                                                autorizados.add(lan6Interesado);
                                            }

                                        }

                                        // Se Prepara la Notificacion
                                        log.info("El procedimiento Flexia es " + notificacionVO.getCodigoProcedimiento());
                                        //DESARROLLO 0_______________________________________________________________________________________________________________________DESARROLLO 0
                                        /*    Integer esDesarrolloCero = PluginNotificacionPlateaNotificacionDAO.getInstance().esDesarrolloCero(numExpediente, con);
                                        String procedimientoPlatea = "";
                                        if (esDesarrolloCero > 0){
                                            procedimientoPlatea = ConfigurationParameter.getParameter(ConstantesPluginNotificacionPlatea.ID_PROC_DESARROLLO_CERO, ConstantesPluginNotificacionPlatea.FICHERO_PROPIEDADES);
                                        } else {
                                            procedimientoPlatea = gestionAdaptadoresLan6Config.getCodProcedimientoPlatea(notificacionVO.getCodigoProcedimiento()); //convierteProcedimiento(codProcedimiento);
                                        }
                                        //DESARROLLO 0_______________________________________________________________________________________________________________________DESARROLLO 0
                                        log.info ("esDesarrolloCero: " + esDesarrolloCero);
                                         */
                                        // Instanciamos variables genericas
                                        log.info("notificacion.codProc: " + notificacionVO.getCodigoProcedimiento());
//                                        log.info("Procedimiento Platea: " + procedimientoPlatea);
                                        Lan6NotificacionesServicios servicioNotificacion = new Lan6NotificacionesServicios(notificacionVO.getCodigoProcedimiento()); // 2.2
                                        log.info("Se crea el servicio de envio de notificaciones con procedimiento " + notificacionVO.getCodigoProcedimiento());
//                                        Lan6DokusiServicios servicioDokusi = new Lan6DokusiServicios(notificacionVO.getCodigoProcedimiento()); // 2.2
//                                        log.info("Se crea el servicio de Dokusi con prodimiento " + notificacionVO.getCodigoProcedimiento());
                                        // Expediente
                                        Lan6TramiteNotificacion lan6TramiteNotificacion = new Lan6TramiteNotificacion();
                                        log.info("Se crea el objeto Lan6TramiteNotificacion a enviar a Platea");

                                        // Rellenamos datos Notificacion
                                        lan6TramiteNotificacion.setNumExpediente(notificacionVO.getNumExpediente());
                                        log.info("Num expediente: " + lan6TramiteNotificacion.getNumExpediente());
                                        lan6TramiteNotificacion.setEjercicioExpediente(Integer.toString(notificacionVO.getEjercicio()));
                                        log.info("Ejercicio expediente: " + lan6TramiteNotificacion.getEjercicioExpediente());

                                        // Datos de tramite Notificado
                                        Long codTram = PluginNotificacionPlateaNotificacionDAO.getInstance().getCodigoVisibleTramite(codOrg, notificacionVO.getCodigoProcedimiento(), notificacionVO.getCodigoTramite() + "", con);
                                        log.debug("PROC: " + notificacionVO.getCodigoProcedimiento() + " TRAMITE: " + codTram);
                                        if (notificacionVO.getCodigoProcedimiento().equals("REPLE")
                                                && (codTram.equals(new Long("301")) || codTram.equals(new Long("304")) || codTram.equals(new Long("401")) || codTram.equals(new Long("404"))
                                                || codTram.equals(new Long("412")))) {
                                            log.debug("REPLE - TRAMITE: " + codTram);
                                            //tramite 301 -Requerimiento documentación 2º pago
                                            if (codTram.equals(new Long("301"))) {
                                                log.debug("-codTram es 301- ");
                                                lan6TramiteNotificacion.setIdActoNotificado(Lan6Constantes.Notificaciones.ACTO_NOTIFICADO_REQUERIMIENTO);
                                                lan6TramiteNotificacion.setDescActoNotificado(Lan6Constantes.Notificaciones.ACTO_NOTIFICADO_REQUERIMIENTO_DESC);
                                                //String desTram=ConfigurationParameter.getParameter("DES_TRAM_REQ_PAGO2_LAN62_REPLE", Lan6Constantes.FICHERO_PROP_ADAPTADORES_PLATEA);
//                                                String desTram = ConfigurationParameter.getParameter("DES_TRAM_REQ_PAGO2_LAN62_REPLE", ConstantesPluginNotificacionPlatea.FICHERO_ADPATADORES);
                                                String desTram = gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("DES_TRAM_REQ_PAGO2_LAN62_REPLE"); // 2.2
                                                lan6TramiteNotificacion.setDescTramiteNotificacion(desTram);
                                            } //tramite 304 - Requerimiento subsanación de documentación 2º pago
                                            else if (codTram.equals(new Long("304"))) {
                                                lan6TramiteNotificacion.setIdActoNotificado(Lan6Constantes.Notificaciones.ACTO_NOTIFICADO_REQ_SUB);
                                                lan6TramiteNotificacion.setDescActoNotificado(Lan6Constantes.Notificaciones.ACTO_NOTIFICADO_RESOL_DESIS_DESC);
                                                //String desTram=ConfigurationParameter.getParameter("DES_TRAM_REQ_SUB_PAGO2_LAN62_REPLE", Lan6Constantes.FICHERO_PROP_ADAPTADORES_PLATEA);
//                                                String desTram = ConfigurationParameter.getParameter("DES_TRAM_REQ_SUB_PAGO2_LAN62_REPLE", ConstantesPluginNotificacionPlatea.FICHERO_ADPATADORES);
                                                String desTram = gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("DES_TRAM_REQ_SUB_PAGO2_LAN62_REPLE"); // 2.2
                                                lan6TramiteNotificacion.setDescTramiteNotificacion(desTram);
                                            } //Trámite 401 ? Comunicación petición documentación plantilla:
                                            else if (codTram.equals(new Long("401"))) {
                                                lan6TramiteNotificacion.setIdActoNotificado(Lan6Constantes.Notificaciones.ACTO_NOTIFICADO_REQUERIMIENTO);
                                                lan6TramiteNotificacion.setDescActoNotificado(Lan6Constantes.Notificaciones.ACTO_NOTIFICADO_REQUERIMIENTO_DESC);
                                                //String desTram=ConfigurationParameter.getParameter("DES_TRAM_REQ_PLANTILLA_LAN62_REPLE", Lan6Constantes.FICHERO_PROP_ADAPTADORES_PLATEA);
//                                                String desTram = ConfigurationParameter.getParameter("DES_TRAM_REQ_PLANTILLA_LAN62_REPLE", ConstantesPluginNotificacionPlatea.FICHERO_ADPATADORES);
                                                String desTram = gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("DES_TRAM_REQ_PLANTILLA_LAN62_REPLE"); // 2.2
                                                lan6TramiteNotificacion.setDescTramiteNotificacion(desTram);
                                            } //Trámite 404 ? Requerimiento subsanación de documentación plantilla:
                                            else if (codTram.equals(new Long("404"))) {
                                                lan6TramiteNotificacion.setIdActoNotificado(Lan6Constantes.Notificaciones.ACTO_NOTIFICADO_REQ_SUB);
                                                lan6TramiteNotificacion.setDescActoNotificado(Lan6Constantes.Notificaciones.ACTO_NOTIFICADO_REQ_SUB_DESC);
                                                //String desTram=ConfigurationParameter.getParameter("DES_TRAM_REQ_SUB_PLANTILLA_LAN62_REPLE", Lan6Constantes.FICHERO_PROP_ADAPTADORES_PLATEA);
//                                                String desTram = ConfigurationParameter.getParameter("DES_TRAM_REQ_SUB_PLANTILLA_LAN62_REPLE", ConstantesPluginNotificacionPlatea.FICHERO_ADPATADORES);
                                                String desTram = gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("DES_TRAM_REQ_SUB_PLANTILLA_LAN62_REPLE"); // 2.2
                                                lan6TramiteNotificacion.setDescTramiteNotificacion(desTram);
                                            } //Trámite 412 ? Requerimiento conversión indefinido:
                                            else if (codTram.equals(Long.valueOf("412"))) {
                                                lan6TramiteNotificacion.setIdActoNotificado(Lan6Constantes.Notificaciones.ACTO_NOTIFICADO_REQUERIMIENTO);
                                                lan6TramiteNotificacion.setDescActoNotificado(Lan6Constantes.Notificaciones.ACTO_NOTIFICADO_REQUERIMIENTO_DESC);
                                                //String desTram=ConfigurationParameter.getParameter("DES_TRAM_REQ_CONVER_INDEF_LAN62_REPLE", Lan6Constantes.FICHERO_PROP_ADAPTADORES_PLATEA);
//                                                String desTram = ConfigurationParameter.getParameter("DES_TRAM_REQ_CONVER_INDEF_LAN62_REPLE", ConstantesPluginNotificacionPlatea.FICHERO_ADPATADORES);
                                                String desTram = gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("DES_TRAM_REQ_CONVER_INDEF_LAN62_REPLE"); // 2.2
                                                lan6TramiteNotificacion.setDescTramiteNotificacion(desTram);
                                            }
                                            log.info("Id acto notificado REPLE: " + lan6TramiteNotificacion.getIdActoNotificado());
                                            log.info("Descripcion acto notificado REPLE: " + lan6TramiteNotificacion.getDescActoNotificado().toUpperCase());
                                            log.info("Descripcion TRÁMITE notificado REPLE: " + lan6TramiteNotificacion.getDescTramiteNotificacion().toUpperCase());
                                        } else if (notificacionVO.getCodigoProcedimiento().equals("ECA") && codTram.equals(new Long("510"))){
                                            log.info("ECA - 510");
                                            lan6TramiteNotificacion.setIdActoNotificado(Lan6Constantes.Notificaciones.ACTO_NOTIFICADO_OTROS_MOD_IMPORTE);
                                            lan6TramiteNotificacion.setDescActoNotificado(Lan6Constantes.Notificaciones.ACTO_NOTIFICADO_OTROS_MOD_IMPORTE_DESC);
                                        } else if (notificacionVO.getCodigoProcedimiento().equals("ECA") && codTram.equals(new Long("540"))){
                                            log.info("ECA - 540");
                                            lan6TramiteNotificacion.setIdActoNotificado(Lan6Constantes.Notificaciones.ACTO_NOTIFICADO_RESOL_MOD_IMPORTE);
                                            lan6TramiteNotificacion.setDescActoNotificado(Lan6Constantes.Notificaciones.ACTO_NOTIFICADO_RESOL_MOD_IMPORTE_DESC);
                                        }else {
                                            log.info("NO ES REPLE ESPECIAL ");
                                            //ESte valor tiene eque estar en el catálogo de procedimientos!!!!!
                                            log.info("tipo notificacion: " + notificacionVO.getCodigoTipoNotificacion());
                                            String tipoNotificacion = getEquivalenciaTipoNotificacion(notificacionVO.getCodigoTipoNotificacion());
                                            lan6TramiteNotificacion.setIdActoNotificado(tipoNotificacion);
                                            log.info("Id acto notificado: " + lan6TramiteNotificacion.getIdActoNotificado());

                                            List<TipoNotificacionValueObject> listaTiposNotificacion = NotificacionesUtil.getTiposNotificacion();
                                            String descripcionTipoNotificacion = "";
                                            for (TipoNotificacionValueObject tipo : listaTiposNotificacion) {
                                                log.info("listaTiposNotificacion: " + tipo.getCodigo());
                                                if (notificacionVO.getCodigoTipoNotificacion() != null && notificacionVO.getCodigoTipoNotificacion().equals(tipo.getCodigo())) {
                                                    descripcionTipoNotificacion = tipo.getDescripcion();
                                                }
                                            }
                                            lan6TramiteNotificacion.setDescActoNotificado(descripcionTipoNotificacion);
                                            log.info("Descripcion acto notificado: " + lan6TramiteNotificacion.getDescActoNotificado().toUpperCase());
                                        }
                                        lan6TramiteNotificacion.setIdDestinatario(autorizado.getNif());
                                        log.info("Id destinatario: " + lan6TramiteNotificacion.getIdDestinatario());
                                        lan6TramiteNotificacion.setNombreDestinatario(autorizado.getNombre() + " " + autorizado.getApellido1() + " " + autorizado.getApellido2());
                                        log.info("Nombre destinatario: " + lan6TramiteNotificacion.getNombreDestinatario());



                                        lan6TramiteNotificacion.setTextoNotificacion(notificacionVO.getTextoNotificacion().toUpperCase());
                                        log.info("Texto: " + lan6TramiteNotificacion.getTextoNotificacion());
                                        //escribirFichero(notificacionVO.getTextoNotificacion());
                                        lan6TramiteNotificacion.setIdEmisor(notificacionVO.getIdEmisor());
                                        log.info("Id emisor: " + lan6TramiteNotificacion.getIdEmisor());
                                        lan6TramiteNotificacion.setNombreEmisor(notificacionVO.getNombreEmisor());
                                        log.info("Nombre emisor: " + lan6TramiteNotificacion.getNombreEmisor());
                                        if (!autorizados.isEmpty()) {
                                            lan6TramiteNotificacion.setAutorizados(autorizados);
                                        }

                                        // DOCUMENTOS
                                        List<Lan6Documento> documentos = new ArrayList<Lan6Documento>();
                                        log.info("Pasamos a los documentos");

                                        PluginNotificacionPlateaDocumentoDokusiDAO documentoDokusiDAO = PluginNotificacionPlateaDocumentoDokusiDAO.getInstance();
                                        String idDokusi = "";
                                        // TRATAMOS LOS DOCUMENTOS DEL TRAMITE
                                        for (AdjuntoNotificacionVO doc : notificacionVO.getAdjuntos()) {
                                            // LOS DOCUMENTOS DEL TRAMITE TIENEN QUE ESTAR SELECCIONADOS CON EL CHECK
                                            if (doc.getSeleccionado() != null && doc.getSeleccionado().equals("SI")) {
                                            log.info("Se va a crear un nuevo documento de TRÁMITE");

                                            log.info("Esta seleccionado: " + doc.getSeleccionado());
                                            Lan6Documento lan6Documento = new Lan6Documento();

                                            String[] tituloExtension = getTituloExtension(doc.getNombre(), doc.getContentType(), true);
                                            lan6Documento.setTitulo(tituloExtension[0]);
                                            log.info("Titulo: " + lan6Documento.getTitulo());
                                            lan6Documento.setNombre(tituloExtension[0]);
                                            log.info("Nombre: " + lan6Documento.getNombre());

                                            String formato = MimeTypes.guessExtensionFromMimeType(tituloExtension[1]);
                                            formato = formato.substring(1);
                                            lan6Documento.setFormat(formato);

                                            log.info("Formato: " + lan6Documento.getFormat());
                                            //lan6Documento.setContenido(doc.getContenido());
                                            //log.error("Contenido: " + (lan6Documento.getContenido() != null ? lan6Documento.getContenido().length : 0) + " bytes");
                                            lan6Documento.setTipoDocumental(Lan6Constantes.TIPO_DOCUMENTAL_ARCHIVO);
                                            log.info("Tipo documental: " + lan6Documento.getTipoDocumental());
                                            lan6Documento.setOrigen(Lan6Constantes.MD_ORIGEN_ADMINISTRACION);
                                            log.info("Origen: " + lan6Documento.getOrigen());

                                                // LOS DOCUMENTOS DEL TRAMITE YA ESTAN EN DOKUSI CUANDO SE DAN DE ALTA.
                                                // OBTENEMOS EL ID DE DOKUSI
                                                idDokusi = documentoDokusiDAO.getIdDokusiDocumentoTramite(doc.getCodigoMunicipio(),
                                                        doc.getEjercicio(),
                                                        doc.getCodigoProcedimiento(),
                                                        doc.getNumeroExpediente(),
                                                        doc.getCodigoTramite(),
                                                        doc.getOcurrenciaTramite(),
                                                        doc.getNumeroUnidad(),
                                                        this.getAdaptSQLBD(String.valueOf(codOrg)));
                                                log.info("Antes de comprobar el documento notificado");
                                                if (docNotif) {
                                                    docNotif = PluginNotificacionPlateaNotificacionDAO.getInstance().documentoNotificado(notificacionVO.getNumExpediente(), idDokusi, this.getAdaptSQLBD(String.valueOf(codOrg)));
                                                }

                                                if (idDokusi == null || idDokusi.equals("")) {
                                                    log.info("No se ha encontrado el documento que buscamos, por lo que no tenemos su Id en Dokusi");
                                                    dev = false;
                                                    //return dev;
                                                }
                                                //solo se firma si el documento ha sido transformado
                                                if (docNotif) {
                                                    Lan6DokusiServicios servicios = new Lan6DokusiServicios(notificacionVO.getCodigoProcedimiento()); // 2.2

                                                    lan6Documento.setIdDocumento(idDokusi);
                                                    log.info("Id documento: " + lan6Documento.getIdDocumento());

                                                    //  si no ha sido firmado en el portafirmas se envía a convertir a PDF
                                                    boolean firmado = doc.getFirmado().equals("SI");
                                                    if (!firmado) {
                                                        log.info("El documento con idDokusi '" + idDokusi + "' NO está firmado, pasa a convertir a PDF");
                                                        lan6Documento.setConvertirAPdf(true);
                                                    } else {
                                                        log.info("El documento con idDokusi '" + idDokusi + "' ya está firmado, NO se convierte a PDF");
                                                        lan6Documento.setConvertirAPdf(false);
//                                                                log.info("Establecemos la propiedad PDFA a true");
//                                                                lan6Documento.setPDFA(true);
                                                    }

                                                    //compruebo localizador
                                                    /* comento esto, no hay docs con localizador
                                                    boolean localizador = PluginNotificacionPlateaNotificacionDAO.getInstance().obtieneLocalizador(notificacionVO.getCodigoProcedimiento(), notificacionVO.getCodigoTramite(), numExpediente, con);
                                                    log.info("localizador: " + localizador);

                                                    if (localizador) {
                                                        lan6Documento.setSincrono(true);
                                                        String id2 = servicios.crearLocalizadorDocumento(lan6Documento);
                                                        log.info("id localizador: " + id2);
                                                    }
                                                    */
                                                    //lan6Documento.setDocNotificacion(true);
                                                    documentos.add(lan6Documento);
                                                    log.info("Se añade el nuevo documento");

                                                    lan6Documento.setFormat("pdf");

                                                    //  si no ha sido firmado en el portafirmas se envía a firmar
                                                    if (!firmado) {
                                                        log.info("El documento con idDokusi '" + idDokusi + "' NO está firmado, pasa a firmarse");
                                                        lan6Documento = servicios.firmarDocumento(lan6Documento);
                                                    } else {
                                                        log.info("El documento con idDokusi '" + idDokusi + "' ya está firmado");
                                                    }
                                                }
                                            } else {
                                                log.info("No hacemos nada con este documento porque no está seleccionado");
                                            }
                                        }

                                        int docsTramite = documentos.size();
                                        //  #489694 enviar documentos externos
                                        // docNotif = true;
                                        // TRATAMOS LOS DOCUMENTOS EXTERNOS
                                        for (AdjuntoNotificacionVO docExt : notificacionVO.getAdjuntosExternos()) {
                                            log.info("Se va a crear un nuevo documento EXTERNO");

                                            log.info("Esta seleccionado: " + docExt.getSeleccionado());
                                            Lan6Documento lan6Documento = new Lan6Documento();

                                            String[] tituloExtension = getTituloExtension(docExt.getNombre(), docExt.getContentType(), false);
                                            lan6Documento.setTitulo(tituloExtension[0]);
                                            log.info("Titulo: " + lan6Documento.getTitulo());
                                            lan6Documento.setNombre(tituloExtension[0]);
                                            log.info("Nombre: " + lan6Documento.getNombre());

                                            String formato = MimeTypes.guessExtensionFromMimeType(tituloExtension[1]);
                                            formato = formato.substring(1);
                                            lan6Documento.setFormat(formato);
                                            log.info("Formato: " + lan6Documento.getFormat());


                                            lan6Documento.setTipoDocumental(Lan6Constantes.TIPO_DOCUMENTAL_ARCHIVO);
                                            log.info("Tipo documental: " + lan6Documento.getTipoDocumental());
                                            lan6Documento.setOrigen(Lan6Constantes.MD_ORIGEN_ADMINISTRACION);
                                            log.info("Origen: " + lan6Documento.getOrigen());

                                            // LOS DOCUMENTOS EXTERNOS SE GRABAN EN DOKUSI CUANDO SE ADJUNTAN.
                                            // OBTENEMOS EL ID DE DOKUSI
                                            idDokusi = documentoDokusiDAO.getIdDokusiDocumentoExterno(docExt.getIdDocExterno(), this.getAdaptSQLBD(String.valueOf(codOrg)));

                                            if (idDokusi == null || idDokusi.equals("")) {
                                                log.info("No se ha encontrado el documento que buscamos, por lo que no tenemos su Id en Dokusi");
                                                dev = false;
                                                //return dev;
                                            } else {
                                                lan6Documento.setIdDocumento(idDokusi);
                                                log.info("Id documento: " + lan6Documento.getIdDocumento());
                                            }
                                            lan6Documento.setConvertirAPdf(false);
                                            if (formato.equalsIgnoreCase("pdf")){
                                                lan6Documento.setPDFA(false);
                                                log.info("pdfa = " + lan6Documento.isPDFA());
                                                //   lan6Documento.setConvertirAPdf(true);
                                            }
//                                                    lan6Documento.setIdExpediente(numExpediente);
//                                                    lan6Documento.setIdProcPlatea(procedimientoPlatea);

                                            documentos.add(lan6Documento);
                                            log.info("Se añade el nuevo documento - OID " + idDokusi);

                                        } // documentos EXTERNOS

                                        log.info("Se ha notificado el documento");
                                        lan6TramiteNotificacion.setDocumentos(documentos);
                                        lan6TramiteNotificacion.setMailsAvisos(formatearEmailsAvisoNotificacionDsdNotificacionVO(notificacionVO,autorizado.getRol()));
                                        lan6TramiteNotificacion.setTfnosAvisos(formatearTlfnosAvisoNotificacionDsdNotificacionVO(notificacionVO,autorizado.getRol()));
                                        log.info("mails: " + lan6TramiteNotificacion.getMailsAvisos());
                                        log.info("sms: " + lan6TramiteNotificacion.getTfnosAvisos());
                                        //log.error("idioma: " + notificacionVO.getIdioma());
                                        String idioma = Lan6Constantes.IDIOMA_ES;
                                        if (notificacionVO.getIdioma() != null) {
                                            if (notificacionVO.getIdioma().equals("eu")) {
                                                idioma = Lan6Constantes.IDIOMA_EU;
                                            }
                                        }
                                        lan6TramiteNotificacion.setIdiomaNotificacion(idioma);
                                        log.info("Idioma: " + lan6TramiteNotificacion.getIdiomaNotificacion());

                                        log.info("documentos.size() " + documentos.size());
                                        log.info("Se envian " + docsTramite + " documentos de tramite");
                                        int docsExternos = documentos.size() - docsTramite;
                                        int tramIncu = Integer.parseInt(ConfigurationParameter.getParameter(ConstantesPluginNotificacionPlatea.TRAMITE_INCUMPLIMIENTO, ConstantesPluginNotificacionPlatea.FICHERO_PROPIEDADES));
                                        log.info("Se envian " + docsExternos + " documentos externos");

                                        if (notificacionVO.getCodigoProcedimiento().equals("AACC")) {
                                            if (notificacionVO.getCodigoTramite() == tramIncu) {
                                                lan6TramiteNotificacion.setDescTramiteNotificacion(Lan6Constantes.TRAMITE_RESOLUCION_INCUMPLIMIENTO_DESC);
                                            }
                                        }

                                        // Llamada método
                                        if (docNotif) {

                                            String idNotificacionPublicada = "";
                                            log.info("Vamos a realizar la llamada a crearYPublicarNotificacion");
                                            String informacionLan6TramiteNotificacion= objetoLan6TramiteNotificacionTexto(lan6TramiteNotificacion);
                                            log.info("Enviamos el objeto:"+informacionLan6TramiteNotificacion);
                                            //Grabamos en la bbdd en el campo xml información la informacion que mandamos en el objeto lan6TramiteNotificacion
                                            PluginNotificacionPlateaNotificacionDAO.getInstance().updateNotificacionXML(notificacionVO,informacionLan6TramiteNotificacion, this.getAdaptSQLBD(String.valueOf(codOrg)));
                                            idNotificacionPublicada = servicioNotificacion.crearYPublicarNotificacion(lan6TramiteNotificacion);
                                            //idNotificacionPublicada= servicioNotificacion.crearYPublicarNotificacion(null);
                                            log.info("El resultado de la llamada a Platea es " + idNotificacionPublicada);

                                            if (idNotificacionPublicada != null && !idNotificacionPublicada.isEmpty()) {
                                                dev = true;
                                                log.info("crearTramiteNotificacion ");
                                                servicioNotificacion.crearTramiteNotificacion(lan6TramiteNotificacion, idNotificacionPublicada);
                                                PluginNotificacionPlateaNotificacionDAO notificacionDAO = PluginNotificacionPlateaNotificacionDAO.getInstance();
                                                boolean exito = notificacionDAO.updateNotificacion(notificacionVO, idNotificacionPublicada, this.getAdaptSQLBD(String.valueOf(codOrg)));
                                                log.info("Exito al actualizar notificacion con Id de Platea " + exito);
                                            } else {
                                                dev = false;
                                            }
                                        } else {
                                            try {
                                                //#282252
                                                //int intentos = numIntentos + 1;
                                                String error = "El campo RELDOC_PREPNOTIF de la tabla MELANBIDE_DOKUSI_RELDOC_TRAMIT para el documento con OID " + idDokusi + " no está actualizado. La transformación no se ha realizado, por lo que no se puede enviar el documento en la notificación.";
                                                log.error("El campo RELDOC_PREPNOTIF de la tabla MELANBIDE_DOKUSI_RELDOC_TRAMIT para el documento con OID " + idDokusi + " no está actualizado. La transformación no se ha realizado, por lo que no se puede enviar el documento en la notificación.");
                                                //PluginNotificacionPlateaNotificacionDAO.getInstance().actualizarError(id, intentos, error, con);
                                                PluginNotificacionPlateaNotificacionDAO.getInstance().actualizarError(id, error, con);
                                                log.error("enviamos el error");
                                                ErrorBean err = new ErrorBean();
                                                err.setIdError("NOTIFICACIONES_003");
                                                err.setMensajeError("El campo RELDOC_PREPNOTIF de la tabla MELANBIDE_DOKUSI_RELDOC_TRAMIT para el documento con OID " + idDokusi + " no está actualizado. La transformación no se ha realizado, por lo que no se puede enviar el documento en la notificación.");
                                                err.setSituacion("JobExecute");

                                                PluginNotificacionPlatea.grabarError(err, error, error, numExpediente);

                                            } catch (Exception e) {
                                                log.error("Error en la función actualizarError: " + e.getMessage().toString());

                                            }
                                        }
                                    } catch (Lan6NotificacionesExcepcion ice) {
                                        log.error("Error en el job de notificacion Lan6NotificacionesExcepcion: ", ice);
                                        try {
                                            int x = 3;
                                            ArrayList<String> codes = ice.getCodes();
                                            ArrayList<String> messages = ice.getMessages();
                                            String causa = "";//ice.getCausaExcepcion();
                                            causa = ice.getCausaExcepcion();
                                            causa += " " + ice.getMessage();
                                            log.error("causa error en el job de notificacion Lan6NotificacionesExcepcion: " + causa);
                                            StackTraceElement[] stacktrace = ice.getStackTrace();
                                            String error = "";
//                                      for (int i=0; i<codes.size();i++){
//                                          error += messages.get(i);
//                                          if(codes.get(i).equals("NSHF_002") || codes.get(i).equals("NOTIF_003")){
//                                             x = PluginNotificacionPlateaNotificacionDAO.getInstance().obtieneIntentos(id, con);
//                                             x++;
//                                             break;
//                                          }
//                                      }
                                            if (null != ice.getTrazaExcepcion()) {
                                                error = ice.getTrazaExcepcion();
                                            }
                                            //#282252   
                                            //log.error("num intentos: " + x);

                                            ErrorBean err = new ErrorBean();
                                            err.setIdError("NOTIFICACIONES_004");
                                            err.setMensajeError("Error en el job de notificacion Lan6NotificacionesExcepcion");
                                            err.setSituacion("JobExecute");
                                            err.setIdClave("Notificación - " + notificacionVO.getCodigoNotificacion());

                                            PluginNotificacionPlatea.grabarError(err, error, causa, numExpediente);
                                            //#282252 
                                            //PluginNotificacionPlateaNotificacionDAO.getInstance().actualizarError(id, x, error, con);
                                            PluginNotificacionPlateaNotificacionDAO.getInstance().actualizarError(id, error, con);

                                            log.error("despues");
                                          
                                        } catch (Exception e) {
                                            log.error("Error en la función actualizarError: ", e);

                                        }

                                    } catch (Lan6Excepcion ex) {
                                        log.error("Error en el job de notificacion Lan6Excepcion: ", ex);
                                        int x = 3;
                                        ArrayList<String> codes = ex.getCodes();
                                        ArrayList<String> messages = ex.getMessages();
                                        String causa = ex.getCausaExcepcion();

                                        log.error("causa error en el job de notificacion Lan6Excepcion: " + causa);
                                        String error = "";
                                        for (int i = 0; i < codes.size(); i++) {
                                            error += messages.get(i);
                                            if (codes.get(i).equals("NSHF_002") || codes.get(i).equals("NOTIF_003")) {
                                                x = 2;
                                            }
                                        }
                                        try {
                                            log.error(error);


                                            ErrorBean err = new ErrorBean();
                                            err.setIdError("NOTIFICACIONES_004");
                                            err.setMensajeError("Error en el job de notificacion Lan6Excepcion");
                                            err.setSituacion("JobExecute");

                                            PluginNotificacionPlatea.grabarError(err, error, causa, numExpediente);
                                            //#282252 
                                            //PluginNotificacionPlateaNotificacionDAO.getInstance().actualizarError(id, x, error, con);
                                            PluginNotificacionPlateaNotificacionDAO.getInstance().actualizarError(id, error, con);
                                            log.error("despues");
                                        } catch (Exception e) {
                                            log.error("Error en la función actualizarError: " + e.getMessage());

                                        }
                                    } catch (Exception e) {
                                        log.error("Error en el job de notificacion: ", e);

                                        try {
                                            //#282252 
                                            //int intentos = numIntentos + 1;
                                            //PluginNotificacionPlateaNotificacionDAO.getInstance().actualizarError(id, intentos, e.toString(), con);
                                            PluginNotificacionPlateaNotificacionDAO.getInstance().actualizarError(id, e.toString(), con);
                                        } catch (Exception i) {
                                            log.error("Error en la función actualizarError: " + i.getMessage());

                                        }

                                        try {
                                            //throw e;
                                        } catch (Exception ex) {
                                            java.util.logging.Logger.getLogger(PluginNotificacionJob.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }
                                }
                            }

                            if (dosEntornos) {
                                codOrg++;
                            } else {
                                codOrg = 2;
                            }
                            
                            try{
                                if (con != null && !con.isClosed()) {
                                    con.close();
                                }
                                log.debug("Conexión cerrada en el OAD");
                            } catch (SQLException sqle) {
                                log.error("*** ConexionBD: "+ sqle.toString());
                                throw new BDException(999,"Error, no se pudo cerrar la conexion", sqle.toString());
                             }                           
                            
                        }
                    } catch (Exception e) {
                        log.error("Error en el job de notificacion: ", e);

                        try {
                            //#282252
                            //int intentos = numIntentos + 1;
                            //PluginNotificacionPlateaNotificacionDAO.getInstance().actualizarError(id, intentos, e.toString(), con);
                            PluginNotificacionPlateaNotificacionDAO.getInstance().actualizarError(id, e.toString(), con);
                        } catch (Exception i) {
                            log.error("Error en la función actualizarError: " + i.getMessage());

                        }

                        try {
                            throw e;
                        } catch (Exception ex) {
                            java.util.logging.Logger.getLogger(PluginNotificacionJob.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }
                }
            }
        } catch (Exception ex) {
            log.error("Error: " + ex);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    java.util.logging.Logger.getLogger(PluginNotificacionJob.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    protected static Config conf
            = ConfigServiceHelper.getConfig("notificaciones");

    private String codOrganizacion;

    public String getCodOrganizacion() {
        return codOrganizacion;
    }

    public void setCodOrganizacion(String codOrganizacion) {
        this.codOrganizacion = codOrganizacion;
    }

    private String[] getTituloExtension(String fichero, String contentType, boolean esDocumentoTramite) {
        log.info("Titulo " + fichero + " , tipo " + contentType);
        String[] res = new String[2];
        res[0] = "";
        res[1] = "";

        if (esDocumentoTramite) {
            // ES DOCUMENTO DEL TRAMITE, NO TIENE EXTENSION
            //res[0] = sustituirCaracteresEspeciales(fichero);
            res[0] = fichero;
            res[1] = "pdf";
        } else {
            // ES DOCUMENTO EXTERNO ADJUNTO, SACAMOS LA EXTENSION DEL contentType
            int dotSlash = contentType != null ? contentType.lastIndexOf("/") : -1;
            if (dotSlash == -1) {
                // EL contentType NO TIENE NINGUNA BARRA
                res[0] = fichero;
                res[1] = contentType;
            } else {
                // EL contentType TIENE BARRA
                res[0] = fichero;
                res[1] = contentType.substring(dotSlash + 1);
            }
        }

        return res;
    }

    private String sustituirCaracteresEspeciales(String input) {
        String s = input.replaceAll(" ", "");
        s = s.replaceAll("á", "a");
        s = s.replaceAll("é", "e");
        s = s.replaceAll("í", "i");
        s = s.replaceAll("ó", "o");
        s = s.replaceAll("ú", "u");
        s = s.replaceAll("Á", "A");
        s = s.replaceAll("É", "E");
        s = s.replaceAll("Í", "I");
        s = s.replaceAll("Ó", "O");
        s = s.replaceAll("Ú", "U");

        return s;
    }

    private String getExtensionFichero(String contentType) {
        int slashIndex = contentType != null ? contentType.lastIndexOf("/") : -1;
        if (slashIndex == -1) {
            return contentType;
        } else {
            return contentType.substring(slashIndex + 1);
        }
    }

    private String getEquivalenciaTipoNotificacion(String tipoNotifFlexia) {
        log.info("tipoNotifFlexia: " + tipoNotifFlexia);
        if (tipoNotifFlexia != null) {
            if (tipoNotifFlexia.equals("1")) {
                return Lan6Constantes.Notificaciones.ACTO_NOTIFICADO_REQ_SUB;
            } else if (tipoNotifFlexia.equals("2")) {
                return Lan6Constantes.Notificaciones.ACTO_NOTIFICADO_RESOL;
            } else if (tipoNotifFlexia.equals("3")) {
                return Lan6Constantes.Notificaciones.ACTO_NOTIFICADO_REQ_ALEG;
            } else if (tipoNotifFlexia.equals("4")) {
                return Lan6Constantes.Notificaciones.ACTO_NOTIFICADO_COMUNICATION;
            } else if (tipoNotifFlexia.equals("5")) {
                return Lan6Constantes.Notificaciones.ACTO_NOTIFICADO_REQUERIMIENTO;
            } else if (tipoNotifFlexia.equals("6")) {
                return Lan6Constantes.Notificaciones.ACTO_NOTIFICADO_RESOL_ALEG;
            } else if (tipoNotifFlexia.equals("7")) {
                return Lan6Constantes.Notificaciones.ACTO_NOTIFICADO_RESOL_RECUR;
            } else if (tipoNotifFlexia.equals("8")) {
                return Lan6Constantes.Notificaciones.ACTO_NOTIFICADO_RESOL_DESIS;
            } else if (tipoNotifFlexia.equals("9")) {
                return Lan6Constantes.Notificaciones.ACTO_NOTIFICADO_RESOL_RENUN;
            } else if (tipoNotifFlexia.equals("10")) {
                return Lan6Constantes.Notificaciones.ACTO_NOTIFICADO_OTROS_MOD_IMPORTE;
            } else if (tipoNotifFlexia.equals("11")) {
                return Lan6Constantes.Notificaciones.ACTO_NOTIFICADO_RESOL_MOD_IMPORTE;
            } else if (tipoNotifFlexia.equals("12")) {
                return Lan6Constantes.Notificaciones.ACTO_NOTIFICADO_RENUN_CARTA_PAGO;
            } else {
                return "";
            }
        }
        return "";
    }

    private AdaptadorSQLBD getAdaptSQLBD(String codOrganizacion) {
        ResourceBundle config = ResourceBundle.getBundle("techserver");
        String gestor = config.getString("CON.gestor");
        String jndiGenerico = config.getString("CON.jndi");
        Connection conGenerico = null;
        Statement st = null;
        ResultSet rs = null;
        String[] salida = null;
        Connection con = null;

        DataSource ds = null;
        AdaptadorSQLBD adapt = null;
        synchronized (this) {
            try {
                PortableContext pc = PortableContext.getInstance();
                if (log.isDebugEnabled()) {
                    log.debug("He cogido el jndi: " + jndiGenerico);
                }
                ds = (DataSource) pc.lookup(jndiGenerico, DataSource.class);
                // Conexión al esquema genérico
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
                te.printStackTrace();
                log.error("*** AdaptadorSQLBD: " + te.toString());
            } catch (SQLException e) {
                e.printStackTrace();
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
                    e.printStackTrace();
                }//try-catch
            }// finally

        }// synchronized
        return adapt;
    }//getConnection

    private String formatearEmailsAvisoNotificacionDsdNotificacionVO(NotificacionBVO notificacionVO,int rol) {
        String respuesta="";
        if(notificacionVO!=null){
            if(rol==2){
                if (notificacionVO.getEmails() != null && !notificacionVO.getEmails().isEmpty()) {
                    respuesta += (respuesta.isEmpty() ? notificacionVO.getEmails() : ";" + notificacionVO.getEmails());
                }
            }else{
                if (notificacionVO.getEmailsTitular() != null && !notificacionVO.getEmailsTitular().isEmpty()) {
                    respuesta += (respuesta.isEmpty() ? notificacionVO.getEmailsTitular() : ";" + notificacionVO.getEmailsTitular());
                }
            }
           
        }
        return respuesta;
    }

    private String formatearTlfnosAvisoNotificacionDsdNotificacionVO(NotificacionBVO notificacionVO, int rol) {
        String respuesta = "";
        if (notificacionVO != null) {
            if(rol==2){
                if (notificacionVO.getSms() != null && !notificacionVO.getSms().isEmpty()) {
                    respuesta += (respuesta.isEmpty() ? notificacionVO.getSms() : ";" + notificacionVO.getSms());
                }
            }else{
                if (notificacionVO.getSmsTitular() != null && !notificacionVO.getSmsTitular().isEmpty()) {
                    respuesta += (respuesta.isEmpty() ? notificacionVO.getSmsTitular() : ";" + notificacionVO.getSmsTitular());
                }
            }
           
            
        }
        return respuesta;
    }

    /**
     * Convierte el objeto a un string para ser guardado en la bbdd
     * 
     * @param lan6TramiteNotificacion
     * @return 
     */
    
    private String objetoLan6TramiteNotificacionTexto(Lan6TramiteNotificacion lan6TramiteNotificacion) {

        try {
            String x = "numExpediente: " + lan6TramiteNotificacion.getNumExpediente()
                    + "| ejercicioExpediente: " + lan6TramiteNotificacion.getEjercicioExpediente()
                    + "| idActoNotificado: " + lan6TramiteNotificacion.getIdActoNotificado()
                    + "| descActoNotificado: " + lan6TramiteNotificacion.getDescActoNotificado()
                    + "| descTramiteNotificacion: " + lan6TramiteNotificacion.getDescTramiteNotificacion()
                    + "| idDestinatario: " + lan6TramiteNotificacion.getIdDestinatario()
                    + "| nombreDestinatario: " + lan6TramiteNotificacion.getNombreDestinatario()
                    + "| idiomaNotificacion: " + lan6TramiteNotificacion.getIdiomaNotificacion()
                    + "| textoNotificacion: " + lan6TramiteNotificacion.getTextoNotificacion()
                    + "| idEmisor: " + lan6TramiteNotificacion.getIdEmisor()
                    + "| nombreEmisor: " + lan6TramiteNotificacion.getNombreEmisor()
                    + "| mailsAvisos: " + lan6TramiteNotificacion.getMailsAvisos()
                    + "| tfnosAvisos: " + lan6TramiteNotificacion.getTfnosAvisos();
        
        x = x+"| documentos: [";
        try {
               for(Lan6Documento documento : lan6TramiteNotificacion.getDocumentos()){
                    String documentoString = "("
                            + "| naturaleza: " + documento.getNaturaleza()
                            + "| idDocumento: " + documento.getIdDocumento()
                            + "| format: " + documento.getFormat()
                            + "| nombre: " + documento.getNombre()
                            + "| titulo: " + documento.getTitulo()
                            + "| serieDocumental: " + documento.getSerieDocumental()
                            + "| origen: " + documento.getOrigen()
                            + "| tipoDocumental: " + documento.getTipoDocumental()
                            + "| asuntoDocumental: " + documento.getAsuntoDocumental()
                            + "| version: " + documento.getVersion()
                            + "| convertirAPdf: " + documento.isConvertirAPdf()
                            + "| conLocalizador: " + documento.isConLocalizador()
                            + "| sincrono: " + documento.isSincrono()
                            + "| PDFA: " + documento.isPDFA()
                            + "| reducirQR: " + documento.isReducirQR()
                            + "| idProcArchivoDigital: " + documento.getIdProcArchivoDigital()
                            + "| numTitular: " + documento.getNumTitular()
                            + "| tipoFirma: " + documento.getTipoFirma()
                            + "| fechaCreacion: " + documento.getFechaCreacion()
                            + "| idExpediente: " + documento.getIdExpediente()
                            + "| idRutaPif: " + documento.getIdRutaPif()
                            + ")";
                x=x+documentoString;
            }
        } catch (Exception e) {
            x= x + "Error al intentar parsear los documentos:"+ e.getLocalizedMessage();
        }
        x=x+"]";
        return x;
        } catch (Exception e) {
            //Mostramos error por si el parseo ha ido mal
            String y="Error al intentar parsear el objeto Lan6TramiteNotificacion para guardar en bbdd: "+ e.getMessage();
            log.error(y);
            return y;
        }
    }
}
