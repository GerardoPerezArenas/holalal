package es.altia.flexia.integracion.moduloexterno.melanbide47.dao;

import es.altia.flexia.integracion.moduloexterno.melanbide47.util.MeLanbide47MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide47.vo.orientacion.OriEntidadCertCalidad;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OriEntidadCertCalidadDAO {

    private static final Logger log = LogManager.getLogger(OriEntidadCertCalidadDAO.class);
    private final SimpleDateFormat formatFechaLog = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    public OriEntidadCertCalidad getOriEntidadCertCalidadById(Integer id, Connection con) throws SQLException, Exception{
        log.info(" getOriEntidadCertCalidadById - Begin " + id + formatFechaLog.format(new Date()));
        OriEntidadCertCalidad resultado = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String query = "select * "
                    + " from ori_ENTIDAD_CERT_CALIDAD "
                    + " where "
                    + " id=? ";
            log.info("sql = " + query);
            ps = con.prepareStatement(query);
            ps.setInt(1, id);
            log.info("params = " + id);
            rs = ps.executeQuery();
            while (rs.next()) {
                resultado = (OriEntidadCertCalidad) MeLanbide47MappingUtils.getInstance().map(rs, OriEntidadCertCalidad.class);
            }
        } catch (SQLException e) {
            log.error("Se ha producido recuperando datos Certificados Calidad", e);
            throw e;
        } catch (Exception e) {
            log.error("Se ha producido recuperando datos Certificados Calidad", e);
            throw e;
        } finally {
            log.debug("Procedemos a cerrar el resultset");
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        log.info(" getOriEntidadCertCalidadById - End " + id + formatFechaLog.format(new Date()));
        return resultado;
    }

    public List<OriEntidadCertCalidad> getOriEntidadCertCalidadByNumExp(String numExpediente, Connection con) throws SQLException, Exception{
        log.info(" getOriEntidadCertCalidadByNumExp - Begin " + " - " + numExpediente + " " + formatFechaLog.format(new Date()));
        List<OriEntidadCertCalidad> resultado = new ArrayList<OriEntidadCertCalidad>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String query = "select * "
                    + " from ori_ENTIDAD_CERT_CALIDAD " +
                    " where num_exp=?"
                    ;
            log.info("sql = " + query);
            ps = con.prepareStatement(query);
            ps.setString(1, numExpediente);
            log.info("params = " + numExpediente);
            rs = ps.executeQuery();
            while (rs.next()) {
                resultado.add((OriEntidadCertCalidad) MeLanbide47MappingUtils.getInstance().map(rs, OriEntidadCertCalidad.class));
            }
        } catch (SQLException e) {
            log.error("Se ha producido recuperando Lista Certificados calidad expediente ", e);
            throw e;
        } catch (Exception e) {
            log.error("Se ha producido recuperando lista certificados calidad Expediente ", e);
            throw e;
        } finally {
            log.debug("Procedemos a cerrar el resultset");
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        log.info(" getOriEntidadCertCalidadByNumExp - End " + formatFechaLog.format(new Date()));
        return resultado;
    }

    public List<OriEntidadCertCalidad> getListaOriEntidadCertCalidadByCodEntidad(Integer codEntidad, Connection con) throws Exception {
        log.info(" getListaOriEntidadCertCalidadByCodEntidad - Begin " + " - " + codEntidad + " " + formatFechaLog.format(new Date()));
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<OriEntidadCertCalidad> retList = new ArrayList<OriEntidadCertCalidad>();
        try {
            String query = null;
            query = "select * "
                    + " from ori_ENTIDAD_CERT_CALIDAD " +
                    " where id_entidad=?"
            ;
            log.info("sql = " + query);
            ps = con.prepareStatement(query);
            ps.setInt(1, codEntidad);
            log.info("params = " + codEntidad);
            rs = ps.executeQuery();
            while (rs.next()) {
                retList.add((OriEntidadCertCalidad) MeLanbide47MappingUtils.getInstance().map(rs, OriEntidadCertCalidad.class));
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando lista Certificados Calidad Entidad ", ex);
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return retList;
    }

    public OriEntidadCertCalidad getOriEntidadCertCalidadByIdEntidadAndIdCertificado(Integer idEntidad, String idCertificado, Connection con) throws SQLException, Exception{
        log.info(" getOriEntidadCertCalidadByIdEntidadAndIdCertificado - Begin " + idEntidad + " , "+  idCertificado + " " +formatFechaLog.format(new Date()));
        OriEntidadCertCalidad resultado = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String query = "select * "
                    + " from ori_ENTIDAD_CERT_CALIDAD "
                    + " where "
                    + " id_entidad=? "
                    + " and id_certificado=? "
                    ;
            log.info("sql = " + query);
            ps = con.prepareStatement(query);
            int contParams = 1;
            ps.setInt(contParams++, idEntidad);
            ps.setString(contParams++, idCertificado);
            log.info("params = " + idEntidad
                    + ", " + idCertificado
            );
            rs = ps.executeQuery();
            while (rs.next()) {
                resultado = (OriEntidadCertCalidad) MeLanbide47MappingUtils.getInstance().map(rs, OriEntidadCertCalidad.class);
            }
        } catch (SQLException e) {
            log.error("Se ha producido error recuperando datos Certificados Calidad Entidad-IdCertificado ", e);
            throw e;
        } catch (Exception e) {
            log.error("Se ha producido erro recuperando datos Certificados Calidad Entidad-IdCertificado ", e);
            throw e;
        } finally {
            log.debug("Procedemos a cerrar el resultset");
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        log.info(" getOriEntidadCertCalidadByIdEntidadAndIdCertificado - End " + idEntidad + " , "+  idCertificado + " " + formatFechaLog.format(new Date()));
        return resultado;
    }

    public boolean insertOriEntidadCertCalidad(OriEntidadCertCalidad oriEntidadCertCalidad, Connection con) throws SQLException, Exception{
        log.info(" insertOriEntidadCertCalidad - Begin " +formatFechaLog.format(new Date()));
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            if(oriEntidadCertCalidad ==null) {
                log.error("No se puede insertar Datos de Certificados de calidad para la Entidad - Objeto recibido a null");
                return false;
            }
            String query = "insert into ori_ENTIDAD_CERT_CALIDAD (ID,NUM_EXP,ID_ENTIDAD,ID_CERTIFICADO,VALOR_SN_SOLICITUD,VALOR_SN_VALIDADO) "
                    + " values "
                    + " ("
                    + " SEQ_ori_ENTIDAD_CERT_CALID.nextval"
                    + ",?,?,?,?,?)"
                    ;
            log.info("sql = " + query);
            ps = con.prepareStatement(query);
            int contParams = 1;
            ps.setString(contParams++, oriEntidadCertCalidad.getNumExp());
            ps.setInt(contParams++, oriEntidadCertCalidad.getIdEntidad());
            ps.setString(contParams++, oriEntidadCertCalidad.getIdCertificado());
            if( oriEntidadCertCalidad.getValorSNSolicitud()!=null)
                ps.setInt(contParams++, oriEntidadCertCalidad.getValorSNSolicitud());
            else {
                ps.setNull(contParams++, Types.NULL);
            }
            if (oriEntidadCertCalidad.getValorSNValidado()!=null)
                ps.setInt(contParams++, oriEntidadCertCalidad.getValorSNValidado());
            else
                ps.setNull(contParams++, Types.NULL);
            log.info("Datos a guardar => " + oriEntidadCertCalidad.toString());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            log.error("Se ha producido error  insertando datos Certificados Calidad Entidad ", e);
            throw e;
        } catch (Exception e) {
            log.error("Se ha producido erro insertando datos Certificados Calidad Entidad ", e);
            throw e;
        } finally {
            log.debug("Procedemos a cerrar el resultset");
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
            log.info(" insertOriEntidadCertCalidad - End  " + formatFechaLog.format(new Date()));
        }
    }

    public boolean updateOriEntidadCertCalidad(OriEntidadCertCalidad oriEntidadCertCalidad, Connection con) throws SQLException, Exception{
        log.info(" updateOriEntidadCertCalidad - Begin " +formatFechaLog.format(new Date()));
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            if(oriEntidadCertCalidad ==null) {
                log.error("No se puede actualizar Datos de Certificados de calidad para la Entidad - Objeto recibido a null");
                return false;
            }
            String query = "update ori_ENTIDAD_CERT_CALIDAD set " +
                    " VALOR_SN_SOLICITUD=? " +
                    ",VALOR_SN_VALIDADO=? "
                    + " where  "
                    + " id=? "
                    ;
            log.info("sql = " + query);
            ps = con.prepareStatement(query);
            int contParams = 1;
            if( oriEntidadCertCalidad.getValorSNSolicitud() != null)
                ps.setInt(contParams++, oriEntidadCertCalidad.getValorSNSolicitud());
            else
                ps.setNull(contParams++, Types.NULL);
            if(oriEntidadCertCalidad.getValorSNValidado() != null)
                ps.setInt(contParams++, oriEntidadCertCalidad.getValorSNValidado());
            else
                ps.setNull(contParams++, Types.NULL);
            ps.setInt(contParams++, oriEntidadCertCalidad.getId());
            log.info("Datos a Actualizar => " + oriEntidadCertCalidad.toString());
            int resultadoExec  = ps.executeUpdate();
            log.info("filas Actualizadas => " + resultadoExec);
            return resultadoExec > 0;
        } catch (SQLException e) {
            log.error("Se ha producido error actualizando datos Certificados Calidad Entidad ", e);
            throw e;
        } catch (Exception e) {
            log.error("Se ha producido error actualizando datos Certificados Calidad Entidad ", e);
            throw e;
        } finally {
            log.debug("Procedemos a cerrar el resultset");
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
            log.info(" updateOriEntidadCertCalidad - End  " + formatFechaLog.format(new Date()));
        }

    }

    public boolean deleteOriEntidadCertCalidadById(Integer id, Connection con) throws SQLException, Exception{
        log.info(" deleteOriEntidadCertCalidadById - Begin " +formatFechaLog.format(new Date()));
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            if(id==null) {
                log.error("No se puede eliminar Datos de Certificados de calidad para la Entidad - Id recibido a null");
                return false;
            }
            String query = "delete from  ori_ENTIDAD_CERT_CALIDAD "
                    + " where  "
                    + " id=? "
                    ;
            log.info("sql = " + query);
            ps = con.prepareStatement(query);
            int contParams = 1;
            ps.setInt(contParams++, id);
            log.info("params => " + id);
            int resultadoExec  = ps.executeUpdate();
            log.info("filas Eliminadas => " + resultadoExec);
            return resultadoExec > 0;
        } catch (SQLException e) {
            log.error("Se ha producido error eliminando datos Certificados Calidad Entidad ", e);
            throw e;
        } catch (Exception e) {
            log.error("Se ha producido error eliminando datos Certificados Calidad Entidad ", e);
            throw e;
        } finally {
            log.debug("Procedemos a cerrar el resultset");
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
            log.info(" deleteOriEntidadCertCalidadById - End  " + formatFechaLog.format(new Date()));
        }

    }

    public boolean deleteOriEntidadCertCalidadByIdEntidad(Integer idEntidad, Connection con) throws SQLException, Exception{
        log.info(" deleteOriEntidadCertCalidadByIdEntidad - Begin " +formatFechaLog.format(new Date()));
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            if(idEntidad==null) {
                log.error("No se puede eliminar Datos de Certificados de calidad para la Entidad - IdEntidad recibido a null");
                return false;
            }
            String query = "delete from  ori_ENTIDAD_CERT_CALIDAD "
                    + " where  "
                    + " id_Entidad=? "
                    ;
            log.info("sql = " + query);
            ps = con.prepareStatement(query);
            int contParams = 1;
            ps.setInt(contParams++, idEntidad);
            log.info("params => " + idEntidad);
            int resultadoExec  = ps.executeUpdate();
            log.info("filas Eliminadas => " + resultadoExec);
            return resultadoExec > 0;
        } catch (SQLException e) {
            log.error("Se ha producido error eliminando datos Certificados Calidad Entidad ", e);
            throw e;
        } catch (Exception e) {
            log.error("Se ha producido error eliminando datos Certificados Calidad Entidad ", e);
            throw e;
        } finally {
            log.debug("Procedemos a cerrar el resultset");
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
            log.info(" deleteOriEntidadCertCalidadByIdEntidad - End  " + formatFechaLog.format(new Date()));
        }

    }

    public boolean deleteOriEntidadCertCalidadByNumExp(String numExp, Connection con) throws SQLException, Exception{
        log.info(" deleteOriEntidadCertCalidadByNumExp - Begin " +formatFechaLog.format(new Date()));
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            if(numExp==null) {
                log.error("No se puede eliminar Datos de Certificados de calidad para la Entidad - numExp recibido a null");
                return false;
            }
            String query = "delete from  ori_ENTIDAD_CERT_CALIDAD "
                    + " where  "
                    + " num_exp=? "
                    ;
            log.info("sql = " + query);
            ps = con.prepareStatement(query);
            int contParams = 1;
            ps.setString(contParams++, numExp);
            log.info("params => " + numExp);
            int resultadoExec  = ps.executeUpdate();
            log.info("filas Eliminadas => " + resultadoExec);
            return resultadoExec > 0;
        } catch (SQLException e) {
            log.error("Se ha producido error eliminando datos Certificados Calidad Entidad ", e);
            throw e;
        } catch (Exception e) {
            log.error("Se ha producido error eliminando datos Certificados Calidad Entidad ", e);
            throw e;
        } finally {
            log.debug("Procedemos a cerrar el resultset");
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
            log.info(" deleteOriEntidadCertCalidadByNumExp - End  " + formatFechaLog.format(new Date()));
        }

    }

}
