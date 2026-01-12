package es.altia.flexia.integracion.moduloexterno.melanbide14.manager;

import es.altia.flexia.integracion.moduloexterno.melanbide14.dao.MeLanbide14DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide14.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide14.util.ConstantesMeLanbide14;
import es.altia.flexia.integracion.moduloexterno.melanbide14.vo.DesplegableAdmonLocalVO;
import es.altia.flexia.integracion.moduloexterno.melanbide14.vo.OperacionSolicitadaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide14.vo.OperacionVO;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class MeLanbide14Manager {

    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide14Manager.class);

    //Instancia
    private static MeLanbide14Manager instance = null;

    public static MeLanbide14Manager getInstance() {
        if (instance == null) {
            synchronized (MeLanbide14Manager.class) {
                instance = new MeLanbide14Manager();
            }
        }
        return instance;
    }

    public List<OperacionVO> getListaOperaciones(final String numExp, int codOrganizacion, final AdaptadorSQLBD adapt) throws Exception {
        List<OperacionVO> lista = new ArrayList<OperacionVO>();
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide14DAO meLanbide14DAO = MeLanbide14DAO.getInstance();
            lista = meLanbide14DAO.getListaOperaciones(numExp, codOrganizacion, con);

            List<DesplegableAdmonLocalVO> listaPrio = MeLanbide14Manager.getInstance().getValoresE_DES_VAL(ConfigurationParameter.getParameter(ConstantesMeLanbide14.COD_DES_PRIO, ConstantesMeLanbide14.FICHERO_PROPIEDADES), adapt);
            List<DesplegableAdmonLocalVO> listaLin1 = MeLanbide14Manager.getInstance().getValoresE_DES_VAL(ConfigurationParameter.getParameter(ConstantesMeLanbide14.COD_DES_LINACT1, ConstantesMeLanbide14.FICHERO_PROPIEDADES), adapt);
            List<DesplegableAdmonLocalVO> listaLin2 = MeLanbide14Manager.getInstance().getValoresE_DES_VAL(ConfigurationParameter.getParameter(ConstantesMeLanbide14.COD_DES_LINACT2, ConstantesMeLanbide14.FICHERO_PROPIEDADES), adapt);
            List<DesplegableAdmonLocalVO> listaLin3 = MeLanbide14Manager.getInstance().getValoresE_DES_VAL(ConfigurationParameter.getParameter(ConstantesMeLanbide14.COD_DES_LINACT3, ConstantesMeLanbide14.FICHERO_PROPIEDADES), adapt);

            for (OperacionVO cont : lista) {
                cont.setDesPrio("");
                for (DesplegableAdmonLocalVO valordesp : listaPrio) {
                    if (valordesp.getDes_val_cod().equals(cont.getPrio())) {
                        cont.setDesPrio(valordesp.getDes_nom());
                    }
                }
                cont.setDesLin1("");
                for (DesplegableAdmonLocalVO valordesp : listaLin1) {
                    if (valordesp.getDes_val_cod().equals(cont.getLin1())) {
                        cont.setDesLin1(valordesp.getDes_nom());
                    }
                }
                cont.setDesLin2("");
                for (DesplegableAdmonLocalVO valordesp : listaLin2) {
                    if (valordesp.getDes_val_cod().equals(cont.getLin2())) {
                        cont.setDesLin2(valordesp.getDes_nom());
                    }
                }
                cont.setDesLin3("");
                for (DesplegableAdmonLocalVO valordesp : listaLin3) {
                    if (valordesp.getDes_val_cod().equals(cont.getLin3())) {
                        cont.setDesLin3(valordesp.getDes_nom());
                    }
                }
            }
            return lista;
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD recuperando datos sobre las operaciones ", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD recuperando datos sobre las operaciones ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }

    public OperacionVO getOperacionPorID(final String id, final AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide14DAO meLanbide14DAO = MeLanbide14DAO.getInstance();
            return meLanbide14DAO.getOperacionPorID(id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre una operacion: " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre un acceso:  " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean crearOperacion(final OperacionVO nuevaOperacion, final AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        boolean insertOK;
        try {
            con = adapt.getConnection();
            MeLanbide14DAO meLanbide14DAO = MeLanbide14DAO.getInstance();
            insertOK = meLanbide14DAO.crearOperacion(nuevaOperacion, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD creando una operacion : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD creando una operacion : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
        return insertOK;
    }

    public boolean modificarOperacion(final OperacionVO operacionModif, final AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide14DAO meLanbide14DAO = MeLanbide14DAO.getInstance();
            return meLanbide14DAO.modificarOperacion(operacionModif, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD actualizando una operacion : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD actualizando una operacion : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }

    public int eliminarOperacion(final String id, final String numExp, final AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide14DAO meLanbide14DAO = MeLanbide14DAO.getInstance();
            return meLanbide14DAO.eliminarOperacion(id, numExp, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD al eliminar una operacion: " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD al eliminar una operacion: " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }

    /*      OPERACIONES SOLICITADAS     */
    public List<String> getEjerciciosSolicitados(String numExp, AdaptadorSQLBD adapt) throws Exception {
        log.debug("==> getEjerciciosPresentados -  Manager");
        Connection con = null;
        try {
            con = adapt.getConnection();
            return MeLanbide14DAO.getInstance().getEjerciciosSolicitados(numExp, con);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD al recuperar los ejercicios presentados: ", ex);
            throw new Exception(ex);
        } finally {
            adapt.devolverConexion(con);
        }
    }

    public List<OperacionSolicitadaVO> getOperacionesSolicitadasEjercicio(String numExp, String ejercicio, AdaptadorSQLBD adapt) throws Exception {
        log.debug("==> getOperacionesSolicitadasEjercicio -  Manager");
        Connection con = null;
        try {
            con = adapt.getConnection();
            return MeLanbide14DAO.getInstance().getOperacionesSolicitadasEjercicio(numExp, ejercicio, con);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD al recuperar los ejercicios presentados: ", ex);
            throw new Exception(ex);
        } finally {
            adapt.devolverConexion(con);
        }
    }

    public OperacionSolicitadaVO getOperacionSolicitadaPorID(String id, AdaptadorSQLBD adapt) throws Exception {
        log.debug("==> getOperacionSolicitadaPorID -  Manager");
        Connection con = null;
        try {
            con = adapt.getConnection();
            return MeLanbide14DAO.getInstance().getOperacionSolocitadaPorId(id, con);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD recuperando una factura ", ex);
            throw new Exception(ex);
        } finally {
            adapt.devolverConexion(con);
        }
    }

    public boolean nuevaOpeSolicitada(OperacionSolicitadaVO operacion, AdaptadorSQLBD adapt) throws Exception {
        log.debug("==> nuevaOpeSolicitada -  Manager");
        Connection con = null;
        try {
            con = adapt.getConnection();
            return MeLanbide14DAO.getInstance().nuevaOpeSolicitada(operacion, con);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD recuperando una factura ", ex);
            throw new Exception(ex);
        } finally {
            adapt.devolverConexion(con);
        }
    }

    public boolean modificarOpeSolicitada(OperacionSolicitadaVO operacion, AdaptadorSQLBD adapt) throws Exception {
        log.debug("==> modificarOpeSolicitada -  Manager");
        Connection con = null;
        try {
            con = adapt.getConnection();
            return MeLanbide14DAO.getInstance().modificarOpeSolicitada(operacion, con);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD recuperando una factura ", ex);
            throw new Exception(ex);
        } finally {
            adapt.devolverConexion(con);
        }
    }

    public boolean eliminarOpeSolicitada(String id, AdaptadorSQLBD adapt) throws Exception {
        log.debug("==> eliminarOpeSolicitada -  Manager");
        Connection con = null;
        try {
            con = adapt.getConnection();
            return MeLanbide14DAO.getInstance().eliminarOpeSolicitada(id, con);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD recuperando una factura ", ex);
            throw new Exception(ex);
        } finally {
            adapt.devolverConexion(con);
        }
    }

    /**
     *
     * @param des_cod
     * @param adapt
     * @return
     * @throws Exception
     */
    public List<DesplegableAdmonLocalVO> getValoresE_DES_VAL(String des_cod, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return MeLanbide14DAO.getInstance().getValoresE_DES_VAL(des_cod, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD recuperando valores de desplegable : " + des_cod, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción recuperando valores de desplegable :  " + des_cod, ex);
            throw new Exception(ex);
        } finally {
            adapt.devolverConexion(con);
        }
    }

}
