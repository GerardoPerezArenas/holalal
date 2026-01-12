/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide51;


import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.melanbide51.vo.ControlAccesoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide51.manager.MeLanbide51Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide51.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide51.util.ConstantesMeLanbide51;
import es.altia.flexia.integracion.moduloexterno.melanbide51.vo.CriteriosBusquedaAccesoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide51.vo.DesplegableAdmonLocalVO;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;
import es.altia.technical.PortableContext;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.smartcardio.*;
import javax.sql.DataSource;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author davidg
 */
public class MELANBIDE51 extends ModuloIntegracionExterno {
    
    private static Logger log = LogManager.getLogger(MELANBIDE51.class);   
    
    public String cargarPantallaPrincipal(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.debug("Entramos en cargarPantallaPrincipal de " + this.getClass().getName());
        AdaptadorSQLBD adapt = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (Exception ex) {
            log.error(this.getClass().getName() + " Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }
        String url = "/jsp/extension/melanbide51/melanbide51.jsp";
        if (adapt != null) {
            try {
                List<ControlAccesoVO> listaAccesos = MeLanbide51Manager.getInstance().getDatosControlAcceso(codOrganizacion, adapt);
                if (listaAccesos.size() > 0) {
                    request.setAttribute("listaAccesos", listaAccesos);
                }
                //Cargamos el el request los valores  de los desplegables
                List<DesplegableAdmonLocalVO> listaMotivoVisita_busq = MeLanbide51Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide51.COD_DESP_MOTIVO_VISITA, ConstantesMeLanbide51.FICHERO_PROPIEDADES), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (listaMotivoVisita_busq.size() > 0) {
                    request.setAttribute("listaMotivoVisita_busq", listaMotivoVisita_busq);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                log.error("Error al recueperar los datos De Acceso - MELANBIDE51 - cargarPantallaPrincipal", ex);
            }
        }
        return url;
    }
    
    public String cargarNuevoAcceso(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response){
        String nuevo = "1";
        String urlnuevoAcceso = "/jsp/extension/melanbide51/nuevoAcceso.jsp?codOrganizacion=" + codOrganizacion;
        try {
            if(request.getAttribute("nuevo")!=null){
                if(request.getAttribute("nuevo").toString().equals("0"))
                    request.setAttribute("nuevo", nuevo);
            }else{
                request.setAttribute("nuevo", nuevo);
            }
            //Cargamos el el request los valores  de los desplegables
            List<DesplegableAdmonLocalVO> listaMotivoVisita = MeLanbide51Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide51.COD_DESP_MOTIVO_VISITA,ConstantesMeLanbide51.FICHERO_PROPIEDADES),this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            if (listaMotivoVisita.size() > 0) {
                request.setAttribute("listaMotivoVisita", listaMotivoVisita);
            }
            
        } catch (Exception ex) {
            log.debug("Se ha presentado un error al intentar preparar la jsp de un Nuevo Acceso : " + ex.getMessage());
        }
        return urlnuevoAcceso;
    }
    
    public String cargarModificarAcceso(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String nuevo = "0";
        String urlnuevoAcceso = "/jsp/extension/melanbide51/nuevoAcceso.jsp?codOrganizacion=" + codOrganizacion;
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
                ControlAccesoVO datModif = MeLanbide51Manager.getInstance().getControlAccesoPorID(id, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (datModif != null) {
                    request.setAttribute("datModif", datModif);
                }
            }
            //Cargamos el el request los valores  de los desplegables
            List<DesplegableAdmonLocalVO> listaMotivoVisita = MeLanbide51Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide51.COD_DESP_MOTIVO_VISITA,ConstantesMeLanbide51.FICHERO_PROPIEDADES),this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            if (listaMotivoVisita.size() > 0) {
                request.setAttribute("listaMotivoVisita", listaMotivoVisita);
            }
        } catch (Exception ex) {
            log.debug("Error al tratar de preparar los datos para modificar y llamar la jsp de modificacion : " + ex.getMessage());
        }
        return urlnuevoAcceso;

    }
    
    public void eliminarAcceso(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<ControlAccesoVO> lista = new ArrayList<ControlAccesoVO>();
        try {
            String id = (String) request.getParameter("id");
            if (id == null || id.equals("")) {
                log.debug("No se ha recibido desde la JSP el id del Control de Acceso a Elimnar ");
                codigoOperacion = "3";
            } else {
                    AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                    int result = MeLanbide51Manager.getInstance().eliminarAcceso(id,adapt);
                    if (result <= 0) {
                        codigoOperacion = "1";
                    } else {
                        codigoOperacion = "0";
                        try {
                            lista = MeLanbide51Manager.getInstance().getDatosControlAcceso(codOrganizacion, adapt);
                        } catch (Exception ex) {
                            log.debug("Error al recuperar la lista de acceso despues de eliminar un registro de Control");
                        }
                    }
            }
        } catch (Exception ex) {
            log.debug("Error eliminando un registro de Control : " + ex);
            codigoOperacion = "2";
        }
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        for (ControlAccesoVO fila : lista) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append(fila.getId() != null ? fila.getId().toString() : "");
            xmlSalida.append("</ID>");
            xmlSalida.append("<M51CA_FECHA>");
            xmlSalida.append(fila.getFecha());
            xmlSalida.append("</M51CA_FECHA>");
            xmlSalida.append("<M51CA_NUMTAR>");
            xmlSalida.append(fila.getNo_tarjeta());
            xmlSalida.append("</M51CA_NUMTAR>");
            xmlSalida.append("<M51CA_DNI_CIF>");
            xmlSalida.append(fila.getNif_Dni());
            xmlSalida.append("</M51CA_DNI_CIF>");
            xmlSalida.append("<M51CA_NOMBRE>");
            xmlSalida.append(fila.getNombre());
            xmlSalida.append("</M51CA_NOMBRE>");
            xmlSalida.append("<M51CA_APE1>");
            xmlSalida.append(fila.getApellido1());
            xmlSalida.append("</M51CA_APE1>");
            xmlSalida.append("<M51CA_APE2>");
            xmlSalida.append(fila.getApellido2());
            xmlSalida.append("</M51CA_APE2>");
            xmlSalida.append("<M51CA_TEL>");
            xmlSalida.append(fila.getTelefono());
            xmlSalida.append("</M51CA_TEL>");
            xmlSalida.append("<M51CA_EMPENT>");
            xmlSalida.append(fila.getEmpresa_entidad());
            xmlSalida.append("</M51CA_EMPENT>");
            xmlSalida.append("<M51CA_SERVVIS>");
            xmlSalida.append(fila.getServicio_visitado());
            xmlSalida.append("</M51CA_SERVVIS>");
            xmlSalida.append("<M51CA_PERSCONT>");
            xmlSalida.append(fila.getPersona_contacto());
            xmlSalida.append("</M51CA_PERSCONT>");
            xmlSalida.append("<M51CA_MOTVIS>");
            xmlSalida.append(fila.getDes_mot_visita());
            xmlSalida.append("</M51CA_MOTVIS>");
            xmlSalida.append("<M51CA_FECHAIV>");
            xmlSalida.append(fila.getFechaIV());
            xmlSalida.append("</M51CA_FECHAIV>");
            xmlSalida.append("<M51CA_FECHAFV>");
            xmlSalida.append(fila.getFechaFV());
            xmlSalida.append("</M51CA_FECHAFV>");
            xmlSalida.append("<M51CA_HORENT>");
            xmlSalida.append(fila.getHora_entrada());
            xmlSalida.append("</M51CA_HORENT>");
            xmlSalida.append("<M51CA_HORSAL>");
            xmlSalida.append(fila.getHora_salida());
            xmlSalida.append("</M51CA_HORSAL>");
            xmlSalida.append("<M51CA_OBSER>");
            xmlSalida.append(fila.getObservaciones());
            xmlSalida.append("</M51CA_OBSER>");
            xmlSalida.append("<DESC_MOTVIS>");
            xmlSalida.append(fila.getDes_mot_visita());
            xmlSalida.append("</DESC_MOTVIS>");
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
    
    public void crearNuevoAcceso(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        String codigoOperacion = "0";
        List<ControlAccesoVO> lista = new ArrayList<ControlAccesoVO>();
        ControlAccesoVO nuevoAcceso = new ControlAccesoVO();
        AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        try
        {
            //Recojo los parametros
            String fecha = (String)request.getParameter("fecha");
            String notarjeta = (String)request.getParameter("notarjeta");
            String dni_nif = (String)request.getParameter("dni_nif");
            String nombre = (String)request.getParameter("nombre");
            String apellido1 = (String)request.getParameter("apellido1");
            String apellido2 = (String)request.getParameter("apellido2");
            String telefono = (String)request.getParameter("telefono");
            String empresa_entidad = (String)request.getParameter("empresa_entidad");
            String serv_visitado = (String)request.getParameter("serv_visitado");
            String pers_contacto = (String)request.getParameter("pers_contacto");
            String motiv_visita = (String)request.getParameter("motiv_visita");
            String fechaiv = (String)request.getParameter("fechaIV");
            String fechafv = (String)request.getParameter("fechaFV");
            String hora_entrada = (String)request.getParameter("hora_entrada");
            String hora_salida = (String)request.getParameter("hora_salida");
            String observaciones = (String)request.getParameter("observaciones");
            
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            //SimpleDateFormat formatoFecha1 = new SimpleDateFormat("dd/MM/yyyy HH24:mm:ss.SSS");
            nuevoAcceso.setFecha(new java.sql.Date(formatoFecha.parse(fecha).getTime()));
            if(fechaiv != null && !fechaiv.equals("")){
                nuevoAcceso.setFechaIV(new java.sql.Date(formatoFecha.parse(fechaiv).getTime()));
            }else
                nuevoAcceso.setFechaIV(null);
            if(fechafv != null && !fechafv.equals("")){
                nuevoAcceso.setFechaFV(new java.sql.Date(formatoFecha.parse(fechafv).getTime()));
            }else
                nuevoAcceso.setFechaFV(null);
            
            nuevoAcceso.setNo_tarjeta(notarjeta);
            nuevoAcceso.setNif_Dni(dni_nif);
            nuevoAcceso.setNombre(nombre);
            nuevoAcceso.setApellido1(apellido1);
            nuevoAcceso.setApellido2(apellido2);
            nuevoAcceso.setTelefono(telefono);
            nuevoAcceso.setEmpresa_entidad(empresa_entidad);
            nuevoAcceso.setServicio_visitado(serv_visitado);
            nuevoAcceso.setPersona_contacto(pers_contacto);
            nuevoAcceso.setCod_mot_visita(motiv_visita);
            if(hora_entrada != null && !hora_entrada.equals("")){
                //nuevoAcceso.setHora_entrada(new java.sql.Time(formatoFecha1.parse(hora_entrada).getTime()));
                nuevoAcceso.setHora_entrada(Time.valueOf(hora_entrada));
            }else
                nuevoAcceso.setHora_entrada(null);
            if(hora_salida!=null && !hora_salida.equals("")){
                //nuevoAcceso.setHora_salida(new java.sql.Time(formatoFecha1.parse(fecha + " " + hora_salida).getTime()));
                nuevoAcceso.setHora_salida(Time.valueOf(hora_salida));
            }else
                nuevoAcceso.setHora_salida(null);

            nuevoAcceso.setObservaciones(observaciones);
            MeLanbide51Manager meLanbide51Manager = MeLanbide51Manager.getInstance();
            boolean insertOK = meLanbide51Manager.crearNuevoAcceso(nuevoAcceso, adapt);
            if(insertOK){
                log.debug("Registro de Acceso Insertado Correctamente");
                lista=meLanbide51Manager.getDatosControlAcceso(codOrganizacion, adapt);
            }else{
                log.debug("NO se ha insertado correctamente el nuevo registro de acceso");
                codigoOperacion = "1";
            }
        }
        catch(Exception ex)
        {
            log.debug("Error al parsear los parametros recibidos del jsp al objeto ControlAccesoVO" + ex.getMessage());
            codigoOperacion = "2";
        }
        
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
            xmlSalida.append("<CODIGO_OPERACION>");
                xmlSalida.append(codigoOperacion);
            xmlSalida.append("</CODIGO_OPERACION>");
            for(ControlAccesoVO fila : lista)
            {
                xmlSalida.append("<FILA>");
                xmlSalida.append("<ID>");
                xmlSalida.append(fila.getId() != null ? fila.getId().toString() : "");
                xmlSalida.append("</ID>");
                xmlSalida.append("<M51CA_FECHA>");
                xmlSalida.append(fila.getFecha());
                xmlSalida.append("</M51CA_FECHA>");
                xmlSalida.append("<M51CA_NUMTAR>");
                xmlSalida.append(fila.getNo_tarjeta());
                xmlSalida.append("</M51CA_NUMTAR>");
                xmlSalida.append("<M51CA_DNI_CIF>");
                xmlSalida.append(fila.getNif_Dni());
                xmlSalida.append("</M51CA_DNI_CIF>");
                xmlSalida.append("<M51CA_NOMBRE>");
                xmlSalida.append(fila.getNombre());
                xmlSalida.append("</M51CA_NOMBRE>");
                xmlSalida.append("<M51CA_APE1>");
                xmlSalida.append(fila.getApellido1());
                xmlSalida.append("</M51CA_APE1>");
                xmlSalida.append("<M51CA_APE2>");
                xmlSalida.append(fila.getApellido2());
                xmlSalida.append("</M51CA_APE2>");
                xmlSalida.append("<M51CA_TEL>");
                xmlSalida.append(fila.getTelefono());
                xmlSalida.append("</M51CA_TEL>");
                xmlSalida.append("<M51CA_EMPENT>");
                xmlSalida.append(fila.getEmpresa_entidad());
                xmlSalida.append("</M51CA_EMPENT>");
                xmlSalida.append("<M51CA_SERVVIS>");
                xmlSalida.append(fila.getServicio_visitado());
                xmlSalida.append("</M51CA_SERVVIS>");
                xmlSalida.append("<M51CA_PERSCONT>");
                xmlSalida.append(fila.getPersona_contacto());
                xmlSalida.append("</M51CA_PERSCONT>");
                xmlSalida.append("<M51CA_MOTVIS>");
                xmlSalida.append(fila.getDes_mot_visita());
                xmlSalida.append("</M51CA_MOTVIS>");
                xmlSalida.append("<M51CA_FECHAIV>");
                xmlSalida.append(fila.getFechaIV()) ;     
                xmlSalida.append("</M51CA_FECHAIV>");
                xmlSalida.append("<M51CA_FECHAFV>");
                xmlSalida.append(fila.getFechaFV()) ;     
                xmlSalida.append("</M51CA_FECHAFV>");
                xmlSalida.append("<M51CA_HORENT>");
                xmlSalida.append(fila.getHora_entrada());
                xmlSalida.append("</M51CA_HORENT>");
                xmlSalida.append("<M51CA_HORSAL>");
                xmlSalida.append(fila.getHora_salida());
                xmlSalida.append("</M51CA_HORSAL>");
                xmlSalida.append("<M51CA_OBSER>");
                xmlSalida.append(fila.getObservaciones());
                xmlSalida.append("</M51CA_OBSER>");
                xmlSalida.append("<DESC_MOTVIS>");
                xmlSalida.append(fila.getDes_mot_visita());
                xmlSalida.append("</DESC_MOTVIS>");
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
    
    public void modificarAcceso(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<ControlAccesoVO> lista = new ArrayList<ControlAccesoVO>();
        AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        try {
            //Recojo los parametros
            String id = (String) request.getParameter("id");
            String fecha = (String) request.getParameter("fecha");
            String notarjeta = (String) request.getParameter("notarjeta");
            String dni_nif = (String) request.getParameter("dni_nif");
            String nombre = (String) request.getParameter("nombre");
            String apellido1 = (String) request.getParameter("apellido1");
            String apellido2 = (String) request.getParameter("apellido2");
            String telefono = (String) request.getParameter("telefono");
            String empresa_entidad = (String) request.getParameter("empresa_entidad");
            String serv_visitado = (String) request.getParameter("serv_visitado");
            String pers_contacto = (String) request.getParameter("pers_contacto");
            String motiv_visita = (String) request.getParameter("motiv_visita");
            String fechaiv =(String) request.getParameter("fechaIV");
            String fechafv =(String) request.getParameter("fechaFV");
            String hora_entrada = (String) request.getParameter("hora_entrada");
            String hora_salida = (String) request.getParameter("hora_salida");
            String observaciones = (String) request.getParameter("observaciones");
            
            if(id==null && id.equals("")){
                log.debug("No se ha recibido desde la JSP el id del Control de Acceso a Modificar ");
                codigoOperacion = "3";
            }else{
                ControlAccesoVO datModif = MeLanbide51Manager.getInstance().getControlAccesoPorID(id, adapt);
                datModif.setId(Integer.parseInt(id));
                
                SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
                //SimpleDateFormat formatoFecha1 = new SimpleDateFormat("dd/MM/yyyy HH24:mm:ss.SSS");
                datModif.setFecha(new java.sql.Date(formatoFecha.parse(fecha).getTime()));
                
               // datModif.setFechaIV(new java.sql.Date(formatoFecha.parse(fechaiv).getTime()));
            
                //datModif.setFechaFV(new java.sql.Date(formatoFecha.parse(fechafv).getTime()));
                
                datModif.setNo_tarjeta(notarjeta);
                datModif.setNif_Dni(dni_nif);
                datModif.setNombre(nombre);
                datModif.setApellido1(apellido1);
                datModif.setApellido2(apellido2);
                datModif.setTelefono(telefono);
                datModif.setEmpresa_entidad(empresa_entidad);
                datModif.setServicio_visitado(serv_visitado);
                datModif.setPersona_contacto(pers_contacto);
                datModif.setCod_mot_visita(motiv_visita);
                 if (fechaiv != null && !fechaiv.equals(""))
                     datModif.setFechaIV(new java.sql.Date(formatoFecha.parse(fechaiv).getTime()));
                else
                     datModif.setFechaIV(null);
                 if (fechafv != null && !fechafv.equals(""))
                     datModif.setFechaFV(new java.sql.Date(formatoFecha.parse(fechafv).getTime()));
                 else
                     datModif.setFechaFV(null);
                if (hora_entrada != null && !hora_entrada.equals("")) {
                    //nuevoAcceso.setHora_entrada(new java.sql.Time(formatoFecha1.parse(hora_entrada).getTime()));
                    datModif.setHora_entrada(Time.valueOf(hora_entrada));
                } else {
                    datModif.setHora_entrada(null);
                }
                if (hora_salida != null && !hora_salida.equals("")) {
                    //nuevoAcceso.setHora_salida(new java.sql.Time(formatoFecha1.parse(fecha + " " + hora_salida).getTime()));
                    datModif.setHora_salida(Time.valueOf(hora_salida));
                } else {
                    datModif.setHora_salida(null);
                }
                datModif.setObservaciones(observaciones);
                
                MeLanbide51Manager meLanbide51Manager = MeLanbide51Manager.getInstance();
                boolean modOK = meLanbide51Manager.modificarAcceso(datModif, adapt);
                if (modOK) {
                    try {
                        lista = meLanbide51Manager.getDatosControlAcceso(codOrganizacion, adapt);
                    } catch (BDException bde) {
                        codigoOperacion = "1";
                        log.debug("Error de tipo BD al recuperar la lista de Accesos despues de Modificar un registro : " + bde.getMensaje());
                    } catch (Exception ex) {
                        codigoOperacion = "2";
                        log.debug("Error al recuperar la lista de Accesos despues de Modificar un registro : " + ex.getMessage());
                    }
                } else {
                    codigoOperacion = "2";
                }
            }

            
        } catch (Exception ex) {
            log.debug("Error modificar --- ", ex);
            codigoOperacion = "2";
        }

        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        for (ControlAccesoVO fila : lista) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append(fila.getId() != null ? fila.getId().toString() : "");
            xmlSalida.append("</ID>");
            xmlSalida.append("<M51CA_FECHA>");
            xmlSalida.append(fila.getFecha());
            xmlSalida.append("</M51CA_FECHA>");
            xmlSalida.append("<M51CA_NUMTAR>");
            xmlSalida.append(fila.getNo_tarjeta());
            xmlSalida.append("</M51CA_NUMTAR>");
            xmlSalida.append("<M51CA_DNI_CIF>");
            xmlSalida.append(fila.getNif_Dni());
            xmlSalida.append("</M51CA_DNI_CIF>");
            xmlSalida.append("<M51CA_NOMBRE>");
            xmlSalida.append(fila.getNombre());
            xmlSalida.append("</M51CA_NOMBRE>");
            xmlSalida.append("<M51CA_APE1>");
            xmlSalida.append(fila.getApellido1());
            xmlSalida.append("</M51CA_APE1>");
            xmlSalida.append("<M51CA_APE2>");
            xmlSalida.append(fila.getApellido2());
            xmlSalida.append("</M51CA_APE2>");
            xmlSalida.append("<M51CA_TEL>");
            xmlSalida.append(fila.getTelefono());
            xmlSalida.append("</M51CA_TEL>");
            xmlSalida.append("<M51CA_EMPENT>");
            xmlSalida.append(fila.getEmpresa_entidad());
            xmlSalida.append("</M51CA_EMPENT>");
            xmlSalida.append("<M51CA_SERVVIS>");
            xmlSalida.append(fila.getServicio_visitado());
            xmlSalida.append("</M51CA_SERVVIS>");
            xmlSalida.append("<M51CA_PERSCONT>");
            xmlSalida.append(fila.getPersona_contacto());
            xmlSalida.append("</M51CA_PERSCONT>");
            xmlSalida.append("<M51CA_MOTVIS>");
            xmlSalida.append(fila.getDes_mot_visita());
            xmlSalida.append("</M51CA_MOTVIS>");
            xmlSalida.append("<M51CA_FECHAIV>");
            xmlSalida.append(fila.getFechaIV());
            xmlSalida.append("</M51CA_FECHAIV>");
            xmlSalida.append("<M51CA_FECHAFV>");
            xmlSalida.append(fila.getFechaFV());
            xmlSalida.append("</M51CA_FECHAFV>");
            xmlSalida.append("<M51CA_HORENT>");
            xmlSalida.append(fila.getHora_entrada());
            xmlSalida.append("</M51CA_HORENT>");
            xmlSalida.append("<M51CA_HORSAL>");
            xmlSalida.append(fila.getHora_salida());
            xmlSalida.append("</M51CA_HORSAL>");
            xmlSalida.append("<M51CA_OBSER>");
            xmlSalida.append(fila.getObservaciones());
            xmlSalida.append("</M51CA_OBSER>");
            xmlSalida.append("<DESC_MOTVIS>");
            xmlSalida.append(fila.getDes_mot_visita());
            xmlSalida.append("</DESC_MOTVIS>");
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
    
    public void busquedaFiltrandoListaAcceso(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<ControlAccesoVO> lista = new ArrayList<ControlAccesoVO>();
        AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        try {
            //Recojo los parametros
            String dni_nif_busq = (String) request.getParameter("dni_nif_busq");
            String nombre_busq = (String) request.getParameter("nombre_busq");
            String apellido1_busq = (String) request.getParameter("apellido1_busq");
            String apellido2_busq = (String) request.getParameter("apellido2_busq");
            String meLanbide51Fecha_busqE = (String) request.getParameter("meLanbide51Fecha_busqE");
            String hora_entrada_busq = (String) request.getParameter("hora_entrada_busq");
            String hora_entrada_busqF = (String) request.getParameter("hora_entrada_busqF");
            String meLanbide51Fecha_busqS = (String) request.getParameter("meLanbide51Fecha_busqS");
            String hora_salida_busq = (String) request.getParameter("hora_salida_busq");
            String hora_salida_busqF = (String) request.getParameter("hora_salida_busqF");
            String estado = (String) request.getParameter("estado");
            String empresa_entidad_busq = (String) request.getParameter("empresa_entidad_busq");
            String codListaMotivoVisita_busq = (String) request.getParameter("codListaMotivoVisita_busq");
            
            CriteriosBusquedaAccesoVO _criteriosBusquedaAccesoVO = new CriteriosBusquedaAccesoVO();
            
            _criteriosBusquedaAccesoVO.setDni_nif_busq(dni_nif_busq);
            _criteriosBusquedaAccesoVO.setNombre_busq(nombre_busq);
            _criteriosBusquedaAccesoVO.setApellido1_busq(apellido1_busq);
            _criteriosBusquedaAccesoVO.setApellido2_busq(apellido2_busq);
            _criteriosBusquedaAccesoVO.setMeLanbide51Fecha_busqE(meLanbide51Fecha_busqE);
            _criteriosBusquedaAccesoVO.setHora_entrada_busq(hora_entrada_busq);
            _criteriosBusquedaAccesoVO.setHora_entrada_busqF(hora_entrada_busqF);
            _criteriosBusquedaAccesoVO.setMeLanbide51Fecha_busqS(meLanbide51Fecha_busqS);
            _criteriosBusquedaAccesoVO.setHora_salida_busq(hora_salida_busq);
            _criteriosBusquedaAccesoVO.setHora_salida_busqF(hora_salida_busqF);
            _criteriosBusquedaAccesoVO.setEstado(estado);
            _criteriosBusquedaAccesoVO.setEmpresa_entidad_busq(empresa_entidad_busq);
            _criteriosBusquedaAccesoVO.setCodListaMotivoVisita_busq(codListaMotivoVisita_busq);
            
            lista = MeLanbide51Manager.getInstance().busquedaFiltrandoListaAcceso(_criteriosBusquedaAccesoVO, adapt);

        } catch (Exception ex) {
            log.debug("Error al consultar registro de acceso con filtros : " + ex);
            codigoOperacion = "2";
        }
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        for (ControlAccesoVO fila : lista) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append(fila.getId() != null ? fila.getId().toString() : "");
            xmlSalida.append("</ID>");
            xmlSalida.append("<M51CA_FECHA>");
            xmlSalida.append(fila.getFecha());
            xmlSalida.append("</M51CA_FECHA>");
            xmlSalida.append("<M51CA_NUMTAR>");
            xmlSalida.append(fila.getNo_tarjeta());
            xmlSalida.append("</M51CA_NUMTAR>");
            xmlSalida.append("<M51CA_DNI_CIF>");
            xmlSalida.append(fila.getNif_Dni());
            xmlSalida.append("</M51CA_DNI_CIF>");
            xmlSalida.append("<M51CA_NOMBRE>");
            xmlSalida.append(fila.getNombre());
            xmlSalida.append("</M51CA_NOMBRE>");
            xmlSalida.append("<M51CA_APE1>");
            xmlSalida.append(fila.getApellido1());
            xmlSalida.append("</M51CA_APE1>");
            xmlSalida.append("<M51CA_APE2>");
            xmlSalida.append(fila.getApellido2());
            xmlSalida.append("</M51CA_APE2>");
            xmlSalida.append("<M51CA_TEL>");
            xmlSalida.append(fila.getTelefono());
            xmlSalida.append("</M51CA_TEL>");
            xmlSalida.append("<M51CA_EMPENT>");
            xmlSalida.append(fila.getEmpresa_entidad());
            xmlSalida.append("</M51CA_EMPENT>");
            xmlSalida.append("<M51CA_SERVVIS>");
            xmlSalida.append(fila.getServicio_visitado());
            xmlSalida.append("</M51CA_SERVVIS>");
            xmlSalida.append("<M51CA_PERSCONT>");
            xmlSalida.append(fila.getPersona_contacto());
            xmlSalida.append("</M51CA_PERSCONT>");
            xmlSalida.append("<M51CA_MOTVIS>");
            xmlSalida.append(fila.getDes_mot_visita());
            xmlSalida.append("</M51CA_MOTVIS>");
            xmlSalida.append("<M51CA_FECHAIV>");
            xmlSalida.append(fila.getFechaIV());
            xmlSalida.append("</M51CA_FECHAIV>");
            xmlSalida.append("<M51CA_FECHAFV>");
            xmlSalida.append(fila.getFechaFV());
            xmlSalida.append("</M51CA_FECHAFV>");
            xmlSalida.append("<M51CA_HORENT>");
            xmlSalida.append(fila.getHora_entrada());
            xmlSalida.append("</M51CA_HORENT>");
            xmlSalida.append("<M51CA_HORSAL>");
            xmlSalida.append(fila.getHora_salida());
            xmlSalida.append("</M51CA_HORSAL>");
            xmlSalida.append("<M51CA_OBSER>");
            xmlSalida.append(fila.getObservaciones());
            xmlSalida.append("</M51CA_OBSER>");
            xmlSalida.append("<DESC_MOTVIS>");
            xmlSalida.append(fila.getDes_mot_visita());
            xmlSalida.append("</DESC_MOTVIS>");
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
    
    // Funciones Privadas
    private AdaptadorSQLBD getAdaptSQLBD(String codOrganizacion) {
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
                try {
                    if (st != null) {
                        st.close();
                    }
                    if (rs != null) {
                        rs.close();
                    }
                    if (conGenerico != null && !conGenerico.isClosed()) {
                        conGenerico.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }//try-catch
            }// finally
            if (log.isDebugEnabled()) {
                log.debug("getConnection() : END");
            }
        }// synchronized
        return adapt;
    }//getConnection
    
    public void leerSmartCard(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {

        String codigoOperacion = "0";
        String responseStr=null;
        try {
            //recojo la lista de lectores conectados
            TerminalFactory factory = TerminalFactory.getDefault();
            List<CardTerminal> terminals = factory.terminals().list();
            log.debug("Terminals: " + terminals);

	        // me quedo con el primero (suponiendo que solo hay uno. 
            //Habría que mirar la manera de asegurarse que es el que queremos en caso de que haya más de uno conectado...)
            CardTerminal terminal = terminals.get(0);

            // conecto con el lector
            Card card = terminal.connect("T=0");
            log.debug("card: " + card);
            CardChannel channel = card.getBasicChannel();

            //Envio el comando
            byte ucByteSend[] = new byte[5];
            ucByteSend[0] = (byte) 0xFF;//CLA
            ucByteSend[1] = (byte) 0xCA;//INS
            ucByteSend[2] = (byte) 0x00;//P1
            ucByteSend[3] = (byte) 0x00;//P2
            ucByteSend[4] = (byte) 0x00;//Le

            CommandAPDU command = new CommandAPDU(ucByteSend);
            ResponseAPDU r = channel.transmit(command);
            responseStr = bytesToHex(r.getData());
              
            card.disconnect(false);
        } catch (CardNotPresentException nex)//No detecta tarjeta
        {
            codigoOperacion = "1";
            log.debug("Error: no se encuentra la tarjeta o está dañada : " + nex.getMessage());
        } catch (CardException cex)//No detecta lector
        {
            codigoOperacion = "2";
            log.debug("Error: no se puede establecer la conexión con el lector : " + cex.getMessage());
        }
        
        //Proceso la respuesta
         
            //System.out.println("UID: " +responseStr);
            StringBuffer xmlSalida = new StringBuffer();
            xmlSalida.append("<RESPUESTA>");
            xmlSalida.append("<CODIGO_OPERACION>");
            xmlSalida.append(codigoOperacion);
            xmlSalida.append("</CODIGO_OPERACION>");
            xmlSalida.append("<CODIGO_UID>");
            xmlSalida.append(responseStr);
            xmlSalida.append("</CODIGO_UID>");
            xmlSalida.append("</RESPUESTA>");
            //Desconecto la tarjeta. El boolean que se pasa por parámetro indica si se debe resetear la tarjeta despues de desconectar.
        
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
    
    public void leerDatosVisitaDNI(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        String existeVisita="0";
        String codigoOperacion = "0";
        ControlAccesoVO datosVisita = new ControlAccesoVO();
        AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        //String responseStr=null;
        try {
            
            String dni_nif = (String) request.getParameter("dni_nif");
            
            datosVisita = MeLanbide51Manager.getInstance().getControlAccesoPorDNICIF(dni_nif, adapt);
           
        }  
        catch (Exception ex) {
            log.debug("Error al consultar registro de acceso con filtros : " + ex);
            codigoOperacion = "2";
        }
            if (datosVisita.getId()!=null){
                existeVisita="1";
            }
                
        //Proceso la respuesta
         
            //System.out.println("UID: " +responseStr);
            StringBuffer xmlSalida = new StringBuffer();
            xmlSalida.append("<RESPUESTA>");
            
                xmlSalida.append("<CODIGO_OPERACION>");
                xmlSalida.append(codigoOperacion);
                xmlSalida.append("</CODIGO_OPERACION>");
                xmlSalida.append("<EXISTE_VISITA>");
                xmlSalida.append(existeVisita);
                xmlSalida.append("</EXISTE_VISITA>");
                xmlSalida.append("<M51CA_DNI_CIF>");
                xmlSalida.append(datosVisita.getNif_Dni()!=null?datosVisita.getNif_Dni():"");
                xmlSalida.append("</M51CA_DNI_CIF>");
                xmlSalida.append("<M51CA_NOMBRE>");
                xmlSalida.append(datosVisita.getNombre()!=null?datosVisita.getNombre():"");
                xmlSalida.append("</M51CA_NOMBRE>");
                xmlSalida.append("<M51CA_APE1>");
                xmlSalida.append(datosVisita.getApellido1()!=null?datosVisita.getApellido1():"");
                xmlSalida.append("</M51CA_APE1>");
                xmlSalida.append("<M51CA_APE2>");
                xmlSalida.append(datosVisita.getApellido2()!=null?datosVisita.getApellido2():"");
                xmlSalida.append("</M51CA_APE2>");
                xmlSalida.append("<M51CA_TEL>");
                xmlSalida.append(datosVisita.getTelefono()!=null?datosVisita.getTelefono():"");
                xmlSalida.append("</M51CA_TEL>");
                xmlSalida.append("<M51CA_EMPENT>");
                xmlSalida.append(datosVisita.getEmpresa_entidad()!=null?datosVisita.getEmpresa_entidad():"");
                xmlSalida.append("</M51CA_EMPENT>");
              
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
    
    private String bytesToHex(byte[] bytes) {
		char[] hexArray = "0123456789ABCDEF".toCharArray();
	    
		char[] hexChars = new char[bytes.length * 2];
	    
		for(int j = 0; j < bytes.length; j++)
		{
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}
}
