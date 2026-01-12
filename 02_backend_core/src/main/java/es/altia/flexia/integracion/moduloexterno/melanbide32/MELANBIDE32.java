/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide32;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.melanbide32.i18n.MeLanbide32I18n;
import es.altia.flexia.integracion.moduloexterno.melanbide32.manager.MeLanbide32Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide32.manager.MeLanbide32ManagerCriteriosSeleccionCE;
import es.altia.flexia.integracion.moduloexterno.melanbide32.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide32.util.ConstantesMeLanbide32;
import es.altia.flexia.integracion.moduloexterno.melanbide32.util.MeLanbide32Utils;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.centroempleo.CeTrayectoriaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.centroempleo.CeUbicacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.centroempleo.FilaTrayectoriaCentroEmpleoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.centroempleo.FilaUbicCentroEmpleoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.centroempleo.AmbitoCentroEmpleoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.centroempleo.AmbitoSolicitadoCempVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.centroempleo.FilaAmbitoSolicitadoCempVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.comun.EntidadVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.comun.EspecialidadVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.comun.MunicipioVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.comun.ProvinciaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.comun.SelectItem;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.centroempleo.UbicacionCentroEmpleoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.orientacion.UbicacionHorasVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.comun.AuditoriaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.comun.TerceroVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.comun.DomicilioVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.comun.ViaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.comun.TipoViaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.orientacion.AmbitoHorasVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.orientacion.FilaTrayectoriaOrientacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.orientacion.FilaUbicOrientacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.orientacion.OriTrayectoriaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.orientacion.OriUbicValoracionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.orientacion.OriUbicacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.procesos.FilaAuditoriaProcesosVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.reports.Subreport;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.reports.FilaUbicacionReportVO;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;
import es.altia.technical.PortableContext;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 *
 * @author santiagoc
 */
public class MELANBIDE32 extends ModuloIntegracionExterno {

    //Logger
    private static Logger log = LogManager.getLogger(MELANBIDE32.class);
    ResourceBundle m_Conf = ResourceBundle.getBundle("common");
    MELANBIDE32CriteriosEvaActionController mELANBIDE32CriteriosEvaActionController = new MELANBIDE32CriteriosEvaActionController();
    MeLanbide32ManagerCriteriosSeleccionCE meLanbide32ManagerCriteriosSeleccionCE = new MeLanbide32ManagerCriteriosSeleccionCE();

    // Alta Expedientes via registro platea --> MELANBIDE 42
    public void cargarExpedienteExtension(int codigoOrganizacion, String numeroExpediente, String xml) throws Exception {
        final Class cls = Class.forName("es.altia.flexia.integracion.moduloexterno.melanbide42.MELANBIDE42");
        final Object me42Class = cls.newInstance();
        final Class[] types = {int.class, String.class, String.class};
        final Method method = cls.getMethod("cargarExpedienteExtension", types);
        method.invoke(me42Class, codigoOrganizacion, numeroExpediente, xml);
    }

    
    public String cargarDatosORI(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
        String ejercicio = "";
        try
        {            
            String[] datos = numExpediente.split(ConstantesMeLanbide32.BARRA_SEPARADORA);
            ejercicio = datos[0];
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            List<FilaUbicOrientacionVO> listaUbicOrientacion = MeLanbide32Manager.getInstance().getUbicacionesORI(codOrganizacion, ejercicio, numExpediente, adapt);
            request.setAttribute("listaUbicOrientacion", listaUbicOrientacion);

            EntidadVO entidad = MeLanbide32Manager.getInstance().getEntidad(codOrganizacion, numExpediente, ejercicio, adapt);
            if(entidad != null && entidad.getOriEntCod() != null)
            {
                List<FilaTrayectoriaOrientacionVO> trayectorias = MeLanbide32Manager.getInstance().getTrayectoriasORI(entidad.getOriEntCod(), adapt);
                request.setAttribute("listaTrayectoriaOrientacion", trayectorias);
                request.setAttribute("entidad", entidad);
            }
        }
        catch(Exception ex)
        {
            log.debug("No se ha podido obtener el ejercicio al que pertenece el expediente");
        }
        return "/jsp/extension/melanbide32/orientacion/datosEspecificosORI.jsp";
    }

