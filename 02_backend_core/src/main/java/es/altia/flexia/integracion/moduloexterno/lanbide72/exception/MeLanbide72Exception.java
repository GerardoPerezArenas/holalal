package es.altia.flexia.integracion.moduloexterno.lanbide72.exception;
/**
 * Clase para manejar las excepciones del modulo MeLanbide72
 * 
 * @author gerardo.perez
 *  
 */
public class MeLanbide72Exception extends Exception {
    
    public final static Integer ERROR_FECHA_INICIO_SUPERIOR_FIN = 1;
    public final static Integer ERROR_FECHA_INICIO_VACIA = 2;
    public final static Integer ERROR_FECHA_FIN_VACIA = 3;
    
    private Integer codError = null;
    
    public MeLanbide72Exception (String message){
        super(message);
    }//MeLanbide01Exception
    
    public MeLanbide72Exception (String message, Throwable ex){
        super(message, ex);
    }//MeLanbide01Exception
    
    public Integer getCodError(){
        return this.codError;
    }//getCodError
    public void setCodError(Integer codError){
        this.codError = codError;
    }//setCodError
    
}//class
