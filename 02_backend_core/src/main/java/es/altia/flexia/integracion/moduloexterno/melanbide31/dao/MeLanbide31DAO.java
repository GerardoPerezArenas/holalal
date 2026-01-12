/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide31.dao;

import es.altia.agora.business.util.GlobalNames;
import es.altia.flexia.integracion.moduloexterno.melanbide31.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide31.util.ConstantesMeLanbide31;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author santiagoc
 */
public class MeLanbide31DAO 
{
    
    
    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide31DAO.class);
    
    //Instancia
    private static MeLanbide31DAO instance = null;
    
    private MeLanbide31DAO()
    {
        
    }
    
    public static MeLanbide31DAO getInstance()
    {
        if(instance == null)
        {
            synchronized(MeLanbide31DAO.class)
            {
                instance = new MeLanbide31DAO();
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
            query = "select TNU_VALOR from " + ConfigurationParameter.getParameter(ConstantesMeLanbide31.TABLA_TIPO_DATO_1, ConstantesMeLanbide31.FICHERO_PROPIEDADES)
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
    
    public String getValorCampoTexto(int codOrganizacion, String numExp, String ejercicio, String codigoCampo, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        String valor = null;
        try
        {
            String query = null;
            query = "select TXT_VALOR from " + ConfigurationParameter.getParameter(ConstantesMeLanbide31.TABLA_TIPO_DATO_2, ConstantesMeLanbide31.FICHERO_PROPIEDADES)
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
    
    public Date getValorCampoFecha(int codOrganizacion, String numExp, String ejercicio, String codigoCampo, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        Date valor = null;
        try
        {
            String query = null;
            query = "select TFE_VALOR from " + ConfigurationParameter.getParameter(ConstantesMeLanbide31.TABLA_TIPO_DATO_3, ConstantesMeLanbide31.FICHERO_PROPIEDADES)
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
    
    
    public String getValorCampoTextoLargo(int codOrganizacion, String numExp, String ejercicio, String codigoCampo, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        String valor = null;
        try
        {
            String query = null;
            query = "select TTL_VALOR from " + ConfigurationParameter.getParameter(ConstantesMeLanbide31.TABLA_TIPO_DATO_4, ConstantesMeLanbide31.FICHERO_PROPIEDADES)
                    + " where TTL_MUN = '" + codOrganizacion + "' and TTL_EJE = '" + ejercicio 
                    + "' and TTL_NUM = '"+numExp+"' and TTL_COD = '"+codigoCampo+"'";
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
                valor = rs.getString("TTL_VALOR");
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
            query = "select TDE_VALOR from " + ConfigurationParameter.getParameter(ConstantesMeLanbide31.TABLA_TIPO_DATO_6, ConstantesMeLanbide31.FICHERO_PROPIEDADES)
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
    
    public String getProvinciaTercero(int codOrganizacion, String numExp, String ejercicio, Connection con)throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        String valor = null;
        try
        {
            String query = null;
            query = "select DNN_PRV from T_DNN inner join E_EXT on DNN_DOM = EXT_DOT where EXT_MUN = '"+codOrganizacion+"' and EXT_EJE = '"+ejercicio+"' and EXT_NUM = '"+numExp+"'";
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
                valor = rs.getString("DNN_PRV");
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
    
    public String getCodPostalTercero(int codOrganizacion, String numExp, String ejercicio, Connection con)throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        String valor = null;
        try
        {
            String query = null;
            query = "select DNN_CPO from T_DNN inner join E_EXT on DNN_DOM = EXT_DOT where EXT_MUN = '"+codOrganizacion+"' and EXT_EJE = '"+ejercicio+"' and EXT_NUM = '"+numExp+"'";
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
                valor = rs.getString("DNN_CPO");
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
    
     public String getDescripcionProvincia(String codigoProv, Connection con)throws Exception
     {
        Statement st = null;
        ResultSet rs = null;
        String valor = null;
        try
        {
            String query = null;
            query = "select PRV_NOM from "+GlobalNames.ESQUEMA_GENERICO+"T_PRV where PRV_COD = '"+codigoProv+"'";
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
                valor = rs.getString("PRV_NOM");
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
     
     public List<String> getValoresCumplimentados(int codOrganizacion, String numExp, String ejercicio, Connection con) throws Exception
     {
         List<String> retList = new ArrayList<String>();
         String query = null;
         Statement st = null;
         ResultSet rs = null;
         String valorTxt = null;
         Date valorFec = null;
         BigDecimal valorNum = null;
         String codigo = null;
         
         //Campos numericos (TIPO 1)
         query = "select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide31.TABLA_TIPO_DATO_1, ConstantesMeLanbide31.FICHERO_PROPIEDADES)
                    + " where TNU_MUN = '" + codOrganizacion + "' and TNU_EJE = '" + ejercicio 
                    + "' and TNU_NUM = '"+numExp+"'";
         st = con.createStatement();
         rs = st.executeQuery(query);
         while(rs.next())
         {
             codigo = rs.getString("TNU_COD");
             valorNum = rs.getBigDecimal("TNU_VALOR");
             if(valorNum != null)
             {
                 retList.add(codigo);
             }
         }
         
         //Campos Texto (TIPO 2)
         query = "select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide31.TABLA_TIPO_DATO_2, ConstantesMeLanbide31.FICHERO_PROPIEDADES)
                    + " where TXT_MUN = '" + codOrganizacion + "' and TXT_EJE = '" + ejercicio 
                    + "' and TXT_NUM = '"+numExp+"'";
         st = con.createStatement();
         rs = st.executeQuery(query);
         while(rs.next())
         {
             codigo = rs.getString("TXT_COD");
             valorTxt = rs.getString("TXT_VALOR");
             if(valorTxt != null && !valorTxt.equals(""))
             {
                 retList.add(codigo);
             }
         }
         
         //Campos Fecha (TIPO 3)
         query = "select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide31.TABLA_TIPO_DATO_3, ConstantesMeLanbide31.FICHERO_PROPIEDADES)
                    + " where TFE_MUN = '" + codOrganizacion + "' and TFE_EJE = '" + ejercicio 
                    + "' and TFE_NUM = '"+numExp+"'";
         st = con.createStatement();
         rs = st.executeQuery(query);
         while(rs.next())
         {
             codigo = rs.getString("TFE_COD");
             valorFec = rs.getDate("TFE_VALOR");
             if(valorFec != null)
             {
                 retList.add(codigo);
             }
         }
         
         //Campos Texto Largo (TIPO 4)
         query = "select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide31.TABLA_TIPO_DATO_4, ConstantesMeLanbide31.FICHERO_PROPIEDADES)
                    + " where TTL_MUN = '" + codOrganizacion + "' and TTL_EJE = '" + ejercicio 
                    + "' and TTL_NUM = '"+numExp+"'";
         st = con.createStatement();
         rs = st.executeQuery(query);
         while(rs.next())
         {
             codigo = rs.getString("TTL_COD");
             valorTxt = rs.getString("TTL_VALOR");
             if(valorTxt != null && !valorTxt.equals(""))
             {
                 retList.add(codigo);
             }
         }
         
         //Campos Desplegable (TIPO 6)
         query = "select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide31.TABLA_TIPO_DATO_6, ConstantesMeLanbide31.FICHERO_PROPIEDADES)
                    + " where TDE_MUN = '" + codOrganizacion + "' and TDE_EJE = '" + ejercicio 
                    + "' and TDE_NUM = '"+numExp+"'";
         st = con.createStatement();
         rs = st.executeQuery(query);
         while(rs.next())
         {
             codigo = rs.getString("TDE_COD");
             valorTxt = rs.getString("TDE_VALOR");
             if(valorTxt != null && !valorTxt.equals(""))
             {
                 retList.add(codigo);
             }
         }
         
         return retList;
     }
     
     public String getDescripcionCampo(int codOrganizacion, String codigoCampo, Connection con) throws Exception
     {
         String descr = "";
         Statement st = null;
         ResultSet rs = null;
         String query = "select PCA_DES from "+ConfigurationParameter.getParameter(ConstantesMeLanbide31.TABLA_CAMPOS_PROC, ConstantesMeLanbide31.FICHERO_PROPIEDADES)
                        + " where PCA_COD = '"+codigoCampo+"' and PCA_MUN = '"+codOrganizacion+"' and PCA_PRO = 'EH'";
         try
         {
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                descr = rs.getString("PCA_DES");
            }
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
         return descr;
     }
}
