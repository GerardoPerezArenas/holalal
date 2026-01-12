/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide48.vo;

/**
 *
 * @author INGDGC
 */
public class ColecProcesoAdjudicacionExcelDatosVO {
    
    private int idConvocatoria;
    private int idColectivo;
    private String numeroExpediente;
    private int idAmbito;
    private int idUbicacion;
    private int idEntidadPadre;
    private String entidadPadreCif;
    private String entidadPadreNombre;
    private int idEntidad;
    private String entidadCif;
    private String entidadNombre;
    private Double puntuacionTrayectoriaEentidad;
    private Double puntuacionUbicacionCT;
    private Double puntuacionSegundosLocales;
    private Double puntuacionPlanIgualdad;
    private Double puntuacionCertificadoCalidad;
    private Double puntuacionEspacioComplem;
    private Double puntuacionCentroEspEmpleo;
    private Double puntuacionEmpoPromEmpInsercion;
    private Double valoracionTotalUbicacion;
    private String fechaRegistroStr;
    private Double bloquesHorasSolicitados;
    private Double numeroUbicaciones;
    private Double valoracionTotalMaxEntidad;
    private String aceptNumeroSupeHorasStr;
    private int idBdAdjudicacion;
    private Double horasAsignadas;
    private String direccion;
    private String territorioHistoricoDes;
    private String municipioDes;
    private String codigoPostal;
    private String expEnTramResolProvisional;
    private int ubicacionAdjudicada;
    public ColecProcesoAdjudicacionExcelDatosVO() {
    }

    public ColecProcesoAdjudicacionExcelDatosVO(int idConvocatoria, int idColectivo, String numeroExpediente, int idAmbito, int idUbicacion, int idEntidadPadre, String entidadPadreCif, String entidadPadreNombre, int idEntidad, String entidadCif, String entidadNombre, Double puntuacionTrayectoriaEentidad, Double puntuacionUbicacionCT, Double puntuacionSegundosLocales, Double puntuacionPlanIgualdad, Double puntuacionCertificadoCalidad, Double puntuacionEspacioComplem, Double puntuacionCentroEspEmpleo, Double puntuacionEmpoPromEmpInsercion, Double valoracionTotalUbicacion, String fechaRegistroStr, Double bloquesHorasSolicitados, Double numeroUbicaciones, Double valoracionTotalMaxEntidad, String aceptNumeroSupeHorasStr, int idBdAdjudicacion, Double horasAsignadas, String direccion, String territorioHistoricoDes, String municipioDes, String codigoPostal, String expEnTramResolProvisional,int ubicacionAdjudicada) {
        this.idConvocatoria = idConvocatoria;
        this.idColectivo = idColectivo;
        this.numeroExpediente = numeroExpediente;
        this.idAmbito = idAmbito;
        this.idUbicacion = idUbicacion;
        this.idEntidadPadre = idEntidadPadre;
        this.entidadPadreCif = entidadPadreCif;
        this.entidadPadreNombre = entidadPadreNombre;
        this.idEntidad = idEntidad;
        this.entidadCif = entidadCif;
        this.entidadNombre = entidadNombre;
        this.puntuacionTrayectoriaEentidad = puntuacionTrayectoriaEentidad;
        this.puntuacionUbicacionCT = puntuacionUbicacionCT;
        this.puntuacionSegundosLocales = puntuacionSegundosLocales;
        this.puntuacionPlanIgualdad = puntuacionPlanIgualdad;
        this.puntuacionCertificadoCalidad = puntuacionCertificadoCalidad;
        this.puntuacionEspacioComplem = puntuacionEspacioComplem;
        this.puntuacionCentroEspEmpleo = puntuacionCentroEspEmpleo;
        this.puntuacionEmpoPromEmpInsercion = puntuacionEmpoPromEmpInsercion;
        this.valoracionTotalUbicacion = valoracionTotalUbicacion;
        this.fechaRegistroStr = fechaRegistroStr;
        this.bloquesHorasSolicitados = bloquesHorasSolicitados;
        this.numeroUbicaciones = numeroUbicaciones;
        this.valoracionTotalMaxEntidad = valoracionTotalMaxEntidad;
        this.aceptNumeroSupeHorasStr = aceptNumeroSupeHorasStr;
        this.idBdAdjudicacion = idBdAdjudicacion;
        this.horasAsignadas = horasAsignadas;
        this.direccion = direccion;
        this.territorioHistoricoDes = territorioHistoricoDes;
        this.municipioDes = municipioDes;
        this.codigoPostal = codigoPostal;
        this.expEnTramResolProvisional = expEnTramResolProvisional;
        this.ubicacionAdjudicada = ubicacionAdjudicada;
    }

    public int getIdConvocatoria() {
        return idConvocatoria;
    }

