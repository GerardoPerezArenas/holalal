package es.altia.flexia.integracion.moduloexterno.melanbide48.dao;

import es.altia.flexia.integracion.moduloexterno.melanbide48.util.MeLanbide48MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide48.vo.ColecCertCalidadPuntuacion;
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

public class ColecCertCalidadPuntuacionDAO {

    private static final Logger log = LogManager.getLogger(ColecCertCalidadPuntuacionDAO.class);
    private final SimpleDateFormat formatFechaLog = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    public ColecCertCalidadPuntuacion getColecCertCalidadPuntuacionByIdConvocatoriaAndCodigo(int idConvocatoria, String codigo, Connection con) throws SQLException, Exception{
        log.info(" getColecCertCalidadPuntuacionByIdConvocatoriaAndCodigo - Begin " + codigo + formatFechaLog.format(new Date()));
        ColecCertCalidadPuntuacion resultado = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String query = "select * "
                    + " from colec_cert_calidad_puntuacion "
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
                resultado = (ColecCertCalidadPuntuacion) MeLanbide48MappingUtils.getInstance().map(rs, ColecCertCalidadPuntuacion.class);
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
        log.info(" getColecCertCalidadPuntuacionByIdConvocatoriaAndCodigo - End " + codigo + formatFechaLog.format(new Date()));
        return resultado;
    }

    public List<ColecCertCalidadPuntuacion> getColecCertCalidadPuntuacionByIdConvocatoria(int idConvocatoria, Connection con) throws SQLException, Exception{
        log.info(" getColecCertCalidadPuntuacionByIdConvocatoria - Begin " +  formatFechaLog.format(new Date()));
        List<ColecCertCalidadPuntuacion> resultado = new ArrayList<ColecCertCalidadPuntuacion>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String query = "select * "
                    + " from colec_cert_calidad_puntuacion "
                    + " where "
                    + " ID_CONVOCATORIA=? ";
            log.info("sql = " + query);
            ps = con.prepareStatement(query);
            ps.setInt(1, idConvocatoria);
            log.info("params = " + idConvocatoria);
            rs=ps.executeQuery();
            while (rs.next()) {
                resultado.add((ColecCertCalidadPuntuacion) MeLanbide48MappingUtils.getInstance().map(rs, ColecCertCalidadPuntuacion.class));
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
        log.info(" getColecCertCalidadPuntuacionByIdConvocatoria - End " + formatFechaLog.format(new Date()));
        return resultado;
    }

}
