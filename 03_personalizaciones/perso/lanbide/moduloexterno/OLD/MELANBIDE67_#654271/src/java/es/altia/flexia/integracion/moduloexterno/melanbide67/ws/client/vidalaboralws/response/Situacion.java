/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.altia.flexia.integracion.moduloexterno.melanbide67.ws.client.vidalaboralws.response;

/**
 *
 * @author pablo.bugia
 */
public class Situacion {
    private String numeroAfiliacion;
    private String regimen;
    private String empresa;
    private String codigoCuentaCotizacion;
    private String provincia;
    private String fechaAlta;
    private String fechaEfectos;
    private String fechaBaja;
    private String contratoTrabajo;
    private String contratoTiempoParcial;
    private String grupoCotizacion;
    private int diasAlta;

    public Situacion(String numeroAfiliacion, String regimen, String empresa, String codigoCuentaCotizacion, String provincia, String fechaAlta, String fechaEfectos, String fechaBaja, String contratoTrabajo, String contratoTiempoParcial, String grupoCotizacion, int diasAlta) {
        this.numeroAfiliacion = numeroAfiliacion;
        this.regimen = regimen;
        this.empresa = empresa;
        this.codigoCuentaCotizacion = codigoCuentaCotizacion;
        this.provincia = provincia;
        this.fechaAlta = fechaAlta;
        this.fechaEfectos = fechaEfectos;
        this.fechaBaja = fechaBaja;
        this.contratoTrabajo = contratoTrabajo;
        this.contratoTiempoParcial = contratoTiempoParcial;
        this.grupoCotizacion = grupoCotizacion;
        this.diasAlta = diasAlta;
    }

    public String getNumeroAfiliacion() {
        return numeroAfiliacion;
    }

    public void setNumeroAfiliacion(String numeroAfiliacion) {
        this.numeroAfiliacion = numeroAfiliacion;
    }

    public String getRegimen() {
        return regimen;
    }

    public void setRegimen(String regimen) {
        this.regimen = regimen;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getCodigoCuentaCotizacion() {
        return codigoCuentaCotizacion;
    }

    public void setCodigoCuentaCotizacion(String codigoCuentaCotizacion) {
        this.codigoCuentaCotizacion = codigoCuentaCotizacion;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(String fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public String getFechaEfectos() {
        return fechaEfectos;
    }

    public void setFechaEfectos(String fechaEfectos) {
        this.fechaEfectos = fechaEfectos;
    }

    public String getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(String fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public String getContratoTrabajo() {
        return contratoTrabajo;
    }

    public void setContratoTrabajo(String contratoTrabajo) {
        this.contratoTrabajo = contratoTrabajo;
    }

    public String getContratoTiempoParcial() {
        return contratoTiempoParcial;
    }

    public void setContratoTiempoParcial(String contratoTiempoParcial) {
        this.contratoTiempoParcial = contratoTiempoParcial;
    }

    public String getGrupoCotizacion() {
        return grupoCotizacion;
    }

    public void setGrupoCotizacion(String grupoCotizacion) {
        this.grupoCotizacion = grupoCotizacion;
    }

    public int getDiasAlta() {
        return diasAlta;
    }

    public void setDiasAlta(int diasAlta) {
        this.diasAlta = diasAlta;
    }

    @Override
    public String toString() {
        return "Situacion{" + "numeroAfiliacion=" + numeroAfiliacion + ", regimen=" + regimen + ", empresa=" + empresa + ", codigoCuentaCotizacion=" + codigoCuentaCotizacion + ", provincia=" + provincia + ", fechaAlta=" + fechaAlta + ", fechaEfectos=" + fechaEfectos + ", fechaBaja=" + fechaBaja + ", contratoTrabajo=" + contratoTrabajo + ", contratoTiempoParcial=" + contratoTiempoParcial + ", grupoCotizacion=" + grupoCotizacion + ", diasAlta=" + diasAlta + '}';
    }
}
