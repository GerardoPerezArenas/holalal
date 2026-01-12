package es.altia.flexia.integracion.moduloexterno.melanbide03.dao;

import es.altia.flexia.integracion.moduloexterno.melanbide03.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide03.util.ConstantesMeLanbide03;
import es.altia.flexia.integracion.moduloexterno.melanbide03.vo.CerCertificadoVO;
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
 *  <li>david.caamano * 17-08-2012 * #53275 Edición inicial</li>
 * </ol> 
 */
public class MeLanbide03CertificadoDAO {
    
    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide03CertificadoDAO.class);
    
    //Instancia
    private static MeLanbide03CertificadoDAO instance = null;
    
    /**
     * Devuelve una instancia de MeLanbide03CertificadoDAO, si no existe la crea
     * @return MeLanbide03CertificadoDAO
     */
    public static MeLanbide03CertificadoDAO getInstance(){
        if(log.isDebugEnabled()) log.debug("getInstance() : BEGIN");
        if(instance == null){
            synchronized(MeLanbide03CertificadoDAO.class){
                if(instance == null){
                    instance = new MeLanbide03CertificadoDAO();
                }//if(instance == null)
            }//synchronized(MeLanbide03CertificadoDAO.class)
        }//if(instance == null)
        if(log.isDebugEnabled()) log.debug("getInstance() : END");
        return instance;
    }//getInstance
    
    /**
     * Devuelve el código del certificado para un expediente
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
        if(log.isDebugEnabled()) log.debug("Buscamos en la tabla MELANBIDE03_CERTIFICADO el certificado seleccionado para el expediente");
        try{
           String sql = "Select COD_CERTIFICADO from " + ConfigurationParameter.getParameter(ConstantesMeLanbide03.MELANBIDE03_CERTIFICADO, ConstantesMeLanbide03.FICHERO_PROPIEDADES)
                    + " where NUM_EXPEDIENTE = '" + numExpediente + "'"
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
            if(rs!=null) rs.close();
            if(st!=null) st.close();
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("getCertificadoExpediente() : END");
        return codCertificado;
    }//getCertificadoExpediente
    
    
   /**
     * Indica si para un expediente existe algún certificado asociado
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
            String sql = "SELECT COD_CERTIFICADO FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide03.MELANBIDE03_CERTIFICADO, ConstantesMeLanbide03.FICHERO_PROPIEDADES)
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
            if(rs!=null) rs.close();
            if(st!=null) st.close();
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("existeCertificadoExpediente () : END");
        return codCertificado;
        
    }//existeCertificadoExpediente
    
    /**
     * Crea un registro con la información del certificado, incluye tambien sus unidades competenciales
     * @param certificado
     * @param con
     * @throws Exception 
     */
    public void insertarCertificadoExpediente (CerCertificadoVO certificado, Connection con) throws SQLException, Exception{
        if(log.isDebugEnabled()) log.debug("insertarCertificadoExpediente() : BEGIN");
        Statement st = null;
        ResultSet rs = null;
        try{
            String sql = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide03.MELANBIDE03_CERTIFICADO, ConstantesMeLanbide03.FICHERO_PROPIEDADES)
                   + " (COD_CERTIFICADO, COD_AREA, COD_FAMILIA, NUM_EXPEDIENTE, COD_ORGANIZACION, COD_PROCEDIMIENTO) " 
                   + " VALUES('" + certificado.getCodCertificado() + "','";
            
            if(("null".equals(certificado.getCodArea()))||(certificado.getCodArea()==null)) {
                sql=sql+ " ','";  
            }else {        
                sql=sql+ certificado.getCodArea() + "','";  
            }
            if(("null".equals(certificado.getArea().getCodFamilia()))||(certificado.getArea().getCodFamilia()==null)) {
                       sql=sql+  " ','";
            }else {
                sql=sql+ certificado.getArea().getCodFamilia() + "','" ;  
            }
                
            sql=sql+ certificado.getNumExpediente() + "',"
                   + certificado.getCodOrganizacion() + ",'" + certificado.getCodProcedimiento() + "')";
                
            if(log.isDebugEnabled()) log.debug("sql = " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            
            if(certificado.getUnidades() != null && certificado.getUnidades().size() > 0){
                if(log.isDebugEnabled()) log.debug("Creamos las unidades competenciales del expediente");
                MeLanbide03CertCentroDAO certCentroDao = MeLanbide03CertCentroDAO.getInstance();
                certCentroDao.insertarCentros(certificado.getUnidades(), con);                
            }
            
        }catch (SQLException e) {
            log.error("Se ha producido un error insertando el certificado", e);            
            throw e;
        }catch (Exception e) {
            log.error("Se ha producido un error insertando el certificado", e);            
            throw e;
        }finally{
            if(log.isDebugEnabled()) log.debug("Procedemos a cerrar el statement y el resultset");
            if(rs!=null) rs.close();
            if(st!=null) st.close();
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("insertarCertificadoExpediente() : END");
        
    }//insertarCertificadoExpediente
    
    /**
     * Actualiza los datos de un certificado, incluye tambien sus unidades competenciales
     * @param certificado: Objeto del tipo CerCertificadoVO
     * @param codCertificadoViejo: Código del certificado viejo
     * @param con: Conexión a la BBDD
     * @throws SQLException si se ha producido algún error al acceder a la BBDD
     */
    public void actualizarCertificadoExpediente (CerCertificadoVO certificado,String codCertificadoViejo, Connection con) throws Exception, SQLException{
        if(log.isDebugEnabled()) log.debug("insertarCertificadoExpediente() : BEGIN");
        Statement st = null;
        ResultSet rs = null;
        try{
            String sql = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide03.MELANBIDE03_CERTIFICADO, ConstantesMeLanbide03.FICHERO_PROPIEDADES)
                    + " SET COD_CERTIFICADO = '" + certificado.getCodCertificado() + "', ";
                    if((certificado.getCodArea()==null)||("null".equals(certificado.getCodArea())))
                    {
                        sql=sql + " COD_AREA = ' ', ";
                    }else  sql=sql+ " COD_AREA = '" + certificado.getCodArea() + "', ";
                    if((certificado.getArea().getCodFamilia() ==null)||("null".equals(certificado.getArea().getCodFamilia() )))
                    {
                        sql=sql + " COD_FAMILIA = ' ', ";
                    }else  sql=sql+ " COD_FAMILIA = '" + certificado.getArea().getCodFamilia()  + "', ";
                   
                   sql=sql+
                     " NUM_EXPEDIENTE = '" + certificado.getNumExpediente() + "', "
                    + " COD_ORGANIZACION = " + certificado.getCodOrganizacion() + ", "
                    + " COD_PROCEDIMIENTO = '" + certificado.getCodProcedimiento() + "'"
                    + " WHERE COD_CERTIFICADO='" + codCertificadoViejo +  "'"
                    + " AND NUM_EXPEDIENTE='" + certificado.getNumExpediente() + "' " 
                    + " AND COD_ORGANIZACION = " + Integer.valueOf(certificado.getCodOrganizacion());
            
            if(log.isDebugEnabled()) log.debug("sql = " + sql);            
            st = con.createStatement();
            rs = st.executeQuery(sql);
            
            if(certificado.getUnidades() != null && certificado.getUnidades().size() > 0){
                if(log.isDebugEnabled()) log.debug("Actualizamos las unidades competenciales del expediente");
                MeLanbide03CertCentroDAO certCentroDao = MeLanbide03CertCentroDAO.getInstance();
                certCentroDao.actualizarCentros(certificado.getUnidades(), con);
            }
                        
        }catch (SQLException e) {
            log.error("Se ha producido un error recuperando los certificados", e);            
            throw e;
        }catch (Exception ex){
            log.error("Se ha producido un error recuperando los certificados", ex);            
            throw ex;
        }finally{
            if(log.isDebugEnabled()) log.debug("Procedemos a cerrar el statement y el resultset");
            if(rs!=null) rs.close();            
            if(st!=null) st.close();
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("insertarCertificadoExpediente() : END");
    }//actualizarCertificadoExpediente
    
    public void actualizarCertificadoExpediente (CerCertificadoVO certificado, Connection con) throws Exception, SQLException{
        if(log.isDebugEnabled()) log.debug("actualizarCertificadoExpediente() : BEGIN");
        Statement st = null;
        ResultSet rs = null;
        try{
            String sql = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide03.MELANBIDE03_CERTIFICADO, ConstantesMeLanbide03.FICHERO_PROPIEDADES)
                    + " SET COD_CERTIFICADO = '" + certificado.getCodCertificado() + "', ";
                    if((certificado.getCodArea()==null)||("null".equals(certificado.getCodArea())))
                    {
                        sql=sql + " COD_AREA = ' ', ";
                    }else  sql=sql+ " COD_AREA = '" + certificado.getCodArea() + "', ";
                    if((certificado.getArea().getCodFamilia() ==null)||("null".equals(certificado.getArea().getCodFamilia() )))
                    {
                        sql=sql + " COD_FAMILIA = ' ', ";
                    }else  sql=sql+ " COD_FAMILIA = '" + certificado.getArea().getCodFamilia()  + "', ";
                   
                   sql=sql+
                     " NUM_EXPEDIENTE = '" + certificado.getNumExpediente() + "', "
                    + " COD_ORGANIZACION = " + certificado.getCodOrganizacion() + ", "
                    + " COD_PROCEDIMIENTO = '" + certificado.getCodProcedimiento() + "'"
                    + " WHERE NUM_EXPEDIENTE='" + certificado.getNumExpediente() + "' " 
                    + " AND COD_ORGANIZACION = " + Integer.valueOf(certificado.getCodOrganizacion());
            
            if(log.isDebugEnabled()) log.debug("sql = " + sql);            
            st = con.createStatement();
            rs = st.executeQuery(sql);
            
            if(log.isDebugEnabled()) log.debug("actualizarCertificadoExpediente  FIN");
            if(certificado.getUnidades() != null && certificado.getUnidades().size() > 0){
                if(log.isDebugEnabled()) log.debug("Actualizamos las unidades competenciales del expediente");
                MeLanbide03CertCentroDAO certCentroDao = MeLanbide03CertCentroDAO.getInstance();
                certCentroDao.actualizarCentros(certificado.getUnidades(), con);
                if(log.isDebugEnabled()) log.debug("Actualizamos las unidades competenciales del expediente FIN");
            }
                        
        }catch (SQLException e) {
            log.error("Se ha producido un error ACTUALIZANDO los certificados", e);            
            throw e;
        }catch (Exception ex){
            log.error("Se ha producido un error ACTUALIZANDO los certificados", ex);            
            throw ex;
        }finally{
            if(log.isDebugEnabled()) log.debug("Procedemos a cerrar el statement y el resultset");
            if(rs!=null) rs.close();            
            if(st!=null) st.close();
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("insertarCertificadoExpediente() : END");
    }//actualizarCertificadoExpediente
    
    
    
    
    
   /**
     * Borra la información de un certificado asociado a un expediente, borra tambien las unidades competenciales
     * @param certificado: Datos del certificado
     * @param con: Conexión a la BBDD
     * @return boolean: True si se ha borrado correctamente y false en caso contrario
     * @throws SQLException si ocurre algún error al ejecutar la consulta contra la BBDD
     */
    public boolean borrarCertificadoExpediente (CerCertificadoVO certificado, Connection con) throws SQLException{
        if(log.isDebugEnabled()) log.debug("borrarCertificadoExpediente() : BEGIN");
        boolean exito = false;
        Statement st = null;
                
        try{
            String sql = "DELETE FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide03.MELANBIDE03_CERTIFICADO, ConstantesMeLanbide03.FICHERO_PROPIEDADES)
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
                MeLanbide03CertCentroDAO certCentroDao = MeLanbide03CertCentroDAO.getInstance();
                certCentroDao.borrarCentros(certificado.getCodCertificado(), certificado.getNumExpediente(), String.valueOf(certificado.getCodOrganizacion()), con);
                exito = true;
            }
           
        }catch (SQLException e) {
            exito = false;
            log.error("Se ha producido un error recuperando los certificados", e);            
            throw e;
        }finally{
            if(log.isDebugEnabled()) log.debug("Procedemos a cerrar el statement y el resultset");
            if(st!=null) st.close();
        }//try-catch-finally
        
        if(log.isDebugEnabled()) log.debug("borrarCertificadoExpediente() : END");        
        return exito;
        
    }//borrarCertificadoExpediente
    
    /**
     * Recupera el tipo de acreditación
     * @param numExpediente
     * @param codOrganizacion
     * @param con
     * @return String
     * @throws Exception 
     */
    public String getTipoAcreditacion(String numExpediente, int codOrganizacion,Connection con) throws Exception{
        if(log.isDebugEnabled()) log.debug("getTipoAcreditacion( numExpediente = " + numExpediente +  " codOrganizacion = " + codOrganizacion + " ) : BEGIN");
        String tipoAcred = null;
        Statement st = null;
        ResultSet rs = null;
        try{
           String sql = "SELECT TDE_VALOR FROM E_TDE,E_PCA, E_DES_VAL "
                    + " WHERE TDE_NUM= '" + numExpediente + "'"
                    + " AND TDE_MUN = PCA_MUN AND TDE_COD = PCA_COD AND PCA_DESPLEGABLE = DES_COD AND TDE_VALOR = DES_VAL_COD AND TDE_COD='TIPOACREDITACION'"; 
           
           
            if(log.isDebugEnabled()) log.debug("sql = " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
           
           while(rs.next()){
               tipoAcred = rs.getString("TDE_VALOR");
           }//while(rs.next())
           
           if(log.isDebugEnabled()) log.debug("Se recupera el tipo de acreditación de la BBDD");
        }catch (Exception e) {
            log.error("Se ha producido un error recuperando los certificados", e);
            throw new Exception(e);
        }finally{
            if(log.isDebugEnabled()) log.debug("Procedemos a cerrar el statement y el resultset");
            if(rs!=null) rs.close();
            if(st!=null) st.close();
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("getTipoAcreditacion() : END");
        return tipoAcred;
    }//getTipoAcreditacion
    
    /**
     * Recupera el tipo de valoración
     * Hay que comprobar que el expediente tiene valoración total o paso de APA a CP (campo VALORACION2 del trámite 2 - VALORACION CP APA es igual a "T" o a "C"), 
     * se generaría un PDF con la certificación CP / APA en PDF.
     * @param numExpediente
     * @param codOrganizacion
     * @param con
     * @return String
     * @throws Exception 
     */
    public String getValoracion(String numExpediente, int codOrganizacion, Connection con) throws Exception{
        if(log.isDebugEnabled()) log.debug("getValoracion( numExpediente = " + numExpediente +  " codOrganizacion = " + codOrganizacion + " ) : BEGIN");
        String valoracion = null;
        Statement st = null;
        ResultSet rs = null;
        try{
           String sql = "SELECT TDET_VALOR FROM E_TDET, E_TCA, E_DES_VAL "
                    + " WHERE TDET_NUM= '" + numExpediente + "'"
                    + " AND TDET_MUN = TCA_MUN AND TDET_COD = TCA_COD AND TCA_DESPLEGABLE = DES_COD AND TDET_VALOR = DES_VAL_COD AND TDET_COD='VALORACION2'"; 
           
            if(log.isDebugEnabled()) log.debug("sql = " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
           
           while(rs.next()){
               valoracion = rs.getString("TDET_VALOR");
           }//while(rs.next())
           
           if(log.isDebugEnabled()) log.debug("Se recupera el tipo de acreditación de la BBDD");
        }catch (Exception e) {
            log.error("Se ha producido un error recuperando los certificados", e);
            throw new Exception(e);
        }finally{
            if(log.isDebugEnabled()) log.debug("Procedemos a cerrar el statement y el resultset");
            if(rs!=null) rs.close();
            if(st!=null) st.close();
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("getValoracion() : END");
        return valoracion;
    }//getValoracion
    
}//class
