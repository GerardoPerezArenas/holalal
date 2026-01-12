package es.altia.flexia.integracion.moduloexterno.melanbide72;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import es.altia.flexia.integracion.moduloexterno.melanbide72.manager.MeLanbide72Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide72.vo.AlarmaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide72.vo.MedidaA1BCVO;
import es.altia.flexia.integracion.moduloexterno.melanbide72.vo.MedidaA2VO;
import es.altia.technical.PortableContext;
import es.altia.util.conexion.BDException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javax.sql.DataSource;

public class MELANBIDE72 extends ModuloIntegracionExterno {

    private static Logger log = LogManager.getLogger(MELANBIDE72.class);
    
    // Alta Expedientes via registro platea --> MELANBIDE 42
    public void cargarExpedienteExtension(int codigoOrganizacion, String numeroExpediente, String xml) throws Exception {
        final Class cls = Class.forName("es.altia.flexia.integracion.moduloexterno.melanbide42.MELANBIDE42");
        final Object me42Class = cls.newInstance();
        final Class[] types = {int.class, String.class, String.class};
        final Method method = cls.getMethod("cargarExpedienteExtension", types);
        method.invoke(me42Class, codigoOrganizacion, numeroExpediente, xml);
    }

    public String cargarPantallaPrincipal(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.debug("Entramos en cargarPantallaPrincipal de " + this.getClass().getName());
        AdaptadorSQLBD adapt = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (Exception ex) {
            log.error(this.getClass().getName() + " Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }
        String url = "/jsp/extension/melanbide72/melanbide72.jsp";
        request.setAttribute("numExp", numExpediente);
        if (adapt != null) {
            
            //medidas alternativas
            try {
                List<MedidaA1BCVO> listaMedidasA1 = MeLanbide72Manager.getInstance().getDatosMedidasA1(numExpediente, codOrganizacion, adapt);
                if (listaMedidasA1.size() > 0) {
                    request.setAttribute("listaMedidasA1", listaMedidasA1);
                }
                List<MedidaA2VO> listaMedidasA2 = MeLanbide72Manager.getInstance().getDatosMedidasA2(numExpediente, codOrganizacion, adapt);
                if (listaMedidasA2.size() > 0) {
                    request.setAttribute("listaMedidasA2", listaMedidasA2);
                }
                List<MedidaA1BCVO> listaMedidasB = MeLanbide72Manager.getInstance().getDatosMedidasB(numExpediente, codOrganizacion, adapt);
                if (listaMedidasB.size() > 0) {
                    request.setAttribute("listaMedidasB", listaMedidasB);
                }
                List<MedidaA1BCVO> listaMedidasC = MeLanbide72Manager.getInstance().getDatosMedidasC(numExpediente, codOrganizacion, adapt);
                if (listaMedidasC.size() > 0) {
                    request.setAttribute("listaMedidasC", listaMedidasC);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                log.error("Error al recuperar las medidas alternativas - MELANBIDE72 - cargarPantallaPrincipal", ex);
            }
            
            //campos suplementarios numéricos necesarios para el formulario (tabla E_TNU, campos TNU_COD, TNU_VALOR para TNU_NUM='numExp')
                //A1NUMTRABSUST __  TRABAJADORES SUSTITUYE CONTRATACION DISCP CENTRO EMPLEO A1 (num_trab_sust_A1)
                //A1PERANIOS __  PERIODO AÑOS CENTRO EMPLEO A1 (periodo_contr_A1)
                
                //A2NUMTRABSUST __  TRABAJADORES SUSTITUYE CONTRATACION DISCP AUTÓNOMA A2 (num_trab_sust_A2)
                //A2PERANIOS __  PERIODO AÑOS AUTÓNOMA A2 (periodo_contr_A2)
                
                //BNUMTRABSUST __  TRABAJADORES SUSTITUYE CONTRATACION DISCP DONACIÓN B (num_trab_sust_B)
                //BPERANIOS __  PERIODO AÑOS DONACIÓN B (periodo_contr_B)
                
                //CNUMTRABSUST __  TRABAJADORES SUSTITUYE CONTRATACION DISCP ENCLAVE C (num_trab_sust_C)
                //CPERANIOS __  PERIODO AÑOS ENCLAVE C (periodo_contr_C)
                //CPERDISCPENCL __  TRABAJADORES DISCP OCUPADOS EN EL ENCLAVE C (pers_disc_ocup_C)

                //CECON CUANTIFICACION ECONOMICA ANUAL (cuant_econ_anual)
                //IMPSIM IMPORTE ANUAL TOTAL (imp_anual_total)
                //TRABSIM CONTRATACION TOTAL (contratacion_total)
                
                /*SELECT TNU_VALOR FROM E_TNU WHERE TNU_NUM='2020/DECEX/000002' AND TNU_COD='A1NUMTRABSUST';

                MERGE INTO E_TNU USING (SELECT 1 FROM DUAL) ON (TNU_NUM = '2020/DECEX/000002' AND TNU_COD = 'A1NUMTRABSUST' AND TNU_MUN = 1) WHEN MATCHED THEN UPDATE SET TNU_VALOR = 100
                WHEN NOT MATCHED THEN INSERT (TNU_MUN, TNU_EJE, TNU_NUM, TNU_COD, TNU_VALOR) VALUES (1, 2020, '2020/DECEX/000002', 'A1NUMTRABSUST', 100);

                DELETE FROM E_TNU WHERE TNU_NUM='2020/DECEX/000002' AND TNU_COD='A1NUMTRABSUST' AND TNU_MUN=1;*/
                
            try {
                String cod_num_trab_sust_A1 = "A1NUMTRABSUST";
                Integer num_trab_sust_A1 = MeLanbide72Manager.getInstance().getCampoSuplementarioNumerico(numExpediente, codOrganizacion, cod_num_trab_sust_A1, adapt).intValue();
                String cod_periodo_contr_A1 = "A1PERANIOS";
                Integer periodo_contr_A1 = MeLanbide72Manager.getInstance().getCampoSuplementarioNumerico(numExpediente, codOrganizacion, cod_periodo_contr_A1, adapt).intValue();
                
                String cod_num_trab_sust_A2 = "A2NUMTRABSUST";
                Integer num_trab_sust_A2 = MeLanbide72Manager.getInstance().getCampoSuplementarioNumerico(numExpediente, codOrganizacion, cod_num_trab_sust_A2, adapt).intValue();
                String cod_periodo_contr_A2 = "A2PERANIOS";
                Integer periodo_contr_A2 = MeLanbide72Manager.getInstance().getCampoSuplementarioNumerico(numExpediente, codOrganizacion, cod_periodo_contr_A2, adapt).intValue();
                
                String cod_num_trab_sust_B = "BNUMTRABSUST";
                Integer num_trab_sust_B = MeLanbide72Manager.getInstance().getCampoSuplementarioNumerico(numExpediente, codOrganizacion, cod_num_trab_sust_B, adapt).intValue();
                String cod_periodo_contr_B = "BPERANIOS";
                Integer periodo_contr_B = MeLanbide72Manager.getInstance().getCampoSuplementarioNumerico(numExpediente, codOrganizacion, cod_periodo_contr_B, adapt).intValue();
                
                String cod_num_trab_sust_C = "CNUMTRABSUST";
                Integer num_trab_sust_C = MeLanbide72Manager.getInstance().getCampoSuplementarioNumerico(numExpediente, codOrganizacion, cod_num_trab_sust_C, adapt).intValue();
                String cod_periodo_contr_C = "CPERANIOS";
                Integer periodo_contr_C = MeLanbide72Manager.getInstance().getCampoSuplementarioNumerico(numExpediente, codOrganizacion, cod_periodo_contr_C, adapt).intValue();
                String cod_pers_disc_ocup_C = "CPERDISCPENCL";
                Integer pers_disc_ocup_C = MeLanbide72Manager.getInstance().getCampoSuplementarioNumerico(numExpediente, codOrganizacion, cod_pers_disc_ocup_C, adapt).intValue();
                
                String cod_cuant_econ_anual = "CECON";
                Double cuant_econ_anual = MeLanbide72Manager.getInstance().getCampoSuplementarioNumerico(numExpediente, codOrganizacion, cod_cuant_econ_anual, adapt);
                String cod_imp_anual_total = "IMPSIM";
                Double imp_anual_total = MeLanbide72Manager.getInstance().getCampoSuplementarioNumerico(numExpediente, codOrganizacion, cod_imp_anual_total, adapt);
                String cod_contratacion_total = "TRABSIM";
                Integer contratacion_total = MeLanbide72Manager.getInstance().getCampoSuplementarioNumerico(numExpediente, codOrganizacion, cod_contratacion_total, adapt).intValue();
                
                request.setAttribute("num_trab_sust_A1", Integer.toString(num_trab_sust_A1));
                request.setAttribute("periodo_contr_A1", Integer.toString(periodo_contr_A1));
                
                request.setAttribute("num_trab_sust_A2", Integer.toString(num_trab_sust_A2));
                request.setAttribute("periodo_contr_A2", Integer.toString(periodo_contr_A2));
                
                request.setAttribute("num_trab_sust_B", Integer.toString(num_trab_sust_B));
                request.setAttribute("periodo_contr_B", Integer.toString(periodo_contr_B));
                
                request.setAttribute("num_trab_sust_C", Integer.toString(num_trab_sust_C));
                request.setAttribute("periodo_contr_C", Integer.toString(periodo_contr_C));
                request.setAttribute("pers_disc_ocup_C", Integer.toString(pers_disc_ocup_C));
                
                request.setAttribute("cuant_econ_anual", Double.toString(cuant_econ_anual));
                request.setAttribute("imp_anual_total", Double.toString(imp_anual_total));
                request.setAttribute("contratacion_total", Integer.toString(contratacion_total));
            } catch (Exception ex) {
                ex.printStackTrace();
                log.error("Error al recuperar campos suplementarios - MELANBIDE72 - cargarPantallaPrincipal", ex);
            }
            
        }
        
        return url;
    }
    
    public String cargarNuevaMedidaA1(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String nuevo = "1";
        String numExp = "";
        String urlnuevaMedida = "/jsp/extension/melanbide72/nuevaMedidaA1.jsp?codOrganizacion=" + codOrganizacion;
        try {
            if (request.getAttribute("nuevo") != null) {
                if (request.getAttribute("nuevo").toString().equals("0")) {
                    request.setAttribute("nuevo", nuevo);
                }
            } else {
                request.setAttribute("nuevo", nuevo);
            }
            if (request.getParameter("numExp") != null) {
                numExp = request.getParameter("numExp").toString();
                request.setAttribute("numExp", numExp);
            }

        } catch (Exception ex) {
            log.debug("Se ha presentado un error al intentar preparar la jsp de una Nueva Medida A1 : " + ex.getMessage());
        }
        return urlnuevaMedida;
    }
    
    public void crearNuevaMedidaA1(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        MedidaA1BCVO nuevaMedidaA1 = new MedidaA1BCVO();
        List<MedidaA1BCVO> lista = new ArrayList<MedidaA1BCVO>();
        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            //parámetros
            String numExp = (String) request.getParameter("expediente");
            String nombre = (String) request.getParameter("nombre");
            String nif = (String) request.getParameter("nif");
            String importeAnual = (String) request.getParameter("importeAnual").replace(",", ".");
            String objetoContrato = (String) request.getParameter("objetoContrato");

            nuevaMedidaA1.setNumExp(numExp);
            nuevaMedidaA1.setNombre(nombre);
            nuevaMedidaA1.setNif(nif);
            if (importeAnual != null && !"".equals(importeAnual)) {
                nuevaMedidaA1.setImporteAnual(Double.parseDouble(importeAnual));
            }
            nuevaMedidaA1.setObjetoContrato(objetoContrato);

            MeLanbide72Manager meLanbide72Manager = MeLanbide72Manager.getInstance();
            boolean insertOK = meLanbide72Manager.crearNuevaMedidaA1(nuevaMedidaA1, adapt);
            if (insertOK) {
                lista = meLanbide72Manager.getDatosMedidasA1(numExp, codOrganizacion, adapt);
                log.debug("Medida A1 Insertada Correctamente");
            } else {
                log.debug("NO se ha insertado correctamente la nueva medida A1 ");
                codigoOperacion = "1";
            }
        } catch (Exception ex) {
            log.debug("Error al parsear los parámetros recibidos del jsp al objeto MedidaA1BCVO" + ex.getMessage());
            codigoOperacion = "2";
        }

        String xmlSalida = null;
        xmlSalida = obtenerXmlSalidaA1(request, codigoOperacion, lista);
        retornarXML(xmlSalida, response);
        
    }
    
    public String cargarModificarMedidaA1(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String nuevo = "0";
        String urlnuevaMedida = "/jsp/extension/melanbide72/nuevaMedidaA1.jsp?codOrganizacion=" + codOrganizacion;
        try {
            if (request.getAttribute("nuevo") != null) {
                if (!request.getAttribute("nuevo").toString().equals("0")) {
                    request.setAttribute("nuevo", nuevo);
                }
            } else {
                request.setAttribute("nuevo", nuevo);
            }
            String id = request.getParameter("id");
      
            if (id != null && !id.equals("")) {
                MedidaA1BCVO datModif = MeLanbide72Manager.getInstance().getMedidaA1PorID(id, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (datModif != null) {
                    request.setAttribute("datModif", datModif);
                }
            }

        } catch (Exception ex) {
            log.debug("Error al tratar de preparar los datos para modificar y llamar la jsp de modificacion de medida A1 : " + ex.getMessage());
        }
        return urlnuevaMedida;

    }

    public void modificarMedidaA1(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<MedidaA1BCVO> lista = new ArrayList<MedidaA1BCVO>();

        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
    
            String id = (String) request.getParameter("id");
            String numExp = (String) request.getParameter("expediente");
            String nombre = (String) request.getParameter("nombre");
            String nif = (String) request.getParameter("nif");
            String importeAnual = (String) request.getParameter("importeAnual").replace(",", ".");
            String objetoContrato = (String) request.getParameter("objetoContrato");

            if (id == null || id.equals("")) {
                log.debug("No se ha recibido desde la JSP el id de la Medida A1 a Modificar ");
                codigoOperacion = "3";
            } else {
                MedidaA1BCVO datModif = MeLanbide72Manager.getInstance().getMedidaA1PorID(id, adapt);
                numExp = datModif.getNumExp();
                datModif.setId(Integer.parseInt(id));

                datModif.setNumExp(numExp);
                datModif.setNombre(nombre);
                datModif.setNif(nif);
                if (importeAnual != null && !"".equals(importeAnual)) {
                    datModif.setImporteAnual(Double.parseDouble(importeAnual));
                } else {
                    datModif.setImporteAnual(null);
                }
                datModif.setObjetoContrato(objetoContrato);

                MeLanbide72Manager meLanbide72Manager = MeLanbide72Manager.getInstance();
                boolean modOK = meLanbide72Manager.modificarMedidaA1(datModif, adapt);
                if (modOK) {
                    try {
                        lista = meLanbide72Manager.getDatosMedidasA1(numExp, codOrganizacion, adapt);
                    } catch (BDException bde) {
                        codigoOperacion = "1";
                        log.debug("Error de tipo BD al recuperar la lista de Medidas A1 después de Modificar un registro : " + bde.getMensaje());
                    } catch (Exception ex) {
                        codigoOperacion = "2";
                        log.debug("Error al recuperar la lista de Medidas A1 después de Modificar un registro : " + ex.getMessage());
                    }
                } else {
                    codigoOperacion = "2";
                }
            }
        } catch (Exception ex) {
            log.debug("Error modificar --- ", ex);
            codigoOperacion = "2";
        }

        String xmlSalida = null;
        xmlSalida = obtenerXmlSalidaA1(request, codigoOperacion, lista);
        retornarXML(xmlSalida, response);

    }
    
    public void eliminarMedidaA1(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<MedidaA1BCVO> lista = new ArrayList<MedidaA1BCVO>();
        String numExp = "";
        try {
            String id = (String) request.getParameter("id");
            if (id == null || id.equals("")) {
                log.debug("No se ha recibido desde la JSP el id de la Medida A1 a Elimnar ");
                codigoOperacion = "3";
            } else {
                numExp = request.getParameter("numExp").toString();
                AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                int result = MeLanbide72Manager.getInstance().eliminarMedidaA1(id, adapt);
                if (result <= 0) {
                    codigoOperacion = "1";
                } else {
                    codigoOperacion = "0";
                    try {
                        lista = MeLanbide72Manager.getInstance().getDatosMedidasA1(numExp, codOrganizacion, adapt);
                    } catch (Exception ex) {
                        log.debug("Error al recuperar la lista de medidas A1 después de eliminar un registro");
                    }
                }
            }
        } catch (Exception ex) {
            log.debug("Error eliminando un registro : " + ex);
            codigoOperacion = "2";
        }
        
        String xmlSalida = null;
        xmlSalida = obtenerXmlSalidaA1(request, codigoOperacion, lista);
        retornarXML(xmlSalida, response);
        
    }
    
    public String cargarNuevaMedidaA2(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String nuevo = "1";
        String numExp = "";
        String urlnuevaMedida = "/jsp/extension/melanbide72/nuevaMedidaA2.jsp?codOrganizacion=" + codOrganizacion;
        try {
            if (request.getAttribute("nuevo") != null) {
                if (request.getAttribute("nuevo").toString().equals("0")) {
                    request.setAttribute("nuevo", nuevo);
                }
            } else {
                request.setAttribute("nuevo", nuevo);
            }
            if (request.getParameter("numExp") != null) {
                numExp = request.getParameter("numExp").toString();
                request.setAttribute("numExp", numExp);
            }

        } catch (Exception ex) {
            log.debug("Se ha presentado un error al intentar preparar la jsp de una Nueva Medida A2 : " + ex.getMessage());
        }
        return urlnuevaMedida;
    }
    
    public void crearNuevaMedidaA2(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        MedidaA2VO nuevaMedidaA2 = new MedidaA2VO();
        List<MedidaA2VO> lista = new ArrayList<MedidaA2VO>();
        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            //parámetros
            String numExp = (String) request.getParameter("expediente");
            String nombre = (String) request.getParameter("nombre");
            String apellido1 = (String) request.getParameter("apellido1");
            String apellido2 = (String) request.getParameter("apellido2");
            String tipoDocumento = (String) request.getParameter("tipoDocumento");
            String dni = (String) request.getParameter("dni");
            String tfno = (String) request.getParameter("tfno");
            String email = (String) request.getParameter("email");
            String provincia = (String) request.getParameter("provincia");
            String municipio = (String) request.getParameter("municipio");
            String localidad = (String) request.getParameter("localidad");
            String direccion = (String) request.getParameter("direccion");
            String portal = (String) request.getParameter("portal");
            String piso = (String) request.getParameter("piso");
            String letra = (String) request.getParameter("letra");
            String cPostal = (String) request.getParameter("cPostal");
            String importeAnual = (String) request.getParameter("importeAnual").replace(",", ".");
            String objetoContrato = (String) request.getParameter("objetoContrato");

            nuevaMedidaA2.setNumExp(numExp);
            nuevaMedidaA2.setNombre(nombre);
            nuevaMedidaA2.setApellido1(apellido1);
            nuevaMedidaA2.setApellido2(apellido2);
            nuevaMedidaA2.setTipoDocumento(tipoDocumento);
            nuevaMedidaA2.setDni(dni);
            nuevaMedidaA2.setTfno(tfno);
            nuevaMedidaA2.setEmail(email);
            nuevaMedidaA2.setProvincia(provincia);
            nuevaMedidaA2.setMunicipio(municipio);
            nuevaMedidaA2.setLocalidad(localidad);
            nuevaMedidaA2.setDireccion(direccion);
            nuevaMedidaA2.setPortal(portal);
            nuevaMedidaA2.setPiso(piso);
            nuevaMedidaA2.setLetra(letra);
            nuevaMedidaA2.setC_postal(cPostal);
            if (importeAnual != null && !"".equals(importeAnual)) {
                nuevaMedidaA2.setImporteAnual(Double.parseDouble(importeAnual));
            }
            nuevaMedidaA2.setObjetoContrato(objetoContrato);

            MeLanbide72Manager meLanbide72Manager = MeLanbide72Manager.getInstance();
            boolean insertOK = meLanbide72Manager.crearNuevaMedidaA2(nuevaMedidaA2, adapt);
            if (insertOK) {
                lista = meLanbide72Manager.getDatosMedidasA2(numExp, codOrganizacion, adapt);
                log.debug("Medida A2 Insertada Correctamente");
            } else {
                log.debug("NO se ha insertado correctamente la nueva medida A2 ");
                codigoOperacion = "1";
            }
        } catch (Exception ex) {
            log.debug("Error al parsear los parámetros recibidos del jsp al objeto MedidaA2VO" + ex.getMessage());
            codigoOperacion = "2";
        }

        String xmlSalida = null;
        xmlSalida = obtenerXmlSalidaA2(request, codigoOperacion, lista);
        retornarXML(xmlSalida, response);
        
    }
    
    public String cargarModificarMedidaA2(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String nuevo = "0";
        String urlnuevaMedida = "/jsp/extension/melanbide72/nuevaMedidaA2.jsp?codOrganizacion=" + codOrganizacion;
        try {
            if (request.getAttribute("nuevo") != null) {
                if (!request.getAttribute("nuevo").toString().equals("0")) {
                    request.setAttribute("nuevo", nuevo);
                }
            } else {
                request.setAttribute("nuevo", nuevo);
            }
            String id = request.getParameter("id");
      
            if (id != null && !id.equals("")) {
                MedidaA2VO datModif = MeLanbide72Manager.getInstance().getMedidaA2PorID(id, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (datModif != null) {
                    request.setAttribute("datModif", datModif);
                }
            }

        } catch (Exception ex) {
            log.debug("Error al tratar de preparar los datos para modificar y llamar la jsp de modificacion de medida A2 : " + ex.getMessage());
        }
        return urlnuevaMedida;

    }

    public void modificarMedidaA2(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<MedidaA2VO> lista = new ArrayList<MedidaA2VO>();

        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
    
            String id = (String) request.getParameter("id");
            String numExp = (String) request.getParameter("expediente");
            String nombre = (String) request.getParameter("nombre");
            String apellido1 = (String) request.getParameter("apellido1");
            String apellido2 = (String) request.getParameter("apellido2");
            String tipoDocumento = (String) request.getParameter("tipoDocumento");
            String dni = (String) request.getParameter("dni");
            String tfno = (String) request.getParameter("tfno");
            String email = (String) request.getParameter("email");
            String provincia = (String) request.getParameter("provincia");
            String municipio = (String) request.getParameter("municipio");
            String localidad = (String) request.getParameter("localidad");
            String direccion = (String) request.getParameter("direccion");
            String portal = (String) request.getParameter("portal");
            String piso = (String) request.getParameter("piso");
            String letra = (String) request.getParameter("letra");
            String cPostal = (String) request.getParameter("cPostal");
            String importeAnual = (String) request.getParameter("importeAnual").replace(",", ".");
            String objetoContrato = (String) request.getParameter("objetoContrato");

            if (id == null || id.equals("")) {
                log.debug("No se ha recibido desde la JSP el id de la Medida A2 a Modificar ");
                codigoOperacion = "3";
            } else {
                MedidaA2VO datModif = MeLanbide72Manager.getInstance().getMedidaA2PorID(id, adapt);
                numExp = datModif.getNumExp();
                datModif.setId(Integer.parseInt(id));

                datModif.setNumExp(numExp);
                datModif.setNombre(nombre);
                datModif.setApellido1(apellido1);
                datModif.setApellido2(apellido2);
                datModif.setTipoDocumento(tipoDocumento);
                datModif.setDni(dni);
                datModif.setTfno(tfno);
                datModif.setEmail(email);
                datModif.setProvincia(provincia);
                datModif.setMunicipio(municipio);
                datModif.setLocalidad(localidad);
                datModif.setDireccion(direccion);
                datModif.setPortal(portal);
                datModif.setPiso(piso);
                datModif.setLetra(letra);
                datModif.setC_postal(cPostal);
                if (importeAnual != null && !"".equals(importeAnual)) {
                    datModif.setImporteAnual(Double.parseDouble(importeAnual));
                } else {
                    datModif.setImporteAnual(null);
                }
                datModif.setObjetoContrato(objetoContrato);

                MeLanbide72Manager meLanbide72Manager = MeLanbide72Manager.getInstance();
                boolean modOK = meLanbide72Manager.modificarMedidaA2(datModif, adapt);
                if (modOK) {
                    try {
                        lista = meLanbide72Manager.getDatosMedidasA2(numExp, codOrganizacion, adapt);
                    } catch (BDException bde) {
                        codigoOperacion = "1";
                        log.debug("Error de tipo BD al recuperar la lista de Medidas A2 después de Modificar un registro : " + bde.getMensaje());
                    } catch (Exception ex) {
                        codigoOperacion = "2";
                        log.debug("Error al recuperar la lista de Medidas A2 después de Modificar un registro : " + ex.getMessage());
                    }
                } else {
                    codigoOperacion = "2";
                }
            }
        } catch (Exception ex) {
            log.debug("Error modificar --- ", ex);
            codigoOperacion = "2";
        }

        String xmlSalida = null;
        xmlSalida = obtenerXmlSalidaA2(request, codigoOperacion, lista);
        retornarXML(xmlSalida, response);

    }
    
    public void eliminarMedidaA2(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<MedidaA2VO> lista = new ArrayList<MedidaA2VO>();
        String numExp = "";
        try {
            String id = (String) request.getParameter("id");
            if (id == null || id.equals("")) {
                log.debug("No se ha recibido desde la JSP el id de la Medida A2 a Elimnar ");
                codigoOperacion = "3";
            } else {
                numExp = request.getParameter("numExp").toString();
                AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                int result = MeLanbide72Manager.getInstance().eliminarMedidaA2(id, adapt);
                if (result <= 0) {
                    codigoOperacion = "1";
                } else {
                    codigoOperacion = "0";
                    try {
                        lista = MeLanbide72Manager.getInstance().getDatosMedidasA2(numExp, codOrganizacion, adapt);
                    } catch (Exception ex) {
                        log.debug("Error al recuperar la lista de medidas A2 después de eliminar un registro");
                    }
                }
            }
        } catch (Exception ex) {
            log.debug("Error eliminando un registro : " + ex);
            codigoOperacion = "2";
        }
        
        String xmlSalida = null;
        xmlSalida = obtenerXmlSalidaA2(request, codigoOperacion, lista);
        retornarXML(xmlSalida, response);
        
    }
    
    public String cargarNuevaMedidaB(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String nuevo = "1";
        String numExp = "";
        String urlnuevaMedida = "/jsp/extension/melanbide72/nuevaMedidaB.jsp?codOrganizacion=" + codOrganizacion;
        try {
            if (request.getAttribute("nuevo") != null) {
                if (request.getAttribute("nuevo").toString().equals("0")) {
                    request.setAttribute("nuevo", nuevo);
                }
            } else {
                request.setAttribute("nuevo", nuevo);
            }
            if (request.getParameter("numExp") != null) {
                numExp = request.getParameter("numExp").toString();
                request.setAttribute("numExp", numExp);
            }

        } catch (Exception ex) {
            log.debug("Se ha presentado un error al intentar preparar la jsp de una Nueva Medida B : " + ex.getMessage());
        }
        return urlnuevaMedida;
    }
    
    public void crearNuevaMedidaB(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        MedidaA1BCVO nuevaMedidaB = new MedidaA1BCVO();
        List<MedidaA1BCVO> lista = new ArrayList<MedidaA1BCVO>();
        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            //parámetros
            String numExp = (String) request.getParameter("expediente");
            String nombre = (String) request.getParameter("nombre");
            String nif = (String) request.getParameter("nif");
            String importeAnual = (String) request.getParameter("importeAnual").replace(",", ".");

            nuevaMedidaB.setNumExp(numExp);
            nuevaMedidaB.setNombre(nombre);
            nuevaMedidaB.setNif(nif);
            if (importeAnual != null && !"".equals(importeAnual)) {
                nuevaMedidaB.setImporteAnual(Double.parseDouble(importeAnual));
            }

            MeLanbide72Manager meLanbide72Manager = MeLanbide72Manager.getInstance();
            boolean insertOK = meLanbide72Manager.crearNuevaMedidaB(nuevaMedidaB, adapt);
            if (insertOK) {
                lista = meLanbide72Manager.getDatosMedidasB(numExp, codOrganizacion, adapt);
                log.debug("Medida B Insertada Correctamente");
            } else {
                log.debug("NO se ha insertado correctamente la nueva medida B ");
                codigoOperacion = "1";
            }
        } catch (Exception ex) {
            log.debug("Error al parsear los parámetros recibidos del jsp al objeto MedidaA1BCVO" + ex.getMessage());
            codigoOperacion = "2";
        }

        String xmlSalida = null;
        xmlSalida = obtenerXmlSalidaBC(request, codigoOperacion, lista);
        retornarXML(xmlSalida, response);
        
    }
    
