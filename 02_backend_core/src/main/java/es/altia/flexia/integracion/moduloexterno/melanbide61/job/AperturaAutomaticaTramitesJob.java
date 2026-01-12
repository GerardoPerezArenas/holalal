/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.flexia.integracion.moduloexterno.melanbide61.job;

import es.altia.agora.business.sge.exception.TramitacionException;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.flexia.integracion.moduloexterno.melanbide61.dao.MeLanbide61DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide61.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide61.util.ConstantesMeLanbide43;
import es.altia.flexia.integracion.moduloexterno.melanbide61.util.ConstantesMeLanbide61;
import es.altia.flexia.integracion.moduloexterno.melanbide61.vo.ExpedienteREPLE;
import es.altia.flexia.integracion.moduloexterno.melanbide61.vo.ExpedienteUAAP;
import es.altia.flexia.registro.digitalizacion.lanbide.util.GestionAdaptadoresLan6Config;
import es.altia.technical.PortableContext;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.lanbide.lan6.adaptadoresPlatea.informarConsulta.beans.Lan6Tramite;
import es.lanbide.lan6.adaptadoresPlatea.informarConsulta.servicios.Lan6InformarConsultaServicios;
import es.lanbide.lan6.adaptadoresPlatea.utilidades.constantes.Lan6Constantes;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javax.sql.DataSource;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;

public class AperturaAutomaticaTramitesJob implements Job {

