/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide56.util;

import es.altia.flexia.integracion.moduloexterno.melanbide56.vo.Domicilio;
import es.altia.flexia.integracion.moduloexterno.melanbide56.vo.Expediente;
import es.altia.flexia.integracion.moduloexterno.melanbide56.vo.Tercero;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author santiagoc
 */
public class MeLanbide56MappingUtils
{
    private static MeLanbide56MappingUtils instance = null;
    
    private MeLanbide56MappingUtils()
    {
        
    }
    
    public static MeLanbide56MappingUtils getInstance()
    {
        if(instance == null)
        {
            synchronized(MeLanbide56MappingUtils.class)
            {
                instance = new MeLanbide56MappingUtils();
            }
        }
        return instance;
    }
    
    public Object map(ResultSet rs, Class clazz) throws Exception
    {
        if(clazz == Tercero.class)
        {
            return mapearTercero(rs);
        }
        else if(clazz == Domicilio.class)
        {
            return mapearDomicilio(rs);
        }
        else if(clazz == Expediente.class)
        {
            return mapearExpediente(rs);
        }
        else
        {
            return null;
        }
    }
    
    private Tercero mapearTercero(ResultSet rs) throws Exception
    {
        Tercero ter = new Tercero();
        ter.setExternalCode(rs.getString("EXTERNAL_CODE"));
        ter.setTerAp1(rs.getString("AP1"));
        ter.setTerAp2(rs.getString("AP2"));
        ter.setTerApl(rs.getBigDecimal("APL"));
        ter.setTerCod(rs.getLong("COD"));
        if(rs.wasNull())
        {
            ter.setTerCod(null);
        }
        ter.setTerDce(rs.getString("DCE"));
        ter.setTerDoc(rs.getString("DOC"));
        ter.setTerDom(rs.getBigDecimal("DOM"));
        ter.setTerFal(rs.getDate("FAL"));
        ter.setTerFbj(rs.getDate("FBJ"));
        ter.setTerNml(rs.getBigDecimal("NML"));
        ter.setTerNoc(rs.getString("NOC"));
        ter.setTerNom(rs.getString("NOM"));
        ter.setTerNve(rs.getBigDecimal("VER"));
        ter.setTerPa1(rs.getString("PA1"));
        ter.setTerPa2(rs.getString("PA2"));
        ter.setTerSit(rs.getString("SIT"));
        ter.setTerTid(rs.getBigDecimal("TID"));
        ter.setTerTlf(rs.getString("TLF"));
        ter.setTerUal(rs.getBigDecimal("UAL"));
        ter.setTerUbj(rs.getBigDecimal("UBJ"));
        ter.setExtRol(rs.getString("ROL"));
        return ter;
    }
    
    private Domicilio mapearDomicilio(ResultSet rs) throws Exception
    {
        Domicilio dom = new Domicilio();
        dom.setBlq(rs.getString("DNN_BLQ"));
        dom.setCpo(rs.getString("DNN_CPO"));
        
        dom.setDmc(rs.getString("DNN_DMC"));
        dom.setDom(rs.getBigDecimal("DNN_DOM"));
        dom.setEsc(rs.getString("DNN_ESC"));
        dom.setEsi(rs.getInt("DNN_ESI"));
        if(rs.wasNull())
        {
            dom.setEsi(null);
        }
        dom.setFal(rs.getString("DNN_FAL"));
        dom.setFbj(rs.getString("DNN_FBJ"));
        dom.setLed(rs.getString("DNN_LED"));
        dom.setLeh(rs.getString("DNN_LEH"));
        dom.setLug(rs.getString("DNN_LUG"));
        dom.setMun(rs.getInt("DNN_MUN"));
        if(rs.wasNull())
        {
            dom.setMun(null);
        }
        dom.setNud(rs.getInt("DNN_NUD"));
        if(rs.wasNull())
        {
            dom.setNud(null);
        }
        dom.setNuh(rs.getInt("DNN_NUH"));
        if(rs.wasNull())
        {
            dom.setNuh(null);
        }
        dom.setPai(rs.getInt("DNN_PAI"));
        if(rs.wasNull())
        {
            dom.setPai(null);
        }
        dom.setPlt(rs.getString("DNN_PLT"));
        dom.setPor(rs.getString("DNN_POR"));
        dom.setPrv(rs.getInt("DNN_PRV"));
        if(rs.wasNull())
        {
            dom.setPrv(null);
        }
        dom.setPta(rs.getString("DNN_PTA"));
        dom.setRca(rs.getString("DNN_RCA"));
        dom.setSit(rs.getString("DNN_SIT"));
        dom.setSmu(rs.getInt("DNN_SMU"));
        if(rs.wasNull())
        {
            dom.setSmu(null);
        }
        dom.setSpa(rs.getInt("DNN_SPA"));
        if(rs.wasNull())
        {
            dom.setSpa(null);
        }
        dom.setSpr(rs.getInt("DNN_SPR"));
        if(rs.wasNull())
        {
            dom.setSpr(null);
        }
        dom.setTvi(rs.getInt("DNN_TVI"));
        if(rs.wasNull())
        {
            dom.setTvi(null);
        }
        dom.setUal(rs.getString("DNN_UAL"));
        dom.setUbj(rs.getString("DNN_UBJ"));
        dom.setVia(rs.getLong("DNN_VIA"));
        if(rs.wasNull())
        {
            dom.setVia(null);
        }
        dom.setNomVia(rs.getString("VIA_NOM"));
        dom.setVmu(rs.getInt("DNN_VMU"));
        if(rs.wasNull())
        {
            dom.setVmu(null);
        }
        dom.setVpa(rs.getInt("DNN_VPA"));
        if(rs.wasNull())
        {
            dom.setVpa(null);
        }
        dom.setVpr(rs.getInt("DNN_VPR"));
        if(rs.wasNull())
        {
            dom.setVpr(null);
        }
        return dom;
    }
    
    private Expediente mapearExpediente(ResultSet rs) throws SQLException
    {
        Expediente exp = new Expediente();
        exp.setExpAsu(rs.getString("ASU"));
        exp.setExpEje(rs.getInt("EJE"));
        if(rs.wasNull())
            exp.setExpEje(null);
        exp.setExpMun(rs.getInt("MUN"));
        if(rs.wasNull())
            exp.setExpMun(null);
        exp.setExpNum(rs.getString("NUM"));
        exp.setExpObs(rs.getString("OBS"));
        exp.setExpPro(rs.getString("PRO"));
        exp.setUorRegistro(rs.getInt("UOR"));
        if(rs.wasNull())
            exp.setUorRegistro(null);
        exp.setExrDep(rs.getInt("DEP"));
        if(rs.wasNull())
            exp.setExrDep(null);
        exp.setResTdo(rs.getInt("TDO"));
        if(rs.wasNull())
            exp.setResTdo(null);
        exp.setResNtr(rs.getString("NTR"));
        exp.setResAut(rs.getString("AUT"));
        return exp;
    }
}
