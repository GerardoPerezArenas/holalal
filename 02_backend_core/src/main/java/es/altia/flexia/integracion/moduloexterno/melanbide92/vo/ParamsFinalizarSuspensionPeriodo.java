package es.altia.flexia.integracion.moduloexterno.melanbide92.vo;

import java.math.BigDecimal;

/**
 *
 * @author kigonzalez
 */
public class ParamsFinalizarSuspensionPeriodo {

    private String zorUsUsuario;
    private String zorUsPassword;
    private String area;
    private BigDecimal zorLiNumLiquidacion;

    public String getZorUsUsuario() {
        return zorUsUsuario;
    }

    public void setZorUsUsuario(String zorUsUsuario) {
        this.zorUsUsuario = zorUsUsuario;
    }

    public String getZorUsPassword() {
        return zorUsPassword;
    }

    public void setZorUsPassword(String zorUsPassword) {
        this.zorUsPassword = zorUsPassword;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public BigDecimal getZorLiNumLiquidacion() {
        return zorLiNumLiquidacion;
    }

    public void setZorLiNumLiquidacion(BigDecimal zorLiNumLiquidacion) {
        this.zorLiNumLiquidacion = zorLiNumLiquidacion;
    }

    @Override
    public String toString() {
        return "ParamsSuspenderPeriodoPago{\n" + "zorUsUsuario=" + zorUsUsuario + ", zorUsPassword=" + zorUsPassword + ", area=" + area + ",\n zorLiNumLiquidacion=" + zorLiNumLiquidacion + '}';
    }

}
