package es.altia.flexia.integracion.moduloexterno.lanbide02.vo;

import es.altia.flexia.integracion.moduloexterno.lanbide02.util.Utilities;

public class GrupoCotizacionSumaVO {
    private int grupo;
    private double sumaContratosSolicitados=0;
    private double sumaContratosConcedidos=0;
    private double sumaMesesSolicitados=0;
    private double sumaMesesConcedidos=0;
    private double sumaCostesSolicitados=0;
    private double sumaCostesConcedidos=0;
    private double sumaSubvencionSolicitada=0;
    private double sumaSubvencionConcedida=0;
       
    /**
     * @return the grupo
     */
    public int getGrupo() {
        return grupo;
    }

    /**
     * @param grupo the grupo to set
     */
    public void setGrupo(int grupo) {
        this.grupo = grupo;
    }

    /**
     * @return the sumaContratosSolicitados
     */
    public double getSumaContratosSolicitados() {
        return sumaContratosSolicitados;
    }


    public String getSumaContratosSolicitadosConFormato() {
        return Utilities.formatoNumeroDecimales(sumaContratosSolicitados);
    }

    /**
     * @param sumaContratosSolicitados the sumaContratosSolicitados to set
     */
    public void setSumaContratosSolicitados(double sumaContratosSolicitados) {
        this.sumaContratosSolicitados = sumaContratosSolicitados;
    }

    /**
     * @return the sumaContratosConcedidos
     */
    public double getSumaContratosConcedidos() {
        return sumaContratosConcedidos;
    }


    public String getSumaContratosConcedidosConFormato() {
        return Utilities.formatoNumeroDecimales(sumaContratosConcedidos);
    }


    /**
     * @param sumaContratosConcedidos the sumaContratosConcedidos to set
     */
    public void setSumaContratosConcedidos(double sumaContratosConcedidos) {
        this.sumaContratosConcedidos = sumaContratosConcedidos;
    }

    /**
     * @return the sumaMesesSolicitados
     */
    public double getSumaMesesSolicitados() {
        return sumaMesesSolicitados;
    }


   /**
     * @return the sumaMesesSolicitados
     */
    public String getSumaMesesSolicitadosConFormato() {
        return Utilities.formatoNumeroDecimales(sumaMesesSolicitados);
    }

    /**
     * @param sumaMesesSolicitados the sumaMesesSolicitados to set
     */
    public void setSumaMesesSolicitados(double sumaMesesSolicitados) {
        this.sumaMesesSolicitados = sumaMesesSolicitados;
    }

    /**
     * @return the sumaMesesConcedidos
     */
    public double getSumaMesesConcedidos() {
        return sumaMesesConcedidos;
    }


    public String getSumaMesesConcedidosConFormato() {
        return Utilities.formatoNumeroDecimales(sumaMesesConcedidos);
    }

    /**
     * @param sumaMesesConcedidos the sumaMesesConcedidos to set
     */
    public void setSumaMesesConcedidos(double sumaMesesConcedidos) {
        this.sumaMesesConcedidos = sumaMesesConcedidos;
    }

    /**
     * @return the sumaCostesSolicitados
     */
    public double getSumaCostesSolicitados() {
        return sumaCostesSolicitados;
    }


    /**
     * @return the sumaCostesSolicitados
     */
    public String getSumaCostesSolicitadosConFormato() {
        return Utilities.formatoNumeroDecimales(sumaCostesSolicitados);
    }


    /**
     * @param sumaCostesSolicitados the sumaCostesSolicitados to set
     */
    public void setSumaCostesSolicitados(double sumaCostesSolicitados) {
        this.sumaCostesSolicitados = sumaCostesSolicitados;
    }

    /**
     * @return the sumaCostesConcedidos
     */
    public double getSumaCostesConcedidos() {
        return sumaCostesConcedidos;
    }

    /**
     * @return the sumaCostesConcedidos
     */
    public String getSumaCostesConcedidosConFormato() {
        return Utilities.formatoNumeroDecimales(sumaCostesConcedidos);
    }

    /**
     * @param sumaCostesConcedidos the sumaCostesConcedidos to set
     */
    public void setSumaCostesConcedidos(double sumaCostesConcedidos) {
        this.sumaCostesConcedidos = sumaCostesConcedidos;
    }

    /**
     * @return the sumaSubvencionSolicitada
     */
    public double getSumaSubvencionSolicitada() {
        return sumaSubvencionSolicitada;
    }

   /**
     * @return the sumaSubvencionSolicitada
     */
    public String getSumaSubvencionSolicitadaConFormato() {
        return Utilities.formatoNumeroDecimales(sumaSubvencionSolicitada);
    }

    /**
     * @param sumaSubvencionSolicitada the sumaSubvencionSolicitada to set
     */
    public void setSumaSubvencionSolicitada(double sumaSubvencionSolicitada) {
        this.sumaSubvencionSolicitada = sumaSubvencionSolicitada;
    }

    /**
     * @return the sumaSubvencionConcedida
     */
    public double getSumaSubvencionConcedida() {
        return sumaSubvencionConcedida;
    }


    public String getSumaSubvencionConcedidaConFormato() {
        return Utilities.formatoNumeroDecimales(sumaSubvencionConcedida);
    }

    /**
     * @param sumaSubvencionConcedida the sumaSubvencionConcedida to set
     */
    public void setSumaSubvencionConcedida(double sumaSubvencionConcedida) {
        this.sumaSubvencionConcedida = sumaSubvencionConcedida;
    }

    
   

}