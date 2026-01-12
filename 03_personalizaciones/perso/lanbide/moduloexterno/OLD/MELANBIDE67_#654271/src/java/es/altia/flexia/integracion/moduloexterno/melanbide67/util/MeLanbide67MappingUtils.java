/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide67.util;

import es.altia.flexia.integracion.moduloexterno.melanbide67.vo.LeaukPuestoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide67.vo.FilaPuestoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide67.vo.CentroColVO;
import es.altia.flexia.integracion.moduloexterno.melanbide67.vo.DesplegableVO;
import es.altia.flexia.integracion.moduloexterno.melanbide67.vo.FilaCentroColVO;
import es.altia.flexia.integracion.moduloexterno.melanbide67.vo.SelectItem;
import es.altia.flexia.integracion.moduloexterno.melanbide67.vo.SubSolicVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.ValorCampoDesplegableModuloIntegracionVO;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.text.SimpleDateFormat;

/**
 *
 * @author santiagoc
 */
public class MeLanbide67MappingUtils {

    private static MeLanbide67MappingUtils instance = null;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat(ConstantesMeLanbide67.FORMATO_FECHA);

    private MeLanbide67MappingUtils() {

    }

    public static MeLanbide67MappingUtils getInstance() {
        if (instance == null) {
            synchronized (MeLanbide67MappingUtils.class) {
                instance = new MeLanbide67MappingUtils();
            }
        }
        return instance;
    }

    public Object map(ResultSet rs, Class clazz) throws Exception {
        if (clazz == LeaukPuestoVO.class) {
            return mapearCpePuestoVO(rs);
        } else if (clazz == FilaPuestoVO.class) {
            return mapearFilaPuestoVO(rs);
        } else if (clazz == CentroColVO.class) {
            return mapearCentroColVO(rs);
        } else if (clazz == FilaCentroColVO.class) {
            return mapearFilaCentroColVO(rs);
        } else if (clazz == ValorCampoDesplegableModuloIntegracionVO.class) {
            return mapearValorCampoDesplegableModuloIntegracionVO(rs);
        } else if (clazz == SubSolicVO.class) {
            return mapeaSubSolicVO(rs);
        } else if (clazz == DesplegableVO.class) {
            return mapearDesplegableVO(rs);
        }
        return null;
    }

    public List<SelectItem> fromCampoDesplegableToSelectItemVO(List<ValorCampoDesplegableModuloIntegracionVO> fromList) {
        List<SelectItem> toList = new ArrayList<SelectItem>();
        if (fromList != null && fromList.size() > 0) {
            SelectItem si = null;
            for (ValorCampoDesplegableModuloIntegracionVO v : fromList) {
                si = new SelectItem();
                si.setCodigo(v.getCodigo());
                si.setDescripcion(v.getDescripcion() != null ? v.getDescripcion().toUpperCase() : v.getCodigo());
                toList.add(si);
            }
        }
        Collections.sort(toList);
        return toList;
    }

    public List<SelectItem> fromCampoDesplegableToSelectItemVO(List<ValorCampoDesplegableModuloIntegracionVO> fromList, int sortMode) {
        List<SelectItem> toList = new ArrayList<SelectItem>();
        if (fromList != null && fromList.size() > 0) {
            SelectItem si = null;
            for (ValorCampoDesplegableModuloIntegracionVO v : fromList) {
                si = new SelectItem();
                si.setCodigo(v.getCodigo());
                si.setDescripcion(v.getDescripcion() != null ? v.getDescripcion().toUpperCase() : v.getCodigo());
                si.setModoOrdenacion(sortMode);
                toList.add(si);
            }
        }
        Collections.sort(toList);
        return toList;
    }

