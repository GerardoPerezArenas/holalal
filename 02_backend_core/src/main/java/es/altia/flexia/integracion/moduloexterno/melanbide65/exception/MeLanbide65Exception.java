package es.altia.flexia.integracion.moduloexterno.melanbide65.exception;

public class MeLanbide65Exception extends Exception {
    private int codigo;
    private String error;
    
    public MeLanbide65Exception(String message) {
        super(message);
    }
    
    public MeLanbide65Exception(int codigo, String error) {
        this.codigo = codigo;
        this.error = error;
    }
    

    /**
     * @return the codigo
     */
    public int getCodigo() {
        return codigo;
    }

    /**
     * @param codigo the codigo to set
     */
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    /**
     * @return the error
     */
    public String getError() {
        return error;
    }

    /**
     * @param error the error to set
     */
    public void setError(String error) {
        this.error = error;
    }
    
}
