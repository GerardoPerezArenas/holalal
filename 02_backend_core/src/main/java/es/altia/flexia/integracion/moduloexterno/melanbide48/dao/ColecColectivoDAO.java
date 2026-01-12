/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide48.dao;

import es.altia.flexia.integracion.moduloexterno.melanbide48.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide48.util.ConstantesMeLanbide48;
import es.altia.flexia.integracion.moduloexterno.melanbide48.util.MeLanbide48MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide48.vo.ColecColectivo;
import es.altia.flexia.integracion.moduloexterno.melanbide48.vo.SelectItem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author INGDGC
 */
public class ColecColectivoDAO {
    
    private static final Logger log = LogManager.getLogger(ColecColectivoDAO.class);
    private final SimpleDateFormat formatFechaLog = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    
    public ColecColectivo getColecColectivoById(int id, Connection con) throws SQLException, Exception{
        log.info(" getColecColectivoById - Begin " + id + formatFechaLog.format(new Date()));
        ColecColectivo resultado = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String query = "select * "
                    + " from colec_colectivo "
                    + " where "
                    + " id=? ";
            log.info("sql = " + query);
            ps = con.prepareStatement(query);
            ps.setLong(1, id);
            log.info("params = " + id);
            rs = ps.executeQuery();
            while (rs.next()) {
                resultado = (ColecColectivo) MeLanbide48MappingUtils.getInstance().map(rs, ColecColectivo.class);
            }
        } catch (SQLException e) {
            log.error("Se ha producido recuperando datos Colectivo ", e);
            throw e;
        } catch (Exception e) {
            log.error("Se ha producido recuperando datos Colectivo ", e);
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
        log.info(" getColecColectivoById - End " + id + formatFechaLog.format(new Date()));
        return resultado;
    }
    
    public List<ColecColectivo> getColecColectivoTodos(Connection con) throws SQLException, Exception{
        log.info(" getColecColectivoTodos - Begin " +  formatFechaLog.format(new Date()));
        List<ColecColectivo> resultado = new ArrayList<ColecColectivo>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String query = "select * "
                    + " from colec_colectivo "
                    ;
            log.info("sql = " + query);
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                resultado.add((ColecColectivo) MeLanbide48MappingUtils.getInstance().map(rs, ColecColectivo.class));
            }
        } catch (SQLException e) {
            log.error("Se ha producido recuperando datos Colectivo Todos ", e);
            throw e;
        } catch (Exception e) {
            log.error("Se ha producido recuperando datos Colectivo Todos ", e);
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
        log.info(" getColecColectivoTodos - End " + formatFechaLog.format(new Date()));
        return resultado;
    }
    
    public List<SelectItem> getListaColectivos(Integer idioma, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<SelectItem> retList = new ArrayList<SelectItem>();
        try {
            String query = null;
            query = "select id CODIGO, case when 4=" + (idioma != null ? idioma : "null") + " then descripcioneu else descripcion end as DESCRIPCION "
                    + " from " + ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_COLECTIVO, ConstantesMeLanbide48.FICHERO_PROPIEDADES)
                    + " order by id ";
            if (log.isDebugEnabled()) {
                log.error("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                retList.add((SelectItem) MeLanbide48MappingUtils.getInstance().map(rs, SelectItem.class));
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando lista colectivos.", ex);
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
