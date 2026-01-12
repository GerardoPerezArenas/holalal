package es.altia.flexia.integracion.moduloexterno.melanbide13.util;

import es.altia.flexia.integracion.moduloexterno.melanbide13.vo.ListaExpedientesVO;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MeLanbide13MappingUtils {

    private static MeLanbide13MappingUtils instance = null;

    private MeLanbide13MappingUtils() {
    }

    public static MeLanbide13MappingUtils getInstance() {
        if (instance == null) {
            synchronized (MeLanbide13MappingUtils.class) {
                instance = new MeLanbide13MappingUtils();
            }
        }
        return instance;
    }

    public Object map(ResultSet rs, Class clazz) throws Exception {
        if (clazz == ListaExpedientesVO.class) {
            return mapearListaExpedientesVO(rs);
        }
        return null;
    }

    private Object mapearListaExpedientesVO(ResultSet rs) throws SQLException {
        ListaExpedientesVO listaExpedientes = new ListaExpedientesVO();
        listaExpedientes.setNumExp(rs.getString("EXPEDIENTE"));
        listaExpedientes.setMes(rs.getString("MES") != null && !rs.getString("MES").isEmpty() ? rs.getString("MES") : "");
        listaExpedientes.setTerritorio(rs.getString("TERRITORIO_HISTORICO") != null && !rs.getString("TERRITORIO_HISTORICO").isEmpty() ? rs.getString("TERRITORIO_HISTORICO") : "");
        listaExpedientes.setImporte(rs.getDouble("IMPORTE") != 0 ? rs.getDouble("IMPORTE") : 0);

        return listaExpedientes;
    }

}
