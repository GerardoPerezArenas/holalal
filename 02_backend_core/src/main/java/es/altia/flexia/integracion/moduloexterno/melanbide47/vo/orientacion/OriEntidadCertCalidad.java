package es.altia.flexia.integracion.moduloexterno.melanbide47.vo.orientacion;

public class OriEntidadCertCalidad {

    Integer id;
    String numExp;
    Integer idEntidad;
    String idCertificado;
    Integer valorSNSolicitud;
    Integer valorSNValidado;

    public OriEntidadCertCalidad(Integer id, String numExp, Integer idEntidad, String idCertificado, Integer valorSNSolicitud, Integer valorSNValidado) {
        this.id = id;
        this.numExp = numExp;
        this.idEntidad = idEntidad;
        this.idCertificado = idCertificado;
        this.valorSNSolicitud = valorSNSolicitud;
        this.valorSNValidado = valorSNValidado;
    }

    public OriEntidadCertCalidad() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumExp() {
        return numExp;
    }

    public void setNumExp(String numExp) {
        this.numExp = numExp;
    }

    public Integer getIdEntidad() {
        return idEntidad;
    }

    public void setIdEntidad(Integer idEntidad) {
        this.idEntidad = idEntidad;
    }

    public String getIdCertificado() {
        return idCertificado;
    }

    public void setIdCertificado(String idCertificado) {
        this.idCertificado = idCertificado;
    }

    public Integer getValorSNSolicitud() {
        return valorSNSolicitud;
    }

    public void setValorSNSolicitud(Integer valorSNSolicitud) {
        this.valorSNSolicitud = valorSNSolicitud;
    }

    public Integer getValorSNValidado() {
        return valorSNValidado;
    }

    public void setValorSNValidado(Integer valorSNValidado) {
        this.valorSNValidado = valorSNValidado;
    }

    @Override
    public String toString() {
        return "OriEntidadCertCalidad{" +
                "id=" + id +
                ", numExp='" + numExp + '\'' +
                ", idEntidad=" + idEntidad +
                ", idCertificado='" + idCertificado + '\'' +
                ", valorSNSolicitud=" + valorSNSolicitud +
                ", valorSNValidado=" + valorSNValidado +
                '}';
    }
}
