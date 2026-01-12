/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide32.vo.orientacion;

/**
 *
 * @author santiagoc
 */
public class FilaUbicOrientacionVO 
{
    private Integer codigoUbic = -1;
    private String provincia = "-";
    private String ambito = "-";
    private String municipio = "-";
    private String direccion = "-";
    private String horas = "";
    private String despachos = "";
    private String aulaGrupal = "";
    private String valoracion = "";
    private String total = "";
    private String horasAdj = "";
    
    public FilaUbicOrientacionVO()
    {
        
    }

    public Integer getCodigoUbic() {
        return codigoUbic;
    }

    public void setCodigoUbic(Integer codigoUbic) {
        this.codigoUbic = codigoUbic;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getAmbito() {
        return ambito;
    }

    public void setAmbito(String ambito) {
        this.ambito = ambito;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getHoras() {
        return horas;
    }

    public void setHoras(String horas) {
        this.horas = horas;
    }

    public String getDespachos() {
        return despachos;
    }

    public void setDespachos(String despachos) {
        this.despachos = despachos;
    }

    public String getAulaGrupal() {
        return aulaGrupal;
    }

    public void setAulaGrupal(String aulaGrupal) {
        this.aulaGrupal = aulaGrupal;
    }

    public String getValoracion() {
        return valoracion;
    }

    public void setValoracion(String valoracion) {
        this.valoracion = valoracion;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getHorasAdj() {
        return horasAdj;
    }

    public void setHorasAdj(String horasAdj) {
        this.horasAdj = horasAdj;
    }
}
