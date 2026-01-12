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
public class FilaVisProspectoresVO 
{
    private String idvisita;
    private String codProspector;      
    private String cif;    
    private String empresa;
    private Integer codSector;
    private String descSector;
    private String direccion;
    private String cpostal;
    private String localidad;
    private Integer codProvincia;
    private String descProvincia;
    private String numTrab;
    private String numTrabDisc;
    private Integer codCumpleLismi;
    private String descCumpleLismi;
    private Integer codResultadoFinal;
    private String descResultadoFinal;
    private String nifProspector;
    private String persContacto;  
    private String puesto;
    private String mail;
    private String telefono;
    private String fecVisita;

    private String observaciones;
    
    private List<String> errores = new ArrayList<String>();    
    private String[] erroresCampos = null;
    
    public FilaVisProspectoresVO()
    {      
        erroresCampos = new String[NUM_CAMPOS_FILA];
        for(int i = 0; i < erroresCampos.length; i++)
        {
            erroresCampos[i] = ConstantesMeLanbide44.FALSO;
        }   
    }

    public String getIdvisita() {
        return idvisita;
    }

    public void setIdvisita(String idvisita) {
        this.idvisita = idvisita;
    }

    public String getCodProspector() {
        return codProspector;
    }

    public void setCodProspector(String codProspector) {
        this.codProspector = codProspector;
    }

    public String getCif() {
        return cif;
    }

    public void setCif(String cif) {
        this.cif = cif;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCpostal() {
        return cpostal;
    }

    public void setCpostal(String cpostal) {
        this.cpostal = cpostal;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getNumTrab() {
        return numTrab;
    }

    public void setNumTrab(String numTrab) {
        this.numTrab = numTrab;
    }

    public String getNumTrabDisc() {
        return numTrabDisc;
    }

    public void setNumTrabDisc(String numTrabDisc) {
        this.numTrabDisc = numTrabDisc;
    }

    public String getNifProspector() {
        return nifProspector;
    }

    public void setNifProspector(String nifProspector) {
        this.nifProspector = nifProspector;
    }

    public String getPersContacto() {
        return persContacto;
    }

    public void setPersContacto(String persContacto) {
        this.persContacto = persContacto;
    }

    public String getPuesto() {
        return puesto;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getFecVisita() {
        return fecVisita;
    }

    public void setFecVisita(String fecVisita) {
        this.fecVisita = fecVisita;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Integer getCodSector() {
        return codSector;
    }

    public void setCodSector(Integer codSector) {
        this.codSector = codSector;
    }

    public String getDescSector() {
        return descSector;
    }

    public void setDescSector(String descSector) {
        this.descSector = descSector;
    }

    public Integer getCodProvincia() {
        return codProvincia;
    }

    public void setCodProvincia(Integer codProvincia) {
        this.codProvincia = codProvincia;
    }

    public String getDescProvincia() {
        return descProvincia;
    }

    public void setDescProvincia(String descProvincia) {
        this.descProvincia = descProvincia;
    }

    public Integer getCodCumpleLismi() {
        return codCumpleLismi;
    }

    public void setCodCumpleLismi(Integer codCumpleLismi) {
        this.codCumpleLismi = codCumpleLismi;
    }

    public String getDescCumpleLismi() {
        return descCumpleLismi;
    }

    public void setDescCumpleLismi(String descCumpleLismi) {
        this.descCumpleLismi = descCumpleLismi;
    }

    public Integer getCodResultadoFinal() {
        return codResultadoFinal;
    }

    public void setCodResultadoFinal(Integer codResultadoFinal) {
        this.codResultadoFinal = codResultadoFinal;
    }

    public String getDescResultadoFinal() {
        return descResultadoFinal;
    }

    public void setDescResultadoFinal(String descResultadoFinal) {
        this.descResultadoFinal = descResultadoFinal;
    }
   

    
    public static int NUM_CAMPOS_FILA = 19;
    
    public static int POS_CAMPO_FECVISITA = 3;
    public static int POS_CAMPO_EMPRESA = 1;
    public static int POS_CAMPO_CIF = 2;
    public static int POS_CAMPO_SECTORACT = 4;
    public static int POS_CAMPO_DIRECCION = 5;
    public static int POS_CAMPO_CPOSTAL = 6;
    public static int POS_CAMPO_LOCALIDAD = 7;
    public static int POS_CAMPO_PROVINCIA = 8;
    public static int POS_CAMPO_PCONTACTO = 9;
    public static int POS_CAMPO_CARGO = 10;
    public static int POS_CAMPO_EMAIL = 11;    
    public static int POS_CAMPO_TELEFONO = 12;
    public static int POS_CAMPO_NIFPROSPECTOR = 13;
    public static int POS_CAMPO_NUMTRAB = 14;    
    public static int POS_CAMPO_NUMTRABDISC = 15;
    public static int POS_CAMPO_CUMPLELISMI = 16;        
    public static int POS_CAMPO_RESULTADO =17;    
    public static int POS_CAMPO_OBSERVACIONES =18;
    
    
    
    
    public List<String> getErrores() {
        return errores;
    }

    public void setErrores(List<String> errores) {
        this.errores = errores;
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
    
    
   }
