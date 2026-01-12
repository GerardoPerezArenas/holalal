package es.altia.flexia.integracion.moduloexterno.melanbide12.manager;

import es.altia.flexia.integracion.moduloexterno.melanbide12.dao.MeLanbide12DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide12.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide12.util.ConstantesMeLanbide12;
import es.altia.flexia.integracion.moduloexterno.melanbide12.vo.DesplegableAdmonLocalVO;
import es.altia.flexia.integracion.moduloexterno.melanbide12.vo.FilaMinimisVO;
import es.altia.flexia.integracion.moduloexterno.melanbide12.vo.FilaL1ParticipanteVO;
import es.altia.flexia.integracion.moduloexterno.melanbide12.vo.FilaL1EmpresaExternaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide12.vo.FilaL1EmpresaPropiaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide12.vo.FilaL2ParticipanteVO;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class MeLanbide12Manager {

    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide12Manager.class);

    //Instancia
    private static MeLanbide12Manager instance = null;

    public static MeLanbide12Manager getInstance() {
        if (instance == null) {
            synchronized (MeLanbide12Manager.class) {
                instance = new MeLanbide12Manager();
            }
        }
        return instance;
    }

    public List<FilaMinimisVO> getDatosMinimis(String numExp, int codOrganizacion, AdaptadorSQLBD adapt) throws Exception {
        List<FilaMinimisVO> lista = new ArrayList<FilaMinimisVO>();
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide12DAO meLanbide12DAO = MeLanbide12DAO.getInstance();
            lista = meLanbide12DAO.getDatosMinimis(numExp, codOrganizacion, con);
            //recuperamos los cod y desc de desplegables para traducir en la tabla principal
            List<DesplegableAdmonLocalVO> listaEstado = MeLanbide12Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide12.COD_DES_ESTADO_AYUDA, ConstantesMeLanbide12.FICHERO_PROPIEDADES), adapt);

            for (FilaMinimisVO cont : lista) {
                for (DesplegableAdmonLocalVO valordesp : listaEstado) {
                    if (valordesp.getDes_val_cod().equals(cont.getEstado())) {
                        cont.setDesEstado(valordesp.getDes_nom());
                        log.debug("Estado: "+cont.getDesEstado());
                        break;
                    }
                }
            }

            return lista;
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD recuperando datos sobre las minimis ", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD recuperando datos sobre las minimis ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }

    public FilaMinimisVO getMinimisPorID(String id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide12DAO meLanbide12DAO = MeLanbide12DAO.getInstance();
            return meLanbide12DAO.getMinimisPorID(id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre una minimis:  " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre una minimis:  " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public int eliminarMinimis(String id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide12DAO meLanbide12DAO = MeLanbide12DAO.getInstance();
            return meLanbide12DAO.eliminarMinimis(id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD al eliminar una minimis:  " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD al eliminar una minimis:   " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean crearNuevoMinimis(FilaMinimisVO nuevaMinimis, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        boolean insertOK;
        try {
            con = adapt.getConnection();
            MeLanbide12DAO meLanbide12DAO = MeLanbide12DAO.getInstance();
            insertOK = meLanbide12DAO.crearNuevoMinimis(nuevaMinimis, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD creando una minimis : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD creando una minimis : " + ex.getMessage(), ex);
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

    public boolean modificarMinimis(FilaMinimisVO datModif, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        boolean insertOK;
        try {
            con = adapt.getConnection();
            MeLanbide12DAO meLanbide12DAO = MeLanbide12DAO.getInstance();
            insertOK = meLanbide12DAO.modificarMinimis(datModif, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD actualizando una minimis : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD actualizando una minimis : " + ex.getMessage(), ex);
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

    public List<DesplegableAdmonLocalVO> getValoresDesplegablesAdmonLocalxdes_cod(String des_cod, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide12DAO meLanbide12DAO = MeLanbide12DAO.getInstance();
            return meLanbide12DAO.getValoresDesplegablesAdmonLocalxdes_cod(des_cod, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD recuperando valores de desplegable : " + des_cod, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD recuperando valores de desplegable :  " + des_cod, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }

    public String getValorDesplegableExpediente(int codOrg, String numExp, String codCampo, AdaptadorSQLBD adapt) throws Exception {
                log.debug("==> getValorDesplegableExpediente Manager");
    Connection con = null;
        try {
            con = adapt.getConnection();
            return MeLanbide12DAO.getInstance().getValorDesplegableExpediente(codOrg, numExp, codCampo, con);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD recuperando un desplegable de expediente ", ex);
            throw new Exception(ex);
        } finally {
            adapt.devolverConexion(con);
        }
    }

    public List<FilaL1ParticipanteVO> getDatosL1Participante(String numExp, int codOrganizacion, AdaptadorSQLBD adapt) throws Exception {
        List<FilaL1ParticipanteVO> lista = new ArrayList<FilaL1ParticipanteVO>();
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide12DAO meLanbide12DAO = MeLanbide12DAO.getInstance();
            lista = meLanbide12DAO.getDatosL1Participante(numExp, codOrganizacion, con);
            //recuperamos los cod y desc de desplegables para traducir en la tabla principal
            List<DesplegableAdmonLocalVO> listaTipoDoc = MeLanbide12Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide12.COD_DES_TIP_DOC, ConstantesMeLanbide12.FICHERO_PROPIEDADES), adapt);

            for (FilaL1ParticipanteVO cont : lista) {
                for (DesplegableAdmonLocalVO valordesp : listaTipoDoc) {
                    if (valordesp.getDes_val_cod().equals(cont.getTipoDoc())) {
                        cont.setDesTipoDoc(valordesp.getDes_nom());
                        log.debug("TipoDoc: "+cont.getDesTipoDoc());
                        break;
                    }
                }
            }
            return lista;
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD recuperando datos sobre L1Participante ", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD recuperando datos sobre L1Participante ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }

    public FilaL1ParticipanteVO getL1ParticipantePorID(String id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide12DAO meLanbide12DAO = MeLanbide12DAO.getInstance();
            return meLanbide12DAO.getL1ParticipantePorID(id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre L1Participante:  " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre L1Participante:  " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public int eliminarL1Participante(String id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide12DAO meLanbide12DAO = MeLanbide12DAO.getInstance();
            return meLanbide12DAO.eliminarL1Participante(id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD al eliminar L1Participante:  " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD al eliminar L1Participante:   " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean crearNuevoL1Participante(FilaL1ParticipanteVO nuevoL1Participante, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        boolean insertOK;
        try {
            con = adapt.getConnection();
            MeLanbide12DAO meLanbide12DAO = MeLanbide12DAO.getInstance();
            insertOK = meLanbide12DAO.crearNuevoL1Participante(nuevoL1Participante, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD creando L1Participante : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD creando L1Participante : " + ex.getMessage(), ex);
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

    public boolean modificarL1Participante(FilaL1ParticipanteVO datModif, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        boolean insertOK;
        try {
            con = adapt.getConnection();
            MeLanbide12DAO meLanbide12DAO = MeLanbide12DAO.getInstance();
            insertOK = meLanbide12DAO.modificarL1Participante(datModif, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD actualizando L1Participante : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD actualizando L1Participante : " + ex.getMessage(), ex);
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

    public List<FilaL1EmpresaExternaVO> getDatosL1EmpresaExterna(String numExp, int codOrganizacion, AdaptadorSQLBD adapt) throws Exception {
        List<FilaL1EmpresaExternaVO> lista = new ArrayList<FilaL1EmpresaExternaVO>();
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide12DAO meLanbide12DAO = MeLanbide12DAO.getInstance();
            lista = meLanbide12DAO.getDatosL1EmpresaExterna(numExp, codOrganizacion, con);
            return lista;
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD recuperando datos sobre L1EmpresaExterna ", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD recuperando datos sobre L1EmpresaExterna ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }

    public FilaL1EmpresaExternaVO getL1EmpresaExternaPorID(String id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide12DAO meLanbide12DAO = MeLanbide12DAO.getInstance();
            return meLanbide12DAO.getL1EmpresaExternaPorID(id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre L1EmpresaExterna:  " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre L1EmpresaExterna:  " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public int eliminarL1EmpresaExterna(String id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide12DAO meLanbide12DAO = MeLanbide12DAO.getInstance();
            return meLanbide12DAO.eliminarL1EmpresaExterna(id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD al eliminar L1EmpresaExterna:  " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD al eliminar L1EmpresaExterna:   " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean crearNuevoL1EmpresaExterna(FilaL1EmpresaExternaVO nuevoL1EmpresaExterna, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        boolean insertOK;
        try {
            con = adapt.getConnection();
            MeLanbide12DAO meLanbide12DAO = MeLanbide12DAO.getInstance();
            insertOK = meLanbide12DAO.crearNuevoL1EmpresaExterna(nuevoL1EmpresaExterna, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD creando L1EmpresaExterna : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD creando L1EmpresaExterna : " + ex.getMessage(), ex);
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

    public boolean modificarL1EmpresaExterna(FilaL1EmpresaExternaVO datModif, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        boolean insertOK;
        try {
            con = adapt.getConnection();
            MeLanbide12DAO meLanbide12DAO = MeLanbide12DAO.getInstance();
            insertOK = meLanbide12DAO.modificarL1EmpresaExterna(datModif, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD actualizando L1EmpresaExterna : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD actualizando L1EmpresaExterna : " + ex.getMessage(), ex);
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

    public List<FilaL1EmpresaPropiaVO> getDatosL1EmpresaPropia(String numExp, int codOrganizacion, AdaptadorSQLBD adapt) throws Exception {
        List<FilaL1EmpresaPropiaVO> lista = new ArrayList<FilaL1EmpresaPropiaVO>();
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide12DAO meLanbide12DAO = MeLanbide12DAO.getInstance();
            lista = meLanbide12DAO.getDatosL1EmpresaPropia(numExp, codOrganizacion, con);
            return lista;
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD recuperando datos sobre L1EmpresaPropia ", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD recuperando datos sobre L1EmpresaPropia ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }

    public FilaL1EmpresaPropiaVO getL1EmpresaPropiaPorID(String id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide12DAO meLanbide12DAO = MeLanbide12DAO.getInstance();
            return meLanbide12DAO.getL1EmpresaPropiaPorID(id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre L1EmpresaPropia:  " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre L1EmpresaPropia:  " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public int eliminarL1EmpresaPropia(String id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide12DAO meLanbide12DAO = MeLanbide12DAO.getInstance();
            return meLanbide12DAO.eliminarL1EmpresaPropia(id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD al eliminar L1EmpresaPropia:  " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD al eliminar L1EmpresaPropia:   " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean crearNuevoL1EmpresaPropia(FilaL1EmpresaPropiaVO nuevoL1EmpresaPropia, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        boolean insertOK;
        try {
            con = adapt.getConnection();
            MeLanbide12DAO meLanbide12DAO = MeLanbide12DAO.getInstance();
            insertOK = meLanbide12DAO.crearNuevoL1EmpresaPropia(nuevoL1EmpresaPropia, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD creando L1EmpresaPropia : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD creando L1EmpresaPropia : " + ex.getMessage(), ex);
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

    public boolean modificarL1EmpresaPropia(FilaL1EmpresaPropiaVO datModif, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        boolean insertOK;
        try {
            con = adapt.getConnection();
            MeLanbide12DAO meLanbide12DAO = MeLanbide12DAO.getInstance();
            insertOK = meLanbide12DAO.modificarL1EmpresaPropia(datModif, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD actualizando L1EmpresaPropia : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD actualizando L1EmpresaPropia : " + ex.getMessage(), ex);
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

    public List<FilaL2ParticipanteVO> getDatosL2Participante(String numExp, int codOrganizacion, AdaptadorSQLBD adapt) throws Exception {
        List<FilaL2ParticipanteVO> lista = new ArrayList<FilaL2ParticipanteVO>();
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide12DAO meLanbide12DAO = MeLanbide12DAO.getInstance();
            lista = meLanbide12DAO.getDatosL2Participante(numExp, codOrganizacion, con);
            //recuperamos los cod y desc de desplegables para traducir en la tabla principal
            List<DesplegableAdmonLocalVO> listaTipoDoc = MeLanbide12Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide12.COD_DES_TIP_DOC_L2, ConstantesMeLanbide12.FICHERO_PROPIEDADES), adapt);

            for (FilaL2ParticipanteVO cont : lista) {
                for (DesplegableAdmonLocalVO valordesp : listaTipoDoc) {
                    if (valordesp.getDes_val_cod().equals(cont.getTipoDoc())) {
                        cont.setDesTipoDoc(valordesp.getDes_nom());
                        log.debug("TipoDoc: "+cont.getDesTipoDoc());
                        break;
                    }
                }
            }
            return lista;
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD recuperando datos sobre L2Participante ", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD recuperando datos sobre L2Participante ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }

    public FilaL2ParticipanteVO getL2ParticipantePorID(String id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide12DAO meLanbide12DAO = MeLanbide12DAO.getInstance();
            return meLanbide12DAO.getL2ParticipantePorID(id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre L2Participante:  " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre L2Participante:  " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public int eliminarL2Participante(String id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide12DAO meLanbide12DAO = MeLanbide12DAO.getInstance();
            return meLanbide12DAO.eliminarL2Participante(id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD al eliminar L2Participante:  " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD al eliminar L2Participante:   " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean crearNuevoL2Participante(FilaL2ParticipanteVO nuevoL2Participante, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        boolean insertOK;
        try {
            con = adapt.getConnection();
            MeLanbide12DAO meLanbide12DAO = MeLanbide12DAO.getInstance();
            insertOK = meLanbide12DAO.crearNuevoL2Participante(nuevoL2Participante, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD creando L2Participante : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD creando L2Participante : " + ex.getMessage(), ex);
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

    public boolean modificarL2Participante(FilaL2ParticipanteVO datModif, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        boolean insertOK;
        try {
            con = adapt.getConnection();
            MeLanbide12DAO meLanbide12DAO = MeLanbide12DAO.getInstance();
            insertOK = meLanbide12DAO.modificarL2Participante(datModif, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD actualizando L2Participante : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD actualizando L2Participante : " + ex.getMessage(), ex);
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

}
