
package es.altia.flexia.integracion.moduloexterno.melanbide32.manager;

import es.altia.flexia.integracion.moduloexterno.melanbide32.dao.OriCECriteriosCentroDAO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.dao.OriCECriteriosEvaDAO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.centroempleo.CeUbicacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.centroempleo.OriCECriteriosCentro;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.centroempleo.OriCECriteriosCentroDTO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.centroempleo.OriCECriteriosEvaDTO;
import java.sql.Connection;
import java.util.List;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author INGDGC
 */
public class MeLanbide32ManagerCriteriosSeleccionCE extends MeLanbide32Manager {
    
    private static Logger log = LogManager.getLogger(MeLanbide32ManagerCriteriosSeleccionCE.class);
    SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    private static OriCECriteriosEvaDAO oriCECriteriosEvaDAO = new OriCECriteriosEvaDAO();
    private static OriCECriteriosCentroDAO oriCECriteriosCentroDAO  = new OriCECriteriosCentroDAO();
    
    public List<OriCECriteriosEvaDTO> getOriCECriteriosEvaDTOByCodigo(String codigoCriterio, Integer ejercicioConvocatoria, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return oriCECriteriosEvaDAO.getOriCECriteriosEvaDTOByCodigo(codigoCriterio, ejercicioConvocatoria, con);
        } catch (BDException e) {
            log.error("Se ha producido BDException  al getOriCECriteriosEvaDTOByCodigo " + e.getMensaje(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("SSe ha producido Exception al getOriCECriteriosEvaDTOByCodigo  " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage(),e);
            }
        }
    }
    
    public List<OriCECriteriosCentroDTO> getOriCECriteriosCentroByIdCentro(long idCentro, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return oriCECriteriosCentroDAO.getOriCECriteriosCentroDTOByIdCentro(idCentro, con);
        } catch (BDException e) {
            log.error("Se ha producido BDException  al getOriCECriteriosCentroByIdCentro " + e.getMensaje(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("SSe ha producido Exception al getOriCECriteriosCentroByIdCentro  " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage(),e);
            }
        }
    }
    
    public List<OriCECriteriosCentroDTO> getOriCECriteriosCentroDTOByNumeroExpediente(String numeroExpediente, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return oriCECriteriosCentroDAO.getOriCECriteriosCentroDTOByNumeroExpediente(numeroExpediente, con);
        } catch (BDException e) {
            log.error("Se ha producido BDException  al getOriCECriteriosCentroDTOByNumeroExpediente " + e.getMensaje(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("SSe ha producido Exception al getOriCECriteriosCentroDTOByNumeroExpediente  " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage(),e);
            }
        }
    }
    
