/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.lanbide01.persistence.dao;

import es.altia.flexia.integracion.moduloexterno.lanbide01.vo.Melanbide01Decreto;
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
public class Melanbide01DecretoDao {
    
    // Formateador de Fecha RegistroLog
    SimpleDateFormat formatFechaLog = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    Logger log = LogManager.getLogger(Melanbide01DecretoDao.class);
    
    
    public List<Melanbide01Decreto> getDecretoTodos(Connection con) throws SQLException, Exception {
        log.info(" getDecretoTodos - Begin " + formatFechaLog.format(new Date()));
        List<Melanbide01Decreto> resultadoList = new ArrayList<Melanbide01Decreto>();
        String query = "";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            query = "select * "
                    + " from MELANBIDE01_DECRETO "
                    ;
            log.info("sql = " + query);
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                Melanbide01Decreto resultado = new Melanbide01Decreto();
                resultado.setId(rs.getInt("id"));
                resultado.setDecretoCodigo(rs.getString("decretoCodigo"));
                resultado.setDecretoDescripcion(rs.getString("decretoDescripcion"));
                resultado.setDescretoDecripcionEu(rs.getString("descretoDecripcionEu"));
                resultado.setDecretoFecPublicacion(rs.getDate("decretoFecPublicacion"));
                resultado.setDecretoFecEntradaVigor(rs.getDate("decretoFecEntradaVigor"));
                resultado.setDecretoFecFinAplicacion(rs.getDate("decretoFecFinAplicacion"));
                resultadoList.add(resultado);
            }
        } catch (SQLException e) {
            log.error("Se ha producido recuperando Lista de Decretos ", e);
            throw e;
        } catch (Exception e) {
            log.error("Se ha producido recuperando datos de decretos ", e);
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

    public Melanbide01Decreto getDecretoById(int id, Connection con) throws SQLException, Exception {
        log.info(" getDecretoById - Begin " + id + formatFechaLog.format(new Date()));
        Melanbide01Decreto resultado = new Melanbide01Decreto();
        String query = "";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            query = "select * "
                    + " from MELANBIDE01_DECRETO "
                    + " where "
                    + " id=? ";
            log.info("sql = " + query);
            ps = con.prepareStatement(query);
            ps.setLong(1, id);
            log.info("params = " + id);
            rs = ps.executeQuery();
            while (rs.next()) {
               resultado.setId(rs.getInt("id"));
               resultado.setDecretoCodigo(rs.getString("decretoCodigo"));
                resultado.setDecretoDescripcion(rs.getString("decretoDescripcion"));
                resultado.setDescretoDecripcionEu(rs.getString("descretoDecripcionEu"));
                resultado.setDecretoFecPublicacion(rs.getDate("decretoFecPublicacion"));
                resultado.setDecretoFecEntradaVigor(rs.getDate("decretoFecEntradaVigor"));
                resultado.setDecretoFecFinAplicacion(rs.getDate("decretoFecFinAplicacion"));
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
    
    public Melanbide01Decreto getDecretoAplicableExpediente(Date fechaReferenciaExpediente, Connection con) throws SQLException, Exception {
        log.info(" getDecretoAplicableExpediente - Begin " + fechaReferenciaExpediente + " - " + formatFechaLog.format(new Date()));
        Melanbide01Decreto resultado = null;
        String query = "";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            if(fechaReferenciaExpediente!=null){
                query = "select * "
                        + " from MELANBIDE01_DECRETO "
                        + " where "
                        + " decretoFecEntradaVigor <= ? "
                        + " and (nvl(decretoFecFinAplicacion,sysdate+1)) > ? ";
                log.info("sql = " + query);
                ps = con.prepareStatement(query);
                java.sql.Date fechaReferenciaExpedienteSQLDate = new java.sql.Date(fechaReferenciaExpediente.getTime());
                ps.setDate(1, fechaReferenciaExpedienteSQLDate);
                ps.setDate(2, fechaReferenciaExpedienteSQLDate);
                log.info("params = " + fechaReferenciaExpedienteSQLDate);
                rs = ps.executeQuery();
                while (rs.next()) {
                    resultado = new Melanbide01Decreto();
                    resultado.setId(rs.getInt("id"));
                    resultado.setDecretoCodigo(rs.getString("decretoCodigo"));
                    resultado.setDecretoDescripcion(rs.getString("decretoDescripcion"));
                    resultado.setDescretoDecripcionEu(rs.getString("descretoDecripcionEu"));
                    resultado.setDecretoFecPublicacion(rs.getDate("decretoFecPublicacion"));
                    resultado.setDecretoFecEntradaVigor(rs.getDate("decretoFecEntradaVigor"));
                    resultado.setDecretoFecFinAplicacion(rs.getDate("decretoFecFinAplicacion"));
                }
            }else{
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
        log.info(" getDecretoById - End " + fechaReferenciaExpediente + " " +formatFechaLog.format(new Date()));
        return resultado;
    }
    
    public Melanbide01Decreto getDecretoByCodigoDecreto(String decretoCodigo, Connection con) throws SQLException, Exception {
        log.info(" getDecretoByCodigoDecreto - Begin " + decretoCodigo + formatFechaLog.format(new Date()));
        Melanbide01Decreto resultado = new Melanbide01Decreto();
        String query = "";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            query = "select * "
                    + " from MELANBIDE01_DECRETO "
                    + " where "
                    + " decretoCodigo=? ";
            log.info("sql = " + query);
            ps = con.prepareStatement(query);
            ps.setString(1, decretoCodigo);
            log.info("params = " + decretoCodigo);
            rs = ps.executeQuery();
            while (rs.next()) {
                resultado.setId(rs.getInt("id"));
                resultado.setDecretoCodigo(rs.getString("decretoCodigo"));
                resultado.setDecretoDescripcion(rs.getString("decretoDescripcion"));
                resultado.setDescretoDecripcionEu(rs.getString("descretoDecripcionEu"));
                resultado.setDecretoFecPublicacion(rs.getDate("decretoFecPublicacion"));
                resultado.setDecretoFecEntradaVigor(rs.getDate("decretoFecEntradaVigor"));
                resultado.setDecretoFecFinAplicacion(rs.getDate("decretoFecFinAplicacion"));
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
