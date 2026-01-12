package es.altia.flexia.integracion.moduloexterno.melanbide48.manager;

import es.altia.flexia.integracion.moduloexterno.melanbide48.dao.ColecCertCalidadDAO;
import es.altia.flexia.integracion.moduloexterno.melanbide48.vo.ColecCertCalidad;
import es.altia.flexia.integracion.moduloexterno.melanbide48.vo.SelectItem;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.List;

public class MELanbide48ManagerColecCertCalidad {

    private static final Logger log = LogManager.getLogger(MELanbide48ManagerColecCertCalidad.class);
    // Formateador de fecha para trzas en logs dureacion de metodos, etc
    SimpleDateFormat formatFechaLog = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    SimpleDateFormat formatFechaddMMyyy = new SimpleDateFormat("dd/MM/yyyy");

    private static ColecCertCalidadDAO colecCertCalidadDAO = new ColecCertCalidadDAO();

    public ColecCertCalidad getColecCertCalidadByCodigo(String codigo, AdaptadorSQLBD adapt) throws Exception {
        log.info("getColecCertCalidadByCodigo - Begin - " + codigo);
        Connection con = null;
        try {
            con = adapt.getConnection();
            return colecCertCalidadDAO.getColecCertCalidadByCodigo(codigo, con);
        } catch (BDException e) {
            log.error("Se ha producido una BDException getColecCertCalidadByCodigo ", e);
            throw e;
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion getColecCertCalidadByCodigo ", ex);
            throw ex;
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD", e);
            }
            log.info("getColecCertCalidadByCodigo - End -");
        }
    }

    public List<ColecCertCalidad> getColecCertCalidad(AdaptadorSQLBD adapt) throws Exception {
        log.info("getColecCertCalidad - Begin - " );
        Connection con = null;
        try {
            con = adapt.getConnection();
            return colecCertCalidadDAO.getColecCertCalidad(con);
        } catch (BDException e) {
            log.error("Se ha producido una BDException getColecCertCalidad ", e);
            throw e;
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion getColecCertCalidad ", ex);
            throw ex;
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD", e);
            }
            log.info("getColecCertCalidad - End -");
        }
    }

    public List<SelectItem> getListaColecCertCalidad(Integer idioma,AdaptadorSQLBD adapt) throws Exception {
        log.info("getListaColecCertCalidad - Begin - " );
        Connection con = null;
        try {
            con = adapt.getConnection();
            return colecCertCalidadDAO.getListaColecCertCalidad(idioma, con);
        } catch (BDException e) {
            log.error("Se ha producido una BDException getListaColecCertCalidad ", e);
            throw e;
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion getListaColecCertCalidad ", ex);
            throw ex;
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD", e);
            }
            log.info("getListaColecCertCalidad - End -");
        }
    }


}
