package es.altia.flexia.integracion.moduloexterno.melanbide15.vo;

import java.sql.Date;

public class IdentidadVO {
    
    private Integer id;

    private String numExp;
    
    private String dniNie;
    private String descDniNie;
    private String numIden;
    private String nombre;
    private String apellido1;
    private String apellido2;
    private String sexo;
    private String descSexo;
   
    private Date fechaNacimiento;
    private String fecNacStr;
    
    private String sustituto;
    private String descSustituto;

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

    public String getDniNie() {
        return dniNie;
    }

    public void setDniNie(String dniNie) {
        this.dniNie = dniNie;
    }

    public String getDescDniNie() {
        return descDniNie;
    }

    public void setDescDniNie(String descDniNie) {
        this.descDniNie = descDniNie;
    }

    public String getNumIden() {
        return numIden;
    }

    public void setNumIden(String numIden) {
        this.numIden = numIden;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getDescSexo() {
        return descSexo;
    }

    public void setDescSexo(String descSexo) {
        this.descSexo = descSexo;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getFecNacStr() {
        return fecNacStr;
    }

    public void setFecNacStr(String fecNacStr) {
        this.fecNacStr = fecNacStr;
    }

    public String getSustituto() {
        return sustituto;
    }

    public void setSustituto(String sustituto) {
        this.sustituto = sustituto;
    }

    public String getDescSustituto() {
        return descSustituto;
    }

    public void setDescSustituto(String descSustituto) {
        this.descSustituto = descSustituto;
    }

    

}
