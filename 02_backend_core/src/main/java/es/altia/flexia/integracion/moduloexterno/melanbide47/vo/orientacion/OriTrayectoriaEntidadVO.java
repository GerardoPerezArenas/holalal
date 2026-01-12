/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide47.vo.orientacion;

import java.sql.Date;

/**
 *
 * @author kepa
 */
public class OriTrayectoriaEntidadVO {

    private Integer idTrayEntidad;
    private Integer idConActGrupo;
    private Integer idConActSubgrupo;
    private String numExpediente;
    private String codEntidad;
    private String descActividad;
    private Integer tieneExperiencia;
    private Date fechaInicio;
    private Date fechaFin;
    private Integer numMeses;
    private Integer tieneExperienciaVal;
    private Date fechaInicioVal;
    private Date fechaFinVal;
    private Integer numMesesVal;
    private Date fechaAlta;
    private Date fechaModificacion;
    private Date fechaValidacion;
    //Información Adicional de la entidad
    private String cifEntidad;
    private String nombreEntidad;

    public Integer getIdTrayEntidad() {
        return idTrayEntidad;
    }

    public void setIdTrayEntidad(Integer idTrayEntidad) {
        this.idTrayEntidad = idTrayEntidad;
    }

    public Integer getIdConActGrupo() {
        return idConActGrupo;
    }

    public void setIdConActGrupo(Integer idConActGrupo) {
        this.idConActGrupo = idConActGrupo;
    }

    public Integer getIdConActSubgrupo() {
        return idConActSubgrupo;
    }

    public void setIdConActSubgrupo(Integer idConActSubgrupo) {
        this.idConActSubgrupo = idConActSubgrupo;
    }

    public String getNumExpediente() {
        return numExpediente;
    }

    public void setNumExpediente(String numExpediente) {
        this.numExpediente = numExpediente;
    }

    public String getCodEntidad() {
        return codEntidad;
    }

    public void setCodEntidad(String codEntidad) {
        this.codEntidad = codEntidad;
    }

    public String getDescActividad() {
        return descActividad;
    }

    public void setDescActividad(String descActividad) {
        this.descActividad = descActividad;
    }

    public Integer getTieneExperiencia() {
        return tieneExperiencia;
    }

    public void setTieneExperiencia(Integer tieneExperiencia) {
        this.tieneExperiencia = tieneExperiencia;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Integer getNumMeses() {
        return numMeses;
    }

    public void setNumMeses(Integer numMeses) {
        this.numMeses = numMeses;
    }

    public Integer getTieneExperienciaVal() {
        return tieneExperienciaVal;
    }

    public void setTieneExperienciaVal(Integer tieneExperienciaVal) {
        this.tieneExperienciaVal = tieneExperienciaVal;
    }

    public Date getFechaInicioVal() {
        return fechaInicioVal;
    }

    public void setFechaInicioVal(Date fechaInicioVal) {
        this.fechaInicioVal = fechaInicioVal;
    }

    public Date getFechaFinVal() {
        return fechaFinVal;
    }

    public void setFechaFinVal(Date fechaFinVal) {
        this.fechaFinVal = fechaFinVal;
    }

    public Integer getNumMesesVal() {
        return numMesesVal;
    }

    public void setNumMesesVal(Integer numMesesVal) {
        this.numMesesVal = numMesesVal;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public Date getFechaValidacion() {
        return fechaValidacion;
    }

    public void setFechaValidacion(Date fechaValidacion) {
        this.fechaValidacion = fechaValidacion;
    }

    public String getCifEntidad() {
        return cifEntidad;
    }

    public void setCifEntidad(String cifEntidad) {
        this.cifEntidad = cifEntidad;
    }

    public String getNombreEntidad() {
        return nombreEntidad;
    }

    public void setNombreEntidad(String nombreEntidad) {
        this.nombreEntidad = nombreEntidad;
    }

}
