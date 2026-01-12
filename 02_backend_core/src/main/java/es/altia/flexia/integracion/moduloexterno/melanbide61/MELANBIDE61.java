package es.altia.flexia.integracion.moduloexterno.melanbide61;


import com.google.gson.Gson;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.melanbide61.manager.MeLanbide61Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide61.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide61.util.ConstantesMeLanbide61;
import es.altia.flexia.integracion.moduloexterno.melanbide61.util.MeLanbide61Utils;
import es.altia.flexia.integracion.moduloexterno.melanbide61.vo.ContratoRenovacionPlantillaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide61.vo.DatosEconomicosExpVO;
import es.altia.flexia.integracion.moduloexterno.melanbide61.vo.DesplegableVO;
import es.altia.flexia.integracion.moduloexterno.melanbide61.vo.FilaContratoRenovacionPlantillaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide61.vo.FilaDocumentoContableVO;
import es.altia.flexia.integracion.moduloexterno.melanbide61.vo.PersonaContratoRenovacionPlantillaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide61.vo.SubSolicVO;
import es.altia.flexia.integracion.moduloexterno.melanbide61.vo.Tercero;
import es.altia.flexia.integracion.moduloexterno.melanbide61.vo.TrabajadorCAPVValueObject;
import es.altia.flexia.integracion.moduloexterno.melanbide61.vo.ValorCampoDesplegableEstadoVO;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExternoCampoSupFactoria;
import es.altia.flexia.integracion.moduloexterno.plugin.camposuplementario.IModuloIntegracionExternoCamposFlexia;
import es.altia.technical.PortableContext;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * @author santiago.calvo
 * @version 16/08/2012 1.0 Historial de cambios: <ol> <li>santiago.calvo *
 * 28-12-2012 * Edición inicial</li> </ol>
 */
public class MELANBIDE61 extends ModuloIntegracionExterno {

    //Logger
    private static Logger log = LogManager.getLogger(MELANBIDE61.class);
    private static final SimpleDateFormat formatoFecha = new SimpleDateFormat(ConstantesMeLanbide61.FORMATO_FECHA);
    private static final MeLanbide61Utils m61Utils = new MeLanbide61Utils();
    
    // Alta Expedientes via registro platea --> MELANBIDE 42
    public void cargarExpedienteExtension(int codigoOrganizacion, String numeroExpediente, String xml) throws Exception{
        final Class cls = Class.forName("es.altia.flexia.integracion.moduloexterno.melanbide42.MELANBIDE42");
        final Object me42Class = cls.newInstance();
        final Class[] types = {int.class, String.class, String.class};
        final Method method = cls.getMethod("cargarExpedienteExtension", types);
        method.invoke(me42Class, codigoOrganizacion, numeroExpediente, xml);
    }
    
