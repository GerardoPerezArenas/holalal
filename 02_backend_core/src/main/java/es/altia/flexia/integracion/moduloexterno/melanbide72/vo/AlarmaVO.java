package es.altia.flexia.integracion.moduloexterno.melanbide72.vo;

public class AlarmaVO {
    
    
    /*
    
        50 días desde la fecha del registro que ha iniciado el expediente 
        (10 días antes de que se cumplan los 60 días del silencio positivo)
    
    Han pasado 50 dias desde la fecha @fechaAltaRegistro@  del registro @numeroEntradaRegistro@ que ha iniciado el expediente @numeroExpediente@
    
    
    
    */
    
    private int codigoAlarma;  // Alarma 1, 2 , 3 y 4
    private String numeroExpediente;
    private String fechaAltaRegistro;
    private String numeroEntradaRegistro;
    private String subCasoAlarma; // En el caso de la alarma 2 hay 4 casos , usamos este campo para identificar cada uno
    

    public AlarmaVO(int codigoAlarma, String numeroExpediente, String fechaAltaRegistro, String numeroEntradaRegistro, String subCasoAlarma ) {
        this.codigoAlarma = codigoAlarma;
        this.numeroExpediente = numeroExpediente;
        this.fechaAltaRegistro = fechaAltaRegistro;
        this.numeroEntradaRegistro = numeroEntradaRegistro;
        this.subCasoAlarma = subCasoAlarma;
    }

    public AlarmaVO() {
    }
    
    public int getCodigoAlarma() {
        return codigoAlarma;
    }

    public void setCodigoAlarma(int codigoAlarma) {
        this.codigoAlarma = codigoAlarma;
    }

    public String getNumeroExpediente() {
        return numeroExpediente;
    }

    public void setNumeroExpediente(String numeroExpediente) {
        this.numeroExpediente = numeroExpediente;
    }

    public String getFechaAltaRegistro() {
        return fechaAltaRegistro;
    }

    public void setFechaAltaRegistro(String fechaAltaRegistro) {
        this.fechaAltaRegistro = fechaAltaRegistro;
    }

    public String getNumeroEntradaRegistro() {
        return numeroEntradaRegistro;
    }

    public void setNumeroEntradaRegistro(String numeroEntradaRegistro) {
        this.numeroEntradaRegistro = numeroEntradaRegistro;
    }
    public String getsubCasoAlarma() {
        return subCasoAlarma;
    }

    public void setsubCasoAlarma(String subCasoAlarma) {
        this.subCasoAlarma = subCasoAlarma;
    }
    
    

    @Override
    public String toString() {
        return "AlarmaVO{" + "codigoAlarma=" + codigoAlarma + ", numeroExpediente=" + numeroExpediente + ", fechaAltaRegistro=" + fechaAltaRegistro + ", numeroEntradaRegistro=" + numeroEntradaRegistro + ", subCasoAlarma=" + subCasoAlarma + '}';
    }
    
     
}