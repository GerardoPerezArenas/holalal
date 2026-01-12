package es.altia.flexia.integracion.moduloexterno.melanbide66.vo;

import java.sql.Date;

/**
 *
 * @author altia
 */
public class TerceroVO {
    
    private String codTer;
    private String versionTercero;
    private String tipoDoc;
    private String doc;
    private String nombre;
    private String apellido1;
    private String apellido2;
    private String nombreCompleto;
    private String numSoporte;
    private String tSexoTercero;
    private Date tFecNacimiento;
    private String tNacionTercero;
    private String codRol;
    private String rol;
    private int edad;//se calcula en base a la fecha de nacimiento y fecha resolución inicio expdte
 
    public TerceroVO()
    {
        
    }

    public String getCodTer() {
        return codTer;
    }

    public void setCodTer(String codTer) {
        this.codTer = codTer;
    }
    
    public String getTipoDoc() {
        return tipoDoc;
    }
        
    public void setTipoDoc(String tipoDoc) {
        this.tipoDoc = tipoDoc;
    }

    public String getDoc() {
        return doc;
    }
    
    public void setDoc(String doc) {
        this.doc = doc;
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
    
    public String getNumSoporte() {
        return numSoporte;
    }

    public void setNumSoporte(String numSoporte) {
        this.numSoporte = numSoporte;
    }
    
    public String getTSexoTercero() {
        return tSexoTercero;
    }

    public void setTSexoTercero(String tSexoTercero) {
        this.tSexoTercero = tSexoTercero;
    }
    
    public Date getTFecNacimiento() {
        return tFecNacimiento;
    }

    public void setTFecNacimiento(Date tFecNacimiento) {
        this.tFecNacimiento = tFecNacimiento;
    }
    
    public String getTNacionTercero() {
        return tNacionTercero;
    }

    public void setTNacionTercero(String tNacionTercero) {
        this.tNacionTercero = tNacionTercero;
    }
    
    
    public String getCodRol() {
        return codRol;
    }

    public void setCodRol(String codRol) {
        this.codRol = codRol;
    }
    
    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
     public String getVersionTercero() {
        return versionTercero;
    }

    public void setVersionTercero(String versionTercero) {
        this.versionTercero = versionTercero;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    
    
}
