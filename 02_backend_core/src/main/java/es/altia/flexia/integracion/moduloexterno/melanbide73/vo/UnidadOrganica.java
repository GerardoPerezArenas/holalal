/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.altia.flexia.integracion.moduloexterno.melanbide73.vo;

import es.altia.flexia.integracion.moduloexterno.melanbide73.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide73.util.ConstantesMeLanbide73;

/**
 *
 * @author pablo.bugia
 */
public final class UnidadOrganica {
    private final int uorCod;
    private final Integer uorPad;
    private final String uorNom;
    private final String uorCodVis;
    private final String uorEstado;

    public UnidadOrganica(int uorCod, int uorPad, String uorNom, String uorEstado, java.lang.String uorCodVis) {
        this.uorCod = uorCod;
        this.uorPad = uorPad;
        this.uorNom = uorNom;
        this.uorEstado = uorEstado;
        this.uorCodVis = uorCodVis;
    }

    public int getUorCod() {
        return uorCod;
    }

    public int getUorPad() {
        return uorPad;
    }

    public String getUorNom() {
        return uorNom;
    }

    public boolean isActive() {
        return uorEstado.equalsIgnoreCase(ConfigurationParameter.getParameter(ConstantesMeLanbide73.VALUE_UOR_ACTIVA,
                            ConstantesMeLanbide73.FICHERO_PROPIEDADES));
    }

    public String getUorCodVis() {
        return uorCodVis;
    }

    @Override
    public String toString() {
        return "UnidadOrganica{" + "uorCod=" + uorCod + ", uorPad=" + uorPad + ", uorNom=" + uorNom + ", uorEstado=" + isActive() + '}';
    }
    
    
    
    
}
