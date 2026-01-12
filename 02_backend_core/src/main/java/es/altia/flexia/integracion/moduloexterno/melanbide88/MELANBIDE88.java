/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide88;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import es.altia.flexia.integracion.moduloexterno.melanbide88.manager.Melanbide88Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide88.util.MeLanbide88Utils;
import es.altia.flexia.integracion.moduloexterno.melanbide88.vo.MeLanbideConvocatorias;
import es.altia.flexia.integracion.moduloexterno.melanbide88.vo.Melanbide88Inversiones;
import es.altia.flexia.integracion.moduloexterno.melanbide88.vo.Melanbide88Socios;
import es.altia.flexia.integracion.moduloexterno.melanbide88.vo.Melanbide88Subsolic;
import es.altia.flexia.integracion.moduloexterno.melanbide88.vo.SelectItem;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author INGDGC
 */
public class MELANBIDE88 extends ModuloIntegracionExterno  {

    private static final Logger LOG = LogManager.getLogger(MELANBIDE88.class);
    SimpleDateFormat formatFechaLog = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    SimpleDateFormat formatFechaddMMyyyy = new SimpleDateFormat("dd/MM/yyyy");
    private static final MeLanbide88Utils meLanbide88Utils = new MeLanbide88Utils();
    private final Melanbide88Manager melanbide88Manager = new Melanbide88Manager();
    //private final MeLanbide88I18n meLanbide88I18n = MeLanbide88I18n.getInstance();
    
    
    // Alta Expedientes via registro platea --> MELANBIDE 42
    public void cargarExpedienteExtension(int codigoOrganizacion, String numeroExpediente, String xml) throws Exception{
        final Class cls = Class.forName("es.altia.flexia.integracion.moduloexterno.melanbide42.MELANBIDE42");
        final Object me42Class = cls.newInstance();
        final Class[] types = {int.class, String.class, String.class};
        final Method method = cls.getMethod("cargarExpedienteExtension", types);
        method.invoke(me42Class, codigoOrganizacion, numeroExpediente, xml);
    }
    
