
package es.altia.flexia.integracion.moduloexterno.meikus.plugin.bilbomatica;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for w75BTerceroIkusVO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="w75BTerceroIkusVO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codigo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cuentaBancaria" type="{http://ws.service.ws.w75b.ejie.com/}w75BCuentaVO" minOccurs="0"/>
 *         &lt;element name="estado" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="nombre" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="versionTercero" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "w75BTerceroIkusVO", propOrder = {
    "codigo",
    "cuentaBancaria",
    "estado",
    "nombre",
    "tipo",
    "versionTercero"
})
public class W75BTerceroIkusVO {

    protected String codigo;
    protected W75BCuentaVO cuentaBancaria;
    protected boolean estado;
    protected String nombre;
    protected String tipo;
    protected String versionTercero;

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
     * Gets the value of the cuentaBancaria property.
     * 
     * @return
     *     possible object is
     *     {@link W75BCuentaVO }
     *     
     */
    public W75BCuentaVO getCuentaBancaria() {
        return cuentaBancaria;
    }

    /**
     * Sets the value of the cuentaBancaria property.
     * 
     * @param value
     *     allowed object is
     *     {@link W75BCuentaVO }
     *     
     */
    public void setCuentaBancaria(W75BCuentaVO value) {
        this.cuentaBancaria = value;
    }

    /**
     * Gets the value of the estado property.
     * 
     */
    public boolean isEstado() {
        return estado;
    }

    /**
     * Sets the value of the estado property.
     * 
     */
    public void setEstado(boolean value) {
        this.estado = value;
    }

    /**
     * Gets the value of the nombre property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Sets the value of the nombre property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombre(String value) {
        this.nombre = value;
    }

    /**
     * Gets the value of the tipo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Sets the value of the tipo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipo(String value) {
        this.tipo = value;
    }

    /**
     * Gets the value of the versionTercero property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersionTercero() {
        return versionTercero;
    }

    /**
     * Sets the value of the versionTercero property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersionTercero(String value) {
        this.versionTercero = value;
    }

}
