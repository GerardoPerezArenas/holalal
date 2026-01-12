package es.altia.flexia.integracion.moduloexterno.melanbide03.dao;

import es.altia.agora.business.util.GlobalNames;
import es.altia.flexia.integracion.moduloexterno.melanbide03.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide03.util.ConstantesMeLanbide03;
import es.altia.flexia.integracion.moduloexterno.melanbide03.vo.CerAreaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide03.vo.CerFamiliaVO;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
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
public class CerAreasDAO {
    
    //Logger
    private static Logger log = LogManager.getLogger(CerAreasDAO.class);
    
    //Instancia
    private static CerAreasDAO instance = null;
    
    /**
     * Devuelve una instancia de CerAreasDAO, si no existe la crea
     * @return CerAreasDAO
     */
    public static CerAreasDAO getInstance(){
        if(log.isDebugEnabled()) log.debug("getInstance() : BEGIN");
        if(instance == null){
            synchronized(CerAreasDAO.class){
                if(instance == null){
                    instance = new CerAreasDAO();
                }//if(instance == null)
            }//synchronized(CerAreasDAO.class)
        }//if(instance == null)
        if(log.isDebugEnabled()) log.debug("getInstance() : END");
        return instance;
    }//getInstance
    
    /**
     * Devuelve el área relacionada con el código de área del certificado
     * @param codArea Código del area relacionada en el certificado
     * @param con SQLConnection
     * @return ArrayList<CerAreaVO>
     */
    /*public ArrayList<CerAreaVO> getAreas(Connection con, String codArea) throws Exception{
        if(log.isDebugEnabled()) log.debug("getAreas( codArea = " + codArea + " ) : BEGIN");
        ArrayList<CerAreaVO> listaAreas = new ArrayList<CerAreaVO>();
        Statement st = null;
        ResultSet rs = null;
        try{
            String sql = "Select * from " + GlobalNames.ESQUEMA_GENERICO
                + ConfigurationParameter.getParameter(ConstantesMeLanbide03.CER_AREAS, ConstantesMeLanbide03.FICHERO_PROPIEDADES)
                + " where CODAREA like '" + codArea + "'";
            if(log.isDebugEnabled()) log.debug("sql = " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next()){
                CerAreaVO cerArea = new CerAreaVO();
                    cerArea.setCodArea(rs.getString("CODAREA"));
                    cerArea.setCodFamilia(rs.getString("CODFAMILIA"));
                    cerArea.setDescAreaC(rs.getString("DESAREA_C"));
                    cerArea.setDescAreaE(rs.getString("DESAREA_E"));
                        if(log.isDebugEnabled()) log.debug("Recuperamos la familia");
                        String codFamilia = rs.getString("CODFAMILIA");
                        CerFamiliaDAO familiaDAO = CerFamiliaDAO.getInstance();
                        CerFamiliaVO familia = familiaDAO.getFamilia(codFamilia, con);
                    cerArea.setFamilia(familia);
                listaAreas.add(cerArea);
            }//while(rs.next())
        }catch (Exception e) {
            log.error("Se ha producido un error recuperando las áreas", e);
            throw new Exception(e);
        }finally{
            if(log.isDebugEnabled()) log.debug("Procedemos a cerrar el statement y el resultset");
            if(rs!=null) rs.close();
            if(st!=null) st.close();            
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("getAreas() : END");
        return listaAreas;
    }//getAreas*/
    
    /**
     * Devuelve el área relacionada con el código de área del certificado
     * @param codArea Código del area relacionada en el certificado
     * @param con SQLConnection
     * @return CerAreaVO
     */
    public CerAreaVO getArea (Connection con, String codArea) throws Exception{
        if(log.isDebugEnabled()) log.debug("getArea( codArea = " + codArea + " ) : BEGIN");
        CerAreaVO area = new CerAreaVO();
        Statement st = null;
        ResultSet rs = null;
        try{
            String sql = "Select * from " + GlobalNames.ESQUEMA_GENERICO
                + ConfigurationParameter.getParameter(ConstantesMeLanbide03.CER_AREAS, ConstantesMeLanbide03.FICHERO_PROPIEDADES)
                + " where CODAREA='" + codArea + "'";
            if(log.isDebugEnabled()) log.debug("sql = " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next()){
                area.setCodArea(rs.getString("CODAREA"));
                area.setCodFamilia(rs.getString("CODFAMILIA"));
                area.setDescAreaC(rs.getString("DESAREA_C"));
                area.setDescAreaE(rs.getString("DESAREA_E"));
            }//while(rs.next())
        }catch (Exception e) {
            log.error("Se ha producido un error recuperando las áreas", e);
            throw new Exception(e);
        }finally{
            if(log.isDebugEnabled()) log.debug("Procedemos a cerrar el statement y el resultset");
            if(rs!=null) rs.close();
            if(st!=null) st.close();         
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("getArea() : END");
        return area;
    }//getArea
}//class
