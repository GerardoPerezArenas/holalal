package es.altia.flexia.integracion.moduloexterno.melanbide43.exception;

public class GestionAutomaticaKONTUException extends Exception{

    private int codigo;
    private String descripcionError;
    private String expedienteDeError;
    private int anoAnotacionDeError;
    private long numAnotacionDeError;

    public GestionAutomaticaKONTUException(int codigo, String descripcionError) {
        super(descripcionError);
        this.codigo = codigo;
        this.descripcionError = descripcionError;
    }

    public GestionAutomaticaKONTUException(int codigo, String descripcionError, String expedienteDeError) {
        super(descripcionError);
        this.codigo = codigo;
        this.descripcionError = descripcionError;
        this.expedienteDeError = expedienteDeError;
    }

    public GestionAutomaticaKONTUException(int codigo, String descripcionError, String expedienteDeError, int anoAnotacionDeError, long numAnotacionDeError) {
        super(descripcionError);
        this.codigo = codigo;
        this.descripcionError = descripcionError;
        this.expedienteDeError = expedienteDeError;
        this.anoAnotacionDeError = anoAnotacionDeError;
        this.numAnotacionDeError = numAnotacionDeError;
    }

    public GestionAutomaticaKONTUException(int codigo, String descripcionError, int anoAnotacionDeError, long numAnotacionDeError) {
        super(descripcionError);
        this.codigo = codigo;
        this.descripcionError = descripcionError;
        this.anoAnotacionDeError = anoAnotacionDeError;
        this.numAnotacionDeError = numAnotacionDeError;
    }

    public GestionAutomaticaKONTUException(String descripcionError) {
        super(descripcionError);
        this.descripcionError = descripcionError;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getDescripcionError() {
        return descripcionError;
    }

    public void setDescripcionError(String descripcionError) {
        this.descripcionError = descripcionError;
    }

    public String getExpedienteDeError() {
        return expedienteDeError;
    }

    public int getAnoAnotacionDeError() {
        return anoAnotacionDeError;
    }

    public long getNumAnotacionDeError() {
        return numAnotacionDeError;
    }
}
