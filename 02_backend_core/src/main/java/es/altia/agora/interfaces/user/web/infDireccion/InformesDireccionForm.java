package es.altia.agora.interfaces.user.web.infDireccion;

import es.altia.common.service.config.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.apache.struts.action.ActionForm;


/** Clase utilizada para capturar o mostrar el estado de una Tramitacion */
public class InformesDireccionForm extends ActionForm {
   //Queremos usar el fichero de configuración technical
    protected static Config m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    //Necesitamos el servicio de log
    protected static Logger m_Log =
            LogManager.getLogger(InformesDireccionForm.class.getName());

    //Reutilizamos

    /* Seccion donde metemos los metods get y set de los campos del formulario */

}
