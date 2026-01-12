/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide47.vo.orientacion;

import java.math.BigDecimal;

/**
 *
 * @author santiagoc
 */
public class OriUbicValoracionVO {

    //Id
    private Integer oriOrientUbicCod;

    //Datos Ubicacion
    private String descProvincia;
    private String descAmbito;
    private String descMunicipio;
    private String direccion;
    private String direccionNumero;
    private String direccionPiso;
    private String direccionLetra;
    private String codPostal;
    private String horasSolic;
    //Solicitud
    private Integer oriEntTrayectoria;
    private BigDecimal oriUbicPuntuacion;
    private String oriOrientDespachos;
    private String oriOrientAulagrupal;
    private String oriOrientUbicaEspacioAdicional;
    private String oriOrientUbicaEspAdicioHerrBusqEmpleo;
    private Integer oriCELocalPreviamenteAprobado;
    private Integer oriCEMantenimientoRequisitosLPA;
    private Integer oriEntPlanIgualdad;
    private Integer oriEntCertCalidad;

    //Validado
    private Double oriEntTrayectoriaVal;
    private BigDecimal oriUbicPuntuacionVal;
    private String oriOrientDespachosValidados;
    private String oriOrientAulaGrupalValidada;
    private String oriOrientUbicaEspacioAdicionalVal;
    private String oriOrientUbicaEspAdicioHerrBusqEmpleoVal;
    private Integer oriCELocalPreviamenteAprobadoVAL;
    private Integer oriCEMantenimientoRequisitosLPAVAL;
    private Integer oriEntPlanIgualdadVal;
    private Integer oriEntCertCalidadVal;

    //Valoracion
    private BigDecimal oriOrientValTray;
    private BigDecimal oriOrientValUbic;
    private Long oriOrientValDespachos;
    private Long oriOrientValAulas;
    private Long oriOrientVal1EspacioAdicional;
    private Long oriOrientValEspAdicioHerrBusqEmpleo;
    private Long oriCELocalPreviamenteAprobadoValoracion;
    private Long oriCEMantenimientoRequisitosLPAValoracion;
    private Long oriEntPlanIgualdadValoracion;
    private Long oriEntCertCalidadValoracion;
    //Puntuacion
    private BigDecimal oriOrientPuntuacion;

    //Observaciones
    private String oriOrientObservaciones;

    private Integer entSujetaDerPubl;
    private Integer entSujetaDerPublVal;
    private Integer compIgualdadOpcion;
    private String compIgualdadOpcionLiteral;
    private Integer compIgualdadOpcionVal;
    private String compIgualdadOpcionValLiteral;

    public OriUbicValoracionVO() {

    }

    public Integer getOriOrientUbicCod() {
        return oriOrientUbicCod;
    }

    public void setOriOrientUbicCod(Integer oriOrientUbicCod) {
        this.oriOrientUbicCod = oriOrientUbicCod;
    }

    public String getDescProvincia() {
        return descProvincia;
    }

    public void setDescProvincia(String descProvincia) {
        this.descProvincia = descProvincia;
    }

    public String getDescAmbito() {
        return descAmbito;
    }

    public void setDescAmbito(String descAmbito) {
        this.descAmbito = descAmbito;
    }

    public String getDescMunicipio() {
        return descMunicipio;
    }

