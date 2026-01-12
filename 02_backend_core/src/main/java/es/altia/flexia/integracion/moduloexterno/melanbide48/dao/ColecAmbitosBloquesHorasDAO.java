/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide48.dao;

import es.altia.flexia.integracion.moduloexterno.melanbide48.util.MeLanbide48MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide48.vo.ColecAmbitosBloquesHoras;
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
public class ColecAmbitosBloquesHorasDAO {
    
    private static final Logger log = LogManager.getLogger(ColecAmbitosBloquesHorasDAO.class);
    private final SimpleDateFormat formatFechaLog = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    
    public ColecAmbitosBloquesHoras getColecAmbitosBloquesHorasByPK(int idBdConvocatoria, int colectivo, int id, Connection con) throws SQLException, Exception {
        log.info(" getColecAmbitosBloquesHorasByPK - Begin " + formatFechaLog.format(new Date()));
        ColecAmbitosBloquesHoras resultado = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String query = "select * "
                    + " from colec_ambitos_bloques_horas "
                    + " where "
                    + " IDFKCONVOCATORIAACTIVA=? "
                    + " and COLECTIVO=? "
                    + " and id=? ";
            log.info("sql = " + query);
            int params=1;
            ps = con.prepareStatement(query);
            ps.setLong(params++, idBdConvocatoria);
            ps.setLong(params++, colectivo);
            ps.setLong(params++, id);
            log.info("params = "
                    + "," + idBdConvocatoria 
                    + "," + colectivo 
                    + "," + id
            );
            rs = ps.executeQuery();
            while (rs.next()) {
                resultado = (ColecAmbitosBloquesHoras) MeLanbide48MappingUtils.getInstance().map(rs, ColecAmbitosBloquesHoras.class);
            }
        } catch (SQLException e) {
            log.error("Se ha producido recuperando getColecAmbitosBloquesHorasByPK ", e);
            throw e;
        } catch (Exception e) {
            log.error("Se ha producido recuperando getColecAmbitosBloquesHorasByPK ", e);
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
        log.info(" getColecAmbitosBloquesHorasByPK - End " + id + formatFechaLog.format(new Date()));
        return resultado;
    }
    
    public List<ColecAmbitosBloquesHoras> getColecAmbitosBloquesHorasByConvocatoriaColectivo(int idBdConvocatoria, int colectivo, Connection con) throws SQLException, Exception {
        log.info(" getColecAmbitosBloquesHorasByConvocatoriaColectivo - Begin " + colectivo + formatFechaLog.format(new Date()));
        List<ColecAmbitosBloquesHoras> resultado = new ArrayList<ColecAmbitosBloquesHoras>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String query = "select * "
                    + " from colec_ambitos_bloques_horas "
                    + " where "
                    + " IDFKCONVOCATORIAACTIVA=? "
                    + " and COLECTIVO=? "
                    ;
            log.info("sql = " + query);
            int params=1;
            ps = con.prepareStatement(query);
            ps.setLong(params++, idBdConvocatoria);
            ps.setLong(params++, colectivo);
            log.info("params = "
                    + "," + idBdConvocatoria 
                    + "," + colectivo 
            );
            rs = ps.executeQuery();
            while (rs.next()) {
                resultado.add((ColecAmbitosBloquesHoras) MeLanbide48MappingUtils.getInstance().map(rs, ColecAmbitosBloquesHoras.class));
            }
        } catch (SQLException e) {
            log.error("Se ha producido recuperando getColecAmbitosBloquesHorasByConvocatoriaColectivo ", e);
            throw e;
        } catch (Exception e) {
            log.error("Se ha producido recuperando getColecAmbitosBloquesHorasByConvocatoriaColectivo ", e);
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
        log.info(" getColecAmbitosBloquesHorasByConvocatoriaColectivo - End " + colectivo + formatFechaLog.format(new Date()));
        return resultado;
    }
    
    public List<SelectItem> getAmbitoSolicitadoxColectivoConvocatoria(Integer idioma, Integer idBDConvocatoria, Integer codColectivo, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<SelectItem> retList = new ArrayList<SelectItem>();
        try {
            String query = null;
            query = "select "
                    + " id CODIGO "
                    + " ,case when 4=" + (idioma != null ? idioma : "null") + " then ambitoDescripcioneu else ambitoDescripcion end as  DESCRIPCION "
                    + " From COLEC_AMBITOS_BLOQUES_HORAS "
                    + " where idfkconvocatoriaactiva=" + (idBDConvocatoria != null ? idBDConvocatoria : "null")
                    + " and colectivo=" + (codColectivo != null ? codColectivo : "null")
                    + " order by DESCRIPCION";
            log.info("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                retList.add((SelectItem) MeLanbide48MappingUtils.getInstance().map(rs, SelectItem.class));
            }
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Lista Desplegable Ambitos. " + idBDConvocatoria + "/" + codColectivo, ex);
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
    
    /**
     * Coectivo 1 , 2 Solo con TH
     *
     * @param idioma
     * @param idBDConvocatoria
     * @param codColectivo
     * @param territorioHistorico
     * @param con
     * @return
     * @throws Exception
     */
    public List<SelectItem> getAmbitoSolicitadoxColectivoConvocatoriaTH(Integer idioma, Integer idBDConvocatoria, Integer codColectivo, Integer territorioHistorico, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<SelectItem> retList = new ArrayList<SelectItem>();
        try {
            String query = null;
            query = "select distinct "
                    + " colec_ambitos_bloques_horas.id CODIGO "
                    + " ,case when 4=" + (idioma != null ? idioma : "null") + " then colec_ambitos_bloques_horas.ambitoDescripcioneu else colec_ambitos_bloques_horas.ambitoDescripcion end as  DESCRIPCION "
                    + " From COLEC_AMBITOS_BLOQUES_HORAS "
                    + " LEFT JOIN colec_municipios_puntuacion ON COLEC_AMBITOS_BLOQUES_HORAS.idfkconvocatoriaactiva=colec_municipios_puntuacion.idfkconvocatoria and  colec_municipios_puntuacion.CODIGOCOLECTIVO=COLEC_AMBITOS_BLOQUES_HORAS.colectivo AND colec_municipios_puntuacion.GRUPOAMBITOORDEN=COLEC_AMBITOS_BLOQUES_HORAS.id and colec_municipios_puntuacion.codigoprovincia=" + (territorioHistorico != null ? territorioHistorico : "null")
                    + " where colec_ambitos_bloques_horas.idfkconvocatoriaactiva=" + (idBDConvocatoria != null ? idBDConvocatoria : "null")
                    + " and colec_ambitos_bloques_horas.colectivo=" + (codColectivo != null ? codColectivo : "null")
                    + " and colec_municipios_puntuacion.codigoprovincia=" + (territorioHistorico != null ? territorioHistorico : "null")
                    + " order by DESCRIPCION";
            log.info("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                retList.add((SelectItem) MeLanbide48MappingUtils.getInstance().map(rs, SelectItem.class));
            }
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Lista Desplegable Ambitos. " + idBDConvocatoria + "/" + codColectivo, ex);
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
