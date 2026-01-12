package es.altia.flexia.integracion.moduloexterno.melanbide03.dao;

import es.altia.agora.business.util.GlobalNames;
import es.altia.flexia.integracion.moduloexterno.melanbide03.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide03.util.ConstantesMeLanbide03;
import es.altia.flexia.integracion.moduloexterno.melanbide03.vo.CerFamiliaVO;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * @author david.caamano
 * @version 16/08/2012 1.0
 * Historial de cambios:
 * <ol>
 *  <li>david.caamano * 16-08-2012 * #86969 Edición inicial</li>
 * </ol> 
 */
public class CerFamiliaDAO {
    
    //Logger
    private static Logger log = LogManager.getLogger(CerFamiliaDAO.class);
    
    //Instance
    private static CerFamiliaDAO instance;
    
    public static CerFamiliaDAO getInstance(){
        if(log.isDebugEnabled()) log.debug("getInstance() : BEGIN");
        if(instance == null){
            synchronized(CerFamiliaDAO.class){
                if(instance == null){
                    instance = new CerFamiliaDAO();
                }//if(instance == null)
            }//synchronized(CerFamiliaDAO.class)
        }//if(instance == null)
        if(log.isDebugEnabled()) log.debug("getInstance() : END");
        return instance;
    }//getInstance
    
    /**
     * Devuelve un objeto que contiene la familia del certificado
     * @param codFamilia
     * @param con
     * @return CerFamiliaVO
     * @throws Exception 
     */
    public CerFamiliaVO getFamilia(String codFamilia, Connection con) throws Exception{
        if(log.isDebugEnabled()) log.debug("getFamilia( codFamilia = " + codFamilia + " ) : BEGIN");
        CerFamiliaVO familia = new CerFamiliaVO();
        Statement st = null;
        ResultSet rs = null;
        try{
            String sql = "Select * from " + GlobalNames.ESQUEMA_GENERICO
                + ConfigurationParameter.getParameter(ConstantesMeLanbide03.CER_FAMILIAS, ConstantesMeLanbide03.FICHERO_PROPIEDADES)
                + " where CODFAMILIA='" + codFamilia + "'";
            if(log.isDebugEnabled()) log.debug("sql = " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next()){
                familia.setCodFamilia(rs.getString("CODFAMILIA"));
                familia.setDescFamiliaC(rs.getString("DESFAMILIA_C"));
                familia.setDescFamiliaE(rs.getString("DESFAMILIA_E"));
                familia.setImagen(rs.getString("IMAGEN"));
            }//while(rs.next())
        }catch (Exception e) {
            log.error("Se ha producido un error recuperando la familia", e);
            throw new Exception(e);
        }finally{
            if(log.isDebugEnabled()) log.debug("Procedemos a cerrar el statement y el resultset");
            if(rs!=null) rs.close();
            if(st!=null) st.close();
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("getFamilia() : END");
        return familia;
    }//getFamilias
    
}//class