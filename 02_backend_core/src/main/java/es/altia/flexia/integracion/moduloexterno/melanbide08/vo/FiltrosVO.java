package es.altia.flexia.integracion.moduloexterno.melanbide08.vo;

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
    private String estadoFirma;
    private String tipoDocumento;
    private String fechaEnvioPeticion;
    private String fechaEnvioPeticionDesde;
    private String fechaEnvioPeticionHasta;
    private String documentoNotificado;
    
        public FiltrosVO(){
        
    }

    public FiltrosVO(int start, int finish, int numEntries, int draw, String ejercicio, String procedimiento, String numeroExpediente , String estadoFirma, String tipoDocumento, String fechaEnvioPeticion,String documentoNotificado,String fechaEnvioPeticionDesde,String fechaEnvioPeticionHasta ) {
        this.start = start;
        this.finish = finish;
        this.numEntries = numEntries;
        this.draw = draw;
        this.ejercicio = ejercicio;
        this.procedimiento = procedimiento;
        this.numeroExpediente = numeroExpediente;
        this.estadoFirma = estadoFirma;
        this.tipoDocumento = tipoDocumento;
        this.fechaEnvioPeticion = fechaEnvioPeticion;
        this.documentoNotificado = documentoNotificado;
         this.fechaEnvioPeticionDesde = fechaEnvioPeticionDesde;
          this.fechaEnvioPeticionHasta = fechaEnvioPeticionHasta;
    }

    public String getDocumentoNotificado() {
        return documentoNotificado;
    }

    public void setDocumentoNotificado(String documentoNotificado) {
        this.documentoNotificado = documentoNotificado;
    }

    public String getFechaEnvioPeticionDesde() {
        return fechaEnvioPeticionDesde;
    }

    public void setFechaEnvioPeticionDesde(String fechaEnvioPeticionDesde) {
        this.fechaEnvioPeticionDesde = fechaEnvioPeticionDesde;
    }

    public String getFechaEnvioPeticionHasta() {
        return fechaEnvioPeticionHasta;
    }

    public void setFechaEnvioPeticionHasta(String fechaEnvioPeticionHasta) {
        this.fechaEnvioPeticionHasta = fechaEnvioPeticionHasta;
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

    public String getEstadoFirma() {
        return estadoFirma;
    }

    public void setEstadoFirma(String estadoFirma) {
        this.estadoFirma = estadoFirma;
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
