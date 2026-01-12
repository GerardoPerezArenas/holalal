/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide62.util;

import es.altia.flexia.integracion.moduloexterno.melanbide62.vo.ExpedienteVO;
import es.altia.flexia.integracion.moduloexterno.melanbide62.vo.TerceroVO;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
//import org.apache.commons.lang.StringEscapeUtils;


/**
 *
 * @author davidg
 */
public class MeLanbide62MappingUtils {

    private static MeLanbide62MappingUtils instance = null;

    private MeLanbide62MappingUtils() {
    }

    public static MeLanbide62MappingUtils getInstance() {
        if (instance == null) {
            synchronized (MeLanbide62MappingUtils.class) {
                instance = new MeLanbide62MappingUtils();
            }
        }
        return instance;
    }

  /*Mapeo para el detalle de errores*/
        public Object map(ResultSet rs, Class clazz) throws Exception {
        
         if (clazz == ExpedienteVO.class) {
                return mapearExpedienteVO(rs);
        }
        return null;
    }

    private Object mapearExpedienteVO(ResultSet rs) throws SQLException {
        ExpedienteVO expediente=new ExpedienteVO();
        TerceroVO tercero=new TerceroVO();        
        tercero.setTFecNacimiento(rs.getDate("FECNACIMIENTO"));
        expediente.setTercero(tercero);
        expediente.setFecPresentacion(rs.getDate("FECPRESENTACION"));     
        return expediente;
    }    
   

}
