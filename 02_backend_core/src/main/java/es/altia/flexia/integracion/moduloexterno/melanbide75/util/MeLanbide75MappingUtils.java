
package es.altia.flexia.integracion.moduloexterno.melanbide75.util;

import es.altia.flexia.integracion.moduloexterno.melanbide75.vo.ControlAccesoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide75.vo.DesplegableAdmonLocalVO;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MeLanbide75MappingUtils {

    private static MeLanbide75MappingUtils instance = null;

    private MeLanbide75MappingUtils() {
    }

    public static MeLanbide75MappingUtils getInstance() {
        if (instance == null) {
            synchronized (MeLanbide75MappingUtils.class) {
                instance = new MeLanbide75MappingUtils();
            }
        }
        return instance;
    }

    public Object map(ResultSet rs, Class clazz) throws Exception {
        return mapVers2(rs, clazz, true);
    }

    public Object mapVers2(ResultSet rs, Class clazz, boolean completo) throws Exception {
        if (clazz == ControlAccesoVO.class) {
            return mapearControlAccesoVO(rs);
        } else if (clazz == DesplegableAdmonLocalVO.class) {
            return mapearDesplegableAdmonLocalVO(rs);
        }

        return null;
    }

    private Object mapearControlAccesoVO(ResultSet rs) throws SQLException {
        ControlAccesoVO controlAcceso = new ControlAccesoVO();

        controlAcceso.setNumExp(rs.getString("NUM_EXP"));
        
        controlAcceso.setId(rs.getInt("ID"));
        if (rs.wasNull()) {
            controlAcceso.setId(null);
        }
        
        //controlAcceso.setCnae(rs.getString("CNAE"));
        
        controlAcceso.setPuesto(rs.getString("DENOM_PUESTO"));
        
        controlAcceso.setNombre(rs.getString("NOMBRE_APELLIDOS"));
        
        controlAcceso.setNif(rs.getString("NIF"));
        
        Double aux1 = new Double(rs.getDouble("POR_DISC"));
        if (aux1 != 0) {
            controlAcceso.setPorDisc(aux1.doubleValue());
        }
        
        controlAcceso.setTipoCon(rs.getString("TIPO_CONTRATO"));
        
        if (rs.getDate("CONTRATO_DESDE") != null) {
            controlAcceso.setConDesde(rs.getDate("CONTRATO_DESDE"));
        }
        
        if (rs.getDate("CONTRATO_HASTA") != null) {
            controlAcceso.setConHasta(rs.getDate("CONTRATO_HASTA"));
        }
        
        Integer aux2 = new Integer(rs.getInt("TOTAL_DIAS"));
        if (aux2 != 0) {
            controlAcceso.setTotalDias(aux2.intValue());
        }
      
//        
        return controlAcceso;
    }

    private Object mapearDesplegableAdmonLocalVO(ResultSet rs) throws SQLException {
        DesplegableAdmonLocalVO valoresDesplegable = new DesplegableAdmonLocalVO();
        valoresDesplegable.setDes_cod(rs.getString("DES_COD"));
        valoresDesplegable.setDes_val_cod(rs.getString("DES_VAL_COD"));
        valoresDesplegable.setDes_nom(rs.getString("DES_NOM"));
        return valoresDesplegable;
    }

}
