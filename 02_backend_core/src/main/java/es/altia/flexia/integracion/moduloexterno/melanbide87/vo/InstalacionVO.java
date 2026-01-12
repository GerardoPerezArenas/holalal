/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide87.vo;

/**
 *
 * @author kepa
 */
public class InstalacionVO {

    private Integer id;
    private String numExp;
    private String tipoInst;
    private String descTipoInst;
    private String municipio;
    private String localidad;
    private String direccion;
    private Integer codPost;

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

    public String getTipoInst() {
        return tipoInst;
    }

    public void setTipoInst(String tipoInst) {
        this.tipoInst = tipoInst;
    }

    public String getDescTipoInst() {
        return descTipoInst;
    }

    public void setDescTipoInst(String descTipoInst) {
        this.descTipoInst = descTipoInst;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Integer getCodPost() {
        return codPost;
    }

    public void setCodPost(Integer codPost) {
        this.codPost = codPost;
    }
   
}
