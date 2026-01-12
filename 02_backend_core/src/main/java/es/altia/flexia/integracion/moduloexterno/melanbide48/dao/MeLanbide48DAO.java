/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide48.dao;

import es.altia.agora.business.util.GlobalNames;
import es.altia.flexia.integracion.moduloexterno.melanbide48.i18n.MeLanbide48I18n;
import es.altia.flexia.integracion.moduloexterno.melanbide48.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide48.util.ConstantesMeLanbide48;
import es.altia.flexia.integracion.moduloexterno.melanbide48.util.MeLanbide48MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide48.util.MeLanbide48Utils;
import es.altia.flexia.integracion.moduloexterno.melanbide48.vo.ColecCentrosVO;
import es.altia.flexia.integracion.moduloexterno.melanbide48.vo.ColecComproRealizacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide48.vo.ColecEntidadEnAgrupacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide48.vo.ColecEntidadVO;
import es.altia.flexia.integracion.moduloexterno.melanbide48.vo.ColecProcesoAdjudicacionRespuestaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide48.vo.ColecProgConvActPredefinidaColectivo;
import es.altia.flexia.integracion.moduloexterno.melanbide48.vo.ColecRepresentanteVO;
import es.altia.flexia.integracion.moduloexterno.melanbide48.vo.ColecSolicitudVO;
import es.altia.flexia.integracion.moduloexterno.melanbide48.vo.ColecTrayActividadVO;
import es.altia.flexia.integracion.moduloexterno.melanbide48.vo.ColecTrayEspVO;
import es.altia.flexia.integracion.moduloexterno.melanbide48.vo.ColecTrayOtroProgramaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide48.vo.ColecTrayVO;
import es.altia.flexia.integracion.moduloexterno.melanbide48.vo.ColecTrayectoriaEntidad;
import es.altia.flexia.integracion.moduloexterno.melanbide48.vo.ColecUbicacionesCTVO;
import es.altia.flexia.integracion.moduloexterno.melanbide48.vo.FilaTrayEspVO;
import es.altia.flexia.integracion.moduloexterno.melanbide48.vo.SelectItem;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.ValorCampoDesplegableModuloIntegracionVO;
import es.altia.util.conexion.BDException;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author santiagoc
 */
public class MeLanbide48DAO 
{ 
    
    //Logger
    private static final Logger log = LogManager.getLogger(MeLanbide48DAO.class);
    private final SimpleDateFormat formatFechaLog = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    private final SimpleDateFormat formatFechaddMMyyyy = new SimpleDateFormat("dd/MM/yyyy");
    private final MeLanbide48Utils meLanbide48Utils = new MeLanbide48Utils();
    
    //Instancia
    private static MeLanbide48DAO instance = null;
    
    private MeLanbide48DAO()
    {
        
    }
    
    public static MeLanbide48DAO getInstance()
    {
        if(instance == null)
        {
            synchronized(MeLanbide48DAO.class)
            {
                instance = new MeLanbide48DAO();
            }
        }
        return instance;
    }  
    
