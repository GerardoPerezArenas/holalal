/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide53.manager;

import com.lanbide.lan6.errores.bean.ErrorBean;
import com.lanbide.lan6.registro.error.RegistroErrores;
import com.lanbide.lan6.retramite.solicitud.Retramitacion;
import com.lanbide.lan6.utilidades.Constantes;
import es.altia.flexia.integracion.moduloexterno.melanbide53.MELANBIDE53;
import es.altia.flexia.integracion.moduloexterno.melanbide53.dao.MeLanbide53DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide53.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide53.util.ConstantesMeLanbide53;
import es.altia.flexia.integracion.moduloexterno.melanbide53.vo.DetalleErrorVO;
import es.altia.flexia.integracion.moduloexterno.melanbide53.vo.DetalleEstadisticaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide53.vo.EstadisticasVO;
import es.altia.flexia.integracion.moduloexterno.melanbide53.vo.EventoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide53.vo.FiltroEstadisticasVO;
import es.altia.flexia.integracion.moduloexterno.melanbide53.vo.RegistroErrorCriteriosFiltroVO;
import es.altia.flexia.integracion.moduloexterno.melanbide53.vo.RegistroErrorVO;
import es.altia.flexia.integracion.moduloexterno.melanbide53.vo.RegistroIdenErrorCriteriosFiltroVO;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import es.lanbide.lan6.adaptadoresPlatea.excepciones.Lan6UtilExcepcion;
import es.lanbide.lan6.excepciones.Lan6GestErrorestExcepcion;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author davidg
 */
