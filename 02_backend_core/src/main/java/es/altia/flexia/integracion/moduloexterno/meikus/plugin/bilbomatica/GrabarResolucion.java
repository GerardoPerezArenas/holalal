
package es.altia.flexia.integracion.moduloexterno.meikus.plugin.bilbomatica;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for grabarResolucion complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="grabarResolucion">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="W75bSeguridadVO" type="{http://ws.service.ws.w75b.ejie.com/}w75BSeguridadVO" minOccurs="0"/>
 *         &lt;element name="W75bResolucionVO" type="{http://ws.service.ws.w75b.ejie.com/}w75BResolucionVO" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "grabarResolucion", propOrder = {
    "w75BSeguridadVO",
    "w75BResolucionVO"
})
public class GrabarResolucion {

    @XmlElement(name = "W75bSeguridadVO")
    protected W75BSeguridadVO w75BSeguridadVO;
    @XmlElement(name = "W75bResolucionVO")
    protected W75BResolucionVO w75BResolucionVO;

    /**
     * Gets the value of the w75BSeguridadVO property.
     * 
     * @return
     *     possible object is
     *     {@link W75BSeguridadVO }
     *     
     */
    public W75BSeguridadVO getW75BSeguridadVO() {
        return w75BSeguridadVO;
    }

    /**
     * Sets the value of the w75BSeguridadVO property.
     * 
     * @param value
     *     allowed object is
     *     {@link W75BSeguridadVO }
     *     
     */
    public void setW75BSeguridadVO(W75BSeguridadVO value) {
        this.w75BSeguridadVO = value;
    }

    /**
     * Gets the value of the w75BResolucionVO property.
     * 
     * @return
     *     possible object is
     *     {@link W75BResolucionVO }
     *     
     */
    public W75BResolucionVO getW75BResolucionVO() {
        return w75BResolucionVO;
    }

    /**
     * Sets the value of the w75BResolucionVO property.
     * 
     * @param value
     *     allowed object is
     *     {@link W75BResolucionVO }
     *     
     */
    public void setW75BResolucionVO(W75BResolucionVO value) {
        this.w75BResolucionVO = value;
    }

}
