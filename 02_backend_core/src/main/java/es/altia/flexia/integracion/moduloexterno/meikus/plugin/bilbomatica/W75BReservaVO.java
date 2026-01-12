
package es.altia.flexia.integracion.moduloexterno.meikus.plugin.bilbomatica;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for w75BReservaVO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="w75BReservaVO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ejercicio" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fechaReserva" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="idReserva" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="importeAnios" type="{http://ws.service.ws.w75b.ejie.com/}w75BImporteAniosVO" minOccurs="0"/>
 *         &lt;element name="lineaAyuda" type="{http://ws.service.ws.w75b.ejie.com/}w75BLineaAyudaVO" minOccurs="0"/>
 *         &lt;element name="numeroExpediente" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "w75BReservaVO", propOrder = {
    "ejercicio",
    "fechaReserva",
    "idReserva",
    "importeAnios",
    "lineaAyuda",
    "numeroExpediente"
})
public class W75BReservaVO {

    protected String ejercicio;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaReserva;
    protected String idReserva;
    protected W75BImporteAniosVO importeAnios;
    protected W75BLineaAyudaVO lineaAyuda;
    protected String numeroExpediente;

    /**
     * Gets the value of the ejercicio property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEjercicio() {
        return ejercicio;
    }

    /**
     * Sets the value of the ejercicio property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEjercicio(String value) {
        this.ejercicio = value;
    }

    /**
     * Gets the value of the fechaReserva property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaReserva() {
        return fechaReserva;
    }

    /**
     * Sets the value of the fechaReserva property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaReserva(XMLGregorianCalendar value) {
        this.fechaReserva = value;
    }

    /**
     * Gets the value of the idReserva property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdReserva() {
        return idReserva;
    }

    /**
     * Sets the value of the idReserva property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdReserva(String value) {
        this.idReserva = value;
    }

    /**
     * Gets the value of the importeAnios property.
     * 
     * @return
     *     possible object is
     *     {@link W75BImporteAniosVO }
     *     
     */
    public W75BImporteAniosVO getImporteAnios() {
        return importeAnios;
    }

    /**
     * Sets the value of the importeAnios property.
     * 
     * @param value
     *     allowed object is
     *     {@link W75BImporteAniosVO }
     *     
     */
    public void setImporteAnios(W75BImporteAniosVO value) {
        this.importeAnios = value;
    }

    /**
     * Gets the value of the lineaAyuda property.
     * 
     * @return
     *     possible object is
     *     {@link W75BLineaAyudaVO }
     *     
     */
    public W75BLineaAyudaVO getLineaAyuda() {
        return lineaAyuda;
    }

    /**
     * Sets the value of the lineaAyuda property.
     * 
     * @param value
     *     allowed object is
     *     {@link W75BLineaAyudaVO }
     *     
     */
    public void setLineaAyuda(W75BLineaAyudaVO value) {
        this.lineaAyuda = value;
    }

    /**
     * Gets the value of the numeroExpediente property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroExpediente() {
        return numeroExpediente;
    }

    /**
     * Sets the value of the numeroExpediente property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroExpediente(String value) {
        this.numeroExpediente = value;
    }

}
