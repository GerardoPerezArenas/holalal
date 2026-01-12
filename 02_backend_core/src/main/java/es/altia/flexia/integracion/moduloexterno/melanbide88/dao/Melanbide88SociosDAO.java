/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide88.dao;

import es.altia.flexia.integracion.moduloexterno.melanbide88.util.Melanbide88MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide88.vo.Melanbide88Socios;
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
public class Melanbide88SociosDAO {
    
    private static final Logger LOG = LogManager.getLogger(Melanbide88SociosDAO.class);
    private final SimpleDateFormat formatFechaddMMyyyy = new SimpleDateFormat("dd/MM/yyyy");
    private final SimpleDateFormat formatFechaLog = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private final Melanbide88MappingUtils melanbide88MappingUtils = new Melanbide88MappingUtils();
    
    
    public Melanbide88Socios getMelanbide88SociosByID(Long id, Connection con) throws SQLException, Exception {
        LOG.info(" getMelanbide88SociosByID - Begin " + formatFechaLog.format(new Date()));
        Melanbide88Socios resultado = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String query = "select * "
                    + " from MELANBIDE88_SOCIOS "
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
                resultado = melanbide88MappingUtils.getMelanbide88Socios(rs);
            }
        } catch (SQLException e) {
            LOG.error("Se ha producido recuperando getMelanbide88SociosByID ", e);
            throw e;
        } catch (Exception e) {
            LOG.error("Se ha producido recuperando getMelanbide88SociosByID ", e);
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
        LOG.info(" getMelanbide88SociosByID - End " + id + formatFechaLog.format(new Date()));
        return resultado;
    }
    
    public List<Melanbide88Socios> getMelanbide88SociosByNumExp(String numExp, Connection con) throws SQLException, Exception {
        LOG.info(" getMelanbide88SociosByNumExp - Begin " + formatFechaLog.format(new Date()));
        List<Melanbide88Socios> resultado = new ArrayList<Melanbide88Socios>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String query = "select * "
                    + " from MELANBIDE88_SOCIOS "
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
                resultado.add(melanbide88MappingUtils.getMelanbide88Socios(rs));
            }
        } catch (SQLException e) {
            LOG.error("Se ha producido recuperando getMelanbide88SociosByNumExp ", e);
            throw e;
        } catch (Exception e) {
            LOG.error("Se ha producido recuperando getMelanbide88SociosByNumExp ", e);
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
        LOG.info(" getMelanbide88SociosByNumExp - End " + numExp + formatFechaLog.format(new Date()));
        return resultado;
    }
    
    public boolean saveMelanbide88Socios(Melanbide88Socios datos, Connection con) throws SQLException, Exception {
        LOG.info(" saveMelanbide88Socios - Begin " + formatFechaLog.format(new Date()));
        boolean resultado = false;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "";
        try {
            if (datos != null) {
                if (datos.getId() != null && datos.getId() > 0) {
                    LOG.info("--Es una update");
                    query = "update "
                            + " MELANBIDE88_SOCIOS "
                            + " set "
                            + " num_exp=?,dniNieSocio=?,nombreSocio=?,apellido1Socio=?,apellido2Socio=? "
                            + " where "
                            + " id=? ";
                    LOG.info("sql = " + query);
                    int params = 1;
                    ps = con.prepareStatement(query);
                    ps.setString(params++, datos.getNum_exp());
                    ps.setString(params++, datos.getDniNieSocio());
                    ps.setString(params++, datos.getNombreSocio());
                    ps.setString(params++, datos.getApellido1Socio());
                    ps.setString(params++, datos.getApellido2Socio());
                    ps.setLong(params++, datos.getId());
                } else {
                    LOG.info("--Es una insert");
                    query = "insert into "
                            + " MELANBIDE88_SOCIOS "
                            + " (id,num_exp,dniNieSocio,nombreSocio,apellido1Socio,apellido2Socio) "
                            + " values (SEQ_MELANBIDE88_SOCIOS.nextval,?,?,?,?,?) ";
                    LOG.info("sql = " + query);
                    int params = 1;
                    ps = con.prepareStatement(query);
                    ps.setString(params++, datos.getNum_exp());
                    ps.setString(params++, datos.getDniNieSocio());
                    ps.setString(params++, datos.getNombreSocio());
                    ps.setString(params++, datos.getApellido1Socio());
                    ps.setString(params++, datos.getApellido2Socio());
                }
                LOG.info("params = "
                        + "" + datos.toString()
                );
                resultado = ps.executeUpdate() > 0;
            } else {
                LOG.error("Objeto de datos recibidos a NULL - No se puede guardar ls informacion.");
            }
        } catch (SQLException e) {
            LOG.error("Se ha producido error  saveMelanbide88Socios ", e);
            throw e;
        } catch (Exception e) {
            LOG.error("Se ha producido Error saveMelanbide88Socios ", e);
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
        LOG.info(" saveMelanbide88Socios - End " + formatFechaLog.format(new Date()));
        return resultado;
    }
    
    public boolean deleteMelanbide88SociosByID(Long id, Connection con) throws SQLException, Exception {
        LOG.info(" deleteMelanbide88SociosByID - Begin " + formatFechaLog.format(new Date()));
        boolean resultado = false;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "";
        try {
            if (id != null) {
                    query = " delete "
                            + " MELANBIDE88_SOCIOS "
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
            LOG.error("Se ha producido error  deleteMelanbide88SociosByID ", e);
            throw e;
        } catch (Exception e) {
            LOG.error("Se ha producido Error deleteMelanbide88SociosByID ", e);
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
        LOG.info(" deleteMelanbide88SociosByID - End " + formatFechaLog.format(new Date()));
        return resultado;
    }
}
