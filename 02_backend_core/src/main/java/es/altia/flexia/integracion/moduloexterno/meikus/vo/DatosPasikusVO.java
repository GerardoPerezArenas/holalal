/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.altia.flexia.integracion.moduloexterno.meikus.vo;

/**
 *
 * @author pablo.bugia
 */
public class DatosPasikusVO {
    private String envioEika; // boolean o String, no est? claro
    private String impReserva; // 
    private String idReserva;
    private String impanio1;
    private String impanio2;
    private String idIkus;
    private String expeIkad;
    private String importeConcedido;
    private String pago1;
    private String pago2;
    private String idPago1;
    private String idPago2;
    private String expeIkao1;
    private String expeIkao2;
    private String mostrarBtnReserva;
    private String mostrarBtnGrabarRes;
    private String mostrarBtnGrabarPago;

    public DatosPasikusVO(String envioEika, String impReserva, String idReserva, String impanio1, String impanio2,
                          String idIkus, String expeIkad, String importeConcedido, String pago1, String pago2, String idPago1, String idPago2,
                          String expeIkao1, String expeIkao2, String mostrarBtnReserva, String mostrarBtnGrabarRes, String mostrarBtnGrabarPago) {
        this.envioEika = envioEika;
        this.impReserva = impReserva;
        this.idReserva = idReserva;
        this.impanio1 = impanio1;
        this.impanio2 = impanio2;
        this.idIkus = idIkus;
        this.expeIkad = expeIkad;
        this.importeConcedido = importeConcedido;
        this.pago1 = pago1;
        this.pago2 = pago2;
        this.idPago1 = idPago1;
        this.idPago2 = idPago2;
        this.expeIkao1 = expeIkao1;
        this.expeIkao2 = expeIkao2;
        this.mostrarBtnReserva = mostrarBtnReserva;
        this.mostrarBtnGrabarRes = mostrarBtnGrabarRes;
        this.mostrarBtnGrabarPago = mostrarBtnGrabarPago;
    }

    
    public DatosPasikusVO() {
    }

    public String getEnvioEika() {
        return envioEika;
    }

    public void setEnvioEika(String envioEika) {
        this.envioEika = envioEika;
    }

    public String getImpReserva() {
        return impReserva;
    }

    public void setImpReserva(String impReserva) {
        this.impReserva = impReserva;
    }

    public String getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(String idReserva) {
        this.idReserva = idReserva;
    }

    public String getImpanio1() {
        return impanio1;
    }

    public void setImpanio1(String impanio1) {
        this.impanio1 = impanio1;
    }

    public String getImpanio2() {
        return impanio2;
    }

    public void setImpanio2(String impanio2) {
        this.impanio2 = impanio2;
    }

    public String getIdIkus() {
        return idIkus;
    }

    public void setIdIkus(String idIkus) {
        this.idIkus = idIkus;
    }

    public String getExpeIkad() {
        return expeIkad;
    }

    public void setExpeIkad(String expeIkad) {
        this.expeIkad = expeIkad;
    }

    public String getImporteConcedido() {
        return importeConcedido;
    }

    public void setImporteConcedido(String importeConcedido) {
        this.importeConcedido = importeConcedido;
    }

    public String getPago1() {
        return pago1;
    }

    public void setPago1(String pago1) {
        this.pago1 = pago1;
    }

    public String getPago2() {
        return pago2;
    }

    public void setPago2(String pago2) {
        this.pago2 = pago2;
    }

    public String getIdPago1() {
        return idPago1;
    }

    public void setIdPago1(String idPago1) {
        this.idPago1 = idPago1;
    }

    public String getIdPago2() {
        return idPago2;
    }

    public void setIdPago2(String idPago2) {
        this.idPago2 = idPago2;
    }

    public String getExpeIkao1() {
        return expeIkao1;
    }

    public void setExpeIkao1(String expeIkao1) {
        this.expeIkao1 = expeIkao1;
    }

    public String getExpeIkao2() {
        return expeIkao2;
    }

    public void setExpeIkao2(String expeIkao2) {
        this.expeIkao2 = expeIkao2;
    }

    public String getMostrarBtnReserva() {
        return mostrarBtnReserva;
    }

    public void setMostrarBtnReserva(String mostrarBtnReserva) {
        this.mostrarBtnReserva = mostrarBtnReserva;
    }

    public String getMostrarBtnGrabarRes() {
        return mostrarBtnGrabarRes;
    }

    public void setMostrarBtnGrabarRes(String mostrarBtnGrabarRes) {
        this.mostrarBtnGrabarRes = mostrarBtnGrabarRes;
    }

    public String getMostrarBtnGrabarPago() {
        return mostrarBtnGrabarPago;
    }

    public void setMostrarBtnGrabarPago(String mostrarBtnGrabarPago) {
        this.mostrarBtnGrabarPago = mostrarBtnGrabarPago;
    }
    
}
