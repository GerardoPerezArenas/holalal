
package es.altia.flexia.integracion.moduloexterno.melanbide54.cliente.consultaagencia;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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
 *                             &lt;element name="ADE-NIFE">
 *                               &lt;simpleType>
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   &lt;length value="9"/>
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
 *                             &lt;element name="ADS-NUM-REG">
 *                               &lt;simpleType>
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                                   &lt;totalDigits value="2"/>
 *                                 &lt;/restriction>
 *                               &lt;/simpleType>
 *                             &lt;/element>
 *                             &lt;element name="ADS-NUM-TOTAL">
 *                               &lt;simpleType>
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                                   &lt;totalDigits value="2"/>
 *                                 &lt;/restriction>
 *                               &lt;/simpleType>
 *                             &lt;/element>
 *                             &lt;element name="DATOSs" minOccurs="0">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="DATOS" maxOccurs="10">
 *                                         &lt;complexType>
 *                                           &lt;complexContent>
 *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                               &lt;sequence>
 *                                                 &lt;element name="ADS-NIFE">
 *                                                   &lt;simpleType>
 *                                                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                                       &lt;length value="9"/>
 *                                                     &lt;/restriction>
 *                                                   &lt;/simpleType>
 *                                                 &lt;/element>
 *                                                 &lt;element name="ADS-CO-AGENC">
 *                                                   &lt;simpleType>
 *                                                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                                       &lt;length value="10"/>
 *                                                     &lt;/restriction>
 *                                                   &lt;/simpleType>
 *                                                 &lt;/element>
 *                                                 &lt;element name="ADS-FX-RESOL">
 *                                                   &lt;simpleType>
 *                                                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                                       &lt;length value="8"/>
 *                                                     &lt;/restriction>
 *                                                   &lt;/simpleType>
 *                                                 &lt;/element>
 *                                                 &lt;element name="ADS-NOMAGE">
 *                                                   &lt;simpleType>
 *                                                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                                       &lt;length value="55"/>
 *                                                     &lt;/restriction>
 *                                                   &lt;/simpleType>
 *                                                 &lt;/element>
 *                                                 &lt;element name="ADS-FX-BAJA">
 *                                                   &lt;simpleType>
 *                                                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                                       &lt;length value="8"/>
 *                                                     &lt;/restriction>
 *                                                   &lt;/simpleType>
 *                                                 &lt;/element>
 *                                                 &lt;element name="ADS-MOTBAJA">
 *                                                   &lt;simpleType>
 *                                                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                                       &lt;length value="2"/>
 *                                                     &lt;/restriction>
 *                                                   &lt;/simpleType>
 *                                                 &lt;/element>
 *                                               &lt;/sequence>
 *                                             &lt;/restriction>
 *                                           &lt;/complexContent>
 *                                         &lt;/complexType>
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
@XmlRootElement(name = "IWACCIFSResponse")
public class IWACCIFSResponse {

    @XmlElement(name = "AREA-COMUNICACION", required = true)
    protected IWACCIFSResponse.AREACOMUNICACION areacomunicacion;

    /**
     * Gets the value of the areacomunicacion property.
     * 
     * @return
     *     possible object is
     *     {@link IWACCIFSResponse.AREACOMUNICACION }
     *     
     */
    public IWACCIFSResponse.AREACOMUNICACION getAREACOMUNICACION() {
        return areacomunicacion;
    }

