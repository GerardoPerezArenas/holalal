/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide71.util;

import es.altia.flexia.integracion.moduloexterno.melanbide71.vo.DatosEconomicosExpVO;
import es.altia.flexia.integracion.moduloexterno.melanbide71.vo.ExpedienteVO;
import es.altia.flexia.integracion.moduloexterno.melanbide71.vo.TerceroVO;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 *
 * @author davidg
 */
public class MeLanbide71MappingUtils {

    private static MeLanbide71MappingUtils instance = null;

    private MeLanbide71MappingUtils() {
    }

    public static MeLanbide71MappingUtils getInstance() {
        if (instance == null) {
            synchronized (MeLanbide71MappingUtils.class) {
                instance = new MeLanbide71MappingUtils();
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
//        expediente.setCifCBSC(rs.getString("cifCBSC"));
        
        return expediente;
    }

    private Object mapearDatosEconomicosVO(ResultSet rs) throws SQLException {
        DatosEconomicosExpVO datosEcon=new DatosEconomicosExpVO();
        datosEcon.setImporteSubvencion(rs.getInt("SUB_IMPORTE"));
//        datosEcon.setPorcentajePrimerPago(rs.getDouble("PLA_PORCENTAJE"));
        
        return datosEcon;
    }
}
