package es.altia.flexia.integracion.moduloexterno.melanbide37.vo;

public class ExpCEPAPCriteriosFiltroVO {
 
    /*private String fechaExpInicio;
    private String fechaExpFin;*/
    private String dniInteresado;
    private String numeroExpediente;
    
    // Parámetros para paginación
    private Integer numeroLineasxPagina;
    private Integer paginaActual;
    private Integer numeroInicialLinea;
    private Integer numeroFinalLinea;

    /*public String getFechaExpInicio() {
        return fechaExpInicio;
    }

    public void setFechaExpInicio(String fechaExpInicio) {
        this.fechaExpInicio = fechaExpInicio;
    }

    public String getFechaExpFin() {
        return fechaExpFin;
    }

    public void setFechaExpFin(String fechaExpFin) {
        this.fechaExpFin = fechaExpFin;
    }*/

    public String getDniInteresado() {
        return dniInteresado;
    }

    public void setDniInteresado(String dniInteresado) {
        this.dniInteresado = dniInteresado;
    }

    public String getNumeroExpediente() {
        return numeroExpediente;
    }

    public void setNumeroExpediente(String numeroExpediente) {
        this.numeroExpediente = numeroExpediente;
    }

    public Integer getNumeroLineasxPagina() {
        return numeroLineasxPagina;
    }

    public void setNumeroLineasxPagina(Integer numeroLineasxPagina) {
        this.numeroLineasxPagina = numeroLineasxPagina;
    }

    public Integer getPaginaActual() {
        return paginaActual;
    }

    public void setPaginaActual(Integer paginaActual) {
        this.paginaActual = paginaActual;
    }

    public Integer getNumeroInicialLinea() {
        return numeroInicialLinea;
    }

    public void setNumeroInicialLinea(Integer numeroInicialLinea) {
        this.numeroInicialLinea = numeroInicialLinea;
    }

    public Integer getNumeroFinalLinea() {
        return numeroFinalLinea;
    }

    public void setNumeroFinalLinea(Integer numeroFinalLinea) {
        this.numeroFinalLinea = numeroFinalLinea;
    }
    
        
}
