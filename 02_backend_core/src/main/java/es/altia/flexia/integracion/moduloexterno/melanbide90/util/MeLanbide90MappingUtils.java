package es.altia.flexia.integracion.moduloexterno.melanbide90.util;

import es.altia.flexia.integracion.moduloexterno.melanbide90.vo.DesplegableAdmonLocalVO;
import es.altia.flexia.integracion.moduloexterno.melanbide90.vo.FacturaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide90.vo.FamiliaSolicitadaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide90.vo.FilaMinimisVO;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class MeLanbide90MappingUtils {

    private static MeLanbide90MappingUtils instance = null;
    private final SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");

    private MeLanbide90MappingUtils() {
    }

    public static MeLanbide90MappingUtils getInstance() {
        if (instance == null) {
            synchronized (MeLanbide90MappingUtils.class) {
                instance = new MeLanbide90MappingUtils();
            }
        }
        return instance;
    }

    public Object map(ResultSet rs, Class clazz) throws Exception {
        if (clazz == FilaMinimisVO.class) {
            return mapearMinimisVO(rs);
        } else if (clazz == DesplegableAdmonLocalVO.class) {
            return mapearDesplegable(rs);
        } else if (clazz == FacturaVO.class) {
            return mapearFacturaVO(rs);
        } else if (clazz == FamiliaSolicitadaVO.class) {
            return mapearFamiliaSolicitadaVO(rs);
        }
        return null;
    }

    private Object mapearMinimisVO(ResultSet rs) throws SQLException {
        FilaMinimisVO minimis = new FilaMinimisVO();

        minimis.setId(rs.getInt("ID"));
        if (rs.wasNull()) {
            minimis.setId(null);
        }

        minimis.setNumExp(rs.getString("NUM_EXP"));

        minimis.setEstado(rs.getString("ESTADO"));
        minimis.setOrganismo(rs.getString("ORGANISMO"));
        minimis.setObjeto(rs.getString("OBJETO"));
        Double aux1 = new Double(rs.getDouble("IMPORTE"));
        if (aux1 != 0) {
            minimis.setImporte(aux1.doubleValue());
        }
        if (rs.getDate("FECHA") != null) {
            minimis.setFecha(rs.getDate("FECHA"));
            minimis.setFechaStr(formatoFecha.format(rs.getDate("FECHA")));
        }

        return minimis;
    }

    private Object mapearDesplegable(ResultSet rs) throws SQLException {
        DesplegableAdmonLocalVO valoresDesplegable = new DesplegableAdmonLocalVO();
        valoresDesplegable.setDes_cod(rs.getString("DES_COD"));
        valoresDesplegable.setDes_val_cod(rs.getString("DES_VAL_COD"));
        valoresDesplegable.setDes_nom(rs.getString("DES_NOM"));
        return valoresDesplegable;
    }

    /**
     *
     * @param rs
     * @return objeto FacturaVO
     * @throws SQLException
     */
    private Object mapearFacturaVO(ResultSet rs) throws SQLException {
        FacturaVO factura = new FacturaVO();
        factura.setId(rs.getInt("ID"));
        if (rs.wasNull()) {
            factura.setId(null);
        }
        factura.setNumExp(rs.getString("NUM_EXP"));
        factura.setFamilia(rs.getString("FAMILIA") != null && !rs.getString("FAMILIA").isEmpty() ? rs.getString("FAMILIA") : "");
        factura.setDescFamiliaCas(rs.getString("DESFAMILIA_C") != null && !rs.getString("DESFAMILIA_C").isEmpty() ? rs.getString("DESFAMILIA_C") : "");
        factura.setDescFamiliaEus(rs.getString("DESFAMILIA_E") != null && !rs.getString("DESFAMILIA_E").isEmpty() ? rs.getString("DESFAMILIA_E") : "");
        factura.setNumOrden(rs.getInt("NUMORDEN") != 0 ? rs.getInt("NUMORDEN") : null);
        factura.setTipoGasto(rs.getString("TIPOGASTO") != null && !rs.getString("TIPOGASTO").isEmpty() ? rs.getString("TIPOGASTO") : "");
        factura.setDescTipoGasto(rs.getString("DES_GASTO") != null && !rs.getString("DES_GASTO").isEmpty() ? rs.getString("DES_GASTO") : "");
        factura.setProveedor(rs.getString("PROVEEDOR") != null && !rs.getString("PROVEEDOR").isEmpty() ? rs.getString("PROVEEDOR") : "");
        factura.setNumFactura(rs.getString("NUMFACTURA") != null && !rs.getString("NUMFACTURA").isEmpty() ? rs.getString("NUMFACTURA") : "");
        factura.setFecEmision(rs.getDate("FECEMISION") != null ? rs.getDate("FECEMISION") : null);
        if (factura.getFecEmision() != null) {
            factura.setFecEmisionStr(formatoFecha.format(factura.getFecEmision()));
        }
        factura.setFecPago(rs.getDate("FECPAGO") != null ? rs.getDate("FECPAGO") : null);
        if (factura.getFecPago() != null) {
            factura.setFecPagoStr(formatoFecha.format(factura.getFecPago()));
        }
        factura.setImporteBase(rs.getDouble("IMPORTEBASE") != 0 ? rs.getDouble("IMPORTEBASE") : 0);
        factura.setImporteBaseValidado(rs.getDouble("IMPORTEBASE_VALIDADO") != 0 ? rs.getDouble("IMPORTEBASE_VALIDADO") : 0);
        factura.setImporteIva(rs.getDouble("IMPORTEIVA") != 0 ? rs.getDouble("IMPORTEIVA") : 0);
        factura.setImporteIvaValidado(rs.getDouble("IMPORTEIVA_VALIDADO") != 0 ? rs.getDouble("IMPORTEIVA_VALIDADO") : 0);
        factura.setImporteTotal(rs.getDouble("TOTAL") != 0 ? rs.getDouble("TOTAL") : 0);
//        factura.setImporteTotal((rs.getDouble("IMPORTEBASE") + rs.getDouble("IMPORTEIVA")));
        factura.setImporteVali(rs.getDouble("IMPORTEVALI") != 0 ? rs.getDouble("IMPORTEVALI") : 0);
        factura.setValidada(rs.getString("VALIDADA"));
        factura.setDescValidada(rs.getString("DES_VALIDADA") != null && !rs.getString("DES_VALIDADA").isEmpty() ? rs.getString("DES_VALIDADA") : "");
        factura.setIvaSub(rs.getString("IVASUB"));
        factura.setDescIvaSub(rs.getString("DES_IVASUB") != null && !rs.getString("DES_IVASUB").isEmpty() ? rs.getString("DES_IVASUB") : "");
        factura.setMotNoVal(rs.getString("MOTNOVAL"));
        factura.setDescMotNoVal(rs.getString("DES_MOTIVO") != null && !rs.getString("DES_MOTIVO").isEmpty() ? rs.getString("DES_MOTIVO") : "");
        factura.setPorcentajeIva(rs.getString("PORCIVA") != null && !rs.getString("PORCIVA").isEmpty() ? rs.getString("PORCIVA") : "");
        factura.setPorcentajeIvaValidado(rs.getString("PORCIVA_VALIDADO") != null && !rs.getString("PORCIVA_VALIDADO").isEmpty() ? rs.getString("PORCIVA_VALIDADO") : "");
        factura.setDescPorcentajeIva(rs.getString("DES_PORCENTAJE") != null && !rs.getString("DES_PORCENTAJE").isEmpty() ? rs.getString("DES_PORCENTAJE") : "");
        factura.setDescPorcentajeIvaValidado(rs.getString("DES_PORCENTAJE_VALIDADO") != null && !rs.getString("DES_PORCENTAJE_VALIDADO").isEmpty() ? rs.getString("DES_PORCENTAJE_VALIDADO") : "");
        factura.setExento(rs.getString("EXENTO") != null && !rs.getString("EXENTO").isEmpty() ? rs.getString("EXENTO") : "");
        factura.setProrrata(rs.getInt("PRORRATA"));
        factura.setProrrataValidado(rs.getInt("PRORRATA_VALIDADO"));
        return factura;
    }

    /**
     *
     * @param rs
     * @return
     * @throws Exception
     */
    private Object mapearFamiliaSolicitadaVO(ResultSet rs) throws Exception {
        FamiliaSolicitadaVO familia = new FamiliaSolicitadaVO();
        familia.setCodigo(rs.getString("FAMILIA") != null && !rs.getString("FAMILIA").isEmpty() ? rs.getString("FAMILIA") : "");
        familia.setDescFamiliaCas(rs.getString("DESFAMILIA_C") != null && !rs.getString("DESFAMILIA_C").isEmpty() ? rs.getString("DESFAMILIA_C") : "");
        familia.setDescFamiliaEus(rs.getString("DESFAMILIA_E") != null && !rs.getString("DESFAMILIA_E").isEmpty() ? rs.getString("DESFAMILIA_E") : "");
        familia.setBaseTotal(rs.getDouble("BASETOTAL") != 0 ? rs.getDouble("BASETOTAL") : 0);
        familia.setBaseTotalValidado(rs.getDouble("BASETOTAL_VALIDADO") != 0 ? rs.getDouble("BASETOTAL_VALIDADO") : 0);
        familia.setIvaTotal(rs.getDouble("IVATOTAL") != 0 ? rs.getDouble("IVATOTAL") : 0);
        familia.setIvaTotalValidado(rs.getDouble("IVATOTAL_VALIDADO") != 0 ? rs.getDouble("IVATOTAL_VALIDADO") : 0);
        familia.setImporteTotal(rs.getDouble("IMPORTETOTAL") != 0 ? rs.getDouble("IMPORTETOTAL") : 0);
        familia.setImporteTotalValidado(rs.getDouble("IMPORTETOTAL_VALIDADO") != 0 ? rs.getDouble("IMPORTETOTAL_VALIDADO") : 0);
        familia.setValidadoTotal(rs.getDouble("VALITOTAL") != 0 ? rs.getDouble("VALITOTAL") : 0);

        return familia;
    }

}
