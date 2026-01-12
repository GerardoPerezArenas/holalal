/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide35.util;

import es.altia.flexia.integracion.moduloexterno.melanbide35.exception.DatoExcelNoValidoException;
import es.altia.flexia.integracion.moduloexterno.melanbide35.exception.ExcelRowMappingException;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.comun.Eca23ConfiguracionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.comun.EcaConfiguracionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.EcaInsPreparadoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.EcaJusPreparadoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.EcaJusProspectoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.EcaSegPreparadores2016VO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.EcaSegPreparadoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.EcaVisProspectoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.FilaInsPreparadoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.FilaPreparadorJustificacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.FilaProspectorJustificacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.FilaSegPreparadoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.FilaVisProspectoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.JusInsercionesECA23VO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.JusPreparadoresECA23VO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.JusSeguimientosECA23VO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.JustificacionECA23VO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.resumen.EcaResPreparadoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.resumen.EcaResProspectoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.resumen.EcaResumenVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.resumen.FilaEcaResPreparadoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.resumen.FilaEcaResProspectoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.solicitud.DatosAnexosVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.solicitud.EcaSolPreparadoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.solicitud.EcaSolProspectoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.solicitud.EcaSolValoracionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.solicitud.EcaSolicitud23VO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.solicitud.EcaSolicitudVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.solicitud.FilaInsercionesECA23VO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.solicitud.FilaPreparadorECA23VO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.solicitud.FilaPreparadorSolicitudVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.solicitud.FilaProspectorECA23VO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.solicitud.FilaProspectorSolicitudVO;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;

/**
 *
 * @author SantiagoC
 */
public class MeLanbide35MappingUtils {

    private static MeLanbide35MappingUtils instance = null;
    private static Logger log = LogManager.getLogger(MeLanbide35MappingUtils.class);
    private final SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");

    private MeLanbide35MappingUtils() {

    }

    public static MeLanbide35MappingUtils getInstance() {
        if (instance == null) {
            synchronized (MeLanbide35MappingUtils.class) {
                instance = new MeLanbide35MappingUtils();
            }
        }
        return instance;
    }

    public Object map(ResultSet rs, Class clazz) throws Exception {
        if (clazz == EcaSolicitudVO.class) {
            return mapearEcaSolicitudVO(rs);
        } else if (clazz == EcaSolPreparadoresVO.class) {
            return mapearEcaSolPreparadoresVO(rs);
        } else if (clazz == EcaSolProspectoresVO.class) {
            return mapearEcaSolProspectoresVO(rs);
        } else if (clazz == EcaSolValoracionVO.class) {
            return mapearEcaSolValoracionVO(rs);
        } else if (clazz == FilaPreparadorSolicitudVO.class) {
            return mapearFilaPreparadorSolicitudVO(rs);
        } else if (clazz == FilaProspectorSolicitudVO.class) {
            return mapearFilaProspectorSolicitudVO(rs);
        } else if (clazz == DatosAnexosVO.class) {
            return mapearDatosAnexosVO(rs);
        } else if (clazz == EcaConfiguracionVO.class) {
            return mapearEcaConfiguracionVO(rs);
        } else if (clazz == FilaPreparadorJustificacionVO.class) {
            return mapearFilaPreparadorJustificacionVO(rs);
        } else if (clazz == EcaJusPreparadoresVO.class) {
            return mapearEcaJusPreparadoresVO(rs);
        } else if (clazz == FilaSegPreparadoresVO.class) {
            return mapearFilaSegPreparadoresVO(rs);
        } else if (clazz == FilaInsPreparadoresVO.class) {
            return mapearFilaInsPreparadoresVO(rs);
        } else if (clazz == EcaSegPreparadoresVO.class) {
            return mapearEcaSegPreparadoresVO(rs);
        } else if (clazz == es.altia.flexia.integracion.moduloexterno.melanbide44.vo.justificacion.EcaSegPreparadores2016VO.class) {
            return mapearEcaSegPreparadoresVO_2016(rs);
        } else if (clazz == FilaProspectorJustificacionVO.class) {
            return mapearFilaProspectorJustificacionVO(rs);
        } else if (clazz == EcaJusProspectoresVO.class) {
            return mapearEcaJusProspectoresVO(rs);
        } else if (clazz == FilaVisProspectoresVO.class) {
            return mapearFilaVisProspectoresVO(rs);
        } else if (clazz == EcaVisProspectoresVO.class) {
            return mapearEcaVisProspectoresVO(rs);
        } else if (clazz == EcaResPreparadoresVO.class) {
            return mapearEcaResPreparadoresVO(rs);
        } else if (clazz == EcaResProspectoresVO.class) {
            return mapearEcaResProspectoresVO(rs);
        } else if (clazz == FilaEcaResPreparadoresVO.class) {
            return mapearFilaEcaResPreparadoresVO(rs);
        } else if (clazz == FilaEcaResProspectoresVO.class) {
            return mapearFilaEcaResProspectoresVO(rs);
        } else if (clazz == EcaResumenVO.class) {
            return mapearEcaResumenVO(rs);
        } else if (clazz == FilaInsercionesECA23VO.class) {
            return mapearEca23SolInsercionesVO(rs);
        } else if (clazz == EcaSolicitud23VO.class) {
            return mapearEca23SolicitudVO(rs);
        } else if (clazz == FilaProspectorECA23VO.class) {
            return mapearEca23SolProspectoresVO(rs);
        } else if (clazz == FilaPreparadorECA23VO.class) {
            return mapearEca23SolPreparadoresVO(rs);
        } else if (clazz == Eca23ConfiguracionVO.class) {
            return mapearEca23ConfiguracionVO(rs);
        } else if (clazz == JusPreparadoresECA23VO.class) {
            return mapearJusPrepECA23(rs);
        } else if (clazz == JusInsercionesECA23VO.class) {
            return mapearJusInsECA23(rs);
        } else if (clazz == JusSeguimientosECA23VO.class) {
            return mapearJusSegECA23(rs);
        }
        return null;
    }

    public Object map(HSSFRow row, Class clazz) throws Exception {
        if (clazz == EcaSolPreparadoresVO.class) {
            return mapearEcaSolPreparadoresVO(row);
        } else if (clazz == EcaSolProspectoresVO.class) {
            return mapearEcaSolProspectoresVO(row);
        } else if (clazz == FilaPreparadorSolicitudVO.class) {
            return mapearFilaPreparadorSolicitudVO(row);
        } else if (clazz == EcaSegPreparadoresVO.class) {
            return mapearEcaSegPreparadoresVO(row);
        } else if (clazz == es.altia.flexia.integracion.moduloexterno.melanbide44.vo.justificacion.EcaSegPreparadores2016VO.class) {
            return mapearEcaSegPreparadoresVO_2016(row);
        } else if (clazz == EcaInsPreparadoresVO.class) {
            return mapearEcaInsPreparadoresVO(row);
        } else if (clazz == EcaVisProspectoresVO.class) {
            return mapearEcaVisProspectoresVO(row);
        }
        return null;
    }

    ///////////////////////////////////
    ///////     2023          /////////
    ///////////////////////////////////
    private FilaInsercionesECA23VO mapearEca23SolInsercionesVO(ResultSet rs) throws Exception {
        FilaInsercionesECA23VO insercion = new FilaInsercionesECA23VO();
        insercion.setId(rs.getInt("ID"));
        insercion.setNumeroExpediente(rs.getString("NUMEROEXPEDIENTE"));
        insercion.setTipoDiscapacidad(rs.getString("TIPODISCAPACIDAD"));
        insercion.setTipoSexoEdad(rs.getString("TIPOPORSEXO"));
        insercion.setNumeroPersonas(rs.getInt("NUMPERSONAS"));
        insercion.setPorcentajeTrabajo(rs.getBigDecimal("PORCTRABAJO"));
        insercion.setImporteCalculadoUnAnio(rs.getBigDecimal("IMPORTECAL1ANO"));
        insercion.setImporteSolicitado(rs.getBigDecimal("IMPORTESOLIC"));

        return insercion;
    }

    private Eca23ConfiguracionVO mapearEca23ConfiguracionVO(ResultSet rs) throws Exception {
        Eca23ConfiguracionVO configuracion = new Eca23ConfiguracionVO();
        configuracion.setAno(rs.getInt("ANO"));
        configuracion.setcAh(rs.getBigDecimal("CA_H"));
        configuracion.setcAm(rs.getBigDecimal("CA_M"));
        configuracion.setcAh45(rs.getBigDecimal("CA_H45"));
        configuracion.setcBh(rs.getBigDecimal("CB_H"));
        configuracion.setcBm(rs.getBigDecimal("CB_M"));
        configuracion.setcBh45(rs.getBigDecimal("CB_H45"));
        configuracion.setcCh(rs.getBigDecimal("CC_H"));
        configuracion.setcCm(rs.getBigDecimal("CC_M"));
        configuracion.setcCh45(rs.getBigDecimal("CC_H45"));
        configuracion.setcDh(rs.getBigDecimal("CD_H"));
        configuracion.setcDm(rs.getBigDecimal("CD_M"));
        configuracion.setcDh45(rs.getBigDecimal("CD_H45"));
        configuracion.setMaximoSeguimientos(rs.getInt("MAX_SEG"));
        configuracion.setImporteSeguimientos(rs.getBigDecimal("IMP_SEG"));

        return configuracion;
    }

    private EcaSolicitud23VO mapearEca23SolicitudVO(ResultSet rs) throws Exception {
        EcaSolicitud23VO solicitud = new EcaSolicitud23VO();
        solicitud.setNumeroExpediente(rs.getString("NUMEROEXPEDIENTE"));
        solicitud.setTotalNumeroPersonas(rs.getInt("TOTALSEG"));
        solicitud.setTotalNumeroPersonasInsercion(rs.getInt("TOTALNUMPERSONAS"));
        solicitud.setTotalImporteInserciones(rs.getBigDecimal("TOTALIMPORTESOLIC"));
        solicitud.setMujeresSeguimiento(rs.getInt("MUJERESSEG"));
        solicitud.setHombresSeguimiento(rs.getInt("HOMBRESSEG"));
        solicitud.setCuantiaSolicitada(rs.getBigDecimal("CUANTIASOLIC"));
        solicitud.setImporteSolicitadoSeguimiento(rs.getBigDecimal("IMPORTESOLICSEG"));
        solicitud.setHorasConvenio(rs.getBigDecimal("HORASJORNCOMPPREP"));
        solicitud.setSumatoriaHorasTotales(rs.getBigDecimal("SUMHORASPREP"));
        solicitud.setTotalImporteCalculadoUnAnio(rs.getBigDecimal("TOTALIMPCAL1ANO"));

        return solicitud;
    }

    private FilaProspectorECA23VO mapearEca23SolProspectoresVO(ResultSet rs) throws Exception {
        FilaProspectorECA23VO prospector = new FilaProspectorECA23VO();
        prospector.setId(rs.getInt("ID"));
        prospector.setNumeroExpediente(rs.getString("NUMEROEXPEDIENTE"));
        prospector.setNombre(rs.getString("NOMBRE"));
        prospector.setDni(rs.getString("DNI"));

        return prospector;
    }

    private FilaPreparadorECA23VO mapearEca23SolPreparadoresVO(ResultSet rs) throws Exception {
        FilaPreparadorECA23VO preparador = new FilaPreparadorECA23VO();
        preparador.setId(rs.getInt("ID"));
        preparador.setNumeroExpediente(rs.getString("NUMEROEXPEDIENTE"));
        preparador.setNombre(rs.getString("NOMBRE"));
        preparador.setDni(rs.getString("DNI"));
        preparador.setHorasECA(rs.getBigDecimal("HORAS_ANIO"));

        return preparador;
    }

    private JusInsercionesECA23VO mapearJusInsECA23(ResultSet rs) throws Exception {
        JusInsercionesECA23VO jus = new JusInsercionesECA23VO();
        jus.setId(rs.getInt("ID"));
        jus.setNumExpediente(rs.getString("NUM_EXP"));
        jus.setDni(rs.getString("DNI"));
        jus.setNombre(rs.getString("NOMBRE"));
        jus.setApellido1(rs.getString("APELLIDO1"));
        jus.setApellido2(rs.getString("APELLIDO2") != null && !rs.getString("APELLIDO2").isEmpty() ? rs.getString("APELLIDO2") : "");
        jus.setSexo(rs.getString("SEXO"));
        jus.setDescSexo(rs.getString("DES_SEXO"));
        jus.setfNacimiento(rs.getDate("FEC_NAC"));
        jus.setfNacimientoStr(formatoFecha.format(jus.getfNacimiento()));
        jus.setTipoDisc(rs.getString("TIPODISC"));
        jus.setDescTipoDisc(rs.getString("DES_DIS"));
        jus.setGrado(rs.getDouble("GRADO"));
        jus.setColectivo(rs.getString("COLECTIVO"));
        jus.setDescColectivo(rs.getString("DES_COL"));
        jus.setTipoContrato(rs.getString("TIPOCONTRATO"));
        jus.setDescTipoContrato(rs.getString("DES_CON"));
        jus.setJornada(rs.getDouble("JORNADA"));
        jus.setfInicio(rs.getDate("FEC_INICIO"));
        jus.setfIniciostr(formatoFecha.format(jus.getfInicio()));
        jus.setTipoEdadSexo(rs.getString("TIPO_EDAD_SEXO"));
        jus.setDescTipoEdadSexo(rs.getString("DES_TSE"));
        jus.setfFin(rs.getDate("FEC_FIN") != null ? rs.getDate("FEC_FIN") : null);
        jus.setfFinStr(jus.getfFin() != null ? formatoFecha.format(jus.getfFin()) : "");
        jus.setDias(rs.getInt("DIAS"));
        jus.setEdad(rs.getInt("EDAD"));
        jus.setEmpresa(rs.getString("EMPRESA"));
        jus.setNifEmpresa(rs.getString("NIF_EMPRESA"));
        jus.setCnae(rs.getString("CNAE"));
        jus.setDescCnaeC(rs.getString("CNAE_C"));
        jus.setDescCnaeE(rs.getString("CNAE_E"));
        jus.setNifPreparador(rs.getString("NIF_PREPARADOR"));
        jus.setImporteInser(rs.getDouble("IMPORTE_INSER"));

        return jus;
    }

    private JusPreparadoresECA23VO mapearJusPrepECA23(ResultSet rs) throws Exception {
        JusPreparadoresECA23VO jus = new JusPreparadoresECA23VO();
        jus.setId(rs.getInt("ID"));
        jus.setNumExpediente(rs.getString("NUM_EXP"));
        jus.setNifPreparador(rs.getString("NIF_PREPARADOR"));
        jus.setJornada(rs.getDouble("JORNADA"));
        jus.setPermitidos(rs.getInt("PERMITIDOS"));
        jus.setImportePrep(rs.getDouble("IMPORTE_PREP"));

        return jus;
    }

    private JusSeguimientosECA23VO mapearJusSegECA23(ResultSet rs) throws Exception {
        JusSeguimientosECA23VO jus = new JusSeguimientosECA23VO();
        jus.setId(rs.getInt("ID"));
        jus.setNumExpediente(rs.getString("NUM_EXP"));
        jus.setNifPreparador(rs.getString("NIF_PREPARADOR"));
        jus.setDni(rs.getString("DNI"));
        jus.setNombre(rs.getString("NOMBRE"));
        jus.setApellido1(rs.getString("APELLIDO1"));
        jus.setApellido2(rs.getString("APELLIDO2") != null && !rs.getString("APELLIDO2").isEmpty() ? rs.getString("APELLIDO2") : "");
        jus.setSexo(rs.getString("SEXO"));
        jus.setDescSexo(rs.getString("DES_SEXO"));
        jus.setfNacimiento(rs.getDate("FEC_NAC"));
        jus.setfNacimientoStr(formatoFecha.format(jus.getfNacimiento()));
        jus.setTipoDisc(rs.getString("TIPODISC"));
        jus.setDescTipoDisc(rs.getString("DES_DIS"));
        jus.setGrado(rs.getDouble("GRADO"));
        jus.setColectivo(rs.getString("COLECTIVO"));
        jus.setDescColectivo(rs.getString("DES_COL"));
        jus.setTipoContrato(rs.getString("TIPOCONTRATO"));
        jus.setDescTipoContrato(rs.getString("DES_CON"));
        jus.setJornada(rs.getDouble("JORNADA"));
        jus.setfInicio(rs.getDate("FEC_INICIO"));
        jus.setfIniciostr(formatoFecha.format(jus.getfInicio()));
        jus.setTipoEdadSexo(rs.getString("TIPO_EDAD_SEXO"));
        jus.setDescTipoEdadSexo(rs.getString("DES_TSE"));
        jus.setfFin(rs.getDate("FEC_FIN") != null ? rs.getDate("FEC_FIN") : null);
        jus.setfFinStr(jus.getfFin() != null ? formatoFecha.format(jus.getfFin()) : "");
        jus.setEmpresa(rs.getString("EMPRESA"));
        jus.setNifEmpresa(rs.getString("NIF_EMPRESA"));
        jus.setCnae(rs.getString("CNAE"));
        jus.setDescCnaeC(rs.getString("CNAE_C"));
        jus.setDescCnaeE(rs.getString("CNAE_E"));
        jus.setImporteSegui(rs.getDouble("IMPORTE_SEGUI"));

        return jus;
    }

    ///////////////////////////////////
    ///////     ANTERIORES      ///////
    ///////////////////////////////////
    public FilaPreparadorSolicitudVO fromPreparadorVOToFilaPreparadorVO(EcaSolPreparadoresVO prep) {
        FilaPreparadorSolicitudVO fila = new FilaPreparadorSolicitudVO();
        //fila.setApellidos(null);
        fila.setC1Total(prep.getInsC1() != null ? prep.getInsC1().toPlainString().replaceAll("\\.", ",") : "");
        fila.setC1h(prep.getInsC1H() != null ? prep.getInsC1H().toPlainString().replaceAll("\\.", ",") : "");
        fila.setC1m(prep.getInsC1M() != null ? prep.getInsC1M().toPlainString().replaceAll("\\.", ",") : "");
        fila.setC2Total(prep.getInsC2() != null ? prep.getInsC2().toPlainString().replaceAll("\\.", ",") : "");
        fila.setC2h(prep.getInsC2H() != null ? prep.getInsC2H().toPlainString().replaceAll("\\.", ",") : "");
        fila.setC2m(prep.getInsC2M() != null ? prep.getInsC2M().toPlainString().replaceAll("\\.", ",") : "");
        fila.setC3Total(prep.getInsC3() != null ? prep.getInsC3().toPlainString().replaceAll("\\.", ",") : "");
        fila.setC3h(prep.getInsC3H() != null ? prep.getInsC3H().toPlainString().replaceAll("\\.", ",") : "");
        fila.setC3m(prep.getInsC3M() != null ? prep.getInsC3M().toPlainString().replaceAll("\\.", ",") : "");
        fila.setC4Total(prep.getInsC4() != null ? prep.getInsC4().toPlainString().replaceAll("\\.", ",") : "");
        fila.setC4h(prep.getInsC4H() != null ? prep.getInsC4H().toPlainString().replaceAll("\\.", ",") : "");
        fila.setC4m(prep.getInsC4M() != null ? prep.getInsC4M().toPlainString().replaceAll("\\.", ",") : "");
        fila.setCostesSalarialesSS(prep.getCoste() != null ? prep.getCoste().toPlainString().replaceAll("\\.", ",") : "");
        fila.setCostesSalarialesSSEca(prep.getImpSSECA() != null ? prep.getImpSSECA().toPlainString().replaceAll("\\.", ",") : "");
        fila.setCostesSalarialesSSJor(prep.getImpSSJC() != null ? prep.getImpSSJC().toPlainString().replaceAll("\\.", ",") : "");
        fila.setCostesSalarialesSSPorJor(prep.getImpSSJR() != null ? prep.getImpSSJR().toPlainString().replaceAll("\\.", ",") : "");
        fila.setFechaFin(prep.getFecFin() != null ? formatoFecha.format(prep.getFecFin()) : "");
        fila.setFechaInicio(prep.getFecIni() != null ? formatoFecha.format(prep.getFecIni()) : "");
        fila.setHorasAnuales(prep.getHorasJC() != null ? prep.getHorasJC().toPlainString().replaceAll("\\.", ",") : "");
        fila.setHorasContrato(prep.getHorasCont() != null ? prep.getHorasCont().toPlainString().replaceAll("\\.", ",") : "");
        fila.setHorasDedicacionECA(prep.getHorasEca() != null ? prep.getHorasEca().toPlainString().replaceAll("\\.", ",") : "");
        fila.setId(prep.getSolPreparadoresCod() != null ? prep.getSolPreparadoresCod().toString() : "");
        fila.setImporte(prep.getImpSegAnt() != null ? prep.getImpSegAnt().toPlainString().replaceAll("\\.", ",") : "");
        fila.setInserciones(prep.getInsImporte() != null ? prep.getInsImporte().toPlainString().replaceAll("\\.", ",") : "");
        fila.setNif(prep.getNif() != null ? prep.getNif().toUpperCase() : "");
        fila.setNombreApel(prep.getNombre() != null ? prep.getNombre().toUpperCase() : "");
        fila.setNumSegAnteriores(prep.getSegAnt() != null ? prep.getSegAnt().toString().replaceAll("\\.", ",") : "");
        fila.setSeguimientosInserciones(prep.getInsSegImporte() != null ? prep.getInsSegImporte().toPlainString().replaceAll("\\.", ",") : "");
        fila.setTipoSust(prep.getTipoSust());
        fila.setSolPreparadorOrigen(prep.getSolPreparadorOrigen());
        return fila;
    }

    public BigDecimal fromFormateadoToDecimal(String numformateado) {
        BigDecimal numero = BigDecimal.ZERO;
        numero = new BigDecimal(numformateado.replace(".", "").replace(",", "."));
        return numero;
    }

    public EcaSolPreparadoresVO fromFilaPreparadorVOToPreparadorVO(FilaPreparadorSolicitudVO fila) throws Exception {

        EcaSolPreparadoresVO prep = new EcaSolPreparadoresVO();
        prep.setCoste(fila.getCostesSalarialesSS() != null && !fila.getCostesSalarialesSS().equals("") && !fila.getCostesSalarialesSS().equals("-") ? fromFormateadoToDecimal(fila.getCostesSalarialesSS()) : null);
        prep.setFecFin(fila.getFechaFin() != null && !fila.getFechaFin().equals("") && !fila.getFechaFin().equals("-") ? formatoFecha.parse(fila.getFechaFin()) : null);
        prep.setFecIni(fila.getFechaInicio() != null && !fila.getFechaInicio().equals("") && !fila.getFechaInicio().equals("-") ? formatoFecha.parse(fila.getFechaInicio()) : null);
        prep.setHorasCont(fila.getHorasContrato() != null && !fila.getHorasContrato().equals("") && !fila.getHorasContrato().equals("-") ? fromFormateadoToDecimal(fila.getHorasContrato()) : null);
        prep.setHorasEca(fila.getHorasDedicacionECA() != null && !fila.getHorasDedicacionECA().equals("") && !fila.getHorasDedicacionECA().equals("-") ? fromFormateadoToDecimal(fila.getHorasDedicacionECA()) : null);
        prep.setHorasJC(fila.getHorasAnuales() != null && !fila.getHorasAnuales().equals("") && !fila.getHorasAnuales().equals("-") ? fromFormateadoToDecimal(fila.getHorasAnuales()) : null);
        prep.setImpSSECA(fila.getCostesSalarialesSSEca() != null && !fila.getCostesSalarialesSSEca().equals("") && !fila.getCostesSalarialesSSEca().equals("-") ? fromFormateadoToDecimal(fila.getCostesSalarialesSSEca()) : null);
        prep.setImpSSJC(fila.getCostesSalarialesSSJor() != null && !fila.getCostesSalarialesSSJor().equals("") && !fila.getCostesSalarialesSSJor().equals("-") ? fromFormateadoToDecimal(fila.getCostesSalarialesSSJor()) : null);
        prep.setImpSSJR(fila.getCostesSalarialesSSPorJor() != null && !fila.getCostesSalarialesSSPorJor().equals("") && !fila.getCostesSalarialesSSPorJor().equals("-") ? fromFormateadoToDecimal(fila.getCostesSalarialesSSPorJor()) : null);
        prep.setImpSegAnt(fila.getImporte() != null && !fila.getImporte().equals("") && !fila.getImporte().equals("-") ? fromFormateadoToDecimal(fila.getImporte()) : null);
        prep.setInsC1(fila.getC1Total() != null && !fila.getC1Total().equals("") && !fila.getC1Total().equals("-") ? fromFormateadoToDecimal(fila.getC1Total()) : null);
        prep.setInsC1H(fila.getC1h() != null && !fila.getC1h().equals("") && !fila.getC1h().equals("-") ? fromFormateadoToDecimal(fila.getC1h()) : null);
        prep.setInsC1M(fila.getC1m() != null && !fila.getC1m().equals("") && !fila.getC1m().equals("-") ? fromFormateadoToDecimal(fila.getC1m()) : null);
        prep.setInsC2(fila.getC2Total() != null && !fila.getC2Total().equals("") && !fila.getC2Total().equals("-") ? fromFormateadoToDecimal(fila.getC2Total()) : null);
        prep.setInsC2H(fila.getC2h() != null && !fila.getC2h().equals("") && !fila.getC2h().equals("-") ? fromFormateadoToDecimal(fila.getC2h()) : null);
        prep.setInsC2M(fila.getC2m() != null && !fila.getC2m().equals("") && !fila.getC2m().equals("-") ? fromFormateadoToDecimal(fila.getC2m()) : null);
        prep.setInsC3(fila.getC3Total() != null && !fila.getC3Total().equals("") && !fila.getC3Total().equals("-") ? fromFormateadoToDecimal(fila.getC3Total()) : null);
        prep.setInsC3H(fila.getC3h() != null && !fila.getC3h().equals("") && !fila.getC3h().equals("-") ? fromFormateadoToDecimal(fila.getC3h()) : null);
        prep.setInsC3M(fila.getC3m() != null && !fila.getC3m().equals("") && !fila.getC3m().equals("-") ? fromFormateadoToDecimal(fila.getC3m()) : null);
        prep.setInsC4(fila.getC4Total() != null && !fila.getC4Total().equals("") && !fila.getC4Total().equals("-") ? fromFormateadoToDecimal(fila.getC4Total()) : null);
        prep.setInsC4H(fila.getC4h() != null && !fila.getC4h().equals("") && !fila.getC4h().equals("-") ? fromFormateadoToDecimal(fila.getC4h()) : null);
        prep.setInsC4M(fila.getC4m() != null && !fila.getC4m().equals("") && !fila.getC4m().equals("-") ? fromFormateadoToDecimal(fila.getC4m()) : null);
        prep.setInsImporte(fila.getInserciones() != null && !fila.getInserciones().equals("") && !fila.getInserciones().equals("-") ? fromFormateadoToDecimal(fila.getInserciones()) : null);
        prep.setInsSegImporte(fila.getSeguimientosInserciones() != null && !fila.getSeguimientosInserciones().equals("") && !fila.getSeguimientosInserciones().equals("-") ? fromFormateadoToDecimal(fila.getSeguimientosInserciones()) : null);
        prep.setNif(fila.getNif() != null && !fila.getNif().equals("") && !fila.getNif().equals("-") ? fila.getNif() : "");
        prep.setNombre(fila.getNombreApel() != null && !fila.getNombreApel().equals("") && !fila.getNombreApel().equals("-") ? fila.getNombreApel() : "");
        prep.setSegAnt(fila.getNumSegAnteriores() != null && !fila.getNumSegAnteriores().equals("") && !fila.getNumSegAnteriores().equals("-") ? fromFormateadoToDecimal(fila.getNumSegAnteriores()).intValue() : null);
        prep.setSolPreparadoresCod(fila.getId() != null && !fila.getId().equals("") && !fila.getId().equals("-") ? Integer.parseInt(fila.getId()) : null);
        prep.setTipoSust(fila.getTipoSust());
        prep.setSolPreparadorOrigen(fila.getSolPreparadorOrigen());
        return prep;
    }

