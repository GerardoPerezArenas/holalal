
package es.altia.flexia.integracion.moduloexterno.meikus.plugin.bilbomatica;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for w75BLineaAyudaVO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="w75BLineaAyudaVO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="descripcion" type="{http://ws.service.ws.w75b.ejie.com/}w75BDescripcionVO" minOccurs="0"/>
 *         &lt;element name="idLinea" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipoConcursal" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="ultimoEjercicio" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "w75BLineaAyudaVO", propOrder = {
    "descripcion",
    "idLinea",
    "tipoConcursal",
    "ultimoEjercicio"
})
public class W75BLineaAyudaVO {

    protected W75BDescripcionVO descripcion;
    protected String idLinea;
    protected boolean tipoConcursal;
    protected String ultimoEjercicio;

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

    /**
     * Gets the value of the idLinea property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdLinea() {
        return idLinea;
    }

    /**
     * Sets the value of the idLinea property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdLinea(String value) {
        this.idLinea = value;
    }

    /**
     * Gets the value of the tipoConcursal property.
     * 
     */
    public boolean isTipoConcursal() {
        return tipoConcursal;
    }

    /**
     * Sets the value of the tipoConcursal property.
     * 
     */
    public void setTipoConcursal(boolean value) {
        this.tipoConcursal = value;
    }

    /**
     * Gets the value of the ultimoEjercicio property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUltimoEjercicio() {
        return ultimoEjercicio;
    }

    /**
     * Sets the value of the ultimoEjercicio property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUltimoEjercicio(String value) {
        this.ultimoEjercicio = value;
    }

}
