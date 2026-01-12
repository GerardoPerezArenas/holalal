/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.altia.flexia.integracion.moduloexterno.melanbide06;

import es.altia.common.service.mail.exception.MailException;
import es.altia.common.service.mail.exception.MailServiceNotActivedException;
import es.altia.flexia.integracion.moduloexterno.melanbide06.manager.GestionAvisosManager;
import es.altia.flexia.integracion.moduloexterno.melanbide06.util.MeLanbide06ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide06.util.MeLanbide06Constantes;
import es.altia.flexia.integracion.moduloexterno.melanbide06.vo.GestionAvisosVO;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.technical.PortableContext;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.melanbide06.util.MailHelper06;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import javax.sql.DataSource;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * @author jaime.hermoso
 * @version 23/01/2023 1.0
 * Historial de cambios:
 * <ol>
 *  <li>jaime.hermoso * 23/01/2023 * Edición inicial</li>
 * </ol> 
 */
public class GestionAvisos {
    
    //Logger
    private static Logger log = LogManager.getLogger(GestionAvisos.class);
    
    //Codigos de error devueltos por las operaciones
    private final static String TODO_CORRECTO = "0";
    private final static String ERROR = "1";
    private final static String ERROR_ENVIANDO_EMAIL = "6";
    private final static String ERROR_SERVICIO_MAIL_INACTIVO = "7";
    private final static String ERROR_RECUPERANDO_EMAIL_UOR = "11";
    private final static String ERROR_RECUPERANDO_EMAIL_USUARIO = "12";
    private final static String ERROR_CODIFICACION_TEXTO = "13";
    private final static String ERROR_RECUPERANDO_CONFIG_MAIL = "14";
    
    /**
     * Envia un email al usuario que inicia el expediente
     * 
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @return 
     */
    
    public String envioCorreoGenerico(int codOrganizacion, String codEvento, String codAsunto, String codProcedimiento, Map<String,String> parametros, String opcion) throws SQLException {
        String resultado = TODO_CORRECTO;
        GestionAvisosVO gestionAvisosVO = new GestionAvisosVO();
        GestionAvisosVO gestionAvisosVariables = new GestionAvisosVO();
        AdaptadorSQLBD adaptador = getAdaptSQLBD(String.valueOf(codOrganizacion));
        GestionAvisosManager gestionAvisosmanager = new GestionAvisosManager();
        if(codEvento !=null && !codEvento.equals("")){
            if(opcion.equals("doc")){
                gestionAvisosVO = gestionAvisosmanager.recuperarConfiguracionEmail(adaptador, codEvento, codProcedimiento, opcion);
            }else if(opcion.equals("wsregistro")){
                gestionAvisosVO = gestionAvisosmanager.recuperarConfiguracionEmail(adaptador, codEvento, codAsunto, opcion);
            }
        }else{
            gestionAvisosVO = null;
        }
        
        if(gestionAvisosVO != null){
            gestionAvisosVariables = sustituirVariables(gestionAvisosVO,parametros);
            if(log.isDebugEnabled()) log.info("Enviamos el correo");
            MailHelper06 mailHelper = new MailHelper06();
            try{

            if(gestionAvisosVO.getConfiguracion_activa().equals("1")){
                String asuntoEncode = new String(gestionAvisosVariables.getAsunto().getBytes("iso-8859-1"), "iso-8859-1");
                String contenidoEncode = new String(gestionAvisosVariables.getContenido().getBytes("iso-8859-1"), "iso-8859-1");
                if(gestionAvisosVO.getEmail().contains(";")){
                    String[] parts = gestionAvisosVO.getEmail().split(";");
                    for(int i = 0; i < parts.length; i++) {
                        mailHelper.sendMail(parts[i], asuntoEncode, contenidoEncode, null);
                    }  
                }else{
                  mailHelper.sendMail(gestionAvisosVO.getEmail(), asuntoEncode, contenidoEncode, null);  
                }  
            }else{
              log.error("Se ha producido un error enviando el email");
               resultado = ERROR_ENVIANDO_EMAIL;      
            }

            }catch(MailException ex){
                log.error("Se ha producido un error enviando el email a la UOR " + ex.getMessage());
                resultado = ERROR_ENVIANDO_EMAIL;
            }catch(MailServiceNotActivedException ex){
                log.error("El servicio de email no esta activo en la aplicacion " + ex.getMessage());
                resultado = ERROR_SERVICIO_MAIL_INACTIVO;
            } catch (IOException ioe) {
                log.error("La codificación del texto ha sido errónea " + ioe.getMessage(),ioe);
                resultado = ERROR_CODIFICACION_TEXTO;
            }//try-catch
        }else{
            log.error("No se ha encontrado ninguna configuración de email");
            resultado = ERROR_RECUPERANDO_CONFIG_MAIL; 
        }
        return resultado;
    }
    
       
    public String buscarEventoExtracto(String codOrganizacion, String extracto) throws SQLException {
        String resultado;
        AdaptadorSQLBD adaptador = getAdaptSQLBD(String.valueOf(codOrganizacion));
        GestionAvisosManager gestionAvisosmanager = new GestionAvisosManager();
        resultado = gestionAvisosmanager.buscarEventoExtracto(adaptador, extracto);
        return resultado;
    }
    
