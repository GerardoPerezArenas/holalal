/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide40.dao;

import es.altia.flexia.integracion.moduloexterno.melanbide40.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide40.util.ConstantesMeLanbide40;
import es.altia.flexia.integracion.moduloexterno.melanbide40.util.MeLanbide40MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide40.vo.S75PagosVO;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author santiagoc
 */
public class MeLanbide40DAO 
{
    
    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide40DAO.class);
    
    //Instancia
    private static MeLanbide40DAO instance = null;
    
    private MeLanbide40DAO()
    {
        
    }
    
    public static MeLanbide40DAO getInstance()
    {
        if(instance == null)
        {
            synchronized(MeLanbide40DAO.class)
            {
                instance = new MeLanbide40DAO();
            }
        }
        return instance;
    }    
    
    
    private String getDatosTercerosPorExpediente(String numExp,  Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        String nomApe = "";
        String result = "";
        try
        {
            String query = null;
            
            query = "SELECT DISTINCT HTE_DOC "
                    + " FROM E_EXT "
                    + " LEFT JOIN T_HTE ON HTE_TER=EXT_TER AND HTE_NVR=EXT_NVR "
                    + " WHERE EXT_PRO='EMPNL' "
                    + " AND EXT_NUM='"+numExp+"'" ;
            
            /*query = "select t.ter_nom nom,t.ter_doc, t.ter_ap1 ap1, t.ter_ap2 ap2,\r\n"
                   +" h.hte_nom nom_his, h.hte_ap1 ap1_his, h.hte_ap2 ap2_his, DNN_PRV, PRV_NOM"
                   +" from e_ext\r\n"
                   +" left join t_ter t on t.ter_cod = ext_ter and ter_nve = ext_nvr\r\n"
                   +" left join t_hte h on h.hte_ter = ext_ter and hte_nvr = ext_nvr\r\n"               
                   +" where ext_num = '"+numExp+"' ";*/
            
            
            
            
            
            /*query = "select ext_ter from e_ext "
                    +" where ext_num = '"+numExp+"' ";*/
         
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while(rs.next())
            {
                /*if(rs.getString("ext_ter")!=null)
                    result = result +rs.getString("ext_ter")+",";*/
                if(rs.getString("HTE_DOC")!=null)
                      result = "'"+result +rs.getString("HTE_DOC")+"',";        
            }
            if (!result.equals(""))
                result = result.substring(0,result.length()-1);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando la lista de terceros por rol", ex);
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
        
        return result;
    }
    
    
    public String[] getDatosExpedientesCEPAP(String numExp, String codCertificado, Connection con) throws Exception{
        String[] expedientes =null;
        
        Statement st = null;
        ResultSet rs = null;
        try
        {            
            String datosTercero = null;
            datosTercero = this.getDatosTercerosPorExpediente(numExp, con);
        
            /*String query = "select count(num_expediente) as contexp "
                    + " from e_ext "
                    + " inner join melanbide03_certificado on num_expediente =ext_num "
                    + " where cod_certificado like '"+codCertificado+"' "
                    + " and ext_ter in ("+datosTercero+")";*/
            
            String query = "SELECT COUNT(*) as contexp FROM ("
                    +"SELECT DISTINCT EXT_NUM "
                    + " FROM E_EXT "
                    + " LEFT JOIN T_HTE ON HTE_TER=EXT_TER AND HTE_NVR=EXT_NVR "
                    + " inner join melanbide03_certificado on num_expediente =ext_num  "
                    + " WHERE EXT_PRO='CEPAP' "
                    + " AND HTE_DOC IN ("+datosTercero+ ")" 
                    + " AND cod_certificado like '"+codCertificado+"'"
                    + ")"; 
            
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            int numfilas=0;
            if(rs.next()){
                numfilas = rs.getInt("contexp");
            }
            
            expedientes = new String[numfilas];
            /*query = "select num_expediente "
                    + " from e_ext "
                    + " inner join melanbide03_certificado on num_expediente =ext_num "
                    + " where cod_certificado like '"+codCertificado+"' "
                    + " and ext_ter in ("+datosTercero+")";*/
            
            query = "SELECT DISTINCT EXT_NUM AS num_expediente "
                    + " FROM E_EXT "
                    + " LEFT JOIN T_HTE ON HTE_TER=EXT_TER AND HTE_NVR=EXT_NVR "
                    + " inner join melanbide03_certificado on num_expediente =ext_num  "
                    + " WHERE EXT_PRO='CEPAP' "
                    + " AND HTE_DOC IN ("+datosTercero+ ")" 
                    + " AND cod_certificado like '"+codCertificado+"'"; 
            
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            int rowcount=0;
            int cont=0;                      
            while (rs.next()){
                expedientes[cont] = rs.getString("num_expediente");
                cont++;
            }
            
            
        }catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando datos del expedientes CEPAP relacionados", ex);
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
        
        
        return expedientes;
    }
    
    
    
    
    public BigDecimal getValorCampoNumerico(int codOrganizacion, String numExp, String ejercicio, String codigoCampo, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        BigDecimal valor = null;
        try
        {
            String query = null;
            query = "select TNU_VALOR from " + ConfigurationParameter.getParameter(ConstantesMeLanbide40.TABLA_TIPO_DATO_NUMERICO, ConstantesMeLanbide40.FICHERO_PROPIEDADES)
                    + " where TNU_MUN = '" + codOrganizacion + "' and TNU_EJE = '" + ejercicio 
                    + "' and TNU_NUM = '"+numExp+"' and TNU_COD = '"+codigoCampo+"'";
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
                valor = rs.getBigDecimal("TNU_VALOR");
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando las áreas", ex);
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
        return valor;
    }
    
    public BigDecimal getValorCampoNumericoTramite(int codOrganizacion, String ejercicio, String numExp, Long codigoTramite, String codigoCampo, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        BigDecimal valor = null;
        try
        {
            String query = null;
            query = "select TNUT_VALOR from " + ConfigurationParameter.getParameter(ConstantesMeLanbide40.TABLA_TIPO_DATO_NUMERICO_TRAMITE, ConstantesMeLanbide40.FICHERO_PROPIEDADES)
                    + " where TNUT_MUN = "+codOrganizacion
                    + " and TNUT_EJE = "+ejercicio 
                    + " and TNUT_NUM = '"+numExp+"'"
                    + " and TNUT_TRA = "+codigoTramite
                    + " and TNUT_COD = '"+codigoCampo+"'";
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
                valor = rs.getBigDecimal("TNUT_VALOR");
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando las áreas", ex);
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
        return valor;
    }
    
    public S75PagosVO getPagosExpediente(int codOrganizacion, String numExp, String ejercicio, int numPago, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        try
        {
            S75PagosVO pago = null;
            String query = "select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide40.TABLA_S75_PAGOS, ConstantesMeLanbide40.FICHERO_PROPIEDADES)
                    + " where PAG_MUN = '" + codOrganizacion + "' and PAG_PRO = '" + ConstantesMeLanbide40.CODIGO_PROCEDIMIENTO_MELANBIDE40 
                    + "' and PAG_EJE = '" + ejercicio 
                    + "' and PAG_NUM = '" + numExp 
                    + "' and PAG_NUMPAGO = '" + numPago + "'";
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                pago = (S75PagosVO)MeLanbide40MappingUtils.getInstance().map(rs, S75PagosVO.class);
            }
            return pago;
        }
        catch(Exception ex)
        {
            
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
        return null;
    }
    
    }
