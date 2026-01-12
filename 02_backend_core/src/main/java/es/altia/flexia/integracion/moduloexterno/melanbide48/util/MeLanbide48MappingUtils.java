/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide48.util;

import es.altia.flexia.integracion.moduloexterno.melanbide48.vo.*;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.ValorCampoDesplegableModuloIntegracionVO;
import org.apache.commons.lang.math.NumberUtils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author santiagoc
 */
public class MeLanbide48MappingUtils 
{
    private static MeLanbide48MappingUtils instance = null;
    
    private MeLanbide48MappingUtils()
    {
        
    }
    
    public static MeLanbide48MappingUtils getInstance()
    {
        if(instance == null)
        {
            synchronized(MeLanbide48MappingUtils.class)
            {
                instance = new MeLanbide48MappingUtils();
            }
        }
        return instance;
    }
    
    public Object map(ResultSet rs, Class clazz) throws Exception
    {
        if(clazz == ColecEntidadVO.class)
        {
            return mapearColecEntidadVO(rs);
        }
        else if(clazz == ColecEntidadEnAgrupacionVO.class)
        {
            return mapearColecEntidadEnAgrupacionVO(rs);
        }
        else if(clazz == ColecRepresentanteVO.class)
        {
            return mapearColecRepresentanteVO(rs);
        }
        else if(clazz == ValorCampoDesplegableModuloIntegracionVO.class)
        {
            return mapearValorCampoDesplegableModuloIntegracionVO(rs);
        }
        else if(clazz == ColecSolicitudVO.class)
        {
            return mapearColecSolicitudVO(rs);
        }
        else if(clazz == ColecMunVO.class)
        {
            return mapearColecMunVO(rs);
        }
        else if(clazz == ColecCentrosVO.class)
        {
            return mapearColecCentrosVO(rs);
        }
        else if(clazz == ColecTrayVO.class)
        {
            return mapearColecTrayVO(rs);
        }
        else if(clazz == ColecTrayEspVO.class)
        {
            return mapearColecTrayEspVO(rs);
        }
        else if(clazz == FilaEntidadVO.class)
        {
            return mapearFilaEntidadVO(rs);
        }
        else if(clazz == FilaBusquedaMunicipioVO.class)
        {
            return mapearFilaMunicipioBusquedaVO(rs);
        }
        else if(clazz == FilaColecCentrosVO.class)
        {
            return mapearFilaColecCentrosVO(rs);
        }
        else if(clazz == FilaTrayEspVO.class)
        {
            return mapearFilaTrayEspVO(rs);
        }
        else if(clazz == SelectItem.class)
        {
            return mapearSelectItem(rs);
        }
        else if(clazz == ColecTrayOtroProgramaVO.class)
        {
            return mapearColecTrayOtroProgramaVO(rs);
        }
        else if(clazz == ColecTrayActividadVO.class)
        {
            return mapearColecTrayActividadVO(rs);
        }
        else if(clazz == ColecUbicacionesCTVO.class)
        {
            return mapearColecUbicacionesCTVO(rs);
        }
        else if(clazz == ColecComproRealizacionVO.class)
        {
            return mapearColecComproRealizacionVO(rs);
        }else if(clazz == ColecTrayectoriaEntidad.class)
        {
            return mapearColecTrayectoriaEntidad(rs);
        }else if(clazz == ColecUbicCTValoracion.class)
        {
            return mapearColecUbicCTValoracion(rs);
        }else if(clazz == ColecTrayeEntiValida.class)
        {
            return mapearColecTrayeEntiValida(rs);
        }else if(clazz == ColecColectivo.class)
        {
            return mapearColecColectivo(rs);
        }else if(clazz == ColecAmbitosBloquesHoras.class)
        {
            return mapearColecAmbitosBloquesHoras(rs);
        }else if(clazz == ColecCertCalidad.class)
        {
            return mapearColecCertCalidad(rs);
        }else if(clazz == ColecEntidadCertCalidad.class)
        {
            return mapearColecEntidadCertCalidad(rs);
        }else if(clazz == ColecCompIgualdadPuntuacion.class)
        {
            return mapearColecCompIgualdadPuntuacion(rs);
        }else if(clazz == ColecCertCalidadPuntuacion.class)
        {
            return mapearColecCertCalidadPuntuacion(rs);
        }
        return null;
    }
    private ColecEntidadVO mapearColecEntidadVO(ResultSet rs) throws Exception
    {
        ColecEntidadVO entidad = new ColecEntidadVO();

        entidad.setCodEntidad(rs.getLong("COLEC_ENT_COD"));
        if(rs.wasNull())
        {
            entidad.setCodEntidad(null);
        }
        entidad.setEjercicio(rs.getInt("COLEC_EXP_EJE"));
        if(rs.wasNull())
        {
            entidad.setEjercicio(null);
        }
        entidad.setNumExp(rs.getString("COLEC_NUMEXP"));
        entidad.setAnioConvocatoria(rs.getInt("COLEC_ANIO_CONVOCATORIA"));
        if(rs.wasNull())
        {
            entidad.setAnioConvocatoria(null);
        }
        entidad.setEsAgrupacion(rs.getInt("COLEC_ENT_ESAGRUPACION"));
        if(rs.wasNull())
        {
            entidad.setEsAgrupacion(null);
        }
        entidad.setTipoCif(rs.getString("COLEC_ENT_TIPO_CIF"));
        entidad.setCif(rs.getString("COLEC_ENT_CIF"));
        entidad.setNombre(rs.getString("COLEC_ENT_NOMBRE"));
        entidad.setCentroEspEmpTH(rs.getInt("COLEC_ENT_CENTESPEMPTH"));
        if(rs.wasNull())
        {
            entidad.setCentroEspEmpTH(null);
        }
        entidad.setParticipanteMayorCentEcpEmpTH(rs.getInt("COLEC_ENT_PARTMAYCEETH"));
        if(rs.wasNull())
        {
            entidad.setParticipanteMayorCentEcpEmpTH(null);
        }
        entidad.setEmpresaInsercionTH(rs.getInt("COLEC_ENT_EMPINSERCIONTH"));
        if(rs.wasNull())
        {
            entidad.setEmpresaInsercionTH(null);
        }
        entidad.setPromotorEmpInsercionTH(rs.getInt("COLEC_ENT_PROMOEMPINSTH"));
        if(rs.wasNull())
        {
            entidad.setPromotorEmpInsercionTH(null);
        }
        entidad.setNumTotalEntAgrupacion(rs.getInt("COLEC_ENT_NROTOTINTEG_AGRUP"));
        if(rs.wasNull())
        {
            entidad.setNumTotalEntAgrupacion(null);
        }
        entidad.setPlanIgualdad(rs.getInt("PLANIGUALDAD"));
        if (rs.wasNull()) {
            entidad.setPlanIgualdad(null);
        }
        entidad.setCertificadoCalidad(rs.getInt("CERTIFICADOCALIDAD"));
        if (rs.wasNull()) {
            entidad.setCertificadoCalidad(null);
        }
        entidad.setAceptaNumeroSuperiorHoras(rs.getInt("ACEPTNUMEROSUPEHORAS"));
        if (rs.wasNull()) {
            entidad.setAceptaNumeroSuperiorHoras(null);
        }
        entidad.setSegundosLocalesMismoAmbito(rs.getInt("SEGUNDOLOCALMISMOAMB"));
        if (rs.wasNull()) {
            entidad.setSegundosLocalesMismoAmbito(null);
        }
        if(existeColumnaEnResultSet("TRAYNUMEROMESES_VALIDADOS", rs))
            entidad.setTrayNumeroMesesValidados(rs.getDouble("TRAYNUMEROMESES_VALIDADOS"));
        if(existeColumnaEnResultSet("COLEC_ENT_CENTESPEMPTH_VAL", rs))
            entidad.setCentroEspEmpTHValidado(rs.getInt("COLEC_ENT_CENTESPEMPTH_VAL"));
        if(existeColumnaEnResultSet("COLEC_ENT_PARTMAYCEETH_VAL", rs))
            entidad.setParticipanteMayorCentEcpEmpTHValidado(rs.getInt("COLEC_ENT_PARTMAYCEETH_VAL"));
        if(existeColumnaEnResultSet("COLEC_ENT_EMPINSERCIONTH_VAL", rs))
            entidad.setEmpresaInsercionTHValidado(rs.getInt("COLEC_ENT_EMPINSERCIONTH_VAL"));
        if(existeColumnaEnResultSet("COLEC_ENT_PROMOEMPINSTH_VAL", rs))
            entidad.setPromotorEmpInsercionTHValidado(rs.getInt("COLEC_ENT_PROMOEMPINSTH_VAL"));
        if(existeColumnaEnResultSet("PLANIGUALDAD_VAL", rs))
            entidad.setPlanIgualdadValidado(rs.getInt("PLANIGUALDAD_VAL"));
        if(existeColumnaEnResultSet("CERTIFICADOCALIDAD_VAL", rs))
            entidad.setCertificadoCalidadValidado(rs.getInt("CERTIFICADOCALIDAD_VAL"));
        if(existeColumnaEnResultSet("SEGUNDOLOCALMISMOAMB_VAL", rs))
            entidad.setSegundosLocalesMismoAmbitoValidado(rs.getInt("SEGUNDOLOCALMISMOAMB_VAL"));
        if(existeColumnaEnResultSet("COLEC_ENT_ESAGRUPACION_VAL", rs))
            entidad.setEsAgrupacionValidado(rs.getInt("COLEC_ENT_ESAGRUPACION_VAL"));
        if(existeColumnaEnResultSet("COMP_IGUALDAD_OPCION", rs)) {
            entidad.setCompIgualdadOpcion(NumberUtils.createInteger(rs.getString("COMP_IGUALDAD_OPCION")));
        }
        if(existeColumnaEnResultSet("COMP_IGUALDAD_OPCION_VAL", rs)) {
            entidad.setCompIgualdadOpcionVal(NumberUtils.createInteger(rs.getString("COMP_IGUALDAD_OPCION_VAL")));
        }
        if(existeColumnaEnResultSet("COMP_IGUALDAD_OPCION_TEXTO", rs))
            entidad.setCompIgualdadOpcionLiteral(rs.getString("COMP_IGUALDAD_OPCION_TEXTO"));
        if(existeColumnaEnResultSet("COMP_IGUALDAD_OPCION_VAL_TEXTO", rs))
            entidad.setCompIgualdadOpcionValLiteral(rs.getString("COMP_IGUALDAD_OPCION_VAL_TEXTO"));
        if(existeColumnaEnResultSet("ENT_SUJETA_DER_PUBL", rs)) {
            entidad.setEntSujetaDerPubl(NumberUtils.createInteger(rs.getString("ENT_SUJETA_DER_PUBL")));
        }
        if(existeColumnaEnResultSet("ENT_SUJETA_DER_PUBL_VAL", rs)) {
            entidad.setEntSujetaDerPublVal(NumberUtils.createInteger(rs.getString("ENT_SUJETA_DER_PUBL_VAL")));
        }
        if(existeColumnaEnResultSet("ENT_SIN_ANIMO_LUCRO", rs)) {
            entidad.setEntSinAnimoLucro(NumberUtils.createInteger(rs.getString("ENT_SIN_ANIMO_LUCRO")));
        }
        if(existeColumnaEnResultSet("ENT_SIN_ANIMO_LUCRO_VAL", rs)) {
            entidad.setEntSinAnimoLucroVal(NumberUtils.createInteger(rs.getString("ENT_SIN_ANIMO_LUCRO_VAL")));
        }

        return entidad;
    }
    