    public String cargarModificarMedidaB(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String nuevo = "0";
        String urlnuevaMedida = "/jsp/extension/melanbide72/nuevaMedidaB.jsp?codOrganizacion=" + codOrganizacion;
        try {
            if (request.getAttribute("nuevo") != null) {
                if (!request.getAttribute("nuevo").toString().equals("0")) {
                    request.setAttribute("nuevo", nuevo);
                }
            } else {
                request.setAttribute("nuevo", nuevo);
            }
            String id = request.getParameter("id");
      
            if (id != null && !id.equals("")) {
                MedidaA1BCVO datModif = MeLanbide72Manager.getInstance().getMedidaBPorID(id, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (datModif != null) {
                    request.setAttribute("datModif", datModif);
                }
            }

        } catch (Exception ex) {
            log.debug("Error al tratar de preparar los datos para modificar y llamar la jsp de modificacion de medida B : " + ex.getMessage());
        }
        return urlnuevaMedida;

    }

    public void modificarMedidaB(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<MedidaA1BCVO> lista = new ArrayList<MedidaA1BCVO>();

        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
    
            String id = (String) request.getParameter("id");
            String numExp = (String) request.getParameter("expediente");
            String nombre = (String) request.getParameter("nombre");
            String nif = (String) request.getParameter("nif");
            String importeAnual = (String) request.getParameter("importeAnual").replace(",", ".");

            if (id == null || id.equals("")) {
                log.debug("No se ha recibido desde la JSP el id de la Medida B a Modificar ");
                codigoOperacion = "3";
            } else {
                MedidaA1BCVO datModif = MeLanbide72Manager.getInstance().getMedidaBPorID(id, adapt);
                numExp = datModif.getNumExp();
                datModif.setId(Integer.parseInt(id));

                datModif.setNumExp(numExp);
                datModif.setNombre(nombre);
                datModif.setNif(nif);
                if (importeAnual != null && !"".equals(importeAnual)) {
                    datModif.setImporteAnual(Double.parseDouble(importeAnual));
                } else {
                    datModif.setImporteAnual(null);
                }

                MeLanbide72Manager meLanbide72Manager = MeLanbide72Manager.getInstance();
                boolean modOK = meLanbide72Manager.modificarMedidaB(datModif, adapt);
                if (modOK) {
                    try {
                        lista = meLanbide72Manager.getDatosMedidasB(numExp, codOrganizacion, adapt);
                    } catch (BDException bde) {
                        codigoOperacion = "1";
                        log.debug("Error de tipo BD al recuperar la lista de Medidas B después de Modificar un registro : " + bde.getMensaje());
                    } catch (Exception ex) {
                        codigoOperacion = "2";
                        log.debug("Error al recuperar la lista de Medidas B después de Modificar un registro : " + ex.getMessage());
                    }
                } else {
                    codigoOperacion = "2";
                }
            }
        } catch (Exception ex) {
            log.debug("Error modificar --- ", ex);
            codigoOperacion = "2";
        }

        String xmlSalida = null;
        xmlSalida = obtenerXmlSalidaBC(request, codigoOperacion, lista);
        retornarXML(xmlSalida, response);

    }
    
