/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide43.dao;

import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.MeLanbide43JobsEjeInf;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author INGDGC
 */
public class MeLanbide43JobsEjeInfDAO {
    
    private Logger log = LogManager.getLogger(this.getClass());
    
    public MeLanbide43JobsEjeInf getById(long id,Connection con) throws Exception{
        PreparedStatement ps = null;
        ResultSet rs = null;
        MeLanbide43JobsEjeInf respuesta = new MeLanbide43JobsEjeInf();
        String mensajeGestionError = "";
        try {
            String query = "SELECT * "
                    + " FROM MELANBIDE43_JOBS_EJE_INF "
                    + " WHERE id=? ";
            log.debug("sql = " + query);
            ps = con.prepareStatement(query);
            log.debug("param: "
                    + " id:" + id
            );
            int contador = 1;
            ps.setLong(contador++, id);
            rs = ps.executeQuery();
            if (rs.next()) { 
                respuesta.setId(rs.getLong("id"));
                respuesta.setNumeroTarea(rs.getInt("numeroTarea"));
                respuesta.setEjercicio(rs.getInt("ejercicio"));
                respuesta.setProcedimiento(rs.getString("procedimiento"));
                respuesta.setNumeroExpediente(rs.getString("numeroExpediente"));
                respuesta.setNombreProcesoJob(rs.getString("nombreProcesoJob"));
                respuesta.setFechaHoraAltaPeticion(rs.getTimestamp("fechaHoraAltaPeticion"));
                respuesta.setFechaHoraModificaPeticion(rs.getTimestamp("fechaHoraModificaPeticion"));
                respuesta.setProcesado(rs.getInt("procesado"));
                respuesta.setResultado(rs.getString("resultado"));
                respuesta.setDetalles(rs.getString("detalles"));
            }
        } catch (NumberFormatException ne) {
            mensajeGestionError = "NumberFormatException Al parsear alguno de los parametros recibidos - getById:  " + ne.getMessage();
            log.error(mensajeGestionError, ne);
            throw new Exception(mensajeGestionError);
        } catch (SQLException sqle) {
            mensajeGestionError = "SQLException : " + sqle.getErrorCode() + " " + sqle.getSQLState() + " " + sqle.getMessage();
            log.error(mensajeGestionError, sqle);
            throw new Exception(mensajeGestionError);
        } catch (Exception e) {
            log.error("Error Generico al getById: " + e.getMessage(), e);
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
    
    public MeLanbide43JobsEjeInf updateById(MeLanbide43JobsEjeInf data,Connection con) throws Exception{
        PreparedStatement ps = null;
        ResultSet rs = null;
        MeLanbide43JobsEjeInf respuesta = new MeLanbide43JobsEjeInf();
        String mensajeGestionError = "";
        try {
            if(data!=null && data.getId()>0){
                String query = "update MELANBIDE43_JOBS_EJE_INF "
                        + " SET "
                        + " procesado=? "
                        + " ,resultado=? "
                        + " ,detalles=? "
                        + " ,fechaHoraModificaPeticion=? "
                        + " WHERE id=? ";
                log.debug("sql = " + query);
                ps = con.prepareStatement(query);
                log.debug("param: "
                        + " procesado=? " + data.getProcesado()
                        + " ,resultado=? " + data.getResultado()
                        + " ,detalles=? " + data.getDetalles()
                        + " ,fechaHoraModificaPeticion=? " + new Timestamp(new Date().getTime())
                        + " id=? " + data.getId()
                );
                int contador = 1;
                ps.setInt(contador++, data.getProcesado());
                if(data.getResultado()!=null && !data.getResultado().isEmpty())
                    ps.setString(contador++, data.getResultado());
                else
                    ps.setNull(contador++,Types.VARCHAR);
                if(data.getDetalles()!=null && !data.getDetalles().isEmpty())
                    ps.setString(contador++, data.getDetalles());
                else
                    ps.setNull(contador++,Types.VARCHAR);
                ps.setTimestamp(contador++,new Timestamp(new Date().getTime()));
                ps.setLong(contador++,data.getId());
                if(ps.executeUpdate()>0){
                    respuesta=getById(data.getId(), con);
                }else{
                    log.error("NO se ha poddo realizar la actualizacion con el ID recibido " + data.getId());
                }
            }else{
                log.error("NO se hace actualizacion no se recibe ID...");
            }
            
        } catch (NumberFormatException ne) {
            mensajeGestionError = "NumberFormatException Al parsear alguno de los parametros recibidos - updateById:  " + ne.getMessage();
            log.error(mensajeGestionError, ne);
            throw new Exception(mensajeGestionError);
        } catch (SQLException sqle) {
            mensajeGestionError = "SQLException : " + sqle.getErrorCode() + " " + sqle.getSQLState() + " " + sqle.getMessage();
            log.error(mensajeGestionError, sqle);
            throw new Exception(mensajeGestionError);
        } catch (Exception e) {
            log.error("Error Generico al getById: " + e.getMessage(), e);
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
    
    public List<MeLanbide43JobsEjeInf> getExpedientesProcesarByNumeroTarea(int numeroTarea,Connection con) throws Exception{
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<MeLanbide43JobsEjeInf> respuesta = new ArrayList<MeLanbide43JobsEjeInf>();
        String mensajeGestionError = "";
        try {
            String query = "SELECT * "
                    + " FROM MELANBIDE43_JOBS_EJE_INF "
                    + " WHERE procesado=0 "
                    + " AND numeroTarea=? ";
            log.debug("sql = " + query);
            ps = con.prepareStatement(query);
            log.debug("param: "
                    + " numeroTarea:" + numeroTarea
            );
            int contador = 1;
            ps.setInt(contador++, numeroTarea);
            rs = ps.executeQuery();
            while (rs.next()) { 
                MeLanbide43JobsEjeInf meLanbide43JobsEjeInf = new MeLanbide43JobsEjeInf();
                meLanbide43JobsEjeInf.setId(rs.getLong("id"));
                meLanbide43JobsEjeInf.setNumeroTarea(rs.getInt("numeroTarea"));
                meLanbide43JobsEjeInf.setEjercicio(rs.getInt("ejercicio"));
                meLanbide43JobsEjeInf.setProcedimiento(rs.getString("procedimiento"));
                meLanbide43JobsEjeInf.setNumeroExpediente(rs.getString("numeroExpediente"));
                meLanbide43JobsEjeInf.setNombreProcesoJob(rs.getString("nombreProcesoJob"));
                meLanbide43JobsEjeInf.setFechaHoraAltaPeticion(rs.getTimestamp("fechaHoraAltaPeticion"));
                meLanbide43JobsEjeInf.setFechaHoraModificaPeticion(rs.getTimestamp("fechaHoraModificaPeticion"));
                meLanbide43JobsEjeInf.setProcesado(rs.getInt("procesado"));
                meLanbide43JobsEjeInf.setResultado(rs.getString("resultado"));
                meLanbide43JobsEjeInf.setDetalles(rs.getString("detalles"));
                respuesta.add(meLanbide43JobsEjeInf);
            }
        } catch (NumberFormatException ne) {
            mensajeGestionError = "NumberFormatException Al parsear alguno de los parametros recibidos - getExpedientesProcesarByNumeroTarea:  " + ne.getMessage();
            log.error(mensajeGestionError, ne);
            throw new Exception(mensajeGestionError);
        } catch (SQLException sqle) {
            mensajeGestionError = "SQLException : " + sqle.getErrorCode() + " " + sqle.getSQLState() + " " + sqle.getMessage();
            log.error(mensajeGestionError, sqle);
            throw new Exception(mensajeGestionError);
        } catch (Exception e) {
            log.error("Error Generico al getById: " + e.getMessage(), e);
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
    
}
