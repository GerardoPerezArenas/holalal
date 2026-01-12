package es.altia.flexia.integracion.moduloexterno.melanbide53.vo;

/**
 * Representa a una ubicación de un centro
 * @author davidg
 */
public class RegistroErrorVO_EXCEL2 {
    

    private String mensajeError;
    private String idErrorFK;   // id en tabla iden_err
    private String num;

    /**
     * @return the mensajeError
     */
    public String getMensajeError() {
        return mensajeError;
    }

    /**
     * @param mensajeError the mensajeError to set
     */
    public void setMensajeError(String mensajeError) {
        this.mensajeError = mensajeError;
    }

    /**
     * @return the idErrorFK
     */
    public String getIdErrorFK() {
        return idErrorFK;
    }

    /**
     * @param idErrorFK the idErrorFK to set
     */
    public void setIdErrorFK(String idErrorFK) {
        this.idErrorFK = idErrorFK;
    }

    /**
     * @return the num
     */
    public String getNum() {
        return num;
    }

    /**
     * @param num the num to set
     */
    public void setNum(String num) {
        this.num = num;
    }

   
}
