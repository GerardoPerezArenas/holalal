/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide32.util;

import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.centroempleo.UbicacionCentroEmpleoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.centroempleo.CeUbicacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.centroempleo.CeTrayectoriaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.centroempleo.FilaTrayectoriaCentroEmpleoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.centroempleo.AmbitoCentroEmpleoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.centroempleo.AmbitoSolicitadoCempVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.centroempleo.FilaAmbitoSolicitadoCempVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.centroempleo.OriCECriteriosCentro;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.centroempleo.OriCECriteriosCentroDTO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.centroempleo.OriCECriteriosEva;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.centroempleo.OriCECriteriosEvaOpcion;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.comun.EntidadVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.comun.EspecialidadVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.comun.MunicipioVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.comun.ProvinciaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.comun.TerceroVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.comun.DomicilioVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.comun.ViaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.comun.TipoViaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.orientacion.FilaTrayectoriaOrientacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.orientacion.OriTrayectoriaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.orientacion.OriUbicacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.orientacion.AmbitoHorasVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.orientacion.UbicacionHorasVO;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author santiagoc
 */
public class MappingUtils 
{
    private static MappingUtils instance = null;
    private static final Logger log = LogManager.getLogger(MappingUtils.class);
    
    private MappingUtils(){}
    
    public static MappingUtils getInstance()
    {
        if(instance == null)
        {
            synchronized(MappingUtils.class)
            {
                instance = new MappingUtils();
            }
        }
        return instance;
    }
    
