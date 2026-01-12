package es.altia.flexia.integracion.moduloexterno.melanbide04;

import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExternoCampoSupFactoria;
import es.altia.flexia.integracion.moduloexterno.plugin.camposuplementario.IModuloIntegracionExternoCamposFlexia;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.CampoSuplementarioModuloIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.SalidaIntegracionVO;
import java.io.PrintWriter;
import java.util.ResourceBundle;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class MELANBIDE04 extends ModuloIntegracionExterno
{
  private Logger log = LogManager.getLogger(MELANBIDE04.class);
  private final String CAMPO_SUPLEMENTARIO_BASE_COTIZACION = "/CAMPO_SUPLEMENTARIO_BASE_COTIZACION";
  private final String CAMPO_SUPLEMENTARIO_DURACION_CONTRATO_MESES = "/CAMPO_SUPLEMENTARIO_DURACION_CONTRATO_MESES";
  private final String CAMPO_SUPLEMENTARIO_DURACION_CONTRATO_DIAS = "/CAMPO_SUPLEMENTARIO_DURACION_CONTRATO_DIAS";
  private final String CAMPO_SUPLEMENTARIO_SUBVENCION_TOTAL = "/CAMPO_SUPLEMENTARIO_SUBVENCION_TOTAL";
  private final String CAMPO_SUPLEMENTARIO_PRIMER_PAGO_60 = "/CAMPO_SUPLEMENTARIO_PRIMER_PAGO_60";
  private final String CAMPO_SUPLEMENTARIO_SEGUNDO_PAGO_40 = "/CAMPO_SUPLEMENTARIO_SEGUNDO_PAGO_40";
  private final String CAMPO_SUPLEMENTARIO_CANTIDAD_SOBRANTE_NO_PAGADA = "/CAMPO_SUPLEMENTARIO_CANTIDAD_SOBRANTE_NO_PAGADA";
  private final String CAMPO_SUPLEMENTARIO_CANTIDAD_TOTAL_PAGADA = "/CAMPO_SUPLEMENTARIO_CANTIDAD_TOTAL_PAGADA";

  private final String MODULO_INTEGRACION = "/MODULO_INTEGRACION/";
  private final String PANTALLA_EXPEDIENTE_SALIDA = "/PANTALLA_EXPEDIENTE/SALIDA";

  public String prepararPantallaCalculo(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
  {
    String redireccion = null;

    if (this.log.isDebugEnabled()) this.log.debug("prepararPantallaCalculo ( codOrganizacion = " + codOrganizacion + " codTramite = " + codTramite + " ocurrenciaTramite = " + ocurrenciaTramite + " numExpediente = " + numExpediente + " ) : BEGIN");

    try
    {
      IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();

      ResourceBundle config = ResourceBundle.getBundle("MELANBIDE04");

      String campoBaseCotizacion = config.getString(codOrganizacion + "/CAMPO_SUPLEMENTARIO_BASE_COTIZACION");
      String campoContratoMeses = config.getString(codOrganizacion + "/CAMPO_SUPLEMENTARIO_DURACION_CONTRATO_MESES");
      String campoContratoDias = config.getString(codOrganizacion + "/CAMPO_SUPLEMENTARIO_DURACION_CONTRATO_DIAS");
      String campoTotalPagada = config.getString(codOrganizacion + "/CAMPO_SUPLEMENTARIO_CANTIDAD_TOTAL_PAGADA");

      String campoImportePrimerPago = config.getString(codOrganizacion + "/CAMPO_SUPLEMENTARIO_PRIMER_PAGO_60");
      String campoImporteSegundoPago = config.getString(codOrganizacion + "/CAMPO_SUPLEMENTARIO_SEGUNDO_PAGO_40");

      this.log.debug("campoBaseCotizacion: " + campoBaseCotizacion);
      this.log.debug("campoContratoMeses: " + campoContratoMeses);
      this.log.debug("campoContratoDias: " + campoContratoDias);
      this.log.debug("campoTotalPagada: " + campoTotalPagada);

      String[] datos = numExpediente.split("/");
      String ejercicio = datos[0];
      String codProcedimiento = datos[1];

      SalidaIntegracionVO sCampoBaseCotizacion = el.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), datos[0], numExpediente, datos[1], campoBaseCotizacion, 6);
      SalidaIntegracionVO sCampoContratoMeses = el.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), datos[0], numExpediente, datos[1], campoContratoMeses, 1);
      SalidaIntegracionVO sCampoContratoDias = el.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), datos[0], numExpediente, datos[1], campoContratoDias, 1);

      SalidaIntegracionVO IMPORTE_PRIMER_PAGO = el.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento, campoImportePrimerPago, 1);
      SalidaIntegracionVO IMPORTE_SEGUNDO_PAGO = el.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento, campoImporteSegundoPago, 1);

      String sBaseCotizacion = "";
      String sNumContratoMeses = "";
      String sNumContratoDias = "";

      if ((sCampoBaseCotizacion.getStatus() == 0) && (sCampoContratoMeses.getStatus() == 0) && (sCampoContratoDias.getStatus() == 0))
      {
        sBaseCotizacion = sCampoBaseCotizacion.getCampoSuplementario().getDescripcionValorDesplegable();

        sNumContratoMeses = sCampoContratoMeses.getCampoSuplementario().getValorNumero();
        sNumContratoDias = sCampoContratoDias.getCampoSuplementario().getValorNumero();

        this.log.debug("sBaseCotizacion: " + sBaseCotizacion + ", sNumContratoMeses: " + sNumContratoMeses + ",sNumContratoDias: " + sNumContratoDias);

        Double dBaseCotizacion = new Double(sBaseCotizacion.split("|")[0].replaceAll(",", "."));
        Double dNumContratoMeses = new Double(sNumContratoMeses);
        Double dNumContratoDias = new Double(sNumContratoDias);

        double subvencionTotal = redondearDosDecimales(dBaseCotizacion.doubleValue() * 0.183D * 0.3D * (dNumContratoMeses.doubleValue() + dNumContratoDias.doubleValue() / 30.0D));
        double importePrimerPago = redondearDosDecimales(subvencionTotal * 0.6D);
        double importeSegundoPago = redondearDosDecimales(subvencionTotal * 0.4D);

        double valorCampoSupImportePrimerPago = 0.0D;
        double valorCampoSupImporteSegundoPago = 0.0D;

        if ((IMPORTE_PRIMER_PAGO != null) && (IMPORTE_PRIMER_PAGO.getStatus() == 0) && 
          (IMPORTE_PRIMER_PAGO.getCampoSuplementario().getValorNumero() != null) && (!IMPORTE_PRIMER_PAGO.getCampoSuplementario().getValorNumero().equals(""))) {
          valorCampoSupImportePrimerPago = new Double(IMPORTE_PRIMER_PAGO.getCampoSuplementario().getValorNumero()).doubleValue();
        }

        if ((IMPORTE_SEGUNDO_PAGO != null) && (IMPORTE_SEGUNDO_PAGO.getStatus() == 0) && 
          (IMPORTE_SEGUNDO_PAGO.getCampoSuplementario().getValorNumero() != null) && (!IMPORTE_SEGUNDO_PAGO.getCampoSuplementario().getValorNumero().equals(""))) {
          valorCampoSupImporteSegundoPago = new Double(IMPORTE_SEGUNDO_PAGO.getCampoSuplementario().getValorNumero()).doubleValue();
        }

        double cantidadTotalPagada = redondearDosDecimales(valorCampoSupImportePrimerPago + valorCampoSupImporteSegundoPago);
        double cantidadSobranteNoPagada = redondearDosDecimales(subvencionTotal - cantidadTotalPagada);

        request.setAttribute("subvencionTotal", new Double(subvencionTotal));
        request.setAttribute("importePrimerPago", new Double(importePrimerPago));
        request.setAttribute("importeSegundoPago", new Double(importeSegundoPago));
        request.setAttribute("cantidadSobranteNoPagada", new Double(cantidadSobranteNoPagada));
        request.setAttribute("cantidadTotalPagada", new Double(cantidadTotalPagada));
      }
      else
      {
        request.setAttribute("error_campos_suplementarios_origen", "SI");
      }
      redireccion = config.getString(codOrganizacion + "/MODULO_INTEGRACION/" + getNombreModulo() + "/" + codProcedimiento + "/PANTALLA_EXPEDIENTE/SALIDA");
    } catch (Exception e) {
      e.printStackTrace();
      redireccion = null;
    }

    return redireccion;
  }

  public String recalcular(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
  {
    int status = -1;
    double subvencionTotal = 0.0D;
    double importePrimerPago = 0.0D;
    double importeSegundoPago = 0.0D;
    double cantidadTotalSobrante = 0.0D;
    double cantidadTotalPagada = 0.0D;
    boolean errorLeerPropiedades = false;
    String campoBaseCotizacion = null;
    String campoContratoMeses = null;
    String campoContratoDias = null;
    String campoTotalPagada = null;

    String campoImportePrimerPago = null;
    String campoImporteSegundoPago = null;

    if (this.log.isDebugEnabled()) this.log.debug("recalcular ( codOrganizacion = " + codOrganizacion + " codTramite = " + codTramite + " ocurrenciaTramite = " + ocurrenciaTramite + " numExpediente = " + numExpediente + " ) : BEGIN");
    IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
    try
    {
      ResourceBundle config = ResourceBundle.getBundle(getNombreModulo());
      campoBaseCotizacion = config.getString(codOrganizacion + "/CAMPO_SUPLEMENTARIO_BASE_COTIZACION");
      campoContratoMeses = config.getString(codOrganizacion + "/CAMPO_SUPLEMENTARIO_DURACION_CONTRATO_MESES");
      campoContratoDias = config.getString(codOrganizacion + "/CAMPO_SUPLEMENTARIO_DURACION_CONTRATO_DIAS");
      campoTotalPagada = config.getString(codOrganizacion + "/CAMPO_SUPLEMENTARIO_CANTIDAD_TOTAL_PAGADA");

      campoImportePrimerPago = config.getString(codOrganizacion + "/CAMPO_SUPLEMENTARIO_PRIMER_PAGO_60");
      campoImporteSegundoPago = config.getString(codOrganizacion + "/CAMPO_SUPLEMENTARIO_SEGUNDO_PAGO_40");

      this.log.debug("campoBaseCotizacion: " + campoBaseCotizacion);
      this.log.debug("campoContratoMeses: " + campoContratoMeses);
      this.log.debug("campoContratoDias: " + campoContratoDias);
      this.log.debug("campoTotalPagada: " + campoTotalPagada);
    }
    catch (Exception e) {
      e.printStackTrace();
      errorLeerPropiedades = true;
      status = -1;
    }

    if (!errorLeerPropiedades) {
      try {
        String[] datos = numExpediente.split("/");
        String ejercicio = datos[0];
        String codProcedimiento = datos[1];

        SalidaIntegracionVO sCampoBaseCotizacion = el.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento, campoBaseCotizacion, 6);
        SalidaIntegracionVO sCampoContratoMeses = el.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento, campoContratoMeses, 1);
        SalidaIntegracionVO sCampoContratoDias = el.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento, campoContratoDias, 1);

        SalidaIntegracionVO IMPORTE_PRIMER_PAGO = el.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento, campoImportePrimerPago, 1);
        SalidaIntegracionVO IMPORTE_SEGUNDO_PAGO = el.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento, campoImporteSegundoPago, 1);

        String sBaseCotizacion = "";
        String sNumContratoMeses = "";
        String sNumContratoDias = "";

        if ((sCampoBaseCotizacion.getStatus() == 0) && (sCampoContratoMeses.getStatus() == 0) && (sCampoContratoDias.getStatus() == 0))
        {
          sBaseCotizacion = sCampoBaseCotizacion.getCampoSuplementario().getDescripcionValorDesplegable().replaceAll(",", ".");
          sNumContratoMeses = sCampoContratoMeses.getCampoSuplementario().getValorNumero();
          sNumContratoDias = sCampoContratoDias.getCampoSuplementario().getValorNumero();

          this.log.debug("sBaseCotizacion: " + sBaseCotizacion + ",sNumContratoMeses: " + sNumContratoMeses + ",sNumContratoDias: " + sNumContratoDias);
          Double dBaseCotizacion = new Double(sBaseCotizacion);
          Double dNumContratoMeses = new Double(sNumContratoMeses);
          Double dNumContratoDias = new Double(sNumContratoDias);

          subvencionTotal = redondearDosDecimales(dBaseCotizacion.doubleValue() * 0.183D * 0.3D * (dNumContratoMeses.doubleValue() + dNumContratoDias.doubleValue() / 30.0D));
          importePrimerPago = redondearDosDecimales(subvencionTotal * 0.6D);
          importeSegundoPago = redondearDosDecimales(subvencionTotal * 0.4D);

          double valorCampoSupImportePrimerPago = 0.0D;
          double valorCampoSupImporteSegundoPago = 0.0D;

          if ((IMPORTE_PRIMER_PAGO != null) && (IMPORTE_PRIMER_PAGO.getStatus() == 0) && 
            (IMPORTE_PRIMER_PAGO.getCampoSuplementario().getValorNumero() != null) && (!IMPORTE_PRIMER_PAGO.getCampoSuplementario().getValorNumero().equals(""))) {
            valorCampoSupImportePrimerPago = new Double(IMPORTE_PRIMER_PAGO.getCampoSuplementario().getValorNumero()).doubleValue();
          }

          if ((IMPORTE_SEGUNDO_PAGO != null) && (IMPORTE_SEGUNDO_PAGO.getStatus() == 0) && 
            (IMPORTE_SEGUNDO_PAGO.getCampoSuplementario().getValorNumero() != null) && (!IMPORTE_SEGUNDO_PAGO.getCampoSuplementario().getValorNumero().equals(""))) {
            valorCampoSupImporteSegundoPago = new Double(IMPORTE_SEGUNDO_PAGO.getCampoSuplementario().getValorNumero()).doubleValue();
          }

          cantidadTotalPagada = redondearDosDecimales(valorCampoSupImportePrimerPago + valorCampoSupImporteSegundoPago);
          cantidadTotalSobrante = redondearDosDecimales(subvencionTotal - cantidadTotalPagada);

          status = 0;
        } else {
          status = -2;
        }
      } catch (Exception e) {
        e.printStackTrace();
        status = -3;
      }
    }
    try
    {
      response.setContentType("TEXT/XML");
      response.setCharacterEncoding("ISO-8859-1");
      PrintWriter out = response.getWriter();

      StringBuffer sb = new StringBuffer();
      sb.append("<RESPUESTA>");
      sb.append("<STATUS>");
      sb.append(status);
      sb.append("</STATUS>");
      if (status == 0) {
        sb.append("<SUBVENCION_TOTAL>");
        sb.append(subvencionTotal);
        sb.append("</SUBVENCION_TOTAL>");

        sb.append("<IMPORTE_SUBVENCION_TOTAL>");
        sb.append(subvencionTotal);
        sb.append("</IMPORTE_SUBVENCION_TOTAL>");

        sb.append("<IMPORTE_PRIMER_PAGO>");
        sb.append(importePrimerPago);
        sb.append("</IMPORTE_PRIMER_PAGO>");

        sb.append("<IMPORTE_SEGUNDO_PAGO>");
        sb.append(importeSegundoPago);
        sb.append("</IMPORTE_SEGUNDO_PAGO>");

        sb.append("<CANTIDAD_SOBRANTE_NO_PAGADA>");
        sb.append(cantidadTotalSobrante);
        sb.append("</CANTIDAD_SOBRANTE_NO_PAGADA>");

        sb.append("<CANTIDAD_TOTAL_PAGADA>");
        sb.append(cantidadTotalPagada);
        sb.append("</CANTIDAD_TOTAL_PAGADA>");
      }
      sb.append("</RESPUESTA>");

      out.println(sb.toString());
      out.flush();
      out.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  private boolean isDouble(String dato)
  {
    boolean exito = false;
    try {
      Double.parseDouble(dato);
      exito = true;
    } catch (Exception e) {
      exito = false;
    }
    return exito;
  }

  private boolean esDistintoNullYEspacioBlanco(String dato)
  {
    return (dato != null) && (!"".equalsIgnoreCase(dato));
  }

  public String grabarCalculosCampos(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
  {
    int status = -1;

    boolean errorLeerPropiedades = false;
    String campoCantidadPagada = null;
    String pSubvencionTotal = null;
    String pCantidadSobranteNoPagada = null;

    String ejercicio = null;
    String codProcedimiento = null;

    if (this.log.isDebugEnabled()) this.log.debug("recalcular ( codOrganizacion = " + codOrganizacion + " codTramite = " + codTramite + " ocurrenciaTramite = " + ocurrenciaTramite + " numExpediente = " + numExpediente + " ) : BEGIN");
    IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();

    String paramSubvencionTotal = request.getParameter("subvencionTotal");
    String paramImportePrimerPago = request.getParameter("importePrimerPago");
    String paramImporteSegundoPago = request.getParameter("importeSegundoPago");
    String paramCantidadSobranteNoPagada = request.getParameter("cantidadSobranteNoPagada");
    String paramCantidadTotalPagada = request.getParameter("cantidadTotalPagada");
    try
    {
      ResourceBundle config = ResourceBundle.getBundle(getNombreModulo());
      campoCantidadPagada = config.getString(codOrganizacion + "/CAMPO_SUPLEMENTARIO_CANTIDAD_TOTAL_PAGADA");
      pSubvencionTotal = config.getString(codOrganizacion + "/CAMPO_SUPLEMENTARIO_SUBVENCION_TOTAL");
      pCantidadSobranteNoPagada = config.getString(codOrganizacion + "/CAMPO_SUPLEMENTARIO_CANTIDAD_SOBRANTE_NO_PAGADA");
    }
    catch (Exception e) {
      e.printStackTrace();
      errorLeerPropiedades = true;
      status = -1;
    }

    if (!errorLeerPropiedades) {
      try
      {
        String[] datos = numExpediente.split("/");
        ejercicio = datos[0];
        codProcedimiento = datos[1];

        double subvencionTotal = 0.0D;
        double importePrimerPago = 0.0D;
        double importeSegundoPago = 0.0D;
        double cantidadSobranteNoPagada = 0.0D;
        double cantidadTotalPagada = 0.0D;

        if (esDistintoNullYEspacioBlanco(paramSubvencionTotal)) {
          paramSubvencionTotal = paramSubvencionTotal.replaceAll(",", ".");
          if (isDouble(paramSubvencionTotal)) subvencionTotal = new Double(paramSubvencionTotal).doubleValue();

        }

        if (esDistintoNullYEspacioBlanco(paramImportePrimerPago)) {
          paramImportePrimerPago = paramImportePrimerPago.replaceAll(",", ".");
          if (isDouble(paramImportePrimerPago)) {
            importePrimerPago = new Double(paramImportePrimerPago).doubleValue();
          }

        }

        if (esDistintoNullYEspacioBlanco(paramImporteSegundoPago)) {
          paramImporteSegundoPago = paramImporteSegundoPago.replaceAll(",", ".");

          if (isDouble(paramImporteSegundoPago)) {
            importeSegundoPago = new Double(paramImporteSegundoPago).doubleValue();
          }

        }

        if (esDistintoNullYEspacioBlanco(paramCantidadSobranteNoPagada)) {
          paramCantidadSobranteNoPagada = paramCantidadSobranteNoPagada.replaceAll(",", ".");
          if (isDouble(paramCantidadSobranteNoPagada)) {
            cantidadSobranteNoPagada = new Double(paramCantidadSobranteNoPagada).doubleValue();
          }

        }

        if (esDistintoNullYEspacioBlanco(paramCantidadTotalPagada)) {
          paramCantidadTotalPagada = paramCantidadTotalPagada.replaceAll(",", ".");

          if (isDouble(paramCantidadTotalPagada)) {
            cantidadTotalPagada = new Double(paramCantidadTotalPagada).doubleValue();
          }

        }

        CampoSuplementarioModuloIntegracionVO campo = new CampoSuplementarioModuloIntegracionVO();
        campo.setTramite(false);
        campo.setCodOrganizacion(Integer.toString(codOrganizacion));
        campo.setCodProcedimiento(codProcedimiento);
        campo.setNumExpediente(numExpediente);
        campo.setEjercicio(ejercicio);
        campo.setCodigoCampo(pSubvencionTotal);
        campo.setValorNumero(Double.toString(subvencionTotal));
        campo.setTipoCampo(1);

        SalidaIntegracionVO salidaGrabarSubvencionTotal = el.grabarCampoSuplementario(campo);
        this.log.debug("salidaGrabarSubvencionTotal.status: " + salidaGrabarSubvencionTotal.getStatus());
        this.log.debug("salidaGrabarSubvencionTotal.descStatus: " + salidaGrabarSubvencionTotal.getDescStatus());

        CampoSuplementarioModuloIntegracionVO campoSobranteNoPagado = new CampoSuplementarioModuloIntegracionVO();
        campoSobranteNoPagado.setTramite(false);
        campoSobranteNoPagado.setCodOrganizacion(Integer.toString(codOrganizacion));
        campoSobranteNoPagado.setCodProcedimiento(codProcedimiento);
        campoSobranteNoPagado.setNumExpediente(numExpediente);
        campoSobranteNoPagado.setEjercicio(ejercicio);
        campoSobranteNoPagado.setCodigoCampo(pCantidadSobranteNoPagada);
        campoSobranteNoPagado.setValorNumero(Double.toString(cantidadSobranteNoPagada));
        campoSobranteNoPagado.setTipoCampo(1);
        SalidaIntegracionVO salidaGrabarSobranteNoPagado = el.grabarCampoSuplementario(campoSobranteNoPagado);
        this.log.debug("salidaGrabarSobranteNoPagado.status: " + salidaGrabarSobranteNoPagado.getStatus());
        this.log.debug("salidaGrabarSobranteNoPagado.descStatus: " + salidaGrabarSobranteNoPagado.getDescStatus());

        CampoSuplementarioModuloIntegracionVO campoTotalPagado = new CampoSuplementarioModuloIntegracionVO();
        campoTotalPagado.setTramite(false);
        campoTotalPagado.setCodOrganizacion(Integer.toString(codOrganizacion));
        campoTotalPagado.setCodProcedimiento(codProcedimiento);
        campoTotalPagado.setNumExpediente(numExpediente);
        campoTotalPagado.setEjercicio(ejercicio);
        campoTotalPagado.setCodigoCampo(campoCantidadPagada);
        campoTotalPagado.setValorNumero(Double.toString(cantidadTotalPagada));
        campoTotalPagado.setTipoCampo(1);
        SalidaIntegracionVO salidaGrabarTotalPagado = el.grabarCampoSuplementario(campoTotalPagado);
        this.log.debug("salidaGrabarTotalPagado.status: " + salidaGrabarTotalPagado.getStatus());
        this.log.debug("salidaGrabarTotalPagado.descStatus: " + salidaGrabarTotalPagado.getDescStatus());

        status = 0;
      }
      catch (Exception e)
      {
        e.printStackTrace();
        status = -3;
      }
    }

    try
    {
      response.setContentType("TEXT/XML");
      response.setCharacterEncoding("ISO-8859-1");
      PrintWriter out = response.getWriter();

      StringBuffer sb = new StringBuffer();
      sb.append("<RESPUESTA>");
      sb.append("<STATUS>");
      sb.append(status);
      sb.append("</STATUS>");
      sb.append("</RESPUESTA>");

      out.println(sb.toString());
      out.flush();
      out.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  private double redondearDosDecimales(double numero)
  {
    return Math.rint(numero * 100.0D) / 100.0D;
  }
}