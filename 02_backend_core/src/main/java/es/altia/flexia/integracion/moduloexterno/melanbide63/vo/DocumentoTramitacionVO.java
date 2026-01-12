package es.altia.flexia.integracion.moduloexterno.melanbide63.vo;

import java.util.Date;

public class DocumentoTramitacionVO {
    private int numeroDocumento;
    private String nombreDocumento;
    private byte[] contenido;
    private int codUsuarioCreacion;
    private int codUsuarioModif;
    private int codPlantillaOrigen;
    private int codPlantillaDestino;
    private String estadoFirma;
    //campos de la tabla E_CRD que no se a que se refieren
    private String crdExpFD;
    private String crdDocFD;
    private int crdFirFD;
    //Almacen del documento. 0:BBDD 1:DOKUSI
    private int almacen = -1;
    private String codGestorDokusi;
    private String preparadoNotificacion;
    
    /**
     * @return the nombreDocumento
     */
    public String getNombreDocumento() {
        return nombreDocumento;
    }

    /**
     * @param nombreDocumento the nombreDocumento to set
     */
    public void setNombreDocumento(String nombreDocumento) {
        this.nombreDocumento = nombreDocumento;
    }

    /**
     * @return the contenido
     */
    public byte[] getContenido() {
        return contenido;
    }

    /**
     * @param contenido the contenido to set
     */
    public void setContenido(byte[] contenido) {
        this.contenido = contenido;
    }

    /**
     * @return the numeroDocumento
     */
    public int getNumeroDocumento() {
        return numeroDocumento;
    }

    /**
     * @param numeroDocumento the numeroDocumento to set
     */
    public void setNumeroDocumento(int numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    /**
     * @return the codUsuarioCreacion
     */
    public int getCodUsuarioCreacion() {
        return codUsuarioCreacion;
    }

    /**
     * @param codUsuarioCreacion the codUsuarioCreacion to set
     */
    public void setCodUsuarioCreacion(int codUsuarioCreacion) {
        this.codUsuarioCreacion = codUsuarioCreacion;
    }

    /**
     * @return the codUsuarioModif
     */
    public int getCodUsuarioModif() {
        return codUsuarioModif;
    }

    /**
     * @param codUsuarioModif the codUsuarioModif to set
     */
    public void setCodUsuarioModif(int codUsuarioModif) {
        this.codUsuarioModif = codUsuarioModif;
    }

    /**
     * @return the estadoFirma
     */
    public String getEstadoFirma() {
        return estadoFirma;
    }

    /**
     * @param estadoFirma the estadoFirma to set
     */
    public void setEstadoFirma(String estadoFirma) {
        this.estadoFirma = estadoFirma;
    }

    /**
     * @return the crdExpFD
     */
    public String getCrdExpFD() {
        return crdExpFD;
    }

    /**
     * @param crdExpFD the crdExpFD to set
     */
    public void setCrdExpFD(String crdExpFD) {
        this.crdExpFD = crdExpFD;
    }

    /**
     * @return the crdDocFD
     */
    public String getCrdDocFD() {
        return crdDocFD;
    }

    /**
     * @param crdDocFD the crdDocFD to set
     */
    public void setCrdDocFD(String crdDocFD) {
        this.crdDocFD = crdDocFD;
    }

    /**
     * @return the crdFirFD
     */
    public int getCrdFirFD() {
        return crdFirFD;
    }

    /**
     * @param crdFirFD the crdFirFD to set
     */
    public void setCrdFirFD(int crdFirFD) {
        this.crdFirFD = crdFirFD;
    }

    /**
     * @return the almacen
     */
    public int getAlmacen() {
        return almacen;
    }

    /**
     * @param almacen the almacen to set
     */
    public void setAlmacen(int almacen) {
        this.almacen = almacen;
    }

    /**
     * @return the codGestorDokusi
     */
    public String getCodGestorDokusi() {
        return codGestorDokusi;
    }

    /**
     * @param codGestorDokusi the codGestorDokusi to set
     */
    public void setCodGestorDokusi(String codGestorDokusi) {
        this.codGestorDokusi = codGestorDokusi;
    }

    /**
     * @return the preparadoNotificacion
     */
    public String getPreparadoNotificacion() {
        return preparadoNotificacion;
    }

    /**
     * @param preparadoNotificacion the preparadoNotificacion to set
     */
    public void setPreparadoNotificacion(String preparadoNotificacion) {
        this.preparadoNotificacion = preparadoNotificacion;
    }

    /**
     * @return the codPlantillaOrigen
     */
    public int getCodPlantillaOrigen() {
        return codPlantillaOrigen;
    }

    /**
     * @param codPlantillaOrigen the codPlantillaOrigen to set
     */
    public void setCodPlantillaOrigen(int codPlantillaOrigen) {
        this.codPlantillaOrigen = codPlantillaOrigen;
    }

    /**
     * @return the codPlantillaDestino
     */
    public int getCodPlantillaDestino() {
        return codPlantillaDestino;
    }

    /**
     * @param codPlantillaDestino the codPlantillaDestino to set
     */
    public void setCodPlantillaDestino(int codPlantillaDestino) {
        this.codPlantillaDestino = codPlantillaDestino;
    }

    
}
