/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide48.vo;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author INGDGC
 */
public class ColecTrayectoriaEntidad{
       
    private Integer id;
    private Integer trayCodColectivo;
    private Integer trayIdFkProgConvActGrupo;
    private Integer trayIdFkProgConvActSubGrPre;
    private String trayNumExpediente;
    private Integer trayCodigoEntidad;
    private String trayDescripcion;
    private Integer trayTieneExperiencia;
    private String trayNombreAdmonPublica;
    private Date trayFechaInicio;
    private String trayFechaInicioString;
    private Date trayFechaFin;
    private String trayFechaFinString;
    private Double trayNumeroMeses;
    private Date trayFechaAlta;
    private Date trayFechaModificacion;
    //Campos Adicionales Descripciones o informacion en tablas relacionadas Uso interfaces no mapeados en metodo mapping util    
    private String colectivoDescripcion;
    
    public ColecTrayectoriaEntidad() {
    }

    public ColecTrayectoriaEntidad(Integer id) {
        this.id = id;
    }

    public ColecTrayectoriaEntidad(Integer trayCodColectivo,Integer trayCodigoEntidad, String trayNumExpediente) {
        this.trayCodColectivo = trayCodColectivo;
        this.trayNumExpediente = trayNumExpediente;
        this.trayCodigoEntidad = trayCodigoEntidad;
    }

    public ColecTrayectoriaEntidad(Integer id, Integer trayCodColectivo, Integer trayIdFkProgConvActGrupo, Integer trayIdFkProgConvActSubGrPre, String trayNumExpediente, Integer trayCodigoEntidad, String trayDescripcion, Integer trayTieneExperiencia, String trayNombreAdmonPublica, Date trayFechaInicio, Date trayFechaFin, Double trayNumeroMeses, Date trayFechaAlta, Date trayFechaModificacion) {
        this.id = id;
        this.trayCodColectivo = trayCodColectivo;
        this.trayIdFkProgConvActGrupo = trayIdFkProgConvActGrupo;
        this.trayIdFkProgConvActSubGrPre = trayIdFkProgConvActSubGrPre;
        this.trayNumExpediente = trayNumExpediente;
        this.trayCodigoEntidad = trayCodigoEntidad;
        this.trayDescripcion = trayDescripcion;
        this.trayTieneExperiencia = trayTieneExperiencia;
        this.trayNombreAdmonPublica = trayNombreAdmonPublica;
        this.trayFechaInicio = trayFechaInicio;
        this.trayFechaFin = trayFechaFin;
        this.trayNumeroMeses = trayNumeroMeses;
        this.trayFechaAlta = trayFechaAlta;
        this.trayFechaModificacion = trayFechaModificacion;
    }
    
    

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTrayCodColectivo() {
        return trayCodColectivo;
    }

    public void setTrayCodColectivo(Integer trayCodColectivo) {
        this.trayCodColectivo = trayCodColectivo;
    }

    public Integer getTrayIdFkProgConvActGrupo() {
        return trayIdFkProgConvActGrupo;
    }

    public void setTrayIdFkProgConvActGrupo(Integer trayIdFkProgConvActGrupo) {
        this.trayIdFkProgConvActGrupo = trayIdFkProgConvActGrupo;
    }

    public Integer getTrayIdFkProgConvActSubGrPre() {
        return trayIdFkProgConvActSubGrPre;
    }

    public void setTrayIdFkProgConvActSubGrPre(Integer trayIdFkProgConvActSubGrPre) {
        this.trayIdFkProgConvActSubGrPre = trayIdFkProgConvActSubGrPre;
    }

    public String getTrayNumExpediente() {
        return trayNumExpediente;
    }

    public void setTrayNumExpediente(String trayNumExpediente) {
        this.trayNumExpediente = trayNumExpediente;
    }

    public Integer getTrayCodigoEntidad() {
        return trayCodigoEntidad;
    }

    public void setTrayCodigoEntidad(Integer trayCodigoEntidad) {
        this.trayCodigoEntidad = trayCodigoEntidad;
    }

    public String getTrayDescripcion() {
        return trayDescripcion;
    }

    public void setTrayDescripcion(String trayDescripcion) {
        this.trayDescripcion = trayDescripcion;
    }

    public Integer getTrayTieneExperiencia() {
        return trayTieneExperiencia;
    }

    public void setTrayTieneExperiencia(Integer trayTieneExperiencia) {
        this.trayTieneExperiencia = trayTieneExperiencia;
    }

    public String getTrayNombreAdmonPublica() {
        return trayNombreAdmonPublica;
    }

    public void setTrayNombreAdmonPublica(String trayNombreAdmonPublica) {
        this.trayNombreAdmonPublica = trayNombreAdmonPublica;
    }

    public Date getTrayFechaInicio() {
        return trayFechaInicio;
    }

    public void setTrayFechaInicio(Date trayFechaInicio) {
        this.trayFechaInicio = trayFechaInicio;
    }

    public Date getTrayFechaFin() {
        return trayFechaFin;
    }

    public void setTrayFechaFin(Date trayFechaFin) {
        this.trayFechaFin = trayFechaFin;
    }

    public Double getTrayNumeroMeses() {
        return trayNumeroMeses;
    }

    public void setTrayNumeroMeses(Double trayNumeroMeses) {
        this.trayNumeroMeses = trayNumeroMeses;
    }

    public Date getTrayFechaAlta() {
        return trayFechaAlta;
    }

    public void setTrayFechaAlta(Date trayFechaAlta) {
        this.trayFechaAlta = trayFechaAlta;
    }

    public Date getTrayFechaModificacion() {
        return trayFechaModificacion;
    }

    public void setTrayFechaModificacion(Date trayFechaModificacion) {
        this.trayFechaModificacion = trayFechaModificacion;
    }

    public String getTrayFechaInicioString() {
        if(trayFechaInicio!=null)trayFechaInicioString=new SimpleDateFormat("dd/MM/yyyy").format(trayFechaInicio);
        return trayFechaInicioString;
    }

    public void setTrayFechaInicioString(String trayFechaInicioString) {
        this.trayFechaInicioString = trayFechaInicioString;
    }

    public String getTrayFechaFinString() {
        if(trayFechaFin!=null)trayFechaFinString=new SimpleDateFormat("dd/MM/yyyy").format(trayFechaFin);
        return trayFechaFinString;
    }

    public void setTrayFechaFinString(String trayFechaFinString) {
        this.trayFechaFinString = trayFechaFinString;
    }
    /**
     * Campo Adicional informacion en tablas relacionadas Uso interfaces no mapeados en metodo mapping util
     */
    public String getColectivoDescripcion() {
        return colectivoDescripcion;
    }
    /**
     * Campo Adicional informacion en tablas relacionadas Uso interfaces no mapeados en metodo mapping util
     */
    public void setColectivoDescripcion(String colectivoDescripcion) {
        this.colectivoDescripcion = colectivoDescripcion;
    }
    
    
}
