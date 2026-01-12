/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide47.util;

import es.altia.flexia.integracion.moduloexterno.melanbide47.vo.comun.*;
import es.altia.flexia.integracion.moduloexterno.melanbide47.vo.orientacion.*;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;


/**
 *
 * @author santiagoc
 */
public class MeLanbide47MappingUtils 
{
    private static MeLanbide47MappingUtils instance = null;

    private MeLanbide47MappingUtils()
    {
        
    }
    private static final Logger log = LogManager.getLogger(MeLanbide47MappingUtils.class.getName());
    
    public static MeLanbide47MappingUtils getInstance()
    {
        if(instance == null)
        {
            synchronized(MeLanbide47MappingUtils.class)
            {
                instance = new MeLanbide47MappingUtils();
            }
        }
        return instance;
    }
    
    public Object map(ResultSet rs, Class clazz) throws Exception {
        return mapVers2(rs, clazz, false);
    }
    
    public Object mapVers2(ResultSet rs, Class clazz, boolean nuevaCon) throws Exception
    {
        if(clazz == EntidadVO.class)
        {
            return mapearEntidadVO(rs);
        }
        else if(clazz == FilaTrayectoriaOrientacionVO.class)
        {
            return mapearFilaTrayectoriaOriVO(rs);
        }
        else if(clazz == OriTrayectoriaVO.class)
        {
            return mapearOriTrayectoriaVO(rs);
        }
        else if(clazz == AmbitosHorasVO.class)
        {
            return mapearAmbitoHoras(rs);
        }
        else if(clazz == ProvinciaVO.class)
        {
            return mapearProvincia(rs);
        }
        else if(clazz == MunicipioVO.class)
        {
            return mapearMunicipio(rs);
        }
        else if(clazz == OriUbicVO.class)
        {
            return mapearUbicacionORI(rs);
        }
        else if(clazz == UbicacionesVO.class)
        {
            return mapearUbicacionHoras(rs);
        }
        else if(clazz == EspecialidadesVO.class)
        {
            return mapearEspecialidad(rs);
        }
        else if(clazz == TerceroVO.class)
        {
            return mapearTercero(rs);
        }
        else if(clazz == DomicilioVO.class)
        {
            return mapearDomicilio(rs);
        }
        else if(clazz == ViaVO.class)
        {
            return mapearVia(rs);
        }
        else if(clazz == TipoViaVO.class)
        {
            return mapearTipoVia(rs);
        }
        else if(clazz == AsociacionVO.class)
        {
            return mapearAsociacionVO(rs);
        }
        else if(clazz == FilaAsociacionVO.class)
        {
            return mapearFilaAsociacionVO(rs);
        }
        else if(clazz == FilaUbicOrientacionVO.class)
        {
            return mapearFilaUbicOrientacionVO(rs,nuevaCon);
        }
        else if(clazz == OriTrayOtroProgramaVO.class)
        {
            return mapearTrayectoriaOtroPrograma(rs);
        }
        else if(clazz == OriTrayActividadVO.class)
        {
            return mapearTrayectoriaActividad(rs);
        }
        else if(clazz == OriAmbitoSolicitadoVO.class)
        {
            return mapearOriAmbitoSolicitadoVO(rs);
        }
        else if(clazz == FilaOriAmbitoSolicitadoVO.class)
        {
            return mapearFilaOriAmbitoSolicitadoVO(rs);
        }
        else if(clazz == OriTrayectoriaEntidadVO.class)
        {
            return mapearTrayectoriaEntidadVO(rs);
        }else if(clazz == OriCertCalidad.class)
        {
            return mapearOriCertCalidad(rs);
        }else if(clazz == OriEntidadCertCalidad.class)
        {
            return mapearOriEntidadCertCalidad(rs);
        }else if(clazz == OriCompIgualdadPuntuacion.class)
        {
            return mapearOriCompIgualdadPuntuacion(rs);
        }else if(clazz == OriCertCalidadPuntuacion.class)
        {
            return mapearOriCertCalidadPuntuacion(rs);
        }else if(clazz == SelectItem.class){
            return  mapearSelectItem(rs);
        }
        return null;
    }
    
    private EntidadVO mapearEntidadVO(ResultSet rs) throws Exception
    {
        EntidadVO vo = new EntidadVO();
        vo.setOriEntCod(rs.getLong("ORI_ENT_COD"));
        if(rs.wasNull())
        {
            vo.setOriEntCod(null);
        }
        vo.setExtEje(rs.getInt("EXT_EJE"));
        if(rs.wasNull())
        {
            vo.setExtEje(null);
        }
        vo.setExtMun(rs.getInt("EXT_MUN"));
        if(rs.wasNull())
        {
            vo.setExtMun(null);
        }
        vo.setExtNum(rs.getString("EXT_NUM"));
        vo.setExtNvr(rs.getInt("EXT_NVR"));
        if(rs.wasNull())
        {
            vo.setExtNvr(null);
        }
        vo.setExtTer(rs.getLong("EXT_TER"));
        if(rs.wasNull())
        {
            vo.setExtTer(null);
        }
        vo.setOriEntNom(rs.getString("ORI_ENT_NOM"));
        vo.setOriEntAdmLocalVal(rs.getString("ORI_ENT_ADMLOCAL_VAL"));
        vo.setOriEntSupramunVal(rs.getString("ORI_ENT_SUPRAMUN_VAL"));
        vo.setOriExpCentrofpPubVal(rs.getString("ORI_EXP_CENTROFP_PUB_VAL"));
        vo.setOriExpCentrofpPrivVal(rs.getString("ORI_EXP_CENTROFP_PRIV_VAL"));
        vo.setAgenciaColocacionVal(rs.getString("ORI_ENT_VAL_AGENCOLOC"));
        vo.setNumAgenciaColocacion(rs.getString("ORI_ENT_AGENCOLOC_NUMERO"));
        vo.setNumAgenciaColocacionVal(rs.getString("ORI_ENT_AGENCOLOC_NUMERO_VAL"));
        vo.setOriEntTrayectoria(rs.getInt("ORI_ENT_TRAYECTORIA"));
        if (rs.wasNull()) {
            vo.setOriEntTrayectoria(null);
        }
        vo.setOriEntTrayectoriaVal(rs.getDouble("ORI_ENT_TRAYECTORIA_VAL"));
        if(rs.wasNull())
        {
            vo.setOriEntTrayectoriaVal(null);
        }
        vo.setOriOrientValUbic(rs.getInt("ORI_ORIENT_VAL_UBIC"));
        if(rs.wasNull())
        {
            vo.setOriOrientValUbic(null);
        }
        vo.setOriOrientValDespachos(rs.getInt("ORI_ORIENT_VAL_DESPACHOS"));
        if(rs.wasNull())
        {
            vo.setOriOrientValDespachos(null);
        }
        vo.setOriOrientValAulas(rs.getInt("ORI_ORIENT_VAL_AULAS"));
        if(rs.wasNull())
        {
            vo.setOriOrientValAulas(null);
        }
        vo.setOriEntAceptaMas(rs.getInt("ORI_ENT_ACEPTA_MAS"));
        if(rs.wasNull())
        {
            vo.setOriEntAceptaMas(null);
        }
        vo.setOriEntAsociacion(rs.getInt("ORI_ENT_ASOCIACION"));
        if(rs.wasNull())
        {
            vo.setOriEntAsociacion(null);
        }
        vo.setSegundosLocalesAmbito(rs.getInt("ORI_ENT_SEGULOCALES_AMB"));
        if(rs.wasNull())
        {
            vo.setSegundosLocalesAmbito(null);
        }
        vo.setEspacioAdicional(rs.getString("ORI_ORIENT_VAL_ESPACIOADICIONA"));
        vo.setEspacioHerraBusqEmpleo(rs.getString("ORI_ORIENT_VAL_ESPHERRABUSQEMP"));
        vo.setSinAnimoLucro(rs.getInt("ORI_ENT_SINANIMOLUCRO"));
        if(rs.wasNull())
        {
            vo.setSinAnimoLucro(null);
        }
        vo.setPlanIgualdad(rs.getInt("ORI_ENT_PLAN_IGUALDAD_REG"));
        if(rs.wasNull())
        {
            vo.setPlanIgualdad(null);
        }
        vo.setPlanIgualdadVal(rs.getInt("ORI_ENT_PLAN_IGUALDAD_REG_VAL"));
        if(rs.wasNull())
        {
            vo.setPlanIgualdadVal(null);
        }
        vo.setCertificadoCalidad(rs.getInt("ORI_ENT_CERT_CALIDAD"));
        if(rs.wasNull())
        {
            vo.setCertificadoCalidad(null);
        }
        vo.setCertificadoCalidadVal(rs.getInt("ORI_ENT_CERT_CALIDAD_VAL"));
        if(rs.wasNull())
        {
            vo.setCertificadoCalidadVal(null);
        }
        vo.setSinAnimoLucroVal(rs.getInt("ORI_ENT_SINANIMOLUCRO_VAL"));
        if(rs.wasNull())
        {
            vo.setSinAnimoLucroVal(null);
        }
        vo.setOriValoracionTrayectoria(rs.getBigDecimal("ORI_VALORACION_TRAYECTORIA"));
        if (rs.wasNull()) {
            vo.setOriValoracionTrayectoria(null);
        }

        if(existeColumnaEnResultSet("COMP_IGUALDAD_OPCION", rs)) {
            vo.setCompIgualdadOpcion(NumberUtils.createInteger(rs.getString("COMP_IGUALDAD_OPCION")));
        }
        if(existeColumnaEnResultSet("COMP_IGUALDAD_OPCION_VAL", rs)) {
            vo.setCompIgualdadOpcionVal(NumberUtils.createInteger(rs.getString("COMP_IGUALDAD_OPCION_VAL")));
        }
        if(existeColumnaEnResultSet("COMP_IGUALDAD_OPCION_TEXTO", rs))
            vo.setCompIgualdadOpcionLiteral(rs.getString("COMP_IGUALDAD_OPCION_TEXTO"));
        if(existeColumnaEnResultSet("COMP_IGUALDAD_OPCION_VAL_TEXTO", rs))
            vo.setCompIgualdadOpcionValLiteral(rs.getString("COMP_IGUALDAD_OPCION_VAL_TEXTO"));
        if(existeColumnaEnResultSet("ENT_SUJETA_DER_PUBL", rs)) {
            vo.setEntSujetaDerPubl(NumberUtils.createInteger(rs.getString("ENT_SUJETA_DER_PUBL")));
        }
        if(existeColumnaEnResultSet("ENT_SUJETA_DER_PUBL_VAL", rs)) {
            vo.setEntSujetaDerPublVal(NumberUtils.createInteger(rs.getString("ENT_SUJETA_DER_PUBL_VAL")));
        }

        return vo;
    }
    
