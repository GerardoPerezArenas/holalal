/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide39.dao;

import es.altia.agora.business.util.GlobalNames;
import es.altia.flexia.integracion.moduloexterno.melanbide39.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide39.util.ConstantesMeLanbide39;
import es.altia.flexia.integracion.moduloexterno.melanbide39.util.MeLanbide39MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide39.util.MeLanbide39Utils;
import es.altia.flexia.integracion.moduloexterno.melanbide39.vo.CpeJustificacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide39.vo.CpeOfertaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide39.vo.CpePuestoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide39.vo.FilaInformeDatosPuestosVO;
import es.altia.flexia.integracion.moduloexterno.melanbide39.vo.FilaInformeHoja2DatosPuestosVO;
import es.altia.flexia.integracion.moduloexterno.melanbide39.vo.FilaInformeResumenEconomicoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide39.vo.FilaInformeResumenPuestosContratadosVO;
import es.altia.flexia.integracion.moduloexterno.melanbide39.vo.FilaJustificacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide39.vo.FilaOfertaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide39.vo.FilaPersonaContratadaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide39.vo.FilaPuestoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide39.vo.FilaResultadoBusqTitulaciones;
import es.altia.flexia.integracion.moduloexterno.melanbide39.vo.FilaResumenVO;
import es.altia.flexia.integracion.moduloexterno.melanbide39.vo.SalarioVO;
import es.altia.flexia.integracion.moduloexterno.melanbide39.vo.SelectItem;
import es.altia.flexia.integracion.moduloexterno.melanbide59.util.ConstantesMeLanbide59;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.ValorCampoDesplegableModuloIntegracionVO;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author santiagoc
 */
public class MeLanbide39DAO 
{ 
    
    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide39DAO.class);
    
    //Instancia
    private static MeLanbide39DAO instance = null;
    
    private MeLanbide39DAO()
    {
        
    }
    
    public static MeLanbide39DAO getInstance()
    {
        if(instance == null)
        {
            synchronized(MeLanbide39DAO.class)
            {
                instance = new MeLanbide39DAO();
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
            query = "select TNU_VALOR from " + ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_TIPO_DATO_NUMERICO, ConstantesMeLanbide39.FICHERO_PROPIEDADES)
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
            log.error("Se ha producido un error recuperando el campo suplementario numerico "+codigoCampo+" para el expediente "+numExp, ex);
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
    
    public int guardarValorCampoNumerico(int codOrganizacion, String ejercicio, String numExp, String codigoCampo, BigDecimal valor, Connection con) throws Exception
    {
        Statement st = null;
        int result = 0;
        try
        {
            boolean nuevo = false;
            if(this.getValorCampoNumerico(codOrganizacion, numExp, ejercicio, codigoCampo, con) == null)
            {
                nuevo = true;
            }
            String query = null;
            if(nuevo)
            {
                query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_TIPO_DATO_NUMERICO, ConstantesMeLanbide39.FICHERO_PROPIEDADES)
                    + " (TNU_MUN, TNU_EJE, TNU_NUM, TNU_COD, TNU_VALOR)"
                    + " values("+codOrganizacion
                    + ", "+ejercicio
                    + ", '"+numExp+"'"
                    + ", '"+codigoCampo+"'"
                    + ", "+valor.toPlainString()
                    + ")";
            }
            else
            {
                query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_TIPO_DATO_NUMERICO, ConstantesMeLanbide39.FICHERO_PROPIEDADES)
                    + " set TNU_VALOR = "+valor.toPlainString()
                    + " where TNU_MUN = '"+codOrganizacion+"'"
                    + " and TNU_EJE = "+ejercicio
                    + " and TNU_NUM = '"+numExp+"'"
                    + " and TNU_COD = '"+codigoCampo+"'";
            }
            if(log.isDebugEnabled()) 
                log.debug("sql guardarValorCampoNumerico= " + query);
            st = con.createStatement();
            result = st.executeUpdate(query);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error grabando el campo suplementario numerico "+codigoCampo+" para el expediente "+numExp, ex);
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
    
    public int eliminarValorCampoNumerico(int codOrganizacion, String ejercicio, String numExp, String codigoCampo, Connection con) throws Exception
    {
        Statement st = null;
        int result = 0;
        try
        {
            String query = null;
            query = "delete from " + ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_TIPO_DATO_NUMERICO, ConstantesMeLanbide39.FICHERO_PROPIEDADES)
                + " where TNU_MUN = "+codOrganizacion
                + " and TNU_EJE = "+ejercicio
                + " and TNU_NUM = '"+numExp+"'"
                + " and TNU_COD = '"+codigoCampo+"'";
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            result = st.executeUpdate(query);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error eliminando el campo suplementario numerico "+codigoCampo+" para el expediente "+numExp, ex);
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
    
    public String getValorCampoTexto(int codOrganizacion, String numExp, String ejercicio, String codigoCampo, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        String valor = null;
        try
        {
            String query = null;
            query = "select TXT_VALOR from " + ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_TIPO_DATO_TEXTO, ConstantesMeLanbide39.FICHERO_PROPIEDADES)
                    + " where TXT_MUN = '" + codOrganizacion + "' and TXT_EJE = '" + ejercicio 
                    + "' and TXT_NUM = '"+numExp+"' and TXT_COD = '"+codigoCampo+"'";
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
                valor = rs.getString("TXT_VALOR");
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando el campo suplementario texto "+codigoCampo+" para el expediente "+numExp, ex);
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
    
    //Leire, modificación importe justificado
    /*public void guardarImporteJustificado (int codOrganizacion, String ejercicio, String numExp, String codigoCampo, String valor, Connection con)
    {
        Statement st = null;
        try
        {
            boolean nuevo = false;
            String query = null;
            if(this.getValorCampoTexto(codOrganizacion, numExp, ejercicio, codigoCampo, con) == null)
            {
                nuevo = true;
            }
            if(nuevo)
            {
                query
            }
            
        }catch(Exception ex){
            log.error("Error en funcion guardarImporteJustificado "+ex);
        }
    
    }*/
    
    public int guardarValorCampoTexto(int codOrganizacion, String ejercicio, String numExp, String codigoCampo, String valor, Connection con) throws Exception
    {
        Statement st = null;
        int result = 0;
        try
        {
            boolean nuevo = false;
            if(this.getValorCampoTexto(codOrganizacion, numExp, ejercicio, codigoCampo, con) == null)
            {
                nuevo = true;
            }
            String query = null;
            if(nuevo)
            {
                query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_TIPO_DATO_TEXTO, ConstantesMeLanbide39.FICHERO_PROPIEDADES)
                    + " (TXT_MUN, TXT_EJE, TXT_NUM, TXT_COD, TXT_VALOR)"
                    + " values("+codOrganizacion
                    + ", "+ejercicio
                    + ", '"+numExp+"'"
                    + ", '"+codigoCampo+"'"
                    + ", '"+valor+"'"
                    + ")";
            }
            else
            {
                query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_TIPO_DATO_TEXTO, ConstantesMeLanbide39.FICHERO_PROPIEDADES)
                    + " set TXT_VALOR = '"+valor+"'"
                    + " where TXT_MUN = '"+codOrganizacion+"'"
                    + " and TXT_EJE = "+ejercicio
                    + " and TXT_NUM = '"+numExp+"'"
                    + " and TXT_COD = '"+codigoCampo+"'";
            }
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            result = st.executeUpdate(query);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error grabando el campo suplementario texto "+codigoCampo+" para el expediente "+numExp, ex);
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
    
    public int eliminarValorCampoTexto(int codOrganizacion, String ejercicio, String numExp, String codigoCampo, Connection con) throws Exception
    {
        Statement st = null;
        int result = 0;
        try
        {
            String query = null;
            query = "delete from " + ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_TIPO_DATO_TEXTO, ConstantesMeLanbide39.FICHERO_PROPIEDADES)
                + " where TXT_MUN = "+codOrganizacion
                + " and TXT_EJE = "+ejercicio
                + " and TXT_NUM = '"+numExp+"'"
                + " and TXT_COD = '"+codigoCampo+"'";
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            result = st.executeUpdate(query);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error eliminando el campo suplementario numerico "+codigoCampo+" para el expediente "+numExp, ex);
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
    
    public String getValorCampoDesplegable(int codOrganizacion, String numExp, String ejercicio, String codigoCampo, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        String valor = null;
        try
        {
            String query = null;
            query = "select TDE_VALOR from " + ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_TIPO_DATO_DESPLEGABLE, ConstantesMeLanbide39.FICHERO_PROPIEDADES)
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
            log.error("Se ha producido un error recuperando el campo suplementario desplegable "+codigoCampo+" para el expediente "+numExp, ex);
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
    
    public int guardarValorCampoDesplegable(int codOrganizacion, String ejercicio, String numExp, String codigoCampo, String valor, Connection con) throws Exception
    {
        Statement st = null;
        int result = 0;
        try
        {
            boolean nuevo = false;
            if(this.getValorCampoDesplegable(codOrganizacion, numExp, ejercicio, codigoCampo, con) == null)
            {
                nuevo = true;
            }
            String query = null;
            if(nuevo)
            {
                query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_TIPO_DATO_DESPLEGABLE, ConstantesMeLanbide39.FICHERO_PROPIEDADES)
                    + " (TDE_MUN, TDE_EJE, TDE_NUM, TDE_COD, TDE_VALOR)"
                    + " values("+codOrganizacion
                    + ", "+ejercicio
                    + ", '"+numExp+"'"
                    + ", '"+codigoCampo+"'"
                    + ", '"+valor+"'"
                    + ")";
            }
            else
            {
                query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_TIPO_DATO_DESPLEGABLE, ConstantesMeLanbide39.FICHERO_PROPIEDADES)
                    + " set TDE_VALOR = '"+valor+"'"
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
            log.error("Se ha producido un error grabando el campo suplementario desplegable "+codigoCampo+" para el expediente "+numExp, ex);
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
    
    public int eliminarValorCampoDesplegable(int codOrganizacion, String ejercicio, String numExp, String codigoCampo, Connection con) throws Exception
    {
        Statement st = null;
        int result = 0;
        try
        {
            String query = null;
            query = "delete from " + ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_TIPO_DATO_DESPLEGABLE, ConstantesMeLanbide39.FICHERO_PROPIEDADES)
                + " where TDE_MUN = "+codOrganizacion
                + " and TDE_EJE = "+ejercicio
                + " and TDE_NUM = '"+numExp+"'"
                + " and TDE_COD = '"+codigoCampo+"'";
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            result = st.executeUpdate(query);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error eliminando el campo suplementario desplegable "+codigoCampo+" para el expediente "+numExp, ex);
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
    
    public Date getValorCampoFecha(int codOrganizacion, String numExp, String ejercicio, String codigoCampo, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        Date valor = null;
        try
        {
            String query = null;
            query = "select TFE_VALOR from " + ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_TIPO_DATO_FECHA, ConstantesMeLanbide39.FICHERO_PROPIEDADES)
                    + " where TFE_MUN = '" + codOrganizacion + "' and TFE_EJE = '" + ejercicio 
                    + "' and TFE_NUM = '"+numExp+"' and TFE_COD = '"+codigoCampo+"'";
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
                valor = rs.getDate("TFE_VALOR");
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando el campo suplementario fecha "+codigoCampo+" para el expediente "+numExp, ex);
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
    
    public int guardarValorCampoFecha(int codOrganizacion, String ejercicio, String numExp, String codigoCampo, Date valor, Connection con) throws Exception
    {
        Statement st = null;
        int result = 0;
        try
        {
            boolean nuevo = false;
            if(this.getValorCampoFecha(codOrganizacion, numExp, ejercicio, codigoCampo, con) == null)
            {
                nuevo = true;
            }
            String query = null;
            SimpleDateFormat formatoFecha = new SimpleDateFormat(ConstantesMeLanbide39.FORMATO_FECHA);
            if(nuevo)
            {
                query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_TIPO_DATO_FECHA, ConstantesMeLanbide39.FICHERO_PROPIEDADES)
                    + " (TFE_MUN, TFE_EJE, TFE_NUM, TFE_COD, TFE_VALOR)"
                    + " values("+codOrganizacion
                    + ", "+ejercicio
                    + ", '"+numExp+"'"
                    + ", '"+codigoCampo+"'"
                    + ", TO_DATE('"+formatoFecha.format(valor)+"', '"+ConstantesMeLanbide39.FORMATO_FECHA+"')"
                    + ")";
            }
            else
            {
                query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_TIPO_DATO_FECHA, ConstantesMeLanbide39.FICHERO_PROPIEDADES)
                    + " set TFE_VALOR = TO_DATE('"+formatoFecha.format(valor)+"', '"+ConstantesMeLanbide39.FORMATO_FECHA+"')"
                    + " where TFE_MUN = '"+codOrganizacion+"'"
                    + " and TFE_EJE = "+ejercicio
                    + " and TFE_NUM = '"+numExp+"'"
                    + " and TFE_COD = '"+codigoCampo+"'";
            }
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            result = st.executeUpdate(query);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error grabando el campo suplementario fecha "+codigoCampo+" para el expediente "+numExp, ex);
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
    
    public int eliminarValorCampoFecha(int codOrganizacion, String ejercicio, String numExp, String codigoCampo, Connection con) throws Exception
    {
        Statement st = null;
        int result = 0;
        try
        {
            String query = null;
            query = "delete from " + ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_TIPO_DATO_FECHA, ConstantesMeLanbide39.FICHERO_PROPIEDADES)
                + " where TFE_MUN = "+codOrganizacion
                + " and TFE_EJE = "+ejercicio
                + " and TFE_NUM = '"+numExp+"'"
                + " and TFE_COD = '"+codigoCampo+"'";
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            result = st.executeUpdate(query);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error eliminando el campo suplementario fecha "+codigoCampo+" para el expediente "+numExp, ex);
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
    
    public List<SelectItem> getListaPaises(Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        List<ValorCampoDesplegableModuloIntegracionVO> listaValores = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
        try
        {
            String query = "select PAI_COD as CODIGO, PAI_NOM as DESCRIPCION from "+ GlobalNames.ESQUEMA_GENERICO + ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_PAISES, ConstantesMeLanbide39.FICHERO_PROPIEDADES);
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while(rs.next())
            {
                listaValores.add((ValorCampoDesplegableModuloIntegracionVO)MeLanbide39MappingUtils.getInstance().map(rs, ValorCampoDesplegableModuloIntegracionVO.class));
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando la lista de paises", ex);
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
        return MeLanbide39MappingUtils.getInstance().fromCampoDesplegableToSelectItemVO(listaValores);
    }
    
    public Map<String, BigDecimal> cargarCalculosCpe(int codOrganizacion, Integer ejercicio, String numExpediente, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        Map<String, BigDecimal> calculos = new HashMap<String, BigDecimal>();
        try
        {
            String query =null;
            BigDecimal valor = null;
            String codigoCampo = null;
            
            //Importe solicitado
                query = "select nvl(sum(CPE_PTO_IMP_TOT_SOLIC), 0) as IMSOL from "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_PUESTO, ConstantesMeLanbide39.FICHERO_PROPIEDADES)
                   +" where CPE_PTO_NUMEXP = '"+numExpediente+"'";
            
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                valor = rs.getBigDecimal("IMSOL");
                if(valor != null)
                {
                    calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_IMP_SOLICITADO, new BigDecimal(MeLanbide39Utils.redondearDecimalesString(valor, 2)));
                }
                else
                {
                    calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_IMP_SOLICITADO, new BigDecimal(ConstantesMeLanbide39.CERO_CON_DECIMALES));
                }
            }
            else
            {
                calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_IMP_SOLICITADO, new BigDecimal(ConstantesMeLanbide39.CERO_CON_DECIMALES));
            }
            
            //importe convocatoria
            if(ejercicio != null)
            {
                    /*query = "select nvl(sum(TNU_VALOR), 0) as IMCONV from "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_TIPO_DATO_NUMERICO, ConstantesMeLanbide39.FICHERO_PROPIEDADES) +" where TNU_EJE = '"+ejercicio.toString()
                            +"' and TNU_COD = '"+ConfigurationParameter.getParameter(ConstantesMeLanbide39.CAMPO_SUPL_IMP_CON, ConstantesMeLanbide39.FICHERO_PROPIEDADES) +"'";*/
                
                query = "select sum(IMPORTE_CONCEDIDO) as IMCONV from ("

                      +" select"

                      +" case when CPE_PTO_COD_RESULT IN ('"+ConstantesMeLanbide39.CODIGO_RESULTADO_RENUNCIA+"')then 0"
                      +" when CPE_PTO_COD_RESULT IN ('"+ConstantesMeLanbide39.CODIGO_RESULTADO_DENEGADO+"')then 0"
                      +" when CPE_PTO_COD_RESULT IN ('"+ConstantesMeLanbide39.CODIGO_RESULTADO_NO_EVALUADO+"')then 0"
                      +" ELSE("
                      + "nvl(case when nvl(CPE_PTO_IMP_MAX_SUBV, 999999999) > nvl(CPE_PTO_IMP_TOT_SOLIC, 9999999999) then CPE_PTO_IMP_TOT_SOLIC else CPE_PTO_IMP_MAX_SUBV end, 0))"
//						+" WHEN"
//                      +" NVL(CPE_PTO_SAL_SOLIC,0)+NVL(CPE_PTO_DIETAS_SOLIC,0)+NVL(CPE_PTO_TRAM_SOLIC,0)>"
//                      +" NVL(CPE_PTO_SALARIO_ANEX_TOT,0)+NVL(CPE_PTO_DIETAS_MANUT,0)+NVL(CPE_PTO_TRAM_SEGUROS,0)"
//                      +" THEN NVL(CPE_PTO_SALARIO_ANEX_TOT,0)+NVL(CPE_PTO_DIETAS_MANUT,0)+NVL(CPE_PTO_TRAM_SEGUROS,0)"
//                      +" ELSE NVL(CPE_PTO_SAL_SOLIC,0)+NVL(CPE_PTO_DIETAS_SOLIC,0)+NVL(CPE_PTO_TRAM_SOLIC,0)"
                      +" END IMPORTE_CONCEDIDO"
                      +" from "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_PUESTO, ConstantesMeLanbide39.FICHERO_PROPIEDADES)+" puesto"
                      +" where cpe_pto_exp_eje = "+ejercicio.toString()
                      +" )";
                
                if(log.isDebugEnabled()) 
                    log.debug("sql = " + query);
                st = con.createStatement();
                rs = st.executeQuery(query);
                if(rs.next())
                {
                    valor = rs.getBigDecimal("IMCONV");
                    if(valor != null)
                    {
                        calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_IMP_CONVOCATORIA, new BigDecimal(MeLanbide39Utils.redondearDecimalesString(valor, 2)));
                    }
                    else
                    {
                        calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_IMP_CONVOCATORIA, new BigDecimal(ConstantesMeLanbide39.CERO_CON_DECIMALES));
                    }
                }
                else
                {
                    calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_IMP_CONVOCATORIA, new BigDecimal(ConstantesMeLanbide39.CERO_CON_DECIMALES));
                }
            }
            else
            {
                calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_IMP_CONVOCATORIA, new BigDecimal(ConstantesMeLanbide39.CERO_CON_DECIMALES));
            }
            
            //importe previsto concesion
                /*query = "select nvl(sum(CPE_PTO_IMP_MAX_SUBV), 0) as IMCON from "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_PUESTO, ConstantesMeLanbide39.FICHERO_PROPIEDADES) 
                        +" where CPE_PTO_NUMEXP = '"+numExpediente+"'" 
                   +" and CPE_PTO_COD_RESULT = '"+ConstantesMeLanbide39.CODIGO_RESULTADO_CONCEDIDO+"'";*/
            
            /*query = "select nvl(sum(CPE_PTO_IMP_MAX_SUBV), 0) as IMCON from "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_PUESTO, ConstantesMeLanbide39.FICHERO_PROPIEDADES)+" p"
                   +" inner join "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_OFERTA, ConstantesMeLanbide39.FICHERO_PROPIEDADES)+" o"
                   +" on o.CPE_OFE_COD_PUESTO = CPE_PTO_COD_PUESTO and o.CPE_OFE_NUMEXP = p.CPE_PTO_NUMEXP"
                   +" where CPE_PTO_NUMEXP = '"+numExpediente+"'"
                   +" and CPE_PTO_COD_RESULT = '"+ConstantesMeLanbide39.CODIGO_RESULTADO_CONCEDIDO+"'"
                   +" and CPE_OFE_FEC_BAJA is null"
                   +" and CPE_OFE_COD_CAUSA_BAJA is null";*/
            
            query = "select nvl(sum("
                    +" case when nvl(CPE_PTO_IMP_MAX_SUBV, 0) < nvl(CPE_PTO_IMP_TOT_SOLIC, 0) then CPE_PTO_IMP_MAX_SUBV"
                    +" else CPE_PTO_IMP_TOT_SOLIC end"
                    +" ), 0) as IMCON from "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_PUESTO, ConstantesMeLanbide39.FICHERO_PROPIEDADES)+" p"
                    +" inner join "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_OFERTA, ConstantesMeLanbide39.FICHERO_PROPIEDADES)+" o"
                   +" on o.CPE_OFE_COD_PUESTO = CPE_PTO_COD_PUESTO and o.CPE_OFE_NUMEXP = p.CPE_PTO_NUMEXP"
                    +" where CPE_PTO_NUMEXP = '"+numExpediente+"'"
                    +" and CPE_PTO_COD_RESULT = '"+ConstantesMeLanbide39.CODIGO_RESULTADO_CONCEDIDO+"'"
                    +" and CPE_OFE_ID_OFERTA=1 AND CPE_OFE_COD_CAUSA_RENUNCIA IS NULL ";
            
            
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                valor = rs.getBigDecimal("IMCON");
                if(valor != null)
                {
                    calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_IMP_PREV_CON, new BigDecimal(MeLanbide39Utils.redondearDecimalesString(valor, 2)));
                }
                else
                {
                    calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_IMP_PREV_CON, new BigDecimal(ConstantesMeLanbide39.CERO_CON_DECIMALES));
                }
            }
            else
            {
                calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_IMP_PREV_CON, new BigDecimal(ConstantesMeLanbide39.CERO_CON_DECIMALES));
            }
            
            //otras ayudas solicitado
            if(ejercicio != null)
            {
                codigoCampo = ConfigurationParameter.getParameter(ConstantesMeLanbide39.CAMPO_SUPL_OTRAS_AYUDAS_SOLIC, ConstantesMeLanbide39.FICHERO_PROPIEDADES);
                valor = this.getValorCampoNumerico(codOrganizacion, numExpediente, String.valueOf(ejercicio), codigoCampo, con);
                if(valor != null)
                {
                    calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_OTRAS_AYUDAS_SOLIC, new BigDecimal(MeLanbide39Utils.redondearDecimalesString(valor, 2)));
                }
                else
                {
                    calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_OTRAS_AYUDAS_SOLIC, new BigDecimal(ConstantesMeLanbide39.CERO_CON_DECIMALES));
                }
            }
            else
            {
                calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_OTRAS_AYUDAS_SOLIC, new BigDecimal(ConstantesMeLanbide39.CERO_CON_DECIMALES));
            }
            
            //otras ayudas concedido
            if(ejercicio != null)
            {
                codigoCampo = ConfigurationParameter.getParameter(ConstantesMeLanbide39.CAMPO_SUPL_OTRAS_AYUDAS_CONCE, ConstantesMeLanbide39.FICHERO_PROPIEDADES);
                valor = this.getValorCampoNumerico(codOrganizacion, numExpediente, String.valueOf(ejercicio), codigoCampo, con);
                if(valor != null)
                {
                    calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_OTRAS_AYUDAS_CONCE, new BigDecimal(MeLanbide39Utils.redondearDecimalesString(valor, 2)));
                }
                else
                {
                    calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_OTRAS_AYUDAS_CONCE, new BigDecimal(ConstantesMeLanbide39.CERO_CON_DECIMALES));
                }
            }
            else
            {
                calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_OTRAS_AYUDAS_CONCE, new BigDecimal(ConstantesMeLanbide39.CERO_CON_DECIMALES));
            }
            
            //minimis solicitado
            if(ejercicio != null)
            {
                codigoCampo = ConfigurationParameter.getParameter(ConstantesMeLanbide39.CAMPO_SUPL_MINIMIS_SOLIC, ConstantesMeLanbide39.FICHERO_PROPIEDADES);
                valor = this.getValorCampoNumerico(codOrganizacion, numExpediente, String.valueOf(ejercicio), codigoCampo, con);
                if(valor != null)
                {
                    calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_MINIMIS_SOLIC, new BigDecimal(MeLanbide39Utils.redondearDecimalesString(valor, 2)));
                }
                else
                {
                    calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_MINIMIS_SOLIC, new BigDecimal(ConstantesMeLanbide39.CERO_CON_DECIMALES));
                }
            }
            else
            {
                calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_MINIMIS_SOLIC, new BigDecimal(ConstantesMeLanbide39.CERO_CON_DECIMALES));
            }
            
            //minimis concedido
            if(ejercicio != null)
            {
                codigoCampo = ConfigurationParameter.getParameter(ConstantesMeLanbide39.CAMPO_SUPL_MINIMIS_CONCE, ConstantesMeLanbide39.FICHERO_PROPIEDADES);
                valor = this.getValorCampoNumerico(codOrganizacion, numExpediente, String.valueOf(ejercicio), codigoCampo, con);
                if(valor != null)
                {
                    calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_MINIMIS_CONCE, new BigDecimal(MeLanbide39Utils.redondearDecimalesString(valor, 2)));
                }
                else
                {
                    calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_MINIMIS_CONCE, new BigDecimal(ConstantesMeLanbide39.CERO_CON_DECIMALES));
                }
            }
            else
            {
                calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_MINIMIS_CONCE, new BigDecimal(ConstantesMeLanbide39.CERO_CON_DECIMALES));
            }
            
           
            
            //importe renunciado (solicitud)
            /*query = "select nvl(sum(CPE_PTO_IMP_MAX_SUBV), 0) as IMREN from "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_PUESTO, ConstantesMeLanbide39.FICHERO_PROPIEDADES) +" where CPE_PTO_NUMEXP = '"+numExpediente+"'" 
               +" and CPE_PTO_COD_RESULT = '"+ConstantesMeLanbide39.CODIGO_RESULTADO_RENUNCIA+"'";*/
            
            
            query = "select nvl(sum(case when nvl(CPE_PTO_IMP_MAX_SUBV, 999999999) > nvl(CPE_PTO_IMP_TOT_SOLIC, 9999999999) then CPE_PTO_IMP_TOT_SOLIC else CPE_PTO_IMP_MAX_SUBV end), 0) as IMREN from "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_PUESTO, ConstantesMeLanbide39.FICHERO_PROPIEDADES) +" where CPE_PTO_NUMEXP = '"+numExpediente+"'" 
               +" and CPE_PTO_COD_RESULT = '"+ConstantesMeLanbide39.CODIGO_RESULTADO_RENUNCIA+"'";
                
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                valor = rs.getBigDecimal("IMREN");
                if(valor != null)
                {
                    calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_IMP_REN, new BigDecimal(MeLanbide39Utils.redondearDecimalesString(valor, 2)));
                }
                else
                {
                    calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_IMP_REN, new BigDecimal(ConstantesMeLanbide39.CERO_CON_DECIMALES));
                }
            }
            else
            {
                calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_IMP_REN, new BigDecimal(ConstantesMeLanbide39.CERO_CON_DECIMALES));
            }
            
            //importe renunciado (resolucion)
            /*query = "select nvl(sum("
                                  + " case when nvl(CPE_PTO_IMP_MAX_SUBV, 999999999) > nvl(CPE_PTO_IMP_TOT_SOLIC, 9999999999)"
                                  + " then CPE_PTO_IMP_TOT_SOLIC else CPE_PTO_IMP_MAX_SUBV end), 0) as IMRENRES"
                   +" from "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_PUESTO, ConstantesMeLanbide39.FICHERO_PROPIEDADES) 
                   +" p inner join "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_OFERTA, ConstantesMeLanbide39.FICHERO_PROPIEDADES) 
                   +" o on p.CPE_PTO_COD_PUESTO = o.CPE_OFE_COD_PUESTO and p.CPE_PTO_NUMEXP = '"+numExpediente+"'"
                   +" and CPE_OFE_CONTRATACION = '"+ConstantesMeLanbide39.FALSO+"'";*/
             BigDecimal impteRenunciadoResolucion=BigDecimal.ZERO;
            query = "select"
                    +" nvl(sum("
                            +" case when nvl(CPE_PTO_IMP_MAX_SUBV, 999999999) > nvl(CPE_PTO_IMP_TOT_SOLIC, 9999999999)"
                            +" then CPE_PTO_IMP_TOT_SOLIC else CPE_PTO_IMP_MAX_SUBV end), 0) as IMRENRES"
                    +" from "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_PUESTO, ConstantesMeLanbide39.FICHERO_PROPIEDADES)
                    +" p inner join "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_OFERTA, ConstantesMeLanbide39.FICHERO_PROPIEDADES)
                    +" o on p.CPE_PTO_COD_PUESTO = o.CPE_OFE_COD_PUESTO and p.CPE_PTO_NUMEXP = o.CPE_OFE_NUMEXP"
                    +" where p.CPE_PTO_NUMEXP = '"+numExpediente+"' and CPE_PTO_COD_RESULT='001' and (CPE_OFE_CONTRATACION = '"+ConstantesMeLanbide39.FALSO+"' or CPE_OFE_CONTRATACION_PRES_OFE = '"+ConstantesMeLanbide39.FALSO+"')"; 
                
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                valor = rs.getBigDecimal("IMRENRES");
                if(valor != null)
                {
                    calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_IMP_REN_RES, new BigDecimal(MeLanbide39Utils.redondearDecimalesString(valor, 2)));
                    impteRenunciadoResolucion=new BigDecimal(MeLanbide39Utils.redondearDecimalesString(valor, 2));
                }
                else
                {
                    calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_IMP_REN_RES, new BigDecimal(ConstantesMeLanbide39.CERO_CON_DECIMALES));
                }
            }
            else
            {
                calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_IMP_REN_RES, new BigDecimal(ConstantesMeLanbide39.CERO_CON_DECIMALES));
            }
            
             //importe concedido
            valor = null;
            if(ejercicio != null)
            {
                codigoCampo = ConfigurationParameter.getParameter(ConstantesMeLanbide39.CAMPO_SUPL_IMP_CON, ConstantesMeLanbide39.FICHERO_PROPIEDADES);
                valor = this.getValorCampoNumerico(codOrganizacion, numExpediente, String.valueOf(ejercicio), codigoCampo, con);
            }
            BigDecimal prevConcedido = calculos.get(ConstantesMeLanbide39.CAMPO_SUPL_IMP_PREV_CON);
            if(prevConcedido != null && prevConcedido.doubleValue() >= 0)
            {
                //Esto indica lo que deberia valer el importe concedido.
                //En la jsp se compararÃ¡ este valor con el importe concedido que se muestra. Si no coinciden,
                //la caja de texto del importe concedido aparecerÃ¡ en rojo para indicarlo.
                
                BigDecimal oayu = calculos.get(ConstantesMeLanbide39.CAMPO_SUPL_OTRAS_AYUDAS_CONCE) != null ? calculos.get(ConstantesMeLanbide39.CAMPO_SUPL_OTRAS_AYUDAS_CONCE) : new BigDecimal(ConstantesMeLanbide39.CERO_CON_DECIMALES);
                prevConcedido = prevConcedido.subtract(oayu);
                // #245131 si en oferta renuncia por alguna razón restar importe DE RENUNCIA(RESOLUCION)     
                prevConcedido = prevConcedido.subtract(impteRenunciadoResolucion);
                if(prevConcedido.doubleValue() < 0)
                {
                    prevConcedido = new BigDecimal(ConstantesMeLanbide39.CERO_CON_DECIMALES);
                }
                    
                //Si ha solicitado menos que lo concedido, entonces se le concede como máximo lo solicitado
                BigDecimal solic = calculos.get(ConstantesMeLanbide39.CAMPO_SUPL_IMP_SOLICITADO);
                if(solic != null && prevConcedido.compareTo(solic) > 0)
                {
                    prevConcedido = new BigDecimal(solic.toPlainString());
                }
                calculos.put("CONCEDIDO_REAL", new BigDecimal(MeLanbide39Utils.redondearDecimalesString(prevConcedido, 2)));
                if(valor != null)
                {
                    calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_IMP_CON, new BigDecimal(MeLanbide39Utils.redondearDecimalesString(valor, 2)));
                }
                else
                {
                    calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_IMP_CON, new BigDecimal(MeLanbide39Utils.redondearDecimalesString(prevConcedido, 2)));
                }
            }
            else
            {
                calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_IMP_CON, new BigDecimal(ConstantesMeLanbide39.CERO_CON_DECIMALES));
            }
            
            
            
            //importe por despido
            valor = calcularImportePuestosBaja(numExpediente, ejercicio, ConstantesMeLanbide39.CODIGO_CAUSA_BAJA_DESPIDO, con);
            calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_IMP_DESP, new BigDecimal(MeLanbide39Utils.redondearDecimalesString(valor, 2)));
            
            //importe por bajas
            valor = calcularImportePuestosBaja(numExpediente, ejercicio, ConstantesMeLanbide39.CODIGO_CAUSA_BAJA_CESE_VOLUNTARIO, con);
            calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_IMP_BAJA, new BigDecimal(MeLanbide39Utils.redondearDecimalesString(valor, 2)));
            
            //importe justificado
            //query = "select nvl(sum(CPE_JUS_IMP_JUSTIFICADO), 0) as IMJUS from "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_JUSTIFICACION, ConstantesMeLanbide39.FICHERO_PROPIEDADES) +" where CPE_JUS_NUMEXP = '"+numExpediente+"'";
            
            query = "select nvl(sum(CPE_JUS_IMP_JUSTIFICADO), 0) as IMJUS"
                   +" from CPE_JUSTIFICACION j"
                   +" inner join CPE_OFERTA o on CPE_JUS_COD_PUESTO = CPE_OFE_COD_PUESTO AND j.CPE_JUS_ID_OFERTA = CPE_OFE_ID_OFERTA and j.CPE_JUS_NUMEXP = o.CPE_OFE_NUMEXP"
                   +" inner join CPE_PUESTO p on o.CPE_OFE_COD_PUESTO = CPE_PTO_COD_PUESTO and o.CPE_OFE_NUMEXP = p.CPE_PTO_NUMEXP"
                   +" where CPE_JUS_NUMEXP = '"+numExpediente+"'"
                   +" and CPE_OFE_NIF is not null"
                   +" and p.CPE_PTO_COD_RESULT <> '"+ConstantesMeLanbide39.CODIGO_RESULTADO_DENEGADO+"'";
                    
                    //suponemos justificado cuando tiene un importe al menos alguna contratacion (para el calculo del importe general justificado)
                    //+" and CPE_JUS_COD_ESTADO = '"+ConstantesMeLanbide39.CODIGO_ESTADO_JUSTIFICADO+"'";
            
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                valor = rs.getBigDecimal("IMJUS");
                if(valor != null)
                {
                    calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_IMP_JUS, new BigDecimal(MeLanbide39Utils.redondearDecimalesString(valor, 2)));
                }
                else
                {
                    calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_IMP_JUS, new BigDecimal(ConstantesMeLanbide39.CERO_CON_DECIMALES));
                }
            }
            else
            {
                calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_IMP_JUS, new BigDecimal(ConstantesMeLanbide39.CERO_CON_DECIMALES));
            }
            
            //importe no justificado
            valor = null;
            BigDecimal impConcedido = calculos.get(ConstantesMeLanbide39.CAMPO_SUPL_IMP_CON);
            if(impConcedido == null)
            {
                impConcedido = new BigDecimal(ConstantesMeLanbide39.CERO_CON_DECIMALES);
            }
            BigDecimal impJustificado  = calculos.get(ConstantesMeLanbide39.CAMPO_SUPL_IMP_JUS);
            if(impJustificado == null)
            {
                impJustificado = new BigDecimal(ConstantesMeLanbide39.CERO_CON_DECIMALES);
            }
            
            valor = impConcedido.subtract(impJustificado);
            if(valor != null)
            {
                if(valor.doubleValue() < 0.0)
                {
                    valor = new BigDecimal(ConstantesMeLanbide39.CERO_CON_DECIMALES);
                }
                calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_IMP_NO_JUS, new BigDecimal(MeLanbide39Utils.redondearDecimalesString(valor, 2)));
            }
            
            //importe pagado
            valor = null;
            if(ejercicio != null)
            {
                codigoCampo = ConfigurationParameter.getParameter(ConstantesMeLanbide39.CAMPO_SUPL_IMP_PAG, ConstantesMeLanbide39.FICHERO_PROPIEDADES);
                valor = this.getValorCampoNumerico(codOrganizacion, numExpediente, String.valueOf(ejercicio), codigoCampo, con);
            }
            if(valor != null)
            {
                calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_IMP_PAG, new BigDecimal(MeLanbide39Utils.redondearDecimalesString(valor, 2)));
            }
            
            //importe pagado resolución
            valor = null;
            if(ejercicio != null)
            {
                codigoCampo = ConfigurationParameter.getParameter(ConstantesMeLanbide39.CAMPO_SUPL_IMP_PAG_RES, ConstantesMeLanbide39.FICHERO_PROPIEDADES);
                valor = this.getValorCampoNumerico(codOrganizacion, numExpediente, String.valueOf(ejercicio), codigoCampo, con);
            }
            if(valor != null)
            {
                calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_IMP_PAG_RES, new BigDecimal(MeLanbide39Utils.redondearDecimalesString(valor, 2)));
            }
            
            //Pagado real
            BigDecimal concedido = calculos.get(ConstantesMeLanbide39.CAMPO_SUPL_IMP_CON);
            if(concedido != null)
            {
                BigDecimal pagado = concedido.multiply(new BigDecimal("0.8"));
                calculos.put("PAGADO_REAL", new BigDecimal(MeLanbide39Utils.redondearDecimalesString(pagado, 2)));
            }
            else
            {
                calculos.put("PAGADO_REAL", new BigDecimal(ConstantesMeLanbide39.CERO_CON_DECIMALES));
            }
            
            
            
            //Importe 2Âº pago
            valor = null;
            if(ejercicio != null)
            {
                codigoCampo = ConfigurationParameter.getParameter(ConstantesMeLanbide39.CAMPO_SUPL_IMP_PAG_2, ConstantesMeLanbide39.FICHERO_PROPIEDADES);
                valor = this.getValorCampoNumerico(codOrganizacion, numExpediente, String.valueOf(ejercicio), codigoCampo, con);
            }
            if(valor != null)
            {
                calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_IMP_PAG_2, new BigDecimal(MeLanbide39Utils.redondearDecimalesString(valor, 2)));
            }
            BigDecimal pagado = calculos.get(ConstantesMeLanbide39.CAMPO_SUPL_IMP_PAG) != null ? calculos.get(ConstantesMeLanbide39.CAMPO_SUPL_IMP_PAG) : new BigDecimal(ConstantesMeLanbide39.CERO_CON_DECIMALES);
            BigDecimal justificado = calculos.get(ConstantesMeLanbide39.CAMPO_SUPL_IMP_JUS) != null ? calculos.get(ConstantesMeLanbide39.CAMPO_SUPL_IMP_JUS) : new BigDecimal(ConstantesMeLanbide39.CERO_CON_DECIMALES);
            if(concedido == null)
            {
                concedido = new BigDecimal(ConstantesMeLanbide39.CERO_CON_DECIMALES);
            }
            
            if(justificado.compareTo(concedido) > 0)
            {
                justificado = concedido;
            }
            
            BigDecimal pago2 = null;
            
            BigDecimal temp = justificado.subtract(pagado);
            
            if(temp.compareTo(new BigDecimal(ConstantesMeLanbide39.CERO_CON_DECIMALES)) > 0)
            {
                pago2 = new BigDecimal(temp.toPlainString());
            }
            else
            {
                pago2 = new BigDecimal(ConstantesMeLanbide39.CERO_CON_DECIMALES);
            }
            
            calculos.put("PAGADO_REAL_2", new BigDecimal(MeLanbide39Utils.redondearDecimalesString(pago2, 2)));
            
            //importe reintegrar
            BigDecimal reintegro = new BigDecimal(ConstantesMeLanbide39.CERO_CON_DECIMALES);
            BigDecimal impJustif = calculos.get(ConstantesMeLanbide39.CAMPO_SUPL_IMP_JUS);
            if(impJustif == null)
            {
                impJustif = new BigDecimal(ConstantesMeLanbide39.CERO_CON_DECIMALES);
            }
            
            BigDecimal impPagado = calculos.get(ConstantesMeLanbide39.CAMPO_SUPL_IMP_PAG);
            if(impPagado == null)
            {
                impPagado = new BigDecimal(ConstantesMeLanbide39.CERO_CON_DECIMALES);
            }
            
            if(impPagado.doubleValue() > impJustif.doubleValue())
            {
                reintegro = impPagado.subtract(impJustif);
            }
            calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_IMP_REI, new BigDecimal(MeLanbide39Utils.redondearDecimalesString(reintegro, 2)));
            
            
            //TODO: Importe bonificaciones
            //De momento se gestiona como un único campo, pero más adelante posiblemente se separe en 3 conceptos diferentes
            if(ejercicio != null)
            {
                codigoCampo = ConfigurationParameter.getParameter(ConstantesMeLanbide39.CAMPO_SUPL_IMP_BONIF, ConstantesMeLanbide39.FICHERO_PROPIEDADES);
                valor = this.getValorCampoNumerico(codOrganizacion, numExpediente, String.valueOf(ejercicio), codigoCampo, con);
                if(valor != null)
                {
                    calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_IMP_BONIF, new BigDecimal(MeLanbide39Utils.redondearDecimalesString(valor, 2)));
                }
                else
                {
                    calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_IMP_BONIF, new BigDecimal(ConstantesMeLanbide39.CERO_CON_DECIMALES));
                }
            }
            else
            {
                calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_IMP_BONIF, new BigDecimal(ConstantesMeLanbide39.CERO_CON_DECIMALES));
            }
            
            //importe justificado 2
            BigDecimal justificado2 = new BigDecimal(0);
            BigDecimal jus = null;
            try
            {
                String coste="";
                List<FilaResumenVO> filasResumen = getListaResumenPorExpediente(numExpediente, con);
                for(FilaResumenVO fila : filasResumen)
                {
                    coste = fila.getCosteSubvecionable();
                    coste = coste.replace(",", ".");
                    if(!coste.equals("")){
                        jus = new BigDecimal(coste);
                    }
                    justificado2=justificado2.add(jus);
                }
                calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_IMP_JUS2, justificado2);
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
                log.error("Error al actualizar el importe justificado del campo suplementario: " + ex.getMessage());
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error calculando importes para el expediente "+numExpediente, ex);
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
        return calculos;
    }
    
    public Map<String, Integer> cargarCalculosPuestos(int codOrganizacion, Integer ejercicio, String numExpediente, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        Map<String, Integer> calculos = new HashMap<String, Integer>();
        try
        {
            String query =null;
            Integer valor = null;
            
            //Puestos solicitados
            query = "select count(*) as PSOL from CPE_PUESTO where CPE_PTO_NUMEXP = '"+numExpediente+"'";
            
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                valor = rs.getInt("PSOL");
                if(rs.wasNull())
                {
                    valor = 0;
                }
                if(valor != null)
                {
                    calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_PUESTOS_SOLICITADOS, valor);
                }
                else
                {
                    calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_PUESTOS_SOLICITADOS, 0);
                }
            }
            else
            {
                calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_PUESTOS_SOLICITADOS, 0);
            }
            
            //Puestos denegados
            query = "select count(*) as PDEN from CPE_PUESTO where CPE_PTO_NUMEXP = '"+numExpediente+"'"
                    +" and CPE_PTO_COD_RESULT = '"+ConstantesMeLanbide39.CODIGO_RESULTADO_DENEGADO+"'";
            
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                valor = rs.getInt("PDEN");
                if(rs.wasNull())
                {
                    valor = 0;
                }
                if(valor != null)
                {
                    calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_PUESTOS_DENEGADOS, valor);
                }
                else
                {
                    calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_PUESTOS_DENEGADOS, 0);
                }
            }
            else
            {
                calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_PUESTOS_DENEGADOS, 0);
            }
            
            //puestos contratados
            /*query = "select count(distinct(CPE_OFE_COD_PUESTO)) as PCON from CPE_OFERTA where CPE_OFE_NUMEXP = '"+numExpediente+"'"
                    +" and CPE_OFE_NIF is not null";*/
            
            query = "select count(distinct(CPE_OFE_COD_PUESTO)) as PCON"
                   +" from CPE_OFERTA o"
                   +" inner join CPE_PUESTO p on o.CPE_OFE_COD_PUESTO = CPE_PTO_COD_PUESTO and o.CPE_OFE_NUMEXP = p.CPE_PTO_NUMEXP"
                   +" where o.CPE_OFE_NUMEXP = '"+numExpediente+"'"
                   +" and p.CPE_PTO_COD_RESULT <> '"+ConstantesMeLanbide39.CODIGO_RESULTADO_DENEGADO+"'"
                   +" and o.CPE_OFE_NIF is not null";
            
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                valor = rs.getInt("PCON");
                if(rs.wasNull())
                {
                    valor = 0;
                }
                if(valor != null)
                {
                    calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_PUESTOS_CONTRATADOS, valor);
                }
                else
                {
                    calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_PUESTOS_CONTRATADOS, 0);
                }
            }
            else
            {
                calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_PUESTOS_CONTRATADOS, 0);
            }
            
            //personas contratadas
            query = "select count(*) as PERSCON from CPE_OFERTA where CPE_OFE_NUMEXP = '"+numExpediente+"'"
                    +" and CPE_OFE_NIF is not null";
            
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                valor = rs.getInt("PERSCON");
                if(rs.wasNull())
                {
                    valor = 0;
                }
                if(valor != null)
                {
                    calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_PERSONAS_CONTRATADAS, valor);
                }
                else
                {
                    calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_PERSONAS_CONTRATADAS, 0);
                }
            }
            else
            {
                calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_PERSONAS_CONTRATADAS, 0);
            }
            
            //puestos despidos
//            query = "select count(distinct(CPE_OFE_COD_PUESTO)) as PDESP from CPE_OFERTA where CPE_OFE_NUMEXP = '"+numExpediente+"'"
//                    +" and CPE_OFE_FEC_BAJA is not null and CPE_OFE_COD_CAUSA_BAJA = '"+ConstantesMeLanbide39.CODIGO_CAUSA_BAJA_DESPIDO+"'";
//            
//            if(log.isDebugEnabled()) 
//                log.debug("sql = " + query);
//            st = con.createStatement();
//            rs = st.executeQuery(query);
//            if(rs.next())
//            {
//                valor = rs.getInt("PDESP");
//                if(rs.wasNull())
//                {
//                    valor = 0;
//                }
//                if(valor != null)
//                {
//                    calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_PUESTOS_DESPIDO, valor);
//                }
//                else
//                {
//                    calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_PUESTOS_DESPIDO, 0);
//                }
//            }
//            else
//            {
//                calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_PUESTOS_DESPIDO, 0);
//            }
            
            //query = "select * from CPE_OFERTA where CPE_OFE_NUMEXP = '"+numExpediente+"' order by CPE_OFE_COD_PUESTO, CPE_OFE_ID_OFERTA";
            
            query = "select * from CPE_OFERTA o"
                   +" inner join CPE_PUESTO p on o.CPE_OFE_COD_PUESTO = CPE_PTO_COD_PUESTO and o.CPE_OFE_NUMEXP = p.CPE_PTO_NUMEXP"
                   +" where CPE_OFE_NUMEXP = '"+numExpediente+"'"
                   +" and p.CPE_PTO_COD_RESULT <> '"+ConstantesMeLanbide39.CODIGO_RESULTADO_DENEGADO+"'"
                   +" order by CPE_OFE_COD_PUESTO, CPE_OFE_ID_OFERTA";
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            
            List<CpeOfertaVO> ofertasEnEstado = new ArrayList<CpeOfertaVO>();
            List<String> codigosPuesto = new ArrayList<String>();
            List<String> codigosDescartados = new ArrayList<String>();
            while(rs.next())
            {
                ofertasEnEstado.add((CpeOfertaVO)MeLanbide39MappingUtils.getInstance().map(rs, CpeOfertaVO.class));
            }
            if(ofertasEnEstado.size() > 0)
            {
                codigosPuesto = buscarPuestosDeOfertaBaja(ofertasEnEstado, 0, codigosPuesto, codigosDescartados, ConstantesMeLanbide39.CODIGO_CAUSA_BAJA_DESPIDO, con);
            }
            
            if(codigosPuesto != null)
            {
                calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_PUESTOS_DESPIDO, codigosPuesto.size());
            }
            else
            {
                calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_PUESTOS_DESPIDO, 0);
            }
            
            //personas despidos
            query = "select count(*) as PERSDESP from CPE_OFERTA where CPE_OFE_NUMEXP = '"+numExpediente+"'"
                    +" and CPE_OFE_FEC_BAJA is not null and CPE_OFE_COD_CAUSA_BAJA = '"+ConstantesMeLanbide39.CODIGO_CAUSA_BAJA_DESPIDO+"'";
            
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                valor = rs.getInt("PERSDESP");
                if(rs.wasNull())
                {
                    valor = 0;
                }
                if(valor != null)
                {
                    calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_PERSONAS_DESPIDO, valor);
                }
                else
                {
                    calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_PERSONAS_DESPIDO, 0);
                }
            }
            else
            {
                calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_PERSONAS_DESPIDO, 0);
            }
            
            //puestos baja
//            query = "select count(distinct(CPE_OFE_COD_PUESTO)) as PDESP from CPE_OFERTA where CPE_OFE_NUMEXP = '"+numExpediente+"'"
//                    +" and CPE_OFE_FEC_BAJA is not null and CPE_OFE_COD_CAUSA_BAJA = '"+ConstantesMeLanbide39.CODIGO_CAUSA_BAJA_CESE_VOLUNTARIO+"'";
//            
//            if(log.isDebugEnabled()) 
//                log.debug("sql = " + query);
//            st = con.createStatement();
//            rs = st.executeQuery(query);
//            if(rs.next())
//            {
//                valor = rs.getInt("PDESP");
//                if(rs.wasNull())
//                {
//                    valor = 0;
//                }
//                if(valor != null)
//                {
//                    calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_PUESTOS_BAJA, valor);
//                }
//                else
//                {
//                    calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_PUESTOS_BAJA, 0);
//                }
//            }
//            else
//            {
//                calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_PUESTOS_BAJA, 0);
//            }
            
            ofertasEnEstado = new ArrayList<CpeOfertaVO>();
            codigosPuesto = new ArrayList<String>();
            codigosDescartados = new ArrayList<String>();
            
            //query = "select * from CPE_OFERTA where CPE_OFE_NUMEXP = '"+numExpediente+"' order by CPE_OFE_COD_PUESTO, CPE_OFE_ID_OFERTA";
            
            query = "select * from CPE_OFERTA o"
                   +" inner join CPE_PUESTO p on o.CPE_OFE_COD_PUESTO = CPE_PTO_COD_PUESTO and o.CPE_OFE_NUMEXP = p.CPE_PTO_NUMEXP"
                   +" where CPE_OFE_NUMEXP = '"+numExpediente+"'"
                   +" and p.CPE_PTO_COD_RESULT <> '"+ConstantesMeLanbide39.CODIGO_RESULTADO_DENEGADO+"'"
                   +" order by CPE_OFE_COD_PUESTO, CPE_OFE_ID_OFERTA";
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            
            while(rs.next())
            {
                ofertasEnEstado.add((CpeOfertaVO)MeLanbide39MappingUtils.getInstance().map(rs, CpeOfertaVO.class));
            }
            if(ofertasEnEstado.size() > 0)
            {
                codigosPuesto = buscarPuestosDeOfertaBaja(ofertasEnEstado, 0, codigosPuesto, codigosDescartados, ConstantesMeLanbide39.CODIGO_CAUSA_BAJA_CESE_VOLUNTARIO, con);
            }
            
            if(codigosPuesto != null)
            {
                calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_PUESTOS_BAJA, codigosPuesto.size());
            }
            else
            {
                calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_PUESTOS_BAJA, 0);
            }
            
            //personas baja
            query = "select count(*) as PERSBAJA from CPE_OFERTA where CPE_OFE_NUMEXP = '"+numExpediente+"'"
                    +" and CPE_OFE_FEC_BAJA is not null and CPE_OFE_COD_CAUSA_BAJA = '"+ConstantesMeLanbide39.CODIGO_CAUSA_BAJA_CESE_VOLUNTARIO+"'";
            
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                valor = rs.getInt("PERSBAJA");
                if(rs.wasNull())
                {
                    valor = 0;
                }
                if(valor != null)
                {
                    calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_PERSONAS_BAJA, valor);
                }
                else
                {
                    calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_PERSONAS_BAJA, 0);
                }
            }
            else
            {
                calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_PERSONAS_BAJA, 0);
            }
            
            //Puestos renunciados
            /*query = "select count(*) as PREN from CPE_PUESTO where CPE_PTO_NUMEXP = '"+numExpediente+"'"
                    +" and CPE_PTO_COD_RESULT = '"+ConstantesMeLanbide39.CODIGO_RESULTADO_RENUNCIA+"'";*/
            
            //se modifica porque la renuncia puede ser en la solicitud o en la oferta (si no hay oferta o no hay contrato) se tiene que mirar de puesto porque sino nonincluye los solicitados renunciados
            query = "SELECT  NVL(SUM("
                     +" CASE"
                        +" WHEN p.CPE_PTO_COD_RESULT='002' THEN 1"
                        //+" WHEN p.CPE_PTO_COD_RESULT='003' THEN 1"
                        +" WHEN p.CPE_PTO_COD_RESULT='001' and o.CPE_OFE_CONTRATACION='N'  THEN 1 "
                        +" WHEN p.CPE_PTO_COD_RESULT='001' and o.CPE_OFE_CONTRATACION_PRES_OFE='N' THEN 1"
                      +" END), 0) AS PREN"
                    +" FROM CPE_PUESTO p"
                    //+" INNER JOIN CPE_OFERTA o"
                    +" left JOIN CPE_OFERTA o ON p.CPE_PTO_COD_PUESTO = o.CPE_OFE_COD_PUESTO AND p.CPE_PTO_NUMEXP = o.CPE_OFE_NUMEXP and P.CPE_PTO_COD_RESULT='001' "
                    //+" WHERE o.CPE_OFE_NUMEXP = '"+numExpediente+"'"; 
                    +" WHERE p.CPE_PTO_NUMEXP  = '"+numExpediente+"'"; 
                    
            
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                valor = rs.getInt("PREN");
                if(rs.wasNull())
                {
                    valor = 0;
                }
                if(valor != null)
                {
                    calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_PUESTOS_RENUNCIADOS, valor);
                }
                else
                {
                    calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_PUESTOS_RENUNCIADOS, 0);
                }
            }
            else
            {
                calculos.put(ConstantesMeLanbide39.CAMPO_SUPL_PUESTOS_RENUNCIADOS, 0);
            }
            
            //Puestos renunciados tras resolucion
            query = "SELECT  NVL(SUM("
                     +" CASE"
                        +" WHEN p.CPE_PTO_COD_RESULT='001' and o.CPE_OFE_CONTRATACION='N'  THEN 1 "
                        +" WHEN p.CPE_PTO_COD_RESULT='001' and o.CPE_OFE_CONTRATACION_PRES_OFE='N' THEN 1"
                      +" END), 0) AS PRENRES"
                    +" FROM CPE_PUESTO p"
                    +" INNER JOIN CPE_OFERTA o"
                    +" ON p.CPE_PTO_COD_PUESTO = o.CPE_OFE_COD_PUESTO"
                    +" AND p.CPE_PTO_NUMEXP = o.CPE_OFE_NUMEXP"
                    +" WHERE o.CPE_OFE_NUMEXP = '"+numExpediente+"'"; 
            
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                valor = rs.getInt("PRENRES");
                if(rs.wasNull())
                {
                    valor = 0;
                }
                if(valor != null)
                {
                    calculos.put(ConstantesMeLanbide59.CAMPO_SUPL_RES_NUM_PUESTO, valor);
                }
                else
                {
                    calculos.put(ConstantesMeLanbide59.CAMPO_SUPL_RES_NUM_PUESTO, 0);
                }
            }
            else
            {
                calculos.put(ConstantesMeLanbide59.CAMPO_SUPL_RES_NUM_PUESTO, 0);
            }
            
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error calculando puestos para el expediente "+numExpediente, ex);
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
        return calculos;
    }
    
    public CpePuestoVO getPuestoPorCodigoYExpediente(CpePuestoVO p, Connection con) throws Exception
    {
        if(p != null && p.getCodPuesto() != null)
        {
            Statement st = null;
            ResultSet rs = null;
            CpePuestoVO puesto = null;
            try
            {
                String query = "select * from "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_PUESTO, ConstantesMeLanbide39.FICHERO_PROPIEDADES)
                              +" where CPE_PTO_COD_PUESTO = '"+p.getCodPuesto()+"'"
                              +" and CPE_PTO_EXP_EJE = "+(p.getEjercicio() != null ? p.getEjercicio().toString() : "")
                              +" and CPE_PTO_NUMEXP = '"+p.getNumExp()+"'";
                if(log.isDebugEnabled()) 
                    log.debug("sql = " + query);
                st = con.createStatement();
                rs = st.executeQuery(query);
                if(rs.next())
                {
                    puesto = (CpePuestoVO)MeLanbide39MappingUtils.getInstance().map(rs, CpePuestoVO.class);
                }
                return puesto;
            }
            catch(Exception ex)
            {
                log.error("Se ha producido un error recuperando puesto "+(p != null ? p.getCodPuesto() : "(puesto = null)")+" para expediente "+(p != null ? p.getNumExp() : "(puesto = null)"), ex);
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
        else
        {
            return null;
        }
    }
    
    public CpePuestoVO guardarCpePuestoVO(CpePuestoVO puesto, Connection con) throws Exception
    {
        Statement st = null;
        try
        {
            String query = null;
            SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide39.FORMATO_FECHA);
            if(puesto.getCodPuesto() == null)
            {
                String codPuesto = this.getNuevoCodigoPuesto(puesto, con);
                if(codPuesto == null || codPuesto.equals(""))
                {
                    throw new Exception();
                }
                puesto.setCodPuesto(codPuesto);
                //Es un puesto nuevo
                query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_PUESTO, ConstantesMeLanbide39.FICHERO_PROPIEDADES)
                        + "(CPE_PTO_COD_PUESTO, CPE_PTO_EXP_EJE, CPE_PTO_NUMEXP, CPE_PTO_CIUDAD_DESTINO, CPE_PTO_COD_GR_COT, CPE_PTO_COD_IDIOMA_1, CPE_PTO_COD_IDIOMA_2, CPE_PTO_COD_IDIOMA_3,"
                        + " CPE_PTO_COD_MOTIVO,"
                        + " CPE_PTO_COD_NIV_FORM, CPE_PTO_COD_NIV_IDI_1, CPE_PTO_COD_NIV_IDI_2, CPE_PTO_COD_NIV_IDI_3, CPE_PTO_COD_RESULT,"
                        + " CPE_PTO_COD_TIT_1, CPE_PTO_COD_TIT_2,"
                        + " CPE_PTO_COD_TIT_3, CPE_PTO_CONVENIO_COL, CPE_PTO_COSTE_CONT, CPE_PTO_DESC_PUESTO, CPE_PTO_DIETAS_CONV, CPE_PTO_DIETAS_MANUT,"
                        + " CPE_PTO_DPTO, CPE_PTO_FEC_INI,"
                        + " CPE_PTO_FEC_FIN, CPE_PTO_FUNCIONES, CPE_PTO_IMP_MAX_SUBV, CPE_PTO_MESES_MANUT, CPE_PTO_OBSERVACIONES, CPE_PTO_OTROS_BENEF,"
                        + " CPE_PTO_PAI_COD_1,"
                        + " CPE_PTO_PAI_COD_2, CPE_PTO_PAI_COD_3, CPE_PTO_SALARIO_ANEX1, CPE_PTO_SALARIO_ANEX2, CPE_PTO_SALARIO_ANEX3,"
                        + " CPE_PTO_SAL_BRUTO, CPE_PTO_TRAM_SEGUROS,"
                        + " CPE_PTO_SAL_SOLIC, CPE_PTO_DIETAS_SOLIC, CPE_PTO_TRAM_SOLIC, CPE_PTO_IMP_TOT_SOLIC, CPE_PTO_SALARIO_ANEX_TOT, CPE_PTO_MESES_CONTRATO)"
                        + " values('"+puesto.getCodPuesto()+"'"
                        + ", "+(puesto.getEjercicio() != null ? puesto.getEjercicio() : "null")
                        + ", "+(puesto.getNumExp() != null ? "'"+puesto.getNumExp()+"'" : "null")
                        + ", "+(puesto.getCiudadDestino() != null ? "'"+puesto.getCiudadDestino()+"'" : "null")
                        + ", "+(puesto.getCodGrCot() != null ? "'"+puesto.getCodGrCot()+"'" : "null")
                        + ", "+(puesto.getCodIdioma1() != null ? "'"+puesto.getCodIdioma1()+"'" : "null")
                        + ", "+(puesto.getCodIdioma2() != null ? "'"+puesto.getCodIdioma2()+"'" : "null")
                        + ", "+(puesto.getCodIdioma3() != null ? "'"+puesto.getCodIdioma3()+"'" : "null")
                        + ", "+(puesto.getCodMotivo() != null ? "'"+puesto.getCodMotivo()+"'" : "null")
                        + ", "+(puesto.getCodNivForm() != null ? "'"+puesto.getCodNivForm()+"'" : "null")
                        + ", "+(puesto.getCodNivIdi1() != null ? "'"+puesto.getCodNivIdi1()+"'" : "null")
                        + ", "+(puesto.getCodNivIdi2() != null ? "'"+puesto.getCodNivIdi2()+"'" : "null")
                        + ", "+(puesto.getCodNivIdi3() != null ? "'"+puesto.getCodNivIdi3()+"'" : "null")
                        + ", "+(puesto.getCodResult() != null ? "'"+puesto.getCodResult()+"'" : "null")
                        + ", "+(puesto.getCodTit1() != null ? "'"+puesto.getCodTit1()+"'" : "null")
                        + ", "+(puesto.getCodTit2() != null ? "'"+puesto.getCodTit2()+"'" : "null")
                        + ", "+(puesto.getCodTit3() != null ? "'"+puesto.getCodTit3()+"'" : "null")
                        + ", "+(puesto.getConvenioCol() != null ? "'"+puesto.getConvenioCol()+"'" : "null")
                        + ", "+(puesto.getCosteCont() != null ? puesto.getCosteCont().toPlainString() : "null")
                        + ", "+(puesto.getDescPuesto() != null ? "'"+puesto.getDescPuesto()+"'" : "null")
                        + ", "+(puesto.getDietasConv() != null ? puesto.getDietasConv().toPlainString() : "null")
                        + ", "+(puesto.getDietasManut() != null ? puesto.getDietasManut().toPlainString() : "null")
                        + ", "+(puesto.getDpto() != null ? "'"+puesto.getDpto()+"'" : "null")
                        + ", "+(puesto.getFecIni() != null ? "TO_DATE('"+format.format(puesto.getFecIni())+"', 'dd/mm/yyyy')" : "null")
                        + ", "+(puesto.getFecFin() != null ? "TO_DATE('"+format.format(puesto.getFecFin())+"', 'dd/mm/yyyy')" : "null")
                        + ", "+(puesto.getFunciones() != null ? "'"+puesto.getFunciones()+"'" : "null")
                        + ", "+(puesto.getImpMaxSuv() != null ? puesto.getImpMaxSuv().toPlainString() : "null")
                        + ", "+(puesto.getMesesManut() != null ? puesto.getMesesManut().toString() : "null")
                        + ", "+(puesto.getObservaciones() != null ? "'"+puesto.getObservaciones()+"'" : "null")
                        + ", "+(puesto.getOtrosBenef() != null ? "'"+puesto.getOtrosBenef()+"'" : "null")
                        + ", "+(puesto.getPaiCod1() != null ? puesto.getPaiCod1().toString() : "null")
                        + ", "+(puesto.getPaiCod2() != null ? puesto.getPaiCod2().toString() : "null")
                        + ", "+(puesto.getPaiCod3() != null ? puesto.getPaiCod3().toString() : "null")
                        + ", "+(puesto.getSalarioAnex1() != null ? puesto.getSalarioAnex1().toPlainString() : "null")
                        + ", "+(puesto.getSalarioAnex2() != null ? puesto.getSalarioAnex2().toPlainString() : "null")
                        + ", "+(puesto.getSalarioAnex3() != null ? puesto.getSalarioAnex3().toPlainString() : "null")
                        + ", "+(puesto.getSalBruto() != null ? puesto.getSalBruto().toPlainString() : "null")
                        + ", "+(puesto.getTramSeguros() != null ? puesto.getTramSeguros().toPlainString() : "null")
                        + ", "+(puesto.getSalSolic() != null ? puesto.getSalSolic().toPlainString() : "null")
                        + ", "+(puesto.getDietasSolic() != null ? puesto.getDietasSolic().toPlainString() : "null")
                        + ", "+(puesto.getTramSolic() != null ? puesto.getTramSolic().toPlainString() : "null")
                        + ", "+(puesto.getImpTotSolic() != null ? puesto.getImpTotSolic().toPlainString() : "null")
                        + ", "+(puesto.getSalarioAnexTot() != null ? puesto.getSalarioAnexTot().toPlainString() : "null")
                        + ", "+(puesto.getMesesContrato() != null ? puesto.getMesesContrato().toString() : "null")
                        + ")";
            }
            else
            {
                //Es un puesto que ya existe
                query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_PUESTO, ConstantesMeLanbide39.FICHERO_PROPIEDADES)
                        + " set"
                        + " CPE_PTO_CIUDAD_DESTINO = "+(puesto.getCiudadDestino() != null ? "'"+puesto.getCiudadDestino()+"'" : "null")+", "
                        + " CPE_PTO_COD_GR_COT = "+(puesto.getCodGrCot() != null ? "'"+puesto.getCodGrCot()+"'" : "null")+", "
                        + " CPE_PTO_COD_IDIOMA_1 = "+(puesto.getCodIdioma1() != null ? "'"+puesto.getCodIdioma1()+"'" : "null")+", "
                        + " CPE_PTO_COD_IDIOMA_2 = "+(puesto.getCodIdioma2() != null ? "'"+puesto.getCodIdioma2()+"'" : "null")+", "
                        + " CPE_PTO_COD_IDIOMA_3 = "+(puesto.getCodIdioma3() != null ? "'"+puesto.getCodIdioma3()+"'" : "null")+", "
                        + " CPE_PTO_COD_MOTIVO = "+(puesto.getCodMotivo() != null ? "'"+puesto.getCodMotivo()+"'" : "null")+", "
                        + " CPE_PTO_COD_NIV_FORM = "+(puesto.getCodNivForm() != null ? "'"+puesto.getCodNivForm()+"'" : "null")+", "
                        + " CPE_PTO_COD_NIV_IDI_1 = "+(puesto.getCodNivIdi1() != null ? "'"+puesto.getCodNivIdi1()+"'" : "null")+", "
                        + " CPE_PTO_COD_NIV_IDI_2 = "+(puesto.getCodNivIdi2() != null ? "'"+puesto.getCodNivIdi2()+"'" : "null")+", "
                        + " CPE_PTO_COD_NIV_IDI_3 = "+(puesto.getCodNivIdi3() != null ? "'"+puesto.getCodNivIdi3()+"'" : "null")+", "
                        + " CPE_PTO_COD_RESULT = "+(puesto.getCodResult() != null ? "'"+puesto.getCodResult()+"'" : "null")+", "
                        + " CPE_PTO_COD_TIT_1 = "+(puesto.getCodTit1() != null ? "'"+puesto.getCodTit1()+"'" : "null")+", "
                        + " CPE_PTO_COD_TIT_2 = "+(puesto.getCodTit2() != null ? "'"+puesto.getCodTit2()+"'" : "null")+", "
                        + " CPE_PTO_COD_TIT_3 = "+(puesto.getCodTit3() != null ? "'"+puesto.getCodTit3()+"'" : "null")+", "
                        + " CPE_PTO_CONVENIO_COL = "+(puesto.getConvenioCol() != null ? "'"+puesto.getConvenioCol()+"'" : "null")+", "
                        + " CPE_PTO_COSTE_CONT = "+(puesto.getCosteCont() != null ? puesto.getCosteCont().toPlainString() : "null")+", "
                        + " CPE_PTO_DESC_PUESTO = "+(puesto.getDescPuesto() != null ? "'"+puesto.getDescPuesto()+"'" : "null")+", "
                        + " CPE_PTO_DIETAS_CONV = "+(puesto.getDietasConv() != null ? puesto.getDietasConv().toPlainString() : "null")+", "
                        + " CPE_PTO_DIETAS_MANUT = "+(puesto.getDietasManut() != null ? puesto.getDietasManut().toPlainString() : "null")+", "
                        + " CPE_PTO_DPTO = "+(puesto.getDpto() != null ? "'"+puesto.getDpto()+"'" : "null")+", "
                        + " CPE_PTO_FEC_INI = "+(puesto.getFecIni() != null ? "TO_DATE('"+format.format(puesto.getFecIni())+"', 'dd/mm/yyyy')" : "null")+", "
                        + " CPE_PTO_FEC_FIN = "+(puesto.getFecFin() != null ? "TO_DATE('"+format.format(puesto.getFecFin())+"', 'dd/mm/yyyy')" : "null")+", "
                        + " CPE_PTO_FUNCIONES = "+(puesto.getFunciones() != null ? "'"+puesto.getFunciones()+"'" : "null")+", "
                        + " CPE_PTO_IMP_MAX_SUBV = "+(puesto.getImpMaxSuv() != null ? puesto.getImpMaxSuv().toPlainString() : "null")+", "
                        + " CPE_PTO_MESES_MANUT = "+(puesto.getMesesManut() != null ? puesto.getMesesManut().toString() : "null")+", "
                        + " CPE_PTO_OBSERVACIONES = "+(puesto.getObservaciones() != null ? "'"+puesto.getObservaciones()+"'" : "null")+", "
                        + " CPE_PTO_OTROS_BENEF = "+(puesto.getOtrosBenef() != null ? "'"+puesto.getOtrosBenef()+"'" : "null")+", "
                        + " CPE_PTO_PAI_COD_1 = "+(puesto.getPaiCod1() != null ? puesto.getPaiCod1().toString() : "null")+", "
                        + " CPE_PTO_PAI_COD_2 = "+(puesto.getPaiCod2() != null ? puesto.getPaiCod2().toString() : "null")+", "
                        + " CPE_PTO_PAI_COD_3 = "+(puesto.getPaiCod3() != null ? puesto.getPaiCod3().toString() : "null")+", "
                        + " CPE_PTO_SALARIO_ANEX1 = "+(puesto.getSalarioAnex1() != null ? puesto.getSalarioAnex1().toPlainString() : "null")+", "
                        + " CPE_PTO_SALARIO_ANEX2 = "+(puesto.getSalarioAnex2() != null ? puesto.getSalarioAnex2().toPlainString() : "null")+", "
                        + " CPE_PTO_SALARIO_ANEX3 = "+(puesto.getSalarioAnex3() != null ? puesto.getSalarioAnex3().toPlainString() : "null")+", "
                        + " CPE_PTO_SAL_BRUTO = "+(puesto.getSalBruto() != null ? puesto.getSalBruto().toPlainString() : "null")+", "
                        + " CPE_PTO_TRAM_SEGUROS = "+(puesto.getTramSeguros() != null ? puesto.getTramSeguros().toPlainString() : "null")+", "
                        + " CPE_PTO_SAL_SOLIC = "+(puesto.getSalSolic() != null ? puesto.getSalSolic().toPlainString() : "null")+", "
                        + " CPE_PTO_DIETAS_SOLIC = "+(puesto.getDietasSolic() != null ? puesto.getDietasSolic().toPlainString() : "null")+", "
                        + " CPE_PTO_TRAM_SOLIC = "+(puesto.getTramSolic() != null ? puesto.getTramSolic().toPlainString() : "null")+", "
                        + " CPE_PTO_IMP_TOT_SOLIC = "+(puesto.getImpTotSolic() != null ? puesto.getImpTotSolic().toPlainString() : "null")+", "
                        + " CPE_PTO_SALARIO_ANEX_TOT = "+(puesto.getSalarioAnexTot() != null ? puesto.getSalarioAnexTot().toPlainString() : "null")+", "
                        + " CPE_PTO_MESES_CONTRATO = "+(puesto.getMesesContrato() != null ? puesto.getMesesContrato().toString() : "null")
                        + " where CPE_PTO_COD_PUESTO = '"+puesto.getCodPuesto()+"'"
                        + " and CPE_PTO_EXP_EJE = "+puesto.getEjercicio().toString()
                        + " and CPE_PTO_NUMEXP = '"+puesto.getNumExp()+"'";
            }
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            int res = st.executeUpdate(query);
            if(res > 0)
            {
                return puesto;
            }
            else
            {
                return null;
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error guardando los datos del puestp "+(puesto != null ? puesto.getCodPuesto() : "(puesto = null)")+" para el expediente "+(puesto != null ? puesto.getNumExp() : "(puesto = null)"), ex);
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
    
    private String getNuevoCodigoPuesto(CpePuestoVO puesto, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        String nuevoCodigo = null;
        try
        {
            String query = null;
                query = "select nvl(max (CPE_PTO_COD_PUESTO), 0) as COD_ANT from "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_PUESTO, ConstantesMeLanbide39.FICHERO_PROPIEDADES)
                      + " where CPE_PTO_EXP_EJE = "+puesto.getEjercicio()+" and CPE_PTO_NUMEXP = '"+puesto.getNumExp()+"'";
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                String codAnt = rs.getString("COD_ANT");
                if(codAnt != null)
                {
                    //convierto el anterior a int para poder sumarle 1
                    int ant = Integer.parseInt(codAnt);
                    ant++;
                    //Completo el codigo nuevo (despues de sumar 1) con ceros por la izquierda hasta 4
                    codAnt = ""+ant;
                    int cerosFaltan = 4 - codAnt.length();
                    while(cerosFaltan != 0)
                    {
                        codAnt = "0"+codAnt;
                        cerosFaltan--;
                    }
                    nuevoCodigo = codAnt;
                }
                else
                {
                    nuevoCodigo = "0001";
                }
            }
            else
            {
                nuevoCodigo = "0001";
            }
            return nuevoCodigo;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error calculando el siguiente cÃ³digo de puesto para el expediente "+(puesto != null ? puesto.getNumExp() : "(puesto = null)"), ex);
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
                e.printStackTrace();
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    
    private Integer getNuevoCodigoOferta(CpeOfertaVO oferta, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        int nuevoCodigo;
        try
        {
            String query = null;
                query = "select nvl(max (CPE_OFE_ID_OFERTA), 0) as COD_ANT from "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_OFERTA, ConstantesMeLanbide39.FICHERO_PROPIEDADES)
                      + " where CPE_OFE_EXP_EJE = "+oferta.getExpEje()+" and CPE_OFE_NUMEXP = '"+oferta.getNumExp()+"' and CPE_OFE_COD_PUESTO = '"+oferta.getCodPuesto()+"'";
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                int codAnt = rs.getInt("COD_ANT");
                if(rs.wasNull())
                {
                    nuevoCodigo = 0;
                }
                nuevoCodigo = codAnt+1;
            }
            else
            {
                nuevoCodigo = 1;
            }
            return nuevoCodigo;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error calculando el siguiente cÃ³digo de oferta para el expediente "+(oferta != null ? oferta.getNumExp() : "(puesto = null)"), ex);
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
                e.printStackTrace();
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    
    private Integer getNuevoCodigoJustificacion(CpeJustificacionVO justif, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        int nuevoCodigo;
        try
        {
            String query = null;
                query = "select nvl(max (CPE_JUS_ID_JUSTIFICACION), 0) as COD_ANT from "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_JUSTIFICACION, ConstantesMeLanbide39.FICHERO_PROPIEDADES)
                      + " where CPE_JUS_EXP_EJE = "+justif.getEjercicio()+" and CPE_JUS_NUMEXP = '"+justif.getNumExpediente()+"' and CPE_JUS_COD_PUESTO = '"+justif.getCodPuesto()+"'";
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                int codAnt = rs.getInt("COD_ANT");
                if(rs.wasNull())
                {
                    nuevoCodigo = 0;
                }
                nuevoCodigo = codAnt+1;
            }
            else
            {
                nuevoCodigo = 1;
            }
            return nuevoCodigo;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error calculando el siguiente cÃ³digo de justificacion para el expediente "+(justif != null ? justif.getNumExpediente() : "(justif = null)"), ex);
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
                e.printStackTrace();
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    
    public List<FilaPuestoVO> getListaPuestosPorExpediente(String numExpediente, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        List<FilaPuestoVO> puestos = new ArrayList<FilaPuestoVO>();
        try
        {
            String query = null;
                query = "select"
                        +" CPE_PTO_COD_PUESTO, CPE_PTO_DESC_PUESTO,"
                        +" PAI_NOM AS PAIS,"
                        +" nivelFor.DES_NOM as NIVEL_FORMATIVO,"
                        +" tit1.GEN_TIT_DESC_TITULAC_C as TITULACION1,"
                        +" tit2.GEN_TIT_DESC_TITULAC_C as TITULACION2,"
                        +" tit3.GEN_TIT_DESC_TITULAC_C as TITULACION3,"
                        +" CPE_PTO_IMP_TOT_SOLIC as SUBV_SOLIC, CPE_PTO_IMP_MAX_SUBV as SUBV_APROB,"
                        +" res.DES_NOM as RESULTADO,"
                        +" mot.DES_NOM as MOTIVO,"
                        +" CPE_PTO_OBSERVACIONES"
                        +" from "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_PUESTO, ConstantesMeLanbide39.FICHERO_PROPIEDADES)+" puesto"
                        +" left join "+GlobalNames.ESQUEMA_GENERICO + ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_PAISES, ConstantesMeLanbide39.FICHERO_PROPIEDADES)+" pais on puesto.CPE_PTO_PAI_COD_1 = pais.PAI_COD"
                        +" left join (select * from "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_E_DES_VAL, ConstantesMeLanbide39.FICHERO_PROPIEDADES)+" where DES_COD = 'NFOR') nivelFor on puesto.CPE_PTO_COD_NIV_FORM = nivelFor.DES_VAL_COD"
                        +" left join "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_GEN_TITULACIONES, ConstantesMeLanbide39.FICHERO_PROPIEDADES)+" tit1 on puesto.CPE_PTO_COD_TIT_1 = tit1.GEN_TIT_COD_TITULAC"
                        +" left join "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_GEN_TITULACIONES, ConstantesMeLanbide39.FICHERO_PROPIEDADES)+" tit2 on puesto.CPE_PTO_COD_TIT_2 = tit2.GEN_TIT_COD_TITULAC"
                        +" left join "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_GEN_TITULACIONES, ConstantesMeLanbide39.FICHERO_PROPIEDADES)+" tit3 on puesto.CPE_PTO_COD_TIT_3 = tit3.GEN_TIT_COD_TITULAC"
                        +" left join (select * from "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_E_DES_VAL, ConstantesMeLanbide39.FICHERO_PROPIEDADES)+" where DES_COD = 'RES') res on puesto.CPE_PTO_COD_RESULT = res.DES_VAL_COD"
                        +" left join (select * from "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_E_DES_VAL, ConstantesMeLanbide39.FICHERO_PROPIEDADES)+" where DES_COD = 'MOT') mot on puesto.CPE_PTO_COD_MOTIVO = mot.DES_VAL_COD"
                        +" where CPE_PTO_NUMEXP = '"+numExpediente+"'"
                        +" order by CPE_PTO_COD_PUESTO";
            
            
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while(rs.next())
            {
                puestos.add((FilaPuestoVO)MeLanbide39MappingUtils.getInstance().map(rs, FilaPuestoVO.class));
            }
            return puestos;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando puestos para el expediente "+numExpediente, ex);
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
    
    public List<FilaPuestoVO> getListaPuestosHistPorExpediente(int codOrganizacion, String numExpediente, String codTramite, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        List<FilaPuestoVO> puestos = new ArrayList<FilaPuestoVO>();
        try
        {
            String query = null;
                
                query = "select"
                        +" CPE_PTO_COD_PUESTO, CPE_PTO_DESC_PUESTO,"
                        +" PAI_NOM AS PAIS,"
                        +" nivelFor.DES_NOM as NIVEL_FORMATIVO,"
                        +" tit1.GEN_TIT_DESC_TITULAC_C as TITULACION1,"
                        +" tit2.GEN_TIT_DESC_TITULAC_C as TITULACION2,"
                        +" tit3.GEN_TIT_DESC_TITULAC_C as TITULACION3,"
                        +" CPE_PTO_IMP_TOT_SOLIC as SUBV_SOLIC, CPE_PTO_IMP_MAX_SUBV as SUBV_APROB,"
                        +" res.DES_NOM as RESULTADO,"
                        +" mot.DES_NOM as MOTIVO,"
                        +" CPE_PTO_OBSERVACIONES"
                        +" from "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_HIST_PUESTO, ConstantesMeLanbide39.FICHERO_PROPIEDADES)+" puesto"
                        +" inner join E_TRA ON TRA_PRO = 'CPE' AND TRA_COD = CPE_PTO_COD_TRAMITE AND TRA_MUN = "+ codOrganizacion + " AND TRA_COU = " + codTramite
                       +" left join "+GlobalNames.ESQUEMA_GENERICO + ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_PAISES, ConstantesMeLanbide39.FICHERO_PROPIEDADES)+" pais on puesto.CPE_PTO_PAI_COD_1 = pais.PAI_COD"
                        +" left join (select * from "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_E_DES_VAL, ConstantesMeLanbide39.FICHERO_PROPIEDADES)+" where DES_COD = 'NFOR') nivelFor on puesto.CPE_PTO_COD_NIV_FORM = nivelFor.DES_VAL_COD"
                        +" left join "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_GEN_TITULACIONES, ConstantesMeLanbide39.FICHERO_PROPIEDADES)+" tit1 on puesto.CPE_PTO_COD_TIT_1 = tit1.GEN_TIT_COD_TITULAC"
                        +" left join "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_GEN_TITULACIONES, ConstantesMeLanbide39.FICHERO_PROPIEDADES)+" tit2 on puesto.CPE_PTO_COD_TIT_2 = tit2.GEN_TIT_COD_TITULAC"
                        +" left join "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_GEN_TITULACIONES, ConstantesMeLanbide39.FICHERO_PROPIEDADES)+" tit3 on puesto.CPE_PTO_COD_TIT_3 = tit3.GEN_TIT_COD_TITULAC"
                        +" left join (select * from "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_E_DES_VAL, ConstantesMeLanbide39.FICHERO_PROPIEDADES)+" where DES_COD = 'RES') res on puesto.CPE_PTO_COD_RESULT = res.DES_VAL_COD"
                        +" left join (select * from "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_E_DES_VAL, ConstantesMeLanbide39.FICHERO_PROPIEDADES)+" where DES_COD = 'MOT') mot on puesto.CPE_PTO_COD_MOTIVO = mot.DES_VAL_COD"
                        +" where CPE_PTO_NUMEXP = '"+numExpediente+"'"// AND CPE_PTO_COD_TRAMITE = " + codTramite
                        +" order by CPE_PTO_COD_PUESTO";
            
            
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while(rs.next())
            {
                puestos.add((FilaPuestoVO)MeLanbide39MappingUtils.getInstance().map(rs, FilaPuestoVO.class));
            }
            return puestos;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando puestos para el expediente "+numExpediente, ex);
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
    
    public int eliminarCpePuesto(CpePuestoVO p, Connection con) throws Exception
    {
        Statement st = null;
        try
        {
            String query = null;
                query = "delete from "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_PUESTO, ConstantesMeLanbide39.FICHERO_PROPIEDADES)
                       +" where CPE_PTO_COD_PUESTO = '"+p.getCodPuesto()+"'"
                       +" and CPE_PTO_EXP_EJE = "+p.getEjercicio()
                       +" and CPE_PTO_NUMEXP = '"+p.getNumExp()+"'";
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            return st.executeUpdate(query);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error eliminando puesto  "+(p != null ? p.getCodPuesto() : "puesto = null"), ex);
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
    
    public SalarioVO getSalarioAnexo1PorPaisGrupoCot(Integer codPais, String codGrCot, Connection con) throws Exception
    {
        if(codGrCot != null && !codGrCot.equals("") && codPais != null)
        {
            Statement st = null;
            ResultSet rs = null;
            try
            {
                String query = null;
                    query = "select * from "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_SALARIOS, ConstantesMeLanbide39.FICHERO_PROPIEDADES)
                           +" where CPE_SAL_PAI_COD = "+codPais.toString()
                           +" and CPE_SAL_COD_GR_COT = '"+codGrCot+"'";
                if(log.isDebugEnabled()) 
                    log.debug("sql = " + query);
                st = con.createStatement();
                rs = st.executeQuery(query);
                SalarioVO salario = null;
                if(rs.next())
                {
                    salario = (SalarioVO)MeLanbide39MappingUtils.getInstance().map(rs, SalarioVO.class);
                }
                return salario;
            }
            catch(Exception ex)
            {
                log.error("Se ha producido un error recuperando salario para pais = "+codPais+" grupoCot = "+codGrCot, ex);
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
        else
        {
            return null;
        }
    }
    
    public List<FilaOfertaVO> getListaOfertasNoDenegadasPorExpediente(String numExpediente, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        List<FilaOfertaVO> ofertas = new ArrayList<FilaOfertaVO>();
        try
        {
            String query = null;
                
                query = "select tmp.ID_OFERTA,tmp.COD_PUESTO,tmp.DESC_PUESTO, tmp.COD_OFERTA, tmp.DESC_OFERTA, tmp.NOMBRE, tmp.APE1, tmp.APE2, tmp.NIF, "
                        +" tmp.FEC_INI,tmp.FEC_FIN, tmp.FEC_BAJA,COD_CBAJA,"
                        +" tmp.CPE_OFE_CONTRATACION,tmp.CPE_OFE_CONTRATACION_PRES_OFE, tmp.COD_CRENUNCIA,tmp.ID_OFE_ORIGEN, "
                        +" causab.DES_NOM as CAUSA_BAJA,"
                       // +" causaren.DES_NOM as CAUSA_RENUNCIA,"
                        +" case when CPE_OFE_CONTRATACION_PRES_OFE='N' then  causarenNoOfe.DES_NOM  else causaren.DES_NOM   end AS CAUSA_RENUNCIA,"
                        +" oferta2.COD_OFERTA_2"
                        +" from"
                        +" (select"
                        +" CPE_OFE_ID_OFERTA as ID_OFERTA,"
                        +" CPE_OFE_COD_PUESTO as COD_PUESTO,"
                        +" CPE_OFE_DESC_PUESTO as DESC_PUESTO,"
                        +" CPE_OFE_COD_OFERTA as COD_OFERTA,"
                        +" CPE_OFE_COD_OFERTA as DESC_OFERTA,"
                        +" CPE_OFE_NOMBRE as NOMBRE,"
                        +" CPE_OFE_APE1 as APE1,"
                        +" CPE_OFE_APE2 as APE2,"
                        +" CPE_OFE_NIF as NIF,"
                        +" CPE_OFE_FEC_INI as FEC_INI,"
                        +" CPE_OFE_FEC_FIN as FEC_FIN,"
                        +" CPE_OFE_FEC_BAJA as FEC_BAJA,"
                        +" CPE_OFE_COD_CAUSA_BAJA as COD_CBAJA,"
                        +" CPE_OFE_CONTRATACION,"
                        +" CPE_OFE_CONTRATACION_PRES_OFE,"
                        +" CPE_OFE_COD_CAUSA_RENUNCIA as COD_CRENUNCIA,"
                        +" CPE_OFE_COD_CAUSA_REN_PRE_OF, "
                        +" CPE_OFE_ID_OFERTA_ORIGEN as ID_OFE_ORIGEN"
                        +" from "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_OFERTA, ConstantesMeLanbide39.FICHERO_PROPIEDADES) +" oferta"
                        +" inner join "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_PUESTO, ConstantesMeLanbide39.FICHERO_PROPIEDADES) +" puesto on oferta.CPE_OFE_COD_PUESTO = puesto.CPE_PTO_COD_PUESTO and puesto.CPE_PTO_NUMEXP = oferta.CPE_OFE_NUMEXP"
                        +" where puesto.CPE_PTO_NUMEXP = '"+numExpediente+"'"
                        +" and puesto.CPE_PTO_COD_RESULT <> '"+ConstantesMeLanbide39.CODIGO_RESULTADO_DENEGADO+"'"
                        +" and puesto.CPE_PTO_COD_RESULT <> '"+ConstantesMeLanbide39.CODIGO_RESULTADO_RENUNCIA+"')tmp"
                        +" left join (select * from "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_E_DES_VAL, ConstantesMeLanbide39.FICHERO_PROPIEDADES) +" where DES_COD = '"+ConfigurationParameter.getParameter(ConstantesMeLanbide39.CAMPO_SUPL_CAUSA_BAJA, ConstantesMeLanbide39.FICHERO_PROPIEDADES) +"') causab on tmp.COD_CBAJA = causab.DES_VAL_COD"
                        +" left join (select * from "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_E_DES_VAL, ConstantesMeLanbide39.FICHERO_PROPIEDADES) +" where DES_COD = '"+ConfigurationParameter.getParameter(ConstantesMeLanbide39.CAMPO_SUPL_CAUSA_RENUNCIA, ConstantesMeLanbide39.FICHERO_PROPIEDADES) +"') causaren on tmp.COD_CRENUNCIA = causaren.DES_VAL_COD"
                        +" LEFT JOIN (SELECT * FROM "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_E_DES_VAL, ConstantesMeLanbide39.FICHERO_PROPIEDADES) +" WHERE DES_COD = '"+ConfigurationParameter.getParameter(ConstantesMeLanbide39.CAMPO_SUPL_CAUSA_RENUNCIA, ConstantesMeLanbide39.FICHERO_PROPIEDADES) +"') causarenNoOfe ON  tmp.CPE_OFE_COD_CAUSA_REN_PRE_OF = causarenNoOfe.DES_VAL_COD"
                        +" left join (select CPE_OFE_ID_OFERTA as ID_OFERTA_2, CPE_OFE_COD_PUESTO as COD_PUESTO_2, CPE_OFE_COD_OFERTA as COD_OFERTA_2 from "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_OFERTA, ConstantesMeLanbide39.FICHERO_PROPIEDADES) +" where CPE_OFE_NUMEXP = '"+numExpediente+"') oferta2 on tmp.ID_OFE_ORIGEN = oferta2.ID_OFERTA_2 and tmp.COD_PUESTO = oferta2.COD_PUESTO_2"
                        +" order by tmp.COD_PUESTO, tmp.ID_OFERTA, tmp.FEC_BAJA";
            
            
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while(rs.next())
            {
                ofertas.add((FilaOfertaVO)MeLanbide39MappingUtils.getInstance().map(rs, FilaOfertaVO.class));
            }
            return ofertas;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando ofertas para el expediente "+numExpediente, ex);
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
    
    public List<FilaOfertaVO> getListaOfertasHistNoDenegadasPorExpediente(int codOrganizacion, String numExpediente, String codTramite, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        List<FilaOfertaVO> ofertas = new ArrayList<FilaOfertaVO>();
        try
        {
            String query = null;
                
                query = "select tmp.*,"
                        +" causab.DES_NOM as CAUSA_BAJA,"
                        +" causaren.DES_NOM as CAUSA_RENUNCIA,"
                        +" oferta2.COD_OFERTA_2"
                        +" from"
                        +" (select"
                        +" CPE_OFE_ID_OFERTA as ID_OFERTA,"
                        +" CPE_OFE_COD_PUESTO as COD_PUESTO,"
                        +" CPE_OFE_DESC_PUESTO as DESC_PUESTO,"
                        +" CPE_OFE_COD_OFERTA as COD_OFERTA,"
                        +" CPE_OFE_COD_OFERTA as DESC_OFERTA,"
                        +" CPE_OFE_NOMBRE as NOMBRE,"
                        +" CPE_OFE_APE1 as APE1,"
                        +" CPE_OFE_APE2 as APE2,"
                        +" CPE_OFE_NIF as NIF,"
                        +" CPE_OFE_FEC_INI as FEC_INI,"
                        +" CPE_OFE_FEC_FIN as FEC_FIN,"
                        +" CPE_OFE_FEC_BAJA as FEC_BAJA,"
                        +" CPE_OFE_COD_CAUSA_BAJA as COD_CBAJA,"
                        +" CPE_OFE_CONTRATACION,"
                        +" CPE_OFE_COD_CAUSA_RENUNCIA as COD_CRENUNCIA,"
                        +" CPE_OFE_CONTRATACION_PRES_OFE,"
                        +" CPE_OFE_ID_OFERTA_ORIGEN as ID_OFE_ORIGEN"
                        +" from "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_HIST_OFERTA, ConstantesMeLanbide39.FICHERO_PROPIEDADES) +" oferta"
                        +" inner join E_TRA ON TRA_PRO = 'CPE' AND TRA_COD = CPE_OFE_COD_TRAMITE AND TRA_MUN = "+ codOrganizacion + " AND TRA_COU = " + codTramite
                        +" inner join "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_HIST_PUESTO, ConstantesMeLanbide39.FICHERO_PROPIEDADES) +" puesto on oferta.CPE_OFE_COD_PUESTO = puesto.CPE_PTO_COD_PUESTO and puesto.CPE_PTO_NUMEXP = oferta.CPE_OFE_NUMEXP  AND CPE_OFE_COD_TRAMITE = CPE_PTO_COD_TRAMITE"
                        +" where puesto.CPE_PTO_NUMEXP = '"+numExpediente+"'"// AND CPE_OFE_COD_TRAMITE = " + codTramite
                        +" and puesto.CPE_PTO_COD_RESULT <> '"+ConstantesMeLanbide39.CODIGO_RESULTADO_DENEGADO+"'"
                        +" and puesto.CPE_PTO_COD_RESULT <> '"+ConstantesMeLanbide39.CODIGO_RESULTADO_RENUNCIA+"')tmp"
                        +" left join (select * from "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_E_DES_VAL, ConstantesMeLanbide39.FICHERO_PROPIEDADES) +" where DES_COD = '"+ConfigurationParameter.getParameter(ConstantesMeLanbide39.CAMPO_SUPL_CAUSA_BAJA, ConstantesMeLanbide39.FICHERO_PROPIEDADES) +"') causab on tmp.COD_CBAJA = causab.DES_VAL_COD"
                        +" left join (select * from "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_E_DES_VAL, ConstantesMeLanbide39.FICHERO_PROPIEDADES) +" where DES_COD = '"+ConfigurationParameter.getParameter(ConstantesMeLanbide39.CAMPO_SUPL_CAUSA_RENUNCIA, ConstantesMeLanbide39.FICHERO_PROPIEDADES) +"') causaren on tmp.COD_CRENUNCIA = causaren.DES_VAL_COD"
                        +" left join (select CPE_OFE_ID_OFERTA as ID_OFERTA_2, CPE_OFE_COD_PUESTO as COD_PUESTO_2, CPE_OFE_COD_OFERTA as COD_OFERTA_2 from "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_OFERTA, ConstantesMeLanbide39.FICHERO_PROPIEDADES) +" where CPE_OFE_NUMEXP = '"+numExpediente+"') oferta2 on tmp.ID_OFE_ORIGEN = oferta2.ID_OFERTA_2 and tmp.COD_PUESTO = oferta2.COD_PUESTO_2"
                        +" order by tmp.COD_PUESTO, tmp.ID_OFERTA, tmp.FEC_BAJA";
            
            
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while(rs.next())
            {
                ofertas.add((FilaOfertaVO)MeLanbide39MappingUtils.getInstance().map(rs, FilaOfertaVO.class));
            }
            return ofertas;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando ofertas para el expediente "+numExpediente, ex);
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
    
    public CpeOfertaVO getOfertaPorCodigoPuestoYExpediente(CpeOfertaVO p, Connection con) throws Exception
    {
        if(p != null && p.getIdOferta() != null)
        {
            Statement st = null;
            ResultSet rs = null;
            CpeOfertaVO oferta = null;
            try
            {
                String query = "select * from "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_OFERTA, ConstantesMeLanbide39.FICHERO_PROPIEDADES)
                              +" where CPE_OFE_ID_OFERTA = '"+p.getIdOferta()+"'"
                              +" and CPE_OFE_COD_PUESTO = '"+(p.getCodPuesto() != null ? p.getCodPuesto() : "")+"'"
                              +" and CPE_OFE_EXP_EJE = "+(p.getExpEje() != null ? p.getExpEje().toString() : "")
                              +" and CPE_OFE_NUMEXP = '"+p.getNumExp()+"'";
                if(log.isDebugEnabled()) 
                    log.debug("sql = " + query);
                st = con.createStatement();
                rs = st.executeQuery(query);
                if(rs.next())
                {
                    oferta = (CpeOfertaVO)MeLanbide39MappingUtils.getInstance().map(rs, CpeOfertaVO.class);
                }
                return oferta;
            }
            catch(Exception ex)
            {
                log.error("Se ha producido un error recuperando oferta "+(p != null ? p.getIdOferta() : "(oferta = null)")+" para puesto "+(p != null ? p.getCodPuesto() : "(oferta = null)")+" expediente "+(p != null ? p.getNumExp() : "(oferta = null)"), ex);
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
        else
        {
            return null;
        }
    }
    
//    public CpeOfertaVO guardarCpeOfertaVO(CpeOfertaVO oferta, Connection con) throws Exception
//    {
//        if(oferta == null)
//        {
//            return null;
//        }
//        else
//        {
//            Statement st = null;
//            try
//            {
//                String query = null;
//                SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide39.FORMATO_FECHA);
//                if(oferta.getIdOferta() == null)
//                {
//                    
//                    if(oferta.getCodPuesto() == null || oferta.getCodPuesto().equals(""))
//                    {
//                        throw new Exception();
//                    }
//                    Integer idOferta = this.getNextId(ConstantesMeLanbide39.SEQ_CPE_OFERTA, con);
//                    oferta.setIdOferta(idOferta);
//                    //Es un puesto nuevo
//                    query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_OFERTA, ConstantesMeLanbide39.FICHERO_PROPIEDADES)
//                            + "(CPE_OFE_ID_OFERTA, CPE_OFE_COD_PUESTO, CPE_OFE_EXP_EJE, CPE_OFE_NUMEXP, CPE_OFE_ANO_TITULACION, CPE_OFE_APE1, CPE_OFE_APE2,"
//                            + " CPE_OFE_COD_CAUSA_BAJA,"
//                            + " CPE_OFE_COD_GR_COT, CPE_OFE_COD_OFERTA, CPE_OFE_COD_OFI_GEST, CPE_OFE_COD_TIPO_NIF,"
//                            + " CPE_OFE_COD_TITULACION, CPE_OFE_CONTRATACION,"
//                            + " CPE_OFE_DIETAS_CONVENIO, CPE_OFE_DIETAS_CONVOC, CPE_OFE_DIFUSION, CPE_OFE_DOC_CONTRATACION, CPE_OFE_DOC_GEST_OFE, CPE_OFE_EMAIL,"
//                            + " CPE_OFE_FEC_BAJA,"
//                            + " CPE_OFE_FEC_DIFUSION, CPE_OFE_FEC_ENV_CAND, CPE_OFE_FEC_FIN, CPE_OFE_FEC_INI, CPE_OFE_FEC_NAC, CPE_OFE_FL_SUSTITUTO,"
//                            + " CPE_OFE_MOT_CONTRATACION,"
//                            + " CPE_OFE_NIF, CPE_OFE_NOMBRE, CPE_OFE_NUM_TOT_CAND, CPE_OFE_OBSERVACIONES, CPE_OFE_PREC, CPE_OFE_PREC_NOM,"
//                            + " CPE_OFE_SALARIOB, CPE_OFE_SEXO, CPE_OFE_TLF)"
//                            + " values("+oferta.getIdOferta().toString()+""
//                            + ", "+(oferta.getCodPuesto() != null ? "'"+oferta.getCodPuesto()+"'" : "null")
//                            + ", "+(oferta.getExpEje() != null ? oferta.getExpEje() : "null")
//                            + ", "+(oferta.getNumExp() != null ? "'"+oferta.getNumExp()+"'" : "null")
//                            + ", "+(oferta.getAnoTitulacion() != null ? oferta.getAnoTitulacion().toString() : "null")
//                            + ", "+(oferta.getApe1() != null ? "'"+oferta.getApe1()+"'" : "null")
//                            + ", "+(oferta.getApe2() != null ? "'"+oferta.getApe2()+"'" : "null")
//                            + ", "+(oferta.getCodCausaBaja() != null ? "'"+oferta.getCodCausaBaja()+"'" : "null")
//                            + ", "+(oferta.getCodGrCot() != null ? "'"+oferta.getCodGrCot()+"'" : "null")
//                            + ", "+(oferta.getCodOferta() != null ? "'"+oferta.getCodOferta()+"'" : "null")
//                            + ", "+(oferta.getCodOfiGest() != null ? "'"+oferta.getCodOfiGest()+"'" : "null")
//                            + ", "+(oferta.getCodTipoNif() != null ? "'"+oferta.getCodTipoNif()+"'" : "null")
//                            + ", "+(oferta.getCodTitulacion() != null ? "'"+oferta.getCodTitulacion()+"'" : "null")
//                            + ", "+(oferta.getContratacion() != null ? "'"+oferta.getContratacion()+"'" : "null")
//                            + ", "+(oferta.getDietasConvenio() != null ? oferta.getDietasConvenio().toPlainString() : "null")
//                            + ", "+(oferta.getDietasConvoc() != null ? oferta.getDietasConvoc().toPlainString() : "null")
//                            + ", "+(oferta.getDifusion() != null ? "'"+oferta.getDifusion()+"'" : "null")
//                            + ", "+(oferta.getDocContratacion() != null ? oferta.getDocContratacion() : "null")
//                            + ", "+(oferta.getDocGestOfe() != null ? oferta.getDocGestOfe() : "null")
//                            + ", "+(oferta.getEmail() != null ? "'"+oferta.getEmail()+"'" : "null")
//                            + ", "+(oferta.getFecBaja() != null ? "TO_DATE('"+format.format(oferta.getFecBaja())+"', 'dd/mm/yyyy')" : "null")
//                            + ", "+(oferta.getFecDifusion() != null ? "TO_DATE('"+format.format(oferta.getFecDifusion())+"', 'dd/mm/yyyy')" : "null")
//                            + ", "+(oferta.getFecEnvCand() != null ? "TO_DATE('"+format.format(oferta.getFecEnvCand())+"', 'dd/mm/yyyy')" : "null")
//                            + ", "+(oferta.getFecFin() != null ? "TO_DATE('"+format.format(oferta.getFecFin())+"', 'dd/mm/yyyy')" : "null")
//                            + ", "+(oferta.getFecIni() != null ? "TO_DATE('"+format.format(oferta.getFecIni())+"', 'dd/mm/yyyy')" : "null")
//                            + ", "+(oferta.getFecNac() != null ? "TO_DATE('"+format.format(oferta.getFecNac())+"', 'dd/mm/yyyy')" : "null")
//                            + ", "+(oferta.getFlSustituto() != null ? "'"+oferta.getFlSustituto()+"'" : "null")
//                            + ", "+(oferta.getMotContratacion() != null ? "'"+oferta.getMotContratacion()+"'" : "null")
//                            + ", "+(oferta.getNif() != null ? "'"+oferta.getNif()+"'" : "null")
//                            + ", "+(oferta.getNombre() != null ? "'"+oferta.getNombre()+"'" : "null")
//                            + ", "+(oferta.getNumTotCand() != null ? oferta.getNumTotCand().toString() : "null")
//                            + ", "+(oferta.getObservaciones() != null ? "'"+oferta.getObservaciones()+"'" : "null")
//                            + ", "+(oferta.getPrec() != null ? "'"+oferta.getPrec()+"'" : "null")
//                            + ", "+(oferta.getPrecNom() != null ? "'"+oferta.getPrecNom()+"'" : "null")
//                            + ", "+(oferta.getSalarioB() != null ? oferta.getSalarioB().toPlainString() : "null")
//                            + ", "+(oferta.getSexo() != null ? "'"+oferta.getSexo()+"'" : "null")
//                            + ", "+(oferta.getTlf() != null ? "'"+oferta.getTlf()+"'" : "null")
//                            + ")";
//                }
//                else
//                {
//                    //Es una oferta que ya existe
//                    query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_OFERTA, ConstantesMeLanbide39.FICHERO_PROPIEDADES)
//                            + " set"
//                            + " CPE_OFE_ANO_TITULACION = "+(oferta.getAnoTitulacion() != null ? "'"+oferta.getAnoTitulacion()+"'" : "null")+", "
//                            + " CPE_OFE_APE1 = "+(oferta.getApe1() != null ? "'"+oferta.getApe1()+"'" : "null")+", "
//                            + " CPE_OFE_APE2 = "+(oferta.getApe2() != null ? "'"+oferta.getApe2()+"'" : "null")+", "
//                            + " CPE_OFE_COD_CAUSA_BAJA = "+(oferta.getCodCausaBaja() != null ? "'"+oferta.getCodCausaBaja()+"'" : "null")+", "
//                            + " CPE_OFE_COD_GR_COT = "+(oferta.getCodGrCot() != null ? "'"+oferta.getCodGrCot()+"'" : "null")+", "
//                            + " CPE_OFE_COD_OFERTA = "+(oferta.getCodOferta() != null ? "'"+oferta.getCodOferta()+"'" : "null")+", "
//                            + " CPE_OFE_COD_OFI_GEST = "+(oferta.getCodOfiGest() != null ? "'"+oferta.getCodOfiGest()+"'" : "null")+", "
//                            + " CPE_OFE_COD_TIPO_NIF = "+(oferta.getCodTipoNif() != null ? "'"+oferta.getCodTipoNif()+"'" : "null")+", "
//                            + " CPE_OFE_COD_TITULACION = "+(oferta.getCodTitulacion() != null ? "'"+oferta.getCodTitulacion()+"'" : "null")+", "
//                            + " CPE_OFE_CONTRATACION = "+(oferta.getContratacion() != null ? "'"+oferta.getContratacion()+"'" : "null")+", "
//                            + " CPE_OFE_DIETAS_CONVENIO = "+(oferta.getDietasConvenio() != null ? oferta.getDietasConvenio().toPlainString() : "null")+", "
//                            + " CPE_OFE_DIETAS_CONVOC = "+(oferta.getDietasConvoc() != null ? oferta.getDietasConvoc().toPlainString() : "null")+", "
//                            + " CPE_OFE_DIFUSION = "+(oferta.getDifusion() != null ? "'"+oferta.getDifusion()+"'" : "null")+", "
//                            + " CPE_OFE_DOC_CONTRATACION = "+(oferta.getDocContratacion() != null ? oferta.getDocContratacion() : "null")+", "
//                            + " CPE_OFE_DOC_GEST_OFE = "+(oferta.getDocGestOfe() != null ? oferta.getDocGestOfe() : "null")+", "
//                            + " CPE_OFE_EMAIL = "+(oferta.getEmail() != null ? "'"+oferta.getEmail()+"'" : "null")+", "
//                            + " CPE_OFE_FEC_BAJA = "+(oferta.getFecBaja() != null ? "TO_DATE('"+format.format(oferta.getFecBaja())+"', 'dd/mm/yyyy')" : "null")+", "
//                            + " CPE_OFE_FEC_DIFUSION = "+(oferta.getFecDifusion() != null ? "TO_DATE('"+format.format(oferta.getFecDifusion())+"', 'dd/mm/yyyy')" : "null")+", "
//                            + " CPE_OFE_FEC_ENV_CAND = "+(oferta.getFecEnvCand() != null ? "TO_DATE('"+format.format(oferta.getFecEnvCand())+"', 'dd/mm/yyyy')" : "null")+", "
//                            + " CPE_OFE_FEC_FIN = "+(oferta.getFecFin() != null ? "TO_DATE('"+format.format(oferta.getFecFin())+"', 'dd/mm/yyyy')" : "null")+", "
//                            + " CPE_OFE_FEC_INI = "+(oferta.getFecIni() != null ? "TO_DATE('"+format.format(oferta.getFecIni())+"', 'dd/mm/yyyy')" : "null")+", "
//                            + " CPE_OFE_FEC_NAC = "+(oferta.getFecNac() != null ? "TO_DATE('"+format.format(oferta.getFecNac())+"', 'dd/mm/yyyy')" : "null")+", "
//                            + " CPE_OFE_FL_SUSTITUTO = "+(oferta.getFlSustituto() != null ? "'"+oferta.getFlSustituto()+"'" : "null")+", "
//                            + " CPE_OFE_MOT_CONTRATACION = "+(oferta.getMotContratacion() != null ? "'"+oferta.getMotContratacion()+"'" : "null")+", "
//                            + " CPE_OFE_NIF = "+(oferta.getNif() != null ? "'"+oferta.getNif()+"'" : "null")+", "
//                            + " CPE_OFE_NOMBRE = "+(oferta.getNombre() != null ? "'"+oferta.getNombre()+"'" : "null")+", "
//                            + " CPE_OFE_NUM_TOT_CAND = "+(oferta.getNumTotCand() != null ? oferta.getNumTotCand().toString() : "null")+", "
//                            + " CPE_OFE_OBSERVACIONES = "+(oferta.getObservaciones() != null ? "'"+oferta.getObservaciones()+"'" : "null")+", "
//                            + " CPE_OFE_PREC = "+(oferta.getPrec() != null ? "'"+oferta.getPrec()+"'" : "null")
//                            + " CPE_OFE_PREC_NOM = "+(oferta.getPrecNom() != null ? "'"+oferta.getPrecNom()+"'" : "null")
//                            + " CPE_OFE_SALARIOB = "+(oferta.getSalarioB() != null ? oferta.getSalarioB().toPlainString() : "null")
//                            + " CPE_OFE_SEXO = "+(oferta.getSexo() != null ? "'"+oferta.getSexo()+"'" : "null")
//                            + " CPE_OFE_TLF = "+(oferta.getTlf() != null ? "'"+oferta.getTlf()+"'" : "null")
//                            + " where CPE_OFE_ID_OFERTA = '"+oferta.getIdOferta()+"'"
//                            + " and CPE_PTO_EXP_EJE = "+oferta.getExpEje().toString()
//                            + " and CPE_PTO_NUMEXP = '"+oferta.getNumExp()+"'";
//                }
//                if(log.isDebugEnabled()) 
//                    log.debug("sql = " + query);
//                st = con.createStatement();
//                int res = st.executeUpdate(query);
//                if(res > 0)
//                {
//                    return oferta;
//                }
//                else
//                {
//                    return null;
//                }
//            }
//            catch(Exception ex)
//            {
//                log.error("Se ha producido un error guardando los datos de la oferta "+(oferta != null ? oferta.getIdOferta() : "(oferta = null)")+" para el expediente "+(oferta != null ? oferta.getNumExp() : "(puesto = null)"), ex);
//                throw new Exception(ex);
//            }
//            finally
//            {
//                try
//                {
//                    if(log.isDebugEnabled()) 
//                        log.debug("Procedemos a cerrar el statement y el resultset");
//                    if(st!=null) 
//                        st.close();
//                }
//                catch(Exception e)
//                {
//                    log.error("Se ha producido un error cerrando el statement y el resulset", e);
//                    throw new Exception(e);
//                }
//            }
//        }
//    }
    
    public CpeOfertaVO guardarCpeOfertaVO(CpeOfertaVO oferta, Connection con) throws Exception
    {
        if(oferta == null)
        {
            return null;
        }
        else
        {
            PreparedStatement st = null;
            Statement stDelete = null;
            int resultDelete = 0;
            
            try
            {
                String query = null;
                SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide39.FORMATO_FECHA);
                String sus="";
                if(oferta.getIdOferta() == null)
                {
                    if(oferta.getCodPuesto() == null || oferta.getCodPuesto().equals(""))
                    {
                        throw new Exception();
                    }
                    Integer idOferta = this.getNuevoCodigoOferta(oferta, con);
                    oferta.setIdOferta(idOferta);
                    //Es un puesto nuevo
                    query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_OFERTA, ConstantesMeLanbide39.FICHERO_PROPIEDADES)
                            + "(CPE_OFE_ID_OFERTA, CPE_OFE_COD_PUESTO, CPE_OFE_EXP_EJE, CPE_OFE_NUMEXP, CPE_OFE_ANO_TITULACION, CPE_OFE_APE1, CPE_OFE_APE2,"
                            + " CPE_OFE_COD_CAUSA_BAJA,"
                            + " CPE_OFE_COD_GR_COT, CPE_OFE_COD_OFERTA, CPE_OFE_COD_OFI_GEST, CPE_OFE_COD_TIPO_NIF,"
                            + " CPE_OFE_COD_TITULACION, CPE_OFE_CONTRATACION,CPE_OFE_CONTRATACION_PRES_OFE,"
                            + " CPE_OFE_DIETAS_CONVENIO, CPE_OFE_DIETAS_CONVOC, CPE_OFE_DIFUSION, CPE_OFE_EMAIL,"
                            + " CPE_OFE_FEC_BAJA,"
                            + " CPE_OFE_FEC_DIFUSION, CPE_OFE_FEC_ENV_CAND, CPE_OFE_FEC_FIN, CPE_OFE_FEC_INI, CPE_OFE_FEC_NAC, CPE_OFE_FL_SUSTITUTO,"
                            + " CPE_OFE_MOT_CONTRATACION,"
                            + " CPE_OFE_NIF, CPE_OFE_NOMBRE, CPE_OFE_NUM_TOT_CAND, CPE_OFE_OBSERVACIONES, CPE_OFE_PREC, CPE_OFE_PREC_NOM,"
                            + " CPE_OFE_SALARIOB, CPE_OFE_SEXO, CPE_OFE_TLF, CPE_OFE_DOC_CONTRATACION, CPE_OFE_DOC_GEST_OFE, CPE_OFE_NOM_DOC_CONTRATACION, CPE_OFE_NOM_DOC_GEST_OFE, CPE_OFE_ID_OFERTA_ORIGEN, CPE_OFE_MESES_CONTRATO, "
                            + " CPE_OFE_DESC_PUESTO, CPE_OFE_PAI_COD_1, CPE_OFE_PAI_COD_2, CPE_OFE_PAI_COD_3,"    
                            + " CPE_OFE_COD_TIT_1, CPE_OFE_COD_TIT_2,CPE_OFE_COD_TIT_3,CPE_OFE_FUNCIONES, "    
                            + " CPE_OFE_COD_IDIOMA_1, CPE_OFE_COD_IDIOMA_2, CPE_OFE_COD_IDIOMA_3, "
                            + " CPE_OFE_COD_NIV_IDI_1,CPE_OFE_COD_NIV_IDI_2,CPE_OFE_COD_NIV_IDI_3,"   
                            +" CPE_OFE_COD_NIV_FORM,CPE_OFE_CIUDAD_DESTINO , CPE_OFE_DPTO, CPE_OFE_COD_CAUSA_RENUNCIA, CPE_OFE_COD_CAUSA_REN_PRE_OF "
                            + ")"
                            + " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
                            + "?,?,?,?,?,?,?,?,?,?,?,"
                            + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? "
                            + ")";
                    
                            
                            if(log.isDebugEnabled()) 
                                log.debug("sql = " + query);
                            st = con.prepareStatement(query);
                            
                            if(log.isDebugEnabled()) 
                                log.debug("sql = " + query);
                            st = con.prepareStatement(query);
                            st.setInt(1, oferta.getIdOferta());
                            st.setString(2, oferta.getCodPuesto());
                            st.setInt(3, oferta.getExpEje());
                            st.setString(4, oferta.getNumExp());
                            if(oferta.getAnoTitulacion() != null)
                            {
                                st.setInt(5, oferta.getAnoTitulacion());
                            }
                            else
                            {
                                st.setNull(5, java.sql.Types.INTEGER); 
                            }
                            st.setString(6, oferta.getApe1());
                            st.setString(7, oferta.getApe2());
                            st.setString(8, oferta.getCodCausaBaja());
                            st.setString(9, oferta.getCodGrCot());
                            st.setString(10, oferta.getCodOferta());
                            st.setString(11, oferta.getCodOfiGest());
                            st.setString(12, oferta.getCodTipoNif());
                            st.setString(13, oferta.getCodTitulacion());
                            st.setString(14, oferta.getContratacion());
                            st.setString(15, oferta.getContratacionPresOferta());
                            st.setBigDecimal(16, oferta.getDietasConvenio());
                            st.setBigDecimal(17, oferta.getDietasConvoc());
                            st.setString(18, oferta.getDifusion());
                            st.setString(19, oferta.getEmail());
                            st.setDate(20, oferta.getFecBaja() != null ? new java.sql.Date(oferta.getFecBaja().getTime()) : null);
                            st.setDate(21, oferta.getFecDifusion() != null ? new java.sql.Date(oferta.getFecDifusion().getTime()) : null);
                            st.setDate(22, oferta.getFecEnvCand() != null ? new java.sql.Date(oferta.getFecEnvCand().getTime()) : null);
                            st.setDate(23, oferta.getFecFin() != null ? new java.sql.Date(oferta.getFecFin().getTime()) : null);
                            st.setDate(24, oferta.getFecIni() != null ? new java.sql.Date(oferta.getFecIni().getTime()) : null);
                            st.setDate(25, oferta.getFecNac() != null ? new java.sql.Date(oferta.getFecNac().getTime()) : null);
                            st.setString(26, oferta.getFlSustituto());
                            if(oferta.getFlSustituto() != null){
                                sus=oferta.getFlSustituto();
                            } 
                            st.setString(27, oferta.getMotContratacion());
                            st.setString(28, oferta.getNif());
                            st.setString(29, oferta.getNombre());
                            if(oferta.getNumTotCand() != null)
                            {
                                st.setInt(30, oferta.getNumTotCand());
                            }
                            else
                            {
                                st.setNull(30, java.sql.Types.INTEGER); 
                            }
                            st.setString(31, oferta.getObservaciones());
                            st.setString(32, oferta.getPrec());
                            st.setString(33, oferta.getPrecNom());
                            st.setBigDecimal(34, oferta.getSalarioB());
                            st.setString(35, oferta.getSexo());
                            st.setString(36, oferta.getTlf());
                            st.setBytes(37, oferta.getDocContratacion());
                            st.setBytes(38, oferta.getDocGestOfe());
                            st.setString(39, oferta.getNomDocContratacion());
                            st.setString(40, oferta.getNomDocGestOfe());
                            if(oferta.getIdOfertaOrigen() != null)
                            {
                                st.setInt(41, oferta.getIdOfertaOrigen());
                            }
                            else
                            {
                                st.setNull(41, java.sql.Types.INTEGER); 
                            }
                            if(oferta.getMesesContrato() != null)
                            {
                                st.setInt(42, oferta.getMesesContrato());
                            }
                            else
                            {
                                st.setNull(42, java.sql.Types.INTEGER); 
                            }
                            st.setString(43, oferta.getDescPuesto());
                            if(oferta.getPaiCod1() != null)
                            {
                                st.setInt(44, oferta.getPaiCod1());
                            }
                            else
                            {
                                st.setNull(44, java.sql.Types.INTEGER); 
                            }
                            if(oferta.getPaiCod2() != null)
                            {
                                st.setInt(45, oferta.getPaiCod2());
                            }
                            else
                            {
                                st.setNull(45, java.sql.Types.INTEGER); 
                            }
                            if(oferta.getPaiCod3() != null)
                            {
                                st.setInt(46, oferta.getPaiCod3());
                            }
                            else
                            {
                                st.setNull(46, java.sql.Types.INTEGER); 
                            }
                            st.setString(47, oferta.getCodTit1());
                            st.setString(48, oferta.getCodTit2());
                            st.setString(49, oferta.getCodTit3());
                            st.setString(50, oferta.getFunciones());
                            st.setString(51, oferta.getCodIdioma1());
                            st.setString(52, oferta.getCodIdioma2());
                            st.setString(53, oferta.getCodIdioma3());
                            st.setString(54, oferta.getCodNivIdi1());
                            st.setString(55, oferta.getCodNivIdi2());
                            st.setString(56, oferta.getCodNivIdi3());
                            st.setString(57, oferta.getCodNivForm());
                            st.setString(58, oferta.getCiudadDestino());
                            st.setString(59, oferta.getDpto());
                            st.setString(60, oferta.getCodCausaRenuncia());
                            st.setString(61, oferta.getCodCausaRenunciaPresOferta());
                }
                else
                {
                    //Es una oferta que ya existe
                    query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_OFERTA, ConstantesMeLanbide39.FICHERO_PROPIEDADES)
                            + " set"
                            + " CPE_OFE_ANO_TITULACION = ?,"
                            + " CPE_OFE_APE1 = ?,"
                            + " CPE_OFE_APE2 = ?,"
                            + " CPE_OFE_COD_CAUSA_BAJA = ?,"
                            + " CPE_OFE_COD_GR_COT = ?,"
                            + " CPE_OFE_COD_OFERTA = ?,"
                            + " CPE_OFE_COD_OFI_GEST = ?,"
                            + " CPE_OFE_COD_TIPO_NIF = ?,"
                            + " CPE_OFE_COD_TITULACION = ?,"
                            + " CPE_OFE_CONTRATACION = ?,"
                            + " CPE_OFE_CONTRATACION_PRES_OFE = ?,"
                            + " CPE_OFE_DIETAS_CONVENIO = ?,"
                            + " CPE_OFE_DIETAS_CONVOC = ?,"
                            + " CPE_OFE_DIFUSION = ?,"
                            + " CPE_OFE_EMAIL = ?,"
                            + " CPE_OFE_FEC_BAJA = ?,"
                            + " CPE_OFE_FEC_DIFUSION = ?,"
                            + " CPE_OFE_FEC_ENV_CAND = ?,"
                            + " CPE_OFE_FEC_FIN = ?,"
                            + " CPE_OFE_FEC_INI = ?,"
                            + " CPE_OFE_FEC_NAC = ?,"
                            + " CPE_OFE_FL_SUSTITUTO = ?,"
                            + " CPE_OFE_MOT_CONTRATACION = ?,"
                            + " CPE_OFE_NIF = ?,"
                            + " CPE_OFE_NOMBRE = ?,"
                            + " CPE_OFE_NUM_TOT_CAND = ?,"
                            + " CPE_OFE_OBSERVACIONES = ?,"
                            + " CPE_OFE_PREC = ?,"
                            + " CPE_OFE_PREC_NOM = ?,"
                            + " CPE_OFE_SALARIOB = ?,"
                            + " CPE_OFE_SEXO = ?,"
                            + " CPE_OFE_TLF = ?,"
                            + " CPE_OFE_DOC_CONTRATACION = ?,"
                            + " CPE_OFE_DOC_GEST_OFE = ?,"
                            + " CPE_OFE_NOM_DOC_CONTRATACION = ?,"
                            + " CPE_OFE_NOM_DOC_GEST_OFE = ?,"
                            + " CPE_OFE_ID_OFERTA_ORIGEN = ?,"
                            + " CPE_OFE_MESES_CONTRATO = ?,"
                            
                            + " CPE_OFE_DESC_PUESTO = ?, CPE_OFE_PAI_COD_1 = ?, CPE_OFE_PAI_COD_2 = ?, CPE_OFE_PAI_COD_3 = ?,"    
                            + " CPE_OFE_COD_TIT_1 = ?, CPE_OFE_COD_TIT_2 = ?,CPE_OFE_COD_TIT_3 = ?,CPE_OFE_FUNCIONES = ?, "    
                            + " CPE_OFE_COD_IDIOMA_1 = ?, CPE_OFE_COD_IDIOMA_2 = ?, CPE_OFE_COD_IDIOMA_3 = ?, "
                            + " CPE_OFE_COD_NIV_IDI_1 = ?,CPE_OFE_COD_NIV_IDI_2 = ?,CPE_OFE_COD_NIV_IDI_3 = ?,"   
                            +" CPE_OFE_COD_NIV_FORM = ?, "
                            +" CPE_OFE_CIUDAD_DESTINO = ?, "
                            +" CPE_OFE_DPTO = ?,"
                            +" CPE_OFE_COD_CAUSA_RENUNCIA = ?, "
                            +" CPE_OFE_COD_CAUSA_REN_PRE_OF = ? "
                            
                            + " where CPE_OFE_ID_OFERTA = ?"
                            + " and CPE_OFE_COD_PUESTO = ?"
                            + " and CPE_OFE_EXP_EJE = ?"
                            + " and CPE_OFE_NUMEXP = ?";
                            
                    
                            if(log.isDebugEnabled()) 
                                log.debug("sql = " + query);
                            st = con.prepareStatement(query);
                    
                            if(oferta.getAnoTitulacion() != null)
                            {
                                st.setInt(1, oferta.getAnoTitulacion());
                            }
                            else
                            {
                                st.setNull(1, java.sql.Types.INTEGER); 
                            }
                            st.setString(2, oferta.getApe1());
                            st.setString(3, oferta.getApe2());
                            st.setString(4, oferta.getCodCausaBaja());
                            st.setString(5, oferta.getCodGrCot());
                            st.setString(6, oferta.getCodOferta());
                            st.setString(7, oferta.getCodOfiGest());
                            st.setString(8, oferta.getCodTipoNif());
                            st.setString(9, oferta.getCodTitulacion());
                            st.setString(10, oferta.getContratacion());
                            st.setString(11, oferta.getContratacionPresOferta());
                            st.setBigDecimal(12, oferta.getDietasConvenio());
                            st.setBigDecimal(13, oferta.getDietasConvoc());
                            st.setString(14, oferta.getDifusion());
                            st.setString(15, oferta.getEmail());
                            st.setDate(16, oferta.getFecBaja() != null ? new java.sql.Date(oferta.getFecBaja().getTime()) : null);
                            st.setDate(17, oferta.getFecDifusion() != null ? new java.sql.Date(oferta.getFecDifusion().getTime()) : null);
                            st.setDate(18, oferta.getFecEnvCand() != null ? new java.sql.Date(oferta.getFecEnvCand().getTime()) : null);
                            st.setDate(19, oferta.getFecFin() != null ? new java.sql.Date(oferta.getFecFin().getTime()) : null);
                            st.setDate(20, oferta.getFecIni() != null ? new java.sql.Date(oferta.getFecIni().getTime()) : null);
                            st.setDate(21, oferta.getFecNac() != null ? new java.sql.Date(oferta.getFecNac().getTime()) : null);
                            st.setString(22, oferta.getFlSustituto());
                            if(oferta.getFlSustituto() != null){
                                sus=oferta.getFlSustituto();
                            } 
                            st.setString(23, oferta.getMotContratacion());
                            st.setString(24, oferta.getNif());
                            st.setString(25, oferta.getNombre());
                            if(oferta.getNumTotCand() != null)
                            {
                                st.setInt(26, oferta.getNumTotCand());
                            }
                            else
                            {
                                st.setNull(26, java.sql.Types.INTEGER); 
                            }
                            st.setString(27, oferta.getObservaciones());
                            st.setString(28, oferta.getPrec());
                            st.setString(29, oferta.getPrecNom());
                            st.setBigDecimal(30, oferta.getSalarioB());
                            st.setString(31, oferta.getSexo());
                            st.setString(32, oferta.getTlf());
                            st.setBytes(33, oferta.getDocContratacion());
                            st.setBytes(34, oferta.getDocGestOfe());
                            st.setString(35, oferta.getNomDocContratacion());
                            st.setString(36, oferta.getNomDocGestOfe());
                            if(oferta.getIdOfertaOrigen() != null)
                            {
                                st.setInt(37, oferta.getIdOfertaOrigen());
                            }
                            else
                            {
                                st.setNull(37, java.sql.Types.INTEGER); 
                            }
                            if(oferta.getMesesContrato() != null)
                            {
                                st.setInt(38, oferta.getMesesContrato());
                            }
                            else
                            {
                                st.setNull(38, java.sql.Types.INTEGER); 
                            }
                            
                            st.setString(39, oferta.getDescPuesto());
                            if(oferta.getPaiCod1() != null)
                            {
                                st.setInt(40, oferta.getPaiCod1());
                            }
                            else
                            {
                                st.setNull(40, java.sql.Types.INTEGER); 
                            }
                            if(oferta.getPaiCod2() != null)
                            {
                                st.setInt(41, oferta.getPaiCod2());
                            }
                            else
                            {
                                st.setNull(41, java.sql.Types.INTEGER); 
                            }
                            if(oferta.getPaiCod3() != null)
                            {
                                st.setInt(42, oferta.getPaiCod3());
                            }
                            else
                            {
                                st.setNull(42, java.sql.Types.INTEGER); 
                            }
                            st.setString(43, oferta.getCodTit1());
                            st.setString(44, oferta.getCodTit2());
                            st.setString(45, oferta.getCodTit3());
                            st.setString(46, oferta.getFunciones());
                            st.setString(47, oferta.getCodIdioma1());
                            st.setString(48, oferta.getCodIdioma2());
                            st.setString(49, oferta.getCodIdioma3());
                            st.setString(50, oferta.getCodNivIdi1());
                            st.setString(51, oferta.getCodNivIdi2());
                            st.setString(52, oferta.getCodNivIdi3());
                            st.setString(53, oferta.getCodNivForm());
                            st.setString(54, oferta.getCiudadDestino());
                            st.setString(55, oferta.getDpto());
                            st.setString(56, oferta.getCodCausaRenuncia());
                            st.setString(57, oferta.getCodCausaRenunciaPresOferta());
                            st.setInt(58, oferta.getIdOferta());
                            st.setString(59, oferta.getCodPuesto());
                            st.setInt(60, oferta.getExpEje());
                            st.setString(61, oferta.getNumExp());
                            
                }
                
                int cpe_ofe_id_oferta=0;
                if (oferta.getIdOferta()!=null){
                    cpe_ofe_id_oferta=oferta.getIdOferta();
                }else{
                    cpe_ofe_id_oferta=0;
                }
                
                if (sus!=null){
                    if (sus.equals("N") ){
                        eliminarSustituto(cpe_ofe_id_oferta,oferta.getCodPuesto(),oferta.getNumExp(),con);
                    }
                }
                /*if (sus.equals("N") && oferta.getIdOfertaOrigen()==null){
                    String queryDelete = null;
                    queryDelete = "DELETE FROM CPE_OFERTA " 
                    + " WHERE CPE_OFE_NUMEXP = '"+oferta.getNumExp()+"'"
                    + " and cpe_ofe_cod_puesto = "+oferta.getCodPuesto()
                    + " and cpe_ofe_id_oferta_origen = '"+oferta.getIdOfertaOrigen()+"'";

                    if(log.isDebugEnabled()) 
                        log.debug("sqlDelete = " + queryDelete);
                    stDelete = con.createStatement();
                    resultDelete = st.executeUpdate(queryDelete);
                }*/
                
                int res = st.executeUpdate();
                if(res > 0)
                {
                    return oferta;
                }
                else
                {
                    return null;
                }
            }
            catch(Exception ex)
            {
                log.error("Se ha producido un error guardando los datos de la oferta "+(oferta != null ? oferta.getIdOferta() : "(oferta = null)")+" para el expediente "+(oferta != null ? oferta.getNumExp() : "(puesto = null)"), ex);
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
    }
    
    public String eliminarSustituto(int idOferta, String idPuesto,String numExp, Connection con) throws Exception
    {
        CallableStatement st = null;
        ResultSet rs = null;
        try
        {
            String query = null;
            query = "call ELIMINA_SUSTITUTO_CPE (?,?,?)";
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.prepareCall(query);
            st.setInt(1, idOferta); 
            st.setString(2, idPuesto);
            st.setString(3, numExp);
            int resu = st.executeUpdate();
            return "";
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error borrando sustituto", ex);
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
    
    /*private Integer getNextId(String seqName, Connection con) throws Exception
    {
        
        Statement st = null;
        ResultSet rs = null;
        Integer numSec = null;
        try
        {
            String query = null;
            //Creo el id con la secuencia
            query = "select "+ seqName+".nextval from dual";
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                numSec = rs.getInt(1);
                if(rs.wasNull())
                {
                    throw new Exception();
                }
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error generando el siguiente valor de la secuencia "+seqName, ex);
            throw new Exception(ex);
        }
        return numSec;
    }*/
    
    public List<SelectItem> getTiposDocumento(Connection con) throws Exception
    {
        List<ValorCampoDesplegableModuloIntegracionVO> listaValores = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
            Statement st = null;
            ResultSet rs = null;
        try
        {
            String query = null;
            //Creo el id con la secuencia
            query = "SELECT TID_COD as CODIGO,TID_DES as DESCRIPCION FROM "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_TIPO_DOCUMENTO, ConstantesMeLanbide39.FICHERO_PROPIEDADES)
                   +" ORDER BY TID_COD ASC";
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            
            while(rs.next())
            {
                listaValores.add((ValorCampoDesplegableModuloIntegracionVO)MeLanbide39MappingUtils.getInstance().map(rs, ValorCampoDesplegableModuloIntegracionVO.class));
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando lista tipos documento.", ex);
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
        return MeLanbide39MappingUtils.getInstance().fromCampoDesplegableToSelectItemVO(listaValores);
    }
    
    public int eliminarCpeOferta(CpeOfertaVO p, Connection con) throws Exception
    {
        Statement st = null;
        try
        {
            String query = null;
                query = "delete from "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_OFERTA, ConstantesMeLanbide39.FICHERO_PROPIEDADES)
                       +" where CPE_OFE_ID_OFERTA = '"+p.getIdOferta()+"'"
                       +" and CPE_OFE_EXP_EJE = "+p.getExpEje()
                       +" and CPE_OFE_NUMEXP = '"+p.getNumExp()+"'";
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            return st.executeUpdate(query);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error eliminando oferta  "+(p != null ? p.getIdOferta() : "oferta = null"), ex);
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
    
    
    
    public HashMap<String, String> getDatosTercero(int codOrganizacion, String numExp, String ejercicio, String rol, Connection con)throws Exception
    {
        HashMap<String, String> map = new HashMap<String, String>();
        Statement st = null;
        ResultSet rs = null;
        try
        {
            String query = null;
            
            query = "select TER_NOC from "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_REL_TERCERO_DOMICILIO, ConstantesMeLanbide39.FICHERO_PROPIEDADES)+" r"
                   +" inner join "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_TERCEROS, ConstantesMeLanbide39.FICHERO_PROPIEDADES)+" t"
                   +" on r.EXT_TER  = t.TER_COD"
                   +" and r.EXT_MUN = '"+codOrganizacion+"' and r.EXT_EJE = '"+ejercicio+"' and r.EXT_NUM = '"+numExp+"' and r.EXT_ROL = "+rol;
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                map.put("TER_NOC", rs.getString("TER_NOC"));
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando las ÃƒÂ¡reas", ex);
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
        return map;
    }
    
    public int eliminarOfertasPorPuestoYExpediente(CpePuestoVO p, Connection con) throws Exception
    {
        Statement st = null;
        try
        {
            String query = null;
                query = "delete from "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_OFERTA, ConstantesMeLanbide39.FICHERO_PROPIEDADES)
                       +" where CPE_OFE_COD_PUESTO = '"+p.getCodPuesto()+"'"
                       +" and CPE_OFE_EXP_EJE = "+p.getEjercicio()
                       +" and CPE_OFE_NUMEXP = '"+p.getNumExp()+"'";
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            return st.executeUpdate(query);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error eliminando ofertas para puesto  "+(p != null ? p.getCodPuesto() : "puesto = null"), ex);
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
    
    public int eliminarJustificacionesPorPuestoYExpediente(CpePuestoVO p, Connection con) throws Exception
    {
        Statement st = null;
        try
        {
            String query = null;
                query = "delete from "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_JUSTIFICACION, ConstantesMeLanbide39.FICHERO_PROPIEDADES)
                       +" where CPE_JUS_COD_PUESTO = '"+p.getCodPuesto()+"'"
                       +" and CPE_JUS_EXP_EJE = "+p.getEjercicio()
                       +" and CPE_JUS_NUMEXP = '"+p.getNumExp()+"'";
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            return st.executeUpdate(query);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error eliminando justificaciones para puesto  "+(p != null ? p.getCodPuesto() : "puesto = null"), ex);
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
    
    public List<FilaJustificacionVO> getListaJustificacionesNoDenegadasPorExpediente(String numExpediente, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        List<FilaJustificacionVO> justificaciones = new ArrayList<FilaJustificacionVO>();
        try
        {
            String query = null;
                
                /*query = "select tmp.*,"
                        +" causab.DES_NOM as CAUSA_BAJA,"
                        +" estado.DES_NOM as ESTADO"
                        +" from"
                        +" (select"
                        +" CPE_JUS_ID_JUSTIFICACION as ID_JUSTIFICACION,"
                        +" CPE_OFE_ID_OFERTA as ID_OFERTA,"
                        +" CPE_OFE_COD_PUESTO as COD_PUESTO,"
                        +" CPE_PTO_DESC_PUESTO as DESC_PUESTO,"
                        +" CPE_OFE_COD_OFERTA as DESC_OFERTA,"
                        +" CPE_OFE_NOMBRE as NOMBRE,"
                        +" CPE_OFE_APE1 as APE1,"
                        +" CPE_OFE_APE2 as APE2,"
                        +" CPE_OFE_NIF as NIF,"
                        +" CPE_OFE_FEC_INI as FEC_INI,"
                        +" CPE_OFE_FEC_FIN as FEC_FIN,"
                        +" CPE_OFE_FEC_BAJA as FEC_BAJA,"
                        +" CPE_OFE_COD_CAUSA_BAJA as COD_CBAJA,"
                        +" CPE_JUS_COD_ESTADO as COD_ESTADO"
                        +" from "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_OFERTA, ConstantesMeLanbide39.FICHERO_PROPIEDADES) +" oferta"
                        +" inner join "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_PUESTO, ConstantesMeLanbide39.FICHERO_PROPIEDADES) +" puesto on oferta.CPE_OFE_COD_PUESTO = puesto.CPE_PTO_COD_PUESTO and puesto.CPE_PTO_NUMEXP = oferta.CPE_OFE_NUMEXP"
                        +" inner join "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_JUSTIFICACION, ConstantesMeLanbide39.FICHERO_PROPIEDADES) +" justif on oferta.CPE_OFE_ID_OFERTA = justif.CPE_JUS_ID_OFERTA and oferta.CPE_OFE_COD_PUESTO = justif.CPE_JUS_COD_PUESTO and oferta.CPE_OFE_NUMEXP = justif.CPE_JUS_NUMEXP"
                        +" where puesto.CPE_PTO_NUMEXP = '"+numExpediente+"'"
                        +" and puesto.CPE_PTO_COD_RESULT <> '"+ConstantesMeLanbide39.CODIGO_RESULTADO_DENEGADO+"'"
                        //+" and oferta.CPE_OFE_FEC_BAJA is null"
                        +" )tmp"
                        +" left join (select * from "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_E_DES_VAL, ConstantesMeLanbide39.FICHERO_PROPIEDADES) +" where DES_COD = '"+ConfigurationParameter.getParameter(ConstantesMeLanbide39.CAMPO_SUPL_CAUSA_BAJA, ConstantesMeLanbide39.FICHERO_PROPIEDADES) +"') causab on tmp.COD_CBAJA = causab.DES_VAL_COD"
                        +" left join (select * from "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_E_DES_VAL, ConstantesMeLanbide39.FICHERO_PROPIEDADES) +" where DES_COD = '"+ConfigurationParameter.getParameter(ConstantesMeLanbide39.CAMPO_SUPL_ESTADO_JUSTIF, ConstantesMeLanbide39.FICHERO_PROPIEDADES) +"') estado on tmp.COD_ESTADO = estado.DES_VAL_COD"
                        +" order by tmp.COD_PUESTO, tmp.ID_OFERTA, tmp.FEC_BAJA";*/
                
                query = "select tmp.COD_PUESTO, tmp.DESC_PUESTO, tmp.IMP_SOLIC, sum(tmp.IMP_JUSTIF) as IMP_JUSTIF, codigosP.total as NUM_CONTRATACIONES,"
                        +" tmp.COD_ESTADO,"
                        +" estado.DES_NOM as ESTADO, SALARIO_SUB, DIETAS_JUSTI, GASTOS_GESION, BONIF, "
                        +" BASE_COTIZACION, COEF_APLICADO, PORC_FOGASA, PORC_COEFICIENTE, PORC_APORTACION"
                        +" from"
                        +" (select"
                        +" CPE_OFE_COD_PUESTO as COD_PUESTO,"
                        +" CPE_OFE_DESC_PUESTO as DESC_PUESTO,"
                        +" CPE_PTO_IMP_TOT_SOLIC as IMP_SOLIC,"
                        +" CPE_JUS_IMP_JUSTIFICADO as IMP_JUSTIF,"
                        +" CPE_JUS_SALARIOSUB as SALARIO_SUB, "
                        + "CPE_JUS_DIETAS as DIETAS_JUSTI, " 
                        + "CPE_JUS_GASTOSGES as GASTOS_GESION, "
                        + "CPE_JUS_BONIF as BONIF, " 
                        + "CPE_JUS_MINORACION as MINORACION,"
                        + "CPE_JUS_BASCOT_CC as BASE_COTIZACION,"
                        + "CPE_JUS_COEF_TGSS as COEF_APLICADO, "
                        + "CPE_JUS_FOGASA  as PORC_FOGASA,"
                        + "CPE_JUS_AT as PORC_COEFICIENTE, "
                        + "CPE_JUS_EPSV as PORC_APORTACION,"
                        +" CPE_JUS_COD_ESTADO as COD_ESTADO"
                        +" from "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_OFERTA, ConstantesMeLanbide39.FICHERO_PROPIEDADES) +" oferta"
                        +" inner join "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_PUESTO, ConstantesMeLanbide39.FICHERO_PROPIEDADES) +" puesto on oferta.CPE_OFE_COD_PUESTO = puesto.CPE_PTO_COD_PUESTO and puesto.CPE_PTO_NUMEXP = oferta.CPE_OFE_NUMEXP"
                        +" inner join "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_JUSTIFICACION, ConstantesMeLanbide39.FICHERO_PROPIEDADES) +" justif on oferta.CPE_OFE_ID_OFERTA = justif.CPE_JUS_ID_OFERTA and oferta.CPE_OFE_COD_PUESTO = justif.CPE_JUS_COD_PUESTO and oferta.CPE_OFE_NUMEXP = justif.CPE_JUS_NUMEXP"
                        +" where puesto.CPE_PTO_NUMEXP = '"+numExpediente+"'"
                        +" and puesto.CPE_PTO_COD_RESULT <> '"+ConstantesMeLanbide39.CODIGO_RESULTADO_DENEGADO+"'"
                        +" and puesto.CPE_PTO_COD_RESULT <> '"+ConstantesMeLanbide39.CODIGO_RESULTADO_RENUNCIA+"'"
                        +" )tmp"
                        +" left join (select * from "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_E_DES_VAL, ConstantesMeLanbide39.FICHERO_PROPIEDADES) +" where DES_COD = '"+ConfigurationParameter.getParameter(ConstantesMeLanbide39.CAMPO_SUPL_ESTADO_JUSTIF, ConstantesMeLanbide39.FICHERO_PROPIEDADES) +"') estado on tmp.COD_ESTADO = estado.DES_VAL_COD"
                        
                        +" left join (select distinct CPE_OFE_COD_PUESTO, count(*) as total"
                        +" from "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_OFERTA, ConstantesMeLanbide39.FICHERO_PROPIEDADES) +" where CPE_OFE_NUMEXP = '"+numExpediente+"' and CPE_OFE_NIF is not null group by CPE_OFE_COD_PUESTO)"
                        +" codigosP on tmp.COD_PUESTO = codigosP.CPE_OFE_COD_PUESTO"
                        
                        +" group by tmp.COD_PUESTO, tmp.DESC_PUESTO, tmp.IMP_SOLIC, tmp.COD_ESTADO, estado.DES_NOM, codigosP.total, "
                        + " SALARIO_SUB, DIETAS_JUSTI, GASTOS_GESION, BONIF, BASE_COTIZACION, COEF_APLICADO, PORC_FOGASA, PORC_COEFICIENTE, PORC_APORTACION"
                        
                        +" order by tmp.COD_PUESTO";
            
            
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while(rs.next())
            {
                justificaciones.add((FilaJustificacionVO)MeLanbide39MappingUtils.getInstance().map(rs, FilaJustificacionVO.class));
            }
            return justificaciones;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando justificaciones para el expediente "+numExpediente, ex);
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
    
    
    public List<FilaJustificacionVO> getListaJustificacionesHistNoDenegadasPorExpediente(int codOrganizacion, String numExpediente, String codTramite, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        List<FilaJustificacionVO> justificaciones = new ArrayList<FilaJustificacionVO>();
        try
        {
            String query = null;
                
                query = "select tmp.COD_PUESTO, tmp.DESC_PUESTO, tmp.IMP_SOLIC, sum(tmp.IMP_JUSTIF) as IMP_JUSTIF, codigosP.total as NUM_CONTRATACIONES,"
                        +" tmp.COD_ESTADO,"
                        +" estado.DES_NOM as ESTADO, SALARIO_SUB, DIETAS_JUSTI, GASTOS_GESION, BONIF "
                        //+" BASE_COTIZACION, COEF_APLICADO, PORC_FOGASA, PORC_COEFICIENTE, PORC_APORTACION"
                        +" from"
                        +" (select"
                        +" CPE_OFE_COD_PUESTO as COD_PUESTO,"
                        +" CPE_OFE_DESC_PUESTO as DESC_PUESTO,"
                        +" CPE_PTO_IMP_TOT_SOLIC as IMP_SOLIC,"
                        +" CPE_JUS_IMP_JUSTIFICADO as IMP_JUSTIF,"
                        +" CPE_JUS_SALARIOSUB as SALARIO_SUB, "
                        + "CPE_JUS_DIETAS as DIETAS_JUSTI, " 
                        + "CPE_JUS_GASTOSGES as GASTOS_GESION, "
                        + "CPE_JUS_BONIF as BONIF, " 
                        //+ "CPE_JUS_MINORACION as MINORACION,"
                        //+ "CPE_JUS_BASCOT_CC as BASE_COTIZACION,"
                        //+ "CPE_JUS_COEF_TGSS as COEF_APLICADO, "
                        //+ "CPE_JUS_FOGASA  as PORC_FOGASA,"
                        //+ "CPE_JUS_AT as PORC_COEFICIENTE, "
                        //+ "CPE_JUS_EPSV as PORC_APORTACION, "
                        +" CPE_JUS_COD_ESTADO as COD_ESTADO"
                        +" from "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_HIST_OFERTA, ConstantesMeLanbide39.FICHERO_PROPIEDADES) +" oferta"
                        +" inner join "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_HIST_PUESTO, ConstantesMeLanbide39.FICHERO_PROPIEDADES) +" puesto on oferta.CPE_OFE_COD_PUESTO = puesto.CPE_PTO_COD_PUESTO and puesto.CPE_PTO_NUMEXP = oferta.CPE_OFE_NUMEXP"
                        +" inner join "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_HIST_JUSTIFICACION, ConstantesMeLanbide39.FICHERO_PROPIEDADES) +" justif on oferta.CPE_OFE_ID_OFERTA = justif.CPE_JUS_ID_OFERTA and oferta.CPE_OFE_COD_PUESTO = justif.CPE_JUS_COD_PUESTO and oferta.CPE_OFE_NUMEXP = justif.CPE_JUS_NUMEXP"
                        +" inner join E_TRA ON TRA_PRO = 'CPE' AND TRA_COD = CPE_JUS_COD_TRAMITE AND TRA_MUN = "+ codOrganizacion + " AND TRA_COU = " + codTramite
                        +" where puesto.CPE_PTO_NUMEXP = '"+numExpediente+"'"// and CPE_JUS_COD_TRAMITE = " + codTramite
                        +" and puesto.CPE_PTO_COD_RESULT <> '"+ConstantesMeLanbide39.CODIGO_RESULTADO_DENEGADO+"'"
                        +" and puesto.CPE_PTO_COD_RESULT <> '"+ConstantesMeLanbide39.CODIGO_RESULTADO_RENUNCIA+"'"
                        +" )tmp"
                        +" left join (select * from "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_E_DES_VAL, ConstantesMeLanbide39.FICHERO_PROPIEDADES) +" where DES_COD = '"+ConfigurationParameter.getParameter(ConstantesMeLanbide39.CAMPO_SUPL_ESTADO_JUSTIF, ConstantesMeLanbide39.FICHERO_PROPIEDADES) +"') estado on tmp.COD_ESTADO = estado.DES_VAL_COD"
                        
                        +" left join (select distinct CPE_OFE_COD_PUESTO, count(*) as total"
                        +" from "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_HIST_OFERTA, ConstantesMeLanbide39.FICHERO_PROPIEDADES) +" where CPE_OFE_NUMEXP = '"+numExpediente+"' and CPE_OFE_NIF is not null group by CPE_OFE_COD_PUESTO)"
                        +" codigosP on tmp.COD_PUESTO = codigosP.CPE_OFE_COD_PUESTO"
                        
                        +" group by tmp.COD_PUESTO, tmp.DESC_PUESTO, tmp.IMP_SOLIC, tmp.COD_ESTADO, estado.DES_NOM, codigosP.total, "
                        //+ " SALARIO_SUB, DIETAS_JUSTI, GASTOS_GESION, BONIF, BASE_COTIZACION, COEF_APLICADO, PORC_FOGASA, PORC_COEFICIENTE, PORC_APORTACION"                        
                        + " SALARIO_SUB, DIETAS_JUSTI, GASTOS_GESION, BONIF"                        
                        +" order by tmp.COD_PUESTO";
            
            
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while(rs.next())
            {
                justificaciones.add((FilaJustificacionVO)MeLanbide39MappingUtils.getInstance().map(rs, FilaJustificacionVO.class));
            }
            return justificaciones;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando justificaciones de historico para el expediente "+numExpediente, ex);
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
    
    public CpeJustificacionVO getJustificacionPorCodigoPuestoYExpediente(CpeJustificacionVO p, Connection con) throws Exception
    {
        if(p != null && p.getIdJustificacion() != null)
        {
            Statement st = null;
            ResultSet rs = null;
            CpeJustificacionVO justif = null;
            try
            {
                String query = "select * from "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_JUSTIFICACION, ConstantesMeLanbide39.FICHERO_PROPIEDADES)
                              +" where CPE_JUS_ID_JUSTIFICACION = '"+p.getIdJustificacion()+"'"
                              +" and CPE_JUS_COD_PUESTO = '"+(p.getCodPuesto() != null ? p.getCodPuesto() : "")+"'"
                              +" and CPE_JUS_EXP_EJE = "+(p.getEjercicio() != null ? p.getEjercicio().toString() : "")
                              +" and CPE_JUS_NUMEXP = '"+p.getNumExpediente()+"'";
                if(log.isDebugEnabled()) 
                    log.debug("sql = " + query);
                st = con.createStatement();
                rs = st.executeQuery(query);
                if(rs.next())
                {
                    justif = (CpeJustificacionVO)MeLanbide39MappingUtils.getInstance().map(rs, CpeJustificacionVO.class);
                }
                return justif;
            }
            catch(Exception ex)
            {
                log.error("Se ha producido un error recuperando oferta "+(p != null ? p.getIdOferta() : "(oferta = null)")+" para puesto "+(p != null ? p.getCodPuesto() : "(oferta = null)")+" expediente "+(p != null ? p.getNumExpediente() : "(oferta = null)"), ex);
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
        else
        {
            return null;
        }
    }
    
     public CpeJustificacionVO getJustificacionPorCodigoPuestoOfertaYExpediente(CpeJustificacionVO p, Connection con) throws Exception
    {
        if(p != null)
        {
            Statement st = null;
            ResultSet rs = null;
            CpeJustificacionVO justif = null;
            try
            {
                String query = "select * from "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_JUSTIFICACION, ConstantesMeLanbide39.FICHERO_PROPIEDADES)
                              +" where CPE_JUS_COD_PUESTO = '"+(p.getCodPuesto() != null ? p.getCodPuesto() : "")+"'"
                              +" and CPE_JUS_ID_OFERTA = '"+(p.getIdOferta() != null ? p.getIdOferta() : "")+"'"
                              +" and CPE_JUS_EXP_EJE = "+(p.getEjercicio() != null ? p.getEjercicio().toString() : "")
                              +" and CPE_JUS_NUMEXP = '"+p.getNumExpediente()+"'";
                if(log.isDebugEnabled()) 
                    log.debug("sql = " + query);
                st = con.createStatement();
                rs = st.executeQuery(query);
                if(rs.next())
                {
                    justif = (CpeJustificacionVO)MeLanbide39MappingUtils.getInstance().map(rs, CpeJustificacionVO.class);
                }
                return justif;
            }
            catch(Exception ex)
            {
                log.error("Se ha producido un error recuperando oferta "+(p != null ? p.getIdOferta() : "(oferta = null)")+" para puesto "+(p != null ? p.getCodPuesto() : "(oferta = null)")+" expediente "+(p != null ? p.getNumExpediente() : "(oferta = null)"), ex);
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
        else
        {
            return null;
        }
    }
    
    public void actualizaImporteJusti(int codOrg, String numExp, String eje, String impJustif, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        try
        {
            String query = null;
            query = "SELECT * FROM E_TNU WHERE TNU_NUM = '" + numExp + "' AND TNU_COD = 'IMJUS'";
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                //ya existe
                query = "UPDATE E_TNU SET TNU_VALOR = "+impJustif+ " WHERE TNU_NUM = '" + numExp + "' AND TNU_COD = 'IMJUS'";
            }
            else
            {
                //Es nuevo
                
                query = "INSERT INTO E_TNU (TNU_COD, TNU_EJE, TNU_MUN, TNU_NUM, TNU_VALOR) VALUES "
                        + "('IMPJUS', " + eje + ", " + codOrg + ", '" + numExp + "', " + impJustif + ")";
            }
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            int res = st.executeUpdate(query);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            log.error("Se ha producido un error en actualizaImporteJusti: ", ex);
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
    
    public CpeJustificacionVO guardarCpeJustificacionVO(CpeJustificacionVO justif, Connection con) throws Exception
    {
        Statement st = null;
        try
        {
            String query = null;
            SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide39.FORMATO_FECHA);
            if(justif.getIdJustificacion() == null)
            {
                Integer codJustif = this.getNuevoCodigoJustificacion(justif, con);
                if(codJustif == null || codJustif.equals(""))
                {
                    throw new Exception();
                }
                justif.setIdJustificacion(codJustif);
                //Es un puesto nuevo
                query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_JUSTIFICACION, ConstantesMeLanbide39.FICHERO_PROPIEDADES)
                        + "(CPE_JUS_ID_JUSTIFICACION, CPE_JUS_COD_PUESTO, CPE_JUS_ID_OFERTA, CPE_JUS_EXP_EJE, CPE_JUS_NUMEXP, CPE_JUS_COD_ESTADO, CPE_JUS_FL_VARIOS_TRAB, "
                        + "CPE_JUS_IMP_JUSTIFICADO, CPE_JUS_SALARIOSUB, CPE_JUS_DIETAS, CPE_JUS_GASTOSGES, CPE_JUS_BONIF,"
                        + "CPE_JUS_MINORACION, CPE_JUS_BASCOT_CC, CPE_JUS_COEF_TGSS, CPE_JUS_FOGASA, CPE_JUS_AT,  " //CPE_JUS_EPSV,
                        + "CPE_JUS_DIAS_TRAB, CPE_JUS_DIAS_SEGSOC, CPE_JUS_BASCOT_AT, CPE_JUS_MESES_EXT, CPE_JUS_SALARIO, CPE_JUS_APORTEPSV)"
                        + " values("+justif.getIdJustificacion().toString()
                        + ", "+(justif.getCodPuesto() != null ? "'"+justif.getCodPuesto()+"'" : "null")
                        + ", "+(justif.getIdOferta() != null ? "'"+justif.getIdOferta()+"'" : "null")
                        + ", "+(justif.getEjercicio() != null ? justif.getEjercicio() : "null")
                        + ", "+(justif.getNumExpediente() != null ? "'"+justif.getNumExpediente()+"'" : "null")
                        + ", "+(justif.getCodEstado() != null ? "'"+justif.getCodEstado()+"'" : "null")
                        + ", "+(justif.getFlVariosTrab() != null ? "'"+justif.getFlVariosTrab()+"'" : "null")
                        + ", "+(justif.getImpJustificado() != null ? justif.getImpJustificado().toPlainString() : "null")
                        + ", "+(justif.getSalarioSub()!= null ? justif.getSalarioSub().toPlainString() : "null")
                        + ", "+(justif.getDietasJusti()!= null ? justif.getDietasJusti().toPlainString() : "null")
                        + ", "+(justif.getGastosGestion()!= null ? justif.getGastosGestion().toPlainString() : "null")
                        + ", "+(justif.getBonif()!= null ? justif.getBonif().toPlainString() : "null")
                        + ", "+(justif.getMinoracion()!= null ? justif.getMinoracion().toPlainString() : "null")
                        + ", "+(justif.getBaseCC()!= null ? justif.getBaseCC().toPlainString() : "null")
                        + ", "+(justif.getCoeficienteApli()!= null ? justif.getCoeficienteApli().toPlainString() : "null")
                        + ", "+(justif.getPorcFogasa()!= null ? justif.getPorcFogasa().toPlainString() : "null")
                        + ", "+(justif.getPorcCoeficiente()!= null ? justif.getPorcCoeficiente().toPlainString() : "null")
                        //+ ", "+(justif.getPorcAportacion()!= null ? justif.getPorcAportacion().toPlainString() : "null")
                        + ", " + justif.getDiasTrab() + ", " + justif.getDiasSegSoc()
                        + ", "+(justif.getBaseAT()!= null ? justif.getBaseAT().toPlainString() : "null")
                        + ", "+(justif.getMesesExt()!= null ? justif.getMesesExt().toPlainString() : "null")
                        + ", "+(justif.getSalario()!= null ? justif.getSalario().toPlainString() : "null")
                        + ", "+(justif.getAportEpsv()!= null ? justif.getAportEpsv().toPlainString() : "null")
                        + ")";
            }
            else
            {
                //Es un puesto que ya existe
                query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_JUSTIFICACION, ConstantesMeLanbide39.FICHERO_PROPIEDADES)
                        + " set"
                        + " CPE_JUS_COD_ESTADO = "+(justif.getCodEstado() != null ? "'"+justif.getCodEstado()+"'" : "null")+", "
                        + " CPE_JUS_FL_VARIOS_TRAB = "+(justif.getFlVariosTrab() != null ? "'"+justif.getFlVariosTrab()+"'" : "null")+", "
                        + " CPE_JUS_IMP_JUSTIFICADO = "+(justif.getImpJustificado() != null ? justif.getImpJustificado().toPlainString() : "null")+", "
                        + " CPE_JUS_SALARIOSUB = "+(justif.getSalarioSub() != null ? justif.getSalarioSub().toPlainString() : "null")+", "
                        + " CPE_JUS_DIETAS = "+(justif.getDietasJusti() != null ? justif.getDietasJusti().toPlainString() : "null")+", "
                        + " CPE_JUS_GASTOSGES = "+(justif.getGastosGestion() != null ? justif.getGastosGestion().toPlainString() : "null")+", "
                        + " CPE_JUS_BONIF = "+(justif.getBonif() != null ? justif.getBonif().toPlainString() : "null")+", "
                        + " CPE_JUS_MINORACION = "+(justif.getMinoracion()!= null ? justif.getMinoracion().toPlainString() : "null")+", "
                        + " CPE_JUS_BASCOT_CC = "+(justif.getBaseCC() != null ? justif.getBaseCC().toPlainString() : "null")+", "
                        + " CPE_JUS_COEF_TGSS = "+(justif.getCoeficienteApli() != null ? justif.getCoeficienteApli().toPlainString() : "null")+", "
                        + " CPE_JUS_FOGASA = "+(justif.getPorcFogasa() != null ? justif.getPorcFogasa().toPlainString() : "null")+", "
                        + " CPE_JUS_AT = "+(justif.getPorcCoeficiente() != null ? justif.getPorcCoeficiente().toPlainString() : "null")+", "
                        //+ " CPE_JUS_EPSV = "+(justif.getPorcAportacion() != null ? justif.getPorcAportacion().toPlainString() : "null")+", "
                        + " CPE_JUS_DIAS_TRAB = "+justif.getDiasTrab() +", "
                        + " CPE_JUS_DIAS_SEGSOC = "+justif.getDiasSegSoc()
                        + ", CPE_JUS_BASCOT_AT = " + justif.getBaseAT()
                        + ", CPE_JUS_MESES_EXT = " + justif.getMesesExt()
                        + ", CPE_JUS_SALARIO = " + justif.getSalario()
                        + ", CPE_JUS_APORTEPSV = " + justif.getAportEpsv()
                        + " where CPE_JUS_ID_JUSTIFICACION = "+justif.getIdJustificacion().toString()
                        + " and CPE_JUS_COD_PUESTO = '"+justif.getCodPuesto()+"'"
                        + " and CPE_JUS_EXP_EJE = "+justif.getEjercicio().toString()
                        + " and CPE_JUS_NUMEXP = '"+justif.getNumExpediente()+"'";
            }
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            int res = st.executeUpdate(query);
            if(res > 0)
            {
                return justif;
            }
            else
            {
                return null;
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            log.error("Se ha producido un error guardando los datos de justificacion "+(justif != null ? justif.getIdJustificacion() : "(justif = null)")+" para el expediente "+(justif != null ? justif.getNumExpediente() : "(justif = null)"), ex);
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

public String guardarHistorico(String numExp, String codTramite, Connection con) throws Exception
    {
         CallableStatement st = null;
        ResultSet rs = null;
        try
        {
            String query = null;
            query = "call CPE_HISTORICO (?,?)";
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.prepareCall(query);
            st.setString(1, numExp); 
            st.setString(2, codTramite);
           // st.registerOutParameter(2, java.sql.Types.VARCHAR);
            int i = st.executeUpdate();
            //String result = st.getString(1);
            return "";
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error guardando histórico", ex);
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

public String borrarHistorico(String numExp, String codTramite, Connection con) throws Exception
    {
         CallableStatement st = null;
        ResultSet rs = null;
        try
        {
            String query = null;
            query = "call CPE_BORRADO_HISTORICO (?,?)";
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.prepareCall(query);
            st.setString(1, numExp); 
            st.setString(2, codTramite);
           // st.registerOutParameter(2, java.sql.Types.VARCHAR);
            int i = st.executeUpdate();
            //String result = st.getString(1);
            return "";
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error borrando histórico", ex);
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
    public String obtenerDiasTrabajados_old(String numExp, String nif, Connection con) throws Exception
    {
        CallableStatement st = null;
        String result2 = "";
        try
        {
            String sql = "call CPE_DIAS_SUBVENCIONABLES (?,?,?)";
            if(log.isDebugEnabled()) 
                log.debug("sql = " + sql);
            st = con.prepareCall(sql);
            st.setString(1, numExp); 
            st.setString(2, nif);
            st.registerOutParameter(3,  java.sql.Types.VARCHAR);
           // st.registerOutParameter(2, java.sql.Types.VARCHAR);
            int i = st.executeUpdate();
            result2 = st.getString(3);
            //String result = st.getString(1);
            
        }catch(Exception ex){
            log.debug("Error en funcion obtenerDiasTrabajados. " + ex);
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
        return result2;
    }
    
    public String obtenerDiasTrabajados(String numExp, String codPuesto, Integer idOferta, Connection con) throws Exception
    {
        CallableStatement st = null;
        String result2 = "";
        try
        {
            String sql = "call CPE_DIAS_SUBVENCIONABLES (?,?,?,?)";
            if(log.isDebugEnabled()) 
                log.debug("sql = " + sql);
            st = con.prepareCall(sql);
            st.setString(1, numExp); 
            st.setString(2, codPuesto);
            st.setString(3, idOferta.toString());
            st.registerOutParameter(4,  java.sql.Types.VARCHAR);
           // st.registerOutParameter(2, java.sql.Types.VARCHAR);
            int i = st.executeUpdate();
            result2 = st.getString(4);
            //String result = st.getString(1);
            
        }catch(Exception ex){
            log.debug("Error en funcion obtenerDiasTrabajados. " + ex);
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
        return result2;
    }

    public List<FilaResumenVO> getListaResumenPorExpediente(String numExpediente, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        int anoExpediente = MeLanbide39Utils.getEjercicioDeExpediente(numExpediente);
        List<FilaResumenVO> resumen = new ArrayList<FilaResumenVO>();
        FilaResumenVO res = new FilaResumenVO();
        try
        {
            String query = null;
                //Leire, se comentan las línas en las que se realicen cálculos con los días trabajados
                query = "select"
                         + " CPE_JUS_COD_PUESTO, CPE_JUS_ID_OFERTA, "
                        + " CPE_OFE_NOMBRE || ' ' || CPE_OFE_APE1 || ' '||coalesce(CPE_OFE_APE2,'') NOMBRE_APELLIDOS"
                        + " ,CPE_OFE_COD_NIV_FORM"
                        + " ,CPE_OFE_NIF"
                        + " ,CASE WHEN CPE_PTO_PAI_COD_1 IS NOT NULL THEN CPE_PTO_PAI_COD_1||'-'||pai1.PAI_NOM ELSE ' ' END PAIS_SOLICITADO1"
                        + " ,CASE WHEN CPE_PTO_PAI_COD_2 IS NOT NULL THEN CPE_PTO_PAI_COD_2||'-'||pai2.PAI_NOM ELSE ' ' END PAIS_SOLICITADO2"
                        + " ,CASE WHEN CPE_PTO_PAI_COD_3 IS NOT NULL THEN CPE_PTO_PAI_COD_3||'-'||pai3.PAI_NOM ELSE ' ' END PAIS_SOLICITADO3"
                        + " ,CASE WHEN CPE_OFE_PAI_COD_1 IS NOT NULL THEN CPE_OFE_PAI_COD_1||'-'||pai4.PAI_NOM ELSE ' ' END PAIS_FINAL1"
                        + " ,CASE WHEN CPE_OFE_PAI_COD_2 IS NOT NULL THEN CPE_OFE_PAI_COD_2||'-'||pai5.PAI_NOM ELSE ' ' END PAIS_FINAL2"
                        + " ,CASE WHEN CPE_OFE_PAI_COD_3 IS NOT NULL THEN CPE_OFE_PAI_COD_3||'-'||pai6.PAI_NOM ELSE ' ' END PAIS_FINAL3"
                        + " ,CPE_JUS_MESES_EXT"
                        + " ,CPE_OFE_FEC_INI"
                        +" ,CASE WHEN CPE_OFE_FEC_FIN > TO_DATE('31/10/"+(anoExpediente+1)+"', 'dd/MM/yyyy') THEN TO_DATE('31/10/"+(anoExpediente+1)+"', 'dd/MM/yyyy') ELSE CPE_OFE_FEC_FIN END CPE_OFE_FEC_FIN"
                        //+ " ,CASE WHEN (CASE WHEN CPE_OFE_FEC_FIN > TO_DATE('31/10/"+(anoExpediente+1)+"', 'dd/MM/yyyy') THEN TO_DATE('31/10/"+(anoExpediente+1)+"', 'dd/MM/yyyy') ELSE CPE_OFE_FEC_FIN END-CPE_OFE_FEC_INI)+1 >= 360 THEN 360 ELSE (CASE WHEN CPE_OFE_FEC_FIN > TO_DATE('31/10/"+(anoExpediente+1)+"', 'dd/MM/yyyy') THEN TO_DATE('31/10/"+(anoExpediente+1)+"', 'dd/MM/yyyy') ELSE CPE_OFE_FEC_FIN END-CPE_OFE_FEC_INI)+1 END DIAS_TRABAJADOS"
                        + " ,CPE_PTO_SALARIO_ANEX_TOT SALARIO_ANEXO"
                        //+ " ,ROUND(CPE_PTO_SALARIO_ANEX_TOT*case when ((CASE WHEN CPE_OFE_FEC_FIN > TO_DATE('31/10/"+(anoExpediente+1)+"', 'dd/MM/yyyy') THEN TO_DATE('31/10/"+(anoExpediente+1)+"', 'dd/MM/yyyy') ELSE CPE_OFE_FEC_FIN END-CPE_OFE_FEC_INI)+1)>=360 then 360 else ((CASE WHEN CPE_OFE_FEC_FIN > TO_DATE('31/10/"+(anoExpediente+1)+"', 'dd/MM/yyyy') THEN TO_DATE('31/10/"+(anoExpediente+1)+"', 'dd/MM/yyyy') ELSE CPE_OFE_FEC_FIN END-CPE_OFE_FEC_INI)+1) end/360,2) MAX_SUBV_SALARIO_EN_PERIODO"
                        + " ,CPE_JUS_MINORACION MINORACION, CPE_JUS_DIAS_TRAB, CPE_JUS_DIAS_SEGSOC "
                        //+ " ,ROUND(CPE_PTO_SALARIO_ANEX_TOT*case when ((CASE WHEN CPE_OFE_FEC_FIN > TO_DATE('31/10/"+(anoExpediente+1)+"', 'dd/MM/yyyy') THEN TO_DATE('31/10/"+(anoExpediente+1)+"', 'dd/MM/yyyy') ELSE CPE_OFE_FEC_FIN END-CPE_OFE_FEC_INI)+1)>=360 then 360 else ((CASE WHEN CPE_OFE_FEC_FIN > TO_DATE('31/10/"+(anoExpediente+1)+"', 'dd/MM/yyyy') THEN TO_DATE('31/10/"+(anoExpediente+1)+"', 'dd/MM/yyyy') ELSE CPE_OFE_FEC_FIN END-CPE_OFE_FEC_INI)+1) end/360,2)-nvl(cpe_jus_minoracion,0) DEVENGADO_SIN_DIETAS_SUBV"
                        //------------------COSTE ANUAL -----------------------
                        + " ,CPE_JUS_BASCOT_CC BC_CC_EN_PERI, CPE_OFE_FEC_BAJA "
                        + " ,CPE_JUS_COEF_TGSS COEFICIENTE_TGSS"
                        + " ,CPE_JUS_BASCOT_CC *CPE_JUS_COEF_TGSS/100 SS_EMPRESA"
                        + " ,CPE_JUS_BASCOT_AT  BC_AT "
                        + " ,CPE_JUS_FOGASA COEFICIENTE_FOGASA"
                        + " ,CPE_JUS_AT COEFICIENTE_AT"
                        + " , CPE_JUS_BASCOT_AT *(NVL(CPE_JUS_FOGASA,0)+NVL(CPE_JUS_AT,0))/100 FOGASA_AT_EMPRESA "
                        + " , CPE_JUS_EPSV PORC_EPSV"  
                        //antes se calculaba importe epsv con % ahora se mete manualmente
                        + " , CPE_JUS_APORTEPSV  APORTACIONES_EPSV_EMPRESA "
                        + " , (CPE_JUS_BASCOT_CC *CPE_JUS_COEF_TGSS/100)+(CPE_JUS_BASCOT_at *(NVL(CPE_JUS_FOGASA,0)+NVL(CPE_JUS_AT,0))/100)+NVL(CPE_JUS_APORTEPSV,0) TOTAL_EMPRESA "
                        //REDONDEAR DECIMALES DE CADA CÁLCULO PARA QUE COINCIDA CON DATOS DE PANTALLA RESUMEN
                        //+ " , (CPE_JUS_SALARIO )+(CPE_JUS_BASCOT_CC *CPE_JUS_COEF_TGSS/100)+(CPE_JUS_BASCOT_at *(NVL(CPE_JUS_FOGASA,0)+NVL(CPE_JUS_AT,0))/100)+NVL(CPE_JUS_APORTEPSV,0) TOTAL_PERIODO "
                        + " ,(CPE_JUS_SALARIO )+round((CPE_JUS_BASCOT_CC *CPE_JUS_COEF_TGSS/100),2)+round((CPE_JUS_BASCOT_AT *(NVL(CPE_JUS_FOGASA,0)+NVL(CPE_JUS_AT,0))/100),2)+NVL(CPE_JUS_APORTEPSV,0) TOTAL_PERIODO "
                                               
                       // + ", "
                        //------------------MAXIMOS SUBVENCIONABLES ----------------------
                        + " ,(CPE_PTO_SALARIO_ANEX_TOT - CPE_JUS_MINORACION) SUBV_MINORADA_SALAR_SSOCIAL"
                        //+ " ,ROUND(CPE_PTO_SALARIO_ANEX_TOT*case when ((CASE WHEN CPE_OFE_FEC_FIN > TO_DATE('31/10/"+(anoExpediente+1)+"', 'dd/MM/yyyy') THEN TO_DATE('31/10/"+(anoExpediente+1)+"', 'dd/MM/yyyy') ELSE CPE_OFE_FEC_FIN END-CPE_OFE_FEC_INI)+1)>=360 then 360 else ((CASE WHEN CPE_OFE_FEC_FIN > TO_DATE('31/10/"+(anoExpediente+1)+"', 'dd/MM/yyyy') THEN TO_DATE('31/10/"+(anoExpediente+1)+"', 'dd/MM/yyyy') ELSE CPE_OFE_FEC_FIN END-CPE_OFE_FEC_INI)+1) end/360,2)  MAX_SUBV_PERIODO"
                       
                        //CAMBIAMOS PARA COGER SOLO DOS DECIMALES DE CADA IMPORTE A SUMAR (SINONNO COINCIDE CON PANTALLA DE DATOS JUSTIFICADOS DE LA PERSONA)
                        +", ROUND(NVL(CPE_JUS_SALARIO,0 ),2)+ROUND(NVL(CPE_JUS_BASCOT_CC *CPE_JUS_COEF_TGSS/100,0),2)+ROUND(NVL(CPE_JUS_BASCOT_AT *(NVL(CPE_JUS_FOGASA,0)+NVL(CPE_JUS_AT,0))/100,0),2)+NVL(CPE_JUS_APORTEPSV,0) SALARIO_PERIODO "
                        //+ " , NVL(CPE_JUS_SALARIO,0 )+NVL(CPE_JUS_BASCOT_CC *CPE_JUS_COEF_TGSS/100,0)+NVL(CPE_JUS_BASCOT_at *(NVL(CPE_JUS_FOGASA,0)+NVL(CPE_JUS_AT,0))/100,0)+NVL(CPE_JUS_APORTEPSV,0) SALARIO_PERIODO "
                        
                        /*+ " ,CASE WHEN ROUND(CPE_PTO_SALARIO_ANEX_TOT*case when ((CASE WHEN CPE_OFE_FEC_FIN > TO_DATE('31/10/"+(anoExpediente+1)+"', 'dd/MM/yyyy') THEN TO_DATE('31/10/"+(anoExpediente+1)+"', 'dd/MM/yyyy') ELSE CPE_OFE_FEC_FIN END-CPE_OFE_FEC_INI)+1)>=360 then 360 else ((CASE WHEN CPE_OFE_FEC_FIN > TO_DATE('31/10/"+(anoExpediente+1)+"', 'dd/MM/yyyy') THEN TO_DATE('31/10/"+(anoExpediente+1)+"', 'dd/MM/yyyy') ELSE CPE_OFE_FEC_FIN END-CPE_OFE_FEC_INI)+1) end/360,2)>NVL(CPE_JUS_BASCOT_CC,0 )+NVL(CPE_JUS_BASCOT_CC *CPE_JUS_COEF_TGSS/100,0)+NVL(CPE_JUS_BASCOT_CC *(NVL(CPE_JUS_FOGASA,0)+NVL(CPE_JUS_AT,0))/100,0)+NVL(CPE_JUS_APORTEPSV)"
                        + " THEN NVL(CPE_JUS_BASCOT_CC,0 )+NVL(CPE_JUS_BASCOT_CC *CPE_JUS_COEF_TGSS/100,0)+NVL(CPE_JUS_BASCOT_CC *(NVL(CPE_JUS_FOGASA,0)+NVL(CPE_JUS_AT,0))/100,0)+NVL(CPE_JUS_EPSV*CPE_JUS_BASCOT_CC /100,0)"
                        + " ELSE ROUND(CPE_PTO_SALARIO_ANEX_TOT*case when ((CASE WHEN CPE_OFE_FEC_FIN > TO_DATE('31/10/"+(anoExpediente+1)+"', 'dd/MM/yyyy') THEN TO_DATE('31/10/"+(anoExpediente+1)+"', 'dd/MM/yyyy') ELSE CPE_OFE_FEC_FIN END-CPE_OFE_FEC_INI)+1)>=360 then 360 else ((CASE WHEN CPE_OFE_FEC_FIN > TO_DATE('31/10/"+(anoExpediente+1)+"', 'dd/MM/yyyy') THEN TO_DATE('31/10/"+(anoExpediente+1)+"', 'dd/MM/yyyy') ELSE CPE_OFE_FEC_FIN END-CPE_OFE_FEC_INI)+1) end/360,2)"
                        + "       END SALARIO_JUS_SUBVENCION"*/
                        + " , CPE_JUS_DIETAS DIETAS_ABONADAS_PERIODO"
                        + " , CPE_PTO_DIETAS_MANUT DIETAS_CONCEDIDAS_PERIODO"
                        + " , CASE WHEN CPE_JUS_DIETAS >=CPE_PTO_DIETAS_MANUT THEN CPE_PTO_DIETAS_MANUT"
                        + " ELSE CPE_JUS_DIETAS END MAX_SUB_DIETAS"
                        + " ,CPE_JUS_GASTOSGES VIS_SEG_ABONADO"
                        + " ,CPE_PTO_TRAM_SEGUROS VIS_SEG_CONCEDIDO"
                        + " , CASE WHEN CPE_JUS_GASTOSGES>=CPE_PTO_TRAM_SEGUROS THEN CPE_PTO_TRAM_SEGUROS ELSE CPE_JUS_GASTOSGES END VIS_SEG_SUBV"
                        + " ,CPE_JUS_BONIF BONIFICACIONES, CPE_JUS_MESES_EXT, CPE_JUS_BASCOT_AT, CPE_JUS_SALARIO "
                        /*+ " ,NVL(CASE WHEN ROUND(CPE_PTO_SALARIO_ANEX_TOT*case when((CASE WHEN CPE_OFE_FEC_FIN > TO_DATE('31/10/"+(anoExpediente+1)+"', 'dd/MM/yyyy') THEN TO_DATE('31/10/"+(anoExpediente+1)+"', 'dd/MM/yyyy') ELSE CPE_OFE_FEC_FIN END-CPE_OFE_FEC_INI)+1)>=360 then 360 else ((CASE WHEN CPE_OFE_FEC_FIN > TO_DATE('31/10/"+(anoExpediente+1)+"', 'dd/MM/yyyy') THEN TO_DATE('31/10/"+(anoExpediente+1)+"', 'dd/MM/yyyy') ELSE CPE_OFE_FEC_FIN END-CPE_OFE_FEC_INI)+1) end/360,2)  >= (CPE_JUS_BASCOT_CC )+(CPE_JUS_BASCOT_CC *CPE_JUS_COEF_TGSS/100)+(CPE_JUS_BASCOT_CC *(NVL(CPE_JUS_FOGASA,0)+NVL(CPE_JUS_AT,0))/100)+(CPE_JUS_EPSV*CPE_JUS_BASCOT_CC /100)"
                        + "       THEN (CPE_JUS_BASCOT_CC )+(CPE_JUS_BASCOT_CC *CPE_JUS_COEF_TGSS/100)+(CPE_JUS_BASCOT_CC *(NVL(CPE_JUS_FOGASA,0)+NVL(CPE_JUS_AT,0))/100)+(CPE_JUS_EPSV*CPE_JUS_BASCOT_CC /100)"
                        + "       ELSE ROUND(CPE_PTO_SALARIO_ANEX_TOT*CASE WHEN((CASE WHEN CPE_OFE_FEC_FIN > TO_DATE('31/10/"+(anoExpediente+1)+"', 'dd/MM/yyyy') THEN TO_DATE('31/10/"+(anoExpediente+1)+"', 'dd/MM/yyyy') ELSE CPE_OFE_FEC_FIN END-CPE_OFE_FEC_INI)+1)>=360 then 360 else ((CASE WHEN CPE_OFE_FEC_FIN > TO_DATE('31/10/"+(anoExpediente+1)+"', 'dd/MM/yyyy') THEN TO_DATE('31/10/"+(anoExpediente+1)+"', 'dd/MM/yyyy') ELSE CPE_OFE_FEC_FIN END-CPE_OFE_FEC_INI)+1) end/360,2)"
                        + " END ,0)"
                        + " +"
                        + " NVL(CASE WHEN CPE_JUS_DIETAS >=CPE_PTO_DIETAS_MANUT THEN CPE_PTO_DIETAS_MANUT"
                        + " ELSE CPE_JUS_DIETAS "
                        + " END ,0)"
                        + " +     "
                        + " NVL(CASE WHEN CPE_JUS_GASTOSGES>=CPE_PTO_TRAM_SEGUROS THEN CPE_PTO_TRAM_SEGUROS ELSE CPE_JUS_GASTOSGES END,0) "
                        + " -"
                        + " NVL(CPE_JUS_BONIF,0) coste_subvencionable"*/
                        
                        + " from "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_PUESTO, ConstantesMeLanbide39.FICHERO_PROPIEDADES) +" p "
                        + " inner join "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_OFERTA, ConstantesMeLanbide39.FICHERO_PROPIEDADES) +" o on P.Cpe_Pto_Cod_Puesto=O.Cpe_Ofe_Cod_Puesto and P.Cpe_Pto_Numexp =o.CPE_OFE_NUMEXP"
                        + " inner  join "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_JUSTIFICACION, ConstantesMeLanbide39.FICHERO_PROPIEDADES) +" j on P.Cpe_Pto_Cod_Puesto=J.Cpe_Jus_Cod_Puesto and P.Cpe_Pto_Numexp =j.CPE_jus_NUMEXP and j.CPE_JUS_id_OFERTA=O.Cpe_Ofe_id_Oferta"
                        + " inner join " + GlobalNames.ESQUEMA_GENERICO + "T_PAI pai1 on pai1.PAI_COD=CPE_PTO_PAI_COD_1"
                        + " LEFT join " + GlobalNames.ESQUEMA_GENERICO + "T_PAI pai2 on pai2.PAI_COD=CPE_PTO_PAI_COD_2"
                        + " LEFT join " + GlobalNames.ESQUEMA_GENERICO + "T_PAI pai3 on pai3.PAI_COD=CPE_PTO_PAI_COD_3"
                        + " inner join " + GlobalNames.ESQUEMA_GENERICO + "T_PAI pai4 on pai4.PAI_COD=CPE_OFE_PAI_COD_1"
                        + " LEFT join " + GlobalNames.ESQUEMA_GENERICO + "T_PAI pai5 on pai5.PAI_COD=CPE_OFE_PAI_COD_2"
                        + " LEFT join " + GlobalNames.ESQUEMA_GENERICO + "T_PAI pai6 on pai6.PAI_COD=CPE_OFE_PAI_COD_3"
                        + " where CPE_OFE_NUMEXP = '"+numExpediente+"'"
                        +" and CPE_PTO_COD_RESULT = '"+ConstantesMeLanbide39.CODIGO_RESULTADO_CONCEDIDO+"'"
                        + " and CPE_OFE_NIF is not null";
            
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            
            DecimalFormat df = new DecimalFormat("#.##");
            Double salarioAnexo = null;
            Double salario = null;
            Double minoracion = null;
            Double bcPeriodo = null;
            String bcAT = "";
            Double coeficienteTgss = null;
            Double coeficienteFog = null;
            Double coeficienteAt = null;
            //Double porcEpsv = null;
            Double aportacionEPSV =null;
            Double dietasAbon = null;
            Double dietasConce = null;
            Double vigSegAbon = null;
            Double vigSegConce = null;
            Double bonif = null;
            while(rs.next())
            {         
                int dias = 0;
                int diasSeg = 0;
                int diasC = 0;
                res = new FilaResumenVO();
                //resumen.add();
                res = (FilaResumenVO)MeLanbide39MappingUtils.getInstance().map(rs, FilaResumenVO.class);
                
                //Leire, hacemos los cálculos relacionados con el número de días
                if(rs.getString("SALARIO_ANEXO") != null)
                    salarioAnexo = rs.getDouble("SALARIO_ANEXO");
                else 
                    salarioAnexo = 0.0;
                if(rs.getString("CPE_JUS_SALARIO") != null)
                    salario = rs.getDouble("CPE_JUS_SALARIO");
                else 
                    salario = 0.0;
                res.setSalario(salario.toString().replaceAll("\\.", ","));
                if(rs.getString("MINORACION") != null)
                    minoracion = rs.getDouble("MINORACION");
                else 
                    minoracion = 0.0;
                if(rs.getString("BC_CC_EN_PERI") != null)
                    bcPeriodo = rs.getDouble("BC_CC_EN_PERI");
                else 
                    bcPeriodo = 0.0;
                if(rs.getString("COEFICIENTE_TGSS") != null)
                    coeficienteTgss = rs.getDouble("COEFICIENTE_TGSS");
                else 
                    coeficienteTgss = 0.0;
                if(rs.getString("COEFICIENTE_FOGASA") != null)
                    coeficienteFog = rs.getDouble("COEFICIENTE_FOGASA");
                else 
                    coeficienteFog = 0.0;
                if(rs.getString("COEFICIENTE_AT") != null)
                    coeficienteAt = rs.getDouble("COEFICIENTE_AT");
                else 
                    coeficienteAt = 0.0;
                /*if(rs.getString("PORC_EPSV") != null)
                    porcEpsv = rs.getDouble("PORC_EPSV");
                else 
                    porcEpsv = 0.0;*/
                if (rs.getString("APORTACIONES_EPSV_EMPRESA")!= null)
                    aportacionEPSV = rs.getDouble("APORTACIONES_EPSV_EMPRESA");
                else
                    aportacionEPSV = 0.0;
                if(rs.getString("DIETAS_ABONADAS_PERIODO") != null)
                    dietasAbon = rs.getDouble("DIETAS_ABONADAS_PERIODO");
                else 
                    dietasAbon = 0.0;
                if(rs.getString("DIETAS_CONCEDIDAS_PERIODO") != null)
                    dietasConce = rs.getDouble("DIETAS_CONCEDIDAS_PERIODO");
                else 
                    dietasConce = 0.0;
                if(rs.getString("VIS_SEG_ABONADO") != null)
                    vigSegAbon = rs.getDouble("VIS_SEG_ABONADO");
                else 
                    vigSegAbon = 0.0;
                if(rs.getString("VIS_SEG_CONCEDIDO") != null)
                    vigSegConce = rs.getDouble("VIS_SEG_CONCEDIDO");
                else 
                    vigSegConce = 0.0;
                if(rs.getString("BONIFICACIONES") != null)
                    bonif = rs.getDouble("BONIFICACIONES");
                else 
                    bonif = 0.0;
                if(rs.getString("CPE_JUS_DIAS_TRAB") != null)
                    dias = rs.getInt("CPE_JUS_DIAS_TRAB");
                if(rs.getString("CPE_JUS_DIAS_SEGSOC") != null)
                    diasSeg = rs.getInt("CPE_JUS_DIAS_SEGSOC");
                
                if(rs.getString("CPE_JUS_BASCOT_AT") != null)
                    bcAT  = rs.getString("CPE_JUS_BASCOT_AT");
                else
                    bcAT = "0";
                
                String rdo="";
                if(rs.getString("CPE_OFE_NIF")!=null){
                     //rdo = obtenerDiasTrabajados(numExpediente, rs.getString("CPE_OFE_NIF"), con);
                     rdo = obtenerDiasTrabajados(numExpediente, rs.getString("CPE_JUS_COD_PUESTO"),rs.getInt("CPE_JUS_ID_OFERTA"), con);
                }
                
                if(rdo != null && !rdo.equals(""))
                    diasC = Integer.parseInt(rdo);
                else
                    diasC = 0;
                if(dias == 0)
                    dias = diasC;
                if(diasSeg == 0)
                    diasSeg = diasC;
                if(rs.getString("CPE_JUS_MESES_EXT")== null || rs.getString("CPE_JUS_MESES_EXT").equals(""))
                    res.setMesesExt("0");
                else
                    res.setMesesExt(rs.getString("CPE_JUS_MESES_EXT"));
                res.setTotalDias(dias);
                res.setTotalDiasSeg(diasSeg);
                
                Double dato = (salarioAnexo * dias /360) - minoracion;
                res.setMaximoPeriodoSubv(df.format(dato).toString());
                dato = (salarioAnexo * dias /360);
                res.setMaximoSubv(df.format(dato).toString());
                //MAX_SUBV_SALARIO_EN_PERIODO
                dato = (salarioAnexo * dias /360) - minoracion;
                if(dato <0) dato = 0.0;
                res.setDevengado(df.format(dato).toString());
                
                dato = salarioAnexo * dias /360;
                Double dato2 = bcPeriodo + (bcPeriodo*coeficienteTgss/100)+ (bcPeriodo*coeficienteFog*coeficienteAt/100)+ /*(porcEpsv*bcPeriodo/100)*/aportacionEPSV;
                /*if(dato > dato2)
                    dato = dato2;*/
                dato = bcPeriodo*coeficienteTgss/100;
                res.setSsEmpresa(df.format(dato).toString());
                res.setBcAT(bcAT);
                
                
                
                dato = (salarioAnexo * dias /360)-minoracion;
                dato2 = salario+ (bcPeriodo*coeficienteTgss/100) + (Double.parseDouble(bcAT)*(coeficienteFog + coeficienteAt)/100)+ /*(porcEpsv*bcPeriodo/100)*/aportacionEPSV;
                
                if(dato >= dato2)
                    dato = dato2;
                if(dietasAbon >= dietasConce)
                    dato += dietasConce;
                else 
                    dato += dietasAbon;
                
                if(vigSegAbon>= vigSegConce)
                    dato += vigSegConce;
                else
                    dato += vigSegAbon;
                
                dato = dato - bonif;
                if(dato < 0) dato = 0.0;
                //res.setCostePeriodoSubv(rdo);
                res.setCosteSubvecionable(df.format(dato).toString());  
                
                resumen.add(res);
            }
            return resumen;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando el resumen para el expediente "+numExpediente, ex);
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



    public List<FilaInformeResumenPuestosContratadosVO> getDatosInformeResumenPuestosContratados(Integer ejercicio, Connection con) throws Exception
    {
        List<FilaInformeResumenPuestosContratadosVO> retList = new ArrayList<FilaInformeResumenPuestosContratadosVO>();
        
        ResultSet rs = null;
        Statement st = null;
        try
        {
            String tablaPuesto = ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_PUESTO, ConstantesMeLanbide39.FICHERO_PROPIEDADES);
            String tablaOferta = ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_OFERTA, ConstantesMeLanbide39.FICHERO_PROPIEDADES);
            String tablaEdesval = ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_E_DES_VAL, ConstantesMeLanbide39.FICHERO_PROPIEDADES);
            String tablaTitulaciones =ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_GEN_TITULACIONES, ConstantesMeLanbide39.FICHERO_PROPIEDADES);
            //"GEN_TITULACIONES";
            String tablaNivelesFormativos =ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_GEN_NIVELES_FORMATIVOS, ConstantesMeLanbide39.FICHERO_PROPIEDADES);
            //"GEN_NIVELES_FORMATIVOS";
            String tablaPais = ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_PAISES, ConstantesMeLanbide39.FICHERO_PROPIEDADES);
            String tablaTexto = ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_TIPO_DATO_TEXTO, ConstantesMeLanbide39.FICHERO_PROPIEDADES);
            String tablaNumero = ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_TIPO_DATO_NUMERICO, ConstantesMeLanbide39.FICHERO_PROPIEDADES);
            String tablaExpediente = ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_E_EXP, ConstantesMeLanbide39.FICHERO_PROPIEDADES);
            String tablaProvincia = ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_PROVINCIAS, ConstantesMeLanbide39.FICHERO_PROPIEDADES);
            String tablaDesplegable = ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_TIPO_DATO_DESPLEGABLE, ConstantesMeLanbide39.FICHERO_PROPIEDADES);
            
            String codigoTit = ConfigurationParameter.getParameter(ConstantesMeLanbide39.CAMPO_SUPL_TITULACION, ConstantesMeLanbide39.FICHERO_PROPIEDADES);
            String codigoIdi = ConfigurationParameter.getParameter(ConstantesMeLanbide39.CAMPO_SUPL_IDIOMA, ConstantesMeLanbide39.FICHERO_PROPIEDADES);
            String codigoNidi = ConfigurationParameter.getParameter(ConstantesMeLanbide39.CAMPO_SUPL_NIVEL_IDIOMA, ConstantesMeLanbide39.FICHERO_PROPIEDADES);
            String codigoEmpresa = ConfigurationParameter.getParameter(ConstantesMeLanbide39.CAMPO_SUPL_EMPRESA, ConstantesMeLanbide39.FICHERO_PROPIEDADES);
            String codigoRes = ConfigurationParameter.getParameter(ConstantesMeLanbide39.CAMPO_SUPL_RESULTADO, ConstantesMeLanbide39.FICHERO_PROPIEDADES);
            
            String codigoPSolic = ConfigurationParameter.getParameter(ConstantesMeLanbide39.CAMPO_SUPL_PUESTOS_SOLICITADOS, ConstantesMeLanbide39.FICHERO_PROPIEDADES);
            String codigoPDen = ConfigurationParameter.getParameter(ConstantesMeLanbide39.CAMPO_SUPL_PUESTOS_DENEGADOS, ConstantesMeLanbide39.FICHERO_PROPIEDADES);
            String codigoPRen = ConfigurationParameter.getParameter(ConstantesMeLanbide39.CAMPO_SUPL_PUESTOS_RENUNCIADOS, ConstantesMeLanbide39.FICHERO_PROPIEDADES);
            String codigoPCont = ConfigurationParameter.getParameter(ConstantesMeLanbide39.CAMPO_SUPL_PUESTOS_CONTRATADOS, ConstantesMeLanbide39.FICHERO_PROPIEDADES);
            String codigoNumPRes = ConfigurationParameter.getParameter(ConstantesMeLanbide39.CAMPO_SUPL_RES_NUM_PUESTO, ConstantesMeLanbide39.FICHERO_PROPIEDADES);
            String codigoCren = ConfigurationParameter.getParameter(ConstantesMeLanbide39.CAMPO_SUPL_CAUSA_RENUNCIA, ConstantesMeLanbide39.FICHERO_PROPIEDADES);
            String codigoMotivo = ConfigurationParameter.getParameter(ConstantesMeLanbide39.CAMPO_SUPL_MOTIVO, ConstantesMeLanbide39.FICHERO_PROPIEDADES);
            String codigoTerHis = ConfigurationParameter.getParameter(ConstantesMeLanbide39.CAMPO_SUPL_TER_HIS, ConstantesMeLanbide39.FICHERO_PROPIEDADES);
            
            
            String query = "select"
                          +" exp.exp_num as NUM_EXPEDIENTE,\r\n"
                          +" texto1.TXT_VALOR as EMPRESA,\r\n"
                          +" texto2.nom_prov as TER_HIS,\r\n"
                          +" CASE WHEN CPE_OFE_ID_OFERTA=1 THEN 1\r\n"
                          +" ELSE 0 END  as PUESTOS_SOLICITADOS,\r\n"
                          
                          +" CASE WHEN CPE_OFE_ID_OFERTA=1 THEN CPE_PTO_COD_PUESTO ELSE null END as cod_puesto,\r\n"
                          +" CASE WHEN CPE_OFE_ID_OFERTA=1 THEN CPE_PTO_DESC_PUESTO  ELSE null END as desc_puesto,\r\n"
                                              
                          +" CASE WHEN CPE_OFE_ID_OFERTA=1 THEN p1.PAI_NOM ELSE null END as PAIS_SOL_1,\r\n"
                          +" CASE WHEN CPE_OFE_ID_OFERTA=1 THEN p2.PAI_NOM ELSE null END as PAIS_SOL_2,\r\n"
                          +" CASE WHEN CPE_OFE_ID_OFERTA=1 THEN p3.PAI_NOM ELSE null END as PAIS_SOL_3,\r\n"
                          
                          //+" CASE WHEN CPE_OFE_ID_OFERTA=1 THEN des_tit_1.DES_NOM ELSE null END as TITULACION1,\r\n"
                          //+" CASE WHEN CPE_OFE_ID_OFERTA=1 THEN des_tit_2.DES_NOM ELSE null END as TITULACION2,\r\n"
                          //+" CASE WHEN CPE_OFE_ID_OFERTA=1 THEN des_tit_3.DES_NOM ELSE null END as TITULACION3,\r\n"
                          +" CASE WHEN CPE_OFE_ID_OFERTA=1 THEN des_tit_1.GEN_TIT_desc_TITULAC_c ELSE null END as TITULACION1,\r\n"
                          +" CASE WHEN CPE_OFE_ID_OFERTA=1 THEN des_tit_2.GEN_TIT_desc_TITULAC_c ELSE null END as TITULACION2,\r\n"
                          +" CASE WHEN CPE_OFE_ID_OFERTA=1 THEN des_tit_3.GEN_TIT_desc_TITULAC_c ELSE null END as TITULACION3,\r\n"

                          +" CASE WHEN CPE_OFE_ID_OFERTA=1 THEN des_idi_1.DES_NOM ELSE null END as IDIOMA_1,\r\n"
                          +" CASE WHEN CPE_OFE_ID_OFERTA=1 THEN des_idi_2.DES_NOM ELSE null END as IDIOMA_2,\r\n"
                          +" CASE WHEN CPE_OFE_ID_OFERTA=1 THEN des_idi_3.DES_NOM ELSE null END as IDIOMA_3,\r\n"

                          +" CASE WHEN CPE_OFE_ID_OFERTA=1 THEN des_nidi_1.DES_NOM ELSE null END as NIVEL_IDIOMA_1,\r\n"
                          +" CASE WHEN CPE_OFE_ID_OFERTA=1 THEN des_nidi_2.DES_NOM ELSE null END as NIVEL_IDIOMA_2,\r\n"
                          +" CASE WHEN CPE_OFE_ID_OFERTA=1 THEN des_nidi_3.DES_NOM ELSE null END as NIVEL_IDIOMA_3,\r\n"
                          
                          +" CASE WHEN CPE_OFE_ID_OFERTA=1 THEN res.DES_NOM ELSE null END AS RESULTADO,\r\n"
                          +" CASE WHEN res.DES_VAL_COD IN ('"+ConstantesMeLanbide39.CODIGO_RESULTADO_CONCEDIDO+"','"+ConstantesMeLanbide39.CODIGO_RESULTADO_RENUNCIA+"','"+ConstantesMeLanbide39.CODIGO_RESULTADO_NO_EVALUADO+"') AND CPE_OFE_ID_OFERTA=1 THEN 0\r\n"
                          +" WHEN res.DES_VAL_COD IN ('"+ConstantesMeLanbide39.CODIGO_RESULTADO_DENEGADO+"') AND CPE_OFE_ID_OFERTA=1 THEN 1\r\n"
                          +" ELSE NULL\r\n"
                          +" END PUESTOS_DENEGADOS,\r\n"
                          +" CASE WHEN res.DES_VAL_COD IN ('"+ConstantesMeLanbide39.CODIGO_RESULTADO_CONCEDIDO+"','"+ConstantesMeLanbide39.CODIGO_RESULTADO_DENEGADO+"','"+ConstantesMeLanbide39.CODIGO_RESULTADO_NO_EVALUADO+"') AND CPE_OFE_ID_OFERTA=1THEN 0\r\n"
                          +" WHEN res.DES_VAL_COD IN ('"+ConstantesMeLanbide39.CODIGO_RESULTADO_RENUNCIA+"') AND CPE_OFE_ID_OFERTA=1 THEN 1\r\n"
                          +" ELSE NULL\r\n"
                          +" END\r\n"
                          +" PUESTOS_REN_ANTES_RES,\r\n"
                          //+" CPE_PTO_COD_MOTIVO as MOTIVO,\r\n"
                          +" case when CPE_PTO_COD_MOTIVO is not null then des_motivo.DES_COD || ' - ' || des_motivo.DES_NOM else '' end as MOTIVO,\r\n"

                          +" case when res.DES_VAL_COD IN ('"+ConstantesMeLanbide39.CODIGO_RESULTADO_CONCEDIDO+"') then CPE_OFE_COD_OFERTA else null end as OFERTA,\r\n"
                          +" case when res.DES_VAL_COD IN ('"+ConstantesMeLanbide39.CODIGO_RESULTADO_CONCEDIDO+"') then CPE_OFE_PREC_NOM else null end as PERSONAS_PRECANDIDATAS,\r\n"
                          +" case when res.DES_VAL_COD IN ('"+ConstantesMeLanbide39.CODIGO_RESULTADO_CONCEDIDO+"') then CPE_OFE_DIFUSION else null end as DIFUSION,\r\n"
                          +" case when res.DES_VAL_COD IN ('"+ConstantesMeLanbide39.CODIGO_RESULTADO_CONCEDIDO+"') then TO_CHAR(CPE_OFE_FEC_ENV_CAND, '"+ConstantesMeLanbide39.FORMATO_FECHA+"') else null end as FECHA_ENV_PERS_CANDIDATAS,\r\n"
                          +" case when res.DES_VAL_COD IN ('"+ConstantesMeLanbide39.CODIGO_RESULTADO_CONCEDIDO+"') then CPE_OFE_NUM_TOT_CAND else null end as NUMERO_CANDIDATOS,\r\n"
                    
                    
                          +" case when res.DES_VAL_COD IN ('"+ConstantesMeLanbide39.CODIGO_RESULTADO_CONCEDIDO+"') then\r\n"
                            + " case when CPE_OFE_ID_OFERTA = 1 then CPE_OFE_NIF else 'SUSTITUTO - '||CPE_OFE_NIF end\r\n"
                          +" else null end as NIF_SELEC,"
                    
                          +" case when res.DES_VAL_COD IN ('"+ConstantesMeLanbide39.CODIGO_RESULTADO_CONCEDIDO+"') then CPE_OFE_NOMBRE else null end as NOM_SELEC,\r\n"                    
                          +" case when res.DES_VAL_COD IN ('"+ConstantesMeLanbide39.CODIGO_RESULTADO_CONCEDIDO+"') then CPE_OFE_APE1 else null end as APE1_SELEC,\r\n"
                          +" case when res.DES_VAL_COD IN ('"+ConstantesMeLanbide39.CODIGO_RESULTADO_CONCEDIDO+"') then CPE_OFE_APE2 else null end as APE2_SELEC,\r\n"                          
                    
                          +" case when res.DES_VAL_COD IN ('"+ConstantesMeLanbide39.CODIGO_RESULTADO_CONCEDIDO+"') then TO_CHAR(CPE_OFE_FEC_NAC, '"+ConstantesMeLanbide39.FORMATO_FECHA+"') else null end as FECHA_NACIMIENTO,\r\n"
                          +" case when res.DES_VAL_COD IN ('"+ConstantesMeLanbide39.CODIGO_RESULTADO_CONCEDIDO+"') then CPE_OFE_SEXO else null end as SEXO,\r\n"
                          +" case when res.DES_VAL_COD IN ('"+ConstantesMeLanbide39.CODIGO_RESULTADO_CONCEDIDO+"') then pc1.PAI_NOM else null end as PAIS_CONT_1,\r\n"
                          +" case when res.DES_VAL_COD IN ('"+ConstantesMeLanbide39.CODIGO_RESULTADO_CONCEDIDO+"') then pc2.PAI_NOM else null end as PAIS_CONT_2,\r\n"
                          +" case when res.DES_VAL_COD IN ('"+ConstantesMeLanbide39.CODIGO_RESULTADO_CONCEDIDO+"') then pc3.PAI_NOM else null end as PAIS_CONT_3,\r\n"

                          //+" case when res.DES_VAL_COD IN ('"+ConstantesMeLanbide39.CODIGO_RESULTADO_RENUNCIA+"') then null else des_tit_sel.DES_NOM end as TITULACION_PERS_CANDIDATA,\r\n"
                          +" case when res.DES_VAL_COD IN ('"+ConstantesMeLanbide39.CODIGO_RESULTADO_CONCEDIDO+"') then des_tit_sel.GEN_TIT_desc_TITULAC_c else null end as TITULACION_PERS_CANDIDATA,\r\n"
                          //+" case\r\n"
                          //+"when res.DES_VAL_COD IN ('"+ConstantesMeLanbide39.CODIGO_RESULTADO_RENUNCIA+"') then null "
                          //+" when SUBSTR(des_tit_sel.DES_NOM, 1, 2) = '59' then 'GRADOS'\r\n"
                          //+" when SUBSTR(des_tit_sel.DES_NOM, 1, 2) = '60' then 'MÁSTER'\r\n"
                          //+" when SUBSTR(des_tit_sel.DES_NOM, 1, 2) = '61' then 'DOCTORADO'\r\n"
                          //+" when SUBSTR(des_tit_sel.DES_NOM, 1, 2) = '55' then 'LICENCIATURAS E INGENIERÍAS SUPERIORES'\r\n"
                          //+" when SUBSTR(des_tit_sel.DES_NOM, 1, 2) = '54' then 'DIPLOMATURAS E INGERNIERÍAS TÉCNICAS'\r\n"
                          //+" when SUBSTR(des_tit_sel.DES_NOM, 1, 2) = '51' then 'GRADOS SUPERIORES'\r\n"
                          //+" when SUBSTR(des_tit_sel.DES_NOM, 1, 2) = '33' then 'GRADOS MEDIOS'\r\n"
                                              
                         +" case when res.DES_VAL_COD IN ('"+ConstantesMeLanbide39.CODIGO_RESULTADO_CONCEDIDO+"') then des_niv_for_sel.gen_nivf_desc_abr_c else null end as NIVEL_FORMATIVO,\r\n"

                          +" case when res.DES_VAL_COD IN ('"+ConstantesMeLanbide39.CODIGO_RESULTADO_CONCEDIDO+"') then TO_CHAR(CPE_OFE_FEC_INI, '"+ConstantesMeLanbide39.FORMATO_FECHA+"') else null end as FECHA_INICIO,\r\n"
                          //+" case when cpe_ofe_contratacion = 'S' then case when res.DES_VAL_COD IN ('"+ConstantesMeLanbide39.CODIGO_RESULTADO_RENUNCIA+"') then null else "
                          +" case when cpe_ofe_contratacion = 'S' and res.DES_VAL_COD IN ('"+ConstantesMeLanbide39.CODIGO_RESULTADO_CONCEDIDO+"') then \r\n"
                            + " CASE WHEN CPE_OFE_FEC_BAJA IS NULL THEN TO_CHAR(CPE_OFE_FEC_FIN, '"+ConstantesMeLanbide39.FORMATO_FECHA+"') ELSE TO_CHAR(CPE_OFE_FEC_BAJA, '"+ConstantesMeLanbide39.FORMATO_FECHA+"') END else null\r\n"
                          + " end as FECHA_FIN,\r\n"
                          +" case when res.DES_VAL_COD IN ('"+ConstantesMeLanbide39.CODIGO_RESULTADO_CONCEDIDO+"') then "
                        + "floor(months_between((CASE WHEN CPE_OFE_FEC_BAJA IS NULL THEN CPE_OFE_FEC_FIN ELSE CPE_OFE_FEC_BAJA END)+1, CPE_OFE_FEC_INI)) "
                        + " else null end as TIEMPO_CONTRATACION,\r\n"
                          +" CASE "
                          +" when puesto.CPE_PTO_COD_RESULT IN ('"+ConstantesMeLanbide39.CODIGO_RESULTADO_CONCEDIDO+"') and CPE_OFE_NIF IS NOT NULL  AND CPE_OFE_COD_CAUSA_BAJA IS NULL THEN 1\r\n"
                          +" ELSE 0  end as PUESTOS_CONTRATADOS,\r\n"
                          +" case when res.DES_VAL_COD IN ('"+ConstantesMeLanbide39.CODIGO_RESULTADO_CONCEDIDO+"') and CPE_OFE_CONTRATACION = 'N' then REN.DES_NOM "
                            + " when res.DES_VAL_COD IN ('"+ConstantesMeLanbide39.CODIGO_RESULTADO_CONCEDIDO+"') and CPE_OFE_CONTRATACION_PRES_OFE= 'N' then REN_NOOFE.DES_NOM "
                            + " else null end as MOTIVO_RENUNCIA,\r\n"
                         
                          +" case when res.DES_VAL_COD IN ('"+ConstantesMeLanbide39.CODIGO_RESULTADO_CONCEDIDO+"') and (CPE_OFE_CONTRATACION = 'N' or CPE_OFE_CONTRATACION_PRES_OFE= 'N') "
                            + " then 1 else 0 end as PUESTOS_REN_ANT_1PAGO,\r\n"                    
                          //+" reN.DES_NOM CAUSA_RENUNCIA,"
                          +" case when res.DES_VAL_COD IN ('"+ConstantesMeLanbide39.CODIGO_RESULTADO_CONCEDIDO+"') and CPE_OFE_CONTRATACION = 'N' then REN.DES_NOM "
                            + " when res.DES_VAL_COD IN ('"+ConstantesMeLanbide39.CODIGO_RESULTADO_CONCEDIDO+"') and CPE_OFE_CONTRATACION_PRES_OFE= 'N' then REN_NOOFE.DES_NOM "
                            + " else null end as CAUSA_RENUNCIA,\r\n"
                            + "case when puesto.CPE_PTO_COD_RESULT IN ('001') then CPE_OFE_ID_OFERTA else 0 end CPE_OFE_ID_OFERTA"

                          +" from\r\n"
                          +" "+tablaExpediente+" exp left join\r\n"
                          +" "+tablaPuesto+" puesto\r\n"
                          +" on exp.exp_num = puesto.CPE_PTO_NUMEXP and exp.exp_eje = puesto.CPE_PTO_EXP_EJE\r\n"
                          +" left join "+tablaOferta+" oferta on puesto.CPE_PTO_COD_PUESTO = oferta.CPE_OFE_COD_PUESTO and puesto.CPE_PTO_NUMEXP = oferta.CPE_OFE_NUMEXP\r\n"
                          +" left join "+tablaTexto+" texto1 on texto1.TXT_NUM = puesto.CPE_PTO_NUMEXP and texto1.TXT_EJE = puesto.CPE_PTO_EXP_EJE and texto1.TXT_COD = '"+codigoEmpresa+"'\r\n"
                          +" left join (select texto.TDE_COD, texto.TDE_NUM, texto.TDE_EJE, texto.TDE_VALOR, upper(prov.PRV_NOM) nom_prov  from "+GlobalNames.ESQUEMA_GENERICO+tablaProvincia+" prov inner join "+tablaDesplegable+" texto on texto.TDE_VALOR = LPAD(to_char(prov.PRV_COD) ,2,'0')) texto2 on texto2.TDE_NUM = puesto.CPE_PTO_NUMEXP and texto2.TDE_EJE = puesto.CPE_PTO_EXP_EJE and texto2.TDE_COD = '"+codigoTerHis+"'\r\n"

                          +" left join "+tablaNumero+" numero1 on numero1.TNU_NUM = puesto.CPE_PTO_NUMEXP and numero1.TNU_EJE = puesto.CPE_PTO_EXP_EJE and numero1.TNU_COD = '"+codigoPSolic+"'\r\n"
                          +" left join "+tablaNumero+" numero2 on numero2.TNU_NUM = puesto.CPE_PTO_NUMEXP and numero2.TNU_EJE = puesto.CPE_PTO_EXP_EJE and numero2.TNU_COD = '"+codigoPDen+"'\r\n"
                          +" left join "+tablaNumero+" numero3 on numero3.TNU_NUM = puesto.CPE_PTO_NUMEXP and numero3.TNU_EJE = puesto.CPE_PTO_EXP_EJE and numero3.TNU_COD = '"+codigoPRen+"'\r\n"
                          +" left join "+tablaNumero+" numero4 on numero4.TNU_NUM = puesto.CPE_PTO_NUMEXP and numero4.TNU_EJE = puesto.CPE_PTO_EXP_EJE and numero4.TNU_COD = '"+codigoPCont+"'\r\n"
                          //+" left join "+tablaNumero+" numero5 on numero5.TNU_NUM = puesto.CPE_PTO_NUMEXP and numero5.TNU_EJE = puesto.CPE_PTO_EXP_EJE and numero5.TNU_COD = '"+codigoNumPRes+"'\r\n"


                          +" left join "+GlobalNames.ESQUEMA_GENERICO+tablaPais+" p1 on puesto.CPE_PTO_PAI_COD_1 = p1.PAI_COD\r\n"
                          +" left join "+GlobalNames.ESQUEMA_GENERICO+tablaPais+" p2 on puesto.CPE_PTO_PAI_COD_2 = p2.PAI_COD\r\n"
                          +" left join "+GlobalNames.ESQUEMA_GENERICO+tablaPais+" p3 on puesto.CPE_PTO_PAI_COD_3 = p3.PAI_COD\r\n"

                          +" left join "+tablaTitulaciones+" des_tit_1 on des_tit_1.GEN_TIT_COD_TITULAC = puesto.CPE_PTO_COD_TIT_1 \r\n"
                          +" left join "+tablaTitulaciones+" des_tit_2 on des_tit_2.GEN_TIT_COD_TITULAC = puesto.CPE_PTO_COD_TIT_2 \r\n"
                          +" left join "+tablaTitulaciones+" des_tit_3 on des_tit_3.GEN_TIT_COD_TITULAC = puesto.CPE_PTO_COD_TIT_3 \r\n"

                          +" left join "+tablaEdesval+" des_idi_1 on des_idi_1.DES_VAL_COD = puesto.CPE_PTO_COD_IDIOMA_1 and des_idi_1.DES_COD = '"+codigoIdi+"'\r\n"
                          +" left join "+tablaEdesval+" des_nidi_1 on des_nidi_1.DES_VAL_COD = puesto.CPE_PTO_COD_NIV_IDI_1 and des_nidi_1.DES_COD = '"+codigoNidi+"'\r\n"

                          +" left join "+tablaEdesval+" des_idi_2 on des_idi_2.DES_VAL_COD = puesto.CPE_PTO_COD_IDIOMA_2 and des_idi_2.DES_COD = '"+codigoIdi+"'\r\n"
                          +" left join "+tablaEdesval+" des_nidi_2 on des_nidi_2.DES_VAL_COD = puesto.CPE_PTO_COD_NIV_IDI_2 and des_nidi_2.DES_COD = '"+codigoNidi+"'\r\n"

                          +" left join "+tablaEdesval+" des_idi_3 on des_idi_3.DES_VAL_COD = puesto.CPE_PTO_COD_IDIOMA_3 and des_idi_3.DES_COD = '"+codigoIdi+"'\r\n"
                          +" left join "+tablaEdesval+" des_nidi_3 on des_nidi_3.DES_VAL_COD = puesto.CPE_PTO_COD_NIV_IDI_3 and des_nidi_3.DES_COD = '"+codigoNidi+"'\r\n"

                          //+" left join "+tablaEdesval+" des_tit_sel on des_tit_sel.DES_VAL_COD = oferta.CPE_OFE_COD_TITULACION and des_tit_sel.DES_COD = '"+codigoTit+"'\r\n"
                          +" left join "+tablaTitulaciones+" des_tit_sel on des_tit_sel.GEN_TIT_COD_TITULAC = oferta.CPE_OFE_COD_TITULACION\r\n"
                          +" left join "+tablaNivelesFormativos+" des_niv_for_sel on des_niv_for_sel.GEN_NIVF_COD = SUBSTR(des_tit_sel.GEN_TIT_cod_TITULAC, 1, 2)\r\n"  
                    
                          +" left join "+tablaEdesval+" des_motivo on des_motivo.DES_VAL_COD = puesto.CPE_PTO_COD_MOTIVO and des_motivo.DES_COD = '"+codigoMotivo+"'\r\n"

                          +" left join "+GlobalNames.ESQUEMA_GENERICO+tablaPais+" pc1 on oferta.CPE_OFE_PAI_COD_1 = pc1.PAI_COD\r\n"
                          +" left join "+GlobalNames.ESQUEMA_GENERICO+tablaPais+" pc2 on oferta.CPE_OFE_PAI_COD_2 = pc2.PAI_COD\r\n"
                          +" left join "+GlobalNames.ESQUEMA_GENERICO+tablaPais+" pc3 on oferta.CPE_OFE_PAI_COD_3 = pc3.PAI_COD\r\n"
                          +" left join (select * from "+tablaEdesval+" where DES_COD = '"+codigoRes+"') res on puesto.CPE_PTO_COD_RESULT = res.DES_VAL_COD\r\n"
                          +" left join (select * from "+tablaEdesval+" where DES_COD = '"+codigoCren+"') ren on OFERTA.CPE_OFE_COD_CAUSA_RENUNCIA = reN.DES_VAL_COD\r\n"
                          +" left join (select * from "+tablaEdesval+" where DES_COD = 'CROF') ren_noofe on OFERTA.CPE_OFE_COD_CAUSA_REN_PRE_OF = ren_noofe.DES_VAL_COD\r\n"

                          +" where\r\n"
                          +" exp.exp_eje = '"+ejercicio+"'\r\n"
                          +" and EXP.exp_num like '%"+ConstantesMeLanbide39.COD_PROCEDIMIENTO+"%'\r\n"
                          +" --and puesto.CPE_PTO_COD_RESULT <> '"+ConstantesMeLanbide39.CODIGO_RESULTADO_DENEGADO+"'\r\n"
                          +" --and oferta.CPE_OFE_NIF is not null\r\n"
                          +" --and exp.exp_est <> '"+ConstantesMeLanbide39.ESTADO_EXPEDIENTE_ANULADO+"'\r\n"
                          +" /*\r\n"

                          +" group by exp.exp_num, texto1.TXT_VALOR,\r\n"
                          +" numero1.TNU_VALOR, numero2.TNU_VALOR, numero3.TNU_VALOR, numero4.TNU_VALOR, numero5.TNU_VALOR, CPE_OFE_MOT_CONTRATACION,res.DES_NOM ,\r\n"
                          +" CPE_PTO_COD_PUESTO, CPE_PTO_DESC_PUESTO,\r\n"
                          +" p1.PAI_NOM, p2.PAI_NOM, p3.PAI_NOM,\r\n"
                          +" des_tit_1.DES_NOM, des_tit_2.DES_NOM, des_tit_3.DES_NOM,\r\n"
                          +" des_idi_1.DES_NOM, des_nidi_1.DES_NOM, des_idi_2.DES_NOM, des_nidi_2.DES_NOM, des_idi_3.DES_NOM, des_nidi_3.DES_NOM,\r\n"
                          +" CPE_OFE_COD_OFERTA, CPE_OFE_PREC_NOM,\r\n"
                          +" CPE_OFE_DIFUSION, CPE_OFE_FEC_ENV_CAND, CPE_OFE_NUM_TOT_CAND, CPE_OFE_NIF, CPE_OFE_NOMBRE, CPE_OFE_APE1, CPE_OFE_APE2, CPE_OFE_FEC_NAC, CPE_OFE_SEXO, pc1.PAI_NOM, pc2.PAI_NOM, pc3.PAI_NOM,\r\n"
                          +" des_tit_sel.DES_NOM, CPE_OFE_FEC_INI, CPE_OFE_FEC_FIN, CPE_OFE_MESES_CONTRATO\r\n"
                          +" */\r\n"
                          +" order by NUM_EXPEDIENTE, EMPRESA, CPE_PTO_COD_PUESTO, CPE_OFE_FEC_INI, CPE_OFE_FEC_FIN";
            
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while(rs.next())
            {
                FilaInformeResumenPuestosContratadosVO linea = (FilaInformeResumenPuestosContratadosVO)MeLanbide39MappingUtils.getInstance().map(rs, FilaInformeResumenPuestosContratadosVO.class);
                //##275951 CPE - Listado Resumen puestos contratados-- no sabemos porqué se hace esto en cpe y no en cme--quitamos
                /*if(rs.getString("PUESTOS_SOLICITADOS").equals("1")){
                //if(rs.getString("CPE_OFE_ID_OFERTA").equals("1")){//null pointer
                    String sust = validaSustituto(linea.getNumExpediente(), linea.getCodPuesto(), con);
                    if(sust.equals("0"))
                        linea.setPuestosCont(1);
                }*/
                retList.add(linea);
                
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
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
        return retList;
    }

    public String validaSustituto (String numExp, String puesto, Connection con) throws Exception
    {
        ResultSet rs = null;
        Statement st = null;
        String sustituto = "";
        try
        {
            String sentencia = "SELECT COUNT(*) AS NUM FROM CPE_OFERTA WHERE CPE_OFE_NUMEXP = '"+numExp+"' "
                    + "AND CPE_OFE_COD_PUESTO = '" +puesto + "' AND CPE_OFE_ID_OFERTA <> 1";

            if(log.isDebugEnabled()) log.debug("sql = " + sentencia);
            st = con.createStatement();
            rs = st.executeQuery(sentencia);
            if(rs.next())
            {
                sustituto = rs.getString("NUM");
            }
        }
        catch (Exception ex)
        {
            
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
        return sustituto;
    }

    public List<FilaInformeResumenEconomicoVO> getDatosInformeResumenEconomico(Integer ejercicio, Connection con) throws Exception
    {
        log.error("ENTRA EN DAO:getDatosInformeResumenEconomico");
        List<FilaInformeResumenEconomicoVO> retList = new ArrayList<FilaInformeResumenEconomicoVO>();
        
        ResultSet rs = null;
        Statement st = null;
        try
        {
            String tablaPuesto = ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_PUESTO, ConstantesMeLanbide39.FICHERO_PROPIEDADES);
            String tablaOferta = ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_OFERTA, ConstantesMeLanbide39.FICHERO_PROPIEDADES);
            String tablaEdesval = ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_E_DES_VAL, ConstantesMeLanbide39.FICHERO_PROPIEDADES);
            String tablaPais = ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_PAISES, ConstantesMeLanbide39.FICHERO_PROPIEDADES);
            String tablaProvincias = ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_PROVINCIAS, ConstantesMeLanbide39.FICHERO_PROPIEDADES);
            String tablaTexto = ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_TIPO_DATO_TEXTO, ConstantesMeLanbide39.FICHERO_PROPIEDADES);
            String tablaTextoLargo = ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_TIPO_DATO_TEXTO_LARGO, ConstantesMeLanbide39.FICHERO_PROPIEDADES);
            String tablaNumero = ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_TIPO_DATO_NUMERICO, ConstantesMeLanbide39.FICHERO_PROPIEDADES);
            String tablaDesplegable = ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_TIPO_DATO_DESPLEGABLE, ConstantesMeLanbide39.FICHERO_PROPIEDADES);
            String tablaExpediente = ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_E_EXP, ConstantesMeLanbide39.FICHERO_PROPIEDADES);
            String tablaRelacionTerceros = ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_REL_TERCERO_DOMICILIO, ConstantesMeLanbide39.FICHERO_PROPIEDADES);
            String tablaTerceros = ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_TERCEROS, ConstantesMeLanbide39.FICHERO_PROPIEDADES);
            
            String codigoNivForm = ConfigurationParameter.getParameter(ConstantesMeLanbide39.CAMPO_SUPL_NIVEL_FORMATIVO, ConstantesMeLanbide39.FICHERO_PROPIEDADES);
            String codigoTit = ConfigurationParameter.getParameter(ConstantesMeLanbide39.CAMPO_SUPL_TITULACION, ConstantesMeLanbide39.FICHERO_PROPIEDADES);
            String codigoCNAE = ConfigurationParameter.getParameter(ConstantesMeLanbide39.CAMPO_SUPL_CNAE, ConstantesMeLanbide39.FICHERO_PROPIEDADES);
            String codigoCAC = ConfigurationParameter.getParameter(ConstantesMeLanbide39.CAMPO_SUPL_CAC, ConstantesMeLanbide39.FICHERO_PROPIEDADES);
            String codigoEmpresa = ConfigurationParameter.getParameter(ConstantesMeLanbide39.CAMPO_SUPL_EMPRESA, ConstantesMeLanbide39.FICHERO_PROPIEDADES);
            String codigoImSol = ConfigurationParameter.getParameter(ConstantesMeLanbide39.CAMPO_SUPL_IMP_SOLICITADO, ConstantesMeLanbide39.FICHERO_PROPIEDADES);
            String codigoImRen = ConfigurationParameter.getParameter(ConstantesMeLanbide39.CAMPO_SUPL_IMP_REN, ConstantesMeLanbide39.FICHERO_PROPIEDADES);
            String codigoTipoEntidad = ConfigurationParameter.getParameter(ConstantesMeLanbide39.CAMPO_SUPL_TIPO_ENTIDAD, ConstantesMeLanbide39.FICHERO_PROPIEDADES);
            String codigoTerHis = ConfigurationParameter.getParameter(ConstantesMeLanbide39.CAMPO_SUPL_TER_HIS, ConstantesMeLanbide39.FICHERO_PROPIEDADES);
            String codigoResultado = ConfigurationParameter.getParameter(ConstantesMeLanbide39.CAMPO_SUPL_RESULTADO, ConstantesMeLanbide39.FICHERO_PROPIEDADES);
            String codigoMinimiC = ConfigurationParameter.getParameter(ConstantesMeLanbide39.CAMPO_SUPL_MINIMIS_CONCE, ConstantesMeLanbide39.FICHERO_PROPIEDADES);
            String codigoResNumPuesto = ConfigurationParameter.getParameter(ConstantesMeLanbide39.CAMPO_SUPL_RES_NUM_PUESTO, ConstantesMeLanbide39.FICHERO_PROPIEDADES);
            String codigoResImpRenun = ConfigurationParameter.getParameter(ConstantesMeLanbide39.CAMPO_SUPL_RES_IMP_RENUN, ConstantesMeLanbide39.FICHERO_PROPIEDADES);
            String codigoResModImpConc = ConfigurationParameter.getParameter(ConstantesMeLanbide39.CAMPO_SUPL_RES_MOD_IMP_CONC, ConstantesMeLanbide39.FICHERO_PROPIEDADES);
            String codigoCausaRen = ConfigurationParameter.getParameter(ConstantesMeLanbide39.CAMPO_SUPL_CAUSA_RENUNCIA, ConstantesMeLanbide39.FICHERO_PROPIEDADES);
            
            
            
            String query = "SELECT * FROM (\r\n"
                          +" select\r\n"
                          +" 1 orden, exp.exp_num as N_EXPEDIENTE,\r\n"
                          +" COALESCE(to_char(CPE_PTO_COD_PUESTO),'') as COD_PUESTO,\r\n"
                          //+" COALESCE(entidad.valor ,'')entidad,\r\n"
                          +" COALESCE(entidad.valor , COALESCE(TER_NOC, ''))entidad,\r\n"
                          +" COALESCE(texto2.nom_prov, '') PROVINCIA,\r\n"
                          +" COALESCE(TO_CHAR(cnae.valor),'') CNAE09,\r\n"
                         // +" 1 N_PUESTOS_SOLICITADOS,\r\n"--> SI NO HAY PUESTO 0
                     +"CASE WHEN CPE_PTO_COD_PUESTO IS NULL THEN 0 ELSE 1 END AS N_PUESTOS_SOLICITADOS,\r\n"
                    
                          +" COALESCE(CPE_PTO_DESC_PUESTO,'') as PUESTO,\r\n"
                          +" COALESCE(TO_CHAR(CPE_PTO_FEC_INI,'"+ConstantesMeLanbide39.FORMATO_FECHA+"'),'') as FEC_INI,\r\n"
                          +" COALESCE(TO_CHAR(CPE_PTO_FEC_FIN ,'"+ConstantesMeLanbide39.FORMATO_FECHA+"'),'')as FEC_FIN,\r\n"
                          +" CPE_PTO_MESES_CONTRATO as PERIODO_MESES_SUBVENCIONABLES,\r\n"
                          +" case when CPE_PTO_COD_NIV_FORM is not null then CPE_PTO_COD_NIV_FORM else '' end as TITULACION_AB,\r\n"
                          +" COALESCE(PAI1.VALOR,'') PAIS1,\r\n"
                          +" COALESCE(PAI2.VALOR,'') PAIS2,\r\n"
                          +" COALESCE(PAI3.VALOR,'') PAIS3,\r\n"
                          +" --------------COSTES CONTRATO-------------------------\r\n"
                          +" CPE_PTO_SAL_BRUTO as COSTES_CONT_SALARIO_SS,\r\n"
                          +" CPE_PTO_DIETAS_CONV as COSTES_CONT_DIETAS_CONVE,\r\n"
                          +" CASE WHEN NVL(CPE_PTO_SAL_BRUTO,0)+NVL(CPE_PTO_DIETAS_CONV,0) =0 THEN 0\r\n"
                          +" ELSE NVL(CPE_PTO_SAL_BRUTO,0)+NVL(CPE_PTO_DIETAS_CONV,0) \r\n"
                          +" END\r\n"
                          +" as COSTES_CONT,\r\n"
                          +" --------------SUBVENCION SOLICITADA-------------------------\r\n"
                          +" CPE_PTO_SAL_SOLIC  as SOL_SALARIO_SS,\r\n"
                          +" CPE_PTO_DIETAS_SOLIC  as SOL_DIETAS_CONV_SS,\r\n"
                          +" CPE_PTO_TRAM_SOLIC  as SOL_GASTOS_TRA,\r\n"
                          +" CASE WHEN TO_CHAR(NVL(CPE_PTO_SAL_SOLIC,0)+NVL(CPE_PTO_GASTOS_VIS,0)+NVL(CPE_PTO_GASTOS_SEG,0) )='0' THEN 0\r\n"
                          +" ELSE NVL(CPE_PTO_SAL_SOLIC,0)+NVL(CPE_PTO_GASTOS_VIS,0)+NVL(CPE_PTO_GASTOS_SEG,0)\r\n"
                          +" END\r\n"
                          +" as SOLICITADO,\r\n"
                          +" res.DES_NOM AS RESULTADO,\r\n"

                          +" --------------MAXIMO SUBVENCIONABLE -------------------------\r\n"
                          +" case when res.DES_VAL_COD IN ('"+ConstantesMeLanbide39.CODIGO_RESULTADO_RENUNCIA+"')then 0 else CPE_PTO_SALARIO_ANEX_TOT end as DOTACION_ANUAL_MAXIMA,\r\n"
                          +" case when res.DES_VAL_COD IN ('"+ConstantesMeLanbide39.CODIGO_RESULTADO_RENUNCIA+"')then 0 else CPE_PTO_MESES_MANUT end as MESES_SUBVENCIONABLES,\r\n"
                          +" case when res.DES_VAL_COD IN ('"+ConstantesMeLanbide39.CODIGO_RESULTADO_RENUNCIA+"')then 0 else CPE_PTO_DIETAS_MANUT end as DIETAS_SUBV,\r\n"
                          +" case when res.DES_VAL_COD IN ('"+ConstantesMeLanbide39.CODIGO_RESULTADO_RENUNCIA+"')then 0 else CPE_PTO_TRAM_SEGUROS end AS GASTOS_TRAM,\r\n"
                          //+" case when res.DES_VAL_COD IN ('"+ConstantesMeLanbide39.CODIGO_RESULTADO_RENUNCIA+"')then 0 else null end AS MINIMIS_CONCEDIDOS,\r\n"
                          +" NVL(MINIMIS.VALOR,0)AS MINIMIS_CONCEDIDOS,\r\n"
                          +" case when res.DES_VAL_COD IN ('"+ConstantesMeLanbide39.CODIGO_RESULTADO_RENUNCIA+"')then 0 else NVL(CPE_PTO_SALARIO_ANEX_TOT,0)+NVL(CPE_PTO_DIETAS_MANUT,0)+NVL(CPE_PTO_TRAM_SEGUROS,0) end IMP_MAX_SUBVENCIONABLE,\r\n"
                          +" --------------CONCEDIDO-------------------------\r\n"
                          +" case when res.DES_VAL_COD IN ('"+ConstantesMeLanbide39.CODIGO_RESULTADO_RENUNCIA+"') or res.DES_VAL_COD IN ('"+ConstantesMeLanbide39.CODIGO_RESULTADO_DENEGADO+"') then 0\r\n"                    
                          +"   WHEN\r\n"
                          +"     NVL(CPE_PTO_SAL_SOLIC,0)+NVL(CPE_PTO_DIETAS_SOLIC,0)+NVL(CPE_PTO_TRAM_SOLIC,0)> --SOLICITADO >\r\n"
                          +"     NVL(CPE_PTO_SALARIO_ANEX_TOT,0)+NVL(CPE_PTO_DIETAS_MANUT,0)+NVL(CPE_PTO_TRAM_SEGUROS,0) --SUBVENCIONABLE\r\n"
                          +"     THEN NVL(CPE_PTO_SALARIO_ANEX_TOT,0)+NVL(CPE_PTO_DIETAS_MANUT,0)+NVL(CPE_PTO_TRAM_SEGUROS,0) --SUBVENCIONABLE\r\n"
                          +"   ELSE NVL(CPE_PTO_SAL_SOLIC,0)+NVL(CPE_PTO_DIETAS_SOLIC,0)+NVL(CPE_PTO_TRAM_SOLIC,0) --SOLICITADO\r\n"
                          +"   END IMPORTE_CONCEDIDO,\r\n"
                          +" --------------RENUNCIAS-------------------------\r\n"                          
                           +" CASE WHEN RES.DES_VAL_COD IN ('002')THEN 1 \r\n"
                           +"  WHEN RES.DES_VAL_COD IN ('001') AND CPE_OFE_CONTRATACION = 'N' THEN 1 \r\n"
                           +"  WHEN RES.DES_VAL_COD IN ('001') AND CPE_OFE_CONTRATACION_pres_ofe = 'N' THEN 1 \r\n"
                           +"  else 0 end as PUESTOS_REN_ANT_1PAGO, \r\n"

                           +" CASE WHEN RES.DES_VAL_COD IN ('002')AND (( CPE_PTO_IMP_MAX_SUBV IS NOT NULL AND CPE_PTO_IMP_TOT_SOLIC <= CPE_PTO_IMP_MAX_SUBV)or(CPE_PTO_IMP_MAX_SUBV is  null)) THEN CPE_PTO_IMP_TOT_SOLIC  \r\n"
                           +"       WHEN RES.DES_VAL_COD IN ('002')AND CPE_PTO_IMP_MAX_SUBV IS NOT NULL AND CPE_PTO_IMP_TOT_SOLIC > CPE_PTO_IMP_MAX_SUBV THEN CPE_PTO_IMP_MAX_SUBV \r\n"
                           +"       WHEN RES.DES_VAL_COD IN ('001')AND (CPE_OFE_CONTRATACION = 'N' OR cpe_ofe_contratacion_pres_ofe='N') AND ((CPE_PTO_IMP_MAX_SUBV IS NOT NULL AND CPE_PTO_IMP_TOT_SOLIC <= CPE_PTO_IMP_MAX_SUBV ) or CPE_PTO_IMP_MAX_SUBV is null) THEN CPE_PTO_IMP_TOT_SOLIC \r\n"
                           +"       WHEN RES.DES_VAL_COD IN ('001')AND (CPE_OFE_CONTRATACION = 'N' or cpe_ofe_contratacion_pres_ofe='N') AND CPE_PTO_IMP_MAX_SUBV IS NOT NULL AND  CPE_PTO_IMP_TOT_SOLIC > CPE_PTO_IMP_MAX_SUBV THEN CPE_PTO_IMP_MAX_SUBV \r\n"
                           +"       ELSE NULL \r\n"
                           +" END IMPORTE_RENUNCIA, \r\n"
                          +" case when reN.DES_NOM is not null then reN.DES_NOM || '-OBSERVACIONES: '||CPE_OFE_MOT_CONTRATACION else '' end as MOTIVO,\r\n"
                          +" --------------TRAS RESOLUCION -----------------\r\n"
                          +"    CASE WHEN res.DES_VAL_COD IN ('"+ConstantesMeLanbide39.CODIGO_RESULTADO_CONCEDIDO+"') THEN "
                          +" case when res.DES_VAL_COD IN ('"+ConstantesMeLanbide39.CODIGO_RESULTADO_RENUNCIA+"')then null\r\n"
                          +"   WHEN\r\n"
                          +"     NVL(CPE_PTO_SAL_SOLIC,0)+NVL(CPE_PTO_DIETAS_SOLIC,0)+NVL(CPE_PTO_TRAM_SOLIC,0)> --SOLICITADO >\r\n"
                          +"     NVL(CPE_PTO_SALARIO_ANEX_TOT,0)+NVL(CPE_PTO_DIETAS_MANUT,0)+NVL(CPE_PTO_TRAM_SEGUROS,0) --SUBVENCIONABLE\r\n"
                          +"     THEN NVL(CPE_PTO_SALARIO_ANEX_TOT,0)+NVL(CPE_PTO_DIETAS_MANUT,0)+NVL(CPE_PTO_TRAM_SEGUROS,0) --SUBVENCIONABLE\r\n"
                          +"   ELSE NVL(CPE_PTO_SAL_SOLIC,0)+NVL(CPE_PTO_DIETAS_SOLIC,0)+NVL(CPE_PTO_TRAM_SOLIC,0) --SOLICITADO\r\n"
                          +"   END\r\n"
                          +"   -\r\n"
                         
                          +"   CASE\r\n" 
                          +"   WHEN CPE_OFE_CONTRATACION = 'N' AND NVL(CPE_PTO_SAL_SOLIC,0)+NVL(CPE_PTO_DIETAS_SOLIC,0)+NVL(CPE_PTO_TRAM_SOLIC,0)> --SOLICITADO >\r\n"
                          +"     NVL(CPE_PTO_SALARIO_ANEX_TOT,0)+NVL(CPE_PTO_DIETAS_MANUT,0)+NVL(CPE_PTO_TRAM_SEGUROS,0) --SUBVENCIONABLE\r\n"
                          +"     THEN NVL(CPE_PTO_SALARIO_ANEX_TOT,0)+NVL(CPE_PTO_DIETAS_MANUT,0)+NVL(CPE_PTO_TRAM_SEGUROS,0) --SUBVENCIONABLE\r\n"
                          +"   WHEN CPE_OFE_CONTRATACION = 'N' AND NVL(CPE_PTO_SAL_SOLIC,0)+NVL(CPE_PTO_DIETAS_SOLIC,0)+NVL(CPE_PTO_TRAM_SOLIC,0)<= --SOLICITADO <\r\n"
                          +"     NVL(CPE_PTO_SALARIO_ANEX_TOT,0)+NVL(CPE_PTO_DIETAS_MANUT,0)+NVL(CPE_PTO_TRAM_SEGUROS,0) --SUBVENCIONABLE\r\n"
                          +"     THEN NVL(CPE_PTO_SAL_SOLIC,0)+NVL(CPE_PTO_DIETAS_SOLIC,0)+NVL(CPE_PTO_TRAM_SOLIC,0) --SOLICITADO\r\n"
                          +"   ELSE 0\r\n END ELSE 0 "
                          +"   END\r\n"
                          +"   IMP_CONCEDIDO_TRAS_RESOL, \r\n"
                          +" CPE_PTO_GASTOS_SEG, CPE_PTO_GASTOS_VIS, CPE_JUS_GASTOS_SEG, CPE_JUS_GASTOS_VIS "


                          +" from\r\n"
                          +" "+tablaExpediente+" exp\r\n"
                          +" LEFT join (select TXT_VALOR valor ,txt_num from "+tablaTexto+"  where TXT_EJE = '"+ejercicio+"' and TXT_COD = '"+codigoEmpresa+"') ENTIDAD on entidad.txt_num=exp.exp_num\r\n"
                          +" LEFT join (select TER_NOC, EXT_MUN, EXT_NUM from "+tablaRelacionTerceros+" r inner join "+tablaTerceros+" t on r.EXT_TER  = t.TER_COD and r.EXT_EJE = '"+ejercicio+"' and r.EXT_ROL = "+ConstantesMeLanbide39.CODIGO_ROL_ENTIDAD_SOLICITANTE+") TERCERO on tercero.ext_num = exp.exp_num and tercero.ext_mun = exp.exp_mun\r\n"
                          +" left join (select texto.TDE_COD, texto.TDE_NUM, texto.TDE_EJE, texto.TDE_VALOR, upper(prov.PRV_NOM) nom_prov  from "+GlobalNames.ESQUEMA_GENERICO+tablaProvincias+" prov inner join "+tablaDesplegable+" texto on texto.TDE_VALOR = LPAD(to_char(prov.PRV_COD) ,2,'0')) texto2 on texto2.TDE_NUM = exp.exp_num and texto2.TDE_EJE = exp.exp_eje and texto2.TDE_COD = '"+codigoTerHis+"'\r\n"
                          +" left join (select TTL_VALOR valor,TTL_num from "+tablaTextoLargo+"  where TTL_EJE = '"+ejercicio+"' and TTL_COD = '"+codigoCNAE+"')CNAE on cnae.TTL_num=exp.exp_num\r\n"
                          +" left join (select count(*) numero, CPE_PTO_NUMEXP from "+tablaPuesto+" where CPE_PTO_EXP_EJE = '"+ejercicio+"' group by CPE_PTO_NUMEXP )psolic on exp.exp_num = psolic.CPE_PTO_NUMEXP\r\n"
                          +" lEFT join "+tablaPuesto+" puesto\r\n"
                          +" on exp.exp_num = puesto.CPE_PTO_NUMEXP and exp.exp_eje = puesto.CPE_PTO_EXP_EJE\r\n"
                          +" left join "+tablaOferta+" oferta on puesto.CPE_PTO_COD_PUESTO = oferta.CPE_OFE_COD_PUESTO and puesto.CPE_PTO_NUMEXP = oferta.CPE_OFE_NUMEXP\r\n"
                          +" LEFT JOIN CPE_JUSTIFICACION  ON EXP.EXP_NUM = CPE_JUS_NUMEXP AND EXP.EXP_EJE = CPE_JUS_EXP_EJE AND \r\n"
                          +" CPE_JUS_ID_OFERTA = CPE_OFE_ID_OFERTA AND CPE_JUS_COD_PUESTO  = puesto.CPE_PTO_COD_PUESTO      \r\n"
                          +" left join (select * from "+tablaEdesval+" where DES_COD = '"+codigoResultado+"') res on puesto.CPE_PTO_COD_RESULT = res.DES_VAL_COD\r\n"
                          +" LEFT JOIN (select DES_NOM VALOR,DES_VAL_COD from "+tablaEdesval+" where DES_COD = '"+codigoNivForm+"' ) NFOR ON NFOR.DES_VAL_COD = puesto.CPE_PTO_COD_NIV_FORM\r\n"
                          +" LEFT JOIN (select PAI_NOL VALOR,PAI_COD from "+GlobalNames.ESQUEMA_GENERICO+tablaPais+") PAI1 ON PAI1.PAI_COD= puesto.CPE_PTO_PAI_COD_1\r\n"
                          +" LEFT JOIN (select PAI_NOL VALOR,PAI_COD from "+GlobalNames.ESQUEMA_GENERICO+tablaPais+") PAI2 ON PAI2.PAI_COD= puesto.CPE_PTO_PAI_COD_2\r\n"
                          +" LEFT JOIN (select PAI_NOL VALOR,PAI_COD from "+GlobalNames.ESQUEMA_GENERICO+tablaPais+") PAI3 ON PAI3.PAI_COD= puesto.CPE_PTO_PAI_COD_3\r\n"
                          +" LEFT JOIN (select tnu_num, tnu_valor valor from "+tablaNumero+" where TNU_COD = '"+codigoMinimiC+"' and TNU_eje='"+ejercicio+"')MINIMIS ON MINIMIS.tnu_num=exp.exp_num\r\n"
                          +" LEFT JOIN (select tnu_num, tnu_valor valor from "+tablaNumero+" where TNU_COD = '"+codigoResNumPuesto+"' and TNU_eje='"+ejercicio+"')PUES_RENUN ON PUES_RENUN.tnu_num=exp.exp_num\r\n"
                          +" LEFT JOIN (select tnu_num, tnu_valor valor from "+tablaNumero+" where TNU_COD = '"+codigoResImpRenun+"' and TNU_eje='"+ejercicio+"')IMP_RENUN ON IMP_RENUN.tnu_num=exp.exp_num\r\n"
                          +" left join (select * from "+tablaEdesval+" where DES_COD = '"+codigoCausaRen+"') reN on OFERTA.CPE_OFE_COD_CAUSA_RENUNCIA = reN.DES_VAL_COD\r\n"

                          +" where\r\n"
                          +" --exp.exp_est <> '"+ConstantesMeLanbide39.ESTADO_EXPEDIENTE_ANULADO+"' and\r\n"
                          +" EXP.exp_num like '%"+ConstantesMeLanbide39.COD_PROCEDIMIENTO+"%'\r\n"
                          +" and EXP.EXP_EJE = '"+ejercicio+"'"
                          +" and (CPE_OFE_ID_OFERTA=1  or CPE_OFE_ID_OFERTA is null)\r\n"

                          +" UNION\r\n"
                          +" select orden, n_expediente, COD_PUESTO, ENTIDAD, PROVINCIA, CNAE09, N_PUESTOS_SOLICITADOS, PUESTO,  FEC_INI, FEC_FIN, PERIODO_MESES_SUBVENCIONABLES,\r\n"
                          +" TITULACION_AB,PAIS1,PAIS2,PAIS3,COSTES_CONT_SALARIO_SS,COSTES_CONT_DIETAS_CONVE,nvl(SUM(COSTES_CONT),0) COSTES_CONT, null SOL_SALARIO_SS,\r\n"
                          +" null SOL_DIETAS_CONV_SS,null SOL_GASTOS_TRA,SUM( SOLICITADO) SOLICITADO,null RESULTADO,null DOTACION_ANUAL_MAXIMA,null MESES_SUBVENCIONABLES,null DIETAS_SUBV,null GASTOS_TRAM,\r\n"
                          +" NVL(MINIMIS.VALOR,0) MINIMIS_CONCEDIDOS,\r\n"
                          +" SUM(IMP_MAX_SUBVENCIONABLE) IMP_MAX_SUBVENCIONABLE,SUM( IMPORTE_CONCEDIDO)  IMPORTE_CONCEDIDO,SUM(NUM_RENUNCIAS)NUM_RENUNCIAS,\r\n"
                          +" SUM(IMPORTE_RENUNCIA)IMPORTE_RENUNCIA,MOTIVO,\r\n"
                          +" SUM(IMP_CONCEDIDO_TRAS_RESOL) IMP_CONCEDIDO_TRAS_RESOL,\r\n" 
                            +" SUM(CPE_PTO_GASTOS_SEG) CPE_PTO_GASTOS_SEG , SUM(CPE_PTO_GASTOS_VIS) CPE_PTO_GASTOS_VIS, \r\n"
                            +" SUM(CPE_JUS_GASTOS_SEG) CPE_JUS_GASTOS_SEG, SUM(CPE_JUS_GASTOS_VIS) CPE_JUS_GASTOS_VIS\r\n"
                          +" FROM(\r\n"

                          +" select\r\n"
                          +" 2 orden, exp.exp_num as N_EXPEDIENTE,\r\n"
                          +" '' COD_PUESTO,\r\n"
                          //+" COALESCE(entidad.valor ,'')entidad,\r\n"
                          +" COALESCE(entidad.valor , COALESCE(TER_NOC, ''))entidad,\r\n"
                          +" coalesce(texto2.nom_prov, '')provincia,\r\n"
                          +" 'TOTAL' CNAE09,\r\n"
                          +" psolic.numero N_PUESTOS_SOLICITADOS,\r\n"
                          +" ''  as PUESTO,\r\n"
                          +" '' RESULTADO,\r\n"
                          +" '' as FEC_INI,\r\n"
                          +" '' AS FEC_FIN,\r\n"
                          +" 0  as PERIODO_MESES_SUBVENCIONABLES,\r\n"
                          +" ''  as TITULACION_AB,\r\n"
                          +" ''  PAIS1,\r\n"
                          +" '' PAIS2,\r\n"
                          +" ''  PAIS3,\r\n"
                          +" --------------COSTES CONTRATO-------------------------\r\n"
                          +" 0  as COSTES_CONT_SALARIO_SS,\r\n"
                          +" 0  as COSTES_CONT_DIETAS_CONVE,\r\n"
                          +" --SUM(\r\n"
                          +" CASE WHEN NVL(CPE_PTO_SAL_BRUTO,0)+NVL(CPE_PTO_DIETAS_CONV,0) =0 THEN 0\r\n"
                          +" ELSE NVL(CPE_PTO_SAL_BRUTO,0)+NVL(CPE_PTO_DIETAS_CONV,0)\r\n"
                          +" END\r\n"
                          +" --)\r\n"
                          +" as COSTES_CONT,\r\n"
                          +" --------------SUBVENCION SOLICITADA-------------------------\r\n"
                          +" 0  as SOL_SALARIO_SS,\r\n"
                          +" 0 as SOL_DIETAS_CONV_SS,\r\n"
                          +" 0 as SOL_GASTOS_TRA,\r\n"
                          +" --SUM(\r\n"
                          +" CASE WHEN NVL(CPE_PTO_SAL_SOLIC,0)+NVL(CPE_PTO_GASTOS_VIS,0)+NVL(CPE_PTO_GASTOS_SEG,0) =0 THEN 0\r\n"
                          +" ELSE NVL(CPE_PTO_SAL_SOLIC,0)+NVL(CPE_PTO_GASTOS_VIS,0)+NVL(CPE_PTO_GASTOS_SEG,0)\r\n"
                          +" END\r\n"
                          +" --)\r\n"
                          +" as SOLICITADO,\r\n"
                          +" --------------MAXIMO SUBVENCIONABLE -------------------------\r\n"
                          +" 0 as DOTACION_ANUAL_MAXIMA,\r\n"
                          +" 0 as MESES_SUBVENCIONABLES,\r\n"
                          +" 0 as DIETAS_SUBV,\r\n"
                          +" 0 AS GASTOS_TRAM,\r\n"
                          +" --COALESCE(TO_CHAR(NVL(MINIMIS.VALOR,0)),'') AS MINIMIS_CONCEDIDOS,\r\n"
                          +" --SUM(\r\n"
                          +" case\n\n"
                          +" when res.DES_VAL_COD IN ('"+ConstantesMeLanbide39.CODIGO_RESULTADO_RENUNCIA+"') THEN 0\n\n"
                          +" else\n\n"
                          +" NVL(CPE_PTO_SALARIO_ANEX_TOT,0)+NVL(CPE_PTO_DIETAS_MANUT,0)+NVL(CPE_PTO_TRAM_SEGUROS,0)\r\n"
                          +" --)\r\n"
                          +" end"
                          +" IMP_MAX_SUBVENCIONABLE,\r\n"
                          +" --------------CONCEDIDO-------------------------\r\n"
                          +" --SUM(\r\n"
                          +" CASE\r\n"
                          +" when res.DES_VAL_COD IN ('"+ConstantesMeLanbide39.CODIGO_RESULTADO_RENUNCIA+"') or res.DES_VAL_COD IN ('"+ConstantesMeLanbide39.CODIGO_RESULTADO_DENEGADO+"') then 0\r\n"
                          //+" when res.DES_VAL_COD IN ('"+ConstantesMeLanbide39.CODIGO_RESULTADO_RENUNCIA+"') then 0\r\n"  
                          +"   WHEN\r\n"
                          +"     NVL(CPE_PTO_SAL_SOLIC,0)+NVL(CPE_PTO_DIETAS_SOLIC,0)+NVL(CPE_PTO_TRAM_SOLIC,0)> --SOLICITADO >\r\n"
                          +"     NVL(CPE_PTO_SALARIO_ANEX_TOT,0)+NVL(CPE_PTO_DIETAS_MANUT,0)+NVL(CPE_PTO_TRAM_SEGUROS,0) --SUBVENCIONABLE\r\n"
                          +"     THEN NVL(CPE_PTO_SALARIO_ANEX_TOT,0)+NVL(CPE_PTO_DIETAS_MANUT,0)+NVL(CPE_PTO_TRAM_SEGUROS,0)--SUBVENCIONABLE\r\n"
                          +"   ELSE NVL(CPE_PTO_SAL_SOLIC,0)+NVL(CPE_PTO_DIETAS_SOLIC,0)+NVL(CPE_PTO_TRAM_SOLIC,0) --SOLICITADO\r\n"
                          +"   END\r\n"
                          +"   --)\r\n"
                          +"   IMPORTE_CONCEDIDO,\r\n"
                          +" --------------RENUNCIAS-------------------------\r\n"
                        +"   CASE WHEN RES.DES_VAL_COD IN ('"+ConstantesMeLanbide39.CODIGO_RESULTADO_RENUNCIA+"')THEN 1 \r\n"
                        +"       WHEN RES.DES_VAL_COD IN ('"+ConstantesMeLanbide39.CODIGO_RESULTADO_CONCEDIDO+"') AND CPE_OFE_CONTRATACION = 'N' THEN 1 \r\n"
                        +"       WHEN RES.DES_VAL_COD IN ('"+ConstantesMeLanbide39.CODIGO_RESULTADO_CONCEDIDO+"') AND CPE_OFE_CONTRATACION_pres_ofe = 'N' THEN 1 \r\n"
                        +"      else 0 end as NUM_RENUNCIAS, \r\n"
                        
                        +"      CASE WHEN RES.DES_VAL_COD IN ('"+ConstantesMeLanbide39.CODIGO_RESULTADO_RENUNCIA+"')AND ((CPE_PTO_IMP_MAX_SUBV IS NOT NULL AND CPE_PTO_IMP_TOT_SOLIC <= CPE_PTO_IMP_MAX_SUBV) or CPE_PTO_IMP_MAX_SUBV IS NULL) THEN CPE_PTO_IMP_TOT_SOLIC \r\n "
                        +"      WHEN RES.DES_VAL_COD IN ('"+ConstantesMeLanbide39.CODIGO_RESULTADO_RENUNCIA+"')AND CPE_PTO_IMP_MAX_SUBV IS NOT NULL AND CPE_PTO_IMP_TOT_SOLIC > CPE_PTO_IMP_MAX_SUBV THEN CPE_PTO_IMP_MAX_SUBV \r\n "
                        +"      WHEN RES.DES_VAL_COD IN ('"+ConstantesMeLanbide39.CODIGO_RESULTADO_CONCEDIDO+"')AND (CPE_OFE_CONTRATACION = 'N' OR CPE_OFE_CONTRATACION_PRES_OFE='N') AND ((CPE_PTO_IMP_MAX_SUBV IS NOT NULL AND CPE_PTO_IMP_TOT_SOLIC <= CPE_PTO_IMP_MAX_SUBV) or CPE_PTO_IMP_MAX_SUBV IS NULL) THEN CPE_PTO_IMP_TOT_SOLIC \r\n "
                        +"      WHEN RES.DES_VAL_COD IN ('"+ConstantesMeLanbide39.CODIGO_RESULTADO_CONCEDIDO+"')AND (CPE_OFE_CONTRATACION = 'N' or cpe_ofe_contratacion_pres_ofe='N') AND CPE_PTO_IMP_MAX_SUBV is not null and CPE_PTO_IMP_TOT_SOLIC > CPE_PTO_IMP_MAX_SUBV THEN CPE_PTO_IMP_MAX_SUBV \r\n "
                        +"      ELSE NULL \r\n "
                        +"      END IMPORTE_RENUNCIA, \r\n "
                    
                          +" '' MOTIVO,\r\n"
                          +" --------------TRAS RESOLUCION -----------------\r\n"
                          +" CASE\r\n"
                          +" when res.DES_VAL_COD IN ('"+ConstantesMeLanbide39.CODIGO_RESULTADO_CONCEDIDO+"') then "
                          +"  CASE WHEN\r\n"
                          +"     NVL(CPE_PTO_SAL_SOLIC,0)+NVL(CPE_PTO_DIETAS_SOLIC,0)+NVL(CPE_PTO_TRAM_SOLIC,0)> --SOLICITADO >\r\n"
                          +"     NVL(CPE_PTO_SALARIO_ANEX_TOT,0)+NVL(CPE_PTO_DIETAS_MANUT,0)+NVL(CPE_PTO_TRAM_SEGUROS,0) --SUBVENCIONABLE\r\n"
                          +"     THEN NVL(CPE_PTO_SALARIO_ANEX_TOT,0)+NVL(CPE_PTO_DIETAS_MANUT,0)+NVL(CPE_PTO_TRAM_SEGUROS,0) --SUBVENCIONABLE\r\n"
                          +"   ELSE NVL(CPE_PTO_SAL_SOLIC,0)+NVL(CPE_PTO_DIETAS_SOLIC,0)+NVL(CPE_PTO_TRAM_SOLIC,0) --SOLICITADO\r\n"
                          +"   END\r\n"
                          +"   -\r\n"
                          +"   CASE\r\n"
                          +"   WHEN CPE_OFE_CONTRATACION = 'N' AND NVL(CPE_PTO_SAL_SOLIC,0)+NVL(CPE_PTO_DIETAS_SOLIC,0)+NVL(CPE_PTO_TRAM_SOLIC,0)> --SOLICITADO >\r\n"
                          +"     NVL(CPE_PTO_SALARIO_ANEX_TOT,0)+NVL(CPE_PTO_DIETAS_MANUT,0)+NVL(CPE_PTO_TRAM_SEGUROS,0) --SUBVENCIONABLE\r\n"
                          +"     THEN NVL(CPE_PTO_SALARIO_ANEX_TOT,0)+NVL(CPE_PTO_DIETAS_MANUT,0)+NVL(CPE_PTO_TRAM_SEGUROS,0) --SUBVENCIONABLE\r\n"
                          +"   WHEN CPE_OFE_CONTRATACION = 'N' AND NVL(CPE_PTO_SAL_SOLIC,0)+NVL(CPE_PTO_DIETAS_SOLIC,0)+NVL(CPE_PTO_TRAM_SOLIC,0)<= --SOLICITADO <\r\n"
                          +"     NVL(CPE_PTO_SALARIO_ANEX_TOT,0)+NVL(CPE_PTO_DIETAS_MANUT,0)+NVL(CPE_PTO_TRAM_SEGUROS,0) --SUBVENCIONABLE\r\n"
                          +"     THEN NVL(CPE_PTO_SAL_SOLIC,0)+NVL(CPE_PTO_DIETAS_SOLIC,0)+NVL(CPE_PTO_TRAM_SOLIC,0) --SOLICITADO\r\n"
                          +"   ELSE 0\r\n"
                          +"   END ELSE 0 END \r\n"
                          +"   IMP_CONCEDIDO_TRAS_RESOL,\r\n" 
                          +" CPE_PTO_GASTOS_SEG, CPE_PTO_GASTOS_VIS, CPE_JUS_GASTOS_SEG, CPE_JUS_GASTOS_VIS"


                          +" from\r\n"
                          +" "+tablaExpediente+" exp\r\n"
                          +" LEFT join  (select TXT_VALOR valor ,txt_num from "+tablaTexto+"  where TXT_EJE = '"+ejercicio+"' and TXT_COD = '"+codigoEmpresa+"') ENTIDAD on entidad.txt_num=exp.exp_num\r\n"
                          +" LEFT join (select TER_NOC, EXT_MUN, EXT_NUM from "+tablaRelacionTerceros+" r inner join "+tablaTerceros+" t on r.EXT_TER  = t.TER_COD and r.EXT_EJE = '"+ejercicio+"' and r.EXT_ROL = "+ConstantesMeLanbide39.CODIGO_ROL_ENTIDAD_SOLICITANTE+") TERCERO on tercero.ext_num = exp.exp_num and tercero.ext_mun = exp.exp_mun\r\n"
                          +" left join (select texto.TXT_COD, texto.TXT_NUM, texto.TXT_EJE, texto.TXT_VALOR, upper(prov.PRV_NOM) nom_prov  from "+GlobalNames.ESQUEMA_GENERICO+tablaProvincias+" prov inner join "+tablaTexto+" texto on texto.TXT_VALOR = to_char(prov.PRV_COD)) texto2 on texto2.TXT_NUM = exp.exp_num and texto2.TXT_EJE = exp.exp_eje and texto2.TXT_COD = '"+codigoTerHis+"'\r\n"
                          +" left join (select TTL_VALOR valor,TTL_num from "+tablaTextoLargo+"  where TTL_EJE = '"+ejercicio+"' and TTL_COD = '"+codigoCNAE+"')CNAE on cnae.TTL_num=exp.exp_num\r\n"
                          +" left join (select count(*) numero, CPE_PTO_NUMEXP from "+tablaPuesto+" where CPE_PTO_EXP_EJE = '"+ejercicio+"' group by CPE_PTO_NUMEXP )psolic on exp.exp_num = psolic.CPE_PTO_NUMEXP\r\n"
                          +" lEFT join "+tablaPuesto+" puesto\r\n"
                          +" on exp.exp_num = puesto.CPE_PTO_NUMEXP and exp.exp_eje = puesto.CPE_PTO_EXP_EJE\r\n"
                          +" left join "+tablaOferta+" oferta on puesto.CPE_PTO_COD_PUESTO = oferta.CPE_OFE_COD_PUESTO and puesto.CPE_PTO_NUMEXP = oferta.CPE_OFE_NUMEXP\r\n"
                          +" LEFT JOIN CPE_JUSTIFICACION  ON EXP.EXP_NUM = CPE_JUS_NUMEXP AND EXP.EXP_EJE = CPE_JUS_EXP_EJE AND \r\n"
                          +" CPE_JUS_ID_OFERTA = CPE_OFE_ID_OFERTA AND CPE_JUS_COD_PUESTO  = puesto.CPE_PTO_COD_PUESTO      \r\n"
                    +" left join (select * from "+tablaEdesval+" where DES_COD = '"+codigoResultado+"') res on puesto.CPE_PTO_COD_RESULT = res.DES_VAL_COD\r\n"
                          +" LEFT JOIN (select DES_NOM VALOR,DES_VAL_COD from "+tablaEdesval+" where DES_COD = '"+codigoNivForm+"' ) NFOR ON NFOR.DES_VAL_COD = puesto.CPE_PTO_COD_NIV_FORM\r\n"
                          +" LEFT JOIN (select PAI_NOL VALOR,PAI_COD from "+GlobalNames.ESQUEMA_GENERICO+tablaPais+") PAI1 ON PAI1.PAI_COD= puesto.CPE_PTO_PAI_COD_1\r\n"
                          +" LEFT JOIN (select PAI_NOL VALOR,PAI_COD from "+GlobalNames.ESQUEMA_GENERICO+tablaPais+") PAI2 ON PAI2.PAI_COD= puesto.CPE_PTO_PAI_COD_2\r\n"
                          +" LEFT JOIN (select PAI_NOL VALOR,PAI_COD from "+GlobalNames.ESQUEMA_GENERICO+tablaPais+") PAI3 ON PAI3.PAI_COD= puesto.CPE_PTO_PAI_COD_3\r\n"
                          +" LEFT JOIN (select tnu_num, tnu_valor valor from "+tablaNumero+" where TNU_COD = '"+codigoResNumPuesto+"' and TNU_eje='"+ejercicio+"')PUES_RENUN ON PUES_RENUN.tnu_num=exp.exp_num\r\n"
                          +" LEFT JOIN (select tnu_num, tnu_valor valor from "+tablaNumero+" where TNU_COD = '"+codigoResImpRenun+"' and TNU_eje='"+ejercicio+"')IMP_RENUN ON IMP_RENUN.tnu_num=exp.exp_num\r\n"

                          +" where\r\n"
                          +" --exp.exp_est <> '"+ConstantesMeLanbide39.ESTADO_EXPEDIENTE_ANULADO+"' AND\r\n"
                          +" EXP.exp_num like '%"+ConstantesMeLanbide39.COD_PROCEDIMIENTO+"%'\r\n"
                          +" and EXP.EXP_EJE = '"+ejercicio+"'"
                          +" and (CPE_OFE_ID_OFERTA=1  or CPE_OFE_ID_OFERTA is null)"
                          +" )TOTAL\r\n"
                          +" LEFT JOIN (select tnu_num, tnu_valor valor from "+tablaNumero+" where TNU_COD = '"+codigoResModImpConc+"' and TNU_eje='"+ejercicio+"')CONC_RES ON CONC_RES.tnu_num=TOTAL.n_expediente\r\n"
                          +" LEFT JOIN (select tnu_num, tnu_valor valor from "+tablaNumero+" where TNU_COD = '"+codigoMinimiC+"' and TNU_eje='"+ejercicio+"')MINIMIS ON MINIMIS.tnu_num=TOTAL.n_expediente\r\n"
                          +" GROUP BY  orden, n_expediente, COD_PUESTO, ENTIDAD, PROVINCIA, CNAE09, N_PUESTOS_SOLICITADOS, PUESTO, RESULTADO, FEC_INI, FEC_FIN, PERIODO_MESES_SUBVENCIONABLES,\r\n"
                          +" TITULACION_AB,PAIS1,PAIS2,PAIS3,COSTES_CONT_SALARIO_SS,COSTES_CONT_DIETAS_CONVE, SOL_SALARIO_SS,SOL_DIETAS_CONV_SS,SOL_GASTOS_TRA,DOTACION_ANUAL_MAXIMA,MESES_SUBVENCIONABLES,DIETAS_SUBV,GASTOS_TRAM,\r\n"
                          +" MOTIVO,\r\n"
                          +" CONC_RES.VALOR,NVL(MINIMIS.VALOR,0) )\r\n"
                          +" order by N_EXPEDIENTE, ORDEN,COD_PUESTO";
            
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            String sol ="";
            String salarioSS="";
            String dietas="";
            String gastos="";
            
            while(rs.next())
            {          
                retList.add((FilaInformeResumenEconomicoVO)MeLanbide39MappingUtils.getInstance().map(rs, FilaInformeResumenEconomicoVO.class));
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
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
        log.error("SALE DE DAO:getDatosInformeResumenEconomico");
        return retList;        
    }


    public List<String> getNumerosExpedientes(Integer ejercicio, Connection con) throws Exception
    {
        List<String> retList = new ArrayList<String>();
        
        ResultSet rs = null;
        Statement st = null;
        try
        {
            String tablaPuesto = ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_PUESTO, ConstantesMeLanbide39.FICHERO_PROPIEDADES);
            
            String query = "select distinct CPE_PTO_NUMEXP from "+tablaPuesto+" where CPE_PTO_EXP_EJE = "+ejercicio+" order by CPE_PTO_NUMEXP";
            
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while(rs.next())
            {
                retList.add(rs.getString("CPE_PTO_NUMEXP"));
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
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
        return retList;
    }
    
    public int actualizarOfertasPuestoPorRenuncia(CpePuestoVO p, boolean pasarARenuncia, Connection con) throws Exception
    {
        Statement st = null;
        try
        {
            String query = null;
            if(pasarARenuncia)
            {
                query = "update "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_OFERTA, ConstantesMeLanbide39.FICHERO_PROPIEDADES)
                       +" set CPE_OFE_FEC_BAJA = SYSDATE,"
                       +" CPE_OFE_COD_CAUSA_BAJA = '"+ConstantesMeLanbide39.CODIGO_CAUSA_BAJA_RENUNCIA+"'"
                       +" where CPE_OFE_COD_PUESTO = '"+p.getCodPuesto()+"'"
                       +" and CPE_OFE_EXP_EJE = "+p.getEjercicio()
                       +" and CPE_OFE_NUMEXP = '"+p.getNumExp()+"'";
            }
            else
            {
                query = "update "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_OFERTA, ConstantesMeLanbide39.FICHERO_PROPIEDADES)
                       +" set CPE_OFE_FEC_BAJA = null,"
                       +" CPE_OFE_COD_CAUSA_BAJA = null"
                       +" where CPE_OFE_COD_PUESTO = '"+p.getCodPuesto()+"'"
                       +" and CPE_OFE_EXP_EJE = "+p.getEjercicio()
                       +" and CPE_OFE_NUMEXP = '"+p.getNumExp()+"'";
            }
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            return st.executeUpdate(query);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error actualizando ofertas por renuncia para el puesto  "+(p != null ? p.getCodPuesto() : "puesto = null")+" y el expediente "+(p != null ? p.getNumExp() : "puesto = null"), ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                if(st != null)
                    st.close();
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    
    public int actualizarJustificacionesPuestoPorRenuncia(CpePuestoVO p, boolean pasarARenuncia, Connection con) throws Exception
    {
        Statement st = null;
        try
        {
            String query = null;
            if(pasarARenuncia)
            {
                query = "update "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_JUSTIFICACION, ConstantesMeLanbide39.FICHERO_PROPIEDADES)
                       +" set CPE_JUS_COD_ESTADO = '"+ConstantesMeLanbide39.CODIGO_ESTADO_RENUNCIA+"',"
                       +" CPE_JUS_IMP_JUSTIFICADO = 0"
                       +" where CPE_JUS_COD_PUESTO = '"+p.getCodPuesto()+"'"
                       +" and CPE_JUS_EXP_EJE = "+p.getEjercicio()
                       +" and CPE_JUS_NUMEXP = '"+p.getNumExp()+"'";
            }
            else
            {
                query = "update "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_JUSTIFICACION, ConstantesMeLanbide39.FICHERO_PROPIEDADES)
                       +" set CPE_JUS_COD_ESTADO = null,"
                       +" CPE_JUS_IMP_JUSTIFICADO = 0"
                       +" where CPE_JUS_COD_PUESTO = '"+p.getCodPuesto()+"'"
                       +" and CPE_JUS_EXP_EJE = "+p.getEjercicio()
                       +" and CPE_JUS_NUMEXP = '"+p.getNumExp()+"'";
            }
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            return st.executeUpdate(query);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error actualizando ofertas por renuncia para el puesto  "+(p != null ? p.getCodPuesto() : "puesto = null")+" y el expediente "+(p != null ? p.getNumExp() : "puesto = null"), ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                if(st != null)
                    st.close();
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    
    public int eliminarOfertasPuesto(CpePuestoVO p, Connection con) throws Exception
    {
        Statement st = null;
        try
        {
            String query = null;
                query = "delete from "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_OFERTA, ConstantesMeLanbide39.FICHERO_PROPIEDADES)
                       +" where CPE_OFE_COD_PUESTO = '"+p.getCodPuesto()+"'"
                       +" and CPE_OFE_EXP_EJE = "+p.getEjercicio()
                       +" and CPE_OFE_NUMEXP = '"+p.getNumExp()+"'";
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            return st.executeUpdate(query);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error actualizando ofertas por renuncia para el puesto  "+(p != null ? p.getCodPuesto() : "puesto = null")+" y el expediente "+(p != null ? p.getNumExp() : "puesto = null"), ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                if(st != null)
                    st.close();
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    
    public int eliminarJustificacionesPuesto(CpePuestoVO p, Connection con) throws Exception
    {
        Statement st = null;
        try
        {
            String query = null;
                query = "delete from "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_JUSTIFICACION, ConstantesMeLanbide39.FICHERO_PROPIEDADES)
                       +" where CPE_JUS_COD_PUESTO = '"+p.getCodPuesto()+"'"
                       +" and CPE_JUS_EXP_EJE = "+p.getEjercicio()
                       +" and CPE_JUS_NUMEXP = '"+p.getNumExp()+"'";
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            return st.executeUpdate(query);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error actualizando ofertas por renuncia para el puesto  "+(p != null ? p.getCodPuesto() : "puesto = null")+" y el expediente "+(p != null ? p.getNumExp() : "puesto = null"), ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                if(st != null)
                    st.close();
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    
    private BigDecimal calcularImportePuestosBaja(String numExpediente, Integer ejercicio, String codigoEstado, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        try
        {
            BigDecimal retVal = new BigDecimal("0");
            String query = "select * from "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_OFERTA, ConstantesMeLanbide39.FICHERO_PROPIEDADES)+" o"
                            +" inner join "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_PUESTO, ConstantesMeLanbide39.FICHERO_PROPIEDADES)+" p"
                            +" on o.CPE_OFE_COD_PUESTO = CPE_PTO_COD_PUESTO and o.CPE_OFE_NUMEXP = p.CPE_PTO_NUMEXP"
                            +" where CPE_OFE_NUMEXP = '"+numExpediente+"'"
                            +" and CPE_OFE_EXP_EJE = "+ejercicio
                            +" and CPE_OFE_NIF is not null"
                            +" and CPE_OFE_FEC_BAJA is not null"
                            +" and CPE_OFE_COD_CAUSA_BAJA = '"+codigoEstado+"'";
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            List<CpeOfertaVO> ofertasEnEstado = new ArrayList<CpeOfertaVO>();
            List<String> codigosPuesto = new ArrayList<String>();
            List<String> codigosDescartados = new ArrayList<String>();
            while(rs.next())
            {
                ofertasEnEstado.add((CpeOfertaVO)MeLanbide39MappingUtils.getInstance().map(rs, CpeOfertaVO.class));
            }
            if(ofertasEnEstado.size() > 0)
            {
                codigosPuesto = buscarPuestosDeOfertaBaja(ofertasEnEstado, 0, codigosPuesto, codigosDescartados, codigoEstado, con);
            }
            CpePuestoVO puesto = null;
            for(String str : codigosPuesto)
            {
                puesto = new CpePuestoVO();
                puesto.setEjercicio(ejercicio);
                puesto.setNumExp(numExpediente);
                puesto.setCodPuesto(str);
                puesto = this.getPuestoPorCodigoYExpediente(puesto, con);
                if(puesto != null && puesto.getImpMaxSuv() != null)
                {
                    retVal = retVal.add(puesto.getImpMaxSuv());
                }
            }
            return retVal;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            return new BigDecimal("0");
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
    
    private List<String> buscarPuestosDeOfertaBaja(List<CpeOfertaVO> ofertas, int pos, List<String> codigosPuesto, List<String> codigosDescartados, String codigoEstado, Connection con) throws Exception
    {
        if(ofertas != null)
        {
            CpeOfertaVO oferta = null;
            List<CpeOfertaVO> listaHijos = null;
            while(pos < ofertas.size())
            {
                oferta = ofertas.get(pos);
                if(((oferta.getFecBaja() != null && oferta.getCodCausaBaja() != null && oferta.getCodCausaBaja().equalsIgnoreCase(codigoEstado)) || (oferta.getNif() == null && oferta.getIdOfertaOrigen() != null)) && !codigosPuesto.contains(oferta.getCodPuesto()))
                {
                    if(!codigosDescartados.contains(oferta.getCodPuesto()))
                    {
                        listaHijos = getOfertasSustitucion(oferta, con);
                        if(listaHijos != null && listaHijos.size() > 0)
                        {
                            codigosPuesto = buscarPuestosDeOfertaBaja(listaHijos, 0, codigosPuesto, codigosDescartados, codigoEstado, con);
                        }
                        else
                        {
                            codigosPuesto.add(oferta.getCodPuesto());
                        }
                    }
                }
                else
                {
                    codigosDescartados.add(oferta.getCodPuesto());
                }
                pos++;
            }
        }
        return codigosPuesto;
    }
    
    private List<CpeOfertaVO> getOfertasSustitucion(CpeOfertaVO ofertaOrigen, Connection con) throws Exception
    {
        List<CpeOfertaVO> retList = new ArrayList<CpeOfertaVO>();
        Statement st = null;
        ResultSet rs = null;
        try
        {
            String query = "select * from "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_OFERTA, ConstantesMeLanbide39.FICHERO_PROPIEDADES)
                            +" where CPE_OFE_NUMEXP = '"+ofertaOrigen.getNumExp()+"'"
                            +" and CPE_OFE_EXP_EJE = "+ofertaOrigen.getExpEje()
                            +" and CPE_OFE_COD_PUESTO = '"+ofertaOrigen.getCodPuesto()+"'"
                            +" and CPE_OFE_ID_OFERTA_ORIGEN = "+ofertaOrigen.getIdOferta();
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while(rs.next())
            {
                retList.add((CpeOfertaVO)MeLanbide39MappingUtils.getInstance().map(rs, CpeOfertaVO.class));
            }
            return retList;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            return new ArrayList<CpeOfertaVO>();
        }
        finally
        {
            try
            {
                if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                if(st != null)
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
    
     public int obtenerNumMeses(String fini, String ffin, Connection con) throws Exception
    {
        Statement st = null;
        int nummeses=0;
        try
        {
            String query = null;
            //query = "select floor(months_between('"+ffin+"','"+fini+"')) as meses from dual";    
            query = "select floor(months_between(to_date('"+ffin+"', 'dd/mm/yyyy')+1,to_date('"+fini+"', 'dd/mm/yyyy'))) as meses from dual";    
            
           
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next())
            {
                 nummeses =rs.getInt("meses");
               
            }
            return nummeses;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error consultando nÂº meses.  (ffin = "+ffin+" fini = "+fini+")", ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                if(st != null)
                    st.close();
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    
    public CpeOfertaVO getUltimaOfertaPorPuestoYExpediente(CpeOfertaVO p, Connection con) throws Exception
    {
        if(p != null && p.getCodPuesto() != null && p.getNumExp() != null)
        {
            Statement st = null;
            ResultSet rs = null;
            CpeOfertaVO oferta = null;
            try
            {
                /*String query = "select * from "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_OFERTA, ConstantesMeLanbide39.FICHERO_PROPIEDADES)
                              +" where CPE_OFE_ID_OFERTA = '"+p.getIdOferta()+"'"
                              +" and CPE_OFE_COD_PUESTO = '"+(p.getCodPuesto() != null ? p.getCodPuesto() : "")+"'"
                              +" and CPE_OFE_EXP_EJE = "+(p.getExpEje() != null ? p.getExpEje().toString() : "")
                              +" and CPE_OFE_NUMEXP = '"+p.getNumExp()+"'";*/
                
                String query = "select o1.* from "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_OFERTA, ConstantesMeLanbide39.FICHERO_PROPIEDADES)+" o1"
                            +" inner join (select max(CPE_OFE_ID_OFERTA) CPE_OFE_ID_OFERTA from "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_OFERTA, ConstantesMeLanbide39.FICHERO_PROPIEDADES)
                            +" where CPE_OFE_COD_PUESTO = '"+(p.getCodPuesto() != null ? p.getCodPuesto() : "")+"'"
                            +" and CPE_OFE_EXP_EJE = "+(p.getExpEje() != null ? p.getExpEje().toString() : "")
                            +" and CPE_OFE_NUMEXP = '"+p.getNumExp()+"'"
                            +") o2"
                            +" on o1.CPE_OFE_ID_OFERTA = o2.CPE_OFE_ID_OFERTA"
                            +" where CPE_OFE_COD_PUESTO = '"+(p.getCodPuesto() != null ? p.getCodPuesto() : "")+"'"
                            +" and CPE_OFE_EXP_EJE = "+(p.getExpEje() != null ? p.getExpEje().toString() : "")
                            +" and CPE_OFE_NUMEXP = '"+p.getNumExp()+"'";
                if(log.isDebugEnabled()) 
                    log.debug("sql = " + query);
                st = con.createStatement();
                rs = st.executeQuery(query);
                if(rs.next())
                {
                    oferta = (CpeOfertaVO)MeLanbide39MappingUtils.getInstance().map(rs, CpeOfertaVO.class);
                }
                return oferta;
            }
            catch(Exception ex)
            {
                log.error("Se ha producido un error recuperando oferta "+(p != null ? p.getIdOferta() : "(oferta = null)")+" para puesto "+(p != null ? p.getCodPuesto() : "(oferta = null)")+" expediente "+(p != null ? p.getNumExp() : "(oferta = null)"), ex);
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
        else
        {
            return null;
        }
    }
    
    public List<FilaPersonaContratadaVO> getListaContratacionesPuesto(CpePuestoVO p, Connection con) throws Exception
    {
        List<FilaPersonaContratadaVO> retList = new ArrayList<FilaPersonaContratadaVO>();
        if(p != null && p.getCodPuesto() != null && p.getNumExp() != null)
        {
            Statement st = null;
            ResultSet rs = null;
            try
            {
                String query = "select CPE_OFE_ID_OFERTA, CPE_JUS_ID_JUSTIFICACION, CPE_OFE_NIF, CPE_OFE_NOMBRE, "
                        + "CPE_OFE_APE1, CPE_OFE_APE2, CPE_OFE_FEC_INI, CPE_OFE_FEC_FIN, "
                        + "CPE_JUS_IMP_JUSTIFICADO as IMP_JUSTIF, CPE_JUS_SALARIOSUB as SALARIO_SUB, CPE_JUS_DIETAS as DIETAS_JUSTI, " +
                        "CPE_JUS_GASTOSGES as GASTOS_GESION, CPE_JUS_BONIF as BONIF, " +
                        "CPE_JUS_BASCOT_CC as BASE_COTIZACION, CPE_JUS_COEF_TGSS as COEF_APLICADO, " +
                        "CPE_JUS_FOGASA  as PORC_FOGASA, CPE_JUS_AT as PORC_COEFICIENTE, " +
                        "CPE_JUS_EPSV as PORC_APORTACION"
                               +" from "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_OFERTA, ConstantesMeLanbide39.FICHERO_PROPIEDADES)+" o"
                               +" inner join "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_JUSTIFICACION, ConstantesMeLanbide39.FICHERO_PROPIEDADES)+" j"
                               +" on o.CPE_OFE_COD_PUESTO = j.CPE_JUS_COD_PUESTO and o.CPE_OFE_ID_OFERTA = j.CPE_JUS_ID_OFERTA"
                               +" and o.CPE_OFE_EXP_EJE = j.CPE_JUS_EXP_EJE and o.CPE_OFE_NUMEXP = j.CPE_JUS_NUMEXP"
                               +" where CPE_OFE_COD_PUESTO = '"+p.getCodPuesto()+"' and CPE_OFE_EXP_EJE = "+p.getEjercicio()+" and CPE_OFE_NUMEXP = '"+p.getNumExp()+"' and CPE_OFE_NIF is not null"
                               +" order by CPE_OFE_COD_OFERTA, CPE_OFE_ID_OFERTA";
                if(log.isDebugEnabled()) 
                    log.debug("sql = " + query);
                st = con.createStatement();
                rs = st.executeQuery(query);
                
                while(rs.next())
                {
                    retList.add((FilaPersonaContratadaVO)MeLanbide39MappingUtils.getInstance().map(rs, FilaPersonaContratadaVO.class));
                }
            }
            catch(Exception ex)
            {
                log.error("Se ha producido un error recuperando contrataciones para puesto "+(p != null ? p.getCodPuesto() : "(oferta = null)")+" expediente "+(p != null ? p.getNumExp() : "(oferta = null)"), ex);
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
        return retList;
    }
    
    public List<FilaResultadoBusqTitulaciones> buscarTitulaciones(String codigo, String descripcion, Connection con) throws Exception
    {
        List<FilaResultadoBusqTitulaciones> retList = new ArrayList<FilaResultadoBusqTitulaciones>();
        Statement st = null;
        ResultSet rs = null;
        try
        {
            
            
            String filtroCod = codigo != null && !codigo.equals("") ? codigo : "";
            String filtroDesc = descripcion != null && !descripcion.equals("") ? "%"+descripcion+"%" : "%";
            
                       
            String query = "select * from "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_GEN_TITULACIONES, ConstantesMeLanbide39.FICHERO_PROPIEDADES)
                            +" where upper(GEN_TIT_DESC_TITULAC_C) like '"+filtroDesc.toUpperCase()+"'";
            if (!filtroCod.equals("")) query+=" and upper(GEN_TIT_COD_TITULAC)='"+filtroCod+"'";
                        
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);

            while(rs.next())
            {
                retList.add((FilaResultadoBusqTitulaciones)MeLanbide39MappingUtils.getInstance().map(rs, FilaResultadoBusqTitulaciones.class));
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error buscando titulaciones con codigo =  "+codigo+" descripcion = "+descripcion, ex);
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
        return retList;
    }
    
    public String getDescripcionCampoDesplegable(String codigoCampo, String valorDesplegable, Connection con) throws Exception
    {
        String retStr = "";
        Statement st = null;
        ResultSet rs = null;
        try
        {
            
            String query = "select DES_NOM from "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_E_DES_VAL, ConstantesMeLanbide39.FICHERO_PROPIEDADES)
                          +" where DES_COD = '"+ConfigurationParameter.getParameter(codigoCampo, ConstantesMeLanbide39.FICHERO_PROPIEDADES)+"'"
                          +" and DES_VAL_COD = '"+valorDesplegable+"'";
            
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);

            if(rs.next())
            {
                String temp = rs.getString("DES_NOM");
                retStr = temp != null ? temp : "";
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando descripcion de campo desplegable con codigoCampo =  "+codigoCampo+" y valorDesplegable = "+valorDesplegable, ex);
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
        return retStr;
    }
    
    
    public String getDescripcionTitulacion(String codigo, Connection con) throws Exception
    {
        String retStr = "";
        Statement st = null;
        ResultSet rs = null;
        try
        {
            
            String query = "select GEN_TIT_COD_TITULAC||'-'||GEN_TIT_DESC_TITULAC_C  as TITULACION from "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_GEN_TITULACIONES, ConstantesMeLanbide39.FICHERO_PROPIEDADES)
                          +" where GEN_TIT_COD_TITULAC = '"+codigo+"'";
            
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);

            if(rs.next())
            {
                String temp = rs.getString("TITULACION");
                retStr = temp != null ? temp : "";
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando descripcion de TITULACION con codigo =  "+codigo, ex);
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
        return retStr;
    }
    
    public Long getTramiteActualExpediente(int codOrganizacion, int ejercicio, String numExp, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        Long tram = null;
        try
        {
            String query = null;
            query = "select MAX(CRO_TRA) as CRO_TRA from " + ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_E_CRO, ConstantesMeLanbide39.FICHERO_PROPIEDADES)
                        +" where CRO_PRO = '"+ConstantesMeLanbide39.COD_PROCEDIMIENTO+"'"
                        +" and CRO_EJE = "+ejercicio+" and CRO_NUM = '"+numExp+"' and CRO_OCU = 1 and CRO_FEF is null";
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                tram = rs.getLong("CRO_TRA");
                if(rs.wasNull())
                    tram = null;
            }
            return tram;
        }
        catch(Exception ex)
        {
            con.rollback();
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
    }    
    
    public Long obtenerCodigoInternoTramite(Integer entorno, String codTramite, Connection con)throws Exception
    {
        Long cod=0L;
        Statement st = null;
        ResultSet rs = null;
        try
        {
            String sql = "select TRA_COD from "+ ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_TRAMITES, ConstantesMeLanbide39.FICHERO_PROPIEDADES)
                        +" where TRA_MUN = "+entorno
                        +" and TRA_PRO = '"+ConstantesMeLanbide39.COD_PROCEDIMIENTO+"'"
                        +" and TRA_COU = "+ConfigurationParameter.getParameter(codTramite, ConstantesMeLanbide39.FICHERO_PROPIEDADES);

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
    
    public boolean tieneTramiteIniciado(int codOrganizacion, int ejercicio, String numExp, Long codTramite, Connection con) throws Exception
    {
        boolean tramiteIniciado = false;
        Statement st = null;
        ResultSet rs = null;
        try
        {
            String sql = "select * from "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_E_CRO, ConstantesMeLanbide39.FICHERO_PROPIEDADES)
                    +" where CRO_MUN = "+codOrganizacion
                    +" and CRO_PRO = '"+ConstantesMeLanbide39.COD_PROCEDIMIENTO+"'"
                    +" and CRO_EJE = "+ejercicio
                    +" and CRO_NUM = '"+numExp+"'"
                    +" and CRO_TRA = "+codTramite;

            if(log.isDebugEnabled()) 
                    log.debug("sql = " + sql);
            
            st = con.createStatement();
            rs = st.executeQuery(sql);
            if(rs.next())
            {
                tramiteIniciado = true;
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando el código interno del trámite : "+codTramite, ex);
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
    	return tramiteIniciado;
    }
    
    public boolean tieneTramiteFinalizado(int codOrganizacion, int ejercicio, String numExp, Long codTramite, Connection con) throws Exception
    {
        boolean tramiteIniciado = false;
        Statement st = null;
        ResultSet rs = null;
        try
        {
            String sql = "select * from "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_E_CRO, ConstantesMeLanbide39.FICHERO_PROPIEDADES)
                    +" where CRO_MUN = "+codOrganizacion
                    +" and CRO_PRO = '"+ConstantesMeLanbide39.COD_PROCEDIMIENTO+"'"
                    +" and CRO_EJE = "+ejercicio
                    +" and CRO_NUM = '"+numExp+"'"
                    +" and CRO_TRA = "+codTramite
                    +" and CRO_FEF is not null";

            if(log.isDebugEnabled()) 
                    log.debug("sql = " + sql);
            
            st = con.createStatement();
            rs = st.executeQuery(sql);
            if(rs.next())
            {
                tramiteIniciado = true;
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando el código interno del trámite : "+codTramite, ex);
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
    	return tramiteIniciado;
    }
    
    
    public List<FilaInformeDatosPuestosVO> getInformeDatosPuestos(int ejercicio, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        int anoExpediente = ejercicio;//MeLanbide39Utils.getEjercicioDeExpediente(numExpediente);
        List<FilaInformeDatosPuestosVO> informePuestos = new ArrayList<FilaInformeDatosPuestosVO>();
        FilaInformeDatosPuestosVO res = new FilaInformeDatosPuestosVO();
        try
        {
            String query = null;
            String tablaEdesval = ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_E_DES_VAL, ConstantesMeLanbide39.FICHERO_PROPIEDADES);
            String codigoRes = ConfigurationParameter.getParameter(ConstantesMeLanbide39.CAMPO_SUPL_RESULTADO, ConstantesMeLanbide39.FICHERO_PROPIEDADES);
            
                //Leire, se comentan las línas en las que se realicen cálculos con los días trabajados
                query = "select distinct CPE_OFE_NUMEXP,CPE_JUS_COD_PUESTO, CPE_JUS_ID_OFERTA,"+
                         //"HTE_NOM ||' ' || HTE_AP1||' ' || HTE_AP2 as entidad, \n" +
                        " CASE WHEN ROLSUBR.HTE_NOM IS NOT NULL THEN \n " +
                        " ROLSOLI.HTE_NOM ||' ' || ROLSOLI.HTE_AP1||' ' || ROLSOLI.HTE_AP2||'(inicial), '|| CHR(10) || ROLSUBR.HTE_NOM ||' ' || ROLSUBR.HTE_AP1||' ' || ROLSUBR.HTE_AP2||'(subrogado)' \n" +
                        " ELSE ROLSOLI.HTE_NOM ||' ' || ROLSOLI.HTE_AP1||' ' || ROLSOLI.HTE_AP2 \n" +
                        " end as ENTIDAD, "+
                        "  CASE\n" +
                        "    WHEN CPE_OFE_SEXO = 'H'\n" +
                        "    THEN 'Hombre'\n" +
                        "    WHEN CPE_OFE_SEXO = 'M'\n" +
                        "    THEN 'Mujer'\n" +
                        "  END AS SEXO,"
                        +"case"
                          +" when res.DES_VAL_COD IN ('"+ConstantesMeLanbide39.CODIGO_RESULTADO_RENUNCIA+"') then null\r\n"
                          +" when CPE_OFE_ID_OFERTA is null then null\r\n"
                          +" when CPE_OFE_ID_OFERTA = 1 then CPE_OFE_NOMBRE || ' ' || CPE_OFE_APE1 || ' '||coalesce(CPE_OFE_APE2,'')\r\n"
                          +" else 'SUSTITUTO - '||CPE_OFE_NOMBRE || ' ' || CPE_OFE_APE1 || ' '||coalesce(CPE_OFE_APE2,'')\r\n"
                          +" end as NOMBRE_APELLIDOS"
                        //+ " CPE_OFE_NOMBRE || ' ' || CPE_OFE_APE1 || ' '||coalesce(CPE_OFE_APE2,'') NOMBRE_APELLIDOS"
                        + " ,N.DES_NOM CPE_OFE_COD_NIV_FORM"
                        + " ,CPE_OFE_NIF, ACTIVIDAD"
                        + " ,CASE WHEN CPE_PTO_PAI_COD_1 IS NOT NULL THEN CPE_PTO_PAI_COD_1||'-'||pai1.PAI_NOM ELSE ' ' END PAIS_SOLICITADO1"
                        + " ,CASE WHEN CPE_PTO_PAI_COD_2 IS NOT NULL THEN CPE_PTO_PAI_COD_2||'-'||pai2.PAI_NOM ELSE ' ' END PAIS_SOLICITADO2"
                        + " ,CASE WHEN CPE_PTO_PAI_COD_3 IS NOT NULL THEN CPE_PTO_PAI_COD_3||'-'||pai3.PAI_NOM ELSE ' ' END PAIS_SOLICITADO3"
                        + " ,CASE WHEN CPE_OFE_PAI_COD_1 IS NOT NULL THEN CPE_OFE_PAI_COD_1||'-'||pai4.PAI_NOM ELSE ' ' END PAIS_FINAL1"
                        + " ,CASE WHEN CPE_OFE_PAI_COD_2 IS NOT NULL THEN CPE_OFE_PAI_COD_2||'-'||pai5.PAI_NOM ELSE ' ' END PAIS_FINAL2"
                        + " ,CASE WHEN CPE_OFE_PAI_COD_3 IS NOT NULL THEN CPE_OFE_PAI_COD_3||'-'||pai6.PAI_NOM ELSE ' ' END PAIS_FINAL3"
                        + " ,CPE_JUS_MESES_EXT"
                        + " ,CPE_OFE_FEC_INI "
                        +" ,CASE WHEN CPE_OFE_FEC_FIN > TO_DATE('31/10/"+(anoExpediente+1)+"', 'dd/MM/yyyy') THEN TO_DATE('31/10/"+(anoExpediente+1)+"', 'dd/MM/yyyy') ELSE CPE_OFE_FEC_FIN END CPE_OFE_FEC_FIN"
                        + " ,CPE_PTO_SALARIO_ANEX_TOT SALARIO_ANEXO"
                        + " ,CPE_JUS_MINORACION MINORACION, CPE_JUS_DIAS_TRAB, CPE_JUS_DIAS_SEGSOC "
                        + " ,CPE_JUS_BASCOT_CC BC_CC_EN_PERI, CPE_OFE_FEC_BAJA "
                        + " ,CPE_JUS_COEF_TGSS COEFICIENTE_TGSS"
                        + " ,CPE_JUS_BASCOT_CC *CPE_JUS_COEF_TGSS/100 SS_EMPRESA"
                        + " ,CPE_JUS_BASCOT_AT  BC_AT "
                        + " ,CPE_JUS_FOGASA COEFICIENTE_FOGASA"
                        + " ,CPE_JUS_AT COEFICIENTE_AT"
                        + " ,CPE_PTO_MESES_MANUT"
                        + " , CPE_JUS_BASCOT_AT *(NVL(CPE_JUS_FOGASA,0)+NVL(CPE_JUS_AT,0))/100 FOGASA_AT_EMPRESA "
                        + " , CPE_JUS_EPSV PORC_EPSV"
                        + " , CPE_JUS_APORTEPSV APORTACIONES_EPSV_EMPRESA "
                        + " , (CPE_JUS_BASCOT_CC *CPE_JUS_COEF_TGSS/100)+(CPE_JUS_BASCOT_at *(NVL(CPE_JUS_FOGASA,0)+NVL(CPE_JUS_AT,0))/100)+NVL(CPE_JUS_APORTEPSV,0)TOTAL_EMPRESA "
                        + " , (CPE_JUS_SALARIO )+(CPE_JUS_BASCOT_CC *CPE_JUS_COEF_TGSS/100)+(CPE_JUS_BASCOT_at *(NVL(CPE_JUS_FOGASA,0)+NVL(CPE_JUS_AT,0))/100)+NVL(CPE_JUS_APORTEPSV,0)TOTAL_PERIODO "
                        //redondear cada calculo a dos decimales para que coincida con pantalla de justificación
                        //+ " , (CPE_JUS_SALARIO )+ROUND((CPE_JUS_BASCOT_CC *CPE_JUS_COEF_TGSS/100),2)+ROUND((CPE_JUS_BASCOT_at *(NVL(CPE_JUS_FOGASA,0)+NVL(CPE_JUS_AT,0))/100),2)+NVL(CPE_JUS_APORTEPSV,0)TOTAL_PERIODO "
                        + " ,(CPE_PTO_SALARIO_ANEX_TOT - CPE_JUS_MINORACION) SUBV_MINORADA_SALAR_SSOCIAL"
                        //redondear cada calculo a dos decimales para que coincida con pantalla de justificación
                        + " , NVL(CPE_JUS_SALARIO,0 )+ROUND(NVL(CPE_JUS_BASCOT_CC *CPE_JUS_COEF_TGSS/100,0),2)+ROUND(NVL(CPE_JUS_BASCOT_at *(NVL(CPE_JUS_FOGASA,0)+NVL(CPE_JUS_AT,0))/100,0),2)+NVL(CPE_JUS_APORTEPSV,0)SALARIO_PERIODO "

                        + " , CPE_JUS_DIETAS DIETAS_ABONADAS_PERIODO"
                        + " , CPE_PTO_DIETAS_MANUT DIETAS_CONCEDIDAS_PERIODO"
                        + " , CASE WHEN CPE_JUS_DIETAS >=CPE_PTO_DIETAS_MANUT THEN CPE_PTO_DIETAS_MANUT"
                        + " ELSE CPE_JUS_DIETAS END MAX_SUB_DIETAS"
                        + " ,CPE_JUS_GASTOSGES VIS_SEG_ABONADO"
                        + " ,CPE_PTO_TRAM_SEGUROS VIS_SEG_CONCEDIDO"
                        + " , CASE WHEN CPE_JUS_GASTOSGES>=CPE_PTO_TRAM_SEGUROS THEN CPE_PTO_TRAM_SEGUROS ELSE CPE_JUS_GASTOSGES END VIS_SEG_SUBV"
                        + " ,CPE_JUS_BONIF BONIFICACIONES, CPE_JUS_MESES_EXT, CPE_JUS_BASCOT_AT, CPE_JUS_SALARIO,CPE_JUS_IMP_JUSTIFICADO,\n" +
                        "  CPE_PTO_IMP_MAX_SUBV, B.DES_NOM AS MOTIVO_BAJA, CPE_PTO_GASTOS_SEG, CPE_PTO_GASTOS_VIS, CPE_JUS_GASTOS_SEG, CPE_JUS_GASTOS_VIS, "
                        + "nvl(case when nvl(CPE_PTO_IMP_MAX_SUBV, 999999999) > nvl(CPE_PTO_IMP_TOT_SOLIC, 9999999999) then CPE_PTO_IMP_TOT_SOLIC else CPE_PTO_IMP_MAX_SUBV end, 0) AS IMPORTE \n" 
                        + " from "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_PUESTO, ConstantesMeLanbide39.FICHERO_PROPIEDADES) +" p "
                        //+ " INNER JOIN E_EXT ON EXT_NUM = Cpe_Pto_Numexp AND EXT_ROL = 1 "
                        //+ " inner join T_HTE on EXT_TER=HTE_TER AND EXT_NVR = HTE_NVR"
                        //+ " LEFT JOIN T_CAMPOS_DESPLEGABLE ON COD_CAMPO ='TSEXOTERCERO' AND COD_TERCERO = HTE_TER AND COD_MUNICIPIO = EXT_MUN "
                        +" LEFT join (\n" +   
                                "select HTE_NOM,HTE_AP1,HTE_AP2,EXT_ROL,EXT_NUM   from E_EXT \n" + 
                                "INNER JOIN T_HTE ON EXT_TER=HTE_TER AND EXT_NVR = HTE_NVR \n" + 
                                ")ROLSOLI ON ROLSOLI.EXT_NUM = CPE_PTO_NUMEXP AND ROLSOLI.EXT_ROL = 1 \n" + 
                            " LEFT join (\n" +   
                                "select HTE_NOM,HTE_AP1,HTE_AP2,EXT_ROL,EXT_NUM   from E_EXT \n" + 
                                "INNER JOIN T_HTE ON EXT_TER=HTE_TER AND EXT_NVR = HTE_NVR \n" + 
                                ")ROLSUBR ON ROLSUBR.EXT_NUM = CPE_PTO_NUMEXP AND ROLSUBR.EXT_ROL = 2 \n"  
                        
                        + " Left Join ("
                        + "    Select Dbms_Lob.Substr(Ttl_Valor,4000,1) AS ACTIVIDAD,Ttl_Num, TTL_EJE"
                        + "    From E_Ttl "
                        + "    Where Ttl_Cod = 'ACTIV'"
                        + " ) ON TTL_NUM = Cpe_Pto_Numexp "
                        + " inner join "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_OFERTA, ConstantesMeLanbide39.FICHERO_PROPIEDADES) +" o on P.Cpe_Pto_Cod_Puesto=O.Cpe_Ofe_Cod_Puesto and P.Cpe_Pto_Numexp =o.CPE_OFE_NUMEXP"
                        + " inner  join "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_JUSTIFICACION, ConstantesMeLanbide39.FICHERO_PROPIEDADES) +" j on P.Cpe_Pto_Cod_Puesto=J.Cpe_Jus_Cod_Puesto and P.Cpe_Pto_Numexp =j.CPE_jus_NUMEXP and j.CPE_JUS_id_OFERTA=O.Cpe_Ofe_id_Oferta"
                        + " inner join " + GlobalNames.ESQUEMA_GENERICO + "T_PAI pai1 on pai1.PAI_COD=CPE_PTO_PAI_COD_1"
                        + " LEFT join " + GlobalNames.ESQUEMA_GENERICO + "T_PAI pai2 on pai2.PAI_COD=CPE_PTO_PAI_COD_2"
                        + " LEFT join " + GlobalNames.ESQUEMA_GENERICO + "T_PAI pai3 on pai3.PAI_COD=CPE_PTO_PAI_COD_3"
                        + " inner join " + GlobalNames.ESQUEMA_GENERICO + "T_PAI pai4 on pai4.PAI_COD=CPE_OFE_PAI_COD_1"
                        + " LEFT join " + GlobalNames.ESQUEMA_GENERICO + "T_PAI pai5 on pai5.PAI_COD=CPE_OFE_PAI_COD_2"
                        + " LEFT join " + GlobalNames.ESQUEMA_GENERICO + "T_PAI pai6 on pai6.PAI_COD=CPE_OFE_PAI_COD_3"                        
                          +" left join (select * from "+tablaEdesval+" where DES_COD = '"+codigoRes+"') res on CPE_PTO_COD_RESULT = res.DES_VAL_COD\r\n"
                        //+ " LEFT JOIN E_DES_VAL ON DES_COD = 'CBAJ' AND DES_VAL_COD = CPE_OFE_COD_CAUSA_BAJA"                        
                        + " LEFT JOIN E_DES_VAL B ON B.DES_COD = 'CBAJ' AND B.DES_VAL_COD = CPE_OFE_COD_CAUSA_BAJA"
                        + " LEFT JOIN E_DES_VAL N ON N.DES_COD = 'NFOR' AND N.DES_VAL_COD = CPE_OFE_COD_NIV_FORM"
                        + " where CPE_OFE_EXP_EJE = "+anoExpediente+""
                        + " and CPE_OFE_NIF is not null ORDER BY CPE_OFE_NUMEXP";
            
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            
            DecimalFormat df = new DecimalFormat("#.##");
            Double salarioAnexo = null;
            Double salario = null;
            Double minoracion = null;
            Double bcPeriodo = null;
            String bcAT = "";
            Double bcCC = null;
            Double coeficienteTgss = null;
            Double coeficienteFog = null;
            Double coeficienteAt = null;
            //Double porcEpsv = null;
            Double aportacionEPSV = null;
            Double dietasAbon = null;
            Double dietasConce = null;
            Double vigSegAbon = null;
            Double vigSegConce = null;
            Double bonif = null;
            Double jusEpsv = null;
            Double mesesManut = null;
            
            while(rs.next())
            {         
                int dias = 0;
                int diasSeg = 0;
                int diasC = 0;
                res = new FilaInformeDatosPuestosVO();
                //resumen.add();
                res = (FilaInformeDatosPuestosVO)MeLanbide39MappingUtils.getInstance().map(rs, FilaInformeDatosPuestosVO.class);
                
                //Leire, hacemos los cálculos relacionados con el número de días
                
                if(rs.getString("CPE_PTO_MESES_MANUT") != null)
                    mesesManut = rs.getDouble("CPE_PTO_MESES_MANUT");
                else 
                    mesesManut = 0.0;
                 
                if(rs.getString("SALARIO_ANEXO") != null)
                    salarioAnexo = rs.getDouble("SALARIO_ANEXO");
                else 
                    salarioAnexo = 0.0;
                if(rs.getString("CPE_JUS_SALARIO") != null)
                    salario = rs.getDouble("CPE_JUS_SALARIO");
                else 
                    salario = 0.0;
                
                if(rs.getString("MINORACION") != null)
                    minoracion = rs.getDouble("MINORACION");
                else 
                    minoracion = 0.0;
                if(rs.getString("BC_CC_EN_PERI") != null)
                    bcPeriodo = rs.getDouble("BC_CC_EN_PERI");
                else 
                    bcPeriodo = 0.0;
                if(rs.getString("COEFICIENTE_TGSS") != null)
                    coeficienteTgss = rs.getDouble("COEFICIENTE_TGSS");
                else 
                    coeficienteTgss = 0.0;
                if(rs.getString("COEFICIENTE_FOGASA") != null)
                    coeficienteFog = rs.getDouble("COEFICIENTE_FOGASA");
                else 
                    coeficienteFog = 0.0;
                if(rs.getString("COEFICIENTE_AT") != null)
                    coeficienteAt = rs.getDouble("COEFICIENTE_AT");
                else 
                    coeficienteAt = 0.0;
                /*if(rs.getString("PORC_EPSV") != null)
                    porcEpsv = rs.getDouble("PORC_EPSV");
                else 
                    porcEpsv = 0.0;*/
                if(rs.getString("APORTACIONES_EPSV_EMPRESA") != null)
                    aportacionEPSV = rs.getDouble("APORTACIONES_EPSV_EMPRESA");
                else 
                    aportacionEPSV = 0.0;
                
                if(rs.getString("DIETAS_ABONADAS_PERIODO") != null)
                    dietasAbon = rs.getDouble("DIETAS_ABONADAS_PERIODO");
                else 
                    dietasAbon = 0.0;
                if(rs.getString("DIETAS_CONCEDIDAS_PERIODO") != null)
                    dietasConce = rs.getDouble("DIETAS_CONCEDIDAS_PERIODO");
                else 
                    dietasConce = 0.0;
                if(rs.getString("VIS_SEG_ABONADO") != null)
                    vigSegAbon = rs.getDouble("VIS_SEG_ABONADO");
                else 
                    vigSegAbon = 0.0;
                if(rs.getString("VIS_SEG_CONCEDIDO") != null)
                    vigSegConce = rs.getDouble("VIS_SEG_CONCEDIDO");
                else 
                    vigSegConce = 0.0;
                if(rs.getString("BONIFICACIONES") != null)
                    bonif = rs.getDouble("BONIFICACIONES");
                else 
                    bonif = 0.0;
                if(rs.getString("CPE_JUS_DIAS_TRAB") != null)
                    dias = rs.getInt("CPE_JUS_DIAS_TRAB");
                if(rs.getString("CPE_JUS_DIAS_SEGSOC") != null)
                    diasSeg = rs.getInt("CPE_JUS_DIAS_SEGSOC");
                
                if(rs.getString("CPE_JUS_BASCOT_AT") != null)
                    bcAT  = rs.getString("CPE_JUS_BASCOT_AT");
                else
                    bcAT = "0";
                
                if(rs.getString("BC_CC_EN_PERI") != null)
                    bcCC  = rs.getDouble("BC_CC_EN_PERI");
                else
                    bcCC = 0.0;
                
                if(rs.getString("PORC_EPSV") != null)
                    jusEpsv  = rs.getDouble("PORC_EPSV");
                else
                    jusEpsv = 0.0;
                
                String rdo ="";
                if (rs.getString("CPE_OFE_NUMEXP")!=null && rs.getString("CPE_OFE_NIF")!=null){
                    //rdo = obtenerDiasTrabajados(rs.getString("CPE_OFE_NUMEXP"), rs.getString("CPE_OFE_NIF"), con);
                    rdo = obtenerDiasTrabajados(rs.getString("CPE_OFE_NUMEXP"),rs.getString("CPE_JUS_COD_PUESTO"), rs.getInt("CPE_JUS_ID_OFERTA"), con);
                }
                
                if(rdo != null && !rdo.equals(""))
                    diasC = Integer.parseInt(rdo);
                else
                    diasC = 0;
                if(dias == 0)
                    dias = diasC;
                if(diasSeg == 0)
                    diasSeg = diasC;
                /*if(rs.getString("CPE_JUS_MESES_EXT")== null || rs.getString("CPE_JUS_MESES_EXT").equals(""))
                    res.setMesesExt("0");
                else
                    res.setMesesExt(rs.getString("CPE_JUS_MESES_EXT"));*/
                
                if(rs.getString("CPE_PTO_MESES_MANUT")== null || rs.getString("CPE_PTO_MESES_MANUT").equals(""))
                    res.setMesesExt("0");
                else
                    res.setMesesExt(rs.getString("CPE_PTO_MESES_MANUT"));
                
                res.setDiasSubv(String.valueOf(diasSeg));
                
                Double dato2 = salario + (bcCC * coeficienteTgss/100);
                res.setSalarioAnualBrutoJus(String.valueOf(dato2));
                
                //dato2 = dato2 - bonif;
                DecimalFormat df2 = new DecimalFormat("#.##");
                Double salarioSSBonifD=Double.parseDouble(res.getMaxSubvencion().replace(",", "."))-bonif;
                res.setSalarioSSBonif(String.valueOf(df2.format(salarioSSBonifD)).replace(",", "."));
                 
                Double maxCoste = (salarioAnexo * dias /360) - minoracion;
                
                if(maxCoste >= Double.parseDouble(res.getMaxSubvencion().replace(",", "."))){
                     res.setMaxSubvencionSubFinal(res.getMaxSubvencion());
                }else{
                     res.setMaxSubvencionSubFinal(df.format(maxCoste).toString());
                }
                    
                
               
                
                Double dato = bcPeriodo*coeficienteTgss/100;
                
                BigDecimal visadoPto = new BigDecimal(0);
                BigDecimal visadoJus = new BigDecimal(0);
                BigDecimal visadoSubv = new BigDecimal(0);
                BigDecimal seguroPto = new BigDecimal(0);
                BigDecimal seguroJus = new BigDecimal(0);
                BigDecimal seguroSubv = new BigDecimal(0);
                
                if(rs.getString("CPE_PTO_GASTOS_VIS")!= null)
                    visadoPto = visadoPto.add(rs.getBigDecimal("CPE_PTO_GASTOS_VIS"));
                if(rs.getString("CPE_JUS_GASTOS_VIS")!= null)
                    visadoJus = visadoJus.add(rs.getBigDecimal("CPE_JUS_GASTOS_VIS"));
                if(rs.getString("CPE_PTO_GASTOS_SEG")!= null)
                    seguroPto = seguroPto.add(rs.getBigDecimal("CPE_PTO_GASTOS_SEG"));
                if(rs.getString("CPE_JUS_GASTOS_SEG")!= null)
                    seguroJus = seguroJus.add(rs.getBigDecimal("CPE_JUS_GASTOS_SEG"));
                if(visadoPto.compareTo(visadoJus) == -1)
                    visadoSubv = visadoSubv.add(visadoPto);
                else
                    visadoSubv = visadoSubv.add(visadoJus);
                if(seguroPto.compareTo(seguroJus) == -1)
                    seguroSubv = seguroSubv.add(seguroPto);
                else
                    seguroSubv = seguroSubv.add(seguroJus);
                res.setGastosSeguro(seguroSubv);
                res.setGastosVisado(visadoSubv);
                
                
                dato = (salarioAnexo * dias /360)-minoracion;
                dato2 = salario+ (bcPeriodo*coeficienteTgss/100) + (Double.parseDouble(bcAT)*(coeficienteFog + coeficienteAt)/100)+ /*(porcEpsv*bcPeriodo/100)*/aportacionEPSV;
                
                if(dato >= dato2)
                    dato = dato2;
                
                if(dietasAbon >= dietasConce)
                    dato += dietasConce;
                else 
                    dato += dietasAbon;
                
                if(vigSegAbon>= vigSegConce)
                    dato += vigSegConce;
                else
                    dato += vigSegAbon;
                
                dato = dato - bonif;
                if(dato < 0) dato = 0.0;
                //res.setCostePeriodoSubv(rdo);
                res.setTotalSubv(df.format(dato).toString());  
                
                informePuestos.add(res);
            }
            return informePuestos;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando el resumen para el expediente "+ ex);
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
    
    public List<FilaInformeDatosPuestosVO> getInformeDatosPuestos2016(int ejercicio, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        int anoExpediente = ejercicio;//MeLanbide39Utils.getEjercicioDeExpediente(numExpediente);
        List<FilaInformeDatosPuestosVO> resumen = new ArrayList<FilaInformeDatosPuestosVO>();
        FilaInformeDatosPuestosVO res = new FilaInformeDatosPuestosVO();
        try
        {
            String query = null;
            String tablaEdesval = ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_E_DES_VAL, ConstantesMeLanbide39.FICHERO_PROPIEDADES);
            String codigoRes = ConfigurationParameter.getParameter(ConstantesMeLanbide39.CAMPO_SUPL_RESULTADO, ConstantesMeLanbide39.FICHERO_PROPIEDADES);
            
                //Leire, se comentan las línas en las que se realicen cálculos con los días trabajados
                query = "select CPE_OFE_NUMEXP,"+
                        " CPE_JUS_COD_PUESTO, CPE_JUS_ID_OFERTA,"+
                        //+ "HTE_NOM ||' ' || HTE_AP1||' ' || HTE_AP2 as entidad, \n" +
                        " CASE WHEN ROLSUBR.HTE_NOM IS NOT NULL THEN \n " +
                        " ROLSOLI.HTE_NOM ||' ' || ROLSOLI.HTE_AP1||' ' || ROLSOLI.HTE_AP2||' (inicial),'||  CHR(10)  || ROLSUBR.HTE_NOM ||' ' || ROLSUBR.HTE_AP1||' ' || ROLSUBR.HTE_AP2||' (subrogado)' \n" +
                        " ELSE ROLSOLI.HTE_NOM ||' ' || ROLSOLI.HTE_AP1||' ' || ROLSOLI.HTE_AP2 \n" +
                        " end as ENTIDAD, "+
                        //"CASE WHEN VALOR = '1' THEN 'Hombre' ELSE 'Mujer' end AS SEXO,"
                        "  CASE\n" +
                        "    WHEN CPE_OFE_SEXO = 'H'\n" +
                        "    THEN 'Hombre'\n" +
                        "    WHEN CPE_OFE_SEXO = 'M'\n" +
                        "    THEN 'Mujer'\n" +
                        "  END AS SEXO,"
                        +"case"
                          +" when res.DES_VAL_COD IN ('"+ConstantesMeLanbide39.CODIGO_RESULTADO_RENUNCIA+"') then null\r\n"
                          +" when CPE_OFE_ID_OFERTA is null then null\r\n"
                          +" when CPE_OFE_ID_OFERTA = 1 then CPE_OFE_NOMBRE || ' ' || CPE_OFE_APE1 || ' '||coalesce(CPE_OFE_APE2,'')\r\n"
                          +" else 'SUSTITUTO - '||CPE_OFE_NOMBRE || ' ' || CPE_OFE_APE1 || ' '||coalesce(CPE_OFE_APE2,'')\r\n"
                          +" end as NOMBRE_APELLIDOS"
                        //+ " CPE_OFE_NOMBRE || ' ' || CPE_OFE_APE1 || ' '||coalesce(CPE_OFE_APE2,'') NOMBRE_APELLIDOS"
                        + " ,N.DES_NOM CPE_OFE_COD_NIV_FORM"
                        + " ,CPE_OFE_NIF, ACTIVIDAD"
                        + " ,CASE WHEN CPE_PTO_PAI_COD_1 IS NOT NULL THEN CPE_PTO_PAI_COD_1||'-'||pai1.PAI_NOM ELSE ' ' END PAIS_SOLICITADO1"
                        + " ,CASE WHEN CPE_PTO_PAI_COD_2 IS NOT NULL THEN CPE_PTO_PAI_COD_2||'-'||pai2.PAI_NOM ELSE ' ' END PAIS_SOLICITADO2"
                        + " ,CASE WHEN CPE_PTO_PAI_COD_3 IS NOT NULL THEN CPE_PTO_PAI_COD_3||'-'||pai3.PAI_NOM ELSE ' ' END PAIS_SOLICITADO3"
                        + " ,CASE WHEN CPE_OFE_PAI_COD_1 IS NOT NULL THEN CPE_OFE_PAI_COD_1||'-'||pai4.PAI_NOM ELSE ' ' END PAIS_FINAL1"
                        + " ,CASE WHEN CPE_OFE_PAI_COD_2 IS NOT NULL THEN CPE_OFE_PAI_COD_2||'-'||pai5.PAI_NOM ELSE ' ' END PAIS_FINAL2"
                        + " ,CASE WHEN CPE_OFE_PAI_COD_3 IS NOT NULL THEN CPE_OFE_PAI_COD_3||'-'||pai6.PAI_NOM ELSE ' ' END PAIS_FINAL3"
                        + " ,CPE_JUS_MESES_EXT"
                        + " ,CPE_OFE_FEC_INI "
                        +" ,CASE WHEN CPE_OFE_FEC_FIN > TO_DATE('31/10/"+(anoExpediente+1)+"', 'dd/MM/yyyy') THEN TO_DATE('31/10/"+(anoExpediente+1)+"', 'dd/MM/yyyy') ELSE CPE_OFE_FEC_FIN END CPE_OFE_FEC_FIN"
                        + " ,CPE_PTO_SALARIO_ANEX_TOT SALARIO_ANEXO"
                        + " ,CPE_JUS_MINORACION MINORACION, CPE_JUS_DIAS_TRAB, CPE_JUS_DIAS_SEGSOC "
                        + " ,CPE_JUS_BASCOT_CC BC_CC_EN_PERI, CPE_OFE_FEC_BAJA "
                        + " ,CPE_JUS_COEF_TGSS COEFICIENTE_TGSS"
                        + " ,CPE_JUS_BASCOT_CC *CPE_JUS_COEF_TGSS/100 SS_EMPRESA"
                        + " ,CPE_JUS_BASCOT_AT  BC_AT "
                        + " ,CPE_JUS_FOGASA COEFICIENTE_FOGASA"
                        + " ,CPE_JUS_AT COEFICIENTE_AT"
                        + " ,CPE_PTO_MESES_MANUT"
                        + " , CPE_JUS_BASCOT_AT *(NVL(CPE_JUS_FOGASA,0)+NVL(CPE_JUS_AT,0))/100 FOGASA_AT_EMPRESA "
                        + " , CPE_JUS_EPSV PORC_EPSV"
                        + " , CPE_JUS_APORTEPSV APORTACIONES_EPSV_EMPRESA "
                        + " , (CPE_JUS_BASCOT_CC *CPE_JUS_COEF_TGSS/100)+(CPE_JUS_BASCOT_at *(NVL(CPE_JUS_FOGASA,0)+NVL(CPE_JUS_AT,0))/100)+NVL(CPE_JUS_APORTEPSV,0)TOTAL_EMPRESA "
                        //redondear cada calculo a dos decimales para que coincida con pantalla de justificación
                        + " , (CPE_JUS_SALARIO )+ROUND((CPE_JUS_BASCOT_CC *CPE_JUS_COEF_TGSS/100),2)+ROUND((CPE_JUS_BASCOT_at *(NVL(CPE_JUS_FOGASA,0)+NVL(CPE_JUS_AT,0))/100),2)+NVL(CPE_JUS_APORTEPSV,0)TOTAL_PERIODO "
                        //+ " , (CPE_JUS_SALARIO )+(CPE_JUS_BASCOT_CC *CPE_JUS_COEF_TGSS/100)+(CPE_JUS_BASCOT_at *(NVL(CPE_JUS_FOGASA,0)+NVL(CPE_JUS_AT,0))/100)+NVL(CPE_JUS_APORTEPSV,0)TOTAL_PERIODO "
                        + " ,(CPE_PTO_SALARIO_ANEX_TOT - CPE_JUS_MINORACION) SUBV_MINORADA_SALAR_SSOCIAL"
                        //redondear cada calculo a dos decimales para que coincida con pantalla de justificación
                        + " , NVL(CPE_JUS_SALARIO,0 )+ROUND(NVL(CPE_JUS_BASCOT_CC *CPE_JUS_COEF_TGSS/100,0),2)+ROUND(NVL(CPE_JUS_BASCOT_at *(NVL(CPE_JUS_FOGASA,0)+NVL(CPE_JUS_AT,0))/100,0),2)+NVL(CPE_JUS_APORTEPSV,0)SALARIO_PERIODO "
                        //+ " , NVL(CPE_JUS_SALARIO,0 )+NVL(CPE_JUS_BASCOT_CC *CPE_JUS_COEF_TGSS/100,0)+NVL(CPE_JUS_BASCOT_at *(NVL(CPE_JUS_FOGASA,0)+NVL(CPE_JUS_AT,0))/100,0)+NVL(CPE_JUS_APORTEPSV,0)SALARIO_PERIODO "

                        + " , CPE_JUS_DIETAS DIETAS_ABONADAS_PERIODO"
                        + " , CPE_PTO_DIETAS_MANUT DIETAS_CONCEDIDAS_PERIODO"
                        + " , CASE WHEN CPE_JUS_DIETAS >=CPE_PTO_DIETAS_MANUT THEN CPE_PTO_DIETAS_MANUT"
                        + " ELSE CPE_JUS_DIETAS END MAX_SUB_DIETAS"
                        + " ,CPE_JUS_GASTOSGES VIS_SEG_ABONADO"
                        + " ,CPE_PTO_TRAM_SEGUROS VIS_SEG_CONCEDIDO"
                        + " , CASE WHEN CPE_JUS_GASTOSGES>=CPE_PTO_TRAM_SEGUROS THEN CPE_PTO_TRAM_SEGUROS ELSE CPE_JUS_GASTOSGES END VIS_SEG_SUBV"
                        + " ,CPE_JUS_BONIF BONIFICACIONES, CPE_JUS_MESES_EXT, CPE_JUS_BASCOT_AT, CPE_JUS_SALARIO,CPE_JUS_IMP_JUSTIFICADO,\n" +
                        "  CPE_PTO_IMP_MAX_SUBV, B.DES_NOM AS MOTIVO_BAJA, CPE_PTO_GASTOS_SEG, CPE_PTO_GASTOS_VIS, CPE_JUS_GASTOS_SEG, CPE_JUS_GASTOS_VIS, "
                        + "nvl(case when nvl(CPE_PTO_IMP_MAX_SUBV, 999999999) > nvl(CPE_PTO_IMP_TOT_SOLIC, 9999999999) then CPE_PTO_IMP_TOT_SOLIC else CPE_PTO_IMP_MAX_SUBV end, 0) AS IMPORTE \n" 
                        + " from "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_PUESTO, ConstantesMeLanbide39.FICHERO_PROPIEDADES) +" p "
                        /*+ " INNER JOIN E_EXT ON EXT_NUM = Cpe_Pto_Numexp AND EXT_ROL = 1 "
                        + " inner join T_HTE on EXT_TER=HTE_TER AND EXT_NVR = HTE_NVR"
                        + " LEFT JOIN T_CAMPOS_DESPLEGABLE ON COD_CAMPO ='TSEXOTERCERO' AND COD_TERCERO = HTE_TER AND COD_MUNICIPIO = EXT_MUN "
                        */
                        +" LEFT join (\n" +   
                                "select HTE_NOM,HTE_AP1,HTE_AP2,EXT_ROL,EXT_NUM   from E_EXT \n" + 
                                "INNER JOIN T_HTE ON EXT_TER=HTE_TER AND EXT_NVR = HTE_NVR \n" + 
                                ")ROLSOLI ON ROLSOLI.EXT_NUM = CPE_PTO_NUMEXP AND ROLSOLI.EXT_ROL = 1 \n" + 
                            " LEFT join (\n" +   
                                "select HTE_NOM,HTE_AP1,HTE_AP2,EXT_ROL,EXT_NUM   from E_EXT \n" + 
                                "INNER JOIN T_HTE ON EXT_TER=HTE_TER AND EXT_NVR = HTE_NVR \n" + 
                                ")ROLSUBR ON ROLSUBR.EXT_NUM = CPE_PTO_NUMEXP AND ROLSUBR.EXT_ROL = 2 \n"                                                  
                        + " Left Join ("
                        + "    Select Dbms_Lob.Substr(Ttl_Valor,4000,1) AS ACTIVIDAD,Ttl_Num, TTL_EJE"
                        + "    From E_Ttl "
                        + "    Where Ttl_Cod = 'ACTIV'"
                        + " ) ON TTL_NUM = Cpe_Pto_Numexp "
                        + " inner join "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_OFERTA, ConstantesMeLanbide39.FICHERO_PROPIEDADES) +" o on P.Cpe_Pto_Cod_Puesto=O.Cpe_Ofe_Cod_Puesto and P.Cpe_Pto_Numexp =o.CPE_OFE_NUMEXP"
                        + " inner  join "+ConfigurationParameter.getParameter(ConstantesMeLanbide39.TABLA_CPE_JUSTIFICACION, ConstantesMeLanbide39.FICHERO_PROPIEDADES) +" j on P.Cpe_Pto_Cod_Puesto=J.Cpe_Jus_Cod_Puesto and P.Cpe_Pto_Numexp =j.CPE_jus_NUMEXP and j.CPE_JUS_id_OFERTA=O.Cpe_Ofe_id_Oferta"
                        + " left join " + GlobalNames.ESQUEMA_GENERICO + "T_PAI pai1 on pai1.PAI_COD=CPE_PTO_PAI_COD_1"
                        + " LEFT join " + GlobalNames.ESQUEMA_GENERICO + "T_PAI pai2 on pai2.PAI_COD=CPE_PTO_PAI_COD_2"
                        + " LEFT join " + GlobalNames.ESQUEMA_GENERICO + "T_PAI pai3 on pai3.PAI_COD=CPE_PTO_PAI_COD_3"
                        + " left join " + GlobalNames.ESQUEMA_GENERICO + "T_PAI pai4 on pai4.PAI_COD=CPE_OFE_PAI_COD_1"
                        + " LEFT join " + GlobalNames.ESQUEMA_GENERICO + "T_PAI pai5 on pai5.PAI_COD=CPE_OFE_PAI_COD_2"
                        + " LEFT join " + GlobalNames.ESQUEMA_GENERICO + "T_PAI pai6 on pai6.PAI_COD=CPE_OFE_PAI_COD_3"                        
                          +" left join (select * from "+tablaEdesval+" where DES_COD = '"+codigoRes+"') res on CPE_PTO_COD_RESULT = res.DES_VAL_COD\r\n"
                        //+ " LEFT JOIN E_DES_VAL ON DES_COD = 'CBAJ' AND DES_VAL_COD = CPE_OFE_COD_CAUSA_BAJA"                        
                        + " LEFT JOIN E_DES_VAL B ON B.DES_COD = 'CBAJ' AND B.DES_VAL_COD = CPE_OFE_COD_CAUSA_BAJA"
                        + " LEFT JOIN E_DES_VAL N ON N.DES_COD = 'NFOR' AND N.DES_VAL_COD = CPE_OFE_COD_NIV_FORM"
                        + " where CPE_OFE_EXP_EJE = "+anoExpediente+""
                        + " and CPE_OFE_NIF is not null ORDER BY CPE_OFE_NUMEXP";
            
            if(log.isDebugEnabled()) 
                log.debug("sqlJus = " + query);
  
            st = con.createStatement();
            rs = st.executeQuery(query);

            DecimalFormat df = new DecimalFormat("#.##");
            Double salarioAnexo = 0.00;
            Double salario = 0.00;
            Double minoracion = 0.00;
            Double bcPeriodo = 0.00;
            String bcAT = "";
            Double bcCC = 0.00;
            Double coeficienteTgss = 0.00;
            Double coeficienteFog = 0.00;
            Double coeficienteAt = 0.00;
            //Double porcEpsv = 0.00;
            Double aportacionEPSV = 0.00;
            Double dietasAbon = 0.00;
            Double dietasConce = 0.00;
            Double vigSegAbon = 0.00;
            Double vigSegConce = 0.00;
            Double bonif = 0.00;
            Double jusEpsv = 0.00;
            Double mesesManut = 0.00;
            Double dato=0.00;
            Double dato2=0.00;
            
            while(rs.next())
            {         
                int dias = 0;
                int diasSeg = 0;
                int diasC = 0;
                res = new FilaInformeDatosPuestosVO();
                //resumen.add();
                res = (FilaInformeDatosPuestosVO)MeLanbide39MappingUtils.getInstance().map(rs, FilaInformeDatosPuestosVO.class);
                
                //Leire, hacemos los cálculos relacionados con el número de días
                if(rs.getString("CPE_PTO_MESES_MANUT") != null)
                    mesesManut = rs.getDouble("CPE_PTO_MESES_MANUT");
                else 
                    mesesManut = 0.0;
                
                if(rs.getString("SALARIO_ANEXO") != null)
                    salarioAnexo = rs.getDouble("SALARIO_ANEXO");
                else 
                    salarioAnexo = 0.0;
                
                if(rs.getString("CPE_JUS_SALARIO") != null)
                    salario = rs.getDouble("CPE_JUS_SALARIO");
                else 
                    salario = 0.0;
                
                if(rs.getString("MINORACION") != null)
                    minoracion = rs.getDouble("MINORACION");
                else 
                    minoracion = 0.0;
                
                if(rs.getString("BC_CC_EN_PERI") != null)
                    bcPeriodo = rs.getDouble("BC_CC_EN_PERI");
                else 
                    bcPeriodo = 0.0;
                
                if(rs.getString("COEFICIENTE_TGSS") != null)
                    coeficienteTgss = rs.getDouble("COEFICIENTE_TGSS");
                else 
                    coeficienteTgss = 0.0;
                
                if(rs.getString("COEFICIENTE_FOGASA") != null)
                    coeficienteFog = rs.getDouble("COEFICIENTE_FOGASA");
                else 
                    coeficienteFog = 0.0;
                
                if(rs.getString("COEFICIENTE_AT") != null)
                    coeficienteAt = rs.getDouble("COEFICIENTE_AT");
                else 
                    coeficienteAt = 0.0;
                
                /*if(rs.getString("PORC_EPSV") != null)
                    porcEpsv = rs.getDouble("PORC_EPSV");
                else 
                    porcEpsv = 0.0;
                */
                if(rs.getString("APORTACIONES_EPSV_EMPRESA") != null)
                    aportacionEPSV = rs.getDouble("APORTACIONES_EPSV_EMPRESA");
                else 
                    aportacionEPSV = 0.0;      
                if(rs.getString("DIETAS_ABONADAS_PERIODO") != null)
                    dietasAbon = rs.getDouble("DIETAS_ABONADAS_PERIODO");
                else 
                    dietasAbon = 0.0;
                
                if(rs.getString("DIETAS_CONCEDIDAS_PERIODO") != null)
                    dietasConce = rs.getDouble("DIETAS_CONCEDIDAS_PERIODO");
                else 
                    dietasConce = 0.0;
                
                if(rs.getString("VIS_SEG_ABONADO") != null)
                    vigSegAbon = rs.getDouble("VIS_SEG_ABONADO");
                else 
                    vigSegAbon = 0.0;
                
                if(rs.getString("VIS_SEG_CONCEDIDO") != null)
                    vigSegConce = rs.getDouble("VIS_SEG_CONCEDIDO");
                else 
                    vigSegConce = 0.0;
                if(rs.getString("BONIFICACIONES") != null)
                    bonif = rs.getDouble("BONIFICACIONES");
                else 
                    bonif = 0.0;
                
                if(rs.getString("CPE_JUS_DIAS_TRAB") != null)
                    dias = rs.getInt("CPE_JUS_DIAS_TRAB");
                
                if(rs.getString("CPE_JUS_DIAS_SEGSOC") != null)
                    diasSeg = rs.getInt("CPE_JUS_DIAS_SEGSOC");
                
                if(rs.getString("CPE_JUS_BASCOT_AT") != null)
                    bcAT  = rs.getString("CPE_JUS_BASCOT_AT");
                else
                    bcAT = "0";
                
                if(rs.getString("BC_CC_EN_PERI") != null)
                    bcCC  = rs.getDouble("BC_CC_EN_PERI");
                else
                    bcCC = 0.0;
                
                if(rs.getString("PORC_EPSV") != null)
                    jusEpsv  = rs.getDouble("PORC_EPSV");
                else
                    jusEpsv = 0.0;
                
                String rdo ="";
                if(rs.getString("CPE_OFE_NUMEXP")!= null && rs.getString("CPE_OFE_NIF")!=null){
                 // rdo= obtenerDiasTrabajados(rs.getString("CPE_OFE_NUMEXP"), rs.getString("CPE_OFE_NIF"), con);
                }
                
                if(rdo != null && !rdo.equals(""))
                    diasC = Integer.parseInt(rdo);
                else
                    diasC = 0;
                
                if(dias == 0)
                    dias = diasC;
                if(diasSeg == 0)
                    diasSeg = diasC;
                /*if(rs.getString("CPE_JUS_MESES_EXT")== null || rs.getString("CPE_JUS_MESES_EXT").equals(""))
                    res.setMesesExt("0");
                else
                    res.setMesesExt(rs.getString("CPE_JUS_MESES_EXT"));*/
                 if(rs.getString("CPE_PTO_MESES_MANUT")!= null ){
                     if(rs.getString("CPE_PTO_MESES_MANUT")== null || rs.getString("CPE_PTO_MESES_MANUT").equals(""))
                    res.setMesesExt("0");
                else
                    res.setMesesExt(rs.getString("CPE_PTO_MESES_MANUT"));
                 }
                
                res.setDiasSubv(String.valueOf(diasSeg));
                
                dato2 = salario + (bcCC * coeficienteTgss/100);
                res.setSalarioAnualBrutoJus(String.valueOf(dato2));
                
                dato2 = dato2 - bonif;
                res.setSalarioSSBonif(String.valueOf(dato2));
                
                dato = bcPeriodo*coeficienteTgss/100;
                
                BigDecimal visadoPto = new BigDecimal(0);
                BigDecimal visadoJus = new BigDecimal(0);
                BigDecimal visadoSubv = new BigDecimal(0);
                BigDecimal seguroPto = new BigDecimal(0);
                BigDecimal seguroJus = new BigDecimal(0);
                BigDecimal seguroSubv = new BigDecimal(0);
                
                if(rs.getBigDecimal("CPE_PTO_GASTOS_VIS")!=null){
                    visadoPto = visadoPto.add(rs.getBigDecimal("CPE_PTO_GASTOS_VIS")!=null?rs.getBigDecimal("CPE_PTO_GASTOS_VIS"):BigDecimal.ZERO);
                }
               
                if(rs.getBigDecimal("CPE_JUS_GASTOS_VIS")!=null){
                    visadoJus = visadoJus.add(rs.getBigDecimal("CPE_JUS_GASTOS_VIS")!=null?rs.getBigDecimal("CPE_JUS_GASTOS_VIS"):BigDecimal.ZERO);
                }
             
                if(rs.getBigDecimal("CPE_PTO_GASTOS_SEG")!=null){
                    seguroPto = seguroPto.add(rs.getBigDecimal("CPE_PTO_GASTOS_SEG")!=null?rs.getBigDecimal("CPE_PTO_GASTOS_SEG"):BigDecimal.ZERO);
                }
                
                 if(rs.getBigDecimal("CPE_JUS_GASTOS_SEG")!=null){
                    seguroJus = seguroJus.add(rs.getBigDecimal("CPE_JUS_GASTOS_SEG")!=null?rs.getBigDecimal("CPE_JUS_GASTOS_SEG"):BigDecimal.ZERO);
                 }
                
                
                if(visadoPto.compareTo(visadoJus) == -1)
                    visadoSubv = visadoSubv.add(visadoPto);
                else
                    visadoSubv = visadoSubv.add(visadoJus);
                if(seguroPto.compareTo(seguroJus) == -1)
                    seguroSubv = seguroSubv.add(seguroPto);
                else
                    seguroSubv = seguroSubv.add(seguroJus);

                res.setGastosSeguro(seguroSubv);
                res.setGastosVisado(visadoSubv);

                dato = (salarioAnexo * dias /360)-minoracion;
                dato2 = salario+ (bcPeriodo*coeficienteTgss/100) + (Double.parseDouble(bcAT)*(coeficienteFog + coeficienteAt)/100)+ /*(porcEpsv*bcPeriodo/100)*/aportacionEPSV;
               
                if(dato >= dato2)
                    dato = dato2;
                
                DecimalFormat df2 = new DecimalFormat("#.##");
                res.setMaxSubvencion(df2.format(dato).toString());
        
                dato += visadoSubv.doubleValue();
                dato += seguroSubv.doubleValue();
//                if(dietasAbon >= dietasConce)
//                    dato += dietasConce;
//                else 
//                    dato += dietasAbon;
//                
//                if(vigSegAbon>= vigSegConce)
//                    dato += vigSegConce;
//                else
//                    dato += vigSegAbon;
   
                dato = dato - bonif;
                if(dato < 0) dato = 0.0;
                //res.setCostePeriodoSubv(rdo);
                res.setTotalSubv(df.format(dato).toString());  
                
                resumen.add(res);
            }
            return resumen;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando el resumen para el expediente "+ ex);
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
    
    public List<FilaInformeHoja2DatosPuestosVO> getInformeDatosPuestosHoja2(int ejercicio, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        int anoExpediente = ejercicio;//MeLanbide39Utils.getEjercicioDeExpediente(numExpediente);
        List<FilaInformeHoja2DatosPuestosVO> resumen = new ArrayList<FilaInformeHoja2DatosPuestosVO>();
        FilaInformeHoja2DatosPuestosVO res = new FilaInformeHoja2DatosPuestosVO();
        try{
            /*String sentencia = "SELECT CPE_JUS_NUMEXP, HTE_NOM ||' ' || HTE_AP1||' ' || HTE_AP2 as entidad, \n" +
                            " I.TNU_VALOR AS IMPCON, P.TNU_VALOR AS PRIMERPAGO, S.TNU_VALOR AS SEGPAGO, \n" +
                            " SUM(CPE_PTO_SALARIO_ANEX_TOT) CPE_PTO_SALARIO_ANEX_TOT, SUM(CPE_JUS_DIAS_TRAB)CPE_JUS_DIAS_TRAB, \n" +
                            " SUM(CPE_JUS_MINORACION)CPE_JUS_MINORACION, SUM(CPE_JUS_BASCOT_CC)CPE_JUS_BASCOT_CC,\n" +
                            " SUM(CPE_JUS_COEF_TGSS)CPE_JUS_COEF_TGSS, SUM(CPE_JUS_BASCOT_AT)CPE_JUS_BASCOT_AT, \n" +
                            " SUM(CPE_JUS_FOGASA)CPE_JUS_FOGASA, SUM(CPE_JUS_AT)CPE_JUS_AT, SUM(CPE_JUS_EPSV)CPE_JUS_EPSV,\n" +
                            " SUM(CPE_JUS_DIETAS)CPE_JUS_DIETAS, SUM(CPE_PTO_DIETAS_MANUT)CPE_PTO_DIETAS_MANUT, \n" +
                            " SUM(CPE_JUS_GASTOSGES)CPE_JUS_GASTOSGES, SUM(CPE_PTO_TRAM_SEGUROS)CPE_PTO_TRAM_SEGUROS,\n" +
                            " SUM(CPE_JUS_BONIF)CPE_JUS_BONIF, SUM(CPE_JUS_SALARIO) CPE_JUS_SALARIO \n" +
                            " FROM CPE_JUSTIFICACION\n" +
                            " INNER JOIN E_EXT ON EXT_NUM = CPE_JUS_NUMEXP AND EXT_ROL = 1\n" +
                            " inner join T_HTE on EXT_TER=HTE_TER AND EXT_NVR = HTE_NVR\n" +
                            " INNER JOIN CPE_PUESTO  ON CPE_JUS_COD_PUESTO = CPE_PTO_COD_PUESTO AND CPE_JUS_NUMEXP = CPE_PTO_NUMEXP\n" +
                            " LEFT JOIN E_TNU I ON I.TNU_NUM = CPE_JUS_NUMEXP AND I.TNU_EJE = CPE_JUS_EXP_EJE AND I.TNU_COD = 'IMCON'\n" +
                            " LEFT JOIN E_TNU P ON P.TNU_NUM = CPE_JUS_NUMEXP AND P.TNU_EJE = CPE_JUS_EXP_EJE AND P.TNU_COD = 'IMPAG'\n" +
                            " LEFT JOIN E_TNU S ON S.TNU_NUM = CPE_JUS_NUMEXP AND S.TNU_EJE = CPE_JUS_EXP_EJE AND S.TNU_COD = 'IMPAG2'\n" +
                            " where CPE_JUS_EXP_EJE = " + anoExpediente + 
                            " GROUP BY CPE_JUS_NUMEXP, HTE_NOM ||' ' || HTE_AP1||' ' || HTE_AP2, I.TNU_VALOR, P.TNU_VALOR, S.TNU_VALOR "
                            + " ORDER BY CPE_JUS_NUMEXP";*/
            
             String sentencia = "SELECT distinct CPE_JUS_NUMEXP,ENTIDAD,IMPCON,PRIMERPAGO,SEGPAGO, \n "+
                            " SUM(CPE_PTO_SALARIO_ANEX_TOT) CPE_PTO_SALARIO_ANEX_TOT, SUM(CPE_JUS_DIAS_TRAB)CPE_JUS_DIAS_TRAB, \n" +
                            " SUM(CPE_JUS_MINORACION)CPE_JUS_MINORACION, SUM(CPE_JUS_BASCOT_CC)CPE_JUS_BASCOT_CC,\n" +
                            " SUM(CPE_JUS_COEF_TGSS)CPE_JUS_COEF_TGSS, SUM(CPE_JUS_BASCOT_AT)CPE_JUS_BASCOT_AT, \n" +
                            " SUM(CPE_JUS_FOGASA)CPE_JUS_FOGASA, SUM(CPE_JUS_AT)CPE_JUS_AT, SUM(CPE_JUS_EPSV)CPE_JUS_EPSV,\n" +
                            " SUM(CPE_JUS_DIETAS)CPE_JUS_DIETAS, SUM(CPE_PTO_DIETAS_MANUT)CPE_PTO_DIETAS_MANUT, \n" +
                            " SUM(CPE_JUS_GASTOSGES)CPE_JUS_GASTOSGES, SUM(CPE_PTO_TRAM_SEGUROS)CPE_PTO_TRAM_SEGUROS,\n" +
                            " SUM(CPE_JUS_BONIF)CPE_JUS_BONIF, SUM(CPE_JUS_SALARIO) CPE_JUS_SALARIO \n" +
                            " FROM ( \n "+
                            " SELECT DISTINCT CPE_JUS_NUMEXP,  "+                    
                            " CASE WHEN ROLSUBR.HTE_NOM IS NOT NULL THEN \n" +
                            " ROLSOLI.HTE_NOM ||' ' || ROLSOLI.HTE_AP1||' ' || ROLSOLI.HTE_AP2||'(inicial), ' || ROLSUBR.HTE_NOM ||' ' || ROLSUBR.HTE_AP1||' ' || ROLSUBR.HTE_AP2||'(subrogado)' \n" +
                            " ELSE ROLSOLI.HTE_NOM ||' ' || ROLSOLI.HTE_AP1||' ' || ROLSOLI.HTE_AP2 \n" +
                            " end as ENTIDAD, \n" +
                            " I.TNU_VALOR AS IMPCON, P.TNU_VALOR AS PRIMERPAGO, S.TNU_VALOR AS SEGPAGO, \n" +
                            " CPE_PTO_SALARIO_ANEX_TOT, CPE_JUS_DIAS_TRAB, \n" +
                            " CPE_JUS_MINORACION, CPE_JUS_BASCOT_CC,\n" +
                            " CPE_JUS_COEF_TGSS, CPE_JUS_BASCOT_AT, \n" +
                            " CPE_JUS_FOGASA, CPE_JUS_AT,CPE_JUS_EPSV,\n" +
                            " CPE_JUS_DIETAS,CPE_PTO_DIETAS_MANUT, \n" +
                            " CPE_JUS_GASTOSGES, CPE_PTO_TRAM_SEGUROS,\n" +
                            " CPE_JUS_BONIF,  CPE_JUS_SALARIO \n" +
                            " FROM CPE_JUSTIFICACION\n" +
                            " LEFT join (\n" +   
                                "select HTE_NOM,HTE_AP1,HTE_AP2,EXT_ROL,EXT_NUM   from E_EXT \n" + 
                                "INNER JOIN T_HTE ON EXT_TER=HTE_TER AND EXT_NVR = HTE_NVR \n" + 
                                ")ROLSOLI ON ROLSOLI.EXT_NUM = CPE_JUS_NUMEXP AND ROLSOLI.EXT_ROL = 1 \n" + 
                            " LEFT join (\n" +   
                                "select HTE_NOM,HTE_AP1,HTE_AP2,EXT_ROL,EXT_NUM   from E_EXT \n" + 
                                "INNER JOIN T_HTE ON EXT_TER=HTE_TER AND EXT_NVR = HTE_NVR \n" + 
                                ")ROLSUBR ON ROLSUBR.EXT_NUM = CPE_JUS_NUMEXP AND ROLSUBR.EXT_ROL = 2 \n" + 
 
                            " INNER JOIN CPE_PUESTO  ON CPE_JUS_COD_PUESTO = CPE_PTO_COD_PUESTO AND CPE_JUS_NUMEXP = CPE_PTO_NUMEXP\n" +
                            " LEFT JOIN E_TNU I ON I.TNU_NUM = CPE_JUS_NUMEXP AND I.TNU_EJE = CPE_JUS_EXP_EJE AND I.TNU_COD = 'IMCON'\n" +
                            " LEFT JOIN E_TNU P ON P.TNU_NUM = CPE_JUS_NUMEXP AND P.TNU_EJE = CPE_JUS_EXP_EJE AND P.TNU_COD = 'IMPAG'\n" +
                            " LEFT JOIN E_TNU S ON S.TNU_NUM = CPE_JUS_NUMEXP AND S.TNU_EJE = CPE_JUS_EXP_EJE AND S.TNU_COD = 'IMPAG2'\n" +
                            " where CPE_JUS_EXP_EJE = " + anoExpediente + 
                            " ) GROUP BY  CPE_JUS_NUMEXP,ENTIDAD,IMPCON,PRIMERPAGO,SEGPAGO " +                     
                            " ORDER BY CPE_JUS_NUMEXP";
            
            
                        if(log.isDebugEnabled()) 
                log.debug("sql = " + sentencia);
            st = con.createStatement();
            rs = st.executeQuery(sentencia);
            
            DecimalFormat df = new DecimalFormat("#.##");
            Double salarioAnexo = null;
            Double salario = null;
            Double minoracion = null;
            Double bcPeriodo = null;
            String bcAT = "";
            Double coeficienteTgss = null;
            Double coeficienteFog = null;
            Double coeficienteAt = null;
            Double porcEpsv = null;
            Double dietasAbon = null;
            Double dietasConce = null;
            Double vigSegAbon = null;
            Double vigSegConce = null;
            Double bonif = null;
            while(rs.next())
            {         
                int dias = 0;
                
                res.setNumExpediente(rs.getString("CPE_JUS_NUMEXP"));
                res.setEmpresa(rs.getString("entidad"));
                res.setImporteConce(rs.getString("IMPCON"));
                res.setPrimerPago(rs.getString("PRIMERPAGO"));
                res.setSegundoPago(rs.getString("SEGPAGO"));
                
                if(rs.getString("CPE_PTO_SALARIO_ANEX_TOT") != null)
                    salarioAnexo = rs.getDouble("CPE_PTO_SALARIO_ANEX_TOT");
                else 
                    salarioAnexo = 0.0;
                
                if(rs.getString("CPE_JUS_SALARIO") != null)
                    salario = rs.getDouble("CPE_JUS_SALARIO");
                else 
                    salario = 0.0;
                
                if(rs.getString("CPE_JUS_MINORACION") != null)
                    minoracion = rs.getDouble("CPE_JUS_MINORACION");
                else 
                    minoracion = 0.0;
                if(rs.getString("CPE_JUS_BASCOT_CC") != null)
                    bcPeriodo = rs.getDouble("CPE_JUS_BASCOT_CC");
                else 
                    bcPeriodo = 0.0;
                if(rs.getString("CPE_JUS_COEF_TGSS") != null)
                    coeficienteTgss = rs.getDouble("CPE_JUS_COEF_TGSS");
                else 
                    coeficienteTgss = 0.0;
                if(rs.getString("CPE_JUS_FOGASA") != null)
                    coeficienteFog = rs.getDouble("CPE_JUS_FOGASA");
                else 
                    coeficienteFog = 0.0;
                if(rs.getString("CPE_JUS_AT") != null)
                    coeficienteAt = rs.getDouble("CPE_JUS_AT");
                else 
                    coeficienteAt = 0.0;
                if(rs.getString("CPE_JUS_EPSV") != null)
                    porcEpsv = rs.getDouble("CPE_JUS_EPSV");
                else 
                    porcEpsv = 0.0;
                if(rs.getString("CPE_JUS_DIETAS") != null)
                    dietasAbon = rs.getDouble("CPE_JUS_DIETAS");
                else 
                    dietasAbon = 0.0;
                if(rs.getString("CPE_PTO_DIETAS_MANUT") != null)
                    dietasConce = rs.getDouble("CPE_PTO_DIETAS_MANUT");
                else 
                    dietasConce = 0.0;
                if(rs.getString("CPE_JUS_GASTOSGES") != null)
                    vigSegAbon = rs.getDouble("CPE_JUS_GASTOSGES");
                else 
                    vigSegAbon = 0.0;
                if(rs.getString("CPE_PTO_TRAM_SEGUROS") != null)
                    vigSegConce = rs.getDouble("CPE_PTO_TRAM_SEGUROS");
                else 
                    vigSegConce = 0.0;
                if(rs.getString("CPE_JUS_BONIF") != null)
                    bonif = rs.getDouble("CPE_JUS_BONIF");
                else 
                    bonif = 0.0;
                if(rs.getString("CPE_JUS_DIAS_TRAB") != null)
                    dias = rs.getInt("CPE_JUS_DIAS_TRAB");
//                if(rs.getString("CPE_JUS_DIAS_SEGSOC") != null)
//                    diasSeg = rs.getInt("CPE_JUS_DIAS_SEGSOC");
                
                if(rs.getString("CPE_JUS_BASCOT_AT") != null)
                    bcAT  = rs.getString("CPE_JUS_BASCOT_AT");
                else
                    bcAT = "0";
                
//                if(rs.getString("BC_CC_EN_PERI") != null)
//                    bcCC  = rs.getDouble("BC_CC_EN_PERI");
//                else
//                    bcCC = 0.0;
//                
//                if(rs.getString("PORC_EPSV") != null)
//                    jusEpsv  = rs.getDouble("PORC_EPSV");
//                else
//                    jusEpsv = 0.0;
                
                //añdimos el importe subvencionable
                BigDecimal justificado = new BigDecimal(0);
                BigDecimal justif = null;
                String coste="";
                List<FilaResumenVO> filasResumen = getListaResumenPorExpediente(res.getNumExpediente(), con);
                for(FilaResumenVO fila : filasResumen)
                {
                    coste = fila.getCosteSubvecionable();
                    coste = coste.replace(",", ".");
                    if(!coste.equals("")){
                        justif = new BigDecimal(coste);
                    }
                    justificado=justificado.add(justif);
                }
                res.setImporteSubv(justificado.toString());  
//                Double dato = (salarioAnexo * dias /360)-minoracion;
//                Double dato2 = salario+ (bcPeriodo*coeficienteTgss/100) + (Double.parseDouble(bcAT)*(coeficienteFog + coeficienteAt)/100)+ (porcEpsv*bcPeriodo/100);
//                
//                if(dato >= dato2)
//                    dato = dato2;
//                if(dietasAbon >= dietasConce)
//                    dato += dietasConce;
//                else 
//                    dato += dietasAbon;
//                
//                if(vigSegAbon>= vigSegConce)
//                    dato += vigSegConce;
//                else
//                    dato += vigSegAbon;
//                
//                dato = dato - bonif;
//                if(dato < 0) dato = 0.0;
//                //res.setCostePeriodoSubv(rdo);
//                res.setImporteSubv(df.format(dato).toString());  
                resumen.add(res);
                res = new FilaInformeHoja2DatosPuestosVO();
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error en getInformeDatosPuestosHoja2: "+ ex);
            throw ex;
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
        return resumen;
    }
}