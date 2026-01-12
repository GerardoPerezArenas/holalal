/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide58.vo;

import java.sql.Date;


/**
 *
 * @author altia
 */
public class CausaAltaBajaVO {
    
    private Integer id;
    private String desCod;
    private String desValCod;
    private String des_nombre;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getDesCod() {
        return desCod;
    }

    public void setDesCod(String desCod) {
        this.desCod = desCod;
    }
    
    public String getDesValCod() {
        return desValCod;
    }

    public void setDesValCod(String desValCod) {
        this.desValCod = desValCod;
    }

    public String getDes_nombre() {
        return des_nombre;
    }

    public void setDes_nombre(String des_nombre) {
        this.des_nombre = des_nombre;
    }
    
}
