/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide32.vo.orientacion;

import java.math.BigDecimal;

/**
 *
 * @author santiagoc
 */
public class OriUbicValoracionVO 
{
    //Id
    private Integer oriOrientUbicCod;
    
    //Datos Ubicacion
    private String descProvincia;
    private String descAmbito;
    private String descMunicipio;
    private String direccion;
    private String horasSolic;
    //Solicitud
    private Integer oriEntTrayectoria;
    private BigDecimal oriUbicPuntuacion;
    private Integer oriOrientDespachos;
    private String oriOrientAulagrupal;
    //Validado
    private Integer oriEntTrayectoriaVal;
    private BigDecimal oriUbicPuntuacionVal;
    private Integer oriOrientDespachosValidados;
    private String oriOrientAulaGrupalValidada;
    
    //Valoracion
    private Integer oriOrientValTray;
    private BigDecimal oriOrientValUbic;
    private Long oriOrientValDespachos;
    private Long oriOrientValAulas;
    
    //Puntuacion
    private BigDecimal oriOrientPuntuacion;
    
    //Observaciones
    private String oriOrientObservaciones;
    
    public OriUbicValoracionVO()
    {
        
    }

    public Integer getOriOrientUbicCod() {
        return oriOrientUbicCod;
    }

    public void setOriOrientUbicCod(Integer oriOrientUbicCod) {
        this.oriOrientUbicCod = oriOrientUbicCod;
    }

    public String getDescProvincia() {
        return descProvincia;
    }

    public void setDescProvincia(String descProvincia) {
        this.descProvincia = descProvincia;
    }

    public String getDescAmbito() {
        return descAmbito;
    }

    public void setDescAmbito(String descAmbito) {
        this.descAmbito = descAmbito;
    }

    public String getDescMunicipio() {
        return descMunicipio;
    }

    public void setDescMunicipio(String descMunicipio) {
        this.descMunicipio = descMunicipio;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getHorasSolic() {
        return horasSolic;
    }

    public void setHorasSolic(String horasSolic) {
        this.horasSolic = horasSolic;
    }

    public Integer getOriEntTrayectoria() {
        return oriEntTrayectoria;
    }

    public void setOriEntTrayectoria(Integer oriEntTrayectoria) {
        this.oriEntTrayectoria = oriEntTrayectoria;
    }

    public BigDecimal getOriUbicPuntuacion() {
        return oriUbicPuntuacion;
    }

    public void setOriUbicPuntuacion(BigDecimal oriUbicPuntuacion) {
        this.oriUbicPuntuacion = oriUbicPuntuacion;
    }

    public Integer getOriOrientDespachos() {
        return oriOrientDespachos;
    }

    public void setOriOrientDespachos(Integer oriOrientDespachos) {
        this.oriOrientDespachos = oriOrientDespachos;
    }

    public String getOriOrientAulagrupal() {
        return oriOrientAulagrupal;
    }

    public void setOriOrientAulagrupal(String oriOrientAulagrupal) {
        this.oriOrientAulagrupal = oriOrientAulagrupal;
    }

    public Integer getOriEntTrayectoriaVal() {
        return oriEntTrayectoriaVal;
    }

    public void setOriEntTrayectoriaVal(Integer oriEntTrayectoriaVal) {
        this.oriEntTrayectoriaVal = oriEntTrayectoriaVal;
    }

    public BigDecimal getOriUbicPuntuacionVal() {
        return oriUbicPuntuacionVal;
    }

    public void setOriUbicPuntuacionVal(BigDecimal oriUbicPuntuacionVal) {
        this.oriUbicPuntuacionVal = oriUbicPuntuacionVal;
    }

    public Integer getOriOrientDespachosValidados() {
        return oriOrientDespachosValidados;
    }

    public void setOriOrientDespachosValidados(Integer oriOrientDespachosValidados) {
        this.oriOrientDespachosValidados = oriOrientDespachosValidados;
    }

    public String getOriOrientAulaGrupalValidada() {
        return oriOrientAulaGrupalValidada;
    }

    public void setOriOrientAulaGrupalValidada(String oriOrientAulaGrupalValidada) {
        this.oriOrientAulaGrupalValidada = oriOrientAulaGrupalValidada;
    }

    public Integer getOriOrientValTray() {
        return oriOrientValTray;
    }

    public void setOriOrientValTray(Integer oriOrientValTray) {
        this.oriOrientValTray = oriOrientValTray;
    }

    public BigDecimal getOriOrientValUbic() {
        return oriOrientValUbic;
    }

    public void setOriOrientValUbic(BigDecimal oriOrientValUbic) {
        this.oriOrientValUbic = oriOrientValUbic;
    }

    public Long getOriOrientValDespachos() {
        return oriOrientValDespachos;
    }

    public void setOriOrientValDespachos(Long oriOrientValDespachos) {
        this.oriOrientValDespachos = oriOrientValDespachos;
    }

    public Long getOriOrientValAulas() {
        return oriOrientValAulas;
    }

    public void setOriOrientValAulas(Long oriOrientValAulas) {
        this.oriOrientValAulas = oriOrientValAulas;
    }

    public BigDecimal getOriOrientPuntuacion() {
        return oriOrientPuntuacion;
    }

    public void setOriOrientPuntuacion(BigDecimal oriOrientPuntuacion) {
        this.oriOrientPuntuacion = oriOrientPuntuacion;
    }

    public String getOriOrientObservaciones() {
        return oriOrientObservaciones;
    }

    public void setOriOrientObservaciones(String oriOrientObservaciones) {
        this.oriOrientObservaciones = oriOrientObservaciones;
    }
}
