/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide35.manager;

import es.altia.flexia.integracion.moduloexterno.melanbide35.dao.MeLanbide35DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.i18n.MeLanbide35I18n;
import es.altia.flexia.integracion.moduloexterno.melanbide35.util.ConstantesMeLanbide35;
import es.altia.flexia.integracion.moduloexterno.melanbide35.util.MeLanbide35Utils;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.comun.Eca23ConfiguracionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.comun.EcaConfiguracionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.comun.SelectItem;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.informes.FilaInformeDesglose;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.informes.FilaInformeProyectos;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.informes.FilaResumenInformeProyectos;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.EcaJusPreparadoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.EcaJusProspectoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.EcaSegPreparadores2016VO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.EcaSegPreparadoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.EcaVisProspectoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.FilaInsPreparadoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.FilaPreparadorJustificacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.FilaProspectorJustificacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.FilaSegPreparadoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.FilaVisProspectoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.JusInsercionesECA23VO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.JusPreparadoresECA23VO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.JusSeguimientosECA23VO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.JustificacionECA23VO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.procesos.FilaAuditoriaProcesosVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.resumen.EcaResDetalleInsercionesVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.resumen.EcaResumenVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.resumen.FilaEcaResPreparadoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.resumen.FilaEcaResProspectoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.solicitud.DatosAnexosVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.solicitud.EcaSolPreparadoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.solicitud.EcaSolProspectoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.solicitud.EcaSolValoracionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.solicitud.EcaSolicitud23VO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.solicitud.EcaSolicitudVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.solicitud.FilaInsercionesECA23VO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.solicitud.FilaPreparadorECA23VO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.solicitud.FilaPreparadorSolicitudVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.solicitud.FilaProspectorECA23VO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.solicitud.FilaProspectorSolicitudVO;
import es.altia.flexia.registro.digitalizacion.lanbide.vo.GeneralComboVO;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author santiagoc
 */
public class MeLanbide35Manager {

    //Logger
    private static final Logger log = LogManager.getLogger(MeLanbide35Manager.class);

    //Instancia
    private static MeLanbide35Manager instance = null;

    private MeLanbide35Manager() {

    }

    public static MeLanbide35Manager getInstance() {
        if (instance == null) {
            synchronized (MeLanbide35Manager.class) {
                instance = new MeLanbide35Manager();
            }
        }
        return instance;
    }

