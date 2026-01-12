package es.altia.flexia.integracion.moduloexterno.melanbide33.util;

import es.altia.flexia.integracion.moduloexterno.melanbide33.vo.DesplegableAdmonLocalVO;
import es.altia.flexia.integracion.moduloexterno.melanbide33.vo.FilaMinimisVO;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class MeLanbide33MappingUtils {

    private static MeLanbide33MappingUtils instance = null;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    private MeLanbide33MappingUtils() {
    }

    public static MeLanbide33MappingUtils getInstance() {
        if (instance == null) {
            synchronized (MeLanbide33MappingUtils.class) {
                instance = new MeLanbide33MappingUtils();
            }
        }
        return instance;
    }

    public Object map(ResultSet rs, Class clazz) throws Exception {
        if (clazz == FilaMinimisVO.class) {
            return mapearMinimisVO(rs);
        } else if (clazz == DesplegableAdmonLocalVO.class) {
            return mapearDesplegableAdmonLocalVO(rs);
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
