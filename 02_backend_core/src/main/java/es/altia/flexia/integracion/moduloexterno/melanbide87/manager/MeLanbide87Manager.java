package es.altia.flexia.integracion.moduloexterno.melanbide87.manager;

import es.altia.flexia.integracion.moduloexterno.melanbide87.dao.MeLanbide87DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide87.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide87.util.ConstantesMeLanbide87;
import es.altia.flexia.integracion.moduloexterno.melanbide87.vo.DesplegableAdmonLocalVO;
import es.altia.flexia.integracion.moduloexterno.melanbide87.vo.InstalacionVO;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class MeLanbide87Manager {

    //Logger
    private static final Logger log = LogManager.getLogger(MeLanbide87Manager.class);

    //Instancia
    private static MeLanbide87Manager instance = null;

    public static MeLanbide87Manager getInstance() {
        if (instance == null) {
            synchronized (MeLanbide87Manager.class) {
                instance = new MeLanbide87Manager();
            }
        }
        return instance;
    }

    public List<InstalacionVO> getListaInstalaciones(String numExp, int codOrganizacion, AdaptadorSQLBD adapt) throws Exception {
        List<InstalacionVO> lista = new ArrayList<InstalacionVO>();
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide87DAO meLanbide84DAO = MeLanbide87DAO.getInstance();
            lista = meLanbide84DAO.getListaInstalaciones(numExp, codOrganizacion, con);
            //recuperamos los cod y desc de desplegables para traducir en la tabla principal
            List<DesplegableAdmonLocalVO> listaTipoInst = MeLanbide87Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide87.COD_DES_TINS, ConstantesMeLanbide87.FICHERO_PROPIEDADES), adapt);
            for (InstalacionVO pers : lista) {
                for (DesplegableAdmonLocalVO valordesp : listaTipoInst) {
                    if (valordesp.getDes_val_cod().equals(pers.getTipoInst())) {
                        pers.setDescTipoInst(valordesp.getDes_nom());
                        break;
                    }
                }               
            }
            return lista;
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos sobre las instalaciónes ", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre las instalaciónes ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public InstalacionVO getInstalacionPorID(String id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide87DAO meLanbide84DAO = MeLanbide87DAO.getInstance();
            return meLanbide84DAO.getInstalacionPorID(id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos sobre una instalación:  " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre una instalación:  " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean crearNuevaInstalacion(InstalacionVO nuevaInstalacion, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        boolean insertOK;
        try {
            con = adapt.getConnection();
            MeLanbide87DAO meLanbide84DAO = MeLanbide87DAO.getInstance();
            insertOK = meLanbide84DAO.crearNuevaInstalacion(nuevaInstalacion, con);
        } catch (BDException e) {
            log.error("Se ha producido una Excepcion en la BBDD creando una instalación : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD creando una instalación : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        return insertOK;
    }

    public boolean modificarInstalacion(InstalacionVO instalacionModif, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide87DAO meLanbide84DAO = MeLanbide87DAO.getInstance();
            return meLanbide84DAO.modificarInstalacion(instalacionModif, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD Actualizando una instalación : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD Actualizando una instalación : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public int eliminarInstalacion(String id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide87DAO meLanbide84DAO = MeLanbide87DAO.getInstance();
            return meLanbide84DAO.eliminarInstalacion(id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD al Eliminar una instalación:  " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD al Eliminar una instalación:   " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public List<DesplegableAdmonLocalVO> getValoresDesplegablesAdmonLocalxdes_cod(String des_cod, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide87DAO meLanbide84DAO = MeLanbide87DAO.getInstance();
            return meLanbide84DAO.getValoresDesplegablesAdmonLocalxdes_cod(des_cod, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando los valores de un Desplegable : " + des_cod, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando los valores de un desplegable :  " + des_cod, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

}
