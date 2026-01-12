/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.flexia.integracion.moduloexterno.pluginnotificacionplatea;

import com.lanbide.lan6.errores.bean.ErrorBean;
import es.altia.agora.business.util.GlobalNames;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.flexia.notificacion.vo.*;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;


import java.sql.*;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.ResourceBundle;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PluginNotificacionPlateaNotificacionDAO {

    private static PluginNotificacionPlateaNotificacionDAO instance =	null;
    protected   static Config m_CommonProperties; // Para el fichero de contantes
    protected static Config m_ConfigTechnical; //	Para el fichero de configuracion técnico
    protected static Config m_ConfigError; // Para los mensajes de error localizados
    protected static Log m_Log =
            LogFactory.getLog(PluginNotificacionPlateaNotificacionDAO.class.getName());




     protected PluginNotificacionPlateaNotificacionDAO() {
                m_CommonProperties = ConfigServiceHelper.getConfig("common");
		// Queremos usar el	fichero de configuración technical
		m_ConfigTechnical =	ConfigServiceHelper.getConfig("techserver");
		// Queremos tener acceso a los mensajes de error localizados
		m_ConfigError	= ConfigServiceHelper.getConfig("error");


	}

    
     
     public static PluginNotificacionPlateaNotificacionDAO getInstance() {
        // Si no hay una instancia de esta clase tenemos que crear una
        synchronized (PluginNotificacionPlateaNotificacionDAO.class) {
            if (instance == null) {
                instance = new PluginNotificacionPlateaNotificacionDAO();
            }

        }
        return instance;
    }
     
     
     /**
  * Actualiza la información de una notificación
  * @param notificacion: NotificacionVO
  * @param con: Connection 
  * @return Un boolean
  */
 public boolean updateNotificacion(NotificacionBVO notificacion, String  codNotificacionPlatea, AdaptadorSQLBD obd) throws Exception {

    boolean exito = false;
    Statement st = null;
    String sql = "";
    
     //AdaptadorSQLBD obd = null;
    Connection con = null;
    
    try{
      //obd = new AdaptadorSQLBD(params);
      con = obd.getConnection();
        
      String numeroExpediente=notificacion.getNumExpediente();
      int codNotificacion = notificacion.getCodigoNotificacion();
      m_Log.error("Expediente: " + numeroExpediente);
      m_Log.error("codNotificacion: " + codNotificacion);
//#282252
//      sql = "UPDATE NOTIFICACION SET COD_NOTIFICACION_PLATEA='" + codNotificacionPlatea + "' " + 
//             "WHERE CODIGO_NOTIFICACION=" + codNotificacion + " AND NUM_EXPEDIENTE='" + numeroExpediente + "'";
      sql = "UPDATE NOTIFICACION SET EN_PROCESO = 'N', COD_NOTIFICACION_PLATEA='" + codNotificacionPlatea + "', FECHA_ENVIO = SYSDATE, FIRMADA = 'E'" +
            " WHERE CODIGO_NOTIFICACION=" + codNotificacion + " AND NUM_EXPEDIENTE='" + numeroExpediente + "'";

      if(m_Log.isDebugEnabled()) m_Log.error(sql);
      st = con.createStatement();
      int resultado = st.executeUpdate(sql);
      m_Log.error("PluginNotificacionPlateaNotificacionDAO.updateNotificacion notificación actualizadas: " + resultado);
      if(resultado>=1) exito = true;      

    }catch (Exception e){
        e.printStackTrace();
        if(m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + getClass().getName());
        m_Log.error("Excepcion : " + e.toString());
        exito = false;
        throw e;
    }finally{
        try{
            if (st!=null) st.close();    
            if(con != null) con.close();
        }catch(Exception e){
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + getClass().getName());
            
        }
    }

    return exito;
   }
 
 
 
      /**
  * Actualiza la información de una notificación
  * @param notificacion: NotificacionVO
  * @param con: Connection 
  * @return Un boolean
  */
 public boolean updateNotificacionXML(NotificacionBVO notificacion, String objetoNotificacion, AdaptadorSQLBD obd) throws Exception {

    boolean exito = false;
    Statement st = null;
    String sql = "";
    
     //AdaptadorSQLBD obd = null;
    Connection con = null;
    
    try{
      //obd = new AdaptadorSQLBD(params);
      con = obd.getConnection();
        
      String numeroExpediente=notificacion.getNumExpediente();
      int codNotificacion = notificacion.getCodigoNotificacion();
      m_Log.error("Expediente: " + numeroExpediente);
      m_Log.error("codNotificacion: " + codNotificacion);

      sql = "UPDATE NOTIFICACION SET XML_NOTIFICACION = '" + objetoNotificacion + "'" +
            " WHERE CODIGO_NOTIFICACION=" + codNotificacion + " AND NUM_EXPEDIENTE='" + numeroExpediente + "'";

      if(m_Log.isDebugEnabled()) m_Log.error(sql);
      st = con.createStatement();
      int resultado = st.executeUpdate(sql);
      m_Log.error("PluginNotificacionPlateaNotificacionDAO.updateNotificacionXML notificación xml actualizado: " + resultado);
      if(resultado>=1) exito = true;      

    }catch (Exception e){
        e.printStackTrace();
        if(m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + getClass().getName());
        m_Log.error("Excepcion : " + e.toString());
        exito = false;
        throw e;
    }finally{
        try{
            if (st!=null) st.close();    
            if(con != null) con.close();
        }catch(Exception e){
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + getClass().getName());
            
        }
    }

    return exito;
   }
 
 public String getTipoNotificacion(NotificacionBVO notificacion, Connection con) throws TechnicalException {

        String tipoNotificacion="";
        ResultSet rs = null;
        Statement st = null;
        String parteWhere;


        try{
            String codProcedimiento=notificacion.getCodigoProcedimiento();
            int codMunicipio=notificacion.getCodigoMunicipio();
            int codTramite=notificacion.getCodigoTramite();

             String sql = "SELECT TRA_COD_TIPO_NOTIFICACION FROM E_TRA WHERE TRA_MUN="+codMunicipio+" AND TRA_PRO='"+codProcedimiento+"' AND TRA_COD="+codTramite;


            if(m_Log.isDebugEnabled()) m_Log.error(sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);

            while(rs.next()){
                tipoNotificacion=rs.getString("TRA_COD_TIPO_NOTIFICACION");
            }

        }catch(Exception e){
            e.printStackTrace();
            m_Log.error("Error en getTipoNotificacion: " + e);
        }finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        return tipoNotificacion;
    }
 
 public boolean getTercero(int codNotif, int codTer, Connection con) throws TechnicalException {
        ResultSet rs = null;
        Statement st = null;
        boolean esta = false;

        try{

             String sql = "SELECT * FROM AUTORIZADO_NOTIFICACION WHERE CODIGO_NOTIFICACION="+codNotif+" AND COD_TERCERO="+codTer;


            if(m_Log.isDebugEnabled()) m_Log.error(sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);

            if(rs.next()){
                esta = true;
            }

        }catch(Exception e){
            e.printStackTrace();
            m_Log.error("Error en getTipoNotificacion: " + e);
        }finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        return esta;
    }
 
 
 
 //Almacena la firma de una notificación en base de datos
  public NotificacionVO getNotificacion(NotificacionVO notificacion, Connection con) throws TechnicalException {

        NotificacionVO notificacionVORetorno = new NotificacionVO();
        ResultSet rs = null;
        Statement st = null;
        String parteWhere;

        try{
            
            String numeroExpediente=notificacion.getNumExpediente();
            String codProcedimiento=notificacion.getCodigoProcedimiento();
            int ejercicio=notificacion.getEjercicio();
            int codMunicipio=notificacion.getCodigoMunicipio();
            int codTramite=notificacion.getCodigoTramite();
            int ocuTramite=notificacion.getOcurrenciaTramite();
           
            parteWhere=" NUM_EXPEDIENTE='"+numeroExpediente+"' AND COD_PROCEDIMIENTO='"+codProcedimiento+"' AND EJERCICIO="+ejercicio+" AND "+
                        "COD_MUNICIPIO="+codMunicipio+" AND COD_TRAMITE="+codTramite+" AND OCU_TRAMITE="+ocuTramite;
           
             String sql = "SELECT CODIGO_NOTIFICACION,NUM_EXPEDIENTE,COD_PROCEDIMIENTO,EJERCICIO,COD_MUNICIPIO," +
                          "COD_TRAMITE,OCU_TRAMITE,ACTO_NOTIFICADO,CADUCIDAD_NOTIFICACION,FIRMA,TEXTO_NOTIFICACION,FIRMADA" +
                          " FROM NOTIFICACION WHERE " + parteWhere+" ORDER BY CODIGO_NOTIFICACION DESC";

            if(m_Log.isDebugEnabled()) m_Log.error(sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);

            if(rs.next()){ //Puede tener varias notificaciones pero vamos a devolver la ultima

                notificacionVORetorno=notificacion;
                notificacionVORetorno.setCodigoNotificacion(rs.getInt("CODIGO_NOTIFICACION"));
                notificacionVORetorno.setNumExpediente(rs.getString("NUM_EXPEDIENTE"));
                notificacionVORetorno.setCodigoProcedimiento(rs.getString("COD_PROCEDIMIENTO"));
                notificacionVORetorno.setEjercicio(rs.getInt("EJERCICIO"));
                notificacionVORetorno.setCodigoMunicipio(rs.getInt("COD_MUNICIPIO"));
                notificacionVORetorno.setCodigoTramite(rs.getInt("COD_TRAMITE"));
                notificacionVORetorno.setOcurrenciaTramite(rs.getInt("OCU_TRAMITE"));
                notificacionVORetorno.setActoNotificado(rs.getString("ACTO_NOTIFICADO"));
                notificacionVORetorno.setCaducidadNotificacion(rs.getInt("CADUCIDAD_NOTIFICACION"));
                //notificacionVORetorno.setFirma(rs.getString("FIRMA"));

               
                   byte[] contenido = null;
                    // Se lee el contenido binario del documento
                    java.io.InputStream stream = rs.getBinaryStream("FIRMA");
                    java.io.ByteArrayOutputStream ot = new java.io.ByteArrayOutputStream();
                    int c;
                    if (stream != null) {
                         while ((c = stream.read()) != -1) {
                             ot.write(c);
                         }
                    }
                    ot.flush();
                    contenido = ot.toByteArray();
                    ot.close();

                    String value = new String(contenido);

                    notificacionVORetorno.setFirma(value);
                   
                    String textoNotificacion = rs.getString("TEXTO_NOTIFICACION");
                    
                    // NECESITAMOS COMENTAR ESTA LLAMADA PARA QUE FUNCIONE LA CODIFICACIÓN CUANDO LLAMAMOS A PLATEA
                    //textoNotificacion = StringEscapeUtils.escapeJavaScript(textoNotificacion);
                            
                    notificacionVORetorno.setTextoNotificacion(textoNotificacion);
                //notificacionVORetorno.setTextoNotificacion(rs.getString("TEXTO_NOTIFICACION"));
                notificacionVORetorno.setEstadoNotificacion(rs.getString("FIRMADA"));

            }

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();                
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return notificacionVORetorno;
    }
     
    public boolean enviarNotificacion(int codigo,String[] params) throws TechnicalException {

      AdaptadorSQLBD obd = null;
      Connection con = null;
      Statement st = null;
      String sql = "";

      try{

      obd = new AdaptadorSQLBD(params);
      con = obd.getConnection();
      obd.inicioTransaccion(con);
      st = con.createStatement();



      sql = "UPDATE NOTIFICACION SET FECHA_SOL_ENVIO = SYSDATE" +
          " WHERE CODIGO_NOTIFICACION = " + codigo;
      if(m_Log.isDebugEnabled()) m_Log.error(sql);

      st.executeUpdate(sql);
      obd.finTransaccion(con);



      }catch (Exception e){
      try{
            obd.rollBack(con);
      }catch(Exception ex){
            ex.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + getClass().getName());

      }
      e.printStackTrace();
      if(m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + getClass().getName());
      return false;

    }finally{
      try{
            if (st!=null) st.close();
            obd.devolverConexion(con);
        }catch(Exception bde) {
            bde.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.error("Excepcion capturada en: " + getClass().getName());
            return false;
        }
    }

    return true;
  }
    
    public ArrayList<NotificacionBVO> recogerNotificacionesPendientes(AdaptadorSQLBD adapt) throws Exception  {
        Connection con = null;
        //AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
        con = adapt.getConnection();
        NotificacionBVO notificacionVORetorno = new NotificacionBVO();
        ArrayList<NotificacionBVO> notificaciones = new ArrayList<NotificacionBVO>();
        ResultSet rs = null;
        Statement st = null;
        String parteWhere;

        try{
           
            String sql = "SELECT CODIGO_NOTIFICACION,NUM_EXPEDIENTE,COD_PROCEDIMIENTO,EJERCICIO,COD_MUNICIPIO," 
                    + " COD_TRAMITE,OCU_TRAMITE,ACTO_NOTIFICADO,CADUCIDAD_NOTIFICACION,FIRMA, I.TXT_VALOR AS IDIOMA, "
                    + " TEXTO_NOTIFICACION,FIRMADA, NUM_INTENTOS, S.TXT_VALOR AS SMS, E.TXT_VALOR AS EMAIL " 
                    + " ,ST.TXT_VALOR AS SMSTIT, ET.TXT_VALOR AS EMAILTIT "
                    + " FROM NOTIFICACION "
                    + " LEFT JOIN E_TXT S ON NUM_EXPEDIENTE = S.TXT_NUM AND S.TXT_COD = 'AVISOSMS' " 
                    + " LEFT JOIN E_TXT E ON NUM_EXPEDIENTE = E.TXT_NUM AND E.TXT_COD = 'AVISOEMAIL' " 
                    + " LEFT JOIN E_TXT ST ON NUM_EXPEDIENTE = ST.TXT_NUM AND ST.TXT_COD = 'AVISOSMSTIT' " 
                    + " LEFT JOIN E_TXT ET ON NUM_EXPEDIENTE = ET.TXT_NUM AND ET.TXT_COD = 'AVISOEMAILTIT' " 
                    + " LEFT JOIN E_TXT I ON NUM_EXPEDIENTE = I.TXT_NUM AND I.TXT_COD = 'IDIOMAAVISOS' "
                    + " INNER JOIN E_EXP EX ON NUM_EXPEDIENTE = EX.EXP_NUM AND EX.EXP_EST <> '1' "
                    + " WHERE FECHA_SOL_ENVIO is not null AND "
                    + " (EN_PROCESO IS NULL OR EN_PROCESO <> 'S') AND "
                    + " (NUM_INTENTOS IS NULL OR NUM_INTENTOS < 3) AND "                    
                    + " (COD_NOTIFICACION_PLATEA IS NULL OR COD_NOTIFICACION_PLATEA = '' OR COD_NOTIFICACION_PLATEA = '0') ORDER BY CODIGO_NOTIFICACION DESC";

            if(m_Log.isDebugEnabled()) m_Log.error("Recogiendo notificaciones pendientes: " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);

            while(rs.next()){ //Puede tener varias notificaciones pero vamos a devolver la ultima
                //m_Log.error("codigo notificacion recogida: " + rs.getInt("CODIGO_NOTIFICACION"));
                notificacionVORetorno.setCodigoNotificacion(rs.getInt("CODIGO_NOTIFICACION"));
                notificacionVORetorno.setNumExpediente(rs.getString("NUM_EXPEDIENTE"));
                notificacionVORetorno.setCodigoProcedimiento(rs.getString("COD_PROCEDIMIENTO"));
                notificacionVORetorno.setEjercicio(rs.getInt("EJERCICIO"));
                notificacionVORetorno.setCodigoMunicipio(rs.getInt("COD_MUNICIPIO"));
                notificacionVORetorno.setCodigoTramite(rs.getInt("COD_TRAMITE"));
                notificacionVORetorno.setOcurrenciaTramite(rs.getInt("OCU_TRAMITE"));
                notificacionVORetorno.setActoNotificado(rs.getString("ACTO_NOTIFICADO"));
                notificacionVORetorno.setCaducidadNotificacion(rs.getInt("CADUCIDAD_NOTIFICACION"));
                notificacionVORetorno.setEmails(rs.getString("EMAIL"));
                notificacionVORetorno.setSms(rs.getString("SMS"));
                notificacionVORetorno.setEmailsTitular(rs.getString("EMAILTIT"));
                notificacionVORetorno.setSmsTitular(rs.getString("SMSTIT"));
                notificacionVORetorno.setIdioma(rs.getString("IDIOMA"));
                if(rs.getString("NUM_INTENTOS") != null)
                    notificacionVORetorno.setNumIntentos(rs.getInt("NUM_INTENTOS"));
                else 
                    notificacionVORetorno.setNumIntentos(0);
                //notificacionVORetorno.setFirma(rs.getString("FIRMA"));

               
                byte[] contenido = null;
                 // Se lee el contenido binario del documento
                 java.io.InputStream stream = rs.getBinaryStream("FIRMA");
                 java.io.ByteArrayOutputStream ot = new java.io.ByteArrayOutputStream();
                 int c;
                 if (stream != null) {
                      while ((c = stream.read()) != -1) {
                          ot.write(c);
                      }
                 }
                 ot.flush();
                 contenido = ot.toByteArray();
                 ot.close();

                 String value = new String(contenido);

                 notificacionVORetorno.setFirma(value);

                 String textoNotificacion = rs.getString("TEXTO_NOTIFICACION");

                 // NECESITAMOS COMENTAR ESTA LLAMADA PARA QUE FUNCIONE LA CODIFICACIÓN CUANDO LLAMAMOS A PLATEA
                 //textoNotificacion = StringEscapeUtils.escapeJavaScript(textoNotificacion);

                 notificacionVORetorno.setTextoNotificacion(textoNotificacion);
                //notificacionVORetorno.setTextoNotificacion(rs.getString("TEXTO_NOTIFICACION"));
                notificacionVORetorno.setEstadoNotificacion(rs.getString("FIRMADA"));
                notificaciones.add(notificacionVORetorno);
                
                notificacionVORetorno = new NotificacionBVO();
            }
            
            if (notificaciones.size() > 0){
                m_Log.info("Se actualizaran " + notificaciones.size() + " notificaciobnes");
                actualizarNotificacionesEnTratamiento(notificaciones, con);
            }else{
                m_Log.info("No hay notificaciones a tratar");
            }

        }
        catch(Exception e){
            m_Log.error("Error en recogerNotificacionesPendientes: " , e);
            e.printStackTrace();
            throw e;
            /*m_Log.error("enviamos el error");
            ErrorBean err = new ErrorBean();
            err.setIdError("NOTIFICACIONES_012");
            err.setMensajeError("Error al recoger las notificaciones pendientes");
            err.setSituacion("recogerNotificacionesPendientes");

            PluginNotificacionPlatea.grabarError(err, e.getMessage().toString(), e.toString(),"");*/
        }/*catch(Exception e){
            m_Log.error("Error en recogerNotificacionesPendientes: " + e);
            e.printStackTrace();
            
        }*/
        finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();  
                if(con != null)
                    con.close();              
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return notificaciones;
    }
    
    public boolean documentoNotificado(String numExp, String oid, AdaptadorSQLBD adapt) throws BDException  {
        Connection con = null;
        //AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
        con = adapt.getConnection();
        boolean notificado = false;
        ResultSet rs = null;
        Statement st = null;

        try{
           
            String sql = "SELECT RELDOC_PREPNOTIF " +
                          "FROM MELANBIDE_DOKUSI_RELDOC_TRAMIT "
                    +     "WHERE RELDOC_NUM = '"+numExp+"' AND RELDOC_OID = '"+oid+"'";
                    //+     " AND RELDOC_PREPNOTIF = '1'";

            if(m_Log.isDebugEnabled()) m_Log.error("Comprobando si el documento ha sido notificado" + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);

            if(rs.next()){ 
                String notif = rs.getString("RELDOC_PREPNOTIF");
                m_Log.info("RELDOC_PREPNOTIF: " + notif);
                if(("1").equals(notif)){
                    notificado = true;
                    m_Log.error("Documento notificado");
                }
                m_Log.error("Documento notificado: " + notificado);
            }

        }catch(Exception e){
            m_Log.error("Error en documentoNotificado: " + e);
            e.printStackTrace();
        }finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();  
                if(con != null)
                    con.close();              
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return notificado;
    }
