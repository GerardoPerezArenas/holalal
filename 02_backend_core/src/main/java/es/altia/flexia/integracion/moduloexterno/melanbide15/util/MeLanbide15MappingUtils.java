package es.altia.flexia.integracion.moduloexterno.melanbide15.util;

import es.altia.flexia.integracion.moduloexterno.melanbide15.vo.ContratacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide15.vo.DesplegableVO;
import es.altia.flexia.integracion.moduloexterno.melanbide15.vo.FormacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide15.vo.IdentidadVO;
import es.altia.flexia.integracion.moduloexterno.melanbide15.vo.OrientacionVO;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class MeLanbide15MappingUtils {

    private static MeLanbide15MappingUtils instance = null;
    private final SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");

    private MeLanbide15MappingUtils() {
    }

    public static MeLanbide15MappingUtils getInstance() {
        if (instance == null) {
            synchronized (MeLanbide15MappingUtils.class) {
                instance = new MeLanbide15MappingUtils();
            }
        }
        return instance;
    }

    public Object map(ResultSet rs, Class clazz) throws Exception {
        if (clazz == IdentidadVO.class) {
            return mapearIdentidadVO(rs);
        } else if (clazz == DesplegableVO.class) {
            return mapearDesplegable(rs);
        } else if (clazz == FormacionVO.class) {
            return mapearFormacionVO(rs);
        } else if (clazz == OrientacionVO.class) {
            return mapearOrientacionVO(rs);
        } else if (clazz == ContratacionVO.class) {
            return mapearContratacionVO(rs);
        }
        return null;
    }

    public Object mapearIdentidadVO(ResultSet rs) throws SQLException {
        IdentidadVO identidad = new IdentidadVO();
        identidad.setId(rs.getInt("ID"));
        if (rs.wasNull()) {
            identidad.setId(null);
        }
        identidad.setNumExp(rs.getString("NUM_EXP") != null && !rs.getString("NUM_EXP").isEmpty() ? rs.getString("NUM_EXP") : "");
        identidad.setDniNie(rs.getString("DNI") != null && !rs.getString("DNI").isEmpty() ? rs.getString("DNI") : "");
        identidad.setNumIden(rs.getString("NUM_IDEN") != null && !rs.getString("NUM_IDEN").isEmpty() ? rs.getString("NUM_IDEN") : "");
        identidad.setNombre(rs.getString("NOMBRE") != null && !rs.getString("NOMBRE").isEmpty() ? rs.getString("NOMBRE") : "");
        identidad.setApellido1(rs.getString("APELLIDO1") != null && !rs.getString("APELLIDO1").isEmpty() ? rs.getString("APELLIDO1") : "");
        identidad.setApellido2(rs.getString("APELLIDO2") != null && !rs.getString("APELLIDO2").isEmpty() ? rs.getString("APELLIDO2") : "");
        identidad.setSexo(rs.getString("SEXO") != null && !rs.getString("SEXO").isEmpty() ? rs.getString("SEXO") : "");

        // Manejo de la fecha de nacimiento
        java.sql.Date fechaNac = rs.getDate("FEC_NAC");
        identidad.setFechaNacimiento(fechaNac); // Asignación directa
        if (identidad.getFechaNacimiento() != null) {
            identidad.setFecNacStr(formatoFecha.format(identidad.getFechaNacimiento()));
        }
        identidad.setSustituto(rs.getString("SUSTITUTO") != null && !rs.getString("SUSTITUTO").isEmpty() ? rs.getString("SUSTITUTO") : "");

        return identidad;
    }

    public FormacionVO mapearFormacionVO(ResultSet rs) throws SQLException {
        FormacionVO formacion = new FormacionVO();
        formacion.setId(rs.getInt("ID"));
        if (rs.wasNull()) {
            formacion.setId(null);
        }
        formacion.setNumExp(rs.getString("NUM_EXP") != null && !rs.getString("NUM_EXP").isEmpty() ? rs.getString("NUM_EXP") : "");
        formacion.setDniFor(rs.getString("DNI_FOR") != null && !rs.getString("DNI_FOR").isEmpty() ? rs.getString("DNI_FOR") : "");
        formacion.setNumIdenFor(rs.getString("NUM_IDEN_FOR") != null && !rs.getString("NUM_IDEN_FOR").isEmpty() ? rs.getString("NUM_IDEN_FOR") : "");

        // Manejo de valores opcionales
        double porcentajeFor = rs.getDouble("PORCENTAJE_FOR");
        formacion.setPorcentajeFor(rs.wasNull() ? null : porcentajeFor); // Porcentaje de Formación
        // Manejo seguro de valores numéricos
        double horasFor = rs.getDouble("HORAS_FOR");
        formacion.setHorasFor(rs.wasNull() ? null : horasFor); // Horas de Formación

        double subvencionFor = rs.getDouble("SUBVENCION_FOR");
        formacion.setSubvencionFor(rs.wasNull() ? null : subvencionFor); // Subvención

        return formacion;
    }

    public OrientacionVO mapearOrientacionVO(ResultSet rs) throws SQLException {
        OrientacionVO objeto = new OrientacionVO();

        // Asignación de valores con manejo de nulos
        objeto.setId(rs.getObject("ID") != null ? rs.getInt("ID") : null); // ID secuencial
        objeto.setNumExp(getStringOrDefault(rs, "NUM_EXP", "")); // Número de Expediente
        objeto.setDniOri(getStringOrDefault(rs, "DNI_ORI", "")); // DNI / NIE
        objeto.setNumIdenOri(getStringOrDefault(rs, "NUM_IDEN_ORI", "")); // Número de Identificación

        // Manejo seguro de valores numéricos
        double horasOri = rs.getDouble("HORAS_ORI");
        objeto.setHorasOri(rs.wasNull() ? null : horasOri); // Horas de Orientación

        double subvencionOri = rs.getDouble("SUBVENCION_ORI");
        objeto.setSubvencionOri(rs.wasNull() ? null : subvencionOri); // Subvención

        return objeto;
    }

    public ContratacionVO mapearContratacionVO(ResultSet rs) throws SQLException {
        ContratacionVO objeto = new ContratacionVO();

        // Asignación de valores con manejo de nulos
        objeto.setId(rs.getObject("ID") != null ? rs.getInt("ID") : null); // ID secuencial
        objeto.setNumExp(getStringOrDefault(rs, "NUM_EXP", "")); // Número de Expediente
        objeto.setDniCon(getStringOrDefault(rs, "DNI_CON", "")); // DNI / NIE
        objeto.setNumIdenCon(getStringOrDefault(rs, "NUM_IDEN_CON", "")); // Número de Identificación

        java.sql.Date fecIniCon = rs.getDate("FEC_INI_CON");
        objeto.setFecIniCon(fecIniCon); // Asignación directa
        if (objeto.getFecIniCon() != null) {
            objeto.setFecIniConStr(formatoFecha.format(objeto.getFecIniCon()));
        }
        java.sql.Date fecFinCon = rs.getDate("FEC_INI_CON");
        objeto.setFecFinCon(fecFinCon); // Asignación directa
        if (objeto.getFecFinCon() != null) {
            objeto.setFecFinConStr(formatoFecha.format(objeto.getFecFinCon()));
        }

        // Manejo seguro de valores opcionales
        double subvencionCon = rs.getDouble("SUBVENCION_CON");
        objeto.setSubvencionCon(rs.wasNull() ? null : subvencionCon); // Subvención

        return objeto;
    }

// Método auxiliar reutilizable
    private String getStringOrDefault(ResultSet rs, String columnName, String defaultValue) throws SQLException {
        String value = rs.getString(columnName);
        return value != null && !value.isEmpty() ? value : defaultValue;
    }

    private Object mapearDesplegable(ResultSet rs) throws SQLException {
        DesplegableVO valoresDesplegable = new DesplegableVO();
        valoresDesplegable.setDes_cod(rs.getString("DES_COD"));
        valoresDesplegable.setDes_val_cod(rs.getString("DES_VAL_COD"));
        valoresDesplegable.setDes_nom(rs.getString("DES_NOM"));
        return valoresDesplegable;
    }

}
