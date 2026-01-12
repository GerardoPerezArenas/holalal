package es.altia.flexia.integracion.moduloexterno.melanbide09;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.sge.persistence.manual.TramitacionExpedientesDAO;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.flexia.integracion.moduloexterno.melanbide09.dao.MeLanbide09DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide09.manager.MeLanbide09LogJobManager;
import es.altia.flexia.integracion.moduloexterno.melanbide09.manager.MeLanbide09Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide09.util.RequestInformationHolder;
import es.altia.flexia.integracion.moduloexterno.melanbide09.vo.ComboVO;
import es.altia.flexia.integracion.moduloexterno.melanbide09.vo.FiltrosVO;
import es.altia.flexia.integracion.moduloexterno.melanbide09.vo.ResultadoConsultaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide09.vo.RetornoResultadosFiltros;
import java.util.Date;
import java.util.List;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;
import es.altia.flexia.integracion.moduloexterno.pluginnotificacionplatea.PluginNotificacionPlateaNotificacionDAO;
import es.altia.flexia.notificacion.persistence.AutorizadoNotificacionManager;
import es.altia.flexia.notificacion.persistence.NotificacionDAO;
import es.altia.flexia.notificacion.plugin.FactoriaPluginNotificacion;
import es.altia.flexia.notificacion.plugin.PluginNotificacion;
import es.altia.flexia.notificacion.vo.NotificacionVO;
import es.altia.technical.PortableContext;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;



public class MELANBIDE09 extends ModuloIntegracionExterno{
private static final Logger log = LogManager.getLogger(MELANBIDE09.class);
SimpleDateFormat formatDateLog = new SimpleDateFormat("yyyyMMddHHmmssSSS"); 
  protected static Config registroConf = ConfigServiceHelper.getConfig("Registro");

   public String cargarPantallaPrincipalConsultaExpedientesSinNotificar(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("cargarPantallaPrincipalConsultaExpedientesSinNotificar - Begin()" + formatDateLog.format(new Date()));
        String respuestaServicio = "/jsp/extension/melanbide09/pantallaprincipalconsultar.jsp";
         AdaptadorSQLBD adapt = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (Exception ex) {
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
            
       
        List<ComboVO> listaProcedimiento = MeLanbide09Manager.getInstance().getComboProcedimiento(codOrganizacion,usuario.getIdUsuario(), adapt);
        List<ComboVO> listaTramites = new ArrayList<ComboVO>();
        request.setAttribute("listaProcedimiento",listaProcedimiento);
        } catch (Exception ex) {
            log.error("Se ha producido un error a cargar la pantalla principal de Consulta de estado de expedientes sin notificar ", ex);
            request.setAttribute("mensajeInicial", "Se ha presentado un error al cargar la pantalla de consulta de estado de expedientes sin notificar ...Contacte con el ADMIN para mas detalles. " + ex.getMessage());
        }
        log.info("cargarPantallaPrincipalConsultaExpedientesSinNotificar - End()" + formatDateLog.format(new Date()) + " " + respuestaServicio);
        return respuestaServicio;
    }
   
   
   
