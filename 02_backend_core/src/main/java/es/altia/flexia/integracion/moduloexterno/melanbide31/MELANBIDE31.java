/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide31;


import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.melanbide31.i18n.MeLanbide31I18n;
import es.altia.flexia.integracion.moduloexterno.melanbide31.manager.MeLanbide31Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide31.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide31.util.ConstantesMeLanbide31;
import es.altia.flexia.integracion.moduloexterno.melanbide31.util.MeLanbide31Utils;
import es.altia.flexia.integracion.moduloexterno.melanbide31.vo.FilaValidacionDatoSuplementarioVO;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;
import es.altia.technical.PortableContext;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author santiagoc
 */
public class MELANBIDE31 extends ModuloIntegracionExterno 
{
    //Logger
    private static Logger log = LogManager.getLogger(MELANBIDE31.class);
    
    public String validarDatos(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
        log.debug("Entra en MELANBIDE31.validarDatos");
        log.debug("Idioma = "+request.getParameter("idioma"));
        
        List<FilaValidacionDatoSuplementarioVO> listaValidaciones = this.procesarValidaciones(codOrganizacion, numExpediente, request);
        request.setAttribute("listaValidaciones", listaValidaciones);
        
        return "/jsp/extension/melanbide31/melanbide31.jsp";
    }
    
    public void actualizarValidaciones(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        List<FilaValidacionDatoSuplementarioVO> listaValidaciones = this.procesarValidaciones(codOrganizacion, numExpediente, request);
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
            xmlSalida.append("<CODIGO_OPERACION>");
                xmlSalida.append("0");
            xmlSalida.append("</CODIGO_OPERACION>");
            for(FilaValidacionDatoSuplementarioVO fila : listaValidaciones)
            {
                xmlSalida.append("<FILA>");
                    xmlSalida.append("<CAMPO>");
                        xmlSalida.append(fila.getNombreCampo());
                    xmlSalida.append("</CAMPO>");
                    xmlSalida.append("<VALOR>");
                        xmlSalida.append(fila.getValor());
                    xmlSalida.append("</VALOR>");
                    xmlSalida.append("<RESULTADO>");
                        xmlSalida.append(fila.getResultado());
                    xmlSalida.append("</RESULTADO>");
                    xmlSalida.append("<OBSERVACIONES>");
                        xmlSalida.append(fila.getObservaciones());
                    xmlSalida.append("</OBSERVACIONES>");
                xmlSalida.append("</FILA>");
            }
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
    
    private List<FilaValidacionDatoSuplementarioVO> procesarValidaciones(int codOrganizacion, String numExpediente, HttpServletRequest request)
    {
        String ejercicio = null;
        List<FilaValidacionDatoSuplementarioVO> listaValidaciones = new ArrayList<FilaValidacionDatoSuplementarioVO>();
        try
        {
            String[] datos = numExpediente.split(ConstantesMeLanbide31.BARRA_SEPARADORA);
            ejercicio = datos[0];
        }
        catch(Exception ex)
        {
            log.debug("No se ha podido obtener el ejercicio al que pertenece el expediente");
        }
        if(ejercicio != null && !ejercicio.equals(""))
        {
            String claveCod;
            String codigo;
            String obs;
            String resultado;
            boolean error;
            FilaValidacionDatoSuplementarioVO vo = null;

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

            //1 - Campo RESFAV
            vo = new FilaValidacionDatoSuplementarioVO();
            vo.setNombreCampo(MeLanbide31I18n.getInstance().getMensaje(codIdioma, "label.campoResFav"));
            claveCod = ConstantesMeLanbide31.CODIGOS_CAMPOS.RESFAV;
            codigo = "";
            resultado = "";
            obs = "";
            error = false;
            try
            {
                codigo = ConfigurationParameter.getParameter(claveCod, ConstantesMeLanbide31.FICHERO_CAMPOS_VAL);
                vo.setCodigo(codigo);
                try
                {
                    String nombre = this.getDescripcionCampo(codOrganizacion, codigo);
                    if(nombre != null && !nombre.equals(""))
                    {
                        vo.setNombreCampo(nombre);
                    }
                }
                catch(Exception ex)
                {
                    
                }
                if(codigo != null && !codigo.equalsIgnoreCase(""))
                {
                    //Recupero los datos y hago las validaciones
                    String valor = MeLanbide31Manager.getInstance().getValorCampoDesplegable(codOrganizacion, numExpediente, ejercicio, codigo, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    vo.setValor(valor);
                    if(valor == null || valor.equals(""))
                    {
                        //No hay datos
                        error = true;
                        obs = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "msg.campoResFavIncorrecto");
                    }
                    else if(!valor.equalsIgnoreCase(ConstantesMeLanbide31.SI) && !valor.equalsIgnoreCase(ConstantesMeLanbide31.NO))
                    {
                        //El dato es erroneo
                        error = true;
                        obs = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "msg.campoResFavIncorrecto");
                    }
                    else
                    {
                        if(valor.equalsIgnoreCase(ConstantesMeLanbide31.SI))
                        {
                            //se deben rellenar todos los datos hasta "Revisor del expediente"
                            if(validarResolucionFavorable(codOrganizacion, numExpediente, ejercicio))
                            {
                                //Hay que comprobar el campo solicitud en plazo
                                String solicEnPlazo = MeLanbide31Manager.getInstance().getValorCampoDesplegable(codOrganizacion, numExpediente, ejercicio, ConfigurationParameter.getParameter(ConstantesMeLanbide31.CODIGOS_CAMPOS.SOLICENPLAZO, ConstantesMeLanbide31.FICHERO_CAMPOS_VAL), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                                if(solicEnPlazo != null && solicEnPlazo.equalsIgnoreCase(ConstantesMeLanbide31.NO))
                                {
                                    error = true;
                                    obs = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "msg.resFavNoFavorableSolicNoEnPlazo");
                                }
                                else
                                {
                                    //El dato es correcto
                                    resultado = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "label.correcto");
                                    vo.setResultado(MeLanbide31Utils.formatearMensajeColor(resultado, ConstantesMeLanbide31.VERDE));
                                }
                            }
                            else
                            {
                                error = true;
                                obs = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "msg.resFavFaltaDatosPorRellenar");
                            }
                        }
                        else
                        {
                            if(validarResolucionNoFavorable(codOrganizacion, numExpediente, ejercicio))
                            {
                                //El dato es correcto
                                resultado = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "label.correcto");
                                vo.setResultado(MeLanbide31Utils.formatearMensajeColor(resultado, ConstantesMeLanbide31.VERDE));
                            }
                            else
                            {
                                error = true;
                                obs = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "msg.resNoFavCamposOblig");
                            }
                        }
                    }
                }
                else
                {
                    error = true;
                    obs = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "msg.errorValidacion");
                }
            }
            catch(Exception ex)
            {
                error = true;
                obs = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "msg.errorValidacion");
            }
            if(error)
            {
                try
                {
                    vo.setCodigo(codigo);
                    vo.setNombreCampo(MeLanbide31I18n.getInstance().getMensaje(codIdioma, "label.campoResFav"));
                    resultado = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "label.error");
                    vo.setResultado(MeLanbide31Utils.formatearMensajeColor(resultado, ConstantesMeLanbide31.ROJO));
                    vo.setObservaciones(obs);
                }
                catch(Exception ex)
                {

                }
            }
            listaValidaciones.add(vo);

            //2 - Campo FECINICONTRATO
            vo = new FilaValidacionDatoSuplementarioVO();
            vo.setNombreCampo(MeLanbide31I18n.getInstance().getMensaje(codIdioma, "label.campoFecIniContrato"));
            claveCod = ConstantesMeLanbide31.CODIGOS_CAMPOS.FECINICONTRATO;
            codigo = "";
            resultado = "";
            obs = "";
            error = false;
            try
            {
                codigo = ConfigurationParameter.getParameter(claveCod, ConstantesMeLanbide31.FICHERO_CAMPOS_VAL);
                vo.setCodigo(codigo);
                try
                {
                    String nombre = this.getDescripcionCampo(codOrganizacion, codigo);
                    if(nombre != null && !nombre.equals(""))
                    {
                        vo.setNombreCampo(nombre);
                    }
                }
                catch(Exception ex)
                {
                    
                }
                if(codigo != null && !codigo.equalsIgnoreCase(""))
                {
                    //Recupero los datos y hago las validaciones
                    Date valor = MeLanbide31Manager.getInstance().getValorCampoFecha(codOrganizacion, numExpediente, ejercicio, codigo, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    if(valor == null)
                    {
                        //No hay datos
                        vo.setValor(MeLanbide31I18n.getInstance().getMensaje(codIdioma, "label.vacio"));
                        resultado = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "label.sinDatos");
                        vo.setResultado(MeLanbide31Utils.formatearMensajeColor(resultado, ConstantesMeLanbide31.NEGRO));
                    }
                    else
                    {
                        Date fecDesde = null;
                        Date fecHasta = null;
                        String formatoFecha = ConfigurationParameter.getParameter("formatoFecha", ConstantesMeLanbide31.FICHERO_CAMPOS_VAL);
                        SimpleDateFormat format = new SimpleDateFormat(formatoFecha);
                        vo.setValor(format.format(valor));
                        try
                        {
                            fecDesde = format.parse(ConfigurationParameter.getParameter("valor.fecIniContrato.fecDesde", ConstantesMeLanbide31.FICHERO_CAMPOS_VAL));
                            fecHasta = format.parse(ConfigurationParameter.getParameter("valor.fecIniContrato.fecHasta", ConstantesMeLanbide31.FICHERO_CAMPOS_VAL));
                            if(valor.before(fecDesde) || valor.after(fecHasta))
                            {
                                //El dato es erroneo
                                error = true;
                                obs = String.format(MeLanbide31I18n.getInstance().getMensaje(codIdioma, "msg.campoFecIniContratoIncorrecto"), format.format(fecDesde), format.format(fecHasta));
                            }
                            else
                            {
                                //El dato es correcto
                                resultado = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "label.correcto");
                                vo.setResultado(MeLanbide31Utils.formatearMensajeColor(resultado, ConstantesMeLanbide31.VERDE));
                            }
                        }
                        catch(Exception ex)
                        {
                            ex.printStackTrace();
                            log.debug("Ha ocurrido un error al cargar las fechas desde/hasta para validar el campo FECINICONTRATO");
                            error = true;
                            obs = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "msg.errorValidacion");
                        }
                    }
                }
                else
                {
                    error = true;
                    obs = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "msg.errorValidacion");
                }
            }
            catch(Exception ex)
            {
                error = true;
                obs = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "msg.errorValidacion");
            }
            if(error)
            {
                try
                {
                    resultado = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "label.error");
                    vo.setResultado(MeLanbide31Utils.formatearMensajeColor(resultado, ConstantesMeLanbide31.ROJO));
                    vo.setObservaciones(obs);
                }
                catch(Exception ex)
                {

                }
            }
            listaValidaciones.add(vo);

            //3 - Campo FECALTASS
            vo = new FilaValidacionDatoSuplementarioVO();
            vo.setNombreCampo(MeLanbide31I18n.getInstance().getMensaje(codIdioma, "label.campoFecAltaSS"));
            claveCod = ConstantesMeLanbide31.CODIGOS_CAMPOS.FECALTASS;
            codigo = "";
            resultado = "";
            obs = "";
            error = false;
            try
            {
                codigo = ConfigurationParameter.getParameter(claveCod, ConstantesMeLanbide31.FICHERO_CAMPOS_VAL);
                vo.setCodigo(codigo);
                try
                {
                    String nombre = this.getDescripcionCampo(codOrganizacion, codigo);
                    if(nombre != null && !nombre.equals(""))
                    {
                        vo.setNombreCampo(nombre);
                    }
                }
                catch(Exception ex)
                {
                    
                }
                if(codigo != null && !codigo.equalsIgnoreCase(""))
                {
                    //Recupero los datos y hago las validaciones
                    Date valor = MeLanbide31Manager.getInstance().getValorCampoFecha(codOrganizacion, numExpediente, ejercicio, codigo, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    if(valor == null)
                    {
                        //No hay datos
                        vo.setValor(MeLanbide31I18n.getInstance().getMensaje(codIdioma, "label.vacio"));
                        resultado = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "label.sinDatos");
                        vo.setResultado(MeLanbide31Utils.formatearMensajeColor(resultado, ConstantesMeLanbide31.NEGRO));
                    }
                    else
                    {
                        Date fecDesde = null;
                        Date fecHasta = null;
                        String formatoFecha = ConfigurationParameter.getParameter("formatoFecha", ConstantesMeLanbide31.FICHERO_CAMPOS_VAL);
                        SimpleDateFormat format = new SimpleDateFormat(formatoFecha);
                        vo.setValor(format.format(valor));
                        try
                        {
                            fecDesde = format.parse(ConfigurationParameter.getParameter("valor.fecAltaSS.fecDesde", ConstantesMeLanbide31.FICHERO_CAMPOS_VAL));
                            fecHasta = format.parse(ConfigurationParameter.getParameter("valor.fecAltaSS.fecHasta", ConstantesMeLanbide31.FICHERO_CAMPOS_VAL));
                            if(valor.before(fecDesde) || valor.after(fecHasta))
                            {
                                //El dato es erroneo
                                error = true;
                                obs = String.format(MeLanbide31I18n.getInstance().getMensaje(codIdioma, "msg.campoFecAltaSSIncorrecto"), format.format(fecDesde), format.format(fecHasta));
                            }
                            else
                            {
                                //El dato es correcto
                                resultado = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "label.correcto");
                                vo.setResultado(MeLanbide31Utils.formatearMensajeColor(resultado, ConstantesMeLanbide31.VERDE));
                            }
                        }
                        catch(Exception ex)
                        {
                            log.debug("Ha ocurrido un error al cargar las fechas desde/hasta para validar el campo FECALTASS");
                            error = true;
                            obs = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "msg.errorValidacion");
                        }
                    }
                }
                else
                {
                    error = true;
                    obs = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "msg.errorValidacion");
                }
            }
            catch(Exception ex)
            {
                error = true;
                obs = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "msg.errorValidacion");
            }
            if(error)
            {
                try
                {
                    resultado = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "label.error");
                    vo.setResultado(MeLanbide31Utils.formatearMensajeColor(resultado, ConstantesMeLanbide31.ROJO));
                    vo.setObservaciones(obs);
                }
                catch(Exception ex)
                {

                }
            }
            listaValidaciones.add(vo);

