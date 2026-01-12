/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.lanbide01.persistence.dao;

import es.altia.flexia.integracion.moduloexterno.lanbide01.vo.Melanbide01DepenPerSut;
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
public class Melanbide01DepenPerSutDao {
    
    // Formateador de Fecha RegistroLog
    SimpleDateFormat formatFechaLog = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    Logger log = LogManager.getLogger(Melanbide01DepenPerSutDao.class);
    
    
    public List<Melanbide01DepenPerSut> getTodosCausantesSubvencion(String numeroExpediente, Connection con) throws SQLException, Exception{
        log.info(" getTodosCausantesSubvencion - Begin "+ numeroExpediente + formatFechaLog.format(new Date()));
        List<Melanbide01DepenPerSut> resultadoList = new ArrayList<Melanbide01DepenPerSut>();
        String query="";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            query= "select MELANBIDE01_DEPEN_PERSUT.* "
                    + " ,t_tid.tid_des tipoDocumentoDesc,e_des_val.des_nom tipoDependienteDesc "
                    + " from MELANBIDE01_DEPEN_PERSUT "
                    + " left join t_tid on t_tid.tid_cod=melanbide01_depen_persut.tipodocumento "
                    + " LEFT JOIN e_des_val ON e_des_val.des_cod='PEDE' AND e_des_val.des_val_cod=melanbide01_depen_persut.tipodependiente "
                    + " where "
                    + " numeroExpediente=?"
                    + " order by melanbide01_depen_persut.fechanacimiento "
                    ;
            log.info("sql = " + query);
            ps = con.prepareStatement(query);
            ps.setString(1, numeroExpediente);            
            log.info("params = " + numeroExpediente);
            rs=ps.executeQuery();
            while (rs.next()) {                
                Melanbide01DepenPerSut resultado = new Melanbide01DepenPerSut();
                resultado.setId(rs.getLong("id"));
                resultado.setNumeroExpediente(rs.getString("numeroExpediente"));
                resultado.setCorrelativo(rs.getString("correlativo"));
                resultado.setTipoDependiente(rs.getInt("tipoDependiente"));
                resultado.setParentezco(rs.getString("parentezco"));
                resultado.setNombre(rs.getString("nombre"));
                resultado.setApellidos(rs.getString("apellidos"));
                resultado.setTipoDocumento(rs.getString("tipoDocumento"));
                resultado.setNumeroDocumento(rs.getString("numeroDocumento"));
                resultado.setFechaNacimiento(rs.getDate("fechaNacimiento"));
                resultado.setEsMinusvalido(rs.getString("esMinusvalido"));
                resultado.setPorcentajeMinusvalia(rs.getDouble("porcentajeMinusvalia"));
                resultado.setFechaHoraRegistro(rs.getTimestamp("fechaHoraRegistro"));
                resultado.setFechaHoraModificacion(rs.getTimestamp("fechaHoraModificacion"));
                resultado.setTipoDocumentoDesc(rs.getString("tipoDocumentoDesc"));
                resultado.setTipoDependienteDesc(rs.getString("tipoDependienteDesc"));
                resultadoList.add(resultado);
            }
        }catch (SQLException e) {
            log.error("Se ha producido recuperando datos Causantes ", e);            
            throw e;
        }catch (Exception e) {
            log.error("Se ha producido recuperando datos Causantes ", e);            
            throw e;
        }finally{
            if(log.isDebugEnabled()) log.debug("Procedemos a cerrar el resultset");
            if(ps!=null) ps.close();
            if(rs!=null) rs.close();
        }
        log.info(" getTodosCausantesSubvencion - End "+ numeroExpediente + formatFechaLog.format(new Date()));
        return resultadoList;
    }
    
    public Melanbide01DepenPerSut getCausantesSubvencionById(Long id, Connection con) throws SQLException, Exception{
        log.info(" getCausantesSubvencionById - Begin "+ id + formatFechaLog.format(new Date()));
        Melanbide01DepenPerSut resultado = new Melanbide01DepenPerSut();
        String query="";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            query= "select MELANBIDE01_DEPEN_PERSUT.* "
                    + " ,t_tid.tid_des tipoDocumentoDesc,e_des_val.des_nom tipoDependienteDesc "
                    + " from MELANBIDE01_DEPEN_PERSUT "
                    + " left join t_tid on t_tid.tid_cod=melanbide01_depen_persut.tipodocumento "
                    + " LEFT JOIN e_des_val ON e_des_val.des_cod='PEDE' AND e_des_val.des_val_cod=melanbide01_depen_persut.tipodependiente "
                    + " where "
                    + " id=? "
                    + " order by melanbide01_depen_persut.fechanacimiento "
                    ;
            log.info("sql = " + query);
            ps = con.prepareStatement(query);
            ps.setLong(1, id);            
            log.info("params = " + id);
            rs=ps.executeQuery();
            while (rs.next()) {                
                resultado.setId(rs.getLong("id"));
                resultado.setNumeroExpediente(rs.getString("numeroExpediente"));
                resultado.setCorrelativo(rs.getString("correlativo"));
                resultado.setTipoDependiente(rs.getInt("tipoDependiente"));
                resultado.setParentezco(rs.getString("parentezco"));
                resultado.setNombre(rs.getString("nombre"));
                resultado.setApellidos(rs.getString("apellidos"));
                resultado.setTipoDocumento(rs.getString("tipoDocumento"));
                resultado.setNumeroDocumento(rs.getString("numeroDocumento"));
                resultado.setFechaNacimiento(rs.getDate("fechaNacimiento"));
                resultado.setEsMinusvalido(rs.getString("esMinusvalido"));
                resultado.setPorcentajeMinusvalia(rs.getDouble("porcentajeMinusvalia"));
                resultado.setFechaHoraRegistro(rs.getTimestamp("fechaHoraRegistro"));
                resultado.setFechaHoraModificacion(rs.getTimestamp("fechaHoraModificacion"));
                resultado.setTipoDocumentoDesc(rs.getString("tipoDocumentoDesc"));
                resultado.setTipoDependienteDesc(rs.getString("tipoDependienteDesc"));
            }
        }catch (SQLException e) {
            log.error("Se ha producido recuperando datos Causantes ", e);            
            throw e;
        }catch (Exception e) {
            log.error("Se ha producido recuperando datos Causantes ", e);            
            throw e;
        }finally{
            if(log.isDebugEnabled()) log.debug("Procedemos a cerrar el resultset");
            if(ps!=null) ps.close();
            if(rs!=null) rs.close();
        }
        log.info(" getCausantesSubvencionById - End "+ id + formatFechaLog.format(new Date()));
        return resultado;
    }
    
    public void agregarNuevoCausante(Melanbide01DepenPerSut dato, Connection con) throws SQLException, Exception{
        log.info(" agregarNuevoCausante - Begin " + formatFechaLog.format(new Date()));
        String query="";
        PreparedStatement ps = null;
        try{
            if(dato!=null){
                query = "insert into "
                        + " MELANBIDE01_DEPEN_PERSUT "
                        + " (id,numeroExpediente,correlativo,tipoDependiente,parentezco,nombre,apellidos,tipoDocumento,numeroDocumento,fechaNacimiento,esMinusvalido,porcentajeMinusvalia) "
                        + " values (SEQ_MELANBIDE01_DEPEN_PERSUT.NEXTVAL,?,?,?,?,?,?,?,?,?,?,?) ";
                log.info("sql = " + query);
                ps = con.prepareStatement(query);
                int paramCounter=1;
                ps.setString(paramCounter++, dato.getNumeroExpediente());
                ps.setString(paramCounter++, dato.getCorrelativo());
                if(dato.getTipoDependiente()!=null){
                    ps.setInt(paramCounter++, dato.getTipoDependiente());
                }else
                    ps.setNull(paramCounter++, Types.INTEGER);
                ps.setString(paramCounter++, dato.getParentezco());
                ps.setString(paramCounter++, dato.getNombre());
                ps.setString(paramCounter++, dato.getApellidos());
                ps.setString(paramCounter++, dato.getTipoDocumento());
                ps.setString(paramCounter++, dato.getNumeroDocumento());
                if(dato.getFechaNacimiento()!=null){
                    ps.setDate(paramCounter++, new java.sql.Date(dato.getFechaNacimiento().getTime()));
                }else
                    ps.setNull(paramCounter++, java.sql.Types.DATE);
                ps.setString(paramCounter++, dato.getEsMinusvalido());
                if(dato.getPorcentajeMinusvalia()!=null){
                    ps.setDouble(paramCounter++, dato.getPorcentajeMinusvalia());
                }else
                    ps.setNull(paramCounter++, Types.DOUBLE);
                log.info("params = " + dato.getId() + "-" + dato.getNumeroExpediente() + "-" + dato.getCorrelativo() + "-" + dato.getTipoDependiente() + "-" + dato.getParentezco() + "-"
                        + dato.getNombre() + "-" + dato.getApellidos() + "-" + dato.getTipoDocumento() + "-" + dato.getNumeroDocumento() + "-" + dato.getFechaNacimiento() + "-"
                        + dato.getEsMinusvalido() + "-" + dato.getPorcentajeMinusvalia()
                );
                log.info(" filas insertadas "
                        + ps.executeUpdate()
                );
            }else{
                log.error("Objeto a insertar en BD recibido a null ");
            }
        }catch (SQLException e) {
            log.error("Se ha producido insertando datos Causantes ", e);            
            throw e;
        }catch (Exception e) {
            log.error("Se ha producido insertando datos Causantes ", e);            
            throw e;
        }finally{
            if(log.isDebugEnabled()) log.debug("Procedemos a cerrar el resultset");
            if(ps!=null) ps.close();
        }
        log.info(" agregarNuevoCausante - End "+ formatFechaLog.format(new Date()));
    }
    
    public void actualizarDatosCausante(Melanbide01DepenPerSut dato, Connection con) throws SQLException, Exception{
        log.info(" actualizarDatosCausante - Begin " + formatFechaLog.format(new Date()));
        String query="";
        PreparedStatement ps = null;
        try{
            if(dato!=null){
                query = "update "
                        + " MELANBIDE01_DEPEN_PERSUT "
                        + " set "
                        + " correlativo=? "
                        + " ,tipoDependiente=? "
                        + " ,parentezco=? "
                        + " ,nombre=? "
                        + " ,apellidos=? "
                        + " ,tipoDocumento=? "
                        + " ,numeroDocumento=? "
                        + " ,fechaNacimiento=? "
                        + " ,esMinusvalido=? "
                        + " ,porcentajeMinusvalia=? "
                        + " ,fechaHoraModificacion=SYSTIMESTAMP "
                        + " where "
                        + " id=? "
                        ;
                log.info("sql = " + query);
                ps = con.prepareStatement(query);
                int paramCounter=1;
                ps.setString(paramCounter++, dato.getCorrelativo());
                if(dato.getTipoDependiente()!=null){
                    ps.setInt(paramCounter++, dato.getTipoDependiente());
                }else
                    ps.setNull(paramCounter++, Types.INTEGER);
                ps.setString(paramCounter++, dato.getParentezco());
                ps.setString(paramCounter++, dato.getNombre());
                ps.setString(paramCounter++, dato.getApellidos());
                ps.setString(paramCounter++, dato.getTipoDocumento());
                ps.setString(paramCounter++, dato.getNumeroDocumento());
                if(dato.getFechaNacimiento()!=null){
                    ps.setDate(paramCounter++, new java.sql.Date(dato.getFechaNacimiento().getTime()));
                }else
                    ps.setNull(paramCounter++, java.sql.Types.DATE);
                ps.setString(paramCounter++, dato.getEsMinusvalido());
                if(dato.getPorcentajeMinusvalia()!=null){
                    ps.setDouble(paramCounter++, dato.getPorcentajeMinusvalia());
                }else
                    ps.setNull(paramCounter++, Types.DOUBLE);
                if(dato.getId()!=null){
                    ps.setLong(paramCounter++, dato.getId());
                }else
                    ps.setNull(paramCounter++, Types.BIGINT);
                log.info("params = " + dato.getCorrelativo() + "-" + dato.getTipoDependiente() + "-" + dato.getParentezco() + "-"
                        + dato.getNombre() + "-" + dato.getApellidos() + "-" + dato.getTipoDocumento() + "-" + dato.getNumeroDocumento() + "-" + dato.getFechaNacimiento() + "-"
                        + dato.getEsMinusvalido() + "-" + dato.getPorcentajeMinusvalia() + " - " + dato.getId()
                );
                log.info(" filas ACTUALIZADAS  "
                        + ps.executeUpdate()
                );
            }else{
                log.error("Objeto a MODIFICAR en BD recibido a null ");
            }
        }catch (SQLException e) {
            log.error("Se ha producido actualizando datos Causantes ", e);            
            throw e;
        }catch (Exception e) {
            log.error("Se ha producido actualizado datos Causantes ", e);            
            throw e;
        }finally{
            if(log.isDebugEnabled()) log.debug("Procedemos a cerrar el resultset");
            if(ps!=null) ps.close();
        }
        log.info(" actualizarDatosCausante - End "+ formatFechaLog.format(new Date()));
    }
    
    public void eliminarCausante(Long dato, Connection con) throws SQLException, Exception{
        log.info(" eliminarCausante - Begin " + formatFechaLog.format(new Date()));
        String query="";
        PreparedStatement ps = null;
        try{
            if(dato!=null){
                query = "delete "
                        + " MELANBIDE01_DEPEN_PERSUT "
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
            log.error("Se ha producido error  al eliminarCausante datos Causantes ", e);            
            throw e;
        }catch (Exception e) {
            log.error("Se ha producido error al  eliminarCausante datos Causantes ", e);            
            throw e;
        }finally{
            if(log.isDebugEnabled()) log.debug("Procedemos a cerrar el resultset");
            if(ps!=null) ps.close();
        }
        log.info(" eliminarCausante - End "+ formatFechaLog.format(new Date()));
    }


}
