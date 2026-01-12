/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide53.vo;

/**
 *
 * @author Ixone
 */
public class DetalleErrorVO {
  
    private String id;//IDEN_ERR_ID        
    private String descripcionCorta;//IDEN_ERR_DESC_C    
    private String descripcion;//IDEN_ERR_DESC      
    private String tipo;//IDEN_ERR_TIPO        
    private String desTipo;
    private String critico;
    private String desCritico;//IDEN_ERR_CRIT      
    private String aviso;//IDEN_ERR_MEN_AVISO 
    private String mails;//IDEN_ERR_MAILS     
    private String acciones;//IDEN_ERR_ACC     
    private String modulo;//IDEN_ERR_MOD     
    // Numero de registros obtenidos en las consultas -- se pone para recuperar datos de la paginacio
    private Integer noTotalRegConsulta;
    private Integer noRegEnLaConsulta;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescripcionCorta() {
        return descripcionCorta;
    }

    public void setDescripcionCorta(String descripcionCorta) {
        this.descripcionCorta = descripcionCorta;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDesTipo() {
        return desTipo;
    }

    public void setDesTipo(String desTipo) {
        this.desTipo = desTipo;
    }

    public String getCritico() {
        return critico;
    }

    public void setCritico(String critico) {
        this.critico = critico;
    }

    public String getDesCritico() {
        return desCritico;
    }

    public void setDesCritico(String desCritico) {
        this.desCritico = desCritico;
    }

    public String getAviso() {
        return aviso;
    }

    public void setAviso(String aviso) {
        this.aviso = aviso;
    }

    public String getMails() {
        return mails;
    }

    public void setMails(String mails) {
        this.mails = mails;
    }

    public String getAcciones() {
        return acciones;
    }

    public void setAcciones(String acciones) {
        this.acciones = acciones;
    }

    public String getModulo() {
        return modulo;
    }

    public void setModulo(String modulo) {
        this.modulo = modulo;
    }
    
    public Integer getNoTotalRegConsulta() {
        return noTotalRegConsulta;
    }

    public void setNoTotalRegConsulta(Integer noTotalRegConsulta) {
        this.noTotalRegConsulta = noTotalRegConsulta;
    }

    public Integer getNoRegEnLaConsulta() {
        return noRegEnLaConsulta;
    }

    public void setNoRegEnLaConsulta(Integer noRegEnLaConsulta) {
        this.noRegEnLaConsulta = noRegEnLaConsulta;
    }
    
}
