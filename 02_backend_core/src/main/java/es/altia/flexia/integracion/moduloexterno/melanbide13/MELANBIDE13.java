package es.altia.flexia.integracion.moduloexterno.melanbide13;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.melanbide13.i18n.MeLanbide13I18n;
import es.altia.flexia.integracion.moduloexterno.melanbide13.manager.MeLanbide13Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide13.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide13.util.ConstantesMeLanbide13;
import es.altia.flexia.integracion.moduloexterno.melanbide13.vo.ListaExpedientesVO;
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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
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

public class MELANBIDE13 extends ModuloIntegracionExterno {

    private static Logger log = LogManager.getLogger(MELANBIDE13.class);
    private final MeLanbide13Manager m13Manager = new MeLanbide13Manager();
    ResourceBundle m_Conf = ResourceBundle.getBundle("common");

    public String cargarPantallaPrincipal(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarPantallaPrincipal de " + this.getClass().getName());
        AdaptadorSQLBD adapt = null;
        Double costeTotal = 0.0;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (SQLException ex) {
            log.error(this.getClass().getName() + " Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }
        String url = "/jsp/extension/melanbide13/melanbide13.jsp";
        request.setAttribute("numExp", numExpediente);

        if (adapt != null) {
            try {
                String anioAyuda = m13Manager.getValorDesplegableExpediente(codOrganizacion, numExpediente, ConfigurationParameter.getParameter(ConstantesMeLanbide13.COD_ANIO, ConstantesMeLanbide13.FICHERO_PROPIEDADES), adapt);
                List<ListaExpedientesVO> listaExpedientes = m13Manager.getListaExpedientes(numExpediente, anioAyuda, adapt);
                if (!listaExpedientes.isEmpty()) {
                    for (ListaExpedientesVO listaExpediente : listaExpedientes) {
                        if (listaExpediente.getImporte() != null) {
                            costeTotal += listaExpediente.getImporte();
                        }
                    }
                    log.debug("costeTotal: " + costeTotal);

                    request.setAttribute("listaExpedientes", listaExpedientes);
                    request.setAttribute("costeTotal", costeTotal.toString());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                log.error("Error al recuperar los datos de Expedientes - MELANBIDE13 - cargarPantallaPrincipal", ex);
            }
        }

        return url;
    }

    // Funciones Privadas
    private AdaptadorSQLBD getAdaptSQLBD(String codOrganizacion) throws SQLException {
        ResourceBundle config = ResourceBundle.getBundle("techserver");
        String gestor = config.getString("CON.gestor");
        String jndiGenerico = config.getString("CON.jndi");
        Connection conGenerico = null;
        Statement st = null;
        ResultSet rs = null;
        String[] salida = null;

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

        }// synchronized
        return adapt;
    }//getConnection

    public void generarExcelListaExpedientes(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en generarExcelListaExpedientes de " + this.getClass().getSimpleName());
        String rutaArchivoSalida = null;
        List<ListaExpedientesVO> listaExpedientes = null;
        Connection con = null;
        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            Double total = 0.0;
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

            MeLanbide13I18n meLanbide13I18n = MeLanbide13I18n.getInstance();
            NumberFormat formatNumber = NumberFormat.getCurrencyInstance(Locale.ITALY);
            String numExpe = "";

            try {
                numExpe = request.getParameter("numExp");
                try {
                    String anioAyuda = m13Manager.getValorDesplegableExpediente(codOrganizacion, numExpe, ConfigurationParameter.getParameter(ConstantesMeLanbide13.COD_ANIO, ConstantesMeLanbide13.FICHERO_PROPIEDADES), adapt);
                    listaExpedientes = m13Manager.getListaExpedientes(numExpe, anioAyuda, adapt);
                    if (listaExpedientes.isEmpty()) {
                        meLanbide13I18n.getMensaje(idioma, "error.errorGen");
                    }
                } catch (Exception ex) {
                    log.error("Error al recuperar datos del ListaExpedientes");
                }

            } catch (Exception ex) {
                log.error("Error recuperando datos de ListaExpedientes : " + ex);
            } finally {
                try {
                    adapt.devolverConexion(con);
                } catch (BDException e) {
                    log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
                }
            }

            FileInputStream istr = null;
            try {
                HSSFWorkbook libro = new HSSFWorkbook();

                HSSFPalette palette = libro.getCustomPalette();
                HSSFColor hssfColor = null;
                try {
                    hssfColor = palette.findColor((byte) 166, (byte) 25, (byte) 46);
                    if (hssfColor == null) {
                        log.error("No Color");
                        hssfColor = palette.getColor(HSSFColor.DARK_RED.index);
                    }
                } catch (Exception e) {
                }

                HSSFFont normal = libro.createFont();
                normal.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
                normal.setFontHeight((short) 150);

                HSSFFont negritaTotal = libro.createFont();
                negritaTotal.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                negritaTotal.setFontHeight((short) 200);
                negritaTotal.setColor(HSSFColor.BLACK.index);

                int numFila = 0;

                HSSFRow fila = null;
                HSSFCell celda = null;
                HSSFCellStyle estiloCelda = null;
                HSSFCellStyle estiloImportes = null;
                HSSFCellStyle estiloTotal = null;

                HSSFSheet hoja = libro.createSheet("Expedientes Relacionados");

                //Se establece el ancho de cada columnas
                //hoja.setColumnWidth((short)0, (short)1500);//ID
                hoja.setColumnWidth(0, 8000);//Nº Expediente
                hoja.setColumnWidth(1, 5000);//Mes
                hoja.setColumnWidth(2, 7000);//Territorio Historico
                hoja.setColumnWidth(3, 3000);//Importe

                fila = hoja.createRow(numFila);
                //Se establece el alto de las columnas
                fila.setHeight((short) 750);
                
               
                crearEstiloListaExpedientes(libro, fila, celda, estiloCelda, idioma);

                int p = 0;
                int n = 0;

                String numExp = null;

                estiloCelda = libro.createCellStyle();
                estiloCelda.setWrapText(true);
                estiloCelda.setFont(normal);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THIN);

                estiloImportes = libro.createCellStyle();
                estiloImportes.setFont(normal);
                estiloImportes.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
                estiloImportes.setBorderRight(HSSFCellStyle.BORDER_THICK);
                estiloImportes.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                estiloImportes.setBorderBottom(HSSFCellStyle.BORDER_THIN);

 estiloTotal = libro.createCellStyle();
                estiloTotal.setFont(negritaTotal);      
                estiloTotal.setAlignment(HSSFCellStyle.ALIGN_CENTER);

                double importe;
                //Insertamos los datos, fila a fila
                for (ListaExpedientesVO filaI : listaExpedientes) {
                    if (filaI.getNumExp() != null && !filaI.getNumExp().equals(numExp)) {
                        numExp = filaI.getNumExp();
                        p = 0;
                        n++;
                        numFila++;
                    } else {
                        numFila++;
                        p++;
                    }
                    log.debug("Fila " + numFila);
                    fila = hoja.createRow(numFila);

                    //COLUMNA: N= EXPEDIENTE
                    celda = fila.createCell(0);
                    celda.setCellValue(filaI.getNumExp() != null ? filaI.getNumExp() : "");
                    celda.setCellStyle(estiloCelda);

                    //COLUMNA: MES
                    celda = fila.createCell(1);
                    celda.setCellValue(filaI.getMes() != null ? String.valueOf(getDescripcionDesplegable(request, filaI.getMes())) : "");
                    celda.setCellStyle(estiloCelda);

                    //COLUMNA: TERRITORIO HISTORICO
                    celda = fila.createCell(2);
                    celda.setCellValue(filaI.getTerritorio() != null ? filaI.getTerritorio() : "");
                    celda.setCellStyle(estiloCelda);

                    //COLUMNA: IMPORTE
                    celda = fila.createCell(3);
                    importe = (filaI.getImporte() != null ? filaI.getImporte() : 0.0);
                    celda.setCellValue(formatNumber.format(importe));
                    celda.setCellStyle(estiloImportes);

                    total += importe;
                    log.info("importe: " + importe);
                }
                log.info("total: " + total);

                numFila++;
                fila = hoja.createRow(numFila);
                celda = fila.createCell(2);
                celda.setCellValue(meLanbide13I18n.getMensaje(idioma, "label.importeTotal").toUpperCase());
                celda.setCellStyle(estiloTotal);

                celda = fila.createCell(3);
                celda.setCellValue(formatNumber.format(total));
                celda.setCellStyle(estiloImportes);

                File directorioTemp = new File(m_Conf.getString("PDF.base_dir"));
                File informe = File.createTempFile("Expedientes_Relacionados", ".xls", directorioTemp);
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
                log.debug("EXCEPCION informe resumenDatoslistaExpedientes");
            } finally {
                if (istr != null) {
                    istr.close();
                }
            }
        } catch (IOException ex) {
            log.debug("EXCEPCION informe resumenDatoslistaExpedientes");
        } catch (SQLException ex) {
            log.debug("EXCEPCION informe resumenDatoslistaExpedientes");
        }
    }

