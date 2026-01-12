package es.altia.agora.business.util.persistence;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.util.persistence.manual.MenuDAO;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



import java.util.Vector;



public class MenuManager {

	// Mi propia instancia usada en el metodo getInstance.
	private static MenuManager instance = null;

	// Para el fichero de configuracion technical.
	protected static Config m_ConfigTechnical;
	// Para el fichero de mensajes de error localizados.
    protected static Config m_ConfigError;

    protected static Logger m_Log =
            LogManager.getLogger(MenuManager.class.getName());


    protected MenuManager() {
	  //Queremos usar el fichero de configuración technical
	  m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
	  //Queremos tener acceso a los mensajes de error localizados
	  m_ConfigError = ConfigServiceHelper.getConfig("error");
    }


    /**
     * Factory method para el <code>Singelton</code>.
     * @return La unica instancia de MenuManager
     */
    public static MenuManager getInstance() {
	  //Si no hay una instancia de esta clase tenemos que crear una.
	  if (instance == null) {
		// Necesitamos sincronización aquí para serializar (no multithread) las invocaciones a este metodo
		synchronized(MenuManager.class) {
		    if (instance == null)
			  instance = new MenuManager();
		}
	  }
	  return instance;
    }

	 public Vector buscaMenu(UsuarioValueObject usuarioVO) {

		//queremos estar informados de cuando este metodo es ejecutado
		m_Log.info("buscaMenu");

		try{
			return MenuDAO.getInstance().loadMenu(usuarioVO);
		}catch (TechnicalException te) {
            if(m_Log.isErrorEnabled()) m_Log.error("JDBC Technical problem " + te.getMessage());
            return null;
        }
	}
}