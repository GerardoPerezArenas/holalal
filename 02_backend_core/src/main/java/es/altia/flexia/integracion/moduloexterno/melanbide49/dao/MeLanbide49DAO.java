/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide49.dao;

import es.altia.flexia.integracion.moduloexterno.melanbide49.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide49.util.ConstantesMeLanbide49;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author santiagoc
 */
public class MeLanbide49DAO {

    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide49DAO.class);

    //Instancia
    private static MeLanbide49DAO instance = null;

    private MeLanbide49DAO() {

    }

    public static MeLanbide49DAO getInstance() {
        if (instance == null) {
            synchronized (MeLanbide49DAO.class) {
                instance = new MeLanbide49DAO();
            }
        }
        return instance;
    }

    public /*String*/ boolean insertarSecuencia(int codOrganizacion, String numExp, String codTramite, Connection con) throws Exception {
        log.info("Entra en insertarSecuencia DAO");
        Statement stat = null;
        ResultSet rs = null;
        int result = 0;
        int idSecuencia;
        try {
            String query = null;
            String ano = "";
            if (numExp.contains("/")) {
                String[] partes = numExp.split("/");
                ano = partes[0];
            }
            /* En PRO
            query = "insert into "+ConfigurationParameter.getParameter(ConstantesMeLanbide49.TABLA_TIPO_DATO_TEXTO, ConstantesMeLanbide49.FICHERO_PROPIEDADES)+
            " (txt_mun, txt_eje, txt_num,txt_cod, txt_valor) " +
                "select "+codOrganizacion+", "+ano+",'"+numExp+"','"+ConfigurationParameter.getParameter(ConstantesMeLanbide49.CAMPO_SUPL_NUMAGENCIA2, ConstantesMeLanbide49.FICHERO_PROPIEDADES)+"', TO_CHAR("+ConfigurationParameter.getParameter(ConstantesMeLanbide49.AACC_NUMEROAGENCIA_SQ, ConstantesMeLanbide49.FICHERO_PROPIEDADES)+".NEXTVAL) FROM DUAL";
             */
            // mi primer modo
 /*           query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide49.TABLA_TIPO_DATO_TEXTO, ConstantesMeLanbide49.FICHERO_PROPIEDADES)
                    + " (txt_mun, txt_eje, txt_num,txt_cod, txt_valor) "
                    + " VALUES (" + codOrganizacion + "," + ano + ","
                    + "'" + numExp + "','"
                    + ConfigurationParameter.getParameter(ConstantesMeLanbide49.CAMPO_SUPL_NUMAGENCIA2, ConstantesMeLanbide49.FICHERO_PROPIEDADES)
                    + "', TO_CHAR(" + ConfigurationParameter.getParameter(ConstantesMeLanbide49.AACC_NUMEROAGENCIA_SQ, ConstantesMeLanbide49.FICHERO_PROPIEDADES) + ".NEXTVAL))";
 */         
            // mi segundo modo
            
            idSecuencia = this.getNextId(ConfigurationParameter.getParameter(ConstantesMeLanbide49.AACC_NUMEROAGENCIA_SQ, ConstantesMeLanbide49.FICHERO_PROPIEDADES), con);
            log.info("Numero de agencia recuperado de la secuencia: "+ idSecuencia);
            query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide49.TABLA_TIPO_DATO_TEXTO, ConstantesMeLanbide49.FICHERO_PROPIEDADES)
                    + " (txt_mun, txt_eje, txt_num,txt_cod, txt_valor) "
                    + " VALUES (" + codOrganizacion + "," + ano + ","
                    + "'" + numExp + "','"
                    + ConfigurationParameter.getParameter(ConstantesMeLanbide49.CAMPO_SUPL_NUMAGENCIA2, ConstantesMeLanbide49.FICHERO_PROPIEDADES)
                    + "', '"  +idSecuencia + "')";
            
            log.info("SQL a ejecutar: " + query);
            stat = con.createStatement();
            result = stat.executeUpdate(query);            
            log.info("El resultado de la INSERT es: "+ result);
            if (result > 0) {
                log.info("SQL Ejecutada");
                return true;
            } else {
                log.error("Error al insertar el Número de Agencia ");
                return false;
            }            
        } catch (Exception ex) {
            log.error("Se ha producido un error guardando el Número de Agencia ", ex);
            throw new Exception(ex);
        }finally {
            if (log.isDebugEnabled()) {
                log.debug("Procedemos a cerrar el statement y el resultset");
            }
            if (stat != null) {
                stat.close();
            }
        }
      
    }
       private Integer getNextId(String seqName, Connection con) throws Exception {

        Statement st = null;
        ResultSet rs = null;
        Integer numSec = null;
        try {
            String query = null;
            //Creo el id con la secuencia
            query = "select " + seqName + ".nextval from dual";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                numSec = rs.getInt(1);
                log.info("Nº de secuencia " + seqName + "recuperado: " + numSec);
                if (rs.wasNull()) {
                    throw new Exception();
                }
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error generando el siguiente valor de la secuencia " + seqName, ex);
            throw new Exception(ex);
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("Procedemos a cerrar el statement y el resultset");
            }
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return numSec;
    }

}
