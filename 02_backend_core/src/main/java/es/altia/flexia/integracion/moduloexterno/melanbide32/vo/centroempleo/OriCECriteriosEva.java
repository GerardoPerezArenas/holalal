
package es.altia.flexia.integracion.moduloexterno.melanbide32.vo.centroempleo;

/**
 *
 * @author INGDGC
 */
public class OriCECriteriosEva {
    
    private long id; //number primary key
    private String codigo; // varchar2(100 byte) not null
    private String codigoOrden; // varchar2(100 byte) not null
    private String descripcion_ES;  //varchar2(1500 byte)  not null
    private String descripcion_EU; // varchar2(1500 byte)
    private int ejercicioConvocatoria; // EJERCICIOCONVOCATORIA	NUMBER

    public OriCECriteriosEva(long id, String codigo, String codigoOrden, String descripcion_ES, String descripcion_EU, int ejercicioConvocatoria) {
        this.id = id;
        this.codigo = codigo;
        this.codigoOrden = codigoOrden;
        this.descripcion_ES = descripcion_ES;
        this.descripcion_EU = descripcion_EU;
        this.ejercicioConvocatoria = ejercicioConvocatoria;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
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

    public int getEjercicioConvocatoria() {
        return ejercicioConvocatoria;
    }

    public void setEjercicioConvocatoria(int ejercicioConvocatoria) {
        this.ejercicioConvocatoria = ejercicioConvocatoria;
    }

    @Override
    public String toString() {
        return "OriCECriteriosEva{" +
                "id=" + id +
                ", codigo='" + codigo + '\'' +
                ", codigoOrden='" + codigoOrden + '\'' +
                ", descripcion_ES='" + descripcion_ES + '\'' +
                ", descripcion_EU='" + descripcion_EU + '\'' +
                ", ejercicioConvocatoria=" + ejercicioConvocatoria +
                '}';
    }
}
