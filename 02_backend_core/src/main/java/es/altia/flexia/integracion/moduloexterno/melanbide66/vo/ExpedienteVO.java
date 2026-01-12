package es.altia.flexia.integracion.moduloexterno.melanbide66.vo;

import java.sql.Date;

/**
 *
 * @author altia
 */
public class ExpedienteVO {
    
    private TerceroVO tercero;
    private DatosEconomicosExpVO datosEconomicos;
    private Date fecPresentacion;
    private String cifCBSC;
    private Boolean esCBSC;

    public TerceroVO getTercero() {
        return tercero;
    }

    public void setTercero(TerceroVO tercero) {
        this.tercero = tercero;
    }

    public Date getFecPresentacion() {
        return fecPresentacion;
    }

    public void setFecPresentacion(Date fechaPresentacion) {
        this.fecPresentacion = fechaPresentacion;
    }

    public DatosEconomicosExpVO getDatosEconomicos() {
        return datosEconomicos;
    }

    public void setDatosEconomicos(DatosEconomicosExpVO datosEconomicos) {
        this.datosEconomicos = datosEconomicos;
    }

    public String getCifCBSC() {
        return cifCBSC;
    }

    public void setCifCBSC(String cifCBSC) {
        this.cifCBSC = cifCBSC;
    }

    public Boolean getEsCBSC() {
        return esCBSC;
    }

    public void setEsCBSC(Boolean esCBSC) {
        this.esCBSC = esCBSC;
    }
    
    
}
