/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide44.manager;

import es.altia.flexia.integracion.moduloexterno.melanbide44.dao.MeLanbide44DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.i18n.MeLanbide44I18n;
import es.altia.flexia.integracion.moduloexterno.melanbide44.util.ConstantesMeLanbide44;
import es.altia.flexia.integracion.moduloexterno.melanbide44.util.MeLanbide44Utils;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.comun.EcaConfiguracionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.comun.SelectItem;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.informes.FilaInformeDesglose;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.informes.FilaInformeProyectos;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.informes.FilaResumenInformeProyectos;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.justificacion.EcaJusPreparadoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.justificacion.EcaJusProspectoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.justificacion.EcaSegPreparadores2016VO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.justificacion.EcaSegPreparadoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.justificacion.EcaVisProspectoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.justificacion.EcaVisProspectores2016VO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.justificacion.FilaInsPreparadoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.justificacion.FilaPreparadorJustificacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.justificacion.FilaProspectorJustificacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.justificacion.FilaSegPreparadoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.justificacion.FilaVisProspectoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.procesos.FilaAuditoriaProcesosVO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.resumen.EcaResDetalleInsercionesVO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.resumen.EcaResPreparadoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.resumen.EcaResProspectoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.resumen.EcaResumenVO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.resumen.FilaEcaResPreparadoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.resumen.FilaEcaResProspectoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.solicitud.DatosAnexosVO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.solicitud.EcaSolPreparadoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.solicitud.EcaSolProspectoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.solicitud.EcaSolValoracionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.solicitud.EcaSolicitudVO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.solicitud.FilaPreparadorSolicitudVO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.solicitud.FilaProspectorSolicitudVO;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author santiagoc
 */
