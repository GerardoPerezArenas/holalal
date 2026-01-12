package es.altia.flexia.integracion.moduloexterno.melanbide47.dao;

import es.altia.flexia.integracion.moduloexterno.melanbide47.util.MeLanbide47MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide47.vo.orientacion.OriCertCalidad;
import es.altia.flexia.integracion.moduloexterno.melanbide47.vo.comun.SelectItem;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OriCertCalidadDAO {

    private static final Logger log = LogManager.getLogger(OriCertCalidadDAO.class);
    private final SimpleDateFormat formatFechaLog = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    public OriCertCalidad getOriCertCalidadByCodigo(String codigo, Connection con) throws SQLException, Exception{
        log.info(" getOriCertCalidadByCodigo - Begin " + codigo + formatFechaLog.format(new Date()));
        OriCertCalidad resultado = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String query = "select * "
                    + " from ori_cert_calidad "
                    + " where "
                    + " codigo=? ";
            log.info("sql = " + query);
            ps = con.prepareStatement(query);
            ps.setString(1, codigo);
            log.info("params = " + codigo);
            rs = ps.executeQuery();
            while (rs.next()) {
                resultado = (OriCertCalidad) MeLanbide47MappingUtils.getInstance().map(rs, OriCertCalidad.class);
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
        log.info(" getOriCertCalidadByCodigo - End " + codigo + formatFechaLog.format(new Date()));
        return resultado;
    }

    public List<OriCertCalidad> getOriCertCalidad(Connection con) throws SQLException, Exception{
        log.info(" getOriCertCalidad - Begin " +  formatFechaLog.format(new Date()));
        List<OriCertCalidad> resultado = new ArrayList<OriCertCalidad>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String query = "select * "
                    + " from ori_cert_calidad "
                    ;
            log.info("sql = " + query);
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                resultado.add((OriCertCalidad) MeLanbide47MappingUtils.getInstance().map(rs, OriCertCalidad.class));
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
        log.info(" getOriCertCalidad - End " + formatFechaLog.format(new Date()));
        return resultado;
    }

    public List<SelectItem> getListaOriCertCalidad(Integer idioma, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<SelectItem> retList = new ArrayList<SelectItem>();
        try {
            String query = null;
            query = "select codigo CODIGO, case when 4=" + (idioma != null ? idioma : "null") + " then descripcioneu else descripcion end as DESCRIPCION "
                    + " from ori_cert_calidad "
                    + " order by codigo ";
            if (log.isDebugEnabled()) {
                log.error("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                retList.add((SelectItem) MeLanbide47MappingUtils.getInstance().map(rs, SelectItem.class));
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando lista Certificados Calidad.", ex);
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
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
}
