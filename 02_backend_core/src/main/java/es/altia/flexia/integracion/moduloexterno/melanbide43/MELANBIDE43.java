package es.altia.flexia.integracion.moduloexterno.melanbide43;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lanbide.lan6.errores.bean.ErrorBean;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.melanbide43.i18n.MeLanbide43I18n;
import es.altia.flexia.integracion.moduloexterno.melanbide43.manager.MeLanbide43Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide43.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide43.util.ConstantesMeLanbide43;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.DatosAvisoCSRegexlan;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.FaseVO;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.FilaFaseVO;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.FilaListadoMisGestiones;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.FilaLlamadasMisGestVO;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.FilaNotificacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.Participantes;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.ProcedimientoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.TramiteVO;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExternoCampoSupFactoria;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExternoParamAdicionales;
import es.altia.flexia.integracion.moduloexterno.plugin.camposuplementario.IModuloIntegracionExternoCamposFlexia;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.SalidaIntegracionVO;
import es.altia.flexia.registro.digitalizacion.lanbide.util.GestionAdaptadoresLan6Config;
import es.altia.technical.PortableContext;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.lanbide.lan6.adaptadoresPlatea.dokusi.beans.Lan6Documento;
import es.lanbide.lan6.adaptadoresPlatea.dokusi.servicios.Lan6DokusiServicios;
import es.lanbide.lan6.adaptadoresPlatea.excepciones.Lan6Excepcion;
import es.lanbide.lan6.adaptadoresPlatea.excepciones.Lan6InformarConsultaExcepcion;
import es.lanbide.lan6.adaptadoresPlatea.informarConsulta.beans.Lan6Aportacion;
import es.lanbide.lan6.adaptadoresPlatea.informarConsulta.beans.Lan6AportacionDoc;
import es.lanbide.lan6.adaptadoresPlatea.informarConsulta.beans.Lan6Expediente;
import es.lanbide.lan6.adaptadoresPlatea.informarConsulta.beans.Lan6Interesado;
import es.lanbide.lan6.adaptadoresPlatea.informarConsulta.beans.Lan6Notificacion;
import es.lanbide.lan6.adaptadoresPlatea.informarConsulta.beans.Lan6Participacion;
import es.lanbide.lan6.adaptadoresPlatea.informarConsulta.beans.Lan6Tramite;
import es.lanbide.lan6.adaptadoresPlatea.informarConsulta.servicios.Lan6InformarConsultaServicios;
import es.lanbide.lan6.adaptadoresPlatea.utilidades.constantes.Lan6Constantes;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 *
 * @author laura
 */
public class MELANBIDE43  extends ModuloIntegracionExterno {
    //Logger
    private static Logger log = LogManager.getLogger(MELANBIDE43.class);
    ResourceBundle m_Conf = ResourceBundle.getBundle("common");
    
     //Constantes de la clase
    private final String BARRA                              = "/";
    
    private final String MODULO_INTEGRACION = "/MODULO_INTEGRACION/";
    private final String PANTALLA_EXPEDIENTE_SALIDA = "/PANTALLA_EXPEDIENTE/SALIDA";
    
    GsonBuilder gsonB = new GsonBuilder().setDateFormat("dd/MM/yyyy");
    Gson gson = gsonB.serializeNulls().create();
    private static final GestionAdaptadoresLan6Config gestionAdaptadoresLan6Config = new GestionAdaptadoresLan6Config();

    
    // Alta Expedientes via registro platea --> MELANBIDE 42
   public void cargarExpedienteExtension(int codigoOrganizacion, String numeroExpediente, String xml) throws Exception{
        final Class cls = Class.forName("es.altia.flexia.integracion.moduloexterno.melanbide42.MELANBIDE42");
        final Object me43Class = cls.newInstance();
        final Class[] types = {int.class, String.class, String.class};
        final Method method = cls.getMethod("cargarExpedienteExtension", types);
        method.invoke(me43Class, codigoOrganizacion, numeroExpediente, xml);
    }
    
   public String generarMisGestInicio (int codOrganizacion,int codTramite,int ocurrenciaTramite,String numExpediente) throws Exception
   {
       String mensaje = "";
       try{
        if(log.isDebugEnabled()) log.debug("generarMisGestionesInicio() : BEGIN");
        mensaje = MeLanbide43Manager.getInstance().generarMisGestiones(codOrganizacion, codTramite, ocurrenciaTramite, numExpediente, "I",getAdaptSQLBD(String.valueOf(codOrganizacion)));
        if(log.isDebugEnabled()) log.debug("generarMisGestionesInicio() : END");
       }
       catch(Exception x)
       {
           log.error("Error generarMisGestInicio => " + x.getMessage(),x);
           throw x;
       }
        return mensaje;
    }
   
   public String comprobarExpGenerado (int codOrganizacion,int codTramite,int ocurrenciaTramite,String numExpediente) throws Exception
   {
       String codOperacion = "0";       
       AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
       Connection con = null;
    
       
       try {

           con = adaptador.getConnection();
           MeLanbide43Manager meLanbide43Manager = MeLanbide43Manager.getInstance();
           if(log.isDebugEnabled()) log.info("comprobarExpGenerado() : BEGIN");
           String[] datos = numExpediente.split(BARRA);
           // en los expedientes RGI e IMV hay que controlar si son publicables en SEDE para saber si se ejecuta
           String ejercicio = datos[0];
           String codProcedimiento = datos[1];
           int publicar = 1;
        if (codProcedimiento.equalsIgnoreCase("RGI") || codProcedimiento.equalsIgnoreCase("IMV")) {
               publicar = meLanbide43Manager.getCampoNumericoEnteroExpediente(numExpediente, "PUBSEDE", adaptador);
           }
           if (publicar == 1) {
               if (!meLanbide43Manager.comprobarExpGenerado(codOrganizacion, codTramite, ocurrenciaTramite, numExpediente, con)) {
                   log.debug("comprobarExpGenerado: INICIO llamada a generarMisGestiones exp " + numExpediente);
                   codOperacion = meLanbide43Manager.generarMisGestiones(codOrganizacion, codTramite, ocurrenciaTramite, numExpediente, "I", getAdaptSQLBD(String.valueOf(codOrganizacion)));
                   log.debug("comprobarExpGenerado: FIN llamada a generarMisGestiones exp " + numExpediente + " - codOperacion: " + codOperacion);
                   if (!"0".equals(codOperacion)) {
                       codOperacion = "1";
                       log.error("comprobarExpGenerado: error en la creacion del expediente en \"Mis gestiones\" ");
                   }
               }
           }
          
           if (log.isDebugEnabled()) {
               log.info("comprobarExpGenerado() : END");
           }
       }
       catch(Exception ex)
       {
            codOperacion = "1";            
            //meLanbide43Manager.borrarProcesado(id, adaptador);
            //mensaje = MeLanbide43I18n.getInstance().getMensaje(1, "error.errorGen");
            
            //insertar error en registro de errores           
            log.error("Error en la funcion comprobarExpGenerado: ", ex);
            String error = "Error en la funcion comprobarExpGenerado: " + ex.getMessage()!=null?ex.getMessage().toString():"null";
            
            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("COMPEXP_001");
            errorB.setMensajeError("Error al comprobar un expediente en la funcion comprobarExpGenerado");
            errorB.setSituacion("comprobarExpedienteGenerado");

            MeLanbide43Manager.grabarError(errorB, ex.getMessage()!=null?ex.getMessage().toString():"null", ex.toString(), numExpediente);
           //throw x;
       }
       finally
       {
           adaptador.devolverConexion(con);
       }
        return codOperacion;
    }
   
   public String generarMisGestInicioOficio (int codOrganizacion,int codTramite,int ocurrenciaTramite,String numExpediente) throws Exception
   {
       String mensaje = "";
       try{
        if(log.isDebugEnabled()) log.info("generarMisGestInicioOficio() : BEGIN");
        mensaje = generarMisGestionesOficio(codOrganizacion, codTramite, ocurrenciaTramite, numExpediente, "I");
        if(log.isDebugEnabled()) log.info("generarMisGestInicioOficio() : END");
       }
       catch(Exception x)
       {
           throw x;
       }
        return mensaje;
    }
   
   public String generarMisGestAvance (int codOrganizacion,int codTramite,int ocurrenciaTramite,String numExpediente) throws Exception{
       String mensaje = "";
       Date date = new Date();
       try{ 
            mensaje = MeLanbide43Manager.getInstance().generarMisGestiones(codOrganizacion, codTramite, ocurrenciaTramite, numExpediente, "A",getAdaptSQLBD(String.valueOf(codOrganizacion)));

           if(log.isDebugEnabled()) log.info("generarMisGestionesAvance() : END");
        }
       catch(Exception x)
       {
           log.error("Error al avanzar el trámite: " + x);
           throw x;
       }
        return mensaje;
    }
   
   public String generarMisGestRetroceso (int codOrganizacion,int codTramite,int ocurrenciaTramite,String numExpediente) throws Exception{
        String mensaje = "";
       try{
            mensaje = MeLanbide43Manager.getInstance().generarMisGestiones(codOrganizacion, codTramite, ocurrenciaTramite, numExpediente, "R",getAdaptSQLBD(String.valueOf(codOrganizacion)));

            if(log.isDebugEnabled()) log.info("generarMisGestionesRetroceso() : END");
        }
       catch(Exception x)
       {
           throw x;
       }
        return mensaje;
    }
   
   public String generarMisGestCierre (int codOrganizacion,int codTramite,int ocurrenciaTramite,String numExpediente) throws Exception{
        String mensaje = "";
       try{
            mensaje = MeLanbide43Manager.getInstance().generarMisGestiones(codOrganizacion, codTramite, ocurrenciaTramite, numExpediente, "C",getAdaptSQLBD(String.valueOf(codOrganizacion)));
        if(log.isDebugEnabled()) log.info("generarMisGestionesCierre() : END: mensaje " + mensaje);
        }
       catch(Exception x)
       {
           throw x;
       }
        return mensaje;
    }
   
   public String generarMisGestSubs (int codOrganizacion,int codTramite,int ocurrenciaTramite,String numExpediente, ModuloIntegracionExternoParamAdicionales parametros) throws Exception{
        String mensaje = "0";
       try{
            log.info("generarMisGestSubs() : begin");
            AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            MeLanbide43Manager meLanbide43Manager = MeLanbide43Manager.getInstance();
            String[] datos          = numExpediente.split(BARRA);
            Date date = new Date();
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String fechaIni = meLanbide43Manager.fechaInicioExpediente(numExpediente, adaptador);                
            date = dateFormat.parse(fechaIni);
            String fecha = MeLanbide43Manager.getInstance().verificarFecha(datos[1], adaptador);
            Date fec = null;
            
            //Obtener usuario que cerro tramite anterior
            String usuarioTram = meLanbide43Manager.obtenerUsuarioUltimoTramite(numExpediente, adaptador);
            log.debug("usuarioTram:"+usuarioTram);
            
            if(fecha != ""){
                fec = dateFormat.parse(fecha);
                if(date.after(fec)){                     
                    //comprobar si la notificacion electronica y se entrga docu de forma presencial o telemática #233088
                    //if(meLanbide43Manager.notificElec(numExpediente, adaptador) && "IN".equals(parametros.getOrigenLlamada()))
                    //Solo es necesario saber si se llama de forma presencial para cerrar la aportacion
                    if("IN".equals(parametros.getOrigenLlamada()))
                    
                   /*if(meLanbide43Manager.notificElec(numExpediente, adaptador)
                        //   && (!ConfigurationParameter.getParameter(ConstantesMeLanbide43.USU_TRAMI, ConstantesMeLanbide43.FICHERO_PROPIEDADES).equals(usuarioTram))
                      )*/
                   {
                       cerrarPlazoAportacion(codOrganizacion, numExpediente);
                       aportacionDocumentacion(numExpediente, codOrganizacion, codTramite, ocurrenciaTramite);
                       
                       //se ha detectado que esta operación generarMisGestSubs está puesta también en aportaciones (además de en subsanaciones)
                       //el anterior método mal llamado cerrarPlazoAportación cierra la espera de subsanación
                       //cerrar la espera de aportación:
                       cerrarPlazos(codOrganizacion, codTramite, ocurrenciaTramite, numExpediente);

                   }else {
                        log.info("Se le llama de forma telemática por lo que no es necesario cerrar la aportacion (ya lo hacen ellos)");
                   }
                }
            }
        if(log.isDebugEnabled()) log.info("generarMisGestSubs() : END");
        }
       catch(Exception x)
       {
           mensaje = "2";
           log.error("Error generarMisGestSubs: " + x.getMessage());
           
           throw x;
       }
        return mensaje;
    }
    
    //#368734
    //operacion de Mis Gestiones para cerrar esperas (abiertas con método justificantesPagos())
    public String cerrarEsperaMisGest (int codOrganizacion,int codTramite,int ocurrenciaTramite,String numExpediente, ModuloIntegracionExternoParamAdicionales parametros) throws Exception{
        String mensaje = "0";
        try{
            log.info("cerrarEsperaMisGest() : BEGIN");
            AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            MeLanbide43Manager meLanbide43Manager = MeLanbide43Manager.getInstance();
            String[] exp = numExpediente.split(BARRA);
            Date date = new Date();
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String fechaIni = meLanbide43Manager.fechaInicioExpediente(numExpediente, adaptador);                
            date = dateFormat.parse(fechaIni);
            String fecha = MeLanbide43Manager.getInstance().verificarFecha(exp[1], adaptador);
            Date fec = null;
            
            //Obtener usuario que cerro tramite anterior
            String usuarioTram = meLanbide43Manager.obtenerUsuarioUltimoTramite(numExpediente, adaptador);
            log.debug("usuarioTram:"+usuarioTram);
            
            if(fecha != ""){
                fec = dateFormat.parse(fecha);
                if(date.after(fec)) {
                    //Solo es necesario saber si se llama de forma presencial para cerrar la aportacion
                    if("IN".equals(parametros.getOrigenLlamada())) {
                        cerrarEspera(codOrganizacion, numExpediente, codTramite);
                    }else {
                        log.info("Se le llama de forma telemática por lo que no es necesario cerrar la aportacion (ya lo hacen ellos)");
                    }
                }
            }
            log.info("cerrarEsperaMisGest() : END");
        }
        catch(Exception x)
        {
            mensaje = "2";
            log.error("Error cerrarEsperaMisGest: " + x.getMessage());

            throw x;
        }
        return mensaje;
    }
   
