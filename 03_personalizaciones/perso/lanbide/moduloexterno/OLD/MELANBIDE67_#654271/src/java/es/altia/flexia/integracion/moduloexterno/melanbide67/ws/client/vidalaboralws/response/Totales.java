/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.altia.flexia.integracion.moduloexterno.melanbide67.ws.client.vidalaboralws.response;

/**
 *
 * @author pablo.bugia
 */
public class Totales {
    private int totalDiasAlta;
    private int diasPluriempleo;
    private int aniosAlta;
    private int mesesAlta;
    private int diasAlta;

    public Totales(int totalDiasAlta, int diasPluriempleo, int aniosAlta, int mesesAlta, int diasAlta) {
        this.totalDiasAlta = totalDiasAlta;
        this.diasPluriempleo = diasPluriempleo;
        this.aniosAlta = aniosAlta;
        this.mesesAlta = mesesAlta;
        this.diasAlta = diasAlta;
    }

    public int getTotalDiasAlta() {
        return totalDiasAlta;
    }

    public void setTotalDiasAlta(int totalDiasAlta) {
        this.totalDiasAlta = totalDiasAlta;
    }

    public int getDiasPluriempleo() {
        return diasPluriempleo;
    }

    public void setDiasPluriempleo(int diasPluriempleo) {
        this.diasPluriempleo = diasPluriempleo;
    }

    public int getAniosAlta() {
        return aniosAlta;
    }

    public void setAniosAlta(int aniosAlta) {
        this.aniosAlta = aniosAlta;
    }

    public int getMesesAlta() {
        return mesesAlta;
    }

    public void setMesesAlta(int mesesAlta) {
        this.mesesAlta = mesesAlta;
    }

    public int getDiasAlta() {
        return diasAlta;
    }

    public void setDiasAlta(int diasAlta) {
        this.diasAlta = diasAlta;
    }

    @Override
    public String toString() {
        return "Totales{" + "totalDiasAlta=" + totalDiasAlta + ", diasPluriempleo=" + diasPluriempleo + ", aniosAlta=" + aniosAlta + ", mesesAlta=" + mesesAlta + ", diasAlta=" + diasAlta + '}';
    }
}
