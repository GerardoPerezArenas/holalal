/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide48.vo;

import java.util.Date;

/**
 *
 * @author santiagoc
 */
public class ColecTrayEspVO 
{
    private Long codTrayEsp;
    private Long codEntidad;
    private String numExp;
    private Integer expEje;
    private Integer colectivo;
    private String nombreAdm;
    private String descActividad;
    private Integer validada;
    private Date fecSysdate;
    
    public ColecTrayEspVO()
    {
        
    }

    public Long getCodTrayEsp() {
        return codTrayEsp;
    }

    public void setCodTrayEsp(Long codTrayEsp) {
        this.codTrayEsp = codTrayEsp;
    }

    public Long getCodEntidad() {
        return codEntidad;
    }

    public void setCodEntidad(Long codEntidad) {
        this.codEntidad = codEntidad;
    }

    public String getNumExp() {
        return numExp;
    }

    public void setNumExp(String numExp) {
        this.numExp = numExp;
    }

    public Integer getExpEje() {
        return expEje;
    }

    public void setExpEje(Integer expEje) {
        this.expEje = expEje;
    }

    public Integer getColectivo() {
        return colectivo;
    }

    public void setColectivo(Integer colectivo) {
        this.colectivo = colectivo;
    }

    public String getNombreAdm() {
        return nombreAdm;
    }

    public void setNombreAdm(String nombreAdm) {
        this.nombreAdm = nombreAdm;
    }

    public String getDescActividad() {
        return descActividad;
    }

    public void setDescActividad(String descActividad) {
        this.descActividad = descActividad;
    }

    public Integer getValidada() {
        return validada;
    }

    public void setValidada(Integer validada) {
        this.validada = validada;
    }

    public Date getFecSysdate() {
        return fecSysdate;
    }

    public void setFecSysdate(Date fecSysdate) {
        this.fecSysdate = fecSysdate;
    }
}
