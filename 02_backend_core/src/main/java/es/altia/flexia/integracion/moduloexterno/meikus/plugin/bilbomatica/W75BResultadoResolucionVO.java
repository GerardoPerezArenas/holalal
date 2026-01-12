
package es.altia.flexia.integracion.moduloexterno.meikus.plugin.bilbomatica;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for w75BResultadoResolucionVO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="w75BResultadoResolucionVO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codigoError" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="mensaje" type="{http://ws.service.ws.w75b.ejie.com/}w75BDescripcionVO" minOccurs="0"/>
 *         &lt;element name="retorno" type="{http://ws.service.ws.w75b.ejie.com/}w75BResolucionVO" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "w75BResultadoResolucionVO", propOrder = {
    "codigoError",
    "mensaje",
    "retorno"
})
public class W75BResultadoResolucionVO {

    protected String codigoError;
    protected W75BDescripcionVO mensaje;
    protected W75BResolucionVO retorno;

    /**
     * Gets the value of the codigoError property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoError() {
        return codigoError;
    }

    /**
     * Sets the value of the codigoError property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoError(String value) {
        this.codigoError = value;
    }

    /**
     * Gets the value of the mensaje property.
     * 
     * @return
     *     possible object is
     *     {@link W75BDescripcionVO }
     *     
     */
    public W75BDescripcionVO getMensaje() {
        return mensaje;
    }

    /**
     * Sets the value of the mensaje property.
     * 
     * @param value
     *     allowed object is
     *     {@link W75BDescripcionVO }
     *     
     */
    public void setMensaje(W75BDescripcionVO value) {
        this.mensaje = value;
    }

    /**
     * Gets the value of the retorno property.
     * 
     * @return
     *     possible object is
     *     {@link W75BResolucionVO }
     *     
     */
    public W75BResolucionVO getRetorno() {
        return retorno;
    }

    /**
     * Sets the value of the retorno property.
     * 
     * @param value
     *     allowed object is
     *     {@link W75BResolucionVO }
     *     
     */
    public void setRetorno(W75BResolucionVO value) {
        this.retorno = value;
    }

}
