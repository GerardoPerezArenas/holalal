/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package es.altia.flexia.integracion.moduloexterno.melanbide67.lanbide.merkalan.model;

import es.altia.flexia.integracion.moduloexterno.melanbide67.lanbide.merkalan.util.StringUtils;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author pablo.bugia
 */
public enum Languages {

    ES_ES("C", "es_ES"),
    EU_ES("E", "eu_ES");

    private final String code;

    private final String localeName;

    private Languages(String code, String localeName) {
        this.code = code;
        this.localeName = localeName;
    }

    public static Languages fromLocaleName(String localeName) {
        if (StringUtils.isNullOrBlank(localeName)) {
            return null;
        }

//        List<Languages> filteredResults = Arrays.asList(Languages.values()).stream()
//                                                                .filter(item -> item.getLocaleName().equalsIgnoreCase(localeName))
//                                                                .collect(Collectors.toList());
        List<Languages> filteredResults = new ArrayList<Languages>();
        for (final Languages language : Languages.values()) {
            if (language.getCode().equalsIgnoreCase(localeName)) {
                filteredResults.add(language);
            }
        }
        return filteredResults.isEmpty() ? null : filteredResults.get(0);
    }

    public String getCode() {
        return code;
    }

    public String getLocaleName() {
        return localeName;
    }
    
    
}
