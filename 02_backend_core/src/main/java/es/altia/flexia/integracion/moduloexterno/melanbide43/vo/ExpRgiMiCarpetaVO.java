package es.altia.flexia.integracion.moduloexterno.melanbide43.vo;

/**
 *
 * @author kepa.gonzalez
 */
public class ExpRgiMiCarpetaVO {

    private Integer id;
    private String numExpediente;
    String tipoDoc;
    String numDoc;
    String fechaConcesion;

    public ExpRgiMiCarpetaVO(){
    }
    
    public ExpRgiMiCarpetaVO(Integer id, String numExpediente, String tipoDoc, String numDoc, String fechaConcesion) {
        this.id = id;
        this.numExpediente = numExpediente;
        this.tipoDoc = tipoDoc;
        this.numDoc = numDoc;
        this.fechaConcesion = fechaConcesion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumExpediente() {
        return numExpediente;
    }

    public void setNumExpediente(String numExpediente) {
        this.numExpediente = numExpediente;
    }

    public String getTipoDoc() {
        return tipoDoc;
    }

    public void setTipoDoc(String tipoDoc) {
        this.tipoDoc = tipoDoc;
    }

    public String getNumDoc() {
        return numDoc;
    }

    public void setNumDoc(String numDoc) {
        this.numDoc = numDoc;
    }

    public String getFechaConcesion() {
        return fechaConcesion;
    }

    public void setFechaConcesion(String fechaConcesion) {
        this.fechaConcesion = fechaConcesion;
    }

}