    public EcaSolicitudVO getDatosSolicitud(String numExp, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            return meLanbide35DAO.getDatosSolicitud(numExp, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando solicitud para expediente " + numExp, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando solicitud para expediente " + numExp, ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public EcaSolicitud23VO getDatosSolicitudECA23(String numExp, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            return meLanbide35DAO.getSolicitudECA23(numExp, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando solicitud para expediente " + numExp, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando solicitud para expediente " + numExp, ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public DatosAnexosVO getDatosSolicitudAnexos(EcaSolicitudVO solAsociada, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            return meLanbide35DAO.getDatosSolicitudAnexos(solAsociada, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos anexos para solicitud " + (solAsociada != null ? solAsociada.getSolicitudCod() : "(solAsociada = null)"), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos anexos para solicitud " + (solAsociada != null ? solAsociada.getSolicitudCod() : "(solAsociada = null)"), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public DatosAnexosVO getDatosSolicitudCarga(EcaSolicitudVO solAsociada, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            return meLanbide35DAO.getDatosSolicitudCarga(solAsociada, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos carga para solicitud " + (solAsociada != null ? solAsociada.getSolicitudCod() : "(solAsociada = null)"), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos carga para solicitud " + (solAsociada != null ? solAsociada.getSolicitudCod() : "(solAsociada = null)"), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public EcaSolicitudVO guardarDatosSolicitud(EcaSolicitudVO sol, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            return meLanbide35DAO.guardarDatosSolicitud(sol, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD guardando datos de la solicitud " + (sol != null && sol.getSolicitudCod() != null ? sol.getSolicitudCod().toString() : ""), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD guardando datos de la solicitud " + (sol != null && sol.getSolicitudCod() != null ? sol.getSolicitudCod().toString() : ""), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public EcaSolicitudVO getSolicitudPorCodigo(EcaSolicitudVO sol, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            return meLanbide35DAO.getSolicitudPorCodigo(sol, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando solicitud con id " + (sol != null && sol.getSolicitudCod() != null ? sol.getSolicitudCod().toString() : ""), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando solicitud con id " + (sol != null && sol.getSolicitudCod() != null ? sol.getSolicitudCod().toString() : ""), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public int importarPreparadores(String numExp, List<EcaSolPreparadoresVO> listaPrep, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        int total = 0;
        try {
            EcaSolicitudVO sol = this.getDatosSolicitud(numExp, adaptador);
            if (sol != null && sol.getSolicitudCod() != null) {
                con = adaptador.getConnection();
                adaptador.inicioTransaccion(con);
                MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();

                //Primero elimino todos los registros asociados a la solicitud
                List<EcaJusPreparadoresVO> listJus = meLanbide35DAO.getPreparadoresJustificacion(sol, con);
                if (listJus != null && !listJus.isEmpty()) {
                    for (EcaJusPreparadoresVO p : listJus) {
                        this.eliminarPreparadorJustificacion(p, adaptador);
                    }
                }

                meLanbide35DAO.eliminarTodosPreparadoresSol_Solicitud(sol, con);

                //Guardo la relacion NIF - ID para ir asociando los sustitutos
                Map<String, Integer> mapaIds = new HashMap<String, Integer>();
                Integer id = null;
                //Luego voy haciendo los insert uno a uno
                for (EcaSolPreparadoresVO prep : listaPrep) {
                    prep.setSolicitud(sol.getSolicitudCod());

                    //Miro si es un sustituto. En ese caso busco el ID del sustituido en el hashmap
                    if (prep.getNifPreparadorSustituido() != null && !prep.getNifPreparadorSustituido().isEmpty()) {
                        id = mapaIds.get(prep.getNifPreparadorSustituido());
                        if (id != null) {
                            prep.setSolPreparadorOrigen(id);
                            prep.setTipoSust(ConstantesMeLanbide35.TIPOS_SUSTITUTO.SUSTITUTO_SOLICITUD);
                        }
                    }

                    prep = meLanbide35DAO.guardarEcaSolPreparadoresVO(prep, con);
                    if (prep != null) {
                        total++;
                        if (prep.getSolPreparadoresCod() != null && prep.getNif() != null && !mapaIds.containsKey(prep.getNif())) {
                            mapaIds.put(prep.getNif(), prep.getSolPreparadoresCod());
                        }
                    }
                }


                adaptador.finTransaccion(con);
            }
            return total;
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD importando preparadores", e);
            adaptador.rollBack(con);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD importando preparadores", ex);
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

    public List<FilaPreparadorSolicitudVO> getListaPreparadoresSolicitud(EcaSolicitudVO sol, AdaptadorSQLBD adaptador, int idioma) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            List<FilaPreparadorSolicitudVO> retList = meLanbide35DAO.getListaPreparadoresSolicitud(sol, con);
            EcaConfiguracionVO config = this.getConfiguracionEca(MeLanbide35Utils.getEjercicioDeExpediente(sol.getNumExp()), adaptador);
            EcaSolPreparadoresVO origen = null;
            List<EcaSolPreparadoresVO> sustitutos = null;
            for (FilaPreparadorSolicitudVO fila : retList) {
                origen = null;
                if (fila.getSolPreparadorOrigen() != null) {
                    origen = new EcaSolPreparadoresVO();
                    origen.setSolPreparadoresCod(fila.getSolPreparadorOrigen());
                    origen = meLanbide35DAO.getPreparadorSolicitudPorId(origen, con);
                }

                sustitutos = null;
                try {
                    sustitutos = meLanbide35DAO.getSustitutosPreparadorSolicitud(Integer.valueOf(fila.getId()), con);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                List<String> errores = MeLanbide35Utils.validarFilaPreparadorSolicitud(fila, origen, sustitutos, config, sol.getExpEje(), idioma);
                String parte = null;
                String mensajeCompleto = null;
                if (errores != null && !errores.isEmpty()) {
                    String[] partes = null;
                    String[] codigos = null;
                    List<String> erroresSinCod = new ArrayList<String>();
                    for (String str : errores) {
                        partes = str.split(ConstantesMeLanbide35.BARRA_SEPARADORA);
                        codigos = partes[0].split(ConstantesMeLanbide35.SEPARADOR_VALORES_CONF);
                        for (String codigo : codigos) {
                            fila.setErrorCampo(Integer.parseInt(codigo), ConstantesMeLanbide35.CIERTO);
                            for (int i = 1; i < partes.length; i++) {
                                parte = partes[i];
                                if (mensajeCompleto == null) {
                                    mensajeCompleto = parte;
                                } else {
                                    mensajeCompleto += ConstantesMeLanbide35.BARRA_SEPARADORA + parte;
                                }
                            }
                            if (!erroresSinCod.contains(mensajeCompleto)) {
                                erroresSinCod.add(mensajeCompleto);
                            }
                            mensajeCompleto = null;
                        }
                    }
                    fila.setErrores(erroresSinCod);
                    //MeLanbide35Utils.formatearFilaPreparadorSolicitudVOError(fila);
                }

                if (fila.getTipoSust() != null && fila.getTipoSust().equals(ConstantesMeLanbide35.TIPOS_SUSTITUTO.SUSTITUTO_SOLICITUD)) {
                    if (fila.getNif() != null && !fila.getNif().isEmpty() && !fila.getNif().equals("-")) {
                        fila.setNif(fila.getNif() + " - " + "(" + fila.getTipoSust() + ")");
                    }
                }
            }
            return retList;
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando lista preparadores de solicitud " + (sol != null ? sol.getSolicitudCod() : "(solicitud = null)"), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando lista preparadores de solicitud " + (sol != null ? sol.getSolicitudCod() : "(solicitud = null)"), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
   
    public int eliminarPreparadorSolicitud(EcaSolPreparadoresVO prep, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            adaptador.inicioTransaccion(con);
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            int pos = 0;
            List<Integer> listaE = new ArrayList<Integer>();
            listaE.add(prep.getSolPreparadoresCod());
            buscarPrepElim(listaE, prep, pos, con);
            int result = 0;
            for (Integer i : listaE) {
                prep = new EcaSolPreparadoresVO();
                prep.setSolPreparadoresCod(i);
                prep = meLanbide35DAO.getPreparadorSolicitudPorId(prep, con);
                result += meLanbide35DAO.eliminarPreparadorSolicitud(prep, con);
            }
            adaptador.finTransaccion(con);
            return result;
        } catch (BDException e) {
            adaptador.rollBack(con);
            log.error("Se ha producido una excepcion en la BBDD eliminando preparador solicitud " + (prep != null ? prep.getSolPreparadoresCod() : "(preparador = null)"), e);
            throw new Exception(e);
        } catch (Exception ex) {
            adaptador.rollBack(con);
            log.error("Se ha producido una excepcion en la BBDD eliminando preparador solicitud " + (prep != null ? prep.getSolPreparadoresCod() : "(preparador = null)"), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public EcaSolPreparadoresVO getPreparadorSolicitudPorId(EcaSolPreparadoresVO prep, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            return meLanbide35DAO.getPreparadorSolicitudPorId(prep, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando preparador solicitud " + (prep != null ? prep.getSolPreparadoresCod() : "(preparador = null)"), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando preparador solicitud " + (prep != null ? prep.getSolPreparadoresCod() : "(preparador = null)"), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public EcaSolPreparadoresVO guardarEcaSolPreparadoresVO(EcaSolPreparadoresVO prep, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            return meLanbide35DAO.guardarEcaSolPreparadoresVO(prep, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD guardando preparador solicitud " + (prep != null ? prep.getSolPreparadoresCod() : "(preparador = null)"), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD guardando preparador solicitud " + (prep != null ? prep.getSolPreparadoresCod() : "(preparador = null)"), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public int importarProspectores(String numExp, List<EcaSolProspectoresVO> listaPros, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        int total = 0;
        try {
            EcaSolicitudVO sol = this.getDatosSolicitud(numExp, adaptador);
            if (sol != null && sol.getSolicitudCod() != null) {
                con = adaptador.getConnection();
                adaptador.inicioTransaccion(con);
                MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();

                //Primero elimino todos los registros asociados a la solicitud
                List<EcaJusProspectoresVO> listJus = meLanbide35DAO.getProspectoresJustificacion(sol, con);
                if (listJus != null && !listJus.isEmpty()) {
                    for (EcaJusProspectoresVO p : listJus) {
                        this.eliminarProspectorJustificacion(p, adaptador);
                    }
                }
                meLanbide35DAO.eliminarTodosProspectoresSol_Solicitud(sol, con);

                //Guardo la relacion NIF - ID para ir asociando los sustitutos
                Map<String, Integer> mapaIds = new HashMap<String, Integer>();
                Integer id = null;
                //Luego voy haciendo los insert uno a uno
                for (EcaSolProspectoresVO pros : listaPros) {
                    pros.setSolicitud(sol.getSolicitudCod());

                    //Miro si es un sustituto. En ese caso busco el ID del sustituido en el hashmap
                    if (pros.getNifProspectorSustituido() != null && !pros.getNifProspectorSustituido().isEmpty()) {
                        id = mapaIds.get(pros.getNifProspectorSustituido());
                        if (id != null) {
                            pros.setSolProspectorOrigen(id);
                            pros.setTipoSust(ConstantesMeLanbide35.TIPOS_SUSTITUTO.SUSTITUTO_SOLICITUD);
                        }
                    }

                    pros = meLanbide35DAO.guardarEcaSolProspectoresVO(pros, con);
                    if (pros != null) {
                        total++;
                        if (pros.getSolProspectoresCod() != null && pros.getNif() != null && !mapaIds.containsKey(pros.getNif())) {
                            mapaIds.put(pros.getNif(), pros.getSolProspectoresCod());
                        }
                    }
                }
                adaptador.finTransaccion(con);
            }
            return total;
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD importando prospectores", e);
            adaptador.rollBack(con);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD importando prospectores", ex);
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

    public List<FilaProspectorSolicitudVO> getListaProspectoresSolicitud(EcaSolicitudVO sol, AdaptadorSQLBD adaptador, int idioma) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            List<FilaProspectorSolicitudVO> retList = meLanbide35DAO.getListaProspectoresSolicitud(sol, con);
            EcaConfiguracionVO config = this.getConfiguracionEca(MeLanbide35Utils.getEjercicioDeExpediente(sol.getNumExp()), adaptador);
            EcaSolProspectoresVO origen = null;
            List<EcaSolProspectoresVO> sustitutos = null;
            String mensajeCompleto = null;
            String parte = null;
            for (FilaProspectorSolicitudVO fila : retList) {
                origen = null;
                if (fila.getSolProspectorOrigen() != null) {
                    origen = new EcaSolProspectoresVO();
                    origen.setSolProspectoresCod(fila.getSolProspectorOrigen());
                    origen = meLanbide35DAO.getProspectorSolicitudPorId(origen, con);
                }

                sustitutos = null;
                try {
                    sustitutos = meLanbide35DAO.getSustitutosProspectorSolicitud(Integer.valueOf(fila.getId()), con);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                List<String> errores = MeLanbide35Utils.validarFilaProspectorSolicitud(fila, origen, sustitutos, config, sol.getExpEje(), idioma);
                if (errores != null && !errores.isEmpty()) {
                    String[] partes = null;
                    String[] codigos = null;
                    List<String> erroresSinCod = new ArrayList<String>();
                    for (String str : errores) {
                        partes = str.split(ConstantesMeLanbide35.BARRA_SEPARADORA);
                        codigos = partes[0].split(ConstantesMeLanbide35.SEPARADOR_VALORES_CONF);
                        for (String codigo : codigos) {
                            fila.setErrorCampo(Integer.parseInt(codigo), ConstantesMeLanbide35.CIERTO);
                            for (int i = 1; i < partes.length; i++) {
                                parte = partes[i];
                                if (mensajeCompleto == null) {
                                    mensajeCompleto = parte;
                                } else {
                                    mensajeCompleto += ConstantesMeLanbide35.BARRA_SEPARADORA + parte;
                                }
                            }
                            if (!erroresSinCod.contains(mensajeCompleto)) {
                                erroresSinCod.add(mensajeCompleto);
                            }
                            mensajeCompleto = null;
                        }
                    }
                    fila.setErrores(erroresSinCod);
                    //MeLanbide35Utils.formatearFilaPreparadorSolicitudVOError(fila);
                }

                if (fila.getTipoSust() != null && fila.getTipoSust().equals(ConstantesMeLanbide35.TIPOS_SUSTITUTO.SUSTITUTO_SOLICITUD)) {
                    if (fila.getNif() != null && !fila.getNif().isEmpty() && !fila.getNif().equals("-")) {
                        fila.setNif(fila.getNif() + " - " + "(" + fila.getTipoSust() + ")");
                    }
                }
            }
            return retList;
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando lista prospectores de solicitud " + (sol != null ? sol.getSolicitudCod() : "(solicitud = null)"), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando lista prospectores de solicitud " + (sol != null ? sol.getSolicitudCod() : "(solicitud = null)"), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public EcaSolProspectoresVO getProspectorSolicitudPorId(EcaSolProspectoresVO pros, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            return meLanbide35DAO.getProspectorSolicitudPorId(pros, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando prospector solicitud " + (pros != null ? pros.getSolProspectoresCod() : "(prospector = null)"), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando prospector solicitud " + (pros != null ? pros.getSolProspectoresCod() : "(prospector = null)"), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public EcaSolProspectoresVO guardarEcaSolProspectoresVO(EcaSolProspectoresVO pros, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            return meLanbide35DAO.guardarEcaSolProspectoresVO(pros, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD guardando prospector solicitud " + (pros != null ? pros.getSolProspectoresCod() : "(prospector = null)"), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD guardando prospector solicitud " + (pros != null ? pros.getSolProspectoresCod() : "(prospector = null)"), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    /*public int eliminarProspectorSolicitud(EcaSolProspectoresVO pros, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            return meLanbide35DAO.eliminarProspectorSolicitud(pros, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD eliminando prospector solicitud "+(pros != null ? pros.getSolProspectoresCod() : "(prospector = null)"), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD eliminando prospector solicitud "+(pros != null ? pros.getSolProspectoresCod() : "(prospector = null)"), ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                adaptador.devolverConexion(con);       
            }
            catch(Exception e)
            {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }*/
    public int eliminarProspectorSolicitud(EcaSolProspectoresVO pros, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            adaptador.inicioTransaccion(con);
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            int pos = 0;
            List<Integer> listaE = new ArrayList<Integer>();
            listaE.add(pros.getSolProspectoresCod());
            buscarProsElim(listaE, pros, pos, con);
            int result = 0;
            for (Integer i : listaE) {
                pros = new EcaSolProspectoresVO();
                pros.setSolProspectoresCod(i);
                pros = meLanbide35DAO.getProspectorSolicitudPorId(pros, con);
                result += meLanbide35DAO.eliminarProspectorSolicitud(pros, con);
            }
            adaptador.finTransaccion(con);
            return result;
        } catch (BDException e) {
            adaptador.rollBack(con);
            log.error("Se ha producido una excepcion en la BBDD eliminando prospector solicitud " + (pros != null ? pros.getSolProspectoresCod() : "(prospector = null)"), e);
            throw new Exception(e);
        } catch (Exception ex) {
            adaptador.rollBack(con);
            log.error("Se ha producido una excepcion en la BBDD eliminando prospector solicitud " + (pros != null ? pros.getSolProspectoresCod() : "(prospector = null)"), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public EcaSolValoracionVO getValoracionSolicitud(EcaSolicitudVO sol, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            return meLanbide35DAO.getValoracionSolicitud(sol, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando valoracion de solicitud " + (sol != null ? sol.getSolicitudCod() : "(solicitud = null)"), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando valoracion de solicitud " + (sol != null ? sol.getSolicitudCod() : "(solicitud = null)"), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public EcaSolValoracionVO guardarDatosValoracionSolicitud(EcaSolValoracionVO val, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            return meLanbide35DAO.guardarDatosValoracionSolicitud(val, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD guardando valoracion de la solicitud " + (val != null && val.getSolicitud() != null ? val.getSolicitud().toString() : ""), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD guardando valoracion de la solicitud " + (val != null && val.getSolicitud() != null ? val.getSolicitud().toString() : ""), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public EcaConfiguracionVO getConfiguracionEca(int ano, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            return meLanbide35DAO.getConfiguracionEca(ano, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando la configuracion del a�o " + ano, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando la configuracion del a�o " + ano, ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    //funciones justificacion
    public List<FilaPreparadorJustificacionVO> getListaPreparadoresJustificacion(EcaSolicitudVO sol, AdaptadorSQLBD adaptador, int idioma) throws Exception {
        Connection con = null;
        try {
            /*
            con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            return meLanbide35DAO.getListaPreparadoresJustificacion(sol, con);
             */
            con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            List<FilaPreparadorJustificacionVO> retList = meLanbide35DAO.getListaPreparadoresJustificacion(sol, idioma, con);
            EcaConfiguracionVO config = this.getConfiguracionEca(MeLanbide35Utils.getEjercicioDeExpediente(sol.getNumExp()), adaptador);
            List<Integer> listaTipoContrato = meLanbide35DAO.getListaCodigosTipoContrato(con);

            String mensaje = "";
            MeLanbide35I18n traductor = MeLanbide35I18n.getInstance();
            EcaJusPreparadoresVO origen = null;
            List<EcaJusPreparadoresVO> sustitutos = null;
            for (FilaPreparadorJustificacionVO fila : retList) {
                origen = null;
                if (fila.getJusPreparadorOrigen() != null) {
                    origen = new EcaJusPreparadoresVO();
                    origen.setJusPreparadoresCod(fila.getJusPreparadorOrigen());
                    origen = meLanbide35DAO.getPreparadorJustificacionPorId(origen, con);
                }

                sustitutos = null;
                try {
                    sustitutos = meLanbide35DAO.getSustitutosPreparadorJustificacion(Integer.valueOf(fila.getId()), con);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                List<String> errores = MeLanbide35Utils.validarFilaPreparadorJustificacion(fila, origen, sustitutos, listaTipoContrato, config, sol.getExpEje(), idioma, false);
                EcaJusPreparadoresVO prep = new EcaJusPreparadoresVO();
                prep.setJusPreparadoresCod(Integer.valueOf(fila.getId()));
                prep = meLanbide35DAO.getPreparadorJustificacionPorId(prep, con);
                List<FilaSegPreparadoresVO> listaseg = getListaSeguimientos(sol, prep, ConstantesMeLanbide35.TIPO_SEGUIMIENTO_MELANBIDE35, "", "", idioma, adaptador);
                List<FilaInsPreparadoresVO> listainsc1 = getListaInserciones(sol, prep, ConstantesMeLanbide35.TIPO_INSERCION_MELANBIDE35, "1", "", idioma, adaptador);
                List<FilaInsPreparadoresVO> listainsc2 = getListaInserciones(sol, prep, ConstantesMeLanbide35.TIPO_INSERCION_MELANBIDE35, "2", "", idioma, adaptador);
                List<FilaInsPreparadoresVO> listainsc3 = getListaInserciones(sol, prep, ConstantesMeLanbide35.TIPO_INSERCION_MELANBIDE35, "3", "", idioma, adaptador);
                List<FilaInsPreparadoresVO> listainsc4 = getListaInserciones(sol, prep, ConstantesMeLanbide35.TIPO_INSERCION_MELANBIDE35, "4", "", idioma, adaptador);

                List<FilaInsPreparadoresVO> listains = getListaInserciones(sol, prep, ConstantesMeLanbide35.TIPO_INSERCION_MELANBIDE35, "0", "", idioma, adaptador);

                for (FilaSegPreparadoresVO seg : listaseg) {
                    List<String> erroresseg = seg.getErrores();
                    if (!erroresseg.isEmpty()) {

                        try {
                            fila.setNumSegAnteriores(MeLanbide35Utils.restarSeguimientoPorErrores(fila.getNumSegAnteriores(), "100", false));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                        mensaje = traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.SegErrores");
                        mensaje = FilaPreparadorJustificacionVO.POS_CAMPO_NUM_SEG_ANTERIORES + ConstantesMeLanbide35.BARRA_SEPARADORA + mensaje;
                        if (!errores.contains(mensaje)) {
                            errores.add(mensaje);
                        }
                    }
                    //break;
                }
                DecimalFormatSymbols simbolo = new DecimalFormatSymbols();
                simbolo.setDecimalSeparator(',');
                simbolo.setGroupingSeparator('.');
                DecimalFormat formateador = new DecimalFormat("#,##0.00", simbolo);

                String[] partesnum = fila.getNumSegAnteriores().split("-");
                if (partesnum.length > 0) {
                    BigDecimal numseg = partesnum[1] != null ? new BigDecimal(partesnum[1].trim().replace(".", "").replace(",", ".")) : BigDecimal.ZERO;
                    BigDecimal imp1 = numseg.multiply(config.getImSeguimiento());
                    BigDecimal imp2 = new BigDecimal("0.00");
                    try {
                        imp2 = new BigDecimal(fila.getCostesSalarialesSSEca().replace(".", "").replace(",", ".")).multiply(config.getPoMaxSeguimientos());
                    } catch (Exception ex) {

                    }
                    if (imp1.compareTo(imp2) < 0) {
                        fila.setImporte(formateador.format(imp1));

                    } else {
                        fila.setImporte(formateador.format(imp2));

                    }

                }

                for (FilaInsPreparadoresVO ins : listainsc1) {
                    List<String> erroresseg = ins.getErrores();
                    if (!erroresseg.isEmpty()) {

                        try {
                            if (ins.getSexo() != null) {
                                int sexo = Integer.parseInt(ins.getSexo());
                                if ((sexo == ConstantesMeLanbide35.CODIGOS_SEXO.HOMBRE || sexo == ConstantesMeLanbide35.CODIGOS_SEXO.MUJER)) {
                                    fila.setC1Total(MeLanbide35Utils.restarSeguimientoPorErrores(fila.getC1Total(), ins.getPorcJornada(), true));

                                    switch (sexo) {
                                        case ConstantesMeLanbide35.CODIGOS_SEXO.HOMBRE:
                                            fila.setC1h(MeLanbide35Utils.restarSeguimientoPorErrores(fila.getC1h(), ins.getPorcJornada(), true));
                                        //break;
                                        case ConstantesMeLanbide35.CODIGOS_SEXO.MUJER:
                                            fila.setC1m(MeLanbide35Utils.restarSeguimientoPorErrores(fila.getC1m(), ins.getPorcJornada(), true));
                                        //break;
                                    }
                                }
                            }
                        } catch (NumberFormatException ex) {
                            ex.printStackTrace();
                        }

                        mensaje = traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.C1Errores");
                        mensaje = FilaPreparadorJustificacionVO.POS_CAMPO_C1TOTAL + ConstantesMeLanbide35.BARRA_SEPARADORA + mensaje;
                        if (!errores.contains(mensaje)) {
                            errores.add(mensaje);
                        }
                        //break;
                    }
                }

                for (FilaInsPreparadoresVO ins : listainsc2) {
                    List<String> erroresseg = ins.getErrores();
                    if (!erroresseg.isEmpty()) {

                        try {
                            if (ins.getSexo() != null) {
                                int sexo = Integer.parseInt(ins.getSexo());
                                if ((sexo == ConstantesMeLanbide35.CODIGOS_SEXO.HOMBRE || sexo == ConstantesMeLanbide35.CODIGOS_SEXO.MUJER)) {
                                    fila.setC2Total(MeLanbide35Utils.restarSeguimientoPorErrores(fila.getC2Total(), ins.getPorcJornada(), true));

                                    switch (sexo) {
                                        case ConstantesMeLanbide35.CODIGOS_SEXO.HOMBRE:
                                            fila.setC2h(MeLanbide35Utils.restarSeguimientoPorErrores(fila.getC2h(), ins.getPorcJornada(), true));
                                        //break;
                                        case ConstantesMeLanbide35.CODIGOS_SEXO.MUJER:
                                            fila.setC2m(MeLanbide35Utils.restarSeguimientoPorErrores(fila.getC2m(), ins.getPorcJornada(), true));
                                        //break;
                                    }
                                }
                            }
                        } catch (NumberFormatException ex) {
                            ex.printStackTrace();
                        }

                        mensaje = traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.C2Errores");
                        mensaje = FilaPreparadorJustificacionVO.POS_CAMPO_C2TOTAL + ConstantesMeLanbide35.BARRA_SEPARADORA + mensaje;
                        if (!errores.contains(mensaje)) {
                            errores.add(mensaje);
                        }
                        //break;
                    }
                }

                for (FilaInsPreparadoresVO ins : listainsc3) {
                    List<String> erroresseg = ins.getErrores();
                    if (!erroresseg.isEmpty()) {

                        try {
                            if (ins.getSexo() != null) {
                                int sexo = Integer.parseInt(ins.getSexo());
                                if ((sexo == ConstantesMeLanbide35.CODIGOS_SEXO.HOMBRE || sexo == ConstantesMeLanbide35.CODIGOS_SEXO.MUJER)) {
                                    fila.setC3Total(MeLanbide35Utils.restarSeguimientoPorErrores(fila.getC3Total(), ins.getPorcJornada(), true));

                                    switch (sexo) {
                                        case ConstantesMeLanbide35.CODIGOS_SEXO.HOMBRE:
                                            fila.setC3h(MeLanbide35Utils.restarSeguimientoPorErrores(fila.getC3h(), ins.getPorcJornada(), true));
                                        break;
                                        case ConstantesMeLanbide35.CODIGOS_SEXO.MUJER:
                                            fila.setC3m(MeLanbide35Utils.restarSeguimientoPorErrores(fila.getC3m(), ins.getPorcJornada(), true));
                                        break;
                                    }
                                }
                            }
                        } catch (NumberFormatException ex) {
                            ex.printStackTrace();
                        }

                        mensaje = traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.C3Errores");
                        mensaje = FilaPreparadorJustificacionVO.POS_CAMPO_C3TOTAL + ConstantesMeLanbide35.BARRA_SEPARADORA + mensaje;
                        if (!errores.contains(mensaje)) {
                            errores.add(mensaje);
                        }
                        //break;
                    }
                }

                for (FilaInsPreparadoresVO ins : listainsc4) {
                    List<String> erroresseg = ins.getErrores();
                    if (!erroresseg.isEmpty()) {

                        try {
                            if (ins.getSexo() != null) {
                                int sexo = Integer.parseInt(ins.getSexo());
                                if ((sexo == ConstantesMeLanbide35.CODIGOS_SEXO.HOMBRE || sexo == ConstantesMeLanbide35.CODIGOS_SEXO.MUJER)) {
                                    fila.setC4Total(MeLanbide35Utils.restarSeguimientoPorErrores(fila.getC4Total(), ins.getPorcJornada(), true));

                                    switch (sexo) {
                                        case ConstantesMeLanbide35.CODIGOS_SEXO.HOMBRE:
                                            fila.setC4h(MeLanbide35Utils.restarSeguimientoPorErrores(fila.getC4h(), ins.getPorcJornada(), true));
                                        //break;
                                        case ConstantesMeLanbide35.CODIGOS_SEXO.MUJER:
                                            fila.setC4m(MeLanbide35Utils.restarSeguimientoPorErrores(fila.getC4m(), ins.getPorcJornada(), true));
                                        //break;
                                    }
                                }
                            }
                        } catch (NumberFormatException ex) {
                            ex.printStackTrace();
                        }
                        mensaje = traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.C4Errores");
                        mensaje = FilaPreparadorJustificacionVO.POS_CAMPO_C4TOTAL + ConstantesMeLanbide35.BARRA_SEPARADORA + mensaje;
                        if (!errores.contains(mensaje)) {
                            errores.add(mensaje);
                        }
                        //break;
                    }
                }

                for (FilaInsPreparadoresVO ins : listains) {
                    List<String> erroresseg = ins.getErrores();
                    if (!erroresseg.isEmpty()) {

                        mensaje = traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.NoColectivoErrores");
                        //mensaje = FilaPreparadorJustificacionVO.POS_CAMPO_C1TOTAL+ConstantesMeLanbide35.SEPARADOR_VALORES_CONF+FilaPreparadorJustificacionVO.POS_CAMPO_C2TOTAL+ConstantesMeLanbide35.SEPARADOR_VALORES_CONF+FilaPreparadorJustificacionVO.POS_CAMPO_C3TOTAL+ConstantesMeLanbide35.SEPARADOR_VALORES_CONF+FilaPreparadorJustificacionVO.POS_CAMPO_C4TOTAL+ConstantesMeLanbide35.BARRA_SEPARADORA+mensaje;
                        mensaje = FilaPreparadorJustificacionVO.POS_CAMPO_NIF + ConstantesMeLanbide35.BARRA_SEPARADORA + mensaje;
                        if (!errores.contains(mensaje)) {
                            errores.add(mensaje);
                        }
                        //break;
                    }
                }

                String parte = null;
                String mensajeCompleto = null;
                if (errores != null && !errores.isEmpty()) {
                    String[] partes = null;
                    String[] codigos = null;
                    List<String> erroresSinCod = new ArrayList<String>();
                    for (String str : errores) {
                        if (str != null && !str.isEmpty()) {
                            partes = str.split(ConstantesMeLanbide35.BARRA_SEPARADORA);
                            codigos = partes[0].split(ConstantesMeLanbide35.SEPARADOR_VALORES_CONF);
                            for (String codigo : codigos) {
                                fila.setErrorCampo(Integer.parseInt(codigo), ConstantesMeLanbide35.CIERTO);
                                for (int i = 1; i < partes.length; i++) {
                                    parte = partes[i];
                                    if (mensajeCompleto == null) {
                                        mensajeCompleto = parte;
                                    } else {
                                        mensajeCompleto += ConstantesMeLanbide35.BARRA_SEPARADORA + parte;
                                    }
                                }
                                if (!erroresSinCod.contains(mensajeCompleto)) {
                                    erroresSinCod.add(mensajeCompleto);
                                }
                                mensajeCompleto = null;
                            }
                        }
                    }
                    fila.setErrores(erroresSinCod);
                    //MeLanbide35Utils.formatearFilaPreparadorSolicitudVOError(fila);
                }

                if (fila.esSustituto() != null && fila.esSustituto()) {
                    if (fila.getNif() != null && !fila.getNif().isEmpty() && !fila.getNif().equals("-")) {
                        fila.setNif(fila.getNif() + " - " + "(" + (fila.getTipoSust() != null && !fila.getTipoSust().isEmpty() ? fila.getTipoSust() : "S") + ")");
                    }
                }

                BigDecimal imptseg = fila.getImporte() != null && fila.getImporte() != "" ? new BigDecimal(fila.getImporte().trim().replace(".", "").replace(",", ".")) : BigDecimal.ZERO;
                BigDecimal imptins = fila.getInserciones() != null && fila.getInserciones() != "" ? new BigDecimal(fila.getInserciones().trim().replace(".", "").replace(",", ".")) : BigDecimal.ZERO;
                fila.setSeguimientosInserciones(formateador.format(imptseg.add(imptins)));

            }
            return retList;
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando lista preparadores", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recu�rando lista preparadores", ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public EcaJusPreparadoresVO getPreparadorJustificacionPorId(EcaJusPreparadoresVO prep, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            return meLanbide35DAO.getPreparadorJustificacionPorId(prep, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando preparador solicitud " + (prep != null ? prep.getJusPreparadoresCod() : "(preparador = null)"), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando preparador solicitud " + (prep != null ? prep.getJusPreparadoresCod() : "(preparador = null)"), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public EcaJusPreparadoresVO getPreparadorJustificacionPorNIF(EcaSolicitudVO sol, String nif, Date feInicio, /*String feFin,*/ AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            return meLanbide35DAO.getPreparadorJustificacionPorNIF(sol, nif, feInicio, /*feFin,*/ con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando preparador solicitud nif:" + nif + ", " + (sol != null ? sol.getSolicitudCod() : "(solicitud = null)"), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando preparador solicitud nif:" + nif + ", " + (sol != null ? sol.getSolicitudCod() : "(solicitud = null)"), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public EcaJusPreparadoresVO getPreparadorJustificacionSustituto(Integer idPrep, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            return meLanbide35DAO.getPreparadorJustificacionSustituto(idPrep, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando sustituto preparador:" + idPrep, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando sustituto preparador:" + idPrep, ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public EcaJusProspectoresVO getProspectorJustificacionSustituto(Integer idPros, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            return meLanbide35DAO.getProspectorJustificacionSustituto(idPros, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando sustituto prospector:" + idPros, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando sustituto prospector:" + idPros, ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public EcaJusPreparadoresVO guardarEcaJusPreparadoresVO(EcaJusPreparadoresVO prep, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            EcaJusPreparadoresVO preparador = meLanbide35DAO.guardarEcaJusPreparadoresVO(prep, con);
            //meLanbide35DAO.actualizarSustituto(idPrepOrigen, prep, con);
            return preparador;
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD guardando preparador justificacion " + (prep != null ? prep.getJusPreparadoresCod() : "(preparador = null)"), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD guardando preparador justificacion " + (prep != null ? prep.getJusPreparadoresCod() : "(preparador = null)"), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public int eliminarPreparadorJustificacion(EcaJusPreparadoresVO prep, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            adaptador.inicioTransaccion(con);
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            int pos = 0;
            List<Integer> listaE = new ArrayList<Integer>();
            listaE.add(prep.getJusPreparadoresCod());
            buscarPrepElim(listaE, prep, pos, con);
            int result = 0;
            for (Integer i : listaE) {
                prep = new EcaJusPreparadoresVO();
                prep.setJusPreparadoresCod(i);
                prep = meLanbide35DAO.getPreparadorJustificacionPorId(prep, con);
                meLanbide35DAO.eliminarTodosSeguimientosPreparador(prep, con);
                result += meLanbide35DAO.eliminarPreparadorJustificacion(prep, con);
            }
            adaptador.finTransaccion(con);
            return result;
        } catch (BDException e) {
            adaptador.rollBack(con);
            log.error("Se ha producido una excepcion en la BBDD eliminando preparador justificacion " + (prep != null ? prep.getJusPreparadoresCod() : "(preparador = null)"), e);
            throw new Exception(e);
        } catch (Exception ex) {
            adaptador.rollBack(con);
            log.error("Se ha producido una excepcion en la BBDD eliminando preparador justificacion " + (prep != null ? prep.getJusPreparadoresCod() : "(preparador = null)"), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public int importarSeguimientos(String numExp, List<EcaSegPreparadoresVO> listaSeg, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        int total = 0;
        try {
            EcaSolicitudVO sol = this.getDatosSolicitud(numExp, adaptador);
            if (sol != null && sol.getSolicitudCod() != null) {
                con = adaptador.getConnection();
                adaptador.inicioTransaccion(con);
                MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();

                //Primero elimino todos los registros asociados a la solicitud
                meLanbide35DAO.eliminarTodosSeguimientosPrep_Solicitud(sol, ConstantesMeLanbide35.TIPO_SEGUIMIENTO_MELANBIDE35, con);

                //Luego voy haciendo los insert uno a uno                
                for (EcaSegPreparadoresVO seg : listaSeg) {
                    //seg.setSolicitud(sol.getSolicitudCod());
                    //guardar preparador -buscar para esta solicitud por nif de preparador
                    seg.setTipo(0);
                    if (meLanbide35DAO.guardarEcaSegPreparadoresVO(seg, con) != null) {
                        total++;
                    }
                }
                adaptador.finTransaccion(con);
            }
            return total;
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD importando preparadores", e);
            adaptador.rollBack(con);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD importando preparadores", ex);
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

    public int importarSeguimientos2016(String numExp, List<EcaSegPreparadores2016VO> listaSeg, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        int total = 0;
        try {
            EcaSolicitudVO sol = this.getDatosSolicitud(numExp, adaptador);
            if (sol != null && sol.getSolicitudCod() != null) {
                con = adaptador.getConnection();
                adaptador.inicioTransaccion(con);
                MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();

                //Primero elimino todos los registros asociados a la solicitud
                meLanbide35DAO.eliminarTodosSeguimientosPrep_Solicitud(sol, ConstantesMeLanbide35.TIPO_SEGUIMIENTO_MELANBIDE35, con);

                //Luego voy haciendo los insert uno a uno                
                for (EcaSegPreparadores2016VO seg : listaSeg) {
                    //seg.setSolicitud(sol.getSolicitudCod());
                    //guardar preparador -buscar para esta solicitud por nif de preparador
                    seg.setTipo(0);
                    if (meLanbide35DAO.guardarEcaSegPreparadoresVO_2016(seg, con) != null) {
                        total++;
                    }
                }
                adaptador.finTransaccion(con);
            }
            return total;
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD importando preparadores", e);
            adaptador.rollBack(con);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD importando preparadores", ex);
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

    public List<FilaSegPreparadoresVO> getListaSeguimientos(EcaSolicitudVO sol, EcaJusPreparadoresVO prep, String tipo, String colectivo, String sexo, Integer idioma, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            /*con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            return meLanbide35DAO.getListaSeguimientos(prep, tipo, colectivo, sexo, idioma, con);
             */
            con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            List<FilaSegPreparadoresVO> retList = meLanbide35DAO.getListaSeguimientos(sol, prep, tipo, colectivo, sexo, idioma, con);
            EcaConfiguracionVO config = this.getConfiguracionEca(MeLanbide35Utils.getEjercicioDeExpediente(sol.getNumExp()), adaptador);

            List<Integer> listaTipoContrato = meLanbide35DAO.getListaCodigosTipoContrato(con);
            List<Integer> listaTipoDiscapacidad = meLanbide35DAO.getListaCodigosTipoDiscapacidad(con);
            List<Integer> listaGravedad = meLanbide35DAO.getListaCodigosGravedad(con);

            for (FilaSegPreparadoresVO fila : retList) {

                List<String> errores = MeLanbide35Utils.validarFilaSeguimientoPrep(sol.getNumExp(), fila, config, idioma, listaTipoDiscapacidad, listaGravedad, listaTipoContrato);

                if (errores != null && !errores.isEmpty()) {
                    String[] partes = null;
                    String[] codigos = null;
                    List<String> erroresSinCod = new ArrayList<String>();
                    for (String str : errores) {
                        partes = str.split(ConstantesMeLanbide35.BARRA_SEPARADORA);
                        codigos = partes[0].split(ConstantesMeLanbide35.SEPARADOR_VALORES_CONF);
                        for (String codigo : codigos) {
                            fila.setErrorCampo(Integer.parseInt(codigo), ConstantesMeLanbide35.CIERTO);
                            erroresSinCod.add(partes[1]);
                        }
                    }
                    fila.setErrores(erroresSinCod);
                    //MeLanbide35Utils.formatearFilaPreparadorSolicitudVOError(fila);
                }
            }
            return retList;
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando lista preparadores de solicitud " + (sol != null ? sol.getSolicitudCod() : "(solicitud = null)"), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando lista preparadores de solicitud " + (sol != null ? sol.getSolicitudCod() : "(solicitud = null)"), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public List<FilaInsPreparadoresVO> getListaInserciones(EcaSolicitudVO sol, EcaJusPreparadoresVO prep, String tipo, String colectivo, String sexo, Integer idioma, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            /*con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            return meLanbide35DAO.getListaSeguimientos(prep, tipo, colectivo, sexo, idioma, con);
             */
            con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            List<FilaInsPreparadoresVO> retList = meLanbide35DAO.getListaInserciones(sol, prep, tipo, colectivo, sexo, idioma, con);
            EcaConfiguracionVO config = this.getConfiguracionEca(MeLanbide35Utils.getEjercicioDeExpediente(sol.getNumExp()), adaptador);

            List<Integer> listaTipoContrato = meLanbide35DAO.getListaCodigosTipoContrato(con);
            List<Integer> listaTipoDiscapacidad = meLanbide35DAO.getListaCodigosTipoDiscapacidad(con);
            List<Integer> listaGravedad = meLanbide35DAO.getListaCodigosGravedad(con);

            for (FilaInsPreparadoresVO fila : retList) {

                List<String> errores = MeLanbide35Utils.validarFilaInsercionPrep(sol.getNumExp(), fila, config, idioma, listaTipoDiscapacidad, listaGravedad, listaTipoContrato);

                if (errores != null && !errores.isEmpty()) {
                    String[] partes = null;
                    String[] codigos = null;
                    List<String> erroresSinCod = new ArrayList<String>();
                    for (String str : errores) {
                        partes = str.split(ConstantesMeLanbide35.BARRA_SEPARADORA);
                        codigos = partes[0].split(ConstantesMeLanbide35.SEPARADOR_VALORES_CONF);
                        for (String codigo : codigos) {
                            fila.setErrorCampo(Integer.parseInt(codigo), ConstantesMeLanbide35.CIERTO);
                            erroresSinCod.add(partes[1]);
                        }
                    }
                    fila.setErrores(erroresSinCod);
                    //MeLanbide35Utils.formatearFilaPreparadorSolicitudVOError(fila);
                }
            }
            return retList;
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando lista preparadores de solicitud " + (sol != null ? sol.getSolicitudCod() : "(solicitud = null)"), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando lista preparadores de solicitud " + (sol != null ? sol.getSolicitudCod() : "(solicitud = null)"), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public List<FilaProspectorJustificacionVO> getListaProspectoresJustificacion(EcaSolicitudVO sol, AdaptadorSQLBD adaptador, int idioma) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            List<FilaProspectorJustificacionVO> retList = meLanbide35DAO.getListaProspectoresJustificacion(sol, idioma, con);
            EcaConfiguracionVO config = this.getConfiguracionEca(MeLanbide35Utils.getEjercicioDeExpediente(sol.getNumExp()), adaptador);
            String mensaje = "";
            MeLanbide35I18n traductor = MeLanbide35I18n.getInstance();
            EcaJusProspectoresVO origen = null;
            List<EcaJusProspectoresVO> sustitutos = null;
            for (FilaProspectorJustificacionVO fila : retList) {
                origen = null;
                if (fila.getJusProspectorOrigen() != null) {
                    origen = new EcaJusProspectoresVO();
                    origen.setJusProspectoresCod(fila.getJusProspectorOrigen());
                    origen = meLanbide35DAO.getProspectorJustificacionPorId(origen, con);
                }

                sustitutos = null;
                try {
                    sustitutos = meLanbide35DAO.getSustitutosProspectorJustificacion(Integer.valueOf(fila.getId()), con);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                List<String> errores = MeLanbide35Utils.validarFilaProspectorJustificacion(fila, origen, sustitutos, config, sol.getExpEje(), idioma);
                EcaJusProspectoresVO pros = new EcaJusProspectoresVO();
                pros.setJusProspectoresCod(Integer.valueOf(fila.getId()));
                pros = meLanbide35DAO.getProspectorJustificacionPorId(pros, con);

                List<FilaVisProspectoresVO> listavis = getListaVisitas(sol, pros, idioma, adaptador);

                for (FilaVisProspectoresVO vis : listavis) {
                    List<String> erroresseg = vis.getErrores();
                    if (!erroresseg.isEmpty()) {

                        try {
                            fila.setVisitas(MeLanbide35Utils.restarSeguimientoPorErrores(fila.getVisitas(), "100", false));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                        mensaje = traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.visErrores");
                        mensaje = FilaProspectorJustificacionVO.POS_CAMPO_VISITAS + ConstantesMeLanbide35.BARRA_SEPARADORA + mensaje;
                        if (!errores.contains(mensaje)) {
                            errores.add(mensaje);
                        }
                    }

                }

                //como hay errores en visitas--> importe de visitas mal calculado-> recalcular
                String[] partesvis = fila.getVisitas().split("-");
                String parte1 = partesvis[1].replaceAll(",", "\\.").trim();
                BigDecimal visitas = new BigDecimal(parte1);
                BigDecimal costepros = null;
                try {
                    costepros = fila.getCostesSalarialesSSEca() != null && !fila.getCostesSalarialesSSEca().isEmpty() && !fila.getCostesSalarialesSSEca().equals("-") ? new BigDecimal(fila.getCostesSalarialesSSEca().replaceAll("\\.", "").replaceAll(",", "\\.")) : new BigDecimal("0.00");
                } catch (Exception ex) {
                    costepros = new BigDecimal("0.00");
                }
                BigDecimal imptvisitas = BigDecimal.ZERO;
                BigDecimal minimo = BigDecimal.ZERO;
                BigDecimal maximo = BigDecimal.ZERO;

                try {
                    minimo = new BigDecimal(config.getMinEmpVisit().toString());
                    maximo = new BigDecimal(config.getMaxEmpVisit().toString());

                    //Hay que recalcular el minimo/maximo
                    if (fila.getHorasContrato() != null && fila.getHorasAnuales() != null) {
                        minimo = minimo.multiply(new BigDecimal(fila.getHorasContrato().replace(".", "").replace(",", ".")));
                        minimo = minimo.divide(new BigDecimal(fila.getHorasAnuales().replace(".", "").replace(",", ".")), 20, RoundingMode.HALF_UP);

                        maximo = maximo.multiply(new BigDecimal(fila.getHorasContrato().replace(".", "").replace(",", ".")));
                        maximo = maximo.divide(new BigDecimal(fila.getHorasAnuales().replace(".", "").replace(",", ".")), 20, RoundingMode.HALF_UP);
                    }
                } catch (Exception ex) {
                    //ex.printStackTrace();
                }

                if (visitas.compareTo(minimo) < 0) {
                    imptvisitas = BigDecimal.ZERO;
                } else if ((visitas.compareTo(maximo) > 0) && ((maximo.multiply(config.getImpVisita())).compareTo(costepros) < 0)) {
                    imptvisitas = maximo.multiply(config.getImpVisita());
                } /*else if ((visitas.multiply(config.getImpVisita())).compareTo(costepros) <  0 )
                {
                    imptvisitas = visitas.multiply(config.getImpVisita());
                }
                else 
                {
                    imptvisitas = costepros;
                }*/ else {
                    //Tras los cambios de sustitutos, no hay que limitar si supera el importe de los gastos salariales
                    imptvisitas = visitas.multiply(config.getImpVisita());
                }

                DecimalFormatSymbols simbolo = new DecimalFormatSymbols();
                simbolo.setDecimalSeparator(',');
                simbolo.setGroupingSeparator('.');
                DecimalFormat formateador = new DecimalFormat("#,##0.00", simbolo);

                fila.setVisitasImp(formateador.format(new BigDecimal(MeLanbide35Utils.redondearDecimalesString(imptvisitas, 2))));

                String parte = null;
                String mensajeCompleto = null;
                if (errores != null && !errores.isEmpty()) {
                    String[] partes = null;
                    String[] codigos = null;
                    List<String> erroresSinCod = new ArrayList<String>();
                    for (String str : errores) {
                        partes = str.split(ConstantesMeLanbide35.BARRA_SEPARADORA);
                        codigos = partes[0].split(ConstantesMeLanbide35.SEPARADOR_VALORES_CONF);
                        for (String codigo : codigos) {
                            fila.setErrorCampo(Integer.parseInt(codigo), ConstantesMeLanbide35.CIERTO);
                            for (int i = 1; i < partes.length; i++) {
                                parte = partes[i];
                                if (mensajeCompleto == null) {
                                    mensajeCompleto = parte;
                                } else {
                                    mensajeCompleto += ConstantesMeLanbide35.BARRA_SEPARADORA + parte;
                                }
                            }
                            if (!erroresSinCod.contains(mensajeCompleto)) {
                                erroresSinCod.add(mensajeCompleto);
                            }
                            mensajeCompleto = null;
                        }
                    }
                    fila.setErrores(erroresSinCod);
                    //MeLanbide35Utils.formatearFilaPreparadorSolicitudVOError(fila);
                }

                if (fila.esSustituto() != null && fila.esSustituto()) {
                    if (fila.getNif() != null && !fila.getNif().isEmpty() && !fila.getNif().equals("-")) {
                        fila.setNif(fila.getNif() + " - " + "(" + (fila.getTipoSust() != null && !fila.getTipoSust().isEmpty() ? fila.getTipoSust() : "S") + ")");
                    }
                }
            }
            return retList;
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando lista preparadores", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recu�rando lista preparadores", ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public List<FilaAuditoriaProcesosVO> filtrarAuditoriaProcesos(String nombre, Date feDesde, Date feHasta, Integer codProc, int codIdioma, AdaptadorSQLBD adaptador) throws Exception {
        List<FilaAuditoriaProcesosVO> retList = new ArrayList<FilaAuditoriaProcesosVO>();
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            retList = meLanbide35DAO.filtrarAuditoriaProcesos(nombre, feDesde, feHasta, codProc, codIdioma, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion recuperando auditoria de procesos", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion recuperando auditoria de procesos", ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexiÃ³n a la BBDD: " + e.getMessage());
            }
        }
        return retList;
    }

    public int importarInserciones(String numExp, List<EcaSegPreparadoresVO> listaIns, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        int total = 0;
        try {
            EcaSolicitudVO sol = this.getDatosSolicitud(numExp, adaptador);
            if (sol != null && sol.getSolicitudCod() != null) {
                con = adaptador.getConnection();
                adaptador.inicioTransaccion(con);
                MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();

                //Primero elimino todos los registros asociados a la solicitud
                meLanbide35DAO.eliminarTodosSeguimientosPrep_Solicitud(sol, ConstantesMeLanbide35.TIPO_INSERCION_MELANBIDE35, con);

                //Luego voy haciendo los insert uno a uno                
                for (EcaSegPreparadoresVO ins : listaIns) {
                    //seg.setSolicitud(sol.getSolicitudCod());
                    //guardar preparador -buscar para esta solicitud por nif de preparador
                    ins.setTipo(Integer.valueOf(ConstantesMeLanbide35.TIPO_INSERCION_MELANBIDE35));
                    if (meLanbide35DAO.guardarEcaSegPreparadoresVO(ins, con) != null) {
                        total++;
                    }
                }
                adaptador.finTransaccion(con);
            }
            return total;
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD importando preparadores", e);
            adaptador.rollBack(con);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD importando preparadores", ex);
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

    public int importarInserciones2016(String numExp, List<EcaSegPreparadores2016VO> listaIns, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        int total = 0;
        try {
            EcaSolicitudVO sol = this.getDatosSolicitud(numExp, adaptador);
            if (sol != null && sol.getSolicitudCod() != null) {
                con = adaptador.getConnection();
                adaptador.inicioTransaccion(con);
                MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();

                //Primero elimino todos los registros asociados a la solicitud
                meLanbide35DAO.eliminarTodosSeguimientosPrep_Solicitud(sol, ConstantesMeLanbide35.TIPO_INSERCION_MELANBIDE35, con);

                //Luego voy haciendo los insert uno a uno                
                for (EcaSegPreparadores2016VO ins : listaIns) {
                    //seg.setSolicitud(sol.getSolicitudCod());
                    //guardar preparador -buscar para esta solicitud por nif de preparador
                    ins.setTipo(Integer.valueOf(ConstantesMeLanbide35.TIPO_INSERCION_MELANBIDE35));
                    if (meLanbide35DAO.guardarEcaSegPreparadoresVO_2016(ins, con) != null) {
                        total++;
                    }
                }
                adaptador.finTransaccion(con);
            }
            return total;
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD importando preparadores", e);
            adaptador.rollBack(con);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD importando preparadores", ex);
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

    public EcaSegPreparadoresVO getSeguimientoPreparadorPorId(EcaSegPreparadoresVO seg, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            return meLanbide35DAO.getSeguimientoPreparadorPorId(seg, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando seguimiento del preparador " + (seg != null ? seg.getJusPreparadoresCod() + '_' + seg.getSegPreparadoresCod() : "(seguimiento = null)"), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando seguimiento del preparador " + (seg != null ? seg.getJusPreparadoresCod() + '_' + seg.getSegPreparadoresCod() : "(seguimiento = null)"), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public List<FilaInformeDesglose> getDatosInformeDesglose(String ejercicio, AdaptadorSQLBD adaptador) throws Exception {
        List<FilaInformeDesglose> retList = new ArrayList<FilaInformeDesglose>();
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            retList = meLanbide35DAO.getDatosInformeDesglose(ejercicio, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion recuperando datos del informe desglose", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion recuperando datos del informe desglose", ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexiÃ³n a la BBDD: " + e.getMessage());
            }
        }
        Collections.sort(retList);
        return retList;
    }

    public List<SelectItem> getListaTipoContrato(Integer idioma, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        List<SelectItem> lista = null;
        MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
        try {
            con = adaptador.getConnection();
            lista = meLanbide35DAO.getListaTipoContrato(idioma, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion recuperando lista tipo de contratos", e);
            throw new Exception(e);
        } catch (Exception e) {
            log.debug("Error al obtener los tipos de contrato." + e.getMessage());
            throw new Exception(e);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        return lista;
    }

    public List<SelectItem> getListaTipoDiscapacidad(Integer idioma, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        List<SelectItem> lista = new ArrayList<SelectItem>();
        MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
        try {
            con = adaptador.getConnection();
            lista = meLanbide35DAO.getListaTipodiscapacidad(idioma, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion recuperando lista tipo de discapacidad", e);
            throw new Exception(e);
        } catch (Exception e) {
            log.debug("Error al obtener los tipos de contrato." + e.getMessage());
            throw new Exception(e);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        return lista;
    }

    public List<SelectItem> getListaGravedad(Integer idioma, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        List<SelectItem> lista = new ArrayList<SelectItem>();
        MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
        try {
            con = adaptador.getConnection();
            lista = meLanbide35DAO.getListaGravedad(idioma, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion recuperando lista gravedad", e);
            throw new Exception(e);
        } catch (Exception e) {
            log.debug("Error al obtener los tipos de gravedad." + e.getMessage());
            throw new Exception(e);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        return lista;
    }

    public EcaSegPreparadoresVO guardarEcaSegPreparadoresVO(EcaSegPreparadoresVO seg, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            return meLanbide35DAO.guardarEcaSegPreparadoresVO(seg, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD guardando seguimiento de preparador  " + (seg != null ? seg.getJusPreparadoresCod() + "_" + seg.getSegPreparadoresCod() : "(seguimiento = null)"), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD guardando seguimiento de preparador " + (seg != null ? seg.getJusPreparadoresCod() + "_" + seg.getSegPreparadoresCod() : "(seguimiento = null)"), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public int eliminarSegPreparador(EcaSegPreparadoresVO seg, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            return meLanbide35DAO.eliminarSegPreparador(seg, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD eliminando seguimiento preparador solicitud " + (seg != null ? seg.getJusPreparadoresCod() + "_" + seg.getSegPreparadoresCod() : "(seguimiento = null)"), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD eliminandos seguimiento preparador solicitud " + (seg != null ? seg.getJusPreparadoresCod() + "_" + seg.getSegPreparadoresCod() : "(seguimiento = null)"), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public EcaJusProspectoresVO getProspectorJustificacionPorId(EcaJusProspectoresVO pros, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            return meLanbide35DAO.getProspectorJustificacionPorId(pros, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando prospector justificacion " + (pros != null ? pros.getJusProspectoresCod() : "(prospector = null)"), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando prospector justificacion " + (pros != null ? pros.getJusProspectoresCod() : "(prospector = null)"), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public EcaJusProspectoresVO guardarEcaJusProspectoresVO(EcaJusProspectoresVO pros, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            return meLanbide35DAO.guardarEcaJusProspectoresVO(pros, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD guardando prospector justificacion " + (pros != null ? pros.getSolProspectoresCod() : "(prospector = null)"), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD guardando prospector justificacion " + (pros != null ? pros.getSolProspectoresCod() : "(prospector = null)"), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public int eliminarProspectorJustificacion(EcaJusProspectoresVO pros, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            adaptador.inicioTransaccion(con);
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            int pos = 0;
            List<Integer> listaE = new ArrayList<Integer>();
            listaE.add(pros.getJusProspectoresCod());
            buscarProsElim(listaE, pros, pos, con);
            int result = 0;
            for (Integer i : listaE) {
                pros = new EcaJusProspectoresVO();
                pros.setJusProspectoresCod(i);
                pros = meLanbide35DAO.getProspectorJustificacionPorId(pros, con);
                meLanbide35DAO.eliminarTodasVisitasProspector(pros, con);
                result += meLanbide35DAO.eliminarProspectorJustificacion(pros, con);
            }
            adaptador.finTransaccion(con);
            return result;
        } catch (BDException e) {
            adaptador.rollBack(con);
            log.error("Se ha producido una excepcion en la BBDD eliminando prospector justificacion " + (pros != null ? pros.getJusProspectoresCod() : "(prospector = null)"), e);
            throw new Exception(e);
        } catch (Exception ex) {
            adaptador.rollBack(con);
            log.error("Se ha producido una excepcion en la BBDD eliminando prospector justificacion " + (pros != null ? pros.getJusProspectoresCod() : "(prospector = null)"), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public List<FilaVisProspectoresVO> getListaVisitas(EcaSolicitudVO sol, EcaJusProspectoresVO prep, Integer idioma, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            List<FilaVisProspectoresVO> retList = meLanbide35DAO.getListaVisitas(sol, prep, idioma, con);
            EcaConfiguracionVO config = this.getConfiguracionEca(MeLanbide35Utils.getEjercicioDeExpediente(sol.getNumExp()), adaptador);

            List<Integer> listaSector = meLanbide35DAO.getListaCodigosSectorActividad(con);
            List<Integer> listaProvincia = meLanbide35DAO.getListaCodigosProvincias(con);
            List<Integer> listaCumple = meLanbide35DAO.getListaCodigosCumple(con);
            List<Integer> listaResultado = meLanbide35DAO.getListaCodigosResultado(con);

            for (FilaVisProspectoresVO fila : retList) {
                List<String> errores = MeLanbide35Utils.validarFilaVisitaPros(sol.getNumExp(), fila, listaSector, listaProvincia, listaCumple, listaResultado, config, idioma);

                //Valido la fecha de visita por si hay que a�adir el error.-
                if (fila.getFecVisita() != null && !fila.getFecVisita().isEmpty() && !fila.getFecVisita().equals("-")) {
                    EcaJusProspectoresVO pros = null;
                    try {
                        SimpleDateFormat formatoDeFecha = new SimpleDateFormat("dd/MM/yyyy");
                        pros = meLanbide35DAO.getProspectorJustificacionPorNIF(sol, fila.getNifProspector(), formatoDeFecha.parse(fila.getFecVisita()), con);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    if (pros == null) {
                        MeLanbide35I18n traductor = MeLanbide35I18n.getInstance();
                        String mensaje = FilaVisProspectoresVO.POS_CAMPO_NIFPROSPECTOR + ConstantesMeLanbide35.BARRA_SEPARADORA + traductor.getMensaje(idioma, "error.NifProspectorNoEncontrado");
                        if (!errores.contains(mensaje)) {
                            errores.add(mensaje);
                        }
                    }
                }

                if (errores != null && !errores.isEmpty()) {
                    String[] partes = null;
                    String[] codigos = null;
                    List<String> erroresSinCod = new ArrayList<String>();
                    for (String str : errores) {
                        partes = str.split(ConstantesMeLanbide35.BARRA_SEPARADORA);
                        codigos = partes[0].split(ConstantesMeLanbide35.SEPARADOR_VALORES_CONF);
                        for (String codigo : codigos) {
                            fila.setErrorCampo(Integer.parseInt(codigo), ConstantesMeLanbide35.CIERTO);
                            erroresSinCod.add(partes[1]);
                        }
                    }
                    fila.setErrores(erroresSinCod);
                }
            }
            return retList;
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando lista preparadores de solicitud " + (sol != null ? sol.getSolicitudCod() : "(solicitud = null)"), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando lista preparadores de solicitud " + (sol != null ? sol.getSolicitudCod() : "(solicitud = null)"), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public int importarVisitas(String numExp, List<EcaVisProspectoresVO> listaVis, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        int total = 0;
        try {
            EcaSolicitudVO sol = this.getDatosSolicitud(numExp, adaptador);
            if (sol != null && sol.getSolicitudCod() != null) {
                con = adaptador.getConnection();
                adaptador.inicioTransaccion(con);
                MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();

                //Primero elimino todos los registros asociados a la solicitud
                meLanbide35DAO.eliminarTodasVisitasPros(sol, con);

                Statement stm = con.createStatement();
                //Luego voy haciendo los insert uno a uno                
                for (EcaVisProspectoresVO vis : listaVis) {
                    //guardar visita              
                    if (meLanbide35DAO.guardarEcaVisProspectoresVO(vis, stm, con) != null) {
                        total++;
                    }
                }
                if (stm != null) {
                    stm.close();
                }
                adaptador.finTransaccion(con);
            }
            return total;
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD importando preparadores", e);
            adaptador.rollBack(con);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD importando preparadores", ex);
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

    public EcaJusProspectoresVO getProspectorJustificacionPorNIF(EcaSolicitudVO sol, String nif, Date fecha, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            return meLanbide35DAO.getProspectorJustificacionPorNIF(sol, nif, fecha, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando prospector solicitud nif:" + nif + ", " + (sol != null ? sol.getSolicitudCod() : "(solicitud = null)"), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando prospector solicitud nif:" + nif + ", " + (sol != null ? sol.getSolicitudCod() : "(solicitud = null)"), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    /*public List<FilaInformeProyectos> getDatosInformeProyectos(String ejercicio, AdaptadorSQLBD adaptador) throws Exception
    {
        List<FilaInformeProyectos> retList = new ArrayList<FilaInformeProyectos>();
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            retList = meLanbide35DAO.getDatosInformeProyectos(ejercicio, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion recuperando datos del informe proyectos", e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion recuperando datos del informe proyectos", ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                adaptador.devolverConexion(con);       
            }
            catch(Exception e)
            {
                log.error("Error al cerrar conexiÃ³n a la BBDD: " + e.getMessage());
            }
        }
        return retList;
    }*/
    public List<FilaResumenInformeProyectos> getDatosResumenInformeProyectos(String ejercicio, AdaptadorSQLBD adaptador) throws Exception {
        List<FilaResumenInformeProyectos> retList = new ArrayList<FilaResumenInformeProyectos>();
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            retList = meLanbide35DAO.getDatosResumenInformeProyectos(ejercicio, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion recuperando datos resumen del informe proyectos", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion recuperando datos resumen del informe proyectos", ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexiÃ³n a la BBDD: " + e.getMessage());
            }
        }
        Collections.sort(retList);
        return retList;
    }

    public List<FilaInformeProyectos> getDatosInformeProyectos(String numExp, AdaptadorSQLBD adaptador) throws Exception {
        List<FilaInformeProyectos> retList = new ArrayList<FilaInformeProyectos>();
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();

            FilaInformeProyectos filaAct = null;
            FilaInformeProyectos filaAux3 = null;
            List<FilaInformeProyectos> listaParte2 = meLanbide35DAO.getDatosInformeProyectosParte2(numExp, con);
            List<FilaInformeProyectos> listaParte3 = meLanbide35DAO.getDatosInformeProyectosParte3(numExp, con);

            int posEnLista = 0;

            for (FilaInformeProyectos fila : listaParte2) {
                filaAct = new FilaInformeProyectos();
                filaAct.setEmpresa(fila.getEmpresa());
                filaAct.setInsSeg(fila.getInsSeg());
                filaAct.setTotalTrabajadores(fila.getTotalTrabajadores());
                filaAct.setH25(fila.getH25());
                filaAct.setH25_54(fila.getH25_54());
                filaAct.setH55(fila.getH55());
                filaAct.setM25(fila.getM25());
                filaAct.setM25_54(fila.getM25_54());
                filaAct.setM55(fila.getM55());

                posEnLista = listaParte3.indexOf(fila);

                if (posEnLista >= 0) {
                    filaAux3 = listaParte3.get(posEnLista);
                    if (filaAux3 != null) {
                        filaAct.setTotalPreparadores(filaAux3.getTotalPreparadores());
                        filaAct.setPrepIndefinido(filaAux3.getPrepIndefinido());
                        filaAct.setPrepTemporal(filaAux3.getPrepTemporal());
                    }
                }
                retList.add(filaAct);
            }
        } catch (BDException e) {
            log.error("Se ha producido una excepcion recuperando datos del informe proyectos", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion recuperando datos del informe proyectos", ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexiÃ³n a la BBDD: " + e.getMessage());
            }
        }
        return retList;
    }

    public EcaVisProspectoresVO getVisitaProspectorPorId(EcaVisProspectoresVO vis, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            return meLanbide35DAO.getVisitaProspectorPorId(vis, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando visita del prospector " + (vis != null ? vis.getJusProspectoresCod() + '_' + vis.getVisProspectoresCod() : "(visita = null)"), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando visita del prospector " + (vis != null ? vis.getJusProspectoresCod() + '_' + vis.getVisProspectoresCod() : "(visita = null)"), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public List<SelectItem> getListaSectorActividad(Integer idioma, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        List<SelectItem> lista = null;
        MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
        try {
            con = adaptador.getConnection();
            lista = meLanbide35DAO.getListaSectorActividad(idioma, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion recuperando lista sectores de actividad", e);
            throw new Exception(e);
        } catch (Exception e) {
            log.debug("Error al obtener los sectores de actividad." + e.getMessage());
            throw new Exception(e);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        return lista;
    }

    public List<SelectItem> getListaProvincias(Integer idioma, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        List<SelectItem> lista = null;
        MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
        try {
            con = adaptador.getConnection();
            lista = meLanbide35DAO.getListaProvincias(idioma, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion recuperando lista provincias", e);
            throw new Exception(e);
        } catch (Exception e) {
            log.debug("Error al obtener las provincias." + e.getMessage());
            throw new Exception(e);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        return lista;
    }

    public List<SelectItem> getListaCumpleLismi(Integer idioma, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        List<SelectItem> lista = null;
        MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
        try {
            con = adaptador.getConnection();
            lista = meLanbide35DAO.getListaCumpleLismi(idioma, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion recuperando lista cumple Lismi", e);
            throw new Exception(e);
        } catch (Exception e) {
            log.debug("Error al obtener lista cumple Lismi." + e.getMessage());
            throw new Exception(e);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        return lista;
    }

    public List<SelectItem> getListaResultadoFinal(Integer idioma, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        List<SelectItem> lista = null;
        MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
        try {
            con = adaptador.getConnection();
            lista = meLanbide35DAO.getListaResultadoFinal(idioma, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion recuperando lista resultado Final", e);
            throw new Exception(e);
        } catch (Exception e) {
            log.debug("Error al obtener lista resultado Final." + e.getMessage());
            throw new Exception(e);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        return lista;
    }

    public EcaVisProspectoresVO guardarEcaVisProspectoresVO(EcaVisProspectoresVO vis, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        Statement stm = null;
        try {
            con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            stm = con.createStatement();
            return meLanbide35DAO.guardarEcaVisProspectoresVO(vis, stm, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD guardando visita de prospector  " + (vis != null ? vis.getJusProspectoresCod() + "_" + vis.getVisProspectoresCod() : "(visita = null)"), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD guardando seguimiento de preparador " + (vis != null ? vis.getJusProspectoresCod() + "_" + vis.getVisProspectoresCod() : "(visita = null)"), ex);
            throw new Exception(ex);
        } finally {
            try {
                if (stm != null) {
                    stm.close();
                }
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            } catch (SQLException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public List<Integer> getListaCodigosProvincia(AdaptadorSQLBD adaptador) throws Exception {

        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            return meLanbide35DAO.getListaCodigosProvincias(con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando codigos provincia", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando codigos provincia", ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public List<Integer> getListaCodigosCumple(AdaptadorSQLBD adaptador) throws Exception {

        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            return meLanbide35DAO.getListaCodigosCumple(con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando codigos cumple", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando codigos cumple", ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public List<Integer> getListaCodigosResultado(AdaptadorSQLBD adaptador) throws Exception {

        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            return meLanbide35DAO.getListaCodigosResultado(con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando codigos resultado", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando codigos resultado", ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public List<Integer> getListaCodigosSectorActividad(AdaptadorSQLBD adaptador) throws Exception {

        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            return meLanbide35DAO.getListaCodigosSectorActividad(con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando codigos codigos sector", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando codigos sector", ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public List<Integer> getListaCodigosTipoContrato(AdaptadorSQLBD adaptador) throws Exception {

        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            return meLanbide35DAO.getListaCodigosTipoContrato(con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando codigos tipo de contrato", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando codigos tipo de contrato", ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public List<Integer> getListaCodigosTipoDiscapacidad(AdaptadorSQLBD adaptador) throws Exception {

        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            return meLanbide35DAO.getListaCodigosTipoDiscapacidad(con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando codigos tipo de discapacidad", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando codigos tipo de discapacidad", ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public List<Integer> getListaCodigosGravedad(AdaptadorSQLBD adaptador) throws Exception {

        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            return meLanbide35DAO.getListaCodigosGravedad(con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando codigos gravedad", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando codigos gravedad", ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public EcaSolPreparadoresVO getPreparadorSolicitudPorNIF(EcaSolicitudVO sol, String nif, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            return meLanbide35DAO.getPreparadorSolicitudPorNIF(sol, nif, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando preparador solicitud nif:" + nif + ", " + (sol != null ? sol.getSolicitudCod() : "(solicitud = null)"), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando preparador solicitud nif:" + nif + ", " + (sol != null ? sol.getSolicitudCod() : "(solicitud = null)"), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public EcaSolProspectoresVO getProspectorSolicitudPorNIF(EcaSolicitudVO sol, String nif, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            return meLanbide35DAO.getProspectorSolicitudPorNIF(sol, nif, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando prospector solicitud nif:" + nif + ", " + (sol != null ? sol.getSolicitudCod() : "(solicitud = null)"), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando prospector solicitud nif:" + nif + ", " + (sol != null ? sol.getSolicitudCod() : "(solicitud = null)"), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public EcaSolProspectoresVO getProspectorSolicitudSustituto(Integer idPros, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            return meLanbide35DAO.getProspectorSolicitudSustituto(idPros, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando sustituto prospector:" + idPros, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando sustituto prospector:" + idPros, ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public EcaSolPreparadoresVO getPreparadorSolicitudSustituto(Integer idPrep, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            return meLanbide35DAO.getPreparadorSolicitudSustituto(idPrep, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando sustituto preparador:" + idPrep, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando sustituto preparador:" + idPrep, ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public int eliminarVisitaProspectorJustificacion(EcaVisProspectoresVO vis, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            return meLanbide35DAO.eliminarVisitaProspectorJustificacion(vis, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD eliminando visita : " + (vis != null ? "codigo vis = " + vis.getVisProspectoresCod() + " codigo pros = " + vis.getJusProspectoresCod() : " (visita = null)"), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD eliminando visita : " + (vis != null ? "codigo vis = " + vis.getVisProspectoresCod() + " codigo pros = " + vis.getJusProspectoresCod() : " (visita = null)"), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public Map<String, Integer> getNumeroSeguimientosInsercionesPreparador(EcaJusPreparadoresVO prep, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            return meLanbide35DAO.getNumeroSeguimientosInsercionesPreparador(prep, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando numero seguimientos preparador : " + (prep != null && prep.getJusPreparadoresCod() != null ? prep.getJusPreparadoresCod() : "null"), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando numero seguimientos preparador : " + (prep != null && prep.getJusPreparadoresCod() != null ? prep.getJusPreparadoresCod() : "null"), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public Integer getNumeroVisitasProspector(EcaJusProspectoresVO pros, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            return meLanbide35DAO.getNumeroVisitasProspector(pros, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando numero visitas prospector : " + (pros != null && pros.getJusProspectoresCod() != null ? pros.getJusProspectoresCod() : "null"), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando numero visitas prospector : " + (pros != null && pros.getJusProspectoresCod() != null ? pros.getJusProspectoresCod() : "null"), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean hayPreparadoresParaCopiar(String numExp, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            EcaSolicitudVO sol = this.getDatosSolicitud(numExp, adaptador);
            if (sol != null && sol.getSolicitudCod() != null) {
                con = adaptador.getConnection();
                MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();

                return meLanbide35DAO.hayPreparadoresParaCopiar(sol, con);
            }
            return false;
        } catch (BDException e) {
            log.error("Se ha producido un error comprobando si hay preparadores para copiar", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido un error comprobando si hay preparadores para copiar", ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public int copiarPreparadores(String numExp, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        int resultado = 0;
        try {
            EcaSolicitudVO sol = this.getDatosSolicitud(numExp, adaptador);
            if (sol != null && sol.getSolicitudCod() != null) {
                con = adaptador.getConnection();
                adaptador.inicioTransaccion(con);
                MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();

                //Primero elimino todos los registros asociados a la solicitud
                meLanbide35DAO.eliminarTodosSeguimientosPrep_Solicitud(sol, "0", con);
                meLanbide35DAO.eliminarTodosSeguimientosPrep_Solicitud(sol, "1", con);
                meLanbide35DAO.eliminarTodosPreparadoresJus(sol, con);
                resultado = meLanbide35DAO.copiarPreparadoresFromSolToJus(sol, con);
                adaptador.finTransaccion(con);
            }
            return resultado;
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD importando preparadores", e);
            adaptador.rollBack(con);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD importando preparadores", ex);
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

    public boolean hayProspectoresParaCopiar(String numExp, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            EcaSolicitudVO sol = this.getDatosSolicitud(numExp, adaptador);
            if (sol != null && sol.getSolicitudCod() != null) {
                con = adaptador.getConnection();
                MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();

                return meLanbide35DAO.hayProspectoresParaCopiar(sol, con);
            }
            return false;
        } catch (BDException e) {
            log.error("Se ha producido un error comprobando si hay prospectores para copiar", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido un error comprobando si hay prospectores para copiar", ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public int copiarProspectores(String numExp, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        int resultado = 0;
        try {
            EcaSolicitudVO sol = this.getDatosSolicitud(numExp, adaptador);
            if (sol != null && sol.getSolicitudCod() != null) {
                con = adaptador.getConnection();
                adaptador.inicioTransaccion(con);
                MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();

                //Primero elimino todos los registros asociados a la solicitud
                meLanbide35DAO.eliminarTodasVisitasPros(sol, con);
                meLanbide35DAO.eliminarTodosProspectoresJus(sol, con);
                resultado = meLanbide35DAO.copiarProspectoresFromSolToJus(sol, con);
                adaptador.finTransaccion(con);
            }
            return resultado;
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD importando preparadores", e);
            adaptador.rollBack(con);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD importando preparadores", ex);
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

    private void buscarPrepElim(List<Integer> listaE, EcaSolPreparadoresVO prep, int pos, Connection con) throws Exception {
        List<EcaSolPreparadoresVO> listaS = MeLanbide35DAO.getInstance().getSustitutosPreparadorSolicitud(prep.getSolPreparadoresCod(), con);
        if (listaS != null && !listaS.isEmpty()) {
            while (pos < listaS.size()) {
                prep = listaS.get(pos);
                if (!listaE.contains(prep.getSolPreparadoresCod())) {
                    listaE.add(prep.getSolPreparadoresCod());
                }
                buscarPrepElim(listaE, prep, pos, con);
                pos++;
            }
        }
    }

    private void buscarProsElim(List<Integer> listaE, EcaSolProspectoresVO pros, int pos, Connection con) throws Exception {
        List<EcaSolProspectoresVO> listaS = MeLanbide35DAO.getInstance().getSustitutosProspectorSolicitud(pros.getSolProspectoresCod(), con);
        if (listaS != null && !listaS.isEmpty()) {
            while (pos < listaS.size()) {
                pros = listaS.get(pos);
                if (!listaE.contains(pros.getSolProspectoresCod())) {
                    listaE.add(pros.getSolProspectoresCod());
                }
                buscarProsElim(listaE, pros, pos, con);
                pos++;
            }
        }
    }

    private void buscarPrepElim(List<Integer> listaE, EcaJusPreparadoresVO prep, int pos, Connection con) throws Exception {
        List<EcaJusPreparadoresVO> listaS = MeLanbide35DAO.getInstance().getSustitutosPreparadorJustificacion(prep.getJusPreparadoresCod(), con);
        if (listaS != null && !listaS.isEmpty()) {
            while (pos < listaS.size()) {
                prep = listaS.get(pos);
                if (!listaE.contains(prep.getJusPreparadoresCod())) {
                    listaE.add(prep.getJusPreparadoresCod());
                }
                buscarPrepElim(listaE, prep, pos, con);
                pos++;
            }
        }
    }

    private void buscarProsElim(List<Integer> listaE, EcaJusProspectoresVO pros, int pos, Connection con) throws Exception {
        List<EcaJusProspectoresVO> listaS = MeLanbide35DAO.getInstance().getSustitutosProspectorJustificacion(pros.getJusProspectoresCod(), con);
        if (listaS != null && !listaS.isEmpty()) {
            while (pos < listaS.size()) {
                pros = listaS.get(pos);
                if (!listaE.contains(pros.getJusProspectoresCod())) {
                    listaE.add(pros.getJusProspectoresCod());
                }
                buscarProsElim(listaE, pros, pos, con);
                pos++;
            }
        }
    }

    public List<FilaEcaResProspectoresVO> getListaDetalleProspectoresResumen(String numExp, int codIdioma, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            DecimalFormatSymbols simbolo = new DecimalFormatSymbols();
            simbolo.setDecimalSeparator(',');
            simbolo.setGroupingSeparator('.');
            DecimalFormat formateador = new DecimalFormat("#,##0.00", simbolo);
            con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            List<FilaEcaResProspectoresVO> retList = new ArrayList<FilaEcaResProspectoresVO>();
            EcaSolicitudVO sol = getDatosSolicitud(numExp, adaptador);
            List<FilaVisProspectoresVO> listaVis = null;
            if (sol != null) {
                EcaConfiguracionVO config = this.getConfiguracionEca(MeLanbide35Utils.getEjercicioDeExpediente(sol.getNumExp()), adaptador);
                retList = meLanbide35DAO.getListaDetalleProspectoresResumen(numExp, con);

                EcaJusProspectoresVO pros = null;
                FilaEcaResProspectoresVO fila = null;

                BigDecimal imptvisitas = BigDecimal.ZERO;
                BigDecimal minimo = BigDecimal.ZERO;
                BigDecimal maximo = BigDecimal.ZERO;
                BigDecimal visitas = BigDecimal.ZERO;

                int num_visitas = 0;
                for (int i = 0; i < retList.size(); i++) {
                    fila = retList.get(i);
                    if (fila.getTipoSust() != null) {
                        fila.setImpPagar(formateador.format(new BigDecimal("0.00")));
                    } else {
                        if (fila.getEcaJusProspectoresCod() != null) {
                            pros = new EcaJusProspectoresVO();
                            pros.setJusProspectoresCod(fila.getEcaJusProspectoresCod().intValue());
                            pros = this.getProspectorJustificacionPorId(pros, adaptador);
                            if (pros != null) {
                                listaVis = getListaVisitas(sol, pros, codIdioma, adaptador);
                                num_visitas = listaVis.size();
                                for (FilaVisProspectoresVO vis : listaVis) {
                                    if ((vis.getErrores() != null) && (!vis.getErrores().isEmpty())) {
                                        //fila.setImpPagar(formateador.format(new BigDecimal( "0.00")));
                                        num_visitas--;
                                    }
                                }

                                visitas = new BigDecimal(num_visitas);

                                try {
                                    minimo = new BigDecimal(config.getMinEmpVisit().toString());
                                    maximo = new BigDecimal(config.getMaxEmpVisit().toString());

                                    //Hay que recalcular el minimo/maximo
                                    /*if(pros.getHorasCont() != null && pros.getHorasJC() != null)
                                {
                                    minimo = minimo.multiply(pros.getHorasCont());
                                    minimo = minimo.divide(pros.getHorasJC(), 20, RoundingMode.HALF_UP);

                                    maximo = maximo.multiply(pros.getHorasCont());
                                    maximo = maximo.divide(pros.getHorasJC(), 20, RoundingMode.HALF_UP);
                                }*/
                                } catch (Exception ex) {
                                    //ex.printStackTrace();
                                }

                                if (visitas.compareTo(minimo) < 0) {
                                    imptvisitas = BigDecimal.ZERO;
                                } else if ((visitas.compareTo(maximo) > 0) && ((maximo.multiply(config.getImpVisita())).compareTo(pros.getImpSSECA()) < 0)) {
                                    imptvisitas = maximo.multiply(config.getImpVisita());
                                } else {
                                    //Tras los cambios de sustitutos, no hay que limitar si supera el importe de los gastos salariales
                                    imptvisitas = visitas.multiply(config.getImpVisita());
                                }

                            }

                            if (pros.getImpSSECA() != null && pros.getImpSSECA().compareTo(imptvisitas) < 0) {
                                imptvisitas = pros.getImpSSECA();
                            }

                            //fila.setImpPagar(formateador.format(imptvisitas));
                        }
                    }

                    num_visitas = 0;
                    imptvisitas = BigDecimal.ZERO;
                    minimo = BigDecimal.ZERO;
                    maximo = BigDecimal.ZERO;
                    visitas = BigDecimal.ZERO;
                }
            }
            return retList;
        } catch (BDException e) {
            log.error(new StringBuilder().append("Se ha producido una excepcion en la BBDD recuperando lista detalle prospectores resumen para expediente ").append(numExp).toString(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error(new StringBuilder().append("Se ha producido una excepcion en la BBDD recuperando lista detalle prospectores resumen para expediente ").append(numExp).toString(), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error(new StringBuilder().append("Error al cerrar conexion a la BBDD: ").append(e.getMessage()).toString());
            }
        }
    }

    public List<FilaEcaResPreparadoresVO> getListaDetallePreparadoresResumen(String numExp, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            return meLanbide35DAO.getListaDetallePreparadoresResumen(numExp, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando lista detalle preparadores resumen para expediente " + numExp, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando lista detalle preparadores resumen para expediente " + numExp, ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public EcaResDetalleInsercionesVO cargarDetalleInsercionesPrep(String ejercicio, EcaJusPreparadoresVO prep, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            return meLanbide35DAO.cargarDetalleInsercionesPrep(ejercicio, prep, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando detalle inserciones resumen para ejercicio " + ejercicio + " y preparador " + prep.getJusPreparadoresCod(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando detalle inserciones resumen para ejercicio " + ejercicio + " y preparador " + prep.getJusPreparadoresCod(), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public String actualizarDatosResumen(String numExp, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        String codigo = null;
        Integer res = 0;
        String mensaje = null;
        try {
            con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();

            try {
                codigo = meLanbide35DAO.obtieneSeguimientosCorrectos(numExp, con);
                if (codigo != null && codigo.equals("0")) {
                    try {
                        codigo = meLanbide35DAO.obtieneVisitasCorrectas(numExp, con);
                        if (codigo != null && codigo.equals("0")) {
                            try {
                                codigo = meLanbide35DAO.cargaResumenECA(numExp, con);
                                if (codigo != null && codigo.equalsIgnoreCase(ConstantesMeLanbide35.OK)) {
                                } else {
                                    res = 3;
                                }
                            } catch (Exception ex) {
                                res = 3;
                                log.error("Se ha producido una excepcion en la BBDD ejecutando proceso CARGA_RESUMEN_ECA para numExpediente " + numExp, ex);
                            }
                        } else {
                            res = 2;
                        }
                    } catch (Exception ex) {
                        res = 2;
                        log.error("Se ha producido una excepcion en la BBDD ejecutando proceso obtiene_visitas_correctas para numExpediente " + numExp, ex);
                    }
                } else {
                    res = 1;
                }
            } catch (Exception ex) {
                res = 1;
                log.error("Se ha producido una excepcion en la BBDD ejecutando proceso obtiene_seguimientos_correctos para numExpediente " + numExp, ex);
            }

            switch (res) {
                case 0:
                    mensaje = "msg.resumen.actualizarDatos.ok";
                    break;
                case 1:
                    mensaje = "msg.resumen.actualizarDatos.error.obtieneSeguimientosCorrectos";
                    break;
                case 2:
                    mensaje = "msg.resumen.actualizarDatos.error.obtieneVisitasCorrectas";
                    break;
                case 3:
                    mensaje = "msg.resumen.actualizarDatos.error.cargaResumenECA";
                    break;
            }
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD actualizando datos resumen para numExpediente " + numExp, e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD actualizando datos resumen para numExpedient " + numExp, ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        //return mensaje;
        return codigo;
    }

    public EcaResumenVO getResumenSolicitud(EcaSolicitudVO solicitud, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return MeLanbide35DAO.getInstance().getResumenSolicitud(solicitud, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos resumen para solicitud " + solicitud, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos resumen para solicitud " + solicitud, ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public EcaResumenVO guardarResumenSolicitud(EcaResumenVO res, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            return meLanbide35DAO.guardarResumenSolicitud(res, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD guardando resumen de la solicitud " + (res != null && res.getSolicitud() != null ? res.getSolicitud().toString() : ""), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD guardando resumen de la solicitud " + (res != null && res.getSolicitud() != null ? res.getSolicitud().toString() : ""), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public List<EcaSolPreparadoresVO> getSustitutosPreparadorSolicitud(Integer codPrep, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            return meLanbide35DAO.getSustitutosPreparadorSolicitud(codPrep, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando sustitutos solicitud para preparador " + codPrep, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando sustitutos solicitud para preparador " + codPrep, ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public List<EcaSolProspectoresVO> getSustitutosProspectorSolicitud(Integer codPrep, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            return meLanbide35DAO.getSustitutosProspectorSolicitud(codPrep, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando sustitutos solicitud para prospector " + codPrep, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando sustitutos solicitud para prospector " + codPrep, ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public List<EcaJusPreparadoresVO> getSustitutosPreparadorJustificacion(Integer codPrep, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            return meLanbide35DAO.getSustitutosPreparadorJustificacion(codPrep, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando sustitutos justificacion para preparador " + codPrep, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando sustitutos justificacion para preparador " + codPrep, ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public List<EcaJusProspectoresVO> getSustitutosProspectorJustificacion(Integer codPrep, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            return meLanbide35DAO.getSustitutosProspectorJustificacion(codPrep, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando sustitutos justificacion para prospector " + codPrep, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando sustitutos justificacion para prospector " + codPrep, ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public List<FilaInsercionesECA23VO> getListaInsercionesSolicitudECA23(EcaSolicitud23VO sol, AdaptadorSQLBD adaptador, int idioma) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            List<FilaInsercionesECA23VO> retList = meLanbide35DAO.getListaInsercionesSolicitudECA23(sol, con);
            FilaInsercionesECA23VO item;
            Integer totalNumPersona = 0;
            BigDecimal totalImporteSolUnAnio = new BigDecimal(0);
            BigDecimal totalImporteSolicitado = new BigDecimal(0);
            int pos = 0;
            if (idioma == 4) {
                pos = 1;
            }
            for (int i = 0; i < retList.size(); i++) {
                totalNumPersona = totalNumPersona + retList.get(i).getNumeroPersonas();
                totalImporteSolUnAnio = totalImporteSolUnAnio.add(retList.get(i).getImporteCalculadoUnAnio());
                totalImporteSolicitado = totalImporteSolicitado.add(retList.get(i).getImporteSolicitado());
            }
            List<SelectItem> listaTipoEdadSexo = getInstance().getListaDesplegable(adaptador, "TPSE", idioma);

            List<SelectItem> listaTipoDiscapacidad = getInstance().getListaDesplegable(adaptador, "TPDE", idioma);
            for (Iterator i$ = retList.iterator(); i$.hasNext();) {
                item = (FilaInsercionesECA23VO) i$.next();
                for (SelectItem edad : listaTipoEdadSexo) {
                    if (String.valueOf(item.getTipoSexoEdad()).equalsIgnoreCase(String.valueOf(edad.getId()))) {
                        item.setTipoSexoEdad(edad.getLabel().split("\\|")[pos]);
                    }
                }
                for (SelectItem discapacidad : listaTipoDiscapacidad) {
                    if (String.valueOf(item.getTipoDiscapacidad()).equalsIgnoreCase(String.valueOf(discapacidad.getId()))) {
                        item.setTipoDiscapacidad(discapacidad.getLabel().split("\\|")[pos]);
                    }
                }
            }

            meLanbide35DAO.updateSolicitudTotalInsercionesECA23(totalNumPersona, totalImporteSolUnAnio, totalImporteSolicitado, sol, con);

            return retList;
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando lista prospectores de solicitud " + (sol != null ? sol.getNumeroExpediente() : "(solicitud = null)"), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando lista prospectores de solicitud " + (sol != null ? sol.getNumeroExpediente() : "(solicitud = null)"), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public List<FilaProspectorECA23VO> getListaProspectoresSolicitudECA23(EcaSolicitud23VO sol, AdaptadorSQLBD adaptador, int idioma) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            List<FilaProspectorECA23VO> retList = meLanbide35DAO.getListaProspectoresSolicitudECA23(sol, con);
            return retList;
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando lista prospectores de solicitud " + (sol != null ? sol.getNumeroExpediente() : "(solicitud = null)"), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando lista prospectores de solicitud " + (sol != null ? sol.getNumeroExpediente() : "(solicitud = null)"), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public List<FilaPreparadorECA23VO> getListaPreparadoresSolicitudECA23(EcaSolicitud23VO sol, AdaptadorSQLBD adaptador, int idioma) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            List<FilaPreparadorECA23VO> retList = meLanbide35DAO.getListaPreparadoresSolicitudECA23(sol, con);
            return retList;
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando lista prospectores de solicitud " + (sol != null ? sol.getNumeroExpediente() : "(solicitud = null)"), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando lista prospectores de solicitud " + (sol != null ? sol.getNumeroExpediente() : "(solicitud = null)"), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public EcaSolicitud23VO getSolicitudECA23(String numeroExpediente, AdaptadorSQLBD adaptador, int idioma) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            EcaSolicitud23VO retList = meLanbide35DAO.getSolicitudECA23(numeroExpediente, con);
            return retList;
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando la solicitud " + (numeroExpediente != null ? numeroExpediente : "(solicitud = null)"), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando la solicitud " + (numeroExpediente != null ? numeroExpediente : "(solicitud = null)"), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public Eca23ConfiguracionVO getImportesECA23(String anio, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            return meLanbide35DAO.getConfiguracionECA23(anio, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando la configuracion " + (anio != null ? anio : "(configuracion = null)"), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando la configuracion " + (anio != null ? anio : "(configuracion = null)"), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public int eliminarInsercionSolicitud23(FilaInsercionesECA23VO inser, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            adaptador.inicioTransaccion(con);
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            int result = 0;

            result += meLanbide35DAO.eliminarInsercionSolicitud23(inser, con);

            adaptador.finTransaccion(con);
            return result;
        } catch (BDException e) {
            adaptador.rollBack(con);
            log.error("Se ha producido una excepcion en la BBDD eliminando preparador solicitud " + (inser != null ? inser.getId() : "(insercion = null)"), e);
            throw new Exception(e);
        } catch (Exception ex) {
            adaptador.rollBack(con);
            log.error("Se ha producido una excepcion en la BBDD eliminando preparador solicitud " + (inser != null ? inser.getId() : "(insercion = null)"), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public int eliminarProspectorSolicitud23(FilaProspectorECA23VO prospec, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            adaptador.inicioTransaccion(con);
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            int result = 0;

            result += meLanbide35DAO.eliminarProspectorSolicitud23(prospec, con);

            adaptador.finTransaccion(con);
            return result;
        } catch (BDException e) {
            adaptador.rollBack(con);
            log.error("Se ha producido una excepcion en la BBDD eliminando preparador solicitud " + (prospec != null ? prospec.getId() : "(insercion = null)"), e);
            throw new Exception(e);
        } catch (Exception ex) {
            adaptador.rollBack(con);
            log.error("Se ha producido una excepcion en la BBDD eliminando preparador solicitud " + (prospec != null ? prospec.getId() : "(insercion = null)"), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public int eliminarPreparadorSolicitud23(FilaPreparadorECA23VO prep, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            adaptador.inicioTransaccion(con);
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            int result = 0;

            result += meLanbide35DAO.eliminarPreparadorSolicitud23(prep, con);

            adaptador.finTransaccion(con);
            return result;
        } catch (BDException e) {
            adaptador.rollBack(con);
            log.error("Se ha producido una excepcion en la BBDD eliminando preparador solicitud " + (prep != null ? prep.getId() : "(insercion = null)"), e);
            throw new Exception(e);
        } catch (Exception ex) {
            adaptador.rollBack(con);
            log.error("Se ha producido una excepcion en la BBDD eliminando preparador solicitud " + (prep != null ? prep.getId() : "(insercion = null)"), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public FilaInsercionesECA23VO guardarEca23SolInsercionVO(FilaInsercionesECA23VO inser, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            return meLanbide35DAO.guardarEca23SolInsercionVO(inser, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD guardando preparador solicitud " + (inser != null ? inser.getNumeroExpediente() : "(insercion = null)"), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD guardando preparador solicitud " + (inser != null ? inser.getNumeroExpediente() : "(insercion = null)"), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public EcaSolicitud23VO guardarEca23SeguimientosVO(EcaSolicitud23VO segui, AdaptadorSQLBD adaptador)
            throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            return meLanbide35DAO.guardarEca23SeguimientosVO(segui, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD guardando seguimiento solicitud " + (segui != null ? segui.getNumeroExpediente() : "(seguimiento = null)"), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD guardando seguimiento solicitud " + (segui != null ? segui.getNumeroExpediente() : "(seguimiento = null)"), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public EcaSolicitud23VO actualizaCuantiaSolic(EcaSolicitud23VO sol, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            return meLanbide35DAO.actualizaCuantiaSolic(sol, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD guardando guardarEca23Importes solicitud " + (sol != null ? sol.getNumeroExpediente() : "(guardarEca23Importes = null)"), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD guardando guardarEca23Importes solicitud " + (sol != null ? sol.getNumeroExpediente() : "(guardarEca23Importes = null)"), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public EcaSolicitud23VO guardarHorasPreparadorSolicitud23(EcaSolicitud23VO segui, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            return meLanbide35DAO.guardarHorasPreparadorSolicitud23(segui, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD guardando horas preparador solicitud " + (segui != null ? segui.getNumeroExpediente() : "( horas de los preparadores = null)"), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD guardando horas preparador solicitud " + (segui != null ? segui.getNumeroExpediente() : "( horas de los preparadores = null)"), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public FilaProspectorECA23VO guardarEca23SolProspectorVO(FilaProspectorECA23VO prospec, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            return meLanbide35DAO.guardarEca23SolProspectorVO(prospec, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD guardando prospector solicitud " + (prospec != null ? prospec.getNumeroExpediente() : "(insercion = null)"), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD guardando prospector solicitud " + (prospec != null ? prospec.getNumeroExpediente() : "(insercion = null)"), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public FilaPreparadorECA23VO guardarEca23SolPreparadorVO(FilaPreparadorECA23VO prep, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
            return meLanbide35DAO.guardarEca23SolPreparadorVO(prep, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD guardando preparador solicitud " + (prep != null ? prep.getNumeroExpediente() : "(insercion = null)"), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD guardando preparador solicitud " + (prep != null ? prep.getNumeroExpediente() : "(insercion = null)"), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public List<SelectItem> getListaDesplegable(AdaptadorSQLBD adaptador, String idLista, Integer idioma) throws Exception {
        Connection con = null;
        //     lista = null;
        MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
        try {
            con = adaptador.getConnection();
            return meLanbide35DAO.getListaDesplegable(con, idLista, idioma);
        } catch (BDException e) {
            log.error("Se ha producido un error recuperando el desplegable", e);
            throw new Exception(e);
        } catch (Exception e) {
            log.error("Se ha producido un error recuperando el desplegable." + e.getMessage());
            throw new Exception(e);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public double getTotalJusInsercionesECA23(String numExp, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return MeLanbide35DAO.getInstance().getTotalJusInsercionesECA23(numExp, con);
        } catch (Exception ex) {
            log.error("Error al obtener el total de INSERCIONES 23." + ex.getMessage());
            throw new Exception(ex);
        } finally {
            adaptador.devolverConexion(con);
        }
    }

    public double getTotalJusSegumientosECA23(String numExp, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return MeLanbide35DAO.getInstance().getTotalJusSeguimientosECA23(numExp, con);
        } catch (Exception ex) {
            log.error("Error al obtener el total de SEGUIMIENTOS 23." + ex.getMessage());
            throw new Exception(ex);
        } finally {
            adaptador.devolverConexion(con);
        }
    }

    public List<JusInsercionesECA23VO> getJustInsercionesECA23(String numExp, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return MeLanbide35DAO.getInstance().getJustInsercionesECA23(numExp, con);
        } catch (Exception ex) {
            log.error("Error al obtener lista INSERCIONES 23." + ex.getMessage());
            throw new Exception(ex);
        } finally {
            adaptador.devolverConexion(con);
        }
    }

    public JusInsercionesECA23VO getJusInsercionECA23(String numExp, String id, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return MeLanbide35DAO.getInstance().getJusInsercionECA23(numExp, id, con);
        } catch (Exception ex) {
            log.error("Error al obtener  INSERCION 23." + ex.getMessage());
            throw new Exception(ex);
        } finally {
            adaptador.devolverConexion(con);
        }
    }

    public List<JusPreparadoresECA23VO> getJusPreparadoresECA23(String numExp, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return MeLanbide35DAO.getInstance().getJusPreparadoresECA23(numExp, con);
        } catch (Exception ex) {
            log.error("Error al obtener lista PREPARADORES 23." + ex.getMessage());
            throw new Exception(ex);
        } finally {
            adaptador.devolverConexion(con);
        }
    }

    public JusPreparadoresECA23VO getJusPreparadorECA23(String numExp, String id, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return MeLanbide35DAO.getInstance().getJusPreparadorECA23(numExp, id, con);
        } catch (Exception ex) {
            log.error("Error al obtener  PREPARADOR 23." + ex.getMessage());
            throw new Exception(ex);
        } finally {
            adaptador.devolverConexion(con);
        }
    }

    public List<JusSeguimientosECA23VO> getJustSeguimientosECA23(String numExp, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return MeLanbide35DAO.getInstance().getJustSeguimientosECA23(numExp, con);
        } catch (Exception ex) {
            log.error("Error al obtener lista SEGUIMIENTOS 23." + ex.getMessage());
            throw new Exception(ex);
        } finally {
            adaptador.devolverConexion(con);
        }
    }

    public List<JusSeguimientosECA23VO> getJustSeguimientosPreparadorECA23(String numExp, String dniPreparador, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return MeLanbide35DAO.getInstance().getJustSeguimientosPreparadorECA23(numExp, dniPreparador, con);
        } catch (Exception ex) {
            log.error("Error al obtener lista SEGUIMIENTOS por PREPARADOR 23." + ex.getMessage());
            throw new Exception(ex);
        } finally {
            adaptador.devolverConexion(con);
        }
    }

    public double getTotalJusSeguimientosPreparadorECA23(String numExp, String dniPreparador, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return MeLanbide35DAO.getInstance().getTotalJusSeguimientosPreparadorECA23(numExp, dniPreparador, con);
        } catch (Exception ex) {
            log.error("Error al obtener el importe total SEGUIMIENTOS por PREPARADOR 23." + ex.getMessage());
            throw new Exception(ex);
        } finally {
            adaptador.devolverConexion(con);
        }
    }

    public int getNumJusSeguimientosPreparadorECA23(String numExp, String dniPreparador, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return MeLanbide35DAO.getInstance().getNumJusSeguimientosPreparadorECA23(numExp, dniPreparador, con);
        } catch (Exception ex) {
            log.error("Error al obtener n�mero de  SEGUIMIENTOS por PREPARADOR 23." + ex.getMessage());
            throw new Exception(ex);
        } finally {
            adaptador.devolverConexion(con);
        }
    }

    public List<JusSeguimientosECA23VO> getValSeguimientosPreparadorECA23(String numExp, String dniPreparador, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return MeLanbide35DAO.getInstance().getValSeguimientosPreparadorECA23(numExp, dniPreparador, con);
        } catch (Exception ex) {
            log.error("Error al obtener lista SEGUIMIENTOS por PREPARADOR 23." + ex.getMessage());
            throw new Exception(ex);
        } finally {
            adaptador.devolverConexion(con);
        }
    }
    public double getTotalValInsercionesECA23(String numExp, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return MeLanbide35DAO.getInstance().getTotalValInsercionesECA23(numExp, con);
        } catch (Exception ex) {
            log.error("Error al obtener el total de INSERCIONES 23." + ex.getMessage());
            throw new Exception(ex);
        } finally {
            adaptador.devolverConexion(con);
        }
    }

    public double getTotalValSegumientosECA23(String numExp, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return MeLanbide35DAO.getInstance().getTotalValSeguimientosECA23(numExp, con);
        } catch (Exception ex) {
            log.error("Error al obtener el total de SEGUIMIENTOS 23." + ex.getMessage());
            throw new Exception(ex);
        } finally {
            adaptador.devolverConexion(con);
        }
    }

    public List<JusInsercionesECA23VO> getValInsercionesECA23(String numExp, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return MeLanbide35DAO.getInstance().getValInsercionesECA23(numExp, con);
        } catch (Exception ex) {
            log.error("Error al obtener lista INSERCIONES 23." + ex.getMessage());
            throw new Exception(ex);
        } finally {
            adaptador.devolverConexion(con);
        }
    }
    public List<JusPreparadoresECA23VO> getValPreparadoresECA23(String numExp, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return MeLanbide35DAO.getInstance().getValPreparadoresECA23(numExp, con);
        } catch (Exception ex) {
            log.error("Error al obtener lista PREPARADORES 23." + ex.getMessage());
            throw new Exception(ex);
        } finally {
            adaptador.devolverConexion(con);
        }
    }
    public void insertValInsercionesECA23(JusInsercionesECA23VO ins, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide35DAO.getInstance().insertValInsercionesECA23(ins, con);
        } catch (Exception ex) {
            log.error("Error al insertar una insersion 23." + ex.getMessage());
            throw new Exception(ex);
        } finally {
            adaptador.devolverConexion(con);
        }
    }
    public int deleteValInsercionECA23(Integer id, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return MeLanbide35DAO.getInstance().deleteValInsercionesECA23(id, con);
        } catch (Exception ex) {
            log.error("Error al eliminar una insercion 23." + ex.getMessage());
            throw new Exception(ex);
        } finally {
            adaptador.devolverConexion(con);
        }
    }
    public boolean updateInsercionValECA23(JusInsercionesECA23VO ins, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return MeLanbide35DAO.getInstance().updateInsercionValECA23(ins, con);
        } catch (Exception ex) {
            log.error("Error al actualizar una insercion 23." + ex.getMessage());
            throw new Exception(ex);
        } finally {
            adaptador.devolverConexion(con);
        }
    }
    public void insertValPreparadoresECA23(JusPreparadoresECA23VO preparador, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide35DAO.getInstance().insertValPreparadoresECA23(preparador, con);
        } catch (Exception ex) {
            log.error("Error al insertar un preparador 23." + ex.getMessage());
            throw new Exception(ex);
        } finally {
            adaptador.devolverConexion(con);
        }
    }
    public int deleteValPreparadorECA23(Integer id, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return MeLanbide35DAO.getInstance().deleteValPreparadorECA23(id, con);
        } catch (Exception ex) {
            log.error("Error al eliminar un preparador 23." + ex.getMessage());
            throw new Exception(ex);
        } finally {
            adaptador.devolverConexion(con);
        }
    }
    public boolean updatePreparadorValECA23(JusPreparadoresECA23VO preparador, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return MeLanbide35DAO.getInstance().updatePreparadorValECA23(preparador, con);
        } catch (Exception ex) {
            log.error("Error al actualizar un preparador 23." + ex.getMessage());
            throw new Exception(ex);
        } finally {
            adaptador.devolverConexion(con);
        }
    }
    public void insertValSeguimientosECA23(JusSeguimientosECA23VO seguimiento, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide35DAO.getInstance().insertValSeguimientosECA23(seguimiento, con);
        } catch (Exception ex) {
            log.error("Error al insertar un seguimiento 23." + ex.getMessage());
            throw new Exception(ex);
        } finally {
            adaptador.devolverConexion(con);
        }
    }
    public int deleteValSeguimientoECA23(Integer id, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return MeLanbide35DAO.getInstance().deleteValSeguimientoECA23(id, con);
        } catch (Exception ex) {
            log.error("Error al eliminar un seguimiento 23." + ex.getMessage());
            throw new Exception(ex);
        } finally {
            adaptador.devolverConexion(con);
        }
    }
    public boolean updateSeguimientoValECA23(JusSeguimientosECA23VO seguimiento, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return MeLanbide35DAO.getInstance().updateSeguimientoValECA23(seguimiento, con);
        } catch (Exception ex) {
            log.error("Error al actualizar un seguimiento 23." + ex.getMessage());
            throw new Exception(ex);
        } finally {
            adaptador.devolverConexion(con);
        }
    }
    public int getNumValSeguimientosPreparadorECA23(String numExp, String dniPreparador, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return MeLanbide35DAO.getInstance().getNumValSeguimientosPreparadorECA23(numExp, dniPreparador, con);
        } catch (Exception ex) {
            log.error("Error al obtener n�mero de  SEGUIMIENTOS por PREPARADOR 23." + ex.getMessage());
            throw new Exception(ex);
        } finally {
            adaptador.devolverConexion(con);
        }
    }

    public double getTotalValSeguimientosPreparadorECA23(String numExp, String dniPreparador, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return MeLanbide35DAO.getInstance().getTotalValSeguimientosPreparadorECA23(numExp, dniPreparador, con);
        } catch (Exception ex) {
            log.error("Error al obtener el importe total SEGUIMIENTOS por PREPARADOR 23." + ex.getMessage());
            throw new Exception(ex);
        } finally {
            adaptador.devolverConexion(con);
        }
    }

    public int copiarDesdeJustificiacionAValidacion(String numExp, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        int resultado = -1;
        try {
            if (numExp != null) {
                con = adaptador.getConnection();
                adaptador.inicioTransaccion(con);
                MeLanbide35DAO meLanbide35DAO = MeLanbide35DAO.getInstance();
                final List<JusInsercionesECA23VO> jusInsercionesECA23VOList = meLanbide35DAO.getJustInsercionesECA23(numExp, con);
                final List<JusSeguimientosECA23VO> jusSeguimientosECA23VOList = meLanbide35DAO.getJustSeguimientosECA23(numExp, con);
                final List<JusPreparadoresECA23VO> jusPreparadoresECA23VOList = meLanbide35DAO.getJusPreparadoresECA23(numExp, con);


                for (final JusInsercionesECA23VO jusInsercionesECA23VO : jusInsercionesECA23VOList) {
                    final JusInsercionesECA23VO valInsercionesECA23= meLanbide35DAO.getValInsercionesECA23ById(jusInsercionesECA23VO.getId(), con);
                    if (valInsercionesECA23 == null) {
                        meLanbide35DAO.insertValInsercionesECA23(jusInsercionesECA23VO, con);
                        if (resultado == -1) {
                            resultado = 0;
                        }
                    }
                }

                for (final JusSeguimientosECA23VO jusSeguimientosECA23VO: jusSeguimientosECA23VOList) {
                    final JusSeguimientosECA23VO valSeguimientosECA23VO = meLanbide35DAO.getValSeguimientosECA23ById(jusSeguimientosECA23VO.getId(), con);
                    if (valSeguimientosECA23VO == null) {
                        meLanbide35DAO.insertValSeguimientosECA23(jusSeguimientosECA23VO, con);
                        if (resultado == -1) {
                            resultado = 0;
                        }
                    }
                }

                for (final JusPreparadoresECA23VO jusPreparadoresECA23VO : jusPreparadoresECA23VOList) {
                    final JusPreparadoresECA23VO valPreparadoresECA23VO = meLanbide35DAO.getValPreparadoresECA23ById(jusPreparadoresECA23VO.getId(), con);
                    if (valPreparadoresECA23VO == null) {
                        meLanbide35DAO.insertValPreparadoresECA23(jusPreparadoresECA23VO, con);
                        if (resultado == -1) {
                            resultado = 0;
                        }
                    }
                }
                adaptador.finTransaccion(con);
            }
            return resultado;
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD importando preparadores", e);
            adaptador.rollBack(con);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD importando preparadores", ex);
            adaptador.rollBack(con);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
                throw new Exception(e);
            }
        }
    }

    public List<GeneralComboVO> getCombo(final String desCod, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return MeLanbide35DAO.getInstance().getValuesCombos(desCod, con);
        } catch (Exception ex) {
            log.error("Error al obtener los valores del combo " + ex.getMessage());
            throw new Exception(ex);
        } finally {
            adaptador.devolverConexion(con);
        }
    }

    public List<GeneralComboVO> getComboExterno(final String nombreVista, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return MeLanbide35DAO.getInstance().getValuesCombosExterno(nombreVista, con);
        } catch (Exception ex) {
            log.error("Error al obtener los valores del combo " + ex.getMessage());
            throw new Exception(ex);
        } finally {
            adaptador.devolverConexion(con);
        }
    }

    public double getSuplementarioImporte(String numExp, String campo, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return MeLanbide35DAO.getInstance().getSuplementarioImporte(numExp, campo, con);
        } catch (Exception ex) {
            log.error("Error al obtener un importe. " + ex.getMessage());
            throw new Exception(ex);
        } finally {
            adaptador.devolverConexion(con);
        }
    }
}