    public String cargarPantallaDatosTreco(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        LOG.error("cargarPantallaDatosTreco - Tiempo de Ini Metodo " + numExpediente + " " + formatFechaLog.format(new java.util.Date()));
        AdaptadorSQLBD adapt = null;
        MeLanbideConvocatorias convocatoriaActiva = null;
        request.setAttribute("codProcedimiento",meLanbide88Utils.getCodProcedimientoDeExpediente(numExpediente));
        try {
            adapt = meLanbide88Utils.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (Exception ex) {
            LOG.error("Error preparando el Adaptador de BBD ", ex);
        }
        // Recogemos los datos de El decreto/Convocatoria Aplicable
        try {
            //convocatoriaActiva = meLanbide88Manager.getDecretoAplicableExpediente(codOrganizacion, numExpediente, adapt);
            request.setAttribute("convocatoriaActiva", convocatoriaActiva);
        } catch (Exception ex) {
            LOG.error("Erro al tratar de leer los datos del decretoaplicable al expediente " + numExpediente, ex);
        }
        // DATOS GENERALES MODULO
        try {
            
            LOG.debug("Finalizada la comprobacion de interesado y entidad principal de Modulo de extensióon");
        } catch (Exception ex) {
            LOG.error("Error a comporbar interesado y entidad pricipal para modulo de extension ", ex);
        }
        String url;
        try {
            url = cargarSubpestanaSocios(numExpediente, adapt, request);
            if (url != null) {
                request.setAttribute("urlPestanaDatos_socios", url);
            }
        } catch (Exception ex) {
            LOG.error(ex.getMessage());
        }

        try {
            url = cargarSubpestanaInversiones(numExpediente, adapt, request);
            if (url != null) {
                request.setAttribute("urlPestanaDatos_inversiones", url);
            }
        } catch (Exception ex) {
            LOG.error(ex.getMessage());
        }
        try {
            url = cargarSubpestanaSubvenciones(numExpediente, adapt, request);
            if (url != null) {
                request.setAttribute("urlPestanaDatos_subvenciones", url);
            }
        } catch (Exception ex) {
            LOG.error(ex.getMessage());
        }
        LOG.error("cargarPantallaDatosTreco - Tiempo de Fin Metodo " + numExpediente + " " + formatFechaLog.format(new java.util.Date()));
        return "/jsp/extension/melanbide88/m88DatosModExtension.jsp";
    }

    private String cargarSubpestanaSocios(String numExpediente, AdaptadorSQLBD adapt, HttpServletRequest request) {
        return "/jsp/extension/melanbide88/socios/m88PantallaSocios.jsp";
    }

    private String cargarSubpestanaInversiones(String numExpediente, AdaptadorSQLBD adapt, HttpServletRequest request) {
        return "/jsp/extension/melanbide88/inversiones/m88PantallaInversiones.jsp";
    }

    private String cargarSubpestanaSubvenciones(String numExpediente, AdaptadorSQLBD adapt, HttpServletRequest request) {
        return "/jsp/extension/melanbide88/subvenciones/m88PantallaSubvenciones.jsp";
    }
    
    public void getListaDatosPestanaSocios(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) throws SQLException{
        LOG.info("getListaDatosPestanaSocios - Begin()" + formatFechaLog.format(new Date()));
        AdaptadorSQLBD adapt = meLanbide88Utils.getAdaptSQLBD(String.valueOf(codOrganizacion));
        List<Melanbide88Socios> respuestaServicio = new ArrayList<Melanbide88Socios>();
        try {
            //Idioma
            int codIdioma = meLanbide88Utils.getIdiomaUsuarioFromRequest(request);
            respuestaServicio = melanbide88Manager.getMelanbide88SociosByNumExp(numExpediente,adapt);
        } catch (Exception e) {
            LOG.error("Se ha presentado un error al leer Datos - Socios ", e);
            respuestaServicio = null;
        }
        meLanbide88Utils.parsearRespuestasEnviarJSON(request, response, respuestaServicio);
        LOG.info("getListaDatosPestanaSocios - End()" + formatFechaLog.format(new Date()));
    }
    
    public void getListaDatosPestanaInversiones(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) throws SQLException{
        LOG.info("getListaDatosPestanaInversiones - Begin()" + formatFechaLog.format(new Date()));
        AdaptadorSQLBD adapt = meLanbide88Utils.getAdaptSQLBD(String.valueOf(codOrganizacion));
        List<Melanbide88Inversiones> respuestaServicio = new ArrayList<Melanbide88Inversiones>();
        try {
            //Idioma
            int codIdioma = meLanbide88Utils.getIdiomaUsuarioFromRequest(request);
            respuestaServicio = melanbide88Manager.getMelanbide88InversionesByNumExp(numExpediente,adapt);
        } catch (Exception e) {
            LOG.error("Se ha presentado un error al leer Datos - Socios ", e);
            respuestaServicio = null;
        }
        meLanbide88Utils.parsearRespuestasEnviarJSON(request, response, respuestaServicio);
        LOG.info("getListaDatosPestanaInversiones - End()" + formatFechaLog.format(new Date()));
    }
    
    public void getListaDatosPestanaSubvenciones(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) throws SQLException{
        LOG.info("getListaDatosPestanaSubvenciones - Begin()" + formatFechaLog.format(new Date()));
        AdaptadorSQLBD adapt = meLanbide88Utils.getAdaptSQLBD(String.valueOf(codOrganizacion));
        List<Melanbide88Subsolic> respuestaServicio = new ArrayList<Melanbide88Subsolic>();
        try {
            //Idioma
            int codIdioma = meLanbide88Utils.getIdiomaUsuarioFromRequest(request);
            respuestaServicio = melanbide88Manager.getMelanbide88SubsolicByNumExp(numExpediente,adapt);
        } catch (Exception e) {
            LOG.error("Se ha presentado un error al leer Datos - Socios ", e);
            respuestaServicio = null;
        }
        meLanbide88Utils.parsearRespuestasEnviarJSON(request, response, respuestaServicio);
        LOG.info("getListaDatosPestanaSubvenciones - End()" + formatFechaLog.format(new Date()));
    }
    
    public String cargarAltaEdicionSocios(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        LOG.info("cargarAltaEdicionSocios - Begin()" + formatFechaLog.format(new Date()));
        try {
            AdaptadorSQLBD adapt = meLanbide88Utils.getAdaptSQLBD(String.valueOf(codOrganizacion));
            //Idioma
            int codIdioma = meLanbide88Utils.getIdiomaUsuarioFromRequest(request);
            String identificadorBDGestionar = (String)request.getParameter("identificadorBDGestionar");
            if(identificadorBDGestionar!=null && !identificadorBDGestionar.isEmpty()){
                Melanbide88Socios datosModif =  melanbide88Manager.getMelanbide88SociosByID(Long.valueOf(identificadorBDGestionar), adapt);
                request.setAttribute("datosModif", datosModif);
            }else{
                LOG.info("-- No se recibe ID  agestionar - Alta Socio");
            }
        } catch (Exception e) {
            LOG.error("Se ha presentado un error al cargar los datos  - Socios ", e);
        }
        LOG.info("cargarAltaEdicionSocios - End()" + formatFechaLog.format(new Date()));
        return "/jsp/extension/melanbide88/socios/m88AltaModificacionSocios.jsp";
    }
    
    public void guardarDatosSocio(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) throws SQLException{
        LOG.info("guardarDatosSocio - Begin()" + formatFechaLog.format(new Date()));
        AdaptadorSQLBD adapt = meLanbide88Utils.getAdaptSQLBD(String.valueOf(codOrganizacion));
        List<Melanbide88Socios> respuestaServicio = new ArrayList<Melanbide88Socios>();
        String jsonDatos = "";
        try {
            //Idioma
            int codIdioma = meLanbide88Utils.getIdiomaUsuarioFromRequest(request);
            jsonDatos = (String) request.getParameter("jsonMelanbide88Socios");
            String tipoOperacion = (String) request.getParameter("tipoOperacion");
            LOG.info("tipoOperacion : " + tipoOperacion);
            LOG.info("jsonDatoss : " + jsonDatos);
            if (jsonDatos != null && !jsonDatos.isEmpty()) {
                GsonBuilder gsonB = new GsonBuilder().setDateFormat("dd/MM/yyyy");
                gsonB.serializeNulls();
                Gson gson = gsonB.create();
                Melanbide88Socios datos = (Melanbide88Socios) gson.fromJson(jsonDatos, Melanbide88Socios.class);
                if (datos != null) {
                    LOG.info("Datos guardados: "
                            + melanbide88Manager.guardarMelanbide88Socios(codIdioma, datos, adapt)
                    );
                }
                respuestaServicio = melanbide88Manager.getMelanbide88SociosByNumExp(numExpediente, adapt);
            }
        } catch (Exception e) {
            LOG.error("Se ha presentado un error al Guardar Datos - Socios ", e);
            respuestaServicio = null;
        }
        meLanbide88Utils.parsearRespuestasEnviarJSON(request, response, respuestaServicio);
        LOG.info("guardarDatosSocio - End()" + formatFechaLog.format(new Date()));
    }
    
    public void eliminarDatosSocio(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) throws SQLException{
        LOG.info("eliminarDatosSocio - Begin()" + formatFechaLog.format(new Date()));
        AdaptadorSQLBD adapt = meLanbide88Utils.getAdaptSQLBD(String.valueOf(codOrganizacion));
        List<Melanbide88Socios> respuestaServicio = new ArrayList<Melanbide88Socios>();
        try {
            //Idioma
            int codIdioma = meLanbide88Utils.getIdiomaUsuarioFromRequest(request);
            String identificadorBDGestionar = (String) request.getParameter("identificadorBDGestionar");
            LOG.info("identificadorBDGestionar : " + identificadorBDGestionar);
            if (identificadorBDGestionar != null && !identificadorBDGestionar.isEmpty()) {
                    LOG.info("Operacion Correcta : "
                            + melanbide88Manager.eliminarMelanbide88Socios(Long.valueOf(identificadorBDGestionar), adapt)
                    );
                respuestaServicio = melanbide88Manager.getMelanbide88SociosByNumExp(numExpediente, adapt);
            }
        } catch (Exception e) {
            LOG.error("Se ha presentado un error al Eliminar Datos - Socios ", e);
            respuestaServicio = null;
        }
        meLanbide88Utils.parsearRespuestasEnviarJSON(request, response, respuestaServicio);
        LOG.info("eliminarDatosSocio - End()" + formatFechaLog.format(new Date()));
    }
    
    public String cargarAltaEdicionInversiones(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        LOG.info("cargarAltaEdicionInversiones - Begin()" + formatFechaLog.format(new Date()));
        try {
            AdaptadorSQLBD adapt = meLanbide88Utils.getAdaptSQLBD(String.valueOf(codOrganizacion));
            //Idioma
            int codIdioma = meLanbide88Utils.getIdiomaUsuarioFromRequest(request);
            // Cargamos en la request los desplegabes
            List<SelectItem> pagadaSelect = melanbide88Manager.getDesplegableE_DES("BOOL", codIdioma, adapt);
            request.setAttribute("pagadaSelect", pagadaSelect);
            String identificadorBDGestionar = (String)request.getParameter("identificadorBDGestionar");
            if(identificadorBDGestionar!=null && !identificadorBDGestionar.isEmpty()){
                Melanbide88Inversiones datosModif =  melanbide88Manager.getMelanbide88InversionesByID(Long.valueOf(identificadorBDGestionar), adapt);
                request.setAttribute("datosModif", datosModif);
            }else{
                LOG.info("-- No se recibe ID  agestionar - Alta Inversiones");
            }
        } catch (Exception e) {
            LOG.error("Se ha presentado un error al cargar los datos  - Inversiones ", e);
        }
        LOG.info("cargarAltaEdicionInversiones - End()" + formatFechaLog.format(new Date()));
        return "/jsp/extension/melanbide88/inversiones/m88AltaModificacionInversiones.jsp";
    }
    
    public void guardarDatosInversion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) throws SQLException{
        LOG.info("guardarDatosInversion - Begin()" + formatFechaLog.format(new Date()));
        AdaptadorSQLBD adapt = meLanbide88Utils.getAdaptSQLBD(String.valueOf(codOrganizacion));
        List<Melanbide88Inversiones> respuestaServicio = new ArrayList<Melanbide88Inversiones>();
        String jsonDatos = "";
        try {
            //Idioma
            int codIdioma = meLanbide88Utils.getIdiomaUsuarioFromRequest(request);
            jsonDatos = (String) request.getParameter("jsonMelanbide88Inversiones");
            String tipoOperacion = (String) request.getParameter("tipoOperacion");
            LOG.info("tipoOperacion : " + tipoOperacion);
            LOG.info("jsonDatoss : " + jsonDatos);
            if (jsonDatos != null && !jsonDatos.isEmpty()) {
                GsonBuilder gsonB = new GsonBuilder().setDateFormat("dd/MM/yyyy");
                gsonB.serializeNulls();
                Gson gson = gsonB.create();
                Melanbide88Inversiones datos = (Melanbide88Inversiones) gson.fromJson(jsonDatos, Melanbide88Inversiones.class);
                if (datos != null) {
                    LOG.info("Datos guardados: "
                            + melanbide88Manager.guardarMelanbide88Inversiones(codIdioma, datos, adapt)
                    );
                }
                respuestaServicio = melanbide88Manager.getMelanbide88InversionesByNumExp(numExpediente, adapt);
            }
        } catch (Exception e) {
            LOG.error("Se ha presentado un error al Guardar Datos - Inversion ", e);
            respuestaServicio = null;
        }
        meLanbide88Utils.parsearRespuestasEnviarJSON(request, response, respuestaServicio);
        LOG.info("guardarDatosInversion - End()" + formatFechaLog.format(new Date()));
    }
    
