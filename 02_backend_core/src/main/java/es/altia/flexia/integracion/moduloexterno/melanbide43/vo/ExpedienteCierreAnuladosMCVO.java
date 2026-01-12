/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.altia.flexia.integracion.moduloexterno.melanbide43.vo;

/**
 *
 * @author alexandrep
 */
public class ExpedienteCierreAnuladosMCVO {
    
     private int codOrganizacion;
        private int ejercicio;
        private String codProcedimiento;
        private String numeroExpediente;
        private int codTramiteActal;
        private int ocurrenciaTramiteActual;

        public int getCodOrganizacion() {
            return codOrganizacion;
        }

        public void setCodOrganizacion(int codOrganizacion) {
            this.codOrganizacion = codOrganizacion;
        }

        public int getEjercicio() {
            return ejercicio;
        }

        public void setEjercicio(int ejercicio) {
            this.ejercicio = ejercicio;
        }

        public String getCodProcedimiento() {
            return codProcedimiento;
        }

        public void setCodProcedimiento(String codProcedimiento) {
            this.codProcedimiento = codProcedimiento;
        }

        public String getNumeroExpediente() {
            return numeroExpediente;
        }

        public void setNumeroExpediente(String numeroExpediente) {
            this.numeroExpediente = numeroExpediente;
        }

        public int getCodTramiteActal() {
            return codTramiteActal;
        }

        public void setCodTramiteActal(int codTramiteActal) {
            this.codTramiteActal = codTramiteActal;
        }

        public int getOcurrenciaTramiteActual() {
            return ocurrenciaTramiteActual;
        }

        public void setOcurrenciaTramiteActual(int ocurrenciaTramiteActual) {
            this.ocurrenciaTramiteActual = ocurrenciaTramiteActual;
        }
    
}
