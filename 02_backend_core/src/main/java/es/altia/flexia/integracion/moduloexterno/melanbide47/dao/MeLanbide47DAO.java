/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide47.dao;

import es.altia.agora.business.util.GlobalNames;
import es.altia.flexia.integracion.moduloexterno.melanbide47.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide47.util.ConstantesMeLanbide47;
import es.altia.flexia.integracion.moduloexterno.melanbide47.util.MeLanbide47MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide47.util.MeLanbide47Utils;
import es.altia.flexia.integracion.moduloexterno.melanbide47.vo.comun.AuditoriaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide47.vo.comun.DomicilioVO;
import es.altia.flexia.integracion.moduloexterno.melanbide47.vo.comun.EntidadVO;
import es.altia.flexia.integracion.moduloexterno.melanbide47.vo.comun.EspecialidadesVO;
import es.altia.flexia.integracion.moduloexterno.melanbide47.vo.comun.MunicipioVO;
import es.altia.flexia.integracion.moduloexterno.melanbide47.vo.comun.ProvinciaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide47.vo.comun.SelectItem;
import es.altia.flexia.integracion.moduloexterno.melanbide47.vo.comun.TerceroVO;
import es.altia.flexia.integracion.moduloexterno.melanbide47.vo.comun.TipoViaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide47.vo.comun.ViaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide47.vo.orientacion.AmbitosHorasVO;
import es.altia.flexia.integracion.moduloexterno.melanbide47.vo.orientacion.AsociacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide47.vo.orientacion.FilaAsociacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide47.vo.orientacion.FilaOriAmbitoSolicitadoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide47.vo.orientacion.FilaUbicOrientacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide47.vo.orientacion.OriAmbitoSolicitadoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide47.vo.orientacion.OriTrayActividadVO;
import es.altia.flexia.integracion.moduloexterno.melanbide47.vo.orientacion.OriTrayOtroProgramaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide47.vo.orientacion.OriTrayectoriaEntidadVO;
import es.altia.flexia.integracion.moduloexterno.melanbide47.vo.orientacion.OriTrayectoriaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide47.vo.orientacion.OriUbicVO;
import es.altia.flexia.integracion.moduloexterno.melanbide47.vo.orientacion.UbicacionesVO;
import es.altia.flexia.integracion.moduloexterno.melanbide47.vo.procesos.FilaAuditoriaProcesosVO;
import java.math.BigDecimal;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;



/**
 *
 * @author santiagoc
 */
