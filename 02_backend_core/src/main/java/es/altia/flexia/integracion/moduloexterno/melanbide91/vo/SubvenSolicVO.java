
package es.altia.flexia.integracion.moduloexterno.melanbide91.vo;


import java.util.Date;

public class SubvenSolicVO {
    
    
    private Integer id;
    private String numExp;
    private String tipoDatos;
    private String destipoDatos;
    private String tipo;
    private Date fecha;
    private String fechaStr;
    private String destino;
    private Double coste;
 
   // private Integer cantsolic;
     
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

    public String getTipoDatos() {
        return tipoDatos;
    }

    public void setTipoDatos(String tipoDatos) {
        this.tipoDatos = tipoDatos;
    }
    public String getDesTipoDatos() {
        return destipoDatos;
    }

    public void setDesTipoDatos(String destipoDatos) {
        this.destipoDatos = destipoDatos;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public Double getCoste() {
        return coste;
    }

    public void setCoste(Double coste) {
        this.coste = coste;
    }

   /* public Integer getCantsolic() {
        return cantsolic;
    }

    public void setCantsolic(Integer cantsolic) {
        this.cantsolic = cantsolic;
    }
*/
    public String getFechaStr() {
        return fechaStr;
    }

    public void setFechaStr(String fechaStr) {
        this.fechaStr = fechaStr;
    }
    
    
    
}
