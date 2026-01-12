/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide88.dao;

import es.altia.flexia.integracion.moduloexterno.melanbide88.vo.Melanbide88Subsolic;
import es.altia.flexia.integracion.moduloexterno.melanbide88.vo.SelectItem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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
public class Melanbide88DAO {
    
    private static final Logger LOG = LogManager.getLogger(Melanbide88DAO.class);
    private final SimpleDateFormat formatFechaddMMyyyy = new SimpleDateFormat("dd/MM/yyyy");
    private final SimpleDateFormat formatFechaLog = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    
    public boolean existeColumnaEnResultSet(String nombrecolumna, ResultSet rs) throws SQLException {
        boolean existe = false;
        if (rs != null) {
            ResultSetMetaData metaData = rs.getMetaData();
            int count = metaData.getColumnCount();
            for (int i = 1; i <= count; i++) {
                String namecolumn = metaData.getColumnName(i); // Primera columna es 1
                if (namecolumn.equalsIgnoreCase(nombrecolumna)) {
                    return true;
                }
            }
        }
        return existe;
    }
    
    public List<SelectItem> getDesplegableE_DES(String codigo, int idioma, Connection con) throws SQLException, Exception {
        LOG.info(" getDesplegableE_DES - Begin " + formatFechaLog.format(new Date()));
        List<SelectItem> resultado = new ArrayList<SelectItem>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String query = "select * "
                    + " from E_DES_VAL "
                    + " where "
                    + " des_cod=? ";
            LOG.info("sql = " + query);
            int params = 1;
            ps = con.prepareStatement(query);
            ps.setString(params++, codigo);
            LOG.info("params = "
                    + "" + codigo
            );
            rs = ps.executeQuery();
            while (rs.next()) {
                resultado.add(new SelectItem(
                        rs.getString("DES_VAL_COD")
                        ,rs.getString("DES_NOM")
                        ,rs.getString("DES_NOM"))
                    );
            }
        } catch (SQLException e) {
            LOG.error("Se ha producido recuperando getDesplegableE_DES ", e);
            throw e;
        } catch (Exception e) {
            LOG.error("Se ha producido recuperando getDesplegableE_DES ", e);
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
        LOG.info(" getDesplegableE_DES - End " + codigo + formatFechaLog.format(new Date()));
        return resultado;
    }

}
