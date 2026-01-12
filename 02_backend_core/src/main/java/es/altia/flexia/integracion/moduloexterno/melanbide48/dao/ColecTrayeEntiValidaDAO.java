/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide48.dao;

import es.altia.flexia.integracion.moduloexterno.melanbide48.util.MeLanbide48MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide48.vo.ColecTrayeEntiValida;
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
public class ColecTrayeEntiValidaDAO {
    
    private final SimpleDateFormat formatFechaLog = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    private final Logger log = LogManager.getLogger(MeLanbideConvocatoriasDAO.class);
    
    public ColecTrayeEntiValida getColecTrayeEntiValidaByID(int id,Connection con) throws SQLException, Exception{
        log.info(" getColecTrayeEntiValidaByID - Begin " + id + formatFechaLog.format(new Date()));
        ColecTrayeEntiValida resultado = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            int contadorParam = 1;
            String query = "select * "
                    + " from COLEC_TRAYE_ENTI_VALIDA "
                    + " where "
                    + " id=? ";
            log.info("sql = " + query);
            ps = con.prepareStatement(query);
            ps.setLong(contadorParam++, id);
            log.info("params = " + id);
            rs = ps.executeQuery();
            while (rs.next()) {
                resultado = (ColecTrayeEntiValida)MeLanbide48MappingUtils.getInstance().map(rs, ColecTrayeEntiValida.class);
            }
        } catch (SQLException e) {
            log.error("Se ha producido recuperando datos Valoracion Ubicacion ", e);
            throw e;
        } catch (Exception e) {
            log.error("Se ha producido recuperando datos Valoracion Ubicacion ", e);
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
        log.info(" getColecTrayeEntiValidaByID - End " + id + formatFechaLog.format(new Date()));
        return resultado;       
    }
    
    public List<ColecTrayeEntiValida> getColecTrayeEntiValidaByNumExpedienteCodEntidad(String numeroExpediente,int codEntidad, Connection con) throws SQLException, Exception {
        log.info(" getColecTrayeEntiValidaByID - Begin " + numeroExpediente + formatFechaLog.format(new Date()));
        List<ColecTrayeEntiValida> resultado = new ArrayList<ColecTrayeEntiValida>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            int contadorParam = 1;
            String query = "select * "
                    + " from COLEC_TRAYE_ENTI_VALIDA "
                    + " where "
                    + " numeroExpediente=? "
                    + " and idFkEntidad=? ";
            log.info("sql = " + query);
            ps = con.prepareStatement(query);
            ps.setString(contadorParam++, numeroExpediente);
            ps.setInt(contadorParam++, codEntidad);
            log.info("params = " + numeroExpediente +" "+codEntidad);
            rs = ps.executeQuery();
            while (rs.next()) {
                resultado.add((ColecTrayeEntiValida) MeLanbide48MappingUtils.getInstance().map(rs, ColecTrayeEntiValida.class));
            }
        } catch (SQLException e) {
            log.error("Se ha producido recuperando datos Valoracion Ubicacion ", e);
            throw e;
        } catch (Exception e) {
            log.error("Se ha producido recuperando datos Valoracion Ubicacion ", e);
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
        log.info(" getColecTrayeEntiValidaByID - End " + numeroExpediente + formatFechaLog.format(new Date()));
        return resultado;
    }
    
    public ColecTrayeEntiValida getColecTrayeEntiValidaByEntidadColectivo(int codEntidad,int codColectivo, Connection con) throws SQLException, Exception {
        log.info(" getColecTrayeEntiValidaByEntidadColectivo - Begin " + codColectivo + " " + codEntidad + " "+formatFechaLog.format(new Date()));
        ColecTrayeEntiValida resultado = new ColecTrayeEntiValida();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            int contadorParam = 1;
            String query = "select * "
                    + " from COLEC_TRAYE_ENTI_VALIDA "
                    + " where "
                    + " idFkEntidad=? "
                    + " and idFkColectivo=? ";
            log.info("sql = " + query);
            ps = con.prepareStatement(query);
            ps.setInt(contadorParam++, codEntidad);
            ps.setInt(contadorParam++, codColectivo);
            log.info("params = " + codEntidad +" "+codColectivo);
            rs = ps.executeQuery();
            while (rs.next()) {
                resultado=(ColecTrayeEntiValida) MeLanbide48MappingUtils.getInstance().map(rs, ColecTrayeEntiValida.class);
            }
        } catch (SQLException e) {
            log.error("Se ha producido recuperando datos Valoracion Ubicacion ", e);
            throw e;
        } catch (Exception e) {
            log.error("Se ha producido recuperando datos Valoracion Ubicacion ", e);
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
        log.info(" getColecTrayeEntiValidaByID - End " + codEntidad + " "+codColectivo  + " " +formatFechaLog.format(new Date()));
        return resultado;
    }
    
