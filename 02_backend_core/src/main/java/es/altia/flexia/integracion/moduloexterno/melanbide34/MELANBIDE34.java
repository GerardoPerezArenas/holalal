/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide34;


import com.google.gson.Gson;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.melanbide34.dao.MeLanbide34DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide34.i18n.MeLanbide34I18n;
import es.altia.flexia.integracion.moduloexterno.melanbide34.manager.MeLanbide34Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide34.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide34.util.ConstantesMeLanbide34;
import es.altia.flexia.integracion.moduloexterno.melanbide34.vo.DesplegableAdmonLocalVO;
import es.altia.flexia.integracion.moduloexterno.melanbide34.vo.FilaMinimisVO;
import es.altia.flexia.integracion.moduloexterno.melanbide34.vo.SelectItem;
import es.altia.flexia.integracion.moduloexterno.melanbide34.vo.ValoresCalculo;
import es.altia.flexia.integracion.moduloexterno.melanbide34.vo.ValoresPago;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;
import es.altia.technical.PortableContext;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class MELANBIDE34 extends ModuloIntegracionExterno
{
    
    //Logger
    private static Logger log = LogManager.getLogger(MELANBIDE34.class);   
    
    private String msgValidacion = "";
    
    public void cargarExpedienteExtension(int codigoOrganizacion, String numeroExpediente, String xml) throws Exception{
        log.info("cargarExpedienteExtension begin");
        final Class cls = Class.forName("es.altia.flexia.integracion.moduloexterno.melanbide42.MELANBIDE42");
        final Object me42Class = cls.newInstance();
        final Class[] types = {int.class, String.class, String.class};
        final Method method = cls.getMethod("cargarExpedienteExtension", types);
        method.invoke(me42Class, codigoOrganizacion, numeroExpediente, xml);
    }
    
    public String cargarPantalla(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
        ValoresCalculo valoresCalculo = new ValoresCalculo();
        String ejercicio = "";
        String mensaje = "";
        int codIdioma = 1;
        try
        {
            if(request.getParameter("idioma") != null)
            {
                codIdioma = Integer.parseInt((String)request.getParameter("idioma"));
            }
        }
        catch(Exception ex)
        {

        }
        try
        {
            String[] datos = numExpediente.split(ConstantesMeLanbide34.BARRA_SEPARADORA);
            ejercicio = datos[0];
            ValoresCalculo v = cargarValoresCalculo(codOrganizacion, numExpediente, ejercicio);
            if(v != null && v.getImpSolic() != null)
            {
                if(validarDatos(v, codIdioma))
                {
                    v = this.realizarCalculos(v);
                    valoresCalculo = v;
                }
                else
                {
                    mensaje = msgValidacion;
                }
            }
            else
            {   
                mensaje = MeLanbide34I18n.getInstance().getMensaje(codIdioma, "msg.error.noDatos");
            }
            List<SelectItem> listaResultado =  MeLanbide34Manager.getInstance().getListaDesplegable(this.getAdaptSQLBD(String.valueOf(codOrganizacion)), ConstantesMeLanbide34.TIPO_DESPL_BOOL);
            request.setAttribute("lstResultado", listaResultado);    
            List<SelectItem> listaMotivoDen =  MeLanbide34Manager.getInstance().getListaDesplegable(this.getAdaptSQLBD(String.valueOf(codOrganizacion)), ConstantesMeLanbide34.TIPO_DESPL_MDEN);
            request.setAttribute("lstMotivoDen", listaMotivoDen); 
            
        }
        catch(Exception ex)
        {
            mensaje = MeLanbide34I18n.getInstance().getMensaje(codIdioma, "msg.error.noDatos");
        }
        request.setAttribute("valoresCalculo", valoresCalculo);
        request.setAttribute("mensaje", mensaje);
        
        boolean deshabilitado = true;
        try
        {
            Long tramAbierto = MeLanbide34Manager.getInstance().getCodigoUltimoTramiteAbierto(codOrganizacion, ejercicio, numExpediente, 1L, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            if(tramAbierto != null)
            {
                if(tramAbierto.toString().equalsIgnoreCase(ConstantesMeLanbide34.CODIGO_TRAM_ETECNICO))
                {
                    deshabilitado = false;
                }
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
           if(log.isDebugEnabled()) log.debug("ERROR  tramite ");
        }
        request.setAttribute("deshabilitado", deshabilitado);
        return "/jsp/extension/melanbide34/melanbide34_ET.jsp";
    }
    
    public void recalcular(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        String mensaje = "";
        String codigoOperacion = "0";
        ValoresCalculo valoresCalculo = new ValoresCalculo();
        int codIdioma = 1;
        try
        {
            if(request.getParameter("idioma") != null)
            {
                codIdioma = Integer.parseInt((String)request.getParameter("idioma"));
            }
        }
        catch(Exception ex)
        {

        }
        try
        {
            String p1_str = (String)request.getParameter("p1");
            String p2_str = (String)request.getParameter("p2");
            String p3_str = (String)request.getParameter("p3");
            String p4_str = (String)request.getParameter("p4");
            
            String[] datos = numExpediente.split(ConstantesMeLanbide34.BARRA_SEPARADORA);
            String ejercicio = datos[0];
            
            ValoresCalculo v = cargarValoresCalculo(codOrganizacion, numExpediente, ejercicio);
            
            if(v != null && v.getImpSolic() != null)
            {
                v.setP1(p1_str);
                v.setP2(p2_str);
                v.setP3(p3_str);
                v.setP4(p4_str);
                if(validarDatos(v, codIdioma))
                {
                    v = this.realizarCalculos(v);
                    valoresCalculo = v;
                }
                else
                {
                    mensaje = msgValidacion;
                }
            }
            else
            {   
                mensaje = MeLanbide34I18n.getInstance().getMensaje(codIdioma, "msg.error.noDatos");
            }
        }
        catch(Exception ex)
        {
            
        }
        escribirRespuesta(valoresCalculo, codigoOperacion, mensaje, request, response);
    }
    
    public String cargarPantallaSubSolic(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarPantallaSubvenciones de " + this.getClass().getName());
        AdaptadorSQLBD adapt = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (Exception ex) {
            log.error(this.getClass().getName() + " Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }
        String url = "/jsp/extension/melanbide34/subSolic.jsp";
        request.setAttribute("numExp", numExpediente);
        if (adapt != null) {
            try {
                List<FilaMinimisVO> listaMinimis = MeLanbide34Manager.getInstance().getDatosMinimis(numExpediente, codOrganizacion, adapt);
                if (listaMinimis.size() > 0) {
                    for (FilaMinimisVO lm : listaMinimis) {
                        lm.setDesEstado(getDescripcionDesplegable(request, lm.getDesEstado()));
                    }
                    request.setAttribute("listaMinimis", listaMinimis);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                log.error("Error al recuperar los datos de minimis - MELANBIDE34 - cargarPantallaSubvenciones", ex);
            }
        }
        return url;
    }    
    
    public void refrescarPantalla(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        String mensaje = "";
        String codigoOperacion = "0";
        ValoresCalculo valoresCalculo = new ValoresCalculo();
        int codIdioma = 1;
        try
        {
            if(request.getParameter("idioma") != null)
            {
                codIdioma = Integer.parseInt((String)request.getParameter("idioma"));
            }
        }
        catch(Exception ex)
        {

        }
        try
        {
            String[] datos = numExpediente.split(ConstantesMeLanbide34.BARRA_SEPARADORA);
            String ejercicio = datos[0];
            
            ValoresCalculo v = cargarValoresCalculo(codOrganizacion, numExpediente, ejercicio);
            
            if(v != null && v.getImpSolic() != null)
            {
                if(validarDatos(v, codIdioma))
                {
                    v = this.realizarCalculos(v);
                    valoresCalculo = v;
                }
                else
                {
                    mensaje = msgValidacion;
                }
            }
            else
            {   
                mensaje = MeLanbide34I18n.getInstance().getMensaje(codIdioma, "msg.error.noDatos");
            }
        }
        catch(Exception ex)
        {
            
        }
        
        boolean deshabilitado = true;
        try
        {
            String[] datos = numExpediente.split(ConstantesMeLanbide34.BARRA_SEPARADORA);
            String ejercicio = datos[0];
            Long tramAbierto = MeLanbide34Manager.getInstance().getCodigoUltimoTramiteAbierto(codOrganizacion, ejercicio, numExpediente, 1L, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            if(tramAbierto != null)
            {
                if(tramAbierto.toString().equalsIgnoreCase(ConstantesMeLanbide34.CODIGO_TRAM_ETECNICO))
                {
                    deshabilitado = false;
                }
            }
        }
        catch(Exception ex)
        {
            
        }
        request.setAttribute("deshabilitado", deshabilitado);
        escribirRespuesta(valoresCalculo, codigoOperacion, mensaje, request, response);
    }
    
    public void guardar(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    { 
        ValoresCalculo valoresCalculo = new ValoresCalculo();
        String ejercicio = "";
        String mensaje = "";
        String codigoOperacion = "0";
        int codIdioma = 1;
        try
        {
            if(request.getParameter("idioma") != null)
            {
                codIdioma = Integer.parseInt((String)request.getParameter("idioma"));
            }
        }
        catch(Exception ex)
        {

        }
        
        String p1 = (String)request.getParameter("p1");
        String p2 = (String)request.getParameter("p2");
        String p3 = (String)request.getParameter("p3");
        String p4 = (String)request.getParameter("p4");
        String concedida = (String)request.getParameter("concedida");
        String fecEstudio = (String)request.getParameter("fecEstudio");        
        String resul = (String)request.getParameter("resul");
        String motden1 = (String)request.getParameter("motden1");
        String motden2 = (String)request.getParameter("motden2");
        String motden3 = (String)request.getParameter("motden3");
        
        
        try
        {
            String[] datos = numExpediente.split(ConstantesMeLanbide34.BARRA_SEPARADORA);
            ejercicio = datos[0];
            ValoresCalculo v = cargarValoresCalculo(codOrganizacion, numExpediente, ejercicio);
            if(v != null && v.getImpSolic() != null)
            {
                v.setP1(p1);
                v.setP2(p2);
                v.setP3(p3);
                v.setP4(p4);
                v.setConcedida(concedida);
                v.setFecEstudio(fecEstudio);
                v.setResulSubv(resul);
                v.setMotivoDen1(motden1);
                v.setMotivoDen2(motden2);
                v.setMotivoDen3(motden3);
                
                if(validarDatos(v, codIdioma))
                {
                    v = this.realizarCalculos(v);
                    valoresCalculo = v;
                    boolean correcto = MeLanbide34Manager.getInstance().guardarValoresCalculo(codOrganizacion, valoresCalculo, numExpediente, ejercicio, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    if(correcto)
                    {
                        mensaje = MeLanbide34I18n.getInstance().getMensaje(codIdioma, "msg.datosGuardados");
                    }
                    else
                    {
                        mensaje = MeLanbide34I18n.getInstance().getMensaje(codIdioma, "msg.error.datosNoGuardados");
                        codigoOperacion = "3";
                    }
                }
                else
                {
                    codigoOperacion = "2";
                    mensaje = msgValidacion;
                }
            }
            else
            {   
                codigoOperacion = "1";
                mensaje = MeLanbide34I18n.getInstance().getMensaje(codIdioma, "msg.error.noDatos");
            }
        }
        catch(Exception ex)
        {
            codigoOperacion = "3";
            mensaje = MeLanbide34I18n.getInstance().getMensaje(codIdioma, "error.errorGen");
        }
        escribirRespuesta(valoresCalculo, codigoOperacion, mensaje, request, response);
    }
    
    private ValoresCalculo cargarValoresCalculo(int codOrganizacion, String numExpediente, String ejercicio) throws Exception
    {
        AdaptadorSQLBD adapt = this.getAdaptSQLBD(""+codOrganizacion);
        ValoresCalculo valoresCalculo = new ValoresCalculo();
        valoresCalculo.setLimite(ConfigurationParameter.getParameter(ConstantesMeLanbide34.CALCULOS_LIMMAX_CEI, ConstantesMeLanbide34.FICHERO_S75));
        valoresCalculo.setPorcentaje(ConfigurationParameter.getParameter(ConstantesMeLanbide34.CALCULOS_PORSUB_CEI, ConstantesMeLanbide34.FICHERO_S75));

        //BigDecimal impSolic = MeLanbide34Manager.getInstance().getValorCampoNumerico(codOrganizacion, numExpediente, ejercicio, ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_IMPSOLIC,adapt);
        BigDecimal impSolic = MeLanbide34Manager.getInstance().getValorCampoNumerico(codOrganizacion, numExpediente, ejercicio, ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_COSTESINIVA,adapt);
        if(impSolic != null)
        {
            valoresCalculo.setImpSolic(impSolic.toPlainString());
            BigDecimal cuanlim = MeLanbide34Manager.getInstance().getValorCampoNumericoTramite(codOrganizacion, numExpediente, ejercicio, ConstantesMeLanbide34.CODIGO_TRAM_ETECNICO, ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_CUANLIM,adapt);
            if(cuanlim != null)
            {
                valoresCalculo.setLimitePorc(cuanlim.toPlainString());
            }

            BigDecimal p1 = MeLanbide34Manager.getInstance().getValorCampoNumericoTramite(codOrganizacion, numExpediente, ejercicio, ConstantesMeLanbide34.CODIGO_TRAM_ETECNICO, ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_P1,adapt);
            if(p1 != null)
            {
                valoresCalculo.setP1(p1.toPlainString());
            }
            else
            {
                valoresCalculo.setP1(ConfigurationParameter.getParameter(ConstantesMeLanbide34.CALCULOS_PONDE1_CEI, ConstantesMeLanbide34.FICHERO_S75));
            }

            BigDecimal p2 = MeLanbide34Manager.getInstance().getValorCampoNumericoTramite(codOrganizacion, numExpediente, ejercicio, ConstantesMeLanbide34.CODIGO_TRAM_ETECNICO, ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_P2,adapt);
            if(p2 != null)
            {
                valoresCalculo.setP2(p2.toPlainString());
            }
            else
            {
                valoresCalculo.setP2(ConfigurationParameter.getParameter(ConstantesMeLanbide34.CALCULOS_PONDE2_CEI, ConstantesMeLanbide34.FICHERO_S75));
            }

            BigDecimal p3 = MeLanbide34Manager.getInstance().getValorCampoNumericoTramite(codOrganizacion, numExpediente, ejercicio, ConstantesMeLanbide34.CODIGO_TRAM_ETECNICO, ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_P3,adapt);
            if(p3 != null)
            {
                valoresCalculo.setP3(p3.toPlainString());
            }
            else
            {
                valoresCalculo.setP3(ConfigurationParameter.getParameter(ConstantesMeLanbide34.CALCULOS_PONDE3_CEI, ConstantesMeLanbide34.FICHERO_S75));
            }

            BigDecimal p4 = MeLanbide34Manager.getInstance().getValorCampoNumericoTramite(codOrganizacion, numExpediente, ejercicio, ConstantesMeLanbide34.CODIGO_TRAM_ETECNICO, ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_P4,adapt);
            if(p4 != null)
            {
                valoresCalculo.setP4(p4.toPlainString());
            }
            else
            {
                valoresCalculo.setP4(ConfigurationParameter.getParameter(ConstantesMeLanbide34.CALCULOS_PONDE4_CEI, ConstantesMeLanbide34.FICHERO_S75));
            }

            BigDecimal propuesta = MeLanbide34Manager.getInstance().getValorCampoNumericoTramite(codOrganizacion, numExpediente, ejercicio, ConstantesMeLanbide34.CODIGO_TRAM_ETECNICO, ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_PROPUESTA,adapt);
            if(propuesta != null)
            {
                valoresCalculo.setPropuesta(propuesta.toPlainString());
            }

            BigDecimal concedida = MeLanbide34Manager.getInstance().getValorCampoNumerico(codOrganizacion, numExpediente, ejercicio, ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_CONCEDIDA,adapt);
            if(concedida != null)
            {
                valoresCalculo.setConcedida(concedida.toPlainString());
            }

            BigDecimal total = MeLanbide34Manager.getInstance().getValorCampoNumericoTramite(codOrganizacion, numExpediente, ejercicio, ConstantesMeLanbide34.CODIGO_TRAM_ETECNICO, ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_SUMPOND,adapt);
            if(total != null)
            {
                valoresCalculo.setTotal(total.toPlainString());
            }
            Date fecEstudio = MeLanbide34Manager.getInstance().getValorCampoFechaTramite(codOrganizacion, numExpediente, ejercicio, ConstantesMeLanbide34.CODIGO_TRAM_ETECNICO, ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_FECETECNICO,adapt);
            if(fecEstudio != null)
            {
                 SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide34.FORMATO_FECHA);
                 
                valoresCalculo.setFecEstudio(format.format(fecEstudio));
            }
            String resultado = MeLanbide34Manager.getInstance().getValorCampoDesplegable(codOrganizacion, numExpediente, ejercicio, ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_RESULSUBV,adapt);
            if(resultado != null)
            {
                valoresCalculo.setResulSubv(resultado);
            }
             String motden1 = MeLanbide34Manager.getInstance().getValorCampoDesplegable(codOrganizacion, numExpediente, ejercicio, ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_MOTDEN1,adapt);
            if(motden1 != null)
            {
                valoresCalculo.setMotivoDen1(motden1);
            }
             String motden2 = MeLanbide34Manager.getInstance().getValorCampoDesplegable(codOrganizacion, numExpediente, ejercicio, ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_MOTDEN2,adapt);
            if(motden2 != null)
            {
                valoresCalculo.setMotivoDen2(motden2);
            }
             String motden3 = MeLanbide34Manager.getInstance().getValorCampoDesplegable(codOrganizacion, numExpediente, ejercicio, ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_MOTDEN3,adapt);
            if(motden3 != null)
            {
                valoresCalculo.setMotivoDen3(motden3);
            }
           
            
        }
        
        return valoresCalculo;
    }
    
    private ValoresCalculo realizarCalculos(ValoresCalculo v)
    {
        BigDecimal cien = new BigDecimal("100.00");
        BigDecimal sumando = new BigDecimal("0.00");
        BigDecimal impSolic = new BigDecimal(v.getImpSolic());
        BigDecimal limitePorc = new BigDecimal(v.getPorcentaje());
        BigDecimal limite = impSolic.multiply(limitePorc);
        limite = limite.divide(cien);
        limite = limite.add(sumando);
        limite = limite.setScale(2, RoundingMode.HALF_DOWN);
        v.setLimitePorc(limite.toPlainString());
        
        BigDecimal p1 = new BigDecimal(v.getP1());
        BigDecimal p2 = new BigDecimal(v.getP2());
        BigDecimal p3 = new BigDecimal(v.getP3());
        BigDecimal p4 = new BigDecimal(v.getP4());
        
        BigDecimal sumpond = new BigDecimal("0");
        sumpond = sumpond.add(p1);
        sumpond = sumpond.add(p2);
        sumpond = sumpond.add(p3);
        sumpond = sumpond.add(p4);
        
        v.setTotal(sumpond.toPlainString());
        
        BigDecimal propuesta = new BigDecimal(v.getLimitePorc());
        propuesta = propuesta.multiply(sumpond);
        propuesta = propuesta.divide(cien);
        propuesta = propuesta.add(sumando);
        propuesta = propuesta.setScale(2, RoundingMode.HALF_DOWN);
        v.setPropuesta(propuesta.toPlainString());
        
        BigDecimal concedida = null;
        if(v.getConcedida() == null || v.getConcedida().equals(""))
        {
            BigDecimal limMax = new BigDecimal(ConfigurationParameter.getParameter(ConstantesMeLanbide34.CALCULOS_LIMMAX_CEI, ConstantesMeLanbide34.FICHERO_S75));
            concedida = new BigDecimal(v.getPropuesta());
            if(concedida.compareTo(limMax) > 0)
            {
                concedida = new BigDecimal(limMax.toPlainString());
            }
            concedida.add(sumando);
            concedida = concedida.setScale(2, RoundingMode.HALF_DOWN);
        }
        else
        {
            concedida = new BigDecimal(v.getConcedida());
            concedida = concedida.add(sumando);
            concedida = concedida.setScale(2, RoundingMode.HALF_DOWN);
        }
        v.setConcedida(concedida.toPlainString());
        
        BigDecimal pp1 = new BigDecimal(ConfigurationParameter.getParameter(ConstantesMeLanbide34.CALCULOS_PP1_CEI, ConstantesMeLanbide34.FICHERO_S75));
        BigDecimal pago1 = concedida.multiply(pp1);
        pago1 = pago1.divide(cien);
        pago1 = pago1.add(sumando);
        pago1 = pago1.setScale(2, RoundingMode.HALF_DOWN);
        v.setPago1(pago1.toPlainString());
        
        BigDecimal pp2 = new BigDecimal(ConfigurationParameter.getParameter(ConstantesMeLanbide34.CALCULOS_PP2_CEI, ConstantesMeLanbide34.FICHERO_S75));
        BigDecimal pago2 = concedida.multiply(pp2);
        pago2 = pago2.divide(cien);
        pago2 = pago2.add(sumando);
        pago2 = pago2.setScale(2, RoundingMode.HALF_DOWN);
        v.setPago2(pago2.toPlainString());
        
        BigDecimal pp3 = new BigDecimal(ConfigurationParameter.getParameter(ConstantesMeLanbide34.CALCULOS_PP3_CEI, ConstantesMeLanbide34.FICHERO_S75));
        BigDecimal pago3 = concedida.multiply(pp3);
        pago3 = pago3.divide(cien);
        pago3 = pago3.add(sumando);
        pago3 = pago3.setScale(2, RoundingMode.HALF_DOWN);
        v.setPago3(pago3.toPlainString());
        
        
        return v;
    }
    
    private void escribirRespuesta(ValoresCalculo v, String codigoOperacion, String mensaje, HttpServletRequest request, HttpServletResponse response)
    {
    DecimalFormatSymbols simbolo=new DecimalFormatSymbols();
    simbolo.setDecimalSeparator(',');
    simbolo.setGroupingSeparator('.');
    DecimalFormat formateador = new DecimalFormat("###,##0.00",simbolo);
        
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
            xmlSalida.append("<CODIGO_OPERACION>");
                xmlSalida.append(codigoOperacion);
            xmlSalida.append("</CODIGO_OPERACION>");
            xmlSalida.append("<VALORES>");
                xmlSalida.append("<LIMITEPORC>");
                    xmlSalida.append(v != null && v.getLimitePorc() != null ? v.getLimitePorc() : "");
                xmlSalida.append("</LIMITEPORC>");
                xmlSalida.append("<PROPUESTA>");
                    xmlSalida.append(v != null && v.getPropuesta() != null ? /*formateador.format(*/v.getPropuesta()/*)*/ : "");
                xmlSalida.append("</PROPUESTA>");
                xmlSalida.append("<CONCEDIDA>");
                    xmlSalida.append(v != null && v.getConcedida() != null ? /*formateador.format(*/v.getConcedida()/*)*/ : "");
                xmlSalida.append("</CONCEDIDA>");
                xmlSalida.append("<TOTAL>");
                    xmlSalida.append(v != null && v.getTotal() != null ? v.getTotal() : "");
                xmlSalida.append("</TOTAL>");
                xmlSalida.append("<PAGO1>");
                    xmlSalida.append(v != null && v.getPago1() != null ? /*formateador.format(*/v.getPago1()/*)*/ : "");
                xmlSalida.append("</PAGO1>");
                xmlSalida.append("<PAGO2>");
                    xmlSalida.append(v != null && v.getPago2() != null ? /*formateador.format(*/v.getPago2()/*)*/ : "");
                xmlSalida.append("</PAGO2>");
                xmlSalida.append("<PAGO3>");
                    xmlSalida.append(v != null && v.getPago3() != null ? /*formateador.format(*/v.getPago3()/*)*/ : "");
                xmlSalida.append("</PAGO3>");
                xmlSalida.append("<FETECNICO>");
                    xmlSalida.append(v != null && v.getFecEstudio() != null ? v.getFecEstudio() : "");
                xmlSalida.append("</FETECNICO>");
                xmlSalida.append("<RESULTADO>");
                    xmlSalida.append(v != null && v.getResulSubv() != null ? v.getResulSubv() : "");
                xmlSalida.append("</RESULTADO>");
                xmlSalida.append("<MOTDEN1>");
                    xmlSalida.append(v != null && v.getMotivoDen1() != null ? v.getMotivoDen1() : "");
                xmlSalida.append("</MOTDEN1>");
                 xmlSalida.append("<MOTDEN2>");
                    xmlSalida.append(v != null && v.getMotivoDen2() != null ? v.getMotivoDen2() : "");
                xmlSalida.append("</MOTDEN2>");
                 xmlSalida.append("<MOTDEN3>");
                    xmlSalida.append(v != null && v.getMotivoDen3() != null ? v.getMotivoDen3() : "");
                xmlSalida.append("</MOTDEN3>");
            xmlSalida.append("</VALORES>");
            xmlSalida.append("<MENSAJE>");
                xmlSalida.append(mensaje);
            xmlSalida.append("</MENSAJE>");
        xmlSalida.append("</RESPUESTA>");
        try{
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        }catch(Exception e){
            e.printStackTrace();
        }//try-catch
    }
    
    private boolean validarDatos(ValoresCalculo v, int codIdioma)
    {
        boolean correcto = true;
        try
        {
            this.msgValidacion = "";
            BigDecimal p1 = new BigDecimal(v.getP1());
            BigDecimal p2 = new BigDecimal(v.getP2());
            BigDecimal p3 = new BigDecimal(v.getP3());
            BigDecimal p4 = new BigDecimal(v.getP4());
            
            BigDecimal maxP1 = new BigDecimal(ConfigurationParameter.getParameter(ConstantesMeLanbide34.CALCULOS_PONDE1_CEI, ConstantesMeLanbide34.FICHERO_S75));
            BigDecimal maxP2 = new BigDecimal(ConfigurationParameter.getParameter(ConstantesMeLanbide34.CALCULOS_PONDE2_CEI, ConstantesMeLanbide34.FICHERO_S75));
            BigDecimal maxP3 = new BigDecimal(ConfigurationParameter.getParameter(ConstantesMeLanbide34.CALCULOS_PONDE3_CEI, ConstantesMeLanbide34.FICHERO_S75));
            BigDecimal maxP4 = new BigDecimal(ConfigurationParameter.getParameter(ConstantesMeLanbide34.CALCULOS_PONDE4_CEI, ConstantesMeLanbide34.FICHERO_S75));
            
            String nombreCampo = "";
            if(p1.compareTo(maxP1) > 0)
            {
                //ERROR
                correcto = false;
                this.msgValidacion = MeLanbide34I18n.getInstance().getMensaje(codIdioma, "msg.error.pondIncorrecto");
                nombreCampo = MeLanbide34I18n.getInstance().getMensaje(codIdioma, "label.p1");
                msgValidacion = String.format(msgValidacion, nombreCampo, maxP1.toPlainString());
            }
            if(p2.compareTo(maxP2) > 0)
            {
                //ERROR
                correcto = false;
                this.msgValidacion = MeLanbide34I18n.getInstance().getMensaje(codIdioma, "msg.error.pondIncorrecto");
                nombreCampo = MeLanbide34I18n.getInstance().getMensaje(codIdioma, "label.p2");
                msgValidacion = String.format(msgValidacion, nombreCampo, maxP2.toPlainString());
            }
            if(p3.compareTo(maxP3) > 0)
            {
                //ERROR
                correcto = false;
                this.msgValidacion = MeLanbide34I18n.getInstance().getMensaje(codIdioma, "msg.error.pondIncorrecto");
                nombreCampo = MeLanbide34I18n.getInstance().getMensaje(codIdioma, "label.p3");
                msgValidacion = String.format(msgValidacion, nombreCampo, maxP3.toPlainString());
            }
            if(p4.compareTo(maxP4) > 0)
            {
                //ERROR
                correcto = false;
                this.msgValidacion = MeLanbide34I18n.getInstance().getMensaje(codIdioma, "msg.error.pondIncorrecto");
                nombreCampo = MeLanbide34I18n.getInstance().getMensaje(codIdioma, "label.p4");
                msgValidacion = String.format(msgValidacion, nombreCampo, maxP4.toPlainString());
            }
            
        }
        catch(Exception ex)
        {
            correcto = false;
        }
        return correcto;
    }
    
    
    
    /**************************************
     * ****** PESTAÑA PRIMER PAGO
     * *****************************************/
     public String cargarPantallaPrimerPago(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response){
         return cargarPantallaPago(codOrganizacion,  codTramite, ocurrenciaTramite, numExpediente, request,response, 1);
     }
     
     public String cargarPantallaSegundoPago(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response){
         return cargarPantallaPago(codOrganizacion,  codTramite, ocurrenciaTramite, numExpediente, request,response, 2);
     }
     
     public String cargarPantallaTercerPago(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response){
         return cargarPantallaPago(codOrganizacion,  codTramite, ocurrenciaTramite, numExpediente, request,response, 3);
     }
     
    public String cargarPantallaPago(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response, int pago)
    {
        ValoresPago valoresPago = new ValoresPago();
        String ejercicio = "";
        String mensaje = "";
        int codIdioma = 1;
        try
        {
            if(request.getParameter("idioma") != null)
            {
                codIdioma = Integer.parseInt((String)request.getParameter("idioma"));
            }
        }
        catch(Exception ex)
        {

        }
        try
        {
            String[] datos = numExpediente.split(ConstantesMeLanbide34.BARRA_SEPARADORA);
            ejercicio = datos[0];
            ValoresPago v = cargarValoresPago(codOrganizacion, numExpediente, ejercicio, pago);
            if(v != null && v.getImporteConcedidoPago() != null)
            {
                if(validarDatosPago(v, codIdioma))
                {
                    v = this.realizarCalculosPago(v);
                    valoresPago = v;
                }
                else
                {
                    mensaje = msgValidacion;
                }
            }
            else
            {   
                mensaje = MeLanbide34I18n.getInstance().getMensaje(codIdioma, "msg.error.noDatos");
            }
            List<SelectItem> listaViable =  MeLanbide34Manager.getInstance().getListaDesplegable(this.getAdaptSQLBD(String.valueOf(codOrganizacion)), ConstantesMeLanbide34.TIPO_DESPL_BOOL);
            request.setAttribute("lstViable", listaViable);    
                        
        }
        catch(Exception ex)
        {
            mensaje = MeLanbide34I18n.getInstance().getMensaje(codIdioma, "msg.error.noDatos");
        }
        request.setAttribute("valoresPago", valoresPago);
        request.setAttribute("mensaje", mensaje);
        
        boolean deshabilitado = true;
        try
        {
            Long tramAbierto = MeLanbide34Manager.getInstance().getCodigoUltimoTramiteAbierto(codOrganizacion, ejercicio, numExpediente, 1L, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            if(tramAbierto != null)
            {
                String tramite="";
                 switch (pago){
                    case 1: 
                            tramite=ConstantesMeLanbide34.CODIGO_TRAM_PRIMERPAGO;                   
                            break;
                    case 2:                    
                            tramite=ConstantesMeLanbide34.CODIGO_TRAM_SEGUNDOPAGO;
                            break;
                    case 3: 
                            tramite=ConstantesMeLanbide34.CODIGO_TRAM_TERCERPAGO;
                            break;
                 }   
                if(tramAbierto.toString().equalsIgnoreCase(tramite))
                {
                    deshabilitado = false;
                }
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
           if(log.isDebugEnabled()) log.debug("ERROR  tramite ");
        }
        request.setAttribute("deshabilitado", deshabilitado);
        return "/jsp/extension/melanbide34/melanbide34_Pago"+pago+".jsp";
    }
    
    private ValoresPago cargarValoresPago(int codOrganizacion, String numExpediente, String ejercicio, int pago) throws Exception
    {
        AdaptadorSQLBD adapt = this.getAdaptSQLBD(""+codOrganizacion);
        ValoresPago valoresPago = new ValoresPago();
        
        String campoConcedido="";
        String campoAPagar="";
        String campoDescuento="";
        String tramite="";
        String campoFechaPago="";
        
        switch (pago){
            case 1: 
                    campoConcedido = ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_IMPCONA1; 
                    campoAPagar = ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_IMPPAGAR1;
                    campoDescuento = ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_DESCUENTO1;
                    tramite=ConstantesMeLanbide34.CODIGO_TRAM_PRIMERPAGO;
                    campoFechaPago = ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_FECPRIMERPAGO;
                    break;
            case 2: 
                    campoConcedido = ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_IMPCONA2; 
                    campoAPagar = ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_IMPPAGAR2;
                    campoDescuento = ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_DESCUENTO2;
                    tramite=ConstantesMeLanbide34.CODIGO_TRAM_SEGUNDOPAGO;
                    campoFechaPago = ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_FECSEGUNDOPAGO;
                    break;
            case 3: 
                    campoConcedido = ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_IMPCONA3; 
                    campoAPagar = ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_IMPPAGAR3;
                    campoDescuento = ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_DESCUENTO3;
                    tramite=ConstantesMeLanbide34.CODIGO_TRAM_TERCERPAGO;
                    campoFechaPago = ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_FECTERCERPAGO;
                    break;
        }
                
        
        BigDecimal impPago1 = MeLanbide34Manager.getInstance().getValorCampoNumerico(codOrganizacion, numExpediente, ejercicio, campoConcedido,adapt);
        if(impPago1 != null)
        {
            valoresPago.setImporteConcedidoPago(impPago1.toPlainString());
            
            BigDecimal impAPagar = MeLanbide34Manager.getInstance().getValorCampoNumerico(codOrganizacion, numExpediente, ejercicio,  campoAPagar,adapt);
            if(impAPagar != null)
            {
                valoresPago.setImporteAPagar(impAPagar.toPlainString());
            }
            else
            {
                valoresPago.setImporteAPagar(valoresPago.getImporteAPagar()!= null?valoresPago.getImporteAPagar():valoresPago.getImporteConcedidoPago());
            }

            BigDecimal descuento = MeLanbide34Manager.getInstance().getValorCampoNumerico(codOrganizacion, numExpediente, ejercicio,  campoDescuento,adapt);
            if(descuento != null)
            {
                valoresPago.setDescuento(descuento.toPlainString());
            }else
            {
                valoresPago.setDescuento(valoresPago.getDescuento());
            }

            Date fecPago = MeLanbide34Manager.getInstance().getValorCampoFechaTramite(codOrganizacion, numExpediente, ejercicio, tramite, campoFechaPago,adapt);
            if(fecPago != null)
            {
                 SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide34.FORMATO_FECHA);
                 
                valoresPago.setFecPago(format.format(fecPago));
            }else
            {
                valoresPago.setFecPago(valoresPago.getFecPago());
            }
            if (pago==2){
                String viable = MeLanbide34Manager.getInstance().getValorCampoDesplegable(codOrganizacion, numExpediente, ejercicio, ConstantesMeLanbide34.CAMPO_SUPLEMENTARIO_VIABLE,adapt);
                if(viable != null)
                {
                    valoresPago.setViable(viable);
                }
            }
        }
        
        return valoresPago;
    }
    
    
    private boolean validarDatosPago(ValoresPago v, int codIdioma)
    {
        boolean correcto = true;
        try
        {
            this.msgValidacion = "";
            BigDecimal impConcedido = new BigDecimal(v.getImporteConcedidoPago()); 
            BigDecimal impAPagar = new BigDecimal(v.getImporteAPagar()!=null?v.getImporteAPagar():v.getImporteConcedidoPago());   
                               
                        
            String nombreCampo = "";
            if(impAPagar.compareTo(impConcedido) > 0)
            {
                //ERROR
                correcto = false;
                this.msgValidacion = MeLanbide34I18n.getInstance().getMensaje(codIdioma, "msg.error.pondIncorrecto");
                nombreCampo = MeLanbide34I18n.getInstance().getMensaje(codIdioma, "label.impPagar1");
                msgValidacion = String.format(msgValidacion, nombreCampo, impConcedido.toPlainString());
            }          
            
        }
        catch(Exception ex)
        {
            correcto = false;
        }
        return correcto;
    }
    
    
     private ValoresPago realizarCalculosPago(ValoresPago v)
    {
        BigDecimal impConcedido = new BigDecimal(v.getImporteConcedidoPago());
        BigDecimal impApagar = new BigDecimal(v.getImporteAPagar()!=null?v.getImporteAPagar():v.getImporteConcedidoPago());
        
        BigDecimal descuento = new BigDecimal("0");
        descuento = impConcedido.subtract(impApagar);  
        descuento = descuento.setScale(2, RoundingMode.HALF_DOWN);
        v.setDescuento(descuento.toPlainString());
        
        return v;
    }
    
     
    /*  public void refrescarPantallaPagos(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        String mensaje = "";
        String codigoOperacion = "0";
        ValoresPago valoresPago = new ValoresPago();
        int codIdioma = 1;
        try
        {
            if(request.getParameter("idioma") != null)
            {
                codIdioma = Integer.parseInt((String)request.getParameter("idioma"));
            }
        }
        catch(Exception ex)
        {

        }
        try
        {
            String[] datos = numExpediente.split(ConstantesMeLanbide34.BARRA_SEPARADORA);
            String ejercicio = datos[0];
            
            ValoresPago v = cargarValoresPago(codOrganizacion, numExpediente, ejercicio);
            
            if(v != null && v.getImporteConcedidoPago() != null)
            {
                if(validarDatosPago(v, codIdioma))
                {
                    v = this.realizarCalculosPago(v);
                    valoresPago = v;
                }
                else
                {
                    mensaje = msgValidacion;
                }
            }
            else
            {   
                mensaje = MeLanbide34I18n.getInstance().getMensaje(codIdioma, "msg.error.noDatos");
            }
        }
        catch(Exception ex)
        {
            
        }
        
        boolean deshabilitado = true;
        try
        {
            String[] datos = numExpediente.split(ConstantesMeLanbide34.BARRA_SEPARADORA);
            String ejercicio = datos[0];
            Long tramAbierto = MeLanbide34Manager.getInstance().getCodigoUltimoTramiteAbierto(codOrganizacion, ejercicio, numExpediente, 1L, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            if(tramAbierto != null)
            {
                if(tramAbierto.toString().equalsIgnoreCase(ConstantesMeLanbide34.CODIGO_TRAM_ETECNICO))
                {
                    deshabilitado = false;
                }
            }
        }
        catch(Exception ex)
        {
            
        }
        request.setAttribute("deshabilitado", deshabilitado);
        escribirRespuestaPago(valoresPago, codigoOperacion, mensaje, request, response);
    }
      */
      
    private void escribirRespuestaPago(ValoresPago v, String codigoOperacion, String mensaje, HttpServletRequest request, HttpServletResponse response)
    {
    DecimalFormatSymbols simbolo=new DecimalFormatSymbols();
    simbolo.setDecimalSeparator(',');
    simbolo.setGroupingSeparator('.');
    DecimalFormat formateador = new DecimalFormat("###,##0.00",simbolo);
    //System.out.println(formateador.format(v.getImporteAPagar());
        
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
            xmlSalida.append("<CODIGO_OPERACION>");
                xmlSalida.append(codigoOperacion);
            xmlSalida.append("</CODIGO_OPERACION>");
            xmlSalida.append("<VALORES>");
                xmlSalida.append("<IMPAPAGAR>");
                    xmlSalida.append(v != null && v.getImporteAPagar() != null ? /*formateador.format(*/v.getImporteAPagar()/*)*/ : "");
                xmlSalida.append("</IMPAPAGAR>");
                xmlSalida.append("<CONCEDIDOPAGO>");
                    xmlSalida.append(v != null && v.getImporteConcedidoPago() != null ? /*formateador.format(*/v.getImporteConcedidoPago()/*)*/ : "");
                xmlSalida.append("</CONCEDIDOPAGO>");
                xmlSalida.append("<DESCUENTO>");
                    xmlSalida.append(v != null && v.getDescuento() != null ? /*formateador.format(*/v.getDescuento()/*)*/ : "");
                xmlSalida.append("</DESCUENTO>");               
                xmlSalida.append("<FPAGO>");
                    xmlSalida.append(v != null && v.getFecPago() != null ? v.getFecPago() : "");
                xmlSalida.append("</FPAGO>");                
            xmlSalida.append("</VALORES>");
            xmlSalida.append("<MENSAJE>");
                xmlSalida.append(mensaje);
            xmlSalida.append("</MENSAJE>");
        xmlSalida.append("</RESPUESTA>");
        try{
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        }catch(Exception e){
            e.printStackTrace();
        }//try-catch
    }  
      
    public void guardarPago(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    { 
        ValoresPago valoresPago = new ValoresPago();
        String ejercicio = "";
        String mensaje = "";
        String codigoOperacion = "0";
        int codIdioma = 1;
        try
        {
            if(request.getParameter("idioma") != null)
            {
                codIdioma = Integer.parseInt((String)request.getParameter("idioma"));
            }
        }
        catch(Exception ex)
        {

        }
        
        String impteAPagar = (String)request.getParameter("impAPagar");
        String fecPago = (String)request.getParameter("fecPago");        
        String descuento = (String)request.getParameter("descuento");        
        int pago = Integer.parseInt((String)request.getParameter("pago"));
        String viable = (String)request.getParameter("viable");
        
        try
        {
            String[] datos = numExpediente.split(ConstantesMeLanbide34.BARRA_SEPARADORA);
            ejercicio = datos[0];
            ValoresPago v = cargarValoresPago(codOrganizacion, numExpediente, ejercicio, pago);
            if(v != null && v.getImporteAPagar() != null)
            {
                v.setImporteAPagar(impteAPagar);
                v.setDescuento(descuento);
                v.setFecPago(fecPago);
                v.setViable(viable);
                                
                if(validarDatosPago(v, codIdioma))
                {
                    v = this.realizarCalculosPago(v);
                    valoresPago = v;
                    boolean correcto = MeLanbide34Manager.getInstance().guardarValoresPago(codOrganizacion, valoresPago, numExpediente, ejercicio, pago, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    if(correcto)
                    {
                        mensaje = MeLanbide34I18n.getInstance().getMensaje(codIdioma, "msg.datosGuardadosOK");
                    }
                    else
                    {
                        mensaje = MeLanbide34I18n.getInstance().getMensaje(codIdioma, "msg.error.datosNoGuardados");
                        codigoOperacion = "3";
                    }
                }
                else
                {
                    codigoOperacion = "2";
                    mensaje = msgValidacion;
                }
            }
            else
            {   
                codigoOperacion = "1";
                mensaje = MeLanbide34I18n.getInstance().getMensaje(codIdioma, "msg.error.noDatos");
            }
        }
        catch(Exception ex)
        {
            codigoOperacion = "3";
            mensaje = MeLanbide34I18n.getInstance().getMensaje(codIdioma, "error.errorGen");
        }
        escribirRespuestaPago(valoresPago, codigoOperacion, mensaje, request, response);
    }
    
    
    
    /**
     * Operación que recupera los datos de conexión a la BBDD
     * @param codOrganizacion
     * @return AdaptadorSQLBD
     */
    private AdaptadorSQLBD getAdaptSQLBD(String codOrganizacion){
        if(log.isDebugEnabled()) log.debug("getConnection ( codOrganizacion = " + codOrganizacion + " ) : BEGIN");
        ResourceBundle config = ResourceBundle.getBundle("techserver");
        String gestor = config.getString("CON.gestor");
        String jndiGenerico = config.getString("CON.jndi");
        Connection conGenerico = null;
        Statement st = null;
        ResultSet rs = null;
        String[] salida = null;
        Connection con = null;
        
        if(log.isDebugEnabled()){
            log.debug("getJndi =========> ");
            log.debug("parametro codOrganizacion: " + codOrganizacion);
            log.debug("gestor: " + gestor);
            log.debug("jndi: " + jndiGenerico);
        }//if(log.isDebugEnabled())

        DataSource ds = null;
        AdaptadorSQLBD adapt = null;
        synchronized (this) {
            try{
                PortableContext pc = PortableContext.getInstance();
                if(log.isDebugEnabled())log.debug("He cogido el jndi: " + jndiGenerico);
                ds = (DataSource)pc.lookup(jndiGenerico, DataSource.class);
                // Conexión al esquema genérico
                conGenerico = ds.getConnection();

                String sql = "SELECT EEA_BDE FROM A_EEA WHERE EEA_APL=" + ConstantesDatos.APP_GESTION_EXPEDIENTES + " AND AAE_ORG=" + codOrganizacion;
                st = conGenerico.createStatement();
                rs = st.executeQuery(sql);
                String jndi = null;
                while(rs.next()){
                    jndi = rs.getString("EEA_BDE");
                }//while(rs.next())

                st.close();
                rs.close();
                conGenerico.close();

                if(jndi!=null && gestor!=null && !"".equals(jndi) && !"".equals(gestor)){
                    salida = new String[7];
                    salida[0] = gestor;
                    salida[1] = "";
                    salida[2] = "";
                    salida[3] = "";
                    salida[4] = "";
                    salida[5] = "";
                    salida[6] = jndi;
                    adapt = new AdaptadorSQLBD(salida);
                }//if(jndi!=null && gestor!=null && !"".equals(jndi) && !"".equals(gestor))
            }catch(TechnicalException te){
                te.printStackTrace();
                log.error("*** AdaptadorSQLBD: " + te.toString());
            }catch(SQLException e){
                e.printStackTrace();
            }finally{
                try{
                    if(st!=null) st.close();
                    if(rs!=null) rs.close();
                    if(conGenerico!=null && !conGenerico.isClosed())
                    conGenerico.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }//try-catch
            }// finally
            if(log.isDebugEnabled()) log.debug("getConnection() : END");
         }// synchronized
        return adapt;
     }//getConnection
    
    /**
     * M?todo que retorna el valor de un desplegable en el idiona del usuario
     *
     * @param request
     * @param descripcionCompleta
     * @return String con el valor
     */
    private String getDescripcionDesplegable(HttpServletRequest request, String descripcionCompleta) {
        String descripcion = descripcionCompleta;
        
        String barraSeparadoraDobleIdiomaDesple = ConfigurationParameter.getParameter(ConstantesMeLanbide34.BARRA_SEPARADORA_IDIOMA_DESPLEGABLES, ConstantesMeLanbide34.FICHERO_PROPIEDADES);
        
        try {
            if (!descripcion.isEmpty()) {

                String[] descripcionDobleIdioma = (descripcion != null ? descripcion.split(barraSeparadoraDobleIdiomaDesple) : null);
                if (descripcionDobleIdioma != null && descripcionDobleIdioma.length > 1) {
                    if (getIdioma(request) == ConstantesMeLanbide34.CODIGO_IDIOMA_EUSKERA) {
                        descripcion = descripcionDobleIdioma[1];
                    } else {
                        // Cogemos la primera posicion que deberia ser castellano
                        descripcion = descripcionDobleIdioma[0];
                    }
                }

            } else {
                descripcion = "-";
            }
            return descripcion;
        } catch (Exception e) {
            return descripcion;
        }
        
    }
    /**
     * M?todo que recupera el Idioma de la request para la gestion de Desplegables
     *
     * @param request
     * @return int idioma 1-castellano 4-euskera
     */
    private int getIdioma(HttpServletRequest request) {
        // Recuperamos el Idioma de la request para la gestion de Desplegables
        UsuarioValueObject usuario = new UsuarioValueObject();
        int idioma = ConstantesMeLanbide34.CODIGO_IDIOMA_CASTELLANO;  // Por Defecto 1 Castellano
        try {

            if (request != null && request.getSession() != null) {
                usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
                if (usuario != null) {
                    idioma = usuario.getIdioma();
                }
            }
        } catch (Exception ex) {
            log.error("Error al recuperar el idioma del usuario de la request, asignamos por defecto 1 Castellano", ex);
            idioma = ConstantesMeLanbide34.CODIGO_IDIOMA_CASTELLANO;
        }

        return idioma;
    }
     public String cargarNuevoMinimis(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarNuevoMinimis - " + numExpediente);
        String nuevo = "1";
        String numExp = "";
        String urlnuevoMinimis = "/jsp/extension/melanbide34/nuevoSubSolic.jsp?codOrganizacion=" + codOrganizacion;
        try {
            if (request.getAttribute("nuevo") != null) {
                if (request.getAttribute("nuevo").toString().equals("0")) {
                    request.setAttribute("nuevo", nuevo);
                }
            } else {
                request.setAttribute("nuevo", nuevo);
            }
            if (request.getParameter("numExp") != null) {
                numExp = request.getParameter("numExp");
                request.setAttribute("numExp", numExp);
            }

            cargarDesplegablesSubvencion(codOrganizacion, request);

        } catch (Exception ex) {
            log.debug("Se ha presentado un error al intentar preparar la jsp de una nueva minimis : " + ex.getMessage());
        }
        return urlnuevoMinimis;
    }

    public String cargarModificarMinimis(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarModificarMinimis - " + numExpediente);
        String nuevo = "0";
        String urlnuevoMinimis = "/jsp/extension/melanbide34/nuevoSubSolic.jsp?codOrganizacion=" + codOrganizacion;
        try {
            if (request.getAttribute("nuevo") != null) {
                if (!request.getAttribute("nuevo").toString().equals("0")) {
                    request.setAttribute("nuevo", nuevo);
                }
            } else {
                request.setAttribute("nuevo", nuevo);
            }
            if (request.getParameter("numExp") != null) {
                request.setAttribute("numExp", request.getParameter("numExp"));
            }
            String id = request.getParameter("id");
            if (id != null && !id.equals("")) {
                FilaMinimisVO datModif = MeLanbide34Manager.getInstance().getMinimisPorID(id, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (datModif != null) {
                    request.setAttribute("datModif", datModif);
                }
            }

            cargarDesplegablesSubvencion(codOrganizacion, request);

        } catch (Exception ex) {
            log.debug("Error al tratar de preparar los datos para modificar y llamar la jsp de modificaci?n : " + ex.getMessage());
        }
        return urlnuevoMinimis;

    }

    public void crearNuevoMinimis(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en crearNuevoMinimis - " + numExpediente);
        String codigoOperacion = "-1";
        List<FilaMinimisVO> lista = new ArrayList<FilaMinimisVO>();
        FilaMinimisVO nuevaMinimis = new FilaMinimisVO();

        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));

            String numExp = request.getParameter("numExp");

            String estado = (String) request.getParameter("estado");
            String organismo = (String) request.getParameter("organismo");
            String objeto = (String) request.getParameter("objeto");
            String importe = (String) request.getParameter("importe").replace(",", ".");
            String fecha = (String) request.getParameter("fecha");

            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");

            nuevaMinimis.setNumExp(numExp);

            nuevaMinimis.setEstado(estado);
            nuevaMinimis.setOrganismo(organismo);
            nuevaMinimis.setObjeto(objeto);
            if (importe != null && !"".equals(importe)) {
                nuevaMinimis.setImporte(Double.valueOf(importe));
            }
            if (fecha != null && !"".equals(fecha)) {
                nuevaMinimis.setFecha(new java.sql.Date(formatoFecha.parse(fecha).getTime()));
            }

            MeLanbide34Manager meLanbide34Manager = MeLanbide34Manager.getInstance();
            boolean insertOK = meLanbide34Manager.crearNuevoMinimis(nuevaMinimis, adapt);
            if (insertOK) {
                log.debug("minimis insertada correctamente");
                codigoOperacion = "0";
                lista = meLanbide34Manager.getDatosMinimis(numExp, codOrganizacion, adapt);
                if (lista.size() > 0) {
                    for (FilaMinimisVO lm : lista) {
                        lm.setDesEstado(getDescripcionDesplegable(request, lm.getDesEstado()));
                    }
                }
            } else {
                log.debug("NO se ha insertado correctamente la nueva minimis");
                codigoOperacion = "1";
            }
        } catch (Exception ex) {
            log.debug("Error al parsear los par?metros recibidos del jsp al objeto " + ex.getMessage());
            codigoOperacion = "3";
        }

        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);
        if (codigoOperacion.equalsIgnoreCase("0")) {
            resultado.setAtributo("lista", lista);
        }
        retornarJSON(new Gson().toJson(resultado), response);
    }

    public void modificarMinimis(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en modificarMinimis - " + numExpediente);
        String codigoOperacion = "-1";
        List<FilaMinimisVO> lista = new ArrayList<FilaMinimisVO>();

        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            //Recojo los parametros
            String id = (String) request.getParameter("id");

            String numExp = (String) request.getParameter("numExp");

            String estado = (String) request.getParameter("estado");
            String organismo = (String) request.getParameter("organismo");
            String objeto = (String) request.getParameter("objeto");
            String importe = (String) request.getParameter("importe").replace(",", ".");
            String fecha = (String) request.getParameter("fecha");

            if (id == null || id.equals("")) {
                log.debug("No se ha recibido desde la JSP el id de la minimis a Modificar ");
                codigoOperacion = "3";
            } else {
                FilaMinimisVO datModif = new FilaMinimisVO();

                datModif.setId(Integer.valueOf(id));

                SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");

                datModif.setNumExp(numExp);

                datModif.setEstado(estado);
                datModif.setOrganismo(organismo);
                datModif.setObjeto(objeto);
                if (importe != null && !"".equals(importe)) {
                    datModif.setImporte(Double.valueOf(importe));
                }
                if (fecha != null && !"".equals(fecha)) {
                    datModif.setFecha(new java.sql.Date(formatoFecha.parse(fecha).getTime()));
                }

                MeLanbide34Manager meLanbide34Manager = MeLanbide34Manager.getInstance();
                boolean modOK = meLanbide34Manager.modificarMinimis(datModif, adapt);
                if (modOK) {
                    codigoOperacion = "0";
                    try {
                        lista = meLanbide34Manager.getDatosMinimis(numExp, codOrganizacion, adapt);
                        if (lista.size() > 0) {
                            for (FilaMinimisVO lm : lista) {
                                lm.setDesEstado(getDescripcionDesplegable(request, lm.getDesEstado()));
                            }
                        }
                    } catch (BDException bde) {
                        codigoOperacion = "1";
                        log.debug("Error de tipo BD al recuperar la lista de minimis despu?s de Modificar una minimis : " + bde.getMensaje());
                    } catch (Exception ex) {
                        codigoOperacion = "2";
                        log.debug("Error al recuperar la lista de minimis despu?s de Modificar una minimis : " + ex.getMessage());
                    }
                } else {
                    codigoOperacion = "3";
                }
            }

        } catch (Exception ex) {
            log.debug("Error modificar --- ", ex);
            codigoOperacion = "3";
        }
        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);
        if (codigoOperacion.equalsIgnoreCase("0")) {
            resultado.setAtributo("lista", lista);
        }
        retornarJSON(new Gson().toJson(resultado), response);

    }

    public void eliminarMinimis(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en eliminarMinimis - " + numExpediente);
        String codigoOperacion = "-1";
        List<FilaMinimisVO> lista = new ArrayList<FilaMinimisVO>();
        String numExp = "";
        try {
            String id = request.getParameter("id");
            if (id == null || id.equals("")) {
                log.debug("No se ha recibido desde la JSP el id de la minimis a Elimnar ");
                codigoOperacion = "3";
            } else {
                numExp = request.getParameter("numExp");
                AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                int result = MeLanbide34Manager.getInstance().eliminarMinimis(id, adapt);
                if (result <= 0) {
                    codigoOperacion = "6";
                } else {
                    codigoOperacion = "0";
                    try {
                        lista = MeLanbide34Manager.getInstance().getDatosMinimis(numExp, codOrganizacion, adapt);
                        if (lista.size() > 0) {
                            for (FilaMinimisVO lm : lista) {
                                lm.setDesEstado(getDescripcionDesplegable(request, lm.getDesEstado()));
                            }
                        }
                    } catch (Exception ex) {
                        codigoOperacion = "5";
                        log.debug("Error al recuperar la lista de minimis despu?s de eliminar una minimis");
                    }
                }
            }
        } catch (Exception ex) {
            log.debug("Error eliminando una minimis: " + ex);
            codigoOperacion = "2";
        }
        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);
        if (codigoOperacion.equalsIgnoreCase("0")) {
            resultado.setAtributo("lista", lista);
        }
        retornarJSON(new Gson().toJson(resultado), response);
    }

    /**
     * M?todo que recupera los valores de los desplegables del m?dulo
     *
     * @param codOrganizacion
     * @param request
     * @throws SQLException
     * @throws Exception
     */
    private void cargarDesplegablesSubvencion(int codOrganizacion, HttpServletRequest request) throws SQLException, Exception {
        List<DesplegableAdmonLocalVO> listaEstado = MeLanbide34Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide34.COD_DES_DTSV, ConstantesMeLanbide34.FICHERO_PROPIEDADES), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        if (!listaEstado.isEmpty()) {
            listaEstado = traducirDesplegable(request, listaEstado);
            request.setAttribute("listaEstado", listaEstado);
        }
    }

    /**
     * M?todo que extrae la descripci?n de los desplegables en el idioma del
     * usuario, en BBDD est?n en un campo separadas por Pipeline |
     *
     * @param request
     * @param desplegable
     * @return la lista en el idioma de usu
     */
    private List<DesplegableAdmonLocalVO> traducirDesplegable(HttpServletRequest request, List<DesplegableAdmonLocalVO> desplegable) {
        for (DesplegableAdmonLocalVO d : desplegable) {
            if (d.getDes_nom() != null && !d.getDes_nom().isEmpty()) {
                d.setDes_nom(getDescripcionDesplegable(request, d.getDes_nom()));
            }
        }
        return desplegable;
    }

    
    /**
     * M?todo llamado para devolver un String en formato JSON al cliente que ha
     * realiza la petici?n a alguna de las operaciones de este action
     *
     * @param json: String que contiene el JSON a devolver
     * @param response: Objeto de tipo HttpServletResponse a trav?s del cual se
     * devuelve la salida al cliente que ha realizado la solicitud
     */
    private void retornarJSON(String json, HttpServletResponse response) {
        try {
            if (json != null) {
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                out.print(json);
                out.flush();
                out.close();
            }
        } catch (IOException e) {
        }
    }
   
    
}