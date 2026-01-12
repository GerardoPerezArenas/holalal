/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package es.altia.flexia.integracion.moduloexterno.melanbide67.lanbide.wsclients.langaivision360.enums;

import es.altia.flexia.integracion.moduloexterno.melanbide67.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide67.util.ConstantesMeLanbide67;

/**
 *
 * @author pablo.bugia
 */
public enum GeneratedDocuments {
    CV(ConfigurationParameter.getParameter(ConstantesMeLanbide67.DOCUMENTO_CV, ConstantesMeLanbide67.FICHERO_PROPIEDADES)),
    DEMANDA(ConfigurationParameter.getParameter(ConstantesMeLanbide67.DOCUMENTO_DEMANDA, ConstantesMeLanbide67.FICHERO_PROPIEDADES));

    private String code;

    private GeneratedDocuments(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
    
}
