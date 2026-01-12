package es.altia.flexia.integracion.moduloexterno.melanbide03.vo;

import java.io.Serializable;

/**
 * @author david.caamano
 * @version 16/08/2012 1.0
 * Historial de cambios:
 * <ol>
 *  <li>david.caamano * 16-08-2012 * #53275 Edición inicial</li>
 * </ol> 
 */
public class CerFamiliaVO implements Serializable {
    
    //Propiedades
    private String codFamilia;
    private String descFamiliaC;
    private String descFamiliaE;
    private String imagen;
    
    //Métodos de acceso
    public String getCodFamilia() {
        return codFamilia;
    }//getCodFamilia
    public void setCodFamilia(String codFamilia) {
        this.codFamilia = codFamilia;
    }//setCodFamilia

    public String getDescFamiliaC() {
        return descFamiliaC;
    }//getDescFamiliaC
    public void setDescFamiliaC(String descFamiliaC) {
        this.descFamiliaC = descFamiliaC;
    }//setDescFamiliaC

    public String getDescFamiliaE() {
        return descFamiliaE;
    }//getDescFamiliaE
    public void setDescFamiliaE(String descFamiliaE) {
        this.descFamiliaE = descFamiliaE;
    }//setDescFamiliaE

    public String getImagen() {
        return imagen;
    }//getImagen
    public void setImagen(String imagen) {
        this.imagen = imagen;
    }//setImagen
    
    public String toString(){
        StringBuilder sb = new StringBuilder();
            sb.append("CerFamiliaVO{ ");
            sb.append("codFamilia = " );
            sb.append(codFamilia);
            sb.append(" ");
            sb.append("descFamiliaC = ");
            sb.append(descFamiliaC);
            sb.append(" ");
            sb.append("descFamiliaE = ");
            sb.append(descFamiliaE);
            sb.append(" ");
            sb.append("imagen = ");
            sb.append(imagen);
            sb.append(" };");
        return sb.toString();
    }//toString
}//class
