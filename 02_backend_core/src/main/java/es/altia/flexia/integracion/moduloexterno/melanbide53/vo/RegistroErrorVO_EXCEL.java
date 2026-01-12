package es.altia.flexia.integracion.moduloexterno.melanbide53.vo;

/**
 * Representa a una ubicación de un centro
 * @author davidg
 */
public class RegistroErrorVO_EXCEL {
    
    private Integer id;
    private String fechaError;
    private String mensajeError;
    private String mensajeException;
    private String causaException;
    private String trazaError;
    private String idProcedimiento;
    private String idErrorFK;   // id en tabla iden_err
    private String clave;   // oid - codigo de registro - codigo  de expediente
    private String errorNotificado;
    private String errorRevisado;
    private String fechaRevisionError;
    private String observacionesError;
    private String sistemaOrigen;
    private String ubicacionError;  // Clase y metodo donde se origina error
    private String ficheroLog; // fichero log donde se registra el erro
    private String evento;  // Evento acto notiticado
    private String numeroExpediente;  // Numero de expediente o de registro
    
    // Numero de registros obtenidos en las consultas -- se pone para recuperar datos de la paginacio
    private Integer noTotalRegConsulta;
    private Integer noRegEnLaConsulta;
    
    private String xmlReglasSolicitud;
    private String solicitudRetramitada;
    private String fechaRetramitacion;
    private String resultadoRetramitacion;
    private String impacto;
    private String tratado;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFechaError() {
        return fechaError;
    }

    public void setFechaError(String fechaError) {
        this.fechaError = fechaError;
    }

    public String getMensajeError() {
        return mensajeError;
    }

    public void setMensajeError(String mensajeError) {
        this.mensajeError = mensajeError;
    }

    public String getMensajeException() {
        return mensajeException;
    }

    public void setMensajeException(String mensajeException) {
        this.mensajeException = mensajeException;
    }

    public String getCausaException() {
        return causaException;
    }

    public void setCausaException(String causaException) {
        this.causaException = causaException;
    }

    public String getTrazaError() {
        return trazaError;
    }

    public void setTrazaError(String trazaError) {
        this.trazaError = trazaError;
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

    public String getFechaRevisionError() {
        return fechaRevisionError;
    }

    public void setFechaRevisionError(String fechaRevisionError) {
        this.fechaRevisionError = fechaRevisionError;
    }

    public String getObservacionesError() {
        return observacionesError;
    }

    public void setObservacionesError(String observacionesError) {
        this.observacionesError = observacionesError;
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
    
    public Integer getNoTotalRegConsulta() {
        return noTotalRegConsulta;
    }

    public void setNoTotalRegConsulta(Integer noTotalRegConsulta) {
        this.noTotalRegConsulta = noTotalRegConsulta;
    }

    public Integer getNoRegEnLaConsulta() {
        return noRegEnLaConsulta;
    }

    public void setNoRegEnLaConsulta(Integer noRegEnLaConsulta) {
        this.noRegEnLaConsulta = noRegEnLaConsulta;
    }

    public String getXmlReglasSolicitud() {
        return xmlReglasSolicitud;
    }

    public void setXmlReglasSolicitud(String xmlReglasSolicitud) {
        this.xmlReglasSolicitud = xmlReglasSolicitud;
    }

    public String getSolicitudRetramitada() {
        return solicitudRetramitada;
    }

    public void setSolicitudRetramitada(String solicitudRetramitada) {
        this.solicitudRetramitada = solicitudRetramitada;
    }

    public String getFechaRetramitacion() {
        return fechaRetramitacion;
    }

    public void setFechaRetramitacion(String fechaRetramitacion) {
        this.fechaRetramitacion = fechaRetramitacion;
    }

    public String getResultadoRetramitacion() {
        return resultadoRetramitacion;
    }

    public void setResultadoRetramitacion(String resultadoRetramitacion) {
        this.resultadoRetramitacion = resultadoRetramitacion;
    }

    public String getImpacto() {
        return impacto;
    }

    public void setImpacto(String impacto) {
        this.impacto = impacto;
    }

    public String getTratado() {
        return tratado;
    }

    public void setTratado(String tratado) {
        this.tratado = tratado;
    }
    
}
