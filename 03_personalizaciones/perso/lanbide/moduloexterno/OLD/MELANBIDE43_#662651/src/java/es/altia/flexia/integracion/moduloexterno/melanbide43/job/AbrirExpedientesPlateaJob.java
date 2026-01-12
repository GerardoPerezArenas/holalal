/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.flexia.integracion.moduloexterno.melanbide43.job;

import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.flexia.integracion.moduloexterno.melanbide43.dao.MeLanbide43DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide43.manager.MeLanbide43Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide43.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide43.util.ConstantesMeLanbide43;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.ExpTram;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.Participantes;
import es.altia.technical.PortableContext;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;

public class AbrirExpedientesPlateaJob implements Job {
    
   @Override
   public void execute(JobExecutionContext jec) throws JobExecutionException 
   {
       try
       {
            int pepe = jec.getRefireCount();
            Job trab = jec.getJobInstance();

            log.error("jec.getRefireCount(): " + pepe);
            Trigger pepito = jec.getTrigger();
            
            String servidor = ConfigurationParameter.getParameter(ConstantesMeLanbide43.CAMPO_SERVIDOR, ConstantesMeLanbide43.FICHERO_PROPIEDADES);
            
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
                    int codOrg = Integer.parseInt(ConfigurationParameter.getParameter("COD_ORG",ConstantesMeLanbide43.FICHERO_PROPIEDADES));
                    //int codOrg = 0;      //pruebas
                    //int codOrg = 1;      //real
                    boolean dosEntornos = Boolean.getBoolean(ConfigurationParameter.getParameter("DOS_ENTORNOS", 
                    ConstantesMeLanbide43.FICHERO_PROPIEDADES));
                  
                    while(codOrg < 2){
                        AdaptadorSQLBD adaptador= this.getAdaptSQLBD(String.valueOf(codOrg));
                        con = adaptador.getConnection();
                        log.error("en el while de tokens codOrg: " + codOrg);

                        /* #263085
                         * Existen una serie de expedientes en regexlan. Son expedientes de orientación en estado “espera de justificación” que hay que migrar a platea (proceso bach).
                         * Esperas en regexlan.Para que se pueda aportar documentación a un trámite de Regexlan, es necesario que en el panel del expediente en Mis Gestiones, aparezca el botón de Aportación de Documentación.
                         * Este botón (Apertura de espera de Aportación) lo debe añadir Regexlan. En el momento en el que se abre el trámite de Espera de Documentación en Regexlan, se lanza un proceso que a través de los Adaptadores de PLATEA genera el botón.
                         * Se debe añadir la llamada a ese proceso, en la configuración de la apertura del trámite.                         *                    
                        */
                        MeLanbide43DAO meLanbide43DAO = MeLanbide43DAO.getInstance();
                        //ORI14
                        //String codTramiteExternoEspera = "6";//PERIODO ESPERA PARA RECIBIR DOCUMENTACION JUSTIFICATIVA 2 PAGO (ori14,colec,cemp)
                        //String codProc="ORI14";
                        List<String> listaProc=new ArrayList<String>();
                        /*listaProc.add("ORI14");
                        listaProc.add("COLEC");
                        listaProc.add("CEMP");
                        
                        /////////////
                        //listaProc.add("CUOTS");
                        //listaProc.add("QUEJA");
                        //listaProc.add("CONCM");
                        //listaProc.add("AACC");
                        //listaProc.add("LEI");
                        //listaProc.add("RGEF");
                        //listaProc.add("RGCF");
                        listaProc.add("RGCFM");
                        //listaProc.add("CEI");
                        //listaProc.add("SEI");
                        //listaProc.add("SUBAF");
                        listaProc.add("SUENC");*/
                        
                        // #372565
                        listaProc.add("SEI");
                        
                        //for(Iterator<String> p = listaProc.iterator(); p.hasNext(); ) {
                        // String codProc = p.next();
                        for (String codProc : listaProc)  {
                            log.debug("Procedimiento: "+codProc);
                            //se abren todos los expedientes en Platea
                            List<ExpTram> expedientes = meLanbide43DAO.getExpedientesPendientesPlatea(con,codProc);
                            log.debug("numexpedientes:"+expedientes.size());

                            for(Iterator<ExpTram> i = expedientes.iterator(); i.hasNext(); ) {
                                ExpTram item = i.next();
                                numExpediente = item.getNumExpediente();
                                log.debug("numexpediente:"+numExpediente);
                                String[] datos = numExpediente.split("/");
                                //LAMAR A MIS GESTIONES INICIO                            
                                MeLanbide43Manager meLanbide43Manager = MeLanbide43Manager.getInstance();                            
                                //Date date = new Date();
                                //DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                //String fechaIni = meLanbide43Manager.fechaInicioExpediente(numExpediente, adaptador);                
                               // date = dateFormat.parse(fechaIni);
                                
                               // String fecha = meLanbide43Manager.verificarFecha(datos[1]/*codProc*/, adaptador);
                                Participantes par = meLanbide43Manager.obtenerDatosParticipantes(numExpediente,adaptador);
                                log.debug("PARTICIPANTES: "+par); 
                                //log.debug("fecha: "+fecha); 
                                log.error("tipo id: " + par.getTipoID());
                                if(par.getTipoID() != 0){
                                   // Date fec = null;
                                   // if(fecha != ""){
                                    //    fec = dateFormat.parse(fecha);
                                      //  if(date.after(fec)){
                                        //    log.debug(date+" after "+fec);
                                            //evento="I"
                                            
                                            //crear expediente platea en trámite de inicio
                                            //Long codTramite= meLanbide43DAO.obtenerCodigoTramiteFase (codProc, "1", con); 
                                            //Long codTramiteInicio =meLanbide43DAO.obtenerCodigoInternoTramite(codOrg, codProc, "1", con); 
                                            //Long codTramiteInicio =meLanbide43DAO.obtenerCodigoInternoTramite(codOrg, codProc, codTramite.toString(), con); 
                                            Long codTramiteInicio =meLanbide43DAO.obtenerCodigoTramiteFase (codProc, "1", con); 
                                            log.error("codTramiteInicio: " + codTramiteInicio);
                                            //Long codTramiteEspera =meLanbide43DAO.obtenerCodigoInternoTramite(codOrg, codProc, codTramiteExternoEspera, con);     
                                            //Long codTramite =meLanbide43DAO.obtenerCodigoInternoTramite(codOrg, codProc, codTramiteExterno, con);     
                                                String idGest = MeLanbide43Manager.getInstance().guardarGestiones(numExpediente, codOrg, adaptador, codTramiteInicio.intValue(), "I");
                                                log.error("Id generado: " + idGest);
                                                /*boolean abrirTramEspera=false;
                                                if (codTramiteEspera.equals(new Long(item.getCodTramite()))){
                                                    abrirTramEspera=true;
                                                    log.debug("En trámite espera de justificación ("+item.getCodTramite()+")" );
                                                }else
                                                    log.debug("NO En trámite espera de justificación ("+item.getCodTramite()+")" );*/
                                                //iniciar expediente
                                                String codigoOperacion = "";
                                                codigoOperacion = MeLanbide43Manager.getInstance().avanzarGestiones(idGest, String.valueOf(codOrg),adaptador);                                       
                                                //abrir trámite espera de justificación
                                                /*log.debug("codTramiteEspera: "+codTramiteEspera );
                                                log.debug("item.getCodTramite(): "+item.getCodTramite() );
                                                if (abrirTramEspera && ("ORI14".equals(codProc) || "COLEC".equals(codProc) || "CEMP".equals(codProc) || "ORI".equals(codProc) )){//solo en ori14,colec y cemp
                                                    log.debug("Abrir trámite espera de justificación " );
                                                    MeLanbide43Manager.getInstance().justificantesPagos(codOrg, codTramiteEspera.intValue(), numExpediente, adaptador);
                                       //         }
                                    //    }
                                    }*/
                                }

                            }       
                        }
                        if(dosEntornos) codOrg++;
                        else codOrg = 2;  
                        if(con != null){                           
                           con.close();                           
                        }
                        
                    }
                 }
                 catch(Exception e)
                 {
                     log.error("Error en el job de AbrirExpedientesPlateaJob: ", e);
                         
                     /*   MeLanbide43Manager.borrarProcesado(idGest, getAdaptSQLBD(String.valueOf(codOrganizacion)));
            
                        //insertar error en registro de errores           
                        log.error("Error en la función generarMisGestiones: ", e);
                        String error = "Error en la job generarMisGestiones: " + e.getMessage()!=null?e.getMessage().toString():"null";

                        ErrorBean errorB = new ErrorBean();
                        errorB.setIdError("MISGEST_012");
                        errorB.setMensajeError("Error al iniciar un expediente en la función AbrirExpedientesPlateaJob");
                        errorB.setSituacion("generarMisGestiones");

                        MeLanbide43Manager.grabarError(errorB, e.getMessage()!=null?e.getMessage().toString():"null", e.toString(), numExpediente);
                         //fin 
                        */
           
                      try{
                          int intentos = numIntentos + 1;
                          //meLanbide34DAO.getInstance().actualizarError(id, intentos, e.toString(), con);
                      }catch(Exception i){
                          log.error("Error en la funci�n actualizarError: " + i.getMessage());

                      }

                     try {
                         throw e;
                     } catch (Exception ex) {
                         java.util.logging.Logger.getLogger(AbrirExpedientesPlateaJob.class.getName()).log(Level.SEVERE, null, ex);
                     }

                 }
                 finally{
                     if(con != null){
                         try {
                                con.close();
                        } catch (SQLException ex) {
                            java.util.logging.Logger.getLogger(AbrirExpedientesPlateaJob.class.getName()).log(Level.SEVERE, null, ex);
                        }
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
   
  

    protected static Config conf =ConfigServiceHelper.getConfig("notificaciones");    
    
    private Logger log = Logger.getLogger(AbrirExpedientesPlateaJob.class);

    private String codOrganizacion;
   

    public String getCodOrganizacion() {
        return codOrganizacion;
    }

    public void setCodOrganizacion(String codOrganizacion) {
        this.codOrganizacion = codOrganizacion;
    }

   
    
    private AdaptadorSQLBD getAdaptSQLBD(String codOrganizacion){
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
                // Conexi�n al esquema gen�rico
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
            if(log.isDebugEnabled()) log.error("getConnection() : END");
         }// synchronized
        return adapt;
     }//getConnection
    
   
    
    
}
