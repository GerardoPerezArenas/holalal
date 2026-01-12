/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide58.vo;

import java.sql.Date;


/**
 *
 * @author altia
 */
public class AltaVO {
    
    private Integer id;
    private String numExp;
    private String nombre;
    private String apellidos;
    private Date fechaAlta;
    private String numSS;
    private String nif;
    private Integer numLinea;
    private String causa;
    private String causaDesc;

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

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public String getNumSS() {
        return numSS;
    }

    public void setNumSS(String numSS) {
        this.numSS = numSS;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }
    
    public String getCausa() {
        return causa;
    }

    public void setCausa(String causa) {
        this.causa = causa;
    }
    
    public String getCausaDesc() {
        return causaDesc;
    }

    public void setCausaDesc(String causaDesc) {
        this.causaDesc = causaDesc;
    }

    public Integer getNumLinea() {
        return numLinea;
    }

    public void setNumLinea(Integer numLinea) {
        this.numLinea = numLinea;
    }

}
