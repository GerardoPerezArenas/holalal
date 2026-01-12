package es.altia.flexia.integracion.moduloexterno.melanbide07.vo;

import java.math.BigDecimal;

/**
 *
 * @author kigonzalez
 */
public class ParamsAnularDeuda {

    private String zorUsUsuario;
    private String zorUsPassword;
    private String area;
    private BigDecimal zorLiNumLiquidacion;
    private String zorMaCod;
    private String zorLiMotivoAnulacionTexto;

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

    public String getZorMaCod() {
        return zorMaCod;
    }

    public void setZorMaCod(String zorMaCod) {
        this.zorMaCod = zorMaCod;
    }

    public String getZorLiMotivoAnulacionTexto() {
        return zorLiMotivoAnulacionTexto;
    }

    public void setZorLiMotivoAnulacionTexto(String zorLiMotivoAnulacionTexto) {
        this.zorLiMotivoAnulacionTexto = zorLiMotivoAnulacionTexto;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ParamsAnularDeuda{");
        sb.append("\n zorUsUsuario=").append(zorUsUsuario);
        sb.append(", zorUsPassword=").append(zorUsPassword);
        sb.append(", area=").append(area);
        sb.append(",\n zorLiNumLiquidacion=").append(zorLiNumLiquidacion);
        sb.append(",\n zorMaCod=").append(zorMaCod);
        sb.append(",\n zorLiMotivoAnulacionTexto=").append(zorLiMotivoAnulacionTexto);
        sb.append("\n }");
        return sb.toString();
    }

    
}