    /**
     * Sets the value of the areacomunicacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link IWACCIFSResponse.AREACOMUNICACION }
     *     
     */
    public void setAREACOMUNICACION(IWACCIFSResponse.AREACOMUNICACION value) {
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
     *                   &lt;element name="ADE-NIFE">
     *                     &lt;simpleType>
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         &lt;length value="9"/>
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
     *                   &lt;element name="ADS-NUM-REG">
     *                     &lt;simpleType>
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
     *                         &lt;totalDigits value="2"/>
     *                       &lt;/restriction>
     *                     &lt;/simpleType>
     *                   &lt;/element>
     *                   &lt;element name="ADS-NUM-TOTAL">
     *                     &lt;simpleType>
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
     *                         &lt;totalDigits value="2"/>
     *                       &lt;/restriction>
     *                     &lt;/simpleType>
     *                   &lt;/element>
     *                   &lt;element name="DATOSs" minOccurs="0">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="DATOS" maxOccurs="10">
     *                               &lt;complexType>
     *                                 &lt;complexContent>
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                     &lt;sequence>
     *                                       &lt;element name="ADS-NIFE">
     *                                         &lt;simpleType>
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                                             &lt;length value="9"/>
     *                                           &lt;/restriction>
     *                                         &lt;/simpleType>
     *                                       &lt;/element>
     *                                       &lt;element name="ADS-CO-AGENC">
     *                                         &lt;simpleType>
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                                             &lt;length value="10"/>
     *                                           &lt;/restriction>
     *                                         &lt;/simpleType>
     *                                       &lt;/element>
     *                                       &lt;element name="ADS-FX-RESOL">
     *                                         &lt;simpleType>
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                                             &lt;length value="8"/>
     *                                           &lt;/restriction>
     *                                         &lt;/simpleType>
     *                                       &lt;/element>
     *                                       &lt;element name="ADS-NOMAGE">
     *                                         &lt;simpleType>
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                                             &lt;length value="55"/>
     *                                           &lt;/restriction>
     *                                         &lt;/simpleType>
     *                                       &lt;/element>
     *                                       &lt;element name="ADS-FX-BAJA">
     *                                         &lt;simpleType>
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                                             &lt;length value="8"/>
     *                                           &lt;/restriction>
     *                                         &lt;/simpleType>
     *                                       &lt;/element>
     *                                       &lt;element name="ADS-MOTBAJA">
     *                                         &lt;simpleType>
     *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                                             &lt;length value="2"/>
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
        "adcodcontrolentirex",
        "adcareacontrol",
        "adedatosentrada",
        "adsdatossalida"
    })
    public static class AREACOMUNICACION {

        @XmlElement(name = "AD-COD-CONTROL-ENTIREX", required = true)
        protected BigDecimal adcodcontrolentirex;
        @XmlElement(name = "ADC-AREA-CONTROL", required = true)
        protected IWACCIFSResponse.AREACOMUNICACION.ADCAREACONTROL adcareacontrol;
        @XmlElement(name = "ADE-DATOS-ENTRADA", required = true)
        protected IWACCIFSResponse.AREACOMUNICACION.ADEDATOSENTRADA adedatosentrada;
        @XmlElement(name = "ADS-DATOS-SALIDA", required = true)
        protected IWACCIFSResponse.AREACOMUNICACION.ADSDATOSSALIDA adsdatossalida;

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
         *     {@link IWACCIFSResponse.AREACOMUNICACION.ADCAREACONTROL }
         *     
         */
        public IWACCIFSResponse.AREACOMUNICACION.ADCAREACONTROL getADCAREACONTROL() {
            return adcareacontrol;
        }

        /**
         * Sets the value of the adcareacontrol property.
         * 
         * @param value
         *     allowed object is
         *     {@link IWACCIFSResponse.AREACOMUNICACION.ADCAREACONTROL }
         *     
         */
        public void setADCAREACONTROL(IWACCIFSResponse.AREACOMUNICACION.ADCAREACONTROL value) {
            this.adcareacontrol = value;
        }

        /**
         * Gets the value of the adedatosentrada property.
         * 
         * @return
         *     possible object is
         *     {@link IWACCIFSResponse.AREACOMUNICACION.ADEDATOSENTRADA }
         *     
         */
        public IWACCIFSResponse.AREACOMUNICACION.ADEDATOSENTRADA getADEDATOSENTRADA() {
            return adedatosentrada;
        }

        /**
         * Sets the value of the adedatosentrada property.
         * 
         * @param value
         *     allowed object is
         *     {@link IWACCIFSResponse.AREACOMUNICACION.ADEDATOSENTRADA }
         *     
         */
        public void setADEDATOSENTRADA(IWACCIFSResponse.AREACOMUNICACION.ADEDATOSENTRADA value) {
            this.adedatosentrada = value;
        }

        /**
         * Gets the value of the adsdatossalida property.
         * 
         * @return
         *     possible object is
         *     {@link IWACCIFSResponse.AREACOMUNICACION.ADSDATOSSALIDA }
         *     
         */
        public IWACCIFSResponse.AREACOMUNICACION.ADSDATOSSALIDA getADSDATOSSALIDA() {
            return adsdatossalida;
        }

        /**
         * Sets the value of the adsdatossalida property.
         * 
         * @param value
         *     allowed object is
         *     {@link IWACCIFSResponse.AREACOMUNICACION.ADSDATOSSALIDA }
         *     
         */
        public void setADSDATOSSALIDA(IWACCIFSResponse.AREACOMUNICACION.ADSDATOSSALIDA value) {
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
         *         &lt;element name="ADE-NIFE">
         *           &lt;simpleType>
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               &lt;length value="9"/>
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
            "adenife"
        })
        public static class ADEDATOSENTRADA {

            @XmlElement(name = "ADE-USUARIO", required = true)
            protected String adeusuario;
            @XmlElement(name = "ADE-NIFE", required = true)
            protected String adenife;

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
             * Gets the value of the adenife property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getADENIFE() {
                return adenife;
            }

            /**
             * Sets the value of the adenife property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setADENIFE(String value) {
                this.adenife = value;
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
         *         &lt;element name="ADS-NUM-REG">
         *           &lt;simpleType>
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
         *               &lt;totalDigits value="2"/>
         *             &lt;/restriction>
         *           &lt;/simpleType>
         *         &lt;/element>
         *         &lt;element name="ADS-NUM-TOTAL">
         *           &lt;simpleType>
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
         *               &lt;totalDigits value="2"/>
         *             &lt;/restriction>
         *           &lt;/simpleType>
         *         &lt;/element>
         *         &lt;element name="DATOSs" minOccurs="0">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="DATOS" maxOccurs="10">
         *                     &lt;complexType>
         *                       &lt;complexContent>
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                           &lt;sequence>
         *                             &lt;element name="ADS-NIFE">
         *                               &lt;simpleType>
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *                                   &lt;length value="9"/>
         *                                 &lt;/restriction>
         *                               &lt;/simpleType>
         *                             &lt;/element>
         *                             &lt;element name="ADS-CO-AGENC">
         *                               &lt;simpleType>
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *                                   &lt;length value="10"/>
         *                                 &lt;/restriction>
         *                               &lt;/simpleType>
         *                             &lt;/element>
         *                             &lt;element name="ADS-FX-RESOL">
         *                               &lt;simpleType>
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *                                   &lt;length value="8"/>
         *                                 &lt;/restriction>
         *                               &lt;/simpleType>
         *                             &lt;/element>
         *                             &lt;element name="ADS-NOMAGE">
         *                               &lt;simpleType>
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *                                   &lt;length value="55"/>
         *                                 &lt;/restriction>
         *                               &lt;/simpleType>
         *                             &lt;/element>
         *                             &lt;element name="ADS-FX-BAJA">
         *                               &lt;simpleType>
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *                                   &lt;length value="8"/>
         *                                 &lt;/restriction>
         *                               &lt;/simpleType>
         *                             &lt;/element>
         *                             &lt;element name="ADS-MOTBAJA">
         *                               &lt;simpleType>
         *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *                                   &lt;length value="2"/>
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
            "adsnumreg",
            "adsnumtotal",
            "datoSs"
        })
        public static class ADSDATOSSALIDA {

            @XmlElement(name = "ADS-NUM-REG", required = true)
            protected BigDecimal adsnumreg;
            @XmlElement(name = "ADS-NUM-TOTAL", required = true)
            protected BigDecimal adsnumtotal;
            @XmlElement(name = "DATOSs")
            protected IWACCIFSResponse.AREACOMUNICACION.ADSDATOSSALIDA.DATOSs datoSs;

            /**
             * Gets the value of the adsnumreg property.
             * 
             * @return
             *     possible object is
             *     {@link BigDecimal }
             *     
             */
            public BigDecimal getADSNUMREG() {
                return adsnumreg;
            }

            /**
             * Sets the value of the adsnumreg property.
             * 
             * @param value
             *     allowed object is
             *     {@link BigDecimal }
             *     
             */
            public void setADSNUMREG(BigDecimal value) {
                this.adsnumreg = value;
            }

            /**
             * Gets the value of the adsnumtotal property.
             * 
             * @return
             *     possible object is
             *     {@link BigDecimal }
             *     
             */
            public BigDecimal getADSNUMTOTAL() {
                return adsnumtotal;
            }

            /**
             * Sets the value of the adsnumtotal property.
             * 
             * @param value
             *     allowed object is
             *     {@link BigDecimal }
             *     
             */
            public void setADSNUMTOTAL(BigDecimal value) {
                this.adsnumtotal = value;
            }

            /**
             * Gets the value of the datoSs property.
             * 
             * @return
             *     possible object is
             *     {@link IWACCIFSResponse.AREACOMUNICACION.ADSDATOSSALIDA.DATOSs }
             *     
             */
            public IWACCIFSResponse.AREACOMUNICACION.ADSDATOSSALIDA.DATOSs getDATOSs() {
                return datoSs;
            }

            /**
             * Sets the value of the datoSs property.
             * 
             * @param value
             *     allowed object is
             *     {@link IWACCIFSResponse.AREACOMUNICACION.ADSDATOSSALIDA.DATOSs }
             *     
             */
            public void setDATOSs(IWACCIFSResponse.AREACOMUNICACION.ADSDATOSSALIDA.DATOSs value) {
                this.datoSs = value;
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
             *         &lt;element name="DATOS" maxOccurs="10">
             *           &lt;complexType>
             *             &lt;complexContent>
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                 &lt;sequence>
             *                   &lt;element name="ADS-NIFE">
             *                     &lt;simpleType>
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
             *                         &lt;length value="9"/>
             *                       &lt;/restriction>
             *                     &lt;/simpleType>
             *                   &lt;/element>
             *                   &lt;element name="ADS-CO-AGENC">
             *                     &lt;simpleType>
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
             *                         &lt;length value="10"/>
             *                       &lt;/restriction>
             *                     &lt;/simpleType>
             *                   &lt;/element>
             *                   &lt;element name="ADS-FX-RESOL">
             *                     &lt;simpleType>
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
             *                         &lt;length value="8"/>
             *                       &lt;/restriction>
             *                     &lt;/simpleType>
             *                   &lt;/element>
             *                   &lt;element name="ADS-NOMAGE">
             *                     &lt;simpleType>
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
             *                         &lt;length value="55"/>
             *                       &lt;/restriction>
             *                     &lt;/simpleType>
             *                   &lt;/element>
             *                   &lt;element name="ADS-FX-BAJA">
             *                     &lt;simpleType>
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
             *                         &lt;length value="8"/>
             *                       &lt;/restriction>
             *                     &lt;/simpleType>
             *                   &lt;/element>
             *                   &lt;element name="ADS-MOTBAJA">
             *                     &lt;simpleType>
             *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
             *                         &lt;length value="2"/>
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
                "datos"
            })
            public static class DATOSs {

                @XmlElement(name = "DATOS", required = true)
                protected List<IWACCIFSResponse.AREACOMUNICACION.ADSDATOSSALIDA.DATOSs.DATOS> datos;

                /**
                 * Gets the value of the datos property.
                 * 
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the datos property.
                 * 
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getDATOS().add(newItem);
                 * </pre>
                 * 
                 * 
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link IWACCIFSResponse.AREACOMUNICACION.ADSDATOSSALIDA.DATOSs.DATOS }
                 * 
                 * 
                 */
                public List<IWACCIFSResponse.AREACOMUNICACION.ADSDATOSSALIDA.DATOSs.DATOS> getDATOS() {
                    if (datos == null) {
                        datos = new ArrayList<IWACCIFSResponse.AREACOMUNICACION.ADSDATOSSALIDA.DATOSs.DATOS>();
                    }
                    return this.datos;
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
                 *         &lt;element name="ADS-NIFE">
                 *           &lt;simpleType>
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
                 *               &lt;length value="9"/>
                 *             &lt;/restriction>
                 *           &lt;/simpleType>
                 *         &lt;/element>
                 *         &lt;element name="ADS-CO-AGENC">
                 *           &lt;simpleType>
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
                 *               &lt;length value="10"/>
                 *             &lt;/restriction>
                 *           &lt;/simpleType>
                 *         &lt;/element>
                 *         &lt;element name="ADS-FX-RESOL">
                 *           &lt;simpleType>
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
                 *               &lt;length value="8"/>
                 *             &lt;/restriction>
                 *           &lt;/simpleType>
                 *         &lt;/element>
                 *         &lt;element name="ADS-NOMAGE">
                 *           &lt;simpleType>
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
                 *               &lt;length value="55"/>
                 *             &lt;/restriction>
                 *           &lt;/simpleType>
                 *         &lt;/element>
                 *         &lt;element name="ADS-FX-BAJA">
                 *           &lt;simpleType>
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
                 *               &lt;length value="8"/>
                 *             &lt;/restriction>
                 *           &lt;/simpleType>
                 *         &lt;/element>
                 *         &lt;element name="ADS-MOTBAJA">
                 *           &lt;simpleType>
                 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
                 *               &lt;length value="2"/>
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
                    "adsnife",
                    "adscoagenc",
                    "adsfxresol",
                    "adsnomage",
                    "adsfxbaja",
                    "adsmotbaja"
                })
                public static class DATOS {

                    @XmlElement(name = "ADS-NIFE", required = true)
                    protected String adsnife;
                    @XmlElement(name = "ADS-CO-AGENC", required = true)
                    protected String adscoagenc;
                    @XmlElement(name = "ADS-FX-RESOL", required = true)
                    protected String adsfxresol;
                    @XmlElement(name = "ADS-NOMAGE", required = true)
                    protected String adsnomage;
                    @XmlElement(name = "ADS-FX-BAJA", required = true)
                    protected String adsfxbaja;
                    @XmlElement(name = "ADS-MOTBAJA", required = true)
                    protected String adsmotbaja;

                    /**
                     * Gets the value of the adsnife property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getADSNIFE() {
                        return adsnife;
                    }

                    /**
                     * Sets the value of the adsnife property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setADSNIFE(String value) {
                        this.adsnife = value;
                    }

                    /**
                     * Gets the value of the adscoagenc property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getADSCOAGENC() {
                        return adscoagenc;
                    }

                    /**
                     * Sets the value of the adscoagenc property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setADSCOAGENC(String value) {
                        this.adscoagenc = value;
                    }

                    /**
                     * Gets the value of the adsfxresol property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getADSFXRESOL() {
                        return adsfxresol;
                    }

                    /**
                     * Sets the value of the adsfxresol property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setADSFXRESOL(String value) {
                        this.adsfxresol = value;
                    }

                    /**
                     * Gets the value of the adsnomage property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getADSNOMAGE() {
                        return adsnomage;
                    }

                    /**
                     * Sets the value of the adsnomage property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setADSNOMAGE(String value) {
                        this.adsnomage = value;
                    }

                    /**
                     * Gets the value of the adsfxbaja property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getADSFXBAJA() {
                        return adsfxbaja;
                    }

                    /**
                     * Sets the value of the adsfxbaja property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setADSFXBAJA(String value) {
                        this.adsfxbaja = value;
                    }

                    /**
                     * Gets the value of the adsmotbaja property.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getADSMOTBAJA() {
                        return adsmotbaja;
                    }

                    /**
                     * Sets the value of the adsmotbaja property.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setADSMOTBAJA(String value) {
                        this.adsmotbaja = value;
                    }

                }

            }

        }

    }

}