     private ColecEntidadEnAgrupacionVO mapearColecEntidadEnAgrupacionVO(ResultSet rs) throws Exception
    {
        ColecEntidadEnAgrupacionVO entidad = new ColecEntidadEnAgrupacionVO();
        
        entidad.setCodEntidadPadreAgrup(rs.getLong("COLEC_ENT_AGRUP_COD"));
        if(rs.wasNull())
        {
            entidad.setCodEntidadPadreAgrup(null);
        }
        entidad.setCodEntidad(rs.getLong("COLEC_ENT_COD"));
        if(rs.wasNull())
        {
            entidad.setCodEntidad(null);
        }
        entidad.setNumExp(rs.getString("COLEC_NUMEXP"));
        entidad.setTipoCif(rs.getString("COLEC_ENT_TIPO_CIF"));
        entidad.setCif(rs.getString("COLEC_ENT_CIF"));
        entidad.setNombre(rs.getString("COLEC_ENT_NOMBRE"));
        entidad.setCentroEspEmpTH(rs.getInt("COLEC_ENT_CENTESPEMPTH"));
        if(rs.wasNull())
        {
            entidad.setCentroEspEmpTH(null);
        }
        entidad.setParticipanteMayorCentEcpEmpTH(rs.getInt("COLEC_ENT_PARTMAYCEETH"));
        if(rs.wasNull())
        {
            entidad.setParticipanteMayorCentEcpEmpTH(null);
        }
        entidad.setEmpresaInsercionTH(rs.getInt("COLEC_ENT_EMPINSERCIONTH"));
        if(rs.wasNull())
        {
            entidad.setEmpresaInsercionTH(null);
        }
        entidad.setPromotorEmpInsercionTH(rs.getInt("COLEC_ENT_PROMOEMPINSTH"));
        if(rs.wasNull())
        {
            entidad.setPromotorEmpInsercionTH(null);
        }
        entidad.setPorcentaCompromisoRealizacion(rs.getDouble("COLEC_ENT_PORCEN_COMPROM_REALI"));
        if(rs.wasNull())
        {
            entidad.setPorcentaCompromisoRealizacion(null);
        }
        entidad.setPlanIgualdad(rs.getInt("PLANIGUALDAD"));
        if (rs.wasNull()) {
            entidad.setPlanIgualdad(null);
        }
        entidad.setCertificadoCalidad(rs.getInt("CERTIFICADOCALIDAD"));
        if (rs.wasNull()) {
            entidad.setCertificadoCalidad(null);
        }
        entidad.setAceptaNumeroSuperiorHoras(rs.getInt("ACEPTNUMEROSUPEHORAS"));
        if (rs.wasNull()) {
            entidad.setAceptaNumeroSuperiorHoras(null);
        }
        entidad.setSegundosLocalesMismoAmbito(rs.getInt("SEGUNDOLOCALMISMOAMB"));
        if (rs.wasNull()) {
            entidad.setSegundosLocalesMismoAmbito(null);
        }
        if(existeColumnaEnResultSet("COMP_IGUALDAD_OPCION", rs)) {
            entidad.setCompIgualdadOpcion(NumberUtils.createInteger(rs.getString("COMP_IGUALDAD_OPCION")));
        }
        if(existeColumnaEnResultSet("ENT_SUJETA_DER_PUBL", rs)) {
            entidad.setEntSujetaDerPubl(NumberUtils.createInteger(rs.getString("ENT_SUJETA_DER_PUBL")));
        }
        if(existeColumnaEnResultSet("ENT_SIN_ANIMO_LUCRO", rs)) {
            entidad.setEntSinAnimoLucro(NumberUtils.createInteger(rs.getString("ENT_SIN_ANIMO_LUCRO")));
        }

        return entidad;
    }
    
