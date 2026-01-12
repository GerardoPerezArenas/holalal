package es.altia.flexia.integracion.moduloexterno.meikus.vo;

/**
 * @author david.caamano
 * @version 10/01/2012 1.0 Historial de cambios:
 * <ol>
 * <li>david.caamano * 10/01/2012 * Edición inicial</li>
 * </ol>
 */
public class TerceroPasarela {

    private int codTercero;
    private String documento;
    private String tipoTercero;
    private String version;
    private String codTerritorioHistorico;
    private String formaJuridica;
    private String nombre;
    private String idTerceroIkus;

    public int getCodTercero() {
        return codTercero;
    }

    public void setCodTercero(int codTercero) {
        this.codTercero = codTercero;
    }
    
    public String getDocumento() {
        return documento;
    }//getCodigo

    public void setDocumento(String documento) {
        this.documento = documento;
    }//setCodigo

    public String getTipoTercero() {
        return tipoTercero;
    }//getTipoTercero

    public void setTipoTercero(String tipoTercero) {
        this.tipoTercero = tipoTercero;
    }//setTipoTercero

    public String getVersion() {
        return version;
    }//getVersion

    public void setVersion(String version) {
        this.version = version;
    }//setVersion

    public String getCodTerritorioHistorico() {
        return codTerritorioHistorico;
    }//getCodTerritorioHistorico

    public void setCodTerritorioHistorico(String codTerritorioHistorico) {
        this.codTerritorioHistorico = codTerritorioHistorico;
    }//setCodTerritorioHistorico

    public String getFormaJuridica() {
        return this.formaJuridica;
    }

    public void setFormaJuridica(String formaJuridica) {
        this.formaJuridica = formaJuridica;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIdTerceroIkus() {
        return idTerceroIkus;
    }

    public void setIdTerceroIkus(String idTerceroIkus) {
        this.idTerceroIkus = idTerceroIkus;
    }

}//class
