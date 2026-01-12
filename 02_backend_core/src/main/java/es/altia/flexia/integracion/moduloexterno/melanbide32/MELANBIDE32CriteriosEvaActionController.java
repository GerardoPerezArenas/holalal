package es.altia.flexia.integracion.moduloexterno.melanbide32;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import es.altia.flexia.integracion.moduloexterno.melanbide32.manager.MeLanbide32Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide32.manager.MeLanbide32ManagerCriteriosSeleccionCE;
import es.altia.flexia.integracion.moduloexterno.melanbide32.util.MeLanbide32Utils;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.centroempleo.AmbitoCentroEmpleoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.centroempleo.OriCECriteriosCentroDTO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.centroempleo.OriCECriteriosEvaDTO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import es.altia.util.ajax.respuesta.RespuestaAjaxUtils;
import java.lang.reflect.Type;
import java.util.HashMap;

/**
 *
 * @author INGDGC
 */
public class MELANBIDE32CriteriosEvaActionController  {
    
    private static final Logger log = LogManager.getLogger(MELANBIDE32CriteriosEvaActionController.class);
    SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    private static MeLanbide32ManagerCriteriosSeleccionCE meLanbide32ManagerCriteriosSeleccionCE = new MeLanbide32ManagerCriteriosSeleccionCE();
    private static MELANBIDE32 melanbide32 = new MELANBIDE32();
    
    private GsonBuilder gsonBuilder = new GsonBuilder().serializeNulls();
    private Gson gson = gsonBuilder.serializeNulls().create();
    
    public void getListaCriteriosEvaluacionByCodigo(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoCriterio = request.getParameter("codigoCriterio");
        try {
            log.info("codigoCriterio:" + codigoCriterio);
            List<OriCECriteriosEvaDTO> listaResponse = meLanbide32ManagerCriteriosSeleccionCE.getOriCECriteriosEvaDTOByCodigo(codigoCriterio, MeLanbide32Utils.getEjercicioDeExpediente(numExpediente),melanbide32.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            log.info("listaResponse: " + (listaResponse!=null?listaResponse.toString():"Lista vacia"));
            RespuestaAjaxUtils.retornarJSON(gson.toJson(listaResponse), response);
        } catch (Exception e) {
            log.error("Error procesando getListaCriteriosEvaluacionByCodigo " + e.getMessage(), e);
        }finally{
        }
    }
    
    public void getListaCriteriosEvaluacionCEByIdCentro(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String idCcentro = request.getParameter("idCentro");
        try {
            log.info("idCcentro:" + idCcentro);
            long idCentroLong  = (idCcentro!=null && !idCcentro.isEmpty() && !idCcentro.equalsIgnoreCase("null") ? Long.parseLong(idCcentro):0);
            List<OriCECriteriosCentroDTO> listaResponse = meLanbide32ManagerCriteriosSeleccionCE.getOriCECriteriosCentroByIdCentro(idCentroLong, melanbide32.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            log.info("listaResponse: " + (listaResponse!=null?listaResponse.toString():"Lista vacia"));
            RespuestaAjaxUtils.retornarJSON(gson.toJson(listaResponse), response);
        } catch (Exception e) {
            log.error("Error procesando getListaCriteriosEvaluacionCEByIdCentro " + e.getMessage(), e);
        }finally{
        }
    }

    void getAmbitoCentroEmpleoVOByID(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String idBDAmbito = request.getParameter("idBDAmbito");
        try {
            log.info("idBDAmbito:" + idBDAmbito);
            int idBDAmbitoInt = (idBDAmbito!=null && !idBDAmbito.isEmpty() && !idBDAmbito.equalsIgnoreCase("null") ? Integer.parseInt(idBDAmbito):0);
            AmbitoCentroEmpleoVO respuesta = meLanbide32ManagerCriteriosSeleccionCE.getAmbitoCentroEmpleoPorCodigo(idBDAmbitoInt, melanbide32.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            log.info("respuesta: " + (respuesta != null ? respuesta.toString() : "Datos  no recuperados"));
            RespuestaAjaxUtils.retornarJSON(gson.toJson(respuesta), response);
        } catch (Exception e) {
            log.error("Error procesando getAmbitoCentroEmpleoVOByID " + e.getMessage(), e);
        } finally {
        }
    }
    
}
