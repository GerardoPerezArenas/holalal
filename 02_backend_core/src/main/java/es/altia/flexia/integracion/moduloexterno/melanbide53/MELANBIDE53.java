/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide53;

/**
 *
 * @author davidg
 */
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.melanbide53.manager.MeLanbide53Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide53.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide53.util.ConstantesMeLanbide53;
import es.altia.flexia.integracion.moduloexterno.melanbide53.util.MeLanbide53Utilities;
import es.altia.flexia.integracion.moduloexterno.melanbide53.vo.DetalleErrorVO;
import es.altia.flexia.integracion.moduloexterno.melanbide53.vo.DocumentoLan6TablaDocVO;
import es.altia.flexia.integracion.moduloexterno.melanbide53.vo.RegistroErrorCriteriosFiltroVO;
import es.altia.flexia.integracion.moduloexterno.melanbide53.vo.RegistroErrorVO;
import es.altia.flexia.integracion.moduloexterno.melanbide53.vo.RegistroIdenErrorCriteriosFiltroVO;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;
import es.altia.technical.PortableContext;
import es.altia.util.commons.MimeTypes;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import es.lanbide.lan6.adaptadoresPlatea.dokusi.beans.Lan6Documento;
import es.lanbide.lan6.adaptadoresPlatea.dokusi.beans.Lan6Solicitud;
import es.lanbide.lan6.adaptadoresPlatea.dokusi.servicios.Lan6DokusiServicios;
import es.lanbide.lan6.adaptadoresPlatea.excepciones.Lan6Excepcion;
import java.io.BufferedOutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import es.lanbide.lan6.excepciones.Lan6GestErrorestExcepcion;
import com.lanbide.lan6.errores.bean.ErrorBean;
import com.lanbide.lan6.registro.error.RegistroErrores;
import com.lanbide.lan6.retramite.solicitud.Retramitacion;
import com.lanbide.lan6.utilidades.Constantes;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.lanbide.lan6.adaptadoresPlatea.excepciones.Lan6UtilExcepcion;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.melanbide53.dao.MeLanbide53DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide53.util.MeLanbide53MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide53.vo.DetalleEstadisticaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide53.vo.EstadisticasVO;
import es.altia.flexia.integracion.moduloexterno.melanbide53.vo.EstadisticasVO_EXCEL;
import es.altia.flexia.integracion.moduloexterno.melanbide53.vo.EventoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide53.vo.FiltroEstadisticasVO;
import es.altia.flexia.integracion.moduloexterno.melanbide53.vo.InformeInternoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide53.vo.RegistroErrorVO_EXCEL;
import es.altia.flexia.integracion.moduloexterno.melanbide53.vo.RegistroErrorVO_EXCEL2;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;
import es.altia.technical.PortableContext;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.hssf.util.HSSFColor;

public class MELANBIDE53 extends ModuloIntegracionExterno {

    //Logger
    private static Logger log = LogManager.getLogger(MELANBIDE53.class);

    RegistroErrorCriteriosFiltroVO _criterioBusqueda = new RegistroErrorCriteriosFiltroVO();

    public String cargarPantallaPrincipal(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.error("Entramos en cargarPantalla Principal - Modulo53 - Gestion Errores");
        List<RegistroErrorVO> ListErrores = new ArrayList<RegistroErrorVO>();
        AdaptadorSQLBD adapt = null;
        try {
            if (request.getParameter("codOrganizacion") != null) {
                log.error("Se recibe datos de la organizacion en la request");
                codOrganizacion = Integer.valueOf(request.getParameter("codOrganizacion"));
            }

            log.error("Antes de generar el Adaptador de BD - Gestion errores");
            adapt = this.getAdaptSQLBD_GestionErores();
            Integer numMaxLIneas = Integer.valueOf(ConfigurationParameter.getParameter(ConstantesMeLanbide53.NUMERO_LINEAS_PAGINA, ConstantesMeLanbide53.FICHERO_PROPIEDADES));
            log.error("Despues de generar el Adaptador de BD - Gestion errores - Vamos a recoger datos del BD. Maximo Lineas Pagina recueperada Properties:  " + numMaxLIneas);
            RegistroErrorCriteriosFiltroVO _criterioBusqueda = new RegistroErrorCriteriosFiltroVO();
            _criterioBusqueda.setErrorRevisado(ConstantesMeLanbide53.BOOLEAN_FALSE);
            _criterioBusqueda.setNumeroInicialLinea(0);
            _criterioBusqueda.setNumeroFinalLinea(numMaxLIneas);
            _criterioBusqueda.setNumeroLineasxPagina(numMaxLIneas);
            //ListErrores = MeLanbide53Manager.getInstance().recogeRegistrosError(adapt, numMaxLIneas,1);
            ListErrores = MeLanbide53Manager.getInstance().busquedaFiltrandoListaErrores(_criterioBusqueda, adapt);
            int numeroTotalRegistros = 0;
            if (ListErrores != null) {
                // con el primer registro me vale
                numeroTotalRegistros = ListErrores.get(0).getNoTotalRegConsulta();
            }
            log.error("Control - Numero Registros Recueperados carga inicial Lista Erroes " + numeroTotalRegistros);
            request.setAttribute("numeroTotalRegistros", numeroTotalRegistros);
            request.setAttribute("iniciaModulo", "1");
            log.error("Hemos cargado la lista de errores  - asiganamos el valor en la  request");
            request.setAttribute("ListErrores", ListErrores);
        } catch (Exception ex) {
            log.error("Error en funcion cargarPantallaPrincipal: " + ex);
            ex.printStackTrace();
        }
        log.error("Fin metodo cargarPantallaPrincipal - Modulo53 - Gestion Errores - retornamos URL :  /jsp/extension/melanbide53/melanbide53.jspz");
        return "/jsp/extension/melanbide53/melanbide53.jsp";
    }

    public String cargarPantallaIdenErrores(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.error("Entramos en cargarPantalla Principal - Modulo53 - Gestion Errores");
        List<DetalleErrorVO> ListErrores = new ArrayList<DetalleErrorVO>();
        AdaptadorSQLBD adapt = null;
        try {
            if (request.getParameter("codOrganizacion") != null) {
                log.error("Se recibe datos de la organizacion en la request");
                codOrganizacion = Integer.valueOf(request.getParameter("codOrganizacion"));
            }

            Integer numMaxLIneas = Integer.valueOf(ConfigurationParameter.getParameter(ConstantesMeLanbide53.NUMERO_LINEAS_PAGINA, ConstantesMeLanbide53.FICHERO_PROPIEDADES));
            log.error("Despues de generar el Adaptador de BD - Gestion errores - Vamos a recoger datos del BD. Maximo Lineas Pagina recueperada Properties:  " + numMaxLIneas);
            RegistroIdenErrorCriteriosFiltroVO _criterioBusqueda = new RegistroIdenErrorCriteriosFiltroVO();

            log.error("Antes de generar el Adaptador de BD - Gestion errores");
            adapt = this.getAdaptSQLBD_GestionErores();
            log.error("Despues de generar el Adaptador de BD - Gestion errores - Vamos a recoger datos del BD ");
            ListErrores = MeLanbide53Manager.getInstance().busquedaIdenErrores(_criterioBusqueda, adapt);
            int numeroTotalRegistros = 0;
            if (ListErrores != null) {
                // con el primer registro me vale
                numeroTotalRegistros = ListErrores.get(0).getNoTotalRegConsulta();
            }
            log.error("Control - Numero Registros Recueperados carga inicial Lista Erroes " + numeroTotalRegistros);
            request.setAttribute("numeroTotalRegistros", numeroTotalRegistros);
            request.setAttribute("iniciaModulo", "1");

            log.error("Hemos cargado la lista de identif de errores  - asiganamos el valor en ela request");
            request.setAttribute("ListErrores", ListErrores);
        } catch (Exception ex) {
            log.error("Error en funcion cargarPantallaPrincipal: " + ex);
            ex.printStackTrace();
        }
        log.error("Fin metodo cargarPantallaPrincipal - Modulo53 - Gestion Errores - retornamos URL :  /jsp/extension/melanbide53/melanbide53.jspz");
        return "/jsp/extension/melanbide53/detalleBusquedaIdenErr.jsp";
    }

