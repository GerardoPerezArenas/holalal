/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.altia.flexia.integracion.moduloexterno.melanbide67.lanbide.wsclients.langaivision360.dto;

import es.altia.flexia.integracion.moduloexterno.melanbide67.lanbide.merkalan.model.I18nItem;

/**
 *
 * @author pablo.bugia
 */
public class SearchCVResponseDTO {

    private String url;

    private int errorCode;

    I18nItem description;

    public SearchCVResponseDTO() {
    }

    public SearchCVResponseDTO(String cvUrl) {
        this(cvUrl, 0);
    }

    public SearchCVResponseDTO(int error) {
        this(null, error);
    }

    public SearchCVResponseDTO(String url, int errorCode) {
        this.url = url;
        this.errorCode = errorCode;
    }

    public boolean hasError() {
        return errorCode != 0;
    }

    public I18nItem getDescription() {
        return description;
    }

    public void setDescription(I18nItem description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public int getErrorCode() {
        return errorCode;
    }    
}
