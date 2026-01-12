
package es.altia.flexia.integracion.moduloexterno.melanbide89.manager;

import es.altia.flexia.integracion.moduloexterno.melanbide89.vo.AccesoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide89.dao.MeLanbide89DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide89.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide89.util.ConstantesMeLanbide89;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class MeLanbide89Manager {

    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide89Manager.class);

    //Instancia
    private static MeLanbide89Manager instance = null;

    public static MeLanbide89Manager getInstance() {
        if (instance == null) {
            synchronized (MeLanbide89Manager.class) {
                instance = new MeLanbide89Manager();
            }
        }
        return instance;
    }

    public List<AccesoVO> getListaAccesos(String numExp, int codOrganizacion, AdaptadorSQLBD adapt) throws Exception {
        List<AccesoVO> lista = new ArrayList<AccesoVO>();
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide89DAO meLanbide89DAO = MeLanbide89DAO.getInstance();
            lista = meLanbide89DAO.getListaAccesos(numExp, codOrganizacion, con);
            return lista;
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD recuperando datos sobre los accesos ", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD recuperando datos sobre los accesos ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }

    public AccesoVO getAccesoPorID(String id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide89DAO meLanbide89DAO = MeLanbide89DAO.getInstance();
            return meLanbide89DAO.getAccesoPorID(id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre un acceso:  " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre un acceso:  " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean crearAcceso(AccesoVO nuevoAcceso, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        boolean insertOK;
        try {
            con = adapt.getConnection();
            MeLanbide89DAO meLanbide89DAO = MeLanbide89DAO.getInstance();
            insertOK = meLanbide89DAO.crearAcceso(nuevoAcceso, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD creando un acceso : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD creando un acceso : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
        return insertOK;
    }

    public boolean modificarAcceso(AccesoVO accesoModif, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide89DAO meLanbide89DAO = MeLanbide89DAO.getInstance();
            return meLanbide89DAO.modificarAcceso(accesoModif, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD actualizando un acceso : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD actualizando un acceso : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public int eliminarAcceso(String id, String numExp, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide89DAO meLanbide89DAO = MeLanbide89DAO.getInstance();
            return meLanbide89DAO.eliminarAcceso(id, numExp, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD al eliminar un acceso:  " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD al eliminar un acceso:   " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }
  
}
