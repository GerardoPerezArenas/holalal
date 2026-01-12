package es.altia.flexia.integracion.moduloexterno.melanbide48.vo;

public class ColecCompIgualdad {

    String codigo;
    String descripcion;
    String descripcioneu;

    public ColecCompIgualdad(String codigo, String descripcion, String descripcioneu) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.descripcioneu = descripcioneu;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcioneu() {
        return descripcioneu;
    }

    public void setDescripcioneu(String descripcioneu) {
        this.descripcioneu = descripcioneu;
    }

    @Override
    public String toString() {
        return "ColecCompIgualdad{" +
                "codigo='" + codigo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", descripcioneu='" + descripcioneu + '\'' +
                '}';
    }
}
