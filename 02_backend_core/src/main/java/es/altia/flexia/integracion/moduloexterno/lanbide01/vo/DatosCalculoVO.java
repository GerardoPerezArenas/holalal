package es.altia.flexia.integracion.moduloexterno.lanbide01.vo;

import java.util.ArrayList;

public class DatosCalculoVO {
    private int id;
    private String numExpediente;
    private String ejercicio;
    private String codMunicipio;
    private String codProcedimiento;
    private String importeSubvencionado;
    private String nombreModulo;
    private ArrayList<DatosPeriodoVO> periodos;
    private String sumaTotalDiasPeriodos;
    private int descuento;
    private double totalConDescuento;
    private String diasRestantesSubvencionables;
    
    public int getId() {
        return id;
    }//getId
    public void setId(int id) {
        this.id = id;
    }//setId

    public String getNumExpediente() {
        return numExpediente;
    }//getNumExpediente
    public void setNumExpediente(String numExpediente) {
        this.numExpediente = numExpediente;
    }//setNumExpediente

    public String getCodMunicipio() {
        return codMunicipio;
    }//getCodMunicipio
    public void setCodMunicipio(String codMunicipio) {
        this.codMunicipio = codMunicipio;
    }//setCodMunicipio

    public String getCodProcedimiento() {
        return codProcedimiento;
    }//getCodProcedimiento
    public void setCodProcedimiento(String codProcedimiento) {
        this.codProcedimiento = codProcedimiento;
    }//setCodProcedimiento
    
    public String getEjercicio() {
        return ejercicio;
    }//getEjercicio
    public void setEjercicio(String ejercicio) {
        this.ejercicio = ejercicio;
    }//setEjercicio

    public String getImporteSubvencionado() {
        return importeSubvencionado;
    }//getImporteSubvencionado
    public void setImporteSubvencionado(String importeSubvencionado) {
        this.importeSubvencionado = importeSubvencionado;
    }//setImporteSubvencionado

    public String getNombreModulo() {
        return nombreModulo;
    }//getNombreModulo
    public void setNombreModulo(String nombreModulo) {
        this.nombreModulo = nombreModulo;
    }//setNombreModulo
    
    public ArrayList<DatosPeriodoVO> getPeriodos() {
        return periodos;
    }//getPeriodos
    public void setPeriodos(ArrayList<DatosPeriodoVO> periodos) {
        this.periodos = periodos;
    }//setPeriodos

    public String getSumaTotalDiasPeriodos() {
        return sumaTotalDiasPeriodos;
    }//getSumaTotalDiasPeriodos
    public void setSumaTotalDiasPeriodos(String sumaTotalDiasPeriodos) {
        this.sumaTotalDiasPeriodos = sumaTotalDiasPeriodos;
    }//setSumaTotalDiasPeriodos

    public int getDescuento() {
        return descuento;
    }//getDescuento
    public void setDescuento(int descuento) {
        this.descuento = descuento;
    }//setDescuento
    
    public double getTotalConDescuento() {
        return totalConDescuento;
    }//getTotalConDescuento
    public void setTotalConDescuento(double totalConDescuento) {
        this.totalConDescuento = totalConDescuento;
    }//setTotalConDescuento

    public String getDiasRestantesSubvencionables() {
        return diasRestantesSubvencionables;
    }//getDiasRestantesSubvencionables
    public void setDiasRestantesSubvencionables(String diasRestantesSubvencionables) {
        this.diasRestantesSubvencionables = diasRestantesSubvencionables;
    }//setDiasRestantesSubvencionables

}//class