    private FilaTrayectoriaOrientacionVO mapearFilaTrayectoriaOriVO(ResultSet rs)throws Exception
    {
        FilaTrayectoriaOrientacionVO fila = new FilaTrayectoriaOrientacionVO();
        fila.setDescTrayectoria(rs.getString("ORI_ORITRAY_DESCRIPCION") != null ? rs.getString("ORI_ORITRAY_DESCRIPCION").replaceAll("\r\n", "<br />").replaceAll("\n", "<br />").replaceAll("\r", "<br />").toUpperCase() : "");
        try
        {
            String str = fila.getDescTrayectoria();
            str = str.replaceAll(System.getProperty("line.separator"), "<br />");
            fila.setDescTrayectoria(str);
        }
        catch(Exception ex)
        {
            
        }
        fila.setDuracionServicio(rs.getString("ORI_ORITRAY_DURACION") != null ? rs.getBigDecimal("ORI_ORITRAY_DURACION").toPlainString().replaceAll("\\.", ",").toUpperCase() : "");
        fila.setOrganismo(rs.getString("ORI_ORITRAY_ORGANISMO") != null ? rs.getString("ORI_ORITRAY_ORGANISMO").toUpperCase() : "");
        fila.setOriOritrayCod(rs.getInt("ORI_ORITRAY_COD"));
        if(rs.wasNull())
        {
            fila.setOriOritrayCod(null);
        }
        return fila;
    }
    
