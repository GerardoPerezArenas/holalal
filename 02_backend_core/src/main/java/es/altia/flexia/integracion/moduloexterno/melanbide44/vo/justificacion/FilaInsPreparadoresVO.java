/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide44.vo.justificacion;

import es.altia.flexia.integracion.moduloexterno.melanbide44.util.ConstantesMeLanbide44;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Laura
 */
public class FilaInsPreparadoresVO 
{
    private String idseg;
    private String codPreparador;  
    private String tipo;
    private String nif;    
    private String nombreApe;
    private String fecNacimiento;
    private String sexo;
    private Integer codTipoDiscapacidad;
    private String tipoDiscapacidad;
    private Integer codGravedad;
    private String gravedad;
    private Integer codTipoContrato;
    private String tipoContrato;
    private String porcJornada;
    private String fecIni;
    private String fecFin;
    private String nifPreparador;
    //private String persContacto;    
    private String fecSeguimiento;

    private String observaciones;
    
    private String colectivo;
    
    private List<String> errores = new ArrayList<String>();    
    private String[] erroresCampos = null;

    
    public static int NUM_CAMPOS_FILA = 16;
    
    public static int POS_CAMPO_NIF = 1;
    public static int POS_CAMPO_NOMBREAPEL = 2;    
    public static int POS_CAMPO_FECHA_NACIMIENTO = 3;
    public static int POS_CAMPO_SEXO = 4;
    public static int POS_CAMPO_COLECTIVO = 5;
    public static int POS_CAMPO_TIPODISCAPACIDAD = 6;
    public static int POS_CAMPO_GRAVEDAD = 7;
    public static int POS_CAMPO_TIPOCONTRATO = 8;
    public static int POS_CAMPO_PORCJORN = 9;    
    public static int POS_CAMPO_FECHA_INICIO = 10;
    public static int POS_CAMPO_FECHA_FIN = 11;    
    public static int POS_CAMPO_NIF_PREPARADOR = 12;
    public static int POS_CAMPO_EMPRESA = 13;        
    //public static int POS_CAMPO_CONTACTO =14;
    public static int POS_CAMPO_HORAS_ANUALES = 14;  
    public static int POS_CAMPO_OBSERVACIONES =15;
    
    private Integer finContratoDespido;
    
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    public String getNifPreparador() {
        return nifPreparador;
    }

    public void setNifPreparador(String nifPreparador) {
        this.nifPreparador = nifPreparador;
    }
    private String empresa;
    private String horasCont;
    
    public FilaInsPreparadoresVO()
    {      
        erroresCampos = new String[NUM_CAMPOS_FILA];
        for(int i = 0; i < erroresCampos.length; i++)
        {
            erroresCampos[i] = ConstantesMeLanbide44.FALSO;
        }   
    }

    public String getIdseg() {
        return idseg;
    }

    public void setIdseg(String idseg) {
        this.idseg = idseg;
    }

    public String getCodPreparador() {
        return codPreparador;
    }

    public void setCodPreparador(String codPreparador) {
        this.codPreparador = codPreparador;
    }


    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getNombreApe() {
        return nombreApe;
    }

    public void setNombreApe(String nombreApe) {
        this.nombreApe = nombreApe;
    }

    public String getFecNacimiento() {
        return fecNacimiento;
    }

    public void setFecNacimiento(String fecNacimiento) {
        this.fecNacimiento = fecNacimiento;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public Integer getCodTipoDiscapacidad() {
        return codTipoDiscapacidad;
    }

    public void setCodTipoDiscapacidad(Integer codTipoDiscapacidad) {
        this.codTipoDiscapacidad = codTipoDiscapacidad;
    }

    public String getTipoDiscapacidad() {
        return tipoDiscapacidad;
    }

    public void setTipoDiscapacidad(String tipoDiscapacidad) {
        this.tipoDiscapacidad = tipoDiscapacidad;
    }

    public Integer getCodGravedad() {
        return codGravedad;
    }

    public void setCodGravedad(Integer codGravedad) {
        this.codGravedad = codGravedad;
    }

    public String getGravedad() {
        return gravedad;
    }

    public void setGravedad(String gravedad) {
        this.gravedad = gravedad;
    }

    public Integer getCodTipoContrato() {
        return codTipoContrato;
    }

    public void setCodTipoContrato(Integer codTipoContrato) {
        this.codTipoContrato = codTipoContrato;
    }

    public String getTipoContrato() {
        return tipoContrato;
    }

    public void setTipoContrato(String tipoContrato) {
        this.tipoContrato = tipoContrato;
    }

    public String getPorcJornada() {
        return porcJornada;
    }

    public void setPorcJornada(String porcJornada) {
        this.porcJornada = porcJornada;
    }

    public String getFecIni() {
        return fecIni;
    }

    public void setFecIni(String fecIni) {
        this.fecIni = fecIni;
    }

    public String getFecFin() {
        return fecFin;
    }

    public void setFecFin(String fecFin) {
        this.fecFin = fecFin;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getHorasCont() {
        return horasCont;
    }

    public void setHorasCont(String horasCont) {
        this.horasCont = horasCont;
    }
    
    /*    public String getNombrePersContacto() {
        return persContacto;
    }

    public void setNombrePersContacto(String nombrePersContacto) {
        this.persContacto = nombrePersContacto;
    }*/

    public String getFecSeguimiento() {
        return fecSeguimiento;
    }

    public void setFecSeguimiento(String fecSeguimiento) {
        this.fecSeguimiento = fecSeguimiento;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    
    public List<String> getErrores() {
        return errores;
    }

    public void setErrores(List<String> errores) {
        this.errores = errores;
    }

    public String getColectivo() {
        return colectivo;
    }

    public void setColectivo(String colectivo) {
        this.colectivo = colectivo;
    }

    public String getErrorCampo(int posCampo)
    {
        if(posCampo >= 0 && posCampo < erroresCampos.length)
        {
            try
            {
                return erroresCampos[posCampo];
            }
            catch(Exception ex)
            {
                return ConstantesMeLanbide44.FALSO;
            }
        }
        return ConstantesMeLanbide44.FALSO;
    }
    
    public void setErrorCampo(int posCampo, String errorCampo)
    {
        if(posCampo >= 0 && posCampo < erroresCampos.length)
        {
            if(errorCampo != null && (errorCampo.equalsIgnoreCase(ConstantesMeLanbide44.CIERTO) || errorCampo.equalsIgnoreCase(ConstantesMeLanbide44.FALSO)))
            {
                erroresCampos[posCampo] = errorCampo;
            }
        }
    }

    public Integer getFinContratoDespido() {
        return finContratoDespido;
    }

    public void setFinContratoDespido(Integer finContratoDespido) {
        this.finContratoDespido = finContratoDespido;
    }
    
   }