    private ColecRepresentanteVO mapearColecRepresentanteVO(ResultSet rs) throws Exception
    {
        ColecRepresentanteVO representante = new ColecRepresentanteVO();

        representante.setCodRepresentante(rs.getLong("COD_REPRESENTANTE"));
        if(rs.wasNull())
        {
            representante.setCodRepresentante(null);
        }
        representante.setNumExp(rs.getString("EXPEDIENTE"));
        representante.setEjercicio(rs.getInt("EJERCICIO"));
        representante.setCodTipoDoc(rs.getString("COD_TIPO_DOC"));
        representante.setDocumento(rs.getString("DOCUMENTO"));
        representante.setNombre(rs.getString("NOMBRE"));
        
        return representante;
    }
    
    private ValorCampoDesplegableModuloIntegracionVO mapearValorCampoDesplegableModuloIntegracionVO(ResultSet rs) throws Exception
    {
        ValorCampoDesplegableModuloIntegracionVO campo = new ValorCampoDesplegableModuloIntegracionVO();
        campo.setCodigo(rs.getString("CODIGO"));
        campo.setDescripcion(rs.getString("DESCRIPCION"));
        return campo;
    }
    
    private ColecSolicitudVO mapearColecSolicitudVO(ResultSet rs) throws Exception
    {
        ColecSolicitudVO solicitud = new ColecSolicitudVO();
        solicitud.setCodSolicitud(rs.getLong("COLEC_COD_SOLICITUD"));
        if(rs.wasNull())
        {
            solicitud.setCodSolicitud(null);
        }
        solicitud.setNumExp(rs.getString("COLEC_NUMEXP"));
        solicitud.setEjercicio(rs.getInt("COLEC_EXP_EJE"));
        if(rs.wasNull())
        {
            solicitud.setEjercicio(null);
        }
        solicitud.setCol1Ar(rs.getInt("COLEC_COL1_AR"));
        if(rs.wasNull())
        {
            solicitud.setCol1Ar(null);
        }
        solicitud.setCol1Bi(rs.getInt("COLEC_COL1_BI"));
        if(rs.wasNull())
        {
            solicitud.setCol1Bi(null);
        }
        solicitud.setCol1Gi(rs.getInt("COLEC_COL1_GI"));
        if(rs.wasNull())
        {
            solicitud.setCol1Gi(null);
        }
        solicitud.setCol2Ar(rs.getInt("COLEC_COL2_AR"));
        if(rs.wasNull())
        {
            solicitud.setCol2Ar(null);
        }
        solicitud.setCol2Bi(rs.getInt("COLEC_COL2_BI"));
        if(rs.wasNull())
        {
            solicitud.setCol2Bi(null);
        }
        solicitud.setCol2Gi(rs.getInt("COLEC_COL2_GI"));
        if(rs.wasNull())
        {
            solicitud.setCol2Gi(null);
        }
        solicitud.setCol3Ar(rs.getInt("COLEC_COL3_AR"));
        if(rs.wasNull())
        {
            solicitud.setCol3Ar(null);
        }
        solicitud.setCol3Bi(rs.getInt("COLEC_COL3_BI"));
        if(rs.wasNull())
        {
            solicitud.setCol3Bi(null);
        }
        solicitud.setCol3Gi(rs.getInt("COLEC_COL3_GI"));
        if(rs.wasNull())
        {
            solicitud.setCol3Gi(null);
        }
        solicitud.setCol4Ar(rs.getInt("COLEC_COL4_AR"));
        if(rs.wasNull())
        {
            solicitud.setCol4Ar(null);
        }
        solicitud.setCol4Bi(rs.getInt("COLEC_COL4_BI"));
        if(rs.wasNull())
        {
            solicitud.setCol4Bi(null);
        }
        solicitud.setCol4Gi(rs.getInt("COLEC_COL4_GI"));
        if(rs.wasNull())
        {
            solicitud.setCol4Gi(null);
        }
        solicitud.setCol1Ult5Val(rs.getLong("COLEC1_ULT5_VAL"));
        if(rs.wasNull())
        {
            solicitud.setCol1Ult5Val(null);
        }
        solicitud.setCol2Ult5Val(rs.getLong("COLEC2_ULT5_VAL"));
        if(rs.wasNull())
        {
            solicitud.setCol2Ult5Val(null);
        }
        solicitud.setTotVal(rs.getLong("COLEC_TOT_VAL"));
        if(rs.wasNull())
        {
            solicitud.setTotVal(null);
        }
        solicitud.setCodigoColectivo(rs.getInt("codigoColectivo"));
        if(rs.wasNull())
        {
            solicitud.setCodigoColectivo(null);
        }
        solicitud.setTerritorioHistorico(rs.getInt("territorioHistorico"));
        if(rs.wasNull())
        {
            solicitud.setTerritorioHistorico(null);
        }
        solicitud.setAmbitoComarca(rs.getInt("AMBITOCOMARCA"));
        if(rs.wasNull())
        {
            solicitud.setAmbitoComarca(null);
        }
        solicitud.setNumeroBloquesHoras(rs.getInt("numeroBloquesHoras"));
        if(rs.wasNull())
        {
            solicitud.setNumeroBloquesHoras(null);
        }
        solicitud.setNumeroUbicaciones(rs.getInt("numeroUbicaciones"));
        if(rs.wasNull())
        {
            solicitud.setNumeroUbicaciones(null);
        }
        return solicitud;
    }
    
