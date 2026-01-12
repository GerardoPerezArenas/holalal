/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.altia.flexia.integracion.moduloexterno.melanbide67.lanbide.wsclients.langaivision360.facade;

import es.altia.flexia.integracion.moduloexterno.melanbide67.lanbide.merkalan.exception.RemoteServiceException;
import es.altia.flexia.integracion.moduloexterno.melanbide67.lanbide.merkalan.model.Languages;
import es.altia.flexia.integracion.moduloexterno.melanbide67.lanbide.wsclients.langaivision360.dto.SearchCVResponseDTO;
import es.altia.flexia.integracion.moduloexterno.melanbide67.lanbide.wsclients.langaivision360.enums.GeneratedDocuments;
import es.altia.flexia.integracion.moduloexterno.melanbide67.ws.client.langaivision360.LangaiVision360ServiceLocator;

/**
 *
 * @author pablo.bugia
 */
public class LangaiVision360WSFacade {

    private LangaiVision360ServiceLocator langaiVision360ServiceLocator = null;

    public LangaiVision360WSFacade(String langaiDemWSDLLocation) {
        langaiVision360ServiceLocator = new LangaiVision360ServiceLocator();
        langaiVision360ServiceLocator.setLangaiVision360PortEndpointAddress(langaiDemWSDLLocation);
    }

    public SearchCVResponseDTO generateApplicantCV(String documentType, String documentNum, 
            String document, String locale) throws RemoteServiceException {
        String cvUrl = "";
        SearchCVResponseDTO response;
        Languages lang = Languages.fromLocaleName(locale);
        try {
            cvUrl = langaiVision360ServiceLocator.getLangaiVision360Port().generarDocumentoPdf(documentType,
                    documentNum,
                    document,
                    null, null, null,
                    lang.getCode());
        } catch (Exception ex) {
            throw new RemoteServiceException("Error generating applicant CV", ex);
        }

        if (null != cvUrl) {
            if (cvUrl.startsWith("http")) {
                //Response may contain a URL. We assume it's a valid one.
                response = new SearchCVResponseDTO(cvUrl);
            } else {
                //It's not a URL. There may have been some error.
                response = new SearchCVResponseDTO(Integer.valueOf(cvUrl));
            }
        } else {
            response = new SearchCVResponseDTO(Integer.valueOf(cvUrl));
        }
        return response;
    }    
}
