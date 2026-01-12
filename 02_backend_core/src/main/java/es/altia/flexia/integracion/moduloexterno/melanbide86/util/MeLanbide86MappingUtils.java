package es.altia.flexia.integracion.moduloexterno.melanbide86.util;

import es.altia.flexia.integracion.moduloexterno.melanbide86.vo.DesplegableAdmonLocalVO;
import es.altia.flexia.integracion.moduloexterno.melanbide86.vo.FilaContratacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide86.vo.FilaMinimisVO;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class MeLanbide86MappingUtils {

    private static MeLanbide86MappingUtils instance = null;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    private MeLanbide86MappingUtils() {
    }

    public static MeLanbide86MappingUtils getInstance() {
        if (instance == null) {
            synchronized (MeLanbide86MappingUtils.class) {
                instance = new MeLanbide86MappingUtils();
            }
        }
        return instance;
    }

    public Object map(ResultSet rs, Class clazz) throws Exception {
        if (clazz == FilaContratacionVO.class) {
            return mapearContratacion(rs);
        } 
        if (clazz == FilaMinimisVO.class) {
            return mapearMinimisVO(rs);
        } 
        if (clazz == DesplegableAdmonLocalVO.class) {
            return mapearDesplegableAdmonLocalVO(rs);
        }
        return null;
    }

    public Object mapearContratacion(ResultSet rs) throws SQLException {
        FilaContratacionVO filaContratacion = new FilaContratacionVO();

        filaContratacion.setId(rs.getInt("ID"));
        if (rs.wasNull()) {
            filaContratacion.setId(null);
        }
        filaContratacion.setNumExp(rs.getString("NUM_EXP"));
        filaContratacion.setNumPuesto(rs.getInt("NUM_PUESTO"));
        filaContratacion.setDenomPuesto(rs.getString("DENOMPUESTO1").replace("\'", ""));
        filaContratacion.setActDes1(rs.getString("ACTDES1"));
        filaContratacion.setTitulacion1(rs.getString("TITULACION1"));
        filaContratacion.setTipoCont1(rs.getString("TIPCONT1"));
        filaContratacion.setDurContrato1(rs.getInt("DURCONTRATO1"));
        filaContratacion.setGrupoCotiz1(rs.getString("GRUPOCOTIZ1"));
        filaContratacion.setCosteSalarial1(rs.getDouble("COSTESALARIAL1"));
        filaContratacion.setSubvSolicitada1(rs.getDouble("SUBVSOLICITADA1"));
        filaContratacion.setCainVinn1(rs.getString("CAINVINN1"));
        // NOFERTA2 NOMBRE2 APELLIDO12 APELLIDO22 DNINIE2 SEXO2 DENOMPUESTO2 ACTDES2 TITULACION2 TIPCONT2 DURCONTRATO2 GRUPOCOTIZ2 FECHANACIMIENTO2 FECHAINICIO2 EDAD2 RETRIBUCIONBRUTA2 CAINVINN2
        if (rs.getString("NOFERTA2") != null && !rs.getString("NOFERTA2").isEmpty()) {
            filaContratacion.setnOferta2(rs.getString("NOFERTA2"));
        } else {
            filaContratacion.setnOferta2("");
        }
        if (rs.getString("NOMBRE2") != null && !rs.getString("NOMBRE2").isEmpty()) {
            filaContratacion.setNombre2(rs.getString("NOMBRE2"));
        } else {
            filaContratacion.setNombre2("");
        }

        filaContratacion.setApellido12(rs.getString("APELLIDO12") != null && !rs.getString("APELLIDO12").isEmpty() ? rs.getString("APELLIDO12") : "");
        filaContratacion.setApellido22(rs.getString("APELLIDO22") != null && !rs.getString("APELLIDO22").isEmpty() ? rs.getString("APELLIDO22") : "");
        filaContratacion.setDniNie2(rs.getString("DNINIE2") != null && !rs.getString("DNINIE2").isEmpty() ? rs.getString("DNINIE2") : "");
        filaContratacion.setSexo2(rs.getString("SEXO2") != null && !rs.getString("SEXO2").isEmpty() ? rs.getString("SEXO2") : "");
        filaContratacion.setDenomPuesto2(rs.getString("DENOMPUESTO2") != null && !rs.getString("DENOMPUESTO2").isEmpty() ? rs.getString("DENOMPUESTO2") : "");
        filaContratacion.setActDes2(rs.getString("ACTDES2") != null && !rs.getString("ACTDES2").isEmpty() ? rs.getString("ACTDES2") : "");
        filaContratacion.setTitulacion2(rs.getString("TITULACION2") != null && !rs.getString("TITULACION2").isEmpty() ? rs.getString("TITULACION2") : "");
        filaContratacion.setTipoCont2(rs.getString("TIPCONT2") != null && !rs.getString("TIPCONT2").isEmpty() ? rs.getString("TIPCONT2") : "");
        filaContratacion.setGrupoCotiz2(rs.getString("GRUPOCOTIZ2") != null && !rs.getString("GRUPOCOTIZ2").isEmpty() ? rs.getString("GRUPOCOTIZ2") : "");
        Integer aux = rs.getInt("DURCONTRATO2");
        if (aux != 0) {
            filaContratacion.setDurContrato2(aux);
        }
        if (rs.getDate("FECHANACIMIENTO2") != null) {
            filaContratacion.setFechaNacimiento2(rs.getDate("FECHANACIMIENTO2"));
            filaContratacion.setFecNacStr2(dateFormat.format(rs.getDate("FECHANACIMIENTO2")));
        }
        if (rs.getDate("FECHAINICIO2") != null) {
            filaContratacion.setFechaInicio2(rs.getDate("FECHAINICIO2"));
            filaContratacion.setFecIniStr2(dateFormat.format(rs.getDate("FECHAINICIO2")));
        }
        aux = rs.getInt("EDAD2");
        if (aux != 0) {
            filaContratacion.setEdad2(aux);
        }
        Double aux1 = rs.getDouble("RETRIBUCIONBRUTA2");
        if (aux1 != 0) {
            filaContratacion.setRetribucionBruta2(rs.getDouble("RETRIBUCIONBRUTA2"));
        }
        filaContratacion.setCainVinn2(rs.getString("CAINVINN2") != null && !rs.getString("CAINVINN2").isEmpty() ? rs.getString("CAINVINN2") : "");

// NOMBRE3 APELLIDO13 APELLIDO23 DNINIE3 DURCONTRATO3 FECHAINICIO3 FECHAFIN3 COSTESALARIAL3 COSTESSS3 COSTETOTALREAL3 SUBVCONCEDIDALAN3
        if (rs.getString("NOMBRE3") != null && !rs.getString("NOMBRE3").isEmpty()) {
            filaContratacion.setNombre3(rs.getString("NOMBRE3"));
        } else {
            filaContratacion.setNombre3("");
        }
        if (rs.getString("APELLIDO13") != null && !rs.getString("APELLIDO13").isEmpty()) {
            filaContratacion.setApellido13(rs.getString("APELLIDO13"));
        } else {
            filaContratacion.setApellido13("");
        }
        if (rs.getString("APELLIDO23") != null && !rs.getString("APELLIDO23").isEmpty()) {
            filaContratacion.setApellido23(rs.getString("APELLIDO23"));
        } else {
            filaContratacion.setApellido23("");
        }
        if (rs.getString("DNINIE3") != null && !rs.getString("DNINIE3").isEmpty()) {
            filaContratacion.setDniNie3(rs.getString("DNINIE3"));
        } else {
            filaContratacion.setDniNie3("");
        }
        aux = rs.getInt("DURCONTRATO3");
        if (aux != 0) {
            filaContratacion.setDurContrato3(aux);
        }
        if (rs.getDate("FECHAINICIO3") != null) {
            filaContratacion.setFechaInicio3(rs.getDate("FECHAINICIO3"));
            filaContratacion.setFecIniStr3(dateFormat.format(rs.getDate("FECHAINICIO3")));
        }
        if (rs.getDate("FECHAFIN3") != null) {
            filaContratacion.setFechaFin3(rs.getDate("FECHAFIN3"));
            filaContratacion.setFecFinStr3(dateFormat.format(rs.getDate("FECHAFIN3")));
        }
        aux1 = rs.getDouble("COSTESALARIAL3");
        if (aux1 != 0) {
            filaContratacion.setCosteSalarial3(rs.getDouble("COSTESALARIAL3"));
        }
        aux1 = rs.getDouble("COSTESSS3");
        if (aux1 != 0) {
            filaContratacion.setCostesSS3(rs.getDouble("COSTESSS3"));
        }
        aux1 = rs.getDouble("COSTETOTALREAL3");
        if (aux1 != 0) {
            filaContratacion.setCosteTotalReal(rs.getDouble("COSTETOTALREAL3"));
        }
        aux1 = rs.getDouble("SUBVCONCEDIDALAN3");
        if (aux1 != 0) {
            filaContratacion.setSubvConcedidaLan3(rs.getDouble("SUBVCONCEDIDALAN3"));
        }

        return filaContratacion;
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
            minimis.setFechaStr(dateFormat.format(rs.getDate("FECHA")));
        }

        return minimis;
    }
     

    private Object mapearDesplegableAdmonLocalVO(ResultSet rs) throws SQLException {
        DesplegableAdmonLocalVO valoresDesplegable = new DesplegableAdmonLocalVO();
        valoresDesplegable.setDes_cod(rs.getString("DES_COD"));
        valoresDesplegable.setDes_val_cod(rs.getString("DES_VAL_COD"));
        valoresDesplegable.setDes_nom(rs.getString("DES_NOM"));
        return valoresDesplegable;
    }

}
