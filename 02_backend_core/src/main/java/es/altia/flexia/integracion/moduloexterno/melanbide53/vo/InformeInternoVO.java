package es.altia.flexia.integracion.moduloexterno.melanbide53.vo;

/**
 *
 * @author altia
 */
public class InformeInternoVO {

    
    private String uoNombre;
    private String uoCodigo;
    private String mes;
    private String anyo;
    private String totalExpedientes;

    
    
    public InformeInternoVO()
    {
        
    }

    public String getUoNombre() {
        return uoNombre;
    }

    public void setUoNombre(String uoNombre) {
        this.uoNombre = uoNombre;
    }

    public String getUoCodigo() {
        return uoCodigo;
    }

    public void setUoCodigo(String uoCodigo) {
        this.uoCodigo = uoCodigo;
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
