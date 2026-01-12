/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide69.util;

import es.altia.flexia.integracion.moduloexterno.melanbide69.vo.DatosEconomicosExpVO;
import es.altia.flexia.integracion.moduloexterno.melanbide69.vo.DatosSuplementariosExpedienteVO;
import es.altia.flexia.integracion.moduloexterno.melanbide69.vo.ExpedienteVO;
import es.altia.flexia.integracion.moduloexterno.melanbide69.vo.InformeApesVO;
import es.altia.flexia.integracion.moduloexterno.melanbide69.vo.InformeApes_No_AceptadasVO;
import es.altia.flexia.integracion.moduloexterno.melanbide69.vo.SocioVO;
import es.altia.flexia.integracion.moduloexterno.melanbide69.vo.TerceroVO;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author davidg
 */
public class MeLanbide69MappingUtils {

    private static MeLanbide69MappingUtils instance = null;

    private MeLanbide69MappingUtils() {
    }

    public static MeLanbide69MappingUtils getInstance() {
        if (instance == null) {
            synchronized (MeLanbide69MappingUtils.class) {
                instance = new MeLanbide69MappingUtils();
            }
        }
        return instance;
    }

    /*Mapeo para el detalle de errores*/
    public Object map(ResultSet rs, Class clazz) throws Exception {
        if (clazz == InformeApesVO.class) {
            return mapearFacturaVO(rs);
        }
        if (clazz == InformeApes_No_AceptadasVO.class) {
            return mapearInformeApes_No_AceptadasVO(rs);
        }
        if (clazz == ExpedienteVO.class) {
            return mapearExpedienteVO(rs);
        }
        if (clazz == DatosEconomicosExpVO.class) {
            return mapearDatosEconomicosVO(rs);
        }
        if (clazz == DatosSuplementariosExpedienteVO.class) {
            return mapearImportesExpedienteVO(rs);
        }
        if (clazz == SocioVO.class) {
            return mapearSocioVO(rs);
        }
        return null;
    }

    private Object mapearFacturaVO(ResultSet rs) throws SQLException {
        BigDecimal impPagadoNum2 = null;
        InformeApesVO numero = new InformeApesVO();
        BigDecimal impteRenunciadoResolucion = BigDecimal.ZERO;
        /*
       IDENTIFICACION,ENTREGADA_FACT,ENTREGADO_JUSTIF,CODIGO_CONCEPTO,IMPORTE
         */
        String impNuevo = String.valueOf(rs.getBigDecimal("IMPORTE"));
        double importeDouble = Double.parseDouble(impNuevo);
        //impPagadoNum2 = impNuevo != null && !impNuevo.equals("") ? new BigDecimal(impNuevo.replaceAll(",", "\\.")) : null;

        numero.setIDENTIFICACION(rs.getString("IDENTIFICACION"));
        numero.setENTREGADA_FACT(rs.getString("ENTREGADA_FACT"));
        numero.setENTREGADO_JUSTIF(rs.getString("ENTREGADO_JUSTIF"));
        numero.setCODIGO_SUBCONCEPTO(rs.getInt("CODIGO_SUBCONCEPTO"));
        //BigDecimal num=new BigDecimal(numero.setIMPORTE(rs.getBigDecimal("IMPORTE")));

        //impteRenunciadoResolucion=new BigDecimal(MeLanbide69Util.redondearDecimalesString(IMPORTEnUEVO, 2));
        numero.setIMPORTE(importeDouble);

        return numero;
    }

    private Object mapearInformeApes_No_AceptadasVO(ResultSet rs) throws SQLException {
        InformeApes_No_AceptadasVO numero = new InformeApes_No_AceptadasVO();
        /*
       IDENTIFICACION,ENTREGADA_FACT,ENTREGADO_JUSTIF,CODIGO_CONCEPTO,IMPORTE
         */
        numero.setIDENTIFICACION(rs.getString("IDENTIFICACION"));
        numero.setENTREGADA_FACT(rs.getString("ENTREGADA_FACT"));
        numero.setENTREGADO_JUSTIF(rs.getString("ENTREGADO_JUSTIF"));
        numero.setCODIGO_SUBCONCEPTO(rs.getInt("CODIGO_SUBCONCEPTO"));
        numero.setIMPORTE(rs.getDouble("IMPORTE"));
        numero.setOBSERVACIONES(rs.getString("OBSERVACIONES"));

        return numero;
    }

