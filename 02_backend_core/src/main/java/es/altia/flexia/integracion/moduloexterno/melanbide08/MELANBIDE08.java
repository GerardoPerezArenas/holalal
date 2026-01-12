package es.altia.flexia.integracion.moduloexterno.melanbide08;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import es.altia.agora.business.documentos.DocumentoManager;
import es.altia.agora.business.documentos.helper.CodigoSeguroVerificacionHelper;
import es.altia.agora.business.editor.mantenimiento.persistence.manual.DocumentosAplicacionDAO;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.sge.DocumentoTramiteVO;
import es.altia.agora.business.sge.MetadatosDocumentoVO;
import es.altia.agora.business.sge.persistence.DefinicionProcedimientosManager;
import es.altia.agora.business.sge.persistence.DefinicionTramitesManager;
import es.altia.agora.business.sge.plugin.documentos.AlmacenDocumento;
import es.altia.agora.business.sge.plugin.documentos.AlmacenDocumentoTramitacionFactoria;
import es.altia.agora.business.sge.plugin.documentos.exception.AlmacenDocumentoTramitacionException;
import es.altia.agora.business.sge.plugin.documentos.vo.Documento;
import es.altia.agora.business.sge.plugin.documentos.vo.DocumentoFirma;
import es.altia.agora.business.sge.plugin.documentos.vo.DocumentoTramitacionFactoria;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.DocumentConversionException;
import es.altia.common.exception.FormatNotSupportedException;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.flexia.integracion.moduloexterno.melanbide08.manager.MeLanbide08LogJobManager;
import es.altia.flexia.integracion.moduloexterno.melanbide08.manager.MeLanbide08Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide08.vo.ComboVO;
import es.altia.flexia.integracion.moduloexterno.melanbide08.vo.FiltrosVO;
import es.altia.flexia.integracion.moduloexterno.melanbide08.vo.RetornoResultadosFiltros;
import es.altia.flexia.integracion.moduloexterno.melanbide08.vo.TramitacionExpedientesValueObject;
import java.util.Date;
import java.util.List;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;
import es.altia.technical.PortableContext;
import es.altia.flexia.integracion.moduloexterno.melanbide08.util.JODConverterHelper;
import es.altia.flexia.integracion.moduloexterno.melanbide08.util.RequestInformationHolder;
import es.altia.util.commons.MimeTypes;
import es.altia.util.commons.WebOperations;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.documentos.DocumentOperations;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import javax.sql.DataSource;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;



public class MELANBIDE08 extends ModuloIntegracionExterno{
private static final Logger log = LogManager.getLogger(MELANBIDE08.class);
SimpleDateFormat formatDateLog = new SimpleDateFormat("yyyyMMddHHmmssSSS"); 
  protected static Config registroConf = ConfigServiceHelper.getConfig("Registro");