    public FilaProspectorSolicitudVO fromProspectorVOToFilaProspectorVO(EcaSolProspectoresVO pros) {
        SimpleDateFormat formatoFecha = new SimpleDateFormat(ConstantesMeLanbide35.FORMATO_FECHA);
        FilaProspectorSolicitudVO fila = new FilaProspectorSolicitudVO();
        fila.setCoste(pros.getCoste() != null ? pros.getCoste().toPlainString().replaceAll("\\.", ",") : "");
        fila.setCostesSalarialesSSEca(pros.getImpSSECA() != null ? pros.getImpSSECA().toPlainString().replaceAll("\\.", ",") : "");
        fila.setCostesSalarialesSSJor(pros.getImpSSJC() != null ? pros.getImpSSJC().toPlainString().replaceAll("\\.", ",") : "");
        fila.setCostesSalarialesSSPorJor(pros.getImpSSJR() != null ? pros.getImpSSJR().toPlainString().replaceAll("\\.", ",") : "");
        fila.setFechaFin(pros.getFecFin() != null ? formatoFecha.format(pros.getFecFin()) : "");
        fila.setFechaInicio(pros.getFecIni() != null ? formatoFecha.format(pros.getFecIni()) : "");
        fila.setHorasAnuales(pros.getHorasJC() != null ? pros.getHorasJC().toPlainString().replaceAll("\\.", ",") : "");
        fila.setHorasContrato(pros.getHorasCont() != null ? pros.getHorasCont().toPlainString().replaceAll("\\.", ",") : "");
        fila.setHorasDedicacionECA(pros.getHorasEca() != null ? pros.getHorasEca().toPlainString().replaceAll("\\.", ",") : "");
        fila.setId(pros.getSolProspectoresCod() != null ? pros.getSolProspectoresCod().toString() : "");
        fila.setNif(pros.getNif() != null ? pros.getNif().toUpperCase() : "");
        fila.setNombreApel(pros.getNombre() != null ? pros.getNombre().toUpperCase() : "");
        fila.setVisitas(pros.getVisitas() != null ? pros.getVisitas().toString() : "");
        fila.setVisitasImp(pros.getVisitasImp() != null ? pros.getVisitasImp().toPlainString().replaceAll("\\.", ",") : "");
        fila.setTipoSust(pros.getTipoSust());
        fila.setSolProspectorOrigen(pros.getSolProspectorOrigen());
        return fila;
    }

    public EcaSolProspectoresVO fromFilaProspectorVOToProspectorVO(FilaProspectorSolicitudVO fila) throws Exception {
        SimpleDateFormat formatoFecha = new SimpleDateFormat(ConstantesMeLanbide35.FORMATO_FECHA);
        EcaSolProspectoresVO pros = new EcaSolProspectoresVO();
        pros.setCoste(fila.getCoste() != null && !fila.getCoste().equals("") && !fila.getCoste().equals("-") ? fromFormateadoToDecimal(fila.getCoste()) : null);
        pros.setFecFin(fila.getFechaFin() != null && !fila.getFechaFin().equals("") && !fila.getFechaFin().equals("-") ? formatoFecha.parse(fila.getFechaFin()) : null);
        pros.setFecIni(fila.getFechaInicio() != null && !fila.getFechaInicio().equals("") && !fila.getFechaInicio().equals("-") ? formatoFecha.parse(fila.getFechaInicio()) : null);
        pros.setHorasCont(fila.getHorasContrato() != null && !fila.getHorasContrato().equals("") && !fila.getHorasContrato().equals("-") ? fromFormateadoToDecimal(fila.getHorasContrato()) : null);
        pros.setHorasEca(fila.getHorasDedicacionECA() != null && !fila.getHorasDedicacionECA().equals("") && !fila.getHorasDedicacionECA().equals("-") ? fromFormateadoToDecimal(fila.getHorasDedicacionECA()) : null);
        pros.setHorasJC(fila.getHorasAnuales() != null && !fila.getHorasAnuales().equals("") && !fila.getHorasAnuales().equals("-") ? fromFormateadoToDecimal(fila.getHorasAnuales()) : null);
        pros.setImpSSECA(fila.getCostesSalarialesSSEca() != null && !fila.getCostesSalarialesSSEca().equals("") && !fila.getCostesSalarialesSSEca().equals("-") ? fromFormateadoToDecimal(fila.getCostesSalarialesSSEca()) : null);
        pros.setImpSSJC(fila.getCostesSalarialesSSJor() != null && !fila.getCostesSalarialesSSJor().equals("") && !fila.getCostesSalarialesSSJor().equals("-") ? fromFormateadoToDecimal(fila.getCostesSalarialesSSJor()) : null);
        pros.setImpSSJR(fila.getCostesSalarialesSSPorJor() != null && !fila.getCostesSalarialesSSPorJor().equals("") && !fila.getCostesSalarialesSSPorJor().equals("-") ? fromFormateadoToDecimal(fila.getCostesSalarialesSSPorJor()) : null);
        pros.setNif(fila.getNif() != null && !fila.getNif().equals("") && !fila.getNif().equals("-") ? fila.getNif() : "");
        pros.setNombre(fila.getNombreApel() != null && !fila.getNombreApel().equals("") && !fila.getNombreApel().equals("-") ? fila.getNombreApel() : "");
        pros.setSolProspectoresCod(fila.getId() != null && !fila.getId().equals("") && !fila.getId().equals("-") ? Integer.parseInt(fila.getId()) : null);
        pros.setVisitas(fila.getVisitas() != null && !fila.getVisitas().equals("") && !fila.getVisitas().equals("-") ? fromFormateadoToDecimal(fila.getVisitas()).intValue() : null);
        pros.setVisitasImp(fila.getVisitasImp() != null && !fila.getVisitasImp().equals("") && !fila.getVisitasImp().equals("-") ? fromFormateadoToDecimal(fila.getVisitasImp()) : null);
        pros.setTipoSust(fila.getTipoSust());
        pros.setSolProspectorOrigen(fila.getSolProspectorOrigen());
        return pros;
    }

    private EcaSolicitudVO mapearEcaSolicitudVO(ResultSet rs) throws Exception {
        EcaSolicitudVO sol = new EcaSolicitudVO();
        sol.setSolicitudCod(rs.getInt("ECA_SOLICITUD_COD"));
        if (rs.wasNull()) {
            sol.setSolicitudCod(null);
        }
        sol.setNumExp(rs.getString("ECA_NUMEXP"));
        sol.setExpEje(rs.getInt("ECA_EXP_EJE"));
        if (rs.wasNull()) {
            sol.setExpEje(null);
        }
        sol.setInserC1H(rs.getFloat("ECA_INSER_C1_H"));
        if (rs.wasNull()) {
            sol.setInserC1H(null);
        }
        sol.setInserC1M(rs.getFloat("ECA_INSER_C1_M"));
        if (rs.wasNull()) {
            sol.setInserC1M(null);
        }
        sol.setInserC2H(rs.getFloat("ECA_INSER_C2_H"));
        if (rs.wasNull()) {
            sol.setInserC2H(null);
        }
        sol.setInserC2M(rs.getFloat("ECA_INSER_C2_M"));
        if (rs.wasNull()) {
            sol.setInserC2M(null);
        }
        sol.setInserC3H(rs.getFloat("ECA_INSER_C3_H"));
        if (rs.wasNull()) {
            sol.setInserC3H(null);
        }
        sol.setInserC3M(rs.getFloat("ECA_INSER_C3_M"));
        if (rs.wasNull()) {
            sol.setInserC3M(null);
        }
        sol.setInserC4H(rs.getFloat("ECA_INSER_C4_H"));
        if (rs.wasNull()) {
            sol.setInserC4H(null);
        }
        sol.setInserC4M(rs.getFloat("ECA_INSER_C4_M"));
        if (rs.wasNull()) {
            sol.setInserC4M(null);
        }
        sol.setSegH(rs.getInt("ECA_SEG_H"));
        if (rs.wasNull()) {
            sol.setSegH(null);
        }
        sol.setSegM(rs.getInt("ECA_SEG_M"));
        if (rs.wasNull()) {
            sol.setSegM(null);
        }
        sol.setSegActuaciones(rs.getBigDecimal("ECA_SEG_ACTUACIONES"));
        sol.setProspectoresNum(rs.getInt("ECA_PROSPECTORES_NUM"));
        if (rs.wasNull()) {
            sol.setProspectoresNum(null);
        }
        sol.setProspectoresImp(rs.getBigDecimal("ECA_PROSPECTORES_IMP"));
        sol.setPreparadoresNum(rs.getInt("ECA_PREPARADORES_NUM"));
        if (rs.wasNull()) {
            sol.setPreparadoresNum(null);
        }
        sol.setPreparadoresImp(rs.getBigDecimal("ECA_PREPARADORES_IMP"));
        sol.setGastos(rs.getBigDecimal("ECA_GASTOS"));
        int otrasSub = rs.getInt("ECA_OTRAS_SUB");
        if (rs.wasNull()) {
            otrasSub = 0;
        }
        sol.setOtrasSub(otrasSub == 1 ? true : false);
        sol.setSubPub(rs.getBigDecimal("ECA_SUB_PUB"));
        sol.setSubPriv(rs.getBigDecimal("ECA_SUB_PRIV"));
        sol.setTotalSubvencion(rs.getBigDecimal("ECA_TOTAL_SUBVENCION"));
        sol.setTotalAprobado(rs.getBigDecimal("ECA_TOTAL_APROBADO"));

        //CONCEDIDO
        sol.setInserC1HConc(rs.getFloat("ECA_INSER_C1_H_CONC"));
        if (rs.wasNull()) {
            sol.setInserC1HConc(null);
        }
        sol.setInserC1MConc(rs.getFloat("ECA_INSER_C1_M_CONC"));
        if (rs.wasNull()) {
            sol.setInserC1MConc(null);
        }
        sol.setInserC2HConc(rs.getFloat("ECA_INSER_C2_H_CONC"));
        if (rs.wasNull()) {
            sol.setInserC2HConc(null);
        }
        sol.setInserC2MConc(rs.getFloat("ECA_INSER_C2_M_CONC"));
        if (rs.wasNull()) {
            sol.setInserC2MConc(null);
        }
        sol.setInserC3HConc(rs.getFloat("ECA_INSER_C3_H_CONC"));
        if (rs.wasNull()) {
            sol.setInserC3HConc(null);
        }
        sol.setInserC3MConc(rs.getFloat("ECA_INSER_C3_M_CONC"));
        if (rs.wasNull()) {
            sol.setInserC3MConc(null);
        }
        sol.setInserC4HConc(rs.getFloat("ECA_INSER_C4_H_CONC"));
        if (rs.wasNull()) {
            sol.setInserC4HConc(null);
        }
        sol.setInserC4MConc(rs.getFloat("ECA_INSER_C4_M_CONC"));
        if (rs.wasNull()) {
            sol.setInserC4MConc(null);
        }
        sol.setSegHConc(rs.getInt("ECA_SEG_H_CONC"));
        if (rs.wasNull()) {
            sol.setSegHConc(null);
        }
        sol.setSegMConc(rs.getInt("ECA_SEG_M_CONC"));
        if (rs.wasNull()) {
            sol.setSegMConc(null);
        }
        sol.setSegActuacionesConc(rs.getBigDecimal("ECA_SEG_ACTUACIONES_CONC"));
        sol.setProspectoresNumConc(rs.getInt("ECA_PROSPECTORES_NUM_CONC"));
        if (rs.wasNull()) {
            sol.setProspectoresNumConc(null);
        }
        sol.setProspectoresImpConc(rs.getBigDecimal("ECA_PROSPECTORES_IMP_CONC"));
        sol.setPreparadoresNumConc(rs.getInt("ECA_PREPARADORES_NUM_CONC"));
        if (rs.wasNull()) {
            sol.setPreparadoresNumConc(null);
        }
        sol.setPreparadoresImpConc(rs.getBigDecimal("ECA_PREPARADORES_IMP_CONC"));
        sol.setGastosConc(rs.getBigDecimal("ECA_GASTOS_CONC"));
        return sol;
    }

    private EcaSolPreparadoresVO mapearEcaSolPreparadoresVO(ResultSet rs) throws Exception {
        EcaSolPreparadoresVO prep = new EcaSolPreparadoresVO();
        prep.setSolPreparadoresCod(rs.getInt("ECA_SOL_PREPARADORES_COD"));
        if (rs.wasNull()) {
            prep.setSolPreparadoresCod(null);
        }
        prep.setNif(rs.getString("ECA_PREP_NIF"));
        prep.setNombre(rs.getString("ECA_PREP_NOMBRE"));
        prep.setFecIni(rs.getDate("ECA_PREP_FECINI"));
        prep.setFecFin(rs.getDate("ECA_PREP_FECFIN"));
        prep.setHorasJC(rs.getBigDecimal("ECA_PREP_HORAS_JC"));
        prep.setHorasCont(rs.getBigDecimal("ECA_PREP_HORAS_CONT"));
        prep.setHorasEca(rs.getBigDecimal("ECA_PREP_HORAS_ECA"));
        prep.setImpSSJC(rs.getBigDecimal("ECA_PREP_IMP_SS_JC"));
        prep.setImpSSJR(rs.getBigDecimal("ECA_PREP_IMP_SS_JR"));
        prep.setImpSSECA(rs.getBigDecimal("ECA_PREP_IMP_SS_ECA"));
        prep.setSegAnt(rs.getInt("ECA_PREP_SEG_ANT"));
        if (rs.wasNull()) {
            prep.setSegAnt(null);
        }
        prep.setImpSegAnt(rs.getBigDecimal("ECA_PREP_IMP_SEG_ANT"));
        prep.setInsC1H(rs.getBigDecimal("ECA_PREP_INS_C1_H"));
        prep.setInsC1M(rs.getBigDecimal("ECA_PREP_INS_C1_M"));
        prep.setInsC1(rs.getBigDecimal("ECA_PREP_INS_C1"));
        prep.setInsC2H(rs.getBigDecimal("ECA_PREP_INS_C2_H"));
        prep.setInsC2M(rs.getBigDecimal("ECA_PREP_INS_C2_M"));
        prep.setInsC2(rs.getBigDecimal("ECA_PREP_INS_C2"));
        prep.setInsC3H(rs.getBigDecimal("ECA_PREP_INS_C3_H"));
        prep.setInsC3M(rs.getBigDecimal("ECA_PREP_INS_C3_M"));
        prep.setInsC3(rs.getBigDecimal("ECA_PREP_INS_C3"));
        prep.setInsC4H(rs.getBigDecimal("ECA_PREP_INS_C4_H"));
        prep.setInsC4M(rs.getBigDecimal("ECA_PREP_INS_C4_M"));
        prep.setInsC4(rs.getBigDecimal("ECA_PREP_INS_C4"));
        prep.setInsImporte(rs.getBigDecimal("ECA_PREP_INS_IMPORTE"));
        prep.setInsSegImporte(rs.getBigDecimal("ECA_PREP_INS_SEG_IMPORTE"));
        prep.setCoste(rs.getBigDecimal("ECA_PREP_COSTE"));
        prep.setImpteConcedido(rs.getBigDecimal("ECA_PREP_IMPTE_CONCEDIDO"));
        prep.setSolicitud(rs.getInt("ECA_PREP_SOLICITUD"));
        if (rs.wasNull()) {
            prep.setSolicitud(null);
        }

        prep.setNif_Carga(rs.getString("ECA_PREP_NIF_CARGA"));
        prep.setNombre_Carga(rs.getString("ECA_PREP_NOMBRE_CARGA"));
        prep.setFecIni_Carga(rs.getDate("ECA_PREP_FECINI_CARGA"));
        prep.setFecFin_Carga(rs.getDate("ECA_PREP_FECFIN_CARGA"));
        prep.setHorasJC_Carga(rs.getBigDecimal("ECA_PREP_HORAS_JC_CARGA"));
        prep.setHorasCont_Carga(rs.getBigDecimal("ECA_PREP_HORAS_CONT_CARGA"));
        prep.setHorasEca_Carga(rs.getBigDecimal("ECA_PREP_HORAS_ECA_CARGA"));
        prep.setImpSSJC_Carga(rs.getBigDecimal("ECA_PREP_IMP_SS_JC_CARGA"));
        prep.setImpSSJR_Carga(rs.getBigDecimal("ECA_PREP_IMP_SS_JR_CARGA"));
        prep.setImpSSECA_Carga(rs.getBigDecimal("ECA_PREP_IMP_SS_ECA_CARGA"));
        prep.setSegAnt_Carga(rs.getInt("ECA_PREP_SEG_ANT_CARGA"));
        if (rs.wasNull()) {
            prep.setSegAnt_Carga(null);
        }
        prep.setImpSegAnt_Carga(rs.getBigDecimal("ECA_PREP_IMP_SEG_ANT_CARGA"));
        prep.setInsC1H_Carga(rs.getBigDecimal("ECA_PREP_INS_C1_H_CARGA"));
        prep.setInsC1M_Carga(rs.getBigDecimal("ECA_PREP_INS_C1_M_CARGA"));
        prep.setInsC1_Carga(rs.getBigDecimal("ECA_PREP_INS_C1_CARGA"));
        prep.setInsC2H_Carga(rs.getBigDecimal("ECA_PREP_INS_C2_H_CARGA"));
        prep.setInsC2M_Carga(rs.getBigDecimal("ECA_PREP_INS_C2_M_CARGA"));
        prep.setInsC2_Carga(rs.getBigDecimal("ECA_PREP_INS_C2_CARGA"));
        prep.setInsC3H_Carga(rs.getBigDecimal("ECA_PREP_INS_C3_H_CARGA"));
        prep.setInsC3M_Carga(rs.getBigDecimal("ECA_PREP_INS_C3_M_CARGA"));
        prep.setInsC3_Carga(rs.getBigDecimal("ECA_PREP_INS_C3_CARGA"));
        prep.setInsC4H_Carga(rs.getBigDecimal("ECA_PREP_INS_C4_H_CARGA"));
        prep.setInsC4M_Carga(rs.getBigDecimal("ECA_PREP_INS_C4_M_CARGA"));
        prep.setInsC4_Carga(rs.getBigDecimal("ECA_PREP_INS_C4_CARGA"));
        prep.setInsImporte_Carga(rs.getBigDecimal("ECA_PREP_INS_IMPORTE_CARGA"));
        prep.setInsSegImporte_Carga(rs.getBigDecimal("ECA_PREP_INS_SEG_IMPORTE_CARGA"));
        prep.setCoste_Carga(rs.getBigDecimal("ECA_PREP_COSTE_CARGA"));
        prep.setTipoSust(rs.getString("ECA_PREP_TIPO_SUST"));
        prep.setSolPreparadorOrigen(rs.getInt("ECA_PREP_CODORIGEN"));
        if (rs.wasNull()) {
            prep.setSolPreparadorOrigen(null);
        }
        return prep;
    }

    private EcaSolProspectoresVO mapearEcaSolProspectoresVO(ResultSet rs) throws Exception {
        EcaSolProspectoresVO pro = new EcaSolProspectoresVO();
        pro.setSolProspectoresCod(rs.getInt("ECA_SOL_PROSPECTORES_COD"));
        if (rs.wasNull()) {
            pro.setSolProspectoresCod(null);
        }
        pro.setNif(rs.getString("ECA_PROS_NIF"));
        pro.setNombre(rs.getString("ECA_PROS_NOMBRE"));
        pro.setFecIni(rs.getDate("ECA_PROS_FECINI"));
        pro.setFecFin(rs.getDate("ECA_PROS_FECFIN"));
        pro.setHorasJC(rs.getBigDecimal("ECA_PROS_HORAS_JC"));
        pro.setHorasCont(rs.getBigDecimal("ECA_PROS_HORAS_CONT"));
        pro.setHorasEca(rs.getBigDecimal("ECA_PROS_HORAS_ECA"));
        pro.setImpSSJC(rs.getBigDecimal("ECA_PROS_IMP_SS_JC"));
        pro.setImpSSJR(rs.getBigDecimal("ECA_PROS_IMP_SS_JR"));
        pro.setImpSSECA(rs.getBigDecimal("ECA_PROS_IMP_SS_ECA"));
        pro.setVisitas(rs.getInt("ECA_PROS_VISITAS"));
        if (rs.wasNull()) {
            pro.setVisitas(null);
        }
        pro.setVisitasImp(rs.getBigDecimal("ECA_PROS_VISITAS_IMP"));
        pro.setCoste(rs.getBigDecimal("ECA_PROS_COSTE"));
        pro.setImpteConcedido(rs.getBigDecimal("ECA_PROS_IMPTE_CONCEDIDO"));
        pro.setSolicitud(rs.getInt("ECA_PROS_SOLICITUD"));
        if (rs.wasNull()) {
            pro.setSolicitud(null);
        }

        pro.setNif_Carga(rs.getString("ECA_PROS_NIF_CARGA"));
        pro.setNombre_Carga(rs.getString("ECA_PROS_NOMBRE_CARGA"));
        pro.setFecIni_Carga(rs.getDate("ECA_PROS_FECINI_CARGA"));
        pro.setFecFin_Carga(rs.getDate("ECA_PROS_FECFIN_CARGA"));
        pro.setHorasJC_Carga(rs.getBigDecimal("ECA_PROS_HORAS_JC_CARGA"));
        pro.setHorasCont_Carga(rs.getBigDecimal("ECA_PROS_HORAS_CONT_CARGA"));
        pro.setHorasEca_Carga(rs.getBigDecimal("ECA_PROS_HORAS_ECA_CARGA"));
        pro.setImpSSJC_Carga(rs.getBigDecimal("ECA_PROS_IMP_SS_JC_CARGA"));
        pro.setImpSSJR_Carga(rs.getBigDecimal("ECA_PROS_IMP_SS_JR_CARGA"));
        pro.setImpSSECA_Carga(rs.getBigDecimal("ECA_PROS_IMP_SS_ECA_CARGA"));
        pro.setVisitas_Carga(rs.getInt("ECA_PROS_VISITAS_CARGA"));
        if (rs.wasNull()) {
            pro.setVisitas_Carga(null);
        }
        pro.setVisitasImp_Carga(rs.getBigDecimal("ECA_PROS_VISITAS_IMP_CARGA"));
        pro.setCoste_Carga(rs.getBigDecimal("ECA_PROS_COSTE_CARGA"));
        pro.setTipoSust(rs.getString("ECA_PROS_TIPO_SUST"));
        pro.setSolProspectorOrigen(rs.getInt("ECA_PROS_CODORIGEN"));
        if (rs.wasNull()) {
            pro.setSolProspectorOrigen(null);
        }
        return pro;
    }

