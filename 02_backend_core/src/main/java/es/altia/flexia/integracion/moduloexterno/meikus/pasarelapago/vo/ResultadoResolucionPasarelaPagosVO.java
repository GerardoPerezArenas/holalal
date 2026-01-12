package es.altia.flexia.integracion.moduloexterno.meikus.pasarelapago.vo;


/**
 * @author david.caamano
 * @version 03/12/2012 1.0
 * Historial de cambios:
 * <ol>
 *  <li>david.caamano * 03/12/2012 * Edición inicial</li>
 * </ol> 
 */
public class ResultadoResolucionPasarelaPagosVO {
    
    private String codigoError;
    private String codMensajeError;

    public String getCodMensajeError() {
        return codMensajeError;
    }//getCodMensajeError
    public void setCodMensajeError(String codMensajeError) {
        this.codMensajeError = codMensajeError;
    }//setCodMensajeError

    public String getCodigoError() {
        return codigoError;
    }//getCodigoError
    public void setCodigoError(String codigoError) {
        this.codigoError = codigoError;
    }//setCodigoError
    
}//class
