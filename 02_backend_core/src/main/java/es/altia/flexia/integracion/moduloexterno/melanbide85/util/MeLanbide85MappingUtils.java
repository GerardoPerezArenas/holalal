package es.altia.flexia.integracion.moduloexterno.melanbide85.util;

import es.altia.flexia.integracion.moduloexterno.melanbide85.vo.DesplegableAdmonLocalVO;
import es.altia.flexia.integracion.moduloexterno.melanbide85.vo.FilaContratacionVO;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class MeLanbide85MappingUtils {

    private static MeLanbide85MappingUtils instance = null;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    private MeLanbide85MappingUtils() {
    }

    public static MeLanbide85MappingUtils getInstance() {
        if (instance == null) {
            synchronized (MeLanbide85MappingUtils.class) {
                instance = new MeLanbide85MappingUtils();
            }
        }
        return instance;
    }

    public Object map(ResultSet rs, Class clazz) throws Exception {
        if (clazz == FilaContratacionVO.class) {
            return mapearContratacionVO(rs);
        } else if (clazz == DesplegableAdmonLocalVO.class) {
            return mapearDesplegableAdmonLocalVO(rs);
        }

        return null;
    }

    private Object mapearContratacionVO(ResultSet rs) throws SQLException {
        FilaContratacionVO contratacion = new FilaContratacionVO();

        contratacion.setId(rs.getInt("ID"));
        if (rs.wasNull()) {
            contratacion.setId(null);
        }

        contratacion.setNumExp(rs.getString("NUM_EXP"));
        Integer aux1 = new Integer(rs.getInt("NUM_PUESTO"));
        if (aux1 != 0) {
            contratacion.setNumpuesto(aux1.intValue());
        }

        //fase1 - solicitud__________________________
        contratacion.setDenompuesto1(rs.getString("DENOMPUESTO1"));
        contratacion.setTitulacion1(rs.getString("TITULACION1"));
        contratacion.setMunicipioct1(rs.getString("MUNICIPIOCT1"));
        contratacion.setDurcontrato1(rs.getString("DURCONTRATO1"));
        contratacion.setGrupocotiz1(rs.getString("GRUPOCOTIZ1"));
        Double aux2 = rs.getDouble("COSTESALARIAL1");
        if (aux2 != 0) {
            contratacion.setCostesalarial1(aux2);
        }
        Double aux3 = new Double(rs.getDouble("COSTEEPIS1"));
        if (aux3 != 0) {
            contratacion.setCosteepis1(aux3.doubleValue());
        }
        Double aux4 = new Double(rs.getDouble("SUBVSOLICITADA1"));
        if (aux4 != 0) {
            contratacion.setSubvsolicitada1(aux4.doubleValue());
        }
        contratacion.setEmpverde1(rs.getString("EMPVERDE1"));
        contratacion.setEmpdigit1(rs.getString("EMPDIGIT1"));
        contratacion.setEmpgen1(rs.getString("EMPGEN1"));

        //fase2 - inicio contrato____________________
        if (rs.getString("NOFERTA2") != null && !rs.getString("NOFERTA2").isEmpty()) {
            contratacion.setNoferta2(rs.getString("NOFERTA2"));
        } else {
            contratacion.setNoferta2("");
        }
        if (rs.getString("NOMBRE2") != null && !rs.getString("NOMBRE2").isEmpty()) {
            contratacion.setNombre2(rs.getString("NOMBRE2"));
        } else {
            contratacion.setNombre2("");
        }
        if (rs.getString("APELLIDO12") != null && !rs.getString("APELLIDO12").isEmpty()) {
            contratacion.setApellido12(rs.getString("APELLIDO12"));
        } else {
            contratacion.setApellido12("");
        }
        if (rs.getString("APELLIDO22") != null && !rs.getString("APELLIDO22").isEmpty()) {
            contratacion.setApellido22(rs.getString("APELLIDO22"));
        } else {
            contratacion.setApellido22("");
        }
        if (rs.getString("DNINIE2") != null && !rs.getString("DNINIE2").isEmpty()) {
            contratacion.setDninie2(rs.getString("DNINIE2"));
        } else {
            contratacion.setDninie2("");
        }
        if (rs.getString("SEXO2") != null && !rs.getString("SEXO2").isEmpty()) {
            contratacion.setSexo2(rs.getString("SEXO2"));
        } else {
            contratacion.setSexo2("");
        }
        if (rs.getString("TITULACION2") != null && !rs.getString("TITULACION2").isEmpty()) {
            contratacion.setTitulacion2(rs.getString("TITULACION2"));
        } else {
            contratacion.setTitulacion2("");
        }
        if (rs.getString("DENOMPUESTO2") != null && !rs.getString("DENOMPUESTO2").isEmpty()) {
            contratacion.setDenompuesto2(rs.getString("DENOMPUESTO2"));
        } else {
            contratacion.setDenompuesto2("");
        }
        if (rs.getString("MUNICIPIOCT2") != null && !rs.getString("MUNICIPIOCT2").isEmpty()) {
            contratacion.setMunicipioct2(rs.getString("MUNICIPIOCT2"));
        } else {
            contratacion.setMunicipioct2("");
        }
        if (rs.getString("GRUPOCOTIZ2") != null && !rs.getString("GRUPOCOTIZ2").isEmpty()) {
            contratacion.setGrupocotiz2(rs.getString("GRUPOCOTIZ2"));
        } else {
            contratacion.setGrupocotiz2("");
        }
        if (rs.getString("DURCONTRATO2") != null && !rs.getString("DURCONTRATO2").isEmpty()) {
            contratacion.setDurcontrato2(rs.getString("DURCONTRATO2"));
        } else {
            contratacion.setDurcontrato2("");
        }
        if (rs.getDate("FECHANACIMIENTO2") != null) {
            contratacion.setFechanacimiento2(rs.getDate("FECHANACIMIENTO2"));
            contratacion.setFecNacStr2(dateFormat.format(rs.getDate("FECHANACIMIENTO2")));
        }
        if (rs.getDate("FECHAINICIO2") != null) {
            contratacion.setFechainicio2(rs.getDate("FECHAINICIO2"));
            contratacion.setFecIniStr2(dateFormat.format(rs.getDate("FECHAINICIO2")));
        }
        Integer aux5 = new Integer(rs.getInt("EDAD"));
        if (aux5 != 0) {
            contratacion.setEdad(aux5.intValue());
        }
        Double aux6 = new Double(rs.getDouble("RETRIBUCIONBRUTA2"));
        if (aux6 != 0) {
            contratacion.setRetribucionbruta2(aux6.doubleValue());
        }
        if (rs.getString("EMPVERDE2") != null && !rs.getString("EMPVERDE2").isEmpty()) {
            contratacion.setEmpverde2(rs.getString("EMPVERDE2"));
        } else {
            contratacion.setEmpverde2("");
        }
        if (rs.getString("EMPDIGIT2") != null && !rs.getString("EMPDIGIT2").isEmpty()) {
            contratacion.setEmpdigit2(rs.getString("EMPDIGIT2"));
        } else {
            contratacion.setEmpdigit2("");
        }
        if (rs.getString("EMPGEN2") != null && !rs.getString("EMPGEN2").isEmpty()) {
            contratacion.setEmpgen2(rs.getString("EMPGEN2"));
        } else {
            contratacion.setEmpgen2("");
        }

        //fase3 - fin contrato_______________________
        if (rs.getString("NOMBRE3") != null && !rs.getString("NOMBRE3").isEmpty()) {
            contratacion.setNombre3(rs.getString("NOMBRE3"));
        } else {
            contratacion.setNombre3("");
        }
        if (rs.getString("APELLIDO13") != null && !rs.getString("APELLIDO13").isEmpty()) {
            contratacion.setApellido13(rs.getString("APELLIDO13"));
        } else {
            contratacion.setApellido13("");
        }
        if (rs.getString("APELLIDO23") != null && !rs.getString("APELLIDO23").isEmpty()) {
            contratacion.setApellido23(rs.getString("APELLIDO23"));
        } else {
            contratacion.setApellido23("");
        }
        if (rs.getString("DNINIE3") != null && !rs.getString("DNINIE3").isEmpty()) {
            contratacion.setDninie3(rs.getString("DNINIE3"));
        } else {
            contratacion.setDninie3("");
        }
        if (rs.getString("DURCONTRATO3") != null && !rs.getString("DURCONTRATO3").isEmpty()) {
            contratacion.setDurcontrato3(rs.getString("DURCONTRATO3"));
        } else {
            contratacion.setDurcontrato3("");
        }
        if (rs.getDate("FECHAINICIO3") != null) {
            contratacion.setFechainicio3(rs.getDate("FECHAINICIO3"));
            contratacion.setFecIniStr3(dateFormat.format(rs.getDate("FECHAINICIO3")));
        }
        if (rs.getDate("FECHAFIN3") != null) {
            contratacion.setFechafin3(rs.getDate("FECHAFIN3"));
            contratacion.setFecFinStr3(dateFormat.format(rs.getDate("FECHAFIN3")));
        }
        Double aux7 = new Double(rs.getDouble("COSTESALARIAL3"));
        if (aux7 != 0) {
            contratacion.setCostesalarial3(aux7.doubleValue());
        }
        Double aux8 = new Double(rs.getDouble("COSTESSS3"));
        if (aux8 != 0) {
            contratacion.setCostesss3(aux8.doubleValue());
        }
        Double aux9 = new Double(rs.getDouble("COSTEEPIS3"));
        if (aux9 != 0) {
            contratacion.setCosteepis3(aux9.doubleValue());
        }
        Double aux10 = new Double(rs.getDouble("COSTETOTALREAL3"));
        if (aux10 != 0) {
            contratacion.setCostetotalreal3(aux10.doubleValue());
        }
        Double aux11 = new Double(rs.getDouble("SUBVCONCEDIDALAN3"));
        if (aux11 != 0) {
            contratacion.setSubvconcedidalan3(aux11.doubleValue());
        }

        return contratacion;
    }

    private Object mapearDesplegableAdmonLocalVO(ResultSet rs) throws SQLException {
        DesplegableAdmonLocalVO valoresDesplegable = new DesplegableAdmonLocalVO();
        valoresDesplegable.setDes_cod(rs.getString("DES_COD"));
        valoresDesplegable.setDes_val_cod(rs.getString("DES_VAL_COD"));
        valoresDesplegable.setDes_nom(rs.getString("DES_NOM"));
        return valoresDesplegable;
    }

}
