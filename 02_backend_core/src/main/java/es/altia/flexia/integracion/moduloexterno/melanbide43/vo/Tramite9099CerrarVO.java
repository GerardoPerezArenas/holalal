
package es.altia.flexia.integracion.moduloexterno.melanbide43.vo;

import java.util.Date;

/**
 *
 * @author alexandrep
 */
public class Tramite9099CerrarVO {
    
        private Integer codOrganizacion;
        private Integer ejercicio;
        private String codProcedimiento;
        private String numeroExpediente;
        private Integer codTramiteActal;
        private Integer ocurrenciaTramiteActual;
        private Date fechaInicioTramite;
        private Integer usuario;
        private Integer uor;

    public Integer getUsuario() {
        return usuario;
    }

    public void setUsuario(Integer usuario) {
        this.usuario = usuario;
    }

    public Integer getUor() {
        return uor;
    }

    public void setUor(Integer uor) {
        this.uor = uor;
    }

        public Date getFechaInicioTramite() {
            return fechaInicioTramite;
        }

        public void setFechaInicioTramite(Date fechaInicioTramite) {
            this.fechaInicioTramite = fechaInicioTramite;
        }

        
        
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

        public Integer getCodTramiteActal() {
            return codTramiteActal;
        }

        public void setCodTramiteActal(int codTramiteActal) {
            this.codTramiteActal = codTramiteActal;
        }

        public Integer getOcurrenciaTramiteActual() {
            return ocurrenciaTramiteActual;
        }

        public void setOcurrenciaTramiteActual(Integer ocurrenciaTramiteActual) {
            this.ocurrenciaTramiteActual = ocurrenciaTramiteActual;
        }
    }
