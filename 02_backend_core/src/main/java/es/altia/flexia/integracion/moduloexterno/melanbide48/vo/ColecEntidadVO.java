/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide48.vo;

/**
 *
 * @author santiagoc
 */
public class ColecEntidadVO 
{
    private Long codEntidad;
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
    private Integer planIgualdad;
    private Integer certificadoCalidad;
    private Integer aceptaNumeroSuperiorHoras;
    private Integer segundosLocalesMismoAmbito;
    private Double trayNumeroMesesValidados;
    private Integer esAgrupacionValidado;
    private Integer centroEspEmpTHValidado;
    private Integer participanteMayorCentEcpEmpTHValidado;
    private Integer empresaInsercionTHValidado;
    private Integer promotorEmpInsercionTHValidado;
    private Integer planIgualdadValidado;
    private Integer certificadoCalidadValidado;
    private Integer segundosLocalesMismoAmbitoValidado;
    private Integer entSujetaDerPubl;
    private Integer entSujetaDerPublVal;
    private Integer compIgualdadOpcion;
    private String compIgualdadOpcionLiteral;
    private Integer compIgualdadOpcionVal;
    private String compIgualdadOpcionValLiteral;
    private Integer entSinAnimoLucro;
    private Integer entSinAnimoLucroVal;

    public ColecEntidadVO(){}

    public ColecEntidadVO(Long codEntidad) {
        this.codEntidad = codEntidad;
    }

    public ColecEntidadVO(Long codEntidad, String numExp) {
        this.codEntidad = codEntidad;
        this.numExp = numExp;
    }

