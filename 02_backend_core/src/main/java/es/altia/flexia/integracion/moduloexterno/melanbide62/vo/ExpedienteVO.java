package es.altia.flexia.integracion.moduloexterno.melanbide62.vo;

import java.sql.Date;

/**
 *
 * @author altia
 */
public class ExpedienteVO {
    
    private TerceroVO tercero;
    private Date fecPresentacion;

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
    
    
}
