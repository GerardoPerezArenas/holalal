/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide48.vo;

import java.util.List;

/**
 *
 * @author INGDGC
 */
public class ColecProcesoAdjudicacionRespuestaVO {
    
    private String codigo;
    private String mensaje;
    private Boolean expedientesEstadoIncorrecto=false;
    private List<String> expedientesEstadoIncorrectoList;

    public ColecProcesoAdjudicacionRespuestaVO() {
    }
     
    public ColecProcesoAdjudicacionRespuestaVO(String codigo, String mensaje) {
        this.codigo = codigo;
        this.mensaje = mensaje;
    }

    public ColecProcesoAdjudicacionRespuestaVO(String codigo, String mensaje, List<String> expedientesEstadoIncorrectoList) {
        this.codigo = codigo;
        this.mensaje = mensaje;
        this.expedientesEstadoIncorrectoList = expedientesEstadoIncorrectoList;
        this.expedientesEstadoIncorrecto = expedientesEstadoIncorrectoList!=null && !expedientesEstadoIncorrectoList.isEmpty();
    }
    
    
    
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Boolean getExpedientesEstadoIncorrecto() {
        return expedientesEstadoIncorrecto;
    }

    public void setExpedientesEstadoIncorrecto(Boolean expedientesEstadoIncorrecto) {
        this.expedientesEstadoIncorrecto = expedientesEstadoIncorrecto;
    }

    public List<String> getExpedientesEstadoIncorrectoList() {
        return expedientesEstadoIncorrectoList;
    }

    public void setExpedientesEstadoIncorrectoList(List<String> expedientesEstadoIncorrectoList) {
        this.expedientesEstadoIncorrectoList = expedientesEstadoIncorrectoList;
    }

    @Override
    public String toString() {
        return "ColecProcesoAdjudicacionRespuestaVO{" + "codigo=" + codigo + ", mensaje=" + mensaje + ", expedientesEstadoIncorrecto=" + expedientesEstadoIncorrecto + ", expedientesEstadoIncorrectoList=" + expedientesEstadoIncorrectoList + '}';
    }
        
}
