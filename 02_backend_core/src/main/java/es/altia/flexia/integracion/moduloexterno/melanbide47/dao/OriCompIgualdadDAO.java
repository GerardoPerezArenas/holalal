package es.altia.flexia.integracion.moduloexterno.melanbide47.dao;

import es.altia.flexia.integracion.moduloexterno.melanbide47.util.MeLanbide47MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide47.vo.orientacion.OriCompIgualdad;
import es.altia.flexia.integracion.moduloexterno.melanbide47.vo.comun.SelectItem;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OriCompIgualdadDAO {

    private static final Logger log = LogManager.getLogger(OriCompIgualdadDAO.class);
    private final SimpleDateFormat formatFechaLog = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    public OriCompIgualdad getOriCompIgualdadByCodigo(String codigo, Connection con) throws SQLException, Exception{
        log.info(" getOriCompIgualdadByCodigo - Begin " + codigo + formatFechaLog.format(new Date()));
        OriCompIgualdad resultado = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String query = "select * "
                    + " from ori_COMP_IGUALDAD "
                    + " where "
                    + " codigo=? ";
            log.info("sql = " + query);
            ps = con.prepareStatement(query);
            ps.setString(1, codigo);
            log.info("params = " + codigo);
            rs = ps.executeQuery();
            while (rs.next()) {
                resultado = (OriCompIgualdad) MeLanbide47MappingUtils.getInstance().map(rs, OriCompIgualdad.class);
            }
        } catch (SQLException e) {
            log.error("Se ha producido recuperando datos Compromiso Igualdad", e);
            throw e;
        } catch (Exception e) {
            log.error("Se ha producido recuperando datos Compromiso Igualdad", e);
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
        log.info(" getOriCompIgualdadByCodigo - End " + codigo + formatFechaLog.format(new Date()));
        return resultado;
    }

    public List<OriCompIgualdad> getOriCompIgualdad(Connection con) throws SQLException, Exception{
        log.info(" getOriCompIgualdad - Begin " +  formatFechaLog.format(new Date()));
        List<OriCompIgualdad> resultado = new ArrayList<OriCompIgualdad>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String query = "select * "
                    + " from ori_COMP_IGUALDAD "
                    ;
            log.info("sql = " + query);
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                resultado.add((OriCompIgualdad) MeLanbide47MappingUtils.getInstance().map(rs, OriCompIgualdad.class));
            }
        } catch (SQLException e) {
            log.error("Se ha producido recuperando Lita Compromiso Igualdad ", e);
            throw e;
        } catch (Exception e) {
            log.error("Se ha producido recuperando lista Compromiso Igualdad", e);
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
        log.info(" getOriCompIgualdad - End " + formatFechaLog.format(new Date()));
        return resultado;
    }

    public List<SelectItem> getListaOriCompIgualdad(Integer idioma, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<SelectItem> retList = new ArrayList<SelectItem>();
        try {
            String query = null;
            query = "select codigo CODIGO, case when 4=" + (idioma != null ? idioma : "null") + " then descripcioneu else descripcion end as DESCRIPCION "
                    + " from ori_COMP_IGUALDAD "
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
            log.error("Se ha producido un error recuperando lista Compromiso igualdad.", ex);
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
