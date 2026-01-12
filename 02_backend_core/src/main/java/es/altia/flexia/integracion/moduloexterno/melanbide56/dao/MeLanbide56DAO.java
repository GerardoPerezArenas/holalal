/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide56.dao;

import es.altia.flexia.integracion.moduloexterno.melanbide56.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide56.util.ConstantesMeLanbide56;
import es.altia.flexia.integracion.moduloexterno.melanbide56.util.MeLanbide56MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide56.vo.CampoSuplementario;
import es.altia.flexia.integracion.moduloexterno.melanbide56.vo.Domicilio;
import es.altia.flexia.integracion.moduloexterno.melanbide56.vo.Expediente;
import es.altia.flexia.integracion.moduloexterno.melanbide56.vo.Tercero;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author santiagoc
 */
public class MeLanbide56DAO
{
    
    //Logger
    private static final Logger log = LogManager.getLogger(MeLanbide56DAO.class);
    
    //Instancia
    private static MeLanbide56DAO instance = null;
    
    private MeLanbide56DAO()
    {
        
    }
    
    public static MeLanbide56DAO getInstance()
    {
        if(instance == null)
        {
            synchronized(MeLanbide56DAO.class)
            {
                instance = new MeLanbide56DAO();
            }
        }
        return instance;
    }  
    
    public boolean grabarCampoSuplementarioTramite(int codOrganizacion, String codProcedimiento, Integer ejercicio, String numExp, int codTramite, int ocurrenciaTramite, String codCampo, Object valor, int tipoDato, Connection con) throws Exception
    {
        boolean result = false;
        switch(tipoDato)
        {
            case ConstantesMeLanbide56.TIPOS_DATOS_SUPLEMENTARIOS.NUMERICO:
                result = grabarCampoSuplementarioNumericoTramite(codOrganizacion, codProcedimiento, ejercicio, numExp, codTramite, ocurrenciaTramite, codCampo, (Integer)valor, con);
                break;
            case ConstantesMeLanbide56.TIPOS_DATOS_SUPLEMENTARIOS.FECHA:
                result = grabarCampoSuplementarioFechaTramite(codOrganizacion, codProcedimiento, ejercicio, numExp, codTramite, ocurrenciaTramite, codCampo, valor.toString(), con);
                break;
        }
        return result;
    }
    
    public boolean grabarCamposSuplementariosTramite(int codOrganizacion, String codProcedimiento, Integer ejercicio, String numExp, int codTramite, int ocurrenciaTramite, List<CampoSuplementario> camposSuplementarios, Connection con) throws Exception
    {
        boolean result = false;
        if(camposSuplementarios.size() > 0)
        {
            int tipoDato;
            for(CampoSuplementario campo : camposSuplementarios)
            {
                tipoDato = campo.getTipoDato();
                switch(tipoDato)
                {
                    case ConstantesMeLanbide56.TIPOS_DATOS_SUPLEMENTARIOS.NUMERICO:
                        result = grabarCampoSuplementarioNumericoTramite(codOrganizacion, codProcedimiento, ejercicio, numExp, codTramite, ocurrenciaTramite, campo.getCodCampo(), (Integer)campo.getValor(), con);
                        break;
                    case ConstantesMeLanbide56.TIPOS_DATOS_SUPLEMENTARIOS.FECHA:
                        result = grabarCampoSuplementarioFechaTramite(codOrganizacion, codProcedimiento, ejercicio, numExp, codTramite, ocurrenciaTramite, campo.getCodCampo(), campo.getValor().toString(), con);
                        break;
                }
            }
        }
        return result;
    }
    
