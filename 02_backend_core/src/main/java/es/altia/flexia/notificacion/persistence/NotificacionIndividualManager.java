package es.altia.flexia.notificacion.persistence;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;

import es.altia.flexia.notificacion.vo.*;

public class NotificacionIndividualManager {


    private static NotificacionIndividualManager instance =	null;
    protected static Config m_ConfigTechnical; //	Para el fichero de configuracion técnico
    protected static Config m_ConfigError; // Para los mensajes de error localizados
    protected static Logger m_Log =
            LogManager.getLogger(NotificacionIndividualManager.class.getName());


    protected NotificacionIndividualManager() {
		// Queremos usar el	fichero de configuración technical
		m_ConfigTechnical =	ConfigServiceHelper.getConfig("techserver");
		// Queremos tener acceso a los mensajes de error localizados
		m_ConfigError	= ConfigServiceHelper.getConfig("error");
	}
	
	public static NotificacionIndividualManager getInstance() {
        // Si no hay una instancia de esta clase tenemos que crear una
        synchronized (NotificacionIndividualManager.class) {
            if (instance == null) {
                instance = new NotificacionIndividualManager();
            }

        }
        return instance;
    }

}
