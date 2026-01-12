/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide88.dao;

import es.altia.flexia.integracion.moduloexterno.melanbide88.util.Melanbide88MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide88.vo.Melanbide88Inversiones;
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
public class Melanbide88InversionesDAO {
    
    private static final Logger LOG = LogManager.getLogger(Melanbide88InversionesDAO.class);
    private final SimpleDateFormat formatFechaddMMyyyy = new SimpleDateFormat("dd/MM/yyyy");
    private final SimpleDateFormat formatFechaLog = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private final Melanbide88MappingUtils melanbide88MappingUtils = new Melanbide88MappingUtils();
    
    public Melanbide88Inversiones getMelanbide88InversionesByID(Long id, Connection con) throws SQLException, Exception {
        LOG.info(" getMelanbide88InversionesByID - Begin " + formatFechaLog.format(new Date()));
        Melanbide88Inversiones resultado = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String query = "select * "
                    + " from MELANBIDE88_INVERSIONES "
                    + " where "
                    + " id=? ";
            LOG.info("sql = " + query);
            int params = 1;
            ps = con.prepareStatement(query);
            ps.setLong(params++, id);
            LOG.info("params = "
                    + "" + id
            );
            rs = ps.executeQuery();
            while (rs.next()) {
                resultado = melanbide88MappingUtils.getMelanbide88Inversiones(rs);
            }
        } catch (SQLException e) {
            LOG.error("Se ha producido recuperando getMelanbide88InversionesByID ", e);
            throw e;
        } catch (Exception e) {
            LOG.error("Se ha producido recuperando getMelanbide88InversionesByID ", e);
            throw e;
        } finally {
            LOG.debug("Procedemos a cerrar el resultset");
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        LOG.info(" getMelanbide88InversionesByID - End " + id + formatFechaLog.format(new Date()));
        return resultado;
    }

    public List<Melanbide88Inversiones> getMelanbide88InversionesByNumExp(String numExp, Connection con) throws SQLException, Exception {
        LOG.info(" getMelanbide88InversionesByNumExp - Begin " + formatFechaLog.format(new Date()));
        List<Melanbide88Inversiones> resultado = new ArrayList<Melanbide88Inversiones>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String query = "select * "
                    + " from MELANBIDE88_INVERSIONES "
                    + " where "
                    + " num_exp=? ";
            LOG.info("sql = " + query);
            int params = 1;
            ps = con.prepareStatement(query);
            ps.setString(params++, numExp);
            LOG.info("params = "
                    + "" + numExp
            );
            rs = ps.executeQuery();
            while (rs.next()) {
                resultado.add(melanbide88MappingUtils.getMelanbide88Inversiones(rs));
            }
        } catch (SQLException e) {
            LOG.error("Se ha producido recuperando getMelanbide88InversionesByNumExp ", e);
            throw e;
        } catch (Exception e) {
            LOG.error("Se ha producido recuperando getMelanbide88InversionesByNumExp ", e);
            throw e;
        } finally {
            LOG.debug("Procedemos a cerrar el resultset");
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        LOG.info(" getMelanbide88InversionesByNumExp - End " + numExp + formatFechaLog.format(new Date()));
        return resultado;
    }
    
