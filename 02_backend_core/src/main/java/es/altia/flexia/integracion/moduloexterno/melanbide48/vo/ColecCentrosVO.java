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
public class ColecCentrosVO 
{
    private Long codCentro;
    private String numExp;
    private Integer expEje;
    private Integer ambito;
    private Integer colectivo;
    private Long comarca;
    private Long mun;
    private String direccion;
    private Date fecSysdate;
    
    public ColecCentrosVO()
    {
        
    }

    public Long getCodCentro() {
        return codCentro;
    }

    public void setCodCentro(Long codCentro) {
        this.codCentro = codCentro;
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

    public Integer getAmbito() {
        return ambito;
    }

    public void setAmbito(Integer ambito) {
        this.ambito = ambito;
    }

    public Integer getColectivo() {
        return colectivo;
    }

    public void setColectivo(Integer colectivo) {
        this.colectivo = colectivo;
    }

    public Long getComarca() {
        return comarca;
    }

    public void setComarca(Long comarca) {
        this.comarca = comarca;
    }

    public Long getMun() {
        return mun;
    }

    public void setMun(Long mun) {
        this.mun = mun;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Date getFecSysdate() {
        return fecSysdate;
    }

    public void setFecSysdate(Date fecSysdate) {
        this.fecSysdate = fecSysdate;
    }
}
