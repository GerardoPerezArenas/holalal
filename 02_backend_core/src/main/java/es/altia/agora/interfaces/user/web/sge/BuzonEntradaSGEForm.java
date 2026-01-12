package es.altia.agora.interfaces.user.web.sge;

import es.altia.agora.business.sge.BuzonEntradaSGEValueObject;
import es.altia.common.service.config.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.altia.technical.Message;
import es.altia.technical.ValidationException;

import java.util.Iterator;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/** Clase utilizada para capturar o mostrar el estado del BuzonEntradaSGE */
public class BuzonEntradaSGEForm extends ActionForm {
	//Queremos usar el fichero de configuración technical
    protected static Config m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    //Necesitamos el servicio de log
    protected static Logger m_Log =
            LogManager.getLogger(BuzonEntradaSGEForm.class.getName());
    //Reutilizamos
	BuzonEntradaSGEValueObject buzonVO = new BuzonEntradaSGEValueObject();
	private Vector iniciados;

	public Vector getIniciados(){ return iniciados; }
    public void setIniciados(Vector ini){ iniciados=ini; }		

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request, String idioma) {
		m_Log.debug("validate");
        ActionErrors errors = new ActionErrors();
        //BuzonEntradaSGEValueObject hara el trabajo para nostros ...
        try {
			buzonVO.validate(idioma);
        } catch (ValidationException ve) {
			//Hay errores...
			//Tenemos que traducirlos a formato struts
			errors=validationException(ve,errors);
        }
        return errors;
    }

    /* Función que procesa los errores de validación a formato struts */
    private ActionErrors validationException(ValidationException ve,ActionErrors errors){
		Iterator iter = ve.getMessages().get();
		while (iter.hasNext()) {
			Message message = (Message)iter.next();
			errors.add(message.getProperty(), new ActionError(message.getMessageKey()));
		}
		return errors;
    }
}
    