/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide32.dao;

import es.altia.flexia.integracion.moduloexterno.melanbide32.manager.MeLanbide32Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide32.util.ConstantesMeLanbide32;
import es.altia.flexia.integracion.moduloexterno.melanbide32.util.MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.centroempleo.*;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.orientacion.AmbitoHorasVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.comun.EntidadVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.comun.MunicipioVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.comun.ProvinciaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.comun.SelectItem;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.orientacion.FilaUbicOrientacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.orientacion.OriUbicacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.orientacion.UbicacionHorasVO;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import es.altia.agora.business.util.GlobalNames;
import es.altia.flexia.integracion.moduloexterno.melanbide32.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide32.util.MeLanbide32Utils;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.comun.AuditoriaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.comun.EspecialidadVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.comun.TerceroVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.comun.DomicilioVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.comun.TipoViaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.comun.ViaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.orientacion.FilaTrayectoriaOrientacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.orientacion.OriTrayectoriaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.procesos.FilaAuditoriaProcesosVO;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;

/**
 *
 * @author santiagoc
 */
public class MeLanbide32DAO 
{
    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide32DAO.class);
    
    //Instancia
    private static MeLanbide32DAO instance = null;
    private final MeLanbide32Manager meLanbide32Manager = MeLanbide32Manager.getInstance();
    
    private MeLanbide32DAO()
    {
        
    }
    
    public static MeLanbide32DAO getInstance()
    {
        if(instance == null)
        {
            synchronized(MeLanbide32DAO.class)
            {
                instance = new MeLanbide32DAO();
            }
        }
        return instance;
    }    
    
    public List<SelectItem> getListaProvincias(String codigoPais, List<String> codigosProv, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        List<SelectItem> retList = new ArrayList<SelectItem>();
        try
        {
            String query = null;
            query = "select PRV_COD, PRV_NOM from "+ GlobalNames.ESQUEMA_GENERICO + ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_PROVINCIAS, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
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
            query = "select ORI_AMB_COD, ORI_AMB_AMBITO from " + ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_AMBITOS_HORAS, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
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
        return retList;
    }
    
    public List<SelectItem> getAmbitosCentroEmpleoPorProvincia(String codProvincia, String anoConv, Connection con)throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        List<SelectItem> retList = new ArrayList<SelectItem>();
        try
        {
            String query = null;
            query = "select ORI_AMB_COD, ORI_AMB_AMBITO from " + ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_AMBITOS_CE, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
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
        return retList;
    }
    /**
     * 
     * @param anoConv : Ejercicio de la convocatoria que gestiona todos los ambitos.
     * @param tipoAmbito : 0=Todos, 1=Especial, 2=Distribuido o 3=Resto Ambitos
     * @param con
     * @return
     * @throws Exception 
     */
    public List<SelectItem> getAmbitosCentroEmpleoPorAnioConvAndTipo(String oriAmbCeAnoconv,int oriAmbCeTipoAmbito, Connection con)throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        List<SelectItem> retList = new ArrayList<SelectItem>();
        try
        {
            String query = null;
            query = "select ORI_AMB_COD, ORI_AMB_AMBITO from " + ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_AMBITOS_CE, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                    + " where ORI_AMB_ANOCONV = " + (oriAmbCeAnoconv!=null && oriAmbCeAnoconv!="" ? Integer.parseInt(oriAmbCeAnoconv) : "null")
                    + prepararCondicion2AmbitoxAnioConvAndTipo(oriAmbCeTipoAmbito)
                    + " ORDER BY ORI_AMB_AMBITO"
                    ;
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
            log.error("Se ha producido un error recuperando las ambitos por anios CEMP", ex);
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
        return retList;
    }
    public List<SelectItem> getDesplegableTipoAmbitosCEMP(Connection con)throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        List<SelectItem> retList = new ArrayList<SelectItem>();
        try
        {
            String query = null;
            query = "SELECT * FROM E_DES_VAL "
                    + " WHERE DES_COD='" +ConfigurationParameter.getParameter(ConstantesMeLanbide32.COD_DESPLE_TIPO_AMBITO_CEMP, ConstantesMeLanbide32.FICHERO_PROPIEDADES)+ "'"
                    + " ORDER BY DES_VAL_COD "
                    ;
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            Integer cod;
            String desc;
            SelectItem si;
            while(rs.next())
            {
                cod = rs.getInt("DES_VAL_COD");
                desc = rs.getString("DES_NOM");
                si = new SelectItem();
                si.setId(cod);
                si.setLabel(desc);
                retList.add(si);
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando las ambitos por anios CEMP", ex);
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
                
                query = "select MUN_PRV, MUN_COD from "+ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_UBICACIONES_HORAS, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                        + " where MUN_PAI = "+ConstantesMeLanbide32.CODIGO_PAIS_ESPANA
                        + " and ORI_AMB_COD = "+codAmbito+" and ORI_UBIC_ANO = "+ano;
                if(log.isDebugEnabled()) 
                    log.error("sql = " + query);
                st = con.createStatement();
                rs = st.executeQuery(query);
                while(rs.next())
                {
                    codProv = rs.getInt("MUN_PRV");
                    codMun = rs.getInt("MUN_COD");
                    query2 = "select MUN_COD, MUN_NOM from "+ GlobalNames.ESQUEMA_GENERICO + ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_MUNICIPIOS, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                             + " where MUN_PAI = "+ConstantesMeLanbide32.CODIGO_PAIS_ESPANA+" and MUN_PRV = "+codProv
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
                if(st2!=null) 
                    st2.close();
                if(rs2!=null) 
                    rs2.close();  
            }
        }
        return retList;
    }
    
    public List<SelectItem> getMunicipiosPorAmbitoProvinciaCentroEmpleo(String codProvincia, Integer codAmbito, Integer ano, Connection con)throws Exception
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
                
                query = "select MUN_PRV, MUN_COD from "+ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_UBICACIONES_CE, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                        + " where MUN_PAI = "+ConstantesMeLanbide32.CODIGO_PAIS_ESPANA
                        + " and ORI_AMB_COD = "+codAmbito+" and ORI_UBIC_ANO = "+ano;
                if(log.isDebugEnabled()) 
                    log.error("sql = " + query);
                st = con.createStatement();
                rs = st.executeQuery(query);
                while(rs.next())
                {
                    codProv = rs.getInt("MUN_PRV");
                    codMun = rs.getInt("MUN_COD");
                    query2 = "select MUN_COD, MUN_NOM from "+ GlobalNames.ESQUEMA_GENERICO + ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_MUNICIPIOS, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                             + " where MUN_PAI = "+ConstantesMeLanbide32.CODIGO_PAIS_ESPANA+" and MUN_PRV = "+codProv
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
                if(st2!=null) 
                    st2.close();
                if(rs2!=null) 
                    rs2.close();
            }
        }
        return retList;
    }
    
    public EntidadVO getEntidad(int codOrganizacion, String numExp, String ejercicio, Connection con)throws Exception
    {
        EntidadVO vo = null;
        //Statement st = null;
        //ResultSet rs = null;
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
                query2 = "select * from (select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_ENTIDAD, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                        +" where EXT_MUN = '" + codOrganizacion + "' and EXT_EJE = '" + ejercicio + "' and EXT_NUM = '" + numExp +"' "
                        //+" and EXT_TER = '" + ter + "' and EXT_NVR = '"+ nvr +"'";
                        +" order by ORI_ENT_COD desc) where rownum = 1";
                if(log.isDebugEnabled()) 
                    log.error("sql = " + query2);
                st2 = con.createStatement();
                rs2 = st2.executeQuery(query2);
                if(rs2.next())
                {
                    //parada = true;
                    try
                    {
                        vo = (EntidadVO)MappingUtils.getInstance().map(rs2, EntidadVO.class);
                    }
                    catch(Exception ex)
                    {
                        
                    }
                }
            /*}*/
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
            if(st2!=null) 
                st2.close();
            if(rs2!=null) 
                rs2.close();
        }
        return vo;
    }
    
    public EntidadVO getEntidadPorCodigo(Long codEnt, Connection con)throws Exception
    {
        EntidadVO vo = null;
        Statement st = null;
        ResultSet rs = null;
        try
        {
            String query = null;
            query = "select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_ENTIDAD, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                        +" where ORI_ENT_COD = '" + codEnt + "'";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                try
                {
                    vo = (EntidadVO)MappingUtils.getInstance().map(rs, EntidadVO.class);
                }
                catch(Exception ex)
                {

                }
            }
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
        return vo;
    }
    
    public HashMap<String, String> getDatosTercero(int codOrganizacion, String numExp, String ejercicio, Connection con)throws Exception
    {
        HashMap<String, String> map = new HashMap<String, String>();
        Statement st = null;
        ResultSet rs = null;
        try
        {
            String query = null;
            query = "select EXT_EJE, EXT_NUM, EXT_TER, EXT_NVR, EXT_MUN from "+ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_REL_TERCERO_DOMICILIO, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
            +" e where e.EXT_MUN = '"+codOrganizacion+"' and e.EXT_EJE = '"+ejercicio+"' and e.EXT_NUM = '"+numExp+"'";
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
            }
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
        return map;
    }
    
    public EntidadVO crearEntidad(EntidadVO entidad, Connection con)throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        try
        {
            String query = null;
            //Creo el id con la secuencia
            query = "select "+ ConfigurationParameter.getParameter(ConstantesMeLanbide32.SEQ_ORI_ENTIDAD, ConstantesMeLanbide32.FICHERO_PROPIEDADES)+".nextval from dual";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                Long numSec = rs.getLong(1);
                if(rs.wasNull())
                {
                    throw new Exception();
                }
                entidad.setOriEntCod(numSec);
            }
            
            //Guardo la entidad en BD
            query = "insert into "+ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_ENTIDAD, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                    +"(ORI_ENT_COD, EXT_MUN, EXT_EJE, EXT_NUM, EXT_TER, EXT_NVR,ORI_ENT_NOM) values"
                    +" ('"+entidad.getOriEntCod().toString()
                    +"', '"+(entidad.getExtMun() != null ? entidad.getExtMun().toString() : "")
                    +"', '"+(entidad.getExtEje() != null ? entidad.getExtEje().toString() : "")
                    +"', '"+(entidad.getExtNum() != null ? entidad.getExtNum() : "")
                    +"', "+(entidad.getExtTer() != null ? entidad.getExtTer() : "null")
                    +", "+(entidad.getExtNvr() != null ? entidad.getExtNvr() : "null")
                    +", '"+(entidad.getOriEntNom()!= null ? entidad.getOriEntNom() : "")+"'"
                    +")";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            if(insert > 0)
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
    }
    
    public OriUbicacionVO crearUbicacionORI(OriUbicacionVO ubic, Connection con)throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        try
        {
            String query = null;
            //Creo el id con la secuencia
            query = "select "+ ConfigurationParameter.getParameter(ConstantesMeLanbide32.SEQ_ORI_ORI_UBICACION, ConstantesMeLanbide32.FICHERO_PROPIEDADES)+".nextval from dual";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                Integer numSec = rs.getInt(1);
                if(rs.wasNull())
                {
                    throw new Exception();
                }
                ubic.setOriOrientUbicCod(numSec);
            }
            
            //Guardo la entidad en BD
            query = "insert into "+ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_ORI_UBIC, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                    +"(ORI_ORIENT_UBIC_COD, ORI_ORIENT_ANO, ORI_ENT_COD, ORI_AMB_COD, MUN_PAI, MUN_PRV, MUN_COD, ORI_ORIENT_DIRECCION, ORI_ORIENT_HORASSOLICITADAS, ORI_ORIENT_DESPACHOS, ORI_ORIENT_AULAGRUPAL, ORI_ORIENT_VAL_TRAY, ORI_ORIENT_PUNTUACION, PRV_PAI) values"
                    +" ('"
                    +ubic.getOriOrientUbicCod().toString()
                    +"', '"+(ubic.getOriOrientAno() != null ? ubic.getOriOrientAno().toString() : "")
                    +"', '"+(ubic.getOriEntCod() != null ? ubic.getOriEntCod().toString() : "")
                    +"', '"+(ubic.getOriAmbCod() != null ? ubic.getOriAmbCod().toString() : "")
                    +"', '"+(ubic.getMunPai() != null ? ubic.getMunPai().toString() : "")
                    +"', '"+(ubic.getMunPrv() != null ? ubic.getMunPrv().toString() : "")
                    +"', '"+(ubic.getMunCod() != null ? ubic.getMunCod().toString() : "")
                    +"', '"+ubic.getOriOrientDireccion()
                    +"', '"+(ubic.getOriOrientHorasSolicitadas() != null ? ubic.getOriOrientHorasSolicitadas().toString() : "")
                    +"', '"+(ubic.getOriOrientDespachos() != null ? ubic.getOriOrientDespachos().toString() : "")
                    +"', '"+ubic.getOriOrientAulagrupal()
                    +"', "+(ubic.getOriOrientValTray() != null ? ubic.getOriOrientValTray() : null)
                    +", "+(ubic.getOriOrientPuntuacion() != null ? ubic.getOriOrientPuntuacion() : null)
                    +", "+(ubic.getPrvPai() != null ? ubic.getPrvPai().toString() : null)
                    +")";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            if(insert > 0)
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
    }
    
    public OriUbicacionVO modificarUbicacionORI(OriUbicacionVO ubic, Connection con)throws Exception
    {
        Statement st = null;
        try
        {
            String query = null;
            
            //Guardo la entidad en BD
            query = "update "+ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_ORI_UBIC, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                    + " set ORI_ORIENT_ANO = '"+(ubic.getOriOrientAno() != null ? ubic.getOriOrientAno().toString() : "")+"',"
                    + " ORI_ENT_COD = '"+(ubic.getOriEntCod() != null ? ubic.getOriEntCod().toString() : "")+"',"
                    + " ORI_AMB_COD = '"+(ubic.getOriAmbCod() != null ? ubic.getOriAmbCod().toString() : "")+"',"
                    + " MUN_PAI = '"+(ubic.getMunPai() != null ? ubic.getMunPai().toString() : "")+"',"
                    + " MUN_PRV = '"+(ubic.getMunPrv() != null ? ubic.getMunPrv().toString() : "")+"',"
                    + " MUN_COD = '"+(ubic.getMunCod() != null ? ubic.getMunCod().toString() : "")+"',"
                    + " ORI_ORIENT_DIRECCION = '"+(ubic.getOriOrientDireccion() != null ? ubic.getOriOrientDireccion() : "")+"',"
                    + " ORI_ORIENT_HORASSOLICITADAS = '"+(ubic.getOriOrientHorasSolicitadas() != null ? ubic.getOriOrientHorasSolicitadas().toString() : "")+"',"
                    + " ORI_ORIENT_DESPACHOS = '"+(ubic.getOriOrientDespachos() != null ? ubic.getOriOrientDespachos().toString() : "")+"',"
                    + " ORI_ORIENT_AULAGRUPAL = '"+(ubic.getOriOrientAulagrupal() != null ? ubic.getOriOrientAulagrupal().toString() : "")+"',"
                    + " ORI_ORIENT_HORASADJ = '"+(ubic.getOriOrientHorasadj() != null ? ubic.getOriOrientHorasadj().toString() : "")+"',"
                    + " ORI_ORIENT_DESPACHOS_VALIDADOS = '"+(ubic.getOriOrientDespachosValidados() != null ? ubic.getOriOrientDespachosValidados().toString() : "")+"',"
                    + " ORI_ORIENT_AULAGRUPAL_VALIDADA = '"+(ubic.getOriOrientAulaGrupalValidada() != null ? ubic.getOriOrientAulaGrupalValidada() : "")+"',"
                    + " ORI_ORIENT_VAL_TRAY = '"+(ubic.getOriOrientValTray() != null ? ubic.getOriOrientValTray().toString() : "")+"',"
                    + " ORI_ORIENT_VAL_UBIC = "+(ubic.getOriOrientValUbic() != null ? ubic.getOriOrientValUbic().toString() : "null")+","
                    + " ORI_ORIENT_VAL_DESPACHOS = '"+(ubic.getOriOrientValDespachos() != null ? ubic.getOriOrientValDespachos().toString() : "")+"',"
                    + " ORI_ORIENT_VAL_AULAS = '"+(ubic.getOriOrientValAulas() != null ? ubic.getOriOrientValAulas().toString() : "")+"',"
                    + " ORI_ORIENT_PUNTUACION = "+(ubic.getOriOrientPuntuacion() != null ? ubic.getOriOrientPuntuacion().toString() : "null")+","
                    + " ORI_ORIENT_OBSERVACIONES = '"+(ubic.getOriOrientObservaciones() != null ? ubic.getOriOrientObservaciones() : "")+"',"
                    + " PRV_PAI = '"+(ubic.getPrvPai() != null ? ubic.getPrvPai().toString() : "")+"'"
                    + " where ORI_ORIENT_UBIC_COD = '"+ubic.getOriOrientUbicCod().toString()+"'";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            if(insert > 0)
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
            log.error("Se ha producido un error recuperando las áreas", ex);
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
    
    public int eliminarUbicacionORI(Integer idUbic, Connection con)throws Exception
    {
        Statement st = null;
        try
        {
            String query = null;
            
            //Guardo la entidad en BD
            query = "delete from "+ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_ORI_UBIC, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                    +" where ORI_ORIENT_UBIC_COD = '" + idUbic.toString() + "'";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            return st.executeUpdate(query);
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
        }
    }
    
    public List<FilaUbicOrientacionVO> getUbicacionesORI(int codOrganizacion, String ejercicio, String numExpediente, Connection con)throws Exception
    {
        
        List<FilaUbicOrientacionVO> retList = new ArrayList<FilaUbicOrientacionVO>();
        Statement st = null;
        ResultSet rs = null;
        Statement st2 = null;
        ResultSet rs2 = null;
        try
        {
            String query = null;
            query = "select ORI_ENT_COD from "+ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_ENTIDAD, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                    +" where EXT_MUN = '"+codOrganizacion+"' and EXT_EJE = '"+ejercicio+"' and EXT_NUM = '"+numExpediente+"'";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            String entCod = "";
            FilaUbicOrientacionVO fila = null;
            while(rs.next())
            {
                //Voy recorriendo todas las entidades para recoger sus ubicaciones
                entCod = rs.getString("ORI_ENT_COD");
                if(entCod != null && !entCod.equals(""))
                {
                    query = "select * from "+ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_ORI_UBIC, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                            +" where ORI_ENT_COD = '"+entCod+"'";
                    st2 = con.createStatement();
                    rs2 = st2.executeQuery(query);
                    
                    AmbitoHorasVO amb = null;
                    ProvinciaVO prov = null;
                    MunicipioVO mun = null;
                    
                    String val1 = null;
                    BigDecimal val2 = null;
                    String val3 = null;
                    String val4 = null;
                    String valResult = null;
                    while(rs2.next())
                    {
                        fila = new FilaUbicOrientacionVO();
                        try
                        {
                            int codAmb = rs2.getInt("ORI_AMB_COD");
                            if(!rs2.wasNull())
                            {
                                amb = this.getAmbitoHorasPorCodigo(codAmb, con);
                                if(amb != null)
                                {
                                    fila.setAmbito(amb.getOriAmbAmbito() != null ? amb.getOriAmbAmbito().toUpperCase() : "-");
                                    prov = getProvinciaPorCodigo(amb.getOriAmbTerHis(), con);
                                    if(prov != null)
                                    {
                                        fila.setProvincia(prov.getPrvNom() != null && !prov.getPrvNom().equals("") ? prov.getPrvNom().toUpperCase() : "-");
                                    }
                                    else   
                                    {
                                        fila.setProvincia("-");
                                    }
                                }
                                else
                                {
                                    fila.setAmbito("-");
                                    fila.setProvincia("-");
                                }
                            }
                            else
                            {
                                fila.setAmbito("-");
                                fila.setProvincia("-");
                            }
                        }
                        catch(Exception ex)
                        {
                            fila.setAmbito("-");
                            fila.setProvincia("-");
                        }
                        fila.setAulaGrupal(rs2.getString("ORI_ORIENT_AULAGRUPAL") != null ? rs2.getString("ORI_ORIENT_AULAGRUPAL").toUpperCase() : "-");
                        fila.setCodigoUbic(rs2.getInt("ORI_ORIENT_UBIC_COD"));
                        if(rs2.wasNull())
                        {
                            fila.setCodigoUbic(null);
                        }
                        fila.setDespachos(rs2.getString("ORI_ORIENT_DESPACHOS") != null ? rs2.getString("ORI_ORIENT_DESPACHOS").toUpperCase() : "-");
                        fila.setDireccion(rs2.getString("ORI_ORIENT_DIRECCION") != null ? rs2.getString("ORI_ORIENT_DIRECCION").toUpperCase() : "-");
                        fila.setHoras(rs2.getString("ORI_ORIENT_HORASSOLICITADAS") != null ? rs2.getString("ORI_ORIENT_HORASSOLICITADAS").toUpperCase() : "-");
                        fila.setHorasAdj(rs2.getString("ORI_ORIENT_HORASADJ") != null ? rs2.getString("ORI_ORIENT_HORASADJ").toUpperCase() : "-");
                        try
                        {
                            int provCod = rs2.getInt("MUN_PRV");
                            if(!rs2.wasNull())
                            {
                                
                                int munCod = rs2.getInt("MUN_COD");
                                if(!rs2.wasNull())
                                {
                                    mun = this.getMunicipioPorCodigoYProvincia(munCod, provCod, con);
                                    if(mun != null)
                                    {
                                        fila.setMunicipio(mun.getMunNom() != null ? mun.getMunNom().toUpperCase() : "-");
                                    }
                                }
                                else
                                {
                                    fila.setMunicipio("");
                                }
                            }
                        }
                        catch(Exception ex)
                        {
                            
                        }
                        fila.setTotal(rs2.getString("ORI_ORIENT_PUNTUACION") != null ? rs2.getString("ORI_ORIENT_PUNTUACION") : "-");
                        
                        val1 = rs2.getString("ORI_ORIENT_VAL_TRAY") != null ? rs2.getString("ORI_ORIENT_VAL_TRAY") : "";
                        val2 = rs2.getBigDecimal("ORI_ORIENT_VAL_UBIC") != null ? rs2.getBigDecimal("ORI_ORIENT_VAL_UBIC") : new BigDecimal("0");
                        val3 = rs2.getString("ORI_ORIENT_VAL_DESPACHOS") != null ? rs2.getString("ORI_ORIENT_VAL_DESPACHOS") : "";
                        val4 = rs2.getString("ORI_ORIENT_VAL_AULAS") != null ? rs2.getString("ORI_ORIENT_VAL_AULAS") : "";
                        
//                        valResult = "";
//                        valResult += val1 != null && !val1.equals("") ? val1 : "";
//                        valResult += !valResult.equals("") && (val2 != null && !val2.equals("")) ? " - " : "";
//                        valResult += val2 != null && !val2.equals("") ? val2 : "";
//                        valResult += !valResult.equals("") && (val3 != null && !val3.equals("")) ? " - " : "";
//                        valResult += val3 != null && !val3.equals("") ? val3 : "";
//                        valResult += !valResult.equals("") && (val4 != null && !val4.equals("")) ? " - " : "";
//                        valResult += val4 != null && !val4.equals("") ? val4 : "";
                            
                        valResult = "";
                        valResult += val1 != null && !val1.equals("") ? val1 : "0";
                        valResult += " - ";
                        valResult += val2 != null ? val2.toPlainString() : new BigDecimal("0");
                        valResult += " - ";
                        valResult += val3 != null && !val3.equals("") ? val3 : "0";
                        valResult += " - ";
                        valResult += val4 != null && !val4.equals("") ? val4 : "0";
                        
                        fila.setValoracion(valResult != null && !valResult.equals("") ? valResult : "-");
                        retList.add(fila);
                    }
                }
            }
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
            if(st2!=null) 
                st2.close();
            if(rs2!=null) 
                rs2.close();
        }
        return retList;
    }
    
    public AmbitoHorasVO getAmbitoHorasPorCodigo(Integer ambCod, Connection con)throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        try
        {
            String query = null;
            //Creo el id con la secuencia
            query = "select * from "+ ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_AMBITOS_HORAS, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                    +" where ORI_AMB_COD = '" + ambCod + "'";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            AmbitoHorasVO ambito = null;
            if(rs.next())
            {
                ambito = (AmbitoHorasVO)MappingUtils.getInstance().map(rs, AmbitoHorasVO.class);
            }
            return ambito;            
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
    }
    
    public AmbitoCentroEmpleoVO getAmbitoCentroEmpleoPorCodigo(Integer ambCod, Connection con)throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        try
        {
            String query = null;
            //Creo el id con la secuencia
            query = "select * from "+ ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_AMBITOS_CE, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                    +" where ORI_AMB_COD = '" + ambCod + "'";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            AmbitoCentroEmpleoVO ambito = null;
            if(rs.next())
            {
                ambito = (AmbitoCentroEmpleoVO)MappingUtils.getInstance().map(rs, AmbitoCentroEmpleoVO.class);
            }
            return ambito;            
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
    }
    
    public ProvinciaVO getProvinciaPorCodigo(Integer provCod, Connection con)throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        try
        {
            String query = null;
            //Creo el id con la secuencia
            query = "select * from "+ GlobalNames.ESQUEMA_GENERICO + ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_PROVINCIAS, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                    +" where PRV_COD = '" + provCod + "'";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            ProvinciaVO prov = null;
            if(rs.next())
            {
                prov = (ProvinciaVO)MappingUtils.getInstance().map(rs, ProvinciaVO.class);
            }
            return prov;            
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
    }
    
    public MunicipioVO getMunicipioPorCodigoYProvincia(Integer munCod, Integer provCod, Connection con)throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        try
        {
            String query = null;
            //Creo el id con la secuencia
            query = "select * from "+ GlobalNames.ESQUEMA_GENERICO + ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_MUNICIPIOS, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                    +" where MUN_COD = '" + munCod + "' and MUN_PRV = '" + provCod + "'";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            MunicipioVO mun = null;
            if(rs.next())
            {
                mun = (MunicipioVO)MappingUtils.getInstance().map(rs, MunicipioVO.class);
            }
            return mun;            
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
    }
    
    public OriUbicacionVO getUbicacionORIPorCodigo(String codUbic, Connection con)throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        try
        {
            String query = null;
            //Creo el id con la secuencia
            query = "select * from "+ ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_ORI_UBIC, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                    +" where ORI_ORIENT_UBIC_COD = '" + codUbic + "'";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            OriUbicacionVO ubic = null;
            if(rs.next())
            {
                ubic = (OriUbicacionVO)MappingUtils.getInstance().map(rs, OriUbicacionVO.class);
            }
            return ubic;            
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
    }
    
    public UbicacionHorasVO getUbicacion(OriUbicacionVO pUbic, Connection con)throws Exception
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
            query = "select * from "+ ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_UBICACIONES_HORAS, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                    +" where ORI_UBIC_ANO = '"+pUbic.getOriOrientAno()+"' and ORI_AMB_COD = '"+pUbic.getOriAmbCod()+"'"
                    +" and MUN_PAI = '"+pUbic.getMunPai()+"' and MUN_PRV = '"+pUbic.getMunPrv()+"' and MUN_COD = '"+pUbic.getMunCod()+"' and PRV_PAI = '"+pUbic.getPrvPai()+"'";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            UbicacionHorasVO ubic = null;
            if(rs.next())
            {
                ubic = (UbicacionHorasVO)MappingUtils.getInstance().map(rs, UbicacionHorasVO.class);
            }
            return ubic;            
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
    }
    
    public OriTrayectoriaVO crearTrayectoriaORI(OriTrayectoriaVO tray, Connection con)throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        try
        {
            String query = null;
            //Creo el id con la secuencia
            query = "select "+ ConfigurationParameter.getParameter(ConstantesMeLanbide32.SEQ_ORI_ORI_TRAYECTORIA, ConstantesMeLanbide32.FICHERO_PROPIEDADES)+".nextval from dual";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                Integer numSec = rs.getInt(1);
                if(rs.wasNull())
                {
                    throw new Exception();
                }
                tray.setOriOritrayCod(numSec);
            }
            
            //Guardo la entidad en BD
            query = "insert into "+ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_ORI_TRAYECTORIA, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                    +"(ORI_ORITRAY_COD, ORI_ENT_COD, ORI_ORITRAY_DESCRIPCION, ORI_ORITRAY_DURACION, ORI_ORITRAY_ORGANISMO)"
                    +" values"
                    +" ("
                    +" '"+(tray.getOriOritrayCod() != null ? tray.getOriOritrayCod().toString() : "")+"',"
                    +" '"+(tray.getOriEntCod() != null ? tray.getOriEntCod().toString() : "")+"',"
                    +" '"+(tray.getOriOritrayDescripcion() != null ? tray.getOriOritrayDescripcion() : "")+"',"
                    +" '"+(tray.getOriOritrayDuracion() != null ? tray.getOriOritrayDuracion().toString().replaceAll("\\.", ",") : "")+"',"
                    +" '"+(tray.getOriOritrayOrganismo() != null ? tray.getOriOritrayOrganismo() : "")+"')";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            if(insert > 0)
            {
                try
                {
                    actualizarTrayectoriaEntidad(tray.getOriEntCod(), con);
                }
                catch(Exception ex)
                {
                    
                }
                return tray;
            }
            else
            {
                return null;
            }
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
    }
    
    public List<FilaTrayectoriaOrientacionVO> getTrayectoriasORI(Long oriEntCod, Connection con)throws Exception
    {
        
        List<FilaTrayectoriaOrientacionVO> retList = new ArrayList<FilaTrayectoriaOrientacionVO>();
        Statement st = null;
        ResultSet rs = null;
        try
        {
            String query = null;
            query = "select * from "+ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_ORI_TRAYECTORIA, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                    +" where ORI_ENT_COD = '"+oriEntCod+"'";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            FilaTrayectoriaOrientacionVO fila = null;
            while(rs.next())
            {
                try
                {
                    fila = (FilaTrayectoriaOrientacionVO)MappingUtils.getInstance().map(rs, FilaTrayectoriaOrientacionVO.class);
                    retList.add(fila);
                }
                catch(Exception ex)
                {
                    
                }
            }
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
        return retList;
    }
    
    public OriTrayectoriaVO getTrayectoriaORIPorCodigo(String codTray, Connection con)throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        try
        {
            String query = null;
            //Creo el id con la secuencia
            query = "select * from "+ ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_ORI_TRAYECTORIA, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
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
                    tray = (OriTrayectoriaVO)MappingUtils.getInstance().map(rs, OriTrayectoriaVO.class);
                }
                catch(Exception ex)
                {
                    
                }
            }
            return tray;            
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
    }
    
    public OriTrayectoriaVO modificarTrayectoriaORI(OriTrayectoriaVO tray, Connection con)throws Exception
    {
        Statement st = null;
        try
        {
            String query = null;
            
            //Guardo la entidad en BD
            query = "update "+ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_ORI_TRAYECTORIA, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                    + " set ORI_ENT_COD = '"+(tray.getOriEntCod() != null ? tray.getOriEntCod().toString() : "")+"',"
                    + " ORI_ORITRAY_DESCRIPCION = '"+(tray.getOriOritrayDescripcion() != null ? tray.getOriOritrayDescripcion() : "")+"',"
                    + " ORI_ORITRAY_DURACION = '"+(tray.getOriOritrayDuracion() != null ? tray.getOriOritrayDuracion().toString().replaceAll("\\.", ",") : "")+"',"
                    + " ORI_ORITRAY_ORGANISMO = '"+(tray.getOriOritrayOrganismo() != null ? tray.getOriOritrayOrganismo() : "")+"'"
                    + " where ORI_ORITRAY_COD = '"+(tray.getOriOritrayCod() != null ? tray.getOriOritrayCod().toString() : "")+"'";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            if(insert > 0)
            {
                try
                {
                    actualizarTrayectoriaEntidad(tray.getOriEntCod(), con);
                }
                catch(Exception ex)
                {
                    
                }
                return tray;
            }
            else
            {
                return null;
            }
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
        }
    }
    
    public int eliminarTrayectoriaORI(Integer idTray, Connection con)throws Exception
    {
        Statement st = null;
        try
        {
            String query = null;
            OriTrayectoriaVO tray = this.getTrayectoriaORIPorCodigo(idTray.toString(), con);
            query = "delete from "+ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_ORI_TRAYECTORIA, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                    +" where ORI_ORITRAY_COD = '" + idTray.toString() + "'";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            int result = st.executeUpdate(query);
            if(result > 0)
            {
                try
                {
                    actualizarTrayectoriaEntidad(tray.getOriEntCod(), con);
                }
                catch(Exception ex)
                {
                    
                }
            }
            return result;
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
        }
    }
    
    private int actualizarTrayectoriaEntidad(Long idEnt, Connection con)throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        try
        {
            String query = null;
            
            query = "select SUM(ORI_ORITRAY_DURACION) from "+ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_ORI_TRAYECTORIA, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                    +" where ORI_ENT_COD = '" + idEnt.toString() + "'";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            int anos = 0;
            if(rs.next())
            {
                anos = rs.getInt(1);
                if(anos > 99)
                    anos = 99;
            }
            
            query = "update "+ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_ENTIDAD, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                    +" set ORI_ENT_TRAYECTORIA = '"+anos+"' where ORI_ENT_COD = '"+idEnt+"'";
            return st.executeUpdate(query);
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
    }
    
    public int modificarEntidad(EntidadVO entidad, Connection con)throws Exception
    {
        Statement st = null;
        try
        {
            String query = null;
            
            query = "update "+ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_ENTIDAD, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                    +" set EXT_MUN = '"+(entidad.getExtMun() != null ? entidad.getExtMun().toString() : "")+"',"
                    +" EXT_EJE = '"+(entidad.getExtEje() != null ? entidad.getExtEje().toString() : "")+"',"
                    +" EXT_NUM = '"+(entidad.getExtNum() != null ? entidad.getExtNum() : "")+"',"
                    +" EXT_TER = '"+(entidad.getExtTer() != null ? entidad.getExtTer().toString() : "")+"',"
                    +" EXT_NVR = '"+(entidad.getExtNvr() != null ? entidad.getExtNvr().toString() : "")+"',"
                    +" ORI_ENT_ADMLOCAL = '"+(entidad.getOriEntAdmLocal() != null ? entidad.getOriEntAdmLocal() : "")+"',"
                    +" ORI_ENT_SUPRAMUN = '"+(entidad.getOriEntSupramun() != null ? entidad.getOriEntSupramun() : "")+"',"
                    +" ORI_EXP_COD = '"+(entidad.getOriExpCod() != null ? entidad.getOriExpCod().toString() : "")+"',"
                    +" ORI_ENT_OTROS = '"+(entidad.getOriEntOtros() != null ? entidad.getOriEntOtros() : "")+"',"
                    +" ORI_ENT_ACOLOCACION = '"+(entidad.getOriEntAcolocacion() != null ? entidad.getOriEntAcolocacion() : "")+"',"
                    +" ORI_ENT_NUMTRAB = '"+(entidad.getOriEntNumtrab() != null ? entidad.getOriEntNumtrab().toString().replaceAll("\\.", ",") : "")+"',"
                    +" ORI_ENT_NUMTRAB_DISC = '"+(entidad.getOriEntNumtrabDisc() != null ? entidad.getOriEntNumtrabDisc().toString().replaceAll("\\.", ",") : "")+"',"
                    +" ORI_ENT_PORTRAB_DISC = '"+(entidad.getOriEntPortrabDisc()!= null ? entidad.getOriEntPortrabDisc().toString().replaceAll("\\.", ",") : "")+"',"
                    +" ORI_ENT_TRAYECTORIA = '"+(entidad.getOriEntTrayectoria() != null ? (entidad.getOriEntTrayectoria().intValue() <= 99 ? entidad.getOriEntTrayectoria().toString() : "99") : "")+"',"
                    +" ORI_ENT_TRAYECTORIA_VAL = '"+(entidad.getOriEntTrayectoriaVal() != null ? entidad.getOriEntTrayectoriaVal().toString() : "")+"',"
                    +" ORI_ENT_NOM = '"+(entidad.getOriEntNom() != null ? entidad.getOriEntNom() : "")+"',"
                    +" ORI_ENT_ADMLOCAL_VAL = '"+(entidad.getOriEntAdmLocalVal() != null ? entidad.getOriEntAdmLocalVal() : "")+"',"
                    +" ORI_ENT_SUPRAMUN_VAL = '"+(entidad.getOriEntSupramunVal() != null ? entidad.getOriEntSupramunVal() : "")+"',"
                    +" ORI_EXP_COD_VAL = '"+(entidad.getOriExpCodVal() != null ? entidad.getOriExpCodVal().toString() : "")+"',"
                    +" ORI_ENT_ACOLOCACION_VAL = '"+(entidad.getOriEntAcolocacionVal() != null ? entidad.getOriEntAcolocacionVal() : "")+"',"
                    +" ORI_ENT_NUMTRAB_VAL = '"+(entidad.getOriEntNumtrabVal() != null ? entidad.getOriEntNumtrabVal().toString().replaceAll("\\.", ",") : "")+"',"
                    +" ORI_ENT_NUMTRAB_DISC_VAL = '"+(entidad.getOriEntNumtrabDiscVal() != null ? entidad.getOriEntNumtrabDiscVal().toString().replaceAll("\\.", ",") : "")+"',"
                    +" ORI_ENT_PORTRAB_DISC_VAL = '"+(entidad.getOriEntPortrabDiscVal() != null ? entidad.getOriEntPortrabDiscVal().toString().replaceAll("\\.", ",") : "")+"',"
                    +" ORI_ENT_OTROS_VAL = '"+(entidad.getOriEntOtrosVal() != null ? entidad.getOriEntOtrosVal() : "")+"'"
                    +" ,ORI_ENT_TIPO_ESPPERSDISC = '"+(entidad.getTipAtenPerDiscapa()!= null ? entidad.getTipAtenPerDiscapa() : "")+"'"
                    +" ,ORI_ENT_TIPO_ESPATCOLEXC = '"+(entidad.getTipAtenColRiesExc()!= null ? entidad.getTipAtenColRiesExc() : "")+"'"
                    +" ,ORI_ENT_TIPO_ENTCENTROFP = '"+(entidad.getTipCentroForProfe()!= null ? entidad.getTipCentroForProfe() : "")+"'"
                    +" ,ORI_ENT_TIPO_ENTORGSINDI = '"+(entidad.getTipOrgSindiEmpres()!= null ? entidad.getTipOrgSindiEmpres() : "")+"'"
                    +" ,ORI_ENT_TIPO_ENTNOANILUC = '"+(entidad.getTipSinAnimoLucro()!= null ? entidad.getTipSinAnimoLucro() : "")+"'"
                    +" ,ORI_ENT_TIPO_ESPPERSDISC_VAL = '"+(entidad.getTipAtenPerDiscapaVAL()!= null ? entidad.getTipAtenPerDiscapaVAL() : "")+"'"
                    +" ,ORI_ENT_TIPO_ESPATCOLEXC_VAL = '"+(entidad.getTipAtenColRiesExcVAL() != null ? entidad.getTipAtenColRiesExcVAL() : "")+"'"
                    +" ,ORI_ENT_TIPO_ENTCENTROFP_VAL = '"+(entidad.getTipCentroForProfeVAL() != null ? entidad.getTipCentroForProfeVAL() : "")+"'"
                    +" ,ORI_ENT_TIPO_ENTORGSINDI_VAL = '"+(entidad.getTipOrgSindiEmpresVAL() != null ? entidad.getTipOrgSindiEmpresVAL() : "")+"'"
                    +" ,ORI_ENT_TIPO_ENTNOANILUC_VAL = '"+(entidad.getTipSinAnimoLucroVAL() != null ? entidad.getTipSinAnimoLucroVAL() : "")+"'"
                    +" where ORI_ENT_COD = '"+entidad.getOriEntCod()+"'";
            
            if(log.isDebugEnabled())
            {
                log.debug("modificarEntidad SQL = "+query);
            }
            st = con.createStatement();
            return st.executeUpdate(query);
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
        }
    }
    
    public List<FilaUbicCentroEmpleoVO> getUbicacionesCE(int codOrganizacion, String ejercicio, String numExpediente, Connection con) throws Exception{
        log.error("getUbicacionesCE - DAO - Begin");
        String codProc = MeLanbide32Utils.getCodProcedimientoDeExpediente(numExpediente);
        log.error("getUbicacionesCE - Llamado para " + codProc);
        if("CEMP".equalsIgnoreCase(codProc)){
            return getUbicacionesCE_CEMP(codOrganizacion, ejercicio, numExpediente, con);
        }else{
            return getUbicacionesCE_Generico(codOrganizacion, ejercicio, numExpediente, con);
        }
    }
    public List<FilaUbicCentroEmpleoVO> getUbicacionesCE_Generico(int codOrganizacion, String ejercicio, String numExpediente, Connection con)throws Exception
    {
        log.error("getUbicacionesCE_Generico - DAO - Begin");
        List<FilaUbicCentroEmpleoVO> retList = new ArrayList<FilaUbicCentroEmpleoVO>();
        Statement st = null;
        ResultSet rs = null;
        Statement st2 = null;
        ResultSet rs2 = null;
        try
        {
            String query = null;
            query = "select ORI_ENT_COD from "+ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_ENTIDAD, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                    +" where EXT_MUN = '"+codOrganizacion+"' and EXT_EJE = '"+ejercicio+"' and EXT_NUM = '"+numExpediente+"'";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            String entCod = "";
            FilaUbicCentroEmpleoVO fila = null;
            while(rs.next())
            {
                //Voy recorriendo todas las entidades para recoger sus ubicaciones
                entCod = rs.getString("ORI_ENT_COD");
                if(entCod != null && !entCod.equals(""))
                {
                    query = "select * from "+ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_CE_UBIC, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                            +" where ORI_ENT_COD = '"+entCod+"'";
                    if (log.isDebugEnabled()) {
                        log.error("sql = " + query);
                    }                    
                    st2 = con.createStatement();
                    rs2 = st2.executeQuery(query);
                    
                    AmbitoCentroEmpleoVO amb = null;
                    ProvinciaVO prov = null;
                    MunicipioVO mun = null;
                    while(rs2.next())
                    {
                        fila = new FilaUbicCentroEmpleoVO();
                        fila.setEspecial(rs2.getString("ORI_CE_ESPECIAL") != null ? rs2.getString("ORI_CE_ESPECIAL").toUpperCase() : "");
                        fila.setValidacion(rs2.getString("ORI_CE_VALIDACION") != null ? rs2.getString("ORI_CE_VALIDACION").toUpperCase() : "");
                        fila.setCodigoPostal(rs2.getString("ORI_CE_CP_UBICACION") != null ? rs2.getString("ORI_CE_CP_UBICACION").toUpperCase() : "");
                        fila.setHorarioAtencion(rs2.getString("ORI_CE_HORARIO_UBICACION") != null ? rs2.getString("ORI_CE_HORARIO_UBICACION"): "");
                        fila.setOriCeUbicCod(rs2.getInt("ORI_CE_UBIC_COD"));
                        if(rs2.wasNull())
                        {
                            fila.setOriCeUbicCod(null);
                        }
                        try
                        {
                            int ambCod = rs2.getInt("ORI_AMB_COD");
                            if(!rs2.wasNull())
                            {
                                amb = this.getAmbitoCentroEmpleoPorCodigo(ambCod, con);
                                if(amb != null)
                                {
                                    fila.setAmbito(amb.getOriAmbAmbito() != null ? amb.getOriAmbAmbito().toUpperCase() : "-");
                                    prov = getProvinciaPorCodigo(amb.getOriAmbTerHis(), con);
                                    if(prov != null)
                                    {
                                        fila.setProvincia(prov.getPrvNom() != null && !prov.getPrvNom().equals("") ? prov.getPrvNom().toUpperCase() : "-");
                                    }
                                }
                            }
                            else
                            {
                                fila.setAmbito("");
                            }
                        }
                        catch(Exception ex)
                        {
                            
                        }
                        fila.setDireccion(rs2.getString("ORI_CE_DIRECCION") != null ? rs2.getString("ORI_CE_DIRECCION").toUpperCase() : "-");
                        try
                        {
                            int provCod = rs2.getInt("MUN_PRV");
                            if(!rs2.wasNull())
                            {
                                int munCod = rs2.getInt("MUN_COD");
                                if(!rs2.wasNull())
                                {
                                    mun = this.getMunicipioPorCodigoYProvincia(munCod, provCod, con);
                                    if(mun != null)
                                    {
                                        fila.setMunicipio(mun.getMunNom() != null ? mun.getMunNom().toUpperCase() : "-");
                                    }
                                }
                                else
                                {
                                    fila.setMunicipio("");
                                }
                            }
                            else
                            {
                                fila.setProvincia("");
                            }
                        }
                        catch(Exception ex)
                        {
                            
                        }
                        fila.setAdjudicado(rs2.getString("ORI_CE_ADJUDICADA") != null ? rs2.getString("ORI_CE_ADJUDICADA").toUpperCase() : "-");
                        retList.add(fila);
                    }
                }
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando las ubicaciones", ex);
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
            if(st2!=null) 
                st2.close();
            if(rs2!=null) 
                rs2.close();
        }
        return retList;
    }
    
    public List<FilaUbicCentroEmpleoVO> getUbicacionesCE_CEMP(int codOrganizacion, String ejercicio, String numExpediente, Connection con)throws Exception
    {
        log.error("getUbicacionesCE_CEMP - DAO - Begin");
        List<FilaUbicCentroEmpleoVO> retList = new ArrayList<FilaUbicCentroEmpleoVO>();
        PreparedStatement ps=null;
        ResultSet rs = null;
        try
        {
            String query = "SELECT " +
                        " ORI_CE_UBIC.ORI_CE_UBIC_COD,ORI_CE_UBIC.ORI_CE_ESPECIAL, ORI_CE_UBIC.ORI_AMB_COD,ORI_AMBITOS_CE.ORI_AMB_AMBITO AMBITO " +
                        " ,ORI_CE_UBIC.MUN_PRV, T_PRV.PRV_NOM PROVINCIA,ORI_CE_UBIC.MUN_COD, T_MUN.MUN_NOM MUNICIPIO " +
                        " ,ORI_CE_DIRECCION,ORI_CE_ADJUDICADA,ORI_CE_VALIDACION,ORI_CE_CP_UBICACION,ORI_CE_HORARIO_UBICACION " +
                        " ,ORI_CE_APROBADO, ORI_CE_MANTENIDO" +
                        " ,ORI_CE_UBIC_NUM_EXP,ORI_CE_UBIC.ORI_ENT_COD " +
                        " ,ORI_CE_LOCAL_NUEVO_VALIDADO " +
                        " FROM " +
                        " ORI_CE_UBIC " +
                        " LEFT JOIN ORI_AMBITOS_CE ON ORI_CE_UBIC.ORI_AMB_COD = ORI_AMBITOS_CE.ORI_AMB_COD " +
                        " LEFT JOIN FLBGEN.T_PRV T_PRV ON T_PRV.PRV_PAI=ORI_CE_UBIC.PRV_PAI AND T_PRV.PRV_COD=ORI_CE_UBIC.MUN_PRV " +
                        " LEFT JOIN FLBGEN.T_MUN T_MUN ON T_MUN.MUN_PAI=ORI_CE_UBIC.MUN_PAI AND T_MUN.MUN_PRV=ORI_CE_UBIC.MUN_PRV AND T_MUN.MUN_COD = ORI_CE_UBIC.MUN_COD " +
                        " WHERE ORI_CE_UBIC_NUM_EXP=?";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            if(log.isDebugEnabled()) 
                log.error("sql-params= " + numExpediente);
            ps = con.prepareStatement(query);
            ps.setString(1,numExpediente);
            rs = ps.executeQuery();
            FilaUbicCentroEmpleoVO fila = new FilaUbicCentroEmpleoVO();
            while(rs.next())
            {
                fila.setOriCeUbicCod(rs.getInt("ORI_CE_UBIC_COD"));
                if (rs.wasNull()) {
                    fila.setOriCeUbicCod(null);
                }
                fila.setEspecial(rs.getString("ORI_CE_ESPECIAL"));
                fila.setProvincia(rs.getString("PROVINCIA"));
                fila.setAmbito(rs.getString("AMBITO"));
                fila.setMunicipio(rs.getString("MUNICIPIO"));
                fila.setDireccion(rs.getString("ORI_CE_DIRECCION"));
                fila.setAdjudicado(rs.getString("ORI_CE_ADJUDICADA"));
                fila.setValidacion(rs.getString("ORI_CE_VALIDACION"));
                fila.setCodigoPostal(rs.getString("ORI_CE_CP_UBICACION"));
                fila.setHorarioAtencion(rs.getString("ORI_CE_HORARIO_UBICACION"));
                fila.setAprobado(rs.getString("ORI_CE_APROBADO"));
                fila.setMantenido(rs.getString("ORI_CE_MANTENIDO"));
                fila.setNumExpediente(rs.getString("ORI_CE_UBIC_NUM_EXP"));
                fila.setCodigoEntidad(rs.getInt("ORI_ENT_COD"));
                if (rs.wasNull()) {
                    fila.setCodigoEntidad(null);
                }
                fila.setPuntuacionCentroE(getPuntuacionUbicacionCE(fila.getOriCeUbicCod(),con));
                fila.setLocalNuevoValidado(rs.getString("ORI_CE_LOCAL_NUEVO_VALIDADO"));
                retList.add(fila);
                fila= new FilaUbicCentroEmpleoVO();
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando las ubicaciones", ex);
            throw new Exception(ex);
        }
        finally
        {
            if(log.isDebugEnabled()) 
                log.debug("Procedemos a cerrar el statement y el resultset");
            if(ps!=null) 
                ps.close();
            if(rs!=null) 
                rs.close();
        }
        return retList;
    }
    
    public List<CeConvocatoriaVO> getConvocatoriasCE_CEMP(Connection con)throws Exception
    {
        log.error("getConvocatoriasCE_CEMP - DAO - Begin");
        List<CeConvocatoriaVO> convocatoriasCEMP = new ArrayList<CeConvocatoriaVO>();
        PreparedStatement ps=null;
        ResultSet rs = null;
        try
        {
            String query = "SELECT ORI_CECON_COD_ID,ORI_CECON_DESCRIPCION,ORI_CECON_ANIOCONVOCATORIA FROM ORI_CE_CONVOCATORIAS WHERE ORI_CECON_COD_ID > 0 ORDER BY ORI_CECON_ANIOCONVOCATORIA";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            CeConvocatoriaVO convocatoria = new CeConvocatoriaVO();
            while(rs.next())
            {
                convocatoria.setOriCeconCodId(rs.getInt("ORI_CECON_COD_ID"));
                convocatoria.setOriCeconDescripcion(rs.getString("ORI_CECON_DESCRIPCION"));
                convocatoria.setOriCeconAnioconvocatoria(rs.getInt("ORI_CECON_ANIOCONVOCATORIA"));
                convocatoriasCEMP.add(convocatoria);
                convocatoria= new CeConvocatoriaVO();
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando las convocatorias de CEMP", ex);
            throw new Exception(ex);
        }
        finally
        {
            if(log.isDebugEnabled()) 
                log.debug("Procedemos a cerrar el statement y el resultset");
            if(ps!=null) 
                ps.close();
            if(rs!=null) 
                rs.close();
        }
        return convocatoriasCEMP;
    }
    
    public List<FilaTrayectoriaCentroEmpleoVO> getTrayectoriasCE(Long oriEntCod, String procedimiento, Connection con)throws Exception
    {
        
        List<FilaTrayectoriaCentroEmpleoVO> retList = new ArrayList<FilaTrayectoriaCentroEmpleoVO>();
        Statement st = null;
        ResultSet rs = null;
        try
        {
            String query = null;
            if(procedimiento.equals(ConstantesMeLanbide32.CODIGO_PROCEDIMIENTO_CEMP)){
                int noTrayectorias = getCountTrayectoriasCExCodEnt(oriEntCod,con);
                int ejercicio = getEjercicioEntidadTablaEntidad(oriEntCod, con);
                if(noTrayectorias == 0)
                {
                    /*CeTrayectoriaVO trayectoriaPrecargada = new CeTrayectoriaVO();
                    Field[] variables = ConstantesMeLanbide32.class.getFields();
                    int totalConvocatoriasCEMP = 0;
                    for(Field v:variables){
                        if(v.getName().startsWith("TRAYECTORIA_PRECARGA_"))
                            totalConvocatoriasCEMP++;
                    }
                    log.debug("Variables de textos de convocatorias leidas d ela clase constantes melambide32 : " + totalConvocatoriasCEMP);
                    for(int x=0; x<totalConvocatoriasCEMP;x++)
                    {
                        trayectoriaPrecargada.setOriEntCod(oriEntCod);
                        switch (x)
                        {
                            case 0:
                                trayectoriaPrecargada.setOriCetraDescripcion(ConstantesMeLanbide32.TRAYECTORIA_PRECARGA_1);
                                break;
                            case 1:
                              trayectoriaPrecargada.setOriCetraDescripcion(ConstantesMeLanbide32.TRAYECTORIA_PRECARGA_2);
                              break;
                            case 2:
                              trayectoriaPrecargada.setOriCetraDescripcion(ConstantesMeLanbide32.TRAYECTORIA_PRECARGA_3);
                              break;
                            case 3:
                              trayectoriaPrecargada.setOriCetraDescripcion(ConstantesMeLanbide32.TRAYECTORIA_PRECARGA_4);
                              break;
                            case 4:
                              trayectoriaPrecargada.setOriCetraDescripcion(ConstantesMeLanbide32.TRAYECTORIA_PRECARGA_5);
                              break;
                        }
                        trayectoriaPrecargada.setOriCetraDuracion(BigDecimal.ZERO);
                        trayectoriaPrecargada.setOriCetraDuracionValidada(BigDecimal.ZERO);
                        //ignoramos la convocatoria 2017 para expediente sde a?os anteriores
                        if(ejercicio<2017 && x > 3){
                            log.debug("Ignoramos la creacion de la Convoacatorias  2017 y posteirores para expedientes con entidades en a?os anteirores al 2017");
                        }else
                            crearTrayectoriaCE(trayectoriaPrecargada,con);
                        trayectoriaPrecargada = new CeTrayectoriaVO();
                    }*/
                    
                    //se recogen las convocatorias de BBDD en lugar de las constantes
                    List<CeConvocatoriaVO> convocatorias = new ArrayList<CeConvocatoriaVO>();
                    convocatorias = getConvocatoriasCE_CEMP(con);
                    
                    CeTrayectoriaVO trayectoria = new CeTrayectoriaVO();
                    int totalConvocatoriasCEMP = convocatorias.size();
                    log.debug("Variables de textos de convocatorias de la tabla ORI_CE_CONVOCATORIAS : " + totalConvocatoriasCEMP);
                    for(int i=0; i<totalConvocatoriasCEMP;i++)
                    {
                        trayectoria.setOriEntCod(oriEntCod);
                        trayectoria.setOriCetraDescripcion(convocatorias.get(i).getOriCeconDescripcion());  
                        trayectoria.setOriCetraDuracion(BigDecimal.ZERO);
                        trayectoria.setOriCetraDuracionValidada(BigDecimal.ZERO);
                        trayectoria.setOriCetraConvocatoria(convocatorias.get(i).getOriCeconCodId());
                        //ignoramos la convocatoria 2017 para expedientes de años anteriores
                        if(ejercicio<2017 && i > 3){
                            log.debug("Ignoramos la creación de la Convocatoria 2017 y posteriores para expedientes con entidades en años anteriores al 2017");
                        }else if(ejercicio<2018 && i > 4){
                            log.debug("Ignoramos la creación de la Convocatoria 2018 y posteriores para expedientes con entidades en años anteriores al 2018");
                        }else
                            crearTrayectoriaCE(trayectoria,con);
                        trayectoria = new CeTrayectoriaVO();
                    }
                    
                }                
            }
            query = "SELECT " +
                    "    trayectoria.ori_ce_cod, " +
                    "    trayectoria.ori_ent_cod, " +
                    "    CASE WHEN SUBSTR(NVL(ORI_CE_TRAY_NUM_EXP,'0000'),0,4) >= 2018 THEN " +
                    "        ORI_CE_CONVOCATORIAS.ORI_CECON_DESCRIPCION " +
                    "    ELSE " +
                    "        trayectoria.ori_cetra_descripcion " +
                    "    END ori_cetra_descripcion, " +
                    "    trayectoria.ori_cetra_duracion, " +
                    "    trayectoria.ori_ce_tray_num_exp, " +
                    "    trayectoria.ori_cetra_duracion_val, " +
                    "    trayectoria.ori_cetra_convocatoria " +
                    " from "+ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_CE_TRAYECTORIA, ConstantesMeLanbide32.FICHERO_PROPIEDADES) + " trayectoria " +
                    " LEFT JOIN ORI_CE_CONVOCATORIAS on TRAYECTORIA.ORI_CETRA_CONVOCATORIA=ORI_CE_CONVOCATORIAS.ORI_CECON_COD_ID " +
                    " where trayectoria.ORI_ENT_COD = '" +oriEntCod+ "'" + 
                    " order by trayectoria.ORI_CETRA_CONVOCATORIA, trayectoria.ORI_CE_COD " 
                ;
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            FilaTrayectoriaCentroEmpleoVO fila = null;
            while(rs.next())
            {
                try
                {
                    fila = (FilaTrayectoriaCentroEmpleoVO)MappingUtils.getInstance().map(rs, FilaTrayectoriaCentroEmpleoVO.class);
                    retList.add(fila);
                }
                catch(Exception ex)
                {
                    log.error("Error recuperando la lista de trayectorias", ex);
                }
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando las trayectorias", ex);
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
        return retList;
    }
    
    public int getCountTrayectoriasCExCodEnt(Long oriEntCod, Connection con)throws Exception
    {
        
        int noTrayectorias  = 0;
        Statement st = null;
        ResultSet rs = null;
        try
        {
            String query = null;
            query = "select Count(*) as noTrayectorias from "+ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_CE_TRAYECTORIA, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                    +" where ORI_ENT_COD = '"+oriEntCod+"'";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while(rs.next())
            {
                try
                {
                    noTrayectorias =  rs.getInt("noTrayectorias");
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando Numero Trayectorias de Un Entidad ", ex);
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
        return noTrayectorias;
    }
    public int getEjercicioEntidadTablaEntidad(Long oriEntCod, Connection con)throws Exception
    {
        
        int ejercicio  = 0;
        Statement st = null;
        ResultSet rs = null;
        try
        {
            String query = null;
            query = "select EXT_EJE as ejercicio from "+ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_ENTIDAD, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                    +" where ORI_ENT_COD = '"+oriEntCod+"'";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while(rs.next())
            {
                try
                {
                    ejercicio =  rs.getInt("ejercicio");
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando Ejerici de una Entidad ", ex);
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
        return ejercicio;
    }
    
    public CeUbicacionVO crearUbicacionCE(CeUbicacionVO ubic, Connection con)throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        try
        {
            String query = null;
            //Creo el id con la secuencia
            query = "select "+ ConfigurationParameter.getParameter(ConstantesMeLanbide32.SEQ_ORI_CE_UBICACION, ConstantesMeLanbide32.FICHERO_PROPIEDADES)+".nextval from dual";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                Integer numSec = rs.getInt(1);
                if(rs.wasNull())
                {
                    throw new Exception();
                }
                ubic.setOriCeUbicCod(numSec);
            }
            
            //Guardo la entidad en BD
            query = "insert into "+ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_CE_UBIC, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                    +"(ORI_CE_UBIC_COD, ORI_ENT_COD, ORI_AMB_COD, MUN_PAI, MUN_PRV, MUN_COD, ORI_CE_DIRECCION,"
                    + " ORI_CE_ESPECIAL, PRV_PAI, ORI_CE_VALIDACION, ORI_CE_CP_UBICACION,ORI_CE_HORARIO_UBICACION,"
                    + " ORI_CE_APROBADO, ORI_CE_MANTENIDO, ORI_CE_UBIC_NUM_EXP, ORI_CE_LOCAL_NUEVO_VALIDADO) values"
                    +" ('"
                    +ubic.getOriCeUbicCod().toString()
                    +"', '"+(ubic.getOriEntCod() != null ? ubic.getOriEntCod().toString() : "")
                    +"', '"+(ubic.getOriAmbCod() != null ? ubic.getOriAmbCod().toString() : "")
                    +"', '"+(ubic.getMunPai() != null ? ubic.getMunPai().toString() : "")
                    +"', '"+(ubic.getMunPrv() != null ? ubic.getMunPrv().toString() : "")
                    +"', '"+(ubic.getMunCod() != null ? ubic.getMunCod().toString() : "")
                    +"', '"+ubic.getOriCeDireccion()
                    +"', '"+ubic.getOriCeEspecial()
                    +"', "+(ubic.getPrvPai() != null ? ubic.getPrvPai().toString() : null)
                    +", '"+ubic.getValidacion() + "'"
                    +", '"+ubic.getCodigoPostal() + "'"
                    +", '"+ubic.getHorarioAtencion() + "'"
                    +", '"+(ubic.getAprobado()!=null && ubic.getAprobado()!="" ? ubic.getAprobado() : "")+ "'"
                    +", '"+(ubic.getMantenido()!=null && ubic.getMantenido()!="" ? ubic.getMantenido() : "") + "'"
                    +", '"+ubic.getNumeroExpediente()+ "'"
                    +", '"+(ubic.getLocalNuevoValidado()!=null && !ubic.getLocalNuevoValidado().isEmpty() ? ubic.getLocalNuevoValidado() : "")+ "'"
                    +")";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            if(insert > 0)
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
    }
    
    public CeUbicacionVO getUbicacionCEPorCodigo(String codUbic, Connection con)throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        try
        {
            String query = null;
            //Creo el id con la secuencia
            query = "select * from "+ ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_CE_UBIC, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                    +" where ORI_CE_UBIC_COD = '" + codUbic + "'";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            CeUbicacionVO ubic = null;
            if(rs.next())
            {
                ubic = (CeUbicacionVO)MappingUtils.getInstance().map(rs, CeUbicacionVO.class);
            }
            return ubic;            
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando las Ubicaciones", ex);
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
    
    public CeUbicacionVO modificarUbicacionCE(CeUbicacionVO ubic, Connection con)throws Exception
    {
        Statement st = null;
        try
        {
            String query = null;
            
            //Guardo la entidad en BD
            query = "update "+ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_CE_UBIC, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                    + " set ORI_ENT_COD = '"+(ubic.getOriEntCod() != null ? ubic.getOriEntCod().toString() : "")+"',"
                    + " ORI_AMB_COD = '"+(ubic.getOriAmbCod() != null ? ubic.getOriAmbCod().toString() : "")+"',"
                    + " MUN_PAI = '"+(ubic.getMunPai() != null ? ubic.getMunPai().toString() : "")+"',"
                    + " MUN_PRV = '"+(ubic.getMunPrv() != null ? ubic.getMunPrv().toString() : "")+"',"
                    + " MUN_COD = '"+(ubic.getMunCod() != null ? ubic.getMunCod().toString() : "")+"',"
                    + " ORI_CE_DIRECCION = '"+(ubic.getOriCeDireccion() != null ? ubic.getOriCeDireccion() : "")+"',"
                    + " ORI_CE_ADJUDICADA = '"+(ubic.getOriCeAdjudicada() != null ? ubic.getOriCeAdjudicada() : "")+"',"
                    + " ORI_CE_ESPECIAL = '"+(ubic.getOriCeEspecial() != null ? ubic.getOriCeEspecial() : "")+"',"
                    + " ORI_CE_VALIDACION = '"+(ubic.getValidacion()!= null ? ubic.getValidacion() : "")+"',"
                    + " PRV_PAI = '"+(ubic.getPrvPai() != null ? ubic.getPrvPai().toString() : "")+"'"
                    + " ,ORI_CE_CP_UBICACION = '"+(ubic.getCodigoPostal()!= null ? ubic.getCodigoPostal(): "")+"'"
                    + " ,ORI_CE_HORARIO_UBICACION = '"+(ubic.getHorarioAtencion()!= null ? ubic.getHorarioAtencion() : "")+"'"
                    + " , ORI_CE_APROBADO = '" + (ubic.getAprobado() != null ? ubic.getAprobado() : "")+"'"
                    + " , ORI_CE_MANTENIDO = '" + (ubic.getMantenido() != null ? ubic.getMantenido() : "")+"' "
                    + " , ORI_CE_LOCAL_NUEVO_VALIDADO = '" + (ubic.getLocalNuevoValidado() != null && !ubic.getLocalNuevoValidado().isEmpty() ? ubic.getLocalNuevoValidado() : "")+"' "
                    + " where ORI_CE_UBIC_COD = '"+ubic.getOriCeUbicCod().toString()+"'";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            if(insert > 0)
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
            log.error("Se ha producido un error modificando las ubicaciones", ex);
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
    
    public int eliminarUbicacionCE(Integer idUbic, Connection con)throws Exception
    {
        Statement st = null;
        try
        {
            String query = null;
            
            //Guardo la entidad en BD
            query = "delete from "+ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_CE_UBIC, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                    +" where ORI_CE_UBIC_COD = '" + idUbic.toString() + "'";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            return st.executeUpdate(query);
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
        }
    }
    
    public CeTrayectoriaVO crearTrayectoriaCE(CeTrayectoriaVO tray, Connection con)throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        try
        {
            String query = null;
            //Creo el id con la secuencia
            query = "select "+ ConfigurationParameter.getParameter(ConstantesMeLanbide32.SEQ_ORI_CE_TRAYECTORIA, ConstantesMeLanbide32.FICHERO_PROPIEDADES)+".nextval from dual";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                Integer numSec = rs.getInt(1);
                if(rs.wasNull())
                {
                    throw new Exception();
                }
                tray.setOriCeCod(numSec);
            }
            
            //Guardo la entidad en BD
            //se añade campo de convocatoria
            query = "insert into "+ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_CE_TRAYECTORIA, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                    +"(ORI_CE_COD, ORI_ENT_COD, ORI_CETRA_DESCRIPCION, ORI_CETRA_DURACION,ORI_CETRA_DURACION_VAL,ORI_CETRA_CONVOCATORIA)"
                    +" values"
                    +" ("
                    +" '"+(tray.getOriCeCod() != null ? tray.getOriCeCod().toString() : "")+"',"
                    +" '"+(tray.getOriEntCod() != null ? tray.getOriEntCod().toString() : "")+"',"
                    +" '"+(tray.getOriCetraDescripcion() != null ? tray.getOriCetraDescripcion() : "")+"',"
                    +" '"+(tray.getOriCetraDuracion() != null ? tray.getOriCetraDuracion().toString().replaceAll("\\.", ",") : "")+"'"
                    +" ,'"+(tray.getOriCetraDuracionValidada()!= null ? tray.getOriCetraDuracionValidada().toString().replaceAll("\\.", ",") : "")+"',"
                    +" '"+(tray.getOriCetraConvocatoria() != null ? tray.getOriCetraConvocatoria().toString() : "")+"'"
                    + ")"
                    ;
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            if(insert > 0)
            {
                return tray;
            }
            else
            {
                return null;
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando las trayectorias", ex);
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
    
    public CeTrayectoriaVO getTrayectoriaCEPorCodigo(String codTray, Connection con)throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        try
        {
            String query = null;
            //Creo el id con la secuencia
            query = "SELECT "
                    + "    trayectoria.ori_ce_cod, "
                    + "    trayectoria.ori_ent_cod, "
                    + "    CASE WHEN SUBSTR(NVL(ORI_CE_TRAY_NUM_EXP,'0000'),0,4) >= 2018 THEN "
                    + "        ORI_CE_CONVOCATORIAS.ORI_CECON_DESCRIPCION "
                    + "    ELSE "
                    + "        trayectoria.ori_cetra_descripcion "
                    + "    END ori_cetra_descripcion, "
                    + "    trayectoria.ori_cetra_duracion, "
                    + "    trayectoria.ori_ce_tray_num_exp, "
                    + "    trayectoria.ori_cetra_duracion_val, "
                    + "    trayectoria.ori_cetra_convocatoria "
                    + " from " + ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_CE_TRAYECTORIA, ConstantesMeLanbide32.FICHERO_PROPIEDADES) + " trayectoria "
                    + " LEFT JOIN ORI_CE_CONVOCATORIAS on TRAYECTORIA.ORI_CETRA_CONVOCATORIA=ORI_CE_CONVOCATORIAS.ORI_CECON_COD_ID "
                    +" where trayectoria.ORI_CE_COD = '" + codTray + "'";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            CeTrayectoriaVO tray = null;
            if(rs.next())
            {
                try
                {
                    tray = (CeTrayectoriaVO)MappingUtils.getInstance().map(rs, CeTrayectoriaVO.class);
                }
                catch(Exception ex)
                {
                    
                }
            }
            return tray;            
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
    }
    
    public CeTrayectoriaVO modificarTrayectoriaCE(CeTrayectoriaVO tray, Connection con)throws Exception
    {
        Statement st = null;
        try
        {
            String query = null;
            
            //Guardo la entidad en BD
            query = "update "+ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_CE_TRAYECTORIA, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                    + " set ORI_ENT_COD = '"+(tray.getOriEntCod() != null ? tray.getOriEntCod().toString() : "")+"',"
                    + " ORI_CETRA_DESCRIPCION = '"+(tray.getOriCetraDescripcion() != null ? tray.getOriCetraDescripcion() : "")+"',"
                    + " ORI_CETRA_DURACION = '"+(tray.getOriCetraDuracion() != null ? tray.getOriCetraDuracion().toString().replaceAll("\\.", ",") : "")+"'"
                    + " ,ORI_CETRA_DURACION_VAL = '"+(tray.getOriCetraDuracionValidada()!= null ? tray.getOriCetraDuracionValidada().toString().replaceAll("\\.", ",") : "")+"'"
                    + " where ORI_CE_COD = '"+(tray.getOriCeCod() != null ? tray.getOriCeCod().toString() : "")+"'";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            if(insert > 0)
            {
                try
                {
                    actualizarTrayectoriaEntidad(tray.getOriEntCod(), con);
                }
                catch(Exception ex)
                {
                    
                }
                return tray;
            }
            else
            {
                return null;
            }
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
        }
    }
    
    public int eliminarTrayectoriaCE(Integer idTray, Connection con)throws Exception
    {
        Statement st = null;
        try
        {
            String query = null;
            query = "delete from "+ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_CE_TRAYECTORIA, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                    +" where ORI_CE_COD = '" + idTray.toString() + "'";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            return st.executeUpdate(query);
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
            query = "select ORI_ESP_COD, ORI_ESP_DESC from "+ ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_ESPECIALIDADES, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
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
            query = "select distinct ORI_AMB_TERHIS from "+ ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_AMBITOS_HORAS, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
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
            query = "select distinct ORI_AMB_TERHIS from "+ ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_AMBITOS_CE, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
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
        return retList;
    }
    
    public List<AmbitoHorasVO> getAmbitosHorasPorProvincia(Integer codProvincia, Integer anoConv, Connection con)throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        List<AmbitoHorasVO> retList = new ArrayList<AmbitoHorasVO>();
        try
        {
            String query = null;
            query = "select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_AMBITOS_HORAS, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                    + " where ORI_AMB_TERHIS = '" + codProvincia + "' and ORI_AMB_ANOCONV = '" + anoConv + "' ORDER BY ORI_AMB_AMBITO";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            AmbitoHorasVO ambito = null;
            while(rs.next())
            {
                ambito = (AmbitoHorasVO)MappingUtils.getInstance().map(rs, AmbitoHorasVO.class);
                if(ambito != null)
                {
                    retList.add(ambito);
                }
            }
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
        return retList;
    }
    
    public List<AmbitoCentroEmpleoVO> getAmbitosCentroEmpleoPorProvincia(Integer codProvincia, Integer anoConv, Connection con)throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        List<AmbitoCentroEmpleoVO> retList = new ArrayList<AmbitoCentroEmpleoVO>();
        try
        {
            String query = null;
            query = "select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_AMBITOS_CE, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                    + " where ORI_AMB_TERHIS = '" + codProvincia + "' and ORI_AMB_ANOCONV = '" + anoConv + "' ORDER BY ORI_AMB_AMBITO";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            AmbitoCentroEmpleoVO ambito = null;
            while(rs.next())
            {
                ambito = (AmbitoCentroEmpleoVO)MappingUtils.getInstance().map(rs, AmbitoCentroEmpleoVO.class);
                if(ambito != null)
                {
                    retList.add(ambito);
                }
            }
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
        return retList;
    }
    
    public List<OriUbicacionVO> getUbicacionesDeAmbitoORI(AmbitoHorasVO amb, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        List<OriUbicacionVO> retList = new ArrayList<OriUbicacionVO>();
        try
        {
            String query = null;
            /*query = "select * from "+ ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_ORI_UBIC, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                    +" where ORI_AMB_COD = '"+amb.getOriAmbCod().toString()+"'";*/
            
            
            query = "select ubic.* from "+ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_ORI_UBIC, ConstantesMeLanbide32.FICHERO_PROPIEDADES)+" ubic "
                    +" inner join "+ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_ENTIDAD, ConstantesMeLanbide32.FICHERO_PROPIEDADES)+" ent"
                    +" on ubic.ori_ent_cod = ent.ori_ent_cod"
                    +" where ORI_AMB_COD = "+amb.getOriAmbCod()+" order by ent.ori_ent_nom, ent.ori_ent_cod, ubic.ori_orient_puntuacion desc";
            
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            OriUbicacionVO ubic = null;
            while(rs.next())
            {
                ubic = (OriUbicacionVO)MappingUtils.getInstance().map(rs, OriUbicacionVO.class);
                if(ubic != null)
                {
                    retList.add(ubic);
                }
            }
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
        return retList;
    }
    
    public List<CeUbicacionVO> getUbicacionesDeAmbitoCE(AmbitoCentroEmpleoVO amb, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        List<CeUbicacionVO> retList = new ArrayList<CeUbicacionVO>();
        try
        {
            String query = null;
            query = "select * from "+ ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_CE_UBIC, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                    +" where ORI_AMB_COD = '"+amb.getOriAmbCod().toString()+"'";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            CeUbicacionVO ubic = null;
            while(rs.next())
            {
                ubic = (CeUbicacionVO)MappingUtils.getInstance().map(rs, CeUbicacionVO.class);
                if(ubic != null)
                {
                    retList.add(ubic);
                }
            }
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
        return retList;
    }
    
    public List<CeUbicacionVO> getUbicacionesDeAmbitoCEMP2014(AmbitoCentroEmpleoVO amb, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        List<CeUbicacionVO> retList = new ArrayList<CeUbicacionVO>();
        try
        {
            String query = null;
            if(amb!=null)
                log.debug("amb.getOriAmbAnoconv() " + amb.getOriAmbAnoconv());
            else 
                log.debug("getUbicacionesDeAmbitoCEMP2014 - parametro amb viene null.");
            if(amb!=null && amb.getOriAmbAnoconv()>=2017){
                log.debug("Convocatorias dede 2017 - Generamos Nueva Select.");
                query="SELECT ubicacioncentro.* " 
                        +" , adjudicaciontemp.ORI_ORDEN " 
                        +" FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_CE_UBIC, ConstantesMeLanbide32.FICHERO_PROPIEDADES) + " ubicacioncentro "
                        +" LEFT JOIN " + ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_TMP_ADJUDICACION_CENTROS_DSD2017, ConstantesMeLanbide32.FICHERO_PROPIEDADES) + " adjudicaciontemp "
                        +" ON adjudicaciontemp.ORI_AMB_COD=ubicacioncentro.ORI_AMB_COD AND ubicacioncentro.ORI_CE_UBIC_COD=adjudicaciontemp.ORI_CE_UBIC_COD  "
                        +"  AND adjudicaciontemp.CONVOCATORIA="+(amb.getOriAmbAnoconv()!=null?amb.getOriAmbAnoconv():"null")
                        +" WHERE ubicacioncentro.ORI_AMB_COD = '"+amb.getOriAmbCod().toString()+"'"
                        +" ORDER BY adjudicaciontemp.ori_ce_adjudicado  nulls last, adjudicaciontemp.puntuacionubicacioncentro desc nulls last, adjudicaciontemp.ORI_ORDEN NULLS LAST, ubicacioncentro.ori_ce_especial desc nulls last "
                    ;
            }else{
                query = " SELECT " + ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_CE_UBIC, ConstantesMeLanbide32.FICHERO_PROPIEDADES) +  ".* " 
                        + " , ORI_ORDEN " 
                        + " FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_CE_UBIC, ConstantesMeLanbide32.FICHERO_PROPIEDADES) 
                        + " LEFT JOIN " + ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_TMP_ADJUDICACION_CENTROS, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                        + " ON " + ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_TMP_ADJUDICACION_CENTROS, ConstantesMeLanbide32.FICHERO_PROPIEDADES) 
                        +".ORI_AMB_COD=" + ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_CE_UBIC, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                        +".ORI_AMB_COD AND " + ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_CE_UBIC, ConstantesMeLanbide32.FICHERO_PROPIEDADES) 
                        +".ORI_CE_UBIC_COD=" + ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_TMP_ADJUDICACION_CENTROS, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                        +".ORI_CE_UBIC_COD "
                        + " WHERE " + ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_CE_UBIC, ConstantesMeLanbide32.FICHERO_PROPIEDADES) 
                        + ".ORI_AMB_COD = '"+amb.getOriAmbCod().toString()+"'"
                        + " ORDER BY ORI_ORDEN";
            }
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            CeUbicacionVO ubic = new CeUbicacionVO();
            while(rs.next())
            {

                // 2017/11/23 Añadimos columa ORI_ORDEN en clase util mapeo evitar mapear a mano aqui.
                ubic = (CeUbicacionVO)MappingUtils.getInstance().map(rs, CeUbicacionVO.class);
                if(ubic != null)
                {
                    retList.add(ubic);
                    ubic = new CeUbicacionVO();
                }
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando las areas", ex);
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
        return retList;
    }
    
    public String[] adjudicaOrientacion(Integer ano, Connection con) throws Exception
    {
        CallableStatement st = null;
        try
        {
            String query = null;
            query = "call adjudica_orientacion(?,?,?)";
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
            log.error("Se ha producido un error recuperando las áreas", ex);
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
    
    public String consolidaHoras(Connection con) throws Exception
    {
        CallableStatement st = null;
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
            log.error("Se ha producido un error recuperando las áreas", ex);
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
    
    public String deshacerConsolidacionHoras(Integer ano, Connection con) throws Exception
    {
        CallableStatement st = null;
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
            log.error("Se ha producido un error recuperando las áreas", ex);
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
    
    public String[] adjudicaCentros(Integer ano, Connection con) throws Exception
    {
        CallableStatement st = null;
        try
        {
            String query = null;
            query = "call adjudica_centros(?,?,?)";
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
            log.error("Se ha producido un error recuperando las áreas", ex);
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
    
    public String consolidaCentros(Connection con) throws Exception
    {
        CallableStatement st = null;
        try
        {
            String query = null;
            query = "call consolida_centros(?)";
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
            log.error("Se ha producido un error recuperando las áreas", ex);
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
    
    public String deshacerConsolidacionCentros(Integer ano, Connection con) throws Exception
    {
        CallableStatement st = null;
        try
        {
            String query = null;
            query = "call elimina_consolidacion_centros(?, ?)";
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
            log.error("Se ha producido un error recuperando las áreas", ex);
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
    
    public String[] adjudicaCentrosCemp(Integer ano, Connection con) throws Exception
    {
        CallableStatement st = null;
        try
        {
            String query = null;
            if(ano>=2023){
                query="call CEMP_ADJUDICA_CENTROS_DSD2023(?,?,?)";
            }else if(ano>=2017 && ano<=2022){
                query="call CEMP_ADJUDICA_CENTROS_DSD2017(?,?,?)";
            }else
                query = "call cemp_adjudica_centros(?,?,?)";
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
            log.error("Se ha producido un error recuperando las áreas", ex);
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
    
    public String consolidaCentrosCemp(Integer ano,Connection con) throws Exception
    {
        CallableStatement st = null;
        try
        {
            String query = null;
            String result="";
            if(ano!=null && ano>=2017){
                query = "call consolida_centrosCEMP_dsd2017(?,?)";
                if(log.isDebugEnabled()) {
                    log.error("sql = " + query);
                    log.debug("parametros Entrada : " + ano);
                }
                st = con.prepareCall(query);
                st.setInt(1,ano);
                st.registerOutParameter(2, java.sql.Types.VARCHAR);
                int i = st.executeUpdate();
                result = st.getString(2);
            } else{
                query = "call consolida_centros(?)";
                if(log.isDebugEnabled()) 
                    log.error("sql = " + query);
                st = con.prepareCall(query);
                st.registerOutParameter(1, java.sql.Types.VARCHAR);
                int i = st.executeUpdate();
                result = st.getString(1);
            }
            
            return result;
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
        }
    }
      
    public String deshacerConsolidacionCentrosCemp(Integer ano, Connection con) throws Exception
    {
        CallableStatement st = null;
        try
        {
            String query = null;
            query = "call elimina_consolidacion_centros(?, ?)";
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
            log.error("Se ha producido un error recuperando las áreas", ex);
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
    
    public EspecialidadVO getEspecialidadPorCodigo(Integer codEsp, Connection con) throws Exception
    {
        EspecialidadVO vo = null;
        Statement st = null;
        ResultSet rs = null;
        try
        {
            String query = null;
            query = "select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_ESPECIALIDADES, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                        +" where ORI_ESP_COD = '" + codEsp + "'";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                try
                {
                    vo = (EspecialidadVO)MappingUtils.getInstance().map(rs, EspecialidadVO.class);
                }
                catch(Exception ex)
                {

                }
            }
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
        return vo;
    }
    
    public BigDecimal getHorasAsignadasUbicacionORI(Integer codUbic, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        try
        {
            String query = null;
            query = "select HORAS_ASIGNADAS from " + ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_TMP_ADJUDICACION, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
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
        return null;
    }
    
    public String getUbicacionAdjudicadaCE(Integer codUbic, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        try
        {
            String query = null;
            query = "select ORI_CE_ADJUDICADO from " + ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_TMP_ADJUDICACION_CENTROS, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                        +" where ORI_CE_UBIC_COD = '" + codUbic + "'";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                try
                {
                    return rs.getString("ORI_CE_ADJUDICADO");
                }
                catch(Exception ex)
                {

                }
            }
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
        return null;
    }
    
    public String getUbicacionAdjudicadaCEMP2014(Integer codUbic,Integer order, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        try
        {
            String query = null;
            query = "SELECT ORI_CE_ADJUDICADO FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_TMP_ADJUDICACION_CENTROS, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                        +" WHERE ORI_CE_UBIC_COD='" + codUbic + "'"
                        + " AND ORI_ORDEN='" + order + "'";  
            
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                try
                {
                    return rs.getString("ORI_CE_ADJUDICADO");
                }
                catch(Exception ex)
                {

                }
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando las valor texto adjudicado para excel resolucion - cemp procesos", ex);
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
        return null;
    }
    
    public String getUbicacionAdjudicadaCEMP2014dsd2017(Integer codUbic,Integer order, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        try
        {
            String query = null;
            query = "SELECT ORI_CE_ADJUDICADO FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_TMP_ADJUDICACION_CENTROS_DSD2017, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                        +" WHERE ORI_CE_UBIC_COD='" + codUbic + "'"
                        + " AND ORI_ORDEN='" + order + "'";  
            
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                try
                {
                    return rs.getString("ORI_CE_ADJUDICADO");
                }
                catch(Exception ex)
                {

                }
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando las valor texto adjudicado convocatoria >= 2017 para excel resolucion - cemp procesos", ex);
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
        return null;
    }
    
    public int actualizarValoracionTrayectoriaUbicaciones(EntidadVO entidad, Integer valor, Connection con) throws Exception
    {
        Statement st = null;
        try
        {
            if(entidad != null && entidad.getOriEntCod() != null && valor != null)
            {
                con.setAutoCommit(false);
                String query = null;
                query = "update  " + ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_ORI_UBIC, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
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
            log.error("Se ha producido un error recuperando las áreas", ex);
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
                query = "select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_ORI_UBIC, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
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
    }
    
    public List<OriUbicacionVO> getUbicacionesDeEntidadORI(EntidadVO entidad, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        List<OriUbicacionVO> retList = new ArrayList<OriUbicacionVO>();
        try
        {
            if(entidad != null && entidad.getOriEntCod() != null)
            {
                String query = null;
                query = "select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_ORI_UBIC, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                            +" where ORI_ENT_COD = '"+entidad.getOriEntCod()+"'";
                if(log.isDebugEnabled()) 
                    log.error("sql = " + query);
                st = con.createStatement();
                rs = st.executeQuery(query);
                while(rs.next())
                {
                    retList.add((OriUbicacionVO)MappingUtils.getInstance().map(rs, OriUbicacionVO.class));
                }
            }
            return retList;
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
    }
    
    public int crearAuditoria(AuditoriaVO aud, Connection con) throws Exception
    {
        int retValue = 0;
        if(aud != null)
        {
            
            Statement st = null;
            try
            {
                String query = null;
                query = "insert into "+ ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_AUDITORIA, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
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
                log.error("Se ha producido un error recuperando las áreas", ex);
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
            query = "select au.*, u.usu_nom from "+ ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_AUDITORIA, ConstantesMeLanbide32.FICHERO_PROPIEDADES)+" au"
                   +" left join "+ GlobalNames.ESQUEMA_GENERICO + ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_USUARIOS, ConstantesMeLanbide32.FICHERO_PROPIEDADES)+" u"
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
                descProceso = MeLanbide32Utils.obtenerNombreProceso(cproceso, codIdioma);
                fila.setProceso(descProceso != null ? descProceso.toUpperCase() : "");
                resultado = rs.getString("RESULTADO");
                fila.setResultado(resultado != null ? resultado.toUpperCase() : "");
                retList.add(fila);
            }
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
        return retList;
    }
    
    public List<EntidadVO> getEntidadesQueNoEstanEnEstado(String ejercicio, String codProc, List<String> tramites, Connection con) throws Exception
    {
        List<EntidadVO> retList = new ArrayList<EntidadVO>();
            
        Statement st = null;
        ResultSet rs = null;
        try
        {
            /*String query = "select ent.* from "+ ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_ENTIDAD, ConstantesMeLanbide32.FICHERO_PROPIEDADES)+" ent"
                          +" inner join "+ ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_E_CRO, ConstantesMeLanbide32.FICHERO_PROPIEDADES)+" cro"
                          +" on cro.cro_num = ent.ext_num"
                          +" where cro_pro = '"+ConstantesMeLanbide32.CODIGO_PROCEDIMIENTO+"' and cro_eje = "+ejercicio+" and cro_tra not in (";
            
            for(int i = 0; i < tramites.size(); i++)
            {
                if(i > 0)
                {
                    query += ",";
                }
                query += tramites.get(i);
            }
            
            query += ")";*/
            
            /*String query = "select * from "+ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_ENTIDAD, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                           +" where ext_num in (select distinct(ext_num) from "+ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_ENTIDAD, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                           +" ent inner join "+ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_E_CRO, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                           +" cro on cro.cro_num = ent.ext_num where cro_pro = '"+ConstantesMeLanbide32.CODIGO_PROCEDIMIENTO+"' "
                           +" and cro_eje = "+ejercicio+" and cro_tra not in (";
            
            for(int i = 0; i < tramites.size(); i++)
            {
                if(i > 0)
                {
                    query += ",";
                }
                query += tramites.get(i);
            }
            
//            query += ")) and ori_ent_cod in ((select distinct ori_ent_cod from "+ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_ORI_UBIC, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
//                    +") union (select distinct ori_ent_cod from "+ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_CE_UBIC, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
//                    +"))";
            
            query += "))";*/
            
            
            
            
            String query = "select * from "+ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_ENTIDAD, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                           +" where ori_ent_cod in( select cod from("
                           +" select max(ori_ent_cod) cod, ext_num from "+ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_ENTIDAD, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                           +" where ext_num in (select distinct(ext_num) from "+ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_ENTIDAD, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                           +" ent inner join "+ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_E_CRO, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
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
            
//            query += ")) and ori_ent_cod in ((select distinct ori_ent_cod from "+ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_ORI_UBIC, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
//                    +") union (select distinct ori_ent_cod from "+ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_CE_UBIC, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
//                    +"))";
            
            query += ")) group by ext_num))";
            
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while(rs.next())
            {
                retList.add((EntidadVO)MappingUtils.getInstance().map(rs, EntidadVO.class));
            }
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
        return retList;
    }
    
    public List<CeUbicacionVO> getUbicacionesDeEntidadCE(EntidadVO entidad, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        List<CeUbicacionVO> retList = new ArrayList<CeUbicacionVO>();
        try
        {
            if(entidad != null && entidad.getOriEntCod() != null)
            {
                String query = null;
                query = "select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_CE_UBIC, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                            +" where ORI_ENT_COD = '"+entidad.getOriEntCod()+"'";
                if(log.isDebugEnabled()) 
                    log.error("sql = " + query);
                st = con.createStatement();
                rs = st.executeQuery(query);
                while(rs.next())
                {
                    retList.add((CeUbicacionVO)MappingUtils.getInstance().map(rs, CeUbicacionVO.class));
                }
            }
            return retList;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando las ubicaciones para la entidad "+entidad.getOriEntCod(), ex);
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
    
    public TerceroVO getTerceroPorCodigo(Long codTercero, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        TerceroVO ter = null;
        try
        {
            String query = null;
            query = "select * from "+ ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_TERCEROS, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                    +" where ter_cod = '"+codTercero+"'";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                ter = (TerceroVO)MappingUtils.getInstance().map(rs, TerceroVO.class);
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
                query = "select dom.* from "+ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_DOMICILIO, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                        +" dom inner join "+ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_T_DOT, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                        +" dot on dom.DNN_DOM = dot.DOT_DOM where dot.DOT_TER = '"+ter.getTerCod()+"'";
                if(log.isDebugEnabled()) 
                    log.error("sql = " + query);
                st = con.createStatement();
                rs = st.executeQuery(query);
                if(rs.next())
                {
                    dom = (DomicilioVO)MappingUtils.getInstance().map(rs, DomicilioVO.class);
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
                query = "select tip.* from "+ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_TIPO_VIA, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                        +" tip inner join t_dnn dom on tip.tvi_cod = dom.dnn_tvi and dnn_dom = '"+dom.getDom().toString()+"'";
                if(log.isDebugEnabled()) 
                    log.error("sql = " + query);
                st = con.createStatement();
                rs = st.executeQuery(query);
                if(rs.next())
                {
                    tipoVia = (TipoViaVO)MappingUtils.getInstance().map(rs, TipoViaVO.class);
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
                query = "select via.* from "+ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_VIAS, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                        +" via inner join t_dnn dom on via.via_cod = dom.dnn_via and dnn_dom = '"+dom.getDom().toString()+"'";
                if(log.isDebugEnabled()) 
                    log.error("sql = " + query);
                st = con.createStatement();
                rs = st.executeQuery(query);
                if(rs.next())
                {
                    via = (ViaVO)MappingUtils.getInstance().map(rs, ViaVO.class);
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
    
    public TerceroVO getTerceroPorExpedienteYRol(int codOrganizacion, String numExpediente, String ejercicio, String rol, Connection con) throws Exception
    {
        PreparedStatement pt = null;
        ResultSet rs = null;
        TerceroVO ter = null;
        try
        {
            String query = query ="SELECT "+ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_TERCEROS, ConstantesMeLanbide32.FICHERO_PROPIEDADES)+".* " 
                    + " FROM " 
                    + ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_REL_TERCERO_DOMICILIO, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                    + " INNER JOIN " + ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_TERCEROS, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                    + " ON T_TER.TER_COD=E_EXT.EXT_TER AND E_EXT.EXT_NVR=T_TER.TER_NVE " 
                    + " WHERE E_EXT.EXT_MUN=? AND E_EXT.EXT_EJE=? AND E_EXT.EXT_PRO=? AND E_EXT.EXT_NUM=? AND E_EXT.EXT_ROL=?"
                  ;
            String codProc = MeLanbide32Utils.getCodProcedimientoDeExpediente(numExpediente);
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            if(log.isDebugEnabled()) 
                log.error("sql-params = " +codOrganizacion+","+ejercicio+","+codProc+","+numExpediente+","+rol);
            
            pt = con.prepareStatement(query);
            pt.setInt(1, codOrganizacion);
            pt.setInt(2, ejercicio!=null && !ejercicio.equalsIgnoreCase("")?Integer.valueOf(ejercicio):null);
            pt.setString(3, codProc);
            pt.setString(4, numExpediente);
            pt.setInt(5, rol!=null && !rol.equalsIgnoreCase("")?Integer.valueOf(rol):null);
            rs = pt.executeQuery();
            if(rs.next())
            {
                ter = (TerceroVO)MappingUtils.getInstance().map(rs, TerceroVO.class);
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
            if(log.isDebugEnabled()) 
                log.debug("Procedemos a cerrar el statement y el resultset");
            if(pt!=null) 
                pt.close();
            if(rs!=null) 
                rs.close();
        }
    }
    
    public List<OriUbicacionVO> getUbicacionesAdjudicadasDeEntidadORI(EntidadVO entidad, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        List<OriUbicacionVO> retList = new ArrayList<OriUbicacionVO>();
        try
        {
            if(entidad != null && entidad.getOriEntCod() != null)
            {
                String query = null;
                query = "select * from "+ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_ORI_UBIC, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                        +" ubic inner join "+ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_TMP_ADJUDICACION, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                        +" adj on ubic.ori_orient_ubic_cod = adj.ori_orient_ubic_cod"
                        +" where ubic.ORI_ENT_COD = '"+entidad.getOriEntCod()+"' and adj.horas_asignadas > 0";
                if(log.isDebugEnabled()) 
                    log.error("sql = " + query);
                st = con.createStatement();
                rs = st.executeQuery(query);
                while(rs.next())
                {
                    retList.add((OriUbicacionVO)MappingUtils.getInstance().map(rs, OriUbicacionVO.class));
                }
            }
            return retList;
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
    }
    
    public List<CeUbicacionVO> getUbicacionesAdjudicadasDeEntidadCE(EntidadVO entidad, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        List<CeUbicacionVO> retList = new ArrayList<CeUbicacionVO>();
        try
        {
            if(entidad != null && entidad.getOriEntCod() != null)
            {
                String query = null;
                query = "select * from "+ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_CE_UBIC, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                        +" ubic inner join "+ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_TMP_ADJUDICACION_CENTROS, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                        +" adj on ubic.ori_ce_ubic_cod = adj.ori_ce_ubic_cod"
                        +" where ubic.ORI_ENT_COD = '"+entidad.getOriEntCod()+"' and adj.ori_ce_adjudicado = 'S'";
                if(log.isDebugEnabled()) 
                    log.error("sql = " + query);
                st = con.createStatement();
                rs = st.executeQuery(query);
                while(rs.next())
                {
                    retList.add((CeUbicacionVO)MappingUtils.getInstance().map(rs, CeUbicacionVO.class));
                }
            }
            return retList;
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
    }
    
    public String getCodigoInternoTramite(int codOrganizacion, String codProc, String codTramite, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        Long valor = null;
        try
        {
            String query = "select TRA_COD from "+ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_TRAMITES, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
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
            log.error("Se ha producido un error recuperando el código interno del trámite "+codTramite, ex);
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
            String query = "select EXP_EST from "+ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_EXPEDIENTES, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
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
                    if(estado == ConstantesMeLanbide32.ESTADO_CERRADO || estado == ConstantesMeLanbide32.ESTADO_ANULADO)
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
            if(log.isDebugEnabled()) 
                log.debug("Procedemos a cerrar el statement y el resultset");
            if(st!=null) 
                st.close();
            if(rs!=null) 
                rs.close();
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
            String query = "select CRO_TRA from "+ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_E_CRO, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
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

                    String query2 = "select MAX(TRA_COU) as TRA_COU from "+ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_TRAMITES, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
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
                    log.error("Se ha producido un error recuperando el trámite abierto ("+numExp+")", ex);
                    throw new Exception(ex);
                }
                finally
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
                    
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando el trámite abierto ("+numExp+")", ex);
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
        return cod;
    }
    
    public int getAnosTrayectoriaCE(EntidadVO entidad, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        int anosTray = 0;
        try
        {
            String query = "select sum(nvl(ORI_CETRA_DURACION,0)) trayectoria from "+ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_CE_TRAYECTORIA, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                        +" where ori_ent_cod = "+entidad.getOriEntCod()
                        + " order by ORI_CETRA_CONVOCATORIA, ORI_CE_COD "
                    ;
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                anosTray = rs.getInt("TRAYECTORIA");
                if(rs.wasNull())
                {
                    anosTray = 0;
                }
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando los años de trayectoria CE para la entidad "+(entidad != null ? entidad.getOriEntCod() : "(entidad = null)"), ex);
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
        return anosTray;
    }
    
    public int getAnosTrayectoriaValidadaCE(EntidadVO entidad, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        int anosTray = 0;
        try
        {
            String query = "select sum(nvl(ORI_CETRA_DURACION_VAL,0)) trayectoria from "+ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_CE_TRAYECTORIA, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                        +" where ori_ent_cod = "+entidad.getOriEntCod();
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                anosTray = rs.getInt("TRAYECTORIA");
                if(rs.wasNull())
                {
                    anosTray = 0;
                }
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando los años de trayectoria validada CE para la entidad "+(entidad != null ? entidad.getOriEntCod() : "(entidad = null)"), ex);
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
        return anosTray;
    }
    
    public int getAnosTrayectoriaCE_Validada(EntidadVO entidad, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        int anosTray = 0;
        try
        {
            String query = "select sum(nvl(ORI_CETRA_DURACION_VAL,0)) trayectoria from "+ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_CE_TRAYECTORIA, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                        +" where ori_ent_cod = "+entidad.getOriEntCod();
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                anosTray = rs.getInt("TRAYECTORIA");
                if(rs.wasNull())
                {
                    anosTray = 0;
                }
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando los años de trayectoria CE para la entidad "+(entidad != null ? entidad.getOriEntCod() : "(entidad = null)"), ex);
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
        return anosTray;
    }
    
    public String getMotivoDenegacionCEMP2014_ExcelResol(EntidadVO entidad, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        String proc = entidad.getExtNum().substring(5, 9);

        try
        {
            String query = "SELECT TXTT_VALOR AS MOTIVODENEGACION FROM "+ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_CAMPOSSUPTXT_TRA, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                            + " WHERE TXTT_MUN="+ entidad.getExtMun()
                            + " AND TXTT_NUM='"+ entidad.getExtNum() +"'"
                            + " AND TXTT_TRA=" + Integer.valueOf(ConstantesMeLanbide32.COD_TRAMITE_EVALUACION_DOCUMENTACION_APORTADA)
                            + " AND TXTT_COD='" + ConstantesMeLanbide32.COD_CAMPO_SUP_TRA_MOTIVODENEGACION + "'";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                return rs.getString("MOTIVODENEGACION");
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error el motivo de denegacion en la generacion de excel resolucion -CEMP Procesos- para el expediente "+entidad.getExtNum(), ex);
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
        return null;
    }
    
    public Boolean expteEstaTramiteResolProvisional(String numExp, Connection con) throws Exception {
        log.debug("expteEstaTramiteResolProvisional- Begin() - CEMP DAO");
        Statement st = null;
        ResultSet rs = null;
        Boolean returnValue = false;
        if (numExp != null) {
            try {
                String query = null;
                query = "SELECT COUNT(1) TRAMITE_RESOLPROV_ABIERTO "
                        //+ "--EXP_PRO, EXP_EJE, EXP_NUM, EXP_MUN,E_CRO.CRO_TRA, E_CRO.CRO_FEI, E_CRO.CRO_FEF " 
                        + " FROM E_EXP "
                        + " LEFT JOIN E_CRO ON E_CRO.CRO_MUN=E_EXP.EXP_MUN AND E_CRO.CRO_PRO=E_EXP.EXP_PRO AND E_CRO.CRO_EJE=E_EXP.EXP_EJE AND E_CRO.CRO_NUM=E_EXP.EXP_NUM "
                        + " WHERE E_EXP.EXP_NUM='" + numExp + "'"
                        + " AND E_CRO.CRO_TRA=" + ConstantesMeLanbide32.COD_TRAMITE_RESOLUCION_PROVISIONAL
                        + " AND E_CRO.CRO_FEF IS NULL";

                if (log.isDebugEnabled()) {
                    log.error("sql = " + query);
                }
                st = con.createStatement();
                rs = st.executeQuery(query);
                if (rs.next()) {
                    Integer validacion = rs.getInt("TRAMITE_RESOLPROV_ABIERTO");
                    if (validacion != null && validacion > 0) {
                        returnValue = true;
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
        log.debug("getFechaRegistroExpediente- Begin() - CEMP DAO");
        Statement st = null;
        ResultSet rs = null;
        String returnValue = "";
        if (numExp != null) {
            try {
                String query = null;
                query = "SELECT TO_CHAR((RES_FEC),'DD/MM/YYYY HH24:MI:SS') FECHA_REGISTRO,EXR_NUM "
                        + " FROM R_RES "
                        + " LEFT JOIN E_EXR ON EXR_NRE=R_RES.RES_NUM AND EXR_EJR=RES_EJE AND EXR_UOR=RES_UOR AND EXR_DEP=RES_DEP AND EXR_TIP=RES_TIP "
                        + " WHERE REGISTRO_TELEMATICO=1 AND EXR_PRO='" + codPro + "'"
                        + " AND  EXR_NUM='" + numExp + "'"
                        + " ORDER BY EXR_NUM ";

                if (log.isDebugEnabled()) {
                    log.error("sql = " + query);
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

    public List<FilaAmbitoSolicitadoCempVO> getAmbitosSolicitadosCEMP(String numExpediente, Connection con) throws Exception {
        List<FilaAmbitoSolicitadoCempVO> retList = new ArrayList<FilaAmbitoSolicitadoCempVO>();
        Statement st = null;
        ResultSet rs = null;
        try {
            String query = "select * "
                    + " from " + ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_AMBITOS_CE_SOLICITADOS, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                    + " where ORI_AMB_SOL_NUM_EXP ='" + numExpediente + "'"
                    + " order by ORI_AMB_CE_COD,ORI_AMB_CE_COD_ID ";
            
            log.info("sql: " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            List<SelectItem> opcinesDeplegableTipoAMbito = getDesplegableTipoAmbitosCEMP(con);
            while (rs.next()) {
                FilaAmbitoSolicitadoCempVO ambitoSolicitadoVO = (FilaAmbitoSolicitadoCempVO) MappingUtils.getInstance().map(rs, FilaAmbitoSolicitadoCempVO.class);
                //Rellenamos las descripciones de Ambito y Provincia
                if(ambitoSolicitadoVO.getOriAmbCeCod()!=null){
                    AmbitoCentroEmpleoVO ambitoVO = getAmbitoCentroEmpleoPorCodigo(ambitoSolicitadoVO.getOriAmbCeCod(), con);
                    ambitoSolicitadoVO.setOriAmbCeAmbito(ambitoVO.getOriAmbAmbito());
                    for (SelectItem selectItem : opcinesDeplegableTipoAMbito) {
                        if (ambitoSolicitadoVO.getOriAmbCeTipoAmbito() != null) {
                            if (selectItem.getId() == ambitoSolicitadoVO.getOriAmbCeTipoAmbito()) {
                                ambitoSolicitadoVO.setOriAmbCeTipoAmbitoDesc(selectItem.getLabel());
                                break;
                            }
                        }
                    }
                }else{
                    log.error("Tenemos una linea en BD con codigo de Ambito a NULL en los datos del Ambitos Solicitados para Expediente : " + numExpediente);
                }
                retList.add(ambitoSolicitadoVO);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error getAmbitosSolicitadosCEMP ", ex);
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

    public AmbitoSolicitadoCempVO getAmbitoSolicitadoCEMPPorCodigo(AmbitoSolicitadoCempVO ambiSol, Connection con) throws Exception {
        AmbitoSolicitadoCempVO retList = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            String query = "select * "
                    + " from " + ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_AMBITOS_CE_SOLICITADOS, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                    + " where ORI_AMB_CE_COD_ID =" + (ambiSol != null && ambiSol.getOriAmbCeCodId()!= null ? ambiSol.getOriAmbCeCodId() : 0)
                    + " order by ORI_AMB_CE_COD,ORI_AMB_CE_COD_ID ";
            log.info("sql: " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                retList = (AmbitoSolicitadoCempVO) MappingUtils.getInstance().map(rs, AmbitoSolicitadoCempVO.class);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error getAmbitoSolicitadoCEMPPorCodigo ", ex);
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

    public AmbitoSolicitadoCempVO guardarAmbitoSolicitadoCEMP(AmbitoSolicitadoCempVO ambitSol, Connection con) throws Exception {
        log.info("guardarAmbitoSolicitadoCEMP - Begin()");
        Statement st = null;
        try {
            if (ambitSol == null || ambitSol.getOriAmbSolNumExp() == null) {
                log.error("Se ha producido un error guardando un Ambito Solicitado CEMP. NO han llegado los datos de el expediente.");
                throw new Exception("Se ha producido un error guardando un Ambito Solicitado. NO han llegado los datos de el expediente.");
            } else {
                String query = null;
                if (ambitSol.getOriAmbCeCodId()== null) {
                    Long codTray = this.getNextId(ConfigurationParameter.getParameter(ConstantesMeLanbide32.SEQ_ORI_AMBITOS_CE_SOLICITADOS, ConstantesMeLanbide32.FICHERO_PROPIEDADES), con);
                    if (codTray == null) {
                        throw new Exception("Error al recuperar el ID consecutivo dela secuencia SEQ_ORI_AMBITOS_CE_SOLICITADOS para guardar un AmbitoSolicitado");
                    }
                    //Es un registro nuevo
                    ambitSol.setOriAmbCeCodId(Integer.valueOf(codTray.toString()));
                    query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_AMBITOS_CE_SOLICITADOS, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                            + " (ORI_AMB_CE_COD_ID"
                            + ", ORI_AMB_CE_COD"
                            + ", ORI_AMB_CE_ENT_COD"
                            + ", ORI_AMB_CE_ANOCONV"
                            + ", ORI_AMB_CE_AMBITO"
                            + ", ORI_AMB_CE_TIPO_AMBITO"
                            + ", ORI_AMB_CE_NUMCE"
                            + ", ORI_AMB_SOL_NUM_EXP"
                            + ")"
                            + " values(" + ambitSol.getOriAmbCeCodId()
                            + ", " + (ambitSol.getOriAmbCeCod()!= null ? ambitSol.getOriAmbCeCod() : "null")
                            + ", " + (ambitSol.getOriAmbCeEntCod()!= null ? ambitSol.getOriAmbCeEntCod() : "null")
                            + ", " + (ambitSol.getOriAmbCeAnoconv()!= null ? ambitSol.getOriAmbCeAnoconv() : "null")
                            + ", '" + ambitSol.getOriAmbCeAmbito() + "'"
                            + ", " + (ambitSol.getOriAmbCeTipoAmbito()!= null ? ambitSol.getOriAmbCeTipoAmbito() : "null")
                            + ", " + (ambitSol.getOriAmbCeNumce()!= null ? ambitSol.getOriAmbCeNumce() : "null")
                            + ", '" + ambitSol.getOriAmbSolNumExp()+ "'"
                            + ")";
                } else { //Es un registro que ya existe
                    query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_AMBITOS_CE_SOLICITADOS, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                            + " set "
                            + " ORI_AMB_CE_TIPO_AMBITO=" + (ambitSol.getOriAmbCeTipoAmbito()!= null ? ambitSol.getOriAmbCeTipoAmbito() : "null")
                            + ", ORI_AMB_CE_COD=" + (ambitSol.getOriAmbCeCod()!= null ? ambitSol.getOriAmbCeCod() : "null")
                            + ", ORI_AMB_CE_AMBITO='" + ambitSol.getOriAmbSolNumExp()+"'"
                            + ", ORI_AMB_CE_NUMCE=" + (ambitSol.getOriAmbCeNumce()!= null ? ambitSol.getOriAmbCeNumce() : "null")
                            + " where ORI_AMB_CE_COD_ID=" + ambitSol.getOriAmbCeCodId()
                            + " and ORI_AMB_SOL_NUM_EXP='" + ambitSol.getOriAmbSolNumExp()+ "'"
                            ;
                }
                log.info("sql = " + query);
                st = con.createStatement();
                int res = st.executeUpdate(query);
                if (res > 0) {
                    log.debug("guardo " + res + "registros");
                    return ambitSol;
                } else {
                    log.error("Se ha producido un error guardando un Ambito Solicitado " + (ambitSol != null ? ambitSol.getOriAmbSolNumExp() : " Sin Datos de Numero de Expediente."));
                    throw new Exception("No se ha podido actualizar ningun valor en la tabla ORI_AMBITOS_SOLICITADOS");
                }
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error guardando un Ambito Solicitado CEMP" + (ambitSol != null ? ambitSol.getOriAmbSolNumExp() : " Sin Datos de Numero de Expediente."), ex);
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
    
    public int eliminarAmbitoSolicitadoCEMP(AmbitoSolicitadoCempVO ambiSol, Connection con) throws Exception {
        log.info("eliminarAmbitoSolicitadoCEMP - DAO - Begin()");
        Statement st = null;
        int result = 0;
        try {
            String query = null;
            query = "delete from " + ConfigurationParameter.getParameter(ConstantesMeLanbide32.TABLA_ORI_AMBITOS_CE_SOLICITADOS, ConstantesMeLanbide32.FICHERO_PROPIEDADES)
                    + " where "
                    + " ORI_AMB_CE_COD_ID=" + ambiSol.getOriAmbCeCodId()
                    + " and ORI_AMB_SOL_NUM_EXP='" + ambiSol.getOriAmbSolNumExp() + "'"
                    ;
            log.info("sql = " + query);
            st = con.createStatement();
            result = st.executeUpdate(query);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD eliminarAmbitoSolicitadoCEMP ", ex);
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
        log.debug("eliminarAmbitoSolicitadoCEMP - DAO - End() : " + result);
        return result;
    }
    
        private Long getNextId(String seqName, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        Long numSec = null;
        try {
            String query = null;
            //Creo el id con la secuencia
            query = "select " + seqName + ".nextval from dual";
            if (log.isDebugEnabled()) {
                log.error("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                numSec = rs.getLong(1);
                if (rs.wasNull()) {
                    throw new Exception();
                }
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error generando el siguiente valor de la secuencia " + seqName, ex);
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
        return numSec;
    }

    private String prepararCondicion2AmbitoxAnioConvAndTipo(int tipoAmbito) {
        log.debug("prepararCondicion2AmbitoxAnioConvAndTipo -  Begin() " + tipoAmbito);
        String textReturn="";
        try {
            switch(tipoAmbito){
                case 1:
                    textReturn =" and ORI_AMB_CE_ESPECIAL=1 ";
                    break;
                case 2 :
                    textReturn =" and ORI_AMB_DISTR=1 ";
                    break;
                case 3:
                    textReturn=" and (ORI_AMB_DISTR=0)"; //getOriCEMantenimientoRequisitosLPA
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            log.error("Error al prepararCondicion2AmbitoxAnioConvAndTipo " + e.getMessage(), e);
        }
        log.debug("prepararCondicion2AmbitoxAnioConvAndTipo -  End() " + textReturn);
        return textReturn;
    }

    public int getPuntuacionUbicacionCE(int oriCeUbicCod, Connection con) throws Exception {
        return getPuntuacionUbicacionCE(getUbicacionCEPorCodigo(String.valueOf(oriCeUbicCod),con), con);
    }

    public int getPuntuacionUbicacionCE(CeUbicacionVO ubicacion, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        int puntuacionUbicacion = 0;
        try {
            String query = "select  ub.ori_ce_ubic_cod, sum(crc.puntuacion) puntuacion " +
            "            from ori_ce_ubic ub " +
            "            left join (WITH opciones_filtradas AS ( " +
                    "            SELECT cc.idcentro,cc.idcriterio,NVL(op.codigosubgrupo, 0) AS codigosubgrupo,op.puntuacion " +
                    "            FROM ori_ce_criterios_centro cc " +
                    "            JOIN ori_ce_criterios_eva_opcion op ON cc.idcriterio = op.idcriteriofk AND cc.idcriterioopcion = op.id " +
                    "            WHERE cc.CENTROSELECCIONOPCION = 1 AND cc.CENTROSELECCIONOPCIONVAL = 1 " +
                    "                AND cc.ejercicioConvocatoria = "+MeLanbide32Utils.getEjercicioDeExpediente(ubicacion.getNumeroExpediente())+" AND op.ejercicioConvocatoria = " + MeLanbide32Utils.getEjercicioDeExpediente(ubicacion.getNumeroExpediente()) +
                    "        ), " +
                    "        subgrupos_agrupados AS ( " +
                    "            SELECT idcentro,idcriterio,codigosubgrupo, " +
                    "                CASE " +
                    "                    WHEN codigosubgrupo = 0 or codigosubgrupo = 1 THEN MAX(puntuacion) " +
                    "                    WHEN codigosubgrupo = 4 THEN  " +
                    "                        CASE  " +
                    "                            WHEN SUM(puntuacion) > ( SELECT MAXIMAPUNTUACIONSUBGRUPO FROM ori_ce_criterio_eva_subgrup_opc WHERE id = 4) THEN 2 " +
                    "                            ELSE SUM(puntuacion) " +
                    "                        END " +
                    "                    ELSE SUM(puntuacion) " +
                    "                END AS puntuacion_normalizada " +
                    "            FROM opciones_filtradas " +
                    "            GROUP BY idcentro, idcriterio, codigosubgrupo " +
                    "        ), " +
                    "        final_puntuacion AS ( " +
                    "            SELECT idcentro,idcriterio,SUM(puntuacion_normalizada) AS puntuacion " +
                    "            FROM subgrupos_agrupados " +
                    "            GROUP BY idcentro, idcriterio " +
                    "        ) " +
                    "        SELECT *  " +
                    "        FROM final_puntuacion " +
                    "        ORDER BY idcentro, idcriterio" +
            "                )crc on crc.idcentro=ub.ori_ce_ubic_cod  " +
            " where ub.ori_ce_ubic_cod=" + (ubicacion!=null ? ubicacion.getOriCeUbicCod() : "null") +
            " GROUP BY  ub.ori_ce_ubic_cod order by puntuacion desc"
            ;
            log.info("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                puntuacionUbicacion = rs.getInt("puntuacion");
                if (rs.wasNull()) {
                    puntuacionUbicacion = 0;
                }
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando los años de trayectoria validada CE para la entidad " + (ubicacion != null ? ubicacion.getOriCeUbicCod(): "(Ubicacion = null)"), ex);
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
        return puntuacionUbicacion;
    }
}
