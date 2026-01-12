package es.altia.flexia.integracion.moduloexterno.melanbide47.manager;

import es.altia.flexia.integracion.moduloexterno.melanbide47.dao.OriCompIgualdadDAO;
import es.altia.flexia.integracion.moduloexterno.melanbide47.vo.orientacion.OriCompIgualdad;
import es.altia.flexia.integracion.moduloexterno.melanbide47.vo.comun.SelectItem;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.List;

public class MELanbide47ManagerOriCompIgualdad {

    private static final Logger log = LogManager.getLogger(MELanbide47ManagerOriCompIgualdad.class);
    // Formateador de fecha para trzas en logs dureacion de metodos, etc
    SimpleDateFormat formatFechaLog = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    SimpleDateFormat formatFechaddMMyyy = new SimpleDateFormat("dd/MM/yyyy");

    private static OriCompIgualdadDAO oriCompIgualdadDAO = new OriCompIgualdadDAO();

    public OriCompIgualdad getOriOritivoByCodigo(String codigo, AdaptadorSQLBD adapt) throws Exception {
        log.info("getOriOritivoByCodigo - Begin - " + codigo);
        Connection con = null;
        try {
            con = adapt.getConnection();
            return oriCompIgualdadDAO.getOriCompIgualdadByCodigo(codigo, con);
        } catch (BDException e) {
            log.error("Se ha producido una BDException getOriOriOritivoByCodigo ", e);
            throw e;
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion getOriOriOritivoByCodigo ", ex);
            throw ex;
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD", e);
            }
            log.info("getOriOriOritivoByCodigo - End -");
        }
    }

    public List<OriCompIgualdad> getOriCompIgualdad(AdaptadorSQLBD adapt) throws Exception {
        log.info("getOriCompIgualdad - Begin - " );
        Connection con = null;
        try {
            con = adapt.getConnection();
            return oriCompIgualdadDAO.getOriCompIgualdad(con);
        } catch (BDException e) {
            log.error("Se ha producido una BDException getOriCompIgualdad ", e);
            throw e;
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion getOriCompIgualdad ", ex);
            throw ex;
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD", e);
            }
            log.info("getOriCompIgualdad - End -");
        }
    }

    public List<SelectItem> getListaOriCompIgualdad(Integer idioma,AdaptadorSQLBD adapt) throws Exception {
        log.info("getListaOriCompIgualdad - Begin - " );
        Connection con = null;
        try {
            con = adapt.getConnection();
            return oriCompIgualdadDAO.getListaOriCompIgualdad(idioma, con);
        } catch (BDException e) {
            log.error("Se ha producido una BDException getListaOriCompIgualdad ", e);
            throw e;
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion getListaOriCompIgualdad ", ex);
            throw ex;
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD", e);
            }
            log.info("getListaOriCompIgualdad - End -");
        }
    }


}
