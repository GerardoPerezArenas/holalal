package es.altia.flexia.integracion.moduloexterno.melanbide07.vo;

import java.math.BigDecimal;

/**
 *
 * @author kigonzalez
 */
public class ParamsAltaFraccionamiento {

    private String zorUsUsuario;
    private String zorUsPassword;
    private String area;
    private BigDecimal zorLiNumLiquidacion;
    private String zorLiFechaSolicitudFracc;
    private BigDecimal cuotaSolicitada;
    private BigDecimal mesesAplazamiento;
    private String zorCbtIban;
    private String zorCbtEntidad;
    private String zorCbtSucursal;
    private String zorCbtDc;
    private String zorCbtNumCuenta;

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

    public String getZorLiFechaSolicitudFracc() {
        return zorLiFechaSolicitudFracc;
    }

    public void setZorLiFechaSolicitudFracc(String zorLiFechaSolicitudFracc) {
        this.zorLiFechaSolicitudFracc = zorLiFechaSolicitudFracc;
    }

    public BigDecimal getCuotaSolicitada() {
        return cuotaSolicitada;
    }

    public void setCuotaSolicitada(BigDecimal cuotaSolicitada) {
        this.cuotaSolicitada = cuotaSolicitada;
    }

    public BigDecimal getMesesAplazamiento() {
        return mesesAplazamiento;
    }

    public void setMesesAplazamiento(BigDecimal mesesAplazamiento) {
        this.mesesAplazamiento = mesesAplazamiento;
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


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ParamsAltaFraccionamiento{");
        sb.append("\n zorUsUsuario=").append(zorUsUsuario);
        sb.append(", zorUsPassword=").append(zorUsPassword);
        sb.append(", area=").append(area);
        sb.append(",\n zorLiNumLiquidacion=").append(zorLiNumLiquidacion);
        sb.append(",\n zorLiFechaSolicitudFracc=").append(zorLiFechaSolicitudFracc);
        sb.append(",\n cuotaSolicitada=").append(cuotaSolicitada);
        sb.append(",\n mesesAplazamiento=").append(mesesAplazamiento);
        sb.append(",\n zorCbtIban=").append(zorCbtIban);
        sb.append(",\n zorCbtEntidad=").append(zorCbtEntidad);
        sb.append(",\n zorCbtSucursal=").append(zorCbtSucursal);
        sb.append(",\n zorCbtDc=").append(zorCbtDc);
        sb.append(",\n zorCbtNumCuenta=").append(zorCbtNumCuenta);
        sb.append("\n}");
        return sb.toString();
    }

  

}
