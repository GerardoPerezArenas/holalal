/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide72.job;

import es.altia.flexia.integracion.moduloexterno.melanbide72.util.ConstantesMeLanbide72;
//import es.altia.flexia.integracion.moduloexterno.melanbide72.job.manager.InteropJobsFSEExptsProceManager;
//import es.altia.flexia.integracion.moduloexterno.melanbide72.job.manager.InteropJobsLogManager;
import es.altia.flexia.integracion.moduloexterno.melanbide72.job.manager.InteropJobsUtils;
//import es.altia.flexia.integracion.moduloexterno.melanbide72.jobs.vo.InteropJobsFSEExpedienteRequest;
//import es.altia.flexia.integracion.moduloexterno.melanbide72.jobs.vo.InteropJobsFSEProcedi;
import es.altia.flexia.integracion.moduloexterno.melanbide72.util.ConfigurationParameter;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author gerardo.perez
 */
public class LazarAlarmaJob implements Job {

    private static final Logger log = LogManager.getLogger(LazarAlarmaJob.class);
    private final SimpleDateFormat formatFechaLog = new SimpleDateFormat("yyyyMMddHHmmssSSS");
   // private final InteropJobsLogManager interopJobsLogManager = new InteropJobsLogManager();
    private final InteropJobsUtils interopJobsUtils = new InteropJobsUtils();
    //  private final InteropJobsFSEExptsProceManager interopJobsFSEExptsProceManager = new InteropJobsFSEExptsProceManager();
    private final M72GestionAlarmasDECEXBatch m72GestionAlarmasDECEXBatch = new M72GestionAlarmasDECEXBatch();
            

    @Override
     public void execute(JobExecutionContext jec)
    throws JobExecutionException
  {
    log.info(getClass().getName() + ".execute -  Begin " + this.formatFechaLog.format(new Date()));
    try
    {
      int contador = jec.getRefireCount();
      log.info("jec.getRefireCount(): " + contador);
      this.interopJobsUtils.getClass();this.interopJobsUtils.getClass();String servidor = this.interopJobsUtils.getParameter("SERVIDOR", "MELANBIDE72");
      


      log.info("servidor: " + servidor);
      if (servidor.equals(System.getProperty("weblogic.Name"))) {
      synchronized (jec)
      {
        AdaptadorSQLBD adaptador = null;
        try
        {
          log.info("Job lanzado : " + System.getProperty("weblogic.Name"));
          this.interopJobsUtils.getClass();this.interopJobsUtils.getClass();int codOrg = Integer.parseInt(this.interopJobsUtils.getParameter("ORGANIZACION_ESQUEMA", "MELANBIDE72"));
          this.interopJobsUtils.getClass();this.interopJobsUtils.getClass();boolean dosEntornos = Boolean.getBoolean(ConfigurationParameter.getParameter("DOS_ENTORNOS", "MELANBIDE72"));
          while (codOrg < 2)
          {
            adaptador = this.interopJobsUtils.getAdaptSQLBD(String.valueOf(codOrg));
            log.info("While de tokens codOrg: " + codOrg);
            

            log.info("Vamos a enviar correo Alarma1 Correcto: " + codOrg);
            this.m72GestionAlarmasDECEXBatch.envioCorreosAlarma1(adaptador, servidor);
            
            log.info("While de tokens codOrg: " + codOrg);
            log.info("Vamos a enviar correo Alarma2 Correcto: " + codOrg);
            this.m72GestionAlarmasDECEXBatch.envioCorreosAlarma2(adaptador);
            
            log.info("While de tokens codOrg: " + codOrg);
            this.m72GestionAlarmasDECEXBatch.envioCorreosAlarma3(adaptador);
            log.info("Vamos a enviar correo Alarma3 Correcto: " + codOrg);
            
            log.info("While de tokens codOrg: " + codOrg);
            this.m72GestionAlarmasDECEXBatch.envioCorreosAlarma4(adaptador);
            log.info("Vamos a enviar correo Alarma4 Correcto: " + codOrg);
            if (dosEntornos) {
              codOrg++;
            } else {
              codOrg = 2;
            }
          }
        }
        catch (Exception e)
        {
          e.printStackTrace();
          log.error(" Error Al ejecutar el job. ", e);
        }
        finally
        {
          log.info(getClass().getName() + ".execute -  Fin " + this.formatFechaLog.format(new Date()));
        }
      }
    }
    
    }
      
    catch (Exception ex)
    {
      ex.printStackTrace();
      log.error(getClass().getName() + " Error: " + ex);
    }
  }
}