    public ColecEntidadVO(Long codEntidad, Integer ejercicio, String numExp, Integer anioConvocatoria, Integer esAgrupacion, String tipoCif, String cif, String nombre, Integer centroEspEmpTH, Integer participanteMayorCentEcpEmpTH, Integer empresaInsercionTH, Integer promotorEmpInsercionTH, Integer numTotalEntAgrupacion, Integer planIgualdad, Integer certificadoCalidad, Integer aceptaNumeroSuperiorHoras, Integer segundosLocalesMismoAmbito, Double trayNumeroMesesValidados, Integer esAgrupacionValidado, Integer centroEspEmpTHValidado, Integer participanteMayorCentEcpEmpTHValidado, Integer empresaInsercionTHValidado, Integer promotorEmpInsercionTHValidado, Integer planIgualdadValidado, Integer certificadoCalidadValidado, Integer segundosLocalesMismoAmbitoValidado, Integer entSujetaDerPubl, Integer entSujetaDerPublVal, Integer compIgualdadOpcion, String compIgualdadOpcionLiteral, Integer compIgualdadOpcionVal, String compIgualdadOpcionValLiteral, Integer entSinAnimoLucro, Integer entSinAnimoLucroVal) {
        this.codEntidad = codEntidad;
        this.ejercicio = ejercicio;
        this.numExp = numExp;
        this.anioConvocatoria = anioConvocatoria;
        this.esAgrupacion = esAgrupacion;
        this.tipoCif = tipoCif;
        this.cif = cif;
        this.nombre = nombre;
        this.centroEspEmpTH = centroEspEmpTH;
        this.participanteMayorCentEcpEmpTH = participanteMayorCentEcpEmpTH;
        this.empresaInsercionTH = empresaInsercionTH;
        this.promotorEmpInsercionTH = promotorEmpInsercionTH;
        this.numTotalEntAgrupacion = numTotalEntAgrupacion;
        this.planIgualdad = planIgualdad;
        this.certificadoCalidad = certificadoCalidad;
        this.aceptaNumeroSuperiorHoras = aceptaNumeroSuperiorHoras;
        this.segundosLocalesMismoAmbito = segundosLocalesMismoAmbito;
        this.trayNumeroMesesValidados = trayNumeroMesesValidados;
        this.esAgrupacionValidado = esAgrupacionValidado;
        this.centroEspEmpTHValidado = centroEspEmpTHValidado;
        this.participanteMayorCentEcpEmpTHValidado = participanteMayorCentEcpEmpTHValidado;
        this.empresaInsercionTHValidado = empresaInsercionTHValidado;
        this.promotorEmpInsercionTHValidado = promotorEmpInsercionTHValidado;
        this.planIgualdadValidado = planIgualdadValidado;
        this.certificadoCalidadValidado = certificadoCalidadValidado;
        this.segundosLocalesMismoAmbitoValidado = segundosLocalesMismoAmbitoValidado;
        this.entSujetaDerPubl = entSujetaDerPubl;
        this.entSujetaDerPublVal = entSujetaDerPublVal;
        this.compIgualdadOpcion = compIgualdadOpcion;
        this.compIgualdadOpcionLiteral = compIgualdadOpcionLiteral;
        this.compIgualdadOpcionVal = compIgualdadOpcionVal;
        this.compIgualdadOpcionValLiteral = compIgualdadOpcionValLiteral;
        this.entSinAnimoLucro = entSinAnimoLucro;
        this.entSinAnimoLucroVal = entSinAnimoLucroVal;
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

    public Double getTrayNumeroMesesValidados() {
        return trayNumeroMesesValidados;
    }

    public void setTrayNumeroMesesValidados(Double trayNumeroMesesValidados) {
        this.trayNumeroMesesValidados = trayNumeroMesesValidados;
    }

    public Integer getEsAgrupacionValidado() {
        return esAgrupacionValidado;
    }

    public void setEsAgrupacionValidado(Integer esAgrupacionValidado) {
        this.esAgrupacionValidado = esAgrupacionValidado;
    }

    public Integer getCentroEspEmpTHValidado() {
        return centroEspEmpTHValidado;
    }

    public void setCentroEspEmpTHValidado(Integer centroEspEmpTHValidado) {
        this.centroEspEmpTHValidado = centroEspEmpTHValidado;
    }

    public Integer getParticipanteMayorCentEcpEmpTHValidado() {
        return participanteMayorCentEcpEmpTHValidado;
    }

    public void setParticipanteMayorCentEcpEmpTHValidado(Integer participanteMayorCentEcpEmpTHValidado) {
        this.participanteMayorCentEcpEmpTHValidado = participanteMayorCentEcpEmpTHValidado;
    }

    public Integer getEmpresaInsercionTHValidado() {
        return empresaInsercionTHValidado;
    }

    public void setEmpresaInsercionTHValidado(Integer empresaInsercionTHValidado) {
        this.empresaInsercionTHValidado = empresaInsercionTHValidado;
    }

    public Integer getPromotorEmpInsercionTHValidado() {
        return promotorEmpInsercionTHValidado;
    }

    public void setPromotorEmpInsercionTHValidado(Integer promotorEmpInsercionTHValidado) {
        this.promotorEmpInsercionTHValidado = promotorEmpInsercionTHValidado;
    }

    public Integer getPlanIgualdadValidado() {
        return planIgualdadValidado;
    }

    public void setPlanIgualdadValidado(Integer planIgualdadValidado) {
        this.planIgualdadValidado = planIgualdadValidado;
    }

    public Integer getCertificadoCalidadValidado() {
        return certificadoCalidadValidado;
    }

    public void setCertificadoCalidadValidado(Integer certificadoCalidadValidado) {
        this.certificadoCalidadValidado = certificadoCalidadValidado;
    }

    public Integer getSegundosLocalesMismoAmbitoValidado() {
        return segundosLocalesMismoAmbitoValidado;
    }

    public void setSegundosLocalesMismoAmbitoValidado(Integer segundosLocalesMismoAmbitoValidado) {
        this.segundosLocalesMismoAmbitoValidado = segundosLocalesMismoAmbitoValidado;
    }

    public Integer getEntSujetaDerPubl() {
        return entSujetaDerPubl;
    }

    public void setEntSujetaDerPubl(Integer entSujetaDerPubl) {
        this.entSujetaDerPubl = entSujetaDerPubl;
    }

    public Integer getEntSujetaDerPublVal() {
        return entSujetaDerPublVal;
    }

    public void setEntSujetaDerPublVal(Integer entSujetaDerPublVal) {
        this.entSujetaDerPublVal = entSujetaDerPublVal;
    }

    public Integer getCompIgualdadOpcion() {
        return compIgualdadOpcion;
    }

    public void setCompIgualdadOpcion(Integer compIgualdadOpcion) {
        this.compIgualdadOpcion = compIgualdadOpcion;
    }

    public Integer getCompIgualdadOpcionVal() {
        return compIgualdadOpcionVal;
    }

    public void setCompIgualdadOpcionVal(Integer compIgualdadOpcionVal) {
        this.compIgualdadOpcionVal = compIgualdadOpcionVal;
    }

    public String getCompIgualdadOpcionLiteral() {
        return compIgualdadOpcionLiteral;
    }

    public void setCompIgualdadOpcionLiteral(String compIgualdadOpcionLiteral) {
        this.compIgualdadOpcionLiteral = compIgualdadOpcionLiteral;
    }

    public String getCompIgualdadOpcionValLiteral() {
        return compIgualdadOpcionValLiteral;
    }

    public void setCompIgualdadOpcionValLiteral(String compIgualdadOpcionValLiteral) {
        this.compIgualdadOpcionValLiteral = compIgualdadOpcionValLiteral;
    }

    public Integer getEntSinAnimoLucro() {
        return entSinAnimoLucro;
    }

    public void setEntSinAnimoLucro(Integer entSinAnimoLucro) {
        this.entSinAnimoLucro = entSinAnimoLucro;
    }

    public Integer getEntSinAnimoLucroVal() {
        return entSinAnimoLucroVal;
    }

    public void setEntSinAnimoLucroVal(Integer entSinAnimoLucroVal) {
        this.entSinAnimoLucroVal = entSinAnimoLucroVal;
    }
}
