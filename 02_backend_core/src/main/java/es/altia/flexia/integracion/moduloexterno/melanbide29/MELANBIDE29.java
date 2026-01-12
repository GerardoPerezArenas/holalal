package es.altia.flexia.integracion.moduloexterno.melanbide29;


import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.melanbide29.manager.MeLanbide29Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide29.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide29.util.ConstantesMeLanbide29;
import es.altia.flexia.integracion.moduloexterno.melanbide29.vo.ContratoRenovacionPlantillaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide29.vo.FilaContratoRenovacionPlantillaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide29.vo.FilaDocumentoContableVO;
import es.altia.flexia.integracion.moduloexterno.melanbide29.vo.PersonaContratoRenovacionPlantillaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide29.vo.Tercero;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExternoCampoSupFactoria;
import es.altia.flexia.integracion.moduloexterno.plugin.camposuplementario.IModuloIntegracionExternoCamposFlexia;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.SalidaIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.ValorCampoDesplegableModuloIntegracionVO;
import es.altia.technical.PortableContext;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
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
 * @version 16/08/2012 1.0
 * Historial de cambios:
 * <ol>
 *  <li>santiago.calvo * 28-12-2012 * Edición inicial</li>
 * </ol> 
 */
public class MELANBIDE29 extends ModuloIntegracionExterno
{
    
    //Logger
    private static Logger log = LogManager.getLogger(MELANBIDE29.class);
    
    
    
