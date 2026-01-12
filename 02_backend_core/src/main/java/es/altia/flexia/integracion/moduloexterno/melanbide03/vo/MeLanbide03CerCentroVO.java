package es.altia.flexia.integracion.moduloexterno.melanbide03.vo;

/**
 * @author david.caamano
 * @version 16/08/2012 1.0
 * Historial de cambios:
 * <ol>
 *  <li>david.caamano * 16-08-2012 * #53275 Edición inicial</li>
 * </ol> 
 */
public class MeLanbide03CerCentroVO {
    
    //Propiedades
    private String codCertificado;
    private String codArea;
    private String codFamilia;

    //Métodos de acceso
    public String getCodCertificado() {
        return codCertificado;
    }//getCodCertificado
    public void setCodCertificado(String codCertificado) {
        this.codCertificado = codCertificado;
    }//setCodCertificado
    
    public String getCodArea() {
        return codArea;
    }//getCodArea
    public void setCodArea(String codArea) {
        this.codArea = codArea;
    }//setCodArea

    public String getCodFamilia() {
        return codFamilia;
    }//getCodFamilia
    public void setCodFamilia(String codFamilia) {
        this.codFamilia = codFamilia;
    }//setCodFamilia
    
    public String toString(){
        StringBuilder sb = new StringBuilder();
            sb.append("MeLanbide03CerCentroVO { ");
            sb.append("codCertificado = ");
            sb.append(codCertificado);
            sb.append(" ");
            sb.append("codArea = ");
            sb.append(codArea);
            sb.append(" ");
            sb.append("codFamilia = ");
            sb.append(codFamilia);
            sb.append(" ");
            sb.append(" };");
        return sb.toString();
    }//toString
    
}//class
