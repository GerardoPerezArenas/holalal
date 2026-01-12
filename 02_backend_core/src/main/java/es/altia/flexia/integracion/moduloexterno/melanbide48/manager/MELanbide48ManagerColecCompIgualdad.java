package es.altia.flexia.integracion.moduloexterno.melanbide48.manager;

import es.altia.flexia.integracion.moduloexterno.melanbide48.dao.ColecCompIgualdadDAO;
import es.altia.flexia.integracion.moduloexterno.melanbide48.vo.ColecCompIgualdad;
import es.altia.flexia.integracion.moduloexterno.melanbide48.vo.SelectItem;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.List;

public class MELanbide48ManagerColecCompIgualdad {

    private static final Logger log = LogManager.getLogger(MELanbide48ManagerColecCompIgualdad.class);
    // Formateador de fecha para trzas en logs dureacion de metodos, etc
    SimpleDateFormat formatFechaLog = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    SimpleDateFormat formatFechaddMMyyy = new SimpleDateFormat("dd/MM/yyyy");

    private static ColecCompIgualdadDAO colecCompIgualdadDAO = new ColecCompIgualdadDAO();

    public ColecCompIgualdad getColecColectivoByCodigo(String codigo, AdaptadorSQLBD adapt) throws Exception {
        log.info("getColecColectivoByCodigo - Begin - " + codigo);
        Connection con = null;
        try {
            con = adapt.getConnection();
            return colecCompIgualdadDAO.getColecCompIgualdadByCodigo(codigo, con);
        } catch (BDException e) {
            log.error("Se ha producido una BDException getColecColecColectivoByCodigo ", e);
            throw e;
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion getColecColecColectivoByCodigo ", ex);
            throw ex;
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD", e);
            }
            log.info("getColecColecColectivoByCodigo - End -");
        }
    }

    public List<ColecCompIgualdad> getColecCompIgualdad(AdaptadorSQLBD adapt) throws Exception {
        log.info("getColecCompIgualdad - Begin - " );
        Connection con = null;
        try {
            con = adapt.getConnection();
            return colecCompIgualdadDAO.getColecCompIgualdad(con);
        } catch (BDException e) {
            log.error("Se ha producido una BDException getColecCompIgualdad ", e);
            throw e;
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion getColecCompIgualdad ", ex);
            throw ex;
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD", e);
            }
            log.info("getColecCompIgualdad - End -");
        }
    }

    public List<SelectItem> getListaColecCompIgualdad(Integer idioma,AdaptadorSQLBD adapt) throws Exception {
        log.info("getListaColecCompIgualdad - Begin - " );
        Connection con = null;
        try {
            con = adapt.getConnection();
            return colecCompIgualdadDAO.getListaColecCompIgualdad(idioma, con);
        } catch (BDException e) {
            log.error("Se ha producido una BDException getListaColecCompIgualdad ", e);
            throw e;
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion getListaColecCompIgualdad ", ex);
            throw ex;
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD", e);
            }
            log.info("getListaColecCompIgualdad - End -");
        }
    }


}