public class MeLanbide53Manager {

    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide53Manager.class);
    //Instancia
    private static MeLanbide53Manager instance = null;

    public static MeLanbide53Manager getInstance() {
        if (instance == null) {
            synchronized (MeLanbide53Manager.class) {
                instance = new MeLanbide53Manager();
            }
        }
        return instance;
    }

    public List<RegistroErrorVO> recogeRegistrosError(AdaptadorSQLBD adaptador, Integer numLineaxPag, Integer numPagActual) throws Exception {
        List<RegistroErrorVO> errores = new ArrayList<RegistroErrorVO>();
        Connection con = null;
        try {
            if (adaptador != null) {
                log.error(" adapt es dif de null -- antes de crear conexion" + adaptador.toString());
                con = adaptador.getConnection();
            }

            Integer[] numInicialFinalRegPaginar = calcularRegistroInicialFinalpaginacion(numLineaxPag, numPagActual);
            Integer lineaRegInicial = 0;
            Integer lineaRegistroFinal = 0;
            if (numInicialFinalRegPaginar != null && numInicialFinalRegPaginar.length > 1) {
                lineaRegInicial = numInicialFinalRegPaginar[0];
                lineaRegistroFinal = numInicialFinalRegPaginar[1];
            }
            errores = MeLanbide53DAO.getInstance().recogeRegistroErrores(con, lineaRegInicial, lineaRegistroFinal);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en recogeRegistrosError: " + ex);
            throw ex;
        } finally {
            if (adaptador != null) {
                adaptador.devolverConexion(con);
            }
        }
        return errores;
    }

    public List<DetalleErrorVO> recogeRegistrosIdenError(AdaptadorSQLBD adaptador) throws Exception {
        List<DetalleErrorVO> errores = new ArrayList<DetalleErrorVO>();
        Connection con = null;
        try {
            if (adaptador != null) {
                log.error(" adapt es dif de null -- antes de crear conexion" + adaptador.toString());
                con = adaptador.getConnection();
            }

            errores = MeLanbide53DAO.getInstance().recogeRegistroIdentError(con);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en recogeRegistrosError: " + ex);
            throw ex;
        } finally {
            if (adaptador != null) {
                adaptador.devolverConexion(con);
            }
        }
        return errores;
    }

    public List<RegistroErrorVO> busquedaFiltrandoListaErrores(RegistroErrorCriteriosFiltroVO _criterioBusqueda, AdaptadorSQLBD adapt) throws Exception {
        List<RegistroErrorVO> lista = new ArrayList<RegistroErrorVO>();
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide53DAO meLanbide53DAO = MeLanbide53DAO.getInstance();

            lista = meLanbide53DAO.busquedaFiltrandoListaErrores(_criterioBusqueda, con);
            for (int i = 0; i < lista.size(); i++) {
                String solicitud = lista.get(i).getXmlReglasSolicitud();
                log.debug("\r\n xml solicitud " + i + " ===>  " + solicitud);
            }
            return lista;
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos de lista para gestion de errores", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos de lista para gestion de errores ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error(" Gestion de Errores  - Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public List<DetalleErrorVO> busquedaIdenErrores(RegistroIdenErrorCriteriosFiltroVO _criterioBusqueda, AdaptadorSQLBD adapt) throws Exception {
        List<DetalleErrorVO> lista = new ArrayList<DetalleErrorVO>();
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide53DAO meLanbide53DAO = MeLanbide53DAO.getInstance();
            lista = meLanbide53DAO.busquedaIdentificacionErrores(_criterioBusqueda, con);
            return lista;
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos de lista para gestion de errores", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos de lista para gestion de errores ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error(" Gestion de Errores  - Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public RegistroErrorVO getErrorPorID(String id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide53DAO meLanbide53DAO = MeLanbide53DAO.getInstance();
            return meLanbide53DAO.getErrorPorID(id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos de un  error en la gestion de Errores - Modificar " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos de un  error en la gestion de Errores - Modificar:  " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public DetalleErrorVO getIdErrorPorID(String id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide53DAO meLanbide53DAO = MeLanbide53DAO.getInstance();
            return meLanbide53DAO.getIdErrorPorID(id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos de un  error en la gestion de Errores - Modificar " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos de un  error en la gestion de Errores - Modificar:  " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean getIdErrorBorrado(String id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;

        try {
            con = adapt.getConnection();
            MeLanbide53DAO meLanbide53DAO = MeLanbide53DAO.getInstance();
            return meLanbide53DAO.getErrorABorrar(id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos de un  error en getIdErrorBorrado: " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos de un  error en getIdErrorBorrado:  " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean modificarErrores(RegistroErrorVO datModif, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        boolean modifOK;
        try {
            con = adapt.getConnection();
            MeLanbide53DAO meLanbide53DAO = MeLanbide53DAO.getInstance();
            modifOK = meLanbide53DAO.modificarGestionError(datModif, con);
        } catch (BDException e) {
            log.error("Se ha producido una Excepcion en la BBDD Actualizando  Gestion Errores : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD Actualizando un Gestion Errores : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        return modifOK;
    }

    public boolean modificarIdErrores(DetalleErrorVO datModif, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        boolean modifOK;
        try {
            con = adapt.getConnection();
            MeLanbide53DAO meLanbide53DAO = MeLanbide53DAO.getInstance();
            modifOK = meLanbide53DAO.modificarIdError(datModif, con);
        } catch (BDException e) {
            log.error("Se ha producido una Excepcion en la BBDD Actualizando  Gestion Errores : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD Actualizando un Gestion Errores : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        return modifOK;
    }

    public boolean eliminarIdErrores(String id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        boolean modifOK;
        try {
            con = adapt.getConnection();
            MeLanbide53DAO meLanbide53DAO = MeLanbide53DAO.getInstance();
            modifOK = meLanbide53DAO.eliminarIdError(id, con);
        } catch (BDException e) {
            log.error("Se ha producido una Excepcion en eliminarIdErrores : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en eliminarIdErrores : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        return modifOK;
    }

    public boolean insertarIdErrores(DetalleErrorVO datModif, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        boolean modifOK;
        try {
            con = adapt.getConnection();
            MeLanbide53DAO meLanbide53DAO = MeLanbide53DAO.getInstance();
            modifOK = meLanbide53DAO.insertarIdError(datModif, con);
        } catch (BDException e) {
            log.error("Se ha producido una Excepcion en la BBDD Actualizando  Gestion Errores : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD Actualizando un Gestion Errores : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        return modifOK;
    }

    public DetalleErrorVO getDetalleErrorPorID(String idError, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide53DAO meLanbide53DAO = MeLanbide53DAO.getInstance();
            return meLanbide53DAO.getDetalleErrorPorID(idError, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando detalles de un  error en la gestion de Errores - ver detalle " + idError, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando detalles  de un  error en la gestion de Errores - ver detalle:  " + idError, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    private Integer[] calcularRegistroInicialFinalpaginacion(Integer numLineaxPag, Integer numPagActual) throws Exception {
        Integer[] returnData = new Integer[2];
        try {
            Integer numInicial = 0;
            Integer numFinal = Integer.valueOf(ConfigurationParameter.getParameter(ConstantesMeLanbide53.NUMERO_LINEAS_PAGINA, ConstantesMeLanbide53.FICHERO_PROPIEDADES));
            if (numLineaxPag != null && numPagActual != null) {
                numInicial = (numPagActual * numLineaxPag) - numLineaxPag;
                numFinal = (numPagActual * numLineaxPag);
            }
            returnData[0] = numInicial;
            returnData[1] = numFinal;
        } catch (Exception e) {
            log.error("Error al calcular los registro iniciales y finales para la paginación ", e);
            throw e;
        }
        return returnData;
    }
    
    public int oidEsOK(String oidDocumento, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;

        try {
            con = adapt.getConnection();
            MeLanbide53DAO meLanbide53DAO = MeLanbide53DAO.getInstance();
            return meLanbide53DAO.oidEsOK(oidDocumento, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos del oid de estadísticas: " + oidDocumento, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos del oid de estadísticas:  " + oidDocumento, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public String getRegistroDeOid(String oidDocumento, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;

        try {
            con = adapt.getConnection();
            MeLanbide53DAO meLanbide53DAO = MeLanbide53DAO.getInstance();
            return meLanbide53DAO.getRegistroDeOid(oidDocumento, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos de un oid de documento: " + oidDocumento, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos de un oid de documento:  " + oidDocumento, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public String retramitarSolicitud(String id, AdaptadorSQLBD adapt) throws Exception {
        log.debug("INICIO retramitarSolicitud; idRegError--> " + id);
        String resultado = "";
        ErrorBean errorBean = null;
        try {
            resultado = Retramitacion.retramitaSolicitud(id);
            log.error("--pruebaRetramite; idRegError--> " + id + " --Resultado-->" + resultado);

        } catch (Lan6GestErrorestExcepcion e) {
            Connection con = null;
            try {
                con = adapt.getConnection();
                MeLanbide53DAO meLanbide53DAO = MeLanbide53DAO.getInstance();
                RegistroErrorVO registro = meLanbide53DAO.getErrorPorID(id, con);

                String trazaError = Lan6UtilExcepcion.escribirStackTrace(e);
                log.error("--ERROR; lanzarExcepcion Traza-->" + trazaError);
                errorBean = new ErrorBean();
                errorBean.setMensajeError(e.getMensajeExcepcion());
                errorBean.setMensajeExcepError("Se ha producido un error en la retramitación."/*e.getMensajeExcepcion()*/);
                errorBean.setCausa(e.getCausaExcepcion());
                errorBean.setTraza(trazaError);
                errorBean.setIdProcedimiento(registro.getIdProcedimiento());
                errorBean.setIdError(e.getCodes().get(0).toString());
                errorBean.setIdClave(registro.getClave());
                errorBean.setIdFlexia(ConstantesMeLanbide53.SYS_NO_PROCEDE);
                errorBean.setSistemaOrigen("SYS_GESTION_ERRORES");
                errorBean.setSituacion(MELANBIDE53.class.getName() + ".etramitarSolicitud()");
                errorBean.setErrorLog(Constantes.LOG_LAN6GESTIONERRORES/*.LOG_LAN6TOOLKITUTILIDADES*/);
                errorBean.setObservaciones("");
                log.error("--ERROR; setMensajeError y setMensajeExcepError-->" + e.getMensajeExcepcion());
                log.error("--ERROR; e.getCausaExcepcion()-->" + e.getCausaExcepcion());
                log.error("--ERROR; setTraza-->" + trazaError);
                log.error("--ERROR; setIdError-->" + e.getCodes().get(0).toString());
                RegistroErrores.registroError(errorBean);
                resultado = errorBean.getSituacion() + " " + errorBean.getIdError();
                log.error("Errore display : " + resultado);
            } catch (Exception ex) {
                log.error("Se ha producido una excepcion en la BBDD Actualizando un Gestion Errores : " + ex.getMessage(), ex);
                throw new Exception(ex);
            } finally {
                try {
                    adapt.devolverConexion(con);
                } catch (Exception ex) {
                    log.error("Error al cerrar conexion a la BBDD: " + ex.getMessage());
                }
            }
        }
        log.debug("FIN retramitarSolicitud; idRegError--> " + id);
        return resultado;
    }
//#292920

    public List<EstadisticasVO> getDatosEstadisticas(AdaptadorSQLBD adapt) throws Exception {
        List<EstadisticasVO> lista = new ArrayList<EstadisticasVO>();
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide53DAO meLanbide53DAO = MeLanbide53DAO.getInstance();
            lista = meLanbide53DAO.getDatosEstadisticas(con);
            return lista;
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos Estadisticas ", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos Estadisticas ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public List<EstadisticasVO> getFiltroEstadisticas(FiltroEstadisticasVO filtroEstad, AdaptadorSQLBD adapt) throws Exception {
        List<EstadisticasVO> lista = new ArrayList<EstadisticasVO>();
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide53DAO meLanbide53DAO = MeLanbide53DAO.getInstance();
            lista = meLanbide53DAO.getFiltroEstadisticas(filtroEstad, con);
            return lista;
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando filtro Estadisticas ", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando filtro Estadisticas ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public DetalleEstadisticaVO getDetalleEstadisticaPorID(int idEstadistica, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide53DAO meLanbide53DAO = MeLanbide53DAO.getInstance();
            return meLanbide53DAO.getDetalleEstadisticaPorID(idEstadistica, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando detalles de una estadistica en la gestion de Estadisticas - ver detalle " + idEstadistica, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando detalles de una estadistica en la gestion de Estadisticas - ver detalle: " + idEstadistica, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public DetalleEstadisticaVO getDetalleEstadisticaHistPorID(int idEstadistica, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide53DAO meLanbide53DAO = MeLanbide53DAO.getInstance();
            return meLanbide53DAO.getDetalleEstadisticaHistPorID(idEstadistica, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando detalles de una estadistica histórico en la gestion de Estadisticas - ver detalle " + idEstadistica, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando detalles de una estadistica histórico en la gestion de Estadisticas - ver detalle: " + idEstadistica, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public String retramitaSolicitudEstadisticas(String oid, String id, AdaptadorSQLBD adapt) throws Exception {
        log.debug("INICIO retramitaSolicitudEstadisticas; oidDocumento--> " + oid + " idEstadisticas--> " + id);
        String resultado = "";
        ErrorBean errorBean = null;
        try {
            resultado = Retramitacion.retramitaSolicitudEstadisticas(oid, id);
            
            log.info("--retramitaSolicitudEstadisticas; oidDocumento--> " + oid + " idEstadisticas--> " + id + " --Resultado-->" + resultado);

        } catch (Lan6GestErrorestExcepcion e) {
            Connection con = null;
            try {
                con = adapt.getConnection();
                MeLanbide53DAO meLanbide53DAO = MeLanbide53DAO.getInstance();
                //RegistroErrorVO registro = meLanbide53DAO.getErrorPorID(id, con);
                DetalleEstadisticaVO estad = meLanbide53DAO.getDetalleEstadisticaPorID(Integer.parseInt(id), con);

                String trazaError = Lan6UtilExcepcion.escribirStackTrace(e);
                log.error("--ERROR; lanzarExcepcion Traza-->" + trazaError);
                errorBean = new ErrorBean();
                errorBean.setMensajeError(e.getMensajeExcepcion());
                errorBean.setMensajeExcepError("Se ha producido un error en la retramitación."/*e.getMensajeExcepcion()*/);
                errorBean.setCausa(e.getCausaExcepcion());
                errorBean.setTraza(trazaError);
                //errorBean.setIdProcedimiento(registro.getIdProcedimiento());
                errorBean.setIdProcedimiento(estad.getIdProcedimiento());
                errorBean.setIdError(e.getCodes().get(0).toString());
                //errorBean.setIdClave(registro.getClave());
                errorBean.setIdClave(oid);
                errorBean.setIdFlexia(ConstantesMeLanbide53.SYS_NO_PROCEDE);
                errorBean.setSistemaOrigen("SYS_GESTION_ERRORES");
                errorBean.setSituacion(MELANBIDE53.class.getName() + ".retramitarSolicitud()");
                errorBean.setErrorLog(Constantes.LOG_LAN6GESTIONERRORES/*.LOG_LAN6TOOLKITUTILIDADES*/);
                errorBean.setObservaciones("");
                log.error("--ERROR; setMensajeError y setMensajeExcepError-->" + e.getMensajeExcepcion());
                log.error("--ERROR; e.getCausaExcepcion()-->" + e.getCausaExcepcion());
                log.error("--ERROR; setTraza-->" + trazaError);
                log.error("--ERROR; setIdError-->" + e.getCodes().get(0).toString());
                RegistroErrores.registroError(errorBean);
                resultado = errorBean.getSituacion() + " " + errorBean.getIdError();
                log.error("Errore display : " + resultado);
            } catch (Exception ex) {
                log.error("Se ha producido una excepcion en la BBDD Actualizando un Gestion Errores : " + ex.getMessage(), ex);
                throw new Exception(ex);
            } finally {
                try {
                    adapt.devolverConexion(con);
                } catch (Exception ex) {
                    log.error("Error al cerrar conexion a la BBDD: " + ex.getMessage());
                }
            }
        }
        log.debug("FIN retramitaSolicitudEstadisticas; oidDocumento--> " + oid + " idEstadisticas--> " + id);
        return resultado;
    }
    
}
