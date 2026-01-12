
package es.altia.flexia.integracion.moduloexterno.meikus.plugin.bilbomatica;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for w75BResolucionVO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="w75BResolucionVO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="anioConvocatoria" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="conceptoResumido" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ejercicioContable" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="envioEIKA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="expEika" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fechaResolucion" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="formaJuridica" type="{http://ws.service.ws.w75b.ejie.com/}w75BFormaJuridicaVO" minOccurs="0"/>
 *         &lt;element name="idAplicacion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idAplicacionDefinitiva" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idConvocatoria" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idDisposicion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idExpediente" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="importeAnios" type="{http://ws.service.ws.w75b.ejie.com/}w75BImporteAniosVO" minOccurs="0"/>
 *         &lt;element name="importeConcedido" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="lineaAyuda" type="{http://ws.service.ws.w75b.ejie.com/}w75BLineaAyudaVO" minOccurs="0"/>
 *         &lt;element name="numeroExpediente" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="pago" type="{http://ws.service.ws.w75b.ejie.com/}w75BPagoVO" minOccurs="0"/>
 *         &lt;element name="tercero" type="{http://ws.service.ws.w75b.ejie.com/}w75BTerceroVO" minOccurs="0"/>
 *         &lt;element name="territorioHistorico" type="{http://ws.service.ws.w75b.ejie.com/}w75BTerritorioHistoricoVO" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "w75BResolucionVO", propOrder = {
    "anioConvocatoria",
    "conceptoResumido",
    "ejercicioContable",
    "envioEIKA",
    "expEika",
    "fechaResolucion",
    "formaJuridica",
    "idAplicacion",
    "idAplicacionDefinitiva",
    "idConvocatoria",
    "idDisposicion",
    "idExpediente",
    "importeAnios",
    "importeConcedido",
    "lineaAyuda",
    "numeroExpediente",
    "pago",
    "tercero",
    "territorioHistorico"
})
public class W75BResolucionVO {

    protected String anioConvocatoria;
    protected String conceptoResumido;
    protected String ejercicioContable;
    protected String envioEIKA;
    protected String expEika;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaResolucion;
    protected W75BFormaJuridicaVO formaJuridica;
    protected String idAplicacion;
    protected String idAplicacionDefinitiva;
    protected String idConvocatoria;
    protected String idDisposicion;
    protected String idExpediente;
    protected W75BImporteAniosVO importeAnios;
    protected double importeConcedido;
    protected W75BLineaAyudaVO lineaAyuda;
    protected String numeroExpediente;
    protected W75BPagoVO pago;
    protected W75BTerceroVO tercero;
    protected W75BTerritorioHistoricoVO territorioHistorico;

    /**
     * Gets the value of the anioConvocatoria property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAnioConvocatoria() {
        return anioConvocatoria;
    }

    /**
     * Sets the value of the anioConvocatoria property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAnioConvocatoria(String value) {
        this.anioConvocatoria = value;
    }

    /**
     * Gets the value of the conceptoResumido property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConceptoResumido() {
        return conceptoResumido;
    }

    /**
     * Sets the value of the conceptoResumido property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConceptoResumido(String value) {
        this.conceptoResumido = value;
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
     * Gets the value of the envioEIKA property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEnvioEIKA() {
        return envioEIKA;
    }

    /**
     * Sets the value of the envioEIKA property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnvioEIKA(String value) {
        this.envioEIKA = value;
    }

    /**
     * Gets the value of the expEika property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExpEika() {
        return expEika;
    }

    /**
     * Sets the value of the expEika property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExpEika(String value) {
        this.expEika = value;
    }

    /**
     * Gets the value of the fechaResolucion property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaResolucion() {
        return fechaResolucion;
    }

    /**
     * Sets the value of the fechaResolucion property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaResolucion(XMLGregorianCalendar value) {
        this.fechaResolucion = value;
    }

    /**
     * Gets the value of the formaJuridica property.
     * 
     * @return
     *     possible object is
     *     {@link W75BFormaJuridicaVO }
     *     
     */
    public W75BFormaJuridicaVO getFormaJuridica() {
        return formaJuridica;
    }

    /**
     * Sets the value of the formaJuridica property.
     * 
     * @param value
     *     allowed object is
     *     {@link W75BFormaJuridicaVO }
     *     
     */
    public void setFormaJuridica(W75BFormaJuridicaVO value) {
        this.formaJuridica = value;
    }

    /**
     * Gets the value of the idAplicacion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdAplicacion() {
        return idAplicacion;
    }

    /**
     * Sets the value of the idAplicacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdAplicacion(String value) {
        this.idAplicacion = value;
    }

    /**
     * Gets the value of the idAplicacionDefinitiva property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdAplicacionDefinitiva() {
        return idAplicacionDefinitiva;
    }

    /**
     * Sets the value of the idAplicacionDefinitiva property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdAplicacionDefinitiva(String value) {
        this.idAplicacionDefinitiva = value;
    }

    /**
     * Gets the value of the idConvocatoria property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdConvocatoria() {
        return idConvocatoria;
    }

    /**
     * Sets the value of the idConvocatoria property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdConvocatoria(String value) {
        this.idConvocatoria = value;
    }

    /**
     * Gets the value of the idDisposicion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdDisposicion() {
        return idDisposicion;
    }

    /**
     * Sets the value of the idDisposicion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdDisposicion(String value) {
        this.idDisposicion = value;
    }

    /**
     * Gets the value of the idExpediente property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdExpediente() {
        return idExpediente;
    }

    /**
     * Sets the value of the idExpediente property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdExpediente(String value) {
        this.idExpediente = value;
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
     * Gets the value of the importeConcedido property.
     * 
     */
    public double getImporteConcedido() {
        return importeConcedido;
    }

    /**
     * Sets the value of the importeConcedido property.
     * 
     */
    public void setImporteConcedido(double value) {
        this.importeConcedido = value;
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

    /**
     * Gets the value of the pago property.
     * 
     * @return
     *     possible object is
     *     {@link W75BPagoVO }
     *     
     */
    public W75BPagoVO getPago() {
        return pago;
    }

    /**
     * Sets the value of the pago property.
     * 
     * @param value
     *     allowed object is
     *     {@link W75BPagoVO }
     *     
     */
    public void setPago(W75BPagoVO value) {
        this.pago = value;
    }

    /**
     * Gets the value of the tercero property.
     * 
     * @return
     *     possible object is
     *     {@link W75BTerceroVO }
     *     
     */
    public W75BTerceroVO getTercero() {
        return tercero;
    }

    /**
     * Sets the value of the tercero property.
     * 
     * @param value
     *     allowed object is
     *     {@link W75BTerceroVO }
     *     
     */
    public void setTercero(W75BTerceroVO value) {
        this.tercero = value;
    }

    /**
     * Gets the value of the territorioHistorico property.
     * 
     * @return
     *     possible object is
     *     {@link W75BTerritorioHistoricoVO }
     *     
     */
    public W75BTerritorioHistoricoVO getTerritorioHistorico() {
        return territorioHistorico;
    }

    /**
     * Sets the value of the territorioHistorico property.
     * 
     * @param value
     *     allowed object is
     *     {@link W75BTerritorioHistoricoVO }
     *     
     */
    public void setTerritorioHistorico(W75BTerritorioHistoricoVO value) {
        this.territorioHistorico = value;
    }

}
