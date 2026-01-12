/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide60.util;

import es.altia.flexia.integracion.moduloexterno.melanbide60.vo.CmeJustificacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide60.vo.CmeOfertaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide60.vo.CmePuestoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide60.vo.FilaInformeDatosPuestosVO;
import es.altia.flexia.integracion.moduloexterno.melanbide60.vo.FilaInformeResumenEconomicoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide60.vo.FilaInformeResumenPuestosContratadosVO;
import es.altia.flexia.integracion.moduloexterno.melanbide60.vo.FilaJustificacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide60.vo.FilaOfertaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide60.vo.FilaPersonaContratadaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide60.vo.FilaPuestoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide60.vo.FilaResultadoBusqTitulaciones;
import es.altia.flexia.integracion.moduloexterno.melanbide60.vo.FilaResumenVO;
import es.altia.flexia.integracion.moduloexterno.melanbide60.vo.SalarioVO;
import es.altia.flexia.integracion.moduloexterno.melanbide60.vo.SelectItem;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.ValorCampoDesplegableModuloIntegracionVO;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author santiagoc
 */
public class MeLanbide60MappingUtils 
{
    
    private static MeLanbide60MappingUtils instance = null;
    
    private MeLanbide60MappingUtils()
    {
        
    }
    
    public static MeLanbide60MappingUtils getInstance()
    {
        if(instance == null)
        {
            synchronized(MeLanbide60MappingUtils.class)
            {
                instance = new MeLanbide60MappingUtils();
            }
        }
        return instance;
    }
    
    public Object map(ResultSet rs, Class clazz) throws Exception
    {
        if(clazz == CmePuestoVO.class)
        {
            return mapearCmePuestoVO(rs);
        }
        else if(clazz == FilaPuestoVO.class)
        {
            return mapearFilaPuestoVO(rs);
        }
        else if(clazz == ValorCampoDesplegableModuloIntegracionVO.class)
        {
            return mapearValorCampoDesplegableModuloIntegracionVO(rs);
        }
        else if(clazz == SalarioVO.class)
        {
            return mapearSalarioVO(rs);
        }
        else if(clazz == CmeOfertaVO.class)
        {
            return mapearCmeOfertaVO(rs);
        }
        else if(clazz == CmeJustificacionVO.class)
        {
            return mapearCmeJustificacionVO(rs);
        }
        else if(clazz == CmeJustificacionVO.class)
        {
            return mapearCmeJustificacionVO(rs);
        }
        else if(clazz == FilaOfertaVO.class)
        {
            return mapearFilaOfertaVO(rs);
        }
        else if(clazz == FilaJustificacionVO.class)
        {
            return mapearFilaJustificacionVO(rs);
        }
        else if(clazz == FilaPersonaContratadaVO.class)
        {
            return mapearFilaPersonaContratadaVO(rs);
        }
        else if(clazz == FilaInformeResumenPuestosContratadosVO.class)
        {
            return mapearFilaInformeResumenPuestosContratadosVO(rs);
        }
        else if(clazz == FilaInformeResumenEconomicoVO.class)
        {
            return mapearFilaInformeResumenEconomicoVO(rs);
        }
        else if(clazz == FilaResultadoBusqTitulaciones.class)
        {
            return mapearFilaResultadoBusqTitulaciones(rs);
        }
        else if(clazz == FilaResumenVO.class)
        {
            return mapearFilaResumenVO(rs);
        }
        else if(clazz == FilaInformeDatosPuestosVO.class)
        {
            return mapearFilaInformeDatosPuestosVO(rs);
        }
        return null;
    }
    