    public void setIdConvocatoria(int idConvocatoria) {
        this.idConvocatoria = idConvocatoria;
    }

    public int getIdColectivo() {
        return idColectivo;
    }

    public void setIdColectivo(int idColectivo) {
        this.idColectivo = idColectivo;
    }

    public String getNumeroExpediente() {
        return numeroExpediente;
    }

    public void setNumeroExpediente(String numeroExpediente) {
        this.numeroExpediente = numeroExpediente;
    }

    public int getIdAmbito() {
        return idAmbito;
    }

    public void setIdAmbito(int idAmbito) {
        this.idAmbito = idAmbito;
    }

    public int getIdUbicacion() {
        return idUbicacion;
    }

    public void setIdUbicacion(int idUbicacion) {
        this.idUbicacion = idUbicacion;
    }

    public int getIdEntidadPadre() {
        return idEntidadPadre;
    }

    public void setIdEntidadPadre(int idEntidadPadre) {
        this.idEntidadPadre = idEntidadPadre;
    }

    public String getEntidadPadreCif() {
        return entidadPadreCif;
    }

    public void setEntidadPadreCif(String entidadPadreCif) {
        this.entidadPadreCif = entidadPadreCif;
    }

    public String getEntidadPadreNombre() {
        return entidadPadreNombre;
    }

    public void setEntidadPadreNombre(String entidadPadreNombre) {
        this.entidadPadreNombre = entidadPadreNombre;
    }

    public int getIdEntidad() {
        return idEntidad;
    }

    public void setIdEntidad(int idEntidad) {
        this.idEntidad = idEntidad;
    }

    public String getEntidadCif() {
        return entidadCif;
    }

    public void setEntidadCif(String entidadCif) {
        this.entidadCif = entidadCif;
    }

    public String getEntidadNombre() {
        return entidadNombre;
    }

    public void setEntidadNombre(String entidadNombre) {
        this.entidadNombre = entidadNombre;
    }

    public Double getPuntuacionTrayectoriaEentidad() {
        return puntuacionTrayectoriaEentidad;
    }

    public void setPuntuacionTrayectoriaEentidad(Double puntuacionTrayectoriaEentidad) {
        this.puntuacionTrayectoriaEentidad = puntuacionTrayectoriaEentidad;
    }

    public Double getPuntuacionUbicacionCT() {
        return puntuacionUbicacionCT;
    }

    public void setPuntuacionUbicacionCT(Double puntuacionUbicacionCT) {
        this.puntuacionUbicacionCT = puntuacionUbicacionCT;
    }

    public Double getPuntuacionSegundosLocales() {
        return puntuacionSegundosLocales;
    }

    public void setPuntuacionSegundosLocales(Double puntuacionSegundosLocales) {
        this.puntuacionSegundosLocales = puntuacionSegundosLocales;
    }

    public Double getPuntuacionPlanIgualdad() {
        return puntuacionPlanIgualdad;
    }

    public void setPuntuacionPlanIgualdad(Double puntuacionPlanIgualdad) {
        this.puntuacionPlanIgualdad = puntuacionPlanIgualdad;
    }

    public Double getPuntuacionCertificadoCalidad() {
        return puntuacionCertificadoCalidad;
    }

    public void setPuntuacionCertificadoCalidad(Double puntuacionCertificadoCalidad) {
        this.puntuacionCertificadoCalidad = puntuacionCertificadoCalidad;
    }

    public Double getPuntuacionEspacioComplem() {
        return puntuacionEspacioComplem;
    }

    public void setPuntuacionEspacioComplem(Double puntuacionEspacioComplem) {
        this.puntuacionEspacioComplem = puntuacionEspacioComplem;
    }

    public Double getPuntuacionCentroEspEmpleo() {
        return puntuacionCentroEspEmpleo;
    }

    public void setPuntuacionCentroEspEmpleo(Double puntuacionCentroEspEmpleo) {
        this.puntuacionCentroEspEmpleo = puntuacionCentroEspEmpleo;
    }

    public Double getPuntuacionEmpoPromEmpInsercion() {
        return puntuacionEmpoPromEmpInsercion;
    }

    public void setPuntuacionEmpoPromEmpInsercion(Double puntuacionEmpoPromEmpInsercion) {
        this.puntuacionEmpoPromEmpInsercion = puntuacionEmpoPromEmpInsercion;
    }

    public Double getValoracionTotalUbicacion() {
        return valoracionTotalUbicacion;
    }

    public void setValoracionTotalUbicacion(Double valoracionTotalUbicacion) {
        this.valoracionTotalUbicacion = valoracionTotalUbicacion;
    }

    public String getFechaRegistroStr() {
        return fechaRegistroStr;
    }