    private void crearEstiloListaExpedientes(HSSFWorkbook libro, HSSFRow fila, HSSFCell celda, HSSFCellStyle estiloCelda, int idioma) {
        log.info("Entramos en crearEstilolistaExpedientes " + this.getClass().getSimpleName());
        try {
            MeLanbide13I18n meLanbide13I18n = MeLanbide13I18n.getInstance();

            HSSFPalette palette = libro.getCustomPalette();
            HSSFColor hssfColor = null;
            try {
                hssfColor = palette.findColor((byte) 166, (byte) 25, (byte) 46);
                if (hssfColor == null) {
                    hssfColor = palette.getColor(HSSFColor.DARK_RED.index);
                }

                HSSFFont negritaTitulo = libro.createFont();
                negritaTitulo.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                negritaTitulo.setFontHeight((short) 170);
                negritaTitulo.setColor(HSSFColor.WHITE.index);
                //celda = fila.

                //cabeceras               
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

                celda = fila.createCell(0);
                celda.setCellValue(meLanbide13I18n.getMensaje(idioma, "tabla.numeroExpediente").toUpperCase());
                celda.setCellStyle(estiloCelda);

                celda = fila.createCell(1);
                celda.setCellValue(meLanbide13I18n.getMensaje(idioma, "tabla.mes").toUpperCase());
                celda.setCellStyle(estiloCelda);

                celda = fila.createCell(2);
                celda.setCellValue(meLanbide13I18n.getMensaje(idioma, "tabla.territorio").toUpperCase());
                celda.setCellStyle(estiloCelda);

                celda = fila.createCell(3);
                celda.setCellValue(meLanbide13I18n.getMensaje(idioma, "tabla.importe").toUpperCase());
                celda.setCellStyle(estiloCelda);

            } catch (Exception e) {
            }

        } catch (Exception ex) {

        }

    }

