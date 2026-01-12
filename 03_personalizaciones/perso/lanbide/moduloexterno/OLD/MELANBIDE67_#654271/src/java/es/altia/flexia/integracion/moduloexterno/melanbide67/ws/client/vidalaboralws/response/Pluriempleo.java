/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.altia.flexia.integracion.moduloexterno.melanbide67.ws.client.vidalaboralws.response;

/**
 *
 * @author pablo.bugia
 */
public class Pluriempleo {
    private int totalDiasAlta;
    private int aniosAlta;
    private int mesesAlta;
    private int diasAlta;

    public Pluriempleo(int totalDiasAlta, int aniosAlta, int mesesAlta, int diasAlta) {
        this.totalDiasAlta = totalDiasAlta;
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
        return "Pluriempleo{" + "totalDiasAlta=" + totalDiasAlta + ", aniosAlta=" + aniosAlta + ", mesesAlta=" + mesesAlta + ", diasAlta=" + diasAlta + '}';
    }
}
