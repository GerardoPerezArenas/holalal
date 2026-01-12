package es.altia.flexia.integracion.moduloexterno.melanbide65;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.melanbide65.exception.MeLanbide65Exception;
import es.altia.flexia.integracion.moduloexterno.melanbide65.i18n.MeLanbide65I18n;
import es.altia.flexia.integracion.moduloexterno.melanbide65.manager.MeLanbide65Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide65.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide65.util.ConstantesMeLanbide65;
import es.altia.flexia.integracion.moduloexterno.melanbide65.vo.DesplegableAdmonLocalVO;
import es.altia.flexia.integracion.moduloexterno.melanbide65.vo.EncargadoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide65.vo.ExpUAAPCriteriosFiltroVO;
import es.altia.flexia.integracion.moduloexterno.melanbide65.vo.ExpedienteUAAPVO;
import es.altia.flexia.integracion.moduloexterno.melanbide65.vo.PersonaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide65.vo.TecnicoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide65.vo.TrabajadorVO;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;
import es.altia.technical.PortableContext;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import es.altia.flexia.integracion.moduloexterno.melanbide65.util.MeLanbide65MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide65.vo.PersonaContratadaVO;
import org.json.JSONArray;
import es.altia.util.ajax.respuesta.RespuestaAjaxUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide_interop.nisae.GestionServiciosNISAECVL;
import es.altia.flexia.integracion.moduloexterno.melanbide_interop.nisae.entities.InteropLlamadasServiciosNisae;
import es.altia.flexia.integracion.moduloexterno.melanbide_interop.util.ConstantesMeLanbideInterop;
import es.altia.flexia.integracion.moduloexterno.melanbide_interop.MELANBIDE_INTEROP;
import es.altia.flexia.integracion.moduloexterno.melanbide_interop.vo.cvl.Persona;

public class MELANBIDE65 extends ModuloIntegracionExterno {

    private static Logger log = LogManager.getLogger(MELANBIDE65.class);
    private static final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    ResourceBundle m_Conf = ResourceBundle.getBundle("common");
    private static MELANBIDE_INTEROP melanbide_interop = new MELANBIDE_INTEROP();

    private GsonBuilder gsonBuilder = new GsonBuilder().serializeNulls();
    private Gson gson = gsonBuilder.serializeNulls().create();

    // Alta Expedientes via registro platea --> MELANBIDE 42
    public void cargarExpedienteExtension(int codigoOrganizacion, String numeroExpediente, String xml) throws Exception {
        final Class cls = Class.forName("es.altia.flexia.integracion.moduloexterno.melanbide42.MELANBIDE42");
        final Object me42Class = cls.newInstance();
        final Class[] types = {int.class, String.class, String.class};
        final Method method = cls.getMethod("cargarExpedienteExtension", types);
        method.invoke(me42Class, codigoOrganizacion, numeroExpediente, xml);
    }