    private LeaukPuestoVO mapearCpePuestoVO(ResultSet rs) throws Exception {
        LeaukPuestoVO puesto = new LeaukPuestoVO();
        puesto.setIdPuesto(rs.getInt("ID"));
        puesto.setCodPuesto(rs.getString("COD_PUESTO"));
        puesto.setDescPuesto(rs.getString("NOMBRE_PUESTO_SOL"));
        puesto.setNumExp(rs.getString("NUM_EXP"));
        puesto.setApellido1(rs.getString("APELLIDO1"));
        puesto.setApellido2(rs.getString("APELLIDO2"));
        puesto.setNombre(rs.getString("NOMBRE"));
        puesto.setImpSubvSol(rs.getBigDecimal("IMPORTE_SUBV_SOL"));
        puesto.setImpSubvEst(rs.getBigDecimal("IMPORTE_SUBV_EST"));
        puesto.setImpSubvOfe(rs.getBigDecimal("IMPORTE_SUBV_OFE"));
        puesto.setReintegros(rs.getBigDecimal("REINTEGROS"));
        puesto.setImpTotal(rs.getBigDecimal("IMPORTE_TOTAL"));
        puesto.setSalarioSol(rs.getBigDecimal("SALARIO_SOL"));
        puesto.setSalarioOfe(rs.getBigDecimal("SALARIO_OFE"));
        puesto.setSexoSol(rs.getInt("SEXO_SOL"));
        puesto.setSexoOfe(rs.getInt("SEXO_OFE"));
        puesto.setDptoSol(rs.getString("DPTO_SOL"));
        puesto.setDptoOfe(rs.getString("DPTO_OFE"));
        puesto.setCodTitulacionSol(rs.getString("TITULACION_SOL"));
        puesto.setCodTitulacionOfe(rs.getString("TITULACION_OFE"));
        puesto.setCodModSol(rs.getString("MODALIDAD_SOL"));
        puesto.setCodModOfe(rs.getString("MODALIDAD_OFE"));
        puesto.setJornadaLabSol(rs.getBigDecimal("JORNADA_LAB_SOL"));
        puesto.setJornadaLabOfe(rs.getBigDecimal("JORNADA_LAB_OFE"));
        puesto.setCentroTrabSol(rs.getString("CENTRO_TRAB_SOL"));
        puesto.setCentroTrabOfe(rs.getString("CENTRO_TRAB_OFE"));
        puesto.setCtaCotizSol(rs.getString("CTA_COTIZ_SOL"));
        puesto.setCtaCotizOfe(rs.getString("CTA_COTIZ_OFE"));
        puesto.setFecIniSol(rs.getDate("FEC_INI_CONTR_SOL"));
        puesto.setFecFinSol(rs.getDate("FEC_FIN_CONTR_SOL"));
        puesto.setFecIniOfe(rs.getDate("FEC_INI_CONTR_OFE"));
        puesto.setFecFinOfe(rs.getDate("FEC_FIN_CONTR_OFE"));
        puesto.setGrupoCotizSol(rs.getString("GRUPO_COTIZ_SOL"));
        puesto.setGrupoCotizOfe(rs.getString("GRUPO_COTIZ_OFE"));
        puesto.setConvenioColSol(rs.getString("CONVENIO_COL_SOL"));
        puesto.setConvenioColOfe(rs.getString("CONVENIO_COL_OFE"));
        puesto.setObservaciones(rs.getString("OBSERVACIONES"));
        puesto.setCodTipoNif(rs.getString("COD_TIPO_NIF"));
        puesto.setNif(rs.getString("DNI_TRAB"));
        puesto.setFecNacimiento(rs.getDate("FEC_NACIMIENTO"));
        puesto.setNum_oferta(rs.getString("NUM_OFERTA"));
        puesto.setCentroColGestion(rs.getString("CENTRO_COL_GESTION"));
        puesto.setCentroColCaptacion(rs.getString("CENTRO_COL_CAPTACION"));
        puesto.setImpPago1(rs.getBigDecimal("IMP_PAGO_1"));
        puesto.setImpPago2(rs.getBigDecimal("IMP_PAGO_2"));
        /*puesto.setEjercicio(rs.getInt("NUM_EXP"));
        if(rs.wasNull())
        {
            puesto.setEjercicio(null);
        }*/

        return puesto;
    }