//#282252    
//    public int actualizarError(int id, int intentos, String error, Connection con) throws Exception
    public int actualizarError(int id, String error, Connection con) throws Exception    
    {
        Statement st = null;
        ResultSet rs = null;
        int result = 0;
        boolean nuevo = true;
        int num = 0;
        try
        {
            String query = null;
            
                query = "SELECT NUM_INTENTOS FROM NOTIFICACION WHERE CODIGO_NOTIFICACION = "+ id;
                st = con.createStatement();
                m_Log.error("sql = " + query);
                rs = st.executeQuery(query);
                if(rs.next()){
                    if(rs.getString("NUM_INTENTOS") != null)
                        num = rs.getInt("NUM_INTENTOS");
                    m_Log.error("intentos = " + num);
                }
    /* #282252
                if(num >= 1) intentos = 3;
                query = "UPDATE NOTIFICACION" //+ ConfigurationParameter.getParameter(ConstantesMeLanbide42.TABLA_CONTADORES, ConstantesMeLanbide42.FICHERO_PROPIEDADES)
                    + " SET NUM_INTENTOS = " + intentos
                    + " WHERE CODIGO_NOTIFICACION =  " + id;
    */
                num = num + 1;
                query = "UPDATE NOTIFICACION" //+ ConfigurationParameter.getParameter(ConstantesMeLanbide42.TABLA_CONTADORES, ConstantesMeLanbide42.FICHERO_PROPIEDADES)
                    + " SET EN_PROCESO = 'N', NUM_INTENTOS = " + num
                    + " WHERE CODIGO_NOTIFICACION =  " + id;
                
                if(m_Log.isDebugEnabled()) 
                    m_Log.error("sql = " + query);
                st = con.createStatement();
                result = st.executeUpdate(query);
        }
        catch(Exception ex)
        {
            //insertaError(con, ex.toString(), id, "actualizarProcesados");
            //log.error("Se ha producido un error grabando el contador ("+tipoContador+")", ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if(m_Log.isDebugEnabled()) 
                    m_Log.error("Procedemos a cerrar el statement y el resultset");
                if(st!=null) 
                    st.close();
                if(rs!=null) rs.close();
            }
            catch(Exception e)
            {
                m_Log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return result;
    }
    
    public int obtieneIntentos(int id, Connection con) throws Exception {
        ResultSet rs = null;
        Statement st = null;
        int intentos = 0;
        try
        {
            String sql = "SELECT NUM_INTENTOS FROM NOTIFICACION WHERE CODIGO_NOTIFICACION = " + id;
            
            if(m_Log.isDebugEnabled()) m_Log.error(sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            if(rs.next()){
                if(null != rs.getString("NUM_INTENTOS") && !"".equals(rs.getString("NUM_INTENTOS")))
                    intentos = rs.getInt("NUM_INTENTOS");
            }            
        }catch(Exception ex){
            m_Log.error("Error en obtieneIntentos: " + ex);
        }finally{
            try{
                if(m_Log.isDebugEnabled()) 
                    m_Log.error("Procedemos a cerrar el statement y el resultset");
                if(st!=null) 
                    st.close();
                if(rs!=null) 
                    rs.close();
            }catch(Exception e){
                m_Log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return intentos;
    }
    
     public boolean obtieneLocalizador (String proce, int codTramite, String numExp, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        boolean localizador= false;
        try
        {
            String query = null;
            
            query = "SELECT * FROM NOTIF_LOCALIZACION " 
                    + "Inner Join E_Crd On Cod_Proc = Crd_Pro And Crd_Tra = Cod_TraM AND CRD_DOT = Cod_Doc"
                + " Where Crd_Num =  '" + numExp + "' AND  Cod_Tram = " + codTramite;

            if(m_Log.isDebugEnabled()) 
                m_Log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next()){ 
                localizador =true;
            }
            m_Log.error("Documento con localizador: " + localizador);
        }
        catch(Exception ex)
        {
            //insertaError(con, ex.toString(), id, "actualizarProcesados");
            m_Log.error("Error en obtieneLocalizador: ", ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if(m_Log.isDebugEnabled()) 
                    m_Log.error("Procedemos a cerrar el statement y el resultset");
                if(st!=null) 
                    st.close();
                if(rs!=null) 
                    rs.close();
            }
            catch(Exception e)
            {
                m_Log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return localizador;
    }
     
     
     //trámites
    public Long getCodigoInternoTramite(int codOrganizacion, String procedimiento, String codTramite, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        Long valor = null;
        try
        {
            String query = "select TRA_COD from E_TRA "
                            +" where TRA_MUN = "+codOrganizacion
                            +" and TRA_PRO = '"+procedimiento+"'"
                            +" and TRA_COU = "+codTramite
                            +" AND TRA_FBA IS NULL";
            if(m_Log.isDebugEnabled()) 
                m_Log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                valor = rs.getLong("TRA_COD");
                if(rs.wasNull())
                {
                    valor = null;
                }
            }
        }
        catch(Exception ex)
        {
            m_Log.error("Se ha producido un error recuperando el código interno del trámite "+codTramite, ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if(m_Log.isDebugEnabled()) 
                    m_Log.debug("Procedemos a cerrar el statement y el resultset");
                if(st!=null) 
                    st.close();
                if(rs!=null) 
                    rs.close();
            }
            catch(Exception e)
            {
                m_Log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return valor;
    }
    
    public Long getCodigoVisibleTramite(int codOrganizacion, String procedimiento, String codTramite, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        Long valor = null;
        try
        {
            String query = "select TRA_COU from E_TRA "
                            +" where TRA_MUN = "+codOrganizacion
                            +" and TRA_PRO = '"+procedimiento+"'"
                            +" and TRA_COD = "+codTramite
                            +" AND TRA_FBA IS NULL";
            if(m_Log.isDebugEnabled()) 
                m_Log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                valor = rs.getLong("TRA_COU");
                if(rs.wasNull())
                {
                    valor = null;
                }
            }
        }
        catch(Exception ex)
        {
            m_Log.error("Se ha producido un error recuperando el código interno del trámite "+codTramite, ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if(m_Log.isDebugEnabled()) 
                    m_Log.debug("Procedemos a cerrar el statement y el resultset");
                if(st!=null) 
                    st.close();
                if(rs!=null) 
                    rs.close();
            }
            catch(Exception e)
            {
                m_Log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return valor;
    }
    
    //devuelve Integer > 0 si es Desarrollo 0 (si la fecha de creación del expediente está entre las 2 fechas definidas en MELANBIDE43_LLAMAMISGEST, ambas incluidas)
    public Integer esDesarrolloCero(String numExp, Connection con) throws TechnicalException {

        Integer resultado = 0;
        ResultSet rs = null;
        Statement st = null;

        try{
            String sql = "select count(*) AS CONT\n" +
                            "from E_EXP\n" +
                            "inner join MELANBIDE43_LLAMAMISGEST\n" +
                            "    on MELANBIDE43_LLAMAMISGEST.COD_PROC=E_EXP.EXP_PRO\n" +
                            "where EXP_NUM = '" + numExp +"'\n" +
                            "and EXP_FEI > FECDES0DESDE and EXP_FEI < FECDES0HASTA+1";
             
            if(m_Log.isDebugEnabled()) m_Log.error(sql);
            
            st = con.createStatement();
            rs = st.executeQuery(sql);

            while(rs.next()){
                resultado=rs.getInt("CONT");
            }
            
        }catch(Exception e){
            e.printStackTrace();
            m_Log.error("Error en esDesarrolloCero: " + e);
        }finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        return resultado;
    }
    
    
    public String getValorCampoSuplementarioExpteTipoTextoxCodNumExp(String numExpediente, String codigoCampo,Connection con) throws Exception  {
        ResultSet rs = null;
        Statement st = null;
        String valorCampo="";
        m_Log.info("getValorCampoSuplementarioExpteTipoTextoxCodNumExp - Begin() " + numExpediente + "-" + codigoCampo);
        try{
            Integer ejercicio = (numExpediente!=null && numExpediente.split("/") != null && numExpediente.split("/").length >0 ? Integer.parseInt(numExpediente.split("/")[0]):0);
            String sql = "select TXT_VALOR from e_txt where txt_cod='"+codigoCampo+"' AND TXT_EJE="+ejercicio+" AND TXt_num='"+numExpediente+"'";

            st = con.createStatement();
            rs = st.executeQuery(sql);

            while(rs.next()){
                valorCampo=rs.getString("TXT_VALOR");
            }
        }
        catch(Exception e){
            m_Log.error("Error en getValorCampoSuplementarioExpteTipoTextoxCodNumExp: " , e);
            throw e;
        }finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        m_Log.info("getValorCampoSuplementarioExpteTipoTextoxCodNumExp - End () " + valorCampo);
        return valorCampo;
    }
    
    public static ArrayList<AutorizadoNotificacionVO> getInteresadosExpedientePluginNotificacion(String numExpediente, int codigoNotificacion, Connection con) throws TechnicalException {
        ResultSet rs = null;
        Statement st = null;
        ArrayList<AutorizadoNotificacionVO> arrayAux = new ArrayList<AutorizadoNotificacionVO>();
        ArrayList<AutorizadoNotificacionVO> arrayRetorno = new ArrayList<AutorizadoNotificacionVO>();

        try {
            String sqlQuery = "SELECT DISTINCT " +
                    " EXT_ROL,EXT_DOT,ROL_DES,ROL_PDE,TID_DES,HTE_TID TER_TID,HTE_TER TER_COD,HTE_NVR TER_NVE,HTE_DOC TER_DOC,HTE_NOM TER_NOM,HTE_AP1 TER_AP1,HTE_AP2 TER_AP2,HTE_TLF TER_TLF,DNN_PAI,HTE_DCE TER_DCE,HTE_NOC TER_NOC, " +
                    "        PAI_NOM,DNN_PRV,	PRV_COD,PRV_NOM,DNN_MUN,MUN_COD,MUN_NOM,TVI_DES,VIA_COD,VIA_NOM,DNN_DMC,DNN_NUD,DNN_LED,DNN_NUH, " +
                    "        DNN_LEH,DNN_BLQ,DNN_POR,DNN_ESC,DNN_PLT,DNN_PTA,DNN_CPO, EXT_MUN,EXT_EJE  " +
                    "  ,EXT_NOTIFICACION_ELECTRONICA " +
                    " FROM  " +
                    " E_EXT " +
                    " LEFT JOIN T_HTE ON EXT_TER=HTE_TER AND EXT_NVR=HTE_NVR " +
                    " LEFT JOIN E_ROL ON EXT_MUN=ROL_MUN AND EXT_PRO=ROL_PRO AND EXT_ROL= ROL_COD " +
                    " LEFT JOIN T_DNN ON EXT_DOT=DNN_DOM  " +
                    " LEFT JOIN T_TVI ON DNN_TVI=TVI_COD " +
                    " LEFT JOIN T_VIA ON DNN_VIA=VIA_COD AND DNN_PAI=VIA_PAI AND DNN_PRV=VIA_PRV AND DNN_MUN=VIA_MUN  " +
                    " LEFT JOIN T_TID ON HTE_TID=TID_COD  " +
                    " LEFT JOIN FLBGEN.T_PAI T_PAI on DNN_PAI=t_pai.PAI_COD  " +
                    " LEFT JOIN FLBGEN.T_PRV T_PRV on DNN_PRV=T_PRV.PRV_COD AND DNN_PAI=T_PRV.PRV_PAI    " +
                    " LEFT JOIN FLBGEN.T_MUN T_MUN on DNN_MUN=T_MUN.MUN_COD AND DNN_PRV=T_MUN.MUN_PRV AND DNN_PAI=T_MUN.MUN_PAI AND DNN_MUN=T_MUN.MUN_COD  " +
                    " WHERE  " 
                    + " EXT_NUM='" + numExpediente + "' "
                    + " AND (EXT_NOTIFICACION_ELECTRONICA='1'   or  ((EXT_NOTIFICACION_ELECTRONICA is null or EXT_NOTIFICACION_ELECTRONICA=0) and  ext_rol in(1,2))) "
                    + " ORDER BY EXT_NOTIFICACION_ELECTRONICA DESC nulls last, EXT_ROL DESC, ROL_PDE DESC "
                    ;
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sqlQuery);
            }

            st = con.createStatement();
            rs = st.executeQuery(sqlQuery);

            int i = 0;
            ResourceBundle bundle = ResourceBundle.getBundle("notificaciones");
            String codPaisRT = bundle.getString("COUNTRY");
            while (rs.next()) {
                m_Log.info("-- Traza leyendo Datos Terceros Expedietnes Notificacion " + numExpediente);
                AutorizadoNotificacionVO autorizadoNotifAux = new AutorizadoNotificacionVO();

                autorizadoNotifAux.setCodigoTercero(rs.getInt("TER_COD"));
                autorizadoNotifAux.setNumeroVersionTercero(rs.getInt("TER_NVE"));
                autorizadoNotifAux.setNombre(rs.getString("TER_NOM"));

                autorizadoNotifAux.setNif(rs.getString("TER_DOC"));
                autorizadoNotifAux.setTipoDocumento(rs.getString("TER_TID"));
                autorizadoNotifAux.setEmail(rs.getString("TER_DCE"));
                autorizadoNotifAux.setNumeroExpediente(numExpediente);
                autorizadoNotifAux.setCodigoMunicipio(rs.getInt("EXT_MUN"));
                autorizadoNotifAux.setEjercicio(rs.getInt("EXT_EJE"));
                autorizadoNotifAux.setSeleccionado("NO");
                autorizadoNotifAux.setApellido1(rs.getString("TER_AP1"));
                autorizadoNotifAux.setApellido2(rs.getString("TER_AP2"));
                autorizadoNotifAux.setTelefono(rs.getString("TER_TLF"));
                autorizadoNotifAux.setCodigoNotificacion(codigoNotificacion);

                String nombreCompleto = "";
                
                if (autorizadoNotifAux.getApellido1() != null && !"".equals(autorizadoNotifAux.getApellido1()) && !"null".equalsIgnoreCase(autorizadoNotifAux.getApellido1())) {
                    nombreCompleto = autorizadoNotifAux.getApellido1();
                }
                if (autorizadoNotifAux.getApellido2() != null && !"".equals(autorizadoNotifAux.getApellido2()) && !"null".equalsIgnoreCase(autorizadoNotifAux.getApellido2())) {
                    nombreCompleto += (!nombreCompleto.isEmpty() ? " " + autorizadoNotifAux.getApellido2() : autorizadoNotifAux.getApellido2());
                }
                if (autorizadoNotifAux.getNombre() != null && !"".equals(autorizadoNotifAux.getNombre()) && !"null".equalsIgnoreCase(autorizadoNotifAux.getNombre())) {
                    nombreCompleto += (!nombreCompleto.isEmpty() ? ", " + autorizadoNotifAux.getNombre() : autorizadoNotifAux.getNombre());
                }

                autorizadoNotifAux.setNombreCompleto(nombreCompleto);

                autorizadoNotifAux.setCodPais(codPaisRT);
                autorizadoNotifAux.setCodProvincia(rs.getString("PRV_COD"));
                autorizadoNotifAux.setDescProvincia(rs.getString("PRV_NOM"));
                autorizadoNotifAux.setDescMunicipio(rs.getString("MUN_NOM"));
                autorizadoNotifAux.setCodVia(rs.getString("VIA_COD"));
                autorizadoNotifAux.setDescVia(rs.getString("VIA_NOM"));
                String tipoVia = rs.getString("TVI_DES");
                String nombreVia = rs.getString("VIA_NOM");
                String numDesde = rs.getString("DNN_NUD");
                String letraDesde = rs.getString("DNN_LED");
                String numHasta = rs.getString("DNN_NUH");
                String letraHasta = rs.getString("DNN_LEH");
                String bloque = rs.getString("DNN_BLQ");
                String portal = rs.getString("DNN_POR");
                String escalera = rs.getString("DNN_ESC");
                String planta = rs.getString("DNN_PLT");
                String puerta = rs.getString("DNN_PTA");
                autorizadoNotifAux.setDireccion(construirDireccion(tipoVia, nombreVia, numDesde, letraDesde, numHasta,
                        letraHasta, bloque, portal, escalera, planta, puerta));
                autorizadoNotifAux.setCodPostal(rs.getString("DNN_CPO"));
                autorizadoNotifAux.setCodDomicilio(rs.getInt("EXT_DOT"));
                autorizadoNotifAux.setRol(rs.getInt("EXT_ROL"));

                arrayAux.add(autorizadoNotifAux);
            }

            rs.close();
            m_Log.info("-- Traza leyendo Datos Terceros Expedietnes Notificacion - ArrayInteresado " + arrayAux.size());

            for (int j = 0; j < arrayAux.size(); j++) {
                AutorizadoNotificacionVO autorizadoNotifVORetorno = new AutorizadoNotificacionVO();
                autorizadoNotifVORetorno = arrayAux.get(j);
                if (estaAutorizadoEnNotificacion(autorizadoNotifVORetorno, codigoNotificacion, con)) {
                    autorizadoNotifVORetorno.setSeleccionado("SI");
                    autorizadoNotifVORetorno.setCodigoNotificacion(codigoNotificacion);
                }

                arrayRetorno.add(autorizadoNotifVORetorno);

            }

        } catch (Exception e) {
            e.printStackTrace();
            m_Log.error(" Error al OBtener datos interesado : " + e.getMessage(),e);
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        m_Log.info("-- Traza leyendo Datos Terceros Expedietnes Notificacion - arrayRetorno " + arrayAux.size());
        return arrayRetorno;
    }
    
    private static String construirDireccion(String tipoVia, String nombreVia, String numDesde, String letraDesde,
                                      String numHasta, String letraHasta, String bloque, String portal, String escalera,
                                      String planta, String puerta) {

        StringBuffer dirBuffer = new StringBuffer();
        if (tipoVia != null && !"".equals(tipoVia.trim())) dirBuffer.append(tipoVia).append(" ");
        dirBuffer.append(nombreVia).append(" ");
        if (numDesde != null && !numDesde.trim().equals("")) dirBuffer.append(numDesde).append(" ");
        if (letraDesde != null && !letraDesde.trim().equals("")) dirBuffer.append(letraDesde).append(" ");
        if ((numHasta != null && !numHasta.trim().equals("")) || (letraHasta != null && !letraHasta.trim().equals(""))) {
            dirBuffer.append(" - ");
            if (numHasta != null && !numHasta.trim().equals("")) dirBuffer.append(numHasta).append(" ");
            if (letraHasta != null && !letraHasta.trim().equals("")) dirBuffer.append(letraHasta).append(" ");
        }
        if (bloque != null && !bloque.trim().equals("")) dirBuffer.append("BLQ. ").append(bloque).append(" ");
        if (portal != null && !portal.trim().equals("")) dirBuffer.append("POR. ").append(portal).append(" ");
        if (escalera != null && !escalera.trim().equals("")) dirBuffer.append("ESC. ").append(escalera).append(" ");
        if (planta != null && !planta.trim().equals("")) dirBuffer.append(planta).append("º ");
        if (puerta != null && !puerta.trim().equals("")) dirBuffer.append(puerta).append(" ");

        return dirBuffer.toString().trim();

    }
    
    //Comprueba si un interesado de un expediente está asociado a una notificación
    private static boolean estaAutorizadoEnNotificacion(AutorizadoNotificacionVO autorizado, int codNotificacion, Connection con) throws TechnicalException {

        boolean retorno = false;
        ResultSet rs = null;
        Statement st = null;

        try {
            int codigoMunicipio = autorizado.getCodigoMunicipio();
            int ejercicio = autorizado.getEjercicio();
            String numeroExpediente = autorizado.getNumeroExpediente();
            int codTercero = autorizado.getCodigoTercero();
            int verTercero = autorizado.getNumeroVersionTercero();

            String sql = "SELECT CODIGO_NOTIFICACION FROM AUTORIZADO_NOTIFICACION "
                    + " WHERE CODIGO_NOTIFICACION = " + codNotificacion + " AND COD_MUNICIPIO=" + codigoMunicipio + " AND EJERCICIO = " + ejercicio + " AND NUM_EXPEDIENTE = '" + numeroExpediente + "'"
                    + "  AND COD_TERCERO= " + codTercero + " AND VER_TERCERO=" + verTercero;

            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }

            st = con.createStatement();
            rs = st.executeQuery(sql);

            if (rs.next()) {
                retorno = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return retorno;
    }

    
    private void actualizarNotificacionesEnTratamiento(ArrayList<NotificacionBVO> listNotificaciones, Connection con) throws Exception{
        Statement st = null;
        ResultSet rs = null;
        int result = 0;
        boolean nuevo = true;
        int num = 0;
        try{
            StringBuffer query = new StringBuffer();
            query.append("UPDATE NOTIFICACION ");
            query.append("SET EN_PROCESO = 'S' ");
            query.append("WHERE CODIGO_NOTIFICACION IN ( ");
                query.append(new Integer(listNotificaciones.get(0).getCodigoNotificacion()).toString());
                for (int i=1; i<=listNotificaciones.size()-1; i++){
                    query.append(", ");
                    query.append(new Integer(listNotificaciones.get(i).getCodigoNotificacion()).toString());
                }
            query.append(") ");
            
            st = con.createStatement();
            rs = st.executeQuery(query.toString());
        }catch(Exception ex){
            m_Log.error("Se ha producido un error al tratar al cambiar la notificiacióna a 'en tratamiento'", ex);
            throw ex;
        }finally{
            try{
                if(m_Log.isDebugEnabled()) 
                    m_Log.info("Procedemos a cerrar el statement y el resultset");
                if(st!=null) 
                    st.close();
                if(rs!=null) rs.close();
            }catch(Exception e){
                m_Log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
}
    

