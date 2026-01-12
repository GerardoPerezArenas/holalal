package es.altia.flexia.integracion.moduloexterno.melanbide70.vo;

/**
 *
 * @author altia
 */
public class DatosEconomicosExpVO {
    
    private Integer importeSubvencion;

    public Integer getImporteSubvencion() {
        return importeSubvencion;
    }

    public void setImporteSubvencion(Integer importeSubvencion) {
        this.importeSubvencion = importeSubvencion;

    }

    public Integer getImportePago() {
        if(this.importeSubvencion!=null){
            return this.importeSubvencion;
        }
        return null;
    }
    
}
