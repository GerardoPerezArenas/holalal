package es.altia.flexia.integracion.moduloexterno.meikus.vo;

/**
 *
 * @author paz.rodriguez
 */
public class ConvocatoriaVO {
    
    private Long id;
    private int convocatoria;
    private int numPagos;
    private int justificacion;
    private int plurianualidades;
    private int numAnos;
    private String codProcedimiento;
    private Integer codOrganizacion;

    public Long getId() {
        return id;
    }//getId
    public void setId(Long id) {
        this.id = id;
    }//setId
    
    public int getConvocatoria() {
        return convocatoria;
    }//getConvocatoria
    public void setConvocatoria(int convocatoria) {
        this.convocatoria = convocatoria;
    }//setConvocatoria
    
    public int getNumPagos() {
        return numPagos;
    }//getNumPagos
    public void setNumPagos(int numPagos) {
        this.numPagos = numPagos;
    }//setNumPagos
    
    public int getJustificacion() {
        return justificacion;
    }//getJustificacion
    public void setJustificacion(int justificacion) {
        this.justificacion = justificacion;
    }//setJustificacion
    
    public int getPlurianualidades() {
        return plurianualidades;
    }//getPlurianualidades
    public void setPlurianualidades(int plurianualidades) {
        this.plurianualidades = plurianualidades;
    }//setPlurianualidades
    
    public int getNumAnos() {
        return numAnos;
    }//getNumAnos
    public void setNumAnos(int numAnos) {
        this.numAnos = numAnos;
    }//setNumAnos

    public String getCodProcedimiento() {
        return codProcedimiento;
    }//getCodProcedimiento
    public void setCodProcedimiento(String codProcedimiento) {
        this.codProcedimiento = codProcedimiento;
    }//setCodProcedimiento

    public Integer getCodOrganizacion() {
        return codOrganizacion;
    }//getCodOrganizacion
    public void setCodOrganizacion(Integer codOrganizacion) {
        this.codOrganizacion = codOrganizacion;
    }//setCodOrganizacion
    
}//class
