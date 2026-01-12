/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide64.vo;

/**
 *
 * @author santiagoc
 */
public class Expediente
{
    private String expPro;
    private Integer expEje;
    private String expNum;
    private Integer expMun;
    private String expObs;
    private String expAsu;
    private Integer uorRegistro;
    private Integer exrDep;
    private Integer resTdo;
    private String resNtr;
    private String resAut;
    private int expUsu;
    private int uorTramiteInicio;
    
    public Expediente()
    {
        
    }

    public String getExpPro() {
        return expPro;
    }

    public void setExpPro(String expPro) {
        this.expPro = expPro;
    }

    public Integer getExpEje() {
        return expEje;
    }

    public void setExpEje(Integer expEje) {
        this.expEje = expEje;
    }

    public String getExpNum() {
        return expNum;
    }

    public void setExpNum(String expNum) {
        this.expNum = expNum;
    }

    public Integer getExpMun() {
        return expMun;
    }

    public void setExpMun(Integer expMun) {
        this.expMun = expMun;
    }

    public String getExpObs() {
        return expObs;
    }

    public void setExpObs(String expObs) {
        this.expObs = expObs;
    }

    public String getExpAsu() {
        return expAsu;
    }

    public void setExpAsu(String expAsu) {
        this.expAsu = expAsu;
    }

    public Integer getUorRegistro() {
        return uorRegistro;
    }

    public void setUorRegistro(Integer uorRegistro) {
        this.uorRegistro = uorRegistro;
    }

    public Integer getExrDep() {
        return exrDep;
    }

    public void setExrDep(Integer exrDep) {
        this.exrDep = exrDep;
    }

    public Integer getResTdo() {
        return resTdo;
    }

    public void setResTdo(Integer resTdo) {
        this.resTdo = resTdo;
    }

    public String getResNtr() {
        return resNtr;
    }

    public void setResNtr(String resNtr) {
        this.resNtr = resNtr;
    }

    public String getResAut() {
        return resAut;
    }

    public void setResAut(String resAut) {
        this.resAut = resAut;
    }

    /**
     * @return the expUsu
     */
    public int getExpUsu() {
        return expUsu;
    }

    /**
     * @param expUsu the expUsu to set
     */
    public void setExpUsu(int expUsu) {
        this.expUsu = expUsu;
    }

    /**
     * @return the uorTramiteInicio
     */
    public int getUorTramiteInicio() {
        return uorTramiteInicio;
    }

    /**
     * @param uorTramiteInicio the uorTramiteInicio to set
     */
    public void setUorTramiteInicio(int uorTramiteInicio) {
        this.uorTramiteInicio = uorTramiteInicio;
    }
}
