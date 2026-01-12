package es.altia.flexia.integracion.moduloexterno.melanbide81.manager;

import com.lanbide.lan6.errores.bean.ErrorBean;
import com.lanbide.lan6.registro.error.RegistroErrores;
import es.altia.flexia.integracion.moduloexterno.melanbide81.dao.MeLanbide81DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide81.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide81.util.ConstantesMeLanbide81;
import es.altia.flexia.integracion.moduloexterno.melanbide81.vo.ContratacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide81.vo.DesplegableVO;
import es.altia.flexia.integracion.moduloexterno.melanbide81.vo.ProyectoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide81.vo.PuestoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide81.vo.Tipo1VO;
import es.altia.flexia.integracion.moduloexterno.melanbide81.vo.Tipo2VO;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author kepa
 */
public class MeLanbide81Manager {

    //Logger
    private static final Logger log = LogManager.getLogger(MeLanbide81Manager.class);
    private final MeLanbide81DAO m81DAO = new MeLanbide81DAO();

    //Instancia
    private static MeLanbide81Manager instance = null;

    /**
     *
     * @return
     */
    public static MeLanbide81Manager getInstance() {
        if (instance == null) {
            synchronized (MeLanbide81Manager.class) {
                instance = new MeLanbide81Manager();
            }
        }
        return instance;
    }