    public List<SelectItem> fromCampoDesplegableToSelectItemVO(List<ValorCampoDesplegableModuloIntegracionVO> fromList)
    {
        List<SelectItem> toList = new ArrayList<SelectItem>();
        if(fromList != null && fromList.size() > 0)
        {
            SelectItem si = null;
            for(ValorCampoDesplegableModuloIntegracionVO v : fromList)
            {
                si = new SelectItem();
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
            SelectItem si = null;
            for(ValorCampoDesplegableModuloIntegracionVO v : fromList)
            {
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
    
    private CmePuestoVO mapearCmePuestoVO(ResultSet rs) throws Exception
    {
        CmePuestoVO puesto = new CmePuestoVO();
        puesto.setCiudadDestino(rs.getString("CME_PTO_CIUDAD_DESTINO"));
        puesto.setCodGrCot(rs.getString("CME_PTO_COD_GR_COT"));
        puesto.setCodIdioma1(rs.getString("CME_PTO_COD_IDIOMA_1"));
        puesto.setCodIdioma2(rs.getString("CME_PTO_COD_IDIOMA_2"));
        puesto.setCodIdioma3(rs.getString("CME_PTO_COD_IDIOMA_3"));
        puesto.setCodMotivo(rs.getString("CME_PTO_COD_MOTIVO"));
        puesto.setCodNivForm(rs.getString("CME_PTO_COD_NIV_FORM"));
        puesto.setCodNivIdi1(rs.getString("CME_PTO_COD_NIV_IDI_1"));
        puesto.setCodNivIdi2(rs.getString("CME_PTO_COD_NIV_IDI_2"));
        puesto.setCodNivIdi3(rs.getString("CME_PTO_COD_NIV_IDI_3"));
        puesto.setCodPuesto(rs.getString("CME_PTO_COD_PUESTO"));
        puesto.setCodResult(rs.getString("CME_PTO_COD_RESULT"));
        puesto.setCodTit1(rs.getString("CME_PTO_COD_TIT_1"));
        puesto.setCodTit2(rs.getString("CME_PTO_COD_TIT_2"));
        puesto.setCodTit3(rs.getString("CME_PTO_COD_TIT_3"));
        puesto.setConvenioCol(rs.getString("CME_PTO_CONVENIO_COL"));
        puesto.setCosteCont(MeLanbide60Utils.redondearDecimalesBigDecimal(rs.getBigDecimal("CME_PTO_COSTE_CONT"), 2));
        puesto.setDescPuesto(rs.getString("CME_PTO_DESC_PUESTO"));
        puesto.setDietasConv(MeLanbide60Utils.redondearDecimalesBigDecimal(rs.getBigDecimal("CME_PTO_DIETAS_CONV"), 2));
        //puesto.setDietasManut(rs.getBigDecimal("CME_PTO_DIETAS_MANUT"));
        puesto.setDpto(rs.getString("CME_PTO_DPTO"));
        puesto.setFecFin(rs.getDate("CME_PTO_FEC_FIN"));
        puesto.setFecIni(rs.getDate("CME_PTO_FEC_INI"));
        puesto.setFunciones(rs.getString("CME_PTO_FUNCIONES"));
        puesto.setImpMaxSuv(MeLanbide60Utils.redondearDecimalesBigDecimal(rs.getBigDecimal("CME_PTO_IMP_MAX_SUBV"), 2));
        puesto.setMesesManut(rs.getInt("CME_PTO_MESES_MANUT"));
        if(rs.wasNull())
        {
            puesto.setMesesManut(null);
        }
        puesto.setObservaciones(rs.getString("CME_PTO_OBSERVACIONES"));
        puesto.setOtrosBenef(rs.getString("CME_PTO_OTROS_BENEF"));
        puesto.setPaiCod1(rs.getInt("CME_PTO_PAI_COD_1"));
        if(rs.wasNull())
        {
            puesto.setPaiCod1(null);
        }
        puesto.setPaiCod2(rs.getInt("CME_PTO_PAI_COD_2"));
        if(rs.wasNull())
        {
            puesto.setPaiCod2(null);
        }
        puesto.setPaiCod3(rs.getInt("CME_PTO_PAI_COD_3"));
        if(rs.wasNull())
        {
            puesto.setPaiCod3(null);
        }
        puesto.setRangoEdadPrevisto(rs.getInt("CME_PTO_RANGO_EDAD"));
        puesto.setSalBruto(MeLanbide60Utils.redondearDecimalesBigDecimal(rs.getBigDecimal("CME_PTO_SAL_BRUTO"), 2));
        puesto.setSalarioAnex1(MeLanbide60Utils.redondearDecimalesBigDecimal(rs.getBigDecimal("CME_PTO_SALARIO_ANEX1"), 2));
        puesto.setSalarioAnex2(MeLanbide60Utils.redondearDecimalesBigDecimal(rs.getBigDecimal("CME_PTO_SALARIO_ANEX2"), 2));
        puesto.setSalarioAnex3(MeLanbide60Utils.redondearDecimalesBigDecimal(rs.getBigDecimal("CME_PTO_SALARIO_ANEX3"), 2));
        //puesto.setTramSeguros(MeLanbide60Utils.redondearDecimalesBigDecimal(rs.getBigDecimal("CME_PTO_TRAM_SEGUROS"), 2));
        puesto.setNumExp(rs.getString("CME_PTO_NUMEXP"));
        puesto.setEjercicio(rs.getInt("CME_PTO_EXP_EJE"));
        if(rs.wasNull())
        {
            puesto.setEjercicio(null);
        }
        puesto.setSalSolic(MeLanbide60Utils.redondearDecimalesBigDecimal(rs.getBigDecimal("CME_PTO_SAL_SOLIC"), 2));
        puesto.setDietasSolic(MeLanbide60Utils.redondearDecimalesBigDecimal(rs.getBigDecimal("CME_PTO_DIETAS_SOLIC"), 2));
        puesto.setTramSolic(MeLanbide60Utils.redondearDecimalesBigDecimal(rs.getBigDecimal("CME_PTO_TRAM_SOLIC"), 2));
        puesto.setImpTotSolic(MeLanbide60Utils.redondearDecimalesBigDecimal(rs.getBigDecimal("CME_PTO_IMP_TOT_SOLIC"), 2));
        puesto.setSalarioAnexTot(MeLanbide60Utils.redondearDecimalesBigDecimal(rs.getBigDecimal("CME_PTO_SALARIO_ANEX_TOT"), 2));
        puesto.setMesesContrato(rs.getInt("CME_PTO_MESES_CONTRATO"));
        if(rs.wasNull())
        { 
            puesto.setMesesContrato(null);
        }
        puesto.setGastosSeguro(MeLanbide60Utils.redondearDecimalesBigDecimal(rs.getBigDecimal("CME_PTO_GASTOS_SEG"),2));
        puesto.setGastosVisado(MeLanbide60Utils.redondearDecimalesBigDecimal(rs.getBigDecimal("CME_PTO_GASTOS_VIS"),2));
        puesto.setGastosSeguroSol(MeLanbide60Utils.redondearDecimalesBigDecimal(rs.getBigDecimal("CME_PTO_GASTOS_SOL_SEG"),2));
        puesto.setGastosVisadoSol(MeLanbide60Utils.redondearDecimalesBigDecimal(rs.getBigDecimal("CME_PTO_GASTOS_SOL_VIS"),2));
        puesto.setGastosSeguroCon(MeLanbide60Utils.redondearDecimalesBigDecimal(rs.getBigDecimal("CME_PTO_GASTOS_CON_SEG"),2));
        puesto.setGastosVisadoCon(MeLanbide60Utils.redondearDecimalesBigDecimal(rs.getBigDecimal("CME_PTO_GASTOS_CON_VIS"),2));
        
        return puesto;
    }
    
    private ValorCampoDesplegableModuloIntegracionVO mapearValorCampoDesplegableModuloIntegracionVO(ResultSet rs) throws Exception
    {
        ValorCampoDesplegableModuloIntegracionVO campo = new ValorCampoDesplegableModuloIntegracionVO();
        campo.setCodigo(rs.getString("CODIGO"));
        campo.setDescripcion(rs.getString("DESCRIPCION"));
        return campo;
    }
    
    private FilaPuestoVO mapearFilaPuestoVO(ResultSet rs) throws Exception
    {
        FilaPuestoVO fila = new FilaPuestoVO();
        fila.setCodPuesto(rs.getString("CME_PTO_COD_PUESTO"));
        fila.setDescPuesto(rs.getString("CME_PTO_COD_PUESTO")+(rs.getString("CME_PTO_DESC_PUESTO") != null && !rs.getString("CME_PTO_DESC_PUESTO").equals("") && !rs.getString("CME_PTO_DESC_PUESTO").equals("") ? " - "+rs.getString("CME_PTO_DESC_PUESTO").toUpperCase() : ""));
        
        Integer rangoEdad = rs.getInt("RANGO_EDAD");
        String txtRangoEdad = "";
        if (rangoEdad.equals(45)) {
            txtRangoEdad = ">45";
        } else if (rangoEdad.equals(35)) {
            txtRangoEdad = "35-45";
        } else {
            txtRangoEdad = "-";
        }
        
        fila.setRangoEdad(txtRangoEdad);

        fila.setPais(rs.getString("PAIS") != null && !rs.getString("PAIS").equals("") ? rs.getString("PAIS").toUpperCase() : "-");
        fila.setNivelFor(rs.getString("NIVEL_FORMATIVO") != null && !rs.getString("NIVEL_FORMATIVO").equals("") ? rs.getString("NIVEL_FORMATIVO").toUpperCase() : "-");
        fila.setTitulacion1(rs.getString("TITULACION1") != null && !rs.getString("TITULACION1").equals("") ? rs.getString("TITULACION1").toUpperCase() : "");
        fila.setTitulacion2(rs.getString("TITULACION2") != null && !rs.getString("TITULACION2").equals("") ? rs.getString("TITULACION2").toUpperCase() : "");
        fila.setTitulacion3(rs.getString("TITULACION3") != null && !rs.getString("TITULACION3").equals("") ? rs.getString("TITULACION3").toUpperCase() : "");
        fila.setResultado(rs.getString("RESULTADO") != null && !rs.getString("RESULTADO").equals("") ? rs.getString("RESULTADO").toUpperCase() : "-");
        fila.setMotivo(rs.getString("MOTIVO") != null && !rs.getString("MOTIVO").equals("") ? rs.getString("MOTIVO").toUpperCase() : "-");
        fila.setObservaciones(rs.getString("CME_PTO_OBSERVACIONES") != null && !rs.getString("CME_PTO_OBSERVACIONES").equals("") ? rs.getString("CME_PTO_OBSERVACIONES").replaceAll("\r\n", "<br />").replaceAll("\n", "<br />").replaceAll("\r", "<br />").toUpperCase() : "-");
        try
        {
            String str = fila.getObservaciones();
            str = str.replaceAll(System.getProperty("line.separator"), "<br />");
            fila.setObservaciones(str);
        }
        catch(Exception ex)
        {
            
        }
        fila.setSubvAprob(rs.getBigDecimal("SUBV_APROB") != null ? MeLanbide60Utils.redondearDecimalesString(rs.getBigDecimal("SUBV_APROB"), 2).replaceAll("\\.", ",") : "-");
        fila.setSubvSolic(rs.getBigDecimal("SUBV_SOLIC") != null ? MeLanbide60Utils.redondearDecimalesString(rs.getBigDecimal("SUBV_SOLIC"), 2).replaceAll("\\.", ",") : "-");
        return fila;
    }
    
    private SalarioVO mapearSalarioVO(ResultSet rs) throws Exception
    {
        SalarioVO salario = new SalarioVO();
        salario.setCodGrCot(rs.getString("CME_SAL_COD_GR_COT"));
        salario.setCodPai(rs.getInt("CME_SAL_PAI_COD"));
        if(rs.wasNull())
        {
            salario.setCodPai(null);
        }
        salario.setDescripcion(rs.getString("CME_SAL_DESCRIPCION"));
        salario.setImporte(rs.getBigDecimal("CME_SAL_IMPORTE"));
        return salario;
    }
    
    private CmeOfertaVO mapearCmeOfertaVO(ResultSet rs) throws Exception
    {
        CmeOfertaVO oferta = new CmeOfertaVO();
        
        oferta.setDescPuesto(rs.getString("CME_OFE_DESC_PUESTO"));
        oferta.setPaiCod1(rs.getInt("CME_OFE_PAI_COD_1"));
        if(rs.wasNull())
        {
            oferta.setPaiCod1(null);
        }
        oferta.setPaiCod2(rs.getInt("CME_OFE_PAI_COD_2"));
        if(rs.wasNull())
        {
            oferta.setPaiCod2(null);
        }
        oferta.setPaiCod3(rs.getInt("CME_OFE_PAI_COD_3"));
        if(rs.wasNull())
        {
            oferta.setPaiCod3(null);
        }
        oferta.setCiudadDestino(rs.getString("CME_OFE_CIUDAD_DESTINO"));
        oferta.setDpto(rs.getString("CME_OFE_DPTO"));
        oferta.setCodTit1(rs.getString("CME_OFE_COD_TIT_1"));
        oferta.setCodTit2(rs.getString("CME_OFE_COD_TIT_2"));
        oferta.setCodTit3(rs.getString("CME_OFE_COD_TIT_3"));
        oferta.setFunciones(rs.getString("CME_OFE_FUNCIONES"));
        oferta.setCodIdioma1(rs.getString("CME_OFE_COD_IDIOMA_1"));
        oferta.setCodIdioma2(rs.getString("CME_OFE_COD_IDIOMA_2"));
        oferta.setCodIdioma3(rs.getString("CME_OFE_COD_IDIOMA_3"));
        oferta.setCodNivIdi1(rs.getString("CME_OFE_COD_NIV_IDI_1"));
        oferta.setCodNivIdi2(rs.getString("CME_OFE_COD_NIV_IDI_2"));
        oferta.setCodNivIdi3(rs.getString("CME_OFE_COD_NIV_IDI_3"));
        oferta.setCodNivForm(rs.getString("CME_OFE_COD_NIV_FORM"));
        
        
        oferta.setAnoTitulacion(rs.getInt("CME_OFE_ANO_TITULACION"));
        if(rs.wasNull())
        {
            oferta.setAnoTitulacion(null);
        }
        oferta.setApe1(rs.getString("CME_OFE_APE1"));
        oferta.setApe2(rs.getString("CME_OFE_APE2"));
        oferta.setCodCausaBaja(rs.getString("CME_OFE_COD_CAUSA_BAJA"));
        oferta.setCodGrCot(rs.getString("CME_OFE_COD_GR_COT"));
        oferta.setCodOferta(rs.getString("CME_OFE_COD_OFERTA"));
        oferta.setCodOfiGest(rs.getString("CME_OFE_COD_OFI_GEST"));
        oferta.setCodPuesto(rs.getString("CME_OFE_COD_PUESTO"));
        oferta.setCodTipoNif(rs.getString("CME_OFE_COD_TIPO_NIF"));
        oferta.setCodTitulacion(rs.getString("CME_OFE_COD_TITULACION"));
        oferta.setContratacion(rs.getString("CME_OFE_CONTRATACION"));
        oferta.setContratacionPresOferta(rs.getString("CME_OFE_CONTRATACION_PRES_OFE"));
        oferta.setDietasConvoc(MeLanbide60Utils.redondearDecimalesBigDecimal(rs.getBigDecimal("CME_OFE_DIETAS_CONVOC"), 2));
        oferta.setDietasConvenio(MeLanbide60Utils.redondearDecimalesBigDecimal(rs.getBigDecimal("CME_OFE_DIETAS_CONVENIO"), 2));
        oferta.setDifusion(rs.getString("CME_OFE_DIFUSION"));
        oferta.setDocContratacion(rs.getBytes("CME_OFE_DOC_CONTRATACION"));
        oferta.setDocGestOfe(rs.getBytes("CME_OFE_DOC_GEST_OFE"));
        oferta.setEmail(rs.getString("CME_OFE_EMAIL"));
        oferta.setExpEje(rs.getInt("CME_OFE_EXP_EJE"));
        if(rs.wasNull())
        {
            oferta.setExpEje(null);
        }
        oferta.setFecBaja(rs.getDate("CME_OFE_FEC_BAJA"));
        oferta.setFecDifusion(rs.getDate("CME_OFE_FEC_DIFUSION"));
        oferta.setFecEnvCand(rs.getDate("CME_OFE_FEC_ENV_CAND"));
        oferta.setFecFin(rs.getDate("CME_OFE_FEC_FIN"));
        oferta.setFecIni(rs.getDate("CME_OFE_FEC_INI"));
        oferta.setFecNac(rs.getDate("CME_OFE_FEC_NAC"));
        oferta.setFlSustituto(rs.getString("CME_OFE_FL_SUSTITUTO"));
        oferta.setIdOferta(rs.getInt("CME_OFE_ID_OFERTA"));
        if(rs.wasNull())
        {
            oferta.setIdOferta(null);
        }
        oferta.setIdOfertaOrigen(rs.getInt("CME_OFE_ID_OFERTA_ORIGEN"));
        if(rs.wasNull())
        {
            oferta.setIdOfertaOrigen(null);
        }
        oferta.setMesesContrato(rs.getInt("CME_OFE_MESES_CONTRATO"));
        if(rs.wasNull())
        {
            oferta.setMesesContrato(null);
        }
        oferta.setMotContratacion(rs.getString("CME_OFE_MOT_CONTRATACION"));
        oferta.setNif(rs.getString("CME_OFE_NIF"));
        oferta.setNombre(rs.getString("CME_OFE_NOMBRE"));
        oferta.setNomDocContratacion(rs.getString("CME_OFE_NOM_DOC_CONTRATACION"));
        oferta.setNomDocGestOfe(rs.getString("CME_OFE_NOM_DOC_GEST_OFE"));
        oferta.setNumExp(rs.getString("CME_OFE_NUMEXP"));
        oferta.setNumTotCand(rs.getInt("CME_OFE_NUM_TOT_CAND"));
        if(rs.wasNull())
        {
            oferta.setNumTotCand(null);
        }
        oferta.setObservaciones(rs.getString("CME_OFE_OBSERVACIONES"));
        oferta.setPrec(rs.getString("CME_OFE_PREC"));
        oferta.setPrecNom(rs.getString("CME_OFE_PREC_NOM"));
        oferta.setSalarioB(MeLanbide60Utils.redondearDecimalesBigDecimal(rs.getBigDecimal("CME_OFE_SALARIOB"), 2));
        oferta.setSexo(rs.getString("CME_OFE_SEXO"));
        oferta.setTlf(rs.getString("CME_OFE_TLF"));
        oferta.setCodCausaRenuncia(rs.getString("CME_OFE_COD_CAUSA_RENUNCIA"));
        oferta.setCodCausaRenunciaPresOferta(rs.getString("CME_OFE_COD_CAUSA_REN_PRE_OF"));
        return oferta;
    }
    
    private FilaOfertaVO mapearFilaOfertaVO(ResultSet rs) throws Exception
    {
        SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide60.FORMATO_FECHA);
        FilaOfertaVO fila = new FilaOfertaVO();
        fila.setIdOferta(rs.getInt("ID_OFERTA"));
        if(rs.wasNull())
        {
            fila.setIdOferta(null);
        }
        //fila.setCausaBaja(rs.getString("CAUSA_BAJA") != null && !rs.getString("CAUSA_BAJA").equals("") ? rs.getString("CAUSA_BAJA").toUpperCase() : "-");
        String contratacion = rs.getString("CME_OFE_CONTRATACION");
        String contratacionPresOferta = rs.getString("CME_OFE_CONTRATACION_PRES_OFE");
        
        if (contratacionPresOferta!=null && !contratacionPresOferta.equals(ConstantesMeLanbide60.FALSO)){
            String causaBaja = rs.getString("CAUSA_BAJA");
            if(causaBaja != null && !causaBaja.equals(""))
            {
                //Si hay causa de baja entonces contratacion = 'S'
                fila.setCausaBaja(causaBaja.toUpperCase());
            }
            else
            {
                //Si no hay causa de baja, contratacion sera 'N'
                String causaRen = rs.getString("CAUSA_RENUNCIA");
                fila.setCausaBaja(causaRen != null && !causaRen.equals("") ? "RENUNCIA("+causaRen.toUpperCase()+")" : (contratacion != null && contratacion.equals(ConstantesMeLanbide60.FALSO) ? "RENUNCIA" : "-"));
            }
        }else { //NO HAY OFERTA --causa renuncia(motivo)
            String causaRen = rs.getString("CAUSA_RENUNCIA");
            fila.setCausaBaja(causaRen != null && !causaRen.equals("") ? "RENUNCIA NO OFERTA("+causaRen.toUpperCase()+")" : (contratacionPresOferta != null && contratacionPresOferta.equals(ConstantesMeLanbide60.FALSO) ? "RENUNCIA NO OFERTA" : "-"));
        }
        
        fila.setCodOferta(rs.getString("COD_OFERTA") != null && !rs.getString("COD_OFERTA").equals("") ? rs.getString("COD_OFERTA").toUpperCase() : rs.getString("COD_OFERTA_2") != null && !rs.getString("COD_OFERTA_2").equals("") ? rs.getString("COD_OFERTA_2").toUpperCase() : "-");
        String codOferta = fila.getCodOferta();
        String str = rs.getString("DESC_OFERTA");
        String descOferta = codOferta != null ? codOferta : "";
        descOferta += str != null && !str.equals("") ? (descOferta != null && !descOferta.equals("") ? " - " : "") : "";
        descOferta += str != null ? str : "";
        //fila.setDescOferta(descOferta != null && !descOferta.equals("") ? descOferta.toUpperCase() : "-");
        fila.setDescOferta(codOferta != null && !codOferta.equals("") ? codOferta.toUpperCase() : "-");
        fila.setCodPuesto(rs.getString("COD_PUESTO") != null && !rs.getString("COD_PUESTO").equals("") ? rs.getString("COD_PUESTO").toUpperCase() : "-");
        String descPuesto = fila.getCodPuesto();
        String str2 = rs.getString("DESC_PUESTO");
        descPuesto += str2 != null && !str2.equals("") ? (descPuesto != null && !descPuesto.equals("") ? " - " : "") : "";
        descPuesto += str2 != null ? str2 : "";
        fila.setDescPuesto(descPuesto != null && !descPuesto.equals("") ? descPuesto.toUpperCase() : "-");
        fila.setDni(rs.getString("NIF") != null && !rs.getString("NIF").equals("") ? rs.getString("NIF").toUpperCase() : "-");
        fila.setFecBaja(rs.getDate("FEC_BAJA") != null ? format.format(rs.getDate("FEC_BAJA")) : "-");
        fila.setFecFin(rs.getDate("FEC_FIN") != null ? format.format(rs.getDate("FEC_FIN")) : "-");
        fila.setFecIni(rs.getDate("FEC_INI") != null ? format.format(rs.getDate("FEC_INI")) : "-");
        String nom = rs.getString("NOMBRE");
        String ape1 = rs.getString("APE1");
        String ape2 = rs.getString("APE2");
        String nomApe = nom != null ? nom : "";
        nomApe += ape1 != null && !ape1.equals("") ? (nomApe != null && !nomApe.equals("") ? " " : "") : "";
        nomApe += ape1 != null ? ape1 : "";
        nomApe += ape2 != null && !ape2.equals("") ? (nomApe != null && !nomApe.equals("") ? " " : "") : "";
        nomApe += ape2 != null ? ape2 : "";
        fila.setNomApel(nomApe != null && !nomApe.equals("") ? nomApe.toUpperCase() : "-");
        return fila;
    }
    
    private CmeJustificacionVO mapearCmeJustificacionVO(ResultSet rs) throws Exception
    {
        CmeJustificacionVO justif = new CmeJustificacionVO();
        justif.setCodEstado(rs.getString("CME_JUS_COD_ESTADO"));
        justif.setCodPuesto(rs.getString("CME_JUS_COD_PUESTO"));
        justif.setEjercicio(rs.getInt("CME_JUS_EXP_EJE"));
        
        if(rs.wasNull())
        {
            justif.setEjercicio(null);
        }
        justif.setSalarioSub(rs.getBigDecimal("CME_JUS_SALARIOSUB"));//!= null ? MeLanbide60Utils.redondearDecimalesString(rs.getBigDecimal("SALARIO_SUB"), 2).replaceAll("\\.", ",") : "-");
        justif.setDietasJusti(rs.getBigDecimal("CME_JUS_DIETAS"));
        justif.setGastosGestion(rs.getBigDecimal("CME_JUS_GASTOSGES"));
        justif.setBonif(rs.getBigDecimal("CME_JUS_BONIF"));
        
        justif.setMinoracion(rs.getBigDecimal("CME_JUS_MINORACION"));
        justif.setBaseCC(rs.getBigDecimal("CME_JUS_BASCOT_CC"));
        justif.setCoeficienteApli(rs.getBigDecimal("CME_JUS_COEF_TGSS"));
        justif.setPorcFogasa(rs.getBigDecimal("CME_JUS_FOGASA"));
        justif.setPorcCoeficiente(rs.getBigDecimal("CME_JUS_AT"));
        justif.setPorcAportacion(rs.getBigDecimal("CME_JUS_EPSV"));
        
        if(rs.getString("CME_JUS_APORTEPSV") != null && !rs.getString("CME_JUS_APORTEPSV").equals("") )
            justif.setAportEpsv(rs.getBigDecimal("CME_JUS_APORTEPSV"));
        
        if(rs.getString("CME_JUS_DIAS_TRAB") != null)
            justif.setDiasTrab(rs.getInt("CME_JUS_DIAS_TRAB"));
        if(rs.getString("CME_JUS_DIAS_SEGSOC") != null)
            justif.setDiasSegSoc(rs.getInt("CME_JUS_DIAS_SEGSOC"));
        
        justif.setIdJustificacion(rs.getInt("CME_JUS_ID_JUSTIFICACION"));
        if(rs.wasNull())
        {
            justif.setIdJustificacion(null);
        }
        justif.setIdOferta(rs.getInt("CME_JUS_ID_OFERTA"));
        if(rs.wasNull())
        {
            justif.setIdOferta(null);
        }
        justif.setNumExpediente(rs.getString("CME_JUS_NUMEXP"));
        justif.setFlVariosTrab(rs.getString("CME_JUS_FL_VARIOS_TRAB"));
        if(rs.getString("CME_JUS_BASCOT_AT") != null && !rs.getString("CME_JUS_BASCOT_AT").equals("") )
            justif.setBaseAT(rs.getBigDecimal("CME_JUS_BASCOT_AT"));
        if(rs.getString("CME_JUS_MESES_EXT") != null && !rs.getString("CME_JUS_MESES_EXT").equals("") )
            justif.setMesesExt(rs.getBigDecimal("CME_JUS_MESES_EXT"));
        if(rs.getString("CME_JUS_SALARIO") != null && !rs.getString("CME_JUS_SALARIO").equals("") )
            justif.setSalario(rs.getBigDecimal("CME_JUS_SALARIO"));
        if(rs.getString("CME_JUS_GASTOS_SEG") != null && !rs.getString("CME_JUS_GASTOS_SEG").equals("") )
            justif.setGastosSeguro(rs.getBigDecimal("CME_JUS_GASTOS_SEG"));
        if(rs.getString("CME_JUS_GASTOS_VIS") != null && !rs.getString("CME_JUS_GASTOS_VIS").equals("") )
            justif.setGastosVisado(rs.getBigDecimal("CME_JUS_GASTOS_VIS"));
        justif.setImpJustificado(MeLanbide60Utils.redondearDecimalesBigDecimal(rs.getBigDecimal("CME_JUS_IMP_JUSTIFICADO"), 2));
        return justif;
    }
    
    private FilaJustificacionVO mapearFilaJustificacionVO(ResultSet rs) throws Exception
    {
        SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide60.FORMATO_FECHA);
        FilaJustificacionVO fila = new FilaJustificacionVO();
        /*fila.setIdJustificacion(rs.getInt("ID_JUSTIFICACION"));
        if(rs.wasNull())
        {
            fila.setIdJustificacion(null);
        }*/
        /*fila.setIdOferta(rs.getInt("ID_OFERTA"));
        if(rs.wasNull())
        {
            fila.setIdOferta(null);
        }*/
        //fila.setCodOferta(rs.getString("COD_OFERTA") != null && !rs.getString("COD_OFERTA").equals("") ? rs.getString("COD_OFERTA").toUpperCase() : rs.getString("COD_OFERTA_2") != null && !rs.getString("COD_OFERTA_2").equals("") ? rs.getString("COD_OFERTA_2").toUpperCase() : "-");
        fila.setCodPuesto(rs.getString("COD_PUESTO") != null && !rs.getString("COD_PUESTO").equals("") ? rs.getString("COD_PUESTO").toUpperCase() : "-");
        String descPuesto = fila.getCodPuesto();
        String str2 = rs.getString("DESC_PUESTO");
        descPuesto += str2 != null && !str2.equals("") ? (descPuesto != null && !descPuesto.equals("") ? " - " : "") : "";
        descPuesto += str2 != null ? str2 : "";
        fila.setDescPuesto(descPuesto != null && !descPuesto.equals("") ? descPuesto.toUpperCase() : "-");
        fila.setEstado(rs.getString("ESTADO") != null ? rs.getString("ESTADO") : "-");
        fila.setImpJustif(rs.getBigDecimal("IMP_JUSTIF") != null ? MeLanbide60Utils.redondearDecimalesString(rs.getBigDecimal("IMP_JUSTIF"), 2).replaceAll("\\.", ",") : "-");
        fila.setSalarioSub(rs.getBigDecimal("SALARIO_SUB")!= null ? MeLanbide60Utils.redondearDecimalesString(rs.getBigDecimal("SALARIO_SUB"), 2).replaceAll("\\.", ",") : "-");
        fila.setDietasJusti(rs.getBigDecimal("DIETAS_JUSTI")!= null ? MeLanbide60Utils.redondearDecimalesString(rs.getBigDecimal("DIETAS_JUSTI"), 2).replaceAll("\\.", ",") : "-");
        fila.setGastosGestion(rs.getBigDecimal("GASTOS_GESION")!= null ? MeLanbide60Utils.redondearDecimalesString(rs.getBigDecimal("GASTOS_GESION"), 2).replaceAll("\\.", ",") : "-");
        fila.setBonif(rs.getBigDecimal("BONIF")!= null ? MeLanbide60Utils.redondearDecimalesString(rs.getBigDecimal("BONIF"), 2).replaceAll("\\.", ",") : "-");
        fila.setImpSolic(rs.getBigDecimal("IMP_SOLIC") != null ? MeLanbide60Utils.redondearDecimalesString(rs.getBigDecimal("IMP_SOLIC"), 2).replaceAll("\\.", ",") : "-");
        fila.setNumContrataciones(""+rs.getInt("NUM_CONTRATACIONES"));
        /*if(rs.wasNull())
        {
            fila.setNumContrataciones("-");
        }*/
        return fila;
    }
    
    private FilaPersonaContratadaVO mapearFilaPersonaContratadaVO(ResultSet rs) throws Exception
    {
        SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide60.FORMATO_FECHA);
        FilaPersonaContratadaVO fila = new FilaPersonaContratadaVO();
        fila.setFeDesde(rs.getDate("CME_OFE_FEC_INI") != null ? format.format(rs.getDate("CME_OFE_FEC_INI")) : "-");
        fila.setFeHasta(rs.getDate("CME_OFE_FEC_FIN") != null ? format.format(rs.getDate("CME_OFE_FEC_FIN")) : "-");
        fila.setIdJustificacion(rs.getInt("CME_JUS_ID_JUSTIFICACION"));
        if(rs.wasNull())
        {
            fila.setIdJustificacion(null);
        }
        fila.setSalarioSub(rs.getBigDecimal("SALARIO_SUB")!= null ? MeLanbide60Utils.redondearDecimalesString(rs.getBigDecimal("SALARIO_SUB"), 2).replaceAll("\\.", ",") : "-");
        fila.setDietasJusti(rs.getBigDecimal("DIETAS_JUSTI")!= null ? MeLanbide60Utils.redondearDecimalesString(rs.getBigDecimal("DIETAS_JUSTI"), 2).replaceAll("\\.", ",") : "-");
        fila.setGastosGestion(rs.getBigDecimal("GASTOS_GESION")!= null ? MeLanbide60Utils.redondearDecimalesString(rs.getBigDecimal("GASTOS_GESION"), 2).replaceAll("\\.", ",") : "-");
        fila.setBonif(rs.getBigDecimal("BONIF")!= null ? MeLanbide60Utils.redondearDecimalesString(rs.getBigDecimal("BONIF"), 2).replaceAll("\\.", ",") : "-");
        
        
        fila.setIdOferta(rs.getInt("CME_OFE_ID_OFERTA"));
        if(rs.wasNull())
        {
            fila.setIdOferta(null);
        }
        fila.setImpJustif(rs.getBigDecimal("IMP_JUSTIF") != null ? MeLanbide60Utils.redondearDecimalesString(rs.getBigDecimal("IMP_JUSTIF"), 2).replaceAll("\\.", ",") : "-");
        fila.setNif(rs.getString("CME_OFE_NIF") != null ? rs.getString("CME_OFE_NIF") : "-");
        String nom = rs.getString("CME_OFE_NOMBRE");
        String ape1 = rs.getString("CME_OFE_APE1");
        String ape2 = rs.getString("CME_OFE_APE2");
        String nomApe = nom != null ? nom : "";
        nomApe += ape1 != null && !ape1.equals("") ? (nomApe != null && !nomApe.equals("") ? " " : "") : "";
        nomApe += ape1 != null ? ape1 : "";
        nomApe += ape2 != null && !ape2.equals("") ? (nomApe != null && !nomApe.equals("") ? " " : "") : "";
        nomApe += ape2 != null ? ape2 : "";
        fila.setNomApel(nomApe);
        return fila;
    }
    
    private FilaInformeResumenPuestosContratadosVO mapearFilaInformeResumenPuestosContratadosVO(ResultSet rs) throws Exception
    {
        FilaInformeResumenPuestosContratadosVO fila = new FilaInformeResumenPuestosContratadosVO();
        fila.setApe1Selec(rs.getString("APE1_SELEC"));
        fila.setApe2Selec(rs.getString("APE2_SELEC"));
        fila.setCausaRenuncia(rs.getString("CAUSA_RENUNCIA"));
        fila.setCodPuesto(rs.getString("COD_PUESTO"));
        fila.setDescPuesto(rs.getString("DESC_PUESTO"));
        fila.setDifusion(rs.getString("DIFUSION"));
        fila.setEmpresa(rs.getString("EMPRESA"));
        fila.setFecEnvPersPrecandidatas(rs.getString("FECHA_ENV_PERS_CANDIDATAS"));
        fila.setFecFin(rs.getString("FECHA_FIN"));
        fila.setFecIni(rs.getString("FECHA_INICIO"));
        fila.setFecNac(rs.getString("FECHA_NACIMIENTO"));
        fila.setIdioma1(rs.getString("IDIOMA_1"));
        fila.setIdioma2(rs.getString("IDIOMA_2"));
        fila.setIdioma3(rs.getString("IDIOMA_3"));
        fila.setMotivo(rs.getString("MOTIVO"));
        fila.setMotivoRenuncia(rs.getString("MOTIVO_RENUNCIA"));
        fila.setNifSelec(rs.getString("NIF_SELEC"));
        fila.setNivelFormativo(rs.getString("NIVEL_FORMATIVO"));
        fila.setNivelIdioma1(rs.getString("NIVEL_IDIOMA_1"));
        fila.setNivelIdioma2(rs.getString("NIVEL_IDIOMA_2"));
        fila.setNivelIdioma3(rs.getString("NIVEL_IDIOMA_3"));
        fila.setNomSelec(rs.getString("NOM_SELEC"));
        fila.setNumCandidatos(rs.getInt("NUMERO_CANDIDATOS"));
        if(rs.wasNull())
        {
            fila.setNumCandidatos(null);
        }
        fila.setNumExpediente(rs.getString("NUM_EXPEDIENTE"));
        fila.setOferta(rs.getString("OFERTA"));
        fila.setPaisSol1(rs.getString("PAIS_SOL_1"));
        fila.setPaisSol2(rs.getString("PAIS_SOL_2"));
        fila.setPaisSol3(rs.getString("PAIS_SOL_3"));
        fila.setPaisCont1(rs.getString("PAIS_CONT_1"));
        fila.setPaisCont2(rs.getString("PAIS_CONT_2"));
        fila.setPaisCont3(rs.getString("PAIS_CONT_3"));
        fila.setPersPrecandidatas(rs.getString("PERSONAS_PRECANDIDATAS"));
        fila.setPuestosCont(rs.getInt("PUESTOS_CONTRATADOS"));
        if(rs.wasNull())
        {
            fila.setPuestosCont(null);
        }
        fila.setPuestosDen(rs.getInt("PUESTOS_DENEGADOS"));
        if(rs.wasNull())
        {
            fila.setPuestosDen(null);
        }
        fila.setPuestosRenAntesRes(rs.getInt("PUESTOS_REN_ANTES_RES"));
        if(rs.wasNull())
        {
            fila.setPuestosRenAntesRes(null);
        }
        fila.setPuestosRenAntPrimPago(rs.getInt("PUESTOS_REN_ANT_1PAGO"));
        if(rs.wasNull())
        {
            fila.setPuestosRenAntPrimPago(null);
        }
        fila.setPuestosSolic(rs.getInt("PUESTOS_SOLICITADOS"));
        if(rs.wasNull())
        {
            fila.setPuestosSolic(null);
        }
        fila.setResultado(rs.getString("RESULTADO"));
        fila.setSexo(rs.getString("SEXO"));
        fila.setTerritorioHistorico(rs.getString("TER_HIS"));
        fila.setTiempoContratacion(rs.getInt("TIEMPO_CONTRATACION"));
        if(rs.wasNull())
        {
            fila.setTiempoContratacion(null);
        }
        fila.setTitulacion1(rs.getString("TITULACION1"));
        fila.setTitulacion2(rs.getString("TITULACION2"));
        fila.setTitulacion3(rs.getString("TITULACION3"));
        fila.setTitulacionSelec(rs.getString("TITULACION_PERS_CANDIDATA"));
        return fila;
    }
    
    private FilaInformeResumenEconomicoVO mapearFilaInformeResumenEconomicoVO(ResultSet rs) throws Exception
    {
        FilaInformeResumenEconomicoVO fila = new FilaInformeResumenEconomicoVO();
        fila.setCnae09(rs.getString("CNAE09"));
        fila.setCodPuesto(rs.getString("COD_PUESTO"));
        fila.setCostesCont(rs.getBigDecimal("COSTES_CONT"));
        fila.setCostesContDietasConve(rs.getBigDecimal("COSTES_CONT_DIETAS_CONVE"));
        fila.setCostesContSalarioSS(rs.getBigDecimal("COSTES_CONT_SALARIO_SS"));
        fila.setDietasSubv(rs.getBigDecimal("DIETAS_SUBV"));
        fila.setDotacionAnualMaxima(rs.getBigDecimal("DOTACION_ANUAL_MAXIMA"));
        fila.setEntidad(rs.getString("ENTIDAD"));
        fila.setFecFin(rs.getString("FEC_FIN"));
        fila.setFecIni(rs.getString("FEC_INI"));
        fila.setGastosTram(rs.getBigDecimal("GASTOS_TRAM"));
        fila.setImpConcedidoTrasResol(rs.getBigDecimal("IMP_CONCEDIDO_TRAS_RESOL"));
        fila.setImpMaxSubvencionable(rs.getBigDecimal("IMP_MAX_SUBVENCIONABLE"));
        fila.setImporteConcedido(rs.getBigDecimal("IMPORTE_CONCEDIDO"));
        fila.setImporteRenuncia(rs.getBigDecimal("IMPORTE_RENUNCIA"));
        fila.setMesesSubvencionables(rs.getInt("MESES_SUBVENCIONABLES"));
        fila.setMinimisConcedidos(rs.getBigDecimal("MINIMIS_CONCEDIDOS"));
        fila.setMotivo(rs.getString("MOTIVO"));
        fila.setNumExpediente(rs.getString("N_EXPEDIENTE"));
        fila.setOrden(rs.getInt("ORDEN"));
        fila.setPais1(rs.getString("PAIS1"));
        fila.setPais2(rs.getString("PAIS2"));
        fila.setPais3(rs.getString("PAIS3"));
        fila.setPeriodoMesesSubv(rs.getInt("PERIODO_MESES_SUBVENCIONABLES"));
        fila.setPuesto(rs.getString("PUESTO"));
        fila.setResultado(rs.getString("RESULTADO"));
        fila.setSolDietasConvSS(rs.getBigDecimal("SOL_DIETAS_CONV_SS"));
        fila.setSolGastosTra(rs.getBigDecimal("SOL_GASTOS_TRA"));
        fila.setSolSalarioSS(rs.getBigDecimal("SOL_SALARIO_SS"));
        fila.setSolicitado(rs.getBigDecimal("SOLICITADO"));
        fila.setTerritorioHistorico(rs.getString("PROVINCIA"));
        fila.setTitulacionAB(rs.getString("TITULACION_AB"));
        fila.setnPuestosSolicitados(rs.getInt("N_PUESTOS_SOLICITADOS"));
        return fila;
    }
    
    private FilaResultadoBusqTitulaciones mapearFilaResultadoBusqTitulaciones(ResultSet rs) throws Exception
    {       
        FilaResultadoBusqTitulaciones fila = new FilaResultadoBusqTitulaciones();
        
        fila.setCodigoInterno(rs.getString("GEN_TIT_COD_TITULAC"));
        fila.setCodigoVisible(rs.getString("GEN_TIT_COD_TITULAC"));
        fila.setDescripcion(rs.getString("GEN_TIT_DESC_TITULAC_C"));
        
        return fila;
    }
    
    private FilaResumenVO mapearFilaResumenVO(ResultSet rs) throws Exception
    {
        SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide60.FORMATO_FECHA);
        FilaResumenVO fila = new FilaResumenVO();
        
        fila.setNomApe(rs.getString("NOMBRE_APELLIDOS"));
        fila.setGrupo(rs.getString("CME_OFE_COD_NIV_FORM") != null ? rs.getString("CME_OFE_COD_NIV_FORM") : "-");
        fila.setDni(rs.getString("CME_OFE_NIF"));
        fila.setPaisSolicitado(rs.getString("PAIS_SOLICITADO1") + " // " + rs.getString("PAIS_SOLICITADO2") + " // " +rs.getString("PAIS_SOLICITADO3"));
        fila.setPaisFinal(rs.getString("PAIS_FINAL1")+ " // " + rs.getString("PAIS_FINAL2")+ " // " + rs.getString("PAIS_FINAL3"));
        fila.setMesesContrato(rs.getDouble("CME_JUS_MESES_EXT"));
        fila.setFecIni(rs.getDate("CME_OFE_FEC_INI") != null ? format.format(rs.getDate("CME_OFE_FEC_INI")) : "-");
        if(rs.getDate("CME_OFE_FEC_BAJA") != null)
            fila.setFecFin(rs.getDate("CME_OFE_FEC_BAJA") != null ? format.format(rs.getDate("CME_OFE_FEC_BAJA")) : "-");
        else
            fila.setFecFin(rs.getDate("CME_OFE_FEC_FIN") != null ? format.format(rs.getDate("CME_OFE_FEC_FIN")) : "-");
        //fila.setTotalDias(rs.getInt("DIAS_TRABAJADOS"));
        fila.setSalarioAnexo(rs.getBigDecimal("SALARIO_ANEXO") != null ? MeLanbide60Utils.redondearDecimalesString(rs.getBigDecimal("SALARIO_ANEXO"), 2).replaceAll("\\.", ",") : "-");
        //fila.setMaximoSubv(rs.getBigDecimal("MAX_SUBV_SALARIO_EN_PERIODO") != null ? MeLanbide39Utils.redondearDecimalesString(rs.getBigDecimal("MAX_SUBV_SALARIO_EN_PERIODO"), 2).replaceAll("\\.", ",") : "-");
        fila.setMinoracion(rs.getBigDecimal("MINORACION") != null ? MeLanbide60Utils.redondearDecimalesString(rs.getBigDecimal("MINORACION"), 2).replaceAll("\\.", ",") : "-");
        //fila.setDevengado(rs.getBigDecimal("DEVENGADO_SIN_DIETAS_SUBV") != null ? MeLanbide39Utils.redondearDecimalesString(rs.getBigDecimal("DEVENGADO_SIN_DIETAS_SUBV"), 2).replaceAll("\\.", ",") : "-");

        //------------------COSTE ANUAL -----------------------
        fila.setBcPeriodo(rs.getBigDecimal("BC_CC_EN_PERI") != null ? rs.getString("BC_CC_EN_PERI").replaceAll("\\.", ",") : "-");
        fila.setCoeficienteTGSS(rs.getBigDecimal("COEFICIENTE_TGSS") != null ? MeLanbide60Utils.redondearDecimalesString(rs.getBigDecimal("COEFICIENTE_TGSS"), 2).replaceAll("\\.", ",") : "-");
        fila.setSsEmpresa(rs.getBigDecimal("SS_EMPRESA") != null ? MeLanbide60Utils.redondearDecimalesString(rs.getBigDecimal("SS_EMPRESA"), 2).replaceAll("\\.", ",") : "-");
        fila.setBcAT(rs.getBigDecimal("BC_AT") != null ? rs.getBigDecimal("BC_AT").toString().replaceAll("\\.", ",") : "-");
        fila.setCoeficienteFogasa(rs.getBigDecimal("COEFICIENTE_FOGASA") != null ? MeLanbide60Utils.redondearDecimalesString(rs.getBigDecimal("COEFICIENTE_FOGASA"), 2).replaceAll("\\.", ",") : "-");
        fila.setCoefiecienteAT(rs.getBigDecimal("COEFICIENTE_AT") != null ? MeLanbide60Utils.redondearDecimalesString(rs.getBigDecimal("COEFICIENTE_AT"), 2).replaceAll("\\.", ",") : "-");
        fila.setFogasaATEmpresa(rs.getBigDecimal("FOGASA_AT_EMPRESA") != null ? MeLanbide60Utils.redondearDecimalesString(rs.getBigDecimal("FOGASA_AT_EMPRESA"), 2).replaceAll("\\.", ",") : "-");
        fila.setPorcEPSV(rs.getBigDecimal("PORC_EPSV") != null ? MeLanbide60Utils.redondearDecimalesString(rs.getBigDecimal("PORC_EPSV"), 2).replaceAll("\\.", ",") : "0");
        fila.setAportEPSV(rs.getBigDecimal("APORTACIONES_EPSV_EMPRESA") != null ? MeLanbide60Utils.redondearDecimalesString(rs.getBigDecimal("APORTACIONES_EPSV_EMPRESA"), 2).replaceAll("\\.", ",") : "-");
        fila.setTotalEmpresa(rs.getBigDecimal("TOTAL_EMPRESA") != null ? MeLanbide60Utils.redondearDecimalesString(rs.getBigDecimal("TOTAL_EMPRESA"), 2).replaceAll("\\.", ",") : "-");
        fila.setTotalPeriodo(rs.getBigDecimal("TOTAL_PERIODO") != null ? MeLanbide60Utils.redondearDecimalesString(rs.getBigDecimal("TOTAL_PERIODO"), 2).replaceAll("\\.", ",") : "-");

        //------------------MAXIMOS SUBVENCIONABLES ----------        
        fila.setSubvMinorada(rs.getBigDecimal("SUBV_MINORADA_SALAR_SSOCIAL") != null ? MeLanbide60Utils.redondearDecimalesString(rs.getBigDecimal("SUBV_MINORADA_SALAR_SSOCIAL"), 2).replaceAll("\\.", ",") : "-");
        //fila.setMaximoPeriodoSubv(rs.getBigDecimal("MAX_SUBV_PERIODO") != null ? MeLanbide39Utils.redondearDecimalesString(rs.getBigDecimal("MAX_SUBV_PERIODO"), 2).replaceAll("\\.", ",") : "-");
        fila.setCostePeriodoSubv(rs.getBigDecimal("SALARIO_PERIODO") != null ? MeLanbide60Utils.redondearDecimalesString(rs.getBigDecimal("SALARIO_PERIODO"), 2).replaceAll("\\.", ",") : "-");
        //fila.setCosteJustificacion(rs.getBigDecimal("SALARIO_JUS_SUBVENCION") != null ? MeLanbide39Utils.redondearDecimalesString(rs.getBigDecimal("SALARIO_JUS_SUBVENCION"), 2).replaceAll("\\.", ",") : "-");
        fila.setDietasPeriodoSubv(rs.getBigDecimal("DIETAS_ABONADAS_PERIODO") != null ? MeLanbide60Utils.redondearDecimalesString(rs.getBigDecimal("DIETAS_ABONADAS_PERIODO"), 2).replaceAll("\\.", ",") : "-");
        fila.setDietasConcedidas(rs.getBigDecimal("DIETAS_CONCEDIDAS_PERIODO") != null ? MeLanbide60Utils.redondearDecimalesString(rs.getBigDecimal("DIETAS_CONCEDIDAS_PERIODO"), 2).replaceAll("\\.", ",") : "-");
        fila.setMaximoDietas(rs.getBigDecimal("MAX_SUB_DIETAS") != null ? MeLanbide60Utils.redondearDecimalesString(rs.getBigDecimal("MAX_SUB_DIETAS"), 2).replaceAll("\\.", ",") : "-");
        //A petición del cliente, esta columna no se incluye en la primera version.
        //fila.setDietasSubvJustif(rs.getBigDecimal("MAX_SUB_DIETAS") != null ? MeLanbide39Utils.redondearDecimalesString(rs.getBigDecimal("MAX_SUB_DIETAS"), 2).replaceAll("\\.", ",") : "-");
        fila.setVisadoAbonado(rs.getBigDecimal("VIS_SEG_ABONADO") != null ? MeLanbide60Utils.redondearDecimalesString(rs.getBigDecimal("VIS_SEG_ABONADO"), 2).replaceAll("\\.", ",") : "-");
        fila.setVisadoConcedido(rs.getBigDecimal("VIS_SEG_CONCEDIDO") != null ? MeLanbide60Utils.redondearDecimalesString(rs.getBigDecimal("VIS_SEG_CONCEDIDO"), 2).replaceAll("\\.", ",") : "-");
        fila.setVisadoSubv(rs.getBigDecimal("VIS_SEG_SUBV") != null ? MeLanbide60Utils.redondearDecimalesString(rs.getBigDecimal("VIS_SEG_SUBV"), 2).replaceAll("\\.", ",") : "-");
        fila.setContratoBonif(rs.getBigDecimal("BONIFICACIONES") != null ? MeLanbide60Utils.redondearDecimalesString(rs.getBigDecimal("BONIFICACIONES"), 2).replaceAll("\\.", ",") : "-");
        //fila.setCosteSubvecionable(rs.getBigDecimal("coste_subvencionable") != null ? MeLanbide39Utils.redondearDecimalesString(rs.getBigDecimal("coste_subvencionable"), 2).replaceAll("\\.", ",") : "-");
        if(null == rs.getString("CME_OFE_FL_SUSTITUTO"))
            fila.setSustituto("");
        else
            fila.setSustituto(rs.getString("CME_OFE_FL_SUSTITUTO"));
        
        fila.setMaxSubv(rs.getString("IMP_CONCEDIDO"));
        return fila;
    }
         
