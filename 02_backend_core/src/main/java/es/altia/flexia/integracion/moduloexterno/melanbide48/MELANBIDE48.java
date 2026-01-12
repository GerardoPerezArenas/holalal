/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide48;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.terceros.CondicionesBusquedaTerceroVO;
import es.altia.agora.business.terceros.TercerosValueObject;
import es.altia.agora.business.terceros.persistence.TercerosManager;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.melanbide48.i18n.MeLanbide48I18n;
import es.altia.flexia.integracion.moduloexterno.melanbide48.manager.*;
import es.altia.flexia.integracion.moduloexterno.melanbide48.procesos.ColecProcesoAdjGestionExcel;
import es.altia.flexia.integracion.moduloexterno.melanbide48.util.ConstantesMeLanbide48;
import es.altia.flexia.integracion.moduloexterno.melanbide48.util.MeLanbide48MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide48.util.MeLanbide48Utils;
import es.altia.flexia.integracion.moduloexterno.melanbide48.util.Melanbide48DecretoExpedienteEnum;
import es.altia.flexia.integracion.moduloexterno.melanbide48.vo.*;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;
import es.altia.technical.PortableContext;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 *
 * @author santiagoc
 */
public class MELANBIDE48 extends ModuloIntegracionExterno 
{
    //Logger
    private static final Logger log = LogManager.getLogger(MELANBIDE48.class);
    SimpleDateFormat formatFechaLog = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    SimpleDateFormat formatFechaddMMyyyy = new SimpleDateFormat("dd/MM/yyyy");
    ResourceBundle m_Conf = ResourceBundle.getBundle("common");
    private final MeLanbide48Manager meLanbide48Manager = MeLanbide48Manager.getInstance();
    private final MeLanbide48I18n meLanbide48I18n = MeLanbide48I18n.getInstance();
    private final ColecProcesoAdjGestionExcel colecProcesoAdjGestionExcel  = new ColecProcesoAdjGestionExcel();
    private final MELanbide48ManagerColecCertCalidad meLanbide48ManagerColecCertCalidad = new MELanbide48ManagerColecCertCalidad();
    private final MELanbide48ManagerColecCompIgualdad meLanbide48ManagerColecCompIgualdad = new MELanbide48ManagerColecCompIgualdad();
    private final MELanbide48ManagerColecEntidadCertCalidad meLanbide48ManagerColecEntidadCertCalidad = new MELanbide48ManagerColecEntidadCertCalidad();
    private final MELanbide48ManagerColecCompIgualdadPuntuacion meLanbide48ManagerColecCompIgualdadPuntuacion = new MELanbide48ManagerColecCompIgualdadPuntuacion();
    private final MELanbide48ManagerColecCertCalidadPuntuacion meLanbide48ManagerColecCertCalidadPuntuacion = new MELanbide48ManagerColecCertCalidadPuntuacion();

    
    // Alta Expedientes via registro platea --> MELANBIDE 42
    public void cargarExpedienteExtension(int codigoOrganizacion, String numeroExpediente, String xml) throws Exception{
        final Class cls = Class.forName("es.altia.flexia.integracion.moduloexterno.melanbide42.MELANBIDE42");
        final Object me42Class = cls.newInstance();
        final Class[] types = {int.class, String.class, String.class};
        final Method method = cls.getMethod("cargarExpedienteExtension", types);
        method.invoke(me42Class, codigoOrganizacion, numeroExpediente, xml);
    }
    
    public String cargarPantallaDatosColec(int codOrganizacion,  int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
        log.error("cargarDatosORI - Tiempo de Ini Metodo " + numExpediente + " " + dateFormat.format(new java.util.Date()));
        AdaptadorSQLBD adapt = null;
        MeLanbideConvocatorias convocatoriaActiva = null;
        int idioma = MeLanbide48Utils.getIdiomaUsuarioFromRequest(request);
        try
        {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        }
        catch(Exception ex)
        {
            log.error("Error preparando el Adaptador de BBD ", ex);
        }
        // Recogemos los datos de El decreto/Convocatoria Aplicable
        try {
            convocatoriaActiva=meLanbide48Manager.getDecretoAplicableExpediente(codOrganizacion,numExpediente, adapt);
            request.setAttribute("convocatoriaActiva", convocatoriaActiva);
            request.setAttribute("convocatoriaActivaTest", convocatoriaActiva);
        } catch (Exception ex) {
            log.error("Erro al tratar de leer los datos del decretoaplicable al expediente " + numExpediente, ex);
        }
        try {
            List<SelectItem> listaCertificadosCalidad = meLanbide48ManagerColecCertCalidad.getListaColecCertCalidad(idioma,adapt);
            request.setAttribute("listaCertificadosCalidad",listaCertificadosCalidad);
        }catch (Exception ex){
            log.error("Error al cargar datos de lista Certificados de calidad",ex);
        }
        try {
            List<SelectItem> listaCompromisoIgualdad = meLanbide48ManagerColecCompIgualdad.getListaColecCompIgualdad(idioma,adapt);
            request.setAttribute("listaCompromisoIgualdad",listaCompromisoIgualdad);
        }catch (Exception ex){
            log.error("Error al cargar datos del desplegable Compromiso de igualdad",ex);
        }
        // Comprobamos si existe la entidad, sino la damos de alta, debe ser igual al interesado del expediente.
        // Nif Nombre.
        try {
            log.debug("Empezamos la comprobacion de interesado y entidad principal de Modulo de extensióon");
            ColecEntidadVO entiTemp = new ColecEntidadVO();
            Integer ejercicio = MeLanbide48Utils.getEjercicioDeExpediente(numExpediente);
            entiTemp.setNumExp(numExpediente);
            ColecEntidadVO entidadExp = meLanbide48Manager.getEntidadInteresadaPorExpediente(entiTemp, adapt);
            if(entidadExp==null){
            //creamos la entidad para que no se tenga que insertar nevamente, es posible que sea un expediente nuevo
                HashMap<String,String> datosTercero = meLanbide48Manager.getDatosTercero(codOrganizacion, numExpediente, String.valueOf(ejercicio),"1", adapt);
                if(datosTercero!=null && datosTercero.size()>0){
                    
                    entiTemp.setEjercicio(ejercicio);
                    entiTemp.setAnioConvocatoria(ejercicio);
                    entiTemp.setCif(datosTercero.get("TER_DOC"));
                    String nombreter = datosTercero.get("TER_NOC");
                    // Quitamos las comillas Simples del nombre 
                    nombreter = (nombreter!=null?nombreter.replaceAll("'",""):nombreter);
                    entiTemp.setNombre(nombreter);
                    entidadExp = meLanbide48Manager.guardarColecEntidadVO(codOrganizacion, entiTemp, adapt);
                }
            }
            log.debug("Finalizada la comprobacion de interesado y entidad principal de Modulo de extensióon");
        } catch (Exception ex) {
            log.error("Error a comporbar interesado y entidad pricipal para modulo de extension ",ex);
        }
        String url;
        try
        {
            url = cargarSubpestanaAsociaciones_DatosColec(numExpediente, adapt, request);
            if(url != null)
            {
                request.setAttribute("urlPestanaDatos_asociaciones", url);
            }
        }
        catch(Exception ex)
        {
            log.error(ex.getMessage());
        }

        try
        {
            url = cargarSubpestanaDatosColecTHSol_PorcCompRealxColyTH(numExpediente, adapt, request);
            if(url != null)
            {
                request.setAttribute("urlPestanaDatos_colectivosyTHSol", url);
            }
        }
        catch(Exception ex)
        {
            log.error(ex.getMessage());
        }
        try
        {
            url = cargarSubpestanaExperienciaPreviaArt53(numExpediente, adapt, request);
            if(url != null)
            {
                request.setAttribute("urlPestanaDatos_experPreviaArt53", url);
            }
        }
        catch(Exception ex)
        {
            log.error(ex.getMessage());
        }
        
        try
        {
            url = cargarSubpestanaTrayectoriaGeneral(numExpediente, adapt, request);
            if(url != null)
            {
                request.setAttribute("urlPestanaDatos_trayectoriaGeneral", url);
            }
        }
        catch(Exception ex)
        {
            log.error(ex.getMessage());
        }
        try
        {
            url = cargarSubpestanaExperienciaAcreditable(numExpediente, adapt, request);
            if(url != null)
            {
                request.setAttribute("urlPestanaDatos_experienciaAcreditable", url);
            }
        }
        catch(Exception ex)
        {
            log.error(ex.getMessage());
        }
        
        try
        {
            url = cargarSubpestanaUbicacionesCT(codOrganizacion,numExpediente, adapt, request);
            if(url != null)
            {
                request.setAttribute("urlPestanaDatos_ubicacionesCT", url);
            }
        }
        catch(Exception ex)
        {
            log.error(ex.getMessage());
        }
       log.error("cargarDatosORI - Tiempo de Fin Metodo " + numExpediente + " " + dateFormat.format(new java.util.Date())); 
       return "/jsp/extension/melanbide48/datosColec.jsp";
    }
    
    private void cargarCombosNuevaEntidad(int codOrganizacion, HttpServletRequest request)
    {
        List<SelectItem> listaTipoDoc = new ArrayList<SelectItem>();
        
        //Combo TIPO DOCUMENTO
        try
        {
            listaTipoDoc = meLanbide48Manager.getTiposDocumento(this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        }
        catch(Exception ex)
        {
           log.error(ex.getMessage()); 
        }
        request.setAttribute("listaNif", listaTipoDoc);
    }
 
    public void busquedaTercero(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        String codigoOperacion = "0";
        HashMap mapaResultados = new HashMap();
        try
        {
            String tipoDoc = request.getParameter("tipoDoc");
            String numDoc = request.getParameter("numDoc");
            if(tipoDoc != null && !tipoDoc.equals("") && numDoc != null && !numDoc.equals(""))
            {
                TercerosManager terMan = TercerosManager.getInstance();
                CondicionesBusquedaTerceroVO condsBusq = new CondicionesBusquedaTerceroVO();
                condsBusq.setCodOrganizacion(codOrganizacion);
                log.debug("BusquedaTerceros strTipoDoc: " + tipoDoc);
                condsBusq.setTipoDocumento(Integer.parseInt(tipoDoc));
                condsBusq.setDocumento(numDoc);
                log.debug("BusquedaTerceros txtDNI busqueda: " + request.getParameter("txtDNI"));
                
                HttpSession session = request.getSession();
                UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
                String[] params = usuario.getParamsCon();
                
                // Llamar al manager.
                mapaResultados = terMan.getTercero(condsBusq, params);
                System.out.println("resultado = "+(mapaResultados != null ? mapaResultados.size() : "null"));
            }
        }
        catch(NumberFormatException ex)
        {
            log.error(ex.getMessage());
            codigoOperacion = "2";
        }
        escribirResultadoBusquedaTercero(codigoOperacion, mapaResultados, response);
    }
    

    //Guarda los importes de la parte superior
    public void guardarDatosColec(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        String codigoOperacion = "0";
        
        AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        List<ColecEntidadEnAgrupacionVO> entidadesAgru = new ArrayList<ColecEntidadEnAgrupacionVO>();
        try
        {
            
            Integer ejercicio = MeLanbide48Utils.getEjercicioDeExpediente(numExpediente);
            int codIdioma = MeLanbide48Utils.getIdiomaUsuarioFromRequest(request);

            if(ejercicio != null)
            {
                
                //Datos interesado
                String codEntidad = request.getParameter("codEntidad");
                String cif = request.getParameter("cif");
                String nombre = request.getParameter("nombre");
                String esAsociacion = request.getParameter("esAsociacion");
                String esAsociacionValidado = request.getParameter("esAsociacionValidado");
                String centroEspecialEmpTH = request.getParameter("centroEspecialEmpTH");
                String centroEspecialEmpTHValidado = request.getParameter("centroEspecialEmpTHValidado");
                String particiMayorCentEspeEmpTH = request.getParameter("particiMayorCentEspeEmpTH");
                String particiMayorCentEspeEmpTHValidado = request.getParameter("particiMayorCentEspeEmpTHValidado");
                String empresaInsercionTH = request.getParameter("empresaInsercionTH");
                String empresaInsercionTHValidado = request.getParameter("empresaInsercionTHValidado");
                String promotoraEmprInsercionTH = request.getParameter("promotoraEmprInsercionTH");
                String promotoraEmprInsercionTHValidado = request.getParameter("promotoraEmprInsercionTHValidado");
                String numeroTotalEntAgrupacion = request.getParameter("numeroTotalEntAgrupacion");
                String planIgualdad = request.getParameter("planIgualdad");
                String planIgualdadValidado = request.getParameter("planIgualdadValidado");
                String certificadoCalidad = request.getParameter("certificadoCalidad");
                String certificadoCalidadValidado = request.getParameter("certificadoCalidadValidado");
                String aceptaNumeroSuperiorHoras = request.getParameter("aceptaNumeroSuperiorHoras");
                String segundosLocalesMismoAmbito = request.getParameter("segundosLocalesMismoAmbito");
                String segundosLocalesMismoAmbitoValidado = request.getParameter("segundosLocalesMismoAmbitoValidado");
                String entSinAnimoLucro = request.getParameter("entSinAnimoLucro");
                String sujetoDerPublico = request.getParameter("sujetoDerPublico");
                String compIgualdadOpcion = request.getParameter("compIgualdadOpcion");
                String entSinAnimoLucroValidado = request.getParameter("entSinAnimoLucroValidado");
                String entSujetaDerPublValidado = request.getParameter("entSujetaDerPublValidado");
                String compromisoIgualdadValidado = request.getParameter("compromisoIgualdadValidado");
                String certificadoCalidadLista = request.getParameter("certificadoCalidadLista");
                String certificadoCalidadListaValidado = request.getParameter("certificadoCalidadListaValidado");

                ColecEntidadVO entidad = new ColecEntidadVO();
                entidad.setNumExp(numExpediente);
                Long codEntidadLong = (codEntidad!=null && !codEntidad.equals("") ? Long.parseLong(codEntidad) : null);
                entidad.setCodEntidad(codEntidadLong);
                entidad = meLanbide48Manager.getEntidadPorCodigoYExpediente(entidad, codIdioma, adaptador);

                if (entidad == null) {
                    entidad = new ColecEntidadVO();
                    entidad.setNumExp(numExpediente);
                }else{
                    // estamos modificando, hay que comprobar que no se haya pasado de una Agrupacion a entidad Sencilla
                    String esAgrupacionEnBBD = entidad.getEsAgrupacion()!=null?entidad.getEsAgrupacion().toString():null;
                    if(esAgrupacionEnBBD!=esAsociacion && "0".equals(esAsociacion)){
                        // Si el valor actual es no Agrupacion(0) borramos las entidades existentes
                        meLanbide48Manager.eliminarDatosListaEntidadesAgrupacionxExpediente(numExpediente, adaptador);
                    }
                }
                
                entidad.setEjercicio(ejercicio);
                entidad.setAnioConvocatoria(ejercicio);
                entidad.setCif(cif);
                entidad.setNombre(nombre);
                entidad.setEsAgrupacion(esAsociacion != null && !esAsociacion.equals("") && !esAsociacion.equals("null") ? Integer.valueOf(esAsociacion) : null);
                entidad.setEsAgrupacionValidado(esAsociacionValidado != null && !esAsociacionValidado.equals("") && !esAsociacionValidado.equals("null") ? Integer.valueOf(esAsociacionValidado) : null);
                entidad.setCentroEspEmpTH(centroEspecialEmpTH != null && !centroEspecialEmpTH.equals("") && !centroEspecialEmpTH.equals("null") ? Integer.valueOf(centroEspecialEmpTH) : null);
                entidad.setCentroEspEmpTHValidado(centroEspecialEmpTHValidado != null && !centroEspecialEmpTHValidado.equals("") && !centroEspecialEmpTHValidado.equals("null") ? Integer.valueOf(centroEspecialEmpTHValidado) : null);
                entidad.setParticipanteMayorCentEcpEmpTH(particiMayorCentEspeEmpTH != null && !particiMayorCentEspeEmpTH.equals("") && !particiMayorCentEspeEmpTH.equals("null") ? Integer.valueOf(particiMayorCentEspeEmpTH) : null);
                entidad.setParticipanteMayorCentEcpEmpTHValidado(particiMayorCentEspeEmpTHValidado != null && !particiMayorCentEspeEmpTHValidado.equals("") && !particiMayorCentEspeEmpTHValidado.equals("null") ? Integer.valueOf(particiMayorCentEspeEmpTHValidado) : null);
                entidad.setEmpresaInsercionTH(empresaInsercionTH != null && !empresaInsercionTH.equals("") && !empresaInsercionTH.equals("null") ? Integer.valueOf(empresaInsercionTH) : null);
                entidad.setEmpresaInsercionTHValidado(empresaInsercionTHValidado != null && !empresaInsercionTHValidado.equals("") && !empresaInsercionTHValidado.equals("null") ? Integer.valueOf(empresaInsercionTHValidado) : null);
                entidad.setPromotorEmpInsercionTH(promotoraEmprInsercionTH != null && !promotoraEmprInsercionTH.equals("") && !promotoraEmprInsercionTH.equals("null") ? Integer.valueOf(promotoraEmprInsercionTH) : null);
                entidad.setPromotorEmpInsercionTHValidado(promotoraEmprInsercionTHValidado != null && !promotoraEmprInsercionTHValidado.equals("") && !promotoraEmprInsercionTHValidado.equals("null") ? Integer.valueOf(promotoraEmprInsercionTHValidado) : null);
                entidad.setNumTotalEntAgrupacion(numeroTotalEntAgrupacion != null && !numeroTotalEntAgrupacion.equals("") && !numeroTotalEntAgrupacion.equals("null") ? Integer.valueOf(numeroTotalEntAgrupacion) : null);
                entidad.setPlanIgualdad(planIgualdad != null && !planIgualdad.equals("") && !planIgualdad.equalsIgnoreCase("null") ? Integer.valueOf(planIgualdad) : null);
                entidad.setPlanIgualdadValidado(planIgualdadValidado != null && !planIgualdadValidado.equals("") && !planIgualdadValidado.equalsIgnoreCase("null") ? Integer.valueOf(planIgualdadValidado) : null);
                entidad.setCertificadoCalidad(certificadoCalidad != null && !certificadoCalidad.equals("") && !certificadoCalidad.equalsIgnoreCase("null") ? Integer.valueOf(certificadoCalidad) : null);
                entidad.setCertificadoCalidadValidado(certificadoCalidadValidado != null && !certificadoCalidadValidado.equals("") && !certificadoCalidadValidado.equalsIgnoreCase("null") ? Integer.valueOf(certificadoCalidadValidado) : null);
                entidad.setAceptaNumeroSuperiorHoras(aceptaNumeroSuperiorHoras != null && !aceptaNumeroSuperiorHoras.equals("") && !aceptaNumeroSuperiorHoras.equalsIgnoreCase("null") ? Integer.valueOf(aceptaNumeroSuperiorHoras) : null);
                entidad.setSegundosLocalesMismoAmbito(segundosLocalesMismoAmbito != null && !segundosLocalesMismoAmbito.equals("") && !segundosLocalesMismoAmbito.equalsIgnoreCase("null") ? Integer.valueOf(segundosLocalesMismoAmbito) : null);
                entidad.setSegundosLocalesMismoAmbitoValidado(segundosLocalesMismoAmbitoValidado != null && !segundosLocalesMismoAmbitoValidado.equals("") && !segundosLocalesMismoAmbitoValidado.equalsIgnoreCase("null") ? Integer.valueOf(segundosLocalesMismoAmbitoValidado) : null);
                //Nueva convocatoria
                entidad.setEntSinAnimoLucro(entSinAnimoLucro != null && !entSinAnimoLucro.equals("") && !entSinAnimoLucro.equalsIgnoreCase("null") ? Integer.valueOf(entSinAnimoLucro) : null);
                entidad.setEntSujetaDerPubl(sujetoDerPublico != null && !sujetoDerPublico.equals("") && !sujetoDerPublico.equalsIgnoreCase("null") ? Integer.valueOf(sujetoDerPublico) : null);
                entidad.setCompIgualdadOpcion(compIgualdadOpcion != null && !compIgualdadOpcion.equals("") && !compIgualdadOpcion.equalsIgnoreCase("null") ? Integer.valueOf(compIgualdadOpcion) : null);
                entidad.setEntSinAnimoLucroVal(entSinAnimoLucroValidado != null && !entSinAnimoLucroValidado.equals("") && !entSinAnimoLucroValidado.equalsIgnoreCase("null") ? Integer.valueOf(entSinAnimoLucroValidado) : null);
                entidad.setEntSujetaDerPublVal(entSujetaDerPublValidado != null && !entSujetaDerPublValidado.equals("") && !entSujetaDerPublValidado.equalsIgnoreCase("null") ? Integer.valueOf(entSujetaDerPublValidado) : null);
                entidad.setCompIgualdadOpcionVal(compromisoIgualdadValidado != null && !compromisoIgualdadValidado.equals("") && !compromisoIgualdadValidado.equalsIgnoreCase("null") ? Integer.valueOf(compromisoIgualdadValidado) : null);
                meLanbide48Manager.guardarDatosColec(numExpediente, entidad, adaptador); //representante
                meLanbide48ManagerColecEntidadCertCalidad.guardarDatosCertificadosCalidadSolicitudValidados(entidad,certificadoCalidadLista,certificadoCalidadListaValidado,adaptador);;
                //Actualizamos la lista de Entidades asoiadas en la request
                entidadesAgru = meLanbide48Manager.getListaEntidadesPorExpediente(numExpediente, adaptador);
            }
            else
            {
                codigoOperacion = "3";
            }
        }
        catch(Exception ex)
        {
            codigoOperacion = "2";
            log.error(ex.getMessage());
        }
        escribirListaEntidadesRequest(codigoOperacion, entidadesAgru, response);
        //escribirResultadoRepresentanteRequest(codigoOperacion, response);
    }
    
    public String cargarNuevaEntidad(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        // Ponemos en la request la entidad Padre:
        String codEntidadPadre = request.getParameter("codEntidadPadre");
        ColecEntidadEnAgrupacionVO entidadModif = new ColecEntidadEnAgrupacionVO();
        entidadModif.setCodEntidadPadreAgrup((codEntidadPadre!=null&& !codEntidadPadre.equals("")?Long.parseLong(codEntidadPadre):null));
        request.setAttribute("entidadModif", entidadModif);
        MeLanbideConvocatorias convocatoriaActiva=null;
        int codIdioma = MeLanbide48Utils.getIdiomaUsuarioFromRequest(request);
        AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        // Recogemos los datos de El decreto/Convocatoria Aplicable
        try {
            convocatoriaActiva = meLanbide48Manager.getDecretoAplicableExpediente(codOrganizacion, numExpediente, adapt);
            request.setAttribute("convocatoriaActiva", convocatoriaActiva);
        } catch (Exception ex) {
            log.error("Erro al tratar de leer los datos del decretoaplicable al expediente " + numExpediente, ex);
        }
        try {
            List<SelectItem> listaCertificadosCalidad = meLanbide48ManagerColecCertCalidad.getListaColecCertCalidad(codIdioma,adapt);
            request.setAttribute("listaCertificadosCalidad",listaCertificadosCalidad);
        }catch (Exception ex){
            log.error("Error al cargar datos de lista Certificados de calidad",ex);
        }
        try {
            List<SelectItem> listaCompromisoIgualdad = meLanbide48ManagerColecCompIgualdad.getListaColecCompIgualdad(codIdioma,adapt);
            request.setAttribute("listaCompromisoIgualdad",listaCompromisoIgualdad);
        }catch (Exception ex){
            log.error("Error al cargar datos del desplegable Compromiso de igualdad",ex);
        }
        return "/jsp/extension/melanbide48/nuevaEntidad.jsp?codOrganizacionModulo="+codOrganizacion;
    }
    
    public String cargarModificarEntidadAsociada(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            String opcion = request.getParameter("opcion");
            if(opcion != null)
            {
                String codEntidad = request.getParameter("codEntidad");
                String codEntidadPadre = request.getParameter("codEntidadPadre");
                AdaptadorSQLBD adaptadorSQLBD = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                int codIdioma = MeLanbide48Utils.getIdiomaUsuarioFromRequest(request);
                MeLanbideConvocatorias convocatoriaActiva=null;
                // Recogemos los datos de El decreto/Convocatoria Aplicable
                try {
                    convocatoriaActiva = meLanbide48Manager.getDecretoAplicableExpediente(codOrganizacion, numExpediente, adaptadorSQLBD);
                    request.setAttribute("convocatoriaActiva", convocatoriaActiva);
                } catch (Exception ex) {
                    log.error("Erro al tratar de leer los datos del decretoaplicable al expediente " + numExpediente, ex);
                }
                try {
                    List<SelectItem> listaCertificadosCalidad = meLanbide48ManagerColecCertCalidad.getListaColecCertCalidad(codIdioma,adaptadorSQLBD);
                    request.setAttribute("listaCertificadosCalidad",listaCertificadosCalidad);
                }catch (Exception ex){
                    log.error("Error al cargar datos de lista Certificados de calidad",ex);
                }
                try {
                    List<SelectItem> listaCompromisoIgualdad = meLanbide48ManagerColecCompIgualdad.getListaColecCompIgualdad(codIdioma,adaptadorSQLBD);
                    request.setAttribute("listaCompromisoIgualdad",listaCompromisoIgualdad);
                }catch (Exception ex){
                    log.error("Error al cargar datos del desplegable Compromiso de igualdad",ex);
                }

                if(codEntidad != null && !codEntidad.equals(""))
                {
                    ColecEntidadEnAgrupacionVO entidad = new ColecEntidadEnAgrupacionVO();
                    entidad.setCodEntidad(Long.parseLong(codEntidad));
                    entidad.setCodEntidadPadreAgrup(Long.parseLong(codEntidadPadre));
                    entidad.setNumExp(numExpediente);
                    entidad = meLanbide48Manager.getEntidadPorCodEntiPadre_CodEntidad_NumExp(entidad, adaptadorSQLBD);
                    if(entidad != null)
                    {
                        request.setAttribute("entidadModif", entidad);
                        
                        AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                        String desc = null;

                    }
                    if(opcion.equalsIgnoreCase("consultar"))
                    {
                        request.setAttribute("consulta", true);
                    }
                }
            }
        }
        catch(Exception ex)
        {
            log.error(ex.getMessage());
        }
        return "/jsp/extension/melanbide48/nuevaEntidad.jsp?codOrganizacionModulo="+codOrganizacion;
    }
    