    public String cargarPantallaAnexoA(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.debug("Entramos en cargarPantallaAnexoA de " + this.getClass().getName());
        AdaptadorSQLBD adapt = null;
        String nombreTabla = null;
        ArrayList<PersonaVO> datos = null;
        ArrayList<TrabajadorVO> trabajadores = null;
        String url = "/jsp/extension/melanbide65/anexoA.jsp";

        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (SQLException ex) {
            log.error(this.getClass().getName() + " Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }

        if (adapt != null) {
            Connection con = null;
            try {
                con = adapt.getConnection();
                nombreTabla = ConstantesMeLanbide65.getNOMBRE_TABLA_TRABAJADOR();

                datos = MeLanbide65Manager.getInstance().obtenerDatos(nombreTabla, numExpediente, adapt, con);
                //Convertimos cada objeto PersonaVO en lo que realmente son, objetos de TrabajadorVO
                trabajadores = new ArrayList<TrabajadorVO>();
                for (PersonaVO persona : datos) {
                    ((TrabajadorVO) persona).setDescSexo(getDescripcionDesplegable(request, ((TrabajadorVO) persona).getDescSexo()));
                    ((TrabajadorVO) persona).setDescTipoContrato(getDescripcionDesplegable(request, ((TrabajadorVO) persona).getDescTipoContrato()));
                    ((TrabajadorVO) persona).setDescTipoJornada(getDescripcionDesplegable(request, ((TrabajadorVO) persona).getDescTipoJornada()));
                    ((TrabajadorVO) persona).setDescPensionista(getDescripcionDesplegable(request,((TrabajadorVO) persona).getDescPensionista()));
                    ((TrabajadorVO) persona).setDescTipoPensionista(getDescripcionDesplegable(request,((TrabajadorVO) persona).getDescTipoPensionista()));

                    trabajadores.add((TrabajadorVO) persona);
                }
                request.setAttribute("relacionTrabajadores", trabajadores);
                request.setAttribute("numExpediente", numExpediente);
            } catch (MeLanbide65Exception ex) {
                log.error("Error al recueperar los datos de trabajadores - MELANBIDE65 - cargarPantallaAnexoA", ex);
            } catch (BDException ex) {
                log.error("Error al recueperar los datos de trabajadores - MELANBIDE65 - cargarPantallaAnexoA", ex);
            } finally {
                try {
                    adapt.devolverConexion(con);
                } catch (BDException e) {
                    log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
                }
            }
        }
        return url;
    }

    public String cargarPantallaAnexoB(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.debug("Entramos en cargarPantallaAnexoB de " + this.getClass().getName());
        AdaptadorSQLBD adapt = null;
        ArrayList<PersonaVO> datos = null;
        ArrayList<EncargadoVO> lista = null;
        String nombreTabla;
        String url = "/jsp/extension/melanbide65/anexoB.jsp";

        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (SQLException ex) {
            log.error(this.getClass().getName() + " Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }

        if (adapt != null) {
            Connection con = null;
            try {
                con = adapt.getConnection();
                nombreTabla = ConstantesMeLanbide65.getNOMBRE_TABLA_TECNICO();

                datos = MeLanbide65Manager.getInstance().obtenerDatos(nombreTabla, numExpediente, adapt, con);
                //Convertimos cada objeto PersonaVO en lo que realmente son, objetos de TrabajadorVO
                lista = new ArrayList<EncargadoVO>();
                for (PersonaVO persona : datos) {
                    ((EncargadoVO) persona).setDescTipoContrato(getDescripcionDesplegable(request, ((EncargadoVO) persona).getDescTipoContrato()));
                    ((EncargadoVO) persona).setDescTipoJornada(getDescripcionDesplegable(request, ((EncargadoVO) persona).getDescTipoJornada()));
                    ((EncargadoVO) persona).setDescPensionista(getDescripcionDesplegable(request,((EncargadoVO) persona).getDescPensionista()));
                    ((EncargadoVO) persona).setDescTipoPensionista(getDescripcionDesplegable(request,((EncargadoVO) persona).getDescTipoPensionista()));

                    lista.add((EncargadoVO) persona);
                }
                request.setAttribute("relacionTecnicos", lista);
                request.setAttribute("numExpediente", numExpediente);
            } catch (MeLanbide65Exception ex) {
                log.error("Error al recueperar los datos de técnicos - MELANBIDE65 - cargarPantallaAnexoA", ex);
            } catch (BDException ex) {
                log.error("Error al recueperar los datos de técnicos - MELANBIDE65 - cargarPantallaAnexoA", ex);
            } finally {
                try {
                    adapt.devolverConexion(con);
                } catch (BDException e) {
                    log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
                }
            }
        }
        return url;
    }

    public String cargarPantallaAnexoC(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.debug("Entramos en cargarPantallaAnexoC de " + this.getClass().getName());
        AdaptadorSQLBD adapt = null;
        ArrayList<PersonaVO> datos = null;
        ArrayList<EncargadoVO> lista = null;
        String nombreTabla;
        String url = "/jsp/extension/melanbide65/anexoC.jsp";

        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (SQLException ex) {
            log.error(this.getClass().getName() + " Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }

        if (adapt != null) {
            Connection con = null;
            try {
                con = adapt.getConnection();
                nombreTabla = ConstantesMeLanbide65.getNOMBRE_TABLA_ENCARGADO();

                datos = MeLanbide65Manager.getInstance().obtenerDatos(nombreTabla, numExpediente, adapt, con);
                //Convertimos cada objeto PersonaVO en lo que realmente son, objetos de TrabajadorVO
                lista = new ArrayList<EncargadoVO>();
                for (PersonaVO persona : datos) {
                    ((EncargadoVO) persona).setDescTipoContrato(getDescripcionDesplegable(request, ((EncargadoVO) persona).getDescTipoContrato()));
                    ((EncargadoVO) persona).setDescTipoJornada(getDescripcionDesplegable(request, ((EncargadoVO) persona).getDescTipoJornada()));
                    ((EncargadoVO) persona).setDescPensionista(getDescripcionDesplegable(request,((EncargadoVO) persona).getDescPensionista()));
                    ((EncargadoVO) persona).setDescTipoPensionista(getDescripcionDesplegable(request,((EncargadoVO) persona).getDescTipoPensionista()));

                    lista.add((EncargadoVO) persona);
                }
                request.setAttribute("relacionEncargados", lista);
                request.setAttribute("numExpediente", numExpediente);
            } catch (MeLanbide65Exception ex) {
                log.error("Error al recueperar los datos de técnicos - MELANBIDE65 - cargarPantallaAnexoA", ex);
            } catch (BDException ex) {
                log.error("Error al recueperar los datos de técnicos - MELANBIDE65 - cargarPantallaAnexoA", ex);
            } finally {
                try {
                    adapt.devolverConexion(con);
                } catch (BDException e) {
                    log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
                }
            }
        }
        return url;
    }

    public String cargarFormularioAltaTr(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String url = "/jsp/extension/melanbide65/formularioAltaModTr.jsp";
        MeLanbide65Manager meLanbide65Manager = MeLanbide65Manager.getInstance();
        try {
            //Cargamos el request los valores  de los desplegables
            List<DesplegableAdmonLocalVO> listaContratos = meLanbide65Manager.getValoresDesplegablesAdmonLocalxdes_cod(ConstantesMeLanbide65.getNOMBRE_LISTA_CONTRATO(), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            List<DesplegableAdmonLocalVO> listaJornada = meLanbide65Manager.getValoresDesplegablesAdmonLocalxdes_cod(ConstantesMeLanbide65.getNOMBRE_LISTA_JORNADA(), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            List<DesplegableAdmonLocalVO> listaSexo = meLanbide65Manager.getValoresDesplegablesAdmonLocalxdes_cod(ConstantesMeLanbide65.getNOMBRE_LISTA_SEXO(), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            List<DesplegableAdmonLocalVO> listaPensionista = MeLanbide65Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConstantesMeLanbide65.getNOMBRE_LISTA_PENSIONISTA(), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));


            for(DesplegableAdmonLocalVO lista : listaPensionista){
                lista.setDes_nom(getDescripcionDesplegable(request,lista.getDes_nom()));
            }

            if (!listaPensionista.isEmpty()) {
                request.setAttribute("listaPensionista", listaPensionista);
            }
            List<DesplegableAdmonLocalVO> listaTipoPensionista = MeLanbide65Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod("BOOL", this.getAdaptSQLBD(String.valueOf(codOrganizacion)));

            for(DesplegableAdmonLocalVO lista : listaTipoPensionista){
                lista.setDes_nom(getDescripcionDesplegable(request,lista.getDes_nom().split("\\|")[0]));
            }



            if (!listaTipoPensionista.isEmpty()) {
                request.setAttribute("listaTipoPensionista", listaTipoPensionista);
            }
            for(DesplegableAdmonLocalVO lista : listaContratos){
                lista.setDes_nom(getDescripcionDesplegable(request,lista.getDes_nom()));
            }

            for(DesplegableAdmonLocalVO lista : listaJornada){
                lista.setDes_nom(getDescripcionDesplegable(request,lista.getDes_nom()));
            }

            for(DesplegableAdmonLocalVO lista : listaSexo){
                lista.setDes_nom(getDescripcionDesplegable(request,lista.getDes_nom()));
            }



            if (!listaContratos.isEmpty()) {
                request.setAttribute("listaContrato", listaContratos);
            }
            if (!listaJornada.isEmpty()) {
                request.setAttribute("listaJornada", listaJornada);
            }
            if (!listaSexo.isEmpty()) {
                request.setAttribute("listaSexo", listaSexo);
            }

        } catch (Exception ex) {
            log.debug("Se ha presentado un error al intentar preparar la jsp de un nuevo trabajador : " + ex.getMessage());
        }
        request.setAttribute("nuevo", "1");
        request.setAttribute("numExpediente", request.getParameter("numExp"));
        return url;
    }

    public void darAltaTrabajador(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        TrabajadorVO trabajador = new TrabajadorVO();
        String nombreTabla = null;
        String xmlSalida = null;
        AdaptadorSQLBD adapt = null;
        ArrayList<PersonaVO> lista = null;

        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            if (adapt == null) {
                codigoOperacion = "1";
                throw new MeLanbide65Exception(1, "Problemas para obtener una conexión con la BBDD");
            }
            nombreTabla = ConstantesMeLanbide65.getNOMBRE_TABLA_TRABAJADOR();

            //Recojo los parametros
            String nombre = request.getParameter("nombre");
            String apellido1 = request.getParameter("apellido1");
            String apellido2 = request.getParameter("apellido2");
            String sexo = request.getParameter("sexo");
            String dni = request.getParameter("dni");
            String tipoContrato = request.getParameter("tipoContrato");
            String tipoJornada = request.getParameter("tipoJornada");
            String porcJornadaParcial = request.getParameter("porcJornadaParcial");
            String discPsiquica = request.getParameter("discPsiquica");
            String discFisica = request.getParameter("discFisica");
            String discSensorial = request.getParameter("discSensorial");
            String numExp = (String)request.getParameter("expediente");
            String pensionista =  (String)request.getParameter("pensionista");
            String tipoPensionista = (String)request.getParameter("tipoPensionista");


            // Asigno los valores de la request a las propiedades del objeto TrabajadorVO
            //trabajador.setNumExp(numExp);
            trabajador.setNombre(nombre);
            trabajador.setApellido1(apellido1);
            trabajador.setApellido2(apellido2);
            trabajador.setSexo(Integer.parseInt(sexo));
            trabajador.setDni(dni);
            trabajador.setTipoContrato(tipoContrato);
            trabajador.setTipoJornada(tipoJornada);
            trabajador.setJorParcPorc("".equals(porcJornadaParcial) ? -1.0 : Double.parseDouble(porcJornadaParcial.replace(",", ".")));
            trabajador.setTipoDiscPsiquica("".equals(discPsiquica) ? -1 : Integer.parseInt(discPsiquica));
            trabajador.setTipoDiscFisica("".equals(discFisica) ? -1 : Integer.parseInt(discFisica));
            trabajador.setTipoDiscSensorial("".equals(discSensorial) ? -1 : Integer.parseInt(discSensorial));

            trabajador.setPensionista(pensionista);
            trabajador.setTipoPensionista(tipoPensionista);


            MeLanbide65Manager meLanbide65Manager = MeLanbide65Manager.getInstance();
            boolean insertOK = meLanbide65Manager.insertarDatos(trabajador, nombreTabla, numExp, adapt);

            if (insertOK) {
                log.debug("Registro Insertado Correctamente");
                //Convertimos cada objeto PersonaVO en lo que realmente son, objetos de TrabajadorVO
                lista = meLanbide65Manager.obtenerDatos(nombreTabla, numExp, adapt, null);

            } else {
                log.debug("NO se ha insertado correctamente el nuevo registro");
                codigoOperacion = "-1";
            }
        } catch (MeLanbide65Exception ex) {
            log.error("Error: " + ex.getError());
            codigoOperacion = String.valueOf(ex.getCodigo());
        } catch (NumberFormatException ex) {
            log.error("Error al parsear los parametros de entrada. " + ex.getMessage());
            codigoOperacion = "2";
        } catch (SQLException ex) {
            codigoOperacion = "1";
            log.error("Error SQL: " + ex.getMessage());
        } finally {
            xmlSalida = obtenerXmlSalida(request, codigoOperacion, lista);
            retornarXML(xmlSalida, response);
        }
    }

    public String cargarFormularioModTr(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String url = "/jsp/extension/melanbide65/formularioAltaModTr.jsp";
        String nombreTabla = "";

        TrabajadorVO trabajador = null;

        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            nombreTabla = ConstantesMeLanbide65.getNOMBRE_TABLA_TRABAJADOR();
            String id = request.getParameter("id");
            trabajador = (TrabajadorVO) MeLanbide65Manager.getInstance().obtenerDatosPorId(nombreTabla, Integer.parseInt(id), adapt);
            request.setAttribute("numExpediente", request.getParameter("numExp"));
            request.setAttribute("datosRegistro", trabajador);
            request.setAttribute("identificador", id);

            //Cargamos el request los valores  de los desplegables
            List<DesplegableAdmonLocalVO> listaContratos = MeLanbide65Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConstantesMeLanbide65.getNOMBRE_LISTA_CONTRATO(), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            List<DesplegableAdmonLocalVO> listaJornada = MeLanbide65Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConstantesMeLanbide65.getNOMBRE_LISTA_JORNADA(), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            List<DesplegableAdmonLocalVO> listaSexo = MeLanbide65Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConstantesMeLanbide65.getNOMBRE_LISTA_SEXO(), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            List<DesplegableAdmonLocalVO> listaPensionista = MeLanbide65Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConstantesMeLanbide65.getNOMBRE_LISTA_PENSIONISTA(), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));


            for(DesplegableAdmonLocalVO lista : listaContratos){
                lista.setDes_nom(getDescripcionDesplegable(request,lista.getDes_nom()));
            }

            for(DesplegableAdmonLocalVO lista : listaJornada){
                lista.setDes_nom(getDescripcionDesplegable(request,lista.getDes_nom()));
            }
            for(DesplegableAdmonLocalVO lista : listaSexo){
                lista.setDes_nom(getDescripcionDesplegable(request,lista.getDes_nom()));
            }

            for(DesplegableAdmonLocalVO lista : listaPensionista){
                lista.setDes_nom(getDescripcionDesplegable(request,lista.getDes_nom()));
            }


            if (!listaPensionista.isEmpty()) {
                request.setAttribute("listaPensionista", listaPensionista);
            }
            List<DesplegableAdmonLocalVO> listaTipoPensionista = MeLanbide65Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod("BOOL", this.getAdaptSQLBD(String.valueOf(codOrganizacion)));

            for(DesplegableAdmonLocalVO lista : listaTipoPensionista){
                lista.setDes_nom(getDescripcionDesplegable(request,lista.getDes_nom().split("\\|")[0]));
            }



            if (!listaTipoPensionista.isEmpty()) {
                request.setAttribute("listaTipoPensionista", listaTipoPensionista);
            }
            if (!listaSexo.isEmpty()) {
                request.setAttribute("listaSexo", listaSexo);
            }

            if (!listaContratos.isEmpty()) {
                request.setAttribute("listaContrato", listaContratos);
            }
            if (!listaJornada.isEmpty()) {
                request.setAttribute("listaJornada", listaJornada);
            }

        } catch (NumberFormatException ex) {
            log.error("Error al parsear los datos de entrada");
        } catch (MeLanbide65Exception ex) {
            log.error("Ha ocurrido un error al obtener los datos de un registro en " + nombreTabla);
        } catch (SQLException ex) {
            log.error("Error SQL: " + ex.getMessage());
        } catch (Exception ex) {
            log.error("Error al parsear los datos de entrada");
        }
        request.setAttribute("nuevo", "0");
        return url;
    }

    public void modificarTrabajador(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        TrabajadorVO trabajador = new TrabajadorVO();
        String nombreTabla = null;
        String xmlSalida = null;
        AdaptadorSQLBD adapt = null;
        ArrayList<PersonaVO> lista = null;

        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            if (adapt == null) {
                codigoOperacion = "1";
                throw new MeLanbide65Exception(1, "Problemas para obtener una conexión con la BBDD");
            }
            nombreTabla = ConstantesMeLanbide65.getNOMBRE_TABLA_TRABAJADOR();

            //Recojo los parametros
            String nombre = request.getParameter("nombre");
            String apellido1 = request.getParameter("apellido1");
            String apellido2 = request.getParameter("apellido2");
            String sexo = request.getParameter("sexo");
            String dni = request.getParameter("dni");
            String tipoContrato = request.getParameter("tipoContrato");
            String tipoJornada = request.getParameter("tipoJornada");
            String porcJornadaParcial = request.getParameter("porcJornadaParcial");
            String discPsiquica = request.getParameter("discPsiquica");
            String discFisica = request.getParameter("discFisica");
            String discSensorial = request.getParameter("discSensorial");
            String numExp = request.getParameter("expediente");
            String identificador = request.getParameter("identificador");

            String pensionista =  (String)request.getParameter("pensionista");
            String tipoPensionista = (String)request.getParameter("tipoPensionista");


            // Asigno los valores de la request a las propiedades del objeto TrabajadorVO
            trabajador.setNombre(nombre);
            trabajador.setApellido1(apellido1);
            trabajador.setApellido2(apellido2);
            trabajador.setSexo(Integer.parseInt(sexo));
            trabajador.setDni(dni);
            trabajador.setTipoContrato(tipoContrato);
            trabajador.setTipoJornada(tipoJornada);
            trabajador.setJorParcPorc("".equals(porcJornadaParcial) ? -1.0 : Double.parseDouble(porcJornadaParcial.replace(",", ".")));
            trabajador.setTipoDiscPsiquica("".equals(discPsiquica) ? -1 : Integer.parseInt(discPsiquica));
            trabajador.setTipoDiscPsiquica("".equals(discPsiquica) ? -1 : Integer.parseInt(discPsiquica));
            trabajador.setTipoDiscFisica("".equals(discFisica) ? -1 : Integer.parseInt(discFisica));
            trabajador.setTipoDiscSensorial("".equals(discSensorial) ? -1 : Integer.parseInt(discSensorial));
            trabajador.setPensionista(pensionista);
            trabajador.setTipoPensionista(tipoPensionista);

            MeLanbide65Manager meLanbide65Manager = MeLanbide65Manager.getInstance();
            boolean updateOK = meLanbide65Manager.modificarDatos(trabajador, nombreTabla, Integer.parseInt(identificador), numExp, adapt);
            if (updateOK) {
                log.debug("Registro Modificado Correctamente");
                //Convertimos cada objeto PersonaVO en lo que realmente son, objetos de TrabajadorVO
                lista = meLanbide65Manager.obtenerDatos(nombreTabla, numExp, adapt, null);

            } else {
                log.debug("NO se han modificado los datos del registro correctamente");
                codigoOperacion = "-1";
            }
        } catch (MeLanbide65Exception ex) {
            log.error("Error: " + ex.getError());
            codigoOperacion = String.valueOf(ex.getCodigo());
        } catch (NumberFormatException ex) {
            log.error("Error al parsear los parametros de entrada. " + ex.getMessage());
            codigoOperacion = "2";
        } catch (SQLException ex) {
            codigoOperacion = "1";
            log.error("Error SQL: " + ex.getMessage());
        } finally {
            xmlSalida = obtenerXmlSalida(request, codigoOperacion, lista);
            retornarXML(xmlSalida, response);
        }
    }

    public void eliminarTrabajador(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        String nombreTabla = null;
        String xmlSalida = null;
        AdaptadorSQLBD adapt = null;
        ArrayList<PersonaVO> lista = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            if (adapt == null) {
                codigoOperacion = "1";
                throw new MeLanbide65Exception(1, "Problemas para obtener una conexión con la BBDD");
            }
            nombreTabla = ConstantesMeLanbide65.getNOMBRE_TABLA_TRABAJADOR();

            //Recojo los parametros
            String numExp = request.getParameter("expediente");
            String identificador = request.getParameter("identificador");

            MeLanbide65Manager meLanbide65Manager = MeLanbide65Manager.getInstance();
            boolean deleteOK = meLanbide65Manager.eliminarFila(Integer.parseInt(identificador), nombreTabla, adapt);
            if (deleteOK) {
                log.debug("Registro Eliminado Correctamente");
                //Convertimos cada objeto PersonaVO en lo que realmente son, objetos de TrabajadorVO
                lista = meLanbide65Manager.obtenerDatos(nombreTabla, numExp, adapt, null);

            } else {
                log.debug("NO se ha podido eliminar la fila");
                codigoOperacion = "-1";
            }
        } catch (MeLanbide65Exception ex) {
            log.error("Error: " + ex.getError());
            codigoOperacion = String.valueOf(ex.getCodigo());
        } catch (NumberFormatException ex) {
            log.error("Error al parsear los parametros de entrada. " + ex.getMessage());
            codigoOperacion = "2";
        } catch (SQLException ex) {
            codigoOperacion = "1";
            log.error("Error SQL: " + ex.getMessage());
        } finally {
            xmlSalida = obtenerXmlSalida(request, codigoOperacion, lista);
            retornarXML(xmlSalida, response);
        }
    }

    public String cargarFormularioAltaTec(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String url = "/jsp/extension/melanbide65/formularioAltaModTec.jsp";
        MeLanbide65Manager meLanbide65Manager = MeLanbide65Manager.getInstance();
        try {
            //Cargamos el request los valores  de los desplegables
            List<DesplegableAdmonLocalVO> listaContratos = MeLanbide65Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConstantesMeLanbide65.getNOMBRE_LISTA_CONTRATO_B(), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            List<DesplegableAdmonLocalVO> listaJornada = MeLanbide65Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConstantesMeLanbide65.getNOMBRE_LISTA_JORNADA(), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));

            List<DesplegableAdmonLocalVO> listaPensionista = MeLanbide65Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConstantesMeLanbide65.getNOMBRE_LISTA_PENSIONISTA(), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));

            for(DesplegableAdmonLocalVO lista : listaPensionista){
                lista.setDes_nom(getDescripcionDesplegable(request,lista.getDes_nom()));
            }

            if (!listaPensionista.isEmpty()) {
                request.setAttribute("listaPensionista", listaPensionista);
            }
            List<DesplegableAdmonLocalVO> listaTipoPensionista = MeLanbide65Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod("BOOL", this.getAdaptSQLBD(String.valueOf(codOrganizacion)));

            for(DesplegableAdmonLocalVO lista : listaTipoPensionista){
                lista.setDes_nom(getDescripcionDesplegable(request,lista.getDes_nom().split("\\|")[0]));
            }



            if (!listaTipoPensionista.isEmpty()) {
                request.setAttribute("listaTipoPensionista", listaTipoPensionista);
            }
            for(DesplegableAdmonLocalVO lista : listaContratos){
                lista.setDes_nom(getDescripcionDesplegable(request,lista.getDes_nom()));
            }

            for(DesplegableAdmonLocalVO lista : listaJornada){
                lista.setDes_nom(getDescripcionDesplegable(request,lista.getDes_nom()));
            }


            if (!listaContratos.isEmpty()) {
                request.setAttribute("listaContrato", listaContratos);
            }
            if (!listaJornada.isEmpty()) {
                request.setAttribute("listaJornada", listaJornada);
            }
        } catch (Exception ex) {
            log.debug("Se ha presentado un error al intentar preparar la jsp de un nuevo trabajador : " + ex.getMessage());
        }
        request.setAttribute("nuevo", "1");

        request.setAttribute("numExpediente", request.getParameter("numExp"));
        return url;
    }

    public void darAltaTecnico(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        TecnicoVO tecnico = new TecnicoVO();
        String nombreTabla = null;
        String xmlSalida = null;
        AdaptadorSQLBD adapt = null;
        ArrayList<PersonaVO> lista = null;

        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            if (adapt == null) {
                codigoOperacion = "1";
                throw new MeLanbide65Exception(1, "Problemas para obtener una conexión con la BBDD");
            }
            nombreTabla = ConstantesMeLanbide65.getNOMBRE_TABLA_TECNICO();

            //Recojo los parametros de la jsp
            String nombre = request.getParameter("nombre");
            String apellido1 = request.getParameter("apellido1");
            String apellido2 = request.getParameter("apellido2");
            String dni = request.getParameter("dni");
            String fechaAltaContratoIndefinido = request.getParameter("fechaAltaContratoIndefinido");
            String fechaAltaContratoTemporal = request.getParameter("fechaAltaContratoTemporal");
            String fechaBajaContratoTemporal = request.getParameter("fechaBajaContratoTemporal");
            String jornadaParcialPor = request.getParameter("jornadaParcialPor");
            String numExp = (String) request.getParameter("expediente");
            String tipoContrato = request.getParameter("tipoContrato");
            String tipoJornada = request.getParameter("tipoJornada");

            String pensionista =  (String)request.getParameter("pensionista");
            String tipoPensionista = (String)request.getParameter("tipoPensionista");

            // Asigno los valores de la request a las propiedades del objeto TecnicoVO
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
            tecnico.setNombre(nombre);
            tecnico.setApellido1(apellido1);
            tecnico.setApellido2(apellido2);
            tecnico.setDni(dni);
            tecnico.setFecAltaContrIndef("".equals(fechaAltaContratoIndefinido) ? null : formato.parse(fechaAltaContratoIndefinido));
            tecnico.setFecAltaContrTemp("".equals(fechaAltaContratoTemporal) ? null : formato.parse(fechaAltaContratoTemporal));
            tecnico.setFecBajaContrTemp("".equals(fechaBajaContratoTemporal) ? null : formato.parse(fechaBajaContratoTemporal));
            tecnico.setJornadaParcialPor("".equals(jornadaParcialPor) ? -1.0 : Double.parseDouble(jornadaParcialPor.replace(",", ".")));
            tecnico.setTipoContrato(tipoContrato);
            tecnico.setTipoJornada(tipoJornada);

            tecnico.setPensionista(pensionista);
            tecnico.setTipoPensionista(tipoPensionista);


            MeLanbide65Manager meLanbide65Manager = MeLanbide65Manager.getInstance();
            boolean insertOK = meLanbide65Manager.insertarDatos(tecnico, nombreTabla, numExp, adapt);
            if (insertOK) {
                log.debug("Registro Insertado Correctamente");
                //Convertimos cada objeto PersonaVO en lo que realmente son, objetos de TrabajadorVO
                lista = meLanbide65Manager.obtenerDatos(nombreTabla, numExp, adapt, null);

            } else {
                log.debug("NO se ha insertado correctamente el nuevo registro");
                codigoOperacion = "-1";
            }
        } catch (MeLanbide65Exception ex) {
            log.error("Error: " + ex.getError());
            codigoOperacion = String.valueOf(ex.getCodigo());
        } catch (ParseException ex) {
            log.error("Error al parsear los parametros de entrada. " + ex.getMessage());
            codigoOperacion = "2";
        } catch (NumberFormatException ex) {
            log.error("Error al parsear los parametros de entrada. " + ex.getMessage());
            codigoOperacion = "2";
        } catch (SQLException ex) {
            codigoOperacion = "1";
            log.error("Error SQL: " + ex.getMessage());
        } finally {
            xmlSalida = obtenerXmlSalida(request, codigoOperacion, lista);
            retornarXML(xmlSalida, response);
        }
    }

    public String cargarFormularioModTec(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String url = "/jsp/extension/melanbide65/formularioAltaModTec.jsp";
        String nombreTabla = "";

        EncargadoVO tecnico = null;

        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            nombreTabla = ConstantesMeLanbide65.getNOMBRE_TABLA_TECNICO();
            String id = request.getParameter("id");
            tecnico = (EncargadoVO) MeLanbide65Manager.getInstance().obtenerDatosPorId(nombreTabla, Integer.parseInt(id), adapt);
            request.setAttribute("numExpediente", request.getParameter("numExp"));
            request.setAttribute("datosTecnico", tecnico);
            request.setAttribute("identificador", id);

            //Cargamos el request los valores  de los desplegables
            List<DesplegableAdmonLocalVO> listaContratos = MeLanbide65Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConstantesMeLanbide65.getNOMBRE_LISTA_CONTRATO_B(), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            List<DesplegableAdmonLocalVO> listaJornada = MeLanbide65Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConstantesMeLanbide65.getNOMBRE_LISTA_JORNADA(), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));

            List<DesplegableAdmonLocalVO> listaPensionista = MeLanbide65Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConstantesMeLanbide65.getNOMBRE_LISTA_PENSIONISTA(), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));


            for(DesplegableAdmonLocalVO lista : listaPensionista){
                lista.setDes_nom(getDescripcionDesplegable(request,lista.getDes_nom()));
            }



            if (!listaPensionista.isEmpty()) {
                request.setAttribute("listaPensionista", listaPensionista);
            }


            List<DesplegableAdmonLocalVO> listaTipoPensionista = MeLanbide65Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod("BOOL", this.getAdaptSQLBD(String.valueOf(codOrganizacion)));

            for(DesplegableAdmonLocalVO lista : listaTipoPensionista){
                lista.setDes_nom(getDescripcionDesplegable(request,lista.getDes_nom().split("\\|")[0]));
            }



            if (!listaTipoPensionista.isEmpty()) {
                request.setAttribute("listaTipoPensionista", listaTipoPensionista);
            }

            for(DesplegableAdmonLocalVO lista : listaContratos){
                lista.setDes_nom(getDescripcionDesplegable(request,lista.getDes_nom()));
            }

            for(DesplegableAdmonLocalVO lista : listaJornada){
                lista.setDes_nom(getDescripcionDesplegable(request,lista.getDes_nom()));
            }


            if (!listaContratos.isEmpty()) {
                request.setAttribute("listaContrato", listaContratos);
            }
            if (!listaJornada.isEmpty()) {
                request.setAttribute("listaJornada", listaJornada);
            }

        } catch (NumberFormatException ex) {
            log.error("Error al parsear los datos de entrada");
        } catch (MeLanbide65Exception ex) {
            log.error("Ha ocurrido un error al obtener los datos de un registro en " + nombreTabla);
        } catch (SQLException ex) {
            log.error("Error SQL: " + ex.getMessage());
        } catch (Exception ex) {
            log.error("Error al parsear los datos de entrada");
        }
        request.setAttribute("nuevo", "0");
        return url;
    }

    public void modificarTecnico(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        EncargadoVO tecnico = new EncargadoVO();
        String nombreTabla = null;
        String xmlSalida = null;
        AdaptadorSQLBD adapt = null;
        ArrayList<PersonaVO> lista = null;

        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            if (adapt == null) {
                codigoOperacion = "1";
                throw new MeLanbide65Exception(1, "Problemas para obtener una conexión con la BBDD");
            }
            nombreTabla = ConstantesMeLanbide65.getNOMBRE_TABLA_TECNICO();

            //Recojo los parametros de la jsp
            String nombre = request.getParameter("nombre");
            String apellido1 = request.getParameter("apellido1");
            String apellido2 = request.getParameter("apellido2");
            String dni = request.getParameter("dni");
            String fechaAltaContratoIndefinido = request.getParameter("fechaAltaContratoIndefinido");
            String fechaAltaContratoTemporal = request.getParameter("fechaAltaContratoTemporal");
            String fechaBajaContratoTemporal = request.getParameter("fechaBajaContratoTemporal");
            String jornadaParcialPor = request.getParameter("jornadaParcialPor");
            String numExp = request.getParameter("expediente");
            String identificador = request.getParameter("identificador");
            String tipoContrato = request.getParameter("tipoContrato");
            String tipoJornada = request.getParameter("tipoJornada");

            String pensionista =  (String)request.getParameter("pensionista");
            String tipoPensionista = (String)request.getParameter("tipoPensionista");

            // Asigno los valores de la request a las propiedades del objeto
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyy");
            tecnico.setNombre(nombre);
            tecnico.setApellido1(apellido1);
            tecnico.setApellido2(apellido2);
            tecnico.setDni(dni);
            tecnico.setTipoContrato(tipoContrato);
            tecnico.setTipoJornada(tipoJornada);
            tecnico.setFecAltaContrIndef("".equals(fechaAltaContratoIndefinido) ? null : formato.parse(fechaAltaContratoIndefinido));
            tecnico.setFecAltaContrTemp("".equals(fechaAltaContratoTemporal) ? null : formato.parse(fechaAltaContratoTemporal));
            tecnico.setFecBajaContrTemp("".equals(fechaBajaContratoTemporal) ? null : formato.parse(fechaBajaContratoTemporal));

            tecnico.setJornadaParcialPor("".equals(jornadaParcialPor) ? -1.0 : Double.parseDouble(jornadaParcialPor.replace(",", ".")));
            tecnico.setPensionista(pensionista);
            tecnico.setTipoPensionista(tipoPensionista);


            MeLanbide65Manager meLanbide65Manager = MeLanbide65Manager.getInstance();
            boolean updateOK = meLanbide65Manager.modificarDatos(tecnico, nombreTabla, Integer.parseInt(identificador), numExp, adapt);
            if (updateOK) {
                log.debug("Registro Modificado Correctamente");
                //Convertimos cada objeto PersonaVO en lo que realmente son, objetos de TrabajadorVO
                lista = meLanbide65Manager.obtenerDatos(nombreTabla, numExp, adapt, null);

            } else {
                log.debug("NO se han modificado los datos del registro correctamente");
                codigoOperacion = "-1";
            }
        } catch (MeLanbide65Exception ex) {
            log.error("Error: " + ex.getError());
            codigoOperacion = String.valueOf(ex.getCodigo());
        } catch (NumberFormatException ex) {
            log.error("Error al parsear los parametros de entrad. " + ex.getMessage());
            codigoOperacion = "2";
        } catch (ParseException ex) {
            log.error("Error al parsear los parametros de entrad. " + ex.getMessage());
            codigoOperacion = "2";
        } catch (SQLException ex) {
            log.error("Error SQL: " + ex.getMessage());
            codigoOperacion = "1";
        } finally {
            xmlSalida = obtenerXmlSalida(request, codigoOperacion, lista);
            retornarXML(xmlSalida, response);
        }

    }

    public void eliminarTecnico(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        String nombreTabla = null;
        String xmlSalida = null;
        AdaptadorSQLBD adapt = null;
        ArrayList<PersonaVO> lista = null;

        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            if (adapt == null) {
                codigoOperacion = "1";
                throw new MeLanbide65Exception(1, "Problemas para obtener una conexión con la BBDD");
            }
            nombreTabla = ConstantesMeLanbide65.getNOMBRE_TABLA_TECNICO();

            //Recojo los parametros de la jsp
            String numExp = request.getParameter("expediente");
            String identificador = request.getParameter("identificador");

            MeLanbide65Manager meLanbide65Manager = MeLanbide65Manager.getInstance();
            boolean deleteOK = meLanbide65Manager.eliminarFila(Integer.parseInt(identificador), nombreTabla, adapt);
            if (deleteOK) {
                log.debug("Registro Eliminado Correctamente");
                //Convertimos cada objeto PersonaVO en lo que realmente son, objetos de TrabajadorVO
                lista = meLanbide65Manager.obtenerDatos(nombreTabla, numExp, adapt, null);

            } else {
                log.debug("NO se ha podido eliminar la fila");
                codigoOperacion = "-1";
            }
        } catch (MeLanbide65Exception ex) {
            log.error("Error: " + ex.getError());
            codigoOperacion = String.valueOf(ex.getCodigo());
        } catch (NumberFormatException ex) {
            log.error("Error al parsear los parametros de entrada. " + ex.getMessage());
            codigoOperacion = "2";
        } catch (SQLException ex) {
            log.error("Error SQL: " + ex.getMessage());
            codigoOperacion = "1";
        } finally {
            xmlSalida = obtenerXmlSalida(request, codigoOperacion, lista);
            retornarXML(xmlSalida, response);
        }
    }

    public String cargarFormularioAltaEnc(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String url = "/jsp/extension/melanbide65/formularioAltaModEnc.jsp";
        request.setAttribute("numExpediente", request.getParameter("numExp"));
        try {
            //Cargamos el request los valores  de los desplegables
            List<DesplegableAdmonLocalVO> listaContratos = MeLanbide65Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConstantesMeLanbide65.getNOMBRE_LISTA_CONTRATO_B(), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            List<DesplegableAdmonLocalVO> listaJornada = MeLanbide65Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConstantesMeLanbide65.getNOMBRE_LISTA_JORNADA(), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));

            List<DesplegableAdmonLocalVO> listaPensionista = MeLanbide65Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConstantesMeLanbide65.getNOMBRE_LISTA_PENSIONISTA(), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));

            for(DesplegableAdmonLocalVO lista : listaPensionista){
                lista.setDes_nom(getDescripcionDesplegable(request,lista.getDes_nom()));
            }

            if (!listaPensionista.isEmpty()) {
                request.setAttribute("listaPensionista", listaPensionista);
            }

            List<DesplegableAdmonLocalVO> listaTipoPensionista = MeLanbide65Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod("BOOL", this.getAdaptSQLBD(String.valueOf(codOrganizacion)));

            for(DesplegableAdmonLocalVO lista : listaTipoPensionista){
                lista.setDes_nom(getDescripcionDesplegable(request,lista.getDes_nom().split("\\|")[0]));
            }



            if (!listaTipoPensionista.isEmpty()) {
                request.setAttribute("listaTipoPensionista", listaTipoPensionista);
            }

            for(DesplegableAdmonLocalVO lista : listaContratos){
                lista.setDes_nom(getDescripcionDesplegable(request,lista.getDes_nom()));
            }

            for(DesplegableAdmonLocalVO lista : listaJornada){
                lista.setDes_nom(getDescripcionDesplegable(request,lista.getDes_nom()));
            }


            if (!listaContratos.isEmpty()) {
                request.setAttribute("listaContrato", listaContratos);
            }
            if (!listaJornada.isEmpty()) {
                request.setAttribute("listaJornada", listaJornada);
            }
        } catch (Exception ex) {
            log.debug("Se ha presentado un error al intentar preparar la jsp de un nuevo trabajador : " + ex.getMessage());
        }
        request.setAttribute("nuevo", "1");
        return url;
    }

    public void darAltaEncargado(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        EncargadoVO encargado = new EncargadoVO();
        String nombreTabla = null;
        String xmlSalida = null;
        AdaptadorSQLBD adapt = null;
        ArrayList<PersonaVO> lista = null;

        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            if (adapt == null) {
                codigoOperacion = "1";
                throw new MeLanbide65Exception(1, "Problemas para obtener una conexión con la BBDD");
            }
            nombreTabla = ConstantesMeLanbide65.getNOMBRE_TABLA_ENCARGADO();

            //Recojo los parametros de la jsp
            String nombre = request.getParameter("nombre");
            String apellido1 = request.getParameter("apellido1");
            String apellido2 = request.getParameter("apellido2");
            String dni = request.getParameter("dni");
            String fechaAltaContratoIndefinido = request.getParameter("fechaAltaContratoIndefinido");
            String fechaAltaContratoTemporal = request.getParameter("fechaAltaContratoTemporal");
            String fechaBajaContratoTemporal = request.getParameter("fechaBajaContratoTemporal");
            String jornadaParcialPor = request.getParameter("jornadaParcialPor");
            String numExp = (String) request.getParameter("expediente");
            String tipoContrato = request.getParameter("tipoContrato");
            String tipoJornada = request.getParameter("tipoJornada");


            String pensionista =  (String)request.getParameter("pensionista");
            String tipoPensionista = (String)request.getParameter("tipoPensionista");

            // Asigno los valores de la request a las propiedades del objeto TecnicoVO
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
            encargado.setNombre(nombre);
            encargado.setApellido1(apellido1);
            encargado.setApellido2(apellido2);
            encargado.setDni(dni);
            encargado.setFecAltaContrIndef("".equals(fechaAltaContratoIndefinido) ? null : formato.parse(fechaAltaContratoIndefinido));
            encargado.setFecAltaContrTemp("".equals(fechaAltaContratoTemporal) ? null : formato.parse(fechaAltaContratoTemporal));
            encargado.setFecBajaContrTemp("".equals(fechaBajaContratoTemporal) ? null : formato.parse(fechaBajaContratoTemporal));
            encargado.setJornadaParcialPor("".equals(jornadaParcialPor) ? -1.0 : Double.parseDouble(jornadaParcialPor.replace(",", ".")));
            encargado.setTipoContrato(tipoContrato);
            encargado.setTipoJornada(tipoJornada);
            encargado.setPensionista(pensionista);
            encargado.setTipoPensionista(tipoPensionista);

            MeLanbide65Manager meLanbide65Manager = MeLanbide65Manager.getInstance();
            boolean insertOK = meLanbide65Manager.insertarDatos(encargado, nombreTabla, numExp, adapt);
            if (insertOK) {
                log.debug("Registro Insertado Correctamente");
                //Convertimos cada objeto PersonaVO en lo que realmente son, objetos de TrabajadorVO
                lista = meLanbide65Manager.obtenerDatos(nombreTabla, numExp, adapt, null);

            } else {
                log.debug("NO se ha insertado correctamente el nuevo registro");
                codigoOperacion = "-1";
            }
        } catch (MeLanbide65Exception ex) {
            log.error("Error: " + ex.getError());
            codigoOperacion = String.valueOf(ex.getCodigo());
        } catch (ParseException ex) {
            log.error("Error al parsear los parametros de entrada. " + ex.getMessage());
            codigoOperacion = "2";
        } catch (NumberFormatException ex) {
            log.error("Error al parsear los parametros de entrada. " + ex.getMessage());
            codigoOperacion = "2";
        } catch (SQLException ex) {
            log.error("Error SQL: " + ex.getMessage());
            codigoOperacion = "1";
        } finally {
            xmlSalida = obtenerXmlSalida(request, codigoOperacion, lista);
            retornarXML(xmlSalida, response);
        }
    }

    public String cargarFormularioModEnc(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String url = "/jsp/extension/melanbide65/formularioAltaModEnc.jsp";
        String nombreTabla = "";

        EncargadoVO encargado = null;

        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            nombreTabla = ConstantesMeLanbide65.getNOMBRE_TABLA_ENCARGADO();
            String id = request.getParameter("id");
            encargado = (EncargadoVO) MeLanbide65Manager.getInstance().obtenerDatosPorId(nombreTabla, Integer.parseInt(id), adapt);
            request.setAttribute("numExpediente", request.getParameter("numExp"));
            request.setAttribute("datosEncargado", encargado);
            request.setAttribute("identificador", id);

            //Cargamos el request los valores  de los desplegables
            List<DesplegableAdmonLocalVO> listaContratos = MeLanbide65Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConstantesMeLanbide65.getNOMBRE_LISTA_CONTRATO_B(), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            List<DesplegableAdmonLocalVO> listaJornada = MeLanbide65Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConstantesMeLanbide65.getNOMBRE_LISTA_JORNADA(), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));


            for(DesplegableAdmonLocalVO lista : listaContratos){
                lista.setDes_nom(getDescripcionDesplegable(request,lista.getDes_nom()));
            }

            for(DesplegableAdmonLocalVO lista : listaJornada){
                lista.setDes_nom(getDescripcionDesplegable(request,lista.getDes_nom()));
            }
            List<DesplegableAdmonLocalVO> listaPensionista = MeLanbide65Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConstantesMeLanbide65.getNOMBRE_LISTA_PENSIONISTA(), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));

            for(DesplegableAdmonLocalVO lista : listaPensionista){
                lista.setDes_nom(getDescripcionDesplegable(request,lista.getDes_nom()));
            }


            if (!listaPensionista.isEmpty()) {
                request.setAttribute("listaPensionista", listaPensionista);


            }
            List<DesplegableAdmonLocalVO> listaTipoPensionista = MeLanbide65Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod("BOOL", this.getAdaptSQLBD(String.valueOf(codOrganizacion)));

            for(DesplegableAdmonLocalVO lista : listaTipoPensionista){
                lista.setDes_nom(getDescripcionDesplegable(request,lista.getDes_nom().split("\\|")[0]));
            }



            if (!listaTipoPensionista.isEmpty()) {
                request.setAttribute("listaTipoPensionista", listaTipoPensionista);
            }
            if (!listaContratos.isEmpty()) {
                request.setAttribute("listaContrato", listaContratos);
            }
            if (!listaJornada.isEmpty()) {
                request.setAttribute("listaJornada", listaJornada);
            }

        } catch (NumberFormatException ex) {
            log.error("Error al parsear los datos de entrada");
        } catch (MeLanbide65Exception ex) {
            log.error("Ha ocurrido un error al obtener los datos de un registro en " + nombreTabla);
        } catch (SQLException ex) {
            log.error("Error SQL: " + ex.getMessage());
        } catch (Exception ex) {
            log.error("Error al parsear los datos de los desplegables");
        }
        request.setAttribute("nuevo", "0");
        return url;
    }

    public void modificarEncargado(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        EncargadoVO encargado = new EncargadoVO();
        String nombreTabla = null;
        String xmlSalida = null;
        AdaptadorSQLBD adapt = null;
        ArrayList<PersonaVO> lista = null;

        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            if (adapt == null) {
                codigoOperacion = "1";
                throw new MeLanbide65Exception(1, "Problemas para obtener una conexión con la BBDD");
            }
            nombreTabla = ConstantesMeLanbide65.getNOMBRE_TABLA_ENCARGADO();

            //Recojo los parametros de la jsp
            String nombre = request.getParameter("nombre");
            String apellido1 = request.getParameter("apellido1");
            String apellido2 = request.getParameter("apellido2");
            String dni = request.getParameter("dni");
            String fechaAltaContratoIndefinido = request.getParameter("fechaAltaContratoIndefinido");
            String fechaAltaContratoTemporal = request.getParameter("fechaAltaContratoTemporal");
            String fechaBajaContratoTemporal = request.getParameter("fechaBajaContratoTemporal");
            String jornadaParcialPor = request.getParameter("jornadaParcialPor");
            String numExp = request.getParameter("expediente");
            String identificador = request.getParameter("identificador");
            String tipoContrato = request.getParameter("tipoContrato");
            String tipoJornada = request.getParameter("tipoJornada");

            String pensionista =  (String)request.getParameter("pensionista");
            String tipoPensionista = (String)request.getParameter("tipoPensionista");

            // Asigno los valores de la request a las propiedades del objeto
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyy");
            encargado.setNombre(nombre);
            encargado.setApellido1(apellido1);
            encargado.setApellido2(apellido2);
            encargado.setDni(dni);
            encargado.setTipoContrato(tipoContrato);
            encargado.setTipoJornada(tipoJornada);
            encargado.setFecAltaContrIndef("".equals(fechaAltaContratoIndefinido) ? null : formato.parse(fechaAltaContratoIndefinido));
            encargado.setFecAltaContrTemp("".equals(fechaAltaContratoTemporal) ? null : formato.parse(fechaAltaContratoTemporal));
            encargado.setFecBajaContrTemp("".equals(fechaBajaContratoTemporal) ? null : formato.parse(fechaBajaContratoTemporal));
            encargado.setJornadaParcialPor("".equals(jornadaParcialPor) ? -1.0 : Double.parseDouble(jornadaParcialPor.replace(",", ".")));

            encargado.setPensionista(pensionista);
            encargado.setTipoPensionista(tipoPensionista);

            MeLanbide65Manager meLanbide65Manager = MeLanbide65Manager.getInstance();
            boolean updateOK = meLanbide65Manager.modificarDatos(encargado, nombreTabla, Integer.parseInt(identificador), numExp, adapt);
            if (updateOK) {
                log.debug("Registro Modificado Correctamente");
                //Convertimos cada objeto PersonaVO en lo que realmente son, objetos de TrabajadorVO
                lista = meLanbide65Manager.obtenerDatos(nombreTabla, numExp, adapt, null);

            } else {
                log.debug("NO se han modificado los datos del registro correctamente");
                codigoOperacion = "-1";
            }
        } catch (MeLanbide65Exception ex) {
            log.error("Error: " + ex.getError());
            codigoOperacion = String.valueOf(ex.getCodigo());
        } catch (NumberFormatException ex) {
            log.error("Error al parsear los parametros de entrad. " + ex.getMessage());
            codigoOperacion = "2";
        } catch (ParseException ex) {
            log.error("Error al parsear los parametros de entrad. " + ex.getMessage());
            codigoOperacion = "2";
        } catch (SQLException ex) {
            log.error("Error SQL: " + ex.getMessage());
            codigoOperacion = "1";
        } finally {
            xmlSalida = obtenerXmlSalida(request, codigoOperacion, lista);
            retornarXML(xmlSalida, response);
        }

    }

    public void eliminarEncargado(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        String nombreTabla = null;
        String xmlSalida = null;
        AdaptadorSQLBD adapt = null;
        ArrayList<PersonaVO> lista = null;

        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            if (adapt == null) {
                codigoOperacion = "1";
                throw new MeLanbide65Exception(1, "Problemas para obtener una conexión con la BBDD");
            }
            nombreTabla = ConstantesMeLanbide65.getNOMBRE_TABLA_ENCARGADO();

            //Recojo los parametros de la jsp
            String numExp = request.getParameter("expediente");
            String identificador = request.getParameter("identificador");

            MeLanbide65Manager meLanbide65Manager = MeLanbide65Manager.getInstance();
            boolean deleteOK = meLanbide65Manager.eliminarFila(Integer.parseInt(identificador), nombreTabla, adapt);
            if (deleteOK) {
                log.debug("Registro Eliminado Correctamente");
                //Convertimos cada objeto PersonaVO en lo que realmente son, objetos de TrabajadorVO
                lista = meLanbide65Manager.obtenerDatos(nombreTabla, numExp, adapt, null);

            } else {
                log.debug("NO se ha podido eliminar la fila");
                codigoOperacion = "-1";
            }
        } catch (MeLanbide65Exception ex) {
            log.error("Error: " + ex.getError());
            codigoOperacion = String.valueOf(ex.getCodigo());
        } catch (NumberFormatException ex) {
            log.error("Error al parsear los parametros de entrada. " + ex.getMessage());
            codigoOperacion = "2";
        } catch (SQLException ex) {
            log.error("Error SQL: " + ex.getMessage());
            codigoOperacion = "1";
        } finally {
            xmlSalida = obtenerXmlSalida(request, codigoOperacion, lista);
            retornarXML(xmlSalida, response);
        }
    }

    private String getDescripcionDesplegable(HttpServletRequest request, String descripcionCompleta) {
        String descripcion = descripcionCompleta;

        String barraSeparadoraDobleIdiomaDesple = ConfigurationParameter.getParameter(ConstantesMeLanbide65.BARRA_SEPARADORA_IDIOMA_DESPLEGABLES, ConstantesMeLanbide65.FICHERO_PROPIEDADES);

        try {
            if (!descripcion.isEmpty()) {

                String[] descripcionDobleIdioma = (descripcion != null ? descripcion.split(barraSeparadoraDobleIdiomaDesple) : null);
                if (descripcionDobleIdioma != null && descripcionDobleIdioma.length > 1) {
                    if (getIdioma(request) == ConstantesMeLanbide65.CODIGO_IDIOMA_EUSKERA) {
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

    private int getIdioma(HttpServletRequest request) {
        // Recuperamos el Idioma de la request para la gestion de Desplegables
        UsuarioValueObject usuario = new UsuarioValueObject();
        int idioma = ConstantesMeLanbide65.CODIGO_IDIOMA_CASTELLANO;  // Por Defecto 1 Castellano
        try {
            if (request != null && request.getSession() != null) {
                usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
                if (usuario != null) {
                    idioma = usuario.getIdioma();
                }
            }
        } catch (Exception ex) {
            log.error("Error al recuperar el idioma del usuario de la request, asignamos por defecto 1 Castellano", ex);
            idioma = ConstantesMeLanbide65.CODIGO_IDIOMA_CASTELLANO;
        }
        return idioma;
    }

    //Método privado para montar el xml de salida
    private String obtenerXmlSalida(HttpServletRequest request, String resultadoOperacion, ArrayList<PersonaVO> lista) {
        StringBuffer xmlSalida = null;

        xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(resultadoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        if (lista != null) {
            for (PersonaVO fila : lista) {
                if (fila.getIdentificador() > 0) {
                    xmlSalida.append("<FILA>").append(obtenerXmlSalidaParteComun(fila));

                    if (fila instanceof TrabajadorVO) {
                        TrabajadorVO trabajador = (TrabajadorVO) fila;
                        xmlSalida.append("<DISCPSIQUICA>").append(trabajador.getTipoDiscPsiquica()).append("</DISCPSIQUICA>")
                                .append("<DISCFISICA>").append(trabajador.getTipoDiscFisica()).append("</DISCFISICA>")
                                .append("<DISCSENSORIAL>").append(trabajador.getTipoDiscSensorial()).append("</DISCSENSORIAL>")
                                .append("<SEXO>").append(getDescripcionDesplegable(request, trabajador.getDescSexo())).append("</SEXO>")
                                .append("<TIPOCONTRATO>").append(getDescripcionDesplegable(request, trabajador.getDescTipoContrato())).append("</TIPOCONTRATO>")
                                .append("<TIPOJORNADA>").append(getDescripcionDesplegable(request, trabajador.getDescTipoJornada())).append("</TIPOJORNADA>")
                                .append("<JORNADAPARCIALPOR>").append(trabajador.getJorParcPorc()).append("</JORNADAPARCIALPOR>")
                                .append("<PENSIONISTA>").append(getDescripcionDesplegable(request,trabajador.getDescPensionista())).append("</PENSIONISTA>")
                                .append("<TIPPENSIONISTA>").append(getDescripcionDesplegable(request,trabajador.getDescTipoPensionista())).append("</TIPPENSIONISTA>");

                    } else if (fila instanceof EncargadoVO) {
                        EncargadoVO encarg = (EncargadoVO) fila;
                        xmlSalida.append("<FECHAALTA>").append(encarg.getFecAltaAsStr()).append("</FECHAALTA>")
                                .append("<FECHAALTAT>").append(encarg.getFecAltaContrTempAsStr()).append("</FECHAALTAT>")
                                .append("<FECHABAJAT>").append(encarg.getFecBajaContrTempAsStr()).append("</FECHABAJAT>")
                                .append("<TIPOCONTRATO>").append(getDescripcionDesplegable(request, encarg.getDescTipoContrato())).append("</TIPOCONTRATO>")
                                .append("<TIPOJORNADA>").append(getDescripcionDesplegable(request, encarg.getDescTipoJornada())).append("</TIPOJORNADA>")
                                .append("<JORNADAPARCIALPOR>").append(encarg.getJornadaParcialPor()).append("</JORNADAPARCIALPOR>")
                                .append("<PENSIONISTA>").append(getDescripcionDesplegable(request,encarg.getDescPensionista())).append("</PENSIONISTA>")
                                .append("<TIPPENSIONISTA>").append(getDescripcionDesplegable(request,encarg.getDescTipoPensionista())).append("</TIPPENSIONISTA>");
                    }
                    xmlSalida.append("</FILA>");
                }
            }
        }
        xmlSalida.append("</RESPUESTA>");
        log.debug("xml: " + xmlSalida);
        return xmlSalida.toString();
    }

    //Obtien la parte del xml de salida común a todos los tipos de objeto PersonaVO
    private String obtenerXmlSalidaParteComun(PersonaVO persona) {
        StringBuilder salida = new StringBuilder("<ID>");
        salida.append(String.valueOf(persona.getIdentificador())).append("</ID>")
                .append("<APELLIDO1>").append(persona.getApellido1()).append("</APELLIDO1>")
                .append("<APELLIDO2>").append(persona.getApellido2()).append("</APELLIDO2>")
                .append("<NOMBRE>").append(persona.getNombre()).append("</NOMBRE>")
                .append("<DNI>").append(persona.getDni()).append("</DNI>");

        return salida.toString();
    }

    private String obtenerXmlSalidaExpedientes(HttpServletRequest request, String resultadoOperacion, List<ExpedienteUAAPVO> lista) {
        StringBuffer xmlSalida = null;
        xmlSalida = new StringBuffer();
        String nombreInteresado = "";
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(resultadoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        if (lista != null) {
            for (ExpedienteUAAPVO fila : lista) {
                xmlSalida.append("<FILA>");
                xmlSalida.append("<NUM_EXPEDIENTE>");
                xmlSalida.append(fila.getNumeroExpediente());

                xmlSalida.append("</NUM_EXPEDIENTE>");
                xmlSalida.append("<NOMBRE_APELLIDOS>");
                if (fila.getNombreInteresado() != null) {
                    nombreInteresado = fila.getNombreInteresado().replace("&", " ");
                    nombreInteresado = nombreInteresado.replace("'", " ");
                    xmlSalida.append(nombreInteresado);
                } else {
                    xmlSalida.append("");
                }
                xmlSalida.append("</NOMBRE_APELLIDOS>");
                xmlSalida.append("<CP>");
                if (fila.getCP() != null) {
                    xmlSalida.append(fila.getCP());
                } else {
                    xmlSalida.append("");
                }
                xmlSalida.append("</CP>");
                xmlSalida.append("<IMPORTE_CONINI>");
                xmlSalida.append(fila.getImporteConini().toString());
                xmlSalida.append("</IMPORTE_CONINI>");
                xmlSalida.append("<IMPTOTRECAL>");
                xmlSalida.append(fila.getImporteTotalRecal());
                xmlSalida.append("</IMPTOTRECAL>");
                xmlSalida.append("</FILA>");
            }
        }
        xmlSalida.append("</RESPUESTA>");
        return xmlSalida.toString();
    }

    // Método privado para obtener una adaptador de BBDD
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
     * Método llamado para devolver un String al cliente que ha realiza la
     * petición a alguna de las operaciones de este aquí indicadas
     *
     * @param salida: String que contiene el xml a devolver
     * @param response: Objeto de tipo HttpServletResponse a través del cual
     * se devuelve la salida al cliente que ha realizado la solicitud
     */
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
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String cargarSubvencion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        if (log.isDebugEnabled()) {
            log.info("cargarSubvencion ( codOrganizacion = " + codOrganizacion + " codTramite = " + codTramite + " ocurrenciaTramite = " + ocurrenciaTramite + " numExpediente = " + numExpediente + " ) : BEGIN");
        }
        //acceder a BD para obtener los trabajadores para calcular la subvención
        AdaptadorSQLBD adapt = null;
        String nombreTabla = ConstantesMeLanbide65.E_TNU;
        String[] num = numExpediente.split("/");
        String ejercicio = num[0];
        request.setAttribute("ejercicio", ejercicio);
        int anio = Integer.parseInt(ejercicio);
        if (anio >= 2023) {
            try {
                adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            } catch (SQLException ex) {
                log.error("Error al recuperar el adaptador getAdaptSQLBD ", ex);
            }
            if (adapt != null) {
                Connection con = null;
                try {
                    MeLanbide65Manager meLanbide65Manager = MeLanbide65Manager.getInstance();
                    con = adapt.getConnection();
                    if (!meLanbide65Manager.marcadoNoCalculo(codOrganizacion, anio, numExpediente, adapt)) {
                        //Obtengo el importe de la subvencion
                        String resultadoRecalculo = meLanbide65Manager.recalcularSubvencion(numExpediente, adapt);
                        //Inserto en BBDD el importe de la subvención
                        meLanbide65Manager.insertarSubvencion(codOrganizacion, numExpediente, Double.valueOf(resultadoRecalculo.replace(',', '.')), nombreTabla, adapt);
                    }

                } catch (MeLanbide65Exception e) {
                    log.error("Error al recuperar la subvención: " + e.getMessage());
                } catch (BDException e) {
                    log.error("BDException al recuperar la subvención: " + e.getMessage());
                } catch (NumberFormatException e) {
                    log.error("NumberFormatException al recuperar la subvención: " + e.getMessage());
                } catch (Exception ex) {
                    log.error("Error al recuperar la marca de CALCULAR: " + ex.getMessage());
                } finally {
                    try {
                        adapt.devolverConexion(con);
                    } catch (BDException e) {
                        log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
                    }
                }
            }
        }
        log.debug("cargarSubvencion() : END");
        return "/jsp/extension/melanbide65/melanbide65.jsp";
    }

/*    public void busquedaExpedientesRecalculo(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String codigoOperacion = "0";
        log.info("INICIO.busquedaFiltrandoListaExpedientes");
        List<ExpedienteUAAPVO> lista = new ArrayList<ExpedienteUAAPVO>();
        ExpUAAPCriteriosFiltroVO criterios = new ExpUAAPCriteriosFiltroVO();
        AdaptadorSQLBD adapt = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            criterios.setEjercicio(request.getParameter("ejercicio"));
            criterios.setTipoProcedimiento(request.getParameter("tipoProcedimiento"));
            criterios.setPeriodoSubvencionable(request.getParameter("periodoSubvencionable"));
            lista = MeLanbide65Manager.getInstance().busquedaExpedientesRecalculo(criterios, adapt);
        } catch (SQLException ex) {
            log.error("Error al consultar lista de expedientes - UAAP/AEXCE- Recalculo Importes -: " + ex);
            codigoOperacion = "2";
        }
         String xmlSalida = obtenerXmlSalidaExpedientes(request, codigoOperacion, lista);
            retornarXML(xmlSalida, response);
    }
*/

    public void busquedaFiltrandoListaExpedientes(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String codigoOperacion = "0";
        log.info("INICIO.busquedaFiltrandoListaExpedientes");
        List<ExpedienteUAAPVO> lista = new ArrayList<ExpedienteUAAPVO>();
        AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        try {
            //Recojo los parametros
            ExpUAAPCriteriosFiltroVO _criterioBusqueda = new ExpUAAPCriteriosFiltroVO();
            _criterioBusqueda.setEjercicio((String) request.getParameter("ejercicio"));
            _criterioBusqueda.setTipoProcedimiento((String) request.getParameter("tipoProcedimiento"));
            _criterioBusqueda.setPeriodoSubvencionable((String) request.getParameter("periodoSubvencionable"));
            lista = MeLanbide65Manager.getInstance().busquedaFiltrandoListaExpedientes(_criterioBusqueda, adapt);
        } catch (Exception ex) {

            log.error("Error al consultar lista de expedientes - UAAP/AEXCE- Recalculo Importes -: " + ex);
            codigoOperacion = "2";
            return;
        }
        StringBuffer xmlSalida = new StringBuffer();

        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        String nombreInteresado = "";
        for (ExpedienteUAAPVO fila : lista) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<NUM_EXPEDIENTE>");
            xmlSalida.append(fila.getNumeroExpediente());

            xmlSalida.append("</NUM_EXPEDIENTE>");
            xmlSalida.append("<NOMBRE_APELLIDOS>");
            if (fila.getNombreInteresado() != null) {
                nombreInteresado = fila.getNombreInteresado().replace("&", " ");
                nombreInteresado = nombreInteresado.replace("'", " ");
                xmlSalida.append(nombreInteresado);
            } else {
                xmlSalida.append("");
            }
            xmlSalida.append("</NOMBRE_APELLIDOS>");
            xmlSalida.append("<CP>");
            if (fila.getCP() != null) {
                xmlSalida.append(fila.getCP());
            } else {
                xmlSalida.append("");
            }
            xmlSalida.append("</CP>");
            xmlSalida.append("<IMPORTE_CONINI>");
            xmlSalida.append(fila.getImporteConini().toString());
            xmlSalida.append("</IMPORTE_CONINI>");
            xmlSalida.append("<IMPTOTRECAL>");
            xmlSalida.append(fila.getImporteTotalRecal());
            xmlSalida.append("</IMPTOTRECAL>");
            xmlSalida.append("</FILA>");
        }
        int NumTotalRegisConsul = 0;
        if (lista != null && lista.size() > 0) {
            NumTotalRegisConsul = lista.get(0).getNoTotalRegConsulta();
        }

        xmlSalida.append("<NUMTOTALREGISTROS>");
        xmlSalida.append(NumTotalRegisConsul);
        xmlSalida.append("</NUMTOTALREGISTROS>");
        xmlSalida.append("</RESPUESTA>");
        try {
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());

            out.flush();

            out.close();

        } catch (IOException e) {
            log.error(" Error Recuperando datos en UAAP - Recalculo importes - ", e);
        }//try-catch
    }

    public void marcarRecalculoImportes(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info("INICIO.marcarRecalculoImportes");

        String codigoOperacion = "0";
        AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        String listaExpedientesMarcados = request.getParameter("listaExpedientesMarcados");

        JSONArray listaExpedientesMarcadosArr = new JSONArray(listaExpedientesMarcados);
        String[] listaExpedientesMarcadosStr = getStringArray(listaExpedientesMarcadosArr);

        int numExpedientesMarcados = 0;
        String importeMaximo = request.getParameter("importeMaximo");

        String numExpedientesMarcadosStr = String.valueOf(numExpedientesMarcados);
        try {
            ExpUAAPCriteriosFiltroVO _criterioBusqueda = new ExpUAAPCriteriosFiltroVO();

            _criterioBusqueda.setEjercicio(request.getParameter("ejercicio"));
            _criterioBusqueda.setTipoProcedimiento(request.getParameter("tipoProcedimiento"));

            numExpedientesMarcados = MeLanbide65Manager.getInstance().marcarRecalculoImportes(_criterioBusqueda, codOrganizacion, listaExpedientesMarcadosStr, importeMaximo, adapt);
        } catch (Exception ex) {
            log.error("Error al recalcular importes - Recalculo Importes - : " + ex);
            codigoOperacion = "2";
        }

        StringBuffer xmlSalida = new StringBuffer();

        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");

        xmlSalida.append("<NUMEXPEDIENTESMARCADOS>");
        xmlSalida.append(numExpedientesMarcadosStr);
        xmlSalida.append("</NUMEXPEDIENTESMARCADOS>");
        xmlSalida.append("</RESPUESTA>");
        try {
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        } catch (IOException e) {
            log.error(" Error Recuperando datos de recalculo importes en UAAP - Recalculo Importes - ", e);
        }//try-catch
    }

    private String[] getStringArray(JSONArray jsonArray) {
        String[] stringArray = null;
        if (jsonArray != null) {
            int length = jsonArray.length();
            stringArray = new String[length];
            for (int i = 0; i < length; i++) {
                stringArray[i] = jsonArray.optString(i);
            }
        }
        return stringArray;
    }

    // Generar Excell Anexo A
    public void generarExcelAnexoA(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info(">>>> ENTRA en generarExcelAnexoA  de " + this.getClass().getSimpleName());
        String rutaArchivoSalida = null;

        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            int idioma = 1;
            MeLanbide65I18n meLanbide65I18n = MeLanbide65I18n.getInstance();
            MeLanbide65Manager manager = MeLanbide65Manager.getInstance();
            ArrayList<PersonaVO> datos = null;
            ArrayList<TrabajadorVO> trabajadores = null;
            String nombreTabla;
            String importeCopiaLanbide = "0";
            try {
                numExpediente = request.getParameter("numExp");
                //                      Connection con = null;
                try {
                    //                con = adapt.getConnection();
                    nombreTabla = ConstantesMeLanbide65.getNOMBRE_TABLA_TRABAJADOR();
                    datos = manager.obtenerDatos(nombreTabla, numExpediente, adapt, null);
                    //Convertimos cada objeto PersonaVO en lo que realmente son, objetos de TrabajadorVO
                    trabajadores = new ArrayList<TrabajadorVO>();
                    for (PersonaVO persona : datos) {
                        ((TrabajadorVO) persona).setDescSexo(getDescripcionDesplegable(request, ((TrabajadorVO) persona).getDescSexo()));
                        ((TrabajadorVO) persona).setDescTipoContrato(getDescripcionDesplegable(request, ((TrabajadorVO) persona).getDescTipoContrato()));
                        ((TrabajadorVO) persona).setDescTipoJornada(getDescripcionDesplegable(request, ((TrabajadorVO) persona).getDescTipoJornada()));
                        ((TrabajadorVO) persona).setDescPensionista(getDescripcionDesplegable(request, ((TrabajadorVO) persona).getDescPensionista()));
                        ((TrabajadorVO) persona).setDescTipoPensionista(getDescripcionDesplegable(request, ((TrabajadorVO) persona).getDescTipoPensionista()));

                        trabajadores.add((TrabajadorVO) persona);
                    }
                } catch (Exception ex) {
                    log.error("Error al recuperar datos del Anexo A");
                }
            } catch (Exception ex) {
                log.error("Error recuperando datos de Anexo A : " + ex);
            }

            FileInputStream istr = null;
            try {
                HSSFWorkbook libro = new HSSFWorkbook();

                HSSFPalette palette = libro.getCustomPalette();
                HSSFColor hssfColor = null;
                try {
                    hssfColor = palette.findColor((byte) 75, (byte) 149, (byte) 211);
                    if (hssfColor == null) {
                        hssfColor = palette.getColor(HSSFColor.ROYAL_BLUE.index);
                    }
                } catch (Exception e) {
                }

                HSSFFont negrita = libro.createFont();
                negrita.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

                HSSFFont normal = libro.createFont();
                normal.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
                normal.setFontHeight((short) 150);

                HSSFFont negritaTitulo = libro.createFont();
                negritaTitulo.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                negritaTitulo.setColor(HSSFColor.WHITE.index);
                negritaTitulo.setFontHeight((short) 200);
                int numFila = 0;
                //int numFila2 = 0;

                HSSFRow fila = null;
                HSSFCell celda = null;
                HSSFCellStyle estiloCelda = null;

                HSSFSheet hoja = libro.createSheet("Datos Anexo A - Plantilla");

                //Se establece el ancho de cada columnas
                //hoja.setColumnWidth((short)0, (short)1500);//ID
                hoja.setColumnWidth(0, 3000);///Número de línea
                hoja.setColumnWidth(1, 8000);//Nombre y Apellidos
                hoja.setColumnWidth(2, 4750);//Sexo
                hoja.setColumnWidth(3, 3000);//DNI
                hoja.setColumnWidth(4, 4000);//Pensionista
                hoja.setColumnWidth(5, 5000);//Tipo de pensionista
                hoja.setColumnWidth(6, 5000);//Tipo de contrato
                hoja.setColumnWidth(7, 3000);//Tipo Jornada
                hoja.setColumnWidth(8, 5000);//Porcentaje Jornada laboral parcial
                hoja.setColumnWidth(9, 5000);//Discapacidad Psiquica(33% o mas)
                hoja.setColumnWidth(10, 5000);//Discapacidad Fisica (33% o mas)
                hoja.setColumnWidth(11, 5000);//Discapacidad Sensorial (65% o mas)

                fila = hoja.createRow(numFila);
                //Se establece el alto de las columnas
                fila.setHeight((short) 750);

                crearEstiloInformeDatosAnexoA(libro, fila, celda, estiloCelda, idioma);

                int p = 0;
                int n = 0;

                String numExp = null;

                estiloCelda = libro.createCellStyle();
                estiloCelda.setWrapText(true);
                estiloCelda.setFont(normal);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                //estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                //Insertamos los datos, fila a fila

                for (TrabajadorVO filas : trabajadores) {
                    numFila++;
                    fila = hoja.createRow(numFila);
                    //crearDatosInformeAnexoA(libro, fila, celda, estiloCelda, idioma, causaAl, numExp, numFila, nuevo, n, listaAccesos, normal, codOrganizacion, adapt, format);

                    //COLUMNA: NUMERO DE LINEA
                    celda = fila.createCell(0);
                    celda.setCellValue(numFila);
                    celda.setCellStyle(estiloCelda);

                    //COLUMNA: NOMBRE Y APELLIDOS
                    celda = fila.createCell(1);
                    celda.setCellValue((filas.getApellido1() != null ? filas.getApellido1().toUpperCase() : "") + ", " + (filas.getApellido2() != null ? filas.getApellido2().toUpperCase() : "") + ", " + (filas.getNombre() != null ? filas.getNombre().toUpperCase() : ""));
                    celda.setCellStyle(estiloCelda);

                    //COLUMNA: SEXO
                    celda = fila.createCell(2);
                    celda.setCellValue(filas.getDescSexo() != null ? String.valueOf(filas.getDescSexo()) : "");
                    celda.setCellStyle(estiloCelda);

                    //COLUMNA: DNI
                    celda = fila.createCell(3);
                    celda.setCellValue(filas.getDni() != null ? String.valueOf(filas.getDni()) : "");
                    celda.setCellStyle(estiloCelda);

                    //COLUMNA: PENSIONISTA
                    celda = fila.createCell(4);
                    celda.setCellValue(filas.getDescPensionista() != null ? String.valueOf(filas.getDescPensionista()) : "");
                    celda.setCellStyle(estiloCelda);

                    //COLUMNA: TIPO DE PENSIONISTA
                    celda = fila.createCell(5);
                    celda.setCellValue(filas.getDescTipoPensionista() != null ? String.valueOf(filas.getDescTipoPensionista()) : "");
                    celda.setCellStyle(estiloCelda);

                    //COLUMNA: TIPO DE CONTRATO
                    celda = fila.createCell(6);
                    celda.setCellValue(filas.getDescTipoContrato() != null ? String.valueOf(filas.getDescTipoContrato()) : "");
                    celda.setCellStyle(estiloCelda);

                    //COLUMNA: TIPO DE JORNADA
                    celda = fila.createCell(7);
                    celda.setCellValue(filas.getDescTipoJornada() != null ? String.valueOf(filas.getDescTipoJornada()) : "");
                    celda.setCellStyle(estiloCelda);

                    //COLUMNA: JORNADA LABORAL PARCIAL (%)
                    celda = fila.createCell(8);
                    double jorParcPorc = filas.getJorParcPorc();
                    if (jorParcPorc != 0.0 && !Double.isNaN(jorParcPorc)) {
                        if (jorParcPorc == -1) {
                            celda.setCellValue("");
                            celda.setCellStyle(estiloCelda);
                        } else {
                            celda.setCellValue(jorParcPorc + "  %");
                            celda.setCellStyle(estiloCelda);
                        }
                    } else {
                        celda.setCellStyle(estiloCelda);
                    }

                    //COLUMNA: DISCAPACIDAD PSIQUICA (33% o más)
                    celda = fila.createCell(9);
                    int tipoDiscPsiquica = filas.getTipoDiscPsiquica();
                    if (tipoDiscPsiquica != -1) {
                        celda.setCellValue(filas.getTipoDiscPsiquica() + "  %");
                        celda.setCellStyle(estiloCelda);
                    } else {
                        celda.setCellStyle(estiloCelda);
                    }

                    //COLUMNA: DISCAPACIDAD FISICA (33% o más)
                    celda = fila.createCell(10);
                    int tipoDiscFisica = filas.getTipoDiscFisica();
                    if (tipoDiscFisica != -1) {
                        celda.setCellValue(filas.getTipoDiscFisica() + "  %");
                        celda.setCellStyle(estiloCelda);
                    } else {
                        celda.setCellStyle(estiloCelda);
                    }
                    celda = fila.createCell(11);
                    int tipoDiscSensorial = filas.getTipoDiscSensorial();
                    if (tipoDiscSensorial != -1) {
                        celda.setCellValue(filas.getTipoDiscSensorial() + "  %");
                        celda.setCellStyle(estiloCelda);
                    } else {
                        celda.setCellStyle(estiloCelda);
                    }
                }

                File directorioTemp = new File(m_Conf.getString("PDF.base_dir"));
                File informe = File.createTempFile("resumenDatosAnexoA", ".xls", directorioTemp);

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
            } catch (IOException ioe) {
                log.error("EXCEPCION informe resumenDatosAnexoA");

            } finally {
                if (istr != null) {
                    istr.close();
                }
            }
        } catch (IOException ex) {
            log.error("EXCEPCION informe resumenDatosAnexoA");
        } catch (SQLException ex) {
            log.error("EXCEPCION informe resumenDatosAnexoA");
        }
    }

    private void crearEstiloInformeDatosAnexoA(HSSFWorkbook libro, HSSFRow fila, HSSFCell celda, HSSFCellStyle estiloCelda, int idioma) {
        log.info(">>>> ENTRA en crearEstiloInformeDatosAnexoA de " + this.getClass().getSimpleName());
        try {
            MeLanbide65I18n meLanbide65I18n = MeLanbide65I18n.getInstance();

            HSSFPalette palette = libro.getCustomPalette();
            HSSFColor hssfColor = null;
            try {
                hssfColor = palette.findColor((byte) 75, (byte) 149, (byte) 211);
                if (hssfColor == null) {
                    hssfColor = palette.getColor(HSSFColor.ROYAL_BLUE.index);
                }
                palette.getColor(HSSFColor.GREY_50_PERCENT.index);

                HSSFFont negrita = libro.createFont();
                negrita.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

                HSSFFont normal = libro.createFont();
                normal.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
                normal.setFontHeight((short) 150);

                HSSFFont negritaTitulo = libro.createFont();
                negritaTitulo.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                negritaTitulo.setFontHeight((short) 170);
                negritaTitulo.setColor(HSSFColor.WHITE.index);
                //celda = fila.

                //cabeceras
                // num linea
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(0);
                celda.setCellValue(meLanbide65I18n.getMensaje(idioma, "controlAcceso.tablaAccesos.col1").toUpperCase());
                celda.setCellStyle(estiloCelda);
                // Nombre y Apellidos
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(1);
                celda.setCellValue(meLanbide65I18n.getMensaje(idioma, "controlAcceso.tablaAccesos.col2").toUpperCase());
                celda.setCellStyle(estiloCelda);

                //Sexo
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(2);
                celda.setCellValue(meLanbide65I18n.getMensaje(idioma, "controlAcceso.tablaAccesos.col15").toUpperCase());
                celda.setCellStyle(estiloCelda);
                // DNI
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(3);
                celda.setCellValue(meLanbide65I18n.getMensaje(idioma, "controlAcceso.tablaAccesos.col3").toUpperCase());
                celda.setCellStyle(estiloCelda);
                //Pensionista
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(4);
                celda.setCellValue(meLanbide65I18n.getMensaje(idioma, "controlAcceso.tablaAccesos.col4").toUpperCase());
                celda.setCellStyle(estiloCelda);
                // Tipo pensionista
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(5);
                celda.setCellValue(meLanbide65I18n.getMensaje(idioma, "controlAcceso.tablaAccesos.col5").toUpperCase());
                celda.setCellStyle(estiloCelda);
                // Tipo contrato
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(6);
                celda.setCellValue(meLanbide65I18n.getMensaje(idioma, "controlAcceso.tablaAccesos.col6").toUpperCase());
                celda.setCellStyle(estiloCelda);
                // Tipo Jornada
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(7);
                celda.setCellValue(meLanbide65I18n.getMensaje(idioma, "controlAcceso.tablaAccesos.col10").toUpperCase());
                celda.setCellStyle(estiloCelda);
                // Jornada Laboral Parcial(%)
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(8);
                celda.setCellValue(meLanbide65I18n.getMensaje(idioma, "controlAcceso.tablaAccesos.col11").toUpperCase());
                celda.setCellStyle(estiloCelda);
                // Discapacidad Psiquica(33% o más)
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(9);
                celda.setCellValue(meLanbide65I18n.getMensaje(idioma, "controlAcceso.tablaAccesos.col12").toUpperCase());
                celda.setCellStyle(estiloCelda);
                // Discapacidad Física(33% o más)
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(10);
                celda.setCellValue(meLanbide65I18n.getMensaje(idioma, "controlAcceso.tablaAccesos.col13").toUpperCase());
                celda.setCellStyle(estiloCelda);
                // Discapacidad Sensorial(65% o más)
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(11);
                celda.setCellValue(meLanbide65I18n.getMensaje(idioma, "controlAcceso.tablaAccesos.col14").toUpperCase());
                celda.setCellStyle(estiloCelda);

            } catch (Exception e) {
            }

        } catch (Exception ex) {

        }

    }

    //Generar Excell Anexo B
    public void generarExcelAnexoB(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info(">>>> ENTRA en generarExcelAnexoB  de " + this.getClass().getSimpleName());
        String rutaArchivoSalida = null;

        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            int idioma = 1;
            MeLanbide65I18n meLanbide65I18n = MeLanbide65I18n.getInstance();
            MeLanbide65Manager manager = MeLanbide65Manager.getInstance();
            ArrayList<PersonaVO> datos = null;
            ArrayList<EncargadoVO> lista = null;
            String nombreTabla;
            try {
                numExpediente = request.getParameter("numExp");
                //                  Connection con = null;
                try {
                    //   con = adapt.getConnection();
                    nombreTabla = ConstantesMeLanbide65.getNOMBRE_TABLA_TECNICO();
                    datos = manager.obtenerDatos(nombreTabla, numExpediente, adapt, null);
                    //Convertimos cada objeto PersonaVO en lo que realmente son, objetos de TrabajadorVO
                    lista = new ArrayList<EncargadoVO>();
                    for (PersonaVO persona : datos) {
                        ((EncargadoVO) persona).setDescTipoContrato(getDescripcionDesplegable(request, ((EncargadoVO) persona).getDescTipoContrato()));
                        ((EncargadoVO) persona).setDescTipoJornada(getDescripcionDesplegable(request, ((EncargadoVO) persona).getDescTipoJornada()));
                        ((EncargadoVO) persona).setDescPensionista(getDescripcionDesplegable(request, ((EncargadoVO) persona).getDescPensionista()));
                        ((EncargadoVO) persona).setDescTipoPensionista(getDescripcionDesplegable(request, ((EncargadoVO) persona).getDescTipoPensionista()));

                        lista.add((EncargadoVO) persona);
                    }
                } catch (Exception ex) {
                    log.error("Error al recuperar datos del Anexo B");
                }
            } catch (Exception ex) {
                log.error("Error recuperando datos de Anexo B : " + ex);
            }

            FileInputStream istr = null;
            try {
                HSSFWorkbook libro = new HSSFWorkbook();

                HSSFPalette palette = libro.getCustomPalette();
                HSSFColor hssfColor = null;
                try {
                    hssfColor = palette.findColor((byte) 75, (byte) 149, (byte) 211);
                    if (hssfColor == null) {
                        hssfColor = palette.getColor(HSSFColor.ROYAL_BLUE.index);
                    }
                } catch (Exception e) {
                }

                HSSFFont negrita = libro.createFont();
                negrita.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

                HSSFFont normal = libro.createFont();
                normal.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
                normal.setFontHeight((short) 150);

                HSSFFont negritaTitulo = libro.createFont();
                negritaTitulo.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                negritaTitulo.setColor(HSSFColor.WHITE.index);
                negritaTitulo.setFontHeight((short) 200);
                int numFila = 0;
                //int numFila2 = 0;

                HSSFRow fila = null;
                HSSFCell celda = null;
                HSSFCellStyle estiloCelda = null;

                HSSFSheet hoja = libro.createSheet("Datos Anexo B - Plantilla");

                //Se establece el ancho de cada columnas
                //hoja.setColumnWidth((short)0, (short)1500);//ID
                hoja.setColumnWidth(0, 3000);///Número de línea
                hoja.setColumnWidth(1, 8000);//Apellidos y nombre
                hoja.setColumnWidth(2, 3000);//DNI
                hoja.setColumnWidth(3, 4000);//Pensionista
                hoja.setColumnWidth(4, 5000);//Tipo de pensionista
                hoja.setColumnWidth(5, 5000);//Tipo de contrato
                hoja.setColumnWidth(6, 4000);//Fecha de alta
                hoja.setColumnWidth(7, 4000);//Fecha de alta contrato temporal
                hoja.setColumnWidth(8, 4000);//Fecha de baja contrato temporal
                hoja.setColumnWidth(9, 3000);//Tipo Jornada
                hoja.setColumnWidth(10, 4000);//Porcentaje Jornada laboral parcial

                fila = hoja.createRow(numFila);
                //Se establece el alto de las columnas
                fila.setHeight((short) 750);

                crearEstiloInformeDatosAnexoB(libro, fila, celda, estiloCelda, idioma);

                int p = 0;
                int n = 0;

                String numExp = null;

                estiloCelda = libro.createCellStyle();
                estiloCelda.setWrapText(true);
                estiloCelda.setFont(normal);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                //estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                //Insertamos los datos, fila a fila

                for (EncargadoVO filaI : lista) {
                    numFila++;
                    fila = hoja.createRow(numFila);
                    //crearDatosInformeAnexoB(libro, fila, celda, estiloCelda, idioma, causaAl, numExp, numFila, nuevo, n, listaAccesos, normal, codOrganizacion, adapt, format);

                    //COLUMNA: NUMERO DE LINEA
                    celda = fila.createCell(0);
                    celda.setCellValue(numFila);
                    celda.setCellStyle(estiloCelda);

                    //COLUMNA: APELLIDOS Y NOMBRE
                    celda = fila.createCell(1);
                    celda.setCellValue((filaI.getApellido1() != null ? filaI.getApellido1().toUpperCase() : "") + ", " + (filaI.getApellido2() != null ? filaI.getApellido2().toUpperCase() : "") + ", " + (filaI.getNombre() != null ? filaI.getNombre().toUpperCase() : ""));
                    celda.setCellStyle(estiloCelda);

                    //COLUMNA: DNI
                    celda = fila.createCell(2);
                    celda.setCellValue(filaI.getDni() != null ? String.valueOf(filaI.getDni()) : "");
                    //celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                    celda.setCellStyle(estiloCelda);
                    //COLUMNA: PENSIONISTA
                    celda = fila.createCell(3);
                    celda.setCellValue(filaI.getDescPensionista() != null ? String.valueOf(filaI.getDescPensionista()) : "");
                    celda.setCellStyle(estiloCelda);

                    //COLUMNA: TIPO DE PENSIONISTA
                    celda = fila.createCell(4);
                    celda.setCellValue(filaI.getDescTipoPensionista() != null ? String.valueOf(filaI.getDescTipoPensionista()) : "");
                    celda.setCellStyle(estiloCelda);

                    //COLUMNA: TIPO DE CONTRATO
                    celda = fila.createCell(5);
                    celda.setCellValue(filaI.getDescTipoContrato() != null ? String.valueOf(filaI.getDescTipoContrato()) : "");
                    celda.setCellStyle(estiloCelda);
                    //COLUMNA: FECHA DE ALTA
                    celda = fila.createCell(6);
                    celda.setCellValue(filaI.getFecAltaContrIndef() != null ? dateFormat.format(filaI.getFecAltaContrIndef()) : "");
                    celda.setCellStyle(estiloCelda);

                    //COLUMNA: FECHA DE ALTA CONTRATO TEMPORAL
                    celda = fila.createCell(7);
                    celda.setCellValue(filaI.getFecAltaContrTemp() != null ? dateFormat.format(filaI.getFecAltaContrTemp()) : "");
                    celda.setCellStyle(estiloCelda);

                    // COLUMNA: FECHA DE BAJA CONTRATO TEMPORAL
                    celda = fila.createCell(8);
                    celda.setCellValue(filaI.getFecBajaContrTemp() != null ? dateFormat.format(filaI.getFecBajaContrTemp()) : "");
                    celda.setCellStyle(estiloCelda);

                    //COLUMNA: TIPO DE JORNADA
                    celda = fila.createCell(9);
                    celda.setCellValue(filaI.getDescTipoJornada() != null ? String.valueOf(filaI.getDescTipoJornada()) : "");
                    celda.setCellStyle(estiloCelda);

                    //COLUMNA: JORNADA LABORAL PARCIAL(%)
                    celda = fila.createCell(10);
                    double jornadaParcialPor = filaI.getJornadaParcialPor();
                    if (jornadaParcialPor != 0.0 && !Double.isNaN(jornadaParcialPor)) {
                        if (jornadaParcialPor == -1) {
                            celda.setCellValue("");
                            celda.setCellStyle(estiloCelda);
                        } else {
                            celda.setCellValue(jornadaParcialPor + "  %");
                            celda.setCellStyle(estiloCelda);
                        }
                    } else {
                        celda.setCellStyle(estiloCelda);
                    }

                }

                File directorioTemp = new File(m_Conf.getString("PDF.base_dir"));
                File informe = File.createTempFile("resumenDatosAnexoB", ".xls", directorioTemp);

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
            } catch (IOException ioe) {
                log.error("EXCEPCION informe resumenDatosAnexoB");

            } finally {
                if (istr != null) {
                    istr.close();
                }
            }
        } catch (IOException ex) {
            log.error("EXCEPCION informe resumenDatosAnexoB");
        } catch (SQLException ex) {
            log.error("EXCEPCION informe resumenDatosAnexoB");
        }
    }

    private void crearEstiloInformeDatosAnexoB(HSSFWorkbook libro, HSSFRow fila, HSSFCell celda, HSSFCellStyle estiloCelda, int idioma) {
        log.info(">>>> ENTRA en crearEstiloInformeDatosAnexoB de " + this.getClass().getSimpleName());
        try {
            MeLanbide65I18n meLanbide65I18n = MeLanbide65I18n.getInstance();

            HSSFPalette palette = libro.getCustomPalette();
            HSSFColor hssfColor = null;
            try {
                hssfColor = palette.findColor((byte) 75, (byte) 149, (byte) 211);
                if (hssfColor == null) {
                    hssfColor = palette.getColor(HSSFColor.ROYAL_BLUE.index);
                }
                palette.getColor(HSSFColor.GREY_50_PERCENT.index);

                HSSFFont negrita = libro.createFont();
                negrita.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

                HSSFFont normal = libro.createFont();
                normal.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
                normal.setFontHeight((short) 150);

                HSSFFont negritaTitulo = libro.createFont();
                negritaTitulo.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                negritaTitulo.setFontHeight((short) 170);
                negritaTitulo.setColor(HSSFColor.WHITE.index);
                //celda = fila.

                //cabeceras
                // num linea
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(0);
                celda.setCellValue(meLanbide65I18n.getMensaje(idioma, "controlAcceso.tablaAccesos.col1").toUpperCase());
                celda.setCellStyle(estiloCelda);
                // Apellidos y nombre
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(1);
                celda.setCellValue(meLanbide65I18n.getMensaje(idioma, "controlAcceso.tablaAccesos.col2").toUpperCase());
                celda.setCellStyle(estiloCelda);
                // DNI
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(2);
                celda.setCellValue(meLanbide65I18n.getMensaje(idioma, "controlAcceso.tablaAccesos.col3").toUpperCase());
                celda.setCellStyle(estiloCelda);
                //Pensionista
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(3);
                celda.setCellValue(meLanbide65I18n.getMensaje(idioma, "controlAcceso.tablaAccesos.col4").toUpperCase());
                celda.setCellStyle(estiloCelda);
                // Tipo pensionista
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(4);
                celda.setCellValue(meLanbide65I18n.getMensaje(idioma, "controlAcceso.tablaAccesos.col5").toUpperCase());
                celda.setCellStyle(estiloCelda);
                // Tipo contrato
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(5);
                celda.setCellValue(meLanbide65I18n.getMensaje(idioma, "controlAcceso.tablaAccesos.col6").toUpperCase());
                celda.setCellStyle(estiloCelda);
                // Fecha de alta
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(6);
                celda.setCellValue(meLanbide65I18n.getMensaje(idioma, "controlAcceso.tablaAccesos.col7").toUpperCase());
                celda.setCellStyle(estiloCelda);
                // Fecha de alta cotrato temporal
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(7);
                celda.setCellValue(meLanbide65I18n.getMensaje(idioma, "controlAcceso.tablaAccesos.col8").toUpperCase());
                celda.setCellStyle(estiloCelda);
                // Fecha de baja contrato temporal
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(8);
                celda.setCellValue(meLanbide65I18n.getMensaje(idioma, "controlAcceso.tablaAccesos.col9").toUpperCase());
                celda.setCellStyle(estiloCelda);
                // Tipo de jornada
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(9);
                celda.setCellValue(meLanbide65I18n.getMensaje(idioma, "controlAcceso.tablaAccesos.col10").toUpperCase());
                celda.setCellStyle(estiloCelda);
                // Jornada laboral parcial
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(10);
                celda.setCellValue(meLanbide65I18n.getMensaje(idioma, "controlAcceso.tablaAccesos.col11").toUpperCase());
                celda.setCellStyle(estiloCelda);

            } catch (Exception e) {
            }

        } catch (Exception ex) {

        }

    }

    //Generar Excel Anexo C
    public void generarExcelAnexoC(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info(">>>> ENTRA en generarExcelAnexoC  de " + this.getClass().getSimpleName());
        String rutaArchivoSalida = null;

        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            int idioma = 1;
            MeLanbide65I18n meLanbide65I18n = MeLanbide65I18n.getInstance();
            MeLanbide65Manager manager = MeLanbide65Manager.getInstance();
            ArrayList<PersonaVO> datos = null;
            ArrayList<EncargadoVO> lista = null;
            String nombreTabla;
            try {
                numExpediente = request.getParameter("numExp");
                //              Connection con = null;
                try {
                    //       con = adapt.getConnection();
                    nombreTabla = ConstantesMeLanbide65.getNOMBRE_TABLA_ENCARGADO();
                    datos = manager.obtenerDatos(nombreTabla, numExpediente, adapt, null);
                    //Convertimos cada objeto PersonaVO en lo que realmente son, objetos de TrabajadorVO
                    lista = new ArrayList<EncargadoVO>();
                    for (PersonaVO persona : datos) {
                        ((EncargadoVO) persona).setDescTipoContrato(getDescripcionDesplegable(request, ((EncargadoVO) persona).getDescTipoContrato()));
                        ((EncargadoVO) persona).setDescTipoJornada(getDescripcionDesplegable(request, ((EncargadoVO) persona).getDescTipoJornada()));
                        ((EncargadoVO) persona).setDescPensionista(getDescripcionDesplegable(request, ((EncargadoVO) persona).getDescPensionista()));
                        ((EncargadoVO) persona).setDescTipoPensionista(getDescripcionDesplegable(request, ((EncargadoVO) persona).getDescTipoPensionista()));

                        lista.add((EncargadoVO) persona);
                    }
                } catch (Exception ex) {
                    log.error("Error al recuperar datos del Anexo C");
                }
            } catch (Exception ex) {
                log.error("Error recuperando datos de Anexo C : " + ex);
            }

            FileInputStream istr = null;
            try {
                HSSFWorkbook libro = new HSSFWorkbook();

                HSSFPalette palette = libro.getCustomPalette();
                HSSFColor hssfColor = null;
                try {
                    hssfColor = palette.findColor((byte) 75, (byte) 149, (byte) 211);
                    if (hssfColor == null) {
                        hssfColor = palette.getColor(HSSFColor.ROYAL_BLUE.index);
                    }
                } catch (Exception e) {
                }

                HSSFFont negrita = libro.createFont();
                negrita.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

                HSSFFont normal = libro.createFont();
                normal.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
                normal.setFontHeight((short) 150);

                HSSFFont negritaTitulo = libro.createFont();
                negritaTitulo.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                negritaTitulo.setColor(HSSFColor.WHITE.index);
                negritaTitulo.setFontHeight((short) 200);
                int numFila = 0;
                //int numFila2 = 0;

                HSSFRow fila = null;
                HSSFCell celda = null;
                HSSFCellStyle estiloCelda = null;

                HSSFSheet hoja = libro.createSheet("Datos Anexo C - Plantilla");

                //Se establece el ancho de cada columnas
                //hoja.setColumnWidth((short)0, (short)1500);//ID
                hoja.setColumnWidth(0, 3000);///Número de línea
                hoja.setColumnWidth(1, 8000);//Apellidos y nombre
                hoja.setColumnWidth(2, 3000);//DNI
                hoja.setColumnWidth(3, 4000);//Pensionista
                hoja.setColumnWidth(4, 5000);//Tipo de pensionista
                hoja.setColumnWidth(5, 5000);//Tipo de contrato
                hoja.setColumnWidth(6, 4000);//Fecha de alta
                hoja.setColumnWidth(7, 4000);//Fecha de alta contrato temporal
                hoja.setColumnWidth(8, 4000);//Fecha de baja contrato temporal
                hoja.setColumnWidth(9, 3000);//Tipo Joprnada
                hoja.setColumnWidth(10, 4000);//Porcentaje Jornada laboral parcial

                fila = hoja.createRow(numFila);
                //Se establece el alto de las columnas
                fila.setHeight((short) 750);

                crearEstiloInformeDatosAnexoC(libro, fila, celda, estiloCelda, idioma);

                int p = 0;
                int n = 0;

                String numExp = null;

                estiloCelda = libro.createCellStyle();
                estiloCelda.setWrapText(true);
                estiloCelda.setFont(normal);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                //estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                //Insertamos los datos, fila a fila

                for (EncargadoVO filaI : lista) {
                    numFila++;
                    fila = hoja.createRow(numFila);
                    //crearDatosInformeAnexoC(libro, fila, celda, estiloCelda, idioma, causaAl, numExp, numFila, nuevo, n, listaAccesos, normal, codOrganizacion, adapt, format);

                    //COLUMNA: NUMERO DE LINEA
                    celda = fila.createCell(0);
                    celda.setCellValue(numFila);
                    celda.setCellStyle(estiloCelda);

                    //COLUMNA: APELLIDOS Y NOMBRE
                    celda = fila.createCell(1);
                    celda.setCellValue((filaI.getApellido1() != null ? filaI.getApellido1().toUpperCase() : "") + ", " + (filaI.getApellido2() != null ? filaI.getApellido2().toUpperCase() : "") + ", " + (filaI.getNombre() != null ? filaI.getNombre().toUpperCase() : ""));
                    celda.setCellStyle(estiloCelda);

                    //COLUMNA: DNI
                    celda = fila.createCell(2);
                    celda.setCellValue(filaI.getDni() != null ? String.valueOf(filaI.getDni()) : "");
                    //celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                    celda.setCellStyle(estiloCelda);

                    //COLUMNA: PENSIONISTA
                    celda = fila.createCell(3);
                    celda.setCellValue(filaI.getDescPensionista() != null ? String.valueOf(filaI.getDescPensionista()) : "");
                    celda.setCellStyle(estiloCelda);

                    //COLUMNA: TIPO DE PENSIONISTA
                    celda = fila.createCell(4);
                    celda.setCellValue(filaI.getDescTipoPensionista() != null ? String.valueOf(filaI.getDescTipoPensionista()) : "");
                    celda.setCellStyle(estiloCelda);

                    //COLUMNA: TIPO DE CONTRATO
                    celda = fila.createCell(5);
                    celda.setCellValue(filaI.getDescTipoContrato() != null ? String.valueOf(filaI.getDescTipoContrato()) : "");
                    celda.setCellStyle(estiloCelda);

                    //COLUMNA: FECHA DE ALTA
                    celda = fila.createCell(6);
                    celda.setCellValue(filaI.getFecAltaContrIndef() != null ? dateFormat.format(filaI.getFecAltaContrIndef()) : "");
                    celda.setCellStyle(estiloCelda);

                    //COLUMNA: FECHA DE ALTA CONTRATO TEMPORAL
                    celda = fila.createCell(7);
                    celda.setCellValue(filaI.getFecAltaContrTemp() != null ? dateFormat.format(filaI.getFecAltaContrTemp()) : "");
                    celda.setCellStyle(estiloCelda);

                    // COLUMNA: FECHA DE BAJA CONTRATO TEMPORAL
                    celda = fila.createCell(8);
                    celda.setCellValue(filaI.getFecBajaContrTemp() != null ? dateFormat.format(filaI.getFecBajaContrTemp()) : "");
                    celda.setCellStyle(estiloCelda);

                    //COLUMNA: TIPO DE JORNADA
                    celda = fila.createCell(9);
                    celda.setCellValue(filaI.getDescTipoJornada() != null ? String.valueOf(filaI.getDescTipoJornada()) : "");
                    celda.setCellStyle(estiloCelda);
                    //celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

                    //COLUMNA: PORCENTAJE JORNADA
                    celda = fila.createCell(10);
                    //celda.setCellValue(String.valueOf(filaI.getJornadaParcialPor()) + "  %");
                    //celda.setCellStyle(estiloCelda);
                    //celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                    celda = fila.createCell(10);
                    double jornadaParcialPor = filaI.getJornadaParcialPor();
                    if (jornadaParcialPor != 0.0 && !Double.isNaN(jornadaParcialPor)) {
                        if (jornadaParcialPor == -1) {
                            celda.setCellValue("");
                            celda.setCellStyle(estiloCelda);
                        } else {
                            celda.setCellValue(jornadaParcialPor + "  %");
                            celda.setCellStyle(estiloCelda);
                        }
                    } else {
                        celda.setCellStyle(estiloCelda);
                    }

                }

                File directorioTemp = new File(m_Conf.getString("PDF.base_dir"));
                File informe = File.createTempFile("resumenDatosAnexoC", ".xls", directorioTemp);

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

            } catch (IOException ioe) {
                log.error("EXCEPCION informe resumenDatosAnexoC");

            } finally {
                if (istr != null) {
                    istr.close();
                }
            }
        } catch (IOException ex) {
            log.error("EXCEPCION informe resumenDatosAnexoC");
        } catch (SQLException ex) {
            log.error("EXCEPCION informe resumenDatosAnexoC");
        }
    }

    private void crearEstiloInformeDatosAnexoC(HSSFWorkbook libro, HSSFRow fila, HSSFCell celda, HSSFCellStyle estiloCelda, int idioma) {
        log.info(">>>> ENTRA en crearEstiloInformeDatosAnexoC de " + this.getClass().getSimpleName());
        try {
            MeLanbide65I18n meLanbide65I18n = MeLanbide65I18n.getInstance();

            HSSFPalette palette = libro.getCustomPalette();
            HSSFColor hssfColor = null;
            try {
                hssfColor = palette.findColor((byte) 75, (byte) 149, (byte) 211);
                if (hssfColor == null) {
                    hssfColor = palette.getColor(HSSFColor.ROYAL_BLUE.index);
                }
                palette.getColor(HSSFColor.GREY_50_PERCENT.index);

                HSSFFont negrita = libro.createFont();
                negrita.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

                HSSFFont normal = libro.createFont();
                normal.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
                normal.setFontHeight((short) 150);

                HSSFFont negritaTitulo = libro.createFont();
                negritaTitulo.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                negritaTitulo.setFontHeight((short) 170);
                negritaTitulo.setColor(HSSFColor.WHITE.index);
                //celda = fila.

                //cabeceras
                // num linea
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(0);
                celda.setCellValue(meLanbide65I18n.getMensaje(idioma, "controlAcceso.tablaAccesos.col1").toUpperCase());
                celda.setCellStyle(estiloCelda);
                // Apellidos y nombre
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(1);
                celda.setCellValue(meLanbide65I18n.getMensaje(idioma, "controlAcceso.tablaAccesos.col2").toUpperCase());
                celda.setCellStyle(estiloCelda);
                // DNI
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(2);
                celda.setCellValue(meLanbide65I18n.getMensaje(idioma, "controlAcceso.tablaAccesos.col3").toUpperCase());
                celda.setCellStyle(estiloCelda);
                //Pensionista
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(3);
                celda.setCellValue(meLanbide65I18n.getMensaje(idioma, "controlAcceso.tablaAccesos.col4").toUpperCase());
                celda.setCellStyle(estiloCelda);
                // Tipo pensionista
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(4);
                celda.setCellValue(meLanbide65I18n.getMensaje(idioma, "controlAcceso.tablaAccesos.col5").toUpperCase());
                celda.setCellStyle(estiloCelda);
                // Tipo contrato
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(5);
                celda.setCellValue(meLanbide65I18n.getMensaje(idioma, "controlAcceso.tablaAccesos.col6").toUpperCase());
                celda.setCellStyle(estiloCelda);
                // Fecha de alta
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(6);
                celda.setCellValue(meLanbide65I18n.getMensaje(idioma, "controlAcceso.tablaAccesos.col7").toUpperCase());
                celda.setCellStyle(estiloCelda);
                // Fecha de alta cotrato temporal
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(7);
                celda.setCellValue(meLanbide65I18n.getMensaje(idioma, "controlAcceso.tablaAccesos.col8").toUpperCase());
                celda.setCellStyle(estiloCelda);
                // Fecha de baja contrato temporal
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(8);
                celda.setCellValue(meLanbide65I18n.getMensaje(idioma, "controlAcceso.tablaAccesos.col9").toUpperCase());
                celda.setCellStyle(estiloCelda);
                // Tipo de jornada
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(9);
                celda.setCellValue(meLanbide65I18n.getMensaje(idioma, "controlAcceso.tablaAccesos.col10").toUpperCase());
                celda.setCellStyle(estiloCelda);
                // Jornada laboral parcial
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(10);
                celda.setCellValue(meLanbide65I18n.getMensaje(idioma, "controlAcceso.tablaAccesos.col11").toUpperCase());
                celda.setCellStyle(estiloCelda);

            } catch (Exception e) {
            }

        } catch (Exception ex) {

        }
    }

    // #30997 - Funciones para la exportación de la tabla de recalculo de importes. exportarExcelRecalculoImportes, crearEstiloInformeRecalculoImportes
    public void exportarExcelRecalculoImportes(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String codigoOperacion = "0";
        log.info("INICIO.exportarExcelRecalculoImportes");
        List<ExpedienteUAAPVO> expedientesRecalculados = new ArrayList<ExpedienteUAAPVO>();
        AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        try {   // Se hace recogen los expedientes desde base de datos, pero convendría pasar los datos desde el front, para evitar hacer llamadas innecesarias.
            ExpUAAPCriteriosFiltroVO _criterioBusqueda = new ExpUAAPCriteriosFiltroVO();
            _criterioBusqueda.setEjercicio((String) request.getParameter("ejercicio"));
            _criterioBusqueda.setTipoProcedimiento((String) request.getParameter("tipoProcedimiento"));
            _criterioBusqueda.setPeriodoSubvencionable((String) request.getParameter("periodoSubvencionable"));
            expedientesRecalculados = MeLanbide65Manager.getInstance().busquedaFiltrandoListaExpedientes(_criterioBusqueda, adapt);
        } catch (Exception e) {
            log.error("Error al consultar lista de expedientes - Exportar Excel Recalculo Importes:");
            log.error(e);
        }

        String rutaArchivoSalida = null;
        int idioma = 1;

        FileInputStream istr = null;
        try {
            HSSFWorkbook libro = new HSSFWorkbook();

            HSSFPalette palette = libro.getCustomPalette();
            HSSFColor hssfColor = null;
            hssfColor = palette.findColor((byte) 75, (byte) 149, (byte) 211);
            if (hssfColor == null) {
                palette.getColor(HSSFColor.ROYAL_BLUE.index);
            }

            HSSFFont negrita = libro.createFont();
            negrita.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

            HSSFFont normal = libro.createFont();
            normal.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
            normal.setFontHeight((short) 150);

            HSSFFont negritaTitulo = libro.createFont();
            negritaTitulo.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            negritaTitulo.setColor(HSSFColor.WHITE.index);
            negritaTitulo.setFontHeight((short) 200);

            HSSFRow fila = null;
            HSSFCell celda = null;
            HSSFCellStyle estiloCelda = null;

            HSSFSheet hoja = libro.createSheet("Recalculo Importes - " + request.getParameter("tipoProcedimiento") + " - Plantilla");

            hoja.setColumnWidth(0, 5000);///Número expediente
            hoja.setColumnWidth(1, 8000);//Nombre y Apellidos
            hoja.setColumnWidth(2, 3000);//CP
            hoja.setColumnWidth(3, 4000);//Importe concesion Inicial
            hoja.setColumnWidth(4, 4000);//Importe recalculado

            int numFila = 0;
            fila = hoja.createRow(numFila);
            fila.setHeight((short) 750);

            crearEstiloInformeRecalculoImportes(libro, fila, celda, estiloCelda, idioma);
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);


            NumberFormat formatter = NumberFormat.getInstance();
            formatter.setMaximumFractionDigits(4);

            for (ExpedienteUAAPVO expediente : expedientesRecalculados) {
                numFila++;
                fila = hoja.createRow(numFila);

                celda = fila.createCell(0);
                celda.setCellValue(expediente.getNumeroExpediente());
                celda.setCellStyle(estiloCelda);

                celda = fila.createCell(1);
                celda.setCellValue(expediente.getNombreInteresado());
                celda.setCellStyle(estiloCelda);

                celda = fila.createCell(2);
                celda.setCellValue(expediente.getCP());
                celda.setCellStyle(estiloCelda);

                celda = fila.createCell(3);
                celda.setCellValue(formatter.format(expediente.getImporteConini()));
                celda.setCellStyle(estiloCelda);

                celda = fila.createCell(4);
                celda.setCellValue(expediente.getImporteTotalRecal());
                celda.setCellStyle(estiloCelda);
            }

            File directorioTemp = new File(m_Conf.getString("PDF.base_dir"));
            File informe = File.createTempFile("importesRecalculados" + request.getParameter("tipoProcedimiento"), ".xls", directorioTemp);

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
        } catch (IOException e) {
            log.error("Exportar Recalculo Importes:");
            log.error(e);
        } finally {
            if (istr != null) {
                istr.close();
            }
        }
    }
    private void crearEstiloInformeRecalculoImportes(HSSFWorkbook libro, HSSFRow fila, HSSFCell celda, HSSFCellStyle estiloCelda, int idioma) {
        log.info(">>>> ENTRA en crearEstiloInformeRecalculoImportes de " + this.getClass().getSimpleName());
        try {
            MeLanbide65I18n meLanbide65I18n = MeLanbide65I18n.getInstance();

            HSSFPalette palette = libro.getCustomPalette();
            HSSFColor hssfColor = null;
            hssfColor = palette.findColor((byte) 75, (byte) 149, (byte) 211);
            if (hssfColor == null) {
                hssfColor = palette.getColor(HSSFColor.ROYAL_BLUE.index);
            }
            palette.getColor(HSSFColor.GREY_50_PERCENT.index);

            HSSFFont negrita = libro.createFont();
            negrita.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

            HSSFFont normal = libro.createFont();
            normal.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
            normal.setFontHeight((short) 150);

            HSSFFont negritaTitulo = libro.createFont();
            negritaTitulo.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            negritaTitulo.setFontHeight((short) 170);
            negritaTitulo.setColor(HSSFColor.WHITE.index);

            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(hssfColor.getIndex());
            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaTitulo);
            celda = fila.createCell(0);
            celda.setCellValue(meLanbide65I18n.getMensaje(idioma, "recalculoImporte.tablaExpedientes.col0"));
            celda.setCellStyle(estiloCelda);

            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(hssfColor.getIndex());
            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaTitulo);
            celda = fila.createCell(1);
            celda.setCellValue(meLanbide65I18n.getMensaje(idioma, "recalculoImporte.tablaExpedientes.col1"));
            celda.setCellStyle(estiloCelda);

            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(hssfColor.getIndex());
            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaTitulo);
            celda = fila.createCell(2);
            celda.setCellValue(meLanbide65I18n.getMensaje(idioma, "recalculoImporte.tablaExpedientes.col2"));
            celda.setCellStyle(estiloCelda);

            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(hssfColor.getIndex());
            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaTitulo);
            celda = fila.createCell(3);
            celda.setCellValue(meLanbide65I18n.getMensaje(idioma, "recalculoImporte.tablaExpedientes.col3"));
            celda.setCellStyle(estiloCelda);

            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(hssfColor.getIndex());
            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaTitulo);
            celda = fila.createCell(4);
            celda.setCellValue(meLanbide65I18n.getMensaje(idioma, "recalculoImporte.tablaExpedientes.col4"));
            celda.setCellStyle(estiloCelda);
        } catch (Exception e) {
            log.error("Exportar Recalculo Importes, creación de estilo:");
            log.error(e);
        }
    }

    // INICIO Tarea  #858265 abierta Anterior | 14/15 | Siguiente » UAAP - [39448] - Interoperabilidad - Consulta vida laboral desde la ficha de expediente
    public String cargarVidaLaboral(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente,
                                    HttpServletRequest request, HttpServletResponse response) {

        log.info("=========== ENTRO en cargarVidaLaboral");
        melanbide_interop.cargarVidaLaboral(codOrganizacion, codTramite, ocurrenciaTramite, numExpediente,
                request, response);
        return "/jsp/extension/melanbide65/pestanaVidaLaboral.jsp";
    }

    public void consultaVidaLaboralCVLBatchExpediente(int codOrganizacion, int codTramite, int ocurrenciaTramite,
                                                      String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        try {
            final AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));

            melanbide_interop.consultaVidaLaboralCVLBatchExpediente(codOrganizacion, codTramite, ocurrenciaTramite,
                    obtainPersonasAnexos (codOrganizacion, adapt, numExpediente),
                    numExpediente, request, response);
        } catch (final Exception ex) {
            log.error ("Error en consultaVidaLaboralCVLBatchExpediente " + ex.getMessage());
        }
    }

    private List<Persona> obtainPersonasAnexos (int codOrganizacion,
                                                final AdaptadorSQLBD adapt, final String numExpediente) throws Exception {
        List<Persona> personas = new ArrayList<Persona>();
        List<PersonaVO> personasAnexoA = null;
        List<PersonaVO> personasAnexoB = null;
        List<PersonaVO> personasAnexoC = null;

        log.info("=========== ENTRO en obtainPersonasAnexos");
        final Connection con = adapt.getConnection();
        String nombreTablaAnexoA = ConstantesMeLanbide65.getNOMBRE_TABLA_TRABAJADOR();
        String nombreTablaAnexoB = ConstantesMeLanbide65.getNOMBRE_TABLA_TECNICO();
        String nombreTablaAnexoC = ConstantesMeLanbide65.getNOMBRE_TABLA_ENCARGADO();

        personasAnexoA = MeLanbide65Manager.getInstance().obtenerDatos(nombreTablaAnexoA, numExpediente, adapt, con);
        personasAnexoB = MeLanbide65Manager.getInstance().obtenerDatos(nombreTablaAnexoB, numExpediente, adapt, con);
        personasAnexoC = MeLanbide65Manager.getInstance().obtenerDatos(nombreTablaAnexoC, numExpediente, adapt, con);

        for (final PersonaVO p : personasAnexoA) {
            if (p.getDni() != null && !p.getDni().equals("")) {
                if (!estaEnLista(p.getDni(), personas)) {
                    personas.add(new Persona(p.getNombre(),p.getApellido1(), p.getApellido2(),
                            getTipoDocumento(p.getDni()), p.getDni()));
                }
            }
        }

        for (final PersonaVO p : personasAnexoB) {
            if (p.getDni() != null && !p.getDni().equals("")) {
                if (!estaEnLista(p.getDni(), personas)) {
                    personas.add(new Persona(p.getNombre(),p.getApellido1(), p.getApellido2(),
                            getTipoDocumento(p.getDni()), p.getDni()));
                }
            }
        }

        for (final PersonaVO p : personasAnexoC) {
            if (p.getDni() != null && !p.getDni().equals("")) {
                if (!estaEnLista(p.getDni(), personas)) {
                    personas.add(new Persona(p.getNombre(),p.getApellido1(), p.getApellido2(),
                            getTipoDocumento(p.getDni()), p.getDni()));
                }
            }
        }
        log.info("=========== FIN en obtainPersonasAnexos");
        return personas;
    }

    private boolean estaEnLista (final String dni, final List<Persona> personas) {
        for (final Persona p : personas) {
            if (p.getNumDocumento().equals(dni)) {
                return true;
            }
        }
        return false;
    }


    private String getTipoDocumento(final String numDocumento) {
        String tipoDocumento = null;
        if (numDocumento != null && numDocumento.length() >= 1
                && numDocumento.charAt(0) >= 'A'
                && numDocumento.charAt(0) <= 'Z') {
            tipoDocumento = "NIE";
        } else {
            tipoDocumento = "DNI";
        }
        return tipoDocumento;
    }

    // FIN Tarea  #858265 abierta Anterior | 14/15 | Siguiente » UAAP - [39448] - Interoperabilidad - Consulta vida laboral desde la ficha de expediente
}
