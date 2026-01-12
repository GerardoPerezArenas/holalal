/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.lanbide01.vo;

import java.util.Date;

/**
 *
 * @author INGDGC
 */
public class Melanbide01Decreto {
    
    private int id;
    private String decretoCodigo;
    private String decretoDescripcion;
    private String descretoDecripcionEu;
    private Date decretoFecPublicacion;
    private Date decretoFecEntradaVigor;
    private Date decretoFecFinAplicacion;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDecretoDescripcion() {
        return decretoDescripcion;
    }

    public void setDecretoDescripcion(String decretoDescripcion) {
        this.decretoDescripcion = decretoDescripcion;
    }

    public String getDescretoDecripcionEu() {
        return descretoDecripcionEu;
    }

    public void setDescretoDecripcionEu(String descretoDecripcionEu) {
        this.descretoDecripcionEu = descretoDecripcionEu;
    }

    public Date getDecretoFecPublicacion() {
        return decretoFecPublicacion;
    }

    public void setDecretoFecPublicacion(Date decretoFecPublicacion) {
        this.decretoFecPublicacion = decretoFecPublicacion;
    }

    public Date getDecretoFecEntradaVigor() {
        return decretoFecEntradaVigor;
    }

    public void setDecretoFecEntradaVigor(Date decretoFecEntradaVigor) {
        this.decretoFecEntradaVigor = decretoFecEntradaVigor;
    }

    public Date getDecretoFecFinAplicacion() {
        return decretoFecFinAplicacion;
    }

    public void setDecretoFecFinAplicacion(Date decretoFecFinAplicacion) {
        this.decretoFecFinAplicacion = decretoFecFinAplicacion;
    }

    public String getDecretoCodigo() {
        return decretoCodigo;
    }

    public void setDecretoCodigo(String decretoCodigo) {
        this.decretoCodigo = decretoCodigo;
    }
    
    
    
}