    /**
     *
     * @param des_cod
     * @param adapt
     * @return
     * @throws Exception
     */
    public List<DesplegableVO> getValoresDesplegables(String des_cod, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return m81DAO.getValoresE_Des(des_cod, con);
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

    /**
     *
     * @param numExp
     * @param adapt
     * @return
     * @throws Exception
     */
    public List<ProyectoVO> getListaProyectos(String numExp, AdaptadorSQLBD adapt) throws Exception {
        List<ProyectoVO> lista = new ArrayList<ProyectoVO>();
        Connection con = null;
        try {
            con = adapt.getConnection();
            lista = m81DAO.getListaProyectos(numExp, con);
            List<DesplegableVO> listaTipo = getValoresDesplegables(ConfigurationParameter.getParameter(ConstantesMeLanbide81.COD_TIPO_PROY, ConstantesMeLanbide81.FICHERO_PROPIEDADES), adapt);
            for (ProyectoVO proy : lista) {
                for (DesplegableVO desp : listaTipo) {
                    if (desp.getDes_val_cod().equals(proy.getTipoProyecto())) {
                        proy.setDescTipoProyecto(desp.getDes_nom());
                        break;
                    }
                }
            }
            return lista;
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando la lista de Proyectos ", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando la lista de Proyectos ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    /**
     *
     * @param numExp
     * @param id
     * @param adapt
     * @return
     * @throws Exception
     */
    public ProyectoVO getProyectoPorID(String numExp, String id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return m81DAO.getProyectoPorID(numExp, id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre un Proyecto:  " + id, e);
            throw new Exception(e);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    /**
     *
     * @param nuevoProyecto
     * @param adapt
     * @return
     * @throws Exception
     */
    public boolean crearProyecto(ProyectoVO nuevoProyecto, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return m81DAO.crearProyecto(nuevoProyecto, con);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion grabando un nuevo Proyecto:  ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }

    /**
     *
     * @param datModif
     * @param adapt
     * @return
     * @throws Exception
     */
    public boolean modificarProyecto(ProyectoVO datModif, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return m81DAO.modificarProyecto(datModif, con);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion modificando un Proyecto:  ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }

    /**
     *
     * @param numExp
     * @param id
     * @param adapt
     * @return
     * @throws Exception
     */
    public boolean eliminarProyecto(String numExp, int id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return m81DAO.eliminarProyecto(numExp, id, con);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion eliminando un Proyecto:  ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }

    /**
     *
     * @param numExp
     * @param idProyecto
     * @param adapt
     * @return
     * @throws Exception
     */
    public List<PuestoVO> getListaPuestosxProyecto(String numExp, int idProyecto, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return m81DAO.getListaPuestosxProyecto(numExp, idProyecto, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando la lista de Puestos ", e);
            throw new Exception(e);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    /**
     *
     * @param numExp
     * @param id
     * @param adapt
     * @return
     * @throws Exception
     */
    public PuestoVO getPuestoPorID(String numExp, String id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return m81DAO.getPuestoPorID(numExp, id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando la lista de Puestos ", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando la lista de Puestos ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    /**
     *
     * @param nuevoPuesto
     * @param adapt
     * @return
     * @throws Exception
     */
    public boolean crearPuesto(PuestoVO nuevoPuesto, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return m81DAO.crearPuesto(nuevoPuesto, con);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion grabando un nuevo Puesto:  ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }

    /**
     *
     * @param datModif
     * @param adapt
     * @return
     * @throws Exception
     */
    public boolean modificarPuesto(PuestoVO datModif, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return m81DAO.modificarPuesto(datModif, con);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion modificando un Puesto:  ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }

    /**
     *
     * @param numExp
     * @param id
     * @param adapt
     * @return
     * @throws Exception
     */
    public boolean eliminarPuesto(String numExp, int id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return m81DAO.eliminarPuesto(numExp, id, con);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion eliminando un Puesto:  ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }

    /**
     *
     * @param numExp
     * @param idProyecto
     * @param adapt
     * @return
     * @throws Exception
     */
    public List<ContratacionVO> getListaContratacionesxProyecto(String numExp, int idProyecto, AdaptadorSQLBD adapt) throws Exception {
        List<ContratacionVO> lista = new ArrayList<ContratacionVO>();
        Connection con = null;
        try {
            con = adapt.getConnection();
            lista = m81DAO.getListaContratacionesxProyecto(numExp, idProyecto, con);
            List<DesplegableVO> listaTipo = getValoresDesplegables(ConfigurationParameter.getParameter(ConstantesMeLanbide81.COD_TIPO_DESE, ConstantesMeLanbide81.FICHERO_PROPIEDADES), adapt);
            List<DesplegableVO> listaSexo = getValoresDesplegables(ConfigurationParameter.getParameter(ConstantesMeLanbide81.COD_SEXO, ConstantesMeLanbide81.FICHERO_PROPIEDADES), adapt);
            for (ContratacionVO contr : lista) {
                for (DesplegableVO desp : listaTipo) {
                    if (desp.getDes_val_cod().equals(contr.getTipoDesempleado())) {
                        contr.setDescTipoDesempleado(desp.getDes_nom());
                        break;
                    }
                }
                for (DesplegableVO desp : listaSexo) {
                    if (desp.getDes_val_cod().equals(contr.getSexo())) {
                        contr.setDescSexo(desp.getDes_nom());
                        break;
                    }
                }
            }
            return lista;
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando la lista de Contrataciones ", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando la lista de Contrataciones ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    /**
     *
     * @param numExp
     * @param id
     * @param adapt
     * @return
     * @throws Exception
     */
    public ContratacionVO getContratacionPorID(String numExp, String id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return m81DAO.getContratacionPorID(numExp, id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando una Contratación ", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando una Contratación ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    /**
     *
     * @param nuevoContratacion
     * @param adapt
     * @return
     * @throws Exception
     */
    public boolean crearContratacion(ContratacionVO nuevoContratacion, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return m81DAO.crearContratacion(nuevoContratacion, con);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion grabando un nuevo Contratacion:  ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }

    /**
     *
     * @param datModif
     * @param adapt
     * @return
     * @throws Exception
     */
    public boolean modificarContratacion(ContratacionVO datModif, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return m81DAO.modificarContratacion(datModif, con);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion modificando un Contratacion:  ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }

    /**
     *
     * @param numExp
     * @param id
     * @param adapt
     * @return
     * @throws Exception
     */
    public boolean eliminarContratacion(String numExp, int id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return m81DAO.eliminarContratacion(numExp, id, con);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion eliminando un Contratacion:  ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }

    /**
     *
     * @param nuevoTipo1
     * @param adapt
     * @return
     * @throws Exception
     */
    public boolean crearNuevoTipo1(Tipo1VO nuevoTipo1, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return m81DAO.crearNuevoTipo1(nuevoTipo1, con);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion grabando un nuevo Tipo1:  ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }

    /**
     *
     * @param datModif
     * @param adapt
     * @return
     * @throws Exception
     */
    public boolean modificarTipo1(Tipo1VO datModif, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return m81DAO.modificarTipo1(datModif, con);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion modificando Tipo1:  ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }

    /**
     *
     * @param numExp
     * @param id
     * @param adapt
     * @return
     * @throws Exception
     */
    public boolean eliminarTipo1(String numExp, int id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return m81DAO.eliminarTipo1(numExp, id, con);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion eliminando datos Tipo1:  ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }

    /**
     *
     * @param numExp
     * @param id
     * @param adapt
     * @return
     * @throws Exception
     */
    public Tipo1VO getTipo1PorID(String numExp, String id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return m81DAO.getTipo1PorID(numExp, id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre un tipo 1:  " + id, e);
            throw new Exception(e);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    /**
     *
     * @param numExp
     * @param adapt
     * @return
     * @throws Exception
     */
    public List<Tipo1VO> getListaTipo1(String numExp, AdaptadorSQLBD adapt) throws Exception {
        List<Tipo1VO> lista = new ArrayList<Tipo1VO>();
        Connection con = null;
        try {
            con = adapt.getConnection();
            lista = m81DAO.getListaTipo1(numExp, con);
            List<DesplegableVO> listaSexo = getValoresDesplegables(ConfigurationParameter.getParameter(ConstantesMeLanbide81.COD_SEXO, ConstantesMeLanbide81.FICHERO_PROPIEDADES), adapt);
            List<DesplegableVO> listaGrupoCotizacion = getValoresDesplegables(ConfigurationParameter.getParameter(ConstantesMeLanbide81.COD_GCON, ConstantesMeLanbide81.FICHERO_PROPIEDADES), adapt);
            List<DesplegableVO> listaInscrita = getValoresDesplegables(ConfigurationParameter.getParameter(ConstantesMeLanbide81.COD_DES_BOOL, ConstantesMeLanbide81.FICHERO_PROPIEDADES), adapt);
            for (Tipo1VO contrTipo1 : lista) {
                for (DesplegableVO desp : listaSexo) {
                    if (desp.getDes_val_cod().equals(contrTipo1.getSexo())) {
                        contrTipo1.setDescSexo(desp.getDes_nom());
                        break;
                    }
                }
                for (DesplegableVO desp : listaGrupoCotizacion) {
                    if (desp.getDes_val_cod().equals(contrTipo1.getGrupocot())) {
                        contrTipo1.setDescGrupocot(desp.getDes_nom());
                        break;
                    }
                }
                for (DesplegableVO desp : listaInscrita) {
                    if (desp.getDes_val_cod().equals(contrTipo1.getInscrita())) {
                        contrTipo1.setDescInscrita(desp.getDes_nom());
                        break;
                    }
                }
            }
            return lista;
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando la lista de Tipo1 ", e);
            throw new Exception(e);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    /**
     *
     * @param nuevoTipo2
     * @param adapt
     * @return
     * @throws Exception
     */
    public boolean crearNuevoTipo2(Tipo2VO nuevoTipo2, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return m81DAO.crearNuevoTipo2(nuevoTipo2, con);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion grabando un nuevo Tipo2:  ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }

    /**
     *
     * @param datModif
     * @param adapt
     * @return
     * @throws Exception
     */
    public boolean modificarTipo2(Tipo2VO datModif, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return m81DAO.modificarTipo2(datModif, con);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion modificando Tipo2:  ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }

   /**
     *
     * @param numExp
     * @param id
     * @param adapt
     * @return
     * @throws Exception
     */
    public boolean eliminarTipo2(String numExp, int id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return m81DAO.eliminarTipo2(numExp, id, con);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion eliminando datos Tipo2:  ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }

    /**
     *
     * @param numExp
     * @param id
     * @param adapt
     * @return
     * @throws Exception
     */
    public Tipo2VO getTipo2PorID(String numExp, String id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return m81DAO.getTipo2PorID(numExp, id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre Tipo2:  " + id, e);
            throw new Exception(e);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
    /**
     *
     * @param numExp
     * @param adapt
     * @return
     * @throws Exception
     */
    public List<Tipo2VO> getListaTipo2(String numExp, AdaptadorSQLBD adapt) throws Exception {
        List<Tipo2VO> lista = new ArrayList<Tipo2VO>();
        Connection con = null;
        try {
            con = adapt.getConnection();
            lista = m81DAO.getListaTipo2(numExp, con);
            List<DesplegableVO> listaSexo = getValoresDesplegables(ConfigurationParameter.getParameter(ConstantesMeLanbide81.COD_SEXO, ConstantesMeLanbide81.FICHERO_PROPIEDADES), adapt);
            List<DesplegableVO> listaGrupoCotizacion = getValoresDesplegables(ConfigurationParameter.getParameter(ConstantesMeLanbide81.COD_GCON, ConstantesMeLanbide81.FICHERO_PROPIEDADES), adapt);
            List<DesplegableVO> listaInscrita = getValoresDesplegables(ConfigurationParameter.getParameter(ConstantesMeLanbide81.COD_DES_BOOL, ConstantesMeLanbide81.FICHERO_PROPIEDADES), adapt);
            List<DesplegableVO> listaTipoContrato = getValoresDesplegables(ConfigurationParameter.getParameter(ConstantesMeLanbide81.COD_DES_TCLE, ConstantesMeLanbide81.FICHERO_PROPIEDADES), adapt);
            List<DesplegableVO> listaColectivo = getValoresDesplegables(ConfigurationParameter.getParameter(ConstantesMeLanbide81.COD_DES_CLEP, ConstantesMeLanbide81.FICHERO_PROPIEDADES), adapt);
            for (Tipo2VO contrTipo2 : lista) {
                for (DesplegableVO desp : listaSexo) {
                    if (desp.getDes_val_cod().equals(contrTipo2.getSexo())) {
                        contrTipo2.setDescSexo(desp.getDes_nom());
                        break;
                    }
                }
                for (DesplegableVO desp : listaGrupoCotizacion) {
                    if (desp.getDes_val_cod().equals(contrTipo2.getGrupocot())) {
                        contrTipo2.setDescGrupocot(desp.getDes_nom());
                        break;
                    }
                }
                for (DesplegableVO desp : listaInscrita) {
                    if (desp.getDes_val_cod().equals(contrTipo2.getInscrita())) {
                        contrTipo2.setDescInscrita(desp.getDes_nom());
                        break;
                    }
                }

                for (DesplegableVO desp : listaTipoContrato) {
                    if (desp.getDes_val_cod().equals(contrTipo2.getTipocontrato())) {
                        contrTipo2.setDescTipocontrato(desp.getDes_nom());
                        break;
            }
                }
                for (DesplegableVO desp : listaColectivo) {
                    if (desp.getDes_val_cod().equals(contrTipo2.getColectivo())) {
                        contrTipo2.setDescColectivo(desp.getDes_nom());
                        break;
                    }
                }

            }
            return lista;
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando la lista de Tipo2 ", e);
            throw new Exception(e);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

}
