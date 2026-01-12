
package es.altia.flexia.integracion.moduloexterno.melanbide54.cliente.altaagencia;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AREA-COMUNICACION">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="AD-COD-CONTROL-ENTIREX">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                         &lt;totalDigits value="7"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="ADC-AREA-CONTROL">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="ADC-COD-RETORNO">
 *                               &lt;simpleType>
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   &lt;length value="1"/>
 *                                 &lt;/restriction>
 *                               &lt;/simpleType>
 *                             &lt;/element>
 *                             &lt;element name="ADC-LIT-ERROR">
 *                               &lt;simpleType>
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   &lt;length value="100"/>
 *                                 &lt;/restriction>
 *                               &lt;/simpleType>
 *                             &lt;/element>
 *                             &lt;element name="ADC-COD-ERROR">
 *                               &lt;simpleType>
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   &lt;length value="10"/>
 *                                 &lt;/restriction>
 *                               &lt;/simpleType>
 *                             &lt;/element>
 *                             &lt;element name="ADC-PROG-PRINC">
 *                               &lt;simpleType>
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   &lt;length value="8"/>
 *                                 &lt;/restriction>
 *                               &lt;/simpleType>
 *                             &lt;/element>
 *                             &lt;element name="ADC-CANAL-ORIGEN">
 *                               &lt;simpleType>
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   &lt;length value="1"/>
 *                                 &lt;/restriction>
 *                               &lt;/simpleType>
 *                             &lt;/element>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="ADE-DATOS-ENTRADA">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="ADE-USUARIO">
 *                               &lt;simpleType>
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   &lt;length value="10"/>
 *                                 &lt;/restriction>
 *                               &lt;/simpleType>
 *                             &lt;/element>
 *                             &lt;element name="ADE-NIF-A">
 *                               &lt;simpleType>
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   &lt;length value="9"/>
 *                                 &lt;/restriction>
 *                               &lt;/simpleType>
 *                             &lt;/element>
 *                             &lt;element name="ADE-DENOMINACION">
 *                               &lt;simpleType>
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   &lt;length value="55"/>
 *                                 &lt;/restriction>
 *                               &lt;/simpleType>
 *                             &lt;/element>
 *                             &lt;element name="ADE-ID-COAUT-A">
 *                               &lt;simpleType>
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   &lt;length value="2"/>
 *                                 &lt;/restriction>
 *                               &lt;/simpleType>
 *                             &lt;/element>
 *                             &lt;element name="ADE-FX-AUTOZ-A">
 *                               &lt;simpleType>
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   &lt;length value="8"/>
 *                                 &lt;/restriction>
 *                               &lt;/simpleType>
 *                             &lt;/element>
 *                             &lt;element name="ADE-TIP-VIA">
 *                               &lt;simpleType>
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   &lt;length value="2"/>
 *                                 &lt;/restriction>
 *                               &lt;/simpleType>
 *                             &lt;/element>
 *                             &lt;element name="ADE-NOM-VIA">
 *                               &lt;simpleType>
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   &lt;length value="50"/>
 *                                 &lt;/restriction>
 *                               &lt;/simpleType>
 *                             &lt;/element>
 *                             &lt;element name="ADE-NUMERO">
 *                               &lt;simpleType>
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   &lt;length value="4"/>
 *                                 &lt;/restriction>
 *                               &lt;/simpleType>
 *                             &lt;/element>
 *                             &lt;element name="ADE-BIS-DUP">
 *                               &lt;simpleType>
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   &lt;length value="2"/>
 *                                 &lt;/restriction>
 *                               &lt;/simpleType>
 *                             &lt;/element>
 *                             &lt;element name="ADE-ESCALERA">
 *                               &lt;simpleType>
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   &lt;length value="2"/>
 *                                 &lt;/restriction>
 *                               &lt;/simpleType>
 *                             &lt;/element>
 *                             &lt;element name="ADE-PISO">
 *                               &lt;simpleType>
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   &lt;length value="2"/>
 *                                 &lt;/restriction>
 *                               &lt;/simpleType>
 *                             &lt;/element>
 *                             &lt;element name="ADE-LETRA-PUERTA">
 *                               &lt;simpleType>
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   &lt;length value="3"/>
 *                                 &lt;/restriction>
 *                               &lt;/simpleType>
 *                             &lt;/element>
 *                             &lt;element name="ADE-MUNICIPIO">
 *                               &lt;simpleType>
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                                   &lt;totalDigits value="5"/>
 *                                 &lt;/restriction>
 *                               &lt;/simpleType>
 *                             &lt;/element>
 *                             &lt;element name="ADE-COD-POSTAL">
 *                               &lt;simpleType>
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                                   &lt;totalDigits value="5"/>
 *                                 &lt;/restriction>
 *                               &lt;/simpleType>
 *                             &lt;/element>
 *                             &lt;element name="ADE-TELEFONO">
 *                               &lt;simpleType>
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   &lt;length value="15"/>
 *                                 &lt;/restriction>
 *                               &lt;/simpleType>
 *                             &lt;/element>
 *                             &lt;element name="ADE-FAX">
 *                               &lt;simpleType>
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   &lt;length value="15"/>
 *                                 &lt;/restriction>
 *                               &lt;/simpleType>
 *                             &lt;/element>
 *                             &lt;element name="ADE-MAIL">
 *                               &lt;simpleType>
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   &lt;length value="70"/>
 *                                 &lt;/restriction>
 *                               &lt;/simpleType>
 *                             &lt;/element>
 *                             &lt;element name="ADE-PAGINA-WEB">
 *                               &lt;simpleType>
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   &lt;length value="100"/>
 *                                 &lt;/restriction>
 *                               &lt;/simpleType>
 *                             &lt;/element>
 *                             &lt;element name="ADE-SW-MAIL">
 *                               &lt;simpleType>
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                                   &lt;totalDigits value="1"/>
 *                                 &lt;/restriction>
 *                               &lt;/simpleType>
 *                             &lt;/element>
 *                             &lt;element name="ADE-SW-SMS">
 *                               &lt;simpleType>
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                                   &lt;totalDigits value="1"/>
 *                                 &lt;/restriction>
 *                               &lt;/simpleType>
 *                             &lt;/element>
 *                             &lt;element name="ADE-COD-EXPED">
 *                               &lt;simpleType>
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   &lt;length value="50"/>
 *                                 &lt;/restriction>
 *                               &lt;/simpleType>
 *                             &lt;/element>
 *                             &lt;element name="ADE-FX-RESOLUCION">
 *                               &lt;simpleType>
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   &lt;length value="8"/>
 *                                 &lt;/restriction>
 *                               &lt;/simpleType>
 *                             &lt;/element>
 *                             &lt;element name="ADE-TIP-AGENCIA">
 *                               &lt;simpleType>
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   &lt;length value="1"/>
 *                                 &lt;/restriction>
 *                               &lt;/simpleType>
 *                             &lt;/element>
 *                             &lt;element name="ADE-IN-ETT">
 *                               &lt;simpleType>
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   &lt;length value="1"/>
 *                                 &lt;/restriction>
 *                               &lt;/simpleType>
 *                             &lt;/element>
 *                             &lt;element name="ADE-MAIL2">
 *                               &lt;simpleType>
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   &lt;length value="70"/>
 *                                 &lt;/restriction>
 *                               &lt;/simpleType>
 *                             &lt;/element>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="ADS-DATOS-SALIDA">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="ADS-COD-COLOCACION">
 *                               &lt;simpleType>
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   &lt;length value="10"/>
 *                                 &lt;/restriction>
 *                               &lt;/simpleType>
 *                             &lt;/element>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "areacomunicacion"
})
@XmlRootElement(name = "IWACALASResponse")
public class IWACALASResponse {

