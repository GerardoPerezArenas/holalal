/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide35.exception;

/**
 *
 * @author santiagoc
 */
public class ExcelRowMappingException extends Exception
{
    
    private String campo;
    private String tipo;
    private Integer longMax;
    private Integer pEntera;
    private Integer pDecimal;
    private String mensaje;
    
    public ExcelRowMappingException(String campo)
    {
        super();
        this.campo = campo;
    }

    public String getCampo() {
        return campo;
    }

    public void setCampo(String campo) {
        this.campo = campo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getLongMax() {
        return longMax;
    }

    public void setLongMax(Integer longMax) {
        this.longMax = longMax;
    }

    public Integer getpEntera() {
        return pEntera;
    }

    public void setpEntera(Integer pEntera) {
        this.pEntera = pEntera;
    }

    public Integer getpDecimal() {
        return pDecimal;
    }

    public void setpDecimal(Integer pDecimal) {
        this.pDecimal = pDecimal;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