    public void setFechaRegistroStr(String fechaRegistroStr) {
        this.fechaRegistroStr = fechaRegistroStr;
    }

    public Double getBloquesHorasSolicitados() {
        return bloquesHorasSolicitados;
    }

    public void setBloquesHorasSolicitados(Double bloquesHorasSolicitados) {
        this.bloquesHorasSolicitados = bloquesHorasSolicitados;
    }

    public Double getNumeroUbicaciones() {
        return numeroUbicaciones;
    }

    public void setNumeroUbicaciones(Double numeroUbicaciones) {
        this.numeroUbicaciones = numeroUbicaciones;
    }

    public Double getValoracionTotalMaxEntidad() {
        return valoracionTotalMaxEntidad;
    }

    public void setValoracionTotalMaxEntidad(Double valoracionTotalMaxEntidad) {
        this.valoracionTotalMaxEntidad = valoracionTotalMaxEntidad;
    }

    public String getAceptNumeroSupeHorasStr() {
        return aceptNumeroSupeHorasStr;
    }

    public void setAceptNumeroSupeHorasStr(String aceptNumeroSupeHorasStr) {
        this.aceptNumeroSupeHorasStr = aceptNumeroSupeHorasStr;
    }

    public int getIdBdAdjudicacion() {
        return idBdAdjudicacion;
    }

    public void setIdBdAdjudicacion(int idBdAdjudicacion) {
        this.idBdAdjudicacion = idBdAdjudicacion;
    }

    public Double getHorasAsignadas() {
        return horasAsignadas;
    }

    public void setHorasAsignadas(Double horasAsignadas) {
        this.horasAsignadas = horasAsignadas;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTerritorioHistoricoDes() {
        return territorioHistoricoDes;
    }

    public void setTerritorioHistoricoDes(String territorioHistoricoDes) {
        this.territorioHistoricoDes = territorioHistoricoDes;
    }

    public String getMunicipioDes() {
        return municipioDes;
    }

    public void setMunicipioDes(String municipioDes) {
        this.municipioDes = municipioDes;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getExpEnTramResolProvisional() {
        return expEnTramResolProvisional;
    }

    public void setExpEnTramResolProvisional(String expEnTramResolProvisional) {
        this.expEnTramResolProvisional = expEnTramResolProvisional;
    }

    public int getUbicacionAdjudicada() {
        return ubicacionAdjudicada;
    }

    public void setUbicacionAdjudicada(int ubicacionAdjudicada) {
        this.ubicacionAdjudicada = ubicacionAdjudicada;
    }

    @Override
    public String toString() {
        return "ColecProcesoAdjudicacionExcelDatosVO{" + "idConvocatoria=" + idConvocatoria + ", idColectivo=" + idColectivo + ", numeroExpediente=" + numeroExpediente + ", idAmbito=" + idAmbito + ", idUbicacion=" + idUbicacion + ", idEntidadPadre=" + idEntidadPadre + ", entidadPadreCif=" + entidadPadreCif + ", entidadPadreNombre=" + entidadPadreNombre + ", idEntidad=" + idEntidad + ", entidadCif=" + entidadCif + ", entidadNombre=" + entidadNombre + ", puntuacionTrayectoriaEentidad=" + puntuacionTrayectoriaEentidad + ", puntuacionUbicacionCT=" + puntuacionUbicacionCT + ", puntuacionSegundosLocales=" + puntuacionSegundosLocales + ", puntuacionPlanIgualdad=" + puntuacionPlanIgualdad + ", puntuacionCertificadoCalidad=" + puntuacionCertificadoCalidad + ", puntuacionEspacioComplem=" + puntuacionEspacioComplem + ", puntuacionCentroEspEmpleo=" + puntuacionCentroEspEmpleo + ", puntuacionEmpoPromEmpInsercion=" + puntuacionEmpoPromEmpInsercion + ", valoracionTotalUbicacion=" + valoracionTotalUbicacion + ", fechaRegistroStr=" + fechaRegistroStr + ", bloquesHorasSolicitados=" + bloquesHorasSolicitados + ", numeroUbicaciones=" + numeroUbicaciones + ", valoracionTotalMaxEntidad=" + valoracionTotalMaxEntidad + ", aceptNumeroSupeHorasStr=" + aceptNumeroSupeHorasStr + ", idBdAdjudicacion=" + idBdAdjudicacion + ", horasAsignadas=" + horasAsignadas + ", direccion=" + direccion + ", territorioHistoricoDes=" + territorioHistoricoDes + ", municipioDes=" + municipioDes + ", codigoPostal=" + codigoPostal + ", expEnTramResolProvisional=" + expEnTramResolProvisional + ", ubicacionAdjudicada=" + ubicacionAdjudicada + '}';
    }
    
}
