/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.altia.flexia.integracion.moduloexterno.melanbide67.ws.client.vidalaboralws.request;

/**
 *
 * @author pablo.bugia
 */
public class Tramitador {
    private String codProcedimiento;
    private String nombreProcedimiento;
    private String finalidadProcedimiento;
    private String consentimientoFirmado;
    private String nifUsuarioTramitador;
    private String usuarioTramitador;

    public Tramitador(String codProcedimiento, String nombreProcedimiento, String finalidadProcedimiento, String consentimientoFirmado, String nifUsuarioTramitador, String usuarioTramitador) {
        this.codProcedimiento = codProcedimiento;
        this.nombreProcedimiento = nombreProcedimiento;
        this.finalidadProcedimiento = finalidadProcedimiento;
        this.consentimientoFirmado = consentimientoFirmado;
        this.nifUsuarioTramitador = nifUsuarioTramitador;
        this.usuarioTramitador = usuarioTramitador;
    }

    public String getCodProcedimiento() {
        return codProcedimiento;
    }

    public void setCodProcedimiento(String codProcedimiento) {
        this.codProcedimiento = codProcedimiento;
    }

    public String getNombreProcedimiento() {
        return nombreProcedimiento;
    }

    public void setNombreProcedimiento(String nombreProcedimiento) {
        this.nombreProcedimiento = nombreProcedimiento;
    }

    public String getFinalidadProcedimiento() {
        return finalidadProcedimiento;
    }

    public void setFinalidadProcedimiento(String finalidadProcedimiento) {
        this.finalidadProcedimiento = finalidadProcedimiento;
    }

    public String getConsentimientoFirmado() {
        return consentimientoFirmado;
    }

    public void setConsentimientoFirmado(String consentimientoFirmado) {
        this.consentimientoFirmado = consentimientoFirmado;
    }

    public String getNifUsuarioTramitador() {
        return nifUsuarioTramitador;
    }

    public void setNifUsuarioTramitador(String nifUsuarioTramitador) {
        this.nifUsuarioTramitador = nifUsuarioTramitador;
    }

    public String getUsuarioTramitador() {
        return usuarioTramitador;
    }

    public void setUsuarioTramitador(String usuarioTramitador) {
        this.usuarioTramitador = usuarioTramitador;
    }

    @Override
    public String toString() {
        return "Tramitador{" + "codProcedimiento=" + codProcedimiento + ", nombreProcedimiento=" + nombreProcedimiento + ", finalidadProcedimiento=" + finalidadProcedimiento + ", consentimientoFirmado=" + consentimientoFirmado + ", nifUsuarioTramitador=" + nifUsuarioTramitador + ", usuarioTramitador=" + usuarioTramitador + '}';
    }
}
