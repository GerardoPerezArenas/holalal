// NOMBRE DEL PAQUETE
package es.altia.agora.interfaces.user.web.registro.mantenimiento;

// PAQUETES IMPORTADOS
import es.altia.common.service.config.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.altia.technical.Message;
import es.altia.technical.ValidationException;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.*;

/**
 * <p>Título: Proyecto @gora</p>
 * <p>Descripción: Clase ManActuacionesForm</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Empresa: AYTOS CPD & ALTIA CONSULTORES</p>
 * @author Daniel Toril Cabrera
 * @version 1.0
 */

public class ManActuacionesForm extends org.apache.struts.action.ActionForm {

  //Queremos usar el fichero de configuración technical
  protected static Config m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
  //Necesitamos el servicio de log
  protected static Logger m_Log =
          LogManager.getLogger(ManActuacionesForm.class.getName());

  private Vector codigos;
  private Vector utilizados;
  private Vector utilizadosCerrados;

  public Vector getCodigos(){
    return codigos;
  }
  public void setCodigos(Vector listCods){
    codigos = listCods;
  }

  public Vector getUtilizados(){
	  return utilizados;
  }
  public void setUtilizados(Vector util){
		 this.utilizados=util;
  }

  public Vector getUtilizadosCerrados(){
	  return utilizadosCerrados;
  }
  public void setUtilizadosCerrados(Vector util){
		 this.utilizadosCerrados=util;
  }

  public ActionErrors validate(ActionMapping mapping, HttpServletRequest request, String idioma) {
    m_Log.debug("validate");
    ActionErrors errors = new ActionErrors();

    //RelUnOrgValueObject hara el trabajo para nostros ...
    /*try {
	//manActVO.validate(idioma);
    } catch (ValidationException ve) {
	//Hay errores...
	//Tenemos que traducirlos a formato struts
	errors=validationException(ve,errors);
    }*/
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