    private OriTrayectoriaVO mapearOriTrayectoriaVO(ResultSet rs)throws Exception
    {
        OriTrayectoriaVO tray = new OriTrayectoriaVO();
        tray.setOriAsocCod(rs.getLong("ORI_ASOC_COD"));
        if(rs.wasNull())
        {
            tray.setOriAsocCod(null);
        }
        tray.setNumExp(rs.getString("ORI_ORITRAY_NUMEXP"));
        tray.setOriOritrayCod(rs.getInt("ORI_ORITRAY_COD"));
        if(rs.wasNull())
        {
            tray.setOriOritrayCod(null);
        }
        tray.setAct56_03(rs.getLong("COLEC1_ACT_56_03"));
        if(rs.wasNull())
        {
            tray.setAct56_03(null);
        }
        tray.setDec327_2007(rs.getInt("COLEC1_DEC_327_2007"));
        if(rs.wasNull())
        {
            tray.setDec327_2007(null);
        }
        tray.setDec327_2008(rs.getInt("COLEC1_DEC_327_2008"));
        if(rs.wasNull())
        {
            tray.setDec327_2008(null);
        }
        tray.setDec327_2009(rs.getInt("COLEC1_DEC_327_2009"));
        if(rs.wasNull())
        {
            tray.setDec327_2009(null);
        }
        tray.setDec327_2010(rs.getInt("COLEC1_DEC_327_2010"));
        if(rs.wasNull())
        {
            tray.setDec327_2010(null);
        }
        tray.setLan_2011(rs.getInt("COLEC2_LAN_2011"));
        if(rs.wasNull())
        {
            tray.setLan_2011(null);
        }
        tray.setLan_2013(rs.getInt("COLEC2_LAN_2013"));
        if(rs.wasNull())
        {
            tray.setLan_2013(null);
        }
        tray.setLan_2014(rs.getInt("COLEC2_LAN_2014"));
        if(rs.wasNull())
        {
            tray.setLan_2014(null);
        }
        tray.setLan_2015(rs.getInt("COLEC2_LAN_2015"));
        if(rs.wasNull())
        {
            tray.setLan_2015(null);
        }
        tray.setLan_2017(rs.getInt("COLEC2_LAN_2017"));
        if(rs.wasNull())
        {
            tray.setLan_2017(null);
        }
        tray.setLan_otros(rs.getLong("COLEC2_LAN_OTROS"));
        if(rs.wasNull())
        {
            tray.setLan_otros(null);
        }
        tray.setMin94_2007(rs.getInt("COLEC1_MIN_94_2007"));
        if(rs.wasNull())
        {
            tray.setMin94_2007(null);
        }
        tray.setMin94_2008(rs.getInt("COLEC1_MIN_94_2008"));
        if(rs.wasNull())
        {
            tray.setMin94_2008(null);
        }
        tray.setMin94_2009(rs.getInt("COLEC1_MIN_94_2009"));
        if(rs.wasNull())
        {
            tray.setMin94_2009(null);
        }
        tray.setMin94_2010(rs.getInt("COLEC1_MIN_94_2010"));
        if(rs.wasNull())
        {
            tray.setMin94_2010(null);
        }
        tray.setMin94_2011(rs.getInt("COLEC1_MIN_94_2011"));
        if(rs.wasNull())
        {
            tray.setMin94_2011(null);
        }
        tray.setMin94_2012(rs.getInt("COLEC1_MIN_94_2012"));
        if(rs.wasNull())
        {
            tray.setMin94_2012(null);
        }
        tray.setMin94_2013(rs.getInt("COLEC1_MIN_94_2013"));
        if(rs.wasNull())
        {
            tray.setMin94_2013(null);
        }
        tray.setMin94_2014(rs.getInt("COLEC1_MIN_94_2014"));
        if(rs.wasNull())
        {
            tray.setMin94_2014(null);
        }
        tray.setMin94_2015(rs.getInt("COLEC1_MIN_94_2015"));
        if(rs.wasNull())
        {
            tray.setMin94_2015(null);
        }
        tray.setMin94_2016(rs.getInt("COLEC1_MIN_94_2016"));
        if(rs.wasNull())
        {
            tray.setMin94_2016(null);
        }
        tray.setMin94_2017(rs.getInt("COLEC1_MIN_94_2017"));
        if(rs.wasNull())
        {
            tray.setMin94_2017(null);
        }
        tray.setMin94_2018(rs.getInt("COLEC1_MIN_94_2018"));
        if(rs.wasNull())
        {
            tray.setMin94_2018(null);
        }
        tray.setMin98_2007(rs.getInt("COLEC1_MIN_98_2007"));
        if(rs.wasNull())
        {
            tray.setMin98_2007(null);
        }
        tray.setMin98_2008(rs.getInt("COLEC1_MIN_98_2008"));
        if(rs.wasNull())
        {
            tray.setMin98_2008(null);
        }
        tray.setMin98_2009(rs.getInt("COLEC1_MIN_98_2009"));
        if(rs.wasNull())
        {
            tray.setMin98_2009(null);
        }
        tray.setMin98_2010(rs.getInt("COLEC1_MIN_98_2010"));
        if(rs.wasNull())
        {
            tray.setMin98_2010(null);
        }
        tray.setMin98_2011(rs.getInt("COLEC1_MIN_98_2011"));
        if(rs.wasNull())
        {
            tray.setMin98_2011(null);
        }
        tray.setMin98_2012(rs.getInt("COLEC1_MIN_98_2012"));
        if(rs.wasNull())
        {
            tray.setMin98_2012(null);
        }
        tray.setMin98_2013(rs.getInt("COLEC1_MIN_98_2013"));
        if(rs.wasNull())
        {
            tray.setMin98_2013(null);
        }
        tray.setMin98_2014(rs.getInt("COLEC1_MIN_98_2014"));
        if(rs.wasNull())
        {
            tray.setMin98_2014(null);
        }
        tray.setMin98_2015(rs.getInt("COLEC1_MIN_98_2015"));
        if(rs.wasNull())
        {
            tray.setMin98_2015(null);
        }
        tray.setMin98_2016(rs.getInt("COLEC1_MIN_98_2016"));
        if(rs.wasNull())
        {
            tray.setMin98_2016(null);
        }
        tray.setMin98_2017(rs.getInt("COLEC1_MIN_98_2017"));
        if(rs.wasNull())
        {
            tray.setMin98_2017(null);
        }
        tray.setMin98_2018(rs.getInt("COLEC1_MIN_98_2018"));
        if(rs.wasNull())
        {
            tray.setMin98_2018(null);
        }
        tray.setTas03_2007(rs.getInt("COLEC1_TAS_03_2007"));
        if(rs.wasNull())
        {
            tray.setTas03_2007(null);
        }
        tray.setTas03_2008(rs.getInt("COLEC1_TAS_03_2008"));
        if(rs.wasNull())
        {
            tray.setTas03_2008(null);
        }
        tray.setTas03_2009(rs.getInt("COLEC1_TAS_03_2009"));
        if(rs.wasNull())
        {
            tray.setTas03_2009(null);
        }
        tray.setTas03_2010(rs.getInt("COLEC1_TAS_03_2010"));
        if(rs.wasNull())
        {
            tray.setTas03_2010(null);
        }
        tray.setTas03_2011(rs.getInt("COLEC1_TAS_03_2011"));
        if(rs.wasNull())
        {
            tray.setTas03_2011(null);
        }
        tray.setTas03_2012(rs.getInt("COLEC1_TAS_03_2012"));
        if(rs.wasNull())
        {
            tray.setTas03_2012(null);
        }
        tray.setTas03_2013(rs.getInt("COLEC1_TAS_03_2013"));
        if(rs.wasNull())
        {
            tray.setTas03_2013(null);
        }
        tray.setTas03_2014(rs.getInt("COLEC1_TAS_03_2014"));
        if(rs.wasNull())
        {
            tray.setTas03_2014(null);
        }
        tray.setTas03_2015(rs.getInt("COLEC1_TAS_03_2015"));
        if(rs.wasNull())
        {
            tray.setTas03_2015(null);
        }
        tray.setTas03_2016(rs.getInt("COLEC1_TAS_03_2016"));
        if(rs.wasNull())
        {
            tray.setTas03_2016(null);
        }
        tray.setTas03_2017(rs.getInt("COLEC1_TAS_03_2017"));
        if(rs.wasNull())
        {
            tray.setTas03_2017(null);
        }
        tray.setTas03_2018(rs.getInt("COLEC1_TAS_03_2018"));
        if(rs.wasNull())
        {
            tray.setTas03_2018(null);
        }
        tray.setDec327(rs.getInt("COLEC1_DEC_327"));
        if(rs.wasNull())
        {
            tray.setDec327(null);
        }
        tray.setMin94(rs.getInt("COLEC1_MIN_94"));
        if(rs.wasNull())
        {
            tray.setMin94(null);
        }
        tray.setMin98(rs.getInt("COLEC1_MIN_98"));
        if(rs.wasNull())
        {
            tray.setMin98(null);
        }
        tray.setTas03(rs.getInt("COLEC1_TAS_03"));
        if(rs.wasNull())
        {
            tray.setTas03(null);
        }
        return tray;
    }
    
    private AmbitosHorasVO mapearAmbitoHoras(ResultSet rs) throws Exception
    {
        AmbitosHorasVO ambito = new AmbitosHorasVO();
        ambito.setOriAmbAmbito(rs.getString("ORI_AMB_AMBITO"));
        ambito.setOriAmbAnoconv(rs.getInt("ORI_AMB_ANOCONV"));
        if(rs.wasNull())
        {
            ambito.setOriAmbAnoconv(null);
        }
        ambito.setOriAmbCod(rs.getInt("ORI_AMB_COD"));
        if(rs.wasNull())
        {
            ambito.setOriAmbCod(null);
        }
        ambito.setOriAmbHorasTot(rs.getInt("ORI_AMB_HORASTOT"));
        if(rs.wasNull())
        {
            ambito.setOriAmbHorasTot(null);
        }
        ambito.setOriAmbTerHis(rs.getInt("ORI_AMB_TERHIS"));
        if(rs.wasNull())
        {
            ambito.setOriAmbTerHis(null);
        }
        return ambito;
    }
    
    private ProvinciaVO mapearProvincia(ResultSet rs) throws Exception
    {
        ProvinciaVO prov = new ProvinciaVO();
        prov.setPrvAut(rs.getInt("PRV_AUT"));
        if(rs.wasNull())
        {
            prov.setPrvAut(null);
        }
        prov.setPrvCod(rs.getInt("PRV_COD"));
        if(rs.wasNull())
        {
            prov.setPrvCod(null);
        }
        prov.setPrvNol(rs.getString("PRV_NOL"));
        prov.setPrvNom(rs.getString("PRV_NOM"));
        prov.setPrvPai(rs.getInt("PRV_PAI"));
        if(rs.wasNull())
        {
            prov.setPrvPai(null);
        }
        return prov;
    }
    
    private MunicipioVO mapearMunicipio(ResultSet rs) throws Exception
    {    
        MunicipioVO mun = new MunicipioVO();
        mun.setMunAlt(rs.getInt("MUN_ALT"));
        if(rs.wasNull())
        {
            mun.setMunAlt(null);
        }
        mun.setMunCod(rs.getInt("MUN_COD"));
        if(rs.wasNull())
        {
            mun.setMunCod(null);
        }
        mun.setMunCom(rs.getInt("MUN_COM"));
        if(rs.wasNull())
        {
            mun.setMunCom(null);
        }
        mun.setMunDco(rs.getString("MUN_DCO"));
        mun.setMunKmc(rs.getInt("MUN_KMC"));
        if(rs.wasNull())
        {
            mun.setMunKmc(null);
        }
        mun.setMunLes(rs.getInt("MUN_LES"));
        if(rs.wasNull())
        {
            mun.setMunLes(null);
        }
        mun.setMunLoe(rs.getInt("MUN_LOE"));
        if(rs.wasNull())
        {
            mun.setMunLoe(null);
        }
        mun.setMunLtn(rs.getInt("MUN_LTN"));
        if(rs.wasNull())
        {
            mun.setMunLtn(null);
        }
        mun.setMunLts(rs.getInt("MUN_LTS"));
        if(rs.wasNull())
        {
            mun.setMunLts(null);
        }
        mun.setMunNol(rs.getString("MUN_NOL"));
        mun.setMunNom(rs.getString("MUN_NOM"));
        mun.setMunPai(rs.getInt("MUN_PAI"));
        if(rs.wasNull())
        {
            mun.setMunPai(null);
        }
        mun.setMunPju(rs.getInt("MUN_PJU"));
        if(rs.wasNull())
        {
            mun.setMunPju(null);
        }
        mun.setMunPrv(rs.getInt("MUN_PRV"));
        if(rs.wasNull())
        {
            mun.setMunPrv(null);
        }
        mun.setMunSit(rs.getString("MUN_SIT"));
        mun.setMunSup(rs.getInt("MUN_SUP"));
        if(rs.wasNull())
        {
            mun.setMunSup(null);
        }
        return mun;
    }
    