    public Object map(ResultSet rs, Class clazz) throws Exception
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
        else if(clazz == FilaTrayectoriaCentroEmpleoVO.class)
        {
            return mapearFilaTrayectoriaCentroEmpleoVO(rs);
        }
        else if(clazz == CeUbicacionVO.class)
        {
            return mapearCeUbicacionVO(rs);
        }
        else if(clazz == CeTrayectoriaVO.class)
        {
            return mapearCeTrayectoriaVO(rs);
        }
        else if(clazz == AmbitoHorasVO.class)
        {
            return mapearAmbitoHoras(rs);
        }
        else if(clazz == AmbitoCentroEmpleoVO.class)
        {
            return mapearAmbitoCentroEmpleo(rs);
        }
        else if(clazz == ProvinciaVO.class)
        {
            return mapearProvincia(rs);
        }
        else if(clazz == MunicipioVO.class)
        {
            return mapearMunicipio(rs);
        }
        else if(clazz == OriUbicacionVO.class)
        {
            return mapearUbicacionORI(rs);
        }
        else if(clazz == UbicacionHorasVO.class)
        {
            return mapearUbicacionHoras(rs);
        }
        else if(clazz == UbicacionCentroEmpleoVO.class)
        {
            return mapearUbicacionCentroEmpleo(rs);
        }
        else if(clazz == EspecialidadVO.class)
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
        else if(clazz == AmbitoSolicitadoCempVO.class)
        {
            return mapearAmbitoSolicitadoCempVO(rs);
        }
        else if(clazz == FilaAmbitoSolicitadoCempVO.class)
        {
            return mapearFilaAmbitoSolicitadoCempVO(rs);
        }
        return null;
    }
    
    private EntidadVO mapearEntidadVO(ResultSet rs) throws Exception
    {
        EntidadVO vo = new EntidadVO();
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
        vo.setOriEntAcolocacion(rs.getString("ORI_ENT_ACOLOCACION"));
        vo.setOriEntAdmLocal(rs.getString("ORI_ENT_ADMLOCAL"));
        vo.setOriEntCod(rs.getLong("ORI_ENT_COD"));
        if(rs.wasNull())
        {
            vo.setOriEntCod(null);
        }
        vo.setOriEntNumtrabDisc(rs.getBigDecimal("ORI_ENT_NUMTRAB_DISC"));
        vo.setOriEntPortrabDisc(rs.getBigDecimal("ORI_ENT_PORTRAB_DISC"));
//        if(rs.wasNull())
//        {
//            vo.setOriEntNumtrabDisc(null);
//        }
        vo.setOriEntNumtrab(rs.getBigDecimal("ORI_ENT_NUMTRAB"));
//        if(rs.wasNull())
//        {
//            vo.setOriEntNumtrab(null);
//        }
        vo.setOriEntOtros(rs.getString("ORI_ENT_OTROS"));
        vo.setOriEntSupramun(rs.getString("ORI_ENT_SUPRAMUN"));
        vo.setOriEntTrayectoria(rs.getInt("ORI_ENT_TRAYECTORIA"));
        if(rs.wasNull())
        {
            vo.setOriEntTrayectoria(null);
        }
        vo.setOriEntTrayectoriaVal(rs.getInt("ORI_ENT_TRAYECTORIA_VAL"));
        if(rs.wasNull())
        {
            vo.setOriEntTrayectoriaVal(null);
        }
        vo.setOriExpCod(rs.getInt("ORI_EXP_COD"));
        if(rs.wasNull())
        {
            vo.setOriExpCod(null);
        }
        vo.setOriEntNom(rs.getString("ORI_ENT_NOM"));
        
        vo.setOriEntAdmLocalVal(rs.getString("ORI_ENT_ADMLOCAL_VAL"));
        vo.setOriEntSupramunVal(rs.getString("ORI_ENT_SUPRAMUN_VAL"));
        vo.setOriExpCodVal(rs.getInt("ORI_EXP_COD_VAL"));
        if(rs.wasNull())
        {
            vo.setOriExpCodVal(null);
        }
        vo.setOriEntAcolocacionVal(rs.getString("ORI_ENT_ACOLOCACION_VAL"));
        vo.setOriEntNumtrabVal(rs.getBigDecimal("ORI_ENT_NUMTRAB_VAL"));
        vo.setOriEntNumtrabDiscVal(rs.getBigDecimal("ORI_ENT_NUMTRAB_DISC_VAL"));
        vo.setOriEntPortrabDiscVal(rs.getBigDecimal("ORI_ENT_PORTRAB_DISC_VAL"));
        vo.setOriEntOtrosVal(rs.getString("ORI_ENT_OTROS_VAL"));
        
        vo.setTipAtenPerDiscapa(rs.getString("ORI_ENT_TIPO_ESPPERSDISC"));
        vo.setTipAtenColRiesExc(rs.getString("ORI_ENT_TIPO_ESPATCOLEXC"));
        vo.setTipCentroForProfe(rs.getString("ORI_ENT_TIPO_ENTCENTROFP"));
        vo.setTipOrgSindiEmpres(rs.getString("ORI_ENT_TIPO_ENTORGSINDI"));
        vo.setTipSinAnimoLucro(rs.getString("ORI_ENT_TIPO_ENTNOANILUC"));
        vo.setTipAtenPerDiscapaVAL(rs.getString("ORI_ENT_TIPO_ESPPERSDISC_VAL"));
        vo.setTipAtenColRiesExcVAL(rs.getString("ORI_ENT_TIPO_ESPATCOLEXC_VAL"));
        vo.setTipCentroForProfeVAL(rs.getString("ORI_ENT_TIPO_ENTCENTROFP_VAL"));
        vo.setTipOrgSindiEmpresVAL(rs.getString("ORI_ENT_TIPO_ENTORGSINDI_VAL"));
        vo.setTipSinAnimoLucroVAL(rs.getString("ORI_ENT_TIPO_ENTNOANILUC_VAL"));
        
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
        tray.setOriEntCod(rs.getLong("ORI_ENT_COD"));
        if(rs.wasNull())
        {
            tray.setOriEntCod(null);
        }
        tray.setOriOritrayCod(rs.getInt("ORI_ORITRAY_COD"));
        if(rs.wasNull())
        {
            tray.setOriOritrayCod(null);
        }
        tray.setOriOritrayDescripcion(rs.getString("ORI_ORITRAY_DESCRIPCION"));
        tray.setOriOritrayDuracion(rs.getBigDecimal("ORI_ORITRAY_DURACION"));
//        if(rs.wasNull())
//        {
//            tray.setOriOritrayDuracion(null);
//        }
        tray.setOriOritrayOrganismo(rs.getString("ORI_ORITRAY_ORGANISMO"));
        return tray;
    }
    
    private FilaTrayectoriaCentroEmpleoVO mapearFilaTrayectoriaCentroEmpleoVO(ResultSet rs) throws Exception
    {
        FilaTrayectoriaCentroEmpleoVO fila = new FilaTrayectoriaCentroEmpleoVO();
        fila.setDescripcion(rs.getString("ORI_CETRA_DESCRIPCION") != null ? rs.getString("ORI_CETRA_DESCRIPCION").replaceAll("\r\n", "*BR***").replaceAll("\n", "*BR***").replaceAll("\r", "*BR***").toUpperCase() : "");
        
        try
        {
            String str = fila.getDescripcion();
            str = str.replaceAll(System.getProperty("line.separator"), "*BR***");
            fila.setDescripcion(str);
            //Agregamos el ID de la descripcion de la convocatoria
            fila.setOriCetraConvocatoria(rs.getInt("ORI_CETRA_CONVOCATORIA"));
            if(rs.wasNull())
            {
                fila.setOriCetraConvocatoria(null);
            }
        }
        catch(Exception ex)
        {
            
        }
        fila.setDuracion(rs.getString("ORI_CETRA_DURACION") != null ? rs.getBigDecimal("ORI_CETRA_DURACION").toPlainString().replaceAll("\\.", ",").toUpperCase() : "");
        fila.setDuracionValidada(rs.getString("ORI_CETRA_DURACION_VAL") != null ? rs.getBigDecimal("ORI_CETRA_DURACION_VAL").toPlainString().replaceAll("\\.", ",").toUpperCase() : "");
        fila.setOriCeCod(rs.getInt("ORI_CE_COD"));
        if(rs.wasNull())
        {
            fila.setOriCeCod(null);
        }
        return fila;
    }
    
    private CeUbicacionVO mapearCeUbicacionVO(ResultSet rs) throws Exception
    {
        CeUbicacionVO ubic = new CeUbicacionVO();
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
        ubic.setOriCeAdjudicada(rs.getString("ORI_CE_ADJUDICADA"));
        ubic.setOriCeDireccion(rs.getString("ORI_CE_DIRECCION"));
        ubic.setOriCeEspecial(rs.getString("ORI_CE_ESPECIAL"));
        ubic.setValidacion(rs.getString("ORI_CE_VALIDACION"));
        ubic.setCodigoPostal(rs.getString("ORI_CE_CP_UBICACION"));
        ubic.setHorarioAtencion(rs.getString("ORI_CE_HORARIO_UBICACION"));
        ubic.setAprobado(rs.getString("ORI_CE_APROBADO"));
        ubic.setMantenido(rs.getString("ORI_CE_MANTENIDO"));
        ubic.setNumeroExpediente(rs.getString("ORI_CE_UBIC_NUM_EXP"));
        ubic.setOriCeUbicCod(rs.getInt("ORI_CE_UBIC_COD"));
        if(rs.wasNull())
        {
            ubic.setOriCeUbicCod(null);
        }
        ubic.setOriEntCod(rs.getLong("ORI_ENT_COD"));
        if(rs.wasNull())
        {
            ubic.setOriEntCod(null);
        }
        ubic.setPrvPai(rs.getInt("PRV_PAI"));
        if(rs.wasNull())
        {
            ubic.setPrvPai(null);
        }
        // Orden de ubicaciones al generar el Excel.
        if(existeColumnaEnResultSet("ORI_ORDEN", rs)){
            ubic.setOrdenAdjudicado(rs.getInt("ORI_ORDEN"));
            if(rs.wasNull())
            {
                ubic.setOrdenAdjudicado(null);
            }
        }
        if(existeColumnaEnResultSet("ORI_CE_LOCAL_NUEVO_VALIDADO", rs)){
            ubic.setLocalNuevoValidado(rs.getString("ORI_CE_LOCAL_NUEVO_VALIDADO"));
        }
        
        return ubic;
    }
    
    private CeTrayectoriaVO mapearCeTrayectoriaVO(ResultSet rs) throws Exception
    {
        CeTrayectoriaVO tray = new CeTrayectoriaVO();
        tray.setOriCeCod(rs.getInt("ORI_CE_COD"));
        if(rs.wasNull())
        {
            tray.setOriCeCod(null);
        }
        //Agregamos el ID de la descripcion de la convocatoria
        tray.setOriCetraConvocatoria(rs.getInt("ORI_CETRA_CONVOCATORIA"));
        if (rs.wasNull()) {
            tray.setOriCetraConvocatoria(null);
        }
        tray.setOriCetraDescripcion(rs.getString("ORI_CETRA_DESCRIPCION"));
        tray.setOriCetraDuracion(rs.getBigDecimal("ORI_CETRA_DURACION"));
        tray.setOriCetraDuracionValidada(rs.getBigDecimal("ORI_CETRA_DURACION_VAL"));
//        if(rs.wasNull())
//        {
//            tray.setOriCetraDuracion(null);
//        }
        tray.setOriEntCod(rs.getLong("ORI_ENT_COD"));
        if(rs.wasNull())
        {
            tray.setOriEntCod(null);
        }
        return tray;
    }
    
    private AmbitoHorasVO mapearAmbitoHoras(ResultSet rs) throws Exception
    {
        AmbitoHorasVO ambito = new AmbitoHorasVO();
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
    
    private AmbitoCentroEmpleoVO mapearAmbitoCentroEmpleo(ResultSet rs) throws Exception
    {
        AmbitoCentroEmpleoVO ambito = new AmbitoCentroEmpleoVO();
        ambito.setOriAmbAmbito(rs.getString("ORI_AMB_AMBITO"));
        ambito.setOriAmbAnoconv(rs.getInt("ORI_AMB_ANOCONV"));
        if(rs.wasNull())
        {
            ambito.setOriAmbAnoconv(null);
        }
        ambito.setOriAmbCe(rs.getInt("ORI_AMB_CE"));
        if(rs.wasNull())
        {
            ambito.setOriAmbCe(null);
        }
        ambito.setOriAmbCeEspecial(rs.getInt("ORI_AMB_CE_ESPECIAL"));
        if(rs.wasNull())
        {
            ambito.setOriAmbCeEspecial(null);
        }
        ambito.setOriAmbCod(rs.getInt("ORI_AMB_COD"));
        if(rs.wasNull())
        {
            ambito.setOriAmbCod(null);
        }
        ambito.setOriAmbTerHis(rs.getInt("ORI_AMB_TERHIS"));
        if(rs.wasNull())
        {
            ambito.setOriAmbTerHis(null);
        }
        ambito.setOriAmbDistr(rs.getInt("ORI_AMB_DISTR"));
        if(rs.wasNull())
        {
            ambito.setOriAmbDistr(null);
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
    
    private OriUbicacionVO mapearUbicacionORI(ResultSet rs) throws Exception
    {
        OriUbicacionVO ubic = new OriUbicacionVO();
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
        ubic.setOriOrientAno(rs.getInt("ORI_ORIENT_ANO"));
        if(rs.wasNull())
        {
            ubic.setOriOrientAno(null);
        }
        ubic.setOriOrientAulaGrupalValidada(rs.getString("ORI_ORIENT_AULAGRUPAL_VALIDADA"));
        ubic.setOriOrientAulagrupal(rs.getString("ORI_ORIENT_AULAGRUPAL"));
        ubic.setOriOrientDespachos(rs.getInt("ORI_ORIENT_DESPACHOS"));
        if(rs.wasNull())
        {
            ubic.setOriOrientDespachos(null);
        }
        ubic.setOriOrientDespachosValidados(rs.getInt("ORI_ORIENT_DESPACHOS_VALIDADOS"));
        if(rs.wasNull())
        {
            ubic.setOriOrientDespachosValidados(null);
        }
        ubic.setOriOrientDireccion(rs.getString("ORI_ORIENT_DIRECCION"));
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
        ubic.setOriOrientValTray(rs.getInt("ORI_ORIENT_VAL_TRAY"));
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
        return ubic;
    }
    
    private UbicacionHorasVO mapearUbicacionHoras(ResultSet rs) throws Exception
    {
        UbicacionHorasVO ubic = new UbicacionHorasVO();
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
    
    private UbicacionCentroEmpleoVO mapearUbicacionCentroEmpleo(ResultSet rs) throws Exception
    {
        UbicacionCentroEmpleoVO ubic = new UbicacionCentroEmpleoVO();
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
        ubic.setPrvPai(rs.getInt("PRV_PAI"));
        if(rs.wasNull())
        {
            ubic.setPrvPai(null);
        }
        return ubic;
    }
    
    private EspecialidadVO mapearEspecialidad(ResultSet rs) throws Exception
    {
        EspecialidadVO esp = new EspecialidadVO();
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

    private AmbitoSolicitadoCempVO mapearAmbitoSolicitadoCempVO(ResultSet rs) throws SQLException {
        AmbitoSolicitadoCempVO ambitoSolicitadoCempVO = new AmbitoSolicitadoCempVO();
        ambitoSolicitadoCempVO.setOriAmbCeCodId(rs.getInt("ORI_AMB_CE_COD_ID"));
        if(rs.wasNull())
        {
            ambitoSolicitadoCempVO.setOriAmbCeCodId(null);
        }
        ambitoSolicitadoCempVO.setOriAmbCeCod(rs.getInt("ORI_AMB_CE_COD"));
        if(rs.wasNull())
        {
            ambitoSolicitadoCempVO.setOriAmbCeCod(null);
        }
        ambitoSolicitadoCempVO.setOriAmbCeEntCod(rs.getInt("ORI_AMB_CE_ENT_COD"));
        if(rs.wasNull())
        {
            ambitoSolicitadoCempVO.setOriAmbCeEntCod(null);
        }
        ambitoSolicitadoCempVO.setOriAmbCeAnoconv(rs.getInt("ORI_AMB_CE_ANOCONV"));
        if(rs.wasNull())
        {
            ambitoSolicitadoCempVO.setOriAmbCeAnoconv(null);
        }
        ambitoSolicitadoCempVO.setOriAmbCeAmbito(rs.getString("ORI_AMB_CE_AMBITO"));
        ambitoSolicitadoCempVO.setOriAmbCeTipoAmbito(rs.getInt("ORI_AMB_CE_TIPO_AMBITO"));
        if(rs.wasNull())
        {
            ambitoSolicitadoCempVO.setOriAmbCeTipoAmbito(null);
        }
        ambitoSolicitadoCempVO.setOriAmbCeNumce(rs.getInt("ORI_AMB_CE_NUMCE"));
        if(rs.wasNull())
        {
            ambitoSolicitadoCempVO.setOriAmbCeNumce(null);
        }
        ambitoSolicitadoCempVO.setOriAmbSolNumExp(rs.getString("ORI_AMB_SOL_NUM_EXP"));
        
        return ambitoSolicitadoCempVO;
    }

    private Object mapearFilaAmbitoSolicitadoCempVO(ResultSet rs) throws SQLException {
        FilaAmbitoSolicitadoCempVO listaAmbitoSolicitadoCempVO = new FilaAmbitoSolicitadoCempVO();
        listaAmbitoSolicitadoCempVO.setOriAmbCeCodId(rs.getInt("ORI_AMB_CE_COD_ID"));
        if (rs.wasNull()) {
            listaAmbitoSolicitadoCempVO.setOriAmbCeCodId(null);
        }
        listaAmbitoSolicitadoCempVO.setOriAmbCeCod(rs.getInt("ORI_AMB_CE_COD"));
        if (rs.wasNull()) {
            listaAmbitoSolicitadoCempVO.setOriAmbCeCod(null);
        }
        listaAmbitoSolicitadoCempVO.setOriAmbCeEntCod(rs.getInt("ORI_AMB_CE_ENT_COD"));
        if (rs.wasNull()) {
            listaAmbitoSolicitadoCempVO.setOriAmbCeEntCod(null);
        }
        listaAmbitoSolicitadoCempVO.setOriAmbCeAnoconv(rs.getInt("ORI_AMB_CE_ANOCONV"));
        if (rs.wasNull()) {
            listaAmbitoSolicitadoCempVO.setOriAmbCeAnoconv(null);
        }
        listaAmbitoSolicitadoCempVO.setOriAmbCeAmbito(rs.getString("ORI_AMB_CE_AMBITO"));
        listaAmbitoSolicitadoCempVO.setOriAmbCeTipoAmbito(rs.getInt("ORI_AMB_CE_TIPO_AMBITO"));
        if (rs.wasNull()) {
            listaAmbitoSolicitadoCempVO.setOriAmbCeTipoAmbito(null);
        }
        listaAmbitoSolicitadoCempVO.setOriAmbCeNumce(rs.getInt("ORI_AMB_CE_NUMCE"));
        if (rs.wasNull()) {
            listaAmbitoSolicitadoCempVO.setOriAmbCeNumce(null);
        }
        listaAmbitoSolicitadoCempVO.setOriAmbSolNumExp(rs.getString("ORI_AMB_SOL_NUM_EXP"));
        return listaAmbitoSolicitadoCempVO;
    }

    public OriCECriteriosEva getOriCECriteriosEva(ResultSet rs) throws Exception {
        OriCECriteriosEva respuesta = null;
        if (rs != null) {
            try {
                respuesta = new OriCECriteriosEva(rs.getLong("id"),
                         rs.getString("codigo"),
                         rs.getString("codigoOrden"),
                         rs.getString("descripcion_ES"),
                         rs.getString("descripcion_EU"),
                         rs.getInt("EJERCICIOCONVOCATORIA")
                );
            } catch (Exception e) {
                log.error("Error mapeando Objeto BD a OriCECriteriosEva " + e.getMessage(), e);
                throw e;
            }

        }
        return respuesta;
    }
    
    public OriCECriteriosEvaOpcion getOriCECriteriosEvaOpcion(ResultSet rs) throws Exception {
        OriCECriteriosEvaOpcion respuesta = null;
        if (rs != null) {
            try {
                respuesta = new OriCECriteriosEvaOpcion(rs.getLong("id"),
                         rs.getLong("idCriterioFK"),
                         rs.getString("codigoOrden"),
                         rs.getString("descripcion_ES"),
                         rs.getString("descripcion_EU"),
                         rs.getInt("CODIGOSUBGRUPO"),
                         rs.getInt("EJERCICIOCONVOCATORIA")
                );
            } catch (Exception e) {
                log.error("Error mapeando Objeto BD a OriCECriteriosEva " + e.getMessage(), e);
                throw e;
            }

        }
        return respuesta;
    }

    public OriCECriteriosCentro getOriCECriteriosCentro(ResultSet rs) throws Exception {
        OriCECriteriosCentro respuesta = null;
        if (rs != null) {
            try {
                respuesta = new OriCECriteriosCentro(rs.getLong("ID"),
                        rs.getLong("IDCENTRO"),
                        rs.getLong("IDCRITERIO"),
                        rs.getLong("IDCRITERIOOPCION"),
                        rs.getInt("CENTROSELECCIONOPCION"),
                        rs.getString("NUMEROEXPEDIENTE"),
                        rs.getInt("CENTROSELECCIONOPCIONVAL"),
                        rs.getInt("ejercicioConvocatoria")
                );
            } catch (Exception e) {
                log.error("Error mapeando Objeto BD a OriCECriteriosCentro " + e.getMessage(), e);
                throw e;
            }

        }
        return respuesta;
    }
    
    public OriCECriteriosCentroDTO getOriCECriteriosCentroDTO(ResultSet rs) throws Exception {
        OriCECriteriosCentroDTO respuesta = null;
        if (rs != null) {
            try {
                respuesta = new OriCECriteriosCentroDTO(rs.getLong("ID"),
                        rs.getLong("IDCENTRO"),
                        rs.getLong("IDCRITERIO"),
                        rs.getLong("IDCRITERIOOPCION"),
                        rs.getInt("CENTROSELECCIONOPCION"),
                        rs.getString("NUMEROEXPEDIENTE"),
                        rs.getInt("CENTROSELECCIONOPCIONVAL")
                        ,rs.getString("idElementoHTML")
                        ,rs.getInt("EJERCICIOCONVOCATORIA")
                );
            } catch (Exception e) {
                log.error("Error mapeando Objeto BD a OriCECriteriosCentroDTO " + e.getMessage(), e);
                throw e;
            }

        }
        return respuesta;
    }

}
