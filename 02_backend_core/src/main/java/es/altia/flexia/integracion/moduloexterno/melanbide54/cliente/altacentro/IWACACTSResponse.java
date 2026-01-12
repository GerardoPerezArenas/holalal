
package es.altia.flexia.integracion.moduloexterno.melanbide54.cliente.altacentro;

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
 *                             &lt;element name="ADE-ID-COAUT">
 *                               &lt;simpleType>
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   &lt;length value="2"/>
 *                                 &lt;/restriction>
 *                               &lt;/simpleType>
 *                             &lt;/element>
 *                             &lt;element name="ADE-CO-AGENC-A">
 *                               &lt;simpleType>
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   &lt;length value="10"/>
 *                                 &lt;/restriction>
 *                               &lt;/simpleType>
 *                             &lt;/element>
 *                             &lt;element name="ADE-NU-CENTRO">
 *                               &lt;simpleType>
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   &lt;length value="5"/>
 *                                 &lt;/restriction>
 *                               &lt;/simpleType>
 *                             &lt;/element>
 *                             &lt;element name="ADE-TIP-OPE">
 *                               &lt;simpleType>
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   &lt;length value="1"/>
 *                                 &lt;/restriction>
 *                               &lt;/simpleType>
 *                             &lt;/element>
 *                             &lt;element name="ADE-DATOS-CENTRO">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="ADE-MUNI-CENTRO">
 *                                         &lt;simpleType>
 *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                             &lt;length value="5"/>
 *                                           &lt;/restriction>
 *                                         &lt;/simpleType>
 *                                       &lt;/element>
 *                                       &lt;element name="ADE-TEL-CENTRO">
 *                                         &lt;simpleType>
 *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                             &lt;length value="9"/>
 *                                           &lt;/restriction>
 *                                         &lt;/simpleType>
 *                                       &lt;/element>
 *                                       &lt;element name="ADE-DOMIC-CENTRO">
 *                                         &lt;simpleType>
 *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                             &lt;length value="100"/>
 *                                           &lt;/restriction>
 *                                         &lt;/simpleType>
 *                                       &lt;/element>
 *                                       &lt;element name="ADE-CPOST-CENTRO">
 *                                         &lt;simpleType>
 *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                             &lt;length value="5"/>
 *                                           &lt;/restriction>
 *                                         &lt;/simpleType>
 *                                       &lt;/element>
 *                                       &lt;element name="ADE-EMAIL-CENTRO">
 *                                         &lt;simpleType>
 *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                             &lt;length value="70"/>
 *                                           &lt;/restriction>
 *                                         &lt;/simpleType>
 *                                       &lt;/element>
 *                                       &lt;element name="ADE-OBS-CENTRO">
 *                                         &lt;simpleType>
 *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                             &lt;length value="100"/>
 *                                           &lt;/restriction>
 *                                         &lt;/simpleType>
 *                                       &lt;/element>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
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
 *                             &lt;element name="ADS-NU-CENTRO">
 *                               &lt;simpleType>
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   &lt;length value="5"/>
 *                                 &lt;/restriction>
 *                               &lt;/simpleType>
 *                             &lt;/element>
 *                             &lt;element name="ADS-DATOS-CENTRO">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="ADS-MUNI-CENTRO">
 *                                         &lt;simpleType>
 *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                             &lt;length value="5"/>
 *                                           &lt;/restriction>
 *                                         &lt;/simpleType>
 *                                       &lt;/element>
 *                                       &lt;element name="ADS-TEL-CENTRO">
 *                                         &lt;simpleType>
 *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                             &lt;length value="9"/>
 *                                           &lt;/restriction>
 *                                         &lt;/simpleType>
 *                                       &lt;/element>
 *                                       &lt;element name="ADS-DOMIC-CENTRO">
 *                                         &lt;simpleType>
 *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                             &lt;length value="100"/>
 *                                           &lt;/restriction>
 *                                         &lt;/simpleType>
 *                                       &lt;/element>
 *                                       &lt;element name="ADS-CPOST-CENTRO">
 *                                         &lt;simpleType>
 *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                             &lt;length value="5"/>
 *                                           &lt;/restriction>
 *                                         &lt;/simpleType>
 *                                       &lt;/element>
 *                                       &lt;element name="ADS-EMAIL-CENTRO">
 *                                         &lt;simpleType>
 *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                             &lt;length value="70"/>
 *                                           &lt;/restriction>
 *                                         &lt;/simpleType>
 *                                       &lt;/element>
 *                                       &lt;element name="ADS-OBS-CENTRO">
 *                                         &lt;simpleType>
 *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                             &lt;length value="100"/>
 *                                           &lt;/restriction>
 *                                         &lt;/simpleType>
 *                                       &lt;/element>
 *                                       &lt;element name="ADS-FX-ALTA">
 *                                         &lt;simpleType>
 *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                             &lt;length value="8"/>
 *                                           &lt;/restriction>
 *                                         &lt;/simpleType>
 *                                       &lt;/element>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
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
@XmlRootElement(name = "IWACACTSResponse")
public class IWACACTSResponse {

