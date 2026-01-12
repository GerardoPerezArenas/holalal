/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.lanbide01.vo;

/**
 *
 * @author INGDGC
 */
public class ParametrosLlamadasGestionPestanasM01 {
    
    private String tarea;
    private String modulo;
    private String operacion;
    private String tipo;
    private String control;
    private String tipoOperacion;
    private Melanbide01DepenPerSut jsonCausantesSubvencion;
    private Melanbide01HistoSubv jsonHistorialSibvencion;

    public String getTarea() {
        return tarea;
    }

    public void setTarea(String tarea) {
        this.tarea = tarea;
    }

    public String getModulo() {
        return modulo;
    }

    public void setModulo(String modulo) {
        this.modulo = modulo;
    }

    public String getOperacion() {
        return operacion;
    }

    public void setOperacion(String operacion) {
        this.operacion = operacion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getControl() {
        return control;
    }

    public void setControl(String control) {
        this.control = control;
    }

    public String getTipoOperacion() {
        return tipoOperacion;
    }

    public void setTipoOperacion(String tipoOperacion) {
        this.tipoOperacion = tipoOperacion;
    }

    public Melanbide01DepenPerSut getJsonCausantesSubvencion() {
        return jsonCausantesSubvencion;
    }

    public void setJsonCausantesSubvencion(Melanbide01DepenPerSut jsonCausantesSubvencion) {
        this.jsonCausantesSubvencion = jsonCausantesSubvencion;
    }

    public Melanbide01HistoSubv getJsonHistorialSibvencion() {
        return jsonHistorialSibvencion;
    }

    public void setJsonHistorialSibvencion(Melanbide01HistoSubv jsonHistorialSibvencion) {
        this.jsonHistorialSibvencion = jsonHistorialSibvencion;
    }
    
    
}
