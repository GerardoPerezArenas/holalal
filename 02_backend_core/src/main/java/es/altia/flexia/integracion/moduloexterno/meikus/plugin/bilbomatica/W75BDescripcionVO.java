
package es.altia.flexia.integracion.moduloexterno.meikus.plugin.bilbomatica;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for w75BDescripcionVO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="w75BDescripcionVO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="descripcionCastellano" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="descripcionEuskera" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "w75BDescripcionVO", propOrder = {
    "descripcionCastellano",
    "descripcionEuskera"
})
public class W75BDescripcionVO {

    protected String descripcionCastellano;
    protected String descripcionEuskera;

    /**
     * Gets the value of the descripcionCastellano property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescripcionCastellano() {
        return descripcionCastellano;
    }

    /**
     * Sets the value of the descripcionCastellano property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescripcionCastellano(String value) {
        this.descripcionCastellano = value;
    }

    /**
     * Gets the value of the descripcionEuskera property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescripcionEuskera() {
        return descripcionEuskera;
    }

    /**
     * Sets the value of the descripcionEuskera property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescripcionEuskera(String value) {
        this.descripcionEuskera = value;
    }

}
