package es.altia.flexia.integracion.moduloexterno.melanbide06.vo;

/**
 * @author jaime.hermoso
 * @version 15/12/2022 1.0
 * Historial de cambios:
 * <ol>
 *  <li>jaime.hermoso * 15/12/2022 * Edición inicial</li>
 * </ol> 
 */

public class GestionAvisosVO {

    private int id;
    private Integer organizacion;
    private Integer entidad;
    private Integer ejercicio;
    private String codProcedimiento;
    private String procedimiento;
    private String asunto;
    private String evento;
    private String codEvento;
    private String codAsuntoRegistro;
    private String asuntoRegistro;
    private String uor;
    private String codUor;
    private String contenido;
    private String email;
    private String configuracion_activa;
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getOrganizacion() {
        return organizacion;
    }

    public void setOrganizacion(Integer organizacion) {
        this.organizacion = organizacion;
    }

    public Integer getEntidad() {
        return entidad;
    }

    public void setEntidad(Integer entidad) {
        this.entidad = entidad;
    }

    public Integer getEjercicio() {
        return ejercicio;
    }

    public void setEjercicio(Integer ejercicio) {
        this.ejercicio = ejercicio;
    }

    public String getCodProcedimiento() {
        return codProcedimiento;
    }

    public void setCodProcedimiento(String codProcedimiento) {
        this.codProcedimiento = codProcedimiento;
    }

    public String getUor() {
        return uor;
    }

    public void setUor(String uor) {
        this.uor = uor;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getConfiguracion_activa() {
        return configuracion_activa;
    }

    public void setConfiguracion_activa(String configuracion_activa) {
        this.configuracion_activa = configuracion_activa;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getEvento() {
        return evento;
    }

    public void setEvento(String evento) {
        this.evento = evento;
    }

    public String getAsuntoRegistro() {
        return asuntoRegistro;
    }

    public void setAsuntoRegistro(String asuntoRegistro) {
        this.asuntoRegistro = asuntoRegistro;
    }

    public String getCodEvento() {
        return codEvento;
    }

    public void setCodEvento(String codEvento) {
        this.codEvento = codEvento;
    }

    public String getCodUor() {
        return codUor;
    }

    public void setCodUor(String codUor) {
        this.codUor = codUor;
    }

    public String getProcedimiento() {
        return procedimiento;
    }

    public void setProcedimiento(String procedimiento) {
        this.procedimiento = procedimiento;
    }

    public String getCodAsuntoRegistro() {
        return codAsuntoRegistro;
    }

    public void setCodAsuntoRegistro(String codAsuntoRegistro) {
        this.codAsuntoRegistro = codAsuntoRegistro;
    }
     
}