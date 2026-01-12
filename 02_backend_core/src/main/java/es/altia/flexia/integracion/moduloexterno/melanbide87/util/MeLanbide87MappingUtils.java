package es.altia.flexia.integracion.moduloexterno.melanbide87.util;

import es.altia.flexia.integracion.moduloexterno.melanbide87.vo.DesplegableAdmonLocalVO;
import es.altia.flexia.integracion.moduloexterno.melanbide87.vo.InstalacionVO;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MeLanbide87MappingUtils {

    private static MeLanbide87MappingUtils instance = null;

    private MeLanbide87MappingUtils() {
    }

    public static MeLanbide87MappingUtils getInstance() {
        if (instance == null) {
            synchronized (MeLanbide87MappingUtils.class) {
                instance = new MeLanbide87MappingUtils();
            }
        }
        return instance;
    }

    public Object map(ResultSet rs, Class clazz) throws Exception {
        if (clazz == InstalacionVO.class) {
            return mapearInstalacion(rs);
        } else if (clazz == DesplegableAdmonLocalVO.class) {
            return mapearDesplegableAdmonLocalVO(rs);
        }
        return null;
    }

    private Object mapearInstalacion(ResultSet rs) throws SQLException {
        InstalacionVO persona = new InstalacionVO();

        persona.setId(rs.getInt("ID"));
        if (rs.wasNull()) {
            persona.setId(null);
        }
        persona.setNumExp(rs.getString("NUM_EXP"));
        persona.setTipoInst(rs.getString("TIPINST"));
        persona.setMunicipio(rs.getString("INSTMUNICIPIO"));
        persona.setLocalidad(rs.getString("INSTLOCALIDAD"));
        persona.setDireccion(rs.getString("INSTDIRECCION"));
        persona.setCodPost(rs.getInt("INSTCP"));
        
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
