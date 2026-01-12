package es.altia.flexia.integracion.moduloexterno.melanbide82.vo;

import java.sql.Date;

public class FilaContratacionVO {

    //MELANBIDE82_CONTRATACION 
    private Integer id;
    private String numExp;
    //   private Integer numPuesto;

    private Integer prioridad; // es en numero puesto en INI y FIN
    private String denomPuesto;
    private String nivelCualificacion;
    private String descNivelcualificacion;
    private String modContrato;
    private Integer durContrato;
    private String descDurContrato;
    private String grupoCotiz;
    private String descGrupoCotiz;
    private Double costesalarial;
    private Double subvsolicitada;

    //MELANBIDE82_CONTRATACION_INI
    private Integer idInicio;
    private String municipioInicio;
    private String nombreInicio;
    private String apellido1Inicio;
    private String apellido2Inicio;
    private String dninieInicio;
    private String cv2;
    private Date fechaCv2;
    private String demanda2;
    private Date fechaDemanda2;
    private Date fechanacimientoInicio;
    private String sexoInicio;
    private String descSexoInicio;
    private String nivelcualificacionInicio;
    private String descNivelcualificacionInicio;
    private String puestotrabajoInicio;
    private String nofertaInicio;
    private String grupocotizInicio;
    private String descGrupocotizInicio;
    private Integer durcontratoInicio;
    private String descDurContratoInicio;
    private Date fechainicioInicio;
    private Integer edadInicio;
    private Double retribucionbrutaInicio;
    private String sistGrantiaJuveIni;
    private String descSistGrantiaJuveIni;

    //MELANBIDE82_CONTRATACION_FIN
    private Integer idFin;
    private String municipioFin;
    private String nombreFin;
    private String apellido1Fin;
    private String apellido2Fin;
    private String dninieFin;
    private String cv3;
    private Date fechaCv3;
    private String demanda3;
    private Date fechaDemanda3;
    private String sexoFin;
    private String descSexoFin;
    private String grupocotizFin;
    private String descGrupocotizFin;
    private Integer durcontratoFin;
    private String descDurContratoFin;
    private Date fechainicioFin;
    private Date fechafinFin;
    private Double retribucionbrutaFin;
    private Double costesalarialFin;
    private Double costesssFin;
    private Double indemfincontratoFin;
    private Double costetotalrealFin;
    private Double subvconcedidalanFin;
    private String sistGrantiaJuveFin;
    private String descSistGrantiaJuveFin;

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
// El nº de puesto se corresponde con PRIORIDAD de la tabla contrataciones
//    public Integer getNumPuesto() {
//        return numPuesto;
//    }
//
//    public void setNumPuesto(Integer numPuesto) {
//        this.numPuesto = numPuesto;
//    }

    public Integer getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(Integer prioridad) {
        this.prioridad = prioridad;
    }

    public String getDenomPuesto() {
        return denomPuesto;
    }

    public void setDenomPuesto(String denomPuesto) {
        this.denomPuesto = denomPuesto;
    }

    public String getNivelCualificacion() {
        return nivelCualificacion;
    }

    public void setNivelCualificacion(String nivelCualificacion) {
        this.nivelCualificacion = nivelCualificacion;
    }

    public String getDescNivelcualificacion() {
        return descNivelcualificacion;
    }

    public void setDescNivelcualificacion(String descNivelcualificacion) {
        this.descNivelcualificacion = descNivelcualificacion;
    }

    public String getModContrato() {
        return modContrato;
    }

    public void setModContrato(String modContrato) {
        this.modContrato = modContrato;
    }

    public Integer getDurContrato() {
        return durContrato;
    }

    public void setDurContrato(Integer durContrato) {
        this.durContrato = durContrato;
    }

    public String getDescDurContrato() {
        return descDurContrato;
    }

    public void setDescDurContrato(String descDurContrato) {
        this.descDurContrato = descDurContrato;
    }

    public String getGrupoCotiz() {
        return grupoCotiz;
    }

    public void setGrupoCotiz(String grupoCotiz) {
        this.grupoCotiz = grupoCotiz;
    }

    public String getDescGrupoCotiz() {
        return descGrupoCotiz;
    }

    public void setDescGrupoCotiz(String descGrupoCotiz) {
        this.descGrupoCotiz = descGrupoCotiz;
    }

    public Double getCostesalarial() {
        return costesalarial;
    }

    public void setCostesalarial(Double costesalarial) {
        this.costesalarial = costesalarial;
    }

    public Double getSubvsolicitada() {
        return subvsolicitada;
    }

    public void setSubvsolicitada(Double subvsolicitada) {
        this.subvsolicitada = subvsolicitada;
    }

    public String getMunicipioInicio() {
        return municipioInicio;
    }

    public void setMunicipioInicio(String municipioInicio) {
        this.municipioInicio = municipioInicio;
    }

    public String getNombreInicio() {
        return nombreInicio;
    }

    public void setNombreInicio(String nombreInicio) {
        this.nombreInicio = nombreInicio;
    }

