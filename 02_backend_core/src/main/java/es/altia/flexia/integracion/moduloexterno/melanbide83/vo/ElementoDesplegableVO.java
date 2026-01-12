package es.altia.flexia.integracion.moduloexterno.melanbide83.vo;

public class ElementoDesplegableVO {
    private String codigo;
    private String valor;

    public ElementoDesplegableVO() {
    }

    public ElementoDesplegableVO(String codigo, String valor) {
        this.codigo = codigo;
        this.valor = valor;
    }

    /**
     * @return the codigo
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * @param codigo the codigo to set
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    /**
     * @return the valor
     */
    public String getValor() {
        return valor;
    }

    /**
     * @param valor the valor to set
     */
    public void setValor(String valor) {
        this.valor = valor;
    }
}
