package es.altia.flexia.integracion.moduloexterno.melanbide09.vo;

/**
 *
 * @author alexandrep
 */
public class FiltrosVO {
    
    
    private int start;
    private int finish;
    private int numEntries;
    private int draw;
    
    private String ejercicio;
    private String procedimiento;
   private String numeroExpediente;
    private String tramite;
    private String tipoDocumento;
    private String fechaEnvioPeticion;
    private String fechaEnvioTramiteDesde;
    private String fechaEnvioTramiteHasta;
    private String documentoNotificado;
    private String estadoLog;
    
        public FiltrosVO(){
        
    }

    public FiltrosVO(int start, int finish, int numEntries, int draw, String ejercicio, String procedimiento, String numeroExpediente , String tramite, String tipoDocumento, String fechaEnvioPeticion,String documentoNotificado,String fechaEnvioPeticionDesde,String fechaEnvioPeticionHasta,String estadoLog ) {
        this.start = start;
        this.finish = finish;
        this.numEntries = numEntries;
        this.draw = draw;
        this.ejercicio = ejercicio;
        this.procedimiento = procedimiento;
        this.numeroExpediente = numeroExpediente;
        this.tramite = tramite;
        this.tipoDocumento = tipoDocumento;
        this.fechaEnvioPeticion = fechaEnvioPeticion;
        this.documentoNotificado = documentoNotificado;
        this.fechaEnvioTramiteDesde = fechaEnvioPeticionDesde;
        this.fechaEnvioTramiteHasta = fechaEnvioPeticionHasta;
         this.estadoLog = estadoLog;
    }

    public String getEstadoLog() {
        return estadoLog;
    }

    public void setEstadoLog(String estadoLog) {
        this.estadoLog = estadoLog;
    }

    
    
    
    public String getDocumentoNotificado() {
        return documentoNotificado;
    }

    public void setDocumentoNotificado(String documentoNotificado) {
        this.documentoNotificado = documentoNotificado;
    }

    public String getFechaEnvioTramiteDesde() {
        return fechaEnvioTramiteDesde;
    }

    public void setFechaEnvioTramiteDesde(String fechaEnvioTramiteDesde) {
        this.fechaEnvioTramiteDesde = fechaEnvioTramiteDesde;
    }

    public String getFechaEnvioTramiteHasta() {
        return fechaEnvioTramiteHasta;
    }

    public void setFechaEnvioTramiteHasta(String fechaEnvioTramiteHasta) {
        this.fechaEnvioTramiteHasta = fechaEnvioTramiteHasta;
    }
        
        

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getFinish() {
        return finish;
    }

    public void setFinish(int finish) {
        this.finish = finish;
    }

    public int getNumEntries() {
        return numEntries;
    }

    public void setNumEntries(int numEntries) {
        this.numEntries = numEntries;
    }

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public String getEjercicio() {
        return ejercicio;
    }

    public void setEjercicio(String ejercicio) {
        this.ejercicio = ejercicio;
    }

    public String getProcedimiento() {
        return procedimiento;
    }

    public void setProcedimiento(String procedimiento) {
        this.procedimiento = procedimiento;
    }

    public String getTramite() {
        return tramite;
    }

    public void setTramite(String tramite) {
        this.tramite = tramite;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getFechaEnvioPeticion() {
        return fechaEnvioPeticion;
    }

    public void setFechaEnvioPeticion(String fechaEnvioPeticion) {
        this.fechaEnvioPeticion = fechaEnvioPeticion;
    }

    public String getNumeroExpediente() {
        return numeroExpediente;
    }

    public void setNumeroExpediente(String numeroExpediente) {
        this.numeroExpediente = numeroExpediente;
    }
    
    
}
