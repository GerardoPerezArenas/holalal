
package es.altia.flexia.integracion.moduloexterno.melanbide75.vo;

import java.sql.Date;

/*
    Relación de puestos de trabajo efectivamente ocupados por trabajadores/as con discapacidad

    CNAE (se quita)
    Denominación del puesto
    Nombre y apellido/s persona ocupante
    DNI/NIE
    Porcentaje de Discapacidad
    Tipo de contrato
         1 - INDEFINIDO|MUGAGABEA
         2 - TEMPORAL|ALDI BATERAKOA
         3 - PUESTA A DISPOSICIÓN POR ETT|ABLE BIDEZ ESKURA JARRI
    Período de contratación desde
    Período de contratación hasta
    Total días
*/
public class ControlAccesoVO {
    
    private Integer id;
    private String numExp;
    
    //private String cnae;
    private String puesto;
    private String nombre; 
    private String nif;
    private Double porDisc;
    private String tipoCon;
    private String desTipoCon;
    private Date conDesde;
    private Date conHasta;
    private Integer totalDias;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumExp() {
        return numExp;
    }

    public void setNumExp(String numExp) {
        this.numExp = numExp;
    }
    
    /*public String getCnae() {
        return cnae;
    }

    public void setCnae(String cnae) {
        this.cnae = cnae;
    }*/

    public String getPuesto() {
        return puesto;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public Double getPorDisc() {
        return porDisc;
    }

    public void setPorDisc(Double porDisc) {
        this.porDisc = porDisc;
    }

    public String getTipoCon() {
        return tipoCon;
    }

    public void setTipoCon(String tipoCon) {
        this.tipoCon = tipoCon;
    }

    public String getDesTipoCon() {
        return desTipoCon;
    }

    public void setDesTipoCon(String desTipoCon) {
        this.desTipoCon = desTipoCon;
    }

    public Date getConDesde() {
        return conDesde;
    }

    public void setConDesde(Date conDesde) {
        this.conDesde = conDesde;
    }

    public Date getConHasta() {
        return conHasta;
    }

    public void setConHasta(Date conHasta) {
        this.conHasta = conHasta;
    }

    public Integer getTotalDias() {
        return totalDias;
    }

    public void setTotalDias(Integer totalDias) {
        this.totalDias = totalDias;
    }
    
}