   public String justificantesPagos (int codOrganizacion,int codTramite,int ocurrenciaTramite,String numExpediente) throws Exception{
        String mensaje = "0";
       try{
            log.info("En funcion justificantesPagos: " + numExpediente);
            
            MeLanbide43Manager.getInstance().justificantesPagos (codOrganizacion,codTramite, numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                        
            if(log.isDebugEnabled()) log.info("justificantesPagos() : END");
        }catch(Lan6InformarConsultaExcepcion ice){
            
            ArrayList<String> codes = ice.getCodes();
            ArrayList<String> messages = ice.getMessages();

            
             StackTraceElement[] stacktrace=ice.getStackTrace();
             String error="";
             String causa = "";//ice.getCausaExcepcion();
                causa = ice.getCausaExcepcion();
             for (int i=0; i<codes.size();i++){
                 error += messages.get(i);
                 //error += codes.get(i);
             }
            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("MISGEST_015");
            errorB.setMensajeError("Error Lan6InformarConsultaExcepcion al actualizar tramite");
            errorB.setSituacion("justificantesPagos");

            MeLanbide43Manager.grabarError(errorB, error, causa, numExpediente);
            throw ice;
        }catch(Lan6Excepcion ex){            
            throw ex;
        }     
        catch(Exception ex)
        {            
            throw ex;
        }
        return mensaje;
    }
   
   
   public String esperaAportDocPostal (int codOrganizacion,int codTramite,int ocurrenciaTramite,String numExpediente) throws Exception{
        String mensaje = "0";
       try{
            log.info("En funcion esperaAportDocPostal: " + numExpediente);
            MeLanbide43Manager.getInstance().esperaAportDocPostal (codOrganizacion,codTramite, numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            if(log.isDebugEnabled()) log.info("esperaAportDocPostal() : END");
        }catch(Lan6InformarConsultaExcepcion ice){
            
            ArrayList<String> codes = ice.getCodes();
            ArrayList<String> messages = ice.getMessages();

            
             StackTraceElement[] stacktrace=ice.getStackTrace();
             String error="";
             String causa = "";//ice.getCausaExcepcion();
                causa = ice.getCausaExcepcion();
             for (int i=0; i<codes.size();i++){
                 error += messages.get(i);
                 //error += codes.get(i);
             }
            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("MISGEST_015");
            errorB.setMensajeError("Error Lan6InformarConsultaExcepcion al actualizar tramite");
            errorB.setSituacion("esperaAportDocPostal");

            MeLanbide43Manager.grabarError(errorB, error, causa, numExpediente);
            throw ice;
        }catch(Lan6Excepcion ex){            
            throw ex;
        }     
        catch(Exception ex)
        {            
            throw ex;
        }
        return mensaje;
    }
   
   public String cerrarPlazos (int codOrganizacion,int codTramite,int ocurrenciaTramite, String numExpediente) throws Exception{
       String mensaje = "0";
       AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
       Connection con = null;
       try{
            log.info("En funcion cerrarPlazos: " + numExpediente);
            
            String[] pepe = numExpediente.split("/");
            Date date = new Date();
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String fechaIni = MeLanbide43Manager.getInstance().fechaInicioExpediente(numExpediente, adaptador);                
            date = dateFormat.parse(fechaIni);
            String fecha = MeLanbide43Manager.getInstance().verificarFecha(pepe[1], adaptador);
            Date fec = null;
            if(fecha != ""){
                fec = dateFormat.parse(fecha);
                if(date.after(fec)){  
                   
                    
                    //DESARROLLO 0_______________________________________________________________________________________________________________________DESARROLLO 0
                    con = adaptador.getConnection();
                    Integer esDesarrolloCero = MeLanbide43Manager.getInstance().esDesarrolloCero(numExpediente, con);
                    String idProcedimiento = "";
                    if (esDesarrolloCero > 0) {
                        idProcedimiento = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_DESARROLLO_CERO, ConstantesMeLanbide43.FICHERO_PROPIEDADES);
                    } else {
//                            idProcedimiento = MeLanbide43Manager.getInstance().convierteProcedimiento(pepe[1]);
                        idProcedimiento = gestionAdaptadoresLan6Config.getCodProcedimientoPlatea(pepe[1]); //convierteProcedimiento(codProcedimiento);
                    }
                    //DESARROLLO 0_______________________________________________________________________________________________________________________DESARROLLO 0

                    
                    Lan6InformarConsultaServicios servicios = new Lan6InformarConsultaServicios(pepe[1]);
                    ArrayList<FilaListadoMisGestiones> misGestiones = new ArrayList<FilaListadoMisGestiones>();
                    misGestiones = MeLanbide43Manager.getInstance().getInfoGestiones(numExpediente, codOrganizacion, adaptador, codTramite, "");

                    if (!misGestiones.isEmpty()) {
                        log.info("Registro de inicio: " + misGestiones.get(0).getRegInicio().toString());
                        //String info = MeLanbide43Manager.getInstance().obtenerJustificante(misGestiones.get(0).getNumInicio().toString(), misGestiones.get(0).getRegInicio().toString(),this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                        //log.error("String info: " + info);
                        //if(info == null || info.equals("")){
                        Lan6Tramite lan6Tramite = new Lan6Tramite();
//                        lan6Tramite.setId(Lan6Constantes.TRAMITE_ESPERA_REQUERIMIENTO_APORTACION);
//                        lan6Tramite.setDescripcion(Lan6Constantes.TRAMITE_APORTACION_DOC_REQUERIMIENTO_DESC);
                        lan6Tramite.setId(gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("TRAMITE_ESPERA_REQUERIMIENTO_APORTACION"));
                        lan6Tramite.setDescripcion(gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("TRAMITE_APORTACION_DOC_REQUERIMIENTO_DESC"));
                        lan6Tramite.setDescripcionEu(gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("TRAMITE_APORTACION_DOC_REQUERIMIENTO_DESC_EU"));
                        lan6Tramite.setEjercicio(pepe[0]);
                        lan6Tramite.setIdExpediente(numExpediente);

                        lan6Tramite.setTipoTramite(Lan6Constantes.TRAMITE_ESPERA);
                        lan6Tramite.setFinTramiteEspera(true);
                        if (pepe[1].equals("CEI")) {
                            int tramSeg = Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide43.TRAM_CEI_SEGUNDO_PAGO, ConstantesMeLanbide43.FICHERO_PROPIEDADES));
                            int tramTer = Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide43.TRAM_CEI_TERCER_PAGO, ConstantesMeLanbide43.FICHERO_PROPIEDADES));

                            // Según trámite
                            if (codTramite == tramSeg) {//JUSTIFICACION CONTABLE PARA SEGUNDO PAGO
                                log.info("tramite 23");
//                                lan6Tramite.setInstanciaId(ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_INSTANCIA_PAGO2_LAN68_CEI, ConstantesMeLanbide43.FICHERO_ADAPTADORES));// Apertura del trámite para el segundo pago
                                lan6Tramite.setInstanciaId(gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties(ConstantesMeLanbide43.ID_INSTANCIA_PAGO2_LAN68_CEI)); //2.2
                            }
                            if (codTramite == tramTer) {//JUSTIFICACION CONTABLE PARA TERCER PAGO
                                log.info("tramite 54");
//                                lan6Tramite.setInstanciaId(ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_INSTANCIA_PAGO3_LAN68_CEI, ConstantesMeLanbide43.FICHERO_ADAPTADORES));// Apertura del trámite para el tercer pago
                                lan6Tramite.setInstanciaId(gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties(ConstantesMeLanbide43.ID_INSTANCIA_PAGO3_LAN68_CEI)); //2.2
                            }
                        } else if (pepe[1].equals("SEI")) {
                            if (codTramite == Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide43.TRAM_SEI_PRIMER_PAGO, ConstantesMeLanbide43.FICHERO_PROPIEDADES))) {
//                                lan6Tramite.setInstanciaId(ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_INSTANCIA_PAGO1_LAN68_SEI, ConstantesMeLanbide43.FICHERO_ADAPTADORES));
                                lan6Tramite.setInstanciaId(gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties(ConstantesMeLanbide43.ID_INSTANCIA_PAGO1_LAN68_SEI)); //2.2
                            } else if (codTramite == Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide43.TRAM_SEI_1SEM_2AN, ConstantesMeLanbide43.FICHERO_PROPIEDADES))) {
//                                lan6Tramite.setInstanciaId(ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_INSTANCIA_PAGO_SEM1_ANU2_LAN68_SEI, ConstantesMeLanbide43.FICHERO_ADAPTADORES));
                                lan6Tramite.setInstanciaId(gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties(ConstantesMeLanbide43.ID_INSTANCIA_PAGO_SEM1_ANU2_LAN68_SEI)); //2.2
                            } else if (codTramite == Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide43.TRAM_SEI_2SEM_2AN, ConstantesMeLanbide43.FICHERO_PROPIEDADES))) {
//                                lan6Tramite.setInstanciaId(ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_INSTANCIA_PAGO_SEM2_ANU2_LAN68_SEI, ConstantesMeLanbide43.FICHERO_ADAPTADORES));
                                lan6Tramite.setInstanciaId(gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties(ConstantesMeLanbide43.ID_INSTANCIA_PAGO_SEM2_ANU2_LAN68_SEI)); //2.2
                            } else if (codTramite == Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide43.TRAM_SEI_1SEM_3AN, ConstantesMeLanbide43.FICHERO_PROPIEDADES))) {
//                                lan6Tramite.setInstanciaId(ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_INSTANCIA_PAGO_SEM1_ANU3_LAN68_SEI, ConstantesMeLanbide43.FICHERO_ADAPTADORES));
                                lan6Tramite.setInstanciaId(gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties(ConstantesMeLanbide43.ID_INSTANCIA_PAGO_SEM1_ANU3_LAN68_SEI)); //2.2
                            } else if (codTramite == Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide43.TRAM_SEI_2SEM_3AN, ConstantesMeLanbide43.FICHERO_PROPIEDADES))) {
//                                lan6Tramite.setInstanciaId(ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_INSTANCIA_PAGO_SEM2_ANU3_LAN68_SEI, ConstantesMeLanbide43.FICHERO_ADAPTADORES));
                                lan6Tramite.setInstanciaId(gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties(ConstantesMeLanbide43.ID_INSTANCIA_PAGO_SEM2_ANU3_LAN68_SEI)); //2.2
                            } else if (codTramite == Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide43.TRAM_SEI_JUS_FINAL, ConstantesMeLanbide43.FICHERO_PROPIEDADES))) {
//                                lan6Tramite.setInstanciaId(ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_INSTANCIA_PAGO_FINAL_ANU3_LAN68_SEI, ConstantesMeLanbide43.FICHERO_ADAPTADORES));
                                lan6Tramite.setInstanciaId(gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties(ConstantesMeLanbide43.ID_INSTANCIA_PAGO_FINAL_ANU3_LAN68_SEI)); //2.2
                            } else if (codTramite == Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide43.TRAM_SEI_1SEM_4AN, ConstantesMeLanbide43.FICHERO_PROPIEDADES))) {
//                                lan6Tramite.setInstanciaId(ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_INSTANCIA_PAGO_SEM1_ANU4_LAN68_SEI, ConstantesMeLanbide43.FICHERO_ADAPTADORES));
                                lan6Tramite.setInstanciaId(gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties(ConstantesMeLanbide43.ID_INSTANCIA_PAGO_SEM1_ANU4_LAN68_SEI)); //2.2
                            } else if (codTramite == Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide43.TRAM_SEI_2SEM_4AN, ConstantesMeLanbide43.FICHERO_PROPIEDADES))) {
//                                lan6Tramite.setInstanciaId(ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_INSTANCIA_PAGO_SEM2_ANU4_LAN68_SEI, ConstantesMeLanbide43.FICHERO_ADAPTADORES));
                                lan6Tramite.setInstanciaId(gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties(ConstantesMeLanbide43.ID_INSTANCIA_PAGO3_LAN68_CEI)); //2.2
                            } else if (codTramite == Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide43.TRAM_SEI_JUS_FIN_4AN, ConstantesMeLanbide43.FICHERO_PROPIEDADES))) {
//                                lan6Tramite.setInstanciaId(ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_INSTANCIA_PAGO_FINAL_ANU4_LAN68_SEI, ConstantesMeLanbide43.ID_INSTANCIA_PAGO_SEM2_ANU4_LAN68_SEI));
                                lan6Tramite.setInstanciaId(gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties(ConstantesMeLanbide43.ID_INSTANCIA_PAGO_FINAL_ANU4_LAN68_SEI)); //2.2
                            }/*else if (codTramite == Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide43.TRAM_SEI_2SEM_2AN, ConstantesMeLanbide43.FICHERO_PROPIEDADES))){
                                lan6Tramite.setInstanciaId(Lan6Constantes.ID_INSTANCIA_PAGO1_FOR_SEI);
                            }*/
                        }
                        Calendar hoy = Calendar.getInstance();
                        lan6Tramite.setFechaActualizacion(hoy);


                        ArrayList<Lan6Tramite> tramites = new ArrayList<Lan6Tramite>();
                        tramites.add(lan6Tramite);       
                        //Llamada método       
                        log.info("llamada al metodo actualizarTramites: " + numExpediente);
                        log.info("Parametros enviados actualizarTramites tramites : " + gson.toJson(tramites));
                        String res = servicios.actualizarTramites(tramites);
                        log.info("resultado de actualizarTRamites : " + res);
                    }
                }
            }

            if(log.isDebugEnabled()) log.info("cerrarPlazos() : END");
        }
       catch(Lan6InformarConsultaExcepcion ice){

          ArrayList<String> codes = ice.getCodes();
          ArrayList<String> messages = ice.getMessages();

             String causa = "";//ice.getCausaExcepcion();
                causa = ice.getCausaExcepcion();

           StackTraceElement[] stacktrace=ice.getStackTrace();
           String error="";
           for (int i=0; i<codes.size();i++){
               error += messages.get(i);
               //error += codes.get(i);
           }

            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("MISGEST_015");
            errorB.setMensajeError("Error Lan6InformarConsultaExcepcion avanzando expediente");
            errorB.setSituacion("cerrarPlazos");


         MeLanbide43Manager.grabarError(errorB, error, causa, numExpediente);
        throw ice;
      }catch(Lan6Excepcion ex){            
            log.error("Error en la funcion avanceExpConsulta: " + ex.getException().getMessage());
            String error = "Error en la funcion avanceExpConsulta: " + ex.getException().getMessage();
            
            
            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("MISGEST_014");
            errorB.setMensajeError("Error avanzando expediente");
            errorB.setSituacion("cerrarPlazos");

            MeLanbide43Manager.grabarError(errorB, ex.getException().getMessage().toString(), ex.getException().toString(), numExpediente);
            
            throw ex;
        }  
       catch(Exception x)
       {
           mensaje = "2";
           log.error("Error en funcion cerrarPlazos(): " + x);
           throw x;
       }
       finally
       {
           adaptador.devolverConexion(con);
       }
        return mensaje;
    }
   
    private void cerrarPlazoAportacion(int codOrganizacion, String numExp) throws Exception {
        AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        Connection con = null;
        try {
            log.info("En funcion cerrarPlazoAportacion: " + numExp);
            String[] pepe = numExp.split("/");

            //DESARROLLO 0_______________________________________________________________________________________________________________________DESARROLLO 0
            con = adaptador.getConnection();
            Integer esDesarrolloCero = MeLanbide43Manager.getInstance().esDesarrolloCero(numExp, con);
            String idProcedimiento = "";
            if (esDesarrolloCero > 0) {
                idProcedimiento = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_DESARROLLO_CERO, ConstantesMeLanbide43.FICHERO_PROPIEDADES);
            } else {
//                    idProcedimiento = MeLanbide43Manager.getInstance().convierteProcedimiento(pepe[1]);
                idProcedimiento = gestionAdaptadoresLan6Config.getCodProcedimientoPlatea(pepe[1]); //convierteProcedimiento(codProcedimiento);

            }
            //DESARROLLO 0_______________________________________________________________________________________________________________________DESARROLLO 0

            Lan6InformarConsultaServicios servicios = new Lan6InformarConsultaServicios(pepe[1]);
            //cerrar aportacion mis gestiones
            Lan6Tramite lan6Tramite = new Lan6Tramite();
//            lan6Tramite.setId(Lan6Constantes.TRAMITE_ESPERA_SUBSANACION);
//            lan6Tramite.setDescripcion(Lan6Constantes.TRAMITE_SUBSANACION_DOC_DESC);
//            lan6Tramite.setDescripcionEu(Lan6Constantes.TRAMITE_SUBSANACION_DOC_DESC);
            lan6Tramite.setId(gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("TRAMITE_ESPERA_SUBSANACION"));
            lan6Tramite.setDescripcion(gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("TRAMITE_SUBSANACION_DOC_DESC"));
            lan6Tramite.setDescripcionEu(gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("TRAMITE_SUBSANACION_DOC_DESC_EU"));
            lan6Tramite.setEjercicio(pepe[0]);
            lan6Tramite.setIdExpediente(numExp);

            lan6Tramite.setTipoTramite(Lan6Constantes.TRAMITE_ESPERA);
            lan6Tramite.setFinTramiteEspera(true);

            Calendar hoy = Calendar.getInstance();
            lan6Tramite.setFechaActualizacion(hoy);

            ArrayList<Lan6Tramite> tramites = new ArrayList<Lan6Tramite>();
            tramites.add(lan6Tramite);
            //Llamada método       
            log.error("llamada al metodo actualizarTramites: " + numExp);
            log.info("Parametros enviados actualizarTramites tramites : " + gson.toJson(tramites));
            String res = servicios.actualizarTramites(tramites);
            log.error("Respuesta del metodo actualizarTramites: " + res);

        } catch (Exception x) {
            log.error("Error en funcion cerrarPlazoAportacion " + x);
            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("MISGEST_007");
            errorB.setMensajeError("Error cerrando el plazo de la aportacion");
            errorB.setSituacion("cerrarPlazoAportacion");

            MeLanbide43Manager.grabarError(errorB, x.getMessage().toString(), x.toString(), numExp);
        } finally {
            adaptador.devolverConexion(con);
        }
    }

    private void cerrarEspera(int codOrganizacion, String numExp, int codTramite) throws Exception {
        AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        Connection con = null;
        try {
            log.info("En cerrarEspera: " + numExp);
            String[] exp = numExp.split("/");

            //DESARROLLO 0_______________________________________________________________________________________________________________________DESARROLLO 0
            con = adaptador.getConnection();
            Integer esDesarrolloCero = MeLanbide43Manager.getInstance().esDesarrolloCero(numExp, con);
            String idProcedimiento = "";
            if (esDesarrolloCero > 0) {
                idProcedimiento = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_DESARROLLO_CERO, ConstantesMeLanbide43.FICHERO_PROPIEDADES);
            } else {
//                    idProcedimiento = MeLanbide43Manager.getInstance().convierteProcedimiento(exp[1]);
                idProcedimiento = gestionAdaptadoresLan6Config.getCodProcedimientoPlatea(exp[1]); //convierteProcedimiento(codProcedimiento);
            }
            //DESARROLLO 0_______________________________________________________________________________________________________________________DESARROLLO 0

            Lan6InformarConsultaServicios servicios = new Lan6InformarConsultaServicios(exp[1]);

            Lan6Tramite lan6Tramite = new Lan6Tramite();

            lan6Tramite = MeLanbide43Manager.getInstance().setLan6TramiteEsperas(numExp, codTramite, lan6Tramite);

            //cierra la espera
            lan6Tramite.setFinTramiteEspera(true);

            ArrayList<Lan6Tramite> tramites = new ArrayList<Lan6Tramite>();
            tramites.add(lan6Tramite);

            log.info("llamada al metodo actualizarTramites: " + numExp);
            log.info("Parametros enviados actualizarTramites tramites : " + gson.toJson(tramites));
            String res = servicios.actualizarTramites(tramites);
            log.error("Respuesta del metodo actualizarTramites: " + res);
        } catch (Exception x) {
            log.error("Error en funcion cerrarEspera " + x);
            ErrorBean errorB = new ErrorBean();
            errorB.setMensajeError("Error cerrando espera");
            errorB.setSituacion("cerrarEspera");

            MeLanbide43Manager.grabarError(errorB, x.getMessage().toString(), x.toString(), numExp);
        } finally {
            adaptador.devolverConexion(con);
        }
    }
   
    private void aportacionDocumentacion(String numExp, int codOrganizacion, int tramite, int ocurrencia) throws Exception {
        AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        Connection con = null;
        try {
            log.info("En funcion aportacionDocumentacion: " + numExp);
            Calendar hoy = Calendar.getInstance();
            String[] pepe = numExp.split("/");
            String idProcedimiento = "";
            SimpleDateFormat d = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

            //hoy = d.parse(hoy.toString());
            //DESARROLLO 0_______________________________________________________________________________________________________________________DESARROLLO 0
            con = adaptador.getConnection();
            Integer esDesarrolloCero = MeLanbide43Manager.getInstance().esDesarrolloCero(numExp, con);
            if (esDesarrolloCero > 0) {
                idProcedimiento = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_DESARROLLO_CERO, ConstantesMeLanbide43.FICHERO_PROPIEDADES);
            } else {
//                    idProcedimiento = MeLanbide43Manager.getInstance().convierteProcedimiento(pepe[1]);
                idProcedimiento = gestionAdaptadoresLan6Config.getCodProcedimientoPlatea(pepe[1]); //convierteProcedimiento(codProcedimiento);
            }
            //DESARROLLO 0_______________________________________________________________________________________________________________________DESARROLLO 0

            Lan6InformarConsultaServicios servicios = new Lan6InformarConsultaServicios(pepe[1]);
            ArrayList<String> oids = new ArrayList<String>();
            oids = MeLanbide43Manager.getInstance().obtenerDocumentosExpediente(numExp, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));

            //Expediente
            Lan6Expediente lan6Expediente = new Lan6Expediente();
            lan6Expediente.setNumero(numExp);
            lan6Expediente.setEjercicio(pepe[0]);

            ArrayList<Lan6Tramite> listaTramites = new ArrayList<Lan6Tramite>();
            Lan6Tramite lan6Tramite = new Lan6Tramite();

            String fecha = d.format(hoy.getTime());
            fecha = fecha.replace("/", "");
            log.info("fecha " + fecha);

            if (oids != null && !oids.isEmpty()) {
                lan6Tramite.setId(gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("TRAMITE_SUBSANACION_DOC") + "_" + fecha);
            } else {
                lan6Tramite.setId(gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("TRAMITE_SUBSANACION_DOC"));
            }
