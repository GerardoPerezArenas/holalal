/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide74.manager;

import es.altia.flexia.integracion.moduloexterno.melanbide74.dao.MeLanbide74DAO;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.math.BigDecimal;
import java.sql.Connection;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author Kepa
 */
public class MeLanbide74Manager {
    //Logger

    private static final Logger log = LogManager.getLogger(MeLanbide74Manager.class);

    //Instancia
    private static MeLanbide74Manager instance = null;

    /**
     * Devuelve una instancia de MeLanbide74Manager, si no existe la crea.
     *
     * @return
     */
    public static MeLanbide74Manager getInstance() {
        if (instance == null) {
            synchronized (MeLanbide74Manager.class) {
                if (instance == null) {
                    instance = new MeLanbide74Manager();
                }//if(instance == null)
            }//synchronized(MeLanbide74Manager.class)
        }//if(instance == null)
        return instance;
    }//getInstance

    public String dameCodigoTipoVia(int codDomicilio, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            MeLanbide74DAO meLanbide74DAO = MeLanbide74DAO.getInstance();
            con = adapt.getConnection();
            return meLanbide74DAO.dameCodigoViaDomicilio(codDomicilio, con);
        } catch (Exception e) {
            log.error("Se ha producido una excepcion en la BBDD recogiendo Codigo Tipo Via ", e);
            throw new Exception(e);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexon a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean coincideCodigo(String codigoVia, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            MeLanbide74DAO meLanbide74DAO = MeLanbide74DAO.getInstance();
            con = adapt.getConnection();
            return meLanbide74DAO.coincideCodigo(codigoVia, con);
        } catch (Exception e) {
            log.error("Se ha producido una excepcion en la BBDD comparando Codigo Tipo Via ", e);
            throw new Exception(e);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexon a la BBDD: " + e.getMessage());
            }
        }
    }

    public String getValorCampoTextoTramite(int codOrganizacion, String procedimiento, String numExp, String ejercicio, int tramite, int ocurrencia, String codigoCampo, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            MeLanbide74DAO meLanbide74DAO = MeLanbide74DAO.getInstance();
            con = adapt.getConnection();
            return meLanbide74DAO.getValorCampoTextoTramite(codOrganizacion, procedimiento, numExp, ejercicio, tramite, ocurrencia, codigoCampo, con);
        } catch (Exception e) {
            log.error("Se ha producido una excepcion en la BBDD consultando en la tabla E_TXTT " + codigoCampo, e);
            throw new Exception(e);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexon a la BBDD: " + e.getMessage());
            }
        }
    }

 /*   public int guardarValorCampoTextoTramite(int codOrganizacion, String procedimiento, String ejercicio, String numExp, int tramite, int ocurrencia, String codigoCampo, String valor, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            MeLanbide74DAO meLanbide74DAO = MeLanbide74DAO.getInstance();
            con = adapt.getConnection();
            return meLanbide74DAO.guardarValorCampoTextoTramite(codOrganizacion, procedimiento, ejercicio, numExp, tramite, ocurrencia, codigoCampo, valor, con);
        } catch (Exception e) {
            log.error("Se ha producido una excepcion en la BBDD guardando en la tabla E_TXTT " + codigoCampo, e);
            throw new Exception(e);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexon a la BBDD: " + e.getMessage());
            }
        }
    }
*/
    public int guardarValorCampoNumericoTramite(int codOrganizacion, String procedimiento, String ejercicio, String numExp, int tramite, int ocurrencia, String codigoCampo, BigDecimal valor, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            MeLanbide74DAO m74DAO = MeLanbide74DAO.getInstance();
            con = adapt.getConnection();
            return m74DAO.guardarValorCampoNumericoTramite(codOrganizacion, procedimiento, ejercicio, numExp, tramite, ocurrencia, codigoCampo, valor, con);
        } catch (Exception e) {
            log.error("Se ha producido una excepcion en la BBDD guardando en la tabla E_TNUT " + codigoCampo, e);
            throw new Exception(e);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexon a la BBDD: " + e.getMessage());
            }
        }
    }

    public int guardarValorCampoNumCalculadoTramite(int codOrganizacion, String procedimiento, String ejercicio, String numExp, int tramite, int ocurrencia, String codigoCampo, BigDecimal valor, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            MeLanbide74DAO m74DAO = MeLanbide74DAO.getInstance();
            con = adapt.getConnection();
            return m74DAO.guardarValorCampoNumCalculadoTramite(codOrganizacion, procedimiento, ejercicio, numExp, tramite, ocurrencia, codigoCampo, valor, con);
        } catch (Exception e) {
            log.error("Se ha producido una excepcion en la BBDD guardando en la tabla E_TNUCT " + codigoCampo, e);
            throw new Exception(e);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexon a la BBDD: " + e.getMessage());
            }
        }
    }

    public int borrarRelacionDokusiCST(int codOrg, String numExp, int codTramite, int ocuTramite, String codCampo, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            MeLanbide74DAO m74DAO = MeLanbide74DAO.getInstance();
            con = adapt.getConnection();
            return m74DAO.borrarRelacionDokusiCST(codOrg, numExp, codTramite, ocuTramite, codCampo, con);
        } catch (BDException ex) {
            log.error("Se ha producido una excepcion en la BBDD al borrar la relación con DOKUSI " + codCampo, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexon a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean existeDocumentoTFIT(String numExp, int codTramite, int ocuTramite, String codCampo, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            MeLanbide74DAO m74DAO = MeLanbide74DAO.getInstance();
            con = adapt.getConnection();
            return m74DAO.existeDocumentoTFIT(numExp, codTramite, ocuTramite, codCampo, con);
        } catch (BDException ex) {
            log.error("Se ha producido una excepcion en la BBDD existeDocumentoTFIT ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexon a la BBDD: " + e.getMessage());
            }
        }
    }

    public int borrarCartaPago(String numExp, int codTramite, int ocuTramite, String codCampo, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            MeLanbide74DAO m74DAO = MeLanbide74DAO.getInstance();
            con = adapt.getConnection();
            return m74DAO.borrarCartaPago(numExp, codTramite, ocuTramite, codCampo, con);
        } catch (BDException ex) {
            log.error("Se ha producido una excepcion en la BBDD borrando un fichero de trámite " + codCampo, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexon a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean grabarCartaDokusi(int codOrg, String numExp, String codTramite, String ocuTramite, String codCampo, String nombreFichero, String oid, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            MeLanbide74DAO m74DAO = MeLanbide74DAO.getInstance();
            con = adapt.getConnection();
            return m74DAO.grabarCartaDokusi(codOrg, numExp, codTramite, ocuTramite, codCampo, nombreFichero, oid, con);
        } catch (BDException ex) {
            log.error("Se ha producido una excepcion en la BBDD grabando un fichero de trámite " + codCampo, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexon a la BBDD: " + e.getMessage());
            }
        }
    }

    public String getCodigoSubvencionEika(String ejerSubvencion, String codProcedimiento, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return MeLanbide74DAO.getInstance().getCodigoSubvencionEika(ejerSubvencion, codProcedimiento, con);
        } catch (Exception e) {
            log.error("Se ha producido una excepcion en la BBDD grecuperando el código de la subvencion  ", e);
            throw new Exception(e);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexon a la BBDD: " + e.getMessage());
            }
        }
    }
}//class