    private boolean grabarCampoSuplementarioNumericoTramite(int codOrganizacion, String codProcedimiento, Integer ejercicio, String numExp, int codTramite, int ocurrenciaTramite, String codCampo, Integer valor, Connection con) throws Exception
    {
        PreparedStatement st = null;
        ResultSet rs = null;
        String query = "";
        try
        {
            String sql = "select * from "+ConfigurationParameter.getParameter(ConstantesMeLanbide56.TABLA_E_TNU, ConstantesMeLanbide56.FICHERO_PROPIEDADES)
                    +" where TNU_COD = '" + codCampo+"' AND TNU_NUM = '"+numExp+"'";
            st = con.prepareStatement(sql);
            rs = st.executeQuery();
            if(rs.next()){
                query = "UPDATE "+ConfigurationParameter.getParameter(ConstantesMeLanbide56.TABLA_E_TNU, ConstantesMeLanbide56.FICHERO_PROPIEDADES)
                        + " SET TNU_VALOR = "+valor+""
                        + " WHERE TNU_NUM = '"+numExp+"' AND TNU_COD = '" + codCampo+"'";
            }
            else
            {
                query = "insert into "+ConfigurationParameter.getParameter(ConstantesMeLanbide56.TABLA_E_TNU, ConstantesMeLanbide56.FICHERO_PROPIEDADES)
                          +" (TNU_MUN, TNU_EJE, TNU_NUM, TNU_COD, TNU_VALOR)"
                          +" values ("+codOrganizacion + ", "+ejercicio+", '"+numExp+"', '" + codCampo+"',"+valor+")";
            }
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.prepareStatement(query);

//            st = con.prepareStatement(query);
//            st.setInt(1, codOrganizacion);
//            st.setString(2, codProcedimiento);
//            st.setInt(3, ejercicio);
//            st.setString(4, numExp);
//            st.setInt(5, codTramite);
//            st.setInt(6, ocurrenciaTramite);
//            st.setString(7, codCampo);
//            st.setInt(8, valor);

            int res = st.executeUpdate();
            return res > 0;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error guardando campo suplementario "+codCampo+" para expediente "+numExp);
            throw new Exception(ex);
        }
        finally
        {
            if(log.isDebugEnabled()) 
                log.debug("Procedemos a cerrar el statement y el resultset");
            if(st!=null) 
                st.close();
        }
    }
    
    private boolean grabarCampoSuplementarioFechaTramite(int codOrganizacion, String codProcedimiento, Integer ejercicio, String numExp, int codTramite, int ocurrenciaTramite, String codCampo, String valor, Connection con) throws Exception
    {
        PreparedStatement st = null;
        String query = "";
        ResultSet rs = null;
        try
        {
            String sql = "select * from "+ConfigurationParameter.getParameter(ConstantesMeLanbide56.TABLA_E_TFE, ConstantesMeLanbide56.FICHERO_PROPIEDADES)
                    +" where TFE_COD = '" + codCampo+"' AND TFE_NUM = '"+numExp+"'";
            st = con.prepareStatement(sql);
            rs = st.executeQuery();
            if(rs.next()){
                query = "UPDATE "+ConfigurationParameter.getParameter(ConstantesMeLanbide56.TABLA_E_TFE, ConstantesMeLanbide56.FICHERO_PROPIEDADES)
                        + " SET TFE_VALOR = '"+valor+"'"
                        + " WHERE TFE_NUM = '"+numExp+"' AND TFE_COD = '" + codCampo+"'";
            }
            else
            {
                query = "insert into "+ConfigurationParameter.getParameter(ConstantesMeLanbide56.TABLA_E_TFE, ConstantesMeLanbide56.FICHERO_PROPIEDADES)
                          +" (TFE_MUN, TFE_EJE, TFE_NUM, TFE_COD, TFE_VALOR)"
                          +" values ("+codOrganizacion + ", "+ejercicio+", '"+numExp+"', '" + codCampo+"','"+valor+"')";

            }
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.prepareStatement(query);
            
            int res = st.executeUpdate();
            return res > 0;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error guardando campo suplementario "+codCampo+" para expediente "+numExp);
            throw new Exception(ex);
        }
        finally
        {
            if(log.isDebugEnabled()) 
                log.debug("Procedemos a cerrar el statement y el resultset");
            if(st!=null) 
                st.close();
        }
    }
    
