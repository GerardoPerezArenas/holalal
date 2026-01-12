package es.altia.flexia.integracion.moduloexterno.melanbide47.manager;

import es.altia.flexia.integracion.moduloexterno.melanbide47.dao.OriCompIgualdadPuntuacionDAO;
import es.altia.flexia.integracion.moduloexterno.melanbide47.vo.orientacion.OriCompIgualdadPuntuacion;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.List;

public class MELanbide47ManagerOriCompIgualdadPuntuacion {

    private static final Logger log = LogManager.getLogger(MELanbide47ManagerOriCompIgualdadPuntuacion.class);
    // Formateador de fecha para trzas en logs dureacion de metodos, etc
    SimpleDateFormat formatFechaLog = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    SimpleDateFormat formatFechaddMMyyy = new SimpleDateFormat("dd/MM/yyyy");

    private static OriCompIgualdadPuntuacionDAO oriCompIgualdadPuntuacionDAO = new OriCompIgualdadPuntuacionDAO();

    public OriCompIgualdadPuntuacion getOriCompIgualdadPuntuacionByIdConvocatoriaAndCodigo(int idConvocatoria, String codigo, AdaptadorSQLBD adapt) throws Exception {
        log.info("getOriCertCalidadPuntuacionByIdConvocatoriaAndCodigo - Begin - " + codigo);
        Connection con = null;
        try {
            con = adapt.getConnection();
            return oriCompIgualdadPuntuacionDAO.getOriCompIgualdadPuntuacionByIdConvocatoriaAndCodigo(idConvocatoria,codigo, con);
        } catch (BDException e) {
            log.error("Se ha producido una BDException getOriCompIgualdadPuntuacionByIdConvocatoriaAndCodigo ", e);
            throw e;
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion getOriCompIgualdadPuntuacionByIdConvocatoriaAndCodigo ", ex);
            throw ex;
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD", e);
            }
            log.info("getOriCompIgualdadPuntuacionByIdConvocatoriaAndCodigo - End -");
        }
    }

    public List<OriCompIgualdadPuntuacion>getOriCompIgualdadPuntuacionByIdConvocatoria(int idConvocatoria, AdaptadorSQLBD adapt) throws Exception {
        log.info("getOriCompIgualdadPuntuacionByIdConvocatoria - Begin - " );
        Connection con = null;
        try {
            con = adapt.getConnection();
            return oriCompIgualdadPuntuacionDAO.getOriCompIgualdadPuntuacionByIdConvocatoria(idConvocatoria,con);
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
            log.info("getOriCompIgualdadPuntuacionByIdConvocatoria - End -");
        }
    }

}