//            lan6Tramite.setDescripcion(Lan6Constantes.TRAMITE_SUBSANACION_DOC_DESC);
            lan6Tramite.setDescripcion(gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("TRAMITE_SUBSANACION_DOC_DESC"));
            lan6Tramite.setDescripcionEu(gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("TRAMITE_SUBSANACION_DOC_DESC_EU"));
            lan6Tramite.setTipoTramite(Lan6Constantes.TRAMITE_APORTACION);

            lan6Tramite.setFechaActualizacion(Calendar.getInstance());

            listaTramites.add(lan6Tramite);

            lan6Expediente.setListaTramites(listaTramites);

            //solo si hay documento
            Lan6DokusiServicios dokusi = new Lan6DokusiServicios(idProcedimiento);
            Lan6Documento doclan6 = null;
            //Documento aportado por ciudadano
            Lan6AportacionDoc lan6AportacionDoc = new Lan6AportacionDoc();
            ArrayList<Lan6AportacionDoc> documentos = new ArrayList<Lan6AportacionDoc>();

            if (oids != null && !oids.isEmpty()) {
                log.info("Hay documentos");
                for (String oid : oids) {
                    doclan6 = dokusi.consultarDocumento(oid);
                    String nombre = doclan6.getNombre();
                    log.info("nombre: " + nombre);
                    int punto = nombre.lastIndexOf(".");

                    log.info("punto: " + punto);
                    log.info("nombre.contains(.): " + nombre.contains("."));
                    if (nombre.contains(".")) {
                        nombre = nombre.substring(0, nombre.lastIndexOf("."));
                    }
                    log.info("aportacionDocumentacion oid: " + oid);

                    log.info("nombre tras split: " + nombre);

                    lan6AportacionDoc.setOid(oid);
                    lan6AportacionDoc.setNombre(doclan6.getNombre());
                    lan6AportacionDoc.setDescripcionRol(nombre);
                    lan6AportacionDoc.setTipo(Lan6Constantes.TIPO_DOC_ELECTRONICO);

                    documentos.add(lan6AportacionDoc);
                    lan6AportacionDoc = new Lan6AportacionDoc();
                }

                //Aportacion
                Lan6Aportacion lan6Aportacion = new Lan6Aportacion();
                lan6Aportacion.setTipoAportacion(Lan6Constantes.TIPO_APORTACION_APPLICANT);
                lan6Aportacion.setTipoTramite(Lan6Constantes.TRAMITE_APORTACION);
                lan6Aportacion.setDocumentos(documentos);
                // Recuperamos la fecha de aportacion de documentos
                try {
                    // Consultamos procedimiento especifico
                    log.info("Leemos propiedad => " + codOrganizacion + "/" + ConstantesMeLanbide43.COD_CAMPO_FECHA_ENTREGA_DOC_FECENTDOC + "/" + pepe[1]);
                    String codCampoFecEntDoc = ConfigurationParameter.getParameter(
                            codOrganizacion + "/" + ConstantesMeLanbide43.COD_CAMPO_FECHA_ENTREGA_DOC_FECENTDOC + "/" + pepe[1],
                             ConstantesMeLanbide43.FICHERO_PROPIEDADES);
                    log.info("codCampoFecEntDoc Recuperado=> " + codCampoFecEntDoc);
                    if (codCampoFecEntDoc == null || codCampoFecEntDoc.isEmpty()) {
                        //  leemos el generico
                        log.info("Leemos propiedad Generica => " + codOrganizacion + "/" + ConstantesMeLanbide43.COD_CAMPO_FECHA_ENTREGA_DOC_FECENTDOC);
                        codCampoFecEntDoc = ConfigurationParameter.getParameter(
                                codOrganizacion + "/" + ConstantesMeLanbide43.COD_CAMPO_FECHA_ENTREGA_DOC_FECENTDOC,
                                 ConstantesMeLanbide43.FICHERO_PROPIEDADES);
                        log.info("codCampoFecEntDoc Generico Recuperado => " + codCampoFecEntDoc);
                    }
                    Date fechaAportacionDocumentacion = MeLanbide43Manager.getInstance().getValorCampoSuplementarioTramiteE_TFET(numExp, codCampoFecEntDoc, tramite, ocurrencia, adaptador);
                    Calendar calendarFAD = Calendar.getInstance();
                    if (fechaAportacionDocumentacion != null) {
                        calendarFAD.setTime(fechaAportacionDocumentacion);
                    } else {
                        log.info("No se han recuperado datos de la fecha de aportacion de documentacion ... Seteamos la fecha actual");
                        calendarFAD.setTime(new Date());
                    }
                    lan6Aportacion.setFechaAportacion(calendarFAD);
                } catch (Exception e) {
                    log.error("Error al leer datos de fecha de aportacion " + e.getMessage(), e);
                }
                Participantes par = MeLanbide43Manager.getInstance().obtenerDatosParticipantes(numExp, adaptador);
                lan6Aportacion.setIdEmisor(par != null ? par.getNif() : "-");
                ArrayList<Lan6Aportacion> aportacionesCiudadano = new ArrayList<Lan6Aportacion>();
                aportacionesCiudadano.add(lan6Aportacion);
                lan6Expediente.setAportacionesCiudadano(aportacionesCiudadano);

            }
            //Llamada metodo       
            log.info("Antes de llamada a funcion actualizarAportacionSolicitante: " + numExp);
            log.info("Parametros enviados actualizarAportacionSolicitante - lan6Expediente : " + gson.toJson(lan6Expediente));
            String res = servicios.actualizarAportacionSolicitante(lan6Expediente);
            log.info("Respuesta del metodo actualizarAportacionSolicitante: " + res);

        } catch (Exception ex) {
            log.error("Error en funcion aportacionDocumentacion " + ex);
            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("MISGEST_008");
            errorB.setMensajeError("Error aportando documentacion");
            errorB.setSituacion("aportacionDocumentacion");
            MeLanbide43Manager.grabarError(errorB, ex.getMessage() != null ? ex.getMessage().toString() : "null", ex.toString(), numExp);

        } finally {
            adaptador.devolverConexion(con);
        }
    }
     
   //se llamara a esta funcion al avanzar el primer trámite cunado pueda ser el expediente de oficio para que cree en mis gestiones el expediente
    private String generarMisGestionesOficio (int codOrganizacion,int codTramite,int ocurrenciaTramite,String numExpediente, String evento) throws Exception{
        if(log.isDebugEnabled()) log.info("generarMisGestionesOficio ( codOrganizacion = " + codOrganizacion + " codTramite = " + codTramite + " ocurrenciaTramite = " + ocurrenciaTramite + " numExpediente = " + numExpediente  + " evento = " + evento + " ) : BEGIN");
        SalidaIntegracionVO salida = new SalidaIntegracionVO();
        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        MeLanbide43Manager meLanbide43Manager = MeLanbide43Manager.getInstance();
        
        String clave= null;
        String codigoOperacion = "0";
        String mensaje = "";
        String id= null; 
        AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        try{   
            
            if(numExpediente!=null && !"".equals(numExpediente)){
                String[] datos          = numExpediente.split(BARRA);
                String ejercicio        = datos[0];
                String codProcedimiento = datos[1];
                boolean instancia = meLanbide43Manager.expInstanciaParte(numExpediente,adaptador);
                log.debug("instancia: " + instancia);
                log.debug("codProcedimiento: " + codProcedimiento);
                //PROCS_OFICIO_MG
                /*String procs_oficio = ConfigurationParameter.getParameter("PROCS_OFICIO_MG", ConstantesMeLanbide43.FICHERO_PROPIEDADES);
                String[] aProcs = procs_oficio.split(",");
                log.debug("contiene procedimiento Configurado como OficioMG: " + Arrays.asList(aProcs).contains(codProcedimiento));*/
                if (!instancia)
                {
                    Date date = new Date();
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    String fechaIni = meLanbide43Manager.fechaInicioExpediente(numExpediente, adaptador);                
                    date = dateFormat.parse(fechaIni);
                    String fecha = meLanbide43Manager.verificarFecha(codProcedimiento, adaptador);
                    Participantes par = meLanbide43Manager.obtenerDatosParticipantes(numExpediente,adaptador);
                    log.debug("PARTICIPANTES: "+par); 
                    log.debug("fecha: "+fecha); 
                    log.info("tipo id: " + par.getTipoID());
                    if(par.getTipoID() != 0){
                        Date fec = null;
                        if(fecha != ""){
                            fec = dateFormat.parse(fecha);
                            if(date.after(fec)){
                                log.debug(date+" after "+fec);
                                if(evento.equals("C")){
                                     if(!meLanbide43Manager.expedienteCerrado(numExpediente,adaptador)){
                                        id = MeLanbide43Manager.getInstance().guardarGestiones(numExpediente, codOrganizacion, adaptador, codTramite, evento);
                                        log.info("Id generado: " + id);
                                        codigoOperacion= meLanbide43Manager.avanzarGestiones(id, String.valueOf(codOrganizacion), adaptador);
                                     }else
                                        log.info("El expediente: " +numExpediente+" ya ha sido cerrado" );
                                }else{
                                    id = MeLanbide43Manager.getInstance().guardarGestiones(numExpediente, codOrganizacion, adaptador, codTramite, evento);
                                    log.info("Id generado: " + id);
                                    codigoOperacion= meLanbide43Manager.avanzarGestiones(id, String.valueOf(codOrganizacion), adaptador);
                                }
                                
                            }
                        }
                    }
                }
            }
        }
        catch(Exception ex)
        {
            codigoOperacion = "2";            
            meLanbide43Manager.borrarProcesado(id, adaptador);
            mensaje = MeLanbide43I18n.getInstance().getMensaje(1, "error.errorGen");
            
            //insertar error en registro de errores           
            log.error("Error en la funcion generarMisGestionesOficio: ", ex);
            String error = "Error en la funcion generarMisGestionesOficio: " + ex.getMessage()!=null?ex.getMessage().toString():"null";
            
            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("MISGEST_012");
            errorB.setMensajeError("Error al iniciar un expediente en la funcion generarMisGestionesOficio");
            errorB.setSituacion("generarMisGestionesOficio");
           

            MeLanbide43Manager.grabarError(errorB, ex.getMessage()!=null?ex.getMessage().toString():"null", ex.toString(), numExpediente);
             //fin 
            
            throw ex;
        }
        if(log.isDebugEnabled()) log.info("generarMisGestionesOficio() : END");
        return codigoOperacion;
    }
 
    //public void actualizaInteresados(String numExp, String codOrg)
    public String actualizaInteresados(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente) throws Exception {
        String mensaje = "0";
        AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        Connection con = null;
        String id = null;
        String[] datos = numExpediente.split("/");
        String codProcedimiento = datos[1];
        int publicar = 1;
        if (codProcedimiento.equalsIgnoreCase("RGI") || codProcedimiento.equalsIgnoreCase("IMV")) {
            publicar = MeLanbide43Manager.getInstance().getCampoNumericoEnteroExpediente(numExpediente, "PUBSEDE", adaptador);
        }
        if (publicar == 1) {
            // #489507 insert en MELANBIDE43_INTEGMISGEST
            id = MeLanbide43Manager.getInstance().guardarGestiones(numExpediente, codOrganizacion, adaptador, codTramite, "T");
            log.info("Id generado al guardar actualizaInteresados: " + id);

            try {
                log.info("actualizaInteresados ");

                //DESARROLLO 0_______________________________________________________________________________________________________________________DESARROLLO 0
                con = adaptador.getConnection();
                Integer esDesarrolloCero = MeLanbide43Manager.getInstance().esDesarrolloCero(numExpediente, con);
                String idProcedimiento = "";
                if (esDesarrolloCero > 0) {
                    idProcedimiento = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_DESARROLLO_CERO, ConstantesMeLanbide43.FICHERO_PROPIEDADES);
                } else {
//                    idProcedimiento = MeLanbide43Manager.getInstance().convierteProcedimiento(codProcedimiento);
                    idProcedimiento = gestionAdaptadoresLan6Config.getCodProcedimientoPlatea(codProcedimiento); //convierteProcedimiento(codProcedimiento);
                }
                //DESARROLLO 0_______________________________________________________________________________________________________________________DESARROLLO 0

                Date date = new Date();
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                //AdaptadorSQLBD adaptador =this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                String fechaIni = MeLanbide43Manager.getInstance().fechaInicioExpediente(numExpediente, adaptador);
                date = dateFormat.parse(fechaIni);
                String fecha = MeLanbide43Manager.getInstance().verificarFecha(datos[1], adaptador);
                Date fec = null;
                if (fecha != "") {
                    fec = dateFormat.parse(fecha);
                    if (date.after(fec)) {
                        ArrayList<Participantes> parti = MeLanbide43Manager.getInstance().obtenerListaParticipantes(numExpediente, adaptador);
                        log.info("despues de recoger participantes ");
                        Lan6InformarConsultaServicios servicios = new Lan6InformarConsultaServicios(codProcedimiento);

                        // Expediente
                        Lan6Expediente lan6Expediente = new Lan6Expediente();
                        lan6Expediente.setNumero(numExpediente);
                        lan6Expediente.setEjercicio(datos[0]);
                        log.info("despues de recoger expediente ");
                        // Participacion
                        Lan6Participacion lan6participacion = new Lan6Participacion();

                        //Interesados
                        ArrayList<Lan6Interesado> interesados = new ArrayList<Lan6Interesado>();

                        for (Participantes par : parti) {
                            log.info("nombre interesado: " + par.getNombre());
                            log.info("nif interesado: " + par.getNif());
                            Lan6Interesado lan6Interesado = new Lan6Interesado();
                            lan6Interesado.setNumIdentificacion(par.getNif());
                            //Si es presencial se recogerá el nif de EJIE y no será persona física o no tiene apellido (AUTONOMO)
                            if (par.getApe1() == null || par.getApe1().isEmpty() || par.getTipoID() == 4 || par.getTipoID() == 5) {
                                lan6Interesado.setTipoIdentificacion(Lan6Constantes.TIPO_IDENT_CIF);
                                lan6Interesado.setRazonSocial(par.getNombre());
                            } else {
                                lan6Interesado.setNombre(par.getNombre());
                                lan6Interesado.setApellido1(par.getApe1());
                                log.info("apellido1 interesado: " + par.getApe1());
                                lan6Interesado.setApellido2(par.getApe2());
                                log.info("apellido2 interesado: " + par.getApe2());
                                switch (par.getTipoID()) {
                                    case 1:
                                        lan6Interesado.setTipoIdentificacion(Lan6Constantes.TIPO_IDENT_NIF);
                                        break;
                                    case 2:
                                        lan6Interesado.setTipoIdentificacion(Lan6Constantes.TIPO_IDENT_PASAPORTE);
                                        break;
                                    case 3:
                                        lan6Interesado.setTipoIdentificacion(Lan6Constantes.TIPO_IDENT_NIE);
                                        break;

                                    default:
                                        log.error("El tipo " + par.getTipoID() + " no tiene correspondencia en PLATEA");
                                }
                            }
                            String tipo = "";
                            if (par.getRol().equals("1")) {
                                tipo = Lan6Constantes.TIPO_INTERESADO_TITULAR;
                            } else {
                                tipo = Lan6Constantes.TIPO_INTERESADO_REPRESENTANTE;
                            }
                            lan6Interesado.setTipo(tipo);
                            log.info("despues de recoger el interesado ");
                            interesados.add(lan6Interesado);
                        }
                        // Leemos datos de Avisos Campos suplementarios Regexlan
                        DatosAvisoCSRegexlan datosAvisoCSRegexlan = MeLanbide43Manager.getInstance().getMapaDatosSuplementarioAvisosExpediente(numExpediente, adaptador);
                        if (datosAvisoCSRegexlan != null) {
                            if (hayRepresentanteLegalEnListaInteresado(interesados)) {
                                if (datosAvisoCSRegexlan.getAvisoEmailRepresentante() != null && !datosAvisoCSRegexlan.getAvisoEmailRepresentante().isEmpty()) {
                                    lan6participacion.setMailsAvisos(Arrays.asList(datosAvisoCSRegexlan.getAvisoEmailRepresentante()));
                                }
                                if (datosAvisoCSRegexlan.getAvisoSmsRepresentante() != null && !datosAvisoCSRegexlan.getAvisoSmsRepresentante().isEmpty()) {
                                    lan6participacion.setTfnosAvisos(Arrays.asList(datosAvisoCSRegexlan.getAvisoSmsRepresentante()));
                                }
                            } else {
                                if (datosAvisoCSRegexlan.getAvisoEmailTitular() != null && !datosAvisoCSRegexlan.getAvisoEmailTitular().isEmpty()) {
                                    lan6participacion.setMailsAvisos(Arrays.asList(datosAvisoCSRegexlan.getAvisoEmailTitular()));
                                }
                                if (datosAvisoCSRegexlan.getAvisoSmsTitular() != null && !datosAvisoCSRegexlan.getAvisoSmsTitular().isEmpty()) {
                                    lan6participacion.setTfnosAvisos(Arrays.asList(datosAvisoCSRegexlan.getAvisoSmsTitular()));
                                }
                            }
                            lan6participacion.setIdioma(datosAvisoCSRegexlan.getAvisoIdioma() != null && !datosAvisoCSRegexlan.getAvisoIdioma().isEmpty() ? datosAvisoCSRegexlan.getAvisoIdioma().toLowerCase() : "es");
                        }
                        lan6participacion.setIdParticipacion(Lan6Constantes.PARTICIPACION_UNICA);
                        lan6participacion.setInteresados(interesados);
                        lan6Expediente.setParticipacion(lan6participacion);

                        // Llamada metodo
                        log.info("Parametros enviados actualizarInteresados - lan6Expediente : " + gson.toJson(lan6Expediente));
                        String res = servicios.actualizarInteresados(lan6Expediente);
                        log.info("Respuesta del metodo actualizarInteresados: " + res);

                        // #489507 update en MELANBIDE43_INTEGMISGEST
                        MeLanbide43Manager.getInstance().actualizarProcesadoActInteresados(Integer.parseInt(id), adaptador);

                    }
                }

            } catch (Exception ex) {
                log.error("Error en actualizaInteresados: " + ex);
                mensaje = "2";
                // #489507 update en MELANBIDE43_INTEGMISGEST
                MeLanbide43Manager.getInstance().actualizarError(Integer.parseInt(id), 1, ex.getMessage(), adaptador);
                throw ex;
            } finally {
                adaptador.devolverConexion(con);
            }

        }

        return mensaje;
    }

  public void borraExpConsulta(FilaListadoMisGestiones gestion){ //retroceso de expediente
      /*try{
        Lan6InformarConsultaServicios servicios = new Lan6InformarConsultaServicios(gestion.getId().toString());
        
        String exp = gestion.getNumExp();
        String[] pepe = exp.split("/");
        
        Tramite tra = MeLanbide43Manager.getInstance().obtenerDatosTramite(pepe[1], gestion.getTramiteFin(),this.getAdaptSQLBD(String.valueOf("0")));
        Lan6Tramite  lan6Tramite = new Lan6Tramite();	
        lan6Tramite.setId(gestion.getTramiteFin());
        lan6Tramite.setDescripcion(tra.getDescripcion());
        //lan6Tramite.setDescripcionEu("Apertura expediente lanbide eu");
        lan6Tramite.setEjercicio(pepe[0]);
        lan6Tramite.setIdExpediente(gestion.getNumExp());

        Calendar fecha = Calendar.getInstance();
        fecha.set(2014, 6, 15);
        fecha.setTime(gestion.getFechaGenerado());
        lan6Tramite.setFechaActualizacion(fecha);
        
        ArrayList<Lan6Tramite> tramites = new ArrayList<Lan6Tramite>();
        tramites.add(lan6Tramite);	

        //Llamada método       	
        //servicios.borrarTramitesSistemaConsulta(tramites);          
      }
      catch(Exception ex){          
          log.error("error en la funcion borraExpConsulta: " + ex.getMessage());
      }*/
  }
 
     /**
     * Operacion que recupera los datos de conexion a la BBDD
     * @param codOrganizacion
     * @return AdaptadorSQLBD
     */
    private AdaptadorSQLBD getAdaptSQLBD(String codOrganizacion) throws SQLException{
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
                if(log.isDebugEnabled())log.info("He cogido el jndi: " + jndiGenerico);
                ds = (DataSource)pc.lookup(jndiGenerico, DataSource.class);
                // Conexion al esquema genérico
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
                if(st!=null) st.close();
                if(rs!=null) rs.close();
                if(conGenerico!=null && !conGenerico.isClosed())
                conGenerico.close();
            }// finally
            if(log.isDebugEnabled()) log.debug("getConnection() : END");
         }// synchronized
        return adapt;
     }//getConnection

    //#233079 Mis gestiones - Mantenimiento tabla de fases
   public String cargarPantallaPrincipal(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("Entramos en cargarPantallaPrincipal de " + this.getClass().getName());
        AdaptadorSQLBD adapt = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (Exception ex) {
            log.error(this.getClass().getName() + " Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }
        String url = "/jsp/extension/melanbide43/gestiones/misGestiones.jsp";
        
        if (adapt != null) {
            try {
              
              List<ProcedimientoVO> listaProcedimientos = MeLanbide43Manager.getInstance().getProcedimientos(this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
               if (!listaProcedimientos.isEmpty()) {
                    request.setAttribute("listaProcedimiento", listaProcedimientos);
               }
            } catch (Exception ex) {
                ex.printStackTrace();
                log.error("Error al recueperar los datos De Acceso - MELANBIDE43 - cargarPantallaPrincipal", ex);
            }
        }
        return url;       
    }
   
   public void cargarTablaFases(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)throws Exception{            
        String codProc = request.getParameter("codProc");
        String codigoOperacion="1";
        List<FilaFaseVO> lista = (List<FilaFaseVO>) MeLanbide43Manager.getInstance().getListaFasesporProcedimiento(codProc, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
       
        if (!lista.isEmpty())
        {
            codigoOperacion="0";
        }
        
        escribirListaFasesRequest(codigoOperacion, lista, response);
   }
   
    public String cargarNuevaFase(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        cargarCombosNuevaFase(codOrganizacion, request);
        return "/jsp/extension/melanbide43/gestiones/nuevaFase.jsp?codOrganizacionModulo="+codOrganizacion;
    }
    
    public String cargarModificarFase(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        List<TramiteVO> listaTramites  = new ArrayList<TramiteVO>();
        TramiteVO tramite  = new TramiteVO();
        tramite.setCodTramInterno(request.getParameter("codTram"));   
        tramite.setDescTramite(request.getParameter("descTram"));
        listaTramites.add(tramite);
        if (!listaTramites.isEmpty()) {
            request.setAttribute("listaTramite", listaTramites);
        }
        return "/jsp/extension/melanbide43/gestiones/nuevaFase.jsp?codOrganizacionModulo="+codOrganizacion
                                                                   +"&codProc="+request.getParameter("codProc")
                                                                   +"&codTram="+request.getParameter("codTram")
                                                                   +"&descTram="+request.getParameter("descTram")
                                                                   +"&codFase="+request.getParameter("codFase")
                                                                   +"&descFaseCas="+request.getParameter("descFaseCas")
                                                                   +"&descFaseEus="+request.getParameter("descFaseEus"); 
    }
    
    public void modificarFase(int codOrganizacion, int codTram, int ocurrenciaTram, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        String codigoOperacion = "0";
        FaseVO fase = new FaseVO();
        List<FilaFaseVO> fases = new ArrayList();
        try
        {        
            AdaptadorSQLBD adaptador = null;
            try {
                adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            } catch (SQLException ex) {
                java.util.logging.Logger.getLogger(MELANBIDE43.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            String codProc     = (String)request.getParameter("codProc");
            String codTramite  = (String)request.getParameter("codTramite");
            String codFase     = (String)request.getParameter("codFase");
            String descFaseCas = (String)request.getParameter("descFaseCas").toUpperCase();
            String descFaseEus = (String)request.getParameter("descFaseEus").toUpperCase();

            fase = MeLanbide43Manager.getInstance().getFaseProcedimientoTramiteyFase(codProc, codTramite, codFase, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));

            if(fase.getCodProcedimiento() == null)
            {
                codigoOperacion = "3";
            }
            else
            {
                fase.setDescFaseCas(descFaseCas);
                fase.setDescFaseEus(descFaseEus);
                MeLanbide43Manager.getInstance().modificarFaseVO(codOrganizacion, fase, adaptador);
            }

            fases = MeLanbide43Manager.getInstance().getListaFasesporProcedimiento(codProc, adaptador);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        escribirListaFasesRequest(codigoOperacion, fases, response);
    }
   
    public void cargarEliminarFase(int codOrganizacion, int codTram, int ocurrenciaTram, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        String codigoOperacion = "0";
        List<FilaFaseVO> fases = new ArrayList();
        
        AdaptadorSQLBD adaptador = null;
        try {
            adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE43.class.getName()).log(Level.SEVERE, null, ex);
        }
        try
        {
            String codProc = request.getParameter("codProc");
            String codTramite = request.getParameter("codTram");
            String codFase = request.getParameter("codFase");

            FaseVO fase = new FaseVO();
            fase.setCodProcedimiento(codProc);
            fase.setCodTramite(codTramite);
            fase.setCodFase(codFase);

            fase = MeLanbide43Manager.getInstance().getFaseProcedimientoTramiteyFase(codProc, codTramite, codFase, adaptador);
            
            if(fase == null)
            {
                codigoOperacion = "3";
            }
            else
            {
                MeLanbide43Manager.getInstance().eliminarFaseVO(codOrganizacion, fase, adaptador);
            }

            fases = MeLanbide43Manager.getInstance().getListaFasesporProcedimiento(codProc, adaptador);
        }
        catch(Exception ex)
        {
            codigoOperacion = "2";
            ex.printStackTrace();
        }
        escribirListaFasesRequest(codigoOperacion, fases, response);
    }
    
     private void cargarCombosNuevaFase(int codOrganizacion, HttpServletRequest request)
    {
        log.debug("Entramos en cargarCombosNuevaFase de " + this.getClass().getName());
        AdaptadorSQLBD adapt = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (Exception ex) {
            log.error(this.getClass().getName() + " Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }
        
        // distinguir entre nueva fase o modificacion ??
        if (adapt != null) {
            try {
                String codProc = request.getParameter("codProc");
                List<TramiteVO> listaTramites = MeLanbide43Manager.getInstance().getTramites(codProc, adapt);
                if (!listaTramites.isEmpty()) {
                    request.setAttribute("listaTramite", listaTramites);
               }
            } catch (Exception ex) {
                ex.printStackTrace();
                log.error("Error al recueperar los datos De Acceso - MELANBIDE43 - cargarCombosNuevaFase", ex);
            }
        }       
    }
     
    private void escribirListaFasesRequest(String codigoOperacion, List<FilaFaseVO> fases, HttpServletResponse response)
    {
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");

        for(FilaFaseVO fase : fases)
        {
            xmlSalida.append("<FILA>");
                xmlSalida.append("<COD_TRAM_EXT>");
                   xmlSalida.append(fase.getCodTramExt());
                xmlSalida.append("</COD_TRAM_EXT>");
                xmlSalida.append("<COD_TRAMITE>");
                   xmlSalida.append(fase.getCodTramite());
                xmlSalida.append("</COD_TRAMITE>");
                xmlSalida.append("<TRAMITE>");
                   xmlSalida.append(fase.getDescTramite());
                xmlSalida.append("</TRAMITE>");
                 xmlSalida.append("<COD_FASE>");
                   xmlSalida.append(fase.getCodFase());
                xmlSalida.append("</COD_FASE>");
                xmlSalida.append("<DESC_FASE_C>");
                   xmlSalida.append(fase.getDescFaseCas());
                xmlSalida.append("</DESC_FASE_C>");
                xmlSalida.append("<DESC_FASE_E>");
                   xmlSalida.append(fase.getDescFaseEus());
                xmlSalida.append("</DESC_FASE_E>");
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
        }catch(Exception e){
            e.printStackTrace();
        }
    }
     
    public void cargarTramites(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)throws Exception{            
        String codProcedimiento = request.getParameter("codProc");
        String codigoOperacion="1";
        List<TramiteVO> lista = (List<TramiteVO>) MeLanbide43Manager.getInstance().getTramites(codProcedimiento, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
       
        if (!lista.isEmpty())
        {
            codigoOperacion="0"; 
            StringBuffer xmlSalida = new StringBuffer();

            xmlSalida.append("<RESPUESTA>");
            xmlSalida.append("<CODIGO_OPERACION>");
            xmlSalida.append(codigoOperacion);
            xmlSalida.append("</CODIGO_OPERACION>");

            for (TramiteVO fila : lista) {
                xmlSalida.append("<FILA>");
                
                xmlSalida.append("<COD_TRAM_INTERNO>");
                xmlSalida.append(fila.getCodTramInterno());
                xmlSalida.append("</COD_TRAM_INTERNO>");

                xmlSalida.append("<TRAMITE>");
                xmlSalida.append(fila.getCodTramInterno() + fila.getDescTramite());
                xmlSalida.append("</TRAMITE>");

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
            } catch (Exception e) {
                log.error(" Error Recuperando datos en para Gestion de errores al filtrar - ", e);
            }//try-catch  
        }      
    }
   
    public void guardarFase(int codOrganizacion, int codTram, int ocurrenciaTram, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        String codigoOperacion = "0";
        List<FilaFaseVO> fases = new ArrayList<FilaFaseVO>();
        
        //AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        AdaptadorSQLBD adaptador;
        try {
            adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                
            try
            {  
                String codProc = (String)request.getParameter("codProc");
                String codTramite = (String)request.getParameter("codTramite");
                String codFase = (String)request.getParameter("codFase");
                String descFaseCas = (String)request.getParameter("descFaseCas");
                String descFaseEus = (String)request.getParameter("descFaseEus");

                FaseVO fase = null;

                fase = new FaseVO();

                fase = MeLanbide43Manager.getInstance().getFaseProcedimientoTramiteyFase(codProc, codTramite, codFase, adaptador);

                //si existe dar error
                if (fase.getCodProcedimiento() != null)
                {
                    codigoOperacion = "3";
                }
                else
                {
                    fase.setCodProcedimiento(codProc != null && !codProc.equals("") ? codProc.toUpperCase() : null);
                    fase.setCodTramite(codTramite != null && !codTramite.equals("") ? codTramite.toUpperCase() : null);
                    fase.setCodFase(codFase != null && !codFase.equals("") ? codFase.toUpperCase() : null);
                    fase.setDescFaseCas(descFaseCas != null && !descFaseCas.equals("") ? descFaseCas.toUpperCase() : null);
                    fase.setDescFaseEus(descFaseEus != null && !descFaseEus.equals("") ? descFaseEus.toUpperCase() : null);

                    MeLanbide43Manager.getInstance().guardarFaseVO(codOrganizacion, fase, adaptador);

                    fases = MeLanbide43Manager.getInstance().getListaFasesporProcedimiento(codProc, adaptador);
                }
            }
            catch(Exception ex)
            {
                codigoOperacion = "2";
                ex.printStackTrace();
            }
            
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE43.class.getName()).log(Level.SEVERE, null, ex);
        }
        escribirListaFasesRequest(codigoOperacion, fases, response);
    }
   // METODOS PARA EL MODULO DE NOTIFICACIONES Tarea #233082
    public String notificacionesPantallaPrincipal(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("Entramos en notificacionesPantallaPrincipal de " + this.getClass().getName());
        AdaptadorSQLBD adapt = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (Exception ex) {
            log.error(this.getClass().getName() + " Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }
        String url = "/jsp/extension/melanbide43/notificaciones/notificaciones.jsp";
        
        if (adapt != null) {
            try {
              
              List<ProcedimientoVO> listaProcedimientos = MeLanbide43Manager.getInstance().getProcedimientos(this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
               if (!listaProcedimientos.isEmpty()) {
                    request.setAttribute("listaProcedimiento", listaProcedimientos);
               }
            } catch (Exception ex) {
                ex.printStackTrace();
                log.error("Error al recuperar los datos De Acceso - MELANBIDE43 - cargarPantallaPrincipal", ex);
            }
        }
        return url;       
    }
    
    public void cargarTablaNotificaciones(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)throws Exception{            

        String codProc = request.getParameter("codProc");
        String numExped = request.getParameter("numExped");
        String fecDesde = request.getParameter("fecDesde");
        String fecHasta = request.getParameter("fecHasta");
        String resultado = request.getParameter("resultado");
        String codigoOperacion="1";       
        List<FilaNotificacionVO> lista = (List<FilaNotificacionVO>) MeLanbide43Manager.getInstance().getListaNotificaciones(codProc, numExped, fecDesde, fecHasta, resultado, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));

        if (!lista.isEmpty())
        {
            codigoOperacion="0";
        }

        escribirListaNotificacionesRequest(codigoOperacion, lista, response);
   }
   
    public void reenviarNotificacion(int codOrganizacion, int codTram, int ocurrenciaTram, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        String codigoOperacion = "0";
        FilaNotificacionVO notif = new FilaNotificacionVO();
        List<FilaNotificacionVO> notificaciones = new ArrayList();
        try
        {        
            AdaptadorSQLBD adaptador = null;
            try {
                adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            } catch (SQLException ex) {
                java.util.logging.Logger.getLogger(MELANBIDE43.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            String codNotif  = (String)request.getParameter("codNotif");
            String codProc   = request.getParameter("codProc");
            String numExped  = request.getParameter("numExped");
            String fecDesde  = request.getParameter("fecDesde");
            String fecHasta  = request.getParameter("fecHasta");
            String resultado = request.getParameter("resultado");

            notif = MeLanbide43Manager.getInstance().getNotificacion(codNotif, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));

            if (notif.getCodNotif()==0)
            {
                codigoOperacion = "3";
            }
            else
            {
                MeLanbide43Manager.getInstance().reenviarNotificacion(codOrganizacion, notif, adaptador);
            }

            notificaciones = MeLanbide43Manager.getInstance().getListaNotificaciones(codProc, numExped, fecDesde, fecHasta, resultado, adaptador);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        escribirListaNotificacionesRequest(codigoOperacion, notificaciones, response);
    }

    private void escribirListaNotificacionesRequest(String codigoOperacion, List<FilaNotificacionVO> notificaciones, HttpServletResponse response)
    {
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");


        for(FilaNotificacionVO notificacion : notificaciones)
        {
             xmlSalida.append("<FILA>");
                    xmlSalida.append("<COD_NOTIF>");
                       xmlSalida.append(notificacion.getCodNotif());
                    xmlSalida.append("</COD_NOTIF>");
                    xmlSalida.append("<NUM_EXPED>");
                       xmlSalida.append(notificacion.getNumExped());
                    xmlSalida.append("</NUM_EXPED>");
                    xmlSalida.append("<COD_PROC>");
                       xmlSalida.append(notificacion.getCodProced());
                    xmlSalida.append("</COD_PROC>");
                     xmlSalida.append("<EJERC>");
                       xmlSalida.append(notificacion.getEjerc());
                    xmlSalida.append("</EJERC>");
                    xmlSalida.append("<COD_MUNIC>");
                       xmlSalida.append(notificacion.getCodMunic());
                    xmlSalida.append("</COD_MUNIC>");
                    xmlSalida.append("<COD_TRAM>");
                       xmlSalida.append(notificacion.getTramite());
                    xmlSalida.append("</COD_TRAM>");
                     xmlSalida.append("<OCU_TRAM>");
                       xmlSalida.append(notificacion.getOcuTram());
                    xmlSalida.append("</OCU_TRAM>");
                    xmlSalida.append("<ACT_NOTIF>");
                        if (notificacion.getActNotif() == null) {
                            xmlSalida.append("-");
                        }else{
                            xmlSalida.append(notificacion.getActNotif());
                        }
                    xmlSalida.append("</ACT_NOTIF>");
                    xmlSalida.append("<CAD_NOTIF>");
                       xmlSalida.append(notificacion.getCadNotif());
                    xmlSalida.append("</CAD_NOTIF>");
                    xmlSalida.append("<TXT_NOTIF>");
                        if (notificacion.getTxtNotif() == null){
                            xmlSalida.append("-");
                        }else{ 
                            xmlSalida.append(notificacion.getTxtNotif());
                        }
                    xmlSalida.append("</TXT_NOTIF>");
                    xmlSalida.append("<FIRMADA>");
                        if (notificacion.getFirmada() == null){
                            xmlSalida.append("-");
                        }else{ 
                            xmlSalida.append(notificacion.getFirmada());
                        }
                    xmlSalida.append("</FIRMADA>");
                    xmlSalida.append("<FEC_ENVIO>");
                        if (notificacion.getFecEnvio() == null){
                            xmlSalida.append("-");
                       }else{ 
                            xmlSalida.append(notificacion.getFecEnvio());
                        }
                    xmlSalida.append("</FEC_ENVIO>");
                    xmlSalida.append("<COD_NOTIF_PLATEA>");
                        if (notificacion.getCodNotifPlatea() == null){
                            xmlSalida.append("-");
                        }else{ 
                            xmlSalida.append(notificacion.getCodNotifPlatea());
                        }
                    xmlSalida.append("</COD_NOTIF_PLATEA>");
                    xmlSalida.append("<FEC_SOL_ENVIO>");
                        if (notificacion.getFecSolEnvio() == null){
                            xmlSalida.append("-");
                        }else{   
                            xmlSalida.append(notificacion.getFecSolEnvio());
                        }
                    xmlSalida.append("</FEC_SOL_ENVIO>");
                    xmlSalida.append("<NUM_INTENT>");
                        if (notificacion.getNumIntent() == 0){
                            xmlSalida.append("null");
                        }else{   
                            xmlSalida.append(Integer.toString(notificacion.getNumIntent()));
                        }
                    xmlSalida.append("</NUM_INTENT>");
                    xmlSalida.append("<RESP_LLAMADA>");
                        if (notificacion.getRespLlamada() == null){
                            xmlSalida.append("-");
                        }else{ 
                            xmlSalida.append(notificacion.getRespLlamada());
                        }
                    xmlSalida.append("</RESP_LLAMADA>");
                    xmlSalida.append("<RESULTADO>");
                        if (notificacion.getResultado() == null){
                            xmlSalida.append("-");
                        }else{ 
                            xmlSalida.append(notificacion.getResultado());
                        }
                    xmlSalida.append("</RESULTADO>");
                    xmlSalida.append("<FEC_ACUSE>");
                        if (notificacion.getFecAcuse() == null){
                            xmlSalida.append("-");
                        }else{
                            xmlSalida.append(notificacion.getFecAcuse());
                        }
                    xmlSalida.append("</FEC_ACUSE>");                    
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
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public String abrirEsperaRenunciaCerrarEsperaDesestimiento (int codOrganizacion,int codTramite,int ocurrenciaTramite, String numExpediente) throws Exception{
       String mensaje = "0";
       AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
       Connection con = null;
       try{
            log.info("abrirEsperaRenunciaCerrarEsperaDesestimiento() - Begin - " + numExpediente + " Tramite : " + codTramite);
            
            String[] arregloDatosExp = numExpediente.split("/");
            Date date = new Date();
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String fechaIni = MeLanbide43Manager.getInstance().fechaInicioExpediente(numExpediente, adaptador);                
            date = dateFormat.parse(fechaIni);
            String fecha = MeLanbide43Manager.getInstance().verificarFecha(arregloDatosExp[1], adaptador);
            Date fec = null;
            if(fecha != ""){
                fec = dateFormat.parse(fecha);
                if(date.after(fec)){  
                    
                    
                    //DESARROLLO 0_______________________________________________________________________________________________________________________DESARROLLO 0
                    con = adaptador.getConnection();
                    Integer esDesarrolloCero = MeLanbide43Manager.getInstance().esDesarrolloCero(numExpediente, con);
                    String idProcedimiento = "";
                    if (esDesarrolloCero > 0){
                            idProcedimiento = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_DESARROLLO_CERO, ConstantesMeLanbide43.FICHERO_PROPIEDADES);
                    } else {
//                            idProcedimiento = MeLanbide43Manager.getInstance().convierteProcedimiento(arregloDatosExp[1]);
                        idProcedimiento = gestionAdaptadoresLan6Config.getCodProcedimientoPlatea(arregloDatosExp[1]); //convierteProcedimiento(codProcedimiento);
             }
                    //DESARROLLO 0_______________________________________________________________________________________________________________________DESARROLLO 0
                    

                    Lan6InformarConsultaServicios servicios = new Lan6InformarConsultaServicios(arregloDatosExp[1]);
                    ArrayList<FilaListadoMisGestiones> misGestiones = new ArrayList<FilaListadoMisGestiones>();
                    misGestiones = MeLanbide43Manager.getInstance().getInfoGestiones(numExpediente, codOrganizacion, adaptador, codTramite, "");

                    if(!misGestiones.isEmpty()){
                        log.info("Registro de inicio: " + misGestiones.get(0).getRegInicio().toString());
                        
                        // Abrir Espera de renuncia
                        Lan6Tramite lan6Tramite = new Lan6Tramite();
//                        lan6Tramite.setId(Lan6Constantes.TRAMITE_ESPERA_RESIGNACION);
                        lan6Tramite.setId(gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("TRAMITE_ESPERA_RESIGNACION"));
                        lan6Tramite.setEjercicio(arregloDatosExp[0]);
                        lan6Tramite.setIdExpediente(numExpediente);
                        lan6Tramite.setTipoTramite(Lan6Constantes.TRAMITE_ESPERA);
                        Calendar idInstanciaFechaNotificacion = Calendar.getInstance();
                        /*
                        Cogemos las fechas de fin del tramite anterior en el flujo a Espera de presentacion de recursos
                        108-Espera acuse desistimiento : Viene de la rama de requerimiento
                        04-Resolucion Definitiva: Rama normal
                        Para asignar la fehca de notificacion escogemos la fecha mayor
                        */
                        Date fechaNotificacion = new Date(); // La inicialiazamos con la fecha actual 
                        if(arregloDatosExp[1].equals("ORI")){
                            String codigoVisibleTramResolDefi=ConfigurationParameter.getParameter(ConstantesMeLanbide43.TRAM_ORI_RESOLUCION_DEFINITIVA_COD_VISIBLE, ConstantesMeLanbide43.FICHERO_PROPIEDADES);
                            String codigoVisibleTramEsperaAcusDesis=ConfigurationParameter.getParameter(ConstantesMeLanbide43.TRAM_ORI_ESPERA_ACUSE_DESIS_COD_VISIBLE, ConstantesMeLanbide43.FICHERO_PROPIEDADES);
                            TramiteVO tramiteVOResolucionDefinitiva04 = MeLanbide43Manager.getInstance().getDatosTramiteXcodigoVisibleYExpediente(arregloDatosExp[1],codigoVisibleTramResolDefi,numExpediente,adaptador);
                            TramiteVO tramiteVOEsperaAcuseDesistimiento108 = MeLanbide43Manager.getInstance().getDatosTramiteXcodigoVisibleYExpediente(arregloDatosExp[1],codigoVisibleTramEsperaAcusDesis,numExpediente,adaptador);
                            log.info("tramiteVOResolucionDefinitiva04 : "+tramiteVOResolucionDefinitiva04.getFechaFinTramite());
                            log.info("tramiteVOEsperaAcuseDesistimiento108 : "+tramiteVOEsperaAcuseDesistimiento108.getFechaFinTramite());
                            if(tramiteVOEsperaAcuseDesistimiento108.getFechaFinTramite()!=null){
                                if(tramiteVOResolucionDefinitiva04.getFechaFinTramite()!=null){
                                    fechaNotificacion=tramiteVOResolucionDefinitiva04.getFechaFinTramite();
                                }else{
                                    if(tramiteVOEsperaAcuseDesistimiento108.getFechaFinTramite().compareTo(tramiteVOResolucionDefinitiva04.getFechaFinTramite())>0){
                                        fechaNotificacion=tramiteVOEsperaAcuseDesistimiento108.getFechaFinTramite();
                                    }else
                                        fechaNotificacion=tramiteVOResolucionDefinitiva04.getFechaFinTramite();
                                }
                            }else{
                                if(tramiteVOResolucionDefinitiva04.getFechaFinTramite()!=null){
                                    fechaNotificacion=tramiteVOResolucionDefinitiva04.getFechaFinTramite();
                                }
                            }
                        }else if(arregloDatosExp[1].equals("CEMP")){
                            String codigoVisibleTramResolDefi = ConfigurationParameter.getParameter(ConstantesMeLanbide43.TRAM_CEMP_RESOLUCION_DEFINITIVA_COD_VISIBLE, ConstantesMeLanbide43.FICHERO_PROPIEDADES);
                            String codigoVisibleTramEsperaAcusDesis = ConfigurationParameter.getParameter(ConstantesMeLanbide43.TRAM_CEMP_ESPERA_ACUSE_DESIS_COD_VISIBLE, ConstantesMeLanbide43.FICHERO_PROPIEDADES);
                            TramiteVO tramiteVOResolucionDefinitiva04 = MeLanbide43Manager.getInstance().getDatosTramiteXcodigoVisibleYExpediente(arregloDatosExp[1], codigoVisibleTramResolDefi, numExpediente, adaptador);
                            TramiteVO tramiteVOEsperaAcuseDesistimiento108 = MeLanbide43Manager.getInstance().getDatosTramiteXcodigoVisibleYExpediente(arregloDatosExp[1], codigoVisibleTramEsperaAcusDesis, numExpediente, adaptador);
                            log.info("tramiteVOResolucionDefinitiva04 : " + tramiteVOResolucionDefinitiva04.getFechaFinTramite());
                            log.info("tramiteVOEsperaAcuseDesistimiento108 : " + tramiteVOEsperaAcuseDesistimiento108.getFechaFinTramite());
                            if (tramiteVOEsperaAcuseDesistimiento108.getFechaFinTramite() != null) {
                                if (tramiteVOResolucionDefinitiva04.getFechaFinTramite() != null) {
                                    fechaNotificacion = tramiteVOResolucionDefinitiva04.getFechaFinTramite();
                                } else {
                                    if (tramiteVOEsperaAcuseDesistimiento108.getFechaFinTramite().compareTo(tramiteVOResolucionDefinitiva04.getFechaFinTramite()) > 0) {
                                        fechaNotificacion = tramiteVOEsperaAcuseDesistimiento108.getFechaFinTramite();
                                    } else {
                                        fechaNotificacion = tramiteVOResolucionDefinitiva04.getFechaFinTramite();
                                    }
                                }
                            } else {
                                if (tramiteVOResolucionDefinitiva04.getFechaFinTramite() != null) {
                                    fechaNotificacion = tramiteVOResolucionDefinitiva04.getFechaFinTramite();
                                }
                            }
                        } else if(arregloDatosExp[1].equals("COLEC")){
                            String codigoVisibleTramResolDefi=ConfigurationParameter.getParameter(ConstantesMeLanbide43.TRAM_COLEC_RESOLUCION_DEFINITIVA_COD_VISIBLE, ConstantesMeLanbide43.FICHERO_PROPIEDADES);
                            String codigoVisibleTramEsperaAcusDesis=ConfigurationParameter.getParameter(ConstantesMeLanbide43.TRAM_COLEC_ESPERA_ACUSE_DESIS_COD_VISIBLE, ConstantesMeLanbide43.FICHERO_PROPIEDADES);
                            TramiteVO tramiteVOResolucionDefinitiva04 = MeLanbide43Manager.getInstance().getDatosTramiteXcodigoVisibleYExpediente(arregloDatosExp[1],codigoVisibleTramResolDefi,numExpediente,adaptador);
                            TramiteVO tramiteVOEsperaAcuseDesistimiento108 = MeLanbide43Manager.getInstance().getDatosTramiteXcodigoVisibleYExpediente(arregloDatosExp[1],codigoVisibleTramEsperaAcusDesis,numExpediente,adaptador);
                            log.info("tramiteVOResolucionDefinitiva04 : "+tramiteVOResolucionDefinitiva04.getFechaFinTramite());
                            log.info("tramiteVOEsperaAcuseDesistimiento108 : "+tramiteVOEsperaAcuseDesistimiento108.getFechaFinTramite());
                            if(tramiteVOEsperaAcuseDesistimiento108.getFechaFinTramite()!=null){
                                if(tramiteVOResolucionDefinitiva04.getFechaFinTramite()!=null){
                                    fechaNotificacion=tramiteVOResolucionDefinitiva04.getFechaFinTramite();
                                }else{
                                    if(tramiteVOEsperaAcuseDesistimiento108.getFechaFinTramite().compareTo(tramiteVOResolucionDefinitiva04.getFechaFinTramite())>0){
                                        fechaNotificacion=tramiteVOEsperaAcuseDesistimiento108.getFechaFinTramite();
                                    }else
                                        fechaNotificacion=tramiteVOResolucionDefinitiva04.getFechaFinTramite();
                                }
                            }else{
                                if(tramiteVOResolucionDefinitiva04.getFechaFinTramite()!=null){
                                    fechaNotificacion=tramiteVOResolucionDefinitiva04.getFechaFinTramite();
                                }
                            }
                        }
                        else if(arregloDatosExp[1].equals("AERTE")){
                            String codigoTramAcuseNotificacionResol = ConfigurationParameter.getParameter(ConstantesMeLanbide43.TRAM_AERTE_ACUSE_NOTIFICACION_RESOLUCION, ConstantesMeLanbide43.FICHERO_PROPIEDADES);
                            String codigoCampoFechaAcuseNotifResol = ConfigurationParameter.getParameter(ConstantesMeLanbide43.CAMPO_AERTE_FECACUSENOT, ConstantesMeLanbide43.FICHERO_PROPIEDADES);
                            SalidaIntegracionVO salida = new SalidaIntegracionVO();
                            IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
                            salida = el.getCampoSuplementarioTramite(String.valueOf(codOrganizacion), arregloDatosExp[0], numExpediente, arregloDatosExp[1], Integer.valueOf(codigoTramAcuseNotificacionResol), 1, codigoCampoFechaAcuseNotifResol, 3); // 3 Tipo Fecha
                            log.info("Fecha Acuse Notificacion resolucion : "
                                    + salida != null && salida.getCampoSuplementario() != null && salida.getCampoSuplementario().getValorFecha() != null
                                    ? salida.getCampoSuplementario().getValorFecha()
                                    : ""
                            );
                            if (salida != null && salida.getCampoSuplementario() != null && salida.getCampoSuplementario().getValorFecha() != null) {
                                fechaNotificacion = salida.getCampoSuplementario().getValorFecha().getTime();
                            } else {
                                log.error("Error en funcion abrirEsperaRenunciaCerrarEsperaDesestimiento(): 3. No se ha registrado la fecha de acuse de resolucion . Campo suplementatio del tramite FECHA ACUSE NOTIFICACION RESOLUCION no recuperado ");
                                //return "3";
                            }
                        }
        
                        Calendar hoy = Calendar.getInstance();
                        lan6Tramite.setFechaActualizacion(hoy);
                        Lan6Notificacion  notificacion = new Lan6Notificacion();
                        // En este caso es una publicacion conjunta mediante BOE. Usamos fecha alta de tramite NOTIFICACION DEFINITIVA PUBLICADA
                        log.info("fechaAcuseNotificacion : " + fechaNotificacion);
                        idInstanciaFechaNotificacion.setTime(fechaNotificacion);
                        log.info("idInstanciaFechaNotificacion : " + idInstanciaFechaNotificacion.getTime());
                        notificacion.setFechaRecepcion(idInstanciaFechaNotificacion);  //Util.obtenerCalendar(datosEvento.getFechaNotificacion())
                        lan6Tramite.setNotificacion(notificacion);
                        
                        ArrayList<Lan6Tramite> tramites = new ArrayList<Lan6Tramite>();
                        tramites.add(lan6Tramite);       
                        //Llamada metodo       
                        log.info("Llamada al metodo actualizarTramites : " + numExpediente);
                        log.info("Parametros enviados actualizarTramites - tramites : " + gson.toJson(tramites));
                        String res = servicios.actualizarTramites(tramites);
                        log.info("abrirEsperaRenunciaCerrarEsperaDesestimiento - resultado de actualizarTramites Al crear la espera de Renuncia : " + res);
                        
                        // Cerrar la espera de desestimiento
                        lan6Tramite = new Lan6Tramite();
//                        lan6Tramite.setId(Lan6Constantes.TRAMITE_ESPERA_DESISTIMIENTO);
//                        lan6Tramite.setDescripcion(Lan6Constantes.TRAMITE_DESISTIMIENTO_DESC);
                        lan6Tramite.setId(gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("TRAMITE_ESPERA_DESISTIMIENTO"));
                        lan6Tramite.setDescripcion(gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("TRAMITE_DESISTIMIENTO_DESC"));
                        lan6Tramite.setDescripcionEu(gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("TRAMITE_DESISTIMIENTO_DESC_EU"));
                        lan6Tramite.setEjercicio(arregloDatosExp[0]);
                        lan6Tramite.setIdExpediente(numExpediente);
                        lan6Tramite.setTipoTramite(Lan6Constantes.TRAMITE_ESPERA);
                        lan6Tramite.setFinTramiteEspera(true);
                        
                        hoy = Calendar.getInstance();
                        lan6Tramite.setFechaActualizacion(hoy);

                        tramites = new ArrayList<Lan6Tramite>();
                        tramites.add(lan6Tramite);       
                        //Llamada metodo       
                        log.info("Llamada al metodo actualizarTramites : " + numExpediente);
                        log.info("Parametros enviados actualizarTramites - tramites : " + gson.toJson(tramites));
                        res = servicios.actualizarTramites(tramites);
                        log.info("abrirEsperaRenunciaCerrarEsperaDesestimiento - resultado de actualizarTramites al Cerrar la espera de Desestimiento : " + res);
                    }
                }
            }else
                log.error("Fecha Inicio expediente no validada "
                        + "contra fecha de configuracion de Mis Gestiones para el procedimiento en MELANBIDE43_LLAMAMISGEST " + date + " - " + fec);
        }
       catch(Lan6InformarConsultaExcepcion ice){
           ArrayList<String> codes = ice.getCodes();
           ArrayList<String> messages = ice.getMessages();
           String causa = ice.getCausaExcepcion();
           String error = "";
           for (int i = 0; i < codes.size(); i++) {
               error += (error.isEmpty() ? messages.get(i) :  " " + messages.get(i));
           }
           ErrorBean errorB = new ErrorBean();
           errorB.setIdError("MISGEST_015");
           errorB.setMensajeError("Error Lan6InformarConsultaExcepcion avanzando expediente");
           errorB.setSituacion("abrirEsperaRenunciaCerrarEsperaDesestimiento");
           MeLanbide43Manager.grabarError(errorB, error, causa, numExpediente);
           throw ice;
      }catch(Lan6Excepcion ex){            
            log.error("Error en la funcion abrirEsperaRenunciaCerrarEsperaDesestimiento: " + ex.getException().getMessage());
            String error = "Error en la funcion abrirEsperaRenunciaCerrarEsperaDesestimiento : " + ex.getMessage();
            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("MISGEST_014");
            errorB.setMensajeError("Error Gestionando apertura y cierre de esperas. " + error);
            errorB.setSituacion("abrirEsperaRenunciaCerrarEsperaDesestimiento");
            MeLanbide43Manager.grabarError(errorB, ex.getException().getMessage().toString(), ex.getException().toString(), numExpediente);
            throw ex;
        }  
       catch(Exception x)
       {
           mensaje = "2";
           log.error("Error en funcion abrirEsperaRenunciaCerrarEsperaDesestimiento(): " + x);
           throw x;
       }
       finally
        {
            adaptador.devolverConexion(con);
        }
       log.info("abrirEsperaRenunciaCerrarEsperaDesestimiento() - End - " + numExpediente + " : " + mensaje);
        return mensaje;
    }
    
    public String llamadasMisGestionesPantallaPrincipal(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)throws Exception{            

       log.debug("Entramos en llamadasMisGestionesPantallaPrincipal de " + this.getClass().getName());
        String url = "/jsp/extension/melanbide43/gestiones/llamadasMisGestiones.jsp";
       AdaptadorSQLBD adapt = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (SQLException ex) {
            log.error(this.getClass().getName() + " Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }
        if (adapt != null) {
            try {           
              List<ProcedimientoVO> listaProcedimientos = MeLanbide43Manager.getInstance().getProcedimientos(this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
               if (!listaProcedimientos.isEmpty()) {
                    request.setAttribute("listaProcedimiento", listaProcedimientos);
               }
            } catch (Exception ex) {
                ex.printStackTrace();
                log.error("Error al recuperar los datos De Acceso - MELANBIDE43 - cargarPantallaPrincipal", ex);
            }
        }
        return url;       

    }
    
    public void cargarTablaLLamacargarTablaLdasMisGestiones(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)throws Exception{            

       // String codProc = request.getParameter("codProc");
        String numExped = request.getParameter("numExped");
        String fecDesde = request.getParameter("fecDesde");
        String fecHasta = request.getParameter("fecHasta");
        String dniTercero = request.getParameter("dniTercero");
        String codProcedimiento = request.getParameter("codProcedimiento");
        String codigoOperacion="1";       
        List<FilaLlamadasMisGestVO> lista = (List<FilaLlamadasMisGestVO>) MeLanbide43Manager.getInstance().getListaLlamadasMisGest(numExped, fecDesde, fecHasta, dniTercero, codProcedimiento,this.getAdaptSQLBD(String.valueOf(codOrganizacion)));

        if (!lista.isEmpty())
        {
            codigoOperacion="0";
        }

        escribirListaLlamadasMisGestRequest(codigoOperacion, lista, response);
    }
    
    public void reenviarLlamadasMisGest(int codOrganizacion, int codTram, int ocurrenciaTram, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        String codigoOperacion = "0";
        FilaLlamadasMisGestVO llamada = new FilaLlamadasMisGestVO();
        List<FilaLlamadasMisGestVO> lista = new ArrayList();
        try
        {        
            AdaptadorSQLBD adaptador = null;
            try {
                adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            } catch (SQLException ex) {
                java.util.logging.Logger.getLogger(MELANBIDE43.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            //String codNotif  = (String)request.getParameter("codNotif");
            //String codProc   = request.getParameter("codProc");
            String numExped  = request.getParameter("numExped");
            String fecDesde  = request.getParameter("fecDesde");
            String fecHasta  = request.getParameter("fecHasta");
            String dniTercero = request.getParameter("dniTercero");
            String codProcedimiento = request.getParameter("codProcedimiento");

            llamada = MeLanbide43Manager.getInstance().getLlamada(numExped, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));

            if (llamada.getId()==0)
            {
                codigoOperacion = "3";
            }
            else
            {
                MeLanbide43Manager.getInstance().getLlamada(numExped, adaptador);
            }

            lista = MeLanbide43Manager.getInstance().getListaLlamadasMisGest(numExped, fecDesde, fecHasta, dniTercero, codProcedimiento, adaptador);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        escribirListaLlamadasMisGestRequest(codigoOperacion, lista, response);
    }
    
    private void escribirListaLlamadasMisGestRequest(String codigoOperacion, List<FilaLlamadasMisGestVO> llamadasMisGest, HttpServletResponse response)
    {
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");


        for(FilaLlamadasMisGestVO llamadaMisGest : llamadasMisGest)
        {
                    xmlSalida.append("<FILA>");
                    xmlSalida.append("<ID>");
                       xmlSalida.append(llamadaMisGest.getId());
                    xmlSalida.append("</ID>");
                    
                    xmlSalida.append("<TER_TID>");
                        if (llamadaMisGest.getTerTid() == 0) {
                            xmlSalida.append("-");
                        }else{
                            xmlSalida.append(llamadaMisGest.getTerTid());
                        }
                    xmlSalida.append("</TER_TID>");
                    xmlSalida.append("<NUM_EXPED>");
                        if (llamadaMisGest.getNumExped() == null) {
                            xmlSalida.append("-");
                        }else{
                            xmlSalida.append(llamadaMisGest.getNumExped());
                        }
                    xmlSalida.append("</NUM_EXPED>");
                    xmlSalida.append("<TER_DOC>");
                     if (llamadaMisGest.getTerDoc() == null){
                            xmlSalida.append("-");
                        }else{ 
                            xmlSalida.append(llamadaMisGest.getTerDoc());
                        }
                    xmlSalida.append("</TER_DOC>");
                    xmlSalida.append("<EXP_TIPO>");
                        if (llamadaMisGest.getExpTipo() == null){
                            xmlSalida.append("-");
                        }else{ 
                            xmlSalida.append(llamadaMisGest.getExpTipo());
                        }
                    xmlSalida.append("</EXP_TIPO>");
                    xmlSalida.append("<TIPO_OPERACION>");
                        if (llamadaMisGest.getTipoOperacion() == null){
                            xmlSalida.append("-");
                        }else{ 
                            xmlSalida.append(llamadaMisGest.getTipoOperacion());
                        }
                    xmlSalida.append("</TIPO_OPERACION>");
                    xmlSalida.append("<COD_TRAMITE_INICIO>");
                        if (llamadaMisGest.getCodTramiteInicio() == null){
                            xmlSalida.append("-");
                       }else{ 
                            xmlSalida.append(llamadaMisGest.getCodTramiteInicio());
                        }
                    xmlSalida.append("</COD_TRAMITE_INICIO>");
                    xmlSalida.append("<FECHA_GENERADO>");
                        if (llamadaMisGest.getFechaGenerado() == null){
                            xmlSalida.append("-");
                        }else{ 
                            xmlSalida.append(llamadaMisGest.getFechaGenerado());
                        }
                    xmlSalida.append("</FECHA_GENERADO>");
                    xmlSalida.append("<FECHA_PROCESADO>");
                        if (llamadaMisGest.getFechaProceso() == null){
                            xmlSalida.append("-");
                        }else{   
                            xmlSalida.append(llamadaMisGest.getFechaProceso());
                        }
                    xmlSalida.append("</FECHA_PROCESADO>");
                    xmlSalida.append("<RES_EJE>");
                        if (llamadaMisGest.getResEje() == 0){
                            xmlSalida.append("-");
                        }else{   
                            xmlSalida.append(Integer.toString(llamadaMisGest.getResEje()));
                        }
                    xmlSalida.append("</RES_EJE>");
                    xmlSalida.append("<RES_NUM>");
                        if (llamadaMisGest.getResNum()== 0){
                            xmlSalida.append("-");
                        }else{   
                            xmlSalida.append(Integer.toString(llamadaMisGest.getResNum()));
                        }
                    xmlSalida.append("</RES_NUM>");
                    xmlSalida.append("<REG_TELEMATICO>");
                        if (llamadaMisGest.getRegTelematico() == null){
                            xmlSalida.append("-");
                        }else{
                            xmlSalida.append(llamadaMisGest.getRegTelematico());
                        }
                    xmlSalida.append("</REG_TELEMATICO>");
                    xmlSalida.append("<FECHA_TELEMATICO>");
                        if (llamadaMisGest.getFechaTelematico()== null){
                            xmlSalida.append("-");
                        }else{
                            xmlSalida.append(llamadaMisGest.getFechaTelematico());
                        }
                    xmlSalida.append("</FECHA_TELEMATICO>");      
                    xmlSalida.append("<NUM_INTENT>");
                        if (llamadaMisGest.getNumIntent() == 0){
                            xmlSalida.append("-");
                        }else{   
                            xmlSalida.append(Integer.toString(llamadaMisGest.getNumIntent()));
                        }
                    xmlSalida.append("</NUM_INTENT>");
                xmlSalida.append("</FILA>");
        }
        
        xmlSalida.append("</RESPUESTA>");
        //log.error("XML salida: " + xmlSalida.toString());
        try{
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void getExcelLlamadas(int codOrganizacion, int codTram, int ocurrenciaTram,String numExpediente, HttpServletRequest request, HttpServletResponse response){
       //Recuperar los datos para descargar excel
       AdaptadorSQLBD adaptador = null;
       try{
            adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            String numExped = request.getParameter("numExped");
            String fecDesde = request.getParameter("fecDesde");
            String fecHasta = request.getParameter("fecHasta");
            String dniTercero = request.getParameter("dniTercero");
            String codProcedimiento = request.getParameter("codProcedimiento");
            HSSFWorkbook wb = MeLanbide43Manager.getInstance().descargarExcelLlamadasMisGest(numExped,fecDesde,fecHasta,dniTercero,codProcedimiento,adaptador);
            String fechaActual = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            String filename = "llamadasMisGestiones" + fechaActual + ".xls";
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment; filename=" + filename); 
            OutputStream out = response.getOutputStream();
            wb.write(out);
       }catch(Exception e){ 
           e.printStackTrace();
           log.error("Error al recuperar el Excel con los datos de llamadas mis gestiones - MELANBIDE43 - getExcelLlamadas", e);
       }
    }
    
    public String abrirEsperaPresentacionRecursoReposicion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente) throws Exception {
        String mensaje = "0";
        AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        Connection con = null;
        try {
            log.info("abrirEsperaPresentacionRecursoReposicion() - Begin - " + numExpediente + " Tramite : " + codTramite);

            String[] arregloDatosExp = numExpediente.split("/");
            Date date = new Date();
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String fechaIni = MeLanbide43Manager.getInstance().fechaInicioExpediente(numExpediente, adaptador);
            date = dateFormat.parse(fechaIni);
            String fecha = MeLanbide43Manager.getInstance().verificarFecha(arregloDatosExp[1], adaptador);
            Date fec = null;
            if (fecha != null && !fecha.isEmpty()) {
                fec = dateFormat.parse(fecha);
                if (date.after(fec)) {
                    //DESARROLLO 0_______________________________________________________________________________________________________________________DESARROLLO 0
                    con = adaptador.getConnection();
                    Integer esDesarrolloCero = MeLanbide43Manager.getInstance().esDesarrolloCero(numExpediente, con);
                    String idProcedimiento = "";
                    if (esDesarrolloCero > 0) {
                        idProcedimiento = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_DESARROLLO_CERO, ConstantesMeLanbide43.FICHERO_PROPIEDADES);
                    } else {
//                        idProcedimiento = MeLanbide43Manager.getInstance().convierteProcedimiento(arregloDatosExp[1]);
                        idProcedimiento = gestionAdaptadoresLan6Config.getCodProcedimientoPlatea(arregloDatosExp[1]); //convierteProcedimiento(codProcedimiento);
                    }
                    //DESARROLLO 0_______________________________________________________________________________________________________________________DESARROLLO 0

                    Lan6InformarConsultaServicios servicios = new Lan6InformarConsultaServicios(arregloDatosExp[1]);
                    ArrayList<FilaListadoMisGestiones> misGestiones = new ArrayList<FilaListadoMisGestiones>();
                    misGestiones = MeLanbide43Manager.getInstance().getInfoGestiones(numExpediente, codOrganizacion, adaptador, codTramite, "");
                    if (!misGestiones.isEmpty()) {
                        log.info("Registro de inicio: " + misGestiones.get(0).getRegInicio().toString());
                        // Abrir Espera Presentacion Recursos
                        Lan6Tramite lan6Tramite = new Lan6Tramite();
//                        lan6Tramite.setId(Lan6Constantes.TRAMITE_ESPERA_RECURSO_POTESTATIVO);
                        lan6Tramite.setId(gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("TRAMITE_ESPERA_RECURSO_POTESTATIVO"));
                        lan6Tramite.setEjercicio(arregloDatosExp[0]);
                        lan6Tramite.setIdExpediente(numExpediente);
                        lan6Tramite.setTipoTramite(Lan6Constantes.TRAMITE_ESPERA);
                        Calendar idInstanciaFechaNotificacion = Calendar.getInstance();

                        Date fechaAcuseNotificacion = new Date(); // La inicialiazamos con la fecha actual 
                        
                        if (arregloDatosExp[1].equals("AERTE")) {
                            //String codigoVisibleTramResol = ConfigurationParameter.getParameter(ConstantesMeLanbide43.TRAM_AERTE_NOTIFICACION_RESOLUCION_COD_VISIBLE, ConstantesMeLanbide43.FICHERO_PROPIEDADES);
                            String codigoTramAcuseNotificacionResol = ConfigurationParameter.getParameter(ConstantesMeLanbide43.TRAM_AERTE_ACUSE_NOTIFICACION_RESOLUCION, ConstantesMeLanbide43.FICHERO_PROPIEDADES);
                            String codigoCampoFechaAcuseNotifResol = ConfigurationParameter.getParameter(ConstantesMeLanbide43.CAMPO_AERTE_FECACUSENOT, ConstantesMeLanbide43.FICHERO_PROPIEDADES);
                            //TramiteVO tramiteVOResolucion = MeLanbide43Manager.getInstance().getDatosTramiteXcodigoVisibleYExpediente(arregloDatosExp[1], codigoVisibleTramResol, numExpediente, adaptador);
                            SalidaIntegracionVO salida = new SalidaIntegracionVO();
                            IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
                            salida=el.getCampoSuplementarioTramite(String.valueOf(codOrganizacion),arregloDatosExp[0],numExpediente, arregloDatosExp[1],Integer.valueOf(codigoTramAcuseNotificacionResol), 1, codigoCampoFechaAcuseNotifResol, 3); // 3 Tipo Fecha
                            log.info("Fecha Acuse Notificacion resolucion : " + 
                                    salida != null && salida.getCampoSuplementario()!=null && salida.getCampoSuplementario().getValorFecha()!=null ?
                                    salida.getCampoSuplementario().getValorFecha()
                                    :
                                    ""
                                    );
                            if (salida != null && salida.getCampoSuplementario()!=null && salida.getCampoSuplementario().getValorFecha()!=null) {
                                fechaAcuseNotificacion =  salida.getCampoSuplementario().getValorFecha().getTime();
                            } else {
                                // De momneto no abrimos la espera al no habar pasado e tramite de resolucion
                                log.error("Error en funcion abrirEsperaPresentacionRecursoReposicion(): 3. No se ha registrado la fecha de acuse de resolucion . Campo suplementatio del tramite FECHA ACUSE NOTIFICACION RESOLUCION no recuperado " );
                                return "3";
                            }
                        }
                        Calendar hoy = Calendar.getInstance();
                        lan6Tramite.setFechaActualizacion(hoy);
                        Lan6Notificacion notificacion = new Lan6Notificacion();
                        log.info("fechaAcuseNotificacion : " + fechaAcuseNotificacion);
                        idInstanciaFechaNotificacion.setTime(fechaAcuseNotificacion);
                        log.info("idInstanciaFechaAcuseNotificacion : " + idInstanciaFechaNotificacion.getTime());
                        notificacion.setFechaRecepcion(idInstanciaFechaNotificacion);  
                        lan6Tramite.setNotificacion(notificacion);
                        ArrayList<Lan6Tramite> tramites = new ArrayList<Lan6Tramite>();
                        tramites.add(lan6Tramite);
                        //Llamada metodo       
                        log.info("Llamada al metodo actualizarTramites : " + numExpediente);
                        log.info("Parametros enviados actualizarTramites - tramites : " + gson.toJson(tramites));
                        String res = servicios.actualizarTramites(tramites);
                        log.info(" - resultado de actualizarTramites Al crear la espera de Recurso  : " + res);
                    }
                }
            } else {
                log.error("4. Fecha Inicio expediente no validada "
                        + "contra fecha de configuracion de Mis Gestiones para el procedimiento en MELANBIDE43_LLAMAMISGEST " + date + " - " + fec);
                mensaje="4";
            }
        } catch (Lan6InformarConsultaExcepcion ice) {
            ArrayList<String> codes = ice.getCodes();
            ArrayList<String> messages = ice.getMessages();
            String causa = ice.getCausaExcepcion();
            String error = "";
            for (int i = 0; i < codes.size(); i++) {
                error += (error.isEmpty() ? messages.get(i) : " " + messages.get(i));
            }
            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("MISGEST_015");
            errorB.setMensajeError("Error Lan6InformarConsultaExcepcion avanzando expediente");
            errorB.setSituacion("abrirEsperaPresentacionRecursoReposicion");
            MeLanbide43Manager.grabarError(errorB, error, causa, numExpediente);
            throw ice;
        } catch (Lan6Excepcion ex) {
            log.error("Error en la funcion abrirEsperaPresentacionRecursoReposicion: " + ex.getException().getMessage());
            String error = "Error en la funcion abrirEsperaPresentacionRecursoReposicion : " + ex.getMessage();
            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("MISGEST_014");
            errorB.setMensajeError("Error Gestionando apertura y cierre de esperas. " + error);
            errorB.setSituacion("abrirEsperaPresentacionRecursoReposicion");
            MeLanbide43Manager.grabarError(errorB, ex.getException().getMessage().toString(), ex.getException().toString(), numExpediente);
            throw ex;
        } catch (Exception x) {
            mensaje = "2";
            log.error("Error en funcion abrirEsperaPresentacionRecursoReposicion(): " + x);
            throw x;
        } finally {
            adaptador.devolverConexion(con);
        }
        log.info("abrirEsperaPresentacionRecursoReposicion() - End - " + numExpediente + " : " + mensaje);
        return mensaje;
    }
    
    public String cerrarEsperaPresentacionRecursoReposicion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente) throws Exception {
        String mensaje = "0";
        AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        Connection con = null;
        try {
            log.info("cerrarEsperaPresentacionRecursoReposicion() - Begin - " + numExpediente + " Tramite : " + codTramite);
            String[] arregloDatosExp = numExpediente.split("/");
            Date date = new Date();
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String fechaIni = MeLanbide43Manager.getInstance().fechaInicioExpediente(numExpediente, adaptador);
            date = dateFormat.parse(fechaIni);
            String fecha = MeLanbide43Manager.getInstance().verificarFecha(arregloDatosExp[1], adaptador);
            Date fec = null;
            if (fecha != null && !fecha.isEmpty()) {
                fec = dateFormat.parse(fecha);
                if (date.after(fec)) {
                    //DESARROLLO 0_______________________________________________________________________________________________________________________DESARROLLO 0
                    con = adaptador.getConnection();
                    Integer esDesarrolloCero = MeLanbide43Manager.getInstance().esDesarrolloCero(numExpediente, con);
                    String idProcedimiento = "";
                    if (esDesarrolloCero > 0) {
                        idProcedimiento = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_DESARROLLO_CERO, ConstantesMeLanbide43.FICHERO_PROPIEDADES);
                    } else {
//                        idProcedimiento = MeLanbide43Manager.getInstance().convierteProcedimiento(arregloDatosExp[1]);
                        idProcedimiento = gestionAdaptadoresLan6Config.getCodProcedimientoPlatea(arregloDatosExp[1]); //convierteProcedimiento(codProcedimiento);
                    }
                    //DESARROLLO 0_______________________________________________________________________________________________________________________DESARROLLO 0

                    Lan6InformarConsultaServicios servicios = new Lan6InformarConsultaServicios(arregloDatosExp[1]);
                    ArrayList<FilaListadoMisGestiones> misGestiones = new ArrayList<FilaListadoMisGestiones>();
                    misGestiones = MeLanbide43Manager.getInstance().getInfoGestiones(numExpediente, codOrganizacion, adaptador, codTramite, "");
                    if (!misGestiones.isEmpty()) {
                        log.info("Registro de inicio: " + misGestiones.get(0).getRegInicio().toString());
                        // Cierra Espera Presentacion Recursos
                        Lan6Tramite lan6Tramite = new Lan6Tramite();
//                        lan6Tramite.setId(Lan6Constantes.TRAMITE_ESPERA_RECURSO_POTESTATIVO);
                        lan6Tramite.setId(gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("TRAMITE_ESPERA_RECURSO_POTESTATIVO"));
                        lan6Tramite.setEjercicio(arregloDatosExp[0]);
                        lan6Tramite.setIdExpediente(numExpediente);
                        lan6Tramite.setTipoTramite(Lan6Constantes.TRAMITE_ESPERA);
                        lan6Tramite.setFechaActualizacion(Calendar.getInstance());
                        lan6Tramite.setFinTramiteEspera(true);
                        ArrayList<Lan6Tramite> tramites = new ArrayList<Lan6Tramite>();
                        tramites.add(lan6Tramite);
                        //Llamada metodo       
                        log.info("Llamada al metodo actualizarTramites : " + numExpediente);
                        log.info("Parametros enviados actualizarTramites - tramites : " + gson.toJson(tramites));
                        String res = servicios.actualizarTramites(tramites);
                        log.info(" - resultado de actualizarTramites Al Cerrar la espera de Recurso  : " + res);
                    }else{
                        log.info("No tiene datos en Mis Gestiones/Carpeta no PROCESAMOS.");
                    }
                }
            } else {
                log.error("4. Fecha Inicio expediente no validada "
                        + "contra fecha de configuracion de Mis Gestiones para el procedimiento en MELANBIDE43_LLAMAMISGEST " + date + " - " + fec);
                mensaje = "4";
            }
        } catch (Lan6InformarConsultaExcepcion ice) {
            ArrayList<String> codes = ice.getCodes();
            ArrayList<String> messages = ice.getMessages();
            String causa = ice.getCausaExcepcion();
            String error = "";
            for (int i = 0; i < codes.size(); i++) {
                error += (error.isEmpty() ? messages.get(i) : " " + messages.get(i));
            }
            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("MISGEST_015");
            errorB.setMensajeError("Error Lan6InformarConsultaExcepcion avanzando expediente");
            errorB.setSituacion("abrirEsperaPresentacionRecursoReposicion");
            MeLanbide43Manager.grabarError(errorB, error, causa, numExpediente);
            throw ice;
        } catch (Lan6Excepcion ex) {
            log.error("Error en la funcion cerrarEsperaPresentacionRecursoReposicion: " + ex.getException().getMessage());
            String error = "Error en la funcion cerrarEsperaPresentacionRecursoReposicion : " + ex.getMessage();
            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("MISGEST_014");
            errorB.setMensajeError("Error Gestionando apertura y cierre de esperas. " + error);
            errorB.setSituacion("cerrarEsperaPresentacionRecursoReposicion");
            MeLanbide43Manager.grabarError(errorB, ex.getException().getMessage(), ex.getException().toString(), numExpediente);
            throw ex;
        } catch (Exception x) {
            mensaje = "2";
            log.error("Error en funcion cerrarEsperaPresentacionRecursoReposicion(): " + x);
            throw x;
        } finally {
            adaptador.devolverConexion(con);
        }
        log.info("cerrarEsperaPresentacionRecursoReposicion() - End - " + numExpediente + " : " + mensaje);
        return mensaje;
    }
    
    /**
     * 
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @param codEspera - lan6Tramite.setId(Lan6Constantes.TRAMITE_ESPERA_DESISTIMIENTO);
     * @param descEspera - lan6Tramite.setDescripcion(Lan6Constantes.TRAMITE_DESISTIMIENTO_DESC);
     * @param cierre - boolean true si es cierre de espera, false si es apertura de espera
     * @param desLog - descripción operación - cerrarEsperaDesistimiento
     * @return
     * @throws Exception 
     */
    private String gestionarEspera (int codOrganizacion,int codTramite,int ocurrenciaTramite, String numExpediente, 
            String codEspera, String descEspera, boolean cierre, String desLog) throws Exception{
       String mensaje = "0";
       AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
       Connection con = null;
       try{
            log.info("En funcion " + desLog + ": " + numExpediente);
            
            String[] pepe = numExpediente.split("/");
            Date date = new Date();
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String fechaIni = MeLanbide43Manager.getInstance().fechaInicioExpediente(numExpediente, adaptador);                
            date = dateFormat.parse(fechaIni);
            String fecha = MeLanbide43Manager.getInstance().verificarFecha(pepe[1], adaptador);
            Date fec = null;
            if(fecha != ""){
                fec = dateFormat.parse(fecha);
                if(date.after(fec)){  
            
                    //DESARROLLO 0_______________________________________________________________________________________________________________________DESARROLLO 0
                    con = adaptador.getConnection();
                    Integer esDesarrolloCero = MeLanbide43Manager.getInstance().esDesarrolloCero(numExpediente, con);
                    String idProcedimiento = "";
                    if (esDesarrolloCero > 0) {
                        idProcedimiento = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_DESARROLLO_CERO, ConstantesMeLanbide43.FICHERO_PROPIEDADES);
                    } else {
//                            idProcedimiento = MeLanbide43Manager.getInstance().convierteProcedimiento(pepe[1]);
                        idProcedimiento = gestionAdaptadoresLan6Config.getCodProcedimientoPlatea(pepe[1]); //convierteProcedimiento(codProcedimiento);
                    }
                    //DESARROLLO 0_______________________________________________________________________________________________________________________DESARROLLO 0


                    Lan6InformarConsultaServicios servicios = new Lan6InformarConsultaServicios(pepe[1]);
                    ArrayList<FilaListadoMisGestiones> misGestiones = new ArrayList<FilaListadoMisGestiones>();
                    misGestiones = MeLanbide43Manager.getInstance().getInfoGestiones(numExpediente, codOrganizacion, adaptador, codTramite, "");

                    if(!misGestiones.isEmpty()){
                        Lan6Tramite   lan6Tramite = new Lan6Tramite();   
                        lan6Tramite.setId(codEspera);
                        lan6Tramite.setDescripcion(descEspera);
                        lan6Tramite.setDescripcionEu(descEspera);

                        lan6Tramite.setEjercicio(pepe[0]);
                        lan6Tramite.setIdExpediente(numExpediente);

                        lan6Tramite.setTipoTramite(Lan6Constantes.TRAMITE_ESPERA);
                        //cierre de espera
                        if (cierre){
                            lan6Tramite.setFinTramiteEspera(true);
                        }
                        
                        Calendar hoy = Calendar.getInstance();
                        lan6Tramite.setFechaActualizacion(hoy);

                        ArrayList<Lan6Tramite> tramites = new ArrayList<Lan6Tramite>();
                        tramites.add(lan6Tramite);       
                        //Llamada método       
                        log.info("llamada al metodo actualizarTramites: " + numExpediente);
                        log.info("Parametros enviados actualizarTramites - tramites : " + gson.toJson(tramites));
                        String res = servicios.actualizarTramites(tramites);
                        log.info("resultado de actualizarTramites : " + res);
                    }
                }
            }

            log.info(desLog + "() : END");
        }
        catch(Lan6InformarConsultaExcepcion ice){

            ArrayList<String> codes = ice.getCodes();
            ArrayList<String> messages = ice.getMessages();

            String causa = "";
            causa = ice.getCausaExcepcion();

            StackTraceElement[] stacktrace=ice.getStackTrace();
            String error="";
            for (int i=0; i<codes.size();i++){
                error += messages.get(i);
            }

            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("MISGEST_015");
            errorB.setMensajeError("Error Lan6InformarConsultaExcepcion avanzando expediente");
            errorB.setSituacion(desLog);

            MeLanbide43Manager.grabarError(errorB, error, causa, numExpediente);
            throw ice;
        }catch(Lan6Excepcion ex){            
            log.error("Error en la funcion avanceExpConsulta: " + ex.getException().getMessage());
            String error = "Error en la funcion avanceExpConsulta: " + ex.getException().getMessage();
            
            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("MISGEST_014");
            errorB.setMensajeError("Error avanzando expediente");
            errorB.setSituacion(desLog);

            MeLanbide43Manager.grabarError(errorB, ex.getException().getMessage().toString(), ex.getException().toString(), numExpediente);
            
            throw ex;
        }  
        catch(Exception x) {
            mensaje = "2";
            log.error("Error en funcion " + desLog + "(): " + x);
            throw x;
        }
        finally {
            adaptador.devolverConexion(con);
        }
        return mensaje;
    }
    
    public String cerrarEspDesistimiento (int codOrganizacion,int codTramite,int ocurrenciaTramite,String numExpediente, ModuloIntegracionExternoParamAdicionales parametros) throws Exception{
        String mensaje = "0";
        try{
            log.info("cerrarEspDesistimiento() : begin");
            AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            MeLanbide43Manager meLanbide43Manager = MeLanbide43Manager.getInstance();
            String[] datos = numExpediente.split(BARRA);
            Date date = new Date();
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String fechaIni = meLanbide43Manager.fechaInicioExpediente(numExpediente, adaptador);                
            date = dateFormat.parse(fechaIni);
            String fecha = MeLanbide43Manager.getInstance().verificarFecha(datos[1], adaptador);
            Date fec = null;
            
            if (fecha != "") {
                fec = dateFormat.parse(fecha);
                if (date.after(fec)) {
                    if ("IN".equals(parametros.getOrigenLlamada())) {
                        //cerrarEsperaDesistimiento
                        gestionarEspera(codOrganizacion, codTramite, ocurrenciaTramite, numExpediente,
                                gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("TRAMITE_ESPERA_DESISTIMIENTO"),
                                gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("TRAMITE_DESISTIMIENTO_DESC"), true, "cerrarEsperaDesistimiento");
                    }
                }
            }
            log.info("cerrarEspDesistimiento() : END");
        }
        catch(Exception x) {
            mensaje = "2";
            log.error("Error cerrarEspDesistimiento: " + x.getMessage());
            throw x;
        }
        return mensaje;
    }
    
    public String cerrarEspDesistimientoAbrirEspRenunciaRecursoStd (int codOrganizacion,int codTramite,int ocurrenciaTramite,String numExpediente, ModuloIntegracionExternoParamAdicionales parametros) throws Exception{
        String mensaje = "0";
        try{
            log.info("cerrarEspDesistimientoAbrirEspRenunciaRecursoStd() : begin");
            AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            MeLanbide43Manager meLanbide43Manager = MeLanbide43Manager.getInstance();
            String[] datos = numExpediente.split(BARRA);
            Date date = new Date();
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String fechaIni = meLanbide43Manager.fechaInicioExpediente(numExpediente, adaptador);                
            date = dateFormat.parse(fechaIni);
            String fecha = MeLanbide43Manager.getInstance().verificarFecha(datos[1], adaptador);
            Date fec = null;
            
            if(fecha != ""){
                fec = dateFormat.parse(fecha);
                if(date.after(fec)){                     
                    if("IN".equals(parametros.getOrigenLlamada())) {
                        //cerrarEsperaDesistimiento
                        gestionarEspera(codOrganizacion, codTramite, ocurrenciaTramite, numExpediente, 
                                gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("TRAMITE_ESPERA_DESISTIMIENTO"),
                                gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("TRAMITE_DESISTIMIENTO_DESC"), true, "cerrarEsperaDesistimiento");
                        //abrirEsperaRecurso
                        gestionarEspera(codOrganizacion, codTramite, ocurrenciaTramite, numExpediente, 
                                gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("TRAMITE_ESPERA_RECURSO_ALZADA"),
                                gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("TRAMITE_RECURSO_ALZADA_DOC_DESC"), false, "abrirEsperaRecurso");
                        //abrirEsperaRenuncia
                        gestionarEspera(codOrganizacion, codTramite, ocurrenciaTramite, numExpediente, 
                                gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("TRAMITE_ESPERA_RESIGNACION"),
                                gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("TRAMITE_RENUNCIA_DESC"), false, "abrirEsperaRenuncia");
                    }
                }
            }
            log.info("cerrarEspDesistimientoAbrirEspRenunciaRecursoStd() : END");
        }
        catch(Exception x) {
            mensaje = "2";
            log.error("Error cerrarEspDesistimientoAbrirEspRenunciaRecursoStd: " + x.getMessage());
            throw x;
        }
        return mensaje;
    }
    
    public String cerrarEspRenunciaRecursoStd (int codOrganizacion,int codTramite,int ocurrenciaTramite,String numExpediente, ModuloIntegracionExternoParamAdicionales parametros) throws Exception{
        String mensaje = "0";
        try{
            log.info("cerrarEspRenunciaRecursoStd() : begin");
            AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            MeLanbide43Manager meLanbide43Manager = MeLanbide43Manager.getInstance();
            String[] datos = numExpediente.split(BARRA);
            Date date = new Date();
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String fechaIni = meLanbide43Manager.fechaInicioExpediente(numExpediente, adaptador);                
            date = dateFormat.parse(fechaIni);
            String fecha = MeLanbide43Manager.getInstance().verificarFecha(datos[1], adaptador);
            Date fec = null;
            
            if(fecha != ""){
                fec = dateFormat.parse(fecha);
                if (date.after(fec)) {
                    if ("IN".equals(parametros.getOrigenLlamada())) {
                        //cerrarEsperaRenuncia
                        gestionarEspera(codOrganizacion, codTramite, ocurrenciaTramite, numExpediente,
                                gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("TRAMITE_ESPERA_RESIGNACION"),
                                gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("TRAMITE_RENUNCIA_DESC"), true, "cerrarEsperaRenuncia");
                        //cerrarEsperaRecurso
                        gestionarEspera(codOrganizacion, codTramite, ocurrenciaTramite, numExpediente,
                                gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("TRAMITE_ESPERA_RECURSO_ALZADA"),
                                gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("TRAMITE_RECURSO_ALZADA_DOC_DESC"), true, "cerrarEsperaRecurso");
                    }
                }
            }
            log.info("cerrarEspRenunciaRecursoStd() : END");
        }
        catch(Exception x) {
            mensaje = "2";
            log.error("Error cerrarEspRenunciaRecursoStd: " + x.getMessage());
            throw x;
        }
        return mensaje;
    }
    
    //se debe poner en el avance del trámite de acuse de la resolución (el cual debe tener grabada la fecha de acuse en el campo suplementario FECACUSENOT, se cogerá esta fecha como fecha de acuse)
    public String abrirEsperaRecurso(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente) throws Exception {
        String mensaje = "0";
        AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        Connection con = null;
        try {
            log.info("abrirEsperaRecurso() - Begin - " + numExpediente + " Tramite : " + codTramite);

            String[] arregloDatosExp = numExpediente.split("/");
            Date date = new Date();
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String fechaIni = MeLanbide43Manager.getInstance().fechaInicioExpediente(numExpediente, adaptador);
            date = dateFormat.parse(fechaIni);
            String fecha = MeLanbide43Manager.getInstance().verificarFecha(arregloDatosExp[1], adaptador);
            Date fec = null;
            if (fecha != null && !fecha.isEmpty()) {
                fec = dateFormat.parse(fecha);
                if (date.after(fec)) {
                    //DESARROLLO 0_______________________________________________________________________________________________________________________DESARROLLO 0
                    con = adaptador.getConnection();
                    Integer esDesarrolloCero = MeLanbide43Manager.getInstance().esDesarrolloCero(numExpediente, con);
                    String idProcedimiento = "";
                    if (esDesarrolloCero > 0) {
                        idProcedimiento = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_DESARROLLO_CERO, ConstantesMeLanbide43.FICHERO_PROPIEDADES);
                    } else {
//                        idProcedimiento = MeLanbide43Manager.getInstance().convierteProcedimiento(arregloDatosExp[1]);
                        idProcedimiento = gestionAdaptadoresLan6Config.getCodProcedimientoPlatea(arregloDatosExp[1]); //convierteProcedimiento(codProcedimiento);
                    }
                    //DESARROLLO 0_______________________________________________________________________________________________________________________DESARROLLO 0

                    Lan6InformarConsultaServicios servicios = new Lan6InformarConsultaServicios(arregloDatosExp[1]);
                    ArrayList<FilaListadoMisGestiones> misGestiones = new ArrayList<FilaListadoMisGestiones>();
                    misGestiones = MeLanbide43Manager.getInstance().getInfoGestiones(numExpediente, codOrganizacion, adaptador, codTramite, "");
                    if (!misGestiones.isEmpty()) {
                        // Abrir Espera Presentacion Recursos
                        Lan6Tramite lan6Tramite = new Lan6Tramite();
//                        lan6Tramite.setId(Lan6Constantes.TRAMITE_ESPERA_RECURSO_ALZADA);
                        lan6Tramite.setId(gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("TRAMITE_ESPERA_RECURSO_ALZADA"));
                        lan6Tramite.setEjercicio(arregloDatosExp[0]);
                        lan6Tramite.setIdExpediente(numExpediente);
                        lan6Tramite.setTipoTramite(Lan6Constantes.TRAMITE_ESPERA);
                        Calendar idInstanciaFechaNotificacion = Calendar.getInstance();

                        Date fechaAcuseNotificacion = new Date(); // fecha actual 
                        
                        
                        
 //_____________________fecha de acuse del campo FECACUSENOT del trámite de acuse, que es el que se avanza____________________________________________________________________________________________________________
 //de momento se deja la fecha actual (deberían avanzar en fecha de acuse)
 /*
                        String codigoCampoFechaAcuseNotifResol = "FECACUSENOT";
                        SalidaIntegracionVO salida = new SalidaIntegracionVO();
                        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
                        salida=el.getCampoSuplementarioTramite(String.valueOf(codOrganizacion),arregloDatosExp[0],numExpediente, arregloDatosExp[1],codTramite, ocurrenciaTramite, codigoCampoFechaAcuseNotifResol, 3); // 3 Tipo Fecha
                        log.info("Fecha Acuse Notificacion resolucion : " + 
                                salida != null && salida.getCampoSuplementario()!=null && salida.getCampoSuplementario().getValorFecha()!=null ?
                                salida.getCampoSuplementario().getValorFecha()
                                :
                                ""
                                );
                        if (salida != null && salida.getCampoSuplementario()!=null && salida.getCampoSuplementario().getValorFecha()!=null) {
                            fechaAcuseNotificacion =  salida.getCampoSuplementario().getValorFecha().getTime();
                        } else {
                            //No abrimos la espera
                            log.error("Error en funcion abrirEsperaRecurso(): 3. No se ha registrado la fecha de acuse de resolucion . Campo suplementario del tramite FECHA ACUSE NOTIFICACION RESOLUCION no recuperado " );
                            return "3";
                        }
*/
 //_____________________fecha de acuse del campo FECACUSENOT del trámite de acuse, que es el que se avanza____________________________________________________________________________________________________________
                        
                                
                        Calendar hoy = Calendar.getInstance();
                        lan6Tramite.setFechaActualizacion(hoy);
                        Lan6Notificacion notificacion = new Lan6Notificacion();
                        log.info("fechaAcuseNotificacion : " + fechaAcuseNotificacion);
                        idInstanciaFechaNotificacion.setTime(fechaAcuseNotificacion);
                        log.info("idInstanciaFechaAcuseNotificacion : " + idInstanciaFechaNotificacion.getTime());
                        notificacion.setFechaRecepcion(idInstanciaFechaNotificacion);  
                        lan6Tramite.setNotificacion(notificacion);
                        ArrayList<Lan6Tramite> tramites = new ArrayList<Lan6Tramite>();
                        tramites.add(lan6Tramite);
                        //Llamada metodo       
                        log.info("Llamada al metodo actualizarTramites : " + numExpediente);
                        log.info("Parametros enviados actualizarTramites - tramites : " + gson.toJson(tramites));
                        String res = servicios.actualizarTramites(tramites);
                        log.info(" - resultado de actualizarTramites Al crear la espera de Recurso  : " + res);
                    }
                }
            } else {
                log.error("4. Fecha Inicio expediente no validada "
                        + "contra fecha de configuracion de Mis Gestiones para el procedimiento en MELANBIDE43_LLAMAMISGEST " + date + " - " + fec);
                mensaje="4";
            }
        } catch (Lan6InformarConsultaExcepcion ice) {
            ArrayList<String> codes = ice.getCodes();
            ArrayList<String> messages = ice.getMessages();
            String causa = ice.getCausaExcepcion();
            String error = "";
            for (int i = 0; i < codes.size(); i++) {
                error += (error.isEmpty() ? messages.get(i) : " " + messages.get(i));
            }
            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("MISGEST_015");
            errorB.setMensajeError("Error Lan6InformarConsultaExcepcion avanzando expediente");
            errorB.setSituacion("abrirEsperaRecurso");
            MeLanbide43Manager.grabarError(errorB, error, causa, numExpediente);
            throw ice;
        } catch (Lan6Excepcion ex) {
            log.error("Error en la funcion abrirEsperaRecurso: " + ex.getException().getMessage());
            String error = "Error en la funcion abrirEsperaRecurso : " + ex.getMessage();
            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("MISGEST_014");
            errorB.setMensajeError("Error Gestionando apertura y cierre de esperas. " + error);
            errorB.setSituacion("abrirEsperaRecurso");
            MeLanbide43Manager.grabarError(errorB, ex.getException().getMessage().toString(), ex.getException().toString(), numExpediente);
            throw ex;
        } catch (Exception x) {
            mensaje = "2";
            log.error("Error en funcion abrirEsperaRecurso(): " + x);
            throw x;
        } finally {
            adaptador.devolverConexion(con);
        }
        log.info("abrirEsperaRecurso() - End - " + numExpediente + " : " + mensaje);
        return mensaje;
    }
    
    public String cerrarEspRecursoStd(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, ModuloIntegracionExternoParamAdicionales parametros) throws Exception {
        String mensaje = "0";
        try {
            log.info("cerrarEspRecursoStd() : begin");
            AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            MeLanbide43Manager meLanbide43Manager = MeLanbide43Manager.getInstance();
            String[] datos = numExpediente.split(BARRA);
            Date date = new Date();
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String fechaIni = meLanbide43Manager.fechaInicioExpediente(numExpediente, adaptador);
            date = dateFormat.parse(fechaIni);
            String fecha = MeLanbide43Manager.getInstance().verificarFecha(datos[1], adaptador);
            Date fec = null;

            if (fecha != "") {
                fec = dateFormat.parse(fecha);
                if (date.after(fec)) {
                    if ("IN".equals(parametros.getOrigenLlamada())) {
                        //cerrarEsperaRecurso
                        gestionarEspera(codOrganizacion, codTramite, ocurrenciaTramite, numExpediente,
                                gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("TRAMITE_ESPERA_RECURSO_ALZADA"),
                                gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("TRAMITE_RECURSO_ALZADA_DOC_DESC"), true, "cerrarEsperaRecurso");
                    }
                }
            }
            log.info("cerrarEspRecursoStd() : END");
        } catch (Exception x) {
            mensaje = "2";
            log.error("Error cerrarEspRecursoStd: " + x.getMessage());
            throw x;
        }
        return mensaje;
    }

    private boolean hayRepresentanteLegalEnListaInteresado(ArrayList<Lan6Interesado> interesados) {
        if(interesados!=null && !interesados.isEmpty()){
            for (Lan6Interesado interesado : interesados) {
                if(Lan6Constantes.TIPO_INTERESADO_REPRESENTANTE.equalsIgnoreCase(interesado.getTipo()))
                    return true;
            }
        }
        return false;
    }
    
}