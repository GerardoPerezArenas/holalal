/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide43.vo;

import es.altia.flexia.integracion.moduloexterno.melanbide43.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide43.util.ConstantesMeLanbide43;

/**
 * Contiene los Objetos de Campos Suplementarios de Expediente para AVISOS : IdCuentaInteresado,Email,Telefono,Idioma Tanto para representante como titular
 * @author INGDGC
 */
public class DatosAvisoCSRegexlan {
    
    //AVISOEMAIL,AVISOEMAILTIT,AVISOSMS,AVISOSMSTIT,AVISOIDCTAINTE,AVISOIDCTAINTETIT,IDIOMAAVISOS
    public static final String COD_CAMPO_AVISOEMAIL="AVISOEMAIL";
    public static final String COD_CAMPO_AVISOEMAILTIT="AVISOEMAILTIT";
    public static final String COD_CAMPO_AVISOSMS="AVISOSMS";
    public static final String COD_CAMPO_AVISOSMSTIT="AVISOSMSTIT";
    public static final String COD_CAMPO_AVISOIDCTAINTE="AVISOIDCTAINTE";
    public static final String COD_CAMPO_AVISOIDCTAINTETIT="AVISOIDCTAINTETIT";
    public static final String COD_CAMPO_IDIOMAAVISOS="IDIOMAAVISOS";
    
    /**
     * AVISOIDCTAINTE
     */
    private String avisoIdCuentaInteresadoRepresentante;
    /**
     * AVISOIDCTAINTETIT
     */
    private String avisoIdCuentaInteresadoTitular;
    /**
     * AVISOEMAIL
     */
    private String avisoEmailRepresentante;
    /**
     * AVISOEMAILTIT
     */
    private String avisoEmailTitular;
    /**
     * AVISOSMS,AVISOSMSTIT
     */
    private String avisoSmsRepresentante;
    /**
     * AVISOSMSTIT
     */
    private String avisoSmsTitular;
    /**
     * IDIOMAAVISOS
     */
    private String avisoIdioma;

    /**
     * AVISOIDCTAINTE
     * @return 
     */
    public String getAvisoIdCuentaInteresadoRepresentante() {
        return avisoIdCuentaInteresadoRepresentante;
    }
    
    public void setAvisoIdCuentaInteresadoRepresentante(String avisoIdCuentaInteresadoRepresentante) {
        this.avisoIdCuentaInteresadoRepresentante = avisoIdCuentaInteresadoRepresentante;
    }

    /**
     * AVISOIDCTAINTETIT
     * @return 
     */
    public String getAvisoIdCuentaInteresadoTitular() {
        return avisoIdCuentaInteresadoTitular;
    }

    public void setAvisoIdCuentaInteresadoTitular(String avisoIdCuentaInteresadoTitular) {
        this.avisoIdCuentaInteresadoTitular = avisoIdCuentaInteresadoTitular;
    }

    /**
     * AVISOEMAIL
     * @return 
     */
    public String getAvisoEmailRepresentante() {
        return avisoEmailRepresentante;
    }

    public void setAvisoEmailRepresentante(String avisoEmailRepresentante) {
        this.avisoEmailRepresentante = avisoEmailRepresentante;
    }

    /**
     * AVISOEMAILTIT
     * @return 
     */
    public String getAvisoEmailTitular() {
        return avisoEmailTitular;
    }

    public void setAvisoEmailTitular(String avisoEmailTitular) {
        this.avisoEmailTitular = avisoEmailTitular;
    }

    /**
     * AVISOSMS
     * @return 
     */
    public String getAvisoSmsRepresentante() {
        return avisoSmsRepresentante;
    }

    public void setAvisoSmsRepresentante(String avisoSmsRepresentante) {
        this.avisoSmsRepresentante = avisoSmsRepresentante;
    }

    /**
     * AVISOSMSTIT
     * @return 
     */
    public String getAvisoSmsTitular() {
        return avisoSmsTitular;
    }

    public void setAvisoSmsTitular(String avisoSmsTitular) {
        this.avisoSmsTitular = avisoSmsTitular;
    }

    /**
     * IDIOMAAVISOS
     * @return 
     */
    public String getAvisoIdioma() {
        return avisoIdioma;
    }

    public void setAvisoIdioma(String avisoIdioma) {
        this.avisoIdioma = avisoIdioma;
    }

}
