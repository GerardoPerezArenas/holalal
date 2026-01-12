package es.altia.flexia.integracion.moduloexterno.melanbide53.vo;

/**
 * Representa a una ubicación de un centro
 * @author davidg
 */
public class RegistroErrorCriteriosFiltroVO {
    
    private Integer id;
    private String fechaErrorInicio;
    private String fechaErrorFin;
    private String horaErrorInicio;
    private String horaErrorFin;
    private String idProcedimiento;
    private String idErrorFK;   // id en tabla iden_err
    private String clave;   // oid - codigo de registro - codigo  de expediente
    private String errorNotificado;
    private String errorRevisado;
    private String errorRetramitado;
    private String fechaRevisionErrorInicio;
    private String fechaRevisionErrorFin;
    private String sistemaOrigen;
    private String ubicacionError;  // Clase y metodo donde se origina error
    private String ficheroLog; // fichero log donde se registra el erro
    private String evento;  // Evento acto notiticado
    private String numeroExpediente;  // Numero de expediente o de registro
    
    // Parametros para paginación
    private Integer numeroLineasxPagina;
    private Integer paginaActual;
    private Integer numeroInicialLinea;
    private Integer numeroFinalLinea;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFechaErrorInicio() {
        return fechaErrorInicio;
    }

    public void setFechaErrorInicio(String fechaErrorInicio) {
        this.fechaErrorInicio = fechaErrorInicio;
    }

    public String getFechaErrorFin() {
        return fechaErrorFin;
    }

    public void setFechaErrorFin(String fechaErrorFin) {
        this.fechaErrorFin = fechaErrorFin;
    }

    public String getHoraErrorInicio() {
        return horaErrorInicio;
    }

    public void setHoraErrorInicio(String horaErrorInicio) {
        this.horaErrorInicio = horaErrorInicio;
    }

    public String getHoraErrorFin() {
        return horaErrorFin;
    }

    public void setHoraErrorFin(String horaErrorFin) {
        this.horaErrorFin = horaErrorFin;
    }

    public String getIdProcedimiento() {
        return idProcedimiento;
    }

    public void setIdProcedimiento(String idProcedimiento) {
        this.idProcedimiento = idProcedimiento;
    }

    public String getIdErrorFK() {
        return idErrorFK;
    }

    public void setIdErrorFK(String idErrorFK) {
        this.idErrorFK = idErrorFK;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getErrorNotificado() {
        return errorNotificado;
    }

    public void setErrorNotificado(String errorNotificado) {
        this.errorNotificado = errorNotificado;
    }

    public String getErrorRevisado() {
        return errorRevisado;
    }

    public void setErrorRevisado(String errorRevisado) {
        this.errorRevisado = errorRevisado;
    }
    
     public String getErrorRetramitado() {
        return errorRetramitado;
    }

    public void setErrorRetramitado(String errorRetramitado) {
        this.errorRetramitado = errorRetramitado;
    }

    public String getFechaRevisionErrorInicio() {
        return fechaRevisionErrorInicio;
    }

    public void setFechaRevisionErrorInicio(String fechaRevisionErrorInicio) {
        this.fechaRevisionErrorInicio = fechaRevisionErrorInicio;
    }

    public String getFechaRevisionErrorFin() {
        return fechaRevisionErrorFin;
    }

    public void setFechaRevisionErrorFin(String fechaRevisionErrorFin) {
        this.fechaRevisionErrorFin = fechaRevisionErrorFin;
    }

    public String getSistemaOrigen() {
        return sistemaOrigen;
    }

    public void setSistemaOrigen(String sistemaOrigen) {
        this.sistemaOrigen = sistemaOrigen;
    }

    public String getUbicacionError() {
        return ubicacionError;
    }

    public void setUbicacionError(String ubicacionError) {
        this.ubicacionError = ubicacionError;
    }

    public String getFicheroLog() {
        return ficheroLog;
    }

    public void setFicheroLog(String ficheroLog) {
        this.ficheroLog = ficheroLog;
    }

    public String getEvento() {
        return evento;
    }

    public void setEvento(String evento) {
        this.evento = evento;
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
