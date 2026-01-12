package es.altia.flexia.integracion.moduloexterno.melanbide53.vo;

/**
 *
 * @author altia
 */
public class InformeGeneralVO {

    
    private String descripcionCampo;
    private String mes;
    private String anyo;
    private String totalExpedientes;

    
    
    public InformeGeneralVO()
    {
        
    }

    public String getDescripcionCampo() {
        return descripcionCampo;
    }

    public void setDescripcionCampo(String descripcionCampo) {
        this.descripcionCampo = descripcionCampo;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getAnyo() {
        return anyo;
    }

    public void setAnyo(String anyo) {
        this.anyo = anyo;
    }

    public String getTotalExpedientes() {
        return totalExpedientes;
    }

    public void setTotalExpedientes(String totalExpedientes) {
        this.totalExpedientes = totalExpedientes;
    }

}
