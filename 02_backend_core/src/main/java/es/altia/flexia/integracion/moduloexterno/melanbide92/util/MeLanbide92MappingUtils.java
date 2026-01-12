package es.altia.flexia.integracion.moduloexterno.melanbide92.util;

import es.altia.flexia.integracion.moduloexterno.melanbide92.vo.ProcedimientoLireiVO;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author kepa
 */
public class MeLanbide92MappingUtils {
    
    public ProcedimientoLireiVO mapearProcedimiento(ResultSet rs) throws SQLException {
        ProcedimientoLireiVO procedimiento = new ProcedimientoLireiVO();
        procedimiento.setCodProcedimiento(rs.getString("PROCEDIMIENTO") != null && !rs.getString("PROCEDIMIENTO").isEmpty() ? rs.getString("PROCEDIMIENTO") : "");
        procedimiento.setRequerimiento(rs.getInt("REQUERIMIENTO") != 0 ? rs.getInt("REQUERIMIENTO") : null);
        procedimiento.setResolucion(rs.getInt("RESOLUCION") != 0 ? rs.getInt("RESOLUCION") : null);
        procedimiento.setAcuseRes(rs.getInt("ACUSE_RES") != 0 ? rs.getInt("ACUSE_RES") : null);
        procedimiento.setPago(rs.getInt("PAGO") != 0 ? rs.getInt("PAGO") : null);
        procedimiento.setEjecutiva(rs.getInt("EJECUTIVA") != 0 ? rs.getInt("EJECUTIVA") : null);
        procedimiento.setFracciona(rs.getInt("FRACCIONAMIENTO") != 0 ? rs.getInt("FRACCIONAMIENTO") : null);
        procedimiento.setAnulacion(rs.getInt("ANULACION") != 0 ? rs.getInt("ANULACION") : null);
        procedimiento.setSuspension(rs.getInt("SUSPENSION") != 0 ? rs.getInt("SUSPENSION") : null);
        procedimiento.setFinRama(rs.getInt("FINALIZACION") != 0 ? rs.getInt("FINALIZACION") : null);
        procedimiento.setCierreEspera(rs.getInt("CIERRE_ESP") != 0 ? rs.getInt("CIERRE_ESP") : null);
        return procedimiento;
    }
}
