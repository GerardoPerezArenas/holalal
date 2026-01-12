
package es.altia.flexia.integracion.moduloexterno.melanbide80.vo;

import java.sql.Date;

/*
    6. DATOS PERSONALES E INFORMACIÓN SOBRE EL CONTRATO DE TRABAJO, LA SITUACIÓN SUBVENCIONABLE, EL COMPLEMENTO SALARIAL Y EL IMPORTE SOLICITADO POR PERSONA CON DISCAPACIDAD Y/O EN RIESGO DE EXCLUSIÓN DE LA ENTIDAD BENEFICIARIA.

DNI
NOMBRE
APELLIDO 1
APELLIDO 2
TIPO DE CONTRATO
JORNADA
SITUACION
% REDUCCION DE JORNADA ERTE
FECHA INICIO SITUACIÓN
FECHA FIN SITUACIÓN
Nº DIAS SITUACION
BASE REGULADORA DIARIA
IMPORTE PRESTACION
COMPLEMENTO SALARIAL EMPRESA
IMPORTE SUBVENCION SOLICITADA
*/
public class PersonaVO {
    
    private Integer id;
    private String numExp;
    
    private String dni;
    private String nombre;
    private String apel1;
    private String apel2;
    
    private String tipcontA; //desplegable CTRA
    private String desTipcontA;
    
    private Double tipcontB;
    
    private String jornada; //desplegable JORN
    private String desJornada;
    
    private Double porjorpar;
    
    private String situacion; //desplegable ERTE
    private String desSituacion;
    
    private Double reducjorn;
    private Date fecinisit;
    private Date fecfinsit;
    private Integer numdiasit;
    private Double baseregul;
    private Double impprest;
    private Double complsal;
    private Double impsubvsol;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumExp() {
        return numExp;
    }

    public void setNumExp(String numExp) {
        this.numExp = numExp;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApel1() {
        return apel1;
    }

    public void setApel1(String apel1) {
        this.apel1 = apel1;
    }

    public String getApel2() {
        return apel2;
    }

    public void setApel2(String apel2) {
        this.apel2 = apel2;
    }

    public String getTipcontA() {
        return tipcontA;
    }

    public void setTipcontA(String tipcontA) {
        this.tipcontA = tipcontA;
    }

    public String getDesTipcontA() {
        return desTipcontA;
    }

    public void setDesTipcontA(String desTipcontA) {
        this.desTipcontA = desTipcontA;
    }

    public Double getTipcontB() {
        return tipcontB;
    }

    public void setTipcontB(Double tipcontB) {
        this.tipcontB = tipcontB;
    }

    public String getJornada() {
        return jornada;
    }

    public void setJornada(String jornada) {
        this.jornada = jornada;
    }

    public String getDesJornada() {
        return desJornada;
    }

    public void setDesJornada(String desJornada) {
        this.desJornada = desJornada;
    }

    public Double getPorjorpar() {
        return porjorpar;
    }

    public void setPorjorpar(Double porjorpar) {
        this.porjorpar = porjorpar;
    }

    public String getSituacion() {
        return situacion;
    }

    public void setSituacion(String situacion) {
        this.situacion = situacion;
    }

    public String getDesSituacion() {
        return desSituacion;
    }

    public void setDesSituacion(String desSituacion) {
        this.desSituacion = desSituacion;
    }

    public Double getReducjorn() {
        return reducjorn;
    }

    public void setReducjorn(Double reducjorn) {
        this.reducjorn = reducjorn;
    }

    public Date getFecinisit() {
        return fecinisit;
    }

    public void setFecinisit(Date fecinisit) {
        this.fecinisit = fecinisit;
    }

    public Date getFecfinsit() {
        return fecfinsit;
    }

    public void setFecfinsit(Date fecfinsit) {
        this.fecfinsit = fecfinsit;
    }

    public Integer getNumdiasit() {
        return numdiasit;
    }

    public void setNumdiasit(Integer numdiasit) {
        this.numdiasit = numdiasit;
    }

    public Double getBaseregul() {
        return baseregul;
    }

    public void setBaseregul(Double baseregul) {
        this.baseregul = baseregul;
    }

    public Double getImpprest() {
        return impprest;
    }

    public void setImpprest(Double impprest) {
        this.impprest = impprest;
    }

    public Double getComplsal() {
        return complsal;
    }

    public void setComplsal(Double complsal) {
        this.complsal = complsal;
    }

    public Double getImpsubvsol() {
        return impsubvsol;
    }

    public void setImpsubvsol(Double impsubvsol) {
        this.impsubvsol = impsubvsol;
    }

}