    public void busquedaFiltrandoListaErrores(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String codigoOperacion = "0";
        List<RegistroErrorVO> lista = new ArrayList<RegistroErrorVO>();
        AdaptadorSQLBD adapt = this.getAdaptSQLBD_GestionErores();
        try {
            //Recojo los parametros
            RegistroErrorCriteriosFiltroVO _criterioBusqueda = new RegistroErrorCriteriosFiltroVO();
            _criterioBusqueda.setIdProcedimiento((String) request.getParameter("idprocedimiento_busq"));
            _criterioBusqueda.setIdErrorFK((String) request.getParameter("identificadorError_busq"));
            _criterioBusqueda.setClave((String) request.getParameter("clave_busq"));
            _criterioBusqueda.setErrorNotificado((String) request.getParameter("check_notificado"));
            _criterioBusqueda.setErrorRevisado((String) request.getParameter("check_verficado"));
            _criterioBusqueda.setSistemaOrigen((String) request.getParameter("sistemaOrigen_busq"));
            _criterioBusqueda.setNumeroExpediente((String) request.getParameter("numeroExpediente_busq"));
            _criterioBusqueda.setFechaErrorInicio((String) request.getParameter("meLanbide53Fecha_busqE"));
            _criterioBusqueda.setFechaErrorFin((String) request.getParameter("meLanbide53Fecha_busqS"));
            _criterioBusqueda.setHoraErrorInicio((String) request.getParameter("hora_entrada_busq"));
            _criterioBusqueda.setHoraErrorFin((String) request.getParameter("hora_entrada_busqF"));
            _criterioBusqueda.setFechaRevisionErrorInicio((String) request.getParameter("meLanbide53Fecha_RevbusqE"));
            _criterioBusqueda.setFechaRevisionErrorFin((String) request.getParameter("meLanbide53Fecha_RevbusqF"));
            String idBDError = (String) request.getParameter("identificadorErrorBD_busq");
            _criterioBusqueda.setErrorRetramitado((String) request.getParameter("check_retramitado"));
            if (idBDError != null && !idBDError.isEmpty()) {
                _criterioBusqueda.setId(Integer.valueOf(idBDError));
            }
            lista = MeLanbide53Manager.getInstance().busquedaFiltrandoListaErrores(_criterioBusqueda, adapt);

        } catch (Exception ex) {
            log.error("Error al consultar lista de errores - Gestion  de errores - : " + ex);
            codigoOperacion = "2";
        }
        StringBuffer xmlSalida = new StringBuffer();

        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        MeLanbide53Utilities meLanbide53Utilities = MeLanbide53Utilities.getInstance();
        for (RegistroErrorVO fila : lista) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<REG_ERR_ID>");
            xmlSalida.append(fila.getId() != null ? fila.getId().toString() : "");
            xmlSalida.append("</REG_ERR_ID>");
            xmlSalida.append("<REG_ERR_FEC_ERROR>");
            xmlSalida.append(fila.getFechaError());
            xmlSalida.append("</REG_ERR_FEC_ERROR>");
            xmlSalida.append("<REG_ERR_MEN>");
            xmlSalida.append(fila.getMensajeError() != null ? meLanbide53Utilities.escapeSpecialCharacterXML(fila.getMensajeError()) : "");
            xmlSalida.append("</REG_ERR_MEN>");
            xmlSalida.append("<REG_ERR_EXCEP_MEN>");
            xmlSalida.append(fila.getMensajeException() != null ? meLanbide53Utilities.escapeSpecialCharacterXML(fila.getMensajeException()) : "");
            xmlSalida.append("</REG_ERR_EXCEP_MEN>");
            xmlSalida.append("<REG_ERR_CAUSA>");
            xmlSalida.append(fila.getCausaException() != null ? meLanbide53Utilities.escapeSpecialCharacterXML(fila.getCausaException()) : "");
            xmlSalida.append("</REG_ERR_CAUSA>");
            xmlSalida.append("<REG_ERR_TRAZA>");
            xmlSalida.append(fila.getTrazaError() != null ? meLanbide53Utilities.escapeSpecialCharacterXML(fila.getTrazaError()) : "");
            xmlSalida.append("</REG_ERR_TRAZA>");
            xmlSalida.append("<REG_ERR_ID_PROC>");
            xmlSalida.append(fila.getIdProcedimiento());
            xmlSalida.append("</REG_ERR_ID_PROC>");
            xmlSalida.append("<REG_ERR_IDEN_ERR_ID>");
            xmlSalida.append(fila.getIdErrorFK());
            xmlSalida.append("</REG_ERR_IDEN_ERR_ID>");
            xmlSalida.append("<REG_ERR_ID_CLAVE>");
            xmlSalida.append(fila.getClave());
            xmlSalida.append("</REG_ERR_ID_CLAVE>");
            xmlSalida.append("<REG_ERR_NOT>");
            xmlSalida.append(fila.getErrorNotificado());
            xmlSalida.append("</REG_ERR_NOT>");
            xmlSalida.append("<REG_ERR_REV>");
            xmlSalida.append(fila.getErrorRevisado());
            xmlSalida.append("</REG_ERR_REV>");
            xmlSalida.append("<REG_ERR_FEC_REV>");
            xmlSalida.append(fila.getFechaRevisionError());
            xmlSalida.append("</REG_ERR_FEC_REV>");
            xmlSalida.append("<REG_ERR_OBS>");
            xmlSalida.append(fila.getObservacionesError());
            xmlSalida.append("</REG_ERR_OBS>");
            xmlSalida.append("<REG_ERR_SIS_ORIG>");
            xmlSalida.append(fila.getSistemaOrigen());
            xmlSalida.append("</REG_ERR_SIS_ORIG>");
            xmlSalida.append("<REG_ERR_SITU>");
            xmlSalida.append(fila.getUbicacionError() != null ? meLanbide53Utilities.escapeSpecialCharacterXML(fila.getUbicacionError()) : "");
            xmlSalida.append("</REG_ERR_SITU>");
            xmlSalida.append("<REG_ERR_LOG>");
            xmlSalida.append(fila.getFicheroLog());
            xmlSalida.append("</REG_ERR_LOG>");
            xmlSalida.append("<REG_ERR_EVEN>");
            xmlSalida.append(fila.getEvento());
            xmlSalida.append("</REG_ERR_EVEN>");
            xmlSalida.append("<REG_ERR_ID_FLEXIA>");
            xmlSalida.append(fila.getNumeroExpediente());
            xmlSalida.append("</REG_ERR_ID_FLEXIA>");
            xmlSalida.append("<REG_ERR_SOLICITUD>");
            xmlSalida.append(fila.getXmlReglasSolicitud());
            xmlSalida.append("</REG_ERR_SOLICITUD>");
            xmlSalida.append("<REG_ERR_RETRAMIT_SN>");
            xmlSalida.append(fila.getSolicitudRetramitada());
            xmlSalida.append("</REG_ERR_RETRAMIT_SN>");
            xmlSalida.append("<REG_ERR_FEC_RETRAMIT>");
            xmlSalida.append(fila.getFechaRetramitacion());
            xmlSalida.append("</REG_ERR_FEC_RETRAMIT>");
            xmlSalida.append("<REG_ERR_RESULT_WS>");
            xmlSalida.append(fila.getResultadoRetramitacion());
            xmlSalida.append("</REG_ERR_RESULT_WS>");
            xmlSalida.append("<REG_ERR_IMPACTO>");
            xmlSalida.append(fila.getImpacto());
            xmlSalida.append("</REG_ERR_IMPACTO>");
            xmlSalida.append("<REG_ERR_TRATADO>");
            xmlSalida.append(fila.getTratado());
            xmlSalida.append("</REG_ERR_TRATADO>");

            xmlSalida.append("</FILA>");
        }
        int NumTotalRegisConsul = 0;
        if (lista != null && lista.size() > 0) {
            NumTotalRegisConsul = lista.get(0).getNoTotalRegConsulta();
        }
        xmlSalida.append("<NUMTOTALREGISTROS>");
        xmlSalida.append(NumTotalRegisConsul);
        xmlSalida.append("</NUMTOTALREGISTROS>");
        //NUMREG -- NUMTOTALREGISTROS
        xmlSalida.append("</RESPUESTA>");
        try {
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        } catch (Exception e) {
            log.error(" Error Recuperando datos en para Gestion de errores al filtrar - ", e);
        }//try-catch
    }

    public void busquedaIdenError(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String codigoOperacion = "0";
        List<DetalleErrorVO> lista = new ArrayList<DetalleErrorVO>();
        AdaptadorSQLBD adapt = this.getAdaptSQLBD_GestionErores();
        try {
            //Recojo los parametros
            RegistroIdenErrorCriteriosFiltroVO _criterioBusqueda = new RegistroIdenErrorCriteriosFiltroVO();
            _criterioBusqueda.setId((String) request.getParameter("idError"));
            _criterioBusqueda.setDescripcion((String) request.getParameter("descripcion"));
            _criterioBusqueda.setCodTipo((String) request.getParameter("codTipo"));
            _criterioBusqueda.setCodCritico((String) request.getParameter("codCritico"));

            String idBDError = (String) request.getParameter("identificadorErrorBD_busq");
            if (idBDError != null && !idBDError.isEmpty()) {
                _criterioBusqueda.setId(idBDError);
            }

            lista = MeLanbide53Manager.getInstance().busquedaIdenErrores(_criterioBusqueda, adapt);

        } catch (Exception ex) {
            log.error("Error al consultar lista de errores - Gestion  de errores - : " + ex);
            codigoOperacion = "2";
        }
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        MeLanbide53Utilities meLanbide53Utilities = MeLanbide53Utilities.getInstance();
        for (DetalleErrorVO fila : lista) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<IDEN_ERR_ID>");
            xmlSalida.append(fila.getId() != null ? fila.getId().toString() : "");
            xmlSalida.append("</IDEN_ERR_ID>");
            xmlSalida.append("<IDEN_ERR_DESC_C>");
            xmlSalida.append(fila.getDescripcionCorta() != null ? meLanbide53Utilities.escapeSpecialCharacterXML(fila.getDescripcionCorta()) : "");
            xmlSalida.append("</IDEN_ERR_DESC_C>");
            xmlSalida.append("<IDEN_ERR_DESC>");
            xmlSalida.append(fila.getDescripcion() != null ? meLanbide53Utilities.escapeSpecialCharacterXML(fila.getDescripcion()) : "");
            xmlSalida.append("</IDEN_ERR_DESC>");
            xmlSalida.append("<DES_TIPO>");
            xmlSalida.append(fila.getDesTipo());
            xmlSalida.append("</DES_TIPO>");
            xmlSalida.append("<DES_CRITICO>");
            xmlSalida.append(fila.getDesCritico());
            xmlSalida.append("</DES_CRITICO>");
            xmlSalida.append("</FILA>");
        }
        xmlSalida.append("</RESPUESTA>");
        try {
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        } catch (Exception e) {
            log.error(" Error Recuperando datos en para Gestion de errores al filtrar - ", e);
        }//try-catch
    }

    public String cargarModificarGestionError(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String nuevo = "0";
        String urlnuevoAcceso = "/jsp/extension/melanbide53/nuevaGestionErrores.jsp";
        try {
            if (request.getAttribute("nuevo") != null) {
                if (!request.getAttribute("nuevo").toString().equals("0")) {
                    request.setAttribute("nuevo", nuevo);
                }
            } else {
                request.setAttribute("nuevo", nuevo);
            }
            String id = request.getParameter("id");
            // Recuperramos datos e Acceso a modificar y cargamos en el request
            if (id != null && !id.equals("")) {
                RegistroErrorVO datModif = MeLanbide53Manager.getInstance().getErrorPorID(id, this.getAdaptSQLBD_GestionErores());
                if (datModif != null) {
                    request.setAttribute("datModif", datModif);
                }
            }
        } catch (Exception ex) {
            log.error("Error al tratar de preparar los datos para modificar y llamar la jsp de modificacion - Gestion de Errores : " + ex.getMessage());
        }
        return urlnuevoAcceso;

    }

    public void modificarGestionError(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String codigoOperacion = "0";
        List<RegistroErrorVO> lista = new ArrayList<RegistroErrorVO>();
        AdaptadorSQLBD adapt = this.getAdaptSQLBD_GestionErores();
        try {
            //Recojo los parametros
            String id = (String) request.getParameter("id");
            String meLanbide53FechaError = (String) request.getParameter("meLanbide53FechaError");
            String mensaje_error = (String) request.getParameter("mensaje_error");
            String causa_error = (String) request.getParameter("causa_error");
            String mensaje_exception = (String) request.getParameter("mensaje_exception");
            String traza = (String) request.getParameter("traza");
            String idprocedimiento = (String) request.getParameter("idprocedimiento");
            String identificador_error = (String) request.getParameter("identificador_error");
            String clave = (String) request.getParameter("clave");
            String sistema_origen = (String) request.getParameter("sistema_origen");
            String ubicacion_error = (String) request.getParameter("ubicacion_error");
            String fichero_log = (String) request.getParameter("fichero_log");
            String evento = (String) request.getParameter("evento");
            String numero_expediente = (String) request.getParameter("numero_expediente");
            String ck_notificado = (String) request.getParameter("ck_notificado");
            String ck_revisado = (String) request.getParameter("ck_revisado");
            String meLanbide53FechaRevision = (String) request.getParameter("meLanbide53FechaRevision");
            String observaciones_revision = (String) request.getParameter("observaciones_revision");
            String ck_impacto = (String) request.getParameter("ck_impacto");
            String ck_tratado = (String) request.getParameter("ck_tratado");
            
            if (id == null || id.equals("")) {
                log.error("No se ha recibido desde la JSP el id del un Error para su gestion - Modificar");
                codigoOperacion = "3";
            } else {
                RegistroErrorVO datModif = MeLanbide53Manager.getInstance().getErrorPorID(id, adapt);
                datModif.setId(Integer.parseInt(id));

//                SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
                //SimpleDateFormat formatoFecha1 = new SimpleDateFormat("dd/MM/yyyy HH24:mm:ss.SSS");
                datModif.setFechaError(meLanbide53FechaError);
                datModif.setMensajeError(mensaje_error);
                datModif.setCausaException(causa_error);
                datModif.setMensajeException(mensaje_exception);
                datModif.setTrazaError(traza);
                datModif.setIdProcedimiento(idprocedimiento);
                datModif.setIdErrorFK(identificador_error);
                datModif.setClave(clave);
                datModif.setSistemaOrigen(sistema_origen);
                datModif.setUbicacionError(ubicacion_error);
                datModif.setFicheroLog(fichero_log);
                datModif.setEvento(evento);
                datModif.setNumeroExpediente(numero_expediente);
                datModif.setErrorNotificado(ck_notificado);
                datModif.setErrorRevisado(ck_revisado);
                datModif.setFechaRevisionError(meLanbide53FechaRevision);
                datModif.setObservacionesError(observaciones_revision);
                datModif.setImpacto(ck_impacto);
                datModif.setTratado(ck_tratado);

                MeLanbide53Manager meLanbide53Manager = MeLanbide53Manager.getInstance();
                boolean modOK = meLanbide53Manager.modificarErrores(datModif, adapt);
                if (modOK) {
                    try {
                        Integer numMaxLIneas = Integer.valueOf(ConfigurationParameter.getParameter(ConstantesMeLanbide53.NUMERO_LINEAS_PAGINA, ConstantesMeLanbide53.FICHERO_PROPIEDADES));
                        lista = meLanbide53Manager.recogeRegistrosError(adapt, numMaxLIneas, 1);
                    } catch (BDException bde) {
                        codigoOperacion = "1";
                        log.error("Error de tipo BD al recueperar la lista de Errores despues de Modificar un estado de error : " + bde.getMensaje());
                    } catch (Exception ex) {
                        codigoOperacion = "2";
                        log.error("Error al recuperar la lista de Errores despues de Modificar un registro : " + ex.getMessage());
                    }
                } else {
                    codigoOperacion = "2";
                }
            }

        } catch (Exception ex) {
            codigoOperacion = "2";
        }

        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        MeLanbide53Utilities meLanbide53Utilities = MeLanbide53Utilities.getInstance();
        for (RegistroErrorVO fila : lista) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<REG_ERR_ID>");
            xmlSalida.append(fila.getId() != null ? fila.getId().toString() : "");
            xmlSalida.append("</REG_ERR_ID>");
            xmlSalida.append("<REG_ERR_FEC_ERROR>");
            xmlSalida.append(fila.getFechaError());
            xmlSalida.append("</REG_ERR_FEC_ERROR>");
            xmlSalida.append("<REG_ERR_MEN>");
            xmlSalida.append(fila.getMensajeError() != null ? meLanbide53Utilities.escapeSpecialCharacterXML(fila.getMensajeError()) : "");
            xmlSalida.append("</REG_ERR_MEN>");
            xmlSalida.append("<REG_ERR_EXCEP_MEN>");
            xmlSalida.append(fila.getMensajeException() != null ? meLanbide53Utilities.escapeSpecialCharacterXML(fila.getMensajeException()) : "");
            xmlSalida.append("</REG_ERR_EXCEP_MEN>");
            xmlSalida.append("<REG_ERR_CAUSA>");
            xmlSalida.append(fila.getCausaException() != null ? meLanbide53Utilities.escapeSpecialCharacterXML(fila.getCausaException()) : "");
            xmlSalida.append("</REG_ERR_CAUSA>");
            xmlSalida.append("<REG_ERR_TRAZA>");
            xmlSalida.append(fila.getTrazaError() != null ? meLanbide53Utilities.escapeSpecialCharacterXML(fila.getTrazaError()) : "");
            xmlSalida.append("</REG_ERR_TRAZA>");
            xmlSalida.append("<REG_ERR_ID_PROC>");
            xmlSalida.append(fila.getIdProcedimiento());
            xmlSalida.append("</REG_ERR_ID_PROC>");
            xmlSalida.append("<REG_ERR_IDEN_ERR_ID>");
            xmlSalida.append(fila.getIdErrorFK());
            xmlSalida.append("</REG_ERR_IDEN_ERR_ID>");
            xmlSalida.append("<REG_ERR_ID_CLAVE>");
            xmlSalida.append(fila.getClave());
            xmlSalida.append("</REG_ERR_ID_CLAVE>");
            xmlSalida.append("<REG_ERR_NOT>");
            xmlSalida.append(fila.getErrorNotificado());
            xmlSalida.append("</REG_ERR_NOT>");
            xmlSalida.append("<REG_ERR_REV>");
            xmlSalida.append(fila.getErrorRevisado());
            xmlSalida.append("</REG_ERR_REV>");
            xmlSalida.append("<REG_ERR_FEC_REV>");
            xmlSalida.append(fila.getFechaRevisionError());
            xmlSalida.append("</REG_ERR_FEC_REV>");
            xmlSalida.append("<REG_ERR_OBS>");
            xmlSalida.append(fila.getObservacionesError());
            xmlSalida.append("</REG_ERR_OBS>");
            xmlSalida.append("<REG_ERR_SIS_ORIG>");
            xmlSalida.append(fila.getSistemaOrigen());
            xmlSalida.append("</REG_ERR_SIS_ORIG>");
            xmlSalida.append("<REG_ERR_SITU>");
            xmlSalida.append(fila.getUbicacionError() != null ? meLanbide53Utilities.escapeSpecialCharacterXML(fila.getUbicacionError()) : "");
            xmlSalida.append("</REG_ERR_SITU>");
            xmlSalida.append("<REG_ERR_LOG>");
            xmlSalida.append(fila.getFicheroLog());
            xmlSalida.append("</REG_ERR_LOG>");
            xmlSalida.append("<REG_ERR_EVEN>");
            xmlSalida.append(fila.getEvento());
            xmlSalida.append("</REG_ERR_EVEN>");
            xmlSalida.append("<REG_ERR_ID_FLEXIA>");
            xmlSalida.append(fila.getNumeroExpediente());
            xmlSalida.append("</REG_ERR_ID_FLEXIA>");
            xmlSalida.append("</FILA>");
        }
        xmlSalida.append("</RESPUESTA>");
        try {
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        } catch (Exception e) {
            log.error("Error al modificar gestionando errores", e);
            e.printStackTrace();
        }//try-catch

    }

    public void modificarIdError(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String codigoOperacion = "0";
        List<DetalleErrorVO> lista = new ArrayList<DetalleErrorVO>();
        AdaptadorSQLBD adapt = this.getAdaptSQLBD_GestionErores();
        try {
            //Recojo los parametros
            String id = (String) request.getParameter("id");
            String descC = (String) request.getParameter("descC");
            String desc = (String) request.getParameter("descripcion");
            String tipo = (String) request.getParameter("codTipo");
            String critico = (String) request.getParameter("critico");
            String aviso = (String) request.getParameter("aviso");
            String correos = (String) request.getParameter("correos");
            String acciones = (String) request.getParameter("acciones");
            String modulo = (String) request.getParameter("mod");

            if (id == null || id.equals("")) {
                log.error("No se ha recibido desde la JSP el id del un Error para su gestion - Modificar");
                codigoOperacion = "3";
            } else {
                DetalleErrorVO datModif = MeLanbide53Manager.getInstance().getIdErrorPorID(id, adapt);
                datModif.setId(id);
                datModif.setDescripcionCorta(descC);
                datModif.setDescripcion(desc);
                datModif.setTipo(tipo);
                datModif.setCritico(critico);
                datModif.setAviso(aviso);
                datModif.setMails(correos);
                datModif.setAcciones(acciones);
                datModif.setModulo(modulo);

                MeLanbide53Manager meLanbide53Manager = MeLanbide53Manager.getInstance();
                boolean modOK = meLanbide53Manager.modificarIdErrores(datModif, adapt);
                if (modOK) {
                    try {
                        lista = meLanbide53Manager.recogeRegistrosIdenError(adapt);
                    } catch (BDException bde) {
                        codigoOperacion = "1";
                        log.error("Error de tipo BD al recueperar la lista de Errores despues de Modificar un estado de error : " + bde.getMensaje());
                    } catch (Exception ex) {
                        codigoOperacion = "2";
                        log.error("Error al recuperar la lista de Errores despues de Modificar un registro : " + ex.getMessage());
                    }
                } else {
                    codigoOperacion = "2";
                }
            }

        } catch (Exception ex) {
            codigoOperacion = "2";
        }

        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        MeLanbide53Utilities meLanbide53Utilities = MeLanbide53Utilities.getInstance();
        for (DetalleErrorVO fila : lista) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<IDEN_ERR_ID>");
            xmlSalida.append(fila.getId() != null ? fila.getId().toString() : "");
            xmlSalida.append("</IDEN_ERR_ID>");
            xmlSalida.append("<IDEN_ERR_DESC_C>");
            xmlSalida.append(fila.getDescripcionCorta() != null ? meLanbide53Utilities.escapeSpecialCharacterXML(fila.getDescripcionCorta()) : "");
            xmlSalida.append("</IDEN_ERR_DESC_C>");
            xmlSalida.append("<IDEN_ERR_DESC>");
            xmlSalida.append(fila.getDescripcion() != null ? meLanbide53Utilities.escapeSpecialCharacterXML(fila.getDescripcion()) : "");
            xmlSalida.append("</IDEN_ERR_DESC>");
            xmlSalida.append("<IDEN_ERR_TIPO>");
            xmlSalida.append(fila.getTipo());
            xmlSalida.append("</IDEN_ERR_TIPO>");
            xmlSalida.append("<IDEN_ERR_CRIT>");
            xmlSalida.append(fila.getCritico());
            xmlSalida.append("</IDEN_ERR_CRIT>");
            xmlSalida.append("<DES_TIPO>");
            xmlSalida.append(fila.getDesTipo());
            xmlSalida.append("</DES_TIPO>");
            xmlSalida.append("<DES_CRITICO>");
            xmlSalida.append(fila.getDesCritico());
            xmlSalida.append("</DES_CRITICO>");
            xmlSalida.append("</FILA>");
        }
        xmlSalida.append("</RESPUESTA>");
        try {
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        } catch (Exception e) {
            log.error("Error al modificar gestionando errores", e);
            e.printStackTrace();
        }//try-catch

    }

    public void eliminarIdError(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String codigoOperacion = "0";
        List<DetalleErrorVO> lista = new ArrayList<DetalleErrorVO>();
        AdaptadorSQLBD adapt = this.getAdaptSQLBD_GestionErores();
        try {
            //Recojo los parametros
            String id = (String) request.getParameter("idError");
            String descC = (String) request.getParameter("descC");
            String desc = (String) request.getParameter("descripcion");
            String tipo = (String) request.getParameter("codTipo");
            String critico = (String) request.getParameter("critico");
            String aviso = (String) request.getParameter("aviso");
            String correos = (String) request.getParameter("correos");
            String acciones = (String) request.getParameter("acciones");
            String modulo = (String) request.getParameter("mod");

            if (id == null || id.equals("")) {
                log.error("No se ha recibido desde la JSP el id del un Error para su gestion - Modificar");
                codigoOperacion = "3";
            } else {
                boolean borrar = MeLanbide53Manager.getInstance().getIdErrorBorrado(id, adapt);

                if (borrar) {
                    MeLanbide53Manager meLanbide53Manager = MeLanbide53Manager.getInstance();
                    boolean modOK = meLanbide53Manager.eliminarIdErrores(id, adapt);
                    if (modOK) {
                        try {
                            lista = meLanbide53Manager.recogeRegistrosIdenError(adapt);
                        } catch (BDException bde) {
                            codigoOperacion = "1";
                            log.error("Error de tipo BD al recueperar la lista de Errores despues de Modificar un estado de error : " + bde.getMensaje());
                        } catch (Exception ex) {
                            codigoOperacion = "2";
                            log.error("Error al recuperar la lista de Errores despues de Modificar un registro : " + ex.getMessage());
                        }
                    } else {
                        codigoOperacion = "2";
                    }
                } else {
                    log.error("El codigo de error '" + id + "' tiene registros asociados");
                }
            }

        } catch (Exception ex) {
            codigoOperacion = "2";
        }

        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        MeLanbide53Utilities meLanbide53Utilities = MeLanbide53Utilities.getInstance();
        for (DetalleErrorVO fila : lista) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<IDEN_ERR_ID>");
            xmlSalida.append(fila.getId() != null ? fila.getId().toString() : "");
            xmlSalida.append("</IDEN_ERR_ID>");
            xmlSalida.append("<IDEN_ERR_DESC_C>");
            xmlSalida.append(fila.getDescripcionCorta() != null ? meLanbide53Utilities.escapeSpecialCharacterXML(fila.getDescripcionCorta()) : "");
            xmlSalida.append("</IDEN_ERR_DESC_C>");
            xmlSalida.append("<IDEN_ERR_DESC>");
            xmlSalida.append(fila.getDescripcion() != null ? meLanbide53Utilities.escapeSpecialCharacterXML(fila.getDescripcion()) : "");
            xmlSalida.append("</IDEN_ERR_DESC>");
            xmlSalida.append("<DES_TIPO>");
            xmlSalida.append(fila.getDesTipo());
            xmlSalida.append("</DES_TIPO>");
            xmlSalida.append("<DES_CRITICO>");
            xmlSalida.append(fila.getDesCritico());
            xmlSalida.append("</DES_CRITICO>");
            xmlSalida.append("</FILA>");
        }
        xmlSalida.append("</RESPUESTA>");
        try {
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        } catch (Exception e) {
            log.error(" Error Recuperando datos en para Gestion de errores al eliminar - ", e);
        }//try-catch

    }

    public void nuevoIdError(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String codigoOperacion = "0";
        List<DetalleErrorVO> lista = new ArrayList<DetalleErrorVO>();
        AdaptadorSQLBD adapt = this.getAdaptSQLBD_GestionErores();
        try {
            //Recojo los parametros
            String id = (String) request.getParameter("idError");
            String descC = (String) request.getParameter("descC");
            String desc = (String) request.getParameter("descripcion");
            String tipo = (String) request.getParameter("codTipo");
            String critico = (String) request.getParameter("critico");
            String aviso = (String) request.getParameter("aviso");
            String correos = (String) request.getParameter("correos");
            String acciones = (String) request.getParameter("acciones");
            String modulo = (String) request.getParameter("mod");

            if (id == null || id.equals("")) {
                log.error("No se ha recibido desde la JSP el id del un Error para su gestion - Modificar");
                codigoOperacion = "3";
            } else {
                //DetalleErrorVO datModif = MeLanbide53Manager.getInstance().getIdErrorPorID(id, adapt);
                DetalleErrorVO datModif = new DetalleErrorVO();
                datModif.setId(id);
                datModif.setDescripcionCorta(descC);
                datModif.setDescripcion(desc);
                datModif.setTipo(tipo);
                datModif.setCritico(critico);
                datModif.setAviso(aviso);
                datModif.setMails(correos);
                datModif.setAcciones(acciones);
                datModif.setModulo(modulo);

                MeLanbide53Manager meLanbide53Manager = MeLanbide53Manager.getInstance();
                boolean modOK = meLanbide53Manager.insertarIdErrores(datModif, adapt);
                if (modOK) {
                    try {
                        lista = meLanbide53Manager.recogeRegistrosIdenError(adapt);
                    } catch (BDException bde) {
                        codigoOperacion = "1";
                        log.error("Error de tipo BD al recueperar la lista de Errores despues de Modificar un estado de error : " + bde.getMensaje());
                    } catch (Exception ex) {
                        codigoOperacion = "2";
                        log.error("Error al recuperar la lista de Errores despues de Modificar un registro : " + ex.getMessage());
                    }
                } else {
                    codigoOperacion = "2";
                }
            }

        } catch (Exception ex) {
            codigoOperacion = "2";
        }

        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        MeLanbide53Utilities meLanbide53Utilities = MeLanbide53Utilities.getInstance();
        for (DetalleErrorVO fila : lista) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<IDEN_ERR_ID>");
            xmlSalida.append(fila.getId() != null ? fila.getId().toString() : "");
            xmlSalida.append("</IDEN_ERR_ID>");
            xmlSalida.append("<IDEN_ERR_DESC_C>");
            xmlSalida.append(fila.getDescripcionCorta() != null ? meLanbide53Utilities.escapeSpecialCharacterXML(fila.getDescripcionCorta()) : "");
            xmlSalida.append("</IDEN_ERR_DESC_C>");
            xmlSalida.append("<IDEN_ERR_DESC>");
            xmlSalida.append(fila.getDescripcion() != null ? meLanbide53Utilities.escapeSpecialCharacterXML(fila.getDescripcion()) : "");
            xmlSalida.append("</IDEN_ERR_DESC>");
            xmlSalida.append("<IDEN_ERR_TIPO>");
            xmlSalida.append(fila.getTipo());
            xmlSalida.append("</IDEN_ERR_TIPO>");
            xmlSalida.append("<IDEN_ERR_CRIT>");
            xmlSalida.append(fila.getCritico());
            xmlSalida.append("</IDEN_ERR_CRIT>");
            xmlSalida.append("<DES_TIPO>");
            xmlSalida.append(fila.getDesTipo());
            xmlSalida.append("</DES_TIPO>");
            xmlSalida.append("<DES_CRITICO>");
            xmlSalida.append(fila.getDesCritico());
            xmlSalida.append("</DES_CRITICO>");
            xmlSalida.append("</FILA>");
        }
        xmlSalida.append("</RESPUESTA>");
        try {
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        } catch (Exception e) {
            log.error("Error al modificar gestionando errores", e);
            e.printStackTrace();
        }//try-catch

    }

    public void descargarDocumentoDokusiGError(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) throws Exception {
        byte[] fichero = null;
        String mimeTypeContent = "";
        String nombreFichero = "";
        String extensionFichero = "";
        try {
            log.error("-- Inicia metodo descargarDocumentoDokusiGError");
            //Recojo los parametros
            String oidSolicitud = (String) request.getParameter("oidSolicitud");
            String oid = (String) request.getParameter("oid");
            String idProcedimiento = (String) request.getParameter("idProcedimiento");
            log.error("-- Parametros recibidos : -- oidSolicitud , oid , idProcedimiento ("
                    + oidSolicitud + " , " + oid + " , " + idProcedimiento + ")");
            List<Lan6Documento> arrayAnexosSolicitud = new ArrayList<Lan6Documento>();
            boolean docSolicitudRecuperado = false;
            if (oidSolicitud == null || oidSolicitud.equals("")) {
                log.error("No se ha recibido desde la JSP el oid de la solicitud para recuperar los documentos a Descargar - Obtener Documento");
            } else {
                if (oid != null && !oid.equals("")) {
                    if (idProcedimiento != null && !idProcedimiento.equals("")) {
                        log.error("--* Recuperamos la solictud *-- oid e idproce no null");
                        Lan6Solicitud lan6Solicitud = recuperarSolicitud(oidSolicitud, idProcedimiento);
                        if (lan6Solicitud != null) {
                            if (oidSolicitud.equals(oid)) {
                                log.error("--* Recuperada solictud no null *-- Vamos a recuperar el documento de la solicitud");
                                Lan6Documento docSolicitud = lan6Solicitud.getSolicitud() != null ? lan6Solicitud.getSolicitud() : null;
                                log.error("--* Documento de la solicitud recuperado");
                                if (docSolicitud != null) {
                                    // La solicitud no devuelve el oid en el documento - su contenido no hace falta recogerlo de dokusi viene en la solicitud
                                    log.error("--* Documento Solicutud != null *--");
//                                    if (docSolicitud.getIdDocumento() != null && docSolicitud.getIdDocumento().equals(oid)) {
                                    log.error("Cargamos datos del documento de la solicitud - Se ha solicitado descargar  " + oidSolicitud + " / " + oid);
                                    fichero = docSolicitud.getContenido();
                                    mimeTypeContent = getMimeTypeFromExtension(docSolicitud.getFormat());
                                    nombreFichero = docSolicitud.getNombre();
                                    extensionFichero = ConstantesMeLanbide53.DOT + docSolicitud.getFormat();
                                    docSolicitudRecuperado = true;
//                                    } else {
//                                        log.error("--* No se cumple la condicion docSolicitud.getIdDocumento() != null && docSolicitud.getIdDocumento().equals(oid)");
                                    log.error(docSolicitud.getIdDocumento() != null ? docSolicitud.getIdDocumento() + " - " + oid : oid);
//                                    }
                                } else {
                                    log.error("No se ha recibido el documento Lan6Documento de la solicitud - Obtener Documento");
                                }
                            } else {
                                log.error("Vamos a recuperar la lista de documentos Adjuntos");
                                arrayAnexosSolicitud = lan6Solicitud.getAdjuntos() != null ? lan6Solicitud.getAdjuntos() : null;
                                log.error("Documents Adjutnos recuperados - se ha pedido el doc de la solicitud? : " + docSolicitudRecuperado);
                                if (!docSolicitudRecuperado && arrayAnexosSolicitud != null) {
                                    log.error("Lista de Adjuntod diferente de null - No Docs : " + arrayAnexosSolicitud.size());
                                    for (Lan6Documento docAnexo : arrayAnexosSolicitud) {
                                        if (docAnexo.getIdDocumento() != null && docAnexo.getIdDocumento().equals(oid)) {
                                            // El contenido de los anexos hay que recuperarlo de dokusi solo viene el OID en la solicitud
                                            Lan6Documento docConContenido = recuperarDocumentoDokusi(oid, idProcedimiento);
                                            if (docConContenido != null) {
                                                log.error("Cargamos datos  del documento anexo a la solicitud recuperado - Se ha solicitado descargar : OIDSolicitud/OIDDocumento  " + oidSolicitud + " / " + oid);
                                                fichero = docConContenido.getContenido();
                                                mimeTypeContent = getMimeTypeFromExtension(docConContenido.getFormat());
                                                nombreFichero = docConContenido.getNombre();
                                                extensionFichero = ConstantesMeLanbide53.DOT + docConContenido.getFormat();
                                                break;
                                            } else {
                                                log.error("No se pudo recuperar el cdocumento anexo solicitado oidsolicitud/oidDoc  " + oidSolicitud + "/" + oid);
                                            }

                                        } else {
                                            log.error("--* No se cumple la condicion docAnexo.getIdDocumento()!= null && docAnexo.getIdDocumento().equals(oid)");
                                            log.error(docAnexo.getIdDocumento() != null ? docAnexo.getIdDocumento() + " - " + oid : oid);
                                        }
                                    }
                                } else {
                                    log.error("No se cumple la condicion !docSolicitudRecuperado && arrayAnexosSolicitud != null");
                                }
                            }

                        } else {
                            log.error("Respuesta de la recuperacion de documentos de la solicitud a servicios de Platea llega a null - Obtener Documento");
                        }
                    } else {
                        log.error("No se ha recibido desde la JSP el idProcedimiento para instaciar los servicios de Platea - Obtener Documento");
                    }
                } else {
                    log.error("No se ha recibido desde la JSP el oid del documento solicitado pra la Descargar - Obtener Documento");
                }
            }

        } catch (Exception ex) {
            log.error(this.getClass().getName() + " -  Error al descargar un documento  ", ex);
            throw ex;
        }
        if (fichero != null) {
            BufferedOutputStream bos = null;
            try {
                log.error("Parametros para crearcrearNuevaGestionError el fichero en Outputstring: " + nombreFichero
                        + " Extension : " + extensionFichero
                        + " MimeType : " + mimeTypeContent);
                response.setContentType(mimeTypeContent);
                response.setHeader("Content-Disposition", "attachment; filename=" + nombreFichero + extensionFichero);
                response.setHeader("Content-Transfer-Encoding", "binary");
                log.error("fichero.length : " + fichero.length);
                ServletOutputStream out = response.getOutputStream();
                response.setContentLength(fichero.length);
                bos = new BufferedOutputStream(out);
                bos.write(fichero, 0, fichero.length);
                bos.flush();
                bos.close();
                log.error(" *** Termina de leer y escribir el Fichero ***  : " + nombreFichero);
            } catch (Exception e) {
                if (log.isDebugEnabled()) {
                    log.error("Excepcion en catch de descargarDocumentoDokusiGError", e);
                }
                throw e;
            } finally {
                if (bos != null) {
                    bos.close();
                }
            }
        } else {
            log.error("FICHERO NULO EN descargarDocumentoDokusiGError - No se ha recuperado ningun documento de Dokusi");
        }
        log.error("-- Fin metodo descargarDocumentoDokusiGError");
    }

    public String cargarObtenerDocumentos(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String urlnuevoAcceso = "/jsp/extension/melanbide53/obtenerDocumentos.jsp";
        try {
            // Ya hemos validado en jsp que no venga a nullo vacio
            String oidSolicitud = request.getParameter("oidSolicitud");
            String codProcedimiento = request.getParameter("codProcedimiento");
            // Recuperramos datos e Acceso a modificar y cargamos en el request
            if (oidSolicitud != null && !oidSolicitud.equals("") && codProcedimiento != null && !codProcedimiento.equals("")) {
                List<DocumentoLan6TablaDocVO> listDatDocs = new ArrayList<DocumentoLan6TablaDocVO>();
                Lan6Solicitud lan6Solicitud = this.recuperarSolicitud(oidSolicitud, codProcedimiento);
                listDatDocs = mapearLan6SolicitudToListaDocTabla(lan6Solicitud, oidSolicitud, codProcedimiento);
                if (listDatDocs != null) {
                    request.setAttribute("listaDocumentosTabla", listDatDocs);
                } else {
                    log.error(" No se ha se recuperado ningin documento para la solicitud indicada " + oidSolicitud + " o cod procedimiento a null " + codProcedimiento);
                }
            } else {
                log.error(" Aunque hemos validado en JSP Nos sigue llegando el oid de la solicitud " + oidSolicitud + " o cod procedimiento a null " + codProcedimiento);
            }
        } catch (Exception ex) {
            log.error("Error al tratar de preparar los datos para mostrar la lista de documentos relacionados con la solicitud " + ex.getMessage());
        }
        return urlnuevoAcceso;
    }

    /**
     * Devuelve la extension de fichero correspondiente al tipo MIME pasado.
     * Utilizamos la clase de flexia para no reescribir funciones
     */
    public String getMimeTypeFromExtension(String extension) {
        return MimeTypes.guessMimeTypeFromExtension(extension);
    }

    // Cambiamos la forma de recoger el Adaptador de BD - Este debe ser generico -  Sin organizacion, como FLBGEN
    private AdaptadorSQLBD getAdaptSQLBD_GestionErores() {
        if (log.isDebugEnabled()) {
            log.error("getAdaptSQLBD_GestionErores()");
        }
        ResourceBundle config = ResourceBundle.getBundle("techserver");
        String gestor = config.getString("CON.gestor");
        String jndiError = config.getString("CON.jndi.GestionErrores");
        Connection conErrores = null;
        String[] salida = null;

        if (log.isDebugEnabled()) {
            log.error("getJndi =========> ");
            log.error("gestor: " + gestor);
            log.error("jndi: " + jndiError);
        }//if(log.isDebugEnabled())

        DataSource ds = null;
        AdaptadorSQLBD adapt = null;
        synchronized (this) {
            try {
                PortableContext pc = PortableContext.getInstance();
                if (log.isDebugEnabled()) {
                    log.error("He cogido el jndi: " + jndiError);
                }
                ds = (DataSource) pc.lookup(jndiError, DataSource.class);
                // Conexión al esquema genérico
                conErrores = ds.getConnection();
                salida = new String[7];
                if (jndiError != null && gestor != null && !"".equals(jndiError) && !"".equals(gestor)) {
                    salida[0] = gestor;
                    salida[1] = "";
                    salida[2] = "";
                    salida[3] = "";
                    salida[4] = "";
                    salida[5] = "";
                    salida[6] = jndiError;
                    adapt = new AdaptadorSQLBD(salida);
                }
                conErrores.close();

            } catch (TechnicalException te) {
                te.printStackTrace();
                log.error("*** AdaptadorSQLBD: " + te.toString());
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (conErrores != null && !conErrores.isClosed()) {
                        conErrores.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }//try-catch
            }// finally
            if (log.isDebugEnabled()) {
                log.error("getAdaptSQLBD_GestionErores() : END");
            }
        }// synchronized
        return adapt;
    }//getAdaptSQLBD_GestionErores

    public Lan6Solicitud recuperarSolicitud(String oidSolicitud, String codProcedimiento) throws Lan6Excepcion, Exception {
        Lan6Solicitud lan6Solicitud = null;
        Lan6DokusiServicios servicio = new Lan6DokusiServicios(codProcedimiento);
        log.error("Hacemos la llamada a Platea para recuperar la solicitud --- ");
        try {
            lan6Solicitud = servicio.recuperarSolicitud(oidSolicitud);
        } catch (Lan6Excepcion ex) {
            log.error(MELANBIDE53.class.getName() + " - recuperarSolicitud - Lan6Excepcion - Error en la recuperacionde solicitud llamando a adaptadores de platea", ex);
            throw ex;
        } catch (Exception ex) {
            log.error(MELANBIDE53.class.getName() + " - recuperarSolicitud - Exception - Error en la recuperacionde solicitud llamando a adaptadores de platea", ex);
            throw ex;
        }

        return lan6Solicitud;
    }

    public Lan6Documento recuperarDocumentoDokusi(String oid, String codProcedimiento) throws Lan6Excepcion, Exception {
        Lan6Documento lan6Doc = null;
        Lan6DokusiServicios servicio = new Lan6DokusiServicios(codProcedimiento);
        log.error("Hacemos la llamada a Platea para recuperar la un documento anexo de la solicitud --- ");
        try {
            lan6Doc = servicio.consultarDocumento(oid);
        } catch (Lan6Excepcion ex) {
            log.error(MELANBIDE53.class.getName() + " - recuperarDocumentoDokusi - Lan6Excepcion - Error en la recuperacion de documento anexo a adaptadores de platea", ex);
            throw ex;
        } catch (Exception ex) {
            log.error(MELANBIDE53.class.getName() + " - recuperarDocumentoDokusi - Exception - Error en la recuperacion de documento anexo a adaptadores de platea", ex);
            throw ex;
        }
        return lan6Doc;
    }

    public List<DocumentoLan6TablaDocVO> mapearLan6SolicitudToListaDocTabla(Lan6Solicitud lan6Solicitud, String oidSolicitud, String idProcedimiento) throws Exception {
        List<DocumentoLan6TablaDocVO> listaDocumentos = new ArrayList<DocumentoLan6TablaDocVO>();
        DocumentoLan6TablaDocVO documentoTablaJsp = new DocumentoLan6TablaDocVO();
        try {
            if (lan6Solicitud != null) {
                if (lan6Solicitud.getSolicitud() != null) {
                    documentoTablaJsp = mapearLan6DocumentToDocumentoLan6TablaDocVO(lan6Solicitud.getSolicitud(), oidSolicitud, idProcedimiento);
                    if (documentoTablaJsp.getOid() == null || documentoTablaJsp.getOid().isEmpty()) {
                        documentoTablaJsp.setOid(documentoTablaJsp.getOid_solicitud()); // En pruebas algunas veces vienen null nos asguramos de poner el oid del documento de solicitud
                    }
                    listaDocumentos.add(documentoTablaJsp);
                    documentoTablaJsp = new DocumentoLan6TablaDocVO();
                } else {
                    log.error(" No se recupero el documento de la solicitud - mapearLan6SolicitudToListaDocTabla");
                }
                if (lan6Solicitud.getAdjuntos() != null) {
                    for (Lan6Documento docLan6 : lan6Solicitud.getAdjuntos()) {
                        documentoTablaJsp = mapearLan6DocumentToDocumentoLan6TablaDocVO(docLan6, oidSolicitud, idProcedimiento);
                        listaDocumentos.add(documentoTablaJsp);
                        documentoTablaJsp = new DocumentoLan6TablaDocVO();
                    }
                } else {
                    log.error(" No se recupero documentos adjuntos a la de solicitud - mapearLan6SolicitudToListaDocTabla");
                }
            } else {
                log.error("Respuesta de la recuperacion de documentos de la solicitud a servicios de Platea llega a null - mapearLan6SolicitudToListaDocTabla");
            }
        } catch (Exception ex) {
            log.error("Exception al mapearLan6SolicitudToListaDocTabla de la JSP", ex);
            throw ex;
        }
        return listaDocumentos;
    }

    public DocumentoLan6TablaDocVO mapearLan6DocumentToDocumentoLan6TablaDocVO(Lan6Documento docLan6, String oidSolicitud, String idProcedimiento) throws Exception {
        DocumentoLan6TablaDocVO docTablaJSP = new DocumentoLan6TablaDocVO();
        try {
            if (docLan6 != null) {
                docTablaJSP.setOid(docLan6.getIdDocumento());
                docTablaJSP.setOid_solicitud(oidSolicitud);
                docTablaJSP.setNombre(docLan6.getNombre());
                docTablaJSP.setExtension(docLan6.getFormat());
                docTablaJSP.setNombreCompleto(docLan6.getNombre() + ConstantesMeLanbide53.DOT + docLan6.getFormat());
                docTablaJSP.setIdProcedimiento(idProcedimiento);
            } else {
                log.error("Documento Lan6 recibido a Null no se puede hacer el mapeo en mapearLan6DocumentToDocumentoLan6TablaDocVO");
            }
        } catch (Exception ex) {
            log.error("Exception al mapear de un DocumentoLAn6 a un documento par lista de docs en JSP", ex);
            throw ex;
        }
        return docTablaJSP;
    }

    public String cargarPantallaDetallesIDError(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String nuevo = "0";
        String urlnuevoAcceso = "/jsp/extension/melanbide53/detalleIDError.jsp";
        try {

            String idError = request.getParameter("idError");
            String tipo = request.getParameter("visible");
            // Recuperramos datos de los detalle del error
            if (idError != null && !idError.equals("")) {
                DetalleErrorVO datError = MeLanbide53Manager.getInstance().getDetalleErrorPorID(idError, this.getAdaptSQLBD_GestionErores());
                if (datError != null) {
                    request.setAttribute("datError", datError);
                    request.setAttribute("nuevo", "0");
                }
            }
            request.setAttribute("tipo", tipo);
        } catch (Exception ex) {
            log.error("Error al tratar de preparar los datos para mostrar el detalle de in id error - Gestion de Errores : " + ex.getMessage());
        }
        return urlnuevoAcceso;

    }
