package es.altia.flexia.integracion.moduloexterno.melanbide47.manager;

import es.altia.flexia.integracion.moduloexterno.melanbide47.dao.OriCertCalidadPuntuacionDAO;
import es.altia.flexia.integracion.moduloexterno.melanbide47.vo.orientacion.OriCertCalidadPuntuacion;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.List;

public class MELanbide47ManagerOriCertCalidadPuntuacion {

    private static final Logger log = LogManager.getLogger(MELanbide47ManagerOriCertCalidadPuntuacion.class);
    // Formateador de fecha para trzas en logs dureacion de metodos, etc
    SimpleDateFormat formatFechaLog = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    SimpleDateFormat formatFechaddMMyyy = new SimpleDateFormat("dd/MM/yyyy");

    private static OriCertCalidadPuntuacionDAO oriCertCalidadPuntuacionDAO = new OriCertCalidadPuntuacionDAO();

    public OriCertCalidadPuntuacion getOriCertCalidadPuntuacionByIdConvocatoriaAndCodigo(int idConvocatoria, String codigo, AdaptadorSQLBD adapt) throws Exception {
        log.info("getOriCertCalidadPuntuacionByIdConvocatoriaAndCodigo - Begin - " + codigo);
        Connection con = null;
        try {
            con = adapt.getConnection();
            return oriCertCalidadPuntuacionDAO.getOriCertCalidadPuntuacionByIdConvocatoriaAndCodigo(idConvocatoria,codigo, con);
        } catch (BDException e) {
            log.error("Se ha producido una BDException getOriCertCalidadPuntuacionByIdConvocatoriaAndCodigo ", e);
            throw e;
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion getOriCertCalidadPuntuacionByIdConvocatoriaAndCodigo ", ex);
            throw ex;
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD", e);
            }
            log.info("getOriCertCalidadPuntuacionByIdConvocatoriaAndCodigo - End -");
        }
    }

    public List<OriCertCalidadPuntuacion> getOriCertCalidadPuntuacionByIdConvocatoria(int idConvocatoria, AdaptadorSQLBD adapt) throws Exception {
        log.info("getOriCertCalidadPuntuacionByIdConvocatoria - Begin - " );
        Connection con = null;
        try {
            con = adapt.getConnection();
            return oriCertCalidadPuntuacionDAO.getOriCertCalidadPuntuacionByIdConvocatoria(idConvocatoria,con);
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
            log.info("getOriCertCalidadPuntuacionByIdConvocatoria - End -");
        }
    }

}
