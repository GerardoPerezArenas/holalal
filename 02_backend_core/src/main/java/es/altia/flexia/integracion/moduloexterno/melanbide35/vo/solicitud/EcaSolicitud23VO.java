package es.altia.flexia.integracion.moduloexterno.melanbide35.vo.solicitud;

import java.math.BigDecimal;

/**
 *
 * @author alexandrep
 */
public class EcaSolicitud23VO {

    private String numeroExpediente;
    private Integer totalNumeroPersonas; // TOTALSEG
    private Integer totalNumeroPersonasInsercion; // TOTALNUMPERSONAS
    private BigDecimal totalImporteInserciones; // TOTALIMPORTESOLIC
    
    private Integer mujeresSeguimiento; // MUJERESSEG
    private Integer hombresSeguimiento; // HOMBRESSEG
    private BigDecimal cuantiaSolicitada; // CUANTIASOLIC
    private BigDecimal totalImporteCalculadoUnAnio; // TOTALIMPCAL1ANO
    private BigDecimal importeSolicitadoSeguimiento; // IMPORTESOLICSEG
    
    private BigDecimal horasConvenio;
    private BigDecimal sumatoriaHorasTotales;

    public String getNumeroExpediente() {
        return numeroExpediente;
    }

    public void setNumeroExpediente(String numeroExpediente) {
        this.numeroExpediente = numeroExpediente;
    }

    public Integer getTotalNumeroPersonas() {
        return totalNumeroPersonas;
    }

    public void setTotalNumeroPersonas(Integer totalNumeroPersonas) {
        this.totalNumeroPersonas = totalNumeroPersonas;
    }

    public Integer getTotalNumeroPersonasInsercion() {
        return totalNumeroPersonasInsercion;
    }

    public void setTotalNumeroPersonasInsercion(Integer totalNumeroPersonasInsercion) {
        this.totalNumeroPersonasInsercion = totalNumeroPersonasInsercion;
    }

    public BigDecimal getTotalImporteInserciones() {
        return totalImporteInserciones;
    }

    public void setTotalImporteInserciones(BigDecimal totalImporteInserciones) {
        this.totalImporteInserciones = totalImporteInserciones;
    }

    public Integer getMujeresSeguimiento() {
        return mujeresSeguimiento;
    }

    public void setMujeresSeguimiento(Integer mujeresSeguimiento) {
        this.mujeresSeguimiento = mujeresSeguimiento;
    }

    public Integer getHombresSeguimiento() {
        return hombresSeguimiento;
    }

    public void setHombresSeguimiento(Integer hombresSeguimiento) {
        this.hombresSeguimiento = hombresSeguimiento;
    }

    public BigDecimal getCuantiaSolicitada() {
        return cuantiaSolicitada;
    }

    public void setCuantiaSolicitada(BigDecimal cuantiaSolicitada) {
        this.cuantiaSolicitada = cuantiaSolicitada;
    }

    public BigDecimal getTotalImporteCalculadoUnAnio() {
        return totalImporteCalculadoUnAnio;
    }

    public void setTotalImporteCalculadoUnAnio(BigDecimal totalImporteCalculadoUnAnio) {
        this.totalImporteCalculadoUnAnio = totalImporteCalculadoUnAnio;
    }

    public BigDecimal getImporteSolicitadoSeguimiento() {
        return importeSolicitadoSeguimiento;
    }

    public void setImporteSolicitadoSeguimiento(BigDecimal importeSolicitadoSeguimiento) {
        this.importeSolicitadoSeguimiento = importeSolicitadoSeguimiento;
    }

    public BigDecimal getHorasConvenio() {
        return horasConvenio;
    }

    public void setHorasConvenio(BigDecimal horasConvenio) {
        this.horasConvenio = horasConvenio;
    }

    public BigDecimal getSumatoriaHorasTotales() {
        return sumatoriaHorasTotales;
    }

    public void setSumatoriaHorasTotales(BigDecimal sumatoriaHorasTotales) {
        this.sumatoriaHorasTotales = sumatoriaHorasTotales;
    }

}
