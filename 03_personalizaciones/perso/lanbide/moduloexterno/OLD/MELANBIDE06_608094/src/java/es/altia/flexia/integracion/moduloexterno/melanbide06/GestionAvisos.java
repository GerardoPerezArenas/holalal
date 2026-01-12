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
import es.altia.flexia.integracion.moduloexterno.melanbide06.vo.SelectItem;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.*;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.technical.PortableContext;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.melanbide06.util.MailHelper;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.logging.Level;
import javax.sql.DataSource;
import org.apache.log4j.Logger;

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
    private static Logger log = Logger.getLogger(GestionAvisos.class);
    
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
    
    public String envioCorreoGenerico(int codOrganizacion, String codEvento, String codAsunto, Map<String,String> parametros) throws SQLException {
        String resultado = TODO_CORRECTO;
        GestionAvisosVO gestionAvisosVO = new GestionAvisosVO();
        GestionAvisosVO gestionAvisosVariables = new GestionAvisosVO();
        AdaptadorSQLBD adaptador = getAdaptSQLBD(String.valueOf(codOrganizacion));
        GestionAvisosManager gestionAvisosmanager = new GestionAvisosManager();
        gestionAvisosVO = gestionAvisosmanager.recuperarConfiguracionEmail(adaptador, codEvento, codAsunto);
        
        if(gestionAvisosVO != null){
            gestionAvisosVariables = sustituirVariables(gestionAvisosVO,parametros);
            if(log.isDebugEnabled()) log.debug("Enviamos el correo");
            MailHelper mailHelper = new MailHelper();
            try{

            if(gestionAvisosVO.getConfiguracion_activa().equals("1")){
                String asuntoEncode = new String(gestionAvisosVariables.getAsunto().getBytes("iso-8859-1"), "utf8");
                String contenidoEncode = new String(gestionAvisosVariables.getContenido().getBytes("iso-8859-1"), "utf8");
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
                if(st!=null) st.close();
                if(rs!=null) rs.close();
                if(conGenerico!=null && !conGenerico.isClosed())
                conGenerico.close();
            }// finally
            if(log.isDebugEnabled()) log.debug("getConnection() : END");
         }// synchronized
        return adapt;
     }//getConnection
    
    
    public GestionAvisosVO sustituirVariables(GestionAvisosVO gestionAvisos, Map<String,String> parametros){
        String NUMERO_EXPEDIENTE_VARIABLE = MeLanbide06ConfigurationParameter.getParameter(MeLanbide06Constantes.NUMERO_EXPEDIENTE_VARIABLE, MeLanbide06Constantes.FICHERO_CONFIGURACION);
        String NOMBRE_DOCUMENTO_VARIABLE = MeLanbide06ConfigurationParameter.getParameter(MeLanbide06Constantes.NOMBRE_DOCUMENTO_VARIABLE, MeLanbide06Constantes.FICHERO_CONFIGURACION);
        String INTERESADO_VARIABLE = MeLanbide06ConfigurationParameter.getParameter(MeLanbide06Constantes.INTERESADO_VARIABLE, MeLanbide06Constantes.FICHERO_CONFIGURACION);
        String USUARIO_VARIABLE = MeLanbide06ConfigurationParameter.getParameter(MeLanbide06Constantes.USUARIO_VARIABLE, MeLanbide06Constantes.FICHERO_CONFIGURACION);
        String DOCUMENTO_VARIABLE = MeLanbide06ConfigurationParameter.getParameter(MeLanbide06Constantes.DOCUMENTO_VARIABLE, MeLanbide06Constantes.FICHERO_CONFIGURACION);
        String TRAMITE_VARIABLE = MeLanbide06ConfigurationParameter.getParameter(MeLanbide06Constantes.TRAMITE_VARIABLE, MeLanbide06Constantes.FICHERO_CONFIGURACION);
        String EXPEDIENTE_VARIABLE = MeLanbide06ConfigurationParameter.getParameter(MeLanbide06Constantes.EXPEDIENTE_VARIABLE, MeLanbide06Constantes.FICHERO_CONFIGURACION);
        String UNIDAD_TRAMITADORA_VARIABLE = MeLanbide06ConfigurationParameter.getParameter(MeLanbide06Constantes.UNIDAD_TRAMITADORA_VARIABLE, MeLanbide06Constantes.FICHERO_CONFIGURACION);
        String PROCEDIMIENTO_VARIABLE = MeLanbide06ConfigurationParameter.getParameter(MeLanbide06Constantes.PROCEDIMIENTO_VARIABLE, MeLanbide06Constantes.FICHERO_CONFIGURACION);
        String EJERCICIO_VARIABLE = MeLanbide06ConfigurationParameter.getParameter(MeLanbide06Constantes.EJERCICIO_VARIABLE, MeLanbide06Constantes.FICHERO_CONFIGURACION);
        String MUNICIPIO_VARIABLE = MeLanbide06ConfigurationParameter.getParameter(MeLanbide06Constantes.MUNICIPIO_VARIABLE, MeLanbide06Constantes.FICHERO_CONFIGURACION);
        String OCURRENCIA_VARIABLE = MeLanbide06ConfigurationParameter.getParameter(MeLanbide06Constantes.OCURRENCIA_VARIABLE, MeLanbide06Constantes.FICHERO_CONFIGURACION);
        String ASUNTO_VARIABLE = MeLanbide06ConfigurationParameter.getParameter(MeLanbide06Constantes.ASUNTO_VARIABLE, MeLanbide06Constantes.FICHERO_CONFIGURACION);
        String CARGO_VARIABLE = MeLanbide06ConfigurationParameter.getParameter(MeLanbide06Constantes.CARGO_VARIABLE, MeLanbide06Constantes.FICHERO_CONFIGURACION);
        String UOR_VARIABLE = MeLanbide06ConfigurationParameter.getParameter(MeLanbide06Constantes.UOR_VARIABLE, MeLanbide06Constantes.FICHERO_CONFIGURACION);
        String PLAZO_VARIABLE = MeLanbide06ConfigurationParameter.getParameter(MeLanbide06Constantes.PLAZO_VARIABLE, MeLanbide06Constantes.FICHERO_CONFIGURACION);
        String FECHA_VARIABLE = MeLanbide06ConfigurationParameter.getParameter(MeLanbide06Constantes.FECHA_VARIABLE, MeLanbide06Constantes.FICHERO_CONFIGURACION);
        String FECHA_ANOTACION_VARIABLE = MeLanbide06ConfigurationParameter.getParameter(MeLanbide06Constantes.FECHA_ANOTACION_VARIABLE, MeLanbide06Constantes.FICHERO_CONFIGURACION);
        String NUMERO_ANOTACION_VARIABLE = MeLanbide06ConfigurationParameter.getParameter(MeLanbide06Constantes.NUMERO_ANOTACION_VARIABLE, MeLanbide06Constantes.FICHERO_CONFIGURACION);
        String FECHA_RECHAZO_VARIABLE = MeLanbide06ConfigurationParameter.getParameter(MeLanbide06Constantes.FECHA_RECHAZO_VARIABLE, MeLanbide06Constantes.FICHERO_CONFIGURACION);
        String HORA_VARIABLE = MeLanbide06ConfigurationParameter.getParameter(MeLanbide06Constantes.HORA_VARIABLE, MeLanbide06Constantes.FICHERO_CONFIGURACION);
        String TIPO_REGISTRO_VARIABLE = MeLanbide06ConfigurationParameter.getParameter(MeLanbide06Constantes.TIPO_REGISTRO_VARIABLE, MeLanbide06Constantes.FICHERO_CONFIGURACION);
        String EXTRACTO_VARIABLE = MeLanbide06ConfigurationParameter.getParameter(MeLanbide06Constantes.EXTRACTO_VARIABLE, MeLanbide06Constantes.FICHERO_CONFIGURACION);
        String contenidoVariable = gestionAvisos.getContenido();
        String asuntoVariable = gestionAvisos.getAsunto();
        
        if(parametros != null){
            //Número de Expediente
            if(parametros.get("numExpediente") != null){
                if(NUMERO_EXPEDIENTE_VARIABLE!=null&& NUMERO_EXPEDIENTE_VARIABLE!=""){
                    contenidoVariable=contenidoVariable.replaceAll(NUMERO_EXPEDIENTE_VARIABLE,parametros.get("numExpediente"));
                    asuntoVariable=asuntoVariable.replaceAll(NUMERO_EXPEDIENTE_VARIABLE,parametros.get("numExpediente"));
                }else{
                    log.debug("No se ha podido recuperar el parametro " +MeLanbide06Constantes.NUMERO_EXPEDIENTE_VARIABLE+ " desde el properties.");
                } 
            }

            //Nombre del documento
            if(parametros.get("nombreDocumento") != null){
                if(NOMBRE_DOCUMENTO_VARIABLE!=null&& NOMBRE_DOCUMENTO_VARIABLE!=""){
                    contenidoVariable=contenidoVariable.replaceAll(NOMBRE_DOCUMENTO_VARIABLE,parametros.get("nombreDocumento"));
                    asuntoVariable=asuntoVariable.replaceAll(NOMBRE_DOCUMENTO_VARIABLE,parametros.get("nombreDocumento"));
                }else{
                    log.debug("No se ha podido recuperar el parametro " +MeLanbide06Constantes.NOMBRE_DOCUMENTO_VARIABLE+ " desde el properties.");
                } 
            }

            //Interesado
            if(parametros.get("interesado") != null){
                if(INTERESADO_VARIABLE!=null&& INTERESADO_VARIABLE!=""){
                    contenidoVariable=contenidoVariable.replaceAll(INTERESADO_VARIABLE,parametros.get("interesado"));
                    asuntoVariable=asuntoVariable.replaceAll(INTERESADO_VARIABLE,parametros.get("interesado"));
                }else{
                    log.debug("No se ha podido recuperar el parametro " +MeLanbide06Constantes.INTERESADO_VARIABLE+ " desde el properties.");
                } 
            }

            //Usuario
            if(parametros.get("usuario") != null){
                if(USUARIO_VARIABLE!=null&& USUARIO_VARIABLE!=""){
                    contenidoVariable=contenidoVariable.replaceAll(USUARIO_VARIABLE,parametros.get("usuario"));
                    asuntoVariable=asuntoVariable.replaceAll(USUARIO_VARIABLE,parametros.get("usuario"));
                }else{
                    log.debug("No se ha podido recuperar el parametro " +MeLanbide06Constantes.USUARIO_VARIABLE+ " desde el properties.");
                } 
            }

            //Documento
            if(parametros.get("documento") != null){
                if(DOCUMENTO_VARIABLE!=null&& DOCUMENTO_VARIABLE!=""){
                    contenidoVariable=contenidoVariable.replaceAll(DOCUMENTO_VARIABLE,parametros.get("documento"));
                    asuntoVariable=asuntoVariable.replaceAll(DOCUMENTO_VARIABLE,parametros.get("documento"));
                }else{
                    log.debug("No se ha podido recuperar el parametro " +MeLanbide06Constantes.DOCUMENTO_VARIABLE+ " desde el properties.");
                } 
            }

            //Trámite
            if(parametros.get("tramite") != null){
                if(TRAMITE_VARIABLE!=null&& TRAMITE_VARIABLE!=""){
                    contenidoVariable=contenidoVariable.replaceAll(TRAMITE_VARIABLE,parametros.get("tramite"));
                    asuntoVariable=asuntoVariable.replaceAll(TRAMITE_VARIABLE,parametros.get("tramite"));
                }else{
                    log.debug("No se ha podido recuperar el parametro " +MeLanbide06Constantes.TRAMITE_VARIABLE+ " desde el properties.");
                } 
            }

            //Expediente
            if(parametros.get("expediente") != null){
                if(EXPEDIENTE_VARIABLE!=null&& EXPEDIENTE_VARIABLE!=""){
                    contenidoVariable=contenidoVariable.replaceAll(EXPEDIENTE_VARIABLE,parametros.get("expediente"));
                    asuntoVariable=asuntoVariable.replaceAll(EXPEDIENTE_VARIABLE,parametros.get("expediente"));
                }else{
                    log.debug("No se ha podido recuperar el parametro " +MeLanbide06Constantes.EXPEDIENTE_VARIABLE+ " desde el properties.");
                } 
            }

            //Unidad tramitadora
            if(parametros.get("unidadTramitadora") != null){
                if(UNIDAD_TRAMITADORA_VARIABLE!=null&& UNIDAD_TRAMITADORA_VARIABLE!=""){
                    contenidoVariable=contenidoVariable.replaceAll(UNIDAD_TRAMITADORA_VARIABLE,parametros.get("unidadTramitadora"));
                    asuntoVariable=asuntoVariable.replaceAll(UNIDAD_TRAMITADORA_VARIABLE,parametros.get("unidadTramitadora"));
                }else{
                    log.debug("No se ha podido recuperar el parametro " +MeLanbide06Constantes.UNIDAD_TRAMITADORA_VARIABLE+ " desde el properties.");
                } 
            }

            //Procedimiento
            if(parametros.get("procedimiento") != null){
                if(PROCEDIMIENTO_VARIABLE!=null&& PROCEDIMIENTO_VARIABLE!=""){
                    contenidoVariable=contenidoVariable.replaceAll(PROCEDIMIENTO_VARIABLE,parametros.get("procedimiento"));
                    asuntoVariable=asuntoVariable.replaceAll(PROCEDIMIENTO_VARIABLE,parametros.get("procedimiento"));
                }else{
                    log.debug("No se ha podido recuperar el parametro " +MeLanbide06Constantes.PROCEDIMIENTO_VARIABLE+ " desde el properties.");
                } 
            }

            //Ejercicio
            if(parametros.get("ejercicio") != null){
                if(EJERCICIO_VARIABLE!=null&& EJERCICIO_VARIABLE!=""){
                    contenidoVariable=contenidoVariable.replaceAll(EJERCICIO_VARIABLE,parametros.get("ejercicio"));
                    asuntoVariable=asuntoVariable.replaceAll(EJERCICIO_VARIABLE,parametros.get("ejercicio"));
                }else{
                    log.debug("No se ha podido recuperar el parametro " +MeLanbide06Constantes.EJERCICIO_VARIABLE+ " desde el properties.");
                } 
            }

            //Municipio
            if(parametros.get("municipio") != null){
                if(MUNICIPIO_VARIABLE!=null&& MUNICIPIO_VARIABLE!=""){
                    contenidoVariable=contenidoVariable.replaceAll(MUNICIPIO_VARIABLE,parametros.get("municipio"));
                    asuntoVariable=asuntoVariable.replaceAll(MUNICIPIO_VARIABLE,parametros.get("municipio"));
                }else{
                    log.debug("No se ha podido recuperar el parametro " +MeLanbide06Constantes.MUNICIPIO_VARIABLE+ " desde el properties.");
                } 
            }

            //Ocurrencia
            if(parametros.get("ocurrencia") != null){
                if(OCURRENCIA_VARIABLE!=null&& OCURRENCIA_VARIABLE!=""){
                    contenidoVariable=contenidoVariable.replaceAll(OCURRENCIA_VARIABLE,parametros.get("ocurrencia"));
                    asuntoVariable=asuntoVariable.replaceAll(OCURRENCIA_VARIABLE,parametros.get("ocurrencia"));
                }else{
                    log.debug("No se ha podido recuperar el parametro " +MeLanbide06Constantes.OCURRENCIA_VARIABLE+ " desde el properties.");
                } 
            }

            //Asunto
            if(parametros.get("asunto") != null){
                if(ASUNTO_VARIABLE!=null&& ASUNTO_VARIABLE!=""){
                    contenidoVariable=contenidoVariable.replaceAll(ASUNTO_VARIABLE,parametros.get("asunto"));
                    asuntoVariable=asuntoVariable.replaceAll(ASUNTO_VARIABLE,parametros.get("asunto"));
                }else{
                    log.debug("No se ha podido recuperar el parametro " +MeLanbide06Constantes.ASUNTO_VARIABLE+ " desde el properties.");
                } 
            }

            //Cargo
            if(parametros.get("cargo") != null){
                if(CARGO_VARIABLE!=null&& CARGO_VARIABLE!=""){
                    contenidoVariable=contenidoVariable.replaceAll(CARGO_VARIABLE,parametros.get("cargo"));
                    asuntoVariable=asuntoVariable.replaceAll(CARGO_VARIABLE,parametros.get("cargo"));
                }else{
                    log.debug("No se ha podido recuperar el parametro " +MeLanbide06Constantes.CARGO_VARIABLE+ " desde el properties.");
                }
            }

            //Unidad orgánica
            if(parametros.get("uor") != null){
                if(UOR_VARIABLE!=null&& UOR_VARIABLE!=""){
                    contenidoVariable=contenidoVariable.replaceAll(UOR_VARIABLE,parametros.get("uor"));
                    asuntoVariable=asuntoVariable.replaceAll(UOR_VARIABLE,parametros.get("uor"));
                }else{
                    log.debug("No se ha podido recuperar el parametro " +MeLanbide06Constantes.UOR_VARIABLE+ " desde el properties.");
                }
            }

            //Plazo
            if(parametros.get("plazo") != null){
                if(PLAZO_VARIABLE!=null&& PLAZO_VARIABLE!=""){
                    contenidoVariable=contenidoVariable.replaceAll(PLAZO_VARIABLE,parametros.get("plazo"));
                    asuntoVariable=asuntoVariable.replaceAll(PLAZO_VARIABLE,parametros.get("plazo"));
                }else{
                    log.debug("No se ha podido recuperar el parametro " +MeLanbide06Constantes.PLAZO_VARIABLE+ " desde el properties.");
                }
            }

            //Fecha
            if(parametros.get("fecha") != null){
                if(FECHA_VARIABLE!=null&& FECHA_VARIABLE!=""){
                    contenidoVariable=contenidoVariable.replaceAll(FECHA_VARIABLE,parametros.get("fecha"));
                    asuntoVariable=asuntoVariable.replaceAll(FECHA_VARIABLE,parametros.get("fecha"));
                }else{
                    log.debug("No se ha podido recuperar el parametro " +MeLanbide06Constantes.FECHA_VARIABLE+ " desde el properties.");
                }
            }

            //Fecha anotación
            if(parametros.get("fecha_anotacion") != null){
                if(FECHA_ANOTACION_VARIABLE!=null&& FECHA_ANOTACION_VARIABLE!=""){
                    contenidoVariable=contenidoVariable.replaceAll(FECHA_ANOTACION_VARIABLE,parametros.get("fecha_anotacion"));
                    asuntoVariable=asuntoVariable.replaceAll(FECHA_ANOTACION_VARIABLE,parametros.get("fecha_anotacion"));
                }else{
                    log.debug("No se ha podido recuperar el parametro " +MeLanbide06Constantes.FECHA_ANOTACION_VARIABLE+ " desde el properties.");
                }
            }

            //Número de anotación
            if(parametros.get("num_anotacion") != null){
                if(NUMERO_ANOTACION_VARIABLE!=null&& NUMERO_ANOTACION_VARIABLE!=""){
                    contenidoVariable=contenidoVariable.replaceAll(NUMERO_ANOTACION_VARIABLE,parametros.get("num_anotacion"));
                    asuntoVariable=asuntoVariable.replaceAll(NUMERO_ANOTACION_VARIABLE,parametros.get("num_anotacion"));
                }else{
                    log.debug("No se ha podido recuperar el parametro " +MeLanbide06Constantes.NUMERO_ANOTACION_VARIABLE+ " desde el properties.");
                }
            }

            //Fecha de rechazo
            if(parametros.get("fecha_rechazo") != null){
                if(FECHA_RECHAZO_VARIABLE!=null&& FECHA_RECHAZO_VARIABLE!=""){
                    contenidoVariable=contenidoVariable.replaceAll(FECHA_RECHAZO_VARIABLE,parametros.get("fecha_rechazo"));
                    asuntoVariable=asuntoVariable.replaceAll(FECHA_RECHAZO_VARIABLE,parametros.get("fecha_rechazo"));
                }else{
                    log.debug("No se ha podido recuperar el parametro " +MeLanbide06Constantes.FECHA_RECHAZO_VARIABLE+ " desde el properties.");
                }
            }

            //Hora
            if(parametros.get("hora") != null){
                if(HORA_VARIABLE!=null&& HORA_VARIABLE!=""){
                    contenidoVariable=contenidoVariable.replaceAll(HORA_VARIABLE,parametros.get("hora"));
                    asuntoVariable=asuntoVariable.replaceAll(HORA_VARIABLE,parametros.get("hora"));
                }else{
                    log.debug("No se ha podido recuperar el parametro " +MeLanbide06Constantes.HORA_VARIABLE+ " desde el properties.");
                }
            }

            //Tipo de registro
            if(parametros.get("tipo_registro") != null){
                if(TIPO_REGISTRO_VARIABLE!=null&& TIPO_REGISTRO_VARIABLE!=""){
                    contenidoVariable=contenidoVariable.replaceAll(TIPO_REGISTRO_VARIABLE,parametros.get("tipo_registro"));
                    asuntoVariable=asuntoVariable.replaceAll(TIPO_REGISTRO_VARIABLE,parametros.get("tipo_registro"));
                }else{
                    log.debug("No se ha podido recuperar el parametro " +MeLanbide06Constantes.TIPO_REGISTRO_VARIABLE+ " desde el properties.");
                }
            }

            //Extracto
            if(parametros.get("extracto") != null){
                if(EXTRACTO_VARIABLE!=null&& EXTRACTO_VARIABLE!=""){
                    contenidoVariable=contenidoVariable.replaceAll(EXTRACTO_VARIABLE,parametros.get("extracto"));
                    asuntoVariable=asuntoVariable.replaceAll(EXTRACTO_VARIABLE,parametros.get("extracto"));
                }else{
                    log.debug("No se ha podido recuperar el parametro " +MeLanbide06Constantes.TIPO_REGISTRO_VARIABLE+ " desde el properties.");
                }
            }
        }
        gestionAvisos.setAsunto(asuntoVariable);
        gestionAvisos.setContenido(contenidoVariable);
       
       return gestionAvisos; 
    }
    
}