    public boolean actualizarDatosOriCECriteriosCentro(int idDBCentroEmpleo, String codigoApartadoCriterio, String numeroExpediente, String listaCriteriosSeleccionados, String listaCriteriosSeleccionadosVal, Integer ejericioConvocatoria, AdaptadorSQLBD adapt) {
        Connection con = null;
        boolean respuesta = true;
        try {
            con = adapt.getConnection();
            // Creo una transaccion, Si hay un fallo en la delete de daatos anteroriores o alta en nuevos datos, restablezco lo original
            adapt.inicioTransaccion(con);
            // Quitamos los datos actuales de criterios
            oriCECriteriosCentroDAO.eliminarOriCECriteriosCentroByIdCentro(idDBCentroEmpleo, con);
            // Damos de alta los nuevos cambios
            log.info("Nuevos datos al actualizar guardados eb BD? " +
                    altaNuevoOriCECriteriosCentroByCon(idDBCentroEmpleo, codigoApartadoCriterio, numeroExpediente, listaCriteriosSeleccionados, listaCriteriosSeleccionadosVal, ejericioConvocatoria, con)
            );
            // Commit y cerramos la transaccion
            adapt.finTransaccion(con);
        } catch (BDException e) {
            log.error("Se ha producido BDException  al guardarDatosOriCECriteriosCentroByCE " + e.getMensaje(), e);
            //Si hay fallo rollback de lo modificado en la tabla criterios
            try {
                adapt.rollBack(con);
            } catch (Exception ex) {
                log.error("Error al hacer rollback a la BBDD: " + ex.getMessage(),ex);
            }
            respuesta=false;
        } catch (Exception ex) {
            log.error("Se ha producido Exception al guardarDatosOriCECriteriosCentroByCE  " + ex.getMessage(), ex);
            //Si hay fallo rollback de lo modificado en la tabla criterios
            try {
                adapt.rollBack(con);
            } catch (Exception e) {
                log.error("Error al hacer rollback a la BBDD: " + e.getMessage(),e);
            }
            respuesta=false;
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage(),e);
            }
        }
        return respuesta;
    }
    
    public boolean altaNuevoOriCECriteriosCentroByCon(int idDBCentroEmpleo, String codigoApartadoCriterio, String numeroExpediente, String listaCriteriosSeleccionados, String listaCriteriosSeleccionadosVal, Integer ejercicioConvocatoria, Connection con) {
        boolean respuesta = false;
        try {
            String[] listaCriteriosSeleccionadosArray;
            List<String> listaCriteriosSeleccionadosValList = getListCriteriosValidados(listaCriteriosSeleccionadosVal);
            List<OriCECriteriosEvaDTO> criterioEvaluacion = oriCECriteriosEvaDAO.getOriCECriteriosEvaDTOByCodigo(codigoApartadoCriterio, ejercicioConvocatoria, con);
            if (listaCriteriosSeleccionados != null && !listaCriteriosSeleccionados.isEmpty() && !listaCriteriosSeleccionados.equalsIgnoreCase("null")) {
                listaCriteriosSeleccionadosArray = listaCriteriosSeleccionados.split(",");
                for (String codigoCriterio : listaCriteriosSeleccionadosArray) {
                    String[] codigoCriterioArreglo = codigoCriterio.split("_");
                    if (codigoCriterioArreglo != null && codigoCriterioArreglo.length == 3) {
                        OriCECriteriosCentro cECriteriosCentro = new OriCECriteriosCentro(idDBCentroEmpleo, numeroExpediente, ejercicioConvocatoria);
                        cECriteriosCentro.setIdCriterio(getIdBDCriterioFromListaDTO(codigoCriterioArreglo[1], criterioEvaluacion));
                        cECriteriosCentro.setIdCriterioOpcion(getIdBDCriterioOpcionFromListaDTO(codigoCriterioArreglo[1], codigoCriterioArreglo[2], criterioEvaluacion));
                        cECriteriosCentro.setCentroSeleccionOpcion(1);
                        // Verificamos si viene en la lista de Validados
                        if(listaCriteriosSeleccionadosValList.contains(codigoCriterio)){
                            cECriteriosCentro.setCentroSeleccionOpcionVAL(1);
                            listaCriteriosSeleccionadosValList.remove(codigoCriterio);
                        }
                        respuesta = oriCECriteriosCentroDAO.crearOriCECriteriosCentro(cECriteriosCentro, con);
                    } else {
                        log.error("Codigo Criterio no recibido o formateado erroneamente. "
                                + (codigoCriterioArreglo != null ? Arrays.toString(codigoCriterioArreglo) : "")
                                + "listaCriteriosSeleccionados: " + listaCriteriosSeleccionados
                        );
                    }
                }
            }
            // Si ha quedado alguno validado suelto, (No seleccionadao pero si check validado - Notiene mucho sentido pero puede darse caso - aportaciones carta que justifiquen un criterio no marcado en la solicitud)
            if (!listaCriteriosSeleccionadosValList.isEmpty()) {
                for (String criterioValidado : listaCriteriosSeleccionadosValList) {
                    String[] codigoCriterioArreglo = criterioValidado.split("_");
                    if (codigoCriterioArreglo != null && codigoCriterioArreglo.length == 3) {
                        OriCECriteriosCentro cECriteriosCentro = new OriCECriteriosCentro(idDBCentroEmpleo, numeroExpediente, ejercicioConvocatoria);
                        cECriteriosCentro.setIdCriterio(getIdBDCriterioFromListaDTO(codigoCriterioArreglo[1], criterioEvaluacion));
                        cECriteriosCentro.setIdCriterioOpcion(getIdBDCriterioOpcionFromListaDTO(codigoCriterioArreglo[1], codigoCriterioArreglo[2], criterioEvaluacion));
                        cECriteriosCentro.setCentroSeleccionOpcionVAL(1);
                        respuesta = oriCECriteriosCentroDAO.crearOriCECriteriosCentro(cECriteriosCentro, con);
                    } else {
                        log.error("Codigo Criterio Validado no recibido o formateado erroneamente. "
                                + (codigoCriterioArreglo != null ? Arrays.toString(codigoCriterioArreglo) : "")
                                + "listaCriteriosSeleccionadosVal: " + listaCriteriosSeleccionadosVal
                        );
                    }
                }
            }
        } catch (BDException e) {
            log.error("Se ha producido BDException  al guardarDatosOriCECriteriosCentroByCE " + e.getMensaje(), e);
        } catch (Exception ex) {
            log.error("Se ha producido Exception al guardarDatosOriCECriteriosCentroByCE  " + ex.getMessage(), ex);
        } 
        return respuesta;
    }

    public boolean altaNuevoOriCECriteriosCentro(int idDBCentroEmpleo, String codigoApartadoCriterio, String numeroExpediente, String listaCriteriosSeleccionados, String listaCriteriosSeleccionadosValidados, Integer ejercicioConvocatoria, AdaptadorSQLBD adapt) {
        Connection con = null;
        boolean respuesta = false;
        try {
            con = adapt.getConnection();
            return altaNuevoOriCECriteriosCentroByCon(idDBCentroEmpleo, codigoApartadoCriterio, numeroExpediente, listaCriteriosSeleccionados, listaCriteriosSeleccionadosValidados, ejercicioConvocatoria, con);
        } catch (BDException e) {
            log.error("Se ha producido BDException  al guardarDatosOriCECriteriosCentroByCE " + e.getMensaje(), e);
        } catch (Exception ex) {
            log.error("Se ha producido Exception al guardarDatosOriCECriteriosCentroByCE  " + ex.getMessage(), ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage(), e);
            }
        }
        return respuesta;
    }    
    
    private long getIdBDCriterioFromListaDTO(String codigoOrdenCriterio,List<OriCECriteriosEvaDTO> criterioEvaluacion){
        long respuesta = -1;
        try {
            if(codigoOrdenCriterio!=null && !codigoOrdenCriterio.isEmpty() && !codigoOrdenCriterio.equalsIgnoreCase("null")
                    && criterioEvaluacion!=null && !criterioEvaluacion.isEmpty()){
                for (OriCECriteriosEvaDTO oriCECriteriosEvaDTO : criterioEvaluacion) {
                    if(oriCECriteriosEvaDTO.getCodigoOrden().equalsIgnoreCase(codigoOrdenCriterio))
                            return oriCECriteriosEvaDTO.getId();
                }
            }
        } catch (Exception e) {
            log.error("Error al recuperar el id del Criterio de BD " + e.getMessage(), e);
        }
        return respuesta;
    }
    private long getIdBDCriterioOpcionFromListaDTO(String codigoOrdenCriterio,String codigoOrdenCriterioOpcion,List<OriCECriteriosEvaDTO> criterioEvaluacion){
        long respuesta = -1;
        try {
            if(codigoOrdenCriterio!=null && !codigoOrdenCriterio.isEmpty() && !codigoOrdenCriterio.equalsIgnoreCase("null")
                    && codigoOrdenCriterioOpcion!=null && !codigoOrdenCriterioOpcion.isEmpty() && !codigoOrdenCriterioOpcion.equalsIgnoreCase("null")
                    && criterioEvaluacion!=null && !criterioEvaluacion.isEmpty()
                    ){
                for (OriCECriteriosEvaDTO oriCECriteriosEvaDTO : criterioEvaluacion) {
                    if(oriCECriteriosEvaDTO.getCodigoOrden().equalsIgnoreCase(codigoOrdenCriterio)){
                        return oriCECriteriosEvaDTO.getIDOpcionListaOpciones(codigoOrdenCriterioOpcion);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error al recuperar el id del Criterio de BD " + e.getMessage(), e);
        }
        return respuesta;
    }

    private List<String> getListCriteriosValidados(String listaCriteriosSeleccionadosVal) {
        List<String> respuesta = new ArrayList<String>();
        try {
            if(listaCriteriosSeleccionadosVal != null && !listaCriteriosSeleccionadosVal.isEmpty() && !listaCriteriosSeleccionadosVal.equalsIgnoreCase("null")){
                listaCriteriosSeleccionadosVal=listaCriteriosSeleccionadosVal.replaceAll("v_", "");
                String[] arreglo =  listaCriteriosSeleccionadosVal.split(",");
                if(arreglo!=null){
                    respuesta = new ArrayList<String>(Arrays.asList(arreglo));
                }
            }
        } catch (Exception e) {
            log.error("Error procesando StrinLIsta de Criterios Validados a List<String> " +e.getMessage(), e);
        }
        return respuesta;
    }

    public List<OriCECriteriosEvaDTO> obtenerCriteriosPuntuacionPorUbicacion(CeUbicacionVO ubicacion, AdaptadorSQLBD adaptador) throws BDException {
        Connection con = null;
        List<OriCECriteriosEvaDTO> respuesta = new LinkedList<OriCECriteriosEvaDTO>();
        try {
            con = adaptador.getConnection();
            respuesta =  oriCECriteriosCentroDAO.getCriteriosPuntuacionXUbicacionCE(ubicacion, con);
        } catch (Exception e) {
            log.error("Se ha producido Exception al obtenerCriteriosPuntuacionPorUbicacion " + e.getMessage(), e);
        } finally {
            if (con != null) {
                adaptador.devolverConexion(con);
            }
}
        return respuesta;   
    }

}