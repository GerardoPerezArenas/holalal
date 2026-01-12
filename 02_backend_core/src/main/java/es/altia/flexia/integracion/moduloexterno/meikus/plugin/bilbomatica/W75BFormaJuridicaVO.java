
package es.altia.flexia.integracion.moduloexterno.meikus.plugin.bilbomatica;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for w75BFormaJuridicaVO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="w75BFormaJuridicaVO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cacSc" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codigo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="descripcion" type="{http://ws.service.ws.w75b.ejie.com/}w75BDescripcionVO" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "w75BFormaJuridicaVO", propOrder = {
    "cacSc",
    "codigo",
    "descripcion"
})
public class W75BFormaJuridicaVO {

    protected String cacSc;
    protected String codigo;
    protected W75BDescripcionVO descripcion;

    /**
     * Gets the value of the cacSc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCacSc() {
        return cacSc;
    }

    /**
     * Sets the value of the cacSc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCacSc(String value) {
        this.cacSc = value;
    }

    /**
     * Gets the value of the codigo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * Sets the value of the codigo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigo(String value) {
        this.codigo = value;
    }

    /**
     * Gets the value of the descripcion property.
     * 
     * @return
     *     possible object is
     *     {@link W75BDescripcionVO }
     *     
     */
    public W75BDescripcionVO getDescripcion() {
        return descripcion;
    }

    /**
     * Sets the value of the descripcion property.
     * 
     * @param value
     *     allowed object is
     *     {@link W75BDescripcionVO }
     *     
     */
    public void setDescripcion(W75BDescripcionVO value) {
        this.descripcion = value;
    }

}
