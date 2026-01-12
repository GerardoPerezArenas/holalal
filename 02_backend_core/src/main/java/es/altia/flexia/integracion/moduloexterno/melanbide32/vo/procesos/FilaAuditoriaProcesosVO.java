/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide32.vo.procesos;

/**
 *
 * @author santiagoc
 */
public class FilaAuditoriaProcesosVO 
{
    private String nomApellidos;
    private String proceso;
    private String fecha;
    private String resultado;
    
    public FilaAuditoriaProcesosVO()
    {
        
    }

    public String getNomApellidos() {
        return nomApellidos;
    }

    public void setNomApellidos(String nomApellidos) {
        this.nomApellidos = nomApellidos;
    }

    public String getProceso() {
        return proceso;
    }

    public void setProceso(String proceso) {
        this.proceso = proceso;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }
}
