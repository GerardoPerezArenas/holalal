/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide88.vo;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author INGDGC
 */
public class Melanbide88Inversiones {
    
    private Long id; //  number primary Key,
    private String num_exp; // varchar2(30) not null,
    private Date fecha; // date  NOT NULL,
    private String fechaString; // formato dd/MM/yyyy
    private String numFactura; // VARCHAR2(200) NOT NULL,
    private String descripcion; // VARCHAR(200) NOT NULL,
    private String nombProveedor; // VARCHAR2(200),
    private Double importe; // number not null,
    private String pagada; // varchar2(1) default 'N' not null,
    private Date fechaPago; // date,
    private String fechaPagoString; // formato dd/MM/yyyy,
    private Date fechaAlta; // timestamp default systimestamp
    
    private final transient  SimpleDateFormat formatFechaddMMyyyy = new SimpleDateFormat("dd/MM/yyyy");
    private final transient  SimpleDateFormat formatFechaLog = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public Melanbide88Inversiones() {
    }

    public Melanbide88Inversiones(Long id, String num_exp,Date fecha, String numFactura, String descripcion, String nombProveedor, Double importe, String pagada, Date fechaPago, Date fechaAlta) {
        this.id = id;
        this.num_exp = num_exp;
        this.fecha = fecha;
        this.fechaString = (fecha!=null?formatFechaddMMyyyy.format(fecha):"");
        this.numFactura = numFactura;
        this.descripcion = descripcion;
        this.nombProveedor = nombProveedor;
        this.importe = importe;
        this.pagada = pagada;
        this.fechaPago = fechaPago;
        this.fechaPagoString = (fechaPago!=null?formatFechaddMMyyyy.format(fechaPago):"");
        this.fechaAlta = fechaAlta;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNum_exp() {
        return num_exp;
    }

    public void setNum_exp(String num_exp) {
        this.num_exp = num_exp;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getNumFactura() {
        return numFactura;
    }

    public void setNumFactura(String numFactura) {
        this.numFactura = numFactura;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombProveedor() {
        return nombProveedor;
    }

    public void setNombProveedor(String nombProveedor) {
        this.nombProveedor = nombProveedor;
    }

    public Double getImporte() {
        return importe;
    }

    public void setImporte(Double importe) {
        this.importe = importe;
    }

    public String getPagada() {
        return pagada;
    }

    public void setPagada(String pagada) {
        this.pagada = pagada;
    }

    public Date getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(Date fechaPago) {
        this.fechaPago = fechaPago;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }   

    public String getFechaString() {
        if(this.fecha!=null)
            fechaString=formatFechaddMMyyyy.format(fecha);
        return fechaString;
    }

    public void setFechaString(String fechaString) {
        this.fechaString = fechaString;
    }

    public String getFechaPagoString() {
        if(this.fechaPago!=null)
            fechaPagoString=formatFechaddMMyyyy.format(fechaPago);
        return fechaPagoString;
    }

    public void setFechaPagoString(String fechaPagoString) {
        this.fechaPagoString = fechaPagoString;
    }

    @Override
    public String toString() {
        return "Melanbide88Inversiones{" + "id=" + id + ",num_exp=" + num_exp + ", fecha=" + (fecha!=null?formatFechaddMMyyyy.format(fecha):"") + ", numFactura=" + numFactura + ", descripcion=" + descripcion + ", nombProveedor=" + nombProveedor + ", importe=" + importe + ", pagada=" + pagada + ", fechaPago=" + (fechaPago!=null?formatFechaddMMyyyy.format(fechaPago):"") + ", fechaAlta=" + (fechaAlta!=null?formatFechaLog.format(fechaAlta):"") + '}';
    }
    
}
