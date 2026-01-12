package es.altia.agora.interfaces.user.web.listeners;

import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



public class ApplicationInitContextListener implements ServletContextListener {
    
    protected static Logger log = LogManager.getLogger(ApplicationInitContextListener.class.getName());

    public void contextInitialized(ServletContextEvent sce) {
        log.debug("ApplicationInitContextListener: contextInitialized");
        
        Config configVersion = ConfigServiceHelper.getConfig("version");
        ServletContext servletContext = sce.getServletContext();
        String appVersion = configVersion.getString("version.aplicacion");
        
        log.info(String.format("Version de la aplicacion: %s", appVersion));
        
        servletContext.setAttribute("appVersion", appVersion);
    }

    public void contextDestroyed(ServletContextEvent sce) {
        log.debug("ApplicationInitContextListener: contextDestroyed");
    }
}