    public void guardarEntidad(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        String codigoOperacion = "0";
        List<ColecEntidadEnAgrupacionVO> entidades = new ArrayList<ColecEntidadEnAgrupacionVO>();
        
        AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        try
        {
            
            Integer ejercicio = MeLanbide48Utils.getEjercicioDeExpediente(numExpediente);
            int codIdioma = MeLanbide48Utils.getIdiomaUsuarioFromRequest(request);
            if(ejercicio != null)
            {
                String codEntidad = request.getParameter("codEntidad");
                String cif = request.getParameter("cif");
                String nombre = request.getParameter("nombre");
                String esAsociacion = request.getParameter("esAsociacion");
                String centroEspecialEmpTH = request.getParameter("centroEspecialEmpTH");
                String particiMayorCentEspeEmpTH = request.getParameter("particiMayorCentEspeEmpTH");
                String empresaInsercionTH = request.getParameter("empresaInsercionTH");
                String promotoraEmprInsercionTH = request.getParameter("promotoraEmprInsercionTH");
                String numeroTotalEntAgrupacion = request.getParameter("numeroTotalEntAgrupacion");
                int numeroTotalEntAgrupacion_1 = numeroTotalEntAgrupacion!=null && !"".equals(numeroTotalEntAgrupacion) ? Integer.parseInt(numeroTotalEntAgrupacion):0;

                ColecEntidadVO entidad = new ColecEntidadVO();
                if(codEntidad != null && !codEntidad.equals(""))
                {
                    entidad.setCodEntidad(Long.parseLong(codEntidad));
                    entidad.setNumExp(numExpediente);
                    entidad = meLanbide48Manager.getEntidadPorCodigoYExpediente(entidad,codIdioma, adaptador);
                    // Si es una Actualizacion hay que comprobar los datos de Agrupacion
                    //Comprobamos si hay cambio de tipo de entidad (Agrupacio o no )
                    if(esAsociacion!=null && entidad!=null && Integer.parseInt(esAsociacion)!=(entidad.getEsAgrupacion())){
                        // Se han cambiado los datos. Sobre el tipo de entidad Agrupacion o no.
                        // Si es ahora no es Agrupación borramos los datos de : Lista Entidades, Trayectoria Especial y Trayectoria General de Entidades de la lista.
                        // Lo borramos con el numero de Expediente en cada tabla.
                        // Borramos solo en caso de que NO Sea Asociación.
                        if("0".equalsIgnoreCase(esAsociacion)){
                            meLanbide48Manager.eliminarDatosTrayectoriaExpecialxExpediente(entidad.getNumExp(), adaptador);
                            meLanbide48Manager.eliminarDatosTrayectoriaGeneralxExpediente(entidad.getNumExp(), adaptador);
                            meLanbide48Manager.eliminarDatosListaEntidadesAgrupacionxExpediente(entidad.getNumExp(), adaptador);
                        }
                    }
                }
                else
                {
                    entidad.setNumExp(numExpediente);
                }
                if(entidad == null)
                {
                    codigoOperacion = "3";
                }
                else
                {
                    entidad.setEjercicio(ejercicio);
                    entidad.setAnioConvocatoria(ejercicio);
                    entidad.setEsAgrupacion(esAsociacion!=null && !"".equalsIgnoreCase(esAsociacion)?Integer.parseInt(esAsociacion):null);
                    entidad.setCif(cif != null && !cif.equals("") ? cif.toUpperCase() : null);
                    entidad.setNombre(nombre != null && !nombre.equals("") ? nombre.toUpperCase() : null);
                    entidad.setCentroEspEmpTH(centroEspecialEmpTH != null && !centroEspecialEmpTH.equals("") && !centroEspecialEmpTH.equals("null") ? Integer.valueOf(centroEspecialEmpTH) : null);
                    entidad.setParticipanteMayorCentEcpEmpTH(particiMayorCentEspeEmpTH != null && !particiMayorCentEspeEmpTH.equals("") && !particiMayorCentEspeEmpTH.equals("null") ? Integer.valueOf(particiMayorCentEspeEmpTH) : null);
                    entidad.setEmpresaInsercionTH(empresaInsercionTH != null && !empresaInsercionTH.equals("") && !empresaInsercionTH.equals("null") ? Integer.valueOf(empresaInsercionTH) : null);
                    entidad.setPromotorEmpInsercionTH(promotoraEmprInsercionTH != null && !promotoraEmprInsercionTH.equals("") && !promotoraEmprInsercionTH.equals("null") ? Integer.valueOf(promotoraEmprInsercionTH) : null);
                    entidad.setNumTotalEntAgrupacion(numeroTotalEntAgrupacion_1);
                    
                    meLanbide48Manager.guardarColecEntidadVO(codOrganizacion, entidad, adaptador);
                }
                entidades = meLanbide48Manager.getListaEntidadesPorExpediente(numExpediente, adaptador);
            }
            else
            {
                codigoOperacion = "3";
            }
        }
        catch(Exception ex)
        {
            codigoOperacion = "2";
            log.error(ex.getMessage());
        }
        escribirListaEntidadesRequest(codigoOperacion, entidades, response);
    }
    
    public void guardarEntidadAsociada(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        String codigoOperacion = "0";
        ColecEntidadEnAgrupacionVO entidad = new ColecEntidadEnAgrupacionVO();
        List<ColecEntidadEnAgrupacionVO> entidades = null;
        AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        try
        {
            
            Integer ejercicio = MeLanbide48Utils.getEjercicioDeExpediente(numExpediente);
            if(ejercicio != null)
            {
                String codEntidadPadre = request.getParameter("codEntidadPadre");
                String codEntidad = request.getParameter("codEntidad");
                String cif = request.getParameter("cif");
                String nombre = request.getParameter("nombre");
                String porcentajeCompro = request.getParameter("porcentajeCompro");
                String centroEspecialEmpTH = request.getParameter("centroEspecialEmpTH");
                String particiMayorCentEspeEmpTH = request.getParameter("particiMayorCentEspeEmpTH");
                String empresaInsercionTH = request.getParameter("empresaInsercionTH");
                String promotoraEmprInsercionTH = request.getParameter("promotoraEmprInsercionTH");
                String planIgualdad = request.getParameter("planIgualdad");
                String certificadoCalidad = request.getParameter("certificadoCalidad");
                String entSinAnimoLucro = request.getParameter("entSinAnimoLucro");
                String sujetoDerPublico = request.getParameter("sujetoDerPublico");
                String compIgualdadOpcion = request.getParameter("compIgualdadOpcion");
                String certificadoCalidadLista = request.getParameter("certificadoCalidadLista");
                
                
                if(codEntidad != null && !codEntidad.equals(""))
                {
                    //Modificacion de Datos
                    entidad.setCodEntidadPadreAgrup(Long.parseLong(codEntidadPadre));
                    entidad.setCodEntidad(Long.parseLong(codEntidad));
                    entidad.setNumExp(numExpediente);
                    entidad = meLanbide48Manager.getEntidadPorCodEntiPadre_CodEntidad_NumExp(entidad, adaptador);
                }
                else
                {
                    entidad.setCodEntidadPadreAgrup(Long.parseLong(codEntidadPadre));
                    entidad.setNumExp(numExpediente);
                }
                if(entidad == null)
                {
                    codigoOperacion = "3";
                }
                else
                {
                    entidad.setCif(cif != null && !cif.equals("") ? cif.toUpperCase() : null);
                    entidad.setNombre(nombre != null && !nombre.equals("") ? nombre.toUpperCase() : null);
                    entidad.setCentroEspEmpTH(centroEspecialEmpTH != null && !centroEspecialEmpTH.equals("") && !centroEspecialEmpTH.equals("null") ? Integer.valueOf(centroEspecialEmpTH) : null);
                    entidad.setParticipanteMayorCentEcpEmpTH(particiMayorCentEspeEmpTH != null && !particiMayorCentEspeEmpTH.equals("") && !particiMayorCentEspeEmpTH.equals("null") ? Integer.valueOf(particiMayorCentEspeEmpTH) : null);
                    entidad.setEmpresaInsercionTH(empresaInsercionTH != null && !empresaInsercionTH.equals("") && !empresaInsercionTH.equals("null") ? Integer.valueOf(empresaInsercionTH) : null);
                    entidad.setPromotorEmpInsercionTH(promotoraEmprInsercionTH != null && !promotoraEmprInsercionTH.equals("") && !promotoraEmprInsercionTH.equals("null") ? Integer.valueOf(promotoraEmprInsercionTH) : null);
                    entidad.setPlanIgualdad(planIgualdad != null && !planIgualdad.isEmpty() && !planIgualdad.equalsIgnoreCase("null") ? Integer.valueOf(planIgualdad) : null);
                    entidad.setCertificadoCalidad(certificadoCalidad != null && !certificadoCalidad.isEmpty() && !certificadoCalidad.equals("null") ? Integer.valueOf(certificadoCalidad) : null);
                    Double porcentaje = (porcentajeCompro!=null&& !porcentajeCompro.equals("")?Double.parseDouble(porcentajeCompro):null);
                    entidad.setPorcentaCompromisoRealizacion(porcentaje);

                    entidad.setEntSinAnimoLucro(entSinAnimoLucro != null && !entSinAnimoLucro.equals("") && !entSinAnimoLucro.equalsIgnoreCase("null") ? Integer.valueOf(entSinAnimoLucro) : null);
                    entidad.setEntSujetaDerPubl(sujetoDerPublico != null && !sujetoDerPublico.equals("") && !sujetoDerPublico.equalsIgnoreCase("null") ? Integer.valueOf(sujetoDerPublico) : null);
                    entidad.setCompIgualdadOpcion(compIgualdadOpcion != null && !compIgualdadOpcion.equals("") && !compIgualdadOpcion.equalsIgnoreCase("null") ? Integer.valueOf(compIgualdadOpcion) : null);
                    
                    meLanbide48Manager.guardarColecEntidadEnAgrupacionVO(codOrganizacion, entidad, adaptador);
                    meLanbide48ManagerColecEntidadCertCalidad.guardarDatosCertificadosCalidadSolicitudValidados(MeLanbide48MappingUtils.getInstance().mapearDatosEntidadAsociadaEntidadPadre(entidad),certificadoCalidadLista,null,adaptador);;
                }
                entidades = meLanbide48Manager.getListaEntidadesPorExpediente(numExpediente, adaptador);
            }
            else
            {
                codigoOperacion = "3";
            }
        }
        catch(Exception ex)
        {
            codigoOperacion = "2";
            log.error(ex.getMessage());
        }
        escribirListaEntidadesRequest(codigoOperacion, entidades, response);
    }
    
    public void eliminarEntidadAsociada(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        String codigoOperacion = "0";
        List<ColecEntidadEnAgrupacionVO> entidades = new ArrayList<ColecEntidadEnAgrupacionVO>();
        AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        try
        {
            
            Integer ejercicio = MeLanbide48Utils.getEjercicioDeExpediente(numExpediente);
            if(ejercicio != null)
            {
                String codEntidad = request.getParameter("codEntidad");
                String codEntidadPadre = request.getParameter("codEntidadPadre");

                ColecEntidadEnAgrupacionVO entidad = null;
                if(codEntidad != null && !codEntidad.equals(""))
                {
                    entidad = new ColecEntidadEnAgrupacionVO();
                    entidad.setCodEntidad(Long.parseLong(codEntidad));
                    entidad.setCodEntidadPadreAgrup(Long.parseLong(codEntidadPadre));
                    entidad.setNumExp(numExpediente);
                    entidad = meLanbide48Manager.getEntidadPorCodEntiPadre_CodEntidad_NumExp(entidad, adaptador);
                }
                if(entidad == null)
                {
                    codigoOperacion = "3";
                }
                else
                {
                    meLanbide48Manager.eliminarColecEntidadEnAgrupacionVO(codOrganizacion, entidad, adaptador);
                    // Pendiente la gestion de borrado datos de las entidades asociadas  
                    // De momento Borro los datos de Porcentaje compromiso de relización porque los datos asociados a la entidad borrada
                    // No se visualizan, no nos entereamos del fallo en interfaz
                    meLanbide48Manager.eliminarColecComproRealizacionVOxEntExpte(codOrganizacion, entidad.getNumExp(), entidad.getCodEntidad(), adaptador);
                    // Eliminar datos de certificados de calidad
                    meLanbide48ManagerColecEntidadCertCalidad.deleteColecEntidadCertCalidadByIdEntidad(entidad.getCodEntidad().intValue(),adaptador);
                    
                }
                entidades = meLanbide48Manager.getListaEntidadesPorExpediente(numExpediente, adaptador);
            }
            else
            {
                codigoOperacion = "3";
            }
        }
        catch(Exception ex)
        {
            codigoOperacion = "2";
            log.error(ex.getMessage());
        }
        escribirListaEntidadesRequest(codigoOperacion, entidades, response);
    }
    public void eliminarEntidad(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        String codigoOperacion = "0";
        List<ColecEntidadEnAgrupacionVO> entidades = new ArrayList<ColecEntidadEnAgrupacionVO>();
        
        AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        try
        {
            
            Integer ejercicio = MeLanbide48Utils.getEjercicioDeExpediente(numExpediente);
            int codIdioma = MeLanbide48Utils.getIdiomaUsuarioFromRequest(request);
            if(ejercicio != null)
            {
                
                String codEntidad = request.getParameter("codEntidad");

                ColecEntidadVO entidad = null;
                if(codEntidad != null && !codEntidad.equals(""))
                {
                    entidad = new ColecEntidadVO();
                    entidad.setCodEntidad(Long.parseLong(codEntidad));
                    entidad.setNumExp(numExpediente);
                    entidad = meLanbide48Manager.getEntidadPorCodigoYExpediente(entidad, codIdioma, adaptador);
                }
                if(entidad == null)
                {
                    codigoOperacion = "3";
                }
                else
                {
                    meLanbide48Manager.eliminarColecEntidadVO(codOrganizacion, entidad, adaptador);
                }
                entidades = meLanbide48Manager.getListaEntidadesPorExpediente(numExpediente, adaptador);
            }
            else
            {
                codigoOperacion = "3";
            }
        }
        catch(Exception ex)
        {
            codigoOperacion = "2";
            log.error(ex.getMessage());
        }
        escribirListaEntidadesRequest(codigoOperacion, entidades, response);
    }
    
