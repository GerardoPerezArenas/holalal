
package es.altia.flexia.integracion.moduloexterno.melanbide91.vo;

import java.util.Date;

public class ContrGenVO {
    
    private Integer id;
    private String numExp;
    private String nombre;
    private String apellido1;
    private String apellido2;
    private String sexo;
    private String dessexo;
    private String dni;
    private Double psiquica;
    private Double fisica;
    private Double sensorial;
    private Date fecIni;
    private String fecIniStr;
    private String  jornada;
    private String  desjorn;
    /*private Integer porcParcial;*/
     private Double porcParcial;
    
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
    
    public String getDesSexo() {
        return dessexo;
    }

    public void setDesSexo(String dessexo) {
        this.dessexo = dessexo;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    
    public Double getPsiquica() {
        return psiquica;
    }

    public void setPsiquica(Double psiquica) {
        this.psiquica = psiquica;
    }

    public Double getFisica() {
        return fisica;
    }

    public void setFisica(Double fisica) {
        this.fisica = fisica;
    }

    public Double getSensorial() {
        return sensorial;
    }

    public void setSensorial(Double sensorial) {
        this.sensorial = sensorial;
    }

    public Date getFecIni() {
        return fecIni;
    }

    public void setFecIni(Date fecIni) {
        this.fecIni = fecIni;
    }
     
    public String getFecIniStr() {
        return fecIniStr;
    }

    public void setFecIniStr(String fecIniStr) {
        this.fecIniStr = fecIniStr;
    }
     
    public String getJornada() {
        return jornada;
    }

    public void setJornada(String jornada) {
        this.jornada = jornada;
    }  
    
    public String getDesJorn() {
        return desjorn;
    }

    public void setDesJorn(String desjorn) {
        this.desjorn = desjorn;
    }
    /*
    public Integer getPorcParcial() {
        return porcParcial;
    }

    public void setPorcParcial(Integer porcParcial) {
        this.porcParcial = porcParcial;
    }
    */
  
    public Double getPorcParcial() {
        return porcParcial;
    }

    public void setPorcParcial(Double porcParcial) {
        this.porcParcial = porcParcial;
    }
    
    
    
    
}