    private Object mapearExpedienteVO(ResultSet rs) throws SQLException {
        ExpedienteVO expediente = new ExpedienteVO();
        TerceroVO tercero = new TerceroVO();
        tercero.setTSexoTercero(rs.getString("sexo"));
        tercero.setTFecNacimiento(rs.getDate("fecnacimiento"));
        expediente.setTercero(tercero);
        expediente.setFecPresentacion(rs.getDate("fecPresentacion"));
        expediente.setCifCBSC(rs.getString("cifCBSC"));
        if (rs.getString("esCBSC") != null && rs.getString("esCBSC").equalsIgnoreCase("S")) {
            expediente.setEsCBSC(true);
        } else {
            expediente.setEsCBSC(false);
        }
        return expediente;
    }

    private Object mapearDatosEconomicosVO(ResultSet rs) throws SQLException {
        DatosEconomicosExpVO datosEcon = new DatosEconomicosExpVO();
        datosEcon.setImporteSubvencion(rs.getInt("SUB_IMPORTE"));
//        datosEcon.setPorcentajePrimerPago(rs.getDouble("PLA_PORCENTAJE"));

        return datosEcon;
    }

    private Object mapearImportesExpedienteVO(ResultSet rs) throws SQLException {
        DatosSuplementariosExpedienteVO importesVO = new DatosSuplementariosExpedienteVO();
        Object importeOBJ = rs.getObject("IMPPRIMERPAGOG");
        if (importeOBJ != null) {
            importesVO.setImportePrimerPago(((BigDecimal) importeOBJ).doubleValue());//Object porque si es null con getDouble lo convierte a 0.0
        }
        importeOBJ = rs.getObject("IMPPRIMERPAGOCBSC");
        if (importeOBJ != null) {
            importesVO.setImportePrimerPagoCBSC(((BigDecimal) importeOBJ).doubleValue());//Object porque si es null con getDouble lo convierte a 0.0
        }
        importeOBJ = rs.getObject("IMPSUBVENCIONG");
        if (importeOBJ != null) {
            importesVO.setImporteSubvencion(((BigDecimal) importeOBJ).doubleValue());//Object porque si es null con getDouble lo convierte a 0.0
        }
        importeOBJ = rs.getObject("IMPSUBVENCBSC");
        if (importeOBJ != null) {
            importesVO.setImporteSubvencionCBSC(((BigDecimal) importeOBJ).doubleValue());//Object porque si es null con getDouble lo convierte a 0.0
        }
        importeOBJ = rs.getObject("IMPSEGUNDOPAGO2");
        if (importeOBJ != null) {
            importesVO.setImporteSegundoPago(((BigDecimal) importeOBJ).doubleValue());//Object porque si es null con getDouble lo convierte a 0.0
        }
        importeOBJ = rs.getObject("CANTREINTEGRAR");
        if (importeOBJ != null) {
            importesVO.setImporteReintegro(((BigDecimal) importeOBJ).doubleValue());//Object porque si es null con getDouble lo convierte a 0.0
        }
        importesVO.setFecPrimerPago(rs.getDate("FECPRIMERPAGO"));

        importeOBJ = rs.getObject("RESOLUCION");
        if (importeOBJ != null) {
            importesVO.setResolucion(rs.getString("RESOLUCION"));//Object porque si es null con getDouble lo convierte a 0.0
        }

        return importesVO;
    }

    private Object mapearSocioVO(ResultSet rs) throws SQLException {
        SocioVO socio = new SocioVO();
        socio.setId(rs.getInt("ID"));
        socio.setNumExp(rs.getString("NUM_EXP"));
        socio.setNombre(rs.getString("NOMBRE"));
        socio.setApellido1(rs.getString("APELLIDO1"));
        socio.setApellido2(rs.getString("APELLIDO2") != null && !rs.getString("APELLIDO2").isEmpty() ? rs.getString("APELLIDO2") : "");
        socio.setDni(rs.getString("DNINIE"));
        return socio;
    }

}
