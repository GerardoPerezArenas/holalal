package es.altia.flexia.integracion.moduloexterno.melanbide69.vo;

public class InfoCampoSuplementarioVO {
    private String codigo;
    private Object valor;
    private int tipoCampo;
    private InfoDesplegableVO desplegable;

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
    public Object getValor() {
        return valor;
    }

    /**
     * @param valor the valor to set
     */
    public void setValor(Object valor) {
        this.valor = valor;
    }

    /**
     * @return the desplegable
     */
    public InfoDesplegableVO getDesplegable() {
        return desplegable;
    }

    /**
     * @param desplegable the desplegable to set
     */
    public void setDesplegable(InfoDesplegableVO desplegable) {
        this.desplegable = desplegable;
    }

    /**
     * @return the tipoCampo
     */
    public int getTipoCampo() {
        return tipoCampo;
    }

    /**
     * @param tipoCampo the tipoCampo to set
     */
    public void setTipoCampo(int tipoCampo) {
        this.tipoCampo = tipoCampo;
    }
}
