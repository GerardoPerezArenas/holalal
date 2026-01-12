
package es.altia.flexia.integracion.moduloexterno.meikus.plugin.bilbomatica;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for recuperarListaAplicaciones complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="recuperarListaAplicaciones">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="W75bSeguridadVO" type="{http://ws.service.ws.w75b.ejie.com/}w75BSeguridadVO" minOccurs="0"/>
 *         &lt;element name="idLineaAyuda" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ejercicioContable" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "recuperarListaAplicaciones", propOrder = {
    "w75BSeguridadVO",
    "idLineaAyuda",
    "ejercicioContable"
})
public class RecuperarListaAplicaciones {

    @XmlElement(name = "W75bSeguridadVO")
    protected W75BSeguridadVO w75BSeguridadVO;
    protected String idLineaAyuda;
    protected String ejercicioContable;

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
     * Gets the value of the idLineaAyuda property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdLineaAyuda() {
        return idLineaAyuda;
    }

    /**
     * Sets the value of the idLineaAyuda property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdLineaAyuda(String value) {
        this.idLineaAyuda = value;
    }

    /**
     * Gets the value of the ejercicioContable property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEjercicioContable() {
        return ejercicioContable;
    }

    /**
     * Sets the value of the ejercicioContable property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEjercicioContable(String value) {
        this.ejercicioContable = value;
    }

}
