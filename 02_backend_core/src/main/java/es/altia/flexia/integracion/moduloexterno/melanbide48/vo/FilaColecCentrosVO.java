/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide48.vo;

/**
 *
 * @author santiagoc
 */
public class FilaColecCentrosVO 
{
    private Long codCentro;
    private Integer ambito;
    private String descAmbito;
    private Integer colectivo;
    private Long comarca;
    private String descComarca;
    private Long municipio;
    private String descMunicipio;
    private String direccion;
    
    public FilaColecCentrosVO()
    {
        
    }

    public Long getCodCentro() {
        return codCentro;
    }

    public void setCodCentro(Long codCentro) {
        this.codCentro = codCentro;
    }

    public Integer getAmbito() {
        return ambito;
    }

    public void setAmbito(Integer ambito) {
        this.ambito = ambito;
    }

    public String getDescAmbito() {
        return descAmbito;
    }

    public void setDescAmbito(String descAmbito) {
        this.descAmbito = descAmbito;
    }

    public Integer getColectivo() {
        return colectivo;
    }

    public void setColectivo(Integer colectivo) {
        this.colectivo = colectivo;
    }

    public Long getComarca() {
        return comarca;
    }

    public void setComarca(Long comarca) {
        this.comarca = comarca;
    }

    public String getDescComarca() {
        return descComarca;
    }

    public void setDescComarca(String descComarca) {
        this.descComarca = descComarca;
    }

    public Long getMunicipio() {
        return municipio;
    }

    public void setMunicipio(Long municipio) {
        this.municipio = municipio;
    }

    public String getDescMunicipio() {
        return descMunicipio;
    }

    public void setDescMun(String descMunicipio) {
        this.descMunicipio = descMunicipio;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