   public String cargarDatosPrincipalProcesarNoNotificados(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("cargarDatosPrincipalProcesarNoNotificados - Begin()" + formatDateLog.format(new Date()));
        String respuestaServicio = "/jsp/extension/melanbide09/pantallaprincipalprocesar.jsp";
         AdaptadorSQLBD adapt = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (Exception ex) {
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
            
       
        List<ComboVO> listaProcedimiento = MeLanbide09Manager.getInstance().getComboProcedimiento(codOrganizacion,usuario.getIdUsuario(), adapt);
        List<ComboVO> listaTramites = new ArrayList<ComboVO>();
        request.setAttribute("listaProcedimiento",listaProcedimiento);
        } catch (Exception ex) {
            log.error("Se ha producido un error a cargar la pantalla principal de Consulta de estado de expedientes sin notificar ", ex);
            request.setAttribute("mensajeInicial", "Se ha presentado un error al cargar la pantalla de consulta de estado de expedientes sin notificar ...Contacte con el ADMIN para mas detalles. " + ex.getMessage());
        }
        log.info("cargarDatosPrincipalProcesarNoNotificados - End()" + formatDateLog.format(new Date()) + " " + respuestaServicio);
        return respuestaServicio;
    }
   
   
    public String obtenerTramitesDesplegable(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("obtenerTramitesDesplegable - Begin()" + formatDateLog.format(new Date()));
        String respuestaServicio = "/jsp/extension/melanbide09/pantallaprincipalconsultar.jsp";
         AdaptadorSQLBD adapt = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (Exception ex) {
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
            
        String codProcedimiento = (String) request.getParameter("codProcedimiento");
        List<ComboVO> listaTramite = MeLanbide09Manager.getInstance().getComboTramites(codOrganizacion,usuario.getIdUsuario(), codProcedimiento, adapt);
        request.setAttribute("listaTramite",listaTramite);
        } catch (Exception ex) {
            log.error("Se ha producido un error a cargar la pantalla principal de Consulta de estado de expedientes sin notificar ", ex);
            request.setAttribute("mensajeInicial", "Se ha presentado un error al cargar la pantalla de consulta de estado de expedientes sin notificar ...Contacte con el ADMIN para mas detalles. " + ex.getMessage());
        }
        log.info("obtenerTramitesDesplegable - End()" + formatDateLog.format(new Date()) + " " + respuestaServicio);
        return respuestaServicio;
    }
    
    
          public void cargarDatosPrincipalConsultaSinNotificarFiltros(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) throws InterruptedException {
        

        log.info("cargarDatosPrincipalConsultaSinNotificarFiltros - Begin()" + new Date().toString());
        AdaptadorSQLBD adapt = null;
        RetornoResultadosFiltros retorno = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (Exception ex) {
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
        String fechaEnvioPeticionDesde = "";
        String fechaEnvioPeticionHasta = "";
        String tramite = "";
        
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
            tramite = (String) request.getParameter("tramite");
            fechaEnvioPeticionDesde = (String) request.getParameter("fechaEnvioPeticionDesde");
            fechaEnvioPeticionHasta = (String) request.getParameter("fechaEnvioPeticionHasta");
  
        
         FiltrosVO filtros = new FiltrosVO(startTable, finishTable, numEntries, draw,
                    ejercicio, procedimiento,numeroExpediente, tramite, null, null,null,fechaEnvioPeticionDesde,fechaEnvioPeticionHasta,null);

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


         List<ComboVO> listaProcedimiento = MeLanbide09Manager.getInstance().getComboProcedimiento(codOrganizacion,usuario.getIdUsuario(), adapt);
         //cargar lista de resultados 
         retorno = MeLanbide09Manager.getInstance().getLog(codOrganizacion, adapt, filtros,listaProcedimiento);
         
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
        log.info("cargarDatosPrincipalConsultaSinNotificarFiltros - End()" + new Date().toString());

      }
     
          
           public void cargarDatosPrincipalConsultaNoNotificadosFiltrosSinPaginar(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) throws InterruptedException {
        

        log.info("cargarDatosPrincipalConsultaNoNotificadosFiltrosSinPaginar - Begin()" + new Date().toString());
        AdaptadorSQLBD adapt = null;
        RetornoResultadosFiltros retorno = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (Exception ex) {
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
        String fechaEnvioPeticionDesde = "";
        String fechaEnvioPeticionHasta = "";
        String tramite = "";
        
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
            tramite = (String) request.getParameter("tramite");
            fechaEnvioPeticionDesde = (String) request.getParameter("fechaEnvioPeticionDesde");
            fechaEnvioPeticionHasta = (String) request.getParameter("fechaEnvioPeticionHasta");
  
        
         FiltrosVO filtros = new FiltrosVO(startTable, finishTable, numEntries, draw,
                    ejercicio, procedimiento,numeroExpediente, tramite, null, null,null,fechaEnvioPeticionDesde,fechaEnvioPeticionHasta,null);

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


         List<ComboVO> listaProcedimiento = MeLanbide09Manager.getInstance().getComboProcedimiento(codOrganizacion,usuario.getIdUsuario(), adapt);
         //cargar lista de resultados 
         retorno = MeLanbide09Manager.getInstance().getLogSinPaginar(codOrganizacion, adapt, filtros,listaProcedimiento);
         
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
        log.info("cargarDatosPrincipalConsultaNoNotificadosFiltrosSinPaginar - End()" + new Date().toString());

      }
           
           
           
           
      public void procesarNoNotificadosFiltrosSinPaginar(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) throws InterruptedException {
        

        log.info("cargarDatosPrincipalConsultaNoNotificadosFiltrosSinPaginar - Begin()" + new Date().toString());
        AdaptadorSQLBD adapt = null;
        RetornoResultadosFiltros retorno = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (Exception ex) {
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
        String fechaEnvioPeticionDesde = "";
        String fechaEnvioPeticionHasta = "";
        String tramite = "";
        
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
            tramite = (String) request.getParameter("tramite");
            fechaEnvioPeticionDesde = (String) request.getParameter("fechaEnvioPeticionDesde");
            fechaEnvioPeticionHasta = (String) request.getParameter("fechaEnvioPeticionHasta");
  
        
         FiltrosVO filtros = new FiltrosVO(startTable, finishTable, numEntries, draw,
                    ejercicio, procedimiento,numeroExpediente, tramite, null, null,null,fechaEnvioPeticionDesde,fechaEnvioPeticionHasta,null);

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


         List<ComboVO> listaProcedimiento = MeLanbide09Manager.getInstance().getComboProcedimiento(codOrganizacion,usuario.getIdUsuario(), adapt);
         //cargar lista de resultados 
         retorno = MeLanbide09Manager.getInstance().getLogSinPaginar(codOrganizacion, adapt, filtros,listaProcedimiento);
         
         
         //Registramos las notificaciones en el log como iniciadas
         Date fechaEnviado =new Date();
         //Creamos las lineas en la tabla de logs de los documentos enviados a firmar
          for (int i = 0; i < retorno.getLstRegistros().size(); i++) {
           retorno.getLstRegistros().get(i).setFechaEnviadoJob(fechaEnviado);
           retorno.getLstRegistros().get(i).setIdLog(MeLanbide09LogJobManager.getInstance().insertarLineasLogJob(adapt,retorno.getLstRegistros().get(i)));
          }
          
           //Ejecutamos el proceso en segundo plano 
           final int codOrganizacionFinal= codOrganizacion;
           final RetornoResultadosFiltros retornoFinal = retorno;
           final AdaptadorSQLBD adaptFinal = adapt;
           final RequestInformationHolder requestFinal = new RequestInformationHolder();
           final UsuarioValueObject usuarioFinal = usuario;
           final String actoNotificado = (String) request.getParameter("actoNotificado");
           final String textoNotificacion = (String) request.getParameter("textoNotificacion");
           
           //Assignamos los valores del request en un objecto para recuperarlos luego ya que un thread pierde el contexto 
           requestFinal.setSessionId(request.getSession().getId());
           requestFinal.setOrigen(request.getParameter("origen"));
           requestFinal.setContextPath(request.getContextPath());
          
           
          new Thread(new Runnable() {
     @Override
     public void run() {
         try {
             
             procesarNotificacionesSegundoPlano (codOrganizacionFinal,  retornoFinal,adaptFinal, usuarioFinal, requestFinal,actoNotificado,textoNotificacion);
         } catch (Exception ex) {
            log.error("Error al intentar ejecutar en segundo plano");
         }
     }
        }).start();
        
        
      retorno = new RetornoResultadosFiltros();

          
          
          
          retorno = MeLanbide09Manager.getInstance().getLogSinPaginar(codOrganizacion, adapt, filtros,listaProcedimiento); 
          
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
        log.info("procesarNoNotificadosFiltrosSinPaginar - End()" + new Date().toString());

      }
     

    /**
     * Operación que recupera los datos de conexión a la BBDD
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
                te.printStackTrace();
                log.error("*** AdaptadorSQLBD: " + te.toString());
            }catch(SQLException e){
                e.printStackTrace();
            }finally{
                try{
                    if(st!=null) st.close();
                    if(rs!=null) rs.close();
                    if(conGenerico!=null && !conGenerico.isClosed())
                    conGenerico.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }//try-catch
            }// finally
            if(log.isDebugEnabled()) log.debug("getConnection() : END");
         }// synchronized
        return adapt;
     }//getConnection
    
        private void procesarNotificacionesSegundoPlano(int codOrganizacion,  RetornoResultadosFiltros retorno, AdaptadorSQLBD adapt, UsuarioValueObject usuario, RequestInformationHolder request ,String actoNotificado,String textoNotificacion) throws Exception {
        
          //Procedemos a notificar y avanzar el tramite 
          for (int i = 0; i < retorno.getLstRegistros().size(); i++) {
            MeLanbide09LogJobManager.getInstance().actualizarLineasLogJob(adapt, retorno.getLstRegistros().get(i).getIdLog(), "TRAMITANDO", "");
           try{
  
           //Creamos el adaptador 
              String[] params = usuario.getParamsCon();
               
           //Creamos la notificacion por defecto 
           NotificacionVO notificacionVO = new NotificacionVO();
           notificacionVO.setNumExpediente(retorno.getLstRegistros().get(i).getNumExpediente());
           notificacionVO.setCodigoProcedimiento(retorno.getLstRegistros().get(i).getProcedimiento() );
           notificacionVO.setEjercicio(Integer.valueOf(retorno.getLstRegistros().get(i).getEjercicio()));
           notificacionVO.setCodigoMunicipio(Integer.valueOf(retorno.getLstRegistros().get(i).getCodigoMunicipio()));
           notificacionVO.setCodigoTramite(Integer.valueOf(retorno.getLstRegistros().get(i).getCodTramite()));
           notificacionVO.setOcurrenciaTramite(Integer.valueOf(retorno.getLstRegistros().get(i).getOcurrenciaTramite()));
          
           //Creamos la notificiacion por defecto
           // Se crea la notificación por defecto si es que no existe
            PluginNotificacion pluginNotificacion = FactoriaPluginNotificacion.getImpl(Integer.toString(usuario.getOrgCod()));
            pluginNotificacion.crearNotificacionDefecto(notificacionVO,params);
            
            notificacionVO = pluginNotificacion.getNotificacion(notificacionVO, params);   
            
            
            //Actualizamos la notificacion
            boolean retornoGrabar = false;
            notificacionVO.setActoNotificado(actoNotificado);  
            notificacionVO.setTextoNotificacion(textoNotificacion);
            
            log.debug("Intentamos grabar la notificación: "+notificacionVO.getNumExpediente());
            retornoGrabar=pluginNotificacion.actualizarNotificacion(notificacionVO, params);
                  
           //Añadimos autorizado 
            adapt = new AdaptadorSQLBD(params);
                Connection con = adapt.getConnection();
                try{
             for (int j = 0; j < notificacionVO.getAutorizados().size(); j++) {
                 notificacionVO.getAutorizados().get(j).setCodigoNotificacion(notificacionVO.getCodigoNotificacion());
                    AutorizadoNotificacionManager.getInstance().insertarAutorizado(notificacionVO.getAutorizados().get(j), con);
                    }}catch (Exception e) {
                        
                    } finally {
                    if (con!=null){
                        con.close();
                    }
                }       
                  //Si no hay interesados se sigue hacia delante igualmente
                  if(retornoGrabar==true || notificacionVO.getAutorizados().isEmpty()){
                 //Procedemos a enviar la notificiacion
                 notificacionVO.setCodDepartamento(Integer.toString(usuario.getDepCod()));    
                  
                 Boolean res = false;
                  log.debug("Intentamos enviar: "+notificacionVO.getNumExpediente());
            
                  String codigoTipoNotificacion = NotificacionDAO.getInstance().getTipoNotificacion(notificacionVO, params);
                     log.info("codigoTipoNotificacion: " + codigoTipoNotificacion);
                     notificacionVO.setCodigoTipoNotificacion(codigoTipoNotificacion);
                 
                     res=pluginNotificacion.enviarNotificacion(notificacionVO, params);
                     if(res==true || notificacionVO.getAutorizados().isEmpty())
                     {
                        log.info("Notificacion enviada correctamente: "+notificacionVO.getNumExpediente());
                         pluginNotificacion.guardarEstadoNotificacionEnviada(notificacionVO, params);
                         //TODO Ahora iria el codigo para avanzar el tramite.
                           log.info("OK: "+notificacionVO.getNumExpediente());
                           
                          //Cerramos el tramite 
                           log.info("Cerramos el tramite: "+notificacionVO.getNumExpediente());
                           MeLanbide09Manager.getInstance().cerrarTramite(codOrganizacion, notificacionVO.getNumExpediente(), String.valueOf(notificacionVO.getCodigoTramite()) , notificacionVO.getCodigoProcedimiento(), adapt);
                           
                           //Recuperamos los tramites a abrir
                           List<ResultadoConsultaVO>  nuevosTramites =  MeLanbide09Manager.getInstance().getTramitesSalida(codOrganizacion, adapt, notificacionVO.getCodigoProcedimiento(), notificacionVO.getCodigoTramite());
                           
                             log.info("Abrimos el tramite: "+notificacionVO.getNumExpediente());
                           //Abrimos los tramites 
                           for (int x = 0; x < nuevosTramites.size(); x++) {
                               
                              MeLanbide09Manager.getInstance().abrirTramite(codOrganizacion,notificacionVO.getNumExpediente(), nuevosTramites.get(x).getCodTramite(), notificacionVO.getCodigoProcedimiento(), String.valueOf(retorno.getLstRegistros().get(i).getUtr()),usuario.getIdUsuario(), adapt);
                           }
                           //Si hemos llegado aqui sin ningun error es que todo ha ido bien añadimos al log que todo ha ido correctamente
                          MeLanbide09LogJobManager.getInstance().actualizarLineasLogJob(adapt, retorno.getLstRegistros().get(i).getIdLog(), "OK", null);

                     }   else {
                           MeLanbide09LogJobManager.getInstance().actualizarLineasLogJob(adapt, retorno.getLstRegistros().get(i).getIdLog(), "ERROR", "No se ha podido grabar la notificiacion");
                    
                             log.error("No se ha enviado la notificion: "+notificacionVO.getNumExpediente());
                     } 
                      
                      
                  } //if (retornoGrabar==true)
                  else {
                       MeLanbide09LogJobManager.getInstance().actualizarLineasLogJob(adapt, retorno.getLstRegistros().get(i).getIdLog(), "ERROR", "No se ha podido grabar la notificiacion");
                  
                    log.error("No se ha modificado el estado del documento: "+notificacionVO.getNumExpediente());
                      
                  }
           
           
                } catch (Exception ex) {
                      log.error("Error",ex);
                      MeLanbide09LogJobManager.getInstance().actualizarLineasLogJob(adapt, retorno.getLstRegistros().get(i).getIdLog(), "ERROR", "Error: "+ex.getMessage());
                     }
           }
        
        
        }
    
     public String cargarPantallaPrincipalConsultaLog(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("cargarPantallaPrincipalConsultarLog - Begin()" + formatDateLog.format(new Date()));
        String respuestaServicio = "/jsp/extension/melanbide09/pantallaprincipalconsultarlog.jsp";
         AdaptadorSQLBD adapt = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (Exception ex) {
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
            
       
        List<ComboVO> listaProcedimiento = MeLanbide09Manager.getInstance().getComboProcedimiento(codOrganizacion,usuario.getIdUsuario(), adapt);
        request.setAttribute("listaProcedimiento",listaProcedimiento);
        } catch (Exception ex) {
            log.error("Se ha producido un error a cargar la pantalla principal de Consulta de estado de log de documentos ", ex);
            request.setAttribute("mensajeInicial", "Se ha presentado un error al cargar la pantalla de consulta de estado de log de documentos ...Contacte con el ADMIN para mas detalles. " + ex.getMessage());
        }
        log.info("cargarPantallaPrincipalConsultarLog - End()" + formatDateLog.format(new Date()) + " " + respuestaServicio);
        return respuestaServicio;
    }
   
         public void cargarDatosConsultaEstadoLogJobNotificar(int codOrganizacion , int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("cargarDatosConsultaEstadoLogJobFirma - Begin()" + new Date().toString());
        AdaptadorSQLBD adapt = null;
        RetornoResultadosFiltros retorno = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (Exception ex) {
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
        String tramite="";
 
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
            ejercicio = (String) request.getParameter("ejercicio");
            procedimiento = (String) request.getParameter("procedimiento");
             numeroExpediente = (String) request.getParameter("numeroExpediente");
            tramite = (String) request.getParameter("tramite");
            fechaEnvioPeticionDesde = (String) request.getParameter("fechaEnvioPeticionDesde");
            fechaEnvioPeticionHasta = (String) request.getParameter("fechaEnvioPeticionHasta");
  
        
         FiltrosVO filtros = new FiltrosVO(startTable, finishTable, numEntries, draw,
                    ejercicio, procedimiento,numeroExpediente, tramite, null, null,null,fechaEnvioPeticionDesde,fechaEnvioPeticionHasta,estadoFirma);


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

         List<ComboVO> listaProcedimiento = MeLanbide09Manager.getInstance().getComboProcedimiento(codOrganizacion,usuario.getIdUsuario(), adapt);
         //cargar lista de resultados 
         retorno = MeLanbide09LogJobManager.getInstance().getLog(codOrganizacion, adapt, filtros,listaProcedimiento);
         
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
}

