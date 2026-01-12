package es.altia.flexia.integracion.moduloexterno.melanbide15.vo;

import java.sql.Date;

public class ContratacionVO {
    
    private Integer id;

    private String numExp;
    
  
    private String dniCon;
    private String descDniCon;
    private String numIdenCon;
    private Date fecIniCon;
    private String fecIniConStr;
    private Date fecFinCon;
    private String fecFinConStr;
    private Double subvencionCon;

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

    public String getDniCon() {
        return dniCon;
    }

    public void setDniCon(String dniCon) {
        this.dniCon = dniCon;
    }

    public String getDescDniCon() {
        return descDniCon;
    }

    public void setDescDniCon(String descDniCon) {
        this.descDniCon = descDniCon;
    }

    public String getNumIdenCon() {
        return numIdenCon;
    }

    public void setNumIdenCon(String numIdenCon) {
        this.numIdenCon = numIdenCon;
    }

   
    public Date getFecIniCon() {
        return fecIniCon;
    }

    public void setFecIniCon(Date fecIniCon) {
        this.fecIniCon = fecIniCon;
    }

    public String getFecIniConStr() {
        return fecIniConStr;
    }

    public void setFecIniConStr(String fecIniConStr) {
        this.fecIniConStr = fecIniConStr;
    }

    public Date getFecFinCon() {
        return fecFinCon;
    }

    public void setFecFinCon(Date fecFinCon) {
        this.fecFinCon = fecFinCon;
    }

    public String getFecFinConStr() {
        return fecFinConStr;
    }

    public void setFecFinConStr(String fecFinConStr) {
        this.fecFinConStr = fecFinConStr;
    }

    public Double getSubvencionCon() {
        return subvencionCon;
    }

    public void setSubvencionCon(Double subvencionCon) {
        this.subvencionCon = subvencionCon;
    }

   
    
}
