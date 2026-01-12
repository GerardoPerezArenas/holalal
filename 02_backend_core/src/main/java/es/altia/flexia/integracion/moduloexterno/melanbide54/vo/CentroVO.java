package es.altia.flexia.integracion.moduloexterno.melanbide54.vo;

/**
 * Representa a una ubicación de un centro
 * @author oscar
 */
public class CentroVO {
    
    private String id;
    private String codTH;
    private String desCodTH;
    private String codMun;
    private String desCodMun;
    private String calle;
    private String portal;
    private String piso;
    private String letra;
    private String cp;
    private String tlef;
    private String email;
    private String codsepe;

 
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    
    public String getCodTH() {
        return codTH;
    }
    public void setCodTH(String codTH) {
        this.codTH = codTH;
    }

    
    public String getDesCodTH() {
        return desCodTH;
    }
    public void setDesCodTH(String desCodTH) {
        this.desCodTH = desCodTH;
    }
    
    
    public String getCodMun() {
        return codMun;
    }
    public void setCodMun(String codMun) {
        this.codMun = codMun;
    }
    
    
    public String getDesCodMun() {
        return desCodMun;
    }
    public void setDesCodMun(String desCodMun) {
        this.desCodMun = desCodMun;
    }

    
    public String getCalle() {
        return calle;
    }
    public void setCalle(String calle) {
        this.calle = calle;
    }

    
    public String getPortal() {
        return portal;
    }
    public void setPortal(String portal) {
        this.portal = portal;
    }

   
    public String getPiso() {
        return piso;
    }
    public void setPiso(String piso) {
        this.piso = piso;
    }

    
    public String getLetra() {
        return letra;
    }
    public void setLetra(String letra) {
        this.letra = letra;
    }

    
    public String getCp() {
        return cp;
    }
    public void setCp(String cp) {
        this.cp = cp;
    }

    
    public String getTlef() {
        return tlef;
    }
    public void setTlef(String tlef) {
        this.tlef = tlef;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if(email==null){
            email = "";
        }
        this.email = email;
    }

    public String getCodsepe() {
        return codsepe;
    }

    public void setCodsepe(String codsepe) {
        this.codsepe = codsepe;
    }
    

}
