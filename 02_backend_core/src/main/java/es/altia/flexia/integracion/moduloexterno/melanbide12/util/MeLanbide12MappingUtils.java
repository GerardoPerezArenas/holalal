package es.altia.flexia.integracion.moduloexterno.melanbide12.util;

import es.altia.flexia.integracion.moduloexterno.melanbide12.vo.DesplegableAdmonLocalVO;
import es.altia.flexia.integracion.moduloexterno.melanbide12.vo.FilaMinimisVO;
import es.altia.flexia.integracion.moduloexterno.melanbide12.vo.FilaL1ParticipanteVO;
import es.altia.flexia.integracion.moduloexterno.melanbide12.vo.FilaL1EmpresaExternaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide12.vo.FilaL1EmpresaPropiaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide12.vo.FilaL2ParticipanteVO;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class MeLanbide12MappingUtils {

    private static MeLanbide12MappingUtils instance = null;
    private final SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");

    private MeLanbide12MappingUtils() {
    }

    public static MeLanbide12MappingUtils getInstance() {
        if (instance == null) {
            synchronized (MeLanbide12MappingUtils.class) {
                instance = new MeLanbide12MappingUtils();
            }
        }
        return instance;
    }

    public Object map(ResultSet rs, Class clazz) throws Exception {
        if (clazz == FilaMinimisVO.class) {
            return mapearMinimisVO(rs);
        } else if (clazz == DesplegableAdmonLocalVO.class) {
            return mapearDesplegableAdmonLocalVO(rs);
        } else if (clazz == FilaL1ParticipanteVO.class) {
            return  mapearL1ParticipanteVO(rs);
        } else if (clazz == FilaL1EmpresaExternaVO.class) {
            return  mapearL1EmpresaExternaVO(rs);
        } else if (clazz == FilaL1EmpresaPropiaVO.class) {
            return  mapearL1EmpresaPropiaVO(rs);
        } else if (clazz == FilaL2ParticipanteVO.class) {
            return  mapearL2ParticipanteVO(rs);
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

    private Object mapearL1ParticipanteVO(ResultSet rs) throws SQLException {
        FilaL1ParticipanteVO l1Participante = new FilaL1ParticipanteVO();

        l1Participante.setId(rs.getInt("ID"));
        if (rs.wasNull()) {
            l1Participante.setId(null);
        }
        l1Participante.setNumExp(rs.getString("NUM_EXP"));
        l1Participante.setTipoDoc(rs.getString("TIPO_DOC") != null && !rs.getString("TIPO_DOC").isEmpty() ? rs.getString("TIPO_DOC") : "");
        l1Participante.setDoc(rs.getString("DOC") != null && !rs.getString("DOC").isEmpty() ? rs.getString("DOC") : "");
        l1Participante.setNombre(rs.getString("NOMBRE") != null && !rs.getString("NOMBRE").isEmpty() ? rs.getString("NOMBRE") : "");
        l1Participante.setApe1(rs.getString("APE1") != null && !rs.getString("APE1").isEmpty() ? rs.getString("APE1") : "");
        l1Participante.setApe2(rs.getString("APE2") != null && !rs.getString("APE2").isEmpty() ? rs.getString("APE2") : "");
        l1Participante.setNss(rs.getString("NSS") != null && !rs.getString("NSS").isEmpty() ? rs.getString("NSS") : "");
        l1Participante.setCodActForm(rs.getString("COD_ACT_FORM") != null && !rs.getString("COD_ACT_FORM").isEmpty() ? rs.getString("COD_ACT_FORM") : "");
        if (rs.getDate("FEC_INI_PRACT") != null) {
            l1Participante.setFecIniPract(rs.getDate("FEC_INI_PRACT"));
            l1Participante.setFecIniPractStr(formatoFecha.format(rs.getDate("FEC_INI_PRACT")));
        }
        if (rs.getDate("FEC_FIN_PRACT") != null) {
            l1Participante.setFecFinPract(rs.getDate("FEC_FIN_PRACT"));
            l1Participante.setFecFinPractStr(formatoFecha.format(rs.getDate("FEC_FIN_PRACT")));
        }
        l1Participante.setCcCot(rs.getString("CC_COT") != null && !rs.getString("CC_COT").isEmpty() ? rs.getString("CC_COT") : "");
        Double aux1 = new Double(rs.getDouble("DIAS_COT"));
        if (aux1 != 0) {
            l1Participante.setDiasCot(aux1.doubleValue());
        }
        Double aux2 = new Double(rs.getDouble("IMP_SOLIC"));
        if (aux2 != 0) {
            l1Participante.setImpSolic(aux2.doubleValue());
        }

        return l1Participante;
    }

    private Object mapearL1EmpresaExternaVO(ResultSet rs) throws SQLException {
        FilaL1EmpresaExternaVO l1EmpresaExterna = new FilaL1EmpresaExternaVO();

        l1EmpresaExterna.setId(rs.getInt("ID"));
        if (rs.wasNull()) {
            l1EmpresaExterna.setId(null);
        }
        l1EmpresaExterna.setNumExp(rs.getString("NUM_EXP"));
        l1EmpresaExterna.setCif(rs.getString("CIF") != null && !rs.getString("CIF").isEmpty() ? rs.getString("CIF") : "");
        l1EmpresaExterna.setDenomEmpr(rs.getString("DENOM_EMPR") != null && !rs.getString("DENOM_EMPR").isEmpty() ? rs.getString("DENOM_EMPR") : "");
        l1EmpresaExterna.setnFactura(rs.getString("N_FACTURA") != null && !rs.getString("N_FACTURA").isEmpty() ? rs.getString("N_FACTURA") : "");
        if (rs.getDate("FEC_EMIS") != null) {
            l1EmpresaExterna.setFecEmis(rs.getDate("FEC_EMIS"));
            l1EmpresaExterna.setFecEmisStr(formatoFecha.format(rs.getDate("FEC_EMIS")));
        }
        if (rs.getDate("FEC_PAGO") != null) {
            l1EmpresaExterna.setFecPago(rs.getDate("FEC_PAGO"));
            l1EmpresaExterna.setFecPagoStr(formatoFecha.format(rs.getDate("FEC_PAGO")));
        }
        Double aux1 = new Double(rs.getDouble("IMP_BASE"));
        if (aux1 != 0) {
            l1EmpresaExterna.setImpBase(aux1.doubleValue());
        }
        Double aux2 = new Double(rs.getDouble("IMP_IVA"));
        if (aux2 != 0) {
            l1EmpresaExterna.setImpIva(aux2.doubleValue());
        }
        Double aux3 = new Double(rs.getDouble("IMP_TOTAL"));
        if (aux3 != 0) {
            l1EmpresaExterna.setImpTotal(aux3.doubleValue());
        }
        Double aux4 = new Double(rs.getDouble("PERSONAS"));
        if (aux4 != 0) {
            l1EmpresaExterna.setPersonas(aux4.doubleValue());
        }
        Double aux5 = new Double(rs.getDouble("IMP_PERSONA_FACT"));
        if (aux5 != 0) {
            l1EmpresaExterna.setImpPersonaFact(aux5.doubleValue());
        }
        Double aux6 = new Double(rs.getDouble("IMP_SOLIC"));
        if (aux6 != 0) {
            l1EmpresaExterna.setImpSolic(aux6.doubleValue());
        }

        return l1EmpresaExterna;
    }

    private Object mapearL1EmpresaPropiaVO(ResultSet rs) throws SQLException {
        FilaL1EmpresaPropiaVO l1EmpresaPropia = new FilaL1EmpresaPropiaVO();

        l1EmpresaPropia.setId(rs.getInt("ID"));
        if (rs.wasNull()) {
            l1EmpresaPropia.setId(null);
        }
        l1EmpresaPropia.setNumExp(rs.getString("NUM_EXP"));
        l1EmpresaPropia.setDni(rs.getString("DNI") != null && !rs.getString("DNI").isEmpty() ? rs.getString("DNI") : "");
        l1EmpresaPropia.setNombre(rs.getString("NOMBRE") != null && !rs.getString("NOMBRE").isEmpty() ? rs.getString("NOMBRE") : "");
        l1EmpresaPropia.setApe1(rs.getString("APE1") != null && !rs.getString("APE1").isEmpty() ? rs.getString("APE1") : "");
        l1EmpresaPropia.setApe2(rs.getString("APE2") != null && !rs.getString("APE2").isEmpty() ? rs.getString("APE2") : "");
        Double aux1 = new Double(rs.getDouble("RETR_ANUAL_BRUTA"));
        if (aux1 != 0) {
            l1EmpresaPropia.setRetrAnualBruta(aux1.doubleValue());
        }
        Double aux2 = new Double(rs.getDouble("CC_COT_SS"));
        if (aux2 != 0) {
            l1EmpresaPropia.setCcCotSs(aux2.doubleValue());
        }
        Double aux3 = new Double(rs.getDouble("HORAS_LAB_ANUAL"));
        if (aux3 != 0) {
            l1EmpresaPropia.setHorasLabAnual(aux3.doubleValue());
        }
        Double aux4 = new Double(rs.getDouble("HORAS_IMPUT"));
        if (aux4 != 0) {
            l1EmpresaPropia.setHorasImput(aux4.doubleValue());
        }
        Double aux5 = new Double(rs.getDouble("IMP_GEST"));
        if (aux5 != 0) {
            l1EmpresaPropia.setImpGest(aux5.doubleValue());
        }
        Double aux6 = new Double(rs.getDouble("PERSON_PRACT"));
        if (aux6 != 0) {
            l1EmpresaPropia.setPersonPract(aux6.doubleValue());
        }
        Double aux7 = new Double(rs.getDouble("IMP_SOLIC"));
        if (aux7 != 0) {
            l1EmpresaPropia.setImpSolic(aux7.doubleValue());
        }

        return l1EmpresaPropia;
    }

    private Object mapearL2ParticipanteVO(ResultSet rs) throws SQLException {
        FilaL2ParticipanteVO l2Participante = new FilaL2ParticipanteVO();

        l2Participante.setId(rs.getInt("ID"));
        if (rs.wasNull()) {
            l2Participante.setId(null);
        }
        l2Participante.setNumExp(rs.getString("NUM_EXP"));
        l2Participante.setTipoDoc(rs.getString("TIPO_DOC") != null && !rs.getString("TIPO_DOC").isEmpty() ? rs.getString("TIPO_DOC") : "");
        l2Participante.setDoc(rs.getString("DOC") != null && !rs.getString("DOC").isEmpty() ? rs.getString("DOC") : "");
        l2Participante.setNombre(rs.getString("NOMBRE") != null && !rs.getString("NOMBRE").isEmpty() ? rs.getString("NOMBRE") : "");
        l2Participante.setApe1(rs.getString("APE1") != null && !rs.getString("APE1").isEmpty() ? rs.getString("APE1") : "");
        l2Participante.setApe2(rs.getString("APE2") != null && !rs.getString("APE2").isEmpty() ? rs.getString("APE2") : "");
        l2Participante.setCodActForm(rs.getString("COD_ACT_FORM") != null && !rs.getString("COD_ACT_FORM").isEmpty() ? rs.getString("COD_ACT_FORM") : "");
        Double aux1 = new Double(rs.getDouble("HORAS_PRACT"));
        if (aux1 != 0) {
            l2Participante.setHorasPract(aux1.doubleValue());
        }
        Double aux2 = new Double(rs.getDouble("IMP_SOLIC"));
        if (aux2 != 0) {
            l2Participante.setImpSolic(aux2.doubleValue());
        }

        return l2Participante;
    }

    private Object mapearDesplegableAdmonLocalVO(ResultSet rs) throws SQLException {
        DesplegableAdmonLocalVO valoresDesplegable = new DesplegableAdmonLocalVO();
        valoresDesplegable.setDes_cod(rs.getString("DES_COD"));
        valoresDesplegable.setDes_val_cod(rs.getString("DES_VAL_COD"));
        valoresDesplegable.setDes_nom(rs.getString("DES_NOM"));
        return valoresDesplegable;
    }

}
