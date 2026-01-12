package es.altia.flexia.integracion.moduloexterno.melanbide03.vo;

import java.util.ArrayList;

public class CertRd34RDetalleVO {

    private String numExpediente;

    private String nif;
    private String resultado;
    private String numRegistro;
    private String ccaa;
    private String fecha_registro;
    private ArrayList<CertRd34RDetalleUCVO> unidades_competencia;
    private ArrayList<CertRd34RDetalleErrorVO> listado_errores;


    public String getNumExpediente() {
        return numExpediente;
    }

    public void setNumExpediente(String numExpediente) {
        this.numExpediente = numExpediente;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public String getNumRegistro() {
        return numRegistro;
    }

    public void setNumRegistro(String numRegistro) {
        this.numRegistro = numRegistro;
    }

    public String getCcaa() {
        return ccaa;
    }

    public void setCcaa(String ccaa) {
        this.ccaa = ccaa;
    }

    public String getFecha_registro() {
        return fecha_registro;
    }

    public void setFecha_registro(String fecha_registro) {
        this.fecha_registro = fecha_registro;
    }

    public ArrayList<CertRd34RDetalleUCVO> getUnidades_competencia() {
        return unidades_competencia;
    }

    public void setUnidades_competencia(ArrayList<CertRd34RDetalleUCVO> unidades_competencia) {
        this.unidades_competencia = unidades_competencia;
    }

    public ArrayList<CertRd34RDetalleErrorVO> getListado_errores() {
        return listado_errores;
    }

    public void setListado_errores(ArrayList<CertRd34RDetalleErrorVO> listado_errores) {
        this.listado_errores = listado_errores;
    }
}//class