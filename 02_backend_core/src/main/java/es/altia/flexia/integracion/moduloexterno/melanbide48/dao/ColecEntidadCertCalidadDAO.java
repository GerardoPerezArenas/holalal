package es.altia.flexia.integracion.moduloexterno.melanbide48.dao;

import es.altia.flexia.integracion.moduloexterno.melanbide48.util.MeLanbide48MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide48.vo.ColecEntidadCertCalidad;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ColecEntidadCertCalidadDAO {

    private static final Logger log = LogManager.getLogger(ColecEntidadCertCalidadDAO.class);
    private final SimpleDateFormat formatFechaLog = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    public ColecEntidadCertCalidad getColecEntidadCertCalidadById(Integer id, Connection con) throws SQLException, Exception{
        log.info(" getColecEntidadCertCalidadById - Begin " + id + formatFechaLog.format(new Date()));
        ColecEntidadCertCalidad resultado = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String query = "select * "
                    + " from COLEC_ENTIDAD_CERT_CALIDAD "
                    + " where "
                    + " id=? ";
            log.info("sql = " + query);
            ps = con.prepareStatement(query);
            ps.setInt(1, id);
            log.info("params = " + id);
            rs = ps.executeQuery();
            while (rs.next()) {
                resultado = (ColecEntidadCertCalidad) MeLanbide48MappingUtils.getInstance().map(rs, ColecEntidadCertCalidad.class);
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
        log.info(" getColecEntidadCertCalidadById - End " + id + formatFechaLog.format(new Date()));
        return resultado;
    }

    public List<ColecEntidadCertCalidad> getColecEntidadCertCalidadByNumExp(String numExpediente, Connection con) throws SQLException, Exception{
        log.info(" getColecEntidadCertCalidadByNumExp - Begin " + " - " + numExpediente + " " + formatFechaLog.format(new Date()));
        List<ColecEntidadCertCalidad> resultado = new ArrayList<ColecEntidadCertCalidad>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String query = "select * "
                    + " from COLEC_ENTIDAD_CERT_CALIDAD " +
                    " where num_exp=?"
                    ;
            log.info("sql = " + query);
            ps = con.prepareStatement(query);
            ps.setString(1, numExpediente);
            log.info("params = " + numExpediente);
            rs = ps.executeQuery();
            while (rs.next()) {
                resultado.add((ColecEntidadCertCalidad) MeLanbide48MappingUtils.getInstance().map(rs, ColecEntidadCertCalidad.class));
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
        log.info(" getColecEntidadCertCalidadByNumExp - End " + formatFechaLog.format(new Date()));
        return resultado;
    }

    public List<ColecEntidadCertCalidad> getListaColecEntidadCertCalidadByCodEntidad(Integer codEntidad, Connection con) throws Exception {
        log.info(" getListaColecEntidadCertCalidadByCodEntidad - Begin " + " - " + codEntidad + " " + formatFechaLog.format(new Date()));
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<ColecEntidadCertCalidad> retList = new ArrayList<ColecEntidadCertCalidad>();
        try {
            String query = null;
            query = "select * "
                    + " from COLEC_ENTIDAD_CERT_CALIDAD " +
                    " where id_entidad=?"
            ;
            log.info("sql = " + query);
            ps = con.prepareStatement(query);
            ps.setInt(1, codEntidad);
            log.info("params = " + codEntidad);
            rs = ps.executeQuery();
            while (rs.next()) {
                retList.add((ColecEntidadCertCalidad) MeLanbide48MappingUtils.getInstance().map(rs, ColecEntidadCertCalidad.class));
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

    public ColecEntidadCertCalidad getColecEntidadCertCalidadByIdEntidadAndIdCertificado(Integer idEntidad, String idCertificado, Connection con) throws SQLException, Exception{
        log.info(" getColecEntidadCertCalidadByIdEntidadAndIdCertificado - Begin " + idEntidad + " , "+  idCertificado + " " +formatFechaLog.format(new Date()));
        ColecEntidadCertCalidad resultado = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String query = "select * "
                    + " from COLEC_ENTIDAD_CERT_CALIDAD "
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
                resultado = (ColecEntidadCertCalidad) MeLanbide48MappingUtils.getInstance().map(rs, ColecEntidadCertCalidad.class);
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
        log.info(" getColecEntidadCertCalidadByIdEntidadAndIdCertificado - End " + idEntidad + " , "+  idCertificado + " " + formatFechaLog.format(new Date()));
        return resultado;
    }

    public boolean insertColecEntidadCertCalidad(ColecEntidadCertCalidad colecEntidadCertCalidad, Connection con) throws SQLException, Exception{
        log.info(" insertColecEntidadCertCalidad - Begin " +formatFechaLog.format(new Date()));
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            if(colecEntidadCertCalidad==null) {
                log.error("No se puede insertar Datos de Certificados de calidad para la Entidad - Objeto recibido a null");
                return false;
            }
            String query = "insert into COLEC_ENTIDAD_CERT_CALIDAD (ID,NUM_EXP,ID_ENTIDAD,ID_CERTIFICADO,VALOR_SN_SOLICITUD,VALOR_SN_VALIDADO) "
                    + " values "
                    + " ("
                    + " SEQ_COLEC_ENTIDAD_CERT_CALID.nextval"
                    + ",?,?,?,?,?)"
                    ;
            log.info("sql = " + query);
            ps = con.prepareStatement(query);
            int contParams = 1;
            ps.setString(contParams++, colecEntidadCertCalidad.getNumExp());
            ps.setInt(contParams++, colecEntidadCertCalidad.getIdEntidad());
            ps.setString(contParams++, colecEntidadCertCalidad.getIdCertificado());
            if( colecEntidadCertCalidad.getValorSNSolicitud()!=null)
                ps.setInt(contParams++, colecEntidadCertCalidad.getValorSNSolicitud());
            else {
                ps.setNull(contParams++, Types.NULL);
            }
            if (colecEntidadCertCalidad.getValorSNValidado()!=null)
                ps.setInt(contParams++, colecEntidadCertCalidad.getValorSNValidado());
            else
                ps.setNull(contParams++, Types.NULL);
            log.info("Datos a guardar => " + colecEntidadCertCalidad.toString());
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
            log.info(" insertColecEntidadCertCalidad - End  " + formatFechaLog.format(new Date()));
        }
    }

    public boolean updateColecEntidadCertCalidad(ColecEntidadCertCalidad colecEntidadCertCalidad, Connection con) throws SQLException, Exception{
        log.info(" updateColecEntidadCertCalidad - Begin " +formatFechaLog.format(new Date()));
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            if(colecEntidadCertCalidad==null) {
                log.error("No se puede actualizar Datos de Certificados de calidad para la Entidad - Objeto recibido a null");
                return false;
            }
            String query = "update COLEC_ENTIDAD_CERT_CALIDAD set " +
                    " VALOR_SN_SOLICITUD=? " +
                    ",VALOR_SN_VALIDADO=? "
                    + " where  "
                    + " id=? "
                    ;
            log.info("sql = " + query);
            ps = con.prepareStatement(query);
            int contParams = 1;
            if( colecEntidadCertCalidad.getValorSNSolicitud() != null)
                ps.setInt(contParams++, colecEntidadCertCalidad.getValorSNSolicitud());
            else
                ps.setNull(contParams++, Types.NULL);
            if(colecEntidadCertCalidad.getValorSNValidado() != null)
                ps.setInt(contParams++, colecEntidadCertCalidad.getValorSNValidado());
            else
                ps.setNull(contParams++, Types.NULL);
            ps.setInt(contParams++, colecEntidadCertCalidad.getId());
            log.info("Datos a Actualizar => " + colecEntidadCertCalidad.toString());
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
            log.info(" updateColecEntidadCertCalidad - End  " + formatFechaLog.format(new Date()));
        }

    }

    public boolean deleteColecEntidadCertCalidadById(Integer id, Connection con) throws SQLException, Exception{
        log.info(" deleteColecEntidadCertCalidadById - Begin " +formatFechaLog.format(new Date()));
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            if(id==null) {
                log.error("No se puede eliminar Datos de Certificados de calidad para la Entidad - Id recibido a null");
                return false;
            }
            String query = "delete from  COLEC_ENTIDAD_CERT_CALIDAD "
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
            log.info(" deleteColecEntidadCertCalidadById - End  " + formatFechaLog.format(new Date()));
        }

    }

    public boolean deleteColecEntidadCertCalidadByIdEntidad(Integer idEntidad, Connection con) throws SQLException, Exception{
        log.info(" deleteColecEntidadCertCalidadByIdEntidad - Begin " +formatFechaLog.format(new Date()));
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            if(idEntidad==null) {
                log.error("No se puede eliminar Datos de Certificados de calidad para la Entidad - IdEntidad recibido a null");
                return false;
            }
            String query = "delete from  COLEC_ENTIDAD_CERT_CALIDAD "
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
            log.info(" deleteColecEntidadCertCalidadByIdEntidad - End  " + formatFechaLog.format(new Date()));
        }

    }

    public boolean deleteColecEntidadCertCalidadByNumExp(String numExp, Connection con) throws SQLException, Exception{
        log.info(" deleteColecEntidadCertCalidadByNumExp - Begin " +formatFechaLog.format(new Date()));
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            if(numExp==null) {
                log.error("No se puede eliminar Datos de Certificados de calidad para la Entidad - numExp recibido a null");
                return false;
            }
            String query = "delete from  COLEC_ENTIDAD_CERT_CALIDAD "
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
            log.info(" deleteColecEntidadCertCalidadByNumExp - End  " + formatFechaLog.format(new Date()));
        }

    }

}