    private OriUbicVO mapearUbicacionORI(ResultSet rs) throws Exception
    {
        OriUbicVO ubic = new OriUbicVO();
        ubic.setMunCod(rs.getInt("MUN_COD"));
        if(rs.wasNull())
        {
            ubic.setMunCod(null);
        }
        ubic.setMunPai(rs.getInt("MUN_PAI"));
        if(rs.wasNull())
        {
            ubic.setMunPai(null);
        }
        ubic.setMunPrv(rs.getInt("MUN_PRV"));
        if(rs.wasNull())
        {
            ubic.setMunPrv(null);
        }
        ubic.setOriAmbCod(rs.getInt("ORI_AMB_COD"));
        if(rs.wasNull())
        {
            ubic.setOriAmbCod(null);
        }
        ubic.setOriEntCod(rs.getLong("ORI_ENT_COD"));
        if(rs.wasNull())
        {
            ubic.setOriEntCod(null);
        }
        ubic.setNumExp(rs.getString("ORI_ORIENT_NUMEXP"));
        ubic.setOriOrientAno(rs.getInt("ORI_ORIENT_ANO"));
        if(rs.wasNull())
        {
            ubic.setOriOrientAno(null);
        }
        ubic.setOriOrientAulaGrupalValidada(rs.getString("ORI_ORIENT_AULAGRUPAL_VALIDADA"));
        ubic.setOriOrientAulagrupal(rs.getString("ORI_ORIENT_AULAGRUPAL"));
        ubic.setOriOrientDespachos(rs.getString("ORI_ORIENT_DESPACHOS"));
        ubic.setOriOrientDespachosValidados(rs.getString("ORI_ORIENT_DESPACHOS_VALIDADOS"));
        ubic.setOriOrientDireccion(rs.getString("ORI_ORIENT_DIRECCION"));
        ubic.setOriOrientCP(rs.getString("ORI_ORIENT_CP"));
        ubic.setOriOrientHorasSolicitadas(rs.getBigDecimal("ORI_ORIENT_HORASSOLICITADAS"));
        ubic.setOriOrientHorasadj(rs.getBigDecimal("ORI_ORIENT_HORASADJ"));
        ubic.setOriOrientObservaciones(rs.getString("ORI_ORIENT_OBSERVACIONES"));
        ubic.setOriOrientPuntuacion(rs.getBigDecimal("ORI_ORIENT_PUNTUACION"));
        if(rs.wasNull())
        {
            ubic.setOriOrientPuntuacion(null);
        }
        ubic.setOriOrientUbicCod(rs.getInt("ORI_ORIENT_UBIC_COD"));
        if(rs.wasNull())
        {
            ubic.setOriOrientUbicCod(null);
        }
        ubic.setOriOrientValAulas(rs.getLong("ORI_ORIENT_VAL_AULAS"));
        if(rs.wasNull())
        {
            ubic.setOriOrientValAulas(null);
        }
        ubic.setOriOrientValDespachos(rs.getLong("ORI_ORIENT_VAL_DESPACHOS"));
        if(rs.wasNull())
        {
            ubic.setOriOrientValDespachos(null);
        }
        ubic.setOriOrientValTray(rs.getBigDecimal("ORI_ORIENT_VAL_TRAY"));
        if(rs.wasNull())
        {
            ubic.setOriOrientValTray(null);
        }
        ubic.setOriOrientValUbic(rs.getBigDecimal("ORI_ORIENT_VAL_UBIC"));
        if(rs.wasNull())
        {
            ubic.setOriOrientValUbic(null);
        }
        ubic.setPrvPai(rs.getInt("PRV_PAI"));
        if(rs.wasNull())
        {
            ubic.setPrvPai(null);
        }
        
         ubic.setOriOrientUbicaAmbitoDesc(rs.getString("ORI_ORIENT_AMBITO_UBICACION"));
         ubic.setOriOrientUbicaNombEntidad(rs.getString("ORI_ORIENT_NOMENTID_UBICACION"));
         ubic.setOriOrientUbicaEspacioAdicional(rs.getString("ORI_ORIENT_ESPACIOADICIONA"));
         ubic.setOriOrientUbicaEspAdicioHerrBusqEmpleo(rs.getString("ORI_ORIENT_ESPHERRABUSQEMP"));
         ubic.setOriOrientUbicaEspacioAdicionalVal(rs.getString("ORI_ORIENT_ESPACIOADICIONA_VAL"));
         ubic.setOriOrientUbicaEspAdicioHerrBusqEmpleoVal(rs.getString("ORI_ORIENT_ESPHERRABUSQEMP_VAL"));
         ubic.setOriOrientUbicaTeleFijo(rs.getString("ORI_ORIENT_TELEFIJO_UBICACION"));
         ubic.setOriOrientUbicaPiso(rs.getString("ORI_ORIENT_PISO_UBICACION"));
         ubic.setOriOrientUbicaNumero(rs.getString("ORI_ORIENT_NUMERO_UBICACION"));
         ubic.setOriOrientUbicaLetra(rs.getString("ORI_ORIENT_LETRA_UBICACION"));
         ubic.setOriOrientVal1EspacioAdicional(rs.getLong("ORI_ORIENT_VAL_ESPACIOADICIONA"));
         if(rs.wasNull()){
            ubic.setOriOrientVal1EspacioAdicional(null);
         }
         ubic.setOriOrientValEspAdicioHerrBusqEmpleo(rs.getLong("ORI_ORIENT_VAL_ESPHERRABUSQEMP"));
         if(rs.wasNull())
         {
            ubic.setOriOrientValEspAdicioHerrBusqEmpleo(null);
         }
         
         ubic.setOriCELocalPreviamenteAprobado(rs.getInt("ORI_LOCALPREVAPRO"));
         if(rs.wasNull()){
            ubic.setOriCELocalPreviamenteAprobado(null);
         }
         ubic.setOriCELocalPreviamenteAprobadoVAL(rs.getInt("ORI_LOCALPREVAPRO_VAL"));
         if(rs.wasNull()){
            ubic.setOriCELocalPreviamenteAprobadoVAL(null);
         }
         ubic.setOriCEMantenimientoRequisitosLPA(rs.getInt("ORI_MATENREQ_LPA"));
         if(rs.wasNull()){
            ubic.setOriCEMantenimientoRequisitosLPA(null);
         }
         ubic.setOriCEMantenimientoRequisitosLPAVAL(rs.getInt("ORI_MATENREQ_LPA_VAL"));
         if(rs.wasNull()){
            ubic.setOriCEMantenimientoRequisitosLPAVAL(null);
         }
         ubic.setOriCELocalPreviamenteAprobadoValoracion(rs.getLong("ORI_LOCALPREVAPRO_VALORACION"));
         if(rs.wasNull()){
            ubic.setOriCELocalPreviamenteAprobadoValoracion(null);
         }
         ubic.setOriCEMantenimientoRequisitosLPAValoracion(rs.getLong("ORI_MATENREQ_LPA_VALORACION"));
         if(rs.wasNull()){
            ubic.setOriCEMantenimientoRequisitosLPAValoracion(null);
         }
         ubic.setOriPlanIgualdadValoracion(rs.getLong("ORI_PLANIGUALDAD_VALORACION"));
         if(rs.wasNull()){
            ubic.setOriPlanIgualdadValoracion(null);
         }
         ubic.setOriCertCalidadValoracion(rs.getLong("ORI_CERTCALIDAD_VALORACION"));
         if(rs.wasNull()){
            ubic.setOriPlanIgualdadValoracion(null);
         }
        return ubic;
    }
    