    /**
     * Operación que recupera los datos de conexión a la BBDD
     * @param codOrganizacion
     * @return AdaptadorSQLBD
     */
    public AdaptadorSQLBD getAdaptSQLBD(String codOrganizacion) throws SQLException{
        if(log.isDebugEnabled()) log.info("getConnection ( codOrganizacion = " + codOrganizacion + " ) : BEGIN");
        ResourceBundle config = ResourceBundle.getBundle("mail06");
        String gestor = config.getString("CON.gestor");
        String jndiGenerico = config.getString("CON.jndi");
        Connection conGenerico = null;
        Statement st = null;
        ResultSet rs = null;
        String[] salida = null;
        Connection con = null;
        
        if(log.isDebugEnabled()){
            log.info("getJndi =========> ");
            log.info("parametro codOrganizacion: " + codOrganizacion);
            log.info("gestor: " + gestor);
            log.info("jndi: " + jndiGenerico);
        }//if(log.isDebugEnabled())

        DataSource ds = null;
        AdaptadorSQLBD adapt = null;
        synchronized (this) {
            try{
                PortableContext pc = PortableContext.getInstance();
                if(log.isDebugEnabled())log.info("He cogido el jndi: " + jndiGenerico);
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
                if(st!=null) st.close();
                if(rs!=null) rs.close();
                if(conGenerico!=null && !conGenerico.isClosed())
                conGenerico.close();
            }// finally
            if(log.isDebugEnabled()) log.info("getConnection() : END");
         }// synchronized
        return adapt;
     }//getConnection
    
    
    public GestionAvisosVO sustituirVariables(GestionAvisosVO gestionAvisos, Map<String,String> parametros){
        log.info("parametros : " + (parametros != null ? Arrays.toString(parametros.entrySet().toArray()) :"null"));
        String INTERESADO_VARIABLE = MeLanbide06ConfigurationParameter.getParameter(MeLanbide06Constantes.INTERESADO_VARIABLE, MeLanbide06Constantes.FICHERO_CONFIGURACION);
        String EJERCICIO_VARIABLE = MeLanbide06ConfigurationParameter.getParameter(MeLanbide06Constantes.EJERCICIO_VARIABLE, MeLanbide06Constantes.FICHERO_CONFIGURACION);
        String ASUNTO_VARIABLE = MeLanbide06ConfigurationParameter.getParameter(MeLanbide06Constantes.ASUNTO_VARIABLE, MeLanbide06Constantes.FICHERO_CONFIGURACION);
        String UOR_VARIABLE = MeLanbide06ConfigurationParameter.getParameter(MeLanbide06Constantes.UOR_VARIABLE, MeLanbide06Constantes.FICHERO_CONFIGURACION);
        String FECHA_PRESENTACION_VARIABLE = MeLanbide06ConfigurationParameter.getParameter(MeLanbide06Constantes.FECHA_PRESENTACION_VARIABLE, MeLanbide06Constantes.FICHERO_CONFIGURACION);
        String NUMERO_ANOTACION_VARIABLE = MeLanbide06ConfigurationParameter.getParameter(MeLanbide06Constantes.NUMERO_ANOTACION_VARIABLE, MeLanbide06Constantes.FICHERO_CONFIGURACION);
        String EXTRACTO_VARIABLE = MeLanbide06ConfigurationParameter.getParameter(MeLanbide06Constantes.EXTRACTO_VARIABLE, MeLanbide06Constantes.FICHERO_CONFIGURACION);
        String NUMERO_EXPEDIENTE_VARIABLE = MeLanbide06ConfigurationParameter.getParameter(MeLanbide06Constantes.NUMERO_EXPEDIENTE_VARIABLE, MeLanbide06Constantes.FICHERO_CONFIGURACION);
        String NOMBRE_DOCUMENTO_VARIABLE = MeLanbide06ConfigurationParameter.getParameter(MeLanbide06Constantes.NOMBRE_DOCUMENTO_VARIABLE, MeLanbide06Constantes.FICHERO_CONFIGURACION);
        String contenidoVariable = gestionAvisos.getContenido();
        String asuntoVariable = gestionAvisos.getAsunto();
            
        if(parametros != null){
            //Interesado
            if(parametros.get("interesado") != null){
                if(INTERESADO_VARIABLE!=null&& INTERESADO_VARIABLE!=""){
                    contenidoVariable=contenidoVariable.replaceAll(INTERESADO_VARIABLE,parametros.get("interesado"));
                    asuntoVariable=asuntoVariable.replaceAll(INTERESADO_VARIABLE,parametros.get("interesado"));
                }else{
                    log.info("No se ha podido recuperar el parametro " +MeLanbide06Constantes.INTERESADO_VARIABLE+ " desde el properties.");
                } 
            }

            //Ejercicio
            if(parametros.get("ejercicio") != null){
                if(EJERCICIO_VARIABLE!=null&& EJERCICIO_VARIABLE!=""){
                    contenidoVariable=contenidoVariable.replaceAll(EJERCICIO_VARIABLE,parametros.get("ejercicio"));
                    asuntoVariable=asuntoVariable.replaceAll(EJERCICIO_VARIABLE,parametros.get("ejercicio"));
                }else{
                    log.info("No se ha podido recuperar el parametro " +MeLanbide06Constantes.EJERCICIO_VARIABLE+ " desde el properties.");
                } 
            }

            //Asunto
            if(parametros.get("asunto") != null){
                if(ASUNTO_VARIABLE!=null&& ASUNTO_VARIABLE!=""){
                    contenidoVariable=contenidoVariable.replaceAll(ASUNTO_VARIABLE,parametros.get("asunto"));
                    asuntoVariable=asuntoVariable.replaceAll(ASUNTO_VARIABLE,parametros.get("asunto"));
                }else{
                    log.info("No se ha podido recuperar el parametro " +MeLanbide06Constantes.ASUNTO_VARIABLE+ " desde el properties.");
                } 
            }

            //Unidad orgánica
            if(parametros.get("uor") != null){
                if(UOR_VARIABLE!=null&& UOR_VARIABLE!=""){
                    contenidoVariable=contenidoVariable.replaceAll(UOR_VARIABLE,parametros.get("uor"));
                    asuntoVariable=asuntoVariable.replaceAll(UOR_VARIABLE,parametros.get("uor"));
                }else{
                    log.info("No se ha podido recuperar el parametro " +MeLanbide06Constantes.UOR_VARIABLE+ " desde el properties.");
                }
            }

            //Fecha de presentación
            if(parametros.get("fecha_presentacion") != null){
                if(FECHA_PRESENTACION_VARIABLE!=null&& FECHA_PRESENTACION_VARIABLE!=""){
                    contenidoVariable=contenidoVariable.replaceAll(FECHA_PRESENTACION_VARIABLE,parametros.get("fecha_presentacion"));
                    asuntoVariable=asuntoVariable.replaceAll(FECHA_PRESENTACION_VARIABLE,parametros.get("fecha_presentacion"));
                }else{
                    log.info("No se ha podido recuperar el parametro " +MeLanbide06Constantes.FECHA_PRESENTACION_VARIABLE+ " desde el properties.");
                }
            }

            //Número de anotación
            if(parametros.get("num_anotacion") != null){
                if(NUMERO_ANOTACION_VARIABLE!=null&& NUMERO_ANOTACION_VARIABLE!=""){
                    contenidoVariable=contenidoVariable.replaceAll(NUMERO_ANOTACION_VARIABLE,parametros.get("num_anotacion"));
                    asuntoVariable=asuntoVariable.replaceAll(NUMERO_ANOTACION_VARIABLE,parametros.get("num_anotacion"));
                }else{
                    log.info("No se ha podido recuperar el parametro " +MeLanbide06Constantes.NUMERO_ANOTACION_VARIABLE+ " desde el properties.");
                }
            }

            //Extracto
            if(parametros.get("extracto") != null){
                if(EXTRACTO_VARIABLE!=null&& EXTRACTO_VARIABLE!=""){
                    contenidoVariable=contenidoVariable.replaceAll(EXTRACTO_VARIABLE,parametros.get("extracto"));
                    asuntoVariable=asuntoVariable.replaceAll(EXTRACTO_VARIABLE,parametros.get("extracto"));
                }else{
                    log.info("No se ha podido recuperar el parametro " +MeLanbide06Constantes.EXTRACTO_VARIABLE+ " desde el properties.");
                }
            }
                       
            //Número de expediente
            if(parametros.get("numeroExpediente") != null){
                if(NUMERO_EXPEDIENTE_VARIABLE!=null&& NUMERO_EXPEDIENTE_VARIABLE!=""){
                    contenidoVariable=contenidoVariable.replaceAll(NUMERO_EXPEDIENTE_VARIABLE,parametros.get("numeroExpediente"));
                    asuntoVariable=asuntoVariable.replaceAll(NUMERO_EXPEDIENTE_VARIABLE,parametros.get("numeroExpediente"));
                }else{
                    log.info("No se ha podido recuperar el parametro " +MeLanbide06Constantes.NUMERO_EXPEDIENTE_VARIABLE+ " desde el properties.");
                }
            }
            
            //Nombre del documento
            if(parametros.get("nombreDocumento") != null){
                if(NOMBRE_DOCUMENTO_VARIABLE!=null&& NOMBRE_DOCUMENTO_VARIABLE!=""){
                    contenidoVariable=contenidoVariable.replaceAll(NOMBRE_DOCUMENTO_VARIABLE,parametros.get("nombreDocumento"));
                    contenidoVariable=contenidoVariable.replaceAll(NOMBRE_DOCUMENTO_VARIABLE,parametros.get("nombreDocumento"));
                    asuntoVariable=asuntoVariable.replaceAll(NOMBRE_DOCUMENTO_VARIABLE,parametros.get("nombreDocumento"));
                }else{
                    log.info("No se ha podido recuperar el parametro " +MeLanbide06Constantes.NOMBRE_DOCUMENTO_VARIABLE+ " desde el properties.");
                }
            }
        }
        log.info("asuntoVariable : " + asuntoVariable);
        gestionAvisos.setAsunto(asuntoVariable);
        log.info("contenidoVariable : " + contenidoVariable);
        gestionAvisos.setContenido(contenidoVariable);
       
       return gestionAvisos; 
    }
    