    private FilaInformeDatosPuestosVO mapearFilaInformeDatosPuestosVO(ResultSet rs) throws Exception
    {
        SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide60.FORMATO_FECHA);
        FilaInformeDatosPuestosVO fila = new FilaInformeDatosPuestosVO();
        
        fila.setNumExpediente(rs.getString("CME_OFE_NUMEXP"));
        fila.setEmpresa(rs.getString("entidad"));
        fila.setPersContratada(rs.getString("NOMBRE_APELLIDOS"));
        fila.setSexo(rs.getString("SEXO"));
        fila.setNivelFormativo(rs.getString("CME_OFE_COD_NIV_FORM") != null ? rs.getString("CME_OFE_COD_NIV_FORM") : "-");
        fila.setPais(rs.getString("PAIS_SOLICITADO1") + " // " + rs.getString("PAIS_SOLICITADO2") + " // " +rs.getString("PAIS_SOLICITADO3"));
        fila.setSalarioAnualBruto(rs.getBigDecimal("SALARIO_ANEXO") != null ? MeLanbide60Utils.redondearDecimalesString(rs.getBigDecimal("SALARIO_ANEXO"), 2).replaceAll("\\.", ",") : "-");
        fila.setDietasAloj(rs.getBigDecimal("DIETAS_CONCEDIDAS_PERIODO") != null ? MeLanbide60Utils.redondearDecimalesString(rs.getBigDecimal("DIETAS_CONCEDIDAS_PERIODO"), 2).replaceAll("\\.", ",") : "-");
        fila.setNumMeses(rs.getString("CME_JUS_MESES_EXT"));
        fila.setTramitacionPer(rs.getString("VIS_SEG_CONCEDIDO"));
        fila.setImporteConce(rs.getString("IMPORTE"));
        fila.setActividad(rs.getString("ACTIVIDAD"));
        fila.setPaisDestino(rs.getString("PAIS_FINAL1")+ " // " + rs.getString("PAIS_FINAL2")+ " // " + rs.getString("PAIS_FINAL3"));
        fila.setSalarioAnexo(rs.getBigDecimal("SALARIO_ANEXO") != null ? MeLanbide60Utils.redondearDecimalesString(rs.getBigDecimal("SALARIO_ANEXO"), 2).replaceAll("\\.", ",") : "-");
        fila.setContratoInicio(rs.getDate("CME_OFE_FEC_INI") != null ? format.format(rs.getDate("CME_OFE_FEC_INI")) : "-");
        if(rs.getDate("CME_OFE_FEC_BAJA") != null)
            fila.setFinPeriodoSubv(rs.getDate("CME_OFE_FEC_BAJA") != null ? format.format(rs.getDate("CME_OFE_FEC_BAJA")) : "-");
        else
            fila.setFinPeriodoSubv(rs.getDate("CME_OFE_FEC_FIN") != null ? format.format(rs.getDate("CME_OFE_FEC_FIN")) : "-");
        fila.setMesesSubv(rs.getString("CME_JUS_MESES_EXT"));
        fila.setMotivoBaja(rs.getString("MOTIVO_BAJA"));
        fila.setBonif(rs.getString("BONIFICACIONES"));
        fila.setDietasJus(rs.getString("DIETAS_ABONADAS_PERIODO"));
        fila.setMesesExt(rs.getString("CME_JUS_MESES_EXT"));
        fila.setGastosAbonados(rs.getString("VIS_SEG_ABONADO"));
        fila.setImporteJus(rs.getString("CME_JUS_IMP_JUSTIFICADO"));
        fila.setMaxSubvencion(rs.getBigDecimal("SALARIO_PERIODO") != null ? MeLanbide60Utils.redondearDecimalesString(rs.getBigDecimal("SALARIO_PERIODO"), 2).replaceAll("\\.", ",") : "-");
        fila.setContratoBonif(rs.getBigDecimal("BONIFICACIONES") != null ? MeLanbide60Utils.redondearDecimalesString(rs.getBigDecimal("BONIFICACIONES"), 2).replaceAll("\\.", ",") : "-");
        fila.setDietasMaxSubv(rs.getBigDecimal("MAX_SUB_DIETAS") != null ? MeLanbide60Utils.redondearDecimalesString(rs.getBigDecimal("MAX_SUB_DIETAS"), 2).replaceAll("\\.", ",") : "-");
        fila.setSeguroMedico(rs.getBigDecimal("VIS_SEG_SUBV") != null ? MeLanbide60Utils.redondearDecimalesString(rs.getBigDecimal("VIS_SEG_SUBV"), 2).replaceAll("\\.", ",") : "-");
        
        
        return fila;
    }
}