//Estadisticas

    public String cargarEstadisticas(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String nuevo = "0";
        String urlnuevoAcceso = "/jsp/extension/melanbide53/InformesEstadisticos.jsp";
        try {
            if (request.getAttribute("nuevo") != null) {
                if (!request.getAttribute("nuevo").toString().equals("0")) {
                    request.setAttribute("nuevo", nuevo);
                }
            } else {
                request.setAttribute("nuevo", nuevo);
            }
            String id = request.getParameter("id");
            // Recuperramos datos e Acceso a modificar y cargamos en el request
            if (id != null && !id.equals("")) {
                RegistroErrorVO datModif = MeLanbide53Manager.getInstance().getErrorPorID(id, this.getAdaptSQLBD_GestionErores());
                if (datModif != null) {
                    request.setAttribute("datModif", datModif);
                }
            }
        } catch (Exception ex) {
            log.error("Error al tratar de preparar los datos para modificar y llamar la jsp de modificacion - Gestion de Errores : " + ex.getMessage());
        }
        return urlnuevoAcceso;

    }

    private int creaCeldasMeses(HSSFSheet hoja, HSSFRow fila, HSSFCell celda, int contCelda, HashMap<String, Integer> mapColumnMesAnyo, Date mesHasta, GregorianCalendar gcal, int numColumnasMerge, int numFila) throws Exception {
        int numMes;
        StringBuilder mesAnyo = new StringBuilder();
        while (!gcal.getTime().after(mesHasta)) {
            gcal.add(Calendar.MONTH, 1);
            celda = fila.createCell((short) contCelda++);
            numMes = gcal.get(Calendar.MONTH);
            mesAnyo.delete(0, mesAnyo.length());
            mesAnyo.append(MeLanbide53Utilities.getNombreMesGregCalendar(numMes));
            mesAnyo.append(gcal.get(Calendar.YEAR));
            celda.setCellValue(mesAnyo.toString());

            //creo columnas adicionales para combinarlas después
            for (int i = 1; i < numColumnasMerge; i++) {
                celda = fila.createCell((short) contCelda++);

            }
            //unimos las dos últimas celdas creadas en la fila actual
            //hoja.addMergedRegion(new CellRangeAddress(numFila-1, numFila-1, contCelda-numColumnasMerge, contCelda-1));
            //por cada mesAnyo que mostramos, guardamos el nºcolumna donde comienza dicho mesAnyo para poder asociar más tarde los datos del informe obtenidos a esa columna
            mapColumnMesAnyo.put(mesAnyo.toString(), contCelda - numColumnasMerge);//clave:mesAnyo, valor:nºcolumna donde comienza la celda de ese mesAnyo
        }

        return contCelda;
    }

    private AdaptadorSQLBD getAdaptSQLBD(String codOrganizacion) throws SQLException {
        if (log.isDebugEnabled()) {
            log.debug("getConnection ( codOrganizacion = " + codOrganizacion + " ) : BEGIN");
        }
        ResourceBundle config = ResourceBundle.getBundle("techserver");
        String gestor = config.getString("CON.gestor");
        String jndiGenerico = config.getString("CON.jndi");
        Connection conGenerico = null;
        Statement st = null;
        ResultSet rs = null;
        String[] salida = null;
        Connection con = null;

        if (log.isDebugEnabled()) {
            log.debug("getJndi =========> ");
            log.debug("parametro codOrganizacion: " + codOrganizacion);
            log.debug("gestor: " + gestor);
            log.debug("jndi: " + jndiGenerico);
        }//if(log.isDebugEnabled())-

        DataSource ds = null;
        AdaptadorSQLBD adapt = null;
        synchronized (this) {
            try {
                PortableContext pc = PortableContext.getInstance();
                if (log.isDebugEnabled()) {
                    log.debug("He cogido el jndi: " + jndiGenerico);
                }
                ds = (DataSource) pc.lookup(jndiGenerico, DataSource.class);
                // Conexión al esquema genérico
                conGenerico = ds.getConnection();

                String sql = "SELECT EEA_BDE FROM A_EEA WHERE EEA_APL=" + ConstantesDatos.APP_GESTION_EXPEDIENTES + " AND AAE_ORG=" + codOrganizacion;
                st = conGenerico.createStatement();
                rs = st.executeQuery(sql);
                String jndi = null;
                while (rs.next()) {
                    jndi = rs.getString("EEA_BDE");
                }//while(rs.next())

                st.close();
                rs.close();
                conGenerico.close();

                if (jndi != null && gestor != null && !"".equals(jndi) && !"".equals(gestor)) {
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
            } catch (TechnicalException te) {
                te.printStackTrace();
                log.error("*** AdaptadorSQLBD: " + te.toString());
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (conGenerico != null && !conGenerico.isClosed()) {
                    conGenerico.close();
                }
            }// finally
            if (log.isDebugEnabled()) {
                log.debug("getConnection() : END");
            }
        }// synchronized
        return adapt;
    }//getConnection

    public static String convertNumToColString(int col) {
        int excelColNum = col + 1;

        String colRef = "";
        int colRemain = excelColNum;
        while (colRemain > 0) {
            int thisPart = colRemain % 26;
            if (thisPart == 0) {
                thisPart = 26;
            }
            colRemain = (colRemain - thisPart) / 26;

            char colChar = (char) (thisPart + 64);
            colRef = colChar + colRef;
        }
        return colRef;
    }

    private String getFormulaColumnaTotal(int numColumn, int ultimaFila, int primeraFila) throws Exception {
        StringBuilder formula = new StringBuilder();
        formula.append("SUM(");
        formula.append(convertNumToColString(numColumn));
        formula.append(primeraFila);
        formula.append(":");
        formula.append(convertNumToColString(numColumn));
        formula.append(ultimaFila);
        formula.append(")");
        return formula.toString();
    }

    private void crearFichero(HSSFWorkbook libro, HttpServletResponse response) throws Exception {
        ByteArrayOutputStream excelOutput = new ByteArrayOutputStream();
        libro.write(excelOutput);
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=informeInterno.xls");
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.setContentLength(excelOutput.size());
        response.getOutputStream().write(excelOutput.toByteArray(), 0, excelOutput.size());
        response.getOutputStream().flush();
        response.getOutputStream().close();
    }

    private String getFormulaColumnaPorcentaje(int numColumn, int numFila) throws Exception {
        StringBuilder formula = new StringBuilder();
        formula.append(convertNumToColString(numColumn));
        formula.append(numFila);//primera fila de datos
        formula.append("*100/");
        formula.append(convertNumToColString(numColumn - 1));
        formula.append(numFila);
        return formula.toString();
    }

    private String getFormulaColumnaTotalII(int numColumn, int numFila) throws Exception {
        return getFormulaColumnaTotal(numColumn, numFila, 3);//primera fila de datos del informe interno
    }
//Funcion que genera el excel con el número de incidencias agrupadas por "código de error"

//Funcion que genera el excel con el número de incidencias agrupadas por "código de error"
    public void descargarInformeInterno(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        if (log.isDebugEnabled()) {
            log.debug("descargarInformeInterno() : BEGIN");
        }
        Date mesDesde;
        Date mesHasta;
        SimpleDateFormat sdfFechaMesAnyo = new SimpleDateFormat("MM/yyyy");
        String strFechaDesde;
        String strFechaHasta;
        int numFila = 0;
        final int numColumnasMerge = 3;
        try {
            strFechaDesde = (String) request.getParameter("meLanbide57FechaDesde");
            strFechaHasta = (String) request.getParameter("meLanbide57FechaHasta");
            //se supone que las fechas de inicio y fin son obligatorias
            mesDesde = sdfFechaMesAnyo.parse(strFechaDesde.substring(3));
            mesHasta = sdfFechaMesAnyo.parse(strFechaHasta.substring(3));

            if (log.isDebugEnabled()) {
                log.debug("descargarInformeInterno() : 1");
            }

            HSSFWorkbook libro = new HSSFWorkbook();

            if (log.isDebugEnabled()) {
                log.debug("descargarInformeInterno() : 1.1");
            }

            /**
             * ***********************
             * ESTILOS COMUNES DE CELDAS
            ************************
             */
            if (log.isDebugEnabled()) {
                log.debug("descargarInformeInterno() : 1.2");
            }

            HSSFSheet hoja = hoja = libro.createSheet("INFORME INTERNO");
            if (log.isDebugEnabled()) {
                log.debug("descargarInformeInterno() : 1.3");
            }
            /**
             * *******
             * CABECERAS
            ********
             */
            //MESES (fila 1)
            int contCelda = 0;
            HSSFRow fila = hoja.createRow(numFila++);
            //celda en blanco
            HSSFCell celda = fila.createCell((short) contCelda++);
            celda.setCellValue("REG_ERR_ID");

            //celda total
            celda = fila.createCell((short) contCelda++);
            celda.setCellValue("REG_ERR_FEC_ERROR");

            celda = fila.createCell((short) contCelda++);
            celda.setCellValue("REG_ERR_MEN");

            celda = fila.createCell((short) contCelda++);
            celda.setCellValue("REG_ERR_ID_PROC");

            celda = fila.createCell((short) contCelda++);
            celda.setCellValue("REG_ERR_IDEN_ERR_ID");

            celda = fila.createCell((short) contCelda++);
            celda.setCellValue("REG_ERR_ID_CLAVE");

            celda = fila.createCell((short) contCelda++);
            celda.setCellValue("REG_ERR_NOT");

            celda = fila.createCell((short) contCelda++);
            celda.setCellValue("REG_ERR_REV");

            celda = fila.createCell((short) contCelda++);
            celda.setCellValue("REG_ERR_FEC_REV");

            celda = fila.createCell((short) contCelda++);
            celda.setCellValue("REG_ERR_OBS");

            celda = fila.createCell((short) contCelda++);
            celda.setCellValue("REG_ERR_SIS_ORIG");

            celda = fila.createCell((short) contCelda++);
            celda.setCellValue("REG_ERR_SITU");

            celda = fila.createCell((short) contCelda++);
            celda.setCellValue("REG_ERR_LOG");

            celda = fila.createCell((short) contCelda++);
            celda.setCellValue("REG_ERR_EVEN");

            celda = fila.createCell((short) contCelda++);
            celda.setCellValue("REG_ERR_ID_FLEXIA");

            celda = fila.createCell((short) contCelda++);
            celda.setCellValue("REG_ERR_RETRAMIT_SN");

            celda = fila.createCell((short) contCelda++);
            celda.setCellValue("REG_ERR_FEC_RETRAMIT");

            celda = fila.createCell((short) contCelda++);
            celda.setCellValue("REG_ERR_RESULT_WS");

            if (log.isDebugEnabled()) {
                log.debug("descargarInformeInterno() : 2");
            }

            /**
             * ****
             * DATOS
            *****
             */
            //recupero los datos a mostrar
            AdaptadorSQLBD adapt = null;
            try {
                adapt = this.getAdaptSQLBD_GestionErores();
                // adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            } catch (Exception ex) {
                log.error("Error al recuperar el adaptador getAdaptSQLBD ", ex);
            }
            if (adapt != null) {
                Connection con = null;
                try {
                    con = adapt.getConnection();

                    List<RegistroErrorVO_EXCEL> listaDatosInformeInternoDerivadas = MeLanbide53DAO.getInstance().getListaDatosInformeInternoDerivadas(strFechaDesde, strFechaHasta, con);
                    if (log.isDebugEnabled()) {
                        log.debug("descargarInformeInterno() : 4");
                    }
                    //la lista de datosInformeGeneral está ordenada por unidades pero no por meses y años

                    for (RegistroErrorVO_EXCEL datosInformeInterno : listaDatosInformeInternoDerivadas) {
                        fila = hoja.createRow(numFila++);
                        //muestro la unidad en la primera celda
                        celda = fila.createCell((short) 0);
                        celda.setCellValue(datosInformeInterno.getId());

                        celda = fila.createCell((short) 1);
                        celda.setCellValue(datosInformeInterno.getFechaError());

                        celda = fila.createCell((short) 2);
                        celda.setCellValue(datosInformeInterno.getIdProcedimiento());

                        celda = fila.createCell((short) 3);
                        celda.setCellValue(datosInformeInterno.getIdErrorFK());

                        celda = fila.createCell((short) 4);
                        celda.setCellValue(datosInformeInterno.getClave());

                        celda = fila.createCell((short) 5);
                        celda.setCellValue(datosInformeInterno.getErrorNotificado());

                        celda = fila.createCell((short) 6);
                        celda.setCellValue(datosInformeInterno.getErrorRevisado());

                        celda = fila.createCell((short) 7);
                        celda.setCellValue(datosInformeInterno.getFechaRevisionError());

                        celda = fila.createCell((short) 8);
                        celda.setCellValue(datosInformeInterno.getObservacionesError());

                        celda = fila.createCell((short) 9);
                        celda.setCellValue(datosInformeInterno.getSistemaOrigen());

                        celda = fila.createCell((short) 10);
                        celda.setCellValue(datosInformeInterno.getUbicacionError());

                        celda = fila.createCell((short) 11);
                        celda.setCellValue(datosInformeInterno.getFicheroLog());

                        celda = fila.createCell((short) 12);
                        celda.setCellValue(datosInformeInterno.getEvento());

                        celda = fila.createCell((short) 13);
                        celda.setCellValue(datosInformeInterno.getNumeroExpediente());

                        celda = fila.createCell((short) 14);
                        celda.setCellValue(datosInformeInterno.getUbicacionError());

                        celda = fila.createCell((short) 15);
                        celda.setCellValue(datosInformeInterno.getSolicitudRetramitada());

                        celda = fila.createCell((short) 16);
                        celda.setCellValue(datosInformeInterno.getFechaRetramitacion());

                        celda = fila.createCell((short) 17);
                        celda.setCellValue(datosInformeInterno.getResultadoRetramitacion());

                    }

                } catch (Exception e) {
                    log.error("Error al recuperar tramitesAreas: " + e.getMessage());
                } finally {
                    try {
                        adapt.devolverConexion(con);
                    } catch (Exception e) {
                        log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
                    }
                }
            }
            if (log.isDebugEnabled()) {
                log.debug("descargarInformeInterno() : 6");
            }
            //AUTOAJUSTAR CONTENIDO
            //TODO: mejorar autosize porque deja textos en dos filas..

            /**
             * ***********
             * CREAR FICHERO
            ************
             */
            crearFichero(libro, response);
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando el informe interno ", ex);
        }
        if (log.isDebugEnabled()) {
            log.debug("descargarInformeInterno() : END");
        }
    }

    public void descargarInformeDetalle(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        if (log.isDebugEnabled()) {
            log.debug("descargarInformeInterno() : BEGIN");
        }
        Date mesDesde;
        Date mesHasta;
        SimpleDateFormat sdfFechaMesAnyo = new SimpleDateFormat("MM/yyyy");
        String strFechaDesde;
        String strFechaHasta;
        int numFila = 0;
        final int numColumnasMerge = 3;
        try {
            strFechaDesde = (String) request.getParameter("meLanbide57FechaDesde");
            strFechaHasta = (String) request.getParameter("meLanbide57FechaHasta");
            //se supone que las fechas de inicio y fin son obligatorias
            mesDesde = sdfFechaMesAnyo.parse(strFechaDesde.substring(3));
            mesHasta = sdfFechaMesAnyo.parse(strFechaHasta.substring(3));

            if (log.isDebugEnabled()) {
                log.debug("descargarInformeInterno() : 1");
            }

            HSSFWorkbook libro = new HSSFWorkbook();

            if (log.isDebugEnabled()) {
                log.debug("descargarInformeInterno() : 1.1");
            }

            if (log.isDebugEnabled()) {
                log.debug("descargarInformeInterno() : 1.2");
            }

            HSSFSheet hoja = hoja = libro.createSheet("INFORME INTERNO");
            if (log.isDebugEnabled()) {
                log.debug("descargarInformeInterno() : 1.3");
            }
            /**
             * *******
             * CABECERAS
            ********
             */
            //MESES (fila 1)
            int contCelda = 0;
            HSSFRow fila = hoja.createRow(numFila++);
            //celda en blanco
            HSSFCell celda = fila.createCell((short) contCelda++);
            celda.setCellValue("REG_ERR_IDEN_ERR_ID");

            celda = fila.createCell((short) contCelda++);
            celda.setCellValue("ERROR");

            celda = fila.createCell((short) contCelda++);
            celda.setCellValue("NUMERO");

            if (log.isDebugEnabled()) {
                log.debug("descargarInformeInterno() : 2");
            }

            /**
             * ****
             * DATOS
            *****
             */
            //recupero los datos a mostrar
            AdaptadorSQLBD adapt = null;
            try {
                adapt = this.getAdaptSQLBD_GestionErores();
                // adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            } catch (Exception ex) {
                log.error("Error al recuperar el adaptador getAdaptSQLBD ", ex);
            }
            if (adapt != null) {
                Connection con = null;
                try {
                    con = adapt.getConnection();

                    List<RegistroErrorVO_EXCEL2> listaDatosInformeInternoResueltas = MeLanbide53DAO.getInstance().getListaDatosInformeInternoResueltas(strFechaDesde, strFechaHasta, con);

                    if (log.isDebugEnabled()) {
                        log.debug("descargarInformeInterno() : 4");
                    }
                    //la lista de datosInformeGeneral está ordenada por unidades pero no por meses y años

                    for (RegistroErrorVO_EXCEL2 datosInformeInterno : listaDatosInformeInternoResueltas) {
                        fila = hoja.createRow(numFila++);
                        //muestro la unidad en la primera celda
                        celda = fila.createCell((short) 0);
                        celda.setCellValue(datosInformeInterno.getIdErrorFK());

                        celda = fila.createCell((short) 1);
                        celda.setCellValue(datosInformeInterno.getMensajeError());

                        celda = fila.createCell((short) 2);
                        celda.setCellValue(datosInformeInterno.getNum());

                    }

                } catch (Exception e) {
                    log.error("Error al recuperar tramitesAreas: " + e.getMessage());
                } finally {
                    try {
                        adapt.devolverConexion(con);
                    } catch (Exception e) {
                        log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
                    }
                }
            }
            if (log.isDebugEnabled()) {
                log.debug("descargarInformeInterno() : 6");
            }
            //AUTOAJUSTAR CONTENIDO
            //TODO: mejorar autosize porque deja textos en dos filas..

            /**
             * ***********
             * CREAR FICHERO
            ************
             */
            crearFichero(libro, response);
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando el informe interno ", ex);
        }
        if (log.isDebugEnabled()) {
            log.debug("descargarInformeInterno() : END");
        }
    }

    public String cargarNuevoError(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String nuevo = "0";
        String urlnuevoAcceso = "/jsp/extension/melanbide53/detalleIDError.jsp";
        try {

            String idError = request.getParameter("idError");
            String tipo = request.getParameter("visible");
            // Recuperramos datos de los detalle del error
            if (idError != null && !idError.equals("")) {
                DetalleErrorVO datError = MeLanbide53Manager.getInstance().getDetalleErrorPorID(idError, this.getAdaptSQLBD_GestionErores());
                if (datError != null) {
                    request.setAttribute("datError", datError);
                    request.setAttribute("nuevo", "1");
                }
            }
        } catch (Exception ex) {
            log.error("Error al tratar de preparar los datos para mostrar el detalle de in id error - Gestion de Errores : " + ex.getMessage());
        }
        return urlnuevoAcceso;

    }

    public void retramitarError(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("BEGIN retramitarError ");
        String codigoOperacion = "0";
        String resultado = "";
        ErrorBean resultadoer = new ErrorBean();
        List<DetalleErrorVO> lista = new ArrayList<DetalleErrorVO>();
        AdaptadorSQLBD adapt = this.getAdaptSQLBD_GestionErores();
        try {
            Integer numMaxLIneas = Integer.valueOf(ConfigurationParameter.getParameter(ConstantesMeLanbide53.NUMERO_LINEAS_PAGINA, ConstantesMeLanbide53.FICHERO_PROPIEDADES));
            //Recojo los parametros
            String id = (String) request.getParameter("id");
            //Recojo los parametros 
            RegistroIdenErrorCriteriosFiltroVO _criterioBusqueda = new RegistroIdenErrorCriteriosFiltroVO();
            // _criterioBusqueda.setErrorRevisado(ConstantesMeLanbide53.BOOLEAN_FALSE);
            _criterioBusqueda.setNumeroInicialLinea(0);
            _criterioBusqueda.setNumeroFinalLinea(numMaxLIneas);
            _criterioBusqueda.setNumeroLineasxPagina(numMaxLIneas);
            lista = MeLanbide53Manager.getInstance().busquedaIdenErrores(_criterioBusqueda, adapt);

            if (id == null || id.equals("")) {
                log.error("No se ha recibido desde la JSP el id del un Error para su gestion - Modificar");
                codigoOperacion = "3";
            } else {
                //Retramitar.retramitaSolicitud(id)

                resultado = MeLanbide53Manager.getInstance().retramitarSolicitud(id, this.getAdaptSQLBD_GestionErores());
                //resultado = resultadoer.getSituacion()+" "+resultadoer.getIdError();
                //Retramitacion.retramitaSolicitud(id);

            }
        } catch (Exception ex) {
            codigoOperacion = "2";
        }

        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        //Nuevo
        xmlSalida.append("<DES_ERROR>");
        xmlSalida.append(resultado);
        xmlSalida.append("</DES_ERROR>");
        MeLanbide53Utilities meLanbide53Utilities = MeLanbide53Utilities.getInstance();
        for (DetalleErrorVO fila : lista) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<IDEN_ERR_ID>");
            xmlSalida.append(fila.getId() != null ? fila.getId().toString() : "");
            xmlSalida.append("</IDEN_ERR_ID>");
            xmlSalida.append("<IDEN_ERR_DESC_C>");
            xmlSalida.append(fila.getDescripcionCorta() != null ? meLanbide53Utilities.escapeSpecialCharacterXML(fila.getDescripcionCorta()) : "");
            xmlSalida.append("</IDEN_ERR_DESC_C>");
            xmlSalida.append("<IDEN_ERR_DESC>");
            xmlSalida.append(fila.getDescripcion() != null ? meLanbide53Utilities.escapeSpecialCharacterXML(fila.getDescripcion()) : "");
            xmlSalida.append("</IDEN_ERR_DESC>");
            xmlSalida.append("<IDEN_ERR_TIPO>");
            xmlSalida.append(fila.getTipo());
            xmlSalida.append("</IDEN_ERR_TIPO>");
            xmlSalida.append("<IDEN_ERR_CRIT>");
            xmlSalida.append(fila.getCritico());
            xmlSalida.append("</IDEN_ERR_CRIT>");
            xmlSalida.append("<DES_TIPO>");
            xmlSalida.append(fila.getDesTipo());
            xmlSalida.append("</DES_TIPO>");
            xmlSalida.append("<DES_CRITICO>");
            xmlSalida.append(fila.getDesCritico());
            xmlSalida.append("</DES_CRITICO>");
            xmlSalida.append("</FILA>");
        }
        xmlSalida.append("</RESPUESTA>");

        try {
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
            log.debug("END retramitarError ");
        } catch (Exception e) {
            log.error("Error al modificar gestionando errores", e);
            e.printStackTrace();
        }//try-catch
    }
    
    public void oidEsOK(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("BEGIN oidEsOK ");
        String codigoOperacion = "0";
        int oidOK = 0;
        try {
            String oidDocumento = (String) request.getParameter("oidDocumento");

            if (oidDocumento == null || oidDocumento.equals("")) {
                log.error("No se ha recibido desde la JSP el oid para retramitar");
                codigoOperacion = "3";
            } else {
                oidOK = MeLanbide53Manager.getInstance().oidEsOK(oidDocumento, this.getAdaptSQLBD_GestionErores());
            }
        } catch (Exception ex) {
            codigoOperacion = "2";
        }
        

        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
     
        xmlSalida.append("<ESOK>");
        xmlSalida.append(oidOK);
        xmlSalida.append("</ESOK>");
        
        xmlSalida.append("</RESPUESTA>");

        try {
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
            log.debug("END oidEsOK ");
        } catch (Exception e) {
            log.error("Error al consultar si oid es OK", e);
            e.printStackTrace();
        }//try-catch
    }
    
    public void getRegistroDeOid(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("BEGIN getRegistroDeOid ");
        String codigoOperacion = "0";
        String registro = "";
        String codOrganizacionReal = ConfigurationParameter.getParameter(ConstantesMeLanbide53.COD_ORGANIZACION, ConstantesMeLanbide53.FICHERO_PROPIEDADES);
        AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacionReal));
        try {
            String oidDocumento = (String) request.getParameter("oidDocumento");

            if (oidDocumento == null || oidDocumento.equals("")) {
                log.error("No se ha recibido desde la JSP el oid para retramitar");
                codigoOperacion = "3";
            } else {
                registro = MeLanbide53Manager.getInstance().getRegistroDeOid(oidDocumento, adapt);
            }
        } catch (Exception ex) {
            codigoOperacion = "2";
        }
        

        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
     
        xmlSalida.append("<REGISTRO>");
        xmlSalida.append(registro);
        xmlSalida.append("</REGISTRO>");
        
        xmlSalida.append("</RESPUESTA>");

        try {
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
            log.debug("END getRegistroDeOid ");
        } catch (Exception e) {
            log.error("Error al consultar si oid está en registro", e);
            e.printStackTrace();
        }//try-catch
    }
    
    public void retramitarEstadisticas(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("BEGIN retramitarEstadísticas ");
        String codigoOperacion = "0";
        String resultado = "";
        try {
            String oid = (String) request.getParameter("oid");
            String id = (String) request.getParameter("id");

            if (id == null || id.equals("")) {
                log.error("No se ha recibido desde la JSP el oid para retramitar");
                codigoOperacion = "3";
            } else {
                resultado = MeLanbide53Manager.getInstance().retramitaSolicitudEstadisticas(oid, id, this.getAdaptSQLBD_GestionErores());
            }
        } catch (Exception ex) {
            codigoOperacion = "2";
        }
        

        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
     
        xmlSalida.append("<DES_ERROR>");
        xmlSalida.append(resultado);
        xmlSalida.append("</DES_ERROR>");
        
        xmlSalida.append("</RESPUESTA>");

        try {
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
            log.debug("END retramitarEstadistica ");
        } catch (Exception e) {
            log.error("Error al retramitar Estadistica", e);
            e.printStackTrace();
        }//try-catch
    }

