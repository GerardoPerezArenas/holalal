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
public class Melanbide88Subsolic {
    
    private Long id; //  number primary Key,
    private String num_exp; // varchar2(30) not null,
    private String estado; // VARCHAR(1) NOT NULL,
    private String organismo; // VARCHAR2(200) NOT NULL,
    private String objeto; // VARCHAR(200),
    private Double importe; // number,
    private Date fecha;  //date, 
    private String fechaString;  //Formato dd/MM/yyyy, 
    private Date fechaAlta; // timestamp default systimestamp
    
    private final transient  SimpleDateFormat formatFechaddMMyyyy = new SimpleDateFormat("dd/MM/yyyy");
    private final transient  SimpleDateFormat formatFechaLog = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public Melanbide88Subsolic() {
    }

    public Melanbide88Subsolic(Long id, String num_exp, String estado, String organismo, String objeto, Double importe, Date fecha, Date fechaAlta) {
        this.id = id;
        this.num_exp = num_exp;
        this.estado = estado;
        this.organismo = organismo;
        this.objeto = objeto;
        this.importe = importe;
        this.fecha = fecha;
        this.fechaString = (fecha!=null?formatFechaddMMyyyy.format(fecha):"");
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getOrganismo() {
        return organismo;
    }

    public void setOrganismo(String organismo) {
        this.organismo = organismo;
    }

    public String getObjeto() {
        return objeto;
    }

    public void setObjeto(String objeto) {
        this.objeto = objeto;
    }

    public Double getImporte() {
        return importe;
    }

    public void setImporte(Double importe) {
        this.importe = importe;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public String getFechaString() {
        if(fecha!=null)
            fechaString=formatFechaddMMyyyy.format(fecha);
        return fechaString;
    }

    public void setFechaString(String fechaString) {
        this.fechaString = fechaString;
    }

    @Override
    public String toString() {
        return "Melanbide88Subsolic{" + "id=" + id + ", num_exp=" + num_exp + ", estado=" + estado + ", organismo=" + organismo + ", objeto=" + objeto + ", importe=" + importe + ", fecha=" + (fecha!=null?formatFechaddMMyyyy.format(fecha):"") + ", fechaAlta=" + (fechaAlta!=null?formatFechaLog.format(fechaAlta):"") + '}';
    }

}