   public String cargarPantallaPrincipalConsultaEstadoFirma(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("cargarPantallaPrincipalConsultaEstadoFirma - Begin()" + formatDateLog.format(new Date()));
        String respuestaServicio = "/jsp/extension/melanbide08/pantallaprincipalconsultar.jsp";
         AdaptadorSQLBD adapt = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (SQLException ex) {
            log.error("Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }     
 
        int idiomaUsuario = 1;
       

        try {
            if (request.getParameter("idioma") != null && !request.getParameter("idioma").isEmpty()) {
                try {
                    idiomaUsuario = Integer.parseInt(request.getParameter("idioma"));
                } catch (Exception ex) {
                    log.error("Error al parsear request.getParameter(idioma) a Entero. Asignamos idioma por defecto 1 Castellano.", ex);
                }
            }
            
            HttpSession session = request.getSession();
            UsuarioValueObject usuario = new UsuarioValueObject();
            try {
                if (session != null) {
                    if (usuario != null) {
                        usuario = (UsuarioValueObject) session.getAttribute("usuario");
                    }
                }
            }catch(Exception ex){
                log.error("Error al recuperar los datos del Usuario desde la session", ex);
            }
            
       
        List<ComboVO> listaProcedimiento = MeLanbide08Manager.getInstance().getComboProcedimiento(codOrganizacion,usuario.getIdUsuario(), adapt);
        request.setAttribute("listaProcedimiento",listaProcedimiento);
        } catch (Exception ex) {
            log.error("Se ha producido un error a cargar la pantalla principal de Consulta de estado de firma de documentos ", ex);
            request.setAttribute("mensajeInicial", "Se ha presentado un error al cargar la pantalla de consulta de estado de firma de documentos ...Contacte con el ADMIN para mas detalles. " + ex.getMessage());
        }
        log.info("cargarPantallaPrincipalConsultaEstadoFirma - End()" + formatDateLog.format(new Date()) + " " + respuestaServicio);
        return respuestaServicio;
    }

   public String cargarPantallaPrincipalConsultaEnviarFirma(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("cargarPantallaPrincipalConsultaEstadoFirma - Begin()" + formatDateLog.format(new Date()));
        String respuestaServicio = "/jsp/extension/melanbide08/pantallaprincipalfirmar.jsp";
         AdaptadorSQLBD adapt = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (SQLException ex) {
            log.error("Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }     
 
        int idiomaUsuario = 1;
       

        try {
            if (request.getParameter("idioma") != null && !request.getParameter("idioma").isEmpty()) {
                try {
                    idiomaUsuario = Integer.parseInt(request.getParameter("idioma"));
                } catch (Exception ex) {
                    log.error("Error al parsear request.getParameter(idioma) a Entero. Asignamos idioma por defecto 1 Castellano.", ex);
                }
            }
            
            HttpSession session = request.getSession();
            UsuarioValueObject usuario = new UsuarioValueObject();
            try {
                if (session != null) {
                    if (usuario != null) {
                        usuario = (UsuarioValueObject) session.getAttribute("usuario");
                    }
                }
            }catch(Exception ex){
                log.error("Error al recuperar los datos del Usuario desde la session", ex);
            }
            
       
        List<ComboVO> listaProcedimiento = MeLanbide08Manager.getInstance().getComboProcedimiento(codOrganizacion,usuario.getIdUsuario(), adapt);
        request.setAttribute("listaProcedimiento",listaProcedimiento);
        } catch (Exception ex) {
            log.error("Se ha producido un error a cargar la pantalla principal de Consulta de estado de firma de documentos ", ex);
            request.setAttribute("mensajeInicial", "Se ha presentado un error al cargar la pantalla de consulta de estado de firma de documentos ...Contacte con el ADMIN para mas detalles. " + ex.getMessage());
        }
        log.info("cargarPantallaPrincipalConsultaEstadoFirma - End()" + formatDateLog.format(new Date()) + " " + respuestaServicio);
        return respuestaServicio;
    }

   
   public String cargarPantallaPrincipalConsultarLog(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("cargarPantallaPrincipalConsultarLog - Begin()" + formatDateLog.format(new Date()));
        String respuestaServicio = "/jsp/extension/melanbide08/pantallaprincipalconsultarlog.jsp";
         AdaptadorSQLBD adapt = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (SQLException ex) {
            log.error("Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }     
 
        int idiomaUsuario = 1;
       

        try {
            if (request.getParameter("idioma") != null && !request.getParameter("idioma").isEmpty()) {
                try {
                    idiomaUsuario = Integer.parseInt(request.getParameter("idioma"));
                } catch (Exception ex) {
                    log.error("Error al parsear request.getParameter(idioma) a Entero. Asignamos idioma por defecto 1 Castellano.", ex);
                }
            }
            
            HttpSession session = request.getSession();
            UsuarioValueObject usuario = new UsuarioValueObject();
            try {
                if (session != null) {
                    if (usuario != null) {
                        usuario = (UsuarioValueObject) session.getAttribute("usuario");
                    }
                }
            }catch(Exception ex){
                log.error("Error al recuperar los datos del Usuario desde la session", ex);
            }
            
       
        List<ComboVO> listaProcedimiento = MeLanbide08Manager.getInstance().getComboProcedimiento(codOrganizacion,usuario.getIdUsuario(), adapt);
        request.setAttribute("listaProcedimiento",listaProcedimiento);
        } catch (Exception ex) {
            log.error("Se ha producido un error a cargar la pantalla principal de Consulta de estado de log de documentos ", ex);
            request.setAttribute("mensajeInicial", "Se ha presentado un error al cargar la pantalla de consulta de estado de log de documentos ...Contacte con el ADMIN para mas detalles. " + ex.getMessage());
        }
        log.info("cargarPantallaPrincipalConsultarLog - End()" + formatDateLog.format(new Date()) + " " + respuestaServicio);
        return respuestaServicio;
    }
   
      
      public void cargarDatosPrincipalConsultaEstadoFirmaFiltros(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("cargarDatosPrincipalConsultaEstadoFirmaFiltros - Begin()" + new Date().toString());
        AdaptadorSQLBD adapt = null;
        RetornoResultadosFiltros retorno = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (SQLException ex) {
            log.error("Error al recuperar el adaptador getAdaptSQLBD ", ex);
        } 

 
        int idiomaUsuario = 1;
        int startTable = 0;
        int finishTable = 0;
        int numEntries = 0;
        int draw = 0;
        String ejercicio = "";
        String procedimiento = "";
        String numeroExpediente = "";
        String estadoFirma = "";
        String tipoDocumento = "";
        String fechaEnvioPeticion = "";
        String fechaEnvioPeticionDesde = "";
        String fechaEnvioPeticionHasta = "";
        String documentoNotificado = "";
        
        try {   
            if (request.getParameter("idioma") != null && !request.getParameter("idioma").isEmpty()) {
                try {
                    idiomaUsuario = Integer.parseInt(request.getParameter("idioma"));
                } catch (Exception ex) {
                    log.error("Error al parsear request.getParameter(idioma) a Entero. Asignamos idioma por defecto 1 Castellano.", ex);
                }
            }
        
            //Recoger los parametros
            startTable = Integer.parseInt(request.getParameter("start"));
            finishTable = startTable + Integer.parseInt(request.getParameter("length"));
            numEntries = Integer.parseInt(request.getParameter("length"));
            draw = Integer.parseInt(request.getParameter("draw"));
            ejercicio = (String) request.getParameter("ejercicio");
            procedimiento = (String) request.getParameter("procedimiento");
             numeroExpediente = (String) request.getParameter("numeroExpediente");
            estadoFirma = (String) request.getParameter("estadoFirma");
            tipoDocumento = (String) request.getParameter("tipoDocumento");
            fechaEnvioPeticion = (String) request.getParameter("fechaEnvioPeticion");
            fechaEnvioPeticionDesde = (String) request.getParameter("fechaEnvioPeticionDesde");
            fechaEnvioPeticionHasta = (String) request.getParameter("fechaEnvioPeticionHasta");
            documentoNotificado = (String) request.getParameter("documentoNotificado");
        
         FiltrosVO filtros = new FiltrosVO(startTable, finishTable, numEntries, draw,
                    ejercicio, procedimiento,numeroExpediente, estadoFirma, tipoDocumento, fechaEnvioPeticion,documentoNotificado,fechaEnvioPeticionDesde,fechaEnvioPeticionHasta);

            HttpSession session = request.getSession();
            UsuarioValueObject usuario = new UsuarioValueObject();
            try {
                if (session != null) {
                    if (usuario != null) {
                        usuario = (UsuarioValueObject) session.getAttribute("usuario");
                    }
                }
            }catch(Exception ex){
                log.error("Error al recuperar los datos del Usuario desde la session", ex);
            }


         List<ComboVO> listaProcedimiento = MeLanbide08Manager.getInstance().getComboProcedimiento(codOrganizacion,usuario.getIdUsuario(), adapt);
         //cargar lista de resultados 
         retorno = MeLanbide08Manager.getInstance().getLog(codOrganizacion, adapt, filtros,listaProcedimiento);
         
         GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.serializeNulls();
            Gson gson = gsonBuilder.create();
            String sResponse = gson.toJson(retorno);
            try {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                out.print(sResponse);
                out.flush();
                out.close();
            } catch (Exception e) {
                log.error("Error preparando respuesta " + e.getMessage(), e);
                e.printStackTrace();
            }

            log.info("Retorno size: " + retorno.getLstRegistros().size());

        } catch (Exception ex) {
            log.error("Se ha producido un error a cargar la pantalla de consulta estado firmas " + numExpediente, ex);
            retorno = null;
            request.setAttribute("mensajeInicial", "Se ha presentado un error al cargar la pantalla ...Contacte con el ADMIN para mas detalles. " + ex.getMessage());
        }
        log.info("cargarDatosPrincipalConsultaEstadoFirmaFiltros - End()" + new Date().toString());

      }
        
      
      
      
      public void cargarDatosConsultaEstadoLogJobFirma(int codOrganizacion , int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("cargarDatosConsultaEstadoLogJobFirma - Begin()" + new Date().toString());
        AdaptadorSQLBD adapt = null;
        RetornoResultadosFiltros retorno = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (SQLException ex) {
            log.error("Error al recuperar el adaptador getAdaptSQLBD ", ex);
        } 

        String ejercicio = "";
        String procedimiento = "";
        String numeroExpediente = "";
        String estadoFirma = "";
        String tipoDocumento = "";
        String fechaEnvioPeticion = "";
        String fechaEnvioPeticionDesde = "";
        String fechaEnvioPeticionHasta = "";
        String documentoNotificado = "";
 
        int idiomaUsuario = 1;
        int startTable = 0;
        int finishTable = 0;
        int numEntries = 0;
        int draw = 0;
        ejercicio = (String) request.getParameter("ejercicio");
            procedimiento = (String) request.getParameter("procedimiento");
             numeroExpediente = (String) request.getParameter("numeroExpediente");
            estadoFirma = (String) request.getParameter("estadoFirma");
            tipoDocumento = (String) request.getParameter("tipoDocumento");
            fechaEnvioPeticion = (String) request.getParameter("fechaEnvioPeticion");
            fechaEnvioPeticionDesde = (String) request.getParameter("fechaEnvioPeticionDesde");
            fechaEnvioPeticionHasta = (String) request.getParameter("fechaEnvioPeticionHasta");
            documentoNotificado = (String) request.getParameter("documentoNotificado");
        
        try {   
            if (request.getParameter("idioma") != null && !request.getParameter("idioma").isEmpty()) {
                try {
                    idiomaUsuario = Integer.parseInt(request.getParameter("idioma"));
                } catch (Exception ex) {
                    log.error("Error al parsear request.getParameter(idioma) a Entero. Asignamos idioma por defecto 1 Castellano.", ex);
                }
            }
        
            //Recoger los parametros
            startTable = Integer.parseInt(request.getParameter("start"));
            finishTable = startTable + Integer.parseInt(request.getParameter("length"));
            numEntries = Integer.parseInt(request.getParameter("length"));
            draw = Integer.parseInt(request.getParameter("draw"));
   
        
         FiltrosVO filtros = new FiltrosVO(startTable, finishTable, numEntries, draw,
                 ejercicio, procedimiento,numeroExpediente, estadoFirma, tipoDocumento, fechaEnvioPeticion,documentoNotificado,fechaEnvioPeticionDesde,fechaEnvioPeticionHasta);


            HttpSession session = request.getSession();
            UsuarioValueObject usuario = new UsuarioValueObject();
            try {
                if (session != null) {
                    if (usuario != null) {
                        usuario = (UsuarioValueObject) session.getAttribute("usuario");
                    }
                }
            }catch(Exception ex){
                log.error("Error al recuperar los datos del Usuario desde la session", ex);
            }

         List<ComboVO> listaProcedimiento = MeLanbide08Manager.getInstance().getComboProcedimiento(codOrganizacion,usuario.getIdUsuario(), adapt);
         //cargar lista de resultados 
         retorno = MeLanbide08LogJobManager.getInstance().getLog(codOrganizacion, adapt, filtros,listaProcedimiento);
         
         GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.serializeNulls();
            Gson gson = gsonBuilder.create();
            String sResponse = gson.toJson(retorno);
            try {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                out.print(sResponse);
                out.flush();
                out.close();
            } catch (Exception e) {
                log.error("Error preparando respuesta " + e.getMessage(), e);
                e.printStackTrace();
            }

            log.info("Retorno size: " + retorno.getLstRegistros().size());

        } catch (Exception ex) {
            log.error("Se ha producido un error a cargar la pantalla de consulta estado firmas log" , ex);
            retorno = null;
            request.setAttribute("mensajeInicial", "Se ha presentado un error al cargar la pantalla ...Contacte con el ADMIN para mas detalles. " + ex.getMessage());
        }
        log.info("cargarDatosConsultaEstadoLogJobFirma - End()" + new Date().toString());

      }
        
      
      
   
       public void enviarAFirmarFiltrado(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("cargarDatosPrincipalConsultaEstadoFirmaFiltrosSinPaginar - Begin()" + new Date().toString());
        AdaptadorSQLBD adapt = null;
        RetornoResultadosFiltros retorno = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (SQLException ex) {
            log.error("Error al recuperar el adaptador getAdaptSQLBD ", ex);
        } 

 
        int idiomaUsuario = 1;
        int startTable = 0;
        int finishTable = 0;
        int numEntries = 0;
        int draw = 0;
        String ejercicio = "";
        String procedimiento = "";
        String numeroExpediente = "";
        String estadoFirma = "";
        String tipoDocumento = "";
        String fechaEnvioPeticion = "";
        String fechaEnvioPeticionDesde = "";
        String fechaEnvioPeticionHasta = "";
        String documentoNotificado = "";
        
        try {   
            if (request.getParameter("idioma") != null && !request.getParameter("idioma").isEmpty()) {
                try {
                    idiomaUsuario = Integer.parseInt(request.getParameter("idioma"));
                } catch (Exception ex) {
                    log.error("Error al parsear request.getParameter(idioma) a Entero. Asignamos idioma por defecto 1 Castellano.", ex);
                }
            }
        
            //Recoger los parametros
            startTable = Integer.parseInt(request.getParameter("start"));
            finishTable = startTable + Integer.parseInt(request.getParameter("length"));
            numEntries = Integer.parseInt(request.getParameter("length"));
            draw = Integer.parseInt(request.getParameter("draw"));
            ejercicio = (String) request.getParameter("ejercicio");
            procedimiento = (String) request.getParameter("procedimiento");
             numeroExpediente = (String) request.getParameter("numeroExpediente");
            estadoFirma = (String) request.getParameter("estadoFirma");
            tipoDocumento = (String) request.getParameter("tipoDocumento");
            fechaEnvioPeticion = (String) request.getParameter("fechaEnvioPeticion");
            fechaEnvioPeticionDesde = (String) request.getParameter("fechaEnvioPeticionDesde");
            fechaEnvioPeticionHasta = (String) request.getParameter("fechaEnvioPeticionHasta");
            documentoNotificado = (String) request.getParameter("documentoNotificado");
        
         FiltrosVO filtros = new FiltrosVO(startTable, finishTable, numEntries, draw,
                    ejercicio, procedimiento,numeroExpediente, estadoFirma, tipoDocumento, fechaEnvioPeticion,documentoNotificado,fechaEnvioPeticionDesde,fechaEnvioPeticionHasta);

            HttpSession session = request.getSession();
            UsuarioValueObject usuario = new UsuarioValueObject();
            try {
                if (session != null) {
                    if (usuario != null) {
                        usuario = (UsuarioValueObject) session.getAttribute("usuario");
                    }
                }
            }catch(Exception ex){
                log.error("Error al recuperar los datos del Usuario desde la session", ex);
            }


         List<ComboVO> listaProcedimiento = MeLanbide08Manager.getInstance().getComboProcedimiento(codOrganizacion,usuario.getIdUsuario(), adapt);
         //cargar lista de resultados 
         retorno = MeLanbide08Manager.getInstance().getLogSinPaginar(codOrganizacion, adapt, filtros,listaProcedimiento);
         
         Date fechaEnviado =new Date();
         //Creamos las lineas en la tabla de logs de los documentos enviados a firmar
          for (int i = 0; i < retorno.getLstRegistros().size(); i++) {
           retorno.getLstRegistros().get(i).setFechaEnviadoJob(fechaEnviado);
           retorno.getLstRegistros().get(i).setIdLog(MeLanbide08LogJobManager.getInstance().insertarLineasLogJob(adapt,retorno.getLstRegistros().get(i)));
          }
          
       
          //Ejecutamos el proceso en segundo plano 
          final int codOrganizacionFinal= codOrganizacion;
           final RetornoResultadosFiltros retornoFinal = retorno;
           final AdaptadorSQLBD adaptFinal = adapt;
           final RequestInformationHolder requestFinal = new RequestInformationHolder();
           final UsuarioValueObject usuarioFinal = usuario;
           
           
           //Assignamos los valores del request en un objecto para recuperarlos luego ya que un thread pierde el contexto 
           requestFinal.setSessionId(request.getSession().getId());
           requestFinal.setOrigen(request.getParameter("origen"));
           requestFinal.setContextPath(request.getContextPath());
           requestFinal.setNombreDocumento( request.getParameter("nombreDocumento"));
           
          
     new Thread(new Runnable() {
     @Override
     public void run() {
         try {
             TimeUnit.SECONDS.sleep(10);  
             procesarNotificacionesSegundoPlano (codOrganizacionFinal,  retornoFinal,adaptFinal, usuarioFinal, requestFinal);
         } catch (Exception ex) {
            log.error("Error al intentar ejecutar en segundo plano");
         }
     }
        }).start();
        
        
      retorno = new RetornoResultadosFiltros();

         
         GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.serializeNulls();
            Gson gson = gsonBuilder.create();
            String sResponse = gson.toJson(retorno);
            try {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                out.print(sResponse);
                out.flush();
                out.close();
            } catch (Exception e) {
                log.error("Error preparando respuesta " + e.getMessage(), e);
                e.printStackTrace();
            }

            log.info("Retorno size: " + retorno.getLstRegistros().size());

        } catch (Exception ex) {
            log.error("Se ha producido un error a cargar la pantalla de consulta estado firmas " + numExpediente, ex);
            retorno = null;
            request.setAttribute("mensajeInicial", "Se ha presentado un error al cargar la pantalla ...Contacte con el ADMIN para mas detalles. " + ex.getMessage());
        }
        log.info("cargarDatosPrincipalConsultaEstadoFirmaFiltros - End()" + new Date().toString());

      }
      
      public void cargarDatosPrincipalConsultaEstadoFirmaFiltrosSinPaginar(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("cargarDatosPrincipalConsultaEstadoFirmaFiltrosSinPaginar - Begin()" + new Date().toString());
        AdaptadorSQLBD adapt = null;
        RetornoResultadosFiltros retorno = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (SQLException ex) {
            log.error("Error al recuperar el adaptador getAdaptSQLBD ", ex);
        } 

 
        int idiomaUsuario = 1;
        int startTable = 0;
        int finishTable = 0;
        int numEntries = 0;
        int draw = 0;
        String ejercicio = "";
        String procedimiento = "";
        String numeroExpediente = "";
        String estadoFirma = "";
        String tipoDocumento = "";
        String fechaEnvioPeticion = "";
        String fechaEnvioPeticionDesde = "";
        String fechaEnvioPeticionHasta = "";
        String documentoNotificado = "";
        
        try {   
            if (request.getParameter("idioma") != null && !request.getParameter("idioma").isEmpty()) {
                try {
                    idiomaUsuario = Integer.parseInt(request.getParameter("idioma"));
                } catch (Exception ex) {
                    log.error("Error al parsear request.getParameter(idioma) a Entero. Asignamos idioma por defecto 1 Castellano.", ex);
                }
            }
        
            //Recoger los parametros
            startTable = Integer.parseInt(request.getParameter("start"));
            finishTable = startTable + Integer.parseInt(request.getParameter("length"));
            numEntries = Integer.parseInt(request.getParameter("length"));
            draw = Integer.parseInt(request.getParameter("draw"));
            ejercicio = (String) request.getParameter("ejercicio");
            procedimiento = (String) request.getParameter("procedimiento");
             numeroExpediente = (String) request.getParameter("numeroExpediente");
            estadoFirma = (String) request.getParameter("estadoFirma");
            tipoDocumento = (String) request.getParameter("tipoDocumento");
            fechaEnvioPeticion = (String) request.getParameter("fechaEnvioPeticion");
            fechaEnvioPeticionDesde = (String) request.getParameter("fechaEnvioPeticionDesde");
            fechaEnvioPeticionHasta = (String) request.getParameter("fechaEnvioPeticionHasta");
            documentoNotificado = (String) request.getParameter("documentoNotificado");
        
         FiltrosVO filtros = new FiltrosVO(startTable, finishTable, numEntries, draw,
                    ejercicio, procedimiento,numeroExpediente, estadoFirma, tipoDocumento, fechaEnvioPeticion,documentoNotificado,fechaEnvioPeticionDesde,fechaEnvioPeticionHasta);

            HttpSession session = request.getSession();
            UsuarioValueObject usuario = new UsuarioValueObject();
            try {
                if (session != null) {
                    if (usuario != null) {
                        usuario = (UsuarioValueObject) session.getAttribute("usuario");
                    }
                }
            }catch(Exception ex){
                log.error("Error al recuperar los datos del Usuario desde la session", ex);
            }


         List<ComboVO> listaProcedimiento = MeLanbide08Manager.getInstance().getComboProcedimiento(codOrganizacion,usuario.getIdUsuario(), adapt);
         //cargar lista de resultados 
         retorno = MeLanbide08Manager.getInstance().getLogSinPaginar(codOrganizacion, adapt, filtros,listaProcedimiento);
         
         GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.serializeNulls();
            Gson gson = gsonBuilder.create();
            String sResponse = gson.toJson(retorno);
            try {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                out.print(sResponse);
                out.flush();
                out.close();
            } catch (Exception e) {
                log.error("Error preparando respuesta " + e.getMessage(), e);
                e.printStackTrace();
            }

            log.info("Retorno size: " + retorno.getLstRegistros().size());

        } catch (Exception ex) {
            log.error("Se ha producido un error a cargar la pantalla de consulta estado firmas " + numExpediente, ex);
            retorno = null;
            request.setAttribute("mensajeInicial", "Se ha presentado un error al cargar la pantalla ...Contacte con el ADMIN para mas detalles. " + ex.getMessage());
        }
        log.info("cargarDatosPrincipalConsultaEstadoFirmaFiltros - End()" + new Date().toString());

      }
      
      
    // Mï¿½todo privado para obtener una adaptador de BBDD
    private AdaptadorSQLBD getAdaptSQLBD(String codOrganizacion) throws SQLException {
        if (log.isDebugEnabled()) {
            log.debug("getConnection ( codOrganizacion = " + codOrganizacion + " ) : BEGIN");
        }
        ResourceBundle config = ResourceBundle.getBundle("techserver");
        String gestor = config.getString("CON.gestor");
        String jndiGenerico = config.getString("CON.jndi");
        Connection conGenerico = null;
        Statement st = null;
        ResultSet rs = null;
        String[] salida = null;
        Connection con = null;

        if (log.isDebugEnabled()) {
            log.debug("getJndi =========> ");
            log.debug("parametro codOrganizacion: " + codOrganizacion);
            log.debug("gestor: " + gestor);
            log.debug("jndi: " + jndiGenerico);
        }//if(log.isDebugEnabled())

        DataSource ds = null;
        AdaptadorSQLBD adapt = null;
        synchronized (this) {
            try {
                PortableContext pc = PortableContext.getInstance();
                if (log.isDebugEnabled()) {
                    log.debug("He cogido el jndi: " + jndiGenerico);
                }
                ds = (DataSource) pc.lookup(jndiGenerico, DataSource.class);
                // Conexiï¿½n al esquema genï¿½rico
                conGenerico = ds.getConnection();

                String sql = "SELECT EEA_BDE FROM A_EEA WHERE EEA_APL=" + ConstantesDatos.APP_GESTION_EXPEDIENTES + " AND AAE_ORG=" + codOrganizacion;
                st = conGenerico.createStatement();
                rs = st.executeQuery(sql);
                String jndi = null;
                while (rs.next()) {
                    jndi = rs.getString("EEA_BDE");
                }//while(rs.next())

                st.close();
                rs.close();
                conGenerico.close();

                if (jndi != null && gestor != null && !"".equals(jndi) && !"".equals(gestor)) {
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
            } catch (TechnicalException te) {
                te.printStackTrace();
                log.error("*** AdaptadorSQLBD: " + te.toString());
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (conGenerico != null && !conGenerico.isClosed()) {
                    conGenerico.close();
                }
            }// finally
            if (log.isDebugEnabled()) {
                log.debug("getConnection() : END");
            }
        }// synchronized
        return adapt;
    }//getConnection
    
    private byte[] descargarFichero(Map<String, Object> parametros) throws TechnicalException {
        byte[] fichero = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        log.debug("descargarFichero");
        
        try {
            String codigo = (String) parametros.get("codDocumento");
            String origen = (String) parametros.get("origen");
            Boolean isExpHistorico = (Boolean) parametros.get("isExpHistorico");
            String nombreFichero = (String) parametros.get("nombreDocumento");
            String contextPath = (String) parametros.get("contextPath");
            String sessionId = (String) parametros.get("sessionId");
            
            Config configCommon = ConfigServiceHelper.getConfig("common");
            Config configDoc = ConfigServiceHelper.getConfig("documentos");
            String url = String.format("%s%s%s",
                    configCommon.getString("hostVirtual"),
                    contextPath,
                    configDoc.getString("CSV/URI/SERVLET_VER_DOCUMENTO"));

            Map<String, Object> paramsDescargarFichero = new LinkedHashMap<String, Object>();
            paramsDescargarFichero.put("codigo", codigo);
            paramsDescargarFichero.put("nombreFich", nombreFichero);
            if (ConstantesDatos.ORIGEN_ELIMINAR_DOCUMENTO_EXPEDIENTE.equalsIgnoreCase(origen)) {
                paramsDescargarFichero.put("opcion", "0");
                paramsDescargarFichero.put("expHistorico", isExpHistorico);
            } else if (ConstantesDatos.ORIGEN_ELIMINAR_DOCUMENTO_TRAMITE.equalsIgnoreCase(origen)) {
                paramsDescargarFichero.put("opcion", "1");
            }

            // Descarga del fichero
            paramsDescargarFichero.put("opcion", "7");
            //Comentar solo para pruebas local
            url= "http://localhost:8080/LCE_16_00/VerDocumentoDatosSuplementarios";
            WebOperations.descargarUrlPost(url, sessionId, paramsDescargarFichero, out);

            fichero = out.toByteArray();
        } catch (MalformedURLException mue) {
            throw new TechnicalException("Error al intentar descargar el fichero del documento", mue);
        } catch (IOException ioe) {
            throw new TechnicalException("Error al intentar descargar el fichero del documento", ioe);
        } finally {
            IOUtils.closeQuietly(out);
        }

        return fichero;
    }
    
    
    
    private String getRutaTemporalFicherosCSV(Map<String, Object> parametros) throws TechnicalException {
        StringBuilder path = new StringBuilder();

        log.debug("getRutaTemporalFicherosCSV");
        
        try {
            ResourceBundle config = ResourceBundle.getBundle("documentos");
            String rutaBase = config.getString("RUTA_DISCO_DOCUMENTOS");
            String sessionId = (String) parametros.get("sessionId");
            String numExpediente = (String) parametros.get("numExpediente");
            numExpediente = numExpediente.replaceAll("/", "-");

            path.append(rutaBase)
                .append(File.separator)
                .append(ConstantesDatos.SUBCARPETA_DOCUMENTOS_EXPEDIENTE)
                .append(File.separator)
                .append(sessionId)
                .append(File.separator)
                .append(numExpediente)
                .append(File.separator)
                .append(ConstantesDatos.SUBCARPETA_DOCUMENTOS_CSV);

            if (log.isDebugEnabled()) {
                log.debug(String.format("Ruta temporal de ficheros CSV: %s", path.toString()));
            }
        } catch (Exception e) {
            throw new TechnicalException("Error al obtener la ruta temporal para almacenar los CSV ", e);
        }

        return path.toString();
    }
    
    private Hashtable<String, Object> datosParaObtenerDocumento (Map<String,Object> parametros) {
        Hashtable<String, Object> datos = new Hashtable<String, Object>();
        
        UsuarioValueObject usuario = (UsuarioValueObject) parametros.get("usuario");
        String[] params = (String[]) parametros.get("paramsBBDD");
        Integer codMunicipio = Integer.valueOf(parametros.get("codMunicipio").toString());
        String codProcedimiento = (String) parametros.get("codProcedimiento");
        Integer ejercicio = Integer.valueOf(parametros.get("ejercicio").toString());
        String numeroExpediente = (String) parametros.get("numExpediente");
        Integer codTramite = Integer.valueOf(parametros.get("codTramite").toString());
        Integer ocurrenciaTramite = Integer.valueOf(parametros.get("ocurrenciaTramite").toString());
        Integer codDocumento = Integer.valueOf(parametros.get("codDocumento").toString());
        Integer codUsuario = usuario.getIdUsuario();
        
        datos.put("codMunicipio", Integer.toString(codMunicipio));
        datos.put("codProcedimiento", codProcedimiento);
        datos.put("ejercicio", Integer.toString(ejercicio));
        datos.put("numeroExpediente", numeroExpediente);
        datos.put("codTramite", Integer.toString(codTramite));
        datos.put("ocurrenciaTramite", Integer.toString(ocurrenciaTramite));
        datos.put("codDocumento", Integer.toString(codDocumento));
        datos.put("codUsuario", Integer.toString(codUsuario));
        datos.put("numeroDocumento", Integer.toString(codDocumento));
        datos.put("perteneceRelacion", "false");
        datos.put("params", params);
        
        return datos;
    }
    
    private void obtenerExtensionYTipoMime(String editorTexto, String nombreDocumento, Hashtable<String, Object> datos) {
        String extension = null;
        String tipoMime = null;
        if (ConstantesDatos.OOFFICE.equals(editorTexto)) {
            extension = ConstantesDatos.EXTENSION_DOCUMENTOS_TRAMITACION_OPENOFFICE;
            tipoMime = ConstantesDatos.TIPO_MIME_DOCUMENTO_OPENOFFICE;
        } else if (ConstantesDatos.WORD.equals(editorTexto)) {
            extension = ConstantesDatos.EXTENSION_DOCUMENTOS_TRAMITACION_WORD;
            tipoMime = ConstantesDatos.TIPO_MIME_DOC_TRAMITES;
        } else {
            extension = FilenameUtils.getExtension(nombreDocumento);
            tipoMime = MimeTypes.guessMimeTypeFromExtension(extension);
        }
        datos.put("extension", extension);
        datos.put("tipoMime", tipoMime);
    }
    private int obtenerListaCarpetas(String codOrganizacion, String codProcedimiento, Integer codTramite, String[] params, Hashtable<String, Object> datos, 
            AlmacenDocumento almacen, UsuarioValueObject usuario, String numeroExpediente,AdaptadorSQLBD adaptador) {
        int tipoDocumento;
        
        //Dan error en el core nos los llevamos al modulo
        //String codigoVisibleTramite = DefinicionTramitesManager.getInstance().getCodigoVisibleTramite(codOrganizacion, codProcedimiento, String.valueOf(codTramite), params);
        String codigoVisibleTramite = MeLanbide08Manager.getInstance().getCodigoVisibleTramite(codOrganizacion, codProcedimiento, String.valueOf(codTramite), params, adaptador);
        //String nombreProcedimiento = DefinicionProcedimientosManager.getInstance().getDescripcionProcedimiento(codProcedimiento, params);

        String nombreProcedimiento = MeLanbide08Manager.getInstance().getDescripcionProcedimiento(codProcedimiento, params, adaptador);
        datos.put("codigoVisibleTramite", codigoVisibleTramite);
        datos.put("codificacion", ConstantesDatos.CODIFICACION_UTF_8);
        /**
         * SE INDICA POR ORDEN CUALES SER\C1N LAS CARPETAS EN LAS QUE SE
         * ALOJAR\C1N EL DOCUMENTO EN EL GESTOR DOCUMENTAL *
         */
        ResourceBundle bundleDocumentos = ResourceBundle.getBundle("documentos");
        String carpetaRaiz = bundleDocumentos.getString(String.format("%s%s%s%s%s",
                ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO,
                codOrganizacion,
                ConstantesDatos.BARRA,
                almacen.getNombreServicio(),
                ConstantesDatos.SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ));
        String descripcionOrganizacion = usuario.getOrg();
        ArrayList<String> listaCarpetas = new ArrayList<String>();
        listaCarpetas.add(carpetaRaiz);
        listaCarpetas.add(String.format("%s%s%s", codOrganizacion, ConstantesDatos.GUION, descripcionOrganizacion));
        listaCarpetas.add(String.format("%s%s%s", codProcedimiento, ConstantesDatos.GUION, nombreProcedimiento));
        listaCarpetas.add(numeroExpediente.replaceAll(ConstantesDatos.BARRA, ConstantesDatos.GUION));
        datos.put("listaCarpetas", listaCarpetas);
        tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_GESTOR;
        return tipoDocumento;
    }
    private byte[] obtenerDocumento (Map<String,Object> parametros,AdaptadorSQLBD adaptador) throws TechnicalException {
         
        byte[] fichero = null;
        String nombreFichero = null;
        String tipoContenido = null;
        
        UsuarioValueObject usuario = (UsuarioValueObject) parametros.get("usuario");
        String codOrganizacion = Integer.toString(usuario.getOrgCod());
        String[] params = (String[]) parametros.get("paramsBBDD");
        Integer codMunicipio = Integer.valueOf(parametros.get("codMunicipio").toString());
        String codProcedimiento = (String) parametros.get("codProcedimiento");
        Integer ejercicio = Integer.valueOf(parametros.get("ejercicio").toString());
        String numero = (String) parametros.get("numExpediente");
        Integer codTramite = Integer.valueOf(parametros.get("codTramite").toString());
        Integer ocurrenciaTramite = Integer.valueOf(parametros.get("ocurrenciaTramite").toString());
        Integer numeroDocumento = Integer.valueOf(parametros.get("codDocumento").toString());
        String nombreDocumento = (String) parametros.get("nombreDocumento");
        Boolean isExpHistorico = (Boolean) parametros.get("isExpHistorico");
        
        
        Hashtable<String,Object> datos = datosParaObtenerDocumento(parametros);
        datos.put("nombreDocumento", nombreDocumento);
        
        log.info("params : " + params);

        String editorTexto = MeLanbide08Manager.getInstance().getEditorTexto(codMunicipio,
            numero,codTramite,ocurrenciaTramite, numeroDocumento,false,params,adaptador);
        log.info("editorTexto : " + editorTexto);

        // Para las viejas plantillas comprobamos si tiene CSV, en cuyo caso se
        // trata como un documento que tiene extension en el nombre de fichero.
        if (ConstantesDatos.OOFFICE.equals(editorTexto) || ConstantesDatos.WORD.equals(editorTexto)) {
            // Se verifica si tiene CSV, si tiene CSV es que no es un documento de tipo WORD o OOFFICE
            // Por lo que el fichero tiene extension
            if (DocumentoManager.getInstance().existeMetadatoCSV(
                    params, ConstantesDatos.SUBCONSULTA_E_CRD_ID_METADATO_PK,
                    codMunicipio,
                    codProcedimiento,
                    ejercicio,
                    numero,
                    codTramite,
                    ocurrenciaTramite,
                    numeroDocumento)) {
                log.info("editorTexto valdra vacio");
                editorTexto = "";
            }
        }

        obtenerExtensionYTipoMime(editorTexto, nombreDocumento, datos);      

        log.info("Se obtiene el almacen de documentos para descargar el documento");
        AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(Integer.toString(usuario.getOrgCod())).getImplClassPluginProcedimiento(Integer.toString(usuario.getOrgCod()),codProcedimiento);
        Documento doc = null;

        int tipoDocumento = -1;
        if(!almacen.isPluginGestor()){
            tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_BBDD;
        } else{
            tipoDocumento = obtenerListaCarpetas(codOrganizacion, codProcedimiento, codTramite, params, datos, almacen, usuario, numero,adaptador);
        }
            
        try{
            log.info("Se obtiene el documento");
            doc = DocumentoTramitacionFactoria.getInstance().getDocumento(datos, tipoDocumento);
            doc.setExpHistorico(isExpHistorico);
            fichero = almacen.getDocumento(doc);

            String nombreFicheroExtension = FilenameUtils.getExtension(doc.getNombreDocumento());
            if (StringUtils.isEmpty(nombreFicheroExtension) || !nombreFicheroExtension.equalsIgnoreCase(doc.getExtension())) {
            nombreFichero = doc.getNombreDocumento() + ConstantesDatos.DOT + doc.getExtension();
            } else {
                nombreFichero = doc.getNombreDocumento();
            }

            tipoContenido  = doc.getTipoMimeContenido();

            if (log.isDebugEnabled()) {
                log.info("Logitud del fichero: " + fichero.length);
                log.info("Nombre del fichero: " + nombreFichero);
                log.info("Tipo contenido fichero: " + tipoContenido);
            }

        }catch(AlmacenDocumentoTramitacionException e){
                            e.printStackTrace();
            log.error("Error al recuperar documento : ", e);
            log.error("Error al recuperar documento : Codigo Error recibido flexia(Regitrado en BBDD ERRDIGIT) y mensaje :" + e.getCodigo() + " - " + e.getMessage());
            fichero  = null;
            nombreFichero = null;
            tipoContenido = null;
            throw new TechnicalException (e.getMessage());
        } 
        return fichero;
     }
    
     private byte[] obtieneFicheroPdfCSV(Map<String, Object> parametros, String pdfA,AdaptadorSQLBD adaptador)
            throws TechnicalException, FormatNotSupportedException, Exception {
        byte[] documentoConvertido = null;

        log.debug("obtieneFicheroPdfCSV");
        
        // Descargar fichero
        byte[] ficheroDoc =  this.obtenerDocumento(parametros,adaptador);

        if (ficheroDoc != null && ficheroDoc.length > 0) {
            try {
                String tipoMimeOriginal = "application/msword";
                        //(String) parametros.get("tipoMimeOriginal");
                if (!MimeTypes.PDF[0].equals(tipoMimeOriginal)) {
                    // Convertir fichero a PDF
                    String rutaTemporalCSV = getRutaTemporalFicherosCSV(parametros);
                    documentoConvertido = JODConverterHelper.convertirDocumentoAPdf(rutaTemporalCSV, ficheroDoc, tipoMimeOriginal, pdfA);
                } else {
                    documentoConvertido = ficheroDoc;
                }
            } catch (DocumentConversionException dce) {
                throw new TechnicalException("Error al intentar convertir el fichero a pdf", dce);
            }
        } else {
            throw new TechnicalException("El fichero esta vacio");
        }
        parametros.put("ficheroDoc", ficheroDoc);
        
        log.debug("obtieneFicheroPdfCSV FIN");
        return documentoConvertido;
    }
     
     private String getServerTempFileName(Map<String, Object> parametros, String nombreFichero)
            throws TechnicalException {
        
        log.debug("getServerTempFileName");
        
        StringBuilder path = new StringBuilder();
        String codigoCampo = (String) parametros.get("codigo");
        Integer codTramite = Integer.valueOf(parametros.get("codTramite").toString());
        Integer ocurrenciaTramite = Integer.valueOf(parametros.get("ocurrenciaTramite").toString());
        String sessionId = (String) parametros.get("sessionId");
        String numExpediente = (String) parametros.get("numExpediente");
        numExpediente = numExpediente.replaceAll("/", "-");
        
        try {
            ResourceBundle config = ResourceBundle.getBundle("documentos");
            path.append(config.getString("RUTA_DISCO_DOCUMENTOS"))
                .append(File.separator)
                .append(ConstantesDatos.SUBCARPETA_DOCUMENTOS_EXPEDIENTE)
                .append(File.separator)
                .append(sessionId)
                .append(File.separator)
                .append(numExpediente)
                .append(File.separator)
                .append(codigoCampo);
            
            if (codTramite != null && ocurrenciaTramite != null) {
                path.append("_")
                    .append(codTramite)
                    .append("_")
                    .append(ocurrenciaTramite); 
            }
            
            path.append("_")
                .append(nombreFichero);
            
            if (log.isDebugEnabled()) {
                log.debug(String.format("Ruta del fichero en servidor: %s", path.toString()));
            }
        } catch (Exception e) {
            throw new TechnicalException("Error al obtener la propiedad que indica la ruta en la que se almacenan los documentos en disco: " + e);
        }

        return path.toString();
    }
     
   private DocumentoTramiteVO almacenarDocumentoCSV(Map<String, Object> parametros,
            ByteArrayOutputStream outputStream, String codigoCSV, String portafirmas, DocumentoFirma documento,AdaptadorSQLBD adapt)
            throws TechnicalException {

        DocumentoTramiteVO documentoTramite = null;

        log.debug("almacenarDocumentoCSV");
        Map<String, Object> parametrosDocumentoDuplciado = null;
        
        if (portafirmas != null && "LAN".equals(portafirmas)) {
            parametrosDocumentoDuplciado =  parametros;
            parametrosDocumentoDuplciado.remove("codDocumento");
            parametrosDocumentoDuplciado.put("codDocumento", documento.getCodDocumento());
           TramitacionExpedientesValueObject tramitesVO =  (TramitacionExpedientesValueObject)parametros.get("tramitesVO");
            tramitesVO.setCodDocumento(String.valueOf(documento.getCodDocumento()));
            parametrosDocumentoDuplciado.remove("tramitesVO");
            parametrosDocumentoDuplciado.put("tramitesVO", tramitesVO);
        }

        if (portafirmas != null && "LAN".equals(portafirmas)) {
            documentoTramite = grabarDocumentoCSV(parametrosDocumentoDuplciado, outputStream, codigoCSV, documentoTramite, portafirmas,adapt);
        } else {
           // documentoTramite = grabarDocumentoCSV(parametros, outputStream, codigoCSV, documentoTramite, portafirmas);
        }

        return documentoTramite;
    } 
   
   private DocumentoTramiteVO grabarDocumentoCSV(Map<String, Object> parametros, ByteArrayOutputStream outputStream, String codigoCSV, DocumentoTramiteVO documentoTramite, String portafirmas,AdaptadorSQLBD adapt) throws TechnicalException {
        try {
            TramitacionExpedientesValueObject tramitesVO = (TramitacionExpedientesValueObject) parametros.get("tramitesVO");
            
            Integer codDocumento = Integer.parseInt(parametros.get("codDocumento").toString());
        String codProcedimiento = (String) parametros.get("codProcedimiento");
        Integer codMunicipio = Integer.parseInt(parametros.get("codMunicipio").toString());
        Integer ejercicio = Integer.parseInt(parametros.get("ejercicio").toString());
        String numExpediente = (String) parametros.get("numExpediente");
        Integer codTramite = Integer.parseInt(parametros.get("codTramite").toString());
        Integer ocurrenciaTramite = Integer.parseInt(parametros.get("ocurrenciaTramite").toString());
            
            String editorTexto = (String) parametros.get("editorTexto");
            String[] params = (String[]) parametros.get("paramsBBDD");
            String numeroExpediente = (String) parametros.get("numExpediente");
            byte[] fichero = outputStream.toByteArray();

            // Nombre del fichero. Modificar la extension y cambiarla por PDF
            String nombreAntiguo = (String) parametros.get("nombreDocumento");
            log.debug("nombreAntiguo vale: " + nombreAntiguo);
            log.debug("editorTexto vale: " + editorTexto);
            String nombreNuevo = null;
            if (ConstantesDatos.OOFFICE.equals(editorTexto)) {
                nombreNuevo = String.format("%s.%s",
                        StringUtils.removeEndIgnoreCase(nombreAntiguo, ConstantesDatos.EXTENSION_DOCUMENTOS_TRAMITACION_OPENOFFICE_CON_PUNTO),
                        MimeTypes.FILEEXTENSION_PDF);
            } else if (ConstantesDatos.WORD.equals(editorTexto)) {
                nombreNuevo = String.format("%s.%s",
                        StringUtils.removeEndIgnoreCase(nombreAntiguo, ConstantesDatos.EXTENSION_DOCUMENTOS_TRAMITACION_WORD_CON_PUNTO),
                        MimeTypes.FILEEXTENSION_PDF);
            } else {
                nombreNuevo = String.format("%s.%s",
                        FilenameUtils.removeExtension(nombreAntiguo),
                        MimeTypes.FILEEXTENSION_PDF);
            }
            
            log.debug("nombreNuevo vale: " + nombreNuevo);

            // Metadatos del documento
            MetadatosDocumentoVO metadatosNuevo = new MetadatosDocumentoVO();
            metadatosNuevo.setCsv(codigoCSV);
            metadatosNuevo.setCsvAplicacion(ConstantesDatos.APLICACION_ORIGEN_DOCUMENTO_FLEXIA);
            parametros.put("extension", MimeTypes.FILEEXTENSION_PDF);
            metadatosNuevo.setCsvUri(generarUriCsvDescargaFichero(parametros));

            if (log.isDebugEnabled()) {
                log.debug(String.format("nombreAntiguo: %s", nombreAntiguo));
                log.debug(String.format("nombreNuevo: %s", nombreNuevo));
                log.debug(String.format("metadatosNuevo: %s", metadatosNuevo));
            }

            parametros.put("nombreDocumentoNuevo", nombreNuevo);
            parametros.put("fichero", fichero);
            parametros.put("metadatosNuevo", metadatosNuevo);
            
            // Grabar el documento en el almacen
            //TODO comentar
            grabarDocumentoCSV(parametros, portafirmas);
            
            // Obtenemos la fecha de modificacion del documento y actualizamos los
            // datos de la lista de documentos de tramite del formulario
         //   Vector<es.altia.agora.business.sge.TramitacionExpedientesValueObject> listaDocumentos = tramitesVO.getCodDocumento()ListaDocumentos();
         //   Calendar resultFecha = MeLanbide08Manager.getDocumentoFechaModificacion(
         //           codDocumento, ejercicio,
         //           codMunicipio, numeroExpediente, codTramite,
         //           ocurrenciaTramite, codProcedimiento, params,adapt);
            
     //       String fechaModificacion = DateOperations.toString(resultFecha, DateOperations.LATIN_DATE_FORMAT);
            
         //   for (es.altia.agora.business.sge.TramitacionExpedientesValueObject doc : listaDocumentos) {
           //     if (codDocumento.equals(NumberUtils.createInteger(doc.getCodDocumento()))) {
             //       doc.setDescDocumento(nombreNuevo);
               //     doc.setFechaModificacion(fechaModificacion);
                    //doc.setEditorTexto(null);
                    //doc.setCodPlantilla(null);
                    //doc.setTipoPlantilla(null);
             //       break;
              //  }
          //  }
            
            documentoTramite = new DocumentoTramiteVO();
            documentoTramite.setCodigo(String.valueOf(codDocumento));
            documentoTramite.setNombre(nombreNuevo);
          //  documentoTramite.setFechaModificacion(fechaModificacion);
        } catch (Exception e) {
            throw new TechnicalException("Error al intentar almacenar el fichero", e);
        }
        return documentoTramite;
    }
   
   
   private void grabarDocumentoCSV(Map<String, Object> parametros, String portafirmas) throws TechnicalException {
        log.debug("grabarDocumentoCSV");

//        try {Integer.parseInt(parametros.get("codMunicipio").toString());
            UsuarioValueObject usuario = (UsuarioValueObject) parametros.get("usuario");
            String[] params = (String[]) parametros.get("paramsBBDD");
            Integer codMunicipio = Integer.parseInt(parametros.get("codMunicipio").toString());
            String codProcedimiento = (String) parametros.get("codProcedimiento");
            Integer ejercicio = Integer.parseInt( parametros.get("ejercicio").toString());
            String numeroExpediente = (String) parametros.get("numExpediente");
            Integer codTramite = Integer.parseInt( parametros.get("codTramite").toString());
            Integer ocurrenciaTramite = Integer.parseInt( parametros.get("ocurrenciaTramite").toString());
            Integer codDocumento = Integer.parseInt( parametros.get("codDocumento").toString());
            Integer codUsuario = usuario.getIdUsuario();
            String codOrganizacion = Integer.toString(usuario.getOrgCod());
            String nombreDocumento = (String) parametros.get("nombreDocumentoNuevo");
            MetadatosDocumentoVO metadatos = (MetadatosDocumentoVO) parametros.get("metadatosNuevo");
            Boolean insertarMetadatoEnBBDD = Boolean.FALSE;
            byte[] fichero = (byte[]) parametros.get("fichero");

            if (log.isDebugEnabled()) {
                log.debug(String.format("codMunicipio: %d", codMunicipio));
                log.debug(String.format("codProcedimiento: %s", codProcedimiento));
                log.debug(String.format("ejercicio: %d", ejercicio));
                log.debug(String.format("numeroExpediente: %s", numeroExpediente));
                log.debug(String.format("codTramite: %d", codTramite));
                log.debug(String.format("ocurrenciaTramite: %d", ocurrenciaTramite));
                log.debug(String.format("codDocumento: %d", codDocumento));
                log.debug(String.format("codUsuario: %d", codUsuario));
                log.debug(String.format("nombreDocumento: %s", nombreDocumento));
                log.debug(String.format("numeroDocumento: %d", codDocumento));
                log.debug(String.format("metadatos: %s", metadatos));
                log.debug(String.format("tamanho fichero: %d", fichero.length));
            }

            Hashtable<String, Object> datos = new Hashtable<String, Object>();
            datos.put("codMunicipio", Integer.toString(codMunicipio));
            datos.put("codProcedimiento", codProcedimiento);
            datos.put("ejercicio", Integer.toString(ejercicio));
            datos.put("numeroExpediente", numeroExpediente);
            datos.put("codTramite", Integer.toString(codTramite));
            datos.put("ocurrenciaTramite", Integer.toString(ocurrenciaTramite));
            datos.put("codDocumento", Integer.toString(codDocumento));
            datos.put("codUsuario", Integer.toString(codUsuario));
            datos.put("nombreDocumento", nombreDocumento);
            datos.put("numeroDocumento", Integer.toString(codDocumento));
            datos.put("perteneceRelacion", "false");
            datos.put("params", params);
            datos.put("fichero", fichero);
            
            if (metadatos != null) {
                // CSV
                if (StringUtils.isNotEmpty(metadatos.getCsv())) {
                    datos.put("metadatoDocumentoCsv", metadatos.getCsv());
                    datos.put("metadatoDocumentoCsvAplicacion", metadatos.getCsvAplicacion());
                    datos.put("metadatoDocumentoCsvUri", metadatos.getCsvUri());
                    insertarMetadatoEnBBDD = Boolean.TRUE;
                }
            }
            datos.put("insertarMetadatosEnBBDD", insertarMetadatoEnBBDD);

            AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(codOrganizacion).getImplClassPluginProcedimiento(codOrganizacion, codProcedimiento);
            Documento doc = null;
            int tipoDocumento = -1;
            if (!almacen.isPluginGestor()) {
                tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_BBDD;
            } else { 
                
                String editorTexto = "";
                editorTexto = DocumentosAplicacionDAO.getInstance().getEditorTexto(usuario.getOrgCod(),
                        numeroExpediente, codTramite,
                        ocurrenciaTramite, codDocumento, false, params);
                obtenerExtensionYTipoMime(editorTexto, nombreDocumento, datos);

                tipoDocumento = obtenerListaCarpetas(codOrganizacion, codProcedimiento, codTramite, params, datos, almacen, usuario, numeroExpediente);
            }

            doc = DocumentoTramitacionFactoria.getInstance().getDocumento(datos, tipoDocumento);
            
            log.debug(String.format("doc.getNombreDocumento(): %s", doc.getNombreDocumento()));
            
            //Si el Portafirmas es de tipo Lanbide no es necesario guardar los datos ya que Lanbide crearía 
            //un nuevo registro y los OID de los documentos no serian los correctos 
            try {
                if (!almacen.setDocumento(doc)) {
                    throw new TechnicalException("El documento no ha sido guardado");
                }
            } catch (AlmacenDocumentoTramitacionException adte) {
                throw new TechnicalException("Error al intentar grabar el documento", adte);
            }
    }
   
   private int obtenerListaCarpetas(String codOrganizacion, String codProcedimiento, Integer codTramite, String[] params, Hashtable<String, Object> datos, 
            AlmacenDocumento almacen, UsuarioValueObject usuario, String numeroExpediente) {
        int tipoDocumento;
        String codigoVisibleTramite = DefinicionTramitesManager.getInstance().getCodigoVisibleTramite(codOrganizacion, codProcedimiento, String.valueOf(codTramite), params);
        String nombreProcedimiento = DefinicionProcedimientosManager.getInstance().getDescripcionProcedimiento(codProcedimiento, params);
        datos.put("codigoVisibleTramite", codigoVisibleTramite);
        datos.put("codificacion", ConstantesDatos.CODIFICACION_UTF_8);
        /**
         * SE INDICA POR ORDEN CUALES SER\C1N LAS CARPETAS EN LAS QUE SE
         * ALOJAR\C1N EL DOCUMENTO EN EL GESTOR DOCUMENTAL *
         */
        ResourceBundle bundleDocumentos = ResourceBundle.getBundle("documentos");
        String carpetaRaiz = bundleDocumentos.getString(String.format("%s%s%s%s%s",
                ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO,
                codOrganizacion,
                ConstantesDatos.BARRA,
                almacen.getNombreServicio(),
                ConstantesDatos.SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ));
        String descripcionOrganizacion = usuario.getOrg();
        ArrayList<String> listaCarpetas = new ArrayList<String>();
        listaCarpetas.add(carpetaRaiz);
        listaCarpetas.add(String.format("%s%s%s", codOrganizacion, ConstantesDatos.GUION, descripcionOrganizacion));
        listaCarpetas.add(String.format("%s%s%s", codProcedimiento, ConstantesDatos.GUION, nombreProcedimiento));
        listaCarpetas.add(numeroExpediente.replaceAll(ConstantesDatos.BARRA, ConstantesDatos.GUION));
        datos.put("listaCarpetas", listaCarpetas);
        tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_GESTOR;
        return tipoDocumento;
    }
     
     private String almacenarDocumentoCSV(Map<String, Object> parametros,
                ByteArrayOutputStream outputStream, String codigoCSV)
            throws TechnicalException {
        
        String nombreNuevo = null;
        
        log.debug("almacenarDocumentoCSVExpediente");

        try {
            String codigo = (String) parametros.get("codigo");
            byte[] fichero = outputStream.toByteArray();
            
            // Nombre del fichero
            String nombreAntiguo = (String) parametros.get("nombreFichero");
            nombreNuevo = String.format("%s.%s", FilenameUtils.removeExtension(nombreAntiguo), MimeTypes.FILEEXTENSION_PDF);
            parametros.put("nombreFicheroNuevo", nombreNuevo);
            
            // Ruta del fichero en disco
            String rutaNueva = null;
            String rutaAntigua = (String) parametros.get("rutaFicheroDisco");
            if (StringUtils.isEmpty(rutaAntigua)) {
                rutaNueva = getServerTempFileName(parametros, nombreNuevo);
            } else {
                rutaNueva = rutaAntigua;
            }

            // Estado del documento
            Integer estadoNuevo = ConstantesDatos.ESTADO_DOCUMENTO_NUEVO;

            // Tamaño del fichero
            Integer longitudNueva = new Integer(fichero.length);

            // Tipo del fichero
            String tipoMimeNuevo = MimeTypes.PDF[0];
            
            // Metadatos del documento
            MetadatosDocumentoVO metadatosNuevo = new MetadatosDocumentoVO();
            metadatosNuevo.setCsv(codigoCSV);
            metadatosNuevo.setCsvAplicacion(ConstantesDatos.APLICACION_ORIGEN_DOCUMENTO_FLEXIA);
            metadatosNuevo.setCsvUri(generarUriCsvDescargaFichero(parametros));

            if (log.isDebugEnabled()) {
                log.debug(String.format("nombreAntiguo: %s", nombreAntiguo));
                log.debug(String.format("nombreNuevo: %s", nombreNuevo));
                log.debug(String.format("rutaAntigua: %s", rutaAntigua));
                log.debug(String.format("rutaNueva: %s", rutaNueva));
                log.debug(String.format("estadoNuevo: %d", estadoNuevo));
                log.debug(String.format("longitudNueva: %d", longitudNueva));
                log.debug(String.format("tipoMimeNuevo: %s", tipoMimeNuevo));
                log.debug(String.format("metadatosNuevo: %s", metadatosNuevo));
            }
            
            // Grabar en disco del servidor
             FileUtils.writeByteArrayToFile(new File(rutaNueva), fichero);
            
        } catch (IOException ioe) {
            throw new TechnicalException("Error al intentar grabar el fichero a disco", ioe);
        }
        
        return nombreNuevo;
    }

    private DocumentoTramiteVO crearCsv(TramitacionExpedientesValueObject tEVO, RequestInformationHolder request, AdaptadorSQLBD adapt,  UsuarioValueObject usuario) throws Exception {
        //Creamos el csv 
        DocumentoTramiteVO campo = null;
        ByteArrayInputStream inputStream = null;
        ByteArrayOutputStream outputStream = null;   
     
          try {
            // Obtenemos el tipo de formulario en funcion del origen EXPEDIENTE/TRAMITE
            Map<String, Object> parametros = obtenerParametrosCrearCSV(tEVO, request,"LAN", adapt, usuario);
            
            // Se comprueba si ya existe el metadato CSV
            if (!existeDocumentoMetadatoCSV(parametros)) {
                // Se obtiene el fichero del documento
                //Se indica si al convertir el documento se desea que sea PDF o PDFA y ese valor se almacena en el fichero documentos.properties
                ResourceBundle configDocuemntos = ResourceBundle.getBundle("documentos");
                 
                String propiedad = usuario.getOrgCod()+"/DATOS_SUPLEMENTARIOS/FORMATO_PDFA";
                String pdfA = configDocuemntos.getString(propiedad);
                //LLamada al metodo del core
               
        
                byte[] ficheroDoc = obtieneFicheroPdfCSV(parametros, pdfA,adapt);

                DocumentoFirma duplicado = null; 
                
               duplicado = duplicarDocumentoAntesEnviarFirmar(usuario.getOrgCod(),parametros, null, request,usuario);
                
                if (ficheroDoc != null && ficheroDoc.length > 0) {
                    outputStream = new ByteArrayOutputStream();
                    inputStream = new ByteArrayInputStream(ficheroDoc);

                    String codigoCSV = "";
                    boolean generarCSV = CodigoSeguroVerificacionHelper.incrustarCSVenJustificante(usuario.getOrgCod(), registroConf);

                    if (generarCSV) {
                        // Incrustar el CSV en el pdf
                        String cabeceraCSV = ""; // TODO implementar cabecera
                        codigoCSV = DocumentoManager.getInstance().incrustarCSVenPDF(inputStream, outputStream, cabeceraCSV, usuario.getNombreUsu(), String.valueOf(usuario.getOrgCod()));
                    } else {
                        IOUtils.copy(inputStream,outputStream);
                    }
                    
                    
                    
                    
                    
                    // Almacenar el documento en el servidor y actualizar los datos del form
                     //LLamada al metodo del core
                      String nombreFichero = almacenarDocumentoCSV(parametros, outputStream, codigoCSV);
                       campo = almacenarDocumentoCSV(parametros, outputStream, codigoCSV, "LAN", duplicado, adapt); 
                     // private String almacenarDocumentoCSV(Map<String, Object> parametros,
               // ByteArrayOutputStream outputStream, String codigoCSV)
                              
          
                     String codigo = parametros.get("codigo").toString();
                     
                   
                    campo.setNombre(nombreFichero);
                   
                } else {
                  //  respuesta.setStatus(ConstantesAjax.STATUS_AJAX_ERROR_NO_EXISTE_FICHERO);
                //    respuesta.setDescStatus(ConstantesAjax.DESC_STATUS_AJAX_ERROR_NO_EXISTE_FICHERO);
    }
            } else {
             //   respuesta.setStatus(ConstantesAjax.STATUS_AJAX_ERROR_YA_EXISTE_CSV);
             //   respuesta.setDescStatus(ConstantesAjax.DESC_STATUS_AJAX_ERROR_YA_EXISTE_CSV);
            }
        } catch (FormatNotSupportedException fnse) {
            log.error("Ha ocurrido un error al intentar generar el CSV", fnse);
           throw new Exception(fnse.getMessage());
        } catch (Exception e) {
            log.error("Ha ocurrido un error al intentar generar el CSV", e);
            throw new Exception(e.getMessage());
            
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outputStream);
        }    
    return campo;
    }
private DocumentoFirma duplicarDocumentoAntesEnviarFirmar(int usuOrg, Map<String, Object> parametros, Integer codDocOriginal, RequestInformationHolder request, UsuarioValueObject usuarioVO) throws AlmacenDocumentoTramitacionException {
        log.info("DocumentosExpedienteAction.duplicarDocumentoAntesEnviarFirmar()");
        DocumentoFirma docFirma = null;
        int tipoDocumento = -1;
        String[] params = (String[]) parametros.get("paramsBBDD");
        String codProcedimiento = (String) parametros.get("codProcedimiento");

        // Comprobamos cual es el plugin de almacen de documentos para el procedimiento
        AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(Integer.toString(usuOrg)).getImplClassPluginProcedimiento(Integer.toString(usuOrg), codProcedimiento);
        // Instanciamos el documento con una clase u otra en funcion de si el plugin de almacenamiento es de gestor documental o es bbdd
        if (!almacen.isPluginGestor()) {
            tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_BBDD;
        } else {
            tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_GESTOR;
        }
        
        log.info("nombreServicioAlmacen: " + almacen.getNombreServicio());
        parametros.put("nombreServicioAlmacen", almacen.getNombreServicio());
        
        // Construimos el mapa con los datos del documento para obtener el objeto Documento de la implementacion correcta (BBDD o Gestor)
        log.info("Construimos el mapa con los datos del documento para obtener el objeto Documento de la implementacion correcta (BBDD o Gestor)");
        Hashtable<String, Object> datos = construirMapaDatosDocumentoTramitacion((HashMap) parametros, tipoDocumento, request, usuarioVO);
        
        log.info("obtener el documento Firma");
        docFirma = DocumentoTramitacionFactoria.getInstance().getDocumentoFirma(datos, tipoDocumento);
        
        codDocOriginal = docFirma.getCodDocumento();
        
        // Llamamos a la funcion que duplica el documento segun el plugin de almacen documental
        log.info("Llamamos a la funcion que duplica el documento segun el plugin de almacen documental");
        almacen.setDocumentoDuplicado(docFirma, params);

        return docFirma;
    }
    public static Hashtable<String, Object> construirMapaDatosDocumentoTramitacion(Map<String, Object> parametros, int tipoDocumento, RequestInformationHolder request, UsuarioValueObject usuarioVO) {
        Hashtable mapaDatos = new Hashtable();
        
                   
        Integer codDocumento = Integer.parseInt(parametros.get("codDocumento").toString());
        String codProcedimiento = (String) parametros.get("codProcedimiento");
        Integer codMunicipio = Integer.parseInt(parametros.get("codMunicipio").toString());
        Integer ejercicio = Integer.parseInt(parametros.get("ejercicio").toString());
        String numExpediente = (String) parametros.get("numExpediente");
        Integer codTramite = Integer.parseInt(parametros.get("codTramite").toString());
        Integer ocurrenciaTramite = Integer.parseInt(parametros.get("ocurrenciaTramite").toString());
                   
            
        String[] params = (String[])parametros.get("paramsBBDD");
        String nombreGestor = (String)parametros.get("nombreServicioAlmacen");
        log.debug(("esDocumentoParaRelacion codMunicipio: " + codMunicipio + " , codProcedimiento: " + codProcedimiento + ", codTramite: " + codTramite + ", codDocumento: " + codDocumento + ", params: " + params));
    //    boolean paraRelacion = DocumentosAplicacionManager.getInstance().esDocumentoParaRelacion(codMunicipio, codProcedimiento, codTramite, codDocumento, params);
       // log.debug(("paraRelacion: " + paraRelacion));
        mapaDatos.put("codProcedimiento", codProcedimiento);
        mapaDatos.put("codMunicipio", codMunicipio);
        mapaDatos.put("ejercicio", ejercicio);
        mapaDatos.put("numeroExpediente", numExpediente);
        mapaDatos.put("codTramite", codTramite);
        mapaDatos.put("ocurrenciaTramite", ocurrenciaTramite);
        mapaDatos.put("codDocumento", codDocumento);
        mapaDatos.put("nombreDocumento", parametros.get("nombreDocumento"));
        mapaDatos.put("codUsuario", usuarioVO.getIdUsuario());
        mapaDatos.put("fichero", parametros.get("ficheroDoc"));
        mapaDatos.put("perteneceRelacion",  "false");
        mapaDatos.put("params", params);
        if (tipoDocumento == 2) {
            log.debug(("tipoDocumento es de tipo " + tipoDocumento));
            String codigoVisibleTramite = DefinicionTramitesManager.getInstance().getCodigoVisibleTramite(Integer.toString((int)usuarioVO.getOrgCod()), codProcedimiento, String.valueOf((Object)codTramite), params);
            log.debug(("codigoVisibleTramite: " + codigoVisibleTramite));
            String nombreProcedimiento = DefinicionProcedimientosManager.getInstance().getDescripcionProcedimiento(codProcedimiento, params);
            log.debug(("nombreProcedimiento: " + nombreProcedimiento));
            mapaDatos.put("codigoVisibleTramite", codigoVisibleTramite);
            mapaDatos.put("codificacion", "UTF_8");
            mapaDatos.put("extension", parametros.get("extension"));
            mapaDatos.put("tipoMime", parametros.get("tipoMimeOriginal"));
            ResourceBundle bundleDocumentos = ResourceBundle.getBundle((String)"documentos");
            String carpetaRaiz = bundleDocumentos.getString("Almacenamiento/" + usuarioVO.getOrgCod() + "/" + nombreGestor + "/carpetaRaiz");
            log.debug(("carpetaRaiz: " + carpetaRaiz));
            String descripcionOrganizacion = usuarioVO.getOrg();
            ArrayList listaCarpetas = new ArrayList();
            listaCarpetas.add(carpetaRaiz);
            listaCarpetas.add((usuarioVO.getOrgCod() + "-" + descripcionOrganizacion));
            listaCarpetas.add((codProcedimiento + "-" + nombreProcedimiento));
            listaCarpetas.add(((String)parametros.get("numExpediente")).replaceAll("/", "-"));
            mapaDatos.put("listaCarpetas", listaCarpetas);
        }
        return mapaDatos;
    }

    private Map<String, Object> obtenerParametrosCrearCSV(TramitacionExpedientesValueObject tEVO, RequestInformationHolder request,String portafirmas, AdaptadorSQLBD adapt, UsuarioValueObject usuario) throws Exception {
        Map<String, Object> parametros = new HashMap<String, Object>();
       
        String sessionId = request.getSessionId();
        String origen = request.getOrigen();
        String contextPath = request.getContextPath();
          // Asignamos los valores al mapa
        parametros.put("usuario", usuario);
        parametros.put("sessionId", sessionId);
        parametros.put("contextPath", contextPath);
        //parametros.put("codigo", codigoCampo);
        parametros.put("origen", origen);
        String nombreDocumento = request.getNombreDocumento();
        String tipoMimeOriginal = null;
        String extension = null;
        

        String editorTexto = "ODT";
        
        tipoMimeOriginal = DocumentOperations.determinarTipoMimePlantilla(
                editorTexto, nombreDocumento);
        extension = MimeTypes.guessExtensionFromMimeType(tipoMimeOriginal);
         String[] params = new String[7];
        params[0] ="ORACLE";
        // Asignamos los valores al mapa
        parametros.put("usuario", usuario);
        parametros.put("paramsBBDD", params);
        parametros.put("sessionId", sessionId);
        parametros.put("contextPath", contextPath);
        parametros.put("tramitesVO", tEVO);
        parametros.put("codDocumento", tEVO.getCodigoDocumentoAnterior());
        parametros.put("codigo", tEVO.getCodigoDocumentoAnterior());
        parametros.put("codMunicipio", tEVO.getCodMunicipio());
        parametros.put("ejercicio", tEVO.getEjercicio());
        parametros.put("numExpediente", tEVO.getNumeroExpediente());
        parametros.put("codTramite", tEVO.getCodTramite());
        parametros.put("ocurrenciaTramite", tEVO.getOcurrenciaTramite());
        parametros.put("codProcedimiento", tEVO.getCodProcedimiento());
        parametros.put("isExpHistorico", false);
        parametros.put("editorTexto", editorTexto);
        parametros.put("nombreDocumento",tEVO.getDescDocumento());
        parametros.put("nombreFichero",tEVO.getDescDocumento());
        parametros.put("extension", extension);
        parametros.put("tipoMimeOriginal", tipoMimeOriginal);
        
        
        log.debug("Parametros obtenidos del nombre del documento en el metodo obtenerParametrosCrearCSV");
        log.debug("nombreDocumento = " + request.getNombreDocumento());
        log.debug("tipoMimeOriginal = " + tipoMimeOriginal);
        log.debug("extension = " + extension);
        
        
        if (portafirmas != null && !"".equals(portafirmas) && "LAN".equals(portafirmas)) {
            String OIDDocumento = "";
            log.debug("Tiene seleccionada la propiedad Portafirmas a Lanbide y se devolvera el OID del documento a enviar");
           
        
            try {
                //Se obtiene el OID del documento de la tabla MELANBIDE_DOKUSI_RELDOC_TRAMIT
                OIDDocumento = MeLanbide08Manager.getInstance().obtenerOIDDocumento(Integer.valueOf(tEVO.getCodMunicipio()), Integer.valueOf(tEVO.getEjercicio()),
                        tEVO.getCodProcedimiento(), tEVO.getNumeroExpediente(), Integer.valueOf(tEVO.getCodTramite()), Integer.valueOf(tEVO.getOcurrenciaTramite()),
                        tEVO.getCodigoDocumentoAnterior().toString(), adapt);
            } catch (Exception ex) {
                 log.error("Se produce un error a la hora de obtener el OID del documento. " + ex.getMessage());
                 throw new Exception ("Se produce un error a la hora de obtener el OID del documento. " + ex.getMessage());
            }

            log.debug("OID Documento vale: " + OIDDocumento);
            parametros.put("OIDDocumento", OIDDocumento.trim());
            
            AlmacenDocumento almacen = AlmacenDocumentoTramitacionFactoria.getInstance(
                    Integer.toString(usuario.getOrgCod())).getImplClassPluginProcedimiento(Integer.toString(usuario.getOrgCod()),tEVO.getCodProcedimiento());
            
            parametros.put("nombreServicioAlmacen", almacen.getNombreServicio());
            
            
        }

        return parametros;
  }
     
   
   private boolean existeDocumentoMetadatoCSV(Map<String, Object> parametros) throws TechnicalException {
        boolean existe = false;

        Integer codDocumento = Integer.parseInt(parametros.get("codDocumento").toString());
        String codProcedimiento = (String) parametros.get("codProcedimiento");
        Integer codMunicipio = Integer.parseInt(parametros.get("codMunicipio").toString());
        Integer ejercicio = Integer.parseInt(parametros.get("ejercicio").toString());
        String numExpediente = (String) parametros.get("numExpediente");
        Integer codTramite = Integer.parseInt(parametros.get("codTramite").toString());
        Integer ocurrenciaTramite = Integer.parseInt(parametros.get("ocurrenciaTramite").toString());
        UsuarioValueObject usuario = (UsuarioValueObject) parametros.get("usuario");

        existe = DocumentoManager.getInstance().existeMetadatoCSV(usuario.getParamsCon(), ConstantesDatos.SUBCONSULTA_E_CRD_ID_METADATO_PK,
                codMunicipio,
                codProcedimiento,
                ejercicio,
                numExpediente,
                codTramite,
                ocurrenciaTramite,
                codDocumento);

        if (log.isDebugEnabled()) {
            log.debug(String.format("Existe metadato?: %b", existe));
        }

        return existe;
    }
   
    
    private String generarUriCsvDescargaFichero(Map<String, Object> parametros) {
        DocumentoManager documentoManager = DocumentoManager.getInstance();
        Map<String, Object> paramUrlCsv = new HashMap<String, Object>();
        
        String origen = (String) parametros.get("origen");
        
        if (ConstantesDatos.ORIGEN_ELIMINAR_DOCUMENTO_EXPEDIENTE.equalsIgnoreCase(origen)) {
            paramUrlCsv.put("codigo", parametros.get("codigo"));
            paramUrlCsv.put("nombreFich", parametros.get("nombreFicheroNuevo"));
            paramUrlCsv.put("opcion", "0");
            paramUrlCsv.put("expHistorico", parametros.get("isExpHistorico"));
            paramUrlCsv.put("codMunicipio", parametros.get("codMunicipio"));
            paramUrlCsv.put("ejercicio", parametros.get("ejercicio"));
            paramUrlCsv.put("numExpediente", parametros.get("numExpediente"));
        } else if (ConstantesDatos.ORIGEN_ELIMINAR_DOCUMENTO_TRAMITE.equalsIgnoreCase(origen)) {
            paramUrlCsv.put("codigo", parametros.get("codigo"));
            paramUrlCsv.put("nombreFich", parametros.get("nombreFicheroNuevo"));
            paramUrlCsv.put("opcion", "1");
            paramUrlCsv.put("codMunicipio", parametros.get("codMunicipio"));
            paramUrlCsv.put("ejercicio", parametros.get("ejercicio"));
            paramUrlCsv.put("numExpediente", parametros.get("numExpediente"));
            paramUrlCsv.put("codTramite", parametros.get("codTramite"));
            paramUrlCsv.put("ocurrenciaTramite", parametros.get("ocurrenciaTramite"));
        }
        
        return documentoManager.crearURLCodigoSeguroVerificacion(paramUrlCsv);
    }

    private void procesarNotificacionesSegundoPlano(int codOrganizacion,  RetornoResultadosFiltros retorno, AdaptadorSQLBD adapt, UsuarioValueObject usuario, RequestInformationHolder request ) throws Exception {

//Llamamos el metodo del core para modificar el estado esto ira en el job
         for (int i = 0; i < retorno.getLstRegistros().size(); i++) {
            MeLanbide08LogJobManager.getInstance().actualizarLineasLogJob(adapt, retorno.getLstRegistros().get(i).getIdLog(), "TRAMITANDO", "");
           try{
             //Actualizamos el log a iniciados
             String[] params = new String[3];
         params[0] ="ORACLE";
         //Creamos el objeto con la información del documento a modificar el estado
         TramitacionExpedientesValueObject tEVO = new TramitacionExpedientesValueObject();
         tEVO.setCodigoDocumentoAnterior(retorno.getLstRegistros().get(i).getCodigoDocumentoAnterior());
         tEVO.setCodMunicipio(retorno.getLstRegistros().get(i).getCodigoMunicipio());
         tEVO.setCodProcedimiento(retorno.getLstRegistros().get(i).getProcedimiento());
         tEVO.setNumeroExpediente(retorno.getLstRegistros().get(i).getNumExpediente());
         tEVO.setCodTramite(retorno.getLstRegistros().get(i).getCodTramite());
         tEVO.setOcurrenciaTramite(retorno.getLstRegistros().get(i).getOcurrenciaTramite());
         tEVO.setNumeroExpediente(retorno.getLstRegistros().get(i).getNumExpediente());
         tEVO.setEjercicio(retorno.getLstRegistros().get(i).getEjercicio());
         tEVO.setCodOrganizacion(String.valueOf(codOrganizacion));
         tEVO.setEstadoFirma("O");
         tEVO.setCodigoDocumento(retorno.getLstRegistros().get(i).getCodigoDocumentoAnterior());
         tEVO.setDescDocumento(retorno.getLstRegistros().get(i).getDescDocumento());
         //Creamos el csv 
        
             
         DocumentoTramiteVO documentoCSV = crearCsv(tEVO, request,adapt,usuario);
        
         if (documentoCSV !=null && documentoCSV.getCodigo()!=null){
         log.debug("documentoCSV.getCodDocumento() vale " + documentoCSV.getCodigo());
       //  tEVO.setEstadoFirma("V");   
         //Enviamos el documento convertido al portafirmas y creamos los campos necessarios en la bbdd
         tEVO.setCodigoDocumento(documentoCSV.getCodigo());
             
         
                log.debug("tramExpVO.getCodDocumento() vale " + tEVO.getCodDocumento());
                 log.debug("tramExpVO.getCodigoDocumentoAnterior() vale " + tEVO.getCodigoDocumentoAnterior());
                 log.debug("usuario.getIdUsuario() vale " + usuario.getIdUsuario());
                 log.debug("usuario.getNombreUsu() vale " + usuario.getNombreUsu());
                
                int resultado = MeLanbide08Manager.getInstance().cambiarEstadoFirmaDocumentoCRD(tEVO, usuario.getIdUsuario(), "LAN",params, adapt);
                if (resultado > 0) {
                    //Datos documento TODO poner LOG correcto todo
                    MeLanbide08LogJobManager.getInstance().actualizarLineasLogJob(adapt, retorno.getLstRegistros().get(i).getIdLog(), "OK", null);
                    log.info("Se ha modificado el documento ");
                } else {
                     MeLanbide08LogJobManager.getInstance().actualizarLineasLogJob(adapt, retorno.getLstRegistros().get(i).getIdLog(), "ERROR", "No se ha modificado el estado del documento");
                    //Datos documento TODO poner log 
                    log.error("No se ha modificado el estado del documento");
                }
             
             
         } else{
            MeLanbide08LogJobManager.getInstance().actualizarLineasLogJob(adapt, retorno.getLstRegistros().get(i).getIdLog(), "ERROR", "Error al enviar a firmar no se ha generado el documento");
             log.error("Error al intentar crear el csv");
         }
 
         
        } catch (TechnicalException e){
             log.error("Error almacen dokusi");
            MeLanbide08LogJobManager.getInstance().actualizarLineasLogJob(adapt, retorno.getLstRegistros().get(i).getIdLog(), "ERROR", "Error al dokusi: "+e.getMessage());
 
         }  catch (InvocationTargetException e){
             log.error("Error al invocar un metodo");
            MeLanbide08LogJobManager.getInstance().actualizarLineasLogJob(adapt, retorno.getLstRegistros().get(i).getIdLog(), "ERROR", "Error al intentar invocar un metodo: "+e.getMessage());
 
         }  catch (NoSuchMethodError e){
             log.error("Error al invocar un metodo");
            MeLanbide08LogJobManager.getInstance().actualizarLineasLogJob(adapt, retorno.getLstRegistros().get(i).getIdLog(), "ERROR", "Error al intentar invocar un metodo: "+e.getMessage());
 
         }  catch (Exception e){
            log.error("Error al enviar a firmar ");
            MeLanbide08LogJobManager.getInstance().actualizarLineasLogJob(adapt, retorno.getLstRegistros().get(i).getIdLog(), "ERROR", "Error al enviar a firmar : "+e.getMessage());

         }
      //  method.invoke(coreClass,tEVO,usuario.getIdUsuario(), codOrganizacion, "LAN", params);
         }  

    }
   
}