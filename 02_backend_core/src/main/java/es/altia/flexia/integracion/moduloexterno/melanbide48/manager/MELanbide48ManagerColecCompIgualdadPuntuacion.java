package es.altia.flexia.integracion.moduloexterno.melanbide48.manager;

import es.altia.flexia.integracion.moduloexterno.melanbide48.dao.ColecCompIgualdadPuntuacionDAO;
import es.altia.flexia.integracion.moduloexterno.melanbide48.vo.ColecCompIgualdadPuntuacion;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.List;

public class MELanbide48ManagerColecCompIgualdadPuntuacion {

    private static final Logger log = LogManager.getLogger(MELanbide48ManagerColecCompIgualdadPuntuacion.class);
    // Formateador de fecha para trzas en logs dureacion de metodos, etc
    SimpleDateFormat formatFechaLog = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    SimpleDateFormat formatFechaddMMyyy = new SimpleDateFormat("dd/MM/yyyy");

    private static ColecCompIgualdadPuntuacionDAO colecCompIgualdadPuntuacionDAO = new ColecCompIgualdadPuntuacionDAO();

    public ColecCompIgualdadPuntuacion getColecCompIgualdadPuntuacionByIdConvocatoriaAndCodigo(int idConvocatoria, String codigo, AdaptadorSQLBD adapt) throws Exception {
        log.info("getColecCertCalidadPuntuacionByIdConvocatoriaAndCodigo - Begin - " + codigo);
        Connection con = null;
        try {
            con = adapt.getConnection();
            return colecCompIgualdadPuntuacionDAO.getColecCompIgualdadPuntuacionByIdConvocatoriaAndCodigo(idConvocatoria,codigo, con);
        } catch (BDException e) {
            log.error("Se ha producido una BDException getColecCompIgualdadPuntuacionByIdConvocatoriaAndCodigo ", e);
            throw e;
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion getColecCompIgualdadPuntuacionByIdConvocatoriaAndCodigo ", ex);
            throw ex;
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD", e);
            }
            log.info("getColecCompIgualdadPuntuacionByIdConvocatoriaAndCodigo - End -");
        }
    }

    public List<ColecCompIgualdadPuntuacion>getColecCompIgualdadPuntuacionByIdConvocatoria(int idConvocatoria, AdaptadorSQLBD adapt) throws Exception {
        log.info("getColecCompIgualdadPuntuacionByIdConvocatoria - Begin - " );
        Connection con = null;
        try {
            con = adapt.getConnection();
            return colecCompIgualdadPuntuacionDAO.getColecCompIgualdadPuntuacionByIdConvocatoria(idConvocatoria,con);
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
            log.info("getColecCompIgualdadPuntuacionByIdConvocatoria - End -");
        }
    }

}
