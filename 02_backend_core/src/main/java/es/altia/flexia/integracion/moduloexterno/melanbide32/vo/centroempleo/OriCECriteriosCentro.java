package es.altia.flexia.integracion.moduloexterno.melanbide32.vo.centroempleo;

/**
 *
 * @author INGDGC
 */
public class OriCECriteriosCentro {
    
    private long id; //	NUMBER
    private long idCentro; //	NUMBER
    private long idCriterio; // NUMBER
    private long idCriterioOpcion; // NUMBER
    private int centroSeleccionOpcion; // NUMBER(1,0)
    private String numeroExpediente; // VARCHAR2(30 BYTE)
    private int centroSeleccionOpcionVAL; //	NUMBER(1,0)
    private int ejercicioConvocatoria; //	NUMBER

    public OriCECriteriosCentro(long id, long idCentro, long idCriterio, long idCriterioOpcion, int centroSeleccionOpcion, String numeroExpediente, int centroSeleccionOpcionVAL, int ejercicioConvocatoria) {
        this.id = id;
        this.idCentro = idCentro;
        this.idCriterio = idCriterio;
        this.idCriterioOpcion = idCriterioOpcion;
        this.centroSeleccionOpcion = centroSeleccionOpcion;
        this.numeroExpediente = numeroExpediente;
        this.centroSeleccionOpcionVAL = centroSeleccionOpcionVAL;
        this.ejercicioConvocatoria = ejercicioConvocatoria;
    }

    public OriCECriteriosCentro(long idCentro, String numeroExpediente, int ejercicioConvocatoria) {
        this.idCentro = idCentro;
        this.numeroExpediente = numeroExpediente;
        this.ejercicioConvocatoria = ejercicioConvocatoria;
    }
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdCentro() {
        return idCentro;
    }

    public void setIdCentro(long idCentro) {
        this.idCentro = idCentro;
    }

    public long getIdCriterio() {
        return idCriterio;
    }

    public void setIdCriterio(long idCriterio) {
        this.idCriterio = idCriterio;
    }

    public long getIdCriterioOpcion() {
        return idCriterioOpcion;
    }

    public void setIdCriterioOpcion(long idCriterioOpcion) {
        this.idCriterioOpcion = idCriterioOpcion;
    }

    public int getCentroSeleccionOpcion() {
        return centroSeleccionOpcion;
    }

    public void setCentroSeleccionOpcion(int centroSeleccionOpcion) {
        this.centroSeleccionOpcion = centroSeleccionOpcion;
    }

    public String getNumeroExpediente() {
        return numeroExpediente;
    }

    public void setNumeroExpediente(String numeroExpediente) {
        this.numeroExpediente = numeroExpediente;
    }

    public int getCentroSeleccionOpcionVAL() {
        return centroSeleccionOpcionVAL;
    }

    public void setCentroSeleccionOpcionVAL(int centroSeleccionOpcionVAL) {
        this.centroSeleccionOpcionVAL = centroSeleccionOpcionVAL;
    }

    public int getEjercicioConvocatoria() {
        return ejercicioConvocatoria;
    }

    public void setEjercicioConvocatoria(int ejercicioConvocatoria) {
        this.ejercicioConvocatoria = ejercicioConvocatoria;
    }

    @Override
    public String toString() {
        return "OriCECriteriosCentro{" +
                "id=" + id +
                ", idCentro=" + idCentro +
                ", idCriterio=" + idCriterio +
                ", idCriterioOpcion=" + idCriterioOpcion +
                ", centroSeleccionOpcion=" + centroSeleccionOpcion +
                ", numeroExpediente='" + numeroExpediente + '\'' +
                ", centroSeleccionOpcionVAL=" + centroSeleccionOpcionVAL +
                ", ejercicioConvocatoria=" + ejercicioConvocatoria +
                '}';
    }
}
