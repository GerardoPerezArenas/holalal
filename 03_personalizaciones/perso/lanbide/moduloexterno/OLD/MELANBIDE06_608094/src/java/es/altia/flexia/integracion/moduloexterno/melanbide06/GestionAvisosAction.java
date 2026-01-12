/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide06;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import es.altia.flexia.integracion.moduloexterno.melanbide06.manager.GestionAvisosManager;
import es.altia.flexia.integracion.moduloexterno.melanbide06.manager.MeLanbide06Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide06.vo.GestionAvisosVO;
import es.altia.flexia.integracion.moduloexterno.melanbide06.vo.Melanbide06GA;
import es.altia.flexia.integracion.moduloexterno.melanbide06.vo.SelectItem;
import es.altia.flexia.registro.digitalizacion.lanbide.vo.GeneralComboVO;
import es.altia.util.ajax.respuesta.RespuestaAjaxUtils;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import es.altia.util.ajax.respuesta.RespuestaAjaxUtils;
import java.io.PrintWriter;
import java.util.Date;

/**
 *
 * @author jaime.hermoso
 */
public class GestionAvisosAction extends DispatchAction{
    
    private static Logger LOG = Logger.getLogger(GestionAvisosAction.class);
    private final ResourceBundle confRegistro  = ResourceBundle.getBundle("Registro");
    
    private GsonBuilder gsonBuilder = new GsonBuilder().serializeNulls();
    private Gson gson = gsonBuilder.serializeNulls().create();
        
    
    public ActionForward rescatarProcedimientoByCodAsunto(ActionMapping mapping, ActionForm form,
                                            HttpServletRequest request, HttpServletResponse response) throws Exception {
        //log.info("rescatarProcedimientoByCodAsunto - Begin()" + formatFechaLog.format(new Date()));
        String codAsunto= "";
        String codOrg;
        SelectItem respuesta = new SelectItem();
        GestionAvisosManager gestionAvisosManager = new GestionAvisosManager();
        MeLanbide06 meLanbide06 = new MeLanbide06();    
        
        try {           
            //Recojo los parametros
            codOrg = (String) request.getParameter("codOrg");
            codAsunto = (String) request.getParameter("codAsunto");

            //log.info("codAsunto: " + codAsunto);
            if (codAsunto != null && !codAsunto.isEmpty()) {
                respuesta=gestionAvisosManager.getRescatarProcedimientoByCodAsunto(codAsunto, meLanbide06.getAdaptSQLBD(codOrg));
            }
        } catch (Exception e) {
            //log.error("Se ha presentado un error al rescatar el código del procedimiento", e);
            codAsunto=null;
        }
        RespuestaAjaxUtils.retornarJSON(gson.toJson(respuesta), response);
        return null;
    }
    
    
    public ActionForward rescatarUorByCodProcedimiento(ActionMapping mapping, ActionForm form,
                                            HttpServletRequest request, HttpServletResponse response) throws Exception {
        //log.info("rescatarUorByCodProcedimiento - Begin()" + formatFechaLog.format(new Date()));
        String codPro= "";
        String codOrg;
        List<SelectItem> respuesta = new ArrayList();
        GestionAvisosManager gestionAvisosManager = new GestionAvisosManager();
        MeLanbide06 meLanbide06 = new MeLanbide06();    
        
        try {           
            //Recojo los parametros
            codOrg = (String) request.getParameter("codOrg");
            codPro = (String) request.getParameter("codPro");

            //log.info("codPro: " + codPro);
            if (codPro != null && !codPro.isEmpty()) {
                respuesta=gestionAvisosManager.getRescatarUorByCodProcedimiento(codPro, meLanbide06.getAdaptSQLBD(codOrg));
            }
        } catch (Exception e) {
            //log.error("Se ha presentado un error al rescatar las unidades orgánicas", e);
            codPro=null;
        }
        RespuestaAjaxUtils.retornarJSON(gson.toJson(respuesta), response);
        return null;
    }
    
