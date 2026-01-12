/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide18.util;

import es.altia.flexia.integracion.moduloexterno.melanbide18.vo.FilaDeudaFraccVO;
import es.altia.flexia.integracion.moduloexterno.melanbide18.vo.DeudaZorkuVO;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

/**
 *
 * @author santiagoc
 */
public class MeLanbide18MappingUtils {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    private static MeLanbide18MappingUtils instance = null;

    private MeLanbide18MappingUtils() {

    }

    public static MeLanbide18MappingUtils getInstance() {
        if (instance == null) {
            synchronized (MeLanbide18MappingUtils.class) {
                instance = new MeLanbide18MappingUtils();
            }
        }
        return instance;
    }

    public Object map(ResultSet rs, Class clazz) throws Exception {
        if (clazz == DeudaZorkuVO.class) {
            return mapearDeudaVO(rs);
        } else if (clazz == FilaDeudaFraccVO.class) {
            return mapearDeudaFraccVO(rs);
        }

        return null;
    }

    private Object mapearDeudaVO(ResultSet rs) throws SQLException {
        DeudaZorkuVO deuda = new DeudaZorkuVO();
        deuda.setNumLiquidacion(rs.getLong("NUM_LIQUIDACION"));
        deuda.setNumDocumento(rs.getString("NUM_DOCUMENTO"));
        deuda.setExpediente(rs.getString("EXPEDIENTE"));
        deuda.setImporteDeuda(rs.getDouble("IMPORTE_DEUDA"));
        deuda.setImportePendiente(rs.getDouble("IMPORTE_PENDIENTE"));
        deuda.setImporteCobrado(rs.getDouble("IMPORTE_COBRADO"));
        deuda.setCodTipoPago(rs.getInt("COD_TIPO_PAGO"));
        deuda.setTipoPago(rs.getString("TIPO_PAGO"));
        deuda.setCodEstadoDeuda(rs.getInt("COD_ESTADO_DEUDA"));
        deuda.setEstadoDeuda(rs.getString("ESTADO_DEUDA"));
        deuda.setPeriodo(rs.getString("PERIODO"));
        if (rs.getDate("FECHA_LIMITE_PAGO") != null) {
            deuda.setFechaLimitePago(rs.getDate("FECHA_LIMITE_PAGO"));
            deuda.setFechaLimitePagoStr(dateFormat.format(rs.getDate("FECHA_LIMITE_PAGO")));
        }

        return deuda;
    }
    
    private Object mapearDeudaFraccVO(ResultSet rs) throws SQLException {
        FilaDeudaFraccVO filaDeudaFraccVO = new FilaDeudaFraccVO();
        
        filaDeudaFraccVO.setId(rs.getInt("ID"));
        filaDeudaFraccVO.setNumExp(rs.getString("NUM_EXP"));
        filaDeudaFraccVO.setNumExpDeuda(rs.getString("NUM_EXP_DEUDA"));
        filaDeudaFraccVO.setDeudaImporte(rs.getDouble("DEUDA_IMPORTE"));
        filaDeudaFraccVO.setNumLiquidacion(rs.getLong("NUM_LIQUIDACION"));
        
        return filaDeudaFraccVO;
    }
}
