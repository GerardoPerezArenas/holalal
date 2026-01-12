package es.altia.flexia.integracion.moduloexterno.melanbide48.dao;

import es.altia.flexia.integracion.moduloexterno.melanbide48.util.MeLanbide48MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide48.vo.ColecCompIgualdad;
import es.altia.flexia.integracion.moduloexterno.melanbide48.vo.SelectItem;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ColecCompIgualdadDAO {

    private static final Logger log = LogManager.getLogger(ColecCompIgualdadDAO.class);
    private final SimpleDateFormat formatFechaLog = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    public ColecCompIgualdad getColecCompIgualdadByCodigo(String codigo, Connection con) throws SQLException, Exception{
        log.info(" getColecCompIgualdadByCodigo - Begin " + codigo + formatFechaLog.format(new Date()));
        ColecCompIgualdad resultado = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String query = "select * "
                    + " from COLEC_COMP_IGUALDAD "
                    + " where "
                    + " codigo=? ";
            log.info("sql = " + query);
            ps = con.prepareStatement(query);
            ps.setString(1, codigo);
            log.info("params = " + codigo);
            rs = ps.executeQuery();
            while (rs.next()) {
                resultado = (ColecCompIgualdad) MeLanbide48MappingUtils.getInstance().map(rs, ColecCompIgualdad.class);
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
        log.info(" getColecCompIgualdadByCodigo - End " + codigo + formatFechaLog.format(new Date()));
        return resultado;
    }

    public List<ColecCompIgualdad> getColecCompIgualdad(Connection con) throws SQLException, Exception{
        log.info(" getColecCompIgualdad - Begin " +  formatFechaLog.format(new Date()));
        List<ColecCompIgualdad> resultado = new ArrayList<ColecCompIgualdad>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String query = "select * "
                    + " from COLEC_COMP_IGUALDAD "
                    ;
            log.info("sql = " + query);
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                resultado.add((ColecCompIgualdad) MeLanbide48MappingUtils.getInstance().map(rs, ColecCompIgualdad.class));
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
        log.info(" getColecCompIgualdad - End " + formatFechaLog.format(new Date()));
        return resultado;
    }

    public List<SelectItem> getListaColecCompIgualdad(Integer idioma, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<SelectItem> retList = new ArrayList<SelectItem>();
        try {
            String query = null;
            query = "select codigo CODIGO, case when 4=" + (idioma != null ? idioma : "null") + " then descripcioneu else descripcion end as DESCRIPCION "
                    + " from COLEC_COMP_IGUALDAD "
                    + " order by codigo ";
            if (log.isDebugEnabled()) {
                log.error("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                retList.add((SelectItem) MeLanbide48MappingUtils.getInstance().map(rs, SelectItem.class));
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
