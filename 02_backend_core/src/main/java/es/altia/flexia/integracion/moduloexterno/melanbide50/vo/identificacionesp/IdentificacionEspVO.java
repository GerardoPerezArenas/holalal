/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.flexia.integracion.moduloexterno.melanbide50.vo.identificacionesp;

import java.math.BigDecimal;

/**
 *
 * @author davidg
 */
public class IdentificacionEspVO {
    
        
    private Integer id;   //ID
    private Integer idEspSol;   //ID_ESPSOL
    private String numExp;              //IDE_NUM
    private String codCp;               //IDE_CODESP
    private String denomEsp;    //IDE_DENESP
    private BigDecimal horas;            //IDE_HORAS
    private BigDecimal alumnos;        //IDE_ALUM
    private Integer certPro;       //IDE_CERTP
    private String realDecRegu;   //IDE_RDER
    private String boeFecPub;   //IDE_BOEFP
    private String descripAdapt;  //IDE_DESADAP
    private String observAdapt;  //IDE_OBSADAP
    
    
    public IdentificacionEspVO()
    {
        
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getIdEspSol() {
        return idEspSol;
    }

    public void setIdEspSol(Integer idEspSol) {
        this.idEspSol = idEspSol;
    }

    public String getNumExp() {
        return numExp;
    }
    
    public void setNumExp(String numExp) {
        this.numExp = numExp;
    }
    
    public String getCodCp() {
        return codCp;
    }
    
    public void setCodCp(String codCp) {
        this.codCp = codCp;
    }
    
    public String getDenomEsp() {
        return denomEsp;
    }
    
    public void setDenomEsp(String denomEsp) {
        this.denomEsp = denomEsp;
    }
    
    
    public BigDecimal getHoras() {
        return horas;
    }
    
    public void setHoras(BigDecimal horas) {
        this.horas = horas;
    }
    
    public BigDecimal getAlumnos() {
        return alumnos;
    }
    
    public void setAlumnos(BigDecimal alumnos) {
        this.alumnos = alumnos;
    }
    
    public Integer getCertPro() {
        return certPro;
    }
    
    public void setCertPro(Integer certPro) {
        this.certPro = certPro;
    }
    
    public String getRealDecRegu() {
        return realDecRegu;
    }
    
    public void setRealDecRegu(String realDecRegu) {
        this.realDecRegu = realDecRegu;
    }
    
    public String getBoeFecPub() {
        return boeFecPub;
    }
    
    public void setBoeFecPub(String boeFecPub) {
        this.boeFecPub = boeFecPub;
    }
    
    public String getDescripAdapt() {
        return descripAdapt;
    }
    
    public void setDescripAdapt(String descripAdapt) {
        this.descripAdapt = descripAdapt;
    }
    
    public String getObservAdapt() {
        return observAdapt;
    }
    
    public void setObservAdapt(String observAdapt) {
        this.observAdapt = observAdapt;
    }
    
}
