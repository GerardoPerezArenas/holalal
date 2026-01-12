
package es.altia.flexia.integracion.moduloexterno.meikus.plugin.bilbomatica;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for grabarReserva complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="grabarReserva">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="W75bSeguridadVO" type="{http://ws.service.ws.w75b.ejie.com/}w75BSeguridadVO" minOccurs="0"/>
 *         &lt;element name="W75bReservaVO" type="{http://ws.service.ws.w75b.ejie.com/}w75BReservaVO" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "grabarReserva", propOrder = {
    "w75BSeguridadVO",
    "w75BReservaVO"
})
public class GrabarReserva {

    @XmlElement(name = "W75bSeguridadVO")
    protected W75BSeguridadVO w75BSeguridadVO;
    @XmlElement(name = "W75bReservaVO")
    protected W75BReservaVO w75BReservaVO;

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
     * Gets the value of the w75BReservaVO property.
     * 
     * @return
     *     possible object is
     *     {@link W75BReservaVO }
     *     
     */
    public W75BReservaVO getW75BReservaVO() {
        return w75BReservaVO;
    }

    /**
     * Sets the value of the w75BReservaVO property.
     * 
     * @param value
     *     allowed object is
     *     {@link W75BReservaVO }
     *     
     */
    public void setW75BReservaVO(W75BReservaVO value) {
        this.w75BReservaVO = value;
    }

}
