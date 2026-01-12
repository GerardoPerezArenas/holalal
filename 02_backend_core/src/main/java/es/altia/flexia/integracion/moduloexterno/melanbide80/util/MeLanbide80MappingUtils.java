
package es.altia.flexia.integracion.moduloexterno.melanbide80.util;

import es.altia.flexia.integracion.moduloexterno.melanbide80.vo.PersonaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide80.vo.DesplegableAdmonLocalVO;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MeLanbide80MappingUtils {

    private static MeLanbide80MappingUtils instance = null;

    private MeLanbide80MappingUtils() {
    }

    public static MeLanbide80MappingUtils getInstance() {
        if (instance == null) {
            synchronized (MeLanbide80MappingUtils.class) {
                instance = new MeLanbide80MappingUtils();
            }
        }
        return instance;
    }

    public Object map(ResultSet rs, Class clazz) throws Exception {
        return mapVers2(rs, clazz, true);
    }

    public Object mapVers2(ResultSet rs, Class clazz, boolean completo) throws Exception {
        if (clazz == PersonaVO.class) {
            return mapearPersonaVO(rs);
        } else if (clazz == DesplegableAdmonLocalVO.class) {
            return mapearDesplegableAdmonLocalVO(rs);
        }

        return null;
    }

    private Object mapearPersonaVO(ResultSet rs) throws SQLException {
        PersonaVO persona = new PersonaVO();

        persona.setNumExp(rs.getString("NUM_EXP"));
        
        persona.setId(rs.getInt("ID"));
        if (rs.wasNull()) {
            persona.setId(null);
        }
        
        persona.setDni(rs.getString("DNI"));
        persona.setNombre(rs.getString("NOMBRE"));
        persona.setApel1(rs.getString("APEL1"));
        persona.setApel2(rs.getString("APEL2"));
        
        persona.setTipcontA(rs.getString("TIPCONTA"));
        Double aux1 = new Double(rs.getDouble("TIPCONTB"));
        if (aux1 != 0) {
            persona.setTipcontB(aux1.doubleValue());
        }
        
        persona.setJornada(rs.getString("JORNADA"));
        Double aux2 = new Double(rs.getDouble("PORJORPAR"));
        if (aux2 != 0) {
            persona.setPorjorpar(aux2.doubleValue());
        }
        
        persona.setSituacion(rs.getString("SITUACION"));
        Double aux3 = new Double(rs.getDouble("REDUCJORN"));
        if (aux3 != 0) {
            persona.setReducjorn(aux3.doubleValue());
        }
        
        if (rs.getDate("FECINISIT") != null) {
            persona.setFecinisit(rs.getDate("FECINISIT"));
        }
        if (rs.getDate("FECFINSIT") != null) {
            persona.setFecfinsit(rs.getDate("FECFINSIT"));
        }
        
        Integer aux4 = new Integer(rs.getInt("NUMDIASIT"));
        if (aux4 != 0) {
            persona.setNumdiasit(aux4.intValue());
        }
        
        Double aux5 = new Double(rs.getDouble("BASEREGUL"));
        if (aux5 != 0) {
            persona.setBaseregul(aux5.doubleValue());
        }
        Double aux6 = new Double(rs.getDouble("IMPPREST"));
        if (aux6 != 0) {
            persona.setImpprest(aux6.doubleValue());
        }
        Double aux7 = new Double(rs.getDouble("COMPLSAL"));
        if (aux7 != 0) {
            persona.setComplsal(aux7.doubleValue());
        }
        Double aux8 = new Double(rs.getDouble("IMPSUBVSOL"));
        if (aux8 != 0) {
            persona.setImpsubvsol(aux8.doubleValue());
        }
       
        return persona;
    }

    private Object mapearDesplegableAdmonLocalVO(ResultSet rs) throws SQLException {
        DesplegableAdmonLocalVO valoresDesplegable = new DesplegableAdmonLocalVO();
        valoresDesplegable.setDes_cod(rs.getString("DES_COD"));
        valoresDesplegable.setDes_val_cod(rs.getString("DES_VAL_COD"));
        valoresDesplegable.setDes_nom(rs.getString("DES_NOM"));
        return valoresDesplegable;
    }

}
