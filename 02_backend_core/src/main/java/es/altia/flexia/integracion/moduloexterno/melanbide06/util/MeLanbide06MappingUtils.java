package es.altia.flexia.integracion.moduloexterno.melanbide06.util;

import es.altia.flexia.integracion.moduloexterno.melanbide06.vo.SelectItem;
import java.sql.ResultSet;

/**
 *
 * @author jaime.hermoso
 */
public class MeLanbide06MappingUtils {

    private static MeLanbide06MappingUtils instance = null;

    private MeLanbide06MappingUtils() {

    }

    public static MeLanbide06MappingUtils getInstance() {
        if (instance == null) {
            synchronized (MeLanbide06MappingUtils.class) {
                instance = new MeLanbide06MappingUtils();
            }
        }
        return instance;
    }

    public Object map(ResultSet rs, Class clazz) throws Exception {
        if (clazz == SelectItem.class) {
            return mapearSelectItem(rs);
        }
        return null;
    }

    private SelectItem mapearSelectItem(ResultSet rs) throws Exception {
        SelectItem si = new SelectItem();
        si.setCodigo(rs.getString("CODIGO"));
        si.setDescripcion(rs.getString("DESCRIPCION"));
        return si;
    }
}
