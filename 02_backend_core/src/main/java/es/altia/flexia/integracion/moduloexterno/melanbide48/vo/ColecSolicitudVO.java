/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide48.vo;

import java.util.Date;

/**
 *
 * @author santiagoc
 */
public class ColecSolicitudVO 
{
    private Long codSolicitud;
    private String numExp;
    private Integer ejercicio;
    private Integer col1Ar;
    private Integer col1Bi;
    private Integer col1Gi;
    private Integer col2Ar;
    private Integer col2Bi;
    private Integer col2Gi;
    private Integer col3Ar;
    private Integer col3Bi;
    private Integer col3Gi;
    private Integer col4Ar;
    private Integer col4Bi;
    private Integer col4Gi;
    private Long col1Ult5Val;
    private Long col2Ult5Val;
    private Long totVal;
    private Date fecSysdate;
    private Integer codigoColectivo;
    private String codigoColectivoDesc;
    private Integer territorioHistorico;
    private String territorioHistoricoDesc;
    private Integer ambitoComarca;
    private String ambitoComarcaDesc;
    private Integer numeroBloquesHoras;
    private Integer numeroUbicaciones;

    public ColecSolicitudVO() {
    }
    
    public Long getCodSolicitud() {
        return codSolicitud;
    }
    
    public ColecSolicitudVO(String numExp) {
        this.numExp = numExp;
    }

    public ColecSolicitudVO(Long codSolicitud, String numExp) {
        this.codSolicitud = codSolicitud;
        this.numExp = numExp;
    }

    public void setCodSolicitud(Long codSolicitud) {
        this.codSolicitud = codSolicitud;
    }

    public String getNumExp() {
        return numExp;
    }

    public void setNumExp(String numExp) {
        this.numExp = numExp;
    }

    public Integer getEjercicio() {
        return ejercicio;
    }

    public void setEjercicio(Integer ejercicio) {
        this.ejercicio = ejercicio;
    }

    public Integer getCol1Ar() {
        return col1Ar;
    }

    public void setCol1Ar(Integer col1Ar) {
        this.col1Ar = col1Ar;
    }

    public Integer getCol1Bi() {
        return col1Bi;
    }

    public void setCol1Bi(Integer col1Bi) {
        this.col1Bi = col1Bi;
    }

    public Integer getCol1Gi() {
        return col1Gi;
    }

    public void setCol1Gi(Integer col1Gi) {
        this.col1Gi = col1Gi;
    }

    public Integer getCol2Ar() {
        return col2Ar;
    }

    public void setCol2Ar(Integer col2Ar) {
        this.col2Ar = col2Ar;
    }

    public Integer getCol2Bi() {
        return col2Bi;
    }

    public void setCol2Bi(Integer col2Bi) {
        this.col2Bi = col2Bi;
    }

    public Integer getCol2Gi() {
        return col2Gi;
    }

    public void setCol2Gi(Integer col2Gi) {
        this.col2Gi = col2Gi;
    }

    public Integer getCol3Ar() {
        return col3Ar;
    }

    public void setCol3Ar(Integer col3Ar) {
        this.col3Ar = col3Ar;
    }

    public Integer getCol3Bi() {
        return col3Bi;
    }

    public void setCol3Bi(Integer col3Bi) {
        this.col3Bi = col3Bi;
    }

    public Integer getCol3Gi() {
        return col3Gi;
    }

    public void setCol3Gi(Integer col3Gi) {
        this.col3Gi = col3Gi;
    }

    public Integer getCol4Ar() {
        return col4Ar;
    }

    public void setCol4Ar(Integer col4Ar) {
        this.col4Ar = col4Ar;
    }

    public Integer getCol4Bi() {
        return col4Bi;
    }

    public void setCol4Bi(Integer col4Bi) {
        this.col4Bi = col4Bi;
    }

    public Integer getCol4Gi() {
        return col4Gi;
    }

    public void setCol4Gi(Integer col4Gi) {
        this.col4Gi = col4Gi;
    }

    public Long getCol1Ult5Val() {
        return col1Ult5Val;
    }

    public void setCol1Ult5Val(Long col1Ult5Val) {
        this.col1Ult5Val = col1Ult5Val;
    }

    public Long getCol2Ult5Val() {
        return col2Ult5Val;
    }

    public void setCol2Ult5Val(Long col2Ult5Val) {
        this.col2Ult5Val = col2Ult5Val;
    }

    public Long getTotVal() {
        return totVal;
    }

    public void setTotVal(Long totVal) {
        this.totVal = totVal;
    }

    public Date getFecSysdate() {
        return fecSysdate;
    }

    public void setFecSysdate(Date fecSysdate) {
        this.fecSysdate = fecSysdate;
    }

    public Integer getCodigoColectivo() {
        return codigoColectivo;
    }

    public void setCodigoColectivo(Integer codigoColectivo) {
        this.codigoColectivo = codigoColectivo;
    }

    public Integer getTerritorioHistorico() {
        return territorioHistorico;
    }

    public Integer getAmbitoComarca() {
        return ambitoComarca;
    }

    public void setAmbitoComarca(Integer ambitoComarca) {
        this.ambitoComarca = ambitoComarca;
    }
    public void setTerritorioHistorico(Integer territorioHistorico) {
        this.territorioHistorico = territorioHistorico;
    }

    public Integer getNumeroBloquesHoras() {
        return numeroBloquesHoras;
    }

    public void setNumeroBloquesHoras(Integer numeroBloquesHoras) {
        this.numeroBloquesHoras = numeroBloquesHoras;
    }

    public Integer getNumeroUbicaciones() {
        return numeroUbicaciones;
    }

    public void setNumeroUbicaciones(Integer numeroUbicaciones) {
        this.numeroUbicaciones = numeroUbicaciones;
    }

    public String getCodigoColectivoDesc() {
        return codigoColectivoDesc;
    }

    public void setCodigoColectivoDesc(String codigoColectivoDesc) {
        this.codigoColectivoDesc = codigoColectivoDesc;
    }

    public String getTerritorioHistoricoDesc() {
        return territorioHistoricoDesc;
    }

    public void setTerritorioHistoricoDesc(String territorioHistoricoDesc) {
        this.territorioHistoricoDesc = territorioHistoricoDesc;
    }

    public String getAmbitoComarcaDesc() {
        return ambitoComarcaDesc;
    }

    public void setAmbitoComarcaDesc(String ambitoComarcaDesc) {
        this.ambitoComarcaDesc = ambitoComarcaDesc;
    }
    
}
