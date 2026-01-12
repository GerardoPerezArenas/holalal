package es.altia.flexia.integracion.moduloexterno.melanbide92.vo;

import java.math.BigDecimal;

/**
 *
 * @author kigonzalez
 */
public class ParamsSuspenderPeriodoPago {

    private String zorUsUsuario;
    private String zorUsPassword;
    private String area;
    private BigDecimal zorLiNumLiquidacion;
    private String zorLiFechaSuspension;

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

    public String getZorLiFechaSuspension() {
        return zorLiFechaSuspension;
    }

    public void setZorLiFechaSuspension(String zorLiFechaSuspension) {
        this.zorLiFechaSuspension = zorLiFechaSuspension;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ParamsSuspenderPeriodoPago{");
        sb.append("\n zorUsUsuario=").append(zorUsUsuario);
        sb.append(", zorUsPassword=").append(zorUsPassword);
        sb.append(", area=").append(area);
        sb.append(",\n zorLiNumLiquidacion=").append(zorLiNumLiquidacion);
        sb.append(",\n zorLiFechaSuspension=").append(zorLiFechaSuspension);
        sb.append("\n }");
        return sb.toString();
    }

}
