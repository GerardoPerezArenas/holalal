/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide43.vo;

import java.util.HashMap;

/**
 * 
 * @author INGDGC
 * 
 */
public class MeLanbide43GestionTramitesExpedienteRespuesta {
    
    private String codigoRespuesta;
    private String descripcionRespuesta;
    private boolean respuestaOKBoolean;
    private String codigoDescripcionErrorAsString;
    private HashMap<String,String> respuestaDatosAdicionales;
    public final static String COD_RESP_OK="0";
    public final static String DES_RESP_OK="OK";

    public MeLanbide43GestionTramitesExpedienteRespuesta() {
    }
    public MeLanbide43GestionTramitesExpedienteRespuesta(String codigoRespuesta) {
        this.codigoRespuesta = codigoRespuesta;
        this.descripcionRespuesta = (COD_RESP_OK.equalsIgnoreCase(codigoRespuesta)? DES_RESP_OK : null);
    }
    public MeLanbide43GestionTramitesExpedienteRespuesta(String codigoRespuesta, String descripcionRespuesta) {
        this.codigoRespuesta = codigoRespuesta;
        this.descripcionRespuesta = descripcionRespuesta;
    }
    public MeLanbide43GestionTramitesExpedienteRespuesta(boolean codigoRespuesta) {
        respuestaOKBoolean=codigoRespuesta;
        if(codigoRespuesta){
            this.codigoRespuesta = COD_RESP_OK;
            this.descripcionRespuesta = DES_RESP_OK;
        }
    }

    public MeLanbide43GestionTramitesExpedienteRespuesta( boolean respuestaOKBoolean, String codigoDescripcionErrorAsString,String descripcionRespuesta) {
        this.descripcionRespuesta = descripcionRespuesta;
        this.respuestaOKBoolean = respuestaOKBoolean;
        this.codigoDescripcionErrorAsString = codigoDescripcionErrorAsString;
    }
    
    
    public String getCodigoRespuesta() {
        return codigoRespuesta;
    }

    public void setCodigoRespuesta(String codigoRespuesta) {
        this.codigoRespuesta = codigoRespuesta;
    }

    public String getDescripcionRespuesta() {
        return descripcionRespuesta;
    }

    public void setDescripcionRespuesta(String descripcionRespuesta) {
        this.descripcionRespuesta = descripcionRespuesta;
    }

    public boolean isRespuestaOKBoolean() {
        return respuestaOKBoolean;
    }

    public void setRespuestaOKBoolean(boolean respuestaOKBoolean) {
        this.respuestaOKBoolean = respuestaOKBoolean;
    }

    public HashMap<String, String> getRespuestaDatosAdicionales() {
        return respuestaDatosAdicionales;
    }

    public void setRespuestaDatosAdicionales(HashMap<String, String> respuestaDatosAdicionales) {
        this.respuestaDatosAdicionales = respuestaDatosAdicionales;
    }        
    
    public String getCodigoDescripcionErrorAsString(){
        this.codigoDescripcionErrorAsString="["
                + " codigoRespuesta : " + (this.codigoRespuesta!=null ? this.codigoRespuesta : "" )
                + " descripcionRespuesta : " + (this.descripcionRespuesta!=null ? this.codigoDescripcionErrorAsString : "")
                + " respuestaOKBoolean :" + this.respuestaOKBoolean
                + "]"
                ;
        return this.codigoDescripcionErrorAsString;
    }
}
