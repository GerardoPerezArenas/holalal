/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide48.vo;

/**
 *
 * @author santiagoc
 */
public class ColecRepresentanteVO 
{
    private Long codRepresentante;
    private String numExp;
    private Integer ejercicio;
    private String codTipoDoc;
    private String documento;
    private String nombre;
    
    public ColecRepresentanteVO()
    {
        
    }

    public Long getCodRepresentante() {
        return codRepresentante;
    }

    public void setCodRepresentante(Long codRepresentante) {
        this.codRepresentante = codRepresentante;
    }

    public String getNumExp() {
        return numExp;
    }

    public void setNumExp(String numExp) {
        this.numExp = numExp;
    }

    public Integer getEjercicio() {
        return ejercicio;
    }

    public void setEjercicio(Integer ejercicio) {
        this.ejercicio = ejercicio;
    }

    public String getCodTipoDoc() {
        return codTipoDoc;
    }

    public void setCodTipoDoc(String codTipoDoc) {
        this.codTipoDoc = codTipoDoc;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

}
