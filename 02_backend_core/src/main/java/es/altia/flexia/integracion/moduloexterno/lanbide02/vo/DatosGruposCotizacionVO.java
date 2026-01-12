package es.altia.flexia.integracion.moduloexterno.lanbide02.vo;
import es.altia.flexia.integracion.moduloexterno.lanbide02.util.Utilities;
import java.util.ArrayList;

public class DatosGruposCotizacionVO {

    private ArrayList<GrupoCotizacionSumaVO> gruposCotizacion;
    private double totalContratosSolicitados;
    private double totalMesesSolicitados;
    private double totalCostesSolicitados;
    private double totalSubvencionSolicitada;
    private double totalContratosConcedidos;
    private double totalMesesConcedidos;
    private double totalCostesConcedidos;
    private double totalSubvencionConcedida;

    /**
     * @return the gruposCotizacion
     */
    public ArrayList<GrupoCotizacionSumaVO> getGruposCotizacion() {
        return this.gruposCotizacion;
    }


    public void setGruposCotizacion(ArrayList<GrupoCotizacionSumaVO> grupos) {
        this.gruposCotizacion = grupos;
    }

    /**
     * @return the totalContratosSolicitados
     */
    public double getTotalContratosSolicitados() {
        return totalContratosSolicitados;
    }

    public String getTotalContratosSolicitadosConFormato() {
        return Utilities.formatoNumeroDecimales(totalContratosSolicitados);
    }


    /**
     * @param totalContratosSolicitados the totalContratosSolicitados to set
     */
    public void setTotalContratosSolicitados(double totalContratosSolicitados) {
        this.totalContratosSolicitados = totalContratosSolicitados;
    }

    /**
     * @return the totalMesesSolicitados
     */
    public double getTotalMesesSolicitados() {
        return totalMesesSolicitados;
    }

    public String getTotalMesesSolicitadosConFormato() {
        return Utilities.formatoNumeroDecimales(totalMesesSolicitados);
    }

    /**
     * @param totalMesesSolicitados the totalMesesSolicitados to set
     */
    public void setTotalMesesSolicitados(double totalMesesSolicitados) {
        this.totalMesesSolicitados = totalMesesSolicitados;
    }

    /**
     * @return the totalCostesSolicitados
     */
    public double getTotalCostesSolicitados() {
        return totalCostesSolicitados;
    }

    /**
     * @return the totalCostesSolicitados
     */
    public String getTotalCostesSolicitadosConFormato() {
        return Utilities.formatoNumeroDecimales(totalCostesSolicitados);
    }

    /**
     * @param totalCostesSolicitados the totalCostesSolicitados to set
     */
    public void setTotalCostesSolicitados(double totalCostesSolicitados) {
        this.totalCostesSolicitados = totalCostesSolicitados;
    }

    /**
     * @return the totalSubvencionSolicitada
     */
    public double getTotalSubvencionSolicitada() {
        return totalSubvencionSolicitada;
    }

    public String getTotalSubvencionSolicitadaConFormato() {
        return Utilities.formatoNumeroDecimales(totalSubvencionSolicitada);
    }

    /**
     * @param totalSubvencionSolicitada the totalSubvencionSolicitada to set
     */
    public void setTotalSubvencionSolicitada(double totalSubvencionSolicitada) {
        this.totalSubvencionSolicitada = totalSubvencionSolicitada;
    }

    /**
     * @return the totalContratosConcedidos
     */
    public double getTotalContratosConcedidos() {
        return totalContratosConcedidos;
    }

    public String getTotalContratosConcedidosConFormato() {
        return Utilities.formatoNumeroDecimales(totalContratosConcedidos);
    }

    /**
     * @param totalContratosConcedidos the totalContratosConcedidos to set
     */
    public void setTotalContratosConcedidos(double totalContratosConcedidos) {
        this.totalContratosConcedidos = totalContratosConcedidos;
    }

    /**
     * @return the totalMesesConcedidos
     */
    public double getTotalMesesConcedidos() {
        return totalMesesConcedidos;
    }

    public String getTotalMesesConcedidosConFormato() {
        return Utilities.formatoNumeroDecimales(totalMesesConcedidos);
    }

    /**
     * @param totalMesesConcedidos the totalMesesConcedidos to set
     */
    public void setTotalMesesConcedidos(double totalMesesConcedidos) {
        this.totalMesesConcedidos = totalMesesConcedidos;
    }

    /**
     * @return the totalCostesConcedidos
     */
    public double getTotalCostesConcedidos() {
        return totalCostesConcedidos;
    }

    public String getTotalCostesConcedidosConFormato() {
        return Utilities.formatoNumeroDecimales(totalCostesConcedidos);
    }

    /**
     * @param totalCostesConcedidos the totalCostesConcedidos to set
     */
    public void setTotalCostesConcedidos(double totalCostesConcedidos) {
        this.totalCostesConcedidos = totalCostesConcedidos;
    }

    /**
     * @return the totalSubvencionConcedida
     */
    public double getTotalSubvencionConcedida() {
        return totalSubvencionConcedida;
    }

    
    public String getTotalSubvencionConcedidaConFormato() {
        return Utilities.formatoNumeroDecimales(totalSubvencionConcedida);
    }

    /**
     * @param totalSubvencionConcedida the totalSubvencionConcedida to set
     */
    public void setTotalSubvencionConcedida(double totalSubvencionConcedida) {
        this.totalSubvencionConcedida = totalSubvencionConcedida;
    }     
    
}