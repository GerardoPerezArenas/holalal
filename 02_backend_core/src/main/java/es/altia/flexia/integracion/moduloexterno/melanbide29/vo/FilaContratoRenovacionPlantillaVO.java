/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide29.vo;

/**
 *
 * @author santiagoc
 * Esta clase representa una fila de la tabla de contratos de renovacion
 */
public class FilaContratoRenovacionPlantillaVO 
{
    private String numExpediente = "";//PEXCO_NUM
    private Integer numContrato = -1;//PEXCO_NCON
    private String dni1 = "";
    private String nomApe1 = "";
    private String dni2 = "";
    private String nomApe2 = "";
    private String dni3 = "";
    private String nomApe3 = "";
    
    public FilaContratoRenovacionPlantillaVO()
    {
        
    }

    public String getNumExpediente() {
        return numExpediente;
    }

    public void setNumExpediente(String numExpediente) {
        this.numExpediente = numExpediente;
    }

    public Integer getNumContrato() {
        return numContrato;
    }

    public void setNumContrato(Integer numContrato) {
        this.numContrato = numContrato;
    }

    public String getDni1() {
        return dni1;
    }

    public void setDni1(String dni1) {
        this.dni1 = dni1;
    }

    public String getNomApe1() {
        return nomApe1;
    }

    public void setNomApe1(String nomApe1) {
        this.nomApe1 = nomApe1;
    }

    public String getDni2() {
        return dni2;
    }

    public void setDni2(String dni2) {
        this.dni2 = dni2;
    }

    public String getNomApe2() {
        return nomApe2;
    }

    public void setNomApe2(String nomApe2) {
        this.nomApe2 = nomApe2;
    }

    public String getDni3() {
        return dni3;
    }

    public void setDni3(String dni3) {
        this.dni3 = dni3;
    }

    public String getNomApe3() {
        return nomApe3;
    }

    public void setNomApe3(String nomApe3) {
        this.nomApe3 = nomApe3;
    }
}
