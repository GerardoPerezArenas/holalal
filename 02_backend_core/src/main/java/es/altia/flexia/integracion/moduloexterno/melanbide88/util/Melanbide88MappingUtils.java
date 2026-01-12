/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide88.util;

import es.altia.flexia.integracion.moduloexterno.melanbide88.dao.Melanbide88DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide88.vo.Melanbide88Inversiones;
import es.altia.flexia.integracion.moduloexterno.melanbide88.vo.Melanbide88Socios;
import es.altia.flexia.integracion.moduloexterno.melanbide88.vo.Melanbide88Subsolic;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author INGDGC
 */
public class Melanbide88MappingUtils {
    
    private static final Logger LOG = LogManager.getLogger(Melanbide88MappingUtils.class);
    private final SimpleDateFormat formatFechaddMMyyyy = new SimpleDateFormat("dd/MM/yyyy");
    private final SimpleDateFormat formatFechaLog = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private final Melanbide88DAO melanbide88DAO = new Melanbide88DAO();
    
    public Melanbide88Socios getMelanbide88Socios(ResultSet rs) throws SQLException {
        if (rs != null) {
            return new Melanbide88Socios(
                    (melanbide88DAO.existeColumnaEnResultSet("id",rs)?rs.getLong("id"):null),
                    (melanbide88DAO.existeColumnaEnResultSet("num_exp",rs)?rs.getString("num_exp"):null),
                    (melanbide88DAO.existeColumnaEnResultSet("dniNieSocio",rs)?rs.getString("dniNieSocio"):null),
                    (melanbide88DAO.existeColumnaEnResultSet("nombreSocio",rs)?rs.getString("nombreSocio"):null),
                    (melanbide88DAO.existeColumnaEnResultSet("apellido1Socio",rs)?rs.getString("apellido1Socio"):null),
                    (melanbide88DAO.existeColumnaEnResultSet("apellido2Socio",rs)?rs.getString("apellido2Socio"):null),
                    (melanbide88DAO.existeColumnaEnResultSet("fechaAlta",rs)?rs.getDate("fechaAlta"):null)
            );
        } else {
            LOG.info("ResultSet recibido a Null..");
            return null;
        }
    }
    
    public Melanbide88Inversiones getMelanbide88Inversiones(ResultSet rs) throws SQLException {
        if (rs != null) {
            return new Melanbide88Inversiones(
                    (melanbide88DAO.existeColumnaEnResultSet("id",rs)?rs.getLong("id"):null),
                    (melanbide88DAO.existeColumnaEnResultSet("num_exp",rs)?rs.getString("num_exp"):null),
                    (melanbide88DAO.existeColumnaEnResultSet("fecha",rs)?rs.getDate("fecha"):null),
                    (melanbide88DAO.existeColumnaEnResultSet("numFactura",rs)?rs.getString("numFactura"):null),
                    (melanbide88DAO.existeColumnaEnResultSet("descripcion",rs)?rs.getString("descripcion"):null),
                    (melanbide88DAO.existeColumnaEnResultSet("nombProveedor",rs)?rs.getString("nombProveedor"):null),
                    (melanbide88DAO.existeColumnaEnResultSet("importe",rs)?rs.getDouble("importe"):null),
                    (melanbide88DAO.existeColumnaEnResultSet("pagada",rs)?rs.getString("pagada"):null),
                    (melanbide88DAO.existeColumnaEnResultSet("fechaPago",rs)?rs.getDate("fechaPago"):null),
                    (melanbide88DAO.existeColumnaEnResultSet("fechaAlta",rs)?rs.getDate("fechaAlta"):null)
            );
        } else {
            LOG.info("ResultSet recibido a Null..");
            return null;
        }
    }
    
    public Melanbide88Subsolic getMelanbide88Subsolic(ResultSet rs) throws SQLException {
        if (rs != null) {
            return new Melanbide88Subsolic(
                    (melanbide88DAO.existeColumnaEnResultSet("id",rs)?rs.getLong("id"):null),
                    (melanbide88DAO.existeColumnaEnResultSet("num_exp",rs)?rs.getString("num_exp"):null),
                    (melanbide88DAO.existeColumnaEnResultSet("estado",rs)?rs.getString("estado"):null),
                    (melanbide88DAO.existeColumnaEnResultSet("organismo",rs)?rs.getString("organismo"):null),
                    (melanbide88DAO.existeColumnaEnResultSet("objeto",rs)?rs.getString("objeto"):null),
                    (melanbide88DAO.existeColumnaEnResultSet("importe",rs)?rs.getDouble("importe"):null),
                    (melanbide88DAO.existeColumnaEnResultSet("fecha",rs)?rs.getDate("fecha"):null),
                    (melanbide88DAO.existeColumnaEnResultSet("fechaAlta",rs)?rs.getDate("fechaAlta"):null)
            );
        } else {
            LOG.info("ResultSet recibido a Null..");
            return null;
        }
    }
}
