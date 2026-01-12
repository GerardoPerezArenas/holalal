
package es.altia.flexia.integracion.moduloexterno.melanbide75;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.melanbide75.i18n.MeLanbide75I18n;
import es.altia.flexia.integracion.moduloexterno.melanbide75.manager.MeLanbide75Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide75.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide75.util.ConstantesMeLanbide75;
import es.altia.flexia.integracion.moduloexterno.melanbide75.vo.ControlAccesoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide75.vo.DesplegableAdmonLocalVO;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
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

public class MELANBIDE75 extends ModuloIntegracionExterno {

    private static final Logger log = LogManager.getLogger(MELANBIDE75.class);
    private static final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    ResourceBundle m_Conf = ResourceBundle.getBundle("common");

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
        } catch (SQLException ex) {
            log.error(this.getClass().getName() + " Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }
        String url = "/jsp/extension/melanbide75/melanbide75.jsp";
        request.setAttribute("numExp", numExpediente);
        if (adapt != null) {
            try {
                List<ControlAccesoVO> listaAccesos = MeLanbide75Manager.getInstance().getDatosControlAcceso(numExpediente, codOrganizacion, adapt);
                if (!listaAccesos.isEmpty()) {
                    request.setAttribute("listaAccesos", listaAccesos);
                }
            } catch (Exception ex) {
                log.error("Error al recuperar los datos De Acceso - MELANBIDE75 - cargarPantallaPrincipal", ex);
            }
        }
        return url;
    }
    
    private List<DesplegableAdmonLocalVO> traducirDesplegable(HttpServletRequest request, List<DesplegableAdmonLocalVO> desplegable){        
        for (DesplegableAdmonLocalVO d : desplegable) {
            if (d.getDes_nom() != null && !d.getDes_nom().isEmpty() ){
                d.setDes_nom(getDescripcionDesplegable(request, d.getDes_nom()));
            }
        }
        return desplegable;
    }
    
    public String cargarNuevoAcceso(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String nuevo = "1";
        String numExp = "";
        String urlnuevoAcceso = "/jsp/extension/melanbide75/nuevoPuesto.jsp?codOrganizacion=" + codOrganizacion;
        try {
            if (request.getAttribute("nuevo") != null) {
                if (request.getAttribute("nuevo").toString().equals("0")) {
                    request.setAttribute("nuevo", nuevo);
                }
            } else {
                request.setAttribute("nuevo", nuevo);
            }
            if (request.getParameter("numExp") != null) {
                numExp = request.getParameter("numExp");
                request.setAttribute("numExp", numExp);
            }
            //Cargamos el request los valores  de los desplegables
            List<DesplegableAdmonLocalVO> listaTCO1 = MeLanbide75Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide75.COD_DES_TCCR, ConstantesMeLanbide75.FICHERO_PROPIEDADES), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            
            if (!listaTCO1.isEmpty()) {
                listaTCO1 = traducirDesplegable(request, listaTCO1);
                request.setAttribute("listaTipoCon1", listaTCO1);
            }
            
        } catch (Exception ex) {
            log.error("Se ha presentado un error al intentar preparar la jsp de un Nuevo Puesto : " + ex.getMessage());
        }
        return urlnuevoAcceso;
    }

    public String cargarModificarAcceso(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String nuevo = "0";
        String urlnuevoAcceso = "/jsp/extension/melanbide75/nuevoPuesto.jsp?codOrganizacion=" + codOrganizacion;
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
            if (id != null && !id.isEmpty()) {
                ControlAccesoVO datModif = MeLanbide75Manager.getInstance().getControlAccesoPorID(id, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (datModif != null) {
                    request.setAttribute("datModif", datModif);
                }
            }
            //Cargamos el el request los valores  de los desplegables
         
            List<DesplegableAdmonLocalVO> listaTipoCon1 = MeLanbide75Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide75.COD_DES_TCCR, ConstantesMeLanbide75.FICHERO_PROPIEDADES), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            if (!listaTipoCon1.isEmpty()) {
                listaTipoCon1 = traducirDesplegable(request, listaTipoCon1);
                request.setAttribute("listaTipoCon1", listaTipoCon1);
            }            
        } catch (Exception ex) {
            log.error("Error al tratar de preparar los datos para modificar y llamar la jsp de modificacion : " + ex.getMessage());
        }
        return urlnuevoAcceso;
    }

    public void eliminarAcceso(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<ControlAccesoVO> lista = new ArrayList<ControlAccesoVO>();
        String numExp = "";
        try {
            String id = request.getParameter("id");
            if (id == null || id.isEmpty()) {
                log.error("No se ha recibido desde la JSP el id del Puesto a Elimnar ");
                codigoOperacion = "3";
            } else {
                numExp = request.getParameter("numExp");
                AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                int result = MeLanbide75Manager.getInstance().eliminarAcceso(id, adapt);
                if (result <= 0) {
                    codigoOperacion = "1";
                } else {
                    codigoOperacion = "0";
                    try {
                        lista = MeLanbide75Manager.getInstance().getDatosControlAcceso(numExp, codOrganizacion, adapt);
                    } catch (Exception ex) {
                        log.debug("Error al recuperar la lista de acceso despues de eliminar un Puesto");
                    }
                }
            }
        } catch (Exception ex) {
            log.error("Error eliminando un Puesto: " + ex);
            codigoOperacion = "2";
        }
        String xmlSalida = null;
        xmlSalida = obtenerXmlSalidaAcceso(request, codigoOperacion, lista);
        retornarXML(xmlSalida, response);
    }

    public void crearNuevoAcceso(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<ControlAccesoVO> lista = new ArrayList<ControlAccesoVO>();
        ControlAccesoVO nuevoAcceso = new ControlAccesoVO();
        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            
            String numExp = request.getParameter("expediente");
            
            //String cnae = (String) request.getParameter("cnae");
            String puesto = request.getParameter("puesto");
            String nombre = request.getParameter("nombre");
            String dni_nif = request.getParameter("nif");
            String grado = request.getParameter("porDisc").replace(",", ".");
            String codTipoCon1 = request.getParameter("tipoCon");
            String conDesde = request.getParameter("conDesde");
            String conHasta = request.getParameter("conHasta");
            String totalDias = request.getParameter("totalDias");
            
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        
            nuevoAcceso.setNumExp(numExp);
            
            //nuevoAcceso.setCnae(cnae);
            nuevoAcceso.setPuesto(puesto);
            nuevoAcceso.setNombre(nombre);
            nuevoAcceso.setNif(dni_nif);
            if (grado != null && !"".equals(grado)) {
                nuevoAcceso.setPorDisc(Double.valueOf(grado));
            }
            nuevoAcceso.setTipoCon(codTipoCon1);
            if (conDesde != null && !"".equals(conDesde)) {
                nuevoAcceso.setConDesde(new java.sql.Date(formatoFecha.parse(conDesde).getTime()));
            }
            if (conHasta != null && !"".equals(conHasta)) {
                nuevoAcceso.setConHasta(new java.sql.Date(formatoFecha.parse(conHasta).getTime()));
            }
            if (totalDias != null && !"".equals(totalDias)) {
                nuevoAcceso.setTotalDias(Integer.valueOf(totalDias));
            }
            
            MeLanbide75Manager meLanbide75Manager = MeLanbide75Manager.getInstance();
            boolean insertOK = meLanbide75Manager.crearNuevoAcceso(nuevoAcceso, adapt);
            if (insertOK) {
                log.debug("Puesto Insertado Correctamente");
                lista = meLanbide75Manager.getDatosControlAcceso(numExp, codOrganizacion, adapt);

            } else {
                log.error("NO se ha insertado correctamente el nuevo Puesto");
                codigoOperacion = "1";
            }
        } catch (Exception ex) {
            log.error("Error al parsear los parametros recibidos del jsp al objeto ControlAccesoVO" + ex.getMessage());
            codigoOperacion = "2";
        }
        String xmlSalida = null;
        xmlSalida = obtenerXmlSalidaAcceso(request, codigoOperacion, lista);
        retornarXML(xmlSalida, response);
    }

    public void modificarAcceso(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<ControlAccesoVO> lista = new ArrayList<ControlAccesoVO>();
        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            //Recojo los parametros
            String id = request.getParameter("id");        
            String numExp = request.getParameter("expediente");            
            //String cnae = (String) request.getParameter("cnae");
            String puesto = request.getParameter("puesto");
            String nombre = request.getParameter("nombre");
            String dni_nif = request.getParameter("nif");
            String grado = request.getParameter("porDisc").replace(",", ".");
            String codTipoCon1 = request.getParameter("tipoCon");
            String conDesde = request.getParameter("conDesde");
            String conHasta = request.getParameter("conHasta");
            String totalDias = request.getParameter("totalDias");

            if (id == null || id.isEmpty()) {
                log.error("No se ha recibido desde la JSP el id del Puesto a Modificar ");
                codigoOperacion = "3";
            } else {
                ControlAccesoVO datModif = MeLanbide75Manager.getInstance().getControlAccesoPorID(id, adapt);
                numExp = datModif.getNumExp();
                datModif.setId(Integer.valueOf(id));                              
                datModif.setNumExp(numExp);
                //datModif.setCnae(cnae);
                datModif.setPuesto(puesto);
                datModif.setNombre(nombre);
                datModif.setNif(dni_nif);
                if (grado != null && !"".equals(grado)) {
                    datModif.setPorDisc(Double.valueOf(grado));
                }
                datModif.setTipoCon(codTipoCon1);
                if (conDesde != null && !"".equals(conDesde)) {
                    datModif.setConDesde(new java.sql.Date(dateFormat.parse(conDesde).getTime()));
                }
                if (conHasta != null && !"".equals(conHasta)) {
                    datModif.setConHasta(new java.sql.Date(dateFormat.parse(conHasta).getTime()));
                }
                if (totalDias != null && !"".equals(totalDias)) {
                    datModif.setTotalDias(Integer.valueOf(totalDias));
                }

                MeLanbide75Manager meLanbide75Manager = MeLanbide75Manager.getInstance();
                boolean modOK = meLanbide75Manager.modificarAcceso(datModif, adapt);
                if (modOK) {
                    try {
                        lista = meLanbide75Manager.getDatosControlAcceso(numExp, codOrganizacion, adapt);
                    } catch (BDException bde) {
                        codigoOperacion = "1";
                        log.error("Error de tipo BD al recuperar la lista de Puestos después de Modificar un puesto : " + bde.getMensaje());
                    } catch (Exception ex) {
                        codigoOperacion = "2";
                        log.error("Error al recuperar la lista de Puestos después de Modificar un puesto : " + ex.getMessage());
                    }
                } else {
                    codigoOperacion = "2";
                }
            }
        } catch (Exception ex) {
            log.error("Error modificar --- ", ex);
            codigoOperacion = "2";
        }
        
        String xmlSalida = null;
        xmlSalida = obtenerXmlSalidaAcceso(request, codigoOperacion, lista);
        retornarXML(xmlSalida, response);
    }

    // Funciones Privadas
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
    
    
    private String getDescripcionDesplegable(HttpServletRequest request, String descripcionCompleta) {
        String descripcion = descripcionCompleta;        
        String barraSeparadoraDobleIdiomaDesple = ConfigurationParameter.getParameter(ConstantesMeLanbide75.BARRA_SEPARADORA_IDIOMA_DESPLEGABLES, ConstantesMeLanbide75.FICHERO_PROPIEDADES);        
        try {
            if (!descripcion.isEmpty()) {
                String[] descripcionDobleIdioma = (descripcion != null ? descripcion.split(barraSeparadoraDobleIdiomaDesple) : null);
                if (descripcionDobleIdioma != null && descripcionDobleIdioma.length > 1) {
                    if (getIdioma(request) == ConstantesMeLanbide75.CODIGO_IDIOMA_EUSKERA) {
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
        int idioma = ConstantesMeLanbide75.CODIGO_IDIOMA_CASTELLANO;  // Por Defecto 1 Castellano
        try {
            if (request != null && request.getSession() != null) {
                usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
                if (usuario != null) {
                    idioma = usuario.getIdioma();
                }
            }
        } catch (Exception ex) {
            log.error("Error al recuperar el idioma del usuario de la request, asignamos por defecto 1 Castellano", ex);
            idioma = ConstantesMeLanbide75.CODIGO_IDIOMA_CASTELLANO;
        }
        return idioma;
    }
    

    public void generarExcelAnexoC(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String rutaArchivoSalida = null;
        List<ControlAccesoVO> listaAccesos = null;
        try {
            listaAccesos = (List<ControlAccesoVO>) request.getAttribute("listaAccesos");
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

            MeLanbide75I18n meLanbide75I18n = MeLanbide75I18n.getInstance();
            MeLanbide75Manager manager = MeLanbide75Manager.getInstance();
            String numExpe = "";
            try {
                numExpe = request.getParameter("numExp");
                try {
                    listaAccesos = manager.getRegistrosAcceso(numExpe, adapt);
                    if (listaAccesos.isEmpty()) {
                        meLanbide75I18n.getMensaje(idioma, "error.errorGen");
                    }
                } catch (Exception ex) {
                    log.error("Error al recuperar datos del Plantilla");
                }

            } catch (Exception ex) {
                log.error("Error recuperando datos de Plantilla : " + ex);
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
                    e.printStackTrace();
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

                HSSFSheet hoja = libro.createSheet("Datos puestos");

                //Se establece el ancho de cada columnas
                hoja.setColumnWidth( 0,  8000);//Denominación puesto
                hoja.setColumnWidth( 1,  8000);//Apellidos y nombre
                hoja.setColumnWidth( 2,  3000);//NIF
                hoja.setColumnWidth( 3,  3000);//Porcentaje discapacidad
                hoja.setColumnWidth( 4,  8000);//Tipo de contrato
                hoja.setColumnWidth( 5,  3000);//Fecha contrato desde
                hoja.setColumnWidth( 6,  3000);//Fecha contrato hasta
                hoja.setColumnWidth( 7,  3000);//Total días

                fila = hoja.createRow(numFila);
                //Se establece el alto de las columnas
                fila.setHeight((short) 750);

                crearEstiloInformeDatosAnexoC(libro, fila, celda, estiloCelda, idioma);

                boolean nuevo = true;
                int p = 0;
                int n = 0;
                int height = 0;

                String numExp = null;

                int maxLengthNombre = 0;

                //Insertamos los datos, fila a fila
                for (ControlAccesoVO filaI : listaAccesos) {
                    if (filaI.getNumExp() != null && !filaI.getNumExp().equals(numExp)) {
                        numExp = filaI.getNumExp();
                        nuevo = true;
                        p = 0;
                        n++;
                        numFila++;
                    } else {
                        numFila++;
                        nuevo = false;
                        p++;
                    }

                    fila = hoja.createRow(numFila);

                    crearDatosInformeAnexoC(libro, fila, celda, estiloCelda, idioma, filaI, numExp, numFila, nuevo, n, listaAccesos, normal, codOrganizacion, adapt, dateFormat);

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
                log.error("EXCEPCION informe resumenPuestosContratados");
            } finally {
                if (istr != null) {
                    istr.close();
                }
            }
        } catch (IOException ex) {
            log.error("EXCEPCION informe resumenPuestosContratados");

        } catch (SQLException ex) {
            log.error("EXCEPCION informe resumenPuestosContratados");
        }
    }

    private void crearEstiloInformeDatosAnexoC(HSSFWorkbook libro, HSSFRow fila, HSSFCell celda, HSSFCellStyle estiloCelda, int idioma) {
        try {
            MeLanbide75I18n meLanbide75I18n = MeLanbide75I18n.getInstance();

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
                celda = fila.createCell( 0);
                celda.setCellValue(meLanbide75I18n.getMensaje(idioma, "puesto.tablaPuestos.col3").toUpperCase());
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
                celda = fila.createCell( 1);
                celda.setCellValue(meLanbide75I18n.getMensaje(idioma, "puesto.tablaPuestos.col4").toUpperCase());
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
                celda = fila.createCell( 2);
                celda.setCellValue(meLanbide75I18n.getMensaje(idioma, "puesto.tablaPuestos.col5").toUpperCase());
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
                celda = fila.createCell( 3);
                celda.setCellValue(meLanbide75I18n.getMensaje(idioma, "puesto.tablaPuestos.col6").toUpperCase());
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
                celda = fila.createCell( 4);
                celda.setCellValue(meLanbide75I18n.getMensaje(idioma, "puesto.tablaPuestos.col7").toUpperCase());
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
                celda = fila.createCell( 5);
                celda.setCellValue(meLanbide75I18n.getMensaje(idioma, "puesto.tablaPuestos.col8").toUpperCase());
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
                celda = fila.createCell( 6);
                celda.setCellValue(meLanbide75I18n.getMensaje(idioma, "puesto.tablaPuestos.col9").toUpperCase());
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
                celda = fila.createCell( 7);
                celda.setCellValue(meLanbide75I18n.getMensaje(idioma, "puesto.tablaPuestos.col10").toUpperCase());
                celda.setCellStyle(estiloCelda);

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception ex) {

        }

    }

    private void crearDatosInformeAnexoC(HSSFWorkbook libro, HSSFRow fila, HSSFCell celda, HSSFCellStyle estiloCelda, int idioma, ControlAccesoVO filaI,
            String numExp, int numFila, boolean nuevo, int n, List<ControlAccesoVO> listaAccesos,
            HSSFFont normal, int codOrganizacion, AdaptadorSQLBD adapt, DateFormat format) {
        try {
            String empresa = null;
            String idiomas = null;
            String pais = null;
            String titulacion = null;

            //COLUMNA: DENOMINACIÓN PUESTO
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            if (n == 1) {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            } else {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            if (numFila == listaAccesos.size()) {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell( 0);
            celda.setCellValue(filaI.getPuesto() != null ? String.valueOf(filaI.getPuesto()) : "");
            celda.setCellStyle(estiloCelda);

            //COLUMNA: NOMBRE
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);

            if (n == 1) {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            } else {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }

            if (numFila == listaAccesos.size()) {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell( 1);
            celda.setCellValue((filaI.getNombre() != null ? filaI.getNombre().toUpperCase() : ""));
            celda.setCellStyle(estiloCelda);

            //COLUMNA: NIF
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            if (n == 1) {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            } else {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            if (numFila == listaAccesos.size()) {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell( 2);
            celda.setCellValue(filaI.getNif() != null ? filaI.getNif() : "");
            celda.setCellStyle(estiloCelda);
            
            //COLUMNA: GRADO DISC.
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            if (n == 1) {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            } else {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            if (numFila == listaAccesos.size()) {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell( 3);
            celda.setCellValue(filaI.getPorDisc() != null ? String.valueOf(filaI.getPorDisc()) : "");
            celda.setCellStyle(estiloCelda);
            
            //COLUMNA: TIPO CONTRATO
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            if (n == 1) {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            } else {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            if (numFila == listaAccesos.size()) {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell( 4);
            celda.setCellValue(filaI.getDesTipoCon() != null ? String.valueOf(filaI.getDesTipoCon()) : "");
            celda.setCellStyle(estiloCelda);

            //COLUMNA: FECHA CONTRATO DESDE
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            if (n == 1) {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            } else {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            if (numFila == listaAccesos.size()) {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell( 5);
            celda.setCellValue(filaI.getConDesde() != null ? dateFormat.format(filaI.getConDesde()) : "");
            celda.setCellStyle(estiloCelda);
            
            //COLUMNA: FECHA CONTRATO HASTA
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            if (n == 1) {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            } else {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            if (numFila == listaAccesos.size()) {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell( 6);
            celda.setCellValue(filaI.getConHasta() != null ? dateFormat.format(filaI.getConHasta()) : "");
            celda.setCellStyle(estiloCelda);
            
            //COLUMNA: TOTAL DÍAS
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            if (n == 1) {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            } else {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            if (numFila == listaAccesos.size()) {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell( 7);
            celda.setCellValue(filaI.getTotalDias()!= null ? String.valueOf(filaI.getTotalDias()) : "");
            celda.setCellStyle(estiloCelda);

        } catch (Exception ex) {
            log.error("Error: " + ex);
        }

    }

    private String bytesToHex(byte[] bytes) {
        char[] hexArray = "0123456789ABCDEF".toCharArray();

        char[] hexChars = new char[bytes.length * 2];

        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
    
    // ----------------------------------------------------------------------------------------------------------
    // ---------------    XML    --------------------------------------------------------------------------------
    // ----------------------------------------------------------------------------------------------------------
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
    
    private String obtenerXmlSalidaAcceso(HttpServletRequest request, String codigoOperacion, List<ControlAccesoVO> lista) {
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
            /*xmlSalida.append("<CNAE>");
            xmlSalida.append(fila.getCnae());
            xmlSalida.append("</CNAE>");*/
            xmlSalida.append("<DENOMPUESTO>");
            xmlSalida.append(fila.getPuesto());
            xmlSalida.append("</DENOMPUESTO>");
            xmlSalida.append("<NOMBRE>");
            xmlSalida.append(fila.getNombre());
            xmlSalida.append("</NOMBRE>");
            xmlSalida.append("<NIFCIF>");
            xmlSalida.append(fila.getNif());
            xmlSalida.append("</NIFCIF>");
            xmlSalida.append("<GRADO>");
            if (fila.getPorDisc() != null && !"".equals(fila.getPorDisc())) {
                xmlSalida.append(fila.getPorDisc());
            } else {
                xmlSalida.append("null");
            }
            xmlSalida.append("</GRADO>");
            xmlSalida.append("<TIPOCONTRATO>");
            xmlSalida.append(getDescripcionDesplegable(request, fila.getDesTipoCon()));
            xmlSalida.append("</TIPOCONTRATO>");
            xmlSalida.append("<CONTRATODESDE>");
            if (fila.getConDesde() != null) {
                xmlSalida.append(dateFormat.format(fila.getConDesde()));
            } else {
                xmlSalida.append("");
            }
            xmlSalida.append("</CONTRATODESDE>");
            xmlSalida.append("<CONTRATOHASTA>");
            if (fila.getConHasta() != null) {
                xmlSalida.append(dateFormat.format(fila.getConHasta()));
            } else {
                xmlSalida.append("");
            }
            xmlSalida.append("</CONTRATOHASTA>");
            xmlSalida.append("<TOTALDIAS>");
            if (fila.getTotalDias() != null && !"".equals(fila.getTotalDias())) {
                xmlSalida.append(fila.getTotalDias());
            } else {
                xmlSalida.append("null");
            }
            xmlSalida.append("</TOTALDIAS>");            
            xmlSalida.append("</FILA>");
        }
        xmlSalida.append("</RESPUESTA>");
        log.debug("xml: " + xmlSalida);
        return xmlSalida.toString();
    }
        
}
