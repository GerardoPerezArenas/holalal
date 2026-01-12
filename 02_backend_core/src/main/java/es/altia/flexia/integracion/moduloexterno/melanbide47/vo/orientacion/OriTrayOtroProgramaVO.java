/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide47.vo.orientacion;

import java.util.Date;

/**
 *
 * @author Kepa
 */
public class OriTrayOtroProgramaVO {
    private Integer codIdOtroPrograma;  //0
    private Integer ejercicio;  //1
    private String numExpediente;   //2
    private Integer codEntidad; //3
    private String programa;    //4
    private Integer anioPrograma;   //5
    private Integer duracion;   //6
    private Integer anioProgramaVal;    //7
    private Integer duracionVal;    //8
    //Información Adicional de la entidad
    private String cifEntidad;  //9
    private String nombreEntidad;   //10

    public Integer getCodIdOtroPrograma() {
        return codIdOtroPrograma;
    }

    public void setCodIdOtroPrograma(Integer codIdOtroPrograma) {
        this.codIdOtroPrograma = codIdOtroPrograma;
    }

    public Integer getEjercicio() {
        return ejercicio;
    }

    public void setEjercicio(Integer ejercicio) {
        this.ejercicio = ejercicio;
    }

    public String getNumExpediente() {
        return numExpediente;
    }

    public void setNumExpediente(String numExpediente) {
        this.numExpediente = numExpediente;
    }

    public Integer getCodEntidad() {
        return codEntidad;
    }

    public void setCodEntidad(Integer codEntidad) {
        this.codEntidad = codEntidad;
    }

    public String getPrograma() {
        return programa;
    }

    public void setPrograma(String programa) {
        this.programa = programa;
    }

    public Integer getAnioPrograma() {
        return anioPrograma;
    }

    public void setAnioPrograma(Integer anioPrograma) {
        this.anioPrograma = anioPrograma;
    }

    public Integer getDuracion() {
        return duracion;
    }

    public void setDuracion(Integer duracion) {
        this.duracion = duracion;
    }

    public Integer getAnioProgramaVal() {
        return anioProgramaVal;
    }

    public void setAnioProgramaVal(Integer anioProgramaVal) {
        this.anioProgramaVal = anioProgramaVal;
    }

    public Integer getDuracionVal() {
        return duracionVal;
    }

    public void setDuracionVal(Integer duracionVal) {
        this.duracionVal = duracionVal;
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
