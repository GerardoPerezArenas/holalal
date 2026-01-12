
package es.altia.flexia.integracion.moduloexterno.meikus.plugin.bilbomatica;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for w75BPagoVO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="w75BPagoVO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="idPago" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="importe" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="justificacionObligatoria" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="numeroPago" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipoDocumento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "w75BPagoVO", propOrder = {
    "idPago",
    "importe",
    "justificacionObligatoria",
    "numeroPago",
    "tipoDocumento"
})
public class W75BPagoVO {

    protected String idPago;
    protected double importe;
    protected boolean justificacionObligatoria;
    protected String numeroPago;
    protected String tipoDocumento;

    /**
     * Gets the value of the idPago property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdPago() {
        return idPago;
    }

    /**
     * Sets the value of the idPago property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdPago(String value) {
        this.idPago = value;
    }

    /**
     * Gets the value of the importe property.
     * 
     */
    public double getImporte() {
        return importe;
    }

    /**
     * Sets the value of the importe property.
     * 
     */
    public void setImporte(double value) {
        this.importe = value;
    }

    /**
     * Gets the value of the justificacionObligatoria property.
     * 
     */
    public boolean isJustificacionObligatoria() {
        return justificacionObligatoria;
    }

    /**
     * Sets the value of the justificacionObligatoria property.
     * 
     */
    public void setJustificacionObligatoria(boolean value) {
        this.justificacionObligatoria = value;
    }

    /**
     * Gets the value of the numeroPago property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroPago() {
        return numeroPago;
    }

    /**
     * Sets the value of the numeroPago property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroPago(String value) {
        this.numeroPago = value;
    }

    /**
     * Gets the value of the tipoDocumento property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoDocumento() {
        return tipoDocumento;
    }

    /**
     * Sets the value of the tipoDocumento property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoDocumento(String value) {
        this.tipoDocumento = value;
    }

}
