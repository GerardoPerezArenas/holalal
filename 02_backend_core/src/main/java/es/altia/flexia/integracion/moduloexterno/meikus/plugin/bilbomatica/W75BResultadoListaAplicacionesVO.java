
package es.altia.flexia.integracion.moduloexterno.meikus.plugin.bilbomatica;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for w75BResultadoListaAplicacionesVO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="w75BResultadoListaAplicacionesVO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codigoError" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="mensaje" type="{http://ws.service.ws.w75b.ejie.com/}w75BDescripcionVO" minOccurs="0"/>
 *         &lt;element name="retorno" type="{http://ws.service.ws.w75b.ejie.com/}w75BAplicacionVO" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "w75BResultadoListaAplicacionesVO", propOrder = {
    "codigoError",
    "mensaje",
    "retorno"
})
public class W75BResultadoListaAplicacionesVO {

    protected String codigoError;
    protected W75BDescripcionVO mensaje;
    @XmlElement(nillable = true)
    protected List<W75BAplicacionVO> retorno;

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
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the retorno property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRetorno().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link W75BAplicacionVO }
     * 
     * 
     */
    public List<W75BAplicacionVO> getRetorno() {
        if (retorno == null) {
            retorno = new ArrayList<W75BAplicacionVO>();
        }
        return this.retorno;
    }

}
