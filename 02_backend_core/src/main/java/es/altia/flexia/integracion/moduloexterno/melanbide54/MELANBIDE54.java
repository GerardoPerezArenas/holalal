/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.flexia.integracion.moduloexterno.melanbide54;

/**
 *
 * @author davidg
 */

import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.melanbide54.manager.MeLanbide54Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide54.vo.CentroVO;
import es.altia.flexia.integracion.moduloexterno.melanbide54.vo.MunicipioVO;
import es.altia.flexia.integracion.moduloexterno.melanbide54.vo.SelectItem;
import es.altia.flexia.integracion.moduloexterno.melanbide54.vo.TerritorioHVO;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;
import es.altia.technical.PortableContext;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class MELANBIDE54 extends ModuloIntegracionExterno
{
        //Logger
    private static Logger log = LogManager.getLogger(MELANBIDE54.class);   
    ResourceBundle m_Conf = ResourceBundle.getBundle("common");
    
    //Instancia
    private static MELANBIDE54 instance = null;
    
    public static MELANBIDE54 getInstance()
    {
        if(instance == null)
        {
            synchronized(MELANBIDE54.class)
            {
                instance = new MELANBIDE54();
            }
        }
        return instance;
    }
    
    // Alta Expedientes via registro platea --> MELANBIDE 42
    public void cargarExpedienteExtension(int codigoOrganizacion, String numeroExpediente, String xml) throws Exception{
        final Class cls = Class.forName("es.altia.flexia.integracion.moduloexterno.melanbide42.MELANBIDE42");
        final Object me42Class = cls.newInstance();
        final Class[] types = {int.class, String.class, String.class};
        final Method method = cls.getMethod("cargarExpedienteExtension", types);
        method.invoke(me42Class, codigoOrganizacion, numeroExpediente, xml);
    }

    public String cargarPantallaPrincipal(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {   
        List<CentroVO> errores = new ArrayList<CentroVO>();
        AdaptadorSQLBD adapt = null;
        try
        {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            errores = MeLanbide54Manager.getInstance().recogeCentros(numExpediente, adapt);
            
            request.setAttribute("ListErrores", errores);
        }catch(Exception ex)
        {
            log.error("Error en funcion cargarPantallaPrincipal: " + ex);
        }
        return "/jsp/extension/melanbide54/melanbide54.jsp";
    }
    
    public void crearCentro(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        String codigoOperacion = "0";
        List<CentroVO> centros = new ArrayList<CentroVO>();
        CentroVO nuevoCen = new CentroVO();
        AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        try
        {
            //Recojo los parametros
            String codTh = (String)request.getParameter("codTh");
            String desCodTh = (String)request.getParameter("desCodTh");
            String codMun = (String)request.getParameter("codMun");
            String desCodMun = (String)request.getParameter("desCodMun");
            String calle = (String)request.getParameter("calle");
            String portal = (String)request.getParameter("portal");
            String piso = (String)request.getParameter("piso");
            String letra = (String)request.getParameter("letra");
            String cp = (String)request.getParameter("cp");
            String telef = (String)request.getParameter("telef");
            String email = (String)request.getParameter("email");
            String existe = (String)request.getParameter("existe");

            nuevoCen.setCodTH(codTh);
            nuevoCen.setDesCodTH(desCodTh);
            nuevoCen.setDesCodMun(desCodMun);
            nuevoCen.setCodMun(codMun);
            nuevoCen.setCalle(calle);
            nuevoCen.setPortal(portal);
            nuevoCen.setPiso(piso);
            nuevoCen.setLetra(letra);
            nuevoCen.setCp(cp);
            nuevoCen.setTlef(telef);
            nuevoCen.setEmail(email);
            //else
            //    nuevaEsp.setAcreditacion(Integer.parseInt(ConstantesDatos.ZERO)); 
            MeLanbide54Manager meLanbide54Manager = MeLanbide54Manager.getInstance();
            boolean insertOK = meLanbide54Manager.crearNuevCentro(nuevoCen, numExpediente, adapt);
            if(insertOK)
            {
                try
                    {
                        centros = meLanbide54Manager.getInstance().recogeCentros(numExpediente, adapt);
                    }
                    catch(Exception ex)
                    {
                        codigoOperacion = "2";
                        java.util.logging.Logger.getLogger(MELANBIDE54.class.getName()).log(Level.SEVERE, null, ex);
                    }
            }
            else
            {
                log.debug("No se ha podido Insertar el centro para el expediente : " + numExpediente );
            }
            
        }
        catch(Exception ex)
        {
            codigoOperacion = "2";
        }
        
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
            xmlSalida.append("<CODIGO_OPERACION>");
                xmlSalida.append(codigoOperacion);
            xmlSalida.append("</CODIGO_OPERACION>");
            for(CentroVO fila : centros)
            {
                xmlSalida.append("<FILA>");
                    xmlSalida.append("<ID>");
                        xmlSalida.append(fila.getId()!= null ? fila.getId().toString() : "");
                    xmlSalida.append("</ID>");
                    xmlSalida.append("<EXP_NUM>");
                        xmlSalida.append(numExpediente);
                    xmlSalida.append("</EXP_NUM>");
                    xmlSalida.append("<CODTH>");
                        xmlSalida.append(fila.getCodTH());
                    xmlSalida.append("</CODTH>");
                    xmlSalida.append("<DESCODTH>");
                        xmlSalida.append(fila.getDesCodTH());
                    xmlSalida.append("</DESCODTH>");
                    xmlSalida.append("<CODMUN>");
                        xmlSalida.append(fila.getCodMun());
                    xmlSalida.append("</CODMUN>");
                    xmlSalida.append("<DESCODMUN>");
                        xmlSalida.append(fila.getDesCodMun());
                    xmlSalida.append("</DESCODMUN>");
                    xmlSalida.append("<CALLE>");
                        xmlSalida.append(fila.getCalle());
                    xmlSalida.append("</CALLE>");
                    xmlSalida.append("<PORTAL>");
                        xmlSalida.append(fila.getPortal());
                    xmlSalida.append("</PORTAL>");
                    xmlSalida.append("<PISO>");
                        xmlSalida.append(fila.getPiso());
                    xmlSalida.append("</PISO>");
                    xmlSalida.append("<LETRA>");
                        xmlSalida.append(fila.getLetra());
                    xmlSalida.append("</LETRA>");
                    xmlSalida.append("<CP>");
                        xmlSalida.append(fila.getCp());
                    xmlSalida.append("</CP>");
                    xmlSalida.append("<TELEF>");
                        xmlSalida.append(fila.getTlef());
                    xmlSalida.append("</TELEF>");
                    xmlSalida.append("<EMAIL>");
                        xmlSalida.append(fila.getEmail());
                    xmlSalida.append("</EMAIL>");
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
    
    public void modificarCentro(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        String codigoOperacion = "0";
        List<CentroVO> centros = new ArrayList<CentroVO>();
        CentroVO nuevoCen = new CentroVO();
        AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        try
        {
            //Recojo los parametros
            String id = (String)request.getParameter("id");
            String codTh = (String)request.getParameter("codTh");
            String desCodTh = (String)request.getParameter("desCodTh");
            String desCodMun = (String)request.getParameter("desCodMun");
            String codMun = (String)request.getParameter("codMun");
            String calle = (String)request.getParameter("calle");
            String portal = (String)request.getParameter("portal");
            String piso = (String)request.getParameter("piso");
            String letra = (String)request.getParameter("letra");
            String cp = (String)request.getParameter("cp");
            String telef = (String)request.getParameter("telef");
            String email = (String)request.getParameter("email");
            String existe = (String)request.getParameter("existe");
            
            nuevoCen.setId(id);
            nuevoCen.setCodTH(codTh);
            nuevoCen.setDesCodTH(desCodTh);
            nuevoCen.setDesCodMun(desCodMun);
            nuevoCen.setCodMun(codMun);
            nuevoCen.setCalle(calle);
            nuevoCen.setPortal(portal);
            nuevoCen.setPiso(piso);
            nuevoCen.setLetra(letra);
            nuevoCen.setCp(cp);
            nuevoCen.setTlef(telef);
            nuevoCen.setEmail(email);
            //else
            //    nuevaEsp.setAcreditacion(Integer.parseInt(ConstantesDatos.ZERO)); 
            MeLanbide54Manager meLanbide54Manager = MeLanbide54Manager.getInstance();
            boolean insertOK = meLanbide54Manager.modificandoCentro(nuevoCen, numExpediente, adapt);
            if(insertOK)
            {
                try
                    {
                        centros = meLanbide54Manager.getInstance().recogeCentros(numExpediente, adapt);
                    }
                    catch(Exception ex)
                    {
                        codigoOperacion = "2";
                        java.util.logging.Logger.getLogger(MELANBIDE54.class.getName()).log(Level.SEVERE, null, ex);
                    }
            }
            else
            {
                log.debug("No se ha podido modificar el centro para el expediente : " + numExpediente );
            }
            
        }
        catch(Exception ex)
        {
            codigoOperacion = "2";
        }
        
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
            xmlSalida.append("<CODIGO_OPERACION>");
                xmlSalida.append(codigoOperacion);
            xmlSalida.append("</CODIGO_OPERACION>");
            for(CentroVO fila : centros)
            {
                xmlSalida.append("<FILA>");
                    xmlSalida.append("<ID>");
                        xmlSalida.append(fila.getId()!= null ? fila.getId().toString() : "");
                    xmlSalida.append("</ID>");
                    xmlSalida.append("<EXP_NUM>");
                        xmlSalida.append(numExpediente);
                    xmlSalida.append("</EXP_NUM>");
                    xmlSalida.append("<CODTH>");
                        xmlSalida.append(fila.getCodTH());
                    xmlSalida.append("</CODTH>");
                    xmlSalida.append("<DESCODTH>");
                        xmlSalida.append(fila.getDesCodTH());
                    xmlSalida.append("</DESCODTH>");
                    xmlSalida.append("<CODMUN>");
                        xmlSalida.append(fila.getCodMun());
                    xmlSalida.append("</CODMUN>");
                    xmlSalida.append("<DESCODMUN>");
                        xmlSalida.append(fila.getDesCodMun());
                    xmlSalida.append("</DESCODMUN>");
                    xmlSalida.append("<CALLE>");
                        xmlSalida.append(fila.getCalle());
                    xmlSalida.append("</CALLE>");
                    xmlSalida.append("<PORTAL>");
                        xmlSalida.append(fila.getPortal());
                    xmlSalida.append("</PORTAL>");
                    xmlSalida.append("<PISO>");
                        xmlSalida.append(fila.getPiso());
                    xmlSalida.append("</PISO>");
                    xmlSalida.append("<LETRA>");
                        xmlSalida.append(fila.getLetra());
                    xmlSalida.append("</LETRA>");
                    xmlSalida.append("<CP>");
                        xmlSalida.append(fila.getCp());
                    xmlSalida.append("</CP>");
                    xmlSalida.append("<TELEF>");
                        xmlSalida.append(fila.getTlef());
                    xmlSalida.append("</TELEF>");
                    xmlSalida.append("<EMAIL>");
                        xmlSalida.append(fila.getEmail());
                    xmlSalida.append("</EMAIL>");
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
    
    public void eliminarCentro(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        String codigoOperacion = "0";
        List<CentroVO> centros = new ArrayList<CentroVO>();
        CentroVO nuevoCen = new CentroVO();
        AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        try
        {
            //Recojo los parametros
            String id = (String)request.getParameter("id");
            
            nuevoCen.setId(id);
            //else
            //    nuevaEsp.setAcreditacion(Integer.parseInt(ConstantesDatos.ZERO)); 
            MeLanbide54Manager meLanbide54Manager = MeLanbide54Manager.getInstance();
            boolean insertOK = meLanbide54Manager.eliminandoandoCentro(id, numExpediente, adapt);
            if(insertOK)
            {
                try
                    {
                        centros = meLanbide54Manager.getInstance().recogeCentros(numExpediente, adapt);
                    }
                    catch(Exception ex)
                    {
                        codigoOperacion = "2";
                        java.util.logging.Logger.getLogger(MELANBIDE54.class.getName()).log(Level.SEVERE, null, ex);
                    }
            }
            else
            {
                log.debug("No se ha podido eliminar el centro para el expediente : " + numExpediente );
            }
            
        }
        catch(Exception ex)
        {
            codigoOperacion = "2";
        }
        
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
            xmlSalida.append("<CODIGO_OPERACION>");
                xmlSalida.append(codigoOperacion);
            xmlSalida.append("</CODIGO_OPERACION>");
            for(CentroVO fila : centros)
            {
                xmlSalida.append("<FILA>");
                    xmlSalida.append("<ID>");
                        xmlSalida.append(fila.getId()!= null ? fila.getId().toString() : "");
                    xmlSalida.append("</ID>");
                    xmlSalida.append("<EXP_NUM>");
                        xmlSalida.append(numExpediente);
                    xmlSalida.append("</EXP_NUM>");
                    xmlSalida.append("<CODTH>");
                        xmlSalida.append(fila.getCodTH());
                    xmlSalida.append("</CODTH>");
                    xmlSalida.append("<DESCODTH>");
                        xmlSalida.append(fila.getDesCodTH());
                    xmlSalida.append("</DESCODTH>");
                    xmlSalida.append("<CODMUN>");
                        xmlSalida.append(fila.getCodMun());
                    xmlSalida.append("</CODMUN>");
                    xmlSalida.append("<DESCODMUN>");
                        xmlSalida.append(fila.getDesCodMun());
                    xmlSalida.append("</DESCODMUN>");
                    xmlSalida.append("<CALLE>");
                        xmlSalida.append(fila.getCalle());
                    xmlSalida.append("</CALLE>");
                    xmlSalida.append("<PORTAL>");
                        xmlSalida.append(fila.getPortal());
                    xmlSalida.append("</PORTAL>");
                    xmlSalida.append("<PISO>");
                        xmlSalida.append(fila.getPiso());
                    xmlSalida.append("</PISO>");
                    xmlSalida.append("<LETRA>");
                        xmlSalida.append(fila.getLetra());
                    xmlSalida.append("</LETRA>");
                    xmlSalida.append("<CP>");
                        xmlSalida.append(fila.getCp());
                    xmlSalida.append("</CP>");
                    xmlSalida.append("<TELEF>");
                        xmlSalida.append(fila.getTlef());
                    xmlSalida.append("</TELEF>");
                    xmlSalida.append("<EMAIL>");
                        xmlSalida.append(fila.getEmail());
                    xmlSalida.append("</EMAIL>");
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
    
    public void getListaCentros(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        String codigoOperacion = "0";
        try
        {
            //Al guardar una oferta, se inserta este mensaje en sesion como resultado
            //Justo despues de guardar se llama a este metodo para recargar la tabla.
            //Este es el punto donde se borra este mensaje de sesion, para no mantenerlo en memoria innecesariamente.
            codigoOperacion = request.getSession().getAttribute("mensajeImportar") != null ? (String)request.getSession().getAttribute("mensajeImportar") : "0";
            request.getSession().removeAttribute("mensajeImportar");
        }
        catch(Exception ex)
        {

        }
        List<CentroVO> filasResumen = new ArrayList<CentroVO>();
        try
        {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            filasResumen = MeLanbide54Manager.getInstance().recogeCentros(numExpediente, adapt);

        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        
        escribirListaRequest(codigoOperacion, filasResumen, response);
    }
    
    public String cargarNuevoCentro(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            request.setAttribute("nuevo", "1");
            //cargar combos de territorio histórico y municipios
            ArrayList<TerritorioHVO> territorios = new ArrayList<TerritorioHVO>();
            ArrayList<MunicipioVO> municipios = new ArrayList<MunicipioVO>();
            try {
                territorios = cargarTerritoriosHistoricos();
                //municipios = cargarMunicipios(codOrganizacion);
            }
            catch (Exception ex) {
                log.error("Error en funcion cargarNuevoCentro: " + ex);
            } 
            //Guardamos los certificados recuperados en la request
             request.setAttribute("listaTh", territorios);
             //request.setAttribute("listaMun", municipios);
            
        }
        catch(Exception ex)
        {
            
        }
        return "/jsp/extension/melanbide54/nuevoCentro.jsp?codOrganizacionModulo="+codOrganizacion;
    }
    
    public void cargarMunicipios(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        String codProvincia = (String)request.getParameter("codProvincia");
        List<SelectItem> listaAmbitos = new ArrayList<SelectItem>();
        
        try
        {
            listaAmbitos = MeLanbide54Manager.getInstance().recogeMunicipios(codProvincia, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        }
        catch(Exception ex)
        {
            
        }
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
            xmlSalida.append("<CODIGO_OPERACION>");
                xmlSalida.append("0");
            xmlSalida.append("</CODIGO_OPERACION>");
            for(SelectItem si : listaAmbitos)
            {
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
    
    public ArrayList<TerritorioHVO> cargarTerritoriosHistoricos()
    {
        ArrayList<TerritorioHVO> lista = new ArrayList<TerritorioHVO>();
        TerritorioHVO terri = new TerritorioHVO();
        try {
            terri.setId("1");
            terri.setDescripcion("Araba");
            lista.add(terri);
            terri = new TerritorioHVO();
            terri.setId("20");
            terri.setDescripcion("Gipuzkoa");
            lista.add(terri);
            terri = new TerritorioHVO();
            terri.setId("48");
            terri.setDescripcion("Bizkaia");
            lista.add(terri);
            }
        catch (Exception ex) {
            log.error("Error en funcion cargarTerritoriosHistoricos: " + ex);
        } 
        return lista;
    }
    
    /*public ArrayList<MunicipioVO> cargarMunicipios(String th, int codOrganizacion)
    {
        ArrayList<MunicipioVO> lista = new ArrayList<MunicipioVO>();
        try {
            lista = MeLanbide54Manager.getInstance().recogeMunicipios(th, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        }
        catch (Exception ex) {
            log.error("Error en funcion cargarTerritoriosHistoricos: " + ex);
        } 
        return lista;
    }*/
    
    public String cargarModifCentro(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            request.setAttribute("nuevo", "0");
            String id = request.getParameter("id");
            ArrayList<TerritorioHVO> territorios = new ArrayList<TerritorioHVO>();
            ArrayList<MunicipioVO> municipios = new ArrayList<MunicipioVO>();
            try {
                territorios = cargarTerritoriosHistoricos();
                //municipios = cargarMunicipios(codOrganizacion);
                if(id != null && !id.equals(""))
                {
                    CentroVO datModif = MeLanbide54Manager.getInstance().getCentroPorCodigo(numExpediente, id, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    if(datModif != null)
                    {
                        request.setAttribute("datModif", datModif);
                    }
                }
            }
            catch (Exception ex) {
                log.error("Error en funcion cargarNuevoCentro: " + ex);
            } 
            //Guardamos los certificados recuperados en la request
             request.setAttribute("listaTh", territorios);
             //request.setAttribute("listaMun", municipios);
            
        }
        catch(Exception ex)
        {
            
        }
        return "/jsp/extension/melanbide54/nuevoCentro.jsp?codOrganizacionModulo="+codOrganizacion;

    }
    
    private void escribirListaRequest(String codigoOperacion, List<CentroVO> filas, HttpServletResponse response)
    {
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
            xmlSalida.append("<CODIGO_OPERACION>");
                xmlSalida.append(codigoOperacion);
            xmlSalida.append("</CODIGO_OPERACION>");
            
        for(CentroVO fila : filas)
        {
            xmlSalida.append("<FILA>");
                xmlSalida.append("<ID>");
                xmlSalida.append(fila.getId());
                xmlSalida.append("</ID>");
                xmlSalida.append("<CODTH>");
                    xmlSalida.append(fila.getCodTH());
                xmlSalida.append("</CODTH>");
                xmlSalida.append("<DESCODTH>");
                    xmlSalida.append(fila.getDesCodTH());
                xmlSalida.append("</DESCODTH>");
                xmlSalida.append("<CODMUN>");
                    xmlSalida.append(fila.getCodMun());
                xmlSalida.append("</CODMUN>");
                xmlSalida.append("<DESCODMUN>");
                    xmlSalida.append(fila.getDesCodMun());
                xmlSalida.append("</DESCODMUN>");
                xmlSalida.append("<CALLE>");
                xmlSalida.append(fila.getCalle());
                xmlSalida.append("</CALLE>");
                xmlSalida.append("<PORTAL>");
                xmlSalida.append(fila.getPortal());
                xmlSalida.append("</PORTAL>");
                xmlSalida.append("<PISO>");
                xmlSalida.append(fila.getPiso());
                xmlSalida.append("</PISO>");
                xmlSalida.append("<LETRA>");
                xmlSalida.append(fila.getLetra());
                xmlSalida.append("</LETRA>");
                xmlSalida.append("<CP>");
                xmlSalida.append(fila.getCp());
                xmlSalida.append("</CP>");
                xmlSalida.append("<TELEF>");
                xmlSalida.append(fila.getTlef());
                xmlSalida.append("</TELEF>");
                xmlSalida.append("<EMAIL>");
                xmlSalida.append(fila.getEmail());
                xmlSalida.append("</EMAIL>");
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
