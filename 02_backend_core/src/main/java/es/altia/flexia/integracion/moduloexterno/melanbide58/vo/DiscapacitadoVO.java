/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide58.vo;

import java.sql.Date;

/**
 *
 * @author kepa
 */
public class DiscapacitadoVO {

    private Integer id;
    private String dni;
    private String apellidos;
    private String nombre;
    private String tipoDisc;
    private String descTipoDisc;
    private Double porcDisc;
    private Date fechEmision;
    private Date fechResolucion;
    private String validez;
    private String descValidez;
    private Date fechCaducidad;
    private String discSevera;
    private String descDiscSevera;
    private Date fechValidacion;
    private Date fechBaja;
    private String oidCertificado;
    private String nombreCertificado;
    private String centro;
    private String territorio;
    private String descTerritorio;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoDisc() {
        return tipoDisc;
    }

    public void setTipoDisc(String tipoDisc) {
        this.tipoDisc = tipoDisc;
    }

    public String getDescTipoDisc() {
        return descTipoDisc;
    }

    public void setDescTipoDisc(String descTipoDisc) {
        this.descTipoDisc = descTipoDisc;
    }

    public Double getPorcDisc() {
        return porcDisc;
    }

    public void setPorcDisc(Double porcDisc) {
        this.porcDisc = porcDisc;
    }

    public Date getFechEmision() {
        return fechEmision;
    }

    public void setFechEmision(Date fechEmision) {
        this.fechEmision = fechEmision;
    }

    public Date getFechResolucion() {
        return fechResolucion;
    }

    public void setFechResolucion(Date fechResolucion) {
        this.fechResolucion = fechResolucion;
    }

    public String getValidez() {
        return validez;
    }

    public void setValidez(String validez) {
        this.validez = validez;
    }

    public String getDescValidez() {
        return descValidez;
    }

    public void setDescValidez(String descValidez) {
        this.descValidez = descValidez;
    }

    public Date getFechCaducidad() {
        return fechCaducidad;
    }

    public void setFechCaducidad(Date fechCaducidad) {
        this.fechCaducidad = fechCaducidad;
    }

    public String getDiscSevera() {
        return discSevera;
    }

    public void setDiscSevera(String discSevera) {
        this.discSevera = discSevera;
    }

    public String getDescDiscSevera() {
        return descDiscSevera;
    }

    public void setDescDiscSevera(String descDiscSevera) {
        this.descDiscSevera = descDiscSevera;
    }

    public Date getFechValidacion() {
        return fechValidacion;
    }

    public void setFechValidacion(Date fechValidacion) {
        this.fechValidacion = fechValidacion;
    }

    public Date getFechBaja() {
        return fechBaja;
    }

    public void setFechBaja(Date fechBaja) {
        this.fechBaja = fechBaja;
    }

    public String getOidCertificado() {
        return oidCertificado;
    }

    public void setOidCertificado(String oidCertificado) {
        this.oidCertificado = oidCertificado;
    }

    public String getNombreCertificado() {
        return nombreCertificado;
    }

    public void setNombreCertificado(String nombreCertificado) {
        this.nombreCertificado = nombreCertificado;
    }

    public String getCentro() {
        return centro;
    }

    public void setCentro(String centro) {
        this.centro = centro;
    }

    public String getTerritorio() {
        return territorio;
    }

    public void setTerritorio(String territorio) {
        this.territorio = territorio;
    }

    public String getDescTerritorio() {
        return descTerritorio;
    }

    public void setDescTerritorio(String descTerritorio) {
        this.descTerritorio = descTerritorio;
    }

}