    public void eliminarMedidaB(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<MedidaA1BCVO> lista = new ArrayList<MedidaA1BCVO>();
        String numExp = "";
        try {
            String id = (String) request.getParameter("id");
            if (id == null || id.equals("")) {
                log.debug("No se ha recibido desde la JSP el id de la Medida B a Elimnar ");
                codigoOperacion = "3";
            } else {
                numExp = request.getParameter("numExp").toString();
                AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                int result = MeLanbide72Manager.getInstance().eliminarMedidaB(id, adapt);
                if (result <= 0) {
                    codigoOperacion = "1";
                } else {
                    codigoOperacion = "0";
                    try {
                        lista = MeLanbide72Manager.getInstance().getDatosMedidasB(numExp, codOrganizacion, adapt);
                    } catch (Exception ex) {
                        log.debug("Error al recuperar la lista de medidas B después de eliminar un registro");
                    }
                }
            }
        } catch (Exception ex) {
            log.debug("Error eliminando un registro : " + ex);
            codigoOperacion = "2";
        }
        
        String xmlSalida = null;
        xmlSalida = obtenerXmlSalidaBC(request, codigoOperacion, lista);
        retornarXML(xmlSalida, response);
        
    }
    
    public String cargarNuevaMedidaC(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String nuevo = "1";
        String numExp = "";
        String urlnuevaMedida = "/jsp/extension/melanbide72/nuevaMedidaC.jsp?codOrganizacion=" + codOrganizacion;
        try {
            if (request.getAttribute("nuevo") != null) {
                if (request.getAttribute("nuevo").toString().equals("0")) {
                    request.setAttribute("nuevo", nuevo);
                }
            } else {
                request.setAttribute("nuevo", nuevo);
            }
            if (request.getParameter("numExp") != null) {
                numExp = request.getParameter("numExp").toString();
                request.setAttribute("numExp", numExp);
            }

        } catch (Exception ex) {
            log.debug("Se ha presentado un error al intentar preparar la jsp de una Nueva Medida C : " + ex.getMessage());
        }
        return urlnuevaMedida;
    }
    
