
package es.altia.flexia.integracion.moduloexterno.melanbide89.util;

import es.altia.flexia.integracion.moduloexterno.melanbide89.vo.AccesoVO;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MeLanbide89MappingUtils {

    private static MeLanbide89MappingUtils instance = null;

    private MeLanbide89MappingUtils() {
    }

    public static MeLanbide89MappingUtils getInstance() {
        if (instance == null) {
            synchronized (MeLanbide89MappingUtils.class) {
                instance = new MeLanbide89MappingUtils();
            }
        }
        return instance;
    }

    public Object map(ResultSet rs, Class clazz) throws Exception {
        if (clazz == AccesoVO.class) {
            return mapearAccesoVO(rs);
        }
        
        return null;
    }
    
    private Object mapearAccesoVO(ResultSet rs) throws SQLException {
        AccesoVO acceso = new AccesoVO();
        
        acceso.setId(rs.getInt("ID"));
        if (rs.wasNull()) {
            acceso.setId(null);
        }
        
        acceso.setNumExp(rs.getString("NUM_EXP"));
        
        acceso.setNombre(rs.getString("NOMBRE"));
        acceso.setApellido1(rs.getString("APELLIDO1"));
        acceso.setApellido2(rs.getString("APELLIDO2"));
        acceso.setDninie(rs.getString("DNINIE"));
    
        
        return acceso;
    }

}
