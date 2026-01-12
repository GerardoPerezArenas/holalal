/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.altia.flexia.integracion.moduloexterno.melanbide67.vo;

/**
 *
 * @author pbugia
 */
public class DesplegableExternoVO {
    private String vista;
    private String nombreCod;
    private String nombreDesc;

    public DesplegableExternoVO(String vista, String nombreCod, String nombreDesc) {
        this.vista = vista;
        this.nombreCod = nombreCod;
        this.nombreDesc = nombreDesc;
    }

    public String getVista() {
        return vista;
    }

    public void setVista(String vista) {
        this.vista = vista;
    }

    public String getNombreCod() {
        return nombreCod;
    }

    public void setNombreCod(String nombreCod) {
        this.nombreCod = nombreCod;
    }

    public String getNombreDesc() {
        return nombreDesc;
    }

    public void setNombreDesc(String nombreDesc) {
        this.nombreDesc = nombreDesc;
    }
    
}
