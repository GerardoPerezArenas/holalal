package es.altia.flexia.integracion.moduloexterno.melanbide66.vo;

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
            //return (int)Math.round((this.importeSubvencion*this.porcentajePrimerPago)/100);
           return (double)Math.round((((double)this.importeSubvencion*(double)this.porcentajePrimerPago)/100)*100)/100;     
        }
        return null;
    }
    
}