    /**
     * Método que retorna el valor de un desplegable en el idiona del usuario
     *
     * @param request
     * @param descripcionCompleta
     * @return String con el valor
     */
    public String getDescripcionDesplegable(HttpServletRequest request, String descripcionCompleta) {
        String descripcion = descripcionCompleta;
        String barraSeparadoraDobleIdiomaDesple = ConfigurationParameter.getParameter(ConstantesMeLanbide13.BARRA_SEPARADORA_IDIOMA, ConstantesMeLanbide13.FICHERO_PROPIEDADES);
        try {
            if (!descripcion.isEmpty()) {
                String[] descripcionDobleIdioma = descripcion.split(barraSeparadoraDobleIdiomaDesple);
                if (descripcionDobleIdioma != null && descripcionDobleIdioma.length > 1) {
                    if (getIdioma(request) == ConstantesMeLanbide13.CODIGO_IDIOMA_EUSKERA) {
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

    /**
     * Método que recupera el Idioma de la request para la gestion de
     * Desplegables
     *
     * @param request
     * @return int idioma 1-castellano 4-euskera
     */
    public int getIdioma(HttpServletRequest request) {
        // Recuperamos el Idioma de la request para la gestion de Desplegables
        UsuarioValueObject usuario = new UsuarioValueObject();
        int idioma = ConstantesMeLanbide13.CODIGO_IDIOMA_CASTELLANO;  // Por Defecto 1 Castellano
        try {
            if (request != null && request.getSession() != null) {
                usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
                if (usuario != null) {
                    idioma = usuario.getIdioma();
                }
            }
        } catch (Exception ex) {
            log.error("Error al recuperar el idioma del usuario de la request, asignamos por defecto 1 Castellano", ex);
            idioma = ConstantesMeLanbide13.CODIGO_IDIOMA_CASTELLANO;
        }
        return idioma;
    }

    /**
     * Método llamado para devolver un String en formato JSON al cliente que ha
     * realiza la petición a alguna de las operaciones de este action
     *
     * @param json: String que contiene el JSON a devolver
     * @param response: Objeto de tipo HttpServletResponse a través del cual se
     * devuelve la salida al cliente que ha realizado la solicitud
     */
    private void retornarJSON(String json, HttpServletResponse response) {
        try {
            if (json != null) {
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                out.print(json);
                out.flush();
                out.close();
            }
        } catch (IOException e) {
        }
    }

}
