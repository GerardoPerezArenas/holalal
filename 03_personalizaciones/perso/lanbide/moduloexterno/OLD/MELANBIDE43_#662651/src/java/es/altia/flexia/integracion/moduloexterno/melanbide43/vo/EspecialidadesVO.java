/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.flexia.integracion.moduloexterno.melanbide43.vo;



/**
 *
 * @author davidg
 */
public class EspecialidadesVO {
    
    private Integer id;              //ID
    private String numExp;                      //ESP_NUM
    private String codCP;                       //ESP_CODCP
    private String denominacion;                //ESP_DENOM
    private Integer inscripcionPresencial;      //ESP_PRESE
    private Integer inscripcionTeleformacion;   //ESP_TELEF
    private Integer acreditacion;               //ESP_ACRED
    private String motDeneg;                    //ESP_MOT_DENEG
    
    public EspecialidadesVO()
    {
        
    }

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

    public String getCodCP() {
        return codCP;
    }

    public void setCodCP(String codCP) {
        this.codCP = codCP;
    }

    public String getDenominacion() {
        return denominacion;
    }

    public void setDenominacion(String denominacion) {
        this.denominacion = denominacion;
    }

    public Integer  getInscripcionPresencial() {
        return inscripcionPresencial;
    }

    public void setInscripcionPresencial(Integer inscripcionPresencial) {
        this.inscripcionPresencial = inscripcionPresencial;
    }

    public Integer getInscripcionTeleformacion() {
        return inscripcionTeleformacion;
    }

    public void setInscripcionTeleformacion(Integer inscripcionTeleformacion) {
        this.inscripcionTeleformacion = inscripcionTeleformacion;
    }

    public Integer getAcreditacion() {
        return acreditacion;
    }

    public void setAcreditacion(Integer acreditacion) {
        this.acreditacion = acreditacion;
    }

    public String getMotDeneg() {
        return motDeneg;
    }

    public void setMotDeneg(String motDeneg) {
        this.motDeneg = motDeneg;
    }
}
