/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide32.vo.orientacion;

/**
 *
 * @author santiagoc
 */
public class FilaTrayectoriaOrientacionVO 
{
    private Integer oriOritrayCod = -1;
    private String descTrayectoria = "";
    private String duracionServicio = "";
    private String organismo = "";
    
    public FilaTrayectoriaOrientacionVO()
    {
        
    }

    public Integer getOriOritrayCod() {
        return oriOritrayCod;
    }

    public void setOriOritrayCod(Integer oriOritrayCod) {
        this.oriOritrayCod = oriOritrayCod;
    }

    public String getDescTrayectoria() {
        return descTrayectoria;
    }

    public void setDescTrayectoria(String descTrayectoria) {
        this.descTrayectoria = descTrayectoria;
    }

    public String getDuracionServicio() {
        return duracionServicio;
    }

    public void setDuracionServicio(String duracionServicio) {
        this.duracionServicio = duracionServicio;
    }

    public String getOrganismo() {
        return organismo;
    }

    public void setOrganismo(String organismo) {
        this.organismo = organismo;
    }
}
