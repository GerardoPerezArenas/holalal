package es.altia.flexia.integracion.moduloexterno.melanbide03.vo;

import java.util.ArrayList;
import java.util.Date;

/**
 * @author david.caamano
 * @version 16/08/2012 1.0
 * Historial de cambios:
 * <ol>
 *  <li>david.caamano * 16-08-2012 * #53275 Edici�n inicial</li>
 * </ol> 
 */
public class CertificacionPositiva {
    /*MELANBIDE03_ETIQUETA_TIPOCP;
    MELANBIDE03_ETIQUETA_CODMODPRACT;
    MELANBIDE03_ETIQUETA_MODACREDITADO;*/
    
    //Propiedades
    private String INTERESADO_SOLICITANTE;
    private String INTERESADO_DomSOLICITANTE;
    private String NUM;
    private String INTERESADO_CodPostalSOLICITAN;
    private String INTERESADO_PobSOLICITANTE;
    private String INTERESADO_ProvinciaSOLICITAN;
    private String FECACTEU;
    private String FECACTESP;
    //private String INTERESADO_SOLICITANTE;
    private String INTERESADO_DocSOLICITANTE;
    private String CODIGOCP;                        //MELANBIDE03_ETIQUETA_CODIGOCP
    private String NIVEL;                           //MELANBIDE03_ETIQUETA_NIVEL
    private String DESCRIPCIONE;                    //MELANBIDE03_ETIQUETA_DESCRIPCIONE
    private String DESCRIPCIONC;                    //MELANBIDE03_ETIQUETA_DESCRIPCIONC  
    private String RDFECHAEUSK;                     //MELANBIDE03_ETIQUETA_FECHARD
    private String DECMODTEXTEUSK;                  //MELANBIDE03_ETIQUETA_DESCMODULOE
    private String DECRETO;                         //MELANBIDE03_ETIQUETA_DECRETO
    private String FECHARD;                         //MELANBIDE03_ETIQUETA_FECHARD
    private String DECMODTEXT;                      //MELANBIDE03_ETIQUETA_DESCMODULOC
    
    private String NUMEXPEDIENTE;
    private ArrayList<String> LISTAACREDITADAS3;
    
    //M�todos de acceso

    public String getINTERESADO_SOLICITANTE() {
        return INTERESADO_SOLICITANTE;
    }
    public void setINTERESADO_SOLICITANTE(String INTERESADO_SOLICITANTE) {
        this.INTERESADO_SOLICITANTE = INTERESADO_SOLICITANTE;
    }
    
    public String getINTERESADO_DomSOLICITANTE() {
        return INTERESADO_DomSOLICITANTE;
    }
    public void setINTERESADO_DomSOLICITANTE(String INTERESADO_DomSOLICITANTE) {
        this.INTERESADO_DomSOLICITANTE = INTERESADO_DomSOLICITANTE;
    }
    
    public String getNUM() {
        return NUM;
    }
    public void setNUM(String NUM) {
        this.NUM = NUM;
    }
    
    public String getINTERESADO_CodPostalSOLICITAN() {
        return INTERESADO_CodPostalSOLICITAN;
    }
    public void setINTERESADO_CodPostalSOLICITAN(String INTERESADO_CodPostalSOLICITAN) {
        this.INTERESADO_CodPostalSOLICITAN = INTERESADO_CodPostalSOLICITAN;
    }

    public String getINTERESADO_PobSOLICITANTE() {
        return INTERESADO_PobSOLICITANTE;
    }
    public void setINTERESADO_PobSOLICITANTE(String INTERESADO_PobSOLICITANTE) {
        this.INTERESADO_PobSOLICITANTE = INTERESADO_PobSOLICITANTE;
    }

    public String getINTERESADO_ProvinciaSOLICITAN() {
        return INTERESADO_ProvinciaSOLICITAN;
    }
    
    public void setINTERESADO_ProvinciaSOLICITAN(String INTERESADO_ProvinciaSOLICITAN) {
        this.INTERESADO_ProvinciaSOLICITAN = INTERESADO_ProvinciaSOLICITAN;
    }
    
    public String getFECACTEU() {
        return FECACTEU;
    }
    
    public void setFECACTEU(String FECACTEU) {
        this.FECACTEU = FECACTEU;
    }
    
    public String getFECACTESP() {
        return FECACTESP;
    }
    public void setFECACTESP(String FECACTESP) {
        this.FECACTESP = FECACTESP;
    }
    
    /*public String getINTERESADO_SOLICITANTE() {
        return INTERESADO_SOLICITANTE;
    }
    
    public void setINTERESADO_SOLICITANTE(String INTERESADO_SOLICITANTE) {
        this.INTERESADO_SOLICITANTE = INTERESADO_SOLICITANTE;
    }*/
    
    public String getINTERESADO_DocSOLICITANTE() {
        return INTERESADO_DocSOLICITANTE;
    }
    public void setINTERESADO_DocSOLICITANTE(String INTERESADO_DocSOLICITANTE) {
        this.INTERESADO_DocSOLICITANTE = INTERESADO_DocSOLICITANTE;
    }
    
    public String getCODIGOCP() {
        return CODIGOCP;
    }
    public void setCODIGOCP(String CODIGOCP) {
        this.CODIGOCP = CODIGOCP;
    }
    
    public String getNIVEL() {
        return NIVEL;
    }
    public void setNIVEL(String NIVEL) {
        this.NIVEL = NIVEL;
    }
    
    public String getDESCRIPCIONE() {
        return DESCRIPCIONE;
    }
    public void setDESCRIPCIONE(String DESCRIPCIONE) {
        this.DESCRIPCIONE = DESCRIPCIONE;
    }
    
    public String getDESCRIPCIONC() {
        return DESCRIPCIONC;
    }
    public void setDESCRIPCIONC(String DESCRIPCIONC) {
        this.DESCRIPCIONC = DESCRIPCIONC;
    }
    
    public String getRDFECHAEUSK() {
        return RDFECHAEUSK;
    }
    public void setRDFECHAEUSK(String RDFECHAEUSK) {
        this.RDFECHAEUSK = RDFECHAEUSK;
    }
    
    public String getDECMODTEXTEUSK() {
        return DECMODTEXTEUSK;
    }
    public void setDECMODTEXTEUSK(String DECMODTEXTEUSK) {
        this.DECMODTEXTEUSK = DECMODTEXTEUSK;
    }
    
    public String getDECRETO() {
        return DECRETO;
    }
    public void setDECRETO(String DECRETO) {
        this.DECRETO = DECRETO;
    }
    
    public String getFECHARD() {
        return FECHARD;
    }
    public void setFECHARD(String FECHARD) {
        this.FECHARD = FECHARD;
    }
    
    public String getDECMODTEXT() {
        return DECMODTEXT;
    }
    public void setDECMODTEXT(String DECMODTEXT) {
        this.DECMODTEXT = DECMODTEXT;
    }
    
    public String getNUMEXPEDIENTE() {
        return NUMEXPEDIENTE;
    }
    public void setNUMEXPEDIENTE(String NUMEXPEDIENTE) {
        this.NUMEXPEDIENTE = NUMEXPEDIENTE;
    }
    
    public ArrayList<String> getLISTAACREDITADAS3() {
        return LISTAACREDITADAS3;
    }
    public void setLISTAACREDITADAS3(ArrayList<String> LISTAACREDITADAS3) {
        this.LISTAACREDITADAS3 = LISTAACREDITADAS3;
    }
    
    
}//class
