package es.altia.flexia.integracion.moduloexterno.melanbide92.vo;

/**
 *
 * @author kepa.gonzalez
 */
public class ProcedimientoLireiVO {

    private String codProcedimiento;
    private Integer requerimiento;
    private Integer resolucion;
    private Integer acuseRes;
    private Integer pago;
    private Integer ejecutiva;
    private Integer fracciona;
    private Integer anulacion;
    private Integer suspension;
    private Integer finRama;
    private Integer cierreEspera;

    public String getCodProcedimiento() {
        return codProcedimiento;
    }

    public void setCodProcedimiento(String codProcedimiento) {
        this.codProcedimiento = codProcedimiento;
    }

    public Integer getRequerimiento() {
        return requerimiento;
    }

    public void setRequerimiento(Integer requerimiento) {
        this.requerimiento = requerimiento;
    }

    public Integer getResolucion() {
        return resolucion;
    }

    public void setResolucion(Integer resolucion) {
        this.resolucion = resolucion;
    }

    public Integer getAcuseRes() {
        return acuseRes;
    }

    public void setAcuseRes(Integer acuseRes) {
        this.acuseRes = acuseRes;
    }

    public Integer getPago() {
        return pago;
    }

    public void setPago(Integer pago) {
        this.pago = pago;
    }

    public Integer getEjecutiva() {
        return ejecutiva;
    }

    public void setEjecutiva(Integer ejecutiva) {
        this.ejecutiva = ejecutiva;
    }

    public Integer getFracciona() {
        return fracciona;
    }

    public void setFracciona(Integer fracciona) {
        this.fracciona = fracciona;
    }

    public Integer getAnulacion() {
        return anulacion;
    }

    public void setAnulacion(Integer anulacion) {
        this.anulacion = anulacion;
    }

    public Integer getSuspension() {
        return suspension;
    }

    public void setSuspension(Integer suspension) {
        this.suspension = suspension;
    }

    public Integer getFinRama() {
        return finRama;
    }

    public void setFinRama(Integer finRama) {
        this.finRama = finRama;
    }

    public Integer getCierreEspera() {
        return cierreEspera;
    }

    public void setCierreEspera(Integer cierreEspera) {
        this.cierreEspera = cierreEspera;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ProcedimientoLireiVO{");
        sb.append("codProcedimiento=").append(codProcedimiento);
        sb.append(", requerimiento=").append(requerimiento);
        sb.append(", resolucion=").append(resolucion);
        sb.append(", acuseRes=").append(acuseRes);
        sb.append(", pago=").append(pago);
        sb.append(", ejecutiva=").append(ejecutiva);
        sb.append(", fracciona=").append(fracciona);
        sb.append(", anulacion=").append(anulacion);
        sb.append(", suspension=").append(suspension);
        sb.append(", finRama=").append(finRama);
        sb.append(", cierreEspera=").append(cierreEspera);
        sb.append('}');
        return sb.toString();
    }
    
    
}
