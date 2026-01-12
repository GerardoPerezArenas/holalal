/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide35.vo.informes;

import es.altia.flexia.integracion.moduloexterno.melanbide35.util.ConstantesMeLanbide35;
import es.altia.flexia.integracion.moduloexterno.melanbide35.util.MeLanbide35Utils;
import java.util.Date;

/**
 *
 * @author santiagoc
 */
public class FilaInformeProyectos 
{
    private String entidad;
    private String empresa;
    private String actividad;
    private String fecAprobacion;
    private Double importe;
    private String insSeg;
    private Integer totalTrabajadores;
    private Integer h25;
    private Integer h25_54;
    private Integer h55;
    private Integer m25;
    private Integer m25_54;
    private Integer m55;
    private Integer totalPreparadores;
    private Integer prepIndefinido;
    private Integer prepTemporal;
    private String numExpediente;
    
    public FilaInformeProyectos()
    {
        entidad = "-";
        empresa = "-";
        actividad = "-";
        fecAprobacion = "-";
        importe = 0.0;
        insSeg = "-";
        totalTrabajadores = 0;
        h25 = 0;
        h25_54 = 0;
        h55 = 0;
        m25 = 0;
        m25_54 = 0;
        m55 = 0;
        totalPreparadores = 0;
        prepIndefinido = 0;
        prepTemporal = 0;
        numExpediente = "";
    }

    public String getEntidad() {
        return entidad;
    }

    public void setEntidad(String entidad) {
        this.entidad = entidad;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getActividad() {
        return actividad;
    }

    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    public String getFecAprobacion() {
        return fecAprobacion;
    }

    public void setFecAprobacion(String fecAprobacion) {
        this.fecAprobacion = fecAprobacion;
    }

    public Double getImporte() {
        return importe;
    }

    public void setImporte(Double importe) {
        this.importe = importe;
    }

    public String getInsSeg() {
        return insSeg;
    }

    public void setInsSeg(String insSeg) {
        this.insSeg = insSeg;
    }

    public Integer getTotalTrabajadores() {
        return totalTrabajadores;
    }

    public void setTotalTrabajadores(Integer totalTrabajadores) {
        this.totalTrabajadores = totalTrabajadores;
    }

    public Integer getH25() {
        return h25;
    }

    public void setH25(Integer h25) {
        this.h25 = h25;
    }

    public Integer getH25_54() {
        return h25_54;
    }

    public void setH25_54(Integer h25_54) {
        this.h25_54 = h25_54;
    }

    public Integer getH55() {
        return h55;
    }

    public void setH55(Integer h55) {
        this.h55 = h55;
    }

    public Integer getM25() {
        return m25;
    }

    public void setM25(Integer m25) {
        this.m25 = m25;
    }

    public Integer getM25_54() {
        return m25_54;
    }

    public void setM25_54(Integer m25_54) {
        this.m25_54 = m25_54;
    }

    public Integer getM55() {
        return m55;
    }

    public void setM55(Integer m55) {
        this.m55 = m55;
    }

    public Integer getTotalPreparadores() {
        return totalPreparadores;
    }

    public void setTotalPreparadores(Integer totalPreparadores) {
        this.totalPreparadores = totalPreparadores;
    }

    public Integer getPrepIndefinido() {
        return prepIndefinido;
    }

    public void setPrepIndefinido(Integer prepIndefinido) {
        this.prepIndefinido = prepIndefinido;
    }

    public Integer getPrepTemporal() {
        return prepTemporal;
    }

    public void setPrepTemporal(Integer prepTemporal) {
        this.prepTemporal = prepTemporal;
    }

    public String getNumExpediente() {
        return numExpediente;
    }

    public void setNumExpediente(String numExpediente) {
        this.numExpediente = numExpediente;
    }
    
    public void anadir(int sexo, Date fecNacimiento)
    {
        switch(sexo)
        {
            case ConstantesMeLanbide35.CODIGOS_SEXO.HOMBRE:
                anadirHombre(fecNacimiento);
                break;
            case ConstantesMeLanbide35.CODIGOS_SEXO.MUJER:
                anadirMujer(fecNacimiento);
                break;
        }
    }
    
    private void anadirHombre(Date fecNacimiento)
    {
        int anos = MeLanbide35Utils.calcularEdad(fecNacimiento);
        if(anos < 25)
        {
            h25++;
        }
        else if(anos >= 25 && anos < 55)
        {
            h25_54++;
        }
        else if(anos >= 55)
        {
            h55++;
        }
        totalTrabajadores++;
    }
    
    private void anadirMujer(Date fecNacimiento)
    {
        int anos = MeLanbide35Utils.calcularEdad(fecNacimiento);
        if(anos < 25)
        {
            m25++;
        }
        else if(anos >= 25 && anos < 55)
        {
            m25_54++;
        }
        else if(anos >= 55)
        {
            m55++;
        }
        totalTrabajadores++;
    }
    
    public boolean equals(Object o)
    {
        if(o instanceof FilaInformeProyectos)
        {
            FilaInformeProyectos aux = (FilaInformeProyectos)o;
            if(this.getEmpresa() != null && aux.getEmpresa() != null)
            {
                return this.getEmpresa().equalsIgnoreCase(aux.getEmpresa());
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }
}
