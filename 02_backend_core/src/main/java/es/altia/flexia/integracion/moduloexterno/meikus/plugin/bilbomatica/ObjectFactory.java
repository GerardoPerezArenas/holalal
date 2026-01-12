
package es.altia.flexia.integracion.moduloexterno.meikus.plugin.bilbomatica;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the es.altia.flexia.integracion.moduloexterno.meikus.plugin.bilbomatica package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GrabarReserva_QNAME = new QName("http://ws.service.ws.w75b.ejie.com/", "grabarReserva");
    private final static QName _GrabarPagoResponse_QNAME = new QName("http://ws.service.ws.w75b.ejie.com/", "grabarPagoResponse");
    private final static QName _EliminarReserva_QNAME = new QName("http://ws.service.ws.w75b.ejie.com/", "eliminarReserva");
    private final static QName _RecuperarAplicacionResponse_QNAME = new QName("http://ws.service.ws.w75b.ejie.com/", "recuperarAplicacionResponse");
    private final static QName _ValidarTerceroIkusResponse_QNAME = new QName("http://ws.service.ws.w75b.ejie.com/", "validarTerceroIkusResponse");
    private final static QName _GrabarPago_QNAME = new QName("http://ws.service.ws.w75b.ejie.com/", "grabarPago");
    private final static QName _RecuperarTerceroIkusResponse_QNAME = new QName("http://ws.service.ws.w75b.ejie.com/", "recuperarTerceroIkusResponse");
    private final static QName _RecuperarAplicacion_QNAME = new QName("http://ws.service.ws.w75b.ejie.com/", "recuperarAplicacion");
    private final static QName _RecuperarListaAplicacionesResponse_QNAME = new QName("http://ws.service.ws.w75b.ejie.com/", "recuperarListaAplicacionesResponse");
    private final static QName _GrabarReservaResponse_QNAME = new QName("http://ws.service.ws.w75b.ejie.com/", "grabarReservaResponse");
    private final static QName _RecuperarEjercicio_QNAME = new QName("http://ws.service.ws.w75b.ejie.com/", "recuperarEjercicio");
    private final static QName _RecuperarEjercicioResponse_QNAME = new QName("http://ws.service.ws.w75b.ejie.com/", "recuperarEjercicioResponse");
    private final static QName _ValidarTerceroIkus_QNAME = new QName("http://ws.service.ws.w75b.ejie.com/", "validarTerceroIkus");
    private final static QName _EliminarReservaResponse_QNAME = new QName("http://ws.service.ws.w75b.ejie.com/", "eliminarReservaResponse");
    private final static QName _GrabarResolucion_QNAME = new QName("http://ws.service.ws.w75b.ejie.com/", "grabarResolucion");
    private final static QName _GrabarResolucionResponse_QNAME = new QName("http://ws.service.ws.w75b.ejie.com/", "grabarResolucionResponse");
    private final static QName _RecuperarListaAplicaciones_QNAME = new QName("http://ws.service.ws.w75b.ejie.com/", "recuperarListaAplicaciones");
    private final static QName _RecuperarTerceroIkus_QNAME = new QName("http://ws.service.ws.w75b.ejie.com/", "recuperarTerceroIkus");
    private final static QName _Exception_QNAME = new QName("http://ws.service.ws.w75b.ejie.com/", "Exception");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: es.altia.flexia.integracion.moduloexterno.meikus.plugin.bilbomatica
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GrabarReserva }
     * 
     */
    public GrabarReserva createGrabarReserva() {
        return new GrabarReserva();
    }

    /**
     * Create an instance of {@link EliminarReserva }
     * 
     */
    public EliminarReserva createEliminarReserva() {
        return new EliminarReserva();
    }

    /**
     * Create an instance of {@link W75BTerceroVO }
     * 
     */
    public W75BTerceroVO createW75BTerceroVO() {
        return new W75BTerceroVO();
    }

    /**
     * Create an instance of {@link RecuperarAplicacion }
     * 
     */
    public RecuperarAplicacion createRecuperarAplicacion() {
        return new RecuperarAplicacion();
    }

    /**
     * Create an instance of {@link RecuperarEjercicioResponse }
     * 
     */
    public RecuperarEjercicioResponse createRecuperarEjercicioResponse() {
        return new RecuperarEjercicioResponse();
    }

    /**
     * Create an instance of {@link EliminarReservaResponse }
     * 
     */
    public EliminarReservaResponse createEliminarReservaResponse() {
        return new EliminarReservaResponse();
    }

    /**
     * Create an instance of {@link W75BResultadoLineaAyudaVO }
     * 
     */
    public W75BResultadoLineaAyudaVO createW75BResultadoLineaAyudaVO() {
        return new W75BResultadoLineaAyudaVO();
    }

    /**
     * Create an instance of {@link W75BSeguridadVO }
     * 
     */
    public W75BSeguridadVO createW75BSeguridadVO() {
        return new W75BSeguridadVO();
    }

    /**
     * Create an instance of {@link W75BResultadoListaAplicacionesVO }
     * 
     */
    public W75BResultadoListaAplicacionesVO createW75BResultadoListaAplicacionesVO() {
        return new W75BResultadoListaAplicacionesVO();
    }

    /**
     * Create an instance of {@link W75BResolucionVO }
     * 
     */
    public W75BResolucionVO createW75BResolucionVO() {
        return new W75BResolucionVO();
    }

    /**
     * Create an instance of {@link GrabarReservaResponse }
     * 
     */
    public GrabarReservaResponse createGrabarReservaResponse() {
        return new GrabarReservaResponse();
    }

    /**
     * Create an instance of {@link W75BLocalizacionVO }
     * 
     */
    public W75BLocalizacionVO createW75BLocalizacionVO() {
        return new W75BLocalizacionVO();
    }

    /**
     * Create an instance of {@link ValidarTerceroIkusResponse }
     * 
     */
    public ValidarTerceroIkusResponse createValidarTerceroIkusResponse() {
        return new ValidarTerceroIkusResponse();
    }

    /**
     * Create an instance of {@link RecuperarTerceroIkusResponse }
     * 
     */
    public RecuperarTerceroIkusResponse createRecuperarTerceroIkusResponse() {
        return new RecuperarTerceroIkusResponse();
    }

    /**
     * Create an instance of {@link Exception }
     * 
     */
    public Exception createException() {
        return new Exception();
    }

    /**
     * Create an instance of {@link W75BCuentaVO }
     * 
     */
    public W75BCuentaVO createW75BCuentaVO() {
        return new W75BCuentaVO();
    }

    /**
     * Create an instance of {@link W75BLineaAyudaVO }
     * 
     */
    public W75BLineaAyudaVO createW75BLineaAyudaVO() {
        return new W75BLineaAyudaVO();
    }

    /**
     * Create an instance of {@link W75BTerceroIkusVO }
     * 
     */
    public W75BTerceroIkusVO createW75BTerceroIkusVO() {
        return new W75BTerceroIkusVO();
    }

    /**
     * Create an instance of {@link RecuperarListaAplicaciones }
     * 
     */
    public RecuperarListaAplicaciones createRecuperarListaAplicaciones() {
        return new RecuperarListaAplicaciones();
    }

    /**
     * Create an instance of {@link W75BResultadoValidarTerceroIkusVO }
     * 
     */
    public W75BResultadoValidarTerceroIkusVO createW75BResultadoValidarTerceroIkusVO() {
        return new W75BResultadoValidarTerceroIkusVO();
    }

    /**
     * Create an instance of {@link GrabarPago }
     * 
     */
    public GrabarPago createGrabarPago() {
        return new GrabarPago();
    }

    /**
     * Create an instance of {@link W75BPagoVO }
     * 
     */
    public W75BPagoVO createW75BPagoVO() {
        return new W75BPagoVO();
    }

    /**
     * Create an instance of {@link RecuperarTerceroIkus }
     * 
     */
    public RecuperarTerceroIkus createRecuperarTerceroIkus() {
        return new RecuperarTerceroIkus();
    }

    /**
     * Create an instance of {@link RecuperarEjercicio }
     * 
     */
    public RecuperarEjercicio createRecuperarEjercicio() {
        return new RecuperarEjercicio();
    }

    /**
     * Create an instance of {@link W75BFormaJuridicaVO }
     * 
     */
    public W75BFormaJuridicaVO createW75BFormaJuridicaVO() {
        return new W75BFormaJuridicaVO();
    }

    /**
     * Create an instance of {@link W75BAplicacionVO }
     * 
     */
    public W75BAplicacionVO createW75BAplicacionVO() {
        return new W75BAplicacionVO();
    }

    /**
     * Create an instance of {@link GrabarResolucionResponse }
     * 
     */
    public GrabarResolucionResponse createGrabarResolucionResponse() {
        return new GrabarResolucionResponse();
    }

    /**
     * Create an instance of {@link W75BResultadoResolucionVO }
     * 
     */
    public W75BResultadoResolucionVO createW75BResultadoResolucionVO() {
        return new W75BResultadoResolucionVO();
    }

    /**
     * Create an instance of {@link W75BReservaVO }
     * 
     */
    public W75BReservaVO createW75BReservaVO() {
        return new W75BReservaVO();
    }

    /**
     * Create an instance of {@link W75BImporteAniosVO }
     * 
     */
    public W75BImporteAniosVO createW75BImporteAniosVO() {
        return new W75BImporteAniosVO();
    }

    /**
     * Create an instance of {@link RecuperarListaAplicacionesResponse }
     * 
     */
    public RecuperarListaAplicacionesResponse createRecuperarListaAplicacionesResponse() {
        return new RecuperarListaAplicacionesResponse();
    }

    /**
     * Create an instance of {@link GrabarResolucion }
     * 
     */
    public GrabarResolucion createGrabarResolucion() {
        return new GrabarResolucion();
    }

    /**
     * Create an instance of {@link GrabarPagoResponse }
     * 
     */
    public GrabarPagoResponse createGrabarPagoResponse() {
        return new GrabarPagoResponse();
    }

    /**
     * Create an instance of {@link W75BResultadoReservaVO }
     * 
     */
    public W75BResultadoReservaVO createW75BResultadoReservaVO() {
        return new W75BResultadoReservaVO();
    }

    /**
     * Create an instance of {@link W75BResultadoAplicacionVO }
     * 
     */
    public W75BResultadoAplicacionVO createW75BResultadoAplicacionVO() {
        return new W75BResultadoAplicacionVO();
    }

    /**
     * Create an instance of {@link ValidarTerceroIkus }
     * 
     */
    public ValidarTerceroIkus createValidarTerceroIkus() {
        return new ValidarTerceroIkus();
    }

    /**
     * Create an instance of {@link W75BDescripcionVO }
     * 
     */
    public W75BDescripcionVO createW75BDescripcionVO() {
        return new W75BDescripcionVO();
    }

    /**
     * Create an instance of {@link W75BTerritorioHistoricoVO }
     * 
     */
    public W75BTerritorioHistoricoVO createW75BTerritorioHistoricoVO() {
        return new W75BTerritorioHistoricoVO();
    }

    /**
     * Create an instance of {@link W75BResultadoTerceroIkusVO }
     * 
     */
    public W75BResultadoTerceroIkusVO createW75BResultadoTerceroIkusVO() {
        return new W75BResultadoTerceroIkusVO();
    }

    /**
     * Create an instance of {@link RecuperarAplicacionResponse }
     * 
     */
    public RecuperarAplicacionResponse createRecuperarAplicacionResponse() {
        return new RecuperarAplicacionResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GrabarReserva }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.service.ws.w75b.ejie.com/", name = "grabarReserva")
    public JAXBElement<GrabarReserva> createGrabarReserva(GrabarReserva value) {
        return new JAXBElement<GrabarReserva>(_GrabarReserva_QNAME, GrabarReserva.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GrabarPagoResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.service.ws.w75b.ejie.com/", name = "grabarPagoResponse")
    public JAXBElement<GrabarPagoResponse> createGrabarPagoResponse(GrabarPagoResponse value) {
        return new JAXBElement<GrabarPagoResponse>(_GrabarPagoResponse_QNAME, GrabarPagoResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EliminarReserva }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.service.ws.w75b.ejie.com/", name = "eliminarReserva")
    public JAXBElement<EliminarReserva> createEliminarReserva(EliminarReserva value) {
        return new JAXBElement<EliminarReserva>(_EliminarReserva_QNAME, EliminarReserva.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RecuperarAplicacionResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.service.ws.w75b.ejie.com/", name = "recuperarAplicacionResponse")
    public JAXBElement<RecuperarAplicacionResponse> createRecuperarAplicacionResponse(RecuperarAplicacionResponse value) {
        return new JAXBElement<RecuperarAplicacionResponse>(_RecuperarAplicacionResponse_QNAME, RecuperarAplicacionResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ValidarTerceroIkusResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.service.ws.w75b.ejie.com/", name = "validarTerceroIkusResponse")
    public JAXBElement<ValidarTerceroIkusResponse> createValidarTerceroIkusResponse(ValidarTerceroIkusResponse value) {
        return new JAXBElement<ValidarTerceroIkusResponse>(_ValidarTerceroIkusResponse_QNAME, ValidarTerceroIkusResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GrabarPago }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.service.ws.w75b.ejie.com/", name = "grabarPago")
    public JAXBElement<GrabarPago> createGrabarPago(GrabarPago value) {
        return new JAXBElement<GrabarPago>(_GrabarPago_QNAME, GrabarPago.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RecuperarTerceroIkusResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.service.ws.w75b.ejie.com/", name = "recuperarTerceroIkusResponse")
    public JAXBElement<RecuperarTerceroIkusResponse> createRecuperarTerceroIkusResponse(RecuperarTerceroIkusResponse value) {
        return new JAXBElement<RecuperarTerceroIkusResponse>(_RecuperarTerceroIkusResponse_QNAME, RecuperarTerceroIkusResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RecuperarAplicacion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.service.ws.w75b.ejie.com/", name = "recuperarAplicacion")
    public JAXBElement<RecuperarAplicacion> createRecuperarAplicacion(RecuperarAplicacion value) {
        return new JAXBElement<RecuperarAplicacion>(_RecuperarAplicacion_QNAME, RecuperarAplicacion.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RecuperarListaAplicacionesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.service.ws.w75b.ejie.com/", name = "recuperarListaAplicacionesResponse")
    public JAXBElement<RecuperarListaAplicacionesResponse> createRecuperarListaAplicacionesResponse(RecuperarListaAplicacionesResponse value) {
        return new JAXBElement<RecuperarListaAplicacionesResponse>(_RecuperarListaAplicacionesResponse_QNAME, RecuperarListaAplicacionesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GrabarReservaResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.service.ws.w75b.ejie.com/", name = "grabarReservaResponse")
    public JAXBElement<GrabarReservaResponse> createGrabarReservaResponse(GrabarReservaResponse value) {
        return new JAXBElement<GrabarReservaResponse>(_GrabarReservaResponse_QNAME, GrabarReservaResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RecuperarEjercicio }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.service.ws.w75b.ejie.com/", name = "recuperarEjercicio")
    public JAXBElement<RecuperarEjercicio> createRecuperarEjercicio(RecuperarEjercicio value) {
        return new JAXBElement<RecuperarEjercicio>(_RecuperarEjercicio_QNAME, RecuperarEjercicio.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RecuperarEjercicioResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.service.ws.w75b.ejie.com/", name = "recuperarEjercicioResponse")
    public JAXBElement<RecuperarEjercicioResponse> createRecuperarEjercicioResponse(RecuperarEjercicioResponse value) {
        return new JAXBElement<RecuperarEjercicioResponse>(_RecuperarEjercicioResponse_QNAME, RecuperarEjercicioResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ValidarTerceroIkus }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.service.ws.w75b.ejie.com/", name = "validarTerceroIkus")
    public JAXBElement<ValidarTerceroIkus> createValidarTerceroIkus(ValidarTerceroIkus value) {
        return new JAXBElement<ValidarTerceroIkus>(_ValidarTerceroIkus_QNAME, ValidarTerceroIkus.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EliminarReservaResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.service.ws.w75b.ejie.com/", name = "eliminarReservaResponse")
    public JAXBElement<EliminarReservaResponse> createEliminarReservaResponse(EliminarReservaResponse value) {
        return new JAXBElement<EliminarReservaResponse>(_EliminarReservaResponse_QNAME, EliminarReservaResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GrabarResolucion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.service.ws.w75b.ejie.com/", name = "grabarResolucion")
    public JAXBElement<GrabarResolucion> createGrabarResolucion(GrabarResolucion value) {
        return new JAXBElement<GrabarResolucion>(_GrabarResolucion_QNAME, GrabarResolucion.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GrabarResolucionResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.service.ws.w75b.ejie.com/", name = "grabarResolucionResponse")
    public JAXBElement<GrabarResolucionResponse> createGrabarResolucionResponse(GrabarResolucionResponse value) {
        return new JAXBElement<GrabarResolucionResponse>(_GrabarResolucionResponse_QNAME, GrabarResolucionResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RecuperarListaAplicaciones }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.service.ws.w75b.ejie.com/", name = "recuperarListaAplicaciones")
    public JAXBElement<RecuperarListaAplicaciones> createRecuperarListaAplicaciones(RecuperarListaAplicaciones value) {
        return new JAXBElement<RecuperarListaAplicaciones>(_RecuperarListaAplicaciones_QNAME, RecuperarListaAplicaciones.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RecuperarTerceroIkus }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.service.ws.w75b.ejie.com/", name = "recuperarTerceroIkus")
    public JAXBElement<RecuperarTerceroIkus> createRecuperarTerceroIkus(RecuperarTerceroIkus value) {
        return new JAXBElement<RecuperarTerceroIkus>(_RecuperarTerceroIkus_QNAME, RecuperarTerceroIkus.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Exception }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.service.ws.w75b.ejie.com/", name = "Exception")
    public JAXBElement<Exception> createException(Exception value) {
        return new JAXBElement<Exception>(_Exception_QNAME, Exception.class, null, value);
    }

}
