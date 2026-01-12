package es.altia.flexia.integracion.moduloexterno.melanbide47.vo.orientacion;

public class OriCompIgualdadPuntuacion {

    int idConvocatoria;
    String codigo;
    double puntuacion;

    public OriCompIgualdadPuntuacion(int idConvocatoria, String codigo, double puntuacion) {
        this.idConvocatoria = idConvocatoria;
        this.codigo = codigo;
        this.puntuacion = puntuacion;
    }

    public int getIdConvocatoria() {
        return idConvocatoria;
    }

    public void setIdConvocatoria(int idConvocatoria) {
        this.idConvocatoria = idConvocatoria;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public double getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(double puntuacion) {
        this.puntuacion = puntuacion;
    }

    @Override
    public String toString() {
        return "OriCompIgualdadPuntuacion{" +
                "idConvocatoria=" + idConvocatoria +
                ", codigo='" + codigo + '\'' +
                ", puntuacion=" + puntuacion +
                '}';
    }
}