    private UbicacionesVO mapearUbicacionHoras(ResultSet rs) throws Exception
    {
        UbicacionesVO ubic = new UbicacionesVO();
        ubic.setMunCod(rs.getInt("MUN_COD"));
        if(rs.wasNull())
        {
            ubic.setMunCod(null);
        }
        ubic.setMunPai(rs.getInt("MUN_PAI"));
        if(rs.wasNull())
        {
            ubic.setMunPai(null);
        }
        ubic.setMunPrv(rs.getInt("MUN_PRV"));
        if(rs.wasNull())
        {
            ubic.setMunPrv(null);
        }
        ubic.setOriAmbCod(rs.getInt("ORI_AMB_COD"));
        if(rs.wasNull())
        {
            ubic.setOriAmbCod(null);
        }
        ubic.setOriUbiCod(rs.getLong("ORI_UBI_COD"));
        if(rs.wasNull())
        {
            ubic.setOriUbiCod(null);
        }
        ubic.setOriUbicAno(rs.getInt("ORI_UBIC_ANO"));
        if(rs.wasNull())
        {
            ubic.setOriUbicAno(null);
        }
        ubic.setOriUbicPuntuacion(rs.getBigDecimal("ORI_UBIC_PUNTUACION"));
        ubic.setPrvPai(rs.getInt("PRV_PAI"));
        if(rs.wasNull())
        {
            ubic.setPrvPai(null);
        }
        return ubic;
    }
    
    private EspecialidadesVO mapearEspecialidad(ResultSet rs) throws Exception
    {
        EspecialidadesVO esp = new EspecialidadesVO();
        esp.setOriEspCod(rs.getInt("ORI_ESP_COD"));
        esp.setOriEspDesc(rs.getString("ORI_ESP_DESC"));
        return esp;
    }
    
    private TerceroVO mapearTercero(ResultSet rs) throws Exception
    {
        TerceroVO ter = new TerceroVO();
        ter.setExternalCode(rs.getString("EXTERNAL_CODE"));
        ter.setTerAp1(rs.getString("TER_AP1"));
        ter.setTerAp2(rs.getString("TER_AP2"));
        ter.setTerApl(rs.getBigDecimal("TER_APL"));
        ter.setTerCod(rs.getLong("TER_COD"));
        if(rs.wasNull())
        {
            ter.setTerCod(null);
        }
        ter.setTerDce(rs.getString("TER_DCE"));
        ter.setTerDoc(rs.getString("TER_DOC"));
        ter.setTerDom(rs.getBigDecimal("TER_DOM"));
        ter.setTerFal(rs.getDate("TER_FAL"));
        ter.setTerFbj(rs.getDate("TER_FBJ"));
        ter.setTerNml(rs.getBigDecimal("TER_NML"));
        ter.setTerNoc(rs.getString("TER_NOC"));
        ter.setTerNom(rs.getString("TER_NOM"));
        ter.setTerNve(rs.getBigDecimal("TER_NVE"));
        ter.setTerPa1(rs.getString("TER_PA1"));
        ter.setTerPa2(rs.getString("TER_PA2"));
        ter.setTerSit(rs.getString("TER_SIT"));
        ter.setTerTid(rs.getBigDecimal("TER_TID"));
        ter.setTerTlf(rs.getString("TER_TLF"));
        ter.setTerUal(rs.getBigDecimal("TER_UAL"));
        ter.setTerUbj(rs.getBigDecimal("TER_UBJ"));
        return ter;
    }
    
