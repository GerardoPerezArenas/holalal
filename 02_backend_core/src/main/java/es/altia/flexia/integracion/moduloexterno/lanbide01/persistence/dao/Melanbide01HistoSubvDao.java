/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.lanbide01.persistence.dao;

import es.altia.flexia.integracion.moduloexterno.lanbide01.vo.Melanbide01HistoSubv;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
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
public class Melanbide01HistoSubvDao {
    
     // Formateador de Fecha RegistroLog
    SimpleDateFormat formatFechaLog = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    Logger log = LogManager.getLogger(Melanbide01HistoSubvDao.class);
    
    public List<Melanbide01HistoSubv> getTodoHitorialSubvencionExpediente(String numeroExpediente, Connection con) throws SQLException, Exception{
        log.info(" getTodoHitorialSubvencionExpediente - Begin "+ numeroExpediente + formatFechaLog.format(new Date()));
        List<Melanbide01HistoSubv> resultadoList = new ArrayList<Melanbide01HistoSubv>();
        String query="";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            query= "select * "
                    + " from MELANBIDE01_HISTO_SUBV "
                    + " where "
                    + " numeroExpediente=?"
                    ;
            log.info("sql = " + query);
            ps = con.prepareStatement(query);
            ps.setString(1, numeroExpediente);            
            log.info("params = " + numeroExpediente);
            rs=ps.executeQuery();
            while (rs.next()) {                
                Melanbide01HistoSubv resultado = new Melanbide01HistoSubv();
                resultado.setId(rs.getLong("id"));
                resultado.setNumeroExpediente(rs.getString("numeroExpediente"));
                resultado.setFechaFinInterrupSituacion(rs.getDate("fechaFinInterrupSituacion"));
                resultado.setFechaProrrReanudSituacion(rs.getDate("fechaProrrReanudSituacion"));
                resultado.setFechaHoraRegistro(rs.getTimestamp("fechaHoraRegistro"));
                resultado.setFechaHoraModificacion(rs.getTimestamp("fechaHoraModificacion"));
                resultadoList.add(resultado);
            }
        }catch (SQLException e) {
            log.error("Se ha producido recuperando datos historial ", e);            
            throw e;
        }catch (Exception e) {
            log.error("Se ha producido recuperando datos historial ", e);            
            throw e;
        }finally{
            if(log.isDebugEnabled()) log.debug("Procedemos a cerrar el resultset");
            if(ps!=null) ps.close();
            if(rs!=null) rs.close();
        }
        log.info(" getTodoHitorialSubvencionExpediente - End "+ numeroExpediente + formatFechaLog.format(new Date()));
        return resultadoList;
    }
    
    public Melanbide01HistoSubv getLineaHistorialSubvencionById(Long id, Connection con) throws SQLException, Exception{
        log.info(" getHistorialSubvencionByIdLinea - Begin "+ id + formatFechaLog.format(new Date()));
        Melanbide01HistoSubv resultado = new Melanbide01HistoSubv();
        String query="";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            query= "select * "
                    + " from MELANBIDE01_HISTO_SUBV "
                    + " where "
                    + " id=? "
                    ;
            log.info("sql = " + query);
            ps = con.prepareStatement(query);
            ps.setLong(1, id);            
            log.info("params = " + id);
            rs=ps.executeQuery();
           while (rs.next()) {                
                resultado.setId(rs.getLong("id"));
                resultado.setNumeroExpediente(rs.getString("numeroExpediente"));
                resultado.setFechaFinInterrupSituacion(rs.getDate("fechaFinInterrupSituacion"));
                resultado.setFechaProrrReanudSituacion(rs.getDate("fechaProrrReanudSituacion"));
                resultado.setFechaHoraRegistro(rs.getTimestamp("fechaHoraRegistro"));
                resultado.setFechaHoraModificacion(rs.getTimestamp("fechaHoraModificacion"));
            }
        }catch (SQLException e) {
            log.error("Se ha producido recuperando datos historial ", e);            
            throw e;
        }catch (Exception e) {
            log.error("Se ha producido recuperando datos historial ", e);            
            throw e;
        }finally{
            if(log.isDebugEnabled()) log.debug("Procedemos a cerrar el resultset");
            if(ps!=null) ps.close();
            if(rs!=null) rs.close();
        }
        log.info(" getHistorialSubvencionByIdLinea - End "+ id + formatFechaLog.format(new Date()));
        return resultado;
    }
    
    public void agregarNuevaLineaHistorialSubvencion(Melanbide01HistoSubv dato, Connection con) throws SQLException, Exception{
        log.info(" agregarNuevaLineaHistorialSubvencion - Begin " + formatFechaLog.format(new Date()));
        String query="";
        PreparedStatement ps = null;
        try{
            if(dato!=null){
                query = "insert into "
                        + " MELANBIDE01_HISTO_SUBV "
                        + " (id,numeroExpediente,fechaFinInterrupSituacion,fechaProrrReanudSituacion) "
                        + " values (SEQ_MELANBIDE01_HISTO_SUBV.NEXTVAL,?,?,?) ";
                log.info("sql = " + query);
                ps = con.prepareStatement(query);
                int paramCounter=1;
                ps.setString(paramCounter++, dato.getNumeroExpediente());
                if(dato.getFechaFinInterrupSituacion()!=null){
                    ps.setDate(paramCounter++, new java.sql.Date(dato.getFechaFinInterrupSituacion().getTime()));
                }else
                    ps.setNull(paramCounter++, Types.DATE);
                if(dato.getFechaProrrReanudSituacion()!=null){
                    ps.setDate(paramCounter++, new java.sql.Date(dato.getFechaProrrReanudSituacion().getTime()));
                }else
                    ps.setNull(paramCounter++, Types.DATE);
                log.info("params = " + dato.getNumeroExpediente() + "-" + dato.getFechaFinInterrupSituacion()+ "-" + dato.getFechaProrrReanudSituacion()
                );
                log.info(" filas insertadas "
                        + ps.executeUpdate()
                );
            }else{
                log.error("Objeto a insertar en BD recibido a null ");
            }
        }catch (SQLException e) {
            log.error("Se ha producido insertando datos Historial ", e);            
            throw e;
        }catch (Exception e) {
            log.error("Se ha producido insertando datos Historial ", e);            
            throw e;
        }finally{
            if(log.isDebugEnabled()) log.debug("Procedemos a cerrar el resultset");
            if(ps!=null) ps.close();
        }
        log.info(" agregarNuevaLineaHistorialSubvencion - End "+ formatFechaLog.format(new Date()));
    }
    
    public void actualizarDatosLineaHistorialSubvencion(Melanbide01HistoSubv dato, Connection con) throws SQLException, Exception{
        log.info(" actualizarDatosLineaHistorialSubvencion - Begin " + formatFechaLog.format(new Date()));
        String query="";
        PreparedStatement ps = null;
        try{
            if(dato!=null){
                query = "update "
                        + " MELANBIDE01_HISTO_SUBV "
                        + " set "
                        + " fechaFinInterrupSituacion=? "
                        + " ,fechaProrrReanudSituacion=? "
                        + " ,fechaHoraModificacion=SYSTIMESTAMP "
                        + " where "
                        + " id=? "
                        ;
                log.info("sql = " + query);
                ps = con.prepareStatement(query);
                int paramCounter=1;
                if(dato.getFechaFinInterrupSituacion()!=null){
                    ps.setDate(paramCounter++, new java.sql.Date(dato.getFechaFinInterrupSituacion().getTime()));
                }else
                    ps.setNull(paramCounter++, Types.DATE);
                if(dato.getFechaProrrReanudSituacion()!=null){
                    ps.setDate(paramCounter++, new java.sql.Date(dato.getFechaProrrReanudSituacion().getTime()));
                }else
                    ps.setNull(paramCounter++, Types.DATE);
                if(dato.getId()!=null){
                    ps.setLong(paramCounter++, dato.getId());
                }else
                    ps.setNull(paramCounter++, Types.BIGINT);
                log.info("params = " + dato.getFechaFinInterrupSituacion()+ "-" + dato.getFechaProrrReanudSituacion() 
                        + " - " + dato.getId()
                );
                log.info(" filas ACTUALIZADAS  "
                        + ps.executeUpdate()
                );
            }else{
                log.error("Objeto a MODIFICAR en BD recibido a null ");
            }
        }catch (SQLException e) {
            log.error("Se ha producido actualizando datos Historial ", e);            
            throw e;
        }catch (Exception e) {
            log.error("Se ha producido actualizado datos Historial ", e);            
            throw e;
        }finally{
            if(log.isDebugEnabled()) log.debug("Procedemos a cerrar el resultset");
            if(ps!=null) ps.close();
        }
        log.info(" actualizarDatosLineaHistorialSubvencion - End "+ formatFechaLog.format(new Date()));
    }
    
    public void eliminarFilaHistorialSubvencion(Long dato, Connection con) throws SQLException, Exception{
        log.info(" eliminarFilaHistorialSubvencion - Begin " + formatFechaLog.format(new Date()));
        String query="";
        PreparedStatement ps = null;
        try{
            if(dato!=null){
                query = "delete "
                        + " MELANBIDE01_HISTO_SUBV "
                        + " where "
                        + " id=? "
                        ;
                log.info("sql = " + query);
                ps = con.prepareStatement(query);
                int paramCounter=1;
                ps.setLong(paramCounter++, dato);
                log.info("params = " + dato
                );
                log.info(" filas ELIMINADAS  "
                        + ps.executeUpdate()
                );
            }else{
                log.error("Id Objeto a ELIMINAR en BD recibido a null ");
            }
        }catch (SQLException e) {
            log.error("Se ha producido error  al eliminarFilaHistorialSubvencion datos Causantes ", e);            
            throw e;
        }catch (Exception e) {
            log.error("Se ha producido error al  eliminarFilaHistorialSubvencion datos Causantes ", e);            
            throw e;
        }finally{
            if(log.isDebugEnabled()) log.debug("Procedemos a cerrar el resultset");
            if(ps!=null) ps.close();
        }
        log.info(" eliminarFilaHistorialSubvencion - End "+ formatFechaLog.format(new Date()));
    }

    
}
