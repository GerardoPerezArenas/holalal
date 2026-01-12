package es.altia.flexia.integracion.moduloexterno.melanbide47.dao;

import es.altia.flexia.integracion.moduloexterno.melanbide47.util.MeLanbide47MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide47.vo.orientacion.OriCertCalidadPuntuacion;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OriCertCalidadPuntuacionDAO {

    private static final Logger log = LogManager.getLogger(OriCertCalidadPuntuacionDAO.class);
    private final SimpleDateFormat formatFechaLog = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    public OriCertCalidadPuntuacion getOriCertCalidadPuntuacionByIdConvocatoriaAndCodigo(int idConvocatoria, String codigo, Connection con) throws SQLException, Exception{
        log.info(" getOriCertCalidadPuntuacionByIdConvocatoriaAndCodigo - Begin " + codigo + formatFechaLog.format(new Date()));
        OriCertCalidadPuntuacion resultado = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String query = "select * "
                    + " from ori_cert_calidad_puntuacion "
                    + " where "
                    + " idConvocatoria=? "
                    + " and codigo=? ";
            log.info("sql = " + query);
            ps = con.prepareStatement(query);
            int contParams = 1;
            ps.setInt(contParams++, idConvocatoria);
            ps.setString(contParams++, codigo);
            log.info("params = " + idConvocatoria
                +", " + codigo);
            rs = ps.executeQuery();
            while (rs.next()) {
                resultado = (OriCertCalidadPuntuacion) MeLanbide47MappingUtils.getInstance().map(rs, OriCertCalidadPuntuacion.class);
            }
        } catch (SQLException e) {
            log.error("Se ha producido recuperando datos Certificados Calidad ", e);
            throw e;
        } catch (Exception e) {
            log.error("Se ha producido recuperando datos Certificados Calidad ", e);
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
        log.info(" getOriCertCalidadPuntuacionByIdConvocatoriaAndCodigo - End " + codigo + formatFechaLog.format(new Date()));
        return resultado;
    }

    public List<OriCertCalidadPuntuacion> getOriCertCalidadPuntuacionByIdConvocatoria(int idConvocatoria, Connection con) throws SQLException, Exception{
        log.info(" getOriCertCalidadPuntuacionByIdConvocatoria - Begin " +  formatFechaLog.format(new Date()));
        List<OriCertCalidadPuntuacion> resultado = new ArrayList<OriCertCalidadPuntuacion>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String query = "select * "
                    + " from ori_cert_calidad_puntuacion "
                    + " where "
                    + " ID_CONVOCATORIA=? ";
            log.info("sql = " + query);
            ps = con.prepareStatement(query);
            ps.setInt(1, idConvocatoria);
            log.info("params = " + idConvocatoria);
            rs=ps.executeQuery();
            while (rs.next()) {
                resultado.add((OriCertCalidadPuntuacion) MeLanbide47MappingUtils.getInstance().map(rs, OriCertCalidadPuntuacion.class));
            }
        } catch (SQLException e) {
            log.error("Se ha producido recuperando Lita Certificados calidad ", e);
            throw e;
        } catch (Exception e) {
            log.error("Se ha producido recuperando lista certificados calidad", e);
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
        log.info(" getOriCertCalidadPuntuacionByIdConvocatoria - End " + formatFechaLog.format(new Date()));
        return resultado;
    }

}