public class MeLanbide44Manager 
{
    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide44Manager.class);
    
    //Instancia
    private static MeLanbide44Manager instance = null;
    
    private MeLanbide44Manager()
    {
        
    }
    
    public static MeLanbide44Manager getInstance()
    {
        if(instance == null)
        {
            synchronized(MeLanbide44Manager.class)
            {
                instance = new MeLanbide44Manager();
            }
        }
        return instance;
    }   
    
    public EcaSolicitudVO getDatosSolicitud(String numExp, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
            return meLanbide44DAO.getDatosSolicitud(numExp, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando solicitud para expediente "+numExp, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando solicitud para expediente "+numExp, ex);
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
    } 
    
    public DatosAnexosVO getDatosSolicitudAnexos(EcaSolicitudVO solAsociada, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
            return meLanbide44DAO.getDatosSolicitudAnexos(solAsociada, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos anexos para solicitud "+(solAsociada != null ? solAsociada.getSolicitudCod() : "(solAsociada = null)"), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos anexos para solicitud "+(solAsociada != null ? solAsociada.getSolicitudCod() : "(solAsociada = null)"), ex);
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
    } 
    
    public DatosAnexosVO getDatosSolicitudCarga(EcaSolicitudVO solAsociada, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
            return meLanbide44DAO.getDatosSolicitudCarga(solAsociada, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos carga para solicitud "+(solAsociada != null ? solAsociada.getSolicitudCod() : "(solAsociada = null)"), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos carga para solicitud "+(solAsociada != null ? solAsociada.getSolicitudCod() : "(solAsociada = null)"), ex);
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
    } 
    
    
    public EcaSolicitudVO guardarDatosSolicitud(EcaSolicitudVO sol, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
            return meLanbide44DAO.guardarDatosSolicitud(sol, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD guardando datos de la solicitud "+(sol != null && sol.getSolicitudCod() != null ? sol.getSolicitudCod().toString() : ""), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD guardando datos de la solicitud "+(sol != null && sol.getSolicitudCod() != null ? sol.getSolicitudCod().toString() : ""), ex);
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
    }
    
    public void guardaHistorico(String numExpediente, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
            for(int i=1; i< 8; i++)
                meLanbide44DAO.guardarHistorico(numExpediente, "ECA14_HISTORICO"+i, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD al generar el histórico con número de expediente "+ numExpediente, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD al generar el histórico con número de expediente "+numExpediente, ex);
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
    }
    
    public int importarPreparadores(String numExp, List<EcaSolPreparadoresVO> listaPrep, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        int total = 0;
        try
        {
            EcaSolicitudVO sol = this.getDatosSolicitud(numExp, adaptador);
            if(sol != null && sol.getSolicitudCod() != null)
            {
                con = adaptador.getConnection();
                adaptador.inicioTransaccion(con);
                MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
                
                //Primero elimino todos los registros asociados a la solicitud
                List<EcaJusPreparadoresVO> listJus = meLanbide44DAO.getPreparadoresJustificacion(sol, con);
                if(listJus != null && listJus.size() > 0)
                {
                    for(EcaJusPreparadoresVO p : listJus)
                    {
                        this.eliminarPreparadorJustificacion(p, adaptador);
                    }
                }
                
                meLanbide44DAO.eliminarTodosPreparadoresSol_Solicitud(sol, con);
                
                
                //Guardo la relacion NIF - ID para ir asociando los sustitutos
                Map<String, Integer> mapaIds = new HashMap<String, Integer>();
                Integer id = null;
                //Luego voy haciendo los insert uno a uno
                for(EcaSolPreparadoresVO prep : listaPrep)
                {
                    prep.setSolicitud(sol.getSolicitudCod());
                    
                    //Miro si es un sustituto. En ese caso busco el ID del sustituido en el hashmap
                    if(prep.getNifPreparadorSustituido() != null && !prep.getNifPreparadorSustituido().equals(""))
                    {
                        id = mapaIds.get(prep.getNifPreparadorSustituido());
                        if(id != null)
                        {
                            prep.setSolPreparadorOrigen(id);
                            prep.setTipoSust(ConstantesMeLanbide44.TIPOS_SUSTITUTO.SUSTITUTO_SOLICITUD);
                        }
                    }
                    
                    prep = meLanbide44DAO.guardarEcaSolPreparadoresVO(prep, con);
                    if(prep != null)
                    {
                        total++;
                        if(prep.getSolPreparadoresCod() != null && prep.getNif() != null && !mapaIds.containsKey(prep.getNif()))
                        {
                            mapaIds.put(prep.getNif(), prep.getSolPreparadoresCod());
                        }
                    }
                }
                
                
               /* //Luego voy haciendo los insert uno a uno
                for(EcaSolPreparadoresVO prep : listaPrep)
                {
                    prep.setSolicitud(sol.getSolicitudCod());
                    if(meLanbide44DAO.guardarEcaSolPreparadoresVO(prep, con) != null)
                    {
                        total++;
                    }
                }*/
                adaptador.finTransaccion(con);
            }
            return total;
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD importando preparadores", e);
            adaptador.rollBack(con);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD importando preparadores", ex);
            adaptador.rollBack(con);
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
    }
    
    public List<FilaPreparadorSolicitudVO> getListaPreparadoresSolicitud(EcaSolicitudVO sol, AdaptadorSQLBD adaptador, int idioma) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
            List<FilaPreparadorSolicitudVO> retList =  meLanbide44DAO.getListaPreparadoresSolicitud(sol, con);
            EcaConfiguracionVO config = this.getConfiguracionEca(MeLanbide44Utils.getEjercicioDeExpediente(sol.getNumExp()), adaptador);
            EcaSolPreparadoresVO origen = null;
            List<EcaSolPreparadoresVO> sustitutos = null;
            for(FilaPreparadorSolicitudVO fila : retList)
            {
                origen = null;
                if(fila.getSolPreparadorOrigen() != null)
                {
                    origen = new EcaSolPreparadoresVO();
                    origen.setSolPreparadoresCod(fila.getSolPreparadorOrigen());
                    origen = meLanbide44DAO.getPreparadorSolicitudPorId(origen, con);
                }
                
                sustitutos = null;
                try
                {
                    sustitutos = meLanbide44DAO.getSustitutosPreparadorSolicitud(Integer.parseInt(fila.getId()), con);
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }
                
                List<String> errores = MeLanbide44Utils.validarFilaPreparadorSolicitud(fila, origen, sustitutos, config, sol.getExpEje(), idioma);
                String parte = null;
                String mensajeCompleto = null;
                if(errores != null && errores.size() > 0)
                {
                    String[] partes = null;
                    String[] codigos = null;
                    List<String> erroresSinCod = new ArrayList<String>();
                    for(String str : errores)
                    {
                        partes = str.split(ConstantesMeLanbide44.BARRA_SEPARADORA);
                        codigos = partes[0].split(ConstantesMeLanbide44.SEPARADOR_VALORES_CONF);
                        for(String codigo : codigos)
                        {
                            fila.setErrorCampo(Integer.parseInt(codigo), ConstantesMeLanbide44.CIERTO);
                            for(int i = 1; i < partes.length; i++)
                            {
                                parte = partes[i];
                                if(mensajeCompleto == null)
                                {
                                    mensajeCompleto = new String(parte);
                                }
                                else
                                {
                                    mensajeCompleto += ConstantesMeLanbide44.BARRA_SEPARADORA+parte;
                                }
                            }
                            if(!erroresSinCod.contains(mensajeCompleto))
                            {
                                erroresSinCod.add(mensajeCompleto);
                            }
                            mensajeCompleto = null;
                        }
                    }
                   fila.setErrores(erroresSinCod);
                   //MeLanbide44Utils.formatearFilaPreparadorSolicitudVOError(fila);
                }
                
                if(fila.getTipoSust() != null && fila.getTipoSust().equals(ConstantesMeLanbide44.TIPOS_SUSTITUTO.SUSTITUTO_SOLICITUD))
                {
                    if(fila.getNif() != null && !fila.getNif().equals("") && !fila.getNif().equals("-"))
                    {
                        fila.setNif(fila.getNif() + " - "+"("+fila.getTipoSust()+")");
                    }
                }
            }
            return retList;
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando lista preparadores de solicitud "+(sol != null ? sol.getSolicitudCod() : "(solicitud = null)"), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando lista preparadores de solicitud "+(sol != null ? sol.getSolicitudCod() : "(solicitud = null)"), ex);
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
    }
    
    /*public int eliminarPreparadorSolicitud(EcaSolPreparadoresVO prep, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
            return meLanbide44DAO.eliminarPreparadorSolicitud(prep, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD eliminando preparador solicitud "+(prep != null ? prep.getSolPreparadoresCod() : "(preparador = null)"), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD eliminando preparador solicitud "+(prep != null ? prep.getSolPreparadoresCod() : "(preparador = null)"), ex);
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
    
    public int eliminarPreparadorSolicitud(EcaSolPreparadoresVO prep, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            adaptador.inicioTransaccion(con);
            MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
            int pos = 0;
            List<Integer> listaE = new ArrayList<Integer>();
            listaE.add(prep.getSolPreparadoresCod());
            buscarPrepElim(listaE, prep, pos, con);
            int result = 0;
            for(Integer i : listaE)
            {
                prep = new EcaSolPreparadoresVO();
                prep.setSolPreparadoresCod(i);
                prep = meLanbide44DAO.getPreparadorSolicitudPorId(prep, con);
                result += meLanbide44DAO.eliminarPreparadorSolicitud(prep, con);
            }
            adaptador.finTransaccion(con);
            return result;
        }
        catch(BDException e)
        {
            adaptador.rollBack(con);
            log.error("Se ha producido una excepcion en la BBDD eliminando preparador solicitud "+(prep != null ? prep.getSolPreparadoresCod() : "(preparador = null)"), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            adaptador.rollBack(con);
            log.error("Se ha producido una excepcion en la BBDD eliminando preparador solicitud "+(prep != null ? prep.getSolPreparadoresCod() : "(preparador = null)"), ex);
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
    }
    
    public EcaSolPreparadoresVO getPreparadorSolicitudPorId(EcaSolPreparadoresVO prep, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
            return meLanbide44DAO.getPreparadorSolicitudPorId(prep, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando preparador solicitud "+(prep != null ? prep.getSolPreparadoresCod() : "(preparador = null)"), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando preparador solicitud "+(prep != null ? prep.getSolPreparadoresCod() : "(preparador = null)"), ex);
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
    }
    
   
    public EcaSolPreparadoresVO guardarEcaSolPreparadoresVO(EcaSolPreparadoresVO prep, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
            return meLanbide44DAO.guardarEcaSolPreparadoresVO(prep, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD guardando preparador solicitud "+(prep != null ? prep.getSolPreparadoresCod() : "(preparador = null)"), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD guardando preparador solicitud "+(prep != null ? prep.getSolPreparadoresCod() : "(preparador = null)"), ex);
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
    }
    
    public int importarProspectores(String numExp, List<EcaSolProspectoresVO> listaPros, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        int total = 0;
        try
        {
            EcaSolicitudVO sol = this.getDatosSolicitud(numExp, adaptador);
            if(sol != null && sol.getSolicitudCod() != null)
            {
                con = adaptador.getConnection();
                adaptador.inicioTransaccion(con);
                MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
                
                //Primero elimino todos los registros asociados a la solicitud
                List<EcaJusProspectoresVO> listJus = meLanbide44DAO.getProspectoresJustificacion(sol, con);
                if(listJus != null && listJus.size() > 0)
                {
                    for(EcaJusProspectoresVO p : listJus)
                    {
                        this.eliminarProspectorJustificacion(p, adaptador);
                    }
                }
                meLanbide44DAO.eliminarTodosProspectoresSol_Solicitud(sol, con);
                
                
                //Guardo la relacion NIF - ID para ir asociando los sustitutos
                Map<String, Integer> mapaIds = new HashMap<String, Integer>();
                Integer id = null;
                //Luego voy haciendo los insert uno a uno
                for(EcaSolProspectoresVO pros : listaPros)
                {
                    pros.setSolicitud(sol.getSolicitudCod());
                    
                    //Miro si es un sustituto. En ese caso busco el ID del sustituido en el hashmap
                    if(pros.getNifProspectorSustituido() != null && !pros.getNifProspectorSustituido().equals(""))
                    {
                        id = mapaIds.get(pros.getNifProspectorSustituido());
                        if(id != null)
                        {
                            pros.setSolProspectorOrigen(id);
                            pros.setTipoSust(ConstantesMeLanbide44.TIPOS_SUSTITUTO.SUSTITUTO_SOLICITUD);
                        }
                    }
                    
                    pros = meLanbide44DAO.guardarEcaSolProspectoresVO(pros, con);
                    if(pros != null)
                    {
                        total++;
                        if(pros.getSolProspectoresCod() != null && pros.getNif() != null && !mapaIds.containsKey(pros.getNif()))
                        {
                            mapaIds.put(pros.getNif(), pros.getSolProspectoresCod());
                        }
                    }
                }
                adaptador.finTransaccion(con);
            }
            return total;
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD importando prospectores", e);
            adaptador.rollBack(con);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD importando prospectores", ex);
            adaptador.rollBack(con);
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
    }
    
    public List<FilaProspectorSolicitudVO> getListaProspectoresSolicitud(EcaSolicitudVO sol, AdaptadorSQLBD adaptador, int idioma) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
            List<FilaProspectorSolicitudVO> retList = meLanbide44DAO.getListaProspectoresSolicitud(sol, con);
            EcaConfiguracionVO config = this.getConfiguracionEca(MeLanbide44Utils.getEjercicioDeExpediente(sol.getNumExp()), adaptador);
            EcaSolProspectoresVO origen = null;
            List<EcaSolProspectoresVO> sustitutos = null;
            String mensajeCompleto = null;
            String parte = null;
            for(FilaProspectorSolicitudVO fila : retList)
            {
                origen = null;
                if(fila.getSolProspectorOrigen() != null)
                {
                    origen = new EcaSolProspectoresVO();
                    origen.setSolProspectoresCod(fila.getSolProspectorOrigen());
                    origen = meLanbide44DAO.getProspectorSolicitudPorId(origen, con);
                }
                
                sustitutos = null;
                try
                {
                    sustitutos = meLanbide44DAO.getSustitutosProspectorSolicitud(Integer.parseInt(fila.getId()), con);
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }
                
                List<String> errores = MeLanbide44Utils.validarFilaProspectorSolicitud(fila, origen, sustitutos, config, sol.getExpEje(), idioma);
                if(errores != null && errores.size() > 0)
                {
                    String[] partes = null;
                    String[] codigos = null;
                    List<String> erroresSinCod = new ArrayList<String>();
                    for(String str : errores)
                    {
                        partes = str.split(ConstantesMeLanbide44.BARRA_SEPARADORA);
                        codigos = partes[0].split(ConstantesMeLanbide44.SEPARADOR_VALORES_CONF);
                        for(String codigo : codigos)
                        {
                            fila.setErrorCampo(Integer.parseInt(codigo), ConstantesMeLanbide44.CIERTO);
                            for(int i = 1; i < partes.length; i++)
                            {
                                parte = partes[i];
                                if(mensajeCompleto == null)
                                {
                                    mensajeCompleto = new String(parte);
                                }
                                else
                                {
                                    mensajeCompleto += ConstantesMeLanbide44.BARRA_SEPARADORA+parte;
                                }
                            }
                            if(!erroresSinCod.contains(mensajeCompleto))
                            {
                                erroresSinCod.add(mensajeCompleto);
                            }
                            mensajeCompleto = null;
                        }
                    }
                   fila.setErrores(erroresSinCod);
                   //MeLanbide44Utils.formatearFilaPreparadorSolicitudVOError(fila);
                }
                
                if(fila.getTipoSust() != null && fila.getTipoSust().equals(ConstantesMeLanbide44.TIPOS_SUSTITUTO.SUSTITUTO_SOLICITUD))
                {
                    if(fila.getNif() != null && !fila.getNif().equals("") && !fila.getNif().equals("-"))
                    {
                        fila.setNif(fila.getNif() + " - "+"("+fila.getTipoSust()+")");
                    }
                }
            }
            return retList;
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando lista prospectores de solicitud "+(sol != null ? sol.getSolicitudCod() : "(solicitud = null)"), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando lista prospectores de solicitud "+(sol != null ? sol.getSolicitudCod() : "(solicitud = null)"), ex);
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
    }
    
    public EcaSolProspectoresVO getProspectorSolicitudPorId(EcaSolProspectoresVO pros, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
            return meLanbide44DAO.getProspectorSolicitudPorId(pros, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando prospector solicitud "+(pros != null ? pros.getSolProspectoresCod() : "(prospector = null)"), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando prospector solicitud "+(pros != null ? pros.getSolProspectoresCod() : "(prospector = null)"), ex);
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
    }
    
    public EcaSolProspectoresVO guardarEcaSolProspectoresVO(EcaSolProspectoresVO pros, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
            return meLanbide44DAO.guardarEcaSolProspectoresVO(pros, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD guardando prospector solicitud "+(pros != null ? pros.getSolProspectoresCod() : "(prospector = null)"), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD guardando prospector solicitud "+(pros != null ? pros.getSolProspectoresCod() : "(prospector = null)"), ex);
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
    }
    
    /*public int eliminarProspectorSolicitud(EcaSolProspectoresVO pros, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
            return meLanbide44DAO.eliminarProspectorSolicitud(pros, con);
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
    
    public int eliminarProspectorSolicitud(EcaSolProspectoresVO pros, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            adaptador.inicioTransaccion(con);
            MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
            int pos = 0;
            List<Integer> listaE = new ArrayList<Integer>();
            listaE.add(pros.getSolProspectoresCod());
            buscarProsElim(listaE, pros, pos, con);
            int result = 0;
            for(Integer i : listaE)
            {
                pros = new EcaSolProspectoresVO();
                pros.setSolProspectoresCod(i);
                pros = meLanbide44DAO.getProspectorSolicitudPorId(pros, con);
                result += meLanbide44DAO.eliminarProspectorSolicitud(pros, con);
            }
            adaptador.finTransaccion(con);
            return result;
        }
        catch(BDException e)
        {
            adaptador.rollBack(con);
            log.error("Se ha producido una excepcion en la BBDD eliminando prospector solicitud "+(pros != null ? pros.getSolProspectoresCod() : "(prospector = null)"), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            adaptador.rollBack(con);
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
    }
    
    public EcaSolValoracionVO getValoracionSolicitud(EcaSolicitudVO sol, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
            return meLanbide44DAO.getValoracionSolicitud(sol, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando valoracion de solicitud "+(sol != null ? sol.getSolicitudCod() : "(solicitud = null)"), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando valoracion de solicitud "+(sol != null ? sol.getSolicitudCod() : "(solicitud = null)"), ex);
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
    }
    
    public EcaSolValoracionVO guardarDatosValoracionSolicitud(EcaSolValoracionVO val, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
            return meLanbide44DAO.guardarDatosValoracionSolicitud(val, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD guardando valoracion de la solicitud "+(val != null && val.getSolicitud() != null ? val.getSolicitud().toString() : ""), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD guardando valoracion de la solicitud "+(val != null && val.getSolicitud() != null ? val.getSolicitud().toString() : ""), ex);
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
    }
    
    public EcaConfiguracionVO getConfiguracionEca(int ano, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
            return meLanbide44DAO.getConfiguracionEca(ano, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando la configuracion del aï¿½o "+ano, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando la configuracion del aï¿½o "+ano, ex);
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
    }
    
    
    
    //funciones justificacion
    public List<FilaPreparadorJustificacionVO> getListaPreparadoresJustificacion(EcaSolicitudVO sol, AdaptadorSQLBD adaptador, int idioma) throws Exception
    {
        Connection con = null;
        try
        {
            /*
            con = adaptador.getConnection();
            MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
            return meLanbide44DAO.getListaPreparadoresJustificacion(sol, con);
            */
            con = adaptador.getConnection();
            MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
            List<FilaPreparadorJustificacionVO> retList =  meLanbide44DAO.getListaPreparadoresJustificacion(sol, idioma, con);
            EcaConfiguracionVO config = this.getConfiguracionEca(MeLanbide44Utils.getEjercicioDeExpediente(sol.getNumExp()), adaptador);
            List<Integer> listaTipoContrato =  meLanbide44DAO.getListaCodigosTipoContrato(con);
            
            String mensaje="";
            MeLanbide44I18n traductor = MeLanbide44I18n.getInstance();
            EcaJusPreparadoresVO origen = null;
            List<EcaJusPreparadoresVO> sustitutos = null;
            for(FilaPreparadorJustificacionVO fila : retList)
            {
                origen = null;
                if(fila.getJusPreparadorOrigen() != null)
                {
                    origen = new EcaJusPreparadoresVO();
                    origen.setJusPreparadoresCod(fila.getJusPreparadorOrigen());
                    origen = meLanbide44DAO.getPreparadorJustificacionPorId(origen, con);
                }
                
                sustitutos = null;
                try
                {
                    sustitutos = meLanbide44DAO.getSustitutosPreparadorJustificacion(Integer.parseInt(fila.getId()), con);
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }
                
                List<String> errores = MeLanbide44Utils.validarFilaPreparadorJustificacion(fila, origen, sustitutos, listaTipoContrato, config, sol.getExpEje(), idioma, false);
                EcaJusPreparadoresVO prep = new EcaJusPreparadoresVO();
                prep.setJusPreparadoresCod(Integer.parseInt(fila.getId()));
                prep =meLanbide44DAO.getPreparadorJustificacionPorId(prep, con);
                List<FilaSegPreparadoresVO> listaseg = getListaSeguimientos(sol,prep, ConstantesMeLanbide44.TIPO_SEGUIMIENTO_MELANBIDE44, "", "", idioma, adaptador);
                List<FilaInsPreparadoresVO> listainsc1 = getListaInserciones(sol, prep, ConstantesMeLanbide44.TIPO_INSERCION_MELANBIDE44, "1", "", idioma, adaptador);
                List<FilaInsPreparadoresVO> listainsc2 = getListaInserciones(sol, prep, ConstantesMeLanbide44.TIPO_INSERCION_MELANBIDE44, "2", "", idioma, adaptador);
                List<FilaInsPreparadoresVO> listainsc3 =  getListaInserciones(sol, prep, ConstantesMeLanbide44.TIPO_INSERCION_MELANBIDE44, "3", "", idioma, adaptador);
                List<FilaInsPreparadoresVO> listainsc4 =  getListaInserciones(sol, prep, ConstantesMeLanbide44.TIPO_INSERCION_MELANBIDE44, "4", "", idioma, adaptador);
                
                List<FilaInsPreparadoresVO> listains =  getListaInserciones(sol, prep, ConstantesMeLanbide44.TIPO_INSERCION_MELANBIDE44, "0", "", idioma, adaptador);
                
                for(FilaSegPreparadoresVO seg : listaseg){
                    List<String> erroresseg = seg.getErrores();
                    if (erroresseg.size()>0){
                        
                        try
                        {
                            fila.setNumSegAnteriores(MeLanbide44Utils.restarSeguimientoPorErrores(fila.getNumSegAnteriores(), "100", false));
                        }
                        catch(Exception ex)
                        {
                            ex.printStackTrace();
                        }
                        
                        mensaje = traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.SegErrores");
                        mensaje = FilaPreparadorJustificacionVO.POS_CAMPO_NUM_SEG_ANTERIORES+ConstantesMeLanbide44.BARRA_SEPARADORA+mensaje;
                        if(!errores.contains(mensaje))
                            errores.add(mensaje);   
                    }
                    //break;
                }
                DecimalFormatSymbols simbolo=new DecimalFormatSymbols();
                simbolo.setDecimalSeparator(',');
                simbolo.setGroupingSeparator('.');
                DecimalFormat formateador = new DecimalFormat("#,##0.00",simbolo);
                
                String[] partesnum = fila.getNumSegAnteriores().split("-");
                if (partesnum.length>0){
                    BigDecimal numseg = partesnum[1]!=null ?new BigDecimal(partesnum[1].trim().replace(".", "").replace(",", ".")):BigDecimal.ZERO;
                    BigDecimal imp1 = numseg.multiply(config.getImSeguimiento());
                    BigDecimal imp2 = new BigDecimal("0.00");
                    try
                    {
                        imp2 = new BigDecimal(fila.getCostesSalarialesSSEca().replace(".", "").replace(",", ".")).multiply(config.getPoMaxSeguimientos());
                    }
                    catch(Exception ex)
                    {
                        
                    }
                   if (imp1.compareTo(imp2) <0){
                        fila.setImporte(formateador.format(imp1.setScale(2,RoundingMode.HALF_UP)));
                        
                   }else {
                       fila.setImporte(formateador.format(imp2.setScale(2,RoundingMode.HALF_UP)));
                        
                   }
                   
                }
                
                
                for(FilaInsPreparadoresVO ins : listainsc1){
                    List<String> erroresseg = ins.getErrores();                    
                    if (erroresseg.size()>0){
                        
                        try
                        {
                            if(ins.getSexo() != null)
                            {
                                int sexo = Integer.parseInt(ins.getSexo());
                                if((sexo == ConstantesMeLanbide44.CODIGOS_SEXO.HOMBRE || sexo == ConstantesMeLanbide44.CODIGOS_SEXO.MUJER))
                                {
                                    fila.setC1Total(MeLanbide44Utils.restarSeguimientoPorErrores(fila.getC1Total(), ins.getPorcJornada(), true));
                                    mensaje = traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.C1Errores");
                                    switch(sexo)
                                    {
                                        case ConstantesMeLanbide44.CODIGOS_SEXO.HOMBRE:
                                            fila.setC1h(MeLanbide44Utils.restarSeguimientoPorErrores(fila.getC1h(), ins.getPorcJornada(), true));                                            
                                            fila.setErrorCampo(FilaPreparadorJustificacionVO.POS_CAMPO_C1H, "S");
                                            break;
                                        case ConstantesMeLanbide44.CODIGOS_SEXO.MUJER:
                                            fila.setC1m(MeLanbide44Utils.restarSeguimientoPorErrores(fila.getC1m(), ins.getPorcJornada(), true));
                                            fila.setErrorCampo(FilaPreparadorJustificacionVO.POS_CAMPO_C1M, "S");
                                            break;
                                    }
                                }
                            }
                        }
                        catch(Exception ex)
                        {
                            ex.printStackTrace();
                        }
                        
                        mensaje = traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.C1Errores");
                        mensaje = FilaPreparadorJustificacionVO.POS_CAMPO_C1TOTAL+ConstantesMeLanbide44.BARRA_SEPARADORA+mensaje;
                        if(!errores.contains(mensaje))
                            errores.add(mensaje); 
                        
                        //break;
                    }                    
                }
                
                for(FilaInsPreparadoresVO ins : listainsc2){
                    List<String> erroresseg = ins.getErrores();                    
                    if (erroresseg.size()>0){
                        
                        try
                        {
                            if(ins.getSexo() != null)
                            {
                                int sexo = Integer.parseInt(ins.getSexo());
                                if((sexo == ConstantesMeLanbide44.CODIGOS_SEXO.HOMBRE || sexo == ConstantesMeLanbide44.CODIGOS_SEXO.MUJER))
                                {
                                    fila.setC2Total(MeLanbide44Utils.restarSeguimientoPorErrores(fila.getC2Total(), ins.getPorcJornada(), true));

                                    switch(sexo)
                                    {
                                        case ConstantesMeLanbide44.CODIGOS_SEXO.HOMBRE:
                                            fila.setC2h(MeLanbide44Utils.restarSeguimientoPorErrores(fila.getC2h(), ins.getPorcJornada(), true));
                                            fila.setErrorCampo(FilaPreparadorJustificacionVO.POS_CAMPO_C2H, "S");
                                            break;
                                        case ConstantesMeLanbide44.CODIGOS_SEXO.MUJER:
                                            fila.setC2m(MeLanbide44Utils.restarSeguimientoPorErrores(fila.getC2m(), ins.getPorcJornada(), true));
                                            fila.setErrorCampo(FilaPreparadorJustificacionVO.POS_CAMPO_C2M, "S");
                                            break;
                                    }
                                }
                            }
                        }
                        catch(Exception ex)
                        {
                            ex.printStackTrace();
                        }
                        
                        mensaje = traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.C2Errores");
                        mensaje = FilaPreparadorJustificacionVO.POS_CAMPO_C2TOTAL+ConstantesMeLanbide44.BARRA_SEPARADORA+mensaje;
                        if(!errores.contains(mensaje))
                            errores.add(mensaje);   
                        //break;
                    }                   
                }
                
                for(FilaInsPreparadoresVO ins : listainsc3){
                    List<String> erroresseg = ins.getErrores();                    
                    if (erroresseg.size()>0){
                        
                        try
                        {
                            if(ins.getSexo() != null)
                            {
                                int sexo = Integer.parseInt(ins.getSexo());
                                if((sexo == ConstantesMeLanbide44.CODIGOS_SEXO.HOMBRE || sexo == ConstantesMeLanbide44.CODIGOS_SEXO.MUJER))
                                {
                                    fila.setC3Total(MeLanbide44Utils.restarSeguimientoPorErrores(fila.getC3Total(), ins.getPorcJornada(), true));

                                    switch(sexo)
                                    {
                                        case ConstantesMeLanbide44.CODIGOS_SEXO.HOMBRE:
                                            fila.setC3h(MeLanbide44Utils.restarSeguimientoPorErrores(fila.getC3h(), ins.getPorcJornada(), true));
                                            fila.setErrorCampo(FilaPreparadorJustificacionVO.POS_CAMPO_C3H, "S");
                                            break;
                                        case ConstantesMeLanbide44.CODIGOS_SEXO.MUJER:
                                            fila.setC3m(MeLanbide44Utils.restarSeguimientoPorErrores(fila.getC3m(), ins.getPorcJornada(), true));
                                            fila.setErrorCampo(FilaPreparadorJustificacionVO.POS_CAMPO_C3M, "S");
                                            break;
                                    }
                                }
                            }
                        }
                        catch(Exception ex)
                        {
                            ex.printStackTrace();
                        }
                        
                        mensaje = traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.C3Errores");
                        mensaje = FilaPreparadorJustificacionVO.POS_CAMPO_C3TOTAL+ConstantesMeLanbide44.BARRA_SEPARADORA+mensaje;
                        if(!errores.contains(mensaje))
                            errores.add(mensaje);   
                        //break;
                    }                    
                }
                
                for(FilaInsPreparadoresVO ins : listainsc4){
                    List<String> erroresseg = ins.getErrores();                    
                    if (erroresseg.size()>0){
                        
                        try
                        {
                            if(ins.getSexo() != null)
                            {
                                int sexo = Integer.parseInt(ins.getSexo());
                                if((sexo == ConstantesMeLanbide44.CODIGOS_SEXO.HOMBRE || sexo == ConstantesMeLanbide44.CODIGOS_SEXO.MUJER))
                                {
                                    fila.setC4Total(MeLanbide44Utils.restarSeguimientoPorErrores(fila.getC4Total(), ins.getPorcJornada(), true));

                                    switch(sexo)
                                    {
                                        case ConstantesMeLanbide44.CODIGOS_SEXO.HOMBRE:
                                            fila.setC4h(MeLanbide44Utils.restarSeguimientoPorErrores(fila.getC4h(), ins.getPorcJornada(), true));
                                            fila.setErrorCampo(FilaPreparadorJustificacionVO.POS_CAMPO_C4H, "S");
                                            break;
                                        case ConstantesMeLanbide44.CODIGOS_SEXO.MUJER:
                                            fila.setC4m(MeLanbide44Utils.restarSeguimientoPorErrores(fila.getC4m(), ins.getPorcJornada(), true));
                                            fila.setErrorCampo(FilaPreparadorJustificacionVO.POS_CAMPO_C4M, "S");
                                            break;
                                    }
                                }
                            }
                        }
                        catch(Exception ex)
                        {
                            ex.printStackTrace();
                        }
                        mensaje = traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.C4Errores");
                        mensaje = FilaPreparadorJustificacionVO.POS_CAMPO_C4TOTAL+ConstantesMeLanbide44.BARRA_SEPARADORA+mensaje;
                        if(!errores.contains(mensaje))
                            errores.add(mensaje);   
                        //break;
                    }
                }
                
                for(FilaInsPreparadoresVO ins : listains){
                    List<String> erroresseg = ins.getErrores();                    
                    if (erroresseg.size()>0){
                                                
                        mensaje = traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.NoColectivoErrores");                                          
                        //mensaje = FilaPreparadorJustificacionVO.POS_CAMPO_C1TOTAL+ConstantesMeLanbide44.SEPARADOR_VALORES_CONF+FilaPreparadorJustificacionVO.POS_CAMPO_C2TOTAL+ConstantesMeLanbide44.SEPARADOR_VALORES_CONF+FilaPreparadorJustificacionVO.POS_CAMPO_C3TOTAL+ConstantesMeLanbide44.SEPARADOR_VALORES_CONF+FilaPreparadorJustificacionVO.POS_CAMPO_C4TOTAL+ConstantesMeLanbide44.BARRA_SEPARADORA+mensaje;
                        mensaje = FilaPreparadorJustificacionVO.POS_CAMPO_NIF+ConstantesMeLanbide44.BARRA_SEPARADORA+mensaje;
                        if(!errores.contains(mensaje))
                            errores.add(mensaje);   
                        //break;
                    }
                }
                
                String parte = null;
                String mensajeCompleto = null;
                if(errores != null && errores.size() > 0)
                {
                    String[] partes = null;
                    String[] codigos = null;
                    List<String> erroresSinCod = new ArrayList<String>();
                    for(String str : errores)
                    {
                        if(str != null && !str.equals(""))
                        {
                            partes = str.split(ConstantesMeLanbide44.BARRA_SEPARADORA);
                            codigos = partes[0].split(ConstantesMeLanbide44.SEPARADOR_VALORES_CONF);
                            for(String codigo : codigos)
                            {
                                fila.setErrorCampo(Integer.parseInt(codigo), ConstantesMeLanbide44.CIERTO);
                                for(int i = 1; i < partes.length; i++)
                                {
                                    parte = partes[i];
                                    if(mensajeCompleto == null)
                                    {
                                        mensajeCompleto = new String(parte);
                                    }
                                    else
                                    {
                                        mensajeCompleto += ConstantesMeLanbide44.BARRA_SEPARADORA+parte;
                                    }
                                }
                                if(!erroresSinCod.contains(mensajeCompleto))
                                {
                                    erroresSinCod.add(mensajeCompleto);
                                }
                                mensajeCompleto = null;
                            }
                        }
                    }
                   fila.setErrores(erroresSinCod);
                   //MeLanbide44Utils.formatearFilaPreparadorSolicitudVOError(fila);
                }
                
                if(fila.esSustituto() != null && fila.esSustituto())
                {
                    if(fila.getNif() != null && !fila.getNif().equals("") && !fila.getNif().equals("-"))
                    {
                        fila.setNif(fila.getNif()+" - "+"("+(fila.getTipoSust() != null && !fila.getTipoSust().equals("") ? fila.getTipoSust() : "S")+")");
                    }
                }
                BigDecimal imptins = null;
                BigDecimal jvalorBD = null;
                EcaConfiguracionVO conf = null;
                try
                {
                    if(sol.getNumExp() != null)
                    {
                        String[] datos = sol.getNumExp().split(ConstantesMeLanbide44.BARRA_SEPARADORA);
                        conf = this.getConfiguracionEca(Integer.parseInt(datos[0]), adaptador);
                    }
                }
                catch(Exception ex)
                {  
                    log.debug("No existe configuraciónd e datos para ese año");

                }
                
                String valor[] = fila.getC1h().toString().split("-");
                
                valor[1] = valor[1].trim().replace(",", ".");
                BigDecimal c1h = null;
                c1h = conf.getImC1h();
                if(valor[1].equals("0.0000"))
                    jvalorBD = BigDecimal.ZERO;
                else
                    jvalorBD = new BigDecimal(valor[1]);
                c1h = c1h.multiply(jvalorBD);
                imptins = c1h;
                BigDecimal c1m = null;
                c1m = conf.getImC1m();
                String val11[] = fila.getC1m().toString().split("-");
                val11[1] = val11[1].trim().replace(",", ".");
                //val11[1] = val11[1].substring(1, val11[1].length());
                if(val11[1].equals("0.0000"))
                    jvalorBD = BigDecimal.ZERO;
                else
                    jvalorBD = new BigDecimal(val11[1]);
                c1m = c1m.multiply(jvalorBD);
                imptins = imptins.add(c1m);                  
                
                BigDecimal c2h = null;
                c2h = conf.getImC2h();
                String val21[] = fila.getC2h().toString().split("-");
                val21[1] = val21[1].trim().replace(",", ".");
                if(val21[1].equals("0.0000"))
                    jvalorBD = BigDecimal.ZERO;
                else
                    jvalorBD = new BigDecimal(val21[1]);
                c2h = c2h.multiply(jvalorBD);
                imptins = imptins.add(c2h);
                BigDecimal c2m = null;
                c2m = conf.getImC2m();
                String val22[] = fila.getC2m().toString().split("-");
                val22[1] = val22[1].trim().replace(",", ".");
                // = val22[1].substring(1, val22[1].length());
                if(val22[1].equals("0.0000"))
                    jvalorBD = BigDecimal.ZERO;
                else
                    jvalorBD = new BigDecimal(val22[1]);
                c2m = c2m.multiply(jvalorBD);
                imptins = imptins.add(c2m);      
                
                BigDecimal c3h = null;
                c3h = conf.getImC3h();
                String val31[] = fila.getC3h().toString().split("-");
                val31[1] = val31[1].trim().replace(",", ".");
                //val31[1] = val31[1].substring(1, val31[1].length());
                if(val31[1].equals("0.0000"))
                    jvalorBD = BigDecimal.ZERO;
                else
                    jvalorBD = new BigDecimal(val31[1]);
                c3h = c3h.multiply(jvalorBD);
                imptins = imptins.add(c3h);
                BigDecimal c3m = null;
                c3m = conf.getImC3m();
                String val32[] = fila.getC3m().toString().split("-");
                val32[1] = val32[1].trim().replace(",", ".");
                if(val32[1].equals("0.0000"))
                    jvalorBD = BigDecimal.ZERO;
                else
                    jvalorBD = new BigDecimal(val32[1]);
                c3m = c3m.multiply(jvalorBD);
                imptins = imptins.add(c3m); 
                
                BigDecimal c4h = null;
                c4h = conf.getImC4h();
                String val41[] = fila.getC4h().toString().split("-");
                val41[1] = val41[1].trim().replace(",", ".");
                if(val41[1].equals("0.0000"))
                    jvalorBD = BigDecimal.ZERO;
                else
                    jvalorBD = new BigDecimal(val41[1]);
                c4h = c4h.multiply(jvalorBD);
                imptins = imptins.add(c4h);
                BigDecimal c4m = null;
                c4m = conf.getImC4m();
                String val42[] = fila.getC4m().toString().split("-");
                val42[1] = val42[1].trim().replace(",", ".");
                //val42[1] = val42[1].substring(1, val42[1].length());
                if(val42[1].equals("0.0000"))
                    jvalorBD = BigDecimal.ZERO;
                else
                    jvalorBD = new BigDecimal(val42[1]);
                c4m = c4m.multiply(jvalorBD);
                imptins = imptins.add(c4m);
                
                fila.setInserciones(formateador.format(imptins));
                BigDecimal importe = fila.getInserciones()!=null && fila.getInserciones()!="" ?new BigDecimal(fila.getInserciones().trim().replace(".", "").replace(",", ".")):BigDecimal.ZERO;                
                BigDecimal imptseg = fila.getImporte()!=null && fila.getImporte()!="" ?new BigDecimal(fila.getImporte().trim().replace(".", "").replace(",", ".")):BigDecimal.ZERO;
                //BigDecimal imptins = fila.getInserciones()!=null && fila.getInserciones()!="" ?new BigDecimal(fila.getInserciones().trim().replace(".", "").replace(",", ".")):BigDecimal.ZERO;
                fila.setSeguimientosInserciones(formateador.format(imptseg.add(imptins)));
                
            }
            return retList;
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando lista preparadores", e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuï¿½rando lista preparadores", ex);
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
    }
    
     public EcaJusPreparadoresVO getPreparadorJustificacionPorId(EcaJusPreparadoresVO prep, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
            return meLanbide44DAO.getPreparadorJustificacionPorId(prep, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando preparador solicitud "+(prep != null ? prep.getJusPreparadoresCod() : "(preparador = null)"), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando preparador solicitud "+(prep != null ? prep.getJusPreparadoresCod() : "(preparador = null)"), ex);
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
    }
    
     public EcaJusPreparadoresVO getPreparadorJustificacionPorNIF(EcaSolicitudVO sol, String nif, Date feInicio, /*String feFin,*/ AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
            return meLanbide44DAO.getPreparadorJustificacionPorNIF(sol, nif, feInicio, /*feFin,*/ con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando preparador solicitud nif:"+nif+", "+(sol != null ? sol.getSolicitudCod() : "(solicitud = null)"), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando preparador solicitud nif:"+nif+", "+(sol != null ? sol.getSolicitudCod() : "(solicitud = null)"), ex);
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
    }
    
     
             
    public EcaJusPreparadoresVO getPreparadorJustificacionSustituto(Integer idPrep, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
            return meLanbide44DAO.getPreparadorJustificacionSustituto(idPrep, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando sustituto preparador:"+idPrep, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando sustituto preparador:"+idPrep, ex);
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
    }
    
    public EcaJusProspectoresVO getProspectorJustificacionSustituto(Integer idPros, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
            return meLanbide44DAO.getProspectorJustificacionSustituto(idPros, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando sustituto prospector:"+idPros, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando sustituto prospector:"+idPros, ex);
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
    } 
    
    public EcaJusPreparadoresVO guardarEcaJusPreparadoresVO(EcaJusPreparadoresVO prep, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
            EcaJusPreparadoresVO preparador = meLanbide44DAO.guardarEcaJusPreparadoresVO(prep, con);
            //meLanbide44DAO.actualizarSustituto(idPrepOrigen, prep, con);
            return preparador;
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD guardando preparador justificacion "+(prep != null ? prep.getJusPreparadoresCod() : "(preparador = null)"), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD guardando preparador justificacion "+(prep != null ? prep.getJusPreparadoresCod() : "(preparador = null)"), ex);
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
    }
    
     public int eliminarPreparadorJustificacion(EcaJusPreparadoresVO prep, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            adaptador.inicioTransaccion(con);
            MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
            int pos = 0;
            List<Integer> listaE = new ArrayList<Integer>();
            listaE.add(prep.getJusPreparadoresCod());
            buscarPrepElim(listaE, prep, pos, con);
            int result = 0;
            for(Integer i : listaE)
            {
                prep = new EcaJusPreparadoresVO();
                prep.setJusPreparadoresCod(i);
                prep = meLanbide44DAO.getPreparadorJustificacionPorId(prep, con);
                if (prep != null) {
                    meLanbide44DAO.eliminarTodosSeguimientosPreparador(prep, con);
                    result += meLanbide44DAO.eliminarPreparadorJustificacion(prep, con);
                }
            }
            adaptador.finTransaccion(con);
            return result;
        }
        catch(BDException e)
        {
            adaptador.rollBack(con);
            log.error("Se ha producido una excepcion en la BBDD eliminando preparador justificacion "+(prep != null ? prep.getJusPreparadoresCod() : "(preparador = null)"), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            adaptador.rollBack(con);
            log.error("Se ha producido una excepcion en la BBDD eliminando preparador justificacion "+(prep != null ? prep.getJusPreparadoresCod() : "(preparador = null)"), ex);
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
    }
     
    public int importarSeguimientos(String numExp, List<EcaSegPreparadoresVO> listaSeg, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        int total = 0;
        try
        {
            EcaSolicitudVO sol = this.getDatosSolicitud(numExp, adaptador);
            if(sol != null && sol.getSolicitudCod() != null)
            {
                con = adaptador.getConnection();
                adaptador.inicioTransaccion(con);
                MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
                
                //Primero elimino todos los registros asociados a la solicitud
                meLanbide44DAO.eliminarTodosSeguimientosPrep_Solicitud(sol,ConstantesMeLanbide44.TIPO_SEGUIMIENTO_MELANBIDE44, con);
                
                //Luego voy haciendo los insert uno a uno                
                for(EcaSegPreparadoresVO seg : listaSeg)
                {
                    //seg.setSolicitud(sol.getSolicitudCod());
                    //guardar preparador -buscar para esta solicitud por nif de preparador
                    seg.setTipo(0);
                    if(meLanbide44DAO.guardarEcaSegPreparadoresVO(seg, con) != null)
                    {
                        total++;
                    }
                }
                adaptador.finTransaccion(con);
            }
            return total;
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD importando preparadores", e);
            adaptador.rollBack(con);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD importando preparadores", ex);
            adaptador.rollBack(con);
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
    }
    
    public int importarSeguimientos2016(String numExp, List<EcaSegPreparadores2016VO> listaSeg, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        int total = 0;
        try
        {
            EcaSolicitudVO sol = this.getDatosSolicitud(numExp, adaptador);
            if(sol != null && sol.getSolicitudCod() != null)
            {
                con = adaptador.getConnection();
                adaptador.inicioTransaccion(con);
                MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
                
                //Primero elimino todos los registros asociados a la solicitud
                meLanbide44DAO.eliminarTodosSeguimientosPrep_Solicitud(sol,ConstantesMeLanbide44.TIPO_SEGUIMIENTO_MELANBIDE44, con);
                
                //Luego voy haciendo los insert uno a uno                
                for(EcaSegPreparadores2016VO seg : listaSeg)
                {
                    //seg.setSolicitud(sol.getSolicitudCod());
                    //guardar preparador -buscar para esta solicitud por nif de preparador
                    seg.setTipo(0);
                    if(meLanbide44DAO.guardarEcaSegPreparadoresVO_2016(seg, con) != null)
                    {
                        total++;
                    }
                }
                adaptador.finTransaccion(con);
            }
            return total;
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD importando preparadores", e);
            adaptador.rollBack(con);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD importando preparadores", ex);
            adaptador.rollBack(con);
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
    }
    
    public List<FilaSegPreparadoresVO> getListaSeguimientos(EcaSolicitudVO sol, EcaJusPreparadoresVO prep, String tipo, String colectivo, String sexo, Integer idioma, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            /*con = adaptador.getConnection();
            MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
            return meLanbide44DAO.getListaSeguimientos(prep, tipo, colectivo, sexo, idioma, con);
            */
            con = adaptador.getConnection();
            MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
            List<FilaSegPreparadoresVO> retList =  meLanbide44DAO.getListaSeguimientos(sol, prep, tipo, colectivo, sexo, idioma, con);
            EcaConfiguracionVO config = this.getConfiguracionEca(MeLanbide44Utils.getEjercicioDeExpediente(sol.getNumExp()), adaptador);
            
            List<Integer> listaTipoContrato =  meLanbide44DAO.getListaCodigosTipoContrato(con);
            List<Integer> listaTipoDiscapacidad =  meLanbide44DAO.getListaCodigosTipoDiscapacidad(con);
            List<Integer> listaGravedad =  meLanbide44DAO.getListaCodigosGravedad(con);
            
            for(FilaSegPreparadoresVO fila : retList)
            {
                                               
                List<String> errores = MeLanbide44Utils.validarFilaSeguimientoPrep(sol.getNumExp(),fila, config, idioma, listaTipoDiscapacidad, listaGravedad, listaTipoContrato);
                
                if(errores != null && errores.size() > 0)
                {
                    String[] partes = null;
                    String[] codigos = null;
                    List<String> erroresSinCod = new ArrayList<String>();
                    for(String str : errores)
                    {
                        partes = str.split(ConstantesMeLanbide44.BARRA_SEPARADORA);
                        codigos = partes[0].split(ConstantesMeLanbide44.SEPARADOR_VALORES_CONF);
                        for(String codigo : codigos)
                        {
                            fila.setErrorCampo(Integer.parseInt(codigo), ConstantesMeLanbide44.CIERTO);
                            erroresSinCod.add(partes[1]);
                        }
                    }
                   fila.setErrores(erroresSinCod);
                   //MeLanbide44Utils.formatearFilaPreparadorSolicitudVOError(fila);
                }
            }
            return retList;
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando lista preparadores de solicitud "+(sol != null ? sol.getSolicitudCod() : "(solicitud = null)"), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando lista preparadores de solicitud "+(sol != null ? sol.getSolicitudCod() : "(solicitud = null)"), ex);
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
    }
    
    
    public List<FilaInsPreparadoresVO> getListaInserciones(EcaSolicitudVO sol, EcaJusPreparadoresVO prep, String tipo, String colectivo, String sexo, Integer idioma, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            /*con = adaptador.getConnection();
            MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
            return meLanbide44DAO.getListaSeguimientos(prep, tipo, colectivo, sexo, idioma, con);
            */
            con = adaptador.getConnection();
            MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
            List<FilaInsPreparadoresVO> retList =  meLanbide44DAO.getListaInserciones(sol, prep, tipo, colectivo, sexo, idioma, con);
            EcaConfiguracionVO config = this.getConfiguracionEca(MeLanbide44Utils.getEjercicioDeExpediente(sol.getNumExp()), adaptador);
            
            List<Integer> listaTipoContrato =  meLanbide44DAO.getListaCodigosTipoContrato(con);
            List<Integer> listaTipoDiscapacidad =  meLanbide44DAO.getListaCodigosTipoDiscapacidad(con);
            List<Integer> listaGravedad =  meLanbide44DAO.getListaCodigosGravedad(con);
            
            for(FilaInsPreparadoresVO fila : retList)
            {
                                               
                List<String> errores = MeLanbide44Utils.validarFilaInsercionPrep(sol.getNumExp(),fila, config, idioma, listaTipoDiscapacidad, listaGravedad, listaTipoContrato);
                
                if(errores != null && errores.size() > 0)
                {
                    String[] partes = null;
                    String[] codigos = null;
                    List<String> erroresSinCod = new ArrayList<String>();
                    for(String str : errores)
                    {
                        partes = str.split(ConstantesMeLanbide44.BARRA_SEPARADORA);
                        codigos = partes[0].split(ConstantesMeLanbide44.SEPARADOR_VALORES_CONF);
                        for(String codigo : codigos)
                        {
                            fila.setErrorCampo(Integer.parseInt(codigo), ConstantesMeLanbide44.CIERTO);
                            erroresSinCod.add(partes[1]);
                        }
                    }
                   fila.setErrores(erroresSinCod);
                   //MeLanbide44Utils.formatearFilaPreparadorSolicitudVOError(fila);
                }
            }
            return retList;
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando lista preparadores de solicitud "+(sol != null ? sol.getSolicitudCod() : "(solicitud = null)"), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando lista preparadores de solicitud "+(sol != null ? sol.getSolicitudCod() : "(solicitud = null)"), ex);
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
    }
    
    public List<FilaProspectorJustificacionVO> getListaProspectoresJustificacion(EcaSolicitudVO sol, AdaptadorSQLBD adaptador, int idioma) throws Exception
    {
        Connection con = null;
        try
        {            
            con = adaptador.getConnection();
            MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
            List<FilaProspectorJustificacionVO> retList =  meLanbide44DAO.getListaProspectoresJustificacion(sol, idioma, con);
            EcaConfiguracionVO config = this.getConfiguracionEca(MeLanbide44Utils.getEjercicioDeExpediente(sol.getNumExp()), adaptador);
            String mensaje="";
            MeLanbide44I18n traductor = MeLanbide44I18n.getInstance();
            EcaJusProspectoresVO origen = null;
            List<EcaJusProspectoresVO> sustitutos = null;
            for(FilaProspectorJustificacionVO fila : retList)
            {
                origen = null;
                if(fila.getJusProspectorOrigen() != null)
                {
                    origen = new EcaJusProspectoresVO();
                    origen.setJusProspectoresCod(fila.getJusProspectorOrigen());
                    origen = meLanbide44DAO.getProspectorJustificacionPorId(origen, con);
                }
                
                sustitutos = null;
                try
                {
                    sustitutos = meLanbide44DAO.getSustitutosProspectorJustificacion(Integer.parseInt(fila.getId()), con);
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }
                
                List<String> errores = MeLanbide44Utils.validarFilaProspectorJustificacion(fila, origen, sustitutos, config, sol.getExpEje(), idioma);
                EcaJusProspectoresVO pros = new EcaJusProspectoresVO();
                pros.setJusProspectoresCod(Integer.parseInt(fila.getId()));
                pros =meLanbide44DAO.getProspectorJustificacionPorId(pros, con);
                
                List<FilaVisProspectoresVO> listavis = getListaVisitas(sol,pros, idioma, adaptador);
                                
                for(FilaVisProspectoresVO vis : listavis){
                    List<String> erroresseg = vis.getErrores();
                    if (erroresseg.size()>0){
                        
                        try
                        {
                            fila.setVisitas(MeLanbide44Utils.restarSeguimientoPorErrores(fila.getVisitas(), "100", false));                           
                        }
                        catch(Exception ex)
                        {
                            ex.printStackTrace();
                        }
                        
                        mensaje = traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.visErrores");
                        mensaje = FilaProspectorJustificacionVO.POS_CAMPO_VISITAS+ConstantesMeLanbide44.BARRA_SEPARADORA+mensaje;
                        if(!errores.contains(mensaje))
                            errores.add(mensaje);  
                    }
                    
                }   
                
                //como hay errores en visitas--> importe de visitas mal calculado-> recalcular
                String[] partesvis = fila.getVisitas().split("-");
                String parte1 = partesvis[1].replaceAll(",", "\\.").trim();
                BigDecimal visitas = new BigDecimal(parte1);
                BigDecimal visitasTot = null;
                BigDecimal costepros = null;
                try
                {
                    costepros = fila.getCostesSalarialesSSEca() != null && !fila.getCostesSalarialesSSEca().equals("") && !fila.getCostesSalarialesSSEca().equals("-") ? new BigDecimal(fila.getCostesSalarialesSSEca().replaceAll("\\.", "").replaceAll(",", "\\.")) : new BigDecimal("0.00");
                }
                catch(Exception ex)
                {
                    costepros = new BigDecimal("0.00");
                }
                BigDecimal imptvisitas=BigDecimal.ZERO;
                BigDecimal minimo = BigDecimal.ZERO;
                BigDecimal maximo = BigDecimal.ZERO;
                visitasTot = BigDecimal.ZERO;
                
                try
                {
                    minimo = new BigDecimal(config.getMinEmpVisit().toString());
                    maximo = new BigDecimal(config.getMaxEmpVisit().toString());
                    
                    //recalcular mínimo y máximo en función porcentaje jornada (2013)
                    //if (fila.getHorasContrato() != null && fila.getHorasAnuales() != null)
                    //Hay que recalcular el minimo/maximo en función de jornada dedciación ECA para 2016
                    if(sol.getExpEje()>=2016 && fila.getHorasContrato() != null && fila.getHorasDedicacionECA() != null && fila.getHorasAnuales() != null)
                    {
                        minimo = minimo.multiply(new BigDecimal(fila.getHorasDedicacionECA().replace(".", "").replace(",", ".")));
                        minimo = minimo.divide(new BigDecimal(fila.getHorasAnuales().replace(".", "").replace(",", ".")), 20, RoundingMode.HALF_UP);
                        
                        maximo = maximo.multiply(new BigDecimal(fila.getHorasDedicacionECA().replace(".", "").replace(",", ".")));
                        maximo = maximo.divide(new BigDecimal(fila.getHorasAnuales().replace(".", "").replace(",", ".")), 20, RoundingMode.HALF_UP);
                    }
                    
                    //visitasTot = BigDecimal.ZERO;
                    if(origen != null)
                    {
                        if(origen.getNumEmpVisitar() != null)
                        {
                            visitasTot = visitasTot.add(new BigDecimal(origen.getNumEmpVisitar()));
                        }
                    }
                    if(sustitutos != null && sustitutos.size() > 0)
                    {
                        for(EcaJusProspectoresVO p : sustitutos)
                        {
                            if(p.getNumEmpVisitar() != null)
                            {
                                visitasTot = visitasTot.add(new BigDecimal(p.getNumEmpVisitar()));
                            }
                        }
                    }
                    if(visitas != null)
                    {
                        visitasTot = visitasTot.add(visitas);
                    }
                }
                catch(Exception ex)
                {
                    //ex.printStackTrace();
                }
                
                
                if (visitasTot.compareTo(minimo)<0 ) 
                {
                    imptvisitas=BigDecimal.ZERO;
                }
                /*else if ((visitasTot.compareTo(maximo) > 0)  && ((maximo.multiply(config.getImpVisita())).compareTo(costepros) < 0 ))
                {
                    imptvisitas = maximo.multiply(config.getImpVisita());
                }*/
                /*else if ((visitas.multiply(config.getImpVisita())).compareTo(costepros) <  0 )
                {
                    imptvisitas = visitas.multiply(config.getImpVisita());
                }
                else 
                {
                    imptvisitas = costepros;
                }*/
                else 
                {
                    //Tras los cambios de sustitutos, no hay que limitar si supera el importe de los gastos salariales
                    imptvisitas = visitas.multiply(config.getImpVisita());
                }
                
                DecimalFormatSymbols simbolo=new DecimalFormatSymbols();
                simbolo.setDecimalSeparator(',');
                simbolo.setGroupingSeparator('.');
                DecimalFormat formateador = new DecimalFormat("#,##0.00",simbolo);
              
                fila.setVisitasImp(formateador.format(new BigDecimal(MeLanbide44Utils.redondearDecimalesString(imptvisitas, 2))));
                
                String parte = null;
                String mensajeCompleto = null;
                if(errores != null && errores.size() > 0)
                {
                    String[] partes = null;
                    String[] codigos = null;
                    List<String> erroresSinCod = new ArrayList<String>();
                    for(String str : errores)
                    {
                        partes = str.split(ConstantesMeLanbide44.BARRA_SEPARADORA);
                        codigos = partes[0].split(ConstantesMeLanbide44.SEPARADOR_VALORES_CONF);
                        for(String codigo : codigos)
                        {
                            fila.setErrorCampo(Integer.parseInt(codigo), ConstantesMeLanbide44.CIERTO);
                            for(int i = 1; i < partes.length; i++)
                            {
                                parte = partes[i];
                                if(mensajeCompleto == null)
                                {
                                    mensajeCompleto = new String(parte);
                                }
                                else
                                {
                                    mensajeCompleto += ConstantesMeLanbide44.BARRA_SEPARADORA+parte;
                                }
                            }
                            if(!erroresSinCod.contains(mensajeCompleto))
                            {
                                erroresSinCod.add(mensajeCompleto);
                            }
                            mensajeCompleto = null;
                        }
                    }
                   fila.setErrores(erroresSinCod);
                   //MeLanbide44Utils.formatearFilaPreparadorSolicitudVOError(fila);
                }
                
                if(fila.esSustituto() != null && fila.esSustituto())
                {
                    if(fila.getNif() != null && !fila.getNif().equals("") && !fila.getNif().equals("-"))
                    {
                        fila.setNif(fila.getNif()+" - "+"("+(fila.getTipoSust() != null && !fila.getTipoSust().equals("") ? fila.getTipoSust() : "S")+")");
                    }
                }
            }
            return retList;
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando lista preparadores", e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuï¿½rando lista preparadores", ex);
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
    }
    
    
    
    
    public List<FilaAuditoriaProcesosVO> filtrarAuditoriaProcesos(String nombre, Date feDesde, Date feHasta, Integer codProc, int codIdioma, AdaptadorSQLBD adaptador) throws Exception
    {
        List<FilaAuditoriaProcesosVO> retList = new ArrayList<FilaAuditoriaProcesosVO>();
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
            retList = meLanbide44DAO.filtrarAuditoriaProcesos(nombre, feDesde, feHasta, codProc, codIdioma, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion recuperando auditoria de procesos", e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion recuperando auditoria de procesos", ex);
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
                log.error("Error al cerrar conexiÃƒÂ³n a la BBDD: " + e.getMessage());
            }
        }
        return retList;
    }
    
    public int importarInserciones(String numExp, List<EcaSegPreparadoresVO> listaIns, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        int total = 0;
        try
        {
            EcaSolicitudVO sol = this.getDatosSolicitud(numExp, adaptador);
            if(sol != null && sol.getSolicitudCod() != null)
            {
                con = adaptador.getConnection();
                adaptador.inicioTransaccion(con);
                MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
                
                //Primero elimino todos los registros asociados a la solicitud
                meLanbide44DAO.eliminarTodosSeguimientosPrep_Solicitud(sol,ConstantesMeLanbide44.TIPO_INSERCION_MELANBIDE44, con);
                
                //Luego voy haciendo los insert uno a uno                
                for(EcaSegPreparadoresVO ins : listaIns)
                {
                    //seg.setSolicitud(sol.getSolicitudCod());
                    //guardar preparador -buscar para esta solicitud por nif de preparador
                    ins.setTipo(Integer.parseInt(ConstantesMeLanbide44.TIPO_INSERCION_MELANBIDE44));
                    if(meLanbide44DAO.guardarEcaSegPreparadoresVO(ins, con) != null)
                    {
                        total++;
                    }
                }
                adaptador.finTransaccion(con);
            }
            return total;
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD importando preparadores", e);
            adaptador.rollBack(con);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD importando preparadores", ex);
            adaptador.rollBack(con);
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
    }
 
    public int importarInserciones2016(String numExp, List<EcaSegPreparadores2016VO> listaIns, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        int total = 0;
        try
        {
            EcaSolicitudVO sol = this.getDatosSolicitud(numExp, adaptador);
            if(sol != null && sol.getSolicitudCod() != null)
            {
                con = adaptador.getConnection();
                adaptador.inicioTransaccion(con);
                MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
                
                //Primero elimino todos los registros asociados a la solicitud
                meLanbide44DAO.eliminarTodosSeguimientosPrep_Solicitud(sol,ConstantesMeLanbide44.TIPO_INSERCION_MELANBIDE44, con);
                
                //Luego voy haciendo los insert uno a uno                
                for(EcaSegPreparadores2016VO ins : listaIns)
                {
                    //seg.setSolicitud(sol.getSolicitudCod());
                    //guardar preparador -buscar para esta solicitud por nif de preparador
                    ins.setTipo(Integer.parseInt(ConstantesMeLanbide44.TIPO_INSERCION_MELANBIDE44));
                    if(meLanbide44DAO.guardarEcaSegPreparadoresVO_2016(ins, con) != null)
                    {
                        total++;
                    }
                }
                adaptador.finTransaccion(con);
            }
            return total;
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD importando preparadores", e);
            adaptador.rollBack(con);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD importando preparadores", ex);
            adaptador.rollBack(con);
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
    }
    
    public EcaSegPreparadoresVO getSeguimientoPreparadorPorId(EcaSegPreparadoresVO seg, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
            return meLanbide44DAO.getSeguimientoPreparadorPorId(seg, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando seguimiento del preparador "+(seg != null ? seg.getJusPreparadoresCod()+'_'+seg.getSegPreparadoresCod() : "(seguimiento = null)"), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando seguimiento del preparador "+(seg != null ? seg.getJusPreparadoresCod()+'_'+seg.getSegPreparadoresCod() : "(seguimiento = null)"), ex);
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
    }
    
    public List<FilaInformeDesglose> getDatosInformeDesglose(String ejercicio, AdaptadorSQLBD adaptador) throws Exception
    {
        List<FilaInformeDesglose> retList = new ArrayList<FilaInformeDesglose>();
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
            retList = meLanbide44DAO.getDatosInformeDesglose(ejercicio, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion recuperando datos del informe desglose", e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion recuperando datos del informe desglose", ex);
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
                log.error("Error al cerrar conexiÃƒÂ³n a la BBDD: " + e.getMessage());
            }
        }
        Collections.sort(retList);
        return retList;
    }
    public List<SelectItem> getListaTipoContrato(Integer idioma, AdaptadorSQLBD adaptador)  throws Exception {
         Connection con = null;
        List<SelectItem> lista=null;
        MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
        try{
            con = adaptador.getConnection();
            lista = meLanbide44DAO.getListaTipoContrato( idioma, con);
        }catch(BDException e)
        {
            log.error("Se ha producido una excepcion recuperando lista tipo de contratos", e);
            throw new Exception(e);
        }catch(Exception e){
            log.debug("Error al obtener los tipos de contrato."+e.getMessage());
            throw new Exception(e);
        }finally
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
        return lista;
    }
    
    public List<SelectItem> getListaTipoDiscapacidad(Integer idioma, AdaptadorSQLBD adaptador) throws Exception{
        Connection con = null;
        List<SelectItem> lista=new ArrayList<SelectItem>();
        MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
        try{
            con = adaptador.getConnection();
            lista = meLanbide44DAO.getListaTipodiscapacidad(idioma, con);
        }catch(BDException e)
        {
            log.error("Se ha producido una excepcion recuperando lista tipo de discapacidad", e);
            throw new Exception(e);
        }catch(Exception e){
            log.debug("Error al obtener los tipos de contrato."+e.getMessage());
            throw new Exception(e);
        }finally
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
        return lista;
    }
    
    public List<SelectItem> getListaGravedad(Integer idioma, AdaptadorSQLBD adaptador) throws Exception{
        Connection con = null;
        List<SelectItem> lista=new ArrayList<SelectItem>();
        MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
        try{
            con = adaptador.getConnection();
            lista = meLanbide44DAO.getListaGravedad(idioma, con);
        }catch(BDException e)
        {
            log.error("Se ha producido una excepcion recuperando lista gravedad", e);
            throw new Exception(e);
        }
        catch(Exception e){
            log.debug("Error al obtener los tipos de gravedad."+e.getMessage());
            throw new Exception(e);
        }finally
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
        return lista;
    }
    
    public EcaSegPreparadoresVO guardarEcaSegPreparadoresVO(EcaSegPreparadoresVO seg, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
            return meLanbide44DAO.guardarEcaSegPreparadoresVO(seg, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD guardando seguimiento de preparador  "+(seg != null ? seg.getJusPreparadoresCod()+"_"+seg.getSegPreparadoresCod(): "(seguimiento = null)"), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD guardando seguimiento de preparador "+(seg != null ? seg.getJusPreparadoresCod()+"_"+seg.getSegPreparadoresCod() : "(seguimiento = null)"), ex);
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
    }
    
    public int eliminarSegPreparador(EcaSegPreparadoresVO seg, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
            return meLanbide44DAO.eliminarSegPreparador(seg, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD eliminando seguimiento preparador solicitud "+(seg != null ? seg.getJusPreparadoresCod()+"_"+seg.getSegPreparadoresCod() : "(seguimiento = null)"), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD eliminandos seguimiento preparador solicitud "+(seg != null ? seg.getJusPreparadoresCod()+"_"+seg.getSegPreparadoresCod() : "(seguimiento = null)"), ex);
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
    }
    
    public EcaJusProspectoresVO getProspectorJustificacionPorId(EcaJusProspectoresVO pros, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
            return meLanbide44DAO.getProspectorJustificacionPorId(pros, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando prospector justificacion "+(pros != null ? pros.getJusProspectoresCod() : "(prospector = null)"), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando prospector justificacion "+(pros != null ? pros.getJusProspectoresCod() : "(prospector = null)"), ex);
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
    }
    
    public EcaJusProspectoresVO guardarEcaJusProspectoresVO(EcaJusProspectoresVO pros, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
            return meLanbide44DAO.guardarEcaJusProspectoresVO(pros, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD guardando prospector justificacion "+(pros != null ? pros.getSolProspectoresCod() : "(prospector = null)"), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD guardando prospector justificacion "+(pros != null ? pros.getSolProspectoresCod() : "(prospector = null)"), ex);
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
    }
    
    public int eliminarProspectorJustificacion(EcaJusProspectoresVO pros, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            adaptador.inicioTransaccion(con);
            MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
            int pos = 0;
            List<Integer> listaE = new ArrayList<Integer>();
            listaE.add(pros.getJusProspectoresCod());
            buscarProsElim(listaE, pros, pos, con);
            int result = 0;
            for(Integer i : listaE)
            {
                pros = new EcaJusProspectoresVO();
                pros.setJusProspectoresCod(i);
                pros = meLanbide44DAO.getProspectorJustificacionPorId(pros, con);
                meLanbide44DAO.eliminarTodasVisitasProspector(pros, con);
                result += meLanbide44DAO.eliminarProspectorJustificacion(pros, con);
            }
            adaptador.finTransaccion(con);
            return result;
        }
        catch(BDException e)
        {
            adaptador.rollBack(con);
            log.error("Se ha producido una excepcion en la BBDD eliminando prospector justificacion "+(pros != null ? pros.getJusProspectoresCod() : "(prospector = null)"), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            adaptador.rollBack(con);
            log.error("Se ha producido una excepcion en la BBDD eliminando prospector justificacion "+(pros != null ? pros.getJusProspectoresCod() : "(prospector = null)"), ex);
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
    }
    
    public List<FilaVisProspectoresVO> getListaVisitas(EcaSolicitudVO sol, EcaJusProspectoresVO prep, Integer idioma, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {            
            con = adaptador.getConnection();
            MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
            List<FilaVisProspectoresVO> retList =  meLanbide44DAO.getListaVisitas(sol, prep, idioma, con);
            EcaConfiguracionVO config = this.getConfiguracionEca(MeLanbide44Utils.getEjercicioDeExpediente(sol.getNumExp()), adaptador);
            
            List<Integer> listaSector =  meLanbide44DAO.getListaCodigosSectorActividad( con);
            List<Integer> listaProvincia =  meLanbide44DAO.getListaCodigosProvincias( con);
            List<Integer> listaCumple =  meLanbide44DAO.getListaCodigosCumple(con);
            List<Integer> listaResultado =  meLanbide44DAO.getListaCodigosResultado(con);
            
            for(FilaVisProspectoresVO fila : retList)
            {
                List<String> errores = MeLanbide44Utils.validarFilaVisitaPros(sol.getNumExp(),fila,listaSector, listaProvincia, listaCumple, listaResultado, config, idioma);
                
                
                //Valido la fecha de visita por si hay que añadir el error.-
                if(fila.getFecVisita() != null && !fila.getFecVisita().equals("") && !fila.getFecVisita().equals("-"))
                {
                    EcaJusProspectoresVO pros = null;
                    try
                    {
                        SimpleDateFormat formatoDeFecha = new SimpleDateFormat("dd/MM/yyyy");
                        pros = meLanbide44DAO.getProspectorJustificacionPorNIF(sol, fila.getNifProspector(), formatoDeFecha.parse(fila.getFecVisita()), con);
                    }
                    catch(Exception ex)
                    {
                        ex.printStackTrace();
                    }
                    if(pros == null)
                    {
                        MeLanbide44I18n traductor = MeLanbide44I18n.getInstance();
                        String mensaje = FilaVisProspectoresVO.POS_CAMPO_NIFPROSPECTOR+ConstantesMeLanbide44.BARRA_SEPARADORA+traductor.getMensaje(idioma, "error.NifProspectorNoEncontrado");
                        if(!errores.contains(mensaje))
                        {
                            errores.add(mensaje);
                        }
                    }
                }
                
                if(errores != null && errores.size() > 0)
                {
                    String[] partes = null;
                    String[] codigos = null;
                    List<String> erroresSinCod = new ArrayList<String>();
                    for(String str : errores)
                    {
                        partes = str.split(ConstantesMeLanbide44.BARRA_SEPARADORA);
                        codigos = partes[0].split(ConstantesMeLanbide44.SEPARADOR_VALORES_CONF);
                        for(String codigo : codigos)
                        {
                            fila.setErrorCampo(Integer.parseInt(codigo), ConstantesMeLanbide44.CIERTO);
                            erroresSinCod.add(partes[1]);
                        }
                    }
                   fila.setErrores(erroresSinCod);                   
                }
            }
            return retList;
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando lista preparadores de solicitud "+(sol != null ? sol.getSolicitudCod() : "(solicitud = null)"), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando lista preparadores de solicitud "+(sol != null ? sol.getSolicitudCod() : "(solicitud = null)"), ex);
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
    }
    
    public int importarVisitas(String numExp, List<EcaVisProspectoresVO> listaVis, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        int total = 0;
        try
        {
            EcaSolicitudVO sol = this.getDatosSolicitud(numExp, adaptador);
            if(sol != null && sol.getSolicitudCod() != null)
            {
                con = adaptador.getConnection();
                adaptador.inicioTransaccion(con);
                MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
                
                //Primero elimino todos los registros asociados a la solicitud
                
                meLanbide44DAO.eliminarTodasVisitasPros(sol, con);
                
                Statement stm = con.createStatement();
                //Luego voy haciendo los insert uno a uno                
                for(EcaVisProspectoresVO vis : listaVis)
                {                    
                    //guardar visita              
                    if(meLanbide44DAO.guardarEcaVisProspectoresVO(vis, stm, con) != null)
                    {
                        total++;
                    }
                }
                if(stm!=null) 
                    stm.close();
                adaptador.finTransaccion(con);
            }
            return total;
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD importando preparadores", e);
            adaptador.rollBack(con);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD importando preparadores", ex);
            adaptador.rollBack(con);
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
    }
    
    public int importarVisitas2016(String numExp, List<EcaVisProspectores2016VO> listaVis, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        int total = 0;
        try
        {
            EcaSolicitudVO sol = this.getDatosSolicitud(numExp, adaptador);
            if(sol != null && sol.getSolicitudCod() != null)
            {
                con = adaptador.getConnection();
                adaptador.inicioTransaccion(con);
                MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
                
                //Primero elimino todos los registros asociados a la solicitud
                
                meLanbide44DAO.eliminarTodasVisitasPros(sol, con);
                
                Statement stm = con.createStatement();
                //Luego voy haciendo los insert uno a uno                
                for(EcaVisProspectores2016VO vis : listaVis)
                {                    
                    //guardar visita              
                    if(meLanbide44DAO.guardarEcaVisProspectoresVO_2016(vis, stm, con) != null)
                    {
                        total++;
                    }
                }
                if(stm!=null) 
                    stm.close();
                adaptador.finTransaccion(con);
            }
            return total;
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD importando preparadores", e);
            adaptador.rollBack(con);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD importando preparadores", ex);
            adaptador.rollBack(con);
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
    }
    
    public EcaJusProspectoresVO getProspectorJustificacionPorNIF(EcaSolicitudVO sol, String nif, Date fecha, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
            return meLanbide44DAO.getProspectorJustificacionPorNIF(sol, nif,fecha,  con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando prospector solicitud nif:"+nif+", "+(sol != null ? sol.getSolicitudCod() : "(solicitud = null)"), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando prospector solicitud nif:"+nif+", "+(sol != null ? sol.getSolicitudCod() : "(solicitud = null)"), ex);
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
    }
    
    /*public List<FilaInformeProyectos> getDatosInformeProyectos(String ejercicio, AdaptadorSQLBD adaptador) throws Exception
    {
        List<FilaInformeProyectos> retList = new ArrayList<FilaInformeProyectos>();
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
            retList = meLanbide44DAO.getDatosInformeProyectos(ejercicio, con);
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
                log.error("Error al cerrar conexiÃƒÂ³n a la BBDD: " + e.getMessage());
            }
        }
        return retList;
    }*/
    
    public List<FilaResumenInformeProyectos> getDatosResumenInformeProyectos(String ejercicio, AdaptadorSQLBD adaptador) throws Exception
    {
        List<FilaResumenInformeProyectos> retList = new ArrayList<FilaResumenInformeProyectos>();
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
            retList = meLanbide44DAO.getDatosResumenInformeProyectos(ejercicio, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion recuperando datos resumen del informe proyectos", e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion recuperando datos resumen del informe proyectos", ex);
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
                log.error("Error al cerrar conexiÃƒÂ³n a la BBDD: " + e.getMessage());
            }
        }
        Collections.sort(retList);
        return retList;
    }
    
    public List<FilaInformeProyectos> getDatosInformeProyectos(String numExp, AdaptadorSQLBD adaptador) throws Exception
    {
        List<FilaInformeProyectos> retList = new ArrayList<FilaInformeProyectos>();
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
            
            FilaInformeProyectos filaAct = null;
            FilaInformeProyectos filaAux3 = null;
            List<FilaInformeProyectos> listaParte2 = meLanbide44DAO.getDatosInformeProyectosParte2(numExp, con);
            List<FilaInformeProyectos> listaParte3 = meLanbide44DAO.getDatosInformeProyectosParte3(numExp, con);
            
            int posEnLista = 0;
            
            for(FilaInformeProyectos fila : listaParte2)
            {
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
                
                if(posEnLista >= 0)
                {
                    filaAux3 = listaParte3.get(posEnLista);
                    if(filaAux3 != null)
                    {
                        filaAct.setTotalPreparadores(filaAux3.getTotalPreparadores());
                        filaAct.setPrepIndefinido(filaAux3.getPrepIndefinido());
                        filaAct.setPrepTemporal(filaAux3.getPrepTemporal());
                    }
                }
                retList.add(filaAct);
            }
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
                log.error("Error al cerrar conexiÃƒÂ³n a la BBDD: " + e.getMessage());
            }
        }
        return retList;
    }
    
    
    public EcaVisProspectoresVO getVisitaProspectorPorId(EcaVisProspectoresVO vis, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
            return meLanbide44DAO.getVisitaProspectorPorId(vis, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando visita del prospector "+(vis != null ? vis.getJusProspectoresCod()+'_'+vis.getVisProspectoresCod() : "(visita = null)"), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando visita del prospector "+(vis != null ? vis.getJusProspectoresCod()+'_'+vis.getVisProspectoresCod() : "(visita = null)"), ex);
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
    }
    
    public List<SelectItem> getListaSectorActividad(Integer idioma, AdaptadorSQLBD adaptador)  throws Exception {
         Connection con = null;
        List<SelectItem> lista=null;
        MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
        try{
            con = adaptador.getConnection();
            lista = meLanbide44DAO.getListaSectorActividad( idioma, con);
        }catch(BDException e)
        {
            log.error("Se ha producido una excepcion recuperando lista sectores de actividad", e);
            throw new Exception(e);
        }catch(Exception e){
            log.debug("Error al obtener los sectores de actividad."+e.getMessage());
            throw new Exception(e);
        }finally
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
        return lista;
    }
    
    public List<SelectItem> getListaProvincias(Integer idioma, AdaptadorSQLBD adaptador)  throws Exception {
         Connection con = null;
        List<SelectItem> lista=null;
        MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
        try{
            con = adaptador.getConnection();
            lista = meLanbide44DAO.getListaProvincias( idioma, con);
        }catch(BDException e)
        {
            log.error("Se ha producido una excepcion recuperando lista provincias", e);
            throw new Exception(e);
        }catch(Exception e){
            log.debug("Error al obtener las provincias."+e.getMessage());
            throw new Exception(e);
        }finally
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
        return lista;
    }
    
    public List<SelectItem> getListaCumpleLismi(Integer idioma, AdaptadorSQLBD adaptador)  throws Exception {
         Connection con = null;
        List<SelectItem> lista=null;
        MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
        try{
            con = adaptador.getConnection();
            lista = meLanbide44DAO.getListaCumpleLismi( idioma, con);
        }catch(BDException e)
        {
            log.error("Se ha producido una excepcion recuperando lista cumple Lismi", e);
            throw new Exception(e);
        }catch(Exception e){
            log.debug("Error al obtener lista cumple Lismi."+e.getMessage());
            throw new Exception(e);
        }finally
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
        return lista;
    }
    
    public List<SelectItem> getListaResultadoFinal(Integer idioma, AdaptadorSQLBD adaptador)  throws Exception {
         Connection con = null;
        List<SelectItem> lista=null;
        MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
        try{
            con = adaptador.getConnection();
            lista = meLanbide44DAO.getListaResultadoFinal( idioma, con);
        }catch(BDException e)
        {
            log.error("Se ha producido una excepcion recuperando lista resultado Final", e);
            throw new Exception(e);
        }catch(Exception e){
            log.debug("Error al obtener lista resultado Final."+e.getMessage());
            throw new Exception(e);
        }finally
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
        return lista;
    }

    public EcaVisProspectoresVO guardarEcaVisProspectoresVO(EcaVisProspectoresVO vis, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        Statement stm =null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
            stm = con.createStatement();
            return meLanbide44DAO.guardarEcaVisProspectoresVO(vis, stm, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD guardando visita de prospector  "+(vis != null ? vis.getJusProspectoresCod()+"_"+vis.getVisProspectoresCod(): "(visita = null)"), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD guardando seguimiento de preparador "+(vis != null ? vis.getJusProspectoresCod()+"_"+vis.getVisProspectoresCod() : "(visita = null)"), ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if (stm!=null)
                    stm.close();
                adaptador.devolverConexion(con);       
            }
            catch(Exception e)
            {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
    
    
    public List<Integer> getListaCodigosProvincia(AdaptadorSQLBD adaptador) throws Exception
    {
        
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
            return meLanbide44DAO.getListaCodigosProvincias(con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando codigos provincia", e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando codigos provincia", ex);
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
    }
    
    public List<Integer> getListaCodigosCumple(AdaptadorSQLBD adaptador) throws Exception
    {
        
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
            return meLanbide44DAO.getListaCodigosCumple(con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando codigos cumple", e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando codigos cumple", ex);
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
    }
    
    public List<Integer> getListaCodigosResultado(AdaptadorSQLBD adaptador) throws Exception
    {
        
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
            return meLanbide44DAO.getListaCodigosResultado(con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando codigos resultado", e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando codigos resultado", ex);
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
    }
    
    public List<Integer> getListaCodigosSectorActividad(AdaptadorSQLBD adaptador) throws Exception
    {
        
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
            return meLanbide44DAO.getListaCodigosSectorActividad(con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando codigos codigos sector", e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando codigos sector", ex);
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
    }
    
    
    public List<Integer> getListaCodigosTipoContrato(AdaptadorSQLBD adaptador) throws Exception
    {
        
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
            return meLanbide44DAO.getListaCodigosTipoContrato(con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando codigos tipo de contrato", e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando codigos tipo de contrato", ex);
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
    }
    
    public List<Integer> getListaCodigosTipoDiscapacidad(AdaptadorSQLBD adaptador) throws Exception
    {
        
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
            return meLanbide44DAO.getListaCodigosTipoDiscapacidad(con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando codigos tipo de discapacidad", e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando codigos tipo de discapacidad", ex);
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
    }
    
    public List<Integer> getListaCodigosGravedad(AdaptadorSQLBD adaptador) throws Exception
    {
        
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
            return meLanbide44DAO.getListaCodigosGravedad(con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando codigos gravedad", e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando codigos gravedad", ex);
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
    }
    
     public EcaSolPreparadoresVO getPreparadorSolicitudPorNIF(EcaSolicitudVO sol, String nif, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
            return meLanbide44DAO.getPreparadorSolicitudPorNIF(sol, nif, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando preparador solicitud nif:"+nif+", "+(sol != null ? sol.getSolicitudCod() : "(solicitud = null)"), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando preparador solicitud nif:"+nif+", "+(sol != null ? sol.getSolicitudCod() : "(solicitud = null)"), ex);
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
    }
    
     public EcaSolProspectoresVO getProspectorSolicitudPorNIF(EcaSolicitudVO sol, String nif, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
            return meLanbide44DAO.getProspectorSolicitudPorNIF(sol, nif, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando prospector solicitud nif:"+nif+", "+(sol != null ? sol.getSolicitudCod() : "(solicitud = null)"), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando prospector solicitud nif:"+nif+", "+(sol != null ? sol.getSolicitudCod() : "(solicitud = null)"), ex);
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
    }
    
    public EcaSolProspectoresVO getProspectorSolicitudSustituto(Integer idPros, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
            return meLanbide44DAO.getProspectorSolicitudSustituto(idPros, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando sustituto prospector:"+idPros, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando sustituto prospector:"+idPros, ex);
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
    } 
    
    public EcaSolPreparadoresVO getPreparadorSolicitudSustituto(Integer idPrep, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
            return meLanbide44DAO.getPreparadorSolicitudSustituto(idPrep, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando sustituto preparador:"+idPrep, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando sustituto preparador:"+idPrep, ex);
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
    } 


    public int eliminarVisitaProspectorJustificacion(EcaVisProspectoresVO vis, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
            return meLanbide44DAO.eliminarVisitaProspectorJustificacion(vis, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD eliminando visita : "+(vis != null ? "codigo vis = "+vis.getVisProspectoresCod()+" codigo pros = "+vis.getJusProspectoresCod() : " (visita = null)"), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD eliminando visita : "+(vis != null ? "codigo vis = "+vis.getVisProspectoresCod()+" codigo pros = "+vis.getJusProspectoresCod() : " (visita = null)"), ex);
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
    }

    public Map<String, Integer> getNumeroSeguimientosInsercionesPreparador(EcaJusPreparadoresVO prep, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
            return meLanbide44DAO.getNumeroSeguimientosInsercionesPreparador(prep, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando numero seguimientos preparador : "+(prep != null && prep.getJusPreparadoresCod() != null ? prep.getJusPreparadoresCod() : "null"), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando numero seguimientos preparador : "+(prep != null && prep.getJusPreparadoresCod() != null ? prep.getJusPreparadoresCod() : "null"), ex);
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
    }

    public Integer getNumeroVisitasProspector(EcaJusProspectoresVO pros, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
            return meLanbide44DAO.getNumeroVisitasProspector(pros, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando numero visitas prospector : "+(pros != null && pros.getJusProspectoresCod() != null ? pros.getJusProspectoresCod() : "null"), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando numero visitas prospector : "+(pros != null && pros.getJusProspectoresCod() != null ? pros.getJusProspectoresCod() : "null"), ex);
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
    }
    
    public boolean hayPreparadoresParaCopiar(String numExp, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            EcaSolicitudVO sol = this.getDatosSolicitud(numExp, adaptador);
            if(sol != null && sol.getSolicitudCod() != null)
            {
                con = adaptador.getConnection();
                MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
                
                return meLanbide44DAO.hayPreparadoresParaCopiar(sol, con);
            }
            return false;
        }
        catch(BDException e)
        {
            log.error("Se ha producido un error comprobando si hay preparadores para copiar", e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error comprobando si hay preparadores para copiar", ex);
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
    }
    
    public int copiarPreparadores(String numExp, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        int resultado = 0;
        try
        {
            EcaSolicitudVO sol = this.getDatosSolicitud(numExp, adaptador);
            if(sol != null && sol.getSolicitudCod() != null)
            {
                con = adaptador.getConnection();
                adaptador.inicioTransaccion(con);
                MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
                
                //Primero elimino todos los registros asociados a la solicitud
                meLanbide44DAO.eliminarTodosSeguimientosPrep_Solicitud(sol, "0", con);
                meLanbide44DAO.eliminarTodosSeguimientosPrep_Solicitud(sol, "1", con);
                meLanbide44DAO.eliminarTodosPreparadoresJus(sol, con);                
                resultado=meLanbide44DAO.copiarPreparadoresFromSolToJus(sol, con);
                adaptador.finTransaccion(con);
            }
            return resultado;
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD importando preparadores", e);
            adaptador.rollBack(con);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD importando preparadores", ex);
            adaptador.rollBack(con);
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
    }
    
    public boolean hayProspectoresParaCopiar(String numExp, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            EcaSolicitudVO sol = this.getDatosSolicitud(numExp, adaptador);
            if(sol != null && sol.getSolicitudCod() != null)
            {
                con = adaptador.getConnection();
                MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
                
                return meLanbide44DAO.hayProspectoresParaCopiar(sol, con);
            }
            return false;
        }
        catch(BDException e)
        {
            log.error("Se ha producido un error comprobando si hay prospectores para copiar", e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error comprobando si hay prospectores para copiar", ex);
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
    }
    
    public int copiarProspectores(String numExp, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        int resultado = 0;
        try
        {
            EcaSolicitudVO sol = this.getDatosSolicitud(numExp, adaptador);
            if(sol != null && sol.getSolicitudCod() != null)
            {
                con = adaptador.getConnection();
                adaptador.inicioTransaccion(con);
                MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
                
                //Primero elimino todos los registros asociados a la solicitud
                meLanbide44DAO.eliminarTodasVisitasPros(sol, con);               
                meLanbide44DAO.eliminarTodosProspectoresJus(sol, con);                
                resultado=meLanbide44DAO.copiarProspectoresFromSolToJus(sol, con);
                adaptador.finTransaccion(con);
            }
            return resultado;
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD importando preparadores", e);
            adaptador.rollBack(con);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD importando preparadores", ex);
            adaptador.rollBack(con);
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
    }
     
     private void buscarPrepElim(List<Integer> listaE, EcaSolPreparadoresVO prep, int pos, Connection con) throws Exception
     {
         List<EcaSolPreparadoresVO> listaS = MeLanbide44DAO.getInstance().getSustitutosPreparadorSolicitud(prep.getSolPreparadoresCod(), con);
         if(listaS != null && listaS.size() > 0)
         {
             while(pos < listaS.size())
             {
                 prep = listaS.get(pos);
                 if(!listaE.contains(prep.getSolPreparadoresCod()))
                 {
                     listaE.add(prep.getSolPreparadoresCod());
                 }
                 buscarPrepElim(listaE, prep, pos, con);
                 pos++;
             }
         }
     }
     
     private void buscarProsElim(List<Integer> listaE, EcaSolProspectoresVO pros, int pos, Connection con) throws Exception
     {
         List<EcaSolProspectoresVO> listaS = MeLanbide44DAO.getInstance().getSustitutosProspectorSolicitud(pros.getSolProspectoresCod(), con);
         if(listaS != null && listaS.size() > 0)
         {
             while(pos < listaS.size())
             {
                 pros = listaS.get(pos);
                 if(!listaE.contains(pros.getSolProspectoresCod()))
                 {
                     listaE.add(pros.getSolProspectoresCod());
                 }
                 buscarProsElim(listaE, pros, pos, con);
                 pos++;
             }
         }
     }
     
     private void buscarPrepElim(List<Integer> listaE, EcaJusPreparadoresVO prep, int pos, Connection con) throws Exception
     {
         List<EcaJusPreparadoresVO> listaS = MeLanbide44DAO.getInstance().getSustitutosPreparadorJustificacion(prep.getJusPreparadoresCod(), con);
         if(listaS != null && listaS.size() > 0)
         {
             while(pos < listaS.size())
             {
                 prep = listaS.get(pos);
                 if(!listaE.contains(prep.getJusPreparadoresCod()))
                 {
                     listaE.add(prep.getJusPreparadoresCod());
                 }
                 buscarPrepElim(listaE, prep, pos, con);
                 pos++;
             }
         }
     }
     
     private void buscarProsElim(List<Integer> listaE, EcaJusProspectoresVO pros, int pos, Connection con) throws Exception
     {
         List<EcaJusProspectoresVO> listaS = MeLanbide44DAO.getInstance().getSustitutosProspectorJustificacion(pros.getJusProspectoresCod(), con);
         if(listaS != null && listaS.size() > 0)
         {
             while(pos < listaS.size())
             {
                 pros = listaS.get(pos);
                 if(!listaE.contains(pros.getJusProspectoresCod()))
                 {
                     listaE.add(pros.getJusProspectoresCod());
                 }
                 buscarProsElim(listaE, pros, pos, con);
                 pos++;
             }
         }
     }
     
public List<FilaEcaResProspectoresVO> getListaDetalleProspectoresResumen(String numExp, int codIdioma, AdaptadorSQLBD adaptador) throws Exception
{
    Connection con = null;
    try
    {
        DecimalFormatSymbols simbolo=new DecimalFormatSymbols();
        simbolo.setDecimalSeparator(',');
        simbolo.setGroupingSeparator('.');
        DecimalFormat formateador = new DecimalFormat("#,##0.00",simbolo);
        con = adaptador.getConnection();
        MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
        List<FilaEcaResProspectoresVO> retList = new ArrayList<FilaEcaResProspectoresVO>();
        EcaSolicitudVO sol = getDatosSolicitud(numExp, adaptador);
        List<FilaVisProspectoresVO> listaVis = null;
        if (sol != null)
        {
            EcaConfiguracionVO config = this.getConfiguracionEca(MeLanbide44Utils.getEjercicioDeExpediente(sol.getNumExp()), adaptador);
            retList = meLanbide44DAO.getListaDetalleProspectoresResumen(numExp, con);

            EcaJusProspectoresVO pros = null;
            FilaEcaResProspectoresVO fila = null;
                        
                        
            BigDecimal imptvisitas=BigDecimal.ZERO;
            BigDecimal minimo = BigDecimal.ZERO;
            BigDecimal maximo = BigDecimal.ZERO;
            BigDecimal visitas = BigDecimal.ZERO;
            
            int num_visitas = 0;
            for (int i = 0; i < retList.size(); i++)
            {
                fila = (FilaEcaResProspectoresVO)retList.get(i);
                if (fila.getTipoSust() != null)
                {
                    //fila.setImpPagar(formateador.format(new BigDecimal( "0.00")));
                } 
                else 
                {
                    if (fila.getEcaJusProspectoresCod() != null)
                    {
                        pros = new EcaJusProspectoresVO();
                        pros.setJusProspectoresCod(Integer.valueOf(fila.getEcaJusProspectoresCod().intValue()));
                        pros = this.getProspectorJustificacionPorId(pros, adaptador);
                        if(pros != null)
                        {
                            listaVis = getListaVisitas(sol, pros, Integer.valueOf(codIdioma), adaptador);
                            num_visitas = listaVis.size();
                            for (FilaVisProspectoresVO vis : listaVis)
                            {
                                if ((vis.getErrores() != null) && (vis.getErrores().size() > 0))
                                {
                                    //fila.setImpPagar(formateador.format(new BigDecimal( "0.00")));
                                    num_visitas--;
                                }
                            }
                            
                            visitas = new BigDecimal(num_visitas);

                            try
                            {
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
                            }
                            catch(Exception ex)
                            {
                                //ex.printStackTrace();
                            }


                            if (visitas.compareTo(minimo)<0 ) 
                            {
                                imptvisitas=BigDecimal.ZERO;
                            }
                            else if ((visitas.compareTo(maximo) > 0)  && ((maximo.multiply(config.getImpVisita())).compareTo(pros.getImpSSECA()) < 0 ))
                            {
                                imptvisitas = maximo.multiply(config.getImpVisita());
                            }
                            else 
                            {
                                //Tras los cambios de sustitutos, no hay que limitar si supera el importe de los gastos salariales
                                imptvisitas = visitas.multiply(config.getImpVisita());
                            }
                            
                            if(pros.getImpSSECA() != null && pros.getImpSSECA().compareTo(imptvisitas) < 0)
                            {
                                imptvisitas = pros.getImpSSECA();
                            }

                        }

                        
                        
                        //fila.setImpPagar(formateador.format(imptvisitas));
                        
                    }
                }
                
                num_visitas = 0;
                imptvisitas=BigDecimal.ZERO;
                minimo = BigDecimal.ZERO;
                maximo = BigDecimal.ZERO;
                visitas = BigDecimal.ZERO;
            }
        }
        return retList;
    }
    catch (BDException e)
    {
        log.error(new StringBuilder().append("Se ha producido una excepcion en la BBDD recuperando lista detalle prospectores resumen para expediente ").append(numExp).toString(), e);
        throw new Exception(e);
    }
    catch (Exception ex)
    {
        log.error(new StringBuilder().append("Se ha producido una excepcion en la BBDD recuperando lista detalle prospectores resumen para expediente ").append(numExp).toString(), ex);
        throw new Exception(ex);
    }
    finally
    {
        try
        {
          adaptador.devolverConexion(con);
        }
        catch (Exception e)
        {
          log.error(new StringBuilder().append("Error al cerrar conexion a la BBDD: ").append(e.getMessage()).toString());
        }
    }
}
     
     public List<FilaEcaResPreparadoresVO> getListaDetallePreparadoresResumen(String numExp, AdaptadorSQLBD adaptador) throws Exception
     {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
            return meLanbide44DAO.getListaDetallePreparadoresResumen(numExp, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando lista detalle preparadores resumen para expediente "+numExp, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando lista detalle preparadores resumen para expediente "+numExp, ex);
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
     }
     
     public EcaResDetalleInsercionesVO cargarDetalleInsercionesPrep(String ejercicio, EcaJusPreparadoresVO prep, AdaptadorSQLBD adaptador) throws Exception
     {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
            return meLanbide44DAO.cargarDetalleInsercionesPrep(ejercicio, prep, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando detalle inserciones resumen para ejercicio "+ejercicio+" y preparador "+prep.getJusPreparadoresCod(), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando detalle inserciones resumen para ejercicio "+ejercicio+" y preparador "+prep.getJusPreparadoresCod(), ex);
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
     }
     
     public String actualizarDatosResumen(String numExp, AdaptadorSQLBD adaptador) throws Exception
     {
        Connection con = null;
        String codigo = null;
        Integer res = 0;
        String mensaje = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
            
            try
            {
                codigo = meLanbide44DAO.obtieneSeguimientosCorrectos(numExp, con);
                if(codigo != null && codigo.equals("0"))
                {
                    try
                    {
                        codigo = meLanbide44DAO.obtieneVisitasCorrectas(numExp, con);
                        if(codigo != null && codigo.equals("0"))
                        {
                            try
                            {
                                codigo = meLanbide44DAO.cargaResumenECA(numExp, con);
                                if(codigo != null && codigo.equalsIgnoreCase(ConstantesMeLanbide44.OK))
                                {
                                }
                                else
                                {
                                    res = 3;
                                }
                            }
                            catch(Exception ex)
                            {
                                res = 3;
                                log.error("Se ha producido una excepcion en la BBDD ejecutando proceso CARGA_RESUMEN_ECA para numExpediente "+numExp, ex);
                            }
                        }
                        else
                        {
                            res = 2;
                        }
                    }
                    catch(Exception ex)
                    {
                        res = 2;
                        log.error("Se ha producido una excepcion en la BBDD ejecutando proceso obtiene_visitas_correctas para numExpediente "+numExp, ex);
                    }
                }
                else
                {
                    res = 1;
                }
            }
            catch(Exception ex)
            {
                res = 1;
                log.error("Se ha producido una excepcion en la BBDD ejecutando proceso obtiene_seguimientos_correctos para numExpediente "+numExp, ex);
            }
            
            switch(res)
            {
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
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD actualizando datos resumen para numExpediente "+numExp, e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD actualizando datos resumen para numExpedient "+numExp, ex);
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
        //return mensaje;
        return codigo;
     }
     
     public EcaResumenVO getResumenSolicitud(EcaSolicitudVO solicitud, AdaptadorSQLBD adaptador) throws Exception
     {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            return MeLanbide44DAO.getInstance().getResumenSolicitud(solicitud, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos resumen para solicitud "+solicitud, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos resumen para solicitud "+solicitud, ex);
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
     }
    
    
    public EcaResumenVO guardarResumenSolicitud(EcaResumenVO res, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
            return meLanbide44DAO.guardarResumenSolicitud(res, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD guardando resumen de la solicitud "+(res != null && res.getSolicitud() != null ? res.getSolicitud().toString() : ""), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD guardando resumen de la solicitud "+(res != null && res.getSolicitud() != null ? res.getSolicitud().toString() : ""), ex);
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
    }
    
    public List<EcaSolPreparadoresVO> getSustitutosPreparadorSolicitud(Integer codPrep, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
            return meLanbide44DAO.getSustitutosPreparadorSolicitud(codPrep, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando sustitutos solicitud para preparador "+codPrep, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando sustitutos solicitud para preparador "+codPrep, ex);
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
    }
    
    public List<EcaSolProspectoresVO> getSustitutosProspectorSolicitud(Integer codPrep, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
            return meLanbide44DAO.getSustitutosProspectorSolicitud(codPrep, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando sustitutos solicitud para prospector "+codPrep, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando sustitutos solicitud para prospector "+codPrep, ex);
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
    }
    
    public List<EcaJusPreparadoresVO> getSustitutosPreparadorJustificacion(Integer codPrep, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
            return meLanbide44DAO.getSustitutosPreparadorJustificacion(codPrep, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando sustitutos justificacion para preparador "+codPrep, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando sustitutos justificacion para preparador "+codPrep, ex);
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
    }
    
    public List<EcaJusProspectoresVO> getSustitutosProspectorJustificacion(Integer codPrep, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide44DAO meLanbide44DAO = MeLanbide44DAO.getInstance();
            return meLanbide44DAO.getSustitutosProspectorJustificacion(codPrep, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando sustitutos justificacion para prospector "+codPrep, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando sustitutos justificacion para prospector "+codPrep, ex);
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
    }
}