    public boolean saveMelanbide88Inversiones(Melanbide88Inversiones datos, Connection con) throws SQLException, Exception {
        LOG.info(" saveMelanbide88Inversiones - Begin " + formatFechaLog.format(new Date()));
        boolean resultado = false;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "";
        try {
            if (datos != null) {
                if (datos.getId() != null && datos.getId() > 0) {
                    LOG.info("--Es una update");
                    query = "update "
                            + " MELANBIDE88_INVERSIONES "
                            + " set "
                            + " num_exp=?,fecha=?,numFactura=?,descripcion=?,nombProveedor=?,importe=?,pagada=?,fechaPago=? "
                            + " where "
                            + " id=? ";
                    LOG.info("sql = " + query);
                    int params = 1;
                    ps = con.prepareStatement(query);
                    ps.setString(params++, datos.getNum_exp());
                    if (datos.getFecha() != null) {
                        ps.setDate(params++, new java.sql.Date(datos.getFecha().getTime()));
                    } else {
                        ps.setNull(params++, java.sql.Types.DATE);
                    }
                    ps.setString(params++, datos.getNumFactura());
                    ps.setString(params++, datos.getDescripcion());
                    ps.setString(params++, datos.getNombProveedor());
                    if(datos.getImporte()!=null)
                        ps.setDouble(params++, datos.getImporte());
                    else
                        ps.setNull(params++, java.sql.Types.NULL);
                    ps.setString(params++, datos.getPagada());
                    if(datos.getFechaPago()!=null)
                        ps.setDate(params++, new java.sql.Date(datos.getFechaPago().getTime()));
                    else
                        ps.setNull(params++, java.sql.Types.DATE);
                    ps.setLong(params++, datos.getId());
                } else {
                    LOG.info("--Es una insert");
                    query = "insert into "
                            + " MELANBIDE88_INVERSIONES "
                            + " (id,num_exp,fecha,numFactura,descripcion,nombProveedor,importe,pagada,fechaPago) "
                            + " values (SEQ_MELANBIDE88_INVERSIONES.nextval,?,?,?,?,?,?,?,?) ";
                    LOG.info("sql = " + query);
                    int params = 1;
                    ps = con.prepareStatement(query);
                    ps.setString(params++, datos.getNum_exp());
                    if (datos.getFecha() != null) {
                        ps.setDate(params++, new java.sql.Date(datos.getFecha().getTime()));
                    } else {
                        ps.setNull(params++, java.sql.Types.DATE);
                    }
                    ps.setString(params++, datos.getNumFactura());
                    ps.setString(params++, datos.getDescripcion());
                    ps.setString(params++, datos.getNombProveedor());
                    if (datos.getImporte() != null) {
                        ps.setDouble(params++, datos.getImporte());
                    } else {
                        ps.setNull(params++, java.sql.Types.NULL);
                    }
                    ps.setString(params++, datos.getPagada());
                    if (datos.getFechaPago() != null) {
                        ps.setDate(params++, new java.sql.Date(datos.getFechaPago().getTime()));
                    } else {
                        ps.setNull(params++, java.sql.Types.DATE);
                    }
                }
                LOG.info("params = "
                        + "" + datos.toString()
                );
                resultado = ps.executeUpdate() > 0;
            } else {
                LOG.error("Objeto de datos recibidos a NULL - No se puede guardar ls informacion.");
            }
        } catch (SQLException e) {
            LOG.error("Se ha producido error  saveMelanbide88Inversiones ", e);
            throw e;
        } catch (Exception e) {
            LOG.error("Se ha producido Error saveMelanbide88Inversiones ", e);
            throw e;
        } finally {
            LOG.debug("Procedemos a cerrar el resultset");
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        LOG.info(" saveMelanbide88Inversiones - End " + formatFechaLog.format(new Date()));
        return resultado;
    }

    public boolean deleteMelanbide88InversionesByID(Long id, Connection con) throws SQLException, Exception {
        LOG.info(" deleteMelanbide88InversionesByID - Begin " + formatFechaLog.format(new Date()));
        boolean resultado = false;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "";
        try {
            if (id != null) {
                query = " delete "
                        + " MELANBIDE88_INVERSIONES "
                        + " where "
                        + " id=? ";
                LOG.info("sql = " + query);
                int params = 1;
                ps = con.prepareStatement(query);
                ps.setLong(params++, id);
                LOG.info("params = "
                        + "" + id
                );
                resultado = ps.executeUpdate() > 0;
            } else {
                LOG.error("Objeto de datos recibidos a NULL - No se puede Eliminar informacion.");
            }
        } catch (SQLException e) {
            LOG.error("Se ha producido error  deleteMelanbide88InversionesByID ", e);
            throw e;
        } catch (Exception e) {
            LOG.error("Se ha producido Error deleteMelanbide88InversionesByID ", e);
            throw e;
        } finally {
            LOG.debug("Procedemos a cerrar el resultset");
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        LOG.info(" deleteMelanbide88InversionesByID - End " + formatFechaLog.format(new Date()));
        return resultado;
    }
}
