/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide47;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.melanbide47.dao.MeLanbide47DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide47.i18n.MeLanbide47I18n;
import es.altia.flexia.integracion.moduloexterno.melanbide47.manager.*;
import es.altia.flexia.integracion.moduloexterno.melanbide47.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide47.util.ConstantesMeLanbide47;
import es.altia.flexia.integracion.moduloexterno.melanbide47.util.MeLanbide47Utils;
import es.altia.flexia.integracion.moduloexterno.melanbide47.vo.comun.*;
import es.altia.flexia.integracion.moduloexterno.melanbide47.vo.orientacion.*;
import es.altia.flexia.integracion.moduloexterno.melanbide47.vo.procesos.FilaAuditoriaProcesosVO;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;
import es.altia.technical.PortableContext;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.*;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;

/**
 *
 * @author santiagoc
 */
public class MELANBIDE47 extends ModuloIntegracionExterno
{
    //Logger
    private static Logger log = LogManager.getLogger(MELANBIDE47.class);
    ResourceBundle m_Conf = ResourceBundle.getBundle("common");
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    private final MELanbide47ManagerOriCertCalidad meLanbide47ManagerOriCertCalidad = new MELanbide47ManagerOriCertCalidad();
    private final MELanbide47ManagerOriCompIgualdad meLanbide47ManagerOriCompIgualdad = new MELanbide47ManagerOriCompIgualdad();
    private final MELanbide47ManagerOriEntidadCertCalidad meLanbide47ManagerOriEntidadCertCalidad = new MELanbide47ManagerOriEntidadCertCalidad();
    private final MELanbide47ManagerOriCompIgualdadPuntuacion meLanbide47ManagerOriCompIgualdadPuntuacion = new MELanbide47ManagerOriCompIgualdadPuntuacion();
    private final MELanbide47ManagerOriCertCalidadPuntuacion meLanbide47ManagerOriCertCalidadPuntuacion = new MELanbide47ManagerOriCertCalidadPuntuacion();


    // Alta Expedientes via registro platea --> MELANBIDE 42
    public void cargarExpedienteExtension(int codigoOrganizacion, String numeroExpediente, String xml) throws Exception{
        final Class cls = Class.forName("es.altia.flexia.integracion.moduloexterno.melanbide42.MELANBIDE42");
        final Object me42Class = cls.newInstance();
        final Class[] types = {int.class, String.class, String.class};
        final Method method = cls.getMethod("cargarExpedienteExtension", types);
        method.invoke(me42Class, codigoOrganizacion, numeroExpediente, xml);
    }
    
    
    public String cargarDatosORI(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
        log.info("en funcion cargarDatosORI ");
        SimpleDateFormat formatoFechaCompleto = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
        log.info("cargarDatosORI - Tiempo de Ini Metodo " + numExpediente + " " + formatoFechaCompleto.format(new Date()));
        AdaptadorSQLBD adapt = null;
        MeLanbideConvocatorias convocatoriaActiva = null;
        int idioma = MeLanbide47Utils.getEjercicioDeExpediente(numExpediente);
        try
        {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        }
        catch(Exception ex)
        {
            log.error("Error en cargarDatosORI - getAdaptSQLBD: " + ex.toString());
        }
                Date fechaReferenciaExpediente = null;
        try {
            fechaReferenciaExpediente = MeLanbide47Manager.getInstance().getFechaReferenciaDecretoExpediente(codOrganizacion,numExpediente, adapt);
            String  codProcedimiento = MeLanbide47Utils.getCodProcedimientoDeExpediente(numExpediente);
            convocatoriaActiva=MeLanbide47Manager.getInstance().getDecretoAplicableExpediente(fechaReferenciaExpediente,codProcedimiento, adapt);
            request.setAttribute("convocatoriaActiva", convocatoriaActiva);
        } catch (Exception ex) {
            log.error("Erro al tratar de leer los datos del decreto aplicable al expediente " + numExpediente + " Fecha referencia " + fechaReferenciaExpediente, ex);
        }
        String url = null;
        try
        {     
            url = cargarSubpestanaEntidadORI(codOrganizacion, numExpediente, adapt, request);
            if(url != null)
            {
                request.setAttribute("urlPestanaDatos_entidad", url);
            }
            
            url = cargarSubpestanaAmbitosORI(codOrganizacion, numExpediente,convocatoriaActiva.getDecretoCodigo(), adapt, request);
            if(url != null)
            {
                request.setAttribute("urlPestanaDatos_ambitos", url);
            }
            url = cargarSubpestanaAmbitosSolicitadosORI(codOrganizacion, numExpediente, adapt, request);
            if(url != null)
            {
                request.setAttribute("urlPestanaDatos_ambitos_solicitados", url);
            }
            url = cargarSubpestanaTrayectoriaORI(codOrganizacion, numExpediente, convocatoriaActiva.getDecretoCodigo(), adapt, request);
            if(url != null)
            {
                request.setAttribute("urlPestanaDatos_trayectoria", url);
            }
            try {
                List<SelectItem> listaCertificadosCalidad = meLanbide47ManagerOriCertCalidad.getListaOriCertCalidad(idioma,adapt);
                request.setAttribute("listaCertificadosCalidad",listaCertificadosCalidad);
            }catch (Exception ex){
                log.error("Error al cargar datos de lista Certificados de calidad",ex);
            }
            try {
                List<SelectItem> listaCompromisoIgualdad = meLanbide47ManagerOriCompIgualdad.getListaOriCompIgualdad(idioma,adapt);
                request.setAttribute("listaCompromisoIgualdad",listaCompromisoIgualdad);
            }catch (Exception ex){
                log.error("Error al cargar datos del desplegable Compromiso de igualdad",ex);
            }
        }
        catch(Exception ex)
        {
            log.error("Error en cargarDatosORI: " + ex.toString());
        }
        log.error("cargarDatosORI - Tiempo de Fin Metodo " + numExpediente + " " + formatoFechaCompleto.format(new Date()));
        return "/jsp/extension/melanbide47/orientacion/datosEspecificosORI.jsp";
    }
    
    private String cargarSubpestanaEntidadORI(int codOrganizacion, String numExpediente, AdaptadorSQLBD adapt, HttpServletRequest request)
    {
        try
        {
            int idioma = MeLanbide47Utils.getIdiomaUsuarioFromRequest(request);
            EntidadVO ent = MeLanbide47Manager.getInstance().getEntidad(codOrganizacion, numExpediente, MeLanbide47Utils.getEjercicioDeExpediente(numExpediente), adapt);
            if(ent != null)
            {
                request.setAttribute("entidad", ent);
                List<FilaAsociacionVO> asociaciones = MeLanbide47Manager.getInstance().getListaAsociacionesPorEntidad(ent, adapt);
                if(asociaciones != null)
                {
                    request.setAttribute("asociaciones", asociaciones);
                    request.setAttribute("asociacionesVal", asociaciones);
                }
            }else{
                log.debug("No existe la entidad al cargar el expediente, puede que sea uno nuevo.- Intentamos crearlo si hay uninteresado asociado al expediente");
                // Recuperamos datos del tercero asoviado al expediente, si existe creamos la entidad sino dejamos a null.
                Map<String,String> datosTercero = MeLanbide47Manager.getInstance().getDatosTercero(codOrganizacion, numExpediente, MeLanbide47Utils.getEjercicioDeExpediente(numExpediente), adapt);
                if(datosTercero!=null && datosTercero.size()>0){
                    // Creamos Objeto de la Entidad
                    EntidadVO entidadVO = new EntidadVO();
                    entidadVO.setExtMun(Integer.parseInt(datosTercero.get("EXT_MUN")));
                    entidadVO.setExtEje(Integer.parseInt(datosTercero.get("EXT_EJE")));
                    entidadVO.setExtNum(datosTercero.get("EXT_NUM"));
                    entidadVO.setExtTer(Long.parseLong(datosTercero.get("EXT_TER")));
                    entidadVO.setExtNvr(Integer.parseInt(datosTercero.get("EXT_NVR")));
                    String nombreEnt = datosTercero.get("HTE_NOC");
                    nombreEnt = (nombreEnt!=null?nombreEnt.trim().replaceAll("'",""):null);
                    entidadVO.setOriEntNom(nombreEnt);
                    log.debug("Entidad creada desde la carga de expediente. " + (entidadVO!=null?entidadVO.getOriEntCod() + "/"+entidadVO.getOriEntNom():""));
                    // Creamos el objeto con la Asociacion
                    AsociacionVO asociacionVO = new AsociacionVO();
                    asociacionVO.setOriAsocCif(datosTercero.get("HTE_DOC"));
                    asociacionVO.setOriAsocNombre(datosTercero.get("HTE_NOC"));
                    asociacionVO.setNumExpediente(datosTercero.get("EXT_NUM"));
                    
                    // Metemos la nueva entidad en la Request para que pinte la Trayectoria.
                    entidadVO = MeLanbide47Manager.getInstance().guardarEntidad(entidadVO, adapt);
                    request.setAttribute("entidad", entidadVO);
                    log.debug("Entidad cargada en la request, al ser creada cuado se carga el expediente");
                    //Asignamos el valor de Codigo de entidad a la asociacion
                    asociacionVO.setOriEntCod(entidadVO.getOriEntCod());
                    MeLanbide47Manager.getInstance().guardarAsociacion(entidadVO, asociacionVO, idioma, adapt);
                    log.debug("Asociacion creada desde la carga de expediente, para relacionar la trayectoria. " + (asociacionVO!=null?asociacionVO.getOriAsocCod()+ "/"+entidadVO.getOriEntCod()+"-"+asociacionVO.getOriAsocCif()+"/"+asociacionVO.getOriAsocNombre():""));
                    List<FilaAsociacionVO> asociaciones = MeLanbide47Manager.getInstance().getListaAsociacionesPorEntidad(entidadVO, adapt);
                    if(asociaciones != null)
                    {
                        //Metemos en la request la lista de asociacion para crear la trayectoria
                        request.setAttribute("asociaciones", asociaciones);
                        //Seran las mismas que se vayana validar
                        request.setAttribute("asociacionesVal", asociaciones);
                        log.debug("Asociacion cargada en la request desde la carga de expediente, para relacionar la trayectoria. " + (asociacionVO!=null?asociacionVO.getOriAsocCod()+ "/"+entidadVO.getOriEntCod()+"-"+asociacionVO.getOriAsocCif()+"/"+asociacionVO.getOriAsocNombre():"") + "Lista Asociaciones  con :"+asociaciones.size());
                    }
                }
            }
        }
        catch(Exception ex)
        {
            log.error("Error en cargarSubpestanaEntidadORI: " + ex.toString());
        }
        
        return "/jsp/extension/melanbide47/orientacion/entidad/entidad.jsp";
    }
    
    private String cargarSubpestanaAmbitosORI(int codOrganizacion, String numExpediente, String convocatoriaActiva, AdaptadorSQLBD adapt, HttpServletRequest request)
    {
        try
        {
            boolean nuevaCon = convocatoriaActiva.equalsIgnoreCase("CONV_2021-2023");
            log.debug("Ambitos es nueva?"+nuevaCon);
            EntidadVO ent = MeLanbide47Manager.getInstance().getEntidad(codOrganizacion, numExpediente, MeLanbide47Utils.getEjercicioDeExpediente(numExpediente), adapt);
            List<FilaUbicOrientacionVO> listaUbicOrientacion = new ArrayList<FilaUbicOrientacionVO>();
            if(ent != null)
            {
                listaUbicOrientacion = MeLanbide47Manager.getInstance().getUbicacionesORI(ent,nuevaCon, adapt);
            }
            request.setAttribute("listaUbicOrientacion", listaUbicOrientacion);
        }
        catch(Exception ex)
        {
            log.error("Error en cargarSubpestanaAmbitosORI: " + ex.toString());
        }
        
        return "/jsp/extension/melanbide47/orientacion/ambitos/ambitos.jsp";
    }
    
    private String cargarSubpestanaAmbitosSolicitadosORI(int codOrganizacion, String numExpediente, AdaptadorSQLBD adapt, HttpServletRequest request)
    {
        try
        {
            List<FilaOriAmbitoSolicitadoVO> listaAmbitosSolicitados = new ArrayList<FilaOriAmbitoSolicitadoVO>();
            listaAmbitosSolicitados = MeLanbide47Manager.getInstance().getAmbitosSolicitadosORI(numExpediente, adapt);
            request.setAttribute("listaAmbitosSolicitados", listaAmbitosSolicitados);
        }
        catch(Exception ex)
        {
            log.error("Error en cargarSubpestanaAmbitosSolicitadosORI: " + ex.toString());
        }
        return "/jsp/extension/melanbide47/orientacion/ambitos/ambitosSolicitados.jsp";
    }
    
    private String cargarSubpestanaTrayectoriaORI(int codOrganizacion, String numExpediente, String convocatoriaActiva, AdaptadorSQLBD adapt, HttpServletRequest request) {
       String url = "";
        try {
            // en funcion de la convocatoria se carga jsp correspondiente
            if (convocatoriaActiva.equalsIgnoreCase("CONV_ANTE-2021")) {
                log.debug("Convocatoria Anterior a 2021");
                OriTrayectoriaVO trayEjemplo = new OriTrayectoriaVO();
                trayEjemplo.setNumExp(numExpediente);

                // TRAYECTORIA PRESENTADA
                List<OriTrayectoriaVO> listaTrayectorias = MeLanbide47Manager.getInstance().getListaTrayectoriasPorExpediente(trayEjemplo, adapt);
                if (listaTrayectorias != null) {
                    Map<Long, OriTrayectoriaVO> mapaTrayectorias = new HashMap<Long, OriTrayectoriaVO>();
                    for (OriTrayectoriaVO tray : listaTrayectorias) {
                        if (tray.getOriAsocCod() != null) {
                            mapaTrayectorias.put(tray.getOriAsocCod(), tray);
                        }
                    }
                    request.setAttribute("trayectoriasGeneral", mapaTrayectorias);
                }

                // OTROS PROGRAMAS
                OriTrayOtroProgramaVO trayOtro = new OriTrayOtroProgramaVO();
                trayOtro.setNumExpediente(numExpediente);
                List<OriTrayOtroProgramaVO> listaTrayectoriaOtrosProg = MeLanbide47Manager.getInstance().getListaTrayectoriaOtrosProgramas(trayOtro, adapt);
                if (listaTrayectoriaOtrosProg != null) {
                    log.debug("Hay: " + listaTrayectoriaOtrosProg.size() + " PROGRAMA(s)");
                    request.setAttribute("listaTrayectoriaOtrosProg", listaTrayectoriaOtrosProg);
                }

                // ACTIVIDADES
                OriTrayActividadVO trayActividad = new OriTrayActividadVO();
                trayActividad.setNumExpediente(numExpediente);
                List<OriTrayActividadVO> listaTrayectoriaActividades = MeLanbide47Manager.getInstance().getListaTrayectoriaActividadesXNumExp(trayActividad, adapt);
                request.setAttribute("listaTrayectoriaActividades", listaTrayectoriaActividades);

                // TRAYECTORIA VALIDADA
                List<OriTrayectoriaVO> listaTrayectoriasValidadas = MeLanbide47Manager.getInstance().getListaTrayectoriasPorExpediente(trayEjemplo, adapt, true);
                if (listaTrayectoriasValidadas != null) {
                    Map<Long, OriTrayectoriaVO> mapaTrayectoriasVal = new HashMap<Long, OriTrayectoriaVO>();
                    for (OriTrayectoriaVO tray : listaTrayectoriasValidadas) {
                        if (tray.getOriAsocCod() != null) {
                            mapaTrayectoriasVal.put(tray.getOriAsocCod(), tray);
                        }
                    }
                    request.setAttribute("trayectoriasGeneralVal", mapaTrayectoriasVal);
                }
                request.setAttribute("urlPestanaDatos_trayectoriaGeneralPresentada", "/jsp/extension/melanbide47/orientacion/trayectoria/anteriores/trayectoria.jsp");
                request.setAttribute("urlPestanaDatos_trayectoriaOtrosProgramas", "/jsp/extension/melanbide47/orientacion/trayectoria/anteriores/trayectoriaOtrosProgramas.jsp");
                request.setAttribute("urlPestanaDatos_trayectoriaActividades", "/jsp/extension/melanbide47/orientacion/trayectoria/anteriores/actividades.jsp");
                request.setAttribute("urlPestanaDatos_trayectoriaGeneralValidada", "/jsp/extension/melanbide47/orientacion/trayectoria/anteriores/trayectoriaValidada.jsp");
                url = "/jsp/extension/melanbide47/orientacion/trayectoria/anteriores/pestTrayectoriaGeneral.jsp";

            } else {
                // Por defecto dejamos la de 2021. La mas nueva
                log.debug("Convocatoria 2021/2023");
                String[] datosEntidad = null;
                String codEntidad = null;
                try {
                    codEntidad = MeLanbide47Manager.getInstance().getCodEntidad(codOrganizacion, numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    if (codEntidad != null && !codEntidad.isEmpty()) {
                        request.setAttribute("codEntidad", codEntidad);
                        datosEntidad = MeLanbide47Manager.getInstance().getCifNombreEntidad(numExpediente, codEntidad, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                        if (datosEntidad != null && datosEntidad.length == 2) {
                            request.setAttribute("cifEntidad", datosEntidad[0]);
                            request.setAttribute("nombreEntidad", datosEntidad[1]);
                        }
                    }
                } catch (Exception ex) {
                    log.debug("Error al obtener el nombre de la  entidad al cargar modificar o alta Actividades-21 ", ex);
                }

                // TRAYECTORIA PRESENTADA
                OriTrayectoriaEntidadVO presentada = new OriTrayectoriaEntidadVO();
                presentada.setNumExpediente(numExpediente);
                // GRUPO 1
                presentada.setIdConActGrupo(1);
                List<OriTrayectoriaEntidadVO> listaGrupo1 = MeLanbide47Manager.getInstance().getListaTrayectoriaEntidadXGrupo(presentada, adapt);
                if (listaGrupo1 != null) {
                    log.debug("Hay: " + listaGrupo1.size() + " PROGRAMA(s)");
                }
                request.setAttribute("listaGrupo1", listaGrupo1);

                // GRUPO 2  -   EXPERIENCIA PREVIA
                presentada.setIdConActGrupo(2);
                List<OriTrayectoriaEntidadVO> listaGrupo2 = MeLanbide47Manager.getInstance().getListaTrayectoriaEntidadXGrupo(presentada, adapt);
                if (listaGrupo2 != null) {
                    log.debug("Hay: " + listaGrupo2.size() + " linea(s) de EXPERIENCIA PREVIA");
                }
                request.setAttribute("listaGrupo2", listaGrupo2);

                // GRUPO 3  -   OTROS PROGRAMAS
                presentada.setIdConActGrupo(3);
                List<OriTrayectoriaEntidadVO> listaTrayectoriaOtrosProg = MeLanbide47Manager.getInstance().getListaTrayectoriaEntidadXGrupo(presentada, adapt);
                if (listaTrayectoriaOtrosProg != null) {
                    log.debug("Hay: " + listaTrayectoriaOtrosProg.size() + " OTROS PROGRAMA(s)");
                }
                request.setAttribute("listaTrayectoriaOtrosProg", listaTrayectoriaOtrosProg);

                // GRUPO 4  -   ACTIVIDADES
                presentada.setIdConActGrupo(4);
                List<OriTrayectoriaEntidadVO> listaActividades = MeLanbide47Manager.getInstance().getListaTrayectoriaEntidadXGrupo(presentada, adapt);
                if (listaActividades != null) {
                    log.debug("Hay: " + listaActividades.size() + " ACTIVIDAD(es)");
                }
                request.setAttribute("listaTrayectoriaActividades", listaActividades);

                // GRUPO 5  -   PROGRAMAS ESPECÍFICOS DE ORIENTACIÓN
                presentada.setIdConActGrupo(5);
                List<OriTrayectoriaEntidadVO> listaGrupo5 = MeLanbide47Manager.getInstance().getListaTrayectoriaEntidadXGrupo(presentada, adapt);
                if (listaGrupo5 != null) {
                    log.debug("Hay: " + listaGrupo5.size() + " linea(s) de PROGRAMAS ESPECÍFICOS DE ORIENTACIÓN");
                }
                request.setAttribute("listaGrupo5", listaGrupo5);

                // GRUPO 6  -   PROGRAMAS Y CONTRATOS DE RECOLOCACIÓN 
                presentada.setIdConActGrupo(6);
                List<OriTrayectoriaEntidadVO> listaGrupo6 = MeLanbide47Manager.getInstance().getListaTrayectoriaEntidadXGrupo(presentada, adapt);
                if (listaGrupo6 != null) {
                    log.debug("Hay: " + listaGrupo6.size() + " linea(s) de PROGRAMAS Y CONTRATOS DE RECOLOCACIÓN");
                }
                request.setAttribute("listaGrupo6", listaGrupo6);

                // Resumen
                List<OriTrayectoriaEntidadVO> listaResumen = MeLanbide47Manager.getInstance().getTotalTrayectoriaEntidadXExp(numExpediente, adapt);
                int totalMesesNoSolapado = MeLanbide47Manager.getInstance().getCalculoNroMesesTrayectoriaNoSolapadaSolicitud(numExpediente, adapt);
                Double totalMesesNoSolapadoValidado = MeLanbide47Manager.getInstance().getCalculoNroMesesTrayectoriaNoSolapadaSolicitudValidada(numExpediente, adapt);
                if (listaResumen != null) {
                    log.debug("Hay: " + listaResumen.size() + " linea(s) en el TOTAL del expediente " + numExpediente);
                }
                request.setAttribute("totalMesesNoSolapado", totalMesesNoSolapado);
                request.setAttribute("totalMesesNoSolapadoValidado", (totalMesesNoSolapadoValidado!=null ? String.valueOf(totalMesesNoSolapadoValidado).replace(".",","):"" ));
                request.setAttribute("listaResumen", listaResumen);
                request.setAttribute("urlPestanaDatos_trayectoriaGrupo1", "/jsp/extension/melanbide47/orientacion/trayectoria/convocatoria21/grupo1.jsp");
                request.setAttribute("urlPestanaDatos_trayectoriaGrupo2", "/jsp/extension/melanbide47/orientacion/trayectoria/convocatoria21/grupo2.jsp");
                request.setAttribute("urlPestanaDatos_trayectoriaGrupo3", "/jsp/extension/melanbide47/orientacion/trayectoria/convocatoria21/grupo3.jsp");
                request.setAttribute("urlPestanaDatos_trayectoriaGrupo4", "/jsp/extension/melanbide47/orientacion/trayectoria/convocatoria21/grupo4.jsp");
                request.setAttribute("urlPestanaDatos_trayectoriaGrupo5", "/jsp/extension/melanbide47/orientacion/trayectoria/convocatoria21/grupo5.jsp");
                request.setAttribute("urlPestanaDatos_trayectoriaGrupo6", "/jsp/extension/melanbide47/orientacion/trayectoria/convocatoria21/grupo6.jsp");
                request.setAttribute("urlPestanaDatos_trayectoriaResumen", "/jsp/extension/melanbide47/orientacion/trayectoria/convocatoria21/resumen.jsp");
                url = "/jsp/extension/melanbide47/orientacion/trayectoria/convocatoria21/pestTrayectoria21.jsp";
            }
        } catch (Exception ex) {
            log.error("Error en cargarSubpestanaTrayectoriaORI: " + ex.toString());
        }
        return url;
    }

    public String cargarNuevaAsociacionORI(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        return "/jsp/extension/melanbide47/orientacion/entidad/nuevaAsociacion.jsp?codOrganizacionModulo="+codOrganizacion;
    }
    
    public String cargarModificarAsociacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            String codigoEntidad = request.getParameter("codigoEntidad");
            String codAsociacion = request.getParameter("codAsociacion");
            String opcion = request.getParameter("opcion");
            
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            
            AsociacionVO asoc = new AsociacionVO();
            asoc.setOriEntCod(Long.parseLong(codigoEntidad));
            asoc.setOriAsocCod(Long.parseLong(codAsociacion));
            asoc = MeLanbide47Manager.getInstance().getAsociacionPorCodigoYEntidad(asoc, adapt);
            if(asoc != null)
            {
                request.setAttribute("asociacionModif", asoc);
            }
            
            if(opcion != null && opcion.equals("consultar"))
            {
                request.setAttribute("consulta", true);
            }
        }
        catch(Exception ex)
        {
            log.error("Error : "+ ex.getMessage(), ex);
        }
        return "/jsp/extension/melanbide47/orientacion/entidad/nuevaAsociacion.jsp?codOrganizacionModulo="+codOrganizacion;
    }
    
