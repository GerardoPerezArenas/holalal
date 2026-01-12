/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide35.vo.solicitud;

import java.math.BigDecimal;

/**
 *
 * @author SantiagoC
 */
public class EcaSolValoracionVO 
{
    private Integer solValoracionCod;
    private Integer experienciaNum;
    private BigDecimal experienciaVal;
    private BigDecimal insMujeresNum;
    private BigDecimal insMujeresVal;
    private Integer sensibilidadNum;
    private BigDecimal sensibilidadVal;
    private BigDecimal total;
    private Integer solicitud;
    
    public EcaSolValoracionVO()
    {
        
    }

    public Integer getSolValoracionCod() {
        return solValoracionCod;
    }

    public void setSolValoracionCod(Integer solValoracionCod) {
        this.solValoracionCod = solValoracionCod;
    }

    public Integer getExperienciaNum() {
        return experienciaNum;
    }

    public void setExperienciaNum(Integer experienciaNum) {
        this.experienciaNum = experienciaNum;
    }

    public BigDecimal getExperienciaVal() {
        return experienciaVal;
    }

    public void setExperienciaVal(BigDecimal experienciaVal) {
        this.experienciaVal = experienciaVal;
    }

    public BigDecimal getInsMujeresNum() {
        return insMujeresNum;
    }

    public void setInsMujeresNum(BigDecimal insMujeresNum) {
        this.insMujeresNum = insMujeresNum;
    }

    public BigDecimal getInsMujeresVal() {
        return insMujeresVal;
    }

    public void setInsMujeresVal(BigDecimal insMujeresVal) {
        this.insMujeresVal = insMujeresVal;
    }

    public Integer getSensibilidadNum() {
        return sensibilidadNum;
    }

    public void setSensibilidadNum(Integer sensibilidadNum) {
        this.sensibilidadNum = sensibilidadNum;
    }

    public BigDecimal getSensibilidadVal() {
        return sensibilidadVal;
    }

    public void setSensibilidadVal(BigDecimal sensibilidadVal) {
        this.sensibilidadVal = sensibilidadVal;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Integer getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(Integer solicitud) {
        this.solicitud = solicitud;
    }
}