//            //4 - Campo SOLICENPLAZO
//            vo = new FilaValidacionDatoSuplementarioVO();
//            vo.setNombreCampo(MeLanbide31I18n.getInstance().getMensaje(codIdioma, "label.campoSolicEnPlazo"));
//            claveCod = ConstantesMeLanbide31.CODIGOS_CAMPOS.SOLICENPLAZO;
//            codigo = "";
//            resultado = "";
//            obs = "";
//            error = false;
//            try
//            {
//                codigo = ConfigurationParameter.getParameter(claveCod, ConstantesMeLanbide31.FICHERO_CAMPOS_VAL);
//                vo.setCodigo(codigo);
//                try
//                {
//                    String nombre = this.getDescripcionCampo(codOrganizacion, codigo);
//                    if(nombre != null && !nombre.equals(""))
//                    {
//                        vo.setNombreCampo(nombre);
//                    }
//                }
//                catch(Exception ex)
//                {
//                    
//                }
//                if(codigo != null && !codigo.equalsIgnoreCase(""))
//                {
//                    //Recupero los datos y hago las validaciones
//                    String valor = MeLanbide31Manager.getInstance().getValorCampoDesplegable(codOrganizacion, numExpediente, ejercicio, codigo, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
//                    vo.setValor(valor);
//                    if(valor == null || valor.equals(""))
//                    {
//                        //No hay datos
//                        vo.setValor(MeLanbide31I18n.getInstance().getMensaje(codIdioma, "label.vacio"));
//                        resultado = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "label.sinDatos");
//                        vo.setResultado(MeLanbide31Utils.formatearMensajeColor(resultado, ConstantesMeLanbide31.NEGRO));
//                    }
//                    else if(!valor.equalsIgnoreCase(ConstantesMeLanbide31.SI) && !valor.equalsIgnoreCase(ConstantesMeLanbide31.NO))
//                    {
//                        //El dato es erroneo
//                        error = true;
//                        obs = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "msg.campoSolicEnPlazoIncorrecto");
//                    }
//                    else
//                    {
//                        //El dato es correcto
//                        resultado = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "label.correcto");
//                        vo.setResultado(MeLanbide31Utils.formatearMensajeColor(resultado, ConstantesMeLanbide31.VERDE));
//                    }
//                }
//                else
//                {
//                    error = true;
//                    obs = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "msg.errorValidacion");
//                }
//            }
//            catch(Exception ex)
//            {
//                error = true;
//                obs = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "msg.errorValidacion");
//            }
//            if(error)
//            {
//                try
//                {
//                    resultado = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "label.error");
//                    vo.setResultado(MeLanbide31Utils.formatearMensajeColor(resultado, ConstantesMeLanbide31.ROJO));
//                    vo.setObservaciones(obs);
//                }
//                catch(Exception ex)
//                {
//
//                }
//            }
//            listaValidaciones.add(vo);
            
            //4 - Campo SOLICENPLAZO
            vo = new FilaValidacionDatoSuplementarioVO();
            vo.setNombreCampo(MeLanbide31I18n.getInstance().getMensaje(codIdioma, "label.campoSolicEnPlazo"));
            claveCod = ConstantesMeLanbide31.CODIGOS_CAMPOS.SOLICENPLAZO;
            codigo = "";
            resultado = "";
            obs = "";
            error = false;
            try
            {
                codigo = ConfigurationParameter.getParameter(claveCod, ConstantesMeLanbide31.FICHERO_CAMPOS_VAL);
                vo.setCodigo(codigo);
                try
                {
                    String nombre = this.getDescripcionCampo(codOrganizacion, codigo);
                    if(nombre != null && !nombre.equals(""))
                    {
                        vo.setNombreCampo(nombre);
                    }
                }
                catch(Exception ex)
                {
                    
                }
                if(codigo != null && !codigo.equalsIgnoreCase(""))
                {
                    //Recupero los datos y hago las validaciones
                    String valor = MeLanbide31Manager.getInstance().getValorCampoDesplegable(codOrganizacion, numExpediente, ejercicio, codigo, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    vo.setValor(valor);
                    if(valor == null || valor.equals(""))
                    {
                        //No hay datos
                        vo.setValor(MeLanbide31I18n.getInstance().getMensaje(codIdioma, "label.vacio"));
                        resultado = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "label.sinDatos");
                        vo.setResultado(MeLanbide31Utils.formatearMensajeColor(resultado, ConstantesMeLanbide31.NEGRO));
                    }
                    else if(valor.equalsIgnoreCase(ConstantesMeLanbide31.SI))
                    {
                        //Hay que comprobar la fecha de alta SS
                        String formatoFecha = ConfigurationParameter.getParameter("formatoFecha", ConstantesMeLanbide31.FICHERO_CAMPOS_VAL);
                        SimpleDateFormat format = new SimpleDateFormat(formatoFecha);
                        Date fechaAltaSS = MeLanbide31Manager.getInstance().getValorCampoFecha(codOrganizacion, numExpediente, ejercicio, ConfigurationParameter.getParameter(ConstantesMeLanbide31.CODIGOS_CAMPOS.FECALTASS, ConstantesMeLanbide31.FICHERO_CAMPOS_VAL), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                        if(fechaAltaSS != null)
                        {
                            Date fecha1 = format.parse(ConfigurationParameter.getParameter("valor.fechaAltaSS1", ConstantesMeLanbide31.FICHERO_CAMPOS_VAL));
                            if(fechaAltaSS.before(fecha1))
                            {
                                //La fecha alta SS es anterior a la establecida
                                Date fechaPresRegistro = MeLanbide31Manager.getInstance().getValorCampoFecha(codOrganizacion, numExpediente, ejercicio, ConfigurationParameter.getParameter(ConstantesMeLanbide31.CODIGOS_CAMPOS.FECPRESREGISTRO, ConstantesMeLanbide31.FICHERO_CAMPOS_VAL), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                                if(fechaPresRegistro != null)
                                {
                                    Date fecha2 = format.parse(ConfigurationParameter.getParameter("valor.fechaPresReg1", ConstantesMeLanbide31.FICHERO_CAMPOS_VAL));
                                    if(fechaPresRegistro.before(fecha2))
                                    {
                                        //El dato es correcto
                                        resultado = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "label.correcto");
                                        vo.setResultado(MeLanbide31Utils.formatearMensajeColor(resultado, ConstantesMeLanbide31.VERDE));
                                    }
                                    else
                                    {
                                        //El dato es erroneo
                                        error = true;
                                        obs = String.format(MeLanbide31I18n.getInstance().getMensaje(codIdioma, "msg.fecPresRegistroIncorrecto3"), format.format(fecha2));
                                    }
                                }
                                else
                                {
                                    error = true;
                                    obs = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "msg.errorValidacion");
                                }
                            }
                            else
                            {
                                //La fecha alta SS es posterior a la establecida
                                Date fechaPresRegistro = MeLanbide31Manager.getInstance().getValorCampoFecha(codOrganizacion, numExpediente, ejercicio, ConfigurationParameter.getParameter(ConstantesMeLanbide31.CODIGOS_CAMPOS.FECPRESREGISTRO, ConstantesMeLanbide31.FICHERO_CAMPOS_VAL), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                                if(fechaPresRegistro != null)
                                {
                                    Date fecha2 = format.parse(ConfigurationParameter.getParameter("valor.fechaPresReg2", ConstantesMeLanbide31.FICHERO_CAMPOS_VAL));
                                    //Sumo un mes a la fecha alta SS
                                    Calendar cal = new GregorianCalendar();
                                    cal.setTime(fechaAltaSS);
                                    cal.set(Calendar.MONTH, cal.get(Calendar.MONTH)+1);
                                    fechaAltaSS = cal.getTime();

                                    //if(!fechaPresRegistro.before(fechaAltaSS) || !fechaPresRegistro.before(fecha2))
                                    if(fechaPresRegistro.after(fechaAltaSS) || fechaPresRegistro.after(fecha2))
                                    {
                                        //El dato es erroneo
                                        error = true;
                                        obs = String.format(MeLanbide31I18n.getInstance().getMensaje(codIdioma, "msg.fecPresRegistroIncorrecto4"), format.format(fecha2));
                                    }
                                    else
                                    {
                                        //El dato es correcto
                                        resultado = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "label.correcto");
                                        vo.setResultado(MeLanbide31Utils.formatearMensajeColor(resultado, ConstantesMeLanbide31.VERDE));
                                    }
                                }
                                else
                                {
                                    error = true;
                                    obs = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "msg.errorValidacion");
                                }
                            }
                        }
                        else
                        {
                            error = true;
                            obs = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "msg.errorValidacion");
                        }
                    }
                    else if(valor.equalsIgnoreCase(ConstantesMeLanbide31.NO))
                    {
                        //Hay que comprobar la fecha de alta SS
                        String formatoFecha = ConfigurationParameter.getParameter("formatoFecha", ConstantesMeLanbide31.FICHERO_CAMPOS_VAL);
                        SimpleDateFormat format = new SimpleDateFormat(formatoFecha);
                        Date fechaAltaSS = MeLanbide31Manager.getInstance().getValorCampoFecha(codOrganizacion, numExpediente, ejercicio, ConfigurationParameter.getParameter(ConstantesMeLanbide31.CODIGOS_CAMPOS.FECALTASS, ConstantesMeLanbide31.FICHERO_CAMPOS_VAL), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                        if(fechaAltaSS != null)
                        {
                            Date fecha1 = format.parse(ConfigurationParameter.getParameter("valor.fechaAltaSS1", ConstantesMeLanbide31.FICHERO_CAMPOS_VAL));
                            if(fechaAltaSS.before(fecha1))
                            {
                                //La fecha alta SS es anterior a la establecida
                                Date fechaPresRegistro = MeLanbide31Manager.getInstance().getValorCampoFecha(codOrganizacion, numExpediente, ejercicio, ConfigurationParameter.getParameter(ConstantesMeLanbide31.CODIGOS_CAMPOS.FECPRESREGISTRO, ConstantesMeLanbide31.FICHERO_CAMPOS_VAL), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                                if(fechaPresRegistro != null)
                                {
                                    Date fecha2 = format.parse(ConfigurationParameter.getParameter("valor.fechaPresReg1", ConstantesMeLanbide31.FICHERO_CAMPOS_VAL));
                                    if(fechaPresRegistro.before(fecha2))
                                    {
                                        //El dato es erroneo
                                        error = true;
                                        obs = String.format(MeLanbide31I18n.getInstance().getMensaje(codIdioma, "msg.solicEntregadaEnPlazo"), format.format(fecha2));
                                    }
                                    else
                                    {
                                        //El dato es correcto
                                        resultado = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "label.correcto");
                                        vo.setResultado(MeLanbide31Utils.formatearMensajeColor(resultado, ConstantesMeLanbide31.VERDE));
                                    }
                                }
                                else
                                {
                                    error = true;
                                    obs = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "msg.errorValidacion");
                                }
                            }
                            else
                            {
                                //La fecha alta SS es posterior a la establecida
                                Date fechaPresRegistro = MeLanbide31Manager.getInstance().getValorCampoFecha(codOrganizacion, numExpediente, ejercicio, ConfigurationParameter.getParameter(ConstantesMeLanbide31.CODIGOS_CAMPOS.FECPRESREGISTRO, ConstantesMeLanbide31.FICHERO_CAMPOS_VAL), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                                if(fechaPresRegistro != null)
                                {
                                    Date fecha2 = format.parse(ConfigurationParameter.getParameter("valor.fechaPresReg2", ConstantesMeLanbide31.FICHERO_CAMPOS_VAL));
                                    //Sumo un mes a la fecha alta SS
                                    Calendar cal = new GregorianCalendar();
                                    cal.setTime(fechaAltaSS);
                                    cal.set(Calendar.MONTH, cal.get(Calendar.MONTH)+1);
                                    fechaAltaSS = cal.getTime();

                                    //if(!fechaPresRegistro.before(fechaAltaSS) || !fechaPresRegistro.before(fecha2))
                                    if(fechaPresRegistro.after(fechaAltaSS) || fechaPresRegistro.after(fecha2))
                                    {
                                        //El dato es correcto
                                        resultado = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "label.correcto");
                                        vo.setResultado(MeLanbide31Utils.formatearMensajeColor(resultado, ConstantesMeLanbide31.VERDE));
                                    }
                                    else
                                    {
                                        //El dato es erroneo
                                        error = true;
                                        obs = String.format(MeLanbide31I18n.getInstance().getMensaje(codIdioma, "msg.solicEntregadaEnPlazo"), format.format(fecha2));
                                    }
                                }
                                else
                                {
                                    error = true;
                                    obs = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "msg.errorValidacion");
                                }
                            }
                        }
                        else
                        {
                            error = true;
                            obs = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "msg.errorValidacion");
                        }
                        if(!error)
                        {
                            String resFav = MeLanbide31Manager.getInstance().getValorCampoDesplegable(codOrganizacion, numExpediente, ejercicio, ConfigurationParameter.getParameter(ConstantesMeLanbide31.CODIGOS_CAMPOS.RESFAV, ConstantesMeLanbide31.FICHERO_CAMPOS_VAL), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                            if(resFav == null || !resFav.equalsIgnoreCase(ConstantesMeLanbide31.NO))
                            {
                                //El dato es erroneo
                                error = true;
                                obs = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "msg.resFavNoFavorableSolicNoEnPlazo");
                            }
                            else
                            {
                                //El dato es correcto
                                resultado = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "label.correcto");
                                vo.setResultado(MeLanbide31Utils.formatearMensajeColor(resultado, ConstantesMeLanbide31.VERDE));
                            }
                        }
                    }
                    else
                    {
                        //El dato es erroneo
                        error = true;
                        obs = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "msg.campoSolicEnPlazoIncorrecto");
                    }
                }
                else
                {
                    error = true;
                    obs = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "msg.errorValidacion");
                }
            }
            catch(Exception ex)
            {
                error = true;
                obs = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "msg.errorValidacion");
            }
            if(error)
            {
                try
                {
                    resultado = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "label.error");
                    vo.setResultado(MeLanbide31Utils.formatearMensajeColor(resultado, ConstantesMeLanbide31.ROJO));
                    vo.setObservaciones(obs);
                }
                catch(Exception ex)
                {

                }
            }
            listaValidaciones.add(vo);

            //5 - Campo SUBVTOTAL
            vo = new FilaValidacionDatoSuplementarioVO();
            vo.setNombreCampo(MeLanbide31I18n.getInstance().getMensaje(codIdioma, "label.campoSubvTotal"));
            claveCod = ConstantesMeLanbide31.CODIGOS_CAMPOS.SUBVTOTAL;
            codigo = "";
            resultado = "";
            obs = "";
            error = false;
            try
            {
                codigo = ConfigurationParameter.getParameter(claveCod, ConstantesMeLanbide31.FICHERO_CAMPOS_VAL);
                vo.setCodigo(codigo);
                try
                {
                    String nombre = this.getDescripcionCampo(codOrganizacion, codigo);
                    if(nombre != null && !nombre.equals(""))
                    {
                        vo.setNombreCampo(nombre);
                    }
                }
                catch(Exception ex)
                {
                    
                }
                if(codigo != null && !codigo.equalsIgnoreCase(""))
                {
                    //Recupero los datos y hago las validaciones
                    BigDecimal valor = MeLanbide31Manager.getInstance().getValorCampoNumerico(codOrganizacion, numExpediente, ejercicio, codigo, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    String resFav = MeLanbide31Manager.getInstance().getValorCampoDesplegable(codOrganizacion, numExpediente, ejercicio, ConfigurationParameter.getParameter(ConstantesMeLanbide31.CODIGOS_CAMPOS.RESFAV, ConstantesMeLanbide31.FICHERO_CAMPOS_VAL), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    if(valor == null)
                    {
                        //No hay datos. En este caso, hay que comprobar si la resolucion es favorable
                        //Si la resolucion es favorable, entonces SUBVTOTAL tiene que ser mayor que 0
                        if(resFav != null && resFav.equalsIgnoreCase(ConstantesMeLanbide31.SI))
                        {
                            error = true;
                            obs = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "msg.campoSubvTotalConResFavIncorrecto");
                        }
                        else
                        {
                            resultado = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "label.sinDatos");
                            vo.setResultado(MeLanbide31Utils.formatearMensajeColor(resultado, ConstantesMeLanbide31.NEGRO));
                        }
                        vo.setValor(MeLanbide31I18n.getInstance().getMensaje(codIdioma, "label.vacio"));
                    }
                    else
                    {
                        vo.setValor(valor.toString());
                        //El campo esta informado, con lo cual hay que comprobar:
                        //1) El valor no puede ser mayor que el valor indicado en el fichero de configuración
                        //2) Si la resolución es favorable, el valor tiene que mayor que 0
                        try
                        {
                            Float maxValor = Float.parseFloat(ConfigurationParameter.getParameter("valor.maxValorSubvTotal", ConstantesMeLanbide31.FICHERO_CAMPOS_VAL));
                            if(valor.floatValue() > maxValor)
                            {
                                //El dato es erroneo
                                error = true;
                                obs = String.format(MeLanbide31I18n.getInstance().getMensaje(codIdioma, "msg.campoSubvTotalSuperaMax"), maxValor.toString());
                            }
                            if(resFav != null && resFav.equalsIgnoreCase(ConstantesMeLanbide31.SI))
                            {
                                if(valor.floatValue() <= 0.0)
                                {
                                    //El dato es erroneo
                                    error = true;
                                    obs = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "msg.campoSubvTotalConResFavIncorrecto");
                                }
                                else
                                {
                                    //El dato es correcto
                                    resultado = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "label.correcto");
                                    vo.setResultado(MeLanbide31Utils.formatearMensajeColor(resultado, ConstantesMeLanbide31.VERDE));
                                }
                            }
                            else
                            {
                                error = true;
                                if(valor.floatValue() < 0.0)
                                {
                                    obs = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "msg.numeroNegativo");
                                }
                                else if(valor.floatValue() > maxValor)
                                {
                                    obs = String.format(MeLanbide31I18n.getInstance().getMensaje(codIdioma, "msg.campoSubvTotalSuperaMax"), maxValor.toString());
                                }
                                else
                                {
                                    obs = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "msg.resFavNoIndicado");
                                }
                            }
                        }
                        catch(Exception ex)
                        {
                            log.debug("Ha ocurrido un error al cargar el valor maximo del campo SUBVTOTAL");
                            error = true;
                            obs = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "msg.errorValidacion");
                        }
                    }
                }
                else
                {
                    error = true;
                    obs = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "msg.errorValidacion");
                }
            }
            catch(Exception ex)
            {
                error = true;
                obs = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "msg.errorValidacion");
            }
            if(error)
            {
                try
                {
                    resultado = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "label.error");
                    vo.setResultado(MeLanbide31Utils.formatearMensajeColor(resultado, ConstantesMeLanbide31.ROJO));
                    vo.setObservaciones(obs);
                }
                catch(Exception ex)
                {

                }
            }
            listaValidaciones.add(vo);

            //6 - Campo FECFINCONTRATO
            vo = new FilaValidacionDatoSuplementarioVO();
            vo.setNombreCampo(MeLanbide31I18n.getInstance().getMensaje(codIdioma, "label.campoFecFinContrato"));
            claveCod = ConstantesMeLanbide31.CODIGOS_CAMPOS.FECFINCONTRATO;
            codigo = "";
            resultado = "";
            obs = "";
            error = false;
            try
            {
                codigo = ConfigurationParameter.getParameter(claveCod, ConstantesMeLanbide31.FICHERO_CAMPOS_VAL);
                vo.setCodigo(codigo);
                try
                {
                    String nombre = this.getDescripcionCampo(codOrganizacion, codigo);
                    if(nombre != null && !nombre.equals(""))
                    {
                        vo.setNombreCampo(nombre);
                    }
                }
                catch(Exception ex)
                {
                    
                }
                if(codigo != null && !codigo.equalsIgnoreCase(""))
                {
                    //Recupero los datos y hago las validaciones
                    Date valor = MeLanbide31Manager.getInstance().getValorCampoFecha(codOrganizacion, numExpediente, ejercicio, codigo, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    if(valor == null)
                    {
                        //No hay datos
                        vo.setValor(MeLanbide31I18n.getInstance().getMensaje(codIdioma, "label.vacio"));
                        resultado = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "label.sinDatos");
                        vo.setResultado(MeLanbide31Utils.formatearMensajeColor(resultado, ConstantesMeLanbide31.NEGRO));
                    }
                    else
                    {
                        String formatoFecha = ConfigurationParameter.getParameter("formatoFecha", ConstantesMeLanbide31.FICHERO_CAMPOS_VAL);
                        SimpleDateFormat format = new SimpleDateFormat(formatoFecha);
                        vo.setValor(format.format(valor));
                        //El valor FECFINCONTRATO no puede superar FECALTASS + 12 meses
                        Date fecAltaSS = MeLanbide31Manager.getInstance().getValorCampoFecha(codOrganizacion, numExpediente, ejercicio, ConfigurationParameter.getParameter(ConstantesMeLanbide31.CODIGOS_CAMPOS.FECALTASS, ConstantesMeLanbide31.FICHERO_CAMPOS_VAL), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                        GregorianCalendar cal = new GregorianCalendar();
                        cal.setTime(fecAltaSS);
                        cal.add(Calendar.YEAR,1);
                        if(valor.after(cal.getTime()))
                        {
                            //El dato es erroneo
                            error = true;
                            obs = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "msg.campoFecFinContratoIncorrecto");
                        }
                        else
                        {
                            //El dato es correcto
                            resultado = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "label.correcto");
                            vo.setResultado(MeLanbide31Utils.formatearMensajeColor(resultado, ConstantesMeLanbide31.VERDE));
                        }
                    }
                }
                else
                {
                    error = true;
                    obs = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "msg.errorValidacion");
                }
            }
            catch(Exception ex)
            {
                error = true;
                obs = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "msg.errorValidacion");
            }
            if(error)
            {
                try
                {
                    resultado = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "label.error");
                    vo.setResultado(MeLanbide31Utils.formatearMensajeColor(resultado, ConstantesMeLanbide31.ROJO));
                    vo.setObservaciones(obs);
                }
                catch(Exception ex)
                {

                }
            }
            listaValidaciones.add(vo);

    //        //7 - Campo SUBVTOTAL
    //        vo = new FilaValidacionDatoSuplementarioVO();
    //        vo.setNombreCampo(MeLanbide31I18n.getInstance().getMensaje(codIdioma, "label.campoResFav"));
    //        claveCod = ConstantesMeLanbide31.CODIGOS_CAMPOS.SUBVTOTAL;
    //        claveDesc = "";
    //        codigo = "";
    //        desc = "";
    //        resultado = "";
    //        error = false;
    //        try
    //        {
    //            codigo = ConfigurationParameter.getParameter(claveCod, ConstantesMeLanbide31.FICHERO_CAMPOS_VAL);
    //            vo.setCodigo(codigo);