    private DomicilioVO mapearDomicilio(ResultSet rs) throws Exception
    {
        DomicilioVO dom = new DomicilioVO();
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
    
    private ViaVO mapearVia(ResultSet rs) throws Exception
    {
        ViaVO via = new ViaVO();
        via.setAmi(rs.getInt("VIA_AMI"));
        if(rs.wasNull())
        {
            via.setAmi(null);
        }
        via.setAmx(rs.getInt("VIA_AMX"));
        if(rs.wasNull())
        {
            via.setAmx(null);
        }
        via.setCat(rs.getString("VIA_CAT"));
        via.setCbj(rs.getInt("VIA_CBJ"));
        if(rs.wasNull())
        {
            via.setCbj(null);
        }
        via.setCin(rs.getInt("VIA_CIN"));
        if(rs.wasNull())
        {
            via.setCin(null);
        }
        via.setCod(rs.getLong("VIA_COD"));
        if(rs.wasNull())
        {
            via.setCod(null);
        }
        via.setExp(rs.getString("VIA_EXP"));
        via.setFal(rs.getDate("VIA_FAL"));
        via.setFap(rs.getDate("VIA_FAP"));
        via.setFbj(rs.getDate("VIA_FBJ"));
        via.setFiv(rs.getDate("VIA_FIV"));
        via.setIdo(rs.getString("VIA_IDO"));
        via.setLng(rs.getInt("VIA_LNG"));
        if(rs.wasNull())
        {
            via.setLng(null);
        }
        via.setMun(rs.getInt("VIA_MUN"));
        if(rs.wasNull())
        {
            via.setMun(null);
        }
        via.setNoa(rs.getString("VIA_NOA"));
        via.setNoc(rs.getString("VIA_NOC"));
        via.setNom(rs.getString("VIA_NOM"));
        via.setPai(rs.getInt("VIA_PAI"));
        if(rs.wasNull())
        {
            via.setPai(null);
        }
        via.setPrv(rs.getInt("VIA_PRV"));
        if(rs.wasNull())
        {
            via.setPrv(null);
        }
        via.setSit(rs.getString("VIA_SIT"));
        via.setStr(rs.getString("VIA_STR"));
        via.setTrf(rs.getString("VIA_TRF"));
        via.setTvi(rs.getInt("VIA_TVI"));
        if(rs.wasNull())
        {
            via.setTvi(null);
        }
        via.setUal(rs.getInt("VIA_UAL"));
        if(rs.wasNull())
        {
            via.setUal(null);
        }
        via.setUbj(rs.getInt("VIA_UBJ"));
        if(rs.wasNull())
        {
            via.setUbj(null);
        }
        return via;
    }
    
    private TipoViaVO mapearTipoVia(ResultSet rs) throws Exception
    {
        TipoViaVO tipoVia = new TipoViaVO();
        tipoVia.setAbr(rs.getString("TVI_ABR"));
        tipoVia.setCod(rs.getInt("TVI_COD"));
        if(rs.wasNull())
        {
            tipoVia.setCod(null);
        }
        tipoVia.setDes(rs.getString("TVI_DES"));
        return tipoVia;
    }
    
    private AsociacionVO mapearAsociacionVO(ResultSet rs) throws Exception
    {
        AsociacionVO asociacion = new AsociacionVO();
        asociacion.setOriAsocCod(rs.getLong("ORI_ASOC_COD"));
        if(rs.wasNull())
        {
            asociacion.setOriAsocCod(null);
        }
        asociacion.setOriAsocCif(rs.getString("ORI_ASOC_CIF"));
        asociacion.setOriAsocNombre(rs.getString("ORI_ASOC_NOMBRE"));
        asociacion.setOriEntAdmLocal(rs.getInt("ORI_ENT_ADMLOCAL"));
        if(rs.wasNull())
        {
            asociacion.setOriEntAdmLocal(null);
        }
        asociacion.setOriEntCod(rs.getLong("ORI_ENT_COD"));
        if(rs.wasNull())
        {
            asociacion.setOriEntCod(null);
        }
        asociacion.setOriEntSupramun(rs.getInt("ORI_ENT_SUPRAMUN"));
        if(rs.wasNull())
        {
            asociacion.setOriEntSupramun(null);
        }
        asociacion.setOriExpCentrofpPub(rs.getInt("ORI_EXP_CENTROFP_PUB"));
        if(rs.wasNull())
        {
            asociacion.setOriExpCentrofpPub(null);
        }
        asociacion.setOriExpCentrofpPriv(rs.getInt("ORI_EXP_CENTROFP_PRIV"));
        if(rs.wasNull())
        {
            asociacion.setOriExpCentrofpPriv(null);
        }
        asociacion.setAgenciaColocacion(rs.getInt("ORI_ENT_AGENCOLOC"));
        if(rs.wasNull())
        {
            asociacion.setAgenciaColocacion(null);
        }
        asociacion.setNumAgenciaColocacion(rs.getString("ORI_ENT_AGENCOLOC_NUMERO"));
        asociacion.setPlanIgualdad(rs.getInt("ORI_ENT_PLAN_IGUALDAD_REG"));
        if(rs.wasNull())
        {
            asociacion.setPlanIgualdad(null);
        }
        asociacion.setCertificadoCalidad(rs.getInt("ORI_ENT_CERT_CALIDAD"));
        if(rs.wasNull())
        {
            asociacion.setCertificadoCalidad(null);
        }
        asociacion.setOriAsocTipoDoc(rs.getString("ORI_ENT_ASO_TIPO_DOC"));
        return asociacion;
    }
    
    private FilaAsociacionVO mapearFilaAsociacionVO(ResultSet rs) throws Exception
    {
        FilaAsociacionVO fila = new FilaAsociacionVO();
        String valor = null;
        valor = rs.getString("ORI_ENT_ADMLOCAL");
        fila.setAdmLocal(valor != null && !valor.equals("") ? valor.toUpperCase() : "-");
        valor = rs.getString("ORI_EXP_CENTROFP_PRIV");
        fila.setCentrofpPriv(valor != null && !valor.equals("") ? valor.toUpperCase() : "-");
        valor = rs.getString("ORI_EXP_CENTROFP_PUB");
        fila.setCentrofpPub(valor != null && !valor.equals("") ? valor.toUpperCase() : "-");
        valor = rs.getString("ORI_ASOC_CIF");
        fila.setCif(valor != null && !valor.equals("") ? valor.toUpperCase() : "-");
        fila.setCodAsociacion(rs.getLong("ORI_ASOC_COD"));
        if(rs.wasNull())
        {
            fila.setCodAsociacion(null);
        }
        fila.setCodEntidad(rs.getLong("ORI_ENT_COD"));
        if(rs.wasNull())
        {
            fila.setCodEntidad(null);
        }
        valor = rs.getString("ORI_ASOC_NOMBRE");
        fila.setNombre(valor != null && !valor.equals("") ? valor.toUpperCase() : "-");
        valor = rs.getString("ORI_ENT_SUPRAMUN");
        fila.setSupramun(valor != null && !valor.equals("") ? valor.toUpperCase() : "-");
        valor = rs.getString("ORI_ENT_AGENCOLOC");
        fila.setAgenciaColocacion(valor != null && !valor.equals("") ? valor.toUpperCase() : "-");
        valor = rs.getString("ORI_ENT_SINANIMOLUCRO");
        fila.setSinAnimoLucro(valor != null && !valor.equals("") ? valor.toUpperCase() : "-");
        return fila;
    }
    
    private FilaUbicOrientacionVO mapearFilaUbicOrientacionVO(ResultSet rs, boolean nuevaCon) throws Exception
    {
        String valor = null;
        FilaUbicOrientacionVO fila = new FilaUbicOrientacionVO();
        fila.setCodigoUbic(rs.getInt("ORI_ORIENT_UBIC_COD"));
        if(rs.wasNull())
        {
            fila.setCodigoUbic(null);
        }
        fila.setEjercicio(rs.getInt("ORI_ORIENT_ANO"));
        if(rs.wasNull())
        {
            fila.setEjercicio(null);
        }
        
        fila.setCodProvincia(rs.getInt("PRV_COD"));
        if(rs.wasNull())
        {
            fila.setCodProvincia(null);
        }
        valor = rs.getString("PRV_NOL");
        fila.setProvincia(valor != null && !valor.equals("") ? valor.toUpperCase() : "-");
        
        fila.setCodAmbito(rs.getInt("ORI_AMB_COD"));
        if(rs.wasNull())
        {
            fila.setCodAmbito(null);
        }
        valor = rs.getString("ORI_AMB_AMBITO");
        fila.setAmbito(valor != null && !valor.equals("") ? valor.toUpperCase() : "-");
        
        fila.setCodMunicipio(rs.getInt("MUN_COD"));
        if(rs.wasNull())
        {
            fila.setCodMunicipio(null);
        }
        valor = rs.getString("MUN_NOL");
        fila.setMunicipio(valor != null && !valor.equals("") ? valor.toUpperCase() : "-");
        
        valor = rs.getString("ORI_ORIENT_DIRECCION");
        fila.setDireccion(valor != null && !valor.equals("") ? valor.toUpperCase() : "-");
        valor = rs.getString("ORI_ORIENT_NUMERO_UBICACION");
        fila.setDireccionNumero(valor != null && !valor.equals("") ? valor : "");
        valor = rs.getString("ORI_ORIENT_PISO_UBICACION");
        fila.setDireccionPiso(valor != null && !valor.equals("") ? valor : "");
        valor = rs.getString("ORI_ORIENT_LETRA_UBICACION");
        fila.setDireccionLetra(valor != null && !valor.equals("") ? valor.toUpperCase() : "");
        valor = rs.getString("ORI_ORIENT_CP");
        fila.setCodPostal(valor != null && !valor.equals("") ? valor.toUpperCase() : "-");
        valor = rs.getString("ORI_ORIENT_HORASSOLICITADAS");
        fila.setHoras(valor != null && !valor.equals("") ? valor.toUpperCase() : "-");
        valor = rs.getString("ORI_ORIENT_DESPACHOS");
        fila.setDespachos(valor != null && !valor.equals("") ? valor.toUpperCase() : "-");
        valor = rs.getString("ORI_ORIENT_AULAGRUPAL");
        fila.setAulaGrupal(valor != null && !valor.equals("") ? valor.toUpperCase() : "-");
        valor = rs.getString("ORI_ORIENT_ESPACIOADICIONA");
        fila.setOriOrientUbicaEspacioAdicional(valor != null && !valor.equals("") ? valor.toUpperCase() : "-");
        valor = rs.getString("ORI_ORIENT_ESPHERRABUSQEMP");
        fila.setOriOrientUbicaEspAdicioHerrBusqEmpleo(valor != null && !valor.equals("") ? valor.toUpperCase() : "-");
        valor = rs.getString("ORI_ORIENT_TELEFIJO_UBICACION");
        fila.setOriOrientUbicaTeleFijo(valor != null && !valor.equals("") ? valor.toUpperCase() : "-");
        valor = rs.getString("ORI_LOCALPREVAPRO");
        fila.setOriCELocalPreviamenteAprobado(valor != null && !valor.equals("") ? valor.toUpperCase() : "-");
        valor = rs.getString("ORI_MATENREQ_LPA");
        fila.setOriCEMantenimientoRequisitosLPA(valor != null && !valor.equals("") ? valor.toUpperCase() : "-");
        valor = rs.getString("ORI_LOCALPREVAPRO_VAL");
        fila.setOriCELocalPreviamenteAprobadoVAL(valor != null && !valor.equals("") ? valor.toUpperCase() : "-");
        valor = rs.getString("ORI_MATENREQ_LPA_VAL");
        fila.setOriCEMantenimientoRequisitosLPAVAL(valor != null && !valor.equals("") ? valor.toUpperCase() : "-");
        int codigo = rs.getInt("ORI_ENT_PLAN_IGUALDAD_REG");
        fila.setOriEntPlanIgualdad((codigo==1 ? "SI" : "NO"));
        codigo = rs.getInt("ORI_ENT_PLAN_IGUALDAD_REG_VAL");
        fila.setOriEntPlanIgualdadVal((codigo==1 ? "SI" : "NO"));
        codigo = rs.getInt("ORI_ENT_CERT_CALIDAD");        
        fila.setOriEntCertCalidad((codigo==1 ? "SI" : "NO"));
        codigo = rs.getInt("ORI_ENT_CERT_CALIDAD_VAL");        
        fila.setOriEntCertCalidadVal((codigo==1 ? "SI" : "NO"));
        
        BigDecimal val1 = rs.getBigDecimal("ORI_ORIENT_VAL_TRAY");
        BigDecimal val2 = rs.getBigDecimal("ORI_ORIENT_VAL_UBIC");
        Long val3=null;
        Long val4=null;
        Long val5=null;
        Long val6=null;
        Long val7 = null;
        Long val8 = null;
        if (fila.getEjercicio() != null && fila.getEjercicio() >= 2017) {
//            if(fila.getEjercicio()<2018){
//                val3 = rs.getLong("ORI_ORIENT_VAL_ESPACIOADICIONA");
//                val4 = rs.getLong("ORI_ORIENT_VAL_ESPHERRABUSQEMP"); 
//            }else{
            val3 = rs.getLong("ORI_ORIENT_VAL_ESPACIOADICIONA");
            val4 = rs.getLong("ORI_ORIENT_VAL_ESPHERRABUSQEMP");
            val5 = rs.getLong("ORI_LOCALPREVAPRO_VALORACION");
            val6 = rs.getLong("ORI_MATENREQ_LPA_VALORACION");
//            } 
        } else {
            val3 = rs.getLong("ORI_ORIENT_VAL_DESPACHOS");
            val4 = rs.getLong("ORI_ORIENT_VAL_AULAS");
        }

        String valResult = "";
        valResult += val1 != null ? val1.round(new MathContext(3,RoundingMode.HALF_EVEN)).toPlainString() : "0";
        valResult += " - ";
        valResult += val2 != null ? val2.toPlainString() : "0";
        valResult += " - ";
        valResult += val3 != null ? val3 : "0";
        valResult += " - ";
        valResult += val4 != null ? val4 : "0";
        if (fila.getEjercicio() != null && fila.getEjercicio() >= 2018) {
            valResult += " - ";
            valResult += val5 != null ? val5 : "0";
            valResult += " - ";
            valResult += val6 != null ? val6 : "0";
        }
        if (nuevaCon) {
            val7 = rs.getLong("ORI_PLANIGUALDAD_VALORACION");
            val8 = rs.getLong("ORI_CERTCALIDAD_VALORACION");
            valResult += " - ";
            valResult += val7 != null ? val7 : "0";
            valResult += " - ";
            valResult += val8 != null ? val8 : "0";
        }

        fila.setValoracion(valResult != null && !valResult.equals("") ? valResult : "-");

        valor = rs.getString("ORI_ORIENT_PUNTUACION");
        fila.setTotal(valor != null && !valor.equals("") ? valor.toUpperCase() : "-");
        valor = rs.getString("ORI_ORIENT_HORASADJ");
        fila.setHorasAdj(valor != null && !valor.equals("") ? valor.toUpperCase() : "-");
        return fila;
    }
    
    public boolean existeColumnaEnResultSet(String nombrecolumna, ResultSet rs) throws SQLException{
        boolean existe = false;
        if(rs!=null){
            ResultSetMetaData metaData = rs.getMetaData();
            int count = metaData.getColumnCount(); 
            for (int i = 1; i <= count; i++) {
                String namecolumn = metaData.getColumnName(i); // Primera columna es 1
                if(namecolumn.equalsIgnoreCase(nombrecolumna))
                    return true;
            }
        }
        return existe;
    }

    private OriTrayOtroProgramaVO mapearTrayectoriaOtroPrograma(ResultSet rs) throws Exception {
       OriTrayOtroProgramaVO otroProgramaVO = new OriTrayOtroProgramaVO();
       otroProgramaVO.setCodIdOtroPrograma(rs.getInt("ORI_OTRPRO_ID"));
       if(rs.wasNull()){
           otroProgramaVO.setCodIdOtroPrograma(null);
       }
       otroProgramaVO.setEjercicio(rs.getInt("ORI_OTRPRO_EXP_EJE"));
       otroProgramaVO.setNumExpediente(rs.getString("ORI_OTRPRO_NUMEXP"));
       otroProgramaVO.setCodEntidad(rs.getInt("ORI_OTRPRO_COD_ENTIDAD"));
       otroProgramaVO.setPrograma(rs.getString("ORI_OTRPRO_PROGRAMA"));
       otroProgramaVO.setAnioPrograma(rs.getInt("ORI_OTRPRO_PROG_EJE"));
       otroProgramaVO.setDuracion(rs.getInt("ORI_OTRPRO_DURACION"));
       otroProgramaVO.setAnioProgramaVal(rs.getInt("ORI_OTRPRO_PROG_EJE_VALID"));
        if(rs.wasNull()){
           otroProgramaVO.setAnioProgramaVal(null);
       }
       otroProgramaVO.setDuracionVal(rs.getInt("ORI_OTRPRO_DURACION_VALID"));
        if(rs.wasNull()){
           otroProgramaVO.setDuracionVal(null);
       }
       // Valores Adicionales que no estan en la tabla 
       // Comprobamos por si no viene la columna.
       if(existeColumnaEnResultSet("ORI_ENT_CIF", rs)){
           otroProgramaVO.setCifEntidad(rs.getString("ORI_ENT_CIF"));
       }
       if(existeColumnaEnResultSet("ORI_ENT_NOM", rs)){
           otroProgramaVO.setNombreEntidad(rs.getString("ORI_ENT_NOM"));
       }              
       return otroProgramaVO;
    }

    private Object mapearTrayectoriaActividad(ResultSet rs) throws SQLException{
       OriTrayActividadVO oriTrayActividadVO = new OriTrayActividadVO();
       oriTrayActividadVO.setCodIdActividad(rs.getInt("ORI_ACTIV_ID"));
       if (rs.wasNull())
       {
           oriTrayActividadVO.setCodIdActividad(null);
       }
       oriTrayActividadVO.setEjercicio(rs.getInt("ORI_ACTIV_EXP_EJE"));
       if(rs.wasNull())
       {
           oriTrayActividadVO.setEjercicio(null);
       }
       oriTrayActividadVO.setNumExpediente(rs.getString("ORI_ACTIV_NUMEXP"));
       
       oriTrayActividadVO.setCodEntidad(rs.getInt("ORI_ACTIV_COD_ENTIDAD"));
       if(rs.wasNull())
       {
           oriTrayActividadVO.setCodEntidad(null);
       }
       oriTrayActividadVO.setDescActividad(rs.getString("ORI_ACTIV_ACTIVIDAD"));
        if(rs.wasNull()){
            oriTrayActividadVO.setDescActividad(null);
        }
       oriTrayActividadVO.setEjerActividad(rs.getInt("ORI_ACTIV_ACTIVIDAD_EJE"));
        if(rs.wasNull()){
            oriTrayActividadVO.setEjerActividad(null);
        }
       oriTrayActividadVO.setDuracion(rs.getInt("ORI_ACTIV_DURACION"));
        if(rs.wasNull()){
            oriTrayActividadVO.setDuracion(null);
        }
       oriTrayActividadVO.setEjerActividadVal(rs.getInt("ORI_ACTIV_ACTIVIDAD_EJE_VALID"));
       if(rs.wasNull()){
           oriTrayActividadVO.setEjerActividadVal(null);
       }
       oriTrayActividadVO.setDuracionVal(rs.getInt("ORI_ACTIV_DURACION_VALID"));
        if(rs.wasNull()){
            oriTrayActividadVO.setDuracionVal(null);
        }
       
       //Valores Adicionales que no estan en la tabla 
       if (existeColumnaEnResultSet("ORI_ENT_CIF", rs)){
           oriTrayActividadVO.setCifEntidad(rs.getString("ORI_ENT_CIF"));
       }
       if(existeColumnaEnResultSet("ORI_ENT_NOM", rs)){
           oriTrayActividadVO.setNombreEntidad(rs.getString("ORI_ENT_NOM"));
       }
       return oriTrayActividadVO;
    }

    private OriAmbitoSolicitadoVO mapearOriAmbitoSolicitadoVO(ResultSet rs) throws SQLException {
        OriAmbitoSolicitadoVO ambitoSolicitadoVO  = new OriAmbitoSolicitadoVO();
        ambitoSolicitadoVO.setOriAmbSolCod(rs.getInt("ORI_AMB_SOL_COD"));
        if(rs.wasNull())
        {
            ambitoSolicitadoVO.setOriAmbSolCod(null);
        }
        ambitoSolicitadoVO.setOriAmbSolNumExp(rs.getString("ORI_AMB_SOL_NUM_EXP"));
        ambitoSolicitadoVO.setOriAmbSolTerHis(rs.getInt("ORI_AMB_SOL_TERHIS"));
        if(rs.wasNull())
        {
            ambitoSolicitadoVO.setOriAmbSolTerHis(null);
        }
        ambitoSolicitadoVO.setOriAmbSolAmbito(rs.getInt("ORI_AMB_SOL_AMBITO"));
        if(rs.wasNull())
        {
            ambitoSolicitadoVO.setOriAmbSolAmbito(null);
        }
        ambitoSolicitadoVO.setOriAmbSolNroBloques(rs.getInt("ORI_AMB_SOL_NRO_BLOQUES"));
        if(rs.wasNull())
        {
            ambitoSolicitadoVO.setOriAmbSolNroBloques(null);
        }
        ambitoSolicitadoVO.setOriAmbSolNroUbic(rs.getInt("ORI_AMB_SOL_NRO_UBIC"));
        if(rs.wasNull())
        {
            ambitoSolicitadoVO.setOriAmbSolNroUbic(null);
        }
        return ambitoSolicitadoVO;
    }

    private FilaOriAmbitoSolicitadoVO mapearFilaOriAmbitoSolicitadoVO(ResultSet rs) throws SQLException {
        FilaOriAmbitoSolicitadoVO ambitoSolicitadoVO  = new FilaOriAmbitoSolicitadoVO();
        ambitoSolicitadoVO.setOriAmbSolCod(rs.getInt("ORI_AMB_SOL_COD"));
        if(rs.wasNull())
        {
            ambitoSolicitadoVO.setOriAmbSolCod(null);
        }
        ambitoSolicitadoVO.setOriAmbSolNumExp(rs.getString("ORI_AMB_SOL_NUM_EXP"));
        ambitoSolicitadoVO.setOriAmbSolTerHis(rs.getInt("ORI_AMB_SOL_TERHIS"));
        if(rs.wasNull())
        {
            ambitoSolicitadoVO.setOriAmbSolTerHis(null);
        }
        ambitoSolicitadoVO.setOriAmbSolAmbito(rs.getInt("ORI_AMB_SOL_AMBITO"));
        if(rs.wasNull())
        {
            ambitoSolicitadoVO.setOriAmbSolAmbito(null);
        }
        ambitoSolicitadoVO.setOriAmbSolNroBloques(rs.getInt("ORI_AMB_SOL_NRO_BLOQUES"));
        if(rs.wasNull())
        {
            ambitoSolicitadoVO.setOriAmbSolNroBloques(null);
        }
        ambitoSolicitadoVO.setOriAmbSolNroUbic(rs.getInt("ORI_AMB_SOL_NRO_UBIC"));
        if(rs.wasNull())
        {
            ambitoSolicitadoVO.setOriAmbSolNroUbic(null);
        }
        
        return ambitoSolicitadoVO;
    }
   
    private OriTrayectoriaEntidadVO mapearTrayectoriaEntidadVO(ResultSet rs) throws SQLException {
        OriTrayectoriaEntidadVO trayectoriaEntidad = new OriTrayectoriaEntidadVO();
        trayectoriaEntidad.setIdTrayEntidad(rs.getInt("ID"));
        trayectoriaEntidad.setIdConActGrupo(rs.getInt("TRAYIDFKORIPROGCONVACTGRUPO"));
        trayectoriaEntidad.setIdConActSubgrupo(rs.getInt("TRAYIDFKORIPROGCONVACTSUBGRPRE"));
        if (rs.wasNull()) {
            trayectoriaEntidad.setIdConActSubgrupo(null);
        }
        trayectoriaEntidad.setNumExpediente(rs.getString("TRAYNUMEXPEDIENTE"));
        trayectoriaEntidad.setCodEntidad(rs.getString("TRAYCODIGOENTIDAD"));
        if (rs.wasNull()) {
            trayectoriaEntidad.setCodEntidad(null);
        }
        trayectoriaEntidad.setDescActividad(rs.getString("TRAYDESCRIPCION"));
        if (rs.wasNull()) {
            trayectoriaEntidad.setDescActividad(null);
        }
        trayectoriaEntidad.setTieneExperiencia(rs.getInt("TRAYTIENEEXPERIENCIA"));
        if (rs.wasNull()) {
            trayectoriaEntidad.setTieneExperiencia(null);
        }
        trayectoriaEntidad.setFechaInicio(rs.getDate("TRAYFECHAINICIO"));
        trayectoriaEntidad.setFechaFin(rs.getDate("TRAYFECHAFIN"));
        trayectoriaEntidad.setNumMeses(rs.getInt("TRAYNUMEROMESES"));
        if (rs.wasNull()) {
            trayectoriaEntidad.setNumMeses(null);
        }
        trayectoriaEntidad.setTieneExperienciaVal(rs.getInt("TRAYTIENEEXPERIENCIAVAL"));
        if (rs.wasNull()) {
            trayectoriaEntidad.setTieneExperienciaVal(null);
        }
        trayectoriaEntidad.setFechaInicioVal(rs.getDate("TRAYFECHAINICIOVAL"));
        if (rs.wasNull()) {
            trayectoriaEntidad.setFechaInicioVal(null);
        }
        trayectoriaEntidad.setFechaFinVal(rs.getDate("TRAYFECHAFINVAL"));
        if (rs.wasNull()) {
            trayectoriaEntidad.setFechaFinVal(null);
        }
        trayectoriaEntidad.setNumMesesVal(rs.getInt("TRAYNUMEROMESESVAL"));
        if (rs.wasNull()) {
            trayectoriaEntidad.setNumMesesVal(null);
        }
        trayectoriaEntidad.setFechaAlta(rs.getDate("TRAYFECHAALTA"));
        if (rs.wasNull()) {
            trayectoriaEntidad.setFechaAlta(null);
        }
        trayectoriaEntidad.setFechaModificacion(rs.getDate("TRAYFECHAMODIFICACION"));
        if (rs.wasNull()) {
            trayectoriaEntidad.setFechaModificacion(null);
        }
        trayectoriaEntidad.setFechaValidacion(rs.getDate("TRAYFECHAVALIDACION"));
        if (rs.wasNull()) {
            trayectoriaEntidad.setFechaValidacion(null);
        }
//Valores Adicionales que no estan en la tabla 
        if (existeColumnaEnResultSet("ORI_ENT_CIF", rs)) {
            trayectoriaEntidad.setCifEntidad(rs.getString("ORI_ENT_CIF"));
        }
        if (existeColumnaEnResultSet("ORI_ENT_NOM", rs)) {
            trayectoriaEntidad.setNombreEntidad(rs.getString("ORI_ENT_NOM"));
        }

        return trayectoriaEntidad;
    }


    private Object mapearOriCertCalidad(ResultSet rs) throws SQLException {
        if (rs != null) {
            return new OriCertCalidad(
                    rs.getString("codigo"),
                    rs.getString("descripcion"),
                    rs.getString("descripcioneu")
            );
        } else {
            return null;
        }
    }

    private Object mapearOriCompIgualdad(ResultSet rs) throws SQLException {
        if (rs != null) {
            return new OriCompIgualdad(
                    rs.getString("codigo"),
                    rs.getString("descripcion"),
                    rs.getString("descripcioneu")
            );
        } else {
            return null;
        }
    }

    private Object mapearOriEntidadCertCalidad(ResultSet rs) throws SQLException {
        if (rs != null) {
            return new OriEntidadCertCalidad(
                    rs.getInt("ID"),
                    rs.getString("NUM_EXP"),
                    rs.getInt("ID_ENTIDAD"),
                    rs.getString("ID_CERTIFICADO"),
                    rs.getInt("VALOR_SN_SOLICITUD"),
                    rs.getInt("VALOR_SN_VALIDADO")
            );
        } else {
            return null;
        }
    }
    private Object mapearOriCertCalidadPuntuacion(ResultSet rs) throws SQLException {
        if (rs != null) {
            return new OriCertCalidadPuntuacion(
                    rs.getInt("ID_CONVOCATORIA"),
                    rs.getString("CODIGO"),
                    rs.getDouble("PUNTUACION")
            );
        } else {
            return null;
        }
    }
    private Object mapearOriCompIgualdadPuntuacion(ResultSet rs) throws SQLException {
        if (rs != null) {
            return new OriCompIgualdadPuntuacion(
                    rs.getInt("ID_CONVOCATORIA"),
                    rs.getString("CODIGO"),
                    rs.getDouble("PUNTUACION")
            );
        } else {
            return null;
        }
    }
    private Object mapearSelectItem(ResultSet rs) throws SQLException {
        if (rs != null) {
            SelectItem respuesta = new SelectItem();
            // Ir completando el if else en casos particulares que requieren otro nombre en la columna de la query result
            if (existeColumnaEnResultSet("CODIGO", rs)) {
                respuesta.setId(rs.getString("CODIGO"));
            }
            if (existeColumnaEnResultSet("DESCRIPCION", rs)) {
                respuesta.setLabel(rs.getString("DESCRIPCION"));
            }
            return respuesta;
        } else {
            return null;
        }
    }

}