    public String cargarNuevaSolicitudORI(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            cargarCombosNuevaUbicORI(codOrganizacion, request);
            request.setAttribute("nuevo", "1");
        }
        catch(Exception ex)
        {

        }
        return "/jsp/extension/melanbide32/orientacion/nuevaSolicitudORI.jsp?codOrganizacionModulo="+codOrganizacion;
    }

    public String cargarModifSolicitudORI(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            cargarCombosNuevaUbicORI(codOrganizacion, request);
            request.setAttribute("nuevo", "0");
            String codUbic = request.getParameter("idUbic");
            if(codUbic != null && !codUbic.equals(""))
            {
                OriUbicacionVO ubic = MeLanbide32Manager.getInstance().getUbicacionORIPorCodigo(codUbic, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if(ubic != null && ubic.getOriAmbCod() != null)
                {
                    AmbitoHorasVO amb = MeLanbide32Manager.getInstance().getAmbitoHorasPorCodigo(ubic.getOriAmbCod(), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    if(amb != null)
                    {
                        ubic.setMunPrv(amb.getOriAmbTerHis());
                        request.setAttribute("ubicModif", ubic);
                    }
                }
            }
        }
        catch(Exception ex)
        {

        }
        return "/jsp/extension/melanbide32/orientacion/nuevaSolicitudORI.jsp?codOrganizacionModulo="+codOrganizacion;
    }

    public String cargarValorarSolicitudORI(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            String codUbic = request.getParameter("idUbic");
            if(codUbic != null && !codUbic.equals(""))
            {
                OriUbicValoracionVO ubicVal = new OriUbicValoracionVO();

                AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));

                OriUbicacionVO ubic = MeLanbide32Manager.getInstance().getUbicacionORIPorCodigo(codUbic, adapt);
                if(ubic != null)
                {
                    //Id
                    ubicVal.setOriOrientUbicCod(ubic.getOriOrientUbicCod());
                    //Provincia
                    if(ubic.getMunPrv() != null)
                    {
                        try
                        {
                            ProvinciaVO prov = MeLanbide32Manager.getInstance().getProvinciaPorCodigo(ubic.getMunPrv(), adapt);
                            if (prov != null) {
                                ubicVal.setDescProvincia(prov.getPrvNom() != null ? prov.getPrvNom() : "");
                            }
                        } catch (Exception ex) {

                        }

                        //Municipio
                        if (ubic.getMunCod() != null) {
                            try {
                                MunicipioVO mun = MeLanbide32Manager.getInstance().getMunicipioPorCodigoYProvincia(ubic.getMunCod(), ubic.getMunPrv(), adapt);
                                if (mun != null) {
                                    ubicVal.setDescMunicipio(mun.getMunNom() != null ? mun.getMunNom() : "");
                                }
                            } catch (Exception ex) {

                            }
                        }
                    }

                    //Ambito
                    if (ubic.getOriAmbCod() != null) {
                        try {
                            AmbitoHorasVO amb = MeLanbide32Manager.getInstance().getAmbitoHorasPorCodigo(ubic.getOriAmbCod(), adapt);
                            if (amb != null) {
                                ubicVal.setDescAmbito(amb.getOriAmbAmbito() != null ? amb.getOriAmbAmbito() : "");
                            }
                        } catch (Exception ex) {

                        }
                    }

                    //Direccion
                    ubicVal.setDireccion(ubic.getOriOrientDireccion() != null ? ubic.getOriOrientDireccion() : "");

                    //Horas Solicitadas
                    ubicVal.setHorasSolic(ubic.getOriOrientHorasSolicitadas() != null ? ubic.getOriOrientHorasSolicitadas().toString() : "");

                    //Trayectoria solicitud/validado
                    if (ubic.getOriEntCod() != null) {
                        EntidadVO ent = MeLanbide32Manager.getInstance().getEntidadPorCodigo(ubic.getOriEntCod(), adapt);
                        if (ent != null) {
                            ubicVal.setOriEntTrayectoria(ent.getOriEntTrayectoria());
                            ubicVal.setOriEntTrayectoriaVal(ent.getOriEntTrayectoriaVal());
                        }
                    }

                    //Ubicacion solicitud/validado
                    if (ubic.getOriOrientUbicCod() != null) {
                        UbicacionHorasVO ubicVO = MeLanbide32Manager.getInstance().getUbicacion(ubic, adapt);
                        if (ubicVO != null) {
                            ubicVal.setOriUbicPuntuacion(ubicVO.getOriUbicPuntuacion());
                            ubicVal.setOriUbicPuntuacionVal(ubicVO.getOriUbicPuntuacion());
                        }
                    }

                    //Numero despachos solicitud/validado
                    ubicVal.setOriOrientDespachos(ubic.getOriOrientDespachos());
                    ubicVal.setOriOrientDespachosValidados(ubic.getOriOrientDespachosValidados());

                    //Aula grupal solicitud/validado
                    ubicVal.setOriOrientAulagrupal(ubic.getOriOrientAulagrupal() != null ? ubic.getOriOrientAulagrupal() : "");
                    ubicVal.setOriOrientAulaGrupalValidada(ubic.getOriOrientAulaGrupalValidada() != null ? ubic.getOriOrientAulaGrupalValidada() : "");

                    //Trayectoria valoracion
                    ubicVal.setOriOrientValTray(ubic.getOriOrientValTray());

                    //Ubicacion valoracion
                    ubicVal.setOriOrientValUbic(ubic.getOriOrientValUbic());

                    //Despachos extra valoracion
                    ubicVal.setOriOrientValDespachos(ubic.getOriOrientValDespachos());

                    //Aulas extra valoracion
                    ubicVal.setOriOrientValAulas(ubic.getOriOrientValAulas());

                    //TOTAL
                    ubicVal.setOriOrientPuntuacion(ubic.getOriOrientPuntuacion());

                    //Observaciones
                    ubicVal.setOriOrientObservaciones(ubic.getOriOrientObservaciones() != null ? ubic.getOriOrientObservaciones() : "");
                }
                request.setAttribute("ubicVal", ubicVal);
            }
        } catch (Exception ex) {

        }
        return "/jsp/extension/melanbide32/orientacion/valorarSolicitudORI.jsp?codOrganizacionModulo=" + codOrganizacion;
    }

    public void crearSolicitudORI(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<FilaUbicOrientacionVO> ubicaciones = new ArrayList<FilaUbicOrientacionVO>();
        try {
            //Recojo los parametros
            String provincia = (String) request.getParameter("provincia");
            String ambito = (String) request.getParameter("ambito");
            String municipio = (String) request.getParameter("municipio");
            String direccion = (String) request.getParameter("direccion");
            String horas = (String) request.getParameter("horas");
            String despachos = (String) request.getParameter("despachos");
            String segundaAula = (String) request.getParameter("segundaAula");

            String ejercicio = "";
            try {
                String[] datos = numExpediente.split(ConstantesMeLanbide32.BARRA_SEPARADORA);
                ejercicio = datos[0];
            } catch (Exception ex) {
                log.debug("No se ha podido obtener el ejercicio al que pertenece el expediente");
            }
            EntidadVO entidad = MeLanbide32Manager.getInstance().getEntidad(codOrganizacion, numExpediente, ejercicio, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            if (entidad == null) {
                try {
                    //Si no existe la entidad, hay que crearla lo primero
                    HashMap<String, String> map = MeLanbide32Manager.getInstance().getDatosTercero(codOrganizacion, numExpediente, ejercicio, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    entidad = new EntidadVO();
                    entidad.setExtEje(Integer.parseInt(ejercicio));
                    entidad.setExtMun(codOrganizacion);
                    entidad.setExtNum(numExpediente);
                    try {
                        entidad.setExtNvr(Integer.parseInt(map.get("EXT_NVR")));
                        entidad.setExtTer(Long.parseLong(map.get("EXT_TER")));
                    } catch (Exception ex) {

                    }
                    entidad = MeLanbide32Manager.getInstance().crearEntidad(entidad, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                } catch (Exception ex) {
                    codigoOperacion = "4";
                    entidad = null;
                }
            }
            if (entidad != null) {
                //Si ya existia la entidad o se ha creado correctamente, se procede a guardar la ubicacion
                OriUbicacionVO ubicVO = new OriUbicacionVO();
                ubicVO.setOriOrientAno(Integer.parseInt(ejercicio));
                ubicVO.setOriEntCod(entidad.getOriEntCod());
                ubicVO.setOriAmbCod(Integer.parseInt(ambito));
                ubicVO.setMunPai(Integer.parseInt(ConstantesMeLanbide32.CODIGO_PAIS_ESPANA));
                ubicVO.setMunPrv(Integer.parseInt(provincia));
                ubicVO.setMunCod(Integer.parseInt(municipio));
                ubicVO.setOriOrientDireccion(direccion);
                ubicVO.setOriOrientHorasSolicitadas(new BigDecimal(horas));
                ubicVO.setOriOrientDespachos(Integer.parseInt(despachos));
                ubicVO.setOriOrientAulagrupal(segundaAula != null && !segundaAula.equals("") ? segundaAula : ConstantesMeLanbide32.NO);
                ubicVO.setOriOrientValTray(entidad.getOriEntTrayectoriaVal() != null ? entidad.getOriEntTrayectoriaVal() - 2 : null);
                ubicVO.setPrvPai(Integer.parseInt(ConstantesMeLanbide32.CODIGO_PAIS_ESPANA));
                BigDecimal total = this.calcularValoracion(entidad.getOriEntTrayectoriaVal(), ubicVO.getOriOrientValUbic(), ubicVO.getOriOrientDespachosValidados(), ubicVO.getOriOrientAulaGrupalValidada());
                if (total != null) {
                    ubicVO.setOriOrientPuntuacion(total);
                }
                ubicVO = MeLanbide32Manager.getInstance().crearUbicacionORI(ubicVO, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));

                if (ubicVO != null) {
                    //Si ubicVO != null entonces es que se ha guardado bien
                    try {
                        ubicaciones = MeLanbide32Manager.getInstance().getUbicacionesORI(codOrganizacion, ejercicio, numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    } catch (BDException bde) {
                        codigoOperacion = "1";
                        log.error("Error : " + bde.getMessage(), bde);
                    } catch (Exception ex) {
                        codigoOperacion = "2";
                        log.error("Error : " + ex.getMessage(), ex);
                    }
                } else {
                    codigoOperacion = "2";
                }
            } else if (codigoOperacion.equalsIgnoreCase("0")) {
                codigoOperacion = "3";
            }
        } catch (Exception ex) {
            codigoOperacion = "2";
        }

        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        for (FilaUbicOrientacionVO fila : ubicaciones) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append(fila.getCodigoUbic() != null ? fila.getCodigoUbic().toString() : "");
            xmlSalida.append("</ID>");
            xmlSalida.append("<PROVINCIA>");
            xmlSalida.append(fila.getProvincia());
            xmlSalida.append("</PROVINCIA>");
            xmlSalida.append("<AMBITO>");
            xmlSalida.append(fila.getAmbito());
            xmlSalida.append("</AMBITO>");
            xmlSalida.append("<MUNICIPIO>");
            xmlSalida.append(fila.getMunicipio());
            xmlSalida.append("</MUNICIPIO>");
            xmlSalida.append("<DIRECCION>");
            xmlSalida.append(fila.getDireccion());
            xmlSalida.append("</DIRECCION>");
            xmlSalida.append("<HORAS>");
            xmlSalida.append(fila.getHoras());
            xmlSalida.append("</HORAS>");
            xmlSalida.append("<DESPACHOS>");
            xmlSalida.append(fila.getDespachos());
            xmlSalida.append("</DESPACHOS>");
            xmlSalida.append("<AULAGRUPAL>");
            xmlSalida.append(fila.getAulaGrupal());
            xmlSalida.append("</AULAGRUPAL>");
            xmlSalida.append("<VALORACION>");
            xmlSalida.append(fila.getValoracion());
            xmlSalida.append("</VALORACION>");
            xmlSalida.append("<TOTAL>");
            xmlSalida.append(fila.getTotal());
            xmlSalida.append("</TOTAL>");
            xmlSalida.append("<HORASADJ>");
            xmlSalida.append(fila.getHorasAdj());
            xmlSalida.append("</HORASADJ>");
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
            e.printStackTrace();
        }//try-catch
    }

    public void modificarSolicitudORI(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<FilaUbicOrientacionVO> ubicaciones = new ArrayList<FilaUbicOrientacionVO>();
        try {
            //Recojo los parametros
            String provincia = (String) request.getParameter("provincia");
            String ambito = (String) request.getParameter("ambito");
            String municipio = (String) request.getParameter("municipio");
            String direccion = (String) request.getParameter("direccion");
            String horas = (String) request.getParameter("horas");
            String despachos = (String) request.getParameter("despachos");
            String segundaAula = (String) request.getParameter("segundaAula");
            String idUbic = (String) request.getParameter("idUbic");

            String ejercicio = "";
            try {
                String[] datos = numExpediente.split(ConstantesMeLanbide32.BARRA_SEPARADORA);
                ejercicio = datos[0];
            } catch (Exception ex) {
                log.debug("No se ha podido obtener el ejercicio al que pertenece el expediente");
            }

            OriUbicacionVO ubicVO = MeLanbide32Manager.getInstance().getUbicacionORIPorCodigo(idUbic, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            if (ubicVO != null && ubicVO.getOriEntCod() != null) {
                EntidadVO entidad = MeLanbide32Manager.getInstance().getEntidadPorCodigo(ubicVO.getOriEntCod(), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (entidad != null) {
                    ubicVO.setOriOrientAno(Integer.parseInt(ejercicio));
                    ubicVO.setOriEntCod(entidad.getOriEntCod());
                    ubicVO.setOriAmbCod(Integer.parseInt(ambito));
                    ubicVO.setMunPai(Integer.parseInt(ConstantesMeLanbide32.CODIGO_PAIS_ESPANA));
                    ubicVO.setMunPrv(Integer.parseInt(provincia));
                    ubicVO.setMunCod(Integer.parseInt(municipio));
                    ubicVO.setOriOrientDireccion(direccion);
                    ubicVO.setOriOrientHorasSolicitadas(new BigDecimal(horas));
                    ubicVO.setOriOrientDespachos(Integer.parseInt(despachos));
                    ubicVO.setOriOrientAulagrupal(segundaAula != null && !segundaAula.equals("") ? segundaAula : ConstantesMeLanbide32.NO);
                    ubicVO.setPrvPai(Integer.parseInt(ConstantesMeLanbide32.CODIGO_PAIS_ESPANA));
                    ubicVO = MeLanbide32Manager.getInstance().modificarUbicacionORI(ubicVO, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));

                    if (ubicVO != null) {
                        //Si ubicVO != null entonces es que se ha guardado bien
                        try {
                            ubicaciones = MeLanbide32Manager.getInstance().getUbicacionesORI(codOrganizacion, ejercicio, numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                        } catch (BDException bde) {
                            log.error("Error : " + bde.getMessage(), bde);
                        } catch (Exception ex) {
                            log.error("Error : " + ex.getMessage(), ex);
                        }
                    } else {
                        codigoOperacion = "2";
                    }
                } else {
                    codigoOperacion = "3";
                }
            } else {
                codigoOperacion = "3";
            }
        } catch (BDException bde) {
            codigoOperacion = "1";
        } catch (Exception ex) {
            codigoOperacion = "2";
            log.error("Error al procesar peticion" + ex.getMessage(), ex);
        }

        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        for (FilaUbicOrientacionVO fila : ubicaciones) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append(fila.getCodigoUbic() != null ? fila.getCodigoUbic().toString() : "");
            xmlSalida.append("</ID>");
            xmlSalida.append("<PROVINCIA>");
            xmlSalida.append(fila.getProvincia());
            xmlSalida.append("</PROVINCIA>");
            xmlSalida.append("<AMBITO>");
            xmlSalida.append(fila.getAmbito());
            xmlSalida.append("</AMBITO>");
            xmlSalida.append("<MUNICIPIO>");
            xmlSalida.append(fila.getMunicipio());
            xmlSalida.append("</MUNICIPIO>");
            xmlSalida.append("<DIRECCION>");
            xmlSalida.append(fila.getDireccion());
            xmlSalida.append("</DIRECCION>");
            xmlSalida.append("<HORAS>");
            xmlSalida.append(fila.getHoras());
            xmlSalida.append("</HORAS>");
            xmlSalida.append("<DESPACHOS>");
            xmlSalida.append(fila.getDespachos());
            xmlSalida.append("</DESPACHOS>");
            xmlSalida.append("<AULAGRUPAL>");
            xmlSalida.append(fila.getAulaGrupal());
            xmlSalida.append("</AULAGRUPAL>");
            xmlSalida.append("<VALORACION>");
            xmlSalida.append(fila.getValoracion());
            xmlSalida.append("</VALORACION>");
            xmlSalida.append("<TOTAL>");
            xmlSalida.append(fila.getTotal());
            xmlSalida.append("</TOTAL>");
            xmlSalida.append("<HORASADJ>");
            xmlSalida.append(fila.getHorasAdj());
            xmlSalida.append("</HORASADJ>");
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
            e.printStackTrace();
        }//try-catch
    }

    public void eliminarSolicitudORI(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<FilaUbicOrientacionVO> ubicaciones = new ArrayList<FilaUbicOrientacionVO>();
        try {
            String idUbic = (String) request.getParameter("idUbic");
            Integer idU = null;
            if (idUbic == null || idUbic.equals("")) {
                codigoOperacion = "3";
            } else {
                try {
                    idU = Integer.parseInt(idUbic);
                } catch (Exception ex) {
                    codigoOperacion = "3";
                }
                if (idU != null) {
                    int result = MeLanbide32Manager.getInstance().eliminarUbicacionORI(idU, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    if (result <= 0) {
                        codigoOperacion = "1";
                    } else {
                        codigoOperacion = "0";
                        try {
                            String ejercicio = "";
                            try {
                                String[] datos = numExpediente.split(ConstantesMeLanbide32.BARRA_SEPARADORA);
                                ejercicio = datos[0];
                            } catch (Exception ex) {
                                log.debug("No se ha podido obtener el ejercicio al que pertenece el expediente");
                            }
                            ubicaciones = MeLanbide32Manager.getInstance().getUbicacionesORI(codOrganizacion, ejercicio, numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                        } catch (Exception ex) {
                            log.error("Error : " + ex.getMessage(), ex);
                        }
                    }
                } else {
                    codigoOperacion = "3";
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
        for (FilaUbicOrientacionVO fila : ubicaciones) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append(fila.getCodigoUbic() != null ? fila.getCodigoUbic().toString() : "");
            xmlSalida.append("</ID>");
            xmlSalida.append("<PROVINCIA>");
            xmlSalida.append(fila.getProvincia());
            xmlSalida.append("</PROVINCIA>");
            xmlSalida.append("<AMBITO>");
            xmlSalida.append(fila.getAmbito());
            xmlSalida.append("</AMBITO>");
            xmlSalida.append("<MUNICIPIO>");
            xmlSalida.append(fila.getMunicipio());
            xmlSalida.append("</MUNICIPIO>");
            xmlSalida.append("<DIRECCION>");
            xmlSalida.append(fila.getDireccion());
            xmlSalida.append("</DIRECCION>");
            xmlSalida.append("<HORAS>");
            xmlSalida.append(fila.getHoras());
            xmlSalida.append("</HORAS>");
            xmlSalida.append("<DESPACHOS>");
            xmlSalida.append(fila.getDespachos());
            xmlSalida.append("</DESPACHOS>");
            xmlSalida.append("<AULAGRUPAL>");
            xmlSalida.append(fila.getAulaGrupal());
            xmlSalida.append("</AULAGRUPAL>");
            xmlSalida.append("<VALORACION>");
            xmlSalida.append(fila.getValoracion());
            xmlSalida.append("</VALORACION>");
            xmlSalida.append("<TOTAL>");
            xmlSalida.append(fila.getTotal());
            xmlSalida.append("</TOTAL>");
            xmlSalida.append("<HORASADJ>");
            xmlSalida.append(fila.getHorasAdj());
            xmlSalida.append("</HORASADJ>");
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
            e.printStackTrace();
        }//try-catch
    }

    public void valorarSolicitudORI(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<FilaUbicOrientacionVO> ubicaciones = new ArrayList<FilaUbicOrientacionVO>();
        try {
            String idUbic = (String) request.getParameter("idUbic");
            Integer idU = null;
            if (idUbic == null || idUbic.equals("")) {
                codigoOperacion = "3";
            } else {
                try {
                    idU = Integer.parseInt(idUbic);
                } catch (Exception ex) {
                    codigoOperacion = "3";
                }
                if (idU != null) {
                    AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));

                    //Recojo los parametros
                    String despachos = (String) request.getParameter("despachos");
                    String aulaGrupal = (String) request.getParameter("aulaGrupal");
                    String trayVal = (String) request.getParameter("trayVal");
                    String ubicVal = (String) request.getParameter("ubicVal");
                    String despVal = (String) request.getParameter("despVal");
                    String aulaVal = (String) request.getParameter("aulaVal");
                    String puntuacion = (String) request.getParameter("puntuacion");
                    String observaciones = (String) request.getParameter("observaciones");

                    OriUbicacionVO ubic = MeLanbide32Manager.getInstance().getUbicacionORIPorCodigo(idUbic, adapt);

                    ubic.setOriOrientDespachosValidados(despachos != null && !despachos.equals("") ? Integer.parseInt(despachos) : 0);
                    ubic.setOriOrientAulaGrupalValidada(aulaGrupal != null && !aulaGrupal.equals("") ? aulaGrupal : ConstantesMeLanbide32.NO);
                    ubic.setOriOrientValTray(trayVal != null && !trayVal.equals("") ? Integer.parseInt(trayVal) : 0);
                    ubic.setOriOrientValUbic(ubicVal != null && !ubicVal.equals("") ? new BigDecimal(ubicVal) : new BigDecimal("0"));
                    ubic.setOriOrientValDespachos(despVal != null && !despVal.equals("") ? Long.parseLong(despVal) : 0);
                    ubic.setOriOrientValAulas(aulaVal != null && !aulaVal.equals("") ? Long.parseLong(aulaVal) : 0);
                    ubic.setOriOrientPuntuacion(puntuacion != null && !puntuacion.equals("") ? new BigDecimal(puntuacion) : new BigDecimal("0"));
                    ubic.setOriOrientObservaciones(observaciones);

                    ubic = MeLanbide32Manager.getInstance().modificarUbicacionORI(ubic, adapt);

                    if (ubic != null) {
                        //Si ubic != null entonces es que se ha guardado bien
                        try {
                            String ejercicio = "";
                            try {
                                String[] datos = numExpediente.split(ConstantesMeLanbide32.BARRA_SEPARADORA);
                                ejercicio = datos[0];
                            } catch (Exception ex) {
                                log.debug("No se ha podido obtener el ejercicio al que pertenece el expediente");
                            }

                            ubicaciones = MeLanbide32Manager.getInstance().getUbicacionesORI(codOrganizacion, ejercicio, numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                        } catch (BDException bde) {
                            log.error("Error : " + bde.getMessage(), bde);
                        } catch (Exception ex) {
                            log.error("Error : " + ex.getMessage(), ex);
                        }
                    } else {
                        codigoOperacion = "2";
                    }
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
        for (FilaUbicOrientacionVO fila : ubicaciones) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append(fila.getCodigoUbic() != null ? fila.getCodigoUbic().toString() : "");
            xmlSalida.append("</ID>");
            xmlSalida.append("<PROVINCIA>");
            xmlSalida.append(fila.getProvincia());
            xmlSalida.append("</PROVINCIA>");
            xmlSalida.append("<AMBITO>");
            xmlSalida.append(fila.getAmbito());
            xmlSalida.append("</AMBITO>");
            xmlSalida.append("<MUNICIPIO>");
            xmlSalida.append(fila.getMunicipio());
            xmlSalida.append("</MUNICIPIO>");
            xmlSalida.append("<DIRECCION>");
            xmlSalida.append(fila.getDireccion());
            xmlSalida.append("</DIRECCION>");
            xmlSalida.append("<HORAS>");
            xmlSalida.append(fila.getHoras());
            xmlSalida.append("</HORAS>");
            xmlSalida.append("<DESPACHOS>");
            xmlSalida.append(fila.getDespachos());
            xmlSalida.append("</DESPACHOS>");
            xmlSalida.append("<AULAGRUPAL>");
            xmlSalida.append(fila.getAulaGrupal());
            xmlSalida.append("</AULAGRUPAL>");
            xmlSalida.append("<VALORACION>");
            xmlSalida.append(fila.getValoracion());
            xmlSalida.append("</VALORACION>");
            xmlSalida.append("<TOTAL>");
            xmlSalida.append(fila.getTotal());
            xmlSalida.append("</TOTAL>");
            xmlSalida.append("<HORASADJ>");
            xmlSalida.append(fila.getHorasAdj());
            xmlSalida.append("</HORASADJ>");
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
            e.printStackTrace();
        }//try-catch
    }

    public String cargarNuevaTrayectoriaORI(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setAttribute("nuevo", "1");
        } catch (Exception ex) {

        }
        return "/jsp/extension/melanbide32/orientacion/nuevaTrayectoriaORI.jsp?codOrganizacionModulo=" + codOrganizacion;
    }

    public String cargarModifTrayectoriaORI(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setAttribute("nuevo", "0");
            String codTray = request.getParameter("idTray");
            if (codTray != null && !codTray.equals("")) {
                OriTrayectoriaVO tray = MeLanbide32Manager.getInstance().getTrayectoriaORIPorCodigo(codTray, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (tray != null) {
                    request.setAttribute("trayModif", tray);
                }
            }
        } catch (Exception ex) {

        }
        return "/jsp/extension/melanbide32/orientacion/nuevaTrayectoriaORI.jsp?codOrganizacionModulo=" + codOrganizacion;
    }

    public void crearTrayectoriaORI(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<FilaTrayectoriaOrientacionVO> trayectorias = new ArrayList<FilaTrayectoriaOrientacionVO>();
        try {
            //Recojo los parametros
            String organismo = (String) request.getParameter("organismo");
            String descripcion = (String) request.getParameter("descripcion");
            String duracion = (String) request.getParameter("duracion");

            String ejercicio = "";
            try {
                String[] datos = numExpediente.split(ConstantesMeLanbide32.BARRA_SEPARADORA);
                ejercicio = datos[0];
            } catch (Exception ex) {
                log.debug("No se ha podido obtener el ejercicio al que pertenece el expediente");
            }
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            EntidadVO entidad = MeLanbide32Manager.getInstance().getEntidad(codOrganizacion, numExpediente, ejercicio, adapt);
            if (entidad == null) {
                try {
                    //Si no existe la entidad, hay que crearla lo primero
                    HashMap<String, String> map = MeLanbide32Manager.getInstance().getDatosTercero(codOrganizacion, numExpediente, ejercicio, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    entidad = new EntidadVO();
                    entidad.setExtEje(Integer.parseInt(ejercicio));
                    entidad.setExtMun(codOrganizacion);
                    entidad.setExtNum(numExpediente);
                    try {
                        entidad.setExtNvr(Integer.parseInt(map.get("EXT_NVR")));
                        entidad.setExtTer(Long.parseLong(map.get("EXT_TER")));
                    } catch (Exception ex) {

                    }
                    entidad = MeLanbide32Manager.getInstance().crearEntidad(entidad, adapt);
                } catch (Exception ex) {
                    codigoOperacion = "4";
                    entidad = null;
                }
            }
            if (entidad != null) {
                //Si ya existia la entidad o se ha creado correctamente, se procede a guardar la ubicacion
                OriTrayectoriaVO trayVO = new OriTrayectoriaVO();
                trayVO.setOriEntCod(entidad.getOriEntCod());
                trayVO.setOriOritrayDescripcion(descripcion);
                trayVO.setOriOritrayDuracion(new BigDecimal(duracion.replaceAll(",", "\\.")));
                trayVO.setOriOritrayOrganismo(organismo);
                trayVO = MeLanbide32Manager.getInstance().crearTrayectoriaORI(trayVO, adapt);

                if (trayVO != null) {
                    //Si ubicVO != null entonces es que se ha guardado bien
                    try {
                        trayectorias = MeLanbide32Manager.getInstance().getTrayectoriasORI(entidad.getOriEntCod(), adapt);
                    } catch (BDException bde) {
                        codigoOperacion = "1";
                        log.error("Error : " + bde.getMessage(), bde);
                    } catch (Exception ex) {
                        codigoOperacion = "2";
                        log.error("Error : " + ex.getMessage(), ex);
                    }
                } else {
                    codigoOperacion = "2";
                }
            } else if (codigoOperacion.equalsIgnoreCase("0")) {
                codigoOperacion = "3";
            }
        } catch (Exception ex) {
            codigoOperacion = "2";
        }

        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        for (FilaTrayectoriaOrientacionVO fila : trayectorias) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append(fila.getOriOritrayCod() != null ? fila.getOriOritrayCod().toString() : "");
            xmlSalida.append("</ID>");
            xmlSalida.append("<DESCRIPCION>");
            xmlSalida.append(fila.getDescTrayectoria());
            xmlSalida.append("</DESCRIPCION>");
            xmlSalida.append("<DURACION>");
            xmlSalida.append(fila.getDuracionServicio());
            xmlSalida.append("</DURACION>");
            xmlSalida.append("<ORGANISMO>");
            xmlSalida.append(fila.getOrganismo());
            xmlSalida.append("</ORGANISMO>");
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
            e.printStackTrace();
        }//try-catch
    }

    public void modificarTrayectoriaORI(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<FilaTrayectoriaOrientacionVO> trayectorias = new ArrayList<FilaTrayectoriaOrientacionVO>();
        try {
            //Recojo los parametros
            String organismo = (String) request.getParameter("organismo");
            String descripcion = (String) request.getParameter("descripcion");
            String duracion = (String) request.getParameter("duracion");
            String idTray = (String) request.getParameter("idTray");

            String ejercicio = "";
            try {
                String[] datos = numExpediente.split(ConstantesMeLanbide32.BARRA_SEPARADORA);
                ejercicio = datos[0];
            } catch (Exception ex) {
                log.debug("No se ha podido obtener el ejercicio al que pertenece el expediente");
            }
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            EntidadVO entidad = MeLanbide32Manager.getInstance().getEntidad(codOrganizacion, numExpediente, ejercicio, adapt);
            if (entidad != null) {
                //Si ya existia la entidad o se ha creado correctamente, se procede a guardar la ubicacion
                OriTrayectoriaVO trayVO = MeLanbide32Manager.getInstance().getTrayectoriaORIPorCodigo(idTray, adapt);
                trayVO.setOriEntCod(entidad.getOriEntCod());
                trayVO.setOriOritrayDescripcion(descripcion);
                trayVO.setOriOritrayDuracion(new BigDecimal(duracion.replaceAll(",", "\\.")));
                trayVO.setOriOritrayOrganismo(organismo);
                trayVO = MeLanbide32Manager.getInstance().modificarTrayectoriaORI(trayVO, adapt);

                if (trayVO != null) {
                    //Si ubicVO != null entonces es que se ha guardado bien
                    try {
                        trayectorias = MeLanbide32Manager.getInstance().getTrayectoriasORI(entidad.getOriEntCod(), adapt);
                    } catch (BDException bde) {
                        codigoOperacion = "1";
                        log.error("Error : " + bde.getMessage(), bde);
                    } catch (Exception ex) {
                        codigoOperacion = "2";
                        log.error("Error : " + ex.getMessage(), ex);
                    }
                } else {
                    codigoOperacion = "2";
                }
            } else {
                codigoOperacion = "3";
            }
        } catch (Exception ex) {
            codigoOperacion = "2";
        }

        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        for (FilaTrayectoriaOrientacionVO fila : trayectorias) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append(fila.getOriOritrayCod() != null ? fila.getOriOritrayCod().toString() : "");
            xmlSalida.append("</ID>");
            xmlSalida.append("<DESCRIPCION>");
            xmlSalida.append(fila.getDescTrayectoria());
            xmlSalida.append("</DESCRIPCION>");
            xmlSalida.append("<DURACION>");
            xmlSalida.append(fila.getDuracionServicio());
            xmlSalida.append("</DURACION>");
            xmlSalida.append("<ORGANISMO>");
            xmlSalida.append(fila.getOrganismo());
            xmlSalida.append("</ORGANISMO>");
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
            e.printStackTrace();
        }//try-catch
    }

    public void eliminarTrayectoriaORI(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<FilaTrayectoriaOrientacionVO> trayectorias = new ArrayList<FilaTrayectoriaOrientacionVO>();
        try {
            String idTray = (String) request.getParameter("idTray");
            Integer idT = null;
            if (idTray == null || idTray.equals("")) {
                codigoOperacion = "3";
            } else {
                try {
                    idT = Integer.parseInt(idTray);
                } catch (Exception ex) {
                    codigoOperacion = "3";
                }
                if (idT != null) {
                    AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                    int result = MeLanbide32Manager.getInstance().eliminarTrayectoriaORI(idT, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    if (result <= 0) {
                        codigoOperacion = "1";
                    } else {
                        codigoOperacion = "0";
                        try {
                            String ejercicio = "";
                            try {
                                String[] datos = numExpediente.split(ConstantesMeLanbide32.BARRA_SEPARADORA);
                                ejercicio = datos[0];
                            } catch (Exception ex) {
                                log.debug("No se ha podido obtener el ejercicio al que pertenece el expediente");
                            }
                            EntidadVO entidad = MeLanbide32Manager.getInstance().getEntidad(codOrganizacion, numExpediente, ejercicio, adapt);
                            trayectorias = MeLanbide32Manager.getInstance().getTrayectoriasORI(entidad.getOriEntCod(), adapt);
                        } catch (Exception ex) {
                            log.error("Error : " + ex.getMessage(), ex);
                        }
                    }
                } else {
                    codigoOperacion = "3";
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
        for (FilaTrayectoriaOrientacionVO fila : trayectorias) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append(fila.getOriOritrayCod() != null ? fila.getOriOritrayCod().toString() : "");
            xmlSalida.append("</ID>");
            xmlSalida.append("<DESCRIPCION>");
            xmlSalida.append(fila.getDescTrayectoria());
            xmlSalida.append("</DESCRIPCION>");
            xmlSalida.append("<DURACION>");
            xmlSalida.append(fila.getDuracionServicio());
            xmlSalida.append("</DURACION>");
            xmlSalida.append("<ORGANISMO>");
            xmlSalida.append(fila.getOrganismo());
            xmlSalida.append("</ORGANISMO>");
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
            e.printStackTrace();
        }//try-catch
    }

    public void guardarAnosValEntidadORI(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<FilaUbicOrientacionVO> ubicaciones = new ArrayList<FilaUbicOrientacionVO>();
        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            try {
                String anosVal = (String) request.getParameter("anosVal");
                String nomEnt = (String) request.getParameter("nomEnt");
                if (nomEnt == null || nomEnt.equals("")) {
                    codigoOperacion = "3";
                } else {
                    Integer nAnos = null;
                    if (anosVal != null && !anosVal.equalsIgnoreCase("")) {
                        try {
                            nAnos = Integer.parseInt(anosVal);
                        } catch (Exception ex) {
                            codigoOperacion = "3";
                        }
                    }
                    if (codigoOperacion.equalsIgnoreCase("0")) {
                        String ejercicio = "";
                        try {
                            String[] datos = numExpediente.split(ConstantesMeLanbide32.BARRA_SEPARADORA);
                            ejercicio = datos[0];
                        } catch (Exception ex) {
                            log.debug("No se ha podido obtener el ejercicio al que pertenece el expediente");
                        }
                        EntidadVO entidad = MeLanbide32Manager.getInstance().getEntidad(codOrganizacion, numExpediente, ejercicio, adapt);
                        if (entidad == null) {
                            try {
                                //Si no existe la entidad, hay que crearla lo primero
                                HashMap<String, String> map = MeLanbide32Manager.getInstance().getDatosTercero(codOrganizacion, numExpediente, ejercicio, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                                entidad = new EntidadVO();
                                entidad.setExtEje(Integer.parseInt(ejercicio));
                                entidad.setExtMun(codOrganizacion);
                                entidad.setExtNum(numExpediente);
                                try {
                                    entidad.setExtNvr(Integer.parseInt(map.get("EXT_NVR")));
                                    entidad.setExtTer(Long.parseLong(map.get("EXT_TER")));
                                } catch (Exception ex) {

                                }
                                entidad = MeLanbide32Manager.getInstance().crearEntidad(entidad, adapt);
                            } catch (Exception ex) {
                                codigoOperacion = "4";
                                entidad = null;
                            }
                        }
                        if (entidad != null && codigoOperacion.equalsIgnoreCase("0")) {
                            entidad.setOriEntTrayectoriaVal(nAnos);
                            entidad.setOriEntNom(nomEnt);
                            int result = MeLanbide32Manager.getInstance().modificarEntidad(entidad, adapt);
                            if (result > 0) {
                                codigoOperacion = "0";
                            } else {
                                codigoOperacion = "1";
                            }
                            if (nAnos != null && codigoOperacion.equalsIgnoreCase("0")) {
                                //Actualizamos la valoración de las diferentes ubicaciones para la entidad
                                result = actualizarValoracionUbicaciones(entidad, adapt);
                                if (result == 0) {
                                    //ubicaciones = MeLanbide32Manager.getInstance().getUbicacionesORI(codOrganizacion, ejercicio, numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                                    codigoOperacion = "1";
                                }
//                                else
//                                {
//                                    codigoOperacion = "1";
//                                }
                            }
                            ubicaciones = MeLanbide32Manager.getInstance().getUbicacionesORI(codOrganizacion, ejercicio, numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                        } else if (codigoOperacion.equalsIgnoreCase("0")) {
                            codigoOperacion = "2";
                        }
                    }
                }
            } catch (Exception ex) {
                codigoOperacion = "2";
                log.error("Error : " + ex.getMessage(), ex);
            }
        } catch (Exception ex) {
            codigoOperacion = "2";
        }

        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        for (FilaUbicOrientacionVO fila : ubicaciones) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append(fila.getCodigoUbic() != null ? fila.getCodigoUbic().toString() : "");
            xmlSalida.append("</ID>");
            xmlSalida.append("<PROVINCIA>");
            xmlSalida.append(fila.getProvincia());
            xmlSalida.append("</PROVINCIA>");
            xmlSalida.append("<AMBITO>");
            xmlSalida.append(fila.getAmbito());
            xmlSalida.append("</AMBITO>");
            xmlSalida.append("<MUNICIPIO>");
            xmlSalida.append(fila.getMunicipio());
            xmlSalida.append("</MUNICIPIO>");
            xmlSalida.append("<DIRECCION>");
            xmlSalida.append(fila.getDireccion());
            xmlSalida.append("</DIRECCION>");
            xmlSalida.append("<HORAS>");
            xmlSalida.append(fila.getHoras());
            xmlSalida.append("</HORAS>");
            xmlSalida.append("<DESPACHOS>");
            xmlSalida.append(fila.getDespachos());
            xmlSalida.append("</DESPACHOS>");
            xmlSalida.append("<AULAGRUPAL>");
            xmlSalida.append(fila.getAulaGrupal());
            xmlSalida.append("</AULAGRUPAL>");
            xmlSalida.append("<VALORACION>");
            xmlSalida.append(fila.getValoracion());
            xmlSalida.append("</VALORACION>");
            xmlSalida.append("<TOTAL>");
            xmlSalida.append(fila.getTotal());
            xmlSalida.append("</TOTAL>");
            xmlSalida.append("<HORASADJ>");
            xmlSalida.append(fila.getHorasAdj());
            xmlSalida.append("</HORASADJ>");
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
            e.printStackTrace();
        }//try-catch
    }

    public String cargarDatosCE(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String ejercicio = "";
        String procedimiento = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
        log.error("cargarDatosCE - Tiempo de Ini Metodo " + numExpediente + " " + dateFormat.format(new Date()));
        AdaptadorSQLBD adapt = null;
        try
        {
            String[] datos = numExpediente.split(ConstantesMeLanbide32.BARRA_SEPARADORA);
            ejercicio = datos[0];
            procedimiento = datos[1];
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            List<FilaUbicCentroEmpleoVO> listaUbicCentroEmpleo = MeLanbide32Manager.getInstance().getUbicacionesCE(codOrganizacion, ejercicio, numExpediente, adapt);
            request.setAttribute("listaUbicCentroEmpleo", listaUbicCentroEmpleo);
            request.setAttribute("ejercicio", ejercicio);

            EntidadVO entidad = MeLanbide32Manager.getInstance().getEntidad(codOrganizacion, numExpediente, ejercicio, adapt);
            if (entidad != null && entidad.getOriEntCod() != null) {
                List<FilaTrayectoriaCentroEmpleoVO> trayectorias = MeLanbide32Manager.getInstance().getTrayectoriasCE(entidad.getOriEntCod(), procedimiento, adapt);
                request.setAttribute("listaTrayectoriaCentroEmpleo", trayectorias);
                request.setAttribute("entidad", entidad);
            } else {
                log.debug("No se ha recuperado la entidad -  Puede ser un expediente Nuevo - la creamos para poder carha auto. las trayectorias");
                // 20170816 Cuando no exista la entidad se creará con los datos ingresado como interesado principal
                TerceroVO terceroExpRol1Interesado = MeLanbide32Manager.getInstance().getTerceroPorExpedienteYRol(codOrganizacion, numExpediente, ejercicio, "1", adapt);
                ////
                entidad = new EntidadVO();
                entidad.setExtMun(codOrganizacion);
                entidad.setExtEje(Integer.valueOf(ejercicio));
                entidad.setExtNum(numExpediente);
                //20170816
                if (terceroExpRol1Interesado != null && terceroExpRol1Interesado.getTerNoc() != null) {
                    String nombreEnt = terceroExpRol1Interesado.getTerNoc().trim().replaceAll("'", "");
                    entidad.setOriEntNom(nombreEnt);
                }
                /////
                entidad = MeLanbide32Manager.getInstance().crearEntidad(entidad, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                List<FilaTrayectoriaCentroEmpleoVO> trayectorias = MeLanbide32Manager.getInstance().getTrayectoriasCE(entidad.getOriEntCod(), procedimiento, adapt);
                request.setAttribute("listaTrayectoriaCentroEmpleo", trayectorias);
                request.setAttribute("entidad", entidad);

            }

            List<SelectItem> listaEspecialidades = MeLanbide32Manager.getInstance().getListaEspecialidades(this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            if (listaEspecialidades != null) {
                request.setAttribute("listaEspecialidades", listaEspecialidades);
            }

            //En funcion del procedimiento (AYORI/CEMP) se cargara una jsp u otra par la parte de abajo "Otros datos"
            if (procedimiento != null && procedimiento.equals(ConstantesMeLanbide32.CODIGO_PROCEDIMIENTO_CEMP)) {
                request.setAttribute("urlOtrosDatosCE", "/jsp/extension/melanbide32/centroempleo/cemp/otrosDatos.jsp");
            } else if (procedimiento != null && procedimiento.equals(ConstantesMeLanbide32.CODIGO_PROCEDIMIENTO_AYORI)) {
                request.setAttribute("urlOtrosDatosCE", "/jsp/extension/melanbide32/centroempleo/ayori/otrosDatos.jsp");
            } else {
                //Por defecto tomamos AYORI
                request.setAttribute("urlOtrosDatosCE", "/jsp/extension/melanbide32/centroempleo/ayori/otrosDatos.jsp");
            }
        } catch (Exception ex) {
            log.debug("No se ha podido obtener el ejercicio al que pertenece el expediente");
        }
        try
        {     
            String url = cargarSubpestanaAmbitoCEMP(codOrganizacion, numExpediente, adapt, request);
            if(url != null)
            {
                request.setAttribute("urlPestanaDatos_ambitosSolicitados", url);
            }
        }
        catch(Exception ex)
        {
            log.error("Error en cargarDatosCEMP: " + ex.toString());
        }
        log.error("cargarDatosCE - Tiempo de Fin Metodo " + numExpediente + " " + dateFormat.format(new Date()));
        return "/jsp/extension/melanbide32/centroempleo/datosEspecificosCE.jsp";
    }

    public String cargarNuevaUbicacionCE(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        try {
            cargarCombosNuevaUbicCE(codOrganizacion, request);
            request.setAttribute("nuevo", "1");
        } catch (Exception ex) {

        }
        return "/jsp/extension/melanbide32/centroempleo/nuevaUbicacionCE.jsp?codOrganizacionModulo=" + codOrganizacion;
    }

    public String cargarModifUbicacionCE(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        try {
            cargarCombosNuevaUbicCE(codOrganizacion, request);
            request.setAttribute("nuevo", "0");
            String codUbic = request.getParameter("idUbic");
            if (codUbic != null && !codUbic.equals("")) {
                CeUbicacionVO ubic = MeLanbide32Manager.getInstance().getUbicacionCEPorCodigo(codUbic, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (ubic != null && ubic.getOriAmbCod() != null) {
                    AmbitoCentroEmpleoVO amb = MeLanbide32Manager.getInstance().getAmbitoCentroEmpleoPorCodigo(ubic.getOriAmbCod(), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    if (amb != null) {
                        ubic.setMunPrv(amb.getOriAmbTerHis());
                        request.setAttribute("ubicModif", ubic);
                    }
                }
            }
        } catch (Exception ex) {

        }
        return "/jsp/extension/melanbide32/centroempleo/nuevaUbicacionCE.jsp?codOrganizacionModulo=" + codOrganizacion;
    }

    public String cargarNuevaTrayectoriaCE(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setAttribute("nuevo", "1");
        } catch (Exception ex) {

        }
        return "/jsp/extension/melanbide32/centroempleo/nuevaTrayectoriaCE.jsp?codOrganizacionModulo=" + codOrganizacion;
    }

    public String cargarModifTrayectoriaCE(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setAttribute("nuevo", "0");
            String codTray = request.getParameter("idTray");
            if (codTray != null && !codTray.equals("")) {
                CeTrayectoriaVO tray = MeLanbide32Manager.getInstance().getTrayectoriaCEPorCodigo(codTray, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (tray != null) {
                    request.setAttribute("trayModif", tray);
                }
            }
        } catch (Exception ex) {

        }
        return "/jsp/extension/melanbide32/centroempleo/nuevaTrayectoriaCE.jsp?codOrganizacionModulo=" + codOrganizacion;
    }

    public void crearUbicacionCE(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<FilaUbicCentroEmpleoVO> ubicaciones = new ArrayList<FilaUbicCentroEmpleoVO>();
        try {
            //Recojo los parametros
            String provincia = (String) request.getParameter("provincia");
            String ambito = (String) request.getParameter("ambito");
            String municipio = (String) request.getParameter("municipio");
            String direccion = (String) request.getParameter("direccion");
            String especial = (String) request.getParameter("especial");
            String validacion = (String) request.getParameter("validacion");
            String codigopostal = (String) request.getParameter("codigopostal");
            String horarioAtencion = (String) request.getParameter("horarioAtencion");
            String esAprobado = (String) request.getParameter("aprobado");
            String mantenido = (String) request.getParameter("mantenido");
            String listaCritEvaluacionSeleccionadosCE = (String) request.getParameter("listaCritEvaluacionSeleccionadosCE");
            String listaCritEvaluacionSeleccionadosCEVal = (String) request.getParameter("listaCritEvaluacionSeleccionadosCEVal");
            String localNuevoValidado = (String) request.getParameter("localNuevoValidado");

            String ejercicio = "";
            try {
                String[] datos = numExpediente.split(ConstantesMeLanbide32.BARRA_SEPARADORA);
                ejercicio = datos[0];
            } catch (Exception ex) {
                log.debug("No se ha podido obtener el ejercicio al que pertenece el expediente");
            }
            EntidadVO entidad = MeLanbide32Manager.getInstance().getEntidad(codOrganizacion, numExpediente, ejercicio, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            if (entidad == null) {
                try {
                    //Si no existe la entidad, hay que crearla lo primero
                    HashMap<String, String> map = MeLanbide32Manager.getInstance().getDatosTercero(codOrganizacion, numExpediente, ejercicio, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    TerceroVO datosTercero = MeLanbide32Manager.getInstance().getTerceroPorExpedienteYRol(codOrganizacion, numExpediente, ejercicio, "1", this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    entidad = new EntidadVO();
                    entidad.setExtEje(Integer.parseInt(ejercicio));
                    entidad.setExtMun(codOrganizacion);
                    entidad.setExtNum(numExpediente);
                    entidad.setOriEntNom(datosTercero != null ? datosTercero.getTerNoc() : "");
                    try {
                        entidad.setExtNvr(Integer.parseInt(map.get("EXT_NVR")));
                        entidad.setExtTer(Long.parseLong(map.get("EXT_TER")));
                    } catch (Exception ex) {

                    }
                    entidad = MeLanbide32Manager.getInstance().crearEntidad(entidad, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                } catch (Exception ex) {
                    codigoOperacion = "4";
                    entidad = null;
                }
            }
            if (entidad != null) {
                //Si ya existia la entidad o se ha creado correctamente, se procede a guardar la ubicacion
                CeUbicacionVO ubicVO = new CeUbicacionVO();
                ubicVO.setOriEntCod(entidad.getOriEntCod());
                ubicVO.setOriAmbCod(Integer.parseInt(ambito));
                ubicVO.setMunPai(Integer.parseInt(ConstantesMeLanbide32.CODIGO_PAIS_ESPANA));
                ubicVO.setMunPrv(Integer.parseInt(provincia));
                ubicVO.setMunCod(Integer.parseInt(municipio));
                ubicVO.setOriCeDireccion(direccion);
                ubicVO.setOriCeEspecial(especial);
                ubicVO.setValidacion(validacion);
                ubicVO.setCodigoPostal(codigopostal);
                ubicVO.setHorarioAtencion(horarioAtencion);
                ubicVO.setAprobado(esAprobado);
                ubicVO.setMantenido(mantenido);
                ubicVO.setPrvPai(Integer.parseInt(ConstantesMeLanbide32.CODIGO_PAIS_ESPANA));
                ubicVO.setLocalNuevoValidado(localNuevoValidado);
                ubicVO.setNumeroExpediente(numExpediente);
                ubicVO = MeLanbide32Manager.getInstance().crearUbicacionCE(ubicVO, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));

                if (ubicVO != null) {
                    // Guardar datos de Criterios
                    log.info("-- Criterios Seleccionados CE Creados BD? " + 
                            meLanbide32ManagerCriteriosSeleccionCE.altaNuevoOriCECriteriosCentro(ubicVO.getOriCeUbicCod(), "2", numExpediente, listaCritEvaluacionSeleccionadosCE, listaCritEvaluacionSeleccionadosCEVal, Integer.parseInt(ejercicio), this.getAdaptSQLBD(String.valueOf(codOrganizacion)))
                    );
                    //Si ubicVO != null entonces es que se ha guardado bien
                    try {
                        ubicaciones = MeLanbide32Manager.getInstance().getUbicacionesCE(codOrganizacion, ejercicio, numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    } catch (BDException bde) {
                        codigoOperacion = "1";
                        log.error("Error : " + bde.getMessage(), bde);
                    } catch (Exception ex) {
                        codigoOperacion = "2";
                        log.error("Error : " + ex.getMessage(), ex);
                    }
                } else {
                    codigoOperacion = "2";
                }
            } else if (codigoOperacion.equalsIgnoreCase("0")) {
                codigoOperacion = "3";
            }
        } catch (Exception ex) {
            codigoOperacion = "2";
        }

        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        for (FilaUbicCentroEmpleoVO fila : ubicaciones) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append(fila.getOriCeUbicCod() != null ? fila.getOriCeUbicCod().toString() : "");
            xmlSalida.append("</ID>");
            xmlSalida.append("<ESPECIAL>");
            xmlSalida.append(fila.getEspecial());
            xmlSalida.append("</ESPECIAL>");
            xmlSalida.append("<VALIDACION>");
            xmlSalida.append(fila.getValidacion());
            xmlSalida.append("</VALIDACION>");
            xmlSalida.append("<PROVINCIA>");
            xmlSalida.append(fila.getProvincia());
            xmlSalida.append("</PROVINCIA>");
            xmlSalida.append("<AMBITO>");
            xmlSalida.append(fila.getAmbito());
            xmlSalida.append("</AMBITO>");
            xmlSalida.append("<MUNICIPIO>");
            xmlSalida.append(fila.getMunicipio());
            xmlSalida.append("</MUNICIPIO>");
            xmlSalida.append("<DIRECCION>");
            xmlSalida.append(fila.getDireccion());
            xmlSalida.append("</DIRECCION>");
            xmlSalida.append("<ADJUDICADA>");
            xmlSalida.append(fila.getAdjudicado());
            xmlSalida.append("</ADJUDICADA>");
            xmlSalida.append("<CODIGOPOSTAL>");
            xmlSalida.append(fila.getCodigoPostal());
            xmlSalida.append("</CODIGOPOSTAL>");
            xmlSalida.append("<HORARIOATENCION>");
            xmlSalida.append(fila.getHorarioAtencion());
            xmlSalida.append("</HORARIOATENCION>");
            xmlSalida.append("<APROBADO>");
            xmlSalida.append(fila.getAprobado());
            xmlSalida.append("</APROBADO>");
            xmlSalida.append("<MANTENIDO>");
            xmlSalida.append(fila.getMantenido());
            xmlSalida.append("</MANTENIDO>");
            xmlSalida.append("<PUNTUACIONCE>");
            xmlSalida.append(fila.getPuntuacionCentroE());
            xmlSalida.append("</PUNTUACIONCE>");
            xmlSalida.append("<LOCAL_NUEVO_VALIDADO>");
            xmlSalida.append(fila.getLocalNuevoValidado());
            xmlSalida.append("</LOCAL_NUEVO_VALIDADO>");
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
            e.printStackTrace();
        }//try-catch
    }

    public void modificarUbicacionCE(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<FilaUbicCentroEmpleoVO> ubicaciones = new ArrayList<FilaUbicCentroEmpleoVO>();
        try {
            //Recojo los parametros
            String provincia = (String) request.getParameter("provincia");
            String ambito = (String) request.getParameter("ambito");
            String municipio = (String) request.getParameter("municipio");
            String direccion = (String) request.getParameter("direccion");
            String especial = (String) request.getParameter("especial");
            String validacion = (String) request.getParameter("validacion");
            String idUbic = (String) request.getParameter("idUbic");
            String codigopostal = (String) request.getParameter("codigopostal");
            String horarioAtencion = (String) request.getParameter("horarioAtencion");
            String esAprobado = (String) request.getParameter("aprobado");
            String mantenido = (String) request.getParameter("mantenido");
            String listaCritEvaluacionSeleccionadosCE = (String) request.getParameter("listaCritEvaluacionSeleccionadosCE");
            String listaCritEvaluacionSeleccionadosCEVal = (String) request.getParameter("listaCritEvaluacionSeleccionadosCEVal");
            String actualizarDatosCriterios = (String) request.getParameter("actualizarDatosCriterios");
            String localNuevoValidado = (String) request.getParameter("localNuevoValidado");

            String ejercicio = "";
            try {
                String[] datos = numExpediente.split(ConstantesMeLanbide32.BARRA_SEPARADORA);
                ejercicio = datos[0];
            } catch (Exception ex) {
                log.debug("No se ha podido obtener el ejercicio al que pertenece el expediente");
            }

            CeUbicacionVO ubicVO = MeLanbide32Manager.getInstance().getUbicacionCEPorCodigo(idUbic, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            if (ubicVO != null && ubicVO.getOriEntCod() != null) {
                EntidadVO entidad = MeLanbide32Manager.getInstance().getEntidadPorCodigo(ubicVO.getOriEntCod(), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (entidad != null) {
                    ubicVO.setOriEntCod(entidad.getOriEntCod());
                    ubicVO.setOriAmbCod(Integer.parseInt(ambito));
                    ubicVO.setMunPai(Integer.parseInt(ConstantesMeLanbide32.CODIGO_PAIS_ESPANA));
                    ubicVO.setMunPrv(Integer.parseInt(provincia));
                    ubicVO.setMunCod(Integer.parseInt(municipio));
                    ubicVO.setOriCeDireccion(direccion);
                    ubicVO.setOriCeEspecial(especial);
                    ubicVO.setValidacion(validacion);
                    ubicVO.setCodigoPostal(codigopostal);
                    ubicVO.setHorarioAtencion(horarioAtencion);
                    ubicVO.setAprobado(esAprobado);
                    ubicVO.setMantenido(mantenido);
                    ubicVO.setPrvPai(Integer.parseInt(ConstantesMeLanbide32.CODIGO_PAIS_ESPANA));
                    ubicVO.setNumeroExpediente(numExpediente);
                    ubicVO.setLocalNuevoValidado(localNuevoValidado);
                    ubicVO = MeLanbide32Manager.getInstance().modificarUbicacionCE(ubicVO, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));

                    if (ubicVO != null) {
                        //Si ubicVO != null entonces es que se ha guardado bien
                        // Actualizar datos de Criterios
                        if("1".equalsIgnoreCase(actualizarDatosCriterios)){
                            log.info("-- Criterios Seleccionados CE Actualizados BD? "
                                    + meLanbide32ManagerCriteriosSeleccionCE.actualizarDatosOriCECriteriosCentro(ubicVO.getOriCeUbicCod(), "2", numExpediente, listaCritEvaluacionSeleccionadosCE, listaCritEvaluacionSeleccionadosCEVal,Integer.parseInt(ejercicio),this.getAdaptSQLBD(String.valueOf(codOrganizacion)))
                            );                            
                        }else{
                            log.info("Se ha actualizado el centro, pero no se ha entrado a actualizar Criterios Evaluacion, no se hace operacion sobre esa tabla");
                        }
                        try {
                            ubicaciones = MeLanbide32Manager.getInstance().getUbicacionesCE(codOrganizacion, ejercicio, numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                        } catch (BDException bde) {
                            log.error("Error : " + bde.getMessage(), bde);
                        } catch (Exception ex) {
                            log.error("Error : " + ex.getMessage(), ex);
                        }
                    } else {
                        codigoOperacion = "2";
                    }
                } else {
                    codigoOperacion = "3";
                }
            } else {
                codigoOperacion = "3";
            }
        } catch (BDException bde) {
            codigoOperacion = "1";
        } catch (Exception ex) {
            codigoOperacion = "2";
            log.error("Error al procesar peticion" + ex.getMessage(), ex);
        }

        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        for (FilaUbicCentroEmpleoVO fila : ubicaciones) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append(fila.getOriCeUbicCod() != null ? fila.getOriCeUbicCod().toString() : "");
            xmlSalida.append("</ID>");
            xmlSalida.append("<ESPECIAL>");
            xmlSalida.append(fila.getEspecial());
            xmlSalida.append("</ESPECIAL>");
            xmlSalida.append("<VALIDACION>");
            xmlSalida.append(fila.getValidacion());
            xmlSalida.append("</VALIDACION>");
            xmlSalida.append("<PROVINCIA>");
            xmlSalida.append(fila.getProvincia());
            xmlSalida.append("</PROVINCIA>");
            xmlSalida.append("<AMBITO>");
            xmlSalida.append(fila.getAmbito());
            xmlSalida.append("</AMBITO>");
            xmlSalida.append("<MUNICIPIO>");
            xmlSalida.append(fila.getMunicipio());
            xmlSalida.append("</MUNICIPIO>");
            xmlSalida.append("<DIRECCION>");
            xmlSalida.append(fila.getDireccion());
            xmlSalida.append("</DIRECCION>");
            xmlSalida.append("<ADJUDICADA>");
            xmlSalida.append(fila.getAdjudicado());
            xmlSalida.append("</ADJUDICADA>");
            xmlSalida.append("<CODIGOPOSTAL>");
            xmlSalida.append(fila.getCodigoPostal());
            xmlSalida.append("</CODIGOPOSTAL>");
            xmlSalida.append("<HORARIOATENCION>");
            xmlSalida.append(fila.getHorarioAtencion());
            xmlSalida.append("</HORARIOATENCION>");
            xmlSalida.append("<APROBADO>");
            xmlSalida.append(fila.getAprobado());
            xmlSalida.append("</APROBADO>");
            xmlSalida.append("<MANTENIDO>");
            xmlSalida.append(fila.getMantenido());
            xmlSalida.append("</MANTENIDO>");
            xmlSalida.append("<PUNTUACIONCE>");
            xmlSalida.append(fila.getPuntuacionCentroE());
            xmlSalida.append("</PUNTUACIONCE>");
            xmlSalida.append("<LOCAL_NUEVO_VALIDADO>");
            xmlSalida.append(fila.getLocalNuevoValidado());
            xmlSalida.append("</LOCAL_NUEVO_VALIDADO>");
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
            e.printStackTrace();
        }//try-catch
    }

    public void eliminarUbicacionCE(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<FilaUbicCentroEmpleoVO> ubicaciones = new ArrayList<FilaUbicCentroEmpleoVO>();
        try {
            String idUbic = (String) request.getParameter("idUbic");
            Integer idU = null;
            if (idUbic == null || idUbic.equals("")) {
                codigoOperacion = "3";
            } else {
                try {
                    idU = Integer.parseInt(idUbic);
                } catch (Exception ex) {
                    codigoOperacion = "3";
                }
                if (idU != null) {
                    int result = MeLanbide32Manager.getInstance().eliminarUbicacionCE(idU, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    if (result <= 0) {
                        codigoOperacion = "1";
                    } else {
                        codigoOperacion = "0";
                        try {
                            String ejercicio = "";
                            try {
                                String[] datos = numExpediente.split(ConstantesMeLanbide32.BARRA_SEPARADORA);
                                ejercicio = datos[0];
                            } catch (Exception ex) {
                                log.debug("No se ha podido obtener el ejercicio al que pertenece el expediente");
                            }
                            ubicaciones = MeLanbide32Manager.getInstance().getUbicacionesCE(codOrganizacion, ejercicio, numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                        } catch (Exception ex) {
                            log.error("Error : " + ex.getMessage(), ex);
                        }
                    }
                } else {
                    codigoOperacion = "3";
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
        for (FilaUbicCentroEmpleoVO fila : ubicaciones) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append(fila.getOriCeUbicCod() != null ? fila.getOriCeUbicCod().toString() : "");
            xmlSalida.append("</ID>");
            xmlSalida.append("<ESPECIAL>");
            xmlSalida.append(fila.getEspecial());
            xmlSalida.append("</ESPECIAL>");
            xmlSalida.append("<PROVINCIA>");
            xmlSalida.append(fila.getProvincia());
            xmlSalida.append("</PROVINCIA>");
            xmlSalida.append("<AMBITO>");
            xmlSalida.append(fila.getAmbito());
            xmlSalida.append("</AMBITO>");
            xmlSalida.append("<MUNICIPIO>");
            xmlSalida.append(fila.getMunicipio());
            xmlSalida.append("</MUNICIPIO>");
            xmlSalida.append("<DIRECCION>");
            xmlSalida.append(fila.getDireccion());
            xmlSalida.append("</DIRECCION>");
            xmlSalida.append("<ADJUDICADA>");
            xmlSalida.append(fila.getAdjudicado());
            xmlSalida.append("</ADJUDICADA>");
            xmlSalida.append("<VALIDACION>");
            xmlSalida.append(fila.getValidacion());
            xmlSalida.append("</VALIDACION>");
            xmlSalida.append("<CODIGOPOSTAL>");
            xmlSalida.append(fila.getCodigoPostal());
            xmlSalida.append("</CODIGOPOSTAL>");
            xmlSalida.append("<HORARIOATENCION>");
            xmlSalida.append(fila.getHorarioAtencion());
            xmlSalida.append("</HORARIOATENCION>");
             xmlSalida.append("<APROBADO>");
            xmlSalida.append(fila.getAprobado());
            xmlSalida.append("</APROBADO>");
            xmlSalida.append("<MANTENIDO>");
            xmlSalida.append(fila.getMantenido());
            xmlSalida.append("</MANTENIDO>");
            xmlSalida.append("<PUNTUACIONCE>");
            xmlSalida.append(fila.getPuntuacionCentroE());
            xmlSalida.append("</PUNTUACIONCE>");
            xmlSalida.append("<LOCAL_NUEVO_VALIDADO>");
            xmlSalida.append(fila.getLocalNuevoValidado());
            xmlSalida.append("</LOCAL_NUEVO_VALIDADO>");
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
            e.printStackTrace();
        }//try-catch
    }

    public void crearTrayectoriaCE(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<FilaTrayectoriaCentroEmpleoVO> trayectorias = new ArrayList<FilaTrayectoriaCentroEmpleoVO>();
        try {
            //Recojo los parametros
            String descripcion = (String) request.getParameter("descripcion");
            String duracion = (String) request.getParameter("duracion");
            String duracionValidada = (String) request.getParameter("duracionValidada");

            String ejercicio = "";
            try {
                String[] datos = numExpediente.split(ConstantesMeLanbide32.BARRA_SEPARADORA);
                ejercicio = datos[0];
            } catch (Exception ex) {
                log.debug("No se ha podido obtener el ejercicio al que pertenece el expediente");
            }
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            EntidadVO entidad = MeLanbide32Manager.getInstance().getEntidad(codOrganizacion, numExpediente, ejercicio, adapt);
            if (entidad == null) {
                try {
                    //Si no existe la entidad, hay que crearla lo primero
                    HashMap<String, String> map = MeLanbide32Manager.getInstance().getDatosTercero(codOrganizacion, numExpediente, ejercicio, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    entidad = new EntidadVO();
                    entidad.setExtEje(Integer.parseInt(ejercicio));
                    entidad.setExtMun(codOrganizacion);
                    entidad.setExtNum(numExpediente);
                    try {
                        entidad.setExtNvr(Integer.parseInt(map.get("EXT_NVR")));
                        entidad.setExtTer(Long.parseLong(map.get("EXT_TER")));
                    } catch (Exception ex) {

                    }
                    entidad = MeLanbide32Manager.getInstance().crearEntidad(entidad, adapt);
                } catch (Exception ex) {
                    codigoOperacion = "4";
                    entidad = null;
                }
            }
            if (entidad != null) {
                //Si ya existia la entidad o se ha creado correctamente, se procede a guardar la ubicacion
                CeTrayectoriaVO trayVO = new CeTrayectoriaVO();
                trayVO.setOriEntCod(entidad.getOriEntCod());
                trayVO.setOriCetraDescripcion(descripcion);
                trayVO.setOriCetraDuracion(new BigDecimal(duracion.replaceAll(",", "\\.")));
                trayVO.setOriCetraDuracionValidada(new BigDecimal(duracionValidada.replaceAll(",", "\\.")));
                trayVO = MeLanbide32Manager.getInstance().crearTrayectoriaCE(trayVO, adapt);

                if (trayVO != null) {
                    //Si ubicVO != null entonces es que se ha guardado bien
                    try {
                        String procedimiento = "";
                        try {
                            String[] datos = numExpediente.split(ConstantesMeLanbide32.BARRA_SEPARADORA);
                            procedimiento = datos[1];
                        } catch (Exception ex) {
                            log.debug("No se ha podido obtener el cod procedimiento al que pertenece el expediente");
                        }
                        trayectorias = MeLanbide32Manager.getInstance().getTrayectoriasCE(entidad.getOriEntCod(), procedimiento, adapt);
                    } catch (BDException bde) {
                        codigoOperacion = "1";
                        log.error("Error : " + bde.getMessage(), bde);
                    } catch (Exception ex) {
                        codigoOperacion = "2";
                        log.error("Error : " + ex.getMessage(), ex);
                    }
                } else {
                    codigoOperacion = "2";
                }
            } else if (codigoOperacion.equalsIgnoreCase("0")) {
                codigoOperacion = "3";
            }
        } catch (Exception ex) {
            codigoOperacion = "2";
        }

        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        for (FilaTrayectoriaCentroEmpleoVO fila : trayectorias) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append(fila.getOriCeCod() != null ? fila.getOriCeCod().toString() : "");
            xmlSalida.append("</ID>");
            xmlSalida.append("<DESCRIPCION>");
            xmlSalida.append(fila.getDescripcion());
            xmlSalida.append("</DESCRIPCION>");
            xmlSalida.append("<DURACION>");
            xmlSalida.append(fila.getDuracion() != null ? fila.getDuracion() : "");
            xmlSalida.append("</DURACION>");
            xmlSalida.append("<DURACION_VALIDADA>");
            xmlSalida.append(fila.getDuracionValidada() != null ? fila.getDuracionValidada() : "");
            xmlSalida.append("</DURACION_VALIDADA>");
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
            e.printStackTrace();
        }//try-catch
    }

    public void modificarTrayectoriaCE(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<FilaTrayectoriaCentroEmpleoVO> trayectorias = new ArrayList<FilaTrayectoriaCentroEmpleoVO>();
        try {
            //Recojo los parametros
            String descripcion = (String) request.getParameter("descripcion");
            String duracion = (String) request.getParameter("duracion");
            String duracionValidada = (String) request.getParameter("duracionValidada");
            String idTray = (String) request.getParameter("idTray");

            String ejercicio = "";
            try {
                String[] datos = numExpediente.split(ConstantesMeLanbide32.BARRA_SEPARADORA);
                ejercicio = datos[0];
            } catch (Exception ex) {
                log.debug("No se ha podido obtener el ejercicio al que pertenece el expediente");
            }
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            EntidadVO entidad = MeLanbide32Manager.getInstance().getEntidad(codOrganizacion, numExpediente, ejercicio, adapt);
            if (entidad != null) {
                //Si ya existia la entidad o se ha creado correctamente, se procede a guardar la ubicacion
                CeTrayectoriaVO trayVO = MeLanbide32Manager.getInstance().getTrayectoriaCEPorCodigo(idTray, adapt);
                trayVO.setOriEntCod(entidad.getOriEntCod());
                trayVO.setOriCetraDescripcion(descripcion);
                if(duracion!=null && duracion!="")
                    trayVO.setOriCetraDuracion(new BigDecimal(duracion.replaceAll(",", "\\.")));
                else
                    trayVO.setOriCetraDuracion(null);
                log.debug("duracionValidada : " + duracionValidada);
                if(duracionValidada!=null && !duracionValidada.isEmpty()){
                    log.debug("duracionValidada : DENTRO DEL IF VALIDACION-" + duracionValidada+"-");
                    trayVO.setOriCetraDuracionValidada(new BigDecimal(duracionValidada.replaceAll(",", "\\.")));
                }else
                    trayVO.setOriCetraDuracionValidada(null);
                    
                trayVO = MeLanbide32Manager.getInstance().modificarTrayectoriaCE(trayVO, adapt);

                if (trayVO != null) {
                    //Si ubicVO != null entonces es que se ha guardado bien
                    try {
                        String procedimiento = "";
                        try {
                            String[] datos = numExpediente.split(ConstantesMeLanbide32.BARRA_SEPARADORA);
                            procedimiento = datos[1];
                        } catch (Exception ex) {
                            log.debug("No se ha podido obtener el cod procedimiento al que pertenece el expediente");
                        }
                        trayectorias = MeLanbide32Manager.getInstance().getTrayectoriasCE(entidad.getOriEntCod(), procedimiento, adapt);
                    } catch (BDException bde) {
                        codigoOperacion = "1";
                        log.error("Error : " + bde.getMessage(), bde);
                    } catch (Exception ex) {
                        codigoOperacion = "2";
                        log.error("Error : " + ex.getMessage(), ex);
                    }
                } else {
                    codigoOperacion = "2";
                }
            } else {
                codigoOperacion = "3";
            }
        } catch (Exception ex) {
            log.error("Eror : "+ex.getMessage(), ex);
            codigoOperacion = "2";
        }

        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        for (FilaTrayectoriaCentroEmpleoVO fila : trayectorias) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append(fila.getOriCeCod() != null ? fila.getOriCeCod().toString() : "");
            xmlSalida.append("</ID>");
            xmlSalida.append("<DESCRIPCION>");
            xmlSalida.append(fila.getDescripcion() != null ? fila.getDescripcion() : "");
            xmlSalida.append("</DESCRIPCION>");
            xmlSalida.append("<DURACION>");
            xmlSalida.append(fila.getDuracion() != null ? fila.getDuracion() : "");
            xmlSalida.append("</DURACION>");
            xmlSalida.append("<DURACION_VALIDADA>");
            xmlSalida.append(fila.getDuracionValidada() != null ? fila.getDuracionValidada() : "");
            xmlSalida.append("</DURACION_VALIDADA>");
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
            e.printStackTrace();
        }//try-catch
    }

    public void eliminarTrayectoriaCE(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<FilaTrayectoriaCentroEmpleoVO> trayectorias = new ArrayList<FilaTrayectoriaCentroEmpleoVO>();
        try {
            String idTray = (String) request.getParameter("idTray");
            Integer idT = null;
            if (idTray == null || idTray.equals("")) {
                codigoOperacion = "3";
            } else {
                try {
                    idT = Integer.parseInt(idTray);
                } catch (Exception ex) {
                    codigoOperacion = "3";
                }
                if (idT != null) {
                    AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                    int result = MeLanbide32Manager.getInstance().eliminarTrayectoriaCE(idT, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    if (result <= 0) {
                        codigoOperacion = "1";
                    } else {
                        codigoOperacion = "0";
                        try {
                            String ejercicio = "";
                            try {
                                String[] datos = numExpediente.split(ConstantesMeLanbide32.BARRA_SEPARADORA);
                                ejercicio = datos[0];
                            } catch (Exception ex) {
                                log.debug("No se ha podido obtener el ejercicio al que pertenece el expediente");
                            }
                            EntidadVO entidad = MeLanbide32Manager.getInstance().getEntidad(codOrganizacion, numExpediente, ejercicio, adapt);
                            String procedimiento = "";
                            try {
                                String[] datos = numExpediente.split(ConstantesMeLanbide32.BARRA_SEPARADORA);
                                procedimiento = datos[1];
                            } catch (Exception ex) {
                                log.debug("No se ha podido obtener el cod procedimiento al que pertenece el expediente");
                            }
                            trayectorias = MeLanbide32Manager.getInstance().getTrayectoriasCE(entidad.getOriEntCod(), procedimiento, adapt);
                        } catch (Exception ex) {
                            log.error("Error : " + ex.getMessage(), ex);
                        }
                    }
                } else {
                    codigoOperacion = "3";
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
        for (FilaTrayectoriaCentroEmpleoVO fila : trayectorias) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append(fila.getOriCeCod() != null ? fila.getOriCeCod().toString() : "");
            xmlSalida.append("</ID>");
            xmlSalida.append("<DESCRIPCION>");
            xmlSalida.append(fila.getDescripcion());
            xmlSalida.append("</DESCRIPCION>");
            xmlSalida.append("<DURACION>");
            xmlSalida.append(fila.getDuracion());
            xmlSalida.append("</DURACION>");
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
            e.printStackTrace();
        }//try-catch
    }

    public void guardarOtrosDatosCE(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {

        String codigoOperacion = "0";
        try {
            //Recojo los parametros
            String admLocal = request.getParameter("admLocal");
            String supramun = request.getParameter("supramun");
            String especialidad = request.getParameter("especialidad");
            String otros = request.getParameter("otros");
            String acolocacion = request.getParameter("acolocacion");
            String numTrab = request.getParameter("numTrab");
            String numTrabDisc = request.getParameter("numTrabDisc");
            String porTrabDisc = request.getParameter("porTrabDisc");

            String admLocalVal = request.getParameter("admLocalVal");
            String supramunVal = request.getParameter("supramunVal");
            String especialidadVal = request.getParameter("especialidadVal");
            String otrosVal = request.getParameter("otrosVal");
            String acolocacionVal = request.getParameter("acolocacionVal");
            String numTrabVal = request.getParameter("numTrabVal");
            String numTrabDiscVal = request.getParameter("numTrabDiscVal");
            String porTrabDiscVal = request.getParameter("porTrabDiscVal");

            String persDiscapacid = request.getParameter("persDiscapacid");
            String riesgoExclusio = request.getParameter("riesgoExclusio");
            String formaProfesion = request.getParameter("formaProfesion");
            String orgSindiEmpres = request.getParameter("orgSindiEmpres");
            String sinAnimoLucro = request.getParameter("sinAnimoLucro");
            String persDiscapacidVAL = request.getParameter("persDiscapacidVAL");
            String riesgoExclusioVAL = request.getParameter("riesgoExclusioVAL");
            String formaProfesionVAL = request.getParameter("formaProfesionVAL");
            String orgSindiEmpresVAL = request.getParameter("orgSindiEmpresVAL");
            String sinAnimoLucroVAL = request.getParameter("sinAnimoLucroVAL");

            String nomEnt = request.getParameter("nomEnt");

            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            if (nomEnt != null && !nomEnt.equals("")) {
                String ejercicio = "";
                try {
                    String[] datos = numExpediente.split(ConstantesMeLanbide32.BARRA_SEPARADORA);
                    ejercicio = datos[0];
                } catch (Exception ex) {
                    log.debug("No se ha podido obtener el ejercicio al que pertenece el expediente");
                }
                EntidadVO ent = MeLanbide32Manager.getInstance().getEntidad(codOrganizacion, numExpediente, ejercicio, adapt);
                if (ent == null) {
                    try {
                        //Si no existe la entidad, hay que crearla lo primero
                        HashMap<String, String> map = MeLanbide32Manager.getInstance().getDatosTercero(codOrganizacion, numExpediente, ejercicio, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                        ent = new EntidadVO();
                        ent.setExtEje(Integer.parseInt(ejercicio));
                        ent.setExtMun(codOrganizacion);
                        ent.setExtNum(numExpediente);
                        try {
                            ent.setExtNvr(Integer.parseInt(map.get("EXT_NVR")));
                            ent.setExtTer(Long.parseLong(map.get("EXT_TER")));
                        } catch (Exception ex) {

                        }
                        ent = MeLanbide32Manager.getInstance().crearEntidad(ent, adapt);
                    } catch (Exception ex) {
                        codigoOperacion = "4";
                        ent = null;
                    }
                }
                if (ent != null) {
                    ent.setOriEntNom(nomEnt);
                    ent.setOriEntAdmLocal(admLocal);
                    ent.setOriEntSupramun(supramun);
                    if (especialidad != null && !especialidad.equals("")) {
                        ent.setOriExpCod(Integer.parseInt(especialidad));
                    } else {
                        ent.setOriExpCod(null);
                    }
                    ent.setOriEntOtros(otros);
                    ent.setOriEntAcolocacion(acolocacion);
                    if (numTrab != null && !numTrab.equals("")) {
                        ent.setOriEntNumtrab(new BigDecimal(numTrab.replaceAll(",", "\\.")));
                    } else {
                        ent.setOriEntNumtrab(null);
                    }
                    if (numTrabDisc != null && !numTrabDisc.equals("")) {
                        ent.setOriEntNumtrabDisc(new BigDecimal(numTrabDisc.replaceAll(",", "\\.")));
                    } else {
                        ent.setOriEntNumtrabDisc(null);
                    }
                    if (porTrabDisc != null && !porTrabDisc.equals("")) {
                        ent.setOriEntPortrabDisc(new BigDecimal(porTrabDisc.replaceAll(",", "\\.")));
                    } else {
                        ent.setOriEntPortrabDisc(null);
                    }
                    ent.setOriEntAdmLocalVal(admLocalVal);
                    ent.setOriEntSupramunVal(supramunVal);
                    if (especialidadVal != null && !especialidadVal.equals("")) {
                        ent.setOriExpCodVal(Integer.parseInt(especialidadVal));
                    } else {
                        ent.setOriExpCodVal(null);
                    }
                    ent.setOriEntOtrosVal(otrosVal);
                    ent.setOriEntAcolocacionVal(acolocacionVal);
                    if (numTrabVal != null && !numTrabVal.equals("")) {
                        ent.setOriEntNumtrabVal(new BigDecimal(numTrabVal.replaceAll(",", "\\.")));
                    } else {
                        ent.setOriEntNumtrabVal(null);
                    }
                    if (numTrabDiscVal != null && !numTrabDiscVal.equals("")) {
                        ent.setOriEntNumtrabDiscVal(new BigDecimal(numTrabDiscVal.replaceAll(",", "\\.")));
                    } else {
                        ent.setOriEntNumtrabDiscVal(null);
                    }
                    if (porTrabDiscVal != null && !porTrabDiscVal.equals("")) {
                        ent.setOriEntPortrabDiscVal(new BigDecimal(porTrabDiscVal.replaceAll(",", "\\.")));
                    } else {
                        ent.setOriEntPortrabDiscVal(null);
                    }
                    ent.setTipAtenPerDiscapa(persDiscapacid);
                    ent.setTipAtenColRiesExc(riesgoExclusio);
                    ent.setTipCentroForProfe(formaProfesion);
                    ent.setTipOrgSindiEmpres(orgSindiEmpres);
                    ent.setTipSinAnimoLucro(sinAnimoLucro);
                    ent.setTipAtenPerDiscapaVAL(persDiscapacidVAL);
                    ent.setTipAtenColRiesExcVAL(riesgoExclusioVAL);
                    ent.setTipCentroForProfeVAL(formaProfesionVAL);
                    ent.setTipOrgSindiEmpresVAL(orgSindiEmpresVAL);
                    ent.setTipSinAnimoLucroVAL(sinAnimoLucroVAL);

                    int result = MeLanbide32Manager.getInstance().modificarEntidad(ent, adapt);
                    if (result <= 0) {
                        codigoOperacion = "1";
                    } else {
                        codigoOperacion = "0";
                    }
                } else if (codigoOperacion.equalsIgnoreCase("0")) {
                    codigoOperacion = "2";
                }
            } else {
                codigoOperacion = "3";
            }
        } catch (Exception ex) {
            codigoOperacion = "2";
        }

        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        xmlSalida.append("</RESPUESTA>");
        try {
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }//try-catch
    }

//    public void cargarAmbitosPorProvincia(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
//    {
//        String codProvincia = (String)request.getParameter("codProvincia");
//        String anoConv = "";
//        try
//        {
//            String[] datos = numExpediente.split(ConstantesMeLanbide32.BARRA_SEPARADORA);
//            anoConv = datos[0];
//        }
//        catch(Exception ex)
//        {
//            log.debug("No se ha podido obtener el ejercicio al que pertenece el expediente");
//        }
//        List<SelectItem> listaAmbitos = new ArrayList<SelectItem>();
//        List<SelectItem> listaMunicipios = new ArrayList<SelectItem>();
//        try
//        {
//            listaAmbitos = MeLanbide32Manager.getInstance().getAmbitosPorProvincia(codProvincia, anoConv, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
//            
//            HashMap<Integer, List<Integer>> municipiosAnadidos = new HashMap<Integer, List<Integer>>();
//            HashMap<Integer, List<Integer>> municipiosExcluidos = new HashMap<Integer, List<Integer>>();
//            
//            String municipiosAnadidosStr = ConfigurationParameter.getParameter("CODIGOS_MUN_ANADIDOS_"+codProvincia, ConstantesMeLanbide32.FICHERO_CONF_PANTALLAS);
//            
//            if(municipiosAnadidosStr != null && !municipiosAnadidosStr.equalsIgnoreCase(""))
//            {
//                Integer codProv = null;
//                Integer codMun = null;
//                String act = null;
//                List<Integer> curList = null;
//                
//                try
//                {
//                    String[] municipios = municipiosAnadidosStr.split(ConstantesMeLanbide32.SEPARADOR_VALORES_CONF);
//                    if(municipios != null && municipios.length > 0)
//                    {
//                        for(int i = 0; i < municipios.length; i++)
//                        {
//                            act = municipios[i];
//                            try
//                            {
//                                codProv = Integer.parseInt(act.substring(0, 2));
//                                codMun = Integer.parseInt(act.substring(2, 4));
//                                curList = municipiosAnadidos.get(codProv);
//                                if(curList == null)
//                                {
//                                    curList = new ArrayList<Integer>();
//                                    municipiosAnadidos.put(codProv, curList);
//                                }
//                                curList.add(codMun);
//                            }
//                            catch(Exception ex)
//                            {
//                                log.debug("No se ha podido procesar el municipio añadido "+act+" para la provincia "+codProvincia);
//                            }
//                        }
//                    }
//                }
//                catch(Exception ex)
//                {
//                    log.debug("No se ha podido leer la configuración de municipios añadidos a la provincia "+codProvincia);
//                }
//            }
//            
//            String municipiosExcluidosStr = ConfigurationParameter.getParameter("CODIGOS_MUN_EXCLUIDOS_"+codProvincia, ConstantesMeLanbide32.FICHERO_CONF_PANTALLAS);
//            
//            if(municipiosExcluidosStr != null && !municipiosExcluidosStr.equalsIgnoreCase(""))
//            {
//                Integer codProv = null;
//                Integer codMun = null;
//                String act = null;
//                List<Integer> curList = null;
//                
//                try
//                {
//                    String[] municipios = municipiosExcluidosStr.split(ConstantesMeLanbide32.SEPARADOR_VALORES_CONF);
//                    if(municipios != null && municipios.length > 0)
//                    {
//                        for(int i = 0; i < municipios.length; i++)
//                        {
//                            act = municipios[i];
//                            try
//                            {
//                                codProv = Integer.parseInt(act.substring(0, 2));
//                                codMun = Integer.parseInt(act.substring(2, 4));
//                                curList = municipiosExcluidos.get(codProv);
//                                if(curList == null)
//                                {
//                                    curList = new ArrayList<Integer>();
//                                    municipiosExcluidos.put(codProv, curList);
//                                }
//                                curList.add(codMun);
//                            }
//                            catch(Exception ex)
//                            {
//                                log.debug("No se ha podido procesar el municipio excluido "+act+" para la provincia "+codProvincia);
//                            }
//                        }
//                    }
//                }
//                catch(Exception ex)
//                {
//                    log.debug("No se ha podido leer la configuración de municipios excluidos de la provincia "+codProvincia);
//                }
//            }
//            listaMunicipios = MeLanbide32Manager.getInstance().getMunicipiosPorProvincia(codProvincia, municipiosAnadidos, municipiosExcluidos, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
//        }
//        catch(Exception ex)
//        {
//            
//        }
//        StringBuffer xmlSalida = new StringBuffer();
//        xmlSalida.append("<RESPUESTA>");
//            xmlSalida.append("<CODIGO_OPERACION>");
//                xmlSalida.append("0");
//            xmlSalida.append("</CODIGO_OPERACION>");
//            for(SelectItem si : listaAmbitos)
//            {
//                xmlSalida.append("<ITEM_AMBITO>");
//                    xmlSalida.append("<ID>");
//                        xmlSalida.append(si.getId());
//                    xmlSalida.append("</ID>");
//                    xmlSalida.append("<LABEL>");
//                        xmlSalida.append(si.getLabel());
//                    xmlSalida.append("</LABEL>");
//                xmlSalida.append("</ITEM_AMBITO>");
//            }
//            for(SelectItem si : listaMunicipios)
//            {
//                xmlSalida.append("<ITEM_MUNICIPIO>");
//                    xmlSalida.append("<ID>");
//                        xmlSalida.append(si.getId());
//                    xmlSalida.append("</ID>");
//                    xmlSalida.append("<LABEL>");
//                        xmlSalida.append(si.getLabel());
//                    xmlSalida.append("</LABEL>");
//                xmlSalida.append("</ITEM_MUNICIPIO>");
//            }
//        xmlSalida.append("</RESPUESTA>");
//        try{
//            response.setContentType("text/xml");
//            response.setCharacterEncoding("UTF-8");
//            PrintWriter out = response.getWriter();
//            out.print(xmlSalida.toString());
//            out.flush();
//            out.close();
//        }catch(Exception e){
//            e.printStackTrace();
//        }//try-catch
//    }
    public void cargarAmbitosHorasPorProvincia(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codProvincia = (String) request.getParameter("codProvincia");
        String anoConv = "";
        try {
            String[] datos = numExpediente.split(ConstantesMeLanbide32.BARRA_SEPARADORA);
            anoConv = datos[0];
        } catch (Exception ex) {
            log.debug("No se ha podido obtener el ejercicio al que pertenece el expediente");
        }
        List<SelectItem> listaAmbitos = new ArrayList<SelectItem>();
        try {
            listaAmbitos = MeLanbide32Manager.getInstance().getAmbitosHorasPorProvincia(codProvincia, anoConv, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        } catch (Exception ex) {

        }
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append("0");
        xmlSalida.append("</CODIGO_OPERACION>");
        for (SelectItem si : listaAmbitos) {
            xmlSalida.append("<ITEM_AMBITO>");
            xmlSalida.append("<ID>");
            xmlSalida.append(si.getId());
            xmlSalida.append("</ID>");
            xmlSalida.append("<LABEL>");
            xmlSalida.append(si.getLabel());
            xmlSalida.append("</LABEL>");
            xmlSalida.append("</ITEM_AMBITO>");
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
            e.printStackTrace();
        }//try-catch
    }

    public void cargarAmbitosCentroEmpleoPorProvincia(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codProvincia = (String) request.getParameter("codProvincia");
        String anoConv = "";
        try {
            String[] datos = numExpediente.split(ConstantesMeLanbide32.BARRA_SEPARADORA);
            anoConv = datos[0];
        } catch (Exception ex) {
            log.debug("No se ha podido obtener el ejercicio al que pertenece el expediente");
        }
        List<SelectItem> listaAmbitos = new ArrayList<SelectItem>();
        try {
            listaAmbitos = MeLanbide32Manager.getInstance().getAmbitosCentroEmpleoPorProvincia(codProvincia, anoConv, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        } catch (Exception ex) {

        }
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append("0");
        xmlSalida.append("</CODIGO_OPERACION>");
        for (SelectItem si : listaAmbitos) {
            xmlSalida.append("<ITEM_AMBITO>");
            xmlSalida.append("<ID>");
            xmlSalida.append(si.getId());
            xmlSalida.append("</ID>");
            xmlSalida.append("<LABEL>");
            xmlSalida.append(si.getLabel());
            xmlSalida.append("</LABEL>");
            xmlSalida.append("</ITEM_AMBITO>");
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
            e.printStackTrace();
        }//try-catch
    }
    
    public void cargarAmbitosCentroEmpleoPorAnioConvAndTipo(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String oriAmbCeTipoAmbito = (String) request.getParameter("oriAmbCeTipoAmbito");
        int oriAmbCeTipoAmbitoInt = (oriAmbCeTipoAmbito != null && oriAmbCeTipoAmbito != "" ? Integer.parseInt(oriAmbCeTipoAmbito) : 0);
        String oriAmbCeAnoconv = (String) request.getParameter("oriAmbCeAnoconv");

        try {
            String[] datos = numExpediente.split(ConstantesMeLanbide32.BARRA_SEPARADORA);
            String anoConv = datos[0];
        } catch (Exception ex) {
            log.debug("No se ha podido obtener el ejercicio al que pertenece el expediente");
        }
        List<SelectItem> listaAmbitos = new ArrayList<SelectItem>();
        try {
            listaAmbitos = MeLanbide32Manager.getInstance().getAmbitosCentroEmpleoPorAnioConvAndTipo(oriAmbCeAnoconv,oriAmbCeTipoAmbitoInt,this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        } catch (Exception ex) {

        }
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append("0");
        xmlSalida.append("</CODIGO_OPERACION>");
        for (SelectItem si : listaAmbitos) {
            xmlSalida.append("<ITEM_AMBITO>");
            xmlSalida.append("<ID>");
            xmlSalida.append(si.getId());
            xmlSalida.append("</ID>");
            xmlSalida.append("<LABEL>");
            xmlSalida.append(si.getLabel());
            xmlSalida.append("</LABEL>");
            xmlSalida.append("</ITEM_AMBITO>");
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
            e.printStackTrace();
        }//try-catch
    }

    public void cargarMunicipiosPorAmbitoProvinciaHoras(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        List<SelectItem> listaMunicipios = new ArrayList<SelectItem>();
        try {
            String codProvincia = (String) request.getParameter("codProvincia");
            String codAmbito = (String) request.getParameter("codAmbito");

            String[] datos = numExpediente.split(ConstantesMeLanbide32.BARRA_SEPARADORA);
            String ejercicio = datos[0];
            Integer ano = Integer.parseInt(ejercicio);

            listaMunicipios = MeLanbide32Manager.getInstance().getMunicipiosPorAmbitoProvinciaHoras(codProvincia, Integer.parseInt(codAmbito), ano, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        } catch (Exception ex) {

        }
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append("0");
        xmlSalida.append("</CODIGO_OPERACION>");
        for (SelectItem si : listaMunicipios) {
            xmlSalida.append("<ITEM_MUNICIPIO>");
            xmlSalida.append("<ID>");
            xmlSalida.append(si.getId());
            xmlSalida.append("</ID>");
            xmlSalida.append("<LABEL>");
            xmlSalida.append(si.getLabel());
            xmlSalida.append("</LABEL>");
            xmlSalida.append("<PRV>");
            xmlSalida.append(si.getCodPrv());
            xmlSalida.append("</PRV>");
            xmlSalida.append("</ITEM_MUNICIPIO>");
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
            e.printStackTrace();
        }//try-catch
    }

    public void cargarMunicipiosPorAmbitoProvinciaCentroEmpleo(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        List<SelectItem> listaMunicipios = new ArrayList<SelectItem>();
        try {
            String codProvincia = (String) request.getParameter("codProvincia");
            String codAmbito = (String) request.getParameter("codAmbito");

            String[] datos = numExpediente.split(ConstantesMeLanbide32.BARRA_SEPARADORA);
            String ejercicio = datos[0];
            Integer ano = Integer.parseInt(ejercicio);

            listaMunicipios = MeLanbide32Manager.getInstance().getMunicipiosPorAmbitoProvinciaCentroEmpleo(codProvincia, Integer.parseInt(codAmbito), ano, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        } catch (Exception ex) {

        }
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append("0");
        xmlSalida.append("</CODIGO_OPERACION>");
        for (SelectItem si : listaMunicipios) {
            xmlSalida.append("<ITEM_MUNICIPIO>");
            xmlSalida.append("<ID>");
            xmlSalida.append(si.getId());
            xmlSalida.append("</ID>");
            xmlSalida.append("<LABEL>");
            xmlSalida.append(si.getLabel());
            xmlSalida.append("</LABEL>");
            xmlSalida.append("<PRV>");
            xmlSalida.append(si.getCodPrv());
            xmlSalida.append("</PRV>");
            xmlSalida.append("</ITEM_MUNICIPIO>");
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
            e.printStackTrace();
        }//try-catch
    }

    public void cargarComboProcesos(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {

        int codIdioma = 1;

        UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
        if (usuario != null) {
            codIdioma = usuario.getIdioma();
        }

        List<SelectItem> listaProcesos = new ArrayList<SelectItem>();
        try {
            SelectItem si = null;

            si = new SelectItem();

            si.setId(ConstantesMeLanbide32.COD_PROC_ADJUDICA_CENTROS);
            si.setLabel(MeLanbide32Utils.obtenerNombreProceso(ConstantesMeLanbide32.COD_PROC_ADJUDICA_CENTROS, codIdioma));
            listaProcesos.add(si);

            si = new SelectItem();
            si.setId(ConstantesMeLanbide32.COD_PROC_ADJUDICA_HORAS);
            si.setLabel(MeLanbide32Utils.obtenerNombreProceso(ConstantesMeLanbide32.COD_PROC_ADJUDICA_HORAS, codIdioma));
            listaProcesos.add(si);

            si = new SelectItem();
            si.setId(ConstantesMeLanbide32.COD_PROC_CONSOLIDA_CENTROS);
            si.setLabel(MeLanbide32Utils.obtenerNombreProceso(ConstantesMeLanbide32.COD_PROC_CONSOLIDA_CENTROS, codIdioma));
            listaProcesos.add(si);

            si = new SelectItem();
            si.setId(ConstantesMeLanbide32.COD_PROC_CONSOLIDA_HORAS);
            si.setLabel(MeLanbide32Utils.obtenerNombreProceso(ConstantesMeLanbide32.COD_PROC_CONSOLIDA_HORAS, codIdioma));
            listaProcesos.add(si);

            si = new SelectItem();
            si.setId(ConstantesMeLanbide32.COD_PROC_DESHACER_CONSOLIDACION_CENTROS);
            si.setLabel(MeLanbide32Utils.obtenerNombreProceso(ConstantesMeLanbide32.COD_PROC_DESHACER_CONSOLIDACION_CENTROS, codIdioma));
            listaProcesos.add(si);

            si = new SelectItem();
            si.setId(ConstantesMeLanbide32.COD_PROC_DESHACER_CONSOLIDACION_HORAS);
            si.setLabel(MeLanbide32Utils.obtenerNombreProceso(ConstantesMeLanbide32.COD_PROC_DESHACER_CONSOLIDACION_HORAS, codIdioma));
            listaProcesos.add(si);

            si = new SelectItem();
            si.setId(ConstantesMeLanbide32.COD_PROC_DOCUMENTACION_CENTROS);
            si.setLabel(MeLanbide32Utils.obtenerNombreProceso(ConstantesMeLanbide32.COD_PROC_DOCUMENTACION_CENTROS, codIdioma));
            listaProcesos.add(si);

            si = new SelectItem();
            si.setId(ConstantesMeLanbide32.COD_PROC_DOCUMENTACION_HORAS);
            si.setLabel(MeLanbide32Utils.obtenerNombreProceso(ConstantesMeLanbide32.COD_PROC_DOCUMENTACION_HORAS, codIdioma));
            listaProcesos.add(si);
        } catch (Exception ex) {

        }
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append("0");
        xmlSalida.append("</CODIGO_OPERACION>");
        for (SelectItem si : listaProcesos) {
            xmlSalida.append("<SELECT_ITEM>");
            xmlSalida.append("<ID>");
            xmlSalida.append(si.getId());
            xmlSalida.append("</ID>");
            xmlSalida.append("<LABEL>");
            xmlSalida.append(si.getLabel());
            xmlSalida.append("</LABEL>");
            xmlSalida.append("</SELECT_ITEM>");
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
            e.printStackTrace();
        }//try-catch
    }

    public void filtrarAuditoriaProcesos(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        List<FilaAuditoriaProcesosVO> audList = new ArrayList<FilaAuditoriaProcesosVO>();
        List<FilaAuditoriaProcesosVO> tempList = new ArrayList<FilaAuditoriaProcesosVO>();
        try {
            String p1 = (String) request.getParameter("pagAct");
            String p2 = (String) request.getParameter("maxFilas");

            String nombre = (String) request.getParameter("nomApellidos");
            String p3 = (String) request.getParameter("feDesde");
            String p4 = (String) request.getParameter("feHasta");
            String p5 = (String) request.getParameter("codProc");

            String codProcedimiento = request.getParameter("codProcedimiento");

            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

            Date feDesde = null;
            if (p3 != null && !p3.equals("")) {
                try {
                    feDesde = format.parse(p3);
                    Calendar cal = new GregorianCalendar();
                    cal.setTime(feDesde);
                    cal.set(Calendar.HOUR, 0);
                    cal.set(Calendar.MINUTE, 0);
                    cal.set(Calendar.SECOND, 0);
                    feDesde = cal.getTime();
                } catch (Exception ex) {

                }
            }
            Date feHasta = null;
            if (p4 != null && !p4.equals("")) {
                try {
                    feHasta = format.parse(p4);
                    Calendar cal = new GregorianCalendar();
                    cal.setTime(feHasta);
                    cal.set(Calendar.HOUR, 23);
                    cal.set(Calendar.MINUTE, 59);
                    cal.set(Calendar.SECOND, 59);
                    feHasta = cal.getTime();
                } catch (Exception ex) {

                }
            }

            Integer codProceso = null;
            if (p5 != null && !p5.equals("")) {
                codProceso = Integer.parseInt(p5);
            }

            Integer act = null;
            Integer max = null;
            try {
                act = Integer.parseInt(p1);
                max = Integer.parseInt(p2);
            } catch (Exception ex) {
                act = 0;
                max = 0;
            }

            int codIdioma = 1;

            UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
            if (usuario != null) {
                codIdioma = usuario.getIdioma();
            }

            tempList = MeLanbide32Manager.getInstance().filtrarAuditoriaProcesos(nombre, feDesde, feHasta, codProceso, codProcedimiento, codIdioma, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));

            int desde = act - 1;
            desde = desde * max;
            if (desde < 0) {
                desde = 0;
            }
            int hasta = desde + max;
            if (hasta > tempList.size()) {
                hasta = tempList.size();
            }

            for (int i = desde; i < hasta; i++) {
                audList.add(tempList.get(i));
            }
        } catch (Exception ex) {

        }
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append("0");
        xmlSalida.append("</CODIGO_OPERACION>");
        xmlSalida.append("<TOTAL_REGISTROS>");
        xmlSalida.append(tempList.size());
        xmlSalida.append("</TOTAL_REGISTROS>");
        for (FilaAuditoriaProcesosVO fila : audList) {
            xmlSalida.append("<REGISTRO>");
            xmlSalida.append("<NOMAPELLIDOS>");
            xmlSalida.append(fila.getNomApellidos());
            xmlSalida.append("</NOMAPELLIDOS>");
            xmlSalida.append("<PROCESO>");
            xmlSalida.append(fila.getProceso());
            xmlSalida.append("</PROCESO>");
            xmlSalida.append("<FECHA>");
            xmlSalida.append(fila.getFecha());
            xmlSalida.append("</FECHA>");
            xmlSalida.append("<RESULTADO>");
            xmlSalida.append(fila.getResultado());
            xmlSalida.append("</RESULTADO>");
            xmlSalida.append("</REGISTRO>");
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
            e.printStackTrace();
        }//try-catch
    }

//    public void generarListadoAltaCentros(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
//    {
//        try
//        {
//            
//            if(request.getParameter("ano") != null)
//            {
//                Integer ano = Integer.parseInt(request.getParameter("ano"));
//                String ejercicio = ano.toString();
//                String urlMasterReport = "/es/altia/flexia/integracion/moduloexterno/melanbide32/reports/altaCentrosAyori_main.jasper";
//
//                SimpleDateFormat formato = new SimpleDateFormat("ddMMyyyyHHmmss");
//
//                Date date = new Date();
//                String feImp = formato.format(date);
//
//                JasperReport masterReport = (JasperReport) JRLoader.loadObject(this.getClass().getResourceAsStream(urlMasterReport));
//
//
//                List<Subreport> subreportList = new ArrayList<Subreport>();
//
//
//
//                Map<String, Object> reportParams = new HashMap<String, Object>();
//
//                String urlLogo="/es/altia/flexia/integracion/moduloexterno/melanbide32/reports/imagenes/CabeceraLanbide.jpg";
//                reportParams.put("logo", getClass().getClassLoader().getResourceAsStream(urlLogo));
//            
//            
//            
//            
//
//                /* QUERY PARA RECUPERAR ENTIDADES
//                 select distinct(ubic.ori_ent_cod) from ori_tmp_adjudicacion adj inner join ori_ori_ubic ubic on adj.ori_orient_ubic_cod = ubic.ori_orient_ubic_cod
//                 where adj.horas_asignadas > 0 order by ori_ent_cod
//                 */
//                AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
//                List<String> tramites = new ArrayList<String>();
//                tramites.add(ConstantesMeLanbide32.COD_TRAMITE_DENEGACION);
//                
//                List<EntidadVO> entidades = MeLanbide32Manager.getInstance().getEntidadesQueNoEstanEnEstado(ejercicio, tramites, adapt);
//                
//                List<OriUbicacionVO> ubicacionesHoras = null;
//                List<CeUbicacionVO> ubicacionesCe = null;
//                Subreport subreport = null;
//                Map<String, Object> subreportParams = null;
//                JasperReport subreportJR = null;
//                List<FilaUbicacionReportVO> subreportDataSource = null;
//                TerceroVO ter = null;
//                TerceroVO repLegal = null;
//                DomicilioVO dom = null;
//                TipoViaVO tipoVia = null;
//                ViaVO via = null;
//                String strDomicilio = null;
//                String[] partesTlf = null;
//                MunicipioVO mun = null;
//                ProvinciaVO prv = null;
//                FilaUbicacionReportVO fila = null;
//                subreportJR = (JasperReport) JRLoader.loadObject(this.getClass().getResourceAsStream("/es/altia/flexia/integracion/moduloexterno/melanbide32/reports/altaCentrosAyori_entidad.jasper"));
//                for(EntidadVO ent : entidades)
//                {
//                    try
//                    {
//                        
//                        subreportParams = new HashMap<String, Object>();
//                        subreportDataSource = new ArrayList<FilaUbicacionReportVO>();
//
//                        if(ent.getExtTer() != null)
//                        {
//                            ter = MeLanbide32Manager.getInstance().getTerceroPorCodigo(ent.getExtTer(), adapt);
//                            if(ter != null)
//                            {
//                                subreportParams.put("razonSocial", ter.getTerNoc() != null ? ter.getTerNoc().toUpperCase() : "");
//                                subreportParams.put("documento", ter.getTerDoc() != null ? ter.getTerDoc().toUpperCase() : "");
//                                
//                                dom = MeLanbide32Manager.getInstance().getDomicilioTercero(ter, adapt);
//                                if(dom != null)
//                                {
//                                    if(dom.getTvi() != null)
//                                    {
//                                        tipoVia = MeLanbide32Manager.getInstance().getTipoViaDomicilio(dom, adapt);
//                                    }
//                                    if(dom.getVia() != null)
//                                    {
//                                        via = MeLanbide32Manager.getInstance().getViaDomicilio(dom, adapt);
//                                    }
//                                    
//                                    strDomicilio = this.getDomicilioAsString(dom, tipoVia, via);
//                                    subreportParams.put("domicilio", strDomicilio);
//                                    try
//                                    {
//                                        prv = MeLanbide32Manager.getInstance().getProvinciaPorCodigo(dom.getPrv(), adapt);
//                                        if(prv != null)
//                                        {
//                                            subreportParams.put("provincia", prv.getPrvNom() != null ? prv.getPrvNom().toUpperCase() : "");
//                                        }
//                                        mun = MeLanbide32Manager.getInstance().getMunicipioPorCodigoYProvincia(dom.getMun(), dom.getPrv(), adapt);
//                                        if(mun != null)
//                                        {
//                                            subreportParams.put("municipio", mun.getMunNom() != null ? mun.getMunNom().toUpperCase() : "");
//                                            
//                                        }
//                                    }
//                                    catch(Exception ex)
//                                    {
//                                        
//                                    }
//                                    
//                                    subreportParams.put("codPostal", dom.getCpo() != null ? dom.getCpo().toUpperCase() : "");
//                                }
//                                if(ter.getTerTlf() != null)
//                                {
//                                    partesTlf = ter.getTerTlf().split(ConstantesMeLanbide32.BARRA_SEPARADORA);
//                                    if(partesTlf.length == 2)
//                                    {
//                                        subreportParams.put("telefono", partesTlf[0]);
//                                        subreportParams.put("fax", partesTlf[1]);
//                                    }
//                                    else
//                                    {
//                                        subreportParams.put("telefono", ter.getTerTlf());
//                                    }
//                                }
//                                
//                                subreportParams.put("mail", ter.getTerDce() != null ? ter.getTerDce().toUpperCase() : "");
//                            }
//                        }
//                        
//                        repLegal = MeLanbide32Manager.getInstance().getTerceroPorExpedienteYRol(codOrganizacion, numExpediente, ejercicio, ConstantesMeLanbide32.COD_ROL_REP_LEGAL, adapt);
//                        if(repLegal != null)
//                        {
//                            subreportParams.put("nomApel", repLegal.getTerNoc() != null ? repLegal.getTerNoc().toUpperCase() : "");
//                            subreportParams.put("dniRepresentante", repLegal.getTerDoc() != null ? repLegal.getTerDoc().toUpperCase() : "");
//                        }
//                        
//                        ubicacionesHoras = MeLanbide32Manager.getInstance().getUbicacionesAdjudicadasDeEntidadORI(ent, adapt);
//                        for(OriUbicacionVO ubic : ubicacionesHoras)
//                        {
//                            fila = new FilaUbicacionReportVO();
//                            fila.setDireccion(ubic.getOriOrientDireccion() != null ? ubic.getOriOrientDireccion().toUpperCase() : "");
//                            subreportDataSource.add(fila);
//                        }
//                        
//                        
//                        
//                        
//                        
//                        ubicacionesCe = MeLanbide32Manager.getInstance().getUbicacionesAdjudicadasDeEntidadCE(ent, adapt);
//                        for(CeUbicacionVO ubic : ubicacionesCe)
//                        {
//                            fila = new FilaUbicacionReportVO();
//                            fila.setDireccion(ubic.getOriCeDireccion() != null ? ubic.getOriCeDireccion().toUpperCase() : "");
//                            subreportDataSource.add(fila);
//                        }
//                        
//                        if(subreportDataSource.isEmpty())
//                        {
//                            subreportDataSource.add(new FilaUbicacionReportVO());
//                        }
//                        
//                        subreport = new Subreport(subreportJR, subreportDataSource, subreportParams);
//                        subreportList.add(subreport);
//                    }
//                    catch(Exception ex)
//                    {
//                            log.error(ex.getMessage());
//                    }
//                }
//                
//                if(subreportList.isEmpty())
//                {
//                    subreportDataSource = new ArrayList<FilaUbicacionReportVO>();
//                    subreportDataSource.add(new FilaUbicacionReportVO());
//                    subreportParams = new HashMap<String, Object>();
//                    //subreportJR = (JasperReport) JRLoader.loadObject(this.getClass().getResourceAsStream("/es/altia/flexia/integracion/moduloexterno/melanbide32/reports/altaCentrosAyori_entidad.jasper"));
//                    subreportList.add(new Subreport(subreportJR, subreportDataSource, subreportParams));
//                }
//                
//                JasperPrint print = JasperFillManager.fillReport(masterReport, reportParams, new JRBeanCollectionDataSource(subreportList));
//                ByteArrayOutputStream os = new ByteArrayOutputStream();
//                JRPdfExporter pdfExporter = new JRPdfExporter();
//                pdfExporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
//                pdfExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, os);
//                pdfExporter.exportReport();
//                //progresoReport = 97;
//
//                os.flush();
//                os.close();
//
//
//                File directorioTemp = new File(m_Conf.getString("PDF.base_dir"));
//                if(!directorioTemp.exists())
//                {
//                    directorioTemp.mkdirs();
//                }
//                File informe = File.createTempFile("listadoAltaCentrosAyori", ".pdf", directorioTemp);
//
//                //FileOutputStream archivoSalida = new FileOutputStream(informe);
//                //archivoSalida.close();
//                
//                
//                FileOutputStream fileOutput = new FileOutputStream(informe);
//                BufferedOutputStream bufferedOutput = new BufferedOutputStream(fileOutput);
//                bufferedOutput.write(os.toByteArray(), 0, os.toByteArray().length);
//                bufferedOutput.flush();
//                bufferedOutput.close();
//                fileOutput.flush();
//                fileOutput.close();
//
//                String rutaArchivoSalida = informe.getAbsolutePath();
//
//                FileInputStream istr = new FileInputStream(rutaArchivoSalida);
//
//                BufferedInputStream bstr = new BufferedInputStream( istr ); // promote
//
//                int size = (int) informe.length(); 
//                byte[] data = new byte[size]; 
//                bstr.read( data, 0, size ); 
//                bstr.close();
//
////                response.setContentType("application/pdf");
////                response.setHeader("Content-Disposition", "inline; filename=" + informe.getName());
////                response.setHeader("Content-Transfer-Encoding", "binary");  
//                
//                response.setHeader("Cache-Control","must-revalidate, post-check=0, pre-check=0");
//                response.setHeader("Pragma","public");
//                response.setHeader ("Content-Type", "application/octet-stream");
//                response.setHeader ("Content-Disposition", "attachment; filename="+informe.getName());
//                
//                response.setContentLength(data.length);
//                response.getOutputStream().write(data, 0, data.length);
//                response.getOutputStream().flush();
//                response.getOutputStream().close(); 
//
//                //Creamos la auditoría
//                this.crearAuditoria(codOrganizacion, (UsuarioValueObject)request.getSession().getAttribute("usuario"), ConstantesMeLanbide32.COD_PROC_DOCUMENTACION_CENTROS, null);
//            }
//        } 
//        catch (IOException ex) 
//        {
//            log.error("Error al procesar peticion" + ex.getMessage(),ex);
//        } 
//        catch (Exception e) 
//        {
//                e.printStackTrace();
//                //this.resultadoGenerarInformeEstudioTecnico = mm.getMessage("general.generarDocumentoError");
//        }
//    }
    public void generarListadoAltaCentros(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) throws IOException {
        FileOutputStream fileOutput = null;
        FileInputStream istr = null;
        try {

            if (request.getParameter("ano") != null) {
                Integer ano = Integer.parseInt(request.getParameter("ano"));
                String ejercicio = ano.toString();
                List<Subreport> subreportList = new ArrayList<Subreport>();

                Map<String, Object> reportParams = new HashMap<String, Object>();
                String urlLogoLanbide = "/es/altia/flexia/integracion/moduloexterno/melanbide32/reports/imagenes/Lanbide_logo.jpg";
                reportParams.put("logo", getClass().getClassLoader().getResourceAsStream(urlLogoLanbide));
                String urlLogoGV = "/es/altia/flexia/integracion/moduloexterno/melanbide32/reports/imagenes/GV_logo.jpg";
                reportParams.put("logo2", getClass().getClassLoader().getResourceAsStream(urlLogoGV));

                /* QUERY PARA RECUPERAR ENTIDADES
                 select distinct(ubic.ori_ent_cod) from ori_tmp_adjudicacion adj inner join ori_ori_ubic ubic on adj.ori_orient_ubic_cod = ubic.ori_orient_ubic_cod
                 where adj.horas_asignadas > 0 order by ori_ent_cod
                 */
                AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));

                JasperReport subreportJR = null;
                JasperReport subreportUbicaciones = null;
                subreportJR = (JasperReport) JRLoader.loadObject(this.getClass().getResourceAsStream("/es/altia/flexia/integracion/moduloexterno/melanbide32/reports/altaCentrosAyori_entidad.jasper"));
                subreportUbicaciones = (JasperReport) JRLoader.loadObject(this.getClass().getResourceAsStream("/es/altia/flexia/integracion/moduloexterno/melanbide32/reports/ubicaciones.jasper"));
                List<OriUbicacionVO> ubicacionesHoras = null;
                List<CeUbicacionVO> ubicacionesCe = null;
                Subreport subreport = null;
                Map<String, Object> subreportParams = null;
                List<FilaUbicacionReportVO> subreportDataSourceHoras = null;
                List<FilaUbicacionReportVO> subreportDataSourceCe = null;
                TerceroVO ter = null;
                TerceroVO repLegal = null;
                DomicilioVO dom = null;
                TipoViaVO tipoVia = null;
                ViaVO via = null;
                String strDomicilio = null;
                String[] partesTlf = null;
                MunicipioVO mun = null;
                ProvinciaVO prv = null;
                FilaUbicacionReportVO fila = null;
                ArrayList dataS = new ArrayList();
                dataS.add("");
                List<EntidadVO> entidades = null;
                int codIdioma = 1;
                boolean error = false;
                try {
                    List<String> tramites = new ArrayList<String>();
                    //String[] datos = numExpediente.split(ConstantesMeLanbide32.BARRA_SEPARADORA); //24/01/2017 numexpediente viene nulo
                    //String str = MeLanbide32Manager.getInstance().getCodigoInternoTramite(codOrganizacion, datos[1], ConstantesMeLanbide32.COD_TRAMITE_DENEGACION, adapt);
                    if (request.getParameter("codProcedimiento") != null) {
                        String proc = (String) request.getParameter("codProcedimiento");
                        String str = MeLanbide32Manager.getInstance().getCodigoInternoTramite(codOrganizacion, proc, ConstantesMeLanbide32.COD_TRAMITE_DENEGACION, adapt);
                        if (str != null) {
                            tramites.add(str);
                        }
                        str = null;
                        //str = MeLanbide32Manager.getInstance().getCodigoInternoTramite(codOrganizacion, datos[1], ConstantesMeLanbide32.COD_TRAMITE_CIERRE, adapt);
                        str = MeLanbide32Manager.getInstance().getCodigoInternoTramite(codOrganizacion, proc, ConstantesMeLanbide32.COD_TRAMITE_CIERRE, adapt);
                        if (str != null) {
                            tramites.add(str);
                        }

                        try {
                            UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
                            if (usuario != null) {
                                codIdioma = usuario.getIdioma();
                            }
                        } catch (Exception ex) {

                        }

                        if (tramites != null && tramites.size() > 0) {
                            //entidades = MeLanbide32Manager.getInstance().getEntidadesQueNoEstanEnEstado(ejercicio, datos[1], tramites, adapt);
                            entidades = MeLanbide32Manager.getInstance().getEntidadesQueNoEstanEnEstado(ejercicio, proc, tramites, adapt);
                            if (entidades != null && entidades.size() > 0) {
                                for (EntidadVO ent : entidades) {
                                    try {
                                        if (!MeLanbide32Manager.getInstance().expedienteEstaCerradoOAnulado(codOrganizacion, ent.getExtNum(), ejercicio, adapt)) {
                                            subreportParams = new HashMap<String, Object>();
                                            subreportDataSourceHoras = new ArrayList<FilaUbicacionReportVO>();
                                            subreportDataSourceCe = new ArrayList<FilaUbicacionReportVO>();
                                            ubicacionesHoras = new ArrayList<OriUbicacionVO>();
                                            ubicacionesCe = new ArrayList<CeUbicacionVO>();
                                            if (ent.getExtTer() != null) {
                                                //ter = MeLanbide32Manager.getInstance().getTerceroPorCodigo(ent.getExtTer(), adapt);
                                                ter = MeLanbide32Manager.getInstance().getTerceroPorExpedienteYRol(codOrganizacion, ent.getExtNum(), ejercicio, ConstantesMeLanbide32.COD_ROL_ENTIDAD, adapt);
                                                if (ter != null) {
                                                    subreportParams.put("razonSocial", ter.getTerNoc() != null ? ter.getTerNoc().toUpperCase() : "");
                                                    subreportParams.put("documento", ter.getTerDoc() != null ? ter.getTerDoc().toUpperCase() : "");
                                                    dom = MeLanbide32Manager.getInstance().getDomicilioTercero(ter, adapt);
                                                    if (dom != null) {
                                                        if (dom.getTvi() != null) {
                                                            tipoVia = MeLanbide32Manager.getInstance().getTipoViaDomicilio(dom, adapt);
                                                        }
                                                        if (dom.getVia() != null) {
                                                            via = MeLanbide32Manager.getInstance().getViaDomicilio(dom, adapt);
                                                        }
                                                        strDomicilio = this.getDomicilioAsString(dom, tipoVia, via);
                                                        subreportParams.put("domicilio", strDomicilio);
                                                        try {
                                                            prv = MeLanbide32Manager.getInstance().getProvinciaPorCodigo(dom.getPrv(), adapt);
                                                            if (prv != null) {
                                                                subreportParams.put("provincia", prv.getPrvNom() != null ? prv.getPrvNom().toUpperCase() : "");
                                                            }
                                                            mun = MeLanbide32Manager.getInstance().getMunicipioPorCodigoYProvincia(dom.getMun(), dom.getPrv(), adapt);
                                                            if (mun != null) {
                                                                subreportParams.put("municipio", mun.getMunNom() != null ? mun.getMunNom().toUpperCase() : "");

                                                            }
                                                        } catch (Exception ex) {

                                                        }
                                                        subreportParams.put("codPostal", dom.getCpo() != null ? dom.getCpo().toUpperCase() : "");
                                                    }
                                                    if (ter.getTerTlf() != null) {
                                                        try {
                                                            partesTlf = ter.getTerTlf().split(ConstantesMeLanbide32.BARRA_SEPARADORA);
                                                            if (partesTlf.length == 2) {
                                                                subreportParams.put("telefono", partesTlf[0]);
                                                                subreportParams.put("fax", partesTlf[1]);
                                                            } else {
                                                                subreportParams.put("telefono", ter.getTerTlf());
                                                            }
                                                        } catch (Exception ex) {

                                                        }
                                                    }
                                                    subreportParams.put("mail", ter.getTerDce() != null ? ter.getTerDce().toUpperCase() : "");
                                                }
                                            }
                                            repLegal = MeLanbide32Manager.getInstance().getTerceroPorExpedienteYRol(codOrganizacion, ent.getExtNum(), ejercicio, ConstantesMeLanbide32.COD_ROL_REP_LEGAL, adapt);
                                            if (repLegal != null) {
                                                subreportParams.put("nomApel", repLegal.getTerNoc() != null ? repLegal.getTerNoc().toUpperCase() : "");
                                                subreportParams.put("dniRepresentante", repLegal.getTerDoc() != null ? repLegal.getTerDoc().toUpperCase() : "");
                                            }
                                            ubicacionesHoras = MeLanbide32Manager.getInstance().getUbicacionesAdjudicadasDeEntidadORI(ent, adapt);
                                            if (ubicacionesHoras != null && ubicacionesHoras.size() > 0) {
                                                for (OriUbicacionVO ubic : ubicacionesHoras) {
                                                    fila = new FilaUbicacionReportVO();
                                                    fila.setDireccion(ubic.getOriOrientDireccion() != null ? ubic.getOriOrientDireccion().toUpperCase() : "");
                                                    try {
                                                        mun = MeLanbide32Manager.getInstance().getMunicipioPorCodigoYProvincia(ubic.getMunCod(), ubic.getMunPrv(), adapt);
                                                        if (mun != null) {
                                                            fila.setMunicipio(mun.getMunNom() != null ? mun.getMunNom().toUpperCase() : "");
                                                        }
                                                    } catch (Exception ex) {

                                                    }
                                                    fila.setCodigoP(ubic.getMunPrv() != null ? ubic.getMunPrv().toString() : "");
                                                    subreportDataSourceHoras.add(fila);
                                                }
                                                subreportParams.put("subreportOri", new Subreport(subreportUbicaciones, subreportDataSourceHoras, new HashMap<String, Object>()));
                                            } else {
                                                subreportDataSourceHoras.add(new FilaUbicacionReportVO());
                                            }

                                            ubicacionesCe = MeLanbide32Manager.getInstance().getUbicacionesAdjudicadasDeEntidadCE(ent, adapt);
                                            if (ubicacionesCe != null && ubicacionesCe.size() > 0) {
                                                for (CeUbicacionVO ubic : ubicacionesCe) {
                                                    fila = new FilaUbicacionReportVO();
                                                    fila.setDireccion(ubic.getOriCeDireccion() != null ? ubic.getOriCeDireccion().toUpperCase() : "");
                                                    subreportDataSourceCe.add(fila);
                                                }
                                                subreportParams.put("subreportCe", new Subreport(subreportUbicaciones, subreportDataSourceCe, new HashMap<String, Object>()));
                                            } else {
                                                subreportDataSourceCe.add(new FilaUbicacionReportVO());
                                            }

                                            subreport = new Subreport(subreportJR, dataS, subreportParams);
                                            subreportList.add(subreport);
                                        }
                                    } catch (Exception ex) {
                                        log.error("Error al procesar peticion" + ex.getMessage(), ex);
                                        log.error(ex.getMessage());
                                        error = true;
                                        reportParams.put("mensaje", MeLanbide32I18n.getInstance().getMensaje(codIdioma, "doc.listadoAltaCentros.errorRecuperarDatos"));
                                    }
                                    ter = null;
                                    repLegal = null;
                                    dom = null;
                                    tipoVia = null;
                                    via = null;
                                    strDomicilio = null;
                                    partesTlf = null;
                                    mun = null;
                                    prv = null;
                                }
                                if (subreportList == null || subreportList.isEmpty()) {
                                    reportParams.put("mensaje", MeLanbide32I18n.getInstance().getMensaje(codIdioma, "doc.listadoAltaCentros.noEntidadesMostrarPdf"));
                                }
                            } else {
                                log.debug("LISTADO VACIO " + MeLanbide32I18n.getInstance().getMensaje(codIdioma, "doc.listadoAltaCentros.noEntidadesMostrarPdf"));
                                reportParams.put("mensaje", MeLanbide32I18n.getInstance().getMensaje(codIdioma, "doc.listadoAltaCentros.noEntidadesMostrarPdf"));
                            }
                        } else {
                            error = true;
                            reportParams.put("mensaje", MeLanbide32I18n.getInstance().getMensaje(codIdioma, "doc.listadoAltaCentros.errorRecuperarDatos"));
                        }
                    }

                } catch (Exception ex) {
                    log.debug("ERROR GENERANDO INFORME " + MeLanbide32I18n.getInstance().getMensaje(codIdioma, "doc.listadoAltaCentros.errorRecuperarDatos"));
                    log.debug(ex.getMessage());
                    error = true;
                    reportParams.put("mensaje", MeLanbide32I18n.getInstance().getMensaje(codIdioma, "doc.listadoAltaCentros.errorRecuperarDatos"));
                }
                String urlMasterReport = null;
                JasperReport masterReport = null;
                JasperPrint print = null;
                if (error || subreportList.isEmpty()) {
                    subreportList.clear();
                    log.debug("REPORT VACIO");
                    urlMasterReport = "/es/altia/flexia/integracion/moduloexterno/melanbide32/reports/altaCentrosAyori_main_vacio.jasper";
                    masterReport = (JasperReport) JRLoader.loadObject(this.getClass().getResourceAsStream(urlMasterReport));
                    print = JasperFillManager.fillReport(masterReport, reportParams, new JREmptyDataSource(1));
                } else {
                    log.debug("REPORT LLENO");
                    urlMasterReport = "/es/altia/flexia/integracion/moduloexterno/melanbide32/reports/altaCentrosAyori_main.jasper";
                    masterReport = (JasperReport) JRLoader.loadObject(this.getClass().getResourceAsStream(urlMasterReport));
                    print = JasperFillManager.fillReport(masterReport, reportParams, new JRBeanCollectionDataSource(subreportList));
                }
                ByteArrayOutputStream os = new ByteArrayOutputStream();
//                JRPdfExporter pdfExporter = new JRPdfExporter();
                JRDocxExporter pdfExporter = new JRDocxExporter();
                pdfExporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
                pdfExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, os);
                pdfExporter.exportReport();

//                os.flush();
//                os.close();
                File directorioTemp = new File(m_Conf.getString("PDF.base_dir"));
                if (!directorioTemp.exists()) {
                    directorioTemp.mkdirs();
                }
                File informe = File.createTempFile("listadoAltaCentrosAyori", ".doc", directorioTemp);

                //FileOutputStream archivoSalida = new FileOutputStream(informe);
                //archivoSalida.close();
                fileOutput = new FileOutputStream(informe);
                BufferedOutputStream bufferedOutput = new BufferedOutputStream(fileOutput);
                bufferedOutput.write(os.toByteArray(), 0, os.toByteArray().length);
                bufferedOutput.flush();
                bufferedOutput.close();
                fileOutput.flush();

                //os.flush();
                //os.close();
                String rutaArchivoSalida = informe.getAbsolutePath();
                istr = new FileInputStream(rutaArchivoSalida);
                BufferedInputStream bstr = new BufferedInputStream(istr); // promote
                int size = (int) informe.length();
                byte[] data = new byte[size];
                bstr.read(data, 0, size);
                bstr.close();
//                response.setContentType("application/pdf");
//                response.setHeader("Content-Disposition", "inline; filename=" + informe.getName());
//                response.setHeader("Content-Transfer-Encoding", "binary");  
                log.debug("GENERARLISTADOCENTROS 46");
                response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
                response.setHeader("Pragma", "public");
                response.setHeader("Content-Type", "application/octet-stream");
                response.setHeader("Content-Disposition", "attachment; filename=" + informe.getName());
                response.setContentLength(data.length);
                response.getOutputStream().write(data, 0, data.length);
                response.getOutputStream().flush();
                response.getOutputStream().close();
                //Creamos la auditoría
                //this.crearAuditoria(codOrganizacion, (UsuarioValueObject)request.getSession().getAttribute("usuario"), ConstantesMeLanbide32.COD_PROC_DOCUMENTACION_CENTROS, null);
            }
        } catch (IOException ex) {

            log.debug(ex.getMessage());
            log.error("Error al procesar peticion" + ex.getMessage(), ex);
        } catch (Exception e) {
            log.debug(e.getMessage());
            e.printStackTrace();
            //this.resultadoGenerarInformeEstudioTecnico = mm.getMessage("general.generarDocumentoError");
        } finally {
            fileOutput.close();
            istr.close();
        }
    }

    /**
     * Operación que recupera las listas para los combos y los guarda en la
     * request
     *
     * @param request
     */
    private void cargarCombosNuevaUbicORI(int codOrganizacion, HttpServletRequest request) {
        List<SelectItem> listaProvincias = new ArrayList<SelectItem>();

        //Combo TERRITORIOHISTORICO
        try {
            String provinciasCargar = ConfigurationParameter.getParameter("ori.nuevaUbicacion.listaProvincias", ConstantesMeLanbide32.FICHERO_CONF_PANTALLAS);
            String[] codigosProv = provinciasCargar.split(ConstantesMeLanbide32.SEPARADOR_VALORES_CONF);
            listaProvincias = MeLanbide32Manager.getInstance().getListaProvincias(ConstantesMeLanbide32.CODIGO_PAIS_ESPANA, Arrays.asList(codigosProv), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        } catch (Exception ex) {

        }
        request.setAttribute("listaProvincias", listaProvincias);
    }

    /**
     * Operación que recupera las listas para los combos y los guarda en la
     * request
     *
     * @param request
     */
    private void cargarCombosNuevaUbicCE(int codOrganizacion, HttpServletRequest request) {
        List<SelectItem> listaProvincias = new ArrayList<SelectItem>();

        //Combo TERRITORIOHISTORICO
        try {
            String provinciasCargar = ConfigurationParameter.getParameter("ce.nuevaUbicacion.listaProvincias", ConstantesMeLanbide32.FICHERO_CONF_PANTALLAS);
            String[] codigosProv = provinciasCargar.split(ConstantesMeLanbide32.SEPARADOR_VALORES_CONF);
            listaProvincias = MeLanbide32Manager.getInstance().getListaProvincias(ConstantesMeLanbide32.CODIGO_PAIS_ESPANA, Arrays.asList(codigosProv), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        } catch (Exception ex) {

        }

        request.setAttribute("listaProvincias", listaProvincias);
    }
    private void cargarCombosNuevaAmbitoSolicitadoCEMP(int codOrganizacion, HttpServletRequest request) {
        List<SelectItem> listaTipoAmbitos = new ArrayList<SelectItem>();

        //Combo TIPO AMBITO CEMP 
        try {
            listaTipoAmbitos = MeLanbide32Manager.getInstance().getDesplegableTipoAmbitosCEMP(this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        } catch (Exception ex) {
            log.error("Error al recuperar la lista de tipo de ambitos para le desplegable " + ex.getMessage(), ex);
        }
        request.setAttribute("listaTipoAmbitos", listaTipoAmbitos);
    }

    /**
     * Operación que recupera los datos de conexión a la BBDD
     *
     * @param codOrganizacion
     * @return AdaptadorSQLBD
     */
    public AdaptadorSQLBD getAdaptSQLBD(String codOrganizacion) throws SQLException {
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
        }//if(log.isDebugEnabled())

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

                conGenerico.close();
                rs.close();
                st.close();

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
                if (conGenerico != null && !conGenerico.isClosed()) {
                    conGenerico.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
            }// finally
            if (log.isDebugEnabled()) {
                log.debug("getConnection() : END");
            }
        }// synchronized
        return adapt;
    }//getConnection

    public void adjudicaOrientacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        int codigoOperacion = 0;
        String[] result = null;
        String mensaje = "";
        String estado = "";
        int codIdioma = 1;
        try {
            UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
            if (usuario != null) {
                codIdioma = usuario.getIdioma();
            }
        } catch (Exception ex) {

        }
        if (request.getParameter("ano") != null) {
            try {
                int ano = Integer.parseInt(request.getParameter("ano"));
                try {
                    result = MeLanbide32Manager.getInstance().adjudicaOrientacion(ano, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    mensaje = result[0];
                    if (mensaje != null) {
                        if (mensaje.equalsIgnoreCase(ConstantesMeLanbide32.OK)) {
                            codigoOperacion = 0;
                        } else if (mensaje.equalsIgnoreCase(ConstantesMeLanbide32.ERROR)) {
                            codigoOperacion = 1;
                        } else {
                            mensaje = procesarMensajeProcedimiento(mensaje, codIdioma);
                            codigoOperacion = 4;
                        }
                    } else {
                        codigoOperacion = 1;
                    }

                    if (result[1] != null && result[1].equalsIgnoreCase("1")) {
                        estado = MeLanbide32I18n.getInstance().getMensaje(codIdioma, "expedientes_en_estados_no_permitidos");
                    }

                    //Creamos la auditoría
                    String codProcedimiento = null;
                    try {
                        codProcedimiento = (String) request.getParameter("codProcedimiento");
                    } catch (Exception ex) {

                    }
                    this.crearAuditoria(codOrganizacion, (UsuarioValueObject) request.getSession().getAttribute("usuario"), ConstantesMeLanbide32.COD_PROC_ADJUDICA_HORAS, mensaje, codProcedimiento);
                } catch (Exception ex) {
                    codigoOperacion = 1;
                }
            } catch (Exception ex) {
                codigoOperacion = 2;
            }
        } else {
            codigoOperacion = 3;
        }
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        xmlSalida.append("<MENSAJE>");
        xmlSalida.append(mensaje);
        xmlSalida.append("</MENSAJE>");
        if (estado != null && !estado.equals("")) {
            xmlSalida.append("<ESTADO>");
            xmlSalida.append(estado);
            xmlSalida.append("</ESTADO>");
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
            e.printStackTrace();
        }//try-catch
    }

    public void crearDocumentacionResolucionHoras(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        String rutaArchivoSalida = null;
        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));

            int idioma = 1;
            HttpSession session = request.getSession();
            UsuarioValueObject usuario = new UsuarioValueObject();
            try {
                if (session != null) {
                    usuario = (UsuarioValueObject) session.getAttribute("usuario");
                    if (usuario != null) {
                        idioma = usuario.getIdioma();
                    }
                }
            } catch (Exception ex) {

            }

            Integer ano = new GregorianCalendar().get(Calendar.YEAR);
            try {
                ano = Integer.parseInt((String) request.getParameter("ano"));
            } catch (Exception ex) {

            }

            MeLanbide32I18n meLanbide32I18n = MeLanbide32I18n.getInstance();

            List<Integer> provincias = MeLanbide32Manager.getInstance().getDistintasProvDeAmbitosHoras(adapt);
            HSSFWorkbook libro = new HSSFWorkbook();
            HSSFFont negrita = libro.createFont();
            negrita.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            ProvinciaVO prov = null;
            HSSFSheet hoja = null;
            List<AmbitoHorasVO> ambitosProv = null;
            int numFila = 0;
            List<OriUbicacionVO> ubicacionesAmbito = null;
            int totalesSolicitadas = 0;
            double totalesAdjudicadas = 0.0;
            double puntuacionTotal = 0.0;
            EntidadVO entidad = null;
            Long entCodAnt = null;
            for (Integer codProv : provincias) {
                prov = MeLanbide32Manager.getInstance().getProvinciaPorCodigo(codProv, adapt);
                if (prov != null && prov.getPrvNom() != null) {
                    hoja = libro.createSheet(prov.getPrvNom().toUpperCase());

                    hoja.setColumnWidth(0, 4000);
                    hoja.setColumnWidth(1, 10000);
                    hoja.setColumnWidth(2, 3000);
                    hoja.setColumnWidth(3, 3000);
                    hoja.setColumnWidth(4, 5000);
                    hoja.setColumnWidth(5, 3000);
                    hoja.setColumnWidth(6, 3000);
                    hoja.setColumnWidth(7, 3000);
                    hoja.setColumnWidth(8, 3000);

                    ambitosProv = MeLanbide32Manager.getInstance().getAmbitosHorasPorProvincia(codProv, ano, adapt);
                    for (AmbitoHorasVO amb : ambitosProv) {
                        if (amb.getOriAmbCod() == 2) {
                            System.out.println();
                        }
                        //Ambito
                        anadirAmbitoResolucionHoras(libro, hoja, amb, numFila, meLanbide32I18n, idioma);
                        //HORAS
                        numFila = numFila + 2;
                        anadirHorasAmbitoResolucionHoras(libro, hoja, amb, numFila, meLanbide32I18n, idioma);

                        //Cabecera 1 Tabla
                        numFila = numFila + 3;
                        anadirCabecera1ResolucionHoras(libro, hoja, numFila, meLanbide32I18n, idioma);

                        //Cabecera 2 tabla
                        numFila = numFila + 1;
                        anadirCabecera2ResolucionHoras(libro, hoja, numFila, meLanbide32I18n, idioma);

                        //Entidades
                        try {
                            ubicacionesAmbito = MeLanbide32Manager.getInstance().getUbicacionesDeAmbitoORI(amb, adapt);
                            ubicacionesAmbito = this.ubicacionesQueNoEstanEnResProvAlFinal(ubicacionesAmbito, adapt);
                            for (OriUbicacionVO ubic : ubicacionesAmbito) {
                                numFila = numFila + 1;

                                if (entCodAnt == null) {
                                    entCodAnt = ubic.getOriEntCod();
                                } else if (ubic.getOriEntCod() != null) {
                                    if (entCodAnt.longValue() == ubic.getOriEntCod().longValue()) {
                                        ubic.setOriOrientPuntuacion(new BigDecimal("0"));
                                    }
                                    entCodAnt = ubic.getOriEntCod();
                                }

                                entidad = MeLanbide32Manager.getInstance().getEntidadPorCodigo(ubic.getOriEntCod(), adapt);
                                if (entidad != null) {
                                    double d = anadirEntidadResolucionHoras(libro, hoja, ubic, entidad, numFila, adapt);
                                    totalesSolicitadas = totalesSolicitadas + (ubic.getOriOrientHorasSolicitadas() != null ? ubic.getOriOrientHorasSolicitadas().intValue() : 0);
                                    //totalesAdjudicadas = totalesAdjudicadas + (ubic.getOriOrientHorasadj() != null ? ubic.getOriOrientHorasadj().doubleValue() : 0);
                                    totalesAdjudicadas = totalesAdjudicadas + d;
                                    puntuacionTotal = puntuacionTotal + (ubic.getOriOrientPuntuacion() != null ? ubic.getOriOrientPuntuacion().doubleValue() : 0);
                                }
                            }
                        } catch (Exception ex) {
                            log.error("Error al procesar peticion" + ex.getMessage(), ex);
                        }

                        //Totales
                        numFila = numFila + 1;
                        anadirFilaTotalesResolucionHoras(libro, hoja, numFila, totalesSolicitadas, puntuacionTotal, totalesAdjudicadas, meLanbide32I18n, idioma);
                        numFila = numFila + 4;
                        totalesAdjudicadas = 0.0;
                        totalesSolicitadas = 0;
                        puntuacionTotal = 0;
                    }
                }
                numFila = 0;
            }

            try {
                File directorioTemp = new File(m_Conf.getString("PDF.base_dir"));
                File informe = File.createTempFile("resolucionHoras", ".xls", directorioTemp);

                FileOutputStream archivoSalida = new FileOutputStream(informe);
                libro.write(archivoSalida);
                archivoSalida.close();

                rutaArchivoSalida = informe.getAbsolutePath();

                FileInputStream istr = new FileInputStream(rutaArchivoSalida);

                BufferedInputStream bstr = new BufferedInputStream(istr); // promote

                int size = (int) informe.length();
                byte[] data = new byte[size];
                bstr.read(data, 0, size);
                bstr.close();

                response.setContentType("application/vnd.ms-excel");
                response.setHeader("Content-Disposition", "inline; filename=" + informe.getName());
                response.setHeader("Content-Transfer-Encoding", "binary");
                response.setContentLength(data.length);
                response.getOutputStream().write(data, 0, data.length);
                response.getOutputStream().flush();
                response.getOutputStream().close();

                //Creamos la auditoría
                String codProcedimiento = null;
                try {
                    codProcedimiento = (String) request.getParameter("codProcedimiento");
                } catch (Exception ex) {

                } finally {
                    istr.close();
                }
                this.crearAuditoria(codOrganizacion, (UsuarioValueObject) request.getSession().getAttribute("usuario"), ConstantesMeLanbide32.COD_PROC_DOCUMENTACION_HORAS, null, codProcedimiento);
            } catch (Exception ioe) {
            }
        } catch (Exception ex) {
            log.error("Error al procesar peticion" + ex.getMessage(), ex);
        }
    }

    private void anadirAmbitoResolucionHoras(HSSFWorkbook libro, HSSFSheet hoja, AmbitoHorasVO amb, int numFila, MeLanbide32I18n meLanbide32I18n, int idioma) {
        HSSFRow fila = hoja.createRow(numFila);

        HSSFCellStyle estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        HSSFFont negrita = libro.createFont();
        negrita.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        estiloCelda.setFont(negrita);
        estiloCelda.setWrapText(true);
        HSSFCell celda = fila.createCell(0);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionHoras.ambito"));
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(1);
        celda.setCellValue(amb.getOriAmbAmbito() != null ? amb.getOriAmbAmbito() : "");
        celda.setCellStyle(estiloCelda);
    }

    private void anadirHorasAmbitoResolucionHoras(HSSFWorkbook libro, HSSFSheet hoja, AmbitoHorasVO amb, int numFila, MeLanbide32I18n meLanbide32I18n, int idioma) {
        HSSFRow fila = hoja.createRow(numFila);

        HSSFCell celda = fila.createCell(0);
        HSSFCellStyle estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.TAN.index);
        HSSFFont negrita = libro.createFont();
        negrita.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        estiloCelda.setFont(negrita);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setWrapText(true);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionHoras.horas"));
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.TAN.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(1);
        celda.setCellValue(amb.getOriAmbHorasTot() != null ? amb.getOriAmbHorasTot().doubleValue() : 0);
        celda.setCellStyle(estiloCelda);
        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
    }

    private void anadirCabecera1ResolucionHoras(HSSFWorkbook libro, HSSFSheet hoja, int numFila, MeLanbide32I18n meLanbide32I18n, int idioma) {
        HSSFRow fila = hoja.createRow(numFila);

//        hoja.addMergedRegion(new Region(numFila, (short)0, numFila, (short)1));
//        hoja.addMergedRegion(new Region(numFila, (short)3, numFila, (short)7));
        hoja.addMergedRegion(new CellRangeAddress(numFila, numFila, 0, 1));
        hoja.addMergedRegion(new CellRangeAddress(numFila, numFila, 3, 7));

        HSSFCellStyle estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.CORNFLOWER_BLUE.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setWrapText(true);
        HSSFCell celda = fila.createCell(0);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionHoras.horasOrientacion"));
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.CORNFLOWER_BLUE.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(1);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionHoras.horasOrientacion"));
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.WHITE.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(2);
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(3);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionHoras.valoracion"));
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(4);
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(5);
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(6);
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(7);
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.WHITE.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(8);
        celda.setCellStyle(estiloCelda);
    }

    private void anadirCabecera2ResolucionHoras(HSSFWorkbook libro, HSSFSheet hoja, int numFila, MeLanbide32I18n meLanbide32I18n, int idioma) {
        HSSFRow fila = hoja.createRow(numFila);
        fila.setHeight((short) 800);

        hoja.addMergedRegion(new CellRangeAddress(numFila, numFila, 0, 1));

        HSSFCellStyle estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        HSSFCell celda = fila.createCell(0);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionHoras.entColaboradoras"));
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(1);
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.LIGHT_BLUE.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(2);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionHoras.horasSolic"));
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(3);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionHoras.trayectoria"));
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(4);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionHoras.puntUbicacion"));
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(5);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionHoras.despacho"));
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(6);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionHoras.segundaAula"));
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(7);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionHoras.puntTotal"));
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.LIME.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(8);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionHoras.horasAdj"));
        celda.setCellStyle(estiloCelda);
    }

    private double anadirEntidadResolucionHoras(HSSFWorkbook libro, HSSFSheet hoja, OriUbicacionVO ubic, EntidadVO entidad, int numFila, AdaptadorSQLBD adapt) {
        HSSFRow fila = hoja.createRow(numFila);

        hoja.addMergedRegion(new CellRangeAddress(numFila, numFila, 0, 1));

        HSSFCellStyle estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.WHITE.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setWrapText(true);
        HSSFCell celda = fila.createCell(0);
        String nomEnt = entidad != null && entidad.getOriEntNom() != null ? entidad.getOriEntNom() : "";
        nomEnt += nomEnt != null && !nomEnt.equals("") ? (ubic.getOriOrientDireccion() != null && !ubic.getOriOrientDireccion().equals("") ? " - " : "") : "";
        nomEnt += ubic != null && ubic.getOriOrientDireccion() != null && !ubic.getOriOrientDireccion().equals("") ? ubic.getOriOrientDireccion() : "";

        celda.setCellValue(nomEnt);
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.WHITE.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(1);
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(2);
        celda.setCellValue(ubic.getOriOrientHorasSolicitadas() != null ? ubic.getOriOrientHorasSolicitadas().doubleValue() : 0);
        celda.setCellStyle(estiloCelda);
        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.WHITE.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(3);
        celda.setCellValue(ubic.getOriOrientValTray() != null ? ubic.getOriOrientValTray().doubleValue() : 0);
        celda.setCellStyle(estiloCelda);
        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.WHITE.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(4);
        celda.setCellValue(ubic.getOriOrientValUbic() != null ? ubic.getOriOrientValUbic().doubleValue() : 0);
        celda.setCellStyle(estiloCelda);
        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.WHITE.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(5);
        celda.setCellValue(ubic.getOriOrientValDespachos() != null ? ubic.getOriOrientValDespachos().doubleValue() : 0);
        celda.setCellStyle(estiloCelda);
        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.WHITE.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(6);
        celda.setCellValue(ubic.getOriOrientValAulas() != null ? ubic.getOriOrientValAulas().doubleValue() : 0);
        celda.setCellStyle(estiloCelda);
        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.DARK_YELLOW.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(7);
        celda.setCellValue(ubic.getOriOrientPuntuacion() != null ? ubic.getOriOrientPuntuacion().doubleValue() : 0);
        celda.setCellStyle(estiloCelda);
        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.TAN.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(8);

        BigDecimal bd = null;
        try {
            bd = MeLanbide32Manager.getInstance().getHorasAsignadasUbicacionORI(ubic.getOriOrientUbicCod(), adapt);
        } catch (Exception ex) {

        }
        celda.setCellValue(bd != null ? bd.doubleValue() : 0);
        celda.setCellStyle(estiloCelda);
        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

        return bd != null ? bd.doubleValue() : 0.0;
    }

    private void anadirFilaTotalesResolucionHoras(HSSFWorkbook libro, HSSFSheet hoja, int numFila, int solicitadasTotales, double puntuacionTotal, double adjudicadasTotales, MeLanbide32I18n meLanbide32I18n, int idioma) {
        HSSFRow fila = hoja.createRow(numFila);

        hoja.addMergedRegion(new CellRangeAddress(numFila, numFila, 0, 1));

        HSSFCellStyle estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setWrapText(true);
        HSSFCell celda = fila.createCell(0);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionHoras.horasTotSolicitadas"));
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(1);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionHoras.horasTotSolicitadas"));
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(2);
        celda.setCellValue(Double.valueOf(solicitadasTotales));
        celda.setCellStyle(estiloCelda);
        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.LIGHT_ORANGE.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(6);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionHoras.sumatorio"));
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.LIGHT_ORANGE.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(7);
        celda.setCellValue(Double.valueOf(puntuacionTotal));
        celda.setCellStyle(estiloCelda);
        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.WHITE.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(8);
        celda.setCellValue(Double.valueOf(adjudicadasTotales));
        celda.setCellStyle(estiloCelda);
        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
    }

    public void consolidaHoras(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        int codigoOperacion = 0;
        String mensaje = "";
        try {
            try {
                mensaje = MeLanbide32Manager.getInstance().consolidaHoras(this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (mensaje != null && mensaje.equalsIgnoreCase(ConstantesMeLanbide32.OK)) {
                    codigoOperacion = 0;
                } else {
                    codigoOperacion = 1;
                }

                //Creamos la auditoría
                String codProcedimiento = null;
                try {
                    codProcedimiento = (String) request.getParameter("codProcedimiento");
                } catch (Exception ex) {

                }
                this.crearAuditoria(codOrganizacion, (UsuarioValueObject) request.getSession().getAttribute("usuario"), ConstantesMeLanbide32.COD_PROC_CONSOLIDA_HORAS, mensaje, codProcedimiento);
            } catch (Exception ex) {
                codigoOperacion = 1;
            }
        } catch (Exception ex) {
            codigoOperacion = 2;
        }
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        xmlSalida.append("<MENSAJE>");
        xmlSalida.append(mensaje);
        xmlSalida.append("</MENSAJE>");
        xmlSalida.append("</RESPUESTA>");
        try {
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }//try-catch
    }

    public void deshacerConsolidacionHoras(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        int codigoOperacion = 0;
        String mensaje = "";
        if (request.getParameter("ano") != null) {
            try {
                int ano = Integer.parseInt(request.getParameter("ano"));
                try {
                    mensaje = MeLanbide32Manager.getInstance().deshacerConsolidacionHoras(ano, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    if (mensaje != null && mensaje.equalsIgnoreCase(ConstantesMeLanbide32.OK)) {
                        codigoOperacion = 0;
                    } else {
                        codigoOperacion = 1;
                    }

                    //Creamos la auditoría
                    String codProcedimiento = null;
                    try {
                        codProcedimiento = (String) request.getParameter("codProcedimiento");
                    } catch (Exception ex) {

                    }
                    this.crearAuditoria(codOrganizacion, (UsuarioValueObject) request.getSession().getAttribute("usuario"), ConstantesMeLanbide32.COD_PROC_DESHACER_CONSOLIDACION_HORAS, mensaje, codProcedimiento);
                } catch (Exception ex) {
                    codigoOperacion = 1;
                }
            } catch (Exception ex) {
                codigoOperacion = 2;
            }
        } else {
            codigoOperacion = 3;
        }
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        xmlSalida.append("<MENSAJE>");
        xmlSalida.append(mensaje);
        xmlSalida.append("</MENSAJE>");
        xmlSalida.append("</RESPUESTA>");
        try {
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }//try-catch
    }

    public void adjudicaCentros(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        int codigoOperacion = 0;
        String[] result = null;
        String mensaje = "";
        String estado = "";
        int codIdioma = 1;
        try {
            UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
            if (usuario != null) {
                codIdioma = usuario.getIdioma();
            }
        } catch (Exception ex) {

        }
        if (request.getParameter("ano") != null) {
            try {
                int ano = Integer.parseInt(request.getParameter("ano"));
                String codProcedimiento = null;
                try {
                    codProcedimiento = (String) request.getParameter("codProcedimiento");
                } catch (Exception ex) {

                }
                try {
                    result = MeLanbide32Manager.getInstance().adjudicaCentros(ano, codProcedimiento, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    mensaje = result[0];
                    if (mensaje != null) {
                        if (mensaje != null && mensaje.equalsIgnoreCase(ConstantesMeLanbide32.OK)) {
                            codigoOperacion = 0;
                        } else if (mensaje.equalsIgnoreCase(ConstantesMeLanbide32.ERROR)) {
                            codigoOperacion = 1;
                        } else {
                            mensaje = procesarMensajeProcedimiento(mensaje, codIdioma);
                            codigoOperacion = 4;
                        }
                    } else {
                        codigoOperacion = 1;
                    }

                    if (result[1] != null && result[1].equalsIgnoreCase("1")) {
                        estado = MeLanbide32I18n.getInstance().getMensaje(codIdioma, "expedientes_en_estados_no_permitidos");
                    }

                    //Creamos la auditoría
                    this.crearAuditoria(codOrganizacion, (UsuarioValueObject) request.getSession().getAttribute("usuario"), ConstantesMeLanbide32.COD_PROC_ADJUDICA_CENTROS, mensaje, codProcedimiento);
                } catch (Exception ex) {
                    codigoOperacion = 1;
                }
            } catch (Exception ex) {
                codigoOperacion = 2;
            }
        } else {
            codigoOperacion = 3;
        }
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        xmlSalida.append("<MENSAJE>");
        xmlSalida.append(mensaje);
        xmlSalida.append("</MENSAJE>");
        if (estado != null && !estado.equals("")) {
            xmlSalida.append("<ESTADO>");
            xmlSalida.append(estado);
            xmlSalida.append("</ESTADO>");
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
            e.printStackTrace();
        }//try-catch
    }

    public void consolidaCentros(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        int codigoOperacion = 0;
        String mensaje = "";
        try {
            try {
                String codProcedimiento = null;
                try {
                    codProcedimiento = (String) request.getParameter("codProcedimiento");
                } catch (Exception ex) {

                }
                Integer ano = null;
                try {
                    String anoS = (String) request.getParameter("ano");
                    ano = (anoS != null && !anoS.equals("") ? Integer.valueOf(anoS) : null);
                } catch (Exception ex) {
                    log.error("Error al recuperar el año desde la request : " + ex.getMessage(), ex);
                }
                mensaje = MeLanbide32Manager.getInstance().consolidaCentros(codProcedimiento, ano, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (mensaje != null && mensaje.equalsIgnoreCase(ConstantesMeLanbide32.OK)) {
                    codigoOperacion = 0;
                } else {
                    codigoOperacion = 1;
                }

                //Creamos la auditoría
                this.crearAuditoria(codOrganizacion, (UsuarioValueObject) request.getSession().getAttribute("usuario"), ConstantesMeLanbide32.COD_PROC_CONSOLIDA_CENTROS, mensaje, codProcedimiento);
            } catch (Exception ex) {
                codigoOperacion = 1;
            }
        } catch (Exception ex) {
            codigoOperacion = 2;
        }
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        xmlSalida.append("<MENSAJE>");
        xmlSalida.append(mensaje);
        xmlSalida.append("</MENSAJE>");
        xmlSalida.append("</RESPUESTA>");
        try {
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }//try-catch
    }

    public void deshacerConsolidacionCentros(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        int codigoOperacion = 0;
        String mensaje = "";
        if (request.getParameter("ano") != null) {
            try {
                int ano = Integer.parseInt(request.getParameter("ano"));
                try {
                    String codProcedimiento = null;
                    try {
                        codProcedimiento = (String) request.getParameter("codProcedimiento");
                    } catch (Exception ex) {

                    }
                    mensaje = MeLanbide32Manager.getInstance().deshacerConsolidacionCentros(ano, codProcedimiento, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    if (mensaje != null && mensaje.equalsIgnoreCase(ConstantesMeLanbide32.OK)) {
                        codigoOperacion = 0;
                    } else {
                        codigoOperacion = 1;
                    }

                    //Creamos la auditoría
                    this.crearAuditoria(codOrganizacion, (UsuarioValueObject) request.getSession().getAttribute("usuario"), ConstantesMeLanbide32.COD_PROC_DESHACER_CONSOLIDACION_CENTROS, mensaje, codProcedimiento);
                } catch (Exception ex) {
                    codigoOperacion = 1;
                }
            } catch (Exception ex) {
                codigoOperacion = 2;
            }
        } else {
            codigoOperacion = 3;
        }
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        xmlSalida.append("<MENSAJE>");
        xmlSalida.append(mensaje);
        xmlSalida.append("</MENSAJE>");
        xmlSalida.append("</RESPUESTA>");
        try {
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }//try-catch
    }

    public void crearDocumentacionResolucionCentros(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        String rutaArchivoSalida = null;

        String codProcedimiento = null;
        try {
            codProcedimiento = (String) request.getParameter("codProcedimiento");
        } catch (Exception ex) {
            log.error("Se ha prodicido un Error al recupera el codigo del procedimiento al crear la documentacions de Procesos AYORI / CEMP" + ex.getMessage(), ex);
        }
        if (codProcedimiento.equals(ConstantesMeLanbide32.CODIGO_PROCEDIMIENTO_CEMP)) {
            crearDocumentacionResolucionCentrosCEMP2014(codOrganizacion, codTramite, ocurrenciaTramite, numExpediente, request, response);
        } else {
            try {
                Integer ano = new GregorianCalendar().get(Calendar.YEAR);
                AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));

                try {
                    ano = Integer.parseInt((String) request.getParameter("ano"));
                } catch (Exception ex) {

                }
                int idioma = 1;
                HttpSession session = request.getSession();
                UsuarioValueObject usuario = new UsuarioValueObject();
                try {
                    if (session != null) {
                        usuario = (UsuarioValueObject) session.getAttribute("usuario");
                        if (usuario != null) {
                            idioma = usuario.getIdioma();
                        }
                    }
                } catch (Exception ex) {

                }

                MeLanbide32I18n meLanbide32I18n = MeLanbide32I18n.getInstance();

                List<Integer> provincias = MeLanbide32Manager.getInstance().getDistintasProvDeAmbitosCentroEmpleo(adapt);
                HSSFWorkbook libro = new HSSFWorkbook();
                HSSFFont negrita = libro.createFont();
                negrita.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                ProvinciaVO prov = null;
                HSSFSheet hoja = null;
                List<AmbitoCentroEmpleoVO> ambitosProv = null;
                int numFila = 0;
                List<CeUbicacionVO> ubicacionesAmbito = null;
                EntidadVO entidad = null;
                int totalesAdjudicadas = 0;
                boolean adjudicada = false;
                for (Integer codProv : provincias) {
                    prov = MeLanbide32Manager.getInstance().getProvinciaPorCodigo(codProv, adapt);
                    if (prov != null && prov.getPrvNom() != null) {
                        hoja = libro.createSheet(prov.getPrvNom().toUpperCase());

                        hoja.setColumnWidth(0, 7525);
                        hoja.setColumnWidth(1, 7000);
                        hoja.setColumnWidth(2, 2500);
                        hoja.setColumnWidth(3, 1500);
                        hoja.setColumnWidth(4, 4000);
                        hoja.setColumnWidth(5, 7000);
                        hoja.setColumnWidth(6, 3000);
                        hoja.setColumnWidth(7, 3000);
                        hoja.setColumnWidth(8, 3500);
                        hoja.setColumnWidth(9, 3000);
                        hoja.setColumnWidth(10, 3000);

                        ambitosProv = MeLanbide32Manager.getInstance().getAmbitosPorProvincia(codProv, ano, adapt);
                        for (AmbitoCentroEmpleoVO amb : ambitosProv) {
                            //Ambito
                            DocumentacionResolucion.anadirAmbitoResolucionCentros(libro, hoja, amb, numFila, meLanbide32I18n, idioma);

                            //Cabecera 1 Tabla
                            numFila = numFila + 4;
                            anadirCabecera1ResolucionCentros(libro, hoja, numFila, meLanbide32I18n, idioma);

                            //Cabecera 2 tabla
                            numFila = numFila + 1;
                            anadirCabecera2ResolucionCentros(libro, hoja, numFila, meLanbide32I18n, idioma);

                            //Entidades
                            try {
                                ubicacionesAmbito = MeLanbide32Manager.getInstance().getUbicacionesDeAmbitoCE(amb, adapt);
                                for (CeUbicacionVO ubic : ubicacionesAmbito) {
                                    numFila = numFila + 1;
                                    entidad = MeLanbide32Manager.getInstance().getEntidadPorCodigo(ubic.getOriEntCod(), adapt);
                                    if (entidad != null) {
                                        adjudicada = anadirEntidadResolucionCentros(libro, hoja, ubic, entidad, numFila, codProcedimiento, adapt);
                                        if (adjudicada) {
                                            totalesAdjudicadas++;
                                        }
                                    }
                                }
                            } catch (Exception ex) {

                            }

                            //Totales
                            numFila = numFila + 1;
                            anadirFilaTotalesResolucionCentros(libro, hoja, numFila, ubicacionesAmbito != null ? ubicacionesAmbito.size() : 0, totalesAdjudicadas, meLanbide32I18n, idioma);
                            numFila = numFila + 4;
                            totalesAdjudicadas = 0;
                        }
                    }
                    numFila = 0;
                }

                FileInputStream istr = null;
                try {
                    File directorioTemp = new File(m_Conf.getString("PDF.base_dir"));
                    File informe = File.createTempFile("resolucionCentros", ".xls", directorioTemp);

                    FileOutputStream archivoSalida = new FileOutputStream(informe);
                    libro.write(archivoSalida);
                    archivoSalida.close();

                    rutaArchivoSalida = informe.getAbsolutePath();

                    istr = new FileInputStream(rutaArchivoSalida);

                    BufferedInputStream bstr = new BufferedInputStream(istr); // promote

                    int size = (int) informe.length();
                    byte[] data = new byte[size];
                    bstr.read(data, 0, size);
                    bstr.close();

                    response.setContentType("application/vnd.ms-excel");
                    response.setHeader("Content-Disposition", "inline; filename=" + informe.getName());
                    response.setHeader("Content-Transfer-Encoding", "binary");
                    response.setContentLength(data.length);
                    response.getOutputStream().write(data, 0, data.length);
                    response.getOutputStream().flush();
                    response.getOutputStream().close();

                    //Creamos la auditoría
                    this.crearAuditoria(codOrganizacion, (UsuarioValueObject) request.getSession().getAttribute("usuario"), ConstantesMeLanbide32.COD_PROC_DOCUMENTACION_CENTROS, null, codProcedimiento);
                } catch (Exception ioe) {
                } finally {
                    istr.close();
                }
            } catch (Exception ex) {
                log.error("Error al procesar peticion" + ex.getMessage(), ex);
            }
        }

    }

    public void crearDocumentacionResolucionCentrosCEMP2014(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        String rutaArchivoSalida = null;
        try {
            Integer ano = new GregorianCalendar().get(Calendar.YEAR);
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));

            String codProcedimiento = null;
            try {
                codProcedimiento = (String) request.getParameter("codProcedimiento");
            } catch (Exception ex) {

            }
            try {
                ano = Integer.parseInt((String) request.getParameter("ano"));
            } catch (Exception ex) {

            }
            int idioma = 1;
            HttpSession session = request.getSession();
            UsuarioValueObject usuario = new UsuarioValueObject();
            try {
                if (session != null) {
                    usuario = (UsuarioValueObject) session.getAttribute("usuario");
                    if (usuario != null) {
                        idioma = usuario.getIdioma();
                    }
                }
            } catch (Exception ex) {

            }

            MeLanbide32I18n meLanbide32I18n = MeLanbide32I18n.getInstance();
            List<Integer> provincias = MeLanbide32Manager.getInstance().getDistintasProvDeAmbitosCentroEmpleo(adapt);
            HSSFWorkbook libro = new HSSFWorkbook();

            try {
                if (ano >= 2023) {
                    log.info("Vamos a crear documentación CEMP para el año " + ano + " Entramos en el flujo de la llamada a metodo DocumentacionResolucion.crearDocumentacionResolucionCentrosCEMPdsd2023()");
                    libro = DocumentacionResolucion.crearDocumentacionResolucionCentrosCEMPdsd2023(ano, codProcedimiento, provincias, idioma, meLanbide32I18n, adapt);
                } else if (ano >= 2017 && ano < 2023) {
                    log.info("Vamos a crear documentación CEMP para el año " + ano + " Entramos en el flujo de la llamada a metodo DocumentacionResolucion.crearDocumentacionResolucionCentrosCEMPdsd2017()");
                    libro = DocumentacionResolucion.crearDocumentacionResolucionCentrosCEMPdsd2017(ano, codProcedimiento, provincias, idioma, meLanbide32I18n, adapt);
                } else {
                    log.info("Vamos a crear documentación CEMP para el año " + ano + " Entramos en el flujo de la llamada a metodo DocumentacionResolucion.crearDocumentacionResolucionCentrosCEMP2014()");
                    libro = DocumentacionResolucion.crearDocumentacionResolucionCentrosCEMP2014(ano, codProcedimiento, provincias, idioma, meLanbide32I18n, adapt);
                }
            } catch (Exception e) {
                log.error("Error al generar el fichero de resolucion provisional CEMP : " + e.getMessage(), e);
            }

            /*
            HSSFFont negrita = libro.createFont();
            negrita.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            ProvinciaVO prov = null;
            HSSFSheet hoja = null;
            List<AmbitoCentroEmpleoVO> ambitosProv = null;
            int numFila = 0;
            List<CeUbicacionVO> ubicacionesAmbito = null;
            EntidadVO entidad = null;
            int totalesAdjudicadas = 0;
            boolean adjudicada = false;
            for(Integer codProv : provincias)
            {
                prov = MeLanbide32Manager.getInstance().getProvinciaPorCodigo(codProv, adapt);
                if(prov != null && prov.getPrvNom() != null)
                {
                    String provincia ="";
                    if(prov.getPrvNom().toUpperCase().contains("ARABA"))
                        provincia = "ARABA";
                    else
                        provincia = prov.getPrvNom().toUpperCase();
                    hoja = libro.createSheet(provincia);
                    
                    hoja.setColumnWidth(0, 7525);
                    hoja.setColumnWidth(1, 7000);
                    hoja.setColumnWidth(2, 2500);
                    hoja.setColumnWidth(3, 1500);
                    hoja.setColumnWidth(4, 4000);
                    hoja.setColumnWidth(5, 7000);
                    hoja.setColumnWidth(6, 3000);
                    hoja.setColumnWidth(7, 3000);
                    hoja.setColumnWidth(8, 3500);
                    hoja.setColumnWidth(9, 4000);
                    hoja.setColumnWidth(10, 3000);
                    hoja.setColumnWidth(11, 4500);
                    hoja.setColumnWidth(12, 15000);
                    
                    ambitosProv = MeLanbide32Manager.getInstance().getAmbitosPorProvincia(codProv, ano, adapt);
                    for(AmbitoCentroEmpleoVO amb : ambitosProv)
                    {
                        //Ambito
                        anadirAmbitoResolucionCentros(libro, hoja, amb, numFila, meLanbide32I18n, idioma);
                        
                        //Cabecera 1 Tabla
                        numFila = numFila + 4;
                        anadirCabecera1ResolucionCentrosCEMP2014(libro, hoja, numFila, meLanbide32I18n, idioma);
                        
                        //Cabecera 2 tabla
                        numFila = numFila + 1;
                        anadirCabecera2ResolucionCentrosCEMP2014(libro, hoja, numFila, meLanbide32I18n, idioma);
                        
                        //Entidades
                        try
                        {
                            ubicacionesAmbito = MeLanbide32Manager.getInstance().getUbicacionesDeAmbitoCEMP2014(amb, adapt);
                            for(CeUbicacionVO ubic : ubicacionesAmbito)
                            {
                                numFila = numFila + 1;
                                entidad = MeLanbide32Manager.getInstance().getEntidadPorCodigo(ubic.getOriEntCod(), adapt);
                                if(entidad != null)
                                {
                                    adjudicada = anadirEntidadResolucionCentrosCEMP2014(libro, hoja, ubic, entidad, numFila, codProcedimiento, adapt);
                                    if(adjudicada)
                                    {
                                        totalesAdjudicadas++;
                                    }
                                }
                            }
                        }
                        catch(Exception ex)
                        {
                            
                        }
                        
                        //Totales
                        numFila = numFila + 1;
                        anadirFilaTotalesResolucionCentrosCEMP2014(libro, hoja, numFila, ubicacionesAmbito != null ? ubicacionesAmbito.size() : 0,totalesAdjudicadas, meLanbide32I18n, idioma);
                        numFila = numFila + 4;
                        totalesAdjudicadas = 0;
                    }
                }
                numFila = 0;
            }
             */
            FileInputStream istr = null;
            try {
                File directorioTemp = new File(m_Conf.getString("PDF.base_dir"));
                File informe = File.createTempFile("resolucionCentros", ".xls", directorioTemp);

                FileOutputStream archivoSalida = new FileOutputStream(informe);
                libro.write(archivoSalida);
                archivoSalida.close();

                rutaArchivoSalida = informe.getAbsolutePath();

                istr = new FileInputStream(rutaArchivoSalida);

                BufferedInputStream bstr = new BufferedInputStream(istr); // promote

                int size = (int) informe.length();
                byte[] data = new byte[size];
                bstr.read(data, 0, size);
                bstr.close();

                response.setContentType("application/vnd.ms-excel");
                response.setHeader("Content-Disposition", "inline; filename=" + informe.getName());
                response.setHeader("Content-Transfer-Encoding", "binary");
                response.setContentLength(data.length);
                response.getOutputStream().write(data, 0, data.length);
                response.getOutputStream().flush();
                response.getOutputStream().close();

                //Creamos la auditoría
                this.crearAuditoria(codOrganizacion, (UsuarioValueObject) request.getSession().getAttribute("usuario"), ConstantesMeLanbide32.COD_PROC_DOCUMENTACION_CENTROS, null, codProcedimiento);
            } catch (Exception ioe) {
                log.error("Error generando fichero resolucion horas." + ioe.getMessage(), ioe);
            } finally {
                istr.close();
            }
        } catch (Exception ex) {
            log.error("Error al procesar peticion" + ex.getMessage(), ex);
        }
    }

    private void anadirCabecera1ResolucionCentros(HSSFWorkbook libro, HSSFSheet hoja, int numFila, MeLanbide32I18n meLanbide32I18n, int idioma) {
        HSSFRow fila = hoja.createRow(numFila);

        hoja.addMergedRegion(new CellRangeAddress(numFila, numFila, 3, 8));

        HSSFCellStyle estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        HSSFCell celda = fila.createCell(3);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionHoras.valoracion"));
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(4);
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(5);
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(6);
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(7);
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(8);
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(9);
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.WHITE.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(10);
        celda.setCellStyle(estiloCelda);
    }

    private void anadirCabecera2ResolucionCentros(HSSFWorkbook libro, HSSFSheet hoja, int numFila, MeLanbide32I18n meLanbide32I18n, int idioma) {
        HSSFRow fila = hoja.createRow(numFila);
        fila.setHeight((short) 800);

        hoja.addMergedRegion(new CellRangeAddress(numFila, numFila, 0, 1));

        HSSFCellStyle estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        HSSFCell celda = fila.createCell(0);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionCentros.entColaboradoras"));
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(1);
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(2);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionCentros.centroEspecial"));
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(3);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionCentros.admLocal"));
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(4);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionCentros.supramun"));
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(5);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionCentros.especialidad"));
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(6);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionCentros.acolocacion"));
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(7);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionCentros.numTrab"));
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(8);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionCentros.numTrabDisc"));
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(9);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionCentros.anosTray"));
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.LIME.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(10);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionCentros.adjudicado"));
        celda.setCellStyle(estiloCelda);
    }

    private boolean anadirEntidadResolucionCentros(HSSFWorkbook libro, HSSFSheet hoja, CeUbicacionVO ubic, EntidadVO entidad, int numFila, String codProcedimiento, AdaptadorSQLBD adapt) {
        HSSFRow fila = hoja.createRow(numFila);

        hoja.addMergedRegion(new CellRangeAddress(numFila, numFila, 0, 1));

        HSSFCellStyle estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.WHITE.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setWrapText(true);
        HSSFCell celda = fila.createCell(0);
        String nomEnt = entidad != null && entidad.getOriEntNom() != null ? entidad.getOriEntNom() : "";
        nomEnt += nomEnt != null && !nomEnt.equals("") ? (ubic.getOriCeDireccion() != null && !ubic.getOriCeDireccion().equals("") ? " - " : "") : "";
        nomEnt += ubic != null && ubic.getOriCeDireccion() != null && !ubic.getOriCeDireccion().equals("") ? ubic.getOriCeDireccion() : "";

        celda.setCellValue(nomEnt);
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.WHITE.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(1);
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.WHITE.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(2);
        celda.setCellValue(ubic.getOriCeEspecial() != null ? ubic.getOriCeEspecial() : "");
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.WHITE.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(3);
        if (codProcedimiento != null && codProcedimiento.equalsIgnoreCase(ConstantesMeLanbide32.CODIGO_PROCEDIMIENTO_AYORI)) {
            celda.setCellValue(entidad.getOriEntAdmLocal() != null ? entidad.getOriEntAdmLocal() : "");
        } else if (codProcedimiento != null && codProcedimiento.equalsIgnoreCase(ConstantesMeLanbide32.CODIGO_PROCEDIMIENTO_CEMP)) {
            celda.setCellValue(entidad.getOriEntAdmLocalVal() != null ? entidad.getOriEntAdmLocalVal() : "");
        } else {
            //Por defecto tomamos AYORI
            celda.setCellValue(entidad.getOriEntAdmLocal() != null ? entidad.getOriEntAdmLocal() : "");
        }
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.WHITE.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(4);
        if (codProcedimiento != null && codProcedimiento.equalsIgnoreCase(ConstantesMeLanbide32.CODIGO_PROCEDIMIENTO_AYORI)) {
            celda.setCellValue(entidad.getOriEntSupramun() != null ? entidad.getOriEntSupramun() : "");
        } else if (codProcedimiento != null && codProcedimiento.equalsIgnoreCase(ConstantesMeLanbide32.CODIGO_PROCEDIMIENTO_CEMP)) {
            celda.setCellValue(entidad.getOriEntSupramunVal() != null ? entidad.getOriEntSupramunVal() : "");
        } else {
            //Por defecto tomamos AYORI
            celda.setCellValue(entidad.getOriEntSupramun() != null ? entidad.getOriEntSupramun() : "");
        }
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.WHITE.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(5);
        if (codProcedimiento != null && codProcedimiento.equalsIgnoreCase(ConstantesMeLanbide32.CODIGO_PROCEDIMIENTO_AYORI)) {
            if (entidad.getOriExpCod() != null) {
                String codEspOtros = ConfigurationParameter.getParameter("codEspecialidadOtros", ConstantesMeLanbide32.FICHERO_CONF_PANTALLAS);
                if (codEspOtros != null && codEspOtros.equals(entidad.getOriExpCod().toString())) {
                    celda.setCellValue(entidad.getOriEntOtros() != null ? entidad.getOriEntOtros() : "");
                } else {
                    EspecialidadVO especialidad = null;
                    try {
                        especialidad = MeLanbide32Manager.getInstance().getEspecialidadPorCodigo(entidad.getOriExpCod(), adapt);

                    } catch (Exception ex) {

                    }
                    celda.setCellValue(especialidad != null && especialidad.getOriEspDesc() != null ? especialidad.getOriEspDesc() : "");
                }
            }
        } else if (codProcedimiento != null && codProcedimiento.equalsIgnoreCase(ConstantesMeLanbide32.CODIGO_PROCEDIMIENTO_CEMP)) {
            if (entidad.getOriExpCodVal() != null) {
                String codEspOtros = ConfigurationParameter.getParameter("codEspecialidadOtros", ConstantesMeLanbide32.FICHERO_CONF_PANTALLAS);
                if (codEspOtros != null && codEspOtros.equals(entidad.getOriExpCodVal().toString())) {
                    celda.setCellValue(entidad.getOriEntOtrosVal() != null ? entidad.getOriEntOtrosVal() : "");
                } else {
                    EspecialidadVO especialidad = null;
                    try {
                        especialidad = MeLanbide32Manager.getInstance().getEspecialidadPorCodigo(entidad.getOriExpCodVal(), adapt);

                    } catch (Exception ex) {

                    }
                    celda.setCellValue(especialidad != null && especialidad.getOriEspDesc() != null ? especialidad.getOriEspDesc() : "");
                }
            }
        } else {
            //Por defecto tomamos AYORI
            if (entidad.getOriExpCod() != null) {
                String codEspOtros = ConfigurationParameter.getParameter("codEspecialidadOtros", ConstantesMeLanbide32.FICHERO_CONF_PANTALLAS);
                if (codEspOtros != null && codEspOtros.equals(entidad.getOriExpCod().toString())) {
                    celda.setCellValue(entidad.getOriEntOtros() != null ? entidad.getOriEntOtros() : "");
                } else {
                    EspecialidadVO especialidad = null;
                    try {
                        especialidad = MeLanbide32Manager.getInstance().getEspecialidadPorCodigo(entidad.getOriExpCod(), adapt);

                    } catch (Exception ex) {

                    }
                    celda.setCellValue(especialidad != null && especialidad.getOriEspDesc() != null ? especialidad.getOriEspDesc() : "");
                }
            }
        }
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.WHITE.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(6);
        if (codProcedimiento != null && codProcedimiento.equalsIgnoreCase(ConstantesMeLanbide32.CODIGO_PROCEDIMIENTO_AYORI)) {
            celda.setCellValue(entidad.getOriEntAcolocacion() != null ? entidad.getOriEntAcolocacion() : "");
        } else if (codProcedimiento != null && codProcedimiento.equalsIgnoreCase(ConstantesMeLanbide32.CODIGO_PROCEDIMIENTO_CEMP)) {
            celda.setCellValue(entidad.getOriEntAcolocacionVal() != null ? entidad.getOriEntAcolocacionVal() : "");
        } else {
            //Por defecto tomamos AYORI
            celda.setCellValue(entidad.getOriEntAcolocacion() != null ? entidad.getOriEntAcolocacion() : "");
        }
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.DARK_YELLOW.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(7);
        if (codProcedimiento != null && codProcedimiento.equalsIgnoreCase(ConstantesMeLanbide32.CODIGO_PROCEDIMIENTO_AYORI)) {
            celda.setCellValue(entidad.getOriEntNumtrab() != null ? entidad.getOriEntNumtrab().doubleValue() : 0);
        } else if (codProcedimiento != null && codProcedimiento.equalsIgnoreCase(ConstantesMeLanbide32.CODIGO_PROCEDIMIENTO_CEMP)) {
            celda.setCellValue(entidad.getOriEntNumtrabVal() != null ? entidad.getOriEntNumtrabVal().doubleValue() : 0);
        } else {
            //Por defecto tomamos AYORI
            celda.setCellValue(entidad.getOriEntNumtrab() != null ? entidad.getOriEntNumtrab().doubleValue() : 0);
        }
        celda.setCellStyle(estiloCelda);
        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.TAN.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(8);
        if (codProcedimiento != null && codProcedimiento.equalsIgnoreCase(ConstantesMeLanbide32.CODIGO_PROCEDIMIENTO_AYORI)) {
            celda.setCellValue(entidad.getOriEntNumtrabDisc() != null ? entidad.getOriEntNumtrabDisc().doubleValue() : 0);
        } else if (codProcedimiento != null && codProcedimiento.equalsIgnoreCase(ConstantesMeLanbide32.CODIGO_PROCEDIMIENTO_CEMP)) {
            celda.setCellValue(entidad.getOriEntNumtrabDiscVal() != null ? entidad.getOriEntNumtrabDiscVal().doubleValue() : 0);
        } else {
            //Por defecto tomamos AYORI
            celda.setCellValue(entidad.getOriEntNumtrabDisc() != null ? entidad.getOriEntNumtrabDisc().doubleValue() : 0);
        }
        celda.setCellStyle(estiloCelda);
        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.TAN.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(9);
        int anosTray = 0;
        if (entidad != null) {
            try {
                anosTray = MeLanbide32Manager.getInstance().getAnosTrayectoriaCE(entidad, adapt);
            } catch (Exception ex) {
                log.error(ex.getMessage());
            }
        }
        celda.setCellValue(anosTray);
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.TAN.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(10);
        String adjudicada = null;
        boolean estaAdj = false;
        try {
            adjudicada = MeLanbide32Manager.getInstance().getUbicacionAdjudicadaCE(ubic.getOriCeUbicCod(), adapt);
            if (adjudicada != null && adjudicada.equalsIgnoreCase(ConstantesMeLanbide32.SI)) {
                estaAdj = true;
            }
        } catch (Exception ex) {

        }
        celda.setCellValue(adjudicada != null && !adjudicada.equals("") ? adjudicada : ConstantesMeLanbide32.NO);
        celda.setCellStyle(estiloCelda);
        return estaAdj;
    }

    private void anadirFilaTotalesResolucionCentros(HSSFWorkbook libro, HSSFSheet hoja, int numFila, int solicitadosTotales, int adjudicadosTotales, MeLanbide32I18n meLanbide32I18n, int idioma) {
        HSSFRow fila = hoja.createRow(numFila);

        hoja.addMergedRegion(new CellRangeAddress(numFila, numFila, 0, 1));

        HSSFCellStyle estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setWrapText(true);
        HSSFCell celda = fila.createCell(0);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionCentros.centrosTotSolicitadas"));
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(1);
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(2);
        celda.setCellValue(Double.valueOf(solicitadosTotales));
        celda.setCellStyle(estiloCelda);
        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.WHITE.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(10);
        celda.setCellValue(Double.valueOf(adjudicadosTotales));
        celda.setCellStyle(estiloCelda);
        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
    }

    private int actualizarValoracionUbicaciones(EntidadVO entidad, AdaptadorSQLBD adapt) {
        try {
            List<OriUbicacionVO> uList = MeLanbide32Manager.getInstance().getUbicacionesDeEntidadORI(entidad, adapt);
            //Años trayectoria
            Integer anos = entidad.getOriEntTrayectoriaVal();
            anos = anos - 2;
            BigDecimal total = new BigDecimal("0.0");
            if (uList.size() > 0) {
                for (OriUbicacionVO ubic : uList) {
                    total = this.calcularValoracion(entidad.getOriEntTrayectoriaVal(), ubic.getOriOrientValUbic(), ubic.getOriOrientDespachosValidados(), ubic.getOriOrientAulaGrupalValidada());
                    ubic.setOriOrientValTray(anos);
                    ubic.setOriOrientPuntuacion(total);
                }
                int actualizados = MeLanbide32Manager.getInstance().modificarUbicacionesORI(uList, adapt);
                return actualizados;
            } else {
                return 1;
            }
        } catch (Exception ex) {

        }
        return 0;
    }

    private BigDecimal calcularValoracion(Integer trayectoriaVal, BigDecimal ubicVal, Integer numDespachos, String aulaVal) {
        Double total = 0.0;
        //Trayectoria
        try {
            trayectoriaVal = trayectoriaVal - 2;
            if (trayectoriaVal < 0) {
                trayectoriaVal = 0;
            }
            total += trayectoriaVal;
        } catch (Exception ex) {

        }

        //Ubicacion
        try {
            Double ubicacion = ubicVal.doubleValue();
            total += ubicacion;
        } catch (Exception ex) {

        }

        //Numero despachos
        try {
            numDespachos = (numDespachos - 1) * 2;
            if (numDespachos < 0) {
                numDespachos = 0;
            }
            if (numDespachos > 4) {
                numDespachos = 4;
            }
            total += numDespachos;
        } catch (Exception ex) {

        }

        //Aula grupal
        try {
            if (aulaVal != null && aulaVal.equalsIgnoreCase(ConstantesMeLanbide32.SI)) {
                total += 1;
            }
        } catch (Exception ex) {

        }
        return new BigDecimal(total.toString());
    }

    private String procesarMensajeProcedimiento(String mensaje, Integer idioma) {
        String mensajeAux = new String(mensaje);
        mensajeAux = mensajeAux.toLowerCase();
        mensajeAux = mensajeAux.replaceAll(" ", "_");
        try {
            MeLanbide32I18n mm = MeLanbide32I18n.getInstance();
            mensajeAux = mm.getMensaje(idioma, mensajeAux);
            mensaje = mensajeAux;
        } catch (Exception ex) {

        }
        return mensaje;
    }

    private void crearAuditoria(int codOrganizacion, UsuarioValueObject usuario, int codProceso, String resultado, String codProcedimiento) throws Exception {
        if (usuario != null) {
            AuditoriaVO aud = new AuditoriaVO();
            aud.setUsuCod(usuario.getIdUsuario());
            aud.setProcedimiento(codProceso);
            aud.setResultado(resultado);
            aud.setCodProcedimiento(codProcedimiento);
            MeLanbide32Manager.getInstance().crearAuditoria(aud, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        }
    }

    private String getDomicilioAsString(DomicilioVO dom, TipoViaVO tipoVia, ViaVO via) {
        String descTipoVia = tipoVia != null ? tipoVia.getDes() : null;
        String descVia = via != null ? via.getNom() : null;
        Integer numDesde = dom != null ? dom.getNud() : null;
        String letraDesde = dom != null ? dom.getLed() : null;
        Integer numHasta = dom != null ? dom.getNuh() : null;
        String letraHasta = dom != null ? dom.getLeh() : null;
        String bloque = dom != null ? dom.getBlq() : null;
        String portal = dom != null ? dom.getPor() : null;
        String escalera = dom != null ? dom.getEsc() : null;
        String planta = dom != null ? dom.getPlt() : null;
        String puerta = dom != null ? dom.getPta() : null;
        String domicilio = "";
        domicilio = (descTipoVia != null && !descTipoVia.equals("")) ? domicilio + descTipoVia + " " : domicilio;
        domicilio = (descVia != null && !descVia.equals("")) ? domicilio + descVia + " " : domicilio;
        domicilio = (numDesde != null && !numDesde.equals("")) ? domicilio + " " + numDesde : domicilio;
        domicilio = (letraDesde != null && !letraDesde.equals("")) ? domicilio + " " + letraDesde + " " : domicilio;
        domicilio = (numHasta != null && !numHasta.equals("")) ? domicilio + " " + numHasta : domicilio;
        domicilio = (letraHasta != null && !letraHasta.equals("")) ? domicilio + " " + letraHasta : domicilio;
        domicilio = (bloque != null && !bloque.equals("")) ? domicilio + " Bl. " + bloque : domicilio;
        domicilio = (portal != null && !portal.equals("")) ? domicilio + " Portal " + portal : domicilio;
        domicilio = (escalera != null && !escalera.equals("")) ? domicilio + " Esc. " + escalera : domicilio;
        domicilio = (planta != null && !planta.equals("")) ? domicilio + " " + planta + "º " : domicilio;
        domicilio = (puerta != null && !puerta.equals("")) ? domicilio + puerta : domicilio;
        return domicilio;
    }

    private List<OriUbicacionVO> ubicacionesQueNoEstanEnResProvAlFinal(List<OriUbicacionVO> lista, AdaptadorSQLBD adapt) throws Exception {

        List<OriUbicacionVO> retList = new ArrayList<OriUbicacionVO>();
        for (int i = 0; i < lista.size(); i++) {
            retList.add(i, lista.get(i));
        }

        OriUbicacionVO act = null;
        int pos = 0;
        OriUbicacionVO ult = retList.get(retList.size() - 1);
        boolean parada = false;
        BigDecimal bd = null;
        while (pos < retList.size() && !parada) {
            act = retList.get(pos);
            if (act.getOriEntCod().intValue() == ult.getOriEntCod().intValue()) {
                parada = true;
            }

            try {
                bd = MeLanbide32Manager.getInstance().getHorasAsignadasUbicacionORI(act.getOriOrientUbicCod(), adapt);
            } catch (Exception ex) {

            }
            if (bd == null || bd.intValue() == 0) {
                retList.remove(pos);
                act.setOriOrientPuntuacion(new BigDecimal("0"));
                retList.add(retList.size(), act);
//                if(pos > 0)
//                {
//                    pos--;
//                }
            } else {
                pos++;
            }
            bd = null;
        }

        return retList;
    }

    private String cargarSubpestanaAmbitoCEMP(int codOrganizacion, String numExpediente, AdaptadorSQLBD adapt, HttpServletRequest request) {
        try {
            List<FilaAmbitoSolicitadoCempVO> listaAmbitosSolicitadosCEMP = new ArrayList<FilaAmbitoSolicitadoCempVO>();
            listaAmbitosSolicitadosCEMP = MeLanbide32Manager.getInstance().getAmbitosSolicitadosCEMP(numExpediente, adapt);
            request.setAttribute("listaAmbitosSolicitadosCEMP", listaAmbitosSolicitadosCEMP);
        } catch (Exception e) {
            log.error("Error al preparar los datos para la pesatana de Ambitos Solictados CEMP : " + e.getMessage(), e);
        }
        return "/jsp/extension/melanbide32/centroempleo/ambito/ambitosSolicitadosCEMP.jsp";
    }
    
    public String cargarAltaEdicionAmbitoSolicitadoCEMP(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        try {
            String esAmbiSolEdicion = request.getParameter("esAmbiSolEdicion");
            String idAmbitoSolicitado = request.getParameter("idAmbitoSolicitado");
            String oriAmbCeEntCod = request.getParameter("oriAmbCeEntCod");
            request.setAttribute("nuevo", esAmbiSolEdicion);
            request.setAttribute("oriAmbCeEntCod", oriAmbCeEntCod);
            cargarCombosNuevaAmbitoSolicitadoCEMP(codOrganizacion, request);
            if (idAmbitoSolicitado != null && idAmbitoSolicitado != "") {
                AmbitoSolicitadoCempVO ambiSol = new AmbitoSolicitadoCempVO();
                ambiSol.setOriAmbCeCodId(Integer.parseInt(idAmbitoSolicitado));
                ambiSol.setOriAmbSolNumExp(numExpediente);
                ambiSol = MeLanbide32Manager.getInstance().getAmbitoSolicitadoCEMPPorCodigo(ambiSol, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (ambiSol != null) {
                    request.setAttribute("ambitoSoliModif", ambiSol);
                }
            }
        } catch (Exception ex) {
            log.error("cargarAltaEdicionAmbitoSolicitadoCEMP - " + ex.getMessage(), ex);
        }
        return "/jsp/extension/melanbide32/centroempleo/ambito/nuevoAmbitoSolicitadoCEMP.jsp";
    }
    public void guardarAmbitoSolicitadoCEMP(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<FilaAmbitoSolicitadoCempVO> ambitos = new ArrayList<FilaAmbitoSolicitadoCempVO>();
        EntidadVO ent = null;
        AmbitoSolicitadoCempVO ambitSol = null;
        try {   
            //Recojo los parametros
            Integer oriAmbCeCodId=null;
            Integer oriAmbCeCod=null;
            Integer oriAmbCeEntCod=null;
            Integer oriAmbCeAnoconv=null;
            String oriAmbCeAmbito=null;
            Integer oriAmbCeTipoAmbito=null;
            Integer oriAmbCeNumce=null;
            String parametro = null;
 
            try {
                parametro = request.getParameter("oriAmbCeCodId");
                if (parametro != null && !parametro.equals("")) {
                    oriAmbCeCodId = Integer.parseInt(parametro);
                }
                parametro = request.getParameter("oriAmbCeCod");
                if (parametro != null && !parametro.equals("")) {
                    oriAmbCeCod = Integer.parseInt(parametro);
                }

                parametro = request.getParameter("oriAmbCeEntCod");
                if (parametro != null && !parametro.equals("")) {
                    oriAmbCeEntCod = Integer.parseInt(parametro);
                }

                parametro = request.getParameter("oriAmbCeAnoconv");
                if (parametro != null && !parametro.equals("")) {
                    oriAmbCeAnoconv = Integer.parseInt(parametro);
                }

                parametro = request.getParameter("oriAmbCeAmbito");
                if (parametro != null && !parametro.equals("")) {
                    oriAmbCeAmbito = parametro;
                }
                if (parametro != null && !parametro.equals("")) {
                    oriAmbCeAmbito = parametro;
                }
                parametro = request.getParameter("oriAmbCeTipoAmbito");
                if (parametro != null && !parametro.equals("")) {
                    oriAmbCeTipoAmbito = Integer.parseInt(parametro);
                }
                parametro = request.getParameter("oriAmbCeNumce");
                if (parametro != null && !parametro.equals("")) {
                    oriAmbCeNumce = Integer.parseInt(parametro);
                }

            } catch (Exception ex) {
                codigoOperacion = "3";
            }

            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));

            ambitSol = new AmbitoSolicitadoCempVO();
            ambitSol.setOriAmbCeCodId(oriAmbCeCodId);
            ambitSol.setOriAmbCeCod(oriAmbCeCod);
            ambitSol.setOriAmbCeEntCod(oriAmbCeEntCod);
            ambitSol.setOriAmbCeAnoconv(oriAmbCeAnoconv);
            ambitSol.setOriAmbCeAmbito(oriAmbCeAmbito);
            ambitSol.setOriAmbCeTipoAmbito(oriAmbCeTipoAmbito);
            ambitSol.setOriAmbCeNumce(oriAmbCeNumce);
            ambitSol.setOriAmbSolNumExp(numExpediente);

            try {
                //Guardo los datos
                ambitSol = MeLanbide32Manager.getInstance().guardarAmbitoSolicitadoCEMP(ambitSol, adapt);

                if (ambitSol == null) {
                    codigoOperacion = "1";
                } else {
                    ambitos = MeLanbide32Manager.getInstance().getAmbitosSolicitadosCEMP(numExpediente, adapt);
                }
            } catch (Exception ex) {
                log.error("Error : " + ex.getMessage(), ex);
                codigoOperacion = "1";
            }
        } catch (Exception ex) {
            log.error("Error : " + ex.getMessage(), ex);
            codigoOperacion = "2";
        }
        escribirResultadoGuardarEliminarAmbitoSolicitadoCempResponse(codigoOperacion, ambitos, response);
    }
    
    public void eliminarAmbitoSolicitadoCEMP(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<FilaAmbitoSolicitadoCempVO> filas = new ArrayList<FilaAmbitoSolicitadoCempVO>();
        try {
            String idAmbitoSolicitado = request.getParameter("oriAmbCeCodId");
            try {
                AmbitoSolicitadoCempVO ambiSol = new AmbitoSolicitadoCempVO();
                AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                ambiSol.setOriAmbCeCodId(idAmbitoSolicitado != null && idAmbitoSolicitado != ("") ? Integer.parseInt(idAmbitoSolicitado) : null);
                ambiSol.setOriAmbSolNumExp(numExpediente);
                MeLanbide32Manager.getInstance().eliminarAmbitoSolicitadoCEMP(ambiSol, adaptador);
                filas = MeLanbide32Manager.getInstance().getAmbitosSolicitadosCEMP(numExpediente, adaptador);
            } catch (Exception ex) {
                log.error("Error al eliminar un ambito Solicitado trayectoria  actividades", ex);
                codigoOperacion = "1";
            }
        } catch (Exception ex) {
            log.error("Error al eliminar un ambito solicitado ", ex);
            codigoOperacion = "2";
        }
        escribirResultadoGuardarEliminarAmbitoSolicitadoCempResponse(codigoOperacion, filas, response);
    }

    private void escribirResultadoGuardarEliminarAmbitoSolicitadoCempResponse(String codigoOperacion, List<FilaAmbitoSolicitadoCempVO> ambitos, HttpServletResponse response) {
        StringBuilder xmlSalida = new StringBuilder();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        for (FilaAmbitoSolicitadoCempVO fila : ambitos) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ORI_AMB_CE_COD_ID>");
            xmlSalida.append(fila.getOriAmbCeCodId());
            xmlSalida.append("</ORI_AMB_CE_COD_ID>");
            xmlSalida.append("<ORI_AMB_CE_COD>");
            xmlSalida.append(fila.getOriAmbCeCod());
            xmlSalida.append("</ORI_AMB_CE_COD>");
            xmlSalida.append("<ORI_AMB_CE_ENT_COD>");
            xmlSalida.append(fila.getOriAmbCeEntCod());
            xmlSalida.append("</ORI_AMB_CE_ENT_COD>");
            xmlSalida.append("<ORI_AMB_CE_ANOCONV>");
            xmlSalida.append(fila.getOriAmbCeAnoconv());
            xmlSalida.append("</ORI_AMB_CE_ANOCONV>");
            xmlSalida.append("<ORI_AMB_CE_AMBITO>");
            xmlSalida.append(fila.getOriAmbCeAmbito());
            xmlSalida.append("</ORI_AMB_CE_AMBITO>");
            xmlSalida.append("<ORI_AMB_CE_TIPO_AMBITO>");
            xmlSalida.append(fila.getOriAmbCeTipoAmbito());
            xmlSalida.append("</ORI_AMB_CE_TIPO_AMBITO>");
            xmlSalida.append("<ORI_AMB_CE_NUMCE>");
            xmlSalida.append(fila.getOriAmbCeNumce());
            xmlSalida.append("</ORI_AMB_CE_NUMCE>");
            xmlSalida.append("<ORI_AMB_SOL_NUM_EXP>");
            xmlSalida.append(fila.getOriAmbSolNumExp());
            xmlSalida.append("</ORI_AMB_SOL_NUM_EXP>");
            xmlSalida.append("<ORI_AMB_CE_TIPO_AMBITO_DESC>");
            xmlSalida.append(fila.getOriAmbCeTipoAmbitoDesc());
            xmlSalida.append("</ORI_AMB_CE_TIPO_AMBITO_DESC>");
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
        } catch (IOException e) {
            log.error(e.getStackTrace());
        }//try-catch
    }
    
    /**
     *
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @param request
     * @param response
     */
    public void getListaCriteriosEvaluacionByCodigo(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        mELANBIDE32CriteriosEvaActionController.getListaCriteriosEvaluacionByCodigo(codOrganizacion, codTramite, ocurrenciaTramite, numExpediente, request, response);
    }
    
    /**
     *
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @param request
     * @param response
     */
    public void getCriteriosEvaluacionSeleccionadoCE(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        mELANBIDE32CriteriosEvaActionController.getListaCriteriosEvaluacionCEByIdCentro(codOrganizacion, codTramite, ocurrenciaTramite, numExpediente, request, response);
    }
    
    public void getAmbitoCentroEmpleoVOByID(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        mELANBIDE32CriteriosEvaActionController.getAmbitoCentroEmpleoVOByID(codOrganizacion, codTramite, ocurrenciaTramite, numExpediente, request, response);
    }
    
}