    public List<SelectItem> getTiposDocumento(Connection con) throws Exception
    {
        List<ValorCampoDesplegableModuloIntegracionVO> listaValores = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
            Statement st = null;
            ResultSet rs = null;
        try
        {
            //Creo el id con la secuencia
            String query = "SELECT TID_COD as CODIGO,TID_DES as DESCRIPCION FROM "+ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_TIPO_DOCUMENTO, ConstantesMeLanbide48.FICHERO_PROPIEDADES)
                   +" ORDER BY TID_COD ASC";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            
            while(rs.next())
            {
                listaValores.add((ValorCampoDesplegableModuloIntegracionVO)MeLanbide48MappingUtils.getInstance().map(rs, ValorCampoDesplegableModuloIntegracionVO.class));
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
            catch(SQLException e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return MeLanbide48MappingUtils.getInstance().fromCampoDesplegableToSelectItemVO(listaValores);
    }
    
    public BigDecimal getValorCampoNumerico(int codOrganizacion, String numExp, String ejercicio, String codigoCampo, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        BigDecimal valor = null;
        try
        {
            String query;
            query = "select TNU_VALOR from " + ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_TIPO_DATO_NUMERICO, ConstantesMeLanbide48.FICHERO_PROPIEDADES)
                    + " where TNU_MUN = '" + codOrganizacion + "' and TNU_EJE = '" + ejercicio 
                    + "' and TNU_NUM = '"+numExp+"' and TNU_COD = '"+codigoCampo+"'";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
                valor = rs.getBigDecimal("TNU_VALOR");
        }
        catch(SQLException ex)
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
            catch(SQLException e)
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
            String query;
            if(nuevo)
            {
                query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_TIPO_DATO_NUMERICO, ConstantesMeLanbide48.FICHERO_PROPIEDADES)
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
                query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_TIPO_DATO_NUMERICO, ConstantesMeLanbide48.FICHERO_PROPIEDADES)
                    + " set TNU_VALOR = "+valor.toPlainString()
                    + " where TNU_MUN = '"+codOrganizacion+"'"
                    + " and TNU_EJE = "+ejercicio
                    + " and TNU_NUM = '"+numExp+"'"
                    + " and TNU_COD = '"+codigoCampo+"'";
            }
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
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
            catch(SQLException e)
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
            String query;
            query = "delete from " + ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_TIPO_DATO_NUMERICO, ConstantesMeLanbide48.FICHERO_PROPIEDADES)
                + " where TNU_MUN = "+codOrganizacion
                + " and TNU_EJE = "+ejercicio
                + " and TNU_NUM = '"+numExp+"'"
                + " and TNU_COD = '"+codigoCampo+"'";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            result = st.executeUpdate(query);
        }
        catch(SQLException ex)
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
            catch(SQLException e)
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
            String query;
            query = "select TXT_VALOR from " + ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_TIPO_DATO_TEXTO, ConstantesMeLanbide48.FICHERO_PROPIEDADES)
                    + " where TXT_MUN = '" + codOrganizacion + "' and TXT_EJE = '" + ejercicio 
                    + "' and TXT_NUM = '"+numExp+"' and TXT_COD = '"+codigoCampo+"'";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
                valor = rs.getString("TXT_VALOR");
        }
        catch(SQLException ex)
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
            catch(SQLException e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return valor;
    }  
    
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
            String query;
            if(nuevo)
            {
                query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_TIPO_DATO_TEXTO, ConstantesMeLanbide48.FICHERO_PROPIEDADES)
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
                query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_TIPO_DATO_TEXTO, ConstantesMeLanbide48.FICHERO_PROPIEDADES)
                    + " set TXT_VALOR = '"+valor+"'"
                    + " where TXT_MUN = '"+codOrganizacion+"'"
                    + " and TXT_EJE = "+ejercicio
                    + " and TXT_NUM = '"+numExp+"'"
                    + " and TXT_COD = '"+codigoCampo+"'";
            }
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
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
            catch(SQLException e)
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
            String query;
            query = "delete from " + ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_TIPO_DATO_TEXTO, ConstantesMeLanbide48.FICHERO_PROPIEDADES)
                + " where TXT_MUN = "+codOrganizacion
                + " and TXT_EJE = "+ejercicio
                + " and TXT_NUM = '"+numExp+"'"
                + " and TXT_COD = '"+codigoCampo+"'";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            result = st.executeUpdate(query);
        }
        catch(SQLException ex)
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
            catch(SQLException e)
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
            String query;
            query = "select TDE_VALOR from " + ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_TIPO_DATO_DESPLEGABLE, ConstantesMeLanbide48.FICHERO_PROPIEDADES)
                    + " where TDE_MUN = '" + codOrganizacion + "' and TDE_EJE = '" + ejercicio 
                    + "' and TDE_NUM = '"+numExp+"' and TDE_COD = '"+codigoCampo+"'";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
                valor = rs.getString("TDE_VALOR");
        }
        catch(SQLException ex)
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
            catch(SQLException e)
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
            String query;
            if(nuevo)
            {
                query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_TIPO_DATO_DESPLEGABLE, ConstantesMeLanbide48.FICHERO_PROPIEDADES)
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
                query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_TIPO_DATO_DESPLEGABLE, ConstantesMeLanbide48.FICHERO_PROPIEDADES)
                    + " set TDE_VALOR = '"+valor+"'"
                    + " where TDE_MUN = '"+codOrganizacion+"'"
                    + " and TDE_EJE = "+ejercicio
                    + " and TDE_NUM = '"+numExp+"'"
                    + " and TDE_COD = '"+codigoCampo+"'";
            }
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
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
            catch(SQLException e)
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
            String query;
            query = "delete from " + ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_TIPO_DATO_DESPLEGABLE, ConstantesMeLanbide48.FICHERO_PROPIEDADES)
                + " where TDE_MUN = "+codOrganizacion
                + " and TDE_EJE = "+ejercicio
                + " and TDE_NUM = '"+numExp+"'"
                + " and TDE_COD = '"+codigoCampo+"'";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            result = st.executeUpdate(query);
        }
        catch(SQLException ex)
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
            catch(SQLException e)
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
            String query;
            query = "select TFE_VALOR from " + ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_TIPO_DATO_FECHA, ConstantesMeLanbide48.FICHERO_PROPIEDADES)
                    + " where TFE_MUN = '" + codOrganizacion + "' and TFE_EJE = '" + ejercicio 
                    + "' and TFE_NUM = '"+numExp+"' and TFE_COD = '"+codigoCampo+"'";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
                valor = rs.getDate("TFE_VALOR");
        }
        catch(SQLException ex)
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
            catch(SQLException e)
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
            String query;
            SimpleDateFormat formatoFecha = new SimpleDateFormat(ConstantesMeLanbide48.FORMATO_FECHA);
            if(nuevo)
            {
                query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_TIPO_DATO_FECHA, ConstantesMeLanbide48.FICHERO_PROPIEDADES)
                    + " (TFE_MUN, TFE_EJE, TFE_NUM, TFE_COD, TFE_VALOR)"
                    + " values("+codOrganizacion
                    + ", "+ejercicio
                    + ", '"+numExp+"'"
                    + ", '"+codigoCampo+"'"
                    + ", TO_DATE('"+formatoFecha.format(valor)+"', '"+ConstantesMeLanbide48.FORMATO_FECHA+"')"
                    + ")";
            }
            else
            {
                query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_TIPO_DATO_FECHA, ConstantesMeLanbide48.FICHERO_PROPIEDADES)
                    + " set TFE_VALOR = TO_DATE('"+formatoFecha.format(valor)+"', '"+ConstantesMeLanbide48.FORMATO_FECHA+"')"
                    + " where TFE_MUN = '"+codOrganizacion+"'"
                    + " and TFE_EJE = "+ejercicio
                    + " and TFE_NUM = '"+numExp+"'"
                    + " and TFE_COD = '"+codigoCampo+"'";
            }
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
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
            catch(SQLException e)
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
            String query;
            query = "delete from " + ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_TIPO_DATO_FECHA, ConstantesMeLanbide48.FICHERO_PROPIEDADES)
                + " where TFE_MUN = "+codOrganizacion
                + " and TFE_EJE = "+ejercicio
                + " and TFE_NUM = '"+numExp+"'"
                + " and TFE_COD = '"+codigoCampo+"'";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            result = st.executeUpdate(query);
        }
        catch(SQLException ex)
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
            catch(SQLException e)
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
            String query = "select PAI_COD as CODIGO, PAI_NOM as DESCRIPCION from "+ GlobalNames.ESQUEMA_GENERICO + ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_PAISES, ConstantesMeLanbide48.FICHERO_PROPIEDADES);
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while(rs.next())
            {
                listaValores.add((ValorCampoDesplegableModuloIntegracionVO)MeLanbide48MappingUtils.getInstance().map(rs, ValorCampoDesplegableModuloIntegracionVO.class));
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
            catch(SQLException e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return MeLanbide48MappingUtils.getInstance().fromCampoDesplegableToSelectItemVO(listaValores);
    }
    
    public ColecEntidadVO getEntidadPorCodigoYExpediente(ColecEntidadVO p, int idioma, Connection con) throws Exception
    {
        if(p != null && p.getCodEntidad()!= null)
        {
            PreparedStatement pt = null;
            ResultSet rs = null;
            ColecEntidadVO entidad = null;
            try
            {
                String query = "SELECT "+ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_ENTIDAD, ConstantesMeLanbide48.FICHERO_PROPIEDADES)
                        + ".* "
                        +" ,(CASE WHEN 4=? THEN cci.DESCRIPCIONEU ELSE cci.DESCRIPCION end) COMP_IGUALDAD_OPCION_TEXTO "
                        +",(CASE WHEN 4=? THEN cciv.DESCRIPCIONEU ELSE cciv.DESCRIPCION end) COMP_IGUALDAD_OPCION_VAL_TEXTO "
                        +" from "+ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_ENTIDAD, ConstantesMeLanbide48.FICHERO_PROPIEDADES)
                        +" LEFT JOIN COLEC_COMP_IGUALDAD cci ON colec_ENTIDAD.COMP_IGUALDAD_OPCION = CCI.CODIGO "
                        +" LEFT JOIN COLEC_COMP_IGUALDAD cciv ON colec_ENTIDAD.COMP_IGUALDAD_OPCION_VAL  = CCIv.CODIGO "
                        +" where COLEC_ENT_COD = '"+p.getCodEntidad()+"'"
                        +" and COLEC_NUMEXP = '"+p.getNumExp()+"'";
                if(log.isDebugEnabled()) 
                    log.error("sql = " + query);
                pt = con.prepareStatement(query);
                int contparams = 1;
                pt.setInt(contparams++,idioma);
                pt.setInt(contparams++,idioma);
                rs = pt.executeQuery();
                if(rs.next())
                {
                    entidad = (ColecEntidadVO)MeLanbide48MappingUtils.getInstance().map(rs, ColecEntidadVO.class);
                }
                return entidad;
            }
            catch(Exception ex)
            {
                log.error("Se ha producido un error recuperando entidad "+p.getCodEntidad()+" para expediente "+ p.getNumExp(), ex);
                throw new Exception(ex);
            }
            finally
            {
                try
                {
                    if(log.isDebugEnabled()) { 
                        log.debug("Procedemos a cerrar el statement y el resultset");
                    }
                    if(pt!=null) {
                        pt.close();
                    }
                    if(rs!=null) {
                        rs.close();
                    }
                }
                catch(SQLException e)
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
    
    public ColecRepresentanteVO getRepresentantePorExpediente(ColecRepresentanteVO p, Connection con) throws Exception
    {
        if(p != null && p.getNumExp()!= null)
        {
            Statement st = null;
            ResultSet rs = null;
            ColecRepresentanteVO representante = null;
            try
            {
                
                String query = "SELECT"
                        +" COLEC_COD_REPRESENTANTES AS COD_REPRESENTANTE"
                        +", COLEC_NUMEXP AS EXPEDIENTE"
                        +", COLEC_EXP_EJE AS EJERCICIO"
                        +", COLEC_TIPO_DOC AS COD_TIPO_DOC"
                        +", COLEC_DOCUMENTO AS DOCUMENTO"
                        +", COLEC_NOMBRE_APE AS NOMBRE"
                        + " from "+ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_REPRESENTANTES, ConstantesMeLanbide48.FICHERO_PROPIEDADES)
                              +" where COLEC_NUMEXP = '"+p.getNumExp()+"'";
                if(log.isDebugEnabled()) 
                    log.error("sql = " + query);
                st = con.createStatement();
                rs = st.executeQuery(query);
                if(rs.next())
                {
                    representante = (ColecRepresentanteVO)MeLanbide48MappingUtils.getInstance().map(rs, ColecRepresentanteVO.class);
                }
                return representante;
            }
            catch(Exception ex)
            {
                log.error("Se ha producido un error recuperando representante "+p.getCodRepresentante()+" para expediente "+ p.getNumExp(), ex);
                throw new Exception(ex);
            }
            finally
            {
                try
                {
                    if(log.isDebugEnabled()) { 
                        log.debug("Procedemos a cerrar el statement y el resultset");
                    }
                    if(st!=null) {
                        st.close();
                    }
                    if(rs!=null) {
                        rs.close();
                    }
                }
                catch(SQLException e)
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
    
    public ColecEntidadVO getEntidadInteresadaPorExpediente(ColecEntidadVO p, Connection con) throws Exception
    {
        if(p != null && p.getNumExp()!= null)
        {
            Statement st = null;
            ResultSet rs = null;
            ColecEntidadVO entidad = null;
            try
            {
                String query = "SELECT * "
                        + " FROM "+ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_ENTIDAD, ConstantesMeLanbide48.FICHERO_PROPIEDADES)
                        + " WHERE COLEC_NUMEXP = '"+p.getNumExp()+"'"
                        ;
                if(log.isDebugEnabled()) 
                    log.error("sql = " + query);
                st = con.createStatement();
                rs = st.executeQuery(query);
                if(rs.next())
                {
                    entidad = (ColecEntidadVO)MeLanbide48MappingUtils.getInstance().map(rs, ColecEntidadVO.class);
                }
                return entidad;
            }
            catch(Exception ex)
            {
                log.error("Se ha producido un error recuperando entidad "+p.getCodEntidad()+" para expediente "+ p.getNumExp(), ex);
                throw new Exception(ex);
            }
            finally
            {
                try
                {
                    if(log.isDebugEnabled()) { 
                        log.debug("Procedemos a cerrar el statement y el resultset");
                    }
                    if(st!=null) {
                        st.close();
                    }
                    if(rs!=null) {
                        rs.close();
                    }
                }
                catch(SQLException e)
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
    
    public ColecEntidadVO guardarColecEntidadVO(ColecEntidadVO entidad, Connection con) throws Exception
    {
        Statement st = null;
        try
        {
            String query = null;
            if(entidad.getCodEntidad()== null)
            {
                Long codEntidad = this.getNextId(ConfigurationParameter.getParameter(ConstantesMeLanbide48.COLEC_ENTIDAD_SQ, ConstantesMeLanbide48.FICHERO_PROPIEDADES), con);
                if(codEntidad == null)
                {
                    throw new Exception();
                }
                entidad.setCodEntidad(codEntidad);
                
                //Es una entidad nueva
                query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_ENTIDAD, ConstantesMeLanbide48.FICHERO_PROPIEDADES)
                        + "(COLEC_ENT_COD, COLEC_EXP_EJE, COLEC_NUMEXP, COLEC_ANIO_CONVOCATORIA"
                        + ", COLEC_ENT_ESAGRUPACION, COLEC_ENT_TIPO_CIF, COLEC_ENT_CIF, COLEC_ENT_NOMBRE"
                        + ", COLEC_ENT_CENTESPEMPTH, COLEC_ENT_PARTMAYCEETH, COLEC_ENT_EMPINSERCIONTH, COLEC_ENT_PROMOEMPINSTH"
                        + ", COLEC_ENT_NROTOTINTEG_AGRUP "
                        + ",PLANIGUALDAD,CERTIFICADOCALIDAD,ACEPTNUMEROSUPEHORAS,SEGUNDOLOCALMISMOAMB "
                        + ",COLEC_ENT_CENTESPEMPTH_VAL, COLEC_ENT_PARTMAYCEETH_VAL, COLEC_ENT_EMPINSERCIONTH_VAL, COLEC_ENT_PROMOEMPINSTH_VAL "
                        + ",PLANIGUALDAD_VAL,CERTIFICADOCALIDAD_VAL,SEGUNDOLOCALMISMOAMB_VAL,COLEC_ENT_ESAGRUPACION_VAL "
                        + ",COMP_IGUALDAD_OPCION,COMP_IGUALDAD_OPCION_VAL,ENT_SUJETA_DER_PUBL,ENT_SUJETA_DER_PUBL_VAL,ENT_SIN_ANIMO_LUCRO,ENT_SIN_ANIMO_LUCRO_VAL "
                        + ")"
                        + " values("+entidad.getCodEntidad()
                        + ", "+(entidad.getEjercicio()!= null ? entidad.getEjercicio() : "null")
                        + ", "+(entidad.getNumExp() != null ? "'"+entidad.getNumExp()+"'" : "null")
                        + ", "+(entidad.getAnioConvocatoria()!= null ? entidad.getAnioConvocatoria() : "null")
                        + ", "+(entidad.getEsAgrupacion()!= null ? entidad.getEsAgrupacion() : "null")
                        + ", "+(entidad.getTipoCif()!= null ? "'"+entidad.getTipoCif()+"'" : "null")
                        + ", "+(entidad.getCif()!= null ? "'"+entidad.getCif()+"'" : "null")
                        + ", "+(entidad.getNombre()!= null ? "'"+entidad.getNombre()+"'" : "null")
                        + ", "+(entidad.getCentroEspEmpTH()!= null ? +entidad.getCentroEspEmpTH() : "null")
                        + ", "+(entidad.getParticipanteMayorCentEcpEmpTH()!= null ? entidad.getParticipanteMayorCentEcpEmpTH() : "null")
                        + ", "+(entidad.getEmpresaInsercionTH()!= null ? entidad.getEmpresaInsercionTH() : "null")
                        + ", "+(entidad.getPromotorEmpInsercionTH()!= null ? entidad.getPromotorEmpInsercionTH() : "null")
                        + ", "+(entidad.getNumTotalEntAgrupacion()!= null ? entidad.getNumTotalEntAgrupacion() : "null")
                        + ", "+(entidad.getPlanIgualdad()!= null ? entidad.getPlanIgualdad() : "null")
                        + ", "+(entidad.getCertificadoCalidad()!= null ? entidad.getCertificadoCalidad() : "null")
                        + ", "+(entidad.getAceptaNumeroSuperiorHoras()!= null ? entidad.getAceptaNumeroSuperiorHoras(): "null")
                        + ", "+(entidad.getSegundosLocalesMismoAmbito()!= null ? entidad.getSegundosLocalesMismoAmbito(): "null")
                        + ", "+(entidad.getCentroEspEmpTHValidado()!= null ? +entidad.getCentroEspEmpTHValidado(): "null")
                        + ", "+(entidad.getParticipanteMayorCentEcpEmpTHValidado()!= null ? entidad.getParticipanteMayorCentEcpEmpTHValidado(): "null")
                        + ", "+(entidad.getEmpresaInsercionTHValidado()!= null ? entidad.getEmpresaInsercionTHValidado(): "null")
                        + ", "+(entidad.getPromotorEmpInsercionTHValidado()!= null ? entidad.getPromotorEmpInsercionTHValidado(): "null")
                        + ", "+(entidad.getPlanIgualdadValidado()!= null ? entidad.getPlanIgualdadValidado(): "null")
                        + ", "+(entidad.getCertificadoCalidadValidado()!= null ? entidad.getCertificadoCalidadValidado(): "null")
                        + ", "+(entidad.getSegundosLocalesMismoAmbitoValidado()!= null ? entidad.getSegundosLocalesMismoAmbitoValidado(): "null")
                        + ", "+(entidad.getEsAgrupacionValidado()!= null ? entidad.getEsAgrupacionValidado(): "null")
                        + ", "+(entidad.getCompIgualdadOpcion()!= null ? entidad.getCompIgualdadOpcion(): "null")
                        + ", "+(entidad.getCompIgualdadOpcionVal()!= null ? entidad.getCompIgualdadOpcionVal(): "null")
                        + ", "+(entidad.getEntSujetaDerPubl()!= null ? entidad.getEntSujetaDerPubl(): "null")
                        + ", "+(entidad.getEntSujetaDerPublVal()!= null ? entidad.getEntSujetaDerPublVal(): "null")
                        + ", "+(entidad.getEntSinAnimoLucro()!= null ? entidad.getEntSinAnimoLucro(): "null")
                        + ", "+(entidad.getEntSinAnimoLucroVal()!= null ? entidad.getEntSinAnimoLucroVal(): "null")
                        + ")";
            }
            else
            {
                //Es una entidad que ya existe
                query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_ENTIDAD, ConstantesMeLanbide48.FICHERO_PROPIEDADES)
                        + " set"
                        + " COLEC_ENT_ESAGRUPACION = "+(entidad.getEsAgrupacion()!= null ? entidad.getEsAgrupacion() : "null")
                        + ", COLEC_ENT_TIPO_CIF = "+(entidad.getTipoCif()!= null ? "'"+entidad.getTipoCif()+"'" : "null")
                        + ", COLEC_ENT_CIF = "+(entidad.getCif()!= null ? "'"+entidad.getCif()+"'" : "null")
                            + ", COLEC_ENT_NOMBRE = "+(entidad.getNombre()!= null ? "'"+entidad.getNombre()+"'" : "null")
                        + ", COLEC_ENT_CENTESPEMPTH = "+(entidad.getCentroEspEmpTH()!= null ? entidad.getCentroEspEmpTH() : "null")
                        + ", COLEC_ENT_PARTMAYCEETH = "+(entidad.getParticipanteMayorCentEcpEmpTH()!= null ? entidad.getParticipanteMayorCentEcpEmpTH() : "null")
                        + ", COLEC_ENT_EMPINSERCIONTH = "+(entidad.getEmpresaInsercionTH()!= null ? entidad.getEmpresaInsercionTH() : "null")
                        + ", COLEC_ENT_PROMOEMPINSTH = "+(entidad.getPromotorEmpInsercionTH()!= null ? entidad.getPromotorEmpInsercionTH() : "null")
                        + ", COLEC_ENT_NROTOTINTEG_AGRUP = "+(entidad.getNumTotalEntAgrupacion()!= null ? entidad.getNumTotalEntAgrupacion() : "null")
                        + ", PLANIGUALDAD = "+(entidad.getPlanIgualdad()!= null ? entidad.getPlanIgualdad(): "null")
                        + ", CERTIFICADOCALIDAD = "+(entidad.getCertificadoCalidad()!= null ? entidad.getCertificadoCalidad(): "null")
                        + ", ACEPTNUMEROSUPEHORAS = "+(entidad.getAceptaNumeroSuperiorHoras()!= null ? entidad.getAceptaNumeroSuperiorHoras(): "null")
                        + ", SEGUNDOLOCALMISMOAMB = "+(entidad.getSegundosLocalesMismoAmbito()!= null ? entidad.getSegundosLocalesMismoAmbito(): "null")
                        + ", COLEC_ENT_CENTESPEMPTH_VAL = "+(entidad.getCentroEspEmpTHValidado()!= null ? entidad.getCentroEspEmpTHValidado(): "null")
                        + ", COLEC_ENT_PARTMAYCEETH_VAL = "+(entidad.getParticipanteMayorCentEcpEmpTHValidado()!= null ? entidad.getParticipanteMayorCentEcpEmpTHValidado(): "null")
                        + ", COLEC_ENT_EMPINSERCIONTH_VAL = "+(entidad.getEmpresaInsercionTHValidado()!= null ? entidad.getEmpresaInsercionTHValidado(): "null")
                        + ", COLEC_ENT_PROMOEMPINSTH_VAL = "+(entidad.getPromotorEmpInsercionTHValidado()!= null ? entidad.getPromotorEmpInsercionTHValidado(): "null")
                        + ", PLANIGUALDAD_VAL = "+(entidad.getPlanIgualdadValidado()!= null ? entidad.getPlanIgualdadValidado(): "null")
                        + ", CERTIFICADOCALIDAD_VAL = "+(entidad.getCertificadoCalidadValidado()!= null ? entidad.getCertificadoCalidadValidado(): "null")
                        + ", SEGUNDOLOCALMISMOAMB_VAL = "+(entidad.getSegundosLocalesMismoAmbitoValidado()!= null ? entidad.getSegundosLocalesMismoAmbitoValidado(): "null")
                        + ", COLEC_ENT_ESAGRUPACION_VAL = "+(entidad.getEsAgrupacionValidado()!= null ? entidad.getEsAgrupacionValidado(): "null")
                        + ", COMP_IGUALDAD_OPCION = "+(entidad.getCompIgualdadOpcion()!= null ? entidad.getCompIgualdadOpcion(): "null")
                        + ", COMP_IGUALDAD_OPCION_VAL = "+(entidad.getCompIgualdadOpcionVal()!= null ? entidad.getCompIgualdadOpcionVal(): "null")
                        + ", ENT_SUJETA_DER_PUBL = "+(entidad.getEntSujetaDerPubl()!= null ? entidad.getEntSujetaDerPubl(): "null")
                        + ", ENT_SUJETA_DER_PUBL_VAL = "+(entidad.getEntSujetaDerPublVal()!= null ? entidad.getEntSujetaDerPublVal(): "null")
                        + ", ENT_SIN_ANIMO_LUCRO = "+(entidad.getEntSinAnimoLucro()!= null ? entidad.getEntSinAnimoLucro(): "null")
                        + ", ENT_SIN_ANIMO_LUCRO_VAL = "+(entidad.getEntSinAnimoLucroVal()!= null ? entidad.getEntSinAnimoLucroVal(): "null")
                        + " where COLEC_ENT_COD = '"+entidad.getCodEntidad()+"'"
                        + " and COLEC_NUMEXP = '"+entidad.getNumExp()+"'";
            }
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            int res = st.executeUpdate(query);
            if(res > 0)
            {
                return entidad;
            }
            else
            {
                return null;
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error guardando los datos de la entidad "+(entidad != null ? entidad.getCodEntidad(): "(entidad = null)")+" para el expediente "+(entidad != null ? entidad.getNumExp() : "(entidad = null)"), ex);
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
            catch(SQLException e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    
    public ColecRepresentanteVO guardarColecRepresentanteVO(ColecRepresentanteVO representante, Connection con) throws Exception
    {
        Statement st = null;
        try
        {
            String query = null;
            if(representante.getCodRepresentante()== null)
            {
                Long codRepresentante = this.getNextId(ConfigurationParameter.getParameter(ConstantesMeLanbide48.COLEC_REPRESENTANTES_SQ, ConstantesMeLanbide48.FICHERO_PROPIEDADES), con);
                if(codRepresentante == null)
                {
                    throw new Exception();
                }
                representante.setCodRepresentante(codRepresentante);
                
                //Es un representante nuevo
                query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_REPRESENTANTES, ConstantesMeLanbide48.FICHERO_PROPIEDADES)
                        + "(COLEC_COD_REPRESENTANTES, COLEC_NUMEXP, COLEC_EXP_EJE"
                        + ", COLEC_TIPO_DOC, COLEC_DOCUMENTO, COLEC_NOMBRE_APE, COLEC_FEC_SYSDATE)"
                        + " values("+representante.getCodRepresentante()
                        + ", "+(representante.getNumExp() != null ? "'"+representante.getNumExp()+"'" : "null")
                        + ", "+(representante.getEjercicio()!= null ? representante.getEjercicio() : "null")
                        + ", "+(representante.getCodTipoDoc()!= null ? "'"+representante.getCodTipoDoc()+"'" : "null")
                        + ", "+(representante.getDocumento()!= null ? "'"+representante.getDocumento()+"'" : "null")
                        + ", "+(representante.getNombre()!= null ? "'"+representante.getNombre()+"'" : "null")
                        + ", SYSDATE"
                        + ")";
            }
            else
            {
                //Es un representante que ya existe
                query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_REPRESENTANTES, ConstantesMeLanbide48.FICHERO_PROPIEDADES)
                        + " set"
                        + " COLEC_TIPO_DOC = "+(representante.getCodTipoDoc()!= null ? "'"+representante.getCodTipoDoc()+"'" : "null")
                        + ", COLEC_DOCUMENTO = "+(representante.getDocumento()!= null ? "'"+representante.getDocumento()+"'" : "null")
                        + ", COLEC_NOMBRE_APE = "+(representante.getNombre()!= null ? "'"+representante.getNombre()+"'" : "null")
                        + ", COLEC_FEC_SYSDATE = SYSDATE"
                        + " where COLEC_COD_REPRESENTANTES = '"+representante.getCodRepresentante()+"'"
                        + " and COLEC_NUMEXP = '"+representante.getNumExp()+"'";
            }
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            int res = st.executeUpdate(query);
            if(res > 0)
            {
                return representante;
            }
            else
            {
                return null;
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error guardando los datos del representante "+(representante != null ? representante.getCodRepresentante(): "(representante = null)")+" para el expediente "+(representante != null ? representante.getNumExp() : "(representante = null)"), ex);
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
            catch(SQLException e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }

    /**
     * Metodo que recupera entidades de la tabla en caso de que exista asociacion
     * @param entidad
     * @param con
     * @return
     * @throws Exception
     */
    public List<ColecEntidadEnAgrupacionVO> getListaEntidadesEnAgrupacion(ColecEntidadVO entidad, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        List<ColecEntidadEnAgrupacionVO> entidades = new ArrayList<ColecEntidadEnAgrupacionVO>();
        try
        {
            String query = "SELECT COLEC_ENT_COD,COLEC_ENT_AGRUP_COD,COLEC_NUMEXP,COLEC_ENT_TIPO_CIF,COLEC_ENT_CIF,COLEC_ENT_NOMBRE "
                    + " ,COLEC_ENT_CENTESPEMPTH,COLEC_ENT_PARTMAYCEETH,COLEC_ENT_EMPINSERCIONTH,COLEC_ENT_PROMOEMPINSTH "
                    + " ,COLEC_ENT_PORCEN_COMPROM_REALI,ACEPTNUMEROSUPEHORAS,SEGUNDOLOCALMISMOAMB "
                    + " ,nvl(CASE WHEN CEAL.PLANIGUALDAD IS NULL AND COMP_IGUALDAD_OPCION IS NOT NULL AND COMP_IGUALDAD_OPCION!=4 THEN 1 eLSE PLANIGUALDAD END,0) AS PLANIGUALDAD "
                    + " ,nvl(CASE WHEN CEAL.CERTIFICADOCALIDAD IS NULL AND dce.nroCertificados > 0 THEN 1 ELSE CERTIFICADOCALIDAD  END,0) AS CERTIFICADOCALIDAD "
                    + " ,COMP_IGUALDAD_OPCION,ENT_SUJETA_DER_PUBL,ENT_SUJETA_DER_PUBL_VAL,ENT_SIN_ANIMO_LUCRO,ENT_SIN_ANIMO_LUCRO_VAL "
                    + " FROM "+ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_ENTIDAD_AGRUP_LIST, ConstantesMeLanbide48.FICHERO_PROPIEDADES) + " ceal "
                    + " LEFT JOIN (SELECT NUM_EXP, ID_ENTIDAD, COUNT(1) nroCertificados FROM COLEC_ENTIDAD_CERT_CALIDAD cecc GROUP BY cecc.num_exp,cecc.id_entidad) dce ON  dce.num_exp=ceal.COLEC_NUMEXP  AND dce.id_entidad=ceal.COLEC_ENT_COD  "
                    + " WHERE COLEC_ENT_AGRUP_COD = " + (entidad!=null && entidad.getCodEntidad()!=null ? entidad.getCodEntidad() : "null")
                    ;
            
            
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while(rs.next())
            {
                entidades.add((ColecEntidadEnAgrupacionVO)MeLanbide48MappingUtils.getInstance().map(rs, ColecEntidadEnAgrupacionVO.class));
            }
            return entidades;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando entidades para el expediente "+ (entidad!=null && entidad.getCodEntidad()!=null ? entidad.getCodEntidad() + "/" + entidad.getNumExp() : "Entidad o Codigo de Entidad a NUll"), ex);
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
            catch(SQLException e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    public List<ColecEntidadEnAgrupacionVO> getListaEntidadesPorExpediente(String numExpediente, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        List<ColecEntidadEnAgrupacionVO> entidades = new ArrayList<ColecEntidadEnAgrupacionVO>();
        try
        {
            String query = "SELECT COLEC_ENT_COD,COLEC_ENT_AGRUP_COD,COLEC_NUMEXP,COLEC_ENT_TIPO_CIF,COLEC_ENT_CIF,COLEC_ENT_NOMBRE "
                    + " ,COLEC_ENT_CENTESPEMPTH,COLEC_ENT_PARTMAYCEETH,COLEC_ENT_EMPINSERCIONTH,COLEC_ENT_PROMOEMPINSTH "
                    + " ,COLEC_ENT_PORCEN_COMPROM_REALI,ACEPTNUMEROSUPEHORAS,SEGUNDOLOCALMISMOAMB "
                    + " ,nvl(CASE WHEN CEAL.PLANIGUALDAD IS NULL AND COMP_IGUALDAD_OPCION IS NOT NULL AND COMP_IGUALDAD_OPCION!=4 THEN 1 eLSE PLANIGUALDAD END,0) AS PLANIGUALDAD "
                    + " ,nvl(CASE WHEN CEAL.CERTIFICADOCALIDAD IS NULL AND dce.nroCertificados > 0 THEN 1 ELSE CERTIFICADOCALIDAD  END,0) AS CERTIFICADOCALIDAD "
                    + " ,COMP_IGUALDAD_OPCION,ENT_SUJETA_DER_PUBL,ENT_SUJETA_DER_PUBL_VAL,ENT_SIN_ANIMO_LUCRO,ENT_SIN_ANIMO_LUCRO_VAL "
                    + " FROM "+ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_ENTIDAD_AGRUP_LIST, ConstantesMeLanbide48.FICHERO_PROPIEDADES) + " ceal "
                    + " LEFT JOIN (SELECT NUM_EXP, ID_ENTIDAD, COUNT(1) nroCertificados FROM COLEC_ENTIDAD_CERT_CALIDAD cecc GROUP BY cecc.num_exp,cecc.id_entidad) dce ON  dce.num_exp=ceal.COLEC_NUMEXP  AND dce.id_entidad=ceal.COLEC_ENT_COD  "
                    + " WHERE COLEC_NUMEXP = '"+numExpediente+"'"
                    ;
            
            
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while(rs.next())
            {
                entidades.add((ColecEntidadEnAgrupacionVO)MeLanbide48MappingUtils.getInstance().map(rs, ColecEntidadEnAgrupacionVO.class));
            }
            return entidades;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando entidades para el expediente "+numExpediente, ex);
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
            catch(SQLException e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    
    public int eliminarColecEntidad(ColecEntidadVO p, Connection con) throws Exception
    {
        Statement st = null;
        try
        {
            String query;
                query = "delete from "+ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_ENTIDAD, ConstantesMeLanbide48.FICHERO_PROPIEDADES)
                       +" where COLEC_COD_ENTIDAD = '"+p.getCodEntidad()+"'"
                       +" and COLEC_NUMEXP = '"+p.getNumExp()+"'";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            return st.executeUpdate(query);
        }
        catch(SQLException ex)
        {
            log.error("Se ha producido un error eliminando entidad  "+(p != null ? p.getCodEntidad(): "entidad = null"), ex);
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
            catch(SQLException e)
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
            String query;
            String codProc=MeLanbide48Utils.getCodProcedimientoDeExpediente(numExp);
            query = "select TER_DOC,TER_NOC from "+ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_REL_TERCERO_DOMICILIO, ConstantesMeLanbide48.FICHERO_PROPIEDADES)+" r"
                   +" inner join "+ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_TERCEROS, ConstantesMeLanbide48.FICHERO_PROPIEDADES)+" t"
                   +" on r.EXT_TER  = t.TER_COD"
                   +" and r.EXT_MUN = '"+codOrganizacion+"' and r.EXT_EJE = '"+ejercicio+"' and r.EXT_PRO='"+codProc+"' and r.EXT_NUM = '"+numExp+"' and r.EXT_ROL = "+rol;
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                map.put("TER_DOC", rs.getString("TER_DOC"));
                map.put("TER_NOC", rs.getString("TER_NOC"));
            }
        }
        catch(SQLException ex)
        {
            log.error("Se ha producido un error recuperando las Datos Tercero", ex);
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
            catch(SQLException e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return map;
    }
    
     public int obtenerNumMeses(String fini, String ffin, Connection con) throws Exception
    {
        Statement st = null;
        int nummeses=0;
        try
        {
            String query;
            query = "select floor(months_between(to_date('"+ffin+"', 'dd/mm/yyyy')+1,to_date('"+fini+"', 'dd/mm/yyyy'))) as meses from dual";    
            
           
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next())
            {
                 nummeses =rs.getInt("meses");
               
            }
            return nummeses;
        }
        catch(SQLException ex)
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
            catch(SQLException e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    
    public String getDescripcionCampoDesplegable(String codigoCampo, String valorDesplegable, Connection con) throws Exception
    {
        String retStr = "";
        Statement st = null;
        ResultSet rs = null;
        try
        {
            
            String query = "select DES_NOM from "+ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_E_DES_VAL, ConstantesMeLanbide48.FICHERO_PROPIEDADES)
                          +" where DES_COD = '"+ConfigurationParameter.getParameter(codigoCampo, ConstantesMeLanbide48.FICHERO_PROPIEDADES)+"'"
                          +" and DES_VAL_COD = '"+valorDesplegable+"'";
            
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);

            if(rs.next())
            {
                String temp = rs.getString("DES_NOM");
                retStr = temp != null ? temp : "";
            }
        }
        catch(SQLException ex)
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
            catch(SQLException e)
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
            String query = "select MAX(CRO_TRA) as CRO_TRA from " + ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_E_CRO, ConstantesMeLanbide48.FICHERO_PROPIEDADES)
                        +" where CRO_PRO = '"+ConstantesMeLanbide48.COD_PROCEDIMIENTO+"'"
                        +" and CRO_EJE = "+ejercicio+" and CRO_NUM = '"+numExp+"' and CRO_OCU = 1 and CRO_FEF is null";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
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
        catch(SQLException ex)
        {
            con.rollback();
            log.error("Se ha producido un error recuperando las ï¿½reas", ex);
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
            catch(SQLException e)
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
            String sql = "select TRA_COD from "+ ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_TRAMITES, ConstantesMeLanbide48.FICHERO_PROPIEDADES)
                        +" where TRA_MUN = "+entorno
                        +" and TRA_PRO = '"+ConstantesMeLanbide48.COD_PROCEDIMIENTO+"'"
                        +" and TRA_COU = "+ConfigurationParameter.getParameter(codTramite, ConstantesMeLanbide48.FICHERO_PROPIEDADES);

            if(log.isDebugEnabled()) 
                    log.error("sql = " + sql);
            
            st = con.createStatement();
            rs = st.executeQuery(sql);
            if(rs.next())
            {
                cod = rs.getLong("TRA_COD");
                if(rs.wasNull())
                    cod = 0L;
            }
        }
        catch(SQLException ex)
        {
            log.error("Se ha producido un error recuperando el cï¿½digo interno del trï¿½mite : "+codTramite, ex);
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
            catch(SQLException e)
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
            String sql = "select * from "+ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_E_CRO, ConstantesMeLanbide48.FICHERO_PROPIEDADES)
                    +" where CRO_MUN = "+codOrganizacion
                    +" and CRO_PRO = '"+ConstantesMeLanbide48.COD_PROCEDIMIENTO+"'"
                    +" and CRO_EJE = "+ejercicio
                    +" and CRO_NUM = '"+numExp+"'"
                    +" and CRO_TRA = "+codTramite;

            if(log.isDebugEnabled()) 
                    log.error("sql = " + sql);
            
            st = con.createStatement();
            rs = st.executeQuery(sql);
            if(rs.next())
            {
                tramiteIniciado = true;
            }
        }
        catch(SQLException ex)
        {
            log.error("Se ha producido un error recuperando el cï¿½digo interno del trï¿½mite : "+codTramite, ex);
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
            catch(SQLException e)
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
            String sql = "select * from "+ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_E_CRO, ConstantesMeLanbide48.FICHERO_PROPIEDADES)
                    +" where CRO_MUN = "+codOrganizacion
                    +" and CRO_PRO = '"+ConstantesMeLanbide48.COD_PROCEDIMIENTO+"'"
                    +" and CRO_EJE = "+ejercicio
                    +" and CRO_NUM = '"+numExp+"'"
                    +" and CRO_TRA = "+codTramite
                    +" and CRO_FEF is not null";

            if(log.isDebugEnabled()) 
                    log.error("sql = " + sql);
            
            st = con.createStatement();
            rs = st.executeQuery(sql);
            if(rs.next())
            {
                tramiteIniciado = true;
            }
        }
        catch(SQLException ex)
        {
            log.error("Se ha producido un error recuperando el cï¿½digo interno del trï¿½mite : "+codTramite, ex);
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
            catch(SQLException e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    	return tramiteIniciado;
    }
    
    public ColecSolicitudVO getSolicitudPorId(Integer s, Connection con) throws Exception
    {
        if(s != null)
        {
            Statement st = null;
            ResultSet rs = null;
            ColecSolicitudVO solicitud = null;
            try
            {
                String query = "SELECT *"
                              +" from "+ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_SOLICITUD, ConstantesMeLanbide48.FICHERO_PROPIEDADES)
                              +" where COLEC_COD_SOLICITUD = "+s
                        ;
                if(log.isDebugEnabled()) 
                    log.error("sql = " + query);
                st = con.createStatement();
                rs = st.executeQuery(query);
                if(rs.next())
                {
                    solicitud = (ColecSolicitudVO)MeLanbide48MappingUtils.getInstance().map(rs, ColecSolicitudVO.class);
                }
                return solicitud;
            }
            catch(Exception ex)
            {
                log.error("Se ha producido un error recuperando solicitud para expediente "+ s, ex);
                throw new Exception(ex);
            }
            finally
            {
                try
                {
                    if(log.isDebugEnabled()) { 
                        log.debug("Procedemos a cerrar el statement y el resultset");
                    }
                    if(st!=null) {
                        st.close();
                    }
                    if(rs!=null) {
                        rs.close();
                    }
                }
                catch(SQLException e)
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
    
    public List<ColecSolicitudVO> getSolicitudPorExpediente(ColecSolicitudVO s, Integer idioma,Integer idBDConvocaoriaExpte, Connection con) throws Exception
    {
        List<ColecSolicitudVO> respuesta = new ArrayList<ColecSolicitudVO>();
        if(s != null && s.getNumExp() != null)
        {
            Statement st = null;
            ResultSet rs = null;
            try
            {
                Integer ejercicio = MeLanbide48Utils.getEjercicioDeExpediente(s.getNumExp());
                String query = "SELECT colec_solicitud.* "
                        + " ,cc.descripcion colectivodesc,cc.descripcioneu colectivodesc_eu "
                        + " ,ca.colec_desc_ambito territoriohistoricodesc,ca.colec_desc_ambito territoriohistoricodesc_eu "
                        + " ,case when 3="+(idBDConvocaoriaExpte!=null?idBDConvocaoriaExpte:"null")+"then cabh.ambitodescripcion else cco.colec_desc_comarca end as ambitocomarcadesc "
                        + " ,case when 3="+(idBDConvocaoriaExpte!=null?idBDConvocaoriaExpte:"null")+" then cabh.ambitodescripcioneu else cco.colec_desc_comarca_eu end as ambitocomarcadesc_eu "
                        + " from "+ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_SOLICITUD, ConstantesMeLanbide48.FICHERO_PROPIEDADES)
                        + " left join colec_colectivo cc on cc.id=CODIGOCOLECTIVO "
                        + " left join colec_ambito ca on ca.colec_cod_ambito=TERRITORIOHISTORICO "
                        + " left join colec_comarcas cco on cco.colec_cod_comarca=AMBITOCOMARCA "
                        + " left join COLEC_AMBITOS_BLOQUES_HORAS cabh on cabh.idfkconvocatoriaactiva="+(idBDConvocaoriaExpte!=null?idBDConvocaoriaExpte:"null")+" and cabh.colectivo=CODIGOCOLECTIVO and  cabh.id=colec_solicitud.ambitocomarca "
                        + " where COLEC_NUMEXP = '"+s.getNumExp()+"' AND COLEC_EXP_EJE="+ejercicio
                        ;
                if(log.isDebugEnabled()) 
                    log.error("sql = " + query);
                st = con.createStatement();
                rs = st.executeQuery(query);
                while(rs.next())
                {
                    ColecSolicitudVO datos = (ColecSolicitudVO)MeLanbide48MappingUtils.getInstance().map(rs, ColecSolicitudVO.class);
                    // Agregamos las descripcions de los valores desplegables segun el idioma
                    if(idioma!=null && 4==idioma){
                        //Euskera
                        datos.setCodigoColectivoDesc(rs.getString("colectivodesc_eu"));
                        datos.setTerritorioHistoricoDesc(rs.getString("territoriohistoricodesc_eu"));
                        datos.setAmbitoComarcaDesc(rs.getString("ambitocomarcadesc_eu"));
                    }else{
                        // POr defecto cargamos castellano
                        datos.setCodigoColectivoDesc(rs.getString("colectivodesc"));
                        datos.setTerritorioHistoricoDesc(rs.getString("territoriohistoricodesc"));
                        datos.setAmbitoComarcaDesc(rs.getString("ambitocomarcadesc"));
                    }
                    respuesta.add(datos);
                }
                return respuesta;
            }
            catch(Exception ex)
            {
                log.error("Se ha producido un error recuperando solicitud para expediente "+ s.getNumExp(), ex);
                throw new Exception(ex);
            }
            finally
            {
                try
                {
                    if(log.isDebugEnabled()) { 
                        log.debug("Procedemos a cerrar el statement y el resultset");
                    }
                    if(st!=null) {
                        st.close();
                    }
                    if(rs!=null) {
                        rs.close();
                    }
                }
                catch(SQLException e)
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
    
    public ColecSolicitudVO guardarColecSolicitudVO(ColecSolicitudVO solicitud, Connection con) throws Exception
    {
        Statement st = null;
        try
        {
            String query = null;
            if(solicitud.getCodSolicitud()== null)
            {
                Long codSolicitud = this.getNextId(ConfigurationParameter.getParameter(ConstantesMeLanbide48.COLEC_SOLICITUD_SQ, ConstantesMeLanbide48.FICHERO_PROPIEDADES), con);
                if(codSolicitud == null)
                {
                    throw new Exception();
                }
                solicitud.setCodSolicitud(codSolicitud);
                
                //Es una entidad nueva
                query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_SOLICITUD, ConstantesMeLanbide48.FICHERO_PROPIEDADES)
                        + "(COLEC_COD_SOLICITUD, COLEC_NUMEXP, COLEC_EXP_EJE, COLEC_COL1_AR, COLEC_COL1_BI, COLEC_COL1_GI,"
                        + " COLEC_COL2_AR, COLEC_COL2_BI, COLEC_COL2_GI, COLEC_COL3_AR, COLEC_COL3_BI, COLEC_COL3_GI,"
                        + " COLEC_COL4_AR, COLEC_COL4_BI, COLEC_COL4_GI, COLEC1_ULT5_VAL, COLEC2_ULT5_VAL, COLEC_TOT_VAL "
                        + " ,CODIGOCOLECTIVO, TERRITORIOHISTORICO, AMBITOCOMARCA, NUMEROBLOQUESHORAS, NUMEROUBICACIONES "
                        + ")"
                        + " values("+solicitud.getCodSolicitud()
                        + ", "+(solicitud.getNumExp() != null ? "'"+solicitud.getNumExp()+"'" : "null")
                        + ", "+(solicitud.getEjercicio() != null ? solicitud.getEjercicio() : "null")
                        + ", "+(solicitud.getCol1Ar() != null ? solicitud.getCol1Ar() : "null")
                        + ", "+(solicitud.getCol1Bi() != null ? solicitud.getCol1Bi() : "null")
                        + ", "+(solicitud.getCol1Gi() != null ? solicitud.getCol1Gi() : "null")
                        
                        + ", "+(solicitud.getCol2Ar() != null ? solicitud.getCol2Ar() : "null")
                        + ", "+(solicitud.getCol2Bi() != null ? solicitud.getCol2Bi() : "null")
                        + ", "+(solicitud.getCol2Gi() != null ? solicitud.getCol2Gi() : "null")
                        
                        + ", "+(solicitud.getCol3Ar() != null ? solicitud.getCol3Ar() : "null")
                        + ", "+(solicitud.getCol3Bi() != null ? solicitud.getCol3Bi() : "null")
                        + ", "+(solicitud.getCol3Gi() != null ? solicitud.getCol3Gi() : "null")
                        
                        + ", "+(solicitud.getCol4Ar() != null ? solicitud.getCol4Ar() : "null")
                        + ", "+(solicitud.getCol4Bi() != null ? solicitud.getCol4Bi() : "null")
                        + ", "+(solicitud.getCol4Gi() != null ? solicitud.getCol4Gi() : "null")
                        
                        + ", "+(solicitud.getCol1Ult5Val() != null ? solicitud.getCol1Ult5Val() : "null")
                        + ", "+(solicitud.getCol2Ult5Val() != null ? solicitud.getCol2Ult5Val() : "null")
                        + ", "+(solicitud.getTotVal() != null ? solicitud.getTotVal() : "null")
                        + ", "+(solicitud.getCodigoColectivo()!= null ? solicitud.getCodigoColectivo(): "null")
                        + ", "+(solicitud.getTerritorioHistorico()!= null ? solicitud.getTerritorioHistorico(): "null")
                        + ", "+(solicitud.getAmbitoComarca()!= null ? solicitud.getAmbitoComarca(): "null")
                        + ", "+(solicitud.getNumeroBloquesHoras()!= null ? solicitud.getNumeroBloquesHoras(): "null")
                        + ", "+(solicitud.getNumeroUbicaciones()!= null ? solicitud.getNumeroUbicaciones(): "null")
                        + ")";
            }
            else
            {
                //Es una entidad que ya existe
                query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_SOLICITUD, ConstantesMeLanbide48.FICHERO_PROPIEDADES)
                        + " set"
                        + " COLEC_COL1_AR = "+(solicitud.getCol1Ar() != null ? solicitud.getCol1Ar() : "null")
                        + ", COLEC_COL1_BI = "+(solicitud.getCol1Bi() != null ? solicitud.getCol1Bi() : "null")
                        + ", COLEC_COL1_GI = "+(solicitud.getCol1Gi() != null ? solicitud.getCol1Gi() : "null")
                        
                        + ", COLEC_COL2_AR = "+(solicitud.getCol2Ar() != null ? solicitud.getCol2Ar() : "null")
                        + ", COLEC_COL2_BI = "+(solicitud.getCol2Bi() != null ? solicitud.getCol2Bi() : "null")
                        + ", COLEC_COL2_GI = "+(solicitud.getCol2Gi() != null ? solicitud.getCol2Gi() : "null")
                        
                        + ", COLEC_COL3_AR = "+(solicitud.getCol3Ar() != null ? solicitud.getCol3Ar() : "null")
                        + ", COLEC_COL3_BI = "+(solicitud.getCol3Bi() != null ? solicitud.getCol3Bi() : "null")
                        + ", COLEC_COL3_GI = "+(solicitud.getCol3Gi() != null ? solicitud.getCol3Gi() : "null")
                        
                        + ", COLEC_COL4_AR = "+(solicitud.getCol4Ar() != null ? solicitud.getCol4Ar() : "null")
                        + ", COLEC_COL4_BI = "+(solicitud.getCol4Bi() != null ? solicitud.getCol4Bi() : "null")
                        + ", COLEC_COL4_GI = "+(solicitud.getCol4Gi() != null ? solicitud.getCol4Gi() : "null")
                        
                        + ", COLEC1_ULT5_VAL = "+(solicitud.getCol1Ult5Val() != null ? solicitud.getCol1Ult5Val() : "null")
                        + ", COLEC2_ULT5_VAL = "+(solicitud.getCol2Ult5Val() != null ? solicitud.getCol2Ult5Val() : "null")
                        + ", COLEC_TOT_VAL = "+(solicitud.getTotVal() != null ? solicitud.getTotVal() : "null")
                        + ", codigoColectivo = "+(solicitud.getCodigoColectivo()!= null ? solicitud.getCodigoColectivo() : "null")
                        + ", territorioHistorico = "+(solicitud.getTerritorioHistorico()!= null ? solicitud.getTerritorioHistorico(): "null")
                        + ", AMBITOCOMARCA = "+(solicitud.getAmbitoComarca()!= null ? solicitud.getAmbitoComarca(): "null")
                        + ", numeroBloquesHoras = "+(solicitud.getNumeroBloquesHoras()!= null ? solicitud.getNumeroBloquesHoras(): "null")
                        + ", numeroUbicaciones = "+(solicitud.getNumeroUbicaciones()!= null ? solicitud.getNumeroUbicaciones() : "null")
                        + " where COLEC_COD_SOLICITUD = '"+solicitud.getCodSolicitud()+"'"
                        + " and COLEC_NUMEXP = '"+solicitud.getNumExp()+"'";
            }
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            int res = st.executeUpdate(query);
            if(res > 0)
            {
                return solicitud;
            }
            else
            {
                return null;
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error guardando los datos de la solicitud "+(solicitud != null ? solicitud.getCodSolicitud(): "(solicitud = null)")+" para el expediente "+(solicitud != null ? solicitud.getNumExp() : "(solicitud = null)"), ex);
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
            catch(SQLException e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    
    private Long getNextId(String seqName, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        Long numSec = null;
        try
        {
            String query = null;
            //Creo el id con la secuencia
            query = "select "+ seqName+".nextval from dual";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                numSec = rs.getLong(1);
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
        return numSec;
    }
    
    public List<SelectItem> getListaProvincias(Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        List<SelectItem> retList = new ArrayList<SelectItem>();
        try
        {
            String query = null;
            query = "select COLEC_COD_AMBITO CODIGO, COLEC_DESC_AMBITO DESCRIPCION from "+ ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_AMBITO, ConstantesMeLanbide48.FICHERO_PROPIEDADES);
            query += " order by CODIGO";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while(rs.next())
            {
                retList.add((SelectItem)MeLanbide48MappingUtils.getInstance().map(rs, SelectItem.class));
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando provincias.", ex);
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
    
    public List<SelectItem> getListaComarcas(Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        List<SelectItem> retList = new ArrayList<SelectItem>();
        try
        {
            String query = null;
            query = "select COLEC_COD_COMARCA CODIGO, COLEC_DESC_COMARCA DESCRIPCION from "+ ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_COMARCAS, ConstantesMeLanbide48.FICHERO_PROPIEDADES)
                   +" order by DESCRIPCION";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while(rs.next())
            {
                retList.add((SelectItem)MeLanbide48MappingUtils.getInstance().map(rs, SelectItem.class));
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando provincias.", ex);
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
    
    public List<SelectItem> getComarcasPorAmbito(Integer codAmbito, Connection con)  throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        List<SelectItem> retList = new ArrayList<SelectItem>();
        try
        {
            String query = null;
            query = "select COLEC_COD_COMARCA CODIGO, COLEC_DESC_COMARCA DESCRIPCION from "+ ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_COMARCAS, ConstantesMeLanbide48.FICHERO_PROPIEDADES)
                   +" where COLEC_COD_AMBITO = '"+codAmbito+"'"
                   +" order by DESCRIPCION";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while(rs.next())
            {
                retList.add((SelectItem)MeLanbide48MappingUtils.getInstance().map(rs, SelectItem.class));
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando comarcas para el ambito "+(codAmbito != null ? codAmbito : " (codAmbito = null)"), ex);
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
    
    public List<String> getCodigosComarcaPorAmbito(String codAmbito, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        List<String> retList = new ArrayList<String>();
        if(codAmbito != null && !codAmbito.equals(""))
        {
            try
            {
                String query = null;
                query = "select COLEC_COD_COMARCA"
                       +" from "+ ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_COMARCAS, ConstantesMeLanbide48.FICHERO_PROPIEDADES)
                       +" where COLEC_COD_AMBITO = "+codAmbito;
                
                if(log.isDebugEnabled()) 
                    log.error("sql = " + query);
                st = con.createStatement();
                rs = st.executeQuery(query);
                Long codigo = null;
                while(rs.next())
                {
                    codigo = rs.getLong("COLEC_COD_COMARCA");
                    if(!rs.wasNull())
                    {
                        retList.add(String.valueOf(codigo));
                    }
                }
            }
            catch(Exception ex)
            {
                log.error("Se ha producido una excepcion en la BBDD recuperando codigos de comarca para ambito "+(codAmbito != null ? codAmbito : " (codAmbito = null)"), ex);
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
    
    public int eliminarCentros(List<ColecCentrosVO> centrosEliminar, Connection con) throws Exception
    {
        Statement st = null;
        int result = 0;
        if(centrosEliminar != null && centrosEliminar.size() > 0)
        {
            try
            {
                String query = null;
                query = "delete from "+ ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_CENTROS, ConstantesMeLanbide48.FICHERO_PROPIEDADES)
                       +" where COLEC_COD_CENTROS = "+centrosEliminar.get(0).getCodCentro();
                    for(int i = 1; i < centrosEliminar.size(); i++)
                    {
                        query += "or COLEC_COD_CENTROS = "+centrosEliminar.get(i).getCodCentro();
                    }
                
                if(log.isDebugEnabled()) 
                    log.error("sql = " + query);
                st = con.createStatement();
                result = st.executeUpdate(query);
            }
            catch(Exception ex)
            {
                log.error("Se ha producido una excepcion en la BBDD eliminando centros ", ex);
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
        return result;
    }
    
    public ColecCentrosVO guardarCentro(ColecCentrosVO centro, Connection con) throws Exception
    {
        Statement st = null;
        try
        {
            String query = null;
            if(centro.getCodCentro() == null)
            {
                Long codCentro = this.getNextId(ConfigurationParameter.getParameter(ConstantesMeLanbide48.COLEC_CENTROS_SQ, ConstantesMeLanbide48.FICHERO_PROPIEDADES), con);
                if(codCentro == null)
                {
                    throw new Exception();
                }
                centro.setCodCentro(codCentro);
                
                //Es un representante nuevo
                query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_CENTROS, ConstantesMeLanbide48.FICHERO_PROPIEDADES)
                        + "(COLEC_COD_CENTROS, COLEC_NUMEXP, COLEC_EXP_EJE"
                        + ", COLEC_AMBITO, COLEC_COLECTIVO, COLEC_COMARCA, COLEC_MUN, COLEC_DIRECCION, COLEC_FEC_SYSDATE)"
                        + " values("+centro.getCodCentro()
                        + ", "+(centro.getNumExp() != null ? "'"+centro.getNumExp()+"'" : "null")
                        + ", "+centro.getExpEje()
                        + ", "+centro.getAmbito()
                        + ", "+centro.getColectivo()
                        + ", "+centro.getComarca()
                        + ", "+centro.getMun()
                        + ", "+(centro.getDireccion()!= null ? "'"+centro.getDireccion()+"'" : "null")
                        + ", SYSDATE"
                        + ")";
            }
            else
            {
                //Es un representante que ya existe
                query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_CENTROS, ConstantesMeLanbide48.FICHERO_PROPIEDADES)
                        + " set"
                        + " COLEC_AMBITO = "+centro.getAmbito()
                        + ", COLEC_COLECTIVO = "+centro.getColectivo()
                        + ", COLEC_COMARCA = "+centro.getComarca()
                        + ", COLEC_MUN = "+centro.getMun()
                        + ", COLEC_DIRECCION = "+(centro.getDireccion() != null ? "'"+centro.getDireccion()+"'" : "null")
                        + ", COLEC_FEC_SYSDATE = SYSDATE"
                        + " where COLEC_COD_CENTROS = '"+centro.getCodCentro()+"'"
                        + " and COLEC_NUMEXP = '"+centro.getNumExp()+"'";
            }
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            int res = st.executeUpdate(query);
            if(res > 0)
            {
                return centro;
            }
            else
            {
                return null;
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error guardando los datos del centro "+(centro != null ? centro.getCodCentro(): "(centro = null)")+" para el expediente "+(centro != null ? centro.getNumExp() : "(centro = null)"), ex);
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
            catch(SQLException e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    
    public List<ColecUbicacionesCTVO> getUbicacionesCTPorExpedienteYColectivo(Integer idioma,Integer idBDConvocataria,String numExp, Integer colectivo, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        List<ColecUbicacionesCTVO> retList = new ArrayList<ColecUbicacionesCTVO>();
        if(numExp != null && !numExp.equals("") && colectivo != null)
        {
            try
            {
                String nombreTablaEntidad = definirTablaConsultaEntidadSegunEntAgrupSioNo(numExp,con);
                String query = null;
                query = " SELECT C.* " +
                        ",DATOS_ENTIDAD.COLEC_ENT_CIF " +
                        ",DATOS_ENTIDAD.COLEC_ENT_NOMBRE " +
                        ",COLEC_AMBITO.COLEC_DESC_AMBITO DESC_TERRITORIO " +
                        ",COLEC_COMARCAS.COLEC_DESC_COMARCA DESC_COMARCA " +
                        ",COLEC_MUNICIPIO.COLEC_DESC_MUN DESC_MUNICPIO "
                       + ",(case when 4=" + (idioma != null ? idioma : 1) + " then cabh.ambitodescripcioneu else cabh.ambitodescripcion end) desc_ambitosolicitado "
                       +" from "+ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_UBICACIONES_CT, ConstantesMeLanbide48.FICHERO_PROPIEDADES)+" C "
                       +" LEFT JOIN "+nombreTablaEntidad+ " DATOS_ENTIDAD ON C.COLEC_UBIC_CT_NUMEXP=DATOS_ENTIDAD.COLEC_NUMEXP AND C.COLEC_UBIC_CT_CODENTIDAD=DATOS_ENTIDAD.COLEC_ENT_COD " 
                       +" left join "+ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_AMBITO, ConstantesMeLanbide48.FICHERO_PROPIEDADES)+" ON COLEC_AMBITO.COLEC_COD_AMBITO=C.COLEC_UBIC_CT_TERRITORIO "
                       +" left join "+ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_COMARCAS, ConstantesMeLanbide48.FICHERO_PROPIEDADES)+" on COLEC_COMARCAS.COLEC_COD_AMBITO=C.COLEC_UBIC_CT_TERRITORIO and COLEC_COMARCAS.COLEC_COD_COMARCA=C.COLEC_UBIC_CT_NROCOMARCA "
                       +" left join "+ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_MUN, ConstantesMeLanbide48.FICHERO_PROPIEDADES)+" on COLEC_MUNICIPIO.COLEC_COD_COMARCA=C.COLEC_UBIC_CT_NROCOMARCA and COLEC_MUNICIPIO.COLEC_COD_MUN=C.COLEC_UBIC_CT_MUNICIPIO "
                       +" left join COLEC_AMBITOS_BLOQUES_HORAS cabh on cabh.idfkconvocatoriaactiva=" + (idBDConvocataria != null ? idBDConvocataria : "null") + " and cabh.colectivo=c.COLEC_UBIC_CT_TIPO and  cabh.id=c.FK_ID_AMBITO_SOLICITADO "
                       +" where c.COLEC_UBIC_CT_NUMEXP = '"+numExp+"' and C.COLEC_UBIC_CT_TIPO = "+colectivo
                       +" order by DESC_TERRITORIO, DESC_COMARCA, DESC_MUNICPIO";
                
                if(log.isDebugEnabled()) 
                    log.error("sql = " + query);
                st = con.createStatement();
                rs = st.executeQuery(query);
                while(rs.next())
                {
                    retList.add((ColecUbicacionesCTVO)MeLanbide48MappingUtils.getInstance().map(rs, ColecUbicacionesCTVO.class));
                }
            }
            catch(Exception ex)
            {
                log.error("Se ha producido una excepcion en la BBDD recuperando centros para expediente "+(numExp != null ? numExp : " (expediente = null)")+" y colectivo "+(colectivo != null ? colectivo : " (colectivo = null)"), ex);
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
    
    public ColecUbicacionesCTVO getUbicacionCTPorCodigoYExpediente(Integer idioma,Integer idBDConvocataria,ColecUbicacionesCTVO ubicacionCT, Connection con) throws Exception
    {
        log.debug("getUbicacionCTPorCodigoYExpediente - Begin()");
        Statement st = null;
        ResultSet rs = null;
        if(ubicacionCT != null && ubicacionCT.getCodId()!= null)
        {
            if(ubicacionCT != null && ubicacionCT.getNumExpediente()!= null && !ubicacionCT.getNumExpediente().equals(""))
            {
                try
                {
                    String nombreTablaEntidad = definirTablaConsultaEntidadSegunEntAgrupSioNo(ubicacionCT.getNumExpediente(), con);
                    String query = "";
                    query = " SELECT C.* "
                            + ",DATOS_ENTIDAD.COLEC_ENT_CIF "
                            + ",DATOS_ENTIDAD.COLEC_ENT_NOMBRE "
                            + ",COLEC_AMBITO.COLEC_DESC_AMBITO DESC_TERRITORIO "
                            + ",COLEC_COMARCAS.COLEC_DESC_COMARCA DESC_COMARCA "
                            + ",COLEC_MUNICIPIO.COLEC_DESC_MUN DESC_MUNICPIO "
                            + ",(case when 4=" + (idioma != null ? idioma : 1) + " then cabh.ambitodescripcioneu else cabh.ambitodescripcion end) desc_ambitosolicitado "
                            + " from " + ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_UBICACIONES_CT, ConstantesMeLanbide48.FICHERO_PROPIEDADES) + " C "
                            + " LEFT JOIN " + nombreTablaEntidad + " DATOS_ENTIDAD ON C.COLEC_UBIC_CT_NUMEXP=DATOS_ENTIDAD.COLEC_NUMEXP AND C.COLEC_UBIC_CT_CODENTIDAD=DATOS_ENTIDAD.COLEC_ENT_COD "
                            + " left join " + ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_AMBITO, ConstantesMeLanbide48.FICHERO_PROPIEDADES) + " ON COLEC_AMBITO.COLEC_COD_AMBITO=C.COLEC_UBIC_CT_TERRITORIO "
                            + " left join " + ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_COMARCAS, ConstantesMeLanbide48.FICHERO_PROPIEDADES) + " on COLEC_COMARCAS.COLEC_COD_AMBITO=C.COLEC_UBIC_CT_TERRITORIO and COLEC_COMARCAS.COLEC_COD_COMARCA=C.COLEC_UBIC_CT_NROCOMARCA "
                            + " left join " + ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_MUN, ConstantesMeLanbide48.FICHERO_PROPIEDADES) + " on COLEC_MUNICIPIO.COLEC_COD_COMARCA=C.COLEC_UBIC_CT_NROCOMARCA and COLEC_MUNICIPIO.COLEC_COD_MUN=C.COLEC_UBIC_CT_MUNICIPIO "
                            + " left join COLEC_AMBITOS_BLOQUES_HORAS cabh on cabh.idfkconvocatoriaactiva=" + (idBDConvocataria != null ? idBDConvocataria : "null") + " and cabh.colectivo=c.COLEC_UBIC_CT_TIPO and  cabh.id=c.FK_ID_AMBITO_SOLICITADO "
                            + " where c.COLEC_UBIC_CT_NUMEXP = '" + ubicacionCT.getNumExpediente() + "' and C.COLEC_UBIC_CT_COD = " + ubicacionCT.getCodId()
                            ;

                    if(log.isDebugEnabled()) 
                        log.error("sql = " + query);
                    st = con.createStatement();
                    rs = st.executeQuery(query);
                    if(rs.next())
                    {
                        ubicacionCT = ((ColecUbicacionesCTVO)MeLanbide48MappingUtils.getInstance().map(rs, ColecUbicacionesCTVO.class));
                    }
                }
                catch(Exception ex)
                {
                    log.error("Se ha producido una excepcion en la BBDD recuperando UBICACION "+(ubicacionCT != null ? ubicacionCT.getCodId() : "(centro = null)")+" para el expediente "+(ubicacionCT != null ? ubicacionCT.getNumExpediente(): " (centro = null)"), ex);
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
        else
        {
            return null;
        }
        return ubicacionCT;
    }
    
    public boolean eliminarUbicacionesCentroTraPorCodigoYExpediente(ColecUbicacionesCTVO centro, Connection con) throws Exception
    {
        Statement st = null;
        if(centro != null && centro.getCodId()!= null)
        {
            if(centro != null && centro.getNumExpediente()!= null && !centro.getNumExpediente().equals(""))
            {
                try
                {
                    String query = null;
                    query = "delete "
                           +" from "+ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_UBICACIONES_CT, ConstantesMeLanbide48.FICHERO_PROPIEDADES)+" c"
                           +" where c.COLEC_UBIC_CT_COD = "+centro.getCodId()+" and c.COLEC_UBIC_CT_NUMEXP = '"+centro.getNumExpediente()+"'";

                    if(log.isDebugEnabled()) 
                        log.error("sql = " + query);
                    st = con.createStatement();
                    int res = st.executeUpdate(query);
                    if(res > 0)
                    {
                        return true;
                    }
                    else
                    {
                        return false;
                    }
                }
                catch(Exception ex)
                {
                    log.error("Se ha producido una excepcion en la BBDD recuperando el centro "+(centro != null ? centro.getCodId(): "(centro = null)")+" para el expediente "+(centro != null ? centro.getNumExpediente(): " (centro = null)"), ex);
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
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }
    
    public ColecTrayVO getTrayectoriaPorEntidadYExpediente(ColecTrayVO tray, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        if(tray != null && tray.getCodEntidad() != null)
        {
            if(tray != null && tray.getNumExp() != null && !tray.getNumExp().equals(""))
            {
                try
                {
                    String query = null;
                    query = "select *"
                           +" from "+ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_TRAY, ConstantesMeLanbide48.FICHERO_PROPIEDADES)+" t"
                           +" where t.COLEC_COD_ENTIDAD = "+tray.getCodEntidad()+" and t.COLEC_NUMEXP = '"+tray.getNumExp()+"'";

                    if(log.isDebugEnabled()) 
                        log.error("sql = " + query);
                    st = con.createStatement();
                    rs = st.executeQuery(query);
                    if(rs.next())
                    {
                        tray = ((ColecTrayVO)MeLanbide48MappingUtils.getInstance().map(rs, ColecTrayVO.class));
                    }
                }
                catch(Exception ex)
                {
                    log.error("Se ha producido una excepcion en la BBDD recuperando trayectoria para entidad "+(tray != null ? tray.getCodEntidad() : "(tray = null)")+" y el expediente "+(tray != null ? tray.getNumExp() : " (tray = null)"), ex);
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
        else
        {
            return null;
        }
        return tray;
    }
    
    public boolean guardarTrayectoriaGeneral(ColecTrayVO tray, Connection con) throws Exception
    {
        Statement st = null;
        try
        {
            if(tray.getCodEntidad() == null || (tray.getNumExp() == null || tray.getNumExp().equals("")) || tray.getExpEje() == null)
            {
                return false;
            }
            else
            {
                String query = null;
                if(tray.getCodTray() == null)
                {
                    Long codTray = this.getNextId(ConfigurationParameter.getParameter(ConstantesMeLanbide48.COLEC_TRAY_SQ, ConstantesMeLanbide48.FICHERO_PROPIEDADES), con);
                    if(codTray == null)
                    {
                        throw new Exception();
                    }
                    tray.setCodTray(codTray);

                    //Es un registro nuevo
                    query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_TRAY, ConstantesMeLanbide48.FICHERO_PROPIEDADES)
                            + "(COLEC_COD_TRAY, COLEC_COD_ENTIDAD, COLEC_NUMEXP, COLEC_EXP_EJE"
                            + ", COLEC1_DEC_327_2007, COLEC1_DEC_327_2008, COLEC1_DEC_327_2009, COLEC1_DEC_327_2010"
                            + ", COLEC1_MIN_94_2007, COLEC1_MIN_94_2008, COLEC1_MIN_94_2009, COLEC1_MIN_94_2010, COLEC1_MIN_94_2011, COLEC1_MIN_94_2012, COLEC1_MIN_94_2013, COLEC1_MIN_94_2014, COLEC1_MIN_94_2015, COLEC1_MIN_94_2016, COLEC1_MIN_94_2017, COLEC1_MIN_94_2018"
                            + ", COLEC1_MIN_98_2007, COLEC1_MIN_98_2008, COLEC1_MIN_98_2009, COLEC1_MIN_98_2010, COLEC1_MIN_98_2011, COLEC1_MIN_98_2012, COLEC1_MIN_98_2013, COLEC1_MIN_98_2014, COLEC1_MIN_98_2015, COLEC1_MIN_98_2016, COLEC1_MIN_98_2017, COLEC1_MIN_98_2018"
                            + ", COLEC1_TAS_03_2007, COLEC1_TAS_03_2008, COLEC1_TAS_03_2009, COLEC1_TAS_03_2010, COLEC1_TAS_03_2011, COLEC1_TAS_03_2012, COLEC1_TAS_03_2013, COLEC1_TAS_03_2014, COLEC1_TAS_03_2015, COLEC1_TAS_03_2016, COLEC1_TAS_03_2017, COLEC1_TAS_03_2018"
                            + ", COLEC1_ACT_56_03"
                            + ", COLEC2_LAN_2011, COLEC2_LAN_2013, COLEC2_LAN_2014, COLEC2_LAN_2015, COLEC2_LAN_2017 "
                            + ", COLEC2_LAN_OTROS"
                            + ", COLEC1_ULT5_VAL, COLEC2_ULT5_VAL, COLEC_TOT_VAL"
                            + ")"
                            + " values("+tray.getCodTray()+ ", "+tray.getCodEntidad()+", '"+tray.getNumExp()+"', "+tray.getExpEje()
                            + ", "+(tray.getDec327_2007() != null ? tray.getDec327_2007() : "null")                        
                            + ", "+(tray.getDec327_2008() != null ? tray.getDec327_2008() : "null")                        
                            + ", "+(tray.getDec327_2009() != null ? tray.getDec327_2009() : "null")                        
                            + ", "+(tray.getDec327_2010() != null ? tray.getDec327_2010() : "null")         
                            + ", "+(tray.getMin94_2007() != null ? tray.getMin94_2007() : "null")
                            + ", "+(tray.getMin94_2008() != null ? tray.getMin94_2008() : "null")
                            + ", "+(tray.getMin94_2009() != null ? tray.getMin94_2009() : "null")
                            + ", "+(tray.getMin94_2010() != null ? tray.getMin94_2010() : "null")
                            + ", "+(tray.getMin94_2011() != null ? tray.getMin94_2011() : "null")
                            + ", "+(tray.getMin94_2012() != null ? tray.getMin94_2012() : "null")
                            + ", "+(tray.getMin94_2013() != null ? tray.getMin94_2013() : "null")
                            + ", "+(tray.getMin94_2014() != null ? tray.getMin94_2014() : "null")
                            + ", "+(tray.getMin94_2015() != null ? tray.getMin94_2015() : "null")
                            + ", "+(tray.getMin94_2016() != null ? tray.getMin94_2016() : "null")
                            + ", "+(tray.getMin94_2017() != null ? tray.getMin94_2017() : "null")
                            + ", "+(tray.getMin94_2018() != null ? tray.getMin94_2018() : "null")
                            + ", "+(tray.getMin98_2007() != null ? tray.getMin98_2007() : "null")
                            + ", "+(tray.getMin98_2008() != null ? tray.getMin98_2008() : "null")
                            + ", "+(tray.getMin98_2009() != null ? tray.getMin98_2009() : "null")
                            + ", "+(tray.getMin98_2010() != null ? tray.getMin98_2010() : "null")
                            + ", "+(tray.getMin98_2011() != null ? tray.getMin98_2011() : "null")
                            + ", "+(tray.getMin98_2012() != null ? tray.getMin98_2012() : "null")
                            + ", "+(tray.getMin98_2013() != null ? tray.getMin98_2013() : "null")
                            + ", "+(tray.getMin98_2014() != null ? tray.getMin98_2014() : "null")
                            + ", "+(tray.getMin98_2015() != null ? tray.getMin98_2015() : "null")
                            + ", "+(tray.getMin98_2016() != null ? tray.getMin98_2016() : "null")
                            + ", "+(tray.getMin98_2017() != null ? tray.getMin98_2017() : "null")
                            + ", "+(tray.getMin98_2018() != null ? tray.getMin98_2018() : "null")
                            + ", "+(tray.getTas03_2007() != null ? tray.getTas03_2007() : "null")
                            + ", "+(tray.getTas03_2008() != null ? tray.getTas03_2008() : "null")
                            + ", "+(tray.getTas03_2009() != null ? tray.getTas03_2009() : "null")
                            + ", "+(tray.getTas03_2010() != null ? tray.getTas03_2010() : "null")
                            + ", "+(tray.getTas03_2011() != null ? tray.getTas03_2011() : "null")
                            + ", "+(tray.getTas03_2012() != null ? tray.getTas03_2012() : "null")
                            + ", "+(tray.getTas03_2013() != null ? tray.getTas03_2013() : "null")
                            + ", "+(tray.getTas03_2014() != null ? tray.getTas03_2014() : "null")
                            + ", "+(tray.getTas03_2015() != null ? tray.getTas03_2015() : "null")
                            + ", "+(tray.getTas03_2016() != null ? tray.getTas03_2016() : "null")
                            + ", "+(tray.getTas03_2017() != null ? tray.getTas03_2017() : "null")
                            + ", "+(tray.getTas03_2018() != null ? tray.getTas03_2018() : "null")
                            + ", "+(tray.getAct56_03() != null ? tray.getAct56_03() : "null")
                            + ", "+(tray.getLan_2011() != null ? tray.getLan_2011() : "null")
                            + ", "+(tray.getLan_2013() != null ? tray.getLan_2013() : "null")
                            + ", "+(tray.getLan_2014() != null ? tray.getLan_2014() : "null")
                            + ", "+(tray.getLan_2015() != null ? tray.getLan_2015() : "null")
                            + ", "+(tray.getLan_2017() != null ? tray.getLan_2017() : "null")
                            + ", "+(tray.getLan_otros() != null ? tray.getLan_otros() : "null")
                            + ", "+(tray.getColec1_ult5_val() != null ? tray.getColec1_ult5_val() : "null")
                            + ", "+(tray.getColec2_ult5_val() != null ? tray.getColec2_ult5_val() : "null")
                            + ", "+(tray.getColec_tot_total() != null ? tray.getColec_tot_total() : "null")
                            + ")";
                }
                else
                {
                    //Es un registro que ya existe
                    query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_TRAY, ConstantesMeLanbide48.FICHERO_PROPIEDADES)
                            + " set"
                            + " COLEC1_DEC_327_2007 = "+(tray.getDec327_2007() != null ? tray.getDec327_2007() : "null")
                            + ", COLEC1_DEC_327_2008 = "+(tray.getDec327_2008() != null ? tray.getDec327_2008() : "null")
                            + ", COLEC1_DEC_327_2009 = "+(tray.getDec327_2009() != null ? tray.getDec327_2009() : "null")
                            + ", COLEC1_DEC_327_2010 = "+(tray.getDec327_2010() != null ? tray.getDec327_2010() : "null")
                            + ", COLEC1_MIN_94_2007 = "+(tray.getMin94_2007() != null ? tray.getMin94_2007() : "null")
                            + ", COLEC1_MIN_94_2008 = "+(tray.getMin94_2008() != null ? tray.getMin94_2008() : "null")
                            + ", COLEC1_MIN_94_2009 = "+(tray.getMin94_2009() != null ? tray.getMin94_2009() : "null")
                            + ", COLEC1_MIN_94_2010 = "+(tray.getMin94_2010() != null ? tray.getMin94_2010() : "null")
                            + ", COLEC1_MIN_94_2011 = "+(tray.getMin94_2011() != null ? tray.getMin94_2011() : "null")
                            + ", COLEC1_MIN_94_2012 = "+(tray.getMin94_2012() != null ? tray.getMin94_2012() : "null")
                            + ", COLEC1_MIN_94_2013 = "+(tray.getMin94_2013() != null ? tray.getMin94_2013() : "null")
                            + ", COLEC1_MIN_94_2014 = "+(tray.getMin94_2014() != null ? tray.getMin94_2014() : "null")
                            + ", COLEC1_MIN_94_2015 = "+(tray.getMin94_2015() != null ? tray.getMin94_2015() : "null")
                            + ", COLEC1_MIN_94_2016 = "+(tray.getMin94_2016() != null ? tray.getMin94_2016() : "null")
                            + ", COLEC1_MIN_94_2017 = "+(tray.getMin94_2017() != null ? tray.getMin94_2017() : "null")
                            + ", COLEC1_MIN_94_2018 = "+(tray.getMin94_2018() != null ? tray.getMin94_2018() : "null")
                            + ", COLEC1_MIN_98_2007 = "+(tray.getMin98_2007() != null ? tray.getMin98_2007() : "null")
                            + ", COLEC1_MIN_98_2008 = "+(tray.getMin98_2008() != null ? tray.getMin98_2008() : "null")
                            + ", COLEC1_MIN_98_2009 = "+(tray.getMin98_2009() != null ? tray.getMin98_2009() : "null")
                            + ", COLEC1_MIN_98_2010 = "+(tray.getMin98_2010() != null ? tray.getMin98_2010() : "null")
                            + ", COLEC1_MIN_98_2011 = "+(tray.getMin98_2011() != null ? tray.getMin98_2011() : "null")
                            + ", COLEC1_MIN_98_2012 = "+(tray.getMin98_2012() != null ? tray.getMin98_2012() : "null")
                            + ", COLEC1_MIN_98_2013 = "+(tray.getMin98_2013() != null ? tray.getMin98_2013() : "null")
                            + ", COLEC1_MIN_98_2014 = "+(tray.getMin98_2014() != null ? tray.getMin98_2014() : "null")
                            + ", COLEC1_MIN_98_2015 = "+(tray.getMin98_2015() != null ? tray.getMin98_2015() : "null")
                            + ", COLEC1_MIN_98_2016 = "+(tray.getMin98_2016() != null ? tray.getMin98_2016() : "null")
                            + ", COLEC1_MIN_98_2017 = "+(tray.getMin98_2017() != null ? tray.getMin98_2017() : "null")
                            + ", COLEC1_MIN_98_2018 = "+(tray.getMin98_2018() != null ? tray.getMin98_2018() : "null")
                            + ", COLEC1_TAS_03_2007 = "+(tray.getTas03_2007() != null ? tray.getTas03_2007() : "null")
                            + ", COLEC1_TAS_03_2008 = "+(tray.getTas03_2008() != null ? tray.getTas03_2008() : "null")
                            + ", COLEC1_TAS_03_2009 = "+(tray.getTas03_2009() != null ? tray.getTas03_2009() : "null")
                            + ", COLEC1_TAS_03_2010 = "+(tray.getTas03_2010() != null ? tray.getTas03_2010() : "null")
                            + ", COLEC1_TAS_03_2011 = "+(tray.getTas03_2011() != null ? tray.getTas03_2011() : "null")
                            + ", COLEC1_TAS_03_2012 = "+(tray.getTas03_2012() != null ? tray.getTas03_2012() : "null")
                            + ", COLEC1_TAS_03_2013 = "+(tray.getTas03_2013() != null ? tray.getTas03_2013() : "null")
                            + ", COLEC1_TAS_03_2014 = "+(tray.getTas03_2014() != null ? tray.getTas03_2014() : "null")
                            + ", COLEC1_TAS_03_2015 = "+(tray.getTas03_2015() != null ? tray.getTas03_2015() : "null")
                            + ", COLEC1_TAS_03_2016 = "+(tray.getTas03_2016() != null ? tray.getTas03_2016() : "null")
                            + ", COLEC1_TAS_03_2017 = "+(tray.getTas03_2017() != null ? tray.getTas03_2017() : "null")
                            + ", COLEC1_TAS_03_2018 = "+(tray.getTas03_2018() != null ? tray.getTas03_2018() : "null")
                            + ", COLEC1_ACT_56_03 = "+(tray.getAct56_03() != null ? tray.getAct56_03() : "null")
                            + ", COLEC2_LAN_2011 = "+(tray.getLan_2011() != null ? tray.getLan_2011() : "null")
                            + ", COLEC2_LAN_2013 = "+(tray.getLan_2013() != null ? tray.getLan_2013() : "null")
                            + ", COLEC2_LAN_2014 = "+(tray.getLan_2014() != null ? tray.getLan_2014() : "null")
                            + ", COLEC2_LAN_2015 = "+(tray.getLan_2015() != null ? tray.getLan_2015() : "null")
                            + ", COLEC2_LAN_2017 = "+(tray.getLan_2017() != null ? tray.getLan_2017() : "null")
                            + ", COLEC2_LAN_OTROS = "+(tray.getLan_otros() != null ? tray.getLan_otros() : "null")
                            + ", COLEC1_ULT5_VAL = "+(tray.getColec1_ult5_val() != null ? tray.getColec1_ult5_val() : "null")
                            + ", COLEC2_ULT5_VAL = "+(tray.getColec2_ult5_val() != null ? tray.getColec2_ult5_val() : "null")
                            + ", COLEC_TOT_VAL = "+(tray.getColec_tot_total() != null ? tray.getColec_tot_total() : "null")
                            + " where COLEC_COD_TRAY = '"+tray.getCodTray()+"'"
                            + " and COLEC_COD_ENTIDAD = "+tray.getCodEntidad()
                            + " and COLEC_NUMEXP = '"+tray.getNumExp()+"'";
                }
                if(log.isDebugEnabled()) 
                    log.error("sql = " + query);
                st = con.createStatement();
                int res = st.executeUpdate(query);
                if(res > 0)
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error guardando los datos de trayectoria para entidad "+(tray != null ? tray.getCodEntidad(): "(tray = null)")+" y expediente "+(tray != null ? tray.getNumExp() : "(centro = null)"), ex);
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
            catch(SQLException e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    
    
    public List<ColecTrayVO> getListaTrayectoriasPorExpediente(ColecTrayVO trayEjemplo, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        List<ColecTrayVO> listaTrayectorias = new ArrayList<ColecTrayVO>();
        if(trayEjemplo != null && trayEjemplo.getNumExp() != null && !trayEjemplo.getNumExp().equals(""))
        {
            try
            {
                String query = null;
                Integer ejercicio=MeLanbide48Utils.getEjercicioDeExpediente(trayEjemplo.getNumExp());
                query="select t.* from " 
                   + ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_TRAY, ConstantesMeLanbide48.FICHERO_PROPIEDADES)
                   +" t"
                   +" where t.COLEC_NUMEXP='"+trayEjemplo.getNumExp()+"' and t.COLEC_EXP_EJE="+ejercicio
                ;

                if(log.isDebugEnabled()) 
                    log.error("sql = " + query);
                st = con.createStatement();
                rs = st.executeQuery(query);
                while(rs.next())
                {
                    try
                    {
                        listaTrayectorias.add(((ColecTrayVO)MeLanbide48MappingUtils.getInstance().map(rs, ColecTrayVO.class)));
                    }
                    catch(Exception ex)
                    {
                        
                    }
                }
            }
            catch(Exception ex)
            {
                log.error("Se ha producido una excepcion en la BBDD recuperando trayectorias para el expediente "+(trayEjemplo != null ? trayEjemplo.getNumExp() : " (trayEjemplo = null)"), ex);
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
        return listaTrayectorias;
    }
    
        public ColecTrayEspVO getTrayectoriaEspecifica(ColecTrayEspVO tray, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        if(tray != null && tray.getCodEntidad() != null)
        {
            if(tray != null && tray.getNumExp() != null && !tray.getNumExp().equals(""))
            {
                try
                {
                    String query = null;
                    query = "select *"
                           +" from "+ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_TRAY_ESP, ConstantesMeLanbide48.FICHERO_PROPIEDADES)+" t"
                           +" where t.COLEC_COD_TRAY_ESP = "+tray.getCodTrayEsp()
                           +" and t.COLEC_NUMEXP = '"+tray.getNumExp()+"' AND t.COLEC_EXP_EJE = "+tray.getExpEje()
                           +" and t.COLEC_COLECTIVO = "+tray.getColectivo()
                           +" and t.COLEC_COD_ENTIDAD = "+tray.getCodEntidad();

                    if(log.isDebugEnabled()) 
                        log.error("sql = " + query);
                    st = con.createStatement();
                    rs = st.executeQuery(query);
                    if(rs.next())
                    {
                        tray = ((ColecTrayEspVO)MeLanbide48MappingUtils.getInstance().map(rs, ColecTrayEspVO.class));
                    }
                }
                catch(Exception ex)
                {
                    log.error("Se ha producido una excepcion en la BBDD recuperando trayectoria especifica para entidad "+(tray != null ? tray.getCodEntidad() : "(tray = null)")+", expediente "+(tray != null ? tray.getNumExp() : " (tray = null)")+" y colectivo "+(tray != null ? tray.getColectivo() : " (tray = null)"), ex);
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
        else
        {
            return null;
        }
        return tray;
    }
    
    public ColecTrayEspVO guardarTrayectoriaEspecifica(ColecTrayEspVO tray, Connection con) throws Exception
    {
        Statement st = null;
        try
        {
            if(tray.getCodEntidad() == null || (tray.getNumExp() == null || tray.getNumExp().equals("")) || tray.getExpEje() == null
               || tray.getColectivo() == null)
            {
                log.error("Se ha producido un error guardando los datos de trayectoria para entidad "+(tray != null ? tray.getCodEntidad(): "(tray = null)")+" y expediente "+(tray != null ? tray.getNumExp() : "(centro = null)"));
                throw new Exception();
            }
            else
            {
                String query = null;
                if(tray.getCodTrayEsp() == null)
                {
                    Long codTray = this.getNextId(ConfigurationParameter.getParameter(ConstantesMeLanbide48.COLEC_TRAY_ESP_SQ, ConstantesMeLanbide48.FICHERO_PROPIEDADES), con);
                    if(codTray == null)
                    {
                        throw new Exception();
                    }
                    tray.setCodTrayEsp(codTray);

                    //Es un registro nuevo
                    query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_TRAY_ESP, ConstantesMeLanbide48.FICHERO_PROPIEDADES)
                            + "(COLEC_COD_TRAY_ESP, COLEC_COD_ENTIDAD, COLEC_NUMEXP, COLEC_EXP_EJE, COLEC_COLECTIVO"
                            + ", COLEC_NOMBRE_ADM, COLEC_DESC_ACTIVIDAD, COLEC_VALIDADA"
                            + ")"
                            + " values("+tray.getCodTrayEsp()+ ", "+tray.getCodEntidad()+", '"+tray.getNumExp()+"', "+tray.getExpEje()+", "+tray.getColectivo()
                            + ", "+(tray.getNombreAdm() != null && !tray.getNombreAdm().equals("") ? "'"+tray.getNombreAdm()+"'" : "null") 
                            + ", "+(tray.getDescActividad() != null && !tray.getDescActividad().equals("") ? "'"+tray.getDescActividad()+"'" : "null") 
                            + ", "+(tray.getValidada() != null ? tray.getValidada() : "null") 
                            + ")";
                }
                else
                {
                    //Es un registro que ya existe
                    query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_TRAY_ESP, ConstantesMeLanbide48.FICHERO_PROPIEDADES)
                            + " set"
                            + " COLEC_NOMBRE_ADM = "+(tray.getNombreAdm() != null && !tray.getNombreAdm().equals("") ? "'"+tray.getNombreAdm()+"'" : "null")
                            + ", COLEC_DESC_ACTIVIDAD = "+(tray.getDescActividad() != null && !tray.getDescActividad().equals("") ? "'"+tray.getDescActividad()+"'" : "null")
                            + ", COLEC_VALIDADA = "+(tray.getValidada() != null ? tray.getValidada() : "null")
                            + ", COLEC_COD_ENTIDAD = "+(tray.getCodEntidad()!= null ? tray.getCodEntidad(): "null")
                            + " where COLEC_COD_TRAY_ESP = '"+tray.getCodTrayEsp()+"'"
                            //+ " and COLEC_COD_ENTIDAD = "+tray.getCodEntidad() // No forma parte de la where. Es un valor que se puede cambiar y la clave unica de la tabla es el ID de la trayectoria
                            + " and COLEC_NUMEXP = '"+tray.getNumExp()+"'"
                            + " and COLEC_EXP_EJE = "+tray.getExpEje()
                            + " and COLEC_COLECTIVO = "+tray.getColectivo();
                }
                if(log.isDebugEnabled()) 
                    log.error("sql = " + query);
                st = con.createStatement();
                int res = st.executeUpdate(query);
                if(res > 0)
                {
                    return tray;
                }
                else
                {
                    log.error("Se ha producido un error guardando los datos de trayectoria para entidad "+(tray != null ? tray.getCodEntidad(): "(tray = null)")+" y expediente "+(tray != null ? tray.getNumExp() : "(centro = null)"));
                    throw new Exception();
                }
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error guardando los datos de trayectoria para entidad "+(tray != null ? tray.getCodEntidad(): "(tray = null)")+" y expediente "+(tray != null ? tray.getNumExp() : "(centro = null)"), ex);
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
            catch(SQLException e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    
    public List<FilaTrayEspVO> getListaTrayectoriasEspecificasPorExpedienteYColectivo(ColecTrayEspVO trayEjemplo, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        List<FilaTrayEspVO> listaTrayectorias = new ArrayList<FilaTrayEspVO>();
        if(trayEjemplo != null && trayEjemplo.getNumExp() != null && !trayEjemplo.getNumExp().equals(""))
        {
            if(trayEjemplo.getColectivo() != null)
            {
                try
                {
                    String query = null;
                    String nombreTablaEntidad = definirTablaConsultaEntidadSegunEntAgrupSioNo(trayEjemplo.getNumExp(),con);
                    Integer ejercicio=MeLanbide48Utils.getEjercicioDeExpediente(trayEjemplo.getNumExp());
                    query = "select t.COLEC_COD_TRAY_ESP " +
                            ",t.COLEC_COD_ENTIDAD " +
                            ",t.COLEC_NUMEXP " +
                            ",t.COLEC_EXP_EJE " +
                            ",t.COLEC_COLECTIVO " +
                            ",t.COLEC_NOMBRE_ADM " +
                            ",t.COLEC_DESC_ACTIVIDAD " +
                            ",t.COLEC_VALIDADA " +
                            ",DATOS_ENTIDAD.COLEC_ENT_CIF " +
                            ",DATOS_ENTIDAD.COLEC_ENT_NOMBRE "
                            +"from COLEC_TRAY_ESP t "
                            +"LEFT JOIN "+nombreTablaEntidad+ " DATOS_ENTIDAD ON t.COLEC_NUMEXP=DATOS_ENTIDAD.COLEC_NUMEXP AND T.COLEC_COD_ENTIDAD=DATOS_ENTIDAD.COLEC_ENT_COD " 
                            +" where t.COLEC_NUMEXP = '"+trayEjemplo.getNumExp()+"'"
                            +" AND t.COLEC_EXP_EJE = "+ejercicio
                            +" AND t.COLEC_COLECTIVO = "+trayEjemplo.getColectivo();

                    if(log.isDebugEnabled()) 
                        log.error("sql = " + query);
                    st = con.createStatement();
                    rs = st.executeQuery(query);
                    while(rs.next())
                    {
                        try
                        {
                            listaTrayectorias.add(((FilaTrayEspVO)MeLanbide48MappingUtils.getInstance().map(rs, FilaTrayEspVO.class)));
                        }
                        catch(Exception ex)
                        {
                            ex.printStackTrace();
                        }
                    }
                }
                catch(Exception ex)
                {
                    log.error("Se ha producido una excepcion en la BBDD recuperando trayectorias para el expediente "+(trayEjemplo != null ? trayEjemplo.getNumExp() : " (trayEjemplo = null)"), ex);
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
        }
        return listaTrayectorias;
    }
    
    public int eliminarTrayectoriaEspecifica(ColecTrayEspVO tray, Connection con) throws Exception
    {
        Statement st = null;
        int result = 0;
        try
        {

            String query = null;
                query = "delete from "+ ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_TRAY_ESP, ConstantesMeLanbide48.FICHERO_PROPIEDADES)
                      + " where"
                      + " COLEC_COD_TRAY_ESP = "+tray.getCodTrayEsp()
                      + " and COLEC_NUMEXP = '"+tray.getNumExp()+"'"
                      + " and COLEC_EXP_EJE = "+tray.getExpEje()
                      + " and COLEC_COLECTIVO = "+tray.getColectivo()
                      + " and COLEC_COD_ENTIDAD = "+tray.getCodEntidad();

            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            result = st.executeUpdate(query);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD eliminando centros ", ex);
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
    
    /**
     * Mï¿½todo que recupera la lista de entidades de la tabla en caso de que exista asociaciï¿½n
     * @param numExpediente
     * @param con
     * @return
     * @throws Exception 
     */
    public List<SelectItem> getListaSelectItemEntidadesPorExpediente(String numExpediente, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        List<SelectItem> entidades = new ArrayList<SelectItem>();
        ColecEntidadVO ent = new ColecEntidadVO();
        try
        {
            // Leemos la entidad principal, para saber que listar
            ent.setNumExp(numExpediente);
            ent = getEntidadInteresadaPorExpediente(ent, con);
            if(ent!=null){
                String nombretabla = ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_ENTIDAD, ConstantesMeLanbide48.FICHERO_PROPIEDADES);
                if(ent.getEsAgrupacion()==1){
                    nombretabla=ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_ENTIDAD_AGRUP_LIST, ConstantesMeLanbide48.FICHERO_PROPIEDADES);
                }
                        
                String query = "SELECT COLEC_ENT_COD AS CODIGO, " 
                        +" COLEC_ENT_CIF || ' - ' || nvl(COLEC_ENT_NOMBRE, '') AS DESCRIPCION "
                        +" from " + nombretabla
                        +" where COLEC_NUMEXP = '" + numExpediente + "'"
                        ;

                if (log.isDebugEnabled()) {
                    log.error("sql = " + query);
                }
                st = con.createStatement();
                rs = st.executeQuery(query);
                while (rs.next()) {
                    entidades.add((SelectItem) MeLanbide48MappingUtils.getInstance().map(rs, SelectItem.class));
                }
            }
            return entidades;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando entidades para el expediente "+numExpediente, ex);
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
            catch(SQLException e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }

    public int eliminarDatosTrayectoriaExpecialxExpediente(String numExp, Connection con) throws Exception {
        log.debug("eliminarDatosTrayectoriaExpecialxExpediente() - Begin");
        Statement st = null;
        try {
            String query;
            query = "Delete from " + ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_TRAY_ESP, ConstantesMeLanbide48.FICHERO_PROPIEDADES)
                    + " where COLEC_NUMEXP = '" + numExp + "'";
            if (log.isDebugEnabled()) {
                log.error("sql = " + query);
            }
            st = con.createStatement();
            return st.executeUpdate(query);
        } catch (SQLException ex) {
            log.error("Se ha producido un error eliminando datos Tray. EspecialxNumExpediente : " + numExp, ex);
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }

    public int eliminarDatosTrayectoriaGeneralxExpediente(String numExp, Connection con) throws Exception {
        log.debug("eliminarDatosTrayectoriaGeneralxExpediente() - Begin");
        Statement st = null;
        try {
            String query;
            query = "Delete from " + ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_TRAY, ConstantesMeLanbide48.FICHERO_PROPIEDADES)
                    + " where COLEC_NUMEXP = '" + numExp + "'";
            if (log.isDebugEnabled()) {
                log.error("sql = " + query);
            }
            st = con.createStatement();
            return st.executeUpdate(query);
        } catch (SQLException ex) {
            log.error("Se ha producido un error eliminando datos Tray. GeneralxNumExpediente : " + numExp, ex);
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }

    public int eliminarDatosListaEntidadesAgrupacionxExpediente(String numExp, Connection con) throws Exception {
        log.debug("eliminarDatosListaEntidadesAgrupacionxExpediente() - Begin");
        Statement st = null;
        try {
            String query;
            query = "Delete from " + ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_ENTIDAD_AGRUP_LIST, ConstantesMeLanbide48.FICHERO_PROPIEDADES)
                    + " where COLEC_NUMEXP = '" + numExp + "'";
            if (log.isDebugEnabled()) {
                log.error("sql = " + query);
            }
            st = con.createStatement();
            return st.executeUpdate(query);
        } catch (SQLException ex) {
            log.error("Se ha producido un error eliminando datos Lista Entidades de agrupacion x Expediente : " + numExp, ex);
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }

    public ColecEntidadEnAgrupacionVO getEntidadPorCodEntiPadre_CodEntidad_NumExp(ColecEntidadEnAgrupacionVO p, Connection con) throws Exception {
        log.debug("getEntidadPorCodEntiPadre_CodEntidad_NumExp - BEGIN()");
        if (p != null && p.getNumExp() != null) {
            Statement st = null;
            ResultSet rs = null;
            ColecEntidadEnAgrupacionVO entidad = null;
            try {
                String query = "SELECT * "
                        + " FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_ENTIDAD_AGRUP_LIST, ConstantesMeLanbide48.FICHERO_PROPIEDADES)
                        + " WHERE "
                        + " COLEC_ENT_AGRUP_COD=" + p.getCodEntidadPadreAgrup()
                        + " AND COLEC_ENT_COD=" + p.getCodEntidad()
                        + " AND COLEC_NUMEXP= '" + p.getNumExp() + "'";
                if (log.isDebugEnabled()) {
                    log.error("sql = " + query);
                }
                st = con.createStatement();
                rs = st.executeQuery(query);
                if (rs.next()) {
                    entidad = (ColecEntidadEnAgrupacionVO) MeLanbide48MappingUtils.getInstance().map(rs, ColecEntidadEnAgrupacionVO.class);
                }
                return entidad;
            } catch (Exception ex) {
                log.error("Se ha producido un error recuperando entidad " + p.getCodEntidad() + " para expediente " + p.getNumExp(), ex);
                throw new Exception(ex);
            } finally {
                try {
                    if (log.isDebugEnabled()) {
                        log.debug("Procedemos a cerrar el statement y el resultset");
                    }
                    if (st != null) {
                        st.close();
                    }
                    if (rs != null) {
                        rs.close();
                    }
                } catch (SQLException e) {
                    log.error("Se ha producido un error cerrando el statement y el resulset", e);
                    throw new Exception(e);
                }
            }
        } else {
            return null;
        }
    }

    public ColecEntidadEnAgrupacionVO guardarColecEntidadEnAgrupacionVO(ColecEntidadEnAgrupacionVO entidad, Connection con) throws Exception {
        
        Statement st = null;
        try {
            String query = null;
            if(entidad.getCodEntidadPadreAgrup()!=null && entidad.getCodEntidadPadreAgrup()!=null){
                if (entidad.getCodEntidad()== null) {
                    Long codEntidad = this.getNextId(ConfigurationParameter.getParameter(ConstantesMeLanbide48.COLEC_ENTIDAD_AGRUP_LIST_SQ, ConstantesMeLanbide48.FICHERO_PROPIEDADES), con);
                    if (codEntidad == null) {
                        throw new Exception();
                    }
                    entidad.setCodEntidad(codEntidad);

                    //Es una entidad nueva
                    query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_ENTIDAD_AGRUP_LIST, ConstantesMeLanbide48.FICHERO_PROPIEDADES)
                            + "(COLEC_ENT_COD, COLEC_ENT_AGRUP_COD, COLEC_NUMEXP, COLEC_ENT_TIPO_CIF"
                            + ", COLEC_ENT_CIF, COLEC_ENT_NOMBRE "
                            + ", COLEC_ENT_CENTESPEMPTH, COLEC_ENT_PARTMAYCEETH, COLEC_ENT_EMPINSERCIONTH, COLEC_ENT_PROMOEMPINSTH"
                            + ", COLEC_ENT_PORCEN_COMPROM_REALI "
                            + " ,PLANIGUALDAD,CERTIFICADOCALIDAD,ACEPTNUMEROSUPEHORAS,SEGUNDOLOCALMISMOAMB "
                            + ",COMP_IGUALDAD_OPCION,ENT_SUJETA_DER_PUBL,ENT_SIN_ANIMO_LUCRO  "
                            + ")"
                            + " values(" + entidad.getCodEntidad()
                            + ", " + (entidad.getCodEntidadPadreAgrup()!= null ? entidad.getCodEntidadPadreAgrup() : "null")
                            + ", " + (entidad.getNumExp() != null ? "'" + entidad.getNumExp() + "'" : "null")
                            + ", " + (entidad.getTipoCif() != null ? "'" + entidad.getTipoCif() + "'" : "null")
                            + ", " + (entidad.getCif() != null ? "'" + entidad.getCif() + "'" : "null")
                            + ", " + (entidad.getNombre() != null ? "'" + entidad.getNombre() + "'" : "null")
                            + ", " + (entidad.getCentroEspEmpTH() != null ? +entidad.getCentroEspEmpTH() : "null")
                            + ", " + (entidad.getParticipanteMayorCentEcpEmpTH() != null ? entidad.getParticipanteMayorCentEcpEmpTH() : "null")
                            + ", " + (entidad.getEmpresaInsercionTH() != null ? entidad.getEmpresaInsercionTH() : "null")
                            + ", " + (entidad.getPromotorEmpInsercionTH() != null ? entidad.getPromotorEmpInsercionTH() : "null")
                            + ", " + (entidad.getPorcentaCompromisoRealizacion()!= null ? entidad.getPorcentaCompromisoRealizacion() : "null")
                            + ", " + (entidad.getPlanIgualdad()!= null ? entidad.getPlanIgualdad(): "null")
                            + ", " + (entidad.getCertificadoCalidad()!= null ? entidad.getCertificadoCalidad(): "null")
                            + ", " + (entidad.getAceptaNumeroSuperiorHoras()!= null ? entidad.getAceptaNumeroSuperiorHoras(): "null")
                            + ", " + (entidad.getSegundosLocalesMismoAmbito()!= null ? entidad.getSegundosLocalesMismoAmbito(): "null")
                            + ", " + (entidad.getCompIgualdadOpcion()!= null ? "'"+entidad.getCompIgualdadOpcion()+"'": "null")
                            + ", " + (entidad.getEntSujetaDerPubl()!= null ? "'"+entidad.getEntSujetaDerPubl()+"'": "null")
                            + ", " + (entidad.getEntSinAnimoLucro()!= null ? "'"+entidad.getEntSinAnimoLucro()+"'": "null")
                            + ")";
                } else {
                    //Es una entidad que ya existe
                    query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_ENTIDAD_AGRUP_LIST, ConstantesMeLanbide48.FICHERO_PROPIEDADES)
                            + " set"
                            + " COLEC_ENT_TIPO_CIF = " + (entidad.getTipoCif() != null ? "'" + entidad.getTipoCif() + "'" : "null")
                            + ", COLEC_ENT_CIF = " + (entidad.getCif() != null ? "'" + entidad.getCif() + "'" : "null")
                            + ", COLEC_ENT_NOMBRE = " + (entidad.getNombre() != null ? "'" + entidad.getNombre() + "'" : "null")
                            + ", COLEC_ENT_CENTESPEMPTH = " + (entidad.getCentroEspEmpTH() != null ? entidad.getCentroEspEmpTH() : "null")
                            + ", COLEC_ENT_PARTMAYCEETH = " + (entidad.getParticipanteMayorCentEcpEmpTH() != null ? entidad.getParticipanteMayorCentEcpEmpTH() : "null")
                            + ", COLEC_ENT_EMPINSERCIONTH = " + (entidad.getEmpresaInsercionTH() != null ? entidad.getEmpresaInsercionTH() : "null")
                            + ", COLEC_ENT_PROMOEMPINSTH = " + (entidad.getPromotorEmpInsercionTH() != null ? entidad.getPromotorEmpInsercionTH() : "null")
                            + ", COLEC_ENT_PORCEN_COMPROM_REALI = " + (entidad.getPorcentaCompromisoRealizacion()!= null ? entidad.getPorcentaCompromisoRealizacion(): "null")
                            + ", PLANIGUALDAD = " + (entidad.getPlanIgualdad()!= null ? entidad.getPlanIgualdad(): "null")
                            + ", CERTIFICADOCALIDAD = " + (entidad.getCertificadoCalidad()!= null ? entidad.getCertificadoCalidad(): "null")
                            + ", ACEPTNUMEROSUPEHORAS = " + (entidad.getAceptaNumeroSuperiorHoras()!= null ? entidad.getAceptaNumeroSuperiorHoras(): "null")
                            + ", SEGUNDOLOCALMISMOAMB = " + (entidad.getSegundosLocalesMismoAmbito()!= null ? entidad.getSegundosLocalesMismoAmbito(): "null")
                            + ", COMP_IGUALDAD_OPCION = " + (entidad.getCompIgualdadOpcion()!= null ? "'"+entidad.getCompIgualdadOpcion()+"'": "null")
                            + ", ENT_SUJETA_DER_PUBL = " + (entidad.getEntSujetaDerPubl()!= null ? "'"+entidad.getEntSujetaDerPubl()+"'": "null")
                            + ", ENT_SIN_ANIMO_LUCRO = " + (entidad.getEntSinAnimoLucro()!= null ? "'"+entidad.getEntSinAnimoLucro()+"'": "null")
                            + " where COLEC_ENT_COD =" + entidad.getCodEntidad()
                            + " and COLEC_ENT_AGRUP_COD =" + entidad.getCodEntidadPadreAgrup()
                            + " and COLEC_NUMEXP = '" + entidad.getNumExp() + "'";
                }
                if (log.isDebugEnabled()) {
                    log.error("sql = " + query);
                }
                st = con.createStatement();
                int res = st.executeUpdate(query);
                if (res > 0) {
                    return entidad;
                } else {
                    return null;
                }                
            }else{
                log.debug("Lanzamos Excepcion porque no ha llegado el codigo de la entidad Padre." + (entidad!=null ? entidad.getNumExp() :""));
                throw new Exception("Codigo de entidad padre no recibido al dar de alta un miembro de la asociacio/agrupacion");
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error guardando los datos de la entidad : " + (entidad != null ? entidad.getCodEntidad() + "/" + entidad.getCodEntidadPadreAgrup(): "(entidad = null)") + " para el expediente " + (entidad != null ? entidad.getNumExp() : "(entidad = null)"), ex);
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }

    public int eliminarColecEntidadEnAgrupacionVO(ColecEntidadEnAgrupacionVO entidad, Connection con) throws Exception {
        Statement st = null;
        try
        {
            String query;
                query = "delete from "+ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_ENTIDAD_AGRUP_LIST, ConstantesMeLanbide48.FICHERO_PROPIEDADES)
                       +" where COLEC_ENT_COD = "+entidad.getCodEntidad()
                       +" and COLEC_ENT_AGRUP_COD = "+entidad.getCodEntidadPadreAgrup()
                       +" and COLEC_NUMEXP = '"+entidad.getNumExp()+"'";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            return st.executeUpdate(query);
        }
        catch(SQLException ex)
        {
            log.error("Se ha producido un error eliminando entidad asociada / padre  "+(entidad != null ? entidad.getCodEntidad()+"/"+entidad.getCodEntidadPadreAgrup(): "entidad = null"), ex);
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
            catch(SQLException e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }

    /**
     * 
     * @param numExp
     * @param con
     * @return Nombre la tablaa consultar. POr defecto la tabla COLEC_ENTIDAD que es la tabla principal. Si hay error o no se ha registado ninguna entidad.
     */
    private String definirTablaConsultaEntidadSegunEntAgrupSioNo(String numExp, Connection con){
        log.debug("definirTablaConsultaSegunEntAgrupSioNo -  Begin() " + numExp);
        // Por defecto consultamos la tabla principal : Sino Existe o si da error En ese Metodo
        String nombretabla = ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_ENTIDAD, ConstantesMeLanbide48.FICHERO_PROPIEDADES);
        try {
            ColecEntidadVO ent = new ColecEntidadVO();
            ent.setNumExp(numExp);
            ent = getEntidadInteresadaPorExpediente(ent, con);
            if (ent != null) {
                if (ent.getEsAgrupacion() == 1) {
                    nombretabla = ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_ENTIDAD_AGRUP_LIST, ConstantesMeLanbide48.FICHERO_PROPIEDADES);
                }
            }
        } catch (Exception e) {
                log.error("Se ha producido un error definirTablaConsultaSegunEntAgrupSioNo ", e);
                //throw new Exception(e);
        }
        log.debug("definirTablaConsultaSegunEntAgrupSioNo -  End() - " + nombretabla);
        return nombretabla;
    }

    public List<ColecTrayOtroProgramaVO> getListaTrayGeneralOtrosProgramasxCodEntNumExp(ColecTrayOtroProgramaVO trayEjemplo, Connection con) throws Exception {
        log.debug("getListaTrayectoriaGeneralOtrosProgramasxNumExp - Begin()");
        Statement st = null;
        ResultSet rs = null;
        List<ColecTrayOtroProgramaVO> listaTrayectorias = new ArrayList<ColecTrayOtroProgramaVO>();
        if (trayEjemplo != null && trayEjemplo.getNumExpediente()!= null && !trayEjemplo.getNumExpediente().equals("")) {
            try {
                String query = null;
                String nombreTablaEntidad = definirTablaConsultaEntidadSegunEntAgrupSioNo(trayEjemplo.getNumExpediente(),con);
                query = "select t.* "
                        + ",DATOS_ENTIDAD.COLEC_ENT_CIF " 
                        + ",DATOS_ENTIDAD.COLEC_ENT_NOMBRE "
                        + " from "
                        + ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_OTROS_PROGRAMAS, ConstantesMeLanbide48.FICHERO_PROPIEDADES)
                        + " t"
                        + " LEFT JOIN "+nombreTablaEntidad+" DATOS_ENTIDAD ON T.COLEC_OTRPRO_NUMEXP=DATOS_ENTIDAD.COLEC_NUMEXP AND T.COLEC_OTRPRO_COD_ENTIDAD=DATOS_ENTIDAD.COLEC_ENT_COD "
                        + " where t.COLEC_OTRPRO_NUMEXP='" + trayEjemplo.getNumExpediente()+ "'"
                        + " AND t.COLEC_OTRPRO_COD_ENTIDAD=" + trayEjemplo.getCodEntidad();

                if (log.isDebugEnabled()) {
                    log.error("sql = " + query);
                }
                st = con.createStatement();
                rs = st.executeQuery(query);
                while (rs.next()) {
                    try {
                        listaTrayectorias.add(((ColecTrayOtroProgramaVO) MeLanbide48MappingUtils.getInstance().map(rs, ColecTrayOtroProgramaVO.class)));
                    } catch (Exception ex) {
                        log.debug("Error al mapear la ColecTrayOtroProgramaVO ", ex);
                    }
                }
            } catch (Exception ex) {
                log.error("Se ha producido una excepcion en la BBDD recuperando trayectorias para el expediente " + (trayEjemplo != null ? trayEjemplo.getNumExpediente(): " (trayEjemplo = null)"), ex);
                throw new Exception(ex);
            } finally {
                try {
                    if (log.isDebugEnabled()) {
                        log.debug("Procedemos a cerrar el statement y el resultset");
                    }
                    if (st != null) {
                        st.close();
                    }
                    if (rs != null) {
                        rs.close();
                    }
                } catch (Exception e) {
                    log.error("Se ha producido un error cerrando el statement y el resulset", e);
                    throw new Exception(e);
                }
            }
        }
        return listaTrayectorias;
    }
    public List<ColecTrayOtroProgramaVO> getListaTrayGeneralOtrosProgramasxNumExp(ColecTrayOtroProgramaVO trayEjemplo, Connection con) throws Exception {
        log.debug("getListaTrayGeneralOtrosProgramasxNumExp - Begin()");
        Statement st = null;
        ResultSet rs = null;
        List<ColecTrayOtroProgramaVO> listaTrayectorias = new ArrayList<ColecTrayOtroProgramaVO>();
        if (trayEjemplo != null && trayEjemplo.getNumExpediente()!= null && !trayEjemplo.getNumExpediente().equals("")) {
            try {
                String query = null;
                String nombreTablaEntidad = definirTablaConsultaEntidadSegunEntAgrupSioNo(trayEjemplo.getNumExpediente(),con);
                Integer ejercicio=MeLanbide48Utils.getEjercicioDeExpediente(trayEjemplo.getNumExpediente());
                query = "select t.* "
                        + ",DATOS_ENTIDAD.COLEC_ENT_CIF " 
                        + ",DATOS_ENTIDAD.COLEC_ENT_NOMBRE "
                        + " from "
                        + ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_OTROS_PROGRAMAS, ConstantesMeLanbide48.FICHERO_PROPIEDADES)
                        + " t"
                        + " LEFT JOIN "+nombreTablaEntidad+" DATOS_ENTIDAD ON T.COLEC_OTRPRO_NUMEXP=DATOS_ENTIDAD.COLEC_NUMEXP AND T.COLEC_OTRPRO_COD_ENTIDAD=DATOS_ENTIDAD.COLEC_ENT_COD "
                        + " where t.COLEC_OTRPRO_NUMEXP='" + trayEjemplo.getNumExpediente()+ "' AND t.COLEC_OTRPRO_EXP_EJE="+ejercicio
                        ;

                if (log.isDebugEnabled()) {
                    log.error("sql = " + query);
                }
                st = con.createStatement();
                rs = st.executeQuery(query);
                while (rs.next()) {
                    try {
                        listaTrayectorias.add(((ColecTrayOtroProgramaVO) MeLanbide48MappingUtils.getInstance().map(rs, ColecTrayOtroProgramaVO.class)));
                    } catch (Exception ex) {
                        log.debug("Error al mapear la ColecTrayOtroProgramaVO ", ex);
                    }
                }
            } catch (Exception ex) {
                log.error("Se ha producido una excepcion en la BBDD recuperando trayectorias para el expediente " + (trayEjemplo != null ? trayEjemplo.getNumExpediente(): " (trayEjemplo = null)"), ex);
                throw new Exception(ex);
            } finally {
                try {
                    if (log.isDebugEnabled()) {
                        log.debug("Procedemos a cerrar el statement y el resultset");
                    }
                    if (st != null) {
                        st.close();
                    }
                    if (rs != null) {
                        rs.close();
                    }
                } catch (Exception e) {
                    log.error("Se ha producido un error cerrando el statement y el resulset", e);
                    throw new Exception(e);
                }
            }
        }
        return listaTrayectorias;
    }
    
    public List<ColecTrayActividadVO> getListaTrayGeneralActividadesxCodEntNumExp(ColecTrayActividadVO trayEjemplo, Connection con) throws Exception {
        log.debug("getListaTrayGeneralActividadesxCodEntNumExp - Begin()");
        Statement st = null;
        ResultSet rs = null;
        List<ColecTrayActividadVO> listaTrayectorias = new ArrayList<ColecTrayActividadVO>();
        if (trayEjemplo != null && trayEjemplo.getNumExpediente()!= null && !trayEjemplo.getNumExpediente().equals("")) {
            try {
                String query = null;
                String nombreTablaEntidad = definirTablaConsultaEntidadSegunEntAgrupSioNo(trayEjemplo.getNumExpediente(),con);
                query = "select t.* "
                        + ",DATOS_ENTIDAD.COLEC_ENT_CIF " 
                        + ",DATOS_ENTIDAD.COLEC_ENT_NOMBRE "
                        + " from "
                        + ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_ACTIVIDADES, ConstantesMeLanbide48.FICHERO_PROPIEDADES)
                        + " t"
                        + " LEFT JOIN "+nombreTablaEntidad+" DATOS_ENTIDAD ON T.COLEC_ACTIV_NUMEXP=DATOS_ENTIDAD.COLEC_NUMEXP AND T.COLEC_ACTIV_COD_ENTIDAD=DATOS_ENTIDAD.COLEC_ENT_COD "
                        + " where t.COLEC_ACTIV_NUMEXP='" + trayEjemplo.getNumExpediente()+ "'"
                        + " AND t.COLEC_ACTIV_COD_ENTIDAD=" + trayEjemplo.getCodEntidad();

                if (log.isDebugEnabled()) {
                    log.error("sql = " + query);
                }
                st = con.createStatement();
                rs = st.executeQuery(query);
                while (rs.next()) {
                    try {
                        listaTrayectorias.add(((ColecTrayActividadVO) MeLanbide48MappingUtils.getInstance().map(rs, ColecTrayActividadVO.class)));
                    } catch (Exception ex) {
                        log.debug("Error al mapear la ColecTrayActividadVO ", ex);
                    }
                }
            } catch (Exception ex) {
                log.error("Se ha producido una excepcion en la BBDD recuperando trayectorias para el expediente " + (trayEjemplo != null ? trayEjemplo.getNumExpediente(): " (trayEjemplo = null)"), ex);
                throw new Exception(ex);
            } finally {
                try {
                    if (log.isDebugEnabled()) {
                        log.debug("Procedemos a cerrar el statement y el resultset");
                    }
                    if (st != null) {
                        st.close();
                    }
                    if (rs != null) {
                        rs.close();
                    }
                } catch (Exception e) {
                    log.error("Se ha producido un error cerrando el statement y el resulset", e);
                    throw new Exception(e);
                }
            }
        }
        return listaTrayectorias;
    }
    public List<ColecTrayActividadVO> getListaTrayGeneralActividadesxNumExp(ColecTrayActividadVO trayEjemplo, Connection con) throws Exception {
        log.debug("getListaTrayGeneralActividadesxNumExp - Begin()");
        Statement st = null;
        ResultSet rs = null;
        List<ColecTrayActividadVO> listaTrayectorias = new ArrayList<ColecTrayActividadVO>();
        if (trayEjemplo != null && trayEjemplo.getNumExpediente()!= null && !trayEjemplo.getNumExpediente().equals("")) {
            try {
                String query = null;
                String nombreTablaEntidad = definirTablaConsultaEntidadSegunEntAgrupSioNo(trayEjemplo.getNumExpediente(),con);
                Integer ejercicio=MeLanbide48Utils.getEjercicioDeExpediente(trayEjemplo.getNumExpediente());
                query = "select t.* "
                        + ",DATOS_ENTIDAD.COLEC_ENT_CIF " 
                        + ",DATOS_ENTIDAD.COLEC_ENT_NOMBRE "
                        + " from "
                        + ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_ACTIVIDADES, ConstantesMeLanbide48.FICHERO_PROPIEDADES)
                        + " t"
                        + " LEFT JOIN "+nombreTablaEntidad+" DATOS_ENTIDAD ON T.COLEC_ACTIV_NUMEXP=DATOS_ENTIDAD.COLEC_NUMEXP AND T.COLEC_ACTIV_COD_ENTIDAD=DATOS_ENTIDAD.COLEC_ENT_COD "
                        + " where t.COLEC_ACTIV_NUMEXP='" + trayEjemplo.getNumExpediente()+ "' AND t.COLEC_ACTIV_EXP_EJE="+ejercicio
                        ;

                if (log.isDebugEnabled()) {
                    log.error("sql = " + query);
                }
                st = con.createStatement();
                rs = st.executeQuery(query);
                while (rs.next()) {
                    try {
                        listaTrayectorias.add(((ColecTrayActividadVO) MeLanbide48MappingUtils.getInstance().map(rs, ColecTrayActividadVO.class)));
                    } catch (Exception ex) {
                        log.debug("Error al mapear la ColecTrayActividadVO ", ex);
                    }
                }
            } catch (Exception ex) {
                log.error("Se ha producido una excepcion en la BBDD recuperando trayectorias para el expediente " + (trayEjemplo != null ? trayEjemplo.getNumExpediente(): " (trayEjemplo = null)"), ex);
                throw new Exception(ex);
            } finally {
                try {
                    if (log.isDebugEnabled()) {
                        log.debug("Procedemos a cerrar el statement y el resultset");
                    }
                    if (st != null) {
                        st.close();
                    }
                    if (rs != null) {
                        rs.close();
                    }
                } catch (Exception e) {
                    log.error("Se ha producido un error cerrando el statement y el resulset", e);
                    throw new Exception(e);
                }
            }
        }
        return listaTrayectorias;
    }

    public ColecTrayOtroProgramaVO getTrayectoriaGeneralOtrosProgramas(ColecTrayOtroProgramaVO trayEjemplo, Connection con) throws Exception {
        log.debug("getTrayectoriaGeneralOtrosProgramas - Begin()");
        Statement st = null;
        ResultSet rs = null;
        ColecTrayOtroProgramaVO trayectoria = new ColecTrayOtroProgramaVO();
        if (trayEjemplo != null && trayEjemplo.getNumExpediente() != null && !trayEjemplo.getNumExpediente().equals("")) {
            try {
                String query = null;
                query = "select t.* "
                        + ",DATOS_ENTIDAD.COLEC_ENT_CIF "
                        + ",DATOS_ENTIDAD.COLEC_ENT_NOMBRE "
                        + " from "
                        + ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_OTROS_PROGRAMAS, ConstantesMeLanbide48.FICHERO_PROPIEDADES)
                        + " t"
                        + " LEFT JOIN COLEC_ENTIDAD DATOS_ENTIDAD ON T.COLEC_OTRPRO_NUMEXP=DATOS_ENTIDAD.COLEC_NUMEXP AND T.COLEC_OTRPRO_COD_ENTIDAD=DATOS_ENTIDAD.COLEC_ENT_COD "
                        + " where t.COLEC_OTRPRO_NUMEXP='" + trayEjemplo.getNumExpediente() + "'"
                        + " AND t.COLEC_OTRPRO_COD_ENTIDAD=" + trayEjemplo.getCodEntidad()
                        + " AND t.COLEC_OTRPRO_COD=" + trayEjemplo.getCodIdOtroPrograma()
                        ;

                if (log.isDebugEnabled()) {
                    log.error("sql = " + query);
                }
                st = con.createStatement();
                rs = st.executeQuery(query);
                while (rs.next()) {
                    try {
                        trayectoria=((ColecTrayOtroProgramaVO) MeLanbide48MappingUtils.getInstance().map(rs, ColecTrayOtroProgramaVO.class));
                    } catch (Exception ex) {
                        log.debug("Error al mapear la ColecTrayOtroProgramaVO ", ex);
                    }
                }
            } catch (Exception ex) {
                log.error("Se ha producido una excepcion en la BBDD recuperando trayectorias otros programas para el expediente " + (trayEjemplo != null ? trayEjemplo.getNumExpediente() : " (trayEjemplo = null)"), ex);
                throw new Exception(ex);
            } finally {
                try {
                    if (log.isDebugEnabled()) {
                        log.debug("Procedemos a cerrar el statement y el resultset");
                    }
                    if (st != null) {
                        st.close();
                    }
                    if (rs != null) {
                        rs.close();
                    }
                } catch (Exception e) {
                    log.error("Se ha producido un error cerrando el statement y el resulset", e);
                    throw new Exception(e);
                }
            }
        }
        return trayectoria;
    }

    public ColecTrayActividadVO getTrayectoriaGeneralActividades(ColecTrayActividadVO trayEjemplo, Connection con) throws Exception {
        log.debug("getTrayectoriaGeneralActividades - Begin()");
        Statement st = null;
        ResultSet rs = null;
        ColecTrayActividadVO trayectoria = new ColecTrayActividadVO();
        if (trayEjemplo != null && trayEjemplo.getNumExpediente() != null && !trayEjemplo.getNumExpediente().equals("")) {
            try {
                String query = null;
                query = "select t.* "
                        + ",DATOS_ENTIDAD.COLEC_ENT_CIF "
                        + ",DATOS_ENTIDAD.COLEC_ENT_NOMBRE "
                        + " from "
                        + ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_ACTIVIDADES, ConstantesMeLanbide48.FICHERO_PROPIEDADES)
                        + " t"
                        + " LEFT JOIN COLEC_ENTIDAD DATOS_ENTIDAD ON T.COLEC_ACTIV_NUMEXP=DATOS_ENTIDAD.COLEC_NUMEXP AND T.COLEC_ACTIV_COD_ENTIDAD=DATOS_ENTIDAD.COLEC_ENT_COD "
                        + " where t.COLEC_ACTIV_NUMEXP='" + trayEjemplo.getNumExpediente() + "'"
                        + " AND t.COLEC_ACTIV_COD_ENTIDAD=" + trayEjemplo.getCodEntidad()
                        + " AND t.COLEC_ACTIV_COD=" + trayEjemplo.getCodIdActividad()
                        ;

                if (log.isDebugEnabled()) {
                    log.error("sql = " + query);
                }
                st = con.createStatement();
                rs = st.executeQuery(query);
                while (rs.next()) {
                    try {
                        trayectoria=((ColecTrayActividadVO) MeLanbide48MappingUtils.getInstance().map(rs, ColecTrayActividadVO.class));
                    } catch (Exception ex) {
                        log.debug("Error al mapear la ColecTrayActividadVO ", ex);
                    }
                }
            } catch (Exception ex) {
                log.error("Se ha producido una excepcion en la BBDD recuperando trayectorias actividades para el expediente " + (trayEjemplo != null ? trayEjemplo.getNumExpediente() : " (trayEjemplo = null)"), ex);
                throw new Exception(ex);
            } finally {
                try {
                    if (log.isDebugEnabled()) {
                        log.debug("Procedemos a cerrar el statement y el resultset");
                    }
                    if (st != null) {
                        st.close();
                    }
                    if (rs != null) {
                        rs.close();
                    }
                } catch (Exception e) {
                    log.error("Se ha producido un error cerrando el statement y el resulset", e);
                    throw new Exception(e);
                }
            }
        }
        return trayectoria;
    }

    public ColecTrayOtroProgramaVO guardarTrayectoriaGeneralOtrosProgramas(ColecTrayOtroProgramaVO tray, Connection con) throws Exception {
        log.debug("guardarTrayectoriaGeneralOtrosProgramas - Begin()");
        Statement st = null;
        try {
            if (tray.getCodEntidad() == null || (tray.getNumExpediente()== null || tray.getNumExpediente().equals("")) || tray.getEjercicio()== null) {
                log.error("Se ha producido un error guardando los datos de trayectoria  para entidad " + (tray != null ? tray.getCodEntidad() : "(tray = null)") + " y expediente " + (tray != null ? tray.getNumExpediente(): "(centro = null)"));
                throw new Exception();
            } else {
                String query = null;
                if (tray.getCodIdOtroPrograma()== null) {
                    Long codTray = this.getNextId(ConfigurationParameter.getParameter(ConstantesMeLanbide48.COLEC_OTROS_PROGRAMAS_SQ, ConstantesMeLanbide48.FICHERO_PROPIEDADES), con);
                    if (codTray == null) {
                        throw new Exception();
                    }
                    tray.setCodIdOtroPrograma(Integer.valueOf(codTray.toString()));

                    //Es un registro nuevo
                    query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_OTROS_PROGRAMAS, ConstantesMeLanbide48.FICHERO_PROPIEDADES)
                            + "(COLEC_OTRPRO_COD" +
                            ",COLEC_OTRPRO_TIPO" +
                            ",COLEC_OTRPRO_EXP_EJE" +
                            ",COLEC_OTRPRO_NUMEXP" +
                            ",COLEC_OTRPRO_COD_ENTIDAD" +
                            ",COLEC_OTRPRO_PROGRAMA" +
                            ",COLEC_OTRPRO_PROG_INICIO" +
                            ",COLEC_OTRPRO_PROG_FIN"
                            + ")"
                            + " values(" + tray.getCodIdOtroPrograma()
                            + ", '" + tray.getTipoOtroPrograma()+ "'"
                            + ", " + tray.getEjercicio()+ ""
                            + ", '" + tray.getNumExpediente()+ "'"
                            + ", " + tray.getCodEntidad()
                            + "," + (tray.getPrograma()!= null && !tray.getPrograma().equals("") ? "'"+tray.getPrograma()+"'": "null")
                            + ","+ (tray.getFechaInicio()!= null ? "to_date('" +MeLanbide48Utils.formatearFecha_ddmmyyyy(tray.getFechaInicio()) + "','dd/mm/yyyy')" : "null")
                            + ","+ (tray.getFechaFin()!= null ? "to_date('" +MeLanbide48Utils.formatearFecha_ddmmyyyy(tray.getFechaFin()) + "','dd/mm/yyyy')" : "null")
                            + ")";
                } else {
                    //Es un registro que ya existe
                    query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_OTROS_PROGRAMAS, ConstantesMeLanbide48.FICHERO_PROPIEDADES)
                            + " set"
                            + " COLEC_OTRPRO_TIPO='" + tray.getTipoOtroPrograma() +"'"
                            +",COLEC_OTRPRO_PROGRAMA='" +tray.getPrograma()+"'"
                            +",COLEC_OTRPRO_PROG_INICIO=" + (tray.getFechaInicio()!= null ? "to_date('" +MeLanbide48Utils.formatearFecha_ddmmyyyy(tray.getFechaInicio()) + "','dd/mm/yyyy')" : "null")
                            +",COLEC_OTRPRO_PROG_FIN="+ (tray.getFechaFin()!= null ? "to_date('" +MeLanbide48Utils.formatearFecha_ddmmyyyy(tray.getFechaFin()) + "','dd/mm/yyyy')" : "null")
                            +",COLEC_OTRPRO_COD_ENTIDAD="+ (tray.getCodEntidad()!= null ? tray.getCodEntidad() : "null")
                            + " where COLEC_OTRPRO_EXP_EJE=" + tray.getEjercicio()
                            + " and COLEC_OTRPRO_NUMEXP='" + tray.getNumExpediente()+"'"
                            //+ " and COLEC_OTRPRO_COD_ENTIDAD=" + tray.getCodEntidad()
                            + " and COLEC_OTRPRO_COD = " + tray.getCodIdOtroPrograma()
                            ;
                }
                if (log.isDebugEnabled()) {
                    log.error("sql = " + query);
                }
                st = con.createStatement();
                int res = st.executeUpdate(query);
                if (res > 0) {
                    return tray;
                } else {
                    log.error("Se ha producido un error guardando los datos de trayectoria para entidad " + (tray != null ? tray.getCodEntidad() : "(tray = null)") + " y expediente " + (tray != null ? tray.getNumExpediente(): "(centro = null)"));
                    throw new Exception();
                }
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error guardando los datos de trayectoria general otros programas para entidad " + (tray != null ? tray.getCodEntidad() : "(tray = null)") + " y expediente " + (tray != null ? tray.getNumExpediente(): "(centro = null)"), ex);
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }

    public ColecTrayActividadVO guardarTrayectoriaGeneralActividades(ColecTrayActividadVO tray, Connection con) throws Exception {
        log.debug("guardarTrayectoriaGeneralActividades - Begin()");
        Statement st = null;
        try {
            if (tray.getCodEntidad() == null || (tray.getNumExpediente() == null || tray.getNumExpediente().equals("")) || tray.getEjercicio() == null) {
                log.error("Se ha producido un error guardando los datos de trayectoria  para entidad " + (tray != null ? tray.getCodEntidad() : "(tray = null)") + " y expediente " + (tray != null ? tray.getNumExpediente() : "(centro = null)"));
                throw new Exception();
            } else {
                String query = null;
                if (tray.getCodIdActividad()== null) {
                    Long codTray = this.getNextId(ConfigurationParameter.getParameter(ConstantesMeLanbide48.COLEC_ACTIVIDADES_SQ, ConstantesMeLanbide48.FICHERO_PROPIEDADES), con);
                    if (codTray == null) {
                        throw new Exception();
                    }
                    tray.setCodIdActividad(Integer.valueOf(codTray.toString()));

                    //Es un registro nuevo
                    query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_ACTIVIDADES, ConstantesMeLanbide48.FICHERO_PROPIEDADES)
                            + "(COLEC_ACTIV_COD"
                            + ",COLEC_ACTIV_TIPO"
                            + ",COLEC_ACTIV_EXP_EJE"
                            + ",COLEC_ACTIV_NUMEXP"
                            + ",COLEC_ACTIV_COD_ENTIDAD"
                            + ",COLEC_ACTIV_DESC_SERVPUB"
                            + ",COLEC_ACTIV_INICIO"
                            + ",COLEC_ACTIV_FIN"
                            + ")"
                            + " values(" + tray.getCodIdActividad()
                            + ", '" + tray.getTipoActividad()+ "'"
                            + ", " + tray.getEjercicio() + ""
                            + ", '" + tray.getNumExpediente() + "'"
                            + ", " + tray.getCodEntidad()
                            + "," + (tray.getDesActividadyServPublEmp()!= null && !tray.getDesActividadyServPublEmp().equals("") ? "'" + tray.getDesActividadyServPublEmp() + "'" : "null")
                            + "," + (tray.getFechaInicio() != null ? "to_date('" + MeLanbide48Utils.formatearFecha_ddmmyyyy(tray.getFechaInicio()) + "','dd/mm/yyyy')" : "null")
                            + "," + (tray.getFechaFin() != null ? "to_date('" + MeLanbide48Utils.formatearFecha_ddmmyyyy(tray.getFechaFin()) + "','dd/mm/yyyy')" : "null")
                            + ")";
                } else {
                    //Es un registro que ya existe
                    query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_ACTIVIDADES, ConstantesMeLanbide48.FICHERO_PROPIEDADES)
                            + " set"
                            + " COLEC_ACTIV_TIPO='" + tray.getTipoActividad()+ "'"
                            + ",COLEC_ACTIV_DESC_SERVPUB='" + tray.getDesActividadyServPublEmp()+ "'"
                            + ",COLEC_ACTIV_INICIO=" + (tray.getFechaInicio() != null ? "to_date('" + MeLanbide48Utils.formatearFecha_ddmmyyyy(tray.getFechaInicio()) + "','dd/mm/yyyy')" : "null")
                            + ",COLEC_ACTIV_FIN=" + (tray.getFechaFin() != null ? "to_date('" + MeLanbide48Utils.formatearFecha_ddmmyyyy(tray.getFechaFin()) + "','dd/mm/yyyy')" : "null")
                            + ",COLEC_ACTIV_COD_ENTIDAD=" + (tray.getCodEntidad()!= null ? tray.getCodEntidad() : "null")
                            + " where COLEC_ACTIV_EXP_EJE=" + tray.getEjercicio()
                            + " and COLEC_ACTIV_NUMEXP='" + tray.getNumExpediente() + "'"
                            //+ " and COLEC_ACTIV_COD_ENTIDAD=" + tray.getCodEntidad() 
                            + " and COLEC_ACTIV_COD = " + tray.getCodIdActividad();
                }
                if (log.isDebugEnabled()) {
                    log.error("sql = " + query);
                }
                st = con.createStatement();
                int res = st.executeUpdate(query);
                if (res > 0) {
                    return tray;
                } else {
                    log.error("Se ha producido un error guardando los datos de trayectoria general actividades para entidad " + (tray != null ? tray.getCodEntidad() : "(tray = null)") + " y expediente " + (tray != null ? tray.getNumExpediente(): "(centro = null)"));
                    throw new Exception();
                }
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error guardando los datos de trayectoria general Actividades para entidad " + (tray != null ? tray.getCodEntidad() : "(tray = null)") + " y expediente " + (tray != null ? tray.getNumExpediente(): "(centro = null)"), ex);
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }

    public int eliminarTrayectoriaGralOtrosProgramas(ColecTrayOtroProgramaVO tray, Connection con) throws Exception {
        log.debug("eliminarTrayectoriaGralOtrosProgramas - Begin()");
        Statement st = null;
        int result = 0;
        try {

            String query = null;
            query = "delete from " + ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_OTROS_PROGRAMAS, ConstantesMeLanbide48.FICHERO_PROPIEDADES)
                    + " where"
                    + " COLEC_OTRPRO_COD = " + tray.getCodIdOtroPrograma()
                    + " and COLEC_OTRPRO_NUMEXP = '" + tray.getNumExpediente()+ "'"
                    + " and COLEC_OTRPRO_EXP_EJE = " + tray.getEjercicio()
                    + " and COLEC_OTRPRO_COD_ENTIDAD = " + tray.getCodEntidad();

            if (log.isDebugEnabled()) {
                log.error("sql = " + query);
            }
            st = con.createStatement();
            result = st.executeUpdate(query);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD eliminando trayectoria general otros programas ", ex);
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        log.debug("eliminarTrayectoriaGralOtrosProgramas - End()" + result);
        return result;
    }

    public int eliminarTrayectoriaGralActividades(ColecTrayActividadVO tray, Connection con) throws Exception {
        log.debug("eliminarTrayectoriaGralActividades - Begin()");
        Statement st = null;
        int result = 0;
        try {

            String query = null;
            query = "delete from " + ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_ACTIVIDADES, ConstantesMeLanbide48.FICHERO_PROPIEDADES)
                    + " where"
                    + " COLEC_ACTIV_COD = " + tray.getCodIdActividad()
                    + " and COLEC_ACTIV_NUMEXP = '" + tray.getNumExpediente() + "'"
                    + " and COLEC_ACTIV_EXP_EJE = " + tray.getEjercicio()
                    + " and COLEC_ACTIV_COD_ENTIDAD = " + tray.getCodEntidad();

            if (log.isDebugEnabled()) {
                log.error("sql = " + query);
            }
            st = con.createStatement();
            result = st.executeUpdate(query);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD eliminando trayectoria general actividades ", ex);
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        log.debug("eliminarTrayectoriaGralActividades - End()" + result);
        return result;
    }

    public ColecUbicacionesCTVO guardarUbicacionesCT(ColecUbicacionesCTVO ubicacion, Connection con) throws Exception {
        log.debug("guardarUbicacionesCT - Begin()");
        Statement st = null;
        try {
            if (ubicacion.getCodEntidad() == null || (ubicacion.getNumExpediente() == null || ubicacion.getNumExpediente().equals(""))) {
                log.error("Se ha producido un error guardando los datos de ubicaciones   para entidad " + (ubicacion != null ? ubicacion.getCodEntidad() : "(tray = null)") + " y expediente " + (ubicacion != null ? ubicacion.getNumExpediente() : "(centro = null)"));
                throw new Exception();
            } else {
                String query = null;
                if (ubicacion.getCodId() == null) {
                    Long codTray = this.getNextId(ConfigurationParameter.getParameter(ConstantesMeLanbide48.COLEC_UBICACIONES_CT_SQ, ConstantesMeLanbide48.FICHERO_PROPIEDADES), con);
                    if (codTray == null) {
                        throw new Exception();
                    }
                    ubicacion.setCodId(codTray);

                    //Es un registro nuevo
                    query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_UBICACIONES_CT, ConstantesMeLanbide48.FICHERO_PROPIEDADES)
                            +"(COLEC_UBIC_CT_COD		"	
                            +",COLEC_UBIC_CT_NUMEXP     "
                            +",COLEC_UBIC_CT_TIPO       "
                            +",COLEC_UBIC_CT_CODENTIDAD "
                            +",COLEC_UBIC_CT_TERRITORIO "
                            +",COLEC_UBIC_CT_NROCOMARCA "
                            +",COLEC_UBIC_CT_MUNICIPIO  "
                            +",COLEC_UBIC_CT_LOCALIDAD  "
                            +",COLEC_UBIC_CT_DIRECCION  "
                            +",COLEC_UBIC_CT_PORTAL_DIR "
                            +",COLEC_UBIC_CT_PISO_DIR   "
                            +",COLEC_UBIC_CT_LETRA_DIR  "
                            +",COLEC_UBIC_CT_CODPOSTAL  "
                            +",COLEC_UBIC_CT_TELEFONO   "
                            +",COLEC_UBIC_CT_LOCALPREVAPRO   "
                            +",COLEC_UBIC_CT_MATENREQ_LPA   "
                            +",COLEC_UBIC_CT_ESP_COMP_WIFI   "
                            +",FK_ID_AMBITO_SOLICITADO   "
                            +")"
                            + " values(" + ubicacion.getCodId()
                            + ", '" + ubicacion.getNumExpediente()+ "'"
                            + ", '" + ubicacion.getCodTipoColectivo()+ "'"
                            + ",  " + (ubicacion.getCodEntidad()!=null?ubicacion.getCodEntidad():"null")
                            + ",  " + (ubicacion.getTerritorioHist()!=null?ubicacion.getTerritorioHist():"null")
                            + ",  " + (ubicacion.getComarca()!=null?ubicacion.getComarca():"null")
                            + ",  " + (ubicacion.getMunicipio()!=null?ubicacion.getMunicipio():"null")
                            + ", '" + (ubicacion.getLocalidad()!=null?ubicacion.getLocalidad():"")+"'"
                            + ", '" + (ubicacion.getDireccion()!=null?ubicacion.getDireccion():"")+"'"
                            + ", '" + (ubicacion.getDireccionPortal()!=null?ubicacion.getDireccionPortal():"")+"'"
                            + ", '" + (ubicacion.getDireccionPiso()!=null?ubicacion.getDireccionPiso():"")+"'"
                            + ", '" + (ubicacion.getDireccionLetra()!=null?ubicacion.getDireccionLetra():"")+"'"
                            + ", '" + (ubicacion.getCodigoPostal()!=null?ubicacion.getCodigoPostal():"")+"'"
                            + ", '" + (ubicacion.getTelefono()!=null?ubicacion.getTelefono():"")+"'"
                            + ", " + (ubicacion.getLocalesPreviamenteAprobados()!=null?ubicacion.getLocalesPreviamenteAprobados():"null")
                            + ", " + (ubicacion.getMantieneRequisitosLocalesAprob()!=null?ubicacion.getMantieneRequisitosLocalesAprob():"null")
                            + ", " + (ubicacion.getDisponeEspacioComplWifi()!=null?ubicacion.getDisponeEspacioComplWifi():"null")
                            + ", " + (ubicacion.getFkIdAmbitoSolicitado()!=null?ubicacion.getFkIdAmbitoSolicitado():"null")
                            + ")";
                } else {
                    //Es un registro que ya existe
                    query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_UBICACIONES_CT, ConstantesMeLanbide48.FICHERO_PROPIEDADES)
                            + " set "
                            +"COLEC_UBIC_CT_CODENTIDAD  ="+(ubicacion.getCodEntidad()!=null?ubicacion.getCodEntidad():"null")
                            +",COLEC_UBIC_CT_TIPO  ='"+ubicacion.getCodTipoColectivo()+"'"
                            +",COLEC_UBIC_CT_TERRITORIO =" + (ubicacion.getTerritorioHist()!=null?ubicacion.getTerritorioHist():"null")
                            +",COLEC_UBIC_CT_NROCOMARCA ="+(ubicacion.getComarca()!=null?ubicacion.getComarca():"null")
                            +",COLEC_UBIC_CT_MUNICIPIO  ="+(ubicacion.getMunicipio()!=null?ubicacion.getMunicipio():"null")
                            +",COLEC_UBIC_CT_LOCALIDAD  ='"+(ubicacion.getLocalidad()!=null?ubicacion.getLocalidad():"")+"'"
                            +",COLEC_UBIC_CT_DIRECCION  ='"+(ubicacion.getDireccion()!=null?ubicacion.getDireccion():"")+"'"
                            +",COLEC_UBIC_CT_PORTAL_DIR ='"+(ubicacion.getDireccionPortal()!=null?ubicacion.getDireccionPortal():"")+"'"
                            +",COLEC_UBIC_CT_PISO_DIR   ='"+(ubicacion.getDireccionPiso()!=null?ubicacion.getDireccionPiso():"")+"'"
                            +",COLEC_UBIC_CT_LETRA_DIR  ='"+ (ubicacion.getDireccionLetra()!=null?ubicacion.getDireccionLetra():"")+"'"
                            +",COLEC_UBIC_CT_CODPOSTAL  ='"+(ubicacion.getCodigoPostal()!=null?ubicacion.getCodigoPostal():"")+"'"
                            +",COLEC_UBIC_CT_TELEFONO   ='"+(ubicacion.getTelefono()!=null?ubicacion.getTelefono():"")+"'"
                            +",COLEC_UBIC_CT_LOCALPREVAPRO  ="+(ubicacion.getLocalesPreviamenteAprobados()!=null?ubicacion.getLocalesPreviamenteAprobados():"null")
                            +",COLEC_UBIC_CT_MATENREQ_LPA   ="+(ubicacion.getMantieneRequisitosLocalesAprob()!=null?ubicacion.getMantieneRequisitosLocalesAprob():"null")
                            +",COLEC_UBIC_CT_ESP_COMP_WIFI   ="+(ubicacion.getDisponeEspacioComplWifi()!=null?ubicacion.getDisponeEspacioComplWifi():"null")
                            +",FK_ID_AMBITO_SOLICITADO   ="+(ubicacion.getFkIdAmbitoSolicitado()!=null?ubicacion.getFkIdAmbitoSolicitado():"null")
                            + " where COLEC_UBIC_CT_COD=" + ubicacion.getCodId()
                            + " and COLEC_UBIC_CT_NUMEXP='" + ubicacion.getNumExpediente() + "'"
                            ;
                }
                if (log.isDebugEnabled()) {
                    log.error("sql = " + query);
                }
                st = con.createStatement();
                int res = st.executeUpdate(query);
                if (res > 0) {
                    return ubicacion;
                } else {
                    log.error("Se ha producido un error guardando los datos de ubicacion entidad " + (ubicacion != null ? ubicacion.getCodEntidad() : "(tray = null)") + " y expediente " + (ubicacion != null ? ubicacion.getNumExpediente() : "(centro = null)"));
                    throw new Exception();
                }
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error guardando los datos de ubicaciones para entidad " + (ubicacion != null ? ubicacion.getCodEntidad() : "(tray = null)") + " y expediente " + (ubicacion != null ? ubicacion.getNumExpediente() : "(centro = null)"), ex);
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }

    public List<ColecUbicacionesCTVO> getUbicacionesCTxNumExpediente(String numExp, Integer idioma, Integer idBDConvocataria,Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<ColecUbicacionesCTVO> retList = new ArrayList<ColecUbicacionesCTVO>();
        if (numExp != null && !numExp.equals("")) {
            try {
                String nombreTablaEntidad = definirTablaConsultaEntidadSegunEntAgrupSioNo(numExp, con);
                String query = null;
                query = " SELECT C.* "
                        + ",DATOS_ENTIDAD.COLEC_ENT_CIF "
                        + ",DATOS_ENTIDAD.COLEC_ENT_NOMBRE "
                        + ",COLEC_AMBITO.COLEC_DESC_AMBITO DESC_TERRITORIO "
                        + ",(case when 4="+(idioma!=null?idioma:1)+" then colec_comarcas.colec_desc_comarca_eu else colec_comarcas.colec_desc_comarca end) desc_comarca "
                        + ",(case when 4="+(idioma!=null?idioma:1)+" then colec_municipio.colec_desc_mun_eu else colec_municipio.colec_desc_mun end) desc_municpio "
                        + ",(case when 4="+(idioma!=null?idioma:1)+" then colec_colectivo.descripcioneu else colec_colectivo.descripcion end)  descColectivo "
                        + ",(case when 4="+(idioma!=null?idioma:1)+" then cabh.ambitodescripcioneu else cabh.ambitodescripcion end) desc_ambitosolicitado "
                        + " from " + ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_UBICACIONES_CT, ConstantesMeLanbide48.FICHERO_PROPIEDADES) + " C "
                        + " LEFT JOIN " + nombreTablaEntidad + " DATOS_ENTIDAD ON C.COLEC_UBIC_CT_NUMEXP=DATOS_ENTIDAD.COLEC_NUMEXP AND C.COLEC_UBIC_CT_CODENTIDAD=DATOS_ENTIDAD.COLEC_ENT_COD "
                        + " left join " + ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_AMBITO, ConstantesMeLanbide48.FICHERO_PROPIEDADES) + " ON COLEC_AMBITO.COLEC_COD_AMBITO=C.COLEC_UBIC_CT_TERRITORIO "
                        + " left join " + ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_COMARCAS, ConstantesMeLanbide48.FICHERO_PROPIEDADES) + " on COLEC_COMARCAS.COLEC_COD_AMBITO=C.COLEC_UBIC_CT_TERRITORIO and COLEC_COMARCAS.COLEC_COD_COMARCA=C.COLEC_UBIC_CT_NROCOMARCA "
                        + " left join " + ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_MUN, ConstantesMeLanbide48.FICHERO_PROPIEDADES) + " on COLEC_MUNICIPIO.COLEC_COD_COMARCA=C.COLEC_UBIC_CT_NROCOMARCA and COLEC_MUNICIPIO.COLEC_COD_MUN=C.COLEC_UBIC_CT_MUNICIPIO "
                        + " left join " + ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_COLECTIVO, ConstantesMeLanbide48.FICHERO_PROPIEDADES) + " on colec_colectivo.id=c.COLEC_UBIC_CT_TIPO "
                        + " left join COLEC_AMBITOS_BLOQUES_HORAS cabh on cabh.idfkconvocatoriaactiva="+(idBDConvocataria!=null?idBDConvocataria:"null")+" and cabh.colectivo=c.COLEC_UBIC_CT_TIPO and  cabh.id=c.FK_ID_AMBITO_SOLICITADO "
                        + " where c.COLEC_UBIC_CT_NUMEXP = '" + numExp + "'"
                        + " order by DESC_TERRITORIO, DESC_COMARCA, DESC_MUNICPIO";

                if (log.isDebugEnabled()) {
                    log.error("sql = " + query);
                }
                st = con.createStatement();
                rs = st.executeQuery(query);
                while (rs.next()) {
                    retList.add((ColecUbicacionesCTVO) MeLanbide48MappingUtils.getInstance().map(rs, ColecUbicacionesCTVO.class));
                }
            } catch (Exception ex) {
                log.error("Se ha producido una excepcion en la BBDD recuperando centros para expediente " + (numExp != null ? numExp : " (expediente = null)"), ex);
                throw new Exception(ex);
            } finally {
                try {
                    if (log.isDebugEnabled()) {
                        log.debug("Procedemos a cerrar el statement y el resultset");
                    }
                    if (st != null) {
                        st.close();
                    }
                    if (rs != null) {
                        rs.close();
                    }
                } catch (Exception e) {
                    log.error("Se ha producido un error cerrando el statement y el resulset", e);
                    throw new Exception(e);
                }
            }
        }
        return retList;
    }

    public List<ColecComproRealizacionVO> getDatosEntidadadesPorcenComproRealixColecyTHxExpte(String numExpediente, Connection con) throws Exception {
        log.debug("getDatosEntidadadesPorcenComproRealixColecyTHxExpte - Begin() - DAO ");
        Statement st = null;
        ResultSet rs = null;
        List<ColecComproRealizacionVO> retList = new ArrayList<ColecComproRealizacionVO>();
        if (numExpediente != null && !numExpediente.equals("")) {
            try {
                String nombreTablaEntidad = definirTablaConsultaEntidadSegunEntAgrupSioNo(numExpediente, con);
                String query = null;
                query = " SELECT DPCR.* "
                        + ",DATOS_ENTIDAD.COLEC_ENT_CIF||' - '||DATOS_ENTIDAD.COLEC_ENT_NOMBRE COLEC_ENT_NOMBRE "
                        + ",DATOS_ENTIDAD.COLEC_ENT_COD "
                        + " from " + ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_COMPREAL_XCOLTH, ConstantesMeLanbide48.FICHERO_PROPIEDADES) + " DPCR "
                        + " left join " + nombreTablaEntidad + " DATOS_ENTIDAD ON DATOS_ENTIDAD.COLEC_NUMEXP=DPCR.COLEC_COMPREAL_NUM_EXP AND DATOS_ENTIDAD.COLEC_ENT_COD=DPCR.COLEC_COMPREAL_COD_ENTIDAD "
                        + " where DATOS_ENTIDAD.COLEC_NUMEXP='" + numExpediente + "'"
                        + " order by COLEC_COMPREAL_COD_ENTIDAD,COLEC_COMPREAL_COLECTIVO, COLEC_COMPREAL_TTHH";

                if (log.isDebugEnabled()) {
                    log.error("sql = " + query);
                }
                st = con.createStatement();
                rs = st.executeQuery(query);
                while (rs.next()) {
                    retList.add((ColecComproRealizacionVO) MeLanbide48MappingUtils.getInstance().map(rs, ColecComproRealizacionVO.class));
                }
            } catch (Exception ex) {
                log.error("Se ha producido una excepcion en la BBDD recuperando centros para expediente " + (numExpediente != null ? numExpediente : " (expediente = null)"), ex);
                throw new Exception(ex);
            } finally {
                try {
                    if (log.isDebugEnabled()) {
                        log.debug("Procedemos a cerrar el statement y el resultset");
                    }
                    if (st != null) {
                        st.close();
                    }
                    if (rs != null) {
                        rs.close();
                    }
                } catch (Exception e) {
                    log.error("Se ha producido un error cerrando el statement y el resulset", e);
                    throw new Exception(e);
                }
            }
        }
        log.debug("getDatosEntidadadesPorcenComproRealixColecyTHxExpte - End() - DAO ");
        return retList;
    }
    public List<ColecComproRealizacionVO> getListaEntidadadesInterfazPorcenComproRealixColecyTHxExpte(String numExpediente, Connection con) throws Exception {
        log.debug("getListaEntidadadesInterfazPorcenComproRealixColecyTHxExpte - Begin() - DAO ");
        Statement st = null;
        ResultSet rs = null;
        List<ColecComproRealizacionVO> retList = new ArrayList<ColecComproRealizacionVO>();
        if (numExpediente != null && !numExpediente.equals("")) {
            try {
                String nombreTablaEntidad = definirTablaConsultaEntidadSegunEntAgrupSioNo(numExpediente, con);
                String query = null;
                query = " SELECT DISTINCT DPCR.COLEC_COMPREAL_EJERCICIO, DPCR.COLEC_COMPREAL_NUM_EXP, DPCR.COLEC_COMPREAL_COD_ENTIDAD "
                        + ",DATOS_ENTIDAD.COLEC_ENT_CIF||' - '||DATOS_ENTIDAD.COLEC_ENT_NOMBRE COLEC_ENT_NOMBRE "
                        + ",DATOS_ENTIDAD.COLEC_ENT_COD "
                        // Añadimos las columnas para que no falle el mapeo. Las cargamos con valor null, para respetar la agrupacion la agrupacion.
                        + ",'' COLEC_COMPREAL_COD_ID "
                        + ",'' COLEC_COMPREAL_COLECTIVO "
                        + ",'' COLEC_COMPREAL_TTHH "
                        + ",'' COLEC_COMPREAL_PORCENT "
                        + " from " + nombreTablaEntidad + " DATOS_ENTIDAD "
                        + " left join " + ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_COMPREAL_XCOLTH, ConstantesMeLanbide48.FICHERO_PROPIEDADES) + " DPCR ON DATOS_ENTIDAD.COLEC_NUMEXP=DPCR.COLEC_COMPREAL_NUM_EXP AND DATOS_ENTIDAD.COLEC_ENT_COD=DPCR.COLEC_COMPREAL_COD_ENTIDAD "
                        + " where DATOS_ENTIDAD.COLEC_NUMEXP='" + numExpediente + "'"
                        + " order by DATOS_ENTIDAD.COLEC_ENT_COD ";

                if (log.isDebugEnabled()) {
                    log.error("sql = " + query);
                }
                st = con.createStatement();
                rs = st.executeQuery(query);
                while (rs.next()) {
                    retList.add((ColecComproRealizacionVO) MeLanbide48MappingUtils.getInstance().map(rs, ColecComproRealizacionVO.class));
                }
            } catch (Exception ex) {
                log.error("Se ha producido una excepcion en la BBDD recuperando centros para expediente " + (numExpediente != null ? numExpediente : " (expediente = null)"), ex);
                throw new Exception(ex);
            } finally {
                try {
                    if (log.isDebugEnabled()) {
                        log.debug("Procedemos a cerrar el statement y el resultset");
                    }
                    if (st != null) {
                        st.close();
                    }
                    if (rs != null) {
                        rs.close();
                    }
                } catch (Exception e) {
                    log.error("Se ha producido un error cerrando el statement y el resulset", e);
                    throw new Exception(e);
                }
            }
        }
        log.debug("getListaEntidadadesInterfazPorcenComproRealixColecyTHxExpte - End() - DAO ");
        return retList;
    }

    public void guardarDatosComproRealxColeTH(ColecComproRealizacionVO dato, Connection con) throws BDException, Exception {
        log.debug("guardarDatosComproRealxColeTH - Begin() - DAO ");
        Statement st = null;
        try {
            String query = null;
            // Recuperamos los existente para ese Año,Expediente,Colectivo,Provincia y Entidad
            // Deberia ser solo una linea. NO usamos el ID por facilitar diseño en jsp 
            // de momento la logica es que sea un registro por Expediente y Entidad.
            ColecComproRealizacionVO datoBD = getComproRealizacionVOxAnioExpColecProvyCodent(dato,con);
                if (datoBD == null){
                    // Es nuevo, lo mas probable es ques e la primera vez que se guardan datos para el expediente
                    Long codigoID = this.getNextId(ConfigurationParameter.getParameter(ConstantesMeLanbide48.COLEC_COMPREAL_XCOLTH_SQ, ConstantesMeLanbide48.FICHERO_PROPIEDADES), con);
                    if (codigoID == null) {
                        throw new Exception();
                    }
                    dato.setCodigoID(Integer.valueOf(codigoID.toString()));

                    //Es una entidad nueva
                    query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_COMPREAL_XCOLTH, ConstantesMeLanbide48.FICHERO_PROPIEDADES)
                            + "(COLEC_COMPREAL_COD_ID,COLEC_COMPREAL_EJERCICIO,COLEC_COMPREAL_NUM_EXP,COLEC_COMPREAL_COD_ENTIDAD"
                            + ",COLEC_COMPREAL_COLECTIVO,COLEC_COMPREAL_TTHH,COLEC_COMPREAL_PORCENT)"
                            + " values(" + (dato.getCodigoID()!= null ? dato.getCodigoID() : "null")
                            + ", " + (dato.getEjercicio()!= null ? dato.getEjercicio() : "null")
                            + ", '" + dato.getNumExpediente() + "'"
                            + ", " + (dato.getCodigoEntidad()!= null ? dato.getCodigoEntidad() : "null")
                            + ", " + (dato.getColectivo()!= null ? dato.getColectivo() : "null")
                            + ", " + (dato.getTerritorioHistorico()!= null ? dato.getTerritorioHistorico() : "null")
                            + ", " + (dato.getPorcentajeCompReal()!= null ? dato.getPorcentajeCompReal() : "null")
                            + ")";
                } else {
                    // Existen datos previos, actualizamos
                    // Ojo: No deberia existir mas de una linea, por no usamos el ID de la sequencia
                    // En caso de haberlas se actualizan todas con el mismo valor recibido
                    query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_COMPREAL_XCOLTH, ConstantesMeLanbide48.FICHERO_PROPIEDADES)
                            + " set"
                            + " COLEC_COMPREAL_PORCENT = " + (dato.getPorcentajeCompReal()!= null ? dato.getPorcentajeCompReal(): "null")
                            + " where COLEC_COMPREAL_EJERCICIO =" + dato.getEjercicio()
                            + " and COLEC_COMPREAL_NUM_EXP = '" + dato.getNumExpediente()+"'"
                            + " and COLEC_COMPREAL_COD_ENTIDAD = " + dato.getCodigoEntidad()
                            + " and COLEC_COMPREAL_COLECTIVO = " + dato.getColectivo()
                            + " and COLEC_COMPREAL_TTHH = " + dato.getTerritorioHistorico()
                        ;
                }
                if (log.isDebugEnabled()) {
                    log.error("sql = " + query);
                }
                st = con.createStatement();
                int res = st.executeUpdate(query);
                if(res==0){
                    throw new Exception("Se ha intentado actualizar un registro existente, pero no se ha encontrado esa linea en BBDD");
                }else{
                    if(res>1){
                        log.error(" guardarDatosComproRealxColeTH -  OJO !! : Se ha detectado que hay mas de una linea para Una entidad-Año-Expediente-COlectivo-TH al guardar datos de Porcentaje Compromiso de realizacion. "
                                + "  Lineas Actualizadas : " + res
                                + "  parametros tratados : " + dato.getEjercicio() +"-"+ dato.getNumExpediente() +"-"+  dato.getCodigoEntidad() +"-"+ dato.getColectivo() +"-"+ dato.getTerritorioHistorico()
                        );
                    }
                }

        } catch (BDException ex) {
            log.error("Se ha producido un error guardando los datos porcentaje compromiso  de la entidad : " + (dato != null ? dato.getCodigoEntidad() : "(datos = null)") + " para el expediente " + (dato != null ? dato.getNumExpediente(): "(datos = null)"), ex);
            throw ex;
        } catch (Exception ex) {
            log.error("Se ha producido un error guardando los datos porcentaje compromiso  de la entidad : " + (dato != null ? dato.getCodigoEntidad() : "(datos = null)") + " para el expediente " + (dato != null ? dato.getNumExpediente(): "(datod = null)"), ex);
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        log.debug("guardarDatosComproRealxColeTH - End() - DAO ");
    }

    private ColecComproRealizacionVO getComproRealizacionVOxAnioExpColecProvyCodent(ColecComproRealizacionVO dato, Connection con) throws Exception {
        log.debug("getComproRealizacionVOxAnioExpColecProvyCodent - Begin() - DAO ");
        if (dato != null) {
            Statement st = null;
            ResultSet rs = null;
            ColecComproRealizacionVO entidadCompReal = null;
            try {
                String nombreTablaEntidad = definirTablaConsultaEntidadSegunEntAgrupSioNo(dato.getNumExpediente(), con);
                String query = " SELECT DPCR.* "
                        + ",DATOS_ENTIDAD.COLEC_ENT_CIF||' - '||DATOS_ENTIDAD.COLEC_ENT_NOMBRE COLEC_ENT_NOMBRE "
                        + ",DATOS_ENTIDAD.COLEC_ENT_COD "
                        + " from " + nombreTablaEntidad + " DATOS_ENTIDAD "
                        + " left join " + ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_COMPREAL_XCOLTH, ConstantesMeLanbide48.FICHERO_PROPIEDADES) + " DPCR ON DATOS_ENTIDAD.COLEC_NUMEXP=DPCR.COLEC_COMPREAL_NUM_EXP AND DATOS_ENTIDAD.COLEC_ENT_COD=DPCR.COLEC_COMPREAL_COD_ENTIDAD "
                        + " where DATOS_ENTIDAD.COLEC_NUMEXP='" + dato.getNumExpediente() + "'"
                        + " AND  COLEC_COMPREAL_EJERCICIO="+(dato.getEjercicio()!=null?dato.getEjercicio():"null")
                        + " AND  COLEC_COMPREAL_NUM_EXP='"+(dato.getNumExpediente()!=null?dato.getNumExpediente():"null")+"'"
                        + " AND  COLEC_COMPREAL_COD_ENTIDAD="+(dato.getCodigoEntidad()!=null?dato.getCodigoEntidad():"null")
                        + " AND  COLEC_COMPREAL_COLECTIVO="+(dato.getColectivo()!=null?dato.getColectivo():"null")
                        + " AND  COLEC_COMPREAL_TTHH="+(dato.getTerritorioHistorico()!=null?dato.getTerritorioHistorico():"null")
                        + " order by COLEC_COMPREAL_COD_ENTIDAD,COLEC_COMPREAL_COLECTIVO, COLEC_COMPREAL_TTHH";
                if (log.isDebugEnabled()) {
                    log.error("sql = " + query);
                }
                st = con.createStatement();
                rs = st.executeQuery(query);
                if (rs.next()) {
                    entidadCompReal = (ColecComproRealizacionVO) MeLanbide48MappingUtils.getInstance().map(rs, ColecComproRealizacionVO.class);
                }
                log.debug("getComproRealizacionVOxAnioExpColecProvyCodent - End() - DAO ");
                return entidadCompReal;
            } catch (Exception ex) {
                log.error("Se ha producido un error recuperando datos entidad compromiso realizacion " + dato.getCodigoEntidad() + " para expediente " + dato.getNumExpediente(), ex);
                throw new Exception(ex);
            } finally {
                try {
                    if (log.isDebugEnabled()) {
                        log.debug("Procedemos a cerrar el statement y el resultset");
                    }
                    if (st != null) {
                        st.close();
                    }
                    if (rs != null) {
                        rs.close();
                    }
                } catch (SQLException e) {
                    log.error("Se ha producido un error cerrando el statement y el resulset", e);
                    throw new Exception(e);
                }
            }
        } else {
            log.debug("getComproRealizacionVOxAnioExpColecProvyCodent - End() Retornando null dato entrada venia a null - DAO ");
            return null;
        }
    }

    /**
     *
     * @param numExpediente
     * @param codEntidad
     * @param con
     * @return
     * @throws Exception
     */
    public int eliminarColecComproRealizacionVOxEntExpte(String numExpediente, Long codEntidad, Connection con) throws Exception {
        Statement st = null;
        try {
            String query;
            query = "delete from " + ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_COMPREAL_XCOLTH, ConstantesMeLanbide48.FICHERO_PROPIEDADES)
                    + " where COLEC_COMPREAL_COD_ENTIDAD = " + (codEntidad!=null?codEntidad:"null")
                    + " and COLEC_COMPREAL_NUM_EXP = '" + numExpediente + "'"
                    ;
            if (log.isDebugEnabled()) {
                log.error("sql = " + query);
            }
            st = con.createStatement();
            return st.executeUpdate(query);
        } catch (SQLException ex) {
            log.error("Se ha producido un error eliminando datos de porcentaje compromiso de realizacion entidad   " + codEntidad + " Expediente : " + numExpediente, ex);
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }

    public Date getFechaAltaExpediente(Integer codOrganizacion, String numExpediente, Connection con) throws Exception {
        log.debug("getFechaAltaExpediente - Begin() ");
        Date respuesta = null;
        Statement st = null;
        ResultSet rs = null;
        if (numExpediente != null) {
            try {
                String query = " SELECT exp_fei from e_exp "
                        + " where exp_mun="+(codOrganizacion!=null ? codOrganizacion : "null")
                        + " and exp_num='"+numExpediente+"'"
                        ;
                if (log.isDebugEnabled()) {
                    log.error("sql = " + query);
                }
                st = con.createStatement();
                rs = st.executeQuery(query);
                if (rs.next()) {
                    respuesta = rs.getDate("exp_fei");
                }
            } catch (Exception ex) {
                log.error("Se ha producido un error recuperando datos fecha alta expediente " + numExpediente, ex);
                throw new Exception(ex);
            } finally {
                try {
                    if (log.isDebugEnabled()) {
                        log.debug("Procedemos a cerrar el statement y el resultset");
                    }
                    if (st != null) {
                        st.close();
                    }
                    if (rs != null) {
                        rs.close();
                    }
                } catch (SQLException e) {
                    log.error("Se ha producido un error cerrando el statement y el resulset", e);
                    throw new Exception(e);
                }
            }
        }
        return respuesta;
    }

    public int eliminarLineaColecSolicitud(String identificadorBDEliminar, Connection con) throws SQLException {
        Statement st = null;
        try {
            String query=" delete from " + ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_SOLICITUD, ConstantesMeLanbide48.FICHERO_PROPIEDADES)
                    + " where COLEC_COD_SOLICITUD = " + identificadorBDEliminar;
            log.info("sql = " + query);
            st = con.createStatement();
            return st.executeUpdate(query);
        } catch (SQLException ex) {
            log.error("Se ha producido un error eliminando Datos COlecSolicitud  " + identificadorBDEliminar, ex);
            throw ex;
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw e;
            }
        }
    }
    
    public List<ColecProgConvActPredefinidaColectivo> getListaConvocatoriasPredefinidaXGrupoXColectivo(Integer idioma, Integer codigoGrupo, Integer colectivo,Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<ColecProgConvActPredefinidaColectivo> retList = new ArrayList<ColecProgConvActPredefinidaColectivo>();
        try {
            String query = "select cpcapc.ID, cpcapc.IDCONVOCATORIAACTIVA, cpcapc.CODIGOGRUPO, cpcapc.CODIGOCONVOCATORIAPRED, cpcapc.COLECTIVO "
                    + " ,case when 4="+ (idioma!=null?idioma:"null") +" then cpcap.decripcioneu else cpcap.descripcion end as CODIGOCONVOCATORIAPREDDES "
                    + " ,FECHAINICIO, FECHAFIN "
                    + " from COLEC_PROG_CONV_ACT_PRE_COL cpcapc "
                    + " LEFT JOIN COLEC_PROG_CONV_ACT_PREDEF cpcap ON cpcap.ID=cpcapc.codigoconvocatoriapred "
                    + " WHERE cpcapc.codigogrupo="+ (codigoGrupo!=null?codigoGrupo:"null") +" AND cpcapc.colectivo="+ (colectivo!=null?colectivo:"null")
                    + " order by id ";
            log.info("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                retList.add(new ColecProgConvActPredefinidaColectivo(
                    rs.getInt("id"),
                    rs.getInt("IDCONVOCATORIAACTIVA"),
                    "",
                    rs.getInt("codigoGrupo"),
                    "",
                    rs.getInt("codigoConvocatoriaPred"),
                    rs.getString("codigoConvocatoriaPredDes"),
                    rs.getInt("colectivo"),
                    "",
                    rs.getDate("fechaInicio"),
                    rs.getDate("fechaFin")
                    )
                );
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando lista Convocatorias predefinidas para colectivo 1.", ex);
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return retList;
    }
    
    public List<ColecTrayectoriaEntidad> getListaTrayectoriaAcreditableColectivo(String numExpediente, int colectivo, Connection con) throws SQLException, Exception {
        log.info(" getListaTrayectoriaAcreditableEntidadesColectivo - Begin " + numExpediente + " - Colectivo: " + colectivo + " " +  formatFechaLog.format(new Date()));
        List<ColecTrayectoriaEntidad> resultado = new ArrayList<ColecTrayectoriaEntidad>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String query = "select * "
                    + " from colec_trayectoria_entidad "
                    + " where "
                    + " trayNumExpediente=? "
                    + " and trayCodColectivo=? ";
            log.info("sql = " + query);
            ps = con.prepareStatement(query);
            ps.setString(1, numExpediente);
            ps.setInt(2, colectivo);
            log.info("params = " + numExpediente
                    + " " + colectivo);
            rs = ps.executeQuery();
            while (rs.next()) {
                resultado.add((ColecTrayectoriaEntidad) MeLanbide48MappingUtils.getInstance().map(rs, ColecTrayectoriaEntidad.class));
            }
        } catch (SQLException e) {
            log.error("Se ha producido  getListaTrayectoriaAcreditableEntidadesColectivo ", e);
            throw e;
        } catch (Exception e) {
            log.error("Se ha producido getListaTrayectoriaAcreditableEntidadesColectivo ", e);
            throw e;
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("Procedemos a cerrar el resultset");
            }
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        log.info(" getListaTrayectoriaAcreditableEntidadesColectivo - End " + numExpediente + " - Colectivo: " + colectivo + " " + formatFechaLog.format(new Date()));
        return resultado;
    }

    public List<ColecTrayectoriaEntidad> getListaTrayectoriaAcreditableGrupoColectivoEntidad(String numExpediente, int codigoGrupo, int colectivo, int entidadIDBD, Connection con) throws SQLException, Exception {
        log.info(" getListaTrayectoriaAcreditableGrupoColectivoEntidad - Begin " + numExpediente + " - Colectivo: " + colectivo + " - Entidad : " + entidadIDBD + " - Grupo : " + codigoGrupo + " " + formatFechaLog.format(new Date()));
        List<ColecTrayectoriaEntidad> resultado = new ArrayList<ColecTrayectoriaEntidad>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String query = "select * "
                    + " from colec_trayectoria_entidad "
                    + " where "
                    + " trayNumExpediente=? "
                    + " and TRAYIDFKPROGCONVACTGRUPO=? "
                    + " and trayCodColectivo=? "
                    + " and TRAYCODIGOENTIDAD=? ";
            log.info("sql = " + query);
            ps = con.prepareStatement(query);
            int i = 1;
            ps.setString(i++, numExpediente);
            ps.setInt(i++, codigoGrupo);
            ps.setInt(i++, colectivo);
            ps.setInt(i++, entidadIDBD);
            log.info("params (" + (i - 1) + ")= " + numExpediente
                    + " " + codigoGrupo
                    + " " + colectivo
                    + " " + entidadIDBD
            );
            rs = ps.executeQuery();
            while (rs.next()) {
                resultado.add((ColecTrayectoriaEntidad) MeLanbide48MappingUtils.getInstance().map(rs, ColecTrayectoriaEntidad.class));
            }
        } catch (SQLException e) {
            log.error("Se ha producido  getListaTrayectoriaAcreditableGrupoColectivoEntidad ", e);
            throw e;
        } catch (Exception e) {
            log.error("Se ha producido getListaTrayectoriaAcreditableGrupoColectivoEntidad ", e);
            throw e;
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("Procedemos a cerrar el resultset");
            }
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        log.info(" getListaTrayectoriaAcreditableGrupoColectivoEntidad - End " + numExpediente + " - Colectivo: " + colectivo + " - Entidad: " + entidadIDBD + " " + formatFechaLog.format(new Date()));
        return resultado;
    }

    /**
     * Guarda los datos de las convocatorias predefinidas - Como en interfaz son check/si-no tiene experiencia, si no la tiene se borrar el registro de BD
     * no se almacenan datos innesarios en la tabla para la entidad definidad
     * @param datos
     * @param con
     * @return
     * @throws BDException
     * @throws Exception 
     */
    public boolean guardarDatosColecConvocatoriasPredefColectivoEntidad(ColecTrayectoriaEntidad datos, Connection con) throws BDException, Exception {
        log.debug("guardarDatosColecConvocatoriasPredefColectivoEntidad - Begin() - DAO ");
        Statement st = null;
        try {
            String query = null;
            if(datos!= null){
//                if (datos.getId() == null) {
                    // Es nuevo
                    Long codigoID = this.getNextId(ConfigurationParameter.getParameter(ConstantesMeLanbide48.SEQ_COLEC_TRAYECTORIA_ENTIDA, ConstantesMeLanbide48.FICHERO_PROPIEDADES), con);
                    datos.setId(Integer.valueOf(codigoID.toString()));
                    //Es una entidad nueva
                    query = "insert into COLEC_TRAYECTORIA_ENTIDAD "
                            + "(ID,TRAYCODCOLECTIVO,TRAYIDFKPROGCONVACTGRUPO,TRAYIDFKPROGCONVACTSUBGRPRE,TRAYNUMEXPEDIENTE,TRAYCODIGOENTIDAD,TRAYTIENEEXPERIENCIA,TRAYNUMEROMESES)"
                            + " values(" + (datos.getId() != null ? datos.getId() : "null")
                            + ", " + (datos.getTrayCodColectivo() != null ? datos.getTrayCodColectivo() : "null")
                            + ", " + (datos.getTrayIdFkProgConvActGrupo() != null ? datos.getTrayIdFkProgConvActGrupo() : "null")
                            + ", " + (datos.getTrayIdFkProgConvActSubGrPre() != null ? datos.getTrayIdFkProgConvActSubGrPre() : "null")
                            + ", " + (datos.getTrayNumExpediente() != null ? "'" + datos.getTrayNumExpediente() + "'" : "null")
                            + ", " + (datos.getTrayCodigoEntidad() != null ? datos.getTrayCodigoEntidad() : "null")
                            + ", " + (datos.getTrayTieneExperiencia() != null ? datos.getTrayTieneExperiencia() : "null")
                            + ", " + (datos.getTrayNumeroMeses()!= null ? datos.getTrayNumeroMeses() : "0")
                            + ")";
//                } else {
                    // Existen datos previos, Se elimina si se ha marcado que no tiene experiencia  o se actualiza si existia vacio en BD
//                    if(datos.getTrayTieneExperiencia() != null && datos.getTrayTieneExperiencia()==1){
//                        query = "update COLEC_TRAYECTORIA_ENTIDAD "
//                                + " set "
//                                + " TRAYTIENEEXPERIENCIA = " + datos.getTrayTieneExperiencia()
//                                + " ,TRAYFECHAMODIFICACION = systimestamp "
//                                + " where ID =" + (datos.getId() != null ? datos.getId() : "null");
//                    }else{
//                        query = "delete from COLEC_TRAYECTORIA_ENTIDAD "
//                                + " where ID =" + (datos.getId() != null ? datos.getId() : "null");
//                    }
                    
//                }
                log.error("sql = " + query);
                st = con.createStatement();
                return st.executeUpdate(query) > 0;
            }else{
                log.info("Objeto recibido  a null para guardar ...");
                return false;
            }
        } catch (BDException ex) {
            log.error("Se ha producido BDException guardando los datos trayactoria predefinida" + (datos != null ? datos.getTrayCodigoEntidad(): "(datos = null)") + " para el expediente " + (datos != null ? datos.getTrayNumExpediente(): "(datos = null)"), ex);
            throw ex;
        } catch (Exception ex) {
            log.error("Se ha producido Exception guardando los datos trayactoria predefinida" + (datos != null ? datos.getTrayCodigoEntidad() : "(datos = null)") + " para el expediente " + (datos != null ? datos.getTrayNumExpediente() : "(datos = null)"), ex);
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    public boolean eliminarDatosColecConvocatoriasPredefColectivoEntidad(ColecTrayectoriaEntidad datos,Connection con) throws BDException, Exception {
        log.debug("guardarDatosColecConvocatoriasPredefColectivoEntidad - Begin() - DAO ");
        Statement st = null;
        try {
            if(datos!=null && datos.getTrayCodColectivo() != null && datos.getTrayCodigoEntidad()!= null && datos.getTrayNumExpediente() != null && !datos.getTrayNumExpediente().isEmpty()){                    
                String query = "delete from COLEC_TRAYECTORIA_ENTIDAD "
                        + " where TRAYIDFKPROGCONVACTGRUPO=2"
                        + " and TRAYNUMEXPEDIENTE ='" + datos.getTrayNumExpediente()  +"'"
                        + " and TRAYCODCOLECTIVO =" + datos.getTrayCodColectivo()
                        + " and TRAYCODIGOENTIDAD =" + datos.getTrayCodigoEntidad()
                        ;
                log.error("sql = " + query);
                st = con.createStatement();
                log.info("Elementos Eliminados: " + st.executeUpdate(query));
                return true;
            }else{
                log.info("Objetos recibidos  a null para guardar ...");
                return false;
            }
        } catch (Exception ex) {
            log.error("Se ha producido Exception eliminando los datos trayactoria predefinida" + (datos != null && datos.getTrayCodigoEntidad()!=null? datos.getTrayCodigoEntidad() : "(idEntidad = null)") + " para el expediente " + (datos != null && datos.getTrayNumExpediente()!=null? datos.getTrayNumExpediente(): "(numExpediente = null)"), ex);
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    
    public boolean guardarDatosColecTrayectoriaEntidad(ColecTrayectoriaEntidad datos, Connection con) throws BDException, Exception {
        log.debug("guardarDatosColecTrayectoriaEntidad - Begin() - DAO ");
        Statement st = null;
        try {
            String query = null;
            if(datos!= null){
                if (datos.getId() == null) {
                    // Es nuevo
                    Long codigoID = this.getNextId(ConfigurationParameter.getParameter(ConstantesMeLanbide48.SEQ_COLEC_TRAYECTORIA_ENTIDA, ConstantesMeLanbide48.FICHERO_PROPIEDADES), con);
                    datos.setId(Integer.valueOf(codigoID.toString()));
                    //Es una entidad nueva
                    query = "insert into COLEC_TRAYECTORIA_ENTIDAD "
                            + "(ID, TRAYCODCOLECTIVO, TRAYIDFKPROGCONVACTGRUPO, TRAYIDFKPROGCONVACTSUBGRPRE, TRAYNUMEXPEDIENTE, TRAYCODIGOENTIDAD, TRAYDESCRIPCION, TRAYTIENEEXPERIENCIA, TRAYNOMBREADMONPUBLICA, TRAYFECHAINICIO, TRAYFECHAFIN, TRAYNUMEROMESES)"
                            + " values(" + (datos.getId() != null ? datos.getId() : "null")
                            + ", " + (datos.getTrayCodColectivo() != null ? datos.getTrayCodColectivo() : "null")
                            + ", " + (datos.getTrayIdFkProgConvActGrupo() != null ? datos.getTrayIdFkProgConvActGrupo() : "null")
                            + ", " + (datos.getTrayIdFkProgConvActSubGrPre() != null ? datos.getTrayIdFkProgConvActSubGrPre() : "null")
                            + ", '" + (datos.getTrayNumExpediente() != null ? datos.getTrayNumExpediente() : "")+ "'"
                            + ", " + (datos.getTrayCodigoEntidad() != null ? datos.getTrayCodigoEntidad() : "null")
                            + ", '" + (datos.getTrayDescripcion()!= null ? datos.getTrayDescripcion() : "")+ "'"
                            + ", " + (datos.getTrayTieneExperiencia() != null ? datos.getTrayTieneExperiencia() : "null")
                            + ", '" + (datos.getTrayNombreAdmonPublica()!= null ? datos.getTrayNombreAdmonPublica(): "")+ "'"
                            + ", " + (datos.getTrayFechaInicio()!= null ? "to_date('"+formatFechaddMMyyyy.format(datos.getTrayFechaInicio())+"','dd/MM/yyyy')": "null")
                            + ", " + (datos.getTrayFechaFin()!= null ? "to_date('"+formatFechaddMMyyyy.format(datos.getTrayFechaFin())+"','dd/MM/yyyy')": "null")
                            + ", " + (datos.getTrayNumeroMeses()!= null ? datos.getTrayNumeroMeses() : "0")
                            + ")";
                } else {
                    // Existen datos previos, actualizamos
                    query = "update COLEC_TRAYECTORIA_ENTIDAD "
                            + " set "
                            + "TRAYCODCOLECTIVO=" + (datos.getTrayCodColectivo() != null ? datos.getTrayCodColectivo() : "null")
                            + ",TRAYIDFKPROGCONVACTGRUPO=" + (datos.getTrayIdFkProgConvActGrupo() != null ? datos.getTrayIdFkProgConvActGrupo() : "null")
                            + ",TRAYIDFKPROGCONVACTSUBGRPRE=" + (datos.getTrayIdFkProgConvActSubGrPre() != null ? datos.getTrayIdFkProgConvActSubGrPre() : "null")
                            + ",TRAYNUMEXPEDIENTE='" + (datos.getTrayNumExpediente() != null ? datos.getTrayNumExpediente() : "") + "'"
                            + ",TRAYCODIGOENTIDAD=" + (datos.getTrayCodigoEntidad() != null ? datos.getTrayCodigoEntidad() : "null")
                            + ",TRAYDESCRIPCION='" + (datos.getTrayDescripcion() != null ? datos.getTrayDescripcion() : "") + "'"
                            + ",TRAYTIENEEXPERIENCIA=" + (datos.getTrayTieneExperiencia() != null ? datos.getTrayTieneExperiencia() : "null")
                            + ",TRAYNOMBREADMONPUBLICA='" + (datos.getTrayNombreAdmonPublica() != null ? datos.getTrayNombreAdmonPublica() : "") + "'"
                            + ",TRAYFECHAINICIO=" + (datos.getTrayFechaInicio() != null ? "to_date('" + formatFechaddMMyyyy.format(datos.getTrayFechaInicio()) + "','dd/MM/yyyy')" : "null")
                            + ",TRAYFECHAFIN=" + (datos.getTrayFechaFin() != null ? "to_date('" + formatFechaddMMyyyy.format(datos.getTrayFechaFin()) + "','dd/MM/yyyy')" : "null")
                            + ",TRAYNUMEROMESES=" + (datos.getTrayNumeroMeses() != null ? datos.getTrayNumeroMeses() : "0")
                            + ",TRAYFECHAMODIFICACION = systimestamp "
                            + " where ID =" + datos.getId();
                }
                log.info("sql = " + query);
                st = con.createStatement();
                return st.executeUpdate(query) > 0;
            }else{
                log.info("Objeto recibido  a null para guardar ...");
                return false;
            }
        } catch (BDException ex) {
            log.error("Se ha producido BDException guardando los datos trayactoria predefinida" + (datos != null ? datos.getTrayCodigoEntidad(): "(datos = null)") + " para el expediente " + (datos != null ? datos.getTrayNumExpediente(): "(datos = null)"), ex);
            throw ex;
        } catch (Exception ex) {
            log.error("Se ha producido Exception guardando los datos trayactoria predefinida" + (datos != null ? datos.getTrayCodigoEntidad() : "(datos = null)") + " para el expediente " + (datos != null ? datos.getTrayNumExpediente() : "(datos = null)"), ex);
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    
    public boolean eliminarDatosColecTrayectoriaEntidad(ColecTrayectoriaEntidad datos, Connection con) throws BDException, Exception {
        log.debug("eliminarDatosColecTrayectoriaEntidad - Begin() - DAO ");
        Statement st = null;
        try {
            String query = null;
            if(datos!= null && datos.getId() != null) {
                query = "delete from COLEC_TRAYECTORIA_ENTIDAD "
                        + " where ID =" + datos.getId();
                log.info("sql = " + query);
                st = con.createStatement();
                return st.executeUpdate(query) > 0;
            }else{
                log.info("Objeto recibido  a null para Eliminar ...");
                return false;
            }
        } catch (Exception ex) {
            log.error("Se ha producido Exception eliminando los datos trayactoria predefinida" + (datos != null ? datos.getTrayCodigoEntidad() : "(datos = null)") + " para el expediente " + (datos != null ? datos.getTrayNumExpediente() : "(datos = null)"), ex);
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }

    public ColecTrayectoriaEntidad getColecTrayectoriaEntidadXid(Integer identificadorBDGestionar, Connection con) throws Exception {
        log.info(" getColecTrayectoriaEntidadXid - Begin " + identificadorBDGestionar + " " + formatFechaLog.format(new Date()));
        ColecTrayectoriaEntidad resultado = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String query = "select * "
                    + " from colec_trayectoria_entidad "
                    + " where "
                    + " id=? "
                    ;
            log.info("sql = " + query);
            ps = con.prepareStatement(query);
            ps.setInt(1, identificadorBDGestionar);
            log.info("params = " + identificadorBDGestionar);
            rs = ps.executeQuery();
            if(rs.next()) {
                resultado=(ColecTrayectoriaEntidad) MeLanbide48MappingUtils.getInstance().map(rs, ColecTrayectoriaEntidad.class);
            }
        } catch (SQLException e) {
            log.error("Se ha producido SQLException  getColecTrayectoriaEntidadXid ", e);
            throw e;
        } catch (Exception e) {
            log.error("Se ha producido  Exception getColecTrayectoriaEntidadXid ", e);
            throw e;
        } finally {
            log.debug("Procedemos a cerrar el resultset");
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        log.info(" getColecTrayectoriaEntidadXid - End " + identificadorBDGestionar + " " + formatFechaLog.format(new Date()));
        return resultado;
    }

    /**
     * Guarda los datos de Meses validados por el tramitador desde la pantalla Resmen Trayectoria.
     * @param numeroTotalMesesValidados
     * @param numeroExpediente
     * @param con
     * @return
     * @throws Exception 
     */
    public boolean guardarValidarTotalMesesResumen(String numeroExpediente,Double numeroTotalMesesValidados,Connection con) throws Exception {
        Statement st = null;
        try {
            String query = "update colec_entidad "
                        + " set "
                        + " TRAYNUMEROMESES_VALIDADOS = " + (numeroTotalMesesValidados != null ? numeroTotalMesesValidados : "null")
                        + " where COLEC_NUMEXP='" + (numeroExpediente != null ? numeroExpediente : "null")+"'"
                        ;
            log.info("sql = " + query);
            st = con.createStatement();
            int res = st.executeUpdate(query);
            return res > 0;
        } catch (Exception ex) {
            log.error("Se ha producido un error guardando los datos total Meses Validados " + numeroExpediente, ex);
            throw new Exception(ex);
        } finally {
            try {
                log.debug("Procedemos a cerrar el statement y el resultset");
                if (st != null)st.close();
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    
    /**
     * Recoge la lista de toda la trayectoria, decartando las que no se marcan si tiene experiencia (caso de predefinidas) y fechas invalidas(inicio>fin)
     * @param idioma
     * @param numExpediente
     * @param con
     * @return
     * @throws SQLException
     * @throws Exception 
     */
    public List<ColecTrayectoriaEntidad> getTodaTrayectoriaAcreditableExpedienteNoSolapable(int idioma,String numExpediente, Connection con) throws SQLException, Exception {
        log.info(" getTodaTrayectoriaAcreditableExpedienteNoSolapable - Begin " + numExpediente +" "+ formatFechaLog.format(new Date()));
        List<ColecTrayectoriaEntidad> resultado = new ArrayList<ColecTrayectoriaEntidad>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String query = "SELECT distinct TRAYNUMEXPEDIENTE "
                    + " ,cte.traycodcolectivo,case when 4=? then cc.descripcioneu ELSE cc.descripcion END colectivoDescripcion "
                    + " ,case when cte.trayidfkprogconvactgrupo=2 then (case when 4=? then CPCG.DECRIPCIONEU ELSE CPCG.DESCRIPCION END) else cte.TRAYDESCRIPCION end TRAYDESCRIPCION "
                    + " ,case when cte.trayidfkprogconvactgrupo=2 then CPCG.fechainicio else cte.TRAYFECHAINICIO end TRAYFECHAINICIO "
                    + " ,case when cte.trayidfkprogconvactgrupo=2 then CPCG.fechafin else cte.TRAYFECHAFIN end TRAYFECHAFIN "
                    + " FROM colec_trayectoria_entidad cte"
                    + " LEFT JOIN colec_prog_conv_act_predef CPCG ON  CPCG.CODIGOGRUPO=cte.trayidfkprogconvactgrupo and cpcg.id=cte.trayidfkprogconvactsubgrpre "
                    + " LEFT JOIN colec_colectivo CC ON CC.ID=CTE.traycodcolectivo "
                    + " WHERE cte.traynumexpediente=? "
                    + " and (case when (cte.trayidfkprogconvactgrupo=2 and cte.TRAYTIENEEXPERIENCIA=1) or (cte.trayidfkprogconvactgrupo!=2) then 1 else 0 end)=1 "
                    + " and ((case when cte.trayidfkprogconvactgrupo=2 then CPCG.fechafin else cte.TRAYFECHAFIN end)>=(case when cte.trayidfkprogconvactgrupo=2 then CPCG.fechainicio else cte.TRAYFECHAINICIO end)) "
                    + " and ((case when cte.trayidfkprogconvactgrupo=2 then CPCG.fechafin else cte.TRAYFECHAFIN end)>=to_date('01/01/2016','dd/MM/yyyy')) "
                    + " ORDER BY  cte.traycodcolectivo,to_date(to_char(TRAYFECHAINICIO,'dd/MM/yyy'),'dd/MM/yyyy') ASC, to_date(to_char(TRAYFECHAFIN,'dd/MM/yyy'),'dd/MM/yyyy') ASC "
                    ;
            log.info("sql = " + query);
            ps = con.prepareStatement(query);
            int i = 1;
            ps.setInt(i++, idioma);
            ps.setInt(i++, idioma);
            ps.setString(i++, numExpediente);
            log.info("params (" + (i - 1) + ")= " +idioma +" "+idioma +" "+ numExpediente
            );
            rs = ps.executeQuery();
            while (rs.next()) {
                ColecTrayectoriaEntidad colecTrayectoriaEntidad = new ColecTrayectoriaEntidad();
                // Si es el primer registro o si cambia de colectivo desde la ultima fila leida, en la query ya viebe ordenado por colectivo
                if (resultado.isEmpty()
                        || rs.getInt("traycodcolectivo")!=resultado.get(resultado.size() - 1).getTrayCodColectivo()) {
                    colecTrayectoriaEntidad.setTrayNumExpediente(rs.getString("TRAYNUMEXPEDIENTE"));
                    colecTrayectoriaEntidad.setTrayCodColectivo(rs.getInt("traycodcolectivo"));
                    colecTrayectoriaEntidad.setColectivoDescripcion(rs.getString("colectivoDescripcion"));
                    colecTrayectoriaEntidad.setTrayDescripcion(rs.getString("TRAYDESCRIPCION"));
                    colecTrayectoriaEntidad.setTrayFechaInicio(rs.getDate("TRAYFECHAINICIO"));
                    colecTrayectoriaEntidad.setTrayFechaFin(rs.getDate("TRAYFECHAFIN"));
                    Double meses = meLanbide48Utils.calcularMesesAsDouble(rs.getDate("TRAYFECHAINICIO"), rs.getDate("TRAYFECHAFIN"));
                    colecTrayectoriaEntidad.setTrayNumeroMeses((meses >= 0 ? meses : 0));
                    resultado.add(colecTrayectoriaEntidad);
                } else {
                    colecTrayectoriaEntidad = resultado.get(resultado.size() - 1);
                    Date fechaInicioTemp = rs.getDate("TRAYFECHAINICIO");
                    Date fechaFinTemp = rs.getDate("TRAYFECHAFIN");
                    if (fechaFinTemp != null && fechaInicioTemp != null) {
                        if (fechaInicioTemp.before(colecTrayectoriaEntidad.getTrayFechaFin())) {
                            Calendar calTemp = Calendar.getInstance();
                            calTemp.setTime(colecTrayectoriaEntidad.getTrayFechaFin());
                            calTemp.add(Calendar.DATE, 1);
                            Date fechaInicioTempPlus1 = calTemp.getTime();
                            log.info("Evitamos solapamiento con fechas : " + numExpediente + " Original " + formatFechaddMMyyyy.format(fechaInicioTemp) + " Reemplazamos " + formatFechaddMMyyyy.format(fechaInicioTempPlus1));
                            fechaInicioTemp = fechaInicioTempPlus1;
                        }
                        if (fechaFinTemp.after(colecTrayectoriaEntidad.getTrayFechaFin())) {
                            ColecTrayectoriaEntidad colecTrayectoriaEntidad2 = new ColecTrayectoriaEntidad();
                            colecTrayectoriaEntidad2.setTrayNumExpediente(rs.getString("TRAYNUMEXPEDIENTE"));
                            colecTrayectoriaEntidad2.setTrayCodColectivo(rs.getInt("traycodcolectivo"));
                            colecTrayectoriaEntidad2.setColectivoDescripcion(rs.getString("colectivoDescripcion"));
                            colecTrayectoriaEntidad2.setTrayDescripcion(rs.getString("TRAYDESCRIPCION"));
                            colecTrayectoriaEntidad2.setTrayFechaInicio(new java.sql.Date(fechaInicioTemp.getTime()));
                            colecTrayectoriaEntidad2.setTrayFechaFin(new java.sql.Date(fechaFinTemp.getTime()));
                            Double meses = meLanbide48Utils.calcularMesesAsDouble(colecTrayectoriaEntidad2.getTrayFechaInicio(), colecTrayectoriaEntidad2.getTrayFechaFin());
                            colecTrayectoriaEntidad2.setTrayNumeroMeses((meses >= 0 ? meses : 0));
                            resultado.add(colecTrayectoriaEntidad2);
                        } else {
                            log.info("No cargamos fecha porque esta comprendida dentro de una que se registro previamente. " + formatFechaddMMyyyy.format(fechaInicioTemp) + "  " + formatFechaddMMyyyy.format(fechaFinTemp));
                        }
                    } else {
                        log.info("Una de las de las fechas a calcular viene a null... No calculamos");
                    }
                }
            }
        } catch (SQLException e) {
            log.error("Se ha producido  getTodaTrayectoriaAcreditableExpedienteNoSolapable ", e);
            throw e;
        } catch (Exception e) {
            log.error("Se ha producido getTodaTrayectoriaAcreditableExpedienteNoSolapable ", e);
            throw e;
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("Procedemos a cerrar el resultset");
            }
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        log.info(" getTodaTrayectoriaAcreditableExpedienteNoSolapable - End " + numExpediente + " " + formatFechaLog.format(new Date()));
        return resultado;
    }

    public Double getNumeroTotalMesesValidadosExpediente(String numExpediente, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        Double resultado = null;
        try {
            String query = "SELECT  TRAYNUMEROMESES_VALIDADOS "
                    + " FROM colec_entidad "
                    + " where COLEC_NUMEXP='" + (numExpediente != null ? numExpediente : "null") + "'";
            log.info("sql = " + query);
            st=con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                if(rs.getString("TRAYNUMEROMESES_VALIDADOS")!=null)
                    resultado=rs.getDouble("TRAYNUMEROMESES_VALIDADOS");
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error LEYENDO total Meses Validados " + numExpediente, ex);
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return resultado;
    }
    
    public ColecProcesoAdjudicacionRespuestaVO lanzarProcesoAdjudicacion(Integer idBdConvocatoria,Integer codIdioma, Connection con) throws Exception {
        log.info(" lanzarProcesoAdjudicacion - Begin " + idBdConvocatoria + " " + formatFechaLog.format(new Date()));
        CallableStatement st = null;
        ResultSet rs = null;
        ColecProcesoAdjudicacionRespuestaVO respuesta = null;
        try {
            String query = null;
            query = "call colec_proceso_adjudicacion(?,?,?)";
            log.info("sql = " + query);
            st = con.prepareCall(query);
            st.setInt("idbdconvocatoria", idBdConvocatoria);
            st.registerOutParameter("mensaje", java.sql.Types.VARCHAR);
            st.registerOutParameter("exp_estado_incorrecto", java.sql.Types.NUMERIC);
            st.executeUpdate();
            String mensaje = st.getString("mensaje");
            Integer exp_estado_incorrecto = st.getInt("exp_estado_incorrecto");
            List <String> exp_estado_incorrecto_Lista = (exp_estado_incorrecto>0?this.getExpedientesEstadoIncorrectoProcesoAdjudicacion(idBdConvocatoria,con):new ArrayList<String>());
            respuesta=new ColecProcesoAdjudicacionRespuestaVO(mensaje, MeLanbide48I18n.getInstance().getMensaje(codIdioma,"procesos.adjudicacion.ejecucion.correcta"), exp_estado_incorrecto_Lista);
        } catch (Exception ex) {
            log.error("Se ha producido un error ejecutando procedure en BD - Adjudicacion COLEC. ", ex);
            respuesta = new ColecProcesoAdjudicacionRespuestaVO("ERROR",ex.getMessage() + " - " + ex.getLocalizedMessage());
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception ex) {
                log.error("Se ha producido un error cerrando el statement y el resulset", ex);
                throw ex;
            }
        }
        return respuesta;
    }

    private List<String> getExpedientesEstadoIncorrectoProcesoAdjudicacion(Integer idBdConvocatoria, Connection con) throws SQLException, Exception {
        log.info(" getExpedientesEstadoIncorrectoProcesoAdjudicacion - Begin " + idBdConvocatoria + " " + formatFechaLog.format(new Date()));
        List<String> resultado = new ArrayList<String>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String query = "SELECT "
                    + "DISTINCT EE.EXP_NUM,EE.EXP_EST "
                    + ",CASE when etf.tfe_Valor is not null then etf.tfe_valor else ee.exp_fei end fechaReferenciaExp "
                    + "FROM colec_ubicaciones_ct B "
                    + "INNER JOIN E_EXP ee on ee.exp_pro='COLEC' AND ee.exp_num=b.colec_ubic_ct_numexp "
                    + "LEFT JOIN E_TFE etf ON etf.tfe_eje=ee.exp_eje and etf.tfe_num=ee.exp_num and etf.tfe_cod='FECHAPRESENTACION' "
                    + "inner join melanbide_convocatorias mc on mc.pro_cod=ee.exp_pro and   mc.id=?"
                    + "INNER JOIN E_CRO C ON C.CRO_NUM=b.colec_ubic_ct_numexp "
                    + "LEFT JOIN E_tra et on et.tra_pro=c.cro_pro and et.tra_cod=c.cro_tra "
                    + "WHERE  ee.exp_est!=1 "  //-- Quitar expedientes anulados 
                    + "AND (mc.pro_cod=ee.exp_pro and decretoFecEntradaVigor <= (CASE when etf.tfe_Valor is not null then etf.tfe_valor else ee.exp_fei end) and (nvl(decretoFecFinAplicacion,sysdate+1)) >  (CASE when etf.tfe_Valor is not null then etf.tfe_valor else ee.exp_fei end)) "
                    + "and c.CRO_FEF is null "
                    + "AND et.tra_cou not in(3,12,999)  "  //-- *Revisar* RESOLUCION PROVISIONAL (3), RESOLUCION PROVISIONAL NEGATIVA (12), CIERRE EXPEDIENTE (999). CODIGOS VISIBLES 
                    + "order by ee.exp_num "
            ;
            
            
            int params = 1;
            log.info("sql = " + query);
            ps = con.prepareStatement(query);
            ps.setInt(params++, idBdConvocatoria);
            log.info("params =("+(params-1)+"): "+ idBdConvocatoria);
            rs = ps.executeQuery();
            while (rs.next()) {
                resultado.add(rs.getString("EXP_NUM"));
            }
        } catch (SQLException e) {
            log.error("Se ha producido recuperando datos Expediente estado Incorrecto ", e);
            throw e;
        } catch (Exception e) {
            log.error("Se ha producido recuperando datos Expediente estado Incorrecto ", e);
            throw e;
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("Procedemos a cerrar el resultset");
            }
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        log.info(" getExpedientesEstadoIncorrectoProcesoAdjudicacion - End " + idBdConvocatoria + " " + formatFechaLog.format(new Date()));
        return resultado;
    }
    
}