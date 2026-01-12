/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.altia.flexia.integracion.moduloexterno.melanbide18.vo;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author pbugia
 */
public class ParamsAltaFraccionamiento {
    private String zorUsUsuario;
    private String zorUsPassword;
    
    private String numDocumento;
    private List<String> listExpedientes;
    
    private BigDecimal telefono;
    private String email;
    
    private BigDecimal cuotaSolicitada;
    
    private String zorCbtIban;
    private String zorCbtEntidad;
    private String zorCbtSucursal;
    private String zorCbtDc;
    private String zorCbtNumCuenta;
    private BigDecimal importeCuota;
    private String oidRecurso;
    private String zorLiNumLiquidacion;
    private String expFraccionamiento;
    private boolean notificacionPostal;



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

    public String getNumDocumento() {
        return numDocumento;
    }

    public void setNumDocumento(String numDocumento) {
        this.numDocumento = numDocumento;
    }

    public List<String> getListExpedientes() {
        return listExpedientes;
    }

    public void setListExpedientes(List<String> listExpedientes) {
        this.listExpedientes = listExpedientes;
    }

    public BigDecimal getTelefono() {
        return telefono;
    }

    public void setTelefono(BigDecimal telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BigDecimal getCuotaSolicitada() {
        return cuotaSolicitada;
    }

    public void setCuotaSolicitada(BigDecimal cuotaSolicitada) {
        this.cuotaSolicitada = cuotaSolicitada;
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

    public BigDecimal getImporteCuota() {
        return importeCuota;
    }

    public void setImporteCuota(BigDecimal importeCuota) {
        this.importeCuota = importeCuota;
    }
    
    public String getOidRecurso() {
        return oidRecurso;
    }
    
    public void setOidRecurso(String oidRecurso) {
        this.oidRecurso = oidRecurso;
    }

    public String getZorLiNumLiquidacion() {
        return zorLiNumLiquidacion;
    }

    public void setZorLiNumLiquidacion(String zorLiNumLiquidacion) {
        this.zorLiNumLiquidacion = zorLiNumLiquidacion;
    }

    public String getExpFraccionamiento() {
        return expFraccionamiento;
    }
    
    public void setExpFraccionamiento(String expFraccionamiento) {
        this.expFraccionamiento = expFraccionamiento;
    }

    public boolean isNotificacionPostal() {
        return notificacionPostal;
    }

    public void setNotificacionPostal(boolean notificacionPostal) {
        this.notificacionPostal = notificacionPostal;
    }

    @Override
    public String toString() {
        return "ParamsAltaFraccionamiento{" +
                "zorUsUsuario='" + zorUsUsuario + '\'' +
                ", zorUsPassword='" + zorUsPassword + '\'' +
                ", numDocumento='" + numDocumento + '\'' +
                ", listExpedientes=" + (listExpedientes!= null ? Arrays.toString(listExpedientes.toArray()) : "" )+
                ", telefono=" + telefono +
                ", email='" + email + '\'' +
                ", cuotaSolicitada=" + cuotaSolicitada +
                ", zorCbtIban='" + zorCbtIban + '\'' +
                ", zorCbtEntidad='" + zorCbtEntidad + '\'' +
                ", zorCbtSucursal='" + zorCbtSucursal + '\'' +
                ", zorCbtDc='" + zorCbtDc + '\'' +
                ", zorCbtNumCuenta='" + zorCbtNumCuenta + '\'' +
                ", importeCuota=" + importeCuota +
                ", oidRecurso='" + oidRecurso + '\'' +
                ", zorLiNumLiquidacion='" + zorLiNumLiquidacion + '\'' +
                ", expFraccionamiento='" + expFraccionamiento + '\'' +
                ", notificacionPostal=" + notificacionPostal +
                '}';
    }
}
