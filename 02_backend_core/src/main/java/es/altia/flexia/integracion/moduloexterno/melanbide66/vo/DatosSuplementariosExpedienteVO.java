package es.altia.flexia.integracion.moduloexterno.melanbide66.vo;

import java.sql.Date;


/**
 *
 * @author altia
 */
public class DatosSuplementariosExpedienteVO {
    
        private Double importeSubvencion = null;
    private Double importeSubvencionCBSC = null;
    private Double importePrimerPago = null;
    private Double importePrimerPagoCBSC = null;
    private Date fecPrimerPago = null;
    private Double importeSegundoPago = null;
    private Double importeReintegro = null;

    public Double getImporteSubvencion() {
        return importeSubvencion;
    }

    public void setImporteSubvencion(Double importeSubvencion) {
        this.importeSubvencion = importeSubvencion;
    }

    public Double getImporteSubvencionCBSC() {
        return importeSubvencionCBSC;
    }

    public void setImporteSubvencionCBSC(Double importeSubvencionCBSC) {
        this.importeSubvencionCBSC = importeSubvencionCBSC;
    }

    public Double getImportePrimerPago() {
        return importePrimerPago;
    }

    public void setImportePrimerPago(Double importePrimerPago) {
        this.importePrimerPago = importePrimerPago;
    }

    public Double getImportePrimerPagoCBSC() {
        return importePrimerPagoCBSC;
    }

    public void setImportePrimerPagoCBSC(Double importePrimerPagoCBSC) {
        this.importePrimerPagoCBSC = importePrimerPagoCBSC;
    }

    public Date getFecPrimerPago() {
        return fecPrimerPago;
    }

    public void setFecPrimerPago(Date fecPrimerPago) {
        this.fecPrimerPago = fecPrimerPago;
    }

    public Double getImporteSegundoPago() {
        return importeSegundoPago;
    }

    public void setImporteSegundoPago(Double importeSegundoPago) {
        this.importeSegundoPago = importeSegundoPago;
    }

    public Double getImporteReintegro() {
        return importeReintegro;
    }

    public void setImporteReintegro(Double importeReintegro) {
        this.importeReintegro = importeReintegro;
    }
    

}
