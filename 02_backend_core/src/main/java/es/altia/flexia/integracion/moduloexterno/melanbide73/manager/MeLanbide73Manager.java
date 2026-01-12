/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide73.manager;

import es.altia.flexia.integracion.moduloexterno.melanbide73.dao.MeLanbide73DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide73.vo.GrupoUnidadesOrganicas;
import es.altia.flexia.integracion.moduloexterno.melanbide73.vo.ResultadoOperacion;
import es.altia.flexia.integracion.moduloexterno.melanbide73.vo.UnidadOrganica;
import es.altia.flexia.integracion.moduloexterno.melanbide73.vo.UnidadTramitadoraVO;
import es.altia.flexia.integracion.moduloexterno.melanbide73.vo.Usuario;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import es.altia.flexia.registro.digitalizacion.lanbide.vo.GeneralComboVO;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Kepa
 */
public class MeLanbide73Manager {
    //Logger

    private static Logger log = LogManager.getLogger(MeLanbide73Manager.class);

    //Instancia
    private static MeLanbide73Manager instance = null;

    /**
     * Devuelve una instancia de MeLanbide73Manager, si no existe la crea.
     *
     * @return
     */
    public static MeLanbide73Manager getInstance() {
        if (log.isDebugEnabled()) {
            log.debug("getInstance() : BEGIN");
        }
        if (instance == null) {
            synchronized (MeLanbide73Manager.class) {
                if (instance == null) {
                    instance = new MeLanbide73Manager();
                }//if(instance == null)
            }//synchronized(MeLanbide03Manager.class)
        }//if(instance == null)
        if (log.isDebugEnabled()) {
            log.debug("getInstance() : END");
        }
        return instance;
    }//getInstance

