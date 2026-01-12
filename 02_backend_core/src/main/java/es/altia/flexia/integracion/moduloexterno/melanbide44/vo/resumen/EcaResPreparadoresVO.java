/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide44.vo.resumen;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author santiagoc
 */
public class EcaResPreparadoresVO 
{   
    private Long ecaResPreparadoresCod;
    private String ecaNumexp;
    private Integer ecaJusPreparadoresCod;
    private String nif;
    private String nombre;
    private BigDecimal gastosSalarialesSolicitados;
    private BigDecimal gastosSalarialesConcedidos;
    private BigDecimal gastosSalarialesJustificados;
    private BigDecimal importeSeguimientos;
    private BigDecimal importeInsConcedido;
    private BigDecimal importeInsJustificadas;
    private BigDecimal importeInserciones;
    private BigDecimal importeSegInserciones;
    private BigDecimal aPagar;
    private Date fecSysdate;

    public EcaResPreparadoresVO()
    {

    }

    public Long getEcaResPreparadoresCod() {
        return ecaResPreparadoresCod;
    }

    public void setEcaResPreparadoresCod(Long ecaResPreparadoresCod) {
        this.ecaResPreparadoresCod = ecaResPreparadoresCod;
    }

    public String getEcaNumexp() {
        return ecaNumexp;
    }

    public void setEcaNumexp(String ecaNumexp) {
        this.ecaNumexp = ecaNumexp;
    }

    public Integer getEcaJusPreparadoresCod() {
        return ecaJusPreparadoresCod;
    }

    public void setEcaJusPreparadoresCod(Integer ecaJusPreparadoresCod) {
        this.ecaJusPreparadoresCod = ecaJusPreparadoresCod;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getGastosSalarialesSolicitados() {
        return gastosSalarialesSolicitados;
    }

    public void setGastosSalarialesSolicitados(BigDecimal gastosSalarialesSolicitados) {
        this.gastosSalarialesSolicitados = gastosSalarialesSolicitados;
    }

    public BigDecimal getGastosSalarialesConcedidos() {
        return gastosSalarialesConcedidos;
    }

    public void setGastosSalarialesConcedidos(BigDecimal gastosSalarialesConcedidos) {
        this.gastosSalarialesConcedidos = gastosSalarialesConcedidos;
    }

    public BigDecimal getGastosSalarialesJustificados() {
        return gastosSalarialesJustificados;
    }

    public void setGastosSalarialesJustificados(BigDecimal gastosSalarialesJustificados) {
        this.gastosSalarialesJustificados = gastosSalarialesJustificados;
    }

    public BigDecimal getImporteSeguimientos() {
        return importeSeguimientos;
    }

    public void setImporteSeguimientos(BigDecimal importeSeguimientos) {
        this.importeSeguimientos = importeSeguimientos;
    }

    public BigDecimal getImporteInsConcedido() {
        return importeInsConcedido;
    }

    public void setImporteInsConcedido(BigDecimal importeInsConcedido) {
        this.importeInsConcedido = importeInsConcedido;
    }

    public BigDecimal getImporteInsJustificadas() {
        return importeInsJustificadas;
    }

    public void setImporteInsJustificadas(BigDecimal importeInsJustificadas) {
        this.importeInsJustificadas = importeInsJustificadas;
    }

    public BigDecimal getImporteInserciones() {
        return importeInserciones;
    }

    public void setImporteInserciones(BigDecimal importeInserciones) {
        this.importeInserciones = importeInserciones;
    }

    public BigDecimal getImporteSegInserciones() {
        return importeSegInserciones;
    }

    public void setImporteSegInserciones(BigDecimal importeSegInserciones) {
        this.importeSegInserciones = importeSegInserciones;
    }

    public BigDecimal getaPagar() {
        return aPagar;
    }

    public void setaPagar(BigDecimal aPagar) {
        this.aPagar = aPagar;
    }

    public Date getFecSysdate() {
        return fecSysdate;
    }

    public void setFecSysdate(Date fecSysdate) {
        this.fecSysdate = fecSysdate;
    }
}