    @XmlElement(name = "AREA-COMUNICACION", required = true)
    protected IWACACTSResponse.AREACOMUNICACION areacomunicacion;

    /**
     * Gets the value of the areacomunicacion property.
     * 
     * @return
     *     possible object is
     *     {@link IWACACTSResponse.AREACOMUNICACION }
     *     
     */
    public IWACACTSResponse.AREACOMUNICACION getAREACOMUNICACION() {
        return areacomunicacion;
    }

    /**
     * Sets the value of the areacomunicacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link IWACACTSResponse.AREACOMUNICACION }
     *     
     */
    public void setAREACOMUNICACION(IWACACTSResponse.AREACOMUNICACION value) {
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
     *                   &lt;element name="ADE-ID-COAUT">
     *                     &lt;simpleType>
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         &lt;length value="2"/>
     *                       &lt;/restriction>
     *                     &lt;/simpleType>
     *                   &lt;/element>
     *                   &lt;element name="ADE-CO-AGENC-A">
     *                     &lt;simpleType>
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         &lt;length value="10"/>
     *                       &lt;/restriction>
     *                     &lt;/simpleType>
     *                   &lt;/element>
     *                   &lt;element name="ADE-NU-CENTRO">
     *                     &lt;simpleType>
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         &lt;length value="5"/>
     *                       &lt;/restriction>
     *                     &lt;/simpleType>
     *                   &lt;/element>
     *                   &lt;element name="ADE-TIP-OPE">
     *                     &lt;simpleType>
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         &lt;length value="1"/>
     *                       &lt;/restriction>
     *                     &lt;/simpleType>
     *                   &lt;/element>
     *                   &lt;element name="ADE-DATOS-CENTRO">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="ADE-MUNI-CENTRO">
     *                               &lt;simpleType>
     *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                                   &lt;length value="5"/>
     *                                 &lt;/restriction>
     *                               &lt;/simpleType>
     *                             &lt;/element>
     *                             &lt;element name="ADE-TEL-CENTRO">
     *                               &lt;simpleType>
     *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                                   &lt;length value="9"/>
     *                                 &lt;/restriction>
     *                               &lt;/simpleType>
     *                             &lt;/element>
     *                             &lt;element name="ADE-DOMIC-CENTRO">
     *                               &lt;simpleType>
     *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                                   &lt;length value="100"/>
     *                                 &lt;/restriction>
     *                               &lt;/simpleType>
     *                             &lt;/element>
     *                             &lt;element name="ADE-CPOST-CENTRO">
     *                               &lt;simpleType>
     *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                                   &lt;length value="5"/>
     *                                 &lt;/restriction>
     *                               &lt;/simpleType>
     *                             &lt;/element>
     *                             &lt;element name="ADE-EMAIL-CENTRO">
     *                               &lt;simpleType>
     *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                                   &lt;length value="70"/>
     *                                 &lt;/restriction>
     *                               &lt;/simpleType>
     *                             &lt;/element>
     *                             &lt;element name="ADE-OBS-CENTRO">
     *                               &lt;simpleType>
     *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                                   &lt;length value="100"/>
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
     *         &lt;element name="ADS-DATOS-SALIDA">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="ADS-NU-CENTRO">
     *                     &lt;simpleType>
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         &lt;length value="5"/>
     *                       &lt;/restriction>
     *                     &lt;/simpleType>
     *                   &lt;/element>
     *                   &lt;element name="ADS-DATOS-CENTRO">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="ADS-MUNI-CENTRO">
     *                               &lt;simpleType>
     *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                                   &lt;length value="5"/>
     *                                 &lt;/restriction>
     *                               &lt;/simpleType>
     *                             &lt;/element>
     *                             &lt;element name="ADS-TEL-CENTRO">
     *                               &lt;simpleType>
     *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                                   &lt;length value="9"/>
     *                                 &lt;/restriction>
     *                               &lt;/simpleType>
     *                             &lt;/element>
     *                             &lt;element name="ADS-DOMIC-CENTRO">
     *                               &lt;simpleType>
     *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                                   &lt;length value="100"/>
     *                                 &lt;/restriction>
     *                               &lt;/simpleType>
     *                             &lt;/element>
     *                             &lt;element name="ADS-CPOST-CENTRO">
     *                               &lt;simpleType>
     *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                                   &lt;length value="5"/>
     *                                 &lt;/restriction>
     *                               &lt;/simpleType>
     *                             &lt;/element>
     *                             &lt;element name="ADS-EMAIL-CENTRO">
     *                               &lt;simpleType>
     *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                                   &lt;length value="70"/>
     *                                 &lt;/restriction>
     *                               &lt;/simpleType>
     *                             &lt;/element>
     *                             &lt;element name="ADS-OBS-CENTRO">
     *                               &lt;simpleType>
     *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                                   &lt;length value="100"/>
     *                                 &lt;/restriction>
     *                               &lt;/simpleType>
     *                             &lt;/element>
     *                             &lt;element name="ADS-FX-ALTA">
     *                               &lt;simpleType>
     *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                                   &lt;length value="8"/>
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
        "adcodcontrolentirex",
        "adcareacontrol",
        "adedatosentrada",
        "adsdatossalida"
    })
    public static class AREACOMUNICACION {

        @XmlElement(name = "AD-COD-CONTROL-ENTIREX", required = true)
        protected BigDecimal adcodcontrolentirex;
        @XmlElement(name = "ADC-AREA-CONTROL", required = true)
        protected IWACACTSResponse.AREACOMUNICACION.ADCAREACONTROL adcareacontrol;
        @XmlElement(name = "ADE-DATOS-ENTRADA", required = true)
        protected IWACACTSResponse.AREACOMUNICACION.ADEDATOSENTRADA adedatosentrada;
        @XmlElement(name = "ADS-DATOS-SALIDA", required = true)
        protected IWACACTSResponse.AREACOMUNICACION.ADSDATOSSALIDA adsdatossalida;

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
         *     {@link IWACACTSResponse.AREACOMUNICACION.ADCAREACONTROL }
         *     
         */
        public IWACACTSResponse.AREACOMUNICACION.ADCAREACONTROL getADCAREACONTROL() {
            return adcareacontrol;
        }

        /**
         * Sets the value of the adcareacontrol property.
         * 
         * @param value
         *     allowed object is
         *     {@link IWACACTSResponse.AREACOMUNICACION.ADCAREACONTROL }
         *     
         */
        public void setADCAREACONTROL(IWACACTSResponse.AREACOMUNICACION.ADCAREACONTROL value) {
            this.adcareacontrol = value;
        }

        /**
         * Gets the value of the adedatosentrada property.
         * 
         * @return
         *     possible object is
         *     {@link IWACACTSResponse.AREACOMUNICACION.ADEDATOSENTRADA }
         *     
         */
        public IWACACTSResponse.AREACOMUNICACION.ADEDATOSENTRADA getADEDATOSENTRADA() {
            return adedatosentrada;
        }

        /**
         * Sets the value of the adedatosentrada property.
         * 
         * @param value
         *     allowed object is
         *     {@link IWACACTSResponse.AREACOMUNICACION.ADEDATOSENTRADA }
         *     
         */
        public void setADEDATOSENTRADA(IWACACTSResponse.AREACOMUNICACION.ADEDATOSENTRADA value) {
            this.adedatosentrada = value;
        }

        /**
         * Gets the value of the adsdatossalida property.
         * 
         * @return
         *     possible object is
         *     {@link IWACACTSResponse.AREACOMUNICACION.ADSDATOSSALIDA }
         *     
         */
        public IWACACTSResponse.AREACOMUNICACION.ADSDATOSSALIDA getADSDATOSSALIDA() {
            return adsdatossalida;
        }

        /**
         * Sets the value of the adsdatossalida property.
         * 
         * @param value
         *     allowed object is
         *     {@link IWACACTSResponse.AREACOMUNICACION.ADSDATOSSALIDA }
         *     
         */
        public void setADSDATOSSALIDA(IWACACTSResponse.AREACOMUNICACION.ADSDATOSSALIDA value) {
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
         *         &lt;element name="ADE-ID-COAUT">
         *           &lt;simpleType>
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               &lt;length value="2"/>
         *             &lt;/restriction>
         *           &lt;/simpleType>
         *         &lt;/element>
         *         &lt;element name="ADE-CO-AGENC-A">
         *           &lt;simpleType>
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               &lt;length value="10"/>
         *             &lt;/restriction>
         *           &lt;/simpleType>
         *         &lt;/element>
         *         &lt;element name="ADE-NU-CENTRO">
         *           &lt;simpleType>
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               &lt;length value="5"/>
         *             &lt;/restriction>
         *           &lt;/simpleType>
         *         &lt;/element>
         *         &lt;element name="ADE-TIP-OPE">
         *           &lt;simpleType>
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               &lt;length value="1"/>
         *             &lt;/restriction>
         *           &lt;/simpleType>
         *         &lt;/element>
         *         &lt;element name="ADE-DATOS-CENTRO">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="ADE-MUNI-CENTRO">
         *                     &lt;simpleType>
         *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *                         &lt;length value="5"/>
         *                       &lt;/restriction>
         *                     &lt;/simpleType>
         *                   &lt;/element>
         *                   &lt;element name="ADE-TEL-CENTRO">
         *                     &lt;simpleType>
         *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *                         &lt;length value="9"/>
         *                       &lt;/restriction>
         *                     &lt;/simpleType>
         *                   &lt;/element>
         *                   &lt;element name="ADE-DOMIC-CENTRO">
         *                     &lt;simpleType>
         *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *                         &lt;length value="100"/>
         *                       &lt;/restriction>
         *                     &lt;/simpleType>
         *                   &lt;/element>
         *                   &lt;element name="ADE-CPOST-CENTRO">
         *                     &lt;simpleType>
         *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *                         &lt;length value="5"/>
         *                       &lt;/restriction>
         *                     &lt;/simpleType>
         *                   &lt;/element>
         *                   &lt;element name="ADE-EMAIL-CENTRO">
         *                     &lt;simpleType>
         *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *                         &lt;length value="70"/>
         *                       &lt;/restriction>
         *                     &lt;/simpleType>
         *                   &lt;/element>
         *                   &lt;element name="ADE-OBS-CENTRO">
         *                     &lt;simpleType>
         *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *                         &lt;length value="100"/>
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
            "adeusuario",
            "adeidcoaut",
            "adecoagenca",
            "adenucentro",
            "adetipope",
            "adedatoscentro"
        })
        public static class ADEDATOSENTRADA {

            @XmlElement(name = "ADE-USUARIO", required = true)
            protected String adeusuario;
            @XmlElement(name = "ADE-ID-COAUT", required = true)
            protected String adeidcoaut;
            @XmlElement(name = "ADE-CO-AGENC-A", required = true)
            protected String adecoagenca;
            @XmlElement(name = "ADE-NU-CENTRO", required = true)
            protected String adenucentro;
            @XmlElement(name = "ADE-TIP-OPE", required = true)
            protected String adetipope;
            @XmlElement(name = "ADE-DATOS-CENTRO", required = true)
            protected IWACACTSResponse.AREACOMUNICACION.ADEDATOSENTRADA.ADEDATOSCENTRO adedatoscentro;

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
             * Gets the value of the adeidcoaut property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getADEIDCOAUT() {
                return adeidcoaut;
            }

            /**
             * Sets the value of the adeidcoaut property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setADEIDCOAUT(String value) {
                this.adeidcoaut = value;
            }

            /**
             * Gets the value of the adecoagenca property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getADECOAGENCA() {
                return adecoagenca;
            }

            /**
             * Sets the value of the adecoagenca property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setADECOAGENCA(String value) {
                this.adecoagenca = value;
            }

            /**
             * Gets the value of the adenucentro property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getADENUCENTRO() {
                return adenucentro;
            }

            /**
             * Sets the value of the adenucentro property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setADENUCENTRO(String value) {
                this.adenucentro = value;
            }

            /**
             * Gets the value of the adetipope property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getADETIPOPE() {
                return adetipope;
            }

            /**
             * Sets the value of the adetipope property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setADETIPOPE(String value) {
                this.adetipope = value;
            }

            /**
             * Gets the value of the adedatoscentro property.
             * 
             * @return
             *     possible object is
             *     {@link IWACACTSResponse.AREACOMUNICACION.ADEDATOSENTRADA.ADEDATOSCENTRO }
             *     
             */
            public IWACACTSResponse.AREACOMUNICACION.ADEDATOSENTRADA.ADEDATOSCENTRO getADEDATOSCENTRO() {
                return adedatoscentro;
            }

            /**
             * Sets the value of the adedatoscentro property.
             * 
             * @param value
             *     allowed object is
             *     {@link IWACACTSResponse.AREACOMUNICACION.ADEDATOSENTRADA.ADEDATOSCENTRO }
             *     
             */
            public void setADEDATOSCENTRO(IWACACTSResponse.AREACOMUNICACION.ADEDATOSENTRADA.ADEDATOSCENTRO value) {
                this.adedatoscentro = value;
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
             *         &lt;element name="ADE-MUNI-CENTRO">
             *           &lt;simpleType>
             *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
             *               &lt;length value="5"/>
             *             &lt;/restriction>
             *           &lt;/simpleType>
             *         &lt;/element>
             *         &lt;element name="ADE-TEL-CENTRO">
             *           &lt;simpleType>
             *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
             *               &lt;length value="9"/>
             *             &lt;/restriction>
             *           &lt;/simpleType>
             *         &lt;/element>
             *         &lt;element name="ADE-DOMIC-CENTRO">
             *           &lt;simpleType>
             *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
             *               &lt;length value="100"/>
             *             &lt;/restriction>
             *           &lt;/simpleType>
             *         &lt;/element>
             *         &lt;element name="ADE-CPOST-CENTRO">
             *           &lt;simpleType>
             *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
             *               &lt;length value="5"/>
             *             &lt;/restriction>
             *           &lt;/simpleType>
             *         &lt;/element>
             *         &lt;element name="ADE-EMAIL-CENTRO">
             *           &lt;simpleType>
             *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
             *               &lt;length value="70"/>
             *             &lt;/restriction>
             *           &lt;/simpleType>
             *         &lt;/element>
             *         &lt;element name="ADE-OBS-CENTRO">
             *           &lt;simpleType>
             *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
             *               &lt;length value="100"/>
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
                "ademunicentro",
                "adetelcentro",
                "adedomiccentro",
                "adecpostcentro",
                "adeemailcentro",
                "adeobscentro"
            })
            public static class ADEDATOSCENTRO {

                @XmlElement(name = "ADE-MUNI-CENTRO", required = true)
                protected String ademunicentro;
                @XmlElement(name = "ADE-TEL-CENTRO", required = true)
                protected String adetelcentro;
                @XmlElement(name = "ADE-DOMIC-CENTRO", required = true)
                protected String adedomiccentro;
                @XmlElement(name = "ADE-CPOST-CENTRO", required = true)
                protected String adecpostcentro;
                @XmlElement(name = "ADE-EMAIL-CENTRO", required = true)
                protected String adeemailcentro;
                @XmlElement(name = "ADE-OBS-CENTRO", required = true)
                protected String adeobscentro;

                /**
                 * Gets the value of the ademunicentro property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getADEMUNICENTRO() {
                    return ademunicentro;
                }

                /**
                 * Sets the value of the ademunicentro property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setADEMUNICENTRO(String value) {
                    this.ademunicentro = value;
                }

                /**
                 * Gets the value of the adetelcentro property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getADETELCENTRO() {
                    return adetelcentro;
                }

                /**
                 * Sets the value of the adetelcentro property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setADETELCENTRO(String value) {
                    this.adetelcentro = value;
                }

                /**
                 * Gets the value of the adedomiccentro property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getADEDOMICCENTRO() {
                    return adedomiccentro;
                }

                /**
                 * Sets the value of the adedomiccentro property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setADEDOMICCENTRO(String value) {
                    this.adedomiccentro = value;
                }

                /**
                 * Gets the value of the adecpostcentro property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getADECPOSTCENTRO() {
                    return adecpostcentro;
                }

                /**
                 * Sets the value of the adecpostcentro property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setADECPOSTCENTRO(String value) {
                    this.adecpostcentro = value;
                }

                /**
                 * Gets the value of the adeemailcentro property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getADEEMAILCENTRO() {
                    return adeemailcentro;
                }

                /**
                 * Sets the value of the adeemailcentro property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setADEEMAILCENTRO(String value) {
                    this.adeemailcentro = value;
                }

                /**
                 * Gets the value of the adeobscentro property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getADEOBSCENTRO() {
                    return adeobscentro;
                }

                /**
                 * Sets the value of the adeobscentro property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setADEOBSCENTRO(String value) {
                    this.adeobscentro = value;
                }

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
         *         &lt;element name="ADS-NU-CENTRO">
         *           &lt;simpleType>
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               &lt;length value="5"/>
         *             &lt;/restriction>
         *           &lt;/simpleType>
         *         &lt;/element>
         *         &lt;element name="ADS-DATOS-CENTRO">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="ADS-MUNI-CENTRO">
         *                     &lt;simpleType>
         *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *                         &lt;length value="5"/>
         *                       &lt;/restriction>
         *                     &lt;/simpleType>
         *                   &lt;/element>
         *                   &lt;element name="ADS-TEL-CENTRO">
         *                     &lt;simpleType>
         *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *                         &lt;length value="9"/>
         *                       &lt;/restriction>
         *                     &lt;/simpleType>
         *                   &lt;/element>
         *                   &lt;element name="ADS-DOMIC-CENTRO">
         *                     &lt;simpleType>
         *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *                         &lt;length value="100"/>
         *                       &lt;/restriction>
         *                     &lt;/simpleType>
         *                   &lt;/element>
         *                   &lt;element name="ADS-CPOST-CENTRO">
         *                     &lt;simpleType>
         *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *                         &lt;length value="5"/>
         *                       &lt;/restriction>
         *                     &lt;/simpleType>
         *                   &lt;/element>
         *                   &lt;element name="ADS-EMAIL-CENTRO">
         *                     &lt;simpleType>
         *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *                         &lt;length value="70"/>
         *                       &lt;/restriction>
         *                     &lt;/simpleType>
         *                   &lt;/element>
         *                   &lt;element name="ADS-OBS-CENTRO">
         *                     &lt;simpleType>
         *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *                         &lt;length value="100"/>
         *                       &lt;/restriction>
         *                     &lt;/simpleType>
         *                   &lt;/element>
         *                   &lt;element name="ADS-FX-ALTA">
         *                     &lt;simpleType>
         *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *                         &lt;length value="8"/>
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
            "adsnucentro",
            "adsdatoscentro"
        })
        public static class ADSDATOSSALIDA {

            @XmlElement(name = "ADS-NU-CENTRO", required = true)
            protected String adsnucentro;
            @XmlElement(name = "ADS-DATOS-CENTRO", required = true)
            protected IWACACTSResponse.AREACOMUNICACION.ADSDATOSSALIDA.ADSDATOSCENTRO adsdatoscentro;

            /**
             * Gets the value of the adsnucentro property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getADSNUCENTRO() {
                return adsnucentro;
            }

            /**
             * Sets the value of the adsnucentro property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setADSNUCENTRO(String value) {
                this.adsnucentro = value;
            }

            /**
             * Gets the value of the adsdatoscentro property.
             * 
             * @return
             *     possible object is
             *     {@link IWACACTSResponse.AREACOMUNICACION.ADSDATOSSALIDA.ADSDATOSCENTRO }
             *     
             */
            public IWACACTSResponse.AREACOMUNICACION.ADSDATOSSALIDA.ADSDATOSCENTRO getADSDATOSCENTRO() {
                return adsdatoscentro;
            }

            /**
             * Sets the value of the adsdatoscentro property.
             * 
             * @param value
             *     allowed object is
             *     {@link IWACACTSResponse.AREACOMUNICACION.ADSDATOSSALIDA.ADSDATOSCENTRO }
             *     
             */
            public void setADSDATOSCENTRO(IWACACTSResponse.AREACOMUNICACION.ADSDATOSSALIDA.ADSDATOSCENTRO value) {
                this.adsdatoscentro = value;
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
             *         &lt;element name="ADS-MUNI-CENTRO">
             *           &lt;simpleType>
             *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
             *               &lt;length value="5"/>
             *             &lt;/restriction>
             *           &lt;/simpleType>
             *         &lt;/element>
             *         &lt;element name="ADS-TEL-CENTRO">
             *           &lt;simpleType>
             *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
             *               &lt;length value="9"/>
             *             &lt;/restriction>
             *           &lt;/simpleType>
             *         &lt;/element>
             *         &lt;element name="ADS-DOMIC-CENTRO">
             *           &lt;simpleType>
             *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
             *               &lt;length value="100"/>
             *             &lt;/restriction>
             *           &lt;/simpleType>
             *         &lt;/element>
             *         &lt;element name="ADS-CPOST-CENTRO">
             *           &lt;simpleType>
             *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
             *               &lt;length value="5"/>
             *             &lt;/restriction>
             *           &lt;/simpleType>
             *         &lt;/element>
             *         &lt;element name="ADS-EMAIL-CENTRO">
             *           &lt;simpleType>
             *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
             *               &lt;length value="70"/>
             *             &lt;/restriction>
             *           &lt;/simpleType>
             *         &lt;/element>
             *         &lt;element name="ADS-OBS-CENTRO">
             *           &lt;simpleType>
             *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
             *               &lt;length value="100"/>
             *             &lt;/restriction>
             *           &lt;/simpleType>
             *         &lt;/element>
             *         &lt;element name="ADS-FX-ALTA">
             *           &lt;simpleType>
             *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
             *               &lt;length value="8"/>
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
                "adsmunicentro",
                "adstelcentro",
                "adsdomiccentro",
                "adscpostcentro",
                "adsemailcentro",
                "adsobscentro",
                "adsfxalta"
            })
            public static class ADSDATOSCENTRO {

                @XmlElement(name = "ADS-MUNI-CENTRO", required = true)
                protected String adsmunicentro;
                @XmlElement(name = "ADS-TEL-CENTRO", required = true)
                protected String adstelcentro;
                @XmlElement(name = "ADS-DOMIC-CENTRO", required = true)
                protected String adsdomiccentro;
                @XmlElement(name = "ADS-CPOST-CENTRO", required = true)
                protected String adscpostcentro;
                @XmlElement(name = "ADS-EMAIL-CENTRO", required = true)
                protected String adsemailcentro;
                @XmlElement(name = "ADS-OBS-CENTRO", required = true)
                protected String adsobscentro;
                @XmlElement(name = "ADS-FX-ALTA", required = true)
                protected String adsfxalta;

                /**
                 * Gets the value of the adsmunicentro property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getADSMUNICENTRO() {
                    return adsmunicentro;
                }

                /**
                 * Sets the value of the adsmunicentro property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setADSMUNICENTRO(String value) {
                    this.adsmunicentro = value;
                }

                /**
                 * Gets the value of the adstelcentro property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getADSTELCENTRO() {
                    return adstelcentro;
                }

                /**
                 * Sets the value of the adstelcentro property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setADSTELCENTRO(String value) {
                    this.adstelcentro = value;
                }

                /**
                 * Gets the value of the adsdomiccentro property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getADSDOMICCENTRO() {
                    return adsdomiccentro;
                }

                /**
                 * Sets the value of the adsdomiccentro property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setADSDOMICCENTRO(String value) {
                    this.adsdomiccentro = value;
                }

                /**
                 * Gets the value of the adscpostcentro property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getADSCPOSTCENTRO() {
                    return adscpostcentro;
                }

                /**
                 * Sets the value of the adscpostcentro property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setADSCPOSTCENTRO(String value) {
                    this.adscpostcentro = value;
                }

                /**
                 * Gets the value of the adsemailcentro property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getADSEMAILCENTRO() {
                    return adsemailcentro;
                }

                /**
                 * Sets the value of the adsemailcentro property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setADSEMAILCENTRO(String value) {
                    this.adsemailcentro = value;
                }

                /**
                 * Gets the value of the adsobscentro property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getADSOBSCENTRO() {
                    return adsobscentro;
                }

                /**
                 * Sets the value of the adsobscentro property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setADSOBSCENTRO(String value) {
                    this.adsobscentro = value;
                }

                /**
                 * Gets the value of the adsfxalta property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getADSFXALTA() {
                    return adsfxalta;
                }

                /**
                 * Sets the value of the adsfxalta property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setADSFXALTA(String value) {
                    this.adsfxalta = value;
                }

            }

        }

    }

}
