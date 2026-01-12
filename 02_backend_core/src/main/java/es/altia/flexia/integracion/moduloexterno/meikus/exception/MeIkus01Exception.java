package es.altia.flexia.integracion.moduloexterno.meikus.exception;

/**
 *
 * @author david.caamano
 */
public class MeIkus01Exception extends Exception{

    private String codError;

    public String getCodError() {
        return codError;
    }//getCodError
    public void setCodError(String codError) {
        this.codError = codError;
    }//setCodError

    public MeIkus01Exception (String message){
        super(message);
    }//MeIkus01Exception
    
    public MeIkus01Exception (String message, Throwable ex){
        super(message, ex);
    }//MeIkus01Exception
    
}//MeIkus01Exception
