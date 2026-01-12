/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide03.vo;

import java.util.ArrayList;

/**
 * Clase VO para generar el XML del report
 * 
 * @author david.caamano
 * @version 15/10/2012 1.0
 * Historial de cambios:
 * <ol>
 *  <li>david.caamano * 15-10-2012 * </li>
 * </ol> 
 */
public class MeLanbide03GenerateReportVO {
    
    private ArrayList<MeLanbide03InteresadoGenerateReportVO> interesados;
    private ArrayList<MeLanbide03UnidadCompetencialGenerateReportVO> unidades;

    public ArrayList<MeLanbide03InteresadoGenerateReportVO> getInteresados() {
        return interesados;
    }//getInteresados
    public void setInteresados(ArrayList<MeLanbide03InteresadoGenerateReportVO> interesados) {
        this.interesados = interesados;
    }//setInteresados

    public ArrayList<MeLanbide03UnidadCompetencialGenerateReportVO> getUnidades() {
        return unidades;
    }//getUnidades
    public void setUnidades(ArrayList<MeLanbide03UnidadCompetencialGenerateReportVO> unidades) {
        this.unidades = unidades;
    }//setUnidades
    
}