    private ColecMunVO mapearColecMunVO(ResultSet rs) throws Exception
    {
        ColecMunVO mun = new ColecMunVO();
        mun.setCodComarca(rs.getLong("COLEC_COD_COMARCA"));
        if(rs.wasNull())
        {
            mun.setCodComarca(null);
        }
        mun.setCodMun(rs.getLong("COLEC_COD_MUN"));
        if(rs.wasNull())
        {
            mun.setCodMun(null);
        }
        mun.setDescMun(rs.getString("COLEC_DESC_MUN"));
        if(existeColumnaEnResultSet("COLEC_COD_TTHH", rs))
            mun.setCodTthh(rs.getInt("COLEC_COD_TTHH"));
        if(existeColumnaEnResultSet("COLEC_DESC_MUN_EU", rs))
            mun.setDescMunEu(rs.getString("COLEC_DESC_MUN_EU"));
        return mun;
    }
    
    private ColecCentrosVO mapearColecCentrosVO(ResultSet rs) throws Exception
    {
        ColecCentrosVO centro = new ColecCentrosVO();
        centro.setAmbito(rs.getInt("COLEC_AMBITO"));
        if(rs.wasNull())
        {
            centro.setAmbito(null);
        }
        centro.setCodCentro(rs.getLong("COLEC_COD_CENTROS"));
        if(rs.wasNull())
        {
            centro.setCodCentro(null);
        }
        centro.setColectivo(rs.getInt("COLEC_COLECTIVO"));
        if(rs.wasNull())
        {
            centro.setColectivo(null);
        }
        centro.setComarca(rs.getLong("COLEC_COMARCA"));
        if(rs.wasNull())
        {
            centro.setComarca(null);
        }
        centro.setDireccion(rs.getString("COLEC_DIRECCION"));
        centro.setExpEje(rs.getInt("COLEC_EXP_EJE"));
        if(rs.wasNull())
        {
            centro.setExpEje(null);
        }
        centro.setFecSysdate(rs.getDate("COLEC_FEC_SYSDATE"));
        centro.setMun(rs.getLong("COLEC_MUN"));
        if(rs.wasNull())
        {
            centro.setMun(null);
        }
        centro.setNumExp(rs.getString("COLEC_NUMEXP"));
        return centro;
    }
    
