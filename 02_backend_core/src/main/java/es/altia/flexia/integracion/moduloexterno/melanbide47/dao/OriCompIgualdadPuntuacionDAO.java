package es.altia.flexia.integracion.moduloexterno.melanbide47.dao;

import es.altia.flexia.integracion.moduloexterno.melanbide47.util.MeLanbide47MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide47.vo.orientacion.OriCompIgualdadPuntuacion;
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

public class OriCompIgualdadPuntuacionDAO {

    private static final Logger log = LogManager.getLogger(OriCompIgualdadPuntuacionDAO.class);
    private final SimpleDateFormat formatFechaLog = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    public OriCompIgualdadPuntuacion getOriCompIgualdadPuntuacionByIdConvocatoriaAndCodigo(int idConvocatoria, String codigo, Connection con) throws SQLException, Exception{
        log.info(" getOriCompIgualdadPuntuacionByIdConvocatoriaAndCodigo - Begin " + codigo + formatFechaLog.format(new Date()));
        OriCompIgualdadPuntuacion resultado = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String query = "select * "
                    + " from ori_COMP_IGUALDAD_PUNTUACION "
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
                resultado = (OriCompIgualdadPuntuacion) MeLanbide47MappingUtils.getInstance().map(rs, OriCompIgualdadPuntuacion.class);
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
        log.info(" getOriCompIgualdadPuntuacionByIdConvocatoriaAndCodigo - End " + codigo + formatFechaLog.format(new Date()));
        return resultado;
    }

    public List<OriCompIgualdadPuntuacion> getOriCompIgualdadPuntuacionByIdConvocatoria(int idConvocatoria, Connection con) throws SQLException, Exception{
        log.info(" getOriCompIgualdadPuntuacionByIdConvocatoria - Begin " +  formatFechaLog.format(new Date()));
        List<OriCompIgualdadPuntuacion> resultado = new ArrayList<OriCompIgualdadPuntuacion>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String query = "select * "
                    + " from ori_COMP_IGUALDAD_PUNTUACION "
                    + " where "
                    + " ID_CONVOCATORIA=? ";
            log.info("sql = " + query);
            ps = con.prepareStatement(query);
            ps.setInt(1, idConvocatoria);
            log.info("params = " + idConvocatoria);
            rs=ps.executeQuery();
            while (rs.next()) {
                resultado.add((OriCompIgualdadPuntuacion) MeLanbide47MappingUtils.getInstance().map(rs, OriCompIgualdadPuntuacion.class));
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
        log.info(" getOriCompIgualdadPuntuacionByIdConvocatoria - End " + formatFechaLog.format(new Date()));
        return resultado;
    }

}
