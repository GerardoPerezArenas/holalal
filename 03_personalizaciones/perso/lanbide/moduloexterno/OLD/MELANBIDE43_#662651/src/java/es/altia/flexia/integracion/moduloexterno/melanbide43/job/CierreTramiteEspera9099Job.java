/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.altia.flexia.integracion.moduloexterno.melanbide43.job;

import es.altia.flexia.integracion.moduloexterno.melanbide43.MELANBIDE43;
import es.altia.flexia.integracion.moduloexterno.melanbide43.dao.MeLanbide43DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide43.manager.MeLanbide43Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide43.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide43.util.ConstantesMeLanbide43;
import es.altia.flexia.integracion.moduloexterno.melanbide43.util.MeLanbide43BDUtil;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.Tramite9099CerrarVO;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.Tramite9099VO;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExternoParamAdicionales;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;

/**
 *
 * @author alexandrep
 */
public class CierreTramiteEspera9099Job implements Job{
    
      private final Logger log = Logger.getLogger(CierreTramiteEspera9099Job.class);
    
     @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        try {
            int contador = jec.getRefireCount();
            Job trab = jec.getJobInstance();

            log.info(this.getClass().getName() + " jec.getRefireCount(): " + contador);
            Trigger trigger = jec.getTrigger();

            String servidor = ConfigurationParameter.getParameter(ConstantesMeLanbide43.CAMPO_SERVIDOR, ConstantesMeLanbide43.FICHERO_PROPIEDADES);

            //String servidor = "flexia1"; //DESA
            //String servidor = "paprergi1_flexia1"; //PRE
            //String servidor = "pargi1_flexia1"; //PRO
            log.info(this.getClass().getName() + " servidor: " + servidor);
            if (servidor.equals(System.getProperty("weblogic.Name"))) {//PARA LOCAL QUITAR
                synchronized (jec) {
                    
                    Connection con = null;
                    boolean dev = false;
                    int numIntentos = 0;
                    String numExpediente = "";
                    int id = 0;
                    String[] params = new String[]{"ORACLE"};
                    try {
                        log.info(this.getClass().getSimpleName()+" CierreTramiteEspera9099Job - Execute lanzado " + System.getProperty("weblogic.Name"));

                        int codOrg = Integer.parseInt(ConfigurationParameter.getParameter("COD_ORG", ConstantesMeLanbide43.FICHERO_PROPIEDADES));
                        boolean dosEntornos = Boolean.getBoolean(ConfigurationParameter.getParameter("DOS_ENTORNOS",
                                ConstantesMeLanbide43.FICHERO_PROPIEDADES));

                        while (codOrg < 2) {
                            MeLanbide43BDUtil meLanbide43BDUtil = new MeLanbide43BDUtil();
                            AdaptadorSQLBD adaptador = meLanbide43BDUtil.getAdaptSQLBD(String.valueOf(codOrg));
                            con = adaptador.getConnection();
                            log.info("CierreTramiteEspera9099Job - En el while de tokens codOrg: " + codOrg);
                            
                            
                            //Primero recuperamos todos los expedientes que tengan el tramite en question 9099
                            List<Tramite9099VO> listaProcedimientosTratar = MeLanbide43Manager.getInstance().getProcedimientosConTramite9099(adaptador);
                           
                            log.info("CierreTramiteEspera9099Job - Se recuperar el siguiente numero de procedimientos a tratar"+ listaProcedimientosTratar.size());
                             for (Iterator<Tramite9099VO> i = listaProcedimientosTratar.iterator(); i.hasNext();) {
                                Tramite9099VO item = i.next();
                                
                                //Calculamos el tiempo que tienen para eliminar-se
                               Integer diasCerrar = 31;
                               
 
                                
                                //Recuperamos los tramites a cerrar 
                                List<Tramite9099CerrarVO> tramites =  MeLanbide43Manager.getInstance().getProcedimientosTramites9099PorProcedimiento(item.getProcedimiento(),item.getCodigoTramite(),adaptador);
                             log.info("CierreTramiteEspera9099Job - Se recuperar el siguiente numero de tramites a tratar "+ tramites.size()+" para el procedimiento "+item.getProcedimiento());

                                //Recorremos los tramites a cerrar
                                for (Iterator<Tramite9099CerrarVO> x = tramites.iterator(); x.hasNext();) {
                                    Tramite9099CerrarVO tramite = x.next();
                                     Boolean cerrar = false;
                                     long horaActual = System.currentTimeMillis();
                                     log.info("CierreTramiteEspera9099Job - Tratamos el expediente "+ tramite.getNumeroExpediente());
                                    //Se comprueba que la fecha actual es mayor que la fecha a la que se deberia cerrar el expediente
                                    Calendar cal = Calendar.getInstance();
                                    cal.setTime(tramite.getFechaInicioTramite());
                                    cal.add(Calendar.DATE, diasCerrar); //minus number would decrement the days
                                
                                    if (cal.getTime().getTime()<horaActual){
                                       cerrar=true; 
                                       log.info("CierreTramiteEspera9099Job - Se debe cerrar el tramite ha superado el tiempo maximo que puede estar abierto");
                                    } else {
                                        log.info("CierreTramiteEspera9099Job - No se debe cerrar el tramite no se ha superado el tiempo maximo de espera");
                                    }
                                    
                                    if (cerrar){
                                        //Recuperamos los datos del expediente a cerrar
                                        String numeroExpediente = tramite.getNumeroExpediente();
                                        Integer ocurrenciaActual = tramite.getOcurrenciaTramiteActual();
                                        tramite = null;
                                        tramite = MeLanbide43Manager.getInstance().getProcedimientosTramites9099cerrar(item.getProcedimiento(),item.getCodigoTramite(), numeroExpediente ,adaptador,ocurrenciaActual);
                                        //Ponemos el if por si por algun casual se ha modificado la bbdd en el tiempo que se tarda a revisar si se tiene que cerrar puede que sea mejor todo en un solo sql....
                                        if (tramite!=null){
                                        //Procedemos a realizar el cierre de la espera
                                         MELANBIDE43 melanbide43 = new MELANBIDE43();
                                         ModuloIntegracionExternoParamAdicionales adicionales = new ModuloIntegracionExternoParamAdicionales();
                                        adicionales.setOrigenLlamada("IN");
                                        String resultadoCerrarEspera = "1";
                                        try{
                                        resultadoCerrarEspera =  melanbide43.cerrarEsperaMisGest(codOrg, tramite.getCodTramiteActal(), tramite.getOcurrenciaTramiteActual(), tramite.getNumeroExpediente(), adicionales);
                                        } catch (Exception e) {
                                            
                                        }
                                        if (resultadoCerrarEspera.equalsIgnoreCase("0")){
                                            log.info("CierreTramiteEspera9099Job - Cerrada la espera para el expediente: "+tramite.getNumeroExpediente()); 
                                            //Como todo a ido bien ahora cerramos el tramite
                                             log.info("CierreTramiteEspera9099Job - Cerramos el tramite: "+tramite.getNumeroExpediente());
                                            MeLanbide43Manager.getInstance().cerrarTramite(codOrg, tramite.getNumeroExpediente(), tramite.getCodTramiteActal().toString(), tramite.getCodProcedimiento(), adaptador, tramite.getOcurrenciaTramiteActual());
                                            log.info("CierreTramiteEspera9099Job - Abrimos el nuevo tramite");
                                            //Recuperamos el tramite a abrir y lo abrimos
                                            String tramiteAbrir = MeLanbide43Manager.getInstance().getTramiteSalida(codOrg, adaptador, tramite.getCodProcedimiento(),tramite.getCodTramiteActal());
                                            if (tramiteAbrir!=null){
                                               try{ 
                                                 log.info("CierreTramiteEspera9099Job - Abrimos el nuevo tramite: "+tramiteAbrir);
                                                 //Cogemos los datos del anterior job para poder poner todo al abrir el nuevo tramite 
                                                MeLanbide43Manager.getInstance().abrirTramite(codOrg, tramite.getNumeroExpediente(), tramiteAbrir, tramite.getCodProcedimiento(), tramite.getUor(), tramite.getUsuario(), adaptador);
                                                MeLanbide43Manager.getInstance().insertarLineasLogJob9099(adaptador, numeroExpediente, "OK", "Tramitado");
                                               } catch (Exception e){
                                                 MeLanbide43Manager.getInstance().insertarLineasLogJob9099(adaptador, numeroExpediente, "ERROR", "Error el abrir el nuevo tramite");
                                               }
                                            }else {
                                            log.error("CierreTramiteEspera9099Job - Error al intentar abrir nuevo tramite: "+tramite.getNumeroExpediente());   
                                            MeLanbide43Manager.getInstance().insertarLineasLogJob9099(adaptador, numeroExpediente, "ERROR", "No se ha recuperado un nuevo tramite para abrir");
                                            }
                                        } else {
                                        MeLanbide43Manager.getInstance().insertarLineasLogJob9099(adaptador, numeroExpediente, "ERROR", "No se ha podido cerrar la espera en mi carpeta del tramite");

                                            log.error("CierreTramiteEspera9099Job - Error al cerrar la espera para el expediente: "+tramite.getNumeroExpediente());
                                        }
                                        } //  if (tramite!=null) 
                                        else {
                                         MeLanbide43Manager.getInstance().insertarLineasLogJob9099(adaptador, numeroExpediente, "ERROR", "No se ha encontrado el tramite 9099 al volver a consultar a la bbdd");

                                            //Nunca deberia llegar a este error si ha llegado aqui existe el tramite 
                                            log.error("CierreTramiteEspera9099Job - Error al intentar recuperar el tramite a cerrar");
                                        }
                                    }
                             }    
                             }
                            
                            if (dosEntornos) {
                                codOrg++;
                            } else {
                                codOrg = 2;
                            }
                            if (con != null) {
                                con.close();
                            }

                        }
                    } catch (Exception e) {
                        log.error(CierreTramiteEspera9099Job.class.getName() + " Error en el job : ", e);
                        try {
                            int intentos = numIntentos + 1;
                        } catch (Exception i) {
                            log.error("Error en int intentos = numIntentos + 1 : " + i.getMessage());
                        }
                    } finally {
                        if (con != null) {
                            try {
                                con.close();
                            } catch (SQLException ex) {
                                log.error(CierreTramiteEspera9099Job.class.getName()+" - Error cerrando BBDD", ex);
                            }
                        }
                    }
                }
            
        log.error(this.getClass().getName() + ".execute -  Fin " + new Date().toString());
            }} catch (Exception ex) {
            log.error(this.getClass().getName()  + " Error: "+ex.getMessage(),ex);
        }
        
    }

    private Integer calculoTiempo(Integer plazo, String unidadPlazo) {
      Integer i = 0;
      Integer horasDia = 24;
      Integer mesesDias = 30;
        //Si el tiempo es en horas
        if (unidadPlazo.equalsIgnoreCase("H")){
            return plazo;
        }
        
        //Si el tiempo es en meses calculamos las horas que seria
        if (unidadPlazo.equalsIgnoreCase("M")){
            i = mesesDias * plazo;
            i = i * horasDia;
            return i;
        }
        
        
        i = plazo * horasDia;
        return i;
    }
       
    
    
}
