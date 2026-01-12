/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide51.util;

import es.altia.flexia.integracion.moduloexterno.melanbide51.vo.ControlAccesoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide51.vo.DesplegableAdmonLocalVO;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author davidg
 */
public class MeLanbide51MappingUtils {
    
    private static MeLanbide51MappingUtils instance = null;

    private MeLanbide51MappingUtils() {
    }

    public static MeLanbide51MappingUtils getInstance() {
        if (instance == null) {
            synchronized (MeLanbide51MappingUtils.class) {
                instance = new MeLanbide51MappingUtils();
            }
        }
        return instance;
    }

    public Object map(ResultSet rs, Class clazz) throws Exception {
        if (clazz == ControlAccesoVO.class) {
            return mapearControlAccesoVO(rs);
        }else if(clazz == DesplegableAdmonLocalVO.class) {
            return mapearDesplegableAdmonLocalVO(rs);
        } 
        
        return null;
    }

    private Object mapearControlAccesoVO(ResultSet rs) throws SQLException {
        ControlAccesoVO controlAcceso = new ControlAccesoVO();
        
        controlAcceso.setId(rs.getInt("ID"));
        if (rs.wasNull()) {
            controlAcceso.setId(null);
        }
        controlAcceso.setFecha(rs.getDate("M51CA_FECHA"));
        controlAcceso.setNo_tarjeta(rs.getString("M51CA_NUMTAR"));
        controlAcceso.setNif_Dni(rs.getString("M51CA_DNI_CIF"));
        controlAcceso.setNombre(rs.getString("M51CA_NOMBRE"));
        controlAcceso.setApellido1(rs.getString("M51CA_APE1"));
        controlAcceso.setApellido2(rs.getString("M51CA_APE2"));
        controlAcceso.setTelefono(rs.getString("M51CA_TEL"));
        controlAcceso.setEmpresa_entidad(rs.getString("M51CA_EMPENT"));
        controlAcceso.setServicio_visitado(rs.getString("M51CA_SERVVIS"));
        controlAcceso.setPersona_contacto(rs.getString("M51CA_PERSCONT"));
        controlAcceso.setCod_mot_visita(rs.getString("M51CA_MOTVIS"));
        controlAcceso.setHora_entrada(rs.getTime("M51CA_HORENT"));
        controlAcceso.setHora_salida(rs.getTime("M51CA_HORSAL"));
        controlAcceso.setObservaciones(rs.getString("M51CA_OBSER"));
        controlAcceso.setFechaIV(rs.getDate("M51CA_FECHAIV"));
        controlAcceso.setFechaFV(rs.getDate("M51CA_FECHAFV"));
        
        return  controlAcceso;
    }
    
    private Object mapearDesplegableAdmonLocalVO(ResultSet rs) throws SQLException {
        DesplegableAdmonLocalVO valoresDesplegable = new DesplegableAdmonLocalVO();
        valoresDesplegable.setDes_cod(rs.getString("DES_COD"));
        valoresDesplegable.setDes_val_cod(rs.getString("DES_VAL_COD"));
        valoresDesplegable.setDes_nom(rs.getString("DES_NOM"));
        return valoresDesplegable;
    }
    
}
