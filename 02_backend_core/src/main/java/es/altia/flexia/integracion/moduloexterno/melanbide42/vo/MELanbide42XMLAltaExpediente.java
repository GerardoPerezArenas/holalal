/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.flexia.integracion.moduloexterno.melanbide42.vo;

import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author mikel
 */
public class MELanbide42XMLAltaExpediente {
    
    private Map<String, String> camposVariables = null;

    private ArrayList<MELanbide42ModuloVO> listaModulos = null;
    
    public MELanbide42XMLAltaExpediente (Map<String, String> camposVariables, ArrayList<MELanbide42ModuloVO> listaModulos){
        this.camposVariables = camposVariables;
        this.listaModulos = listaModulos;
    }
    
    public Map<String, String> getCamposVariables() {
        return camposVariables;
    }

    public ArrayList<MELanbide42ModuloVO> getListaModulos() {
        return listaModulos;
    }
    public void addFila(MELanbide42ModuloVO fila){
        listaModulos.add(fila);
    }
}
