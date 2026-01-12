/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide51.vo;

import java.sql.Date;
import java.sql.Time;

/**
 *
 * @author davidg
 */
public class ControlAccesoVO {
    
    private Integer id;
    private Date fecha;
    private Date fechaIV;
    private Date fechaFV;
    private String no_tarjeta;
    private String nif_dni;
    private String nombre;
    private String apellido1;
    private String apellido2;
    private String telefono;
    private String empresa_entidad;
    private String servicio_visitado;
    private String persona_contacto;
    private String cod_mot_visita;
    private String des_mot_visita;
    private Time hora_entrada;
    private Time hora_salida;
    private String observaciones; 

    public Date getFechaIV() {
        return fechaIV;
    }

    public void setFechaIV(Date fechaIV) {
        this.fechaIV = fechaIV;
    }

    public Date getFechaFV() {
        return fechaFV;
    }

    public void setFechaFV(Date fechaFV) {
        this.fechaFV = fechaFV;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getNo_tarjeta() {
        return no_tarjeta;
    }

    public void setNo_tarjeta(String no_tarjeta) {
        this.no_tarjeta = no_tarjeta;
    }

    public String getNif_Dni() {
        return nif_dni;
    }

    public void setNif_Dni(String nif_dni) {
        this.nif_dni = nif_dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmpresa_entidad() {
        return empresa_entidad;
    }

    public void setEmpresa_entidad(String empresa_entidad) {
        this.empresa_entidad = empresa_entidad;
    }

    public String getServicio_visitado() {
        return servicio_visitado;
    }

    public void setServicio_visitado(String servicio_visitado) {
        this.servicio_visitado = servicio_visitado;
    }

    public String getPersona_contacto() {
        return persona_contacto;
    }

    public void setPersona_contacto(String persona_contacto) {
        this.persona_contacto = persona_contacto;
    }

    public String getCod_mot_visita() {
        return cod_mot_visita;
    }

    public void setCod_mot_visita(String cod_mot_visita) {
        this.cod_mot_visita = cod_mot_visita;
    }

    public String getDes_mot_visita() {
        return des_mot_visita;
    }

    public void setDes_mot_visita(String des_mot_visita) {
        this.des_mot_visita = des_mot_visita;
    }

    public Time getHora_entrada() {
        return hora_entrada;
    }

    public void setHora_entrada(Time hora_entrada) {
        this.hora_entrada = hora_entrada;
    }

    public Time getHora_salida() {
        return hora_salida;
    }

    public void setHora_salida(Time hora_salida) {
        this.hora_salida = hora_salida;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    
}
