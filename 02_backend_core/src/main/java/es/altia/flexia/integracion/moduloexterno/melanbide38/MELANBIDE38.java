/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide38;

import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.melanbide38.manager.MeLanbide38Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide38.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide38.util.ConstantesMeLanbide38;
import es.altia.flexia.integracion.moduloexterno.melanbide38.util.ValidadorContratos;
import es.altia.flexia.integracion.moduloexterno.melanbide38.vo.ContratoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide38.vo.FilaContratoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide38.vo.Tercero;
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
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author santiagoc
 */
public class MELANBIDE38 extends ModuloIntegracionExterno 
{
    //Logger
    private static Logger log = LogManager.getLogger(MELANBIDE38.class);
    
    public String cargarContratos(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
        List<FilaContratoVO> contratos = new ArrayList<FilaContratoVO>();
        try
        {
            contratos = MeLanbide38Manager.getInstance().getContratosExpediente(numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        }
        catch(BDException bde)
        {
            java.util.logging.Logger.getLogger(MELANBIDE38.class.getName()).log(Level.SEVERE, null, bde);
        }
        catch(Exception ex)
        {
            java.util.logging.Logger.getLogger(MELANBIDE38.class.getName()).log(Level.SEVERE, null, ex);
        }
        request.setAttribute("contratos", contratos);
        return "/jsp/extension/melanbide38/melanbide38.jsp";
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
        return "/jsp/extension/melanbide38/nuevoContrato.jsp?codOrganizacionModulo="+codOrganizacion;
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
            retList = MeLanbide38Manager.getInstance().busquedaTerceros(terBusq, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            codigoOperacion = "0";
        } 
        catch (Exception ex) 
        {
            codigoOperacion = "1";
            java.util.logging.Logger.getLogger(MELANBIDE38.class.getName()).log(Level.SEVERE, null, ex);
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
    
    public void crearContrato(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        
        log.debug("Creando contrato");
        String codigoOperacion = "";
        if(numExpediente != null && !numExpediente.equals(""))
        {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

            try
            {
                //Contrato
                ContratoVO contrato = new ContratoVO();
                int numContrato = MeLanbide38Manager.getInstance().getNuevoNumContrato(numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if(numContrato < 0)
                {
                    throw new BDException();
                }
                
                //Recojo los parametros
                String idTer = request.getParameter("idTer");
                String dni = request.getParameter("dni");
                String nom = request.getParameter("nom");
                String ap1 = request.getParameter("ap1");
                String ap2 = request.getParameter("ap2");
                String fNac = request.getParameter("fNac");
                String sexo = request.getParameter("sexo");
                String mesesDesempleo = request.getParameter("mesesDesempleo");
                String nEstudios = request.getParameter("nEstudios");
                String minus = request.getParameter("minus");
                String inmig = request.getParameter("inmig");
                String pld = request.getParameter("pld");
                String rml = request.getParameter("rml");
                String otr = request.getParameter("otr");
                String fAlta = request.getParameter("fAlta");
                String duracionContrato = request.getParameter("duracionContrato");
                String tipoContrato = request.getParameter("tipoContrato");
                String tipoJornada = request.getParameter("tipoJornada");
                String porJornada = request.getParameter("porJornada");
                String tipConDur = request.getParameter("tipConDur");
                String salario = request.getParameter("salario");
                String cnoe = request.getParameter("cnoe");
                String segSocial = request.getParameter("segSocial");
                String colectivo = request.getParameter("colectivo");
                String concedido = request.getParameter("concedido");
                String modifRes = request.getParameter("modifRes");
                String recurso = request.getParameter("recurso");
                String cPro = request.getParameter("cPro");
                //Contrato
                try
                {
                    String[] datos = numExpediente.split(ConstantesMeLanbide38.BARRA_SEPARADORA);
                    String ejercicio = datos[0];
                    contrato.setAp1(ap1 != null && !ap1.equals("") ? ap1.toUpperCase() : null);
                    contrato.setAp2(ap2 != null && !ap2.equals("") ? ap2.toUpperCase() : null);
                    contrato.setCnoe(cnoe != null && !cnoe.equals("") ? cnoe.toUpperCase() : "");
                    contrato.setCol(colectivo != null && !colectivo.equals("") ? Integer.parseInt(colectivo) : null);
                    contrato.setCss(segSocial != null && !segSocial.equals("") ? new BigDecimal(segSocial.replaceAll(",", "\\.")) : null);
                    contrato.setDco(duracionContrato != null && !duracionContrato.equals("") ? Integer.parseInt(duracionContrato) : null);
                    contrato.setDoc(dni != null && !dni.equals("") ? dni.toUpperCase() : null);
                    contrato.setEje(Integer.parseInt(ejercicio));
                    contrato.setFac(fAlta != null && !fAlta.equals("") ? format.parse(fAlta) : null);
                    contrato.setFfc(null);
                    contrato.setFna(fNac != null && !fNac.equals("") ? format.parse(fNac) : null);
                    contrato.setImp(concedido != null && !concedido.equals("") ? new BigDecimal(concedido.replaceAll(",", "\\.")) : null);
                    contrato.setImr(modifRes != null && !modifRes.equals("") ? new BigDecimal(modifRes.replaceAll(",", "\\.")) : null);
                    contrato.setInm(inmig != null && !inmig.equals("") ? inmig.toUpperCase() : null);
                    contrato.setIre(recurso != null && !recurso.equals("") ? new BigDecimal(recurso.replaceAll(",", "\\.")) : null);
                    contrato.setMde(mesesDesempleo != null && !mesesDesempleo.equals("") ? Integer.parseInt(mesesDesempleo) : null);
                    contrato.setMin(minus != null && !minus.equals("") ? minus.toUpperCase() : null);
                    contrato.setMun(codOrganizacion);
                    contrato.setnCon(numContrato);
                    contrato.setNes(nEstudios != null && !nEstudios.equals("") ? Integer.parseInt(nEstudios) : null);
                    contrato.setNom(nom != null && !nom.equals("") ? nom.toUpperCase() : null);
                    contrato.setNum(numExpediente);
                    contrato.setOtr(otr != null && !otr.equals("") ? otr.toUpperCase() : null);
                    contrato.setPjt(porJornada != null && !porJornada.equalsIgnoreCase("") ? new BigDecimal(porJornada.replaceAll(",", "\\.")) : null);
                    contrato.setPld(pld != null && !pld.equals("") ? pld.toUpperCase() : null);
                    contrato.setPro(ConstantesMeLanbide38.COD_PROCEDIMIENTO);
                    contrato.setRml(rml != null && !rml.equals("") ? rml.toUpperCase() : null);
                    contrato.setSal(salario != null && !salario.equals("") ? new BigDecimal(salario.replaceAll(",", "\\.")) : null);
                    if(sexo != null && !sexo.equals(""))
                    {
                        if(sexo.equalsIgnoreCase(ConstantesMeLanbide38.SEXO_H))
                        {
                            contrato.setSex(1);
                        }
                        else if(sexo.equalsIgnoreCase(ConstantesMeLanbide38.SEXO_M))
                        {
                            contrato.setSex(2);
                        }

                    }
                    else
                    {
                        contrato.setSex(null);
                    }
                    contrato.setTic(tipoContrato != null && !tipoContrato.equals("") ? Integer.parseInt(tipoContrato) : null);
                    contrato.setTerCod(idTer != null && !idTer.equals("") ? Long.parseLong(idTer) : null);
                    contrato.setTco(tipConDur != null && !tipConDur.equals("") ? Integer.parseInt(tipConDur) : null);
                    contrato.setTjo(tipoJornada != null && !tipoJornada.equals("") ? tipoJornada.toUpperCase() : null);
                    contrato.setcPro(cPro != null && !cPro.equals("") ? Integer.parseInt(cPro) : null);
                    
                    if(ValidadorContratos.getInstance().validarContrato(contrato))
                    {
                        boolean exito = MeLanbide38Manager.getInstance().crearContrato(contrato, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                        if(exito)
                        {
                            codigoOperacion = "0";
                        }
                        else
                        {
                            codigoOperacion = "1";
                        }
                    }
                    else
                    {
                        codigoOperacion = "3";
                    }
                }
                catch(Exception ex)
                {
                    codigoOperacion = "3";
                }
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
        
        List<FilaContratoVO> contratos = new ArrayList<FilaContratoVO>();
        try
        {
            contratos = MeLanbide38Manager.getInstance().getContratosExpediente(numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        }
        catch(BDException bde)
        {
            java.util.logging.Logger.getLogger(MELANBIDE38.class.getName()).log(Level.SEVERE, null, bde);
        }
        catch(Exception ex)
        {
            java.util.logging.Logger.getLogger(MELANBIDE38.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
            xmlSalida.append("<CODIGO_OPERACION>");
                xmlSalida.append(codigoOperacion);
            xmlSalida.append("</CODIGO_OPERACION>");
            for(FilaContratoVO fila : contratos)
            {
                xmlSalida.append("<FILA>");
                    xmlSalida.append("<ID>");
                        xmlSalida.append(fila.getNumContrato());
                    xmlSalida.append("</ID>");
                    xmlSalida.append("<DNI>");
                        xmlSalida.append(fila.getDni() != null ? fila.getDni().toUpperCase() : "");
                    xmlSalida.append("</DNI>");
                    xmlSalida.append("<NOMBREAPELLIDOS>");
                        xmlSalida.append(fila.getNombreApellidos());
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
    
    
    public String cargarModificarContrato(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        if(request.getParameter("idCon") != null && numExpediente != null)
        {
            try
            {
                cargarCombos(codOrganizacion, request);
                Integer numContrato = Integer.parseInt(request.getParameter("idCon"));
                ContratoVO contrato = MeLanbide38Manager.getInstance().getContratoExpedientePorNumContrato(numExpediente, numContrato, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
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
        return "/jsp/extension/melanbide38/nuevoContrato.jsp?codOrganizacionModulo="+codOrganizacion;
    }
    
    public void modificarContrato(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        
        log.debug("Modificando contrato");
        String codigoOperacion = "";
        AdaptadorSQLBD adapt = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (SQLException ex) {
            codigoOperacion = "1";
        }
        if(numExpediente != null && !numExpediente.equals(""))
        {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

            try
            {
                String idCon = request.getParameter("idCon");
                if(idCon != null && !idCon.equals(""))
                {
                    Integer numContrato = Integer.parseInt(idCon);
                    
                    //Contrato
                    ContratoVO contrato = MeLanbide38Manager.getInstance().getContratoExpedientePorNumContrato(numExpediente, numContrato, adapt);
                    if(contrato != null)
                    {
                        //Recojo los parametros
                        String idTer = request.getParameter("idTer");
                        String dni = request.getParameter("dni");
                        String nom = request.getParameter("nom");
                        String ap1 = request.getParameter("ap1");
                        String ap2 = request.getParameter("ap2");
                        String fNac = request.getParameter("fNac");
                        String sexo = request.getParameter("sexo");
                        String mesesDesempleo = request.getParameter("mesesDesempleo");
                        String nEstudios = request.getParameter("nEstudios");
                        String minus = request.getParameter("minus");
                        String inmig = request.getParameter("inmig");
                        String pld = request.getParameter("pld");
                        String rml = request.getParameter("rml");
                        String otr = request.getParameter("otr");
                        String fAlta = request.getParameter("fAlta");
                        String duracionContrato = request.getParameter("duracionContrato");
                        String tipoContrato = request.getParameter("tipoContrato");
                        String tipoJornada = request.getParameter("tipoJornada");
                        String porJornada = request.getParameter("porJornada");
                        String tipConDur = request.getParameter("tipConDur");
                        String salario = request.getParameter("salario");
                        String cnoe = request.getParameter("cnoe");
                        String segSocial = request.getParameter("segSocial");
                        String colectivo = request.getParameter("colectivo");
                        String concedido = request.getParameter("concedido");
                        String modifRes = request.getParameter("modifRes");
                        String recurso = request.getParameter("recurso");
                        String cPro = request.getParameter("cPro");
                        //Contrato
                        try
                        {
                            String[] datos = numExpediente.split(ConstantesMeLanbide38.BARRA_SEPARADORA);
                            String ejercicio = datos[0];
                            contrato.setAp1(ap1 != null && !ap1.equals("") ? ap1.toUpperCase() : null);
                            contrato.setAp2(ap2 != null && !ap2.equals("") ? ap2.toUpperCase() : null);
                            contrato.setCnoe(cnoe != null && !cnoe.equals("") ? cnoe.toUpperCase() : "");
                            contrato.setCol(colectivo != null && !colectivo.equals("") ? Integer.parseInt(colectivo) : null);
                            contrato.setCss(segSocial != null && !segSocial.equals("") ? new BigDecimal(segSocial.replaceAll(",", "\\.")) : null);
                            contrato.setDco(duracionContrato != null && !duracionContrato.equals("") ? Integer.parseInt(duracionContrato) : null);
                            contrato.setDoc(dni != null && !dni.equals("") ? dni.toUpperCase() : null);
                            contrato.setEje(Integer.parseInt(ejercicio));
                            contrato.setFac(fAlta != null && !fAlta.equals("") ? format.parse(fAlta) : null);
                            contrato.setFfc(null);
                            contrato.setFna(fNac != null && !fNac.equals("") ? format.parse(fNac) : null);
                            contrato.setImp(concedido != null && !concedido.equals("") ? new BigDecimal(concedido.replaceAll(",", "\\.")) : null);
                            contrato.setImr(modifRes != null && !modifRes.equals("") ? new BigDecimal(modifRes.replaceAll(",", "\\.")) : null);
                            contrato.setInm(inmig != null && !inmig.equals("") ? inmig.toUpperCase() : null);
                            contrato.setIre(recurso != null && !recurso.equals("") ? new BigDecimal(recurso.replaceAll(",", "\\.")) : null);
                            contrato.setMde(mesesDesempleo != null && !mesesDesempleo.equals("") ? Integer.parseInt(mesesDesempleo) : null);
                            contrato.setMin(minus != null && !minus.equals("") ? minus.toUpperCase() : null);
                            contrato.setMun(codOrganizacion);
                            contrato.setnCon(numContrato);
                            contrato.setNes(nEstudios != null && !nEstudios.equals("") ? Integer.parseInt(nEstudios) : null);
                            contrato.setNom(nom != null && !nom.equals("") ? nom.toUpperCase() : null);
                            contrato.setNum(numExpediente);
                            contrato.setOtr(otr != null && !otr.equals("") ? otr.toUpperCase() : null);
                            contrato.setPjt(porJornada != null && !porJornada.equalsIgnoreCase("") ? new BigDecimal(porJornada.replaceAll(",", "\\.")) : null);
                            contrato.setPld(pld != null && !pld.equals("") ? pld.toUpperCase() : null);
                            contrato.setPro(ConstantesMeLanbide38.COD_PROCEDIMIENTO);
                            contrato.setRml(rml != null && !rml.equals("") ? rml.toUpperCase() : null);
                            contrato.setSal(salario != null && !salario.equals("") ? new BigDecimal(salario.replaceAll(",", "\\.")) : null);
                            if(sexo != null && !sexo.equals(""))
                            {
                                if(sexo.equalsIgnoreCase(ConstantesMeLanbide38.SEXO_H))
                                {
                                    contrato.setSex(1);
                                }
                                else if(sexo.equalsIgnoreCase(ConstantesMeLanbide38.SEXO_M))
                                {
                                    contrato.setSex(2);
                                }

                            }
                            else
                            {
                                contrato.setSex(null);
                            }
                            contrato.setTic(tipoContrato != null && !tipoContrato.equals("") ? Integer.parseInt(tipoContrato) : null);
                            contrato.setTerCod(idTer != null && !idTer.equals("") ? Long.parseLong(idTer) : null);
                            contrato.setTco(tipConDur != null && !tipConDur.equals("") ? Integer.parseInt(tipConDur) : null);
                            contrato.setTjo(tipoJornada != null && !tipoJornada.equals("") ? tipoJornada.toUpperCase() : null);
                            contrato.setcPro(cPro != null && !cPro.equals("") ? Integer.parseInt(cPro) : null);
                            if(ValidadorContratos.getInstance().validarContrato(contrato))
                            {
                                boolean exito = MeLanbide38Manager.getInstance().modificarContrato(contrato, adapt);
                                if(exito)
                                {
                                    codigoOperacion = "0";
                                }
                                else
                                {
                                    codigoOperacion = "1";
                                }
                            }
                            else
                            {
                                codigoOperacion = "3";
                            }
                        }
                        catch(Exception ex)
                        {
                            codigoOperacion = "3";
                        }

                    }
                    else
                    {
                        codigoOperacion = "3";
                    }
                }
                else
                {
                    codigoOperacion = "3";
                }
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
        
        log.debug("Contrato modificado");
        
        List<FilaContratoVO> contratos = new ArrayList<FilaContratoVO>();
        try
        {
            contratos = MeLanbide38Manager.getInstance().getContratosExpediente(numExpediente, adapt);
        }
        catch(BDException bde)
        {
            java.util.logging.Logger.getLogger(MELANBIDE38.class.getName()).log(Level.SEVERE, null, bde);
        }
        catch(Exception ex)
        {
            java.util.logging.Logger.getLogger(MELANBIDE38.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
            xmlSalida.append("<CODIGO_OPERACION>");
                xmlSalida.append(codigoOperacion);
            xmlSalida.append("</CODIGO_OPERACION>");
            for(FilaContratoVO fila : contratos)
            {
                xmlSalida.append("<FILA>");
                    xmlSalida.append("<ID>");
                        xmlSalida.append(fila.getNumContrato());
                    xmlSalida.append("</ID>");
                    xmlSalida.append("<DNI>");
                        xmlSalida.append(fila.getDni() != null ? fila.getDni().toUpperCase() : "");
                    xmlSalida.append("</DNI>");
                    xmlSalida.append("<NOMBREAPELLIDOS>");
                        xmlSalida.append(fila.getNombreApellidos());
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
    
    public void eliminarContrato(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        if(log.isDebugEnabled())
            log.debug("Eliminando contrato");
        String codigoOperacion = "";
        AdaptadorSQLBD adapt = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (SQLException ex) {
            codigoOperacion = "1";
        }
        if(request.getParameter("idCon") != null && numExpediente != null && !numExpediente.equals(""))
        {
            try
            {
                Integer numContrato = Integer.parseInt(request.getParameter("idCon"));
                boolean exito = MeLanbide38Manager.getInstance().eliminarContrato(numExpediente, numContrato, adapt);
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
        
        List<FilaContratoVO> contratos = new ArrayList<FilaContratoVO>();
        try
        {
            contratos = MeLanbide38Manager.getInstance().getContratosExpediente(numExpediente, adapt);
        }
        catch(BDException bde)
        {
            java.util.logging.Logger.getLogger(MELANBIDE38.class.getName()).log(Level.SEVERE, null, bde);
        }
        catch(Exception ex)
        {
            java.util.logging.Logger.getLogger(MELANBIDE38.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
            xmlSalida.append("<CODIGO_OPERACION>");
                xmlSalida.append(codigoOperacion);
            xmlSalida.append("</CODIGO_OPERACION>");
            for(FilaContratoVO fila : contratos)
            {
                xmlSalida.append("<FILA>");
                    xmlSalida.append("<ID>");
                        xmlSalida.append(fila.getNumContrato());
                    xmlSalida.append("</ID>");
                    xmlSalida.append("<DNI>");
                        xmlSalida.append(fila.getDni() != null ? fila.getDni().toUpperCase() : "");
                    xmlSalida.append("</DNI>");
                    xmlSalida.append("<NOMBREAPELLIDOS>");
                        xmlSalida.append(fila.getNombreApellidos());
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
        ArrayList<ValorCampoDesplegableModuloIntegracionVO> listaTipoContrato = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
        ArrayList<ValorCampoDesplegableModuloIntegracionVO> listaCatProf = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
        
        //Combo ESTUDIOS
        try
        {
            String campoDesplegable = ConfigurationParameter.getParameter(String.valueOf(codOrganizacion) + ConstantesMeLanbide38.BARRA_SEPARADORA
                    + ConstantesMeLanbide38.CAMPO_SUPLEMENTARIO_ESTUDIOS, ConstantesMeLanbide38.FICHERO_PROPIEDADES);
        
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
            String campoDesplegable = ConfigurationParameter.getParameter(String.valueOf(codOrganizacion) + ConstantesMeLanbide38.BARRA_SEPARADORA
                    + ConstantesMeLanbide38.CAMPO_SUPLEMENTARIO_COLECTIVOS, ConstantesMeLanbide38.FICHERO_PROPIEDADES);
        
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
            String campoDesplegable = ConfigurationParameter.getParameter(String.valueOf(codOrganizacion) + ConstantesMeLanbide38.BARRA_SEPARADORA
                    + ConstantesMeLanbide38.CAMPO_SUPLEMENTARIO_TIP_CON_DUR, ConstantesMeLanbide38.FICHERO_PROPIEDADES);
        
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
            String campoDesplegable = ConfigurationParameter.getParameter(String.valueOf(codOrganizacion) + ConstantesMeLanbide38.BARRA_SEPARADORA
                    + ConstantesMeLanbide38.CAMPO_SUPLEMENTARIO_TIPO_JORNADA, ConstantesMeLanbide38.FICHERO_PROPIEDADES);
        
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
            String campoDesplegable = ConfigurationParameter.getParameter(String.valueOf(codOrganizacion) + ConstantesMeLanbide38.BARRA_SEPARADORA
                    + ConstantesMeLanbide38.CAMPO_SUPLEMENTARIO_CNOE, ConstantesMeLanbide38.FICHERO_PROPIEDADES);
        
            SalidaIntegracionVO salidaIntegracion = el.getCampoDesplegable(String.valueOf(codOrganizacion), campoDesplegable);
            listaCNOE = salidaIntegracion.getCampoDesplegable().getValores();
            
            log.debug("CAMPO DESPLEGABLE = "+campoDesplegable);
            log.debug("CNOE ENCOTRADOS: "+listaCNOE.size());
        }
        catch(Exception ex)
        {
            
        }
        request.setAttribute("listaCNOE", listaCNOE);
        
        //Combo Tipo Contrato
        try
        {
            String campoDesplegable = ConfigurationParameter.getParameter(String.valueOf(codOrganizacion) + ConstantesMeLanbide38.BARRA_SEPARADORA
                    + ConstantesMeLanbide38.CAMPO_SUPLEMENTARIO_TIPO_CONTRATO, ConstantesMeLanbide38.FICHERO_PROPIEDADES);
        
            SalidaIntegracionVO salidaIntegracion = el.getCampoDesplegable(String.valueOf(codOrganizacion), campoDesplegable);
            listaTipoContrato = salidaIntegracion.getCampoDesplegable().getValores();
            
            log.debug("CAMPO DESPLEGABLE = "+campoDesplegable);
            log.debug("TIPO CONTRATO ENCOTRADOS: "+listaTipoContrato.size());
        }
        catch(Exception ex)
        {
            
        }
        request.setAttribute("listaTipoContrato", listaTipoContrato);
        
        //Combo Categoria Profesional
        try
        {
            String campoDesplegable = ConfigurationParameter.getParameter(String.valueOf(codOrganizacion) + ConstantesMeLanbide38.BARRA_SEPARADORA
                    + ConstantesMeLanbide38.CAMPO_SUPLEMENTARIO_CATEGORIA_PROFESIONAL, ConstantesMeLanbide38.FICHERO_PROPIEDADES);
        
            SalidaIntegracionVO salidaIntegracion = el.getCampoDesplegable(String.valueOf(codOrganizacion), campoDesplegable);
            listaCatProf = salidaIntegracion.getCampoDesplegable().getValores();
            
            log.debug("CAMPO DESPLEGABLE = "+campoDesplegable);
            log.debug("TIPO CONTRATO ENCOTRADOS: "+listaCatProf.size());
        }
        catch(Exception ex)
        {
            
        }
        request.setAttribute("listaCatProf", listaCatProf);
    }
    
    /**
     * Operación que recupera los datos de conexión a la BBDD
     * @param codOrganizacion
     * @return AdaptadorSQLBD
     */
    private AdaptadorSQLBD getAdaptSQLBD(String codOrganizacion) throws SQLException{
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
                if(st!=null) st.close();
                if(rs!=null) rs.close();
                if(conGenerico!=null && !conGenerico.isClosed())
                conGenerico.close();
            }// finally
            if(log.isDebugEnabled()) log.debug("getConnection() : END");
         }// synchronized
        return adapt;
     }//getConnection
}