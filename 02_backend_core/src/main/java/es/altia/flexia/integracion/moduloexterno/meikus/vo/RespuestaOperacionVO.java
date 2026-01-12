package es.altia.flexia.integracion.moduloexterno.meikus.vo;

/**
 * @author david.caamano
 * @version 07/12/2012 1.0
 * Historial de cambios:
 * <ol>
 *  <li>david.caamano * 07/12/2012 * Edición inicial</li>
 * </ol> 
 */
public class RespuestaOperacionVO {
    
    private String IdExpediente;
    private Integer codOrganizacion;
    private String codProcedimiento;
    private String ejercicio;
    private OperacionesPasarelaEnum operacion;
    private String infoAdicional;

    public String getIdExpediente() {
        return IdExpediente;
    }//getIdExpediente
    public void setIdExpediente(String IdExpediente) {
        this.IdExpediente = IdExpediente;
    }//setIdExpediente

    public Integer getCodOrganizacion() {
        return codOrganizacion;
    }//getCodOrganizacion
    public void setCodOrganizacion(Integer codOrganizacion) {
        this.codOrganizacion = codOrganizacion;
    }//setCodOrganizacion

    public String getCodProcedimiento() {
        return codProcedimiento;
    }//getCodProcedimiento
    public void setCodProcedimiento(String codProcedimiento) {
        this.codProcedimiento = codProcedimiento;
    }//setCodProcedimiento

    public String getEjercicio() {
        return ejercicio;
    }//getEjercicio
    public void setEjercicio(String ejercicio) {
        this.ejercicio = ejercicio;
    }//setEjercicio
    
    public OperacionesPasarelaEnum getOperacion() {
        return operacion;
    }//getOperacion
    public void setOperacion(OperacionesPasarelaEnum operacion) {
        this.operacion = operacion;
    }//setOperacion

    public String getInfoAdicional() {
        return infoAdicional;
    }//getInfoAdicional
    public void setInfoAdicional(String infoAdicional) {
        this.infoAdicional = infoAdicional;
    }//setInfoAdicional

}//class
