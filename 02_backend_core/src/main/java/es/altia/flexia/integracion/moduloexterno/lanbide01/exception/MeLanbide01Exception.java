package es.altia.flexia.integracion.moduloexterno.lanbide01.exception;
/**
 * Clase para manejar las excepciones del modulo MeLanbide01
 * 
 * @author david.caamano
 * @version 23/10/2012 1.0
 * Historial de cambios:
 * <ol>
 *  <li>david.caamano * 23-10-2012 * </li>
 * </ol> 
 */
public class MeLanbide01Exception extends Exception {
    
    public final static Integer ERROR_FECHA_INICIO_SUPERIOR_FIN = 1;
    public final static Integer ERROR_FECHA_INICIO_VACIA = 2;
    public final static Integer ERROR_FECHA_FIN_VACIA = 3;
    
    private Integer codError = null;
    
    public MeLanbide01Exception (String message){
        super(message);
    }//MeLanbide01Exception
    
    public MeLanbide01Exception (String message, Throwable ex){
        super(message, ex);
    }//MeLanbide01Exception
    
    public Integer getCodError(){
        return this.codError;
    }//getCodError
    public void setCodError(Integer codError){
        this.codError = codError;
    }//setCodError
    
}//class
