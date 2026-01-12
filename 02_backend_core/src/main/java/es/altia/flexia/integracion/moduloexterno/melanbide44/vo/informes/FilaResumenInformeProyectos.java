/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide44.vo.informes;

/**
 *
 * @author santiagoc
 */
public class FilaResumenInformeProyectos implements Comparable
{
    private String numExp;
    private String codProvincia;
    private String descProvincia;
    private String entidad;
    private Double importeSubvencion;
    private Integer totalPrep;
    private Integer prepIndef;
    private Integer prepTempo;
    
    public FilaResumenInformeProyectos()
    {
        
    }

    public String getNumExp() {
        return numExp;
    }

    public void setNumExp(String numExp) {
        this.numExp = numExp;
    }

    public String getCodProvincia() {
        return codProvincia;
    }

    public void setCodProvincia(String codProvincia) {
        this.codProvincia = codProvincia;
    }

    public String getDescProvincia() {
        return descProvincia;
    }

    public void setDescProvincia(String descProvincia) {
        this.descProvincia = descProvincia;
    }

    public String getEntidad() {
        return entidad;
    }

    public void setEntidad(String entidad) {
        this.entidad = entidad;
    }

    public Double getImporteSubvencion() {
        return importeSubvencion;
    }

    public void setImporteSubvencion(Double importeSubvencion) {
        this.importeSubvencion = importeSubvencion;
    }

    public Integer getTotalPrep() {
        return totalPrep;
    }

    public void setTotalPrep(Integer totalPrep) {
        this.totalPrep = totalPrep;
    }

    public Integer getPrepIndef() {
        return prepIndef;
    }

    public void setPrepIndef(Integer prepIndef) {
        this.prepIndef = prepIndef;
    }

    public Integer getPrepTempo() {
        return prepTempo;
    }

    public void setPrepTempo(Integer prepTempo) {
        this.prepTempo = prepTempo;
    }
    
    @Override
    public boolean equals(Object o)
    {
        if(o instanceof FilaResumenInformeProyectos)
        {
            FilaResumenInformeProyectos aux = (FilaResumenInformeProyectos)o;
            if(this.getNumExp() != null && aux.getNumExp() != null)
            {
                return this.getNumExp().equalsIgnoreCase(aux.getNumExp());
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

    public int compareTo(Object o) 
    {
        if(o instanceof FilaResumenInformeProyectos)
        {
            FilaResumenInformeProyectos aux = (FilaResumenInformeProyectos)o;
            if(this.getCodProvincia() != null && !this.getCodProvincia().equals("") && aux.getCodProvincia() != null && !aux.getCodProvincia().equals(""))
            {
                return this.getCodProvincia().compareTo(aux.getCodProvincia());
            }
            else if((this.getCodProvincia() == null || this.getCodProvincia().equals("")) && (aux.getCodProvincia() == null || aux.getCodProvincia().equals("")))
            {
                return 0;
            }
            else if(this.getCodProvincia() != null && !this.getCodProvincia().equals(""))
            {
                return -1;
            }
            else
            {
                return 1;
            }
        }
        else
        {
            return -1;
        }
    }
}