    public String cargarContratos(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.debug("----------------------------------- CARGAR CONTRATOS -----------------------------------");
        log.debug("codOrganizacion = " + codOrganizacion);
        log.debug("codTramite = " + codTramite);
        log.debug("ocurrenciaTramite = " + ocurrenciaTramite);
        log.debug("numExpediente = " + numExpediente);
        List<FilaContratoRenovacionPlantillaVO> contratosRenovacionPlantilla = new ArrayList<FilaContratoRenovacionPlantillaVO>();
        try {
            contratosRenovacionPlantilla = MeLanbide61Manager.getInstance().getContratosExpediente(numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        } catch (BDException bde) {
            java.util.logging.Logger.getLogger(MELANBIDE61.class.getName()).log(Level.SEVERE, null, bde);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(MELANBIDE61.class.getName()).log(Level.SEVERE, null, ex);
        }
        request.setAttribute("contratosRenovacionPlantilla", contratosRenovacionPlantilla);
        log.debug("----------------------------------- CARGAR CONTRATOS -----------------------------------");

        return "/jsp/extension/melanbide61/melanbide61.jsp";
    }

    public String cargarNuevoContrato(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        try {
            cargarCombos(codOrganizacion, request);
            String id = MeLanbide61Manager.getInstance().getMaxIdContrato(numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            if (!id.equals("0")) {
                Integer numContrato = Integer.parseInt(id);
                ContratoRenovacionPlantillaVO contrato = MeLanbide61Manager.getInstance().getContratoExpedientePorNumContrato(numExpediente, numContrato, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (contrato != null) {
                    request.setAttribute("contratoModif", contrato);
                }
            }
            request.setAttribute("nuevo", "1");
            Integer ejercicio = ejercicioExpediente(numExpediente);
            request.setAttribute("ejercicio", ejercicio);
        } catch (Exception ex) {
        }
        return "/jsp/extension/melanbide61/contrato.jsp?codOrganizacionModulo=" + codOrganizacion;
    }
    
    public String cargarModificarContrato(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        if (request.getParameter("idCon") != null && numExpediente != null) {
            try {
                cargarCombos(codOrganizacion, request);
                Integer numContrato = Integer.parseInt(request.getParameter("idCon"));
                ContratoRenovacionPlantillaVO contrato = MeLanbide61Manager.getInstance().getContratoExpedientePorNumContrato(numExpediente, numContrato, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (contrato != null) {
                    request.setAttribute("contratoModif", contrato);
                }
                String cerrado = request.getParameter("cerrado");
                if (cerrado != null && cerrado.equalsIgnoreCase("1")) {
                    request.setAttribute("cerrado", true);
                }
                Integer ejercicio = ejercicioExpediente(numExpediente);
                request.setAttribute("ejercicio", ejercicio);
            } catch (Exception ex) {
            }
        }
        return "/jsp/extension/melanbide61/contrato.jsp?codOrganizacionModulo=" + codOrganizacion;
    }
    
    private Integer ejercicioExpediente(String numExp){
        String ejercicio = "";
        try {
            String[] datos = numExp.split(ConstantesMeLanbide61.BARRA_SEPARADORA);
            ejercicio = datos[0];
        } catch (Exception ex) {
            log.debug("No se ha podido obtener el ejercicio al que pertenece el expediente");
        }
        
        return Integer.parseInt(ejercicio);
    }

    public void guardarContrato(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Guardando contrato");
        String codigoOperacion = "";
        DateFormat fechaF = new SimpleDateFormat("dd/MM/yyyy");
        String fecha20Formateada ="-";

        if (request.getParameter("idCon") != null && numExpediente != null && !numExpediente.equals("")) {
            AdaptadorSQLBD adapt = null;
            Connection con = null;
            try {

                Integer numContrato = Integer.parseInt(request.getParameter("idCon"));
                ContratoRenovacionPlantillaVO contrato = MeLanbide61Manager.getInstance().getContratoExpedientePorNumContrato(numExpediente, numContrato, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (contrato != null) {
                    List<PersonaContratoRenovacionPlantillaVO> personas = contrato.getPersonas();
                    PersonaContratoRenovacionPlantillaVO pers = null;
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                    String dniTerAdicional =(String)request.getParameter("txtDniPA");
                    if (personas.size()<3 && !dniTerAdicional.equals("")){
                        PersonaContratoRenovacionPlantillaVO persAdicional = new PersonaContratoRenovacionPlantillaVO();
                        persAdicional.setCodTipoPersona(1);
                        personas.add(persAdicional);                    
                    }
                    for (int i = 0; i < personas.size(); i++) {
                        pers = personas.get(i);
                        if (pers.getCodTipoPersona() != null) {
                            switch (pers.getCodTipoPersona()) {
                                case 1:
                                    pers.setNumDoc((String) request.getParameter("txtDniPS"));
                                    pers.setNombre((String) request.getParameter("txtNomPS"));
                                    pers.setApellido1((String) request.getParameter("txtApe1PS"));
                                    pers.setApellido2((String) request.getParameter("txtApe2PS"));
                                    if (request.getParameter("fechaNacimientoPS") != null && !((String) request.getParameter("fechaNacimientoPS")).equalsIgnoreCase("")) {
                                        String str = (String) request.getParameter("fechaNacimientoPS");
                                        try {
                                            pers.setFeNac(format.parse((String) request.getParameter("fechaNacimientoPS")));
                                        } catch (Exception ex) {
                                        }
                                    }
                                    if (request.getParameter("txtSexoPS") != null && !((String) request.getParameter("txtSexoPS")).equalsIgnoreCase("")) {
                                        try {
                                            String sexo = (String) request.getParameter("txtSexoPS");
                                            if (sexo.equalsIgnoreCase(ConstantesMeLanbide61.SEXO_H)) {
                                                pers.setSexo(1);
                                            } else if (sexo.equalsIgnoreCase(ConstantesMeLanbide61.SEXO_M)) {
                                                pers.setSexo(2);
                                            }
                                        } catch (Exception ex) {
                                        }
                                    }else{
                                        // NO deberia darse el caso, pero hay que guardar a null al ser obligatorio
                                        // Pero hay que guardar lo que mande el usuario
                                        pers.setSexo(null);
                                    }
                                    pers.setCnoe(request.getParameter("codCNOEPS"));
                                    if (request.getParameter("codColectivoPS") != null && !((String) request.getParameter("codColectivoPS")).equalsIgnoreCase("")) {
                                        try {
                                            pers.setColectivo(Integer.parseInt(request.getParameter("codColectivoPS")));
                                        } catch (Exception ex) {
                                        }
                                    }else pers.setColectivo(null);
                                    if (request.getParameter("fechaInConAd") != null && !((String) request.getParameter("fechaInConAd")).equalsIgnoreCase("")) {
                                        try {
                                            pers.setFecInConAd(format.parse((String) request.getParameter("fechaInConAd")));
                                        } catch (Exception ex) {
                                        }
                                    } else {
                                        pers.setFecInConAd(null);
                                    }
                                    if (request.getParameter("fechaFinConAd") != null && !((String) request.getParameter("fechaFinConAd")).equalsIgnoreCase("")) {
                                        try {
                                            pers.setFecFinConAd(format.parse((String) request.getParameter("fechaFinConAd")));
                                        } catch (Exception ex) {
                                        }
                                    } else {
                                        pers.setFecFinConAd(null);
                                    }

                                    //pers.setConvenio(request.getParameter("convenio"));
                                   /* if (!request.getParameter("dias").equals("")) {
                                        pers.setDias(request.getParameter("dias"));
                                    } else {
                                        pers.setDias("0");
                                    }
                                    if (!request.getParameter("diasF").equals("")) {
                                        pers.setDiasF(request.getParameter("diasF"));
                                    } else {
                                        pers.setDiasF("0");
                                    }*/
                                    if (!request.getParameter("diasI").equals("")) {
                                        pers.setDiasI(request.getParameter("diasI"));
                                    } else {
                                        pers.setDiasI("0");
                                    }
                                    if (!request.getParameter("diasContrato").equals("")) {
                                        pers.setDiasContrato(request.getParameter("diasContrato"));
                                    } else {
                                        pers.setDiasContrato("0");
                                    }
                                   /* if (request.getParameter("fechaPublica") != null && !request.getParameter("fechaPublica").equalsIgnoreCase("")) {
                                     try {
                                     pers.setFechaPublica(format.parse(request.getParameter("fechaPublica")));
                                     } catch (Exception ex) {
                                     }
                                     } else {
                                     pers.setFechaPublica(null);
                                     }
                                     if (request.getParameter("fechaIniContrato") != null && !request.getParameter("fechaIniContrato").equalsIgnoreCase("")) {
                                     try {
                                     pers.setFechaIniContrato(format.parse(request.getParameter("fechaIniContrato")));
                                     } catch (Exception ex) {
                                     }
                                     } else {
                                     pers.setFechaIniContrato(null);
                                     }
                                     if (request.getParameter("fechaFinContrato") != null && !request.getParameter("fechaFinContrato").equalsIgnoreCase("")) {
                                     try {
                                     pers.setFechaFinContrato(format.parse(request.getParameter("fechaFinContrato")));
                                     } catch (Exception ex) {
                                     }
                                     } else {
                                     pers.setFechaFinContrato(null);
                                     }*/
                                    if (request.getParameter("codEstudiosPS") != null && !((String) request.getParameter("codEstudiosPS")).equalsIgnoreCase("")) {
                                        try {
                                            pers.setNivelEstudios(Integer.parseInt(request.getParameter("codEstudiosPS")));
                                        } catch (Exception ex) {
                                        }
                                    }else pers.setNivelEstudios(null);
                                    
                                    if (request.getParameter("codSitPrevia2") != null && !((String) request.getParameter("codSitPrevia2")).equalsIgnoreCase("")) {
                                        try {
                                            pers.setSitPrevia(request.getParameter("codSitPrevia2"));
                                        } catch (Exception ex) {
                                        }
                                    }else pers.setSitPrevia("");
                                    break;
                                case 2:
                                    pers.setNumDoc((String) request.getParameter("txtDniPC"));
                                    pers.setNombre((String) request.getParameter("txtNomPC"));
                                    pers.setApellido1((String) request.getParameter("txtApe1PC"));
                                    pers.setApellido2((String) request.getParameter("txtApe2PC"));
                                    if (request.getParameter("fechaNacimientoPC") != null && !((String) request.getParameter("fechaNacimientoPC")).equalsIgnoreCase("")) {
                                        try {
                                            pers.setFeNac(format.parse((String) request.getParameter("fechaNacimientoPC")));
                                        } catch (Exception ex) {
                                        }
                                    }
                                    if (request.getParameter("txtSexoPC") != null && !((String) request.getParameter("txtSexoPC")).equalsIgnoreCase("")) {
                                        try {
                                            String sexo = (String) request.getParameter("txtSexoPC");
                                            if (sexo.equalsIgnoreCase(ConstantesMeLanbide61.SEXO_H)) {
                                                pers.setSexo(1);
                                            } else if (sexo.equalsIgnoreCase(ConstantesMeLanbide61.SEXO_M)) {
                                                pers.setSexo(2);
                                            }
                                        } catch (Exception ex) {
                                        }
                                    } else {
                                        // NO deberia darse el caso, pero hay que guardar a null al ser obligatorio
                                        // Pero hay que guardar lo que mande el usuario
                                        pers.setSexo(null);
                                    }
                                    pers.setFlMinusvalido(request.getParameter("checkMinusvalidoPC") != null && ((String) request.getParameter("checkMinusvalidoPC")).equalsIgnoreCase(ConstantesMeLanbide61.CHECK_S) ? ConstantesMeLanbide61.CHECK_S : ConstantesMeLanbide61.CHECK_N);
                                    pers.setFlInmigrante(request.getParameter("checkInmigPC") != null && ((String) request.getParameter("checkInmigPC")).equalsIgnoreCase(ConstantesMeLanbide61.CHECK_S) ? ConstantesMeLanbide61.CHECK_S : ConstantesMeLanbide61.CHECK_N);
                                    if (request.getParameter("codEstudiosPC") != null && !((String) request.getParameter("codEstudiosPC")).equalsIgnoreCase("")) {
                                        try {
                                            pers.setNivelEstudios(Integer.parseInt(request.getParameter("codEstudiosPC")));
                                        } catch (Exception ex) {
                                        }
                                    }else pers.setNivelEstudios(null);
                                    if (request.getParameter("codColectivoPC") != null && !((String) request.getParameter("codColectivoPC")).equalsIgnoreCase("")) {
                                        try {
                                            pers.setColectivo(Integer.parseInt(request.getParameter("codColectivoPC")));
                                        } catch (Exception ex) {
                                        }
                                    }else pers.setColectivo(null);
                                    if (request.getParameter("txtMesesDesempleoPC") != null && !((String) request.getParameter("txtMesesDesempleoPC")).equalsIgnoreCase("")) {
                                        try {
                                            pers.setMesesDesempleo(Integer.parseInt(request.getParameter("txtMesesDesempleoPC")));
                                        } catch (Exception ex) {
                                        }
                                    }
                                    pers.setFlPld(request.getParameter("checkPLDPC") != null && ((String) request.getParameter("checkPLDPC")).equalsIgnoreCase(ConstantesMeLanbide61.CHECK_S) ? ConstantesMeLanbide61.CHECK_S : ConstantesMeLanbide61.CHECK_N);
                                    pers.setFlRml(request.getParameter("checkRMLPC") != null && ((String) request.getParameter("checkRMLPC")).equalsIgnoreCase(ConstantesMeLanbide61.CHECK_S) ? ConstantesMeLanbide61.CHECK_S : ConstantesMeLanbide61.CHECK_N);
                                    pers.setFlOtr(request.getParameter("checkOtroPC") != null && ((String) request.getParameter("checkOtroPC")).equalsIgnoreCase(ConstantesMeLanbide61.CHECK_S) ? ConstantesMeLanbide61.CHECK_S : ConstantesMeLanbide61.CHECK_N);
                                    if (request.getParameter("codTipoCon") != null && !((String) request.getParameter("codTipoCon")).equalsIgnoreCase("")) {
                                        try {
                                            pers.setTipoContrato((request.getParameter("codTipoCon")));
                                        } catch (Exception ex) {
                                        }
                                    }else pers.setTipoContrato("");
                                    if (request.getParameter("fechaConPC") != null && !((String) request.getParameter("fechaConPC")).equalsIgnoreCase("")) {
                                        try {
                                            pers.setFeAlta(format.parse((String) request.getParameter("fechaConPC")));
                                        } catch (Exception ex) {
                                        }
                                    } else {
                                        pers.setFeAlta(null);
                                    }
                                    pers.setTipoJornada(request.getParameter("codTipoJornadaPC"));
                                    pers.setCnoe(request.getParameter("codCNOEPC"));
                                    if (request.getParameter("txtDuracionContratoPC") != null && !((String) request.getParameter("txtDuracionContratoPC")).equalsIgnoreCase("")) {
                                        try {
                                            pers.setDuracionContrato(Integer.parseInt(request.getParameter("txtDuracionContratoPC")));
                                        } catch (Exception ex) {
                                        }
                                    }
                                    BigDecimal retri = new BigDecimal("0");
                                    if (request.getParameter("txtRetribucionPC") != null && !((String) request.getParameter("txtRetribucionPC")).equalsIgnoreCase("")) {
                                        retri = new BigDecimal(request.getParameter("txtRetribucionPC"));
                                    }
                                    pers.setRetSalarial(retri);
                                    pers.setFondo(request.getParameter("fondo"));
                                    if (request.getParameter("codSitPrevia") != null && !((String) request.getParameter("codSitPrevia")).equalsIgnoreCase("")) {
                                        try {
                                            pers.setSitPrevia(request.getParameter("codSitPrevia"));
                                        } catch (Exception ex) {
                                        }
                                    }else pers.setSitPrevia("");
                                    pers.setConvenio(request.getParameter("convenio"));
                                    if (request.getParameter("fechaPublica") != null && !request.getParameter("fechaPublica").equalsIgnoreCase("")) {
                                        try {
                                            pers.setFechaPublica(format.parse(request.getParameter("fechaPublica")));
                                        } catch (Exception ex) {
                                        }
                                    } else {
                                        pers.setFechaPublica(null);
                                    }
                                    if (request.getParameter("fechaIniContrato") != null && !request.getParameter("fechaIniContrato").equalsIgnoreCase("")) {
                                        try {
                                            pers.setFechaIniContrato(format.parse(request.getParameter("fechaIniContrato")));
                                        } catch (Exception ex) {
                                        }
                                    } else {
                                        pers.setFechaIniContrato(null);
                                    }
                                    if (request.getParameter("fechaFinContrato") != null && !request.getParameter("fechaFinContrato").equalsIgnoreCase("")) {
                                        try {
                                            pers.setFechaFinContrato(format.parse(request.getParameter("fechaFinContrato")));
                                        } catch (Exception ex) {
                                        }
                                    } else {
                                        pers.setFechaFinContrato(null);
                                    }
                                    if (!request.getParameter("dias").equals("")) {
                                        pers.setDias(request.getParameter("dias"));
                                    } else {
                                        pers.setDias("0");
                                    }
                                    if (!request.getParameter("diasF").equals("")) {
                                        pers.setDiasF(request.getParameter("diasF"));
                                    } else {
                                        pers.setDiasF("0");
                                    }
                                    


                                    break;
                                case 3:
                                    pers.setNumDoc((String) request.getParameter("txtDniPA"));
                                    pers.setNombre((String) request.getParameter("txtNomPA"));
                                    pers.setApellido1((String) request.getParameter("txtApe1PA"));
                                    pers.setApellido2((String) request.getParameter("txtApe2PA"));
                                    if (request.getParameter("fechaNacimientoPA") != null && !((String) request.getParameter("fechaNacimientoPA")).equalsIgnoreCase("")) {
                                        try {
                                            pers.setFeNac(format.parse((String) request.getParameter("fechaNacimientoPA")));
                                        } catch (Exception ex) {
                                        }
                                    }
                                    if (request.getParameter("txtSexoPA") != null && !((String) request.getParameter("txtSexoPA")).equalsIgnoreCase("")) {
                                        try {
                                            String sexo = (String) request.getParameter("txtSexoPA");
                                            if (sexo.equalsIgnoreCase(ConstantesMeLanbide61.SEXO_H)) {
                                                pers.setSexo(1);
                                            } else if (sexo.equalsIgnoreCase(ConstantesMeLanbide61.SEXO_M)) {
                                                pers.setSexo(2);
                                            }
                                        } catch (Exception ex) {
                                        }
                                    } else {
                                        // NO deberia darse el caso, pero hay que guardar a null al ser obligatorio
                                        // Pero hay que guardar lo que mande el usuario
                                        pers.setSexo(null);
                                    }
                                    pers.setFlMinusvalido(request.getParameter("checkMinusvalidoPA") != null && ((String) request.getParameter("checkMinusvalidoPA")).equalsIgnoreCase(ConstantesMeLanbide61.CHECK_S) ? ConstantesMeLanbide61.CHECK_S : ConstantesMeLanbide61.CHECK_N);
                                    pers.setFlInmigrante(request.getParameter("checkInmigPA") != null && ((String) request.getParameter("checkInmigPA")).equalsIgnoreCase(ConstantesMeLanbide61.CHECK_S) ? ConstantesMeLanbide61.CHECK_S : ConstantesMeLanbide61.CHECK_N);
                                    if (request.getParameter("codEstudiosPA") != null && !((String) request.getParameter("codEstudiosPA")).equalsIgnoreCase("")) {
                                        try {
                                            pers.setNivelEstudios(Integer.parseInt(request.getParameter("codEstudiosPA")));
                                        } catch (Exception ex) {
                                        }
                                    }
                                    if (request.getParameter("codColectivoPA") != null && !((String) request.getParameter("codColectivoPA")).equalsIgnoreCase("")) {
                                        try {
                                            pers.setColectivo(Integer.parseInt(request.getParameter("codColectivoPA")));
                                        } catch (Exception ex) {
                                        }
                                    }
                                    if (request.getParameter("txtMesesDesempleoPA") != null && !((String) request.getParameter("txtMesesDesempleoPA")).equalsIgnoreCase("")) {
                                        try {
                                            pers.setMesesDesempleo(Integer.parseInt(request.getParameter("txtMesesDesempleoPA")));
                                        } catch (Exception ex) {
                                        }
                                    }
                                    if (request.getParameter("codTipConDurPA") != null && !((String) request.getParameter("codTipConDurPA")).equalsIgnoreCase("")) {
                                        try {
                                            pers.setTipoContrato((request.getParameter("codTipConDurPA")));
                                        } catch (Exception ex) {
                                        }
                                    }
                                    if (request.getParameter("fechaAltaPA") != null && !((String) request.getParameter("fechaAltaPA")).equalsIgnoreCase("")) {
                                        try {
                                            pers.setFeAlta(format.parse((String) request.getParameter("fechaAltaPA")));
                                        } catch (Exception ex) {
                                        }
                                    }
                                    if (request.getParameter("fechaBajaPA") != null && !((String) request.getParameter("fechaBajaPA")).equalsIgnoreCase("")) {
                                        try {
                                            pers.setFeBaja(format.parse((String) request.getParameter("fechaBajaPA")));
                                        } catch (Exception ex) {
                                        }
                                    } else {
                                        pers.setFeBaja(null);
                                    }
                                    if (request.getParameter("txtReduJorPA") != null && !((String) request.getParameter("txtReduJorPA")).equalsIgnoreCase("")) {
                                        try {
                                            pers.setPorReduJor(new BigDecimal(request.getParameter("txtReduJorPA")));
                                        } catch (Exception ex) {
                                        }
                                    }
                                    pers.setTipoJornada(request.getParameter("codTipoJornadaPA"));
                                    pers.setFlPld(request.getParameter("checkPLDPA") != null && ((String) request.getParameter("checkPLDPA")).equalsIgnoreCase(ConstantesMeLanbide61.CHECK_S) ? ConstantesMeLanbide61.CHECK_S : ConstantesMeLanbide61.CHECK_N);
                                    pers.setFlRml(request.getParameter("checkRMLPA") != null && ((String) request.getParameter("checkRMLPA")).equalsIgnoreCase(ConstantesMeLanbide61.CHECK_S) ? ConstantesMeLanbide61.CHECK_S : ConstantesMeLanbide61.CHECK_N);
                                    pers.setFlOtr(request.getParameter("checkOtroPA") != null && ((String) request.getParameter("checkOtroPA")).equalsIgnoreCase(ConstantesMeLanbide61.CHECK_S) ? ConstantesMeLanbide61.CHECK_S : ConstantesMeLanbide61.CHECK_N);
                                    pers.setCnoe(request.getParameter("codCNOEPA"));

                                    if (request.getParameter("fechaInConPre") != null && !((String) request.getParameter("fechaInConPre")).equalsIgnoreCase("")) {
                                        try {
                                            pers.setFecInConPre(format.parse((String) request.getParameter("fechaInConPre")));
                                        } catch (Exception ex) {
                                        }
                                    } else {
                                        pers.setFecInConPre(null);
                                    }
                                    if (request.getParameter("fechaFinConPre") != null && !((String) request.getParameter("fechaFinConPre")).equalsIgnoreCase("")) {
                                        try {
                                            pers.setFecFinConPre(format.parse((String) request.getParameter("fechaFinConPre")));
                                        } catch (Exception ex) {
                                        }
                                    } else {
                                        pers.setFecFinConPre(null);
                                    }
                                    if (request.getParameter("fechaInConPre2") != null && !((String) request.getParameter("fechaInConPre2")).equalsIgnoreCase("")) {
                                        try {
                                            pers.setFecInConPre2(format.parse((String) request.getParameter("fechaInConPre2")));
                                        } catch (Exception ex) {
                                        }
                                    } else {
                                        pers.setFecInConPre2(null);
                                    }
                                    if (request.getParameter("fechaFinConPre2") != null && !((String) request.getParameter("fechaFinConPre2")).equalsIgnoreCase("")) {
                                        try {
                                            pers.setFecFinConPre2(format.parse((String) request.getParameter("fechaFinConPre2")));
                                        } catch (Exception ex) {
                                        }
                                    } else {
                                        pers.setFecFinConPre2(null);
                                    }
                                    break;
                            }
                        }
                    }
                    
                    boolean exito = MeLanbide61Manager.getInstance().guardarContrato(contrato, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    if (exito) {
                        codigoOperacion = "0";
                        //RECALCULAR
                        adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                        MeLanbide61Manager meLanbide61Manager = MeLanbide61Manager.getInstance();
                        con = adapt.getConnection();
                        //Obtengo la fecha de inicio relevo
                        Date fechaIniRelevo = meLanbide61Manager.obtenerFechaIniRelevo(numExpediente, con);

                        if (fechaIniRelevo!=null){
                            //cálculo la fecha20 relevo
                            Calendar c2 = GregorianCalendar.getInstance();
                            c2.setTime(fechaIniRelevo);
                            c2.add(Calendar.MONTH, 20);            

                            Date fecha20Relevo=c2.getTime();
                            //Inserto en BBDD la fecha20 relevo
                            if (MeLanbide61Manager.insertarFecha20Relevo(codOrganizacion,numExpediente, fecha20Relevo, con)){
                                fecha20Formateada = fechaF.format(fecha20Relevo);                              
                            }
                        } else {
                             //Elimino en BBDD la fecha20 relevo
                            MeLanbide61Manager.eliminarFecha20Relevo(numExpediente, con);                                
                        }
                    } else {
                        codigoOperacion = "2";
                    }
                }
            } catch (BDException ex) {
                codigoOperacion = "1";
            } catch (Exception ex) {
                codigoOperacion = "2";
            }
            finally
            {
              try
              {
                if (con != null)
                {
                  adapt.devolverConexion(con);
                  log.info(">>>> Devuelvo conexion");
                }
              }
              catch (Exception e)
              {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
              }
            }
        } else {
            codigoOperacion = "3";
        }
        if (request.getParameter("errores") == null || !((String) request.getParameter("errores")).equalsIgnoreCase("")) {
            request.setAttribute("cerrarPagina", "1");
        }


        List<FilaContratoRenovacionPlantillaVO> contratosRenovacionPlantilla = new ArrayList<FilaContratoRenovacionPlantillaVO>();
        try {
            contratosRenovacionPlantilla = MeLanbide61Manager.getInstance().getContratosExpediente(numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        } catch (BDException bde) {
            java.util.logging.Logger.getLogger(MELANBIDE61.class.getName()).log(Level.SEVERE, null, bde);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(MELANBIDE61.class.getName()).log(Level.SEVERE, null, ex);
        }

        log.info("Contrato guardado");

        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        xmlSalida.append("<FECHA20>");
        xmlSalida.append(fecha20Formateada);
        xmlSalida.append("</FECHA20>");
        for (FilaContratoRenovacionPlantillaVO fila : contratosRenovacionPlantilla) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append(fila.getNumContrato());
            xmlSalida.append("</ID>");
            xmlSalida.append("<DNICONTRATADO>");
            xmlSalida.append(fila.getDni2().toUpperCase());
            xmlSalida.append("</DNICONTRATADO>");
            xmlSalida.append("<CONTRATADO>");
            xmlSalida.append(fila.getNomApe2().toUpperCase());
            xmlSalida.append("</CONTRATADO>");
            xmlSalida.append("<DNIADICIONAL>");
            xmlSalida.append(fila.getDni3().toUpperCase());
            xmlSalida.append("</DNIADICIONAL>");
            xmlSalida.append("<ADICIONAL>");
            xmlSalida.append(fila.getNomApe3().toUpperCase());
            xmlSalida.append("</ADICIONAL>");
            xmlSalida.append("<DNISUSTITUIDO>");
            xmlSalida.append(fila.getDni1().toUpperCase());
            xmlSalida.append("</DNISUSTITUIDO>");
            xmlSalida.append("<SUSTITUIDO>");
            xmlSalida.append(fila.getNomApe1().toUpperCase());
            xmlSalida.append("</SUSTITUIDO>");
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


        //return "/jsp/extension/melanbide29/contrato.jsp";
    }

    public void crearContrato(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Creando contrato");
        String codigoOperacion = "";
        DateFormat fechaF = new SimpleDateFormat("dd/MM/yyyy");
        String fecha20Formateada ="-";
        if (numExpediente != null && !numExpediente.equals("")) {
            List<PersonaContratoRenovacionPlantillaVO> personas = new ArrayList<PersonaContratoRenovacionPlantillaVO>();
            PersonaContratoRenovacionPlantillaVO p1 = new PersonaContratoRenovacionPlantillaVO();//Persona sustituida
            PersonaContratoRenovacionPlantillaVO p2 = new PersonaContratoRenovacionPlantillaVO();//Persona contratada
            PersonaContratoRenovacionPlantillaVO p3 = new PersonaContratoRenovacionPlantillaVO();//Persona contrato adicional

            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            AdaptadorSQLBD adapt = null;
            Connection con = null;
            try {
                //Contrato
                ContratoRenovacionPlantillaVO contrato = new ContratoRenovacionPlantillaVO();



                int numContrato = MeLanbide61Manager.getInstance().getNuevoNumContrato(numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (numContrato < 0) {
                    throw new BDException();
                }
                String ejercicio = null;
                try {
                    String[] datos = numExpediente.split(ConstantesMeLanbide61.BARRA_SEPARADORA);
                    ejercicio = datos[0];
                } catch (Exception ex) {
                    log.error("No se ha podido obtener el ejercicio al que pertenece el expediente");
                }
                contrato.setEjercicio(Integer.parseInt(ejercicio));
                contrato.setEntorno(codOrganizacion);
                contrato.setNumContrato(numContrato);
                contrato.setNumExpediente(numExpediente);
                contrato.setProcedimiento(ConstantesMeLanbide61.COD_PROC);

                //Persona sustituida (OBLIGATORIO SOLO SI codigo colectivo punto 4.1 es 3 o 4)
                if (request.getParameter("txtDniPS") != null && !request.getParameter("txtDniPS").equals("")) {
                    p1.setCodTipoPersona(1);
                    log.debug("idTerPS: " + request.getParameter("idTerPS"));
                    if (request.getParameter("idTerPS") != null && !request.getParameter("idTerPS").equals("") && !request.getParameter("idTerPA").equals("null")) {
                        p1.setCodTercero(Long.parseLong(request.getParameter("idTerPS")));
                    }
                    p1.setNumDoc(request.getParameter("txtDniPS"));
                    p1.setNombre(request.getParameter("txtNomPS"));
                    p1.setApellido1(request.getParameter("txtApe1PS"));
                    p1.setApellido2(request.getParameter("txtApe2PS"));
                    //p1.setFondo(request.getParameter("fondo"));
                    p1.setSitPrevia(request.getParameter("codSitPrevia2"));
                    if (request.getParameter("fechaNacimientoPS") != null && !request.getParameter("fechaNacimientoPS").equalsIgnoreCase("")) {
                        try {
                            p1.setFeNac(format.parse(request.getParameter("fechaNacimientoPS")));
                        } catch (Exception ex) {
                        }
                    }
                    if (request.getParameter("txtSexoPS") != null && !request.getParameter("txtSexoPS").equalsIgnoreCase("")) {
                        try {
                            String sexo = request.getParameter("txtSexoPS");
                            if (sexo.equalsIgnoreCase(ConstantesMeLanbide61.SEXO_H)) {
                                p1.setSexo(1);
                            } else if (sexo.equalsIgnoreCase(ConstantesMeLanbide61.SEXO_M)) {
                                p1.setSexo(2);
                            }
                        } catch (Exception ex) {
                        }
                    }
                    p1.setCnoe(request.getParameter("codCNOEPS"));
                    if (request.getParameter("codColectivoPS") != null && !((String) request.getParameter("codColectivoPS")).equalsIgnoreCase("")) {
                        try {
                            p1.setColectivo(Integer.parseInt(request.getParameter("codColectivoPS")));
                        } catch (Exception ex) {
                        }
                    }
                    if (request.getParameter("fechaInConAd") != null && !request.getParameter("fechaInConAd").equalsIgnoreCase("")) {
                        try {
                            p1.setFecInConAd(format.parse(request.getParameter("fechaInConAd")));
                        } catch (Exception ex) {
                        }
                    }
                    if (request.getParameter("fechaFinConAd") != null && !request.getParameter("fechaFinConAd").equalsIgnoreCase("")) {
                        try {
                            p1.setFecFinConAd(format.parse(request.getParameter("fechaFinConAd")));
                        } catch (Exception ex) {
                        }
                    }
                    /*if (!request.getParameter("dias").equals("")) {
                        p1.setDias(request.getParameter("dias"));
                    } else {
                        p1.setDias("0");
                    }
                    if (!request.getParameter("diasF").equals("")) {
                        p1.setDiasF(request.getParameter("diasF"));
                    } else {
                        p1.setDiasF("0");
                    }*/
                     if (!request.getParameter("diasI").equals("")) {
                     p1.setDiasI(request.getParameter("diasI"));
                     } else {
                     p1.setDiasI("0");
                     }
                     if (!request.getParameter("diasContrato").equals("")) {
                     p1.setDiasContrato(request.getParameter("diasContrato"));
                     } else {
                     p1.setDiasContrato("0");
                     }
//                p1.setConvenio(request.getParameter("convenio"));
//                p1.setDias(request.getParameter("dias"));
//                p1.setDiasF(request.getParameter("diasF"));
//                p1.setDiasI(request.getParameter("diasI"));
//                p1.setDiasContrato(request.getParameter("diasContrato"));
                  /*  if (request.getParameter("fechaPublica") != null && !request.getParameter("fechaPublica").equalsIgnoreCase("")) {
                     try {
                     p1.setFechaPublica(format.parse(request.getParameter("fechaPublica")));
                     } catch (Exception ex) {
                     }
                     }*/

                    if (request.getParameter("fechaIniContrato") != null && !request.getParameter("fechaIniContrato").equalsIgnoreCase("")) {
                        try {
                            p1.setFechaIniContrato(format.parse(request.getParameter("fechaIniContrato")));
                        } catch (Exception ex) {
                        }
                    }
                    if (request.getParameter("fechaFinContrato") != null && !request.getParameter("fechaFinContrato").equalsIgnoreCase("")) {
                        try {
                            p1.setFechaFinContrato(format.parse(request.getParameter("fechaFinContrato")));
                        } catch (Exception ex) {
                        }
                    }

                    if (request.getParameter("codEstudiosPS") != null && !((String) request.getParameter("codEstudiosPS")).equalsIgnoreCase("")) {
                        try {
                            p1.setNivelEstudios(Integer.parseInt(request.getParameter("codEstudiosPS")));
                        } catch (Exception ex) {
                        }
                    }
                    contrato.addPersona(p1);
                }
                //Persona contratada
                p2.setCodTipoPersona(2);
                log.debug("idTerPC: " + request.getParameter("idTerPC"));
                if (request.getParameter("idTerPC") != null && (!request.getParameter("idTerPC").equals("")) && (!request.getParameter("idTerPC").equals("null"))) {
                    p2.setCodTercero(Long.parseLong(request.getParameter("idTerPC")));
                }
                p2.setNumDoc(request.getParameter("txtDniPC"));
                p2.setNombre(request.getParameter("txtNomPC"));
                p2.setApellido1(request.getParameter("txtApe1PC"));
                p2.setApellido2(request.getParameter("txtApe2PC"));

                if (request.getParameter("fechaNacimientoPC") != null && !request.getParameter("fechaNacimientoPC").equalsIgnoreCase("")) {
                    try {
                        p2.setFeNac(format.parse(request.getParameter("fechaNacimientoPC")));
                    } catch (Exception ex) {
                    }
                }
                if (request.getParameter("txtSexoPC") != null && !request.getParameter("txtSexoPC").equalsIgnoreCase("")) {
                    try {
                        String sexo = request.getParameter("txtSexoPC");
                        if (sexo.equalsIgnoreCase(ConstantesMeLanbide61.SEXO_H)) {
                            p2.setSexo(1);
                        } else if (sexo.equalsIgnoreCase(ConstantesMeLanbide61.SEXO_M)) {
                            p2.setSexo(2);
                        }
                    } catch (Exception ex) {
                    }
                }

                if (request.getParameter("codTipoCon") != null && !request.getParameter("codTipoCon").equalsIgnoreCase("")) {
                    try {
                        p2.setTipoContrato((request.getParameter("codTipoCon")));
                    } catch (Exception ex) {
                    }
                }

                if (request.getParameter("txtRetribucionPC") != null && !request.getParameter("txtRetribucionPC").equalsIgnoreCase("")) {
                    try {
                        p2.setRetSalarial(new BigDecimal(request.getParameter("txtRetribucionPC")));
                    } catch (Exception ex) {
                    }
                }
                if (request.getParameter("fechaConPC") != null && !request.getParameter("fechaConPC").equalsIgnoreCase("")) {
                    try {
                        p2.setFecContrato(format.parse(request.getParameter("fechaConPC")));
                    } catch (Exception ex) {
                    }
                }

                log.debug("###### CHECK MINUSVALIDO = " + request.getParameter("checkMinusvalidoPC"));
                p2.setFlMinusvalido(request.getParameter("checkMinusvalidoPC") != null && request.getParameter("checkMinusvalidoPC").equalsIgnoreCase(ConstantesMeLanbide61.CHECK_S) ? ConstantesMeLanbide61.CHECK_S : ConstantesMeLanbide61.CHECK_N);
                p2.setFlInmigrante(request.getParameter("checkInmigPC") != null && request.getParameter("checkInmigPC").equalsIgnoreCase(ConstantesMeLanbide61.CHECK_S) ? ConstantesMeLanbide61.CHECK_S : ConstantesMeLanbide61.CHECK_N);
                if (request.getParameter("codEstudiosPC") != null && !request.getParameter("codEstudiosPC").equalsIgnoreCase("")) {
                    try {
                        p2.setNivelEstudios(Integer.parseInt(request.getParameter("codEstudiosPC")));
                    } catch (Exception ex) {
                    }
                }
                if (request.getParameter("codColectivoPC") != null && !request.getParameter("codColectivoPC").equalsIgnoreCase("")) {
                    try {
                        p2.setColectivo(Integer.parseInt(request.getParameter("codColectivoPC")));
                    } catch (Exception ex) {
                    }
                }
                if (request.getParameter("txtMesesDesempleoPC") != null && !request.getParameter("txtMesesDesempleoPC").equalsIgnoreCase("")) {
                    try {
                        p2.setMesesDesempleo(Integer.parseInt(request.getParameter("txtMesesDesempleoPC")));
                    } catch (Exception ex) {
                    }
                }
                p2.setFlPld(request.getParameter("checkPLDPC") != null && request.getParameter("checkPLDPC").equalsIgnoreCase(ConstantesMeLanbide61.CHECK_S) ? ConstantesMeLanbide61.CHECK_S : ConstantesMeLanbide61.CHECK_N);
                p2.setFlRml(request.getParameter("checkRMLPC") != null && request.getParameter("checkRMLPC").equalsIgnoreCase(ConstantesMeLanbide61.CHECK_S) ? ConstantesMeLanbide61.CHECK_S : ConstantesMeLanbide61.CHECK_N);
                p2.setFlOtr(request.getParameter("checkOtroPC") != null && request.getParameter("checkOtroPC").equalsIgnoreCase(ConstantesMeLanbide61.CHECK_S) ? ConstantesMeLanbide61.CHECK_S : ConstantesMeLanbide61.CHECK_N);

                if (request.getParameter("fechaConPC") != null && !request.getParameter("fechaConPC").equalsIgnoreCase("")) {
                    try {
                        p2.setFeAlta(format.parse(request.getParameter("fechaConPC")));
                    } catch (Exception ex) {
                    }
                }
                p2.setTipoJornada(request.getParameter("codTipoJornadaPC"));

                if (request.getParameter("fechaFinPC") != null && !request.getParameter("fechaFinPC").equalsIgnoreCase("")) {
                    try {
                        p2.setFeFinContrato(format.parse(request.getParameter("fechaFinPC")));
                    } catch (Exception ex) {
                    }
                }
                p2.setCnoe(request.getParameter("codCNOEPC"));
                if (request.getParameter("txtDuracionContratoPC") != null && !((String) request.getParameter("txtDuracionContratoPC")).equalsIgnoreCase("")) {
                    try {
                        p2.setDuracionContrato(Integer.parseInt(request.getParameter("txtDuracionContratoPC")));
                    } catch (Exception ex) {
                    }
                }
                if (request.getParameter("subPC") != null && !request.getParameter("subPC").equalsIgnoreCase("")) {
                    try {
                        p2.setImpSubvencion(new BigDecimal(request.getParameter("subPC")));
                    } catch (Exception ex) {
                    }
                }
                p2.setFondo(request.getParameter("fondo"));
                p2.setConvenio(request.getParameter("convenio"));
                if (request.getParameter("fechaPublica") != null && !request.getParameter("fechaPublica").equalsIgnoreCase("")) {
                    try {
                        p2.setFechaPublica(format.parse(request.getParameter("fechaPublica")));
                    } catch (Exception ex) {
                    }
                }
                if (request.getParameter("fechaIniContrato") != null && !request.getParameter("fechaIniContrato").equalsIgnoreCase("")) {
                    try {
                        p2.setFechaIniContrato(format.parse(request.getParameter("fechaIniContrato")));
                    } catch (Exception ex) {
                    }
                }
                if (request.getParameter("fechaFinContrato") != null && !request.getParameter("fechaFinContrato").equalsIgnoreCase("")) {
                    try {
                        p2.setFechaFinContrato(format.parse(request.getParameter("fechaFinContrato")));
                    } catch (Exception ex) {
                    }
                }
                if (!request.getParameter("dias").equals("")) {
                    p2.setDias(request.getParameter("dias"));
                } else {
                    p2.setDias("0");
                }
                if (!request.getParameter("diasF").equals("")) {
                    p2.setDiasF(request.getParameter("diasF"));
                } else {
                    p2.setDiasF("0");
                }
                p2.setSitPrevia(request.getParameter("codSitPrevia"));
                contrato.addPersona(p2);
                //Persona de contrato adicional
                if (request.getParameter("txtDniPA") != null && !request.getParameter("txtDniPA").equalsIgnoreCase("")) {
                    p3.setCodTipoPersona(3);
                    log.debug("idTerPA: " + request.getParameter("idTerPA"));
                    if (request.getParameter("idTerPA") != null && !request.getParameter("idTerPA").equals("") && !request.getParameter("idTerPA").equals("null")) {
                        p2.setCodTercero(Long.parseLong(request.getParameter("idTerPA")));
                    }
                    p3.setNumDoc(request.getParameter("txtDniPA"));
                    p3.setNombre(request.getParameter("txtNomPA"));
                    p3.setApellido1(request.getParameter("txtApe1PA"));
                    p3.setApellido2(request.getParameter("txtApe2PA"));

                    if (request.getParameter("fechaNacimientoPA") != null && !request.getParameter("fechaNacimientoPA").equalsIgnoreCase("")) {
                        try {
                            p3.setFeNac(format.parse(request.getParameter("fechaNacimientoPA")));
                        } catch (Exception ex) {
                        }
                    }
                    if (request.getParameter("txtSexoPA") != null && !request.getParameter("txtSexoPA").equalsIgnoreCase("")) {
                        try {
                            String sexo = request.getParameter("txtSexoPA");
                            if (sexo.equalsIgnoreCase(ConstantesMeLanbide61.SEXO_H)) {
                                p3.setSexo(1);
                            } else if (sexo.equalsIgnoreCase(ConstantesMeLanbide61.SEXO_M)) {
                                p3.setSexo(2);
                            }
                        } catch (Exception ex) {
                        }
                    }
                    p3.setFlMinusvalido(request.getParameter("checkMinusvalidoPA") != null && request.getParameter("checkMinusvalidoPA").equalsIgnoreCase(ConstantesMeLanbide61.CHECK_S) ? ConstantesMeLanbide61.CHECK_S : ConstantesMeLanbide61.CHECK_N);
                    p3.setFlInmigrante(request.getParameter("checkInmigPA") != null && request.getParameter("checkInmigPA").equalsIgnoreCase(ConstantesMeLanbide61.CHECK_S) ? ConstantesMeLanbide61.CHECK_S : ConstantesMeLanbide61.CHECK_N);
                    if (request.getParameter("codEstudiosPA") != null && !request.getParameter("codEstudiosPA").equalsIgnoreCase("")) {
                        try {
                            p3.setNivelEstudios(Integer.parseInt(request.getParameter("codEstudiosPA")));
                        } catch (Exception ex) {
                        }
                    }
                    if (request.getParameter("codColectivoPA") != null && !request.getParameter("codColectivoPA").equalsIgnoreCase("")) {
                        try {
                            p3.setColectivo(Integer.parseInt(request.getParameter("codColectivoPA")));
                        } catch (Exception ex) {
                        }
                    }
                    if (request.getParameter("txtMesesDesempleoPA") != null && !request.getParameter("txtMesesDesempleoPA").equalsIgnoreCase("")) {
                        try {
                            p3.setMesesDesempleo(Integer.parseInt(request.getParameter("txtMesesDesempleoPA")));
                        } catch (Exception ex) {
                        }
                    }
                    if (request.getParameter("codTipConDurPA") != null && !request.getParameter("codTipConDurPA").equalsIgnoreCase("")) {
                        try {
                            p3.setTipoContrato(request.getParameter("codTipConDurPA"));
                        } catch (Exception ex) {
                        }
                    }
                    if (request.getParameter("fechaAltaPA") != null && !request.getParameter("fechaAltaPA").equalsIgnoreCase("")) {
                        try {
                            p3.setFeAlta(format.parse(request.getParameter("fechaAltaPA")));
                        } catch (Exception ex) {
                        }
                    }
                    if (request.getParameter("fechaBajaPA") != null && !request.getParameter("fechaBajaPA").equalsIgnoreCase("")) {
                        try {
                            p3.setFeBaja(format.parse(request.getParameter("fechaBajaPA")));
                        } catch (Exception ex) {
                        }
                    }
                    if (request.getParameter("txtReduJorPA") != null && !request.getParameter("txtReduJorPA").equalsIgnoreCase("")) {
                        try {
                            p3.setPorReduJor(new BigDecimal(request.getParameter("txtReduJorPA")));
                        } catch (Exception ex) {
                        }
                    }
                    p3.setTipoJornada(request.getParameter("codTipoJornadaPA"));
                    p3.setFlPld(request.getParameter("checkPLDPA") != null && request.getParameter("checkPLDPA").equalsIgnoreCase(ConstantesMeLanbide61.CHECK_S) ? ConstantesMeLanbide61.CHECK_S : ConstantesMeLanbide61.CHECK_N);
                    p3.setFlRml(request.getParameter("checkRMLPA") != null && request.getParameter("checkRMLPA").equalsIgnoreCase(ConstantesMeLanbide61.CHECK_S) ? ConstantesMeLanbide61.CHECK_S : ConstantesMeLanbide61.CHECK_N);
                    p3.setFlOtr(request.getParameter("checkOtroPA") != null && request.getParameter("checkOtroPA").equalsIgnoreCase(ConstantesMeLanbide61.CHECK_S) ? ConstantesMeLanbide61.CHECK_S : ConstantesMeLanbide61.CHECK_N);
                    if (request.getParameter("retribPA") != null && !request.getParameter("retribPA").equalsIgnoreCase("")) {
                        try {
                            p3.setRetSalarial(new BigDecimal(request.getParameter("retribPA")));
                        } catch (Exception ex) {
                        }
                    }
                    p3.setCnoe(request.getParameter("codCNOEPA"));
                    if (request.getParameter("fechaInConPre") != null && !request.getParameter("fechaInConPre").equalsIgnoreCase("")) {
                        try {
                            p3.setFecInConPre(format.parse(request.getParameter("fechaInConPre")));
                        } catch (Exception ex) {
                        }
                    }
                    if (request.getParameter("fechaFinConPre") != null && !request.getParameter("fechaFinConPre").equalsIgnoreCase("")) {
                        try {
                            p3.setFecFinConPre(format.parse(request.getParameter("fechaFinConPre")));
                        } catch (Exception ex) {
                        }
                    }
                    if (request.getParameter("fechaInConPre2") != null && !request.getParameter("fechaInConPre2").equalsIgnoreCase("")) {
                        try {
                            p3.setFecInConPre2(format.parse(request.getParameter("fechaInConPre2")));
                        } catch (Exception ex) {
                        }
                    }
                    if (request.getParameter("fechaFinConPre2") != null && !request.getParameter("fechaFinConPre2").equalsIgnoreCase("")) {
                        try {
                            p3.setFecFinConPre2(format.parse(request.getParameter("fechaFinConPre2")));
                        } catch (Exception ex) {
                        }
                    }
                    contrato.addPersona(p3);
                }
                boolean exito = MeLanbide61Manager.getInstance().crearContrato(contrato, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (exito) {
                    codigoOperacion = "0";
                    //RECALCULAR
                    adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                    MeLanbide61Manager meLanbide61Manager = MeLanbide61Manager.getInstance();
                    con = adapt.getConnection();
                    //Obtengo la fecha de inicio relevo
                    Date fechaIniRelevo = meLanbide61Manager.obtenerFechaIniRelevo(numExpediente, con);

                    if (fechaIniRelevo!=null){
                        //cálculo la fecha20 relevo
                        Calendar c2 = GregorianCalendar.getInstance();
                        c2.setTime(fechaIniRelevo);
                        c2.add(Calendar.MONTH, 20);            

                        Date fecha20Relevo=c2.getTime();
                        //Inserto en BBDD la fecha20 relevo
                        if (MeLanbide61Manager.insertarFecha20Relevo(codOrganizacion,numExpediente, fecha20Relevo, con)) {
                            fecha20Formateada = fechaF.format(fecha20Relevo); 
                        }
                    } else {
                         //Elimino en BBDD la fecha20 relevo
                        MeLanbide61Manager.eliminarFecha20Relevo(numExpediente, con);
                                
                    }
                } else {
                    codigoOperacion = "1";
                }
            } catch (BDException bde) {
                log.error("Error BD ", bde);
                codigoOperacion = "1";
            } catch (Exception ex) {
                log.error("Error ", ex);
                codigoOperacion = "2";
            }
            finally
            {
              try
              {
                adapt.devolverConexion(con);
              }
              catch (Exception e)
              {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
              }
            }


        } else {
            codigoOperacion = "3";
        }

        log.info("Contrato creado");

        List<FilaContratoRenovacionPlantillaVO> contratosRenovacionPlantilla = new ArrayList<FilaContratoRenovacionPlantillaVO>();
        try {
            contratosRenovacionPlantilla = MeLanbide61Manager.getInstance().getContratosExpediente(numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        } catch (BDException bde) {
            java.util.logging.Logger.getLogger(MELANBIDE61.class.getName()).log(Level.SEVERE, null, bde);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(MELANBIDE61.class.getName()).log(Level.SEVERE, null, ex);
        }

        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        xmlSalida.append("<FECHA20>");
        xmlSalida.append(fecha20Formateada);
        xmlSalida.append("</FECHA20>");
        for (FilaContratoRenovacionPlantillaVO fila : contratosRenovacionPlantilla) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append(fila.getNumContrato());
            xmlSalida.append("</ID>");
            xmlSalida.append("<DNICONTRATADO>");
            xmlSalida.append(fila.getDni2().toUpperCase());
            xmlSalida.append("</DNICONTRATADO>");
            xmlSalida.append("<CONTRATADO>");
            xmlSalida.append(fila.getNomApe2().toUpperCase());
            xmlSalida.append("</CONTRATADO>");
            xmlSalida.append("<DNIADICIONAL>");
            xmlSalida.append(fila.getDni3().toUpperCase());
            xmlSalida.append("</DNIADICIONAL>");
            xmlSalida.append("<ADICIONAL>");
            xmlSalida.append(fila.getNomApe3().toUpperCase());
            xmlSalida.append("</ADICIONAL>");
            xmlSalida.append("<DNISUSTITUIDO>");
            xmlSalida.append(fila.getDni1().toUpperCase());
            xmlSalida.append("</DNISUSTITUIDO>");
            xmlSalida.append("<SUSTITUIDO>");
            xmlSalida.append(fila.getNomApe1().toUpperCase());
            xmlSalida.append("</SUSTITUIDO>");
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

    public void eliminarContrato(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        if (log.isDebugEnabled()) {
            log.debug("Eliminando contrato");
        }
        String codigoOperacion = "";
        DateFormat fechaF = new SimpleDateFormat("dd/MM/yyyy");
        String fecha20Formateada ="-";

        if (request.getParameter("idCon") != null && numExpediente != null && !numExpediente.equals("")) {
            AdaptadorSQLBD adapt = null;
            Connection con = null;
            try {
                Integer numContrato = Integer.parseInt(request.getParameter("idCon"));
                boolean exito = MeLanbide61Manager.getInstance().eliminarContrato(numExpediente, numContrato, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (exito) {
                    codigoOperacion = "0";
                    //RECALCULAR
                    adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                    MeLanbide61Manager meLanbide61Manager = MeLanbide61Manager.getInstance();
                    con = adapt.getConnection();
                    //Obtengo la fecha de inicio relevo
                    Date fechaIniRelevo = meLanbide61Manager.obtenerFechaIniRelevo(numExpediente, con);

                    if (fechaIniRelevo!=null){
                        //cálculo la fecha20 relevo
                        Calendar c2 = GregorianCalendar.getInstance();
                        c2.setTime(fechaIniRelevo);
                        c2.add(Calendar.MONTH, 20);            

                        Date fecha20Relevo=c2.getTime();
                        //Inserto en BBDD la fecha20 relevo
                        if (MeLanbide61Manager.insertarFecha20Relevo(codOrganizacion,numExpediente, fecha20Relevo, con)){                            
                                fecha20Formateada = fechaF.format(fecha20Relevo); 
                        }
                    } else {
                         //Elimino en BBDD la fecha20 relevo
                        MeLanbide61Manager.eliminarFecha20Relevo(numExpediente, con);                     
                        
                    }
                } else {
                    codigoOperacion = "1";
                }
            } catch (BDException ex) {
                codigoOperacion = "1";
            } catch (Exception ex) {
                codigoOperacion = "2";
            }
            finally
            {
              try
              {
                adapt.devolverConexion(con);
              }
              catch (Exception e)
              {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
              }
            }
        } else {
            codigoOperacion = "3";
        }

        List<FilaContratoRenovacionPlantillaVO> contratosRenovacionPlantilla = new ArrayList<FilaContratoRenovacionPlantillaVO>();
        try {
            contratosRenovacionPlantilla = MeLanbide61Manager.getInstance().getContratosExpediente(numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        } catch (BDException bde) {
            java.util.logging.Logger.getLogger(MELANBIDE61.class.getName()).log(Level.SEVERE, null, bde);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(MELANBIDE61.class.getName()).log(Level.SEVERE, null, ex);
        }


        if (log.isDebugEnabled()) {
            log.debug("Contrato eliminado");
        }

        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        xmlSalida.append("<FECHA20>");
        xmlSalida.append(fecha20Formateada);
        xmlSalida.append("</FECHA20>");
        for (FilaContratoRenovacionPlantillaVO fila : contratosRenovacionPlantilla) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append(fila.getNumContrato());
            xmlSalida.append("</ID>");
            xmlSalida.append("<DNICONTRATADO>");
            xmlSalida.append(fila.getDni2().toUpperCase());
            xmlSalida.append("</DNICONTRATADO>");
            xmlSalida.append("<CONTRATADO>");
            xmlSalida.append(fila.getNomApe2().toUpperCase());
            xmlSalida.append("</CONTRATADO>");
            xmlSalida.append("<DNIADICIONAL>");
            xmlSalida.append(fila.getDni3().toUpperCase());
            xmlSalida.append("</DNIADICIONAL>");
            xmlSalida.append("<ADICIONAL>");
            xmlSalida.append(fila.getNomApe3().toUpperCase());
            xmlSalida.append("</ADICIONAL>");
            xmlSalida.append("<DNISUSTITUIDO>");
            xmlSalida.append(fila.getDni1().toUpperCase());
            xmlSalida.append("</DNISUSTITUIDO>");
            xmlSalida.append("<SUSTITUIDO>");
            xmlSalida.append(fila.getNomApe1().toUpperCase());
            xmlSalida.append("</SUSTITUIDO>");
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

    public void busquedaTerceros(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String dni = "";
        String nom = "";
        String ape1 = "";
        String ape2 = "";
        String codigoOperacion = "";
        if (request.getParameter("dni") != null) {
            dni = (String) request.getParameter("dni");
        }
        if (request.getParameter("nom") != null) {
            nom = (String) request.getParameter("nom");
        }
        if (request.getParameter("ape1") != null) {
            ape1 = (String) request.getParameter("ape1");
        }
        if (request.getParameter("ape2") != null) {
            ape2 = (String) request.getParameter("ape2");
        }
        Tercero terBusq = new Tercero();
        terBusq.setApellido1(ape1);
        terBusq.setApellido2(ape2);
        terBusq.setDni(dni);
        terBusq.setNombre(nom);
        List<Tercero> retList = new ArrayList<Tercero>();
        try {
            retList = MeLanbide61Manager.getInstance().busquedaTerceros(terBusq, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            codigoOperacion = "0";
        } catch (Exception ex) {
            codigoOperacion = "1";
            java.util.logging.Logger.getLogger(MELANBIDE61.class.getName()).log(Level.SEVERE, null, ex);
        }
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        for (Tercero t : retList) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append(t.getId());
            xmlSalida.append("</ID>");
            xmlSalida.append("<DNI>");
            xmlSalida.append(t.getDni() != null ? t.getDni().toUpperCase() : "");
            xmlSalida.append("</DNI>");
            xmlSalida.append("<NOMBREAPELLIDOS>");
            xmlSalida.append("<NOMBRE>");
            xmlSalida.append(t.getNombre() != null ? t.getNombre().toUpperCase() : "");
            xmlSalida.append("</NOMBRE>");
            xmlSalida.append("<APE1>");
            xmlSalida.append(t.getApellido1() != null ? t.getApellido1().toUpperCase() : "");
            xmlSalida.append("</APE1>");
            xmlSalida.append("<APE2>");
            xmlSalida.append(t.getApellido2() != null ? t.getApellido2().toUpperCase() : "");
            xmlSalida.append("</APE2>");
            xmlSalida.append("</NOMBREAPELLIDOS>");
            xmlSalida.append("<SEXO>");
            xmlSalida.append(t.getSexo() != null ? t.getSexo().toUpperCase() : "");
            xmlSalida.append("</SEXO>");
            xmlSalida.append("<FNACI>");
            xmlSalida.append(t.getFNaci() != null ? t.getFNaci().toUpperCase() : "");
            xmlSalida.append("</FNACI>");
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
	
	public String cargarPantallaCCCCAPV(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.debug("Entramos en cargarPantallaCCCCAPV - " + this.getClass().getSimpleName());
        AdaptadorSQLBD adapt = null;
        String url = "/jsp/extension/melanbide61/listaTrabajadoresCAPV.jsp";
        List<TrabajadorCAPVValueObject> datos = null;
        
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            if (adapt != null) {
        
                datos = MeLanbide61Manager.getInstance().getTrabajadoresCAPV(numExpediente, adapt);
                
                request.setAttribute("relacionTrabajadores", datos);
                request.setAttribute("numExpediente", numExpediente);
            } else {
                throw new Exception("Error al recuperar el adaptador getAdaptSQLBD");
            }
        } catch (BDException bde) {
            log.error("Error al obtener una conexión con la BBDD", bde);
        } catch (SQLException ex) {
            log.error("Error al recueperar los datos de trabajadores CAPV", ex);
        } catch (Exception ex) {
            log.error(ex.getMessage() + " ", ex);
        }
        
        return url;
    }
    
    public void darAltaTrabajadorCAPV(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.debug("Entramos en darAltaTrabajadorCAPV - " + this.getClass().getSimpleName());
        AdaptadorSQLBD adapt = null;
        List<TrabajadorCAPVValueObject> datos = null;
        TrabajadorCAPVValueObject fila = null;
        String codigoOperacion = "-1";
                
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            if (adapt != null) {
                //Recojemos los parametros de la request
                String numCCC = request.getParameter("numCCC");
                String numTrs = request.getParameter("numTr");
                String numExp = request.getParameter("numExp");
                
                //Creamos objeto TrabajadorCAPVValueObject y establecemos sus propiedades
                fila = new TrabajadorCAPVValueObject();
                fila.setNumCCC(numCCC);
                fila.setNumTrabajadorFijo(Integer.parseInt(numTrs));
                
                boolean exito = MeLanbide61Manager.getInstance().darAltaTrabajadorCAPV(fila, numExp, adapt);
                if (exito){
                    datos = MeLanbide61Manager.getInstance().getTrabajadoresCAPV(numExp, adapt);
                    codigoOperacion = "0";
                }
            } else {
                throw new Exception("Error al recuperar el adaptador getAdaptSQLBD");
            }
        } catch (BDException bde) {
            log.error("Error al obtener una conexión con la BBDD", bde);
            codigoOperacion = "1";
        } catch (NumberFormatException nfe) {
            log.error("Error al parsear los datos de entrada", nfe);
            codigoOperacion = "2";
        } catch (SQLException ex) {
            log.error("Error al insertar datos de trabajadores CAPV", ex);
            codigoOperacion = "4";
        } catch (Exception ex) {
            log.error(ex.getMessage()+" ", ex);
            codigoOperacion = "1";
        } finally {
            ArrayList resultado = new ArrayList();
            resultado.add(codigoOperacion);
            resultado.add(datos);
            retornarJSON(new Gson().toJson(resultado), response);
        }
    }
    
    public void modificarTrabajadorCAPV(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.debug("Entramos en modificarTrabajadorCAPV - " + this.getClass().getSimpleName());
        AdaptadorSQLBD adapt = null;
        List<TrabajadorCAPVValueObject> datos = null;
        TrabajadorCAPVValueObject fila = null;
        String codigoOperacion = "-1";
                
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            if (adapt != null) {
                //Recojemos los parametros de la request
                String numCCC = request.getParameter("numCCC");
                String numTrs = request.getParameter("numTr");
                String numExp = request.getParameter("numExp");
                String ident = request.getParameter("ident");
                
                if(ident != null && !ident.equals("") && !ident.equals("undefined")){
                    //Creamos objeto TrabajadorCAPVValueObject y establecemos sus propiedades
                    fila = new TrabajadorCAPVValueObject();
                    fila.setNumCCC(numCCC);
                    fila.setNumTrabajadorFijo(Integer.parseInt(numTrs));

                    boolean exito = MeLanbide61Manager.getInstance().modificarTrabajadorCAPV(Long.parseLong(ident), fila, numExp, adapt);
                    if (exito){
                        datos = MeLanbide61Manager.getInstance().getTrabajadoresCAPV(numExp, adapt);
                        codigoOperacion = "0";
                    }
                } else {
                    throw new Exception("Ha habido un problema al obtener el identificador de la fila;Err_3");
                }    
            } else {
                throw new Exception("Error al recuperar el adaptador getAdaptSQLBD;Err_1");
            }    
        } catch (BDException bde) {
            log.error("Error al obtener una conexión con la BBDD", bde);
            codigoOperacion = "1";
        } catch (NumberFormatException nfe) {
            log.error("Error al parsear los datos de entrada", nfe);
            codigoOperacion = "2";
        } catch (SQLException ex) {
            log.error("Error al modificar datos de un registro de trabajadores CAPV", ex);
            codigoOperacion = "5";
        } catch (Exception ex) {
            String[] partes = ex.getMessage().split(";Err_");
            log.error(partes[0]+" ", ex);
            codigoOperacion = partes[1];
        } finally {
            ArrayList resultado = new ArrayList();
            resultado.add(codigoOperacion);
            resultado.add(datos);
            retornarJSON(new Gson().toJson(resultado), response);
        }
    }
    
    public void eliminarTrabajadorCAPV(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.debug("Entramos en eliminarTrabajadorCAPV - " + this.getClass().getSimpleName());
        AdaptadorSQLBD adapt = null;
        List<TrabajadorCAPVValueObject> datos = null;
        String codigoOperacion = "-1";
                
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            if (adapt != null) {
                //Recojemos los parametros de la request
                String numExp = request.getParameter("numExp");
                String ident = request.getParameter("ident");
                
                if(ident != null && !ident.equals("") && !ident.equals("undefined")){
                    boolean exito = MeLanbide61Manager.getInstance().eliminarTrabajadorCAPV(Long.parseLong(ident), adapt);
                    if (exito){
                        datos = MeLanbide61Manager.getInstance().getTrabajadoresCAPV(numExp, adapt);
                        codigoOperacion = "0";
                    }
                } else {
                    throw new Exception("Ha habido un problema al obtener el identificador de la fila;Err_3");
                }
            } else {
                throw new Exception("Error al recuperar el adaptador getAdaptSQLBD;Err_1");
            }
        } catch (BDException bde) {
            log.error("Error al obtener una conexión con la BBDD", bde);
            codigoOperacion = "1";
        } catch (NumberFormatException nfe) {
            log.error("Error al parsear los datos de entrada", nfe);
            codigoOperacion = "2";
        } catch (SQLException ex) {
            log.error("Error al eliminar un registro de trabajadores CAPV", ex);
            codigoOperacion = "6";
        } catch (Exception ex) {
            String[] partes = ex.getMessage().split(";Err_");
            log.error(partes[0]+" ", ex);
            codigoOperacion = partes[1];
        } finally {
            ArrayList resultado = new ArrayList();
            resultado.add(codigoOperacion);
            resultado.add(datos);
            retornarJSON(new Gson().toJson(resultado), response);
        }
    }

    /**
     * Operación que recupera los datos de conexión a la BBDD
     *
     * @param codOrganizacion
     * @return AdaptadorSQLBD
     */
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

    /**
     * Operación que recupera las listas para los combos y los guarda en la
     * request
     *
     * @param request
     */
    private void cargarCombos(int codOrganizacion, HttpServletRequest request) {
        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();

        ArrayList<ValorCampoDesplegableEstadoVO> listaEstudios = new ArrayList<ValorCampoDesplegableEstadoVO>();
        ArrayList<ValorCampoDesplegableEstadoVO> listaColectivos = new ArrayList<ValorCampoDesplegableEstadoVO>();
        ArrayList<ValorCampoDesplegableEstadoVO> listaTipConDur = new ArrayList<ValorCampoDesplegableEstadoVO>();
        ArrayList<ValorCampoDesplegableEstadoVO> listaTipoJornada = new ArrayList<ValorCampoDesplegableEstadoVO>();
        ArrayList<ValorCampoDesplegableEstadoVO> listaCNOE = new ArrayList<ValorCampoDesplegableEstadoVO>();
        ArrayList<ValorCampoDesplegableEstadoVO> listaTipoCon = new ArrayList<ValorCampoDesplegableEstadoVO>();
        ArrayList<ValorCampoDesplegableEstadoVO> listaColec2 = new ArrayList<ValorCampoDesplegableEstadoVO>();
        ArrayList<ValorCampoDesplegableEstadoVO> listaSitPrevia = new ArrayList<ValorCampoDesplegableEstadoVO>();
        
        // Recuperamos el Idioma de la request para la gestion de Desplegables
        UsuarioValueObject usuario = new UsuarioValueObject();
        int idioma = ConstantesMeLanbide61.CODIGO_IDIOMA_CASTELLANO;  // Por Defecto 1 Castellano
        try {
            
            if (request!=null && request.getSession()!= null) {
                usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
                if (usuario != null) {
                    idioma = usuario.getIdioma();
                }
            }
        } catch (Exception ex) {
            log.error("Error al recuperar el idioma del usuario de la request, asiganmos por defecto 1 Castellano",ex);
            idioma=ConstantesMeLanbide61.CODIGO_IDIOMA_CASTELLANO;
        }
        log.error("Idioma Definido para Muestra de Desplegables " + idioma);

        //Combo ESTUDIOS
        try {
            String campoDesplegable = ConfigurationParameter.getParameter(String.valueOf(codOrganizacion) + ConstantesMeLanbide61.BARRA_SEPARADORA
                    + ConstantesMeLanbide61.CAMPO_SUPLEMENTARIO_ESTUDIOS, ConstantesMeLanbide61.FICHERO_PROPIEDADES);

           /* SalidaIntegracionVO salidaIntegracion = el.getCampoDesplegable(String.valueOf(codOrganizacion), campoDesplegable);
            listaEstudios = salidaIntegracion.getCampoDesplegable().getValores();

            log.debug("CAMPO DESPLEGABLE = " + campoDesplegable);*/
            
            listaEstudios = MeLanbide61Manager.getInstance().getListaDesplegable(this.getAdaptSQLBD(String.valueOf(codOrganizacion)), campoDesplegable,idioma);
            log.debug("ESTUDIOS ENCOTRADOS: " + listaEstudios.size());
        } catch (Exception ex) {
        }
        request.setAttribute("listaEstudios", listaEstudios);

        //Combo COLECTIVOS
        try {
            String campoDesplegable = ConfigurationParameter.getParameter(String.valueOf(codOrganizacion) + ConstantesMeLanbide61.BARRA_SEPARADORA
                    + ConstantesMeLanbide61.CAMPO_SUPLEMENTARIO_COLECTIVOS, ConstantesMeLanbide61.FICHERO_PROPIEDADES);

            /*SalidaIntegracionVO salidaIntegracion = el.getCampoDesplegable(String.valueOf(codOrganizacion), campoDesplegable);
            listaColectivos = salidaIntegracion.getCampoDesplegable().getValores();*/

            log.debug("CAMPO DESPLEGABLE = " + campoDesplegable);
            listaColectivos = MeLanbide61Manager.getInstance().getListaDesplegable(this.getAdaptSQLBD(String.valueOf(codOrganizacion)), campoDesplegable,idioma);
            log.debug("COLECTIVOS ENCOTRADOS: " + listaColectivos.size());
        } catch (Exception ex) {
        }
        request.setAttribute("listaColectivos", listaColectivos);

        //Combo TIPCONDUR
        try {
            String campoDesplegable = ConfigurationParameter.getParameter(String.valueOf(codOrganizacion) + ConstantesMeLanbide61.BARRA_SEPARADORA
                    + ConstantesMeLanbide61.CAMPO_SUPLEMENTARIO_TIP_CON_DUR, ConstantesMeLanbide61.FICHERO_PROPIEDADES);

            //SalidaIntegracionVO salidaIntegracion = el.getCampoDesplegable(String.valueOf(codOrganizacion), campoDesplegable);
            //listaTipConDur = salidaIntegracion.getCampoDesplegable().getValores();
            listaTipConDur = MeLanbide61Manager.getInstance().getListaDesplegable(this.getAdaptSQLBD(String.valueOf(codOrganizacion)), campoDesplegable,idioma);  
            log.debug("CAMPO DESPLEGABLE = " + campoDesplegable);
            log.debug("TIP CON DUR ENCOTRADOS: " + listaTipConDur.size());
        } catch (Exception ex) {
        }
        request.setAttribute("listaTipConDur", listaTipConDur);

        //Combo TIPO JORNADA
        try {
            String campoDesplegable = ConfigurationParameter.getParameter(String.valueOf(codOrganizacion) + ConstantesMeLanbide61.BARRA_SEPARADORA
                    + ConstantesMeLanbide61.CAMPO_SUPLEMENTARIO_TIPO_JORNADA, ConstantesMeLanbide61.FICHERO_PROPIEDADES);

            //SalidaIntegracionVO salidaIntegracion = el.getCampoDesplegable(String.valueOf(codOrganizacion), campoDesplegable);
            //listaTipoJornada = salidaIntegracion.getCampoDesplegable().getValores();
            listaTipoJornada = MeLanbide61Manager.getInstance().getListaDesplegable(this.getAdaptSQLBD(String.valueOf(codOrganizacion)), campoDesplegable,idioma);  

            log.debug("CAMPO DESPLEGABLE = " + campoDesplegable);
            log.debug("TIPO JORNADA ENCOTRADOS: " + listaTipoJornada.size());
        } catch (Exception ex) {
        }
        request.setAttribute("listaTipoJornada", listaTipoJornada);

        //Combo CNOE
        try {
            String campoDesplegable = ConfigurationParameter.getParameter(String.valueOf(codOrganizacion) + ConstantesMeLanbide61.BARRA_SEPARADORA
                    + ConstantesMeLanbide61.CAMPO_SUPLEMENTARIO_CNOE, ConstantesMeLanbide61.FICHERO_PROPIEDADES);

            //SalidaIntegracionVO salidaIntegracion = el.getCampoDesplegable(String.valueOf(codOrganizacion), campoDesplegable);
            //listaCNOE = salidaIntegracion.getCampoDesplegable().getValores();
            listaCNOE = MeLanbide61Manager.getInstance().getListaDesplegable(this.getAdaptSQLBD(String.valueOf(codOrganizacion)), campoDesplegable,idioma);  

            log.debug("CAMPO DESPLEGABLE = " + campoDesplegable);
            log.debug("CNOE ENCOTRADOS: " + listaCNOE.size());
        } catch (Exception ex) {
        }
        request.setAttribute("listaCNOE", listaCNOE);

        //Combo tipo contrato
        try {
            String campoDesplegable = ConfigurationParameter.getParameter(String.valueOf(codOrganizacion) + ConstantesMeLanbide61.BARRA_SEPARADORA
                    + ConstantesMeLanbide61.CAMPO_SUPLEMENTARIO_TIPOCON, ConstantesMeLanbide61.FICHERO_PROPIEDADES);

            //SalidaIntegracionVO salidaIntegracion = el.getCampoDesplegable(String.valueOf(codOrganizacion), campoDesplegable);
            //listaTipoCon = salidaIntegracion.getCampoDesplegable().getValores();
            listaTipoCon = MeLanbide61Manager.getInstance().getListaDesplegable(this.getAdaptSQLBD(String.valueOf(codOrganizacion)), campoDesplegable,idioma);  

            log.debug("CAMPO DESPLEGABLE = " + campoDesplegable);
            log.debug("CNOE ENCOTRADOS: " + listaTipoCon.size());
        } catch (Exception ex) {
        }
        request.setAttribute("listaTipoCon", listaTipoCon);

        //Combo COLECTIVOS 2
        try {
            String campoDesplegable = ConfigurationParameter.getParameter(String.valueOf(codOrganizacion) + ConstantesMeLanbide61.BARRA_SEPARADORA
                    + ConstantesMeLanbide61.CAMPO_SUPLEMENTARIO_COLEC2, ConstantesMeLanbide61.FICHERO_PROPIEDADES);

            //SalidaIntegracionVO salidaIntegracion = el.getCampoDesplegable(String.valueOf(codOrganizacion), campoDesplegable);
            //listaColec2 = salidaIntegracion.getCampoDesplegable().getValores();
            listaColec2 = MeLanbide61Manager.getInstance().getListaDesplegable(this.getAdaptSQLBD(String.valueOf(codOrganizacion)), campoDesplegable,idioma);  

            log.debug("CAMPO DESPLEGABLE = " + campoDesplegable);
            log.debug("COLECTIVOS ENCOTRADOS: " + listaColec2.size());
        } catch (Exception ex) {
        }
        request.setAttribute("listaColec2", listaColec2);

        //Combo SITUACIÓN PREVIA
        try {
            String campoDesplegable = ConfigurationParameter.getParameter(String.valueOf(codOrganizacion) + ConstantesMeLanbide61.BARRA_SEPARADORA
                    + ConstantesMeLanbide61.CAMPO_SUPLEMENTARIO_TCON, ConstantesMeLanbide61.FICHERO_PROPIEDADES);

            //SalidaIntegracionVO salidaIntegracion = el.getCampoDesplegable(String.valueOf(codOrganizacion), campoDesplegable);
            //listaSitPrevia = salidaIntegracion.getCampoDesplegable().getValores();
            listaSitPrevia = MeLanbide61Manager.getInstance().getListaDesplegable(this.getAdaptSQLBD(String.valueOf(codOrganizacion)), campoDesplegable,idioma);  

            log.debug("CAMPO DESPLEGABLE = " + campoDesplegable);
            log.debug("COLECTIVOS ENCOTRADOS: " + listaSitPrevia.size());
        } catch (Exception ex) {
        }
        
        for (int i=0;i<listaSitPrevia.size();i++){
            System.out.println("AA");
            
        }
        
        
        request.setAttribute("listaSitPrevia", listaSitPrevia);
    }

    public String getListaDocumentosContablesExpediente(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        try {
            List<FilaDocumentoContableVO> listado = MeLanbide61Manager.getInstance().getListaDocumentosContablesExpediente(numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            request.setAttribute("listaDocContables", listado);
        } catch (Exception ex) {
        }
        return "/jsp/extension/melanbide61/listaDocumentosContables.jsp";
    }

    
     /**
     * Método que realiza lo siguiente: en el trámite 4 - ESPERA ACUSE RECIBO RESOLUCION se rellena el campo suplementario de fecha de acuse, FECACUSENOT. Ese trámite abre el 9099 - ESPERA PRESENTACIÓN RECURSOS, que tiene el campo "Fecha inicio plazo" en "Datos generales" que coincide con el FECACUSENOT + 1 día.
     * Se realiza una operación de extensión que copia el campo FECACUSENOT + 1 día del trámite anterior en el campo "Fecha inicio plazo" del trámite 9099. Esa operación de extensión parametriza el código de trámite y nombre de campo origen, y el código de trámite destino, para que se pueda usar en otros procedimientos.
     *
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     *
     *
     */
    public String grabacionAutFechaAcuse(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente) {
        String fechaAcuse="";
        String campoFechaAcuse = null;
        String salida="";

        boolean propiedadesConfiguracion = false;
        boolean fechaExist = false;

        log.error("metodo grabacionAutFechaAcuse");
        
    AdaptadorSQLBD adapt = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            campoFechaAcuse = ConfigurationParameter.getParameter("CODIGO_CAMPO_TRAMITE_FECHA_ACUSE", this.getNombreModulo());
            propiedadesConfiguracion = true;
        } catch (Exception e) {
            salida = "1";
            log.error(this.getClass().getName() + ".altaEspecialidadFicheroEspecialidadesFormacion(): Error al recuperar las propiedades del fichero de configuración " + this.getNombreModulo() + ".properties. " + e.getMessage());
        }
      
        if (propiedadesConfiguracion) {
            try {
                //Recupera el campo suplementario fecha acuse              
                fechaAcuse = MeLanbide61Manager.getInstance().getValorFechaAcuse(numExpediente, campoFechaAcuse, adapt);
                String[] arrayDate=fechaAcuse.split("-");
                String[] arrayDiaHora=arrayDate[2].split(" ");
                String dia=arrayDiaHora[0];
                int diaInt=Integer.parseInt(dia)+1;
                String diaS=String.valueOf(diaInt);
                if(diaS.length()==1) {
                    diaS="0"+diaS;
                }
                fechaAcuse =arrayDate[0]+"/"+arrayDate[1]+"/"+diaS;
                log.error("FECHA ACUSE: "+fechaAcuse );
                fechaExist = true;
            } catch (Exception e) {
                salida = "2";
                log.error(this.getClass().getName() + ".grabacionAutFechaAcuse(): Error al recuperar los campos externos." + e.getMessage());
            }
        }
        
        if (fechaExist) {
            try {
                //Se inserta la fecha en el campo datos generales. Tabla E_CRO. Dato General CRO_FIP           
                boolean insert = MeLanbide61Manager.getInstance().insertFechaDatoGeneral(numExpediente, fechaAcuse, adapt);
                log.error("result insert: "+insert );
                
            } catch (Exception e) {
                salida = "2";
                log.error(this.getClass().getName() + ".grabacionAutFechaAcuse(): Error al insertar el dato general." + e.getMessage());
            }
        }

        return fechaAcuse;
    }
    

    
    public void comprobarDNIEnExpedientes(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) throws SQLException {
        String dni="";
        if (request.getParameter("dni") != null ) 
            dni=(String) request.getParameter("dni");
        String mensaje=MeLanbide61Manager.getInstance().comprobarDniEnExpedientes(this.getAdaptSQLBD(String.valueOf(codOrganizacion)), numExpediente, dni);
        request.setAttribute("mensaje", mensaje);
        
          StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(1);
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
	
	/**
     * Método llamado para devolver un String en formato JSON al cliente que ha realiza la petición 
     * a alguna de las operaciones de este action
     * @param json: String que contiene el JSON a devolver
     * @param response: Objeto de tipo HttpServletResponse a través del cual se devuelve la salida
     * al cliente que ha realizado la solicitud
     */
    private void retornarJSON(String json,HttpServletResponse response){
        
        try{
            if(json!=null){
                response.setCharacterEncoding("UTF-8");                
                PrintWriter out = response.getWriter();
                out.print(json);
                out.flush();
                out.close();
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    public String cargarFecha20Relevo (int codOrganizacion,int codTramite,int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response){
      if(log.isDebugEnabled()) log.debug("cargarFecha20Relevo ( codOrganizacion = " + codOrganizacion + " codTramite = " + codTramite + " ocurrenciaTramite = " + ocurrenciaTramite + " numExpediente = " + numExpediente + " ) : BEGIN");
        //acceder a BD para obtener los trabajadores para calacular la subvención
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        Date fecha20Relevo=null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            MeLanbide61Manager meLanbide61Manager = MeLanbide61Manager.getInstance();
            con = adapt.getConnection();
            //Obtengo la fecha de inicio relevo
            Date fechaIniRelevo = meLanbide61Manager.obtenerFechaIniRelevo(numExpediente, con);
            
            if (fechaIniRelevo!=null){
                //cálculo la fecha20 relevo
                Calendar c2 = GregorianCalendar.getInstance();
                c2.setTime(fechaIniRelevo);
                c2.add(Calendar.MONTH, 20);            

                fecha20Relevo=c2.getTime();
                //Inserto en BBDD la fecha20 relevo
                MeLanbide61Manager.insertarFecha20Relevo(codOrganizacion,numExpediente, fecha20Relevo, con);
            }else {
                
                 //Elimino en BBDD la fecha20 relevo
               MeLanbide61Manager.eliminarFecha20Relevo(numExpediente, con);
                    
            }
            try
            {
              adapt.devolverConexion(con);
            }
            catch (Exception e)
            {
              log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
            
                                               
        }
        catch(Exception e)
        {
            log.error("Error al calcular la fecha20 relevo: " + e.getMessage());
        }
        finally
        {
            try
            {
                adapt.devolverConexion(con);       
            }
            catch(Exception e)
            {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }           
    
        if(log.isDebugEnabled()) log.debug("cargarFecha20Relevo : END");
        return "/jsp/extension/melanbide61/cargarFecha20Relevo.jsp";
    }
    
    /**
     * Operación que calcula y graba los importes total, 1er y 2do Pago
     *
     * @param codOrganizacion: Código de la organización
     * @param codTramite: Código del trámite
     * @param ocurrenciaTramite: Ocurrencia del trámite
     * @param numExpediente: Número del expediente
     * @return String que puede tomar los siguientes valores:
     *
     * "0" --> Si la operación se ha ejecutado correctamente
     * "1" --> Ha ocurrido un error genérico
     * "2" --> No se ha podido obtener una conexión a la BBDD
     * "3" --> Error al recuperar los importes
     * "4" --> Error al guardar los importes
     * 
     */
    public String actualizarDatosSuplementarios(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente) throws BDException {
        log.info(this.getClass().getSimpleName() + ".actualizarDatosSuplementarios  =================>");
        String codOperacion = "0";
        AdaptadorSQLBD adapt = null;
        MeLanbide61Manager meLanbide61Manager = MeLanbide61Manager.getInstance();
        DatosEconomicosExpVO datosEcon;
        Double importeSubvencion = null;
        Double importePrimerPago = null;
        Double importeSegundoPago = null;
        boolean grabaOK = false;
        try {
            try {
                adapt = getAdaptSQLBD(Integer.toString(codOrganizacion));
                //  con = adapt.getConnection();
            } catch (Exception e) {
                log.error("Error al obtener una conexión a la BBDD: " + e.getMessage());
                codOperacion = "2";
            }
            boolean haySustituto = meLanbide61Manager.haySustituto(numExpediente, adapt);
            if (haySustituto) {
                datosEcon = meLanbide61Manager.getImporteSubvencionYPorc(numExpediente, adapt);
                if (datosEcon != null) {
                    importeSubvencion = datosEcon.getImporteSubvencion();
                    importePrimerPago = datosEcon.getImportePrimerPago();
                    importeSegundoPago = datosEcon.getImporteSegundoPago();
                    grabaOK = meLanbide61Manager.guardarImportes(codOrganizacion, numExpediente, datosEcon, adapt);
                    if (!grabaOK) {
                        codOperacion = "4";
                    }
                } else {
                    codOperacion = "3";
                }
            } else {// no hay sustituto
                codOperacion = "0";
            }
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName()+ ".actualizarDatosSuplementarios():  Error: " + e.getMessage());
            codOperacion = "1";
        }
        return codOperacion;
    }
    
    public String cargarPantallaSubSolic(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarPantallaSubSolic de " + this.getClass().getName());
        AdaptadorSQLBD adapt = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (Exception ex) {
            log.error(this.getClass().getName() + " Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }
        String url = "/jsp/extension/melanbide61/subSolic.jsp";
        request.setAttribute("numExp", numExpediente);
        if (adapt != null) {
            try {
                List<SubSolicVO> listaSubSolic = MeLanbide61Manager.getInstance().getDatosSubSolic(numExpediente, codOrganizacion, adapt);
                if (!listaSubSolic.isEmpty()) {
                    for (SubSolicVO lm : listaSubSolic) {
                        lm.setDesEstado(m61Utils.getDescripcionDesplegable(request, lm.getDesEstado()));
                    }
                    request.setAttribute("listaSubSolic", listaSubSolic);
                }
            } catch (Exception ex) {
                log.error("Error al recuperar los datos de SubSolic - MELANBIDE61 - cargarPantallaPrincipal", ex);
            }
        }
        return url;
    }
      /**
     *
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @param request
     * @param response
     * @return
     */
    public String cargarNuevoSubSolic(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarNuevoSubSolic - ");
        String nuevo = "1";
        String urlnuevoSubSolic = "/jsp/extension/melanbide61/nuevoSubSolic.jsp?codOrganizacion=" + codOrganizacion;
        try {
            if (request.getAttribute("nuevo") != null) {
                if (request.getAttribute("nuevo").toString().equals("0")) {
                    request.setAttribute("nuevo", nuevo);
                }
            } else {
                request.setAttribute("nuevo", nuevo);
            }
            if (request.getParameter("numExp") != null) {
                request.setAttribute("numExp", request.getParameter("numExp"));
            }            
            cargarDesplegables(codOrganizacion, request);
            
        } catch (Exception ex) {
            log.debug("Se ha presentado un error al intentar preparar la jsp de un nuevo SubSolic : " + ex.getMessage());
        }
        return urlnuevoSubSolic;
    }

    /**
     *
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @param request
     * @param response
     * @return
     */
    public String cargarModificarSubSolic(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarModificarSubSolic - " + numExpediente);
        String nuevo = "0";
        String urlnuevoSubSolic = "/jsp/extension/melanbide61/nuevoSubSolic.jsp?codOrganizacion=" + codOrganizacion;
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
            if (id != null && !id.isEmpty()) {
                SubSolicVO datModif = MeLanbide61Manager.getInstance().getSubSolicPorID(id, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (datModif != null) {
                    request.setAttribute("datModif", datModif);
                }
            }       
            cargarDesplegables(codOrganizacion, request);
        
        } catch (Exception ex) {
            log.debug("Error al tratar de preparar los datos para modificar y llamar la jsp de modificación : " + ex.getMessage());
        }
        return urlnuevoSubSolic;
    }
    
    public void crearNuevoSubSolic(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en crearNuevoSubSolic - " + numExpediente);
        String codigoOperacion = "-1";
        List<SubSolicVO> lista = new ArrayList<SubSolicVO>();
        SubSolicVO nuevaSubSolic = new SubSolicVO();
        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            
            String numExp = request.getParameter("numExp");            
            String estado = request.getParameter("estado");
            String organismo = request.getParameter("organismo");
            String objeto = request.getParameter("objeto");
            String importe = request.getParameter("importe").replace(",", ".");
            String fecha = request.getParameter("fecha");
                        
            nuevaSubSolic.setNumExp(numExp);            
            nuevaSubSolic.setEstado(estado);
            nuevaSubSolic.setOrganismo(organismo);
            nuevaSubSolic.setObjeto(objeto);
            if (importe != null && !"".equals(importe)) {
                nuevaSubSolic.setImporte(Double.valueOf(importe));
            }
            if (fecha != null && !"".equals(fecha)) {
                nuevaSubSolic.setFecha(new java.sql.Date(formatoFecha.parse(fecha).getTime()));
            }
            
            MeLanbide61Manager meLanbide61Manager = MeLanbide61Manager.getInstance();
            boolean insertOK = meLanbide61Manager.crearNuevoSubSolic(nuevaSubSolic, adapt);
            if (insertOK) {
                log.debug("SubSolic insertada correctamente");
                codigoOperacion = "0";
                lista = meLanbide61Manager.getDatosSubSolic(numExp, codOrganizacion, adapt);
                if (!lista.isEmpty()) {
                    for (SubSolicVO lm : lista) {
                        lm.setDesEstado(m61Utils.getDescripcionDesplegable(request, lm.getDesEstado()));
                    }
                }
            } else {
                log.error("NO se ha insertado correctamente la nueva SubSolic");
                codigoOperacion = "1";
            }
        } catch (Exception ex) {
            log.error("Error al parsear los parámetros recibidos del jsp al objeto " + ex.getMessage());
            codigoOperacion = "3";
        }

        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);
        if (codigoOperacion.equalsIgnoreCase("0")) {
            resultado.setAtributo("lista", lista);
        }
        m61Utils.retornarJSON(new Gson().toJson(resultado), response);
    }

    public void modificarSubSolic(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info("Entramos en modificarSubSolic - " + numExpediente);
        String codigoOperacion = "-1";
        List<SubSolicVO> lista = new ArrayList<SubSolicVO>();        
        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            //Recojo los parametros
            String id = request.getParameter("id");            
            String numExp = request.getParameter("numExp");            
            String estado = request.getParameter("estado");
            String organismo = request.getParameter("organismo");
            String objeto = request.getParameter("objeto");
            String importe = request.getParameter("importe").replace(",", ".");
            String fecha = request.getParameter("fecha");

            if (id == null || id.isEmpty()) {
                log.error("No se ha recibido desde la JSP el id de la SubSolic a Modificar ");
                
            } else {
                SubSolicVO datModif = new SubSolicVO();            
                datModif.setId(Integer.valueOf(id));
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

                MeLanbide61Manager meLanbide61Manager = MeLanbide61Manager.getInstance();
                boolean modOK = meLanbide61Manager.modificarSubSolic(datModif, adapt);
                if (modOK) {
                    codigoOperacion = "0";
                    try {
                        lista = meLanbide61Manager.getDatosSubSolic(numExp, codOrganizacion, adapt);
                        if (!lista.isEmpty()) {
                            for (SubSolicVO lm : lista) {
                                lm.setDesEstado(m61Utils.getDescripcionDesplegable(request, lm.getDesEstado()));
                            }
                        }
                    } catch (BDException bde) {
                        codigoOperacion = "1";
                        log.error("Error de tipo BD al recuperar la lista de SubSolic después de Modificar un SubSolic : " + bde.getMensaje());
                    } catch (Exception ex) {
                        codigoOperacion = "2";
                        log.error("Error al recuperar la lista de SubSolic después de Modificar un SubSolic : " + ex.getMessage());
                    }
                } else {
                    codigoOperacion = "3";
                    log.error("Error al Modificar un SubSolic  ");
                }
            }
        } catch (NumberFormatException ex) {
            log.error("Error modificar --- ", ex);
            codigoOperacion = "3";
        } catch (ParseException ex) {
            log.error("Error modificar --- ", ex);
            codigoOperacion = "3";
        }
        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);
        if (codigoOperacion.equalsIgnoreCase("0")) {
            resultado.setAtributo("lista", lista);
        }
        m61Utils.retornarJSON(new Gson().toJson(resultado), response);
    }
    
    public void eliminarSubSolic(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en eliminarSubSolic - " + numExpediente);
        String codigoOperacion = "-1";
        List<SubSolicVO> lista = new ArrayList<SubSolicVO>();
        String numExp = "";
        try {
            String id = request.getParameter("id");
            if (id == null || id.isEmpty()) {
                log.error("No se ha recibido desde la JSP el id de la SubSolic a Elimnar ");
                codigoOperacion = "3";
            } else {
                numExp = request.getParameter("numExp");
                AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                int result = MeLanbide61Manager.getInstance().eliminarSubSolic(id, adapt);
                if (result <= 0) {
                    codigoOperacion = "6";
                } else {
                    codigoOperacion = "0";
                    try {
                        lista = MeLanbide61Manager.getInstance().getDatosSubSolic(numExp, codOrganizacion, adapt);
                        if (!lista.isEmpty()) {
                            for (SubSolicVO lm : lista) {
                                lm.setDesEstado(m61Utils.getDescripcionDesplegable(request, lm.getDesEstado()));
                            }
                        }
                    } catch (Exception ex) {
                        codigoOperacion = "5";
                        log.error("Error al recuperar la lista de SubSolic después de eliminar un SubSolic");
                    }
                }
            }
        } catch (Exception ex) {
            log.error("Error eliminando un SubSolic: " + ex);
            codigoOperacion = "2";
        }
        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);
        if (codigoOperacion.equalsIgnoreCase("0")) {
            resultado.setAtributo("lista", lista);
        }
        m61Utils.retornarJSON(new Gson().toJson(resultado), response);
    } 
        
    /**
     * Método que recupera los valores de los desplegables del módulo
     *
     * @param codOrganizacion
     * @param request
     * @throws SQLException
     * @throws Exception
     */
    private void cargarDesplegables(int codOrganizacion, HttpServletRequest request) throws SQLException, Exception {
        List<DesplegableVO> listaEstado = MeLanbide61Manager.getInstance().getValoresDesplegables(es.altia.flexia.integracion.moduloexterno.melanbide61.util.ConfigurationParameter.getParameter(ConstantesMeLanbide61.CAMPO_ESTADO, ConstantesMeLanbide61.FICHERO_PROPIEDADES), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        if (!listaEstado.isEmpty()) {
            listaEstado = m61Utils.traducirDesplegable(request, listaEstado);
            request.setAttribute("listaEstado", listaEstado);
        }
    }    


}//class