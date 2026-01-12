/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide41.manager;

import es.altia.flexia.integracion.moduloexterno.melanbide41.dao.MeLanbide41DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide41.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide41.util.ConstantesMeLanbide41;
import es.altia.flexia.integracion.moduloexterno.melanbide41.util.MeLanbide41Exception;
import es.altia.flexia.integracion.moduloexterno.melanbide41.vo.capacidad.CapacidadVO;
import es.altia.flexia.integracion.moduloexterno.melanbide41.vo.disponibilidad.DisponibilidadVO;
import es.altia.flexia.integracion.moduloexterno.melanbide41.vo.dotacion.DotacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide41.vo.especialidades.CerCertificadoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide41.vo.especialidades.EspecialidadesVO;
import es.altia.flexia.integracion.moduloexterno.melanbide41.vo.identificacionesp.IdentificacionEspVO;
import es.altia.flexia.integracion.moduloexterno.melanbide41.vo.material.MaterialVO;
import es.altia.flexia.integracion.moduloexterno.melanbide41.vo.servicios.ServiciosVO;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExternoCampoSupFactoria;
import es.altia.flexia.integracion.moduloexterno.plugin.camposuplementario.IModuloIntegracionExternoCamposFlexia;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.CampoSuplementarioModuloIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.SalidaIntegracionVO;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.ValorCampoDesplegableModuloIntegracionVO;

/**
 *
 * @author davidg
 */