    public boolean getConfiguracionActiva06(){
        boolean respuesta = false;
        try{
          ResourceBundle config = ResourceBundle.getBundle("mail06");
          String valor = config.getString("configuracion.gestionAvisos.melanbide06");   
          respuesta = (valor!=null && !valor.isEmpty() && valor.equals("1"));
        }catch(Exception e){
           log.error("No se ha podido rescatar la configuración activa", e); 
        }     
        return respuesta;
    }
    
     public String getAsuntoDocumento06(){
        String valor = "";
         try{
          ResourceBundle config = ResourceBundle.getBundle("mail06");
          valor = config.getString("asuntoMail");   
        }catch(Exception e){
           log.error("No se ha podido rescatar la configuración activa", e); 
        }     
        return valor;
    }

    public boolean existeConfiguracionEventoEmail(AdaptadorSQLBD adaptador, String codEvento, String codigoBusqueda, String opcion){
        log.info("codEvento: "+codEvento+" codigoBusqueda :" + codigoBusqueda +" opcion : " + opcion);
        GestionAvisosVO respuesta = null;
        try{
            respuesta = GestionAvisosManager.getInstance().recuperarConfiguracionEmail(adaptador,codEvento,codigoBusqueda,opcion);
        }catch(Exception e){
            log.error("No se ha podido determinar si existeConfiguracionEventoEmail", e);
        }
        return respuesta!=null && respuesta.getId()>0;
    }
    
}
