package es.altia.flexia.integracion.moduloexterno.melanbide62.vo;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class InfoExpedienteVO {
    private String numExpediente;
    private String codProcedimiento;
    private List<InfoTramiteVO> listaTramites;
    private String mensajeValidaciones = "";
    // Propiedades que guardan valores de campos suplementarios del expediente
    private int diasSepe;
    private String fichaTecObserv;
    // Propiedad tipo HashMap que almacena los valores para los campos suplementarios cuyo nombre es la clave en cada par <clave,valor>
    private HashMap<String,Object> camposSuplementarios = null;
    // Propiedades que almacenan valores calculados
    private long diasTotalSolicitud;
    private String diasRestantes;

    public InfoExpedienteVO() {
        camposSuplementarios = new HashMap<String, Object>();
    }

    /**
     * @return the numExpediente
     */
    public String getNumExpediente() {
        return numExpediente;
    }

    /**
     * @param numExpediente the numExpediente to set
     */
    public void setNumExpediente(String numExpediente) {
        this.numExpediente = numExpediente;
    }

    /**
     * @return the codProcedimiento
     */
    public String getCodProcedimiento() {
        return codProcedimiento;
    }

    /**
     * @param codProcedimiento the codProcedimiento to set
     */
    public void setCodProcedimiento(String codProcedimiento) {
        this.codProcedimiento = codProcedimiento;
    }

    /**
     * @return the listaTramites
     */
    public List<InfoTramiteVO> getListaTramites() {
        return listaTramites;
    }

    /**
     * @param listaTramites the listaTramites to set
     */
    public void setListaTramites(List<InfoTramiteVO> listaTramites) {
        this.listaTramites = listaTramites;
    }

     /**
     * @return el valor del campo suplementario DIASSEPE
     */
    public int getDiasSepe() {
        Object valor = this.getCampoSuplementario("DIASSEPE");
        if(valor==null) return 0;
        return (Integer) valor;
    }

    /**
     * @param diasSepe añade al HashMap un par con la clave DIASSEPE  y el valor que se pasa
     */
    public void setDiasSepe(int diasSepe) {
        this.setCampoSuplementario("DIASSEPE", diasSepe);
        this.diasSepe = diasSepe;
    }
    

    /**
     * @return el valor de FICHATECNICAOBS o una cadena vacía si el campo es nulo
     */
    public String getFichaTecObserv() {
        String valor = (String) getCampoSuplementario("FICHATECNICAOBS");
        if(valor==null) return "";
        return valor;
    }

    /**
     * @param fichaTecObserv añade al HashMap la clave FICHATECNICAOBS con el valor pasado
     */
    public void setFichaTecObserv(String fichaTecObserv) {
        this.setCampoSuplementario("FICHATECNICAOBS", fichaTecObserv);
        this.fichaTecObserv = fichaTecObserv;
    }
    
    /**
     * Calcula el total de días de solicitud sumando los días de solicitud de cada trámite y lo establece en la propiedad totalDiasSolicitud
     */
    public void calcularTotalDiasSol(){
        long total = 0;
        if(listaTramites!=null && listaTramites.size()>0){
            for(InfoTramiteVO infoTr : this.getListaTramites()){
                total += infoTr.getDiasSolPago();
            }
        }
        this.diasTotalSolicitud = total;
    }
    
    /**
     * @return El valor de diasTotalSolicitud
     */
    public long getDiasTotalSolicitud(){
        return this.diasTotalSolicitud;
    } 

    /**
     * @return el HashMap con todos loc campos suplementarios
     */
    public HashMap<String,Object> getCamposSuplementarios() {
        return camposSuplementarios;
    }
    
    /**
     * @param clave la clave para la que queremos recuperar el valo
     * @return el valor de dicha clave
     */
    public Object getCampoSuplementario(String clave) {
        return getCamposSuplementarios().get(clave);
    }

    /**
     * @param clave la clave del par <clave,valor> que queremos añadir
     * @param valor el valor que corresponde a la clave pasada
     */
    public void setCampoSuplementario(String clave, Object valor) {
        this.getCamposSuplementarios().put(clave, valor);
    }

    /**
     * @return the diasRestantes
     */
    public String getDiasRestantes() {
        return diasRestantes;
    }

    /**
     * Establece el valor de la propiedad diasRestantes como el valo del campo suplementario DIASSEPE menos el valor de la propiedad diasTotalSolicitud
     */
    public void setDiasRestantes() {
        long dias = this.getDiasSepe()-this.getDiasTotalSolicitud();
        this.diasRestantes = String.valueOf(dias);
    }

    /**
     * @return the mensajeValidaciones
     */
    public String getMensajeValidaciones() {
        return mensajeValidaciones;
    }

    /**
     * @param mensajeValidaciones the mensajeValidaciones to set
     */
    public void setMensajeValidaciones(String mensajeValidaciones) {
        this.mensajeValidaciones = mensajeValidaciones;
    }
}
