/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.altia.flexia.integracion.moduloexterno.melanbide73.vo;

/**
 *
 * @author pablo.bugia
 */
public class Usuario {
   private final int usuCod;
   private final String usuLog;
   private final String usuNom;

    public Usuario(int usuCod, String usuLog, String usuNom) {
        this.usuCod = usuCod;
        this.usuLog = usuLog;
        this.usuNom = usuNom;
    }

    public int getUsuCod() {
        return usuCod;
    }

    public String getUsuLog() {
        return usuLog;
    }

    public String getUsuNom() {
        return usuNom;
    }

    @Override
    public String toString() {
        return "Usuario{" + "usuCod=" + usuCod + ", usuLog=" + usuLog + ", usuNom=" + usuNom + '}';
    }
    
    
   
   
}
