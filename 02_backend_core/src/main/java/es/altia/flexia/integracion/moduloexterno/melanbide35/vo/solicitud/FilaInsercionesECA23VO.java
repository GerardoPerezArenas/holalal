
package es.altia.flexia.integracion.moduloexterno.melanbide35.vo.solicitud;

import java.math.BigDecimal;

/**
 *
 * @author alexandrep
 */
public class FilaInsercionesECA23VO {
    
    private Integer id;
    private String numeroExpediente;
    private String tipoDiscapacidad;
    private String tipoSexoEdad;
    private Integer numeroPersonas;
    private BigDecimal porcentajeTrabajo;
    private BigDecimal importeCalculadoUnAnio; // IMPORTECAL1ANO
    private BigDecimal importeSolicitado; // IMPORTESOLIC

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumeroExpediente() {
        return numeroExpediente;
    }

    public void setNumeroExpediente(String numeroExpediente) {
        this.numeroExpediente = numeroExpediente;
    }
    

    public String getTipoDiscapacidad() {
        return tipoDiscapacidad;
    }

    public void setTipoDiscapacidad(String tipoDiscapacidad) {
        this.tipoDiscapacidad = tipoDiscapacidad;
    }

    public String getTipoSexoEdad() {
        return tipoSexoEdad;
    }

    public void setTipoSexoEdad(String tipoSexoEdad) {
        this.tipoSexoEdad = tipoSexoEdad;
    }

    public Integer getNumeroPersonas() {
        return numeroPersonas;
    }

    public void setNumeroPersonas(Integer numeroPersonas) {
        this.numeroPersonas = numeroPersonas;
    }

    public BigDecimal getPorcentajeTrabajo() {
        return porcentajeTrabajo;
    }

    public void setPorcentajeTrabajo(BigDecimal porcentajeTrabajo) {
        this.porcentajeTrabajo = porcentajeTrabajo;
    }

    public BigDecimal getImporteCalculadoUnAnio() {
        return importeCalculadoUnAnio;
    }

    public void setImporteCalculadoUnAnio(BigDecimal importeCalculadoUnAnio) {
        this.importeCalculadoUnAnio = importeCalculadoUnAnio;
    }

    public BigDecimal getImporteSolicitado() {
        return importeSolicitado;
    }

    public void setImporteSolicitado(BigDecimal importeSolicitado) {
        this.importeSolicitado = importeSolicitado;
    }
    
    
}
