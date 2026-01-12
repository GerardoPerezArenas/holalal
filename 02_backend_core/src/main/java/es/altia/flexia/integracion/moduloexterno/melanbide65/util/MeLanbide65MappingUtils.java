package es.altia.flexia.integracion.moduloexterno.melanbide65.util;

import es.altia.flexia.integracion.moduloexterno.melanbide65.vo.DesplegableAdmonLocalVO;
import java.sql.ResultSet;
import java.sql.SQLException;


public class MeLanbide65MappingUtils {

    private static MeLanbide65MappingUtils instance = null;

    private MeLanbide65MappingUtils() {
    }

    public static MeLanbide65MappingUtils getInstance() {
        if (instance == null) {
            synchronized (MeLanbide65MappingUtils.class) {
                instance = new MeLanbide65MappingUtils();
            }
        }
        return instance;
    }

    public Object map(ResultSet rs, Class clazz) throws Exception {
        return mapVers2(rs, clazz, true);
    }

    public Object mapVers2(ResultSet rs, Class clazz, boolean completo) throws Exception {
        if (clazz == DesplegableAdmonLocalVO.class) {
            return mapearDesplegableAdmonLocalVO(rs);
        }

        return null;
    }

    private Object mapearDesplegableAdmonLocalVO(ResultSet rs) throws SQLException {
        DesplegableAdmonLocalVO valoresDesplegable = new DesplegableAdmonLocalVO();
        valoresDesplegable.setDes_cod(rs.getString("DES_COD"));
        valoresDesplegable.setDes_val_cod(rs.getString("DES_VAL_COD"));
        valoresDesplegable.setDes_nom(rs.getString("DES_NOM"));
        return valoresDesplegable;
    }
}