    public void setDescMunicipio(String descMunicipio) {
        this.descMunicipio = descMunicipio;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCodPostal() {
        return codPostal;
    }

    public void setCodPostal(String codPostal) {
        this.codPostal = codPostal;
    }

    public String getHorasSolic() {
        return horasSolic;
    }

    public void setHorasSolic(String horasSolic) {
        this.horasSolic = horasSolic;
    }

    public Integer getOriEntTrayectoria() {
        return oriEntTrayectoria;
    }

    public void setOriEntTrayectoria(Integer oriEntTrayectoria) {
        this.oriEntTrayectoria = oriEntTrayectoria;
    }

    public BigDecimal getOriUbicPuntuacion() {
        return oriUbicPuntuacion;
    }

    public void setOriUbicPuntuacion(BigDecimal oriUbicPuntuacion) {
        this.oriUbicPuntuacion = oriUbicPuntuacion;
    }

    public String getOriOrientDespachos() {
        return oriOrientDespachos;
    }

    public void setOriOrientDespachos(String oriOrientDespachos) {
        this.oriOrientDespachos = oriOrientDespachos;
    }

    public String getOriOrientAulagrupal() {
        return oriOrientAulagrupal;
    }

    public void setOriOrientAulagrupal(String oriOrientAulagrupal) {
        this.oriOrientAulagrupal = oriOrientAulagrupal;
    }

    public Double getOriEntTrayectoriaVal() {
        return oriEntTrayectoriaVal;
    }

    public void setOriEntTrayectoriaVal(Double oriEntTrayectoriaVal) {
        this.oriEntTrayectoriaVal = oriEntTrayectoriaVal;
    }

    public BigDecimal getOriUbicPuntuacionVal() {
        return oriUbicPuntuacionVal;
    }

    public void setOriUbicPuntuacionVal(BigDecimal oriUbicPuntuacionVal) {
        this.oriUbicPuntuacionVal = oriUbicPuntuacionVal;
    }

    public String getOriOrientDespachosValidados() {
        return oriOrientDespachosValidados;
    }

    public void setOriOrientDespachosValidados(String oriOrientDespachosValidados) {
        this.oriOrientDespachosValidados = oriOrientDespachosValidados;
    }

    public String getOriOrientAulaGrupalValidada() {
        return oriOrientAulaGrupalValidada;
    }

    public void setOriOrientAulaGrupalValidada(String oriOrientAulaGrupalValidada) {
        this.oriOrientAulaGrupalValidada = oriOrientAulaGrupalValidada;
    }

    public BigDecimal getOriOrientValTray() {
        return oriOrientValTray;
    }

    public void setOriOrientValTray(BigDecimal oriOrientValTray) {
        this.oriOrientValTray = oriOrientValTray;
    }

    public BigDecimal getOriOrientValUbic() {
        return oriOrientValUbic;
    }

    public void setOriOrientValUbic(BigDecimal oriOrientValUbic) {
        this.oriOrientValUbic = oriOrientValUbic;
    }

    public Long getOriOrientValDespachos() {
        return oriOrientValDespachos;
    }

    public void setOriOrientValDespachos(Long oriOrientValDespachos) {
        this.oriOrientValDespachos = oriOrientValDespachos;
    }

    public Long getOriOrientValAulas() {
        return oriOrientValAulas;
    }

    public void setOriOrientValAulas(Long oriOrientValAulas) {
        this.oriOrientValAulas = oriOrientValAulas;
    }

    public BigDecimal getOriOrientPuntuacion() {
        return oriOrientPuntuacion;
    }

    public void setOriOrientPuntuacion(BigDecimal oriOrientPuntuacion) {
        this.oriOrientPuntuacion = oriOrientPuntuacion;
    }

    public String getOriOrientObservaciones() {
        return oriOrientObservaciones;
    }

    public void setOriOrientObservaciones(String oriOrientObservaciones) {
        this.oriOrientObservaciones = oriOrientObservaciones;
    }

    public String getOriOrientUbicaEspacioAdicional() {
        return oriOrientUbicaEspacioAdicional;
    }

    public void setOriOrientUbicaEspacioAdicional(String oriOrientUbicaEspacioAdicional) {
        this.oriOrientUbicaEspacioAdicional = oriOrientUbicaEspacioAdicional;
    }

    public String getOriOrientUbicaEspAdicioHerrBusqEmpleo() {
        return oriOrientUbicaEspAdicioHerrBusqEmpleo;
    }

    public void setOriOrientUbicaEspAdicioHerrBusqEmpleo(String oriOrientUbicaEspAdicioHerrBusqEmpleo) {
        this.oriOrientUbicaEspAdicioHerrBusqEmpleo = oriOrientUbicaEspAdicioHerrBusqEmpleo;
    }

    public String getOriOrientUbicaEspacioAdicionalVal() {
        return oriOrientUbicaEspacioAdicionalVal;
    }

    public void setOriOrientUbicaEspacioAdicionalVal(String oriOrientUbicaEspacioAdicionalVal) {
        this.oriOrientUbicaEspacioAdicionalVal = oriOrientUbicaEspacioAdicionalVal;
    }

    public String getOriOrientUbicaEspAdicioHerrBusqEmpleoVal() {
        return oriOrientUbicaEspAdicioHerrBusqEmpleoVal;
    }

    public void setOriOrientUbicaEspAdicioHerrBusqEmpleoVal(String oriOrientUbicaEspAdicioHerrBusqEmpleoVal) {
        this.oriOrientUbicaEspAdicioHerrBusqEmpleoVal = oriOrientUbicaEspAdicioHerrBusqEmpleoVal;
    }

    public Long getOriOrientVal1EspacioAdicional() {
        return oriOrientVal1EspacioAdicional;
    }

    public void setOriOrientVal1EspacioAdicional(Long oriOrientVal1EspacioAdicional) {
        this.oriOrientVal1EspacioAdicional = oriOrientVal1EspacioAdicional;
    }

    public Long getOriOrientValEspAdicioHerrBusqEmpleo() {
        return oriOrientValEspAdicioHerrBusqEmpleo;
    }

    public void setOriOrientValEspAdicioHerrBusqEmpleo(Long oriOrientValEspAdicioHerrBusqEmpleo) {
        this.oriOrientValEspAdicioHerrBusqEmpleo = oriOrientValEspAdicioHerrBusqEmpleo;
    }

    public Integer getOriCELocalPreviamenteAprobado() {
        return oriCELocalPreviamenteAprobado;
    }

    public void setOriCELocalPreviamenteAprobado(Integer oriCELocalPreviamenteAprobado) {
        this.oriCELocalPreviamenteAprobado = oriCELocalPreviamenteAprobado;
    }

    public Integer getOriCEMantenimientoRequisitosLPA() {
        return oriCEMantenimientoRequisitosLPA;
    }

    public void setOriCEMantenimientoRequisitosLPA(Integer oriCEMantenimientoRequisitosLPA) {
        this.oriCEMantenimientoRequisitosLPA = oriCEMantenimientoRequisitosLPA;
    }

    public Integer getOriCELocalPreviamenteAprobadoVAL() {
        return oriCELocalPreviamenteAprobadoVAL;
    }

    public void setOriCELocalPreviamenteAprobadoVAL(Integer oriCELocalPreviamenteAprobadoVAL) {
        this.oriCELocalPreviamenteAprobadoVAL = oriCELocalPreviamenteAprobadoVAL;
    }

    public Integer getOriCEMantenimientoRequisitosLPAVAL() {
        return oriCEMantenimientoRequisitosLPAVAL;
    }

    public void setOriCEMantenimientoRequisitosLPAVAL(Integer oriCEMantenimientoRequisitosLPAVAL) {
        this.oriCEMantenimientoRequisitosLPAVAL = oriCEMantenimientoRequisitosLPAVAL;
    }

    public Long getOriCELocalPreviamenteAprobadoValoracion() {
        return oriCELocalPreviamenteAprobadoValoracion;
    }

    public void setOriCELocalPreviamenteAprobadoValoracion(Long oriCELocalPreviamenteAprobadoValoracion) {
        this.oriCELocalPreviamenteAprobadoValoracion = oriCELocalPreviamenteAprobadoValoracion;
    }

    public Long getOriCEMantenimientoRequisitosLPAValoracion() {
        return oriCEMantenimientoRequisitosLPAValoracion;
    }

    public void setOriCEMantenimientoRequisitosLPAValoracion(Long oriCEMantenimientoRequisitosLPAValoracion) {
        this.oriCEMantenimientoRequisitosLPAValoracion = oriCEMantenimientoRequisitosLPAValoracion;
    }

    public String getDireccionNumero() {
        return direccionNumero;
    }

    public void setDireccionNumero(String direccionNumero) {
        this.direccionNumero = direccionNumero;
    }

    public String getDireccionPiso() {
        return direccionPiso;
    }

    public void setDireccionPiso(String direccionPiso) {
        this.direccionPiso = direccionPiso;
    }

    public String getDireccionLetra() {
        return direccionLetra;
    }

    public void setDireccionLetra(String direccionLetra) {
        this.direccionLetra = direccionLetra;
    }

    public Integer getOriEntPlanIgualdad() {
        return oriEntPlanIgualdad;
    }

    public void setOriEntPlanIgualdad(Integer oriEntPlanIgualdad) {
        this.oriEntPlanIgualdad = oriEntPlanIgualdad;
    }

    public Integer getOriEntCertCalidad() {
        return oriEntCertCalidad;
    }

    public void setOriEntCertCalidad(Integer oriEntCertCalidad) {
        this.oriEntCertCalidad = oriEntCertCalidad;
    }

    public Integer getOriEntPlanIgualdadVal() {
        return oriEntPlanIgualdadVal;
    }

    public void setOriEntPlanIgualdadVal(Integer oriEntPlanIgualdadVal) {
        this.oriEntPlanIgualdadVal = oriEntPlanIgualdadVal;
    }

    public Integer getOriEntCertCalidadVal() {
        return oriEntCertCalidadVal;
    }

    public void setOriEntCertCalidadVal(Integer oriEntCertCalidadVal) {
        this.oriEntCertCalidadVal = oriEntCertCalidadVal;
    }

    public Long getOriEntPlanIgualdadValoracion() {
        return oriEntPlanIgualdadValoracion;
    }

    public void setOriEntPlanIgualdadValoracion(Long oriEntPlanIgualdadValoracion) {
        this.oriEntPlanIgualdadValoracion = oriEntPlanIgualdadValoracion;
    }

    public Long getOriEntCertCalidadValoracion() {
        return oriEntCertCalidadValoracion;
    }

    public void setOriEntCertCalidadValoracion(Long oriEntCertCalidadValoracion) {
        this.oriEntCertCalidadValoracion = oriEntCertCalidadValoracion;
    }

    public Integer getEntSujetaDerPubl() {
        return entSujetaDerPubl;
    }

    public void setEntSujetaDerPubl(Integer entSujetaDerPubl) {
        this.entSujetaDerPubl = entSujetaDerPubl;
    }

    public Integer getEntSujetaDerPublVal() {
        return entSujetaDerPublVal;
    }

    public void setEntSujetaDerPublVal(Integer entSujetaDerPublVal) {
        this.entSujetaDerPublVal = entSujetaDerPublVal;
    }

    public Integer getCompIgualdadOpcion() {
        return compIgualdadOpcion;
    }

    public void setCompIgualdadOpcion(Integer compIgualdadOpcion) {
        this.compIgualdadOpcion = compIgualdadOpcion;
    }

    public String getCompIgualdadOpcionLiteral() {
        return compIgualdadOpcionLiteral;
    }

    public void setCompIgualdadOpcionLiteral(String compIgualdadOpcionLiteral) {
        this.compIgualdadOpcionLiteral = compIgualdadOpcionLiteral;
    }

    public Integer getCompIgualdadOpcionVal() {
        return compIgualdadOpcionVal;
    }

    public void setCompIgualdadOpcionVal(Integer compIgualdadOpcionVal) {
        this.compIgualdadOpcionVal = compIgualdadOpcionVal;
    }

    public String getCompIgualdadOpcionValLiteral() {
        return compIgualdadOpcionValLiteral;
    }

    public void setCompIgualdadOpcionValLiteral(String compIgualdadOpcionValLiteral) {
        this.compIgualdadOpcionValLiteral = compIgualdadOpcionValLiteral;
    }

    @Override
    public String toString() {
        return "OriUbicValoracionVO{" +
                "oriOrientUbicCod=" + oriOrientUbicCod +
                ", descProvincia='" + descProvincia + '\'' +
                ", descAmbito='" + descAmbito + '\'' +
                ", descMunicipio='" + descMunicipio + '\'' +
                ", direccion='" + direccion + '\'' +
                ", direccionNumero='" + direccionNumero + '\'' +
                ", direccionPiso='" + direccionPiso + '\'' +
                ", direccionLetra='" + direccionLetra + '\'' +
                ", codPostal='" + codPostal + '\'' +
                ", horasSolic='" + horasSolic + '\'' +
                ", oriEntTrayectoria=" + oriEntTrayectoria +
                ", oriUbicPuntuacion=" + oriUbicPuntuacion +
                ", oriOrientDespachos='" + oriOrientDespachos + '\'' +
                ", oriOrientAulagrupal='" + oriOrientAulagrupal + '\'' +
                ", oriOrientUbicaEspacioAdicional='" + oriOrientUbicaEspacioAdicional + '\'' +
                ", oriOrientUbicaEspAdicioHerrBusqEmpleo='" + oriOrientUbicaEspAdicioHerrBusqEmpleo + '\'' +
                ", oriCELocalPreviamenteAprobado=" + oriCELocalPreviamenteAprobado +
                ", oriCEMantenimientoRequisitosLPA=" + oriCEMantenimientoRequisitosLPA +
                ", oriEntPlanIgualdad=" + oriEntPlanIgualdad +
                ", oriEntCertCalidad=" + oriEntCertCalidad +
                ", oriEntTrayectoriaVal=" + oriEntTrayectoriaVal +
                ", oriUbicPuntuacionVal=" + oriUbicPuntuacionVal +
                ", oriOrientDespachosValidados='" + oriOrientDespachosValidados + '\'' +
                ", oriOrientAulaGrupalValidada='" + oriOrientAulaGrupalValidada + '\'' +
                ", oriOrientUbicaEspacioAdicionalVal='" + oriOrientUbicaEspacioAdicionalVal + '\'' +
                ", oriOrientUbicaEspAdicioHerrBusqEmpleoVal='" + oriOrientUbicaEspAdicioHerrBusqEmpleoVal + '\'' +
                ", oriCELocalPreviamenteAprobadoVAL=" + oriCELocalPreviamenteAprobadoVAL +
                ", oriCEMantenimientoRequisitosLPAVAL=" + oriCEMantenimientoRequisitosLPAVAL +
                ", oriEntPlanIgualdadVal=" + oriEntPlanIgualdadVal +
                ", oriEntCertCalidadVal=" + oriEntCertCalidadVal +
                ", oriOrientValTray=" + oriOrientValTray +
                ", oriOrientValUbic=" + oriOrientValUbic +
                ", oriOrientValDespachos=" + oriOrientValDespachos +
                ", oriOrientValAulas=" + oriOrientValAulas +
                ", oriOrientVal1EspacioAdicional=" + oriOrientVal1EspacioAdicional +
                ", oriOrientValEspAdicioHerrBusqEmpleo=" + oriOrientValEspAdicioHerrBusqEmpleo +
                ", oriCELocalPreviamenteAprobadoValoracion=" + oriCELocalPreviamenteAprobadoValoracion +
                ", oriCEMantenimientoRequisitosLPAValoracion=" + oriCEMantenimientoRequisitosLPAValoracion +
                ", oriEntPlanIgualdadValoracion=" + oriEntPlanIgualdadValoracion +
                ", oriEntCertCalidadValoracion=" + oriEntCertCalidadValoracion +
                ", oriOrientPuntuacion=" + oriOrientPuntuacion +
                ", oriOrientObservaciones='" + oriOrientObservaciones + '\'' +
                ", entSujetaDerPubl=" + entSujetaDerPubl +
                ", entSujetaDerPublVal=" + entSujetaDerPublVal +
                ", compIgualdadOpcion=" + compIgualdadOpcion +
                ", compIgualdadOpcionLiteral='" + compIgualdadOpcionLiteral + '\'' +
                ", compIgualdadOpcionVal=" + compIgualdadOpcionVal +
                ", compIgualdadOpcionValLiteral='" + compIgualdadOpcionValLiteral + '\'' +
                '}';
    }
}
