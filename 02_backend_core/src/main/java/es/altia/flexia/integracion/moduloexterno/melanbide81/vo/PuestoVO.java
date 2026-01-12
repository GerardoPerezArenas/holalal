package es.altia.flexia.integracion.moduloexterno.melanbide81.vo;

/**
 *
 * @author kepa
 */
public class PuestoVO {

    private Integer id;
    private Integer idProyecto;
    private Integer idPrioridadProyecto;
    private String numExp;
    private String denominacion;
    private Double duracion;
    private Double porcJorn;
    private Integer numContratos;
    private Double coste;
    private Double subvencion;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(Integer idProyecto) {
        this.idProyecto = idProyecto;
    }

    public Integer getIdPrioridadProyecto() {
        return idPrioridadProyecto;
    }

    public void setIdPrioridadProyecto(Integer idPrioridadProyecto) {
        this.idPrioridadProyecto = idPrioridadProyecto;
    }

    public String getNumExp() {
        return numExp;
    }

    public void setNumExp(String numExp) {
        this.numExp = numExp;
    }

    public String getDenominacion() {
        return denominacion;
    }

    public void setDenominacion(String denominacion) {
        this.denominacion = denominacion;
    }

    public Double getDuracion() {
        return duracion;
    }

    public void setDuracion(Double duracion) {
        this.duracion = duracion;
    }

    public Double getPorcJorn() {
        return porcJorn;
    }

    public void setPorcJorn(Double porcJorn) {
        this.porcJorn = porcJorn;
    }

    public Integer getNumContratos() {
        return numContratos;
    }

    public void setNumContratos(Integer numContratos) {
        this.numContratos = numContratos;
    }

    public Double getCoste() {
        return coste;
    }

    public void setCoste(Double coste) {
        this.coste = coste;
    }

    public Double getSubvencion() {
        return subvencion;
    }

    public void setSubvencion(Double subvencion) {
        this.subvencion = subvencion;
    }

}