    private EcaSolValoracionVO mapearEcaSolValoracionVO(ResultSet rs) throws Exception {
        EcaSolValoracionVO val = new EcaSolValoracionVO();
        val.setSolValoracionCod(rs.getInt("ECA_SOL_VALORACION_COD"));
        if (rs.wasNull()) {
            val.setSolValoracionCod(null);
        }
        val.setExperienciaNum(rs.getInt("ECA_SOL_VAL_EXPERIENCIA_NUM"));
        if (rs.wasNull()) {
            val.setExperienciaNum(null);
        }
        val.setExperienciaVal(rs.getBigDecimal("ECA_SOL_VAL_EXPERIENCIA_VAL"));
        val.setInsMujeresNum(rs.getBigDecimal("ECA_SOL_VAL_INS_MUJERES_NUM"));
        val.setInsMujeresVal(rs.getBigDecimal("ECA_SOL_VAL_INS_MUJERES_VAL"));
        val.setSensibilidadNum(rs.getInt("ECA_SOL_VAL_SENSIBILIDAD_NUM"));
        if (rs.wasNull()) {
            val.setSensibilidadNum(null);
        }
        val.setSensibilidadVal(rs.getBigDecimal("ECA_SOL_VAL_SENSIBILIDAD_VAL"));
        val.setTotal(rs.getBigDecimal("ECA_SOL_VAL_TOTAL"));
        val.setSolicitud(rs.getInt("ECA_SOL_VAL_SOLICITUD"));
        if (rs.wasNull()) {
            val.setSolicitud(null);
        }
        return val;
    }