    public void eliminarDatosInversion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) throws SQLException{
        LOG.info("eliminarDatosInversion - Begin()" + formatFechaLog.format(new Date()));
        AdaptadorSQLBD adapt = meLanbide88Utils.getAdaptSQLBD(String.valueOf(codOrganizacion));
        List<Melanbide88Inversiones> respuestaServicio = new ArrayList<Melanbide88Inversiones>();
        try {
            //Idioma
            int codIdioma = meLanbide88Utils.getIdiomaUsuarioFromRequest(request);
            String identificadorBDGestionar = (String) request.getParameter("identificadorBDGestionar");
            LOG.info("identificadorBDGestionar : " + identificadorBDGestionar);
            if (identificadorBDGestionar != null && !identificadorBDGestionar.isEmpty()) {
                    LOG.info("Operacion Correcta : "
                            + melanbide88Manager.eliminarMelanbide88Inversiones(Long.valueOf(identificadorBDGestionar), adapt)
                    );
                respuestaServicio = melanbide88Manager.getMelanbide88InversionesByNumExp(numExpediente, adapt);
            }
        } catch (Exception e) {
            LOG.error("Se ha presentado un error al Eliminar Datos - Inversion ", e);
            respuestaServicio = null;
        }
        meLanbide88Utils.parsearRespuestasEnviarJSON(request, response, respuestaServicio);
        LOG.info("eliminarDatosInversion - End()" + formatFechaLog.format(new Date()));
    }
    
    public String cargarAltaEdicionSubvenciones(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        LOG.info("cargarAltaEdicionSubvenciones - Begin()" + formatFechaLog.format(new Date()));
        try {
            AdaptadorSQLBD adapt = meLanbide88Utils.getAdaptSQLBD(String.valueOf(codOrganizacion));
            //Idioma
            int codIdioma = meLanbide88Utils.getIdiomaUsuarioFromRequest(request);
            // Cargar listas desplegables
            List<SelectItem> estadoSelect = melanbide88Manager.getDesplegableE_DES("ESTS", codIdioma, adapt);
            request.setAttribute("estadoSelect", estadoSelect);
            String identificadorBDGestionar = (String)request.getParameter("identificadorBDGestionar");
            if(identificadorBDGestionar!=null && !identificadorBDGestionar.isEmpty()){
                Melanbide88Subsolic datosModif =  melanbide88Manager.getMelanbide88SubsolicByID(Long.valueOf(identificadorBDGestionar), adapt);
                request.setAttribute("datosModif", datosModif);
            }else{
                LOG.info("-- No se recibe ID  agestionar - Alta Subvenciones");
            }
        } catch (Exception e) {
            LOG.error("Se ha presentado un error al cargar los datos  - Subvenciones ", e);
        }
        LOG.info("cargarAltaEdicionSubvenciones - End()" + formatFechaLog.format(new Date()));
        return "/jsp/extension/melanbide88/subvenciones/m88AltaModificacionSubvenciones.jsp";
    }
    
    public void guardarDatosSubvencion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) throws SQLException{
        LOG.info("guardarDatosSubvenciones - Begin()" + formatFechaLog.format(new Date()));
        AdaptadorSQLBD adapt = meLanbide88Utils.getAdaptSQLBD(String.valueOf(codOrganizacion));
        List<Melanbide88Subsolic> respuestaServicio = new ArrayList<Melanbide88Subsolic>();
        String jsonDatos = "";
        try {
            //Idioma
            int codIdioma = meLanbide88Utils.getIdiomaUsuarioFromRequest(request);
            jsonDatos = (String) request.getParameter("jsonMelanbide88Subsolic");
            String tipoOperacion = (String) request.getParameter("tipoOperacion");
            LOG.info("tipoOperacion : " + tipoOperacion);
            LOG.info("jsonDatoss : " + jsonDatos);
            if (jsonDatos != null && !jsonDatos.isEmpty()) {
                GsonBuilder gsonB = new GsonBuilder().setDateFormat("dd/MM/yyyy");
                gsonB.serializeNulls();
                Gson gson = gsonB.create();
                Melanbide88Subsolic datos = (Melanbide88Subsolic) gson.fromJson(jsonDatos, Melanbide88Subsolic.class);
                if (datos != null) {
                    LOG.info("Datos guardados: "
                            + melanbide88Manager.guardarMelanbide88Subsolic(codIdioma, datos, adapt)
                    );
                }
                respuestaServicio = melanbide88Manager.getMelanbide88SubsolicByNumExp(numExpediente, adapt);
            }
        } catch (Exception e) {
            LOG.error("Se ha presentado un error al Guardar Datos - Subvencion ", e);
            respuestaServicio = null;
        }
        meLanbide88Utils.parsearRespuestasEnviarJSON(request, response, respuestaServicio);
        LOG.info("guardarDatosSubvenciones - End()" + formatFechaLog.format(new Date()));
    }
    
    public void eliminarDatosSubvencion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) throws SQLException{
        LOG.info("eliminarDatosSubvencion - Begin()" + formatFechaLog.format(new Date()));
        AdaptadorSQLBD adapt = meLanbide88Utils.getAdaptSQLBD(String.valueOf(codOrganizacion));
        List<Melanbide88Subsolic> respuestaServicio = new ArrayList<Melanbide88Subsolic>();
        try {
            //Idioma
            int codIdioma = meLanbide88Utils.getIdiomaUsuarioFromRequest(request);
            String identificadorBDGestionar = (String) request.getParameter("identificadorBDGestionar");
            LOG.info("identificadorBDGestionar : " + identificadorBDGestionar);
            if (identificadorBDGestionar != null && !identificadorBDGestionar.isEmpty()) {
                    LOG.info("Operacion Correcta : "
                            + melanbide88Manager.eliminarMelanbide88Subsolic(Long.valueOf(identificadorBDGestionar), adapt)
                    );
                respuestaServicio = melanbide88Manager.getMelanbide88SubsolicByNumExp(numExpediente, adapt);
            }
        } catch (Exception e) {
            LOG.error("Se ha presentado un error al Eliminar Datos - Subvencion ", e);
            respuestaServicio = null;
        }
        meLanbide88Utils.parsearRespuestasEnviarJSON(request, response, respuestaServicio);
        LOG.info("eliminarDatosSubvencion - End()" + formatFechaLog.format(new Date()));
    }
}
