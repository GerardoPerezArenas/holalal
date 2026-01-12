package es.altia.flexia.integracion.moduloexterno.melanbide69.vo;

import java.util.ArrayList;
import java.util.HashMap;

public class InfoDesplegableVO {
    private String codDesplegable;
    private String descDesplegable;
    private ArrayList<String> valores;
    private ArrayList<String> codigos;
    private ArrayList<ElementoDesplegableVO> paresCodVal;

    
    /**
     * @return the codDesplegable
     */
    public String getCodDesplegable() {
        return codDesplegable;
    }

    /**
     * @param codDesplegable the codDesplegable to set
     */
    public void setCodDesplegable(String codDesplegable) {
        this.codDesplegable = codDesplegable;
    }

    /**
     * @return the descDesplegable
     */
    public String getDescDesplegable() {
        return descDesplegable;
    }

    /**
     * @param descDesplegable the descDesplegable to set
     */
    public void setDescDesplegable(String descDesplegable) {
        this.descDesplegable = descDesplegable;
    }

    /**
     * @return the valores
     */
    public ArrayList<String> getValores() {
        return valores;
    }

    /**
     * @param valores the valores to set
     */
    public void setValores(ArrayList<String> valores) {
        this.valores = valores;
    }

    /**
     * @return the codigos
     */
    public ArrayList<String> getCodigos() {
        return codigos;
    }

    /**
     * @param codigos the codigos to set
     */
    public void setCodigos(ArrayList<String> codigos) {
        this.codigos = codigos;
    }
    
    public void addValor(String valor){
        if(valores == null) valores = new ArrayList<String>();
        valores.add(valor);
    }
    
    public void addCodigo(String cod){
        if(codigos == null) codigos = new ArrayList<String>();
        codigos.add(cod);
    }

    /**
     * @return the codigoValor
     */
    public ArrayList<ElementoDesplegableVO> getParesCodVal() {
        return paresCodVal;
    }

    /**
     * @param codigoValor the codigoValor to set
     */
    public void setParesCodVal(ArrayList<ElementoDesplegableVO> paresCodVal) {
        this.paresCodVal = paresCodVal;
    }
    
    public void addCodVal(String cod, String val){
        ElementoDesplegableVO elem = new ElementoDesplegableVO(cod, val);
        if(paresCodVal == null) paresCodVal = new ArrayList<ElementoDesplegableVO>();
        paresCodVal.add(elem);
    }
    
    /**
     * Devuelve el valor del primer objeto ElementoDesplegableVO cuyo c�digo coincida con el buscado
     * @param codigo C�digo buscado
     * @return Valor para ese c�digo
     */
    public String getValor(String codigo){
        String valor = null;
        for(ElementoDesplegableVO elem : paresCodVal){
            if(elem.getCodigo().equals(codigo)){
                valor = elem.getValor();
                break;
            }
        }
        return valor;
    }
}
