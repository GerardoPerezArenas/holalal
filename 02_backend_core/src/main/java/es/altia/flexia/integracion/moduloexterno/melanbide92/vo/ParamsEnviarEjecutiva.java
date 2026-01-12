package es.altia.flexia.integracion.moduloexterno.melanbide92.vo;

import java.math.BigDecimal;

/**
 *
 * @author kigonzalez
 */
public class ParamsEnviarEjecutiva {

    private String zorUsUsuario;
    private String zorUsPassword;
    private String area;
    private BigDecimal zorLiNumLiquidacion;
    private String zorLiFechaNotificacion;
    private String zorTnotCodigo;

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

    public String getZorLiFechaNotificacion() {
        return zorLiFechaNotificacion;
    }

    public void setZorLiFechaNotificacion(String zorLiFechaNotificacion) {
        this.zorLiFechaNotificacion = zorLiFechaNotificacion;
    }

    public String getZorTnotCodigo() {
        return zorTnotCodigo;
    }

    public void setZorTnotCodigo(String zorTnotCodigo) {
        this.zorTnotCodigo = zorTnotCodigo;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ParamsEnviarEjecutiva{");
        sb.append("\n zorUsUsuario=").append(zorUsUsuario);
        sb.append(", zorUsPassword=").append(zorUsPassword);
        sb.append(", area=").append(area);
        sb.append(",\n zorLiNumLiquidacion=").append(zorLiNumLiquidacion);
        sb.append(",\n zorLiFechaNotificaci\u00f3n=").append(zorLiFechaNotificacion);
        sb.append(",\n zorTnotCodigo=").append(zorTnotCodigo);
        sb.append("\n }");
        return sb.toString();
    }
    
}