    private CentroColVO mapearCentroColVO(ResultSet rs) throws Exception {
        CentroColVO centroCol = new CentroColVO();
        centroCol.setIdCentroCol(rs.getInt("ID"));
        centroCol.setOfertaEmpleo(rs.getString("OFERTA_EMPLEO"));
        centroCol.setNumExp(rs.getString("NUM_EXP"));
        centroCol.setNumOfertaSol(rs.getInt("NUM_OFERTAS_SOL"));
        centroCol.setNumOfertaOfe(rs.getInt("NUM_OFERTAS_CON"));
        centroCol.setNumConSubvSol(rs.getInt("NUM_CONTR_SOL"));
        centroCol.setNumConSubvOfe(rs.getInt("NUM_CONTR_CON"));
        centroCol.setSubvencion(rs.getInt("IMPORTE_SUBV"));
        centroCol.setImpSubvSol(rs.getBigDecimal("IMPORTE_SUBV_SOL"));
        centroCol.setImpSubvOfe(rs.getBigDecimal("IMPORTE_SUBV_CON"));
        centroCol.setObservaciones(rs.getString("OBSERVACIONES"));

        return centroCol;
    }

    private ValorCampoDesplegableModuloIntegracionVO mapearValorCampoDesplegableModuloIntegracionVO(ResultSet rs) throws Exception {
        ValorCampoDesplegableModuloIntegracionVO campo = new ValorCampoDesplegableModuloIntegracionVO();
        campo.setCodigo(rs.getString("CODIGO"));
        campo.setDescripcion(rs.getString("DESCRIPCION"));
        return campo;
    }