    public void crearNuevaMedidaC(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        MedidaA1BCVO nuevaMedidaC = new MedidaA1BCVO();
        List<MedidaA1BCVO> lista = new ArrayList<MedidaA1BCVO>();
        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            //parámetros
            String numExp = (String) request.getParameter("expediente");
            String nombre = (String) request.getParameter("nombre");
            String nif = (String) request.getParameter("nif");
            String importeAnual = (String) request.getParameter("importeAnual").replace(",", ".");

            nuevaMedidaC.setNumExp(numExp);
            nuevaMedidaC.setNombre(nombre);
            nuevaMedidaC.setNif(nif);
            if (importeAnual != null && !"".equals(importeAnual)) {
                nuevaMedidaC.setImporteAnual(Double.parseDouble(importeAnual));
            }

            MeLanbide72Manager meLanbide72Manager = MeLanbide72Manager.getInstance();
            boolean insertOK = meLanbide72Manager.crearNuevaMedidaC(nuevaMedidaC, adapt);
            if (insertOK) {
                lista = meLanbide72Manager.getDatosMedidasC(numExp, codOrganizacion, adapt);
                log.debug("Medida C Insertada Correctamente");
            } else {
                log.debug("NO se ha insertado correctamente la nueva medida C ");
                codigoOperacion = "1";
            }
        } catch (Exception ex) {
            log.debug("Error al parsear los parámetros recibidos del jsp al objeto MedidaA1BCVO" + ex.getMessage());
            codigoOperacion = "2";
        }

