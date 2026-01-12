
package es.altia.flexia.integracion.moduloexterno.melanbide43.dao;

import com.lanbide.lan6.errores.bean.ErrorBean;
import es.altia.agora.business.terceros.TercerosValueObject;
import es.altia.flexia.integracion.moduloexterno.melanbide43.gestionrdr.util.MELANBIDE43_GestionRdR_Util;
import es.altia.flexia.integracion.moduloexterno.melanbide43.manager.MeLanbide43Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide43.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide43.util.ConstantesMeLanbide43;
import es.altia.flexia.integracion.moduloexterno.melanbide43.util.MeLanbide43MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.ExpAvisos;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.ExpTram;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.Expediente;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.FaseVO;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.FilaFaseVO;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.FilaListadoMisGestiones;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.FilaLlamadasMisGestVO;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.FilaNotificacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.Participantes;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.ProcedimientoSeleccionadoV0;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.ProcedimientoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.Tml;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.Tramite;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.TramiteVO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * @author david.caamano
 * @version 16/08/2012 1.0
 * Historial de cambios:
 * <ol>
 *  <li>david.caamano * 17-08-2012 * #53275 Edici?n inicial</li>
 * </ol> 
 */
public class MeLanbide43DAO {
    
    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide43DAO.class);
    SimpleDateFormat formatDateLog = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    SimpleDateFormat formatoFechaAsunto = new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss");

    //Instancia
    private static MeLanbide43DAO instance = null;
    
    /**
     * Devuelve una instancia de MeLanbide43DAO, si no existe la crea
     * @return MeLanbide43DAO
     */
    public static MeLanbide43DAO getInstance(){
        if(instance == null){
            synchronized(MeLanbide43DAO.class){
                if(instance == null){
                    instance = new MeLanbide43DAO();
                }//if(instance == null)
            }//synchronized(MeLanbide43DAO.class)
        }//if(instance == null)
        return instance;
    }//getInstance
    
     public int guardarMiGestion(String numExpediente, int codTramite, String evento, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        int result = 0;int id = 0;
        boolean nuevo = true;
        try
        {
            String query = null;
            
            String[] pepe = numExpediente.split("/");
            ArrayList<FilaListadoMisGestiones> misGestiones = new ArrayList<FilaListadoMisGestiones>();
            misGestiones = getInfoGestiones(numExpediente, codTramite, evento, con);
            Integer regInicio = null; Integer numInicio = null; Integer tipoCodInte = null;
            String codInte = ""; String tramIni = ""; String regAsun =""; String fechaAsun ="";
            id = getIdGestiones(con);
            id++;
            if(misGestiones.size() > 0){
                if(log.isDebugEnabled()) log.info("Existen datos para insertar en pestaña 'Mis gestiones' de este expediente");

                for(FilaListadoMisGestiones unidad : misGestiones){
                    //id = unidad.getId();
                    tipoCodInte = unidad.getTipoCodInte();
                    codInte = unidad.getCodInteresado();
                    tramIni = unidad.getTramiteInicio(); 
                    regInicio = unidad.getRegInicio();
                    numInicio = unidad.getNumInicio();
                    regAsun = unidad.getRegAsun();
                    fechaAsun = unidad.getFechaAsun();
                }
                query = "insert into MELANBIDE43_INTEGMISGEST" //+ ConfigurationParameter.getParameter(ConstantesMeLanbide42.TABLA_CONTADORES, ConstantesMeLanbide42.FICHERO_PROPIEDADES)
                    + " (ID, EXP_NUM, TER_TID, TER_DOC, TIPO_OPERACION, "
                    + " COD_TRAMITE_INICIO,  FECHA_GENERADO,RES_EJE, RES_NUM, EXP_TIPO, REG_TELEMATICO, FECHA_TELEMATICO) "
                    + " values("+id+", '"+numExpediente+"', " 
                    + tipoCodInte+", '"+codInte+"','"+evento+"', '"+codTramite
                    + "',to_date(to_char(sysdate,'dd/mm/yyyy HH24:mi:ss'), 'dd/mm/yyyy HH24:mi:ss'), "
                    + " "+regInicio+", "+numInicio+", '"+pepe[1]+"', '"+regAsun+"', '"+fechaAsun+"')";

                if(log.isDebugEnabled()) 
                    log.debug("sql = " + query);
                st = con.createStatement();
                result = st.executeUpdate(query);
            }
        }
        catch(Exception ex)
        {
            ErrorBean error = new ErrorBean();
            error.setIdError("MISGEST_001");
            error.setMensajeError("Error al guardar la gestion");
            error.setSituacion("guardarMiGestion");
            
            
            MeLanbide43Manager.grabarError(error, ex.getMessage().toString(), ex.toString(), numExpediente);
            throw new Exception(ex);
        }
        finally
        {
          
            if(st!=null) 
                st.close();
        }
        return result;
    }
     
    public int actualizarProcesados(int id, Connection con) throws Exception
    {
        Statement st = null;
        int result = 0;
        boolean nuevo = true;
        try
        {
            String query = null;
            
            
                query = "UPDATE MELANBIDE43_INTEGMISGEST" //+ ConfigurationParameter.getParameter(ConstantesMeLanbide42.TABLA_CONTADORES, ConstantesMeLanbide42.FICHERO_PROPIEDADES)
                    + " SET FECHA_PROCESADO = to_date(to_char(sysdate,'dd/mm/yyyy HH24:mi:ss'), 'dd/mm/yyyy HH24:mi:ss'), "
                    + " RESULTADO_PROCESO = '1' WHERE ID =  " + id;

                if(log.isDebugEnabled()) 
                    log.debug("sql = " + query);
                st = con.createStatement();
                result = st.executeUpdate(query);
        }
        catch(Exception ex)
        {
            ErrorBean error = new ErrorBean();
            error.setIdError("MISGEST_002");
            error.setMensajeError("Error al actualizar procesados");
            error.setSituacion("actualizarProcesados");
            
            
            MeLanbide43Manager.grabarError(error, ex.getMessage().toString(), ex.toString(), "");
            throw new Exception(ex);
        }
        finally
        {
        
            if(st!=null) 
                st.close();
        }
        return result;
    }
    
    public int actualizarError(int id, int intentos, String error, Connection con) throws Exception
    {
        Statement st = null;
        int result = 0;
        boolean nuevo = true;
        try
        {
            String query = null;
            
            
                query = "UPDATE MELANBIDE43_INTEGMISGEST" //+ ConfigurationParameter.getParameter(ConstantesMeLanbide42.TABLA_CONTADORES, ConstantesMeLanbide42.FICHERO_PROPIEDADES)
                    + " SET NUM_INTENTOS = " + intentos
                    + ", FECHA_PROCESADO = to_date(to_char(sysdate,'dd/mm/yyyy HH24:mi:ss'), 'dd/mm/yyyy HH24:mi:ss'),"
                    + " RESULTADO_PROCESO = '0' WHERE ID =  " + id;

                if(log.isDebugEnabled()) 
                    log.debug("sql = " + query);
                st = con.createStatement();
                result = st.executeUpdate(query);
        }
        catch(Exception ex)
        {
            throw new Exception(ex);
        }
        finally
        {
        
            if(st!=null) 
                st.close();
        }
        return result;
    }
    
    public int  getIdGestiones (Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        int id = 0;
        
        try
        {
            String query = null;
            
            query = "select MAX(ID) cod "
                + "from MELANBIDE43_INTEGMISGEST "; 
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while(rs.next()){
                FilaListadoMisGestiones unidad = new FilaListadoMisGestiones();
                
                if(rs.getString("cod") != null)    
                    id = rs.getInt("cod");  
                //id++;
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando ID de MELANBIDE43_INTEGMISGEST ", ex);
            throw new Exception(ex);
        }finally{
            if(st!=null) st.close();
            if(rs!=null) rs.close();
        }//try-catch-finally 
        if(log.isDebugEnabled()) log.debug("getIdGestiones() : END");
        return id;
    }
    
    public boolean  getExpedienteCerrado (String numExp, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        boolean existe = false;        
        try
        {
            String query = null;
            
            query = "select * from MELANBIDE43_INTEGMISGEST "
                  + "WHERE TIPO_OPERACION = 'C' AND EXP_NUM ='"+numExp+"' AND RESULTADO_PROCESO = 1 "; 
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next()){
                existe = true;
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error en getExpedienteCerrado", ex);
            throw new Exception(ex);
        }finally{
            if(st!=null) st.close();
            if(rs!=null) rs.close();
        }//try-catch-finally 
        if(log.isDebugEnabled()) log.debug("getExpedienteCerrado() : END");
        return existe;
    }
    
    public Boolean  notificadoElec (String numExp,  Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        Boolean notif = false;
        
        try
        {
            String query = null;
            
            
            query = "select * from E_EXT "
                + "WHERE EXT_NUM =  '" +numExp + "' and EXT_NOTIFICACION_ELECTRONICA = 1"; 
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while(rs.next()){
                notif = true;
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando datos", ex);
            
            ErrorBean error = new ErrorBean();
            error.setIdError("MISGEST_003");
            error.setMensajeError("Error recogiendo expedientes");
            error.setSituacion("notificadoElec");
            
            
            MeLanbide43Manager.grabarError(error, ex.getMessage().toString(), ex.toString(), numExp);
            throw new Exception(ex);
        }finally{
            if(st!=null) st.close();
            if(rs!=null) rs.close();
        }//try-catch-finally 
        if(log.isDebugEnabled()) log.debug("notificadoElec() : END");
        return notif;
    }
    
    public int  getIdErrores (Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        int id = 0;
        
        try
        {
            String query = null;
            
            
            query = "select MAX(ID) cod "
                + "from MELANBIDECO_ERRORES "; 
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while(rs.next()){
                FilaListadoMisGestiones unidad = new FilaListadoMisGestiones();
                
                if(rs.getString("cod") != null)    
                    id = rs.getInt("cod");  
                id++;
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando las areas", ex);
            throw new Exception(ex);
        }finally{
            if(st!=null) st.close();
            if(rs!=null) rs.close();
        }//try-catch-finally 
        if(log.isDebugEnabled()) log.debug("getIdErrores() : END");
        return id;
    }
    
    public ArrayList<FilaListadoMisGestiones>  getInfoGestiones(String numExpediente, int codTramite, String evento, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        
        ArrayList<FilaListadoMisGestiones> datosGestiones = new ArrayList<FilaListadoMisGestiones>();
        try
        {
            String query = null;
            
            
            query = "select EXR_EJR ejericicio_anotacion, EXR_NRE numero_registro,TID_COD tipo_doc,"
                    + "HTE_DOC documento, EXP_ASU  "
                + " from E_EXR,E_EXT,T_HTE,T_TID, E_EXP "
                + " where EXT_NUM = EXP_NUM AND EXT_NUM=EXR_NUM(+) and "
                + " HTE_TER=EXT_TER and HTE_NVR=EXT_NVR and "
                + " HTE_TID=TID_cod  and EXT_ROL=1 and "
                + " EXT_NUM='"+numExpediente+"'"; 
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            int id = 0;
            String asunto = "";
            String [] texto;
            String [] datosExp = numExpediente.split("/");
            while(rs.next()){
               
                FilaListadoMisGestiones unidad = new FilaListadoMisGestiones();
                unidad.setNumExp(numExpediente);
                if(rs.getString("tipo_doc") != null)
                    unidad.setTipoCodInte(rs.getInt("tipo_doc"));
                unidad.setCodInteresado(rs.getString("documento"));
                unidad.setTramiteInicio(String.valueOf(codTramite));
                if(rs.getString("numero_registro") != null)
                    unidad.setNumInicio(rs.getInt("numero_registro"));
                if(rs.getString("ejericicio_anotacion") != null)
                    unidad.setRegInicio(rs.getInt("ejericicio_anotacion"));
                if(rs.getString("EXP_ASU") != null)
                    asunto = rs.getString("EXP_ASU");
                //asunto = asunto.replace("|", "@");
                
                
                //////////////////ANTIGUO//////////////////
                /*if(asunto.contains("NumRegGV")){
                    texto = asunto.split(" ");
                    if(texto.length > 2)
                    {
                        unidad.setRegAsun(texto[1]);
                        unidad.setFechaAsun(texto[3]);
                    }
                }*/
                
                
                //////////////NUEVO/////////////////////////////
                log.debug("-------------->asunto: " +asunto);
                if(asunto.toUpperCase().contains("NUMREGGV")){
                    //creo un substring desde donde aparece la cadena "NumRegGV" hasta el final
                    String subs = (asunto.substring(asunto.toUpperCase().indexOf("NUMREGGV"), asunto.length())).trim();
                    log.debug("-------------->subs: " +subs);
                    texto = subs.split(" ");
                    log.debug("-------------->texto:length: " +texto.length);
                    if(texto.length > 1)
                    {
                        log.debug("-------------->texto_REG: " +texto[1]);                        
                        unidad.setRegAsun(texto[1]);
                    }
                    //creo un substring desde donde aparece la cadena "FechaRegGV" hasta el final
                    subs = (asunto.substring(asunto.toUpperCase().indexOf("FECHAREGGV"), asunto.length())).trim();
                    texto = subs.split(" ");
                    log.debug("-------------->texto:length: " +texto.length);
                    if(texto.length > 1)
                    {
                        log.debug("-------------->texto_FECHA: " +texto[1]);                        
                         unidad.setFechaAsun(texto[1]);
                    }
                } else if (datosExp[1].equalsIgnoreCase("RGI") || datosExp[1].equalsIgnoreCase("IMV")) {
                    String hoy = formatoFechaAsunto.format(Calendar.getInstance().getTime());
                    log.info("Es expediente "+ datosExp[1]+" sin fecha en el asunto, se graba la fecha actual con el formato que  los registros: " + hoy);
                    unidad.setFechaAsun(hoy);
                }

                ////////////////////////////////////////
                
                datosGestiones.add(unidad);
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando getInfoGestiones", ex);
            throw new Exception(ex);
        }finally{
            if(st!=null) st.close();
            if(rs!=null) rs.close();
        }//try-catch-finally 
        if(log.isDebugEnabled()) log.debug("getInfoGestiones() : END");
        return datosGestiones;
    }
    public void borrarProcesado(String id, Connection con ) throws Exception
    {
        Statement st = null;
        try
        {
            
            int result = 0;
            String query = "DELETE FROM  MELANBIDE43_INTEGMISGEST" //+ ConfigurationParameter.getParameter(ConstantesMeLanbide42.TABLA_CONTADORES, ConstantesMeLanbide42.FICHERO_PROPIEDADES)
                + " WHERE ID = " + id;
            
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            result = st.executeUpdate(query);
        }catch(Exception ex)
        {
            throw ex;
        }
        finally
        {
            if(st!=null) st.close();
        }
    }
    
    public ArrayList<FilaListadoMisGestiones>  selectProcesados(String id, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        int result = 0;
        boolean nuevo = true;
        ArrayList<FilaListadoMisGestiones> misGestiones = new ArrayList<FilaListadoMisGestiones>();
        try
        {
            String query = null;
            
                query = "SELECT * FROM MELANBIDE43_INTEGMISGEST "
                        + "WHERE (FECHA_PROCESADO IS NULL AND RESULTADO_PROCESO IS NULL) "
                        //+ " OR (RESULTADO_PROCESO = '0'  and  NUM_INTENTOS < 3))"
                        + " AND ID = " + id;

                if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            //int id = 0;
            while(rs.next()){
                FilaListadoMisGestiones unidad = new FilaListadoMisGestiones();
                unidad.setId(rs.getInt("ID"));
                unidad.setNumExp(rs.getString("EXP_NUM"));
                if(rs.getString("TER_TID") != null)
                unidad.setTipoCodInte(rs.getInt("TER_TID"));
                unidad.setCodInteresado(rs.getString("TER_DOC"));
                unidad.setTipoOperacion(rs.getString("TIPO_OPERACION"));
                unidad.setTramiteInicio(rs.getString("COD_TRAMITE_INICIO"));
                unidad.setFechaGenerado(rs.getDate("FECHA_GENERADO"));
                unidad.setNumInicio(rs.getInt("RES_NUM"));
                unidad.setRegInicio(rs.getInt("RES_EJE"));
                unidad.setRegAsun(rs.getString("REG_TELEMATICO"));
                unidad.setFechaAsun(rs.getString("FECHA_TELEMATICO"));
                if(rs.getString("NUM_INTENTOS") != null)
                    unidad.setIntentos(rs.getInt("NUM_INTENTOS"));
                else
                    unidad.setIntentos(0);                
                misGestiones.add(unidad);
            }
        }
        catch(Exception ex)
        {
            //log.error("Se ha producido un error grabando el contador ("+tipoContador+")", ex);
            ErrorBean error = new ErrorBean();
            error.setIdError("MISGEST_009");
            error.setMensajeError("Error recogiendo procesados");
            error.setSituacion("selectProcesados");
            
            
            MeLanbide43Manager.grabarError(error, ex.getMessage().toString(), ex.toString(), "");
            throw new Exception(ex);
        }
        finally
        {
            if(st!=null) 
                st.close();
            if(rs!=null) 
                rs.close();
        }
        return misGestiones;
    }
    
    
    public Expediente leerDatosExp (String numExp, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        int result = 0;
        boolean nuevo = true;
        Expediente exp = new Expediente();
        try
        {
            String query = null;
            log.debug("Antes de formar la query");
                query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide43.TABLA_EXPEDIENTES, ConstantesMeLanbide43.FICHERO_PROPIEDADES)
                        + " LEFT JOIN " + ConfigurationParameter.getParameter(ConstantesMeLanbide43.TABLA_EXR, ConstantesMeLanbide43.FICHERO_PROPIEDADES)
                        + " ON EXR_NUM = EXP_NUM"
                        + " LEFT JOIN " + ConfigurationParameter.getParameter(ConstantesMeLanbide43.TABLA_REGISTRO, ConstantesMeLanbide43.FICHERO_PROPIEDADES)
                        + " ON RES_TIP = 'E' AND RES_UOR = 0 AND EXR_EJR = RES_EJE AND EXR_NRE = RES_NUM "
                        + " LEFT JOIN " + ConfigurationParameter.getParameter(ConstantesMeLanbide43.TABLA_EXPTERCEROS, ConstantesMeLanbide43.FICHERO_PROPIEDADES) + "  ON EXT_NUM = EXP_NUM "
                        + " LEFT JOIN E_REX ON REX_NUM = EXP_NUM "
                        + "WHERE EXP_NUM = '" + numExp + "'";

                if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            int id = 0;
            if(rs.next()){
                exp.setNumExp(rs.getString("EXP_NUM"));
                if(rs.getString("EXP_EJE") != null)
                    exp.setEjercicio(rs.getInt("EXP_EJE"));
                exp.setAsunto(rs.getString("EXP_ASU"));
                if(rs.getString("EXP_FEI") != null)
                    exp.setFechaApertura(rs.getDate("EXP_FEI"));                
                if(rs.getString("RES_FEC") != null)
                    exp.setFechaSolicitud(rs.getDate("RES_FEC"));        
                exp.setEstado(rs.getString("EXP_EST"));
                exp.setCanal(rs.getString("EXT_NOTIFICACION_ELECTRONICA"));          
                if(rs.getString("EXP_FEF") != null)
                    exp.setFechaCierre(rs.getDate("EXP_FEF"));
                log.debug("REX_NUMR = " + rs.getString("REX_NUMR"));
                if(rs.getString("REX_NUMR") != null) {
                    exp.setNumExpRel(rs.getString("REX_NUMR"));
                }
                if(rs.getString("REX_EJER") != null)
                    exp.setEjercicioRel(rs.getInt("REX_EJER"));
            }
        }
        catch(Exception ex)
        {
            //log.error("Se ha producido un error grabando el contador ("+tipoContador+")", ex);
            
            ErrorBean error = new ErrorBean();
            error.setIdError("MISGEST_004");
            error.setMensajeError("Error al leer los datos del expediente");
            error.setSituacion("leerDatosExp");
            log.error("Error en leerDatosExp: " + ex);
            
            MeLanbide43Manager.grabarError(error, ex.getMessage().toString(), ex.toString(), numExp);
            throw new Exception(ex);
        }
        finally
        {
            if(st!=null) 
                st.close();
            if(rs!=null) 
                rs.close();
        }
        return exp;
    }
    
    
    public Tramite leerDatosTramite (String proced, String codTra, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        int result = 0;
        boolean nuevo = true;
        Tramite exp = new Tramite();
        try
        {
            String query = null;
            
                query = "SELECT * FROM MELANBIDE43_FASE " 
                        + "WHERE COD_PROC = '" + proced + "' AND COD_TRAMITE = " + codTra;

                if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            int id = 0;
            if(rs.next()){
                exp.setCod(rs.getString("COD_FASE"));
                exp.setDescripcion(rs.getString("DESC_FASE_C"));
                exp.setDescripcionEu(rs.getString("DESC_FASE_E"));
            }
        }
        catch(Exception ex)
        {
            //log.error("Se ha producido un error grabando el contador ("+tipoContador+")", ex);
            throw new Exception(ex);
        }
        finally
        {
            if(st!=null) 
                st.close();
            if(rs!=null) 
                rs.close();
        }
        return exp;
    }
    
    public String leerJustificante (String numReg, String ano, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        int result = 0;
        boolean nuevo = true;        
        //StringBuilder sb = new StringBuilder();
        //StringBuilder temp = new StringBuilder();
        //String hex = "";
        String oid = "";
        try
        {
            String query = null;
            
//                query = "select * " +
//                    "from MELANBIDE_DOKUSI_RELDOC_REGIST " +
//                    "where RELDOC_NOM_DOC like 'JUSTIFICANTE_SOLICITUD_TELEMATICA%' "
//                   + "and RELDOC_EJE=" + ano +" and RELDOC_NUM="+numReg+" and RELDOC_tip='E' and " +
//                    "RELDOC_UOR=0 ";
                
                query = "select * " +
                    "from R_RED " +
                    "where RED_NOM_DOC like 'JUSTIFICANTE_SOLICITUD_TELEMATICA%' "
                   + "and RED_EJE=" + ano +" and RED_NUM="+numReg+" and RED_TIP='E' and " +
                    "RED_UOR=0 ";
                
                               
                

                if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            int id = 0;
            String nomDoc = "";
            if(rs.next()){
                //oid = rs.getString("RELDOC_OID");
                //nomDoc = rs.getString("RELDOC_NOM_DOC");
                oid = rs.getString("RED_IDDOC_GESTOR");
                nomDoc = rs.getString("RED_NOM_DOC");
                oid = oid+"#"+nomDoc;
                log.debug("devuelvo: " + oid);
            }
            else return null;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();            
            log.error("Error en leerJustificante " + ex.getMessage());
            ErrorBean error = new ErrorBean();
            error.setIdError("MISGEST_011");
            error.setMensajeError("Error leyendo justificante");
            error.setSituacion("leerJustificante");

            MeLanbide43Manager.grabarError(error, ex.getMessage().toString(), ex.toString(), "");
            //log.error("Se ha producido un error grabando el contador ("+tipoContador+")", ex);
            throw new Exception(ex);
        }
        finally
        {
            if(st!=null) 
                st.close();
            if(rs!=null) 
                rs.close();
        }
        return oid;
    }
    
    
    public ArrayList<String> leerDocumentosAportados (String numReg, String ano, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        int result = 0;
        boolean nuevo = true;  
        ArrayList<String> lista = new ArrayList<String>();
        //StringBuilder sb = new StringBuilder();
        //StringBuilder temp = new StringBuilder();
        //String hex = "";
        String oid = "";
        try
        {
            String query = null;
            
//                query = "SELECT * FROM MELANBIDE_DOKUSI_RELDOC_REGIST "
//                        + "WHERE RELDOC_OID NOT IN "
//                        + "(select RELDOC_OID " +
//                        "from MELANBIDE_DOKUSI_RELDOC_REGIST " +
//                        "where RELDOC_NOM_DOC like 'JUSTIFICANTE_SOLICITUD_TELEMATICA%' "
//                       + "and RELDOC_EJE=" + ano +" and RELDOC_NUM="+numReg+" and RELDOC_tip='E' and " +
//                        "RELDOC_UOR=0 )"
//                        + "and RELDOC_EJE=" + ano +" and RELDOC_NUM="+numReg+" and RELDOC_tip='E' and " +
//                        "RELDOC_UOR=0";
                
//                query = "SELECT * FROM R_RED "
//                        + "WHERE RED_IDDOC_GESTOR NOT IN "
//                        + "(select RED_IDDOC_GESTOR " +
//                        "from R_RED " +
//                        "where RED_NOM_DOC like 'JUSTIFICANTE_SOLICITUD_TELEMATICA%' "
//                       + "and RED_EJE=" + ano +" and RED_NUM="+numReg+" and RED_TIPp='E' and " +
//                        "RED_UOR=0 )"
//                        + "and RED_EJE=" + ano +" and RED_NUM="+numReg+" and RED_TIP='E' and " +
//                        "RED_UOR=0";
            
                query = "SELECT * FROM R_RED " +
                        "where RED_NOM_DOC not like 'JUSTIFICANTE_SOLICITUD_TELEMATICA%' "
                       + "and RED_EJE=" + ano +" and RED_NUM="+numReg+" and RED_TIP='E' and " +
                        "RED_UOR=0 and RED_IDDOC_GESTOR is not null";

                if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            int id = 0;
            String nomDoc = "";
            while(rs.next()){
                oid = rs.getString("RED_IDDOC_GESTOR");
                nomDoc = rs.getString("RED_NOM_DOC");
                oid = oid+"#"+nomDoc;
                log.debug("devuelvo: " + oid);
                lista.add(oid);
            }
            //else return null;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();            
            log.error("Error en leerDocumentosAportados " + ex.getMessage());
            ErrorBean error = new ErrorBean();
            error.setIdError("MISGEST_011");
            error.setMensajeError("Error leyendo documentos aportados");
            error.setSituacion("leerDocumentosAportados");

            MeLanbide43Manager.grabarError(error, ex.getMessage().toString(), ex.toString(), "");
            //log.error("Se ha producido un error grabando el contador ("+tipoContador+")", ex);
            throw new Exception(ex);
        }
        finally
        {
            if(st!=null) 
                st.close();
            if(rs!=null) 
                rs.close();
        }
        return lista;
    }
    
    public ArrayList<String> leerDocumentoExpediente (String numExp, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        int result = 0;
        boolean nuevo = true;   
        ArrayList<String> oid = new ArrayList<String>();
        //StringBuilder sb = new StringBuilder();
        //StringBuilder temp = new StringBuilder();
        //String hex = "";
        try
        {
            String query = null;
            
                query = "select * " +
                    "from MELANBIDE_DOKUSI_RELDOC_DOCCST " +
                    "where RELDOC_NUM = '"+numExp+"' ";

                if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            int id = 0;
            String nomDoc = "";
            while(rs.next()){
                oid.add(rs.getString("RELDOC_OID"));
                //oid = new ArrayList<String>();
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();            
            log.error("Error en leerDocumentoExpediente " + ex.getMessage());
            ErrorBean error = new ErrorBean();
            error.setIdError("MISGEST_005");
            error.setMensajeError("Error leyendo documentos del expediente");
            error.setSituacion("leerDocumentoExpediente");
            
            
            MeLanbide43Manager.grabarError(error, ex.getMessage().toString(), ex.toString(), numExp);
            //log.error("Se ha producido un error grabando el contador ("+tipoContador+")", ex);
            throw new Exception(ex);
        }
        finally
        {
            if(st!=null) 
                st.close();
            if(rs!=null) 
                rs.close();
        }
        return oid;
    }
    
    public Participantes leerDatosParticipantes (String numExp, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        int result = 0;
        boolean nuevo = true;
        Participantes exp = new Participantes();
        try
        {
            String query = null;
            
                query = "SELECT EXT_NUM "
                        + ", HTE_DOC, HTE_NOM, HTE_AP1, HTE_AP2,HTE_TLF, HTE_DCE, HTE_NOC, HTE_TID "
                        + ", DNN_DMC, PAI_COD, PRV_COD, MUN_COD, VIA_COD , PAI_NOM, PRV_NOM, MUN_NOM, VIA_NOM, DNN_LED, DNN_NUD, EXT_ROL  " +
                        " FROM E_EXT " +
                        " INNER JOIN T_hte ON EXT_TER = hte_ter and hte_nvr=ext_nvr "
                      + " LEFT JOIN T_DOT ON EXT_DOT = DOT_DOM AND HTE_TER = DOT_TER  " +
                        " LEFT JOIN T_DNN ON DNN_DOM = DOT_DOM " +
                        " LEFT JOIN FLBGEN.T_PAI ON PAI_COD = DNN_PAI " +
                        " LEFT JOIN FLBGEN.T_PRV ON PRV_PAI = PAI_COD AND PRV_COD = DNN_PRV " +
                        " LEFT JOIN FLBGEN.T_MUN ON MUN_PAI = PAI_COD AND MUN_PRV = PRV_COD AND MUN_COD = DNN_MUN " +
                        " LEFT JOIN T_VIA ON VIA_PAI = PAI_COD AND VIA_PRV = PRV_COD AND VIA_MUN = MUN_COD AND VIA_COD = DNN_VIA " +
                        " WHERE EXT_NUM = '"+numExp+"' AND EXT_ROL=1";

                if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            int id = 0;
            if(rs.next()){
                exp.setNumExp(rs.getString("EXT_NUM"));
                exp.setNif(rs.getString("HTE_DOC"));
                exp.setNombre(rs.getString("HTE_NOM"));
                exp.setApe1(rs.getString("HTE_AP1"));
                exp.setApe2(rs.getString("HTE_AP2"));
                exp.setTlf(rs.getString("HTE_TLF"));
                exp.setMail(rs.getString("HTE_DCE"));
                exp.setNomC(rs.getString("HTE_NOC"));
                exp.setTipoID(rs.getInt("HTE_TID"));
                exp.setIdPais(rs.getString("PAI_COD"));
                exp.setIdProv(rs.getString("PRV_COD"));
                exp.setIdMuni(rs.getString("MUN_COD"));
                exp.setIdCalle(rs.getString("VIA_COD"));
                exp.setPais(rs.getString("PAI_NOM"));
                exp.setProv(rs.getString("PRV_NOM"));
                exp.setMuni(rs.getString("MUN_NOM"));
                exp.setCalle(rs.getString("VIA_NOM"));
                exp.setNum(rs.getString("DNN_NUD"));
                exp.setLetra(rs.getString("DNN_LED"));
                //exp.setRol(rs.getString("EXT_ROL"));
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();            
            log.error("Error en leerDatosParticipantes " + ex.getMessage());
            
            ErrorBean error = new ErrorBean();
            error.setIdError("MISGEST_006");
            error.setMensajeError("Error leyendo datos de participantes");
            error.setSituacion("leerDatosParticipantes");
            
            
            MeLanbide43Manager.grabarError(error, ex.getMessage().toString(), ex.toString(), numExp);
            //log.error("Se ha producido un error grabando el contador ("+tipoContador+")", ex);
            throw new Exception(ex);
        }
        finally
        {
            if(st!=null) 
                st.close();
            if(rs!=null) 
                rs.close();
        }
        return exp;
    }
    
    
    public ArrayList<Participantes> leerListaParticipantes (String numExp, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        int result = 0;
        boolean nuevo = true;
        Participantes exp = new Participantes();
        ArrayList<Participantes> parti = new ArrayList<Participantes>();
        try
        {
            String query = null;
            
                query = "SELECT EXT_NUM "
                        + ",HTE_DOC, HTE_NOM, HTE_AP1, HTE_AP2, HTE_TLF, HTE_DCE, HTE_NOC, HTE_TID "
                        + ", DNN_DMC, PAI_COD, PRV_COD, MUN_COD, VIA_COD "
                        + ", PAI_NOM, PRV_NOM, MUN_NOM, VIA_NOM, DNN_LED, DNN_NUD, EXT_ROL  "
                        + " FROM E_EXT "
                        + " INNER JOIN T_hte ON EXT_TER = hte_ter and hte_nvr=ext_nvr "
                        + " LEFT JOIN T_DOT ON EXT_DOT = DOT_DOM AND HTE_TER = DOT_TER " +
                          " LEFT JOIN T_DNN ON DNN_DOM = DOT_DOM " +
                          " LEFT JOIN FLBGEN.T_PAI ON PAI_COD = DNN_PAI " +
                          " LEFT JOIN FLBGEN.T_PRV ON PRV_PAI = PAI_COD AND PRV_COD = DNN_PRV " +
                          " LEFT JOIN FLBGEN.T_MUN ON MUN_PAI = PAI_COD AND MUN_PRV = PRV_COD AND MUN_COD = DNN_MUN " +
                          " LEFT JOIN T_VIA ON VIA_PAI = PAI_COD AND VIA_PRV = PRV_COD AND VIA_MUN = MUN_COD AND VIA_COD = DNN_VIA " +
                          " WHERE EXT_NUM = '"+numExp+"' AND (EXT_ROL = 1 OR EXT_ROL = 2)";

                if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            int id = 0;
            while(rs.next()){
                exp.setNumExp(rs.getString("EXT_NUM"));
                exp.setNif(rs.getString("HTE_DOC"));
                exp.setNombre(rs.getString("HTE_NOM"));
                exp.setApe1(rs.getString("HTE_AP1"));
                exp.setApe2(rs.getString("HTE_AP2"));
                exp.setTlf(rs.getString("HTE_TLF"));
                exp.setMail(rs.getString("HTE_DCE"));
                exp.setNomC(rs.getString("HTE_NOC"));
                exp.setTipoID(rs.getInt("HTE_TID"));
                exp.setIdPais(rs.getString("PAI_COD"));
                exp.setIdProv(rs.getString("PRV_COD"));
                exp.setIdMuni(rs.getString("MUN_COD"));
                exp.setIdCalle(rs.getString("VIA_COD"));
                exp.setPais(rs.getString("PAI_NOM"));
                exp.setProv(rs.getString("PRV_NOM"));
                exp.setMuni(rs.getString("MUN_NOM"));
                exp.setCalle(rs.getString("VIA_NOM"));
                exp.setNum(rs.getString("DNN_NUD"));
                exp.setLetra(rs.getString("DNN_LED"));
                exp.setRol(rs.getString("EXT_ROL"));
                parti.add(exp);
                exp = new Participantes();
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();            
            log.error("Error en leerDatosParticipantes " + ex.getMessage());
            
            ErrorBean error = new ErrorBean();
            error.setIdError("MISGEST_006");
            error.setMensajeError("Error leyendo datos de participantes");
            error.setSituacion("leerDatosParticipantes");
            
            
            MeLanbide43Manager.grabarError(error, ex.getMessage().toString(), ex.toString(), numExp);
            //log.error("Se ha producido un error grabando el contador ("+tipoContador+")", ex);
            throw new Exception(ex);
        }
        finally
        {
            if(st!=null) 
                st.close();
            if(rs!=null) 
                rs.close();
        }
        return parti;
    }
    
    public int guardarConQuartz(Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        int result = 0;
        boolean nuevo = true;
        int id = 0; 
        try
        {
            String query = null;
            
            ArrayList<FilaListadoMisGestiones> misGestiones = new ArrayList<FilaListadoMisGestiones>();
            id = getIdGestiones(con);
                if(log.isDebugEnabled()) log.info("Existen datos para insertar en pestaña 'Mis gestiones' de este expediente");

            query = "insert into MELANBIDE43_INTEGMISGEST" //+ ConfigurationParameter.getParameter(ConstantesMeLanbide42.TABLA_CONTADORES, ConstantesMeLanbide42.FICHERO_PROPIEDADES)
                + " (ID, EXP_NUM, TER_TID, TER_DOC, TIPO_OPERACION, "
                + " TRAMITE_INICIO,  FECHA_GENERADO) "
                + " values("+id+", "  
                + ", 1, '11111111H','Q', 'TRAM"
                + "',TO_DATE(sysdate,'dd/mm/yyyy')"
                + ")";
            
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            result = st.executeUpdate(query);
            
        }
        catch(Exception ex)
        {
            throw new Exception(ex);
        }
        finally
        {

            if(st!=null) 
                st.close();
        }
        return result;
    }
    
    public int insertaError(Connection con, String ex, int codigo, String accion) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        int result = 0;
        boolean nuevo = true;
        try
        {
            String query = null;
            
            int id = 0; 
            id = getIdErrores(con);
                if(log.isDebugEnabled()) log.info("Existen datos para insertar en pestaña 'Mis gestiones' de este expediente");

            query = "insert into MELANBIDECO_ERRORES" //+ ConfigurationParameter.getParameter(ConstantesMeLanbide42.TABLA_CONTADORES, ConstantesMeLanbide42.FICHERO_PROPIEDADES)
                + " (ID, MODULO, FECHA_HORA, ACCION_ES, EXCEPCION, DESCRIPCION_ES) "
                + " values("+id+", 'MELANBIDE43', "
                + "to_date(to_char(sysdate,'dd/mm/yyyy HH24:mi:ss'), 'dd/mm/yyyy HH24:mi:ss') , '"+accion+"', '"
                + ex + "', '')";

            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            result = st.executeUpdate(query);
            
        }
        catch(Exception exc)
        {
            throw new Exception(exc);
        }
        finally
        {
          
            if(st!=null) 
                st.close();
        }
        return result;
    }
    
    public String verificarFecha (Connection con, String proce)throws Exception{
        String fecha = "";
        Statement st = null;
        ResultSet rs = null;
        try
        {
            String query = null;
            
            query = "select TO_CHAR( FECHA, 'dd/MM/yyyy') AS FECHA " +
                "from MELANBIDE43_LLAMAMISGEST " +
                "where COD_PROC = '"+proce+"' ";

            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            
            if(rs.next()){
                fecha = rs.getString("FECHA");
            }
        }catch(Exception ex){
            throw ex;
        }finally {
            if(st!=null) 
                st.close(); 
            if(rs!=null) 
                rs.close();
        }
        return fecha;
    }
    
    public String fechaInicioExp (Connection con, String exp)throws Exception{
        String fecha = "";
        Statement st = null;
        ResultSet rs = null;
        try
        {
            String query = null;
            
            query = "select TO_CHAR( EXP_FEI, 'dd/MM/yyyy') AS FECHA " +
                "from E_EXP " +
                "where EXP_NUM = '"+exp+"' ";

            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            
            if(rs.next()){
                fecha = rs.getString("FECHA");
            }
        }catch(Exception ex){
            throw ex;
        }finally {
            if(st!=null) 
                st.close(); 
            if(rs!=null) 
                rs.close();
        }
        return fecha;
    }
    
    public Boolean  expInstanciaParte (String numExp,  Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        Boolean notif = false;
        
        try
        {
            String query = null;
            
            
            query = "Select * From E_Exp " +
                    "Inner Join E_Exr On Exr_Num = Exp_Num And Exr_Mun = Exp_Mun And Exp_Pro = Exr_Pro " +
                    "WHERE EXR_TOP = 0 AND EXR_NUM = '"+numExp+"'"; 
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next()){
                notif = true;
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando datos en expInstanciaParte", ex);
            
            ErrorBean error = new ErrorBean();
            error.setIdError("MISGEST_003");
            error.setMensajeError("Error recogiendo expedientes");
            error.setSituacion("expInstanciaParte");
            
            
            MeLanbide43Manager.grabarError(error, ex.getMessage().toString(), ex.toString(), numExp);
            throw new Exception(ex);
        }finally{
            if(st!=null) st.close();
            if(rs!=null) rs.close();
        }//try-catch-finally 
        if(log.isDebugEnabled()) log.debug("getCodigosUnidades() : END");
        return notif;
    }
    public List<ProcedimientoVO> getProcedimientos(Connection con) throws Exception
    {
        log.debug(" Entrando a DAO.GetProcedimientos");
        Statement st = null;
        ResultSet rs = null;
        List<ProcedimientoVO> listProc  = new ArrayList<ProcedimientoVO>();
        ProcedimientoVO procedimiento  = new ProcedimientoVO();
        try
        {
            String query = null;
            query = "SELECT pro_cod,pro_des FROM E_PRO ORDER BY PRO_COD";
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while(rs.next())
            {
                procedimiento = (ProcedimientoVO)MeLanbide43MappingUtils.getInstance().map(rs, ProcedimientoVO.class);
                listProc.add(procedimiento);
                procedimiento = new ProcedimientoVO();
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando Procedimientos", ex);
            throw new Exception(ex);
        }
        finally
        {
            if(st!=null) 
                st.close();
            if(rs!=null) 
                rs.close();
        }
        return listProc;
    } 

    public List<FilaFaseVO> getListaFasesporProcedimiento(String codProc, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        List<FilaFaseVO> fases = new ArrayList<FilaFaseVO>();
        try
        {
            String query = null;
               query = "SELECT FASE.COD_PROC     COD_PROC," +
                       "       TR.TRA_COU        COD_TRAM_EXT," +
                       "       FASE.COD_TRAMITE  COD_TRAMITE," +
                       "       DTR.TML_VALOR     TRAMITE," +
                       "       FASE.COD_FASE     COD_FASE," +
                       "       FASE.DESC_FASE_C," +
                       "       FASE.DESC_FASE_E" +
                       "  FROM MELANBIDE43_FASE FASE, E_TML DTR, E_TRA TR  " +
                       " WHERE COD_PROC      = '"+ codProc +"'" +
                       " AND   COD_PROC      = TML_PRO " +
                       " AND   COD_PROC      = TRA_PRO " +
                       " AND   FASE.COD_TRAMITE   = TML_TRA " +
                       " AND   FASE.COD_TRAMITE   = TRA_COD" +
                       " AND   TRA_FBA     IS NULL " +
                       " ORDER BY COD_PROC, CAST(COD_TRAM_EXT AS INT), COD_FASE";

            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while(rs.next())
            {
                fases.add((FilaFaseVO)MeLanbide43MappingUtils.getInstance().map(rs, FilaFaseVO.class));
            }
            return fases;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando fases para el procedimiento "+codProc, ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if(st!=null) 
                    st.close();
                if(rs!=null)
                    rs.close();
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }

    public FaseVO getFaseProcedimientoTramiteyFase(String codProc, String codTram, String codFase, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        FaseVO fase = new FaseVO();
        try
        {
            String query = null;
               query = "SELECT FASE.COD_PROC  COD_PROC," +
                    "       TR.TRA_COU        COD_TRAM_EXT," +
                    "       FASE.COD_TRAMITE  COD_TRAMITE," +
                    "       FASE.COD_FASE  COD_FASE," +
                    "       FASE.DESC_FASE_C," +
                    "       FASE.DESC_FASE_E" +
                    " FROM  MELANBIDE43_FASE FASE, E_TRA TR " +
                    " WHERE COD_PROC        = '" + codProc +"'" +
                    " AND   COD_PROC        = TRA_PRO " +
                    " AND   COD_TRAMITE     = '" + codTram +"'" +
                    " AND   COD_TRAMITE     = TRA_COD " +
                    " AND   COD_FASE        = '" + codFase +"'";

            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while(rs.next())
            {
                fase = (FaseVO)MeLanbide43MappingUtils.getInstance().map(rs, FaseVO.class);
            }
            return fase;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando fase " + codFase + " para el procedimiento "+ codProc + " y el trámite "+ codTram, ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if(st!=null) 
                    st.close();
                if(rs!=null)
                    rs.close();
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    
    public FaseVO guardarFaseVO(FaseVO fase, Connection con) throws Exception
    {
        Statement st = null;
        try
        {
            String query = null;

            query = "insert into MELANBIDE43_FASE (COD_PROC, COD_TRAMITE, COD_FASE, DESC_FASE_C, DESC_FASE_E)"
                    + " values("+(fase.getCodProcedimiento() != null ? "'"+fase.getCodProcedimiento()+"'" : "null")
                    + ", "+(fase.getCodTramite() != null ? "'"+fase.getCodTramite()+"'" : "null")
                    + ", "+(fase.getCodFase() != null ? "'"+fase.getCodFase()+"'" : "null")
                    + ", "+(fase.getDescFaseCas() != null ? "'"+fase.getDescFaseCas()+"'" : "null")
                    + ", "+(fase.getDescFaseEus() != null ? "'"+fase.getDescFaseEus()+"'" : "null")                      
                    + ")";
           
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            int res = st.executeUpdate(query);
            if(res > 0)
            {
                return fase;
            }
            else
            {
                return null;
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error guardando los datos del fase "+(fase != null ? fase.getCodFase() : "(fase = null)")+" para el procedimiento "+(fase != null ? fase.getCodProcedimiento() : "(fase = null)"), ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if(st!=null) 
                    st.close();
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    
    public int eliminarFase(FaseVO p, Connection con) throws Exception
    {
        Statement st = null;
        try
        {
            String query = null;
                query = "delete from MELANBIDE43_FASE"
                       +" where COD_PROC    = '"+p.getCodProcedimiento() +"'"
                       +" and   COD_TRAMITE = '"+p.getCodTramite() +"'" 
                       +" and   COD_FASE    = '"+p.getCodFase() +"'";
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            return st.executeUpdate(query);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error eliminando fase  "+(p != null ? p.getCodFase() : "fase = null")+" para el procedimiento "+(p != null ? p.getCodProcedimiento() : "fase = null")+" y el trámite "+(p != null ? p.getCodTramite() : "fase = null"), ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if(st!=null)
                    st.close();
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    
    public int modificarFase(FaseVO p, Connection con) throws Exception
    {
        Statement st = null;
        try
        {
            String query = null;
                query = "UPDATE MELANBIDE43_FASE" 
                    +" SET DESC_FASE_C   = '"+p.getDescFaseCas() +"',"
                    +"     DESC_FASE_E   = '"+p.getDescFaseEus() +"'"
                    +" where COD_PROC    = '"+p.getCodProcedimiento() +"'"
                    +" and   COD_TRAMITE = '"+p.getCodTramite() +"'" 
                    +" and   COD_FASE    = '"+p.getCodFase() +"'";
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            return st.executeUpdate(query);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error modificando fase  "+(p != null ? p.getCodFase() : "fase = null")+" para el procedimiento "+(p != null ? p.getCodProcedimiento() : "fase = null")+" y el trámite "+(p != null ? p.getCodTramite() : "fase = null"), ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if(st!=null)
                    st.close();
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    
    public List<TramiteVO> getTramites(String codProc, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        List<TramiteVO> listTram  = new ArrayList<TramiteVO>();
        TramiteVO tramite  = new TramiteVO();
        try
        {
            String query = null;
            query = "SELECT TR.TRA_COD      COD_TRAM_INTERNO," +
                    "       DTR.TML_VALOR   TRAMITE " +
                    "FROM   E_TRA TR, " +
                    "       E_TML DTR " +
                    "WHERE TRA_PRO   = TML_PRO " +
                    "  AND TR.TRA_COD  = DTR.TML_TRA " +
                    "  AND TRA_PRO     = '"+ codProc +"'" +
                    "  AND TRA_FBA     IS NULL " +
                    "ORDER BY CAST(TRA_COD AS INT)";
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while(rs.next())
            {
                tramite = (TramiteVO)MeLanbide43MappingUtils.getInstance().map(rs, TramiteVO.class);
                listTram.add(tramite);
                tramite = new TramiteVO();
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando Tramites para el Procedimiento: "+ codProc, ex);
            throw new Exception(ex);
        }
        finally
        {
            if(st!=null) 
                st.close();
            if(rs!=null) 
                rs.close();
        }
        return listTram;
    }
    public TramiteVO getDatosTramiteXcodigoVisibleYExpediente(String codProc,String codigoVisibleTramite,String numeroExp, Connection con) throws Exception {
        log.info("getTramiteXcodigoVisible - Begin() " + codProc+"/"+codigoVisibleTramite );
        Statement st = null;
        ResultSet rs = null;
        TramiteVO tramite = new TramiteVO();
        try {
            String query = null;
            query = "SELECT TR.TRA_COD COD_TRAM_INTERNO, " +
                    " DTR.TML_VALOR TRAMITE " +
                    " ,E_Cro.Cro_Fei " +
                    " ,E_Cro.Cro_FeF "
                    + " FROM E_TRA TR "
                    + " LEFT JOIN E_TML DTR ON TR.TRA_PRO=DTR.TML_PRO AND TR.TRA_COD=DTR.TML_TRA "
                    + " LEFT JOIN E_CRO  ON TR.TRA_PRO=E_CRO.CRO_PRO AND TR.TRA_COD=E_CRO.CRO_TRA AND E_CRO.CRO_NUM='"+numeroExp+"' "
                    + "WHERE TRA_PRO = '" + codProc + "'"
                    + "  AND TRA_COU = '" + codigoVisibleTramite + "'"
                    + "  AND TRA_FBA IS NULL "
                    + "ORDER BY CAST(TRA_COD AS INT)"
                    ;
            log.debug("getTramiteXcodigoVisible sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                tramite = (TramiteVO) MeLanbide43MappingUtils.getInstance().map(rs, TramiteVO.class);
                tramite.setFechaInicioTramite(rs.getDate("Cro_Fei"));
                tramite.setFechaFinTramite(rs.getDate("Cro_FeF"));
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando Tramites para el Procedimiento: " + codProc, ex);
            throw new Exception(ex);
        } finally {
           
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        log.debug("getTramiteXcodigoVisible - End() " + tramite.getCodTramInterno() );
        return tramite;
    }

    //#233082 - Consulta y reenvio de notificaciones - inicio
    public List<FilaNotificacionVO> getListaNotificaciones(String codProc, String numExped, String fecDesde, String fecHasta, String resultado, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        List<FilaNotificacionVO> notificaciones = new ArrayList<FilaNotificacionVO>();
        try
        {
            String query = null;
            query = "SELECT CODIGO_NOTIFICACION AS COD_NOTIF," +               
                    "       NUM_EXPEDIENTE      AS NUM_EXPED," +                 
                    "       COD_PROCEDIMIENTO   AS COD_PROC," +                 
                    "       EJERCICIO           AS EJERC," +                    
                    "       COD_MUNICIPIO       AS COD_MUNIC," +         
                    "       COD_TRAMITE         AS COD_TRAM," +
                    "       E_TML.TML_VALOR     AS DES_TRAM," +
                    "       OCU_TRAMITE         AS OCU_TRAM," +
                    "       ACTO_NOTIFICADO     AS ACT_NOTIF," +
                    "       CADUCIDAD_NOTIFICACION AS CAD_NOTIF," +
                    "       TEXTO_NOTIFICACION  AS TXT_NOTIF," +
                    "       FIRMADA             AS FIRMADA," +             
                    "       TO_CHAR ( FECHA_ENVIO, 'dd/MM/yyyy') AS FEC_ENVIO," +               
                    "       COD_NOTIFICACION_PLATEA  AS COD_NOTIF_PLATEA," +
                    "       TO_CHAR ( FECHA_SOL_ENVIO, 'dd/MM/yyyy') AS FEC_SOL_ENVIO," + 
                    "       NUM_INTENTOS        AS NUM_INTENT," +
                    "       RESPUESTA_LLAMADA   AS RESP_LLAMADA," +
                    "       RESULTADO," +
                    "       TO_CHAR ( FECHA_ACUSE, 'dd/MM/yyyy') AS FEC_ACUSE" +                     
                    "  FROM NOTIFICACION  "+
                    "INNER JOIN E_TML ON" +
                    "      E_TML.TML_PRO = NOTIFICACION.COD_PROCEDIMIENTO AND " +
                    "      E_TML.TML_TRA = NOTIFICACION.COD_TRAMITE "+
                    " WHERE 1=1";
            if (codProc!= null && !codProc.isEmpty()){
                query = query + "   AND COD_PROCEDIMIENTO = '"+ codProc +"'"; 
            }
            if (numExped!= null && !numExped.isEmpty()){
                query = query + "   AND NUM_EXPEDIENTE = '"+ numExped +"'" ;
            }
            if (fecDesde!= null && !fecDesde.isEmpty()){
                query = query + "   AND FECHA_ENVIO     >= TO_DATE ('"+ fecDesde + "','dd/mm/yyyy')";
            }     
            if (fecHasta!= null && !fecHasta.isEmpty()){
                query = query + "   AND FECHA_ENVIO     <= TO_DATE ('"+ fecHasta + "','dd/mm/yyyy')";
            }     
            if (resultado!= null && !resultado.isEmpty()){
                query = query + "   AND RESULTADO     = '"+ resultado +"'";
            }   
            query = query + " ORDER BY COD_MUNIC, EJERC, COD_PROC, NUM_EXPED, COD_NOTIF";  

            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while(rs.next())
            {
                notificaciones.add((FilaNotificacionVO)MeLanbide43MappingUtils.getInstance().map(rs, FilaNotificacionVO.class));
            }
            return notificaciones;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando notificaciones para el procedimiento "+codProc, ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
               
                if(st!=null) 
                    st.close();
                if(rs!=null)
                    rs.close();
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    
    public FilaNotificacionVO getNotificacion(String codNotif, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        FilaNotificacionVO notif = new FilaNotificacionVO();
        try
        {
            String query = null;
                query = "SELECT CODIGO_NOTIFICACION AS COD_NOTIF," +               
                    "       NUM_EXPEDIENTE      AS NUM_EXPED," +                 
                    "       COD_PROCEDIMIENTO   AS COD_PROC," +                 
                    "       EJERCICIO           AS EJERC," +                    
                    "       COD_MUNICIPIO       AS COD_MUNIC," +         
                    "       COD_TRAMITE         AS COD_TRAM," +
                    "       ' '                 AS DES_TRAM," +
                    "       OCU_TRAMITE         AS OCU_TRAM," +
                    "       ACTO_NOTIFICADO     AS ACT_NOTIF," +
                    "       CADUCIDAD_NOTIFICACION AS CAD_NOTIF," +
                    "       TEXTO_NOTIFICACION  AS TXT_NOTIF," +
                    "       FIRMADA             AS FIRMADA," +
                    "       FECHA_ENVIO         AS FEC_ENVIO," +
                    "       COD_NOTIFICACION_PLATEA  AS COD_NOTIF_PLATEA," +
                    "       FECHA_SOL_ENVIO     AS FEC_SOL_ENVIO," +
                    "       NUM_INTENTOS        AS NUM_INTENT," +
                    "       RESPUESTA_LLAMADA   AS RESP_LLAMADA," +
                    "       RESULTADO," +
                    "       FECHA_ACUSE         AS FEC_ACUSE" +
                    " FROM  NOTIFICACION " +
                    " WHERE CODIGO_NOTIFICACION  = " + codNotif + " ";

            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while(rs.next())
            {
                notif = (FilaNotificacionVO)MeLanbide43MappingUtils.getInstance().map(rs, FilaNotificacionVO.class);
            }
            return notif;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando notificacion " + codNotif, ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if(st!=null) 
                    st.close();
                if(rs!=null)
                    rs.close();
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    public int reenviarNotificacion(FilaNotificacionVO p, Connection con) throws Exception
    {
        Statement st = null;
        try
        {    
            String query = null;
                query = "UPDATE NOTIFICACION" 
                         +" SET   FECHA_ENVIO    = null, "
                         +"       NUM_INTENTOS   = null,  "
                         +"       RESPUESTA_LLAMADA   = ' ' "
                         +" WHERE CODIGO_NOTIFICACION   = "+p.getCodNotif();
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            return st.executeUpdate(query);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error modificando notificacion  "+(p != null ? p.getCodNotif() : "notif = null"), ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if(st!=null)
                    st.close();
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    
    //#233082 - Consulta y reenvio de notificaciones - fin
    
    public String getInsertarCodigoInerno(String codigointernodescrip, String numExpediente, Connection con) throws Exception {
        //destripamos el string que nos llega del jsp
        String mensaje ;
        //PRUEB -- 2 -- 2 -- DOS -- PROCEDIMIENTO DE PRUEBA
        log.debug("string = " + numExpediente); 
        //Sustituimos el codigo Interno pro el que recibimos .
        String codigoInterno=numExpediente.substring(9,10);
        codigoInterno.replace(codigoInterno,"");
        log.debug("string con replace= " + codigoInterno); 
        String codigoExterno=numExpediente.substring(14,15);
        log.debug("string = " + numExpediente);   
        String procedimiento=numExpediente.substring(20,3);
        log.debug("string = " + procedimiento);   
        String tramie=numExpediente.substring(24,numExpediente.length());
        log.debug("string = " + procedimiento);   
                
        
        Statement st = null;
        ResultSet rs = null;
        
        String procedimientomostrar;
        List<Tml> listTml  = new ArrayList<Tml>();
        Tml datosInformeInterno;
        
        try
        {

            String query ="";

            log.debug("SQL CREACION  = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            
            while(rs.next())
            {
                log.debug("fila informe");
                datosInformeInterno = (Tml)MeLanbide43MappingUtils.getInstance().map(rs, Tml.class);
                procedimientomostrar=datosInformeInterno.getTRA_COU()+""+ 
                        datosInformeInterno.getTRA_COD()+""+
                        datosInformeInterno.getTML_VALOR()+""+
                        datosInformeInterno.getTRA_PRO();
                        log.debug("String datos  = " + procedimientomostrar);
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando los datos del informe interno", ex);
            throw new Exception(ex);
        }
        finally
        {
            if(st!=null) 
                st.close();
            if(rs!=null) 
                rs.close();
        }
        return procedimiento;
    }

    public String CrearNuevoProcedimiento(String codigoint, String procedimiento, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        //int codigo=Integer.parseInt(codigoint);
        log.debug("Entramos en cargarPantallaPrincipal de " + codigoint);
        List<ProcedimientoSeleccionadoV0> listTramites  = new ArrayList<ProcedimientoSeleccionadoV0>();
        ProcedimientoSeleccionadoV0 datosInformeInterno = null;
        String procedimientomostrar = null;
           
        try
        {
            String query = "SELECT TRA_PRO,PR.PRO_DES PROCEDIMIENTO,TR.TRA_COD COD_TRAM_INTERNO,COD_FASE,DTR.TML_VALOR TRAMITE,DESC_FASE_C,DESC_FASE_E FROM E_TRA TR,E_PRO PR,E_TML DTR, \n" +
"melanbide43_fase ME WHERE PRO_COD =TRA_PRO AND TR.TRA_COD=DTR.TML_TRA AND TML_PRO   =PRO_COD AND PRO_COD   ='"+procedimiento+"' AND TRA_FBA  IS NULL AND me.cod_fase=TR.TRA_COD AND TR.TRA_COD='"+codigoint+"'ORDER BY PRO_DES,TRA_COU;";

            log.debug("SQL CREACION  = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            
            while(rs.next())
            {
                log.debug("fila informe");
                datosInformeInterno = (ProcedimientoSeleccionadoV0)MeLanbide43MappingUtils.getInstance().map(rs, ProcedimientoSeleccionadoV0.class);
                 procedimientomostrar=
                        datosInformeInterno.getPROCEDIMIENTO()+""+ 
                        datosInformeInterno.getTRA_PRO()+""+
                        datosInformeInterno.getCOD_TRAM_EXTERNO()+""+
                        datosInformeInterno.getCOD_TRAM_INTERNO()+""+
                        datosInformeInterno.getTRAMITE();
                        log.debug("String datos  = " + procedimientomostrar);
                        listTramites.add(datosInformeInterno);
                       // listTramites.add(procedimientomostrar);
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando los datos del informe interno", ex);
            throw new Exception(ex);
        }
        finally
        {
            if(st!=null) 
                st.close();
            if(rs!=null) 
                rs.close();
        }
        String finalprocedimiento = null;
        return finalprocedimiento;
    }

  public String obtenerUsuarioUltimoTramite (Connection con, String numExp)throws Exception{
        String usuario = "";
        Statement st = null;
        ResultSet rs = null;
        try
        {
            String query = null;
            
            query = "select CRO_USF from e_cro where cro_num LIKE '"+numExp+"' AND CRO_FEF IS NOT NULL ORDER BY CRO_FEF DESC" ;

            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            
            if(rs.next()){
                usuario = rs.getString("CRO_USF");
            }
        }catch(Exception ex){
            throw ex;
        }  
        finally
        {
            if(st!=null) 
                st.close();
            if(rs!=null) 
                rs.close();
        }
        return usuario;
    }
  
  public List<String> getExpedientesEnProcTramite( Connection con, String codProc, String codTramExterno) throws Exception{
       //Statement st = null;
       PreparedStatement ps =null;
       ResultSet rs = null;
       String resultado=null;
       List<String> expedientes = new ArrayList<String>();
       try
        {
            log.debug("getExpedientesEnProcTramite("+codProc+","+codTramExterno+")");
            
            StringBuffer sb = new StringBuffer("SELECT CRO_TRA,CRO_NUM, CRO_TRA,CRO_FEF ");
             sb.append(" FROM E_CRO   ");           
             sb.append(" WHERE CRO_PRO=?  AND CRO_FEF IS NULL  ");
             sb.append(" AND EXISTS (SELECT E_TRA.TRA_COU   ");
             sb.append(" FROM E_TML   ");
             sb.append(" INNER JOIN E_TRA ON TRA_COD=TML_TRA AND TRA_PRO=TML_PRO  ");
             sb.append(" WHERE TML_PRO=?   ");
             sb.append(" AND TRA_COU IN (?) AND TRA_FBA IS NULL  ");//6 PERIDODO ESPERA RECIBIR DOCU JUS 2PAGO
             sb.append(" AND CRO_TRA=TRA_COD AND CRO_PRO=TRA_PRO) ");
             //SE COMPRUEBA que no se haya dado de alta ya en platea
             sb.append(" and NOT EXISTS (SELECT * FROM MELANBIDE43_INTEGMISGEST WHERE EXP_NUM=CRO_NUM AND TIPO_OPERACION='I' AND RESULTADO_PROCESO=1) ");
            
        if(log.isDebugEnabled()) 
            log.debug("sql = " + sb.toString());
            //st = con.createStatement();
            ps = con.prepareStatement(sb.toString());
            
            ps.setString(1, codProc);  
            ps.setString(2, codProc); 
            ps.setString(3, codTramExterno);            
            rs = ps.executeQuery();
        
            while(rs.next())
            {                
                resultado=rs.getString("CRO_NUM");
                expedientes.add(resultado);
            }
        } catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando los expedientes", ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if(ps!=null) 
                    ps.close();
                if(rs!=null) 
                    rs.close();
            }catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);                
            }
        }
        
        return expedientes;       
   }
  
  
  public List<ExpTram> getExpedientesPendientesPlatea( Connection con, String codProc) throws Exception{
       //Statement st = null;
       PreparedStatement ps =null;
       ResultSet rs = null;
       String numExp=null;
       int codTram=0;
       int codTramInicInterno=1;
       int codTramInicExterno=11;
       List<ExpTram> expedientes = new ArrayList<ExpTram>();
       ExpTram expTram = new ExpTram();
       int codOrg = Integer.parseInt(ConfigurationParameter.getParameter("COD_ORG",ConstantesMeLanbide43.FICHERO_PROPIEDADES));
       
       try
        {
            log.debug("getExpedientesPendientesPlatea("+codProc+")");
            
            /*StringBuffer sb = new StringBuffer("SELECT CRO_TRA,CRO_NUM, CRO_FEF ");
             sb.append(" FROM E_CRO   ");           
             sb.append(" WHERE CRO_PRO=?  AND CRO_FEF IS NULL  ");        
             //SE COMPRUEBA que no se haya dado de alta ya en platea
             sb.append(" and NOT EXISTS (SELECT * FROM MELANBIDE43_INTEGMISGEST WHERE EXP_NUM=CRO_NUM AND TIPO_OPERACION='I' AND RESULTADO_PROCESO=1) ");
            */
            
            /*StringBuffer sb = new StringBuffer("SELECT CRO_TRA, CRO_NUM, CRO_FEF ");
             sb.append(" FROM E_CRO   "); 
             sb.append(" INNER JOIN E_EXP ON E_CRO.CRO_NUM = E_EXP.EXP_NUM ");
             sb.append(" INNER JOIN MELANBIDE43_LLAMAMISGEST ON E_EXP.EXP_PRO = MELANBIDE43_LLAMAMISGEST.COD_PROC ");
             sb.append(" WHERE CRO_PRO=?  AND CRO_FEF IS NULL  AND CRO_EJE IN (2016, 2017, 2018) ");        
             //SE COMPRUEBA que no se haya dado de alta ya en platea
             sb.append(" AND NOT EXISTS (SELECT * FROM MELANBIDE43_INTEGMISGEST WHERE EXP_NUM=CRO_NUM AND TIPO_OPERACION='I' AND RESULTADO_PROCESO=1) ");
             //SE COMPRUEBA que la fecha del expediente sea mayot que la fecha de la tabla MELANBIDE43_LLAMAMISGEST
             sb.append(" AND MELANBIDE43_LLAMAMISGEST.fecha < E_EXP.EXP_FEI ");*/
            
            /*SELECT CRO_TRA, CRO_NUM, CRO_FEF 
            FROM E_CRO    
            INNER JOIN E_EXP ON E_CRO.CRO_NUM = E_EXP.EXP_NUM 
            WHERE E_EXP.EXP_EST=0 -- expedientes
            AND CRO_PRO='SEI' 
            AND CRO_TRA=1
            AND CRO_EJE IN (2016, 2017, 2018)
            AND CRO_NUM NOT IN (SELECT MELANBIDE43_INTEGMISGEST.EXP_NUM FROM MELANBIDE43_INTEGMISGEST 
                                WHERE TIPO_OPERACION='I' 
                                AND RESULTADO_PROCESO=1
                                AND MELANBIDE43_INTEGMISGEST.EXP_NUM LIKE '%SEI%'
                                AND RES_EJE IN (2016, 2017, 2018))
            ;*/
            
            
            
            StringBuffer sb = new StringBuffer("SELECT CRO_TRA, CRO_NUM, CRO_FEF ");
            sb.append(" FROM E_CRO "); 
            sb.append(" INNER JOIN E_EXP ON E_CRO.CRO_NUM = E_EXP.EXP_NUM ");
            sb.append(" WHERE E_EXP.EXP_EST=0 AND CRO_PRO=? AND CRO_TRA=? AND CRO_EJE IN (2016, 2017, 2018) ");        
            //SE COMPRUEBA que no se haya dado de alta ya en platea
            sb.append(" AND CRO_NUM NOT IN (SELECT MELANBIDE43_INTEGMISGEST.EXP_NUM FROM MELANBIDE43_INTEGMISGEST ");
            sb.append(" WHERE TIPO_OPERACION='I' ");
            sb.append(" AND RESULTADO_PROCESO=1 ");
            sb.append(" AND MELANBIDE43_INTEGMISGEST.EXP_NUM LIKE '%SEI%' ");
            sb.append(" AND RES_EJE IN (2016, 2017, 2018)) ");
            
            codTramInicInterno = obtenerCodigoInternoTramiteInt(codOrg, codProc, codTramInicExterno, con);
              
        if(log.isDebugEnabled()) 
            log.debug("sql = " + sb.toString());
            //st = con.createStatement();
            ps = con.prepareStatement(sb.toString());
            
            ps.setString(1, codProc); 
            ps.setInt(2, codTramInicInterno);
            rs = ps.executeQuery();
        
            while(rs.next())
            {                
                numExp=rs.getString("CRO_NUM");
                codTram =rs.getInt("CRO_TRA");
                log.debug("numExp = " + numExp+". Tramite:"+codTram);
                expedientes.add(new ExpTram(numExp,codTram));
                //expedientes.add(expTram);
                //expedientes.add(resultado);
            }
        } catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando los expedientes", ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {            
                if(ps!=null) 
                    ps.close();
                if(rs!=null) 
                    rs.close();
            }catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);                
            }
        }
        
        return expedientes;       
   }
  
  public Long obtenerCodigoInternoTramite(Integer entorno, String proc, String codTramite, Connection con)throws Exception
    {
        Long cod=0L;
        Statement st = null;
        ResultSet rs = null;
        try
        {
            String sql = "select TRA_COD from E_TRA "
                        +" where TRA_MUN = "+entorno
                        +" and TRA_PRO = '"+proc+"'"
                        +" and TRA_COU = "+codTramite;

            if(log.isDebugEnabled()) 
                    log.debug("sql = " + sql);
            
            st = con.createStatement();
            rs = st.executeQuery(sql);
            if(rs.next())
            {
                cod = rs.getLong("TRA_COD");
                if(rs.wasNull())
                    cod = 0L;
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando el código interno del trámite : "+codTramite, ex);
            throw new Exception(ex);
        }
        finally
        {
                if(st!=null) 
                    st.close();
                if(rs!=null) 
                    rs.close();
        }
    	return cod;
    }
  
  public int obtenerCodigoInternoTramiteInt(Integer entorno, String proc, int codTramite, Connection con)throws Exception
    {
        int cod=0;
        Statement st = null;
        ResultSet rs = null;
        try
        {
            String sql = "select TRA_COD from E_TRA "
                        +" where TRA_MUN = "+entorno
                        +" and TRA_PRO = '"+proc+"'"
                        +" and TRA_COU = "+codTramite;

            if(log.isDebugEnabled()) 
                    log.debug("sql = " + sql);
            
            st = con.createStatement();
            rs = st.executeQuery(sql);
            if(rs.next())
            {
                cod = rs.getInt("TRA_COD");
                if(rs.wasNull())
                    cod = 0;
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando el código interno del trámite : "+codTramite, ex);
            throw new Exception(ex);
        }
        finally
        {
               if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                if(st!=null) 
                    st.close();
                if(rs!=null) 
                    rs.close();
        }
    	return cod;
    }

  /**
   * 
   * @param numExpediente
   * @param con
   * @return
   * @throws Exception 
   */
    public boolean comprobarExpGenerado(String numExpediente, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        int resultado;
        try
        {
            String sql = "select count(*) from melanbide43_integmisgest "
                        +" where tipo_operacion='I' "
                        +" and resultado_proceso=1 "
                        +" and EXP_NUM = '"+numExpediente+"'";

            if(log.isDebugEnabled()) 
                    log.debug("sql = " + sql);
            
            st = con.createStatement();
            rs = st.executeQuery(sql);
            rs.next();
            resultado = rs.getInt(1);
            
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error comprobando la generacion del Expediente : "+numExpediente, ex);
            throw new Exception(ex);
        }
        finally
        {
               if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                if(st!=null) 
                    st.close();
                if(rs!=null) 
                    rs.close();
        }
    	return resultado>0;
    }

    public Long obtenerCodigoTramiteFase(String codProc, String codFase, Connection con) throws Exception {
        Long cod=0L;
        Statement st = null;
        ResultSet rs = null;
        try
        {
            String sql = "select cod_tramite from melanbide43_fase "
                        + "where cod_proc='"+codProc+"' and cod_fase="+codFase;  

            if(log.isDebugEnabled()) 
                    log.debug("sql = " + sql);
            
            st = con.createStatement();
            rs = st.executeQuery(sql);
            if(rs.next())
            {
                cod = rs.getLong("cod_tramite");
                if(rs.wasNull())
                    cod = 0L;
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando el código del trámite del procedimiento: "+codProc+", fase: "+codFase, ex);
            throw new Exception(ex);
        }
        finally
        {
               if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                if(st!=null) 
                    st.close();
                if(rs!=null) 
                    rs.close();
        }
    	return cod;    
    }
    
    public boolean comprobarTerceroTramElectronica(String numExp, Connection con) throws Exception {
        boolean tramElectronica=false;
        Statement st=null;
        ResultSet rs=null;
        try
        {
            String sql = "SELECT * FROM E_EXT WHERE EXT_NUM='"+numExp+"' AND EXT_NOTIFICACION_ELECTRONICA=1";  

            if(log.isDebugEnabled()) 
                    log.debug("sql = " + sql);
            
            st = con.createStatement();
            rs = st.executeQuery(sql);
            if(rs.next())
            {
                tramElectronica=true;
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error al comprobar si tramitacion electronica:"+numExp, ex);
            throw new Exception(ex);
        }
        finally
        {
                if(st!=null) 
                    st.close();
                if(rs!=null) 
                    rs.close();
        }
        
        return tramElectronica;
    }
  
//COMENTAR PARA PRO PORQUE NO SUBIDO
//#233079 Mis gestiones - Mantenimiento tabla de fases    
     public void CrearNuevoProcedimiento(ProcedimientoSeleccionadoV0 procedimiento, Connection con) throws Exception {
        
        Statement st = null;
        ResultSet rs = null;
        int result = 0;
        boolean nuevo = true;
        int id = 0; 
        try
        {
            String query = null;
            query = "Insert into MELANBIDE43_FASE values ('"+procedimiento.getPROCEDIMIENTO()+"',"+procedimiento.getCOD_TRAM_INTERNO()+","+procedimiento.getCOD_FASE()+",'"+procedimiento.getDESC_FASE_C()+"','"+procedimiento.getDESC_FASE_E()+"')"; 
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            result = st.executeUpdate(query);
            
 
        }
        catch(Exception ex)
        {
           // insertaError(con, ex.toString(), id, "guardarConQuartz");
            //log.error("Se ha producido un error grabando el contador ("+tipoContador+")", ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if(st!=null) 
                    st.close();
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }

    }

    public void CrearNuevoProcedimiento(ProcedimientoSeleccionadoV0 procedimiento, String fecha, Connection con) throws Exception {
         Statement st = null;
        ResultSet rs = null;
        int result = 0;
        boolean nuevo = true;
        int id = 0; 
        try
        {
            String query = null;
            query = "Insert into MELANBIDE43_LLAMAMISGEST (COD_PROC,FECHA) values ('"+procedimiento.getPROCEDIMIENTO()+"','"+fecha+"')"; 
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            result = st.executeUpdate(query);
            
                    
        }
        catch(Exception ex)
        {
           // insertaError(con, ex.toString(), id, "guardarConQuartz");
            //log.error("Se ha producido un error grabando el contador ("+tipoContador+")", ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if(st!=null) 
                    st.close();
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    //FIN COMENTAR PARA PRO PORQUE NO SUBIDO
    
     /**
   * 
   * @param numExpediente
   * @param con
   * @return
   * @throws Exception 
   */
    public boolean comprobarJustificantePagos(String numExpediente, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        boolean resultado=false;
        try
        {

            String sql = "SELECT * FROM E_CRO "
                        +" INNER JOIN E_TRA ON TRA_PRO='CEMP' AND TRA_COD=CRO_TRA AND TRA_COU='41' AND CRO_FEF IS NOT NULL"
                        +" WHERE CRO_NUM= '"+numExpediente+"'";

            if(log.isDebugEnabled()) 
                    log.debug("sql = " + sql);
            
            st = con.createStatement();
            rs = st.executeQuery(sql);
            if(rs.next()){
                resultado=true;
            }
            
            
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error comprobando la generacion del Expediente : "+numExpediente, ex);
            throw new Exception(ex);
        }
        finally
        {
                if(st!=null) 
                    st.close();
                if(rs!=null) 
                    rs.close();
        }
    	return resultado;
    }
    
    public List<ExpAvisos> getExpedientesActualizarAvisos( Connection con) throws Exception{
       Statement st = null;
       ResultSet rs = null;
       String eje = null;
       String proc =null;
       String numExp=null;
       String tipoNotif=null;
       String mail=null;
       
       int codOrg = Integer.parseInt(ConfigurationParameter.getParameter("COD_ORG",ConstantesMeLanbide43.FICHERO_PROPIEDADES));
       
       List<ExpAvisos> expedientesAvisos = new ArrayList<ExpAvisos>();
       
       try
        {
            log.debug("getExpedientesActualizarAvisos");
                       
            /*String sql = "SELECT EXP_EJE EJERCICIO, "
                +" EXP_PRO PROCEDIMIENTO, "     
                +" EXP_NUM EXPEDIENTE, " 
                +" CASE WHEN EXT_NOTIFICACION_ELECTRONICA = 1 THEN 'ELECTRONICA' ELSE 'POSTAL' END AS TIPO_NOTIFICACION, " 
                +" TXT_VALOR EMAIL "
                +" FROM E_EXP " 
                +" INNER JOIN E_EXT ON EXP_NUM = EXT_NUM AND EXT_ROL=1 "
                +" LEFT JOIN E_TXT ON EXP_NUM = TXT_NUM AND TXT_COD='AVISOEMAIL' "
                +" WHERE EXP_PRO IN ('ORI14', 'CEMP', 'COLEC') AND EXP_EJE IN ('2015', '2017')  "
                +" and exp_num not in ('2015/COLEC/000002','2015/COLEC/000003','2015/COLEC/000016', " 
                +" '2015/COLEC/000017','2015/ORI14/000023','2015/ORI14/000027','2015/ORI14/000030', " 
                +" '2015/ORI14/000032','2015/ORI14/000047','2015/ORI14/000053','2015/ORI14/000054','2017/ORI14/000022') "    
                +" ORDER BY EXP_PRO, EXP_NUM ";*/   
            
            
            /*SELECT EXP_EJE EJERCICIO,
            EXP_PRO PROCEDIMIENTO,     
            EXP_NUM EXPEDIENTE, 
            CASE WHEN EXT_NOTIFICACION_ELECTRONICA = 1 THEN 'ELECTRONICA' ELSE 'POSTAL' END AS TIPO_NOTIFICACION, 
            TXT_VALOR EMAIL
            FROM E_EXP 
            INNER JOIN E_EXT ON EXP_NUM = EXT_NUM AND EXT_ROL=1
            LEFT JOIN E_TXT ON EXP_NUM = TXT_NUM AND TXT_COD='AVISOEMAIL'
            WHERE EXP_PRO IN ('SEI') 
            AND EXP_EJE IN (2016, 2017, 2018)
            AND EXP_EST=0
            ORDER BY EXP_PRO, EXP_NUM
            ;*/
            
          
            String sql = "SELECT EXP_EJE EJERCICIO, "
            +" EXP_PRO PROCEDIMIENTO, "     
            +" EXP_NUM EXPEDIENTE, " 
            +" CASE WHEN EXT_NOTIFICACION_ELECTRONICA = 1 THEN 'ELECTRONICA' ELSE 'POSTAL' END AS TIPO_NOTIFICACION, " 
            +" TXT_VALOR EMAIL "
            +" FROM E_EXP " 
            +" INNER JOIN E_EXT ON EXP_NUM = EXT_NUM AND EXT_ROL=1 "
            +" LEFT JOIN E_TXT ON EXP_NUM = TXT_NUM AND TXT_COD='AVISOEMAIL' "
            +" WHERE EXP_PRO IN ('SEI') AND EXP_EJE IN (2016, 2017, 2018) AND EXP_EST=0 "   
            +" ORDER BY EXP_PRO, EXP_NUM ";
            
            if(log.isDebugEnabled()) 
                    log.debug("sql = " + sql);
            
            st = con.createStatement();
            rs = st.executeQuery(sql);
            
        
            while(rs.next())
            {                
                eje = rs.getString("EJERCICIO");
                proc = rs.getString("PROCEDIMIENTO");
                numExp = rs.getString("EXPEDIENTE");
                tipoNotif = rs.getString("TIPO_NOTIFICACION");
                mail = rs.getString("EMAIL");
                
                
                expedientesAvisos.add(new ExpAvisos(eje, proc, numExp, tipoNotif, mail));

            }
        } catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando los expedientes", ex);
            throw new Exception(ex);
        }
        finally
        {
                if(st!=null) 
                    st.close();
                if(rs!=null) 
                    rs.close();            
        }
        
        return expedientesAvisos;       
   }


    public List<String> getExpedientesAperturaEsperaNuevoPersonal( Connection con) throws Exception{
       Statement st = null;
       ResultSet rs = null;       
       String numExp=null;
       
       List<String> expedientesApertura = new ArrayList<String>();
       
       try
        {
            log.debug("getExpedientesActualizarAvisos");
            
                       
            String sql = " SELECT CRO_NUM FROM E_CRO A " +
                            " INNER JOIN E_EXP ON EXP_NUM = CRO_NUM " +
                            " INNER JOIN E_TRA ON TRA_PRO = CRO_PRO AND CRO_TRA=TRA_COD AND TRA_COU IN (5) " +
                            " WHERE CRO_PRO IN ('ORI14', 'CEMP', 'COLEC') AND CRO_EJE IN ('2015', '2017') AND CRO_FEF IS NULL " +
                            " AND EXISTS( " +
                                " SELECT CRO_NUM FROM E_CRO B " +
                                " INNER JOIN E_EXP ON EXP_NUM = CRO_NUM " +
                                " INNER JOIN E_TRA ON TRA_PRO = CRO_PRO AND CRO_TRA=TRA_COD AND TRA_COU IN (400) " +
                                " WHERE B.CRO_NUM = A.CRO_NUM AND CRO_FEF IS NULL " +
                         ") ";
                               
        
             
             

            if(log.isDebugEnabled()) 
                    log.debug("sql = " + sql);
            
            st = con.createStatement();
            rs = st.executeQuery(sql);
            
        
            while(rs.next())
            {                
                numExp = rs.getString("CRO_NUM"); 
                
                expedientesApertura.add(numExp);

            }
        } catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando los expedientes", ex);
            throw new Exception(ex);
        }
        finally
        {
                if(st!=null) 
                    st.close();
                if(rs!=null) 
                    rs.close();          
            
        }
        
        return expedientesApertura;       
   }    
    
    
   public List<String> getExpedientesAvanceMisGestiones( Connection con) throws Exception{
       Statement st = null;
       ResultSet rs = null;       
       String numExp=null;
       
       List<String> expedientesAvance = new ArrayList<String>();
       
       try
        {
            log.debug("getExpedientesAvanceMisGestiones");
            
                       
            String sql = " SELECT NUM_EXP FROM CEPAP_MIGRACION_EXCEL \n" +
                            " INNER JOIN E_CRO ON CRO_NUM = NUM_EXP AND CRO_TRA="+ConfigurationParameter.getParameter(ConstantesMeLanbide43.TRAM_CONCM_RESOLUCION, ConstantesMeLanbide43.FICHERO_PROPIEDADES)+" AND CRO_FEF IS NULL \n" +
                            " WHERE NUM_TAREA=310390 AND VIA_SOLICITUD=0";                             
        
             
             

            if(log.isDebugEnabled()) 
                    log.debug("sql = " + sql);
            
            st = con.createStatement();
            rs = st.executeQuery(sql);
            
        
            while(rs.next())
            {                
                numExp = rs.getString("NUM_EXP"); 
                
                expedientesAvance.add(numExp);

            }
        } catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando los expedientes", ex);
            throw new Exception(ex);
        }
        finally
        {
                if(st!=null) 
                    st.close();
                if(rs!=null) 
                    rs.close();          
            
        }
        
        return expedientesAvance;       
   } 
   
   
    public int modificarExpedienteAvanceMisGestiones(String numExpediente, String observaciones, Connection con) throws Exception{
       Statement st = null;
       int result = 0;      

       
       try
        {
            log.debug("modificarExpedienteAvanceMisGestiones");
            
                       
            String sql = " UPDATE CEPAP_MIGRACION_EXCEL \n" +
                            " SET VIA_SOLICITUD=1, OBSERVACIONES_EXPEDIENTE='"+observaciones+"' \n" +
                            " WHERE NUM_EXP='"+numExpediente+"' AND NUM_TAREA=310390 AND VIA_SOLICITUD=0";                             
        
                       

            if(log.isDebugEnabled()) 
                    log.debug("sql = " + sql);
                st = con.createStatement();
                result = st.executeUpdate(sql);

            
            
            
        
            
        } catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando los expedientes", ex);
            throw new Exception(ex);
        }
        finally
        {
                if(st!=null) 
                    st.close();       
            
        }
       return result;
     
   }
   
    public ArrayList<Participantes> leerListaParticipantesRepresentantesRdRxExp(String numExp, int codRolRepre,Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        Participantes exp = new Participantes();
        ArrayList<Participantes> parti = new ArrayList<Participantes>();
        try {
            String query = null;
            query = "SELECT EXT_NUM"
                    + ",HTE_DOC,HTE_NOM,HTE_AP1,HTE_AP2,HTE_TLF,HTE_DCE,HTE_NOC,HTE_TID "
                    + ", DNN_DMC,  PAI_COD, PRV_COD, MUN_COD, VIA_COD "
                    + ", PAI_NOM, PRV_NOM, MUN_NOM, VIA_NOM, DNN_LED, DNN_NUD, EXT_ROL  "
                    + " FROM E_EXT "
                    + " INNER JOIN T_HTE ON EXT_TER = HTE_TER AND HTE_NVR=EXT_NVR"
                    + " LEFT JOIN T_DOT ON EXT_DOT = DOT_DOM AND HTE_TER = DOT_TER  "
                    + " LEFT JOIN T_DNN ON DNN_DOM = DOT_DOM "
                    + " LEFT JOIN FLBGEN.T_PAI ON PAI_COD = DNN_PAI "
                    + " LEFT JOIN FLBGEN.T_PRV ON PRV_PAI = PAI_COD AND PRV_COD = DNN_PRV "
                    + " LEFT JOIN FLBGEN.T_MUN ON MUN_PAI = PAI_COD AND MUN_PRV = PRV_COD AND MUN_COD = DNN_MUN "
                    + " LEFT JOIN T_VIA ON VIA_PAI = PAI_COD AND VIA_PRV = PRV_COD AND VIA_MUN = MUN_COD AND VIA_COD = DNN_VIA "
                    + " WHERE EXT_NUM = '" + numExp + "' AND EXT_ROL = "+codRolRepre
                    ;
            
            log.debug("sql = " + query);
            
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                exp.setNumExp(rs.getString("EXT_NUM"));
                exp.setNif(rs.getString("HTE_DOC"));
                exp.setNombre(rs.getString("HTE_NOM"));
                exp.setApe1(rs.getString("HTE_AP1"));
                exp.setApe2(rs.getString("HTE_AP2"));
                exp.setTlf(rs.getString("HTE_TLF"));
                exp.setMail(rs.getString("HTE_DCE"));
                exp.setNomC(rs.getString("HTE_NOC"));
                exp.setTipoID(rs.getInt("HTE_TID"));
                exp.setIdPais(rs.getString("PAI_COD"));
                exp.setIdProv(rs.getString("PRV_COD"));
                exp.setIdMuni(rs.getString("MUN_COD"));
                exp.setIdCalle(rs.getString("VIA_COD"));
                exp.setPais(rs.getString("PAI_NOM"));
                exp.setProv(rs.getString("PRV_NOM"));
                exp.setMuni(rs.getString("MUN_NOM"));
                exp.setCalle(rs.getString("VIA_NOM"));
                exp.setNum(rs.getString("DNN_NUD"));
                exp.setLetra(rs.getString("DNN_LED"));
                exp.setRol(rs.getString("EXT_ROL"));
                parti.add(exp);
                exp = new Participantes();
            }
        } catch (Exception ex) {
            log.error("Error en leerDatosParticipantes " + ex.getMessage(), ex);
            throw  ex;
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return parti;
    }

    //Datos basicos buscados por NIF no asociado a un expediente por tanto buscamos en T_TER
    public TercerosValueObject getDatosBasicosterceroRepreRdR(String nif, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        TercerosValueObject tercero = new TercerosValueObject();
        try {
            String query = null;
            query = "SELECT TER_DOC, TER_NOM, TER_AP1, TER_AP2, " +
                    " TER_TLF, TER_DCE, TER_NOC, TER_TID, TER_COD,TER_NVE, DOT_DOM as TER_DOM " +
                    " ,DNN_DMC " +
                    " ,PAI_COD, PRV_COD, MUN_COD, VIA_COD  " +
                    " ,PAI_NOM, PRV_NOM, MUN_NOM, VIA_NOM, DNN_LED, DNN_NUD " +
                    " FROM T_TER  " +
                    " LEFT JOIN T_DOT ON  TER_COD = DOT_TER  " +    // TER_DOM = DOT_DOM AND
                    " LEFT JOIN T_DNN ON DNN_DOM = DOT_DOM " +
                    " LEFT JOIN FLBGEN.T_PAI ON PAI_COD = DNN_PAI " +
                    " LEFT JOIN FLBGEN.T_PRV ON PRV_PAI = PAI_COD AND PRV_COD = DNN_PRV " +
                    " LEFT JOIN FLBGEN.T_MUN ON MUN_PAI = PAI_COD AND MUN_PRV = PRV_COD AND MUN_COD = DNN_MUN " +
                    " LEFT JOIN T_VIA ON VIA_PAI = PAI_COD AND VIA_PRV = PRV_COD AND VIA_MUN = MUN_COD AND VIA_COD = DNN_VIA " +
                    " WHERE TER_DOC = '"+nif+"'"
                    // Ordenamos los que no tienen domcilio al inicio, luego el domicilio principal en caso de que hayan mas de uno, evitamos error null pointer en insertarHIstirico, ï¿½
                    // luego ordenamos por fecha de alta y numero de codigo y version
                    + " ORDER BY (case when ter_dom is null then 0 else 1 end) asc, DOT_DPA  asc nulls first , TER_FAL ASC,ter_cod asc,ter_nve asc "
                    ;
            log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                tercero.setIdentificador(rs.getString("TER_COD"));
                tercero.setCodTerceroOrigen(rs.getString("TER_COD"));
                tercero.setVersion(rs.getString("TER_NVE"));
                // Ponemos -1 porque al insertar datos en el historico de operaciones falla si el domicilio es null.
                tercero.setDomPrincipal((rs.getString("TER_DOM")!= null && !rs.getString("TER_DOM").isEmpty() ? rs.getString("TER_DOM"): "-1"));
                tercero.setIdDomicilio((rs.getString("TER_DOM")!= null && !rs.getString("TER_DOM").isEmpty() ? rs.getString("TER_DOM"): "-1"));
                tercero.setNombre(rs.getString("TER_NOM"));
                tercero.setApellido1(rs.getString("TER_AP1"));
                tercero.setApellido2(rs.getString("TER_AP2"));
                tercero.setTipoDocumento(rs.getString("TER_TID"));
                tercero.setDocumento(rs.getString("TER_DOC"));
                tercero.setTelefono(rs.getString("TER_TLF"));
                tercero.setEmail(rs.getString("TER_DCE"));
                tercero.setNombreCompleto(rs.getString("TER_NOC"));
            }
        } catch (Exception ex) {
            log.error("Error en getDatosBasicosterceroRepreRdR " + ex.getMessage(), ex);
            throw ex;
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return tercero;
    }

    public boolean insertarRepresentanteLegalExpedienteRdR(TercerosValueObject tercero, int codigoOrganizacion, String numExpediente, String codProcedimiento,int codRolRepreProcedimientoInt,Connection con) throws Exception {
        Statement st = null;
        int result = 0;
        try {
            String query = null;
            int ejercicio = (numExpediente!=null && !numExpediente.isEmpty()? Integer.valueOf(numExpediente.substring(0,4)) : 0);
            query = "INSERT INTO E_EXT (EXT_MUN,EXT_EJE,EXT_NUM,EXT_TER,EXT_NVR,EXT_DOT,EXT_ROL,EXT_PRO,MOSTRAR,EXT_NOTIFICACION_ELECTRONICA) "
                    + "  values( " + codigoOrganizacion + ", " + ejercicio + ", '" + numExpediente + "', " + tercero.getIdentificador()
                    + ", " + tercero.getVersion()+ ", " + (tercero.getDomPrincipal() != null && !tercero.getDomPrincipal().isEmpty() ? tercero.getDomPrincipal() : 0)
                    + ", " + codRolRepreProcedimientoInt + ", '"+ codProcedimiento +"', 1, '1')";
            log.debug("sql = " + query);
            st = con.createStatement();
            result = st.executeUpdate(query);
            if(result>0)
                return true;
            else
                return false;

        } catch (Exception exc) {
            log.error("Error al realizar la insercion del ROL REPRESENTANTE en el expediente " + numExpediente , exc);
            throw exc;
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    public boolean eliminarRepresentanteLegalExpedienteRdR(TercerosValueObject tercero, int codOrganizacion, String numExpediente, String codProcedimiento, int codRolRepreProcedimientoInt, Connection con) throws Exception {
        Statement st = null;
        int result = 0;
        try {
            String query = null;
            int ejercicio = (numExpediente != null && !numExpediente.isEmpty() ? Integer.valueOf(numExpediente.substring(0, 4)) : 0);
            query = "DELETE FROM E_EXT "
                    + " WHERE "
                    + " EXT_MUN="+codOrganizacion
                    + " AND EXT_EJE="+ejercicio
                    + " AND EXT_PRO='"+codProcedimiento+"'"
                    + " AND EXT_NUM='" +numExpediente +"'"
                    + " AND EXT_TER="+(tercero!=null && tercero.getIdentificador()!= null && !tercero.getIdentificador().isEmpty() ? tercero.getIdentificador(): "null")
                    + " AND EXT_NVR="+(tercero!=null && tercero.getVersion()!= null && !tercero.getVersion().isEmpty() ? tercero.getVersion(): "null")
                    + " AND EXT_ROL="+codRolRepreProcedimientoInt
                    ;
            log.debug("sql = " + query);
            st = con.createStatement();
            result = st.executeUpdate(query);
            if (result > 0) {
                return true;
            } else {
                return false;
            }

        } catch (Exception exc) {
            log.error("Error al eliminar el ROL REPRESENTANTE en el expediente " + numExpediente, exc);
            throw exc;
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    public boolean existeNIFRepresentanteLegalEnExpedienteRdR(String lan6ApoderadoTypeID, int codOrganizacion, int codRolRepreProcedimientoInt, String codProcedimiento, String numExpediente, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        boolean result = false;
        try {
            String query = null;
            int ejercicio = (numExpediente != null && !numExpediente.isEmpty() ? Integer.valueOf(numExpediente.substring(0, 4)) : 0);
            query = " SELECT COUNT(1) EXISTE "
                    + " FROM E_EXT "
                    + " LEFT JOIN T_HTE ON EXT_TER=HTE_TER AND EXT_NVR=HTE_NVR "
                    + " WHERE "
                    + " EXT_MUN=" + codOrganizacion
                    + " AND EXT_EJE=" + ejercicio
                    + " AND EXT_PRO='" + codProcedimiento + "'"
                    + " AND EXT_NUM='" + numExpediente + "'"
                    + " AND EXT_ROL=" + codRolRepreProcedimientoInt
                    + " AND HTE_DOC='" + lan6ApoderadoTypeID + "'"
                    ;
            log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                int res = rs.getInt("EXISTE");
                if(res>0){
                    result=true;
                }
            }
        } catch (Exception exc) {
            log.error("Error al cmprobar si un NIF esta dadode alta como representante legal en un expediente" + numExpediente, exc);
            throw exc;
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return result;
    }
    
    // poR BUSCARSE CONTRA E_EXT aputamos al historico de teceros
    public TercerosValueObject getDatosTerceroRdRxNifExpteRol(String numExpediente, String nif,Integer rol, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        TercerosValueObject tercero = new TercerosValueObject();
        try {
            String query = null;
            query = "SELECT "
                    + " HTE_DOC, HTE_NOM, HTE_AP1, HTE_AP2,HTE_TLF, HTE_DCE, HTE_NOC, HTE_TID, HTE_TER,HTE_NVR,HTE_DOT  "
                    + " ,DNN_DMC "
                    + " ,PAI_COD, PRV_COD, MUN_COD, VIA_COD  "
                    + " ,PAI_NOM, PRV_NOM, MUN_NOM, VIA_NOM, DNN_LED, DNN_NUD "
                    + " ,EXT_ROL, MOSTRAR, EXT_NOTIFICACION_ELECTRONICA "
                    + " FROM E_EXT  "
                    + " LEFT JOIN T_HTE ON EXT_TER=HTE_TER AND EXT_NVR=HTE_NVR  "
                    + " LEFT JOIN T_DOT ON HTE_DOT = DOT_DOM AND HTE_TER = DOT_TER    "
                    + " LEFT JOIN T_DNN ON DNN_DOM = DOT_DOM  "
                    + " LEFT JOIN FLBGEN.T_PAI ON PAI_COD = DNN_PAI "
                    + " LEFT JOIN FLBGEN.T_PRV ON PRV_PAI = PAI_COD AND PRV_COD = DNN_PRV "
                    + " LEFT JOIN FLBGEN.T_MUN ON MUN_PAI = PAI_COD AND MUN_PRV = PRV_COD AND MUN_COD = DNN_MUN "
                    + " LEFT JOIN T_VIA ON VIA_PAI = PAI_COD AND VIA_PRV = PRV_COD AND VIA_MUN = MUN_COD AND VIA_COD = DNN_VIA "
                    + " WHERE "
                    + " EXT_NUM='"+numExpediente+"'"
                    + " AND EXT_ROL=" + (rol != null ? rol : -1)
                    + " AND hte_DOC = '" + nif + "'"
                    // Ordenamos los que no tienen domcilio al inicio, luego el domicilio principal en caso de que hayan mas de uno, evitamos error null pointer en insertarHIstirico, ï¿½
                    // luego ordenamos por fecha de alta y numero de codigo y version
                    + " ORDER BY (case when hte_dot is null then 0 else 1 end) asc, DOT_DPA  asc nulls first , HTE_FOP ASC,hte_ter asc,HTE_nvr asc ";
            log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                tercero.setIdentificador(rs.getString("HTE_TER"));
                tercero.setCodTerceroOrigen(rs.getString("HTE_TER"));
                tercero.setVersion(rs.getString("HTE_NVR"));
                tercero.setDomPrincipal((rs.getString("HTE_DOT") != null && !rs.getString("HTE_DOT").isEmpty() ? rs.getString("HTE_DOT") : "-1"));
                tercero.setIdDomicilio((rs.getString("HTE_DOT") != null && !rs.getString("HTE_DOT").isEmpty() ? rs.getString("HTE_DOT") : "-1"));
                tercero.setNombre(rs.getString("HTE_NOM"));
                tercero.setApellido1(rs.getString("HTE_AP1"));
                tercero.setApellido2(rs.getString("HTE_AP2"));
                tercero.setTipoDocumento(rs.getString("HTE_TID"));
                tercero.setDocumento(rs.getString("HTE_DOC"));
                tercero.setTelefono(rs.getString("HTE_TLF"));
                tercero.setEmail(rs.getString("HTE_DCE"));
                tercero.setNombreCompleto(rs.getString("HTE_NOC"));
               // tercero.setCodRol(rs.getInt("EXT_ROL") );
                //tercero.setNotificacionElectronica(rs.getString("EXT_NOTIFICACION_ELECTRONICA"));
                // Ponemos -1 prque al insertar datos en el historico de operaciones falla si el domicilio es null.
            }
        } catch (Exception ex) {
            log.error("Error en getDatosBasicosterceroRepreRdR " + ex.getMessage(), ex);
            throw ex;
        } finally {
            log.debug("Procedemos a cerrar el statement y el resultset");
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return tercero;
    }
    
    /***
     * 
     * @param numExpediente
     * @param codCampo Codiogo Campo suplementario para avisos : AVISOEMAIL o AVISOSMS
     * @param con
     * @return texto del campo suplementario para avisos electronicos segun el codigo recibido : AVISOEMAIL o AVISOSMS
     * @throws Exception 
     */
    public String  getDatosNotificacionElectronicaAVISOSxExptexCodCampo(String numExpediente, String codCampo, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String valorCampo = "";
        try {
            String query = null;
            query = "SELECT TXT_VALOR "
                    + " FROM E_TXT "
                    + " WHERE TXT_NUM='"+numExpediente+"'"
                    + " AND TXT_COD='" + codCampo + "'"
                    ;
            log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                valorCampo= rs.getString("TXT_VALOR");
            }
        } catch (Exception ex) {
            log.error("Error en getDatosNotificacioElectronicaAVISOSxExptexCodCampo " + ex.getMessage(), ex);
            throw ex;
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        log.debug("getDatosNotificacioElectronicaAVISOSxExptexCodCampo : "+ numExpediente +" - "+  valorCampo);
        return valorCampo;
    }
    
    /***
     * Actualiza el valor de un campo suplementario de tipo texto segun codigo y numero de expediente. Elimina el valor existente si lo hay e inserta el nuevo
     * @param codOrganizacion
     * @param numExpediente
     * @param codCampo
     * @param nuevoValorCampo
     * @param con
     * @return true si las operaciones se ejecutaron correctamente o false si no se inserta ningun valor o se presenta un error
     * @throws Exception 
     */
    public boolean  updateDatosNotificacionElectronicaAVISOSxExptexCodCampo(int codOrganizacion, String numExpediente, String codCampo, String nuevoValorCampo, Connection con) throws Exception {
        Statement st = null;
        Statement st2 = null;        
        int result = 0;
        try {
            String query = null;
            // Quitamos los datos actuales si hay
            int ejercicio = (numExpediente != null && !numExpediente.isEmpty() ? Integer.valueOf(numExpediente.substring(0, 4)) : 0);
            query = "DELETE FROM E_TXT "
                    + " WHERE "
                    + " TXT_MUN=" + codOrganizacion
                    + " AND TXT_EJE=" + ejercicio
                    + " AND TXT_NUM='" + numExpediente + "'"
                    + " AND TXT_COD='" + codCampo + "'"
                    ;
            log.debug("sql Borramos para actualizar = " + query);
            st = con.createStatement();
            result = st.executeUpdate(query);
            log.debug("sql updateDatosNotificacionElectronicaAVISOSxExptexCodCampo - result delete = " + result);
            // Ahora insertamos el nuevo dato. 
            query="INSERT   INTO E_TXT "
                    + "VALUES ("+
                    codOrganizacion
                    + "," + ejercicio
                    + ",'" + numExpediente +"'"
                    + ",'" + codCampo +"'"
                    + ",'" + nuevoValorCampo +"'"
                    + ") "
                    ;
            log.debug("sql Insertamos  = " + query);
            st2 = con.createStatement();
            result = st2.executeUpdate(query);
            log.debug("sql result updateDatosNotificacionElectronicaAVISOSxExptexCodCampo - Insercion = " + result);
            return result > 0;

        } catch (Exception exc) {
            log.error("updateDatosNotificacionElectronicaAVISOSxExptexCodCampo - Error al actualizar los datos de Aviso en el expediente " + numExpediente, exc);
            throw exc;
        } finally {
            if (st != null) {
                st.close();
            }
            if (st2 != null) {
                st2.close();
            }
        }
    }
    /**
     * Elimina los datos de notificacion de acuerdo al codigo de campo suplementario recibido, Solo actua sobre E_TXT
     * @param codOrganizacion
     * @param numExpediente
     * @param codCampo
     * @param con
     * @return
     * @throws Exception 
     */
    public boolean  borarDatosNotificacionElectronicaAVISOSxExptexCodCampo(int codOrganizacion, String numExpediente, String codCampo, Connection con) throws Exception {
        Statement st = null;
       int result = 0;
        try {
            int ejercicio = MELANBIDE43_GestionRdR_Util.getInstance().getEjercicioDsdNumExpediente(numExpediente);
            String query = "DELETE FROM E_TXT "
                    + " WHERE "
                    + " TXT_MUN=" + codOrganizacion
                    + " AND TXT_EJE=" + ejercicio
                    + " AND TXT_NUM='" + numExpediente + "'"
                    + " AND TXT_COD='" + codCampo + "'"
                    ;
            log.debug("sql Borramos = " + query);
            st = con.createStatement();
            result = st.executeUpdate(query);
            log.debug("sql result borarDatosNotificacionElectronicaAVISOSxExptexCodCampo  = " + result);
            return result > 0;
        } catch (SQLException exc) {
            log.error("borarDatosNotificacionElectronicaAVISOSxExptexCodCampo - SQLException - Error al eliminar los datos de Aviso en el expediente " + numExpediente, exc);
            throw exc;
        }catch (Exception exc) {
            log.error("borarDatosNotificacionElectronicaAVISOSxExptexCodCampo - Exception - Error al eliminar los datos de Aviso en el expediente " + numExpediente, exc);
            throw exc;
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }
    
    //devuelve Integer > 0 si es Desarrollo 0 (si la fecha de creaci?n del expediente est? entre las 2 fechas definidas en MELANBIDE43_LLAMAMISGEST, ambas incluidas)
    public Integer esDesarrolloCero(String numExp, Connection con) throws Exception {

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
             
            if(log.isDebugEnabled()) log.debug(sql);
            
            st = con.createStatement();
            rs = st.executeQuery(sql);

            while(rs.next()){
                resultado=rs.getInt("CONT");
            }
        }catch(Exception ex){
            log.error("Error en esDesarrolloCero " + ex.getMessage(), ex);
            throw ex;
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
   
    public List<FilaLlamadasMisGestVO>  selectLlamadasMisGestiones(String numExped, String fecDesde, String fecHasta, String dniTercero, String codProcedimiento,Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        List<FilaLlamadasMisGestVO> llamadasMisGest = new ArrayList<FilaLlamadasMisGestVO>();
        try
        {
            String query = null;
            query = "SELECT ID, " +               
                    " EXP_NUM AS NUM_EXPED," + 
                    "       TER_TID," +
                    "       TER_DOC," +                 
                    "       EXP_TIPO, " +                    
                    "       TIPO_OPERACION, " +         
                    "       COD_TRAMITE_INICIO, " +
                    "       TO_CHAR ( FECHA_GENERADO, 'dd/MM/yyyy') AS FECHA_GENERADO ," +
                    "       TO_CHAR ( FECHA_PROCESADO, 'dd/MM/yyyy') AS FECHA_PROCESADO," +
                    "       RESULTADO_PROCESO," +
                    "       RES_EJE," +
                    "       RES_NUM," +
                  //  "       RESPUESTA_LLAMADA," +             
                    "       FECHA_TELEMATICO," +               
                    "       REG_TELEMATICO," +
                    "       NUM_INTENTOS        AS NUM_INTENT " +                     
                    " FROM MELANBIDE43_INTEGMISGEST   "+
                    " WHERE 1=1 and rownum < 1001  " ;
            /*if (!codProc.equals(null) & !codProc.equals("")){
                query = query + "   AND COD_PROCEDIMIENTO = '"+ codProc +"'"; 
            }*/
            if (numExped!=null && !numExped.isEmpty()){
                query = query + "   AND EXP_NUM = '"+ numExped +"'" ;
            }
            if (fecDesde != null && !fecDesde.isEmpty()){
                query = query + "   AND FECHA_GENERADO >= TO_DATE ('"+ fecDesde + "','dd/mm/yyyy')";
            }     
            if (fecHasta !=null && !fecHasta.isEmpty()){
                query = query + "   AND TO_DATE(TO_CHAR(FECHA_GENERADO,'dd/MM/yyyy'),'dd/MM/yyyy') <= TO_DATE ('"+ fecHasta + "','dd/mm/yyyy')";
            }     
            if (dniTercero != null && !dniTercero.isEmpty()){
                query = query + "   AND upper(TER_DOC) = '"+ dniTercero +"'";
            }   
            if (codProcedimiento != null && !codProcedimiento.isEmpty()){
                query = query + "   AND upper(EXP_TIPO) = '"+ codProcedimiento +"'";
            }   
            query = query + " ORDER BY ID DESC ";  

            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while(rs.next())
            {
                llamadasMisGest.add((FilaLlamadasMisGestVO)MeLanbide43MappingUtils.getInstance().map(rs, FilaLlamadasMisGestVO.class));
            }
            return llamadasMisGest;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando llamadas "+numExped, ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                if(st!=null) 
                    st.close();
                if(rs!=null)
                    rs.close();
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    
    public FilaLlamadasMisGestVO getLlamada(String numExped, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        FilaLlamadasMisGestVO llamada = new FilaLlamadasMisGestVO();
        try
        {
            String query = null;
                query = "SELECT ID, " +               
                    " EXP_NUM AS NUM_EXPED," + 
                    "       TER_TID," +
                    "       TER_DOC," +                 
                    "       EXP_TIPO, " +                    
                    "       TIPO_OPERACION, " +         
                    "       COD_TRAMITE_INICIO, " +
                    "       TO_CHAR ( FECHA_GENERADO, 'dd/MM/yyyy') AS FECHA_GENERADO ," +
                    "       TO_CHAR ( FECHA_PROCESADO, 'dd/MM/yyyy') AS FECHA_PROCESADO," +
                    "       RESULTADO_PROCESO," +
                    "       RES_EJE," +
                    "       RES_NUM," +
                  //  "       RESPUESTA_LLAMADA," +             
                    "       FECHA_TELEMATICO," +               
                    "       REG_TELEMATICO," +
                    "       NUM_INTENTOS        AS NUM_INTENT " +                     
                    " FROM MELANBIDE43_INTEGMISGEST   "+
                    " WHERE NUM_EXPED  = " + numExped + " ";

            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while(rs.next())
            {
                llamada = (FilaLlamadasMisGestVO)MeLanbide43MappingUtils.getInstance().map(rs, FilaLlamadasMisGestVO.class);
            }
            return llamada;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando llamada " + numExped, ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                if(st!=null) 
                    st.close();
                if(rs!=null)
                    rs.close();
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    
    //
    public List<FilaLlamadasMisGestVO> getListadoLlamadasMisGestExcel(Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<FilaLlamadasMisGestVO> llamadas = new ArrayList<FilaLlamadasMisGestVO>();
        try
        {
            String query = "SELECT ID, " +               
                    " EXP_NUM AS NUM_EXPED," + 
                    "       TER_TID," +
                    "       TER_DOC," +                 
                    "       EXP_TIPO, " +                    
                    "       TIPO_OPERACION, " +         
                    "       COD_TRAMITE_INICIO, " +
                    "       TO_CHAR ( FECHA_GENERADO, 'dd/MM/yyyy') AS FECHA_GENERADO ," +
                    "       TO_CHAR ( FECHA_PROCESADO, 'dd/MM/yyyy') AS FECHA_PROCESADO," +
                    "       RESULTADO_PROCESO," +
                    "       RES_EJE," +
                    "       RES_NUM," +
                  //  "       RESPUESTA_LLAMADA," +             
                    "       FECHA_TELEMATICO," +               
                    "       REG_TELEMATICO," +
                    "       NUM_INTENTOS        AS NUM_INTENT " +                     
                    " FROM MELANBIDE43_INTEGMISGEST   "+
                    " ORDER BY ID";

            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while(rs.next())
            {
                llamadas.add((FilaLlamadasMisGestVO)MeLanbide43MappingUtils.getInstance().map(rs, FilaLlamadasMisGestVO.class));
            }
            return llamadas;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando las llamadas a mis gestiones ", ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if(st!=null) 
                    st.close();
                if(rs!=null)
                    rs.close();
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    
    public List<ProcedimientoVO> getComboProcedimiento(int codOrganizacion, Connection con) throws Exception {
        log.info("getComboNisaeProcedimiento - Begin () " + formatDateLog.format(new Date()));
        List<ProcedimientoVO> retorno = new ArrayList<ProcedimientoVO>();
        PreparedStatement pt = null;
        ResultSet rs = null;
        try {
            String query = " SELECT PRO_COD, PRO_DES "
                    + " FROM E_PRO "
                    + " WHERE PRO_EST=1 "
                    + " AND PRO_MUN=? "
                    + " ORDER BY PRO_COD ";

            log.debug("sql = " + query);
            pt = con.prepareStatement(query);
            pt.setInt(1, codOrganizacion);
            log.debug("Param ? : " + codOrganizacion);
            rs = pt.executeQuery();
            while (rs.next()) {
                ProcedimientoVO elementoListaRetorno = new ProcedimientoVO();
                elementoListaRetorno.setCodProc(rs.getString("PRO_COD"));
                elementoListaRetorno.setDescProc(rs.getString("PRO_DES"));
                retorno.add(elementoListaRetorno);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recoger combos ... ", ex);
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
        log.debug("getComboProcedimiento - End () " + formatDateLog.format(new Date()));
        return retorno;
    }
    
    public int actualizarProcesadoActInteresados(int id, Connection con) throws Exception
    {
        Statement st = null;
        int result = 0;
        try
        {
            String query = null;
            
            query = "UPDATE MELANBIDE43_INTEGMISGEST"
                + " SET FECHA_PROCESADO = to_date(to_char(sysdate,'dd/mm/yyyy HH24:mi:ss'), 'dd/mm/yyyy HH24:mi:ss'), "
                + " TER_DOC = (\n" +
                    "select RTRIM (XMLAGG(XMLELEMENT (E,\n" +
                    " 'ROL=' || ext_rol || ', ',\n" +
                    " 'DOC=' || hte_doc || ', ',\n" +
                    " 'NOM=' || hte_noc || ', ',\n" +
                    " 'MARCA_NOT=' || ext_notificacion_electronica || ', ',\n" +
                    " 'COD_TER=' || ext_ter || ', ',\n" +
                    " 'NVR=' || ext_nvr || ', ',\n" +
                    " 'DOM=' || ext_dot || ' '\n" +
                    "|| CHR(13))).EXTRACT ('//text()').GETCLOBVAL(),',') INTERESADOS \n" +
                    "from e_ext \n" +
                    "inner join t_hte on t_hte.hte_ter=e_ext.ext_ter and t_hte.hte_nvr=e_ext.ext_nvr\n" +
                    "where ext_num=(select exp_num from MELANBIDE43_INTEGMISGEST where id=" + id + ")\n" +
                    "), "
                + " RESULTADO_PROCESO = '1' WHERE ID =  " + id;

            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            result = st.executeUpdate(query);
        }
        catch(Exception ex)
        {
            ErrorBean error = new ErrorBean();
            error.setIdError("MISGEST_002");
            error.setMensajeError("Error al actualizar procesado actualizaInteresados");
            error.setSituacion("actualizarProcesadoActInteresados");
            
            
            MeLanbide43Manager.grabarError(error, ex.getMessage().toString(), ex.toString(), "");
            throw new Exception(ex);
        }
        finally
        {
            
            if(st!=null) 
                st.close();
        }
        return result;
    }
    
    public String getValorCampoSuplementarioExpedienteTDESP(String numExpediente, String codCampo, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String valorCampo = "";
        try {
            String query = null;
            query = "SELECT TDE_VALOR "
                    + " FROM E_TDE "
                    + " WHERE TDE_NUM='" + numExpediente + "'"
                    + " AND TDE_COD='" + codCampo + "'";
            log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                valorCampo = rs.getString("TDE_VALOR");
            }
        } catch (Exception ex) {
            log.error("Error en getValorCampoSuplementarioExpedienteTDESP " + ex.getMessage(), ex);
            throw ex;
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        log.debug("getValorCampoSuplementarioExpedienteTDESP : " + numExpediente + " - " + valorCampo);
        return valorCampo;
    }
 
    /**
     * recupera el valor del campo suplementario numerico de expediente que sea
     * entero recibido en codCampo
     *
     * @param numExp
     * @param codCampo
     * @param con
     * @return
     * @throws Exception
     */
    public int getCampoNumericoEnteroExpediente(String numExp, String codCampo, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        int valor = 0;
        String query = null;
        try {
            query = "SELECT tnu_valor FROM e_tnu"
                    + " WHERE tnu_num='" + numExp + "'"
                    + " AND tnu_cod='" + codCampo + "'";
            log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                valor = rs.getInt("TNU_VALOR");
            }
        } catch (SQLException ex) {
            log.error("Error en getCampoNumericoEnteroExpediente " + ex.getMessage(), ex);
            throw ex;
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return valor;
    }
    public boolean  eliminarExpTratadoAbrirMiCarpeta(String numExpediente, Connection con) throws Exception {
        Statement st = null;
        int result = 0;
        try {
            String query = "DELETE FROM EXP_ABRIR_MICARPETA WHERE NUM_EXP='" + numExpediente + "'";
            log.debug("sql= " + query);
            st = con.createStatement();
            result = st.executeUpdate(query);
            log.debug("sql result eliminarExpTratadoAbrirMiCarpeta  = " + result);
            return result > 0;
        } catch (SQLException exc) {
            log.error("eliminarExpTratadoAbrirMiCarpeta - SQLException - Error al eliminar expediente tratado de EXP_ABRIR_MICARPETA" + numExpediente, exc);
            throw exc;
        }catch (Exception exc) {
            log.error("eliminarExpTratadoAbrirMiCarpeta - Exception - Error al eliminar expediente tratado de EXP_ABRIR_MICARPETA" + numExpediente, exc);
            throw exc;
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }
    
    public List<String> getExpedientesReabrirMicarpeta(Connection con) throws Exception{
        PreparedStatement ps =null;
        ResultSet rs = null;
        String numExp=null;
        List<String> expedientes = new ArrayList<String>();
       
        try
        {
            String query = "SELECT NUM_EXP from EXP_ABRIR_MICARPETA";
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
        
            while(rs.next())
            {                
                numExp=rs.getString("NUM_EXP");
                log.debug("numExp = " + numExp);
                expedientes.add(numExp);
            }
        } catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando los expedientes", ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if(ps!=null) 
                    ps.close();
                if(rs!=null) 
                    rs.close();
            }catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);                
            }
        }
        
        return expedientes;       
   }

    public Date getValorCampoSuplementarioTramiteE_TFET(String numExpediente, String codCampo, int tramite, int ocurrencia, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        Date valorCampo = null;
        try {
            String query = null;
            query = "SELECT TFET_VALOR "
                    + " FROM E_TFET "
                    + " WHERE TFET_NUM='" + numExpediente + "'"
                    + " AND TFET_COD='" + codCampo + "'"
                    + " AND TFET_TRA=" + tramite
                    + " AND TFET_OCU=" + ocurrencia
            ;
            log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                valorCampo = rs.getDate("TFET_VALOR");
            }
        } catch (Exception ex) {
            log.error("Error en getValorCampoSuplementarioTramiteE_TFET " + ex.getMessage(), ex);
            throw ex;
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        log.debug("getValorCampoSuplementarioTramiteE_TFET : " + numExpediente + " - " + valorCampo);
        return valorCampo;
    }

    //devuelve true si el expediente está en MiCarpeta (si está en MELANBIDE43_LLAMAMISGEST, con tipo_operacion='I', resultado_proceso=1)
    //select count(*) from melanbide43_integmisgest where exp_num = 'XXXX/PROCEDIMIENTO/XXXXXX' and tipo_operacion='I' and resultado_proceso=1;
    public boolean iniciadoEnMiCarpeta (String numExp, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        boolean existe = false;        
        try {
            String sql = "select exp_num\n" +
                            " from melanbide43_integmisgest\n" +
                            " where exp_num = '" + numExp +"'\n" +
                            " and tipo_operacion='I' and resultado_proceso=1"; 
            if(log.isDebugEnabled()) 
                log.debug("sql = " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            if(rs.next()){
                existe = true;
            }
        } catch(SQLException ex) {
            log.error("Se ha producido un error en enMiCarpeta: " + ex.getMessage(), ex);
            throw new Exception(ex);
        }catch(Exception ex) {
            log.error("Se ha producido un error en enMiCarpeta", ex);
            throw new Exception(ex);
        } finally {
            if(st!=null) st.close();
            if(rs!=null) rs.close();
        }
        if(log.isDebugEnabled()) log.debug("enMiCarpeta() : END");
        return existe;
    }

    /**
     * metodo que devuelve true si el expediente está CERRADO en MiCarpeta (si
     * está en MELANBIDE43_INTEGMISGEST, con tipo_operacion='C',
     * resultado_proceso=1)
     *
     * @param numExp
     * @param con
     * @return
     * @throws Exception
     */
    public boolean cerradoEnMiCarpeta(String numExp, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        boolean cerrado = false;
        try {
            String sql = "select exp_num\n"
                    + " from melanbide43_integmisgest\n"
                    + " where exp_num = '" + numExp + "'\n"
                    + " and tipo_operacion='C' and resultado_proceso=1";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + sql);
            }
            st = con.createStatement();
            rs = st.executeQuery(sql);
            if (rs.next()) {
                cerrado = true;
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error al comprobar si un expediente está cerrado en MiCarpeta", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("cerradoEnMiCarpeta() : END");
        }
        return cerrado;
    }
    
    //delete from melanbide43_integmisgest where tipo_operacion='C' and exp_num='2023/ATASE/000009';
    public void  eliminarGestionesExpedienteCerradoMiCarpeta(String numExpediente, Connection con) throws Exception {
        Statement st = null;
        int result = 0;
        try {
            String query = "delete from melanbide43_integmisgest where tipo_operacion='C' and exp_num='" + numExpediente + "'";
            log.debug("sql= " + query);
            st = con.createStatement();
            result = st.executeUpdate(query);
            log.debug("sql result eliminarGestionesExpedienteCerradoMiCarpeta  = " + result);
        } catch (SQLException exc) {
            log.error("eliminarGestionesExpedienteCerradoMiCarpeta - SQLException - Error al eliminar expediente cerrado, tratado en melanbide43_integmisgest" + numExpediente, exc);
            throw exc;
        }catch (Exception exc) {
            log.error("eliminarGestionesExpedienteCerradoMiCarpeta - Exception - Error al eliminar expediente cerrado, tratado en melanbide43_integmisgest" + numExpediente, exc);
            throw exc;
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }
    
    
}//class