    /**
     * Devuelve la lista de unidades tramitadoras de un expediente
     *
     * @param numExp
     * @param con
     * @return ArrayList<UnidadTramitadoraVO>
     *
     */
    public ArrayList<UnidadTramitadoraVO> cargarUnidadesExpediente(String numExp, AdaptadorSQLBD adapt) throws Exception {
        if (log.isInfoEnabled()) {
            log.debug("ENTRA EN cargarUnidades (expediente = " + numExp + ")");
        }
        Connection con = null;
        ArrayList<UnidadTramitadoraVO> listaReturn = new ArrayList<UnidadTramitadoraVO>();
        try {
            con = adapt.getConnection();
            MeLanbide73DAO meLanbide73Dao = MeLanbide73DAO.getInstance();
            listaReturn = meLanbide73Dao.cargaUnidadesExpediente(numExp, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepciÃ³n en la BBDD recuperando las unidades del expediente", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepciÃ³n recuperando las unidades tramitadoras del expediente", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
                log.debug(">>> Devolvemos la conexion.");
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        if (log.isInfoEnabled()) {
            log.debug("cargarUnidades() : FIN");
        }
        return listaReturn;
    }// cargarUnidadesExpediente

    /**
     * Devuelve la lista de unidades tramitadoras de expedientes RGI
     *
     * @param con
     *
     * @return ArrayList<UnidadTramitadoraVO> cargarListaUnidades
     *
     */
    public ArrayList<UnidadTramitadoraVO> cargarListaUnidades(AdaptadorSQLBD adapt) throws Exception {
        if (log.isInfoEnabled()) {
            log.debug("ENTRA EN cargarListaUnidades ");
        }
        Connection con = null;
        ArrayList<UnidadTramitadoraVO> listaReturn = new ArrayList<UnidadTramitadoraVO>();
        try {
            con = adapt.getConnection();
            MeLanbide73DAO meLanbide73Dao = MeLanbide73DAO.getInstance();
            listaReturn = meLanbide73Dao.cargarListaUnidades(con);
        } catch (BDException e) {
            log.error("Se ha producido una excepciÃ³n en la BBDD recuperando las unidades del expediente", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepciÃ³n recuperando las unidades tramitadoras del expediente", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
                log.debug(">>> Devolvemos la conexion.");
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        if (log.isInfoEnabled()) {

            log.debug("cargarListaUnidades() : FIN");
        }
        return listaReturn;
    }// cargarListaUnidades

    /**
     * Devuelve la lista de unidades tramitadoras de un usuario
     *
     * @param codUsuario
     * @param con
     * @return ArrayList<UnidadTramitadoraVO>
     *
     */
    public ArrayList<UnidadTramitadoraVO> cargarUnidadesUsuario(String codUsuario, AdaptadorSQLBD adapt) throws Exception {
        if (log.isInfoEnabled()) {
            log.debug("ENTRA EN cargarUnidades (Usuario = " + codUsuario + ")");
        }
        Connection con = null;
        ArrayList<UnidadTramitadoraVO> listaReturn = new ArrayList<UnidadTramitadoraVO>();
        try {
            con = adapt.getConnection();
            MeLanbide73DAO meLanbide73Dao = MeLanbide73DAO.getInstance();
            listaReturn = meLanbide73Dao.cargaUnidadesUsuario(codUsuario, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepciÃ³n en la BBDD recuperando las unidades del usuario", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepciÃ³n recuperando las unidades tramitadoras del usuario", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
                log.debug(">>> Devolvemos la conexion.");
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        if (log.isInfoEnabled()) {
            log.debug("cargarUnidadesUsuario() : FIN");
        }
        return listaReturn;
    }

    public boolean cambiarUnidad(UnidadTramitadoraVO nueva, String numExp, AdaptadorSQLBD adapt) throws Exception {
        log.debug("===========  ENTRO en MeLanbide73Manager CambiarUnidad ");

        log.debug("Expediente " + numExp);
        Connection con = null;
        boolean insertOK = false;
        try {
            con = adapt.getConnection();
            adapt.inicioTransaccion(con);
            MeLanbide73DAO meLanbibe73DAO = MeLanbide73DAO.getInstance();
            insertOK = meLanbibe73DAO.cambiarUnidad(nueva, numExp, con);
            adapt.finTransaccion(con);
        } catch (BDException e) {
            log.error("Se ha producido una excepciÃ³n en la BBDD Actualizando una Unidad : " + e.getMessage(), e);
            adapt.rollBack(con);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepciÃ³n  Actualizando una Unidad : " + ex.getMessage(), ex);
            adapt.rollBack(con);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
                log.debug(">>> Devolvemos la conexion.");
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }

        return insertOK;

    }// cambiarUnidad

    public boolean insertarOperacionExpediente(int codMunicipio, String ejercicio, String numExpediente, String fechaOperacion, String codUsuario, String descripcion, Calendar fechaAct, AdaptadorSQLBD adapt) throws Exception {
        log.debug("=========== Entro en insertarOperacion");
        boolean insertOK = false;
        Connection con = null;
        try {
            con = adapt.getConnection();
            adapt.inicioTransaccion(con);
            MeLanbide73DAO meLanbibe73DAO = MeLanbide73DAO.getInstance();
            insertOK = meLanbibe73DAO.insertarHistorico(codMunicipio, ejercicio, numExpediente, fechaOperacion, codUsuario, descripcion, fechaAct, con);
            adapt.finTransaccion(con);
        } catch (BDException e) {
            log.error("Se ha producido una excepciÃ³n en la BBDD Insertando OPERACION_EXPEDIENTE : " + e.getMessage(), e);
            adapt.rollBack(con);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Error Insertando OPERACION_EXPEDIENTE : " + ex.getMessage(), ex);
            adapt.rollBack(con);
        } finally {
            try {
                adapt.devolverConexion(con);
                log.debug(">>> Devolvemos la conexion.");
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }

        return insertOK;
    }// insertar Operacion Expediente

    /**
     * Devuelve la lista de usuarios
     *
     * @return ArrayList<GeneralComboVO>
     *
     */
    public List<GeneralComboVO> cargarListaUsuarios(AdaptadorSQLBD adapt) throws Exception {
        if (log.isInfoEnabled()) {
            log.debug("ENTRA EN cargarUsuarios");
        }
        Connection con = null;
        List<GeneralComboVO> listaReturn = new ArrayList<GeneralComboVO>();
        try {
            con = adapt.getConnection();
            MeLanbide73DAO meLanbide73Dao = MeLanbide73DAO.getInstance();
            listaReturn = meLanbide73Dao.cargarListaUsuarios(con);
        } catch (BDException e) {
            log.error("Se ha producido una excepciÃ³n en la BBDD recuperando los usuarios", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepciÃ³n recuperando los usuarios", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
                log.debug(">>> Devolvemos la conexion.");
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        if (log.isInfoEnabled()) {
            log.debug("cargarUsuarios() : FIN");
        }
        return listaReturn;
    }

    /**
     * Devuelve la lista de unidades orgánicas
     *
     * @return ArrayList<GeneralComboVO>
     *
     */
    public List<GeneralComboVO> cargarListaUORs(AdaptadorSQLBD adapt) throws Exception {
        if (log.isInfoEnabled()) {
            log.debug("ENTRA EN cargarUORs");
        }
        Connection con = null;
        List<GeneralComboVO> listaReturn = new ArrayList<GeneralComboVO>();
        try {
            con = adapt.getConnection();
            MeLanbide73DAO meLanbide73Dao = MeLanbide73DAO.getInstance();
            listaReturn = meLanbide73Dao.cargarListaUORS(con);
        } catch (BDException e) {
            log.error("Se ha producido una excepciÃ³n en la BBDD recuperando las UOR", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepciÃ³n recuperando las UOR", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
                log.debug(">>> Devolvemos la conexion.");
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        if (log.isInfoEnabled()) {
            log.debug("cargarUORs() : FIN");
        }
        return listaReturn;
    }

    /**
     * Devuelve la lista de grupos de unidades orgánicas
     *
     * @return ArrayList<GeneralComboVO>
     *
     */
    public List<GeneralComboVO> cargarGruposUORS(AdaptadorSQLBD adapt, int codOrganizacion) throws Exception {
        if (log.isInfoEnabled()) {
            log.debug("ENTRA EN cargarGruposUORs");
        }
        Connection con = null;
        List<GeneralComboVO> listaReturn = new ArrayList<GeneralComboVO>();
        try {
            con = adapt.getConnection();
            MeLanbide73DAO meLanbide73Dao = MeLanbide73DAO.getInstance();
            listaReturn = meLanbide73Dao.cargarGruposUORS(con, codOrganizacion);
        } catch (BDException e) {
            log.error("Se ha producido una excepciÃ³n en la BBDD recuperando los grupos UOR", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepciÃ³n recuperando los grupos UOR", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
                log.debug(">>> Devolvemos la conexion.");
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        if (log.isInfoEnabled()) {
            log.debug("cargarGruposUORs() : FIN");
        }
        return listaReturn;
    }

    /**
     * Devuelve la lista de unidades orgánicas de un determinado grupo
     *
     * @return ArrayList<GeneralComboVO>
     *
     */
    /*public List<GeneralComboVO> cargarUORSGrupo(AdaptadorSQLBD adapt, long grpUorCod,
            int codOrganizacion) throws Exception {
        if (log.isInfoEnabled()) {
            log.debug("ENTRA EN cargarUORsGrupo");
        }
        Connection con = null;
        List<GeneralComboVO> listaReturn = new ArrayList<GeneralComboVO>();
        try {
            con = adapt.getConnection();
            MeLanbide73DAO meLanbide73Dao = MeLanbide73DAO.getInstance();
            listaReturn = meLanbide73Dao.cargarUORSGrupo(con, grpUorCod, codOrganizacion);
        } catch (BDException e) {
            log.error("Se ha producido una excepciÃ³n en la BBDD recuperando las UOR de grupo", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepciÃ³n recuperando las UOR de grupo", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
                log.debug(">>> Devolvemos la conexion.");
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        if (log.isInfoEnabled()) {
            log.debug("cargarUORsGrupo() : FIN");
        }
        return listaReturn;
    }*/
    /**
     * Crea un nuevo grupo de unidades orgánicas
     *
     *
     */
    public int insertarGrupoUOR(AdaptadorSQLBD adapt, final String nombreGrupo,
            int codOrganizacion) throws Exception {
        int result = -1;
        if (log.isInfoEnabled()) {
            log.debug("ENTRA EN insertarGrupoUOR");
        }
        Connection con = null;
        try {
            con = adapt.getConnection();
            adapt.inicioTransaccion(con);

            MeLanbide73DAO meLanbide73Dao = MeLanbide73DAO.getInstance();
            if (!meLanbide73Dao.existeGrupo(con, nombreGrupo, codOrganizacion)) {
                result = meLanbide73Dao.insertarGrupoUOR(con, nombreGrupo, codOrganizacion);
            }
            adapt.finTransaccion(con);
        } catch (BDException e) {
            log.error("Se ha producido una excepciÃ³n en la BBDD insertando un grupo de UOR", e);
            adapt.rollBack(con);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepciÃ³n insertando un grupo de UOR", ex);
            adapt.rollBack(con);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
                log.debug(">>> Devolvemos la conexion.");
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        if (log.isInfoEnabled()) {
            log.debug("insertarGrupoUOR() : FIN");
        }
        return result;
    }

    /**
     * Borra un grupo de unidades orgánicas
     *
     *
     */
    public boolean borrarGrupoUOR(AdaptadorSQLBD adaptGenerico, AdaptadorSQLBD adapt,
            int id, int codOrganizacion) throws Exception {
        boolean result = false;
        if (log.isInfoEnabled()) {
            log.debug("ENTRA EN borrarGrupoUOR");
        }
        Connection con = null;
        Connection conGenerica = null;
        try {
            con = adapt.getConnection();
            conGenerica = adaptGenerico.getConnection();
            adapt.inicioTransaccion(con);
            MeLanbide73DAO meLanbide73Dao = MeLanbide73DAO.getInstance();
            final List<Usuario> usuarios = meLanbide73Dao.getUsuariosGrupo(conGenerica, id, codOrganizacion);
            if (usuarios.isEmpty()) {
                final List<UnidadOrganica> uors = meLanbide73Dao.getUnidadesOrganicasGrupo(con, id, codOrganizacion);
                for (final UnidadOrganica uor : uors) {
                    meLanbide73Dao.borrarUORGrupoUOR(conGenerica, id, uor.getUorCod(), codOrganizacion);
                }
                result = meLanbide73Dao.borrarGrupoUOR(conGenerica, id, codOrganizacion);
            }
            adapt.finTransaccion(con);
        } catch (BDException e) {
            log.error("Se ha producido una excepciÃ³n en la BBDD eliminando un grupo de UOR", e);
            adapt.rollBack(con);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepciÃ³n eliminando un grupo de UOR", ex);
            adapt.rollBack(con);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
                adaptGenerico.devolverConexion(conGenerica);
                log.debug(">>> Devolvemos la conexion.");
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        if (log.isInfoEnabled()) {
            log.debug("borrarGrupoUOR() : FIN");
        }
        return result;
    }

    /**
     * Modifica el nombre de un grupo de unidades orgánicas
     *
     *
     */
    public int modificarGrupoUOR(AdaptadorSQLBD adapt, int id, final String nombreGrupo,
            int codOrganizacion) throws Exception {
        int result = -1;
        if (log.isInfoEnabled()) {
            log.debug("ENTRA EN modificarGrupoUOR");
        }
        Connection con = null;
        try {
            con = adapt.getConnection();
            adapt.inicioTransaccion(con);
            MeLanbide73DAO meLanbide73Dao = MeLanbide73DAO.getInstance();
            if (!meLanbide73Dao.existeGrupo(con, nombreGrupo, codOrganizacion)) {
                result = meLanbide73Dao.modificarGrupoUOR(con, id, codOrganizacion, nombreGrupo);
            }
            adapt.finTransaccion(con);
        } catch (BDException e) {
            log.error("Se ha producido una excepciÃ³n en la BBDD modificando un grupo de UOR", e);
            adapt.rollBack(con);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepciÃ³n modificando un grupo de UOR", ex);
            adapt.rollBack(con);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
                log.debug(">>> Devolvemos la conexion.");
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        if (log.isInfoEnabled()) {
            log.debug("modificarGrupoUOR() : FIN");
        }
        return result;
    }

    /**
     * Inserta una unidad orgánica en un grupo de ellas
     *
     *
     */
    public boolean insertarUOREnGrupoUOR(AdaptadorSQLBD adaptGenerico, AdaptadorSQLBD adapt,
            int idGrupo, int idUOR,
            int codOrganizacion) throws Exception {
        boolean result = false;
        if (log.isInfoEnabled()) {
            log.debug("ENTRA EN insertarUOREnGrupoUOR");
        }
        Connection con = null;
        Connection conGenerico = null;
        try {
            con = adapt.getConnection();
            conGenerico = adaptGenerico.getConnection();

            MeLanbide73DAO meLanbide73Dao = MeLanbide73DAO.getInstance();
            adaptGenerico.inicioTransaccion(conGenerico);
            if (meLanbide73Dao.existeUO(con, idUOR)) {
                result = meLanbide73Dao.insertarUorGrpuor(conGenerico, idGrupo, idUOR, codOrganizacion);
            }

            // Si la unidad orgánica en cuestión está en al menos otro grupo
            if (!getUsuarios(adapt, idGrupo, codOrganizacion).isEmpty()) {
                final List<GrupoUnidadesOrganicas> grpUors = getGruposUORsAfectadosAlAnadirEliminarUOR(adaptGenerico,
                        idGrupo, idUOR, codOrganizacion);
                if (!grpUors.isEmpty()) {
                    result = borrarUouUorGrpuor(adaptGenerico, grpUors, idUOR, codOrganizacion);
                }
            }

            if (result) { // Se insertan los usuarios del grupo
                final List<Usuario> usuarios = meLanbide73Dao.getUsuariosGrupo(conGenerico, idGrupo, codOrganizacion);
                for (final Usuario usuario : usuarios) {
                    meLanbide73Dao.insertarUsuarioEnUOU(conGenerico, idUOR, usuario.getUsuCod(), codOrganizacion);
                }
            }
            adaptGenerico.finTransaccion(conGenerico);
        } catch (BDException e) {
            log.error("Se ha producido una excepciÃ³n en la BBDD insertando una UOR en un grupo", e);
            adaptGenerico.rollBack(conGenerico);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepciÃ³n insertando una UOR en un grupo", ex);
            adaptGenerico.rollBack(conGenerico);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
                adaptGenerico.devolverConexion(conGenerico);
                log.debug(">>> Devolvemos la conexion.");
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        if (log.isInfoEnabled()) {
            log.debug("insertarUOREnGrupoUOR() : FIN");
        }
        return result;
    }

    /**
     * Elimina una unidad orgánica de un grupo de unidades orgánicas
     *
     *
     */
    public boolean eliminarUORDeGrupoUOR(AdaptadorSQLBD adaptGenerico,
            AdaptadorSQLBD adapt, int idGrupo, int idUOR, int codOrganizacion) throws Exception {
        boolean result = false;
        if (log.isInfoEnabled()) {
            log.debug("ENTRA EN eliminarUORDeGrupoUOR");
        }
        Connection con = null;
        Connection conGenerica = null;
        try {
            con = adapt.getConnection();
            conGenerica = adaptGenerico.getConnection();
            MeLanbide73DAO meLanbide73Dao = MeLanbide73DAO.getInstance();
            adaptGenerico.inicioTransaccion(conGenerica);
            if (meLanbide73Dao.existeUO(con, idUOR)) {
                result = meLanbide73Dao.eliminarUorGrpuor(conGenerica, idGrupo, idUOR, codOrganizacion);
            }

            // Si la unidad orgánica en cuestión está en al menos otro grupo
            if (!getUsuarios(adapt, idGrupo, codOrganizacion).isEmpty()) {
                final List<GrupoUnidadesOrganicas> grpUors = getGruposUORsAfectadosAlAnadirEliminarUOR(adaptGenerico,
                        idGrupo, idUOR, codOrganizacion);
                if (!grpUors.isEmpty()) {
                    result = borrarUouUorGrpuor(adaptGenerico, grpUors, idUOR, codOrganizacion);
                }
            }

            if (result) { // Se eliminan los usuarios de esa UOR que formen parte del grupo
                final List<Usuario> usuarios = meLanbide73Dao.getUsuariosGrupo(conGenerica, idGrupo, codOrganizacion);
                for (final Usuario usuario : usuarios) {
                    meLanbide73Dao.eliminarUsuarioEnUOU(conGenerica, idUOR, usuario.getUsuCod(), codOrganizacion);
                }
            }
            adaptGenerico.finTransaccion(conGenerica);
        } catch (BDException e) {
            log.error("Se ha producido una excepciÃ³n en la BBDD eliminando una UOR en un grupo", e);
            adaptGenerico.rollBack(conGenerica);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepciÃ³n eliminando una UOR en un grupo", ex);
            adaptGenerico.rollBack(conGenerica);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
                adaptGenerico.devolverConexion(conGenerica);
                log.debug(">>> Devolvemos la conexion.");
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        if (log.isInfoEnabled()) {
            log.debug("eliminarUORDeGrupoUOR() : FIN");
        }
        return result;
    }

    /**
     * Inserta un usuario en un grupo de unidades orgánicas
     *
     *
     */
    public ResultadoOperacion insertarUsuarioEnGrupoUOR(AdaptadorSQLBD adapt, AdaptadorSQLBD adaptGenerico,
            int idGrupo, int idUsuario, int codOrganizacion) throws Exception {
        ResultadoOperacion result = ResultadoOperacion.ERROR;
        boolean uoInvalida = false;
        if (log.isInfoEnabled()) {
            log.debug("ENTRA EN insertarUsuarioEnGrupoUOR");
        }
        Connection con = null;
        Connection conGenerica = null;
        try {
            con = adapt.getConnection();
            final MeLanbide73DAO meLanbide73Dao = MeLanbide73DAO.getInstance();
            final List<UnidadOrganica> listaUORS = meLanbide73Dao.getUnidadesOrganicasGrupo(con, idGrupo, codOrganizacion);
            // Se comprueba que las unidades orgánicas estén activas.
            for (final UnidadOrganica uo : listaUORS) {
                if (!uo.isActive()) {
                    uoInvalida = true;
                    result = ResultadoOperacion.UOR_INACTIVA;
                    break;
                }
            }

            conGenerica = adaptGenerico.getConnection();
            adaptGenerico.inicioTransaccion(conGenerica);
            if (!uoInvalida) {
                result = meLanbide73Dao.insertarUsuarioGrpuor(conGenerica, idGrupo, idUsuario, codOrganizacion)
                        ? ResultadoOperacion.CORRECT : ResultadoOperacion.REGISTRO_EXISTENTE;

                // Si al menos alguna de las unidades orgánicas está en otro grupo
                if (!getUnidadesOrganicas(adapt, idGrupo, codOrganizacion).isEmpty()) {
                    final List<GrupoUnidadesOrganicas> grpUors = getGruposUORsAfectados(adapt,
                            idGrupo, codOrganizacion);
                    if (!grpUors.isEmpty()) {
                        final List<UnidadOrganica> uors = getUORsAfectadas(adapt, idGrupo, codOrganizacion);
                        for (final UnidadOrganica uor : uors) {
                            result = borrarUouUorGrpuor(adaptGenerico, grpUors, uor.getUorCod(), codOrganizacion) ?
                                    ResultadoOperacion.CORRECT : ResultadoOperacion.ERROR;
                        }
                    }
                }
            }
            if (result.equals(ResultadoOperacion.CORRECT)) { // Se inserta el usuario en todas las uor de ese grupo
                for (final UnidadOrganica uo : listaUORS) {
                    meLanbide73Dao.insertarUsuarioEnUOU(conGenerica, uo.getUorCod(), idUsuario, codOrganizacion);
                }
            }
            adaptGenerico.finTransaccion(conGenerica);
        } catch (BDException e) {
            log.error("Se ha producido una excepciÃ³n en la BBDD insertando una UOR en un grupo", e);
            adaptGenerico.rollBack(conGenerica);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepciÃ³n insertando una UOR en un grupo", ex);
            adaptGenerico.rollBack(conGenerica);
            throw new Exception(ex);
        } finally {
            try {
                adaptGenerico.devolverConexion(conGenerica);
                adapt.devolverConexion(con);
                log.debug(">>> Devolvemos la conexion.");
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        if (log.isInfoEnabled()) {
            log.debug("insertarUsuarioEnGrupoUOR() : FIN");
        }
        return result;
    }

    /**
     *
     *
     * @return ArrayList<GeneralComboVO>
     *
     */
    public ResultadoOperacion eliminarUsuarioDeGrupoUOR(AdaptadorSQLBD adapt, AdaptadorSQLBD adaptGenerico,
            int idGrupo, int idUsuario, int codOrganizacion) throws Exception {
        ResultadoOperacion result = ResultadoOperacion.ERROR;
        if (log.isInfoEnabled()) {
            log.debug("ENTRA EN eliminarUsuarioDeGrupoUOR");
        }
        Connection con = null;
        Connection conGenerica = null;
        try {
            con = adapt.getConnection();
            MeLanbide73DAO meLanbide73Dao = MeLanbide73DAO.getInstance();
            final List<UnidadOrganica> listaUORS = meLanbide73Dao.getUnidadesOrganicasGrupo(con, idGrupo, codOrganizacion);

            conGenerica = adaptGenerico.getConnection();
            result = ResultadoOperacion.REGISTRO_INEXISTENTE;
            adaptGenerico.inicioTransaccion(conGenerica);

            result = meLanbide73Dao.eliminarUsuarioGrpuor(conGenerica, idGrupo, idUsuario, codOrganizacion)
                    ? ResultadoOperacion.CORRECT : ResultadoOperacion.REGISTRO_INEXISTENTE;

            if (result.equals(ResultadoOperacion.CORRECT)) {// Se elimina el usuario de las UOR del grupo

                // Si al menos alguna de las unidades orgánicas está en otro grupo
                if (!getUnidadesOrganicas(adapt, idGrupo, codOrganizacion).isEmpty()) {
                    final List<GrupoUnidadesOrganicas> grpUors = getGruposUORsAfectados(adapt,
                            idGrupo, codOrganizacion);
                    if (!grpUors.isEmpty()) {
                        final List<UnidadOrganica> uors = getUORsAfectadas(adapt, idGrupo, codOrganizacion);
                        for (final UnidadOrganica uor : uors) {
                            result = borrarUouUorGrpuor(adaptGenerico, grpUors, uor.getUorCod(), codOrganizacion) ?
                                    ResultadoOperacion.CORRECT : ResultadoOperacion.ERROR;
                        }
                    }
                }

                for (final UnidadOrganica uo : listaUORS) {
                    if (meLanbide73Dao.existeUO(con, uo.getUorCod())) {
                        meLanbide73Dao.eliminarUsuarioEnUOU(conGenerica, uo.getUorCod(), idUsuario, codOrganizacion);
                    }
                }
            }
            adaptGenerico.finTransaccion(conGenerica);
        } catch (BDException e) {
            log.error("Se ha producido una excepciÃ³n en la BBDD eliminando un usuario en un grupo", e);
            adaptGenerico.rollBack(conGenerica);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepciÃ³n eliminando un usuario en un grupo", ex);
            adaptGenerico.rollBack(conGenerica);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
                adaptGenerico.devolverConexion(conGenerica);
                log.debug(">>> Devolvemos la conexion.");
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        if (log.isInfoEnabled()) {
            log.debug("eliminarUsuarioDeGrupoUOR() : FIN");
        }
        return result;
    }

    /**
     *
     *
     * @return ArrayList<GeneralComboVO>
     *
     */
    public List<UnidadOrganica> getUnidadesOrganicas(AdaptadorSQLBD adapt, int idGrupoUOR,
            int codOrganizacion) throws Exception {
        List<UnidadOrganica> unidadesOrganicas = new ArrayList<UnidadOrganica>();
        if (log.isInfoEnabled()) {
            log.debug("ENTRA EN getUnidadesOrganicas");
        }
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide73DAO meLanbide73Dao = MeLanbide73DAO.getInstance();
            unidadesOrganicas = meLanbide73Dao.getUnidadesOrganicasGrupo(con, idGrupoUOR, codOrganizacion);
//            log.debug("Unidades orgánicas = " + unidadesOrganicas);
        } catch (BDException e) {
            log.error("Se ha producido una excepciÃ³n en la BBDD consultando un grupo de UOR", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepciÃ³n consultando un grupo de UOR", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
                log.debug(">>> Devolvemos la conexion.");
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        if (log.isInfoEnabled()) {
            log.debug("getUnidadesOrganicas() : FIN");
        }
        return unidadesOrganicas;
    }

    /**
     * Devuelve la lista de usuarios de un determinado grupo de unidades
     * orgánicas
     *
     * @return ArrayList<GeneralComboVO>
     *
     */
    public List<Usuario> getUsuarios(AdaptadorSQLBD adapt, int idGrupoUOR,
            int codOrganizacion) throws Exception {
        List<Usuario> usuarios = new ArrayList<Usuario>();
        if (log.isInfoEnabled()) {
            log.debug("ENTRA EN getUsuarios");
        }
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide73DAO meLanbide73Dao = MeLanbide73DAO.getInstance();
            usuarios = meLanbide73Dao.getUsuariosGrupo(con, idGrupoUOR, codOrganizacion);
//            log.debug("Usuarios = " + usuarios);
        } catch (BDException e) {
            log.error("Se ha producido una excepciÃ³n en la BBDD consultando un grupo de UOR", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepciÃ³n consultando un grupo de UOR", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
                log.debug(">>> Devolvemos la conexion.");
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        if (log.isInfoEnabled()) {
            log.debug("getUsuarios() : FIN");
        }
        return usuarios;
    }

    /**
     * Devuelve la lista de unidades orgánicas afectadas por la operación actual
     *
     * @return ArrayList<GeneralComboVO>
     *
     */
    public List<UnidadOrganica> getUORsAfectadas(AdaptadorSQLBD adapt, int idGrupoUOR,
            int codOrganizacion) throws Exception {
        List<UnidadOrganica> unidadesOrganicas = new ArrayList<UnidadOrganica>();
        if (log.isInfoEnabled()) {
            log.debug("ENTRA EN getUORsAfectadas");
        }
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide73DAO meLanbide73Dao = MeLanbide73DAO.getInstance();
            unidadesOrganicas = meLanbide73Dao.getUORsAfectadas(con,
                    meLanbide73Dao.getUnidadesOrganicasGrupo(con, idGrupoUOR, codOrganizacion),
                    idGrupoUOR, codOrganizacion);
        } catch (BDException e) {
            log.error("Se ha producido una excepciÃ³n en la BBDD consultando un grupo de UOR", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepciÃ³n consultando un grupo de UOR", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
                log.debug(">>> Devolvemos la conexion.");
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        if (log.isInfoEnabled()) {
            log.debug("getUORsAfectadas() : FIN");
        }
        return unidadesOrganicas;
    }

    /**
     * Devuelve la lista de grupos de uors afectados por la operación actual
     *
     * @return ArrayList<GeneralComboVO>
     *
     */
    public List<GrupoUnidadesOrganicas> getGruposUORsAfectados(AdaptadorSQLBD adapt, int idGrupoUOR,
            int codOrganizacion) throws Exception {
        List<GrupoUnidadesOrganicas> grupos = new ArrayList<GrupoUnidadesOrganicas>();
        if (log.isInfoEnabled()) {
            log.debug("ENTRA EN getGruposUORsAfectados");
        }
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide73DAO meLanbide73Dao = MeLanbide73DAO.getInstance();
            grupos = meLanbide73Dao.getGruposUORsAfectados(con,
                    meLanbide73Dao.getUnidadesOrganicasGrupo(con, idGrupoUOR, codOrganizacion),
                    idGrupoUOR, codOrganizacion);
        } catch (BDException e) {
            log.error("Se ha producido una excepciÃ³n en la BBDD consultando un grupo de UOR", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepciÃ³n consultando un grupo de UOR", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
                log.debug(">>> Devolvemos la conexion.");
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        if (log.isInfoEnabled()) {
            log.debug("getGruposUORsAfectados() : FIN");
        }
        return grupos;
    }

    /**
     * Devuelve la lista de grupos de uors afectados por la operación actual
     *
     * @return ArrayList<GeneralComboVO>
     *
     */
    public List<GrupoUnidadesOrganicas> getGruposUORsAfectadosAlAnadirEliminarUOR(AdaptadorSQLBD adapt,
            int idGrupoUOR, int idUOR, int codOrganizacion) throws Exception {
        List<GrupoUnidadesOrganicas> grupos = new ArrayList<GrupoUnidadesOrganicas>();
        if (log.isInfoEnabled()) {
            log.debug("ENTRA EN getGruposUORsAfectados");
        }
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide73DAO meLanbide73Dao = MeLanbide73DAO.getInstance();
            grupos = meLanbide73Dao.getGruposUORsAfectados(con,
                    Arrays.asList(new UnidadOrganica(idUOR, 0, "", "", "")),
                    idGrupoUOR, codOrganizacion);
        } catch (BDException e) {
            log.error("Se ha producido una excepciÃ³n en la BBDD consultando un grupo de UOR", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepciÃ³n consultando un grupo de UOR", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
                log.debug(">>> Devolvemos la conexion.");
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        if (log.isInfoEnabled()) {
            log.debug("getGruposUORsAfectados() : FIN");
        }
        return grupos;
    }

    /**
     * Devuelve verdadero si la uor está en otro grupo al menos
     *
     * @return result
     *
     */
    public boolean existeUOREnOtroGrupo(AdaptadorSQLBD adapt, int idGrupoUOR,
            int idUOR, int codOrganizacion) throws Exception {
        boolean result = false;
        if (log.isInfoEnabled()) {
            log.debug("ENTRA EN existeUOREnOtroGrupo");
        }
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide73DAO meLanbide73Dao = MeLanbide73DAO.getInstance();
            result = meLanbide73Dao.existeUOREnOtroGrupo(con, idGrupoUOR, idUOR, codOrganizacion);
        } catch (BDException e) {
            log.error("Se ha producido una excepciÃ³n en la BBDD consultando un grupo de UOR", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepciÃ³n consultando un grupo de UOR", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
                log.debug(">>> Devolvemos la conexion.");
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        if (log.isInfoEnabled()) {
            log.debug("existeUOREnOtroGrupo() : FIN");
        }
        return result;
    }

    /**
     * Borra los usuarios de la unidad orgánica idUOR
     * que estén la lista grpUors.
     * 
     * @return result
     *
     */
    public boolean borrarUouUorGrpuor(AdaptadorSQLBD adapt,
            final List<GrupoUnidadesOrganicas> grpUors,
            int idUOR, int codOrganizacion) throws Exception {
        boolean result = false;
        if (log.isInfoEnabled()) {
            log.debug("ENTRA EN borrarUouUorGrpuor");
        }
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide73DAO meLanbide73Dao = MeLanbide73DAO.getInstance();
            for (final GrupoUnidadesOrganicas grp : grpUors) {
                final List<Usuario> usuarios = meLanbide73Dao.getUsuariosGrupo(con, grp.getGrpCod(), codOrganizacion);
                for (final Usuario usuario : usuarios) {
                    meLanbide73Dao.eliminarUsuarioEnUOU(con, idUOR, usuario.getUsuCod(), codOrganizacion);
                }
                meLanbide73Dao.eliminarUorGrpuor(con, grp.getGrpCod(), idUOR, codOrganizacion);
            }
            result = true;
        } catch (BDException e) {
            log.error("Se ha producido una excepciÃ³n en la BBDD consultando un grupo de UOR", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepciÃ³n consultando un grupo de UOR", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
                log.debug(">>> Devolvemos la conexion.");
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        if (log.isInfoEnabled()) {
            log.debug("borrarUouUorGrpuor() : FIN");
        }
        return result;
    }
}//class
