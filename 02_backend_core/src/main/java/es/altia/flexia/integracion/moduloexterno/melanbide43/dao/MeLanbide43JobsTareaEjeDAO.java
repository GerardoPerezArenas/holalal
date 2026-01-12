/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide43.dao;

import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.MeLanbide43JobsTareaEje;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author INGDGC
 */
public class MeLanbide43JobsTareaEjeDAO {
    
    Logger log = LogManager.getLogger(this.getClass());
    
    public MeLanbide43JobsTareaEje getMeLanbide43JobsTareaEje(String nombreProcesoJob, Connection con) throws Exception{
        PreparedStatement ps = null;
        ResultSet rs = null;
        MeLanbide43JobsTareaEje respuesta = new MeLanbide43JobsTareaEje();
        String mensajeGestionError = "";
        try {
            String query = "SELECT * "
                    + " FROM MELANBIDE43_JOBS_TAREA_EJE "
                    + " WHERE nombreProcesoJob=? ";
            log.debug("sql = " + query);
            ps = con.prepareStatement(query);
            log.debug("param: "
                    + " nombreProcesoJob:" + nombreProcesoJob
            );
            int contador = 1;
            ps.setString(contador++, nombreProcesoJob);
            rs = ps.executeQuery();
            if (rs.next()) { // Por BD 1 Linea Por Job
                respuesta.setCodigoUsuario(rs.getInt("codigoUsuario"));
                respuesta.setFechaHoraAltaPeticion(rs.getTimestamp("fechaHoraAltaPeticion"));
                respuesta.setNombreProcesoJob(rs.getString("nombreProcesoJob"));
                respuesta.setNombreUsuario(rs.getString("nombreUsuario"));
                respuesta.setNumeroTarea(rs.getInt("numeroTarea"));
            }
        } catch (NumberFormatException ne) {
            mensajeGestionError = "NumberFormatException Al parsear alguno de los parametros recibidos - getMeLanbide43JobsTareaEje:  " + ne.getMessage();
            log.error(mensajeGestionError, ne);
            throw new Exception(mensajeGestionError);
        } catch (SQLException sqle) {
            mensajeGestionError = "SQLException : " + sqle.getErrorCode() + " " + sqle.getSQLState() + " " + sqle.getMessage();
            log.error(mensajeGestionError, sqle);
            throw new Exception(mensajeGestionError);
        } catch (Exception e) {
            log.error("Error Generico al getMeLanbide43JobsTareaEje: " + e.getMessage(), e);
            throw e;
        } finally {
            try {
                if (ps != null && !ps.isClosed()) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                log.error("SQLException cerrando el PreparedStatement : " + +e.getErrorCode() + " " + e.getSQLState() + " " + e.getMessage(), e);
            } catch (Exception e) {
                log.error("Error cerrando el PreparedStatement : " + e.getMessage(), e);
            }
        }
        return respuesta;        
    }
    
    public int getNumeroTareaProcesar(String nombreProcesoJob, Connection con) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        int respuesta = 0;
        try {
            String query = "SELECT * "
                    + " FROM MELANBIDE43_JOBS_TAREA_EJE "
                    + " WHERE nombreProcesoJob=? ";
            log.debug("sql = " + query);
            ps = con.prepareStatement(query);
            log.debug("param: "
                    + " nombreProcesoJob:" + nombreProcesoJob
            );
            int contador = 1;
            ps.setString(contador++, nombreProcesoJob);
            rs = ps.executeQuery();
            if (rs.next()) { // Por BD 1 Linea Por Job
                respuesta = rs.getInt("numeroExpediente");
            }
        } catch (SQLException ex) {
            log.error("SQLException getNumeroTareaProcesar JOB : getErrorCode" + ex.getErrorCode() + " getMessage:" + ex.getMessage() + " getSQLState:" + ex.getSQLState(), ex);
            throw new Exception("ErrorCode: " + ex.getErrorCode() + " Message:" + ex.getMessage() + " SQLState:" + ex.getSQLState());
        } catch (Exception ex) {
            log.error("Se ha producido un error getNumeroTareaProcesar JOB : " + ex.getMessage(), ex);
            throw ex;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                log.error("SQLException cerrando el statement y el resulset getNumeroTareaProcesar JOB : getErrorCode" + ex.getErrorCode() + " getMessage:" + ex.getMessage() + " getSQLState:" + ex.getSQLState(), ex);
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset getNumeroTareaProcesar - JOB - " + e.getMessage(), e);
            }
        }
        return respuesta;
    }
    
}