    private FilaPuestoVO mapearFilaPuestoVO(ResultSet rs) throws Exception {
        FilaPuestoVO fila = new FilaPuestoVO();
        DecimalFormatSymbols simbolo = new DecimalFormatSymbols();
        simbolo.setDecimalSeparator(',');
        simbolo.setGroupingSeparator('.');
        DecimalFormat formateador = new DecimalFormat("#,##0.00", simbolo);

        fila.setIdPuesto(rs.getInt("ID"));
        fila.setCodPuesto(rs.getString("COD_PUESTO"));
        fila.setDescPuesto(rs.getString("COD_PUESTO") + (rs.getString("NOMBRE_PUESTO_SOL") != null && !rs.getString("NOMBRE_PUESTO_SOL").equals("") && !rs.getString("NOMBRE_PUESTO_SOL").equals("") ? " - " + rs.getString("NOMBRE_PUESTO_SOL").toUpperCase() : ""));
        fila.setApellido1(rs.getString("APELLIDO1") != null && !rs.getString("APELLIDO1").equals("") ? rs.getString("APELLIDO1").toUpperCase() : "-");
        fila.setApellido2(rs.getString("APELLIDO2") != null && !rs.getString("APELLIDO2").equals("") ? rs.getString("APELLIDO2").toUpperCase() : "-");
        //fila.setNombre(rs.getString("NOMBRE") != null && !rs.getString("NOMBRE").equals("") ? rs.getString("NOMBRE").toUpperCase() : "-");
        fila.setNombre((rs.getString("APELLIDO1") != null ? " " + rs.getString("APELLIDO1").toUpperCase() : "-") + " " + (rs.getString("APELLIDO2") != null ? " " + rs.getString("APELLIDO2").toUpperCase() : "-") + (rs.getString("NOMBRE") != null ? " , " + rs.getString("NOMBRE").toUpperCase() : ", -"));
        fila.setImpSubvSol(rs.getBigDecimal("IMPORTE_SUBV_SOL") != null ? formateador.format(new BigDecimal(MeLanbide67Utils.redondearDecimalesString(rs.getBigDecimal("IMPORTE_SUBV_SOL"), 2))) : "0,00");
        fila.setImpSubvEst(rs.getBigDecimal("IMPORTE_SUBV_EST") != null ? formateador.format(new BigDecimal(MeLanbide67Utils.redondearDecimalesString(rs.getBigDecimal("IMPORTE_SUBV_EST"), 2))) : "0,00");
        fila.setImpSubvOfe(rs.getBigDecimal("IMPORTE_SUBV_OFE") != null ? formateador.format(new BigDecimal(MeLanbide67Utils.redondearDecimalesString(rs.getBigDecimal("IMPORTE_SUBV_OFE"), 2))) : "0,00");
        fila.setReintegros(rs.getBigDecimal("REINTEGROS") != null ? formateador.format(new BigDecimal(MeLanbide67Utils.redondearDecimalesString(rs.getBigDecimal("REINTEGROS"), 2))) : "0,00");
        fila.setImpTotal(rs.getBigDecimal("IMPORTE_TOTAL") != null ? formateador.format(new BigDecimal(MeLanbide67Utils.redondearDecimalesString(rs.getBigDecimal("IMPORTE_TOTAL"), 2))) : "0,00");
        fila.setSalarioSol(rs.getBigDecimal("SALARIO_SOL") != null ? formateador.format(new BigDecimal(MeLanbide67Utils.redondearDecimalesString(rs.getBigDecimal("SALARIO_SOL"), 2))) : "0,00");
        fila.setSalarioOfe(rs.getBigDecimal("SALARIO_OFE") != null ? formateador.format(new BigDecimal(MeLanbide67Utils.redondearDecimalesString(rs.getBigDecimal("SALARIO_OFE"), 2))) : "0,00");
        fila.setSexoSol(rs.getBigDecimal("SEXO_SOL") != null ? MeLanbide67Utils.redondearDecimalesString(rs.getBigDecimal("SEXO_SOL"), 2).replaceAll("\\.", ",") : "-");
        fila.setSexoOfe(rs.getBigDecimal("SEXO_OFE") != null ? MeLanbide67Utils.redondearDecimalesString(rs.getBigDecimal("SEXO_OFE"), 2).replaceAll("\\.", ",") : "-");
        fila.setDptoSol(rs.getString("DPTO_SOL") != null && !rs.getString("DPTO_SOL").equals("") ? rs.getString("DPTO_SOL").toUpperCase() : "-");
        fila.setDptoOfe(rs.getString("DPTO_OFE") != null && !rs.getString("DPTO_OFE").equals("") ? rs.getString("DPTO_OFE").toUpperCase() : "-");
        fila.setCodTitulacionSol(rs.getString("TITULACION_SOL") != null && !rs.getString("TITULACION_SOL").equals("") ? rs.getString("TITULACION_SOL").toUpperCase() : "-");
        fila.setCodTitulacionOfe(rs.getString("TITULACION_OFE") != null && !rs.getString("TITULACION_OFE").equals("") ? rs.getString("TITULACION_OFE").toUpperCase() : "-");
        fila.setCodModSol(rs.getString("MODALIDAD_SOL") != null && !rs.getString("MODALIDAD_SOL").equals("") ? rs.getString("MODALIDAD_SOL").toUpperCase() : "-");
        fila.setCodModOfe(rs.getString("MODALIDAD_OFE") != null && !rs.getString("MODALIDAD_OFE").equals("") ? rs.getString("MODALIDAD_OFE").toUpperCase() : "-");
        fila.setJornadaLabSol(rs.getBigDecimal("JORNADA_LAB_SOL") != null ? MeLanbide67Utils.redondearDecimalesString(rs.getBigDecimal("JORNADA_LAB_SOL"), 2).replaceAll("\\.", ",") : "0,00");
        fila.setJornadaLabOfe(rs.getBigDecimal("JORNADA_LAB_OFE") != null ? MeLanbide67Utils.redondearDecimalesString(rs.getBigDecimal("JORNADA_LAB_OFE"), 2).replaceAll("\\.", ",") : "0,00");
        fila.setCentroTrabSol(rs.getString("CENTRO_TRAB_SOL") != null && !rs.getString("CENTRO_TRAB_SOL").equals("") ? rs.getString("CENTRO_TRAB_SOL").toUpperCase() : "-");
        fila.setCentroTrabOfe(rs.getString("CENTRO_TRAB_OFE") != null && !rs.getString("CENTRO_TRAB_OFE").equals("") ? rs.getString("CENTRO_TRAB_OFE").toUpperCase() : "-");
        fila.setCtaCotizSol(rs.getString("CTA_COTIZ_SOL") != null && !rs.getString("CTA_COTIZ_SOL").equals("") ? rs.getString("CTA_COTIZ_SOL").toUpperCase() : "-");
        fila.setCtaCotizOfe(rs.getString("CTA_COTIZ_OFE") != null && !rs.getString("CTA_COTIZ_OFE").equals("") ? rs.getString("CTA_COTIZ_OFE").toUpperCase() : "-");
        fila.setFecIniSol(rs.getDate("FEC_INI_CONTR_SOL") != null ? dateFormat.format(rs.getDate("FEC_INI_CONTR_SOL")) : "-");
        fila.setFecFinSol(rs.getDate("FEC_FIN_CONTR_SOL") != null ? dateFormat.format(rs.getDate("FEC_FIN_CONTR_SOL")) : "-");
        fila.setFecIniOfe(rs.getDate("FEC_INI_CONTR_OFE") != null ? dateFormat.format(rs.getDate("FEC_INI_CONTR_OFE")) : "-");
        fila.setFecFinOfe(rs.getDate("FEC_FIN_CONTR_OFE") != null ? dateFormat.format(rs.getDate("FEC_FIN_CONTR_OFE")) : "-");
        fila.setGrupoCotizSol(rs.getString("GRUPO_COTIZ_SOL") != null && !rs.getString("GRUPO_COTIZ_SOL").equals("") ? rs.getString("GRUPO_COTIZ_SOL").toUpperCase() : "-");
        fila.setGrupoCotizOfe(rs.getString("GRUPO_COTIZ_OFE") != null && !rs.getString("GRUPO_COTIZ_OFE").equals("") ? rs.getString("GRUPO_COTIZ_OFE").toUpperCase() : "-");
        fila.setConvenioColSol(rs.getString("CONVENIO_COL_SOL") != null && !rs.getString("CONVENIO_COL_SOL").equals("") ? rs.getString("CONVENIO_COL_SOL").toUpperCase() : "-");
        fila.setConvenioColOfe(rs.getString("CONVENIO_COL_OFE") != null && !rs.getString("CONVENIO_COL_OFE").equals("") ? rs.getString("CONVENIO_COL_OFE").toUpperCase() : "-");
        fila.setObservaciones(rs.getString("OBSERVACIONES") != null && !rs.getString("OBSERVACIONES").equals("") ? rs.getString("OBSERVACIONES").toUpperCase() : "-");
        fila.setDni(rs.getString("DNI_TRAB") != null && !rs.getString("DNI_TRAB").equals("") ? rs.getString("DNI_TRAB").toUpperCase() : "-");
        fila.setFecNacimiento(rs.getDate("FEC_NACIMIENTO") != null ? dateFormat.format(rs.getDate("FEC_NACIMIENTO")) : "-");
        fila.setNum_oferta(rs.getString("NUM_OFERTA") != null && !rs.getString("NUM_OFERTA").equals("") ? rs.getString("NUM_OFERTA").toUpperCase() : "-");
        fila.setCentroColGestion(rs.getString("CENTRO_COL_GESTION") != null && !rs.getString("CENTRO_COL_GESTION").equals("") ? rs.getString("CENTRO_COL_GESTION").toUpperCase() : "-");
        fila.setCentroColCaptacion(rs.getString("CENTRO_COL_CAPTACION") != null && !rs.getString("CENTRO_COL_CAPTACION").equals("") ? rs.getString("CENTRO_COL_CAPTACION").toUpperCase() : "-");
        fila.setImpPago1(rs.getBigDecimal("IMP_PAGO_1") != null ? formateador.format(new BigDecimal(MeLanbide67Utils.redondearDecimalesString(rs.getBigDecimal("IMP_PAGO_1"), 2))) : "-");
        fila.setImpPago2(rs.getBigDecimal("IMP_PAGO_2") != null ? formateador.format(new BigDecimal(MeLanbide67Utils.redondearDecimalesString(rs.getBigDecimal("IMP_PAGO_2"), 2))) : "-");

        return fila;
    }

