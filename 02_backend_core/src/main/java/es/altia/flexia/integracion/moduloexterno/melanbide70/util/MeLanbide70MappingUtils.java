/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide70.util;

import es.altia.flexia.integracion.moduloexterno.melanbide70.vo.DatosEconomicosExpVO;
import es.altia.flexia.integracion.moduloexterno.melanbide70.vo.ExpedienteVO;
import es.altia.flexia.integracion.moduloexterno.melanbide70.vo.TerceroVO;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.commons.lang.StringEscapeUtils;


/**
 *
 * @author davidg
 */
public class MeLanbide70MappingUtils {

    private static MeLanbide70MappingUtils instance = null;

    private MeLanbide70MappingUtils() {
    }

    public static MeLanbide70MappingUtils getInstance() {
        if (instance == null) {
            synchronized (MeLanbide70MappingUtils.class) {
                instance = new MeLanbide70MappingUtils();
            }
        }
        return instance;
    }

  /*Mapeo para el detalle de errores*/
        public Object map(ResultSet rs, Class clazz) throws Exception {
        if (clazz == ExpedienteVO.class) {
                return mapearExpedienteVO(rs);
        }
         if (clazz == DatosEconomicosExpVO.class) {
                return mapearDatosEconomicosVO(rs);
        }       

        return null;
    }

    private Object mapearExpedienteVO(ResultSet rs) throws SQLException {
        ExpedienteVO expediente=new ExpedienteVO();
        TerceroVO tercero=new TerceroVO();
        tercero.setTSexoTercero(rs.getString("sexo"));
        tercero.setTFecNacimiento(rs.getDate("fecnacimiento"));
        expediente.setTercero(tercero);
        expediente.setFecPresentacion(rs.getDate("fecPresentacion"));
        expediente.setCifCBSC(rs.getString("cifCBSC"));
        
        return expediente;
    }

    private Object mapearDatosEconomicosVO(ResultSet rs) throws SQLException {
        DatosEconomicosExpVO datosEcon=new DatosEconomicosExpVO();
        datosEcon.setImporteSubvencion(rs.getInt("SUB_IMPORTE"));
//        datosEcon.setPorcentajePrimerPago(rs.getDouble("PLA_PORCENTAJE"));
        
        return datosEcon;
    }
}
