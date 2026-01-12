/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide50.manager;

import es.altia.flexia.integracion.moduloexterno.melanbide50.dao.MeLanbide50DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide50.util.MeLanbide50Exception;
import es.altia.flexia.integracion.moduloexterno.melanbide50.vo.alta.CampoSuplementario;
import es.altia.flexia.integracion.moduloexterno.melanbide50.vo.alta.Expediente;
import es.altia.flexia.integracion.moduloexterno.melanbide50.vo.alta.Tercero;
import es.altia.flexia.integracion.moduloexterno.melanbide50.vo.capacidad.CapacidadVO;
import es.altia.flexia.integracion.moduloexterno.melanbide50.vo.disponibilidad.DisponibilidadVO;
import es.altia.flexia.integracion.moduloexterno.melanbide50.vo.documentos.DocumentosVO;
import es.altia.flexia.integracion.moduloexterno.melanbide50.vo.dotacion.DotacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide50.vo.espacios.EspacioVO;
import es.altia.flexia.integracion.moduloexterno.melanbide50.vo.especialidades.CerCertificadoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide50.vo.especialidades.EspecialidadesVO;
import es.altia.flexia.integracion.moduloexterno.melanbide50.vo.identificacionesp.IdentificacionEspVO;
import es.altia.flexia.integracion.moduloexterno.melanbide50.vo.material.MaterialVO;
import es.altia.flexia.integracion.moduloexterno.melanbide50.vo.servicios.ServiciosVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.ValorCampoDesplegableModuloIntegracionVO;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author davidg
 */
