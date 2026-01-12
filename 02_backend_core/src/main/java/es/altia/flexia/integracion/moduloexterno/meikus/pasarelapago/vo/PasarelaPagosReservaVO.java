package es.altia.flexia.integracion.moduloexterno.meikus.pasarelapago.vo;

/**
 * @author david.caamano
 * @version 03/12/2012 1.0
 * Historial de cambios:
 * <ol>
 *  <li>david.caamano * 03/12/2012 * Edición inicial</li>
 * </ol> 
 */
public class PasarelaPagosReservaVO {
    
    private String ejercicio;
    private String idReserva;
    private String importeAnios;
    private String idLineaAyuda;
    private String numExpediente;

    public String getEjercicio() {
        return ejercicio;
    }//getEjercicio
    public void setEjercicio(String ejercicio) {
        this.ejercicio = ejercicio;
    }//setEjercicio

    public String getIdLineaAyuda() {
        return idLineaAyuda;
    }//getIdLineaAyuda
    public void setIdLineaAyuda(String idLineaAyuda) {
        this.idLineaAyuda = idLineaAyuda;
    }//setIdLineaAyuda

    public String getIdReserva() {
        return idReserva;
    }//getIdReserva
    public void setIdReserva(String idReserva) {
        this.idReserva = idReserva;
    }//setIdReserva

    public String getImporteAnios() {
        return importeAnios;
    }//getImporteAnios
    public void setImporteAnios(String importeAnios) {
        this.importeAnios = importeAnios;
    }//setImporteAnios

    public String getNumExpediente() {
        return numExpediente;
    }//getNumExpediente
    public void setNumExpediente(String numExpediente) {
        this.numExpediente = numExpediente;
    }//setNumExpediente
    
}