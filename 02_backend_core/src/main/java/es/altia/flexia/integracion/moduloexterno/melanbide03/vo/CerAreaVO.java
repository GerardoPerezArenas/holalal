package es.altia.flexia.integracion.moduloexterno.melanbide03.vo;

/**
 * @author david.caamano
 * @version 16/08/2012 1.0
 * Historial de cambios:
 * <ol>
 *  <li>david.caamano * 16-08-2012 * #53275 Edición inicial</li>
 * </ol> 
 */
public class CerAreaVO {
    
    private String codArea;
    private String descAreaC;
    private String descAreaE;
    private String codFamilia;
    private CerFamiliaVO familia;

    public String getCodArea() {
        return codArea;
    }//getCodArea
    public void setCodArea(String codArea) {
        this.codArea = codArea;
    }//setCodArea

    public String getDescAreaC() {
        return descAreaC;
    }//getDescAreaC
    public void setDescAreaC(String descAreaC) {
        this.descAreaC = descAreaC;
    }//setDescAreaC

    public String getDescAreaE() {
        return descAreaE;
    }//getDescAreaE
    public void setDescAreaE(String descAreaE) {
        this.descAreaE = descAreaE;
    }//setDescAreaE
    
    public String getCodFamilia() {
        return codFamilia;
    }//getCodFamilia
    public void setCodFamilia(String codFamilia) {
        this.codFamilia = codFamilia;
    }//setCodFamilia

    public CerFamiliaVO getFamilia() {
        return familia;
    }//getFamilia
    public void setFamilia(CerFamiliaVO familia) {
        this.familia = familia;
    }//setFamilia
    
    public String toString(){
        StringBuilder sb = new StringBuilder();
            sb.append("CerAreaVO { ");
            sb.append("codArea = ");
            sb.append(codArea);
            sb.append(" ");
            sb.append("descAreaC = ");
            sb.append(descAreaC);
            sb.append(" ");
            sb.append("descAreaE = ");
            sb.append(descAreaE);
            if(familia != null){
                sb.append(" ");
                sb.append("familia = ");
                sb.append(familia.toString());
            }//if(familia != null)
            sb.append(" };");
        return sb.toString();
    }//toString
    
}//class
