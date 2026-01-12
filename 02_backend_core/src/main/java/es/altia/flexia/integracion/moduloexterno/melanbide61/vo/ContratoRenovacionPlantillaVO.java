/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide61.vo;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author santiagoc
 */
public class ContratoRenovacionPlantillaVO {
    
    private Integer entorno;//PEXCO_MUN
    private Integer ejercicio;//PEXCO_EJE
    private String procedimiento;//PEXCO_PRO
    private String numExpediente;//PEXCO_NUM
    private Integer numContrato;//PEXCO_NCON
    private List<PersonaContratoRenovacionPlantillaVO> personas;//Las tres personas(Contratada, Contratada Adicional y Sustituida)
    
    public ContratoRenovacionPlantillaVO()
    {
        
    }

    public Integer getEntorno() {
        return entorno;
    }

    public void setEntorno(Integer entorno) {
        this.entorno = entorno;
    }

    public Integer getEjercicio() {
        return ejercicio;
    }

    public void setEjercicio(Integer ejercicio) {
        this.ejercicio = ejercicio;
    }

    public String getProcedimiento() {
        return procedimiento;
    }

    public void setProcedimiento(String procedimiento) {
        this.procedimiento = procedimiento;
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

    public List<PersonaContratoRenovacionPlantillaVO> getPersonas() 
    {
        return personas;
    }

    public void setPersonas(List<PersonaContratoRenovacionPlantillaVO> personas) 
    {
        this.personas = personas;
    }
    
    public void addPersona(PersonaContratoRenovacionPlantillaVO persona)
    {
        if(personas == null)
        {
            personas = new ArrayList<PersonaContratoRenovacionPlantillaVO>();
        }
        if(!personas.contains(persona))
        {
            this.personas.add(persona);
        }
    }
}
