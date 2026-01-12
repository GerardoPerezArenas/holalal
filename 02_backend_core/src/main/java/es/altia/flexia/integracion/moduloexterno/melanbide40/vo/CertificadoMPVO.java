package es.altia.flexia.integracion.moduloexterno.melanbide40.vo;

import java.util.ArrayList;
import java.util.Date;

/**
 * @author laura
 * @version 03/04/2014 1.0
 * 
 */
public class CertificadoMPVO {
    
    //Propiedades
    private String codCertificado;
    private String numExpediente;
    private Integer codOrganizacion;    
    private String descCertificadoC;
    private String descCertificadoE;
    private String codModPrac;
    private String descModPracC;
    private String descModPracE;

    public String getCodCertificado() {
        return codCertificado;
    }

    public void setCodCertificado(String codCertificado) {
        this.codCertificado = codCertificado;
    }

    public Integer getCodOrganizacion() {
        return codOrganizacion;
    }

    public void setCodOrganizacion(Integer codOrganizacion) {
        this.codOrganizacion = codOrganizacion;
    }

    public String getDescCertificadoC() {
        return descCertificadoC;
    }

    public void setDescCertificadoC(String descCertificadoC) {
        this.descCertificadoC = descCertificadoC;
    }

    public String getDescCertificadoE() {
        return descCertificadoE;
    }

    public void setDescCertificadoE(String descCertificadoE) {
        this.descCertificadoE = descCertificadoE;
    }

    public String getCodModPrac() {
        return codModPrac;
    }

    public void setCodModPrac(String codModPrac) {
        this.codModPrac = codModPrac;
    }

    public String getDescModPracC() {
        return descModPracC;
    }

    public void setDescModPracC(String descModPracC) {
        this.descModPracC = descModPracC;
    }

    public String getDescModPracE() {
        return descModPracE;
    }

    public void setDescModPracE(String descModPracE) {
        this.descModPracE = descModPracE;
    }

    public String getNumExpediente() {
        return numExpediente;
    }

    public void setNumExpediente(String numExpediente) {
        this.numExpediente = numExpediente;
    }

    
    
}//class