    public String cargarNuevoAmbitoORI(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            cargarCombosNuevoAmbitoORI(codOrganizacion, request);
            request.setAttribute("nuevo", "1");
            
            request.setAttribute("nuevaCon", request.getParameter("nuevaCon"));
        }
        catch(Exception ex)
        {
            log.error("cargarNuevoAmbitoORI - "+ex.getMessage(), ex);

        }
        return "/jsp/extension/melanbide47/orientacion/ambitos/nuevoAmbito.jsp?codOrganizacionModulo="+codOrganizacion;
    }
    
    public String cargarModifAmbitoORI(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            cargarCombosNuevoAmbitoORI(codOrganizacion, request);
            request.setAttribute("nuevo", "0");
            String codUbic = request.getParameter("idUbic");
            boolean nuevaCon = Integer.parseInt(request.getParameter("nuevaCon")) > 4;
                           
            if(codUbic != null && !codUbic.equals(""))
            {
                OriUbicVO ubic = new OriUbicVO();
                ubic.setOriOrientUbicCod(Integer.parseInt(codUbic));
                ubic = MeLanbide47Manager.getInstance().getUbicacionORIPorCodigo(ubic,nuevaCon, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if(ubic != null && ubic.getOriAmbCod() != null)
                {
                    AmbitosHorasVO amb = MeLanbide47Manager.getInstance().getAmbitoHorasPorCodigo(ubic.getOriAmbCod(), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    if(amb != null)
                    {
                        ubic.setMunPrv(amb.getOriAmbTerHis());
                        request.setAttribute("ubicModif", ubic);
                    }
                }
                request.setAttribute("nuevaCon", request.getParameter("nuevaCon"));
            }
        }
        catch(Exception ex)
        {
            log.error("cargarModifAmbitoORI - "+ex.getMessage(), ex);
        }
        return "/jsp/extension/melanbide47/orientacion/ambitos/nuevoAmbito.jsp?codOrganizacionModulo="+codOrganizacion;
    }
    
    public String cargarAltaEdicionAmbitoSolicitadoORI(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            String esAmbiSolEdicion = request.getParameter("esAmbiSolEdicion");
            String idAmbitoSolicitado = request.getParameter("idAmbitoSolicitado");
            request.setAttribute("nuevo", esAmbiSolEdicion);
            cargarCombosNuevoAmbitoORI(codOrganizacion, request);
            if(idAmbitoSolicitado != null && idAmbitoSolicitado!="")
            {
                OriAmbitoSolicitadoVO ambiSol = new OriAmbitoSolicitadoVO();
                ambiSol.setOriAmbSolCod(Integer.parseInt(idAmbitoSolicitado));
                ambiSol.setOriAmbSolNumExp(numExpediente);
                ambiSol = MeLanbide47Manager.getInstance().getAmbitoSolicitadoORIPorCodigo(ambiSol, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if(ambiSol != null)
                {
                    request.setAttribute("ambitoSoliModif", ambiSol);
                }
            }
        }
        catch(Exception ex)
        {
            log.error("cargarAltaEdicionAmbitoSolicitadoORI - "+ex.getMessage(), ex);
        }
        return "/jsp/extension/melanbide47/orientacion/ambitos/nuevoAmbitoSolicitado.jsp";
    }
    
    public String cargarValorarAmbitoORI(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        try {
            String codUbic = request.getParameter("idUbic");
            int convActiva = request.getParameter("nuevaCon") != null ? Integer.parseInt(request.getParameter("nuevaCon")) : 4; // Por defecto anteriores a 2021
            boolean nuevaCon = Integer.parseInt(request.getParameter("nuevaCon")) > 4;
            int mesesSol = 0;
            int mesesVal = 0;
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            if (codUbic != null && !codUbic.equals("")) {
                int idioma = MeLanbide47Utils.getIdiomaUsuarioFromRequest(request);
                String codProcedimiento = MeLanbide47Utils.getCodProcedimientoDeExpediente(numExpediente);
                Date fechaReferenciaExpediente = MeLanbide47Manager.getInstance().getFechaReferenciaDecretoExpediente(codOrganizacion,numExpediente, adapt);
                MeLanbideConvocatorias convocatoriaActiva = MeLanbide47Manager.getInstance().getDecretoAplicableExpediente(fechaReferenciaExpediente,codProcedimiento, adapt);
                request.setAttribute("convocatoriaActiva", convocatoriaActiva);

                try {
                    List<SelectItem> listaCertificadosCalidad = meLanbide47ManagerOriCertCalidad.getListaOriCertCalidad(idioma,adapt);
                    request.setAttribute("listaCertificadosCalidad",listaCertificadosCalidad);
                }catch (Exception ex){
                    log.error("Error al cargar datos de lista Certificados de calidad",ex);
                }

                OriUbicValoracionVO ubicVal = new OriUbicValoracionVO();
                OriUbicVO ubic = new OriUbicVO();
                ubic.setOriOrientUbicCod(Integer.parseInt(codUbic));
                ubic = MeLanbide47Manager.getInstance().getUbicacionORIPorCodigo(ubic,nuevaCon, adapt);
                if (ubic != null) {
                    //Id
                    ubicVal.setOriOrientUbicCod(ubic.getOriOrientUbicCod());
                    //Provincia
                    if (ubic.getMunPrv() != null) {
                        try {
                            ProvinciaVO prov = MeLanbide47Manager.getInstance().getProvinciaPorCodigo(ubic.getMunPrv(), adapt);
                            if (prov != null) {
                                ubicVal.setDescProvincia(prov.getPrvNom() != null ? prov.getPrvNom() : "");
                            }
                        } catch (Exception ex) {

                        }

                        //Municipio
                        if (ubic.getMunCod() != null) {
                            try {
                                MunicipioVO mun = MeLanbide47Manager.getInstance().getMunicipioPorCodigoYProvincia(ubic.getMunCod(), ubic.getMunPrv(), adapt);
                                if (mun != null) {
                                    ubicVal.setDescMunicipio(mun.getMunNom() != null ? mun.getMunNom() : "");
                                }
                            } catch (Exception ex) {

                            }
                        }
                    }

                    //Ambito
                    if (ubic.getOriAmbCod() != null) {
                        try {
                            AmbitosHorasVO amb = MeLanbide47Manager.getInstance().getAmbitoHorasPorCodigo(ubic.getOriAmbCod(), adapt);
                            if (amb != null) {
                                ubicVal.setDescAmbito(amb.getOriAmbAmbito() != null ? amb.getOriAmbAmbito() : "");
                            }
                        } catch (Exception ex) {

                        }
                    }

                    //Direccion
                    ubicVal.setDireccion(ubic.getOriOrientDireccion() != null ? ubic.getOriOrientDireccion() : "");
                    ubicVal.setDireccionNumero(ubic.getOriOrientUbicaNumero() != null ? ubic.getOriOrientUbicaNumero() : "");
                    ubicVal.setDireccionPiso(ubic.getOriOrientUbicaPiso() != null ? ubic.getOriOrientUbicaPiso() : "");
                    ubicVal.setDireccionLetra(ubic.getOriOrientUbicaLetra() != null ? ubic.getOriOrientUbicaLetra() : "");

                    //Codigo postal
                    ubicVal.setCodPostal(ubic.getOriOrientCP() != null ? ubic.getOriOrientCP() : "");

                    //Horas Solicitadas
                    ubicVal.setHorasSolic(ubic.getOriOrientHorasSolicitadas() != null ? ubic.getOriOrientHorasSolicitadas().toString() : "");

                    //Trayectoria solicitud/validado + Plan Igualdad + Cert Calidad
                    if (ubic.getOriEntCod() != null) {
                        EntidadVO ent = new EntidadVO();
                        ent.setOriEntCod(ubic.getOriEntCod());
                        ent.setExtNum(numExpediente);
                        ent = MeLanbide47Manager.getInstance().getEntidadPorCodigoYExpediente(ent, idioma, adapt);
                        if (ent != null) {
                            ubicVal.setOriEntTrayectoriaVal(ent.getOriEntTrayectoriaVal());
                            if(nuevaCon){
                                ubicVal.setOriEntTrayectoria(MeLanbide47Manager.getInstance().getCalculoNroMesesTrayectoriaNoSolapadaSolicitud(numExpediente,adapt));
                            }else
                                ubicVal.setOriEntTrayectoria(ent.getOriEntTrayectoria());
                            ubicVal.setOriOrientValTray(ent.getOriValoracionTrayectoria());
                            ubicVal.setOriEntPlanIgualdad(ent.getPlanIgualdad());
                            ubicVal.setOriEntPlanIgualdadVal(ent.getPlanIgualdadVal());
                            ubicVal.setOriEntCertCalidad(ent.getCertificadoCalidad());
                            ubicVal.setOriEntCertCalidadVal(ent.getCertificadoCalidadVal());
                            ubicVal.setCompIgualdadOpcion(ent.getCompIgualdadOpcion());
                            ubicVal.setCompIgualdadOpcionLiteral(ent.getCompIgualdadOpcionLiteral());
                            ubicVal.setCompIgualdadOpcionVal(ent.getCompIgualdadOpcionVal());
                        }
                    }

                    //Ubicacion solicitud/validado
                    if (ubic.getOriOrientUbicCod() != null) {
                        UbicacionesVO ubicVO = MeLanbide47Manager.getInstance().getUbicacion(ubic, adapt);
                        if (ubicVO != null) {
                            ubicVal.setOriUbicPuntuacion(ubicVO.getOriUbicPuntuacion());
                            ubicVal.setOriUbicPuntuacionVal(ubicVO.getOriUbicPuntuacion());
                        }
                    }

                    //Numero despachos solicitud/validado
                    ubicVal.setOriOrientDespachos(ubic.getOriOrientDespachos());
                    ubicVal.setOriOrientDespachosValidados(ubic.getOriOrientDespachosValidados());
                    ubicVal.setOriOrientUbicaEspacioAdicional(ubic.getOriOrientUbicaEspacioAdicional());
                    ubicVal.setOriOrientUbicaEspAdicioHerrBusqEmpleo(ubic.getOriOrientUbicaEspAdicioHerrBusqEmpleo());

                    //Aula grupal solicitud/validado
                    ubicVal.setOriOrientAulagrupal(ubic.getOriOrientAulagrupal() != null ? ubic.getOriOrientAulagrupal() : "");
                    ubicVal.setOriOrientAulaGrupalValidada(ubic.getOriOrientAulaGrupalValidada() != null ? ubic.getOriOrientAulaGrupalValidada() : "");
                    ubicVal.setOriOrientUbicaEspacioAdicionalVal(ubic.getOriOrientUbicaEspacioAdicionalVal() != null ? ubic.getOriOrientUbicaEspacioAdicionalVal() : "");
                    ubicVal.setOriOrientUbicaEspAdicioHerrBusqEmpleoVal(ubic.getOriOrientUbicaEspAdicioHerrBusqEmpleoVal() != null ? ubic.getOriOrientUbicaEspAdicioHerrBusqEmpleoVal() : "");

                    //Trayectoria valoracion
                    if (!nuevaCon) {
                        ubicVal.setOriOrientValTray(ubic.getOriOrientValTray());
                    }

                    //Ubicacion valoracion
                    ubicVal.setOriOrientValUbic(ubic.getOriOrientValUbic());

                    //Despachos extra valoracion
                    ubicVal.setOriOrientValDespachos(ubic.getOriOrientValDespachos());
                    ubicVal.setOriOrientVal1EspacioAdicional(ubic.getOriOrientVal1EspacioAdicional());

                    //Aulas extra valoracion
                    ubicVal.setOriOrientValAulas(ubic.getOriOrientValAulas());
                    ubicVal.setOriOrientValEspAdicioHerrBusqEmpleo(ubic.getOriOrientValEspAdicioHerrBusqEmpleo());

                    ubicVal.setOriEntPlanIgualdadValoracion(ubic.getOriPlanIgualdadValoracion());
                    ubicVal.setOriEntCertCalidadValoracion(ubic.getOriCertCalidadValoracion());

                    //TOTAL
                    ubicVal.setOriOrientPuntuacion(ubic.getOriOrientPuntuacion());

                    //Observaciones
                    ubicVal.setOriOrientObservaciones(ubic.getOriOrientObservaciones() != null ? ubic.getOriOrientObservaciones() : "");

                    //Locales previamene aprovados 
                    ubicVal.setOriCELocalPreviamenteAprobado(ubic.getOriCELocalPreviamenteAprobado());
                    ubicVal.setOriCELocalPreviamenteAprobadoVAL(ubic.getOriCELocalPreviamenteAprobadoVAL());
                    ubicVal.setOriCELocalPreviamenteAprobadoValoracion(ubic.getOriCELocalPreviamenteAprobadoValoracion());
                    ubicVal.setOriCEMantenimientoRequisitosLPA(ubic.getOriCEMantenimientoRequisitosLPA());
                    ubicVal.setOriCEMantenimientoRequisitosLPAVAL(ubic.getOriCEMantenimientoRequisitosLPAVAL());
                    ubicVal.setOriCEMantenimientoRequisitosLPAValoracion(ubic.getOriCEMantenimientoRequisitosLPAValoracion());

                
                }
//                mesesSol= MeLanbide47Manager.getInstance().getValorCampoEntidad(numExpediente,"ORI_ENT_TRAYECTORIA",ubic.getOriEntCod(),adapt);
//               
//                mesesVal= MeLanbide47Manager.getInstance().getValorCampoEntidad(numExpediente,"ORI_ENT_TRAYECTORIA_VAL",ubic.getOriEntCod(),adapt);
//                request.setAttribute("mesesSol", mesesSol);
//                request.setAttribute("mesesVal", mesesVal);
                request.setAttribute("ubicVal", ubicVal);
                request.setAttribute("nuevaCon", convActiva);
            }
        } catch (Exception ex) {
            log.error("Error preparando carga valoracion  ubicacion: ", ex);
        }
        return "/jsp/extension/melanbide47/orientacion/ambitos/valorarAmbitoORI.jsp?codOrganizacionModulo=" + codOrganizacion;
    }

    public void guardarDatosOri14(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        EntidadVO ent = null;
        AsociacionVO asoc = null;
        List<FilaAsociacionVO> asociaciones = new ArrayList<FilaAsociacionVO>();
        MeLanbideConvocatorias convocatoriaActiva = null;

        try {
            int idioma = MeLanbide47Utils.getIdiomaUsuarioFromRequest(request);
            
            Long codEntidad = null;
            Integer asociacion = null;
            String cif = null;
            String nombre = null;
            Integer supramun = null;
            Integer admLocal = null;
            Integer centrofpPub = null;
            Integer centrofpPriv = null;
            Integer sinAnimoLucro = null;
            Integer planIgualdad = null;
            Integer certificadoCalidad = null;
            String ext_ter = "";
            String ext_nvr = "";
            
            String nombreAsociacion = null;
            
            String supramunVal = null;
            String admLocalVal = null;
            String centrofpPubVal = null;
            String centrofpPrivVal = null;
            Integer sinAnimoLucroVal = null;
            Integer planIgualdadVal = null;
            Integer certificadoCalidadVal = null;
            
            Integer aceptaMas = null;
            Integer segundosLocalesAmbito = null;
            Integer trayectoria = null;
            Integer trayectoriaVal = null;
            BigDecimal valoracionTrayectoria = null;
            
            Integer agenciaColocacion = null;
            String numAgenciaColocacion = null;
            String numAgenciaColocacionVal = null;
            String agenciaColocacionVal = null;
            
            Integer oriEntTrayectoria=null;
            Double oriEntTrayectoriaVal=null;

            String sujetoDerPublico = request.getParameter("sujetoDerPublico");
            String compIgualdadOpcion = request.getParameter("compIgualdadOpcion");
            String certificadoCalidadLista = request.getParameter("certificadoCalidadLista");
            String entSujetaDerPublValidado = request.getParameter("entSujetaDerPublValidado");
            String compromisoIgualdadValidado = request.getParameter("compromisoIgualdadValidado");
            String certificadoCalidadListaValidado = request.getParameter("certificadoCalidadListaValidado");


            String parametro = null;
            try
            {
                Date fechaReferenciaExpediente = null;
                AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                try {
                    fechaReferenciaExpediente = MeLanbide47Manager.getInstance().getFechaReferenciaDecretoExpediente(codOrganizacion, numExpediente, adapt);
                    String codProcedimiento = MeLanbide47Utils.getCodProcedimientoDeExpediente(numExpediente);
                    convocatoriaActiva = MeLanbide47Manager.getInstance().getDecretoAplicableExpediente(fechaReferenciaExpediente, codProcedimiento, adapt);
                } catch (Exception ex) {
                    log.error("Erro al tratar de leer los datos del decreto aplicable al expediente " + numExpediente + " Fecha referencia " + fechaReferenciaExpediente, ex);
                }
                boolean nuevaConvocatoria = convocatoriaActiva!=null && convocatoriaActiva.getId() > 4;
                //Recojo todos los parametros de la request
                parametro = request.getParameter("codEntidad");
                if(parametro != null && !parametro.equals(""))
                {
                    codEntidad = Long.parseLong(parametro);
                    ent= new EntidadVO();
                    ent.setOriEntCod(codEntidad);
                }
                
                parametro = request.getParameter("asociacion");
                if(parametro != null && !parametro.equals(""))
                {
                    asociacion = Integer.parseInt(parametro);
                }
                
                if(asociacion != null && asociacion == 0)
                {
                    parametro = request.getParameter("cif");
                    if(parametro != null && !parametro.equals(""))
                    {
                        cif = parametro;
                    }

                    parametro = request.getParameter("nombre");
                    if(parametro != null && !parametro.equals(""))
                    {
                        nombre = parametro;
                    }

                    parametro = request.getParameter("supramun");
                    if(parametro != null && !parametro.equals(""))
                    {
                        supramun = Integer.parseInt(parametro);
                    }

                    parametro = request.getParameter("admLocal");
                    if(parametro != null && !parametro.equals(""))
                    {
                        admLocal = Integer.parseInt(parametro);
                    }

                    parametro = request.getParameter("centrofpPub");
                    if(parametro != null && !parametro.equals(""))
                    {
                        centrofpPub = Integer.parseInt(parametro);
                    }

                    parametro = request.getParameter("centrofpPriv");
                    if(parametro != null && !parametro.equals(""))
                    {
                        centrofpPriv = Integer.parseInt(parametro);
                    }
                    
                    parametro = request.getParameter("sinAnimoLucro");
                    if(parametro != null && !parametro.equals(""))
                    {
                        sinAnimoLucro = Integer.parseInt(parametro);
                    }
                }

                if(asociacion != null && asociacion == 1)
                {
                
                    parametro = request.getParameter("nombreAsociacion");
                    if(parametro != null && !parametro.equals(""))
                    {
                        nombreAsociacion = parametro;
                    }
                }
                
                parametro = request.getParameter("supramunVal");
                if(parametro != null && !parametro.equals(""))
                {
                    supramunVal = parametro;
                }
                
                parametro = request.getParameter("admLocalVal");
                if(parametro != null && !parametro.equals(""))
                {
                    admLocalVal = parametro;
                }
                
                parametro = request.getParameter("centrofpPubVal");
                if(parametro != null && !parametro.equals(""))
                {
                    centrofpPubVal = parametro;
                }
                
                parametro = request.getParameter("centrofpPrivVal");
                if(parametro != null && !parametro.equals(""))
                {
                    centrofpPrivVal = parametro;
                }
                
                parametro = request.getParameter("sinAnimoLucroVal");
                if(parametro != null && !parametro.equals(""))
                {
                    sinAnimoLucroVal = Integer.parseInt(parametro);
                }
                
                parametro = request.getParameter("aceptaMas");
                if(parametro != null && !parametro.equals(""))
                {
                    aceptaMas = Integer.parseInt(parametro);
                }
                parametro = request.getParameter("segundosLocalesAmbito");
                if(parametro != null && !parametro.equals(""))
                {
                    segundosLocalesAmbito = Integer.parseInt(parametro);
                }
                
                parametro = request.getParameter("trayectoriaVal");
                if(parametro != null && !parametro.equals("") && !parametro.equalsIgnoreCase("null"))
                {
                    if (nuevaConvocatoria) {
                        parametro=parametro.replace(",", ".");
                        valoracionTrayectoria = new BigDecimal(parametro);
                    } else {
                        trayectoriaVal = Integer.parseInt(parametro);
                    }                   
                }
                
                parametro = request.getParameter("agenciaColocacion");
                if(parametro != null && !parametro.equals(""))
                {
                    agenciaColocacion = Integer.parseInt(parametro);
                }
                parametro = request.getParameter("agenciaColocacionVal");
                if(parametro != null && !parametro.equals(""))
                {
                    agenciaColocacionVal = parametro;
                }
                /////////////////////////////////////////
                parametro = request.getParameter("numAgenciaColocacion");
                if(parametro != null && !parametro.equals(""))
                {
                    numAgenciaColocacion = parametro;
                }
                parametro = request.getParameter("numAgenciaColocacionVal");
                if(parametro != null && !parametro.equals(""))
                {
                    numAgenciaColocacionVal = parametro;
                }
                parametro = request.getParameter("planIgualdad");
                if(parametro != null && !parametro.equals(""))
                {
                    planIgualdad = Integer.parseInt(parametro);
                }
                parametro = request.getParameter("certificadoCalidad");
                if(parametro != null && !parametro.equals(""))
                {
                    certificadoCalidad = Integer.parseInt(parametro);
                }
                parametro = request.getParameter("planIgualdadVal");
                if(parametro != null && !parametro.equals(""))
                {
                    planIgualdadVal = Integer.parseInt(parametro);
                }
                parametro = request.getParameter("certificadoCalidadVal");
                if(parametro != null && !parametro.equals(""))
                {
                    certificadoCalidadVal = Integer.parseInt(parametro);
                }
                parametro = request.getParameter("oriEntTrayectoria");
                if(parametro != null && !parametro.equals(""))
                {
                    oriEntTrayectoria = Integer.parseInt(parametro);
                }
                parametro = request.getParameter("oriEntTrayectoriaVal");
                if(parametro != null && !parametro.equals(""))
                {
                    parametro=parametro.replace(",", ".");
                    oriEntTrayectoriaVal = Double.valueOf(parametro);
                }
                
                parametro = request.getParameter("ext_ter");
                if(parametro != null && !parametro.equals(""))
                {
                    ext_ter = parametro;
                }
                
                parametro = request.getParameter("ext_nvr");
                if(parametro != null && !parametro.equals(""))
                {
                    ext_nvr = parametro;
                }
                /////////////////////////////////////////
                
                Integer ejercicio = MeLanbide47Utils.getEjercicioDeExpediente(numExpediente);
                try
                {
                    ent = new EntidadVO();
                    ent.setOriEntCod(codEntidad);
                    ent.setExtMun(codOrganizacion);
                    ent.setExtEje(ejercicio);
                    ent.setExtNum(numExpediente);
                    //ent.setOriEntNom(nombreAsociacion);
                    ent.setOriEntNom(nombre);
                    ent.setOriEntAsociacion(asociacion);
                    ent.setOriEntAceptaMas(aceptaMas);
                    ent.setOriEntSupramunVal(supramunVal);
                    if (nuevaConvocatoria) {
                        ent.setOriValoracionTrayectoria(valoracionTrayectoria);
                        ent.setOriEntTrayectoriaVal(oriEntTrayectoriaVal);
                        ent.setOriEntTrayectoria(oriEntTrayectoria);
                    } else {
                        if(trayectoriaVal!=null)
                            ent.setOriEntTrayectoriaVal(Double.valueOf(String.valueOf(trayectoriaVal).replace(",", ".")));
                    }
                    ent.setOriEntAdmLocalVal(admLocalVal);
                    ent.setOriExpCentrofpPrivVal(centrofpPrivVal);
                    ent.setOriExpCentrofpPubVal(centrofpPubVal);
                    ent.setAgenciaColocacionVal(agenciaColocacionVal);
                    ent.setSegundosLocalesAmbito(segundosLocalesAmbito);
                    ent.setSinAnimoLucroVal(sinAnimoLucroVal);
                    ent.setNumAgenciaColocacion(numAgenciaColocacion);
                    ent.setPlanIgualdad(planIgualdad);
                    ent.setCertificadoCalidad(certificadoCalidad);
                    ent.setNumAgenciaColocacionVal(numAgenciaColocacionVal);
                    ent.setPlanIgualdadVal(planIgualdadVal);
                    ent.setCertificadoCalidadVal(certificadoCalidadVal);
                    if (ext_ter != null && !ext_ter.equals("") && !ext_ter.equals("null"))
                    {
                        log.debug("ext_ter: "+ext_ter);
                        ent.setExtTer(Long.parseLong(ext_ter));
                    }
                                     
                    if (ext_nvr != null && !ext_nvr.equals("") && !ext_nvr.equals("null")) {
                        log.debug("ext_nvr: "+ext_nvr);
                        ent.setExtNvr(Integer.parseInt(ext_nvr));
                    }
                    //log.info("ent.planIgualdad+++++++: "+ent.getPlanIgualdad());
                    //log.info("ent.certificadoCalidad+++++++: "+ent.getCertificadoCalidad());
                
                    asoc = new AsociacionVO();
                    asoc.setOriAsocCif(cif);
                    asoc.setOriAsocNombre(nombre);
                    asoc.setOriEntAdmLocal(admLocal);
                    asoc.setOriEntSupramun(supramun);
                    asoc.setOriExpCentrofpPriv(centrofpPriv);
                    asoc.setOriExpCentrofpPub(centrofpPub);
                    asoc.setAgenciaColocacion(agenciaColocacion);
                    asoc.setOriExpSinAnimoLucro(sinAnimoLucro);
                    asoc.setNumAgenciaColocacion(numAgenciaColocacion);
                    asoc.setPlanIgualdad(planIgualdad);
                    asoc.setCertificadoCalidad(certificadoCalidad);
                    asoc.setNumAgenciaColocacionVal(numAgenciaColocacionVal);
                    asoc.setPlanIgualdadVal(planIgualdadVal);
                    asoc.setCertificadoCalidadVal(certificadoCalidadVal);
                    // Convocatoria 2023-2025
                    asoc.setEntSujetaDerPubl(sujetoDerPublico != null && !sujetoDerPublico.equals("") && !sujetoDerPublico.equalsIgnoreCase("null") ? Integer.valueOf(sujetoDerPublico) : null);
                    asoc.setCompIgualdadOpcion(compIgualdadOpcion != null && !compIgualdadOpcion.equals("") && !compIgualdadOpcion.equalsIgnoreCase("null") ? Integer.valueOf(compIgualdadOpcion) : null);
                    asoc.setEntSujetaDerPublVal(entSujetaDerPublValidado != null && !entSujetaDerPublValidado.equals("") && !entSujetaDerPublValidado.equalsIgnoreCase("null") ? Integer.valueOf(entSujetaDerPublValidado) : null);
                    asoc.setCompIgualdadOpcionVal(compromisoIgualdadValidado != null && !compromisoIgualdadValidado.equals("") && !compromisoIgualdadValidado.equalsIgnoreCase("null") ? Integer.valueOf(compromisoIgualdadValidado) : null);

                    ent.setEntSujetaDerPubl(sujetoDerPublico != null && !sujetoDerPublico.equals("") && !sujetoDerPublico.equalsIgnoreCase("null") ? Integer.valueOf(sujetoDerPublico) : null);
                    ent.setCompIgualdadOpcion(compIgualdadOpcion != null && !compIgualdadOpcion.equals("") && !compIgualdadOpcion.equalsIgnoreCase("null") ? Integer.valueOf(compIgualdadOpcion) : null);
                    ent.setEntSujetaDerPublVal(entSujetaDerPublValidado != null && !entSujetaDerPublValidado.equals("") && !entSujetaDerPublValidado.equalsIgnoreCase("null") ? Integer.valueOf(entSujetaDerPublValidado) : null);
                    ent.setCompIgualdadOpcionVal(compromisoIgualdadValidado != null && !compromisoIgualdadValidado.equals("") && !compromisoIgualdadValidado.equalsIgnoreCase("null") ? Integer.valueOf(compromisoIgualdadValidado) : null);

            
                    ent = MeLanbide47Manager.getInstance().guardarDatosORI(ent, asoc, nuevaConvocatoria, idioma, adapt);
                    // Guardar datos de Certificados de calidad
                    meLanbide47ManagerOriEntidadCertCalidad.guardarDatosCertificadosCalidadSolicitudValidados(ent,certificadoCalidadLista,certificadoCalidadListaValidado,adapt);
                    
                    if(ent == null)
                    {
                        codigoOperacion = "1";
                    }
                    else
                    {
                        try
                        {
                            if(ent.getOriEntAsociacion() != null && ent.getOriEntAsociacion() == 1)
                            {
                                asociaciones = MeLanbide47Manager.getInstance().getListaAsociacionesPorEntidad(ent, adapt);
                            }
                        }
                        catch(Exception ex)
                        {
                            codigoOperacion = "1";
                        }
                    }
                }
                catch(Exception ex)
                {
                    log.error("Error : "+ ex.getMessage(), ex);
                    codigoOperacion = "2";
                }
            }
            catch(Exception ex)
            {
                log.error("Error : "+ ex.getMessage(), ex);
                codigoOperacion = "3";
            }
        }
        catch(Exception ex)
        {
            log.error("Error : "+ ex.getMessage(), ex);
            codigoOperacion = "2";
        }
        
        escribirDatosEntidadRequest(codigoOperacion, ent, asociaciones, response);
    }
    
    public void guardarAsociacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        String codigoOperacion = "0";
        List<FilaAsociacionVO> asociaciones = new ArrayList<FilaAsociacionVO>();
        EntidadVO ent = null;
        AsociacionVO asoc = null;
        try
        {
            int idioma = MeLanbide47Utils.getIdiomaUsuarioFromRequest(request);
            //Recojo todos los parametros de la request
            Long codEntidad = null;
            String nombreEntidad = null;
            Long codAsociacion = null;
            String cif = null;
            String nombre = null;
            Integer supramun = null;
            Integer admLocal = null;
            Integer centrofpPub = null;
            Integer centrofpPriv = null;
            Integer agenciaColocacion = null;
            Integer sinAnimoLucro = null;
            Integer ejercicio = null;
            String parametro = null;
                        
            try
            {
                parametro = request.getParameter("codigoEntidad");
                if(parametro != null && !parametro.equals(""))
                {
                    codEntidad = Long.parseLong(parametro);
                }
                
                parametro = request.getParameter("nombreEntidad");
                if(parametro != null && !parametro.equals(""))
                {
                    nombreEntidad = parametro;
                }
                
                parametro = request.getParameter("codAsociacion");
                if(parametro != null && !parametro.equals(""))
                {
                    codAsociacion = Long.parseLong(parametro);
                }
                
                parametro = request.getParameter("cif");
                if(parametro != null && !parametro.equals(""))
                {
                    cif = parametro;
                }
                
                parametro = request.getParameter("nombre");
                if(parametro != null && !parametro.equals(""))
                {
                    nombre = parametro;
                }
                
                parametro = request.getParameter("supramun");
                if(parametro != null && !parametro.equals(""))
                {
                    supramun = Integer.parseInt(parametro);
                }
                
                parametro = request.getParameter("admLocal");
                if(parametro != null && !parametro.equals(""))
                {
                    admLocal = Integer.parseInt(parametro);
                }
                
                parametro = request.getParameter("centrofpPub");
                if(parametro != null && !parametro.equals(""))
                {
                    centrofpPub = Integer.parseInt(parametro);
                }
                
                parametro = request.getParameter("centrofpPriv");
                if(parametro != null && !parametro.equals(""))
                {
                    centrofpPriv = Integer.parseInt(parametro);
                }
                parametro = request.getParameter("agenciaColocacion");
                if(parametro != null && !parametro.equals(""))
                {
                    agenciaColocacion = Integer.parseInt(parametro);
                }
                
                parametro = request.getParameter("sinAnimoLucro");
                if(parametro != null && !parametro.equals(""))
                {
                    sinAnimoLucro = Integer.parseInt(parametro);
                }
                
                ejercicio = MeLanbide47Utils.getEjercicioDeExpediente(numExpediente);
            }
            catch(Exception ex)
            {
                codigoOperacion = "3";
            }
                    
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            
            //Creo una entidad de ejemplo con los datos de la request
            ent = new EntidadVO();
            ent.setOriEntCod(codEntidad);
            ent.setExtNum(numExpediente);
            ent.setOriEntNom(nombreEntidad);
            ent.setExtEje(ejercicio);
            ent.setExtMun(codOrganizacion);
            ent.setOriEntAsociacion(1);
            ent.setSinAnimoLucro(sinAnimoLucro);
            
            //Creo una asociacion de ejemplo con los datos de la request
            asoc = new AsociacionVO();
            asoc.setOriAsocCod(codAsociacion);
            asoc.setOriEntCod(codEntidad);
            asoc.setOriAsocCif(cif);
            asoc.setOriAsocNombre(nombre);
            asoc.setOriEntAdmLocal(admLocal);
            asoc.setOriEntSupramun(supramun);
            asoc.setOriExpCentrofpPriv(centrofpPriv);
            asoc.setOriExpCentrofpPub(centrofpPub);
            asoc.setAgenciaColocacion(agenciaColocacion);
            asoc.setOriExpSinAnimoLucro(sinAnimoLucro);
            asoc.setNumExpediente(numExpediente);
            
            if(codEntidad == null)
            {
               ent = MeLanbide47DAO.getInstance().guardarEntidad(ent, adapt.getConnection());
                
            }
            
            if(codAsociacion == null)
            {
                try
                {
                    boolean asociacionRepetida = MeLanbide47Manager.getInstance().asociacionRepetida(ent, asoc, idioma, adapt);
                    if(asociacionRepetida)
                    {
                        codigoOperacion = "5";
                    }
                }
                catch(Exception ex)
                {
                    codigoOperacion = "1";
                }
            }

            if(codigoOperacion.equals("0"))
            {
                try
                {
                    //Guardo los datos
                    asoc = MeLanbide47Manager.getInstance().guardarAsociacion(ent, asoc, idioma, adapt);

                    if(asoc == null)
                    {
                        codigoOperacion = "1";
                    }
                    else
                    {
                        asociaciones = MeLanbide47Manager.getInstance().getListaAsociacionesPorEntidad(ent, adapt);
                    }
                }
                catch(Exception ex)
                {
                    log.error("Error : "+ ex.getMessage(), ex);
                    codigoOperacion = "1";
                }
            }
        }
        catch(Exception ex)
        {
            codigoOperacion = "2";
        }
        
        this.escribirListaAsociacionesRequest(codigoOperacion, ent, asociaciones, response);
    }
    
     public void eliminarAsociacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
     {
        String codigoOperacion = "0";
        List<FilaAsociacionVO> asociaciones = new ArrayList<FilaAsociacionVO>();
        EntidadVO entidad = null;
        
        AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        try
        {
            
            Integer ejercicio = MeLanbide47Utils.getEjercicioDeExpediente(numExpediente);
            if(ejercicio != null)
            {
                
                String codEntidad = (String)request.getParameter("codEntidad");
                String codAsociacion = (String)request.getParameter("codAsociacion");

                if(codEntidad != null && !codEntidad.equals(""))
                {
                    if(codAsociacion != null && !codAsociacion.equals(""))
                    {
                        AsociacionVO asoc = new AsociacionVO();
                        try
                        {
                            asoc.setOriAsocCod(Long.parseLong(codAsociacion));
                            asoc.setOriEntCod(Long.parseLong(codEntidad));
                            int result = MeLanbide47Manager.getInstance().eliminarAsociacion(asoc, adaptador);
                            
                            if(result > 0)
                            {
                                entidad = MeLanbide47Manager.getInstance().getEntidad(codOrganizacion, numExpediente, MeLanbide47Utils.getEjercicioDeExpediente(numExpediente), adaptador);
                                if(entidad == null)
                                {
                                    codigoOperacion = "1";
                                }
                                else
                                {
                                    asociaciones = MeLanbide47Manager.getInstance().getListaAsociacionesPorEntidad(entidad, adaptador);
                                }
                            }
                            else
                            {
                                codigoOperacion = "1";
                            }
                        }
                        catch(Exception ex)
                        {
                            codigoOperacion = "3";
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
        escribirListaAsociacionesRequest(codigoOperacion, entidad, asociaciones, response);
     }
     
     public void getListaAmbitosORI(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
     {
        List<FilaUbicOrientacionVO> listaAmbitos = new ArrayList<FilaUbicOrientacionVO>();
        String codigoOperacion = "0";
        EntidadVO ent = null;
        try
        {
            int idioma = MeLanbide47Utils.getIdiomaUsuarioFromRequest(request);
            Long codEntidad = null;
            String parametro = null;
            boolean nuevaCon = false;
            try
            {
                parametro = request.getParameter("codEntidad");
                if(parametro != null && !parametro.equals(""))
                {
                    codEntidad = Long.parseLong(parametro);
                }
                int convocatoria = Integer.parseInt(request.getParameter("nuevaCon"));
                nuevaCon= convocatoria > 4;
            }
            catch(Exception ex)
            {
                log.error("Error : "+ ex.getMessage(), ex);
                codigoOperacion = "3";
            }
            
            if(codEntidad != null)
            {
                try
                {
                    AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                    
                    ent = new EntidadVO();
                    ent.setOriEntCod(codEntidad);
                    ent.setExtNum(numExpediente);
                    
                    ent = MeLanbide47Manager.getInstance().getEntidadPorCodigoYExpediente(ent, idioma, adapt);
                    if(ent != null)
                    {
                        listaAmbitos = MeLanbide47Manager.getInstance().getUbicacionesORI(ent, nuevaCon, adapt);
                    }
                    else
                    {
                        codigoOperacion = "1";
                    }
                }
                catch(Exception ex)
                {
                    log.error("Error : "+ ex.getMessage(), ex);
                    codigoOperacion = "1";
                }
            }
            else
            {
                codigoOperacion = "3";
            }
        }
        catch(Exception ex)
        {
            log.error("Error : "+ ex.getMessage(), ex);
        }
        escribirListaAmbitosRequest(codigoOperacion, ent, listaAmbitos, response);
     }
     
    public void guardarAmbitoORI(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<FilaUbicOrientacionVO> ambitos = new ArrayList<FilaUbicOrientacionVO>();
        EntidadVO ent = null;
        OriUbicVO ubic = null;
        try {
            int idioma = MeLanbide47Utils.getIdiomaUsuarioFromRequest(request);
            //Recojo los parametros
            String parametro = null;
            Long codEntidad = null;
            Integer idUbic = null;
            Integer provincia = null;
            Integer ambito = null;
            Integer municipio = null;
            String direccion = null;
            String direccionNumero = null;
            String direccionPiso = null;
            String direccionLetra = null;
            String codPostal = null;
            BigDecimal horas = null;
            String despachos = null;
            String segundaAula = null;
            Integer ejercicio = null;
            String DispMas1Ubicacion = null;
            String EspacioOrdeWifi = null;
            String ubicTelFijo = null;
            String localPreviaAprobado = null;
            String mantieneRequisitosLPA = null;
            boolean nuevaCon = Integer.parseInt(request.getParameter("nuevaCon")) > 4;
            try {
                parametro = request.getParameter("codEntidad");
                if (parametro != null && !parametro.equals("")) {
                    codEntidad = Long.parseLong(parametro);
                }
                parametro = request.getParameter("idUbic");
                if (parametro != null && !parametro.equals("")) {
                    idUbic = Integer.parseInt(parametro);
                }

                parametro = request.getParameter("provincia");
                if (parametro != null && !parametro.equals("")) {
                    provincia = Integer.parseInt(parametro);
                }

                parametro = request.getParameter("ambito");
                if (parametro != null && !parametro.equals("")) {
                    ambito = Integer.parseInt(parametro);
                }

                parametro = request.getParameter("municipio");
                if (parametro != null && !parametro.equals("")) {
                    municipio = Integer.parseInt(parametro);
                }

                parametro = request.getParameter("direccion");
                if (parametro != null && !parametro.equals("")) {
                    direccion = parametro;
                }
                parametro = request.getParameter("direccionNumero");
                if (parametro != null && !parametro.equals("")) {
                    direccionNumero = parametro;
                }
                parametro = request.getParameter("direccionPiso");
                if (parametro != null && !parametro.equals("")) {
                    direccionPiso = parametro;
                }
                parametro = request.getParameter("direccionLetra");
                if (parametro != null && !parametro.equals("")) {
                    direccionLetra = parametro;
                }

                parametro = request.getParameter("codPostal");
                if (parametro != null && !parametro.equals("")) {
                    codPostal = parametro;
                }

                parametro = request.getParameter("horas");
                if (parametro != null && !parametro.equals("")) {
                    horas = new BigDecimal(parametro);
                }

                parametro = request.getParameter("despachos");
                if (parametro != null && !parametro.equals("")) {
                    despachos = parametro;
                }

                parametro = request.getParameter("segundaAula");
                if (parametro != null && !parametro.equals("")) {
                    segundaAula = parametro;
                }
                parametro = request.getParameter("DispMas1Ubicacion");
                if (parametro != null && !parametro.equals("")) {
                    DispMas1Ubicacion = parametro;
                }
                parametro = request.getParameter("EspacioOrdeWifi");
                if (parametro != null && !parametro.equals("")) {
                    EspacioOrdeWifi = parametro;
                }
                parametro = request.getParameter("ubicTelFijo");
                if (parametro != null && !parametro.equals("")) {
                    ubicTelFijo = parametro;
                }
                parametro = request.getParameter("localPreviaAprobado");
                if (parametro != null && !parametro.equals("")) {
                    localPreviaAprobado = parametro;
                }
                parametro = request.getParameter("mantieneRequisitosLPA");
                if (parametro != null && !parametro.equals("")) {
                    mantieneRequisitosLPA = parametro;
                }
                ejercicio = MeLanbide47Utils.getEjercicioDeExpediente(numExpediente);
            } catch (Exception ex) {
                codigoOperacion = "3";
            }

            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            //Creo una entidad de ejemplo con los datos de la request
            ent = new EntidadVO();
            ent.setOriEntCod(codEntidad);
            ent.setExtNum(numExpediente);
            ent.setExtEje(ejercicio);
            ent.setExtMun(codOrganizacion);
            //Creo una entidad de ejemplo con los datos de la request
            ubic = new OriUbicVO();
            ubic.setOriEntCod(codEntidad);
            ubic = MeLanbide47Manager.getInstance().getUbicacionORIPorCodigo(ubic, nuevaCon, adapt);
            ubic.setOriOrientUbicCod(idUbic);

            if (ubic == null) {
                ubic = new OriUbicVO();
                ubic.setOriEntCod(codEntidad);
                ubic.setOriOrientUbicCod(idUbic);
            }
 log.debug("--------------> 6");
            ubic.setNumExp(numExpediente);
            ubic.setOriOrientAno(ejercicio);
            ubic.setMunPai(ConstantesMeLanbide47.CODIGO_PAIS_ESPANA);
            ubic.setPrvPai(ConstantesMeLanbide47.CODIGO_PAIS_ESPANA);
            ubic.setMunPrv(provincia);
            ubic.setOriAmbCod(ambito);
            ubic.setMunCod(municipio);
            ubic.setOriOrientDireccion(direccion);
            ubic.setOriOrientUbicaNumero(direccionNumero);
            ubic.setOriOrientUbicaPiso(direccionPiso);
            ubic.setOriOrientUbicaLetra(direccionLetra);
            ubic.setOriOrientCP(codPostal);
            ubic.setOriOrientHorasSolicitadas(horas);
            ubic.setOriOrientDespachos(despachos);
            ubic.setOriOrientAulagrupal(segundaAula != null && !segundaAula.equals("") ? segundaAula : ConstantesMeLanbide47.NO);
            ubic.setOriOrientUbicaEspacioAdicional(DispMas1Ubicacion != null && !DispMas1Ubicacion.equals("") ? DispMas1Ubicacion : ConstantesMeLanbide47.NO);
            ubic.setOriOrientUbicaEspAdicioHerrBusqEmpleo(EspacioOrdeWifi != null && !EspacioOrdeWifi.equals("") ? EspacioOrdeWifi : ConstantesMeLanbide47.NO);
            ubic.setOriOrientUbicaTeleFijo(ubicTelFijo);
            ubic.setOriCELocalPreviamenteAprobado(localPreviaAprobado != null && !localPreviaAprobado.equals("") ? Integer.parseInt(localPreviaAprobado) : null);
            ubic.setOriCEMantenimientoRequisitosLPA(mantieneRequisitosLPA != null && !mantieneRequisitosLPA.equals("")  ? Integer.parseInt(mantieneRequisitosLPA) : null);

            if (codigoOperacion.equals("0")) {
                try {
                    //Guardo los datos
                    ubic = MeLanbide47Manager.getInstance().guardarUbicacion(ent, ubic, nuevaCon, idioma, adapt);
                    if (ubic == null) {
                        codigoOperacion = "1";
                    } else {
                        ambitos = MeLanbide47Manager.getInstance().getUbicacionesORI(ent, nuevaCon, adapt);
                    }
                     log.debug("--------------> 9");
                } catch (Exception ex) {
                    log.error("Error : " + ex.getMessage(), ex);
                    codigoOperacion = "1";
                }
            }
             log.debug("--------------> 10");
        } catch (Exception ex) {
            codigoOperacion = "2";
        }
        escribirListaAmbitosRequest(codigoOperacion, ent, ambitos, response);
    }

     public void guardarAmbitoSolicitadoORI(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
     {
        String codigoOperacion = "0";
        List<FilaOriAmbitoSolicitadoVO> ambitos = new ArrayList<FilaOriAmbitoSolicitadoVO>();
        EntidadVO ent = null;
        OriAmbitoSolicitadoVO ambitSol = null;
        try
        {
            //Recojo los parametros
            Integer ejercicio = null;
            String parametro = null;
            Integer idAmbitoSolicitado = null;
            Integer codProvinciaAmbitoSolicitado = null;
            Integer codAmbitoSolicitado = null;
            Integer nroBloquesSolic = null;
            Integer nroUbicacionesxAmbito = null;
            
            try
            {
                parametro = request.getParameter("idAmbitoSolicitado");
                if(parametro != null && !parametro.equals(""))
                {
                    idAmbitoSolicitado = Integer.parseInt(parametro);
                }
                parametro = request.getParameter("codProvinciaAmbitoSolicitado");
                if(parametro != null && !parametro.equals(""))
                {
                    codProvinciaAmbitoSolicitado = Integer.parseInt(parametro);
                }
                
                parametro = request.getParameter("codAmbitoSolicitado");
                if(parametro != null && !parametro.equals(""))
                {
                    codAmbitoSolicitado = Integer.parseInt(parametro);
                }
                
                parametro = request.getParameter("nroBloquesSolic");
                if(parametro != null && !parametro.equals(""))
                {
                    nroBloquesSolic = Integer.parseInt(parametro);
                }
                
                parametro = request.getParameter("nroUbicacionesxAmbito");
                if(parametro != null && !parametro.equals(""))
                {
                    nroUbicacionesxAmbito = Integer.parseInt(parametro);
                }
                ejercicio = MeLanbide47Utils.getEjercicioDeExpediente(numExpediente);
            }
            catch(Exception ex)
            {
                codigoOperacion = "3";
            }
            
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            
            ambitSol = new OriAmbitoSolicitadoVO();
            ambitSol.setOriAmbSolCod(idAmbitoSolicitado);
            ambitSol.setOriAmbSolNumExp(numExpediente);
            ambitSol.setOriAmbSolTerHis(codProvinciaAmbitoSolicitado);
            ambitSol.setOriAmbSolAmbito(codAmbitoSolicitado);
            ambitSol.setOriAmbSolNroBloques(nroBloquesSolic);
            ambitSol.setOriAmbSolNroUbic(nroUbicacionesxAmbito);
      
            try
            {
                //Guardo los datos
                ambitSol = MeLanbide47Manager.getInstance().guardarAmbitoSolicitadoORI(ambitSol, adapt);

                if(ambitSol == null)
                {
                    codigoOperacion = "1";
                }
                else
                {
                    ambitos = MeLanbide47Manager.getInstance().getAmbitosSolicitadosORI(numExpediente, adapt);
                }
            }
            catch(Exception ex)
            {
                log.error("Error : "+ ex.getMessage(), ex);
                codigoOperacion = "1";
            }
        }   
        catch(Exception ex)
        {
            codigoOperacion = "2";
        }
        escribirResultadoGuardarEliminarAmbitoSolicitadoResponse(codigoOperacion, ambitos, response);
     }
    
     public void eliminarAmbitoORI(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
     {
        String codigoOperacion = "0";
        List<FilaUbicOrientacionVO> ambitos = new ArrayList<FilaUbicOrientacionVO>();
        EntidadVO entidad = null;
        boolean nuevaCon;
        AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        try
        {
            
            Integer ejercicio = MeLanbide47Utils.getEjercicioDeExpediente(numExpediente);
            if(ejercicio != null)
            {
                
                String codEntidad = (String)request.getParameter("codEntidad");
                String idUbic = (String)request.getParameter("idUbic");
                int convocatoria = Integer.parseInt(request.getParameter("nuevaCon"));
                nuevaCon= convocatoria > 4;
                if(codEntidad != null && !codEntidad.equals(""))
                {
                    if(idUbic != null && !idUbic.equals(""))
                    {
                        OriUbicVO ubic = new OriUbicVO();
                        try
                        {
                            ubic.setOriOrientUbicCod(Integer.parseInt(idUbic));
                            ubic.setOriEntCod(Long.parseLong(codEntidad));
                            int result = MeLanbide47Manager.getInstance().eliminarUbicacionORI(ubic, adaptador);
                            
                            if(result > 0)
                            {
                                entidad = MeLanbide47Manager.getInstance().getEntidad(codOrganizacion, numExpediente, MeLanbide47Utils.getEjercicioDeExpediente(numExpediente), adaptador);
                                if(entidad == null)
                                {
                                    codigoOperacion = "1";
                                }
                                else
                                {
                                    ambitos = MeLanbide47Manager.getInstance().getUbicacionesORI(entidad, nuevaCon,adaptador);
                                }
                            }
                            else
                            {
                                codigoOperacion = "1";
                            }
                        }
                        catch(Exception ex)
                        {
                            codigoOperacion = "3";
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
        escribirListaAmbitosRequest(codigoOperacion, entidad, ambitos, response);
     }
     
    public void valorarAmbitoORI(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<FilaUbicOrientacionVO> ambitos = new ArrayList<FilaUbicOrientacionVO>();
        EntidadVO ent = null;
        OriUbicVO ubic = null;
        try {
            int idioma = MeLanbide47Utils.getIdiomaUsuarioFromRequest(request);
            //Recojo los parametros
            String parametro = null;
            Long codEntidad = null;
            Integer ejercicio = null;
            Integer idUbic = null;
            String despachos = null;
            String aulaGrupal = null;
            BigDecimal trayVal = null;
            BigDecimal ubicVal = null;
            Long despVal = null;
            Long aulaVal = null;
            BigDecimal puntuacion = null;
            String observaciones = null;
            String disp1EspaAdicional = null;
            String dispEspaOrdeIntWifi = null;
            Long disp1EspaAdicionalValor = null;
            Long dispEspaOrdeIntWifiValor = null;
            Integer localPreviamenteAprobadoVAL = null;
            Integer mantenimientoRequisitosLPAVAL = null;
            Long localPreviamenteAprobadoValoracion = null;
            Long mantenimientoRequisitosLPAValoracion = null;
            Integer planIgualdad = null;
            Integer planIgualdadVal = null;
            Long planIgualdadValoracion = null;
            Integer certCalidad = null;
            Integer certCalidadVal = null;
            Long certCalidadValoracion = null;
            boolean nuevaCon = Integer.parseInt(request.getParameter("nuevaCon"))>4;
            try {
                parametro = request.getParameter("codEntidad");
                if (parametro != null && !parametro.equals("")) {
                    codEntidad = Long.parseLong(parametro);
                }

                parametro = request.getParameter("idUbic");
                if (parametro != null && !parametro.equals("")) {
                    idUbic = Integer.parseInt(parametro);
                }

                parametro = request.getParameter("despachos");
                if (parametro != null && !parametro.equals("")) {
                    despachos = parametro;
                }

                parametro = request.getParameter("aulaGrupal");
                if (parametro != null && !parametro.equals("")) {
                    aulaGrupal = parametro;
                }

                parametro = request.getParameter("trayVal");
                if (parametro != null && !parametro.equals("")) {
                    trayVal = new BigDecimal(parametro);
                }

                parametro = request.getParameter("ubicVal");
                if (parametro != null && !parametro.equals("")) {
                    ubicVal = new BigDecimal(parametro);
                }

                parametro = request.getParameter("despVal");
                if (parametro != null && !parametro.equals("")) {
                    despVal = Long.parseLong(parametro);
                }

                parametro = request.getParameter("aulaVal");
                if (parametro != null && !parametro.equals("")) {
                    aulaVal = Long.parseLong(parametro);
                }

                parametro = request.getParameter("puntuacion");
                if (parametro != null && !parametro.equals("")) {
                    puntuacion = new BigDecimal(parametro);
                }

                parametro = request.getParameter("observaciones");
                if (parametro != null && !parametro.equals("")) {
                    observaciones = parametro;
                }

                parametro = request.getParameter("disp1EspaAdicional");
                if (parametro != null && !parametro.equals("")) {
                    disp1EspaAdicional = parametro;
                }

                parametro = request.getParameter("dispEspaOrdeIntWifi");
                if (parametro != null && !parametro.equals("")) {
                    dispEspaOrdeIntWifi = parametro;
                }

                parametro = request.getParameter("disp1EspaAdicionalValor");
                if (parametro != null && !parametro.equals("")) {
                    disp1EspaAdicionalValor = Long.parseLong(parametro);
                }

                parametro = request.getParameter("dispEspaOrdeIntWifiValor");
                if (parametro != null && !parametro.equals("")) {
                    dispEspaOrdeIntWifiValor = Long.parseLong(parametro);
                }

                parametro = request.getParameter("localPreviamenteAprobadoVAL");
                if (parametro != null && !parametro.equals("")) {
                    localPreviamenteAprobadoVAL = Integer.parseInt(parametro);
                }
                parametro = request.getParameter("mantenimientoRequisitosLPAVAL");
                if (parametro != null && !parametro.equals("")) {
                    mantenimientoRequisitosLPAVAL = Integer.parseInt(parametro);
                }
                parametro = request.getParameter("localPreviamenteAprobadoValoracion");
                if (parametro != null && !parametro.equals("")) {
                    localPreviamenteAprobadoValoracion = Long.parseLong(parametro);
                }
                parametro = request.getParameter("mantenimientoRequisitosLPAValoracion");
                if (parametro != null && !parametro.equals("")) {
                    mantenimientoRequisitosLPAValoracion = Long.parseLong(parametro);
                }
                if (nuevaCon) {
                    parametro = request.getParameter("planIgualdad");
                    if (parametro != null && !parametro.equals("")) {
                        planIgualdad = Integer.parseInt(parametro);
                    }
                    parametro = request.getParameter("planIgualdadVal");
                    if (parametro != null && !parametro.equals("")) {
                        planIgualdadVal = Integer.parseInt(parametro);
                    }
                    parametro = request.getParameter("planIgualdadValoracion");
                    if (parametro != null && !parametro.equals("")) {
                        planIgualdadValoracion = Long.parseLong(parametro);
                    }
                    parametro = request.getParameter("certCalidad");
                    if (parametro != null && !parametro.equals("")) {
                        certCalidad = Integer.parseInt(parametro);
                    }
                    parametro = request.getParameter("cerCalidadVal");
                    if (parametro != null && !parametro.equals("")) {
                        certCalidadVal = Integer.parseInt(parametro);
                    }
                    parametro = request.getParameter("certCalidadValoracion");
                    if (parametro != null && !parametro.equals("")) {
                        certCalidadValoracion = Long.parseLong(parametro);
                    }
                }

                ejercicio = MeLanbide47Utils.getEjercicioDeExpediente(numExpediente);
            } catch (NumberFormatException ex) {
                codigoOperacion = "3";
            }

            if (idUbic == null) {
                codigoOperacion = "3";
            } else {
                AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));

                //Creo una entidad de ejemplo con los datos de la request
                ent = new EntidadVO();
                ent.setOriEntCod(codEntidad);
                ent.setExtNum(numExpediente);
                ent.setExtEje(ejercicio);
                ent.setExtMun(codOrganizacion);
               

                //Traigo la ubicacion a valorar y la modifico con los datos de la request
                ubic = new OriUbicVO();
                ubic.setOriEntCod(codEntidad);
                ubic.setOriOrientUbicCod(idUbic);

                ubic = MeLanbide47Manager.getInstance().getUbicacionORIPorCodigo(ubic, nuevaCon, adapt);

                if (ubic != null) {
                    ubic.setNumExp(numExpediente);
                    ubic.setOriOrientDespachosValidados(despachos);
                    ubic.setOriOrientAulaGrupalValidada(aulaGrupal != null ? aulaGrupal : ConstantesMeLanbide47.NO);
                    ubic.setOriOrientValTray(trayVal);
                    ubic.setOriOrientValUbic(ubicVal);
                    ubic.setOriOrientValDespachos(despVal);
                    ubic.setOriOrientValAulas(aulaVal);
                    ubic.setOriOrientPuntuacion(puntuacion);
                    ubic.setOriOrientObservaciones(observaciones);
                    ubic.setOriOrientUbicaEspacioAdicionalVal(disp1EspaAdicional);
                    ubic.setOriOrientUbicaEspAdicioHerrBusqEmpleoVal(dispEspaOrdeIntWifi);
                    ubic.setOriOrientVal1EspacioAdicional(disp1EspaAdicionalValor);
                    ubic.setOriOrientValEspAdicioHerrBusqEmpleo(dispEspaOrdeIntWifiValor);
                    ubic.setOriCELocalPreviamenteAprobadoVAL(localPreviamenteAprobadoVAL);
                    ubic.setOriCEMantenimientoRequisitosLPAVAL(mantenimientoRequisitosLPAVAL);
                    ubic.setOriCELocalPreviamenteAprobadoValoracion(localPreviamenteAprobadoValoracion);
                    ubic.setOriCEMantenimientoRequisitosLPAValoracion(mantenimientoRequisitosLPAValoracion);
                    ubic.setOriPlanIgualdadValoracion(planIgualdadValoracion);
                    ubic.setOriCertCalidadValoracion(certCalidadValoracion);
                    if (nuevaCon) {
                        ent.setPlanIgualdad(planIgualdad);
                        ent.setCertificadoCalidad(certCalidad);
                        ent.setPlanIgualdadVal(planIgualdadVal);
                        ent.setCertificadoCalidadVal(certCalidadVal);
                    }
                    ubic = MeLanbide47Manager.getInstance().guardarUbicacion(ent, ubic, nuevaCon, idioma, adapt);
                   
                    if (ubic != null) {
                        //Si ubic != null entonces es que se ha guardado bien
                        try {
                            ambitos = MeLanbide47Manager.getInstance().getUbicacionesORI(ent, nuevaCon, adapt);
                        } catch (BDException bde) {
                            java.util.logging.Logger.getLogger(MELANBIDE47.class.getName()).log(Level.SEVERE, null, bde);
                        } catch (Exception ex) {
                            java.util.logging.Logger.getLogger(MELANBIDE47.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        codigoOperacion = "2";
                    }
                } else {
                    codigoOperacion = "3";
                }
            }
        } catch (Exception ex) {
            codigoOperacion = "2";
        }

        escribirListaAmbitosRequest(codigoOperacion, ent, ambitos, response);
    }

    public void getTrayectoriaORI(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        Map<Long, OriTrayectoriaVO> mapaTrayectorias = new HashMap<Long, OriTrayectoriaVO>();
        List<AsociacionVO> listaAsociaciones = new ArrayList<AsociacionVO>();
        String codigoOperacion = "0";
        try
        {
            String fromValidacion = (String) request.getParameter("fromValidacion");
            boolean sonDatosValidados = fromValidacion != null && fromValidacion.equalsIgnoreCase("1") ? true : false;
            
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            EntidadVO ent = null;
            ent = MeLanbide47Manager.getInstance().getEntidad(codOrganizacion, numExpediente, MeLanbide47Utils.getEjercicioDeExpediente(numExpediente), adapt);
            if(ent != null)
            {
                List<FilaAsociacionVO> asociaciones = MeLanbide47Manager.getInstance().getListaAsociacionesPorEntidad(ent, adapt);
                OriTrayectoriaVO trayEjemplo = null;
                AsociacionVO asoc = null;
                for(FilaAsociacionVO fila : asociaciones)
                {
                    asoc = new AsociacionVO();
                    asoc.setOriAsocCod(fila.getCodAsociacion());
                    asoc.setOriEntCod(ent.getOriEntCod());
                    asoc = MeLanbide47Manager.getInstance().getAsociacionPorCodigoYEntidad(asoc, adapt);
                    if(asoc != null)
                    {
                        listaAsociaciones.add(asoc);
                        
                        trayEjemplo = new OriTrayectoriaVO();
                        trayEjemplo.setNumExp(numExpediente);
                        trayEjemplo.setOriAsocCod(fila.getCodAsociacion());
                        trayEjemplo = MeLanbide47Manager.getInstance().getTrayectoriaPorAsociacionYExpediente(trayEjemplo, adapt,sonDatosValidados);
                        if(trayEjemplo != null)
                        {
                            mapaTrayectorias.put(trayEjemplo.getOriAsocCod(), trayEjemplo);
                        }
                    }
                    else
                    {
                        codigoOperacion = "3";
                    }
                }
            }
            else
            {
                codigoOperacion = "1";
            }
        }
        catch(Exception ex)
        {
            log.error(ex.getStackTrace());
            codigoOperacion = "1";
        }
        
        escribirTrayectoriaORIRequest(codigoOperacion, listaAsociaciones, mapaTrayectorias, response);
    }
    
    public void guardarTrayectoriaORI(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
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
                //Para Saber si venimos de la jsp de validacion
                String fromValidacion = (String)request.getParameter("fromValidacion");
                boolean sonDatosValidados = fromValidacion!=null && fromValidacion.equalsIgnoreCase("1")?true:false;
                
                Integer numAsociaciones = Integer.parseInt(request.getParameter("numAsociaciones"));
                
                OriTrayectoriaVO tray = null;
                String parametro = null;
                
                List<OriTrayectoriaVO> trayectorias = new ArrayList<OriTrayectoriaVO>();
                
                for(int i = 0; i < numAsociaciones; i++)
                {
                    tray = new OriTrayectoriaVO();
                    tray.setNumExp(numExpediente);
                    tray.setOriAsocCod(Long.parseLong(request.getParameter("codigoAsoc_"+i)));
                    tray = MeLanbide47Manager.getInstance().getTrayectoriaPorAsociacionYExpediente(tray, adapt,sonDatosValidados);
                    
                    if(tray == null)
                    {
                        tray = new OriTrayectoriaVO();
                        tray.setNumExp(numExpediente);
                        tray.setOriAsocCod(Long.parseLong(request.getParameter("codigoAsoc_"+i)));
                    }
                    
                    
                    //dec327
                    parametro = request.getParameter("rad_dec327_"+i);
                    tray.setDec327(parametro != null && !parametro.isEmpty() ? Integer.parseInt(parametro) : null);
                    parametro = request.getParameter("chk_dec327_2007_"+i);
                    tray.setDec327_2007(parametro != null && !parametro.equals("") ? Integer.parseInt(parametro) : null);
                    parametro = request.getParameter("chk_dec327_2008_"+i);
                    tray.setDec327_2008(parametro != null && !parametro.equals("") ? Integer.parseInt(parametro) : null);
                    parametro = request.getParameter("chk_dec327_2009_"+i);
                    tray.setDec327_2009(parametro != null && !parametro.equals("") ? Integer.parseInt(parametro) : null);
                    parametro = request.getParameter("chk_dec327_2010_"+i);
                    tray.setDec327_2010(parametro != null && !parametro.equals("") ? Integer.parseInt(parametro) : null);
                    
                    //min94
                    parametro = request.getParameter("rad_min94_"+i);
                    tray.setMin94(parametro != null && !parametro.isEmpty() ? Integer.parseInt(parametro) : null);
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
                    parametro = request.getParameter("rad_min98_"+i);
                    tray.setMin98(parametro != null && !parametro.isEmpty() ? Integer.parseInt(parametro) : null);
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
                    parametro = request.getParameter("rad_tas03_"+i);
                    tray.setTas03(parametro != null && !parametro.isEmpty() ? Integer.parseInt(parametro) : null);
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
                    
                    //lan:2015
                    parametro = request.getParameter("chk_lan_2015_"+i);
                    tray.setLan_2015(parametro != null && !parametro.equals("") ? Integer.parseInt(parametro) : null);
                    //lan:2017
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
                    boolean resultado = MeLanbide47Manager.getInstance().guardarTrayectoriaAsociaciones(trayectorias, adapt,sonDatosValidados);
                    
                    if(resultado == false)
                    {
                        codigoOperacion = "4";
                    }
                }
                catch(Exception ex)
                {
                    log.error("Error : "+ ex.getMessage(), ex);
                    codigoOperacion = "4";
                }
            }
            catch(Exception ex)
            {
                log.error("Error : "+ ex.getMessage(), ex);
                codigoOperacion = "3";
            }
        }
        catch(Exception ex)
        {
            log.error("Error : "+ ex.getMessage(), ex);
            codigoOperacion = "2";
        }
        escribirResultadoGuardarTrayectoriaORIRequest(codigoOperacion, response);
    }
    
    public void cargarAmbitosHorasPorProvincia(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        String codProvincia = (String)request.getParameter("codProvincia");
        String anoConv = "";
        try
        {
            String[] datos = numExpediente.split(ConstantesMeLanbide47.BARRA_SEPARADORA);
            anoConv = datos[0];
        }
        catch(Exception ex)
        {
            log.debug("No se ha podido obtener el ejercicio al que pertenece el expediente");
        }
        List<SelectItem> listaAmbitos = new ArrayList<SelectItem>();
        try
        {
            listaAmbitos = MeLanbide47Manager.getInstance().getAmbitosHorasPorProvincia(codProvincia, anoConv, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        }
        catch(Exception ex)
        {
            
        }
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
            xmlSalida.append("<CODIGO_OPERACION>");
                xmlSalida.append("0");
            xmlSalida.append("</CODIGO_OPERACION>");
            for(SelectItem si : listaAmbitos)
            {
                xmlSalida.append("<ITEM_AMBITO>");
                    xmlSalida.append("<ID>");
                        xmlSalida.append(si.getId());
                    xmlSalida.append("</ID>");
                    xmlSalida.append("<LABEL>");
                        xmlSalida.append(si.getLabel());
                    xmlSalida.append("</LABEL>");
                xmlSalida.append("</ITEM_AMBITO>");
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
            log.error("Error : "+ e.getMessage(), e);
        }//try-catch
    }
    
    public void cargarMunicipiosPorAmbitoProvinciaHoras(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        List<SelectItem> listaMunicipios = new ArrayList<SelectItem>();
        try
        {
            String codProvincia = (String)request.getParameter("codProvincia");
            String codAmbito = (String)request.getParameter("codAmbito");
            
            String[] datos = numExpediente.split(ConstantesMeLanbide47.BARRA_SEPARADORA);
            String ejercicio = datos[0];
            Integer ano = Integer.parseInt(ejercicio);
            
            listaMunicipios = MeLanbide47Manager.getInstance().getMunicipiosPorAmbitoProvinciaHoras(codProvincia, Integer.parseInt(codAmbito), ano, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        }
        catch(Exception ex)
        {
            
        }
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
            xmlSalida.append("<CODIGO_OPERACION>");
                xmlSalida.append("0");
            xmlSalida.append("</CODIGO_OPERACION>");
            for(SelectItem si : listaMunicipios)
            {
                xmlSalida.append("<ITEM_MUNICIPIO>");
                    xmlSalida.append("<ID>");
                        xmlSalida.append(si.getId());
                    xmlSalida.append("</ID>");
                    xmlSalida.append("<LABEL>");
                        xmlSalida.append(si.getLabel());
                    xmlSalida.append("</LABEL>");
                    xmlSalida.append("<PRV>");
                        xmlSalida.append(si.getCodPrv());
                    xmlSalida.append("</PRV>");
                xmlSalida.append("</ITEM_MUNICIPIO>");
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
            log.error("Error : "+ e.getMessage(), e);
        }//try-catch
    }
    
    public void cargarComboProcesos(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
            
        int codIdioma = 1;
        
        UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
        if(usuario != null)
        {
            codIdioma = usuario.getIdioma();
        }
        
        List<SelectItem> listaProcesos = new ArrayList<SelectItem>();
        try
        {
            SelectItem si = null;
            
            si = new SelectItem();
            
            si = new SelectItem();
            si.setId(ConstantesMeLanbide47.COD_PROC_ADJUDICA_HORAS);
            si.setLabel(MeLanbide47Utils.obtenerNombreProceso(ConstantesMeLanbide47.COD_PROC_ADJUDICA_HORAS, codIdioma));
            listaProcesos.add(si);
            
            si = new SelectItem();
            si.setId(ConstantesMeLanbide47.COD_PROC_CONSOLIDA_HORAS);
            si.setLabel(MeLanbide47Utils.obtenerNombreProceso(ConstantesMeLanbide47.COD_PROC_CONSOLIDA_HORAS, codIdioma));
            listaProcesos.add(si);
            
            si = new SelectItem();
            si.setId(ConstantesMeLanbide47.COD_PROC_DESHACER_CONSOLIDACION_HORAS);
            si.setLabel(MeLanbide47Utils.obtenerNombreProceso(ConstantesMeLanbide47.COD_PROC_DESHACER_CONSOLIDACION_HORAS, codIdioma));
            listaProcesos.add(si);
            
            si = new SelectItem();
            si.setId(ConstantesMeLanbide47.COD_PROC_DOCUMENTACION_HORAS);
            si.setLabel(MeLanbide47Utils.obtenerNombreProceso(ConstantesMeLanbide47.COD_PROC_DOCUMENTACION_HORAS, codIdioma));
            listaProcesos.add(si);
        }
        catch(Exception ex)
        {
            
        }
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
            xmlSalida.append("<CODIGO_OPERACION>");
                xmlSalida.append("0");
            xmlSalida.append("</CODIGO_OPERACION>");
            for(SelectItem si : listaProcesos)
            {
                xmlSalida.append("<SELECT_ITEM>");
                    xmlSalida.append("<ID>");
                        xmlSalida.append(si.getId());
                    xmlSalida.append("</ID>");
                    xmlSalida.append("<LABEL>");
                        xmlSalida.append(si.getLabel());
                    xmlSalida.append("</LABEL>");
                xmlSalida.append("</SELECT_ITEM>");
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
            log.error("Error : "+ e.getMessage(), e);
        }//try-catch
    }
    
    public void filtrarAuditoriaProcesos(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        List<FilaAuditoriaProcesosVO> audList = new ArrayList<FilaAuditoriaProcesosVO>();
        List<FilaAuditoriaProcesosVO> tempList = new ArrayList<FilaAuditoriaProcesosVO>();
        try
        {
            String p1 = (String)request.getParameter("pagAct");
            String p2 = (String)request.getParameter("maxFilas");
            
            String nombre = (String)request.getParameter("nomApellidos");
            String p3 = (String)request.getParameter("feDesde");
            String p4 = (String)request.getParameter("feHasta");
            String p5 = (String)request.getParameter("codProc");
            
            String codProcedimiento = request.getParameter("codProcedimiento");
            
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            
            Date feDesde = null;
            if(p3 != null && !p3.equals(""))
            {
                try
                {
                    feDesde = format.parse(p3);
                    Calendar cal = new GregorianCalendar();
                    cal.setTime(feDesde);
                    cal.set(Calendar.HOUR, 0);
                    cal.set(Calendar.MINUTE, 0);
                    cal.set(Calendar.SECOND, 0);
                    feDesde = cal.getTime();
                }
                catch(Exception ex)
                {
                    
                }
            }
            Date feHasta = null;
            if(p4 != null && !p4.equals(""))
            {
                try
                {
                    feHasta = format.parse(p4);
                    Calendar cal = new GregorianCalendar();
                    cal.setTime(feHasta);
                    cal.set(Calendar.HOUR, 23);
                    cal.set(Calendar.MINUTE, 59);
                    cal.set(Calendar.SECOND, 59);
                    feHasta = cal.getTime();
                }
                catch(Exception ex)
                {
                    
                }
            }
            
            Integer codProceso = null;
            if(p5 != null && !p5.equals(""))
            {
                codProceso = Integer.parseInt(p5);
            }
            
            Integer act = null;
            Integer max = null;
            try
            {
                act = Integer.parseInt(p1);
                max = Integer.parseInt(p2);
            }
            catch(Exception ex)
            {
                act = 0;
                max = 0;
            }
            
            int codIdioma = 1;
        
            UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
            if(usuario != null)
            {
                codIdioma = usuario.getIdioma();
            }
            
            tempList = MeLanbide47Manager.getInstance().filtrarAuditoriaProcesos(nombre, feDesde, feHasta, codProceso, codProcedimiento, codIdioma, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            
            int desde = act-1;
            desde = desde*max;
            if(desde < 0)
            {
                desde = 0;
            }
            int hasta = desde + max;
            if(hasta > tempList.size())
            {
                hasta = tempList.size();
            }
            
            for(int i = desde; i < hasta; i++)
            {
                audList.add(tempList.get(i));
            }
        }
        catch(Exception ex)
        {
            
        }
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
            xmlSalida.append("<CODIGO_OPERACION>");
                xmlSalida.append("0");
            xmlSalida.append("</CODIGO_OPERACION>");
            xmlSalida.append("<TOTAL_REGISTROS>");
                xmlSalida.append(tempList.size());
            xmlSalida.append("</TOTAL_REGISTROS>");
            for(FilaAuditoriaProcesosVO fila : audList)
            {
                xmlSalida.append("<REGISTRO>");
                    xmlSalida.append("<NOMAPELLIDOS>");
                        xmlSalida.append(fila.getNomApellidos());
                    xmlSalida.append("</NOMAPELLIDOS>");
                    xmlSalida.append("<PROCESO>");
                        xmlSalida.append(fila.getProceso());
                    xmlSalida.append("</PROCESO>");
                    xmlSalida.append("<FECHA>");
                        xmlSalida.append(fila.getFecha());
                    xmlSalida.append("</FECHA>");
                    xmlSalida.append("<RESULTADO>");
                        xmlSalida.append(fila.getResultado());
                    xmlSalida.append("</RESULTADO>");
                xmlSalida.append("</REGISTRO>");
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
            log.error("Error : "+ e.getMessage(), e);
        }//try-catch
    }
    
    /**
     * Operaciï¿½n que recupera las listas para los combos y los guarda en la request
     * @param request
     */
    private void cargarCombosNuevoAmbitoORI(int codOrganizacion, HttpServletRequest request)
    {   
        List<SelectItem> listaProvincias = new ArrayList<SelectItem>();
        
        //Combo TERRITORIOHISTORICO
        try
        {
            String provinciasCargar = ConfigurationParameter.getParameter("ori.nuevaUbicacion.listaProvincias", ConstantesMeLanbide47.FICHERO_CONF_PANTALLAS);
            String[] codigosProv = provinciasCargar.split(ConstantesMeLanbide47.SEPARADOR_VALORES_CONF);
            listaProvincias = MeLanbide47Manager.getInstance().getListaProvincias(ConstantesMeLanbide47.CODIGO_PAIS_ESPANA, Arrays.asList(codigosProv), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        }
        catch(Exception ex)
        {
            
        }
        request.setAttribute("listaProvincias", listaProvincias);
    }
    
    /**
     * Operaciï¿½n que recupera los datos de conexiï¿½n a la BBDD
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
        String[] salida = null;
        Connection con = null;
        
        if(log.isDebugEnabled()){
            log.debug("getJndi =========> ");
            log.debug("parametro codOrganizacion: " + codOrganizacion);
            log.debug("gestor: " + gestor);
            log.debug("jndi: " + jndiGenerico);
        }//if(log.isDebugEnabled())

        DataSource ds = null;
        AdaptadorSQLBD adapt = null;
        synchronized (this) {
            try{
                PortableContext pc = PortableContext.getInstance();
                if(log.isDebugEnabled())log.debug("He cogido el jndi: " + jndiGenerico);
                ds = (DataSource)pc.lookup(jndiGenerico, DataSource.class);
                // Conexión al esquema genérico
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
                    salida = new String[7];
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
                log.error("Error : "+ te.getMessage(), te);
                log.error("*** AdaptadorSQLBD: " + te.toString());
            }catch(SQLException e){
                log.error("Error : "+ e.getMessage(), e);
            }finally{
                try{
                    if(st!=null) st.close();
                    if(rs!=null) rs.close();
                    if(conGenerico!=null && !conGenerico.isClosed())
                    conGenerico.close();
                }catch(SQLException e){
                    log.error("Error : "+ e.getMessage(), e);
                }//try-catch
            }// finally
            if(log.isDebugEnabled()) log.debug("getConnection() : END");
         }// synchronized
        return adapt;
     }//getConnection
    
    public void adjudicaOrientacion(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
        int codigoOperacion = 0;
        String[] result = null;
        String mensaje = "";
        String estado = "";
        int codIdioma = 1;
        try
        {
            UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
            if(usuario != null)
            {
                codIdioma = usuario.getIdioma();
            }
        }
        catch(Exception ex)
        {
            
        }
        if(request.getParameter("ano") != null)
        {
            try
            {
                int ano = Integer.parseInt(request.getParameter("ano"));
                try
                {
                    result = MeLanbide47Manager.getInstance().adjudicaOrientacion(ano, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    mensaje = result[0];
                    if(mensaje != null)
                    {
                        if(mensaje.equalsIgnoreCase(ConstantesMeLanbide47.OK))
                        {
                            codigoOperacion = 0;
                        }
                        else if(mensaje.equalsIgnoreCase(ConstantesMeLanbide47.ERROR))
                        {
                            codigoOperacion = 1;
                        }
                        else
                        {
                            mensaje = procesarMensajeProcedimiento(mensaje, codIdioma);
                            codigoOperacion = 4;
                        }
                    }
                    else
                    {
                        codigoOperacion = 1;
                    }
                    
                    if(result[1] != null && result[1].equalsIgnoreCase("1"))
                    {
                        estado = MeLanbide47I18n.getInstance().getMensaje(codIdioma, "expedientes_en_estados_no_permitidos");
                    }
                    
                    //Creamos la auditorï¿½a
                    String codProcedimiento = null;
                    try
                    {
                        codProcedimiento = (String)request.getParameter("codProcedimiento");
                    }
                    catch(Exception ex)
                    {
                        
                    }
                    this.crearAuditoria(codOrganizacion, (UsuarioValueObject)request.getSession().getAttribute("usuario"), ConstantesMeLanbide47.COD_PROC_ADJUDICA_HORAS, mensaje, codProcedimiento);
                }
                catch(Exception ex)
                {
                    codigoOperacion = 1;
                }
            }
            catch(Exception ex)
            {
                codigoOperacion = 2;
            }
        }
        else
        {
            codigoOperacion = 3;
        }
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
            xmlSalida.append("<CODIGO_OPERACION>");
                xmlSalida.append(codigoOperacion);
            xmlSalida.append("</CODIGO_OPERACION>");
            xmlSalida.append("<MENSAJE>");
                xmlSalida.append(mensaje);
            xmlSalida.append("</MENSAJE>");
            if(estado != null && !estado.equals(""))
            {
                xmlSalida.append("<ESTADO>");
                    xmlSalida.append(estado);
                xmlSalida.append("</ESTADO>");
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
            log.error("Error : "+ e.getMessage(), e);
        }//try-catch
    }
    
    public void crearDocumentacionResolucionHoras(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
        log.debug("crearDocumentacionResolucionHoras");        
        String codigoOperacion = "0";
        String rutaArchivoSalida = null;
        try
        {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            
            HttpSession session = request.getSession();
            UsuarioValueObject usuario = new UsuarioValueObject();
            int idioma = MeLanbide47Utils.getIdiomaUsuarioFromRequest(request);

            try
            {
                if (session != null) 
                {
                    usuario = (UsuarioValueObject) session.getAttribute("usuario");
                    if (usuario != null) 
                    {
                        idioma = usuario.getIdioma();
                    }
                }
            }
            catch(Exception ex)
            {

            }
            
            Integer ano = new GregorianCalendar().get(Calendar.YEAR);
            try
            {
                ano = Integer.parseInt((String)request.getParameter("ano"));
            }
            catch(Exception ex)
            {
                
            }
            
            MeLanbide47I18n meLanbide47I18n = MeLanbide47I18n.getInstance();
            
            List<Integer> provincias = MeLanbide47Manager.getInstance().getDistintasProvDeAmbitosHoras(adapt);
            HSSFWorkbook libro = new HSSFWorkbook();
            HSSFFont negrita = libro.createFont();
            negrita.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            ProvinciaVO prov = null;
            HSSFSheet hoja = null;
            List<AmbitosHorasVO> ambitosProv = null;
            int numFila = 0;
            List<OriUbicVO> ubicacionesAmbito = null;
            int totalesSolicitadas = 0;
            double totalesAdjudicadas = 0.0;
            double puntuacionTotal = 0.0;
            EntidadVO entidad = null;
            Long entCodAnt = null;
            for(Integer codProv : provincias)
            {
                prov = MeLanbide47Manager.getInstance().getProvinciaPorCodigo(codProv, adapt);
                if(prov != null && prov.getPrvNom() != null)
                {
                    String nombreProvincia = prov.getPrvNom().toUpperCase();
                    nombreProvincia = nombreProvincia.replaceAll("/", "_");
                    hoja = libro.createSheet(nombreProvincia);
                    
                    hoja.setColumnWidth(0, 6000);//num exp
                    hoja.setColumnWidth(1, 10000);//nombre ent
                    hoja.setColumnWidth(2, 10000);//direccion
                    hoja.setColumnWidth(3, 10000);//municipio
                    hoja.setColumnWidth(4, 3000);//cod postal
                    hoja.setColumnWidth(5, 3000);//horas solic
                    hoja.setColumnWidth(6, 3000);//trayectoria
                    hoja.setColumnWidth(7, 5000);//punt ubic
                    // Desde 2017 debe leerse datos de Disposnibildad 1 espacio adicional y espacio con herramienta busqueda de empleo wifi, ordenadores, interbet
                    hoja.setColumnWidth(8, 3000);//despacho
                    hoja.setColumnWidth(9, 3000);//2 aula
                    hoja.setColumnWidth(10, 3000);//Plan Igualdad
                    hoja.setColumnWidth(11, 3000);//Certificado Calidad
                    hoja.setColumnWidth(12, 3000);//punt total
                    hoja.setColumnWidth(13, 3000);//horas adj
                    // 2017/11/20 anadimos comprobaciones si está en Resolucion Provisinal
                    hoja.setColumnWidth(14, 3000);//Tramite resolucion provisional?
                    hoja.setColumnWidth(15, 6000);//Fecha Registrp
                    
                    ambitosProv = MeLanbide47Manager.getInstance().getAmbitosHorasPorProvincia(codProv, ano, adapt);
                    for(AmbitosHorasVO amb : ambitosProv)
                    {
                        if(amb.getOriAmbCod() == 2)
                            System.out.println();
                        //Ambito
                        anadirAmbitoResolucionHoras(libro, hoja, amb, numFila, meLanbide47I18n, idioma);
                        //HORAS
                        numFila = numFila + 2;
                        anadirHorasAmbitoResolucionHoras(libro, hoja, amb, numFila, meLanbide47I18n, idioma);
                        
                        //Cabecera 1 Tabla
                        numFila = numFila + 3;
                        anadirCabecera1ResolucionHoras(libro, hoja, numFila, meLanbide47I18n, idioma);
                        
                        //Cabecera 2 tabla
                        numFila = numFila + 1;
                        anadirCabecera2ResolucionHoras(libro, hoja, numFila, meLanbide47I18n, 4, ano);
                        numFila = numFila + 1;
                        anadirCabecera2ResolucionHoras(libro, hoja, numFila, meLanbide47I18n, 1, ano);

                        //Entidades
                        try
                        {
                            ubicacionesAmbito = MeLanbide47Manager.getInstance().getUbicacionesDeAmbitoORIDocumResolProvisional(amb, adapt);
                            // 2017/12/19 Comentamos, este orden los esablecemos en la sql del metodo getUbicacionesDeAmbitoORIDocumResolProvisional
                            //ubicacionesAmbito = this.ubicacionesQueNoEstanEnResProvAlFinal(ubicacionesAmbito, adapt);
                            int numFilaInicial = numFila + 1;
                            for(OriUbicVO ubic : ubicacionesAmbito)
                            {
                                    numFila = numFila + 1;

                                    if(entCodAnt == null)
                                    {
                                        entCodAnt = ubic.getOriEntCod();
                                    }
                                    else if(ubic.getOriEntCod() != null)
                                    {
                                        if(entCodAnt.longValue() == ubic.getOriEntCod().longValue())
                                        {
                                            ubic.setOriOrientPuntuacion(new BigDecimal("0"));
                                        }
                                        entCodAnt = ubic.getOriEntCod();
                                    }
                                    entidad = new EntidadVO();
                                    entidad.setOriEntCod(ubic.getOriEntCod());
                                    entidad.setExtNum(ubic.getNumExp());
                                    entidad = MeLanbide47Manager.getInstance().getEntidadPorCodigoYExpediente(entidad, idioma, adapt);
                                    if(entidad != null)
                                    {
                                        double d = anadirEntidadResolucionHoras(libro, hoja, ubic, entidad, numFila, adapt);
                                        Boolean estaTramiteResolProv=MeLanbide47Manager.getInstance().expteEstaTramiteResolProvisional(ubic.getNumExp(),adapt);
                                        //totalesAdjudicadas = totalesAdjudicadas + (ubic.getOriOrientHorasadj() != null ? ubic.getOriOrientHorasadj().doubleValue() : 0);
                                        totalesAdjudicadas = totalesAdjudicadas + d;
                                        // 2017/12/15 Evitamos que sume Las ubicaciones de expedientes Fuera de Tramite resolucion Provisional
                                        if(estaTramiteResolProv){
                                            totalesSolicitadas = totalesSolicitadas + (ubic.getOriOrientHorasSolicitadas() != null ? ubic.getOriOrientHorasSolicitadas().intValue() : 0);
                                            puntuacionTotal = puntuacionTotal + (ubic.getOriOrientPuntuacion() != null ? ubic.getOriOrientPuntuacion().doubleValue() : 0);
                                        }
                                    }
                                }
                            
                            corregirPuntuacionesEnInforme(libro, hoja, ubicacionesAmbito, entidad, numFilaInicial, adapt);
                        }
                        catch(Exception ex)
                        {
                            log.error("Error : "+ ex.getMessage(), ex);
                        }
                        
                        //Totales
                        numFila = numFila + 1;
                        anadirFilaTotalesResolucionHoras(libro, hoja, numFila, totalesSolicitadas, puntuacionTotal, totalesAdjudicadas, meLanbide47I18n, idioma);
                        numFila = numFila + 4;
                        totalesAdjudicadas = 0.0;
                        totalesSolicitadas = 0;
                        puntuacionTotal = 0;
                        // 2017/12/21 Hay que resetear el codigo de entidad enterior. Al cambiar de Ambito
                        entCodAnt=null;
                    } // For Ambito
                }
                numFila = 0;
            }
            
            try 
            {
                File directorioTemp = new File(m_Conf.getString("PDF.base_dir"));
                File informe = File.createTempFile("resolucionHoras", ".xls", directorioTemp);

                FileOutputStream archivoSalida = new FileOutputStream(informe);
                libro.write(archivoSalida);
                archivoSalida.close();

                rutaArchivoSalida = informe.getAbsolutePath();
                
                FileInputStream istr = new FileInputStream(rutaArchivoSalida);

                BufferedInputStream bstr = new BufferedInputStream( istr ); // promote

                int size = (int) informe.length(); 
                byte[] data = new byte[size]; 
                bstr.read( data, 0, size ); 
                bstr.close();

                response.setContentType("application/vnd.ms-excel");
                response.setHeader("Content-Disposition", "inline; filename=" + informe.getName());
                response.setHeader("Content-Transfer-Encoding", "binary");  
                response.setContentLength(data.length);
                response.getOutputStream().write(data, 0, data.length);
                response.getOutputStream().flush();
                response.getOutputStream().close();
    
                //Creamos la auditorï¿½a
                String codProcedimiento = null;
                try
                {
                        codProcedimiento = (String)request.getParameter("codProcedimiento");
                }
                catch(Exception ex)
                {

                }
                this.crearAuditoria(codOrganizacion, (UsuarioValueObject)request.getSession().getAttribute("usuario"), ConstantesMeLanbide47.COD_PROC_DOCUMENTACION_HORAS, null, codProcedimiento);
            } 
            catch (Exception ioe) 
            {
                log.error("Error Catch1 al crearDocumentacionResolucionHoras()", ioe);
            }
        }
        catch(FileNotFoundException fil){
            log.error("Error FileNotFoundException  al crearDocumentacionResolucionHoras()", fil);
        }
        catch(Exception ex)
        {
            log.error("Error general  al crearDocumentacionResolucionHoras()", ex);
        }
    }
    
    private void anadirAmbitoResolucionHoras(HSSFWorkbook libro, HSSFSheet hoja, AmbitosHorasVO amb, int numFila, MeLanbide47I18n meLanbide47I18n, int idioma)
    {
        HSSFRow fila = hoja.createRow(numFila);

        HSSFCellStyle estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);  
        estiloCelda.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        HSSFFont negrita = libro.createFont();
        negrita.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        estiloCelda.setFont(negrita);
        estiloCelda.setWrapText(true);
        HSSFCell celda = fila.createCell(0);
        celda.setCellValue(meLanbide47I18n.getMensaje(idioma, "label.doc.resolucionHoras.ambito"));
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(1);
        celda.setCellValue(amb.getOriAmbAmbito() != null ? amb.getOriAmbAmbito() : "");
        celda.setCellStyle(estiloCelda);
    }
    
    private void anadirHorasAmbitoResolucionHoras(HSSFWorkbook libro, HSSFSheet hoja, AmbitosHorasVO amb, int numFila, MeLanbide47I18n meLanbide47I18n, int idioma)
    {
        HSSFRow fila = hoja.createRow(numFila);
                        
        HSSFCell celda = fila.createCell(0);
        HSSFCellStyle estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.TAN.index);
        HSSFFont negrita = libro.createFont();
        negrita.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        estiloCelda.setFont(negrita);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setWrapText(true);
        celda.setCellValue(meLanbide47I18n.getMensaje(idioma, "label.doc.resolucionHoras.horas"));
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.TAN.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(1);
        celda.setCellValue(amb.getOriAmbHorasTot() != null ? amb.getOriAmbHorasTot().doubleValue() : 0);
        celda.setCellStyle(estiloCelda);
        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
    }
    
    private void anadirCabecera1ResolucionHoras(HSSFWorkbook libro, HSSFSheet hoja, int numFila, MeLanbide47I18n meLanbide47I18n, int idioma)
    {
        HSSFRow fila = hoja.createRow(numFila);

        //hoja.addMergedRegion(new Region(numFila, (short)0, numFila, (short)4));
        //hoja.addMergedRegion(new Region(numFila, (short)6, numFila, (short)10));
        hoja.addMergedRegion(new CellRangeAddress(numFila, numFila,0,4));
        hoja.addMergedRegion(new CellRangeAddress (numFila,numFila,6,12));

        HSSFCellStyle estiloCelda1 = libro.createCellStyle();
        estiloCelda1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda1.setFillForegroundColor(HSSFColor.CORNFLOWER_BLUE.index);
        estiloCelda1.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda1.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda1.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda1.setWrapText(true);
        
        HSSFCellStyle estiloCelda2 = libro.createCellStyle();
        estiloCelda2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda2.setFillForegroundColor(HSSFColor.CORNFLOWER_BLUE.index);
        estiloCelda2.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda2.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda2.setWrapText(true);
        
        HSSFCellStyle estiloCelda3 = libro.createCellStyle();
        estiloCelda3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda3.setFillForegroundColor(HSSFColor.WHITE.index);
        estiloCelda3.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda3.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda3.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda3.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda3.setWrapText(true);
        
        HSSFCellStyle estiloCelda4 = libro.createCellStyle();
        estiloCelda4.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda4.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
        estiloCelda4.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda4.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda4.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda4.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda4.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda4.setWrapText(true);
        
        HSSFCellStyle estiloCelda5 = libro.createCellStyle();
        estiloCelda5.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda5.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
        estiloCelda5.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda5.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda5.setWrapText(true);
        
        HSSFCell celda = fila.createCell(0);
        celda.setCellValue(meLanbide47I18n.getMensaje(idioma, "label.doc.resolucionHoras.horasOrientacion"));
        celda.setCellStyle(estiloCelda1);
        
        celda = fila.createCell(1);
        celda.setCellValue(meLanbide47I18n.getMensaje(idioma, "label.doc.resolucionHoras.horasOrientacion"));
        celda.setCellStyle(estiloCelda2);

        celda = fila.createCell(2);
        celda.setCellValue(meLanbide47I18n.getMensaje(idioma, "label.doc.resolucionHoras.horasOrientacion"));
        celda.setCellStyle(estiloCelda2);
        
        celda = fila.createCell(3);
        celda.setCellValue(meLanbide47I18n.getMensaje(idioma, "label.doc.resolucionHoras.horasOrientacion"));
        celda.setCellStyle(estiloCelda2);
        
        celda = fila.createCell(4);
        celda.setCellValue(meLanbide47I18n.getMensaje(idioma, "label.doc.resolucionHoras.horasOrientacion"));
        celda.setCellStyle(estiloCelda2);

        celda = fila.createCell(5);
        celda.setCellStyle(estiloCelda3);

        celda = fila.createCell(6);
        celda.setCellValue(meLanbide47I18n.getMensaje(idioma, "label.doc.resolucionHoras.valoracion"));
        celda.setCellStyle(estiloCelda4);

        celda = fila.createCell(7);
        celda.setCellStyle(estiloCelda5);

        celda = fila.createCell(8);
        celda.setCellStyle(estiloCelda5);

        celda = fila.createCell(9);
        celda.setCellStyle(estiloCelda5);

        celda = fila.createCell(10);
        celda.setCellStyle(estiloCelda5);
        
        celda = fila.createCell(11);
        celda.setCellStyle(estiloCelda5);

        celda = fila.createCell(12);
        celda.setCellStyle(estiloCelda5);
        
        celda = fila.createCell(13);
        celda.setCellStyle(estiloCelda3);
        
        celda = fila.createCell(14);
        celda.setCellStyle(estiloCelda3);

        celda = fila.createCell(15);
        celda.setCellStyle(estiloCelda3);
    }
    
    private void anadirCabecera2ResolucionHoras(HSSFWorkbook libro, HSSFSheet hoja, int numFila, MeLanbide47I18n meLanbide47I18n, int idioma, Integer ano)
    {
        HSSFRow fila = hoja.createRow(numFila);
        fila.setHeight((short)800);
        
        //hoja.addMergedRegion(new Region(numFila, (short)0, numFila, (short)1));

        HSSFCellStyle estiloCelda1 = libro.createCellStyle();
        estiloCelda1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda1.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
        estiloCelda1.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda1.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda1.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda1.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda1.setWrapText(true);
        
        HSSFCellStyle estiloCelda2 = libro.createCellStyle();
        estiloCelda2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda2.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
        estiloCelda2.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda2.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda2.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda2.setWrapText(true);
        
        HSSFCellStyle estiloCelda3 = libro.createCellStyle();
        estiloCelda3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda3.setFillForegroundColor(HSSFColor.LIGHT_BLUE.index);
        estiloCelda3.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda3.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda3.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda3.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda3.setWrapText(true);
        
        HSSFCellStyle estiloCelda4 = libro.createCellStyle();
        estiloCelda4.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda4.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        estiloCelda4.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda4.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda4.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda4.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda4.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda4.setWrapText(true);
        
        HSSFCellStyle estiloCelda5 = libro.createCellStyle();
        estiloCelda5.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda5.setFillForegroundColor(HSSFColor.LIME.index);
        estiloCelda5.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda5.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda5.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda5.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda5.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda5.setWrapText(true);
        
        HSSFCellStyle estiloCelda6 = libro.createCellStyle();
        estiloCelda6.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda6.setFillForegroundColor(HSSFColor.WHITE.index);
        estiloCelda6.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda6.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda6.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda6.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda6.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda6.setWrapText(true);
        
        
        HSSFCell celda = fila.createCell(0);
        celda.setCellValue(meLanbide47I18n.getMensaje(idioma, "label.doc.resolucionHoras.numExpediente"));
        celda.setCellStyle(estiloCelda1);
        
        celda = fila.createCell(1);
        celda.setCellValue(meLanbide47I18n.getMensaje(idioma, "label.doc.resolucionHoras.nombreEnt"));
        celda.setCellStyle(estiloCelda2);

        celda = fila.createCell(2);
        celda.setCellValue(meLanbide47I18n.getMensaje(idioma, "label.doc.resolucionHoras.direccion"));
        celda.setCellStyle(estiloCelda2);

        celda = fila.createCell(3);
        celda.setCellValue(meLanbide47I18n.getMensaje(idioma, "label.doc.resolucionHoras.municipio"));
        celda.setCellStyle(estiloCelda2);

        celda = fila.createCell(4);
        celda.setCellValue(meLanbide47I18n.getMensaje(idioma, "label.doc.resolucionHoras.codPostal"));
        celda.setCellStyle(estiloCelda2);
        
        celda = fila.createCell(5);
        celda.setCellValue(meLanbide47I18n.getMensaje(idioma, "label.doc.resolucionHoras.horasSolic"));
        celda.setCellStyle(estiloCelda3);

        celda = fila.createCell(6);
        celda.setCellValue(meLanbide47I18n.getMensaje(idioma, "label.doc.resolucionHoras.trayectoria"));
        celda.setCellStyle(estiloCelda4);

        celda = fila.createCell(7);
        celda.setCellValue(meLanbide47I18n.getMensaje(idioma, "label.doc.resolucionHoras.puntUbicacion"));
        celda.setCellStyle(estiloCelda4);

        celda = fila.createCell(8);
        if(ano!=null && ano>=2017)
            celda.setCellValue(meLanbide47I18n.getMensaje(idioma, "label.doc.resolucionHoras.mas1UbicacionAmbito"));
        else
            celda.setCellValue(meLanbide47I18n.getMensaje(idioma, "label.doc.resolucionHoras.despacho"));
        celda.setCellStyle(estiloCelda4);

        celda = fila.createCell(9);
        if(ano!=null && ano>=2017)
            celda.setCellValue(meLanbide47I18n.getMensaje(idioma, "label.doc.resolucionHoras.espacio.herraBusquedaEmp.wifiInternet"));
        else
            celda.setCellValue(meLanbide47I18n.getMensaje(idioma, "label.doc.resolucionHoras.segundaAula"));
        celda.setCellStyle(estiloCelda4);
        
        celda = fila.createCell(10);
        celda.setCellValue(meLanbide47I18n.getMensaje(idioma, "label.doc.resolucionHoras.plan.igualdad"));
        celda.setCellStyle(estiloCelda4);
        
        celda = fila.createCell(11);
        celda.setCellValue(meLanbide47I18n.getMensaje(idioma, "label.doc.resolucionHoras.certificado.calidad"));
        celda.setCellStyle(estiloCelda4);

        celda = fila.createCell(12);
        celda.setCellValue(meLanbide47I18n.getMensaje(idioma, "label.doc.resolucionHoras.puntTotal"));
        celda.setCellStyle(estiloCelda4);

        celda = fila.createCell(13);
        celda.setCellValue(meLanbide47I18n.getMensaje(idioma, "label.doc.resolucionHoras.horasAdj"));
        celda.setCellStyle(estiloCelda5);
        
        celda = fila.createCell(14);
        celda.setCellValue(meLanbide47I18n.getMensaje(idioma, "label.doc.resolucionHoras.estaTramite.resolucionProvisional"));
        celda.setCellStyle(estiloCelda6);

        celda = fila.createCell(15);
        celda.setCellValue(meLanbide47I18n.getMensaje(idioma, "label.doc.resolucionHoras.fecha.registro"));
        celda.setCellStyle(estiloCelda6);
    }
    
    private double anadirEntidadResolucionHoras(HSSFWorkbook libro, HSSFSheet hoja, OriUbicVO ubic, EntidadVO entidad, int numFila, AdaptadorSQLBD adapt)
    {
        HSSFRow fila = hoja.createRow(numFila);
        
        HSSFCellStyle estiloCelda1 = libro.createCellStyle();
        estiloCelda1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda1.setFillForegroundColor(HSSFColor.WHITE.index);
        estiloCelda1.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda1.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda1.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda1.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda1.setWrapText(true);
        
        HSSFCellStyle estiloCelda2 = libro.createCellStyle();
        estiloCelda2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda2.setFillForegroundColor(HSSFColor.WHITE.index);
        estiloCelda2.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda2.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda2.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda2.setWrapText(true);
        
        HSSFCellStyle estiloCelda3 = libro.createCellStyle();
        estiloCelda3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda3.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
        estiloCelda3.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda3.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda3.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda3.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda3.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        estiloCelda3.setWrapText(true);
        
        HSSFCellStyle estiloCelda4 = libro.createCellStyle();
        estiloCelda4.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda4.setFillForegroundColor(HSSFColor.WHITE.index);
        estiloCelda4.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda4.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda4.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda4.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda4.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        estiloCelda4.setWrapText(true);
        
        HSSFCellStyle estiloCelda5 = libro.createCellStyle();
        estiloCelda5.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda5.setFillForegroundColor(HSSFColor.DARK_YELLOW.index);
        estiloCelda5.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda5.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda5.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda5.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda5.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        estiloCelda5.setWrapText(true);
        
        HSSFCellStyle estiloCelda6 = libro.createCellStyle();
        estiloCelda6.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda6.setFillForegroundColor(HSSFColor.TAN.index);
        estiloCelda6.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda6.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda6.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda6.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda6.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        estiloCelda6.setWrapText(true);
        
        HSSFCell celda = fila.createCell(0);
        celda.setCellValue(ubic.getNumExp() != null && !ubic.getNumExp().equals("") ? ubic.getNumExp() : "-");
        celda.setCellStyle(estiloCelda1);
               
        celda = fila.createCell(1);
        
        String nombreEntidad = "-";
        if(entidad != null && entidad.getOriEntAsociacion() != null && entidad.getOriEntAsociacion() == 1)
        {
            //Si es asociacion, se coge el nombre de la entidad
            nombreEntidad = entidad.getOriEntNom() != null && !entidad.getOriEntNom().equals("") ? entidad.getOriEntNom() : "-";
        }
        else
        {
            //Si no es asociacion, habra un solo registro en la tabla ORI14_ASOCIACION. Se debera coger el nombre de dicho registro
            try
            {
                List<FilaAsociacionVO> asociaciones = MeLanbide47Manager.getInstance().getListaAsociacionesPorEntidad(entidad, adapt);
                if(asociaciones != null && asociaciones.size() > 0)
                {
                    FilaAsociacionVO asoc = asociaciones.get(0);
                    nombreEntidad = asoc.getNombre() != null && !asoc.getNombre().equals("") ? asoc.getNombre() : "-";
                }
            }
            catch(Exception ex)
            {
                log.error("Error : "+ ex.getMessage(), ex);
            }
        }
        celda.setCellValue(nombreEntidad.toUpperCase());
        celda.setCellStyle(estiloCelda2);
        
        celda = fila.createCell(2);
        celda.setCellValue(ubic.getOriOrientDireccion() != null && !ubic.getOriOrientDireccion().equals("") ? ubic.getOriOrientDireccion().toUpperCase() : "-");
        celda.setCellStyle(estiloCelda2);

        celda = fila.createCell(3);
        
        String nomMunicipio = null;
        
        try
        {
            MunicipioVO mun = MeLanbide47Manager.getInstance().getMunicipioPorCodigoYProvincia(ubic.getMunCod(), ubic.getMunPrv(), adapt);
            nomMunicipio = mun != null && mun.getMunNol() != null && !mun.getMunNol().equals("") ? mun.getMunNol().toUpperCase() : "-";
        }
        catch(Exception ex)
        {
             nomMunicipio = "-";
        }
        
        celda.setCellValue(nomMunicipio);
        celda.setCellStyle(estiloCelda2);
        
        celda = fila.createCell(4);
        celda.setCellValue(ubic.getOriOrientCP() != null && !ubic.getOriOrientCP().equals("") ? ubic.getOriOrientCP().toUpperCase() : "-");
        celda.setCellStyle(estiloCelda2);
        
        celda = fila.createCell(5);
        celda.setCellValue(ubic.getOriOrientHorasSolicitadas() != null ? ubic.getOriOrientHorasSolicitadas().doubleValue() : 0);
        celda.setCellStyle(estiloCelda3);
        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
        
        celda = fila.createCell(6);
        celda.setCellValue(ubic.getOriOrientValTray() != null ? ubic.getOriOrientValTray().doubleValue() : 0);
        celda.setCellStyle(estiloCelda4);
        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
        
        celda = fila.createCell(7);
        celda.setCellValue(ubic.getOriOrientValUbic() != null ? ubic.getOriOrientValUbic().doubleValue() : 0);
        celda.setCellStyle(estiloCelda4);
        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
        
        celda = fila.createCell(8);
        if(ubic.getOriOrientAno()!=null && ubic.getOriOrientAno()>=2017)
            celda.setCellValue(ubic.getOriOrientVal1EspacioAdicional()!= null ? ubic.getOriOrientVal1EspacioAdicional().doubleValue() : 0);
        else
            celda.setCellValue(ubic.getOriOrientValDespachos() != null ? ubic.getOriOrientValDespachos().doubleValue() : 0);
        celda.setCellStyle(estiloCelda4);
        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
        
        celda = fila.createCell(9);
        if(ubic.getOriOrientAno()!=null && ubic.getOriOrientAno()>=2017)
            celda.setCellValue(ubic.getOriOrientValEspAdicioHerrBusqEmpleo()!= null ? ubic.getOriOrientValEspAdicioHerrBusqEmpleo().doubleValue() : 0);
        else
            celda.setCellValue(ubic.getOriOrientValAulas() != null ? ubic.getOriOrientValAulas().doubleValue() : 0);
        celda.setCellStyle(estiloCelda4);
        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
        
        celda = fila.createCell(10);
        celda.setCellValue(ubic.getOriPlanIgualdadValoracion() != null ? ubic.getOriPlanIgualdadValoracion().doubleValue() : 0);
        celda.setCellStyle(estiloCelda4);
        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
        
        celda = fila.createCell(11);
        celda.setCellValue(ubic.getOriCertCalidadValoracion()!= null ? ubic.getOriCertCalidadValoracion().doubleValue() : 0);
        celda.setCellStyle(estiloCelda4);
        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
        
        celda = fila.createCell(12);
        celda.setCellValue(ubic.getOriOrientPuntuacion() != null ? ubic.getOriOrientPuntuacion().doubleValue() : 0);
        celda.setCellStyle(estiloCelda5);
        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
        
        celda = fila.createCell(13);
        
        BigDecimal bd = null;
        try
        {
            bd = MeLanbide47Manager.getInstance().getHorasAsignadasUbicacionORI(ubic.getOriOrientUbicCod(), adapt);
        }
        catch(Exception ex)
        {
            
        }
        celda.setCellValue(bd != null ? bd.doubleValue() : 0);
        celda.setCellStyle(estiloCelda6);
        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

        celda = fila.createCell(14);
        Boolean estaResolProvisionalTramite = null;
        try {
            estaResolProvisionalTramite=MeLanbide47Manager.getInstance().expteEstaTramiteResolProvisional(ubic.getNumExp(),adapt);
        } catch (Exception e) {
            log.error("Error al comprobar si el expediente esta en resolucion provisional " + ubic.getNumExp(), e);
        }
        String valorCompruebaTramite = "";
        if(estaResolProvisionalTramite!=null){
            if(estaResolProvisionalTramite)
                valorCompruebaTramite="S";
            else
                valorCompruebaTramite="N";
        }
        celda.setCellValue(valorCompruebaTramite);
        celda.setCellStyle(estiloCelda2);
        
        celda = fila.createCell(15);
        String  fechaRegistro = "";
        try {
            String  codPro = MeLanbide47Utils.getCodProcedimientoDeExpediente(ubic.getNumExp());
            fechaRegistro=MeLanbide47Manager.getInstance().getFechaRegistroExpediente(ubic.getNumExp(),codPro,adapt);
        } catch (Exception e) {
            log.error("Error al comprobar si el expediente esta en resolucion provisional " + ubic.getNumExp(), e);
        }
        celda.setCellValue(fechaRegistro);
        celda.setCellStyle(estiloCelda2);
        
        return bd != null ? bd.doubleValue() : 0.0;
    }
    
    private void corregirPuntuacionesEnInforme(HSSFWorkbook libro, HSSFSheet hoja, List<OriUbicVO> ubicacionesAmbito, EntidadVO entidad, int filaDesde, AdaptadorSQLBD adapt)
    {
        if(ubicacionesAmbito != null && ubicacionesAmbito.size() > 0)
        {
            BigDecimal maxTray = new BigDecimal("0.0");
            BigDecimal maxUbic = new BigDecimal("0.0");
            // Tenemos en cuenta los nuevos campos a aprtir de 2017
            Long maxDespachos_Mas1UbicaMismoAmbi = 0L;
            Long maxAulas_EspaCompBusqTraOrdWifi = 0L;
            Long maxPlanIgualdad = 0L;
            Long maxCertificadoCalidad = 0L;
            BigDecimal maxBloquesSolicitados = new BigDecimal("0.0");
            BigDecimal maxPunt = new BigDecimal("0.0");
            String expAct = "";
            String expAnt = "";
            
            int cont = 0;
            int filaHasta = filaDesde;
            
            HSSFCellStyle estiloCelda_WHITE = libro.createCellStyle();
            estiloCelda_WHITE.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda_WHITE.setFillForegroundColor(HSSFColor.WHITE.index);
            estiloCelda_WHITE.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            estiloCelda_WHITE.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda_WHITE.setBorderRight(HSSFCellStyle.BORDER_THICK);
            estiloCelda_WHITE.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            estiloCelda_WHITE.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda_WHITE.setWrapText(true);

            HSSFCellStyle estiloCelda_DARK_YELLOW = libro.createCellStyle();
            estiloCelda_DARK_YELLOW.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda_DARK_YELLOW.setFillForegroundColor(HSSFColor.DARK_YELLOW.index);
            estiloCelda_DARK_YELLOW.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            estiloCelda_DARK_YELLOW.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda_DARK_YELLOW.setBorderRight(HSSFCellStyle.BORDER_THICK);
            estiloCelda_DARK_YELLOW.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            estiloCelda_DARK_YELLOW.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda_DARK_YELLOW.setWrapText(true);

            for(OriUbicVO ubic : ubicacionesAmbito)
            {
                if(ubic.getNumExp() != null && !ubic.getNumExp().equals(""))
                {
                    expAct = ubic.getNumExp();
                    if(expAnt.equals(""))
                    {
                        expAnt = ubic.getNumExp();
                    }
                    
                    if(!expAct.equals(expAnt))
                    {
                        filaHasta = filaDesde + cont - 1;
                        
                        
                        HSSFRow fila = hoja.getRow(filaDesde);
                        
                        hoja.addMergedRegion(new CellRangeAddress(filaDesde, filaHasta, 5, 5));
                        hoja.addMergedRegion(new CellRangeAddress(filaDesde, filaHasta, 6, 6));
                        hoja.addMergedRegion(new CellRangeAddress(filaDesde, filaHasta, 7, 7));
                        hoja.addMergedRegion(new CellRangeAddress(filaDesde, filaHasta, 8, 8));
                        hoja.addMergedRegion(new CellRangeAddress(filaDesde, filaHasta, 9, 9));
                        hoja.addMergedRegion(new CellRangeAddress(filaDesde, filaHasta, 10, 10));
                        hoja.addMergedRegion(new CellRangeAddress(filaDesde, filaHasta, 11, 11));
                        hoja.addMergedRegion(new CellRangeAddress(filaDesde, filaHasta, 12, 12));
                        
                        HSSFCell celda = fila.createCell(5);
                        celda.setCellValue(maxBloquesSolicitados.doubleValue());
                        celda.setCellStyle(estiloCelda_WHITE);
                        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

                        celda = fila.createCell(6);
                        celda.setCellValue(maxTray.doubleValue());
                        celda.setCellStyle(estiloCelda_WHITE);
                        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

                        celda = fila.createCell(7);
                        celda.setCellValue(maxUbic.doubleValue());
                        celda.setCellStyle(estiloCelda_WHITE);
                        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

                        celda = fila.createCell(8);
                        // Coinciden el maximo de los dos campos Antiguo y nuevo 4 / 4 
                        celda.setCellValue(maxDespachos_Mas1UbicaMismoAmbi);
                        celda.setCellStyle(estiloCelda_WHITE);
                        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

                        celda = fila.createCell(9);
                        // Coinciden el maximo de los dos campos Antiguo y nuevo 1 / 1 
                        celda.setCellValue(maxAulas_EspaCompBusqTraOrdWifi);
                        celda.setCellStyle(estiloCelda_WHITE);
                        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                        
                        celda = fila.createCell(10);
                        celda.setCellValue(maxPlanIgualdad);
                        celda.setCellStyle(estiloCelda_WHITE);
                        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                        
                        celda = fila.createCell(11);
                        celda.setCellValue(maxCertificadoCalidad);
                        celda.setCellStyle(estiloCelda_WHITE);
                        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                        
                        
                        celda = fila.createCell(12);
                        celda.setCellValue(maxPunt.doubleValue());
                        celda.setCellStyle(estiloCelda_DARK_YELLOW);
                        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                        
                        filaDesde = filaHasta + 1;
                        
                        cont = 0;
                        maxTray = new BigDecimal("0.0");
                        maxUbic = new BigDecimal("0.0");
                        maxDespachos_Mas1UbicaMismoAmbi = 0L;
                        maxAulas_EspaCompBusqTraOrdWifi = 0L;
                        maxPlanIgualdad = 0L;
                        maxCertificadoCalidad = 0L;
                        maxBloquesSolicitados = new BigDecimal("0.0");;
                        maxPunt = new BigDecimal("0.0");
                        expAnt = expAct;
                    }
                }

                if(ubic.getOriOrientHorasSolicitadas() != null && ubic.getOriOrientHorasSolicitadas().compareTo(maxBloquesSolicitados)>0)
                {
                    maxBloquesSolicitados = ubic.getOriOrientHorasSolicitadas();
                }

                if(ubic.getOriOrientValTray() != null && ubic.getOriOrientValTray().compareTo(maxTray)>0)
                {
                    maxTray = ubic.getOriOrientValTray();
                }

                if(ubic.getOriOrientValUbic() != null && ubic.getOriOrientValUbic().compareTo(maxUbic) > 0)
                {
                    maxUbic = ubic.getOriOrientValUbic();
                }
                
                if(ubic.getOriOrientAno()>=2017){
                    if (ubic.getOriOrientVal1EspacioAdicional()!= null && ubic.getOriOrientVal1EspacioAdicional() > maxDespachos_Mas1UbicaMismoAmbi) {
                        maxDespachos_Mas1UbicaMismoAmbi = ubic.getOriOrientVal1EspacioAdicional();
                    }

                    if (ubic.getOriOrientValEspAdicioHerrBusqEmpleo()!= null && ubic.getOriOrientValEspAdicioHerrBusqEmpleo() > maxAulas_EspaCompBusqTraOrdWifi) {
                        maxAulas_EspaCompBusqTraOrdWifi = ubic.getOriOrientValEspAdicioHerrBusqEmpleo();
                    }
                }else{
                    if (ubic.getOriOrientValDespachos() != null && ubic.getOriOrientValDespachos() > maxDespachos_Mas1UbicaMismoAmbi) {
                        maxDespachos_Mas1UbicaMismoAmbi = ubic.getOriOrientValDespachos();
                    }

                    if (ubic.getOriOrientValAulas() != null && ubic.getOriOrientValAulas() > maxAulas_EspaCompBusqTraOrdWifi) {
                        maxAulas_EspaCompBusqTraOrdWifi = ubic.getOriOrientValAulas();
                    }
                }
                
                if(ubic.getOriPlanIgualdadValoracion()!= null && ubic.getOriPlanIgualdadValoracion().compareTo(maxPlanIgualdad) > 0)
                {
                    maxPlanIgualdad = ubic.getOriPlanIgualdadValoracion();
                }
                
                if(ubic.getOriCertCalidadValoracion()!= null && ubic.getOriCertCalidadValoracion().compareTo(maxCertificadoCalidad) > 0)
                {
                    maxCertificadoCalidad = ubic.getOriCertCalidadValoracion();
                }
                
                if(ubic.getOriOrientPuntuacion() != null && ubic.getOriOrientPuntuacion().compareTo(maxPunt) > 0)
                {
                    maxPunt = ubic.getOriOrientPuntuacion();
                }
                cont++;
            }            
                        
            HSSFRow fila = hoja.getRow(filaDesde);
            
            filaHasta = filaDesde + cont - 1;
     
            hoja.addMergedRegion(new CellRangeAddress(filaDesde, filaHasta, 5, 5));
            hoja.addMergedRegion(new CellRangeAddress(filaDesde, filaHasta, 6, 6));
            hoja.addMergedRegion(new CellRangeAddress(filaDesde, filaHasta, 7, 7));
            hoja.addMergedRegion(new CellRangeAddress(filaDesde, filaHasta, 8, 8));
            hoja.addMergedRegion(new CellRangeAddress(filaDesde, filaHasta, 9, 9));
            hoja.addMergedRegion(new CellRangeAddress(filaDesde, filaHasta, 10, 10));
            hoja.addMergedRegion(new CellRangeAddress(filaDesde, filaHasta, 11, 11));
            hoja.addMergedRegion(new CellRangeAddress(filaDesde, filaHasta, 12, 12));

            HSSFCell celda = fila.createCell(5);
            celda.setCellValue(maxBloquesSolicitados.doubleValue());
            celda.setCellStyle(estiloCelda_WHITE);
            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

            celda = fila.createCell(6);
            celda.setCellValue(maxTray.doubleValue());
            celda.setCellStyle(estiloCelda_WHITE);
            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

            celda = fila.createCell(7);
            celda.setCellValue(maxUbic.doubleValue());
            celda.setCellStyle(estiloCelda_WHITE);
            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

            celda = fila.createCell(8);
            celda.setCellValue(maxDespachos_Mas1UbicaMismoAmbi);
            celda.setCellStyle(estiloCelda_WHITE);
            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

            celda = fila.createCell(9);
            celda.setCellValue(maxAulas_EspaCompBusqTraOrdWifi);
            celda.setCellStyle(estiloCelda_WHITE);
            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            
            celda = fila.createCell(10);
            celda.setCellValue(maxPlanIgualdad);
            celda.setCellStyle(estiloCelda_WHITE);
            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            
            celda = fila.createCell(11);
            celda.setCellValue(maxCertificadoCalidad);
            celda.setCellStyle(estiloCelda_WHITE);
            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

            celda = fila.createCell(12);
            celda.setCellValue(maxPunt.doubleValue());
            celda.setCellStyle(estiloCelda_DARK_YELLOW);
            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

            filaDesde = filaHasta;
        }
    }
    
    private void anadirFilaTotalesResolucionHoras(HSSFWorkbook libro, HSSFSheet hoja, int numFila, int solicitadasTotales, double puntuacionTotal, double adjudicadasTotales, MeLanbide47I18n meLanbide47I18n, int idioma)
    {
        HSSFRow fila = hoja.createRow(numFila);
        
        hoja.addMergedRegion(new CellRangeAddress(numFila, numFila, 0, 4));
        
        HSSFCellStyle estiloCelda1 = libro.createCellStyle();
        estiloCelda1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda1.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        estiloCelda1.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda1.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda1.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda1.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda1.setWrapText(true);
        
        HSSFCellStyle estiloCelda2 = libro.createCellStyle();
        estiloCelda2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda2.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        estiloCelda2.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda2.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda2.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda2.setWrapText(true);
        
        HSSFCellStyle estiloCelda3 = libro.createCellStyle();
        estiloCelda3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda3.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        estiloCelda3.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda3.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda3.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda3.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda3.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        estiloCelda3.setWrapText(true);
        
        HSSFCellStyle estiloCelda4 = libro.createCellStyle();
        estiloCelda4.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda4.setFillForegroundColor(HSSFColor.LIGHT_ORANGE.index);
        estiloCelda4.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda4.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda4.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda4.setWrapText(true);
        
        HSSFCellStyle estiloCelda5 = libro.createCellStyle();
        estiloCelda5.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda5.setFillForegroundColor(HSSFColor.LIGHT_ORANGE.index);
        estiloCelda5.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda5.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda5.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda5.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        estiloCelda5.setWrapText(true);
        
        HSSFCellStyle estiloCelda6 = libro.createCellStyle();
        estiloCelda6.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda6.setFillForegroundColor(HSSFColor.WHITE.index);
        estiloCelda6.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda6.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda6.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda6.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda6.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        estiloCelda6.setWrapText(true);
        
        HSSFCell celda = fila.createCell(0);
        celda.setCellValue(meLanbide47I18n.getMensaje(idioma, "label.doc.resolucionHoras.horasTotSolicitadas"));
        celda.setCellStyle(estiloCelda1);
        
        
        celda = fila.createCell(1);
        celda.setCellStyle(estiloCelda2);
        
        celda = fila.createCell(2);
        celda.setCellStyle(estiloCelda2);
        
        celda = fila.createCell(3);
        celda.setCellStyle(estiloCelda2);
        
        celda = fila.createCell(4);
        celda.setCellStyle(estiloCelda2);
        
        celda = fila.createCell(5);
        celda.setCellValue(Double.valueOf(solicitadasTotales));
        celda.setCellStyle(estiloCelda3);
        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
        
        celda = fila.createCell(11);
        celda.setCellValue(meLanbide47I18n.getMensaje(idioma, "label.doc.resolucionHoras.sumatorio"));
        celda.setCellStyle(estiloCelda4);
                
        celda = fila.createCell(12);
        celda.setCellValue(Double.valueOf(puntuacionTotal));
        celda.setCellStyle(estiloCelda5);
        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
        
        celda = fila.createCell(13);
        celda.setCellValue(Double.valueOf(adjudicadasTotales));
        celda.setCellStyle(estiloCelda6);
        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
    }
    
    public void consolidaHoras(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
        int codigoOperacion = 0;
        String mensaje = "";
        try
        {
            try
            {
                mensaje = MeLanbide47Manager.getInstance().consolidaHoras(this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if(mensaje != null && mensaje.equalsIgnoreCase(ConstantesMeLanbide47.OK))
                {
                    codigoOperacion = 0;
                }
                else
                {
                    codigoOperacion = 1;
                }
                    
                //Creamos la auditorï¿½a
                String codProcedimiento = null;
                try
                {
                    codProcedimiento = (String)request.getParameter("codProcedimiento");
                }
                catch(Exception ex)
                {

                }
                this.crearAuditoria(codOrganizacion, (UsuarioValueObject)request.getSession().getAttribute("usuario"), ConstantesMeLanbide47.COD_PROC_CONSOLIDA_HORAS, mensaje, codProcedimiento);
            }
            catch(Exception ex)
            {
                codigoOperacion = 1;
            }
        }
        catch(Exception ex)
        {
            codigoOperacion = 2;
        }
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
            xmlSalida.append("<CODIGO_OPERACION>");
                xmlSalida.append(codigoOperacion);
            xmlSalida.append("</CODIGO_OPERACION>");
            xmlSalida.append("<MENSAJE>");
                xmlSalida.append(mensaje);
            xmlSalida.append("</MENSAJE>");
        xmlSalida.append("</RESPUESTA>");
        try{
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        }catch(Exception e){
            log.error("Error : "+ e.getMessage(), e);
        }//try-catch
    }
    
    public void deshacerConsolidacionHoras(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
        int codigoOperacion = 0;
        String mensaje = "";
        if(request.getParameter("ano") != null)
        {
            try
            {
                int ano = Integer.parseInt(request.getParameter("ano"));
                try
                {
                    mensaje = MeLanbide47Manager.getInstance().deshacerConsolidacionHoras(ano, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    if(mensaje != null && mensaje.equalsIgnoreCase(ConstantesMeLanbide47.OK))
                    {
                        codigoOperacion = 0;
                    }
                    else
                    {
                        codigoOperacion = 1;
                    }
                    
                    //Creamos la auditorï¿½a
                    String codProcedimiento = null;
                    try
                    {
                        codProcedimiento = (String)request.getParameter("codProcedimiento");
                    }
                    catch(Exception ex)
                    {
                        
                    }
                    this.crearAuditoria(codOrganizacion, (UsuarioValueObject)request.getSession().getAttribute("usuario"), ConstantesMeLanbide47.COD_PROC_DESHACER_CONSOLIDACION_HORAS, mensaje, codProcedimiento);
                }
                catch(Exception ex)
                {
                    codigoOperacion = 1;
                }
            }
            catch(Exception ex)
            {
                codigoOperacion = 2;
            }
        }
        else
        {
            codigoOperacion = 3;
        }
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
            xmlSalida.append("<CODIGO_OPERACION>");
                xmlSalida.append(codigoOperacion);
            xmlSalida.append("</CODIGO_OPERACION>");
            xmlSalida.append("<MENSAJE>");
                xmlSalida.append(mensaje);
            xmlSalida.append("</MENSAJE>");
        xmlSalida.append("</RESPUESTA>");
        try{
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        }catch(Exception e){
            log.error("Error : "+ e.getMessage(), e);
        }//try-catch
    }
    
    private String procesarMensajeProcedimiento(String mensaje, Integer idioma)
    {
        String mensajeAux = new String(mensaje);
        mensajeAux = mensajeAux.toLowerCase();
        mensajeAux = mensajeAux.replaceAll(" ", "_");
        try
        {
            MeLanbide47I18n mm = MeLanbide47I18n.getInstance();
            mensajeAux = mm.getMensaje(idioma, mensajeAux);
            mensaje = mensajeAux;
        }
        catch(Exception ex)
        {
            
        }
        return mensaje;
    }
    
    private void crearAuditoria(int codOrganizacion, UsuarioValueObject usuario, int codProceso, String resultado, String codProcedimiento) throws Exception
    {
        if(usuario != null)
        {
            AuditoriaVO aud = new AuditoriaVO();
            aud.setUsuCod(usuario.getIdUsuario());
            aud.setProcedimiento(codProceso);
            aud.setResultado(resultado);
            aud.setCodProcedimiento(codProcedimiento);
            MeLanbide47Manager.getInstance().crearAuditoria(aud, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        }
    }
    
    /*private List<OriUbicVO> ubicacionesQueNoEstanEnResProvAlFinal(List<OriUbicVO> lista, AdaptadorSQLBD adapt) throws Exception
    {
        
        List<OriUbicVO> retList = new ArrayList<OriUbicVO>();
        for(int i = 0; i < lista.size(); i++)
        {
            retList.add(i, lista.get(i));
        }
        
        OriUbicVO act = null;
        int pos = 0;
        OriUbicVO ult = retList.get(retList.size()-1);
        boolean parada = false;
        BigDecimal bd = null;
        while(pos < retList.size() && !parada)
        {
            act = retList.get(pos);
            if(act.getOriEntCod().intValue() == ult.getOriEntCod().intValue())
            {
                parada = true;
            }
            
            try
            {
                bd = MeLanbide47Manager.getInstance().getHorasAsignadasUbicacionORI(act.getOriOrientUbicCod(), adapt);
            }
            catch(Exception ex)
            {

            }
            if(bd == null || bd.intValue() == 0)
            {
                retList.remove(pos);
                act.setOriOrientPuntuacion(new BigDecimal("0"));
                retList.add(retList.size(), act);
//                if(pos > 0)
//                {
//                    pos--;
//                }
            }
            else
            {
                pos++;
            }
            bd = null;
        }        
        
        return retList;
    }*/
    
    
    
    private List<OriUbicVO> ubicacionesQueNoEstanEnResProvAlFinal(List<OriUbicVO> lista, AdaptadorSQLBD adapt) throws Exception
    {
        Map<String, List<OriUbicVO>> ubicacionesPorExpediente = new HashMap<String, List<OriUbicVO>>();
        List<OriUbicVO> retList = new ArrayList<OriUbicVO>();
        List<OriUbicVO> tmpList = new ArrayList<OriUbicVO>();
        OriUbicVO act = null;
        for(int i = 0; i < lista.size(); i++)
        {
            act = lista.get(i);
            if(act.getNumExp() != null && !act.getNumExp().equals(""))
            {
                tmpList = ubicacionesPorExpediente.get(act.getNumExp());
                if(tmpList == null)
                {
                    tmpList = new ArrayList<OriUbicVO>();
                    ubicacionesPorExpediente.put(act.getNumExp(), tmpList);
                }
                tmpList.add(act);
            }
        }
        
        int pos = 0;
        OriUbicVO ult = null;
        boolean parada = false;
        BigDecimal bd = null;
        String numExp = null;
        Iterator<String> it = ubicacionesPorExpediente.keySet().iterator();
        while(it.hasNext())
        {
            numExp = it.next();
            tmpList = ubicacionesPorExpediente.get(numExp);
            ult = tmpList.get(tmpList.size()-1);
            while(pos < tmpList.size() && !parada)
            {
                act = tmpList.get(pos);
                if(act.getOriEntCod().intValue() == ult.getOriEntCod().intValue())
                {
                    parada = true;
                }

                try
                {
                    bd = MeLanbide47Manager.getInstance().getHorasAsignadasUbicacionORI(act.getOriOrientUbicCod(), adapt);
                }
                catch(Exception ex)
                {

                }
                if(bd == null || bd.intValue() == 0)
                {
                    tmpList.remove(pos);
                    act.setOriOrientPuntuacion(new BigDecimal("0"));
                    tmpList.add(tmpList.size(), act);
                }
                else
                {
                    pos++;
                }
                bd = null;
            }     
        }
        
        List<String> listaExpedientes = new ArrayList<String>();
        listaExpedientes.addAll(ubicacionesPorExpediente.keySet());
        Collections.sort(listaExpedientes);
        
        retList = new ArrayList<OriUbicVO>();
        for(String numExpAct : listaExpedientes)
        {
            retList.addAll(ubicacionesPorExpediente.get(numExpAct));
        }
        
        return retList;
    }
    
    
    
    private void escribirListaAsociacionesRequest(String codigoOperacion, EntidadVO entidad, List<FilaAsociacionVO> asociaciones, HttpServletResponse response)
    {
        StringBuilder xmlSalida = new StringBuilder();
        xmlSalida.append("<RESPUESTA>");
            xmlSalida.append("<CODIGO_OPERACION>");
                xmlSalida.append(codigoOperacion);
            xmlSalida.append("</CODIGO_OPERACION>");
            xmlSalida.append("<CODIGO_ENTIDAD>");
                xmlSalida.append(entidad != null && entidad.getOriEntCod() != null ? entidad.getOriEntCod() : "-");
            xmlSalida.append("</CODIGO_ENTIDAD>");
            
        for(FilaAsociacionVO asoc : asociaciones)
        {
            xmlSalida.append("<ASOCIACION>");
                xmlSalida.append("<COD_ENTIDAD>");
                        xmlSalida.append(asoc.getCodEntidad());
                xmlSalida.append("</COD_ENTIDAD>");
                xmlSalida.append("<COD_ASOCIACION>");
                        xmlSalida.append(asoc.getCodAsociacion());
                xmlSalida.append("</COD_ASOCIACION>");
                xmlSalida.append("<CIF>");
                        xmlSalida.append(asoc.getCif());
                xmlSalida.append("</CIF>");
                xmlSalida.append("<NOMBRE>");
                        xmlSalida.append(asoc.getNombre());
                xmlSalida.append("</NOMBRE>");
                xmlSalida.append("<SUPRAMUN>");
                        xmlSalida.append(asoc.getSupramun());
                xmlSalida.append("</SUPRAMUN>");
                xmlSalida.append("<ADM_LOCAL>");
                        xmlSalida.append(asoc.getAdmLocal());
                xmlSalida.append("</ADM_LOCAL>");
                xmlSalida.append("<CENTROFP_PUB>");
                        xmlSalida.append(asoc.getCentrofpPub());
                xmlSalida.append("</CENTROFP_PUB>");
                xmlSalida.append("<CENTROFP_PRIV>");
                        xmlSalida.append(asoc.getCentrofpPriv());
                xmlSalida.append("</CENTROFP_PRIV>");
                xmlSalida.append("<ORI_ENT_AGENCOLOC>");
                        xmlSalida.append(asoc.getAgenciaColocacion());
                xmlSalida.append("</ORI_ENT_AGENCOLOC>");
                xmlSalida.append("<SINANIMOLUCRO>");
                        xmlSalida.append(asoc.getSinAnimoLucro());
                xmlSalida.append("</SINANIMOLUCRO>");
            xmlSalida.append("</ASOCIACION>");
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
    }
    
    public void escribirDatosEntidadRequest(String codigoOperacion, EntidadVO entidad, List<FilaAsociacionVO> asociaciones, HttpServletResponse response)
    {
        StringBuilder xmlSalida = new StringBuilder();
        xmlSalida.append("<RESPUESTA>");
            xmlSalida.append("<CODIGO_OPERACION>");
                xmlSalida.append(codigoOperacion);
            xmlSalida.append("</CODIGO_OPERACION>");
            xmlSalida.append("<CODIGO_ENTIDAD>");
                xmlSalida.append(entidad != null && entidad.getOriEntCod() != null ? entidad.getOriEntCod() : "-");
            xmlSalida.append("</CODIGO_ENTIDAD>");
            
        for(FilaAsociacionVO asoc : asociaciones)
        {
            xmlSalida.append("<ASOCIACION>");
                xmlSalida.append("<COD_ENTIDAD>");
                        xmlSalida.append(asoc.getCodEntidad());
                xmlSalida.append("</COD_ENTIDAD>");
                xmlSalida.append("<COD_ASOCIACION>");
                        xmlSalida.append(asoc.getCodAsociacion());
                xmlSalida.append("</COD_ASOCIACION>");
                xmlSalida.append("<CIF>");
                        xmlSalida.append(asoc.getCif());
                xmlSalida.append("</CIF>");
                xmlSalida.append("<NOMBRE>");
                        xmlSalida.append(asoc.getNombre());
                xmlSalida.append("</NOMBRE>");
                xmlSalida.append("<SUPRAMUN>");
                        xmlSalida.append(asoc.getSupramun());
                xmlSalida.append("</SUPRAMUN>");
                xmlSalida.append("<ADM_LOCAL>");
                        xmlSalida.append(asoc.getAdmLocal());
                xmlSalida.append("</ADM_LOCAL>");
                xmlSalida.append("<CENTROFP_PUB>");
                        xmlSalida.append(asoc.getCentrofpPub());
                xmlSalida.append("</CENTROFP_PUB>");
                xmlSalida.append("<CENTROFP_PRIV>");
                        xmlSalida.append(asoc.getCentrofpPriv());
                xmlSalida.append("</CENTROFP_PRIV>");
                xmlSalida.append("<ORI_ENT_AGENCOLOC>");
                        xmlSalida.append(asoc.getAgenciaColocacion());
                xmlSalida.append("</ORI_ENT_AGENCOLOC>");
                xmlSalida.append("<SINANIMOLUCRO>");
                        xmlSalida.append(asoc.getSinAnimoLucro());
                xmlSalida.append("</SINANIMOLUCRO>");
            xmlSalida.append("</ASOCIACION>");
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
    }
    
    public void escribirListaAmbitosRequest(String codigoOperacion, EntidadVO entidad, List<FilaUbicOrientacionVO> ambitos, HttpServletResponse response)
    {
        StringBuilder xmlSalida = new StringBuilder();
        xmlSalida.append("<RESPUESTA>");
            xmlSalida.append("<CODIGO_OPERACION>");
                xmlSalida.append(codigoOperacion);
            xmlSalida.append("</CODIGO_OPERACION>");
            xmlSalida.append("<CODIGO_ENTIDAD>");
                xmlSalida.append(entidad != null && entidad.getOriEntCod() != null ? entidad.getOriEntCod() : "");
            xmlSalida.append("</CODIGO_ENTIDAD>");
            for(FilaUbicOrientacionVO fila : ambitos)
            {
                xmlSalida.append("<FILA>");
                    xmlSalida.append("<ID>");
                        xmlSalida.append(fila.getCodigoUbic() != null ? fila.getCodigoUbic().toString() : "");
                    xmlSalida.append("</ID>");
                    xmlSalida.append("<PROVINCIA>");
                        xmlSalida.append(fila.getProvincia());
                    xmlSalida.append("</PROVINCIA>");
                    xmlSalida.append("<AMBITO>");
                        xmlSalida.append(fila.getAmbito());
                    xmlSalida.append("</AMBITO>");
                    xmlSalida.append("<MUNICIPIO>");
                        xmlSalida.append(fila.getMunicipio());
                    xmlSalida.append("</MUNICIPIO>");
                    xmlSalida.append("<DIRECCION>");
                        xmlSalida.append(fila.getDireccion());
                    xmlSalida.append("</DIRECCION>");
                    xmlSalida.append("<COD_POSTAL>");
                        xmlSalida.append(fila.getCodPostal());
                    xmlSalida.append("</COD_POSTAL>");
                    xmlSalida.append("<HORAS>");
                        xmlSalida.append(fila.getHoras());
                    xmlSalida.append("</HORAS>");
                    xmlSalida.append("<DESPACHOS>");
                        xmlSalida.append(fila.getDespachos());
                    xmlSalida.append("</DESPACHOS>");
                    xmlSalida.append("<AULAGRUPAL>");
                        xmlSalida.append(fila.getAulaGrupal());
                    xmlSalida.append("</AULAGRUPAL>");
                    xmlSalida.append("<VALORACION>");
                        xmlSalida.append(fila.getValoracion());
                    xmlSalida.append("</VALORACION>");
                    xmlSalida.append("<TOTAL>");
                        xmlSalida.append(fila.getTotal());
                    xmlSalida.append("</TOTAL>");
                    xmlSalida.append("<HORASADJ>");
                        xmlSalida.append(fila.getHorasAdj());
                    xmlSalida.append("</HORASADJ>");
                    xmlSalida.append("<TELEFIJO_UBICACION>");
                        xmlSalida.append(fila.getOriOrientUbicaTeleFijo());
                    xmlSalida.append("</TELEFIJO_UBICACION>");
                    xmlSalida.append("<ESPACIOADICIONA>");
                        xmlSalida.append(fila.getOriOrientUbicaEspacioAdicional());
                    xmlSalida.append("</ESPACIOADICIONA>");
                    xmlSalida.append("<ESPHERRABUSQEMP>");
                        xmlSalida.append(fila.getOriOrientUbicaEspAdicioHerrBusqEmpleo());
                    xmlSalida.append("</ESPHERRABUSQEMP>");
                    xmlSalida.append("<ORI_LOCALPREVAPRO>");
                        xmlSalida.append(fila.getOriCELocalPreviamenteAprobado());
                    xmlSalida.append("</ORI_LOCALPREVAPRO>");
                    xmlSalida.append("<ORI_MATENREQ_LPA>");
                        xmlSalida.append(fila.getOriCEMantenimientoRequisitosLPA());
                    xmlSalida.append("</ORI_MATENREQ_LPA>");
                    xmlSalida.append("<ORI_ORIENT_NUMERO_UBICACION>");
                        xmlSalida.append(fila.getDireccionNumero());
                    xmlSalida.append("</ORI_ORIENT_NUMERO_UBICACION>");
                    xmlSalida.append("<ORI_ORIENT_PISO_UBICACION>");
                        xmlSalida.append(fila.getDireccionPiso());
                    xmlSalida.append("</ORI_ORIENT_PISO_UBICACION>");
                    xmlSalida.append("<ORI_ORIENT_LETRA_UBICACION>");
                        xmlSalida.append(fila.getDireccionLetra());
                    xmlSalida.append("</ORI_ORIENT_LETRA_UBICACION>");
                    xmlSalida.append("<ORI_PLANIGUALDAD>");
                        xmlSalida.append(fila.getOriEntPlanIgualdad());
                    xmlSalida.append("</ORI_PLANIGUALDAD>");
                    xmlSalida.append("<ORI_CERTCALIDAD>");
                        xmlSalida.append(fila.getOriEntCertCalidad());
                    xmlSalida.append("</ORI_CERTCALIDAD>");
                xmlSalida.append("</FILA>");
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
    }
    
    private void escribirResultadoGuardarTrayectoriaORIRequest(String codigoOperacion, HttpServletResponse response)
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
    
    public void escribirTrayectoriaORIRequest(String codigoOperacion, List<AsociacionVO> asociaciones, Map<Long, OriTrayectoriaVO> mapaTrayectorias, HttpServletResponse response)
    {
        StringBuilder xml = new StringBuilder();
        xml.append("<RESPUESTA>");
            xml.append("<CODIGO_OPERACION>");
                xml.append(codigoOperacion);
            xml.append("</CODIGO_OPERACION>");
            Iterator<AsociacionVO> it = asociaciones.iterator();
            AsociacionVO asociacion = null;
            OriTrayectoriaVO tray = null;
            Long codAsoc = null;
            String nombreAsociacion = null;
            while(it.hasNext())
            {
                asociacion = it.next();
                codAsoc = asociacion.getOriAsocCod();
                if(mapaTrayectorias.containsKey(codAsoc))
                {
                    tray = mapaTrayectorias.get(codAsoc);
                }
                else
                {
                    tray = null;
                }
                nombreAsociacion = asociacion.getOriAsocCif() != null ? asociacion.getOriAsocCif() : "";
                nombreAsociacion += !nombreAsociacion.equals("") && asociacion.getOriAsocNombre() != null && !asociacion.getOriAsocNombre().equals("") ? " - " : "";
                nombreAsociacion += asociacion.getOriAsocNombre() != null ? asociacion.getOriAsocNombre() : "";
                
                xml.append("<ASOCIACION>");
                    xml.append("<COD_ASOCIACION>");
                        xml.append(codAsoc);
                    xml.append("</COD_ASOCIACION>");
                    xml.append("<NOMBRE_ASOCIACION>");
                        xml.append(nombreAsociacion.toUpperCase());
                    xml.append("</NOMBRE_ASOCIACION>");
                        xml.append("<TRAYECTORIA>");
                            xml.append("<TRAY_COD>");
                                xml.append(tray != null && tray.getOriOritrayCod() != null ? tray.getOriOritrayCod() : "-");
                            xml.append("</TRAY_COD>");
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
                            xml.append("<MIN_94_2017>");
                                xml.append(tray != null && tray.getMin94_2017() != null ? tray.getMin94_2017() : "-");
                            xml.append("</MIN_94_2017>");
                            xml.append("<MIN_94_2018>");
                                xml.append(tray != null && tray.getMin94_2018() != null ? tray.getMin94_2018() : "-");
                            xml.append("</MIN_94_2018>");
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
                            xml.append("<MIN_98_2017>");
                                xml.append(tray != null && tray.getMin98_2017() != null ? tray.getMin98_2017() : "-");
                            xml.append("</MIN_98_2017>");
                            xml.append("<MIN_98_2018>");
                                xml.append(tray != null && tray.getMin98_2018() != null ? tray.getMin98_2018() : "-");
                            xml.append("</MIN_98_2018>");
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
                            xml.append("<TAS_03_2017>");
                                xml.append(tray != null && tray.getTas03_2017() != null ? tray.getTas03_2017() : "-");
                            xml.append("</TAS_03_2017>");
                            xml.append("<TAS_03_2018>");
                                xml.append(tray != null && tray.getTas03_2018() != null ? tray.getTas03_2018() : "-");
                            xml.append("</TAS_03_2018>");
                            xml.append("<ACT_56_03>");
                                xml.append(tray != null && tray.getAct56_03() != null ? tray.getAct56_03() : "-");
                            xml.append("</ACT_56_03>");
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
                            xml.append("<LAN_2017>");
                                xml.append(tray != null && tray.getLan_2017() != null ? tray.getLan_2017() : "-");
                            xml.append("</LAN_2017>");
                            xml.append("<LAN_OTROS>");
                                xml.append(tray != null && tray.getLan_otros() != null ? tray.getLan_otros() : "-");
                            xml.append("</LAN_OTROS>");
                            xml.append("<COLEC1_DEC_327>");
                                xml.append(tray != null && tray.getDec327()!= null ? tray.getDec327() : "-");
                            xml.append("</COLEC1_DEC_327>");
                            xml.append("<COLEC1_MIN_94>");
                                xml.append(tray != null && tray.getMin94()!= null ? tray.getMin94() : "-");
                            xml.append("</COLEC1_MIN_94>");
                            xml.append("<COLEC1_MIN_98>");
                                xml.append(tray != null && tray.getMin98()!= null ? tray.getMin98() : "-");
                            xml.append("</COLEC1_MIN_98>");
                            xml.append("<COLEC1_TAS_03>");
                                xml.append(tray != null && tray.getTas03()!= null ? tray.getTas03() : "-");
                            xml.append("</COLEC1_TAS_03>");
                        xml.append("</TRAYECTORIA>");
                xml.append("</ASOCIACION>");
            }
        xml.append("</RESPUESTA>");
        
        
        xml.append("");
        
        
        
        try{
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xml.toString());
            out.flush();
            out.close();
        }catch(IOException e){
            log.error(e.getStackTrace());
        }//try-catch
    }
    public String cargarAltaEdicionTrayectoriaOtrosProgramas (int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
      log.debug("cargarAltaEdicionTrayectoriaOtrosProgramas - Begin()");
      try
      {
          String modoDatos = request.getParameter("modoDatos");
          String idTray= request.getParameter("idTray");
          String codEntidad = request.getParameter("codEntidad");
          request.setAttribute("modoDatos", modoDatos); // 0 Nueva Actividad / 1 Modificar actividad
          
            List<SelectItem> listaEntidades = new ArrayList<SelectItem>();
            try
            {
                listaEntidades = MeLanbide47Manager.getInstance().getListaSelectItemEntidadesPorExpediente(numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            }
            catch(Exception ex)
            {
                log.debug("Error al obtener la Lista entidad al cargar modificar o alta Otros Programas", ex);
            }
            
            request.setAttribute("listaEntidades", listaEntidades);
            
            OriTrayOtroProgramaVO trayecModif = new OriTrayOtroProgramaVO();
            trayecModif.setNumExpediente(numExpediente);
            trayecModif.setCodIdOtroPrograma(idTray!=null?Integer.parseInt(idTray):null);
            trayecModif.setCodEntidad(codEntidad!=null?Integer.parseInt(codEntidad):null);
            trayecModif.setEjercicio(MeLanbide47Utils.getEjercicioDeExpediente(numExpediente));
            trayecModif=MeLanbide47Manager.getInstance().getTrayectoriaOtrosProgramas(trayecModif, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            request.setAttribute("trayecModif", trayecModif);
            
      }
      catch(Exception ex)
      {
           log.error("Error al cargarAltaEdicionTrayectoriaOtrosProgramas : ", ex);
      }
      
      return "/jsp/extension/melanbide47/orientacion/trayectoria/anteriores/altaEdicionPrograma.jsp";
    }
    public void guardarTrayectoriaOtrosProgramas(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        log.debug("guardarTrayectoriaOtrosProgramas - Begin()");
        String codigoOperacion = "0";
        List<OriTrayOtroProgramaVO> filas = new ArrayList<OriTrayOtroProgramaVO>();
        try
        {
            String idTray = request.getParameter("idTray");
            String codEntidad = request.getParameter("codEntidad");
            String programa = request.getParameter("programa");
            String anio = request.getParameter("anio");
            String duracion = request.getParameter("duracion");
            String anioVal = request.getParameter("anioVal");
            String duracionVal = request.getParameter("duracionVal");
            try {
                OriTrayOtroProgramaVO tray = new OriTrayOtroProgramaVO();
                 AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                tray.setCodIdOtroPrograma(idTray!=null && !idTray.equals("")?Integer.parseInt(idTray):null);
                tray.setCodEntidad(codEntidad!=null && !codEntidad.equals("")?Integer.parseInt(codEntidad):null);
                tray.setNumExpediente(numExpediente);
                tray.setEjercicio(MeLanbide47Utils.getEjercicioDeExpediente(numExpediente));
                tray.setPrograma(programa);
                tray.setAnioPrograma(Integer.parseInt(anio));
                tray.setDuracion(Integer.parseInt(duracion));
                tray.setAnioProgramaVal(anioVal!=null && !anioVal.isEmpty() ? Integer.parseInt(anioVal) : null);
                tray.setDuracionVal(duracionVal !=null && !duracionVal.isEmpty() ? Integer.parseInt(duracionVal): null);
                boolean result =MeLanbide47Manager.getInstance().guardarTrayectoriaOtrosProgramas(tray,adaptador);
                if (result){
                    filas = MeLanbide47Manager.getInstance().getListaTrayectoriaOtrosProgramas(tray, adaptador);
                }else{
                    codigoOperacion="3";
                }
                
                
            } catch (Exception ex) {
                log.error("Error al guardar una trayectoria  otros programas", ex);
                codigoOperacion = "1";
            }
        }
         catch(Exception ex)
        {
            log.error("Error al guardar una trayectoria  otros programas", ex);
            codigoOperacion = "2";
        }
        escribirResultadoGuardarTrayectoriaOtrosProgramasRequest(codigoOperacion, filas, response);
        log.debug("guardarTrayectoriaOtrosProgramas - End()"); 
    }

    public void eliminarTrayectoriaOtrosProgramas(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
         String codigoOperacion = "0";
         List<OriTrayOtroProgramaVO> filas = new ArrayList<OriTrayOtroProgramaVO>();
         try
         {
             String idTray = request.getParameter("idTray");
            String codEntidad = request.getParameter("codEntidad");
            Integer ejercicio = MeLanbide47Utils.getEjercicioDeExpediente(numExpediente);
            String idiomaUsuario = request.getParameter("idiomaUsuario");
             try {
                 OriTrayOtroProgramaVO tray = new OriTrayOtroProgramaVO();
                 AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                tray.setCodIdOtroPrograma(idTray!=null && !idTray.equals("")?Integer.parseInt(idTray):null);
                tray.setCodEntidad(codEntidad!=null&& !codEntidad.equals("")?Integer.parseInt(codEntidad):null);
                tray.setNumExpediente(numExpediente);
                tray.setEjercicio(ejercicio);
                MeLanbide47Manager.getInstance().eliminarTrayectoriaOtrosProgramas(tray,adaptador);
                filas = MeLanbide47Manager.getInstance().getListaTrayectoriaOtrosProgramas(tray, adaptador);
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
         escribirResultadoGuardarTrayectoriaOtrosProgramasRequest(codigoOperacion, filas, response);
    }
    
    private void escribirResultadoGuardarTrayectoriaOtrosProgramasRequest(String codigoOperacion, List<OriTrayOtroProgramaVO> filas, HttpServletResponse response) {
       StringBuilder xmlSalida = new StringBuilder();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        xmlSalida.append("<TRAYECTORIAS>");
        for(OriTrayOtroProgramaVO tray : filas){
            xmlSalida.append("<TRAYECTORIA>");
            xmlSalida.append("<ORI_OTRPRO_ID>");
            xmlSalida.append(tray.getCodIdOtroPrograma());
            xmlSalida.append("</ORI_OTRPRO_ID>");
            xmlSalida.append("<ORI_OTRPRO_EXP_EJE>");
            xmlSalida.append(tray.getEjercicio());
            xmlSalida.append("</ORI_OTRPRO_EXP_EJE>");
            xmlSalida.append("<ORI_OTRPRO_NUMEXP>");
            xmlSalida.append(tray.getNumExpediente()!= null ? tray.getNumExpediente(): "-");
            xmlSalida.append("</ORI_OTRPRO_NUMEXP>");
            xmlSalida.append("<ORI_OTRPRO_COD_ENTIDAD>");
            xmlSalida.append(tray.getCodEntidad());
            xmlSalida.append("</ORI_OTRPRO_COD_ENTIDAD>");
            xmlSalida.append("<ORI_OTRPRO_PROGRAMA>");
            xmlSalida.append(tray.getPrograma()!= null ? tray.getPrograma() : "");
            xmlSalida.append("</ORI_OTRPRO_PROGRAMA>");
            xmlSalida.append("<ORI_OTRPRO_PROG_EJE>");
            xmlSalida.append(tray.getAnioPrograma());
            xmlSalida.append("</ORI_OTRPRO_PROG_EJE>");
            xmlSalida.append("<ORI_OTRPRO_DURACION>");
            xmlSalida.append(tray.getDuracion());
            xmlSalida.append("</ORI_OTRPRO_DURACION>");
            xmlSalida.append("<ORI_ENT_CIF>");
            xmlSalida.append(tray.getCifEntidad()!=null?tray.getCifEntidad():"-");
            xmlSalida.append("</ORI_ENT_CIF>");
            xmlSalida.append("<ORI_ENT_NOM>");
            xmlSalida.append(tray.getNombreEntidad()!= null ? tray.getNombreEntidad(): "-");
            xmlSalida.append("</ORI_ENT_NOM>");
             xmlSalida.append("<ORI_OTRPRO_PROG_EJE_VALID>");
            xmlSalida.append(tray.getAnioProgramaVal());
            xmlSalida.append("</ORI_OTRPRO_PROG_EJE_VALID>");
            xmlSalida.append("<ORI_OTRPRO_DURACION_VALID>");
            xmlSalida.append(tray.getDuracionVal());
            xmlSalida.append("</ORI_OTRPRO_DURACION_VALID>");
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

    public String cargarAltaEdicionTrayectoriaActividades(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response){
       log.debug("cargarAltaEdicionTrayectoriaACtividades - Begin()");
       try
       { 
            String modoDatos = request.getParameter("modoDatos");
            String idTray = request.getParameter("idTray");
            String codEntidad = request.getParameter("codEntidad");
            request.setAttribute("modoDatos", modoDatos); // 0 Nueva Actividad / 1 Modificar actividad

            List<SelectItem> listaEntidades = new ArrayList<SelectItem>();
            try
            {
                listaEntidades = MeLanbide47Manager.getInstance().getListaSelectItemEntidadesPorExpediente(numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            }
            catch(Exception ex)
            {
                log.debug("Error al obtener la Lista entidad al cargar modificar o alta Otros Programas", ex);
            }            
            request.setAttribute("listaEntidades", listaEntidades);
            
            OriTrayActividadVO trayecModif = new OriTrayActividadVO();
            trayecModif.setNumExpediente(numExpediente);
            trayecModif.setCodIdActividad(idTray!=null?Integer.parseInt(idTray):null);
            trayecModif.setCodEntidad(codEntidad!=null?Integer.parseInt(codEntidad):null);
            trayecModif.setEjercicio(MeLanbide47Utils.getEjercicioDeExpediente(numExpediente));
            log.debug("Num exp: "+trayecModif.getNumExpediente());
           log.debug("ID tray: "+trayecModif.getCodIdActividad());
            
            trayecModif=MeLanbide47Manager.getInstance().getTrayectoriaActividades(trayecModif, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
           
            
            request.setAttribute("trayecModif", trayecModif);
       }
       catch(Exception ex)
        {
            log.error("Error al cargarAltaEdicionTrayGenActividades : ", ex);
        }
        return "/jsp/extension/melanbide47/orientacion/trayectoria/anteriores/altaEdicionActividades.jsp";
    }
    
    public void eliminarTrayectoriaActividades(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        String codigoOperacion = "0";
        List<OriTrayActividadVO> filas = new ArrayList<OriTrayActividadVO>();
        try
        {
           String idTray = request.getParameter("idTray");
           String codEntidad = request.getParameter("codEntidad");
           Integer ejercicio = MeLanbide47Utils.getEjercicioDeExpediente(numExpediente);
           String idiomaUsuario = request.getParameter("idiomaUsuario");
            try {
                OriTrayActividadVO tray = new OriTrayActividadVO();
                AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                tray.setCodIdActividad(idTray!=null && !idTray.equals("")?Integer.parseInt(idTray):null);
                tray.setCodEntidad(codEntidad!=null&& !codEntidad.equals("")?Integer.parseInt(codEntidad):null);
                tray.setNumExpediente(numExpediente);
                tray.setEjercicio(ejercicio);
                MeLanbide47Manager.getInstance().eliminarTrayectoriaActividades(tray,adaptador);
                filas=MeLanbide47Manager.getInstance().getListaTrayectoriaActividadesXNumExp(tray, adaptador);
            } catch (Exception ex) {
                log.error("Error al eliminar una trayectoria  actividades", ex);
                codigoOperacion = "1";
            }
        }
        catch(Exception ex)
        {
            log.error("Error al eliminar una trayectoria  actividades", ex);
            codigoOperacion = "2";
        }
        escribirResultadoGuardarTrayectoriaActividadesRequest(codigoOperacion, filas, response);
    }

    private void escribirResultadoGuardarTrayectoriaActividadesRequest(String codigoOperacion, List<OriTrayActividadVO> filas, HttpServletResponse response) {
        StringBuilder xmlSalida = new StringBuilder();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        xmlSalida.append("<TRAYECTORIAS>");
        for (OriTrayActividadVO tray : filas){
            xmlSalida.append("<TRAYECTORIA>");
                xmlSalida.append("<ORI_ACTIV_ID>");
                xmlSalida.append(tray.getCodIdActividad());
                xmlSalida.append("</ORI_ACTIV_ID>");
                xmlSalida.append("<ORI_ACTIV_EXP_EJE>");
                xmlSalida.append(tray.getEjercicio());
                xmlSalida.append("</ORI_ACTIV_EXP_EJE>");      
                xmlSalida.append("<ORI_ACTIV_NUMEXP>");
                xmlSalida.append(tray.getNumExpediente()!= null ? tray.getNumExpediente(): "-");
                xmlSalida.append("</ORI_ACTIV_NUMEXP>");
                xmlSalida.append("<ORI_ACTIV_COD_ENTIDAD>");
                xmlSalida.append(tray.getCodEntidad());
                xmlSalida.append("</ORI_ACTIV_COD_ENTIDAD>");
                xmlSalida.append("<ORI_ACTIV_ACTIVIDAD>");
                xmlSalida.append(tray.getDescActividad()!=null ? tray.getDescActividad():"");
                xmlSalida.append("</ORI_ACTIV_ACTIVIDAD>");
                xmlSalida.append("<ORI_ACTIV_ACTIVIDAD_EJE>");
                xmlSalida.append(tray.getEjerActividad());
                xmlSalida.append("</ORI_ACTIV_ACTIVIDAD_EJE>");
                xmlSalida.append("<ORI_ACTIV_DURACION>");
                xmlSalida.append(tray.getDuracion());         
                xmlSalida.append("</ORI_ACTIV_DURACION>");
                xmlSalida.append("<ORI_ENT_CIF>");
                xmlSalida.append(tray.getCifEntidad() !=null ? tray.getCifEntidad() : "-");
                xmlSalida.append("</ORI_ENT_CIF>");
                xmlSalida.append("<ORI_ENT_NOM>");
                xmlSalida.append(tray.getNombreEntidad() !=null ? tray.getNombreEntidad() : "-");
                xmlSalida.append("</ORI_ENT_NOM>");
                xmlSalida.append("<ORI_ACTIV_ACTIVIDAD_EJE_VALID>");
                xmlSalida.append(tray.getEjerActividadVal());
                xmlSalida.append("</ORI_ACTIV_ACTIVIDAD_EJE_VALID>");
                xmlSalida.append("<ORI_ACTIV_DURACION_VALID>");
                xmlSalida.append(tray.getDuracionVal());         
                xmlSalida.append("</ORI_ACTIV_DURACION_VALID>");
                
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
    public void guardarTrayectoriaActividades(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        log.debug("guardarTrayectoriaActividades - Begin()");
        String codigoOperacion = "0";
        List<OriTrayActividadVO> filas = new ArrayList<OriTrayActividadVO>();
        try
        {
            String idTray = request.getParameter("idTray");
            String codEntidad = request.getParameter("codEntidad");
            String descActividad = request.getParameter("actividad");
            String anio = request.getParameter("anio");
            String duracion = request.getParameter("duracion");
            String anioVal = request.getParameter("anioVal");
            String duracionVal = request.getParameter("duracionVal");
            try {
                OriTrayActividadVO tray = new OriTrayActividadVO();
                AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                tray.setCodIdActividad(idTray!=null && !idTray.equals("")?Integer.parseInt(idTray):null);
                tray.setCodEntidad(codEntidad!=null && !codEntidad.equals("")?Integer.parseInt(codEntidad):null);
                tray.setNumExpediente(numExpediente);
                tray.setEjercicio(MeLanbide47Utils.getEjercicioDeExpediente(numExpediente));
                tray.setDescActividad(descActividad);
                tray.setEjerActividad(Integer.parseInt(anio));
                tray.setDuracion(Integer.parseInt(duracion));
                tray.setEjerActividadVal(anioVal!=null && !anioVal.isEmpty() ? Integer.parseInt(anioVal) : null);
                tray.setDuracionVal(duracionVal!=null && !duracionVal.isEmpty() ? Integer.parseInt(duracionVal) : null);
                boolean result = MeLanbide47Manager.getInstance().guardarTrayectoriaActividades(tray,adaptador);
                if (result){
                    filas = MeLanbide47Manager.getInstance().getListaTrayectoriaActividadesXNumExp(tray, adaptador);
                }else{
                    codigoOperacion="3";
                }                
            } catch (Exception ex) {
                log.error("Error al guardar una trayectoria Actividades", ex);
                codigoOperacion = "1";
            }
        }
        catch(Exception ex)
        {
            log.error("Error al guardar una trayectoria Actividades", ex);
            codigoOperacion = "2";
        }
        escribirResultadoGuardarTrayectoriaActividadesRequest(codigoOperacion, filas, response);
        log.debug("guardarTrayectoriaActividades - End()"); 
    }
    
    public void eliminarAmbitoSolicitadoORI(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        String codigoOperacion = "0";
        List<FilaOriAmbitoSolicitadoVO> filas = new ArrayList<FilaOriAmbitoSolicitadoVO>();
        try
        {
           String idAmbitoSolicitado = request.getParameter("idAmbitoSolicitado");
            try {
                OriAmbitoSolicitadoVO ambiSol = new OriAmbitoSolicitadoVO();
                AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                ambiSol.setOriAmbSolCod(idAmbitoSolicitado!=null && idAmbitoSolicitado!=("") ? Integer.parseInt(idAmbitoSolicitado):null);
                ambiSol.setOriAmbSolNumExp(numExpediente);
                MeLanbide47Manager.getInstance().eliminarAmbitoSolicitadoORI(ambiSol,adaptador);
                filas=MeLanbide47Manager.getInstance().getAmbitosSolicitadosORI(numExpediente, adaptador);
            } catch (Exception ex) {
                log.error("Error al eliminar un ambito Solicitado trayectoria  actividades", ex);
                codigoOperacion = "1";
            }
        }
        catch(Exception ex)
        {
            log.error("Error al eliminar un ambito solicitado ", ex);
            codigoOperacion = "2";
        }
        escribirResultadoGuardarEliminarAmbitoSolicitadoResponse(codigoOperacion, filas, response);
    }

    private void escribirResultadoGuardarEliminarAmbitoSolicitadoResponse(String codigoOperacion, List<FilaOriAmbitoSolicitadoVO> filas, HttpServletResponse response) {
        StringBuilder xmlSalida = new StringBuilder();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        for (FilaOriAmbitoSolicitadoVO fila : filas) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ORI_AMB_SOL_COD>");
            xmlSalida.append(fila.getOriAmbSolCod());
            xmlSalida.append("</ORI_AMB_SOL_COD>");
            xmlSalida.append("<ORI_AMB_SOL_NUM_EXP>");
            xmlSalida.append(fila.getOriAmbSolNumExp());
            xmlSalida.append("</ORI_AMB_SOL_NUM_EXP>");
            xmlSalida.append("<ORI_AMB_SOL_TERHIS>");
            xmlSalida.append(fila.getOriAmbSolTerHis());
            xmlSalida.append("</ORI_AMB_SOL_TERHIS>");
            xmlSalida.append("<ORI_AMB_SOL_AMBITO>");
            xmlSalida.append(fila.getOriAmbSolAmbito());
            xmlSalida.append("</ORI_AMB_SOL_AMBITO>");
            xmlSalida.append("<ORI_AMB_SOL_NRO_BLOQUES>");
            xmlSalida.append(fila.getOriAmbSolNroBloques());
            xmlSalida.append("</ORI_AMB_SOL_NRO_BLOQUES>");
            xmlSalida.append("<ORI_AMB_SOL_NRO_UBIC>");
            xmlSalida.append(fila.getOriAmbSolNroUbic());
            xmlSalida.append("</ORI_AMB_SOL_NRO_UBIC>");
            xmlSalida.append("<ORI_AMB_SOL_TERHIS_DESC>");
            xmlSalida.append(fila.getOriAmbSolTerHisDesc());
            xmlSalida.append("</ORI_AMB_SOL_TERHIS_DESC>");
            xmlSalida.append("<ORI_AMB_SOL_AMBITO_DESC>");
            xmlSalida.append(fila.getOriAmbSolAmbitoDesc());
            xmlSalida.append("</ORI_AMB_SOL_AMBITO_DESC>");
            xmlSalida.append("</FILA>");
        }
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
    
    public String cargarAltaEdicionTrayectoria21(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("cargarAltaEdicionTrayectoria21 - Begin()" + codOrganizacion + " - " + codTramite + " - " + numExpediente);
        String url = "";
        int grupo = 0;
        String modoDatos = request.getParameter("modoDatos");
        String idTrayEntidad = request.getParameter("idTrayEntidad");
        grupo = (Integer.parseInt(request.getParameter("idGrupo")));
        String codEntidad = request.getParameter("codEntidad");
        String nombreEntidad = request.getParameter("nombre");
 
        int idioma = 1;
        
        UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
        if(usuario != null)
        {
            idioma = usuario.getIdioma();
        }
        MeLanbide47I18n meLanbide47I18n = MeLanbide47I18n.getInstance();
        String tituloPagina = "";
        String tituloCaja = "";
        request.setAttribute("modoDatos", modoDatos); // 0 Nuevo  / 1 Modificar 
        try {
            OriTrayectoriaEntidadVO trayModif = new OriTrayectoriaEntidadVO();
            trayModif.setNumExpediente(numExpediente);
            trayModif.setIdTrayEntidad(idTrayEntidad != null ? Integer.parseInt(idTrayEntidad) : null);
            trayModif.setIdConActGrupo(grupo);
            trayModif.setCodEntidad(codEntidad);
            trayModif.setNombreEntidad(nombreEntidad);

            if (modoDatos.equalsIgnoreCase("1")) {
                trayModif = MeLanbide47Manager.getInstance().getTrayEntidadXId(trayModif, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            }
            log.debug("Num exp: " + trayModif.getNumExpediente());
            log.debug("Programa: " + trayModif.getIdTrayEntidad());
            log.debug("Código Grupo: " + trayModif.getIdConActGrupo());
            log.debug("Código Entidad: " + trayModif.getCodEntidad());
            log.debug("Nombre Entidad: " + trayModif.getNombreEntidad());
            request.setAttribute("trayModif", trayModif);
        } catch (Exception ex) {
            log.error("Error al cargarAltaEdicionTrayectoria21 : ", ex);
        }
        url = "/jsp/extension/melanbide47/orientacion/trayectoria/convocatoria21/altaEdicionTrayectoria21.jsp";
        switch (grupo) {
            case 1:
                tituloPagina = meLanbide47I18n.getMensaje(idioma, "legend.trayectoria21.tituloPestana.grupo1");
                tituloCaja = meLanbide47I18n.getMensaje(idioma, "label.trayectoria21.tabla.programaCon");
                break;
            case 2:
                url = "/jsp/extension/melanbide47/orientacion/trayectoria/convocatoria21/altaEdicionGrupo2.jsp";
                tituloPagina = meLanbide47I18n.getMensaje(idioma, "label.trayectoria21.tituloPestana.grupo2");
                tituloCaja = meLanbide47I18n.getMensaje(idioma, "label.trayectoria21.tabla.programaCon");
                cargarCombosSubGrupos(codOrganizacion, idioma, request);
                break;
            case 3:
                tituloPagina = meLanbide47I18n.getMensaje(idioma, "label.trayectoria.tituloPestana.otrosProgramas");
                tituloCaja = meLanbide47I18n.getMensaje(idioma, "label.trayectoria21.tabla.programa");
                break;
            case 4:
                tituloPagina = meLanbide47I18n.getMensaje(idioma, "label.trayectoria.tituloPestana.actividades");
                tituloCaja = meLanbide47I18n.getMensaje(idioma, "label.trayectoria21.tabla.actividad");
                break;
            case 5:
                tituloPagina = meLanbide47I18n.getMensaje(idioma, "label.trayectoria21.tituloPestana.grupo5");
                tituloCaja = meLanbide47I18n.getMensaje(idioma, "label.trayectoria21.tituloPestana.programa");
                break;
            case 6:
                tituloPagina = meLanbide47I18n.getMensaje(idioma, "legend.trayectoria21.tituloPestana.grupo6");
                tituloCaja = meLanbide47I18n.getMensaje(idioma, "label.trayectoria21.tabla.programa");
                break;
        }
    
        request.setAttribute("tituloPagina", tituloPagina);
        request.setAttribute("tituloCaja", tituloCaja);
        return url;
    }

    public void eliminarTrayectoriaEntidad(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.debug("eliminarTrayectoriaEntidad - Begin()");
        String codigoOperacion = "0";
        List<OriTrayectoriaEntidadVO> trayectorias = new ArrayList<OriTrayectoriaEntidadVO>();
        try {
            String numExp = request.getParameter("numero");
            String idTrayEntidad = request.getParameter("idTrayEntidad");
            String codEntidad = request.getParameter("codEntidad");
            int grupo = (Integer.parseInt(request.getParameter("idGrupo")));
            try {
                OriTrayectoriaEntidadVO trayEntidad = new OriTrayectoriaEntidadVO();
                AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                trayEntidad.setIdTrayEntidad(idTrayEntidad != null ? Integer.parseInt(idTrayEntidad) : null);
                trayEntidad.setIdConActGrupo(grupo);
                trayEntidad.setCodEntidad(codEntidad);
                trayEntidad.setNumExpediente(numExp);
                int eliminado = MeLanbide47Manager.getInstance().eliminarTrayEntidad(trayEntidad, adapt);
                if (eliminado<1) {log.error("Error al eliminar un Programa/Trayectoria-21 ");
                codigoOperacion = "1";}
                trayectorias = MeLanbide47Manager.getInstance().getListaTrayectoriaEntidadXGrupo(trayEntidad, adapt);
            } catch (Exception ex) {
                log.error("Error al eliminar un Programa/Trayectoria-21 ", ex);
                codigoOperacion = "1";
            }
        } catch (NumberFormatException ex) {
            log.error("Error al eliminar una Trayectoria-21 ", ex);
            codigoOperacion = "3";
        }
        log.debug("eliminarTrayectoriaEntidad - End()");
        escribirRespuestaTrayectoriaEntidad(codigoOperacion, trayectorias, response);
    }

    public void guardarTrayectoriaEntidad(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.debug("guardarTrayectoriaEntidad - Begin()");
        String codigoOperacion = "0";
        List<OriTrayectoriaEntidadVO> listaTrayEntidades = new ArrayList<OriTrayectoriaEntidadVO>();
        try {
            //  definir variables
            String idTrayEntidad = request.getParameter("idTrayEntidad");
            String numExp = request.getParameter("numero");
            String codEntidad = request.getParameter("codEntidad");
            String descActividad = request.getParameter("actividad");
            String fechaInicio = (String) request.getParameter("fecIni");
            String fechaFin = (String) request.getParameter("fecFin");
            String numMeses = (String) request.getParameter("numMeses");
            String fechaInicioVal = (String) request.getParameter("fecIniVal");
            String fechaFinVal = (String) request.getParameter("fecFinVal");
            String numMesesVal = (String) request.getParameter("numMesesVal");
            int grupo = (Integer.parseInt(request.getParameter("idGrupo")));
            int subGrupo = 0;
            int experiencia = 9;
            int experienciaVal = 9;

            if (grupo == 2) {
                subGrupo = (Integer.parseInt(request.getParameter("idSubGrupo")));
                if (request.getParameter("experiencia") != null && !"".equals(request.getParameter("experiencia"))) {
                    experiencia = (Integer.parseInt(request.getParameter("experiencia")));
                }
                if (request.getParameter("experienciaVal") != null && !"".equals(request.getParameter("experienciaVal"))) {
                    experienciaVal = (Integer.parseInt(request.getParameter("experienciaVal")));
                }
            }

            boolean validado = request.getParameter("validado").equalsIgnoreCase("true");

            try {
                OriTrayectoriaEntidadVO trayEntidad = new OriTrayectoriaEntidadVO();
                AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                //  cargar datos en  objeto
                trayEntidad.setIdTrayEntidad(idTrayEntidad != null && !idTrayEntidad.equals("") ? Integer.parseInt(idTrayEntidad) : null);
                trayEntidad.setIdConActGrupo(grupo);
                trayEntidad.setNumExpediente(numExp);
                trayEntidad.setCodEntidad(codEntidad);
                trayEntidad.setDescActividad(descActividad);
                trayEntidad.setFechaInicio(new java.sql.Date(dateFormat.parse(fechaInicio).getTime()));
                trayEntidad.setFechaFin(new java.sql.Date(dateFormat.parse(fechaFin).getTime()));
                trayEntidad.setNumMeses(Integer.parseInt(numMeses));
                if (grupo == 2) {
                    trayEntidad.setIdConActSubgrupo(subGrupo);
                    if (experiencia != 9) {
                        trayEntidad.setTieneExperiencia(experiencia);
                    }
                    if (experienciaVal != 9) {
                        trayEntidad.setTieneExperienciaVal(experienciaVal);
                    }
                }
                if (fechaInicioVal != null && !"".equals(fechaInicioVal)) {
                    trayEntidad.setFechaInicioVal(new java.sql.Date(dateFormat.parse(fechaInicioVal).getTime()));
                }
                if (fechaFinVal != null && !"".equals(fechaFinVal)) {
                    trayEntidad.setFechaFinVal(new java.sql.Date(dateFormat.parse(fechaFinVal).getTime()));
                }
                if (numMesesVal != null && !"".equals(numMesesVal)) {
                    trayEntidad.setNumMesesVal(Integer.parseInt(numMesesVal));
                }

                boolean result = MeLanbide47Manager.getInstance().guardarTrayEntidad(trayEntidad, validado, adapt);
                if (result) {
                    listaTrayEntidades = MeLanbide47Manager.getInstance().getListaTrayectoriaEntidadXGrupo(trayEntidad, adapt);
                } else {
                    codigoOperacion = "3";
                }
            } catch (Exception ex) {
                log.error("Error al guardar una Trayectoria-21 ", ex);
                codigoOperacion = "1";
            }
        } catch (Exception ex) {
            log.error("Error al guardar una Trayectoria-21 ", ex);
            codigoOperacion = "2";
        }
        log.debug("guardarTrayectoriaEntidad - End()");
        escribirRespuestaTrayectoriaEntidad(codigoOperacion, listaTrayEntidades, response);
    }
    
    public void refrescarPestanaTrayectoria21(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        List<OriTrayectoriaEntidadVO> listaGrupo = new ArrayList<OriTrayectoriaEntidadVO>();
        String codigoOperacion = "0";
        int grupo = 0;
        String[] datosEntidad = null;
        String codEntidad = request.getParameter("codEntidad");
        grupo = Integer.parseInt(request.getParameter("idGrupo"));
        try {
            if (codEntidad != null && !codEntidad.equals("")) {
                OriTrayectoriaEntidadVO trayEntidad = new OriTrayectoriaEntidadVO();
                trayEntidad.setIdConActGrupo(grupo);
                trayEntidad.setCodEntidad(codEntidad);
                trayEntidad.setNumExpediente(numExpediente);
                request.setAttribute("codEntidad", codEntidad);
                AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                datosEntidad = MeLanbide47Manager.getInstance().getCifNombreEntidad(numExpediente, codEntidad, adapt);
                if (datosEntidad != null && datosEntidad.length == 2) {
                    request.setAttribute("cifEntidad", datosEntidad[0]);
                    request.setAttribute("nombreEntidad", datosEntidad[1]);
                } else {
                    codigoOperacion = "3";
                }
                listaGrupo = MeLanbide47Manager.getInstance().getListaTrayectoriaEntidadXGrupo(trayEntidad, adapt);
            } else {
                codigoOperacion = "1";
            }
        } catch (Exception ex) {
            log.error("Error : " + ex.getMessage(), ex);
            codigoOperacion = "2";
        }
        escribirRespuestaTrayectoriaEntidad(codigoOperacion, listaGrupo, response);
    }
    
    public void refrescarPestanaResumen(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        List<OriTrayectoriaEntidadVO> listaResumen = new ArrayList<OriTrayectoriaEntidadVO>();
        String codigoOperacion = "0";        
        String[] datosEntidad = null;
        String codEntidad = request.getParameter("codEntidad");
        try {
            if (codEntidad != null && !codEntidad.equals("")) {
                request.setAttribute("codEntidad", codEntidad);
                AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                datosEntidad = MeLanbide47Manager.getInstance().getCifNombreEntidad(numExpediente, codEntidad, adapt);
                if (datosEntidad != null && datosEntidad.length == 2) {
                    request.setAttribute("cifEntidad", datosEntidad[0]);
                    request.setAttribute("nombreEntidad", datosEntidad[1]);
                } else {
                    codigoOperacion = "3";
                }
                listaResumen = MeLanbide47Manager.getInstance().getTotalTrayectoriaEntidadXExp(numExpediente, adapt);
            } else {
                codigoOperacion = "3";
            }

        } catch (Exception ex) {
            log.error("Error : " + ex.getMessage(), ex);
            codigoOperacion = "1";
        }
        escribirRespuestaTrayectoriaEntidad(codigoOperacion, listaResumen, response);
    }
     
    private void escribirRespuestaTrayectoriaEntidad(String codigoOperacion, List<OriTrayectoriaEntidadVO> trayectorias, HttpServletResponse response) {
        StringBuilder xmlSalida = new StringBuilder();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        xmlSalida.append("<TRAYECTORIAS>");
        for (OriTrayectoriaEntidadVO trayEntidad : trayectorias) {
            xmlSalida.append("<ACTIVIDAD>");
            xmlSalida.append("<ID>");
            xmlSalida.append(trayEntidad.getIdTrayEntidad());
            xmlSalida.append("</ID>");
            xmlSalida.append("<TRAYIDFKORIPROGCONVACTGRUPO>");
            xmlSalida.append(trayEntidad.getIdConActGrupo().toString());
            xmlSalida.append("</TRAYIDFKORIPROGCONVACTGRUPO>");
            xmlSalida.append("<TRAYIDFKORIPROGCONVACTSUBGRPRE>");
            xmlSalida.append(trayEntidad.getIdConActSubgrupo() != null ? trayEntidad.getIdConActSubgrupo().toString() : "-");
            xmlSalida.append("</TRAYIDFKORIPROGCONVACTSUBGRPRE>");
            xmlSalida.append("<TRAYCODIGOENTIDAD>");
            xmlSalida.append(trayEntidad.getCodEntidad());
            xmlSalida.append("</TRAYCODIGOENTIDAD>");
            xmlSalida.append("<TRAYDESCRIPCION>");
            xmlSalida.append(trayEntidad.getDescActividad());
            xmlSalida.append("</TRAYDESCRIPCION>");
            xmlSalida.append("<TRAYTIENEEXPERIENCIA>");
            xmlSalida.append(trayEntidad.getTieneExperiencia()!= null ? trayEntidad.getTieneExperiencia().toString() : "-");
            xmlSalida.append("</TRAYTIENEEXPERIENCIA>");
            xmlSalida.append("<TRAYFECHAINICIO>");
            xmlSalida.append(dateFormat.format(trayEntidad.getFechaInicio()));
            xmlSalida.append("</TRAYFECHAINICIO>");
            xmlSalida.append("<TRAYFECHAFIN>");
            xmlSalida.append(dateFormat.format(trayEntidad.getFechaFin()));
            xmlSalida.append("</TRAYFECHAFIN>");
            xmlSalida.append("<TRAYNUMEROMESES>");
            xmlSalida.append(trayEntidad.getNumMeses());
            xmlSalida.append("</TRAYNUMEROMESES>");
            xmlSalida.append("<TRAYTIENEEXPERIENCIAVAL>");
            xmlSalida.append(trayEntidad.getTieneExperienciaVal()!= null ? trayEntidad.getTieneExperienciaVal().toString() : "-");
            xmlSalida.append("</TRAYTIENEEXPERIENCIAVAL>");
            xmlSalida.append("<TRAYFECHAINICIOVAL>");
            xmlSalida.append(trayEntidad.getFechaInicioVal() != null ? dateFormat.format(trayEntidad.getFechaInicioVal()) : "");
            xmlSalida.append("</TRAYFECHAINICIOVAL>");
            xmlSalida.append("<TRAYFECHAFINVAL>");
            xmlSalida.append(trayEntidad.getFechaFinVal() != null ? dateFormat.format(trayEntidad.getFechaFinVal()) : "");
            xmlSalida.append("</TRAYFECHAFINVAL>");
            xmlSalida.append("<TRAYNUMEROMESESVAL>");
            xmlSalida.append(trayEntidad.getNumMesesVal() != null ? trayEntidad.getNumMesesVal() : "-");
            xmlSalida.append("</TRAYNUMEROMESESVAL>");
            xmlSalida.append("<ORI_ENT_CIF>");
            xmlSalida.append(trayEntidad.getCifEntidad() != null ? trayEntidad.getCifEntidad() : "-");
            xmlSalida.append("</ORI_ENT_CIF>");
            xmlSalida.append("<ORI_ENT_NOM>");
            xmlSalida.append(trayEntidad.getNombreEntidad() != null ? trayEntidad.getNombreEntidad() : "-");
            xmlSalida.append("</ORI_ENT_NOM>");
            xmlSalida.append("</ACTIVIDAD>");
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

    private void cargarCombosSubGrupos(int codOrganizacion, int idioma, HttpServletRequest request) {
        List<SelectItem> listaSubgrupos = new ArrayList<SelectItem>();
        try {
            listaSubgrupos = MeLanbide47Manager.getInstance().getListaSubgrupos(idioma,this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        } catch (Exception e) {
            log.error("Se ha producido un error al recuperar los SubGrupos " + e);
        }
        request.setAttribute("listaSubgrupos", listaSubgrupos);
    }

    public void guardarMesesValidados(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("  >>>> Entramos en guardarMesesValidados de " + this.getClass().getSimpleName() + " - " + numExpediente);
        String codigoOperacion = "0";
        try {
            AdaptadorSQLBD adapt = null;
            try {
                adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            } catch (Exception ex) {
                log.error("Error : " + ex.getMessage(), ex);
                codigoOperacion = "1";
            }
            try {
                String mSol = null;
                String mVal = null;
                mSol = request.getParameter("mesesSolicitados");
                mVal = request.getParameter("mesesValidados");
                int mesesSol = 0;
                if (mSol != null && !mSol.equals("") && !mSol.equalsIgnoreCase("null")) {
                    mesesSol = Integer.parseInt(mSol);
                }
                Double mesesVal = null;
                if (mVal != null && !mVal.equals("") && !mVal.equalsIgnoreCase("null")) {
                    mVal=mVal.replace(",", ".");
                    mesesVal = Double.valueOf(mVal);
                }
                Double puntuacion = (mesesVal!=null?mesesVal:0) * .2;
                try {
                    boolean actualiza = MeLanbide47Manager.getInstance().guardarPuntuacionTrayectorias(codOrganizacion, numExpediente, puntuacion, adapt);
                    boolean resultado = MeLanbide47Manager.getInstance().guardarMesesTrayectorias(codOrganizacion, numExpediente, mesesSol, mesesVal, adapt);
                    if (resultado == false) {
                        codigoOperacion = "1";
                    }
                } catch (Exception ex) {
                    log.error("Error : " + ex.getMessage(), ex);
                    codigoOperacion = "1";
                }
            } catch (NumberFormatException ex) {
                log.error("Error : " + ex.getMessage(), ex);
                codigoOperacion = "3";
            }
        } catch (Exception ex) {
            log.error("Error : " + ex.getMessage(), ex);
            codigoOperacion = "2";
        }
        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("error", codigoOperacion);
        this.retornarJSON(new Gson().toJson(resultado), response);
    }

    /**
     * Metodo que valida todos los datos de un Grupo de Trayectoria
     *
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @param request
     * @param response
     */
    public void validarPestanaTrayectoria21(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("  >>>> Entramos en validarDatosTrayectoria21 de " + this.getClass().getSimpleName() + " - " + numExpediente);
        String codigoOperacion = "0";
        int grupo = 0;
        try {
            AdaptadorSQLBD adapt = null;
            try {
                adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            } catch (Exception ex) {
                log.error("Error : " + ex.getMessage(), ex);
                codigoOperacion = "1";
            }
            try {
                grupo = Integer.parseInt(request.getParameter("idGrupo"));
            } catch (NumberFormatException ex) {
                log.error("Error : " + ex.getMessage(), ex);
                codigoOperacion = "3";
            }
            try {
                if (grupo != 0) {
                    boolean resultado = MeLanbide47Manager.getInstance().validarDatosTrayectoria21(codOrganizacion, numExpediente, grupo, adapt);
                    if (resultado == false) {
                        codigoOperacion = "1";
                    }
                } else {
                    codigoOperacion = "1";
                }
            } catch (Exception ex) {
                log.error("Error : " + ex.getMessage(), ex);
                codigoOperacion = "4";
            }
        } catch (Exception ex) {
            log.error("Error : " + ex.getMessage(), ex);
            codigoOperacion = "2";
        }
        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("error", codigoOperacion);
        this.retornarJSON(new Gson().toJson(resultado), response);

        //      escribirResultadoGuardarTrayectoriaORIRequest(codigoOperacion, response);
    }

    public void getMostrarDatosCertificadosCalidadEntidad(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        Integer idEntidad = request.getParameter("idEntidad")!=null && !request.getParameter("idEntidad").isEmpty() && !request.getParameter("idEntidad").equalsIgnoreCase("null") ? Integer.valueOf(request.getParameter("idEntidad")):null;
        Integer idBDConvocatoriaExpediente = request.getParameter("idBDConvocatoriaExpediente")!=null && !request.getParameter("idBDConvocatoriaExpediente").isEmpty() && !request.getParameter("idBDConvocatoriaExpediente").equalsIgnoreCase("null") ? Integer.valueOf(request.getParameter("idBDConvocatoriaExpediente")):null;
        List<OriEntidadCertCalidad> listaCertificadosCalidadEntidad = new ArrayList<OriEntidadCertCalidad>();
        try {
            listaCertificadosCalidadEntidad = meLanbide47ManagerOriEntidadCertCalidad.getListaOriEntidadCertCalidadByCodEntidad(idEntidad, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        } catch (Exception ex) {
            log.error("Error getMostrarDatosCertificadosCalidadEntidad"+ ex.getMessage(), ex);
        }
        parsearRespuestasEnviarJSON(request,response,listaCertificadosCalidadEntidad);
    }

    public void getListaOriCertCalidadPuntuacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        List<OriCertCalidadPuntuacion> listaCertificadosCalidadPuntuacion = new ArrayList<OriCertCalidadPuntuacion>();
        try {
            Integer idBDConvocatoriaExpediente = request.getParameter("idBDConvocatoriaExpediente")!=null && !request.getParameter("idBDConvocatoriaExpediente").isEmpty() && !request.getParameter("idBDConvocatoriaExpediente").equalsIgnoreCase("null") ? Integer.valueOf(request.getParameter("idBDConvocatoriaExpediente")):null;
            listaCertificadosCalidadPuntuacion = meLanbide47ManagerOriCertCalidadPuntuacion.getOriCertCalidadPuntuacionByIdConvocatoria(idBDConvocatoriaExpediente,getAdaptSQLBD(String.valueOf(codOrganizacion)));
        } catch (Exception ex) {
            log.error("Error getListaOriCertCalidad "+ ex.getMessage(), ex);
        }
        parsearRespuestasEnviarJSON(request,response,listaCertificadosCalidadPuntuacion);
    }

    public void getListaOriCompIgualdadPuntuacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        List<OriCompIgualdadPuntuacion> listaOriCompIgualdadPuntuacion = new ArrayList<OriCompIgualdadPuntuacion>();
        try {
            Integer idBDConvocatoriaExpediente = request.getParameter("idBDConvocatoriaExpediente")!=null && !request.getParameter("idBDConvocatoriaExpediente").isEmpty() && !request.getParameter("idBDConvocatoriaExpediente").equalsIgnoreCase("null") ? Integer.valueOf(request.getParameter("idBDConvocatoriaExpediente")):null;
            listaOriCompIgualdadPuntuacion = meLanbide47ManagerOriCompIgualdadPuntuacion.getOriCompIgualdadPuntuacionByIdConvocatoria(idBDConvocatoriaExpediente,getAdaptSQLBD(String.valueOf(codOrganizacion)));
        } catch (Exception ex) {
            log.error("Error getListaOriCertCalidad "+ ex.getMessage(), ex);
        }
        parsearRespuestasEnviarJSON(request,response,listaOriCompIgualdadPuntuacion);
    }

    /**
     * Método llamado para devolver un String en formato JSON al cliente que ha
     * realiza la petición a alguna de las operaciones de este action
     *
     * @param json: String que contiene el JSON a devolver
     * @param response: Objeto de tipo HttpServletResponse a través del cual se
     * devuelve la salida al cliente que ha realizado la solicitud
     */
    private void retornarJSON(String json, HttpServletResponse response) {
        try {
            if (json != null) {
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                out.print(json);
                out.flush();
                out.close();
            }
        } catch (IOException e) {
             log.error("Error preparando respuesta " + e.getMessage(), e);
        }
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
                respuestaJsonString = MeLanbide47Utils.decodeText(respuestaJsonString, request.getCharacterEncoding());
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

}
