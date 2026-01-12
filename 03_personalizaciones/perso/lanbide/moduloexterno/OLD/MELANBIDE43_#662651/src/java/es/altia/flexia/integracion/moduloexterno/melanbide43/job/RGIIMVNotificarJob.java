/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.altia.flexia.integracion.moduloexterno.melanbide43.job;

import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.melanbide43.MELANBIDE43;
import es.altia.flexia.integracion.moduloexterno.melanbide43.dao.AdjuntarDocumentoExternoDAO;
import es.altia.flexia.integracion.moduloexterno.melanbide43.dao.MeLanbide43DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide43.manager.MeLanbide43Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide43.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide43.util.ConstantesMeLanbide43;
import es.altia.flexia.integracion.moduloexterno.melanbide43.util.MeLanbide43BDUtil;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.ResultadoConsultaNotificarVO;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.Tramite9099CerrarVO;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.Tramite9099VO;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExternoParamAdicionales;
import es.altia.flexia.notificacion.persistence.AutorizadoNotificacionManager;
import es.altia.flexia.notificacion.persistence.NotificacionDAO;
import es.altia.flexia.notificacion.plugin.FactoriaPluginNotificacion;
import es.altia.flexia.notificacion.plugin.PluginNotificacion;
import es.altia.flexia.notificacion.vo.AdjuntoNotificacionVO;
import es.altia.flexia.notificacion.vo.NotificacionVO;
import es.altia.flexiaWS.documentos.bd.util.Configuracion;
import es.altia.technical.PortableContext;
import es.altia.util.commons.MimeTypes;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.lanbide.lan6.adaptadoresPlatea.dokusi.beans.Lan6Documento;
import es.lanbide.lan6.adaptadoresPlatea.dokusi.servicios.Lan6DokusiServicios;
import es.lanbide.lan6.adaptadoresPlatea.excepciones.Lan6Excepcion;
import es.lanbide.lan6.adaptadoresPlatea.utilidades.config.Lan6Config;
import es.lanbide.lan6.adaptadoresPlatea.utilidades.constantes.Lan6Constantes;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;

/**
 *
 * @author alexandrep
 */
public class RGIIMVNotificarJob implements Job{
    
      private final Logger log = Logger.getLogger(RGIIMVNotificarJob.class);
      SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMddHHmmssSSS");
       public static final String DOT = ".";
       
