/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.agora.interfaces.user.web.util;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author adrian.freixeiro
 */
public class OperacionesExpedienteTraductor {
    
    //Para informacion de logs.
    private static Logger m_Log = LogManager.getLogger(OperacionesExpedienteTraductor.class.getName());  
    
    protected void HistoricoExpedienteTraductorHTML() {
    }
            
    public static String traducir(TraductorAplicacionBean traductor, String texto) {
        
        int i = 0;
        int j = 0;
        String resultado = texto;
        
        while (i >= 0 && j >= 0 ) {
            i = texto.indexOf("{", j);
            
            if (i >= 0)
                j =  texto.indexOf("}", i);
            
            if (i >= 0 && j >= 0) {
                String traduccion = traductor.getDescripcion(texto.substring(i+1, j));
                
                if (traduccion != null)
                    resultado = resultado.replace(texto.substring(i, j+1), traduccion);
            }
        }
        
        return resultado;
    }
    
}
