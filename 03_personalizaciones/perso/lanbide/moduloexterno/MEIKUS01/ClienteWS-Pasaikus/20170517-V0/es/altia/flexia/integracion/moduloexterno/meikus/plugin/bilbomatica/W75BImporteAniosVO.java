
package es.altia.flexia.integracion.moduloexterno.meikus.plugin.bilbomatica;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for w75BImporteAniosVO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="w75BImporteAniosVO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="importeAnio1" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="importeAnio2" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="importeAnio3" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="importeAnio4" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="importeAnio5" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="importeRestoAnios" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "w75BImporteAniosVO", propOrder = {
    "importeAnio1",
    "importeAnio2",
    "importeAnio3",
    "importeAnio4",
    "importeAnio5",
    "importeRestoAnios"
})
public class W75BImporteAniosVO {

    protected double importeAnio1;
    protected double importeAnio2;
    protected double importeAnio3;
    protected double importeAnio4;
    protected double importeAnio5;
    protected double importeRestoAnios;

    /**
     * Gets the value of the importeAnio1 property.
     * 
     */
    public double getImporteAnio1() {
        return importeAnio1;
    }

    /**
     * Sets the value of the importeAnio1 property.
     * 
     */
    public void setImporteAnio1(double value) {
        this.importeAnio1 = value;
    }

    /**
     * Gets the value of the importeAnio2 property.
     * 
     */
    public double getImporteAnio2() {
        return importeAnio2;
    }

    /**
     * Sets the value of the importeAnio2 property.
     * 
     */
    public void setImporteAnio2(double value) {
        this.importeAnio2 = value;
    }

    /**
     * Gets the value of the importeAnio3 property.
     * 
     */
    public double getImporteAnio3() {
        return importeAnio3;
    }

    /**
     * Sets the value of the importeAnio3 property.
     * 
     */
    public void setImporteAnio3(double value) {
        this.importeAnio3 = value;
    }

    /**
     * Gets the value of the importeAnio4 property.
     * 
     */
    public double getImporteAnio4() {
        return importeAnio4;
    }

    /**
     * Sets the value of the importeAnio4 property.
     * 
     */
    public void setImporteAnio4(double value) {
        this.importeAnio4 = value;
    }

    /**
     * Gets the value of the importeAnio5 property.
     * 
     */
    public double getImporteAnio5() {
        return importeAnio5;
    }

    /**
     * Sets the value of the importeAnio5 property.
     * 
     */
    public void setImporteAnio5(double value) {
        this.importeAnio5 = value;
    }

    /**
     * Gets the value of the importeRestoAnios property.
     * 
     */
    public double getImporteRestoAnios() {
        return importeRestoAnios;
    }

    /**
     * Sets the value of the importeRestoAnios property.
     * 
     */
    public void setImporteRestoAnios(double value) {
        this.importeRestoAnios = value;
    }

}
