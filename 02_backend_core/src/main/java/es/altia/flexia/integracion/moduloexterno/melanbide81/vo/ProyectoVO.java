package es.altia.flexia.integracion.moduloexterno.melanbide81.vo;

/**
 *
 * @author kepa
 */
public class ProyectoVO {

    private Integer id;
    private String numExp;
    private Integer prioridad;
    private String denominacion;
    private String entidad;
    private String tipoProyecto;
    private String descTipoProyecto;
    private Integer fases;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumExp() {
        return numExp;
    }

    public void setNumExp(String numExp) {
        this.numExp = numExp;
    }

    public Integer getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(Integer prioridad) {
        this.prioridad = prioridad;
    }

    public String getDenominacion() {
        return denominacion;
    }

    public void setDenominacion(String denominacion) {
        this.denominacion = denominacion;
    }

    public String getEntidad() {
        return entidad;
    }

    public void setEntidad(String entidad) {
        this.entidad = entidad;
    }

    public String getTipoProyecto() {
        return tipoProyecto;
    }

    public void setTipoProyecto(String tipoProyecto) {
        this.tipoProyecto = tipoProyecto;
    }

    public String getDescTipoProyecto() {
        return descTipoProyecto;
    }

    public void setDescTipoProyecto(String descTipoProyecto) {
        this.descTipoProyecto = descTipoProyecto;
    }

    public Integer getFases() {
        return fases;
    }

    public void setFases(Integer fases) {
        this.fases = fases;
    }

}
