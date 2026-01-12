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
 * 
 * @author altia
 */
public class MeLanbide03ExpedienteDAO {
    
    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide03ExpedienteDAO.class);
    
    //Instancia
    private static MeLanbide03ExpedienteDAO instance = null;
    
    /**
     * Devuelve una instancia de MeLanbide03CertificadoDAO, si no existe la crea
     * @return MeLanbide03CertificadoDAO
     */
    public static MeLanbide03ExpedienteDAO getInstance(){
        if(log.isDebugEnabled()) log.debug("getInstance() : BEGIN");
        if(instance == null){
            synchronized(MeLanbide03ExpedienteDAO.class){
                if(instance == null){
                    instance = new MeLanbide03ExpedienteDAO();
                }//if(instance == null)
            }//synchronized(MeLanbide03CertificadoDAO.class)
        }//if(instance == null)
        if(log.isDebugEnabled()) log.debug("getInstance() : END");
        return instance;
    }//getInstance
    
    /**
     * Devuelve el tipo de acreditación del expediente
     * @param numExpediente
     * @param codOrganizacion
     * @param con
     * @return String
     * @throws Exception 
     */
    public String getTipoAcreditacion(String numExpediente, int codOrganizacion,Connection con) throws Exception{
        if(log.isDebugEnabled()) log.debug("getTipoAcreditacion() : BEGIN");
        String tipoAcreditacion = null;
        Statement st = null;
        ResultSet rs = null;
        try{
            StringBuffer sBSql = new StringBuffer("Select TDE_VALOR from ");
            sBSql.append(ConfigurationParameter.getParameter(ConstantesMeLanbide03.TABLA_E_TDE, ConstantesMeLanbide03.FICHERO_PROPIEDADES));
            sBSql.append(" where E_TDE.TDE_NUM = '");
            sBSql.append(numExpediente);
            sBSql.append("' and E_TDE.TDE_MUN = ");
            sBSql.append(codOrganizacion);
            sBSql.append(" and E_TDE.TDE_COD = 'TIPOACREDITACION'"); 
            String sql = sBSql.toString();
            if(log.isDebugEnabled()) log.debug("sql = " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
           
           while(rs.next()){
               tipoAcreditacion = rs.getString("TDE_VALOR");
           }
        }catch (Exception e) {
            log.error("Se ha producido un error recuperando el tipo de acreditación", e);
            throw new Exception(e);
        }finally{
            if(log.isDebugEnabled()) log.debug("Procedemos a cerrar el statement y el resultset");
            if(rs!=null) rs.close();
            if(st!=null) st.close();
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("getTipoAcreditacion() : END");
        return tipoAcreditacion;
    }//getCertificadoExpediente
    
}//class
