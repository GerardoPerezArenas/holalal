/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.lanbide01.vo;

import java.sql.Timestamp;
import java.util.Date;

/**
 *
 * @author INGDGC
 */
public class Melanbide01DepenPerSut {
    
    private Long id;
    private String numeroExpediente;
    private String correlativo;
    private Integer tipoDependiente;
    private String parentezco;
    private String nombre;
    private String apellidos;
    private String tipoDocumento;
    private String numeroDocumento;
    private Date fechaNacimiento;
    private String esMinusvalido;
    private Double porcentajeMinusvalia;
    private Timestamp fechaHoraRegistro;
    private Timestamp fechaHoraModificacion;
    
    private String tipoDocumentoDesc;
    private String tipoDependienteDesc;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroExpediente() {
        return numeroExpediente;
    }

    public void setNumeroExpediente(String numeroExpediente) {
        this.numeroExpediente = numeroExpediente;
    }

    public String getCorrelativo() {
        return correlativo;
    }

    public void setCorrelativo(String correlativo) {
        this.correlativo = correlativo;
    }

    public Integer getTipoDependiente() {
        return tipoDependiente;
    }

    public void setTipoDependiente(Integer tipoDependiente) {
        this.tipoDependiente = tipoDependiente;
    }

    public String getParentezco() {
        return parentezco;
    }

    public void setParentezco(String parentezco) {
        this.parentezco = parentezco;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getEsMinusvalido() {
        return esMinusvalido;
    }

    public void setEsMinusvalido(String esMinusvalido) {
        this.esMinusvalido = esMinusvalido;
    }

    public Double getPorcentajeMinusvalia() {
        return porcentajeMinusvalia;
    }

    public void setPorcentajeMinusvalia(Double porcentajeMinusvalia) {
        this.porcentajeMinusvalia = porcentajeMinusvalia;
    }

    public Timestamp getFechaHoraRegistro() {
        return fechaHoraRegistro;
    }

    public void setFechaHoraRegistro(Timestamp fechaHoraRegistro) {
        this.fechaHoraRegistro = fechaHoraRegistro;
    }

    public Timestamp getFechaHoraModificacion() {
        return fechaHoraModificacion;
    }

    public void setFechaHoraModificacion(Timestamp fechaHoraModificacion) {
        this.fechaHoraModificacion = fechaHoraModificacion;
    }

    public String getTipoDocumentoDesc() {
        return tipoDocumentoDesc;
    }

    public void setTipoDocumentoDesc(String tipoDocumentoDesc) {
        this.tipoDocumentoDesc = tipoDocumentoDesc;
    }

    public String getTipoDependienteDesc() {
        return tipoDependienteDesc;
    }

    public void setTipoDependienteDesc(String tipoDependienteDesc) {
        this.tipoDependienteDesc = tipoDependienteDesc;
    }
    
    
    
}