    private FilaPreparadorSolicitudVO mapearFilaPreparadorSolicitudVO(ResultSet rs) throws Exception {
        DecimalFormatSymbols simbolo = new DecimalFormatSymbols();
        simbolo.setDecimalSeparator(',');
        simbolo.setGroupingSeparator('.');
        DecimalFormat formateador = new DecimalFormat("#,##0.00", simbolo);

        String valorStr = null;
        Date valorFecha = null;
        BigDecimal valorBD = null;
        SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide35.FORMATO_FECHA);
        FilaPreparadorSolicitudVO fila = new FilaPreparadorSolicitudVO();
        int id = rs.getInt("ECA_SOL_PREPARADORES_COD");
        if (!rs.wasNull()) {
            fila.setId("" + id);
        }
        valorStr = rs.getString("ECA_PREP_NIF");
        fila.setNif(valorStr != null ? valorStr.toUpperCase() : "-");
        valorStr = rs.getString("ECA_PREP_NOMBRE");
        fila.setNombreApel(valorStr != null ? valorStr.toUpperCase() : "-");
        valorFecha = rs.getDate("ECA_PREP_FECINI");
        fila.setFechaInicio(valorFecha != null ? format.format(valorFecha) : "-");
        valorFecha = rs.getDate("ECA_PREP_FECFIN");
        fila.setFechaFin(valorFecha != null ? format.format(valorFecha) : "-");
        valorBD = rs.getBigDecimal("ECA_PREP_HORAS_JC");
        //fila.setHorasAnuales(valorBD != null ? MeLanbide35Utils.redondearDecimalesString(valorBD, 2).replaceAll("\\.", ",") : "-");
        fila.setHorasAnuales(valorBD != null ? formateador.format(new BigDecimal(MeLanbide35Utils.redondearDecimalesString(valorBD, 2))) : "-");
        valorBD = rs.getBigDecimal("ECA_PREP_HORAS_CONT");
        //fila.setHorasContrato(valorBD != null ? MeLanbide35Utils.redondearDecimalesString(valorBD, 2).replaceAll("\\.", ",") : "-");
        fila.setHorasContrato(valorBD != null ? formateador.format(new BigDecimal(MeLanbide35Utils.redondearDecimalesString(valorBD, 2))) : "-");
        valorBD = rs.getBigDecimal("ECA_PREP_HORAS_ECA");
        fila.setHorasDedicacionECA(valorBD != null ? formateador.format(new BigDecimal(MeLanbide35Utils.redondearDecimalesString(valorBD, 2))) : "-");
        valorBD = rs.getBigDecimal("ECA_PREP_IMP_SS_JC");
        fila.setCostesSalarialesSSJor(valorBD != null ? formateador.format(new BigDecimal(MeLanbide35Utils.redondearDecimalesString(valorBD, 2))) : "-");
        valorBD = rs.getBigDecimal("ECA_PREP_IMP_SS_JR");
        fila.setCostesSalarialesSSPorJor(valorBD != null ? formateador.format(new BigDecimal(MeLanbide35Utils.redondearDecimalesString(valorBD, 2))) : "-");
        valorBD = rs.getBigDecimal("ECA_PREP_IMP_SS_ECA");
        fila.setCostesSalarialesSSEca(valorBD != null ? formateador.format(new BigDecimal(MeLanbide35Utils.redondearDecimalesString(valorBD, 2))) : "-");
        valorBD = rs.getBigDecimal("ECA_PREP_SEG_ANT");
        fila.setNumSegAnteriores(valorBD != null ? valorBD.toPlainString() : "-");
        valorBD = rs.getBigDecimal("ECA_PREP_INS_C1_H");
        fila.setC1h(valorBD != null ? formateador.format(new BigDecimal(MeLanbide35Utils.redondearDecimalesString(valorBD, 2))) : "-");
        valorBD = rs.getBigDecimal("ECA_PREP_INS_C1_M");
        fila.setC1m(valorBD != null ? formateador.format(new BigDecimal(MeLanbide35Utils.redondearDecimalesString(valorBD, 2))) : "-");
        valorBD = rs.getBigDecimal("ECA_PREP_INS_C1");
        fila.setC1Total(valorBD != null ? formateador.format(valorBD) : "-");
        valorBD = rs.getBigDecimal("ECA_PREP_INS_C2_H");
        fila.setC2h(valorBD != null ? formateador.format(new BigDecimal(MeLanbide35Utils.redondearDecimalesString(valorBD, 2))) : "-");
        valorBD = rs.getBigDecimal("ECA_PREP_INS_C2_M");
        fila.setC2m(valorBD != null ? formateador.format(new BigDecimal(MeLanbide35Utils.redondearDecimalesString(valorBD, 2))) : "-");
        valorBD = rs.getBigDecimal("ECA_PREP_INS_C2");
        fila.setC2Total(valorBD != null ? formateador.format(valorBD) : "-");
        valorBD = rs.getBigDecimal("ECA_PREP_INS_C3_H");
        fila.setC3h(valorBD != null ? formateador.format(new BigDecimal(MeLanbide35Utils.redondearDecimalesString(valorBD, 2))) : "-");
        valorBD = rs.getBigDecimal("ECA_PREP_INS_C3_M");
        fila.setC3m(valorBD != null ? formateador.format(new BigDecimal(MeLanbide35Utils.redondearDecimalesString(valorBD, 2))) : "-");
        valorBD = rs.getBigDecimal("ECA_PREP_INS_C3");
        fila.setC3Total(valorBD != null ? formateador.format(valorBD) : "-");
        valorBD = rs.getBigDecimal("ECA_PREP_INS_C4_H");
        fila.setC4h(valorBD != null ? formateador.format(new BigDecimal(MeLanbide35Utils.redondearDecimalesString(valorBD, 2))) : "-");
        valorBD = rs.getBigDecimal("ECA_PREP_INS_C4_M");
        fila.setC4m(valorBD != null ? formateador.format(new BigDecimal(MeLanbide35Utils.redondearDecimalesString(valorBD, 2))) : "-");
        valorBD = rs.getBigDecimal("ECA_PREP_INS_C4");
        fila.setC4Total(valorBD != null ? formateador.format(valorBD) : "-");
        valorBD = rs.getBigDecimal("ECA_PREP_INS_SEG_IMPORTE");
        fila.setSeguimientosInserciones(valorBD != null ? formateador.format(new BigDecimal(MeLanbide35Utils.redondearDecimalesString(valorBD, 2))) : "-");
        valorBD = rs.getBigDecimal("ECA_PREP_IMP_SEG_ANT");
        fila.setImporte(valorBD != null ? formateador.format(new BigDecimal(MeLanbide35Utils.redondearDecimalesString(valorBD, 2))) : "-");
        valorBD = rs.getBigDecimal("ECA_PREP_INS_IMPORTE");
        fila.setInserciones(valorBD != null ? formateador.format(valorBD) : "-");
        valorBD = rs.getBigDecimal("ECA_PREP_COSTE");
        fila.setCostesSalarialesSS(valorBD != null ? formateador.format(new BigDecimal(MeLanbide35Utils.redondearDecimalesString(valorBD, 2))) : "-");
        valorBD = rs.getBigDecimal("ECA_PREP_IMPTE_CONCEDIDO");
        fila.setImporteConcedido(valorBD != null ? formateador.format(new BigDecimal(MeLanbide35Utils.redondearDecimalesString(valorBD, 2))) : "-");
        fila.setTipoSust(rs.getString("ECA_PREP_TIPO_SUST"));
        fila.setSolPreparadorOrigen(rs.getInt("ECA_PREP_CODORIGEN"));
        if (rs.wasNull()) {
            fila.setSolPreparadorOrigen(null);
        }
        return fila;
    }

    private FilaProspectorSolicitudVO mapearFilaProspectorSolicitudVO(ResultSet rs) throws Exception {
        DecimalFormatSymbols simbolo = new DecimalFormatSymbols();
        simbolo.setDecimalSeparator(',');
        simbolo.setGroupingSeparator('.');
        DecimalFormat formateador = new DecimalFormat("#,##0.00", simbolo);

        String valorStr = null;
        Date valorFecha = null;
        BigDecimal valorBD = null;
        SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide35.FORMATO_FECHA);
        FilaProspectorSolicitudVO fila = new FilaProspectorSolicitudVO();
        int id = rs.getInt("ECA_SOL_PROSPECTORES_COD");
        if (!rs.wasNull()) {
            fila.setId("" + id);
        }
        valorStr = rs.getString("ECA_PROS_NIF");
        fila.setNif(valorStr != null ? valorStr.toUpperCase() : "-");
        valorStr = rs.getString("ECA_PROS_NOMBRE");
        fila.setNombreApel(valorStr != null ? valorStr.toUpperCase() : "-");
        valorFecha = rs.getDate("ECA_PROS_FECINI");
        fila.setFechaInicio(valorFecha != null ? format.format(valorFecha) : "-");
        valorFecha = rs.getDate("ECA_PROS_FECFIN");
        fila.setFechaFin(valorFecha != null ? format.format(valorFecha) : "-");
        valorBD = rs.getBigDecimal("ECA_PROS_HORAS_JC");
        fila.setHorasAnuales(valorBD != null ? formateador.format(new BigDecimal(MeLanbide35Utils.redondearDecimalesString(valorBD, 2))) : "-");
        valorBD = rs.getBigDecimal("ECA_PROS_HORAS_CONT");
        fila.setHorasContrato(valorBD != null ? formateador.format(new BigDecimal(MeLanbide35Utils.redondearDecimalesString(valorBD, 2))) : "-");
        valorBD = rs.getBigDecimal("ECA_PROS_HORAS_ECA");
        fila.setHorasDedicacionECA(valorBD != null ? formateador.format(new BigDecimal(MeLanbide35Utils.redondearDecimalesString(valorBD, 2))) : "-");
        valorBD = rs.getBigDecimal("ECA_PROS_IMP_SS_JC");
        fila.setCostesSalarialesSSJor(valorBD != null ? formateador.format(new BigDecimal(MeLanbide35Utils.redondearDecimalesString(valorBD, 2))) : "-");
        valorBD = rs.getBigDecimal("ECA_PROS_IMP_SS_JR");
        fila.setCostesSalarialesSSPorJor(valorBD != null ? formateador.format(new BigDecimal(MeLanbide35Utils.redondearDecimalesString(valorBD, 2))) : "-");
        valorBD = rs.getBigDecimal("ECA_PROS_IMP_SS_ECA");
        fila.setCostesSalarialesSSEca(valorBD != null ? formateador.format(new BigDecimal(MeLanbide35Utils.redondearDecimalesString(valorBD, 2))) : "-");
        valorBD = rs.getBigDecimal("ECA_PROS_VISITAS");
        fila.setVisitas(valorBD != null ? valorBD.toPlainString() : "-");
        valorBD = rs.getBigDecimal("ECA_PROS_VISITAS_IMP");
        fila.setVisitasImp(valorBD != null ? formateador.format(new BigDecimal(MeLanbide35Utils.redondearDecimalesString(valorBD, 2))) : "-");
        valorBD = rs.getBigDecimal("ECA_PROS_COSTE");
        fila.setCoste(valorBD != null ? formateador.format(new BigDecimal(MeLanbide35Utils.redondearDecimalesString(valorBD, 2))) : "-");
        valorBD = rs.getBigDecimal("ECA_PROS_IMPTE_CONCEDIDO");
        fila.setImporteConcedido(valorBD != null ? formateador.format(new BigDecimal(MeLanbide35Utils.redondearDecimalesString(valorBD, 2))) : "-");
        fila.setTipoSust(rs.getString("ECA_PROS_TIPO_SUST"));
        fila.setSolProspectorOrigen(rs.getInt("ECA_PROS_CODORIGEN"));
        if (rs.wasNull()) {
            fila.setSolProspectorOrigen(null);
        }
        return fila;
    }

    private FilaPreparadorJustificacionVO mapearFilaPreparadorJustificacionVO(ResultSet rs) throws Exception {
        DecimalFormatSymbols simbolo = new DecimalFormatSymbols();
        simbolo.setDecimalSeparator(',');
        simbolo.setGroupingSeparator('.');
        DecimalFormat formateador = new DecimalFormat("#,##0.00", simbolo);
        DecimalFormat formateadorInserciones = new DecimalFormat("#,##0.0000", simbolo);

        boolean esSustituto = false;
        String valorStr = null;
        Date valorFecha = null;
        BigDecimal valorBD = null;
        BigDecimal jvalorBD = null;
        SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide35.FORMATO_FECHA);
        FilaPreparadorJustificacionVO fila = new FilaPreparadorJustificacionVO();
        int id = rs.getInt("ECA_JUS_PREPARADORES_COD");

        if (!rs.wasNull()) {
            fila.setId("" + id);
            int origen = rs.getInt("ORIGEN");
            if (!rs.wasNull()) {
                if (id != origen) {
                    esSustituto = true;
                }
            }
        }
        valorStr = rs.getString("ECA_PREP_NIF");
        /*if(valorStr != null)
        {
            if(esSustituto)
            {
                valorStr = ""+valorStr+" - (S)";
            }
        }
        else
        {
            valorStr = "-";
        }*/
        fila.setNif(valorStr != null ? valorStr.toUpperCase() : "-");
        valorStr = rs.getString("ECA_PREP_NOMBRE");
        fila.setNombreApel(valorStr != null ? valorStr.toUpperCase() : "-");
        valorFecha = rs.getDate("ECA_PREP_FECINI");
        fila.setFechaInicio(valorFecha != null ? format.format(valorFecha) : "-");
        valorFecha = rs.getDate("ECA_PREP_FECFIN");
        fila.setFechaFin(valorFecha != null ? format.format(valorFecha) : "-");
        valorBD = rs.getBigDecimal("ECA_PREP_HORAS_JC");
        fila.setHorasAnuales(valorBD != null ? formateador.format(new BigDecimal(MeLanbide35Utils.redondearDecimalesString(valorBD, 2))) : "-");
        valorBD = rs.getBigDecimal("ECA_PREP_HORAS_CONT");
        fila.setHorasContrato(valorBD != null ? formateador.format(new BigDecimal(MeLanbide35Utils.redondearDecimalesString(valorBD, 2))) : "-");
        valorBD = rs.getBigDecimal("ECA_PREP_HORAS_ECA");
        fila.setHorasDedicacionECA(valorBD != null ? formateador.format(new BigDecimal(MeLanbide35Utils.redondearDecimalesString(valorBD, 2))) : "-");
        valorBD = rs.getBigDecimal("ECA_PREP_IMP_SS_JC");
        fila.setCostesSalarialesSSJor(valorBD != null ? formateador.format(new BigDecimal(MeLanbide35Utils.redondearDecimalesString(valorBD, 2))) : "-");
        valorBD = rs.getBigDecimal("ECA_PREP_IMP_SS_JR");
        fila.setCostesSalarialesSSPorJor(valorBD != null ? formateador.format(new BigDecimal(MeLanbide35Utils.redondearDecimalesString(valorBD, 2))) : "-");
        valorBD = rs.getBigDecimal("ECA_PREP_IMP_SS_ECA");
        fila.setCostesSalarialesSSEca(valorBD != null ? formateador.format(new BigDecimal(MeLanbide35Utils.redondearDecimalesString(valorBD, 2))) : "-");
        valorBD = rs.getBigDecimal("ECA_PREP_SEG_ANT");
        jvalorBD = rs.getBigDecimal("NUMSEG") != null ? rs.getBigDecimal("NUMSEG") : BigDecimal.ZERO;
        fila.setNumSegAnteriores(valorBD != null ? valorBD.toPlainString().replaceAll("\\.", ",") + " - " + jvalorBD.intValue() : BigDecimal.ZERO.toPlainString() + "-" + jvalorBD.intValue());
        valorBD = rs.getBigDecimal("IMPORTESEGANT");
        fila.setImporte(valorBD != null ? formateador.format(valorBD) : "-");
        valorBD = rs.getBigDecimal("ECA_PREP_INS_C1_H");
        jvalorBD = rs.getBigDecimal("C1H_JUS") != null ? rs.getBigDecimal("C1H_JUS") : BigDecimal.ZERO;
        fila.setC1h(valorBD != null ? valorBD.toPlainString().replaceAll("\\.", ",") + " - " + formateadorInserciones.format(jvalorBD) : BigDecimal.ZERO.toPlainString() + "-" + formateadorInserciones.format(jvalorBD));
        valorBD = rs.getBigDecimal("ECA_PREP_INS_C1_M");
        jvalorBD = rs.getBigDecimal("C1M_JUS") != null ? rs.getBigDecimal("C1M_JUS") : BigDecimal.ZERO;
        fila.setC1m(valorBD != null ? valorBD.toPlainString().replaceAll("\\.", ",") + " - " + formateadorInserciones.format(jvalorBD) : BigDecimal.ZERO.toPlainString() + "-" + formateadorInserciones.format(jvalorBD));
        valorBD = rs.getBigDecimal("ECA_PREP_INS_C1");
        jvalorBD = rs.getBigDecimal("C1_JUS") != null ? rs.getBigDecimal("C1_JUS") : BigDecimal.ZERO;
        fila.setC1Total(valorBD != null ? valorBD.toPlainString().replaceAll("\\.", ",") + " - " + formateadorInserciones.format(jvalorBD) : BigDecimal.ZERO.toPlainString() + "-" + formateadorInserciones.format(jvalorBD));
        valorBD = rs.getBigDecimal("ECA_PREP_INS_C2_H");
        jvalorBD = rs.getBigDecimal("C2H_JUS") != null ? rs.getBigDecimal("C2H_JUS") : BigDecimal.ZERO;
        fila.setC2h(valorBD != null ? valorBD.toPlainString().replaceAll("\\.", ",") + " - " + formateadorInserciones.format(jvalorBD) : BigDecimal.ZERO.toPlainString() + "-" + formateadorInserciones.format(jvalorBD));
        valorBD = rs.getBigDecimal("ECA_PREP_INS_C2_M");
        jvalorBD = rs.getBigDecimal("C2M_JUS") != null ? rs.getBigDecimal("C2M_JUS") : BigDecimal.ZERO;
        fila.setC2m(valorBD != null ? valorBD.toPlainString().replaceAll("\\.", ",") + " - " + formateadorInserciones.format(jvalorBD) : BigDecimal.ZERO.toPlainString() + "-" + formateadorInserciones.format(jvalorBD));
        valorBD = rs.getBigDecimal("ECA_PREP_INS_C2");
        jvalorBD = rs.getBigDecimal("C2_JUS") != null ? rs.getBigDecimal("C2_JUS") : BigDecimal.ZERO;
        fila.setC2Total(valorBD != null ? valorBD.toPlainString().replaceAll("\\.", ",") + " - " + formateadorInserciones.format(jvalorBD) : BigDecimal.ZERO.toPlainString() + "-" + formateadorInserciones.format(jvalorBD));
        valorBD = rs.getBigDecimal("ECA_PREP_INS_C3_H");
        jvalorBD = rs.getBigDecimal("C3H_JUS") != null ? rs.getBigDecimal("C3H_JUS") : BigDecimal.ZERO;
        fila.setC3h(valorBD != null ? valorBD.toPlainString().replaceAll("\\.", ",") + " - " + formateadorInserciones.format(jvalorBD) : BigDecimal.ZERO.toPlainString() + "-" + formateadorInserciones.format(jvalorBD));
        valorBD = rs.getBigDecimal("ECA_PREP_INS_C3_M");
        jvalorBD = rs.getBigDecimal("C3M_JUS") != null ? rs.getBigDecimal("C3M_JUS") : BigDecimal.ZERO;
        fila.setC3m(valorBD != null ? valorBD.toPlainString().replaceAll("\\.", ",") + " - " + formateadorInserciones.format(jvalorBD) : BigDecimal.ZERO.toPlainString() + "-" + formateadorInserciones.format(jvalorBD));
        valorBD = rs.getBigDecimal("ECA_PREP_INS_C3");
        jvalorBD = rs.getBigDecimal("C3_JUS") != null ? rs.getBigDecimal("C3_JUS") : BigDecimal.ZERO;
        fila.setC3Total(valorBD != null ? valorBD.toPlainString().replaceAll("\\.", ",") + " - " + formateadorInserciones.format(jvalorBD) : BigDecimal.ZERO.toPlainString() + "-" + formateadorInserciones.format(jvalorBD));
        valorBD = rs.getBigDecimal("ECA_PREP_INS_C4_H");
        jvalorBD = rs.getBigDecimal("C4H_JUS") != null ? rs.getBigDecimal("C4H_JUS") : BigDecimal.ZERO;
        fila.setC4h(valorBD != null ? valorBD.toPlainString().replaceAll("\\.", ",") + " - " + formateadorInserciones.format(jvalorBD) : BigDecimal.ZERO.toPlainString() + "-" + formateadorInserciones.format(jvalorBD));
        valorBD = rs.getBigDecimal("ECA_PREP_INS_C4_M");
        jvalorBD = rs.getBigDecimal("C4M_JUS") != null ? rs.getBigDecimal("C4M_JUS") : BigDecimal.ZERO;
        fila.setC4m(valorBD != null ? valorBD.toPlainString().replaceAll("\\.", ",") + " - " + formateadorInserciones.format(jvalorBD) : BigDecimal.ZERO.toPlainString() + "-" + formateadorInserciones.format(jvalorBD));
        valorBD = rs.getBigDecimal("ECA_PREP_INS_C4");
        jvalorBD = rs.getBigDecimal("C4_JUS") != null ? rs.getBigDecimal("C4_JUS") : BigDecimal.ZERO;
        fila.setC4Total(valorBD != null ? valorBD.toPlainString().replaceAll("\\.", ",") + " - " + formateadorInserciones.format(jvalorBD) : BigDecimal.ZERO.toPlainString() + "-" + formateadorInserciones.format(jvalorBD));
        //valorBD = rs.getBigDecimal("ECA_PREP_INS_IMPORTE");
        valorBD = rs.getBigDecimal("IMPINSERCIONES") != null ? rs.getBigDecimal("IMPINSERCIONES") : BigDecimal.ZERO;;
        fila.setInserciones(valorBD != null ? formateador.format(new BigDecimal(MeLanbide35Utils.redondearDecimalesString(valorBD, 2))) : "-");
        //valorBD = rs.getBigDecimal("ECA_PREP_INS_SEG_IMPORTE");
        //fila.setSeguimientosInserciones(valorBD != null ? valorBD.toPlainString().replaceAll("\\.", ",") : "-");
        BigDecimal impteseg = !fila.getImporte().equals("-") ? new BigDecimal(fila.getImporte().replaceAll("\\.", "").replaceAll("\\,", ".")) : BigDecimal.ZERO;
        BigDecimal impteins = !fila.getInserciones().equals("-") ? new BigDecimal(fila.getInserciones().replaceAll("\\.", "").replaceAll("\\,", ".")) : BigDecimal.ZERO;
        BigDecimal imptesegins = impteseg.add(impteins);
        fila.setSeguimientosInserciones(formateador.format(new BigDecimal(MeLanbide35Utils.redondearDecimalesString(imptesegins, 2))));
        valorBD = rs.getBigDecimal("COSTE");
        fila.setCostesSalarialesSS(valorBD != null ? formateador.format(new BigDecimal(MeLanbide35Utils.redondearDecimalesString(valorBD, 2))) : "-");
        fila.setCodTipoContrato(rs.getInt("ECA_PREP_TIPOCONTRATO"));
        if (rs.wasNull()) {
            fila.setCodTipoContrato(null);
        }
        fila.setTipoContrato(rs.getString("TIPOCONTRATO") != null ? rs.getString("TIPOCONTRATO").toUpperCase() : "-");
        fila.setEsSustituto(esSustituto);
        fila.setTipoSust(rs.getString("ECA_PREP_TIPO_SUST"));
        fila.setJusPreparadorOrigen(rs.getInt("ECA_PREP_CODORIGEN"));
        if (rs.wasNull()) {
            fila.setJusPreparadorOrigen(null);
        }
        return fila;
    }

    private DatosAnexosVO mapearDatosAnexosVO(ResultSet rs) throws Exception {
        DatosAnexosVO datos = new DatosAnexosVO();
        datos.setC1H(rs.getBigDecimal("C1_H"));
        datos.setC1M(rs.getBigDecimal("C1_M"));
        datos.setC1T(rs.getBigDecimal("C1_T"));
        datos.setC2H(rs.getBigDecimal("C2_H"));
        datos.setC2M(rs.getBigDecimal("C2_M"));
        datos.setC2T(rs.getBigDecimal("C2_T"));
        datos.setC3H(rs.getBigDecimal("C3_H"));
        datos.setC3M(rs.getBigDecimal("C3_M"));
        datos.setC3T(rs.getBigDecimal("C3_T"));
        datos.setC4H(rs.getBigDecimal("C4_H"));
        datos.setC4M(rs.getBigDecimal("C4_M"));
        datos.setC4T(rs.getBigDecimal("C4_T"));
        datos.setGastos(rs.getBigDecimal("GASTOS"));
        datos.setImportePreparadores(rs.getBigDecimal("IMPORTE_PREPARADORES"));
        datos.setImporteProspectores(rs.getBigDecimal("IMPORTE_PROSPECTORES"));
        datos.setMaxSubvencionable(rs.getBigDecimal("MAX_SUBVENCIONABLE"));
        datos.setNumPreparadores(rs.getBigDecimal("NUM_PREPARADORES"));
        datos.setNumProspectores(rs.getBigDecimal("NUM_PROSPECTORES"));
        datos.setSeguimientosAnt(rs.getBigDecimal("SEGUIMIENTOS_ANT"));
        return datos;
    }

    private EcaConfiguracionVO mapearEcaConfiguracionVO(ResultSet rs) throws Exception {
        EcaConfiguracionVO conf = new EcaConfiguracionVO();
        conf.setAno(rs.getInt("ANO"));
        if (rs.wasNull()) {
            conf.setAno(null);
        }
        conf.setImC1h(rs.getBigDecimal("IM_C1_H"));
        conf.setImC1m(rs.getBigDecimal("IM_C1_M"));
        conf.setImC2h(rs.getBigDecimal("IM_C2_H"));
        conf.setImC2m(rs.getBigDecimal("IM_C2_M"));
        conf.setImC3h(rs.getBigDecimal("IM_C3_H"));
        conf.setImC3m(rs.getBigDecimal("IM_C3_M"));
        conf.setImC4h(rs.getBigDecimal("IM_C4_H"));
        conf.setImC4m(rs.getBigDecimal("IM_C4_M"));
        conf.setImSeguimiento(rs.getBigDecimal("IM_SEGUIMIENTO"));
        conf.setImpVisita(rs.getBigDecimal("IMP_VISITA"));
        conf.setMaxEmpVisit(rs.getInt("MAX_EMP_VISIT"));
        if (rs.wasNull()) {
            conf.setMaxEmpVisit(null);
        }
        conf.setMinEmpVisit(rs.getInt("MIN_EMP_VISIT"));
        if (rs.wasNull()) {
            conf.setMinEmpVisit(null);
        }
        conf.setPoGastos(rs.getBigDecimal("PO_GASTOS"));
        conf.setPoMaxSeguimientos(rs.getBigDecimal("PO_MAX_SEGUIMIENTOS"));
        return conf;
    }

    private EcaSolPreparadoresVO mapearEcaSolPreparadoresVO(HSSFRow row) throws Exception {
        EcaSolPreparadoresVO prep = null;
        String campo = "";//Número del campo que se va a procesar
        String tipoRequerido = "";//Tipo de dato que tiene que haber en la celda.
        int longMax = -1;//Longitud máxima de campos STRING
        int pEntera = -1;//Número de dígitos de la parte entera de los numéricos
        int pDecimal = -1;//Número de dígitos de la parte decimal de los numéricos
        if (row != null && !MeLanbide35Utils.esFilaVacia(24, row)) {
            try {
                HSSFCell cell = null;
                String valorString;
                SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide35.FORMATO_FECHA);
                prep = new EcaSolPreparadoresVO();

                campo = "1";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.STRING_NIF;
                longMax = 10;
                pEntera = -1;
                pDecimal = -1;
                int celdaIni = row.getFirstCellNum();
                cell = row.getCell(celdaIni);
                valorString = (String) MeLanbide35Utils.getValorCelda(cell, tipoRequerido);

                if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, false)) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    prep.setNif(valorString.toUpperCase());
                } else {
                    throw new DatoExcelNoValidoException();
                }

                campo = "2";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.STRING;
                longMax = 200;
                pEntera = -1;
                pDecimal = -1;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = (String) MeLanbide35Utils.getValorCelda(cell, tipoRequerido);

                if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, false)) {
                    valorString = reemplazarSaltosDeLinea(valorString, " ");
                    prep.setNombre(valorString.toUpperCase());
                } else {
                    throw new DatoExcelNoValidoException();
                }

                campo = "3";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.FECHA;
                longMax = -1;
                pEntera = -1;
                pDecimal = -1;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        prep.setFecIni(format.parse(valorString));
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "4";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.FECHA;
                longMax = -1;
                pEntera = -1;
                pDecimal = -1;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        prep.setFecFin(format.parse(valorString));
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "5";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL;
                longMax = -1;
                pEntera = 8;
                pDecimal = 2;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        prep.setHorasJC(new BigDecimal(valorString));
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "6";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL;
                longMax = -1;
                pEntera = 8;
                pDecimal = 2;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        prep.setHorasCont(new BigDecimal(valorString));
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "7";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL;
                longMax = -1;
                pEntera = 8;
                pDecimal = 2;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        prep.setHorasEca(new BigDecimal(valorString));
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "8";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL;
                longMax = -1;
                pEntera = 8;
                pDecimal = 2;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        prep.setImpSSJC(new BigDecimal(valorString));
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "9";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL;
                longMax = -1;
                pEntera = 8;
                pDecimal = 2;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        prep.setImpSSJR(new BigDecimal(valorString));
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "10";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL;
                longMax = -1;
                pEntera = 8;
                pDecimal = 2;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        prep.setImpSSECA(new BigDecimal(valorString));
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "11";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.NUMERICO;
                longMax = -1;
                pEntera = 8;
                pDecimal = -1;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        prep.setSegAnt(new BigDecimal(valorString).intValue());
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "12";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL;
                longMax = -1;
                pEntera = 8;
                pDecimal = 2;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        prep.setImpSegAnt(new BigDecimal(valorString));
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "13";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL;
                longMax = -1;
                pEntera = 8;
                pDecimal = 2;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        prep.setInsC1H(new BigDecimal(valorString));
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "14";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL;
                longMax = -1;
                pEntera = 8;
                pDecimal = 2;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        prep.setInsC1M(new BigDecimal(valorString));
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "15";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL;
                longMax = -1;
                pEntera = 8;
                pDecimal = 2;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        prep.setInsC1(new BigDecimal(valorString));
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "16";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL;
                longMax = -1;
                pEntera = 8;
                pDecimal = 2;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        prep.setInsC2H(new BigDecimal(valorString));
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "17";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL;
                longMax = -1;
                pEntera = 8;
                pDecimal = 2;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        prep.setInsC2M(new BigDecimal(valorString));
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "18";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL;
                longMax = -1;
                pEntera = 8;
                pDecimal = 2;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        prep.setInsC2(new BigDecimal(valorString));
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "19";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL;
                longMax = -1;
                pEntera = 8;
                pDecimal = 2;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        prep.setInsC3H(new BigDecimal(valorString));
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "20";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL;
                longMax = -1;
                pEntera = 8;
                pDecimal = 2;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        prep.setInsC3M(new BigDecimal(valorString));
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "21";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL;
                longMax = -1;
                pEntera = 8;
                pDecimal = 2;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        prep.setInsC3(new BigDecimal(valorString));
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "22";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL;
                longMax = -1;
                pEntera = 8;
                pDecimal = 2;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        prep.setInsC4H(new BigDecimal(valorString));
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "23";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL;
                longMax = -1;
                pEntera = 8;
                pDecimal = 2;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        prep.setInsC4M(new BigDecimal(valorString));
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "24";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL;
                longMax = -1;
                pEntera = 8;
                pDecimal = 2;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        prep.setInsC4(new BigDecimal(valorString));
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "25";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.STRING_NIF;
                longMax = 10;
                pEntera = -1;
                pDecimal = -1;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, tipoRequerido);

                if (valorString != null) {
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        valorString = reemplazarSaltosDeLinea(valorString, "");
                        prep.setNifPreparadorSustituido(valorString.toUpperCase());
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                /*campo = "25";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL;
                longMax = -1;
                pEntera = 8;
                pDecimal = 2;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell);
                if(valorString != null)
                {
                    if(MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true))
                    {
                        prep.setInsImporte(new BigDecimal(valorString));
                    }
                    else
                    {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "26";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL;
                longMax = -1;
                pEntera = 8;
                pDecimal = 2;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell);
                if(valorString != null)
                {
                    if(MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true))
                    {
                        prep.setInsSegImporte(new BigDecimal(valorString));
                    }
                    else
                    {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "27";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL;
                longMax = -1;
                pEntera = 8;
                pDecimal = 2;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell);
                if(valorString != null)
                {
                    if(MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true))
                    {
                        prep.setCoste(new BigDecimal(valorString));
                    }
                    else
                    {
                        throw new DatoExcelNoValidoException();
                    }
                }*/
            } catch (Exception ex) {
                ExcelRowMappingException erme = new ExcelRowMappingException(campo);
                erme.setLongMax(longMax);
                erme.setTipo(tipoRequerido);
                erme.setpEntera(pEntera);
                erme.setpDecimal(pDecimal);
                throw erme;
            }
        }
        return prep;
    }

    private EcaSolProspectoresVO mapearEcaSolProspectoresVO(HSSFRow row) throws Exception {
        EcaSolProspectoresVO pros = null;
        String campo = "";//Número del campo que se va a procesar
        String tipoRequerido = "";//Tipo de dato que tiene que haber en la celda.
        int longMax = -1;//Longitud máxima de campos STRING
        int pEntera = -1;//Número de dígitos de la parte entera de los numéricos
        int pDecimal = -1;//Número de dígitos de la parte decimal de los numéricos
        if (row != null && !MeLanbide35Utils.esFilaVacia(12, row)) {
            try {
                HSSFCell cell = null;
                String valorString;
                SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide35.FORMATO_FECHA);
                pros = new EcaSolProspectoresVO();

                campo = "1";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.STRING_NIF;
                longMax = 10;
                pEntera = -1;
                pDecimal = -1;
                int celdaIni = row.getFirstCellNum();
                cell = row.getCell(celdaIni);
                valorString = (String) MeLanbide35Utils.getValorCelda(cell, tipoRequerido);

                if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, false)) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    pros.setNif(valorString.toUpperCase());
                } else {
                    throw new DatoExcelNoValidoException();
                }

                campo = "2";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.STRING;
                longMax = 100;
                pEntera = -1;
                pDecimal = -1;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = (String) MeLanbide35Utils.getValorCelda(cell, tipoRequerido);

                if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, false)) {
                    valorString = reemplazarSaltosDeLinea(valorString, " ");
                    pros.setNombre(valorString.toUpperCase());
                } else {
                    throw new DatoExcelNoValidoException();
                }

                campo = "3";
                celdaIni++;
                cell = row.getCell(celdaIni);
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.FECHA;
                longMax = -1;
                pEntera = -1;
                pDecimal = -1;
                valorString = MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        pros.setFecIni(format.parse(valorString));
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "4";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.FECHA;
                longMax = -1;
                pEntera = -1;
                pDecimal = -1;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        pros.setFecFin(format.parse(valorString));
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "5";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL;
                longMax = -1;
                pEntera = 8;
                pDecimal = 2;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        pros.setHorasJC(new BigDecimal(valorString));
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "6";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL;
                longMax = -1;
                pEntera = 8;
                pDecimal = 2;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        pros.setHorasCont(new BigDecimal(valorString));
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "7";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL;
                longMax = -1;
                pEntera = 8;
                pDecimal = 2;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        pros.setHorasEca(new BigDecimal(valorString));
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "8";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL;
                longMax = -1;
                pEntera = 8;
                pDecimal = 2;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        pros.setImpSSJC(new BigDecimal(valorString));
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "9";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL;
                longMax = -1;
                pEntera = 8;
                pDecimal = 2;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        pros.setImpSSJR(new BigDecimal(valorString));
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "10";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL;
                longMax = -1;
                pEntera = 8;
                pDecimal = 2;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        pros.setImpSSECA(new BigDecimal(valorString));
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "11";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.NUMERICO;
                longMax = -1;
                pEntera = 8;
                pDecimal = -1;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        pros.setVisitas(new BigDecimal(valorString).intValue());
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "12";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL;
                longMax = -1;
                pEntera = 8;
                pDecimal = 2;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        pros.setVisitasImp(new BigDecimal(valorString));
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "13";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL;
                longMax = -1;
                pEntera = 8;
                pDecimal = 2;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        pros.setCoste(new BigDecimal(valorString));
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "14";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.STRING_NIF;
                longMax = 10;
                pEntera = -1;
                pDecimal = -1;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, tipoRequerido);

                if (valorString != null) {
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        valorString = reemplazarSaltosDeLinea(valorString, "");
                        pros.setNifProspectorSustituido(valorString.toUpperCase());
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }
            } catch (Exception ex) {
                ExcelRowMappingException erme = new ExcelRowMappingException(campo);
                erme.setLongMax(longMax);
                erme.setTipo(tipoRequerido);
                erme.setpEntera(pEntera);
                erme.setpDecimal(pDecimal);
                throw erme;
            }
        }
        return pros;
    }

    private FilaPreparadorSolicitudVO mapearFilaPreparadorSolicitudVO(HSSFRow row) throws Exception {
        FilaPreparadorSolicitudVO fila = null;
        String campo = "";
        if (row != null) {
            try {
                HSSFCell cell = null;
                String valorString = null;
                SimpleDateFormat formatoFecha = new SimpleDateFormat(ConstantesMeLanbide35.FORMATO_FECHA);
                fila = new FilaPreparadorSolicitudVO();

                campo = "1";
                int celdaIni = row.getFirstCellNum();
                cell = row.getCell(celdaIni);
                valorString = (String) MeLanbide35Utils.getValorCelda(cell, ConstantesMeLanbide35.TiposRequeridos.STRING_NIF);

                if (valorString != null && !valorString.equals("")) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    fila.setNif(valorString.toUpperCase());
                } else {
                    fila.setNif("-");
                }

                campo = "2";
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = (String) MeLanbide35Utils.getValorCelda(cell, ConstantesMeLanbide35.TiposRequeridos.STRING);

                if (valorString != null && !valorString.equals("")) {
                    valorString = reemplazarSaltosDeLinea(valorString, " ");
                    fila.setNombreApel(valorString.toUpperCase());
                } else {
                    fila.setNombreApel("-");
                }

                campo = "3";
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, ConstantesMeLanbide35.TiposRequeridos.FECHA);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    fila.setFechaInicio(formatoFecha.format(valorString));
                } else {
                    fila.setFechaInicio("-");
                }

                campo = "4";
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, ConstantesMeLanbide35.TiposRequeridos.FECHA);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    fila.setFechaFin(formatoFecha.format(valorString));
                } else {
                    fila.setFechaFin("-");
                }

                campo = "5";
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    fila.setHorasAnuales(valorString);
                } else {
                    fila.setHorasAnuales("-");
                }

                campo = "6";
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    fila.setHorasContrato(valorString);
                } else {
                    fila.setHorasContrato("-");
                }

                campo = "7";
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    fila.setHorasDedicacionECA(valorString);
                } else {
                    fila.setHorasDedicacionECA("-");
                }

                campo = "8";
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    fila.setCostesSalarialesSSJor(valorString);
                } else {
                    fila.setCostesSalarialesSSJor("-");
                }

                campo = "9";
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    fila.setCostesSalarialesSSPorJor(valorString);
                } else {
                    fila.setCostesSalarialesSSPorJor("-");
                }

                campo = "10";
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    fila.setCostesSalarialesSSEca(valorString);
                } else {
                    fila.setCostesSalarialesSSEca("-");
                }

                campo = "11";
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, ConstantesMeLanbide35.TiposRequeridos.NUMERICO);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    fila.setNumSegAnteriores(valorString);
                } else {
                    fila.setNumSegAnteriores("-");
                }

                campo = "12";
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    fila.setImporte(valorString);
                } else {
                    fila.setImporte("-");
                }

                campo = "13";
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    fila.setC1h(valorString);
                } else {
                    fila.setC1h("-");
                }

                campo = "14";
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    fila.setC1m(valorString);
                } else {
                    fila.setC1m("-");
                }

                campo = "15";
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    fila.setC1Total(valorString);
                } else {
                    fila.setC1Total("-");
                }

                campo = "16";
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    fila.setC2h(valorString);
                } else {
                    fila.setC2h("-");
                }

                campo = "17";
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    fila.setC2m(valorString);
                } else {
                    fila.setC2m("-");
                }

                campo = "18";
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    fila.setC2Total(valorString);
                } else {
                    fila.setC2Total("-");
                }

                campo = "19";
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    fila.setC3h(valorString);
                } else {
                    fila.setC3h("-");
                }

                campo = "20";
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    fila.setC3m(valorString);
                } else {
                    fila.setC3m("-");
                }

                campo = "21";
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    fila.setC3Total(valorString);
                } else {
                    fila.setC3Total("-");
                }

                campo = "22";
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    fila.setC4h(valorString);
                } else {
                    fila.setC4h("-");
                }

                campo = "23";
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    fila.setC4m(valorString);
                } else {
                    fila.setC4m("-");
                }

                campo = "24";
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    fila.setC4Total(valorString);
                } else {
                    fila.setC4Total("-");
                }

                campo = "25";
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    fila.setInserciones(valorString);
                } else {
                    fila.setInserciones("-");
                }

                campo = "26";
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    fila.setSeguimientosInserciones(valorString);
                } else {
                    fila.setSeguimientosInserciones("-");
                }

                campo = "27";
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    fila.setCostesSalarialesSS(valorString);
                } else {
                    fila.setCostesSalarialesSS("-");
                }
            } catch (Exception ex) {
                ExcelRowMappingException erme = new ExcelRowMappingException(campo);
                throw erme;
            }
        }
        return fila;
    }

    private EcaJusPreparadoresVO mapearEcaJusPreparadoresVO(ResultSet rs) throws Exception {
        EcaJusPreparadoresVO prep = new EcaJusPreparadoresVO();
        prep.setJusPreparadoresCod(rs.getInt("ECA_JUS_PREPARADORES_COD"));
        if (rs.wasNull()) {
            prep.setJusPreparadoresCod(null);
        }
        prep.setSolPreparadoresCod(rs.getInt("ECA_SOL_PREPARADORES_COD"));
        if (rs.wasNull()) {
            prep.setSolPreparadoresCod(null);
        }
        prep.setNif(rs.getString("ECA_PREP_NIF"));
        prep.setNombre(rs.getString("ECA_PREP_NOMBRE"));
        prep.setFecIni(rs.getDate("ECA_PREP_FECINI"));
        prep.setFecFin(rs.getDate("ECA_PREP_FECFIN"));
        prep.setHorasJC(rs.getBigDecimal("ECA_PREP_HORAS_JC"));
        prep.setHorasCont(rs.getBigDecimal("ECA_PREP_HORAS_CONT"));
        prep.setHorasEca(rs.getBigDecimal("ECA_PREP_HORAS_ECA"));
        prep.setImpSSJC(rs.getBigDecimal("ECA_PREP_IMP_SS_JC"));
        prep.setImpSSJR(rs.getBigDecimal("ECA_PREP_IMP_SS_JR"));
        prep.setImpSSECA(rs.getBigDecimal("ECA_PREP_IMP_SS_ECA"));
        prep.setTipoContrato(rs.getInt("ECA_PREP_TIPOCONTRATO"));
        if (rs.wasNull()) {
            prep.setTipoContrato(null);
        }
        prep.setSolicitud(rs.getInt("ECA_PREP_SOLICITUD"));
        if (rs.wasNull()) {
            prep.setSolicitud(null);
        }
        prep.setJusPreparadorOrigen(rs.getInt("ECA_PREP_CODORIGEN"));
        if (rs.wasNull()) {
            prep.setJusPreparadorOrigen(null);
        }
        prep.setTipoSust(rs.getString("ECA_PREP_TIPO_SUST"));
        return prep;
    }

    private EcaSegPreparadoresVO mapearEcaSegPreparadoresVO(ResultSet rs) throws Exception {
        EcaSegPreparadoresVO seg = new EcaSegPreparadoresVO();
        seg.setTipo(rs.getInt("ECA_SEG_TIPO"));
        if (rs.wasNull()) {
            seg.setTipo(null);
        }
        seg.setJusPreparadoresCod(rs.getInt("ECA_JUS_PREPARADORES_COD"));
        if (rs.wasNull()) {
            seg.setJusPreparadoresCod(null);
        }
        seg.setSegPreparadoresCod(rs.getInt("ECA_SEG_PREPARADORES_COD"));
        if (rs.wasNull()) {
            seg.setSegPreparadoresCod(null);
        }
        seg.setNif(rs.getString("ECA_SEG_NIF"));
        seg.setNombre(rs.getString("ECA_SEG_NOMBRE"));
        seg.setFecIni(rs.getDate("ECA_SEG_FECINI"));
        seg.setFecFin(rs.getDate("ECA_SEG_FECFIN"));
        seg.setHorasCont(rs.getBigDecimal("ECA_SEG_HORAS"));
        seg.setTipoDiscapacidad(rs.getInt("ECA_SEG_TIPODISCAPACIDAD"));
        if (rs.wasNull()) {
            seg.setTipoDiscapacidad(null);
        }
        seg.setGravedad(rs.getInt("ECA_SEG_DISCGRAVEDAD"));
        if (rs.wasNull()) {
            seg.setGravedad(null);
        }
        seg.setTipoContrato(rs.getInt("ECA_SEG_TIPOCONTRATO"));
        if (rs.wasNull()) {
            seg.setTipoContrato(null);
        }
        seg.setPorcJornada(rs.getBigDecimal("ECA_SEG_PORCJORNADA"));
        seg.setEmpresa(rs.getString("ECA_SEG_EMPRESA"));
        //seg.setNomPersContacto(rs.getString("ECA_SEG_PERSCONTACTO"));
        seg.setObservaciones(rs.getString("ECA_SEG_OBSERVACIONES"));
        seg.setFecNacimiento(rs.getDate("ECA_SEG_FECNACIMIENTO"));
        seg.setFecSeguimiento(rs.getDate("ECA_SEG_FECSEGUIMIENTO"));
        seg.setSexo(rs.getInt("ECA_SEG_SEXO"));
        if (rs.wasNull()) {
            seg.setSexo(null);
        }
        seg.setNifPreparador(rs.getString("NIFPREPARADOR"));
        seg.setFinContratoDespido(rs.getInt("ECA_SEG_FINCONTRATO"));
        if (rs.wasNull()) {
            seg.setFinContratoDespido(null);
        }
        return seg;
    }

    private EcaSegPreparadores2016VO mapearEcaSegPreparadoresVO_2016(ResultSet rs) throws Exception {
        EcaSegPreparadores2016VO seg = new EcaSegPreparadores2016VO();
        seg.setTipo(rs.getInt("ECA_SEG_TIPO"));
        if (rs.wasNull()) {
            seg.setTipo(null);
        }
        seg.setJusPreparadoresCod(rs.getInt("ECA_JUS_PREPARADORES_COD"));
        if (rs.wasNull()) {
            seg.setJusPreparadoresCod(null);
        }
        seg.setSegPreparadoresCod(rs.getInt("ECA_SEG_PREPARADORES_COD"));
        if (rs.wasNull()) {
            seg.setSegPreparadoresCod(null);
        }
        seg.setNif(rs.getString("ECA_SEG_NIF"));
        seg.setNombre(rs.getString("ECA_SEG_NOMBRE"));
        seg.setFecIni(rs.getDate("ECA_SEG_FECINI"));
        seg.setFecFin(rs.getDate("ECA_SEG_FECFIN"));
        seg.setHorasCont(rs.getBigDecimal("ECA_SEG_HORAS"));
        seg.setTipoDiscapacidad(rs.getInt("ECA_SEG_TIPODISCAPACIDAD"));
        if (rs.wasNull()) {
            seg.setTipoDiscapacidad(null);
        }
        seg.setGravedad(rs.getInt("ECA_SEG_DISCGRAVEDAD"));
        if (rs.wasNull()) {
            seg.setGravedad(null);
        }
        seg.setTipoContrato(rs.getInt("ECA_SEG_TIPOCONTRATO"));
        if (rs.wasNull()) {
            seg.setTipoContrato(null);
        }
        seg.setPorcJornada(rs.getBigDecimal("ECA_SEG_PORCJORNADA"));
        seg.setEmpresa(rs.getString("ECA_SEG_EMPRESA"));
        //seg.setNomPersContacto(rs.getString("ECA_SEG_PERSCONTACTO"));
        seg.setObservaciones(rs.getString("ECA_SEG_OBSERVACIONES"));
        seg.setFecNacimiento(rs.getDate("ECA_SEG_FECNACIMIENTO"));
        seg.setFecSeguimiento(rs.getDate("ECA_SEG_FECSEGUIMIENTO"));
        seg.setSexo(rs.getInt("ECA_SEG_SEXO"));
        if (rs.wasNull()) {
            seg.setSexo(null);
        }
        seg.setNifPreparador(rs.getString("NIFPREPARADOR"));
        seg.setFinContratoDespido(rs.getInt("ECA_SEG_FINCONTRATO"));
        if (rs.wasNull()) {
            seg.setFinContratoDespido(null);
        }
        return seg;
    }

    private EcaSegPreparadoresVO mapearEcaSegPreparadoresVO(HSSFRow row) throws Exception {
        EcaSegPreparadoresVO seg = null;
        String campo = "";
        String tipoRequerido = "";
        int longMax = -1;
        int pEntera = -1;
        int pDecimal = -1;
        if (row != null && !MeLanbide35Utils.esFilaVacia(16, row)) {
            try {
                SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide35.FORMATO_FECHA);
                HSSFCell cell = null;
                String valorString;
                seg = new EcaSegPreparadoresVO();

                campo = "1";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.STRING_NIF;
                longMax = 10;
                pEntera = -1;
                pDecimal = -1;
                int celdaIni = row.getFirstCellNum();
                cell = row.getCell(celdaIni);
                valorString = (String) MeLanbide35Utils.getValorCelda(cell, tipoRequerido);

                if (valorString != null && !valorString.equals("")) {
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        valorString = reemplazarSaltosDeLinea(valorString, "");
                        seg.setNif(valorString.toUpperCase());
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "2";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.STRING;
                longMax = 200;
                pEntera = -1;
                pDecimal = -1;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = (String) MeLanbide35Utils.getValorCelda(cell, tipoRequerido);

                if (valorString != null && !valorString.equals("")) {
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        valorString = reemplazarSaltosDeLinea(valorString, " ");
                        seg.setNombre(valorString.toUpperCase());
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "3";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.NUMERICO;
                longMax = -1;
                pEntera = 1;
                pDecimal = -1;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                /*String tipo_hombre=ConstantesMeLanbide35.SEXO.SEXO_HOMBRE;
                String tipo_mujer=ConstantesMeLanbide35.SEXO.SEXO_MUJER;*/
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    /*if(valorString.equals(tipo_hombre)){
                        valorString=ConstantesMeLanbide35.SEXO_NUM.SEXO_NUM_HOMBRE;
                    }
                    if(valorString.equals(tipo_mujer)){
                        valorString=ConstantesMeLanbide35.SEXO_NUM.SEXO_NUM_MUJER;
                    }*/
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        //valorString = valorString.replaceAll("\\.", ",");
                        //valorString = valorString.substring(0, valorString.indexOf(","));
                        seg.setSexo(new BigDecimal(valorString).intValue());
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "4";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.FECHA;
                longMax = -1;
                pEntera = -1;
                pDecimal = -1;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        seg.setFecNacimiento(format.parse(valorString));
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "5";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.NUMERICO;
                longMax = -1;
                pEntera = 1;
                pDecimal = -1;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                /*String psiquico=ConstantesMeLanbide35.CODIGOS_TIPO_DISCAPACIDAD.PSIQUICO;
                String fisico=ConstantesMeLanbide35.CODIGOS_TIPO_DISCAPACIDAD.FISICO;
                String sensorial=ConstantesMeLanbide35.CODIGOS_TIPO_DISCAPACIDAD.SENSORIAL;*/
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    /*if(valorString.equals(psiquico)){
                        valorString=ConstantesMeLanbide35.CODIGOS_TIPO_DISCAPACIDAD_NUM.PSIQUICO;
                    }
                    
                    if(valorString.equals(fisico)){
                        valorString=ConstantesMeLanbide35.CODIGOS_TIPO_DISCAPACIDAD_NUM.FISICO;
                    }
                    
                    if(valorString.equals(sensorial)){
                        valorString=ConstantesMeLanbide35.CODIGOS_TIPO_DISCAPACIDAD_NUM.SENSORIAL;
                    }*/

                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        //valorString = valorString.replaceAll("\\.", ",");
                        //valorString = valorString.substring(0, valorString.indexOf(","));          
                        seg.setTipoDiscapacidad(new BigDecimal(valorString).intValue());
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "6";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.NUMERICO;
                longMax = -1;
                pEntera = 1;
                pDecimal = -1;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                /*String menor_33=ConstantesMeLanbide35.GRAVEDAD_DISCAPACIDAD.MENOR_33;
                String entre_33_65=ConstantesMeLanbide35.GRAVEDAD_DISCAPACIDAD.ENTRE_33_65;
                String mayor_65=ConstantesMeLanbide35.GRAVEDAD_DISCAPACIDAD.MAYOR_65;*/
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    /*if(valorString.equals(menor_33)){
                        valorString=ConstantesMeLanbide35.CODIGOS_GRAVEDAD_DISCAPACIDAD.MENOR_33;
                    }
                    if(valorString.equals(entre_33_65)){
                        valorString=ConstantesMeLanbide35.CODIGOS_GRAVEDAD_DISCAPACIDAD.ENTRE_33_65;
                    }
                    if(valorString.equals(mayor_65)){
                        valorString=ConstantesMeLanbide35.CODIGOS_GRAVEDAD_DISCAPACIDAD.MAYOR_65;
                    }*/
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        //valorString = valorString.replaceAll("\\.", ",");
                        //valorString = valorString.substring(0, valorString.indexOf(","));            
                        seg.setGravedad(new BigDecimal(valorString).intValue());
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "7";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.NUMERICO;
                longMax = -1;
                pEntera = 2;
                pDecimal = -1;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                /*String indefinido=ConstantesMeLanbide35.CODIGOS_TIPO_CONTRATO.INDEFINIDO;
                String temporal=ConstantesMeLanbide35.CODIGOS_TIPO_CONTRATO.TEMPORAL;
                String indefinidoNum=ConstantesMeLanbide35.CODIGOS_TIPO_CONTRATO_NUM.INDEFINIDO;
                String temporalNum=ConstantesMeLanbide35.CODIGOS_TIPO_CONTRATO_NUM.TEMPORAL;*/
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    /*if(valorString.equals(indefinido)){
                        valorString=indefinidoNum;
                    }
                    
                    if(valorString.equals(temporal)){
                        valorString=temporalNum;
                    }*/
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        //valorString = valorString.replaceAll("\\.", ",");
                        //valorString = valorString.substring(0, valorString.indexOf(","));
                        seg.setTipoContrato(new BigDecimal(valorString).intValue());
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "8";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL;
                longMax = -1;
                pEntera = 5;
                pDecimal = 2;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        seg.setPorcJornada(new BigDecimal(valorString.replaceAll(",", "\\.").trim()));//--> CONVERTIR , POR .
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                //El colectivo no se tiene en cuenta
                celdaIni++;

                campo = "10";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.FECHA;
                longMax = -1;
                pEntera = -1;
                pDecimal = -1;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        seg.setFecIni(format.parse(valorString));
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "11";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.FECHA;
                longMax = -1;
                pEntera = -1;
                pDecimal = -1;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        seg.setFecFin(format.parse(valorString));
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "12";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.STRING;
                longMax = 100;
                pEntera = -1;
                pDecimal = -1;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = (String) MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        seg.setEmpresa(valorString);
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "13";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.STRING_NIF;
                longMax = 10;
                pEntera = -1;
                pDecimal = -1;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = (String) MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        seg.setNifPreparador(valorString);
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "14";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL;
                longMax = -1;
                pEntera = 8;
                pDecimal = 2;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        seg.setHorasCont(new BigDecimal(valorString.replaceAll(",", "\\.").trim()));
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "15";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.STRING;
                longMax = 200;
                pEntera = -1;
                pDecimal = -1;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = (String) MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "<br/>");
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        seg.setObservaciones(valorString);
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                /*campo = "14";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.FECHA;
                longMax = -1;
                pEntera = -1;
                pDecimal = -1;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell);
                if(valorString != null)
                {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if(MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true))
                    {
                        seg.setFecSeguimiento(format.parse(valorString));
                    }
                    else
                    {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "15";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.STRING;
                longMax = 100;
                pEntera = -1;
                pDecimal = -1;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = (String)MeLanbide35Utils.getValorCelda(cell);
                if(valorString != null)
                {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if(MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true))
                    {
                        seg.setNomPersContacto(valorString);
                    }
                    else
                    {
                        throw new DatoExcelNoValidoException();
                    }
                }
                
                campo = "16";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.STRING;
                longMax = 200;
                pEntera = -1;
                pDecimal = -1;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = (String)MeLanbide35Utils.getValorCelda(cell);
                if(valorString != null)
                {
                    valorString = reemplazarSaltosDeLinea(valorString, "<br/>");
                    if(MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true))
                    {
                        seg.setObservaciones(valorString);
                    }
                    else
                    {
                        throw new DatoExcelNoValidoException();
                    }
                }*/
            } catch (Exception ex) {
                ExcelRowMappingException erme = new ExcelRowMappingException(campo);
                erme.setLongMax(longMax);
                erme.setTipo(tipoRequerido);
                erme.setpEntera(pEntera);
                erme.setpDecimal(pDecimal);
                throw erme;
            }
        }
        return seg;
    }

    private EcaSegPreparadores2016VO mapearEcaSegPreparadoresVO_2016(HSSFRow row) throws Exception {
        EcaSegPreparadores2016VO seg = null;
        String campo = "";
        String tipoRequerido = "";
        int longMax = -1;
        int pEntera = -1;
        int pDecimal = -1;
        if (row != null && !MeLanbide35Utils.esFilaVacia(16, row)) {
            try {
                SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide35.FORMATO_FECHA);
                HSSFCell cell = null;
                String valorString;
                seg = new EcaSegPreparadores2016VO();

                campo = "1";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.STRING_NIF;
                longMax = 10;
                pEntera = -1;
                pDecimal = -1;
                int celdaIni = row.getFirstCellNum();
                cell = row.getCell(celdaIni);
                valorString = (String) MeLanbide35Utils.getValorCelda(cell, tipoRequerido);

                if (valorString != null && !valorString.equals("")) {
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        valorString = reemplazarSaltosDeLinea(valorString, "");
                        seg.setNif(valorString.toUpperCase());
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "2";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.STRING;
                longMax = 200;
                pEntera = -1;
                pDecimal = -1;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = (String) MeLanbide35Utils.getValorCelda(cell, tipoRequerido);

                if (valorString != null && !valorString.equals("")) {
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        valorString = reemplazarSaltosDeLinea(valorString, " ");
                        seg.setNombre(valorString.toUpperCase());
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "3";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.NUMERICO;
                longMax = -1;
                pEntera = 1;
                pDecimal = -1;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                String tipo_hombre = ConstantesMeLanbide35.SEXO.SEXO_HOMBRE;
                String tipo_mujer = ConstantesMeLanbide35.SEXO.SEXO_MUJER;
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if (valorString.equals(tipo_hombre)) {
                        valorString = ConstantesMeLanbide35.SEXO_NUM.SEXO_NUM_HOMBRE;
                    }
                    if (valorString.equals(tipo_mujer)) {
                        valorString = ConstantesMeLanbide35.SEXO_NUM.SEXO_NUM_MUJER;
                    }
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        //valorString = valorString.replaceAll("\\.", ",");
                        //valorString = valorString.substring(0, valorString.indexOf(","));
                        seg.setSexo(new BigDecimal(valorString).intValue());
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "4";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.FECHA;
                longMax = -1;
                pEntera = -1;
                pDecimal = -1;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        seg.setFecNacimiento(format.parse(valorString));
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "5";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.NUMERICO;
                longMax = -1;
                pEntera = 1;
                pDecimal = -1;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                String psiquico = ConstantesMeLanbide35.CODIGOS_TIPO_DISCAPACIDAD.PSIQUICO;
                String fisico = ConstantesMeLanbide35.CODIGOS_TIPO_DISCAPACIDAD.FISICO;
                String sensorial = ConstantesMeLanbide35.CODIGOS_TIPO_DISCAPACIDAD.SENSORIAL;
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if (valorString.equals(psiquico)) {
                        valorString = ConstantesMeLanbide35.CODIGOS_TIPO_DISCAPACIDAD_NUM.PSIQUICO;
                    }

                    if (valorString.equals(fisico)) {
                        valorString = ConstantesMeLanbide35.CODIGOS_TIPO_DISCAPACIDAD_NUM.FISICO;
                    }

                    if (valorString.equals(sensorial)) {
                        valorString = ConstantesMeLanbide35.CODIGOS_TIPO_DISCAPACIDAD_NUM.SENSORIAL;
                    }

                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        //valorString = valorString.replaceAll("\\.", ",");
                        //valorString = valorString.substring(0, valorString.indexOf(","));          
                        seg.setTipoDiscapacidad(new BigDecimal(valorString).intValue());
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "6";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.NUMERICO;
                longMax = -1;
                pEntera = 1;
                pDecimal = -1;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                String menor_33 = ConstantesMeLanbide35.GRAVEDAD_DISCAPACIDAD.MENOR_33;
                String entre_33_65 = ConstantesMeLanbide35.GRAVEDAD_DISCAPACIDAD.ENTRE_33_65;
                String mayor_65 = ConstantesMeLanbide35.GRAVEDAD_DISCAPACIDAD.MAYOR_65;
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if (valorString.equals(menor_33)) {
                        valorString = ConstantesMeLanbide35.CODIGOS_GRAVEDAD_DISCAPACIDAD.MENOR_33;
                    }
                    if (valorString.equals(entre_33_65)) {
                        valorString = ConstantesMeLanbide35.CODIGOS_GRAVEDAD_DISCAPACIDAD.ENTRE_33_65;
                    }
                    if (valorString.equals(mayor_65)) {
                        valorString = ConstantesMeLanbide35.CODIGOS_GRAVEDAD_DISCAPACIDAD.MAYOR_65;
                    }
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        //valorString = valorString.replaceAll("\\.", ",");
                        //valorString = valorString.substring(0, valorString.indexOf(","));            
                        seg.setGravedad(new BigDecimal(valorString).intValue());
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "7";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.NUMERICO;
                longMax = -1;
                pEntera = 2;
                pDecimal = -1;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                String indefinido = ConstantesMeLanbide35.CODIGOS_TIPO_CONTRATO.INDEFINIDO;
                String temporal = ConstantesMeLanbide35.CODIGOS_TIPO_CONTRATO.TEMPORAL;
                String indefinidoNum = ConstantesMeLanbide35.CODIGOS_TIPO_CONTRATO_NUM.INDEFINIDO;
                String temporalNum = ConstantesMeLanbide35.CODIGOS_TIPO_CONTRATO_NUM.TEMPORAL;
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if (valorString.equals(indefinido)) {
                        valorString = indefinidoNum;
                    }

                    if (valorString.equals(temporal)) {
                        valorString = temporalNum;
                    }
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        //valorString = valorString.replaceAll("\\.", ",");
                        //valorString = valorString.substring(0, valorString.indexOf(","));
                        seg.setTipoContrato(new BigDecimal(valorString).intValue());
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "8";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL;
                longMax = -1;
                pEntera = 5;
                pDecimal = 2;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        seg.setPorcJornada(new BigDecimal(valorString.replaceAll(",", "\\.").trim()));//--> CONVERTIR , POR .
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                //El colectivo no se tiene en cuenta
                celdaIni++;

                campo = "10";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.FECHA;
                longMax = -1;
                pEntera = -1;
                pDecimal = -1;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        seg.setFecIni(format.parse(valorString));
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "11";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.FECHA;
                longMax = -1;
                pEntera = -1;
                pDecimal = -1;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        seg.setFecFin(format.parse(valorString));
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "12";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.STRING;
                longMax = 100;
                pEntera = -1;
                pDecimal = -1;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = (String) MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        seg.setEmpresa(valorString);
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "13";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.STRING_NIF;
                longMax = 10;
                pEntera = -1;
                pDecimal = -1;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = (String) MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        seg.setNifPreparador(valorString);
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "14";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL;
                longMax = -1;
                pEntera = 8;
                pDecimal = 2;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        seg.setHorasCont(new BigDecimal(valorString.replaceAll(",", "\\.").trim()));
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "15";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.STRING;
                longMax = 200;
                pEntera = -1;
                pDecimal = -1;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = (String) MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "<br/>");
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        seg.setObservaciones(valorString);
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                /*campo = "14";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.FECHA;
                longMax = -1;
                pEntera = -1;
                pDecimal = -1;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell);
                if(valorString != null)
                {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if(MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true))
                    {
                        seg.setFecSeguimiento(format.parse(valorString));
                    }
                    else
                    {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "15";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.STRING;
                longMax = 100;
                pEntera = -1;
                pDecimal = -1;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = (String)MeLanbide35Utils.getValorCelda(cell);
                if(valorString != null)
                {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if(MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true))
                    {
                        seg.setNomPersContacto(valorString);
                    }
                    else
                    {
                        throw new DatoExcelNoValidoException();
                    }
                }
                
                campo = "16";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.STRING;
                longMax = 200;
                pEntera = -1;
                pDecimal = -1;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = (String)MeLanbide35Utils.getValorCelda(cell);
                if(valorString != null)
                {
                    valorString = reemplazarSaltosDeLinea(valorString, "<br/>");
                    if(MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true))
                    {
                        seg.setObservaciones(valorString);
                    }
                    else
                    {
                        throw new DatoExcelNoValidoException();
                    }
                }*/
            } catch (Exception ex) {
                ExcelRowMappingException erme = new ExcelRowMappingException(campo);
                erme.setLongMax(longMax);
                erme.setTipo(tipoRequerido);
                erme.setpEntera(pEntera);
                erme.setpDecimal(pDecimal);
                throw erme;
            }
        }
        return seg;
    }

    private EcaSegPreparadoresVO mapearEcaInsPreparadoresVO(HSSFRow row) throws Exception {
        EcaSegPreparadoresVO seg = null;
        String campo = "";
        String tipoRequerido = "";
        int longMax = -1;
        int pEntera = -1;
        int pDecimal = -1;
        if (row != null && !MeLanbide35Utils.esFilaVacia(16, row)) {
            try {
                SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide35.FORMATO_FECHA);
                HSSFCell cell = null;
                String valorString;
                seg = new EcaSegPreparadoresVO();

                campo = "1";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.STRING_NIF;
                longMax = 10;
                pEntera = -1;
                pDecimal = -1;
                int celdaIni = row.getFirstCellNum();
                cell = row.getCell(celdaIni);
                valorString = (String) MeLanbide35Utils.getValorCelda(cell, tipoRequerido);

                if (valorString != null && !valorString.equals("")) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        seg.setNif(valorString.toUpperCase());
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "2";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.STRING;
                longMax = 200;
                pEntera = -1;
                pDecimal = -1;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = (String) MeLanbide35Utils.getValorCelda(cell, tipoRequerido);

                if (valorString != null && !valorString.equals("")) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        seg.setNombre(valorString.toUpperCase());
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "3";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.NUMERICO;
                longMax = -1;
                pEntera = 1;
                pDecimal = -1;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                String tipo_hombre = ConstantesMeLanbide35.SEXO.SEXO_HOMBRE;
                String tipo_mujer = ConstantesMeLanbide35.SEXO.SEXO_MUJER;
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");

                    if (valorString.equals(tipo_hombre)) {
                        valorString = ConstantesMeLanbide35.SEXO_NUM.SEXO_NUM_HOMBRE;
                    }
                    if (valorString.equals(tipo_mujer)) {
                        valorString = ConstantesMeLanbide35.SEXO_NUM.SEXO_NUM_MUJER;
                    }

                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        //valorString = valorString.replaceAll("\\.", ",");
                        //valorString = valorString.substring(0, valorString.indexOf(","));
                        seg.setSexo(new BigDecimal(valorString).intValue());
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "4";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.FECHA;
                longMax = -1;
                pEntera = -1;
                pDecimal = -1;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        seg.setFecNacimiento(format.parse(valorString));
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "5";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.NUMERICO;
                longMax = -1;
                pEntera = 1;
                pDecimal = -1;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                String psiquico = ConstantesMeLanbide35.CODIGOS_TIPO_DISCAPACIDAD.PSIQUICO;
                String fisico = ConstantesMeLanbide35.CODIGOS_TIPO_DISCAPACIDAD.FISICO;
                String sensorial = ConstantesMeLanbide35.CODIGOS_TIPO_DISCAPACIDAD.SENSORIAL;
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if (valorString.equals(psiquico)) {
                        valorString = ConstantesMeLanbide35.CODIGOS_TIPO_DISCAPACIDAD_NUM.PSIQUICO;
                    }

                    if (valorString.equals(fisico)) {
                        valorString = ConstantesMeLanbide35.CODIGOS_TIPO_DISCAPACIDAD_NUM.FISICO;
                    }

                    if (valorString.equals(sensorial)) {
                        valorString = ConstantesMeLanbide35.CODIGOS_TIPO_DISCAPACIDAD_NUM.SENSORIAL;
                    }

                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        //valorString = valorString.replaceAll("\\.", ",");
                        //valorString = valorString.substring(0, valorString.indexOf(","));          
                        seg.setTipoDiscapacidad(new BigDecimal(valorString).intValue());
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "6";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.NUMERICO;
                longMax = -1;
                pEntera = 1;
                pDecimal = -1;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                String menor_33 = ConstantesMeLanbide35.GRAVEDAD_DISCAPACIDAD.MENOR_33;
                String entre_33_65 = ConstantesMeLanbide35.GRAVEDAD_DISCAPACIDAD.ENTRE_33_65;
                String mayor_65 = ConstantesMeLanbide35.GRAVEDAD_DISCAPACIDAD.MAYOR_65;
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if (valorString.equals(menor_33)) {
                        valorString = ConstantesMeLanbide35.CODIGOS_GRAVEDAD_DISCAPACIDAD.MENOR_33;
                    }
                    if (valorString.equals(entre_33_65)) {
                        valorString = ConstantesMeLanbide35.CODIGOS_GRAVEDAD_DISCAPACIDAD.ENTRE_33_65;
                    }
                    if (valorString.equals(mayor_65)) {
                        valorString = ConstantesMeLanbide35.CODIGOS_GRAVEDAD_DISCAPACIDAD.MAYOR_65;
                    }

                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        //valorString = valorString.replaceAll("\\.", ",");
                        //valorString = valorString.substring(0, valorString.indexOf(","));            
                        seg.setGravedad(new BigDecimal(valorString).intValue());
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "7";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.NUMERICO;
                longMax = -1;
                pEntera = 2;
                pDecimal = -1;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                String indefinido = ConstantesMeLanbide35.CODIGOS_TIPO_CONTRATO.INDEFINIDO;
                String temporal = ConstantesMeLanbide35.CODIGOS_TIPO_CONTRATO.TEMPORAL;
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");

                    if (valorString.equals(indefinido)) {
                        valorString = ConstantesMeLanbide35.CODIGOS_TIPO_CONTRATO_NUM.INDEFINIDO;
                    }

                    if (valorString.equals(temporal)) {
                        valorString = ConstantesMeLanbide35.CODIGOS_TIPO_CONTRATO_NUM.TEMPORAL;
                    }

                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        //valorString = valorString.replaceAll("\\.", ",");
                        //valorString = valorString.substring(0, valorString.indexOf(","));
                        seg.setTipoContrato(new BigDecimal(valorString).intValue());
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "8";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL;
                longMax = -1;
                pEntera = 5;
                pDecimal = 2;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        seg.setPorcJornada(new BigDecimal(valorString));
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                //La columna colectivo se ignora
                celdaIni++;

                campo = "10";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.FECHA;
                longMax = -1;
                pEntera = -1;
                pDecimal = -1;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        seg.setFecIni(format.parse(valorString));
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "11";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.FECHA;
                longMax = -1;
                pEntera = -1;
                pDecimal = -1;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        seg.setFecFin(format.parse(valorString));
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "12";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.STRING;
                longMax = 100;
                pEntera = -1;
                pDecimal = -1;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = (String) MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        seg.setEmpresa(valorString);
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "13";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.STRING_NIF;
                longMax = 10;
                pEntera = -1;
                pDecimal = -1;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = (String) MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        seg.setNifPreparador(valorString);
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "14";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL;
                longMax = -1;
                pEntera = 8;
                pDecimal = 2;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        seg.setHorasCont(new BigDecimal(valorString));
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "15";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.STRING;
                longMax = 200;
                pEntera = -1;
                pDecimal = -1;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = (String) MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "<br/>");
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        seg.setObservaciones(valorString);
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                /*campo = "14";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.STRING;
                longMax = 100;
                pEntera = -1;
                pDecimal = -1;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = (String)MeLanbide35Utils.getValorCelda(cell);
                if(valorString != null)
                {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if(MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true))
                    {
                        seg.setNomPersContacto(valorString);
                    }
                    else
                    {
                        throw new DatoExcelNoValidoException();
                    }
                }
                
                campo = "15";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.STRING;
                longMax = 200;
                pEntera = -1;
                pDecimal = -1;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = (String)MeLanbide35Utils.getValorCelda(cell);
                if(valorString != null)
                {
                    valorString = reemplazarSaltosDeLinea(valorString, "<br/>");
                    if(MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true))
                    {
                        seg.setObservaciones(valorString);
                    }
                    else
                    {
                        throw new DatoExcelNoValidoException();
                    }
                }*/
            } catch (Exception ex) {
                ExcelRowMappingException erme = new ExcelRowMappingException(campo);
                erme.setLongMax(longMax);
                erme.setTipo(tipoRequerido);
                erme.setpEntera(pEntera);
                erme.setpDecimal(pDecimal);
                throw erme;
            }
        }
        return seg;
    }

    private FilaSegPreparadoresVO mapearFilaSegPreparadoresVO(ResultSet rs) throws Exception {
        DecimalFormatSymbols simbolo = new DecimalFormatSymbols();
        simbolo.setDecimalSeparator(',');
        simbolo.setGroupingSeparator('.');
        DecimalFormat formateador = new DecimalFormat("#,##0.00", simbolo);

        String valorStr = null;
        Date valorFecha = null;
        Integer valorNum = null;
        BigDecimal valorBD = null;
        BigDecimal jvalorBD = null;
        SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide35.FORMATO_FECHA);
        FilaSegPreparadoresVO fila = new FilaSegPreparadoresVO();
        int idseg = rs.getInt("ECA_SEG_PREPARADORES_COD");
        if (!rs.wasNull()) {
            fila.setIdseg("" + idseg);
        }
        int idPrep = rs.getInt("ECA_JUS_PREPARADORES_COD");
        if (!rs.wasNull()) {
            fila.setCodPreparador("" + idPrep);
        }
        valorNum = rs.getInt("ECA_SEG_TIPO");
        if (valorNum != null) {
            if (valorNum == 0) {
                fila.setTipo("Seguimiento");
            } else {
                fila.setTipo("Insercion");
            }
        }
        fila.setTipo(valorNum != null ? valorNum + "" : "");
        fila.setCodPreparador("" + idPrep);
        valorStr = rs.getString("ECA_SEG_NIF");
        fila.setNif(valorStr != null ? valorStr.toUpperCase() : "-");
        valorStr = rs.getString("ECA_SEG_NOMBRE");
        fila.setNombreApe(valorStr != null ? valorStr.toUpperCase() : "-");
        valorFecha = rs.getDate("ECA_SEG_FECNACIMIENTO");
        fila.setFecNacimiento(valorFecha != null ? format.format(valorFecha) : "-");
        valorNum = rs.getInt("ECA_SEG_SEXO");
        if (valorNum != null) {
            //if (valorNum==0)    fila.setSexo("Hombre");
            //else  fila.setSexo("Mujer");
            fila.setSexo("" + valorNum);
        } else {
            fila.setSexo("");
        }
        valorFecha = rs.getDate("ECA_SEG_FECINI");
        fila.setFecIni(valorFecha != null ? format.format(valorFecha) : "-");
        valorFecha = rs.getDate("ECA_SEG_FECFIN");
        fila.setFecFin(valorFecha != null ? format.format(valorFecha) : "-");
        valorBD = rs.getBigDecimal("ECA_SEG_HORAS");
        fila.setHorasCont(valorBD != null ? formateador.format(new BigDecimal(MeLanbide35Utils.redondearDecimalesString(valorBD, 2))) : "-");
        fila.setCodTipoDiscapacidad(rs.getInt("ECA_SEG_TIPODISCAPACIDAD"));
        if (rs.wasNull()) {
            fila.setCodTipoDiscapacidad(null);
        }
        valorStr = rs.getString("TDISCAPACIDAD");
        fila.setCodGravedad(rs.getInt("ECA_SEG_DISCGRAVEDAD"));
        if (rs.wasNull()) {
            fila.setCodGravedad(null);
        }
        fila.setTipoDiscapacidad(valorStr != null ? valorStr.toUpperCase() : "-");
        valorStr = rs.getString("DISCGRAVEDAD");
        fila.setGravedad(valorStr != null ? valorStr.toUpperCase() : "-");
        fila.setCodTipoContrato(rs.getInt("ECA_SEG_TIPOCONTRATO"));
        if (rs.wasNull()) {
            fila.setCodTipoContrato(null);
        }
        valorStr = rs.getString("TIPOCONTRATO");
        fila.setTipoContrato(valorStr != null ? valorStr.toUpperCase() : "-");
        valorBD = rs.getBigDecimal("ECA_SEG_PORCJORNADA");
        fila.setPorcJornada(valorBD != null ? formateador.format(new BigDecimal(MeLanbide35Utils.redondearDecimalesString(valorBD, 2))) : "-");
        valorStr = rs.getString("ECA_SEG_EMPRESA");
        fila.setEmpresa(valorStr != null ? valorStr.toUpperCase() : "-");
        valorStr = rs.getString("ECA_PREP_NIF");
        fila.setNifPreparador(valorStr != null ? valorStr.toUpperCase() : "-");
        valorFecha = rs.getDate("ECA_SEG_FECSEGUIMIENTO");
        fila.setFecSeguimiento(valorFecha != null ? format.format(valorFecha) : "-");
        //valorStr = rs.getString("ECA_SEG_PERSCONTACTO");
        //fila.setNombrePersContacto(valorStr != null ? valorStr.toUpperCase() : "-");  
        valorStr = rs.getString("ECA_SEG_OBSERVACIONES");
        valorStr = reemplazarSaltosDeLinea(valorStr, "<br/>");
        fila.setObservaciones(valorStr != null ? valorStr.toUpperCase() : "-");
        fila.setFinContratoDespido(rs.getInt("ECA_SEG_FINCONTRATO"));
        if (rs.wasNull()) {
            fila.setFinContratoDespido(null);
        }
        return fila;
    }

    private FilaInsPreparadoresVO mapearFilaInsPreparadoresVO(ResultSet rs) throws Exception {
        DecimalFormatSymbols simbolo = new DecimalFormatSymbols();
        simbolo.setDecimalSeparator(',');
        simbolo.setGroupingSeparator('.');
        DecimalFormat formateador = new DecimalFormat("#,##0.00", simbolo);

        String valorStr = null;
        Date valorFecha = null;
        Integer valorNum = null;
        BigDecimal valorBD = null;
        BigDecimal jvalorBD = null;
        SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide35.FORMATO_FECHA);
        FilaInsPreparadoresVO fila = new FilaInsPreparadoresVO();
        int idseg = rs.getInt("ECA_SEG_PREPARADORES_COD");
        if (!rs.wasNull()) {
            fila.setIdseg("" + idseg);
        }
        int idPrep = rs.getInt("ECA_JUS_PREPARADORES_COD");
        if (!rs.wasNull()) {
            fila.setCodPreparador("" + idPrep);
        }
        valorNum = rs.getInt("ECA_SEG_TIPO");
        if (valorNum != null) {
            if (valorNum == 0) {
                fila.setTipo("Seguimiento");
            } else {
                fila.setTipo("Insercion");
            }
        }
        fila.setTipo(valorNum != null ? valorNum + "" : "");
        fila.setCodPreparador("" + idPrep);
        valorStr = rs.getString("ECA_SEG_NIF");
        fila.setNif(valorStr != null ? valorStr.toUpperCase() : "-");
        valorStr = rs.getString("ECA_SEG_NOMBRE");
        fila.setNombreApe(valorStr != null ? valorStr.toUpperCase() : "-");
        valorFecha = rs.getDate("ECA_SEG_FECNACIMIENTO");
        fila.setFecNacimiento(valorFecha != null ? format.format(valorFecha) : "-");
        valorNum = rs.getInt("ECA_SEG_SEXO");
        if (valorNum != null) {
            //if (valorNum==0)    fila.setSexo("Hombre");
            //else  fila.setSexo("Mujer");
            fila.setSexo("" + valorNum);
        } else {
            fila.setSexo("");
        }
        valorFecha = rs.getDate("ECA_SEG_FECINI");
        fila.setFecIni(valorFecha != null ? format.format(valorFecha) : "-");
        valorFecha = rs.getDate("ECA_SEG_FECFIN");
        fila.setFecFin(valorFecha != null ? format.format(valorFecha) : "-");
        valorBD = rs.getBigDecimal("ECA_SEG_HORAS");
        fila.setHorasCont(valorBD != null ? formateador.format(new BigDecimal(MeLanbide35Utils.redondearDecimalesString(valorBD, 2))) : "-");
        fila.setCodTipoDiscapacidad(rs.getInt("ECA_SEG_TIPODISCAPACIDAD"));
        if (rs.wasNull()) {
            fila.setCodTipoDiscapacidad(null);
        }
        valorStr = rs.getString("TDISCAPACIDAD");
        fila.setCodGravedad(rs.getInt("ECA_SEG_DISCGRAVEDAD"));
        if (rs.wasNull()) {
            fila.setCodGravedad(null);
        }
        fila.setTipoDiscapacidad(valorStr != null ? valorStr.toUpperCase() : "-");
        valorStr = rs.getString("DISCGRAVEDAD");
        fila.setGravedad(valorStr != null ? valorStr.toUpperCase() : "-");
        fila.setCodTipoContrato(rs.getInt("ECA_SEG_TIPOCONTRATO"));
        if (rs.wasNull()) {
            fila.setCodTipoContrato(null);
        }
        valorStr = rs.getString("TIPOCONTRATO");
        fila.setTipoContrato(valorStr != null ? valorStr.toUpperCase() : "-");
        valorBD = rs.getBigDecimal("ECA_SEG_PORCJORNADA");
        fila.setPorcJornada(valorBD != null ? formateador.format(new BigDecimal(MeLanbide35Utils.redondearDecimalesString(valorBD, 2))) : "-");
        valorStr = rs.getString("ECA_SEG_EMPRESA");
        fila.setEmpresa(valorStr != null ? valorStr.toUpperCase() : "-");
        valorStr = rs.getString("ECA_PREP_NIF");
        fila.setNifPreparador(valorStr != null ? valorStr.toUpperCase() : "-");
        //valorFecha = rs.getDate("ECA_SEG_FECSEGUIMIENTO");
        //fila.setFecSeguimiento(valorFecha != null ? format.format(valorFecha) : "-");  
        valorStr = rs.getString("ECA_SEG_PERSCONTACTO");
        //fila.setNombrePersContacto(valorStr != null ? valorStr.toUpperCase() : "-");  
        valorStr = rs.getString("ECA_SEG_OBSERVACIONES");
        valorStr = reemplazarSaltosDeLinea(valorStr, "<br/>");
        fila.setObservaciones(valorStr != null ? valorStr.toUpperCase() : "-");
        valorStr = rs.getString("COLECTIVO");
        fila.setColectivo(valorStr != null ? valorStr.toUpperCase() : "-");
        fila.setFinContratoDespido(rs.getInt("ECA_SEG_FINCONTRATO"));
        if (rs.wasNull()) {
            fila.setFinContratoDespido(null);
        }
        return fila;
    }

    public EcaJusPreparadoresVO fromFilaPrepJusVOToPrepJusVO(FilaPreparadorJustificacionVO fila) throws Exception {
        SimpleDateFormat formatoFecha = new SimpleDateFormat(ConstantesMeLanbide35.FORMATO_FECHA);
        EcaJusPreparadoresVO prep = new EcaJusPreparadoresVO();
        prep.setCoste(fila.getCostesSalarialesSS() != null && !fila.getCostesSalarialesSS().equals("") && !fila.getCostesSalarialesSS().equals("-") ? fromFormateadoToDecimal(fila.getCostesSalarialesSS()) : null);
        prep.setFecFin(fila.getFechaFin() != null && !fila.getFechaFin().equals("") && !fila.getFechaFin().equals("-") ? formatoFecha.parse(fila.getFechaFin()) : null);
        prep.setFecIni(fila.getFechaInicio() != null && !fila.getFechaInicio().equals("") && !fila.getFechaInicio().equals("-") ? formatoFecha.parse(fila.getFechaInicio()) : null);
        prep.setHorasCont(fila.getHorasContrato() != null && !fila.getHorasContrato().equals("") && !fila.getHorasContrato().equals("-") ? fromFormateadoToDecimal(fila.getHorasContrato()) : null);
        prep.setHorasEca(fila.getHorasDedicacionECA() != null && !fila.getHorasDedicacionECA().equals("") && !fila.getHorasDedicacionECA().equals("-") ? fromFormateadoToDecimal(fila.getHorasDedicacionECA()) : null);
        prep.setHorasJC(fila.getHorasAnuales() != null && !fila.getHorasAnuales().equals("") && !fila.getHorasAnuales().equals("-") ? fromFormateadoToDecimal(fila.getHorasAnuales()) : null);
        prep.setImpSSECA(fila.getCostesSalarialesSSEca() != null && !fila.getCostesSalarialesSSEca().equals("") && !fila.getCostesSalarialesSSEca().equals("-") ? fromFormateadoToDecimal(fila.getCostesSalarialesSSEca()) : null);
        prep.setImpSSJC(fila.getCostesSalarialesSSJor() != null && !fila.getCostesSalarialesSSJor().equals("") && !fila.getCostesSalarialesSSJor().equals("-") ? fromFormateadoToDecimal(fila.getCostesSalarialesSSJor()) : null);
        prep.setImpSSJR(fila.getCostesSalarialesSSPorJor() != null && !fila.getCostesSalarialesSSPorJor().equals("") && !fila.getCostesSalarialesSSPorJor().equals("-") ? fromFormateadoToDecimal(fila.getCostesSalarialesSSPorJor()) : null);
        prep.setImpSegAnt(fila.getImporte() != null && !fila.getImporte().equals("") && !fila.getImporte().equals("-") ? fromFormateadoToDecimal(fila.getImporte()) : null);
        prep.setInsImporte(fila.getInserciones() != null && !fila.getInserciones().equals("") && !fila.getInserciones().equals("-") ? fromFormateadoToDecimal(fila.getInserciones()) : null);
        prep.setInsSegImporte(fila.getSeguimientosInserciones() != null && !fila.getSeguimientosInserciones().equals("") && !fila.getSeguimientosInserciones().equals("-") ? fromFormateadoToDecimal(fila.getSeguimientosInserciones()) : null);
        prep.setNif(fila.getNif() != null && !fila.getNif().equals("") && !fila.getNif().equals("-") ? fila.getNif() : "");
        prep.setNombre(fila.getNombreApel() != null && !fila.getNombreApel().equals("") && !fila.getNombreApel().equals("-") ? fila.getNombreApel() : "");
        prep.setSolPreparadoresCod(fila.getId() != null && !fila.getId().equals("") && !fila.getId().equals("-") ? Integer.parseInt(fila.getId()) : null);
        prep.setTipoContrato(fila.getCodTipoContrato());
        prep.setTipoSust(fila.getTipoSust());
        prep.setJusPreparadorOrigen(fila.getJusPreparadorOrigen());
        return prep;
    }

    public EcaSegPreparadoresVO fromFilaSegPrepVOToSegPrepVO(FilaSegPreparadoresVO fila) throws Exception {
        SimpleDateFormat formatoFecha = new SimpleDateFormat(ConstantesMeLanbide35.FORMATO_FECHA);
        EcaSegPreparadoresVO seg = new EcaSegPreparadoresVO();
        seg.setFecSeguimiento(fila.getFecSeguimiento() != null && !fila.getFecSeguimiento().equals("") && !fila.getFecSeguimiento().equals("-") ? formatoFecha.parse(fila.getFecSeguimiento()) : null);
        seg.setNif(fila.getNif() != null && !fila.getNif().equals("") && !fila.getNif().equals("-") ? fila.getNif() : "");
        seg.setNombre(fila.getNombreApe() != null && !fila.getNombreApe().equals("") && !fila.getNombreApe().equals("-") ? fila.getNombreApe() : "");
        seg.setSexo(fila.getSexo() != null && !fila.getSexo().equals("") && !fila.getSexo().equals("-") ? Integer.parseInt(fila.getSexo()) : null);
        seg.setFecNacimiento(fila.getFecNacimiento() != null && !fila.getFecNacimiento().equals("") && !fila.getFecNacimiento().equals("-") ? formatoFecha.parse(fila.getFecNacimiento()) : null);
        seg.setTipoDiscapacidad(fila.getCodTipoDiscapacidad());
        seg.setGravedad(fila.getCodGravedad());
        seg.setTipoContrato(fila.getCodTipoContrato());
        seg.setPorcJornada(fila.getPorcJornada() != null && !fila.getPorcJornada().equals("") && !fila.getPorcJornada().equals("-") ? fromFormateadoToDecimal(fila.getPorcJornada()) : null);
        seg.setFecFin(fila.getFecFin() != null && !fila.getFecFin().equals("") && !fila.getFecFin().equals("-") ? formatoFecha.parse(fila.getFecFin()) : null);
        seg.setFecIni(fila.getFecIni() != null && !fila.getFecIni().equals("") && !fila.getFecIni().equals("-") ? formatoFecha.parse(fila.getFecIni()) : null);
        seg.setNifPreparador(fila.getNifPreparador() != null && !fila.getNifPreparador().equals("") && !fila.getNifPreparador().equals("-") ? fila.getNifPreparador() : "");
        seg.setEmpresa(fila.getEmpresa() != null && !fila.getEmpresa().equals("") && !fila.getEmpresa().equals("-") ? fila.getEmpresa() : null);
        //seg.setNomPersContacto(fila.getNombrePersContacto() != null && !fila.getNombrePersContacto().equals("") && !fila.getNombrePersContacto().equals("-") ? fila.getNombrePersContacto() : null);
        seg.setHorasCont(fila.getHorasCont() != null && !fila.getHorasCont().equals("") && !fila.getHorasCont().equals("-") ? fromFormateadoToDecimal(fila.getHorasCont()) : null);
        seg.setObservaciones(fila.getObservaciones() != null && !fila.getObservaciones().equals("") && !fila.getObservaciones().equals("-") ? fila.getObservaciones() : null);
        seg.setTipo(fila.getTipo() != null && !fila.getTipo().equals("") && !fila.getTipo().equals("-") ? Integer.parseInt(fila.getTipo()) : null);
        seg.setJusPreparadoresCod(fila.getCodPreparador() != null && !fila.getCodPreparador().equals("") && !fila.getCodPreparador().equals("-") ? Integer.parseInt(fila.getCodPreparador()) : null);
        seg.setSegPreparadoresCod(fila.getIdseg() != null && !fila.getIdseg().equals("") && !fila.getIdseg().equals("-") ? Integer.parseInt(fila.getIdseg()) : null);
        return seg;
    }

    public EcaInsPreparadoresVO fromFilaInsPrepVOToInsPrepVO(FilaInsPreparadoresVO fila) throws Exception {
        SimpleDateFormat formatoFecha = new SimpleDateFormat(ConstantesMeLanbide35.FORMATO_FECHA);
        EcaInsPreparadoresVO seg = new EcaInsPreparadoresVO();
        seg.setFecSeguimiento(fila.getFecSeguimiento() != null && !fila.getFecSeguimiento().equals("") && !fila.getFecSeguimiento().equals("-") ? formatoFecha.parse(fila.getFecSeguimiento()) : null);
        seg.setNif(fila.getNif() != null && !fila.getNif().equals("") && !fila.getNif().equals("-") ? fila.getNif() : "");
        seg.setNombre(fila.getNombreApe() != null && !fila.getNombreApe().equals("") && !fila.getNombreApe().equals("-") ? fila.getNombreApe() : "");
        seg.setSexo(fila.getSexo() != null && !fila.getSexo().equals("") && !fila.getSexo().equals("-") ? Integer.parseInt(fila.getSexo()) : null);
        seg.setFecNacimiento(fila.getFecNacimiento() != null && !fila.getFecNacimiento().equals("") && !fila.getFecNacimiento().equals("-") ? formatoFecha.parse(fila.getFecNacimiento()) : null);
        seg.setTipoDiscapacidad(fila.getCodTipoDiscapacidad());
        seg.setGravedad(fila.getCodGravedad());
        seg.setTipoContrato(fila.getCodTipoContrato());
        seg.setPorcJornada(fila.getPorcJornada() != null && !fila.getPorcJornada().equals("") && !fila.getPorcJornada().equals("-") ? fromFormateadoToDecimal(fila.getPorcJornada()) : null);
        seg.setFecFin(fila.getFecFin() != null && !fila.getFecFin().equals("") && !fila.getFecFin().equals("-") ? formatoFecha.parse(fila.getFecFin()) : null);
        seg.setFecIni(fila.getFecIni() != null && !fila.getFecIni().equals("") && !fila.getFecIni().equals("-") ? formatoFecha.parse(fila.getFecIni()) : null);
        seg.setNifPreparador(fila.getNifPreparador() != null && !fila.getNifPreparador().equals("") && !fila.getNifPreparador().equals("-") ? fila.getNifPreparador() : "");
        seg.setEmpresa(fila.getEmpresa() != null && !fila.getEmpresa().equals("") && !fila.getEmpresa().equals("-") ? fila.getEmpresa() : null);
        //seg.setNomPersContacto(fila.getNombrePersContacto() != null && !fila.getNombrePersContacto().equals("") && !fila.getNombrePersContacto().equals("-") ? fila.getNombrePersContacto() : null);
        seg.setHorasCont(fila.getHorasCont() != null && !fila.getHorasCont().equals("") && !fila.getHorasCont().equals("-") ? fromFormateadoToDecimal(fila.getHorasCont()) : null);
        seg.setObservaciones(fila.getObservaciones() != null && !fila.getObservaciones().equals("") && !fila.getObservaciones().equals("-") ? fila.getObservaciones() : null);
        seg.setTipo(fila.getTipo() != null && !fila.getTipo().equals("") && !fila.getTipo().equals("-") ? Integer.parseInt(fila.getTipo()) : null);
        seg.setJusPreparadoresCod(fila.getCodPreparador() != null && !fila.getCodPreparador().equals("") && !fila.getCodPreparador().equals("-") ? Integer.parseInt(fila.getCodPreparador()) : null);
        seg.setSegPreparadoresCod(fila.getIdseg() != null && !fila.getIdseg().equals("") && !fila.getIdseg().equals("-") ? Integer.parseInt(fila.getIdseg()) : null);
        seg.setFinContratoDespido(fila.getFinContratoDespido());
        return seg;
    }

    public EcaJusProspectoresVO fromFilaProsJusVOToProsJusVO(FilaProspectorJustificacionVO fila) throws Exception {
        SimpleDateFormat formatoFecha = new SimpleDateFormat(ConstantesMeLanbide35.FORMATO_FECHA);
        EcaJusProspectoresVO pros = new EcaJusProspectoresVO();
        //pros.setImpSSECA(fila.getCostesSalarialesSSEca() != null && !fila.getCostesSalarialesSSEca().equals("") && !fila.getCostesSalarialesSSEca().equals("-") ? new BigDecimal(fila.getCostesSalarialesSSEca().replaceAll(",", "\\.")) : null);
        pros.setFecFin(fila.getFechaFin() != null && !fila.getFechaFin().equals("") && !fila.getFechaFin().equals("-") ? formatoFecha.parse(fila.getFechaFin()) : null);
        pros.setFecIni(fila.getFechaInicio() != null && !fila.getFechaInicio().equals("") && !fila.getFechaInicio().equals("-") ? formatoFecha.parse(fila.getFechaInicio()) : null);
        pros.setHorasCont(fila.getHorasContrato() != null && !fila.getHorasContrato().equals("") && !fila.getHorasContrato().equals("-") ? fromFormateadoToDecimal(fila.getHorasContrato()) : null);
        pros.setHorasEca(fila.getHorasDedicacionECA() != null && !fila.getHorasDedicacionECA().equals("") && !fila.getHorasDedicacionECA().equals("-") ? fromFormateadoToDecimal(fila.getHorasDedicacionECA()) : null);
        pros.setHorasJC(fila.getHorasAnuales() != null && !fila.getHorasAnuales().equals("") && !fila.getHorasAnuales().equals("-") ? fromFormateadoToDecimal(fila.getHorasAnuales()) : null);
        pros.setImpSSECA(fila.getCostesSalarialesSSEca() != null && !fila.getCostesSalarialesSSEca().equals("") && !fila.getCostesSalarialesSSEca().equals("-") ? fromFormateadoToDecimal(fila.getCostesSalarialesSSEca()) : null);
        pros.setImpSSJC(fila.getCostesSalarialesSSJor() != null && !fila.getCostesSalarialesSSJor().equals("") && !fila.getCostesSalarialesSSJor().equals("-") ? fromFormateadoToDecimal(fila.getCostesSalarialesSSJor()) : null);
        pros.setImpSSJR(fila.getCostesSalarialesSSPorJor() != null && !fila.getCostesSalarialesSSPorJor().equals("") && !fila.getCostesSalarialesSSPorJor().equals("-") ? fromFormateadoToDecimal(fila.getCostesSalarialesSSPorJor()) : null);
        pros.setImpVisitas(fila.getVisitasImp() != null && !fila.getVisitasImp().equals("") && !fila.getVisitasImp().equals("-") ? fromFormateadoToDecimal(fila.getVisitasImp()) : null);
        pros.setImpTotalSolic(fila.getCoste() != null && !fila.getCoste().equals("") && !fila.getCoste().equals("-") ? fromFormateadoToDecimal(fila.getCoste()) : null);
        pros.setNif(fila.getNif() != null && !fila.getNif().equals("") && !fila.getNif().equals("-") ? fila.getNif() : "");
        pros.setNombre(fila.getNombreApel() != null && !fila.getNombreApel().equals("") && !fila.getNombreApel().equals("-") ? fila.getNombreApel() : "");
        pros.setSolProspectoresCod(fila.getId() != null && !fila.getId().equals("") && !fila.getId().equals("-") ? Integer.parseInt(fila.getId()) : null);
        pros.setTipoSust(fila.getTipoSust());
        pros.setJusProspectorOrigen(fila.getJusProspectorOrigen());
        return pros;
    }

    private FilaProspectorJustificacionVO mapearFilaProspectorJustificacionVO(ResultSet rs) throws Exception {
        DecimalFormatSymbols simbolo = new DecimalFormatSymbols();
        simbolo.setDecimalSeparator(',');
        simbolo.setGroupingSeparator('.');
        DecimalFormat formateador = new DecimalFormat("#,##0.00", simbolo);

        boolean esSustituto = false;
        String valorStr = null;
        Date valorFecha = null;
        BigDecimal valorBD = null;
        BigDecimal jvalorBD = null;
        SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide35.FORMATO_FECHA);
        FilaProspectorJustificacionVO fila = new FilaProspectorJustificacionVO();
        int id = rs.getInt("ECA_JUS_PROSPECTORES_COD");
        if (!rs.wasNull()) {
            fila.setId("" + id);
            int origen = rs.getInt("ORIGEN");
            if (!rs.wasNull()) {
                if (id != origen) {
                    esSustituto = true;
                }
            }
        }
        valorStr = rs.getString("ECA_PROS_NIF");
        /*if(valorStr != null)
        {
            if(esSustituto)
            {
                valorStr = ""+valorStr+" - (S)";
            }
        }
        else
        {
            valorStr = "-";
        }*/
        fila.setNif(valorStr != null ? valorStr.toUpperCase() : "-");
        valorStr = rs.getString("ECA_PROS_NOMBRE");
        fila.setNombreApel(valorStr != null ? valorStr.toUpperCase() : "-");
        valorFecha = rs.getDate("ECA_PROS_FECINI");
        fila.setFechaInicio(valorFecha != null ? format.format(valorFecha) : "-");
        valorFecha = rs.getDate("ECA_PROS_FECFIN");
        fila.setFechaFin(valorFecha != null ? format.format(valorFecha) : "-");
        valorBD = rs.getBigDecimal("ECA_PROS_HORAS_JC");
        fila.setHorasAnuales(valorBD != null ? formateador.format(new BigDecimal(MeLanbide35Utils.redondearDecimalesString(valorBD, 2))) : "-");
        valorBD = rs.getBigDecimal("ECA_PROS_HORAS_CONT");
        fila.setHorasContrato(valorBD != null ? formateador.format(new BigDecimal(MeLanbide35Utils.redondearDecimalesString(valorBD, 2))) : "-");
        valorBD = rs.getBigDecimal("ECA_PROS_HORAS_ECA");
        fila.setHorasDedicacionECA(valorBD != null ? formateador.format(new BigDecimal(MeLanbide35Utils.redondearDecimalesString(valorBD, 2))) : "-");
        valorBD = rs.getBigDecimal("ECA_PROS_IMP_SS_JC");
        fila.setCostesSalarialesSSJor(valorBD != null ? formateador.format(new BigDecimal(MeLanbide35Utils.redondearDecimalesString(valorBD, 2))) : "-");
        valorBD = rs.getBigDecimal("ECA_PROS_IMP_SS_JR");
        fila.setCostesSalarialesSSPorJor(valorBD != null ? formateador.format(new BigDecimal(MeLanbide35Utils.redondearDecimalesString(valorBD, 2))) : "-");
        valorBD = rs.getBigDecimal("ECA_PROS_IMP_SS_ECA");
        fila.setCostesSalarialesSSEca(valorBD != null ? formateador.format(new BigDecimal(MeLanbide35Utils.redondearDecimalesString(valorBD, 2))) : "-");
        valorBD = rs.getBigDecimal("ECA_PROS_VISITAS");
        jvalorBD = rs.getBigDecimal("VISITASREAL") != null ? rs.getBigDecimal("VISITASREAL") : BigDecimal.ZERO;
        fila.setVisitas(valorBD != null ? valorBD.toPlainString().replaceAll("\\.", ",") + " - " + jvalorBD.toPlainString().replaceAll("\\.", ",") : BigDecimal.ZERO.toPlainString() + "-" + jvalorBD.toPlainString().replaceAll("\\.", ","));
        valorBD = rs.getBigDecimal("IMPORTEVISITAS") != null ? rs.getBigDecimal("IMPORTEVISITAS") : BigDecimal.ZERO;;
        fila.setVisitasImp(valorBD != null ? formateador.format(valorBD) : "-");
        valorBD = rs.getBigDecimal("ECA_PROS_COSTE") != null ? rs.getBigDecimal("ECA_PROS_COSTE") : BigDecimal.ZERO;;
        fila.setCoste(valorBD != null ? formateador.format(new BigDecimal(MeLanbide35Utils.redondearDecimalesString(valorBD, 2))) : "-");
        fila.setEsSustituto(esSustituto);
        fila.setTipoSust(rs.getString("ECA_PROS_TIPO_SUST"));
        fila.setJusProspectorOrigen(rs.getInt("ECA_PROS_CODORIGEN"));
        if (rs.wasNull()) {
            fila.setJusProspectorOrigen(null);
        }
        return fila;
    }

    private EcaJusProspectoresVO mapearEcaJusProspectoresVO(ResultSet rs) throws Exception {
        EcaJusProspectoresVO pro = new EcaJusProspectoresVO();
        pro.setJusProspectoresCod(rs.getInt("ECA_JUS_PROSPECTORES_COD"));
        if (rs.wasNull()) {
            pro.setJusProspectoresCod(null);
        }
        pro.setSolProspectoresCod(rs.getInt("ECA_SOL_PROSPECTORES_COD"));
        if (rs.wasNull()) {
            pro.setSolProspectoresCod(null);
        }
        pro.setNif(rs.getString("ECA_PROS_NIF"));
        pro.setNombre(rs.getString("ECA_PROS_NOMBRE"));
        pro.setFecIni(rs.getDate("ECA_PROS_FECINI"));
        pro.setFecFin(rs.getDate("ECA_PROS_FECFIN"));
        pro.setHorasJC(rs.getBigDecimal("ECA_PROS_HORAS_JC"));
        pro.setHorasCont(rs.getBigDecimal("ECA_PROS_HORAS_CONT"));
        pro.setHorasEca(rs.getBigDecimal("ECA_PROS_HORAS_ECA"));
        pro.setImpSSJC(rs.getBigDecimal("ECA_PROS_IMP_SS_JC"));
        pro.setImpSSJR(rs.getBigDecimal("ECA_PROS_IMP_SS_JR"));
        pro.setImpSSECA(rs.getBigDecimal("ECA_PROS_IMP_SS_ECA"));
        pro.setSolicitud(rs.getInt("ECA_PROS_SOLICITUD"));
        if (rs.wasNull()) {
            pro.setSolicitud(null);
        }
        pro.setJusProspectorOrigen(rs.getInt("ECA_PROS_CODORIGEN"));
        if (rs.wasNull()) {
            pro.setJusProspectorOrigen(null);
        }
        pro.setTipoSust(rs.getString("ECA_PROS_TIPO_SUST"));
        return pro;
    }

    public EcaVisProspectoresVO fromFilaVisProsVOToVisProsVO(FilaVisProspectoresVO fila) throws Exception {
        SimpleDateFormat formatoFecha = new SimpleDateFormat(ConstantesMeLanbide35.FORMATO_FECHA);
        EcaVisProspectoresVO visita = new EcaVisProspectoresVO();
        visita.setFecVisita(fila.getFecVisita() != null && !fila.getFecVisita().equals("") && !fila.getFecVisita().equals("-") ? formatoFecha.parse(fila.getFecVisita()) : null);
        visita.setCif(fila.getCif() != null && !fila.getCif().equals("") && !fila.getCif().equals("-") ? fila.getCif() : "");
        visita.setEmpresa(fila.getEmpresa() != null && !fila.getEmpresa().equals("") && !fila.getEmpresa().equals("-") ? fila.getEmpresa() : "");
        visita.setSector(fila.getCodSector());
        visita.setDireccion(fila.getDireccion() != null && !fila.getDireccion().equals("") && !fila.getDireccion().equals("-") ? fila.getDireccion() : null);
        visita.setCpostal(fila.getCpostal() != null && !fila.getCpostal().equals("") && !fila.getCpostal().equals("-") ? fila.getCpostal() : null);
        visita.setLocalidad(fila.getLocalidad() != null && !fila.getLocalidad().equals("") && !fila.getLocalidad().equals("-") ? fila.getLocalidad() : null);
        visita.setProvincia(fila.getCodProvincia());
        visita.setPersContacto(fila.getPersContacto() != null && !fila.getPersContacto().equals("") && !fila.getPersContacto().equals("-") ? fila.getPersContacto() : null);
        visita.setPuesto(fila.getPuesto() != null && !fila.getPuesto().equals("") && !fila.getPuesto().equals("-") ? fila.getPuesto() : null);
        visita.setMail(fila.getMail() != null && !fila.getMail().equals("") && !fila.getMail().equals("-") ? fila.getMail() : null);
        visita.setTelefono(fila.getTelefono() != null && !fila.getTelefono().equals("") && !fila.getTelefono().equals("-") ? new Integer(Integer.parseInt(fila.getTelefono())) : null);
        visita.setNifProspector(fila.getNifProspector() != null && !fila.getNifProspector().equals("") && !fila.getNifProspector().equals("-") ? fila.getNifProspector() : "");
        visita.setNumTrab(fila.getNumTrab() != null && !fila.getNumTrab().equals("") && !fila.getNumTrab().equals("-") ? new Integer(Integer.parseInt(fila.getNumTrabDisc())) : null);
        visita.setNumTrabDisc(fila.getNumTrabDisc() != null && !fila.getNumTrabDisc().equals("") && !fila.getNumTrabDisc().equals("-") ? new Integer(Integer.parseInt(fila.getNumTrabDisc())) : null);
        visita.setCumpleLismi(fila.getCodCumpleLismi());
        visita.setResultadoFinal(fila.getCodResultadoFinal());
        visita.setObservaciones(fila.getObservaciones() != null && !fila.getObservaciones().equals("") && !fila.getObservaciones().equals("-") ? fila.getObservaciones() : null);
        visita.setJusProspectoresCod(fila.getCodProspector() != null && !fila.getCodProspector().equals("") && !fila.getCodProspector().equals("-") ? Integer.parseInt(fila.getCodProspector()) : null);
        visita.setVisProspectoresCod(fila.getIdvisita() != null && !fila.getIdvisita().equals("") && !fila.getIdvisita().equals("-") ? Integer.parseInt(fila.getIdvisita()) : null);
        return visita;
    }

    private FilaVisProspectoresVO mapearFilaVisProspectoresVO(ResultSet rs) throws Exception {
        String valorStr = null;
        Date valorFecha = null;
        Integer valorNum = null;
        BigDecimal valorBD = null;
        BigDecimal jvalorBD = null;
        SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide35.FORMATO_FECHA);
        FilaVisProspectoresVO fila = new FilaVisProspectoresVO();
        int idvis = rs.getInt("ECA_VIS_PROSPECTORES_COD");
        if (!rs.wasNull()) {
            fila.setIdvisita("" + idvis);
        }
        int idPros = rs.getInt("ECA_JUS_PROSPECTORES_COD");
        if (!rs.wasNull()) {
            fila.setCodProspector("" + idPros);
        }
        fila.setCodProspector("" + idPros);
        valorStr = rs.getString("ECA_VIS_CIF");
        fila.setCif(valorStr != null ? valorStr.toUpperCase() : "-");
        valorStr = rs.getString("ECA_VIS_EMPRESA");
        fila.setEmpresa(valorStr != null ? valorStr.toUpperCase() : "-");
        fila.setCodSector(rs.getInt("ECA_VIS_SECTOR"));
        if (rs.wasNull()) {
            fila.setCodSector(null);
        }
        valorStr = rs.getString("SECTOR");
        fila.setDescSector(valorStr != null ? valorStr.toUpperCase() : "-");
        fila.setCodCumpleLismi(rs.getInt("ECA_VIS_CUMPLELISMI"));
        if (rs.wasNull()) {
            fila.setCodCumpleLismi(null);
        }
        valorStr = rs.getString("CUMPLELISMI");
        fila.setCodResultadoFinal(rs.getInt("ECA_VIS_RESULTADOFINAL"));
        if (rs.wasNull()) {
            fila.setCodResultadoFinal(null);
        }
        fila.setDescCumpleLismi(valorStr != null ? valorStr.toUpperCase() : "-");
        valorStr = rs.getString("RESULTADO");
        fila.setDescResultadoFinal(valorStr != null ? valorStr.toUpperCase() : "-");
        valorStr = rs.getString("ECA_VIS_DIRECCION");
        fila.setDireccion(valorStr != null ? valorStr.toUpperCase() : "-");
        valorStr = rs.getString("ECA_VIS_CP");
        fila.setCpostal(valorStr != null ? valorStr.toUpperCase() : "-");
        valorStr = rs.getString("ECA_VIS_LOCALIDAD");
        fila.setLocalidad(valorStr != null ? valorStr.toUpperCase() : "-");
        fila.setCodProvincia(rs.getInt("ECA_VIS_PROVINCIA"));
        if (rs.wasNull()) {
            fila.setCodProvincia(null);
        }
        valorStr = rs.getString("PROVINCIA");
        fila.setDescProvincia(valorStr != null ? valorStr.toUpperCase() : "-");
        /*if (valorNum != null){           
            fila.setProvincia(""+valorNum);
        } else fila.setProvincia("");                     
         */
        valorStr = rs.getString("ECA_VIS_PERSCONTACTO");
        fila.setPersContacto(valorStr != null ? valorStr.toUpperCase() : "-");
        valorStr = rs.getString("ECA_VIS_CARGOPUESTO");
        fila.setPuesto(valorStr != null ? valorStr.toUpperCase() : "-");
        valorStr = rs.getString("ECA_VIS_EMAIL");
        fila.setMail(valorStr != null ? valorStr.toUpperCase() : "-");
        valorStr = rs.getString("ECA_VIS_TELEFONO");
        fila.setTelefono(valorStr != null ? valorStr.toUpperCase() : "-");
        valorStr = rs.getString("ECA_VIS_OBSERVACIONES");
        fila.setObservaciones(valorStr != null ? valorStr.toUpperCase() : "-");
        valorNum = rs.getInt("ECA_VIS_NUMTRAB");
        if (valorNum != null) {
            fila.setNumTrab("" + valorNum);
        } else {
            fila.setNumTrab("");
        }
        valorNum = rs.getInt("ECA_VIS_NUMTRABDISC");
        if (valorNum != null) {
            fila.setNumTrabDisc("" + valorNum);
        } else {
            fila.setNumTrabDisc("");
        }
        valorFecha = rs.getDate("ECA_VIS_FECVISITA");
        fila.setFecVisita(valorFecha != null ? format.format(valorFecha) : "-");
        valorStr = rs.getString("ECA_PROS_NIF");
        fila.setNifProspector(valorStr != null ? valorStr.toUpperCase() : "-");
        return fila;
    }

    private EcaVisProspectoresVO mapearEcaVisProspectoresVO(HSSFRow row) throws Exception {
        EcaVisProspectoresVO vis = null;
        String campo = "";
        String tipoRequerido = "";
        int longMax = -1;
        int pEntera = -1;
        int pDecimal = -1;
        if (row != null && !MeLanbide35Utils.esFilaVacia(19, row)) {
            try {
                SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide35.FORMATO_FECHA);
                HSSFCell cell = null;
                String valorString;
                vis = new EcaVisProspectoresVO();

                campo = "1";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.FECHA;
                longMax = -1;
                pEntera = -1;
                pDecimal = -1;
                int celdaIni = row.getFirstCellNum();
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, false)) {
                        vis.setFecVisita(format.parse(valorString));
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "2";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.STRING_NIF;
                longMax = 10;
                pEntera = -1;
                pDecimal = -1;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = (String) MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                valorString = reemplazarSaltosDeLinea(valorString, "");
                if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, false)) {
                    vis.setNifProspector(valorString);
                } else {
                    throw new DatoExcelNoValidoException();
                }

                //El campo 3 corresponde al nombre y apellidos del prospector, 
                //pero dado que se coge de la tabla prospectores, no se trata en la plantilla
                //Simplemente la saltamos
                celdaIni++;

                campo = "4";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.STRING;
                longMax = 100;
                pEntera = -1;
                pDecimal = -1;
                celdaIni++;
                cell = row.getCell(celdaIni);
                if (row.getRowNum() == 40) {
                    System.out.println();
                }
                valorString = (String) MeLanbide35Utils.getValorCelda(cell, tipoRequerido);

                valorString = reemplazarSaltosDeLinea(valorString, "");
                if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, false)) {
                    vis.setEmpresa(valorString.toUpperCase());
                } else {
                    throw new DatoExcelNoValidoException();
                }

                campo = "5";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.STRING;
                longMax = 10;
                pEntera = -1;
                pDecimal = -1;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = (String) MeLanbide35Utils.getValorCelda(cell, ConstantesMeLanbide35.TiposRequeridos.STRING_CIF);

                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        vis.setCif(valorString.toUpperCase());
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                /*campo = "6";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.NUMERICO;
                longMax = -1;
                pEntera = 2;
                pDecimal = -1;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                valorString = reemplazarSaltosDeLinea(valorString, "");
                String ADMINISTRACION_OFICINAS=ConstantesMeLanbide35.SECTOR_ACTIVIDAD.ADMINISTRACION_OFICINAS;
                String AGRARIO=ConstantesMeLanbide35.SECTOR_ACTIVIDAD.AGRARIO;
                String ARTESANIA=ConstantesMeLanbide35.SECTOR_ACTIVIDAD.ARTESANIA;
                String AUTOMOCION=ConstantesMeLanbide35.SECTOR_ACTIVIDAD.AUTOMOCION;
                String COMERCIO=ConstantesMeLanbide35.SECTOR_ACTIVIDAD.COMERCIO;
                String DOCENCIA_INVESTIGACION=ConstantesMeLanbide35.SECTOR_ACTIVIDAD.DOCENCIA_INVESTIGACION;
                String EDIFICACION_OBRAS_PUBLICAS=ConstantesMeLanbide35.SECTOR_ACTIVIDAD.EDIFICACION_OBRAS_PUBLICAS;
                String INDUSTRIA_PESADA_CONSTRUCCIONES_METALICAS=ConstantesMeLanbide35.SECTOR_ACTIVIDAD.INDUSTRIA_PESADA_CONSTRUCCIONES_METALICAS;
                String INDUSTRIAS_ALIMENTARIAS=ConstantesMeLanbide35.SECTOR_ACTIVIDAD.INDUSTRIAS_ALIMENTARIAS;
                String INDUSTRIAS_FABRICACION_EQUIPOS_ELECTROMECANICOS=ConstantesMeLanbide35.SECTOR_ACTIVIDAD.INDUSTRIAS_FABRICACION_EQUIPOS_ELECTROMECANICOS;
                String INDUSTRIA_MADERA_CORCHO=ConstantesMeLanbide35.SECTOR_ACTIVIDAD.INDUSTRIA_MADERA_CORCHO;
                String INDUSTRIAS_GRAFICAS=ConstantesMeLanbide35.SECTOR_ACTIVIDAD.INDUSTRIAS_GRAFICAS;
                String INDUSTRIAS_ANUFACTURERAS_DIVERSAS=ConstantesMeLanbide35.SECTOR_ACTIVIDAD.INDUSTRIAS_ANUFACTURERAS_DIVERSAS;
                String INDUSTRIAS_QUIMICAS=ConstantesMeLanbide35.SECTOR_ACTIVIDAD.INDUSTRIAS_QUIMICAS;
                String INDUSTRIAS_TEXTILES=ConstantesMeLanbide35.SECTOR_ACTIVIDAD.INDUSTRIAS_TEXTILES;
                String INFORMACION_MANIFESTACIONES_ARTISTICAS=ConstantesMeLanbide35.SECTOR_ACTIVIDAD.INFORMACION_MANIFESTACIONES_ARTISTICAS;
                String MANTENIMIENTO_REPARACION=ConstantesMeLanbide35.SECTOR_ACTIVIDAD.MANTENIMIENTO_REPARACION;
                String MINERIA_PRIMERAS_TRASFORMACIONES=ConstantesMeLanbide35.SECTOR_ACTIVIDAD.MINERIA_PRIMERAS_TRASFORMACIONES;
                String MONTAJE_INSTALACION=ConstantesMeLanbide35.SECTOR_ACTIVIDAD.MONTAJE_INSTALACION;
                String PESCA_ACUICULTURA=ConstantesMeLanbide35.SECTOR_ACTIVIDAD.PESCA_ACUICULTURA;
                String PIEL_CUERO=ConstantesMeLanbide35.SECTOR_ACTIVIDAD.PIEL_CUERO;
                String PRODUCCION_TRASFORMACION_ENERGIA_AGUA=ConstantesMeLanbide35.SECTOR_ACTIVIDAD.PRODUCCION_TRASFORMACION_ENERGIA_AGUA;
                String SANIDAD=ConstantesMeLanbide35.SECTOR_ACTIVIDAD.SANIDAD;
                String SEGUROS_FINANZAS=ConstantesMeLanbide35.SECTOR_ACTIVIDAD.SEGUROS_FINANZAS;
                String SERVICIOS_COMUNIDAD_PERSONALES=ConstantesMeLanbide35.SECTOR_ACTIVIDAD.SERVICIOS_COMUNIDAD_PERSONALES;
                String SERVICIOS_EMPRESAS=ConstantesMeLanbide35.SECTOR_ACTIVIDAD.SERVICIOS_EMPRESAS;
                String TRANSPORTES_COMUNICACIONES=ConstantesMeLanbide35.SECTOR_ACTIVIDAD.TRANSPORTES_COMUNICACIONES;
                String TURISMO_HOSTELERIA=ConstantesMeLanbide35.SECTOR_ACTIVIDAD.TURISMO_HOSTELERIA;

                if(valorString.equals(ADMINISTRACION_OFICINAS)){
                    valorString=ConstantesMeLanbide35.SECTOR_ACTIVIDAD_NUM.ADMINISTRACION_OFICINAS;
                }
                
                if(valorString.equals(AGRARIO)){
                    valorString=ConstantesMeLanbide35.SECTOR_ACTIVIDAD_NUM.AGRARIO;
                }
                
                if(valorString.equals(ARTESANIA)){
                    valorString=ConstantesMeLanbide35.SECTOR_ACTIVIDAD_NUM.ARTESANIA;
                }
                
                if(valorString.equals(AUTOMOCION)){
                    valorString=ConstantesMeLanbide35.SECTOR_ACTIVIDAD_NUM.AUTOMOCION;
                }
                
                if(valorString.equals(COMERCIO)){
                    valorString=ConstantesMeLanbide35.SECTOR_ACTIVIDAD_NUM.COMERCIO;
                }
                
                if(valorString.equals(DOCENCIA_INVESTIGACION)){
                    valorString=ConstantesMeLanbide35.SECTOR_ACTIVIDAD_NUM.DOCENCIA_INVESTIGACION;
                }
                
                if(valorString.equals(EDIFICACION_OBRAS_PUBLICAS)){
                    valorString=ConstantesMeLanbide35.SECTOR_ACTIVIDAD_NUM.EDIFICACION_OBRAS_PUBLICAS;
                }
                
                if(valorString.equals(INDUSTRIA_PESADA_CONSTRUCCIONES_METALICAS)){
                    valorString=ConstantesMeLanbide35.SECTOR_ACTIVIDAD_NUM.INDUSTRIA_PESADA_CONSTRUCCIONES_METALICAS;
                }
                
                if(valorString.equals(INDUSTRIAS_ALIMENTARIAS)){
                    valorString=ConstantesMeLanbide35.SECTOR_ACTIVIDAD_NUM.INDUSTRIAS_ALIMENTARIAS;
                }
                
                if(valorString.equals(INDUSTRIAS_FABRICACION_EQUIPOS_ELECTROMECANICOS)){
                    valorString=ConstantesMeLanbide35.SECTOR_ACTIVIDAD_NUM.INDUSTRIAS_FABRICACION_EQUIPOS_ELECTROMECANICOS;
                }
                
                if(valorString.equals(INDUSTRIA_MADERA_CORCHO)){
                    valorString=ConstantesMeLanbide35.SECTOR_ACTIVIDAD_NUM.INDUSTRIA_MADERA_CORCHO;
                }
                
                if(valorString.equals(INDUSTRIAS_GRAFICAS)){
                    valorString=ConstantesMeLanbide35.SECTOR_ACTIVIDAD_NUM.INDUSTRIAS_GRAFICAS;
                }
                
                if(valorString.equals(INDUSTRIAS_ANUFACTURERAS_DIVERSAS)){
                    valorString=ConstantesMeLanbide35.SECTOR_ACTIVIDAD_NUM.INDUSTRIAS_ANUFACTURERAS_DIVERSAS;
                }
                
                if(valorString.equals(INDUSTRIAS_QUIMICAS)){
                    valorString=ConstantesMeLanbide35.SECTOR_ACTIVIDAD_NUM.INDUSTRIAS_QUIMICAS;
                }
                
                if(valorString.equals(INDUSTRIAS_TEXTILES)){
                    valorString=ConstantesMeLanbide35.SECTOR_ACTIVIDAD_NUM.INDUSTRIAS_TEXTILES;
                }
                
                if(valorString.equals(INFORMACION_MANIFESTACIONES_ARTISTICAS)){
                    valorString=ConstantesMeLanbide35.SECTOR_ACTIVIDAD_NUM.INFORMACION_MANIFESTACIONES_ARTISTICAS;
                }
                
                if(valorString.equals(MANTENIMIENTO_REPARACION)){
                    valorString=ConstantesMeLanbide35.SECTOR_ACTIVIDAD_NUM.MANTENIMIENTO_REPARACION;
                }
                
                if(valorString.equals(MINERIA_PRIMERAS_TRASFORMACIONES)){
                    valorString=ConstantesMeLanbide35.SECTOR_ACTIVIDAD_NUM.MINERIA_PRIMERAS_TRASFORMACIONES;
                }
                
                if(valorString.equals(MONTAJE_INSTALACION)){
                    valorString=ConstantesMeLanbide35.SECTOR_ACTIVIDAD_NUM.MONTAJE_INSTALACION;
                }
                
                if(valorString.equals(PESCA_ACUICULTURA)){
                    valorString=ConstantesMeLanbide35.SECTOR_ACTIVIDAD_NUM.PESCA_ACUICULTURA;
                }
                
                if(valorString.equals(PIEL_CUERO)){
                    valorString=ConstantesMeLanbide35.SECTOR_ACTIVIDAD_NUM.PIEL_CUERO;
                }
                
                if(valorString.equals(PRODUCCION_TRASFORMACION_ENERGIA_AGUA)){
                    valorString=ConstantesMeLanbide35.SECTOR_ACTIVIDAD_NUM.PRODUCCION_TRASFORMACION_ENERGIA_AGUA;
                }
                
                if(valorString.equals(SANIDAD)){
                    valorString=ConstantesMeLanbide35.SECTOR_ACTIVIDAD_NUM.SANIDAD;
                }
                
                if(valorString.equals(SEGUROS_FINANZAS)){
                    valorString=ConstantesMeLanbide35.SECTOR_ACTIVIDAD_NUM.SEGUROS_FINANZAS;
                }
                
                if(valorString.equals(SERVICIOS_COMUNIDAD_PERSONALES)){
                    valorString=ConstantesMeLanbide35.SECTOR_ACTIVIDAD_NUM.SERVICIOS_COMUNIDAD_PERSONALES;
                }
                
                if(valorString.equals(SERVICIOS_EMPRESAS)){
                    valorString=ConstantesMeLanbide35.SECTOR_ACTIVIDAD_NUM.SERVICIOS_EMPRESAS;
                }
                
                if(valorString.equals(TRANSPORTES_COMUNICACIONES)){
                    valorString=ConstantesMeLanbide35.SECTOR_ACTIVIDAD_NUM.TRANSPORTES_COMUNICACIONES;
                }
                
                if(valorString.equals(TURISMO_HOSTELERIA)){
                    valorString=ConstantesMeLanbide35.SECTOR_ACTIVIDAD_NUM.TURISMO_HOSTELERIA;
                }

                
                if(MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, false))
                {       
                    //valorString = valorString.replaceAll("\\.", ",");
                    //valorString = valorString.substring(0, valorString.indexOf(","));                  
                    vis.setSector(new BigDecimal(valorString).intValue());
                }
                else
                {
                    throw new DatoExcelNoValidoException();
                }
                 */
                campo = "6";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.NUMERICO;
                longMax = -1;
                pEntera = 2;
                pDecimal = -1;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                valorString = reemplazarSaltosDeLinea(valorString, "");
                if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, false)) {
                    //valorString = valorString.replaceAll("\\.", ",");
                    //valorString = valorString.substring(0, valorString.indexOf(","));                  
                    vis.setSector(new BigDecimal(valorString).intValue());
                } else {
                    throw new DatoExcelNoValidoException();
                }

                campo = "7";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.STRING;
                longMax = 100;
                pEntera = -1;
                pDecimal = -1;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        vis.setDireccion(valorString);
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "8";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.NUMERICO;
                longMax = 5;
                pEntera = 5;
                pDecimal = -1;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");

                    if (valorString.contains(".") || valorString.contains(",")) {
                        valorString = valorString.replaceAll("\\.", ",");
                        String[] partes = valorString.split(",");
                        if (partes.length == 2 && Integer.parseInt(partes[1]) == 0) {
                            valorString = valorString.substring(0, valorString.indexOf(","));
                        }
                    }
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        vis.setCpostal(valorString);
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "9";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.STRING;
                longMax = 100;
                pEntera = -1;
                pDecimal = -1;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        vis.setLocalidad(valorString);
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                /*campo = "10";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.NUMERICO;
                longMax = -1;
                pEntera = 2;
                pDecimal = -1;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = (String)MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                String alaba=ConstantesMeLanbide35.PROVINCIA.ARABA;
                String bizkaia=ConstantesMeLanbide35.PROVINCIA.BIZKAIA;
                String gipuzkoa=ConstantesMeLanbide35.PROVINCIA.GIPUZKOA;
                
                if(valorString != null)
                {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    
                    if(valorString.equals(alaba)){
                        valorString=ConstantesMeLanbide35.PROVINCIA_NUM.ARABA;
                    }
                    
                    if(valorString.equals(bizkaia)){
                        valorString=ConstantesMeLanbide35.PROVINCIA_NUM.BIZKAIA;
                    }
                    
                    if(valorString.equals(gipuzkoa)){
                        valorString=ConstantesMeLanbide35.PROVINCIA_NUM.GIPUZKOA;
                    }
                    
                    if(MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true))
                    {              
                        //valorString = valorString.replaceAll("\\.", ",");
                        //valorString = valorString.substring(0, valorString.indexOf(","));          
                        vis.setProvincia(new BigDecimal(valorString).intValue());
                    }
                    else
                    {
                        throw new DatoExcelNoValidoException();
                    }
                }*/
                campo = "10";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.NUMERICO;
                longMax = -1;
                pEntera = 2;
                pDecimal = -1;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = (String) MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        //valorString = valorString.replaceAll("\\.", ",");
                        //valorString = valorString.substring(0, valorString.indexOf(","));          
                        vis.setProvincia(new BigDecimal(valorString).intValue());
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "11";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.STRING;
                longMax = 100;
                pEntera = -1;
                pDecimal = -1;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = (String) MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        vis.setPersContacto(valorString);
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "12";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.STRING;
                longMax = 100;
                pEntera = -1;
                pDecimal = -1;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = (String) MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        vis.setPuesto(valorString);
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "13";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.STRING;
                longMax = 100;
                pEntera = -1;
                pDecimal = -1;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = (String) MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        vis.setMail(valorString);
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "14";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.NUMERICO;
                longMax = -1;
                pEntera = 9;
                pDecimal = -1;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = (String) MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        //valorString = valorString.replaceAll("\\.", ",");
                        //valorString = valorString.substring(0, valorString.indexOf(","));
                        vis.setTelefono(new BigDecimal(valorString).intValue());
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "15";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.NUMERICO;
                longMax = -1;
                pEntera = 6;
                pDecimal = -1;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        //valorString = valorString.replaceAll("\\.", ",");
                        //valorString = valorString.substring(0, valorString.indexOf(","));
                        vis.setNumTrab(new BigDecimal(valorString).intValue());
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                campo = "16";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.NUMERICO;
                longMax = -1;
                pEntera = 6;
                pDecimal = -1;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        //valorString = valorString.replaceAll("\\.", ",");
                        //valorString = valorString.substring(0, valorString.indexOf(","));
                        vis.setNumTrabDisc(new BigDecimal(valorString).intValue());
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                /*campo = "17";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.NUMERICO;
                longMax = -1;
                pEntera = 1;
                pDecimal = -1;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                String si=ConstantesMeLanbide35.LISMI.SI;
                String no=ConstantesMeLanbide35.LISMI.NO;
                String certificado=ConstantesMeLanbide35.LISMI.CERTIFICADO;
                String no_procede=ConstantesMeLanbide35.LISMI.NO_PROCEDE;
                if(valorString != null)
                {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if(valorString.equals(si)){
                        valorString=ConstantesMeLanbide35.LISMI_NUM.SI;
                    }
                    if(valorString.equals(no)){
                        valorString=ConstantesMeLanbide35.LISMI_NUM.NO;
                    }
                    if(valorString.equals(certificado)){
                        valorString=ConstantesMeLanbide35.LISMI_NUM.CERTIFICADO;
                    }
                    if(valorString.equals(no_procede)){
                        valorString=ConstantesMeLanbide35.LISMI_NUM.NO_PROCEDE;
                    }
                    
                    if(MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true))
                    {   
                        //valorString = valorString.replaceAll("\\.", ",");
                        //valorString = valorString.substring(0, valorString.indexOf(","));
                        vis.setCumpleLismi(new BigDecimal(valorString).intValue());
                    }
                    else
                    {
                        throw new DatoExcelNoValidoException();
                    }
                }*/
                campo = "17";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.NUMERICO;
                longMax = -1;
                pEntera = 1;
                pDecimal = -1;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "");
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        //valorString = valorString.replaceAll("\\.", ",");
                        //valorString = valorString.substring(0, valorString.indexOf(","));
                        vis.setCumpleLismi(new BigDecimal(valorString).intValue());
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

                /*campo = "18";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.NUMERICO;
                longMax = -1;
                pEntera = 1;
                pDecimal = -1;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                valorString = reemplazarSaltosDeLinea(valorString, "");
                String informada=ConstantesMeLanbide35.RESULTADO.INFORMADA;
                String interesada=ConstantesMeLanbide35.RESULTADO.INTERESADA;
                String vinculada=ConstantesMeLanbide35.RESULTADO.VINCULADA;
                
                if(valorString.equals(informada)){
                    valorString=ConstantesMeLanbide35.RESULTADO_NUM.INFORMADA;
                }
                
                if(valorString.equals(interesada)){
                    valorString=ConstantesMeLanbide35.RESULTADO_NUM.INTERESADA;
                }
                
                if(valorString.equals(vinculada)){
                    valorString=ConstantesMeLanbide35.RESULTADO_NUM.VINCULADA;
                }
                
                if(MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, false))
                {
                    //valorString = valorString.replaceAll("\\.", ",");
                    //valorString = valorString.substring(0, valorString.indexOf(","));
                    vis.setResultadoFinal(new BigDecimal(valorString).intValue());
                }
                else
                {
                    throw new DatoExcelNoValidoException();
                }*/
                campo = "18";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.NUMERICO;
                longMax = -1;
                pEntera = 1;
                pDecimal = -1;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                valorString = reemplazarSaltosDeLinea(valorString, "");
                if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, false)) {
                    //valorString = valorString.replaceAll("\\.", ",");
                    //valorString = valorString.substring(0, valorString.indexOf(","));
                    vis.setResultadoFinal(new BigDecimal(valorString).intValue());
                } else {
                    throw new DatoExcelNoValidoException();
                }

                campo = "19";
                tipoRequerido = ConstantesMeLanbide35.TiposRequeridos.STRING;
                longMax = 200;
                pEntera = -1;
                pDecimal = -1;
                celdaIni++;
                cell = row.getCell(celdaIni);
                valorString = (String) MeLanbide35Utils.getValorCelda(cell, tipoRequerido);
                if (valorString != null) {
                    valorString = reemplazarSaltosDeLinea(valorString, "<br/>");
                    if (MeLanbide35Utils.validarDatoExcel(valorString, tipoRequerido, longMax, pEntera, pDecimal, true)) {
                        vis.setObservaciones(valorString);
                    } else {
                        throw new DatoExcelNoValidoException();
                    }
                }

            } catch (Exception ex) {
                ExcelRowMappingException erme = new ExcelRowMappingException(campo);
                erme.setLongMax(longMax);
                erme.setTipo(tipoRequerido);
                erme.setpEntera(pEntera);
                erme.setpDecimal(pDecimal);
                throw erme;
            }
        }
        return vis;
    }

    private EcaVisProspectoresVO mapearEcaVisProspectoresVO(ResultSet rs) throws Exception {
        EcaVisProspectoresVO vis = new EcaVisProspectoresVO();
        vis.setJusProspectoresCod(rs.getInt("ECA_JUS_PROSPECTORES_COD"));
        if (rs.wasNull()) {
            vis.setJusProspectoresCod(null);
        }
        vis.setVisProspectoresCod(rs.getInt("ECA_VIS_PROSPECTORES_COD"));
        if (rs.wasNull()) {
            vis.setVisProspectoresCod(null);
        }
        vis.setCif(rs.getString("ECA_VIS_CIF"));
        vis.setEmpresa(rs.getString("ECA_VIS_EMPRESA"));
        vis.setFecVisita(rs.getDate("ECA_VIS_FECVISITA"));
        vis.setSector(rs.getInt("ECA_VIS_SECTOR"));
        if (rs.wasNull()) {
            vis.setSector(null);
        }
        vis.setProvincia(rs.getInt("ECA_VIS_PROVINCIA"));
        if (rs.wasNull()) {
            vis.setProvincia(null);
        }
        vis.setCumpleLismi(rs.getInt("ECA_VIS_CUMPLELISMI"));
        if (rs.wasNull()) {
            vis.setCumpleLismi(null);
        }
        vis.setResultadoFinal(rs.getInt("ECA_VIS_RESULTADOFINAL"));
        if (rs.wasNull()) {
            vis.setResultadoFinal(null);
        }
        vis.setDireccion(rs.getString("ECA_VIS_DIRECCION"));
        vis.setCpostal(rs.getString("ECA_VIS_CP"));
        vis.setLocalidad(rs.getString("ECA_VIS_LOCALIDAD"));
        vis.setPersContacto(rs.getString("ECA_VIS_PERSCONTACTO"));
        vis.setPuesto(rs.getString("ECA_VIS_CARGOPUESTO"));
        vis.setMail(rs.getString("ECA_VIS_EMAIL"));
        vis.setTelefono(rs.getInt("ECA_VIS_TELEFONO"));
        if (rs.wasNull()) {
            vis.setTelefono(null);
        }
        vis.setNumTrab(rs.getInt("ECA_VIS_NUMTRAB"));
        if (rs.wasNull()) {
            vis.setNumTrab(null);
        }
        vis.setNumTrabDisc(rs.getInt("ECA_VIS_NUMTRABDISC"));
        if (rs.wasNull()) {
            vis.setNumTrabDisc(null);
        }
        vis.setObservaciones(rs.getString("ECA_VIS_OBSERVACIONES"));

        vis.setFecVisita(rs.getDate("ECA_VIS_FECVISITA"));
        vis.setNifProspector(rs.getString("NIFPROSPECTOR"));
        return vis;
    }

    private EcaResPreparadoresVO mapearEcaResPreparadoresVO(ResultSet rs) throws Exception {
        EcaResPreparadoresVO prep = new EcaResPreparadoresVO();
        prep.setEcaJusPreparadoresCod(rs.getInt("ECA_JUS_PREPARADORES_COD"));
        if (rs.wasNull()) {
            prep.setEcaJusPreparadoresCod(null);
        }
        prep.setEcaNumexp(rs.getString("ECA_NUMEXP"));
        prep.setEcaResPreparadoresCod(rs.getLong("ECA_RES_PREPARADORES_COD"));
        if (rs.wasNull()) {
            prep.setEcaResPreparadoresCod(null);
        }
        prep.setFecSysdate(rs.getDate("FEC_SYSDATE"));
        prep.setGastosSalarialesConcedidos(rs.getBigDecimal("GASTOS_SALARIALES_CONCEDIDOS"));
        prep.setGastosSalarialesJustificados(rs.getBigDecimal("GASTOS_SALARIALES_JUSTIFICADOS"));
        prep.setGastosSalarialesSolicitados(rs.getBigDecimal("GASTOS_SALARIALES_SOLICITADOS"));
        prep.setImporteInsConcedido(rs.getBigDecimal("IMPORTE_INS_CONCEDIDO"));
        prep.setImporteInsJustificadas(rs.getBigDecimal("IMPORTE_INS_JUSTIFICADAS"));
        prep.setImporteInserciones(rs.getBigDecimal("IMPORTE_INSERCIONES"));
        prep.setImporteSegInserciones(rs.getBigDecimal("IMPORTE_SEG_INSERCIONES"));
        prep.setImporteSeguimientos(rs.getBigDecimal("IMPORTE_SEGUIMIENTOS"));
        prep.setNif(rs.getString("NIF"));
        prep.setNombre(rs.getString("NOMBRE"));
        prep.setaPagar(rs.getBigDecimal("A_PAGAR"));
        return prep;
    }

    private EcaResProspectoresVO mapearEcaResProspectoresVO(ResultSet rs) throws Exception {
        EcaResProspectoresVO pros = new EcaResProspectoresVO();
        pros.setEcaNumExp(rs.getString("ECA_NUMEXP"));
        pros.setEcaResProspectoresCod(rs.getLong("ECA_RES_PROSPECTORES_COD"));
        if (rs.wasNull()) {
            pros.setEcaResProspectoresCod(null);
        }
        pros.setFecSysdate(rs.getDate("FEC_SYSDATE"));
        pros.setGastosSalarialesConcedidos(rs.getBigDecimal("GASTOS_SALARIALES_CONCEDIDOS"));
        pros.setGastosSalarialesJustificados(rs.getBigDecimal("GASTOS_SALARIALES_JUSTIFICADOS"));
        pros.setGastosSalarialesSolicitados(rs.getBigDecimal("GASTOS_SALARIALES_SOLICITADOS"));
        pros.setImporteVisitas(rs.getBigDecimal("IMPORTE_VISITAS"));
        pros.setNif(rs.getString("NIF"));
        pros.setNombre(rs.getString("NOMBRE"));
        pros.setVisitasConcedidas(rs.getInt("VISITAS_CONCEDIDAS"));
        if (rs.wasNull()) {
            pros.setVisitasConcedidas(null);
        }
        pros.setVisitasJustificadas(rs.getInt("VISITAS_JUSTIFICADAS"));
        if (rs.wasNull()) {
            pros.setVisitasJustificadas(null);
        }
        pros.setaPagar(rs.getBigDecimal("A_PAGAR"));
        return pros;
    }

    private FilaEcaResPreparadoresVO mapearFilaEcaResPreparadoresVO(ResultSet rs) throws Exception {
        DecimalFormatSymbols simbolo = new DecimalFormatSymbols();
        simbolo.setDecimalSeparator(',');
        simbolo.setGroupingSeparator('.');
        DecimalFormat formateador = new DecimalFormat("#,##0.00", simbolo);

        FilaEcaResPreparadoresVO fila = new FilaEcaResPreparadoresVO();

        BigDecimal valorBD = null;
        String valorStr = null;
        /*String concedido = "";
        Double concedidoD=0.00;
        String justificado = "";
        Double justificadoD=0.00;
        String inserciones = "";
        Double insercionesD=0.00;
        Double pagoArealizarD=0.00;*/

        fila.setEcaJusPreparadoresCod(rs.getInt("ECA_JUS_PREPARADORES_COD"));
        if (rs.wasNull()) {
            fila.setEcaJusPreparadoresCod(null);
        }
        fila.setNumExp(rs.getString("ECA_NUMEXP"));

        fila.setEcaResPreparadoresCod(rs.getLong("ECA_RES_PREPARADORES_COD"));
        if (rs.wasNull()) {
            fila.setEcaResPreparadoresCod(null);
        }

        valorBD = rs.getBigDecimal("GASTOS_SALARIALES_CONCEDIDOS");
        /*concedido=formateador.format(new BigDecimal( MeLanbide35Utils.redondearDecimalesString(valorBD, 2)));
        concedido=concedido.replace(".", "");
        concedido=concedido.replace(",", ".");
        concedidoD=Double.parseDouble(concedido);*/
        fila.setGastosSalarialesConcedidos(valorBD != null ? formateador.format(new BigDecimal(MeLanbide35Utils.redondearDecimalesString(valorBD, 2))) : "-");

        valorBD = rs.getBigDecimal("GASTOS_SALARIALES_JUSTIFICADOS");
        /*justificado=formateador.format(new BigDecimal( MeLanbide35Utils.redondearDecimalesString(valorBD, 2)));
        justificado=justificado.replace(".", "");
        justificado=justificado.replace(",", ".");
        justificadoD=Double.parseDouble(justificado);*/
        fila.setGastosSalarialesJustificados(valorBD != null ? formateador.format(new BigDecimal(MeLanbide35Utils.redondearDecimalesString(valorBD, 2))) : "-");

        valorBD = rs.getBigDecimal("GASTOS_SALARIALES_SOLICITADOS");
        fila.setGastosSalarialesSolicitados(valorBD != null ? formateador.format(new BigDecimal(MeLanbide35Utils.redondearDecimalesString(valorBD, 2))) : "-");

        valorBD = rs.getBigDecimal("IMPORTE_INS_CONCEDIDO");
        fila.setImporteInsConcedido(valorBD != null ? formateador.format(new BigDecimal(MeLanbide35Utils.redondearDecimalesString(valorBD, 2))) : "-");

        valorBD = rs.getBigDecimal("IMPORTE_INS_JUSTIFICADAS");
        fila.setImporteInsJustificadas(valorBD != null ? formateador.format(new BigDecimal(MeLanbide35Utils.redondearDecimalesString(valorBD, 2))) : "-");

        valorBD = rs.getBigDecimal("IMPORTE_INSERCIONES");
        /*inserciones=formateador.format(new BigDecimal( MeLanbide35Utils.redondearDecimalesString(valorBD, 2)));
        inserciones=inserciones.replace(".", "");
        inserciones=inserciones.replace(",", ".");
        insercionesD=Double.parseDouble(inserciones);*/
        fila.setImporteInserciones(valorBD != null ? formateador.format(new BigDecimal(MeLanbide35Utils.redondearDecimalesString(valorBD, 2))) : "-");

        valorBD = rs.getBigDecimal("IMPORTE_SEG_INSERCIONES");
        fila.setImporteSegInserciones(valorBD != null ? formateador.format(new BigDecimal(MeLanbide35Utils.redondearDecimalesString(valorBD, 2))) : "-");

        valorBD = rs.getBigDecimal("IMPORTE_SEGUIMIENTOS");
        fila.setImporteSeguimientos(valorBD != null ? formateador.format(new BigDecimal(MeLanbide35Utils.redondearDecimalesString(valorBD, 2))) : "-");

        valorStr = rs.getString("NIF");
        fila.setNif(valorStr != null ? valorStr.toUpperCase() : "-");

        valorStr = rs.getString("NOMBRE");
        fila.setNombre(valorStr != null ? valorStr.toUpperCase() : "-");

        valorBD = rs.getBigDecimal("A_PAGAR");
        /*if(concedidoD>justificadoD){
            pagoArealizarD=(justificadoD*25/100)+insercionesD;
        }else{
            pagoArealizarD=(concedidoD*25/100)+insercionesD;
        }*/
        fila.setImpPagar(valorBD != null ? formateador.format(new BigDecimal(MeLanbide35Utils.redondearDecimalesString(valorBD, 2))) : "-");
        //fila.setImpPagar(pagoArealizarD != null ? String.valueOf(pagoArealizarD) : "-");

        fila.setTipoSust(rs.getString("ECA_PREP_TIPO_SUST"));

        valorBD = rs.getBigDecimal("IMPORTE_SEGUIMIENTOS_LIM");
        fila.setImporteSeguimientosLim(valorBD != null ? formateador.format(new BigDecimal(MeLanbide35Utils.redondearDecimalesString(valorBD, 2))) : "-");
        return fila;
    }

    private FilaEcaResProspectoresVO mapearFilaEcaResProspectoresVO(ResultSet rs) throws Exception {
        DecimalFormatSymbols simbolo = new DecimalFormatSymbols();
        simbolo.setDecimalSeparator(',');
        simbolo.setGroupingSeparator('.');
        DecimalFormat formateador = new DecimalFormat("#,##0.00", simbolo);

        FilaEcaResProspectoresVO fila = new FilaEcaResProspectoresVO();

        BigDecimal valorBD = null;
        String valorStr = null;
        Integer valorInt = null;

        fila.setNumExp(rs.getString("ECA_NUMEXP"));
        fila.setEcaResProspectoresCod(rs.getLong("ECA_RES_PROSPECTORES_COD"));
        if (rs.wasNull()) {
            fila.setEcaResProspectoresCod(null);
        }

        fila.setEcaJusProspectoresCod(rs.getLong("ECA_JUS_PROSPECTORES_COD"));
        if (rs.wasNull()) {
            fila.setEcaJusProspectoresCod(null);
        }

        valorBD = rs.getBigDecimal("GASTOS_SALARIALES_CONCEDIDOS");
        fila.setGastosSalarialesConcedidos(valorBD != null ? formateador.format(new BigDecimal(MeLanbide35Utils.redondearDecimalesString(valorBD, 2))) : "-");

        valorBD = rs.getBigDecimal("GASTOS_SALARIALES_JUSTIFICADOS");
        fila.setGastosSalarialesJustificados(valorBD != null ? formateador.format(new BigDecimal(MeLanbide35Utils.redondearDecimalesString(valorBD, 2))) : "-");

        valorBD = rs.getBigDecimal("GASTOS_SALARIALES_SOLICITADOS");
        fila.setGastosSalarialesSolicitados(valorBD != null ? formateador.format(new BigDecimal(MeLanbide35Utils.redondearDecimalesString(valorBD, 2))) : "-");

        valorBD = rs.getBigDecimal("IMPORTE_VISITAS");
        fila.setImporteVisitas(valorBD != null ? formateador.format(new BigDecimal(MeLanbide35Utils.redondearDecimalesString(valorBD, 2))) : "-");

        valorStr = rs.getString("NIF");
        fila.setNif(valorStr != null ? valorStr.toUpperCase() : "-");

        valorStr = rs.getString("NOMBRE");
        fila.setNombre(valorStr != null ? valorStr.toUpperCase() : "-");

        valorInt = rs.getInt("VISITAS_CONCEDIDAS");
        if (rs.wasNull()) {
            valorInt = null;
        }
        fila.setVisitasConcedidas(valorInt != null ? valorInt.toString() : "-");

        valorInt = rs.getInt("VISITAS_JUSTIFICADAS");
        if (rs.wasNull()) {
            valorInt = null;
        }
        fila.setVisitasJustificadas(valorInt != null ? valorInt.toString() : "-");

        valorBD = rs.getBigDecimal("A_PAGAR");
        fila.setImpPagar(valorBD != null ? formateador.format(new BigDecimal(MeLanbide35Utils.redondearDecimalesString(valorBD, 2))) : "-");

        fila.setTipoSust(rs.getString("ECA_PROS_TIPO_SUST"));

        valorBD = rs.getBigDecimal("IMPORTE_VISITAS_CONC");
        fila.setImporteVisitasConc(valorBD != null ? formateador.format(new BigDecimal(MeLanbide35Utils.redondearDecimalesString(valorBD, 2))) : "-");
        return fila;
    }

    private EcaResumenVO mapearEcaResumenVO(ResultSet rs) throws Exception {
        EcaResumenVO res = new EcaResumenVO();
        res.setEcaCodResumen(rs.getInt("ECA_COD_RESUMEN"));
        if (rs.wasNull()) {
            res.setEcaCodResumen(null);
        }
        res.setSolicitud(rs.getInt("ECA_RES_SOLICITUD"));
        if (rs.wasNull()) {
            res.setSolicitud(null);
        }
        res.setEcaResSubPriv(rs.getBigDecimal("ECA_RES_SUB_PRIV"));
        res.setEcaResSubPub(rs.getBigDecimal("ECA_RES_SUB_PUB"));
        res.setEcaResTotSubv(rs.getBigDecimal("ECA_RES_TOT_SUBV"));
        return res;
    }

    private String reemplazarSaltosDeLinea(String text, String replacement) {
        if (text != null && !text.equals("")) {
            text = text.replaceAll("\r\n", replacement).replaceAll("\n", replacement).replaceAll("\r", replacement);
            try {
                text = text.replaceAll(System.getProperty("line.separator"), replacement);
            } catch (Exception ex) {

            }
        }
        return text;
    }

}
