package es.altia.flexia.integracion.moduloexterno.melanbide48.manager;

import es.altia.flexia.integracion.moduloexterno.melanbide48.dao.ColecCertCalidadDAO;
import es.altia.flexia.integracion.moduloexterno.melanbide48.dao.ColecCertCalidadPuntuacionDAO;
import es.altia.flexia.integracion.moduloexterno.melanbide48.vo.ColecCertCalidad;
import es.altia.flexia.integracion.moduloexterno.melanbide48.vo.ColecCertCalidadPuntuacion;
import es.altia.flexia.integracion.moduloexterno.melanbide48.vo.SelectItem;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.List;

public class MELanbide48ManagerColecCertCalidadPuntuacion {

    private static final Logger log = LogManager.getLogger(MELanbide48ManagerColecCertCalidadPuntuacion.class);
    // Formateador de fecha para trzas en logs dureacion de metodos, etc
    SimpleDateFormat formatFechaLog = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    SimpleDateFormat formatFechaddMMyyy = new SimpleDateFormat("dd/MM/yyyy");

    private static ColecCertCalidadPuntuacionDAO colecCertCalidadPuntuacionDAO = new ColecCertCalidadPuntuacionDAO();

    public ColecCertCalidadPuntuacion getColecCertCalidadPuntuacionByIdConvocatoriaAndCodigo(int idConvocatoria, String codigo, AdaptadorSQLBD adapt) throws Exception {
        log.info("getColecCertCalidadPuntuacionByIdConvocatoriaAndCodigo - Begin - " + codigo);
        Connection con = null;
        try {
            con = adapt.getConnection();
            return colecCertCalidadPuntuacionDAO.getColecCertCalidadPuntuacionByIdConvocatoriaAndCodigo(idConvocatoria,codigo, con);
        } catch (BDException e) {
            log.error("Se ha producido una BDException getColecCertCalidadPuntuacionByIdConvocatoriaAndCodigo ", e);
            throw e;
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion getColecCertCalidadPuntuacionByIdConvocatoriaAndCodigo ", ex);
            throw ex;
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD", e);
            }
            log.info("getColecCertCalidadPuntuacionByIdConvocatoriaAndCodigo - End -");
        }
    }

    public List<ColecCertCalidadPuntuacion> getColecCertCalidadPuntuacionByIdConvocatoria(int idConvocatoria, AdaptadorSQLBD adapt) throws Exception {
        log.info("getColecCertCalidadPuntuacionByIdConvocatoria - Begin - " );
        Connection con = null;
        try {
            con = adapt.getConnection();
            return colecCertCalidadPuntuacionDAO.getColecCertCalidadPuntuacionByIdConvocatoria(idConvocatoria,con);
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
            log.info("getColecCertCalidadPuntuacionByIdConvocatoria - End -");
        }
    }

}
