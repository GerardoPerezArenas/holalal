/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide67.vo;

/**
 *
 * @author INGDGC
 */
public class LlamadaServicioCV {

    private String tipoDoc; // Obligatorio
    private String numDoc;  // Obligatorio
    private String documento; // Obligatorio
    private String idioma; // Obligatorio

    public LlamadaServicioCV() {
    }

    
    public String getTipoDoc() {
        return tipoDoc;
    }

    public void setTipoDoc(String tipoDoc) {
        this.tipoDoc = tipoDoc;
    }

    public String getNumDoc() {
        return numDoc;
    }

    public void setNumDoc(String numDoc) {
        this.numDoc = numDoc;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        /* Para el servicio LangaiVision360 el idioma es:
            C castellano
            E euskera
        */
        if (idioma.equals("1")) {
            this.idioma = "C";
        }
        else if (idioma.equals("4"))
            this.idioma = "E";
        else {
            this.idioma = "C";
        }
    }

    
}