public class MeLanbide47DAO 
{
    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide47DAO.class);
    
    //Instancia
    private static MeLanbide47DAO instance = null;
    
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private static final MeLanbide47Utils meLanbide47Utils = new MeLanbide47Utils();
    
    private MeLanbide47DAO()
    {
        
    }
    
    public static MeLanbide47DAO getInstance()
    {
        if(instance == null)
        {
            synchronized(MeLanbide47DAO.class)
            {
                instance = new MeLanbide47DAO();
            }
        }
        return instance;
    }    
    
    public List<SelectItem> getListaProvincias(Integer codigoPais, List<String> codigosProv, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        List<SelectItem> retList = new ArrayList<SelectItem>();
        try
        {
            String query = null;
            query = "select PRV_COD, PRV_NOM from "+ GlobalNames.ESQUEMA_GENERICO + ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_PROVINCIAS, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                    + " where PRV_PAI = '" + codigoPais + "'";
                    if(codigosProv != null && codigosProv.size() > 0)
                    {
                        query += " and (";
                        boolean primero = true;
                        for(String str : codigosProv)
                        {
                            if(!primero)
                            {
                                query += " OR ";
                            }
                            else
                            {
                                primero = false;
                            }
                            query += "PRV_COD = '"+str+"'";
                        }
                        query += ")";
                    }
                    query += " order by PRV_NOM";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            Integer cod;
            String desc;
            SelectItem si;
            while(rs.next())
            {
                cod = rs.getInt("PRV_COD");
                desc = rs.getString("PRV_NOM");
                si = new SelectItem();
                si.setId(cod);
                si.setLabel(desc);
                retList.add(si);
            }
        }
        catch(Exception ex)
        {
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
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return retList;
    }
    
    public List<SelectItem> getAmbitosHorasPorProvincia(String codProvincia, String anoConv, Connection con)throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        List<SelectItem> retList = new ArrayList<SelectItem>();
        try
        {
            String query = null;
            query = "select ORI_AMB_COD, ORI_AMB_AMBITO from " + ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI14_AMBITOS_HORAS, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                    + " where ORI_AMB_TERHIS = '" + codProvincia + "' and ORI_AMB_ANOCONV = '" + anoConv + "' ORDER BY ORI_AMB_AMBITO";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            Integer cod;
            String desc;
            SelectItem si;
            while(rs.next())
            {
                cod = rs.getInt("ORI_AMB_COD");
                desc = rs.getString("ORI_AMB_AMBITO");
                si = new SelectItem();
                si.setId(cod);
                si.setLabel(desc);
                retList.add(si);
            }
        }
        catch(Exception ex)
        {
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
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return retList;
    }
    
    public List<SelectItem> getMunicipiosPorAmbitoProvinciaHoras(String codProvincia, Integer codAmbito, Integer ano, Connection con)throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        Statement st2 = null;
        ResultSet rs2 = null;
        List<SelectItem> retList = new ArrayList<SelectItem>();
        if(codProvincia != null && codAmbito != null)
        {
            try
            {
                Integer codProv;
                Integer codMun;
                Integer cod;
                String desc;
                SelectItem si;
                String query = null;
                String query2 = null;
                
                query = "select MUN_PRV, MUN_COD from "+ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI14_UBICACIONES_HORAS, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                        + " where MUN_PAI = "+ConstantesMeLanbide47.CODIGO_PAIS_ESPANA
                        + " and ORI_AMB_COD = "+codAmbito+" and ORI_UBIC_ANO = "+ano;
                if(log.isDebugEnabled()) 
                    log.error("sql = " + query);
                st = con.createStatement();
                rs = st.executeQuery(query);
                while(rs.next())
                {
                    codProv = rs.getInt("MUN_PRV");
                    codMun = rs.getInt("MUN_COD");
                    query2 = "select MUN_COD, MUN_NOM from "+ GlobalNames.ESQUEMA_GENERICO + ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_MUNICIPIOS, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                             + " where MUN_PAI = "+ConstantesMeLanbide47.CODIGO_PAIS_ESPANA+" and MUN_PRV = "+codProv
                             + " and MUN_COD = "+codMun;
                    st2 = con.createStatement();
                    rs2 = st2.executeQuery(query2);
                    while(rs2.next())
                    {
                        cod = rs2.getInt("MUN_COD");
                        desc = rs2.getString("MUN_NOM");
                        si = new SelectItem();
                        si.setId(cod);
                        si.setLabel(desc);
                        si.setCodPrv(codProv);
                        retList.add(si);
                    }
                }
                Collections.sort(retList);
            }
            catch(Exception ex)
            {
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
                catch(Exception e)
                {
                    log.error("Se ha producido un error cerrando el statement y el resulset", e);
                    throw new Exception(e);
                }
            }
        }
        return retList;
    }
    
    public EntidadVO getEntidad(int codOrganizacion, String numExp, Integer ejercicio, Connection con)throws Exception
    {
        EntidadVO vo = null;
        Statement st = null;
        ResultSet rs = null;
        Statement st2 = null;
        ResultSet rs2 = null;
        try
        {
            String query = null;
            String query2 = null;
            //String ter = null;
            //String nvr = null;
            //query = "select * from  E_EXT where EXT_MUN = '"+codOrganizacion+"' and EXT_EJE = '"+ejercicio+"' and EXT_NUM = '"+numExp+"'";
            /*if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            boolean parada = false;
            while(rs.next() && !parada)
            {*/
                //ter = rs.getString("EXT_TER");
                //nvr = rs.getString("EXT_NVR");
                query2 = "select * from (select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI14_ENTIDAD, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                        +" where EXT_MUN = '" + codOrganizacion + "' and EXT_EJE = " + ejercicio + " and EXT_NUM = '" + numExp +"' "
                        //+" and EXT_TER = '" + ter + "' and EXT_NVR = '"+ nvr +"'";
                        +"order by ORI_ENT_COD desc) where rownum = 1";
                if(log.isDebugEnabled()) 
                    log.error("sql = " + query2);
                st2 = con.createStatement();
                rs2 = st2.executeQuery(query2);
                if(rs2.next())
                {
                    //parada = true;
                    try
                    {
                        vo = (EntidadVO)MeLanbide47MappingUtils.getInstance().map(rs2, EntidadVO.class);
                    }
                    catch(Exception ex)
                    {
                        log.error("Error en getEntidad - mapeo: " + ex.toString());
                    }
                }
            /*}*/
        }
        catch(Exception ex)
        {
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
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return vo;
    }
    
    public EntidadVO getEntidadPorCodigoYExpediente(EntidadVO entidad, int idioma, Connection con)throws Exception
    {
        EntidadVO vo = null;
        if(entidad != null && entidad.getOriEntCod() != null && entidad.getExtNum() != null)
        {
            PreparedStatement pt = null;
            ResultSet rs = null;
            try
            {
                String query = null;
                query = "select " + ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI14_ENTIDAD, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                        + ".* "
                        +" ,(CASE WHEN 4=? THEN oci.DESCRIPCIONEU ELSE oci.DESCRIPCION end) COMP_IGUALDAD_OPCION_TEXTO "
                        +" ,(CASE WHEN 4=? THEN ociv.DESCRIPCIONEU ELSE ociv.DESCRIPCION end) COMP_IGUALDAD_OPCION_VAL_TEXTO "
                        +" from " + ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI14_ENTIDAD, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                        +" LEFT JOIN ORI_COMP_IGUALDAD oci ON ORI14_ENTIDAD.COMP_IGUALDAD_OPCION = oCI.CODIGO "
                        +" LEFT JOIN ORI_COMP_IGUALDAD ociv ON ORI14_ENTIDAD.COMP_IGUALDAD_OPCION_VAL  = oCIv.CODIGO "
                        +" where ORI_ENT_COD = "+entidad.getOriEntCod()
                        +" and EXT_NUM = '"+entidad.getExtNum()+"'";
                if(log.isDebugEnabled()) 
                    log.error("sql = " + query);
                pt = con.prepareStatement(query);
                int contparams = 1;
                pt.setInt(contparams++,idioma);
                pt.setInt(contparams++,idioma);
                rs = pt.executeQuery();
                if(rs.next())
                {
                    try
                    {
                        vo = (EntidadVO)MeLanbide47MappingUtils.getInstance().map(rs, EntidadVO.class);
                    }
                    catch(Exception ex)
                    {

                    }
                }
            }
            catch(Exception ex)
            {
                log.error("Se ha producido un error recuperando las Areas", ex);
                throw new Exception(ex);
            }
            finally
            {
                try
                {
                    if(log.isDebugEnabled()) 
                        log.debug("Procedemos a cerrar el statement y el resultset");
                    if(pt!=null)
                        pt.close();
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
        return vo;
    }
    
    public HashMap<String, String> getDatosTercero(int codOrganizacion, String numExp, Integer ejercicio, Connection con)throws Exception
    {
        HashMap<String, String> map = new HashMap<String, String>();
        Statement st = null;
        ResultSet rs = null;
        try
        {
            String codProc = meLanbide47Utils.getCodProcedimientoDeExpediente(numExp);
            String query = null;
            query = "select EXT_EJE, EXT_NUM, EXT_TER, EXT_NVR, EXT_MUN "
                    + " , T_HTE.HTE_DOC ,T_HTE.HTE_NOC "
                    + " from "+ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_REL_TERCERO_DOMICILIO, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                    + " e "
                    + " left join t_hte on T_HTE.hte_ter=E.EXT_TER and e.ext_nvr=T_HTE.HTE_NVR "
                    + " where e.EXT_MUN = '"+codOrganizacion+"' and e.EXT_EJE = "+ejercicio+" and e.EXT_PRO='"+codProc+"' and  e.EXT_NUM = '"+numExp+"'";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                map.put("EXT_MUN", rs.getString("EXT_MUN"));
                map.put("EXT_EJE", rs.getString("EXT_EJE"));
                map.put("EXT_NUM", rs.getString("EXT_NUM"));
                map.put("EXT_TER", rs.getString("EXT_TER"));
                map.put("EXT_NVR", rs.getString("EXT_NVR"));
                map.put("HTE_DOC", rs.getString("HTE_DOC"));
                map.put("HTE_NOC", rs.getString("HTE_NOC"));
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando Datos del tercero", ex);
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
    
    public OriUbicVO guardarUbicacion(OriUbicVO ubic, Connection con) throws Exception
    {
        Statement st = null;
        try
        {
            String query = null;
            if(ubic.getOriOrientUbicCod() == null)
            {
                Long codUbicacion = this.getNextId(ConfigurationParameter.getParameter(ConstantesMeLanbide47.SEQ_ORI14_ORI_UBICACION, ConstantesMeLanbide47.FICHERO_PROPIEDADES), con);
                if(codUbicacion == null)
                {
                    throw new Exception();
                }
                ubic.setOriOrientUbicCod(codUbicacion.intValue());
                
                //Es una ubicacion nueva
                query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI14_ORI_UBIC, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                        + "(ORI_ORIENT_UBIC_COD, ORI_ORIENT_ANO, ORI_ENT_COD, ORI_ORIENT_NUMEXP, ORI_AMB_COD, MUN_PAI, MUN_PRV, MUN_COD, PRV_PAI"
                        + ", ORI_ORIENT_DIRECCION, ORI_ORIENT_CP, ORI_ORIENT_HORASSOLICITADAS, ORI_ORIENT_HORASADJ, ORI_ORIENT_DESPACHOS"
                        + ", ORI_ORIENT_AULAGRUPAL, ORI_ORIENT_DESPACHOS_VALIDADOS, ORI_ORIENT_AULAGRUPAL_VALIDADA, ORI_ORIENT_VAL_TRAY"
                        + ", ORI_ORIENT_VAL_UBIC, ORI_ORIENT_VAL_DESPACHOS, ORI_ORIENT_VAL_AULAS, ORI_ORIENT_PUNTUACION, ORI_ORIENT_OBSERVACIONES"
                        + ", ORI_ORIENT_AMBITO_UBICACION ,ORI_ORIENT_NOMENTID_UBICACION "
                        + ",ORI_ORIENT_ESPACIOADICIONA ,ORI_ORIENT_ESPHERRABUSQEMP ,ORI_ORIENT_ESPACIOADICIONA_VAL ,ORI_ORIENT_ESPHERRABUSQEMP_VAL "
                        + ",ORI_ORIENT_TELEFIJO_UBICACION ,ORI_ORIENT_PISO_UBICACION ,ORI_ORIENT_NUMERO_UBICACION ,ORI_ORIENT_LETRA_UBICACION "
                        + ",ORI_ORIENT_VAL_ESPACIOADICIONA, ORI_ORIENT_VAL_ESPHERRABUSQEMP"
                        + ",ORI_LOCALPREVAPRO, ORI_LOCALPREVAPRO_VAL "
                        + ",ORI_MATENREQ_LPA, ORI_MATENREQ_LPA_VAL "
                        + ",ORI_LOCALPREVAPRO_VALORACION, ORI_MATENREQ_LPA_VALORACION, ORI_PLANIGUALDAD_VALORACION,ORI_CERTCALIDAD_VALORACION "
                        + ")"
                        + " values("+ubic.getOriOrientUbicCod()
                        + ", "+ubic.getOriOrientAno()
                        + ", "+ubic.getOriEntCod()
                        + ", "+(ubic.getNumExp() != null && !ubic.getNumExp().equals("") ? "'"+ubic.getNumExp()+"'" : "null")
                        + ", "+ubic.getOriAmbCod()
                        + ", "+ubic.getMunPai()
                        + ", "+ubic.getMunPrv()
                        + ", "+ubic.getMunCod()
                        + ", "+ubic.getPrvPai()
                        + ", "+(ubic.getOriOrientDireccion() != null && !ubic.getOriOrientDireccion().equals("") ? "'"+ubic.getOriOrientDireccion()+"'" : "null")
                        + ", "+(ubic.getOriOrientCP() != null && !ubic.getOriOrientCP().equals("") ? "'"+ubic.getOriOrientCP()+"'" : "null")
                        + ", "+(ubic.getOriOrientHorasSolicitadas() != null ? ubic.getOriOrientHorasSolicitadas().toPlainString() : "null")
                        + ", "+(ubic.getOriOrientHorasadj() != null ? ubic.getOriOrientHorasadj().toPlainString() : "null")
                        + ", "+(ubic.getOriOrientDespachos() != null && !ubic.getOriOrientDespachos().equals("") ? "'"+ubic.getOriOrientDespachos()+"'" : "null")
                        + ", "+(ubic.getOriOrientAulagrupal() != null && !ubic.getOriOrientAulagrupal().equals("") ? "'"+ubic.getOriOrientAulagrupal()+"'" : "null")
                        + ", "+(ubic.getOriOrientDespachosValidados() != null && !ubic.getOriOrientDespachosValidados().equals("") ? "'"+ubic.getOriOrientDespachosValidados()+"'" : "null")
                        + ", "+(ubic.getOriOrientAulaGrupalValidada() != null && !ubic.getOriOrientAulaGrupalValidada().equals("") ? "'"+ubic.getOriOrientAulaGrupalValidada()+"'" : "null")
                        + ", "+(ubic.getOriOrientValTray() != null ? ubic.getOriOrientValTray().toPlainString() : "null")
                        + ", "+(ubic.getOriOrientValUbic() != null ? ubic.getOriOrientValUbic().toPlainString() : "null")
                        + ", "+ubic.getOriOrientValDespachos()
                        + ", "+ubic.getOriOrientValAulas()
                        + ", "+(ubic.getOriOrientPuntuacion() != null ? ubic.getOriOrientPuntuacion().toPlainString() : "null")
                        + ", "+(ubic.getOriOrientObservaciones()!= null && !ubic.getOriOrientObservaciones().equals("") ? "'"+ubic.getOriOrientObservaciones()+"'" : "null")
                        + ", "+(ubic.getOriOrientUbicaAmbitoDesc()!= null && !ubic.getOriOrientUbicaAmbitoDesc().equals("") ? "'"+ubic.getOriOrientUbicaAmbitoDesc()+"'" : "null")
                        + ", "+(ubic.getOriOrientUbicaNombEntidad()!= null && !ubic.getOriOrientUbicaNombEntidad().equals("") ? "'"+ubic.getOriOrientUbicaNombEntidad()+"'" : "null")
                        + ", "+(ubic.getOriOrientUbicaEspacioAdicional()!= null && !ubic.getOriOrientUbicaEspacioAdicional().equals("") ? "'"+ubic.getOriOrientUbicaEspacioAdicional()+"'" : "null")
                        + ", "+(ubic.getOriOrientUbicaEspAdicioHerrBusqEmpleo()!= null && !ubic.getOriOrientUbicaEspAdicioHerrBusqEmpleo().equals("") ? "'"+ubic.getOriOrientUbicaEspAdicioHerrBusqEmpleo()+"'" : "null")
                        + ", "+(ubic.getOriOrientUbicaEspacioAdicionalVal()!= null && !ubic.getOriOrientUbicaEspacioAdicionalVal().equals("") ? "'"+ubic.getOriOrientUbicaEspacioAdicionalVal()+"'" : "null")
                        + ", "+(ubic.getOriOrientUbicaEspAdicioHerrBusqEmpleoVal()!= null && !ubic.getOriOrientUbicaEspAdicioHerrBusqEmpleoVal().equals("") ? "'"+ubic.getOriOrientUbicaEspAdicioHerrBusqEmpleoVal()+"'" : "null")
                        + ", "+(ubic.getOriOrientUbicaTeleFijo()!= null && !ubic.getOriOrientUbicaTeleFijo().equals("") ? "'"+ubic.getOriOrientUbicaTeleFijo()+"'" : "null")
                        + ", "+(ubic.getOriOrientUbicaPiso()!= null && !ubic.getOriOrientUbicaPiso().equals("") ? "'"+ubic.getOriOrientUbicaPiso()+"'" : "null")
                        + ", "+(ubic.getOriOrientUbicaNumero()!= null && !ubic.getOriOrientUbicaNumero().equals("") ? "'"+ubic.getOriOrientUbicaNumero()+"'" : "null")
                        + ", "+(ubic.getOriOrientUbicaLetra()!= null && !ubic.getOriOrientUbicaLetra().equals("") ? "'"+ubic.getOriOrientUbicaLetra()+"'" : "null")
                        + ", "+ubic.getOriOrientVal1EspacioAdicional()
                        + ", "+ubic.getOriOrientValEspAdicioHerrBusqEmpleo()
                        + ", "+(ubic.getOriCELocalPreviamenteAprobado()!=null ? ubic.getOriCELocalPreviamenteAprobado() : "null" )
                        + ", "+(ubic.getOriCELocalPreviamenteAprobadoVAL()!=null ? ubic.getOriCELocalPreviamenteAprobadoVAL() : "null" )
                        + ", "+(ubic.getOriCEMantenimientoRequisitosLPA()!=null ? ubic.getOriCEMantenimientoRequisitosLPA() : "null" )
                        + ", "+(ubic.getOriCEMantenimientoRequisitosLPAVAL()!=null ? ubic.getOriCEMantenimientoRequisitosLPAVAL() : "null" )
                        + ", "+(ubic.getOriCELocalPreviamenteAprobadoValoracion()!=null ? ubic.getOriCELocalPreviamenteAprobadoValoracion(): "null" )
                        + ", "+(ubic.getOriCEMantenimientoRequisitosLPAValoracion()!=null ? ubic.getOriCEMantenimientoRequisitosLPAValoracion(): "null" )
                        + ", "+(ubic.getOriPlanIgualdadValoracion()!=null ? ubic.getOriPlanIgualdadValoracion() : "null" )
                        + ", "+(ubic.getOriCertCalidadValoracion()!=null ? ubic.getOriCertCalidadValoracion() : "null" )
                        + ")";
            }
            else
            {
                //Es una ubicacion que ya existe
                query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI14_ORI_UBIC, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                        + " set"
                        
                        + " ORI_ORIENT_UBIC_COD = "+ubic.getOriOrientUbicCod()
                        + ", ORI_ORIENT_ANO = "+ubic.getOriOrientAno()
                        + ", ORI_ENT_COD = "+ubic.getOriEntCod()
                        + ", ORI_ORIENT_NUMEXP = "+(ubic.getNumExp() != null && !ubic.getNumExp().equals("") ? "'"+ubic.getNumExp()+"'" : "null")
                        + ", ORI_AMB_COD = "+ubic.getOriAmbCod()
                        + ", MUN_PAI = "+ubic.getMunPai()
                        + ", MUN_PRV = "+ubic.getMunPrv()
                        + ", MUN_COD = "+ubic.getMunCod()
                        + ", PRV_PAI = "+ubic.getPrvPai()
                        + ", ORI_ORIENT_DIRECCION = "+(ubic.getOriOrientDireccion() != null && !ubic.getOriOrientDireccion().equals("") ? "'"+ubic.getOriOrientDireccion()+"'" : "null")
                        + ", ORI_ORIENT_CP = "+(ubic.getOriOrientCP() != null && !ubic.getOriOrientCP().equals("") ? "'"+ubic.getOriOrientCP()+"'" : "null")
                        + ", ORI_ORIENT_HORASSOLICITADAS = "+(ubic.getOriOrientHorasSolicitadas() != null ? ubic.getOriOrientHorasSolicitadas().toPlainString() : "null")
                        + ", ORI_ORIENT_HORASADJ = "+(ubic.getOriOrientHorasadj() != null ? ubic.getOriOrientHorasadj().toPlainString() : "null")
                        + ", ORI_ORIENT_DESPACHOS = "+(ubic.getOriOrientDespachos() != null && !ubic.getOriOrientDespachos().equals("") ? "'"+ubic.getOriOrientDespachos()+"'" : "null")
                        + ", ORI_ORIENT_AULAGRUPAL = "+(ubic.getOriOrientAulagrupal() != null && !ubic.getOriOrientAulagrupal().equals("") ? "'"+ubic.getOriOrientAulagrupal()+"'" : "null")
                        + ", ORI_ORIENT_DESPACHOS_VALIDADOS = "+(ubic.getOriOrientDespachosValidados() != null && !ubic.getOriOrientDespachosValidados().equals("") ? "'"+ubic.getOriOrientDespachosValidados()+"'" : "null")
                        + ", ORI_ORIENT_AULAGRUPAL_VALIDADA = "+(ubic.getOriOrientAulaGrupalValidada() != null && !ubic.getOriOrientAulaGrupalValidada().equals("") ? "'"+ubic.getOriOrientAulaGrupalValidada()+"'" : "null")
                        + ", ORI_ORIENT_VAL_TRAY = "+ (ubic.getOriOrientValTray() != null ? ubic.getOriOrientValTray().toPlainString() : "null")
                        + ", ORI_ORIENT_VAL_UBIC = "+(ubic.getOriOrientValUbic() != null ? ubic.getOriOrientValUbic().toPlainString() : "null")
                        + ", ORI_ORIENT_VAL_DESPACHOS = "+ubic.getOriOrientValDespachos()
                        + ", ORI_ORIENT_VAL_AULAS = "+ubic.getOriOrientValAulas()
                        + ", ORI_ORIENT_PUNTUACION = "+(ubic.getOriOrientPuntuacion() != null ? ubic.getOriOrientPuntuacion().toPlainString() : "null")
                        + ", ORI_ORIENT_OBSERVACIONES = "+(ubic.getOriOrientObservaciones() != null && !ubic.getOriOrientObservaciones().equals("") ? "'"+ubic.getOriOrientObservaciones()+"'" : "null")
                        + ", ORI_ORIENT_AMBITO_UBICACION = "+(ubic.getOriOrientUbicaAmbitoDesc() != null && !ubic.getOriOrientUbicaAmbitoDesc().equals("") ? "'"+ubic.getOriOrientUbicaAmbitoDesc()+"'" : "null")
                        + ", ORI_ORIENT_NOMENTID_UBICACION = "+(ubic.getOriOrientUbicaNombEntidad() != null && !ubic.getOriOrientUbicaNombEntidad().equals("") ? "'"+ubic.getOriOrientUbicaNombEntidad()+"'" : "null")
                        + ", ORI_ORIENT_ESPACIOADICIONA = "+(ubic.getOriOrientUbicaEspacioAdicional() != null && !ubic.getOriOrientUbicaEspacioAdicional().equals("") ? "'"+ubic.getOriOrientUbicaEspacioAdicional()+"'" : "null")
                        + ", ORI_ORIENT_ESPHERRABUSQEMP = "+(ubic.getOriOrientUbicaEspAdicioHerrBusqEmpleo() != null && !ubic.getOriOrientUbicaEspAdicioHerrBusqEmpleo().equals("") ? "'"+ubic.getOriOrientUbicaEspAdicioHerrBusqEmpleo()+"'" : "null")
                        + ", ORI_ORIENT_ESPACIOADICIONA_VAL = "+(ubic.getOriOrientUbicaEspacioAdicionalVal() != null && !ubic.getOriOrientUbicaEspacioAdicionalVal().equals("") ? "'"+ubic.getOriOrientUbicaEspacioAdicionalVal()+"'" : "null")
                        + ", ORI_ORIENT_ESPHERRABUSQEMP_VAL = "+(ubic.getOriOrientUbicaEspAdicioHerrBusqEmpleoVal() != null && !ubic.getOriOrientUbicaEspAdicioHerrBusqEmpleoVal().equals("") ? "'"+ubic.getOriOrientUbicaEspAdicioHerrBusqEmpleoVal()+"'" : "null")
                        + ", ORI_ORIENT_TELEFIJO_UBICACION = "+(ubic.getOriOrientUbicaTeleFijo() != null && !ubic.getOriOrientUbicaTeleFijo().equals("") ? "'"+ubic.getOriOrientUbicaTeleFijo()+"'" : "null")
                        + ", ORI_ORIENT_PISO_UBICACION = "+(ubic.getOriOrientUbicaPiso() != null && !ubic.getOriOrientUbicaPiso().equals("") ? "'"+ubic.getOriOrientUbicaPiso()+"'" : "null")
                        + ", ORI_ORIENT_NUMERO_UBICACION = "+(ubic.getOriOrientUbicaNumero() != null && !ubic.getOriOrientUbicaNumero().equals("") ? "'"+ubic.getOriOrientUbicaNumero()+"'" : "null")
                        + ", ORI_ORIENT_LETRA_UBICACION = "+(ubic.getOriOrientUbicaLetra() != null && !ubic.getOriOrientUbicaLetra().equals("") ? "'"+ubic.getOriOrientUbicaLetra()+"'" : "null")
                        + ", ORI_ORIENT_VAL_ESPACIOADICIONA = "+ubic.getOriOrientVal1EspacioAdicional()
                        + ", ORI_ORIENT_VAL_ESPHERRABUSQEMP = "+ubic.getOriOrientValEspAdicioHerrBusqEmpleo()
                        + ", ORI_LOCALPREVAPRO = "+(ubic.getOriCELocalPreviamenteAprobado()!=null ? ubic.getOriCELocalPreviamenteAprobado() : "null" )
                        + ", ORI_LOCALPREVAPRO_VAL = "+(ubic.getOriCELocalPreviamenteAprobadoVAL()!=null ? ubic.getOriCELocalPreviamenteAprobadoVAL() : "null" )
                        + ", ORI_MATENREQ_LPA = "+(ubic.getOriCEMantenimientoRequisitosLPA()!=null ? ubic.getOriCEMantenimientoRequisitosLPA() : "null" )
                        + ", ORI_MATENREQ_LPA_VAL = "+(ubic.getOriCEMantenimientoRequisitosLPAVAL()!=null ? ubic.getOriCEMantenimientoRequisitosLPAVAL() : "null" )
                        + ", ORI_LOCALPREVAPRO_VALORACION = "+(ubic.getOriCELocalPreviamenteAprobadoValoracion()!=null ? ubic.getOriCELocalPreviamenteAprobadoValoracion() : "null" )
                        + ", ORI_MATENREQ_LPA_VALORACION = "+(ubic.getOriCEMantenimientoRequisitosLPAValoracion()!=null ? ubic.getOriCEMantenimientoRequisitosLPAValoracion() : "null" )
                        + ", ORI_PLANIGUALDAD_VALORACION = "+(ubic.getOriPlanIgualdadValoracion()!=null ? ubic.getOriPlanIgualdadValoracion() : "null" )
                        + ", ORI_CERTCALIDAD_VALORACION = "+(ubic.getOriCertCalidadValoracion()!=null ? ubic.getOriCertCalidadValoracion() : "null" )
                        + " where ORI_ORIENT_UBIC_COD = "+ubic.getOriOrientUbicCod()
                        + " and ORI_ENT_COD = "+ubic.getOriEntCod();
            }
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            int res = st.executeUpdate(query);
            if(res > 0)
            {
                return ubic;
            }
            else
            {
                return null;
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error guardando los datos de la asociacion "+(ubic != null ? ubic.getOriOrientUbicCod(): "(ubicacion = null)")+" para la entidad "+(ubic != null ? ubic.getOriEntCod() : "(ubic = null)"), ex);
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
    
    public int eliminarUbicacionORI(OriUbicVO ubic, Connection con)throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        try
        {
            String query = null;
            
            //Guardo la entidad en BD
            query = "delete from "+ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI14_ORI_UBIC, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                    +" where ORI_ORIENT_UBIC_COD = " + ubic.getOriOrientUbicCod()
                    +" and ORI_ENT_COD = "+ubic.getOriEntCod();
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            return st.executeUpdate(query);
        }
        catch(Exception ex)
        {
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
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    
    public List<FilaUbicOrientacionVO> getUbicacionesORI(EntidadVO ent, boolean nuevaCon, Connection con)throws Exception
    {
        
        List<FilaUbicOrientacionVO> retList = new ArrayList<FilaUbicOrientacionVO>();
        Statement st = null;
        ResultSet rs = null;
        try
        {
            if(ent != null && ent.getOriEntCod() != null)
            {
                String query = "select"
                              +" ubic.ORI_ORIENT_UBIC_COD, ubic.ORI_ORIENT_ANO,prov.PRV_COD, prov.PRV_NOL, amb.ORI_AMB_COD, amb.ORI_AMB_AMBITO, mun.MUN_COD, mun.MUN_NOL"
                              +", ubic.ORI_ORIENT_DIRECCION, ubic.ORI_ORIENT_CP, ubic.ORI_ORIENT_HORASSOLICITADAS, ubic.ORI_ORIENT_DESPACHOS, ubic.ORI_ORIENT_AULAGRUPAL"
                              +", ubic.ORI_ORIENT_VAL_TRAY, ubic.ORI_ORIENT_VAL_UBIC, ubic.ORI_ORIENT_VAL_DESPACHOS, ubic.ORI_ORIENT_VAL_AULAS"
                              +", ubic.ORI_ORIENT_PUNTUACION, ubic.ORI_ORIENT_HORASADJ "
                        + ",ubic.ORI_ORIENT_ESPACIOADICIONA " 
                        + ",ubic.ORI_ORIENT_ESPHERRABUSQEMP " 
                        + ",ubic.ORI_ORIENT_ESPACIOADICIONA_VAL " 
                        + ",ubic.ORI_ORIENT_ESPHERRABUSQEMP_VAL " 
                        + ",ubic.ORI_ORIENT_VAL_ESPACIOADICIONA " 
                        + ",ubic.ORI_ORIENT_VAL_ESPHERRABUSQEMP " 
                        + ",ubic.ORI_ORIENT_TELEFIJO_UBICACION "
                        + ",ubic.ORI_ORIENT_NUMERO_UBICACION "
                        + ",ubic.ORI_ORIENT_PISO_UBICACION "
                        + ",ubic.ORI_ORIENT_LETRA_UBICACION "
                        + ",ubic.ORI_LOCALPREVAPRO "
                        + ",ubic.ORI_LOCALPREVAPRO_VAL "
                        + ",ubic.ORI_MATENREQ_LPA "
                        + ",ubic.ORI_MATENREQ_LPA_VAL "
                        + ",ubic.ORI_LOCALPREVAPRO_VALORACION "
                        + ",ubic.ORI_MATENREQ_LPA_VALORACION "
                        + ", ubic.ORI_PLANIGUALDAD_VALORACION"
                        + ", ubic.ORI_CERTCALIDAD_VALORACION"
                        + ", enti.ORI_ENT_PLAN_IGUALDAD_REG"
                        + ", enti.ORI_ENT_PLAN_IGUALDAD_REG_VAL"
                        + ", enti.ORI_ENT_CERT_CALIDAD"
                        + ", enti.ORI_ENT_CERT_CALIDAD_VAL"
                              +" from "+ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI14_ORI_UBIC, ConstantesMeLanbide47.FICHERO_PROPIEDADES)+" ubic"
                              +" left join "+ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI14_AMBITOS_HORAS, ConstantesMeLanbide47.FICHERO_PROPIEDADES)+" amb on ubic.ORI_AMB_COD = amb.ORI_AMB_COD and ubic.ORI_ORIENT_ANO=amb.ORI_AMB_ANOCONV"
                              + " left join "+ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI14_ENTIDAD, ConstantesMeLanbide47.FICHERO_PROPIEDADES)+ " enti on ubic.ORI_ENT_COD = enti.ORI_ENT_COD and enti.EXT_NUM = ubic.ORI_ORIENT_NUMEXP"
                              +" left join "+GlobalNames.ESQUEMA_GENERICO+ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_PROVINCIAS, ConstantesMeLanbide47.FICHERO_PROPIEDADES)+" prov on ubic.MUN_PRV = prov.PRV_COD"
                              +" left join "+GlobalNames.ESQUEMA_GENERICO+ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_MUNICIPIOS, ConstantesMeLanbide47.FICHERO_PROPIEDADES)+" mun on prov.PRV_COD = mun.MUN_PRV and ubic.MUN_COD = mun.MUN_COD"
                              +" where ubic.ORI_ENT_COD = "+ent.getOriEntCod() + " AND ORI_ORIENT_ANO="+ent.getExtEje() + " AND ORI_ORIENT_NUMEXP='"+ent.getExtNum()+"'"
                              +" order by prov.PRV_NOL, amb.ORI_AMB_AMBITO, mun.MUN_NOL, ubic.ORI_ORIENT_CP, ubic.ORI_ORIENT_DIRECCION";
                log.error("sql: " + query);
                st = con.createStatement();
                rs = st.executeQuery(query);
                while(rs.next())
                {
                    FilaUbicOrientacionVO ubic = (FilaUbicOrientacionVO) MeLanbide47MappingUtils.getInstance().mapVers2(rs, FilaUbicOrientacionVO.class, nuevaCon);
                    if (ubic != null && ubic.getEjercicio() != null && ubic.getEjercicio() >= 2018) {
                        BigDecimal horas = getNumBloquesSoliAmbitoSolicitadoORIPorNumExpCodAmbito(ent.getExtNum(), ubic.getCodAmbito(), con);
                        BigDecimal ubicaciones = getNumUbicacinoesEnAmbitoSolicitadoORIPorNumExpCodAmbito(ent.getExtNum(), ubic.getCodAmbito(), con);
                        ubic.setHoras(horas != null ? horas.toString() : null);
                        ubic.setOriOrientUbicaEspacioAdicional((ubicaciones != null && ubicaciones.compareTo(BigDecimal.ONE) == 1 ? ConstantesMeLanbide47.SI : ConstantesMeLanbide47.NO));
                    }
                   
                    retList.add(ubic);
                }
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando las ubicaciones", ex);
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

    public AmbitosHorasVO getAmbitoHorasPorCodigo(Integer ambCod, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        try {
            String query = null;
            //Creo el id con la secuencia
            query = "select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI14_AMBITOS_HORAS, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                    + " where ORI_AMB_COD = '" + ambCod + "'";
            if (log.isDebugEnabled()) {
                log.error("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            AmbitosHorasVO ambito = null;
            if (rs.next()) {
                ambito = (AmbitosHorasVO) MeLanbide47MappingUtils.getInstance().map(rs, AmbitosHorasVO.class);
            }
            return ambito;
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando las ï¿½reas", ex);
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

    public ProvinciaVO getProvinciaPorCodigo(Integer provCod, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        try {
            String query = null;
            //Creo el id con la secuencia
            query = "select * from " + GlobalNames.ESQUEMA_GENERICO + ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_PROVINCIAS, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                    + " where PRV_COD = '" + provCod + "'";
            if (log.isDebugEnabled()) {
                log.error("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            ProvinciaVO prov = null;
            if (rs.next()) {
                prov = (ProvinciaVO) MeLanbide47MappingUtils.getInstance().map(rs, ProvinciaVO.class);
            }
            return prov;
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando las ï¿½reas", ex);
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

    public MunicipioVO getMunicipioPorCodigoYProvincia(Integer munCod, Integer provCod, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        try {
            String query = null;
            //Creo el id con la secuencia
            query = "select * from " + GlobalNames.ESQUEMA_GENERICO + ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_MUNICIPIOS, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                    + " where MUN_COD = '" + munCod + "' and MUN_PRV = '" + provCod + "'";
            if (log.isDebugEnabled()) {
                log.error("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            MunicipioVO mun = null;
            if (rs.next()) {
                mun = (MunicipioVO) MeLanbide47MappingUtils.getInstance().map(rs, MunicipioVO.class);
            }
            return mun;
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando las ï¿½reas", ex);
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null)
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
    
    public OriUbicVO getUbicacionORIPorCodigo(OriUbicVO ubic, boolean nuevaCon, Connection con)throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        try
        {
            String query = null;
            //Creo el id con la secuencia
            query = "select * from "+ ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI14_ORI_UBIC, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                    +" where ORI_ORIENT_UBIC_COD = " + ubic.getOriOrientUbicCod();
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                ubic = (OriUbicVO)MeLanbide47MappingUtils.getInstance().map(rs, OriUbicVO.class);
                if (ubic !=null && ubic.getOriOrientAno() != null && ubic.getOriOrientAno() >= 2018) {
                    ubic.setOriOrientHorasSolicitadas(getNumBloquesSoliAmbitoSolicitadoORIPorNumExpCodAmbito(ubic.getNumExp(),ubic.getOriAmbCod(),con));
                    BigDecimal ubicaciones = getNumUbicacinoesEnAmbitoSolicitadoORIPorNumExpCodAmbito(ubic.getNumExp(), ubic.getOriAmbCod(), con);
                    ubic.setOriOrientUbicaEspacioAdicional((ubicaciones != null && ubicaciones.compareTo(BigDecimal.ONE)==1 ? ConstantesMeLanbide47.SI : ConstantesMeLanbide47.NO));
                }
                if (nuevaCon) {
                    int plan=getValorCampoEntidad(ubic.getNumExp(), ubic.getOriEntCod(),"ORI_ENT_PLAN_IGUALDAD_REG", con);
                    int planVal=getValorCampoEntidad(ubic.getNumExp(), ubic.getOriEntCod(),"ORI_ENT_PLAN_IGUALDAD_REG_VAL", con);
                    int cert=getValorCampoEntidad(ubic.getNumExp(), ubic.getOriEntCod(),"ORI_ENT_CERT_CALIDAD", con);
                    int certVal=getValorCampoEntidad(ubic.getNumExp(), ubic.getOriEntCod(),"ORI_ENT_CERT_CALIDAD_VAL", con);
                    ubic.setOriPlanIgualdad(plan);
                    ubic.setOriPlanIgualdadVal(planVal);
                    ubic.setOriCertCalidad(cert);
                    ubic.setOriCertCalidadVal(certVal);
                }
            }
            return ubic;            
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando ubicación por código ", ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if(log.isDebugEnabled()) 
                    log.error("Procedemos a cerrar el statement y el resultset");
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
    
    public UbicacionesVO getUbicacion(OriUbicVO pUbic, Connection con)throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        try
        {
            if(pUbic.getOriOrientAno() == null || pUbic.getOriAmbCod() == null || pUbic.getMunPai() == null || pUbic.getMunPrv() == null || pUbic.getMunCod() == null || pUbic.getPrvPai() == null)
            {
                return null;
            }
            String query = null;
            //Creo el id con la secuencia
            query = "select * from "+ ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI14_UBICACIONES_HORAS, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                    +" where ORI_UBIC_ANO = '"+pUbic.getOriOrientAno()+"' and ORI_AMB_COD = '"+pUbic.getOriAmbCod()+"'"
                    +" and MUN_PAI = '"+pUbic.getMunPai()+"' and MUN_PRV = '"+pUbic.getMunPrv()+"' and MUN_COD = '"+pUbic.getMunCod()+"' and PRV_PAI = '"+pUbic.getPrvPai()+"'";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            UbicacionesVO ubic = null;
            if(rs.next())
            {
                ubic = (UbicacionesVO)MeLanbide47MappingUtils.getInstance().map(rs, UbicacionesVO.class);
                if (pUbic!=null && pUbic.getOriOrientAno() != null && pUbic.getOriOrientAno() >= 2018) {
                    pUbic.setOriOrientHorasSolicitadas(getNumBloquesSoliAmbitoSolicitadoORIPorNumExpCodAmbito(pUbic.getNumExp(),ubic.getOriAmbCod(),con));
                    BigDecimal ubicaciones = getNumUbicacinoesEnAmbitoSolicitadoORIPorNumExpCodAmbito(pUbic.getNumExp(), ubic.getOriAmbCod(), con);
                    pUbic.setOriOrientUbicaEspacioAdicional((ubicaciones != null && ubicaciones.compareTo(BigDecimal.ONE)==1 ? ConstantesMeLanbide47.SI : ConstantesMeLanbide47.NO));
                }
            }
            return ubic;            
        }
        catch(Exception ex)
        {
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
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    
    public OriTrayectoriaVO getTrayectoriaORIPorCodigo(String codTray, Connection con)throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        try
        {
            String query = null;
            //Creo el id con la secuencia
            query = "select * from "+ ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI14_ORI_TRAYECTORIA, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                    +" where ORI_ORITRAY_COD = '" + codTray + "'";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            OriTrayectoriaVO tray = null;
            if(rs.next())
            {
                try
                {
                    tray = (OriTrayectoriaVO)MeLanbide47MappingUtils.getInstance().map(rs, OriTrayectoriaVO.class);
                }
                catch(Exception ex)
                {
                    
                }
            }
            return tray;            
        }
        catch(Exception ex)
        {
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
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    
    public EntidadVO guardarEntidad(EntidadVO ent, Connection con)throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        try
        {
            log.info("guardarEntidadDAO ");
            String query = null;
            
            if(ent.getOriEntCod() == null)
            {
                //Es una entidad nueva
                Long codEntidad = this.getNextId(ConfigurationParameter.getParameter(ConstantesMeLanbide47.SEQ_ORI14_ENTIDAD, ConstantesMeLanbide47.FICHERO_PROPIEDADES), con);
                if(codEntidad == null)
                {
                    throw new Exception();
                }
                ent.setOriEntCod(codEntidad);
                
                query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI14_ENTIDAD, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                        + "(ORI_ENT_COD, EXT_MUN, EXT_EJE, EXT_NUM, EXT_TER, EXT_NVR"
                        + ", ORI_ENT_NOM, ORI_ENT_ADMLOCAL_VAL, ORI_ENT_SUPRAMUN_VAL, ORI_EXP_CENTROFP_PUB_VAL, ORI_EXP_CENTROFP_PRIV_VAL"
                        + ", ORI_ENT_TRAYECTORIA, ORI_ENT_TRAYECTORIA_VAL, ORI_ORIENT_VAL_UBIC, ORI_ORIENT_VAL_DESPACHOS, ORI_ORIENT_VAL_AULAS, ORI_ENT_ACEPTA_MAS"
                        + ", ORI_ENT_ASOCIACION"
                        + ", ORI_ENT_SEGULOCALES_AMB"
                        + ", ORI_ENT_SINANIMOLUCRO"
                        + ", ORI_ENT_SINANIMOLUCRO_VAL"
                        + ", ori_ent_val_agencoloc"
                        + ", ORI_ENT_AGENCOLOC_NUMERO"
                        + ", ORI_ENT_PLAN_IGUALDAD_REG"
                        + ", ORI_ENT_CERT_CALIDAD"
                        + ", ORI_ENT_AGENCOLOC_NUMERO_VAL"
                        + ", ORI_ENT_PLAN_IGUALDAD_REG_VAL"
                        + ", ORI_ENT_CERT_CALIDAD_VAL"
                        + ", ORI_VALORACION_TRAYECTORIA"
                        + ", COMP_IGUALDAD_OPCION,COMP_IGUALDAD_OPCION_VAL,ENT_SUJETA_DER_PUBL,ENT_SUJETA_DER_PUBL_VAL "
                        + ")"
                        + " values("+ent.getOriEntCod()
                        + ", "+ent.getExtMun()
                        + ", "+ent.getExtEje()
                        + ", "+(ent.getExtNum() != null && !ent.getExtNum().equals("") ? "'"+ent.getExtNum()+"'" : "null")
                        + ", "+ent.getExtTer()
                        + ", "+ent.getExtNvr()
                        + ", "+(ent.getOriEntNom() != null && !ent.getOriEntNom().equals("") ? "'"+ent.getOriEntNom()+"'" : "null")
                        + ", "+(ent.getOriEntAdmLocalVal() != null && !ent.getOriEntAdmLocalVal().equals("") ? "'"+ent.getOriEntAdmLocalVal()+"'" : "null")
                        + ", "+(ent.getOriEntSupramunVal() != null && !ent.getOriEntSupramunVal().equals("") ? "'"+ent.getOriEntSupramunVal()+"'" : "null")
                        + ", "+ent.getOriExpCentrofpPubVal()
                        + ", "+ent.getOriExpCentrofpPrivVal()
                        + ", "+(ent.getOriEntTrayectoria() != null ? ent.getOriEntTrayectoria() : "null")
                        + ", "+(ent.getOriEntTrayectoriaVal()!= null ? ent.getOriEntTrayectoriaVal() : "null")
                        + ", "+ent.getOriOrientValUbic()
                        + ", "+ent.getOriOrientValDespachos()
                        + ", "+ent.getOriOrientValAulas()
                        + ", "+ent.getOriEntAceptaMas()
                        + ", "+ent.getOriEntAsociacion()
                        + ", "+ent.getSegundosLocalesAmbito()
                        + ", "+(ent.getSinAnimoLucro()!= null  ? ent.getSinAnimoLucro() : "null")
                        + ", "+(ent.getSinAnimoLucroVal()!= null  ? ent.getSinAnimoLucroVal() : "null")
                        + ", "+(ent.getAgenciaColocacionVal()!= null  ? ent.getAgenciaColocacionVal() : "null")
                        + ", "+(ent.getNumAgenciaColocacion() != null && !ent.getNumAgenciaColocacion().equals("") ? "'"+ent.getNumAgenciaColocacion()+"'" : "null")
                        + ", "+(ent.getPlanIgualdad()!= null  ? ent.getPlanIgualdad() : "null")
                        + ", "+(ent.getCertificadoCalidad()!= null  ? ent.getCertificadoCalidad() : "null")
                        + ", "+(ent.getNumAgenciaColocacionVal() != null && !ent.getNumAgenciaColocacionVal().equals("") ? "'"+ent.getNumAgenciaColocacionVal()+"'" : "null")
                        + ", "+(ent.getPlanIgualdadVal()!= null  ? ent.getPlanIgualdadVal() : "null")
                        + ", "+(ent.getCertificadoCalidadVal()!= null  ? ent.getCertificadoCalidadVal() : "null")
                        + ", "+(ent.getOriValoracionTrayectoria() != null ? ent.getOriValoracionTrayectoria().toPlainString() : "null")
                        + ", "+(ent.getCompIgualdadOpcion()!= null ? ent.getCompIgualdadOpcion(): "null")
                        + ", "+(ent.getCompIgualdadOpcionVal()!= null ? ent.getCompIgualdadOpcionVal(): "null")
                        + ", "+(ent.getEntSujetaDerPubl()!= null ? ent.getEntSujetaDerPubl(): "null")
                        + ", "+(ent.getEntSujetaDerPublVal()!= null ? ent.getEntSujetaDerPublVal(): "null")
                        + ")";
            }
            else
            {
                query = "update "+ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI14_ENTIDAD, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                        +" set EXT_MUN = "+(ent.getExtMun())
                        +", EXT_EJE = "+(ent.getExtEje())
                        +", EXT_NUM = "+(ent.getExtNum() != null && !ent.getExtNum().equals("") ? "'"+ent.getExtNum()+"'" : "null")
                        +", EXT_TER = "+(ent.getExtTer())
                        +", EXT_NVR = "+(ent.getExtNvr())
                        +", ORI_ENT_NOM = "+(ent.getOriEntNom() != null && !ent.getOriEntNom().equals("") ? "'"+ent.getOriEntNom()+"'" : "null")
                        +", ORI_ENT_ADMLOCAL_VAL = "+(ent.getOriEntAdmLocalVal() != null && !ent.getOriEntAdmLocalVal().equals("") ? "'"+ent.getOriEntAdmLocalVal()+"'" : "null")
                        +", ORI_ENT_SUPRAMUN_VAL = "+(ent.getOriEntSupramunVal() != null && !ent.getOriEntSupramunVal().equals("") ? "'"+ent.getOriEntSupramunVal()+"'" : "null")
                        +", ORI_EXP_CENTROFP_PUB_VAL = "+(ent.getOriExpCentrofpPubVal())
                        +", ORI_EXP_CENTROFP_PRIV_VAL = "+(ent.getOriExpCentrofpPrivVal())
                        +", ORI_ENT_TRAYECTORIA = " +(ent.getOriEntTrayectoria() != null ? ent.getOriEntTrayectoria() : "null")
                        + ", ORI_ENT_TRAYECTORIA_VAL = "+(ent.getOriEntTrayectoriaVal()!= null ? ent.getOriEntTrayectoriaVal() : "null")
                        +", ORI_ORIENT_VAL_UBIC = "+(ent.getOriOrientValUbic())
                        +", ORI_ORIENT_VAL_DESPACHOS = "+(ent.getOriOrientValDespachos())
                        +", ORI_ORIENT_VAL_AULAS = "+(ent.getOriOrientValAulas())
                        +", ORI_ENT_ACEPTA_MAS = "+(ent.getOriEntAceptaMas())
                        +", ORI_ENT_ASOCIACION = "+(ent.getOriEntAsociacion())
                        +", ORI_ENT_SEGULOCALES_AMB = "+(ent.getSegundosLocalesAmbito())
                        +", ORI_ENT_SINANIMOLUCRO = "+(ent.getSinAnimoLucro()!= null  ? ent.getSinAnimoLucro() : "null")
                        +", ORI_ENT_SINANIMOLUCRO_VAL = "+(ent.getSinAnimoLucroVal()!= null  ? ent.getSinAnimoLucroVal() : "null")
                        +", ori_ent_val_agencoloc = "+(ent.getAgenciaColocacionVal()!= null  ? ent.getAgenciaColocacionVal() : "null")
                        +", ORI_ENT_AGENCOLOC_NUMERO = "+(ent.getNumAgenciaColocacion() != null && !ent.getNumAgenciaColocacion().equals("") ? "'"+ent.getNumAgenciaColocacion()+"'" : "null")
                        +", ORI_ENT_PLAN_IGUALDAD_REG = "+(ent.getPlanIgualdad()!= null  ? ent.getPlanIgualdad() : "null")
                        +", ORI_ENT_CERT_CALIDAD = "+(ent.getCertificadoCalidad()!= null  ? ent.getCertificadoCalidad() : "null")
                        +", ORI_ENT_AGENCOLOC_NUMERO_VAL = "+(ent.getNumAgenciaColocacionVal() != null && !ent.getNumAgenciaColocacionVal().equals("") ? "'"+ent.getNumAgenciaColocacionVal()+"'" : "null")
                        +", ORI_ENT_PLAN_IGUALDAD_REG_VAL = "+(ent.getPlanIgualdadVal()!= null  ? ent.getPlanIgualdadVal() : "null")
                        +", ORI_ENT_CERT_CALIDAD_VAL = "+(ent.getCertificadoCalidadVal()!= null  ? ent.getCertificadoCalidadVal() : "null")
                        +", ORI_VALORACION_TRAYECTORIA = " +(ent.getOriValoracionTrayectoria() != null ? ent.getOriValoracionTrayectoria().toPlainString() : "null")
                        + ", COMP_IGUALDAD_OPCION = "+(ent.getCompIgualdadOpcion()!= null ? ent.getCompIgualdadOpcion(): "null")
                        + ", COMP_IGUALDAD_OPCION_VAL = "+(ent.getCompIgualdadOpcionVal()!= null ? ent.getCompIgualdadOpcionVal(): "null")
                        + ", ENT_SUJETA_DER_PUBL = "+(ent.getEntSujetaDerPubl()!= null ? ent.getEntSujetaDerPubl(): "null")
                        + ", ENT_SUJETA_DER_PUBL_VAL = "+(ent.getEntSujetaDerPublVal()!= null ? ent.getEntSujetaDerPublVal(): "null")
                        + " where ORI_ENT_COD = "+ent.getOriEntCod();
            }
            
            
            if(log.isDebugEnabled())
            {
                log.error("modificarEntidad SQL = "+query);
            }
            st = con.createStatement();
            int result = st.executeUpdate(query);
            if(result > 0)
            {
                return ent;
            }
            else
            {
                return null;
            }
            
        }
        catch(Exception ex)
        {
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
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    
    public List<SelectItem> getListaEspecialidades(Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        List<SelectItem> retList = new ArrayList<SelectItem>();
        try
        {
            String query = null;
            query = "select ORI_ESP_COD, ORI_ESP_DESC from "+ ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI14_ESPECIALIDADES, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                    +" order by ORI_ESP_DESC ASC";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            Integer cod;
            String desc;
            SelectItem si;
            while(rs.next())
            {
                cod = rs.getInt("ORI_ESP_COD");
                desc = rs.getString("ORI_ESP_DESC");
                si = new SelectItem();
                si.setId(cod);
                si.setLabel(desc);
                retList.add(si);
            }
        }
        catch(Exception ex)
        {
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
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return retList;
    }
    
    public List<Integer> getDistintasProvDeAmbitosHoras(Connection con)throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        List<Integer> retList = new ArrayList<Integer>();
        try
        {
            String query = null;
            query = "select distinct ORI_AMB_TERHIS from "+ ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI14_AMBITOS_HORAS, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                    +" order by ORI_AMB_TERHIS ASC";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            Integer cod = 0;
            while(rs.next())
            {
                cod = rs.getInt("ORI_AMB_TERHIS");
                if(!rs.wasNull())
                    retList.add(cod);
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando las Areas", ex);
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
    
    public List<Integer> getDistintasProvDeAmbitosCentroEmpleo(Connection con)throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        List<Integer> retList = new ArrayList<Integer>();
        try
        {
            String query = null;
            query = "select distinct ORI_AMB_TERHIS from "+ ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI14_AMBITOS_CE, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                    +" order by ORI_AMB_TERHIS ASC";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            Integer cod = 0;
            while(rs.next())
            {
                cod = rs.getInt("ORI_AMB_TERHIS");
                if(!rs.wasNull())
                    retList.add(cod);
            }
        }
        catch(Exception ex)
        {
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
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return retList;
    }
    
    public List<AmbitosHorasVO> getAmbitosHorasPorProvincia(Integer codProvincia, Integer anoConv, Connection con)throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        List<AmbitosHorasVO> retList = new ArrayList<AmbitosHorasVO>();
        try
        {
            String query = null;
            query = "select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI14_AMBITOS_HORAS, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                    + " where ORI_AMB_TERHIS = '" + codProvincia + "' and ORI_AMB_ANOCONV = '" + anoConv + "' ORDER BY ORI_AMB_AMBITO";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            AmbitosHorasVO ambito = null;
            while(rs.next())
            {
                ambito = (AmbitosHorasVO)MeLanbide47MappingUtils.getInstance().map(rs, AmbitosHorasVO.class);
                if(ambito != null)
                {
                    retList.add(ambito);
                }
            }
        }
        catch(Exception ex)
        {
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
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return retList;
    }
    
    public List<OriUbicVO> getUbicacionesDeAmbitoORIDocumResolProvisional(AmbitosHorasVO amb, Connection con) throws Exception
    {
        log.error("getUbicacionesDeAmbitoORIDocumResolProvisional - Begin()");
        Statement st = null;
        ResultSet rs = null;
        List<OriUbicVO> retList = new ArrayList<OriUbicVO>();
        try
        {
            String query = "select ubic.* "
                    + " ,case when tramite.cro_fei is not null and tramite.cro_fef is null then 1 else 0 end as estaResolProv "
                    + " ,max_punt_ubic_ambito_ent.puntuacion_max_ubic_amb_ent "
                    + " from "+ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI14_ORI_UBIC, ConstantesMeLanbide47.FICHERO_PROPIEDADES)+" ubic "
                    + " inner join "+ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI14_ENTIDAD, ConstantesMeLanbide47.FICHERO_PROPIEDADES)+" ent"
                    + " on ubic.ori_ent_cod = ent.ori_ent_cod"
                    + " LEFT JOIN E_CRO tramite on tramite.cro_eje=ORI_ORIENT_ANO and tramite.cro_num=ORI_ORIENT_NUMEXP and tramite.cro_tra=3 "  
                    + " LEFT JOIN (e_cro tramite2 inner join e_tra oN tra_pro = tramite2.cro_pro  AND tra_cod = tramite2.cro_tra and tra_cou=12) ON tramite2.cro_eje = ori_orient_ano AND tramite2.cro_num = ori_orient_numexp  "
                    + " LEFT JOIN (select ubic.ori_orient_ano,ubic.ori_orient_numexp, ubic.ORI_AMB_COD,ubic.ORI_ENT_COD,max(ORI_ORIENT_PUNTUACION) puntuacion_max_ubic_amb_ent "
                    + "            from  ori14_ori_ubic ubic "
                    + "            group by ubic.ori_orient_ano, ubic.ori_orient_numexp, ubic.ORI_AMB_COD, ubic.ORI_ENT_COD) max_punt_ubic_ambito_ent "
                    + "    on max_punt_ubic_ambito_ent.ori_orient_ano=ubic.ori_orient_ano and max_punt_ubic_ambito_ent.ori_orient_numexp=ubic.ori_orient_numexp and max_punt_ubic_ambito_ent.ori_amb_cod=ubic.ori_amb_cod and max_punt_ubic_ambito_ent.ori_ent_cod=ubic.ori_ent_cod "
                    + " where ubic.ORI_AMB_COD = "+amb.getOriAmbCod()
                    + " AND ( tra_cou is null or tra_cou not in('12')) "  // Que no esten las ubicaciones de expt en resolucion provisional negativa desde convocatoria de 2018.
                    + " order by estaResolProv desc,max_punt_ubic_ambito_ent.puntuacion_max_ubic_amb_ent desc,ubic.ori_ent_cod,ubic.ori_orient_puntuacion desc nulls last,ubic.ORI_ORIENT_HORASSOLICITADAS desc ";
            
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            OriUbicVO ubic = null;
            while(rs.next())
            {
                ubic = (OriUbicVO)MeLanbide47MappingUtils.getInstance().map(rs, OriUbicVO.class);
                // Seteamos la maxima puntuacion de las ubicaciones para que en el excel muestre solo una puntuacion y pueda hacer el merge de las cledas.
                ubic.setOriOrientPuntuacion(rs.getBigDecimal("PUNTUACION_MAX_UBIC_AMB_ENT"));
                if (ubic!= null && ubic.getOriOrientAno() != null && ubic.getOriOrientAno() >= 2018) {
                    ubic.setOriOrientHorasSolicitadas(getNumBloquesSoliAmbitoSolicitadoORIPorNumExpCodAmbito(ubic.getNumExp(),ubic.getOriAmbCod(),con));
                    BigDecimal ubicaciones = getNumUbicacinoesEnAmbitoSolicitadoORIPorNumExpCodAmbito(ubic.getNumExp(), ubic.getOriAmbCod(), con);
                    ubic.setOriOrientUbicaEspacioAdicional((ubicaciones != null && ubicaciones.compareTo(BigDecimal.ONE) == 1 ? ConstantesMeLanbide47.SI : ConstantesMeLanbide47.NO));                    
                    ubic.setOriOrientDireccion((ubic.getOriOrientDireccion() 
                            + (ubic.getOriOrientUbicaNumero() !=null && ubic.getOriOrientUbicaNumero()!="" ? (" " +   ubic.getOriOrientUbicaNumero()) : "") 
                            + (ubic.getOriOrientUbicaPiso() !=null && ubic.getOriOrientUbicaPiso()!="" ? (" " +   ubic.getOriOrientUbicaPiso()) : "") 
                            + (ubic.getOriOrientUbicaLetra() !=null && ubic.getOriOrientUbicaLetra()!="" ? (" " +   ubic.getOriOrientUbicaLetra()) : "")
                            ));
                }
                if(ubic != null)
                {
                    retList.add(ubic);
                }
            }
        }
        catch(Exception ex)
        {
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
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        log.error("getUbicacionesDeAmbitoORIDocumResolProvisional - End()");
        return retList;
    }
    
    public String[] adjudicaOrientacion(Integer ano, Connection con) throws Exception
    {
        CallableStatement st = null;
        ResultSet rs = null;
        try
        {
            String query = null;
            query = "call adjudica_orientacion_2021(?,?,?)";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.prepareCall(query);
            st.setInt(1, ano);
            st.registerOutParameter(2, java.sql.Types.VARCHAR);
            st.registerOutParameter(3, java.sql.Types.VARCHAR);
            int i = st.executeUpdate();
            String result1 = st.getString(2);
            String result2 = st.getString(3);
            return new String[]{result1, result2};
        }
        catch(Exception ex)
        {
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
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    
    public String consolidaHoras(Connection con) throws Exception
    {
        CallableStatement st = null;
        ResultSet rs = null;
        try
        {
            String query = null;
            query = "call consolida_horas(?)";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.prepareCall(query);
            st.registerOutParameter(1, java.sql.Types.VARCHAR);
            int i = st.executeUpdate();
            String result = st.getString(1);
            return result;
        }
        catch(Exception ex)
        {
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
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    
    public String deshacerConsolidacionHoras(Integer ano, Connection con) throws Exception
    {
        CallableStatement st = null;
        ResultSet rs = null;
        try
        {
            String query = null;
            query = "call elimina_consolidacion_horas(?, ?)";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.prepareCall(query);
            st.setInt(1, ano);
            st.registerOutParameter(2, java.sql.Types.VARCHAR);
            int i = st.executeUpdate();
            String result = st.getString(2);
            return result;
        }
        catch(Exception ex)
        {
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
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    
    public EspecialidadesVO getEspecialidadPorCodigo(Integer codEsp, Connection con) throws Exception
    {
        EspecialidadesVO vo = null;
        Statement st = null;
        ResultSet rs = null;
        try
        {
            String query = null;
            query = "select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI14_ESPECIALIDADES, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                        +" where ORI_ESP_COD = '" + codEsp + "'";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                try
                {
                    vo = (EspecialidadesVO)MeLanbide47MappingUtils.getInstance().map(rs, EspecialidadesVO.class);
                }
                catch(Exception ex)
                {

                }
            }
        }
        catch(Exception ex)
        {
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
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return vo;
    }
    
    public BigDecimal getHorasAsignadasUbicacionORI(Integer codUbic, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        try
        {
            String query = null;
            query = "select HORAS_ASIGNADAS from " + ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI14_TMP_ADJUDICACION, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                        +" where ORI_ORIENT_UBIC_COD = '" + codUbic + "'";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                try
                {
                    return rs.getBigDecimal("HORAS_ASIGNADAS");
                }
                catch(Exception ex)
                {

                }
            }
        }
        catch(Exception ex)
        {
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
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return null;
    }
    
    public int actualizarValoracionTrayectoriaUbicaciones(EntidadVO entidad, Integer valor, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        try
        {
            if(entidad != null && entidad.getOriEntCod() != null && valor != null)
            {
                con.setAutoCommit(false);
                String query = null;
                query = "update  " + ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI14_ORI_UBIC, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                            +" set ORI_ORIENT_VAL_TRAY = "+valor+" where ORI_ENT_COD = '" + entidad.getOriEntCod() + "'";
                if(log.isDebugEnabled()) 
                    log.error("sql = " + query);
                st = con.createStatement();
                int result = st.executeUpdate(query);   
                con.commit();
                return result;
            }
            else
            {
                return 0;
            }
        }
        catch(Exception ex)
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
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    
    
    public boolean entidadTieneUbicaciones(EntidadVO entidad, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        try
        {
            if(entidad != null && entidad.getOriEntCod() != null)
            {
                con.setAutoCommit(false);
                String query = null;
                query = "select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI14_ORI_UBIC, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                            +" where ORI_ENT_COD = '"+entidad.getOriEntCod()+"'";
                if(log.isDebugEnabled()) 
                    log.error("sql = " + query);
                st = con.createStatement();
                rs = st.executeQuery(query);
                return rs.next();
            }
            else
            {
                return false;
            }
        }
        catch(Exception ex)
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
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    
    public List<OriUbicVO> getUbicacionesDeEntidadORI(EntidadVO entidad, boolean nuevaCon, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<OriUbicVO> retList = new ArrayList<OriUbicVO>();
        try {
            if (entidad != null && entidad.getOriEntCod() != null) {
                String query = null;
                query = "select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI14_ORI_UBIC, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                        + " where ORI_ENT_COD = '" + entidad.getOriEntCod() + "'"
                        + " and ORI_ORIENT_ANO=" + entidad.getExtEje()
                        + " and ORI_ORIENT_NUMEXP='" + entidad.getExtNum() + "'";
                if (log.isDebugEnabled()) {
                    log.error("sql = " + query);
                }
                st = con.createStatement();
                rs = st.executeQuery(query);
                while (rs.next()) {
                    OriUbicVO ubic = (OriUbicVO) MeLanbide47MappingUtils.getInstance().map(rs, OriUbicVO.class);
                    if (ubic != null && ubic.getOriOrientAno() != null && ubic.getOriOrientAno() >= 2018) {
                        ubic.setOriOrientHorasSolicitadas(getNumBloquesSoliAmbitoSolicitadoORIPorNumExpCodAmbito(ubic.getNumExp(), ubic.getOriAmbCod(), con));
                        BigDecimal ubicaciones = getNumUbicacinoesEnAmbitoSolicitadoORIPorNumExpCodAmbito(ubic.getNumExp(), ubic.getOriAmbCod(), con);
                        ubic.setOriOrientUbicaEspacioAdicional((ubicaciones != null && ubicaciones.compareTo(BigDecimal.ONE) == 1 ? ConstantesMeLanbide47.SI : ConstantesMeLanbide47.NO));

                    }
                    if (nuevaCon) {
                        int plan = getValorCampoEntidad(entidad.getExtNum(), entidad.getOriEntCod(), "ORI_ENT_PLAN_IGUALDAD_REG", con);
                        int planVal = getValorCampoEntidad(entidad.getExtNum(), entidad.getOriEntCod(), "ORI_ENT_PLAN_IGUALDAD_REG_VAL", con);
                        int cert = getValorCampoEntidad(entidad.getExtNum(), entidad.getOriEntCod(), "ORI_ENT_CERT_CALIDAD", con);
                        int certVal = getValorCampoEntidad(entidad.getExtNum(), entidad.getOriEntCod(), "ORI_ENT_CERT_CALIDAD_VAL", con);
                        ubic.setOriPlanIgualdad(plan);
                        ubic.setOriPlanIgualdadVal(planVal);
                        ubic.setOriCertCalidad(cert);
                        ubic.setOriCertCalidadVal(certVal);
                    }
                    retList.add(ubic);
                }
            }
            return retList;
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando las ï¿½reas", ex);
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

    public int crearAuditoria(AuditoriaVO aud, Connection con) throws Exception
    {
        int retValue = 0;
        if(aud != null)
        {
            
            Statement st = null;
            ResultSet rs = null;
            try
            {
                String query = null;
                query = "insert into "+ ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI14_AUDITORIA, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                        +" values("+aud.getUsuCod()
                        +", "+aud.getProcedimiento()
                        +", current_timestamp"
                        +", "+(aud.getResultado() != null ? "'"+aud.getResultado().toUpperCase()+"'" : "null")
                        +", "+(aud.getCodProcedimiento() != null ? "'"+aud.getCodProcedimiento().toUpperCase()+"'" : "null")+")";
                if(log.isDebugEnabled()) 
                    log.error("sql = " + query);
                st = con.createStatement();
                retValue = st.executeUpdate(query);
            }
            catch(Exception ex)
            {
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
                catch(Exception e)
                {
                    log.error("Se ha producido un error cerrando el statement y el resulset", e);
                    throw new Exception(e);
                }
            }
        }
        return retValue;
    }
    
    public List<FilaAuditoriaProcesosVO> filtrarAuditoriaProcesos(String nomApellidos, Date feDesde, Date feHasta, Integer codProceso, String codProcedimiento, int codIdioma, Connection con) throws Exception
    {
        List<FilaAuditoriaProcesosVO> retList = new ArrayList<FilaAuditoriaProcesosVO>();
            
        Statement st = null;
        ResultSet rs = null;
        try
        {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String query = null;
            boolean tieneWhere = false;
            query = "select au.*, u.usu_nom from "+ ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI14_AUDITORIA, ConstantesMeLanbide47.FICHERO_PROPIEDADES)+" au"
                   +" left join "+ GlobalNames.ESQUEMA_GENERICO + ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_USUARIOS, ConstantesMeLanbide47.FICHERO_PROPIEDADES)+" u"
                   +" on au.usu_cod = u.usu_cod";
            
            if(nomApellidos != null && !nomApellidos.equalsIgnoreCase(""))
            {
                if(!tieneWhere)
                {
                    query += " where";
                    tieneWhere = true;
                }
                query += " UPPER(u.usu_nom) like '%"+nomApellidos.toUpperCase()+"%'";
            }
            
            if(feDesde != null && feHasta != null)
            {
                if(!tieneWhere)
                {
                    query += " where";
                    tieneWhere = true;
                }
                else
                {
                    query += " and";
                }
                query += " au.fec_ejecucion between to_timestamp('"+format.format(feDesde)+"', 'dd/mm/yyyy hh24:mi:ss') and to_timestamp('"+format.format(feHasta)+"', 'dd/mm/yyyy hh24:mi:ss')";
            }
            else if(feDesde != null)
            {
                if(!tieneWhere)
                {
                    query += " where";
                    tieneWhere = true;
                }
                else
                {
                    query += " and";
                }
                query += " au.fec_ejecucion >= to_timestamp('"+format.format(feDesde)+"', 'dd/mm/yyyy hh24:mi:ss')";
            }
            else if(feHasta != null)
            {
                if(!tieneWhere)
                {
                    query += " where";
                    tieneWhere = true;
                }
                else
                {
                    query += " and";
                }
                query += " au.fec_ejecucion <= to_timestamp('"+format.format(feHasta)+"', 'dd/mm/yyyy hh24:mi:ss')";
            }
            
            if(codProceso != null)
            {
                if(!tieneWhere)
                {
                    query += " where";
                    tieneWhere = true;
                }
                else
                {
                    query += " and";
                }
                query += " au.proceso = "+codProceso.toString();
            }
            
            if(codProcedimiento != null && !codProcedimiento.equalsIgnoreCase(""))
            {
                if(!tieneWhere)
                {
                    query += " where";
                    tieneWhere = true;
                }
                else
                {
                    query += " and";
                }
                query += " au.cod_procedimiento = '"+codProcedimiento.toUpperCase()+"'";
            }
            
            query += " order by au.fec_ejecucion desc";
            
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            FilaAuditoriaProcesosVO fila = null;
            Timestamp fecha = null;
            String na = null;
            int cproceso = -1;
            String descProceso = null;
            String resultado = null;
            while(rs.next())
            {
                fila = new FilaAuditoriaProcesosVO();
                fecha = rs.getTimestamp("FEC_EJECUCION");
                fila.setFecha(fecha != null ? format.format(fecha) : "");
                na = rs.getString("USU_NOM");
                fila.setNomApellidos(na != null ? na.toUpperCase() : "");
                cproceso = rs.getInt("PROCESO");
                if(rs.wasNull())
                {
                    cproceso = -1;
                }
                descProceso = meLanbide47Utils.obtenerNombreProceso(cproceso, codIdioma);
                fila.setProceso(descProceso != null ? descProceso.toUpperCase() : "");
                resultado = rs.getString("RESULTADO");
                fila.setResultado(resultado != null ? resultado.toUpperCase() : "");
                retList.add(fila);
            }
        }
        catch(Exception ex)
        {
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
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return retList;
    }
    
    public List<EntidadVO> getEntidadesQueNoEstanEnEstado(String ejercicio, String codProc, List<String> tramites, Connection con) throws Exception
    {
        List<EntidadVO> retList = new ArrayList<EntidadVO>();
            
        Statement st = null;
        ResultSet rs = null;
        try
        {
            /*String query = "select ent.* from "+ ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI14_ENTIDAD, ConstantesMeLanbide47.FICHERO_PROPIEDADES)+" ent"
                          +" inner join "+ ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_E_CRO, ConstantesMeLanbide47.FICHERO_PROPIEDADES)+" cro"
                          +" on cro.cro_num = ent.ext_num"
                          +" where cro_pro = '"+ConstantesMeLanbide47.CODIGO_PROCEDIMIENTO+"' and cro_eje = "+ejercicio+" and cro_tra not in (";
            
            for(int i = 0; i < tramites.size(); i++)
            {
                if(i > 0)
                {
                    query += ",";
                }
                query += tramites.get(i);
            }
            
            query += ")";*/
            
            /*String query = "select * from "+ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI14_ENTIDAD, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                           +" where ext_num in (select distinct(ext_num) from "+ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI14_ENTIDAD, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                           +" ent inner join "+ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_E_CRO, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                           +" cro on cro.cro_num = ent.ext_num where cro_pro = '"+ConstantesMeLanbide47.CODIGO_PROCEDIMIENTO+"' "
                           +" and cro_eje = "+ejercicio+" and cro_tra not in (";
            
            for(int i = 0; i < tramites.size(); i++)
            {
                if(i > 0)
                {
                    query += ",";
                }
                query += tramites.get(i);
            }
            
//            query += ")) and ori_ent_cod in ((select distinct ori_ent_cod from "+ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI14_ORI_UBIC, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
//                    +") union (select distinct ori_ent_cod from "+ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI14_CE_UBIC, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
//                    +"))";
            
            query += "))";*/
            
            
            
            
            String query = "select * from "+ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI14_ENTIDAD, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                           +" where ori_ent_cod in( select cod from("
                           +" select max(ori_ent_cod) cod, ext_num from "+ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI14_ENTIDAD, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                           +" where ext_num in (select distinct(ext_num) from "+ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI14_ENTIDAD, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                           +" ent inner join "+ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_E_CRO, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                           +" cro on cro.cro_num = ent.ext_num where cro_pro = '"+codProc+"' "
                           +" and cro_eje = "+ejercicio+" and cro_tra not in (";
            
            for(int i = 0; i < tramites.size(); i++)
            {
                if(i > 0)
                {
                    query += ",";
                }
                query += tramites.get(i);
            }
            
//            query += ")) and ori_ent_cod in ((select distinct ori_ent_cod from "+ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI14_ORI_UBIC, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
//                    +") union (select distinct ori_ent_cod from "+ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI14_CE_UBIC, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
//                    +"))";
            
            query += ")) group by ext_num))";
            
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while(rs.next())
            {
                retList.add((EntidadVO)MeLanbide47MappingUtils.getInstance().map(rs, EntidadVO.class));
            }
        }
        catch(Exception ex)
        {
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
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return retList;
    }
    
    public TerceroVO getTerceroPorCodigo(Long codTercero, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        TerceroVO ter = null;
        try
        {
            String query = null;
            query = "select * from "+ ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_TERCEROS, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                    +" where ter_cod = '"+codTercero+"'";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                ter = (TerceroVO)MeLanbide47MappingUtils.getInstance().map(rs, TerceroVO.class);
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
    
    public DomicilioVO getDomicilioTercero(TerceroVO ter, Connection con) throws Exception
    {
        if(ter != null && ter.getTerCod() != null)
        {
            Statement st = null;
            ResultSet rs = null;
            DomicilioVO dom = null;
            try
            {
                String query = null;
                query = "select dom.* from "+ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_DOMICILIO, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                        +" dom inner join "+ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_T_DOT, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                        +" dot on dom.DNN_DOM = dot.DOT_DOM where dot.DOT_TER = '"+ter.getTerCod()+"'";
                if(log.isDebugEnabled()) 
                    log.error("sql = " + query);
                st = con.createStatement();
                rs = st.executeQuery(query);
                if(rs.next())
                {
                    dom = (DomicilioVO)MeLanbide47MappingUtils.getInstance().map(rs, DomicilioVO.class);
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
    
    public TipoViaVO getTipoViaDomicilio(DomicilioVO dom, Connection con) throws Exception
    {
        if(dom != null && dom.getDom() != null)
        {
            Statement st = null;
            ResultSet rs = null;
            TipoViaVO tipoVia = null;
            try
            {
                String query = null;
                query = "select tip.* from "+ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_TIPO_VIA, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                        +" tip inner join t_dnn dom on tip.tvi_cod = dom.dnn_tvi and dnn_dom = '"+dom.getDom().toString()+"'";
                if(log.isDebugEnabled()) 
                    log.error("sql = " + query);
                st = con.createStatement();
                rs = st.executeQuery(query);
                if(rs.next())
                {
                    tipoVia = (TipoViaVO)MeLanbide47MappingUtils.getInstance().map(rs, TipoViaVO.class);
                }
                return tipoVia;
            }
            catch(Exception ex)
            {
                log.error("Se ha producido un error recuperando tipo via domicilio "+dom.getDom(), ex);
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
    
    public ViaVO getViaDomicilio(DomicilioVO dom, Connection con) throws Exception
    {
        if(dom != null && dom.getDom() != null)
        {
            Statement st = null;
            ResultSet rs = null;
            ViaVO via = null;
            try
            {
                String query = null;
                query = "select via.* from "+ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_VIAS, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                        +" via inner join t_dnn dom on via.via_cod = dom.dnn_via and dnn_dom = '"+dom.getDom().toString()+"'";
                if(log.isDebugEnabled()) 
                    log.error("sql = " + query);
                st = con.createStatement();
                rs = st.executeQuery(query);
                if(rs.next())
                {
                    via = (ViaVO)MeLanbide47MappingUtils.getInstance().map(rs, ViaVO.class);
                }
                return via;
            }
            catch(Exception ex)
            {
                log.error("Se ha producido un error recuperando tipo via domicilio "+dom.getDom(), ex);
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
    
    public TerceroVO getTerceroPorExpedienteYRol(int codOrganizacion, String numExpediente, String ejercicio, String rol, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        Statement st2 = null;
        ResultSet rs2 = null;
        TerceroVO ter = null;
        try
        {
            String query = null;
            query = "select * from "+ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_REL_TERCERO_DOMICILIO, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                    +" where EXT_MUN = "+codOrganizacion+" and EXT_NUM = '"+numExpediente+"' and EXT_EJE = "+ejercicio+" and EXT_ROL = '"+rol+"'";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                String codTercero = rs.getString("EXT_TER");
                String query2 = "select * from "+ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_TERCEROS, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                                +" where TER_COD = "+codTercero;
                st2 = con.createStatement();
                rs2 = st2.executeQuery(query2);
                if(rs2.next())
                {
                    ter = (TerceroVO)MeLanbide47MappingUtils.getInstance().map(rs2, TerceroVO.class);
                }
            }
            return ter;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando tercero con rol "+rol+" para expediente "+numExpediente, ex);
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
                if(st2!=null) 
                    st2.close();
                if(rs2!=null) 
                    rs2.close();
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    
    public List<OriUbicVO> getUbicacionesAdjudicadasDeEntidadORI(EntidadVO entidad, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        List<OriUbicVO> retList = new ArrayList<OriUbicVO>();
        try
        {
            if(entidad != null && entidad.getOriEntCod() != null)
            {
                String query = null;
                query = "select * from "+ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI14_ORI_UBIC, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                        +" ubic inner join "+ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI14_TMP_ADJUDICACION, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                        +" adj on ubic.ori_orient_ubic_cod = adj.ori_orient_ubic_cod"
                        +" where ubic.ORI_ENT_COD = '"+entidad.getOriEntCod()+"' and adj.horas_asignadas > 0";
                if(log.isDebugEnabled()) 
                    log.error("sql = " + query);
                st = con.createStatement();
                rs = st.executeQuery(query);
                while(rs.next())
                {
                    OriUbicVO ubic = (OriUbicVO) MeLanbide47MappingUtils.getInstance().map(rs, OriUbicVO.class);
                    if (ubic != null && ubic.getOriOrientAno() != null && ubic.getOriOrientAno() >= 2018) {
                        ubic.setOriOrientHorasSolicitadas(getNumBloquesSoliAmbitoSolicitadoORIPorNumExpCodAmbito(ubic.getNumExp(), ubic.getOriAmbCod(), con));
                        BigDecimal ubicaciones = getNumUbicacinoesEnAmbitoSolicitadoORIPorNumExpCodAmbito(ubic.getNumExp(), ubic.getOriAmbCod(), con);
                        ubic.setOriOrientUbicaEspacioAdicional((ubicaciones != null && ubicaciones.compareTo(BigDecimal.ONE) == 1 ? ConstantesMeLanbide47.SI : ConstantesMeLanbide47.NO));
                    }
                   
                    retList.add(ubic);
                }
            }
            return retList;
        }
        catch(Exception ex)
        {
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
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    
    public String getCodigoInternoTramite(int codOrganizacion, String codProc, String codTramite, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        Long valor = null;
        try
        {
            String query = "select TRA_COD from "+ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_TRAMITES, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                            +" where TRA_MUN = "+codOrganizacion
                            +" and TRA_PRO = '"+codProc+"'"
                            +" and TRA_COU = "+codTramite;
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
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
            log.error("Se ha producido un error recuperando el c?o interno del tr?te "+codTramite, ex);
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
        return valor != null ? valor.toString() : null;
    }
    
    public boolean expedienteEstaCerradoOAnulado(int codOrganizacion, String numExpediente, String ejercicio, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        boolean cerradoAnulado = false;
        int estado = 0;
        try
        {
            String query = "select EXP_EST from "+ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_EXPEDIENTES, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                            +" where EXP_MUN = "+codOrganizacion
                            +" and EXP_NUM = '"+numExpediente+"'"
                            +" and EXP_EJE = "+ejercicio;
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                estado = rs.getInt("EXP_EST");
                if(rs.wasNull())
                {
                    cerradoAnulado = true;
                }
                else
                {
                    if(estado == ConstantesMeLanbide47.ESTADO_CERRADO || estado == ConstantesMeLanbide47.ESTADO_ANULADO)
                    {
                        cerradoAnulado = true;
                    }
                }
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando el estado del expediente "+numExpediente, ex);
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
        return cerradoAnulado;
    }
    
    public Long getCodigoUltimoTramiteAbierto(int codOrganizacion, String ejercicio, String codProc, String numExp, Long ocurrencia, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        Long cod = null;
        try
        {
            String query = "select CRO_TRA from "+ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_E_CRO, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                                +" where CRO_MUN = "+codOrganizacion
                                +" and CRO_PRO = '"+codProc+"'"
                                +" and CRO_EJE = "+ejercicio
                                +" and CRO_NUM = '"+numExp+"'"
                                +" and CRO_OCU = "+ocurrencia
                                +" and CRO_FEF is null";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
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

                    String query2 = "select MAX(TRA_COU) as TRA_COU from "+ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_TRAMITES, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                                    +" where TRA_MUN = "+codOrganizacion
                                    +" and TRA_PRO = '"+codProc+"'"
                                    +" and TRA_COD in(";
                    if(log.isDebugEnabled()) 
                        log.error("sql = " + query);
                    for(int i = 0; i < listaOcurrencias.size(); i++)
                    {
                        if(i > 0)
                        {
                            query2 += ", ";
                        }
                        query2 += listaOcurrencias.get(i);
                    }
                    query2 += ")";
                    st2 = con.createStatement();
                    rs2 = st2.executeQuery(query2);
                    if(rs2.next())
                    {
                        cod = rs2.getLong("TRA_COU");
                    }
                }
                catch(Exception ex)
                {
                    log.error("Se ha producido un error recuperando el tr?te abierto ("+numExp+")", ex);
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
            log.error("Se ha producido un error recuperando el tr?te abierto ("+numExp+")", ex);
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
    
    public AsociacionVO guardarAsociacion(AsociacionVO asoc, Connection con) throws Exception
    {
        Statement st = null;
        try
        {
            String query = null;
            if(asoc.getOriAsocCod() == null)
            {
                Long codAsociacion = this.getNextId(ConfigurationParameter.getParameter(ConstantesMeLanbide47.SEQ_ORI14_ASOCIACION, ConstantesMeLanbide47.FICHERO_PROPIEDADES), con);
                if(codAsociacion == null)
                {
                    throw new Exception();
                }
                asoc.setOriAsocCod(codAsociacion);
                
                //Es una entidad nueva
                query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI14_ASOCIACION, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                        + "(ORI_ASOC_COD, ORI_ENT_COD, ORI_ASOC_CIF, ORI_ASOC_NOMBRE"
                        + ", ORI_ENT_SUPRAMUN, ORI_ENT_ADMLOCAL, ORI_EXP_CENTROFP_PRIV, ORI_EXP_CENTROFP_PUB"
                        + ", ORI_ENT_AGENCOLOC "
                        + ", ORI_ENT_ASO_NUM_EXP"
                        + ", ORI_ENT_SINANIMOLUCRO"
                        + ", ORI_ENT_AGENCOLOC_NUMERO"
                        + ", ORI_ENT_PLAN_IGUALDAD_REG"
                        + ", ORI_ENT_CERT_CALIDAD"
                        + ", ORI_ENT_AGENCOLOC_NUMERO_VAL"
                        + ", ORI_ENT_PLAN_IGUALDAD_REG_VAL"
                        + ", ORI_ENT_CERT_CALIDAD_VAL"
                        + ", COMP_IGUALDAD_OPCION,COMP_IGUALDAD_OPCION_VAL,ENT_SUJETA_DER_PUBL,ENT_SUJETA_DER_PUBL_VAL "
                        + ")"
                        + " values("+asoc.getOriAsocCod()
                        + ", "+asoc.getOriEntCod()
                        + ", "+(asoc.getOriAsocCif() != null && !asoc.getOriAsocCif().equals("") ? "'"+asoc.getOriAsocCif()+"'" : "null")
                        + ", "+(asoc.getOriAsocNombre() != null && !asoc.getOriAsocCif().equals("") ? "'"+asoc.getOriAsocNombre()+"'" : "null")
                        + ", "+asoc.getOriEntSupramun()
                        + ", "+asoc.getOriEntAdmLocal()
                        + ", "+asoc.getOriExpCentrofpPriv()
                        + ", "+asoc.getOriExpCentrofpPub()
                        + ", "+asoc.getAgenciaColocacion()
                        + ", '"+asoc.getNumExpediente()+ "'"
                        + ", "+(asoc.getOriExpSinAnimoLucro()!=null ? asoc.getOriExpSinAnimoLucro() : "null")
                        + ", "+(asoc.getNumAgenciaColocacion() != null && !asoc.getNumAgenciaColocacion().equals("") ? "'"+asoc.getNumAgenciaColocacion()+"'" : "null")
                        + ", "+(asoc.getPlanIgualdad()!=null ? asoc.getPlanIgualdad() : "null")
                        + ", "+(asoc.getCertificadoCalidad()!=null ? asoc.getCertificadoCalidad() : "null")
                        + ", "+(asoc.getNumAgenciaColocacionVal() != null && !asoc.getNumAgenciaColocacionVal().equals("") ? "'"+asoc.getNumAgenciaColocacionVal()+"'" : "null")
                        + ", "+(asoc.getPlanIgualdadVal()!=null ? asoc.getPlanIgualdadVal() : "null")
                        + ", "+(asoc.getCertificadoCalidadVal()!=null ? asoc.getCertificadoCalidadVal() : "null")
                        + ", "+(asoc.getCompIgualdadOpcion()!= null ? asoc.getCompIgualdadOpcion(): "null")
                        + ", "+(asoc.getCompIgualdadOpcionVal()!= null ? asoc.getCompIgualdadOpcionVal(): "null")
                        + ", "+(asoc.getEntSujetaDerPubl()!= null ? asoc.getEntSujetaDerPubl(): "null")
                        + ", "+(asoc.getEntSujetaDerPublVal()!= null ? asoc.getEntSujetaDerPublVal(): "null")
                        + ")";
            }
            else
            {
                //Es una entidad que ya existe
                query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI14_ASOCIACION, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                        + " set"
                        + " ORI_ASOC_CIF = "+(asoc.getOriAsocCif() != null ? "'"+asoc.getOriAsocCif()+"'" : "null")
                        + ", ORI_ASOC_NOMBRE = "+(asoc.getOriAsocNombre() != null ? "'"+asoc.getOriAsocNombre()+"'" : "null")
                        + ", ORI_ENT_SUPRAMUN = "+asoc.getOriEntSupramun()
                        + ", ORI_ENT_ADMLOCAL = "+asoc.getOriEntAdmLocal()
                        + ", ORI_EXP_CENTROFP_PRIV = "+asoc.getOriExpCentrofpPriv()
                        + ", ORI_EXP_CENTROFP_PUB = "+asoc.getOriExpCentrofpPub()
                        + ", ORI_ENT_AGENCOLOC = "+asoc.getAgenciaColocacion()
                        + ", ORI_ENT_SINANIMOLUCRO = "+(asoc.getOriExpSinAnimoLucro()!=null ? asoc.getOriExpSinAnimoLucro() : "null")
                        + ", ORI_ENT_AGENCOLOC_NUMERO = "+(asoc.getNumAgenciaColocacion() != null ? "'"+asoc.getNumAgenciaColocacion()+"'" : "null")
                        + ", ORI_ENT_PLAN_IGUALDAD_REG = "+(asoc.getPlanIgualdad()!=null ? asoc.getPlanIgualdad() : "null")
                        + ", ORI_ENT_CERT_CALIDAD = "+(asoc.getCertificadoCalidad()!=null ? asoc.getCertificadoCalidad() : "null")
                        + ", ORI_ENT_AGENCOLOC_NUMERO_VAL = "+(asoc.getNumAgenciaColocacionVal() != null ? "'"+asoc.getNumAgenciaColocacionVal()+"'" : "null")
                        + ", ORI_ENT_PLAN_IGUALDAD_REG_VAL = "+(asoc.getPlanIgualdadVal()!=null ? asoc.getPlanIgualdadVal() : "null")
                        + ", ORI_ENT_CERT_CALIDAD_VAL = "+(asoc.getCertificadoCalidadVal()!=null ? asoc.getCertificadoCalidadVal() : "null")
                        + ", COMP_IGUALDAD_OPCION = "+(asoc.getCompIgualdadOpcion()!= null ? asoc.getCompIgualdadOpcion(): "null")
                        + ", COMP_IGUALDAD_OPCION_VAL = "+(asoc.getCompIgualdadOpcionVal()!= null ? asoc.getCompIgualdadOpcionVal(): "null")
                        + ", ENT_SUJETA_DER_PUBL = "+(asoc.getEntSujetaDerPubl()!= null ? asoc.getEntSujetaDerPubl(): "null")
                        + ", ENT_SUJETA_DER_PUBL_VAL = "+(asoc.getEntSujetaDerPublVal()!= null ? asoc.getEntSujetaDerPublVal(): "null")
                        + " where ORI_ASOC_COD = "+asoc.getOriAsocCod()
                        + " and ORI_ENT_COD = "+asoc.getOriEntCod();
            }
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            int res = st.executeUpdate(query);
            if(res > 0)
            {
                return asoc;
            }
            else
            {
                return null;
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error guardando los datos de la asociacion "+(asoc != null ? asoc.getOriAsocCod(): "(asociacion = null)")+" para la entidad "+(asoc != null ? asoc.getOriEntCod() : "(asoc = null)"), ex);
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
    
    public int eliminarAsociacion(AsociacionVO asoc, Connection con) throws Exception
    {
        Statement st = null;
        try
        {
            String query = null;

            //Es una entidad nueva
            query = "delete from " + ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI14_ASOCIACION, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                    + " where ORI_ASOC_COD = "+asoc.getOriAsocCod()+" and ORI_ENT_COD = "+asoc.getOriEntCod();
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            return st.executeUpdate(query);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error guardando los datos de la asociacion "+(asoc != null ? asoc.getOriAsocCod(): "(asociacion = null)")+" para la entidad "+(asoc != null ? asoc.getOriEntCod() : "(asoc = null)"), ex);
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
    
    public AsociacionVO getAsociacionPorCodigoYEntidad(AsociacionVO asoc, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        AsociacionVO ret = null;
        try
        {
            if(asoc != null && asoc.getOriAsocCod() != null && asoc.getOriEntCod() != null)
            {
                String query = null;
                query = "select * from "+ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI14_ASOCIACION, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                        +" where ORI_ASOC_COD = "+asoc.getOriAsocCod()+" and ORI_ENT_COD = "+asoc.getOriEntCod();
                if(log.isDebugEnabled()) 
                    log.error("sql = " + query);
                st = con.createStatement();
                rs = st.executeQuery(query);
                if(rs.next())
                {
                    ret = (AsociacionVO)MeLanbide47MappingUtils.getInstance().map(rs, AsociacionVO.class);
                }
            }
            return ret;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando getAsociacionPorCodigoYEntidad", ex);
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
    public long getCodigoAsociacionEntidadNoAsociacionxCodEntidad(long codigoEntidad, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        long ret = 0;
        try
        {
                String query = null;
                query =" select ORI_ASOC_COD, ORI_ENT_COD, count(ORI_ASOC_COD) over (partition by ORI_ENT_COD) NRO_ENTIDADES "
                      + "  from "+ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI14_ASOCIACION, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                      + "  where ORI_ENT_COD ="+codigoEntidad;
                if(log.isDebugEnabled()) 
                    log.error("sql = " + query);
                st = con.createStatement();
                rs = st.executeQuery(query);
                if(rs.next())
                {
                    long codASociacion = rs.getLong("ORI_ASOC_COD");
                    long codEntidad = rs.getLong("ORI_ENT_COD");
                    long NRO_ENTIDADES = rs.getLong("NRO_ENTIDADES");
                    
                    //Retornamos  Solo el codigo si es un Entidad que no es asociacion, es decir que solo tenga un registro en la tabla asociacion.
                    if(NRO_ENTIDADES==1){
                        ret=codASociacion;
                    }
                }
            return ret;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando getCodigoAsociacionEntidadNoAsociacionxCodEntidad", ex);
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
    
    public List<FilaAsociacionVO> getListaAsociacionesPorEntidad(EntidadVO entidad, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        List<FilaAsociacionVO> filas = new ArrayList<FilaAsociacionVO>();
        try
        {
            if(entidad != null && entidad.getOriEntCod() != null)
            {
                String query = null;
                query = "select * from "+ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI14_ASOCIACION, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                        +" where ORI_ENT_COD = "+entidad.getOriEntCod()+" ORDER BY ORI_ASOC_CIF";
                if(log.isDebugEnabled()) 
                    log.error("sql = " + query);
                st = con.createStatement();
                rs = st.executeQuery(query);
                while(rs.next())
                {
                    filas.add((FilaAsociacionVO)MeLanbide47MappingUtils.getInstance().map(rs, FilaAsociacionVO.class));
                }
            }
            return filas;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando la lista de asociaciones de una entidad ORI14", ex);
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
    
    public boolean eliminarAsociacionesDeEntidad(EntidadVO entidad, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        try
        {
            if(entidad != null && entidad.getOriEntCod() != null)
            {
                String query = null;
                query = "select count(*) NUM_ASOCIACIONES from "+ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI14_ASOCIACION, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                        +" where ORI_ENT_COD = "+entidad.getOriEntCod();
                if(log.isDebugEnabled()) 
                    log.error("sql = " + query);
                st = con.createStatement();
                rs = st.executeQuery(query);
                int numAsociaciones = 0;
                if(rs.next())
                {
                    numAsociaciones = rs.getInt("NUM_ASOCIACIONES");
                }
                
                if(numAsociaciones > 0)
                {
                    //Si hay alguna, las eliminamos
                    query = "delete from "+ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI14_ASOCIACION, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                           +" where ORI_ENT_COD = "+entidad.getOriEntCod();
                    st = con.createStatement();
                    int result = st.executeUpdate(query);
                    if(result < numAsociaciones)
                    {
                        return false;
                    }
                    else
                    {
                        return true;
                    }
                }
                else
                {
                    return true;
                }
            }
            else
            {
                return false;
            }
        }
        catch(Exception ex)
        {
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
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    
    public boolean asociacionRepetida(EntidadVO ent, AsociacionVO asoc, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        try
        {
            String query = null;
            query = "select count(*) NUM_ASOCIACIONES from "+ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI14_ASOCIACION, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                    +" where ORI_ASOC_CIF = '"+asoc.getOriAsocCif()+"'"
                    +" and ORI_ENT_COD = "+ent.getOriEntCod();
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            int numAsociaciones = 0;
            if(rs.next())
            {
                numAsociaciones = rs.getInt("NUM_ASOCIACIONES");
            }

            return numAsociaciones > 0;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error comprobando asociaciones repetidas", ex);
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
    
    public List<OriTrayectoriaVO> getListaTrayectoriasPorExpediente(OriTrayectoriaVO trayEjemplo, Connection con) throws Exception{
        return getListaTrayectoriasPorExpediente(trayEjemplo,con,false);
    }
    public List<OriTrayectoriaVO> getListaTrayectoriasPorExpediente(OriTrayectoriaVO trayEjemplo, Connection con,boolean sonDatosValidados) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        List<OriTrayectoriaVO> listaTrayectorias = new ArrayList<OriTrayectoriaVO>();
        if(trayEjemplo != null && trayEjemplo.getNumExp() != null && !trayEjemplo.getNumExp().equals(""))
        {
            try
            {
                String query = null;
                query = "select t.* from "+establecerTablaTrayectoriasPresentadasOValidadas(sonDatosValidados)+" t"
                       +" where t.ORI_ORITRAY_NUMEXP = '"+trayEjemplo.getNumExp()+"'";

                if(log.isDebugEnabled()) 
                    log.error("sql = " + query);
                st = con.createStatement();
                rs = st.executeQuery(query);
                while(rs.next())
                {
                    try
                    {
                        listaTrayectorias.add(((OriTrayectoriaVO)MeLanbide47MappingUtils.getInstance().map(rs, OriTrayectoriaVO.class)));
                    }
                    catch(Exception ex)
                    {
                        log.error("Error recuperando trayectoria por expediente", ex);
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
    
    // Sobrecargamos para gestionar tabla de datos validados
    public OriTrayectoriaVO getTrayectoriaPorAsociacionYExpediente(OriTrayectoriaVO tray, Connection con) throws Exception{
        return getTrayectoriaPorAsociacionYExpediente(tray,con,false);
    }
    public OriTrayectoriaVO getTrayectoriaPorAsociacionYExpediente(OriTrayectoriaVO tray, Connection con,boolean sonDatosValidados) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        OriTrayectoriaVO retTray = null;
        if(tray != null && tray.getOriAsocCod() != null)
        {
            if(tray != null && tray.getNumExp() != null && !tray.getNumExp().equals(""))
            {
                try
                {
                    String query = null;
                    query = "select *"
                           +" from "+ establecerTablaTrayectoriasPresentadasOValidadas(sonDatosValidados) +" t"
                           +" where t.ORI_ASOC_COD = "+tray.getOriAsocCod()+" and t.ORI_ORITRAY_NUMEXP = '"+tray.getNumExp()+"'";

                    if(log.isDebugEnabled()) 
                        log.error("sql = " + query);
                    st = con.createStatement();
                    rs = st.executeQuery(query);
                    if(rs.next())
                    {
                        retTray = ((OriTrayectoriaVO)MeLanbide47MappingUtils.getInstance().map(rs, OriTrayectoriaVO.class));
                    }
                }
                catch(Exception ex)
                {
                    log.error("Se ha producido una excepcion en la BBDD recuperando trayectoria para entidad "+(tray != null ? tray.getOriAsocCod() : "(tray = null)")+" y el expediente "+(tray != null ? tray.getNumExp() : " (tray = null)"), ex);
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
        return retTray;
    }
    
    // Sobrecrga para el temas de Nuevo apartado de validacion
    public boolean guardarTrayectoriaAsociacion(OriTrayectoriaVO tray, Connection con) throws Exception{
        return guardarTrayectoriaAsociacion(tray,con,false);
    }
    public boolean guardarTrayectoriaAsociacion(OriTrayectoriaVO tray, Connection con, boolean sonDatosValidados) throws Exception
    {
        Statement st = null;
        try
        {
            if(tray.getOriAsocCod() == null || (tray.getNumExp() == null || tray.getNumExp().equals("")))
            {
                return false;
            }
            else
            {
                String query = null;
                if(tray.getOriOritrayCod() == null)
                {
                    Long codTray = null;
                    if(sonDatosValidados){
                        codTray=this.getNextId(ConfigurationParameter.getParameter(ConstantesMeLanbide47.SEQ_ORI14_ORI_TRAYECTORIA_VAL, ConstantesMeLanbide47.FICHERO_PROPIEDADES), con);
                    }else
                        codTray=this.getNextId(ConfigurationParameter.getParameter(ConstantesMeLanbide47.SEQ_ORI14_ORI_TRAYECTORIA, ConstantesMeLanbide47.FICHERO_PROPIEDADES), con);
                    
                    if(codTray == null)
                    {
                        throw new Exception();
                    }
                    tray.setOriOritrayCod(codTray.intValue());

                    //Es un registro nuevo
                    query = "insert into " + establecerTablaTrayectoriasPresentadasOValidadas(sonDatosValidados)
                            + "(ORI_ORITRAY_COD, ORI_ASOC_COD, ORI_ORITRAY_NUMEXP"
                            + ", COLEC1_DEC_327_2007, COLEC1_DEC_327_2008, COLEC1_DEC_327_2009, COLEC1_DEC_327_2010"
                            + ", COLEC1_MIN_94_2007, COLEC1_MIN_94_2008, COLEC1_MIN_94_2009, COLEC1_MIN_94_2010, COLEC1_MIN_94_2011, COLEC1_MIN_94_2012, COLEC1_MIN_94_2013, COLEC1_MIN_94_2014, COLEC1_MIN_94_2015, COLEC1_MIN_94_2016, COLEC1_MIN_94_2017, COLEC1_MIN_94_2018 "
                            + ", COLEC1_MIN_98_2007, COLEC1_MIN_98_2008, COLEC1_MIN_98_2009, COLEC1_MIN_98_2010, COLEC1_MIN_98_2011, COLEC1_MIN_98_2012, COLEC1_MIN_98_2013, COLEC1_MIN_98_2014, COLEC1_MIN_98_2015, COLEC1_MIN_98_2016, COLEC1_MIN_98_2017, COLEC1_MIN_98_2018 "
                            + ", COLEC1_TAS_03_2007, COLEC1_TAS_03_2008, COLEC1_TAS_03_2009, COLEC1_TAS_03_2010, COLEC1_TAS_03_2011, COLEC1_TAS_03_2012, COLEC1_TAS_03_2013, COLEC1_TAS_03_2014, COLEC1_TAS_03_2015, COLEC1_TAS_03_2016, COLEC1_TAS_03_2017, COLEC1_TAS_03_2018 "
                            + ", COLEC1_ACT_56_03"
                            + ", COLEC2_LAN_2011, COLEC2_LAN_2013, COLEC2_LAN_2014, COLEC2_LAN_2015, COLEC2_LAN_2017, COLEC2_LAN_OTROS "
                            + ", COLEC1_DEC_327"
                            + ", COLEC1_MIN_94"
                            + ", COLEC1_MIN_98 "
                            + ", COLEC1_TAS_03 "
                            + " ) "
                            + " values("+tray.getOriOritrayCod()+ ", "+tray.getOriAsocCod()+", '"+tray.getNumExp()+"'"
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
                            + ", "+(tray.getDec327()!= null ? tray.getDec327() : "null")
                            + ", "+(tray.getMin94()!= null ? tray.getMin94() : "null")
                            + ", "+(tray.getMin98()!= null ? tray.getMin98() : "null")
                            + ", "+(tray.getTas03()!= null ? tray.getTas03() : "null")
                            + ")";
                }
                else
                {
                    //Es un registro que ya existe
                    query = "update " + establecerTablaTrayectoriasPresentadasOValidadas(sonDatosValidados)
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
                            + ", COLEC1_DEC_327 = "+(tray.getDec327() != null ? tray.getDec327() : "null")
                            + ", COLEC1_MIN_94 = "+(tray.getMin94()!= null ? tray.getMin94() : "null")
                            + ", COLEC1_MIN_98 = "+(tray.getMin98()!= null ? tray.getMin98() : "null")
                            + ", COLEC1_TAS_03 = "+(tray.getTas03()!= null ? tray.getTas03() : "null")
                            + " where ORI_ORITRAY_COD = '"+tray.getOriOritrayCod()+"'"
                            + " and ORI_ASOC_COD = "+tray.getOriAsocCod()
                            + " and ORI_ORITRAY_NUMEXP = '"+tray.getNumExp()+"'";
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
            log.error("Se ha producido un error guardando los datos de trayectoria para entidad "+(tray != null ? tray.getOriAsocCod() : "(tray = null)")+" y expediente "+(tray != null ? tray.getNumExp() : "(centro = null)"), ex);
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

    private String establecerTablaTrayectoriasPresentadasOValidadas(boolean sonDatosValidados) {
        if(sonDatosValidados)
            return ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI14_ORI_TRAYECTORIA_VAL, ConstantesMeLanbide47.FICHERO_PROPIEDADES);
        else
            return ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI14_ORI_TRAYECTORIA, ConstantesMeLanbide47.FICHERO_PROPIEDADES);
    }

    public Boolean expteEstaTramiteResolProvisional(String numExp, Connection con) throws Exception {
        log.debug("expteEstaTramiteResolProvisional- Begin() - DAO");
        Statement st = null;
        ResultSet rs = null;
        Boolean returnValue =  false;
        if (numExp != null) {
            try {
                    String query = null;
                    query = "SELECT COUNT(1) TRAMITE_RESOLPROV_ABIERTO " 
                            //+ "--EXP_PRO, EXP_EJE, EXP_NUM, EXP_MUN,E_CRO.CRO_TRA, E_CRO.CRO_FEI, E_CRO.CRO_FEF " 
                            + " FROM E_EXP " 
                            + " LEFT JOIN E_CRO ON E_CRO.CRO_MUN=E_EXP.EXP_MUN AND E_CRO.CRO_PRO=E_EXP.EXP_PRO AND E_CRO.CRO_EJE=E_EXP.EXP_EJE AND E_CRO.CRO_NUM=E_EXP.EXP_NUM " 
                            + " WHERE E_EXP.EXP_NUM='"+numExp+"'" 
                            + " AND E_CRO.CRO_TRA="+ConstantesMeLanbide47.COD_TRAMITE_RESOLUCION_PROVISIONAL
                            + " AND E_CRO.CRO_FEF IS NULL"
                            ;

                    if (log.isDebugEnabled()) {
                        log.error("sql = " + query);
                    }
                    st = con.createStatement();
                    rs = st.executeQuery(query);
                    if (rs.next()) {
                        Integer validacion = rs.getInt("TRAMITE_RESOLPROV_ABIERTO");
                        if(validacion!=null && validacion>0){
                            returnValue=true;
                        }
                    }
                } catch (Exception ex) {
                    log.error("Se ha producido una excepcion en la BBDD validandao si expediente esta en Resolucion provisional  - " + numExp, ex);
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
            log.debug("expteEstaTramiteResolProvisional- End() - DAO : " + returnValue);
        return returnValue;
    }

    public String getFechaRegistroExpediente(String numExp, String codPro, Connection con) throws Exception {
        log.debug("getFechaRegistroExpediente- Begin() - DAO");
        Statement st = null;
        ResultSet rs = null;
        String returnValue = "";
        if (numExp != null) {
            try {
                String query = null;
                query = "SELECT TO_CHAR(MIN(RES_FEC),'DD/MM/YYYY HH24:MI:SS') FECHA_REGISTRO,EXR_NUM "
                        + " FROM R_RES "
                        + " LEFT JOIN E_EXR ON EXR_NRE=R_RES.RES_NUM AND EXR_EJR=RES_EJE AND EXR_UOR=RES_UOR AND EXR_DEP=RES_DEP AND EXR_TIP=RES_TIP "
                        + " WHERE EXR_PRO='"+codPro+"'"  
                        + " AND  EXR_NUM='"+numExp+"'" 
                        + " AND EXR_TOP=0 AND R_RES.RES_TIP='E' " // Registro DE ENTRADA que genera el alta del expediente 
                        + " GROUP BY E_EXR.EXR_NUM,R_RES.RES_FEC "  
                        + " ORDER BY E_EXR.EXR_NUM, TO_DATE(FECHA_REGISTRO,'DD/MM/YYYY HH24:MI:SS') ASC "  
                        ;
                if (log.isDebugEnabled()) {
                    log.info("sql = " + query);
                }
                st = con.createStatement();
                rs = st.executeQuery(query);
                if (rs.next()) {
                    returnValue = rs.getString("FECHA_REGISTRO");
                }
            } catch (Exception ex) {
                log.error("Se ha producido una excepcion en la BBDD OBTENIENDO fecha registro   - " + numExp, ex);
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
        log.debug("getFechaRegistroExpediente- End() - DAO : " + returnValue);
        return returnValue;
    }

    public List<OriTrayOtroProgramaVO> getListaTrayectoriaOtrosProgramasxNumExp(OriTrayOtroProgramaVO trayOtro, Connection con) throws Exception {
        log.debug("getListaTrayectoriaOtrosProgramasxNumExp DAO - Begin()");
        Statement st = null;
        ResultSet rs = null;
        List<OriTrayOtroProgramaVO> listaOtrosProg = new ArrayList<OriTrayOtroProgramaVO>();
        if (trayOtro != null && trayOtro.getNumExpediente() != null && !trayOtro.getNumExpediente().equals("")) {
            try {
                String query = null;
                String nombreTablaEntidad = ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI14_ENTIDAD, ConstantesMeLanbide47.FICHERO_PROPIEDADES);
                Integer ejercicio = meLanbide47Utils.getEjercicioDeExpediente(trayOtro.getNumExpediente());
                query= "select t.*"
                        + ",DATOS_ENTIDAD.ORI_ENT_CIF "
                        + ",DATOS_ENTIDAD.ORI_ENT_NOM "
                        + " from "
                        + ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI_OTROS_PROGRAMAS, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                        + " t"
                        + " LEFT JOIN "+nombreTablaEntidad+" DATOS_ENTIDAD ON T.ORI_OTRPRO_NUMEXP=DATOS_ENTIDAD.EXT_NUM and T.ORI_OTRPRO_COD_ENTIDAD=DATOS_ENTIDAD.ORI_ENT_COD "
                        + "where t.ORI_OTRPRO_NUMEXP='"+ trayOtro.getNumExpediente()+ "' and t.ORI_OTRPRO_EXP_EJE="+ejercicio;
                 if (log.isDebugEnabled()) {
                    log.error("sql = " + query);
                }
                st = con.createStatement();
                rs = st.executeQuery(query);
                while (rs.next()){                       
                       try {
                        listaOtrosProg.add(((OriTrayOtroProgramaVO) MeLanbide47MappingUtils.getInstance().map(rs, OriTrayOtroProgramaVO.class)));
                    } catch (Exception ex) {
                         log.debug("Error al mapear la OriTrayOtroProgramaVO ", ex);
                    }
                }
            } catch (Exception ex) {
                   log.error("Se ha producido una excepcion en la BBDD recuperando trayectorias para el expediente " + (trayOtro != null ? trayOtro.getNumExpediente(): " (trayOtro = null)"), ex);              
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
        return listaOtrosProg;
    }

    public OriTrayOtroProgramaVO getTrayectoriaOtrosProgramas(OriTrayOtroProgramaVO tray, Connection con) throws Exception{
        log.debug("getTrayectorialOtrosProgramas - DAO - Begin()");
        Statement st = null;
        ResultSet rs = null;
        OriTrayOtroProgramaVO trayectoria = new OriTrayOtroProgramaVO();
        if (tray !=null && tray.getNumExpediente() != null && !tray.getNumExpediente().equals("")){
            try {
                String query = null;
                query = "select t.* "
                            + ",DATOS_ENTIDAD.ORI_ENT_CIF "
                        + ",DATOS_ENTIDAD.ORI_ENT_NOM "
                        + " from "
                         + ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI_OTROS_PROGRAMAS, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                        + " t"
                        +" LEFT JOIN "
                        + ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI14_ENTIDAD, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                        + " DATOS_ENTIDAD ON T.ORI_OTRPRO_NUMEXP=DATOS_ENTIDAD.EXT_NUM AND T.ORI_OTRPRO_COD_ENTIDAD=DATOS_ENTIDAD.ORI_ENT_COD "
                        + " where t.ORI_OTRPRO_NUMEXP='"+tray.getNumExpediente()+"'"
                        +" and t.ORI_OTRPRO_COD_ENTIDAD="+tray.getCodEntidad()
                        + " and t.ORI_OTRPRO_ID="+tray.getCodIdOtroPrograma()
                        ;
                if (log.isDebugEnabled()) {
                    log.error("sql = " + query);
                }
                 st = con.createStatement();
                rs = st.executeQuery(query);
                while (rs.next()){
                    try {
                      trayectoria=((OriTrayOtroProgramaVO) MeLanbide47MappingUtils.getInstance().map(rs, OriTrayOtroProgramaVO.class));
                    } catch (Exception ex) {
                        log.error("Error al mapear OriTrayOtroPrograma ", ex);
                    }
                }
            } catch (Exception ex) {
                 log.error("Se ha producido una excepcion en la BBDD recuperando trayectorias otros programas para el expediente " + (tray != null ? tray.getNumExpediente() : " (tray = null)"), ex);
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

    public List<SelectItem> getListaSelectItemEntidadesPorExpediente(String numExpediente, Connection con) throws Exception
    {
        
        
      Statement st = null;
        ResultSet rs = null;
        List<SelectItem> entidades = new ArrayList<SelectItem>();
        EntidadVO ent = new EntidadVO();
        try
        {
            // Leemos la entidad principal, para saber que listar
            ent.setExtNum(numExpediente);
            ent = getEntidadInteresadaPorExpediente(ent, con);
            if(ent!=null){
                 String nombretabla = ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI14_ENTIDAD, ConstantesMeLanbide47.FICHERO_PROPIEDADES);
                 String query= "SELECT ORI_ENT_COD As CODIGO, "
                         + "ORI_ENT_CIF || ' - ' || nvl(ORI_ENT_NOM, '') AS DESCRIPCION "
                         + " from "+nombretabla
                         + " WHERE EXT_NUM='"+numExpediente+"'"
                         ;
                
                if (log.isDebugEnabled()) {
                    log.error("sql = " + query);
                }
                st = con.createStatement();
                rs = st.executeQuery(query);
                Integer cod;
                String desc;
                SelectItem si;
                while (rs.next()) {
                   // entidades.add((SelectItem) MeLanbide47MappingUtils.getInstance().map(rs, SelectItem.class));
                   cod=rs.getInt("CODIGO");
                   desc=rs.getString("DESCRIPCION");
                   si=new SelectItem();
                   si.setId(cod);
                   si.setLabel(desc);
                   entidades.add(si);
                }
            }            
            
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
       return entidades;   
    }

    private EntidadVO getEntidadInteresadaPorExpediente(EntidadVO p, Connection con) throws Exception
    {
       if(p !=null && p.getExtNum()!=null)
       {
            Statement st = null;
            ResultSet rs = null;
            EntidadVO entidad = null;
            try
            {
                String query = "SELECT * "
                        + " FROM "+ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI14_ENTIDAD, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                        + " WHERE EXT_NUM='"+p.getExtNum()+"'"
                        ;
                if(log.isDebugEnabled()) 
                    log.error("sql = " + query);
                st = con.createStatement();
                rs = st.executeQuery(query);
                if(rs.next())
                {
                    entidad = (EntidadVO)MeLanbide47MappingUtils.getInstance().map(rs, EntidadVO.class);
                }
            return entidad;
            }
             catch(Exception ex)
            {
                log.error("Se ha producido un error recuperando entidad "+p.getOriEntCod()+" para expediente "+ p.getExtNum(), ex);
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

    public OriTrayOtroProgramaVO guardarTrayectoriaOtrosProgramas(OriTrayOtroProgramaVO tray, Connection con) throws Exception  {
        log.debug("guardarTrayectoriaGeneralOtrosProgramas - DAO - Begin()");
        Statement st = null;
        try {
            if (tray.getCodEntidad() == null || (tray.getNumExpediente()== null || tray.getNumExpediente().equals("")) || tray.getEjercicio()== null) {
                log.error("Se ha producido un error guardando los datos de trayectoria  para entidad " + (tray != null ? tray.getCodEntidad() : "(tray = null)") + " y expediente " + (tray != null ? tray.getNumExpediente(): "(centro = null)"));
                throw new Exception();
            }else{
                String query = null;
                if(tray.getCodIdOtroPrograma()==null){
                    Long codTray = this.getNextId(ConfigurationParameter.getParameter(ConstantesMeLanbide47.SEQ_ORI_OTROS_PROGRAMAS,ConstantesMeLanbide47.FICHERO_PROPIEDADES), con);
                    if (codTray == null){
                        throw new Exception();
                    }
                    tray.setCodIdOtroPrograma(Integer.valueOf(codTray.toString()));
                    
                    //Es un registro nuevo
                    query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI_OTROS_PROGRAMAS, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                            +"(ORI_OTRPRO_ID"
                            +",ORI_OTRPRO_EXP_EJE"
                            +",ORI_OTRPRO_NUMEXP"
                            +",ORI_OTRPRO_COD_ENTIDAD"
                            +",ORI_OTRPRO_PROGRAMA"
                            +",ORI_OTRPRO_PROG_EJE"
                            +",ORI_OTRPRO_DURACION"
                            +",ORI_OTRPRO_PROG_EJE_VALID"
                            +",ORI_OTRPRO_DURACION_VALID"
                            +")"
                            + " VALUES("+tray.getCodIdOtroPrograma()
                            +", "+tray.getEjercicio()
                            +", '"+tray.getNumExpediente()+"'"
                            +", "+tray.getCodEntidad()
                            +", '"+tray.getPrograma()+"'"
                            +", "+tray.getAnioPrograma()
                            +", "+tray.getDuracion()
                            +", "+(tray.getAnioProgramaVal() !=null ? tray.getAnioProgramaVal() : "null")
                            +", "+(tray.getDuracionVal() !=null ? tray.getDuracionVal() : "null")
                            +")";
       
                }else{
                //Es un registro que ya existe
                query = "update "+ ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI_OTROS_PROGRAMAS, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                        +" set"  
                        +" ORI_OTRPRO_COD_ENTIDAD="+(tray.getCodEntidad()!= null ? tray.getCodEntidad() : "null")
                        +", ORI_OTRPRO_PROGRAMA='"+tray.getPrograma()+"'"
                        +", ORI_OTRPRO_PROG_EJE="+tray.getAnioPrograma()
                        +", ORI_OTRPRO_DURACION="+tray.getDuracion()
                        +", ORI_OTRPRO_PROG_EJE_VALID="+(tray.getAnioProgramaVal() !=null ? tray.getAnioProgramaVal() : "null")
                        +", ORI_OTRPRO_DURACION_VALID="+(tray.getDuracionVal() !=null ? tray.getDuracionVal() : "null")
                        +" where ORI_OTRPRO_EXP_EJE="+tray.getEjercicio()
                        + " and ORI_OTRPRO_NUMEXP='"+tray.getNumExpediente()+"'"
                        +" and ORI_OTRPRO_ID="+tray.getCodIdOtroPrograma()
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
         log.error("Se ha producido un error guardando los datos de trayectoria otros programas para entidad " + (tray != null ? tray.getCodEntidad() : "(tray = null)") + " y expediente " + (tray != null ? tray.getNumExpediente(): "(centro = null)"), ex);
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

    public int eliminarTrayectoriaOtrosProgramas(OriTrayOtroProgramaVO tray, Connection con) throws Exception{
        log.debug("eliminarTrayectoriaOtrosProgramas - Begin()");
        Statement st = null;
        int result = 0;
        try {
            String query = null;
            query = "delete from "+ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI_OTROS_PROGRAMAS, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                    +" where"
                    +" ORI_OTRPRO_ID="+tray.getCodIdOtroPrograma()
                    +" and ORI_OTRPRO_NUMEXP='"+tray.getNumExpediente()+"'"
                    +" and ORI_OTRPRO_EXP_EJE="+tray.getEjercicio()
                    +" and ORI_OTRPRO_COD_ENTIDAD="+tray.getCodEntidad();
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

    public List<OriTrayActividadVO> getListaTrayectoriaActividadesXNumExp(OriTrayActividadVO trayActividad, Connection con) throws Exception{
         log.debug("getListaTrayectorialActividadesxNumExp - DAO - Begin()");
        Statement st = null;
        ResultSet rs = null;
        List<OriTrayActividadVO> listaTrayectorias = new ArrayList<OriTrayActividadVO>();
        if (trayActividad != null && trayActividad.getNumExpediente()!= null && !trayActividad.getNumExpediente().equals("")) {
            try {
                String query = null;
                String nombreTablaEntidad = ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI14_ENTIDAD, ConstantesMeLanbide47.FICHERO_PROPIEDADES);
                Integer ejercicio=meLanbide47Utils.getEjercicioDeExpediente(trayActividad.getNumExpediente());
                query = "select t.* "
                        + ",DATOS_ENTIDAD.ORI_ENT_CIF "
                        + ",DATOS_ENTIDAD.ORI_ENT_NOM "
                        + " from "
                        + ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI_ACTIVIDADES, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                        +" T "
                        + "LEFT JOIN "+nombreTablaEntidad+ " DATOS_ENTIDAD ON T.ORI_ACTIV_NUMEXP=DATOS_ENTIDAD.EXT_NUM and T.ORI_ACTIV_COD_ENTIDAD=DATOS_ENTIDAD.ORI_ENT_COD "
                        + " where T.ORI_ACTIV_NUMEXP='"+trayActividad.getNumExpediente()+"' and t.ORI_ACTIV_EXP_EJE="+ejercicio;
                if (log.isDebugEnabled()) {
                    log.error("sql = " + query);
                }
                st = con.createStatement();
                rs = st.executeQuery(query);
                while(rs.next()){
                    try {
                        listaTrayectorias.add(((OriTrayActividadVO) MeLanbide47MappingUtils.getInstance().map(rs, OriTrayActividadVO.class)));
                    } catch (Exception ex) {
                         log.debug("Error al mapear la OriTrayActividadVO ", ex);
                    }
                }
            } catch (Exception ex) {
                log.error("Se ha producido una excepcion en la BBDD recuperando trayectorias para el expediente " + (trayActividad != null ? trayActividad.getNumExpediente(): " (trayActividad = null)"), ex);
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

    public OriTrayActividadVO getTrayectoriaGeneralActividades(OriTrayActividadVO tray, Connection con) throws Exception {
        log.debug("getTrayectoriaGeneralActividades - Begin()");
        Statement st = null;
        ResultSet rs = null;
        OriTrayActividadVO trayectoria = new OriTrayActividadVO();
        if(tray != null && tray.getNumExpediente() != null && !tray.getNumExpediente().equals("")){
            try {
                String query = null;
                query= "select t.* "
                         + ",DATOS_ENTIDAD.ORI_ENT_CIF "
                        + ",DATOS_ENTIDAD.ORI_ENT_NOM "
                        + " from "
                        + ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI_ACTIVIDADES, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                        + " t "
                        + " LEFT JOIN ORI14_ENTIDAD DATOS_ENTIDAD ON t.ORI_ACTIV_NUMEXP=DATOS_ENTIDAD.EXT_NUM and t.ORI_ACTIV_COD_ENTIDAD=DATOS_ENTIDAD.ORI_ENT_COD "
                        + " where t.ORI_ACTIV_NUMEXP='"+tray.getNumExpediente() + "'"
                        + " and t.ORI_ACTIV_COD_ENTIDAD="+tray.getCodEntidad()
                        + " and t.ORI_ACTIV_ID="+tray.getCodIdActividad()
                        ;
                if (log.isDebugEnabled()) {
                    log.error("sql = " + query);
                }
                st = con.createStatement();
                rs = st.executeQuery(query);
                while (rs.next()) {
                    try {
                        trayectoria=((OriTrayActividadVO) MeLanbide47MappingUtils.getInstance().map(rs, OriTrayActividadVO.class));
                    } catch (Exception ex) {
                        log.debug("Error al mapear la OriTrayActividadVO ", ex);
                    }
                }
            } catch (Exception ex) {
                log.error("Se ha producido una excepcion en la BBDD recuperando trayectorias actividades para el expediente " + (tray != null ? tray.getNumExpediente() : " (tray = null)"), ex);
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

    public int eliminarTrayectoriaActividades(OriTrayActividadVO tray, Connection con)throws Exception  {
        log.debug("eliminarTrayectoriaActividades - DAO - Begin()");
        Statement st = null;
        int result = 0;
        try {
            String query=null;
            query = "delete from "  + ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI_ACTIVIDADES, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                    +" where "
                    +" ORI_ACTIV_ID="+tray.getCodIdActividad()
                    +" and ORI_ACTIV_NUMEXP='"+tray.getNumExpediente() + "'"
                    +" and ORI_ACTIV_COD_ENTIDAD="+ tray.getCodEntidad()
                    ;
            if (log.isDebugEnabled()) {
                log.error("sql = " + query);
            }
            st = con.createStatement();
            result = st.executeUpdate(query);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD eliminando trayectoria actividades ", ex);
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
        log.debug("eliminarTrayectoriaActividades -DAO - End()" + result);
        return result;
    }

    public OriTrayActividadVO guardarTrayectoriaActividades(OriTrayActividadVO tray, Connection con)throws Exception{
        log.debug("guardarTrayectoriaActividades - Begin()");
        Statement st = null;
        try{
            if(tray.getCodEntidad() == null || (tray.getNumExpediente() == null || tray.getNumExpediente().equals("")) || tray.getEjercicio() == null){
            log.error("Se ha producido un error guardando los datos de trayectoria actividades para entidad " + (tray != null ? tray.getCodEntidad() : "(tray = null)") + " y expediente " + (tray != null ? tray.getNumExpediente(): "(centro = null)"));
                    throw new Exception();
            }else{
                
                String query = null;
                if (tray.getCodIdActividad()== null) {
                    Long codTray = this.getNextId(ConfigurationParameter.getParameter(ConstantesMeLanbide47.SEQ_ORI_ACTIVIDADES, ConstantesMeLanbide47.FICHERO_PROPIEDADES), con);
                    if (codTray == null) {
                        throw new Exception();
                    }
                    //Es un registro nuevo
                    tray.setCodIdActividad(Integer.valueOf(codTray.toString()));
                    query= "insert into "+ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI_ACTIVIDADES, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                            +" (ORI_ACTIV_ID"
                            +", ORI_ACTIV_EXP_EJE"
                            +", ORI_ACTIV_NUMEXP"
                            +", ORI_ACTIV_COD_ENTIDAD"
                            +", ORI_ACTIV_ACTIVIDAD"                                                       
                            +", ORI_ACTIV_ACTIVIDAD_EJE"
                            +", ORI_ACTIV_DURACION"
                            +", ORI_ACTIV_ACTIVIDAD_EJE_VALID"
                            +", ORI_ACTIV_DURACION_VALID"
                            +")"
                            +" values("+ tray.getCodIdActividad()
                            + ", " + tray.getEjercicio() + ""
                            + ", '" + tray.getNumExpediente() + "'"
                            + ", " + tray.getCodEntidad()
                            + ", "+ (tray.getDescActividad()!=null && !tray.getDescActividad().equals("")?"'"+tray.getDescActividad()+"'":"null")
                            + ", " + tray.getEjerActividad()
                            + ", " + tray.getDuracion()
                            + ", " + (tray.getEjerActividadVal()!=null ? tray.getEjerActividadVal() : "null" )
                            + ", " + (tray.getDuracionVal() != null ? tray.getDuracionVal() : "null")
                            +")"
                            ;
                }else{ //Es un registro que ya existe
                   query = "update " +ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI_ACTIVIDADES, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                           + " set "
                           + " ORI_ACTIV_ACTIVIDAD='" +tray.getDescActividad()+"'"
                           + ", ORI_ACTIV_ACTIVIDAD_EJE="+tray.getEjerActividad()
                           + ", ORI_ACTIV_DURACION="+ tray.getDuracion()
                           + ", ORI_ACTIV_ACTIVIDAD_EJE_VALID="+ (tray.getEjerActividadVal()!=null ? tray.getEjerActividadVal() : "null" )
                           + ", ORI_ACTIV_DURACION_VALID="+ (tray.getDuracionVal() != null ? tray.getDuracionVal() : "null")
                           + " where ORI_ACTIV_EXP_EJE="+tray.getEjercicio()
                           + " and ORI_ACTIV_NUMEXP='"+tray.getNumExpediente()+"'"
                           + " and ORI_ACTIV_ID="+tray.getCodIdActividad()
                           ;
                }
                if (log.isDebugEnabled()) {
                    log.error("sql = " + query);
                }
                st = con.createStatement();
                int res = st.executeUpdate(query);
                 if (res > 0) {
                     log.debug("guardo "+ res + "registros");
                    return tray;
                } else {
                    log.error("Se ha producido un error guardando los datos de trayectoria  actividades para entidad " + (tray != null ? tray.getCodEntidad() : "(tray = null)") + " y expediente " + (tray != null ? tray.getNumExpediente(): "(centro = null)"));
                    throw new Exception();
                }
            }
        }catch (Exception ex) {
            log.error("Se ha producido un error guardando los datos de trayectoria  Actividades para entidad " + (tray != null ? tray.getCodEntidad() : "(tray = null)") + " y expediente " + (tray != null ? tray.getNumExpediente(): "(centro = null)"), ex);
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

    public List<FilaOriAmbitoSolicitadoVO> getAmbitosSolicitadosORI(String numExpediente, Connection con) throws Exception {
        List<FilaOriAmbitoSolicitadoVO> retList = new ArrayList<FilaOriAmbitoSolicitadoVO>();
        Statement st = null;
        ResultSet rs = null;
        try {
            String query = "select * "
                    + " from " + ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI_AMBITOS_SOLICITADOS, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                    + " where ORI_AMB_SOL_NUM_EXP ='" + numExpediente + "'"
                    + " order by ORI_AMB_SOL_TERHIS, ORI_AMB_SOL_AMBITO,ORI_AMB_SOL_COD ";
            log.info("sql: " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                FilaOriAmbitoSolicitadoVO ambitoSolicitadoVO = (FilaOriAmbitoSolicitadoVO) MeLanbide47MappingUtils.getInstance().map(rs, FilaOriAmbitoSolicitadoVO.class);
                //Rellenamos las descripciones de Ambito y Provincia
                ProvinciaVO provinciaVO = getProvinciaPorCodigo(ambitoSolicitadoVO.getOriAmbSolTerHis(), con);
                AmbitosHorasVO ambitoVO = getAmbitoHorasPorCodigo(ambitoSolicitadoVO.getOriAmbSolAmbito(), con);
                
                ambitoSolicitadoVO.setOriAmbSolTerHisDesc((provinciaVO!=null?provinciaVO.getPrvNom():""));
                ambitoSolicitadoVO.setOriAmbSolAmbitoDesc((ambitoVO!=null?ambitoVO.getOriAmbAmbito():""));
                retList.add(ambitoSolicitadoVO);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error getAmbitosSolicitadosORI ", ex);
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

    public OriAmbitoSolicitadoVO guardarAmbitoSolicitadoORI(OriAmbitoSolicitadoVO ambitSol, Connection con) throws Exception {
        log.info("guardarAmbitoSolicitadoORI - Begin()");
        Statement st = null;
        try {
            if (ambitSol== null ||ambitSol.getOriAmbSolNumExp()== null) {
                log.error("Se ha producido un error guardando un Ambito Solicitado. NO han llegado los datos de el expediente.");
                throw new Exception("Se ha producido un error guardando un Ambito Solicitado. NO han llegado los datos de el expediente.");
            } else {
                String query = null;
                if (ambitSol.getOriAmbSolCod()== null) {
                    Long codTray = this.getNextId(ConfigurationParameter.getParameter(ConstantesMeLanbide47.SEQ_ORI14_AMBITOS_SOLICITAD, ConstantesMeLanbide47.FICHERO_PROPIEDADES), con);
                    if (codTray == null) {
                        throw new Exception("Error al recuperar el ID consecutivo dela secuencia SEQ_ORI14_AMBITOS_SOLICITAD para guardar un AmbitoSolicitado");
                    }
                    //Es un registro nuevo
                    ambitSol.setOriAmbSolCod(Integer.valueOf(codTray.toString()));
                    query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI_AMBITOS_SOLICITADOS, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                            + " (ORI_AMB_SOL_COD"
                            + ", ORI_AMB_SOL_NUM_EXP"
                            + ", ORI_AMB_SOL_TERHIS"
                            + ", ORI_AMB_SOL_AMBITO"
                            + ", ORI_AMB_SOL_NRO_BLOQUES"
                            + ", ORI_AMB_SOL_NRO_UBIC"
                            + ")"
                            + " values(" + ambitSol.getOriAmbSolCod()
                            + ", '" + ambitSol.getOriAmbSolNumExp()+ "'"
                            + ", " + (ambitSol.getOriAmbSolTerHis() != null ? ambitSol.getOriAmbSolTerHis() : "null")
                            + ", " + (ambitSol.getOriAmbSolAmbito()!= null ? ambitSol.getOriAmbSolAmbito() : "null")
                            + ", " + (ambitSol.getOriAmbSolNroBloques()!= null ? ambitSol.getOriAmbSolNroBloques() : "null")
                            + ", " + (ambitSol.getOriAmbSolNroUbic()!= null ? ambitSol.getOriAmbSolNroUbic() : "null")
                            + ")";
                } else { //Es un registro que ya existe
                    query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI_AMBITOS_SOLICITADOS, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                            + " set "
                            + " ORI_AMB_SOL_TERHIS=" + (ambitSol.getOriAmbSolTerHis() != null ? ambitSol.getOriAmbSolTerHis() : "null")
                            + ", ORI_AMB_SOL_AMBITO=" + (ambitSol.getOriAmbSolAmbito()!= null ? ambitSol.getOriAmbSolAmbito() : "null")
                            + ", ORI_AMB_SOL_NRO_BLOQUES=" + (ambitSol.getOriAmbSolNroBloques()!= null ? ambitSol.getOriAmbSolNroBloques() : "null")
                            + ", ORI_AMB_SOL_NRO_UBIC=" + (ambitSol.getOriAmbSolNroUbic()!= null ? ambitSol.getOriAmbSolNroUbic() : "null")
                            + " where ORI_AMB_SOL_COD=" + ambitSol.getOriAmbSolCod()
                            + " and ORI_AMB_SOL_NUM_EXP='" + ambitSol.getOriAmbSolNumExp() + "'"
                            ;
                }
                log.info("sql = " + query);
                st = con.createStatement();
                int res = st.executeUpdate(query);
                if (res > 0) {
                    log.debug("guardo " + res + "registros");
                    return ambitSol;
                } else {
                    log.error("Se ha producido un error guardando un Ambito Solicitado " + (ambitSol != null ? ambitSol.getOriAmbSolNumExp():" Sin Datos de Numero de Expediente."));
                    throw new Exception("No se ha podido actualizar ningun valor en la tabla ORI_AMBITOS_SOLICITADOS");
                }
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error guardando un Ambito Solicitado " +  (ambitSol != null ? ambitSol.getOriAmbSolNumExp():" Sin Datos de Numero de Expediente."), ex);
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

    public int eliminarAmbitoSolicitadoORI(OriAmbitoSolicitadoVO ambitSol, Connection con) throws Exception {
        log.info("eliminarAmbitoSolicitadoORI - DAO - Begin()");
        Statement st = null;
        int result = 0;
        try {
            String query = null;
            query = "delete from " + ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI_AMBITOS_SOLICITADOS, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                    + " where "
                    + " ORI_AMB_SOL_COD=" + ambitSol.getOriAmbSolCod()
                    + " and ORI_AMB_SOL_NUM_EXP='" + ambitSol.getOriAmbSolNumExp()+ "'"
                    ;
            log.info("sql = " + query);
            st = con.createStatement();
            result = st.executeUpdate(query);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD eliminarAmbitoSolicitadoORI ", ex);
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
        log.debug("eliminarAmbitoSolicitadoORI - DAO - End() : " + result);
        return result;
    }

    public OriAmbitoSolicitadoVO getAmbitoSolicitadoORIPorCodigo(OriAmbitoSolicitadoVO ambiSol, Connection con) 
            throws Exception{
        OriAmbitoSolicitadoVO retList = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            String query = "select * "
                    + " from " + ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI_AMBITOS_SOLICITADOS, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                    + " where ORI_AMB_SOL_COD =" + (ambiSol!=null && ambiSol.getOriAmbSolCod()!=null ? ambiSol.getOriAmbSolCod() : 0)
                    + " order by ORI_AMB_SOL_TERHIS, ORI_AMB_SOL_AMBITO,ORI_AMB_SOL_COD ";
            log.info("sql: " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                retList= (OriAmbitoSolicitadoVO) MeLanbide47MappingUtils.getInstance().map(rs,OriAmbitoSolicitadoVO.class);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error getAmbitoSolicitadoORIPorCodigo ", ex);
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
    
    public BigDecimal getNumBloquesSoliAmbitoSolicitadoORIPorNumExpCodAmbito(String numExpediente,Integer codigoAmbito, Connection con) 
            throws Exception{
        log.info("getNumBloquesSoliAmbitoSolicitadoORIPorNumExpCodAmbito - Begin() " + numExpediente + "/" +codigoAmbito);
        BigDecimal retList = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            String query = "select * "
                    + " from " + ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI_AMBITOS_SOLICITADOS, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                    + " where ORI_AMB_SOL_NUM_EXP='"+numExpediente+ "'" 
                    + " AND ORI_AMB_SOL_AMBITO =" + (codigoAmbito!=null? codigoAmbito : "NULL") 
                    + " order by ORI_AMB_SOL_TERHIS, ORI_AMB_SOL_AMBITO,ORI_AMB_SOL_COD ";
            log.info("sql: " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                retList= (retList != null ? retList.add(rs.getBigDecimal("ORI_AMB_SOL_NRO_BLOQUES")):rs.getBigDecimal("ORI_AMB_SOL_NRO_BLOQUES"));
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error getNumBloquesSoliAmbitoSolicitadoORIPorNumExpCodAmbito ", ex);
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
        log.info("getNumBloquesSoliAmbitoSolicitadoORIPorNumExpCodAmbito - End() " + retList);
        return retList;
    }
    public BigDecimal getNumUbicacinoesEnAmbitoSolicitadoORIPorNumExpCodAmbito(String numExpediente,Integer codigoAmbito, Connection con) 
            throws Exception{
        log.info("getNumUbicacinoesEnAmbitoSolicitadoORIPorNumExpCodAmbito - Begin() " + numExpediente + "/" +codigoAmbito);
        BigDecimal retList = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            String query = "select * "
                    + " from " + ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI_AMBITOS_SOLICITADOS, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                    + " where ORI_AMB_SOL_NUM_EXP='"+numExpediente+ "'" 
                    + " AND ORI_AMB_SOL_AMBITO =" + (codigoAmbito!=null? codigoAmbito : "NULL") 
                    + " order by ORI_AMB_SOL_TERHIS, ORI_AMB_SOL_AMBITO,ORI_AMB_SOL_COD ";
            log.info("sql: " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                
                retList =  (retList!=null ? retList.add(rs.getBigDecimal("ORI_AMB_SOL_NRO_UBIC")):rs.getBigDecimal("ORI_AMB_SOL_NRO_UBIC"));
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error getNumUbicacinoesEnAmbitoSolicitadoORIPorNumExpCodAmbito ", ex);
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
        log.info("getNumUbicacinoesEnAmbitoSolicitadoORIPorNumExpCodAmbito - End() " + retList);
        return retList;
    }

    public Date getFechaAltaExpediente(Integer codOrganizacion, String numExpediente, Connection con) throws Exception {
        log.debug("getFechaAltaExpediente - Begin() ");
        Date respuesta = null;
        Statement st = null;
        ResultSet rs = null;
        if (numExpediente != null) {
            try {
                String query = " SELECT exp_fei from e_exp "
                        + " where exp_mun=" + (codOrganizacion != null ? codOrganizacion : "null")
                        + " and exp_num='" + numExpediente + "'";
                if (log.isDebugEnabled()) {
                    log.error("sql = " + query);
                }
                st = con.createStatement();
                rs = st.executeQuery(query);
                if (rs.next()) {
                    respuesta = rs.getDate("exp_fei");
                }
            } catch (SQLException ex) {
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

    /**
     *
     * @param numExpediente
     * @param con
     * @return
     * @throws Exception
     */
    public List<OriTrayectoriaEntidadVO> getTotalTrayectoriaEntidadXExp(String numExpediente, Connection con) throws Exception {
        log.info("getTotalTrayectoriaEntidadXExp - DAO - Begin()");
        Statement st = null;
        ResultSet rs = null;
        List<OriTrayectoriaEntidadVO> listaResumen = new ArrayList<OriTrayectoriaEntidadVO>();
        try {
            String query = null;
            String tablaTrayectoriaEntidad = ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI_TRAYECTORIA_ENTIDAD, ConstantesMeLanbide47.FICHERO_PROPIEDADES);
            query = "SELECT TRAYECTORIAS.*"
                    + " FROM " + tablaTrayectoriaEntidad + "  TRAYECTORIAS"
                    + " WHERE TRAYECTORIAS.TRAYNUMEXPEDIENTE = '" + numExpediente + "'"
                    + " ORDER BY TRAYECTORIAS.TRAYIDFKORIPROGCONVACTGRUPO, TRAYECTORIAS.ID";

            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                try {
                    listaResumen.add(((OriTrayectoriaEntidadVO) MeLanbide47MappingUtils.getInstance().map(rs, OriTrayectoriaEntidadVO.class)));
                } catch (Exception ex) {
                    log.error("Error al mapear la OriTrayectoriaEntidadVO ", ex);
                }
            }
        } catch (SQLException ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Programas / Actividades (trayectoriaEntidad) para el expediente " + (numExpediente != null ? numExpediente : " (numExpediente = null)"), ex);
            throw new Exception(ex);
        } finally {
            try {
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
        return listaResumen;
    }

    /**
     *
     * @param trayEntidad
     * @param con
     * @return listaProgAct: lista de Programas/Actividades del expediente
     * @throws Exception
     */
    public List<OriTrayectoriaEntidadVO> getListaTrayectoriaEntidadXGrupo(OriTrayectoriaEntidadVO trayEntidad, Connection con) throws Exception {
        log.info("getListaTrayectoriaEntidadXGrupo - DAO - Begin()");
        Statement st = null;
        ResultSet rs = null;
        List<OriTrayectoriaEntidadVO> listaProgAct = new ArrayList<OriTrayectoriaEntidadVO>();
        if (trayEntidad != null && trayEntidad.getNumExpediente() != null && !trayEntidad.getNumExpediente().equals("")) {
            try {
                String query = null;
                String tablaTrayectoriaEntidad = ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI_TRAYECTORIA_ENTIDAD, ConstantesMeLanbide47.FICHERO_PROPIEDADES);
                String tablaEntidades = ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI14_ENTIDAD, ConstantesMeLanbide47.FICHERO_PROPIEDADES);
                query = "SELECT TRAYECTORIAS.*, DATOS_ENTIDAD.ORI_ENT_CIF, DATOS_ENTIDAD.ORI_ENT_NOM"
                        + " FROM " + tablaTrayectoriaEntidad + "  TRAYECTORIAS"
                        + " LEFT JOIN " + tablaEntidades + " DATOS_ENTIDAD ON TRAYECTORIAS.TRAYCODIGOENTIDAD = DATOS_ENTIDAD.ORI_ENT_COD AND TRAYECTORIAS.TRAYNUMEXPEDIENTE = DATOS_ENTIDAD.EXT_NUM "
                        + " WHERE TRAYECTORIAS.TRAYIDFKORIPROGCONVACTGRUPO = " + trayEntidad.getIdConActGrupo()
                        + " AND TRAYECTORIAS.TRAYNUMEXPEDIENTE = '" + trayEntidad.getNumExpediente() + "'"
                        + " ORDER BY TRAYECTORIAS.ID";

                if (log.isDebugEnabled()) {
                    log.debug("sql = " + query);
                }
                st = con.createStatement();
                rs = st.executeQuery(query);
                while (rs.next()) {
                    try {
                        listaProgAct.add(((OriTrayectoriaEntidadVO) MeLanbide47MappingUtils.getInstance().map(rs, OriTrayectoriaEntidadVO.class)));
                    } catch (Exception ex) {
                        log.error("Error al mapear la OriTrayectoriaEntidadVO ", ex);
                    }
                }
            } catch (SQLException ex) {
                log.error("Se ha producido una excepcion en la BBDD recuperando Programas / Actividades (trayectoriaEntidad) para el expediente " + (trayEntidad != null ? trayEntidad.getNumExpediente() : " (trayectoriaEntidad = null)"), ex);
                throw new Exception(ex);
            } finally {
                try {
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
        return listaProgAct;
    }

    /**
     *
     * @param trayEntidad
     * @param con
     * @return progAct: OriTrayectoriaEntidadVO
     * @throws Exception
     */
    public OriTrayectoriaEntidadVO getTrayEntidadXId(OriTrayectoriaEntidadVO trayEntidad, Connection con) throws Exception {
        log.debug("getTrayEntidad - Begin()");
        Statement st = null;
        ResultSet rs = null;
        OriTrayectoriaEntidadVO progAct = new OriTrayectoriaEntidadVO();
        String tablaTrayectoriaEntidad = ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI_TRAYECTORIA_ENTIDAD, ConstantesMeLanbide47.FICHERO_PROPIEDADES);
        String tablaEntidades = ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI14_ENTIDAD, ConstantesMeLanbide47.FICHERO_PROPIEDADES);

        if (trayEntidad != null && trayEntidad.getNumExpediente() != null && !trayEntidad.getNumExpediente().equals("")) {
            try {
                String query = null;
                query = "SELECT TRAYECTORIAS.*, DATOS_ENTIDAD.ORI_ENT_CIF, DATOS_ENTIDAD.ORI_ENT_NOM"
                        + " FROM " + tablaTrayectoriaEntidad + "  TRAYECTORIAS"
                        + " LEFT JOIN " + tablaEntidades + " DATOS_ENTIDAD ON TRAYECTORIAS.TRAYCODIGOENTIDAD = DATOS_ENTIDAD.ORI_ENT_COD AND TRAYECTORIAS.TRAYNUMEXPEDIENTE = DATOS_ENTIDAD.EXT_NUM "
                        + " WHERE TRAYECTORIAS.TRAYIDFKORIPROGCONVACTGRUPO = " + trayEntidad.getIdConActGrupo()
                        + " AND TRAYECTORIAS.TRAYNUMEXPEDIENTE = '" + trayEntidad.getNumExpediente() + "'"
                        + " and TRAYECTORIAS.TRAYCODIGOENTIDAD =" + trayEntidad.getCodEntidad()
                        + " AND TRAYECTORIAS.ID=" + trayEntidad.getIdTrayEntidad()
                        + " ORDER BY TRAYECTORIAS.ID";

                if (log.isDebugEnabled()) {
                    log.debug("sql = " + query);
                }
                st = con.createStatement();
                rs = st.executeQuery(query);
                while (rs.next()) {
                    try {
                        progAct = ((OriTrayectoriaEntidadVO) MeLanbide47MappingUtils.getInstance().map(rs, OriTrayectoriaEntidadVO.class));
                    } catch (Exception ex) {
                        log.error("Error al mapear la OriTrayectoriaEntidadVO (programa/actividad) ", ex);
                    }
                }
            } catch (SQLException ex) {
                log.error("Se ha producido una excepcion en la BBDD recuperando programas/actividades para el expediente " + (progAct != null ? progAct.getNumExpediente() : " (activ = null)"), ex);
                throw new Exception(ex);
            } finally {
                try {
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
        return progAct;
    }

    /**
     *
     * @param trayEntidad
     * @param con
     * @return 0/1
     * @throws Exception
     */
    public int eliminarTrayEntidad(OriTrayectoriaEntidadVO trayEntidad, Connection con) throws Exception {
        log.info("eliminarTrayEntidad - DAO - Begin()");
        Statement st = null;
        int result = 0;
        try {
            String query = null;
            query = "delete from " + ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI_TRAYECTORIA_ENTIDAD, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                    + " where "
                    + " ID=" + trayEntidad.getIdTrayEntidad()
                    + " and TRAYNUMEXPEDIENTE='" + trayEntidad.getNumExpediente() + "'"
                    + " and TRAYCODIGOENTIDAD=" + trayEntidad.getCodEntidad()
                    + " AND TRAYIDFKORIPROGCONVACTGRUPO = " + trayEntidad.getIdConActGrupo();
            if (log.isDebugEnabled()) {
                log.error("sql = " + query);
            }
            st = con.createStatement();
            result = st.executeUpdate(query);
        } catch (SQLException ex) {
            log.error("Se ha producido una excepcion en la BBDD eliminando trayectoria actividades ", ex);
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
        log.debug("eliminarTrayectoriaActividades -DAO - End()" + result);
        return result;
    }

    /**
     *
     * @param trayEntidad
     * @param validado
     * @param con
     * @return resultado
     * @throws Exception
     */
    public boolean guardarTrayEntidad(OriTrayectoriaEntidadVO trayEntidad, boolean validado, Connection con) throws Exception {
        log.debug("guardarTrayEntidad - Begin()");
        Statement st = null;
        int res = 0;
        try {
            if (trayEntidad.getCodEntidad() == null || (trayEntidad.getNumExpediente() == null || trayEntidad.getNumExpediente().equals(""))) {
                log.error("Se ha producido un error guardando los datos de trayectoria actividades para entidad " + (trayEntidad != null ? trayEntidad.getCodEntidad() : "(actividad = null)") + " y expediente " + (trayEntidad != null ? trayEntidad.getNumExpediente() : "(centro = null)"));
                throw new Exception();
            } else {
                String query = null;
                String fechaIni = "";
                String fechaFin = "";
                String fechaIniVal = "";
                String fechaFinVal = "";
                if (trayEntidad.getFechaInicio() != null && !"".equals(trayEntidad.getFechaInicio().toString())) {
                    fechaIni = dateFormat.format(trayEntidad.getFechaInicio());
                }
                if (trayEntidad.getFechaFin() != null && !"".equals(trayEntidad.getFechaFin().toString())) {
                    fechaFin = dateFormat.format(trayEntidad.getFechaFin());
                }
                if (trayEntidad.getFechaInicioVal() != null && !"".equals(trayEntidad.getFechaInicioVal().toString())) {
                    fechaIniVal = dateFormat.format(trayEntidad.getFechaInicioVal());
                }
                if (trayEntidad.getFechaFinVal() != null && !"".equals(trayEntidad.getFechaFinVal().toString())) {
                    fechaFinVal = dateFormat.format(trayEntidad.getFechaFinVal());
                }
                if (trayEntidad.getIdTrayEntidad() == null) {
                    Long idActividad = this.getNextId(ConfigurationParameter.getParameter(ConstantesMeLanbide47.SEQ_ORI_TRAYECTORIA_ENTIDAD, ConstantesMeLanbide47.FICHERO_PROPIEDADES), con);
                    if (idActividad == null) {
                        log.error("Se ha producido un error al obtener next Id de " + ConfigurationParameter.getParameter(ConstantesMeLanbide47.SEQ_ORI_TRAYECTORIA_ENTIDAD, ConstantesMeLanbide47.FICHERO_PROPIEDADES));
                        throw new Exception();
                    }

                    //Es un registro nuevo
                    trayEntidad.setIdTrayEntidad(Integer.valueOf(idActividad.toString()));
                    query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI_TRAYECTORIA_ENTIDAD, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                            + " (ID"
                            + ", TRAYIDFKORIPROGCONVACTGRUPO"
                            + ", TRAYIDFKORIPROGCONVACTSUBGRPRE"
                            + ", TRAYNUMEXPEDIENTE"
                            + ", TRAYCODIGOENTIDAD"
                            + ", TRAYDESCRIPCION"
                            + ", TRAYTIENEEXPERIENCIA"
                            + ", TRAYFECHAINICIO"
                            + ", TRAYFECHAFIN"
                            + ", TRAYNUMEROMESES"
                            + ", TRAYTIENEEXPERIENCIAVAL"
                            + ", TRAYFECHAINICIOVAL"
                            + ", TRAYFECHAFINVAL"
                            + ", TRAYNUMEROMESESVAL"
                            + ", TRAYFECHAALTA";
                    if (validado) {
                        query = query + ", TRAYFECHAVALIDACION";
                    }
                    query = query
                            + ")"
                            + " VALUES(" + trayEntidad.getIdTrayEntidad()
                            + ", " + trayEntidad.getIdConActGrupo()
                            + ", " + (trayEntidad.getIdConActSubgrupo() != null ? trayEntidad.getIdConActSubgrupo() : "null")
                            + ", '" + trayEntidad.getNumExpediente() + "'"
                            + ", '" + trayEntidad.getCodEntidad() + "'"
                            + ", " + (trayEntidad.getDescActividad() != null && !trayEntidad.getDescActividad().equals("") ? "'" + trayEntidad.getDescActividad() + "'" : "null")
                            + ", " + (trayEntidad.getTieneExperiencia() != null ? trayEntidad.getTieneExperiencia() : "null")
                            + ", TO_DATE('" + fechaIni + "','dd/mm/yyyy')"
                            + ", TO_DATE('" + fechaFin + "','dd/mm/yyyy')"
                            + ", " + trayEntidad.getNumMeses()
                            + ", " + (trayEntidad.getTieneExperienciaVal() != null ? trayEntidad.getTieneExperienciaVal() : "null")
                            + ", TO_DATE('" + fechaIniVal + "','dd/mm/yyyy')"
                            + ", TO_DATE('" + fechaFinVal + "','dd/mm/yyyy')"
                            + ", " + (trayEntidad.getNumMesesVal() != null ? trayEntidad.getNumMesesVal() : "null")
                            + ", current_timestamp"; // fecha actual
                    if (validado) {
                        query = query + ", current_timestamp"; // fecha actual
                    }
                    query = query + ")";
                } else { //Es un registro que ya existe
                    query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI_TRAYECTORIA_ENTIDAD, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                            + " set "
                            + " TRAYCODIGOENTIDAD ='" + trayEntidad.getCodEntidad() + "'"
                            + ", TRAYDESCRIPCION ='" + trayEntidad.getDescActividad() + "'"
                            + ", TRAYTIENEEXPERIENCIA=" + (trayEntidad.getTieneExperiencia() != null ? trayEntidad.getTieneExperiencia() : "null")
                            + ", TRAYFECHAINICIO = TO_DATE('" + fechaIni + "','dd/mm/yyyy')"
                            + ", TRAYFECHAFIN = TO_DATE('" + fechaFin + "','dd/mm/yyyy')"
                            + ", TRAYNUMEROMESES =" + trayEntidad.getNumMeses()
                            + ", TRAYTIENEEXPERIENCIAVAL=" + (trayEntidad.getTieneExperienciaVal() != null ? trayEntidad.getTieneExperienciaVal() : "null")
                            + ", TRAYFECHAINICIOVAL = TO_DATE('" + fechaIniVal + "','dd/mm/yyyy')"
                            + ", TRAYFECHAFINVAL = TO_DATE('" + fechaFinVal + "','dd/mm/yyyy')"
                            + ", TRAYNUMEROMESESVAL =" + (trayEntidad.getNumMesesVal() != null ? trayEntidad.getNumMesesVal() : "null")
                            + ", TRAYFECHAMODIFICACION = current_timestamp";
                    if (validado) {
                        query = query + ", TRAYFECHAVALIDACION = current_timestamp"; // fecha actual
                    }
                    query = query
                            + " where TRAYIDFKORIPROGCONVACTGRUPO = " + trayEntidad.getIdConActGrupo()
                            + " and TRAYNUMEXPEDIENTE ='" + trayEntidad.getNumExpediente() + "'"
                            + " and ID =" + trayEntidad.getIdTrayEntidad();
                }
                if (log.isDebugEnabled()) {
                    log.error("sql = " + query);
                }
                st = con.createStatement();
                res = st.executeUpdate(query);
                if (res > 0) {
                    log.debug("GUARDADOS " + res + " registros");
                    return true;
                } else {
                    log.error("Se ha producido un error guardando los datos de  actividades para entidad " + (trayEntidad != null ? trayEntidad.getCodEntidad() : "(actividad = null)") + " y expediente " + (trayEntidad != null ? trayEntidad.getNumExpediente() : "(centro = null)"));
                    throw new Exception();
                }
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error guardando los datos de Actividades para entidad " + (trayEntidad != null ? trayEntidad.getCodEntidad() : "(actividad = null)") + " y expediente " + (trayEntidad != null ? trayEntidad.getNumExpediente() : "(centro = null)"), ex);
            throw new Exception(ex);
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }

    public String[] getCifNombreEntidad(String numExpediente, String codEntidad, Connection con) throws Exception {
        log.debug("getCifNombreEntidad - Begin()");
        Statement st = null;
        ResultSet rs = null;
        String[] entidad = new String[2];
        String tablaEntidades = ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI14_ENTIDAD, ConstantesMeLanbide47.FICHERO_PROPIEDADES);
        try {
            String query = "SELECT ORI_ENT_CIF, ORI_ENT_NOM FROM " + tablaEntidades
                    + " WHERE EXT_NUM = '" + numExpediente + "' "
                    + " AND ORI_ENT_COD = " + codEntidad;
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                entidad[0] = (rs.getString("ORI_ENT_CIF"));
                entidad[1] = (rs.getString("ORI_ENT_NOM"));
            }
        } catch (SQLException ex) {
            log.error("Se ha producido una excepcion en la BBDD el CIF y el nombre de la entidad  para el expediente ", ex);
            throw new Exception(ex);
        } finally {
            try {
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
        return entidad;
    }

    public String getCodEntidad(int codOrganizacion, String numExpediente, Connection con) throws Exception {
        log.debug("getCodEntidad - Begin()");
        Statement st = null;
        ResultSet rs = null;
        String codEntidad = null;
        String tablaEntidades = ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI14_ENTIDAD, ConstantesMeLanbide47.FICHERO_PROPIEDADES);
        try {
            String query = "SELECT ORI_ENT_COD FROM " + tablaEntidades
                    + " WHERE EXT_NUM = '" + numExpediente + "' "
                    + " AND EXT_MUN = " + codOrganizacion;
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                codEntidad = (rs.getString("ORI_ENT_COD"));
            }
        } catch (SQLException ex) {
            log.error("Se ha producido una excepcion en la BBDD el CÓDIGO de la entidad  para el expediente ", ex);
            throw new Exception(ex);
        } finally {
            try {
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
        return codEntidad;
    }

    public List<SelectItem> getListaSubgrupos(int idioma, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<SelectItem> retList = new ArrayList<SelectItem>();
        String campo = (idioma == 1 ? "DESCRIPCIONCORTA" : "DESCRIPCIONCORTAEU");
        try {
            String query = "SELECT ID, " + campo
                    + " FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI_SUBGRUPOS, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                    + " ORDER BY ID";
            log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            SelectItem si;
            while (rs.next()) {
                si = new SelectItem();
                si.setId(rs.getInt("ID"));
                si.setLabel(rs.getString(campo));
                retList.add(si);
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando los SubGrupos ", ex);
            throw new Exception(ex);
        } finally {
            try {
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
        return retList;
    }

    public boolean guardarMesesTrayectorias(int codOrganizacion, String numExpediente, int mesesSol, Double mesesVal, Connection con) throws Exception {
        log.info("Entramos en guardarMesesValidados de " + this.getClass().getSimpleName());
        Statement st = null;
        String query = null;
        String tabla = ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI14_ENTIDAD, ConstantesMeLanbide47.FICHERO_PROPIEDADES);
        try {
            query = "UPDATE " + tabla
                    + " SET ORI_ENT_TRAYECTORIA = " + mesesSol
                    + ", ORI_ENT_TRAYECTORIA_VAL = " + (mesesVal!=null?mesesVal:"null")
                    + " WHERE EXT_MUN = " + codOrganizacion
                    + " AND EXT_NUM='" + numExpediente + "'";
            if (log.isDebugEnabled()) {
                log.error("sql = " + query);
            }
            st = con.createStatement();
            return st.executeUpdate(query) > 0;
        } catch (SQLException ex) {
            log.error("Se ha producido un error grabando los meses validados ", ex);
            throw new Exception(ex);
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }

    public boolean guardarPuntuacionTrayectorias(int codOrganizacion, String numExpediente,Double puntos, Connection con) throws Exception {
        log.info("Entramos en guardarMesesValidados de " + this.getClass().getSimpleName());
        Statement st = null;
        String query = null;
        String tabla = ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI14_ENTIDAD, ConstantesMeLanbide47.FICHERO_PROPIEDADES);
        try {
            query = "UPDATE " + tabla
                    + " SET ORI_VALORACION_TRAYECTORIA = " + puntos
                    + " WHERE EXT_MUN = " + codOrganizacion
                    + " AND EXT_NUM='" + numExpediente + "'";
            if (log.isDebugEnabled()) {
                log.error("sql = " + query);
            }
            st = con.createStatement();
            return st.executeUpdate(query) > 0;
        } catch (SQLException ex) {
            log.error("Se ha producido un error grabando loa puntuación ", ex);
            throw new Exception(ex);
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    
    public boolean validarDatosTrayectoria21(int codOrganizacion, String numExpediente, int grupo, Connection con) throws Exception {
        log.info("  >>>> Entramos en validarDatosTrayectoria21 de " + this.getClass().getSimpleName() + " - " + numExpediente);
        Statement st = null;
        String query = "";
        try {
            query = "UPDATE "+ ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI_TRAYECTORIA_ENTIDAD, ConstantesMeLanbide47.FICHERO_PROPIEDADES)
                    + " SET TRAYFECHAINICIOVAL=TRAYFECHAINICIO,"
                    + " TRAYFECHAFINVAL = TRAYFECHAFIN,"
                    + " TRAYNUMEROMESESVAL = TRAYNUMEROMESES,"
                    + " TRAYTIENEEXPERIENCIAVAL = TRAYTIENEEXPERIENCIA,"
                    + " TRAYFECHAVALIDACION = current_timestamp"
                    + " WHERE TRAYNUMEXPEDIENTE = '" + numExpediente + "'"
                    + "    AND TRAYIDFKORIPROGCONVACTGRUPO =  " + grupo
                    ;
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int filas = st.executeUpdate(query);
            log.info("Se han copiado los valores en " + filas + " registros del grupo " + grupo);
            return filas > 0;
        } catch (SQLException ex) {
            con.rollback();
            log.error("Se ha producido un error al copiar los datos validados del expediente " + numExpediente + " - " + ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    public int getValorCampoEntidad(String numExp, Long oriEntCod, String campo, Connection con) throws Exception {
       log.debug("getValorCampoEntidad - Begin()");
        Statement st = null;
        ResultSet rs = null;
        int valorCampo = 0;
        String tablaEntidades = ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI14_ENTIDAD, ConstantesMeLanbide47.FICHERO_PROPIEDADES);
        try {
            String query = "SELECT " + campo + " FROM " + tablaEntidades
                    + " WHERE EXT_NUM = '" + numExp + "' "
                    + " AND ORI_ENT_COD = " + oriEntCod
                    ;
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                valorCampo = (rs.getInt(campo));
            }
        } catch (SQLException ex) {
            log.error("Se ha producido una excepcion en la BBDD el valor de "+ campo +"  para el expediente " + numExp, ex);
            throw new Exception(ex);
        } finally {
            try {
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
        return valorCampo;
    }

    public boolean actualizaPlanCertEntidad(EntidadVO entidadEjemplo, Connection con) throws Exception {
        log.info("Entramos en actualizaPlanCertEntidad de " + this.getClass().getSimpleName());
        Statement st = null;
        String query = null;
        String tabla = ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI14_ENTIDAD, ConstantesMeLanbide47.FICHERO_PROPIEDADES);
        try {
            query = "UPDATE " + tabla
                    + " SET ORI_ENT_PLAN_IGUALDAD_REG = " + entidadEjemplo.getPlanIgualdad()
                    + ", ORI_ENT_CERT_CALIDAD = " + entidadEjemplo.getCertificadoCalidad()
                    + ", ORI_ENT_PLAN_IGUALDAD_REG_VAL = " + entidadEjemplo.getPlanIgualdadVal()
                    + ", ORI_ENT_CERT_CALIDAD_VAL = " + entidadEjemplo.getCertificadoCalidadVal()
                    + " WHERE EXT_MUN = " + entidadEjemplo.getExtMun()
                    + " AND EXT_NUM='" + entidadEjemplo.getExtNum() + "'"
                    + " AND ORI_ENT_COD=" + entidadEjemplo.getOriEntCod();
            if (log.isDebugEnabled()) {
                log.error("sql = " + query);
            }
            st = con.createStatement();
            return st.executeUpdate(query) > 0;
        } catch (SQLException ex) {
            log.error("Se ha producido un error grabando Plan Igualdad  Cert Calidad ", ex);
            throw new Exception(ex);
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    
    public boolean actualizaPlanCertValEntidad(EntidadVO entidadEjemplo, Connection con) throws Exception {
        log.info("Entramos en actualizaPlanCertEntidad Validado de " + this.getClass().getSimpleName());
        Statement st = null;
        String query = null;
        String tabla = ConfigurationParameter.getParameter(ConstantesMeLanbide47.TABLA_ORI14_ENTIDAD, ConstantesMeLanbide47.FICHERO_PROPIEDADES);
        try {
            query = "UPDATE " + tabla
                    + " SET ORI_ENT_PLAN_IGUALDAD_REG_VAL = " + entidadEjemplo.getPlanIgualdadVal()
                    + ", ORI_ENT_CERT_CALIDAD_VAL = " + entidadEjemplo.getCertificadoCalidadVal()
                    + " WHERE EXT_MUN = " + entidadEjemplo.getExtMun()
                    + " AND EXT_NUM='" + entidadEjemplo.getExtNum() + "'"
                    + " AND ORI_ENT_COD=" + entidadEjemplo.getOriEntCod();
            if (log.isDebugEnabled()) {
                log.error("sql = " + query);
            }
            st = con.createStatement();
            return st.executeUpdate(query) > 0;
        } catch (SQLException ex) {
            log.error("Se ha producido un error grabando Plan Igualdad  Cert Calidad ", ex);
            throw new Exception(ex);
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }

   /**
    * Lee la trayectoria de la entidad devolviendo la lista ordenada de fechas no solapadas entre convocatorias y el numero e meses entre ellas
    * @param numExpediente
    * @param con
    * @return
    * @throws Exception 
    */
    public List<OriTrayectoriaEntidadVO> getListaFechasTrayectoriaEntidadXExpNoSolapamiento(String numExpediente, Connection con) throws Exception {
        log.info("getListaFechasTrayectoriaEntidadXExpNoSolapamiento - DAO - Begin()");
        Statement st = null;
        ResultSet rs = null;
        List<OriTrayectoriaEntidadVO> listaResumen = new ArrayList<OriTrayectoriaEntidadVO>();
        try {
            String query = " SELECT "
                    + " DISTINCT TRAYNUMEXPEDIENTE, TRAYFECHAINICIO, TRAYFECHAFIN "
                    + " FROM ORI_TRAYECTORIA_ENTIDAD   TRAYECTORIAS "
                    + " WHERE TRAYECTORIAS.TRAYNUMEXPEDIENTE = '"+numExpediente+"'"
                    + " and (case when (TRAYECTORIAS.TRAYIDFKORIPROGCONVACTGRUPO=2 and TRAYECTORIAS.TRAYTIENEEXPERIENCIA=1) or (TRAYECTORIAS.TRAYIDFKORIPROGCONVACTGRUPO!=2) then 1 else 0 end)=1 "
                    + " and (case when TRAYECTORIAS.TRAYFECHAINICIO<TRAYECTORIAS.TRAYFECHAFIN then 1 else 0 end)=1 "
                    + " ORDER BY to_date(TRAYFECHAINICIO,'dd/MM/yyyy') ASC, to_date(TRAYFECHAFIN,'dd/MM/yyyy') ASC ";
            log.info("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                try {
                        OriTrayectoriaEntidadVO oriTrayectoriaEntidadVO = new OriTrayectoriaEntidadVO();
                        if (listaResumen.isEmpty()) {
                            oriTrayectoriaEntidadVO.setNumExpediente(rs.getString("TRAYNUMEXPEDIENTE"));
                            oriTrayectoriaEntidadVO.setFechaInicio(rs.getDate("TRAYFECHAINICIO"));
                            oriTrayectoriaEntidadVO.setFechaFin(rs.getDate("TRAYFECHAFIN"));
                            int meses = meLanbide47Utils.calcularNroMesesEntreDosFechas(rs.getDate("TRAYFECHAINICIO"), rs.getDate("TRAYFECHAFIN"));
                            oriTrayectoriaEntidadVO.setNumMeses((meses >= 0 ? meses : 0));
                            listaResumen.add(oriTrayectoriaEntidadVO);
                        } else {
                            oriTrayectoriaEntidadVO = listaResumen.get(listaResumen.size() - 1);
                            Date fechaInicioTemp = rs.getDate("TRAYFECHAINICIO");
                            Date fechaFinTemp = rs.getDate("TRAYFECHAFIN");
                            if (fechaFinTemp != null && fechaInicioTemp != null) {
                                if (fechaInicioTemp.before(oriTrayectoriaEntidadVO.getFechaFin())) {
                                    Calendar calTemp = Calendar.getInstance();
                                    calTemp.setTime(oriTrayectoriaEntidadVO.getFechaFin());
                                    calTemp.add(Calendar.DATE, 1);
                                    Date fechaInicioTempPlus1 = calTemp.getTime();
                                    log.info("Evitamos solapamiento con fechas : " + numExpediente + " Original " + dateFormat.format(fechaInicioTemp) + " Reemplazamos " + dateFormat.format(fechaInicioTempPlus1));
                                    fechaInicioTemp = fechaInicioTempPlus1;
                                }
                                if (fechaFinTemp.after(oriTrayectoriaEntidadVO.getFechaFin())) {
                                    OriTrayectoriaEntidadVO oriTrayectoriaEntidadVO2 = new OriTrayectoriaEntidadVO();
                                    oriTrayectoriaEntidadVO2.setNumExpediente(rs.getString("TRAYNUMEXPEDIENTE"));
                                    oriTrayectoriaEntidadVO2.setFechaInicio(new java.sql.Date(fechaInicioTemp.getTime()));
                                    oriTrayectoriaEntidadVO2.setFechaFin(new java.sql.Date(fechaFinTemp.getTime()));
                                    int meses = meLanbide47Utils.calcularNroMesesEntreDosFechas(oriTrayectoriaEntidadVO2.getFechaInicio(), oriTrayectoriaEntidadVO2.getFechaFin());
                                    oriTrayectoriaEntidadVO2.setNumMeses((meses >= 0 ? meses : 0));
                                    listaResumen.add(oriTrayectoriaEntidadVO2);
                                } else {
                                    log.info("No cargamos fecha porque esta comprendida dentro de una que se registro previamente. " + dateFormat.format(fechaInicioTemp) + "  " + dateFormat.format(fechaFinTemp));
                                }
                            } else {
                                log.info("Una de las de las fechas a calcular viene a null... No calculamos");
                            }
                        }
                } catch (Exception ex) {
                    log.error("Error al leer los datos trayectoria no solapada ", ex);
                }
            }
        } catch (SQLException ex) {
            log.error("Se ha producido una excepcion en la BBDD Calculando trayectoria no solapada " + (numExpediente != null ? numExpediente : " (numExpediente = null)"), ex);
            throw new Exception(ex);
        } finally {
            try {
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
        return listaResumen;
    }
    
    /**
     * Lee la trayectoria de la entidad devolviendo la lista ordenada de fechas validadas no solapadas entre convocatorias y el numero e meses entre ellas
     * @param numExpediente
     * @param con
     * @return
     * @throws Exception 
     */
    public List<OriTrayectoriaEntidadVO> getListaFechasTrayectoriaEntidadXExpNoSolapamientoValidada(String numExpediente, Connection con) throws Exception {
        log.info("getListaFechasTrayectoriaEntidadXExpNoSolapamientoValidada - DAO - Begin()");
        Statement st = null;
        ResultSet rs = null;
        List<OriTrayectoriaEntidadVO> listaResumen = new ArrayList<OriTrayectoriaEntidadVO>();
        try {
            String query = " SELECT "
                    + " DISTINCT TRAYNUMEXPEDIENTE, TRAYFECHAINICIOVAL, TRAYFECHAFINVAL "
                    + " FROM ORI_TRAYECTORIA_ENTIDAD   TRAYECTORIAS "
                    + " WHERE TRAYECTORIAS.TRAYNUMEXPEDIENTE = '"+numExpediente+"'"
                    + " and (case when (TRAYECTORIAS.TRAYIDFKORIPROGCONVACTGRUPO=2 and TRAYECTORIAS.TRAYTIENEEXPERIENCIA=1) or (TRAYECTORIAS.TRAYIDFKORIPROGCONVACTGRUPO!=2) then 1 else 0 end)=1 "
                    + " and (case when TRAYECTORIAS.TRAYFECHAINICIOVAL<TRAYECTORIAS.TRAYFECHAFINVAL then 1 else 0 end)=1 "
                    + " ORDER BY to_date(TRAYFECHAINICIOVAL,'dd/MM/yyyy') ASC, to_date(TRAYFECHAFINVAL,'dd/MM/yyyy') ASC ";
            log.info("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                try {
                    OriTrayectoriaEntidadVO oriTrayectoriaEntidadVO = new OriTrayectoriaEntidadVO();
                    if(listaResumen.isEmpty()){
                        oriTrayectoriaEntidadVO.setNumExpediente(rs.getString("TRAYNUMEXPEDIENTE"));
                        oriTrayectoriaEntidadVO.setFechaInicioVal(rs.getDate("TRAYFECHAINICIOVAL"));
                        oriTrayectoriaEntidadVO.setFechaFinVal(rs.getDate("TRAYFECHAFINVAL"));
                        int meses=meLanbide47Utils.calcularNroMesesEntreDosFechas(rs.getDate("TRAYFECHAINICIOVAL"), rs.getDate("TRAYFECHAFINVAL"));
                        oriTrayectoriaEntidadVO.setNumMesesVal((meses>=0?meses:0));
                        listaResumen.add(oriTrayectoriaEntidadVO);
                    }else{
                        oriTrayectoriaEntidadVO=listaResumen.get(listaResumen.size()-1);
                        Date fechaInicioTemp = rs.getDate("TRAYFECHAINICIOVAL");
                        Date fechaFinTemp = rs.getDate("TRAYFECHAFINVAL");
                        if(fechaFinTemp!=null && fechaInicioTemp!=null){
                            if(fechaInicioTemp.before(oriTrayectoriaEntidadVO.getFechaFinVal())){
                                Calendar  calTemp = Calendar.getInstance();
                                calTemp.setTime(oriTrayectoriaEntidadVO.getFechaFinVal());
                                calTemp.add(Calendar.DATE, 1);
                                Date fechaInicioTempPlus1=calTemp.getTime();
                                log.info("Evitamos solapamiento con fechas : " + numExpediente +" Original " + dateFormat.format(fechaInicioTemp) + " Reemplazamos "+ dateFormat.format(fechaInicioTempPlus1));
                                fechaInicioTemp=fechaInicioTempPlus1;
                            }
                            if(fechaFinTemp.after(oriTrayectoriaEntidadVO.getFechaFinVal())){
                                OriTrayectoriaEntidadVO oriTrayectoriaEntidadVO2 = new OriTrayectoriaEntidadVO();
                                oriTrayectoriaEntidadVO2.setNumExpediente(rs.getString("TRAYNUMEXPEDIENTE"));
                                oriTrayectoriaEntidadVO2.setFechaInicioVal(new java.sql.Date(fechaInicioTemp.getTime()));
                                oriTrayectoriaEntidadVO2.setFechaFinVal(new java.sql.Date(fechaFinTemp.getTime()));
                                int meses = meLanbide47Utils.calcularNroMesesEntreDosFechas(oriTrayectoriaEntidadVO2.getFechaInicioVal(), oriTrayectoriaEntidadVO2.getFechaFinVal());
                                oriTrayectoriaEntidadVO2.setNumMesesVal((meses >= 0 ? meses : 0));
                                listaResumen.add(oriTrayectoriaEntidadVO2);
                            }else
                                log.info("No cargamos fecha porque esta comprendida dentro de una que se registro previamente. " +  dateFormat.format(fechaInicioTemp) + "  "+ dateFormat.format(fechaFinTemp));
                        }else
                            log.info("Una de las de las fechas a calcular viene a null... No calculamos");
                    }
                } catch (Exception ex) {
                    log.error("Error al leer los datos trayectoria no solapada ", ex);
                }
            }
        } catch (SQLException ex) {
            log.error("Se ha producido una excepcion en la BBDD - Calculando trayectoria no solapada validada " + (numExpediente != null ? numExpediente : " (numExpediente = null)"), ex);
            throw new Exception(ex);
        } finally {
            try {
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
        return listaResumen;
    }
}