    private FilaCentroColVO mapearFilaCentroColVO(ResultSet rs) throws Exception {
        FilaCentroColVO fila = new FilaCentroColVO();

        fila.setIdCentroCol(rs.getInt("ID"));
        fila.setOfertaEmpleo(rs.getString("OFERTA_EMPLEO"));
        fila.setNumExp(rs.getString("NUM_EXP"));
        fila.setNumOfertaSol(rs.getInt("NUM_OFERTAS_SOL"));
        fila.setNumOfertaOfe(rs.getInt("NUM_OFERTAS_CON"));
        fila.setNumConSubvSol(rs.getInt("NUM_CONTR_SOL"));
        fila.setNumConSubvOfe(rs.getInt("NUM_CONTR_CON"));
        fila.setSubvencion(rs.getInt("IMPORTE_SUBV"));
        fila.setImpSubvSol(rs.getString("IMPORTE_SUBV_SOL"));
        fila.setImpSubvOfe(rs.getString("IMPORTE_SUBV_CON"));
        fila.setObservaciones(rs.getString("OBSERVACIONES"));
        //fila.setObservaciones(rs.getString("OBSERVACIONES") != null && !rs.getString("OBSERVACIONES").equals("") ? rs.getString("OBSERVACIONES").replaceAll("\r\n", "<br/>").replaceAll("\n", "<br/>").replaceAll("\r", "<br/>").toUpperCase() : "-");
        /*try
        {
            String str = fila.getObservaciones();
            str = str.replaceAll(System.getProperty("line.separator"), "<br />");
            fila.setObservaciones(str);
        }
        catch(Exception ex)
        {
            
        }*/
        return fila;
    }