    public String cargarContratos(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
        log.debug("----------------------------------- CARGAR CONTRATOS -----------------------------------");
        log.debug("codOrganizacion = "+codOrganizacion);
        log.debug("codTramite = "+codTramite);
        log.debug("ocurrenciaTramite = "+ocurrenciaTramite);
        log.debug("numExpediente = "+numExpediente);
        List<FilaContratoRenovacionPlantillaVO> contratosRenovacionPlantilla = new ArrayList<FilaContratoRenovacionPlantillaVO>();
        try
        {
            contratosRenovacionPlantilla = MeLanbide29Manager.getInstance().getContratosExpediente(numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        }
        catch(BDException bde)
        {
            java.util.logging.Logger.getLogger(MELANBIDE29.class.getName()).log(Level.SEVERE, null, bde);
        }
        catch(Exception ex)
        {
            java.util.logging.Logger.getLogger(MELANBIDE29.class.getName()).log(Level.SEVERE, null, ex);
        }
        request.setAttribute("contratosRenovacionPlantilla", contratosRenovacionPlantilla);
        log.debug("----------------------------------- CARGAR CONTRATOS -----------------------------------");
        
        return "/jsp/extension/melanbide29/melanbide29.jsp";
    }
    
    public String cargarNuevoContrato(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            cargarCombos(codOrganizacion, request);
            request.setAttribute("nuevo", "1");
        }
        catch(Exception ex)
        {
            
        }
        return "/jsp/extension/melanbide29/contrato.jsp?codOrganizacionModulo="+codOrganizacion;
    }
    
    
    public String cargarModificarContrato(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        if(request.getParameter("idCon") != null && numExpediente != null)
        {
            try
            {
                cargarCombos(codOrganizacion, request);
                Integer numContrato = Integer.parseInt(request.getParameter("idCon"));
                ContratoRenovacionPlantillaVO contrato = MeLanbide29Manager.getInstance().getContratoExpedientePorNumContrato(numExpediente, numContrato, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if(contrato != null)
                {
                    request.setAttribute("contratoModif", contrato);
                }
                String cerrado = request.getParameter("cerrado");
                if(cerrado != null && cerrado.equalsIgnoreCase("1"))
                {
                    request.setAttribute("cerrado", true);
                }
            }
            catch(Exception ex)
            {
                
            }
        }
        return "/jsp/extension/melanbide29/contrato.jsp?codOrganizacionModulo="+codOrganizacion;
    }
    
    
    public void guardarContrato(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        log.debug("Guardando contrato");
        String codigoOperacion = "";
        
        if(request.getParameter("idCon") != null && numExpediente != null && !numExpediente.equals(""))
        {
            try
            {
                
                Integer numContrato = Integer.parseInt(request.getParameter("idCon"));
                ContratoRenovacionPlantillaVO contrato = MeLanbide29Manager.getInstance().getContratoExpedientePorNumContrato(numExpediente, numContrato, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if(contrato != null)
                {
                    List<PersonaContratoRenovacionPlantillaVO> personas = contrato.getPersonas();
                    PersonaContratoRenovacionPlantillaVO pers = null;
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                    for(int i = 0; i < personas.size(); i++)
                    {
                        pers = personas.get(i);
                        if(pers.getCodTipoPersona() != null)
                        {
                            switch(pers.getCodTipoPersona())
                            {
                                case 1:
                                    pers.setNombre((String)request.getParameter("txtNomPS"));
                                    pers.setApellido1((String)request.getParameter("txtApe1PS"));
                                    pers.setApellido2((String)request.getParameter("txtApe2PS"));
                                    if(request.getParameter("fechaNacimientoPS") != null && !((String)request.getParameter("fechaNacimientoPS")).equalsIgnoreCase(""))
                                    {
                                        String str = (String)request.getParameter("fechaNacimientoPS");
                                        try
                                        {
                                            pers.setFeNac(format.parse((String)request.getParameter("fechaNacimientoPS")));
                                        }
                                        catch(Exception ex)
                                        {
                                            
                                        }
                                    }
                                    if(request.getParameter("txtSexoPS") != null && !((String)request.getParameter("txtSexoPS")).equalsIgnoreCase(""))
                                    {
                                        try
                                        {
                                            String sexo = (String)request.getParameter("txtSexoPS");
                                            if(sexo.equalsIgnoreCase(ConstantesMeLanbide29.SEXO_H))
                                                pers.setSexo(1);
                                            else if(sexo.equalsIgnoreCase(ConstantesMeLanbide29.SEXO_M))
                                                pers.setSexo(2);
                                        }
                                        catch(Exception ex)
                                        {
                                            
                                        }
                                    }
                                    if(request.getParameter("fechaBajaPS") != null && !((String)request.getParameter("fechaBajaPS")).equalsIgnoreCase(""))
                                    {
                                        try
                                        {
                                            pers.setFeBaja(format.parse((String)request.getParameter("fechaBajaPS")));
                                        }
                                        catch(Exception ex)
                                        {
                                            
                                        }
                                    }
                                    if(request.getParameter("txtReduJorPS") != null && !((String)request.getParameter("txtReduJorPS")).equalsIgnoreCase(""))
                                    {
                                        try
                                        {
                                            pers.setPorReduJor(new BigDecimal(request.getParameter("txtReduJorPS")));
                                        }
                                        catch(Exception ex)
                                        {
                                            
                                        }
                                    }
                                    pers.setCnoe(request.getParameter("codCNOEPS"));
                                    break;
                                case 2:
                                    pers.setNombre((String)request.getParameter("txtNomPC"));
                                    pers.setApellido1((String)request.getParameter("txtApe1PC"));
                                    pers.setApellido2((String)request.getParameter("txtApe2PC"));
                                    if(request.getParameter("fechaNacimientoPC") != null && !((String)request.getParameter("fechaNacimientoPC")).equalsIgnoreCase(""))
                                    {
                                        try
                                        {
                                            pers.setFeNac(format.parse((String)request.getParameter("fechaNacimientoPC")));
                                        }
                                        catch(Exception ex)
                                        {
                                            
                                        }
                                    }
                                    pers.setFlMinusvalido(request.getParameter("checkMinusvalidoPC") != null && ((String)request.getParameter("checkMinusvalidoPC")).equalsIgnoreCase(ConstantesMeLanbide29.CHECK_S) ? ConstantesMeLanbide29.CHECK_S : ConstantesMeLanbide29.CHECK_N);
                                    pers.setFlInmigrante(request.getParameter("checkInmigPC") != null && ((String)request.getParameter("checkInmigPC")).equalsIgnoreCase(ConstantesMeLanbide29.CHECK_S) ? ConstantesMeLanbide29.CHECK_S : ConstantesMeLanbide29.CHECK_N);
                                    if(request.getParameter("codEstudiosPC") != null && !((String)request.getParameter("codEstudiosPC")).equalsIgnoreCase(""))
                                    {
                                        try
                                        {
                                            pers.setNivelEstudios(Integer.parseInt(request.getParameter("codEstudiosPC")));
                                        }
                                        catch(Exception ex)
                                        {
                                            
                                        }
                                    }
                                    if(request.getParameter("codColectivoPC") != null && !((String)request.getParameter("codColectivoPC")).equalsIgnoreCase(""))
                                    {
                                        try
                                        {
                                            pers.setColectivo(Integer.parseInt(request.getParameter("codColectivoPC")));
                                        }
                                        catch(Exception ex)
                                        {
                                            
                                        }
                                    }
                                    if(request.getParameter("txtMesesDesempleoPC") != null && !((String)request.getParameter("txtMesesDesempleoPC")).equalsIgnoreCase(""))
                                    {
                                        try
                                        {
                                            pers.setMesesDesempleo(Integer.parseInt(request.getParameter("txtMesesDesempleoPC")));
                                        }
                                        catch(Exception ex)
                                        {
                                            
                                        }
                                    }
                                    pers.setFlPld(request.getParameter("checkPLDPC") != null && ((String)request.getParameter("checkPLDPC")).equalsIgnoreCase(ConstantesMeLanbide29.CHECK_S) ? ConstantesMeLanbide29.CHECK_S : ConstantesMeLanbide29.CHECK_N);
                                    pers.setFlRml(request.getParameter("checkRMLPC") != null && ((String)request.getParameter("checkRMLPC")).equalsIgnoreCase(ConstantesMeLanbide29.CHECK_S) ? ConstantesMeLanbide29.CHECK_S : ConstantesMeLanbide29.CHECK_N);
                                    pers.setFlOtr(request.getParameter("checkOtroPC") != null && ((String)request.getParameter("checkOtroPC")).equalsIgnoreCase(ConstantesMeLanbide29.CHECK_S) ? ConstantesMeLanbide29.CHECK_S : ConstantesMeLanbide29.CHECK_N);
                                    if(request.getParameter("codTipConDurPC") != null && !((String)request.getParameter("codTipConDurPC")).equalsIgnoreCase(""))
                                    {
                                        try
                                        {
                                            pers.setTipoContrato(Integer.parseInt(request.getParameter("codTipConDurPC")));
                                        }
                                        catch(Exception ex)
                                        {
                                            
                                        }
                                    }
                                    if(request.getParameter("fechaAltaPC") != null && !((String)request.getParameter("fechaAltaPC")).equalsIgnoreCase(""))
                                    {
                                        try
                                        {
                                            pers.setFeAlta(format.parse((String)request.getParameter("fechaAltaPC")));
                                        }
                                        catch(Exception ex)
                                        {
                                            
                                        }
                                    }
                                    pers.setTipoJornada(request.getParameter("codTipoJornadaPC"));
                                    pers.setCnoe(request.getParameter("codCNOEPC"));
                                    if(request.getParameter("txtDuracionContratoPC") != null && !((String)request.getParameter("txtDuracionContratoPC")).equalsIgnoreCase(""))
                                    {
                                        try
                                        {
                                            pers.setDuracionContrato(Integer.parseInt(request.getParameter("txtDuracionContratoPC")));
                                        }
                                        catch(Exception ex)
                                        {
                                            
                                        }
                                    }
                                    break;
                                case 3:
                                    pers.setNombre((String)request.getParameter("txtNomPA"));
                                    pers.setApellido1((String)request.getParameter("txtApe1PA"));
                                    pers.setApellido2((String)request.getParameter("txtApe2PA"));
                                    if(request.getParameter("fechaNacimientoPA") != null && !((String)request.getParameter("fechaNacimientoPA")).equalsIgnoreCase(""))
                                    {
                                        try
                                        {
                                            pers.setFeNac(format.parse((String)request.getParameter("fechaNacimientoPA")));
                                        }
                                        catch(Exception ex)
                                        {
                                            
                                        }
                                    }
                                    pers.setFlMinusvalido(request.getParameter("checkMinusvalidoPA") != null && ((String)request.getParameter("checkMinusvalidoPA")).equalsIgnoreCase(ConstantesMeLanbide29.CHECK_S) ? ConstantesMeLanbide29.CHECK_S : ConstantesMeLanbide29.CHECK_N);
                                    pers.setFlInmigrante(request.getParameter("checkInmigPA") != null && ((String)request.getParameter("checkInmigPA")).equalsIgnoreCase(ConstantesMeLanbide29.CHECK_S) ? ConstantesMeLanbide29.CHECK_S : ConstantesMeLanbide29.CHECK_N);
                                    if(request.getParameter("codEstudiosPA") != null && !((String)request.getParameter("codEstudiosPA")).equalsIgnoreCase(""))
                                    {
                                        try
                                        {
                                            pers.setNivelEstudios(Integer.parseInt(request.getParameter("codEstudiosPA")));
                                        }
                                        catch(Exception ex)
                                        {
                                            
                                        }
                                    }
                                    if(request.getParameter("codColectivoPA") != null && !((String)request.getParameter("codColectivoPA")).equalsIgnoreCase(""))
                                    {
                                        try
                                        {
                                            pers.setColectivo(Integer.parseInt(request.getParameter("codColectivoPA")));
                                        }
                                        catch(Exception ex)
                                        {
                                            
                                        }
                                    }
                                    if(request.getParameter("txtMesesDesempleoPA") != null && !((String)request.getParameter("txtMesesDesempleoPA")).equalsIgnoreCase(""))
                                    {
                                        try
                                        {
                                            pers.setMesesDesempleo(Integer.parseInt(request.getParameter("txtMesesDesempleoPA")));
                                        }
                                        catch(Exception ex)
                                        {
                                            
                                        }
                                    }
                                    if(request.getParameter("codTipConDurPA") != null && !((String)request.getParameter("codTipConDurPA")).equalsIgnoreCase(""))
                                    {
                                        try
                                        {
                                            pers.setTipoContrato(Integer.parseInt(request.getParameter("codTipConDurPA")));
                                        }
                                        catch(Exception ex)
                                        {
                                            
                                        }
                                    }
                                    if(request.getParameter("fechaAltaPA") != null && !((String)request.getParameter("fechaAltaPA")).equalsIgnoreCase(""))
                                    {
                                        try
                                        {
                                            pers.setFeAlta(format.parse((String)request.getParameter("fechaAltaPA")));
                                        }
                                        catch(Exception ex)
                                        {
                                            
                                        }
                                    }
                                    pers.setTipoJornada(request.getParameter("codTipoJornadaPA"));
                                    pers.setFlPld(request.getParameter("checkPLDPA") != null && ((String)request.getParameter("checkPLDPA")).equalsIgnoreCase(ConstantesMeLanbide29.CHECK_S) ? ConstantesMeLanbide29.CHECK_S : ConstantesMeLanbide29.CHECK_N);
                                    pers.setFlRml(request.getParameter("checkRMLPA") != null && ((String)request.getParameter("checkRMLPA")).equalsIgnoreCase(ConstantesMeLanbide29.CHECK_S) ? ConstantesMeLanbide29.CHECK_S : ConstantesMeLanbide29.CHECK_N);
                                    pers.setFlOtr(request.getParameter("checkOtroPA") != null && ((String)request.getParameter("checkOtroPA")).equalsIgnoreCase(ConstantesMeLanbide29.CHECK_S) ? ConstantesMeLanbide29.CHECK_S : ConstantesMeLanbide29.CHECK_N);
                                    pers.setCnoe(request.getParameter("codCNOEPA"));
                                    break;
                            }
                        }
                    }
                    boolean exito = MeLanbide29Manager.getInstance().guardarContrato(contrato, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    if(exito)
                        codigoOperacion = "0";
                    else
                        codigoOperacion = "2";
                }
            }
            catch(BDException ex)
            {
                codigoOperacion = "1";
            }
            catch(Exception ex)
            {
                codigoOperacion = "2";
            }
        }
        else
        {
            codigoOperacion = "3";
        }
        request.setAttribute("cerrarPagina", "1");
        
        
        List<FilaContratoRenovacionPlantillaVO> contratosRenovacionPlantilla = new ArrayList<FilaContratoRenovacionPlantillaVO>();
        try
        {
            contratosRenovacionPlantilla = MeLanbide29Manager.getInstance().getContratosExpediente(numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        }
        catch(BDException bde)
        {
            java.util.logging.Logger.getLogger(MELANBIDE29.class.getName()).log(Level.SEVERE, null, bde);
        }
        catch(Exception ex)
        {
            java.util.logging.Logger.getLogger(MELANBIDE29.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        
        log.debug("Contrato guardado");
        
        
        
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
            xmlSalida.append("<CODIGO_OPERACION>");
                xmlSalida.append(codigoOperacion);
            xmlSalida.append("</CODIGO_OPERACION>");
            for(FilaContratoRenovacionPlantillaVO fila : contratosRenovacionPlantilla)
            {
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
        
        
        //return "/jsp/extension/melanbide29/contrato.jsp";
    }
    
    public void crearContrato(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        log.debug("Creando contrato");
        String codigoOperacion = "";
        if(numExpediente != null && !numExpediente.equals(""))
        {
            List<PersonaContratoRenovacionPlantillaVO> personas = new ArrayList<PersonaContratoRenovacionPlantillaVO>();
            PersonaContratoRenovacionPlantillaVO p1 = new PersonaContratoRenovacionPlantillaVO();//Persona sustituida
            PersonaContratoRenovacionPlantillaVO p2 = new PersonaContratoRenovacionPlantillaVO();//Persona contratada
            PersonaContratoRenovacionPlantillaVO p3 = new PersonaContratoRenovacionPlantillaVO();//Persona contrato adicional

            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

            try
            {
                //Contrato
                ContratoRenovacionPlantillaVO contrato = new ContratoRenovacionPlantillaVO();
                
                
                
                int numContrato = MeLanbide29Manager.getInstance().getNuevoNumContrato(numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if(numContrato < 0)
                    throw new BDException();
                String ejercicio = null;
                try
                {
                    String[] datos = numExpediente.split(ConstantesMeLanbide29.BARRA_SEPARADORA);
                    ejercicio = datos[0];
                }
                catch(Exception ex)
                {
                    log.debug("No se ha podido obtener el ejercicio al que pertenece el expediente");
                }
                contrato.setEjercicio(Integer.parseInt(ejercicio));
                contrato.setEntorno(codOrganizacion);
                contrato.setNumContrato(numContrato);
                contrato.setNumExpediente(numExpediente);
                contrato.setProcedimiento(ConstantesMeLanbide29.COD_PROC);
                //Persona sustituida
                p1.setCodTipoPersona(1);
                p1.setCodTercero(Long.parseLong(request.getParameter("idTerPS")));
                p1.setNumDoc(request.getParameter("txtDniPS"));
                p1.setNombre(request.getParameter("txtNomPS"));
                p1.setApellido1(request.getParameter("txtApe1PS"));
                p1.setApellido2(request.getParameter("txtApe2PS"));
                if(request.getParameter("fechaNacimientoPS") != null && !request.getParameter("fechaNacimientoPS").equalsIgnoreCase(""))
                {
                    try
                    {
                        p1.setFeNac(format.parse(request.getParameter("fechaNacimientoPS")));
                    }
                    catch(Exception ex)
                    {

                    }
                }
                if(request.getParameter("txtSexoPS") != null && !request.getParameter("txtSexoPS").equalsIgnoreCase(""))
                {
                    try
                    {
                        String sexo = request.getParameter("txtSexoPS");
                        if(sexo.equalsIgnoreCase(ConstantesMeLanbide29.SEXO_H))
                            p1.setSexo(1);
                        else if(sexo.equalsIgnoreCase(ConstantesMeLanbide29.SEXO_M))
                            p1.setSexo(2);
                    }
                    catch(Exception ex)
                    {

                    }
                }
                if(request.getParameter("fechaBajaPS") != null && !request.getParameter("fechaBajaPS").equalsIgnoreCase(""))
                {
                    try
                    {
                        p1.setFeBaja(format.parse(request.getParameter("fechaBajaPS")));
                    }
                    catch(Exception ex)
                    {

                    }
                }
                if(request.getParameter("txtReduJorPS") != null && !request.getParameter("txtReduJorPS").equalsIgnoreCase(""))
                {
                    try
                    {
                        p1.setPorReduJor(new BigDecimal(request.getParameter("txtReduJorPS")));
                    }
                    catch(Exception ex)
                    {

                    }
                }
                p1.setCnoe(request.getParameter("codCNOEPS"));
                contrato.addPersona(p1);

                //Persona contratada
                p2.setCodTipoPersona(2);
                p2.setCodTercero(Long.parseLong(request.getParameter("idTerPC")));
                p2.setNumDoc(request.getParameter("txtDniPC"));
                p2.setNombre(request.getParameter("txtNomPC"));
                p2.setApellido1(request.getParameter("txtApe1PC"));
                p2.setApellido2(request.getParameter("txtApe2PC"));

                if(request.getParameter("fechaNacimientoPC") != null && !request.getParameter("fechaNacimientoPC").equalsIgnoreCase(""))
                {
                    try
                    {
                        p2.setFeNac(format.parse(request.getParameter("fechaNacimientoPC")));
                    }
                    catch(Exception ex)
                    {

                    }
                }
                if(request.getParameter("txtSexoPC") != null && !request.getParameter("txtSexoPC").equalsIgnoreCase(""))
                {
                    try
                    {
                        String sexo = request.getParameter("txtSexoPC");
                        if(sexo.equalsIgnoreCase(ConstantesMeLanbide29.SEXO_H))
                            p2.setSexo(1);
                        else if(sexo.equalsIgnoreCase(ConstantesMeLanbide29.SEXO_M))
                            p2.setSexo(2);
                    }
                    catch(Exception ex)
                    {

                    }
                }
                log.debug("###### CHECK MINUSVALIDO = "+request.getParameter("checkMinusvalidoPC"));
                p2.setFlMinusvalido(request.getParameter("checkMinusvalidoPC") != null && request.getParameter("checkMinusvalidoPC").equalsIgnoreCase(ConstantesMeLanbide29.CHECK_S) ? ConstantesMeLanbide29.CHECK_S : ConstantesMeLanbide29.CHECK_N);
                p2.setFlInmigrante(request.getParameter("checkInmigPC") != null && request.getParameter("checkInmigPC").equalsIgnoreCase(ConstantesMeLanbide29.CHECK_S) ? ConstantesMeLanbide29.CHECK_S : ConstantesMeLanbide29.CHECK_N);
                if(request.getParameter("codEstudiosPC") != null && !request.getParameter("codEstudiosPC").equalsIgnoreCase(""))
                {
                    try
                    {
                        p2.setNivelEstudios(Integer.parseInt(request.getParameter("codEstudiosPC")));
                    }
                    catch(Exception ex)
                    {

                    }
                }
                if(request.getParameter("codColectivoPC") != null && !request.getParameter("codColectivoPC").equalsIgnoreCase(""))
                {
                    try
                    {
                        p2.setColectivo(Integer.parseInt(request.getParameter("codColectivoPC")));
                    }
                    catch(Exception ex)
                    {

                    }
                }
                if(request.getParameter("txtMesesDesempleoPC") != null && !request.getParameter("txtMesesDesempleoPC").equalsIgnoreCase(""))
                {
                    try
                    {
                        p2.setMesesDesempleo(Integer.parseInt(request.getParameter("txtMesesDesempleoPC")));
                    }
                    catch(Exception ex)
                    {

                    }
                }
                p2.setFlPld(request.getParameter("checkPLDPC") != null && request.getParameter("checkPLDPC").equalsIgnoreCase(ConstantesMeLanbide29.CHECK_S) ? ConstantesMeLanbide29.CHECK_S : ConstantesMeLanbide29.CHECK_N);
                p2.setFlRml(request.getParameter("checkRMLPC") != null && request.getParameter("checkRMLPC").equalsIgnoreCase(ConstantesMeLanbide29.CHECK_S) ? ConstantesMeLanbide29.CHECK_S : ConstantesMeLanbide29.CHECK_N);
                p2.setFlOtr(request.getParameter("checkOtroPC") != null && request.getParameter("checkOtroPC").equalsIgnoreCase(ConstantesMeLanbide29.CHECK_S) ? ConstantesMeLanbide29.CHECK_S : ConstantesMeLanbide29.CHECK_N);
                if(request.getParameter("codTipConDurPC") != null && !request.getParameter("codTipConDurPC").equalsIgnoreCase(""))
                {
                    try
                    {
                        p2.setTipoContrato(Integer.parseInt(request.getParameter("codTipConDurPC")));
                    }
                    catch(Exception ex)
                    {

                    }
                }
                if(request.getParameter("fechaAltaPC") != null && !request.getParameter("fechaAltaPC").equalsIgnoreCase(""))
                {
                    try
                    {
                        p2.setFeAlta(format.parse(request.getParameter("fechaAltaPC")));
                    }
                    catch(Exception ex)
                    {

                    }
                }
                p2.setTipoJornada(request.getParameter("codTipoJornadaPC"));

                if(request.getParameter("fechaFinPC") != null && !request.getParameter("fechaFinPC").equalsIgnoreCase(""))
                {
                    try
                    {
                        p2.setFeFinContrato(format.parse(request.getParameter("fechaFinPC")));
                    }
                    catch(Exception ex)
                    {

                    }
                }
                if(request.getParameter("retribPC") != null && !request.getParameter("retribPC").equalsIgnoreCase(""))
                {
                    try
                    {
                        p2.setRetSalarial(new BigDecimal(request.getParameter("retribPC")));
                    }
                    catch(Exception ex)
                    {

                    }
                }
                p2.setCnoe(request.getParameter("codCNOEPC"));
                if(request.getParameter("txtDuracionContratoPC") != null && !((String)request.getParameter("txtDuracionContratoPC")).equalsIgnoreCase(""))
                {
                    try
                    {
                        p2.setDuracionContrato(Integer.parseInt(request.getParameter("txtDuracionContratoPC")));
                    }
                    catch(Exception ex)
                    {

                    }
                }
                if(request.getParameter("subPC") != null && !request.getParameter("subPC").equalsIgnoreCase(""))
                {
                    try
                    {
                        p2.setImpSubvencion(new BigDecimal(request.getParameter("subPC")));
                    }
                    catch(Exception ex)
                    {

                    }
                }
                contrato.addPersona(p2);
                //Persona de contrato adicional
                if(request.getParameter("idTerPA") != null && !request.getParameter("idTerPA").equalsIgnoreCase(""))
                {
                    p3.setCodTipoPersona(3);
                    p3.setCodTercero(Long.parseLong(request.getParameter("idTerPA")));
                    p3.setNumDoc(request.getParameter("txtDniPA"));
                    p3.setNombre(request.getParameter("txtNomPA"));
                    p3.setApellido1(request.getParameter("txtApe1PA"));
                    p3.setApellido2(request.getParameter("txtApe2PA"));

                    if(request.getParameter("fechaNacimientoPA") != null && !request.getParameter("fechaNacimientoPA").equalsIgnoreCase(""))
                    {
                        try
                        {
                            p3.setFeNac(format.parse(request.getParameter("fechaNacimientoPA")));
                        }
                        catch(Exception ex)
                        {

                        }
                    }
                    if(request.getParameter("txtSexoPA") != null && !request.getParameter("txtSexoPA").equalsIgnoreCase(""))
                    {
                        try
                        {
                            String sexo = request.getParameter("txtSexoPA");
                            if(sexo.equalsIgnoreCase(ConstantesMeLanbide29.SEXO_H))
                                p3.setSexo(1);
                            else if(sexo.equalsIgnoreCase(ConstantesMeLanbide29.SEXO_M))
                                p3.setSexo(2);
                        }
                        catch(Exception ex)
                        {

                        }
                    }
                    p3.setFlMinusvalido(request.getParameter("checkMinusvalidoPA") != null && request.getParameter("checkMinusvalidoPA").equalsIgnoreCase(ConstantesMeLanbide29.CHECK_S) ? ConstantesMeLanbide29.CHECK_S : ConstantesMeLanbide29.CHECK_N);
                    p3.setFlInmigrante(request.getParameter("checkInmigPA") != null && request.getParameter("checkInmigPA").equalsIgnoreCase(ConstantesMeLanbide29.CHECK_S) ? ConstantesMeLanbide29.CHECK_S : ConstantesMeLanbide29.CHECK_N);
                    if(request.getParameter("codEstudiosPA") != null && !request.getParameter("codEstudiosPA").equalsIgnoreCase(""))
                    {
                        try
                        {
                            p3.setNivelEstudios(Integer.parseInt(request.getParameter("codEstudiosPA")));
                        }
                        catch(Exception ex)
                        {

                        }
                    }
                    if(request.getParameter("codColectivoPA") != null && !request.getParameter("codColectivoPA").equalsIgnoreCase(""))
                    {
                        try
                        {
                            p3.setColectivo(Integer.parseInt(request.getParameter("codColectivoPA")));
                        }
                        catch(Exception ex)
                        {

                        }
                    }
                    if(request.getParameter("txtMesesDesempleoPA") != null && !request.getParameter("txtMesesDesempleoPA").equalsIgnoreCase(""))
                    {
                        try
                        {
                            p3.setMesesDesempleo(Integer.parseInt(request.getParameter("txtMesesDesempleoPA")));
                        }
                        catch(Exception ex)
                        {

                        }
                    }
                    if(request.getParameter("codTipConDurPA") != null && !request.getParameter("codTipConDurPA").equalsIgnoreCase(""))
                    {
                        try
                        {
                            p3.setTipoContrato(Integer.parseInt(request.getParameter("codTipConDurPA")));
                        }
                        catch(Exception ex)
                        {

                        }
                    }
                    if(request.getParameter("fechaAltaPA") != null && !request.getParameter("fechaAltaPA").equalsIgnoreCase(""))
                    {
                        try
                        {
                            p3.setFeAlta(format.parse(request.getParameter("fechaAltaPA")));
                        }
                        catch(Exception ex)
                        {

                        }
                    }
                    p3.setTipoJornada(request.getParameter("codTipoJornadaPA"));
                    p3.setFlPld(request.getParameter("checkPLDPA") != null && request.getParameter("checkPLDPA").equalsIgnoreCase(ConstantesMeLanbide29.CHECK_S) ? ConstantesMeLanbide29.CHECK_S : ConstantesMeLanbide29.CHECK_N);
                    p3.setFlRml(request.getParameter("checkRMLPA") != null && request.getParameter("checkRMLPA").equalsIgnoreCase(ConstantesMeLanbide29.CHECK_S) ? ConstantesMeLanbide29.CHECK_S : ConstantesMeLanbide29.CHECK_N);
                    p3.setFlOtr(request.getParameter("checkOtroPA") != null && request.getParameter("checkOtroPA").equalsIgnoreCase(ConstantesMeLanbide29.CHECK_S) ? ConstantesMeLanbide29.CHECK_S : ConstantesMeLanbide29.CHECK_N);
                    if(request.getParameter("retribPA") != null && !request.getParameter("retribPA").equalsIgnoreCase(""))
                    {
                        try
                        {
                            p3.setRetSalarial(new BigDecimal(request.getParameter("retribPA")));
                        }
                        catch(Exception ex)
                        {

                        }
                    }
                    p3.setCnoe(request.getParameter("codCNOEPA"));
                    contrato.addPersona(p3);
                }
                boolean exito = MeLanbide29Manager.getInstance().crearContrato(contrato, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if(exito)
                    codigoOperacion = "0";
                else
                    codigoOperacion = "1";
            }
            catch(BDException bde)
            {
                codigoOperacion = "1";
            }
            catch(Exception ex)
            {
                codigoOperacion = "2";
            }


            
        }
        else
        {
            codigoOperacion = "3";
        }
        
        log.debug("Contrato creado");
        
        List<FilaContratoRenovacionPlantillaVO> contratosRenovacionPlantilla = new ArrayList<FilaContratoRenovacionPlantillaVO>();
        try
        {
            contratosRenovacionPlantilla = MeLanbide29Manager.getInstance().getContratosExpediente(numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        }
        catch(BDException bde)
        {
            java.util.logging.Logger.getLogger(MELANBIDE29.class.getName()).log(Level.SEVERE, null, bde);
        }
        catch(Exception ex)
        {
            java.util.logging.Logger.getLogger(MELANBIDE29.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
            xmlSalida.append("<CODIGO_OPERACION>");
                xmlSalida.append(codigoOperacion);
            xmlSalida.append("</CODIGO_OPERACION>");
            for(FilaContratoRenovacionPlantillaVO fila : contratosRenovacionPlantilla)
            {
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
    
    public void eliminarContrato(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        if(log.isDebugEnabled())
            log.debug("Eliminando contrato");
        String codigoOperacion = "";
        
        if(request.getParameter("idCon") != null && numExpediente != null && !numExpediente.equals(""))
        {
            try
            {
                Integer numContrato = Integer.parseInt(request.getParameter("idCon"));
                boolean exito = MeLanbide29Manager.getInstance().eliminarContrato(numExpediente, numContrato, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if(exito)
                    codigoOperacion = "0";
                else
                    codigoOperacion = "1";
            }
            catch(BDException ex)
            {
                codigoOperacion = "1";
            }
            catch(Exception ex)
            {
                codigoOperacion = "2";
            }
        }
        else
        {
            codigoOperacion = "3";
        }
        
        List<FilaContratoRenovacionPlantillaVO> contratosRenovacionPlantilla = new ArrayList<FilaContratoRenovacionPlantillaVO>();
        try
        {
            contratosRenovacionPlantilla = MeLanbide29Manager.getInstance().getContratosExpediente(numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        }
        catch(BDException bde)
        {
            java.util.logging.Logger.getLogger(MELANBIDE29.class.getName()).log(Level.SEVERE, null, bde);
        }
        catch(Exception ex)
        {
            java.util.logging.Logger.getLogger(MELANBIDE29.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        if(log.isDebugEnabled())
            log.debug("Contrato eliminado");
        
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
            xmlSalida.append("<CODIGO_OPERACION>");
                xmlSalida.append(codigoOperacion);
            xmlSalida.append("</CODIGO_OPERACION>");
            for(FilaContratoRenovacionPlantillaVO fila : contratosRenovacionPlantilla)
            {
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
    
    public void busquedaTerceros(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        String dni = "";
        String nom = "";
        String ape1 = "";
        String ape2 = "";
        String codigoOperacion = "";
        if(request.getParameter("dni") != null)
        {
            dni = (String)request.getParameter("dni");
        }
        if(request.getParameter("nom") != null)
        {
            nom = (String)request.getParameter("nom");
        }
        if(request.getParameter("ape1") != null)
        {
            ape1 = (String)request.getParameter("ape1");
        }
        if(request.getParameter("ape2") != null)
        {
            ape2 = (String)request.getParameter("ape2");
        }
        Tercero terBusq = new Tercero();
        terBusq.setApellido1(ape1);
        terBusq.setApellido2(ape2);
        terBusq.setDni(dni);
        terBusq.setNombre(nom);
        List<Tercero> retList = new ArrayList<Tercero>();
        try 
        {
            retList = MeLanbide29Manager.getInstance().busquedaTerceros(terBusq, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            codigoOperacion = "0";
        } 
        catch (Exception ex) 
        {
            codigoOperacion = "1";
            java.util.logging.Logger.getLogger(MELANBIDE29.class.getName()).log(Level.SEVERE, null, ex);
        }
        StringBuffer xmlSalida = new StringBuffer();
            xmlSalida.append("<RESPUESTA>");
                xmlSalida.append("<CODIGO_OPERACION>");
                    xmlSalida.append(codigoOperacion);
                xmlSalida.append("</CODIGO_OPERACION>");
                for(Tercero t : retList)
                {
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
     * Operación que recupera las listas para los combos y los guarda en la request
     * @param request
     */
    private void cargarCombos(int codOrganizacion, HttpServletRequest request)
    {
        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        
        ArrayList<ValorCampoDesplegableModuloIntegracionVO> listaEstudios = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
        ArrayList<ValorCampoDesplegableModuloIntegracionVO> listaColectivos = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
        ArrayList<ValorCampoDesplegableModuloIntegracionVO> listaTipConDur = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
        ArrayList<ValorCampoDesplegableModuloIntegracionVO> listaTipoJornada = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
        ArrayList<ValorCampoDesplegableModuloIntegracionVO> listaCNOE = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
        
        //Combo ESTUDIOS
        try
        {
            String campoDesplegable = ConfigurationParameter.getParameter(String.valueOf(codOrganizacion) + ConstantesMeLanbide29.BARRA_SEPARADORA
                    + ConstantesMeLanbide29.CAMPO_SUPLEMENTARIO_ESTUDIOS, ConstantesMeLanbide29.FICHERO_PROPIEDADES);
        
            SalidaIntegracionVO salidaIntegracion = el.getCampoDesplegable(String.valueOf(codOrganizacion), campoDesplegable);
            listaEstudios = salidaIntegracion.getCampoDesplegable().getValores();
            
            log.debug("CAMPO DESPLEGABLE = "+campoDesplegable);
            log.debug("ESTUDIOS ENCOTRADOS: "+listaEstudios.size());
        }
        catch(Exception ex)
        {
            
        }
        request.setAttribute("listaEstudios", listaEstudios);
        
        //Combo COLECTIVOS
        try
        {
            String campoDesplegable = ConfigurationParameter.getParameter(String.valueOf(codOrganizacion) + ConstantesMeLanbide29.BARRA_SEPARADORA
                    + ConstantesMeLanbide29.CAMPO_SUPLEMENTARIO_COLECTIVOS, ConstantesMeLanbide29.FICHERO_PROPIEDADES);
        
            SalidaIntegracionVO salidaIntegracion = el.getCampoDesplegable(String.valueOf(codOrganizacion), campoDesplegable);
            listaColectivos = salidaIntegracion.getCampoDesplegable().getValores();
            
            log.debug("CAMPO DESPLEGABLE = "+campoDesplegable);
            log.debug("COLECTIVOS ENCOTRADOS: "+listaColectivos.size());
        }
        catch(Exception ex)
        {
            
        }
        request.setAttribute("listaColectivos", listaColectivos);
        
        //Combo TIPCONDUR
        try
        {
            String campoDesplegable = ConfigurationParameter.getParameter(String.valueOf(codOrganizacion) + ConstantesMeLanbide29.BARRA_SEPARADORA
                    + ConstantesMeLanbide29.CAMPO_SUPLEMENTARIO_TIP_CON_DUR, ConstantesMeLanbide29.FICHERO_PROPIEDADES);
        
            SalidaIntegracionVO salidaIntegracion = el.getCampoDesplegable(String.valueOf(codOrganizacion), campoDesplegable);
            listaTipConDur = salidaIntegracion.getCampoDesplegable().getValores();
            
            log.debug("CAMPO DESPLEGABLE = "+campoDesplegable);
            log.debug("TIP CON DUR ENCOTRADOS: "+listaTipConDur.size());
        }
        catch(Exception ex)
        {
            
        }
        request.setAttribute("listaTipConDur", listaTipConDur);
        
        //Combo TIPO JORNADA
        try
        {
            String campoDesplegable = ConfigurationParameter.getParameter(String.valueOf(codOrganizacion) + ConstantesMeLanbide29.BARRA_SEPARADORA
                    + ConstantesMeLanbide29.CAMPO_SUPLEMENTARIO_TIPO_JORNADA, ConstantesMeLanbide29.FICHERO_PROPIEDADES);
        
            SalidaIntegracionVO salidaIntegracion = el.getCampoDesplegable(String.valueOf(codOrganizacion), campoDesplegable);
            listaTipoJornada = salidaIntegracion.getCampoDesplegable().getValores();
            
            log.debug("CAMPO DESPLEGABLE = "+campoDesplegable);
            log.debug("TIPO JORNADA ENCOTRADOS: "+listaTipoJornada.size());
        }
        catch(Exception ex)
        {
            
        }
        request.setAttribute("listaTipoJornada", listaTipoJornada);
        
        //Combo CNOE
        try
        {
            String campoDesplegable = ConfigurationParameter.getParameter(String.valueOf(codOrganizacion) + ConstantesMeLanbide29.BARRA_SEPARADORA
                    + ConstantesMeLanbide29.CAMPO_SUPLEMENTARIO_CNOE, ConstantesMeLanbide29.FICHERO_PROPIEDADES);
        
            SalidaIntegracionVO salidaIntegracion = el.getCampoDesplegable(String.valueOf(codOrganizacion), campoDesplegable);
            listaCNOE = salidaIntegracion.getCampoDesplegable().getValores();
            
            log.debug("CAMPO DESPLEGABLE = "+campoDesplegable);
            log.debug("CNOE ENCOTRADOS: "+listaCNOE.size());
        }
        catch(Exception ex)
        {
            
        }
        request.setAttribute("listaCNOE", listaCNOE);
    }
    
    public String getListaDocumentosContablesExpediente(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            List<FilaDocumentoContableVO> listado = MeLanbide29Manager.getInstance().getListaDocumentosContablesExpediente(numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            request.setAttribute("listaDocContables", listado);
        }
        catch(Exception ex)
        {
            
        }
        return "/jsp/extension/melanbide29/listaDocumentosContables.jsp";
    }
}//class