public class MeLanbide41Manager {

    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide41Manager.class);

    //Instancia
    private static MeLanbide41Manager instance = null;

    public static MeLanbide41Manager getInstance() {
        if (instance == null) {
            synchronized (MeLanbide41Manager.class) {
                instance = new MeLanbide41Manager();
            }
        }
        return instance;
    }

    public List<EspecialidadesVO> getDatosEspecialidades(String numExp, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            if (adaptador != null) {
                con = adaptador.getConnection();
            }
            return getDatosEspecialidades(numExp, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos sobre las Especialides Solicitadas para expediente " + numExp, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Especialidades solicitadas para expediente " + numExp, ex);
            throw new Exception(ex);
        } finally {
            try {
                if (adaptador != null) {
                    adaptador.devolverConexion(con);
                }
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    /**
     *
     * Función que obtiene el listado de la base de datos de los motivos de
     * denegacion
     *
     * @param numExp: Número de expediente
     * @param con
     * @return 
     * @throws java.lang.Exception
     */
    public List<ValorCampoDesplegableModuloIntegracionVO> getDatosMotivosDeneg(String numExp, Connection con) throws Exception {
        try {
            MeLanbide41DAO meLanbide41DAO = MeLanbide41DAO.getInstance();
            return meLanbide41DAO.getDatosMotivosDenegacion(con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos sobre las Especialides Solicitadas para expediente " + numExp, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Especialidades solicitadas para expediente " + numExp, ex);
            throw new Exception(ex);
        }
    }

    public EspecialidadesVO getEspecialidadPorCodigo(String numExpediente, String id, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide41DAO meLanbide41DAO = MeLanbide41DAO.getInstance();
            return getEspecialidadPorCodigo(numExpediente, id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos sobre una Especialidad Especifica por codigo: " + numExpediente + " - " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando sobre una Especialidad Especifica por codigo:" + numExpediente + " - " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public EspecialidadesVO getEspecialidadPorCodigo(String numExpediente, String id, Connection con) throws Exception {
        try {
            MeLanbide41DAO meLanbide41DAO = MeLanbide41DAO.getInstance();
            return meLanbide41DAO.getEspecialidadPorCodigo(numExpediente, id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos sobre una Especialidad Especifica por codigo: " + numExpediente + " - " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando sobre una Especialidad Especifica por codigo:" + numExpediente + " - " + id, ex);
            throw new Exception(ex);
        }
    }

    public List<ServiciosVO> getDatosServicios(String numExp, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide41DAO meLanbide41DAO = MeLanbide41DAO.getInstance();
            return meLanbide41DAO.getDatosServicios(numExp, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos sobre las Servicios Higienico-Sanitarios para expediente " + numExp + "Error : " + e.getMensaje(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Servicios Higienico-Sanitariospara expediente " + numExp + "Error : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public ServiciosVO getServicioPorCodigo(String numExpediente, String id, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide41DAO meLanbide41DAO = MeLanbide41DAO.getInstance();
            return meLanbide41DAO.getServicioPorCodigo(numExpediente, id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos sobre un servicio aseo hugienico-sanitario por codigo: " + numExpediente + " - " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando sobre una servicio aseo hugienico-sanitario  por codigo:" + numExpediente + " - " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public List<DisponibilidadVO> getDatosDisponibilidad(String numExp, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide41DAO meLanbide41DAO = MeLanbide41DAO.getInstance();
            return meLanbide41DAO.getDatosDisponibilidad(numExp, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos sobre Disponibilidad de recusros por especialidades formativas para expediente " + numExp + "Error : " + e.getMensaje(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando  Disponibilidad de recusros por especialidades formativas para expediente " + numExp + "Error : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public DisponibilidadVO getDisponibilidadPorCodigo(String numExp, String id, String idespsol, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide41DAO meLanbide41DAO = MeLanbide41DAO.getInstance();
            return meLanbide41DAO.getDisponibilidadPorCodigo(numExp, id, idespsol, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos sobre Disponibilidad de recusros por especialidades formativas para expediente " + numExp + " Codigo " + id + " Error : " + e.getMensaje(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando  Disponibilidad de recusros por especialidades formativas para expediente " + numExp + "Error : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public DisponibilidadVO getDisponibilidadPorCodigoEspSol_NumExp(String numExp, Integer idespsol, Connection con) throws Exception {
        try {
            MeLanbide41DAO meLanbide41DAO = MeLanbide41DAO.getInstance();
            return meLanbide41DAO.getDisponibilidadPorCodigoEspSol_NumExp(numExp, idespsol, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos sobre Disponibilidad de recusros por codigo de especialidad de la lista esp. solicitadas y numero de expediente " + numExp + " Codigo " + idespsol + " Error : " + e.getMensaje(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando  Disponibilidad de recusros por especialidades formativas para expediente " + numExp + "Error : " + ex.getMessage(), ex);
            throw new Exception(ex);
        }
    }

    public List<CapacidadVO> getDatosCapacidad(String numExp, Integer idEpsol, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide41DAO meLanbide41DAO = MeLanbide41DAO.getInstance();
            return meLanbide41DAO.getDatosCapacidad(numExp, idEpsol, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos sobre capacidad de las instalaciones para expediente " + numExp + "Error : " + e.getMensaje(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando  Capacidad de las instalaciones para expediente " + numExp + "Error : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public CapacidadVO getCapacidadPorCodigo(String numExpediente, String id, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide41DAO meLanbide41DAO = MeLanbide41DAO.getInstance();
            return meLanbide41DAO.getCapacidadPorCodigo(numExpediente, id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos sobre un servicio aseo hugienico-sanitario por codigo: " + numExpediente + " - " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando sobre una servicio aseo hugienico-sanitario  por codigo:" + numExpediente + " - " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public List<DotacionVO> getDatosDotacion(String numExp, Integer idEpsol, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide41DAO meLanbide41DAO = MeLanbide41DAO.getInstance();
            return meLanbide41DAO.getDatosDotacion(numExp, idEpsol, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos sobre dotacion para expediente " + numExp + "Error : " + e.getMensaje(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando  dotacion para expediente " + numExp + "Error : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public DotacionVO getDotacionPorCodigo(String numExpediente, String id, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide41DAO meLanbide41DAO = MeLanbide41DAO.getInstance();
            return meLanbide41DAO.getDotacionPorCodigo(numExpediente, id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos sobre Dotacion de isntalaciones por codigo: " + numExpediente + " - " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando sobre Material de consumo por codigo:" + numExpediente + " - " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public List<MaterialVO> getDatosMaterial(String numExp, Integer idEpsol, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide41DAO meLanbide41DAO = MeLanbide41DAO.getInstance();
            return meLanbide41DAO.getDatosMaterial(numExp, idEpsol, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos sobre material de consumo " + numExp + "Error : " + e.getMensaje(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando  materil de consumo para expediente " + numExp + "Error : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public MaterialVO getMaterialPorCodigo(String numExpediente, String id, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide41DAO meLanbide41DAO = MeLanbide41DAO.getInstance();
            return meLanbide41DAO.getMaterialPorCodigo(numExpediente, id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos sobre Material de consumo  por codigo: " + numExpediente + " - " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando sobre Material de consumo por codigo:" + numExpediente + " - " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public List<IdentificacionEspVO> getDatosIdentificacionEsp(String numExp, Integer idEpsol, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide41DAO meLanbide41DAO = MeLanbide41DAO.getInstance();
            return meLanbide41DAO.getDatosIdentificacionEsp(numExp, idEpsol, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos sobre la lista de Identificacion de especialidad para expediente " + numExp + "Error : " + e.getMensaje(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando  la lista de Identificacion de especialidad para expediente " + numExp + "Error : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public IdentificacionEspVO getIdentificacionEspPorCodigo(String numExp, String id, String idespsol, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide41DAO meLanbide41DAO = MeLanbide41DAO.getInstance();
            return meLanbide41DAO.getIdentificacionEspPorCodigo(numExp, id, idespsol, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos lista de identifiacion de especialidad para expediente " + numExp + " Codigo " + id + " Error : " + e.getMensaje(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando  getIdentificacionEspPorCodigo para expediente " + numExp + "Error : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public IdentificacionEspVO getIdentificacionEspPorCodigoEspSol_NumExp(String numExp, Integer idespsol, Connection con) throws Exception {
        try {
            MeLanbide41DAO meLanbide41DAO = MeLanbide41DAO.getInstance();
            return meLanbide41DAO.getIdentificacionEspPorCodigoEspSol_NumExp(numExp, idespsol, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando identifiacion de especialidad desde la lista 1 esp. solicitadas para expediente " + numExp + " Codigo " + idespsol + " Error : " + e.getMensaje(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando  getIdentificacionEspPorCodigo para expediente " + numExp + "Error : " + ex.getMessage(), ex);
            throw new Exception(ex);
        }
    }

    public String getTipoCentro(int codOrg, String proce, int ejercicio, String numExpediente, Integer ocurrencia, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide41DAO meLanbide41DAO = MeLanbide41DAO.getInstance();
            return meLanbide41DAO.getTipoCentro(codOrg, proce, ejercicio, numExpediente, ocurrencia, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando el tipo de centro para el expediente " + numExpediente + " Error : " + e.getMensaje(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando el tipo de centro para el expediente " + numExpediente + " Error : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    /**
     * Devuelve la lista con los códigos y descripciones de los certificados
     *
     * @param con
     * @return ArrayList CerCertificadoVO (Solo incluye el código y la
     * descripción)
     * @throws BDException
     * @throws Exception
     */
    public ArrayList<CerCertificadoVO> getCertificados(Connection con) throws BDException, Exception {
        if (log.isDebugEnabled()) {
            log.debug("getCertificados() : BEGIN");
        }
        ArrayList<CerCertificadoVO> listaCertificados = new ArrayList<CerCertificadoVO>();
        try {
            MeLanbide41DAO meLanbide41DAO = MeLanbide41DAO.getInstance();
            listaCertificados = meLanbide41DAO.getCertificados(con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD recuperando los certificados", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción recuperando los certificados", ex);
            throw new Exception(ex);
        }
        if (log.isDebugEnabled()) {
            log.debug("getCertificados() : END");
        }
        return listaCertificados;
    }//getCertificados

    private void realizarInsertSQL(String sql, AdaptadorSQLBD adaptador) throws MeLanbide41Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide41DAO meLanbide41DAO = MeLanbide41DAO.getInstance();

        } catch (BDException e) {
            log.error("Error BD insertando expediente electrónico", e);
            throw new MeLanbide41Exception("Error BD insertando expediente electrónico", e);
        } catch (Exception e) {
            log.error("Error general insertando expediente electrónico", e);
            throw new MeLanbide41Exception("Error general insertando expediente electrónico", e);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public List<EspecialidadesVO> getDatosEspecialidades(String numExp, Connection con) throws Exception {
        try {
            MeLanbide41DAO meLanbide41DAO = MeLanbide41DAO.getInstance();
            return meLanbide41DAO.getDatosEspecialidadesInterfaz(numExp, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos sobre las Especialides Solicitadas para expediente " + numExp, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Especialidades solicitadas para expediente " + numExp, ex);
            throw new Exception(ex);
        }
    }

    public List<String> getMotDenegSelec(String idEspecialidad, Connection con) throws Exception {
        try {
            MeLanbide41DAO meLanbide41DAO = MeLanbide41DAO.getInstance();
            return meLanbide41DAO.getDatosEspecialidadMotDeneg(idEspecialidad, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos sobre los motivos denegación especialidad", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos sobre los motivos denegación especialidad", ex);
            throw new Exception(ex);
        }
    }

    public int crearNuevaEspecialidad(EspecialidadesVO especialidad, Connection con) throws Exception {
        try {
            MeLanbide41DAO meLanbide41DAO = MeLanbide41DAO.getInstance();
            return meLanbide41DAO.crearNuevaEspecialidad(especialidad, con);
        } catch (BDException e) {
            log.error("Se ha producido un Excepcion en la BBDD creando  una Especialidad : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD creando Especialidda : " + ex.getMessage(), ex);
            throw new Exception(ex);
        }
    }

    public void crearNuevaDisponibilidad(int idEspecialidad, String numExp, String codCP, Connection con) throws Exception {
        try {
            MeLanbide41DAO meLanbide41DAO = MeLanbide41DAO.getInstance();
            meLanbide41DAO.crearNuevaDisponibilidad(idEspecialidad, numExp, codCP, con);
        } catch (BDException e) {
            log.error("Se ha producido un error al Actualizar lienas de Disponibilidad e Identificacion de la Especilidad dada de alta : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido un error al Actualizar lienas de Disponibilidad e Identificacion de la Especilidad dada de alta : " + ex.getMessage(), ex);
            throw new Exception(ex);
        }
    }

    public void crearNuevaEspecialidadMotDeneg(int idEspecialidad, String idMotDeneg, Connection con) throws Exception {
        try {
            MeLanbide41DAO meLanbide41DAO = MeLanbide41DAO.getInstance();
            meLanbide41DAO.crearNuevaEspecialidadMotDeneg(idEspecialidad, idMotDeneg, con);
        } catch (BDException e) {
            log.error("Se ha producido un Excepcion en la BBDD creando  motivo denegación Especialidad : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD creando motivo denegación Especialidad : " + ex.getMessage(), ex);
            throw new Exception(ex);
        }
    }

    public boolean modificarEspecialidad(EspecialidadesVO especialidad, Connection con) throws Exception {
        try {
            MeLanbide41DAO meLanbide41DAO = MeLanbide41DAO.getInstance();
            return meLanbide41DAO.modificarEspecialidad(especialidad, con);
        } catch (BDException e) {
            log.error("Se ha producido un Excepcion en la BBDD Modificando  una Especialidad : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD Modificando Especialidad : " + ex.getMessage(), ex);
            throw new Exception(ex);
        }
    }

    public int eliminarEspecialidadMotDeneg(Integer idEspecialidad, Connection con) throws Exception {
        try {
            MeLanbide41DAO meLanbide41DAO = MeLanbide41DAO.getInstance();
            return meLanbide41DAO.eliminarEspecialidadMotDeneg(idEspecialidad, con);
        } catch (BDException e) {
            log.error("Se ha producido un Excepcion en la BBDD eliminando motivos denegación de una especialidad: " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD eliminando motivos denegación de una especialidad : " + ex.getMessage(), ex);
            throw new Exception(ex);
        }
    }

    public int eliminarEspecialidad(String numExp, Integer id, Connection con) throws Exception {
        try {
            MeLanbide41DAO meLanbide41DAO = MeLanbide41DAO.getInstance();
            return meLanbide41DAO.eliminarEspecialidad(numExp, id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD eliminando especialidad " + numExp + " - " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD eliminando especialidad " + numExp + " - " + id, ex);
            throw new Exception(ex);
        }
    }

    public boolean crearNuevoServicio(ServiciosVO datoNuevo, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide41DAO meLanbide41DAO = MeLanbide41DAO.getInstance();
            return meLanbide41DAO.crearNuevoServicio(datoNuevo, con);
        } catch (BDException e) {
            log.error("Se ha producido un Excepcion en la BBDD creando  un Aseo y Servicio Higienico-Sanitario : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD creando Aseo y Servicio Higienico-Sanitario : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean modificarServicio(ServiciosVO datoNuevo, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide41DAO meLanbide41DAO = MeLanbide41DAO.getInstance();
            return meLanbide41DAO.modificarServicio(datoNuevo, con);
        } catch (BDException e) {
            log.error("Se ha producido un Excepcion en la BBDD Modificando  un Aseo Servicio Higienico-Sanitario : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD Modificando Aseo Servicio Higienico-Sanitario : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public int eliminarServicio(String numExp, Integer id, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide41DAO meLanbide41DAO = MeLanbide41DAO.getInstance();
            return meLanbide41DAO.eliminarServicio(numExp, id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD eliminando Aseo Servcio Higienico-Sanitario " + numExp + " - " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD eliminando Aseo Servcio Higienico-Sanitario " + numExp + " - " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean modificarDisponibilidad(DisponibilidadVO datoNuevo, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide41DAO meLanbide41DAO = MeLanbide41DAO.getInstance();
            return meLanbide41DAO.modificarDisponibilidad(datoNuevo, con);
        } catch (BDException e) {
            log.error("Se ha producido un Excepcion en la BBDD Modificando  una linea de disponibilidad de recursos : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD Modificando linea de disponibilidad de recursos : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean crearNuevaIdentificacionEsp(IdentificacionEspVO datoNuevo, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide41DAO meLanbide41DAO = MeLanbide41DAO.getInstance();
            return meLanbide41DAO.crearNuevaIdentificacioEsp(datoNuevo, con);
        } catch (BDException e) {
            log.error("Se ha producido un Excepcion en la BBDD Modificando  una linea de la lista identificacion de disponibilidad : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD Modificando linea de identificaicon de especialidad : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean modificarIdentificacionEsp(IdentificacionEspVO datoNuevo, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide41DAO meLanbide41DAO = MeLanbide41DAO.getInstance();
            return meLanbide41DAO.modificarIdentificacionEsp(datoNuevo, con);
        } catch (BDException e) {
            log.error("Se ha producido un Excepcion en la BBDD Modificando  una linea de la lista identificacion de disponibilidad : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD Modificando linea de identificaicon de especialidad : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public int eliminarDisponibilidadDesdeListEspSol(String numExp, Integer id, Integer idespsol, Connection con) throws Exception {
        try {
            MeLanbide41DAO meLanbide41DAO = MeLanbide41DAO.getInstance();
            return meLanbide41DAO.eliminarDisponibilidadDesdeListEspSol(numExp, id, idespsol, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD eliminando Disponibilidad desde la lista de especialidades solicitadas " + numExp + " - " + id + " - " + idespsol, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD eliminandoDisponibilidad desde la lista de especialidades solicitadas " + numExp + " - " + id + " - " + idespsol, ex);
            throw new Exception(ex);
        }
    }

    public int eliminarIdentificacionEspDesdeListEspSol(String numExp, Integer id, Integer idespsol, Connection con) throws Exception {
        try {
            MeLanbide41DAO meLanbide41DAO = MeLanbide41DAO.getInstance();
            return meLanbide41DAO.eliminarIdentificacionEspDesdeListEspSol(numExp, id, idespsol, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD eliminando Identificacion de especialidad desde la lista de especialidades solicitadas " + numExp + " - " + id + " - " + idespsol, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD eliminando identificacion de disponibilidad desde la lista de especialidades solicitadas " + numExp + " - " + id + " - " + idespsol, ex);
            throw new Exception(ex);
        }
    }

    public boolean crearNuevaCapacidad(CapacidadVO datoNuevo, AdaptadorSQLBD adaptador) throws Exception {
           log.info("Entramos en crearNuevaCapacidad de " + this.getClass().getSimpleName());
      Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide41DAO meLanbide41DAO = MeLanbide41DAO.getInstance();
            return meLanbide41DAO.crearNuevaCapacidad(datoNuevo, con);
        } catch (BDException e) {
            log.error("Se ha producido un Excepcion en la BBDD creando  Capcidad de Instalaciones  : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD creando Capcidad de Instalaciones  : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean modificarCapacidad(CapacidadVO datoNuevo, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide41DAO meLanbide41DAO = MeLanbide41DAO.getInstance();
            return meLanbide41DAO.modificarCapacidad(datoNuevo, con);
        } catch (BDException e) {
            log.error("Se ha producido un Excepcion en la BBDD Modificando  una Capcidad de Instalaciones  : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD Modificando Capcidad de Instalaciones  : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public int eliminarCapacidad(String numExp, Integer id, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide41DAO meLanbide41DAO = MeLanbide41DAO.getInstance();
            return meLanbide41DAO.eliminarCapacidad(numExp, id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD eliminando Capacidad de Instalaciones " + numExp + " - " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD eliminando capacidad de instalaciones " + numExp + " - " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean crearNuevaDotacion(DotacionVO datoNuevo, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide41DAO meLanbide41DAO = MeLanbide41DAO.getInstance();
            return meLanbide41DAO.crearNuevaDotacion(datoNuevo, con);
        } catch (BDException e) {
            log.error("Se ha producido un Excepcion en la BBDD creando  Dotacion de instalacines : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD creando Dotacion de instalaciones : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean modificarDotacion(DotacionVO datoNuevo, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide41DAO meLanbide41DAO = MeLanbide41DAO.getInstance();
            return meLanbide41DAO.modificarDotacion(datoNuevo, con);
        } catch (BDException e) {
            log.error("Se ha producido un Excepcion en la BBDD Modificando  Dotacion de Instalacines : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD Modificando Dotacion de instalacines : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public int eliminarDotacion(String numExp, Integer id, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide41DAO meLanbide41DAO = MeLanbide41DAO.getInstance();
            return meLanbide41DAO.eliminarDotacion(numExp, id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD eliminando Dotacion de instalaciones " + numExp + " - " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD eliminando Dotacion de instalciones " + numExp + " - " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean crearNuevoMaterial(MaterialVO datoNuevo, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide41DAO meLanbide41DAO = MeLanbide41DAO.getInstance();
            return meLanbide41DAO.crearNuevoMaterial(datoNuevo, con);
        } catch (BDException e) {
            log.error("Se ha producido un Excepcion en la BBDD creando  Material de consumo  : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD creando Material de consumo : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean modificarMaterial(MaterialVO datoNuevo, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide41DAO meLanbide41DAO = MeLanbide41DAO.getInstance();
            return meLanbide41DAO.modificarMaterial(datoNuevo, con);
        } catch (BDException e) {
            log.error("Se ha producido un Excepcion en la BBDD Modificando  Material de consumo : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD Modificando Material de consumo  : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public int eliminarMaterial(String numExp, Integer id, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide41DAO meLanbide41DAO = MeLanbide41DAO.getInstance();
            return meLanbide41DAO.eliminarMaterial(numExp, id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD eliminando Material de consumo " + numExp + " - " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD eliminando material de consumo " + numExp + " - " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    /**
     *
     * @param codOrganizacion
     * @param numExpediente
     * @param con
     * @return
     * @throws MeLanbide41Exception
     */
    public String getDocInteresadoParaIkasLanF(int codOrganizacion, String numExpediente, Connection con) throws MeLanbide41Exception {
        log.info("getDocInteresadoParaIkasLanF -  BEGIN - " + codOrganizacion + " - " + numExpediente);
        String documentoInteresado = "";
        IModuloIntegracionExternoCamposFlexia interfazCamposSuple = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        SalidaIntegracionVO salidaCS = new SalidaIntegracionVO();
        String valorCScodigoCentro = "";
        String valorCScodigoUbicacionCentro = "";
        try {
            String[] datosExpediente = numExpediente.split(ConstantesMeLanbide41.BARRA_SEPARADORA);
            if (datosExpediente != null && datosExpediente.length > 1) {
                String ejercicioExpediente = datosExpediente[0];
                String codProc = datosExpediente[1];
                // RGCF Y CEPAp PASA EL CIF 
                if (codProc.equalsIgnoreCase(ConfigurationParameter.getParameter(codOrganizacion + ConstantesMeLanbide41.BARRA_SEPARADORA + ConstantesMeLanbide41.CODIGO_PROCEDIMIENTO_RGCF, ConstantesMeLanbide41.FICHERO_PROPIEDADES))
                        || codProc.equalsIgnoreCase(ConfigurationParameter.getParameter(codOrganizacion + ConstantesMeLanbide41.BARRA_SEPARADORA + ConstantesMeLanbide41.CODIGO_PROCEDIMIENTO_CEPAP, ConstantesMeLanbide41.FICHERO_PROPIEDADES))) {
                    log.info("Vamos a obtener DNI/CIF para interesado ");
                    documentoInteresado = MeLanbide41DAO.getInstance().getDocumentoInteresadoRolDefectoAltaExpFormaLANF(codOrganizacion, numExpediente, con);
                }// En el resto de casos (RGEF Y RGCFM) buscamos el censo del centro
                else {
                    // Recuperamos los valores de los campos supl. cod centro y cod ubicacion centro
                    log.info("Vamos a obtener Numero de Censo de acuerdo al cod de centro y codigo ubicacion ");
                    String codCampoSupleCodigoCentro = "";
                    String tipoCampoSupleCodigoCentro = "";
                    String codCampoSupleCodigoUbicacionCentro = "";
                    String tipoCampoSupleCodigoUbicacionCentro = "";
                    String[] datosCorr = null;
                    if (codProc.equalsIgnoreCase(ConfigurationParameter.getParameter(codOrganizacion + ConstantesMeLanbide41.BARRA_SEPARADORA + ConstantesMeLanbide41.CODIGO_PROCEDIMIENTO_RGCFM, ConstantesMeLanbide41.FICHERO_PROPIEDADES))) {
                        codCampoSupleCodigoCentro = ConfigurationParameter.getParameter(codOrganizacion + ConstantesMeLanbide41.BARRA_SEPARADORA + ConstantesMeLanbide41.CODIGO_CAMPOSUP_CODCENTRO_RGCFM, ConstantesMeLanbide41.FICHERO_PROPIEDADES);
                        tipoCampoSupleCodigoCentro = ConfigurationParameter.getParameter(codOrganizacion + ConstantesMeLanbide41.BARRA_SEPARADORA + ConstantesMeLanbide41.TIPODATO_CAMPOSUP_CODCENTRO_RGCFM, ConstantesMeLanbide41.FICHERO_PROPIEDADES);
                        codCampoSupleCodigoUbicacionCentro = ConfigurationParameter.getParameter(codOrganizacion + ConstantesMeLanbide41.BARRA_SEPARADORA + ConstantesMeLanbide41.CODIGO_CAMPOSUP_CODUBICACION_CENTRO_RGCFM, ConstantesMeLanbide41.FICHERO_PROPIEDADES);
                        tipoCampoSupleCodigoUbicacionCentro = ConfigurationParameter.getParameter(codOrganizacion + ConstantesMeLanbide41.BARRA_SEPARADORA + ConstantesMeLanbide41.TIPODATO_CAMPOSUP_CODUBICACION_CENTRO_RGCFM, ConstantesMeLanbide41.FICHERO_PROPIEDADES);
                    } else if (codProc.equalsIgnoreCase(ConfigurationParameter.getParameter(codOrganizacion + ConstantesMeLanbide41.BARRA_SEPARADORA + ConstantesMeLanbide41.CODIGO_PROCEDIMIENTO_RGEF, ConstantesMeLanbide41.FICHERO_PROPIEDADES))) {
                        codCampoSupleCodigoCentro = ConfigurationParameter.getParameter(codOrganizacion + ConstantesMeLanbide41.BARRA_SEPARADORA + ConstantesMeLanbide41.CODIGO_CAMPOSUP_CODCENTRO_RGEF, ConstantesMeLanbide41.FICHERO_PROPIEDADES);
                        tipoCampoSupleCodigoCentro = ConfigurationParameter.getParameter(codOrganizacion + ConstantesMeLanbide41.BARRA_SEPARADORA + ConstantesMeLanbide41.TIPODATO_CAMPOSUP_CODCENTRO_RGEF, ConstantesMeLanbide41.FICHERO_PROPIEDADES);
                        codCampoSupleCodigoUbicacionCentro = ConfigurationParameter.getParameter(codOrganizacion + ConstantesMeLanbide41.BARRA_SEPARADORA + ConstantesMeLanbide41.CODIGO_CAMPOSUP_CODUBICACION_CENTRO_RGEF, ConstantesMeLanbide41.FICHERO_PROPIEDADES);
                        tipoCampoSupleCodigoUbicacionCentro = ConfigurationParameter.getParameter(codOrganizacion + ConstantesMeLanbide41.BARRA_SEPARADORA + ConstantesMeLanbide41.TIPODATO_CAMPOSUP_CODUBICACION_CENTRO_RGEF, ConstantesMeLanbide41.FICHERO_PROPIEDADES);
                    }
                    log.info("Codigo Campo Suplementario Codigo Centro : " + codCampoSupleCodigoCentro);
                    salidaCS = interfazCamposSuple.getCampoSuplementarioExpediente(String.valueOf(codOrganizacion), ejercicioExpediente, numExpediente, codProc, codCampoSupleCodigoCentro, Integer.valueOf(tipoCampoSupleCodigoCentro));
                    if (salidaCS.getStatus() == 0) {
                        CampoSuplementarioModuloIntegracionVO codigoCentro = salidaCS.getCampoSuplementario();
                        valorCScodigoCentro = codigoCentro.getValorTexto();
                    }
                    log.info("Codigo Campo Suplementario Codigo Ubicacion Centro : " + codCampoSupleCodigoUbicacionCentro);
                    salidaCS = interfazCamposSuple.getCampoSuplementarioExpediente(String.valueOf(codOrganizacion), ejercicioExpediente, numExpediente, codProc, codCampoSupleCodigoUbicacionCentro, Integer.valueOf(tipoCampoSupleCodigoUbicacionCentro));
                    if (salidaCS.getStatus() == 0) {
                        CampoSuplementarioModuloIntegracionVO codigoUbicacionCentro = salidaCS.getCampoSuplementario();
                        valorCScodigoUbicacionCentro = codigoUbicacionCentro.getValorTexto();
                    }
                    log.info("Codigo Centro y Ubicacion Centro A Consultar : " + valorCScodigoCentro + " - " + valorCScodigoUbicacionCentro);
                    // #418827 - #16806 hay que obtener corrServicio y corrSubservicio para buscar el numero de Censo
                    datosCorr = MeLanbide41DAO.getInstance().getCodigosCorr(valorCScodigoCentro, valorCScodigoUbicacionCentro, con);
                    log.info("corrSubservicio: " + datosCorr[1]);
                    documentoInteresado = MeLanbide41DAO.getInstance().getNumeroCensoCenFor(valorCScodigoCentro, valorCScodigoUbicacionCentro, datosCorr[1], con);
                    //   documentoInteresado = MeLanbide41DAO.getInstance().getNumeroCensoCentro(valorCScodigoCentro, valorCScodigoUbicacionCentro, con);
                }
            }
        } catch (Exception e) {
            log.error("Error en el Manager Melabide41 al intentar recuperar el Documento del Interesado - ", e);
            throw new MeLanbide41Exception("Error en el Manager Melabide41 al intentar recuperar el Documento del Interesado : " + e.getMessage());
        }
        log.info("documentoInteresado Recuperado " + documentoInteresado);
        log.info("getDocInteresadoParaIkasLanF -  END - " + codOrganizacion + " - " + numExpediente);
        return documentoInteresado;
    }

    /**
     *
     * Función que llama al dao para obtener el número de censo SILICOI de base
     * de datos
     *
     * @param codCentro: Código del centro
     * @param codUbicacion: Código de la ubicación
     * @param corrSubservicio
     * @param adaptador: adapatador
     * @return 
     * @throws java.lang.Exception 
     */
    public String getNumCensoSILICOI(String codCentro, String codUbicacion, String corrSubservicio, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            if (adaptador != null) {
                con = adaptador.getConnection();
            }
            MeLanbide41DAO meLanbide41DAO = MeLanbide41DAO.getInstance();
            return meLanbide41DAO.getNumeroCensoSILICOI(codCentro, codUbicacion, corrSubservicio, con);
        } catch (BDException e) {
            log.error("Error en función getNumCensoSILICOI", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Error en función getNumCensoSILICOI", ex);
            throw new Exception(ex);
        } finally {
            try {
                if (adaptador != null) {
                    adaptador.devolverConexion(con);
                }
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    /**
     *
     * Función que obtiene el número de censo de un centro, que es el campo
     * LAN_NUM_REG_AUT de la tabla LAN_CENTROS_SERVICIOS
     *
     * @param codCentro: Código del centro
     * @param codUbicacion: Código de la ubicación
     * @param adaptador: adapatador
     * @return
     * @throws java.lang.Exception
     */
    public String getNumCensoLanbide(String codCentro, String codUbicacion, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            if (adaptador != null) {
                con = adaptador.getConnection();
            }
            MeLanbide41DAO meLanbide41DAO = MeLanbide41DAO.getInstance();
            return meLanbide41DAO.getNumeroCensoLanbide(codCentro, codUbicacion, con);
        } catch (BDException e) {
            log.error("Error en función getNumCensoLanbide", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Error en función getNumCensoLanbide", ex);
            throw new Exception(ex);
        } finally {
            try {
                if (adaptador != null) {
                    adaptador.devolverConexion(con);
                }
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    /**
     *
     * Metodo para recuperar los codigos corrServicio y corrSubservicio
     *
     * @param codCentro código del centro
     * @param codUbic código de la ubicación del centro
     * @param adaptador adaptador BD
     * @return array de Strings con los 2 códigos
     * @throws Exception
     */
    public String[] getCodigosCorr(String codCentro, String codUbic, AdaptadorSQLBD adaptador) throws Exception {
        log.debug("Entra en getCodigosCorr de " + this.getClass().getSimpleName());
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return MeLanbide41DAO.getInstance().getCodigosCorr(codCentro, codUbic, con);
        } catch (Exception e) {
            log.error("Error en función getCodigosCorr", e);
            throw new Exception(e);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    /**
     *
     * @param codigoCentro
     * @param codigoUbicacion
     * @param corrSubservicio
     * @param adaptador
     * @return String con el número de censo del centro en CENFOR
     * @throws java.lang.Exception
     */
    public String getNumeroCensoCenFor(String codigoCentro, String codigoUbicacion, String corrSubservicio, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return MeLanbide41DAO.getInstance().getNumeroCensoCenFor(codigoCentro, codigoUbicacion, corrSubservicio, con);
        } catch (Exception e) {
            log.error("Error en función getNumeroCensoCenFor", e);
            throw new Exception(e);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

}
