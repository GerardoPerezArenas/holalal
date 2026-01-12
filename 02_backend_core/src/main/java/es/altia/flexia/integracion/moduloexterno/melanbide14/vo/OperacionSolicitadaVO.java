package es.altia.flexia.integracion.moduloexterno.melanbide14.vo;

import java.sql.Date;

/**
 *
 * @author kigonzalez
 */
public class OperacionSolicitadaVO {

    private Integer id;
    private String numExp;
    private Integer ejeOperacion;
    private Integer numOpePre;
    private String prioridad;
    private String descPrioridad;
    private String objetivo;
    private String descObjetivo;
    private String tipologia;
    private String descTipologia;
    private Date fecInicio;
    private String fecInicioStr;
    private Date fecFin;
    private String fecFinStr;
    private String entidad;
    private String descEntidad;
    private String organismo;

    public OperacionSolicitadaVO() {
    }

    /**
     *
     * @param id
     * @param numExp
     * @param ejeOperacion
     * @param numOpePre
     * @param prioridad
     * @param objetivo
     * @param tipologia
     * @param fecInicio
     * @param fecFin
     * @param entidad
     * @param organismo
     */
    public OperacionSolicitadaVO(Integer id, String numExp, Integer ejeOperacion, Integer numOpePre, String prioridad, String objetivo, String tipologia, Date fecInicio, Date fecFin, String entidad, String organismo) {
        this.id = id;
        this.numExp = numExp;
        this.ejeOperacion = ejeOperacion;
        this.numOpePre = numOpePre;
        this.prioridad = prioridad;
        this.objetivo = objetivo;
        this.tipologia = tipologia;
        this.fecInicio = fecInicio;
        this.fecFin = fecFin;
        this.entidad = entidad;
        this.organismo = organismo;
    }

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

    public Integer getEjeOperacion() {
        return ejeOperacion;
    }

    public void setEjeOperacion(Integer ejeOperacion) {
        this.ejeOperacion = ejeOperacion;
    }

    public Integer getNumOpePre() {
        return numOpePre;
    }

    public void setNumOpePre(Integer numOpePre) {
        this.numOpePre = numOpePre;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public String getDescPrioridad() {
        return descPrioridad;
    }

    public void setDescPrioridad(String descPrioridad) {
        this.descPrioridad = descPrioridad;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public String getDescObjetivo() {
        return descObjetivo;
    }

    public void setDescObjetivo(String descObjetivo) {
        this.descObjetivo = descObjetivo;
    }

    public String getTipologia() {
        return tipologia;
    }

    public void setTipologia(String tipologia) {
        this.tipologia = tipologia;
    }

    public String getDescTipologia() {
        return descTipologia;
    }

    public void setDescTipologia(String descTipologia) {
        this.descTipologia = descTipologia;
    }

    public Date getFecInicio() {
        return fecInicio;
    }

    public void setFecInicio(Date fecInicio) {
        this.fecInicio = fecInicio;
    }

    public String getFecInicioStr() {
        return fecInicioStr;
    }

    public void setFecInicioStr(String fecInicioStr) {
        this.fecInicioStr = fecInicioStr;
    }

    public Date getFecFin() {
        return fecFin;
    }

    public void setFecFin(Date fecFin) {
        this.fecFin = fecFin;
    }

    public String getFecFinStr() {
        return fecFinStr;
    }

    public void setFecFinStr(String fecFinStr) {
        this.fecFinStr = fecFinStr;
    }

    public String getEntidad() {
        return entidad;
    }

    public void setEntidad(String entidad) {
        this.entidad = entidad;
    }

    public String getDescEntidad() {
        return descEntidad;
    }

    public void setDescEntidad(String descEntidad) {
        this.descEntidad = descEntidad;
    }

    public String getOrganismo() {
        return organismo;
    }

    public void setOrganismo(String organismo) {
        this.organismo = organismo;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("OperacionPresentadaVO{");
        sb.append("id= ").append(id);
        sb.append(", numExp= ").append(numExp);
        sb.append(", ejeOperacion= ").append(ejeOperacion);
        sb.append(", numOpePre= ").append(numOpePre);
        sb.append(", prioridad= ").append(prioridad);
        sb.append(", objetivo= ").append(objetivo);
        sb.append(", tipologia= ").append(tipologia);
        sb.append(", fecInicio= ").append(fecInicio);
        sb.append(", fecFin= ").append(fecFin);
        sb.append(", entidad= ").append(entidad);
        sb.append(", organismo= ").append(organismo);
        sb.append('}');
        return sb.toString();
    }

}
