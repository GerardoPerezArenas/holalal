/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide32.vo.centroempleo;


/**
 *
 * @author santiagoc
 */
public class AmbitoCentroEmpleoVO 
{
    private Integer oriAmbCod;
    private Integer oriAmbTerHis;
    private String oriAmbAmbito;
    private Integer oriAmbAnoconv;
    private Integer oriAmbCe;
    private Integer oriAmbCeEspecial;
    private Integer OriAmbDistr;
    
    public AmbitoCentroEmpleoVO()
    {
        
    }

    public Integer getOriAmbCod() {
        return oriAmbCod;
    }

    public void setOriAmbCod(Integer oriAmbCod) {
        this.oriAmbCod = oriAmbCod;
    }

    public Integer getOriAmbTerHis() {
        return oriAmbTerHis;
    }

    public void setOriAmbTerHis(Integer oriAmbTerHis) {
        this.oriAmbTerHis = oriAmbTerHis;
    }

    public String getOriAmbAmbito() {
        return oriAmbAmbito;
    }

    public void setOriAmbAmbito(String oriAmbAmbito) {
        this.oriAmbAmbito = oriAmbAmbito;
    }

    public Integer getOriAmbAnoconv() {
        return oriAmbAnoconv;
    }

    public void setOriAmbAnoconv(Integer oriAmbAnoconv) {
        this.oriAmbAnoconv = oriAmbAnoconv;
    }

    public Integer getOriAmbCe() {
        return oriAmbCe;
    }

    public void setOriAmbCe(Integer oriAmbCe) {
        this.oriAmbCe = oriAmbCe;
    }

    public Integer getOriAmbCeEspecial() {
        return oriAmbCeEspecial;
    }

    public void setOriAmbCeEspecial(Integer oriAmbCeEspecial) {
        this.oriAmbCeEspecial = oriAmbCeEspecial;
    }

    public Integer getOriAmbDistr() {
        return OriAmbDistr;
    }

    public void setOriAmbDistr(Integer OriAmbDistr) {
        this.OriAmbDistr = OriAmbDistr;
    }

    @Override
    public String toString() {
        return "AmbitoCentroEmpleoVO{" + "oriAmbCod=" + oriAmbCod + ", oriAmbTerHis=" + oriAmbTerHis + ", oriAmbAmbito=" + oriAmbAmbito + ", oriAmbAnoconv=" + oriAmbAnoconv + ", oriAmbCe=" + oriAmbCe + ", oriAmbCeEspecial=" + oriAmbCeEspecial + ", OriAmbDistr=" + OriAmbDistr + '}';
    }

}
