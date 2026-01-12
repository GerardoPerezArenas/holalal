/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide42.vo;

import java.util.ArrayList;

/**
 *
 * @author mikel
 */
public class MELanbide42ModuloVO {

    private String codigo = null;
    
    private final ArrayList<MELanbide42TablaVO> listaTablas = new ArrayList<MELanbide42TablaVO>();
    
    public MELanbide42ModuloVO (String codigo) {
        this.codigo = codigo;
    }
    
    public String getCodigo (){
        return this.codigo;
    }
    
    public ArrayList<MELanbide42TablaVO> getTablas(){
        return listaTablas;
    }
    
    public void addTabla(MELanbide42TablaVO tabla){
        this.listaTablas.add(tabla);
    }
    
    public void addTabla(int num, MELanbide42TablaVO tabla){
        this.listaTablas.add(num,tabla);
    }
   
    public MELanbide42TablaVO getTableByName(String name){
        for (MELanbide42TablaVO t : this.listaTablas){
            if (t.getNombre().equalsIgnoreCase(name)){
                return t;
            }
        }
        
        return null;
    }
}
