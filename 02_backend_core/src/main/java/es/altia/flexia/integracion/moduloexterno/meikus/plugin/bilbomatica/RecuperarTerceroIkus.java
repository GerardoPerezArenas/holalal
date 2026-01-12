
package es.altia.flexia.integracion.moduloexterno.meikus.plugin.bilbomatica;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for recuperarTerceroIkus complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="recuperarTerceroIkus">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="W75bSeguridadVO" type="{http://ws.service.ws.w75b.ejie.com/}w75BSeguridadVO" minOccurs="0"/>
 *         &lt;element name="W75bTerceroIkusVO" type="{http://ws.service.ws.w75b.ejie.com/}w75BTerceroIkusVO" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "recuperarTerceroIkus", propOrder = {
    "w75BSeguridadVO",
    "w75BTerceroIkusVO"
})
public class RecuperarTerceroIkus {

    @XmlElement(name = "W75bSeguridadVO")
    protected W75BSeguridadVO w75BSeguridadVO;
    @XmlElement(name = "W75bTerceroIkusVO")
    protected W75BTerceroIkusVO w75BTerceroIkusVO;

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
     * Gets the value of the w75BTerceroIkusVO property.
     * 
     * @return
     *     possible object is
     *     {@link W75BTerceroIkusVO }
     *     
     */
    public W75BTerceroIkusVO getW75BTerceroIkusVO() {
        return w75BTerceroIkusVO;
    }

    /**
     * Sets the value of the w75BTerceroIkusVO property.
     * 
     * @param value
     *     allowed object is
     *     {@link W75BTerceroIkusVO }
     *     
     */
    public void setW75BTerceroIkusVO(W75BTerceroIkusVO value) {
        this.w75BTerceroIkusVO = value;
    }

}
