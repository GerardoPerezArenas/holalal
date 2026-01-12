/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide34.dao;

import es.altia.flexia.integracion.moduloexterno.melanbide34.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide34.util.ConstantesMeLanbide34;
import es.altia.flexia.integracion.moduloexterno.melanbide34.util.MeLanbide34MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide34.vo.DatosAviso;
import es.altia.flexia.integracion.moduloexterno.melanbide34.vo.DesplegableAdmonLocalVO;
import es.altia.flexia.integracion.moduloexterno.melanbide34.vo.FilaMinimisVO;
import es.altia.flexia.integracion.moduloexterno.melanbide34.vo.S75PagosVO;
import es.altia.flexia.integracion.moduloexterno.melanbide34.vo.SelectItem;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author santiagoc
 */
public class MeLanbide34DAO 
{
    
    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide34DAO.class);
    
    //Instancia
    private static MeLanbide34DAO instance = null;
    
    private MeLanbide34DAO()
    {
        
    }
    
    public static MeLanbide34DAO getInstance()
    {
        if(instance == null)
        {
            synchronized(MeLanbide34DAO.class)
            {
                instance = new MeLanbide34DAO();
            }
        }
        return instance;
    }    
    
    public BigDecimal getValorCampoNumerico(int codOrganizacion, String numExp, String ejercicio, String codigoCampo, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        BigDecimal valor = null;
        try
        {
            String query = null;
            query = "select TNU_VALOR from " + ConfigurationParameter.getParameter(ConstantesMeLanbide34.TABLA_TIPO_DATO_NUMERICO, ConstantesMeLanbide34.FICHERO_PROPIEDADES)
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
        return valor;
    }
    
    public String getValorCampoDesplegable(int codOrganizacion, String numExp, String ejercicio, String codigoCampo, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        String valor = null;
        try
        {
            String query = null;
            query = "select TDE_VALOR from " + ConfigurationParameter.getParameter(ConstantesMeLanbide34.TABLA_TIPO_DATO_DESPLEGABLE, ConstantesMeLanbide34.FICHERO_PROPIEDADES)
                    + " where TDE_MUN = '" + codOrganizacion + "' and TDE_EJE = '" + ejercicio 
                    + "' and TDE_NUM = '"+numExp+"' and TDE_COD = '"+codigoCampo+"'";
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
                valor = rs.getString("TDE_VALOR");
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando las áreas", ex);
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
            query = "select TNUT_VALOR from " + ConfigurationParameter.getParameter(ConstantesMeLanbide34.TABLA_TIPO_DATO_NUMERICO_TRAMITE, ConstantesMeLanbide34.FICHERO_PROPIEDADES)
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
        return valor;
    }
    
     public Date getValorCampoFechaTramite(int codOrganizacion, String ejercicio, String numExp, Long codigoTramite, String codigoCampo, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        Date valor = null;
        try
        {
            String query = null;
            query = "select TFET_VALOR from " + ConfigurationParameter.getParameter(ConstantesMeLanbide34.TABLA_TIPO_DATO_FECHA_TRAMITE, ConstantesMeLanbide34.FICHERO_PROPIEDADES)
                    + " where TFET_MUN = "+codOrganizacion
                    + " and TFET_EJE = "+ejercicio 
                    + " and TFET_NUM = '"+numExp+"'"
                    + " and TFET_TRA = "+codigoTramite
                    + " and TFET_COD = '"+codigoCampo+"'";
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
                valor = rs.getDate("TFET_VALOR");
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando las áreas", ex);
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
        return valor;
    }
    
    public S75PagosVO getPagosExpediente(int codOrganizacion, String numExp, String ejercicio, int numPago, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        try
        {
            S75PagosVO pago = null;
            String query = "select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide34.TABLA_S75_PAGOS, ConstantesMeLanbide34.FICHERO_PROPIEDADES)
                    + " where PAG_MUN = '" + codOrganizacion + "' and PAG_PRO = '" + ConstantesMeLanbide34.CODIGO_PROCEDIMIENTO_MELANBIDE34 
                    + "' and PAG_EJE = '" + ejercicio 
                    + "' and PAG_NUM = '" + numExp 
                    + "' and PAG_NUMPAGO = '" + numPago + "'";
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                pago = (S75PagosVO)MeLanbide34MappingUtils.getInstance().map(rs, S75PagosVO.class);
            }
            return pago;
        }
        catch(Exception ex)
        {
            
        }
        return null;
    }
    
    public int guardarPago(S75PagosVO pago, boolean nuevo, Connection con)
    {
         SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide34.FORMATO_FECHA);
        Statement st = null;
        try
        {
            String query = "";
            if(nuevo)
            {
                query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide34.TABLA_S75_PAGOS, ConstantesMeLanbide34.FICHERO_PROPIEDADES)
                        +" (PAG_MUN, PAG_PRO, PAG_EJE, PAG_NUM, PAG_CONCEP, PAG_ANOPAGO, PAG_NUMPAGO, PAG_IMPCON, PAG_IMPPAG, PAG_IMPANU, PAG_FECPAG)"
                        +" values("
                        +" "+(pago.getPagMun() != null ? pago.getPagMun().toString() : "")+","
                        +" '"+pago.getPagPro()+"',"
                        +" "+(pago.getPagEje() != null ? pago.getPagEje().toString() : "")+","
                        +" '"+pago.getPagNum()+"',"
                        +" '"+pago.getPagConcep()+"',"
                        +" '"+pago.getPagAnoPago()+"',"
                        +" "+(pago.getPagNumpago() != null ? pago.getPagNumpago().toString() : "")+","
                        +" "+(pago.getPagImpcon() != null ? pago.getPagImpcon().toPlainString() : "")+","
                        +" "+(pago.getPagImppag() != null ? pago.getPagImppag().toPlainString() : "NULL")+","
                        +" "+(pago.getPagImpanu() != null ? pago.getPagImpanu().toPlainString() : "NULL")+","
                        +" "+(pago.getPagFecpag() != null ? "TO_DATE('"+format.format(pago.getPagFecpag())+"', 'dd/mm/yyyy')" : "null")
                        +")";
            }
            else
            {
                query = "update "+ ConfigurationParameter.getParameter(ConstantesMeLanbide34.TABLA_S75_PAGOS, ConstantesMeLanbide34.FICHERO_PROPIEDADES)
                        +" set PAG_IMPCON = "+(pago.getPagImpcon() != null ? pago.getPagImpcon().toPlainString() : "")
                        +" , PAG_IMPPAG = "+(pago.getPagImppag() != null ? pago.getPagImppag().toPlainString() : "NULL")
                        +" , PAG_IMPANU = "+(pago.getPagImpanu() != null ? pago.getPagImpanu().toPlainString() : "NULL")
                        +" , PAG_FECPAG = "+(pago.getPagFecpag() != null ? "TO_DATE('"+format.format(pago.getPagFecpag())+"', 'dd/mm/yyyy')" : "null")                        
                        +" where PAG_MUN = "+(pago.getPagMun() != null ? pago.getPagMun().toString() : "")
                        +" and PAG_PRO = '"+pago.getPagPro()+"'"
                        +" and PAG_EJE = "+(pago.getPagEje() != null ? pago.getPagEje().toString() : "")
                        +" and PAG_NUM = '"+pago.getPagNum()+"'"
                        +" and PAG_CONCEP = '"+pago.getPagConcep()+"'"
                        +" and PAG_ANOPAGO = '"+pago.getPagAnoPago()+"'"
                        +" and PAG_NUMPAGO = "+(pago.getPagNumpago() != null ? pago.getPagNumpago().toString() : "");
            }
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            int result = st.executeUpdate(query);
            return result;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return 0;
    }
    
    public int guardarValorCampoNumerico(int codOrganizacion, String ejercicio, String numExp, String codigoCampo, BigDecimal valor, boolean nuevo, Connection con) throws Exception
    {
        Statement st = null;
        int result = 0;
        try
        {
            String query = null;
            if(nuevo)
            {
                query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide34.TABLA_TIPO_DATO_NUMERICO, ConstantesMeLanbide34.FICHERO_PROPIEDADES)
                    + " values("+codOrganizacion
                    + ", "+ejercicio
                    + ", '"+numExp+"'"
                    + ", '"+codigoCampo+"'"
                    + ", "+valor.toPlainString()
                    + ")";
            }
            else
            {
                query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide34.TABLA_TIPO_DATO_NUMERICO, ConstantesMeLanbide34.FICHERO_PROPIEDADES)
                    + " set TNU_VALOR = "+valor.toPlainString()
                    + " where TNU_MUN = '"+codOrganizacion+"'"
                    + " and TNU_EJE = "+ejercicio
                    + " and TNU_NUM = '"+numExp+"'"
                    + " and TNU_COD = '"+codigoCampo+"'";
            }
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            result = st.executeUpdate(query);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error grabando el campo suplementario ("+numExp+")", ex);
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
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return result;
    }
    
    public int guardarValorCampoDesplegable(int codOrganizacion, String ejercicio, String numExp, String codigoCampo, String valor, boolean nuevo, Connection con) throws Exception
    {
        Statement st = null;
        int result = 0;
        try
        {
            String query = null;
            if(nuevo)
            {
                query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide34.TABLA_TIPO_DATO_DESPLEGABLE, ConstantesMeLanbide34.FICHERO_PROPIEDADES)
                    + " values("+codOrganizacion
                    + ", "+ejercicio
                    + ", '"+numExp+"'"
                    + ", '"+codigoCampo+"'"
                    + ", '"+valor+"'"
                    + ")";
            }
            else
            {
                query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide34.TABLA_TIPO_DATO_DESPLEGABLE, ConstantesMeLanbide34.FICHERO_PROPIEDADES)
                    + " set TDE_VALOR = +'"+valor+"'"
                    + " where TDE_MUN = '"+codOrganizacion+"'"
                    + " and TDE_EJE = "+ejercicio
                    + " and TDE_NUM = '"+numExp+"'"
                    + " and TDE_COD = '"+codigoCampo+"'";
            }
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            result = st.executeUpdate(query);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error grabando el campo suplementario ("+numExp+")", ex);
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
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return result;
    }
    
    public int guardarValorCampoNumericoTramite(int codOrganizacion, String ejercicio, String numExp, Long codigoTramite, String codigoCampo, BigDecimal valor, boolean nuevo, Connection con) throws Exception
    {
        Statement st = null;
        int result = 0;
        try
        {
            String query = null;
            if(nuevo)
            {
                query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide34.TABLA_TIPO_DATO_NUMERICO_TRAMITE, ConstantesMeLanbide34.FICHERO_PROPIEDADES)
                    + " values("+codOrganizacion
                    + ", '"+ConstantesMeLanbide34.CODIGO_PROCEDIMIENTO_MELANBIDE34+"'"
                    + ", "+ejercicio
                    + ", '"+numExp+"'"
                    + ", "+codigoTramite
                    + ", 1"
                    + ", '"+codigoCampo+"'"
                    + ", "+valor.toPlainString()
                    + ")";
            }
            else
            {
                query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide34.TABLA_TIPO_DATO_NUMERICO_TRAMITE, ConstantesMeLanbide34.FICHERO_PROPIEDADES)
                    + " set TNUT_VALOR = "+valor.toPlainString()
                    + " where TNUT_MUN = '"+codOrganizacion+"'"
                    + " and TNUT_PRO = '"+ConstantesMeLanbide34.CODIGO_PROCEDIMIENTO_MELANBIDE34+"'"
                    + " and TNUT_EJE = "+ejercicio
                    + " and TNUT_NUM = '"+numExp+"'"
                    + " and TNUT_TRA = "+codigoTramite
                    + " and TNUT_OCU = 1"
                    + " and TNUT_COD = '"+codigoCampo+"'";
            }
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            result = st.executeUpdate(query);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error grabando el campo suplementario tramite ("+numExp+"/"+codigoTramite+")", ex);
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
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return result;
    }
    
     public int guardarValorCampoFechaTramite(int codOrganizacion, String ejercicio, String numExp, Long codigoTramite, String codigoCampo, Date valor, boolean nuevo, Connection con) throws Exception
    {
        Statement st = null;
        int result = 0;
        try
        {
            String query = null;
            SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide34.FORMATO_FECHA);
            if (valor!=null){
                if(nuevo) {
                        query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide34.TABLA_TIPO_DATO_FECHA_TRAMITE, ConstantesMeLanbide34.FICHERO_PROPIEDADES)
                           // + " (TFET_MUN, TFET_PRO,TFET_EJE, TFET_NUM, TFET_TRA,TFET_OCU, TFET_COD, TFET_VALOR )  "
                            + " values("+codOrganizacion
                            + ", '"+ConstantesMeLanbide34.CODIGO_PROCEDIMIENTO_MELANBIDE34+"'"
                            + ", "+ejercicio
                            + ", '"+numExp+"'"
                            + ", "+codigoTramite
                            + ", 1"
                            + ", '"+codigoCampo+"'"
                            + ", "+(valor != null ? "TO_DATE('"+format.format(valor)+"', 'dd/mm/yyyy')" : "null")
                           // + ", "+valor
                            + ",null, null)";
                }
                else
                { 
                    query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide34.TABLA_TIPO_DATO_FECHA_TRAMITE, ConstantesMeLanbide34.FICHERO_PROPIEDADES)
                           // + " set TFET_VALOR = "+valor
                            + " set TFET_VALOR = "+ (valor != null ? "TO_DATE('"+format.format(valor)+"', 'dd/mm/yyyy')" : "null") 
                            + " where TFET_MUN = '"+codOrganizacion+"'"
                            + " and TFET_PRO = '"+ConstantesMeLanbide34.CODIGO_PROCEDIMIENTO_MELANBIDE34+"'"
                            + " and TFET_EJE = "+ejercicio
                            + " and TFET_NUM = '"+numExp+"'"
                            + " and TFET_TRA = "+codigoTramite
                            + " and TFET_OCU = 1"
                            + " and TFET_COD = '"+codigoCampo+"'";
                }
            }else {//si fecha vacía se elimina campo suplementario
                query ="delete " + ConfigurationParameter.getParameter(ConstantesMeLanbide34.TABLA_TIPO_DATO_FECHA_TRAMITE, ConstantesMeLanbide34.FICHERO_PROPIEDADES)
                     + " where TFET_MUN = '"+codOrganizacion+"'"
                        + " and TFET_PRO = '"+ConstantesMeLanbide34.CODIGO_PROCEDIMIENTO_MELANBIDE34+"'"
                        + " and TFET_EJE = "+ejercicio
                        + " and TFET_NUM = '"+numExp+"'"
                        + " and TFET_TRA = "+codigoTramite
                        + " and TFET_OCU = 1"
                        + " and TFET_COD = '"+codigoCampo+"'";      
            }
            
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            result = st.executeUpdate(query);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error grabando el campo suplementario tramite ("+numExp+"/"+codigoTramite+")", ex);
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
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return result;
    }
    
     public int eliminarValorCampoFechaTramite(int codOrganizacion, String ejercicio, String numExp, Long codigoTramite, String codigoCampo, Connection con) throws Exception
    {
        Statement st = null;
        try
        {
             String query ="delete " + ConfigurationParameter.getParameter(ConstantesMeLanbide34.TABLA_TIPO_DATO_FECHA_TRAMITE, ConstantesMeLanbide34.FICHERO_PROPIEDADES)
                     + " where TFET_MUN = '"+codOrganizacion+"'"
                        + " and TFET_PRO = '"+ConstantesMeLanbide34.CODIGO_PROCEDIMIENTO_MELANBIDE34+"'"
                        + " and TFET_EJE = "+ejercicio
                        + " and TFET_NUM = '"+numExp+"'"
                        + " and TFET_TRA = "+codigoTramite
                        + " and TFET_OCU = 1"
                        + " and TFET_COD = '"+codigoCampo+"'";  
            
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            return st.executeUpdate(query);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error eliminando el campo fecha ");
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
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
     
    public Long getCodigoInternoTramite(int codOrganizacion, String codTramite, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        Long valor = null;
        try
        {
            String query = "select TRA_COD from "+ConfigurationParameter.getParameter(ConstantesMeLanbide34.TABLA_TRAMITES, ConstantesMeLanbide34.FICHERO_PROPIEDADES)
                            +" where TRA_MUN = "+codOrganizacion
                            +" and TRA_PRO = '"+ConstantesMeLanbide34.CODIGO_PROCEDIMIENTO_MELANBIDE34+"'"
                            +" and TRA_COU = "+codTramite
                            +" AND TRA_FBA IS NULL";
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
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
            log.error("Se ha producido un error recuperando el código interno del trámite "+codTramite, ex);
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
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return valor;
    }
    
    public Long getCodigoUltimoTramiteAbierto(int codOrganizacion, String ejercicio, String numExp, Long ocurrencia, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        Long cod = null;
        try
        {
            String query = "select CRO_TRA from "+ConfigurationParameter.getParameter(ConstantesMeLanbide34.TABLA_E_CRO, ConstantesMeLanbide34.FICHERO_PROPIEDADES)
                                +" where CRO_MUN = "+codOrganizacion
                                +" and CRO_PRO = '"+ConstantesMeLanbide34.CODIGO_PROCEDIMIENTO_MELANBIDE34+"'"
                                +" and CRO_EJE = "+ejercicio
                                +" and CRO_NUM = '"+numExp+"'"
                                +" and CRO_OCU = "+ocurrencia
                                +" and CRO_FEF is null";
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            List<Long> listaOcurrencias = new ArrayList<Long>();
            Long act = null;
            while(rs.next())
            {
                act = rs.getLong("CRO_TRA");
                if(!rs.wasNull())
                {
                    listaOcurrencias.add(act);
                }
            }
            if(listaOcurrencias.size() > 0)
            {
                Statement st2 = null;
                ResultSet rs2 = null;
                try
                {

                    String query2 = "select MAX(TRA_COU) as TRA_COU from "+ConfigurationParameter.getParameter(ConstantesMeLanbide34.TABLA_TRAMITES, ConstantesMeLanbide34.FICHERO_PROPIEDADES)
                                    +" where TRA_MUN = "+codOrganizacion
                                    +" and TRA_PRO = '"+ConstantesMeLanbide34.CODIGO_PROCEDIMIENTO_MELANBIDE34+"'"
                                    +" and TRA_COD in(";
                    if(log.isDebugEnabled()) 
                        log.debug("sql = " + query);
                    for(int i = 0; i < listaOcurrencias.size(); i++)
                    {
                        if(i > 0)
                        {
                            query2 += ", ";
                        }
                        query2 += listaOcurrencias.get(i);
                    }
                    query2 += ") and tra_cou not like '90%' ";
                    st2 = con.createStatement();
                    rs2 = st2.executeQuery(query2);
                    if(rs2.next())
                    {
                        cod = rs2.getLong("TRA_COU");
                    }
                }
                catch(Exception ex)
                {
                    log.error("Se ha producido un error recuperando el trámite abierto ("+numExp+")", ex);
                    throw new Exception(ex);
                }
                finally
                {
                    try
                    {
                        if(log.isDebugEnabled()) 
                            log.debug("Procedemos a cerrar el statement y el resultset");
                        if(st2!=null) 
                            st2.close();
                        if(rs2!=null)
                            rs2.close();
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
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando el trámite abierto ("+numExp+")", ex);
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
        return cod;
    }
    
    public List<SelectItem> getListaDesplegable( Connection con, String idLista) throws Exception{
        Statement st = null;
        ResultSet rs = null;
        List<SelectItem> lista = new ArrayList<SelectItem>();        
        String query = "select DES_VAL_COD, DES_NOM as NOMBRE from "+ConfigurationParameter.getParameter(ConstantesMeLanbide34.TABLA_VALORES_DESPLEGABLES, ConstantesMeLanbide34.FICHERO_PROPIEDADES)+
                "   where DES_COD='"+idLista+"' ";
         
         if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            String id = null;
            String nombre = null;
            SelectItem si = null;/*new SelectItem();
            si.setId(-1);
            si.setLabel("");
            lista.add(si);*/
            while(rs.next())
            {
                id = rs.getString("DES_VAL_COD");                
                if(!rs.wasNull())
                {
                    nombre = rs.getString("NOMBRE");
                    si = new SelectItem();
                    si.setId(id);
                    si.setLabel(nombre);
                    lista.add(si);          
                }
               
            }
       return lista;
    }
    
    public List<DatosAviso> getExpDocuTramiteAbierto(int codOrganizacion, String numExp, String tramite, Long ocurrencia, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        Long cod = null;
        List<DatosAviso> listaExpedientes = null;
        try
        {
            String query="SELECT CRO_NUM, TFET_VALOR AS FECESTUDIO, EXP_FEI AS FECSOLICITUD "
                        + " FROM "+ConfigurationParameter.getParameter(ConstantesMeLanbide34.TABLA_E_CRO, ConstantesMeLanbide34.FICHERO_PROPIEDADES)
                        +" left join E_TFET on TFET_NUM=CRO_NUM AND TFET_COD='FECRECEPDOCU1' "     
                        +" inner join E_EXP on EXP_NUM=CRO_NUM "
                        +" WHERE CRO_MUN = "+codOrganizacion
                         +" AND CRO_PRO = '"+ConstantesMeLanbide34.CODIGO_PROCEDIMIENTO_MELANBIDE34+"'"                             
                         +" and CRO_TRA ="+tramite+" AND CRO_OCU = "+ocurrencia
                         +" and CRO_FEF is null";
          
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            listaExpedientes = new ArrayList<DatosAviso>();
            String num = null;
            DatosAviso datos= new DatosAviso();
                 
            rs = st.executeQuery(query);
            while(rs.next())
            {
                datos =  (DatosAviso)MeLanbide34MappingUtils.getInstance().map(rs, DatosAviso.class);
                listaExpedientes.add(datos);
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando el trámite abierto ("+numExp+")", ex);
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
        return listaExpedientes;
    }

    public String getNombreTramite(int codOrganizacion,Long codTramite, Connection con) throws Exception{
        //SELECT * FROM E_TML WHERE TML_PRO='CEI' AND TML_TRA=23;
        
        Statement st = null;
        ResultSet rs = null;
        String valor = "";
        try
        {
            String query = "select TML_VALOR from E_TML "
                            +" where TML_MUN = "+codOrganizacion
                            +" and TML_PRO = '"+ConstantesMeLanbide34.CODIGO_PROCEDIMIENTO_MELANBIDE34+"'"
                            +" and TML_TRA = "+codTramite+"";
                           
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                valor = rs.getString("TML_VALOR");
                if(rs.wasNull())
                {
                    valor = null;
                }
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando el nombre del trámite "+codTramite, ex);
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
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return valor;
    }
    
    public String getEmailUO(int codOrganizacion,String unidadOrg, Connection con) throws Exception{
        //SELECT * FROM E_TML WHERE TML_PRO='CEI' AND TML_TRA=23;
        
        Statement st = null;
        ResultSet rs = null;
        String valor = "";
        try
        {
            String query = "SELECT UOR_EMAIL "
                        + " from A_UOR where UOR_COD_VIS='"+unidadOrg+"' ";
                           
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                valor = rs.getString("UOR_EMAIL");
                if(rs.wasNull())
                {
                    valor = null;
                }
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando el email de "+unidadOrg, ex);
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
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return valor;
    }
    
    public List<FilaMinimisVO> getDatosMinimis(String numExp, int codOrganizacion, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<FilaMinimisVO> lista = new ArrayList<FilaMinimisVO>();
        FilaMinimisVO minimis = new FilaMinimisVO();
        try {
            String query = null;
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide34.MELANBIDE34_SUBSOLIC, ConstantesMeLanbide34.FICHERO_PROPIEDADES)
                    + " WHERE NUM_EXP ='" + numExp + "'"
                    + " ORDER BY ID";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                minimis = (FilaMinimisVO) MeLanbide34MappingUtils.getInstance().map(rs, FilaMinimisVO.class);
                //Cargamos en el request los valores de los desplegables
                lista.add(minimis);
                minimis = new FilaMinimisVO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando Minimis ", ex);
            throw new Exception(ex);
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("Procedemos a cerrar el statement y el resultset");
            }
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return lista;
    }

  public FilaMinimisVO getMinimisPorID(String id, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        FilaMinimisVO minimis = new FilaMinimisVO();
        try {
            String query = null;
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide34.MELANBIDE34_SUBSOLIC, ConstantesMeLanbide34.FICHERO_PROPIEDADES)
                    + " WHERE ID=" + (id != null && !id.equals("") ? id : "null");
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                minimis = (FilaMinimisVO) MeLanbide34MappingUtils.getInstance().map(rs, FilaMinimisVO.class);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando una Minimis : " + id, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return minimis;
    }

    public int eliminarMinimis(String id, Connection con) throws Exception {
        Statement st = null;
        try {
            String query = null;
            query = "DELETE FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide34.MELANBIDE34_SUBSOLIC, ConstantesMeLanbide34.FICHERO_PROPIEDADES)
                    + " WHERE ID=" + (id != null && !id.equals("") ? id : "null");
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            return st.executeUpdate(query);
        } catch (SQLException ex) {
            log.error("Se ha producido un error Eliminando Minimis ID : " + id, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    public boolean crearNuevoMinimis(FilaMinimisVO nuevaMinimis, Connection con) throws Exception {
        Statement st = null;
        String query = "";
        String fechaSub = "";
        if (nuevaMinimis != null && nuevaMinimis.getFecha() != null && !nuevaMinimis.getFecha().equals("")) {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            fechaSub = formatoFecha.format(nuevaMinimis.getFecha());
        }
        try {

            int id = recogerIDInsertar(ConfigurationParameter.getParameter(ConstantesMeLanbide34.SEQ_MELANBIDE34_SUBSOLIC, ConstantesMeLanbide34.FICHERO_PROPIEDADES), con);
            query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide34.MELANBIDE34_SUBSOLIC, ConstantesMeLanbide34.FICHERO_PROPIEDADES)
                    + "(ID,NUM_EXP,ESTADO,ORGANISMO,OBJETO,IMPORTE,FECHA) "
                    + " VALUES (" + id
                    + ", '" + nuevaMinimis.getNumExp()
                    + "', '" + nuevaMinimis.getEstado()
                    + "', '" + nuevaMinimis.getOrganismo()
                    + "', '" + nuevaMinimis.getObjeto()
                    + "', " + nuevaMinimis.getImporte()
                    + " , TO_DATE('" + fechaSub + "','dd/mm/yyyy')"
                    + ")";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            if (insert > 0) {
                return true;
            } else {
                log.error("No Se ha podido guardar una nueva Minimis ");
                return false;
            }

        } catch (Exception ex) {
            log.error("Se ha producido un error al insertar una nueva Minimis" + ex.getMessage());
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    public boolean modificarMinimis(FilaMinimisVO datModif, Connection con) throws Exception {
        Statement st = null;
        String query = "";
        String fechaSub = "";
        if (datModif != null && datModif.getFecha() != null && !datModif.getFecha().toString().equals("")) {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            fechaSub = formatoFecha.format(datModif.getFecha());
        }

        try {
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide34.MELANBIDE34_SUBSOLIC, ConstantesMeLanbide34.FICHERO_PROPIEDADES)
                    + " SET ESTADO='" + datModif.getEstado() + "'"
                    + ", ORGANISMO='" + datModif.getOrganismo() + "'"
                    + ", OBJETO='" + datModif.getObjeto() + "'"
                    + ", IMPORTE=" + datModif.getImporte()
                    + ", FECHA=TO_DATE('" + fechaSub + "','dd/mm/yyyy')"
                    + " WHERE ID=" + datModif.getId();
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            return insert > 0;

        } catch (SQLException ex) {
            log.error("Se ha producido un error al modificar una Minimis - " + datModif.getId() + " - " + ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }
     /**
     *
     * @param sequence
     * @param con
     * @return
     * @throws Exception
     */
    private int recogerIDInsertar(String sequence, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        int id = 0;
        try {
            String query = "SELECT " + sequence + ".NextVal AS PROXID FROM DUAL ";
            log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                id = rs.getInt("PROXID");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando el n?mero de ID para inserci?n al llamar la secuencia " + sequence + " : ", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return id;
    }    
    /**
    *
    * @param des_cod
    * @param con
    * @return
    * @throws Exception
    */
    public List<DesplegableAdmonLocalVO> getValoresDesplegablesAdmonLocalxdes_cod(String des_cod, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<DesplegableAdmonLocalVO> lista = new ArrayList<DesplegableAdmonLocalVO>();
        DesplegableAdmonLocalVO valoresDesplegable = new DesplegableAdmonLocalVO();
        try {
            String query = null;
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide34.TABLA_VALORES_DESPLEGABLES, ConstantesMeLanbide34.FICHERO_PROPIEDADES)
                    + " WHERE DES_COD='" + des_cod + "' order by DES_NOM";
            log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                valoresDesplegable = (DesplegableAdmonLocalVO) MeLanbide34MappingUtils.getInstance().map(rs, DesplegableAdmonLocalVO.class);
                lista.add(valoresDesplegable);
                valoresDesplegable = new DesplegableAdmonLocalVO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando valores de desplegable : " + des_cod, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return lista;
    }
    
}
