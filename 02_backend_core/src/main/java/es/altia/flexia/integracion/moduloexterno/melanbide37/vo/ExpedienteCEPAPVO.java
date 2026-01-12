package es.altia.flexia.integracion.moduloexterno.melanbide37.vo;

public class ExpedienteCEPAPVO {
    
    private String numeroExpediente;
    private String docInteresado;
    private String nombreInteresado;
    private String CP;
    private String lugarEnvio;
    private String lote;
    private String tituloEntregado;
    private String telefono;
    
    // Numero de registros obtenidos en las consultas -- se pone para recuperar datos de la paginacio
    private Integer noTotalRegConsulta;
    private Integer noRegEnLaConsulta;

    public String getNumeroExpediente() {
        return numeroExpediente;
    }

    public void setNumeroExpediente(String numeroExpediente) {
        this.numeroExpediente = numeroExpediente;
    }

    public String getDocInteresado() {
        return docInteresado;
    }

    public void setDocInteresado(String docInteresado) {
        this.docInteresado = docInteresado;
    }

    public String getNombreInteresado() {
        return nombreInteresado;
    }

    public void setNombreInteresado(String nombreInteresado) {
        this.nombreInteresado = nombreInteresado;
    }

    public String getCP() {
        return CP;
    }

    public void setCP(String CP) {
        this.CP = CP;
    }

    public String getLugarEnvio() {
        return lugarEnvio;
    }

    public void setLugarEnvio(String lugarEnvio) {
        this.lugarEnvio = lugarEnvio;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }
    
    public String getTituloEntregado() {
        return tituloEntregado;
    }

    public void setTituloEntregado(String tituloEntregado) {
        this.tituloEntregado = tituloEntregado;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
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
    
    
}
