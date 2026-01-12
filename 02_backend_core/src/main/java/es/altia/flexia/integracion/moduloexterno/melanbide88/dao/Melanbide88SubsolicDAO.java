/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide88.dao;

import es.altia.flexia.integracion.moduloexterno.melanbide88.util.Melanbide88MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide88.vo.Melanbide88Subsolic;
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
public class Melanbide88SubsolicDAO {
    
    private static final Logger LOG = LogManager.getLogger(Melanbide88SubsolicDAO.class);
    private final SimpleDateFormat formatFechaddMMyyyy = new SimpleDateFormat("dd/MM/yyyy");
    private final SimpleDateFormat formatFechaLog = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private final Melanbide88MappingUtils melanbide88MappingUtils = new Melanbide88MappingUtils();

    public Melanbide88Subsolic getMelanbide88SubsolicByID(Long id, Connection con) throws SQLException, Exception {
        LOG.info(" getMelanbide88SubsolicByID - Begin " + formatFechaLog.format(new Date()));
        Melanbide88Subsolic resultado = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String query = "select * "
                    + " from MELANBIDE88_SUBSOLIC "
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
                resultado = melanbide88MappingUtils.getMelanbide88Subsolic(rs);
            }
        } catch (SQLException e) {
            LOG.error("Se ha producido recuperando getMelanbide88SubsolicByID ", e);
            throw e;
        } catch (Exception e) {
            LOG.error("Se ha producido recuperando getMelanbide88SubsolicByID ", e);
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
        LOG.info(" getMelanbide88SubsolicByID - End " + id + formatFechaLog.format(new Date()));
        return resultado;
    }

    public List<Melanbide88Subsolic> getMelanbide88SubsolicByNumExp(String numExp, Connection con) throws SQLException, Exception {
        LOG.info(" getMelanbide88SubsolicByNumExp - Begin " + formatFechaLog.format(new Date()));
        List<Melanbide88Subsolic> resultado = new ArrayList<Melanbide88Subsolic>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String query = "select * "
                    + " from MELANBIDE88_SUBSOLIC "
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
                resultado.add(melanbide88MappingUtils.getMelanbide88Subsolic(rs));
            }
        } catch (SQLException e) {
            LOG.error("Se ha producido recuperando getMelanbide88SubsolicByNumExp ", e);
            throw e;
        } catch (Exception e) {
            LOG.error("Se ha producido recuperando getMelanbide88SubsolicByNumExp ", e);
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
        LOG.info(" getMelanbide88SubsolicByNumExp - End " + numExp + formatFechaLog.format(new Date()));
        return resultado;
    }
    
    public boolean saveMelanbide88Subsolic(Melanbide88Subsolic datos, Connection con) throws SQLException, Exception {
        LOG.info(" saveMelanbide88Subsolic - Begin " + formatFechaLog.format(new Date()));
        boolean resultado = false;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "";
        try {
            if (datos != null) {
                if (datos.getId() != null && datos.getId() > 0) {
                    LOG.info("--Es una update");
                    query = "update "
                            + " MELANBIDE88_SUBSOLIC "
                            + " set "
                            + " num_exp=?,estado=?,organismo=?,objeto=?,importe=?,fecha=? "
                            + " where "
                            + " id=? ";
                    LOG.info("sql = " + query);
                    int params = 1;
                    ps = con.prepareStatement(query);
                    ps.setString(params++, datos.getNum_exp());
                    ps.setString(params++, datos.getEstado());
                    ps.setString(params++, datos.getOrganismo());
                    ps.setString(params++, datos.getObjeto());
                    if(datos.getImporte()!=null)
                        ps.setDouble(params++, datos.getImporte());
                    else
                        ps.setNull(params++, java.sql.Types.NULL);
                    if(datos.getFecha()!=null)
                        ps.setDate(params++, new java.sql.Date(datos.getFecha().getTime()));
                    else
                        ps.setNull(params++, java.sql.Types.DATE);
                    ps.setLong(params++, datos.getId());
                } else {
                    LOG.info("--Es una insert");
                    query = "insert into "
                            + " MELANBIDE88_SUBSOLIC "
                            + " (id,num_exp,estado,organismo,objeto,importe,fecha) "
                            + " values (SEQ_MELANBIDE88_SUBSOLIC.nextval,?,?,?,?,?,?) ";
                    LOG.info("sql = " + query);
                    int params = 1;
                    ps = con.prepareStatement(query);
                    ps.setString(params++, datos.getNum_exp());
                    ps.setString(params++, datos.getEstado());
                    ps.setString(params++, datos.getOrganismo());
                    ps.setString(params++, datos.getObjeto());
                    if(datos.getImporte()!=null)
                        ps.setDouble(params++, datos.getImporte());
                    else
                        ps.setNull(params++, java.sql.Types.NULL);
                    if(datos.getFecha()!=null)
                        ps.setDate(params++, new java.sql.Date(datos.getFecha().getTime()));
                    else
                        ps.setNull(params++, java.sql.Types.DATE);
                }
                LOG.info("params = "
                        + "" + datos.toString()
                );
                resultado = ps.executeUpdate() > 0;
            } else {
                LOG.error("Objeto de datos recibidos a NULL - No se puede guardar ls informacion.");
            }
        } catch (SQLException e) {
            LOG.error("Se ha producido error  saveMelanbide88Subsolic ", e);
            throw e;
        } catch (Exception e) {
            LOG.error("Se ha producido Error saveMelanbide88Subsolic ", e);
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
        LOG.info(" saveMelanbide88Subsolic - End " + formatFechaLog.format(new Date()));
        return resultado;
    }

    public boolean deleteMelanbide88SubsolicByID(Long id, Connection con) throws SQLException, Exception {
        LOG.info(" deleteMelanbide88SubsolicByID - Begin " + formatFechaLog.format(new Date()));
        boolean resultado = false;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "";
        try {
            if (id != null) {
                query = " delete "
                        + " MELANBIDE88_SUBSOLIC "
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
            LOG.error("Se ha producido error  deleteMelanbide88SubsolicByID ", e);
            throw e;
        } catch (Exception e) {
            LOG.error("Se ha producido Error deleteMelanbide88SubsolicByID ", e);
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
        LOG.info(" deleteMelanbide88SubsolicByID - End " + formatFechaLog.format(new Date()));
        return resultado;
    }
}
