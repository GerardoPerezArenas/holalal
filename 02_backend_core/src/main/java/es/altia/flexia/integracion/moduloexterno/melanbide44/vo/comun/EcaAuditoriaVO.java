/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide44.vo.comun;

import java.util.Date;

/**
 *
 * @author santiagoc
 */
public class EcaAuditoriaVO 
{
    private Integer usuCod;
    private String nombreApellidos;
    private Integer procedimiento;
    private String descProcedimiento;
    private Date fecEjecucion;
    private String resultado;
    
    public EcaAuditoriaVO()
    {
        
    }

    public Integer getUsuCod() {
        return usuCod;
    }

    public void setUsuCod(Integer usuCod) {
        this.usuCod = usuCod;
    }

    public String getNombreApellidos() {
        return nombreApellidos;
    }

    public void setNombreApellidos(String nombreApellidos) {
        this.nombreApellidos = nombreApellidos;
    }

    public Integer getProcedimiento() {
        return procedimiento;
    }

    public void setProcedimiento(Integer procedimiento) {
        this.procedimiento = procedimiento;
    }

    public String getDescProcedimiento() {
        return descProcedimiento;
    }

    public void setDescProcedimiento(String descProcedimiento) {
        this.descProcedimiento = descProcedimiento;
    }

    public Date getFecEjecucion() {
        return fecEjecucion;
    }

    public void setFecEjecucion(Date fecEjecucion) {
        this.fecEjecucion = fecEjecucion;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }
}
