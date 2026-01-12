/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide48.vo;

import java.util.Date;

/**
 *
 * @author INGDGC
 */
public class ColecProgConvActPredefinidaColectivo {
    private Integer id;
    private Integer idConvocatoriaActiva;
    private String idConvocatoriaActivaDesc;
    private Integer codigoGrupo;
    private String codigoGrupoDesc;
    private Integer codigoConvocatoriaPred;
    private String codigoConvocatoriaPredDesc;
    private Integer colectivo;
    private String colectivoDesc;
    private Date fechaInicio;
    private Date fechaFin;

    public ColecProgConvActPredefinidaColectivo() {
    }

    public ColecProgConvActPredefinidaColectivo(Integer id, Integer idConvocatoriaActiva, String idConvocatoriaActivaDesc, Integer codigoGrupo, String codigoGrupoDesc, Integer codigoConvocatoriaPred, String codigoConvocatoriaPredDesc, Integer colectivo, String colectivoDesc, Date fechaInicio, Date fechaFin) {
        this.id = id;
        this.idConvocatoriaActiva = idConvocatoriaActiva;
        this.idConvocatoriaActivaDesc = idConvocatoriaActivaDesc;
        this.codigoGrupo = codigoGrupo;
        this.codigoGrupoDesc = codigoGrupoDesc;
        this.codigoConvocatoriaPred = codigoConvocatoriaPred;
        this.codigoConvocatoriaPredDesc = codigoConvocatoriaPredDesc;
        this.colectivo = colectivo;
        this.colectivoDesc = colectivoDesc;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }
    

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdConvocatoriaActiva() {
        return idConvocatoriaActiva;
    }

    public void setIdConvocatoriaActiva(Integer idConvocatoriaActiva) {
        this.idConvocatoriaActiva = idConvocatoriaActiva;
    }

    public String getIdConvocatoriaActivaDesc() {
        return idConvocatoriaActivaDesc;
    }

    public void setIdConvocatoriaActivaDesc(String idConvocatoriaActivaDesc) {
        this.idConvocatoriaActivaDesc = idConvocatoriaActivaDesc;
    }

    public Integer getCodigoGrupo() {
        return codigoGrupo;
    }

    public void setCodigoGrupo(Integer codigoGrupo) {
        this.codigoGrupo = codigoGrupo;
    }

    public String getCodigoGrupoDesc() {
        return codigoGrupoDesc;
    }

    public void setCodigoGrupoDesc(String codigoGrupoDesc) {
        this.codigoGrupoDesc = codigoGrupoDesc;
    }

    public Integer getCodigoConvocatoriaPred() {
        return codigoConvocatoriaPred;
    }

    public void setCodigoConvocatoriaPred(Integer codigoConvocatoriaPred) {
        this.codigoConvocatoriaPred = codigoConvocatoriaPred;
    }

    public String getCodigoConvocatoriaPredDesc() {
        return codigoConvocatoriaPredDesc;
    }

    public void setCodigoConvocatoriaPredDesc(String codigoConvocatoriaPredDesc) {
        this.codigoConvocatoriaPredDesc = codigoConvocatoriaPredDesc;
    }

    public Integer getColectivo() {
        return colectivo;
    }

    public void setColectivo(Integer colectivo) {
        this.colectivo = colectivo;
    }

    public String getColectivoDesc() {
        return colectivoDesc;
    }

    public void setColectivoDesc(String colectivoDesc) {
        this.colectivoDesc = colectivoDesc;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }
    
        
}
