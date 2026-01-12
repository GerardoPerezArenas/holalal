
package es.altia.flexia.integracion.moduloexterno.melanbide72.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.lanbide.lan6.errores.bean.ErrorBean;
import es.altia.agora.business.sge.persistence.manual.OperacionesExpedienteDAO;
import es.altia.agora.business.sge.persistence.manual.TramitesExpedienteDAO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.flexia.integracion.moduloexterno.melanbide43.manager.MeLanbide43Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide72.dao.MeLanbide72DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide72.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide72.util.ConstantesMeLanbide72;
import es.altia.flexia.integracion.moduloexterno.melanbide72.vo.RegistroBatchVO;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javax.sql.DataSource;
import es.altia.technical.PortableContext;
import es.lanbide.lan6.adaptadoresPlatea.excepciones.Lan6Excepcion;
import es.lanbide.lan6.adaptadoresPlatea.excepciones.Lan6InformarConsultaExcepcion;


/**
 *
 * @author alexandrep
 */
public class InicioAutomaticoTramiteRecepcionyRegistroEntradaDECEX implements Job {
    
    protected static Config conf = ConfigServiceHelper.getConfig("notificaciones");
    private final Logger log = LogManager.getLogger(InicioAutomaticoTramiteRecepcionyRegistroEntradaDECEX.class);
    
    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        
        Connection con = null;
        try {
            int pepe = jec.getRefireCount();

            log.info("jec.getRefireCount(): " + pepe);

            String servidor = ConfigurationParameter.getParameter(ConstantesMeLanbide72.CAMPO_SERVIDOR, ConstantesMeLanbide72.FICHERO_PROPIEDADES);
            log.info("servidor: " + servidor);
           
            //TODO recordar quitar al subir a DESA PRE ASI no tengo que quitar el if de debajo para local quitar
            //System.setProperty("weblogic.Name",ConfigurationParameter.getParameter(ConstantesMeLanbide72.CAMPO_SERVIDOR, ConstantesMeLanbide72.FICHERO_PROPIEDADES));
            if (servidor.equals(System.getProperty("weblogic.Name"))) {//PARA LOCAL QUITAR
                synchronized (jec) {
                     AdaptadorSQLBD adaptador = null;

                    
                    
                    try{
                        
                        log.info("Execute lanzado " + System.getProperty("weblogic.Name"));
                        
                        //Recogemos los codigos del properties: 
                        setCodOrganizacion(ConfigurationParameter.getParameter(ConstantesMeLanbide72.COD_ORG, ConstantesMeLanbide72.FICHERO_PROPIEDADES));
                        setCodOrg(Integer.parseInt(codOrganizacion));
                        setCodProcedimiento(ConfigurationParameter.getParameter(ConstantesMeLanbide72.COD_PROCEDIMIENTO, ConstantesMeLanbide72.FICHERO_PROPIEDADES));
                      
                        log.info("Organizacion: " + codOrganizacion);
                        
                        try {
                                        adaptador = this.getAdaptSQLBD(codOrganizacion);
                                    } catch (Exception ex) {
                                        log.error("Error al recuperar el adaptador getAdaptSQLBD ", ex);
                                    }
                    
                        
                      
                        
                     //   setCodTramInicio(Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide72.INICIO_EXPEDIENTE, ConstantesMeLanbide72.FICHERO_PROPIEDADES))) 
                     //   setCodTramRevision(Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide72.REVISION_DOCUMENTACION, ConstantesMeLanbide72.FICHERO_PROPIEDADES))) 

                     
                      //Buscar los registros pendientes
                       MeLanbide72DAO meLanbide72DAO = MeLanbide72DAO.getInstance();
                       //Tienen que ser los de 3 mese, año 1, año2 y año3 que no esten ya y que no tengan marcada la documentacion antregada
                       
                      
                       
                    
                       
                        while (codOrg < 2) {
                            con = this.getAdaptSQLBD(codOrganizacion).getConnection();
                            log.info("en el while de tokens codOrg: " + codOrg);
                       
                            
                             //Empezamos con el de 3 meses, tiene que tener el 4 - ACUSE RECIBO NOTIFICACION RESOLUCION realizado y haber pasado 3 meses
                             // y no estar creado ya.
                             List<RegistroBatchVO> registros = meLanbide72DAO.getListaRegistrosDECEX3Meses(con);
                             log.debug("Registros DECEX 3 meses: " + registros.size());
                            
                            if (!registros.isEmpty()) {
                               
                            for (RegistroBatchVO registro : registros) {
                                
                            //Codigo del tramite a iniciar
                            String codigoTramiteIniciar = ConfigurationParameter.getParameter(ConstantesMeLanbide72.TRAM_DECEX_RECEP_REG_ENT_3M, ConstantesMeLanbide72.FICHERO_PROPIEDADES);

                            try {
                            //Iniciamos el tramite de los 3 Meses
                            if(iniciarTramite(codigoTramiteIniciar, registro)){
                                //Todo ha ido bien asi que abrimos la espera 
                                //Inicia la espera                                  
                                    try{
                                            log.info("START - En funcion para iniciar la espera: " + registro.getNumExpediente());

                                            MeLanbide43Manager.getInstance().justificantesPagos (codOrg,Integer.valueOf(codigoTramiteIniciar), registro.getNumExpediente(), adaptador);

                                            if(log.isDebugEnabled()) log.info("En funcion para iniciar la espera: END");
                                        }catch(Lan6InformarConsultaExcepcion ice){

                                            ArrayList<String> codes = ice.getCodes();
                                            ArrayList<String> messages = ice.getMessages();


                                            // StackTraceElement[] stacktrace = ice getStackTrace()
                                             String error="";
                                             String causa;//ice getCausaExcepcion() 
                                                causa = ice.getCausaExcepcion();
                                             for (int i=0; i<codes.size();i++){
                                                 error += messages.get(i);
                                                 //error += codes get(i) 
                                             }
                                            ErrorBean errorB = new ErrorBean();
                                            errorB.setIdError("MISGEST_015");
                                            errorB.setMensajeError("Error Lan6InformarConsultaExcepcion al actualizar tramite");
                                            errorB.setSituacion("justificantesPagos");

                                            MeLanbide43Manager.grabarError(errorB, error, causa,  registro.getNumExpediente());
                                            throw ice;
                                        }catch(Lan6Excepcion ex){            
                                            throw ex;
                                        }     
                                        catch(Exception ex)
                                        {            
                                            throw ex;
                                        }
                            
                                
                            } else {
                                log.error("No se ha podido iniciar el tramite de los tres meses para el expediente"+ registro.getNumExpediente());
                            }
                            }  catch(Exception ex)
                                        {   
                                             log.error("Error al intentar iniciar el tramite de los tres meses para el expediente"+ registro.getNumExpediente()+ex.getMessage());
                                            //throw ex 
                                        }
                            }} else { //  if !registros.isEmpty() 
                                log.error("No existen Expedientes de tres meses sin notificar");
                            }
                            int x =0;
                            //Continuamos con los anuales
                            //los ponemos en un bucle ya que seran todos iguales
                            while (x<3)
                            {
                                //avanzamos el año
                                x++;
                                
                             List<RegistroBatchVO> registrosAnuales = meLanbide72DAO.getListaRegistrosDECEXAños(con,x);
                             log.debug("Registros DECEX "+x+" año: " + registrosAnuales.size());
                            
                            if (!registrosAnuales.isEmpty()) {
                               
                            for (RegistroBatchVO registro : registrosAnuales) {
                                
                            //Codigo del tramite a iniciar
                             String codigoTramiteIniciar ="";
                             if (x==1){
                            codigoTramiteIniciar = ConfigurationParameter.getParameter(ConstantesMeLanbide72.TRAM_DECEX_RECEP_REG_ENT_A1, ConstantesMeLanbide72.FICHERO_PROPIEDADES);
                             }
                               if (x==2){
                            codigoTramiteIniciar = ConfigurationParameter.getParameter(ConstantesMeLanbide72.TRAM_DECEX_RECEP_REG_ENT_A2, ConstantesMeLanbide72.FICHERO_PROPIEDADES);
                             }
                               
                                if (x==3){
                            codigoTramiteIniciar = ConfigurationParameter.getParameter(ConstantesMeLanbide72.TRAM_DECEX_RECEP_REG_ENT_A3, ConstantesMeLanbide72.FICHERO_PROPIEDADES);
                             }
                             
                                
                                try {
                            //Iniciamos los tramites
                            log.info("Init Funcion para iniciar el tramite del año "+x+": " + registro.getNumExpediente());
                            if(iniciarTramite(codigoTramiteIniciar, registro)){
                                //Todo ha ido bien asi que abrimos la espera 
                                //Inicia la espera                                  
                                    try{
                                            log.info("En funcion para iniciar la espera: " + registro.getNumExpediente());

                                            MeLanbide43Manager.getInstance().justificantesPagos (codOrg, Integer.valueOf(codigoTramiteIniciar), registro.getNumExpediente(), adaptador);

                                            if(log.isDebugEnabled()) log.info("justificantesPagos() : END");
                                        }catch(Lan6InformarConsultaExcepcion ice){

                                            ArrayList<String> codes = ice.getCodes();
                                            ArrayList<String> messages = ice.getMessages();


                                            // StackTraceElement[] stacktrace = ice getStackTrace()
                                             String error="";
                                             String causa;//ice getCausaExcepcion() 
                                                causa = ice.getCausaExcepcion();
                                             for (int i=0; i<codes.size();i++){
                                                 error += messages.get(i);
                                                 //error += codes get(i) 
                                             }
                                            ErrorBean errorB = new ErrorBean();
                                            errorB.setIdError("MISGEST_015");
                                            errorB.setMensajeError("Error Lan6InformarConsultaExcepcion al actualizar tramite");
                                            errorB.setSituacion("justificantesPagos");

                                            MeLanbide43Manager.grabarError(errorB, error, causa,  registro.getNumExpediente());
                                            throw ice;
                                        }catch(Lan6Excepcion ex){            
                                            throw ex;
                                        }     
                                        catch(Exception ex)
                                        {            
                                            throw ex;
                                        }
                            
                                
                            } else {
                                log.error("No se ha podido iniciar el tramite del año "+x+" para el expediente"+ registro.getNumExpediente());
                            }
                            }  catch(Exception ex)
                                        {   
                                             log.error("Error al intentar iniciar el tramite del año "+x+" para el expediente"+ registro.getNumExpediente()+ex.getMessage());
                                            //throw ex 
                                        }
                            log.info("Fin Funcion para iniciar el tramite del año "+x+": " + registro.getNumExpediente());
                            }} else { //  if !registros.isEmpty() 
                                log.error("No existen Expedientes del año "+x+" sin notificar");
                            } 
                                
                             log.info("Fin iniciar el tramite del año "+x);
                                
                            } //While anual 
                            
                            
                         codOrg++; 
                           if (con != null) {
                                 con.close();
                           }
                        }//while codOrg
                        
                    } catch (Exception e) {
                        log.error("Error en el job de Inicio Automatico Tramite Recepcion y Registro Entrada DECEX: ", e);
                        //e printStackTrace() 

                        try {
                        } catch (Exception i) {
                            log.error("Error en la función actualizarError: " + i.getMessage());
                        }

                        try {
                            throw e;
                        } catch (Exception ex) {
                            java.util.logging.Logger.getLogger(InicioAutomaticoTramiteRecepcionyRegistroEntradaDECEX.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    } finally {
                        if (con != null) {
                            try {
                                con.close();
                            } catch (SQLException ex) {
                            }
                        }
                    }
                    
                    
       
         }//synchronized
            }//para local quitar 
            else {
                 log.error("Servidor desconocido.");
            }
        } catch (Exception ex) {
            log.error("Error: " + ex);
        }
        }
    
    
    private AdaptadorSQLBD getAdaptSQLBD(String codOrganizacion) throws SQLException {
        if (log.isDebugEnabled()) {
            log.info("getConnection ( codOrganizacion = " + codOrganizacion + " ) : BEGIN");
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

    
       private String codOrganizacion;
    private int codOrg;
    private String codProcedimiento;

    private int codTramInicio;
    private int codTramRevision;

    public String getCodOrganizacion() {
        return codOrganizacion;
    }

    public void setCodOrganizacion(String codOrganizacion) {
        this.codOrganizacion = codOrganizacion;
    }

    public int getCodOrg() {
        return codOrg;
    }

    public void setCodOrg(int codOrg) {
        this.codOrg = codOrg;
    }

    public String getCodProcedimiento() {
        return codProcedimiento;
    }

    public void setCodProcedimiento(String codProcedimiento) {
        this.codProcedimiento = codProcedimiento;
    }

 

    public int getCodTramInicio() {
        return codTramInicio;
    }

    public void setCodTramInicio(int codTramInicio) {
        this.codTramInicio = codTramInicio;
    }

    public int getCodTramRevision() {
        return codTramRevision;
    }

    public void setCodTramRevision(int codTramRevision) {
        this.codTramRevision = codTramRevision;
    }

    private Boolean iniciarTramite(String codigoTramiteIniciar, RegistroBatchVO registroIniciarTramite) {

        AdaptadorSQLBD adaptador = null;
        try {
            adaptador = this.getAdaptSQLBD(codOrganizacion);
        } catch (Exception ex) {
            log.error("Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }
        Connection con = null;
        try {
            con = adaptador.getConnection();
                    
                    log.info("------------Inicio Tramite----------");
                    log.info("Valores con los que iniciamos el tramite");
                    log.info("Codigo organizacion:"+codOrganizacion);
                    log.info("Codigo Procedimiente:"+codProcedimiento);
                    log.info("Ejercicio:"+String.valueOf(registroIniciarTramite.getEjerRegistro()));
                    log.info("Numero:"+registroIniciarTramite.getNumExpediente());
                    log.info("usuario:"+String.valueOf(registroIniciarTramite.getUsuario()));
                    log.info("codTramite:"+String.valueOf(codTramRevision));
                    
                    // Inicia tramite
                    GeneralValueObject tramiteIni = new GeneralValueObject();
                    tramiteIni.setAtributo("codMunicipio", codOrganizacion);
                     tramiteIni.setAtributo("codOrganizacion", codOrganizacion);
                     
                    
                    tramiteIni.setAtributo("codProcedimiento", codProcedimiento);
                    tramiteIni.setAtributo("ejercicio", String.valueOf(registroIniciarTramite.getEjerRegistro()));
                    tramiteIni.setAtributo("numero", registroIniciarTramite.getNumExpediente());
                    tramiteIni.setAtributo("usuario", String.valueOf(registroIniciarTramite.getUsuario()));
                    tramiteIni.setAtributo("nombreUsuario", "ADMINISTRADOR");
                  //  tramiteIni.setAtributo("codTramite", String.valueOf(codTramRevision)) 
                    //tramiteIni.setAtributo("nomTramite", nomTramRevision) 
                    tramiteIni.setAtributo("ocurrTramite", String.valueOf(codigoTramiteIniciar));
                    tramiteIni.setAtributo("codTramite", String.valueOf(codigoTramiteIniciar));
                    //tramiteIni.setAtributo("fechaInicioTramite", fechaFin) 
                    tramiteIni.setAtributo("codUnidadOrganicaExp", registroIniciarTramite.getUnidadTramitadora());
                    tramiteIni.setAtributo("codUnidadTramitadoraTram", registroIniciarTramite.getUnidadTramitadora());
                    tramiteIni.setAtributo("codUnidadTramitadoraAnterior", registroIniciarTramite.getUnidadTramitadora());
                    tramiteIni.setAtributo("codUnidadTramitadoraUsu", registroIniciarTramite.getUnidadTramitadora());
                    

                    //iniciamos el tramite
                   TramitesExpedienteDAO.getInstance().iniciarTramiteManual(adaptador, con, tramiteIni);
                    try {
                        OperacionesExpedienteDAO.getInstance().registrarIniciarTramite(tramiteIni, false, con);
                        log.debug("Grabado en Operaciones Expedientes el inicio de trámite");
                    } catch (TechnicalException e) {
                        log.error("Ha ocurrido un error al grabar la operacion Finalizar Trámite en Historico Operaciones. ", e);
                    }
                    return true;


            } catch (Exception ex) {
            log.error("Error al crear EL TRÁMITE ", ex);
            return false;

        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
            }

    }

    
}
