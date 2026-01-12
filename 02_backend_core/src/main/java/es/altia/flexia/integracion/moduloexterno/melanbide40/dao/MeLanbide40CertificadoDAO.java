package es.altia.flexia.integracion.moduloexterno.melanbide40.dao;

import es.altia.flexia.integracion.moduloexterno.melanbide40.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide40.util.ConstantesMeLanbide40;
import es.altia.flexia.integracion.moduloexterno.melanbide40.vo.CerCertificadoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide40.vo.CerModuloFormativoVO;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * @author david.caamano
 * @version 16/08/2012 1.0
 * Historial de cambios:
 * <ol>
 *  <li>david.caamano * 17-08-2012 * #53275 Edici�n inicial</li>
 * </ol> 
 */
public class MeLanbide40CertificadoDAO {
    
    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide40CertificadoDAO.class);
    
    //Instancia
    private static MeLanbide40CertificadoDAO instance = null;
    
    /**
     * Devuelve una instancia de MeLanbide40CertificadoDAO, si no existe la crea
     * @return MeLanbide40CertificadoDAO
     */
    public static MeLanbide40CertificadoDAO getInstance(){
        if(log.isDebugEnabled()) log.debug("getInstance() : BEGIN");
        if(instance == null){
            synchronized(MeLanbide40CertificadoDAO.class){
                if(instance == null){
                    instance = new MeLanbide40CertificadoDAO();
                }//if(instance == null)
            }//synchronized(MeLanbide40CertificadoDAO.class)
        }//if(instance == null)
        if(log.isDebugEnabled()) log.debug("getInstance() : END");
        return instance;
    }//getInstance
    
    /**
     * Devuelve el c�digo del certificado para un expediente
     * @param numExpediente
     * @param codOrganizacion
     * @param con
     * @return String
     * @throws Exception 
     */
    public String getCertificadoExpediente(String numExpediente, int codOrganizacion,Connection con) throws Exception{
        if(log.isDebugEnabled()) log.debug("getCertificadoExpediente( numExpediente = " + numExpediente +  " codOrganizacion = " + codOrganizacion + " ) : BEGIN");
        String codCertificado = null;
        Statement st = null;
        ResultSet rs = null;
        if(log.isDebugEnabled()) log.debug("Buscamos en la tabla MELANBIDE40_CERTIFICADO el certificado seleccionado para el expediente");
        try{
           String sql = "Select COD_CERTIFICADO from " + ConfigurationParameter.getParameter(ConstantesMeLanbide40.MELANBIDE40_CERTIFICADO, ConstantesMeLanbide40.FICHERO_PROPIEDADES)
                    + " where NUM_EXPEDIENTE like '" + numExpediente + "'"
                    + " and COD_ORGANIZACION = " + codOrganizacion; 
           
            if(log.isDebugEnabled()) log.debug("sql = " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
           
           while(rs.next()){
               codCertificado = rs.getString("COD_CERTIFICADO");
           }//while(rs.next())
           
           if(log.isDebugEnabled()) log.debug("Recuperamos los datos del certificado de la BBDD");
        }catch (Exception e) {
            log.error("Se ha producido un error recuperando los certificados", e);
            throw new Exception(e);
        }finally{
            if(log.isDebugEnabled()) log.debug("Procedemos a cerrar el statement y el resultset");
            if(st!=null) st.close();
            if(rs!=null) rs.close();
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("getCertificadoExpediente() : END");
        return codCertificado;
    }//getCertificadoExpediente
    
    
   /**
     * Indica si para un expediente existe alg�n certificado asociado
     * @param certificado
     * @param con
     * @return boolean
     * @throws SQLException 
     */
    public String existeCertificadoExpediente (CerCertificadoVO certificado, Connection con) throws SQLException{
        if(log.isDebugEnabled()) log.debug("existeCertificadoExpediente () : BEGIN");
        String codCertificado = null;
        
        Statement st = null;
        ResultSet rs = null;
        try{
            String sql = "SELECT COD_CERTIFICADO FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide40.MELANBIDE40_CERTIFICADO, ConstantesMeLanbide40.FICHERO_PROPIEDADES)
                    + " where NUM_EXPEDIENTE='" + certificado.getNumExpediente() + "'"
                    + " and COD_ORGANIZACION= " + certificado.getCodOrganizacion();
            
            if(log.isDebugEnabled()) log.debug("sql = " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next()){
                codCertificado = rs.getString("COD_CERTIFICADO");
            }            
            
        }catch (SQLException e) {
            log.error("Se ha producido un error recuperando los certificados", e);
            throw e;
        }finally{
            if(log.isDebugEnabled()) log.debug("Procedemos a cerrar el statement y el resultset");
            if(st!=null) st.close();
            if(rs!=null) rs.close();
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("existeCertificadoExpediente () : END");
        return codCertificado;
        
    }//existeCertificadoExpediente
    
    /**
     * Crea un registro con la informaci�n del certificado, incluye tambien sus unidades competenciales
     * @param certificado
     * @param con
     * @throws Exception 
     */
    public void insertarCertificadoExpediente (CerCertificadoVO certificado, CerModuloFormativoVO modulo, Connection con) throws SQLException, Exception{
        if(log.isDebugEnabled()) log.debug("insertarCertificadoExpediente() : BEGIN");
        Statement st = null;
        ResultSet rs = null;
        int result=0;
        try{
            String sql = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide40.MELANBIDE40_CERTIFICADO, ConstantesMeLanbide40.FICHERO_PROPIEDADES)
                   + " (COD_CERTIFICADO, NUM_EXPEDIENTE, COD_ORGANIZACION, DES_CERTIFICADO_CAS, DES_CERTIFICADO_EUS,"
                   + "COD_MODPRAC, DES_MODULO_CAS, DES_MODULO_EUS) " 
                   + " VALUES('" + certificado.getCodCertificado() + "','";                
            sql=sql+ certificado.getNumExpediente() + "',"
                   + certificado.getCodOrganizacion() + ",'"+certificado.getDescCertificadoC()+"','"+certificado.getDescCertificadoE()+"',"
                    + "'"+modulo.getCodModulo() +"','"+modulo.getDesModuloC() +"', '"+modulo.getDesModuloC() +"')";
                
            if(log.isDebugEnabled()) log.debug("sql = " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
           
                     
        }catch (SQLException e) {
            log.error("Se ha producido un error insertando el certificado", e);            
            throw e;
            
        }catch (Exception e) {
            log.error("Se ha producido un error insertando el certificado", e);            
            throw e;
        }finally{
            
            if(log.isDebugEnabled()) log.debug("Procedemos a cerrar el statement y el resultset");
            if(st!=null) st.close();
            if(rs!=null) rs.close();
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("insertarCertificadoExpediente() : END");
        
    }//insertarCertificadoExpediente
    
    /**
     * Actualiza los datos de un certificado, incluye tambien sus unidades competenciales
     * @param certificado: Objeto del tipo CerCertificadoVO
     * @param codCertificadoViejo: C�digo del certificado viejo
     * @param con: Conexi�n a la BBDD
     * @throws SQLException si se ha producido alg�n error al acceder a la BBDD
     */
    public void actualizarCertificadoExpediente (CerCertificadoVO certificado,CerModuloFormativoVO modulo,String codCertificadoViejo, Connection con) throws Exception, SQLException{
        if(log.isDebugEnabled()) log.debug("actualizarCertificadoExpediente() : BEGIN");
        Statement st = null;
        ResultSet rs = null;
        try{
            String sql = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide40.MELANBIDE40_CERTIFICADO, ConstantesMeLanbide40.FICHERO_PROPIEDADES)
                    + " SET COD_CERTIFICADO = '" + certificado.getCodCertificado() + "', ";                   
                   sql=sql+
                     " NUM_EXPEDIENTE = '" + certificado.getNumExpediente() + "', "
                    + " COD_ORGANIZACION = " + certificado.getCodOrganizacion() + ", "
                    + " DES_CERTIFICADO_CAS = '" + certificado.getDescCertificadoC() + "',"
                    + " DES_CERTIFICADO_EUS = '" + certificado.getDescCertificadoE() + "',"
                    + " COD_MODPRAC = '" + modulo.getCodModulo() + "',"
                    + " DES_MODULO_CAS = '" + modulo.getDesModuloC() + "',"
                    + " DES_MODULO_EUS = '" + modulo.getDesModuloE() + "' " 
                    + " WHERE COD_CERTIFICADO='" + codCertificadoViejo +  "'"
                    + " AND NUM_EXPEDIENTE='" + certificado.getNumExpediente() + "' " 
                    + " AND COD_ORGANIZACION = " + Integer.valueOf(certificado.getCodOrganizacion());
            
            if(log.isDebugEnabled()) log.debug("sql = " + sql);            
            st = con.createStatement();
            rs = st.executeQuery(sql);
                                 
        }catch (SQLException e) {
            log.error("Se ha producido un error recuperando los certificados", e);            
            throw e;
        }catch (Exception ex){
            log.error("Se ha producido un error recuperando los certificados", ex);            
            throw ex;
        }finally{
            if(log.isDebugEnabled()) log.debug("Procedemos a cerrar el statement y el resultset");
            if(st!=null) st.close();
            if(rs!=null) rs.close();
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("actualizarCertificadoExpediente() : END");
    }//actualizarCertificadoExpediente
    
    
    public void acreditarModulo (String[] expedientes, String certificado, String modulo, Connection con) throws Exception, SQLException{
        if(log.isDebugEnabled()) log.debug("acreditarModulo() : BEGIN");
        Statement st = null;
        ResultSet rs = null;
        try{
            //melanbide_mod_prac
            for (int i=0;i<expedientes.length;i++){
                //getCodigosModulosFormativos(String codCertificado, con);
                
            }
            
            String sql = "UPDATE "+ ConfigurationParameter.getParameter(ConstantesMeLanbide40.MELANBIDE03_MOD_PRACT, ConstantesMeLanbide40.FICHERO_PROPIEDADES)+" SET MODULO_ACREDITADO=0, COD_MOTIVO_ACREDITADO='EXSO' "
                    + " WHERE NUM_EXPEDIENTE IN (''";
            for (int i=0;i<expedientes.length;i++){
                sql +=",'"+expedientes[i]+"'";
            }
            sql += ") and COD_CERTIFICADO='"+certificado+"' AND COD_MODULO='"+modulo+"'";
            
            if(log.isDebugEnabled()) log.debug("sql = " + sql);            
            st = con.createStatement();
            rs = st.executeQuery(sql);
            
        }catch (SQLException e) {
            log.error("Se ha producido un error recuperando los certificados", e);            
            throw e;
        }catch (Exception ex){
            log.error("Se ha producido un error recuperando los certificados", ex);            
            throw ex;
        }finally{
            if(log.isDebugEnabled()) log.debug("Procedemos a cerrar el statement y el resultset");
            if(st!=null) st.close();
            if(rs!=null) rs.close();
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("acreditarModulo() : END");
    
    }
    
   /**
     * Borra la informaci�n de un certificado asociado a un expediente, borra tambien las unidades competenciales
     * @param certificado: Datos del certificado
     * @param con: Conexi�n a la BBDD
     * @return boolean: True si se ha borrado correctamente y false en caso contrario
     * @throws SQLException si ocurre alg�n error al ejecutar la consulta contra la BBDD
     */
   /* public boolean borrarCertificadoExpediente (CerCertificadoVO certificado, Connection con) throws SQLException{
        if(log.isDebugEnabled()) log.debug("borrarCertificadoExpediente() : BEGIN");
        boolean exito = false;
        Statement st = null;
        ResultSet rs = null;
        
        try{
            String sql = "DELETE FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide40.MELANBIDE40_CERTIFICADO, ConstantesMeLanbide40.FICHERO_PROPIEDADES)
                    + " WHERE COD_CERTIFICADO='" + certificado.getCodCertificado() + "' and NUM_EXPEDIENTE='" + certificado.getNumExpediente() + "' "
                    + " AND COD_ORGANIZACION = " + certificado.getCodOrganizacion();
            
            if(log.isDebugEnabled()) log.debug("sql = " + sql);
            
            st = con.createStatement();
            int rowsDeleted = st.executeUpdate(sql);
            boolean continuar = false;
            if(rowsDeleted==1){                
                exito = true;
            }
             
            if(exito){
                if(log.isDebugEnabled()) log.debug("Borramos las unidades competenciales del expediente");
                MeLanbide40CertCentroDAO certCentroDao = MeLanbide40CertCentroDAO.getInstance();
                certCentroDao.borrarCentros(certificado.getCodCertificado(), certificado.getNumExpediente(), String.valueOf(certificado.getCodOrganizacion()), con);
                exito = true;
            }
           
        }catch (SQLException e) {
            exito = false;
            log.error("Se ha producido un error recuperando los certificados", e);            
            throw e;
        }finally{
            try{
                if(log.isDebugEnabled()) log.debug("Procedemos a cerrar el statement y el resultset");
                if(st!=null) st.close();
                if(rs!=null) rs.close();
            }catch(SQLException e){
                log.error("Se ha producido un error cerrando el statement y el resulset", e);                
            }//try-catch
        }//try-catch-finally
        
        if(log.isDebugEnabled()) log.debug("borrarCertificadoExpediente() : END");        
        return exito;
        
    }//borrarCertificadoExpediente*/
}//class
