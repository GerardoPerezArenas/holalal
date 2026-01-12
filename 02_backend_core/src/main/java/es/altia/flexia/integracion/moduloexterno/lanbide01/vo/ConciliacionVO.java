package es.altia.flexia.integracion.moduloexterno.lanbide01.vo;

import java.util.Calendar;

/**
 * Representa a un registro de la tabla ME_LANBIDE01_CONCILIACION
 */
public class ConciliacionVO {
    
    private int id;
    private Calendar fechaDesde;
    private Calendar fechaHasta;
    private String porcSubven;
    private int numDias;    
    private String baseCotizacion;
    private String bonificacion;
    private double gasto;

    public int getId() {
        return id;
    }//getId
    public void setId(int id) {
        this.id = id;
    }//setId

    public Calendar getFechaDesde() {
        return fechaDesde;
    }//getFechaDesde
    public void setFechaDesde(Calendar fechaDesde) {
        this.fechaDesde = fechaDesde;
    }//setFechaDesde

    public Calendar getFechaHasta() {
        return fechaHasta;
    }//getFechaHasta
    public void setFechaHasta(Calendar fechaHasta) {
        this.fechaHasta = fechaHasta;
    }//setFechaHasta

    public String getPorcSubven() {
        return porcSubven;
    }//getPorcSubven
    public void setPorcSubven(String porcSubven) {
        this.porcSubven = porcSubven;
    }//setPorcSubven

    public int getNumDias() {
        return numDias;
    }//getNumDias
    public void setNumDias(int numDias) {
        this.numDias = numDias;
    }//setNumDias

    public String getBaseCotizacion() {
        return baseCotizacion;
    }//getBaseCotizacion
    public void setBaseCotizacion(String baseCotizacion) {
        this.baseCotizacion = baseCotizacion;
    }//setBaseCotizacion

    public String getBonificacion() {
        return bonificacion;
    }//getBonificacion
    public void setBonificacion(String bonificacion) {
        this.bonificacion = bonificacion;
    }//setBonificacion

    public double getGasto() {
        return gasto;
    }//getGasto
    public void setGasto(double gasto) {
        this.gasto = gasto;
    }//setGasto
            
}//class
