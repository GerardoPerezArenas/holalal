package es.altia.flexia.integracion.moduloexterno.lanbide01.vo;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DatosPeriodoVO {
    
    private int id;
    private int idDatosCalculo;
    private Calendar fechaInicio;
    private Calendar fechaFin;
    private String porcSubven;
    private int numDias;
    private String baseCotizacion;
    private String bonificacion;
    private String gasto;
    private String reducPerSust;
    private String jornPersSust;
    private String jornPersCont;
    private Double porcJornSustitucion;
    private Double porcJornRealizada;

    public int getId() {
        return id;
    }//getId
    public void setId(int id) {
        this.id = id;
    }//setId

    public int getIdDatosCalculo() {
        return idDatosCalculo;
    }//getIdDatosCalculo
    public void setIdDatosCalculo(int idDatosCalculo) {
        this.idDatosCalculo = idDatosCalculo;
    }//setIdDatosCalculo

    public Calendar getFechaInicio() {
        return fechaInicio;
    }//getFechaDesde
    public void setFechaInicio(Calendar fechaInicio) {
        this.fechaInicio = fechaInicio;
    }//setFechaInicio

    public Calendar getFechaFin() {
        return fechaFin;
    }//getFechaFin
    public void setFechaFin(Calendar fechaFin) {
        this.fechaFin = fechaFin;
    }//setFechaFin

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

    public String getGasto() {
        return gasto;
    }//getGasto
    public void setGasto(String gasto) {
        this.gasto = gasto;
    }//setGasto

    public String getFechaInicioAsString() {
        if(this.fechaInicio!=null){
            SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
            return sf.format(this.fechaInicio.getTime());
        }else return "";
    }//getFechaInicioAsString

    public String getFechaFinAsString() {
        if(this.fechaFin!=null){
            SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
            return sf.format(this.fechaFin.getTime());
        }else return "";
    }//getFechaFinAsString
    
    public String getReducPerSust() {
        return reducPerSust;
    }//getReducPerSust
    public void setReducPerSust(String reducPerSust) {
        this.reducPerSust = reducPerSust;
    }//setReducPerSust
    
    public String getJornPersSust() {
        return jornPersSust;
    }//getJornPersSust
    public void setJornPersSust(String jornPersSust) {
        this.jornPersSust = jornPersSust;
    }//setJornPersSust

    public String getJornPersCont() {
        return jornPersCont;
    }//getJornPersCont
    public void setJornPersCont(String jornPersCont) {
        this.jornPersCont = jornPersCont;
    }//setJornPersCont

    public Double getPorcJornSustitucion() {
        return porcJornSustitucion;
    }

    public void setPorcJornSustitucion(Double porcJornSustitucion) {
        this.porcJornSustitucion = porcJornSustitucion;
    }

    public Double getPorcJornRealizada() {
        return porcJornRealizada;
    }

    public void setPorcJornRealizada(Double porcJornRealizada) {
        this.porcJornRealizada = porcJornRealizada;
    }
    
    

}//class