    public Expediente getDatosExpediente(int codMunicipio, String numExp, Connection con) throws Exception
    {
        PreparedStatement st = null;
        ResultSet rs = null;
        Expediente exp = null;
        
        try
        {
            /*String query = "select EXP_PRO, EXP_EJE, EXP_NUM, EXP_MUN, EXP_UOR, EXP_OBS, EXP_ASU"
                          +" from "+ConfigurationParameter.getParameter(ConstantesMeLanbide56.TABLA_E_EXP, ConstantesMeLanbide56.FICHERO_PROPIEDADES)
                          +" where EXP_MUN = ? and EXP_NUM = ?";*/
            
            
            String query = "select exp.EXP_PRO PRO, exp.EXP_EJE EJE, exp.EXP_NUM NUM, exp.EXP_MUN MUN, exp.EXP_UOR UOR, exp.EXP_OBS OBS, exp.EXP_ASU ASU,"
                          +" exr.EXR_DEP DEP,"
                          +" res.RES_TDO TDO, res.RES_NTR NTR, res.RES_AUT AUT"
                          +" from "+ConfigurationParameter.getParameter(ConstantesMeLanbide56.TABLA_E_EXP, ConstantesMeLanbide56.FICHERO_PROPIEDADES)+" exp"
                          +" left join "+ConfigurationParameter.getParameter(ConstantesMeLanbide56.TABLA_E_EXR, ConstantesMeLanbide56.FICHERO_PROPIEDADES)+" exr"
                          +" on exp.exp_pro = exr.exr_pro and exp.exp_eje = exr.exr_eje and exp.exp_num = exr.exr_num  and exp.exp_mun = exr.exr_mun"
                          +" left join "+ConfigurationParameter.getParameter(ConstantesMeLanbide56.TABLA_R_RES, ConstantesMeLanbide56.FICHERO_PROPIEDADES)+" res"
                          +" on exr.exr_nre = res.res_num and exr.exr_eje = res.res_eje"
                          +" where exp.EXP_MUN = ? and exp.EXP_NUM = ?";
            st = con.prepareStatement(query);
            st.setInt(1, codMunicipio);
            st.setString(2, numExp);
            log.error("sql: " + query);
            rs = st.executeQuery();
            if(rs.next())
            {
                exp = (Expediente)MeLanbide56MappingUtils.getInstance().map(rs, Expediente.class);
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando datos del expediente "+numExp, ex);
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
        return exp;
    }
    public List<Tercero> getTercerosExpediente(int codMunicipio, Integer ejercicio, String numExp, Connection con) throws Exception
    {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Tercero> terceros = new ArrayList<Tercero>();
        try
        {
            String query = null;
            int historico = 0;
            String codTercero = null;
            String version = null;
            Tercero tercero = null;
            Domicilio domicilio = null;
            /*query = "select ext_rol, ter_doc from t_ter inner join e_ext on ter_cod = ext_ter"
                    +" where ext_num = '"+numExp+"' and ext_rol in (";*/
            
            query = "select t.ter_cod, t.ter_doc, t.ter_nve, h.hte_ter, h.hte_doc, h.hte_nvr,"
                   +" case when t.ter_cod is not null and t.ter_doc is not null then 0 else 1 end as HISTORICO"
                   +" from "+ConfigurationParameter.getParameter(ConstantesMeLanbide56.TABLA_REL_TERCERO_DOMICILIO, ConstantesMeLanbide56.FICHERO_PROPIEDADES)+" rel"
                   +" left join "+ConfigurationParameter.getParameter(ConstantesMeLanbide56.TABLA_TERCEROS, ConstantesMeLanbide56.FICHERO_PROPIEDADES)+" t on t.ter_cod = rel.ext_ter and t.ter_nve = rel.ext_nvr"
                   +" left join "+ConfigurationParameter.getParameter(ConstantesMeLanbide56.TABLA_TERCEROS_HIST, ConstantesMeLanbide56.FICHERO_PROPIEDADES)+" h on h.hte_ter = rel.ext_ter and h.hte_nvr = rel.ext_nvr"
                   +" where ext_rol = 1 and ext_mun = ? and ext_eje = ? and ext_num = ?";
            
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.prepareStatement(query);
            st.setInt(1, codMunicipio);
            st.setInt(2, ejercicio);
            st.setString(3, numExp);
            rs = st.executeQuery();
            while(rs.next())
            {
                
                historico = rs.getInt("HISTORICO");
                switch(historico)
                {
                    case 0:
                        codTercero = rs.getString("TER_COD");
                        version = rs.getString("TER_NVE");
                        tercero = this.getTercero(codMunicipio, ejercicio, numExp, codTercero, version, con);
                        break;
                    case 1:
                        codTercero = rs.getString("HTE_TER");
                        version = rs.getString("HTE_NVR");
                        tercero = this.getTerceroHistorico(codMunicipio, ejercicio, numExp, codTercero, version, con);
                        break;
                    default:
                        tercero = null;
                }
                if(tercero != null)
                {
                    try
                    {
                        domicilio = this.getDomicilioTercero(tercero, con);
                        tercero.setDomicilio(domicilio);
                    }
                    catch(Exception ex)
                    {
                        log.error("Se ha producido un error al recuperar el domicilio del tercero "+tercero.getTerDoc()+" para el expediente "+numExp, ex);
                    }
                    terceros.add(tercero);
                }
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando la lista de terceros para el expediente "+numExp, ex);
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
        return terceros;
    }
        
    private Tercero getTercero(int codMunicipio, Integer ejercicio, String numExpediente, String codTercero, String version, Connection con) throws Exception
    {
        PreparedStatement st = null;
        ResultSet rs = null;
        Tercero ter = null;
        try
        {
            String query = null;
            query = ""
                    + ""
                    + "select"
                    +" TER_COD COD, TER_TID TID, TER_DOC DOC, TER_NOM NOM, TER_AP1 AP1, TER_PA1 PA1, TER_AP2 AP2,"
                    + "TER_PA2 PA2, TER_NOC NOC, TER_NML NML, TER_TLF TLF, TER_DCE DCE, TER_SIT SIT, TER_NVE VER,"
                    + "TER_FAL FAL, TER_UAL UAL, TER_APL APL, TER_FBJ FBJ, TER_UBJ UBJ, TER_DOM DOM, EXTERNAL_CODE EXTERNAL_CODE, EXT_ROL ROL"
                    +" from "+ ConfigurationParameter.getParameter(ConstantesMeLanbide56.TABLA_TERCEROS, ConstantesMeLanbide56.FICHERO_PROPIEDADES)+" t"
                    +" inner join "+ ConfigurationParameter.getParameter(ConstantesMeLanbide56.TABLA_REL_TERCERO_DOMICILIO, ConstantesMeLanbide56.FICHERO_PROPIEDADES)+" r"
                    +" on t.ter_cod = r.ext_ter and t.ter_nve = r.ext_nvr"
                    +" where r.ext_mun = ? and r.ext_eje = ? and r.ext_num = ? and r.ext_ter = ? and r.ext_nvr = ?";
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.prepareStatement(query);
            st.setInt(1, codMunicipio);
            st.setInt(2, ejercicio);
            st.setString(3, numExpediente);
            st.setString(4, codTercero);
            st.setString(5, version);
            rs = st.executeQuery();
            if(rs.next())
            {
                ter = (Tercero)MeLanbide56MappingUtils.getInstance().map(rs, Tercero.class);
            }
            return ter;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando tercero "+codTercero, ex);
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
    }
        
    private Tercero getTerceroHistorico(int codMunicipio, Integer ejercicio, String numExpediente, String codTercero, String version, Connection con) throws Exception
    {
        PreparedStatement st = null;
        ResultSet rs = null;
        Tercero ter = null;
        try
        {
            String query = null;
            query = "select "
                    +" HTE_TER COD, HTE_NVR VER, HTE_TID TID, HTE_DOC DOC, HTE_NOM NOM,"
                    +" HTE_AP1 AP1, HTE_PA1 PA1, HTE_AP2 AP2, HTE_PA2 PA2, HTE_NOC NOC, HTE_NML NML,"
                    +" HTE_TLF TLF, HTE_DCE DCE, HTE_APL APL, EXTERNAL_CODE EXTERNAL_CODE,"
                    +" null SIT, null FAL, null UAL, null FBJ, null UBJ, null DOM, EXT_ROL ROL"
                    +" from "+ ConfigurationParameter.getParameter(ConstantesMeLanbide56.TABLA_TERCEROS_HIST, ConstantesMeLanbide56.FICHERO_PROPIEDADES)+" h"
                    +" inner join "+ ConfigurationParameter.getParameter(ConstantesMeLanbide56.TABLA_REL_TERCERO_DOMICILIO, ConstantesMeLanbide56.FICHERO_PROPIEDADES)+" r"
                    +" on h.hte_ter = r.ext_ter and h.hte_nvr = r.ext_nvr"
                    +" where r.ext_mun = ? and r.ext_eje = ? and r.ext_num = ? and r.ext_ter = ? and r.ext_nvr = ?";
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.prepareStatement(query);
            st.setInt(1, codMunicipio);
            st.setInt(2, ejercicio);
            st.setString(3, numExpediente);
            st.setString(4, codTercero); log.error("codTercero: " + codTercero);
            st.setString(5, version);log.error("version: " + version);
            rs = st.executeQuery();
            if(rs.next())
            {
                ter = (Tercero)MeLanbide56MappingUtils.getInstance().map(rs, Tercero.class);
            }
            return ter;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando tercero "+codTercero, ex);
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
    }
    
    private Domicilio getDomicilioTercero(Tercero ter, Connection con) throws Exception
    {
        if(ter != null && ter.getTerCod() != null)
        {
            PreparedStatement st = null;
            ResultSet rs = null;
            Domicilio dom = null;
            try
            {
                String query = null;
                query = "select dom.*, via.VIA_NOM from "+ConfigurationParameter.getParameter(ConstantesMeLanbide56.TABLA_DOMICILIO, ConstantesMeLanbide56.FICHERO_PROPIEDADES)
                        +" dom inner join "+ConfigurationParameter.getParameter(ConstantesMeLanbide56.TABLA_T_DOT, ConstantesMeLanbide56.FICHERO_PROPIEDADES)
                        +" dot on dom.DNN_DOM = dot.DOT_DOM"
                        +" inner join "+ConfigurationParameter.getParameter(ConstantesMeLanbide56.TABLA_T_VIA, ConstantesMeLanbide56.FICHERO_PROPIEDADES)
                        +" via on dom.DNN_VIA = via.VIA_COD"
                        +" where dot.DOT_TER = ?";
                
                if(log.isDebugEnabled()) 
                    log.debug("sql = " + query);
                st = con.prepareStatement(query);
                st.setLong(1, ter.getTerCod());
                log.debug("codigo tercero: " + ter.getTerCod());
                rs = st.executeQuery();
                if(rs.next())
                {
                    dom = (Domicilio)MeLanbide56MappingUtils.getInstance().map(rs, Domicilio.class);
                }
                return dom;
            }
            catch(Exception ex)
            {
                log.error("Se ha producido un error recuperando domicilio tercero "+ter.getTerCod(), ex);
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
        }
        else
        {
            return null;
        }
    }
}