    public String getApellido1Inicio() {
        return apellido1Inicio;
    }

    public void setApellido1Inicio(String apellido1Inicio) {
        this.apellido1Inicio = apellido1Inicio;
    }

    public String getApellido2Inicio() {
        return apellido2Inicio;
    }

    public void setApellido2Inicio(String apellido2Inicio) {
        this.apellido2Inicio = apellido2Inicio;
    }

    public String getDninieInicio() {
        return dninieInicio;
    }

    public void setDninieInicio(String dninieInicio) {
        this.dninieInicio = dninieInicio;
    }

    public Date getFechanacimientoInicio() {
        return fechanacimientoInicio;
    }

    public void setFechanacimientoInicio(Date fechanacimientoInicio) {
        this.fechanacimientoInicio = fechanacimientoInicio;
    }

    public String getSexoInicio() {
        return sexoInicio;
    }

    public void setSexoInicio(String sexoInicio) {
        this.sexoInicio = sexoInicio;
    }

    public String getDescSexoInicio() {
        return descSexoInicio;
    }

    public void setDescSexoInicio(String descSexoInicio) {
        this.descSexoInicio = descSexoInicio;
    }

    public String getNivelcualificacionInicio() {
        return nivelcualificacionInicio;
    }

    public void setNivelcualificacionInicio(String nivelcualificacionInicio) {
        this.nivelcualificacionInicio = nivelcualificacionInicio;
    }

    public String getDescNivelcualificacionInicio() {
        return descNivelcualificacionInicio;
    }

    public void setDescNivelcualificacionInicio(String descNivelcualificacionInicio) {
        this.descNivelcualificacionInicio = descNivelcualificacionInicio;
    }

    public String getPuestotrabajoInicio() {
        return puestotrabajoInicio;
    }

    public void setPuestotrabajoInicio(String puestotrabajoInicio) {
        this.puestotrabajoInicio = puestotrabajoInicio;
    }

    public String getNofertaInicio() {
        return nofertaInicio;
    }

    public void setNofertaInicio(String nofertaInicio) {
        this.nofertaInicio = nofertaInicio;
    }

    public String getGrupocotizInicio() {
        return grupocotizInicio;
    }

    public void setGrupocotizInicio(String grupocotizInicio) {
        this.grupocotizInicio = grupocotizInicio;
    }

    public String getDescGrupocotizInicio() {
        return descGrupocotizInicio;
    }

    public void setDescGrupocotizInicio(String descGrupocotizInicio) {
        this.descGrupocotizInicio = descGrupocotizInicio;
    }

    public Integer getDurcontratoInicio() {
        return durcontratoInicio;
    }

    public void setDurcontratoInicio(Integer durcontratoInicio) {
        this.durcontratoInicio = durcontratoInicio;
    }

    public String getDescDurContratoInicio() {
        return descDurContratoInicio;
    }

    public void setDescDurContratoInicio(String descDurContratoInicio) {
        this.descDurContratoInicio = descDurContratoInicio;
    }

    public Date getFechainicioInicio() {
        return fechainicioInicio;
    }

    public void setFechainicioInicio(Date fechainicioInicio) {
        this.fechainicioInicio = fechainicioInicio;
    }

    public Integer getEdadInicio() {
        return edadInicio;
    }

    public void setEdadInicio(Integer edadInicio) {
        this.edadInicio = edadInicio;
    }

    public Double getRetribucionbrutaInicio() {
        return retribucionbrutaInicio;
    }

    public void setRetribucionbrutaInicio(Double retribucionbrutaInicio) {
        this.retribucionbrutaInicio = retribucionbrutaInicio;
    }

    public String getMunicipioFin() {
        return municipioFin;
    }

    public void setMunicipioFin(String municipioFin) {
        this.municipioFin = municipioFin;
    }

    public String getNombreFin() {
        return nombreFin;
    }

    public void setNombreFin(String nombreFin) {
        this.nombreFin = nombreFin;
    }

    public String getApellido1Fin() {
        return apellido1Fin;
    }

    public void setApellido1Fin(String apellido1Fin) {
        this.apellido1Fin = apellido1Fin;
    }

    public String getApellido2Fin() {
        return apellido2Fin;
    }

    public void setApellido2Fin(String apellido2Fin) {
        this.apellido2Fin = apellido2Fin;
    }

    public String getDninieFin() {
        return dninieFin;
    }

    public void setDninieFin(String dninieFin) {
        this.dninieFin = dninieFin;
    }

    public String getSexoFin() {
        return sexoFin;
    }

    public void setSexoFin(String sexoFin) {
        this.sexoFin = sexoFin;
    }

    public String getDescSexoFin() {
        return descSexoFin;
    }

    public void setDescSexoFin(String descSexoFin) {
        this.descSexoFin = descSexoFin;
    }

    public String getGrupocotizFin() {
        return grupocotizFin;
    }

    public void setGrupocotizFin(String grupocotizFin) {
        this.grupocotizFin = grupocotizFin;
    }

