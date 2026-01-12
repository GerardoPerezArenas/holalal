/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide48.vo;

/**
 *
 * @author santiagoc
 */
public class FilaBusquedaMunicipioVO 
{
    private Long codMunicipio;
    private Long codComarca;
    private String descMunicipio;
    private String descComarca;
    
    public FilaBusquedaMunicipioVO()
    {
        
    }

    public Long getCodComarca() {
        return codComarca;
    }

    public void setCodComarca(Long codComarca) {
        this.codComarca = codComarca;
    }

    public Long getCodMunicipio() {
        return codMunicipio;
    }

    public void setCodMunicipio(Long codMunicipio) {
        this.codMunicipio = codMunicipio;
    }

    public String getDescMunicipio() {
        return descMunicipio;
    }

    public void setDescMunicipio(String descMunicipio) {
        this.descMunicipio = descMunicipio;
    }

    public String getDescComarca() {
        return descComarca;
    }

    public void setDescComarca(String descComarca) {
        this.descComarca = descComarca;
    }
}
