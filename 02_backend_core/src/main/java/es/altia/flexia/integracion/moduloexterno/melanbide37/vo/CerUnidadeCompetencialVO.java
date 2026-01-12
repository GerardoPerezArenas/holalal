package es.altia.flexia.integracion.moduloexterno.melanbide37.vo;

/**
 * @author david.caamano
 * @version 16/08/2012 1.0
 * Historial de cambios:
 * <ol>
 *  <li>david.caamano * 16-08-2012 * #53275 Edici�n inicial</li>
 * </ol> 
 */
public class CerUnidadeCompetencialVO {
    
    //Propiedades
    private String codUnidad;
    private String descUnidadC;
    private String descUnidadE;
    private Integer nivel;
    
    private String centroAcreditado;
    private String codCentro;
    private String descCentro;
    private String codCertificado;
    private String codOrganizacion;
    private String codProcedimiento;
    private String numExpediente;
    
    private String codMotNoAcreditado;
    private String codOrigenAcred;
    
    private String bloquearEnPantalla;
    private String claveRegistral;
    
    //M�todos de acceso

    public String getCodUnidad() {
        return codUnidad;
    }//getCodUnidad
    public void setCodUnidad(String codUnidad) {
        this.codUnidad = codUnidad;
    }//setCodUnidad

    public String getDescUnidadC() {
        return descUnidadC;
    }//getDescUnidadC
    public void setDescUnidadC(String descUnidadC) {
        this.descUnidadC = descUnidadC;
    }//setDescUnidadC

    public String getDescUnidadE() {
        return descUnidadE;
    }//getDescUnidadE

    public void setDescUnidadE(String descUnidadE) {
        this.descUnidadE = descUnidadE;
    }//setDescUnidadE

    public Integer getNivel() {
        return nivel;
    }//getNivel

    public void setNivel(Integer nivel) {
        this.nivel = nivel;
    }//setNivel

    public String getCentroAcreditado() {
        return centroAcreditado;
    }//getCentroAcreditado
    public void setCentroAcreditado(String centroAcreditado) {
        this.centroAcreditado = centroAcreditado;
    }//setCentroAcreditado

    public String getCodCentro() {
        return codCentro;
    }//getCodCentro
    public void setCodCentro(String codCentro) {
        this.codCentro = codCentro;
    }//setCodCentro
    
    public String getDescCentro(){
        return descCentro;
    }//getDescCentro
    public void setDescCentro(String descCentro){
        this.descCentro = descCentro;
    }//setDescCentro

    public String getCodCertificado() {
        return codCertificado;
    }//getCodCertificado
    public void setCodCertificado(String codCertificado) {
        this.codCertificado = codCertificado;
    }//setCodCertificado
    
    public String getCodOrganizacion() {
        return codOrganizacion;
    }//getCodOrganizacion
    public void setCodOrganizacion(String codOrganizacion) {
        this.codOrganizacion = codOrganizacion;
    }//setCodOrganizacion
    
    public String getCodProcedimiento() {
        return codProcedimiento;
    }//getCodProcedimiento
    public void setCodProcedimiento(String codProcedimiento) {
        this.codProcedimiento = codProcedimiento;
    }//setCodProcedimiento

    public String getNumExpediente() {
        return numExpediente;
    }//getNumExpediente
    public void setNumExpediente(String numExpediente) {
        this.numExpediente = numExpediente;
    }//setNumExpediente

    public String getCodMotNoAcreditado() {
        return codMotNoAcreditado;
    }//getCodMotNoAcreditado
    public void setCodMotNoAcreditado(String codMotNoAcreditado) {
        this.codMotNoAcreditado = codMotNoAcreditado;
    }//setCodMotNoAcreditado

    public String getCodOrigenAcred() {
        return codOrigenAcred;
    }//getCodOrigenAcred
    public void setCodOrigenAcred(String codOrigenAcred) {
        this.codOrigenAcred = codOrigenAcred;
    }//setCodOrigenAcred
    
    public String getBloquearEnPantalla(){
        return this.bloquearEnPantalla;
    }//getBloquearEnPantalla
    public void setBloquearEnPantalla(String bloquearEnPantalla){
        this.bloquearEnPantalla = bloquearEnPantalla;
    }//setBloquearEnPantalla

    public String getClaveRegistral() {
        return claveRegistral;
    }//getClaveRegistral
    public void setClaveRegistral(String claveRegistral) {
        this.claveRegistral = claveRegistral;
    }//setClaveRegistral
    
    public String toString(){
        StringBuilder sb = new StringBuilder();
            sb.append(("CerUnidadeCompetencialVO { "));
            sb.append("codUnidad = ");
            sb.append(codUnidad);
            sb.append(" ");
            sb.append("descUnidadC = ");
            sb.append(descUnidadC);
            sb.append(" ");
            sb.append("descUnidadE = ");
            sb.append(descUnidadE);
            sb.append(" ");
            sb.append(("nivel = "));
            sb.append(nivel);
            sb.append(("centroAcreditado = "));
            sb.append(centroAcreditado);
            sb.append(" ");
            sb.append(("codCentro = "));
            sb.append(codCentro);
            sb.append(" ");
            sb.append(("codCertificado = "));
            sb.append(codCertificado);
            sb.append(" ");
            sb.append(("codOrganizacion = "));
            sb.append(codOrganizacion);
            sb.append(" ");
            sb.append(("codProcedimiento = "));
            sb.append(codProcedimiento);
            sb.append(" ");
            sb.append(("numExpediente = "));
            sb.append(numExpediente);
            sb.append(" ");
            sb.append("codMotNoAcreditado = ");
            sb.append(codMotNoAcreditado);
            sb.append(" ");
            sb.append("codOrigenAcred = ");
            sb.append(codOrigenAcred);
            sb.append(" ");
            sb.append("bloquearEnPantalla = ");
            sb.append(bloquearEnPantalla);
            sb.append("claveRegistral = ");
            sb.append(claveRegistral);
            sb.append((" }; "));
        return sb.toString();
    }//toString
    
}//class
