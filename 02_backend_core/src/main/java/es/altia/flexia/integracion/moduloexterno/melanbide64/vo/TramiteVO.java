/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide64.vo;

/**
 *
 * @author Kepa
 */
public class TramiteVO {
     private String  numExpediente;
     private int codTramite;
     private int ocurrenciaTramite;
     private int codExterno;
     private String nomCampo;
     private String valorCampo;
     private int codigoTabla;
     // 1=num 2=txt 3=fecha 4=texto largo 5=fichero 6=desplegable
     // 8=numerico calculado 9=fecha calculada 10=desplegable externo

    public String getNumExpediente() {
        return numExpediente;
    }

    public void setNumExpediente(String numExpediente) {
        this.numExpediente = numExpediente;
    }

    public int getCodTramite() {
        return codTramite;
    }

    public void setCodTramite(int codTramite) {
        this.codTramite = codTramite;
    }

    public int getOcurrenciaTramite() {
        return ocurrenciaTramite;
    }

    public void setOcurrenciaTramite(int ocurrenciaTramite) {
        this.ocurrenciaTramite = ocurrenciaTramite;
    }

    public int getCodExterno() {
        return codExterno;
    }

    public void setCodExterno(int codExterno) {
        this.codExterno = codExterno;
    }

    public String getNomCampo() {
        return nomCampo;
    }

    public void setNomCampo(String nomCampo) {
        this.nomCampo = nomCampo;
    }

    public String getValorCampo() {
        return valorCampo;
    }

    public void setValorCampo(String valorCampo) {
        this.valorCampo = valorCampo;
    }

    public int getCodigoTabla() {
        return codigoTabla;
    }

    public void setCodigoTabla(int codigoTabla) {
        this.codigoTabla = codigoTabla;
    }
          
    
}//class