        String xmlSalida = null;
        xmlSalida = obtenerXmlSalidaBC(request, codigoOperacion, lista);
        retornarXML(xmlSalida, response);
        
    }
    
    public String cargarModificarMedidaC(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String nuevo = "0";
        String urlnuevaMedida = "/jsp/extension/melanbide72/nuevaMedidaC.jsp?codOrganizacion=" + codOrganizacion;
        try {
            if (request.getAttribute("nuevo") != null) {
                if (!request.getAttribute("nuevo").toString().equals("0")) {
                    request.setAttribute("nuevo", nuevo);
                }
            } else {
                request.setAttribute("nuevo", nuevo);
            }
            String id = request.getParameter("id");
      
            if (id != null && !id.equals("")) {
                MedidaA1BCVO datModif = MeLanbide72Manager.getInstance().getMedidaCPorID(id, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (datModif != null) {
                    request.setAttribute("datModif", datModif);
                }
            }

        } catch (Exception ex) {
            log.debug("Error al tratar de preparar los datos para modificar y llamar la jsp de modificacion de medida C : " + ex.getMessage());
        }
        return urlnuevaMedida;

    }

    public void modificarMedidaC(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<MedidaA1BCVO> lista = new ArrayList<MedidaA1BCVO>();

        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
    
            String id = (String) request.getParameter("id");
            String numExp = (String) request.getParameter("expediente");
            String nombre = (String) request.getParameter("nombre");
            String nif = (String) request.getParameter("nif");
            String importeAnual = (String) request.getParameter("importeAnual").replace(",", ".");

            if (id == null || id.equals("")) {
                log.debug("No se ha recibido desde la JSP el id de la Medida C a Modificar ");
                codigoOperacion = "3";
            } else {
                MedidaA1BCVO datModif = MeLanbide72Manager.getInstance().getMedidaCPorID(id, adapt);
                numExp = datModif.getNumExp();
                datModif.setId(Integer.parseInt(id));

                datModif.setNumExp(numExp);
                datModif.setNombre(nombre);
                datModif.setNif(nif);
                if (importeAnual != null && !"".equals(importeAnual)) {
                    datModif.setImporteAnual(Double.parseDouble(importeAnual));
                } else {
                    datModif.setImporteAnual(null);
                }

                MeLanbide72Manager meLanbide72Manager = MeLanbide72Manager.getInstance();
                boolean modOK = meLanbide72Manager.modificarMedidaC(datModif, adapt);
                if (modOK) {
                    try {
                        lista = meLanbide72Manager.getDatosMedidasC(numExp, codOrganizacion, adapt);
                    } catch (BDException bde) {
                        codigoOperacion = "1";
                        log.debug("Error de tipo BD al recuperar la lista de Medidas C después de Modificar un registro : " + bde.getMensaje());
                    } catch (Exception ex) {
                        codigoOperacion = "2";
                        log.debug("Error al recuperar la lista de Medidas C después de Modificar un registro : " + ex.getMessage());
                    }
                } else {
                    codigoOperacion = "2";
                }
            }
        } catch (Exception ex) {
            log.debug("Error modificar --- ", ex);
            codigoOperacion = "2";
        }

        String xmlSalida = null;
        xmlSalida = obtenerXmlSalidaBC(request, codigoOperacion, lista);
        retornarXML(xmlSalida, response);

    }
    
    public void eliminarMedidaC(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<MedidaA1BCVO> lista = new ArrayList<MedidaA1BCVO>();
        String numExp = "";
        try {
            String id = (String) request.getParameter("id");
            if (id == null || id.equals("")) {
                log.debug("No se ha recibido desde la JSP el id de la Medida C a Elimnar ");
                codigoOperacion = "3";
            } else {
                numExp = request.getParameter("numExp").toString();
                AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                int result = MeLanbide72Manager.getInstance().eliminarMedidaC(id, adapt);
                if (result <= 0) {
                    codigoOperacion = "1";
                } else {
                    codigoOperacion = "0";
                    try {
                        lista = MeLanbide72Manager.getInstance().getDatosMedidasC(numExp, codOrganizacion, adapt);
                    } catch (Exception ex) {
                        log.debug("Error al recuperar la lista de medidas C después de eliminar un registro");
                    }
                }
            }
        } catch (Exception ex) {
            log.debug("Error eliminando un registro : " + ex);
            codigoOperacion = "2";
        }
        
        String xmlSalida = null;
        xmlSalida = obtenerXmlSalidaBC(request, codigoOperacion, lista);
        retornarXML(xmlSalida, response);
        
    }
    
    public void guardarSuplementarios(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        
        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
           
            String numExp = (String) request.getParameter("numExpediente");     
            
            String num_trab_sust_A1 = (String) request.getParameter("num_trab_sust_A1");
            String periodo_contr_A1 = (String) request.getParameter("periodo_contr_A1");
            String num_trab_sust_A2 = (String) request.getParameter("num_trab_sust_A2");
            String periodo_contr_A2 = (String) request.getParameter("periodo_contr_A2");
            String num_trab_sust_B = (String) request.getParameter("num_trab_sust_B");
            String periodo_contr_B = (String) request.getParameter("periodo_contr_B");
            String num_trab_sust_C = (String) request.getParameter("num_trab_sust_C");
            String periodo_contr_C = (String) request.getParameter("periodo_contr_C");
            String pers_disc_ocup_C = (String) request.getParameter("pers_disc_ocup_C");
            String cuantificacion_econ = (String) request.getParameter("cuantificacion_econ");
            String imp_anual_total = (String) request.getParameter("imp_anual_total");
            String contratacion_total = (String) request.getParameter("contratacion_total");
            
            
            MeLanbide72Manager meLanbide72Manager = MeLanbide72Manager.getInstance();
            boolean grabarOK = meLanbide72Manager.guardarSuplementarios(codOrganizacion, numExp, num_trab_sust_A1,
                                                                                                 periodo_contr_A1,
                                                                                                 num_trab_sust_A2,
                                                                                                 periodo_contr_A2,
                                                                                                 num_trab_sust_B,
                                                                                                 periodo_contr_B,
                                                                                                 num_trab_sust_C,
                                                                                                 periodo_contr_C,
                                                                                                 pers_disc_ocup_C,
                                                                                                 cuantificacion_econ,
                                                                                                 imp_anual_total,
                                                                                                 contratacion_total,
                                                                        adapt);
            if (grabarOK) {
                log.debug("Campos suplementarios guardados correctamente");
            } else {
                log.debug("No se han guardado correctamente los campos suplementarios");
                codigoOperacion = "1";
            }
        } catch (Exception ex) {
            log.debug("Error al parsear los parámetros recibidos del jsp " + ex.getMessage());
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
    
    private void retornarXML(String salida, HttpServletResponse response) {
        try {
            if (salida != null) {
                response.setContentType("text/xml");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                out.print(salida);
                out.flush();
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private String obtenerXmlSalidaA1(HttpServletRequest request, String codigoOperacion, List<MedidaA1BCVO> lista) {
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        for (MedidaA1BCVO fila : lista) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append(fila.getId() != null ? fila.getId().toString() : "");
            xmlSalida.append("</ID>");
            xmlSalida.append("<NOMBRE>");
            xmlSalida.append(fila.getNombre());
            xmlSalida.append("</NOMBRE>");
            xmlSalida.append("<NIF_CIF>");
            xmlSalida.append(fila.getNif());
            xmlSalida.append("</NIF_CIF>");
            xmlSalida.append("<IMPORTE_ANUAL>");
            if (fila.getImporteAnual()!= null && !"".equals(fila.getImporteAnual())) {
                xmlSalida.append(fila.getImporteAnual());
            } else {
                xmlSalida.append("null");
            }
            xmlSalida.append("</IMPORTE_ANUAL>");
            xmlSalida.append("<OBJETO_CONTRATO>");
            xmlSalida.append(fila.getObjetoContrato());
            xmlSalida.append("</OBJETO_CONTRATO>");
            
            xmlSalida.append("</FILA>");
        }
        xmlSalida.append("</RESPUESTA>");
        log.debug("xml: " + xmlSalida);
        return xmlSalida.toString();
    }
    
    private String obtenerXmlSalidaA2(HttpServletRequest request, String codigoOperacion, List<MedidaA2VO> lista) {
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        for (MedidaA2VO fila : lista) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append(fila.getId() != null ? fila.getId().toString() : "");
            xmlSalida.append("</ID>");
            xmlSalida.append("<NOMBRE>");
            xmlSalida.append(fila.getNombre());
            xmlSalida.append("</NOMBRE>");
            xmlSalida.append("<APELLIDO1>");
            xmlSalida.append(fila.getApellido1());
            xmlSalida.append("</APELLIDO1>");
            xmlSalida.append("<APELLIDO2>");
            xmlSalida.append(fila.getApellido2());
            xmlSalida.append("</APELLIDO2>");
            xmlSalida.append("<TIPO_DOCUMENTO>");
            xmlSalida.append(fila.getTipoDocumento());
            xmlSalida.append("</TIPO_DOCUMENTO>");
            xmlSalida.append("<DNI_NIE>");
            xmlSalida.append(fila.getDni());
            xmlSalida.append("</DNI_NIE>");
            xmlSalida.append("<TFNO>");
            xmlSalida.append(fila.getTfno());
            xmlSalida.append("</TFNO>");
            xmlSalida.append("<EMAIL>");
            xmlSalida.append(fila.getEmail());
            xmlSalida.append("</EMAIL>");
            xmlSalida.append("<PROVINCIA>");
            xmlSalida.append(fila.getProvincia());
            xmlSalida.append("</PROVINCIA>");
            xmlSalida.append("<MUNICIPIO>");
            xmlSalida.append(fila.getMunicipio());
            xmlSalida.append("</MUNICIPIO>");
            xmlSalida.append("<LOCALIDAD>");
            xmlSalida.append(fila.getLocalidad());
            xmlSalida.append("</LOCALIDAD>");
            xmlSalida.append("<DIRECCION>");
            xmlSalida.append(fila.getDireccion());
            xmlSalida.append("</DIRECCION>");
            xmlSalida.append("<PORTAL>");
            xmlSalida.append(fila.getPortal());
            xmlSalida.append("</PORTAL>");
            xmlSalida.append("<PISO>");
            xmlSalida.append(fila.getPiso());
            xmlSalida.append("</PISO>");
            xmlSalida.append("<LETRA>");
            xmlSalida.append(fila.getLetra());
            xmlSalida.append("</LETRA>");
            xmlSalida.append("<C_POSTAL>");
            xmlSalida.append(fila.getC_postal());
            xmlSalida.append("</C_POSTAL>");
            xmlSalida.append("<IMPORTE_ANUAL>");
            if (fila.getImporteAnual()!= null && !"".equals(fila.getImporteAnual())) {
                xmlSalida.append(fila.getImporteAnual());
            } else {
                xmlSalida.append("null");
            }
            xmlSalida.append("</IMPORTE_ANUAL>");
            xmlSalida.append("<OBJETO_CONTRATO>");
            xmlSalida.append(fila.getObjetoContrato());
            xmlSalida.append("</OBJETO_CONTRATO>");
            
            xmlSalida.append("</FILA>");
        }
        xmlSalida.append("</RESPUESTA>");
        log.debug("xml: " + xmlSalida);
        return xmlSalida.toString();
    }
    
    private String obtenerXmlSalidaBC(HttpServletRequest request, String codigoOperacion, List<MedidaA1BCVO> lista) {
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        for (MedidaA1BCVO fila : lista) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append(fila.getId() != null ? fila.getId().toString() : "");
            xmlSalida.append("</ID>");
            xmlSalida.append("<NOMBRE>");
            xmlSalida.append(fila.getNombre());
            xmlSalida.append("</NOMBRE>");
            xmlSalida.append("<NIF_CIF>");
            xmlSalida.append(fila.getNif());
            xmlSalida.append("</NIF_CIF>");
            xmlSalida.append("<IMPORTE_ANUAL>");
            if (fila.getImporteAnual()!= null && !"".equals(fila.getImporteAnual())) {
                xmlSalida.append(fila.getImporteAnual());
            } else {
                xmlSalida.append("null");
            }
            xmlSalida.append("</IMPORTE_ANUAL>");
            
            xmlSalida.append("</FILA>");
        }
        xmlSalida.append("</RESPUESTA>");
        log.debug("xml: " + xmlSalida);
        return xmlSalida.toString();
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
    
    
    
    

}