    public ActionForward anadirGA(ActionMapping mapping, ActionForm form,
                                            HttpServletRequest request, HttpServletResponse response) throws Exception {
        //log.info("anadirGA - Begin()" + formatFechaLog.format(new Date()));
        String codOrg;
        String datosGA = "";
        List<SelectItem> respuesta = new ArrayList();
        GestionAvisosManager gestionAvisosManager = new GestionAvisosManager();
        MeLanbide06 meLanbide06 = new MeLanbide06(); 
        try {
            //Recojo los parametros
            datosGA = (String) request.getParameter("jsonGA");
            codOrg = (String) request.getParameter("codOrg");
            //log.info("datosGA : " + datosGA);
            if (datosGA != null && !datosGA.isEmpty()) {
                Gson gson = new Gson();
                GsonBuilder gsonB = new GsonBuilder().setDateFormat("dd/MM/yyyy");
                gsonB.serializeNulls();
                gson = gsonB.create();
                Melanbide06GA datosGAPS = (Melanbide06GA) gson.fromJson(datosGA, Melanbide06GA.class);
                if (datosGAPS != null) {
                     gestionAvisosManager.getAnadirGA(datosGAPS, meLanbide06.getAdaptSQLBD(codOrg));
                }
            }
        } catch (Exception e) {
            //log.error("Se ha presentado un error al registrar un GA", e);
        }
        //RespuestaAjaxUtils.retornarJSON(gson.toJson(respuesta), response);
        return null;
    }
    
    public ActionForward eliminarGA(ActionMapping mapping, ActionForm form,
                                            HttpServletRequest request, HttpServletResponse response) throws Exception {
        //log.info("eliminarGA - Begin()" + formatFechaLog.format(new Date()));
        String id;
        String codOrg;
        GestionAvisosManager gestionAvisosManager = new GestionAvisosManager();
        MeLanbide06 meLanbide06 = new MeLanbide06(); 
        try {
            //Recojo los parametros
            id = (String) request.getParameter("filaIDseleccionada");
            codOrg = (String) request.getParameter("codOrg");
             gestionAvisosManager.getEliminarGA(id, meLanbide06.getAdaptSQLBD(codOrg));
        } catch (Exception e) {
            //log.error("Se ha presentado un error al eliminar un GA", e);
        }
        //RespuestaAjaxUtils.retornarJSON(gson.toJson(respuesta), response);
        return null;
    }
    
    public ActionForward modificarGA(ActionMapping mapping, ActionForm form,
                                            HttpServletRequest request, HttpServletResponse response) throws Exception {
        //log.info("modificarGA - Begin()" + formatFechaLog.format(new Date()));
        String codOrg;
        String id;
        String datosGA = "";
        List<SelectItem> respuesta = new ArrayList();
        GestionAvisosManager gestionAvisosManager = new GestionAvisosManager();
        MeLanbide06 meLanbide06 = new MeLanbide06(); 
        try {
            //Recojo los parametros
            datosGA = (String) request.getParameter("jsonGA");
            codOrg = (String) request.getParameter("codOrg");
            id = (String) request.getParameter("filaIDseleccionada");
            //log.info("datosGA : " + datosGA);
            if (datosGA != null && !datosGA.isEmpty()) {
                Gson gson = new Gson();
                GsonBuilder gsonB = new GsonBuilder().setDateFormat("dd/MM/yyyy");
                gsonB.serializeNulls();
                gson = gsonB.create();
                Melanbide06GA datosGAPS = (Melanbide06GA) gson.fromJson(datosGA, Melanbide06GA.class);
                if (datosGAPS != null) {
                     gestionAvisosManager.getModificarGA(datosGAPS,id, meLanbide06.getAdaptSQLBD(codOrg));
                }
            }
        } catch (Exception e) {
            //log.error("Se ha presentado un erro al modificar un GA", e);
        }
        //RespuestaAjaxUtils.retornarJSON(gson.toJson(respuesta), response);
        return null;
    }
    
    public ActionForward cargarDatosGA(ActionMapping mapping, ActionForm form,
                                            HttpServletRequest request, HttpServletResponse response) throws Exception {
        //log.info("cargarDatosGA - Begin()" + formatFechaLog.format(new Date()));
        String codOrg;
        List<GestionAvisosVO> respuesta = new ArrayList();
        GestionAvisosManager gestionAvisosManager = new GestionAvisosManager();
        MeLanbide06 meLanbide06 = new MeLanbide06();    
        try {           
            //Recojo los parametros
            codOrg = (String) request.getParameter("codOrg");
            respuesta=gestionAvisosManager.getDatosGA(meLanbide06.getAdaptSQLBD(codOrg));
        } catch (Exception e) {
        }
        RespuestaAjaxUtils.retornarJSON(gson.toJson(respuesta), response);
        return null;
    }  
    
}
