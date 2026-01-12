
package es.altia.flexia.integracion.moduloexterno.meikus.plugin.bilbomatica;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for recuperarAplicacion complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="recuperarAplicacion">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="W75bSeguridadVO" type="{http://ws.service.ws.w75b.ejie.com/}w75BSeguridadVO" minOccurs="0"/>
 *         &lt;element name="idLineaAyuda" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="numExpediente" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ejercicioContable" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idTerritorio" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idFormaJuridica" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "recuperarAplicacion", propOrder = {
    "w75BSeguridadVO",
    "idLineaAyuda",
    "numExpediente",
    "ejercicioContable",
    "idTerritorio",
    "idFormaJuridica"
})
public class RecuperarAplicacion {

    @XmlElement(name = "W75bSeguridadVO")
    protected W75BSeguridadVO w75BSeguridadVO;
    protected String idLineaAyuda;
    protected String numExpediente;
    protected String ejercicioContable;
    protected String idTerritorio;
    protected String idFormaJuridica;

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
     * Gets the value of the numExpediente property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumExpediente() {
        return numExpediente;
    }

    /**
     * Sets the value of the numExpediente property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumExpediente(String value) {
        this.numExpediente = value;
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

    /**
     * Gets the value of the idTerritorio property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdTerritorio() {
        return idTerritorio;
    }

    /**
     * Sets the value of the idTerritorio property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdTerritorio(String value) {
        this.idTerritorio = value;
    }

    /**
     * Gets the value of the idFormaJuridica property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdFormaJuridica() {
        return idFormaJuridica;
    }

    /**
     * Sets the value of the idFormaJuridica property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdFormaJuridica(String value) {
        this.idFormaJuridica = value;
    }

}
