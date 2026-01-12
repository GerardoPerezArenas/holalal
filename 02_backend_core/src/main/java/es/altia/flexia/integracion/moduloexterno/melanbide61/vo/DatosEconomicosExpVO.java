package es.altia.flexia.integracion.moduloexterno.melanbide61.vo;

/**
 *
 * @author altia
 */
public class DatosEconomicosExpVO {
    
    private Double importeSubvencion;
    private Double porcentajePrimerPago;

    public Double getImporteSubvencion() {
        return importeSubvencion;
    }

    public void setImporteSubvencion(Double importeSubvencion) {
        this.importeSubvencion = importeSubvencion;
    }

    public Double getPorcentajePrimerPago() {
        return porcentajePrimerPago;
    }

    public void setPorcentajePrimerPago(Double porcentajePrimerPago) {
        this.porcentajePrimerPago = porcentajePrimerPago;
    }

    public Double getImportePrimerPago() {
        if(this.importeSubvencion!=null && this.porcentajePrimerPago!=null){
           return (double)Math.round((((double)this.importeSubvencion*(double)this.porcentajePrimerPago)/100)*100)/100;     
        }
        return null;
    }
    public Double getImporteSegundoPago() {
        if(this.importeSubvencion!=null && this.porcentajePrimerPago!=null){
           return (double)Math.round((((double)this.importeSubvencion-(double)this.getImportePrimerPago()))*100)/100;     
        }
        return null;
    }
}
