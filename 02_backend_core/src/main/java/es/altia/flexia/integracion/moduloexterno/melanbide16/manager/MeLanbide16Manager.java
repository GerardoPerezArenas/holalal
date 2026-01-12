/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide16.manager;

import es.altia.flexia.integracion.moduloexterno.melanbide16.dao.MeLanbide16DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide16.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide16.util.ConstantesMeLanbide16;
import es.altia.flexia.integracion.moduloexterno.melanbide16.vo.DesplegableAdmonLocalVO;
import es.altia.flexia.integracion.moduloexterno.melanbide16.vo.FilaMinimisVO;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author santiagoc
 */
public class MeLanbide16Manager {

    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide16Manager.class);

    //Instancia
    private static MeLanbide16Manager instance = null;

    private MeLanbide16Manager() {

    }

    public static MeLanbide16Manager getInstance() {
        if (instance == null) {
            synchronized (MeLanbide16Manager.class) {
                instance = new MeLanbide16Manager();
            }
        }
        return instance;
    }

    public List<FilaMinimisVO> getDatosMinimis(String numExp, int codOrganizacion, AdaptadorSQLBD adapt) throws Exception {
        List<FilaMinimisVO> lista = new ArrayList<FilaMinimisVO>();
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide16DAO meLanbide16DAO = MeLanbide16DAO.getInstance();
            lista = meLanbide16DAO.getDatosMinimis(numExp, codOrganizacion, con);
            //recuperamos los cod y desc de desplegables para traducir en la tabla principal
            List<DesplegableAdmonLocalVO> listaEstado = MeLanbide16Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide16.COD_DES_DTSV, ConstantesMeLanbide16.FICHERO_PROPIEDADES), adapt);

            for (FilaMinimisVO cont : lista) {
                for (DesplegableAdmonLocalVO valordesp : listaEstado) {
                    if (valordesp.getDes_val_cod().equals(cont.getEstado())) {
                        cont.setDesEstado(valordesp.getDes_nom());
                        break;
                    }
                }
            }
            return lista;
        } catch (BDException e) {
            log.error("Se ha producido una excepci0n en la BBDD recuperando datos sobre las minimis ", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre las minimis ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public FilaMinimisVO getMinimisPorID(String id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide16DAO meLanbide16DAO = MeLanbide16DAO.getInstance();
            return meLanbide16DAO.getMinimisPorID(id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre una minimis:  " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre una minimis:  " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public int eliminarMinimis(String id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide16DAO meLanbide16DAO = MeLanbide16DAO.getInstance();
            return meLanbide16DAO.eliminarMinimis(id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepci?n en la BBDD al eliminar una minimis:  " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepci?n en la BBDD al eliminar una minimis:   " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexi?n a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean crearNuevoMinimis(FilaMinimisVO nuevaMinimis, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        boolean insertOK;
        try {
            con = adapt.getConnection();
            MeLanbide16DAO meLanbide16DAO = MeLanbide16DAO.getInstance();
            insertOK = meLanbide16DAO.crearNuevoMinimis(nuevaMinimis, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepci?n en la BBDD creando una minimis : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepci?n en la BBDD creando una minimis : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexi?n a la BBDD: " + e.getMessage());
            }
        }
        return insertOK;
    }

    public boolean modificarMinimis(FilaMinimisVO datModif, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        boolean insertOK;
        try {
            con = adapt.getConnection();
            MeLanbide16DAO meLanbide16DAO = MeLanbide16DAO.getInstance();
            insertOK = meLanbide16DAO.modificarMinimis(datModif, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepci?n en la BBDD actualizando una minimis : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepci?n en la BBDD actualizando una minimis : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexi?n a la BBDD: " + e.getMessage());
            }
        }
        return insertOK;
    }

    public List<DesplegableAdmonLocalVO> getValoresDesplegablesAdmonLocalxdes_cod(String des_cod, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide16DAO meLanbide16DAO = MeLanbide16DAO.getInstance();
            return meLanbide16DAO.getValoresDesplegablesAdmonLocalxdes_cod(des_cod, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepci?n en la BBDD recuperando valores de desplegable : " + des_cod, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepci?n en la BBDD recuperando valores de desplegable :  " + des_cod, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexi?n a la BBDD: " + e.getMessage());
            }
        }
    }
}
