/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide48.vo;

/**
 *
 * @author INGDGC
 */
public class ColecUbicacionesCTVO {
    
    
    private Long codId;
    private String numExpediente;
    private String codTipoColectivo;
    private Long codEntidad;
    private Integer territorioHist;
    private Integer comarca;
    private Integer fkIdAmbitoSolicitado; // Relacion de desplegales Ambitos/Municipio desde 2021
    private Integer municipio;
    private String localidad;
    private String  direccion;
    private String  direccionPortal;
    private String  direccionPiso;
    private String  direccionLetra;
    private String  codigoPostal;
    private String  telefono;
    
    // Añadimos campos informacion Extra
    private String cifEntidad;
    private String nombreEntidad; 
    private String descColectivo;
    private String descTerritorioHist;
    private String descComarca;
    private String descAmbitoSolicitado;
    private String descMunicipio;
    
    private Integer localesPreviamenteAprobados;
    private Integer mantieneRequisitosLocalesAprob;
    private Integer disponeEspacioComplWifi;

    public ColecUbicacionesCTVO() {
    }

    public ColecUbicacionesCTVO(Long codId, String numExpediente) {
        this.codId = codId;
        this.numExpediente = numExpediente;
    }
    
    public Long getCodId() {
        return codId;
    }

    public void setCodId(Long codId) {
        this.codId = codId;
    }

    public String getNumExpediente() {
        return numExpediente;
    }

    public void setNumExpediente(String numExpediente) {
        this.numExpediente = numExpediente;
    }
    
    

    public String getCodTipoColectivo() {
        return codTipoColectivo;
    }

    public void setCodTipoColectivo(String codTipoColectivo) {
        this.codTipoColectivo = codTipoColectivo;
    }

    public Long getCodEntidad() {
        return codEntidad;
    }

    public void setCodEntidad(Long codEntidad) {
        this.codEntidad = codEntidad;
    }

    public Integer getTerritorioHist() {
        return territorioHist;
    }

    public void setTerritorioHist(Integer territorioHist) {
        this.territorioHist = territorioHist;
    }

    public Integer getComarca() {
        return comarca;
    }

    public void setComarca(Integer comarca) {
        this.comarca = comarca;
    }

    public Integer getMunicipio() {
        return municipio;
    }

    public void setMunicipio(Integer municipio) {
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

    public String getDireccionPortal() {
        return direccionPortal;
    }

    public void setDireccionPortal(String direccionPortal) {
        this.direccionPortal = direccionPortal;
    }

    public String getDireccionPiso() {
        return direccionPiso;
    }

    public void setDireccionPiso(String direccionPiso) {
        this.direccionPiso = direccionPiso;
    }

    public String getDireccionLetra() {
        return direccionLetra;
    }

    public void setDireccionLetra(String direccionLetra) {
        this.direccionLetra = direccionLetra;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
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

    public String getDescTerritorioHist() {
        return descTerritorioHist;
    }

    public void setDescTerritorioHist(String descTerritorioHist) {
        this.descTerritorioHist = descTerritorioHist;
    }

    public String getDescComarca() {
        return descComarca;
    }

    public void setDescComarca(String descComarca) {
        this.descComarca = descComarca;
    }

    public String getDescMunicipio() {
        return descMunicipio;
    }

    public void setDescMunicipio(String descMunicipio) {
        this.descMunicipio = descMunicipio;
    }

    public Integer getLocalesPreviamenteAprobados() {
        return localesPreviamenteAprobados;
    }

    public void setLocalesPreviamenteAprobados(Integer localesPreviamenteAprobados) {
        this.localesPreviamenteAprobados = localesPreviamenteAprobados;
    }

    public Integer getMantieneRequisitosLocalesAprob() {
        return mantieneRequisitosLocalesAprob;
    }

    public void setMantieneRequisitosLocalesAprob(Integer mantieneRequisitosLocalesAprob) {
        this.mantieneRequisitosLocalesAprob = mantieneRequisitosLocalesAprob;
    }

    public Integer getDisponeEspacioComplWifi() {
        return disponeEspacioComplWifi;
    }

    public void setDisponeEspacioComplWifi(Integer disponeEspacioComplWifi) {
        this.disponeEspacioComplWifi = disponeEspacioComplWifi;
    }

    public String getDescColectivo() {
        return descColectivo;
    }

    public void setDescColectivo(String descColectivo) {
        this.descColectivo = descColectivo;
    }

    public Integer getFkIdAmbitoSolicitado() {
        return fkIdAmbitoSolicitado;
    }

    public void setFkIdAmbitoSolicitado(Integer fkIdAmbitoSolicitado) {
        this.fkIdAmbitoSolicitado = fkIdAmbitoSolicitado;
    }

    public String getDescAmbitoSolicitado() {
        return descAmbitoSolicitado;
    }

    public void setDescAmbitoSolicitado(String descAmbitoSolicitado) {
        this.descAmbitoSolicitado = descAmbitoSolicitado;
    }

    @Override
    public String toString() {
        return "ColecUbicacionesCTVO{" + "codId=" + codId + ", numExpediente=" + numExpediente + ", codTipoColectivo=" + codTipoColectivo + ", codEntidad=" + codEntidad + ", territorioHist=" + territorioHist + ", comarca=" + comarca + ", fkIdAmbitoSolicitado=" + fkIdAmbitoSolicitado + ", municipio=" + municipio + ", localidad=" + localidad + ", direccion=" + direccion + ", direccionPortal=" + direccionPortal + ", direccionPiso=" + direccionPiso + ", direccionLetra=" + direccionLetra + ", codigoPostal=" + codigoPostal + ", telefono=" + telefono + ", cifEntidad=" + cifEntidad + ", nombreEntidad=" + nombreEntidad + ", descColectivo=" + descColectivo + ", descTerritorioHist=" + descTerritorioHist + ", descComarca=" + descComarca + ", descAmbitoSolicitado=" + descAmbitoSolicitado + ", descMunicipio=" + descMunicipio + ", localesPreviamenteAprobados=" + localesPreviamenteAprobados + ", mantieneRequisitosLocalesAprob=" + mantieneRequisitosLocalesAprob + ", disponeEspacioComplWifi=" + disponeEspacioComplWifi + '}';
    }
   
}
