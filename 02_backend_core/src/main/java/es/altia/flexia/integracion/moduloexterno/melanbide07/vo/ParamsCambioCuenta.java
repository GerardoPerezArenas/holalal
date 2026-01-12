package es.altia.flexia.integracion.moduloexterno.melanbide07.vo;

import java.math.BigDecimal;

/**
 *
 * @author kigonzalez
 */
public class ParamsCambioCuenta {

    private String zorUsUsuario;
    private String zorUsPassword;
    private String area;
    private BigDecimal zorLiNumLiquidacion;
    private String numDocumento;
    private String zorCbtIban;
    private String zorCbtEntidad;
    private String zorCbtSucursal;
    private String zorCbtDc;
    private String zorCbtNumCuenta;
    private String zorCbtSubCodigo;

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

    public String getNumDocumento() {
        return numDocumento;
    }

    public void setNumDocumento(String numDocumento) {
        this.numDocumento = numDocumento;
    }

    public String getZorCbtIban() {
        return zorCbtIban;
    }

    public void setZorCbtIban(String zorCbtIban) {
        this.zorCbtIban = zorCbtIban;
    }

    public String getZorCbtEntidad() {
        return zorCbtEntidad;
    }

    public void setZorCbtEntidad(String zorCbtEntidad) {
        this.zorCbtEntidad = zorCbtEntidad;
    }

    public String getZorCbtSucursal() {
        return zorCbtSucursal;
    }

    public void setZorCbtSucursal(String zorCbtSucursal) {
        this.zorCbtSucursal = zorCbtSucursal;
    }

    public String getZorCbtDc() {
        return zorCbtDc;
    }

    public void setZorCbtDc(String zorCbtDc) {
        this.zorCbtDc = zorCbtDc;
    }

    public String getZorCbtNumCuenta() {
        return zorCbtNumCuenta;
    }

    public void setZorCbtNumCuenta(String zorCbtNumCuenta) {
        this.zorCbtNumCuenta = zorCbtNumCuenta;
    }

    public String getZorCbtSubCodigo() {
        return zorCbtSubCodigo;
    }

    public void setZorCbtSubCodigo(String zorCbtSubCodigo) {
        this.zorCbtSubCodigo = zorCbtSubCodigo;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ParamsCambioCuenta{");
        sb.append("\n zorUsUsuario=").append(zorUsUsuario);
        sb.append(", zorUsPassword=").append(zorUsPassword);
        sb.append(", area=").append(area);
        sb.append(",\n zorLiNumLiquidacion=").append(zorLiNumLiquidacion);
        sb.append(",\n numDocumento=").append(numDocumento);
        sb.append(",\n zorCbtIban=").append(zorCbtIban);
        sb.append(",\n zorCbtEntidad=").append(zorCbtEntidad);
        sb.append(",\n zorCbtSucursal=").append(zorCbtSucursal);
        sb.append(",\n zorCbtDc=").append(zorCbtDc);
        sb.append(",\n zorCbtNumCuenta=").append(zorCbtNumCuenta);
        sb.append(",\n zorCbtSubCodigo=").append(zorCbtSubCodigo);
        sb.append('}');
        return sb.toString();
    }
}