    public String getDescGrupocotizFin() {
        return descGrupocotizFin;
    }

    public void setDescGrupocotizFin(String descGrupocotizFin) {
        this.descGrupocotizFin = descGrupocotizFin;
    }

    public Integer getDurcontratoFin() {
        return durcontratoFin;
    }

    public void setDurcontratoFin(Integer durcontratoFin) {
        this.durcontratoFin = durcontratoFin;
    }

    public String getDescDurContratoFin() {
        return descDurContratoFin;
    }

    public void setDescDurContratoFin(String descDurContratoFin) {
        this.descDurContratoFin = descDurContratoFin;
    }

    public Date getFechainicioFin() {
        return fechainicioFin;
    }

    public void setFechainicioFin(Date fechainicioFin) {
        this.fechainicioFin = fechainicioFin;
    }

    public Date getFechafinFin() {
        return fechafinFin;
    }

    public void setFechafinFin(Date fechafinFin) {
        this.fechafinFin = fechafinFin;
    }

    public Double getRetribucionbrutaFin() {
        return retribucionbrutaFin;
    }

    public void setRetribucionbrutaFin(Double retribucionbrutaFin) {
        this.retribucionbrutaFin = retribucionbrutaFin;
    }

    public Double getCostesalarialFin() {
        return costesalarialFin;
    }

    public void setCostesalarialFin(Double costesalarialFin) {
        this.costesalarialFin = costesalarialFin;
    }

    public Double getCostesssFin() {
        return costesssFin;
    }

    public void setCostesssFin(Double costesssFin) {
        this.costesssFin = costesssFin;
    }

    public Double getIndemfincontratoFin() {
        return indemfincontratoFin;
    }

    public void setIndemfincontratoFin(Double indemfincontratoFin) {
        this.indemfincontratoFin = indemfincontratoFin;
    }

    public Double getCostetotalrealFin() {
        return costetotalrealFin;
    }

    public void setCostetotalrealFin(Double costetotalrealFin) {
        this.costetotalrealFin = costetotalrealFin;
    }

    public Double getSubvconcedidalanFin() {
        return subvconcedidalanFin;
    }

    public void setSubvconcedidalanFin(Double subvconcedidalanFin) {
        this.subvconcedidalanFin = subvconcedidalanFin;
    }

    public String getCv2() {
        return cv2;
    }

    public void setCv2(String cv2) {
        this.cv2 = cv2;
    }

    public Date getFechaCv2() {
        return fechaCv2;
    }

    public void setFechaCv2(Date fechaCv2) {
        this.fechaCv2 = fechaCv2;
    }

    public String getDemanda2() {
        return demanda2;
    }

    public void setDemanda2(String demanda2) {
        this.demanda2 = demanda2;
    }

    public Date getFechaDemanda2() {
        return fechaDemanda2;
    }

    public void setFechaDemanda2(Date fechaDemanda2) {
        this.fechaDemanda2 = fechaDemanda2;
    }

    public String getCv3() {
        return cv3;
    }

    public void setCv3(String cv3) {
        this.cv3 = cv3;
    }

    public Date getFechaCv3() {
        return fechaCv3;
    }

    public void setFechaCv3(Date fechaCv3) {
        this.fechaCv3 = fechaCv3;
    }

    public String getDemanda3() {
        return demanda3;
    }

    public void setDemanda3(String demanda3) {
        this.demanda3 = demanda3;
    }

    public Date getFechaDemanda3() {
        return fechaDemanda3;
    }

    public void setFechaDemanda3(Date fechaDemanda3) {
        this.fechaDemanda3 = fechaDemanda3;
    }

    public String getSistGrantiaJuveIni() {
        return sistGrantiaJuveIni;
    }

    public void setSistGrantiaJuveIni(String sistGrantiaJuveIni) {
        this.sistGrantiaJuveIni = sistGrantiaJuveIni;
    }

    public String getSistGrantiaJuveFin() {
        return sistGrantiaJuveFin;
    }

    public void setSistGrantiaJuveFin(String sistGrantiaJuveFin) {
        this.sistGrantiaJuveFin = sistGrantiaJuveFin;
    }

    public String getDescSistGrantiaJuveIni() {
        return descSistGrantiaJuveIni;
    }

    public void setDescSistGrantiaJuveIni(String descSistGrantiaJuveIni) {
        this.descSistGrantiaJuveIni = descSistGrantiaJuveIni;
    }

    public String getDescSistGrantiaJuveFin() {
        return descSistGrantiaJuveFin;
    }

    public void setDescSistGrantiaJuveFin(String descSistGrantiaJuveFin) {
        this.descSistGrantiaJuveFin = descSistGrantiaJuveFin;
    }

    public Integer getIdInicio() {
        return idInicio;
    }

    public void setIdInicio(Integer idInicio) {
        this.idInicio = idInicio;
    }

    public Integer getIdFin() {
        return idFin;
    }

    public void setIdFin(Integer idFin) {
        this.idFin = idFin;
    }

    
}
