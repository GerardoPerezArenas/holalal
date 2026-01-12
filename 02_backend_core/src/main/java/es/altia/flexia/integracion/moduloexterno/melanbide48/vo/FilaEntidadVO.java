/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide48.vo;

/**
 *
 * * @author santiagoc
 * 
 * Clase que unifica los campos para EntidadPadre y EntidadesAsociadas
 */
public class FilaEntidadVO {
    private Long codEntidad;
    //Añadimos este campo para identificar si es es una entidad Asociada, tener el dato de la entidad padre.
    private Long codEntidadPadreAgrup;
    private Integer ejercicio;
    private String numExp;
    private Integer anioConvocatoria;
    private Integer esAgrupacion;
    private String tipoCif;
    private String cif;
    private String nombre;
    private Integer centroEspEmpTH;
    private Integer participanteMayorCentEcpEmpTH;
    private Integer empresaInsercionTH;
    private Integer promotorEmpInsercionTH;
    private Integer numTotalEntAgrupacion;
    private Double porcentaCompromisoRealizacion;
    private Integer planIgualdad;
    private Integer certificadoCalidad;
    private Integer aceptaNumeroSuperiorHoras;
    private Integer segundosLocalesMismoAmbito;
    
    public FilaEntidadVO()
    {
        
    }

    public Long getCodEntidad() {
        return codEntidad;
    }

    public void setCodEntidad(Long codEntidad) {
        this.codEntidad = codEntidad;
    }

    public Integer getEjercicio() {
        return ejercicio;
    }

    public void setEjercicio(Integer ejercicio) {
        this.ejercicio = ejercicio;
    }

    public String getNumExp() {
        return numExp;
    }

    public void setNumExp(String numExp) {
        this.numExp = numExp;
    }

    public Integer getAnioConvocatoria() {
        return anioConvocatoria;
    }

    public void setAnioConvocatoria(Integer anioConvocatoria) {
        this.anioConvocatoria = anioConvocatoria;
    }

    public Integer getEsAgrupacion() {
        return esAgrupacion;
    }

    public void setEsAgrupacion(Integer esAgrupacion) {
        this.esAgrupacion = esAgrupacion;
    }

    public String getTipoCif() {
        return tipoCif;
    }

    public void setTipoCif(String tipoCif) {
        this.tipoCif = tipoCif;
    }

    public String getCif() {
        return cif;
    }

    public void setCif(String cif) {
        this.cif = cif;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getCentroEspEmpTH() {
        return centroEspEmpTH;
    }

    public void setCentroEspEmpTH(Integer centroEspEmpTH) {
        this.centroEspEmpTH = centroEspEmpTH;
    }

    public Integer getParticipanteMayorCentEcpEmpTH() {
        return participanteMayorCentEcpEmpTH;
    }

    public void setParticipanteMayorCentEcpEmpTH(Integer participanteMayorCentEcpEmpTH) {
        this.participanteMayorCentEcpEmpTH = participanteMayorCentEcpEmpTH;
    }

    public Integer getEmpresaInsercionTH() {
        return empresaInsercionTH;
    }

    public void setEmpresaInsercionTH(Integer empresaInsercionTH) {
        this.empresaInsercionTH = empresaInsercionTH;
    }

    public Integer getPromotorEmpInsercionTH() {
        return promotorEmpInsercionTH;
    }

    public void setPromotorEmpInsercionTH(Integer promotorEmpInsercionTH) {
        this.promotorEmpInsercionTH = promotorEmpInsercionTH;
    }

    public Integer getNumTotalEntAgrupacion() {
        return numTotalEntAgrupacion;
    }

    public void setNumTotalEntAgrupacion(Integer numTotalEntAgrupacion) {
        this.numTotalEntAgrupacion = numTotalEntAgrupacion;
    }

    public Long getCodEntidadPadreAgrup() {
        return codEntidadPadreAgrup;
    }

    public void setCodEntidadPadreAgrup(Long codEntidadPadreAgrup) {
        this.codEntidadPadreAgrup = codEntidadPadreAgrup;
    }

    public Double getPorcentaCompromisoRealizacion() {
        return porcentaCompromisoRealizacion;
    }

    public void setPorcentaCompromisoRealizacion(Double porcentaCompromisoRealizacion) {
        this.porcentaCompromisoRealizacion = porcentaCompromisoRealizacion;
    }

    public Integer getPlanIgualdad() {
        return planIgualdad;
    }

    public void setPlanIgualdad(Integer planIgualdad) {
        this.planIgualdad = planIgualdad;
    }
    public Integer getCertificadoCalidad() {
        return certificadoCalidad;
    }

    public void setCertificadoCalidad(Integer certificadoCalidad) {
        this.certificadoCalidad = certificadoCalidad;
    }

    public Integer getAceptaNumeroSuperiorHoras() {
        return aceptaNumeroSuperiorHoras;
    }

    public void setAceptaNumeroSuperiorHoras(Integer aceptaNumeroSuperiorHoras) {
        this.aceptaNumeroSuperiorHoras = aceptaNumeroSuperiorHoras;
    }

    public Integer getSegundosLocalesMismoAmbito() {
        return segundosLocalesMismoAmbito;
    }

    public void setSegundosLocalesMismoAmbito(Integer segundosLocalesMismoAmbito) {
        this.segundosLocalesMismoAmbito = segundosLocalesMismoAmbito;
    }
    
}