//#292920   
    public String cargarPantallaEstadisticas(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("Entramos en cargarPantallaEstadisticas de " + this.getClass().getName());
        AdaptadorSQLBD adapt = null;
        MeLanbide53Manager meLanbide53Manager = new MeLanbide53Manager();
        try {
            adapt = this.getAdaptSQLBD_GestionErores();
        } catch (Exception ex) {
            log.error(this.getClass().getName() + " Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }
        String url = "/jsp/extension/melanbide53/estadisticas.jsp";

        if (adapt != null) {
            log.error("adapt != null");
            try {
                List<EstadisticasVO> listaEstadisticas = MeLanbide53Manager.getInstance().getDatosEstadisticas(adapt);
                if (listaEstadisticas.size() > 0) {
                    request.setAttribute("listaEstadisticas", listaEstadisticas);
                    /*
                    List<EventoVO> listaEventos = MeLanbide53Manager.getInstance().getEventos(adapt);
                    if (listaEventos.size() > 0) {
                            request.setAttribute("listaEvento", listaEventos);
                    }     
                     */
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                log.error("Error al recueperar los datos De Acceso - MELANBIDE53 - cargarPantallaEstadisticas", ex);
            }
        } else {
            log.error("adapt == null");
        }
        return url;
    }

    public String cargarConsultarEstadistica(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.debug("Entramos en cargarConsultarEstadistica de " + this.getClass().getName());
        AdaptadorSQLBD adapt = null;
        MeLanbide53Manager meLanbide53Manager = new MeLanbide53Manager();
        try {
            adapt = this.getAdaptSQLBD_GestionErores();
        } catch (Exception ex) {
            log.error(this.getClass().getName() + " Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }
        String urlAcceso = "/jsp/extension/melanbide53/detalleIDEstadistica.jsp";
        if (adapt != null) {
            log.error("adapt != null");
            try {
                String idEstadistica = request.getParameter("idEstadistica");
                int idEstadisticaInt = 0;
                if (idEstadistica != null && !idEstadistica.equals("")) {
                    idEstadisticaInt = Integer.parseInt(idEstadistica);
                    // Recuperramos datos de los detalle de la estadistica
                    DetalleEstadisticaVO datEstadistica = MeLanbide53Manager.getInstance().getDetalleEstadisticaPorID(idEstadisticaInt, adapt);
                    if (datEstadistica.getId() != null) {
                        request.setAttribute("datEstadistica", datEstadistica);
                    } else {
                        // Recuperramos datos de los detalle de la estadistica_hist
                        DetalleEstadisticaVO datEstadisticaHist = MeLanbide53Manager.getInstance().getDetalleEstadisticaHistPorID(idEstadisticaInt, adapt);
                        if (datEstadisticaHist.getId() != null) {
                            request.setAttribute("datEstadistica", datEstadisticaHist);
                        }
                    }
                }
            } catch (Exception ex) {
                log.error("Error al tratar de preparar los datos para mostrar el detalle de una estadistica - Gestion de Estadisticas : " + ex.getMessage());
            }
        } else {
            log.error("adapt == null");
        }
        return urlAcceso;

    }

    public void cargarFiltrarEstadisticas(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String codigoOperacion = "0";
        List<EstadisticasVO> listaEstadisticas = new ArrayList<EstadisticasVO>();
        AdaptadorSQLBD adapt = this.getAdaptSQLBD_GestionErores();
        try {
            //Recojo los parametros
            String strFechaDesde = (String) request.getParameter("Fecha_RegbusqE");
            String strFechaHasta = (String) request.getParameter("Fecha_RegbusqF");
            if (strFechaDesde == "") {
                strFechaDesde = "01/01/0001";
            }
            if (strFechaHasta == "") {
                Date date = new Date();
                // Obtenemos solamente la fecha pero usamos slash en lugar de guiones
                DateFormat fecha = new SimpleDateFormat("dd/MM/yyyy");
                strFechaHasta = fecha.format(date);
            }
            String ck_historico = (String) request.getParameter("ck_historico");
            String numeroExpediente = (String) request.getParameter("numeroExpediente");
            String idprocedimiento = (String) request.getParameter("idprocedimiento");
            String clave = (String) request.getParameter("clave");
            String codResultado = (String) request.getParameter("codResultado");
            String error = (String) request.getParameter("error");
            String evento = (String) request.getParameter("evento");

            FiltroEstadisticasVO filtroEstad = new FiltroEstadisticasVO();
            filtroEstad.setFechaDesde(strFechaDesde);
            filtroEstad.setFechaHasta(strFechaHasta);
            filtroEstad.setHistorico(ck_historico);
            filtroEstad.setIdProcedimiento(idprocedimiento);
            filtroEstad.setNumeroExpediente(numeroExpediente);
            filtroEstad.setClave(clave);
            filtroEstad.setResultado(codResultado);
            filtroEstad.setIdError(error);
            filtroEstad.setEvento(evento);

            try {
                listaEstadisticas = MeLanbide53Manager.getInstance().getFiltroEstadisticas(filtroEstad, adapt);

            } catch (Exception ex) {
                codigoOperacion = "2";
                log.error("Error al recuperar la lista de estadisticas : " + ex.getMessage());
            }

        } catch (Exception ex) {
            codigoOperacion = "2";
        }

        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        MeLanbide53Utilities meLanbide53Utilities = MeLanbide53Utilities.getInstance();
        for (EstadisticasVO fila : listaEstadisticas) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append(fila.getId());
            xmlSalida.append("</ID>");
            xmlSalida.append("<FEC_REGISTRO>");
            xmlSalida.append(fila.getFechaRegistro());
            xmlSalida.append("</FEC_REGISTRO>");
            xmlSalida.append("<ID_PROCEDIMIENTO>");
            xmlSalida.append(fila.getIdProcedimiento());
            xmlSalida.append("</ID_PROCEDIMIENTO>");
            xmlSalida.append("<NUM_EXPEDIENTE>");
            xmlSalida.append(fila.getNumeroExpediente() != null ? meLanbide53Utilities.escapeSpecialCharacterXML(fila.getNumeroExpediente()) : "");
            xmlSalida.append("</NUM_EXPEDIENTE>");
            xmlSalida.append("<OID_SOLICITUD>");
            xmlSalida.append(fila.getClave() != null ? meLanbide53Utilities.escapeSpecialCharacterXML(fila.getClave()) : "");
            xmlSalida.append("</OID_SOLICITUD>");
            xmlSalida.append("<RESULTADO>");
            xmlSalida.append(fila.getResultado());
            xmlSalida.append("</RESULTADO>");
            xmlSalida.append("<ID_ERROR>");
            xmlSalida.append(fila.getIdError() != null ? meLanbide53Utilities.escapeSpecialCharacterXML(fila.getIdError()) : "");
            xmlSalida.append("</ID_ERROR>");
            xmlSalida.append("<EVENTO>");
            xmlSalida.append(fila.getEvento() != null ? meLanbide53Utilities.escapeSpecialCharacterXML(fila.getEvento()) : "");
            xmlSalida.append("</EVENTO>");
            xmlSalida.append("<OBSERVACIONES>");
            xmlSalida.append(fila.getObservaciones() != null ? meLanbide53Utilities.escapeSpecialCharacterXML(fila.getObservaciones()) : "");
            xmlSalida.append("</OBSERVACIONES>");
            xmlSalida.append("</FILA>");
        }
        xmlSalida.append("</RESPUESTA>");
        try {
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        } catch (Exception e) {
            log.error("Error al filtrar estadisticas", e);
            e.printStackTrace();
        }//try-catch    
    }

    public void generarExcel(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        if (log.isDebugEnabled()) {
            log.debug("generarExcel() : BEGIN");
        }
        SimpleDateFormat sdfFechaMesAnyo = new SimpleDateFormat("MM/yyyy");
        String strFechaDesde;
        String strFechaHasta;
        int numFila = 0;
        final int numColumnasMerge = 3;
        try {
            strFechaDesde = (String) request.getParameter("Fecha_RegbusqE");
            strFechaHasta = (String) request.getParameter("Fecha_RegbusqF");
            if (strFechaDesde == "") {
                strFechaDesde = "01/01/1900";
            }
            if (strFechaHasta == "") {
                /*
                Date date = new Date();
		// Obtenemos solamente la fecha pero usamos slash en lugar de guiones
		DateFormat fecha = new SimpleDateFormat("dd/MM/yyyy");
		strFechaHasta = fecha.format(date);               
                 */
                strFechaHasta = "31/12/2099";
            }

            String ck_historico = (String) request.getParameter("ck_historico");
            String numeroExpediente = (String) request.getParameter("numeroExpediente");
            String idprocedimiento = (String) request.getParameter("idprocedimiento");
            String clave = (String) request.getParameter("clave");
            String codResultado = (String) request.getParameter("codResultado");
            String error = (String) request.getParameter("error");
            String evento = (String) request.getParameter("evento");

            FiltroEstadisticasVO filtroEstad = new FiltroEstadisticasVO();
            filtroEstad.setFechaDesde(strFechaDesde);
            filtroEstad.setFechaHasta(strFechaHasta);
            filtroEstad.setHistorico(ck_historico);
            filtroEstad.setIdProcedimiento(idprocedimiento);
            filtroEstad.setNumeroExpediente(numeroExpediente);
            filtroEstad.setClave(clave);
            filtroEstad.setResultado(codResultado);
            filtroEstad.setIdError(error);
            filtroEstad.setEvento(evento);

            if (log.isDebugEnabled()) {
                log.debug("generarExcel() : 1");
            }

            HSSFWorkbook libro = new HSSFWorkbook();

            if (log.isDebugEnabled()) {
                log.debug("generarExcel() : 1.1");
            }

            if (log.isDebugEnabled()) {
                log.debug("generarExcel() : 1.2");
            }

            HSSFSheet hoja = hoja = libro.createSheet("INFORME");
            if (log.isDebugEnabled()) {
                log.debug("generarExcel() : 1.3");
            }
            /**
             * *******
             * CABECERAS
            ********
             */
            //MESES (fila 1)
            int contCelda = 0;
            HSSFRow fila = hoja.createRow(numFila++);
            //celda en blanco
            HSSFCell celda = fila.createCell((short) contCelda++);
            celda.setCellValue("ID");

            celda = fila.createCell((short) contCelda++);
            celda.setCellValue("FEC_REGISTRO");

            celda = fila.createCell((short) contCelda++);
            celda.setCellValue("ID_PROCEDIMIENTO");

            celda = fila.createCell((short) contCelda++);
            celda.setCellValue("NUM_EXPEDIENTE");

            celda = fila.createCell((short) contCelda++);
            celda.setCellValue("OID_SOLICITUD");

            celda = fila.createCell((short) contCelda++);
            celda.setCellValue("RESULTADO");

            celda = fila.createCell((short) contCelda++);
            celda.setCellValue("ID_ERROR");

            celda = fila.createCell((short) contCelda++);
            celda.setCellValue("EVENTO");

            celda = fila.createCell((short) contCelda++);
            celda.setCellValue("OBSERVACIONES");

            if (log.isDebugEnabled()) {
                log.debug("generarExcel() : 2");
            }
            /**
             * ****
             * DATOS
            *****
             */
            //recupero los datos a mostrar
            AdaptadorSQLBD adapt = null;
            try {
                adapt = this.getAdaptSQLBD_GestionErores();
            } catch (Exception ex) {
                log.error("Error al recuperar el adaptador getAdaptSQLBD ", ex);
            }
            if (adapt != null) {
                Connection con = null;
                try {
                    con = adapt.getConnection();

                    List<EstadisticasVO_EXCEL> listaDatosInformeEstadisticas = MeLanbide53DAO.getInstance().getListaDatosInformeEstadisticas(filtroEstad, con);

                    if (log.isDebugEnabled()) {
                        log.debug("generarExcel() : 4");
                    }

                    for (EstadisticasVO_EXCEL datosInformeEstadisticas : listaDatosInformeEstadisticas) {
                        fila = hoja.createRow(numFila++);
                        //muestro la unidad en la primera celda
                        celda = fila.createCell((short) 0);
                        celda.setCellValue(datosInformeEstadisticas.getId());

                        celda = fila.createCell((short) 1);
                        celda.setCellValue(datosInformeEstadisticas.getFechaRegistro());

                        celda = fila.createCell((short) 2);
                        celda.setCellValue(datosInformeEstadisticas.getIdProcedimiento());

                        celda = fila.createCell((short) 3);
                        celda.setCellValue(datosInformeEstadisticas.getNumeroExpediente());

                        celda = fila.createCell((short) 4);
                        celda.setCellValue(datosInformeEstadisticas.getClave());

                        celda = fila.createCell((short) 5);
                        celda.setCellValue(datosInformeEstadisticas.getResultado());

                        celda = fila.createCell((short) 6);
                        celda.setCellValue(datosInformeEstadisticas.getIdError());

                        celda = fila.createCell((short) 7);
                        celda.setCellValue(datosInformeEstadisticas.getEvento());

                        celda = fila.createCell((short) 8);
                        celda.setCellValue(datosInformeEstadisticas.getObservaciones());

                    }
                } catch (Exception e) {
                    log.error("Error al recuperar estadisticas: " + e.getMessage());
                } finally {
                    try {
                        adapt.devolverConexion(con);
                    } catch (Exception e) {
                        log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
                    }
                }
            }
            if (log.isDebugEnabled()) {
                log.debug("generarExcel() : 6");
            }
            //AUTOAJUSTAR CONTENIDO
            //TODO: mejorar autosize porque deja textos en dos filas..

            /**
             * ***********
             * CREAR FICHERO
            ************
             */
            crearFichero(libro, response);
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando el informe de estadisticas ", ex);
        }
        if (log.isDebugEnabled()) {
            log.debug("generarExcel() : END");
        }
    }
}
