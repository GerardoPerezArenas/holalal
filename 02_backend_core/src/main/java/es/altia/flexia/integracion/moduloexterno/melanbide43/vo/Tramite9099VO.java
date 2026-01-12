/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.altia.flexia.integracion.moduloexterno.melanbide43.vo;

/**
 *
 * @author alexandrep
 */
public class Tramite9099VO {
    
    private String procedimiento;
    private Integer codigoTramite;
    private Integer plazo;
    private String unidadPlazo;

    public Tramite9099VO(String procedimiento, Integer codigoTramite, Integer plazo, String unidadPlazo) {
        this.procedimiento = procedimiento;
        this.codigoTramite = codigoTramite;
        this.plazo = plazo;
        this.unidadPlazo = unidadPlazo;
    }

    public String getProcedimiento() {
        return procedimiento;
    }

    public void setProcedimiento(String procedimiento) {
        this.procedimiento = procedimiento;
    }

    public Integer getCodigoTramite() {
        return codigoTramite;
    }

    public void setCodigoTramite(Integer codigoTramite) {
        this.codigoTramite = codigoTramite;
    }

    public Integer getPlazo() {
        return plazo;
    }

    public void setPlazo(Integer plazo) {
        this.plazo = plazo;
    }

    public String getUnidadPlazo() {
        return unidadPlazo;
    }

    public void setUnidadPlazo(String unidadPlazo) {
        this.unidadPlazo = unidadPlazo;
    }
    
    
    
}