    @XmlElement(name = "AREA-COMUNICACION", required = true)
    protected IWACALASResponse.AREACOMUNICACION areacomunicacion;

    /**
     * Gets the value of the areacomunicacion property.
     * 
     * @return
     *     possible object is
     *     {@link IWACALASResponse.AREACOMUNICACION }
     *     
     */
    public IWACALASResponse.AREACOMUNICACION getAREACOMUNICACION() {
        return areacomunicacion;
    }

    /**
     * Sets the value of the areacomunicacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link IWACALASResponse.AREACOMUNICACION }
     *     
     */
    public void setAREACOMUNICACION(IWACALASResponse.AREACOMUNICACION value) {
        this.areacomunicacion = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="AD-COD-CONTROL-ENTIREX">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
     *               &lt;totalDigits value="7"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="ADC-AREA-CONTROL">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="ADC-COD-RETORNO">
     *                     &lt;simpleType>
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         &lt;length value="1"/>
     *                       &lt;/restriction>
     *                     &lt;/simpleType>
     *                   &lt;/element>
     *                   &lt;element name="ADC-LIT-ERROR">
     *                     &lt;simpleType>
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         &lt;length value="100"/>
     *                       &lt;/restriction>
     *                     &lt;/simpleType>
     *                   &lt;/element>
     *                   &lt;element name="ADC-COD-ERROR">
     *                     &lt;simpleType>
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         &lt;length value="10"/>
     *                       &lt;/restriction>
     *                     &lt;/simpleType>
     *                   &lt;/element>
     *                   &lt;element name="ADC-PROG-PRINC">
     *                     &lt;simpleType>
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         &lt;length value="8"/>
     *                       &lt;/restriction>
     *                     &lt;/simpleType>
     *                   &lt;/element>
     *                   &lt;element name="ADC-CANAL-ORIGEN">
     *                     &lt;simpleType>
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         &lt;length value="1"/>
     *                       &lt;/restriction>
     *                     &lt;/simpleType>
     *                   &lt;/element>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="ADE-DATOS-ENTRADA">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="ADE-USUARIO">
     *                     &lt;simpleType>
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         &lt;length value="10"/>
     *                       &lt;/restriction>
     *                     &lt;/simpleType>
     *                   &lt;/element>
     *                   &lt;element name="ADE-NIF-A">
     *                     &lt;simpleType>
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         &lt;length value="9"/>
     *                       &lt;/restriction>
     *                     &lt;/simpleType>
     *                   &lt;/element>
     *                   &lt;element name="ADE-DENOMINACION">
     *                     &lt;simpleType>
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         &lt;length value="55"/>
     *                       &lt;/restriction>
     *                     &lt;/simpleType>
     *                   &lt;/element>
     *                   &lt;element name="ADE-ID-COAUT-A">
     *                     &lt;simpleType>
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         &lt;length value="2"/>
     *                       &lt;/restriction>
     *                     &lt;/simpleType>
     *                   &lt;/element>
     *                   &lt;element name="ADE-FX-AUTOZ-A">
     *                     &lt;simpleType>
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         &lt;length value="8"/>
     *                       &lt;/restriction>
     *                     &lt;/simpleType>
     *                   &lt;/element>
     *                   &lt;element name="ADE-TIP-VIA">
     *                     &lt;simpleType>
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         &lt;length value="2"/>
     *                       &lt;/restriction>
     *                     &lt;/simpleType>
     *                   &lt;/element>
     *                   &lt;element name="ADE-NOM-VIA">
     *                     &lt;simpleType>
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         &lt;length value="50"/>
     *                       &lt;/restriction>
     *                     &lt;/simpleType>
     *                   &lt;/element>
     *                   &lt;element name="ADE-NUMERO">
     *                     &lt;simpleType>
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         &lt;length value="4"/>
     *                       &lt;/restriction>
     *                     &lt;/simpleType>
     *                   &lt;/element>
     *                   &lt;element name="ADE-BIS-DUP">
     *                     &lt;simpleType>
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         &lt;length value="2"/>
     *                       &lt;/restriction>
     *                     &lt;/simpleType>
     *                   &lt;/element>
     *                   &lt;element name="ADE-ESCALERA">
     *                     &lt;simpleType>
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         &lt;length value="2"/>
     *                       &lt;/restriction>
     *                     &lt;/simpleType>
     *                   &lt;/element>
     *                   &lt;element name="ADE-PISO">
     *                     &lt;simpleType>
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         &lt;length value="2"/>
     *                       &lt;/restriction>
     *                     &lt;/simpleType>
     *                   &lt;/element>
     *                   &lt;element name="ADE-LETRA-PUERTA">
     *                     &lt;simpleType>
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         &lt;length value="3"/>
     *                       &lt;/restriction>
     *                     &lt;/simpleType>
     *                   &lt;/element>
     *                   &lt;element name="ADE-MUNICIPIO">
     *                     &lt;simpleType>
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
     *                         &lt;totalDigits value="5"/>
     *                       &lt;/restriction>
     *                     &lt;/simpleType>
     *                   &lt;/element>
     *                   &lt;element name="ADE-COD-POSTAL">
     *                     &lt;simpleType>
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
     *                         &lt;totalDigits value="5"/>
     *                       &lt;/restriction>
     *                     &lt;/simpleType>
     *                   &lt;/element>
     *                   &lt;element name="ADE-TELEFONO">
     *                     &lt;simpleType>
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         &lt;length value="15"/>
     *                       &lt;/restriction>
     *                     &lt;/simpleType>
     *                   &lt;/element>
     *                   &lt;element name="ADE-FAX">
     *                     &lt;simpleType>
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         &lt;length value="15"/>
     *                       &lt;/restriction>
     *                     &lt;/simpleType>
     *                   &lt;/element>
     *                   &lt;element name="ADE-MAIL">
     *                     &lt;simpleType>
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         &lt;length value="70"/>
     *                       &lt;/restriction>
     *                     &lt;/simpleType>
     *                   &lt;/element>
     *                   &lt;element name="ADE-PAGINA-WEB">
     *                     &lt;simpleType>
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         &lt;length value="100"/>
     *                       &lt;/restriction>
     *                     &lt;/simpleType>
     *                   &lt;/element>
     *                   &lt;element name="ADE-SW-MAIL">
     *                     &lt;simpleType>
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
     *                         &lt;totalDigits value="1"/>
     *                       &lt;/restriction>
     *                     &lt;/simpleType>
     *                   &lt;/element>
     *                   &lt;element name="ADE-SW-SMS">
     *                     &lt;simpleType>
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
     *                         &lt;totalDigits value="1"/>
     *                       &lt;/restriction>
     *                     &lt;/simpleType>
     *                   &lt;/element>
     *                   &lt;element name="ADE-COD-EXPED">
     *                     &lt;simpleType>
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         &lt;length value="50"/>
     *                       &lt;/restriction>
     *                     &lt;/simpleType>
     *                   &lt;/element>
     *                   &lt;element name="ADE-FX-RESOLUCION">
     *                     &lt;simpleType>
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         &lt;length value="8"/>
     *                       &lt;/restriction>
     *                     &lt;/simpleType>
     *                   &lt;/element>
     *                   &lt;element name="ADE-TIP-AGENCIA">
     *                     &lt;simpleType>
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         &lt;length value="1"/>
     *                       &lt;/restriction>
     *                     &lt;/simpleType>
     *                   &lt;/element>
     *                   &lt;element name="ADE-IN-ETT">
     *                     &lt;simpleType>
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         &lt;length value="1"/>
     *                       &lt;/restriction>
     *                     &lt;/simpleType>
     *                   &lt;/element>
     *                   &lt;element name="ADE-MAIL2">
     *                     &lt;simpleType>
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         &lt;length value="70"/>
     *                       &lt;/restriction>
     *                     &lt;/simpleType>
     *                   &lt;/element>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="ADS-DATOS-SALIDA">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="ADS-COD-COLOCACION">
     *                     &lt;simpleType>
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         &lt;length value="10"/>
     *                       &lt;/restriction>
     *                     &lt;/simpleType>
     *                   &lt;/element>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "adcodcontrolentirex",
        "adcareacontrol",
        "adedatosentrada",
        "adsdatossalida"
    })
    public static class AREACOMUNICACION {

        @XmlElement(name = "AD-COD-CONTROL-ENTIREX", required = true)
        protected BigDecimal adcodcontrolentirex;
        @XmlElement(name = "ADC-AREA-CONTROL", required = true)
        protected IWACALASResponse.AREACOMUNICACION.ADCAREACONTROL adcareacontrol;
        @XmlElement(name = "ADE-DATOS-ENTRADA", required = true)
        protected IWACALASResponse.AREACOMUNICACION.ADEDATOSENTRADA adedatosentrada;
        @XmlElement(name = "ADS-DATOS-SALIDA", required = true)
        protected IWACALASResponse.AREACOMUNICACION.ADSDATOSSALIDA adsdatossalida;

        /**
         * Gets the value of the adcodcontrolentirex property.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getADCODCONTROLENTIREX() {
            return adcodcontrolentirex;
        }

        /**
         * Sets the value of the adcodcontrolentirex property.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setADCODCONTROLENTIREX(BigDecimal value) {
            this.adcodcontrolentirex = value;
        }

        /**
         * Gets the value of the adcareacontrol property.
         * 
         * @return
         *     possible object is
         *     {@link IWACALASResponse.AREACOMUNICACION.ADCAREACONTROL }
         *     
         */
        public IWACALASResponse.AREACOMUNICACION.ADCAREACONTROL getADCAREACONTROL() {
            return adcareacontrol;
        }

        /**
         * Sets the value of the adcareacontrol property.
         * 
         * @param value
         *     allowed object is
         *     {@link IWACALASResponse.AREACOMUNICACION.ADCAREACONTROL }
         *     
         */
        public void setADCAREACONTROL(IWACALASResponse.AREACOMUNICACION.ADCAREACONTROL value) {
            this.adcareacontrol = value;
        }

        /**
         * Gets the value of the adedatosentrada property.
         * 
         * @return
         *     possible object is
         *     {@link IWACALASResponse.AREACOMUNICACION.ADEDATOSENTRADA }
         *     
         */
        public IWACALASResponse.AREACOMUNICACION.ADEDATOSENTRADA getADEDATOSENTRADA() {
            return adedatosentrada;
        }

        /**
         * Sets the value of the adedatosentrada property.
         * 
         * @param value
         *     allowed object is
         *     {@link IWACALASResponse.AREACOMUNICACION.ADEDATOSENTRADA }
         *     
         */
        public void setADEDATOSENTRADA(IWACALASResponse.AREACOMUNICACION.ADEDATOSENTRADA value) {
            this.adedatosentrada = value;
        }

        /**
         * Gets the value of the adsdatossalida property.
         * 
         * @return
         *     possible object is
         *     {@link IWACALASResponse.AREACOMUNICACION.ADSDATOSSALIDA }
         *     
         */
        public IWACALASResponse.AREACOMUNICACION.ADSDATOSSALIDA getADSDATOSSALIDA() {
            return adsdatossalida;
        }

        /**
         * Sets the value of the adsdatossalida property.
         * 
         * @param value
         *     allowed object is
         *     {@link IWACALASResponse.AREACOMUNICACION.ADSDATOSSALIDA }
         *     
         */
        public void setADSDATOSSALIDA(IWACALASResponse.AREACOMUNICACION.ADSDATOSSALIDA value) {
            this.adsdatossalida = value;
        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="ADC-COD-RETORNO">
         *           &lt;simpleType>
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               &lt;length value="1"/>
         *             &lt;/restriction>
         *           &lt;/simpleType>
         *         &lt;/element>
         *         &lt;element name="ADC-LIT-ERROR">
         *           &lt;simpleType>
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               &lt;length value="100"/>
         *             &lt;/restriction>
         *           &lt;/simpleType>
         *         &lt;/element>
         *         &lt;element name="ADC-COD-ERROR">
         *           &lt;simpleType>
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               &lt;length value="10"/>
         *             &lt;/restriction>
         *           &lt;/simpleType>
         *         &lt;/element>
         *         &lt;element name="ADC-PROG-PRINC">
         *           &lt;simpleType>
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               &lt;length value="8"/>
         *             &lt;/restriction>
         *           &lt;/simpleType>
         *         &lt;/element>
         *         &lt;element name="ADC-CANAL-ORIGEN">
         *           &lt;simpleType>
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               &lt;length value="1"/>
         *             &lt;/restriction>
         *           &lt;/simpleType>
         *         &lt;/element>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "adccodretorno",
            "adcliterror",
            "adccoderror",
            "adcprogprinc",
            "adccanalorigen"
        })
        public static class ADCAREACONTROL {

            @XmlElement(name = "ADC-COD-RETORNO", required = true)
            protected String adccodretorno;
            @XmlElement(name = "ADC-LIT-ERROR", required = true)
            protected String adcliterror;
            @XmlElement(name = "ADC-COD-ERROR", required = true)
            protected String adccoderror;
            @XmlElement(name = "ADC-PROG-PRINC", required = true)
            protected String adcprogprinc;
            @XmlElement(name = "ADC-CANAL-ORIGEN", required = true)
            protected String adccanalorigen;

            /**
             * Gets the value of the adccodretorno property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getADCCODRETORNO() {
                return adccodretorno;
            }

            /**
             * Sets the value of the adccodretorno property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setADCCODRETORNO(String value) {
                this.adccodretorno = value;
            }

            /**
             * Gets the value of the adcliterror property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getADCLITERROR() {
                return adcliterror;
            }

            /**
             * Sets the value of the adcliterror property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setADCLITERROR(String value) {
                this.adcliterror = value;
            }

            /**
             * Gets the value of the adccoderror property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getADCCODERROR() {
                return adccoderror;
            }

            /**
             * Sets the value of the adccoderror property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setADCCODERROR(String value) {
                this.adccoderror = value;
            }

            /**
             * Gets the value of the adcprogprinc property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getADCPROGPRINC() {
                return adcprogprinc;
            }

            /**
             * Sets the value of the adcprogprinc property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setADCPROGPRINC(String value) {
                this.adcprogprinc = value;
            }

            /**
             * Gets the value of the adccanalorigen property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getADCCANALORIGEN() {
                return adccanalorigen;
            }

            /**
             * Sets the value of the adccanalorigen property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setADCCANALORIGEN(String value) {
                this.adccanalorigen = value;
            }

        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="ADE-USUARIO">
         *           &lt;simpleType>
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               &lt;length value="10"/>
         *             &lt;/restriction>
         *           &lt;/simpleType>
         *         &lt;/element>
         *         &lt;element name="ADE-NIF-A">
         *           &lt;simpleType>
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               &lt;length value="9"/>
         *             &lt;/restriction>
         *           &lt;/simpleType>
         *         &lt;/element>
         *         &lt;element name="ADE-DENOMINACION">
         *           &lt;simpleType>
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               &lt;length value="55"/>
         *             &lt;/restriction>
         *           &lt;/simpleType>
         *         &lt;/element>
         *         &lt;element name="ADE-ID-COAUT-A">
         *           &lt;simpleType>
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               &lt;length value="2"/>
         *             &lt;/restriction>
         *           &lt;/simpleType>
         *         &lt;/element>
         *         &lt;element name="ADE-FX-AUTOZ-A">
         *           &lt;simpleType>
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               &lt;length value="8"/>
         *             &lt;/restriction>
         *           &lt;/simpleType>
         *         &lt;/element>
         *         &lt;element name="ADE-TIP-VIA">
         *           &lt;simpleType>
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               &lt;length value="2"/>
         *             &lt;/restriction>
         *           &lt;/simpleType>
         *         &lt;/element>
         *         &lt;element name="ADE-NOM-VIA">
         *           &lt;simpleType>
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               &lt;length value="50"/>
         *             &lt;/restriction>
         *           &lt;/simpleType>
         *         &lt;/element>
         *         &lt;element name="ADE-NUMERO">
         *           &lt;simpleType>
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               &lt;length value="4"/>
         *             &lt;/restriction>
         *           &lt;/simpleType>
         *         &lt;/element>
         *         &lt;element name="ADE-BIS-DUP">
         *           &lt;simpleType>
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               &lt;length value="2"/>
         *             &lt;/restriction>
         *           &lt;/simpleType>
         *         &lt;/element>
         *         &lt;element name="ADE-ESCALERA">
         *           &lt;simpleType>
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               &lt;length value="2"/>
         *             &lt;/restriction>
         *           &lt;/simpleType>
         *         &lt;/element>
         *         &lt;element name="ADE-PISO">
         *           &lt;simpleType>
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               &lt;length value="2"/>
         *             &lt;/restriction>
         *           &lt;/simpleType>
         *         &lt;/element>
         *         &lt;element name="ADE-LETRA-PUERTA">
         *           &lt;simpleType>
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               &lt;length value="3"/>
         *             &lt;/restriction>
         *           &lt;/simpleType>
         *         &lt;/element>
         *         &lt;element name="ADE-MUNICIPIO">
         *           &lt;simpleType>
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
         *               &lt;totalDigits value="5"/>
         *             &lt;/restriction>
         *           &lt;/simpleType>
         *         &lt;/element>
         *         &lt;element name="ADE-COD-POSTAL">
         *           &lt;simpleType>
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
         *               &lt;totalDigits value="5"/>
         *             &lt;/restriction>
         *           &lt;/simpleType>
         *         &lt;/element>
         *         &lt;element name="ADE-TELEFONO">
         *           &lt;simpleType>
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               &lt;length value="15"/>
         *             &lt;/restriction>
         *           &lt;/simpleType>
         *         &lt;/element>
         *         &lt;element name="ADE-FAX">
         *           &lt;simpleType>
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               &lt;length value="15"/>
         *             &lt;/restriction>
         *           &lt;/simpleType>
         *         &lt;/element>
         *         &lt;element name="ADE-MAIL">
         *           &lt;simpleType>
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               &lt;length value="70"/>
         *             &lt;/restriction>
         *           &lt;/simpleType>
         *         &lt;/element>
         *         &lt;element name="ADE-PAGINA-WEB">
         *           &lt;simpleType>
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               &lt;length value="100"/>
         *             &lt;/restriction>
         *           &lt;/simpleType>
         *         &lt;/element>
         *         &lt;element name="ADE-SW-MAIL">
         *           &lt;simpleType>
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
         *               &lt;totalDigits value="1"/>
         *             &lt;/restriction>
         *           &lt;/simpleType>
         *         &lt;/element>
         *         &lt;element name="ADE-SW-SMS">
         *           &lt;simpleType>
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
         *               &lt;totalDigits value="1"/>
         *             &lt;/restriction>
         *           &lt;/simpleType>
         *         &lt;/element>
         *         &lt;element name="ADE-COD-EXPED">
         *           &lt;simpleType>
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               &lt;length value="50"/>
         *             &lt;/restriction>
         *           &lt;/simpleType>
         *         &lt;/element>
         *         &lt;element name="ADE-FX-RESOLUCION">
         *           &lt;simpleType>
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               &lt;length value="8"/>
         *             &lt;/restriction>
         *           &lt;/simpleType>
         *         &lt;/element>
         *         &lt;element name="ADE-TIP-AGENCIA">
         *           &lt;simpleType>
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               &lt;length value="1"/>
         *             &lt;/restriction>
         *           &lt;/simpleType>
         *         &lt;/element>
         *         &lt;element name="ADE-IN-ETT">
         *           &lt;simpleType>
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               &lt;length value="1"/>
         *             &lt;/restriction>
         *           &lt;/simpleType>
         *         &lt;/element>
         *         &lt;element name="ADE-MAIL2">
         *           &lt;simpleType>
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               &lt;length value="70"/>
         *             &lt;/restriction>
         *           &lt;/simpleType>
         *         &lt;/element>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "adeusuario",
            "adenifa",
            "adedenominacion",
            "adeidcoauta",
            "adefxautoza",
            "adetipvia",
            "adenomvia",
            "adenumero",
            "adebisdup",
            "adeescalera",
            "adepiso",
            "adeletrapuerta",
            "ademunicipio",
            "adecodpostal",
            "adetelefono",
            "adefax",
            "ademail",
            "adepaginaweb",
            "adeswmail",
            "adeswsms",
            "adecodexped",
            "adefxresolucion",
            "adetipagencia",
            "adeinett",
            "ademail2"
        })
        public static class ADEDATOSENTRADA {

            @XmlElement(name = "ADE-USUARIO", required = true)
            protected String adeusuario;
            @XmlElement(name = "ADE-NIF-A", required = true)
            protected String adenifa;
            @XmlElement(name = "ADE-DENOMINACION", required = true)
            protected String adedenominacion;
            @XmlElement(name = "ADE-ID-COAUT-A", required = true)
            protected String adeidcoauta;
            @XmlElement(name = "ADE-FX-AUTOZ-A", required = true)
            protected String adefxautoza;
            @XmlElement(name = "ADE-TIP-VIA", required = true)
            protected String adetipvia;
            @XmlElement(name = "ADE-NOM-VIA", required = true)
            protected String adenomvia;
            @XmlElement(name = "ADE-NUMERO", required = true)
            protected String adenumero;
            @XmlElement(name = "ADE-BIS-DUP", required = true)
            protected String adebisdup;
            @XmlElement(name = "ADE-ESCALERA", required = true)
            protected String adeescalera;
            @XmlElement(name = "ADE-PISO", required = true)
            protected String adepiso;
            @XmlElement(name = "ADE-LETRA-PUERTA", required = true)
            protected String adeletrapuerta;
            @XmlElement(name = "ADE-MUNICIPIO", required = true)
            protected BigDecimal ademunicipio;
            @XmlElement(name = "ADE-COD-POSTAL", required = true)
            protected BigDecimal adecodpostal;
            @XmlElement(name = "ADE-TELEFONO", required = true)
            protected String adetelefono;
            @XmlElement(name = "ADE-FAX", required = true)
            protected String adefax;
            @XmlElement(name = "ADE-MAIL", required = true)
            protected String ademail;
            @XmlElement(name = "ADE-PAGINA-WEB", required = true)
            protected String adepaginaweb;
            @XmlElement(name = "ADE-SW-MAIL", required = true)
            protected BigDecimal adeswmail;
            @XmlElement(name = "ADE-SW-SMS", required = true)
            protected BigDecimal adeswsms;
            @XmlElement(name = "ADE-COD-EXPED", required = true)
            protected String adecodexped;
            @XmlElement(name = "ADE-FX-RESOLUCION", required = true)
            protected String adefxresolucion;
            @XmlElement(name = "ADE-TIP-AGENCIA", required = true)
            protected String adetipagencia;
            @XmlElement(name = "ADE-IN-ETT", required = true)
            protected String adeinett;
            @XmlElement(name = "ADE-MAIL2", required = true)
            protected String ademail2;

            /**
             * Gets the value of the adeusuario property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getADEUSUARIO() {
                return adeusuario;
            }

            /**
             * Sets the value of the adeusuario property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setADEUSUARIO(String value) {
                this.adeusuario = value;
            }

            /**
             * Gets the value of the adenifa property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getADENIFA() {
                return adenifa;
            }

            /**
             * Sets the value of the adenifa property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setADENIFA(String value) {
                this.adenifa = value;
            }

            /**
             * Gets the value of the adedenominacion property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getADEDENOMINACION() {
                return adedenominacion;
            }

            /**
             * Sets the value of the adedenominacion property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setADEDENOMINACION(String value) {
                this.adedenominacion = value;
            }

            /**
             * Gets the value of the adeidcoauta property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getADEIDCOAUTA() {
                return adeidcoauta;
            }

            /**
             * Sets the value of the adeidcoauta property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setADEIDCOAUTA(String value) {
                this.adeidcoauta = value;
            }

            /**
             * Gets the value of the adefxautoza property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getADEFXAUTOZA() {
                return adefxautoza;
            }

            /**
             * Sets the value of the adefxautoza property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setADEFXAUTOZA(String value) {
                this.adefxautoza = value;
            }

            /**
             * Gets the value of the adetipvia property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getADETIPVIA() {
                return adetipvia;
            }

            /**
             * Sets the value of the adetipvia property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setADETIPVIA(String value) {
                this.adetipvia = value;
            }

            /**
             * Gets the value of the adenomvia property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getADENOMVIA() {
                return adenomvia;
            }

            /**
             * Sets the value of the adenomvia property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setADENOMVIA(String value) {
                this.adenomvia = value;
            }

            /**
             * Gets the value of the adenumero property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getADENUMERO() {
                return adenumero;
            }

            /**
             * Sets the value of the adenumero property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setADENUMERO(String value) {
                this.adenumero = value;
            }

            /**
             * Gets the value of the adebisdup property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getADEBISDUP() {
                return adebisdup;
            }

            /**
             * Sets the value of the adebisdup property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setADEBISDUP(String value) {
                this.adebisdup = value;
            }

            /**
             * Gets the value of the adeescalera property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getADEESCALERA() {
                return adeescalera;
            }

            /**
             * Sets the value of the adeescalera property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setADEESCALERA(String value) {
                this.adeescalera = value;
            }

            /**
             * Gets the value of the adepiso property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getADEPISO() {
                return adepiso;
            }

            /**
             * Sets the value of the adepiso property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setADEPISO(String value) {
                this.adepiso = value;
            }

            /**
             * Gets the value of the adeletrapuerta property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getADELETRAPUERTA() {
                return adeletrapuerta;
            }

            /**
             * Sets the value of the adeletrapuerta property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setADELETRAPUERTA(String value) {
                this.adeletrapuerta = value;
            }

            /**
             * Gets the value of the ademunicipio property.
             * 
             * @return
             *     possible object is
             *     {@link BigDecimal }
             *     
             */
            public BigDecimal getADEMUNICIPIO() {
                return ademunicipio;
            }

            /**
             * Sets the value of the ademunicipio property.
             * 
             * @param value
             *     allowed object is
             *     {@link BigDecimal }
             *     
             */
            public void setADEMUNICIPIO(BigDecimal value) {
                this.ademunicipio = value;
            }

            /**
             * Gets the value of the adecodpostal property.
             * 
             * @return
             *     possible object is
             *     {@link BigDecimal }
             *     
             */
            public BigDecimal getADECODPOSTAL() {
                return adecodpostal;
            }

            /**
             * Sets the value of the adecodpostal property.
             * 
             * @param value
             *     allowed object is
             *     {@link BigDecimal }
             *     
             */
            public void setADECODPOSTAL(BigDecimal value) {
                this.adecodpostal = value;
            }

            /**
             * Gets the value of the adetelefono property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getADETELEFONO() {
                return adetelefono;
            }

            /**
             * Sets the value of the adetelefono property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setADETELEFONO(String value) {
                this.adetelefono = value;
            }

            /**
             * Gets the value of the adefax property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getADEFAX() {
                return adefax;
            }

            /**
             * Sets the value of the adefax property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setADEFAX(String value) {
                this.adefax = value;
            }

            /**
             * Gets the value of the ademail property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getADEMAIL() {
                return ademail;
            }

            /**
             * Sets the value of the ademail property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setADEMAIL(String value) {
                this.ademail = value;
            }

            /**
             * Gets the value of the adepaginaweb property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getADEPAGINAWEB() {
                return adepaginaweb;
            }

            /**
             * Sets the value of the adepaginaweb property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setADEPAGINAWEB(String value) {
                this.adepaginaweb = value;
            }

            /**
             * Gets the value of the adeswmail property.
             * 
             * @return
             *     possible object is
             *     {@link BigDecimal }
             *     
             */
            public BigDecimal getADESWMAIL() {
                return adeswmail;
            }

            /**
             * Sets the value of the adeswmail property.
             * 
             * @param value
             *     allowed object is
             *     {@link BigDecimal }
             *     
             */
            public void setADESWMAIL(BigDecimal value) {
                this.adeswmail = value;
            }

            /**
             * Gets the value of the adeswsms property.
             * 
             * @return
             *     possible object is
             *     {@link BigDecimal }
             *     
             */
            public BigDecimal getADESWSMS() {
                return adeswsms;
            }

            /**
             * Sets the value of the adeswsms property.
             * 
             * @param value
             *     allowed object is
             *     {@link BigDecimal }
             *     
             */
            public void setADESWSMS(BigDecimal value) {
                this.adeswsms = value;
            }

            /**
             * Gets the value of the adecodexped property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getADECODEXPED() {
                return adecodexped;
            }

            /**
             * Sets the value of the adecodexped property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setADECODEXPED(String value) {
                this.adecodexped = value;
            }

            /**
             * Gets the value of the adefxresolucion property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getADEFXRESOLUCION() {
                return adefxresolucion;
            }

            /**
             * Sets the value of the adefxresolucion property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setADEFXRESOLUCION(String value) {
                this.adefxresolucion = value;
            }

            /**
             * Gets the value of the adetipagencia property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getADETIPAGENCIA() {
                return adetipagencia;
            }

            /**
             * Sets the value of the adetipagencia property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setADETIPAGENCIA(String value) {
                this.adetipagencia = value;
            }

            /**
             * Gets the value of the adeinett property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getADEINETT() {
                return adeinett;
            }

            /**
             * Sets the value of the adeinett property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setADEINETT(String value) {
                this.adeinett = value;
            }

            /**
             * Gets the value of the ademail2 property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getADEMAIL2() {
                return ademail2;
            }

            /**
             * Sets the value of the ademail2 property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setADEMAIL2(String value) {
                this.ademail2 = value;
            }

        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="ADS-COD-COLOCACION">
         *           &lt;simpleType>
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               &lt;length value="10"/>
         *             &lt;/restriction>
         *           &lt;/simpleType>
         *         &lt;/element>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "adscodcolocacion"
        })
        public static class ADSDATOSSALIDA {

            @XmlElement(name = "ADS-COD-COLOCACION", required = true)
            protected String adscodcolocacion;

            /**
             * Gets the value of the adscodcolocacion property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getADSCODCOLOCACION() {
                return adscodcolocacion;
            }

            /**
             * Sets the value of the adscodcolocacion property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setADSCODCOLOCACION(String value) {
                this.adscodcolocacion = value;
            }

        }

    }

}
