/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide47.dao;

import es.altia.flexia.integracion.moduloexterno.melanbide47.vo.comun.MeLanbideConvocatorias;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
public class MeLanbideConvocatoriasDAO {
    // Formateador de Fecha RegistroLog
    SimpleDateFormat formatFechaLog = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    Logger log = LogManager.getLogger(MeLanbideConvocatoriasDAO.class);

    public List<MeLanbideConvocatorias> getDecretoTodos(Connection con) throws SQLException, Exception {
        log.info(" getDecretoTodos - Begin " + formatFechaLog.format(new Date()));
        List<MeLanbideConvocatorias> resultadoList = new ArrayList<MeLanbideConvocatorias>();
        String query = "";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            query = "select * "
                    + " from MELANBIDE_CONVOCATORIAS ";
            log.info("sql = " + query);
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                MeLanbideConvocatorias resultado = new MeLanbideConvocatorias();
                resultado.setId(rs.getInt("id"));
                resultado.setProCod(rs.getString("PRO_COD"));
                resultado.setProCodPlatea(rs.getString("PRO_COD_PLATEA"));
                resultado.setNumBOPV(rs.getString("NUM_BOPV"));
                resultado.setFecConsLanbide(rs.getDate("FEC_CONS_LANBIDE"));
                resultado.setDecretoCodigo(rs.getString("DECRETOCODIGO"));
                resultado.setDecretoDesripcion(rs.getString("DECRETODESCRIPCION"));
                resultado.setDecretoDesripcionEu(rs.getString("DECRETODESCRIPCIONEU"));
                resultado.setDecretoFecPublicacion(rs.getDate("DECRETOFECPUBLICACION"));
                resultado.setDecretoFecEntradaVigor(rs.getDate("DECRETOFECENTRADAVIGOR"));
                resultado.setDecretoFecFinAplicacion(rs.getDate("DECRETOFECFINAPLICACION"));
                resultadoList.add(resultado);
            }
        } catch (SQLException e) {
            log.error("Se ha producido recuperando Lista de Decretos/Convocatorias ", e);
            throw e;
        } catch (Exception e) {
            log.error("Se ha producido recuperando datos de Decretos/Convocatorias ", e);
            throw e;
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("Procedemos a cerrar el resultset");
            }
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        log.info(" getDecretoTodos - End " + formatFechaLog.format(new Date()));
        return resultadoList;
    }

    public MeLanbideConvocatorias getDecretoById(int id, Connection con) throws SQLException, Exception {
        log.info(" getDecretoById - Begin " + id + formatFechaLog.format(new Date()));
        String query = "";
        MeLanbideConvocatorias resultado = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            query = "select * "
                    + " from MELANBIDE_CONVOCATORIAS "
                    + " where "
                    + " id=? ";
            log.info("sql = " + query);
            ps = con.prepareStatement(query);
            ps.setLong(1, id);
            log.info("params = " + id);
            rs = ps.executeQuery();
            while (rs.next()) {
                resultado = new MeLanbideConvocatorias(
                        rs.getInt("id")
                        ,rs.getString("PRO_COD")
                        ,rs.getString("PRO_COD_PLATEA")
                        ,rs.getString("NUM_BOPV")
                        ,rs.getDate("FEC_CONS_LANBIDE")
                        ,rs.getString("DECRETOCODIGO")
                        ,rs.getString("DECRETODESCRIPCION")
                        ,rs.getString("DECRETODESCRIPCIONEU")
                        ,rs.getDate("DECRETOFECPUBLICACION")
                        ,rs.getDate("DECRETOFECENTRADAVIGOR")
                        ,rs.getDate("DECRETOFECFINAPLICACION")                        
                );
            }
        } catch (SQLException e) {
            log.error("Se ha producido recuperando datos Decreto ", e);
            throw e;
        } catch (Exception e) {
            log.error("Se ha producido recuperando datos Decreto ", e);
            throw e;
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("Procedemos a cerrar el resultset");
            }
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        log.info(" getDecretoById - End " + id + formatFechaLog.format(new Date()));
        return resultado;
    }

    public MeLanbideConvocatorias getDecretoAplicableExpediente(Date fechaReferenciaExpediente, String codProcedimiento, Connection con) throws SQLException, Exception {
        log.info(" getDecretoAplicableExpediente - Begin " + fechaReferenciaExpediente + " - " + formatFechaLog.format(new Date()));
        MeLanbideConvocatorias resultado = null;
        String query = "";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            if (fechaReferenciaExpediente != null) {
                query = "select * "
                        + " from MELANBIDE_CONVOCATORIAS "
                        + " where "
                        + " pro_cod= ? "
                        + " and decretoFecEntradaVigor <= ? "
                        + " and (nvl(decretoFecFinAplicacion,sysdate+1)) > ? ";
                log.info("sql = " + query);
                ps = con.prepareStatement(query);
                java.sql.Date fechaReferenciaExpedienteSQLDate = new java.sql.Date(fechaReferenciaExpediente.getTime());
                ps.setString(1, codProcedimiento);
                ps.setDate(2, fechaReferenciaExpedienteSQLDate);
                ps.setDate(3, fechaReferenciaExpedienteSQLDate);
                log.info("params(1) = " + codProcedimiento);
                log.info("params(2y3) = " + fechaReferenciaExpedienteSQLDate);
                rs = ps.executeQuery();
                while (rs.next()) {
                    resultado = new MeLanbideConvocatorias(
                            rs.getInt("id")
                            ,rs.getString("PRO_COD")
                            ,rs.getString("PRO_COD_PLATEA")
                            ,rs.getString("NUM_BOPV")
                            ,rs.getDate("FEC_CONS_LANBIDE")
                            ,rs.getString("DECRETOCODIGO")
                            ,rs.getString("DECRETODESCRIPCION")
                            ,rs.getString("DECRETODESCRIPCIONEU")
                            ,rs.getDate("DECRETOFECPUBLICACION")
                            ,rs.getDate("DECRETOFECENTRADAVIGOR")
                            ,rs.getDate("DECRETOFECFINAPLICACION")   
                    );
                }
            } else {
                log.info("Fecha de referencia recibida a null, no devolvemos ningun decreto aplicable.");
            }
        } catch (SQLException e) {
            log.error("Se ha producido recuperando datos Decreto ", e);
            throw e;
        } catch (Exception e) {
            log.error("Se ha producido recuperando datos Decreto ", e);
            throw e;
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("Procedemos a cerrar el resultset");
            }
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        log.info(" getDecretoAplicableExpediente - End " + fechaReferenciaExpediente + " " + formatFechaLog.format(new Date()));
        return resultado;
    }

    public MeLanbideConvocatorias getDecretoByCodigoDecreto(String decretoCodigo, Connection con) throws SQLException, Exception {
        log.info(" getDecretoByCodigoDecreto - Begin " + decretoCodigo + formatFechaLog.format(new Date()));
        MeLanbideConvocatorias resultado = null;
        String query = "";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            query = "select * "
                    + " from MELANBIDE_CONVOCATORIAS "
                    + " where "
                    + " decretoCodigo=? ";
            log.info("sql = " + query);
            ps = con.prepareStatement(query);
            ps.setString(1, decretoCodigo);
            log.info("params = " + decretoCodigo);
            rs = ps.executeQuery();
            while (rs.next()) {
                resultado = new MeLanbideConvocatorias(
                            rs.getInt("id")
                            ,rs.getString("PRO_COD")
                            ,rs.getString("PRO_COD_PLATEA")
                            ,rs.getString("NUM_BOPV")
                            ,rs.getDate("FEC_CONS_LANBIDE")
                            ,rs.getString("DECRETOCODIGO")
                            ,rs.getString("DECRETODESCRIPCION")
                            ,rs.getString("DECRETODESCRIPCIONEU")
                            ,rs.getDate("DECRETOFECPUBLICACION")
                            ,rs.getDate("DECRETOFECENTRADAVIGOR")
                            ,rs.getDate("DECRETOFECFINAPLICACION")   
                    );
            }
        } catch (SQLException e) {
            log.error("Se ha producido recuperando datos Decreto ", e);
            throw e;
        } catch (Exception e) {
            log.error("Se ha producido recuperando datos Decreto ", e);
            throw e;
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("Procedemos a cerrar el resultset");
            }
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        log.info(" getDecretoByCodigoDecreto - End " + decretoCodigo + formatFechaLog.format(new Date()));
        return resultado;
    }
}