    public boolean guardarColecTrayeEntiValida(ColecTrayeEntiValida colecTrayeEntiValida, Connection con) throws SQLException, Exception {
        log.info(" guardarColecTrayeEntiValida - Begin " + formatFechaLog.format(new Date()));
        boolean resultado = false;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            if (colecTrayeEntiValida != null) {
                String query = "";
                int contadorParam = 1;
                if (colecTrayeEntiValida.getId() != null) {

                    query = " update COLEC_TRAYE_ENTI_VALIDA "
                            + " set "
                            + " numeroExpediente=? "
                            + " ,idFkEntidad=? "
                            + " ,idFkColectivo =? "
                            + " ,numeroMesesValidados =? "
                            + " where "
                            + " id=? ";
                    ps = con.prepareStatement(query);
                    ps.setString(contadorParam++, colecTrayeEntiValida.getNumeroExpediente());
                    ps.setInt(contadorParam++, colecTrayeEntiValida.getIdFkEntidad());
                    ps.setInt(contadorParam++, colecTrayeEntiValida.getIdFkColectivo());
                    if (colecTrayeEntiValida.getNumeroMesesValidados() != null) {
                        ps.setDouble(contadorParam++, colecTrayeEntiValida.getNumeroMesesValidados());
                    } else {
                        ps.setNull(contadorParam++, java.sql.Types.NULL);
                    }
                    ps.setInt(contadorParam++, colecTrayeEntiValida.getId());
                } else {
                    query = " insert into COLEC_TRAYE_ENTI_VALIDA "
                            + "("
                            + " id "
                            + " ,numeroExpediente "
                            + " ,idFkEntidad "
                            + " ,idFkColectivo  "
                            + " ,numeroMesesValidados  "
                            + ")"
                            + " values"
                            + "("
                            + " SEQ_COLEC_TRAYE_ENTI_VALIDA.nextval "
                            + ",?,?,?,? "
                            + ") ";
                    ps = con.prepareStatement(query);
                    ps.setString(contadorParam++, colecTrayeEntiValida.getNumeroExpediente());
                    ps.setInt(contadorParam++, colecTrayeEntiValida.getIdFkEntidad());
                    ps.setInt(contadorParam++, colecTrayeEntiValida.getIdFkColectivo());
                    if (colecTrayeEntiValida.getNumeroMesesValidados()!= null) {
                        ps.setDouble(contadorParam++, colecTrayeEntiValida.getNumeroMesesValidados());
                    } else {
                        ps.setNull(contadorParam++, java.sql.Types.NULL);
                    }
                }
                log.info("sql = " + query);
                log.info("params = " + colecTrayeEntiValida.toString());
                resultado = ps.executeUpdate()>0;
                if ( resultado) {
                    log.info("Ejecucion Correcta...");
                } else {
                    log.error("Datos no insertados o actualizados....!!!");
                }
            } else {
                log.info("Objeto  a insertar/modificar recibido a null, no se procesa, respuesta devuelta : null");
            }
        } catch (SQLException e) {
            log.error("Se ha producido Guardando Datos - Validacion Tarayectoria ", e);
            throw e;
        } catch (Exception e) {
            log.error("Se ha producido Guardando Datos - Validacion Trayectoria ", e);
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
        log.info(" guardarColecTrayeEntiValida - End " + formatFechaLog.format(new Date()));
        return resultado;
    }
}
