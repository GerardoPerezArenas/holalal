/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide34.vo;

/**
 *
 * @author lauras
 */
public class ValoresPago {
    private String importeConcedidoPago;
    private String importeAPagar;
    private String descuento;    
    private String fecPago;
    private String viable;
    
    public ValoresPago()
    {
        
    }

    public String getImporteConcedidoPago() {
        return importeConcedidoPago;
    }

    public void setImporteConcedidoPago(String importePago) {
        this.importeConcedidoPago = importePago;
    }

    public String getImporteAPagar() {
        return importeAPagar;
    }

    public void setImporteAPagar(String importeAPagar) {
        this.importeAPagar = importeAPagar;
    }

    public String getDescuento() {
        return descuento;
    }

    public void setDescuento(String descuento) {
        this.descuento = descuento;
    }

    public String getFecPago() {
        return fecPago;
    }

    public void setFecPago(String fecPago) {
        this.fecPago = fecPago;
    }

    public String getViable() {
        return viable;
    }

    public void setViable(String viable) {
        this.viable = viable;
    }

    
    
}