//                try
//                {
//                    String nombre = this.getDescripcionCampo(codOrganizacion, codigo);
//                    if(nombre != null && !nombre.equals(""))
//                    {
//                        vo.setNombreCampo(nombre);
//                    }
//                }
//                catch(Exception ex)
//                {
//                    
//                }
    //            if(codigo != null && !codigo.equalsIgnoreCase(""))
    //            {
    //                //Recupero los datos y hago las validaciones
    //                 
    //            }
    //            else
    //            {
    //                error = true;
    //            }
    //        }
    //        catch(Exception ex)
    //        {
    //            error = true;
    //        }
    //        if(error)
    //        {
    //            try
    //            {
    //                resultado = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "label.error");
    //                vo.setResultado(MeLanbide31Utils.formatearMensajeColor(resultado, ConstantesMeLanbide31.ROJO));
    //            }
    //            catch(Exception ex)
    //            {
    //                
    //            }
    //        }
    //        listaValidaciones.add(vo);

            //8 - Campo PRIMERPAGO60
            vo = new FilaValidacionDatoSuplementarioVO();
            vo.setNombreCampo(MeLanbide31I18n.getInstance().getMensaje(codIdioma, "label.campoPrimerPago60"));
            claveCod = ConstantesMeLanbide31.CODIGOS_CAMPOS.PRIMERPAGO60;
            codigo = "";
            resultado = "";
            obs = "";
            error = false;
            try
            {
                codigo = ConfigurationParameter.getParameter(claveCod, ConstantesMeLanbide31.FICHERO_CAMPOS_VAL);
                vo.setCodigo(codigo);
                try
                {
                    String nombre = this.getDescripcionCampo(codOrganizacion, codigo);
                    if(nombre != null && !nombre.equals(""))
                    {
                        vo.setNombreCampo(nombre);
                    }
                }
                catch(Exception ex)
                {
                    
                }
                if(codigo != null && !codigo.equalsIgnoreCase(""))
                {
                    //Recupero los datos y hago las validaciones
                    BigDecimal valor = MeLanbide31Manager.getInstance().getValorCampoNumerico(codOrganizacion, numExpediente, ejercicio, codigo, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    if(valor == null)
                    {
                        //No hay datos
                        vo.setValor(MeLanbide31I18n.getInstance().getMensaje(codIdioma, "label.vacio"));
                        resultado = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "label.sinDatos");
                        vo.setResultado(MeLanbide31Utils.formatearMensajeColor(resultado, ConstantesMeLanbide31.NEGRO));
                    }
                    else
                    {
                        vo.setValor(valor.toString());
                        if(valor.floatValue() < 0.0)
                        {
                            error = true;
                            obs = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "msg.numeroNegativo");
                        }
                        else
                        {
                            try
                            {
                                float porParte1 = Float.parseFloat(ConfigurationParameter.getParameter("valor.partePago1Subv", ConstantesMeLanbide31.FICHERO_CAMPOS_VAL));
                                BigDecimal totSub = MeLanbide31Manager.getInstance().getValorCampoNumerico(codOrganizacion, numExpediente, ejercicio, ConfigurationParameter.getParameter(ConstantesMeLanbide31.CODIGOS_CAMPOS.SUBVTOTAL, ConstantesMeLanbide31.FICHERO_CAMPOS_VAL), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                                if(totSub != null)
                                {
                                    double f = totSub.doubleValue();
                                    double parte1 = (f*porParte1)/100;
                                    double d = redondearDosDecimales(parte1);
                                    if(valor.doubleValue() != d)
                                    {
                                        //El dato es erroneo
                                        error = true;
                                        BigDecimal scale = new BigDecimal(porParte1);
                                        scale = scale.setScale(2, RoundingMode.UNNECESSARY);
                                        obs = String.format(MeLanbide31I18n.getInstance().getMensaje(codIdioma, "msg.campoPrimerPagoIncorrecto"), scale.toString(), "%");
                                    }
                                    else
                                    {
                                        //El dato es correcto
                                        resultado = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "label.correcto");
                                        vo.setResultado(MeLanbide31Utils.formatearMensajeColor(resultado, ConstantesMeLanbide31.VERDE));
                                    }
                                }
                                else
                                {
                                    log.debug("Ha ocurrido un error al recoger el valor SUBVTOTAL");
                                    error = true;
                                    obs = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "msg.errorValidacion");
                                }
                            }
                            catch(Exception ex)
                            {
                                log.debug("Ha ocurrido un error al recoger el porcentaje del primer pago de SUBVTOTAL");
                                error = true;
                                obs = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "msg.errorValidacion");
                            }
                        }
                    }
                }
                else
                {
                    error = true;
                    obs = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "msg.errorValidacion");
                }
            }
            catch(Exception ex)
            {
                error = true;
                obs = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "msg.errorValidacion");
            }
            if(error)
            {
                try
                {
                    resultado = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "label.error");
                    vo.setResultado(MeLanbide31Utils.formatearMensajeColor(resultado, ConstantesMeLanbide31.ROJO));
                    vo.setObservaciones(obs);
                }
                catch(Exception ex)
                {

                }
            }
            listaValidaciones.add(vo);

            //9 - Campo NUMCUENTABANCARIA
            vo = new FilaValidacionDatoSuplementarioVO();
            vo.setNombreCampo(MeLanbide31I18n.getInstance().getMensaje(codIdioma, "label.numCuentaBancaria"));
            claveCod = ConstantesMeLanbide31.CODIGOS_CAMPOS.NUMCUENTABANCARIA;
            codigo = "";
            resultado = "";
            obs = "";
            error = false;
            try
            {
                codigo = ConfigurationParameter.getParameter(claveCod, ConstantesMeLanbide31.FICHERO_CAMPOS_VAL);
                vo.setCodigo(codigo);
                try
                {
                    String nombre = this.getDescripcionCampo(codOrganizacion, codigo);
                    if(nombre != null && !nombre.equals(""))
                    {
                        vo.setNombreCampo(nombre);
                    }
                }
                catch(Exception ex)
                {
                    
                }
                if(codigo != null && !codigo.equalsIgnoreCase(""))
                {
                    //Recupero los datos y hago las validaciones
                    String valor = MeLanbide31Manager.getInstance().getValorCampoTexto(codOrganizacion, numExpediente, ejercicio, codigo, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    vo.setValor(valor);
                    if(valor == null || valor.equals(""))
                    {
                        //No hay datos
                        vo.setValor(MeLanbide31I18n.getInstance().getMensaje(codIdioma, "label.vacio"));
                        resultado = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "label.sinDatos");
                        vo.setResultado(MeLanbide31Utils.formatearMensajeColor(resultado, ConstantesMeLanbide31.NEGRO));
                    }
                    else
                    {
                        String regExp = "^\\d{4}-\\d{4}-\\d{2}-\\d{10}$";
                        Pattern patron = Pattern.compile(regExp);
                        Matcher encaja = patron.matcher(valor);
                        if(!encaja.matches())
                        {
                            error = true;
                            obs = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "msg.campoNumCuentaFormatoIncorrecto");
                        }
                        else if(!MeLanbide31Utils.validarCuentaBancaria(valor))
                        {
                            //El dato es erroneo
                            error = true;
                            obs = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "msg.campoNumCuentaIncorrecto");
                        }
                        else
                        {
                            //El dato es correcto
                            resultado = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "label.correcto");
                            vo.setResultado(MeLanbide31Utils.formatearMensajeColor(resultado, ConstantesMeLanbide31.VERDE));
                        }
                    }
                }
                else
                {
                    error = true;
                    obs = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "msg.errorValidacion");
                }
            }
            catch(Exception ex)
            {
                error = true;
                obs = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "msg.errorValidacion");
            }
            if(error)
            {
                try
                {
                    resultado = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "label.error");
                    vo.setResultado(MeLanbide31Utils.formatearMensajeColor(resultado, ConstantesMeLanbide31.ROJO));
                    vo.setObservaciones(obs);
                }
                catch(Exception ex)
                {

                }
            }
            listaValidaciones.add(vo);

            //10 - Campo SEGUNDOPAGO40
            vo = new FilaValidacionDatoSuplementarioVO();
            vo.setNombreCampo(MeLanbide31I18n.getInstance().getMensaje(codIdioma, "label.campoSegundoPago40"));
            claveCod = ConstantesMeLanbide31.CODIGOS_CAMPOS.SEGUNDOPAGO40;
            codigo = "";
            resultado = "";
            obs = "";
            error = false;
            try
            {
                codigo = ConfigurationParameter.getParameter(claveCod, ConstantesMeLanbide31.FICHERO_CAMPOS_VAL);
                vo.setCodigo(codigo);
                try
                {
                    String nombre = this.getDescripcionCampo(codOrganizacion, codigo);
                    if(nombre != null && !nombre.equals(""))
                    {
                        vo.setNombreCampo(nombre);
                    }
                }
                catch(Exception ex)
                {
                    
                }
                if(codigo != null && !codigo.equalsIgnoreCase(""))
                {
                    //Recupero los datos y hago las validaciones
                    BigDecimal valor = MeLanbide31Manager.getInstance().getValorCampoNumerico(codOrganizacion, numExpediente, ejercicio, codigo, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    if(valor == null)
                    {
                        //No hay datos
                        vo.setValor(MeLanbide31I18n.getInstance().getMensaje(codIdioma, "label.vacio"));
                        resultado = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "label.sinDatos");
                        vo.setResultado(MeLanbide31Utils.formatearMensajeColor(resultado, ConstantesMeLanbide31.NEGRO));
                    }
                    else
                    {
                        vo.setValor(valor.toString());
                        if(valor.floatValue() < 0.0)
                        {
                            error = true;
                            obs = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "msg.numeroNegativo");
                        }
                        else
                        {
                            Float maxValor = Float.parseFloat(ConfigurationParameter.getParameter("valor.maxValorPago2", ConstantesMeLanbide31.FICHERO_CAMPOS_VAL));
                            if(valor.floatValue() > maxValor || valor.floatValue() < 0.0)
                            {
                                //El dato es erroneo
                                error = true;
                                obs = String.format(MeLanbide31I18n.getInstance().getMensaje(codIdioma, "msg.campoSegundoPagoIncorrecto"), maxValor.toString());
                            }
                            else
                            {
                                //El dato es correcto
                                resultado = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "label.correcto");
                                vo.setResultado(MeLanbide31Utils.formatearMensajeColor(resultado, ConstantesMeLanbide31.VERDE));
                            }
                        }
                    }
                }
                else
                {
                    error = true;
                    obs = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "msg.errorValidacion");
                }
            }
            catch(Exception ex)
            {
                error = true;
                obs = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "msg.errorValidacion");
            }
            if(error)
            {
                try
                {
                    resultado = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "label.error");
                    vo.setResultado(MeLanbide31Utils.formatearMensajeColor(resultado, ConstantesMeLanbide31.ROJO));
                    vo.setObservaciones(obs);
                }
                catch(Exception ex)
                {

                }
            }
            listaValidaciones.add(vo); 
            
            //11 - Campo FECPRESREGISTRO
            vo = new FilaValidacionDatoSuplementarioVO();
            vo.setNombreCampo(MeLanbide31I18n.getInstance().getMensaje(codIdioma, "label.fecPresRegistro"));
            claveCod = ConstantesMeLanbide31.CODIGOS_CAMPOS.FECPRESREGISTRO;
            codigo = "";
            resultado = "";
            obs = "";
            error = false;
            try
            {
                codigo = ConfigurationParameter.getParameter(claveCod, ConstantesMeLanbide31.FICHERO_CAMPOS_VAL);
                vo.setCodigo(codigo);
                try
                {
                    String nombre = this.getDescripcionCampo(codOrganizacion, codigo);
                    if(nombre != null && !nombre.equals(""))
                    {
                        vo.setNombreCampo(nombre);
                    }
                }
                catch(Exception ex)
                {
                    
                }
                if(codigo != null && !codigo.equalsIgnoreCase(""))
                {
                    //Recupero los datos y hago las validaciones
                    Date valor = MeLanbide31Manager.getInstance().getValorCampoFecha(codOrganizacion, numExpediente, ejercicio, codigo, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    if(valor == null)
                    {
                        //No hay datos
                        vo.setValor(MeLanbide31I18n.getInstance().getMensaje(codIdioma, "label.vacio"));
                        resultado = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "label.sinDatos");
                        vo.setResultado(MeLanbide31Utils.formatearMensajeColor(resultado, ConstantesMeLanbide31.NEGRO));
                    }
                    else
                    {
                        String formatoFecha = ConfigurationParameter.getParameter("formatoFecha", ConstantesMeLanbide31.FICHERO_CAMPOS_VAL);
                        SimpleDateFormat format = new SimpleDateFormat(formatoFecha);
                        vo.setValor(format.format(valor));
                        String solicEnPlazo = MeLanbide31Manager.getInstance().getValorCampoDesplegable(codOrganizacion, numExpediente, ejercicio, ConfigurationParameter.getParameter(ConstantesMeLanbide31.CODIGOS_CAMPOS.SOLICENPLAZO, ConstantesMeLanbide31.FICHERO_CAMPOS_VAL), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                        if(solicEnPlazo != null && !solicEnPlazo.equals(""))
                        {
                            //if(solicEnPlazo.equalsIgnoreCase(ConstantesMeLanbide31.SI))
                            //{
                                //Hay que comprobar la fecha de alta SS
                                Date fechaAltaSS = MeLanbide31Manager.getInstance().getValorCampoFecha(codOrganizacion, numExpediente, ejercicio, ConfigurationParameter.getParameter(ConstantesMeLanbide31.CODIGOS_CAMPOS.FECALTASS, ConstantesMeLanbide31.FICHERO_CAMPOS_VAL), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                                if(fechaAltaSS != null)
                                {
                                    Date fecha1 = format.parse(ConfigurationParameter.getParameter("valor.fechaAltaSS1", ConstantesMeLanbide31.FICHERO_CAMPOS_VAL));
                                    if(fechaAltaSS.before(fecha1))
                                    {
                                        //La fecha alta SS es anterior a la establecida
                                        Date fechaPresRegistro = MeLanbide31Manager.getInstance().getValorCampoFecha(codOrganizacion, numExpediente, ejercicio, ConfigurationParameter.getParameter(ConstantesMeLanbide31.CODIGOS_CAMPOS.FECPRESREGISTRO, ConstantesMeLanbide31.FICHERO_CAMPOS_VAL), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                                        if(fechaPresRegistro != null)
                                        {
                                            Date fecha2 = format.parse(ConfigurationParameter.getParameter("valor.fechaPresReg1", ConstantesMeLanbide31.FICHERO_CAMPOS_VAL));
                                            if(fechaPresRegistro.before(fecha2))
                                            {
                                                //El dato es correcto
                                                resultado = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "label.correcto");
                                                vo.setResultado(MeLanbide31Utils.formatearMensajeColor(resultado, ConstantesMeLanbide31.VERDE));
                                            }
                                            else
                                            {
                                                //El dato es erroneo
                                                error = true;
                                                obs = String.format(MeLanbide31I18n.getInstance().getMensaje(codIdioma, "msg.fecPresRegistroIncorrecto1"), format.format(fecha2));
                                            }
                                        }
                                        else
                                        {
                                            error = true;
                                            obs = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "msg.errorValidacion");
                                        }
                                    }
                                    else
                                    {
                                        //La fecha alta SS es posterior a la establecida
                                        Date fechaPresRegistro = MeLanbide31Manager.getInstance().getValorCampoFecha(codOrganizacion, numExpediente, ejercicio, ConfigurationParameter.getParameter(ConstantesMeLanbide31.CODIGOS_CAMPOS.FECPRESREGISTRO, ConstantesMeLanbide31.FICHERO_CAMPOS_VAL), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                                        if(fechaPresRegistro != null)
                                        {
                                            Date fecha2 = format.parse(ConfigurationParameter.getParameter("valor.fechaPresReg2", ConstantesMeLanbide31.FICHERO_CAMPOS_VAL));
                                            //Sumo un mes a la fecha alta SS
                                            Calendar cal = new GregorianCalendar();
                                            cal.setTime(fechaAltaSS);
                                            cal.set(Calendar.MONTH, cal.get(Calendar.MONTH)+1);
                                            fechaAltaSS = cal.getTime();

//                                            if(!fechaPresRegistro.before(fechaAltaSS) || !fechaPresRegistro.before(fecha2))
//                                            {
//                                                //El dato es erroneo
//                                                error = true;
//                                                obs = String.format(MeLanbide31I18n.getInstance().getMensaje(codIdioma, "msg.fecPresRegistroIncorrecto2"), format.format(fecha2));
//                                            }
                                            if(fechaPresRegistro.after(fechaAltaSS) || fechaPresRegistro.after(fecha2))
                                            {
                                                //El dato es erroneo
                                                error = true;
                                                obs = String.format(MeLanbide31I18n.getInstance().getMensaje(codIdioma, "msg.fecPresRegistroIncorrecto2"), format.format(fecha2));
                                            }
                                            else
                                            {
                                                //El dato es correcto
                                                resultado = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "label.correcto");
                                                vo.setResultado(MeLanbide31Utils.formatearMensajeColor(resultado, ConstantesMeLanbide31.VERDE));
                                            }
                                        }
                                        else
                                        {
                                            error = true;
                                            obs = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "msg.errorValidacion");
                                        }
                                    }
                                }
                                else
                                {
                                    error = true;
                                    obs = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "msg.errorValidacion");
                                }
                            //}
                        }
                        else
                        {
                            //El dato es correcto
                            resultado = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "label.correcto");
                            vo.setResultado(MeLanbide31Utils.formatearMensajeColor(resultado, ConstantesMeLanbide31.VERDE));
                        }
                    }
                }
                else
                {
                    error = true;
                    obs = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "msg.errorValidacion");
                }
            }
            catch(Exception ex)
            {
                error = true;
                obs = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "msg.errorValidacion");
            }
            if(error)
            {
                try
                {
                    resultado = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "label.error");
                    vo.setResultado(MeLanbide31Utils.formatearMensajeColor(resultado, ConstantesMeLanbide31.ROJO));
                    vo.setObservaciones(obs);
                }
                catch(Exception ex)
                {

                }
            }
            listaValidaciones.add(vo);
            
            
            
            //12 - PROVINCIA
            vo = new FilaValidacionDatoSuplementarioVO();
            vo.setNombreCampo(MeLanbide31I18n.getInstance().getMensaje(codIdioma, "label.provincia"));
            resultado = "";
            obs = "";
            error = false;
            boolean provinciaOK = false;//La utilizare para validar el cod. postal
            try
            {
                //Recupero los datos y hago las validaciones
                String valor = MeLanbide31Manager.getInstance().getProvinciaTercero(codOrganizacion, numExpediente, ejercicio, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if(valor == null || valor.equalsIgnoreCase(""))
                {
                    //No hay datos
                    vo.setValor(MeLanbide31I18n.getInstance().getMensaje(codIdioma, "label.vacio"));
                    resultado = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "label.sinDatos");
                    vo.setResultado(MeLanbide31Utils.formatearMensajeColor(resultado, ConstantesMeLanbide31.NEGRO));
                }
                else
                {
                    vo.setValor(valor);
                    String provincias = ConfigurationParameter.getParameter("valor.codigosProvincia", ConstantesMeLanbide31.FICHERO_CAMPOS_VAL);
                    if(provincias != null && !provincias.equals(""))
                    {
                        String[] lista = provincias.split(",");
                        int i = 0;
                        boolean encontrado = false;
                        try
                        {
                            int codNumerico = -1;
                            int valorNumerico = Integer.parseInt(valor);
                            while(i < lista.length && !encontrado)
                            {   
                                codNumerico = Integer.parseInt(lista[i]);
                                if(codNumerico == valorNumerico)
                                {
                                    encontrado = true;
                                }
                                i++;
                            }
                        }
                        catch(Exception ex)
                        {
                            
                        }
                        if(encontrado)
                        {
                            //El dato es correcto
                            provinciaOK = true;
                            resultado = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "label.correcto");
                            vo.setResultado(MeLanbide31Utils.formatearMensajeColor(resultado, ConstantesMeLanbide31.VERDE));
                        }
                        else
                        {
                            error = true;
                            obs = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "msg.provinciaIncorrecto");
                            try
                            {
                            
                                for(i = 0; i < lista.length; i++)
                                {
                                    if(i == 0)
                                    {
                                        obs += MeLanbide31Manager.getInstance().getDescripcionProvincia(lista[i],this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                                    }
                                    else
                                    {
                                        obs += ", "+MeLanbide31Manager.getInstance().getDescripcionProvincia(lista[i], this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                                    }
                                }
                            }
                            catch(Exception ex)
                            {
                                obs = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "msg.provinciaIncorrecto");
                            }
                        }
                    }
                    else
                    {
                        error = true;
                        obs = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "msg.errorValidacion");
                    }
                }
            }
            catch(Exception ex)
            {
                error = true;
                obs = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "msg.errorValidacion");
            }
            if(error)
            {
                try
                {
                    resultado = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "label.error");
                    vo.setResultado(MeLanbide31Utils.formatearMensajeColor(resultado, ConstantesMeLanbide31.ROJO));
                    vo.setObservaciones(obs);
                }
                catch(Exception ex)
                {

                }
            }
            listaValidaciones.add(vo);
            
            //13 - CODIGO POSTAL
            vo = new FilaValidacionDatoSuplementarioVO();
            vo.setNombreCampo(MeLanbide31I18n.getInstance().getMensaje(codIdioma, "label.codPostal"));
            resultado = "";
            obs = "";
            error = false;
            try
            {
                //Recupero los datos y hago las validaciones
                String valor = MeLanbide31Manager.getInstance().getCodPostalTercero(codOrganizacion, numExpediente, ejercicio, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if(valor == null || valor.equalsIgnoreCase(""))
                {
                    //No hay datos
                    vo.setValor(MeLanbide31I18n.getInstance().getMensaje(codIdioma, "label.vacio"));
                    resultado = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "label.sinDatos");
                    vo.setResultado(MeLanbide31Utils.formatearMensajeColor(resultado, ConstantesMeLanbide31.NEGRO));
                }
                else
                {
                    vo.setValor(valor);
                    
                    if(provinciaOK)
                    {
                        String provincia = MeLanbide31Manager.getInstance().getProvinciaTercero(codOrganizacion, numExpediente, ejercicio, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                        if(provincia != null && !provincia.equals(""))
                        {
                            provincia = MeLanbide31Utils.pad(provincia, 2, "0", ConstantesMeLanbide31.LPAD);
                            if(valor.startsWith(provincia))
                            {
                                //El dato es correcto
                                resultado = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "label.correcto");
                                vo.setResultado(MeLanbide31Utils.formatearMensajeColor(resultado, ConstantesMeLanbide31.VERDE));
                            }
                            else
                            {
                                error = true;
                                obs = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "msg.codPostalIncorrecto");
                            }
                        }
                        else
                        {
                            error = true;
                            obs = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "msg.errorValidacion");
                        }
                    }
                    else
                    {
                        error = true;
                        obs = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "msg.provinciaIncorrecto");
                        try
                        {
                            String provincias = ConfigurationParameter.getParameter("valor.codigosProvincia", ConstantesMeLanbide31.FICHERO_CAMPOS_VAL);
                            String[] lista = provincias.split(",");
                            for(int i = 0; i < lista.length; i++)
                            {
                                if(i == 0)
                                {
                                    obs += MeLanbide31Manager.getInstance().getDescripcionProvincia(lista[i],this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                                }
                                else
                                {
                                    obs += ", "+MeLanbide31Manager.getInstance().getDescripcionProvincia(lista[i], this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                                }
                            }
                        }
                        catch(Exception ex)
                        {
                            obs = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "msg.provinciaIncorrecto");
                        }
                    }
                }
            }
            catch(Exception ex)
            {
                error = true;
                obs = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "msg.errorValidacion");
            }
            if(error)
            {
                try
                {
                    resultado = MeLanbide31I18n.getInstance().getMensaje(codIdioma, "label.error");
                    vo.setResultado(MeLanbide31Utils.formatearMensajeColor(resultado, ConstantesMeLanbide31.ROJO));
                    vo.setObservaciones(obs);
                }
                catch(Exception ex)
                {

                }
            }
            listaValidaciones.add(vo);
        }
        return listaValidaciones;
    }
    
    private boolean resFavTodosLosDatosRellenos(int codOrganizacion, String numExpediente, String ejercicio) throws Exception
    {
        String claveCod = "";
        String codigo = "";
        //FECPRESREGISTRO
        claveCod = ConstantesMeLanbide31.CODIGOS_CAMPOS.FECPRESREGISTRO;
        codigo = ConfigurationParameter.getParameter(claveCod, ConstantesMeLanbide31.FICHERO_CAMPOS_VAL);
        Date fecPresRegistro = MeLanbide31Manager.getInstance().getValorCampoFecha(codOrganizacion, numExpediente, ejercicio, codigo, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        if(fecPresRegistro == null)
        {
            return false;
        }
        
        //FECMAXIMARESOL
        claveCod = ConstantesMeLanbide31.CODIGOS_CAMPOS.FECMAXIMARESOL;
        codigo = ConfigurationParameter.getParameter(claveCod, ConstantesMeLanbide31.FICHERO_CAMPOS_VAL);
        Date fecMaximaResol = MeLanbide31Manager.getInstance().getValorCampoFecha(codOrganizacion, numExpediente, ejercicio, codigo, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        if(fecMaximaResol == null)
        {
            return false;
        }
        
        //FECINICONTRATO
        claveCod = ConstantesMeLanbide31.CODIGOS_CAMPOS.FECINICONTRATO;
        codigo = ConfigurationParameter.getParameter(claveCod, ConstantesMeLanbide31.FICHERO_CAMPOS_VAL);
        Date fecIniContrato = MeLanbide31Manager.getInstance().getValorCampoFecha(codOrganizacion, numExpediente, ejercicio, codigo, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        if(fecIniContrato == null)
        {
            return false;
        }
        
        //RANGOCONTRATO
        claveCod = ConstantesMeLanbide31.CODIGOS_CAMPOS.RANGOCONTRATO;
        codigo = ConfigurationParameter.getParameter(claveCod, ConstantesMeLanbide31.FICHERO_CAMPOS_VAL);
        String rangoContrato = MeLanbide31Manager.getInstance().getValorCampoDesplegable(codOrganizacion, numExpediente, ejercicio, codigo, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        if(rangoContrato == null || rangoContrato.equals(""))
        {
            return false;
        }
        
        //FECALTASS
        claveCod = ConstantesMeLanbide31.CODIGOS_CAMPOS.FECALTASS;
        codigo = ConfigurationParameter.getParameter(claveCod, ConstantesMeLanbide31.FICHERO_CAMPOS_VAL);
        Date fecAltaSS = MeLanbide31Manager.getInstance().getValorCampoFecha(codOrganizacion, numExpediente, ejercicio, codigo, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        if(fecAltaSS == null)
        {
            return false;
        }
        
        //SOLICENPLAZO
        claveCod = ConstantesMeLanbide31.CODIGOS_CAMPOS.SOLICENPLAZO;
        codigo = ConfigurationParameter.getParameter(claveCod, ConstantesMeLanbide31.FICHERO_CAMPOS_VAL);
        String solicEnPlazo = MeLanbide31Manager.getInstance().getValorCampoDesplegable(codOrganizacion, numExpediente, ejercicio, codigo, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        if(solicEnPlazo == null || solicEnPlazo.equals(""))
        {
            return false;
        }
        
        //NIFPERSCONTR
        claveCod = ConstantesMeLanbide31.CODIGOS_CAMPOS.NIFPERSCONTR;
        codigo = ConfigurationParameter.getParameter(claveCod, ConstantesMeLanbide31.FICHERO_CAMPOS_VAL);
        String nifPersContr = MeLanbide31Manager.getInstance().getValorCampoDesplegable(codOrganizacion, numExpediente, ejercicio, codigo, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        if(nifPersContr == null || nifPersContr.equals(""))
        {
            return false;
        }
        
        //NOMBAPELPERSCONTR
        claveCod = ConstantesMeLanbide31.CODIGOS_CAMPOS.NOMBAPELPERSCONTR;
        codigo = ConfigurationParameter.getParameter(claveCod, ConstantesMeLanbide31.FICHERO_CAMPOS_VAL);
        String nombApel = MeLanbide31Manager.getInstance().getValorCampoDesplegable(codOrganizacion, numExpediente, ejercicio, codigo, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        if(nombApel == null || nombApel.equals(""))
        {
            return false;
        }
        
        //RESFAV
        claveCod = ConstantesMeLanbide31.CODIGOS_CAMPOS.RESFAV;
        codigo = ConfigurationParameter.getParameter(claveCod, ConstantesMeLanbide31.FICHERO_CAMPOS_VAL);
        String resFav = MeLanbide31Manager.getInstance().getValorCampoDesplegable(codOrganizacion, numExpediente, ejercicio, codigo, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        if(resFav == null || resFav.equals(""))
        {
            return false;
        }
        
        //TIPOCONTRATO
        claveCod = ConstantesMeLanbide31.CODIGOS_CAMPOS.TIPOCONTRATO;
        codigo = ConfigurationParameter.getParameter(claveCod, ConstantesMeLanbide31.FICHERO_CAMPOS_VAL);
        String tipoContrato = MeLanbide31Manager.getInstance().getValorCampoDesplegable(codOrganizacion, numExpediente, ejercicio, codigo, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        if(tipoContrato == null || tipoContrato.equals(""))
        {
            return false;
        }
        
        //DURCONTMESES
        claveCod = ConstantesMeLanbide31.CODIGOS_CAMPOS.DURCONTMESES;
        codigo = ConfigurationParameter.getParameter(claveCod, ConstantesMeLanbide31.FICHERO_CAMPOS_VAL);
        BigDecimal durContMeses = MeLanbide31Manager.getInstance().getValorCampoNumerico(codOrganizacion, numExpediente, ejercicio, codigo, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        if(durContMeses == null)
        {
            return false;
        }
        
        //DURCONTDIAS
        claveCod = ConstantesMeLanbide31.CODIGOS_CAMPOS.DURCONTDIAS;
        codigo = ConfigurationParameter.getParameter(claveCod, ConstantesMeLanbide31.FICHERO_CAMPOS_VAL);
        BigDecimal durContDias = MeLanbide31Manager.getInstance().getValorCampoNumerico(codOrganizacion, numExpediente, ejercicio, codigo, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        if(durContDias == null)
        {
            return false;
        }
        
        //FECFINCONTRATO
        claveCod = ConstantesMeLanbide31.CODIGOS_CAMPOS.FECFINCONTRATO;
        codigo = ConfigurationParameter.getParameter(claveCod, ConstantesMeLanbide31.FICHERO_CAMPOS_VAL);
        Date fecFinContrato = MeLanbide31Manager.getInstance().getValorCampoFecha(codOrganizacion, numExpediente, ejercicio, codigo, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        if(fecFinContrato == null)
        {
            return false;
        }
        
        //BASECOTIZ2
        claveCod = ConstantesMeLanbide31.CODIGOS_CAMPOS.BASECOTIZ2;
        codigo = ConfigurationParameter.getParameter(claveCod, ConstantesMeLanbide31.FICHERO_CAMPOS_VAL);
        String baseCotiz2 = MeLanbide31Manager.getInstance().getValorCampoTexto(codOrganizacion, numExpediente, ejercicio, codigo, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        if(baseCotiz2 == null  || baseCotiz2.equals(""))
        {
            return false;
        }
        
        //PRIMERPAGO60
        claveCod = ConstantesMeLanbide31.CODIGOS_CAMPOS.PRIMERPAGO60;
        codigo = ConfigurationParameter.getParameter(claveCod, ConstantesMeLanbide31.FICHERO_CAMPOS_VAL);
        BigDecimal primerPago = MeLanbide31Manager.getInstance().getValorCampoNumerico(codOrganizacion, numExpediente, ejercicio, codigo, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        if(primerPago == null)
        {
            return false;
        }
        
        //SUBVTOTAL
        claveCod = ConstantesMeLanbide31.CODIGOS_CAMPOS.SUBVTOTAL;
        codigo = ConfigurationParameter.getParameter(claveCod, ConstantesMeLanbide31.FICHERO_CAMPOS_VAL);
        BigDecimal subvTotal = MeLanbide31Manager.getInstance().getValorCampoNumerico(codOrganizacion, numExpediente, ejercicio, codigo, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        if(subvTotal == null)
        {
            return false;
        }
        
        //NUMCUENTABANCARIA
        claveCod = ConstantesMeLanbide31.CODIGOS_CAMPOS.NUMCUENTABANCARIA;
        codigo = ConfigurationParameter.getParameter(claveCod, ConstantesMeLanbide31.FICHERO_CAMPOS_VAL);
        String numCuenta = MeLanbide31Manager.getInstance().getValorCampoTexto(codOrganizacion, numExpediente, ejercicio, codigo, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        if(numCuenta == null || numCuenta.equals(""))
        {
            return false;
        }
        
        //FECCONTROLDOC
        claveCod = ConstantesMeLanbide31.CODIGOS_CAMPOS.FECCONTROLDOC;
        codigo = ConfigurationParameter.getParameter(claveCod, ConstantesMeLanbide31.FICHERO_CAMPOS_VAL);
        Date fecControlDoc = MeLanbide31Manager.getInstance().getValorCampoFecha(codOrganizacion, numExpediente, ejercicio, codigo, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        if(fecControlDoc == null)
        {
            return false;
        }
        
        
        return true;
    }
    
    private boolean validarResolucionFavorable(int codOrganizacion, String numExpediente, String ejercicio) throws Exception
    {
        List<String> camposCumplimentados = MeLanbide31Manager.getInstance().getValoresCumplimentados(codOrganizacion, numExpediente, ejercicio, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        String listaCamposOblig = ConfigurationParameter.getParameter("valor.camposResFavSIOblig", ConstantesMeLanbide31.FICHERO_CAMPOS_VAL);
        String[] codigos = listaCamposOblig.split(",");
        boolean parada = false;
        int i = 0;
        while(i < codigos.length && !parada)
        {
            if(!camposCumplimentados.contains(codigos[i]))
            {
                parada = true;
            }
            i++;
        }
        if(parada)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    
    private boolean validarResolucionNoFavorable(int codOrganizacion, String numExpediente, String ejercicio) throws Exception
    {
        List<String> camposCumplimentados = MeLanbide31Manager.getInstance().getValoresCumplimentados(codOrganizacion, numExpediente, ejercicio, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        String listaCamposOblig = ConfigurationParameter.getParameter("valor.camposResFavNOOblig", ConstantesMeLanbide31.FICHERO_CAMPOS_VAL);
        String[] codigosOblig = listaCamposOblig.split(",");
        boolean parada = false;
        int i = 0;
        while(i < codigosOblig.length && !parada)
        {
            if(!camposCumplimentados.contains(codigosOblig[i]))
            {
                parada = true;
            }
            i++;
        }
        if(parada)
        {
            return false;
        }
        else
        {
            String listaCamposVacios = ConfigurationParameter.getParameter("valor.camposResFavNOVacios", ConstantesMeLanbide31.FICHERO_CAMPOS_VAL);
            String[] codigosVacios = listaCamposVacios.split(",");
            i = 0;
            while(i < codigosVacios.length && !parada)
            {
                if(camposCumplimentados.contains(codigosVacios[i]))
                {
                    parada = true;
                }
                i++;
            }
            if(parada)
            {
                return false;
            }
            else
            {
                return true;
            }
        }
    }
    
    private String getDescripcionCampo(int codOrganizacion, String codigoCampo)
    {
        String desc = "";
        String result = "";
        try
        {
            desc = MeLanbide31Manager.getInstance().getDescripcionCampo(codOrganizacion, codigoCampo, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            String tmp = desc.toLowerCase();
            String[] partes = tmp.split(" ");
            for(int i = 0; i < partes.length; i++)
            {
                String primeraLetra = partes[i].substring(0, 1);
                primeraLetra = primeraLetra.toUpperCase();
                result += primeraLetra + partes[i].substring(1, partes[i].length())+" ";
            }
            desc = result;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return desc;
    }
    

    private double redondearDosDecimales(double numero)
    {
      return Math.rint(numero * 100.0D) / 100.0D;
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
}