    private ColecTrayVO mapearColecTrayVO(ResultSet rs) throws Exception
    {
        ColecTrayVO tray = new ColecTrayVO();
        tray.setCodTray(rs.getLong("COLEC_COD_TRAY"));
        if(rs.wasNull())
        {
            tray.setCodTray(null);
        }
        tray.setCodEntidad(rs.getLong("COLEC_COD_ENTIDAD"));
        if(rs.wasNull())
        {
            tray.setCodEntidad(null);
        }
        tray.setNumExp(rs.getString("COLEC_NUMEXP"));
        tray.setExpEje(rs.getInt("COLEC_EXP_EJE"));
        if(rs.wasNull())
        {
            tray.setExpEje(null);
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
        
        tray.setAct56_03(rs.getLong("COLEC1_ACT_56_03"));
        if(rs.wasNull())
        {
            tray.setAct56_03(null);
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
        tray.setColec1_ult5_val(rs.getLong("COLEC1_ULT5_VAL"));
        if(rs.wasNull())
        {
            tray.setColec1_ult5_val(null);
        }
        tray.setColec2_ult5_val(rs.getLong("COLEC2_ULT5_VAL"));
        if(rs.wasNull())
        {
            tray.setColec2_ult5_val(null);
        }
        tray.setColec_tot_total(rs.getLong("COLEC_TOT_VAL"));
        if(rs.wasNull())
        {
            tray.setColec_tot_total(null);
        }
        return tray;
    }
    
    private ColecTrayEspVO mapearColecTrayEspVO(ResultSet rs) throws Exception
    {
        ColecTrayEspVO tray = new ColecTrayEspVO();
        tray.setCodEntidad(rs.getLong("COLEC_COD_ENTIDAD"));
        if(rs.wasNull())
        {
            tray.setCodEntidad(null);
        }
        tray.setCodTrayEsp(rs.getLong("COLEC_COD_TRAY_ESP"));
        if(rs.wasNull())
        {
            tray.setCodTrayEsp(null);
        }
        tray.setColectivo(rs.getInt("COLEC_COLECTIVO"));
        if(rs.wasNull())
        {
            tray.setColectivo(null);
        }
        tray.setDescActividad(rs.getString("COLEC_DESC_ACTIVIDAD"));
        tray.setExpEje(rs.getInt("COLEC_EXP_EJE"));
        if(rs.wasNull())
        {
            tray.setExpEje(null);
        }
        tray.setNombreAdm(rs.getString("COLEC_NOMBRE_ADM"));
        tray.setNumExp(rs.getString("COLEC_NUMEXP"));
        tray.setValidada(rs.getInt("COLEC_VALIDADA"));
        if(rs.wasNull())
        {
            tray.setValidada(null);
        }
        return tray;
    }
    
    private SelectItem mapearSelectItem(ResultSet rs) throws Exception
    {
        SelectItem si = new SelectItem();
        si.setCodigo(rs.getString("CODIGO"));
        si.setDescripcion(rs.getString("DESCRIPCION"));
        return si;
    }
    
    private FilaEntidadVO mapearFilaEntidadVO(ResultSet rs) throws Exception
    {
        FilaEntidadVO entidad = new FilaEntidadVO();
        entidad.setCodEntidad(rs.getLong("COD_ENTIDAD"));
        if(rs.wasNull())
        {
            entidad.setCodEntidad(null);
        }
        entidad.setCodEntidadPadreAgrup(rs.getLong("COD_ENTIDAD_PADRE"));
        if(rs.wasNull())
        {
            entidad.setCodEntidad(null);
        }
        entidad.setNumExp(rs.getString("EXPEDIENTE"));
        entidad.setEjercicio(rs.getInt("EJERCICIO"));
        if(rs.wasNull())
        {
            entidad.setEjercicio(null);
        }
        entidad.setAnioConvocatoria(rs.getInt("ANIO_CONVOCATORIA"));
        if(rs.wasNull())
        {
            entidad.setAnioConvocatoria(null);
        }
        //entidad.setTipoCif(rs.getString("TIPO_CIF"));
        entidad.setCif(rs.getString("CIF"));
        entidad.setNombre(rs.getString("NOMBRE"));
        entidad.setEsAgrupacion(rs.getInt("ES_AGRUPACION"));
        if(rs.wasNull())
        {
            entidad.setEsAgrupacion(null);
        }
        entidad.setCentroEspEmpTH(rs.getInt("CENTRO_ESPECIAL"));
        if(rs.wasNull())
        {
            entidad.setCentroEspEmpTH(null);
        }
        entidad.setParticipanteMayorCentEcpEmpTH(rs.getInt("PART_MAYO_CENT_ESPECIAL"));
        if(rs.wasNull())
        {
            entidad.setParticipanteMayorCentEcpEmpTH(null);
        }
        entidad.setEmpresaInsercionTH(rs.getInt("EMPRESA_INSERCION"));
        if(rs.wasNull())
        {
            entidad.setEmpresaInsercionTH(null);
        }
        entidad.setPromotorEmpInsercionTH(rs.getInt("PROMOTOR_EMP_INSERCION"));
        if(rs.wasNull())
        {
            entidad.setPromotorEmpInsercionTH(null);
        }
        entidad.setNumTotalEntAgrupacion(rs.getInt("NUM_TOTAL_ENT_AGRUP"));
        if(rs.wasNull())
        {
            entidad.setNumTotalEntAgrupacion(null);
        }
        entidad.setPorcentaCompromisoRealizacion(rs.getDouble("PORC_COMPRO_REALIZA"));
        if(rs.wasNull())
        {
            entidad.setPorcentaCompromisoRealizacion(null);
        }
        entidad.setPlanIgualdad(rs.getInt("PLANIGUALDAD"));
        if (rs.wasNull()) {
            entidad.setPlanIgualdad(null);
        }
        entidad.setCertificadoCalidad(rs.getInt("CERTIFICADOCALIDAD"));
        if (rs.wasNull()) {
            entidad.setCertificadoCalidad(null);
        }
        entidad.setAceptaNumeroSuperiorHoras(rs.getInt("ACEPTNUMEROSUPEHORAS"));
        if (rs.wasNull()) {
            entidad.setAceptaNumeroSuperiorHoras(null);
        }
        entidad.setSegundosLocalesMismoAmbito(rs.getInt("SEGUNDOLOCALMISMOAMB"));
        if (rs.wasNull()) {
            entidad.setSegundosLocalesMismoAmbito(null);
        }
        return entidad;
    }
    
    private FilaBusquedaMunicipioVO mapearFilaMunicipioBusquedaVO(ResultSet rs) throws Exception
    {
        FilaBusquedaMunicipioVO fila = new FilaBusquedaMunicipioVO();
        fila.setCodMunicipio(rs.getLong("COD_MUNICIPIO"));
        if(rs.wasNull())
        {
            fila.setCodMunicipio(null);
        }
        fila.setCodComarca(rs.getLong("COD_COMARCA"));
        if(rs.wasNull())
        {
            fila.setCodComarca(null);
        }
        fila.setDescComarca(rs.getString("DESC_COMARCA"));
        fila.setDescMunicipio(rs.getString("DESC_MUNICIPIO"));
        return fila;
    }
    
    public FilaColecCentrosVO mapearFilaColecCentrosVO(ResultSet rs) throws Exception
    {
        FilaColecCentrosVO fila = new FilaColecCentrosVO();
        fila.setAmbito(rs.getInt("COD_AMBITO"));
        if(rs.wasNull())
        {
            fila.setAmbito(null);
        }
        fila.setCodCentro(rs.getLong("COD_CENTRO"));
        if(rs.wasNull())
        {
            fila.setCodCentro(null);
        }
        fila.setColectivo(rs.getInt("COLECTIVO"));
        if(rs.wasNull())
        {
            fila.setColectivo(null);
        }
        fila.setComarca(rs.getLong("COD_COMARCA"));
        if(rs.wasNull())
        {
            fila.setComarca(null);
        }
        fila.setDescAmbito(rs.getString("DESC_AMBITO") != null ? rs.getString("DESC_AMBITO") : "");
        fila.setDescComarca(rs.getString("DESC_COMARCA") != null ? rs.getString("DESC_COMARCA") : "");
        fila.setDescMun(rs.getString("DESC_MUN") != null ? rs.getString("DESC_MUN") : "");
        fila.setDireccion(rs.getString("DIRECCION") != null ? rs.getString("DIRECCION") : "");
        fila.setMunicipio(rs.getLong("COD_MUN"));
        if(rs.wasNull())
        {
            fila.setMunicipio(null);
        }
        return fila;
    }
    
    public FilaTrayEspVO mapearFilaTrayEspVO(ResultSet rs) throws Exception
    {
        String strValor = "";
        FilaTrayEspVO fila = new FilaTrayEspVO();
        fila.setCodTray(rs.getLong("COLEC_COD_TRAY_ESP"));
        if(rs.wasNull())
        {
            fila.setCodTray(null);
        }
        fila.setCodEntidad(rs.getLong("COLEC_COD_ENTIDAD"));
        if(rs.wasNull())
        {
            fila.setCodEntidad(null);
        }
        fila.setNumExpediente(rs.getString("COLEC_NUMEXP"));
        fila.setEjercicio(rs.getInt("COLEC_EXP_EJE"));
        if(rs.wasNull())
        {
            fila.setColectivo(null);
        }
        fila.setColectivo(rs.getInt("COLEC_COLECTIVO"));
        if(rs.wasNull())
        {
            fila.setColectivo(null);
        }
        strValor = rs.getString("COLEC_NOMBRE_ADM");
        fila.setNombreAdm(strValor != null && !strValor.equals("") ? strValor : "-");
        strValor = rs.getString("COLEC_DESC_ACTIVIDAD");
        fila.setDescAct(strValor != null && !strValor.equals("") ? strValor : "-");
        //strValor = rs.getString("NOM_ENT");
        //fila.setNomEntidad(strValor != null && !strValor.equals("") ? strValor : "-");
        fila.setValidada(rs.getInt("COLEC_VALIDADA"));
        if(rs.wasNull())
        {
            fila.setValidada(0);
        }
        fila.setCifEntidad(rs.getString("COLEC_ENT_CIF"));
        fila.setNomEntidad(rs.getString("COLEC_ENT_NOMBRE"));
        return fila;
    }
    
    public List<SelectItem> fromCampoDesplegableToSelectItemVO(List<ValorCampoDesplegableModuloIntegracionVO> fromList)
    {
        List<SelectItem> toList = new ArrayList<SelectItem>();
        if(fromList != null && fromList.size() > 0)
        {
            for(ValorCampoDesplegableModuloIntegracionVO v : fromList)
            {
                SelectItem si = new SelectItem();
                si.setCodigo(v.getCodigo());
                si.setDescripcion(v.getDescripcion() != null ? v.getDescripcion().toUpperCase() : v.getCodigo());
                toList.add(si);
            }
        }
        Collections.sort(toList);
        return toList;
    }
    
    public List<SelectItem> fromCampoDesplegableToSelectItemVO(List<ValorCampoDesplegableModuloIntegracionVO> fromList, int sortMode)
    {
        List<SelectItem> toList = new ArrayList<SelectItem>();
        if(fromList != null && fromList.size() > 0)
        {
            for(ValorCampoDesplegableModuloIntegracionVO v : fromList)
            {
                SelectItem si = new SelectItem();
                si.setCodigo(v.getCodigo());
                si.setDescripcion(v.getDescripcion() != null ? v.getDescripcion().toUpperCase() : v.getCodigo());
                si.setModoOrdenacion(sortMode);
                toList.add(si);
            }
        }
        Collections.sort(toList);
        return toList;
    }

    public FilaEntidadVO mapearFromColecEntidadEnAgrupacionVO_To_FilaEntidadVO(ColecEntidadEnAgrupacionVO entidadEnAgrup) {
        FilaEntidadVO returnvalue = new FilaEntidadVO();
        if(entidadEnAgrup!=null){
            returnvalue.setCodEntidad(entidadEnAgrup.getCodEntidad());
            returnvalue.setCodEntidadPadreAgrup(entidadEnAgrup.getCodEntidadPadreAgrup());
            returnvalue.setEjercicio(MeLanbide48Utils.getEjercicioDeExpediente(entidadEnAgrup.getNumExp()));
            returnvalue.setNumExp(entidadEnAgrup.getNumExp());
            returnvalue.setTipoCif(entidadEnAgrup.getTipoCif());
            returnvalue.setCif(entidadEnAgrup.getCif());
            returnvalue.setNombre(entidadEnAgrup.getNombre());
            returnvalue.setCentroEspEmpTH(entidadEnAgrup.getCentroEspEmpTH());
            returnvalue.setParticipanteMayorCentEcpEmpTH(entidadEnAgrup.getParticipanteMayorCentEcpEmpTH());
            returnvalue.setEmpresaInsercionTH(entidadEnAgrup.getEmpresaInsercionTH());
            returnvalue.setPromotorEmpInsercionTH(entidadEnAgrup.getPromotorEmpInsercionTH());
            returnvalue.setPorcentaCompromisoRealizacion(entidadEnAgrup.getPorcentaCompromisoRealizacion());
        }
        return returnvalue;
    }

    public FilaEntidadVO mapearFromColecEntidadVO_To_FilaEntidadVO(ColecEntidadVO entidadPadre) {
        FilaEntidadVO returnvalue = new FilaEntidadVO();
        if (entidadPadre != null) {
            returnvalue.setCodEntidad(entidadPadre.getCodEntidad());
            returnvalue.setEjercicio(entidadPadre.getEjercicio());
            returnvalue.setNumExp(entidadPadre.getNumExp());
            returnvalue.setAnioConvocatoria(entidadPadre.getAnioConvocatoria());
            returnvalue.setEsAgrupacion(entidadPadre.getEsAgrupacion());
            returnvalue.setTipoCif(entidadPadre.getTipoCif());
            returnvalue.setCif(entidadPadre.getCif());
            returnvalue.setNombre(entidadPadre.getNombre());
            returnvalue.setCentroEspEmpTH(entidadPadre.getCentroEspEmpTH());
            returnvalue.setParticipanteMayorCentEcpEmpTH(entidadPadre.getParticipanteMayorCentEcpEmpTH());
            returnvalue.setEmpresaInsercionTH(entidadPadre.getEmpresaInsercionTH());
            returnvalue.setPromotorEmpInsercionTH(entidadPadre.getPromotorEmpInsercionTH());
            returnvalue.setNumTotalEntAgrupacion(entidadPadre.getNumTotalEntAgrupacion());
        }
        return returnvalue;
    }

    public ColecTrayOtroProgramaVO mapearColecTrayOtroProgramaVO(ResultSet rs) throws SQLException {
        ColecTrayOtroProgramaVO colecTrayOtroProgramaVO = new ColecTrayOtroProgramaVO();
        colecTrayOtroProgramaVO.setCodIdOtroPrograma(rs.getInt("COLEC_OTRPRO_COD"));
        if(rs.wasNull())
        {
            colecTrayOtroProgramaVO.setCodIdOtroPrograma(null);
        }
        colecTrayOtroProgramaVO.setTipoOtroPrograma(rs.getString("COLEC_OTRPRO_TIPO"));
        colecTrayOtroProgramaVO.setEjercicio(rs.getInt("COLEC_OTRPRO_EXP_EJE"));
        if(rs.wasNull())
        {
            colecTrayOtroProgramaVO.setEjercicio(null);
        }
        colecTrayOtroProgramaVO.setNumExpediente(rs.getString("COLEC_OTRPRO_NUMEXP"));
        colecTrayOtroProgramaVO.setCodEntidad(rs.getInt("COLEC_OTRPRO_COD_ENTIDAD"));
        if(rs.wasNull())
        {
            colecTrayOtroProgramaVO.setCodEntidad(null);
        }
        colecTrayOtroProgramaVO.setPrograma(rs.getString("COLEC_OTRPRO_PROGRAMA"));
        colecTrayOtroProgramaVO.setFechaInicio(rs.getDate("COLEC_OTRPRO_PROG_INICIO"));
        if(rs.wasNull())
        {
            colecTrayOtroProgramaVO.setFechaInicio(null);
        }
        colecTrayOtroProgramaVO.setFechaFin(rs.getDate("COLEC_OTRPRO_PROG_FIN"));
        if(rs.wasNull())
        {
            colecTrayOtroProgramaVO.setFechaFin(null);
        }
        // Valores Adicionales que no estan en la tabla 
        // Comprobamos por si no viene la columna.
        if(existeColumnaEnResultSet("COLEC_ENT_CIF", rs)){
            colecTrayOtroProgramaVO.setCifEntidad(rs.getString("COLEC_ENT_CIF"));
        }
        if(existeColumnaEnResultSet("COLEC_ENT_NOMBRE", rs)){
            colecTrayOtroProgramaVO.setNombreEntidad(rs.getString("COLEC_ENT_NOMBRE"));
        }
        return colecTrayOtroProgramaVO;
    }

    public ColecTrayActividadVO mapearColecTrayActividadVO(ResultSet rs) throws SQLException {
        ColecTrayActividadVO colecTrayActividadVO = new ColecTrayActividadVO();
        colecTrayActividadVO.setCodIdActividad(rs.getInt("COLEC_ACTIV_COD"));
        if(rs.wasNull())
        {
            colecTrayActividadVO.setCodIdActividad(null);
        }
        colecTrayActividadVO.setTipoActividad(rs.getString("COLEC_ACTIV_TIPO"));
        colecTrayActividadVO.setEjercicio(rs.getInt("COLEC_ACTIV_EXP_EJE"));
        if(rs.wasNull())
        {
            colecTrayActividadVO.setEjercicio(null);
        }
        colecTrayActividadVO.setNumExpediente(rs.getString("COLEC_ACTIV_NUMEXP"));
        colecTrayActividadVO.setCodEntidad(rs.getInt("COLEC_ACTIV_COD_ENTIDAD"));
        if(rs.wasNull())
        {
            colecTrayActividadVO.setCodEntidad(null);
        }
        colecTrayActividadVO.setDesActividadyServPublEmp(rs.getString("COLEC_ACTIV_DESC_SERVPUB"));
        colecTrayActividadVO.setFechaInicio(rs.getDate("COLEC_ACTIV_INICIO"));
        if(rs.wasNull())
        {
            colecTrayActividadVO.setFechaInicio(null);
        }
        colecTrayActividadVO.setFechaFin(rs.getDate("COLEC_ACTIV_FIN"));
        if(rs.wasNull())
        {
            colecTrayActividadVO.setFechaFin(null);
        }
        // Valores Adicionales que no estan en la tabla 
        // Comprobamos por si no viene la columna.
        if(existeColumnaEnResultSet("COLEC_ENT_CIF", rs)){
            colecTrayActividadVO.setCifEntidad(rs.getString("COLEC_ENT_CIF"));
        }
        if(existeColumnaEnResultSet("COLEC_ENT_NOMBRE", rs)){
            colecTrayActividadVO.setNombreEntidad(rs.getString("COLEC_ENT_NOMBRE"));
        }
        return colecTrayActividadVO;
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

    private ColecUbicacionesCTVO mapearColecUbicacionesCTVO(ResultSet rs) throws SQLException {
        ColecUbicacionesCTVO colecUbicacionesCTVO = new ColecUbicacionesCTVO();
        colecUbicacionesCTVO.setCodId(rs.getLong("COLEC_UBIC_CT_COD"));
        if(rs.wasNull())
        {
            colecUbicacionesCTVO.setCodId(null);
        }
        colecUbicacionesCTVO.setCodTipoColectivo(rs.getString("COLEC_UBIC_CT_TIPO"));
        colecUbicacionesCTVO.setCodEntidad(rs.getLong("COLEC_UBIC_CT_CODENTIDAD"));
        if(rs.wasNull())
        {
            colecUbicacionesCTVO.setCodEntidad(null);
        }
        colecUbicacionesCTVO.setTerritorioHist(rs.getInt("COLEC_UBIC_CT_TERRITORIO"));
        if(rs.wasNull())
        {
            colecUbicacionesCTVO.setTerritorioHist(null);
        }
        colecUbicacionesCTVO.setComarca(rs.getInt("COLEC_UBIC_CT_NROCOMARCA"));
        if(rs.wasNull())
        {
            colecUbicacionesCTVO.setComarca(null);
        }
        colecUbicacionesCTVO.setMunicipio(rs.getInt("COLEC_UBIC_CT_MUNICIPIO"));
        if(rs.wasNull())
        {
            colecUbicacionesCTVO.setMunicipio(null);
        }
        colecUbicacionesCTVO.setLocalidad(rs.getString("COLEC_UBIC_CT_LOCALIDAD"));
        colecUbicacionesCTVO.setDireccion(rs.getString("COLEC_UBIC_CT_DIRECCION"));
        colecUbicacionesCTVO.setDireccionPortal(rs.getString("COLEC_UBIC_CT_PORTAL_DIR"));
        colecUbicacionesCTVO.setDireccionPiso(rs.getString("COLEC_UBIC_CT_PISO_DIR"));
        colecUbicacionesCTVO.setDireccionLetra(rs.getString("COLEC_UBIC_CT_LETRA_DIR"));
        colecUbicacionesCTVO.setCodigoPostal(rs.getString("COLEC_UBIC_CT_CODPOSTAL"));
        colecUbicacionesCTVO.setTelefono(rs.getString("COLEC_UBIC_CT_TELEFONO"));
        colecUbicacionesCTVO.setNumExpediente(rs.getString("COLEC_UBIC_CT_NUMEXP"));
        colecUbicacionesCTVO.setLocalesPreviamenteAprobados(rs.getInt("COLEC_UBIC_CT_LOCALPREVAPRO"));
        if(rs.wasNull())
        {
            colecUbicacionesCTVO.setLocalesPreviamenteAprobados(null);
        }
        colecUbicacionesCTVO.setMantieneRequisitosLocalesAprob(rs.getInt("COLEC_UBIC_CT_MATENREQ_LPA"));
        if(rs.wasNull())
        {
            colecUbicacionesCTVO.setMantieneRequisitosLocalesAprob(null);
        }
        colecUbicacionesCTVO.setDisponeEspacioComplWifi(rs.getInt("COLEC_UBIC_CT_ESP_COMP_WIFI"));
        if(rs.wasNull())
        {
            colecUbicacionesCTVO.setDisponeEspacioComplWifi(null);
        }
        if (existeColumnaEnResultSet("FK_ID_AMBITO_SOLICITADO", rs)) {
            colecUbicacionesCTVO.setFkIdAmbitoSolicitado(rs.getInt("FK_ID_AMBITO_SOLICITADO"));
            if (rs.wasNull()) {
                colecUbicacionesCTVO.setFkIdAmbitoSolicitado(null);
            }
        }
        // Valores Adicionales que no estan en la tabla 
        // Comprobamos por si no viene la columna.
        if(existeColumnaEnResultSet("COLEC_ENT_CIF", rs)){
            colecUbicacionesCTVO.setCifEntidad(rs.getString("COLEC_ENT_CIF"));
        }
        if(existeColumnaEnResultSet("COLEC_ENT_NOMBRE", rs)){
            colecUbicacionesCTVO.setNombreEntidad(rs.getString("COLEC_ENT_NOMBRE"));
        }
        if(existeColumnaEnResultSet("DESC_TERRITORIO", rs)){
            colecUbicacionesCTVO.setDescTerritorioHist(rs.getString("DESC_TERRITORIO"));
        }
        if(existeColumnaEnResultSet("DESC_COMARCA", rs)){
            colecUbicacionesCTVO.setDescComarca(rs.getString("DESC_COMARCA"));
        }
        if(existeColumnaEnResultSet("DESC_MUNICPIO", rs)){
            colecUbicacionesCTVO.setDescMunicipio(rs.getString("DESC_MUNICPIO"));
        }
        if(existeColumnaEnResultSet("descColectivo", rs)){
            colecUbicacionesCTVO.setDescColectivo(rs.getString("descColectivo"));
        }
        if(existeColumnaEnResultSet("desc_ambitosolicitado", rs)){
            colecUbicacionesCTVO.setDescAmbitoSolicitado(rs.getString("desc_ambitosolicitado"));
        }
        
        return colecUbicacionesCTVO;
    }

    private Object mapearColecComproRealizacionVO(ResultSet rs) throws SQLException {
        ColecComproRealizacionVO colecComproRealizacionVO = new ColecComproRealizacionVO();
        colecComproRealizacionVO.setCodigoID(rs.getInt("COLEC_COMPREAL_COD_ID"));
        if(rs.wasNull())
        {
            colecComproRealizacionVO.setCodigoID(null);
        }
        colecComproRealizacionVO.setEjercicio(rs.getInt("COLEC_COMPREAL_EJERCICIO"));
        if(rs.wasNull())
        {
            colecComproRealizacionVO.setEjercicio(null);
        }
        colecComproRealizacionVO.setNumExpediente(rs.getString("COLEC_COMPREAL_NUM_EXP"));
        colecComproRealizacionVO.setCodigoEntidad(rs.getInt("COLEC_ENT_COD"));
        // COLEC_COMPREAL_COD_ENTIDAD --> Asignamos el codigo de la entidad desde la tabla entidad
        // Cuando no hay registros guardados en  tabla de porcentajes, se recuepera la lista de la entidaddes si las hay,
        // Y ese codigo viene a null , se crearian todos los txt con el mismo codigo de entidad = 0.
        if(rs.wasNull())
        {
            colecComproRealizacionVO.setCodigoEntidad(null);
        }
        colecComproRealizacionVO.setColectivo(rs.getInt("COLEC_COMPREAL_COLECTIVO"));
        if(rs.wasNull())
        {
            colecComproRealizacionVO.setColectivo(null);
        }
        colecComproRealizacionVO.setTerritorioHistorico(rs.getInt("COLEC_COMPREAL_TTHH"));
        if(rs.wasNull())
        {
            colecComproRealizacionVO.setTerritorioHistorico(null);
        }
        colecComproRealizacionVO.setPorcentajeCompReal(rs.getDouble("COLEC_COMPREAL_PORCENT"));
        if(rs.wasNull())
        {
            colecComproRealizacionVO.setPorcentajeCompReal(null);
        }
        // campos adicionales para mostrar datos en tabla, como son de otra tabla comprobamos que exista la columna
        if(existeColumnaEnResultSet("COLEC_ENT_NOMBRE", rs)){
            colecComproRealizacionVO.setNombreEntidad(rs.getString("COLEC_ENT_NOMBRE"));
        }
        return colecComproRealizacionVO;
    }

    private Object mapearColecTrayectoriaEntidad(ResultSet rs) throws SQLException {
         if(rs!=null)
             return new ColecTrayectoriaEntidad(
                rs.getInt("id"),
                rs.getInt("trayCodColectivo"),
                rs.getInt("trayIdFkProgConvActGrupo"),
                rs.getInt("trayIdFkProgConvActSubGrPre"),
                rs.getString("trayNumExpediente"),
                rs.getInt("trayCodigoEntidad"),
                rs.getString("trayDescripcion"),
                rs.getInt("trayTieneExperiencia"),
                rs.getString("trayNombreAdmonPublica"),
                rs.getDate("trayFechaInicio"),
                rs.getDate("trayFechaFin"),
                rs.getDouble("trayNumeroMeses"),
                rs.getDate("trayFechaAlta"),
                rs.getDate("trayFechaModificacion")
            );
         else
            return null;
    }
    
    private Object mapearColecUbicCTValoracion(ResultSet rs) throws SQLException {
         if(rs!=null)
             return new ColecUbicCTValoracion(
                rs.getInt("id"),
                rs.getString("numeroExpediente"),
                rs.getInt("idFkUbicacion"),
                rs.getInt("validarSegundosLocales"),
                rs.getInt("validarPlanIgualdad"),
                rs.getInt("validarCertificadoCalidad"),
                rs.getInt("validarEspacioComplem"),
                rs.getInt("validarCentroEspEmpleo"),
                rs.getInt("validarEmpresaInsercion"),
                rs.getInt("validarPromoEmpInsercion"),
                rs.getDouble("puntuacionTrayectoriaEntidad"),
                rs.getDouble("puntuacionUbicacionCT"),
                rs.getDouble("puntuacionSegundosLocales"),
                rs.getDouble("puntuacionPlanIgualdad"),
                rs.getDouble("puntuacionCertificadoCalidad"),
                rs.getDouble("puntuacionEspacioComplem"),
                rs.getDouble("puntuacionCentroEspEmpleo"),
                rs.getDouble("puntuacionEmpOpromEmpInsercion"),
                rs.getString("puntuacionObservaciones")
            );
         else
            return null;
    }

    private Object mapearColecTrayeEntiValida(ResultSet rs) throws SQLException {
        if(rs!=null)
        return new ColecTrayeEntiValida(
                rs.getInt("id"),
                rs.getString("numeroExpediente"),
                rs.getInt("idFkEntidad"),
                rs.getInt("idFkColectivo"),
                rs.getDouble("numeroMesesValidados")
        );
        else
            return null;
    }

    private Object mapearColecColectivo(ResultSet rs) throws SQLException {
        if(rs!=null)
        return new ColecColectivo(
                rs.getInt("id"),
                rs.getString("descripcion"),
                rs.getString("descripcioneu")
        );
        else
            return null;
    }

    private Object mapearColecAmbitosBloquesHoras(ResultSet rs) throws SQLException {
        if (rs != null) {
            return new ColecAmbitosBloquesHoras(
                    rs.getInt("id"),
                    rs.getInt("colectivo"),
                    rs.getInt("idfkconvocatoriaactiva"),
                    rs.getString("ambitodescripcion"),
                    rs.getString("ambitodescripcioneu"),
                    rs.getDouble("numerobloqueshoras"),
                    rs.getInt("numerocentrosminimos"),
                    rs.getBoolean("ubic_imprescindibles")
            );
        } else {
            return null;
        }
    }

    private Object mapearColecCertCalidad(ResultSet rs) throws SQLException {
        if (rs != null) {
            return new ColecCertCalidad(
                    rs.getString("codigo"),
                    rs.getString("descripcion"),
                    rs.getString("descripcioneu")
            );
        } else {
            return null;
        }
    }

    private Object mapearColecCompIgualdad(ResultSet rs) throws SQLException {
        if (rs != null) {
            return new ColecCompIgualdad(
                    rs.getString("codigo"),
                    rs.getString("descripcion"),
                    rs.getString("descripcioneu")
            );
        } else {
            return null;
        }
    }

    private Object mapearColecEntidadCertCalidad(ResultSet rs) throws SQLException {
        if (rs != null) {
            return new ColecEntidadCertCalidad(
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
    private Object mapearColecCertCalidadPuntuacion(ResultSet rs) throws SQLException {
        if (rs != null) {
            return new ColecCertCalidadPuntuacion(
                    rs.getInt("ID_CONVOCATORIA"),
                    rs.getString("CODIGO"),
                    rs.getDouble("PUNTUACION")
            );
        } else {
            return null;
        }
    }
    private Object mapearColecCompIgualdadPuntuacion(ResultSet rs) throws SQLException {
        if (rs != null) {
            return new ColecCompIgualdadPuntuacion(
                    rs.getInt("ID_CONVOCATORIA"),
                    rs.getString("CODIGO"),
                    rs.getDouble("PUNTUACION")
            );
        } else {
            return null;
        }
    }

    public ColecEntidadVO mapearDatosEntidadAsociadaEntidadPadre(ColecEntidadEnAgrupacionVO entidad) {
        return entidad!= null ? new ColecEntidadVO(
                entidad.getCodEntidad()
                ,MeLanbide48Utils.getEjercicioDeExpediente(entidad.getNumExp())
                ,entidad.getNumExp()
                ,MeLanbide48Utils.getEjercicioDeExpediente(entidad.getNumExp())
                ,null
                , entidad.getTipoCif()
                ,entidad.getCif()
                ,entidad.getNombre()
                ,entidad.getCentroEspEmpTH()
                ,entidad.getParticipanteMayorCentEcpEmpTH()
                ,entidad.getEmpresaInsercionTH()
                ,entidad.getPromotorEmpInsercionTH()
                ,null
                ,entidad.getPlanIgualdad()
                ,entidad.getCertificadoCalidad()
                ,entidad.getAceptaNumeroSuperiorHoras()
                ,entidad.getSegundosLocalesMismoAmbito()
                ,null
                ,null
                ,null
                ,null
                ,null
                ,null
                ,null
                ,null
                ,null
                ,entidad.getEntSujetaDerPubl()
                ,null
                ,entidad.getCompIgualdadOpcion()
                ,entidad.getCompIgualdadOpcionLiteral()
                ,null
                ,null
                ,entidad.getEntSinAnimoLucro()
                ,null
        ) : null;
    }
}