    private Object mapeaSubSolicVO(ResultSet rs) throws SQLException {
        SubSolicVO subSolic = new SubSolicVO();
        subSolic.setId(rs.getInt("ID"));
        if (rs.wasNull()) {
            subSolic.setId(null);
        }
        subSolic.setNumExp(rs.getString("NUM_EXP"));
        subSolic.setEstado(rs.getString("ESTADO"));
        subSolic.setOrganismo(rs.getString("ORGANISMO"));
        subSolic.setObjeto(rs.getString("OBJETO"));
        Double aux1 = rs.getDouble("IMPORTE");
        if (aux1 != 0) {
            subSolic.setImporte(aux1);
        }
        if (rs.getDate("FECHA") != null) {
            subSolic.setFecha(rs.getDate("FECHA"));
            subSolic.setFechaStr(dateFormat.format(rs.getDate("FECHA")));
        }

        return subSolic;
    }

    private Object mapearDesplegableVO(ResultSet rs) throws SQLException {
        DesplegableVO valoresDesplegable = new DesplegableVO();
        valoresDesplegable.setDes_cod(rs.getString("DES_COD"));
        valoresDesplegable.setDes_val_cod(rs.getString("DES_VAL_COD"));
        valoresDesplegable.setDes_nom(rs.getString("DES_NOM"));
        return valoresDesplegable;
    }

}
