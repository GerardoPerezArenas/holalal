/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide32.vo.centroempleo;

/**
 *
 * @author santiagoc
 */
public class CeUbicacionVO 
{
    private Integer oriCeUbicCod;
    private Long oriEntCod;
    private Integer oriAmbCod;
    private Integer munPai;
    private Integer munPrv;
    private Integer munCod;
    private Integer prvPai;
    private String oriCeDireccion;
    private String oriCeAdjudicada;
    private String oriCeEspecial;
    private Integer ordenAdjudicado;    // Se crea para pasar cirterio de orden al excel de resolucion en cemp procesos
    private String validacion; 
    private String codigoPostal; 
    private String horarioAtencion; 
    private String numeroExpediente; 
    private String aprobado;
    private String mantenido;
    private String localNuevoValidado; // ORI_CE_LOCAL_NUEVO_VALIDADO

  
    public CeUbicacionVO()
    {
        
    }

    public Integer getOriCeUbicCod() {
        return oriCeUbicCod;
    }

    public void setOriCeUbicCod(Integer oriCeUbicCod) {
        this.oriCeUbicCod = oriCeUbicCod;
    }

    public Long getOriEntCod() {
        return oriEntCod;
    }

    public void setOriEntCod(Long oriEntCod) {
        this.oriEntCod = oriEntCod;
    }

    public Integer getOriAmbCod() {
        return oriAmbCod;
    }

    public void setOriAmbCod(Integer oriAmbCod) {
        this.oriAmbCod = oriAmbCod;
    }

    public Integer getMunPai() {
        return munPai;
    }

    public void setMunPai(Integer munPai) {
        this.munPai = munPai;
    }

    public Integer getMunPrv() {
        return munPrv;
    }

    public void setMunPrv(Integer munPrv) {
        this.munPrv = munPrv;
    }

    public Integer getMunCod() {
        return munCod;
    }

    public void setMunCod(Integer munCod) {
        this.munCod = munCod;
    }

    public Integer getPrvPai() {
        return prvPai;
    }

    public void setPrvPai(Integer prvPai) {
        this.prvPai = prvPai;
    }

    public String getOriCeDireccion() {
        return oriCeDireccion;
    }

    public void setOriCeDireccion(String oriCeDireccion) {
        this.oriCeDireccion = oriCeDireccion;
    }

    public String getOriCeAdjudicada() {
        return oriCeAdjudicada;
    }

    public void setOriCeAdjudicada(String oriCeAdjudicada) {
        this.oriCeAdjudicada = oriCeAdjudicada;
    }

    public String getOriCeEspecial() {
        return oriCeEspecial;
    }

    public void setOriCeEspecial(String oriCeEspecial) {
        this.oriCeEspecial = oriCeEspecial;
    }
    
    public void setOrdenAdjudicado(Integer ordenAdjudicado) {
        this.ordenAdjudicado = ordenAdjudicado;
    }

    public Integer getOrdenAdjudicado() {
        return ordenAdjudicado;
    }

    public String getValidacion() {
        return validacion;
    }

    public void setValidacion(String validacion) {
        this.validacion = validacion;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getHorarioAtencion() {
        return horarioAtencion;
    }

    public void setHorarioAtencion(String horarioAtencion) {
        this.horarioAtencion = horarioAtencion;
    }

    public String getNumeroExpediente() {
        return numeroExpediente;
    }

    public void setNumeroExpediente(String numeroExpediente) {
        this.numeroExpediente = numeroExpediente;
    }
      public String getAprobado() {
        return aprobado;
    }

    public void setAprobado(String aprobado) {
        this.aprobado = aprobado;
    }

    public String getMantenido() {
        return mantenido;
    }

    public void setMantenido(String mantenido) {
        this.mantenido = mantenido;
    }
    
    public String getLocalNuevoValidado() {
        return localNuevoValidado;
    }

    public void setLocalNuevoValidado(String localNuevoValidado) {
        this.localNuevoValidado = localNuevoValidado;
    }
}
