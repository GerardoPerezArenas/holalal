/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide88.vo;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author INGDGC
 */
public class Melanbide88Socios {
    
    private Long id; //  number primary Key,
    private String num_exp; // varchar2(30) not null,
    private String dniNieSocio; // VARCHAR(20) NOT NULL,
    private String nombreSocio; // VARCHAR2(200) NOT NULL,
    private String apellido1Socio; // VARCHAR(200) NOT NULL,
    private String apellido2Socio; // VARCHAR2(200),
    private Date fechaAlta; // timestamp default systimestamp
    
    private final transient  SimpleDateFormat formatFechaddMMyyyy = new SimpleDateFormat("dd/MM/yyyy");
    private final transient  SimpleDateFormat formatFechaLog = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");


    public Melanbide88Socios() {
    }

    public Melanbide88Socios(Long id, String num_exp, String dniNieSocio, String nombreSocio, String apellido1Socio, String apellido2Socio, Date fechaAlta) {
        this.id = id;
        this.num_exp = num_exp;
        this.dniNieSocio = dniNieSocio;
        this.nombreSocio = nombreSocio;
        this.apellido1Socio = apellido1Socio;
        this.apellido2Socio = apellido2Socio;
        this.fechaAlta = fechaAlta;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNum_exp() {
        return num_exp;
    }

    public void setNum_exp(String num_exp) {
        this.num_exp = num_exp;
    }

    public String getDniNieSocio() {
        return dniNieSocio;
    }

    public void setDniNieSocio(String dniNieSocio) {
        this.dniNieSocio = dniNieSocio;
    }

    public String getNombreSocio() {
        return nombreSocio;
    }

    public void setNombreSocio(String nombreSocio) {
        this.nombreSocio = nombreSocio;
    }

    public String getApellido1Socio() {
        return apellido1Socio;
    }

    public void setApellido1Socio(String apellido1Socio) {
        this.apellido1Socio = apellido1Socio;
    }

    public String getApellido2Socio() {
        return apellido2Socio;
    }

    public void setApellido2Socio(String apellido2Socio) {
        this.apellido2Socio = apellido2Socio;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    @Override
    public String toString() {
        return "Melanbide88Socios{" + "id=" + id + ", num_exp=" + num_exp + ", dniNieSocio=" + dniNieSocio + ", nombreSocio=" + nombreSocio + ", apellido1Socio=" + apellido1Socio + ", apellido2Socio=" + apellido2Socio + ", fechaAlta=" + (fechaAlta!=null ? formatFechaLog.format(fechaAlta):"") + '}';
    }
    
}
