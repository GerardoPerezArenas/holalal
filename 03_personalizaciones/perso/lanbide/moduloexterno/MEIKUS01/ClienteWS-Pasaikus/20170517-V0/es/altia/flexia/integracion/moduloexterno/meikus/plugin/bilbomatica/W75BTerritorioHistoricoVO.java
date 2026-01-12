
package es.altia.flexia.integracion.moduloexterno.meikus.plugin.bilbomatica;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for w75BTerritorioHistoricoVO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="w75BTerritorioHistoricoVO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="descripcion" type="{http://ws.service.ws.w75b.ejie.com/}w75BDescripcionVO" minOccurs="0"/>
 *         &lt;element name="idTerritorio" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "w75BTerritorioHistoricoVO", propOrder = {
    "descripcion",
    "idTerritorio"
})
public class W75BTerritorioHistoricoVO {

    protected W75BDescripcionVO descripcion;
    protected String idTerritorio;

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

}
