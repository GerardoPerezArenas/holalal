/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide47.vo.comun;

import java.util.Date;

/**
 *
 * @author INGDGC
 */
public class MeLanbideConvocatorias {
    
    /**
     *  ID	NUMBER
        PRO_COD	VARCHAR2(5 BYTE)
        PRO_COD_PLATEA	VARCHAR2(30 BYTE)
        NUM_BOPV	VARCHAR2(30 BYTE)
        FEC_CONS_LANBIDE	DATE
        DECRETOCODIGO	VARCHAR2(10 BYTE)
        DECRETODESCRIPCION	VARCHAR2(500 BYTE)
        DECRETODESCRIPCIONEU	VARCHAR2(500 BYTE)
        DECRETOFECPUBLICACION	DATE
        DECRETOFECENTRADAVIGOR	DATE
        DECRETOFECFINAPLICACION	DATE
     */
    private Integer id;
    private String proCod;
    private String proCodPlatea;
    private String numBOPV;
    private Date fecConsLanbide;
    private String decretoCodigo;
    private String decretoDesripcion;
    private String decretoDesripcionEu;
    private Date decretoFecPublicacion;
    private Date decretoFecEntradaVigor;
    private Date decretoFecFinAplicacion;

    public MeLanbideConvocatorias() {
    }

    public MeLanbideConvocatorias(Integer id, String proCod, String proCodPlatea, String numBOPV, Date fecConsLanbide, String decretoCodigo, String decretoDesripcion, String decretoDesripcionEu, Date decretoFecPublicacion, Date decretoFecEntradaVigor, Date decretoFecFinAplicacion) {
        this.id = id;
        this.proCod = proCod;
        this.proCodPlatea = proCodPlatea;
        this.numBOPV = numBOPV;
        this.fecConsLanbide = fecConsLanbide;
        this.decretoCodigo = decretoCodigo;
        this.decretoDesripcion = decretoDesripcion;
        this.decretoDesripcionEu = decretoDesripcionEu;
        this.decretoFecPublicacion = decretoFecPublicacion;
        this.decretoFecEntradaVigor = decretoFecEntradaVigor;
        this.decretoFecFinAplicacion = decretoFecFinAplicacion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProCod() {
        return proCod;
    }

    public void setProCod(String proCod) {
        this.proCod = proCod;
    }

    public String getProCodPlatea() {
        return proCodPlatea;
    }

    public void setProCodPlatea(String proCodPlatea) {
        this.proCodPlatea = proCodPlatea;
    }

    public String getNumBOPV() {
        return numBOPV;
    }

    public void setNumBOPV(String numBOPV) {
        this.numBOPV = numBOPV;
    }

    public Date getFecConsLanbide() {
        return fecConsLanbide;
    }

    public void setFecConsLanbide(Date fecConsLanbide) {
        this.fecConsLanbide = fecConsLanbide;
    }

    public String getDecretoCodigo() {
        return decretoCodigo;
    }

    public void setDecretoCodigo(String decretoCodigo) {
        this.decretoCodigo = decretoCodigo;
    }

    public String getDecretoDesripcion() {
        return decretoDesripcion;
    }

    public void setDecretoDesripcion(String decretoDesripcion) {
        this.decretoDesripcion = decretoDesripcion;
    }

    public String getDecretoDesripcionEu() {
        return decretoDesripcionEu;
    }

    public void setDecretoDesripcionEu(String decretoDesripcionEu) {
        this.decretoDesripcionEu = decretoDesripcionEu;
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
    
}