     @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        try {
            int contador = jec.getRefireCount();
            Job trab = jec.getJobInstance();

            log.info(this.getClass().getName() + " jec.getRefireCount(): " + contador);
            Trigger trigger = jec.getTrigger();

            String servidor = ConfigurationParameter.getParameter(ConstantesMeLanbide43.CAMPO_SERVIDOR, ConstantesMeLanbide43.FICHERO_PROPIEDADES);

            //String servidor = "flexia1"; //DESA
            //String servidor = "paprergi1_flexia1"; //PRE
            //String servidor = "pargi1_flexia1"; //PRO
            log.info(this.getClass().getName() + " servidor: " + servidor);
          //  if (servidor.equals(System.getProperty("weblogic.Name"))) {//PARA LOCAL QUITAR
                synchronized (jec) {
                    
                    Connection con = null;
                    boolean dev = false;
                    int numIntentos = 0;
                    String numExpediente = "";
                    int id = 0;
                    String[] params = null;
                    
                    try {
                        log.info(this.getClass().getSimpleName()+" CierreTramiteEspera9099Job - Execute lanzado " + System.getProperty("weblogic.Name"));

                        Integer codOrg = Integer.parseInt(ConfigurationParameter.getParameter("COD_ORG", ConstantesMeLanbide43.FICHERO_PROPIEDADES));
                        boolean dosEntornos = Boolean.getBoolean(ConfigurationParameter.getParameter("DOS_ENTORNOS",
                                ConstantesMeLanbide43.FICHERO_PROPIEDADES));
                        params = Configuracion.getParamsBD(codOrg.toString());
                        while (codOrg < 2) {
                            MeLanbide43BDUtil meLanbide43BDUtil = new MeLanbide43BDUtil();
                            AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrg));
                            con = adaptador.getConnection();
                            log.info("CierreTramiteEspera9099Job - En el while de tokens codOrg: " + codOrg);
                            
                           //Primero llamamos a la funcion del metodo 111 de RGI
                            String procedimiento = "RGI";
                            String campoOidDocumento ="IDDOCREQUER";
                            String codTramiteExternoRequerir="111";
                            String codTramiteExternoSeguimiento="112";
                            String procedimientoPlatea = "LAN31_RGI";   
                            String textoNotificado = "RGI 111 notificar texto";
                            String actoNotificado = "RGI 111 notificar";

                            try{
                                log.info("Inciamos RGI 111");
                            obtenerTramitesTratar111y113(procedimiento,campoOidDocumento,codTramiteExternoRequerir,codTramiteExternoSeguimiento,procedimientoPlatea,con,String.valueOf(codOrg),params,actoNotificado,textoNotificado);
                            } catch (Exception e) {
                                log.error("Error no controlado al intentar enviar notificaciones de "+procedimiento+" tramite con codigo externo "+codTramiteExternoRequerir);
                            }
                            
                            
                            
                           //Llamamos a la funcion del metodo 113 de RGI
                            procedimiento = "RGI";
                            campoOidDocumento ="IDDOCREQUIS";
                            codTramiteExternoRequerir="113";
                            codTramiteExternoSeguimiento="114";
                            procedimientoPlatea = "LAN31_RGI"; 
                            textoNotificado = "RGI 113 notificar texto";
                            actoNotificado = "RGI 113 notificar";
                            
                           try{
                               log.info("Inciamos RGI 113");
                            obtenerTramitesTratar111y113(procedimiento,campoOidDocumento,codTramiteExternoRequerir,codTramiteExternoSeguimiento,procedimientoPlatea,con,String.valueOf(codOrg),params,actoNotificado,textoNotificado);
                            } catch (Exception e) {
                                log.error("Error no controlado al intentar enviar notificaciones de "+procedimiento+" tramite con codigo externo "+codTramiteExternoRequerir);
                            }
                           
                           //Llamamos a la funcion del metodo 111 de IMV
                           
                               procedimiento = "IMV";
                            campoOidDocumento ="IDDOCREQUER";
                            codTramiteExternoRequerir="111";
                            codTramiteExternoSeguimiento="112";
                            procedimientoPlatea = "LAN33_IMV"; 
                            textoNotificado = "IMV 111 notificar texto";
                            actoNotificado = "IMV 111 notificar";
                            
                           try{
                               log.info("Inciamos IMV 111");
                            obtenerTramitesTratar111y113(procedimiento,campoOidDocumento,codTramiteExternoRequerir,codTramiteExternoSeguimiento,procedimientoPlatea,con,String.valueOf(codOrg),params,actoNotificado,textoNotificado);
                            } catch (Exception e) {
                                log.error("Error no controlado al intentar enviar notificaciones de "+procedimiento+" tramite con codigo externo "+codTramiteExternoRequerir);
                            }
                           
                           //Llamamos a la funcion del metodo 113 de IMV
                           
                             
                               procedimiento = "IMV";
                            campoOidDocumento ="IDDOCREQUIS";
                            codTramiteExternoRequerir="113";
                            codTramiteExternoSeguimiento="114";
                            procedimientoPlatea = "LAN33_IMV"; 
                             textoNotificado = "IMV 113 notificar texto";
                            actoNotificado = "IMV 113 notificar";
                            
                           try{
                               log.info("Inciamos IMV 113");
                            obtenerTramitesTratar111y113(procedimiento,campoOidDocumento,codTramiteExternoRequerir,codTramiteExternoSeguimiento,procedimientoPlatea,con,String.valueOf(codOrg),params,actoNotificado,textoNotificado);
                            } catch (Exception e) {
                                log.error("Error no controlado al intentar enviar notificaciones de "+procedimiento+" tramite con codigo externo "+codTramiteExternoRequerir);
                            }
                           
                           
                           //LLamamos a la funcion del metodo Resolucion de RGI
                           
                            procedimiento = "RGI";
                            campoOidDocumento ="IDDOCRESOL";
                            String codTramiteExternoResolucion = "6";
                            procedimientoPlatea = "LAN31_RGI"; 
                             textoNotificado = "RGI 8 notificar texto";
                            actoNotificado = "RGI 8 notificar";
                           String idAcuseResolucion = "IDDOCACUSE";
                           
                            try{
                                
                            log.info("Inciamos RGI 6 resolucion notificar");
                            obtenerTramitesTratar3(procedimiento,campoOidDocumento,codTramiteExternoResolucion,codTramiteExternoSeguimiento,procedimientoPlatea,con,String.valueOf(codOrg),params,actoNotificado,textoNotificado);
                            } catch (Exception e) {
                                log.error("Error no controlado al intentar enviar notificaciones de "+procedimiento+" tramite con codigo externo "+codTramiteExternoRequerir);
                            }
                           
                           //LLamamos a la funcion del metodo Resolucion de IMV
                            procedimiento = "IMV";
                            campoOidDocumento ="IDDOCRESOL";
                            codTramiteExternoResolucion = "3";
                            procedimientoPlatea = "LAN33_IMV"; 
                            textoNotificado = "IMV 3 notificar texto";
                            actoNotificado = "IMV 3 notificar";
                           idAcuseResolucion = "IDDOCACUSE";
                           
                            try{
                                log.info("Inciamos IMV 3 resolucion notificar");
                            obtenerTramitesTratar3(procedimiento,campoOidDocumento,codTramiteExternoResolucion,codTramiteExternoSeguimiento,procedimientoPlatea,con,String.valueOf(codOrg),params,actoNotificado,textoNotificado);
                            } catch (Exception e) {
                                log.error("Error no controlado al intentar enviar notificaciones de "+procedimiento+" tramite con codigo externo "+codTramiteExternoRequerir);
                            } 
                           
                           
                           
                            if (dosEntornos) {
                                codOrg++;
                            } else {
                                codOrg = 2;
                            }
                            if (con != null) {
                                con.close();
                            }

                        }
                    } catch (Exception e) {
                        log.error(RGIIMVNotificarJob.class.getName() + " Error en el job : ", e);
                        try {
                            int intentos = numIntentos + 1;
                        } catch (Exception i) {
                            log.error("Error en int intentos = numIntentos + 1 : " + i.getMessage());
                        }
                    } finally {
                        if (con != null) {
                            try {
                                con.close();
                            } catch (SQLException ex) {
                                log.error(RGIIMVNotificarJob.class.getName()+" - Error cerrando BBDD", ex);
                            }
                        }
                    }
                }
            
        log.error(this.getClass().getName() + ".execute -  Fin " + new Date().toString());
           // }
    } catch (Exception ex) {
            log.error(this.getClass().getName()  + " Error: "+ex.getMessage(),ex);
        }
        
    }

    /**
     * Operación que recupera los datos de conexión a la BBDD
     * @param codOrganizacion
     * @return AdaptadorSQLBD
     */
    private AdaptadorSQLBD getAdaptSQLBD(String codOrganizacion){
        if(log.isDebugEnabled()) log.debug("getConnection ( codOrganizacion = " + codOrganizacion + " ) : BEGIN");
        ResourceBundle config = ResourceBundle.getBundle("techserver");
        String gestor = config.getString("CON.gestor");
        String jndiGenerico = config.getString("CON.jndi");
        Connection conGenerico = null;
        Statement st = null;
        ResultSet rs = null;
        String[] salida = null;
        Connection con = null;
        
        if(log.isDebugEnabled()){
            log.debug("getJndi =========> ");
            log.debug("parametro codOrganizacion: " + codOrganizacion);
            log.debug("gestor: " + gestor);
            log.debug("jndi: " + jndiGenerico);
        }//if(log.isDebugEnabled())

        DataSource ds = null;
        AdaptadorSQLBD adapt = null;
        synchronized (this) {
            try{
                PortableContext pc = PortableContext.getInstance();
                if(log.isDebugEnabled())log.debug("He cogido el jndi: " + jndiGenerico);
                ds = (DataSource)pc.lookup(jndiGenerico, DataSource.class);
                // Conexión al esquema genérico
                conGenerico = ds.getConnection();

                String sql = "SELECT EEA_BDE FROM A_EEA WHERE EEA_APL=" + ConstantesDatos.APP_GESTION_EXPEDIENTES + " AND AAE_ORG=" + codOrganizacion;
                st = conGenerico.createStatement();
                rs = st.executeQuery(sql);
                String jndi = null;
                while(rs.next()){
                    jndi = rs.getString("EEA_BDE");
                }//while(rs.next())

                st.close();
                rs.close();
                conGenerico.close();

                if(jndi!=null && gestor!=null && !"".equals(jndi) && !"".equals(gestor)){
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
            }catch(TechnicalException te){
                te.printStackTrace();
                log.error("*** AdaptadorSQLBD: " + te.toString());
            }catch(SQLException e){
                e.printStackTrace();
            }finally{
                try{
                    if(st!=null) st.close();
                    if(rs!=null) rs.close();
                    if(conGenerico!=null && !conGenerico.isClosed())
                    conGenerico.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }//try-catch
            }// finally
            if(log.isDebugEnabled()) log.debug("getConnection() : END");
         }// synchronized
        return adapt;
     }//getConnection
    
    
     private void obtenerTramitesTratar111y113(String procedimiento, String campoOidDocumento,String codTramiteExternoRequerir, String codTramiteExternoSeguimiento,String procedimientoPlatea, Connection con,String codOrg, String[] paramsBD, String actoNotificado,String textoNotificado) throws SQLException, Exception {
            List<ResultadoConsultaNotificarVO>  tramitesTratar =  recuperarExpedientesNotificarRGIIMV(procedimiento,campoOidDocumento,codTramiteExternoRequerir,codTramiteExternoSeguimiento,con);
            iniciarJobNotificarFirmar(procedimiento,campoOidDocumento,codTramiteExternoRequerir,codTramiteExternoSeguimiento,procedimientoPlatea,con,String.valueOf(codOrg),paramsBD,actoNotificado,textoNotificado,tramitesTratar);

     }
     
      private void obtenerTramitesTratar3(String procedimiento, String campoOidDocumento,String codTramiteExternoRequerir, String idAcuseJustificante,String procedimientoPlatea, Connection con,String codOrg, String[] paramsBD, String actoNotificado,String textoNotificado) throws SQLException, Exception {
            List<ResultadoConsultaNotificarVO>  tramitesTratar =  recuperarExpedientesNotificarRGIIMVResolucion(procedimiento,campoOidDocumento,codTramiteExternoRequerir,idAcuseJustificante,con);
            iniciarJobNotificarFirmar(procedimiento,campoOidDocumento,codTramiteExternoRequerir,idAcuseJustificante,procedimientoPlatea,con,String.valueOf(codOrg),paramsBD,actoNotificado,textoNotificado,tramitesTratar);

     }

    private void iniciarJobNotificarFirmar(String procedimiento, String campoOidDocumento,String codTramiteExternoRequerir, String codTramiteExternoSeguimiento,String procedimientoPlatea, Connection con,String codOrg, String[] paramsBD, String actoNotificado,String textoNotificado, List<ResultadoConsultaNotificarVO>  tramitesTratar) throws SQLException, Exception {
        log.info("iniciarJobNotificar111RGI INIT");
       //Recuperamos los expedientes a notificar con una consulta a la bbdd
       
      
      if (!tramitesTratar.isEmpty()){
          
          //Recorremos los expedientes a notificar
           for (Iterator<ResultadoConsultaNotificarVO> i = tramitesTratar.iterator(); i.hasNext();) {
            ResultadoConsultaNotificarVO item = i.next();
           try{ 
           NotificacionVO notificacionVO = new NotificacionVO();
           notificacionVO.setNumExpediente(item.getNumExpediente());
           notificacionVO.setCodigoProcedimiento(item.getProcedimiento() );
           notificacionVO.setEjercicio(Integer.valueOf(item.getEjercicio()));
           notificacionVO.setCodigoMunicipio(Integer.valueOf(item.getCodigoMunicipio()));
           notificacionVO.setCodigoTramite(Integer.valueOf(item.getCodTramite()));
           notificacionVO.setOcurrenciaTramite(Integer.valueOf(item.getOcurrenciaTramite()));
          
            log.debug("Creamos la notificacion por defecto");
           //Creamos la notificiacion por defecto
           // Se crea la notificación por defecto si es que no existe
            PluginNotificacion pluginNotificacion = FactoriaPluginNotificacion.getImpl(codOrg);
            pluginNotificacion.crearNotificacionDefecto(notificacionVO,paramsBD);
            notificacionVO.setActoNotificado(actoNotificado);  
            notificacionVO.setTextoNotificacion(textoNotificado);
             log.debug("Notificacion por defecto creada");
              log.debug("Entramos a obtener la notificacion");
            notificacionVO = pluginNotificacion.getNotificacion(notificacionVO, paramsBD);   
             log.debug("Notificacion obtenida");
             
             
            //Firmamos y localizamos el documento tiene que ser un documento pdfa TODO falta preguntar si nos lo enviaran como pdfa si no fuera asi se podria modificar mas adelante
           firmarLocalizarDocumentoOid(item.getOidDocumentoNotificar(), procedimientoPlatea);
           
           
           
              log.debug("Recuperamos oid: "+item.getOidDocumentoNotificar()+" con el codigo lan6 LAN31_RGI");
                    String idProcedimiento = Lan6Config.getProperty(Lan6Constantes.FICHERO_PROP_ADAPTADORES_PLATEA, "PROCEDIMIENTO_ID_"+procedimientoPlatea);
                    Lan6DokusiServicios servicios = new Lan6DokusiServicios(idProcedimiento);
                    Lan6Documento lan6Documento =     servicios.consultarDocumento(item.getOidDocumentoNotificar());
                    
                   byte[] fichero = lan6Documento.getContenido();
                   String mimeTypeContent = getMimeTypeFromExtension(lan6Documento.getFormat());
                   String nombreFichero = lan6Documento.getNombre();
                   String extensionFichero = DOT + lan6Documento.getFormat();
                    log.debug("Fichero: " + nombreFichero + "-" + extensionFichero);
                    
                        AdjuntoNotificacionVO adjuntoVO=new AdjuntoNotificacionVO() ;
                       

                        adjuntoVO.setCodigoMunicipio(notificacionVO.getCodigoMunicipio()); 
                        adjuntoVO.setCodigoNotificacion(notificacionVO.getCodigoNotificacion());
                        adjuntoVO.setNumeroExpediente(notificacionVO.getNumExpediente());
                        adjuntoVO.setOcurrenciaTramite(notificacionVO.getOcurrenciaTramite());
                        adjuntoVO.setCodigoTramite(notificacionVO.getCodigoTramite());
                        adjuntoVO.setNombre(nombreFichero);
                        adjuntoVO.setContentType(mimeTypeContent);
                        adjuntoVO.setContenido(fichero);
                        adjuntoVO.setEjercicio(notificacionVO.getEjercicio());
                        AdjuntarDocumentoExternoDAO adjuntarDocumentoExternoDAO = AdjuntarDocumentoExternoDAO.getInstance();
                        adjuntarDocumentoExternoDAO.insertarAdjuntoExterno(adjuntoVO,item.getOidDocumentoNotificar(),paramsBD[0],con);
                        //Una vez adjuntado debemos adjuntar el oid de dokusi para cuando se mande 
            
           
                        
            boolean retornoGrabar = false;            
             log.debug("Intentamos grabar la notificación: "+notificacionVO.getNumExpediente());
             notificacionVO.setActoNotificado(actoNotificado);  
            notificacionVO.setTextoNotificacion(textoNotificado);
            retornoGrabar=pluginNotificacion.actualizarNotificacion(notificacionVO, paramsBD);
            
            log.debug("Añadimos autorizados");
            //  //Añadimos autorizado 
            MeLanbide43BDUtil meLanbide43BDUtil = new MeLanbide43BDUtil();
             AdaptadorSQLBD adaptador = meLanbide43BDUtil.getAdaptSQLBD(String.valueOf(codOrg));
                Connection con1 = adaptador.getConnection();
            try{
             for (int j = 0; j < notificacionVO.getAutorizados().size(); j++) {
                 notificacionVO.getAutorizados().get(j).setCodigoNotificacion(notificacionVO.getCodigoNotificacion());
                    AutorizadoNotificacionManager.getInstance().insertarAutorizado(notificacionVO.getAutorizados().get(j), con1);
                    }}catch (Exception e) {
                        
                    } finally {
                    if (con1!=null){
                        con1.close();
                    }
                }   
            
             log.debug("Añadidos autorizados");
                  //Si no hay interesados se sigue hacia delante igualmente
          
                  if(retornoGrabar==true || notificacionVO.getAutorizados().isEmpty()){
                 log.debug("Procedemos a enviar la notificiacion");
                 //Procedemos a enviar la notificiacion
                 notificacionVO.setCodDepartamento(codOrg);    
                  
                 Boolean res1 = false;
                  log.debug("Intentamos enviar: "+notificacionVO.getNumExpediente());
            
                  String codigoTipoNotificacion = NotificacionDAO.getInstance().getTipoNotificacion(notificacionVO, paramsBD);
                     log.info("codigoTipoNotificacion: " + codigoTipoNotificacion);
                     notificacionVO.setCodigoTipoNotificacion(codigoTipoNotificacion);
                 
                     res1=pluginNotificacion.enviarNotificacion(notificacionVO, paramsBD);
                     if(res1==true || notificacionVO.getAutorizados().isEmpty())
                     {
                        log.info("Notificacion enviada correctamente: "+notificacionVO.getNumExpediente());
                          insertarLineasLogJob(con, item.getNumExpediente(), "OK", "Notificacion enviada correctamente");
                     }
                
                     else {
                         
                     log.info("Error al enviar la notificacion");    
                      insertarLineasLogJob(con, item.getNumExpediente(), "ERROR", "Error al enviar la notificacion");
                     }
                  } else {
                  log.info("Error al intentar actualizar la notificacion");
                    insertarLineasLogJob(con, item.getNumExpediente(), "ERROR", "Error al intentar actualizar la notificacion");
                  }
            }
           catch (Lan6Excepcion e){
             log.info("Error al intentar firmar/localizar el expediente"+item.getNumExpediente(),e);
               insertarLineasLogJob(con, item.getNumExpediente(), "ERROR", "Error al intentar firmar localizar el expediente");
           }
           catch (Exception e){
              log.info("Error al intentar tramitar el expediente"+item.getNumExpediente(),e);
               insertarLineasLogJob(con, item.getNumExpediente(), "ERROR", "Error al intentar tramitar el expediente");
          }   
          }//Cierre for
         
      } else {
          log.info("No existen expedientes a notificar.");
      }
       
      
      
      
       log.info("iniciarJobNotificar111RGI END");
    }
    
    
        /**
     * Devuelve la extension de fichero correspondiente al tipo MIME pasado.
     * Utilizamos la clase de flexia para no reescribir funciones
     */
    private String getMimeTypeFromExtension(String extension) {
        return MimeTypes.guessMimeTypeFromExtension(extension);
    }

    /**Recibimos el procedimiento y el codigo externo del tramite a comprobar con la bbdd
     * 
     * 
     * @param procedimiento
     * @param codTramiteExterno 
     */
    List<ResultadoConsultaNotificarVO> recuperarExpedientesNotificarRGIIMV(String procedimiento, String campoOidDocumento,String codTramiteExternoRequerir, String codTramiteExternoSeguimiento,Connection con) throws SQLException {
    
     log.info("getExpedientesSinNotificar - Begin () " + formatDate.format(new Date()));
        List<ResultadoConsultaNotificarVO> retorno = new ArrayList<ResultadoConsultaNotificarVO>();
        PreparedStatement pt = null;
        ResultSet rs = null;
        try { 
            String query = "";
        
                query = "SELECT ocu.cro_pro,  ocu.cro_num,  ocu.cro_eje,   ocu.cro_tra,   ocu.CRO_MUN, ocu.CRO_UTR,   ocu.CRO_OCU,   des.tml_valor,cro_fei, oid.TXTT_VALOR"
               + " FROM E_CRO ocu INNER JOIN E_TXTT oid ON ocu.cro_tra=oid.TXTT_TRA AND ocu.CRO_NUM=oid.TXTT_NUM AND ocu.cro_ocu=oid.TXTT_OCU" +
                " INNER JOIN e_tml des ON ocu.cro_pro = des.tml_pro AND ocu.cro_tra =  des.tml_tra" +
                " INNER JOIN E_TRA tra ON tra.TRA_COD=des.TML_TRA AND tra.TRA_PRO= des.TML_PRO" +
                " INNER JOIN e_exp exp ON ocu.cro_num = exp.exp_num " +
                " LEFT JOIN notificacion noti ON ocu.cro_tra = noti.COD_TRAMITE AND ocu.cro_ocu = noti.OCU_TRAMITE AND ocu.cro_num = noti.NUM_EXPEDIENTE" +
                " WHERE exp.exp_est=0 AND oid.TXTT_VALOR IS NOT NULL AND ocu.cro_pro='"+procedimiento+"' AND ocu.cro_tra = (SELECT TRA_COD FROM E_TRA WHERE TRA_COU="+codTramiteExternoRequerir+" AND TRA_FBA IS NULL AND TRA_PRO='"+procedimiento+"') AND oid.txtt_cod = '"+campoOidDocumento+"' AND (SELECT ocu2.CRO_NUM FROM E_CRO ocu2 WHERE ocu2.cro_tra = (SELECT TRA_COD FROM E_TRA WHERE TRA_COU="+codTramiteExternoSeguimiento+" AND TRA_FBA IS NULL AND TRA_PRO='"+procedimiento+"') AND ocu2.cro_num=ocu.cro_num AND ocu2.CRO_FEF IS NULL) IS NOT NULL " +
                " AND noti.cod_notificacion_platea IS NULL";

            
            log.info("sql = " + query);
            pt = con.prepareStatement(query);
            rs = pt.executeQuery();

            while (rs.next()) {
                ResultadoConsultaNotificarVO elementoListaRetorno = new ResultadoConsultaNotificarVO();

                elementoListaRetorno.setEjercicio(rs.getString("cro_eje"));
                elementoListaRetorno.setCodTramite(rs.getString("CRO_TRA"));
                elementoListaRetorno.setDesTramite(rs.getString("tml_valor"));
                elementoListaRetorno.setProcedimiento(rs.getString("cro_PRO"));
                elementoListaRetorno.setNumExpediente(rs.getString("Cro_NUM"));
                elementoListaRetorno.setFechaRegistrado(rs.getString("cro_fei"));
                elementoListaRetorno.setCodigoMunicipio(rs.getString("CRO_MUN"));
                elementoListaRetorno.setOcurrenciaTramite(rs.getString("CRO_OCU"));
                elementoListaRetorno.setUtr(rs.getString("CRO_UTR"));
                elementoListaRetorno.setOidDocumentoNotificar(rs.getString("TXTT_VALOR"));
                
                retorno.add(elementoListaRetorno);
            }
        } catch (Exception ex) {
            log.info("Se ha producido un error consultando los registros ", ex);
            throw new SQLException(ex);
        } finally {
            try {
                if (pt != null) {
                    pt.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el preparedstatement y el resulset", e);
                throw new SQLException(e);
            }
        }
        log.info("getExpedientesSinNotificar - End () " + formatDate.format(new Date()));
        return retorno;
    }
    
    List<ResultadoConsultaNotificarVO> recuperarExpedientesNotificarRGIIMVResolucion(String procedimiento, String campoOidDocumento,String idDocumentoAcuseResolucion, String codTramiteExternoSeguimiento,Connection con) throws SQLException {
    
     log.info("getExpedientesSinNotificar - Begin () " + formatDate.format(new Date()));
        List<ResultadoConsultaNotificarVO> retorno = new ArrayList<ResultadoConsultaNotificarVO>();
        PreparedStatement pt = null;
        ResultSet rs = null;
        try { 
            String query = "";
        
               query = "SELECT ocu.cro_pro,  ocu.cro_num,  ocu.cro_eje,   ocu.cro_tra,   ocu.CRO_MUN, ocu.CRO_UTR,   ocu.CRO_OCU,   des.tml_valor,cro_fei, oid.TXTT_VALOR "
              +" FROM E_CRO ocu INNER JOIN E_TXTT oid ON ocu.cro_tra=oid.TXTT_TRA AND ocu.CRO_NUM=oid.TXTT_NUM AND ocu.cro_ocu=oid.TXTT_OCU "
                 +"  INNER JOIN e_tml des ON ocu.cro_pro = des.tml_pro AND ocu.cro_tra =  des.tml_tra"
                 +"  INNER JOIN E_TRA tra ON tra.TRA_COD=des.TML_TRA AND tra.TRA_PRO= des.TML_PRO"
                 +"  INNER JOIN e_exp exp ON ocu.cro_num = exp.exp_num "
                 +"  LEFT JOIN notificacion noti ON ocu.cro_tra = noti.COD_TRAMITE AND ocu.cro_ocu = noti.OCU_TRAMITE AND ocu.cro_num = noti.NUM_EXPEDIENTE "
                +"   WHERE exp.exp_est=0 AND oid.TXTT_VALOR IS NOT NULL AND ocu.cro_pro='"+procedimiento+"' AND ocu.cro_tra = (SELECT TRA_COD FROM E_TRA WHERE TRA_COU="+idDocumentoAcuseResolucion+" AND TRA_FBA IS NULL AND TRA_PRO='"+procedimiento+"') AND oid.txtt_cod = '"+campoOidDocumento+"' AND ocu.CRO_FEF IS NULL"
                +"   AND noti.cod_notificacion_platea IS NULL";

            
            log.info("sql = " + query);
            pt = con.prepareStatement(query);
            rs = pt.executeQuery();

            while (rs.next()) {
                ResultadoConsultaNotificarVO elementoListaRetorno = new ResultadoConsultaNotificarVO();

                elementoListaRetorno.setEjercicio(rs.getString("cro_eje"));
                elementoListaRetorno.setCodTramite(rs.getString("CRO_TRA"));
                elementoListaRetorno.setDesTramite(rs.getString("tml_valor"));
                elementoListaRetorno.setProcedimiento(rs.getString("cro_PRO"));
                elementoListaRetorno.setNumExpediente(rs.getString("Cro_NUM"));
                elementoListaRetorno.setFechaRegistrado(rs.getString("cro_fei"));
                elementoListaRetorno.setCodigoMunicipio(rs.getString("CRO_MUN"));
                elementoListaRetorno.setOcurrenciaTramite(rs.getString("CRO_OCU"));
                elementoListaRetorno.setUtr(rs.getString("CRO_UTR"));
                elementoListaRetorno.setOidDocumentoNotificar(rs.getString("TXTT_VALOR"));
                
                retorno.add(elementoListaRetorno);
            }
        } catch (Exception ex) {
            log.info("Se ha producido un error consultando los registros ", ex);
            throw new SQLException(ex);
        } finally {
            try {
                if (pt != null) {
                    pt.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el preparedstatement y el resulset", e);
                throw new SQLException(e);
            }
        }
        log.info("getExpedientesSinNotificar - End () " + formatDate.format(new Date()));
        return retorno;
    }

    private void firmarLocalizarDocumentoOid(String oidDocumentoNotificar, String procedimientoPlatea) throws Lan6Excepcion {
        log.info("firmarLocalizarDocumentoOid START ");
        String idProcedimiento = Lan6Config.getProperty(Lan6Constantes.FICHERO_PROP_ADAPTADORES_PLATEA, "PROCEDIMIENTO_ID_"+procedimientoPlatea);
        Lan6DokusiServicios servicios = new Lan6DokusiServicios(idProcedimiento);
        Lan6Documento lan6Documento = new Lan6Documento();
			lan6Documento.setIdDocumento(oidDocumentoNotificar);// pdf				
                        lan6Documento.setSincrono(true);
                        servicios.crearLocalizadorDocumento(lan6Documento, false);
                        servicios.firmarDocumento(lan6Documento);
		
                        
                    log.info("firmarLocalizarDocumentoOid END ");
        
        
    }
    
    
    public int insertarLineasLogJob(Connection con, String numeroExpediente, String estado, String mensaje) throws Exception {
        log.info("insertarLineasLogJob MELANBIDE_43_TRAMITE9099_JOB - Begin () ");
        int id = 0;
       CallableStatement  pt = null;
        ResultSet rs = null;

        try {
            String query = "INSERT INTO MELANBIDE43_RGIIMVNOTIFICAR_JOB"
                    + " ( NUMEXPEDIENTE, ESTADOLOG, DESCRIPCION) "
                    + " VALUES (?,?,?)";
                log.info("query: " +query);
                log.info("----Fin aplicar condiciones----");
            
          

            pt = con.prepareCall(query);
            pt.setString(1, numeroExpediente);
            pt.setString(2, estado);
            pt.setString(3, mensaje);
           
            log.info("sql = " + query);
            pt.executeUpdate();
       
            
           
             
            
        } catch (Exception ex) {
            log.info("Se ha producido un error al registrar la linea en el log del job ", ex);
            throw new Exception(ex);
        } finally {
            try {
                if (pt != null) {
                    pt.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el preparedstatement y el resulset", e);
                throw new Exception(e);
            }
        }

        log.info("insertarLineasLogJob MELANBIDE_43_TRAMITE9099_JOB - End () ");
        return id;
    }

  
       
    
    
}
