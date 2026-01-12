/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author Laura
 */
public class EcaSegPreparadoresVO 
{
    private Integer segPreparadoresCod;
    private Integer jusPreparadoresCod;
    private Integer tipo;
    private String nif;
    private String nifPreparador;
    private String nombre;
    private Date fecNacimiento;
    private Integer sexo;
    private Integer tipoDiscapacidad;
    private Integer gravedad;
    private Integer tipoContrato;
    private BigDecimal porcJornada;
    private Date fecIni;
    private Date fecFin;
    private String empresa;
    private BigDecimal horasCont;
    private Date fecSeguimiento;
    //private String nomPersContacto;
    private String observaciones;
    private Integer finContratoDespido;
    
    public EcaSegPreparadoresVO()
    {
        
    }

    public Integer getSegPreparadoresCod() {
        return segPreparadoresCod;
    }

    public void setSegPreparadoresCod(Integer segPreparadoresCod) {
        this.segPreparadoresCod = segPreparadoresCod;
    }

    public Integer getJusPreparadoresCod() {
        return jusPreparadoresCod;
    }

    public void setJusPreparadoresCod(Integer jusPreparadoresCod) {
        this.jusPreparadoresCod = jusPreparadoresCod;
    }

    
    public String getNifPreparador() {
        return nifPreparador;
    }

    public void setNifPreparador(String nifPreparador) {
        this.nifPreparador = nifPreparador;
    }
    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFecNacimiento() {
        return fecNacimiento;
    }

    public void setFecNacimiento(Date fecNacimiento) {
        this.fecNacimiento = fecNacimiento;
    }

    public Integer getSexo() {
        return sexo;
    }

    public void setSexo(Integer sexo) {
        this.sexo = sexo;
    }

    public Integer getTipoDiscapacidad() {
        return tipoDiscapacidad;
    }

    public void setTipoDiscapacidad(Integer tipoDiscapacidad) {
        this.tipoDiscapacidad = tipoDiscapacidad;
    }

    public Integer getGravedad() {
        return gravedad;
    }

    public void setGravedad(Integer gravedad) {
        this.gravedad = gravedad;
    }

    public Integer getTipoContrato() {
        return tipoContrato;
    }

    public void setTipoContrato(Integer tipoContrato) {
        this.tipoContrato = tipoContrato;
    }

    public BigDecimal getPorcJornada() {
        return porcJornada;
    }

    public void setPorcJornada(BigDecimal porcJornada) {
        this.porcJornada = porcJornada;
    }

    public Date getFecIni() {
        return fecIni;
    }

    public void setFecIni(Date fecIni) {
        this.fecIni = fecIni;
    }

    public Date getFecFin() {
        return fecFin;
    }

    public void setFecFin(Date fecFin) {
        this.fecFin = fecFin;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public BigDecimal getHorasCont() {
        return horasCont;
    }

    public void setHorasCont(BigDecimal horasCont) {
        this.horasCont = horasCont;
    }
    
    public Date getFecSeguimiento() {
        return fecSeguimiento;
    }

    public void setFecSeguimiento(Date fecSeguimiento) {
        this.fecSeguimiento = fecSeguimiento;
    }

    /*public String getNomPersContacto() {
        return nomPersContacto;
    }

    public void setNomPersContacto(String nomPersContacto) {
        this.nomPersContacto = nomPersContacto;
    }*/

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Integer getFinContratoDespido() {
        return finContratoDespido;
    }

    public void setFinContratoDespido(Integer finContratoDespido) {
        this.finContratoDespido = finContratoDespido;
    }
}
