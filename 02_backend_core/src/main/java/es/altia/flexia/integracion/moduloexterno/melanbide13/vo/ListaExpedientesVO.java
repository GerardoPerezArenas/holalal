
package es.altia.flexia.integracion.moduloexterno.melanbide13.vo;


public class ListaExpedientesVO {
    
    private Integer id;
    
    private String numExp;
    private String mes;
    private String descMes;
    private String territorio;
    private Double importe;
    private Double importeTotal;
    

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumExp() {
        return numExp;
    }

    public void setNumExp(String numExp) {
        this.numExp = numExp;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }
    public String getDescMes() {
        return descMes;
    }

    public void setDescMes(String descMes) {
        this.descMes = descMes;
    }

    public Double getImporte() {
        return importe;
    }

    public void setImporte(Double importe) {
        this.importe = importe;
    }
    public Double getImporteTotal() {
        return importeTotal;
    }

    public void setImporteTotal(Double importeTotal) {
        this.importeTotal = importeTotal;
    }

    public String getTerritorio() {
        return territorio;
    }

    public void setTerritorio(String territorio) {
        this.territorio = territorio;
    }
  
    
    
}
