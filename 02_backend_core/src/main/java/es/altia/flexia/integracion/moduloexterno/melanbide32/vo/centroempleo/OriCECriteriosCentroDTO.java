package es.altia.flexia.integracion.moduloexterno.melanbide32.vo.centroempleo;

/**
 *
 * @author INGDGC
 */
public class OriCECriteriosCentroDTO extends OriCECriteriosCentro{
    
    private String idElementoHTML; //	NUMBER

    public OriCECriteriosCentroDTO(long id, long idCentro, long idCriterio, long idCriterioOpcion, int centroSeleccionOpcion, String numeroExpediente, int centroSeleccionOpcionVAL, String idElementoHTML, int ejercicioConvocatoria) {
        super(id, idCentro, idCriterio, idCriterioOpcion, centroSeleccionOpcion, numeroExpediente, centroSeleccionOpcionVAL,ejercicioConvocatoria);
        this.idElementoHTML = idElementoHTML;
    }

    public OriCECriteriosCentroDTO(long idCentro, String numeroExpediente,int ejercicioConvocatoria) {
        super(idCentro, numeroExpediente,ejercicioConvocatoria);
    }
    
    public String getIdElementoHTML() {
        return idElementoHTML;
    }

    public void setIdElementoHTML(String idElementoHTML) {
        this.idElementoHTML = idElementoHTML;
    }

    @Override
    public String toString() {
        return "OriCECriteriosCentroDTO{" + 
                "idElementoHTML=" + idElementoHTML + 
                "OriCECriteriosCentro=" + super.toString() +
                '}';
    }
    
    
}