public class MeLanbide50Manager {

    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide50Manager.class);

    //Instancia
    private static MeLanbide50Manager instance = null;

    public static MeLanbide50Manager getInstance() {
        if (instance == null) {
            synchronized (MeLanbide50Manager.class) {
                instance = new MeLanbide50Manager();
            }
        }
        return instance;
    }

    public List<EspecialidadesVO> getDatosEspecialidades(String numExp, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            if (adaptador != null) {
                log.error(" adapt es dif de null -- antes de crear conexion" + adaptador.toString());
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

    public List<EspecialidadesVO> getDatosEspecialidades(String numExp, Connection con) throws Exception {
        try {
            MeLanbide50DAO meLanbide50DAO = MeLanbide50DAO.getInstance();
            return meLanbide50DAO.getDatosEspecialidades(numExp, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos sobre las Especialides Solicitadas para expediente " + numExp, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Especialidades solicitadas para expediente " + numExp, ex);
            throw new Exception(ex);
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
            MeLanbide50DAO meLanbide50DAO = MeLanbide50DAO.getInstance();
            return meLanbide50DAO.getDatosMotivosDenegacion(con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos sobre las Especialides Solicitadas para expediente " + numExp, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Especialidades solicitadas para expediente " + numExp, ex);
            throw new Exception(ex);
        }
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
            MeLanbide50DAO meLanbide50DAO = MeLanbide50DAO.getInstance();
            return meLanbide50DAO.getNumeroCensoSILICOI(codCentro, codUbicacion, corrSubservicio, con);
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
                log.error(" adapt es dif de null -- antes de crear conexion" + adaptador.toString());
                con = adaptador.getConnection();
            }
            MeLanbide50DAO meLanbide50DAO = MeLanbide50DAO.getInstance();
            return meLanbide50DAO.getNumeroCensoLanbide(codCentro, codUbicacion, con);
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

    public EspecialidadesVO getEspecialidadPorCodigo(String numExpediente, String id, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide50DAO meLanbide50DAO = MeLanbide50DAO.getInstance();
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
            MeLanbide50DAO meLanbide50DAO = MeLanbide50DAO.getInstance();
            return meLanbide50DAO.getEspecialidadPorCodigo(numExpediente, id, con);
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
            MeLanbide50DAO meLanbide50DAO = MeLanbide50DAO.getInstance();
            return meLanbide50DAO.getDatosServicios(numExp, con);
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
            MeLanbide50DAO meLanbide50DAO = MeLanbide50DAO.getInstance();
            return meLanbide50DAO.getServicioPorCodigo(numExpediente, id, con);
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
            MeLanbide50DAO meLanbide50DAO = MeLanbide50DAO.getInstance();
            return meLanbide50DAO.getDatosDisponibilidad(numExp, con);
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
            MeLanbide50DAO meLanbide50DAO = MeLanbide50DAO.getInstance();
            return meLanbide50DAO.getDisponibilidadPorCodigo(numExp, id, idespsol, con);
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
            MeLanbide50DAO meLanbide50DAO = MeLanbide50DAO.getInstance();
            return meLanbide50DAO.getDisponibilidadPorCodigoEspSol_NumExp(numExp, idespsol, con);
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
            MeLanbide50DAO meLanbide50DAO = MeLanbide50DAO.getInstance();
            return meLanbide50DAO.getDatosCapacidad(numExp, idEpsol, con);
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
            MeLanbide50DAO meLanbide50DAO = MeLanbide50DAO.getInstance();
            return meLanbide50DAO.getCapacidadPorCodigo(numExpediente, id, con);
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
            MeLanbide50DAO meLanbide50DAO = MeLanbide50DAO.getInstance();
            return meLanbide50DAO.getDatosDotacion(numExp, idEpsol, con);
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
            MeLanbide50DAO meLanbide50DAO = MeLanbide50DAO.getInstance();
            return meLanbide50DAO.getDotacionPorCodigo(numExpediente, id, con);
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
            MeLanbide50DAO meLanbide50DAO = MeLanbide50DAO.getInstance();
            return meLanbide50DAO.getDatosMaterial(numExp, idEpsol, con);
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
            MeLanbide50DAO meLanbide50DAO = MeLanbide50DAO.getInstance();
            return meLanbide50DAO.getMaterialPorCodigo(numExpediente, id, con);
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
            MeLanbide50DAO meLanbide50DAO = MeLanbide50DAO.getInstance();
            return meLanbide50DAO.getDatosIdentificacionEsp(numExp, idEpsol, con);
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
            MeLanbide50DAO meLanbide50DAO = MeLanbide50DAO.getInstance();
            return meLanbide50DAO.getIdentificacionEspPorCodigo(numExp, id, idespsol, con);
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
            MeLanbide50DAO meLanbide50DAO = MeLanbide50DAO.getInstance();
            return meLanbide50DAO.getIdentificacionEspPorCodigoEspSol_NumExp(numExp, idespsol, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando identifiacion de especialidad desde la lista 1 esp. solicitadas para expediente " + numExp + " Codigo " + idespsol + " Error : " + e.getMensaje(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando  getIdentificacionEspPorCodigo para expediente " + numExp + "Error : " + ex.getMessage(), ex);
            throw new Exception(ex);
        }
    }

    public List<EspacioVO> getDatosEspacio(String numExp, Integer idEpsol, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide50DAO meLanbide50DAO = MeLanbide50DAO.getInstance();
            return meLanbide50DAO.getDatosEspacio(numExp, idEpsol, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos sobre espacios " + numExp + " Error : " + e.getMensaje(), e);
            throw new Exception(e);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public EspacioVO getEspacioPorCodigo(String numExp, String id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide50DAO meLanbide50DAO = MeLanbide50DAO.getInstance();
            return meLanbide50DAO.getEspacioPorCodigo(numExp, id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos sobre Material de consumo  por codigo: " + numExp + " - " + id, e);
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
            MeLanbide50DAO meLanbide50DAO = MeLanbide50DAO.getInstance();
            listaCertificados = meLanbide50DAO.getCertificados(con);
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

    public List<String> getMotDenegSelec(String idEspecialidad, Connection con) throws Exception {
        try {
            MeLanbide50DAO meLanbide50DAO = MeLanbide50DAO.getInstance();
            return meLanbide50DAO.getDatosEspecialidadMotDeneg(idEspecialidad, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos sobre los motivos denegación especialidad", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos sobre los motivos denegación especialidad", ex);
            throw new Exception(ex);
        }
    }

    private void realizarInsertSQL(String sql, AdaptadorSQLBD adaptador) throws MeLanbide50Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide50DAO meLanbide50DAO = MeLanbide50DAO.getInstance();

        } catch (BDException e) {
            log.error("Error BD insertando expediente electrónico", e);
            throw new MeLanbide50Exception("Error BD insertando expediente electrónico", e);
        } catch (Exception e) {
            log.error("Error general insertando expediente electrónico", e);
            throw new MeLanbide50Exception("Error general insertando expediente electrónico", e);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public int crearNuevaEspecialidad(EspecialidadesVO especialidad, Connection con) throws Exception {
        try {
            MeLanbide50DAO meLanbide50DAO = MeLanbide50DAO.getInstance();
            return meLanbide50DAO.crearNuevaEspecialidad(especialidad, con);
        } catch (BDException e) {
            log.error("Se ha producido un Excepcion en la BBDD creando  una Especialidad : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD creando Especialidda : " + ex.getMessage(), ex);
            throw new Exception(ex);
        }
    }

    public void crearNuevaEspecialidadMotDeneg(int idEspecialidad, String idMotDeneg, Connection con) throws Exception {
        try {
            MeLanbide50DAO meLanbide50DAO = MeLanbide50DAO.getInstance();
            meLanbide50DAO.crearNuevaEspecialidadMotDeneg(idEspecialidad, idMotDeneg, con);
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
            MeLanbide50DAO meLanbide50DAO = MeLanbide50DAO.getInstance();
            return meLanbide50DAO.modificarEspecialidad(especialidad, con);
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
            MeLanbide50DAO meLanbide50DAO = MeLanbide50DAO.getInstance();
            return meLanbide50DAO.eliminarEspecialidadMotDeneg(idEspecialidad, con);
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
            MeLanbide50DAO meLanbide50DAO = MeLanbide50DAO.getInstance();
            return meLanbide50DAO.eliminarEspecialidad(numExp, id, con);
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
            MeLanbide50DAO meLanbide50DAO = MeLanbide50DAO.getInstance();
            return meLanbide50DAO.crearNuevoServicio(datoNuevo, con);
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
            MeLanbide50DAO meLanbide50DAO = MeLanbide50DAO.getInstance();
            return meLanbide50DAO.modificarServicio(datoNuevo, con);
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
            MeLanbide50DAO meLanbide50DAO = MeLanbide50DAO.getInstance();
            return meLanbide50DAO.eliminarServicio(numExp, id, con);
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
            MeLanbide50DAO meLanbide50DAO = MeLanbide50DAO.getInstance();
            return meLanbide50DAO.modificarDisponibilidad(datoNuevo, con);
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
            MeLanbide50DAO meLanbide50DAO = MeLanbide50DAO.getInstance();
            return meLanbide50DAO.crearNuevaIdentificacioEsp(datoNuevo, con);
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
            MeLanbide50DAO meLanbide50DAO = MeLanbide50DAO.getInstance();
            return meLanbide50DAO.modificarIdentificacionEsp(datoNuevo, con);
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
            MeLanbide50DAO meLanbide50DAO = MeLanbide50DAO.getInstance();
            return meLanbide50DAO.eliminarDisponibilidadDesdeListEspSol(numExp, id, idespsol, con);
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
            MeLanbide50DAO meLanbide50DAO = MeLanbide50DAO.getInstance();
            return meLanbide50DAO.eliminarIdentificacionEspDesdeListEspSol(numExp, id, idespsol, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD eliminando Identificacion de especialidad desde la lista de especialidades solicitadas " + numExp + " - " + id + " - " + idespsol, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD eliminando identificacion de disponibilidad desde la lista de especialidades solicitadas " + numExp + " - " + id + " - " + idespsol, ex);
            throw new Exception(ex);
        }
    }

    public boolean crearNuevaCapacidad(CapacidadVO datoNuevo, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide50DAO meLanbide50DAO = MeLanbide50DAO.getInstance();
            return meLanbide50DAO.crearNuevaCapacidad(datoNuevo, con);
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
            MeLanbide50DAO meLanbide50DAO = MeLanbide50DAO.getInstance();
            return meLanbide50DAO.modificarCapacidad(datoNuevo, con);
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
            MeLanbide50DAO meLanbide50DAO = MeLanbide50DAO.getInstance();
            return meLanbide50DAO.eliminarCapacidad(numExp, id, con);
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
            MeLanbide50DAO meLanbide50DAO = MeLanbide50DAO.getInstance();
            return meLanbide50DAO.crearNuevaDotacion(datoNuevo, con);
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
            MeLanbide50DAO meLanbide50DAO = MeLanbide50DAO.getInstance();
            return meLanbide50DAO.modificarDotacion(datoNuevo, con);
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
            MeLanbide50DAO meLanbide50DAO = MeLanbide50DAO.getInstance();
            return meLanbide50DAO.eliminarDotacion(numExp, id, con);
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
            MeLanbide50DAO meLanbide50DAO = MeLanbide50DAO.getInstance();
            return meLanbide50DAO.crearNuevoMaterial(datoNuevo, con);
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
            MeLanbide50DAO meLanbide50DAO = MeLanbide50DAO.getInstance();
            return meLanbide50DAO.modificarMaterial(datoNuevo, con);
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
            MeLanbide50DAO meLanbide50DAO = MeLanbide50DAO.getInstance();
            return meLanbide50DAO.eliminarMaterial(numExp, id, con);
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

    public boolean crearNuevoEspacio(EspacioVO nuevoDato, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide50DAO meLanbide50DAO = MeLanbide50DAO.getInstance();
            return meLanbide50DAO.crearNuevoEspacio(nuevoDato, con);
        } catch (BDException e) {
            log.error("Se ha producido un Excepcion en la BBDD creando Espacio  : " + e.getMessage(), e);
            throw new Exception(e);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean modificarEspacio(EspacioVO nuevoDato, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide50DAO meLanbide50DAO = MeLanbide50DAO.getInstance();
            return meLanbide50DAO.modificarEspacio(nuevoDato, con);
        } catch (BDException e) {
            log.error("Se ha producido un Excepcion en la BBDD Modificando Espacio : " + e.getMessage(), e);
            throw new Exception(e);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public int eliminarEspacio(String numExp, Integer id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide50DAO meLanbide50DAO = MeLanbide50DAO.getInstance();
            return meLanbide50DAO.eliminarEspacio(numExp, id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD eliminando Espacio " + numExp + " - " + id, e);
            throw new Exception(e);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public List<Tercero> getTercerosExpediente(int codMunicipio, Integer ejercicio, String numExp, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide50DAO meLanbide50DAO = MeLanbide50DAO.getInstance();
            return meLanbide50DAO.getTercerosExpediente(codMunicipio, ejercicio, numExp, con);
        } catch (BDException e) {
            log.error("Se ha producido un error recuperando la lista de terceros para el expediente " + numExp, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando la lista de terceros para el expediente " + numExp, ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean grabarCamposSuplementariosTramite(int codOrganizacion, String codProcedimiento, Integer ejercicio, String numExp, int codTramite, int ocurrenciaTramite, List<CampoSuplementario> camposSuplementarios, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide50DAO meLanbide50DAO = MeLanbide50DAO.getInstance();

            //calculamos de nuevo el codigo del trámite ya que viene a -1
            Long ultTramite = meLanbide50DAO.getCodigoUltimoTramiteAbierto(codOrganizacion, ejercicio.toString(), numExp, /*new Long(ocurrenciaTramite)*/ 1L, con);
            codTramite = ultTramite.intValue();

            log.info("ULTIMO TRAMITE:" + codTramite);
            adaptador.inicioTransaccion(con);
            //boolean result = meLanbide50DAO.grabarCamposSuplementariosTramite(codOrganizacion, codProcedimiento, ejercicio, numExp, codTramite, ocurrenciaTramite, camposSuplementarios, con);
            //boolean result = meLanbide50DAO.grabarCamposSuplementariosTramite(codOrganizacion, codProcedimiento, ejercicio, numExp, Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide50.TRAM_RESOLUCIONINSP, ConstantesMeLanbide50.FICHERO_PROPIEDADES)), 1, camposSuplementarios, con);
            boolean result = meLanbide50DAO.grabarCamposSuplementariosTramite(codOrganizacion, codProcedimiento, ejercicio, numExp, codTramite, ocurrenciaTramite, camposSuplementarios, con);
            if (result == true) {
                adaptador.finTransaccion(con);
            } else {
                adaptador.rollBack(con);
            }
            return result;
        } catch (BDException e) {
            log.error("Se ha producido un error grabando campos suplementarios para tramite " + codTramite + " y expediente " + numExp, e);
            adaptador.rollBack(con);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido un error grabando campos suplementarios para tramite " + codTramite + " y expediente " + numExp, ex);
            adaptador.rollBack(con);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public Expediente getDatosExpediente(int codMunicipio, String numExp, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide50DAO meLanbide50DAO = MeLanbide50DAO.getInstance();
            return meLanbide50DAO.getDatosExpediente(codMunicipio, numExp, con);
        } catch (BDException e) {
            log.error("Se ha producido un error recuperando la lista de terceros para el expediente " + numExp, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando datos del expediente " + numExp, ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public String getNumDocumento(int codOrganizacion, String numExpediente, AdaptadorSQLBD adaptador, int codTramite) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide50DAO meLanbide50DAO = MeLanbide50DAO.getInstance();
            String salida = meLanbide50DAO.getNumDocumento(codTramite, numExpediente, con);
            return salida;
        } catch (BDException e) {
            log.error("Se ha producido un error recuperando la lista de terceros para el expediente " + numExpediente, e);
            throw new Exception(e);
        } catch (MeLanbide50Exception ex) {
            log.error("Se ha producido un error recuperando datos del expediente " + numExpediente, ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public String getErrorEspecialCargarExpedientes50(String numExpediente, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide50DAO meLanbide50DAO = MeLanbide50DAO.getInstance();
            String existeError = meLanbide50DAO.getErrorEspecialCargarExpedientes50(numExpediente, con);
            return existeError;
        } catch (BDException e) {
            log.error("Se ha producido un error recuperando la getErrorEspecialCargarExpedientes50 " + numExpediente, e);
            throw new Exception(e);
        } catch (MeLanbide50Exception ex) {
            log.error("Se ha producido un error recuperando getErrorEspecialCargarExpedientes50 " + numExpediente, ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public List<DocumentosVO> getDatosDocumentos(String numExpediente, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide50DAO meLanbide50DAO = MeLanbide50DAO.getInstance();
            //return  null;
            return meLanbide50DAO.getDatosDocumentos(numExpediente, con);
        } catch (BDException e) {
            log.error("Se ha producido un error recuperando la lista de terceros para el expediente " + numExpediente, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando datos del expediente " + numExpediente, ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public String getDniTercero(int codOrganizacion, String numExpediente, AdaptadorSQLBD adaptador, int codTramite) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide50DAO meLanbide50DAO = MeLanbide50DAO.getInstance();
            return meLanbide50DAO.getDniTercero(numExpediente, con);
        } catch (BDException e) {
            log.error("Se ha producido un error recuperando la lista de terceros para el expediente " + numExpediente, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando datos del expediente " + numExpediente, ex);
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
            return MeLanbide50DAO.getInstance().getCodigosCorr(codCentro, codUbic, con);
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
            return MeLanbide50DAO.getInstance().getNumeroCensoCenFor(codigoCentro, codigoUbicacion, corrSubservicio, con);
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
