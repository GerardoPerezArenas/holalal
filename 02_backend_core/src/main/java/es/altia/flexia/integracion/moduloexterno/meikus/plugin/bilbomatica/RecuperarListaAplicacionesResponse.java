
package es.altia.flexia.integracion.moduloexterno.meikus.plugin.bilbomatica;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for recuperarListaAplicacionesResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="recuperarListaAplicacionesResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="return" type="{http://ws.service.ws.w75b.ejie.com/}w75BResultadoListaAplicacionesVO" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "recuperarListaAplicacionesResponse", propOrder = {
    "_return"
})
public class RecuperarListaAplicacionesResponse {

    @XmlElement(name = "return")
    protected W75BResultadoListaAplicacionesVO _return;

    /**
     * Gets the value of the return property.
     * 
     * @return
     *     possible object is
     *     {@link W75BResultadoListaAplicacionesVO }
     *     
     */
    public W75BResultadoListaAplicacionesVO getReturn() {
        return _return;
    }

    /**
     * Sets the value of the return property.
     * 
     * @param value
     *     allowed object is
     *     {@link W75BResultadoListaAplicacionesVO }
     *     
     */
    public void setReturn(W75BResultadoListaAplicacionesVO value) {
        this._return = value;
    }

}