    private static final GestionAdaptadoresLan6Config gestionAdaptadoresLan6Config = new GestionAdaptadoresLan6Config();

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        try {
            int pepe = jec.getRefireCount();
            Job trab = jec.getJobInstance();

            log.error("jec.getRefireCount(): " + pepe);
            Trigger pepito = jec.getTrigger();
            
            String servidor = ConfigurationParameter.getParameter(ConstantesMeLanbide61.CAMPO_SERVIDOR, ConstantesMeLanbide61.FICHERO_PROPIEDADES);
            
            //String servidor = "flexia1"; //DESA
            //String servidor = "paprergi1_flexia1"; //PRE
            //String servidor = "pargi1_flexia1"; //PRO
            log.error("servidor: " + servidor);
            if(servidor.equals(System.getProperty("weblogic.Name")))            {//PARA LOCAL QUITAR
            synchronized(jec){
                 Connection con = null;
                 
                 boolean dev = false;
                 int numIntentos = 0;
                 String numExpediente = "";
                 int id = 0;
                 String[] params = new String []{"ORACLE"};
                 try
                 {
                     log.error("Execute lanzado " + System.getProperty("weblogic.Name"));
                     //int codOrg = 0;      //pruebas
                     //int codOrg = 1;      //real
                     
                     
                     ////job
                    /*public static String CAMPO_SERVIDOR = "SERVIDOR";
                    public static String TRAMITE_INCUMPLIMIENTO = "TRAMITE_INCUMPLIMIENTO";
                    public static String CODIGO_DOC_TRAMITE = "CODIGO_DOC_TRAMITE";
                    public static String COD_ORG = "COD_ORG";    
                    public static String DOS_ENTORNOS = "DOS_ENTORNOS";
                    public static String UNIDAD_ORG = "UNIDAD_ORG";

                    public static String ENTORNO = "ENTORNO";*/
                    int codOrg = Integer.parseInt(ConfigurationParameter.getParameter("COD_ORG",ConstantesMeLanbide61.FICHERO_PROPIEDADES));
                    //int codOrg = 0;      //pruebas
                    //int codOrg = 1;      //real
                    boolean dosEntornos = Boolean.getBoolean(ConfigurationParameter.getParameter("DOS_ENTORNOS", 
                    ConstantesMeLanbide61.FICHERO_PROPIEDADES));
                  
                    while(codOrg < 2){

                        con = this.getAdaptSQLBD(String.valueOf(codOrg)).getConnection();
                        log.error("en el while de tokens codOrg: " + codOrg);

                        /* #237402
                         * Buscar los expedientes telemáticos (los que hayan sido iniciados con un registro telemático). En E_EXR salen los registros asociados al expediente. 
                         * Un registro es telemático si en R_RES el campo REGISTRO_TELEMATICO es 1                        
                        */
                        MeLanbide61DAO meLanbide61DAO = MeLanbide61DAO.getInstance();
                        //REPLE                        
                        List<ExpedienteREPLE> expedientes = meLanbide61DAO.getExpedientesTelematicosREPLE(con);
                        log.debug("ExpedientesTelematicosREPLE: "+expedientes.size());
                        Calendar hoy = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MMMMM/yyyy hh:mm:ss");
                        for(Iterator<ExpedienteREPLE> i = expedientes.iterator(); i.hasNext(); ) {
                            ExpedienteREPLE item = i.next();
                            numExpediente = item.getNumExpediente();
                            log.debug("numExpediente: " + numExpediente + ", fecha resol: " + item.getFecResolucion() + ", fecha contrato rel: " + item.getFecContratoRelevo());
                            String[] exp = numExpediente.split("/");
                            String ejercicio = exp[0];
//                            String idProcedimiento = convierteProcedimiento(exp[1]);
                            String idProcedimiento = gestionAdaptadoresLan6Config.getCodProcedimientoPlatea(exp[1]); //convierteProcedimiento(codProcedimiento);
                            Lan6InformarConsultaServicios servicios = new Lan6InformarConsultaServicios(exp[1]); // 2.2
                            if (item.getFecResolucion() != null) {
                                log.debug("Fecha resolución no nula: " + item.getFecResolucion());
                                //si fecha actual es mayor que fecha resolucion +12 meses --> abrir trámite 399
                                Calendar c1 = GregorianCalendar.getInstance();
                                c1.setTime(item.getFecResolucion());
                                c1.add(Calendar.MONTH, 12);
                                
                                log.debug("Fecha resolucion +12 meses Formateada: "+sdf.format(c1.getTime()));  
                                System.out.println(item);
                                if (new Date().compareTo(c1.getTime())>=0){
                                //if (item.getFecResolucion() != null && (new Date().compareTo(c1.getTime())>=0)){
                                    log.error("REPLE tramite 399");
                                    //pendiente comprobar antes si ya existe el tramite o no
                                    boolean existeTram = meLanbide61DAO.existeTramite(numExpediente, "399", con);
                                    if (!existeTram){
                                        log.debug("No existe el trámite 399. Hacer apertura");
                                        
                                        //creamos trámite en regexlan
                                        /*TramitacionExpedientesValueObject tEVO = new TramitacionExpedientesValueObject();
                                        tEVO.setCodMunicipio(Integer.toString(codOrg));
                                        tEVO.setCodProcedimiento(exp[1]);
                                        tEVO.setEjercicio(ejercicio);
                                        tEVO.setNumeroExpediente(numExpediente);
                                        log.debug("obtenerCodigoInternoTramite("+codOrg+",REPLE,399,"+con);
                                        Long codTramite =meLanbide61DAO.obtenerCodigoInternoTramite(codOrg, "REPLE","399",  con);
                                        log.debug("CÓDIGO INTERNO trámite 399:"+codTramite);
                                        tEVO.setCodTramite(codTramite.toString());
                                        tEVO.setOcurrenciaTramite(Integer.toString(1));
                                        
                                        //UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
                                        //String[] params = usuario.getParamsCon();
                                        
                                        
                                        Vector listaTramitesIniciar =new Vector();
                                        TramitacionExpedientesValueObject traExp = new TramitacionExpedientesValueObject();
                                        traExp.setCodTramite(codTramite.toString());
                                        traExp.setOcurrenciaTramite("1");
                                        listaTramitesIniciar.add(traExp);
                                        
                                        if (log.isDebugEnabled()) log.debug("el tamanyo de la lista para finalizar es : "
                                                + listaTramitesIniciar.size());
                                        tEVO.setListaTramitesIniciar(listaTramitesIniciar);
                                        
                                        
                                        //iniciarTramitesManual(tEVO, params);
                                        
                                        Vector devolver =TramitacionExpedientesManager.getInstance().iniciarTramitesManual(tEVO, params);
                                        // log.debug("DEVOLVER TAMAÑO INICIARTRAMITE:"+devolver.size());
                                        log.debug("DEVOLVER INICIAR TRAMITE:"+devolver);
                                        */    
                                        log.debug("obtenerCodigoInternoTramite("+codOrg+",REPLE,399,"+con);
                                        Long codTramite =meLanbide61DAO.obtenerCodigoInternoTramite(codOrg, "REPLE","399",  con);
                                        
                                        String unidad =meLanbide61DAO.obtenerUnidadExpediente(numExpediente, con);
                                        log.debug("UNIDAD TRAMITADORA:"+unidad);
                                        meLanbide61DAO.crearTramite(codOrg,numExpediente, codTramite, unidad,con);
                                        
                                        
                                        //creamos trámite en mis gestiones
                                        Lan6Tramite   lan6Tramite = new Lan6Tramite(); 
                                        lan6Tramite.setId(Lan6Constantes.TRAMITE_ESPERA_REQUERIMIENTO_APORTACION);
                                        lan6Tramite.setDescripcion(Lan6Constantes.TRAMITE_ESPERA_REQUERIMIENTO_APORTACION_DESC);
                                        lan6Tramite.setTipoTramite(Lan6Constantes.TRAMITE_ESPERA);

//                                        lan6Tramite.setInstanciaId(ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_INSTANCIA_PAGO2_LAN62_REPLE, ConstantesMeLanbide43.FICHERO_ADPATADORES));                           
                                        lan6Tramite.setInstanciaId(gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties(ConstantesMeLanbide43.ID_INSTANCIA_PAGO2_LAN62_REPLE)); // 2.2

                                        //Datos del expediente
                                        lan6Tramite.setEjercicio(exp[0]);
                                        lan6Tramite.setIdExpediente(numExpediente);                     
                                        lan6Tramite.setFechaActualizacion(hoy);

                                        ArrayList<Lan6Tramite> tramites = new ArrayList<Lan6Tramite>();
                                        tramites.add(lan6Tramite);     

                                        //Llamada método     
                                        log.error("Antes de llamar a actualizarTramites");
                                        String res = servicios.actualizarTramites(tramites);
                                    }
                                }  
                            }                            
                            if (item.getFecContratoRelevo() != null){
                                log.debug("Fecha ContratoRelevo no nula: "+item.getFecContratoRelevo());
                                //si fecha actual es mayor que  fecha inicio contrato relevo + 20 meses --> abrir trámite 499
                                Calendar c2 = GregorianCalendar.getInstance();
                                c2.setTime(item.getFecContratoRelevo());
                                c2.add(Calendar.MONTH, 20);
                                log.debug("Fecha inicio contrato relevo + 20 meses Formateada: "+sdf.format(c2.getTime()));        
                                //if (item.getFecContratoRelevo() != null && (new Date().compareTo(c2.getTime())>0)){
                                if (new Date().compareTo(c2.getTime())>0){
                                    log.error("REPLE tramite 499");
                                    //pendiente comprobar antes si ya existe el tramite o no
                                    boolean existeTram = meLanbide61DAO.existeTramite(numExpediente, "499", con);                                
                                    if (!existeTram){
                                        log.debug("No existe el trámite 499. Hacer apertura");
                                        ////creamos trámite en regexlan
                                        /*TramitacionExpedientesValueObject tEVO = new TramitacionExpedientesValueObject();
                                        tEVO.setCodMunicipio(Integer.toString(codOrg));
                                        tEVO.setCodProcedimiento(exp[1]);
                                        tEVO.setEjercicio(ejercicio);
                                        tEVO.setNumeroExpediente(numExpediente);
                                        Long codTramite =meLanbide61DAO.obtenerCodigoInternoTramite(codOrg, "REPLE","499", con);
                                        log.debug("CÓDIGO INTERNO trámite 499:"+codTramite);
                                        tEVO.setCodTramite(codTramite.toString());
                                        tEVO.setOcurrenciaTramite("1");
                                         
                                        //Vector listaTramitesIniciar = listaTramitesIniciar(listaCodTramites, listaModoTramites, listaUtrTramites,listaTramSigNoCumplenCondEntrada,false);
                                        Vector listaTramitesIniciar =new Vector();
                                        TramitacionExpedientesValueObject traExp = new TramitacionExpedientesValueObject();
                                        traExp.setCodTramite(codTramite.toString());
                                        traExp.setOcurrenciaTramite("1");
                                        listaTramitesIniciar.add(traExp);
                                        
                                        if (log.isDebugEnabled()) log.debug("el tamanyo de la lista para finalizar es : "
                                                + listaTramitesIniciar.size());
                                        tEVO.setListaTramitesIniciar(listaTramitesIniciar);
                                                                                                                                                                
                                        //iniciarTramitesManual(tEVO, params);
                                        Vector devolver =TramitacionExpedientesManager.getInstance().iniciarTramitesManual(tEVO, params);
                                        log.debug("DEVOLVER TAMAÑO INICIARTRAMITE:"+devolver.size());
                                        log.debug("DEVOLVER INICIAR TRAMITE:"+devolver);*/
                                        log.debug("obtenerCodigoInternoTramite("+codOrg+",REPLE,499,"+con);
                                        Long codTramite =meLanbide61DAO.obtenerCodigoInternoTramite(codOrg, "REPLE","499",  con);
                                        
                                        String unidad =meLanbide61DAO.obtenerUnidadExpediente(numExpediente, con);
                                        log.debug("UNIDAD TRAMITADORA:"+unidad);
                                        meLanbide61DAO.crearTramite(codOrg,numExpediente, codTramite, unidad,con);
                                        
                                        //CREAMOS TRÁMITE EN MIS GESTIONES
                                        Lan6Tramite   lan6Tramite = new Lan6Tramite(); 
                                        lan6Tramite.setId(Lan6Constantes.TRAMITE_ESPERA_REQUERIMIENTO_APORTACION);
                                        lan6Tramite.setDescripcion(Lan6Constantes.TRAMITE_ESPERA_REQUERIMIENTO_APORTACION_DESC);
                                        lan6Tramite.setTipoTramite(Lan6Constantes.TRAMITE_ESPERA);

//                                        lan6Tramite.setInstanciaId(ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_INSTANCIA_PLANTILLA_LAN62_REPLE, ConstantesMeLanbide43.FICHERO_ADPATADORES));                            
                                        lan6Tramite.setInstanciaId(gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties(ConstantesMeLanbide43.ID_INSTANCIA_PLANTILLA_LAN62_REPLE)); // 2.2

                                        //Datos del expediente
                                        lan6Tramite.setEjercicio(exp[0]);
                                        lan6Tramite.setIdExpediente(numExpediente);                     
                                        lan6Tramite.setFechaActualizacion(hoy);

                                        ArrayList<Lan6Tramite> tramites = new ArrayList<Lan6Tramite>();
                                        tramites.add(lan6Tramite);     

                                        //Llamada método     
                                        log.error("Antes de llamar a actualizarTramites");
                                        String res = servicios.actualizarTramites(tramites);
                                    }
                                }  
                            }
                            
                        }
                        
                        //UAAP        
                        Calendar d1enero = Calendar.getInstance();
                        d1enero.set(hoy.get(Calendar.YEAR), Calendar.JANUARY, 1, 0, 0);
                        //d1enero.set(hoy.get(Calendar.YEAR), Calendar.JUNE, 22, 0,0);
                        Calendar d1enerofin = Calendar.getInstance();
                        d1enerofin.set(hoy.get(Calendar.YEAR), Calendar.JANUARY, 1, 23, 59);
                        //d1enerofin.set(hoy.get(Calendar.YEAR), Calendar.JUNE, 22, 23,59);

                        log.debug("hoy: " + sdf.format(hoy.getTime()));
                        log.debug("d1enero: " + sdf.format(d1enero.getTime()));
                        log.debug("d1enerofin: " + sdf.format(d1enero.getTime()));
                        if (hoy.compareTo(d1enero) >= 0 && hoy.compareTo(d1enerofin) < 0) {
                            log.debug("lanzar batch abrir esperas UAAP ");
                            List<ExpedienteUAAP> expedientesUAAP = meLanbide61DAO.getExpedientesTelematicosUAAP(con);
                            log.debug("ExpedientesTelematicosUAAP: " + expedientesUAAP.size());
                            for (Iterator<ExpedienteUAAP> i = expedientesUAAP.iterator(); i.hasNext();) {
                                ExpedienteUAAP item = i.next();
                                numExpediente = item.getNumExpediente();
                                log.debug("numExpediente: " + numExpediente + ", fecha registro: " + item.getFecRegistro() + ", anyo registro: " + item.getAnyo());

                                String[] exp = numExpediente.split("/");
//                                String idProcedimiento = convierteProcedimiento(exp[1]);
                                Lan6InformarConsultaServicios servicios = new Lan6InformarConsultaServicios(exp[1]); // 2.2

                                //pendiente comprobar antes si ya existe el tramite o no
                                boolean existeTram = meLanbide61DAO.existeTramite(numExpediente, "12", con);
                                if (!existeTram) {
                                    log.debug("No existe el trámite 12. Hacer apertura");

                                    log.debug("obtenerCodigoInternoTramite(" + codOrg + ",UAAP,12," + con);
                                    Long codTramite =meLanbide61DAO.obtenerCodigoInternoTramite(codOrg, "UAAP","12",  con);

                                    String unidad =meLanbide61DAO.obtenerUnidadExpediente(numExpediente, con);
                                    log.debug("UNIDAD TRAMITADORA:"+unidad);
                                    meLanbide61DAO.crearTramite(codOrg,numExpediente, codTramite, unidad,con);

                                     //CREAMOS TRÁMITE EN MIS GESTIONES
                                     Lan6Tramite lan6Tramite = new Lan6Tramite();
                                     lan6Tramite.setId(Lan6Constantes.TRAMITE_ESPERA_REQUERIMIENTO_APORTACION);
                                     lan6Tramite.setDescripcion(Lan6Constantes.TRAMITE_ESPERA_REQUERIMIENTO_APORTACION_DESC);
                                     lan6Tramite.setTipoTramite(Lan6Constantes.TRAMITE_ESPERA);

//                                    lan6Tramite.setInstanciaId(ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_INSTANCIA_DOC_LIQ_LAN62_UAAP, ConstantesMeLanbide43.FICHERO_ADPATADORES));
                                     lan6Tramite.setInstanciaId(gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties(ConstantesMeLanbide43.ID_INSTANCIA_DOC_LIQ_LAN62_UAAP)); // 2.2

                                     //Datos del expediente                                
                                     lan6Tramite.setEjercicio(exp[0]);
                                     lan6Tramite.setIdExpediente(numExpediente);                     
                                    lan6Tramite.setFechaActualizacion(hoy);

                                    ArrayList<Lan6Tramite> tramites = new ArrayList<Lan6Tramite>();
                                    tramites.add(lan6Tramite);
                                    log.error("Antes de llamar a actualizarTramites");
                                    String res = servicios.actualizarTramites(tramites);
                                 }
                            }
                        }
                                           
                        if(dosEntornos) codOrg++;
                        else codOrg = 2;
                 
                    }
                 }
                 catch(TramitacionException te){
                     log.error("Error en el job de apertura. Tramitacion:",te);
                 }
                 catch(Exception e)
                 {
                     log.error("Error en el job de apertura automática de trámites: ", e);
                     e.printStackTrace();

                      try{
                          int intentos = numIntentos + 1;
                          //meLanbide34DAO.getInstance().actualizarError(id, intentos, e.toString(), con);
                      }catch(Exception i){
                          log.error("Error en la funciï¿½n actualizarError: " + i.getMessage());

                      }

                     try {
                         throw e;
                     } catch (Exception ex) {
                         java.util.logging.Logger.getLogger(AperturaAutomaticaTramitesJob.class.getName()).log(Level.SEVERE, null, ex);
                     }

                 }
                 finally{
                     if(con != null)
                         try {
                             con.close();
                     } catch (SQLException ex) {
                         java.util.logging.Logger.getLogger(AperturaAutomaticaTramitesJob.class.getName()).log(Level.SEVERE, null, ex);
                     }
                 }
            }//para local quitar
            }
       }
       catch(Exception ex)
       {
           log.error("Error: " +ex);
       }
   }
   
   /*public Vector iniciarTramitesManual(TramitacionExpedientesValueObject tEVO, String[] params)
  //throws TramitacionException
  {
    // Sin condicion de salida.
    Vector devolver = new Vector();
    log.debug("iniciarTramiteManual");
    try {
      log.info("Usando persistencia manual");
      devolver = TramitacionExpedientesDAO.getInstance().iniciarTramitesManual(tEVO,params);
      log.debug("iniciarTramiteManual "+devolver.size()+" devolver:"+devolver);
    } catch (TramitacionException te) {
      devolver = null;
      te.printStackTrace();
      log.error("JDBC Technical problem " + te.getMessage());
      //throw new TramitacionException("Problema técnico de JDBC " + te.getMessage());
    } catch(Exception e) {
      devolver = null;
      e.printStackTrace();
      //throw new TramitacionException("Problema técnico de JDBC " + e.getMessage());
    } finally {
      log.debug("iniciarTramiteManual");
      return devolver;
    }
  }*/
   

    protected static Config conf =ConfigServiceHelper.getConfig("notificaciones");    
    
    private Logger log = LogManager.getLogger(AperturaAutomaticaTramitesJob.class);

    private String codOrganizacion;
   

    public String getCodOrganizacion() {
        return codOrganizacion;
    }

    public void setCodOrganizacion(String codOrganizacion) {
        this.codOrganizacion = codOrganizacion;
    }

   /*
    private String[] getTituloExtension(String fichero, String contentType, boolean esDocumentoTramite){
        log.error("Titulo " + fichero + " , tipo " + contentType);
        String[] res = new String[2];
        res[0] = "";
        res[1] = "";
        
        if(esDocumentoTramite){
            // ES DOCUMENTO DEL TRAMITE, NO TIENE EXTENSION
            //res[0] = sustituirCaracteresEspeciales(fichero);
            res[0] = fichero;
            res[1] = "pdf";
        }else{
            // ES DOCUMENTO EXTERNO ADJUNTO, SACAMOS LA EXTENSION DEL contentType
            int dotSlash = contentType != null ? contentType.lastIndexOf("/") : -1;
            if(dotSlash == -1){
                // EL contentType NO TIENE NINGUNA BARRA
                res[0] = fichero;
                res[1] = contentType;
            }else{
                // EL contentType TIENE BARRA
                res[0] = fichero;
                res[1] = contentType.substring(dotSlash + 1);
            }
        }
        
        return res;
    }
    
    private String sustituirCaracteresEspeciales(String input){
        String s = input.replaceAll(" ", "");
        s = s.replaceAll("ï¿½", "a");
        s = s.replaceAll("ï¿½", "e");
        s = s.replaceAll("ï¿½", "i");
        s = s.replaceAll("ï¿½", "o");
        s = s.replaceAll("ï¿½", "u");
        s = s.replaceAll("ï¿½", "A");
        s = s.replaceAll("ï¿½", "E");
        s = s.replaceAll("ï¿½", "I");
        s = s.replaceAll("ï¿½", "O");
        s = s.replaceAll("ï¿½", "U");
        
        return s;
    }
    
    private String getExtensionFichero(String contentType){
        int slashIndex = contentType != null ? contentType.lastIndexOf("/") : -1;
        if(slashIndex == -1){
            return contentType;
        }else{
            return contentType.substring(slashIndex + 1);
        }
    }
    
     private void sendMail(String nombreTramite, String emailUtr,String numExpediente,String fecha) {

        log.debug(">>>>>>>destinatario del mail: " + emailUtr);
        if (emailUtr != null) {            
            // Se recupera el asunto y mensaje del correo
            
            int idiomaUsuario=1;
            MeLanbide61I18n meLanbide61I18n = MeLanbide61I18n.getInstance();
            String asunto =ConfigurationParameter.getParameter(ConstantesMeLanbide61.ENTORNO,ConstantesMeLanbide61.FICHERO_PROPIEDADES)+" - "+meLanbide61I18n.getMensaje(idiomaUsuario, "email.asunto").replaceAll("@expediente@", numExpediente);
            String mensaje =meLanbide61I18n.getMensaje(idiomaUsuario, "email.contenido").replaceAll("@nombre@",nombreTramite ).replaceAll("@fechaMaxima@", fecha);
            
            
            log.debug("Mensaje de notificaciÃ³n: " + mensaje);
            log.debug("Asunto del mail: " + asunto);

            MailHelper mailer = new MailHelper();
            try {
                mailer.sendMail(emailUtr, asunto, mensaje);
            } catch (Exception e) {
                // Si ocurre algÃºn error durante el envÃ­o del correo
                e.printStackTrace();
                log.error("Error al enviar e-mail (CEI) con notificaciÃ³n de fin de plazo " + nombreTramite + ": " + e.getMessage());
            }
        } 
    }*/
    
    private AdaptadorSQLBD getAdaptSQLBD(String codOrganizacion) throws SQLException{
        if(log.isDebugEnabled()) log.error("getConnection ( codOrganizacion = " + codOrganizacion + " ) : BEGIN");
        ResourceBundle config = ResourceBundle.getBundle("techserver");
        String gestor = config.getString("CON.gestor");
        String jndiGenerico = config.getString("CON.jndi");
        Connection conGenerico = null;
        Statement st = null;
        ResultSet rs = null;
        String[] salida = null;
        Connection con = null;
        
        if(log.isDebugEnabled()){
            log.error("getJndi =========> ");
            log.error("parametro codOrganizacion: " + codOrganizacion);
            log.error("gestor: " + gestor);
            log.error("jndi: " + jndiGenerico);
        }//if(log.isDebugEnabled())

        DataSource ds = null;
        AdaptadorSQLBD adapt = null;
        synchronized (this) {
            try{
                PortableContext pc = PortableContext.getInstance();
                if(log.isDebugEnabled())log.error("He cogido el jndi: " + jndiGenerico);
                ds = (DataSource)pc.lookup(jndiGenerico, DataSource.class);
                // Conexiï¿½n al esquema genï¿½rico
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
            if(log.isDebugEnabled()) log.error("getConnection() : END");
         }// synchronized
        return adapt;
     }//getConnection
    
}
