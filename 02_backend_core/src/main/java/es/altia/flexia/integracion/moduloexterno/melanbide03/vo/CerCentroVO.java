package es.altia.flexia.integracion.moduloexterno.melanbide03.vo;

import java.io.Serializable;

/**
 * @author david.caamano
 * @version 16/08/2012 1.0
 * Historial de cambios:
 * <ol>
 *  <li>david.caamano * 16-08-2012 * #53275 Edición inicial</li>
 * </ol> 
 */
public class CerCentroVO implements Serializable {
    
    //Propiedades
    private String codCentro;
    private String titular;
    private String personaContacto;
    private Integer provincia;
    private Integer municipio;
    private String direccion;
    private String codPostal;
    private String telefono;
    private String fax;
    private String eMail;
    
    //Métodos de acceso

    public String getCodCentro() {
        return codCentro;
    }//getCodCentro
    public void setCodCentro(String codCentro) {
        this.codCentro = codCentro;
    }//setCodCentro

    public String getTitular() {
        return titular;
    }//getTitular
    public void setTitular(String titular) {
        this.titular = titular;
    }//setTitular
    
    public String getPersonaContacto() {
        return personaContacto;
    }//getPersonaContacto
    public void setPersonaContacto(String personaContacto) {
        this.personaContacto = personaContacto;
    }//setPersonaContacto
    
    public Integer getProvincia() {
        return provincia;
    }//getProvincia
    public void setProvincia(Integer provincia) {
        this.provincia = provincia;
    }//setProvincia
    
    public Integer getMunicipio() {
        return municipio;
    }//getMunicipio
    public void setMunicipio(Integer municipio) {
        this.municipio = municipio;
    }//setMunicipio
    
    public String getDireccion() {
        return direccion;
    }//getDireccion
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }//setDireccion
    
    public String getCodPostal() {
        return codPostal;
    }//getCodPostal
    public void setCodPostal(String codPostal) {
        this.codPostal = codPostal;
    }//setCodPostal

    public String getTelefono() {
        return telefono;
    }//getTelefono
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }//setTelefono

    public String getFax() {
        return fax;
    }//getFax
    public void setFax(String fax) {
        this.fax = fax;
    }//setFax

    public String geteMail() {
        return eMail;
    }//geteMail
    public void seteMail(String eMail) {
        this.eMail = eMail;
    }//seteMail
    
    public String toString(){
        StringBuilder sb = new StringBuilder();
            sb.append("CerCentroVO { ");
            sb.append("codCentro = ");
            sb.append(codCentro);
            sb.append((" "));
            sb.append("titular = ");
            sb.append(titular);
            sb.append((" "));
            sb.append("personaContacto = ");
            sb.append(personaContacto);
            sb.append((" "));
            sb.append("provincia = ");
            sb.append(provincia);
            sb.append((" "));
            sb.append("municipio = ");
            sb.append(municipio);
            sb.append((" "));
            sb.append("direccion = ");
            sb.append(direccion);
            sb.append((" "));
            sb.append("codPostal = ");
            sb.append(codPostal);
            sb.append((" "));
            sb.append("telefono = ");
            sb.append(telefono);
            sb.append((" "));
            sb.append("fax = ");
            sb.append(fax);
            sb.append((" "));
            sb.append("eMail = ");
            sb.append(eMail);
            sb.append((" };"));
        return sb.toString();
    }//toString
    
}//class
