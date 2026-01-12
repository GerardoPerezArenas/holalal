
package es.altia.flexia.integracion.moduloexterno.melanbide32.vo.centroempleo;

/**
 *
 * @author INGDGC
 */
public class OriCECriteriosEvaOpcion {
    
    private long id; // number primary key
    private long idCriterioFK; // number not null
    private String codigoOrden; // varchar2(100 byte) not null
    private String descripcion_ES; // varchar2(1500 byte)  not null
    private String descripcion_EU; // varchar2(1500 byte)
    private Integer  codigoSubgrupo;  // CODIGOSUBGRUPO	NUMBER
    private int ejercicioConvocatoria; // EJERCICIOCONVOCATORIA	NUMBER

    public OriCECriteriosEvaOpcion(long id, long idCriterioFK, String codigoOrden, String descripcion_ES, String descripcion_EU, Integer codigoSubgrupo, int ejercicioConvocatoria) {
        this.id = id;
        this.idCriterioFK = idCriterioFK;
        this.codigoOrden = codigoOrden;
        this.descripcion_ES = descripcion_ES;
        this.descripcion_EU = descripcion_EU;
        this.codigoSubgrupo = codigoSubgrupo;
        this.ejercicioConvocatoria = ejercicioConvocatoria;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdCriterioFK() {
        return idCriterioFK;
    }

    public void setIdCriterioFK(long idCriterioFK) {
        this.idCriterioFK = idCriterioFK;
    }

    public String getCodigoOrden() {
        return codigoOrden;
    }

    public void setCodigoOrden(String codigoOrden) {
        this.codigoOrden = codigoOrden;
    }

    public String getDescripcion_ES() {
        return descripcion_ES;
    }

    public void setDescripcion_ES(String descripcion_ES) {
        this.descripcion_ES = descripcion_ES;
    }

    public String getDescripcion_EU() {
        return descripcion_EU;
    }

    public void setDescripcion_EU(String descripcion_EU) {
        this.descripcion_EU = descripcion_EU;
    }

    @Override
    public String toString() {
        return "OriCECriteriosEvaOpcion{" + "id=" + id + ", idCriterioFK=" + idCriterioFK + ", codigoOrden=" + codigoOrden + ", descripcion_ES=" + descripcion_ES + ", descripcion_EU=" + descripcion_EU + '}';
    }
        
}
