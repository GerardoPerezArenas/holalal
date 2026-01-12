package es.altia.flexia.integracion.moduloexterno.melanbide53.vo;

/**
 * Representa a una ubicación de un centro
 * @author davidg
 */
public class RegistroIdenErrorCriteriosFiltroVO {
    
    private String id;
    private String descripcion;
    private String codTipo;
    private String codCritico;
    
    // Parametros para paginación
    private Integer numeroLineasxPagina;
    private Integer paginaActual;
    private Integer numeroInicialLinea;
    private Integer numeroFinalLinea;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCodTipo() {
        return codTipo;
    }

    public void setCodTipo(String codTipo) {
        this.codTipo = codTipo;
    }

    public String getCodCritico() {
        return codCritico;
    }

    public void setCodCritico(String codCritico) {
        this.codCritico = codCritico;
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
