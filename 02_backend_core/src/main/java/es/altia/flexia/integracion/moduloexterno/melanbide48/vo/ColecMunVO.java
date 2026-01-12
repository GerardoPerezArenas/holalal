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
public class ColecMunVO 
{
    private Long codMun;
    private String descMun;
    private Long codComarca;
    private Integer codTthh;
    private String descMunEu;
    private Date fecSysdate;
    
    public ColecMunVO()
    {
        
    }

    public Long getCodMun() {
        return codMun;
    }

    public void setCodMun(Long codMun) {
        this.codMun = codMun;
    }

    public String getDescMun() {
        return descMun;
    }

    public void setDescMun(String descMun) {
        this.descMun = descMun;
    }

    public Long getCodComarca() {
        return codComarca;
    }

    public void setCodComarca(Long codComarca) {
        this.codComarca = codComarca;
    }

    public Date getFecSysdate() {
        return fecSysdate;
    }

    public void setFecSysdate(Date fecSysdate) {
        this.fecSysdate = fecSysdate;
    }

    public Integer getCodTthh() {
        return codTthh;
    }

    public void setCodTthh(Integer codTthh) {
        this.codTthh = codTthh;
    }

    public String getDescMunEu() {
        return descMunEu;
    }

    public void setDescMunEu(String descMunEu) {
        this.descMunEu = descMunEu;
    }
    
    
}
