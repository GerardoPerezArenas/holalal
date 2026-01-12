package es.altia.flexia.integracion.moduloexterno.melanbide03.vo;

import java.util.ArrayList;

public class CertRd34RCabeceraVO {

    private int idEnvio;
    private String fechaEnvio;
    private int total_certificados;
    private String fechaRespuesta;
    private int total_grabados;
    private int total_errores;
    private ArrayList<CertRd34RDetalleVO> lista_de_certificados;
    private String error_ws;


    public int getIdEnvio() {
        return idEnvio;
    }

    public void setIdEnvio(int idEnvio) {
        this.idEnvio = idEnvio;
    }

    public String getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(String fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public int getTotal_certificados() {
        return total_certificados;
    }

    public void setTotal_certificados(int total_certificados) {
        this.total_certificados = total_certificados;
    }

    public String getFechaRespuesta() {
        return fechaRespuesta;
    }

    public void setFechaRespuesta(String fechaRespuesta) {
        this.fechaRespuesta = fechaRespuesta;
    }

    public int getTotal_grabados() {
        return total_grabados;
    }

    public void setTotal_grabados(int total_grabados) {
        this.total_grabados = total_grabados;
    }

    public int getTotal_errores() {
        return total_errores;
    }

    public void setTotal_errores(int total_errores) {
        this.total_errores = total_errores;
    }

    public ArrayList<CertRd34RDetalleVO> getLista_de_certificados() {
        return lista_de_certificados;
    }

    public void setLista_de_certificados(ArrayList<CertRd34RDetalleVO> lista_de_certificados) {
        this.lista_de_certificados = lista_de_certificados;
    }

    public String getError_ws() {
        return error_ws;
    }

    public void setError_ws(String error_ws) {
        this.error_ws = error_ws;
    }
}//class