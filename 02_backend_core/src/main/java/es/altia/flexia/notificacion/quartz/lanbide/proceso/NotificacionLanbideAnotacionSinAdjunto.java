package es.altia.flexia.notificacion.quartz.lanbide.proceso;

import es.altia.agora.interfaces.user.web.registro.notificacion.job.util.NotificacionJobLanbideUtils;
import es.altia.agora.interfaces.user.web.sge.notificacion.job.NotificacionPlazoJob;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.flexia.notificacion.quartz.lanbide.persistence.NotificacionLanbideManager;
import java.util.StringTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;


/**
 *
 * @author david.vidal
 */
public class NotificacionLanbideAnotacionSinAdjunto implements Job {
        
    private static Log m_log = LogFactory.getLog(NotificacionPlazoJob.class);
    protected static Config m_ConfigTechnical;
    protected static String gestorD;
            
    public void execute(JobExecutionContext arg0) throws JobExecutionException {                
        try{
            if(NotificacionJobLanbideUtils.getServer().equals(System.getProperty("weblogic.Name"))){
                if(m_log.isInfoEnabled()) m_log.info("------------ lanza NotificacionLanbideAnotacionSinAdjunto Job ::Weblogic.Name: " + System.getProperty("weblogic.Name") + " -----------");
                m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
                gestorD = m_ConfigTechnical.getString("CON.gestor");
                JobDetail jd = arg0.getJobDetail();
                //Obtenemos los parámetros
                if(m_log.isDebugEnabled()) m_log.debug("Obtenemos los parámetros");
                String jndis = (String) jd.getJobDataMap().get("jndis_lanzamiento_proceso");
                StringTokenizer tokens = new StringTokenizer(jndis,";");
                while(tokens.hasMoreTokens()){
                   String params[] = { gestorD, "", "", "", "", "", tokens.nextToken() };
                   NotificacionLanbideManager.getInstance().getAnotacionesConAsunto(params);
                }//while(tokens.hasMoreTokens())                                                                            
            }//if
        } catch (Exception e) {
            e.printStackTrace();
            m_log.error("Error " + e.getMessage());
            throw new JobExecutionException("Error interno");
        }//try-catch
        if(m_log.isDebugEnabled()) m_log.debug("execute () : END");
    }//execute
}//class
