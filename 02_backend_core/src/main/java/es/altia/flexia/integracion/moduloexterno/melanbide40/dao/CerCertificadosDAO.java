package es.altia.flexia.integracion.moduloexterno.melanbide40.dao;

import es.altia.agora.business.util.GlobalNames;
import es.altia.flexia.integracion.moduloexterno.melanbide40.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide40.util.ConstantesMeLanbide40;
import es.altia.flexia.integracion.moduloexterno.melanbide40.vo.CerCertificadoVO;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * @author david.caamano
 * @version 16/08/2012 1.0
 * Historial de cambios:
 * <ol>
 *  <li>david.caamano * 16-08-2012 * #86969 Edici�n inicial</li>
 *  <li>david.caamano * 27-09-2012 * #90628 Edici�n inicial</li>
 * </ol> 
 */
public class CerCertificadosDAO {
    
    //Logger
    private static Logger log = LogManager.getLogger(CerCertificadosDAO.class);
    
    //Instance
    private static CerCertificadosDAO instance = null;
    
    /**
     * Devuelve una �nica instancia de la clase CerCertificadosDao, si no existe la crea.
     * @return CerCertificadosDao
     */
    public static CerCertificadosDAO getInstance(){
        if(log.isDebugEnabled()) log.debug("getInstance() : BEGIN");
        if (instance == null) {
            synchronized(CerCertificadosDAO.class){
                if(instance == null){
                    instance = new CerCertificadosDAO();
                }//if(instance == null)
            }//synchronized(CerCertificadosDAO.class)
        }//if (instance == null) 
        if(log.isDebugEnabled()) log.debug("getInstance() : END");
        return instance;
    }//getInstance
    
    /**
     * Recupera una lista con el c�digo y la descripci�n de los certificados de la tabla CER_CERTIFICADOS
     * @param con
     * @return ArrayList<CerCertificadoVO> (Solo incluye el c�digo y la descripci�n)
     * @throws Exception 
     */
    public ArrayList<CerCertificadoVO> getCertificados (Connection con) throws Exception{
        if(log.isDebugEnabled()) log.debug("getCertificados() : BEGIN");
        ArrayList<CerCertificadoVO> listaCertificados = new ArrayList<CerCertificadoVO>();
        Statement st = null;
        ResultSet rs = null;
        try{
            String sql = "Select * from " + GlobalNames.ESQUEMA_GENERICO
                + ConfigurationParameter.getParameter(ConstantesMeLanbide40.CER_CERTIFICADOS, ConstantesMeLanbide40.FICHERO_PROPIEDADES)
                + " ORDER BY DESCERTIFICADO_C ASC";
            
            if(log.isDebugEnabled()) log.debug("sql = " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next()){
                CerCertificadoVO certificado = new CerCertificadoVO();
                    certificado.setCodCertificado(rs.getString("CODCERTIFICADO"));
                    certificado.setDescCertificadoC(rs.getString("DESCERTIFICADO_C"));
                    certificado.setDescCertificadoE(rs.getString("DESCERTIFICADO_E"));
                listaCertificados.add(certificado);
            }//while(rs.next())
        }catch (Exception e) {
            log.error("Se ha producido un error recuperando los certificados", e);
            throw new Exception(e);
        }finally{
            if(log.isDebugEnabled()) log.debug("Procedemos a cerrar el statement y el resultset");
            if(st!=null) st.close();
            if(rs!=null) rs.close();
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("getCertificados() : END");
        return listaCertificados;
    }//getCertificados
    
    /**
     * Recupera el certificado por su c�digo de certificado
     * 
     * @param codCertificado
     * @param con
     * @return CerCertificadoVO
     * @throws Exception 
     */
    public CerCertificadoVO getCertificado (String codCertificado, Connection con) throws Exception{
        if(log.isDebugEnabled()) log.debug("getCertificado( codCertificado = " + codCertificado + " ) : BEGIN");
        CerCertificadoVO certificado = new CerCertificadoVO();
        Statement st = null;
        ResultSet rs = null;
        try{
            String sql = "Select * from " + GlobalNames.ESQUEMA_GENERICO
                + ConfigurationParameter.getParameter(ConstantesMeLanbide40.CER_CERTIFICADOS, ConstantesMeLanbide40.FICHERO_PROPIEDADES) 
                + " where CODCERTIFICADO='" + codCertificado + "'";
            if(log.isDebugEnabled()) log.debug("sql = " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next()){
                certificado.setCodCertificado(rs.getString("CODCERTIFICADO"));
                certificado.setDescCertificadoC(rs.getString("DESCERTIFICADO_C"));
                certificado.setDescCertificadoE(rs.getString("DESCERTIFICADO_E"));
                certificado.setCodArea(rs.getString("CODAREA"));
                String nivel = rs.getString("NIVEL");
                
                certificado.setNivel("");
                if(nivel!=null) certificado.setNivel(nivel);
                
                String tieneModulo = rs.getString("TIENEMODULO");
                certificado.setTieneModulo("");
                if(tieneModulo!=null) certificado.setTieneModulo(tieneModulo);
                
                certificado.setAbreviado(rs.getString("ABREVIADO"));
                certificado.setDecreto(rs.getString("DECRETO"));
                certificado.setEstado(rs.getString("ESTADO"));
                
               /* String tipoCP = rs.getString("TIPO_CP");
                if(tipoCP != null && !"".equalsIgnoreCase(tipoCP))
                certificado.setTipoCP(Integer.valueOf(tipoCP));
                
                Calendar cal=Calendar.getInstance();
                if(rs.getDate("FECHA_RD") != null){
                    cal.setTime(rs.getDate("FECHA_RD"));
                    if(cal != null)
                        certificado.setFechaRD(cal.getTime());
                }//if(rs.getDate("FECHA_RD") != null)
                
                if(rs.getDate("FECHA_MODIF_RD") != null){
                    cal.setTime(rs.getDate("FECHA_MODIF_RD"));
                    if(cal != null)
                        certificado.setFechaRDModif(cal.getTime());
                }//if(rs.getDate("FECHA_MODIF_RD") != null)
                
                if(rs.getDate("FECHA_DER") != null){
                    cal.setTime(rs.getDate("FECHA_DER"));
                    if(cal != null)
                        certificado.setFechaRDDeroga(cal.getTime());
                }//if(rs.getDate("FECHA_DER") != null)
                
                certificado.setRDModif(rs.getString("DECRETO_MOD"));
                certificado.setRDDeroga(rs.getString("DECRETO_DER"));
                
                String decretoMod = rs.getString("DECRETO_MOD");*/
                /*certificado.setDecretoMod("");
                if(decretoMod!=null && !"".equals(decretoMod) && !"null".equalsIgnoreCase(decretoMod)){
                    certificado.setDecretoMod(decretoMod);
                }*/
                
            }//while(rs.next())
        }catch (Exception e) {
            log.error("Se ha producido un error recuperando el certificado con c�digo = " + codCertificado + " ", e);
            throw new Exception(e);
        }finally{
            if(log.isDebugEnabled()) log.debug("Procedemos a cerrar el statement y el resultset");
            if(st!=null) st.close();
            if(rs!=null) rs.close();
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("getCertificado() : END");
        return certificado;
    }//getCertificado
    
}//class
