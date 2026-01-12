package es.altia.agora.interfaces.user.web.registro.mantenimiento;



import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



import java.util.Vector;

import org.apache.struts.action.ActionForm;


public class MantRegistroExternoForm extends ActionForm {
   
    //Necesitamos el servicio de log
    protected static Logger m_Log =
            LogManager.getLogger(MantRegistroExternoForm.class.getName());
    
    private Vector listaOrganizacionesExternas;
	private Vector listaUnidadesRegistroExternas;
    private String resultado = "";
    
    public Vector getListaOrganizacionesExternas(){
        return listaOrganizacionesExternas;
    }
    public void setListaOrganizacionesExternas(Vector param){
    	this.listaOrganizacionesExternas=param;
    }
	public Vector getListaUnidadesRegistroExternas(){
			return listaUnidadesRegistroExternas;
	}
	public void setListaUnidadesRegistroExternas(Vector param){
			this.listaUnidadesRegistroExternas=param;
	}	
	public String getResultado(){
			return resultado;
		}
	public void setResultado(String param){
			this.resultado=param;
		}
    
}
