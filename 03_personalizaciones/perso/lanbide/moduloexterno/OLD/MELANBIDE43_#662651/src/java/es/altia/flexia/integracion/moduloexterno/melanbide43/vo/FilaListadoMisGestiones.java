package es.altia.flexia.integracion.moduloexterno.melanbide43.vo;

import java.util.Date;

/**
 * @author david.caamano
 * @version 16/08/2012 1.0
 * Historial de cambios:
 * <ol>
 *  <li>david.caamano * 16-08-2012 * #53275 Edici�n inicial</li>
 * </ol> 
 */
public class FilaListadoMisGestiones {
    
    //Propiedades
    private Integer id;
    private String numExp;
    private Integer regInicio;
    private Integer numInicio;
    private String tipoOperacion;
    private Integer tipoCodInte;
    private String codInteresado;
    private String tramiteInicio;
    private String tramiteFin;
    private Date fechaGenerado;
    private Date fechaProcesado;
    private String rdoProceso;
    private String regAsun;
    private String fechaAsun;
    private Integer intentos;
    
    //M�todos de acceso

    public Integer getId() {
        return id;
    }//getCodUnidad
    public void setId(Integer id) {
        this.id = id;
    }//setCodUnidad

    public String getNumExp() {
        return numExp;
    }//getDescUnidadC
    public void setNumExp(String numExp) {
        this.numExp = numExp;
    }//setDescUnidadC
    
    public String getTipoOperacion() {
        return tipoOperacion;
    }//getCentroAcreditado
    public void setTipoOperacion(String tipoOperacion) {
        this.tipoOperacion = tipoOperacion;
    }
    
    public Integer getRegInicio() {
        return regInicio;
    }//getDescUnidadE
    public void setRegInicio(Integer regInicio) {
        this.regInicio = regInicio;
    }//setDescUnidadE

    public Integer getNumInicio() {
        return numInicio;
    }//getNivel
    public void setNumInicio(Integer numInicio) {
        this.numInicio = numInicio;
    }//setNivel

    public Integer getTipoCodInte() {
        return tipoCodInte;
    }//getCentroAcreditado
    public void setTipoCodInte(Integer tipoCodInte) {
        this.tipoCodInte = tipoCodInte;
    }//setCentroAcreditado

    public String getCodInteresado() {
        return codInteresado;
    }//getCodCentro
    public void setCodInteresado(String codInteresado) {
        this.codInteresado = codInteresado;
    }//setCodCentro
    
    public String getTramiteInicio(){
        return tramiteInicio;
    }//getDescCentro
    public void setTramiteInicio(String tramiteInicio){
        this.tramiteInicio = tramiteInicio;
    }//setDescCentro

    public String getTramiteFin() {
        return tramiteFin;
    }//getCodCertificado
    public void setTramiteFin(String tramiteFin) {
        this.tramiteFin = tramiteFin;
    }//setCodCertificado
    
    public Date getFechaGenerado() {
        return fechaGenerado;
    }//getCodOrganizacion
    public void setFechaGenerado(Date fechaGenerado) {
        this.fechaGenerado = fechaGenerado;
    }//setCodOrganizacion
    
    public Date getFechaProcesado() {
        return fechaProcesado;
    }//getCodOrganizacion
    public void setFechaProcesado(Date fechaProcesado) {
        this.fechaProcesado = fechaProcesado;
    }//setCodOrganizacion
    

    public String getRdoProceso() {
        return rdoProceso;
    }
    public void setRdoProceso(String rdoProceso) {
        this.rdoProceso = rdoProceso;
    } 
    
    public String getRegAsun() {
        return regAsun;
    }
    public void setRegAsun(String regAsun) {
        this.regAsun = regAsun;
    } 
    
    public String getFechaAsun() {
        return fechaAsun;
    }
    public void setFechaAsun(String fechaAsun) {
        this.fechaAsun = fechaAsun;
    } 
    public Integer getIntentos() {
        return intentos;
    }
    public void setIntentos(Integer intentos) {
        this.intentos = intentos;
    } 
}//class