    public void guardarSolicitud(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        ColecSolicitudVO solicitud = null;
        String codigoOperacion = "0";
        try
        {
            String idSolicitud = request.getParameter("idSolicitud");
            String col1Araba = request.getParameter("col1Araba");
            String col1Bizkaia = request.getParameter("col1Bizkaia");
            String col1Gipuzkoa = request.getParameter("col1Gipuzkoa");
            String col2Araba = request.getParameter("col2Araba");
            String col2Bizkaia = request.getParameter("col2Bizkaia");
            String col2Gipuzkoa = request.getParameter("col2Gipuzkoa");
            String col3Araba = request.getParameter("col3Araba");
            String col3Bizkaia = request.getParameter("col3Bizkaia");
            String col3Gipuzkoa = request.getParameter("col3Gipuzkoa");
            String col4Araba = request.getParameter("col4Araba");
            String col4Bizkaia = request.getParameter("col4Bizkaia");
            String col4Gipuzkoa = request.getParameter("col4Gipuzkoa");
            
            
            if(numExpediente != null && !numExpediente.equals(""))
            {
                try
                {
                    solicitud = new ColecSolicitudVO();
                    solicitud.setNumExp(numExpediente);
                    solicitud = meLanbide48Manager.getSolicitudPorId((idSolicitud!=null && !idSolicitud.isEmpty() ? Integer.valueOf(idSolicitud):0), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                }
                catch(Exception ex)
                {
                    codigoOperacion = "3";
                }
            }
            
            if(codigoOperacion.equals("0"))
            {
                if(solicitud == null)
                {
                    solicitud = new ColecSolicitudVO();
                    solicitud.setNumExp(numExpediente);
                    solicitud.setEjercicio(MeLanbide48Utils.getEjercicioDeExpediente(numExpediente));
                }
                solicitud.setCol1Ar(col1Araba != null && !col1Araba.equals("") ? Integer.parseInt(col1Araba) : 0);
                solicitud.setCol1Bi(col1Bizkaia != null && !col1Bizkaia.equals("") ? Integer.parseInt(col1Bizkaia) : 0);
                solicitud.setCol1Gi(col1Gipuzkoa != null && !col1Gipuzkoa.equals("") ? Integer.parseInt(col1Gipuzkoa) : 0);
                solicitud.setCol2Ar(col2Araba != null && !col2Araba.equals("") ? Integer.parseInt(col2Araba) : 0);
                solicitud.setCol2Bi(col2Bizkaia != null && !col2Bizkaia.equals("") ? Integer.parseInt(col2Bizkaia) : 0);
                solicitud.setCol2Gi(col2Gipuzkoa != null && !col2Gipuzkoa.equals("") ? Integer.parseInt(col2Gipuzkoa) : 0);
                solicitud.setCol3Ar(col3Araba != null && !col3Araba.equals("") ? Integer.parseInt(col3Araba) : 0);
                solicitud.setCol3Bi(col3Bizkaia != null && !col3Bizkaia.equals("") ? Integer.parseInt(col3Bizkaia) : 0);
                solicitud.setCol3Gi(col3Gipuzkoa != null && !col3Gipuzkoa.equals("") ? Integer.parseInt(col3Gipuzkoa) : 0);
                solicitud.setCol4Ar(col4Araba != null && !col4Araba.equals("") ? Integer.parseInt(col4Araba) : 0);
                solicitud.setCol4Bi(col4Bizkaia != null && !col4Bizkaia.equals("") ? Integer.parseInt(col4Bizkaia) : 0);
                solicitud.setCol4Gi(col4Gipuzkoa != null && !col4Gipuzkoa.equals("") ? Integer.parseInt(col4Gipuzkoa) : 0);
                
                solicitud = meLanbide48Manager.guardarColecSolicitudVO(codOrganizacion, solicitud, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            }
        }
        catch(Exception ex)
        {
            codigoOperacion = "3";
        }
        escribirSolicitudRequest(codigoOperacion, solicitud, response);
    }
    public void guardarDatosComproRealxColeTH(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        ColecComproRealizacionVO compReal = null;
        List<ColecComproRealizacionVO> listacompReal = new ArrayList<ColecComproRealizacionVO>();
        String codigoOperacion = "0";
        String _codEntidades = (String)request.getParameter("codEntidades");
        List<String> codEntidades = new ArrayList<String>();
        try
        {
            if(_codEntidades!=null && !_codEntidades.equals("")){
                String[] arreglo=_codEntidades.split(ConstantesMeLanbide48.COMMA);
                codEntidades=Arrays.asList(arreglo);
            }
            // Formato txt : txtPCR_Col2_20_CodEntidad 
            String nombreTxtBase = "txtPCR_Col";
            String nombreTxtComodin = "_";
            String[] provincias = {"01", "20", "48"};
            // Recorremos los 4 colectivos
            for (int x = 1; x <= 4; x++) {
                    String nombreTxt = nombreTxtBase + x + nombreTxtComodin;
                    //Recorremos las Provincias
                    for (int y = 0; y < provincias.length; y++) {
                        nombreTxt += provincias[y] + nombreTxtComodin;
                        //Recorremos las entidades y comprobamos los campos
                        for (String codEntidad : codEntidades) {
                            // Formamos un Array de los datos que vienen en el nombre de la variable
                            // Recogemos el valor enviafdo de la request para esa variable
                            // Formamos ObjetValue y Añadimos a la lista para guardar.
                            String valorVariable = (String)request.getParameter(nombreTxt+codEntidad);
                            if(valorVariable==null || valorVariable.equals(""))
                                log.debug("Parametro no recibido en la request o el valor es vacio : " + (nombreTxt+codEntidad));
                            else
                                log.debug("Parametro recibido en la request : " + (nombreTxt+codEntidad) + ": " + valorVariable);
                                String[] arrayDatos=nombreTxt.split(ConstantesMeLanbide48.GUION_BAJO);
                                String colectivo = arrayDatos[1].substring(3);
                                String th = arrayDatos[2];
                                
                                ColecComproRealizacionVO dato = new ColecComproRealizacionVO();
                                Integer ejercicio = (numExpediente!=null && !numExpediente.equals("") ? Integer.valueOf(numExpediente.substring(0,4)):null);
                                dato.setEjercicio(ejercicio);
                                dato.setNumExpediente(numExpediente);
                                dato.setCodigoEntidad((!codEntidad.equals("")?Integer.valueOf(codEntidad):null));
                                dato.setColectivo((!colectivo.equals("")?Integer.valueOf(colectivo):null));
                                dato.setTerritorioHistorico((!th.equals("")?Integer.valueOf(th):null));
                                dato.setPorcentajeCompReal((valorVariable!=null && !valorVariable.equals("")? Double.parseDouble(valorVariable):null));
                                
                                listacompReal.add(dato);
                        } // Cierra For Entidades
                        //Reseteamos nombre element para que avance a la siguiente provinvia al continua el LOOP.
                        nombreTxt = nombreTxtBase + x + nombreTxtComodin;
                    } // For Provincias
                } // For Colectivos
            codigoOperacion = meLanbide48Manager.guardarDatosComproRealxColeTH(codOrganizacion, listacompReal, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        }
        catch(Exception ex)
        {
            log.error(" Error guardando datos de Compromiso relaizacion entidades : " + ex.getMessage(), ex);
            codigoOperacion = "3";
        }
        List<ColecComproRealizacionVO> listaDatos = meLanbide48Manager.getDatosEntidadadesPorcenComproRealixColecyTHxExpte(numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        escribirCompRealEntidadesRequest(codigoOperacion,listaDatos,response);
    }
    public void refrescarPestanaCompRealxColeTH(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        String codigoOperacion = "0";
        List<ColecComproRealizacionVO> listaDatos = null;
        // tipoRecarga = 1 --> Estructura Tabla Html ; 2 --> Datos Porcentajes de los txt
        String tipoRecarga = (String)request.getParameter("tipoRecarga");
        try
        {
            if("1".equals(tipoRecarga))
                listaDatos=meLanbide48Manager.getListaEntidadadesInterfazPorcenComproRealixColecyTHxExpte(numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            else if("2".equals(tipoRecarga)){
                listaDatos = meLanbide48Manager.getDatosEntidadadesPorcenComproRealixColecyTHxExpte(numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            }

        }
        catch(Exception ex)
        {
            log.error(" Error guardando datos de Compromiso relaizacion entidades : " + ex.getMessage(), ex);
            codigoOperacion = "3";
        }
        escribirCompRealEntidadesRequest(codigoOperacion,listaDatos,response);
    }
    
    public String cargarNuevoCentro(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            String colectivo = request.getParameter("colectivo");
            request.setAttribute("colectivo", colectivo);
            cargarCombosNuevoCentro(codOrganizacion, request);
            
            List<FilaColecCentrosVO> centros = new ArrayList<FilaColecCentrosVO>();
            try
            {
                //centros = meLanbide48Manager.getCentrosPorExpedienteYColectivo(numExpediente, Integer.parseInt(colectivo), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            }
            catch(Exception ex)
            {
                
            }
            
            if(centros != null)
            {
                request.setAttribute("listaCentros", centros);
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return "/jsp/extension/melanbide48/solicitudes/nuevoCentro.jsp";
    }
    
    public String cargarAltaEdicionUbicacionesCentroTrabajo(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            int codIdioma = MeLanbide48Utils.getIdiomaUsuarioFromRequest(request);            
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            String codigoUbicacion = request.getParameter("idTray");
            MeLanbideConvocatorias convocatoriaActiva=null;
            // Recogemos los datos de El decreto/Convocatoria Aplicable
            try {
                convocatoriaActiva = meLanbide48Manager.getDecretoAplicableExpediente(codOrganizacion, numExpediente, adapt);
                request.setAttribute("convocatoriaActiva", convocatoriaActiva);
            } catch (Exception ex) {
                log.error("Erro al tratar de leer los datos del decretoaplicable al expediente " + numExpediente, ex);
            }
            
            ColecUbicacionesCTVO ubicacionModif = null;
            if(codigoUbicacion != null && !codigoUbicacion.equals(""))
            {
                ubicacionModif = new ColecUbicacionesCTVO();
                ubicacionModif.setCodId((codigoUbicacion!=null && !codigoUbicacion.equals("")?(Long.parseLong(codigoUbicacion)):null));
                ubicacionModif.setNumExpediente(numExpediente);
                ubicacionModif = meLanbide48Manager.getUbicacionCTPorCodigoYExpediente(codOrganizacion,codIdioma,ubicacionModif, adapt);
                if(ubicacionModif != null)
                {
                    request.setAttribute("ubicacionModif", ubicacionModif);
                }
            }
            
            List<SelectItem> listaEntidades = new ArrayList<SelectItem>();
            try {
                listaEntidades = meLanbide48Manager.getListaSelectItemEntidadesPorExpediente(numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            } catch (Exception ex) {
                log.debug("Error al obtener la Lista entidad al cargar modificar o alta nueva ubicacion", ex);
            }
            request.setAttribute("listaEntidades", listaEntidades);
            
            List<SelectItem> listaColectivos = meLanbide48Manager.getListaColectivos(codIdioma, adapt);
            if(listaColectivos != null)
            {
                request.setAttribute("listaColectivos", listaColectivos);
            }
            
            List<SelectItem> listaAmbitos = meLanbide48Manager.getListaProvincias(adapt);
            if(listaAmbitos != null)
            {
                request.setAttribute("listaAmbitos", listaAmbitos);
            }
            // Si hay datos en ubicacionModif hay que cargar las listas de Comarcas y municipios.
            String modoDatos = request.getParameter("modoDatos");
            request.setAttribute("modoDatos", modoDatos);
            if(ubicacionModif!=null){
                List<SelectItem> listaComarcas = new ArrayList<SelectItem>();
                if(ubicacionModif.getCodTipoColectivo()!= null && !ubicacionModif.getCodTipoColectivo().isEmpty()){
                    if (convocatoriaActiva != null && Melanbide48DecretoExpedienteEnum.D2021_2023.getCodigoDecreto().equalsIgnoreCase(convocatoriaActiva.getDecretoCodigo())) {
                        listaComarcas = meLanbide48Manager.getAmbitoSolicitadoxColectivoConvocatoria(codIdioma,convocatoriaActiva.getId(),Integer.valueOf(ubicacionModif.getCodTipoColectivo()),adapt);
                    }
                    if (ubicacionModif.getTerritorioHist() != null) {
                        if (convocatoriaActiva != null && Melanbide48DecretoExpedienteEnum.D2021_2023.getCodigoDecreto().equalsIgnoreCase(convocatoriaActiva.getDecretoCodigo())) {
                            listaComarcas = meLanbide48Manager.getAmbitoSolicitadoxColectivoConvocatoriaTH(codIdioma, convocatoriaActiva.getId(), Integer.valueOf(ubicacionModif.getCodTipoColectivo()), ubicacionModif.getTerritorioHist(), adapt);
                        } else {
                            listaComarcas = meLanbide48Manager.getComarcasPorAmbito(ubicacionModif.getTerritorioHist(), adapt);
                        }
                    }
                }
                if (listaComarcas != null) {
                    request.setAttribute("listaComarcas", listaComarcas);
                }
                
                List<SelectItem> listaMunicipios = null;
                if (convocatoriaActiva != null && Melanbide48DecretoExpedienteEnum.D2021_2023.getCodigoDecreto().equalsIgnoreCase(convocatoriaActiva.getDecretoCodigo())) {
                    listaMunicipios = meLanbide48Manager.getMunicipiosPorConvocatoriaColectivoTHAmbitoSol(codIdioma,convocatoriaActiva.getId(), Integer.valueOf(ubicacionModif.getCodTipoColectivo()), ubicacionModif.getTerritorioHist(), ubicacionModif.getFkIdAmbitoSolicitado(), adapt);
                } else{
                    if (ubicacionModif.getComarca() != null) {
                        listaMunicipios = meLanbide48Manager.getMunicipiosPorComarca(Long.parseLong(ubicacionModif.getComarca().toString()),adapt);
                    }
                }
                if (listaMunicipios != null) {
                    request.setAttribute("listaMunicipios", listaMunicipios);
                }
            }
        }
        catch(Exception ex)
        {
            log.error("Error preparand datos para alta/edicion ubicacion CT", ex);
        }
        return "/jsp/extension/melanbide48/ubicaciones/centro/trabajo/altaModificacionUbicacionCT.jsp";
    }
    
    public String cargarModificarCentroPorParametros(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            request.setAttribute("porParametros", "1");
            
            String opcion = request.getParameter("opcion");
            if(opcion.equalsIgnoreCase("consultar"))
            {
                request.setAttribute("consulta", true);
            }
            
            String colectivo = request.getParameter("colectivo");
            String codigoCentro = request.getParameter("codigoCentro");
            String ambito = request.getParameter("ambito");
            String comarca = request.getParameter("comarca");
            String municipio = request.getParameter("municipio");
            String direccion = request.getParameter("direccion");
            
            ColecCentrosVO centro = new ColecCentrosVO();
            centro.setColectivo(Integer.parseInt(colectivo));
            centro.setCodCentro(Long.parseLong(codigoCentro));
            centro.setAmbito(Integer.parseInt(ambito));
            centro.setComarca(Long.parseLong(comarca));
            centro.setMun(Long.parseLong(municipio));
            centro.setDireccion(direccion);
            
            request.setAttribute("centroModif", centro);
            
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            
            int codIdioma = MeLanbide48Utils.getIdiomaUsuarioFromRequest(request);
            
            List<SelectItem> listaColectivos = meLanbide48Manager.getListaColectivos(codIdioma, adapt);
            if(listaColectivos != null)
            {
                request.setAttribute("listaColectivos", listaColectivos);
            }
            
            List<SelectItem> listaAmbitos = meLanbide48Manager.getListaProvincias(adapt);
            if(listaAmbitos != null)
            {
                request.setAttribute("listaAmbitos", listaAmbitos);
            }
            
            if(centro != null && centro.getAmbito() != null)
            {
                List<SelectItem> listaComarcas = meLanbide48Manager.getComarcasPorAmbito(centro.getAmbito(), adapt);
                if(listaComarcas != null)
                {
                    request.setAttribute("listaComarcas", listaComarcas);
                }
            }
            
            if(centro != null && centro.getComarca() != null)
            {
                List<SelectItem> listaMunicipios = meLanbide48Manager.getMunicipiosPorComarca(centro.getComarca(), adapt);
                if(listaColectivos != null)
                {
                    request.setAttribute("listaMunicipios", listaMunicipios);
                }
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return "/jsp/extension/melanbide48/solicitudes/modificarCentro.jsp";
    }
    
    public void cargarComarcasPorAmbito(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        String codProvincia = request.getParameter("codAmbito");
        String idBDConvocatoriaExpediente = request.getParameter("idBDConvocatoriaExpediente");
        String codigoConvocatoriaExpediente = request.getParameter("codigoConvocatoriaExpediente");
        String codColectivo = request.getParameter("codColectivo");
        List<SelectItem> listaAmbitos = new ArrayList<SelectItem>();
        try
        {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            Integer codIdioma = MeLanbide48Utils.getIdiomaUsuarioFromRequest(request);
            if(codigoConvocatoriaExpediente!=null && !codigoConvocatoriaExpediente.isEmpty() && !codigoConvocatoriaExpediente.equalsIgnoreCase("null") 
                    && "CONV_ANTE-2021".equalsIgnoreCase(codigoConvocatoriaExpediente)){
                listaAmbitos = meLanbide48Manager.getComarcasPorAmbito(Integer.parseInt(codProvincia),adapt);
            }else{
                listaAmbitos = meLanbide48Manager.getAmbitoSolicitadoxColectivoConvocatoriaTH(codIdioma,Integer.valueOf(idBDConvocatoriaExpediente),Integer.valueOf(codColectivo),Integer.valueOf(codProvincia),adapt);
            }
        }
        catch(Exception ex)
        {
            log.error("cargarComarcasPorAmbito", ex);
        }
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
            xmlSalida.append("<CODIGO_OPERACION>");
                xmlSalida.append("0");
            xmlSalida.append("</CODIGO_OPERACION>");
            for(SelectItem si : listaAmbitos)
            {
                xmlSalida.append("<COMARCA>");
                    xmlSalida.append("<CODIGO>");
                        xmlSalida.append(si.getCodigo());
                    xmlSalida.append("</CODIGO>");
                    xmlSalida.append("<DESCRIPCION>");
                        xmlSalida.append(si.getDescripcion());
                    xmlSalida.append("</DESCRIPCION>");
                xmlSalida.append("</COMARCA>");
            }
        xmlSalida.append("</RESPUESTA>");
        try{
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        }catch(Exception e){
            log.error("cargarComarcasPorAmbitocargarComarcasPorAmbito - escribiendo response", e);
        }//try-catch
    }
    
    public void cargarMunicipiosPorComarca(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        Long codComarca = (request.getParameter("codComarca")!=null && !request.getParameter("codComarca").isEmpty() && !request.getParameter("codComarca").equalsIgnoreCase("null")? Long.valueOf(request.getParameter("codComarca")) : 0);
        int codColectivo = (request.getParameter("codColectivo")!=null && !request.getParameter("codColectivo").isEmpty() && !request.getParameter("codColectivo").equalsIgnoreCase("null")? Integer.valueOf(request.getParameter("codColectivo")) : 0);
        int territorioHistorico = (request.getParameter("territorioHistorico")!=null && !request.getParameter("territorioHistorico").isEmpty() && !request.getParameter("territorioHistorico").equalsIgnoreCase("null")? Integer.valueOf(request.getParameter("territorioHistorico")) : 0);
        List<SelectItem> listaAmbitos = new ArrayList<SelectItem>();
        MeLanbideConvocatorias convocatoriaActiva = null;
        try
        {
            int codIdioma = MeLanbide48Utils.getIdiomaUsuarioFromRequest(request);            
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            // Recogemos los datos de El decreto/Convocatoria Aplicable
            try {
                convocatoriaActiva = meLanbide48Manager.getDecretoAplicableExpediente(codOrganizacion, numExpediente, adapt);
            } catch (Exception ex) {
                log.error("Erro al tratar de leer los datos del decretoaplicable al expediente " + numExpediente, ex);
            }
            if ((convocatoriaActiva==null)
                    ||(Melanbide48DecretoExpedienteEnum.DANTE_2021.getCodigoDecreto().equalsIgnoreCase(convocatoriaActiva.getDecretoCodigo()))) {
                listaAmbitos = meLanbide48Manager.getMunicipiosPorComarca(codComarca,adapt);
            } else{
                if (codColectivo == 1 || codColectivo == 2) {
                    listaAmbitos = meLanbide48Manager.getMunicipiosPorConvocatoriaColectivoTH(codIdioma, convocatoriaActiva.getId(), codColectivo, territorioHistorico, adapt);
                } else if (codColectivo == 3 || codColectivo == 4) {
                    listaAmbitos = meLanbide48Manager.getMunicipiosPorConvocatoriaColectivoTHAmbitoSol(codIdioma, convocatoriaActiva.getId(), codColectivo, territorioHistorico, codComarca.intValue(), adapt);
                }
            }
                
        }
        catch(Exception ex)
        {
            log.error("cargarMunicipiosPorComarca", ex);
        }
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
            xmlSalida.append("<CODIGO_OPERACION>");
                xmlSalida.append("0");
            xmlSalida.append("</CODIGO_OPERACION>");
            for(SelectItem si : listaAmbitos)
            {
                xmlSalida.append("<MUNICIPIO>");
                    xmlSalida.append("<CODIGO>");
                        xmlSalida.append(si.getCodigo());
                    xmlSalida.append("</CODIGO>");
                    xmlSalida.append("<DESCRIPCION>");
                        xmlSalida.append(si.getDescripcion());
                    xmlSalida.append("</DESCRIPCION>");
                xmlSalida.append("</MUNICIPIO>");
            }
        xmlSalida.append("</RESPUESTA>");
        try{
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        }catch(Exception e){
            e.printStackTrace();
        }//try-catch
    }
    
    public void buscarMunicipios(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        String codComarca = request.getParameter("codComarca");
        List<FilaBusquedaMunicipioVO> listaMunicipios = new ArrayList<FilaBusquedaMunicipioVO>();
        try
        {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            List<String> listaComarcas = new ArrayList<String>();
            if(codComarca == null || codComarca.equals(""))
            {
                String codAmbito = request.getParameter("codAmbito");
                listaComarcas = meLanbide48Manager.getCodigosComarcaPorAmbito(codAmbito, adapt);
            }
            else
            {
                listaComarcas.add(codComarca);
            }
            
            listaMunicipios = meLanbide48Manager.buscarMunicipios(listaComarcas, adapt);
        }
        catch(Exception ex)
        {
            
        }
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
            xmlSalida.append("<CODIGO_OPERACION>");
                xmlSalida.append("0");
            xmlSalida.append("</CODIGO_OPERACION>");
            for(FilaBusquedaMunicipioVO fila : listaMunicipios)
            {
                xmlSalida.append("<MUNICIPIO>");
                    xmlSalida.append("<CODIGO_MUN>");
                        xmlSalida.append(fila.getCodMunicipio());
                    xmlSalida.append("</CODIGO_MUN>");
                    xmlSalida.append("<COD_COMARCA>");
                        xmlSalida.append(fila.getCodComarca());
                    xmlSalida.append("</COD_COMARCA>");
                    xmlSalida.append("<DESC_COMARCA>");
                        xmlSalida.append(fila.getDescComarca());
                    xmlSalida.append("</DESC_COMARCA>");
                    xmlSalida.append("<NOMBRE_MUN>");
                        xmlSalida.append(fila.getDescMunicipio());
                    xmlSalida.append("</NOMBRE_MUN>");
                xmlSalida.append("</MUNICIPIO>");
            }
        xmlSalida.append("</RESPUESTA>");
        try{
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        }catch(Exception e){
            e.printStackTrace();
        }//try-catch
    }
    
    public void guardarCentros(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        String codigoOperacion = "0";
        List<FilaColecCentrosVO> filas = new ArrayList<FilaColecCentrosVO>();
        try
        {
            String codColectivo = request.getParameter("codColectivo");
            Integer colectivo = Integer.parseInt(codColectivo);
            String[] listaCentros = request.getParameterValues("centros");
            String[] listaCentrosEliminados = request.getParameterValues("centrosEliminados");
            List<ColecCentrosVO> centros = new ArrayList<ColecCentrosVO>();
            List<ColecCentrosVO> centrosEliminados = new ArrayList<ColecCentrosVO>();

            boolean hayDatos = false;
            boolean correcto = true;

            //Transformo el array de centros eliminados
            if(listaCentrosEliminados != null && listaCentrosEliminados.length > 0)
            {
                hayDatos = true;
                int pos = 0;
                ColecCentrosVO centro = null;
                String codigo = null;
                while(pos < listaCentrosEliminados.length && correcto)
                {
                    try
                    {
                        centro = new ColecCentrosVO();
                        codigo = listaCentrosEliminados[pos];
                        centro.setCodCentro(Long.parseLong(codigo));
                        centrosEliminados.add(centro);
                        pos++;
                    }
                    catch(Exception ex)
                    {
                        codigoOperacion = "3";
                        correcto = false;
                    }
                }
            }

            //Transformo el array de centros
            if(listaCentros != null && listaCentros.length > 0)
            {
                hayDatos = true;
                int pos = 0;
                ColecCentrosVO centro = null;
                String[] datos = null;
                Integer ejercicio = MeLanbide48Utils.getEjercicioDeExpediente(numExpediente);
                while(pos < listaCentros.length && correcto)
                {
                    try
                    {
                        datos = listaCentros[pos].split("\\$");
                        centro = new ColecCentrosVO();
                        centro.setCodCentro(datos[0] != null && !datos[0].equals("") ? Long.parseLong(datos[0]) : null);
                        centro.setColectivo(colectivo);
                        centro.setAmbito(Integer.parseInt(datos[1]));
                        centro.setComarca(Long.parseLong(datos[2]));
                        centro.setMun(Long.parseLong(datos[3]));
                        centro.setDireccion(datos[4]);
                        centro.setNumExp(numExpediente);
                        centro.setExpEje(ejercicio);
                        centros.add(centro);
                        pos++;
                    }
                    catch(Exception ex)
                    {
                        ex.printStackTrace();
                        codigoOperacion = "3";
                        correcto = false;
                    }
                }
            }

            if(correcto && hayDatos)
            {
                int codIdioma = MeLanbide48Utils.getIdiomaUsuarioFromRequest(request);

                try
                {
                    AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                    MeLanbide48Manager manager = MeLanbide48Manager.getInstance();
                    boolean result = manager.guardarCentros(codIdioma, centros, centrosEliminados, adaptador);
                    if(result)
                    {
                        //filas = manager.getCentrosPorExpedienteYColectivo(numExpediente, colectivo, adaptador);
                    }
                    else
                    {
                        codigoOperacion = "3";
                    }
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                    codigoOperacion = "1";
                }
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            codigoOperacion = "2";
        }
        //escribirListaCentrosRequest(codigoOperacion, filas, response);
    }
    
    public void guardarUbicacionCT(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        String codigoOperacion = "0";
        List<ColecUbicacionesCTVO> filas = new ArrayList<ColecUbicacionesCTVO>();
        ColecUbicacionesCTVO ubicacioncentro = new ColecUbicacionesCTVO();
        try
        {
            String codIdUbicacion = request.getParameter("idTray");
            
            String codEntidad = request.getParameter("codEntidad");
            String codColectivo = request.getParameter("codColectivo");
            String codAmbito = request.getParameter("codAmbito");
            String codComarca = request.getParameter("codComarca");
            String codMunicipio = request.getParameter("codMunicipio");
            String direccion = request.getParameter("direccion");
            String codigoPostal = request.getParameter("codigoPostal");
            String telefono = request.getParameter("telefono");
            String localPrevAprobado = request.getParameter("localPrevAprobado");
            String mantieneRequLocalApro = request.getParameter("mantieneRequLocalApro");
            String disponeEspacioComplWifi = request.getParameter("disponeEspacioComplWifi");
            
            if(codIdUbicacion != null && !codIdUbicacion.equals(""))
            {
                try
                {
                    ubicacioncentro.setCodId(Long.parseLong(codIdUbicacion));
                }
                catch(Exception ex)
                {
                    codigoOperacion = "3";
                }
            }
            ubicacioncentro.setNumExpediente(numExpediente);
            ubicacioncentro.setCodEntidad(Long.parseLong(codEntidad));
            ubicacioncentro.setCodTipoColectivo(codColectivo);
            ubicacioncentro.setTerritorioHist(Integer.parseInt(codAmbito));
            ubicacioncentro.setComarca(Integer.parseInt(codComarca));
            ubicacioncentro.setMunicipio(Integer.parseInt(codMunicipio));
            ubicacioncentro.setDireccion(direccion);
            ubicacioncentro.setCodigoPostal(codigoPostal);
            ubicacioncentro.setTelefono(telefono);
            ubicacioncentro.setLocalesPreviamenteAprobados((localPrevAprobado!=null && !localPrevAprobado.isEmpty() ? Integer.valueOf(localPrevAprobado) : null));
            ubicacioncentro.setMantieneRequisitosLocalesAprob((mantieneRequLocalApro!=null && !mantieneRequLocalApro.isEmpty() ? Integer.valueOf(mantieneRequLocalApro) : null));
            ubicacioncentro.setDisponeEspacioComplWifi((disponeEspacioComplWifi!=null && !disponeEspacioComplWifi.isEmpty() ? Integer.valueOf(disponeEspacioComplWifi) : null));
            try {
                AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                int codIdioma = MeLanbide48Utils.getIdiomaUsuarioFromRequest(request);

                boolean result = meLanbide48Manager.guardarUbicacionesCT(codIdioma, ubicacioncentro, adaptador);
                if (result) {
                    MeLanbideConvocatorias mlc = meLanbide48Manager.getDecretoAplicableExpediente(codOrganizacion, numExpediente, adaptador);
                    filas = meLanbide48Manager.getUbicacionesCTxNumExpediente(numExpediente, codIdioma,(mlc!=null?mlc.getId():null),adaptador);
                } else {
                    codigoOperacion = "3";
                }
            } catch (Exception ex) {
                codigoOperacion = "1";
            }
        }
        catch(Exception ex)
        {
            codigoOperacion = "2";
            ex.printStackTrace();
        }
        
        escribirListaUbicacionesCentrosTraRequest(codigoOperacion, filas, response);
    }
    
    public void getCentrosPorExpedienteYColectivo(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        String codigoOperacion = "0";
        List<FilaColecCentrosVO> filas = new ArrayList<FilaColecCentrosVO>();
        try
        {
            String codColectivo = request.getParameter("codColectivo");
            Integer colectivo = Integer.parseInt(codColectivo);

            try
            {
                AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                MeLanbide48Manager manager = MeLanbide48Manager.getInstance();
                //filas = manager.getCentrosPorExpedienteYColectivo(numExpediente, colectivo, adaptador);
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
                codigoOperacion = "1";
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            codigoOperacion = "2";
        }
        //escribirListaCentrosRequest(codigoOperacion, filas, response);
    }
    
    public void eliminarUbicacionCentroTraPorCodigoYExpediente(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        String codigoOperacion = "0";
        List<ColecUbicacionesCTVO> filas = new ArrayList<ColecUbicacionesCTVO>();
        try
        {
            String codCentro = request.getParameter("idTray");
            //String codColectivo = request.getParameter("codColectivo");
            if(codCentro != null && !codCentro.equals(""))  //&& codColectivo != null && !codColectivo.equals("")
            {
                Long codCen = null;
                try
                {
                   codCen = Long.parseLong(codCentro);
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }
                int codIdioma = MeLanbide48Utils.getIdiomaUsuarioFromRequest(request);
                if(codCen != null)
                {
                    try
                    {
                        AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                        
                        ColecUbicacionesCTVO centro = new ColecUbicacionesCTVO();
                        centro.setCodId(codCen);
                        centro.setNumExpediente(numExpediente);
                        boolean result = meLanbide48Manager.eliminarUbicacionesCentroTraPorCodigoYExpediente(centro, adapt);
                        
                        if(result)
                        {
                            MeLanbideConvocatorias mlc = meLanbide48Manager.getDecretoAplicableExpediente(codOrganizacion, numExpediente, adapt);
                            filas = meLanbide48Manager.getUbicacionesCTxNumExpediente(numExpediente,codIdioma,(mlc!=null?mlc.getId():null), adapt);
                        }
                        else
                        {
                            codigoOperacion = "2";
                        }
                    }
                    catch(Exception ex)
                    {
                        codigoOperacion = "1";
                        ex.printStackTrace();
                    }
                }
                else
                {
                    codigoOperacion = "3";
                }
            }
            else
            {
                codigoOperacion = "3";
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            codigoOperacion = "2";
        }
        //escribirListaUbicacionesCentrosTraRequest(codigoOperacion, filas, response);
        parsearRespuestasEnviarJSON(request, response, filas);
    }
    
    public void guardarTrayectoriaGeneral(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        String codigoOperacion = "0";
        try
        {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            //Recojo los parametros
            //si no ha ocurrido ningun error, prosigo
            //en caso contrario --> codigoOperacion = 3
            try
            {
                Integer numEntidades = Integer.parseInt(request.getParameter("numEntidades"));
                
                Integer ejercicio = MeLanbide48Utils.getEjercicioDeExpediente(numExpediente);
                
                ColecTrayVO tray = null;
                String parametro = null;
                
                List<ColecTrayVO> trayectorias = new ArrayList<ColecTrayVO>();
                
                for(int i = 0; i < numEntidades; i++)
                {
                    tray = new ColecTrayVO();
                    tray.setNumExp(numExpediente);
                    tray.setExpEje(ejercicio);
                    tray.setCodEntidad(Long.parseLong(request.getParameter("codigoEnt_"+i)));
                    tray = meLanbide48Manager.getTrayectoriaPorEntidadYExpediente(tray, adapt);
                    
                    if(tray == null)
                    {
                        tray = new ColecTrayVO();
                        tray.setNumExp(numExpediente);
                        tray.setExpEje(ejercicio);
                        tray.setCodEntidad(Long.parseLong(request.getParameter("codigoEnt_"+i)));
                    }
                    
                    
                    //dec327
                    parametro = request.getParameter("chk_dec327_2007_"+i);
                    tray.setDec327_2007(parametro != null && !parametro.equals("") ? Integer.parseInt(parametro) : null);
                    parametro = request.getParameter("chk_dec327_2008_"+i);
                    tray.setDec327_2008(parametro != null && !parametro.equals("") ? Integer.parseInt(parametro) : null);
                    parametro = request.getParameter("chk_dec327_2009_"+i);
                    tray.setDec327_2009(parametro != null && !parametro.equals("") ? Integer.parseInt(parametro) : null);
                    parametro = request.getParameter("chk_dec327_2010_"+i);
                    tray.setDec327_2010(parametro != null && !parametro.equals("") ? Integer.parseInt(parametro) : null);
                    
                    //min94
                    parametro = request.getParameter("chk_min94_2007_"+i);
                    tray.setMin94_2007(parametro != null && !parametro.equals("") ? Integer.parseInt(parametro) : null);
                    parametro = request.getParameter("chk_min94_2008_"+i);
                    tray.setMin94_2008(parametro != null && !parametro.equals("") ? Integer.parseInt(parametro) : null);
                    parametro = request.getParameter("chk_min94_2009_"+i);
                    tray.setMin94_2009(parametro != null && !parametro.equals("") ? Integer.parseInt(parametro) : null);
                    parametro = request.getParameter("chk_min94_2010_"+i);
                    tray.setMin94_2010(parametro != null && !parametro.equals("") ? Integer.parseInt(parametro) : null);
                    parametro = request.getParameter("chk_min94_2011_"+i);
                    tray.setMin94_2011(parametro != null && !parametro.equals("") ? Integer.parseInt(parametro) : null);
                    parametro = request.getParameter("chk_min94_2012_"+i);
                    tray.setMin94_2012(parametro != null && !parametro.equals("") ? Integer.parseInt(parametro) : null);
                    parametro = request.getParameter("chk_min94_2013_"+i);
                    tray.setMin94_2013(parametro != null && !parametro.equals("") ? Integer.parseInt(parametro) : null);
                    parametro = request.getParameter("chk_min94_2014_"+i);
                    tray.setMin94_2014(parametro != null && !parametro.equals("") ? Integer.parseInt(parametro) : null);
                    parametro = request.getParameter("chk_min94_2015_"+i);
                    tray.setMin94_2015(parametro != null && !parametro.equals("") ? Integer.parseInt(parametro) : null);
                    parametro = request.getParameter("chk_min94_2016_"+i);
                    tray.setMin94_2016(parametro != null && !parametro.equals("") ? Integer.parseInt(parametro) : null);
                    parametro = request.getParameter("chk_min94_2017_"+i);
                    tray.setMin94_2017(parametro != null && !parametro.equals("") ? Integer.parseInt(parametro) : null);
                    parametro = request.getParameter("chk_min94_2018_"+i);
                    tray.setMin94_2018(parametro != null && !parametro.equals("") ? Integer.parseInt(parametro) : null);
                    
                    //min98
                    parametro = request.getParameter("chk_min98_2007_"+i);
                    tray.setMin98_2007(parametro != null && !parametro.equals("") ? Integer.parseInt(parametro) : null);
                    parametro = request.getParameter("chk_min98_2008_"+i);
                    tray.setMin98_2008(parametro != null && !parametro.equals("") ? Integer.parseInt(parametro) : null);
                    parametro = request.getParameter("chk_min98_2009_"+i);
                    tray.setMin98_2009(parametro != null && !parametro.equals("") ? Integer.parseInt(parametro) : null);
                    parametro = request.getParameter("chk_min98_2010_"+i);
                    tray.setMin98_2010(parametro != null && !parametro.equals("") ? Integer.parseInt(parametro) : null);
                    parametro = request.getParameter("chk_min98_2011_"+i);
                    tray.setMin98_2011(parametro != null && !parametro.equals("") ? Integer.parseInt(parametro) : null);
                    parametro = request.getParameter("chk_min98_2012_"+i);
                    tray.setMin98_2012(parametro != null && !parametro.equals("") ? Integer.parseInt(parametro) : null);
                    parametro = request.getParameter("chk_min98_2013_"+i);
                    tray.setMin98_2013(parametro != null && !parametro.equals("") ? Integer.parseInt(parametro) : null);
                    parametro = request.getParameter("chk_min98_2014_"+i);
                    tray.setMin98_2014(parametro != null && !parametro.equals("") ? Integer.parseInt(parametro) : null);
                    parametro = request.getParameter("chk_min98_2015_"+i);
                    tray.setMin98_2015(parametro != null && !parametro.equals("") ? Integer.parseInt(parametro) : null);
                    parametro = request.getParameter("chk_min98_2016_"+i);
                    tray.setMin98_2016(parametro != null && !parametro.equals("") ? Integer.parseInt(parametro) : null);
                    parametro = request.getParameter("chk_min98_2017_"+i);
                    tray.setMin98_2017(parametro != null && !parametro.equals("") ? Integer.parseInt(parametro) : null);
                    parametro = request.getParameter("chk_min98_2018_"+i);
                    tray.setMin98_2018(parametro != null && !parametro.equals("") ? Integer.parseInt(parametro) : null);
                    
                    //tas03
                    parametro = request.getParameter("chk_tas03_2007_"+i);
                    tray.setTas03_2007(parametro != null && !parametro.equals("") ? Integer.parseInt(parametro) : null);
                    parametro = request.getParameter("chk_tas03_2008_"+i);
                    tray.setTas03_2008(parametro != null && !parametro.equals("") ? Integer.parseInt(parametro) : null);
                    parametro = request.getParameter("chk_tas03_2009_"+i);
                    tray.setTas03_2009(parametro != null && !parametro.equals("") ? Integer.parseInt(parametro) : null);
                    parametro = request.getParameter("chk_tas03_2010_"+i);
                    tray.setTas03_2010(parametro != null && !parametro.equals("") ? Integer.parseInt(parametro) : null);
                    parametro = request.getParameter("chk_tas03_2011_"+i);
                    tray.setTas03_2011(parametro != null && !parametro.equals("") ? Integer.parseInt(parametro) : null);
                    parametro = request.getParameter("chk_tas03_2012_"+i);
                    tray.setTas03_2012(parametro != null && !parametro.equals("") ? Integer.parseInt(parametro) : null);
                    parametro = request.getParameter("chk_tas03_2013_"+i);
                    tray.setTas03_2013(parametro != null && !parametro.equals("") ? Integer.parseInt(parametro) : null);
                    parametro = request.getParameter("chk_tas03_2014_"+i);
                    tray.setTas03_2014(parametro != null && !parametro.equals("") ? Integer.parseInt(parametro) : null);
                    parametro = request.getParameter("chk_tas03_2015_"+i);
                    tray.setTas03_2015(parametro != null && !parametro.equals("") ? Integer.parseInt(parametro) : null);
                    parametro = request.getParameter("chk_tas03_2016_"+i);
                    tray.setTas03_2016(parametro != null && !parametro.equals("") ? Integer.parseInt(parametro) : null);
                    parametro = request.getParameter("chk_tas03_2017_"+i);
                    tray.setTas03_2017(parametro != null && !parametro.equals("") ? Integer.parseInt(parametro) : null);
                    parametro = request.getParameter("chk_tas03_2018_"+i);
                    tray.setTas03_2018(parametro != null && !parametro.equals("") ? Integer.parseInt(parametro) : null);
                    
                    
                    //act5603
                    parametro = request.getParameter("txt_act5603_"+i);
                    tray.setAct56_03(parametro != null && !parametro.equals("") ? Long.parseLong(parametro) : null);
                    
                    //lan_2011
                    parametro = request.getParameter("chk_lan_2011_"+i);
                    tray.setLan_2011(parametro != null && !parametro.equals("") ? Integer.parseInt(parametro) : null);
                    
                    //lan:2013
                    parametro = request.getParameter("chk_lan_2013_"+i);
                    tray.setLan_2013(parametro != null && !parametro.equals("") ? Integer.parseInt(parametro) : null);
                    //lan:2014
                    parametro = request.getParameter("chk_lan_2014_"+i);
                    tray.setLan_2014(parametro != null && !parametro.equals("") ? Integer.parseInt(parametro) : null);
                    //lan:2013
                    parametro = request.getParameter("chk_lan_2015_"+i);
                    tray.setLan_2015(parametro != null && !parametro.equals("") ? Integer.parseInt(parametro) : null);
                    parametro = request.getParameter("chk_lan_2017_"+i);
                    tray.setLan_2017(parametro != null && !parametro.equals("") ? Integer.parseInt(parametro) : null);
                    
                    //otros
                    parametro = request.getParameter("txt_otros_"+i);
                    tray.setLan_otros(parametro != null && !parametro.equals("") ? Long.parseLong(parametro) : null);
                    
                    trayectorias.add(tray);
                }
                try
                {
                    //llamo al manager para guardar
                    boolean resultado = meLanbide48Manager.guardarTrayectoriaGeneralEntidades(trayectorias, adapt);
                    
                    if(resultado == false)
                    {
                        codigoOperacion = "4";
                    }
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                    codigoOperacion = "4";
                }
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
                codigoOperacion = "3";
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            codigoOperacion = "2";
        }
        escribirResultadoGuardarTrayectoriaGeneralRequest(codigoOperacion, response);
    }
    
    
    
    public String cargarNuevaActividadTrayectoria(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            String colectivo = request.getParameter("colectivo");
            String modoDatos = request.getParameter("modoDatos");
            String idTrayEsp = request.getParameter("idTrayEsp");
            String codEntidad = request.getParameter("codEntidad");
            request.setAttribute("colectivo", colectivo);
            request.setAttribute("modoDatos", modoDatos); // 0 Nueva Actividad / 1 Modificar actividad
            
            //cargarCombosNuevaActividadTrayectoria(codOrganizacion, numExpediente,  request);
            List<SelectItem> listaEntidades = new ArrayList<SelectItem>();
            try
            {
                listaEntidades = meLanbide48Manager.getListaSelectItemEntidadesPorExpediente(numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            }
            catch(Exception ex)
            {
                log.debug("Error al obtener la Lista entidad al cargar modificar o alta nueva actividad", ex);
            }
            request.setAttribute("listaEntidades", listaEntidades);
            //if("1".equalsIgnoreCase(modoDatos)){
            ColecTrayEspVO trayecEspModif = new ColecTrayEspVO();
            trayecEspModif.setNumExp(numExpediente);
            trayecEspModif.setColectivo(colectivo!=null?Integer.parseInt(colectivo):null);
            trayecEspModif.setCodTrayEsp(idTrayEsp!=null?Long.parseLong(idTrayEsp):null);
            trayecEspModif.setCodEntidad(codEntidad!=null?Long.parseLong(codEntidad):null);
            trayecEspModif.setExpEje(MeLanbide48Utils.getEjercicioDeExpediente(numExpediente));
            trayecEspModif=meLanbide48Manager.getTrayectoriaEspecifica(trayecEspModif, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            request.setAttribute("trayecEspModif", trayecEspModif);
//            List<FilaTrayEspVO> listaTrayectorias = meLanbide48Manager.getListaTrayectoriasEspecificasPorExpedienteYColectivo(tray, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
//            if(listaTrayectorias != null)
//            {
//                request.setAttribute("listaTrayectorias", listaTrayectorias);
//            }
            //}
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return "/jsp/extension/melanbide48/experiencia/especifica/art53/nuevaActividad.jsp";
    }
    public String cargarAltaEdicionTrayGenOtrosProgramas(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        log.debug("cargarAltaEdicionTrayGenOtrosProgramas - Begin()");
        try
        {
            String modoDatos = request.getParameter("modoDatos");
            String idTray = request.getParameter("idTray");
            String codEntidad = request.getParameter("codEntidad");
            request.setAttribute("modoDatos", modoDatos); // 0 Nueva Actividad / 1 Modificar actividad

            List<SelectItem> listaEntidades = new ArrayList<SelectItem>();
            try
            {
                listaEntidades = meLanbide48Manager.getListaSelectItemEntidadesPorExpediente(numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            }
            catch(Exception ex)
            {
                log.debug("Error al obtener la Lista entidad al cargar modificar o alta nueva actividad", ex);
            }
            request.setAttribute("listaEntidades", listaEntidades);

            ColecTrayOtroProgramaVO trayecModif = new ColecTrayOtroProgramaVO();
            trayecModif.setNumExpediente(numExpediente);
            trayecModif.setCodIdOtroPrograma(idTray!=null?Integer.parseInt(idTray):null);
            trayecModif.setCodEntidad(codEntidad!=null?Integer.parseInt(codEntidad):null);
            trayecModif.setEjercicio(MeLanbide48Utils.getEjercicioDeExpediente(numExpediente));
            trayecModif=meLanbide48Manager.getTrayectoriaGeneralOtrosProgramas(trayecModif, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            request.setAttribute("trayecModif", trayecModif);

        }
        catch(Exception ex)
        {
            log.error("Error al cargarAltaEdicionTrayGenOtrosProgramas : ", ex);
        }
        return "/jsp/extension/melanbide48/experiencia/general/altaEdicionPrograma.jsp";
    }
    public String cargarAltaEdicionTrayGenActividades(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        log.debug("cargarAltaEdicionTrayGenActividades - Begin()");
        try
        {
            String modoDatos = request.getParameter("modoDatos");
            String idTray = request.getParameter("idTray");
            String codEntidad = request.getParameter("codEntidad");
            request.setAttribute("modoDatos", modoDatos); // 0 Nueva Actividad / 1 Modificar actividad

            List<SelectItem> listaEntidades = new ArrayList<SelectItem>();
            try
            {
                listaEntidades = meLanbide48Manager.getListaSelectItemEntidadesPorExpediente(numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            }
            catch(Exception ex)
            {
                log.debug("Error al obtener la Lista entidad al cargar modificar o alta nueva actividad", ex);
            }
            request.setAttribute("listaEntidades", listaEntidades);

            ColecTrayActividadVO trayecModif = new ColecTrayActividadVO();
            trayecModif.setNumExpediente(numExpediente);
            trayecModif.setCodIdActividad(idTray!=null?Integer.parseInt(idTray):null);
            trayecModif.setCodEntidad(codEntidad!=null?Integer.parseInt(codEntidad):null);
            trayecModif.setEjercicio(MeLanbide48Utils.getEjercicioDeExpediente(numExpediente));
            trayecModif=meLanbide48Manager.getTrayectoriaGeneralActividades(trayecModif, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            request.setAttribute("trayecModif", trayecModif);

        }
        catch(Exception ex)
        {
            log.error("Error al cargarAltaEdicionTrayGenActividades : ", ex);
        }
        return "/jsp/extension/melanbide48/experiencia/general/altaEdicionActvidades.jsp";
    }
        
    public void guardarTrayectoriasEspecificas(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        log.debug("guardarTrayectoriasEspecificas - Begin()");
        String codigoOperacion = "0";
        List<FilaTrayEspVO> filas = new ArrayList<FilaTrayEspVO>();
        try
        {
            String codColectivo = request.getParameter("codColectivo");
            String idTrayEsp = request.getParameter("idTrayEsp");
            String codEntidad = request.getParameter("codEntidad");
            String codEntidadOld = request.getParameter("codEntidadOld");
            String nombreAdm = request.getParameter("nombreAdm");
            String descAct = request.getParameter("descAct");
            String idiomaUsuario = request.getParameter("idiomaUsuario");
            log.debug("Parametros : codColectivo,idTrayEsp,codEntidad = " + codColectivo+","+idTrayEsp+","+codEntidad);
            try {
                ColecTrayEspVO tray = new ColecTrayEspVO();
                AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                tray.setCodTrayEsp(idTrayEsp!=null && !idTrayEsp.equals("")?Long.parseLong(idTrayEsp):null);
                log.debug("ASignada ID Trayectoria "+(tray.getCodTrayEsp()!=null?tray.getCodTrayEsp():""));
                tray.setCodEntidad(codEntidad!=null&& !codEntidad.equals("")?Long.parseLong(codEntidad):null);
                tray.setNumExp(numExpediente);
                tray.setExpEje(MeLanbide48Utils.getEjercicioDeExpediente(numExpediente));
                tray.setColectivo(codColectivo!=null&& !codColectivo.equals("")?Integer.parseInt(codColectivo):null);
                tray.setNombreAdm(nombreAdm);
                tray.setDescActividad(descAct);
                boolean result = meLanbide48Manager.guardarTrayectoriaEspecifica(tray,adaptador);
                if (result) {
                    filas = meLanbide48Manager.getListaTrayectoriasEspecificasPorExpedienteYColectivo(tray, adaptador);
                } else {
                    codigoOperacion = "3";
                }
            } catch (Exception ex) {
                log.error("Error al guardar una trayectoria especifica", ex);
                codigoOperacion = "1";
            }
        }
        catch(Exception ex)
        {
            log.error("Error al guardar una trayectoria especifica", ex);
            codigoOperacion = "2";
        }
        escribirResultadoGuardarTrayectoriaEspecificaRequest(codigoOperacion, filas, response);
        log.debug("guardarTrayectoriasEspecificas - End()");
    }
    
    public void guardarTrayectoriaGeneralActividades(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        log.debug("guardarTrayectoriaGeneralActividades - Begin()");
        String codigoOperacion = "0";
        List<ColecTrayActividadVO> filas = new ArrayList<ColecTrayActividadVO>();
        try
        {
            String idTray = request.getParameter("idTray");
            String codEntidad = request.getParameter("codEntidad");
            String descActividad = request.getParameter("descActividad");
            String fechaInicio = request.getParameter("fechaInicio");
            String fechaFin = request.getParameter("fechaFin");
            try {
                ColecTrayActividadVO tray = new ColecTrayActividadVO();
                AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                tray.setCodIdActividad(idTray!=null && !idTray.equals("")?Integer.parseInt(idTray):null);
                tray.setCodEntidad(codEntidad!=null && !codEntidad.equals("")?Integer.parseInt(codEntidad):null);
                tray.setNumExpediente(numExpediente);
                tray.setEjercicio(MeLanbide48Utils.getEjercicioDeExpediente(numExpediente));
                tray.setDesActividadyServPublEmp(descActividad);
                tray.setFechaInicio(MeLanbide48Utils.parsearString_ddmmyyyy_Fecha(fechaInicio));
                tray.setFechaFin(MeLanbide48Utils.parsearString_ddmmyyyy_Fecha(fechaFin));
                boolean result = meLanbide48Manager.guardarTrayectoriaGeneralActividades(tray,adaptador);
                if (result) {
                    filas = meLanbide48Manager.getListaTrayGeneralActividadesxNumExp(tray, adaptador);
                } else {
                    codigoOperacion = "3";
                }
            } catch (Exception ex) {
                log.error("Error al guardar una trayectoria general otros programas", ex);
                codigoOperacion = "1";
            }
        }
        catch(Exception ex)
        {
            log.error("Error al guardar una trayectoria general otros programas", ex);
            codigoOperacion = "2";
        }
        escribirResultadoGuardarTrayectoriaGralActividadesRequest(codigoOperacion, filas, response);
        log.debug("guardarTrayectoriaGeneralActividades - End()");
    }
    public void guardarTrayectoriaGeneralOtrosProgramas(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        log.debug("guardarTrayectoriaGeneralOtrosProgramas - Begin()");
        String codigoOperacion = "0";
        List<ColecTrayOtroProgramaVO> filas = new ArrayList<ColecTrayOtroProgramaVO>();
        try
        {
            String idTray = request.getParameter("idTray");
            String codEntidad = request.getParameter("codEntidad");
            String programa = request.getParameter("programa");
            String fechaInicio = request.getParameter("fechaInicio");
            String fechaFin = request.getParameter("fechaFin");
            try {
                ColecTrayOtroProgramaVO tray = new ColecTrayOtroProgramaVO();
                AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                tray.setCodIdOtroPrograma(idTray!=null && !idTray.equals("")?Integer.parseInt(idTray):null);
                tray.setCodEntidad(codEntidad!=null && !codEntidad.equals("")?Integer.parseInt(codEntidad):null);
                tray.setNumExpediente(numExpediente);
                tray.setEjercicio(MeLanbide48Utils.getEjercicioDeExpediente(numExpediente));
                tray.setPrograma(programa);
                tray.setFechaInicio(MeLanbide48Utils.parsearString_ddmmyyyy_Fecha(fechaInicio));
                tray.setFechaFin(MeLanbide48Utils.parsearString_ddmmyyyy_Fecha(fechaFin));
                boolean result = meLanbide48Manager.guardarTrayectoriaGeneralOtrosProgramas(tray,adaptador);
                if (result) {
                    filas = meLanbide48Manager.getListaTrayGeneralOtrosProgramasxNumExp(tray, adaptador);
                } else {
                    codigoOperacion = "3";
                }
            } catch (Exception ex) {
                log.error("Error al guardar una trayectoria general otros programas", ex);
                codigoOperacion = "1";
            }
        }
        catch(Exception ex)
        {
            log.error("Error al guardar una trayectoria general otros programas", ex);
            codigoOperacion = "2";
        }
        escribirResultadoGuardarTrayectoriaGralOtrosProgramasRequest(codigoOperacion, filas, response);
        log.debug("guardarTrayectoriaGeneralOtrosProgramas - End()");
    }
    
    public void eliminarTrayectoriaEspecifica(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        String codigoOperacion = "0";
        List<FilaTrayEspVO> filas = new ArrayList<FilaTrayEspVO>();
        try
        {
            String codColectivo = request.getParameter("codColectivo");
            String idTrayEsp = request.getParameter("idTrayEsp");
            String codEntidad = request.getParameter("codEntidad");
            Integer ejercicio = MeLanbide48Utils.getEjercicioDeExpediente(numExpediente);
            String idiomaUsuario = request.getParameter("idiomaUsuario");
            try {
                ColecTrayEspVO tray = new ColecTrayEspVO();
                AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                tray.setCodTrayEsp(idTrayEsp!=null && !idTrayEsp.equals("")?Long.parseLong(idTrayEsp):null);
                tray.setCodEntidad(codEntidad!=null&& !codEntidad.equals("")?Long.parseLong(codEntidad):null);
                tray.setNumExp(numExpediente);
                tray.setExpEje(MeLanbide48Utils.getEjercicioDeExpediente(numExpediente));
                tray.setColectivo(codColectivo!=null&& !codColectivo.equals("")?Integer.parseInt(codColectivo):null);
                meLanbide48Manager.eliminarTrayectoriaEspecial(tray, adaptador);
                //if (result) {
                    filas = meLanbide48Manager.getListaTrayectoriasEspecificasPorExpedienteYColectivo(tray, adaptador);
                //} else {
                //    codigoOperacion = "3";
                //}
            } catch (Exception ex) {
                log.error("Error al eliminar una trayectoria especifica", ex);
                codigoOperacion = "1";
            }
        }
        catch(Exception ex)
        {
            log.error("Error al eliminar una trayectoria especifica", ex);
            codigoOperacion = "2";
        }
        escribirResultadoGuardarTrayectoriaEspecificaRequest(codigoOperacion, filas, response);
    }
    
    public void eliminarTrayectoriaGeneralOtrosProgramas(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        String codigoOperacion = "0";
        List<ColecTrayOtroProgramaVO> filas = new ArrayList<ColecTrayOtroProgramaVO>();
        try
        {
            String idTray = request.getParameter("idTray");
            String codEntidad = request.getParameter("codEntidad");
            Integer ejercicio = MeLanbide48Utils.getEjercicioDeExpediente(numExpediente);
            String idiomaUsuario = request.getParameter("idiomaUsuario");
            try {
                ColecTrayOtroProgramaVO tray = new ColecTrayOtroProgramaVO();
                AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                tray.setCodIdOtroPrograma(idTray!=null && !idTray.equals("")?Integer.parseInt(idTray):null);
                tray.setCodEntidad(codEntidad!=null&& !codEntidad.equals("")?Integer.parseInt(codEntidad):null);
                tray.setNumExpediente(numExpediente);
                tray.setEjercicio(ejercicio);
                meLanbide48Manager.eliminarTrayectoriaGralOtrosProgramas(tray, adaptador);
                filas = meLanbide48Manager.getListaTrayGeneralOtrosProgramasxNumExp(tray, adaptador);
            } catch (Exception ex) {
                log.error("Error al eliminar una trayectoria gral otros programas", ex);
                codigoOperacion = "1";
            }
        }
        catch(Exception ex)
        {
            log.error("Error al eliminar una trayectoria gral otros programas", ex);
            codigoOperacion = "2";
        }
        escribirResultadoGuardarTrayectoriaGralOtrosProgramasRequest(codigoOperacion, filas, response);
    }
    
    public void eliminarTrayectoriaGeneralActividades(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        String codigoOperacion = "0";
        List<ColecTrayActividadVO> filas = new ArrayList<ColecTrayActividadVO>();
        try
        {
            String idTray = request.getParameter("idTray");
            String codEntidad = request.getParameter("codEntidad");
            Integer ejercicio = MeLanbide48Utils.getEjercicioDeExpediente(numExpediente);
            String idiomaUsuario = request.getParameter("idiomaUsuario");
            try {
                ColecTrayActividadVO tray = new ColecTrayActividadVO();
                AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                tray.setCodIdActividad(idTray!=null && !idTray.equals("")?Integer.parseInt(idTray):null);
                tray.setCodEntidad(codEntidad!=null&& !codEntidad.equals("")?Integer.parseInt(codEntidad):null);
                tray.setNumExpediente(numExpediente);
                tray.setEjercicio(ejercicio);
                meLanbide48Manager.eliminarTrayectoriaGralActividades(tray, adaptador);
                filas = meLanbide48Manager.getListaTrayGeneralActividadesxNumExp(tray, adaptador);
            } catch (Exception ex) {
                log.error("Error al eliminar una trayectoria gral actividades", ex);
                codigoOperacion = "1";
            }
        }
        catch(Exception ex)
        {
            log.error("Error al eliminar una trayectoria gral actividades", ex);
            codigoOperacion = "2";
        }
        escribirResultadoGuardarTrayectoriaGralActividadesRequest(codigoOperacion, filas, response);
    }

    private String cargarSubpestanaAsociaciones_DatosColec(String numExpediente, AdaptadorSQLBD adapt, HttpServletRequest request)
    {
        try
        {
            ColecEntidadVO entidad = new ColecEntidadVO();
            entidad.setNumExp(numExpediente);
            entidad = meLanbide48Manager.getEntidadInteresadaPorExpediente(entidad, adapt);
            request.setAttribute("entidad", entidad);
            
            // Si es una entidad, recogemos la lista de sus componentes 
            if(entidad!=null && entidad.getEsAgrupacion()!=null && 1==entidad.getEsAgrupacion()){
                List<ColecEntidadEnAgrupacionVO> entidades = meLanbide48Manager.getListaEntidadesEnAgrupacion(entidad, adapt);
                request.setAttribute("entidades", entidades);
            }
            
            // Representante Legal es campo Suplementario. NO hace falta recogerlo
            /*ColecRepresentanteVO representante = new ColecRepresentanteVO();
            representante.setNumExp(numExpediente);
            representante = meLanbide48Manager.getRepresentantePorExpediente(representante, adapt);
            request.setAttribute("representante", representante);
            */
            
        }
        catch(Exception ex)
        {
            log.error(ex.getStackTrace());
        }
        return "/jsp/extension/melanbide48/asociaciones.jsp";
    }
    
    private String cargarSubpestanaSolicitudes_DatosColec(String numExpediente, AdaptadorSQLBD adapt, HttpServletRequest request)
    {
        try
        {
            String url;
            try
            {
                url = cargarSubpestanaGeneral_Solicitudes(numExpediente, adapt, request);
                if(url != null)
                {
                    request.setAttribute("urlPestanaGeneral_solicitud", url);
                }
                
                url = cargarSubpestanaCol1_Solicitudes(numExpediente, adapt, request);
                if(url != null)
                {
                    request.setAttribute("urlPestanaCol1_solicitud", url);
                }
            }
            catch(Exception ex)
            {
                log.error(ex.getMessage());
            }
        }
        catch(Exception ex)
        {
            log.error(ex.getStackTrace());
        }
        return "/jsp/extension/melanbide48/solicitudes.jsp";
    }
    private String cargarSubpestanaDatosColecTHSol_PorcCompRealxColyTH(String numExpediente, AdaptadorSQLBD adapt, HttpServletRequest request)
    {
        try
        {
            String url;
            try
            {
                url = cargarSubpestanaColectivosYTH_Solicitados(numExpediente, adapt, request);
                if(url != null)
                {
                    request.setAttribute("urlPestanaDatos_colectivosSolicitados", url);
                }
                
                url = cargarSubpestanaPorcenComproRelaizaxColecyTH(numExpediente, adapt, request);
                if(url != null)
                {
                    request.setAttribute("urlPestanaDatos_compromisoRealxColeYTTHH", url);
                }
            }
            catch(Exception ex)
            {
                log.error(ex.getMessage());
            }
        }
        catch(Exception ex)
        {
            log.error(ex.getStackTrace());
        }
        return "/jsp/extension/melanbide48/solicitudes/general/pestSolicitudesGeneral.jsp";
    }
    
    private String cargarSubpestanaTrayectoriaGeneral(String numExpediente, AdaptadorSQLBD adapt, HttpServletRequest request)
    {
        try
        {
            String url;
            try
            {
                url = cargarSubpestanaTrayectoria_GeneralProgSubv(numExpediente, adapt, request);
                if(url != null)
                {
                    request.setAttribute("urlPestanaDatos_trayectoriaGeneralProgSubv", url);
                }
                
                url = cargarSubpestanaTrayectoria_OtrosProgramas(numExpediente, adapt, request);
                if(url != null)
                {
                    request.setAttribute("urlPestanaDatos_otrosProgramas", url);
                }
                
                url = cargarSubpestanaTrayectoria_Actividades(numExpediente, adapt, request);
                if(url != null)
                {
                    request.setAttribute("urlPestanaDatos_actividades", url);
                }
            }
            catch(Exception ex)
            {
                log.error(ex.getMessage());
            }
        }
        catch(Exception ex)
        {
            log.error(ex.getStackTrace());
        }
        return "/jsp/extension/melanbide48/experiencia/general/pestTrayectoriaGeneral.jsp";
    }
    
    private String cargarSubpestanaExperienciaAcreditable(String numExpediente, AdaptadorSQLBD adapt, HttpServletRequest request)
    {
        try
        {
            String url;
            try
            {
                url = cargarSubpestanaTrayectoria_expeAcredColectivo(numExpediente,1,adapt, request);
                if(url != null)
                {
                    request.setAttribute("urlPestanaDatos_expeAcredColectivo1", url);
                }
                
                url = cargarSubpestanaTrayectoria_expeAcredColectivo(numExpediente,2,adapt, request);
                if(url != null)
                {
                    request.setAttribute("urlPestanaDatos_expeAcredColectivo2", url);
                }
                
                url = cargarSubpestanaTrayectoria_expeAcredColectivo(numExpediente,3,adapt, request);
                if(url != null)
                {
                    request.setAttribute("urlPestanaDatos_expeAcredColectivo3", url);
                }
                url = cargarSubpestanaTrayectoria_expeAcredColectivo(numExpediente,4,adapt, request);
                if(url != null)
                {
                    request.setAttribute("urlPestanaDatos_expeAcredColectivo4", url);
                }
            }
            catch(Exception ex)
            {
                log.error("Error al cargar los datos para experienciaAcreditable - Subpestanas ",ex);
            }
        }
        catch(Exception ex)
        {
            log.error("Error al cargar los datos para experienciaAcreditable. ",ex);
        }
        return "/jsp/extension/melanbide48/experiencia/acreditable/experienciaAcreditable.jsp";
    }
    
    private String cargarSubpestanaGeneral_Solicitudes(String numExpediente, AdaptadorSQLBD adapt, HttpServletRequest request)
    {
        try
        {
            String url = null;
            url = cargarSubpestanaColectivosYTH_Solicitados(numExpediente, adapt, request);
            if(url != null)
            {
                request.setAttribute("urlPestanaColectivos_general", url);
            }

            url = cargarSubpestanaTrayectoria_GeneralProgSubv(numExpediente, adapt, request);
            if(url != null)
            {
                request.setAttribute("urlPestanaTrayectoria_general", url);
            }
        }
        catch(Exception ex)
        {
            log.error(ex.getMessage());
        }
        return "/jsp/extension/melanbide48/solicitudes/general/general.jsp";
    }
    
    private String cargarSubpestanaColectivosYTH_Solicitados(String numExpediente, AdaptadorSQLBD adapt, HttpServletRequest request)
    {
        try
        {
            int codIdioma = MeLanbide48Utils.getIdiomaUsuarioFromRequest(request);
            
            ColecSolicitudVO sol = new ColecSolicitudVO();
            sol.setNumExp(numExpediente);
            MeLanbideConvocatorias convocatoriaActiva = (MeLanbideConvocatorias) request.getAttribute("convocatoriaActiva");
            Integer idBDConvocatoriaExpediente = convocatoriaActiva != null && convocatoriaActiva.getId()!=null ? convocatoriaActiva.getId() : null;
            List<ColecSolicitudVO> listsol = meLanbide48Manager.getSolicitudPorExpediente(sol,codIdioma,idBDConvocatoriaExpediente, adapt);
            if(listsol != null)
            {
                request.setAttribute("listaSolicitudVO", listsol);
            }
        }
        catch(Exception ex)
        {
            log.error(ex.getStackTrace());
        }
        return "/jsp/extension/melanbide48/solicitudes/general/colectivos.jsp";
    }
    private String cargarSubpestanaPorcenComproRelaizaxColecyTH(String numExpediente, AdaptadorSQLBD adapt, HttpServletRequest request)
    {
        try
        {
            List<ColecComproRealizacionVO> listaDatosEntiComproReali = meLanbide48Manager.getListaEntidadadesInterfazPorcenComproRealixColecyTHxExpte(numExpediente, adapt);
            if(listaDatosEntiComproReali != null)
            {
                request.setAttribute("listaDatosEntiComproReali", listaDatosEntiComproReali);
                log.debug(" Datos cargados en la request, lista listaDatosEntiComproReali. Size " + listaDatosEntiComproReali.size());
            }
            List<ColecComproRealizacionVO> listaDatosEntiComproRealiValorPorcen = meLanbide48Manager.getDatosEntidadadesPorcenComproRealixColecyTHxExpte(numExpediente, adapt);
            if (listaDatosEntiComproRealiValorPorcen != null) {
                request.setAttribute("listaDatosEntiComproRealiValorPorcen", listaDatosEntiComproRealiValorPorcen);
                log.debug(" Datos cargados en la request, lista listaDatosEntiComproRealiValorPorcen. Size " + listaDatosEntiComproRealiValorPorcen.size());
            }
        }
        catch(Exception ex)
        {
            log.error("Error Cargando datos pantalla Compromiso de realizacon: " 
                     + ex.getMessage() + " - " + ex.getStackTrace());
        }
        return "/jsp/extension/melanbide48/solicitudes/general/porcenCompReali.jsp";
    }
    
    private String cargarSubpestanaTrayectoria_GeneralProgSubv(String numExpediente, AdaptadorSQLBD adapt, HttpServletRequest request)
    {
        try
        {
            ColecTrayVO trayEjemplo = new ColecTrayVO();
            trayEjemplo.setNumExp(numExpediente);
            List<ColecTrayVO> listaTrayectorias = meLanbide48Manager.getListaTrayectoriasPorExpediente(trayEjemplo, adapt);
            // Añadimos las entidades de la asociacion para formar la html
            //Entidad Padre
            ColecEntidadVO entidadPadre = new ColecEntidadVO();
            entidadPadre.setNumExp(numExpediente);
            entidadPadre=meLanbide48Manager.getEntidadInteresadaPorExpediente(entidadPadre, adapt);
            request.setAttribute("entidadPadre",entidadPadre);
            List<FilaEntidadVO> listaAsociaciones = meLanbide48Manager.getEntidadadesTrayectoriaGeneralxExpte(numExpediente,adapt);
            request.setAttribute("asociaciones", listaAsociaciones);
            String anoExp=numExpediente.substring(0,4);
            request.setAttribute("anoExp", anoExp);
            if(listaTrayectorias != null)
            {
                Map<Long, ColecTrayVO> mapaTrayectorias = new HashMap<Long, ColecTrayVO>();
                for(ColecTrayVO tray : listaTrayectorias)
                {
                    if(tray.getCodEntidad() != null)
                    {
                        mapaTrayectorias.put(tray.getCodEntidad(), tray);
                    }
                }
                request.setAttribute("trayectoriasGeneral", mapaTrayectorias);
            }
        }
        catch(Exception ex)
        {
            log.error(ex.getStackTrace());
        }
        return "/jsp/extension/melanbide48/experiencia/general/trayectoria.jsp";
    }
    private String cargarSubpestanaTrayectoria_expeAcredColectivo(String numExpediente,int colectivo,AdaptadorSQLBD adapt, HttpServletRequest request)
    {
        String url="/jsp/extension/melanbide48/experiencia/acreditable/experienciaAcreditableError.jsp";
        try
        {
            List<ColecTrayectoriaEntidad> datos = meLanbide48Manager.getListaTrayectoriaAcreditableColectivo(numExpediente, colectivo, adapt);
            switch(colectivo){
                case 1:
                    request.setAttribute("listaColecTrayectoriaEntidadCol1", datos);
                    url="/jsp/extension/melanbide48/experiencia/acreditable/experienciaAcreditableCol1.jsp";
                    break;
                case 2:
                    request.setAttribute("listaColecTrayectoriaEntidadCol2", datos);
                    url="/jsp/extension/melanbide48/experiencia/acreditable/experienciaAcreditableCol2.jsp";
                    break;
                case 3:
                    request.setAttribute("listaColecTrayectoriaEntidadCol3", datos);
                    url="/jsp/extension/melanbide48/experiencia/acreditable/experienciaAcreditableCol3.jsp";
                    break;
                case 4:
                    request.setAttribute("listaColecTrayectoriaEntidadCol4", datos);
                    url="/jsp/extension/melanbide48/experiencia/acreditable/experienciaAcreditableCol4.jsp";
                    break;
            }
            return url;
        }
        catch(Exception ex)
        {
            log.error("Error cargarSubpestanaTrayectoria_expeAcredColectivo" , ex);
        }
        return url;
    }
    
    private String cargarSubpestanaTrayectoria_OtrosProgramas(String numExpediente, AdaptadorSQLBD adapt, HttpServletRequest request)
    {
        try
        {
            ColecTrayOtroProgramaVO trayEjemplo = new ColecTrayOtroProgramaVO();
            trayEjemplo.setNumExpediente(numExpediente);
            List<ColecTrayOtroProgramaVO> listaTrayGenralOtrosProg = meLanbide48Manager.getListaTrayGeneralOtrosProgramasxNumExp(trayEjemplo, adapt);
            request.setAttribute("listaTrayGenralOtrosProg", listaTrayGenralOtrosProg);
        }
        catch(Exception ex)
        {
            log.error(ex.getStackTrace());
        }
        return "/jsp/extension/melanbide48/experiencia/general/otrosProgramas.jsp";
    }
    private String cargarSubpestanaTrayectoria_Actividades(String numExpediente, AdaptadorSQLBD adapt, HttpServletRequest request)
    {
        try
        {
            ColecTrayActividadVO trayEjemplo = new ColecTrayActividadVO();
            trayEjemplo.setNumExpediente(numExpediente);
            List<ColecTrayActividadVO> listaTrayGenralActividades = meLanbide48Manager.getListaTrayGeneralActividadesxNumExp(trayEjemplo, adapt);
            request.setAttribute("listaTrayGenralActividades", listaTrayGenralActividades);
        }
        catch(Exception ex)
        {
            log.error(ex.getStackTrace());
        }
        return "/jsp/extension/melanbide48/experiencia/general/actividades.jsp";
    }
    
    private String cargarSubpestanaCol1_Solicitudes(String numExpediente, AdaptadorSQLBD adapt, HttpServletRequest request)
    {
        try
        {
            String url = null;
            url = cargarSubpestanaCentros_Col1(numExpediente, adapt, request);
            if(url != null)
            {
                request.setAttribute("urlPestanaCentros_colectivo1", url);
            }

            url = cargarSubpestanaTrayectoria_Col1(numExpediente, adapt, request);
            if(url != null)
            {
                request.setAttribute("urlPestanaTrayectoria_colectivo1", url);
            }
        }
        catch(Exception ex)
        {
            log.error(ex.getMessage());
        }
        return "/jsp/extension/melanbide48/solicitudes/colectivo1/colectivo1.jsp";
    }
    
    private String cargarSubpestanaCentros_Col1(String numExpediente, AdaptadorSQLBD adapt, HttpServletRequest request)
    {
        try
        {
            List<FilaColecCentrosVO> centros=null;// = meLanbide48Manager.getCentrosPorExpedienteYColectivo(numExpediente, ConstantesMeLanbide48.CODIGO_COLECTIVO_1, adapt);
            request.setAttribute("centrosCol1", centros);
        }
        catch(Exception ex)
        {
            log.error(ex.getStackTrace());
        }
        return "/jsp/extension/melanbide48/solicitudes/colectivo1/colectivo1_centros.jsp";
    }
    
    private String cargarSubpestanaTrayectoria_Col1(String numExpediente, AdaptadorSQLBD adapt, HttpServletRequest request)
    {
        try
        {
            ColecTrayEspVO trayEjemplo = null;
            List<FilaTrayEspVO> trayectorias = null;
            trayEjemplo = new ColecTrayEspVO();
            trayEjemplo.setColectivo(1);
            trayEjemplo.setNumExp(numExpediente);
            trayectorias = meLanbide48Manager.getListaTrayectoriasEspecificasPorExpedienteYColectivo(trayEjemplo, adapt);

            if(trayectorias != null)
            {
                request.setAttribute("trayectoriasCol1", trayectorias);
            }
        }
        catch(Exception ex)
        {
            log.error(ex.getStackTrace());
        }
        return "/jsp/extension/melanbide48/solicitudes/colectivo1/colectivo1_tray.jsp";
    }
    private String cargarSubpestanaExperienciaPreviaArt53(String numExpediente, AdaptadorSQLBD adapt, HttpServletRequest request)
    {
        try
        {
            ColecTrayEspVO trayEjemplo = null;
            List<FilaTrayEspVO> trayectorias = null;
            trayEjemplo = new ColecTrayEspVO();
            trayEjemplo.setColectivo(1);
            trayEjemplo.setNumExp(numExpediente);
            trayectorias = meLanbide48Manager.getListaTrayectoriasEspecificasPorExpedienteYColectivo(trayEjemplo, adapt);
            // Cargamos los datos del colectivo 1.
            if(trayectorias != null)
            {
                request.setAttribute("trayectoriasCol1", trayectorias);
            }
            // Cargamos los datos del Colectivo 2
            trayEjemplo.setColectivo(2);
            trayectorias=null;
            trayectorias = meLanbide48Manager.getListaTrayectoriasEspecificasPorExpedienteYColectivo(trayEjemplo, adapt);
            if(trayectorias != null)
            {
                request.setAttribute("trayectoriasCol2", trayectorias);
            }
        }
        catch(Exception ex)
        {
            log.error(ex.getStackTrace());
        }
        return "/jsp/extension/melanbide48/experiencia/especifica/art53/expePreviaNecesaria.jsp";
    }
    
    private void escribirListaEntidadesRequest(String codigoOperacion, List<ColecEntidadEnAgrupacionVO> entidades, HttpServletResponse response)
    {
        log.debug("escribirListaEntidadesRequest - Begin()");
        StringBuilder xmlSalida = new StringBuilder();
        xmlSalida.append("<RESPUESTA>");
            xmlSalida.append("<CODIGO_OPERACION>");
                xmlSalida.append(codigoOperacion);
            xmlSalida.append("</CODIGO_OPERACION>");
        if(entidades!=null){    
            for (ColecEntidadEnAgrupacionVO entidad : entidades) {
                xmlSalida.append("<FILA>");
                xmlSalida.append("<COLEC_ENT_COD>");
                xmlSalida.append(entidad.getCodEntidad());
                xmlSalida.append("</COLEC_ENT_COD>");
                xmlSalida.append("<COLEC_ENT_AGRUP_COD>");
                xmlSalida.append(entidad.getCodEntidadPadreAgrup());
                xmlSalida.append("</COLEC_ENT_AGRUP_COD>");
                xmlSalida.append("<COLEC_NUMEXP>");
                xmlSalida.append(entidad.getNumExp());
                xmlSalida.append("</COLEC_NUMEXP>");
                xmlSalida.append("<COLEC_ENT_TIPO_CIF>");
                xmlSalida.append(entidad.getTipoCif());
                xmlSalida.append("</COLEC_ENT_TIPO_CIF>");
                xmlSalida.append("<COLEC_ENT_CIF>");
                xmlSalida.append(entidad.getCif());
                xmlSalida.append("</COLEC_ENT_CIF>");
                xmlSalida.append("<COLEC_ENT_NOMBRE>");
                xmlSalida.append(entidad.getNombre());
                xmlSalida.append("</COLEC_ENT_NOMBRE>");
                xmlSalida.append("<COLEC_ENT_CENTESPEMPTH>");
                xmlSalida.append(entidad.getCentroEspEmpTH());
                xmlSalida.append("</COLEC_ENT_CENTESPEMPTH>");
                xmlSalida.append("<COLEC_ENT_PARTMAYCEETH>");
                xmlSalida.append(entidad.getParticipanteMayorCentEcpEmpTH());
                xmlSalida.append("</COLEC_ENT_PARTMAYCEETH>");
                xmlSalida.append("<COLEC_ENT_EMPINSERCIONTH>");
                xmlSalida.append(entidad.getEmpresaInsercionTH());
                xmlSalida.append("</COLEC_ENT_EMPINSERCIONTH>");
                xmlSalida.append("<COLEC_ENT_PROMOEMPINSTH>");
                xmlSalida.append(entidad.getPromotorEmpInsercionTH());
                xmlSalida.append("</COLEC_ENT_PROMOEMPINSTH>");
                xmlSalida.append("<COLEC_ENT_PORCEN_COMPROM_REALI>");
                xmlSalida.append(entidad.getPorcentaCompromisoRealizacion());
                xmlSalida.append("</COLEC_ENT_PORCEN_COMPROM_REALI>");
                xmlSalida.append("<PLANIGUALDAD>");
                xmlSalida.append(entidad.getPlanIgualdad());
                xmlSalida.append("</PLANIGUALDAD>");
                xmlSalida.append("<CERTIFICADOCALIDAD>");
                xmlSalida.append(entidad.getCertificadoCalidad());
                xmlSalida.append("</CERTIFICADOCALIDAD>");
                xmlSalida.append("<ACEPTNUMEROSUPEHORAS>");
                xmlSalida.append(entidad.getAceptaNumeroSuperiorHoras());
                xmlSalida.append("</ACEPTNUMEROSUPEHORAS>");
                xmlSalida.append("<SEGUNDOLOCALMISMOAMB>");
                xmlSalida.append(entidad.getSegundosLocalesMismoAmbito());
                xmlSalida.append("</SEGUNDOLOCALMISMOAMB>");
                xmlSalida.append("</FILA>");
            }
        }
        xmlSalida.append("</RESPUESTA>");
        try{
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        }catch(IOException e){
            log.error(e.getStackTrace());
        }
        log.debug("escribirListaEntidadesRequest - End()");
    }

    private void escribirCompRealEntidadesRequest(String codigoOperacion, List<ColecComproRealizacionVO> entidadesComproReal, HttpServletResponse response)
    {
        log.debug("escribirCompRealEntidadesRequest - Begin()");
        StringBuilder xmlSalida = new StringBuilder();
        xmlSalida.append("<RESPUESTA>");
            xmlSalida.append("<CODIGO_OPERACION>");
                xmlSalida.append(codigoOperacion);
            xmlSalida.append("</CODIGO_OPERACION>");
        if(entidadesComproReal!=null){    
            for (ColecComproRealizacionVO entidad : entidadesComproReal) {
                xmlSalida.append("<FILA>");
                xmlSalida.append("<COLEC_COMPREAL_COD_ID>");
                xmlSalida.append(entidad.getCodigoID());
                xmlSalida.append("</COLEC_COMPREAL_COD_ID>");
                xmlSalida.append("<COLEC_COMPREAL_EJERCICIO>");
                xmlSalida.append(entidad.getEjercicio());
                xmlSalida.append("</COLEC_COMPREAL_EJERCICIO>");
                xmlSalida.append("<COLEC_COMPREAL_NUM_EXP>");
                xmlSalida.append(entidad.getNumExpediente());
                xmlSalida.append("</COLEC_COMPREAL_NUM_EXP>");
                xmlSalida.append("<COLEC_COMPREAL_COD_ENTIDAD>");
                xmlSalida.append(entidad.getCodigoEntidad());
                xmlSalida.append("</COLEC_COMPREAL_COD_ENTIDAD>");
                xmlSalida.append("<COLEC_COMPREAL_COLECTIVO>");
                xmlSalida.append(entidad.getColectivo());
                xmlSalida.append("</COLEC_COMPREAL_COLECTIVO>");
                xmlSalida.append("<COLEC_COMPREAL_TTHH>");
                xmlSalida.append(entidad.getTerritorioHistorico());
                xmlSalida.append("</COLEC_COMPREAL_TTHH>");
                xmlSalida.append("<COLEC_COMPREAL_PORCENT>");
                xmlSalida.append(entidad.getPorcentajeCompReal());
                xmlSalida.append("</COLEC_COMPREAL_PORCENT>");
                xmlSalida.append("<COLEC_NOMBRE_ENTIDAD>");
                xmlSalida.append(entidad.getNombreEntidad());
                xmlSalida.append("</COLEC_NOMBRE_ENTIDAD>");
                xmlSalida.append("</FILA>");
            }
        }
        xmlSalida.append("</RESPUESTA>");
        try{
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        }catch(IOException e){
            log.error(e.getStackTrace());
        }
        log.debug("escribirCompRealEntidadesRequest - End()");
    }
    
    private void escribirResultadoRepresentanteRequest(String codigoOperacion, HttpServletResponse response)
    {
        StringBuilder xmlSalida = new StringBuilder();
        xmlSalida.append("<RESPUESTA>");
            xmlSalida.append("<CODIGO_OPERACION>");
                xmlSalida.append(codigoOperacion);
            xmlSalida.append("</CODIGO_OPERACION>");
        xmlSalida.append("</RESPUESTA>");
        try{
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        }catch(IOException e){
            log.error(e.getStackTrace());
        }
    }


    private void escribirResultadoBusquedaTercero(String codigoOperacion, HashMap mapaResultados, HttpServletResponse response)
    {
        Vector resultados = (Vector)mapaResultados.get("resultados");
        StringBuilder xmlSalida = new StringBuilder();
        xmlSalida.append("<RESPUESTA>");
            xmlSalida.append("<CODIGO_OPERACION>");
                xmlSalida.append(codigoOperacion);
            xmlSalida.append("</CODIGO_OPERACION>");
        if(resultados != null && resultados.size() > 0)
        {
            xmlSalida.append("<RESULTADOS>");
            TercerosValueObject tercero;
            //En local al menos devuelve mas de un resultado, por eso se coge siempre el primero
            //Se podrÃ­a cambiar para que devuelva mÃ¡s de un tercero
            int numResultados = 1;//resultados.size();
            for(int i = 0; i < numResultados; i++)
            {
                xmlSalida.append("<TERCERO>");
                tercero = (TercerosValueObject)resultados.get(i);
                
                String nombre =tercero.getNombre().toUpperCase();
                nombre += " " + tercero.getApellido1();
                nombre += " " + tercero.getApellido2();
                
                    xmlSalida.append("<NOMBRE>");
                    xmlSalida.append(nombre);
                    xmlSalida.append("</NOMBRE>");
                xmlSalida.append("</TERCERO>");
            }
            xmlSalida.append("</RESULTADOS>");
        }
        List errores = (List)mapaResultados.get("errores");
        if(errores != null && errores.size() > 0)
        {
            xmlSalida.append("<ERRORES>");
            for(int i = 0; i < errores.size(); i++)
            {
                String error = (String)errores.get(i);
                xmlSalida.append("<ERROR>");
                    xmlSalida.append(error);
                xmlSalida.append("</ERROR>");
            }
            xmlSalida.append("</ERRORES>");
        }
        xmlSalida.append("</RESPUESTA>");
        try{
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        }catch(IOException e){
            log.error(e.getStackTrace());
        }//try-catch
    }


    private void escribirSolicitudRequest(String codigoOperacion, ColecSolicitudVO solicitud, HttpServletResponse response)
    {
        StringBuilder xmlSalida = new StringBuilder();
        xmlSalida.append("<RESPUESTA>");
            xmlSalida.append("<CODIGO_OPERACION>");
                xmlSalida.append(codigoOperacion);
            xmlSalida.append("</CODIGO_OPERACION>");
        if(solicitud != null && solicitud.getCodSolicitud() != null)
        {
            xmlSalida.append("<SOLICITUD>");
                xmlSalida.append("<ID_SOLICITUD>");
                    xmlSalida.append(solicitud.getCodSolicitud().toString());
                xmlSalida.append("</ID_SOLICITUD>");
            xmlSalida.append("</SOLICITUD>");
        }
        xmlSalida.append("</RESPUESTA>");
        try{
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        }catch(IOException e){
            log.error(e.getStackTrace());
        }//try-catch
    }
    
    private void escribirListaUbicacionesCentrosTraRequest(String codigoOperacion, List<ColecUbicacionesCTVO> centros, HttpServletResponse response)
    {
        StringBuilder xmlSalida = new StringBuilder();
        xmlSalida.append("<RESPUESTA>");
            xmlSalida.append("<CODIGO_OPERACION>");
                xmlSalida.append(codigoOperacion);
            xmlSalida.append("</CODIGO_OPERACION>");
        if(centros != null && !centros.isEmpty())
        {
            xmlSalida.append("<UBICACIONES>");
            for(ColecUbicacionesCTVO centro : centros)
            {
                xmlSalida.append("<UBICACION>");
                    xmlSalida.append("<COLEC_UBIC_CT_COD>");
                        xmlSalida.append(centro.getCodId());
                    xmlSalida.append("</COLEC_UBIC_CT_COD>");
                    xmlSalida.append("<COLEC_UBIC_CT_NUMEXP>");
                        xmlSalida.append(centro.getNumExpediente());
                    xmlSalida.append("</COLEC_UBIC_CT_NUMEXP>");
                    xmlSalida.append("<COLEC_UBIC_CT_TIPO>");
                        xmlSalida.append(centro.getCodTipoColectivo());
                    xmlSalida.append("</COLEC_UBIC_CT_TIPO>");
                    xmlSalida.append("<COLEC_UBIC_CT_CODENTIDAD>");
                        xmlSalida.append(centro.getCodEntidad()!= null ? centro.getCodEntidad(): "");
                    xmlSalida.append("</COLEC_UBIC_CT_CODENTIDAD>");
                    xmlSalida.append("<COLEC_UBIC_CT_TERRITORIO>");
                        xmlSalida.append(centro.getTerritorioHist()!= null ? centro.getTerritorioHist(): "");
                    xmlSalida.append("</COLEC_UBIC_CT_TERRITORIO>");
                    xmlSalida.append("<COLEC_UBIC_CT_NROCOMARCA>");
                        xmlSalida.append(centro.getComarca()!= null ? centro.getComarca(): "");
                    xmlSalida.append("</COLEC_UBIC_CT_NROCOMARCA>");
                    xmlSalida.append("<COLEC_UBIC_CT_MUNICIPIO>");
                        xmlSalida.append(centro.getMunicipio() != null ? centro.getMunicipio(): "");
                    xmlSalida.append("</COLEC_UBIC_CT_MUNICIPIO>");
                    xmlSalida.append("<COLEC_UBIC_CT_LOCALIDAD>");
                        xmlSalida.append(centro.getLocalidad()!= null ? centro.getLocalidad(): "");
                    xmlSalida.append("</COLEC_UBIC_CT_LOCALIDAD>");
                    xmlSalida.append("<COLEC_UBIC_CT_DIRECCION>");
                        xmlSalida.append(centro.getDireccion() != null ? centro.getDireccion() : "");
                    xmlSalida.append("</COLEC_UBIC_CT_DIRECCION>");
                    xmlSalida.append("<COLEC_UBIC_CT_PORTAL_DIR>");
                        xmlSalida.append(centro.getDireccionPortal()!= null ? centro.getDireccionPortal(): "");
                    xmlSalida.append("</COLEC_UBIC_CT_PORTAL_DIR>");
                    xmlSalida.append("<COLEC_UBIC_CT_PISO_DIR>");
                        xmlSalida.append(centro.getDireccionPiso()!= null ? centro.getDireccionPiso(): "");
                    xmlSalida.append("</COLEC_UBIC_CT_PISO_DIR>");
                    xmlSalida.append("<COLEC_UBIC_CT_LETRA_DIR>");
                        xmlSalida.append(centro.getDireccionLetra()!= null ? centro.getDireccionLetra(): "");
                    xmlSalida.append("</COLEC_UBIC_CT_LETRA_DIR>");
                    xmlSalida.append("<COLEC_UBIC_CT_CODPOSTAL>");
                        xmlSalida.append(centro.getCodigoPostal()!= null ? centro.getCodigoPostal(): "");
                    xmlSalida.append("</COLEC_UBIC_CT_CODPOSTAL>");
                    xmlSalida.append("<COLEC_UBIC_CT_TELEFONO>");
                        xmlSalida.append(centro.getTelefono()!= null ? centro.getTelefono(): "");
                    xmlSalida.append("</COLEC_UBIC_CT_TELEFONO>");
                    // Datos Extras
                    xmlSalida.append("<COLEC_ENT_CIF>");
                        xmlSalida.append(centro.getCifEntidad());
                    xmlSalida.append("</COLEC_ENT_CIF>");
                    xmlSalida.append("<COLEC_ENT_NOMBRE>");
                        xmlSalida.append(centro.getNombreEntidad());
                    xmlSalida.append("</COLEC_ENT_NOMBRE>");
                    xmlSalida.append("<DESC_TERRITORIO>");
                        xmlSalida.append(centro.getDescTerritorioHist());
                    xmlSalida.append("</DESC_TERRITORIO>");
                    xmlSalida.append("<DESC_COMARCA>");
                        xmlSalida.append(centro.getDescComarca());
                    xmlSalida.append("</DESC_COMARCA>");
                    xmlSalida.append("<DESC_MUNICPIO>");
                        xmlSalida.append(centro.getDescMunicipio());
                    xmlSalida.append("</DESC_MUNICPIO>");
                    xmlSalida.append("<COLEC_UBIC_CT_LOCALPREVAPRO>");
                    xmlSalida.append(centro.getLocalesPreviamenteAprobados()!= null ? centro.getLocalesPreviamenteAprobados(): "");
                    xmlSalida.append("</COLEC_UBIC_CT_LOCALPREVAPRO>");
                    xmlSalida.append("<COLEC_UBIC_CT_MATENREQ_LPA>");
                    xmlSalida.append(centro.getMantieneRequisitosLocalesAprob()!= null ? centro.getMantieneRequisitosLocalesAprob(): "");
                    xmlSalida.append("</COLEC_UBIC_CT_MATENREQ_LPA>");
                xmlSalida.append("</UBICACION>");
            }
            xmlSalida.append("</UBICACIONES>");
        }
        xmlSalida.append("</RESPUESTA>");
        try{
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        }catch(IOException e){
            log.error(e.getStackTrace());
        }//try-catch
    }
    
    private void escribirResultadoGuardarTrayectoriaGeneralRequest(String codigoOperacion, HttpServletResponse response)
    {
        StringBuilder xmlSalida = new StringBuilder();
        xmlSalida.append("<RESPUESTA>");
            xmlSalida.append("<CODIGO_OPERACION>");
                xmlSalida.append(codigoOperacion);
            xmlSalida.append("</CODIGO_OPERACION>");
        xmlSalida.append("</RESPUESTA>");
        try{
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        }catch(IOException e){
            log.error(e.getStackTrace());
        }//try-catch
    }
    
    private void escribirResultadoGuardarTrayectoriaEspecificaRequest(String codigoOperacion, List<FilaTrayEspVO> listaTrayectorias, HttpServletResponse response)
    {
        StringBuilder xmlSalida = new StringBuilder();
        xmlSalida.append("<RESPUESTA>");
            xmlSalida.append("<CODIGO_OPERACION>");
                xmlSalida.append(codigoOperacion);
            xmlSalida.append("</CODIGO_OPERACION>");
            xmlSalida.append("<TRAYECTORIAS>");
            for(FilaTrayEspVO tray : listaTrayectorias)
            {
                xmlSalida.append("<TRAYECTORIA>");
                    xmlSalida.append("<COLEC_COD_TRAY_ESP>");
                        xmlSalida.append(tray.getCodTray().toString());
                    xmlSalida.append("</COLEC_COD_TRAY_ESP>");
                    xmlSalida.append("<COLEC_COD_ENTIDAD>");
                        xmlSalida.append(tray.getCodEntidad() != null ? tray.getCodEntidad().toString() : "-");
                    xmlSalida.append("</COLEC_COD_ENTIDAD>");
                    xmlSalida.append("<COLEC_COLECTIVO>");
                        xmlSalida.append(tray.getColectivo().toString());
                    xmlSalida.append("</COLEC_COLECTIVO>");
                    xmlSalida.append("<COLEC_NOMBRE_ADM>");
                        xmlSalida.append(tray.getNombreAdm() != null ? tray.getNombreAdm() : "-");
                    xmlSalida.append("</COLEC_NOMBRE_ADM>");
                    xmlSalida.append("<COLEC_DESC_ACTIVIDAD>");
                        xmlSalida.append(tray.getDescAct() != null ? tray.getDescAct() : "-");
                    xmlSalida.append("</COLEC_DESC_ACTIVIDAD>");
                    xmlSalida.append("<COLEC_ENT_CIF>");
                        xmlSalida.append(tray.getCifEntidad()!= null ? tray.getCifEntidad(): "-");
                    xmlSalida.append("</COLEC_ENT_CIF>");
                    xmlSalida.append("<COLEC_ENT_NOMBRE>");
                        xmlSalida.append(tray.getNomEntidad()!= null ? tray.getNomEntidad() : "-");
                    xmlSalida.append("</COLEC_ENT_NOMBRE>");
                    xmlSalida.append("<COLEC_VALIDADA>");
                        xmlSalida.append(tray.getValidada() != null ? tray.getValidada().toString() : "-");
                    xmlSalida.append("</COLEC_VALIDADA>");
                    xmlSalida.append("<COLEC_NUMEXP>");
                        xmlSalida.append(tray.getNumExpediente());
                    xmlSalida.append("</COLEC_NUMEXP>");
                    xmlSalida.append("<COLEC_EXP_EJE>");
                        xmlSalida.append(tray.getEjercicio()!= null ? tray.getEjercicio().toString() : "-");
                    xmlSalida.append("</COLEC_EXP_EJE>");
                xmlSalida.append("</TRAYECTORIA>");
            }
            xmlSalida.append("</TRAYECTORIAS>");
        xmlSalida.append("</RESPUESTA>");
        try{
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        }catch(IOException e){
            log.error(e.getStackTrace());
        }//try-catch
    }
    
    public String refrescarDatosColec(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente)
    {
        return "0";
    }
    
    private void cargarCombosNuevoCentro(int codOrganizacion, HttpServletRequest request)
    {
        int codIdioma = MeLanbide48Utils.getIdiomaUsuarioFromRequest(request);
        //Combo COLECTIVO
        List<SelectItem> listaColectivos = new ArrayList<SelectItem>();
        try
        {
            
            listaColectivos = meLanbide48Manager.getListaColectivos(codIdioma, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        }
        catch(Exception ex)
        {
            
        }
        request.setAttribute("listaColectivos", listaColectivos);
        
        //Combo TERRITORIO HISTORICO
        List<SelectItem> listaTerHis = new ArrayList<SelectItem>();
        try
        {
            
            listaTerHis = meLanbide48Manager.getListaProvincias(this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        }
        catch(Exception ex)
        {
            
        }
        request.setAttribute("listaAmbitos", listaTerHis);
    }
    
    private void cargarCombosNuevaActividadTrayectoria(int codOrganizacion, String numExpediente, HttpServletRequest request)
    {
        int codIdioma = MeLanbide48Utils.getIdiomaUsuarioFromRequest(request);
        //Combo COLECTIVO
        List<SelectItem> listaColectivos = new ArrayList<SelectItem>();
        try
        {
            
            listaColectivos = meLanbide48Manager.getListaColectivos(codIdioma, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        }
        catch(Exception ex)
        {
            
        }
        request.setAttribute("listaColectivos", listaColectivos);
        
        //Combo ENTIDAD
        List<SelectItem> listaEntidades = new ArrayList<SelectItem>();
        try
        {
            
            listaEntidades = meLanbide48Manager.getListaSelectItemEntidadesPorExpediente(numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        }
        catch(Exception ex)
        {
            
        }
        request.setAttribute("listaEntidades", listaEntidades);
    }
     
    /**
     * OperaciÃ³n que recupera los datos de conexiÃ³n a la BBDD
     * @param codOrganizacion
     * @return AdaptadorSQLBD
     */
    private AdaptadorSQLBD getAdaptSQLBD(String codOrganizacion){
        if(log.isDebugEnabled()) log.debug("getConnection ( codOrganizacion = " + codOrganizacion + " ) : BEGIN");
        ResourceBundle config = ResourceBundle.getBundle("techserver");
        String gestor = config.getString("CON.gestor");
        String jndiGenerico = config.getString("CON.jndi");
        Connection conGenerico = null;
        Statement st = null;
        ResultSet rs = null;
        
        if(log.isDebugEnabled()){
            log.debug("getJndi =========> ");
            log.debug("parametro codOrganizacion: " + codOrganizacion);
            log.debug("gestor: " + gestor);
            log.debug("jndi: " + jndiGenerico);
        }

        AdaptadorSQLBD adapt = null;
        synchronized (this) {
            try{
                PortableContext pc = PortableContext.getInstance();
                if(log.isDebugEnabled())log.debug("He cogido el jndi: " + jndiGenerico);
                DataSource ds = (DataSource)pc.lookup(jndiGenerico, DataSource.class);
                // ConexiÃ³n al esquema genÃ©rico
                conGenerico = ds.getConnection();

                String sql = "SELECT EEA_BDE FROM A_EEA WHERE EEA_APL=" + ConstantesDatos.APP_GESTION_EXPEDIENTES + " AND AAE_ORG=" + codOrganizacion;
                st = conGenerico.createStatement();
                rs = st.executeQuery(sql);
                String jndi = null;
                while(rs.next()){
                    jndi = rs.getString("EEA_BDE");
                }//while(rs.next())

                st.close();
                rs.close();
                conGenerico.close();

                if(jndi!=null && gestor!=null && !"".equals(jndi) && !"".equals(gestor)){
                    String[] salida = new String[7];
                    salida[0] = gestor;
                    salida[1] = "";
                    salida[2] = "";
                    salida[3] = "";
                    salida[4] = "";
                    salida[5] = "";
                    salida[6] = jndi;
                    adapt = new AdaptadorSQLBD(salida);
                }//if(jndi!=null && gestor!=null && !"".equals(jndi) && !"".equals(gestor))
            }catch(TechnicalException te){
                log.error(te.getStackTrace());
                log.error("*** AdaptadorSQLBD: " + te.toString());
            }catch(SQLException e){
                log.error(e.getStackTrace());
            }finally{
                try{
                    if(st!=null) st.close();
                    if(rs!=null) rs.close();
                    if(conGenerico!=null && !conGenerico.isClosed())
                    conGenerico.close();
                }catch(SQLException e){
                    log.error(e.getStackTrace());
                }//try-catch
            }// finally
            if(log.isDebugEnabled()) log.debug("getConnection() : END");
         }// synchronized
        return adapt;
     }//getConnection
    
    
    public void getTrayectoriaGeneral(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        Map<Long, ColecTrayVO> mapaTrayectorias = new HashMap<Long, ColecTrayVO>();
        Map<Long, String> mapaDatEntiTrayectorias = new HashMap<Long, String>();
        List<ColecEntidadVO> listaEntidadesTrayectoria = new ArrayList<ColecEntidadVO>();
        String codigoOperacion = "0";
        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            ColecEntidadVO entidadPrincipal = new ColecEntidadVO();
            entidadPrincipal.setNumExp(numExpediente);
            entidadPrincipal =meLanbide48Manager.getEntidadInteresadaPorExpediente(entidadPrincipal, adapt);
            // Debe existir una entidad principal. Comprbamos que exista.
            // Leemos todas las trayectorias del Expediente, si es una entidad asociacion leemos la lista de entidades y asignamos su respectiva trayectoria.
            // Sino, asiganmos la trayectoria con el nombre la entidad.
            if (entidadPrincipal != null) {
                ColecTrayVO trayEjemplo = new ColecTrayVO();
                trayEjemplo.setNumExp(numExpediente);
                List<ColecTrayVO> trayectorias = meLanbide48Manager.getListaTrayectoriasPorExpediente(trayEjemplo, adapt);
                if(new Integer(1).compareTo(entidadPrincipal.getEsAgrupacion())==0){
                    List<ColecEntidadEnAgrupacionVO> listaAsociaciones=meLanbide48Manager.getListaEntidadesEnAgrupacion(entidadPrincipal,adapt);
                    // rellanamos los HashTables para escribir la request
                    for(ColecEntidadEnAgrupacionVO entidadAsociada : listaAsociaciones){
                        ColecEntidadVO temp = new ColecEntidadVO();
                        temp.setCodEntidad(entidadAsociada.getCodEntidad());
                        temp.setNumExp(entidadAsociada.getNumExp());
                        temp.setNombre(entidadAsociada.getNombre());
                        temp.setCif(entidadAsociada.getCif());
                        listaEntidadesTrayectoria.add(temp);
                    }
                }else{
                    listaEntidadesTrayectoria.add(entidadPrincipal);
                    // Si no es entidad no debe recuperarse mas de una trayectoria
                    if(trayectorias!= null && trayectorias.size()==1){
                        //mapaTrayectorias.put(entidadPrincipal.getCodEntidad(), trayectorias.get(0));
                        log.debug("La lista de trayectorias para entidad princiapl recuperada correctamente. ");
                    }else{
                        log.debug("La lista de trayectorias esta vacía. o La entidad es asociación y se han recuperado mas de una trayectoria");
                        if(trayectorias!=null && trayectorias.size()!=0){
                            codigoOperacion="5";
                            log.debug("La lista de trayectorias para un aentidad no asociación se ha recuperado con mas de una línea.");
                        }
                    }
                }
                for(ColecTrayVO traye : trayectorias){
                    mapaTrayectorias.put(traye.getCodEntidad(), traye);
                }
            } else {
                codigoOperacion = "1";
            }
        } catch (Exception ex) {
            log.error(ex.getStackTrace());
            codigoOperacion = "1";
        }

        escribirTrayectoriasColecRequest(codigoOperacion, listaEntidadesTrayectoria, mapaTrayectorias, response);
    }

    private void escribirTrayectoriasColecRequest(String codigoOperacion, List<ColecEntidadVO> listaEntidadesTrayectoria, Map<Long, ColecTrayVO> mapaTrayectorias, HttpServletResponse response) {
        StringBuilder xml = new StringBuilder();
        xml.append("<RESPUESTA>");
        xml.append("<CODIGO_OPERACION>");
        xml.append(codigoOperacion);
        xml.append("</CODIGO_OPERACION>");
        ColecEntidadVO entidadTrayectoria = null;
        ColecTrayVO tray = null;
        Long codEntiTrayectoria = null;
        String nombreEntiTrayectoria = null;
        Iterator<ColecEntidadVO> it = listaEntidadesTrayectoria.iterator();
        while (it.hasNext()) {
            entidadTrayectoria = it.next();
            codEntiTrayectoria = entidadTrayectoria.getCodEntidad();
            if (mapaTrayectorias.containsKey(codEntiTrayectoria)) {
                tray = mapaTrayectorias.get(codEntiTrayectoria);
            } else {
                tray = null;
            }
            nombreEntiTrayectoria = entidadTrayectoria.getCif()!= null ? entidadTrayectoria.getCif(): "";
            nombreEntiTrayectoria += !nombreEntiTrayectoria.equals("") && entidadTrayectoria.getNombre()!= null && !entidadTrayectoria.getNombre().equals("") ? " - " : "";
            nombreEntiTrayectoria += entidadTrayectoria.getNombre()!= null ? entidadTrayectoria.getNombre(): "";

            xml.append("<ASOCIACION>");
            xml.append("<COD_ASOCIACION>");
            xml.append(codEntiTrayectoria);
            xml.append("</COD_ASOCIACION>");
            xml.append("<NOMBRE_ASOCIACION>");
            xml.append(nombreEntiTrayectoria.toUpperCase());
            xml.append("</NOMBRE_ASOCIACION>");
            xml.append("<TRAYECTORIA>");
            xml.append("<COLEC_COD_TRAY>");
            xml.append(tray != null && tray.getCodTray()!= null ? tray.getCodTray() : "-");
            xml.append("</COLEC_COD_TRAY>");
            xml.append("<DEC_327_2007>");
            xml.append(tray != null && tray.getDec327_2007() != null ? tray.getDec327_2007() : "-");
            xml.append("</DEC_327_2007>");
            xml.append("<DEC_327_2008>");
            xml.append(tray != null && tray.getDec327_2008() != null ? tray.getDec327_2008() : "-");
            xml.append("</DEC_327_2008>");
            xml.append("<DEC_327_2009>");
            xml.append(tray != null && tray.getDec327_2009() != null ? tray.getDec327_2009() : "-");
            xml.append("</DEC_327_2009>");
            xml.append("<DEC_327_2010>");
            xml.append(tray != null && tray.getDec327_2010() != null ? tray.getDec327_2010() : "-");
            xml.append("</DEC_327_2010>");
            xml.append("<MIN_94_2007>");
            xml.append(tray != null && tray.getMin94_2007() != null ? tray.getMin94_2007() : "-");
            xml.append("</MIN_94_2007>");
            xml.append("<MIN_94_2008>");
            xml.append(tray != null && tray.getMin94_2008() != null ? tray.getMin94_2008() : "-");
            xml.append("</MIN_94_2008>");
            xml.append("<MIN_94_2009>");
            xml.append(tray != null && tray.getMin94_2009() != null ? tray.getMin94_2009() : "-");
            xml.append("</MIN_94_2009>");
            xml.append("<MIN_94_2010>");
            xml.append(tray != null && tray.getMin94_2010() != null ? tray.getMin94_2010() : "-");
            xml.append("</MIN_94_2010>");
            xml.append("<MIN_94_2011>");
            xml.append(tray != null && tray.getMin94_2011() != null ? tray.getMin94_2011() : "-");
            xml.append("</MIN_94_2011>");
            xml.append("<MIN_94_2012>");
            xml.append(tray != null && tray.getMin94_2012() != null ? tray.getMin94_2012() : "-");
            xml.append("</MIN_94_2012>");
            xml.append("<MIN_94_2013>");
            xml.append(tray != null && tray.getMin94_2013() != null ? tray.getMin94_2013() : "-");
            xml.append("</MIN_94_2013>");
            xml.append("<MIN_94_2014>");
            xml.append(tray != null && tray.getMin94_2014() != null ? tray.getMin94_2014() : "-");
            xml.append("</MIN_94_2014>");
            xml.append("<MIN_94_2015>");
            xml.append(tray != null && tray.getMin94_2015() != null ? tray.getMin94_2015() : "-");
            xml.append("</MIN_94_2015>");
            xml.append("<MIN_94_2016>");
            xml.append(tray != null && tray.getMin94_2016() != null ? tray.getMin94_2016() : "-");
            xml.append("</MIN_94_2016>");
            xml.append("<MIN_98_2007>");
            xml.append(tray != null && tray.getMin98_2007() != null ? tray.getMin98_2007() : "-");
            xml.append("</MIN_98_2007>");
            xml.append("<MIN_98_2008>");
            xml.append(tray != null && tray.getMin98_2008() != null ? tray.getMin98_2008() : "-");
            xml.append("</MIN_98_2008>");
            xml.append("<MIN_98_2009>");
            xml.append(tray != null && tray.getMin98_2009() != null ? tray.getMin98_2009() : "-");
            xml.append("</MIN_98_2009>");
            xml.append("<MIN_98_2010>");
            xml.append(tray != null && tray.getMin98_2010() != null ? tray.getMin98_2010() : "-");
            xml.append("</MIN_98_2010>");
            xml.append("<MIN_98_2011>");
            xml.append(tray != null && tray.getMin98_2011() != null ? tray.getMin98_2011() : "-");
            xml.append("</MIN_98_2011>");
            xml.append("<MIN_98_2012>");
            xml.append(tray != null && tray.getMin98_2012() != null ? tray.getMin98_2012() : "-");
            xml.append("</MIN_98_2012>");
            xml.append("<MIN_98_2013>");
            xml.append(tray != null && tray.getMin98_2013() != null ? tray.getMin98_2013() : "-");
            xml.append("</MIN_98_2013>");
            xml.append("<MIN_98_2014>");
            xml.append(tray != null && tray.getMin98_2014() != null ? tray.getMin98_2014() : "-");
            xml.append("</MIN_98_2014>");
            xml.append("<MIN_98_2015>");
            xml.append(tray != null && tray.getMin98_2015() != null ? tray.getMin98_2015() : "-");
            xml.append("</MIN_98_2015>");
            xml.append("<MIN_98_2016>");
            xml.append(tray != null && tray.getMin98_2016() != null ? tray.getMin98_2016() : "-");
            xml.append("</MIN_98_2016>");
            xml.append("<TAS_03_2007>");
            xml.append(tray != null && tray.getTas03_2007() != null ? tray.getTas03_2007() : "-");
            xml.append("</TAS_03_2007>");
            xml.append("<TAS_03_2008>");
            xml.append(tray != null && tray.getTas03_2008() != null ? tray.getTas03_2008() : "-");
            xml.append("</TAS_03_2008>");
            xml.append("<TAS_03_2009>");
            xml.append(tray != null && tray.getTas03_2009() != null ? tray.getTas03_2009() : "-");
            xml.append("</TAS_03_2009>");
            xml.append("<TAS_03_2010>");
            xml.append(tray != null && tray.getTas03_2010() != null ? tray.getTas03_2010() : "-");
            xml.append("</TAS_03_2010>");
            xml.append("<TAS_03_2011>");
            xml.append(tray != null && tray.getTas03_2011() != null ? tray.getTas03_2011() : "-");
            xml.append("</TAS_03_2011>");
            xml.append("<TAS_03_2012>");
            xml.append(tray != null && tray.getTas03_2012() != null ? tray.getTas03_2012() : "-");
            xml.append("</TAS_03_2012>");
            xml.append("<TAS_03_2013>");
            xml.append(tray != null && tray.getTas03_2013() != null ? tray.getTas03_2013() : "-");
            xml.append("</TAS_03_2013>");
            xml.append("<TAS_03_2014>");
            xml.append(tray != null && tray.getTas03_2014() != null ? tray.getTas03_2014() : "-");
            xml.append("</TAS_03_2014>");
            xml.append("<TAS_03_2015>");
            xml.append(tray != null && tray.getTas03_2015() != null ? tray.getTas03_2015() : "-");
            xml.append("</TAS_03_2015>");
            xml.append("<TAS_03_2016>");
            xml.append(tray != null && tray.getTas03_2016() != null ? tray.getTas03_2016() : "-");
            xml.append("</TAS_03_2016>");
            //xml.append("<ACT_56_03>");
            //xml.append(tray != null && tray.getAct56_03() != null ? tray.getAct56_03() : "-");
            //xml.append("</ACT_56_03>");
            xml.append("<LAN_2011>");
            xml.append(tray != null && tray.getLan_2011() != null ? tray.getLan_2011() : "-");
            xml.append("</LAN_2011>");
            xml.append("<LAN_2013>");
            xml.append(tray != null && tray.getLan_2013() != null ? tray.getLan_2013() : "-");
            xml.append("</LAN_2013>");
            xml.append("<LAN_2014>");
            xml.append(tray != null && tray.getLan_2014() != null ? tray.getLan_2014() : "-");
            xml.append("</LAN_2014>");
            xml.append("<LAN_2015>");
            xml.append(tray != null && tray.getLan_2015() != null ? tray.getLan_2015() : "-");
            xml.append("</LAN_2015>");
            xml.append("</TRAYECTORIA>");
            xml.append("</ASOCIACION>");
        }
        xml.append("</RESPUESTA>");

        xml.append("");

        try {
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xml.toString());
            out.flush();
            out.close();
        } catch (IOException e) {
            log.error(e.getStackTrace());
        }//try-catch
    }

    private String cargarSubpestanaUbicacionesCT(int codOrganizacion, String numExpediente, AdaptadorSQLBD adapt, HttpServletRequest request) throws Exception {
        try{
            int codIdioma = MeLanbide48Utils.getIdiomaUsuarioFromRequest(request);
            MeLanbideConvocatorias mlc = meLanbide48Manager.getDecretoAplicableExpediente(codOrganizacion, numExpediente, adapt);
            List<ColecUbicacionesCTVO> listaUbicacionesCT = meLanbide48Manager.getUbicacionesCTxNumExpediente(numExpediente,codIdioma,(mlc!=null?mlc.getId():null),adapt);
            String listaUbicacionesCTJSON = "";
            Gson gson = new Gson();
            GsonBuilder gsonB = new GsonBuilder().setDateFormat("dd/MM/yyyy");
            gsonB.serializeNulls();
            gson = gsonB.create();
            listaUbicacionesCTJSON=gson.toJson(listaUbicacionesCT);
            request.setAttribute("listaUbicacionesCTJSON", listaUbicacionesCTJSON);
            request.setAttribute("listaUbicacionesCT", listaUbicacionesCT);
        }catch(Exception ex){
            log.error("Error al cargarSubpestanaUbicacionesCT", ex);
        }
        return "/jsp/extension/melanbide48/ubicaciones/centro/trabajo/ubicacionesCenTra.jsp";
    }

    private void escribirResultadoGuardarTrayectoriaGralOtrosProgramasRequest(String codigoOperacion, List<ColecTrayOtroProgramaVO> filas, HttpServletResponse response) {
        StringBuilder xmlSalida = new StringBuilder();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        xmlSalida.append("<TRAYECTORIAS>");
        for (ColecTrayOtroProgramaVO tray : filas) {
            xmlSalida.append("<TRAYECTORIA>");
            xmlSalida.append("<COLEC_OTRPRO_COD>");
            xmlSalida.append(tray.getCodIdOtroPrograma());
            xmlSalida.append("</COLEC_OTRPRO_COD>");
            xmlSalida.append("<COLEC_OTRPRO_TIPO>");
            xmlSalida.append(tray.getTipoOtroPrograma()!= null ? tray.getTipoOtroPrograma() : "-");
            xmlSalida.append("</COLEC_OTRPRO_TIPO>");
            xmlSalida.append("<COLEC_OTRPRO_EXP_EJE>");
            xmlSalida.append(tray.getEjercicio());
            xmlSalida.append("</COLEC_OTRPRO_EXP_EJE>");
            xmlSalida.append("<COLEC_OTRPRO_NUMEXP>");
            xmlSalida.append(tray.getNumExpediente()!= null ? tray.getNumExpediente(): "-");
            xmlSalida.append("</COLEC_OTRPRO_NUMEXP>");
            xmlSalida.append("<COLEC_OTRPRO_COD_ENTIDAD>");
            xmlSalida.append(tray.getCodEntidad());
            xmlSalida.append("</COLEC_OTRPRO_COD_ENTIDAD>");
            xmlSalida.append("<COLEC_OTRPRO_PROGRAMA>");
            xmlSalida.append(tray.getPrograma()!= null ? tray.getPrograma() : "");
            xmlSalida.append("</COLEC_OTRPRO_PROGRAMA>");
            xmlSalida.append("<COLEC_OTRPRO_PROG_INICIO>");
            xmlSalida.append(tray.getFechaInicio()!= null ? MeLanbide48Utils.formatearFecha_ddmmyyyy(tray.getFechaInicio()) : "-");
            xmlSalida.append("</COLEC_OTRPRO_PROG_INICIO>");
            xmlSalida.append("<COLEC_OTRPRO_PROG_FIN>");
            xmlSalida.append(tray.getFechaFin()!= null ? MeLanbide48Utils.formatearFecha_ddmmyyyy(tray.getFechaFin()) : "-");
            xmlSalida.append("</COLEC_OTRPRO_PROG_FIN>");
            xmlSalida.append("<COLEC_ENT_CIF>");
            xmlSalida.append(tray.getCifEntidad()!=null?tray.getCifEntidad():"-");
            xmlSalida.append("</COLEC_ENT_CIF>");
            xmlSalida.append("<COLEC_ENT_NOMBRE>");
            xmlSalida.append(tray.getNombreEntidad()!= null ? tray.getNombreEntidad(): "-");
            xmlSalida.append("</COLEC_ENT_NOMBRE>");
            xmlSalida.append("</TRAYECTORIA>");
        }
        xmlSalida.append("</TRAYECTORIAS>");
        xmlSalida.append("</RESPUESTA>");
        try {
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        } catch (IOException e) {
            log.error(e.getStackTrace());
        }//try-catch
    }
    
    private void escribirResultadoGuardarTrayectoriaGralActividadesRequest(String codigoOperacion, List<ColecTrayActividadVO> filas, HttpServletResponse response) {
        StringBuilder xmlSalida = new StringBuilder();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        xmlSalida.append("<TRAYECTORIAS>");
        for (ColecTrayActividadVO tray : filas) {
            xmlSalida.append("<TRAYECTORIA>");
            xmlSalida.append("<COLEC_ACTIV_COD>");
            xmlSalida.append(tray.getCodIdActividad());
            xmlSalida.append("</COLEC_ACTIV_COD>");
            xmlSalida.append("<COLEC_ACTIV_TIPO>");
            xmlSalida.append(tray.getTipoActividad()!= null ? tray.getTipoActividad(): "-");
            xmlSalida.append("</COLEC_ACTIV_TIPO>");
            xmlSalida.append("<COLEC_ACTIV_EXP_EJE>");
            xmlSalida.append(tray.getEjercicio());
            xmlSalida.append("</COLEC_ACTIV_EXP_EJE>");
            xmlSalida.append("<COLEC_ACTIV_NUMEXP>");
            xmlSalida.append(tray.getNumExpediente()!= null ? tray.getNumExpediente(): "-");
            xmlSalida.append("</COLEC_ACTIV_NUMEXP>");
            xmlSalida.append("<COLEC_ACTIV_COD_ENTIDAD>");
            xmlSalida.append(tray.getCodEntidad());
            xmlSalida.append("</COLEC_ACTIV_COD_ENTIDAD>");
            xmlSalida.append("<COLEC_ACTIV_DESC_SERVPUB>");
            xmlSalida.append(tray.getDesActividadyServPublEmp()!= null ? tray.getDesActividadyServPublEmp(): "");
            xmlSalida.append("</COLEC_ACTIV_DESC_SERVPUB>");
            xmlSalida.append("<COLEC_ACTIV_INICIO>");
            xmlSalida.append(tray.getFechaInicio()!= null ? MeLanbide48Utils.formatearFecha_ddmmyyyy(tray.getFechaInicio()) : "-");
            xmlSalida.append("</COLEC_ACTIV_INICIO>");
            xmlSalida.append("<COLEC_ACTIV_FIN>");
            xmlSalida.append(tray.getFechaFin()!= null ? MeLanbide48Utils.formatearFecha_ddmmyyyy(tray.getFechaFin()) : "-");
            xmlSalida.append("</COLEC_ACTIV_FIN>");
            xmlSalida.append("<COLEC_ENT_CIF>");
            xmlSalida.append(tray.getCifEntidad()!=null?tray.getCifEntidad():"-");
            xmlSalida.append("</COLEC_ENT_CIF>");
            xmlSalida.append("<COLEC_ENT_NOMBRE>");
            xmlSalida.append(tray.getNombreEntidad()!= null ? tray.getNombreEntidad(): "-");
            xmlSalida.append("</COLEC_ENT_NOMBRE>");
            xmlSalida.append("</TRAYECTORIA>");
        }
        xmlSalida.append("</TRAYECTORIAS>");
        xmlSalida.append("</RESPUESTA>");
        try {
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        } catch (IOException e) {
            log.error(e.getStackTrace());
        }//try-catch
    }
    
    /**
     * Invocacion a guardar  datos desde js llamada ajax, se recibe y lee json y devuelve json (lista de de colectivos solicitados)
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @param request
     * @param response 
     */
    public void guardarSolicitudAjaxResptaJSON(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("guardarSolicitudAjaxResptaJSON - Begin()" + formatFechaLog.format(new Date()));
        AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        String jsonDatos = "";
        List<ColecSolicitudVO> respuestaServicio= new ArrayList<ColecSolicitudVO>();
        try {
            //Idioma
            int codIdioma = MeLanbide48Utils.getIdiomaUsuarioFromRequest(request);
            //Recojo los parametros
            jsonDatos = (String) request.getParameter("colecsolicitud");
            String tipoOperacion = (String) request.getParameter("tipoOperacion");
            String idBDConvocatoriaExpediente = (String) request.getParameter("idBDConvocatoriaExpediente");
            idBDConvocatoriaExpediente = idBDConvocatoriaExpediente!=null && !idBDConvocatoriaExpediente.isEmpty() && !idBDConvocatoriaExpediente.equalsIgnoreCase("null") ? idBDConvocatoriaExpediente : null;
            log.info("tipoOperacion : " + tipoOperacion);
            log.info("jsonDatoss : " + jsonDatos);
            if (jsonDatos != null && !jsonDatos.isEmpty()) {
                Gson gson = new Gson();
                GsonBuilder gsonB = new GsonBuilder().setDateFormat("dd/MM/yyyy");
                gsonB.serializeNulls();
                gson = gsonB.create();
                ColecSolicitudVO datos = (ColecSolicitudVO) gson.fromJson(jsonDatos, ColecSolicitudVO.class);
                if (datos != null) {
                   datos = meLanbide48Manager.guardarColecSolicitudVO(codOrganizacion, datos, adapt);
                   respuestaServicio=meLanbide48Manager.getSolicitudPorExpediente(datos,codIdioma,Integer.valueOf(idBDConvocatoriaExpediente),adapt);
                }
            }
        } catch (Exception e) {
            log.error("Se ha presentado un erro al registrar Datos Coletivo y TH Solicitado", e);
            respuestaServicio=null;
        }
        parsearRespuestasEnviarJSON(request,response,respuestaServicio);       
        log.info("guardarSolicitudAjaxResptaJSON - End()" + formatFechaLog.format(new Date()));
    }
    
    public void eliminarLineaColecSolicitud(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("eliminarLineaColecSolicitud - Begin()" + formatFechaLog.format(new Date()));
        AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        List<ColecSolicitudVO> respuestaServicio = new ArrayList<ColecSolicitudVO>();
        try {
            //Recojo los parametros
            int codIdioma = MeLanbide48Utils.getIdiomaUsuarioFromRequest(request);
            String identificadorBDEliminar = (String) request.getParameter("identificadorBDEliminar");
            String idBDConvocatoriaExpediente = (String) request.getParameter("idBDConvocatoriaExpediente");
            idBDConvocatoriaExpediente = idBDConvocatoriaExpediente != null && !idBDConvocatoriaExpediente.isEmpty() && !idBDConvocatoriaExpediente.equalsIgnoreCase("null") ? idBDConvocatoriaExpediente : null;
            if (identificadorBDEliminar != null && !identificadorBDEliminar.isEmpty()) {
                log.info("Eliminamos : " + identificadorBDEliminar + " " +
                        meLanbide48Manager.eliminarLineaColecSolicitud(identificadorBDEliminar, adapt)
                );
                respuestaServicio=meLanbide48Manager.getSolicitudPorExpediente(new ColecSolicitudVO(numExpediente),codIdioma,Integer.valueOf(idBDConvocatoriaExpediente), adapt);
            }
        } catch (Exception e) {
            log.error("Se ha presentado un erro al registrar Datos Coletivo y TH Solicitado", e);
            respuestaServicio = null;
        }
        parsearRespuestasEnviarJSON(request, response, respuestaServicio);
        log.info("eliminarLineaColecSolicitud - End()" + formatFechaLog.format(new Date()));
    }
    
    public String cargarEditarAddNuevaColecTHSolicitado(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.debug("cargarEditarAddNuevaColecTHSolicitado - Begin()");
        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            Integer codSolicitud = (request.getParameter("codSolicitud")!= null && !request.getParameter("codSolicitud").isEmpty()? Integer.valueOf(request.getParameter("codSolicitud")):null);
            ColecSolicitudVO colecSolicitudVO = null;
            String colecSolicitudVOJSON = null;
            List<SelectItem> listaColectivo = null;
            List<SelectItem> listaTerritorioHistorico = null;
            try {
                Gson gson = new Gson();
                GsonBuilder gsonB = new GsonBuilder().setDateFormat("dd/MM/yyyy");
                gsonB.serializeNulls();
                gson = gsonB.create();
                int codIdioma = MeLanbide48Utils.getIdiomaUsuarioFromRequest(request);
                listaColectivo=meLanbide48Manager.getListaColectivos(codIdioma, adapt);
                listaTerritorioHistorico=meLanbide48Manager.getListaProvincias(adapt);                
                if(codSolicitud!=null){
                    colecSolicitudVO=meLanbide48Manager.getSolicitudPorId(codSolicitud, adapt);
                    colecSolicitudVOJSON = gson.toJson(colecSolicitudVO);
                }
            } catch (Exception ex) {
                log.debug("Error al obtener datos de ColecSolicitud modificar ", ex);
            }
            MeLanbideConvocatorias convocatoriaActiva = null;
            // Recogemos los datos de El decreto/Convocatoria Aplicable
            try {
                convocatoriaActiva = meLanbide48Manager.getDecretoAplicableExpediente(codOrganizacion, numExpediente, adapt);
                request.setAttribute("convocatoriaActiva", convocatoriaActiva);
            } catch (Exception ex) {
                log.error("Erro al tratar de leer los datos del decretoaplicable al expediente " + numExpediente, ex);
            }
            request.setAttribute("colecSolicitudVOJSON", colecSolicitudVOJSON);
            request.setAttribute("listaColectivo", listaColectivo);
            request.setAttribute("listaTerritorioHistorico", listaTerritorioHistorico);
        } catch (Exception ex) {
            log.error("Error al cargarEditarAddNuevaColecTHSolicitado : ", ex);
        }
        return "/jsp/extension/melanbide48/solicitudes/general/altaEdicionColecTHSolicitado.jsp";
    }
    
    public void cargarAmbitosxColectivoConvocatoriaTHJSON(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        Integer codProvincia = request.getParameter("codigoTH")!=null && !request.getParameter("codigoTH").isEmpty() && !request.getParameter("codigoTH").equalsIgnoreCase("null") ? Integer.valueOf(request.getParameter("codigoTH")):null;
        Integer idColectivo = request.getParameter("idColectivo")!=null && !request.getParameter("idColectivo").isEmpty() && !request.getParameter("idColectivo").equalsIgnoreCase("null") ? Integer.valueOf(request.getParameter("idColectivo")):null;
        Integer idBDConvocatoriaExpediente = request.getParameter("idBDConvocatoriaExpediente")!=null && !request.getParameter("idBDConvocatoriaExpediente").isEmpty() && !request.getParameter("idBDConvocatoriaExpediente").equalsIgnoreCase("null") ? Integer.valueOf(request.getParameter("idBDConvocatoriaExpediente")):null;
        List<SelectItem> listaAmbitos = new ArrayList<SelectItem>();
        try {
            int codIdioma = MeLanbide48Utils.getIdiomaUsuarioFromRequest(request);
            listaAmbitos = meLanbide48Manager.getAmbitoSolicitadoxColectivoConvocatoriaTH(codIdioma,idBDConvocatoriaExpediente,idColectivo,codProvincia, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        } catch (Exception ex) {
            log.error("Error recuperando la lista de ambitos solicitada por TH"+ ex.getMessage(), ex);
        }
        parsearRespuestasEnviarJSON(request,response,listaAmbitos);
    }
    
    public void cargarAmbitosxColectivoConvocatoriaJSON(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String idColectivo = request.getParameter("idColectivo");
        String idBDConvocatoriaExpediente = request.getParameter("idBDConvocatoriaExpediente");
        List<SelectItem> listaAmbitos = new ArrayList<SelectItem>();
        try {
            int codIdioma = MeLanbide48Utils.getIdiomaUsuarioFromRequest(request);
            listaAmbitos = meLanbide48Manager.getAmbitoSolicitadoxColectivoConvocatoria(codIdioma,Integer.valueOf(idBDConvocatoriaExpediente),Integer.valueOf(idColectivo), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        } catch (Exception ex) {
            log.error("Error recuperando la lista de ambitos/sumabito  solicitada por Colectivo"+ ex.getMessage(), ex);
        }
        parsearRespuestasEnviarJSON(request,response,listaAmbitos);
    }
        
    private static void parsearRespuestasEnviarJSON(HttpServletRequest request,HttpServletResponse response,Object respuesta ){
        GsonBuilder gsonBuilder = new GsonBuilder().setDateFormat("dd/MM/yyyy");
        gsonBuilder.serializeNulls();
        Gson gson = gsonBuilder.create();
        String respuestaJsonString = gson.toJson(respuesta);
        log.info("respuestaJsonString : " + respuestaJsonString);
        try {
            PrintWriter out = response.getWriter();
            // Codificamos con UTF-8 Encodig de la request para los caracteres especiales o tildes
            if (respuestaJsonString != null && !respuestaJsonString.isEmpty()) {
               respuestaJsonString = MeLanbide48Utils.decodeText(respuestaJsonString, request.getCharacterEncoding());
            }
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            out.print(respuestaJsonString);
            out.flush();
            out.close();
        } catch (Exception e) {
            log.error("Error preparando respuesta " + e.getMessage(), e);
            e.printStackTrace();
        }
    }
    public void guardarUbicacionCTJSON(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        
        log.info("guardarUbicacionCTJSON - Begin()" + formatFechaLog.format(new Date()));
        AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        String jsonDatos = "";
        List<ColecUbicacionesCTVO> respuestaServicio = new ArrayList<ColecUbicacionesCTVO>();
        try {
            //Idioma
            int codIdioma = MeLanbide48Utils.getIdiomaUsuarioFromRequest(request);
            //Recojo los parametros
            jsonDatos = (String) request.getParameter("colecubicacionesct");
            String tipoOperacion = (String) request.getParameter("tipoOperacion");
            String codigoConvocatoriaExpediente = (String) request.getParameter("codigoConvocatoriaExpediente");
            log.info("tipoOperacion : " + tipoOperacion);
            log.info("jsonDatoss : " + jsonDatos);
            if (jsonDatos != null && !jsonDatos.isEmpty()) {
                Gson gson = new Gson();
                GsonBuilder gsonB = new GsonBuilder().setDateFormat("dd/MM/yyyy");
                gsonB.serializeNulls();
                gson = gsonB.create();
                ColecUbicacionesCTVO datos = (ColecUbicacionesCTVO) gson.fromJson(jsonDatos, ColecUbicacionesCTVO.class);
                if (datos != null) {
                    // CONV_ANTE-2021 Viene la comarca, Despues de 2021, hay que leer la comarca a la que pertenece el municipio.
                    if(datos.getComarca()==null && datos.getFkIdAmbitoSolicitado()!=null){
                        ColecMunVO datosMun = meLanbide48Manager.getMunicipioPorTHCodMun(datos.getTerritorioHist(),datos.getMunicipio(),adapt);
                        datos.setComarca(datosMun!=null && datosMun.getCodComarca()!=null?datosMun.getCodComarca().intValue():null);
                    }
                    log.info("Datos guardados: "+
                            meLanbide48Manager.guardarUbicacionesCT(codIdioma,datos, adapt)
                            );
                    Integer idBDConvocatoriaExp=meLanbide48Manager.getIdByDecretoCodigo(codigoConvocatoriaExpediente,MeLanbide48Utils.getCodProcedimientoDeExpediente(numExpediente), adapt);
                    respuestaServicio = meLanbide48Manager.getUbicacionesCTxNumExpediente(numExpediente,codIdioma,idBDConvocatoriaExp,adapt);
                }
            }
        } catch (Exception e) {
            log.error("Se ha presentado un erro al registrar Datos Ubicaciones de Centros de Trabajo", e);
            respuestaServicio = null;
        }
        parsearRespuestasEnviarJSON(request, response, respuestaServicio);
        log.info("guardarUbicacionCTJSON - End()" + formatFechaLog.format(new Date()));
    }
    
    public void getListaConvocatoriasPredefinidaXGrupoXColectivo(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("getListaConvocatoriasPredefinidaXGrupoXColectivo - Begin()" + formatFechaLog.format(new Date()));
        AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        List<ColecProgConvActPredefinidaColectivo> respuestaServicio = new ArrayList<ColecProgConvActPredefinidaColectivo>();
        try {
            //Recojo los parametros
            int codIdioma = MeLanbide48Utils.getIdiomaUsuarioFromRequest(request);
            String codigoGrupo = (String) request.getParameter("codigoGrupo");
            String colectivo = (String) request.getParameter("colectivo");
            respuestaServicio=meLanbide48Manager.getListaConvocatoriasPredefinidaXGrupoXColectivo(codIdioma,(codigoGrupo!=null?Integer.valueOf(codigoGrupo):null),(colectivo!=null?Integer.valueOf(colectivo):null),adapt);
        } catch (Exception e) {
            log.error("Excepcion General - getListaConvocatoriasPredefinidaXGrupoXColectivo - ", e);
            respuestaServicio = null;
        }
        parsearRespuestasEnviarJSON(request, response, respuestaServicio);
        log.info("getListaConvocatoriasPredefinidaXGrupoXColectivo - End()" + formatFechaLog.format(new Date()));
    }
    
    public void obtenerDatosTrayectoriaColectivoXEntidad(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("obtenerDatosTrayectoriaColectivoXEntidad - Begin()" + formatFechaLog.format(new Date()));
        AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        List<ColecTrayectoriaEntidad> respuestaServicio = new ArrayList<ColecTrayectoriaEntidad>();
        try {
            String codigoGrupo = (String) request.getParameter("codigoGrupo");
            String colectivo = (String) request.getParameter("colectivo");
            String identificadorBDGestionar = (String) request.getParameter("identificadorBDGestionar");
            respuestaServicio=meLanbide48Manager.getListaTrayectoriaAcreditableGrupoColectivoEntidad(numExpediente,Integer.valueOf(codigoGrupo),Integer.valueOf(colectivo),Integer.valueOf(identificadorBDGestionar), adapt);
        } catch (Exception e) {
            log.error("Excepcion General - obtenerDatosTrayectoriaColectivoXEntidad - ", e);
            respuestaServicio = null;
        }
        parsearRespuestasEnviarJSON(request, response, respuestaServicio);
        log.info("obtenerDatosTrayectoriaColectivoXEntidad - End()" + formatFechaLog.format(new Date()));
    }
    
    public void guardarDatosColecConvocatoriasPredefColectivoEntidad(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {

        log.info("guardarDatosColecConvocatoriasPredefColectivoEntidad - Begin()" + formatFechaLog.format(new Date()));
        AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        String jsonDatos = "";
        String respuestaServicio = "";
        try {
            //Idioma
            int codIdioma = MeLanbide48Utils.getIdiomaUsuarioFromRequest(request);
            String codEntidad = (String) request.getParameter("identificadorBDGestionar");
            String codColectivo = (String) request.getParameter("colectivo");
            //Recojo los parametros
            jsonDatos = (String) request.getParameter("colecTrayectoriaEntidadLista");
            log.info("jsonDatoss : " + jsonDatos);
            if (jsonDatos != null && !jsonDatos.isEmpty()) {
                Gson gson = new Gson();
                GsonBuilder gsonB = new GsonBuilder().setDateFormat("dd/MM/yyyy");
                gsonB.serializeNulls();
                gson = gsonB.create();
                List<ColecTrayectoriaEntidad> datos = Arrays.asList((ColecTrayectoriaEntidad[]) gson.fromJson(jsonDatos, ColecTrayectoriaEntidad[].class));
                respuestaServicio=meLanbide48Manager.guardarDatosColecConvocatoriasPredefColectivoEntidad(codIdioma, datos, Integer.valueOf(codEntidad), Integer.valueOf(codColectivo), numExpediente, adapt);
            }
        } catch (Exception e) {
            log.error("Se ha presentado un erro al registrar Datos Ubicaciones de Centros de Trabajo", e);
            respuestaServicio = "ERROR";
        }
        parsearRespuestasEnviarJSON(request, response, respuestaServicio);
        log.info("guardarDatosColecConvocatoriasPredefColectivoEntidad - End()" + formatFechaLog.format(new Date()));
    }
    
    /**
     * Carga la pantalla para alta o ediciion de un apartado de la experiencia acreditable de la entidad
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @param request
     * @param response
     * @return 
     */
    public String cargarAltaEdicionColecTrayectoriaEntidad(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            String identificadorBDGestionar = request.getParameter("identificadorBDGestionar");
            request.setAttribute("idBDEntidad", request.getParameter("idBDEntidad"));
            request.setAttribute("cifEntidad", request.getParameter("cifEntidad"));
            request.setAttribute("nombreEntidad", request.getParameter("nombreEntidad"));
            request.setAttribute("idColectivo", request.getParameter("idColectivo"));
            request.setAttribute("idGrupo", request.getParameter("idGrupo"));
            MeLanbideConvocatorias convocatoriaActiva = null;
            // Recogemos los datos de El decreto/Convocatoria Aplicable
            try {
                convocatoriaActiva = meLanbide48Manager.getDecretoAplicableExpediente(codOrganizacion, numExpediente, adapt);
                request.setAttribute("convocatoriaActiva", convocatoriaActiva);
            } catch (Exception ex) {
                log.error("Erro al tratar de leer los datos del decretoaplicable al expediente " + numExpediente, ex);
            }

            ColecTrayectoriaEntidad datosModif = null;
            if (identificadorBDGestionar != null && !identificadorBDGestionar.equals("") && !identificadorBDGestionar.equalsIgnoreCase("null")) {
                datosModif = meLanbide48Manager.getColecTrayectoriaEntidadXid(Integer.valueOf(identificadorBDGestionar), adapt);
                if (datosModif != null) {
                    request.setAttribute("datosModif", datosModif);
                }
            }
        } catch (Exception ex) {
            log.error("Error en la carga de la pantalla para la edicion/alta de ColecTrayectoria Entidad.", ex);
        }
        return "/jsp/extension/melanbide48/experiencia/acreditable/altaModificacionExperienciaAcreditable.jsp";
    }
    
    public void guardarDatosColecTrayectoriaEntidad(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {

        log.info("guardarDatosColecTrayectoriaEntidad - Begin()" + formatFechaLog.format(new Date()));
        AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        String jsonDatos = "";
        String respuestaServicio = "";
        try {
            //Idioma
            int codIdioma = MeLanbide48Utils.getIdiomaUsuarioFromRequest(request);
            //Recojo los parametros
            jsonDatos = (String) request.getParameter("colecTrayectoriaEntidad");
            log.info("jsonDatoss : " + jsonDatos);
            if (jsonDatos != null && !jsonDatos.isEmpty()) {
                Gson gson = new Gson();
                GsonBuilder gsonB = new GsonBuilder().setDateFormat("dd/MM/yyyy");
                gsonB.serializeNulls();
                gson = gsonB.create();
                ColecTrayectoriaEntidad datos =  gson.fromJson(jsonDatos, ColecTrayectoriaEntidad.class);
                if (datos != null) {
                    meLanbide48Manager.guardarDatosColecTrayectoriaEntidad(codIdioma, datos, adapt);
                    respuestaServicio = "OK";
                }
            }
        } catch (Exception e) {
            log.error("Se ha presentado un erro al guardar los Datos Trayectoria " + numExpediente, e);
            respuestaServicio = "ERROR";
        }
        parsearRespuestasEnviarJSON(request, response, respuestaServicio);
        log.info("guardarDatosColecTrayectoriaEntidad - End()" + formatFechaLog.format(new Date()));
    }
    
    public void eliminarDatosColecTrayectoriaEntidad(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("eliminarDatosColecTrayectoriaEntidad - Begin()" + formatFechaLog.format(new Date()));
        AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        String respuestaServicio = "";
        try {
            //Idioma
            int codIdioma = MeLanbide48Utils.getIdiomaUsuarioFromRequest(request);
            //Recojo los parametros
            String idBDEntidad = request.getParameter("idBDEntidad");
            String colectivo = request.getParameter("colectivo");
            String codigoGrupo = request.getParameter("codigoGrupo");
            String identificadorBDGestionar = request.getParameter("identificadorBDGestionar");

            log.info("datosEliminar : " 
                    + numExpediente + " <==numExpediente " 
                    + idBDEntidad + " <==idBDEntidad " 
                    + colectivo + " <==colectivo " 
                    + codigoGrupo + " <==codigoGrupo " 
                    + identificadorBDGestionar + " <==identificadorBDGestionar" 
                    );
            if(meLanbide48Manager.eliminarDatosColecTrayectoriaEntidad(codIdioma,new ColecTrayectoriaEntidad(Integer.valueOf(identificadorBDGestionar)), adapt))
                respuestaServicio = "OK";
        } catch (Exception e) {
            log.error("Se ha presentado un erro al eliminar los Datos Trayectoria " + numExpediente, e);
            respuestaServicio = "ERROR";
        }
        parsearRespuestasEnviarJSON(request, response, respuestaServicio);
        log.info("eliminarDatosColecTrayectoriaEntidad - End()" + formatFechaLog.format(new Date()));
    }
    
    /**
     * Recupera la ista de ubicaciones para un expediente y devuelve en la request un objeto JSON con la lista
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @param request
     * @param response 
     */
    public void getDatosListaUbicacionesExpediente(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("eliminarDatosColecTrayectoriaEntidad - Begin()" + formatFechaLog.format(new Date()));
        List<ColecUbicacionesCTVO> respuestaServicio = new ArrayList<ColecUbicacionesCTVO>();
        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            MeLanbideConvocatorias mlc = meLanbide48Manager.getDecretoAplicableExpediente(codOrganizacion, numExpediente, adapt);
            respuestaServicio = meLanbide48Manager.getUbicacionesCTxNumExpediente(numExpediente, MeLanbide48Utils.getIdiomaUsuarioFromRequest(request),(mlc!=null?mlc.getId():null),adapt);
        } catch (Exception ex) {
            log.error("Exception - getDatosListaUbicacionesExpediente -  ", ex);
        }
        parsearRespuestasEnviarJSON(request, response, respuestaServicio);
    }
    
    public void getListaEntidadesAsociacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("getListaEntidadesAsociacion - Begin()" + formatFechaLog.format(new Date()));
        List<ColecEntidadEnAgrupacionVO> respuestaServicio = new ArrayList<ColecEntidadEnAgrupacionVO>();
        try {
            String identificadorBDGestionar = request.getParameter("identificadorBDGestionar");
            if(identificadorBDGestionar!=null && !identificadorBDGestionar.isEmpty() && !identificadorBDGestionar.equalsIgnoreCase("undefined") && !identificadorBDGestionar.equalsIgnoreCase("null")){
                respuestaServicio = meLanbide48Manager.getListaEntidadesEnAgrupacion(new ColecEntidadVO(Long.valueOf(identificadorBDGestionar)), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            }else
                log.info("Identificador de la Entidad recibido a Null, nose puede recuperar la lista de asociaciones...");            
        } catch (Exception ex) {
            log.error("Exception - getListaEntidadesAsociacion -  ", ex);
        }
        parsearRespuestasEnviarJSON(request, response, respuestaServicio);
    }

    public void guardarValidarTotalMesesResumen(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("guardarValidarTotalMesesResumen - Begin()" + formatFechaLog.format(new Date()));
        AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        String respuestaServicio = "";
        try {
            //Recojo los parametros
            String jsonDatos = request.getParameter("colecTrayeEntiValidaLista");
            log.info("jsonDatoss : " + jsonDatos);
            if (jsonDatos != null && !jsonDatos.isEmpty()) {
                Gson gson = new Gson();
                GsonBuilder gsonB = new GsonBuilder().setDateFormat("dd/MM/yyyy");
                gsonB.serializeNulls();
                gson = gsonB.create();
                List<ColecTrayeEntiValida> datos = Arrays.asList((ColecTrayeEntiValida[]) gson.fromJson(jsonDatos, ColecTrayeEntiValida[].class));
                if (meLanbide48Manager.guardarValidarTotalMesesResumen(numExpediente, datos, adapt)) {
                    respuestaServicio = "OK";
                }
            }
        } catch (Exception e) {
            log.error("Se ha presentado un erro al giardando total meses validados Trayectoria " + numExpediente, e);
            respuestaServicio = "ERROR";
        }
        parsearRespuestasEnviarJSON(request, response, respuestaServicio);
        log.info("guardarValidarTotalMesesResumen - End()" + formatFechaLog.format(new Date()));
    }
    
    public void getTodaTrayectoriaAcreditableExpedienteNoSolapable(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("getTodaTrayectoriaAcreditableExpedienteNoSolapable - Begin()" + formatFechaLog.format(new Date()));
        List<ColecTrayectoriaEntidad> respuestaServicio = new ArrayList<ColecTrayectoriaEntidad>();
        try {
            int codIdioma = MeLanbide48Utils.getIdiomaUsuarioFromRequest(request);
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            if (numExpediente != null && !numExpediente.isEmpty() && !numExpediente.equalsIgnoreCase("undefined") && !numExpediente.equalsIgnoreCase("null")) {
                respuestaServicio = meLanbide48Manager.getTodaTrayectoriaAcreditableExpedienteNoSolapable(codIdioma,numExpediente,adapt);
            } else {
                log.info("Identificador expediente recibido a Null, no se puede recuperar la lista de toda trayectoria no salapada...");
            }
        } catch (Exception ex) {
            log.error("Exception - getTodaTrayectoriaAcreditableExpedienteNoSolapable -  ", ex);
        }
        parsearRespuestasEnviarJSON(request, response, respuestaServicio);
    }
    
    public void getNumeroTotalMesesValidadosExpediente(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("getNumeroTotalMesesValidadosExpediente - Begin()" + formatFechaLog.format(new Date()));
        List<ColecTrayeEntiValida> respuestaServicio = new ArrayList<ColecTrayeEntiValida>();
        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            String idBDEntidad = request.getParameter("idBDEntidad");
            if (numExpediente != null && !numExpediente.isEmpty() && !numExpediente.equalsIgnoreCase("undefined") && !numExpediente.equalsIgnoreCase("null")) {
                respuestaServicio = meLanbide48Manager.getColecTrayeEntiValidaByNumExpedienteCodEntidad(numExpediente,(idBDEntidad!=null && !idBDEntidad.isEmpty() ? Integer.valueOf(idBDEntidad):0),adapt);
            } else {
                log.info("Identificador expediente recibido a Null, no se puede recuperar numero total de meses validados...");
            }
        } catch (Exception ex) {
            log.error("Exception - getNumeroTotalMesesValidadosExpediente -  ", ex);
        }
        parsearRespuestasEnviarJSON(request, response, respuestaServicio);
    }
    
    /**
     * Carga la pantalla para valorar las ubicaciones del centro de trabajo de la entidad

     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @param request
     * @param response
     * @return url de jsp para cargar en el popup
     */
    public String cargarValorarUbicacionesCentroTrabajo(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        try {
            int codIdioma = MeLanbide48Utils.getIdiomaUsuarioFromRequest(request);
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            String codigoUbicacion = request.getParameter("idTray");
            // Entidad Padre para leer los datos generale de validacion
            String codEntidad = request.getParameter("codEntidad");
            MeLanbideConvocatorias convocatoriaActiva = null;
            // Recogemos los datos de El decreto/Convocatoria Aplicable
            try {
                convocatoriaActiva = meLanbide48Manager.getDecretoAplicableExpediente(codOrganizacion, numExpediente, adapt);
                request.setAttribute("convocatoriaActiva", convocatoriaActiva);
            } catch (Exception ex) {
                log.error("Erro al tratar de leer los datos del decreto aplicable al expediente " + numExpediente, ex);
            }
            try {
                List<SelectItem> listaCertificadosCalidad = meLanbide48ManagerColecCertCalidad.getListaColecCertCalidad(codIdioma,adapt);
                request.setAttribute("listaCertificadosCalidad",listaCertificadosCalidad);
            }catch (Exception ex){
                log.error("Error al cargar datos de lista Certificados de calidad",ex);
            }

            ColecUbicacionesCTVO ubicacionModif = null;
            ColecEntidadVO entidadUbicacion = null;
            ColecTrayeEntiValida entidadTrayectoriaValidada = null;
            if (codigoUbicacion != null && !codigoUbicacion.equals("")) {
                ubicacionModif = meLanbide48Manager.getUbicacionCTPorCodigoYExpediente(codOrganizacion, codIdioma, new ColecUbicacionesCTVO(Long.parseLong(codigoUbicacion),numExpediente), adapt);
                if (ubicacionModif != null){ 
                    request.setAttribute("ubicacionModif", ubicacionModif);
                    // Leemos la valoracion de la ubicacion si existe
                    ColecUbicCTValoracion colecUbicCTValoracion  = meLanbide48Manager.getColecUbicCTValoracionByidFkUbicacion(ubicacionModif.getCodId().intValue(), adapt);
                    if(colecUbicCTValoracion!=null)
                        request.setAttribute("colecUbicCTValoracion", colecUbicCTValoracion);
                }
                // Segun entidad padre para leer datos de validacio total
                entidadUbicacion = meLanbide48Manager.getEntidadPorCodigoYExpediente(new ColecEntidadVO(Long.valueOf(codEntidad),ubicacionModif.getNumExpediente()), codIdioma,adapt);
                if(entidadUbicacion!=null){ 
                    request.setAttribute("entidadUbicacion", entidadUbicacion);
                }
                entidadTrayectoriaValidada = meLanbide48Manager.getColecTrayeEntiValidaByEntidadColectivo(Integer.valueOf(codEntidad),Integer.valueOf(ubicacionModif.getCodTipoColectivo()), adapt);
                if (entidadTrayectoriaValidada != null) {
                    request.setAttribute("entidadTrayectoriaValidada", entidadTrayectoriaValidada);
                    request.setAttribute("trayNumeroMesesValidados", String.format(Locale.GERMAN, "%,.2f", entidadTrayectoriaValidada.getNumeroMesesValidados()));
                }
                Double numeroMesesTotalTrayectoria = meLanbide48Manager.getNumeroTotalMesesSinSolaparFechasTrayExpedienteColectivo(codIdioma, numExpediente,Integer.valueOf(ubicacionModif.getCodTipoColectivo()), adapt);
                if(numeroMesesTotalTrayectoria!=null)
                    request.setAttribute("numTotalMesesTraySinSolap", String.format(Locale.GERMAN, "%,.2f", numeroMesesTotalTrayectoria)); 
                Double puntuacionUbicacionMun = meLanbide48Manager.getMunicipioPuntuacionPorConvocatoriaColectivoTHCodMun((convocatoriaActiva!=null?convocatoriaActiva.getId():null), Integer.valueOf(ubicacionModif.getCodTipoColectivo()), ubicacionModif.getTerritorioHist(), ubicacionModif.getMunicipio(), adapt);
                if(puntuacionUbicacionMun!=null)
                    request.setAttribute("puntuacionUbicacionMun", String.format(Locale.GERMAN, "%,.2f", puntuacionUbicacionMun)); 
            }
        } catch (Exception ex) {
         
            log.error("Error preparando datos para valoracion ubicacion CT", ex);
        }
        return "/jsp/extension/melanbide48/ubicaciones/centro/trabajo/valorarUbicacionCT.jsp";
    }
    
    public void guardarDatosValoracionUbicacionCentTra(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("guardarDatosValoracionUbicacionCentTra - Begin()" + formatFechaLog.format(new Date()));
        AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        String jsonDatos = "";
        String respuestaServicio = "";
        try {
            //Idioma
            int codIdioma = MeLanbide48Utils.getIdiomaUsuarioFromRequest(request);
            //Recojo los parametros
            jsonDatos = (String) request.getParameter("colecubicctvaloracion");
            String tipoOperacion = (String) request.getParameter("tipoOperacion");
            log.info("tipoOperacion : " + tipoOperacion);
            log.info("jsonDatoss : " + jsonDatos);
            if (jsonDatos != null && !jsonDatos.isEmpty()) {
                Gson gson = new Gson();
                GsonBuilder gsonB = new GsonBuilder().setDateFormat("dd/MM/yyyy");
                gsonB.serializeNulls();
                gson = gsonB.create();
                ColecUbicCTValoracion datos = (ColecUbicCTValoracion) gson.fromJson(jsonDatos, ColecUbicCTValoracion.class);
                if (datos != null) {
                    ColecUbicCTValoracion respuestaGuardar = meLanbide48Manager.guardarColecUbicCTValoracion(datos, adapt);
                    if(respuestaGuardar!=null)
                        respuestaServicio = "OK";
                    else
                        respuestaServicio = "ERROR";
                }
            }
        } catch (Exception e) {
            log.error("Se ha presentado un erro al registrar Datos Ubicaciones de Centros de Trabajo", e);
            respuestaServicio = null;
        }
        parsearRespuestasEnviarJSON(request, response, respuestaServicio);
        log.info("guardarDatosValoracionUbicacionCentTra - End()" + formatFechaLog.format(new Date()));
    }
    
    /**
     * Llamada ajax desde Js para cargar desplegable de convocatoria por procedimiento
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @param request
     * @param response 
     */
    public void cagarDesplegableConvocatoriasProcAdj(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoProcedimiento = request.getParameter("codProcedimiento");
        List<SelectItem> listaAmbitos = new ArrayList<SelectItem>();
        try {
            int codIdioma = MeLanbide48Utils.getIdiomaUsuarioFromRequest(request);
            listaAmbitos = meLanbide48Manager.cagarDesplegableConvocatoriasProcAdj(codigoProcedimiento,codIdioma,this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        } catch (Exception ex) {
            log.error("Error recuperando la lista de Convocatorias Proceso adjudicacion ", ex);
            listaAmbitos=null;
        }
        parsearRespuestasEnviarJSON(request,response,listaAmbitos);
    }
    
    /**
     * Ejecuta el proceso de adjudicacion para COLEC. Segun la convocatoria pasada.
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @param request
     * @param response 
     */
    public void lanzarProcesoAdjudicacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("lanzarProcesoAdjudicacion - Begin() " + formatFechaLog.format(new Date()));
        String idBdConvocatoria = request.getParameter("idBdConvocatoria");
        ColecProcesoAdjudicacionRespuestaVO respuesta = null;
        try {
            int codIdioma = MeLanbide48Utils.getIdiomaUsuarioFromRequest(request);
            AdaptadorSQLBD adaptadorSQLBD = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            if(idBdConvocatoria!=null && !idBdConvocatoria.isEmpty() && !idBdConvocatoria.equalsIgnoreCase("null")){
                respuesta=meLanbide48Manager.lanzarProcesoAdjudicacion(Integer.valueOf(idBdConvocatoria),codIdioma,adaptadorSQLBD);
            }else{
                respuesta = new ColecProcesoAdjudicacionRespuestaVO("ERROR",meLanbide48I18n.getMensaje(codIdioma, "procesos.adjudicacion.error.idbdconvocatoria.norecibida"));
            }            
        } catch (Exception ex) {
            log.error("Error recuperando la lista de Convocatorias Proceso adjudicacion ", ex);
            respuesta = new ColecProcesoAdjudicacionRespuestaVO("ERROR",ex.getMessage() + " - " + ex.getLocalizedMessage());
        }
        log.info("lanzarProcesoAdjudicacion - End () " + (respuesta!= null ? respuesta.toString() :"") + " " +  formatFechaLog.format(new Date()));
        parsearRespuestasEnviarJSON(request, response, respuesta);
    }
    
    /**
     * Genera el documento excel con los resultados del proceso de adjudicacion.
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @param request
     * @param response 
     */
    public void generarExcelProcesoAdjudicacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("generarExcelProcesoAdjudicacion - Begin() " + formatFechaLog.format(new Date()));
        String idBdConvocatoria = request.getParameter("idBdConvocatoria");
        try {
            int codIdioma = MeLanbide48Utils.getIdiomaUsuarioFromRequest(request);
            AdaptadorSQLBD adaptadorSQLBD = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            SimpleDateFormat formatFileName = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            String rutaArchivoSalida = null;
            AtomicInteger  numFila= null;
            HSSFWorkbook libro = new HSSFWorkbook();
            Map<String,HSSFCellStyle> estilosCelda = colecProcesoAdjGestionExcel.crearEstilosCeldas(libro);
           
            List<ColecColectivo> colectivos = meLanbide48Manager.getColecColectivoTodos(adaptadorSQLBD);
            String preTextoNombreHoja= meLanbide48I18n.getMensaje(codIdioma, "label.solicitud.colectivo");
            for (ColecColectivo colectivo : colectivos) {
                numFila= new AtomicInteger(0);
                String nombreHoja = preTextoNombreHoja.toUpperCase().concat(" "+colectivo.getId());
                HSSFSheet hojaColectivo = libro.createSheet(nombreHoja);
                
                colecProcesoAdjGestionExcel.definirAnchoColumnas(hojaColectivo);
                
                List<ColecAmbitosBloquesHoras> ambitosBloquesHoras = meLanbide48Manager.getColecAmbitosBloquesHorasByConvocatoriaColectivo(Integer.valueOf(idBdConvocatoria),colectivo.getId(),adaptadorSQLBD);
                
                for (ColecAmbitosBloquesHoras ambitoBloquesHoras : ambitosBloquesHoras) {
                    Map<String,Double> mapaTotales = new  HashMap<String, Double>(){{
                        put("sumaTotalUbicaciones",0.0);                    // Linea total para COlectivos 1 y 2
                        put("sumaTotalAmbitoBloquesSolitados",0.0);         // Linea total para COlectivos 3 y 4
                        put("sumaTotalAmbitoBloquesConcedidos",0.0);        // Linea total para COlectivos 3 y 4
                        put("sumaTotalAmbitoValoracionesMaxEntidad",0.0);   // Linea total para COlectivos 1 a 2
                    }};
                    colecProcesoAdjGestionExcel.agregarDatosAmbitoNumeroBloques(libro, hojaColectivo, ambitoBloquesHoras, numFila, meLanbide48I18n, codIdioma,estilosCelda);
                    numFila.incrementAndGet();
                    numFila.incrementAndGet();
                    colecProcesoAdjGestionExcel.agregarDatosCabeceraTablaBloques(libro, hojaColectivo, ambitoBloquesHoras, numFila, meLanbide48I18n, 1, estilosCelda);
                    colecProcesoAdjGestionExcel.agregarDatosCabeceraTablaBloques(libro, hojaColectivo, ambitoBloquesHoras, numFila, meLanbide48I18n, 4, estilosCelda);
                    colecProcesoAdjGestionExcel.agregarDatosUbicacionesTablaBloques(libro, hojaColectivo, ambitoBloquesHoras, numFila, meLanbide48I18n, codIdioma, estilosCelda,mapaTotales,adaptadorSQLBD);
                    colecProcesoAdjGestionExcel.agregarDatosTotalesTablaBloques(libro, hojaColectivo, ambitoBloquesHoras, numFila, meLanbide48I18n, codIdioma, estilosCelda,mapaTotales);
                    numFila.incrementAndGet();                    
                    numFila.incrementAndGet();                    
                }
            }
            
            File informe = new File(m_Conf.getString("PDF.base_dir"),
                    "resolucionHorasCOLEC_"+formatFileName.format(new Date())+".xls"
            );
            FileOutputStream archivoSalida = new FileOutputStream(informe);
            libro.write(archivoSalida);
            archivoSalida.close();

            rutaArchivoSalida = informe.getAbsolutePath();

            FileInputStream istr = new FileInputStream(rutaArchivoSalida);
            BufferedInputStream bstr = new BufferedInputStream(istr);

            int size = (int) informe.length();
            byte[] data = new byte[size];
            bstr.read(data, 0, size);
            bstr.close();

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment; filename=" + informe.getName());
            response.setHeader("Content-Transfer-Encoding", "binary");
            response.setContentLength(data.length);
            response.getOutputStream().write(data, 0, data.length);
            response.getOutputStream().flush();
            response.getOutputStream().close();
                    
        } catch (Exception ex) {
            log.error("Error generarExcelProcesoAdjudicacion ", ex);
        }
        log.info("generarExcelProcesoAdjudicacion - End () " + formatFechaLog.format(new Date()));
        //parsearRespuestasEnviarJSON(request, response, respuesta);
    }

    public void getMostrarDatosCertificadosCalidadEntidad(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        Integer idEntidad = request.getParameter("idEntidad")!=null && !request.getParameter("idEntidad").isEmpty() && !request.getParameter("idEntidad").equalsIgnoreCase("null") ? Integer.valueOf(request.getParameter("idEntidad")):null;
        Integer idBDConvocatoriaExpediente = request.getParameter("idBDConvocatoriaExpediente")!=null && !request.getParameter("idBDConvocatoriaExpediente").isEmpty() && !request.getParameter("idBDConvocatoriaExpediente").equalsIgnoreCase("null") ? Integer.valueOf(request.getParameter("idBDConvocatoriaExpediente")):null;
        List<ColecEntidadCertCalidad> listaCertificadosCalidadEntidad = new ArrayList<ColecEntidadCertCalidad>();
        try {
            listaCertificadosCalidadEntidad = meLanbide48ManagerColecEntidadCertCalidad.getListaColecEntidadCertCalidadByCodEntidad(idEntidad, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        } catch (Exception ex) {
            log.error("Error getMostrarDatosCertificadosCalidadEntidad"+ ex.getMessage(), ex);
        }
        parsearRespuestasEnviarJSON(request,response,listaCertificadosCalidadEntidad);
    }

    public void getListaColecCertCalidad(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        List<ColecCertCalidad> listaCertificadosCalidad = new ArrayList<ColecCertCalidad>();
        try {
            listaCertificadosCalidad = meLanbide48ManagerColecCertCalidad.getColecCertCalidad(getAdaptSQLBD(String.valueOf(codOrganizacion)));
        } catch (Exception ex) {
            log.error("Error getListaColecCertCalidad"+ ex.getMessage(), ex);
        }
        parsearRespuestasEnviarJSON(request,response,listaCertificadosCalidad);
    }

    public void getListaColecCompIgualdad(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        List<ColecCompIgualdad> listaColecCompIgualdad = new ArrayList<ColecCompIgualdad>();
        try {
            listaColecCompIgualdad = meLanbide48ManagerColecCompIgualdad.getColecCompIgualdad(getAdaptSQLBD(String.valueOf(codOrganizacion)));
        } catch (Exception ex) {
            log.error("Error getListaColecCertCalidad"+ ex.getMessage(), ex);
        }
        parsearRespuestasEnviarJSON(request,response,listaColecCompIgualdad);
    }
    public void getListaColecCertCalidadPuntuacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        List<ColecCertCalidadPuntuacion> listaCertificadosCalidadPuntuacion = new ArrayList<ColecCertCalidadPuntuacion>();
        try {
            Integer idBDConvocatoriaExpediente = request.getParameter("idBDConvocatoriaExpediente")!=null && !request.getParameter("idBDConvocatoriaExpediente").isEmpty() && !request.getParameter("idBDConvocatoriaExpediente").equalsIgnoreCase("null") ? Integer.valueOf(request.getParameter("idBDConvocatoriaExpediente")):null;
            listaCertificadosCalidadPuntuacion = meLanbide48ManagerColecCertCalidadPuntuacion.getColecCertCalidadPuntuacionByIdConvocatoria(idBDConvocatoriaExpediente,getAdaptSQLBD(String.valueOf(codOrganizacion)));
        } catch (Exception ex) {
            log.error("Error getListaColecCertCalidad "+ ex.getMessage(), ex);
        }
        parsearRespuestasEnviarJSON(request,response,listaCertificadosCalidadPuntuacion);
    }

    public void getListaColecCompIgualdadPuntuacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        List<ColecCompIgualdadPuntuacion> listaColecCompIgualdadPuntuacion = new ArrayList<ColecCompIgualdadPuntuacion>();
        try {
            Integer idBDConvocatoriaExpediente = request.getParameter("idBDConvocatoriaExpediente")!=null && !request.getParameter("idBDConvocatoriaExpediente").isEmpty() && !request.getParameter("idBDConvocatoriaExpediente").equalsIgnoreCase("null") ? Integer.valueOf(request.getParameter("idBDConvocatoriaExpediente")):null;
            listaColecCompIgualdadPuntuacion = meLanbide48ManagerColecCompIgualdadPuntuacion.getColecCompIgualdadPuntuacionByIdConvocatoria(idBDConvocatoriaExpediente,getAdaptSQLBD(String.valueOf(codOrganizacion)));
        } catch (Exception ex) {
            log.error("Error getListaColecCertCalidad "+ ex.getMessage(), ex);
        }
        parsearRespuestasEnviarJSON(request,response,listaColecCompIgualdadPuntuacion);
    }

    
}
