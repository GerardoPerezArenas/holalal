/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide48.vo;

/**
 *
 * @author INGDGC
 */
public class ColecEntidadEnAgrupacionVO {

    public ColecEntidadEnAgrupacionVO() {
    }

    public ColecEntidadEnAgrupacionVO(Long codEntidad) {
        this.codEntidad = codEntidad;
    }
    
    private Long codEntidad;
    private Long codEntidadPadreAgrup;
    private String numExp;
    private String tipoCif;
    private String cif;
    private String nombre;
    private Integer centroEspEmpTH;
    private Integer participanteMayorCentEcpEmpTH;
    private Integer empresaInsercionTH;
    private Integer promotorEmpInsercionTH;
    private Double porcentaCompromisoRealizacion;
    private Integer planIgualdad;
    private Integer certificadoCalidad;
    private Integer aceptaNumeroSuperiorHoras;
    private Integer segundosLocalesMismoAmbito;

    private Integer entSujetaDerPubl;
    private Integer compIgualdadOpcion;
    private String compIgualdadOpcionLiteral;
    private Integer entSinAnimoLucro;

    public Long getCodEntidad() {
        return codEntidad;
    }

    public void setCodEntidad(Long codEntidad) {
        this.codEntidad = codEntidad;
    }

    public Long getCodEntidadPadreAgrup() {
        return codEntidadPadreAgrup;
    }

    public void setCodEntidadPadreAgrup(Long codEntidadPadreAgrup) {
        this.codEntidadPadreAgrup = codEntidadPadreAgrup;
    }
    
    public String getNumExp() {
        return numExp;
    }

    public void setNumExp(String numExp) {
        this.numExp = numExp;
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

    public Integer getEntSujetaDerPubl() {
        return entSujetaDerPubl;
    }

    public void setEntSujetaDerPubl(Integer entSujetaDerPubl) {
        this.entSujetaDerPubl = entSujetaDerPubl;
    }

    public Integer getCompIgualdadOpcion() {
        return compIgualdadOpcion;
    }

    public void setCompIgualdadOpcion(Integer compIgualdadOpcion) {
        this.compIgualdadOpcion = compIgualdadOpcion;
    }

    public String getCompIgualdadOpcionLiteral() {
        return compIgualdadOpcionLiteral;
    }

    public void setCompIgualdadOpcionLiteral(String compIgualdadOpcionLiteral) {
        this.compIgualdadOpcionLiteral = compIgualdadOpcionLiteral;
    }

    public Integer getEntSinAnimoLucro() {
        return entSinAnimoLucro;
    }

    public void setEntSinAnimoLucro(Integer entSinAnimoLucro) {
        this.entSinAnimoLucro = entSinAnimoLucro;
    }

    @Override
    public String toString() {
        return "ColecEntidadEnAgrupacionVO{" +
                "codEntidad=" + codEntidad +
                ", codEntidadPadreAgrup=" + codEntidadPadreAgrup +
                ", numExp='" + numExp + '\'' +
                ", tipoCif='" + tipoCif + '\'' +
                ", cif='" + cif + '\'' +
                ", nombre='" + nombre + '\'' +
                ", centroEspEmpTH=" + centroEspEmpTH +
                ", participanteMayorCentEcpEmpTH=" + participanteMayorCentEcpEmpTH +
                ", empresaInsercionTH=" + empresaInsercionTH +
                ", promotorEmpInsercionTH=" + promotorEmpInsercionTH +
                ", porcentaCompromisoRealizacion=" + porcentaCompromisoRealizacion +
                ", planIgualdad=" + planIgualdad +
                ", certificadoCalidad=" + certificadoCalidad +
                ", aceptaNumeroSuperiorHoras=" + aceptaNumeroSuperiorHoras +
                ", segundosLocalesMismoAmbito=" + segundosLocalesMismoAmbito +
                ", entSujetaDerPubl=" + entSujetaDerPubl +
                ", compIgualdadOpcion=" + compIgualdadOpcion +
                ", compIgualdadOpcionLiteral='" + compIgualdadOpcionLiteral + '\'' +
                ", entSinAnimoLucro=" + entSinAnimoLucro +
                '}';
    }
}
