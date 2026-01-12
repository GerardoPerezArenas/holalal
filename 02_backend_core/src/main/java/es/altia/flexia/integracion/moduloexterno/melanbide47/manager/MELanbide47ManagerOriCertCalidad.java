package es.altia.flexia.integracion.moduloexterno.melanbide47.manager;

import es.altia.flexia.integracion.moduloexterno.melanbide47.dao.OriCertCalidadDAO;
import es.altia.flexia.integracion.moduloexterno.melanbide47.vo.orientacion.OriCertCalidad;
import es.altia.flexia.integracion.moduloexterno.melanbide47.vo.comun.SelectItem;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.List;

public class MELanbide47ManagerOriCertCalidad {

    private static final Logger log = LogManager.getLogger(MELanbide47ManagerOriCertCalidad.class);
    // Formateador de fecha para trzas en logs dureacion de metodos, etc
    SimpleDateFormat formatFechaLog = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    SimpleDateFormat formatFechaddMMyyy = new SimpleDateFormat("dd/MM/yyyy");

    private static OriCertCalidadDAO oriCertCalidadDAO = new OriCertCalidadDAO();

    public OriCertCalidad getOriCertCalidadByCodigo(String codigo, AdaptadorSQLBD adapt) throws Exception {
        log.info("getOriCertCalidadByCodigo - Begin - " + codigo);
        Connection con = null;
        try {
            con = adapt.getConnection();
            return oriCertCalidadDAO.getOriCertCalidadByCodigo(codigo, con);
        } catch (BDException e) {
            log.error("Se ha producido una BDException getOriCertCalidadByCodigo ", e);
            throw e;
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion getOriCertCalidadByCodigo ", ex);
            throw ex;
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD", e);
            }
            log.info("getOriCertCalidadByCodigo - End -");
        }
    }

    public List<OriCertCalidad> getOriCertCalidad(AdaptadorSQLBD adapt) throws Exception {
        log.info("getOriCertCalidad - Begin - " );
        Connection con = null;
        try {
            con = adapt.getConnection();
            return oriCertCalidadDAO.getOriCertCalidad(con);
        } catch (BDException e) {
            log.error("Se ha producido una BDException getOriCertCalidad ", e);
            throw e;
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion getOriCertCalidad ", ex);
            throw ex;
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD", e);
            }
            log.info("getOriCertCalidad - End -");
        }
    }

    public List<SelectItem> getListaOriCertCalidad(Integer idioma,AdaptadorSQLBD adapt) throws Exception {
        log.info("getListaOriCertCalidad - Begin - " );
        Connection con = null;
        try {
            con = adapt.getConnection();
            return oriCertCalidadDAO.getListaOriCertCalidad(idioma, con);
        } catch (BDException e) {
            log.error("Se ha producido una BDException getListaOriCertCalidad ", e);
            throw e;
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion getListaOriCertCalidad ", ex);
            throw ex;
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD", e);
            }
            log.info("getListaOriCertCalidad - End -");
        }
    }


}
